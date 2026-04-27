"""
Debug - Check what fields are returned in success_records
"""

import sys
if sys.platform == 'win32':
    import codecs
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.buffer, 'strict')
    sys.stderr = codecs.getwriter('utf-8')(sys.stderr.buffer, 'strict')

from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Insert one record
test_record = [{"Name": "Debug Account"}]
result = client.bulk.insert("Account", test_record, wait=True)

print(f"Success count: {result.success_count}")
print(f"Success records: {result.success_records}")
print()

if result.success_records:
    print(f"First record keys: {result.success_records[0].keys()}")
    print(f"First record values: {result.success_records[0]}")
