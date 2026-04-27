"""
Debug script - Check job response details and test standard API
"""

import sys
if sys.platform == 'win32':
    import codecs
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.buffer, 'strict')
    sys.stderr = codecs.getwriter('utf-8')(sys.stderr.buffer, 'strict')

from kinetic_core import JWTAuthenticator, SalesforceClient
import requests
import json
import time

# Authenticate
print("Authenticating...")
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

print(f"Connected to: {session.instance_url}\n")

# First, test standard API to make sure we can create records
print("="*60)
print("TEST 1: Standard API Insert (for comparison)")
print("="*60)

try:
    result = client.create("Account", {"Name": "Standard API Test Account"})
    print(f"✓ Standard API works!")
    print(f"  Created Account ID: {result['id']}")

    # Clean up
    client.delete("Account", result['id'])
    print(f"  Cleaned up test account\n")
except Exception as e:
    print(f"❌ Standard API failed: {e}\n")

# Now test Bulk API with detailed job inspection
print("="*60)
print("TEST 2: Bulk API Insert with Detailed Job Info")
print("="*60)

from kinetic_core.bulk import BulkOperation

test_record = [{"Name": "Bulk API Test Account"}]
csv_data = client.bulk.serializer.records_to_csv(test_record)

print(f"CSV to upload:\n---\n{csv_data}---\n")

try:
    # Create job
    job = client.bulk._create_job(BulkOperation.INSERT, "Account")
    print(f"✓ Job created: {job.id}")

    # Upload data
    client.bulk._upload_data(job.id, csv_data)
    print(f"✓ Data uploaded")

    # Close job
    job = client.bulk._close_job(job.id)
    print(f"✓ Job closed (State: {job.state})")

    # Poll for completion
    print(f"\nPolling for job completion...")
    for i in range(10):  # Max 10 attempts
        time.sleep(2)
        job_url = f"{client.bulk.base_url}/{job.id}"
        response = requests.get(job_url, headers=client.bulk._get_headers())

        if response.ok:
            job_data = response.json()
            print(f"\n  Attempt {i+1}:")
            print(f"    State: {job_data.get('state')}")
            print(f"    Records Processed: {job_data.get('numberRecordsProcessed', 0)}")
            print(f"    Records Failed: {job_data.get('numberRecordsFailed', 0)}")

            # Print full response if failed
            if job_data.get('state') == 'Failed':
                print(f"\n  Full job response:")
                print(json.dumps(job_data, indent=2))

                # Check for system modstamp permission issue
                if 'errorMessage' in job_data:
                    print(f"\n  Error Message: {job_data['errorMessage']}")

                break

            if job_data.get('state') in ['JobComplete', 'Failed', 'Aborted']:
                break
        else:
            print(f"  ❌ Error getting job status: {response.status_code}")
            break

except Exception as e:
    print(f"\n❌ Error: {e}")
    import traceback
    traceback.print_exc()
