"""
Debug script to investigate Bulk API job failures
"""

import sys
if sys.platform == 'win32':
    import codecs
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.buffer, 'strict')
    sys.stderr = codecs.getwriter('utf-8')(sys.stderr.buffer, 'strict')

from kinetic_core import JWTAuthenticator, SalesforceClient
import requests

# Authenticate
print("Authenticating...")
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

print(f"Connected to: {session.instance_url}")
print(f"Bulk API URL: {client.bulk.base_url}\n")

# Try a simple insert
print("Creating a bulk insert job...")
test_record = [{"Name": "Debug Test Account"}]

try:
    # Convert to CSV
    csv_data = client.bulk.serializer.records_to_csv(test_record)
    print(f"CSV Data:\n{csv_data}\n")

    # Create job
    from kinetic_core.bulk import BulkOperation
    job = client.bulk._create_job(BulkOperation.INSERT, "Account")
    print(f"✓ Job created: {job.id}")
    print(f"  State: {job.state}")
    print(f"  Operation: {job.operation}")
    print(f"  Object: {job.object}\n")

    # Upload data
    print("Uploading data...")
    client.bulk._upload_data(job.id, csv_data)
    print(f"✓ Data uploaded\n")

    # Close job
    print("Closing job...")
    job = client.bulk._close_job(job.id)
    print(f"✓ Job closed")
    print(f"  State: {job.state}\n")

    # Check status after a moment
    import time
    print("Waiting 3 seconds...")
    time.sleep(3)

    job = client.bulk._get_job_status(job.id)
    print(f"Current status:")
    print(f"  State: {job.state}")
    print(f"  Records processed: {job.number_records_processed}")

    if job.state == "Failed":
        print(f"\n❌ Job failed!")
        print(f"  Error message: {job.error_message if hasattr(job, 'error_message') else 'N/A'}")

        # Try to get failed results for more details
        print(f"\nFetching failed results...")
        try:
            failed_url = f"{client.bulk.base_url}/{job.id}/failedResults"
            response = requests.get(failed_url, headers=client.bulk._get_headers())
            print(f"  Response status: {response.status_code}")
            if response.ok:
                print(f"  Failed results:\n{response.text}")
        except Exception as e:
            print(f"  Error getting failed results: {e}")

    # Abort the job to clean up
    print(f"\nAborting job...")
    client.bulk.abort_job(job.id)
    print(f"✓ Job aborted")

except Exception as e:
    print(f"\n❌ Error: {e}")
    import traceback
    traceback.print_exc()
