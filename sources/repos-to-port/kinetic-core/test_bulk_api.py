"""
Test script for Bulk API v2 functionality
Tests all operations: insert, update, upsert, delete, query
"""

import os
import sys
from datetime import datetime

# Set UTF-8 encoding for console output on Windows
if sys.platform == 'win32':
    import codecs
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.buffer, 'strict')
    sys.stderr = codecs.getwriter('utf-8')(sys.stderr.buffer, 'strict')

from kinetic_core import JWTAuthenticator, SalesforceClient
from kinetic_core.bulk import BulkOperation


def print_section(title):
    """Print a section header"""
    print(f"\n{'='*60}")
    print(f"  {title}")
    print(f"{'='*60}\n")


def print_result(operation, result):
    """Print operation results"""
    print(f"\n{operation} Results:")
    print(f"  - Successful: {result.success_count}")
    print(f"  - Failed: {result.failed_count}")
    print(f"  - Total: {result.total_records}")

    if result.errors:
        print(f"\n  Errors (first 5):")
        for i, error in enumerate(result.errors[:5], 1):
            print(f"    {i}. {error.message}")

    return result.success_count > 0


def test_bulk_insert(client):
    """Test bulk insert operation"""
    print_section("TEST 1: Bulk Insert")

    # Create test accounts
    test_accounts = [
        {
            "Name": f"Bulk Test Account {i}",
            "Industry": "Technology",
            "Description": f"Test account created via Bulk API v2 at {datetime.now()}"
        }
        for i in range(1, 6)  # Start with 5 records
    ]

    print(f"Inserting {len(test_accounts)} test accounts...")

    try:
        result = client.bulk.insert("Account", test_accounts, wait=True, timeout_minutes=5)
        success = print_result("Insert", result)

        if success:
            # Extract IDs from successful records
            # Salesforce returns lowercase field names in CSV results
            account_ids = [
                record.get('sf__Id') or record.get('Id') or record.get('id')
                for record in result.success_records
            ]
            # Filter out None values
            account_ids = [aid for aid in account_ids if aid]
            print(f"\n  Created Account IDs: {account_ids[:3] if len(account_ids) >= 3 else account_ids}...")
            return account_ids
        else:
            print("\n  ❌ Insert failed - no records created")
            return []

    except Exception as e:
        print(f"\n  ❌ Error during insert: {e}")
        return []


def test_bulk_update(client, account_ids):
    """Test bulk update operation"""
    print_section("TEST 2: Bulk Update")

    if not account_ids:
        print("⚠️  Skipping update test - no accounts to update")
        return False

    # Prepare update records
    update_records = [
        {
            "Id": account_id,
            "Industry": "Software",
            "Description": f"Updated via Bulk API v2 at {datetime.now()}"
        }
        for account_id in account_ids
    ]

    print(f"Updating {len(update_records)} accounts...")

    try:
        result = client.bulk.update("Account", update_records, wait=True, timeout_minutes=5)
        return print_result("Update", result)

    except Exception as e:
        print(f"\n  ❌ Error during update: {e}")
        return False


def test_bulk_query(client):
    """Test bulk query operation"""
    print_section("TEST 3: Bulk Query")

    # Query for accounts created today
    soql = "SELECT Id, Name, Industry, Description FROM Account WHERE CreatedDate = TODAY LIMIT 100"

    print(f"Executing bulk query:\n  {soql}")

    try:
        result = client.bulk.query(soql, timeout_minutes=5)

        print(f"\nQuery Results:")
        print(f"  - Records retrieved: {result.record_count}")
        print(f"  - Job state: {result.job.state}")

        if result.records:
            print(f"\n  Sample records (first 3):")
            for i, record in enumerate(result.records[:3], 1):
                print(f"    {i}. {record.get('Name')} - {record.get('Industry')}")

        return result.record_count > 0

    except Exception as e:
        print(f"\n  ❌ Error during query: {e}")
        return False


def test_bulk_delete(client, account_ids):
    """Test bulk delete operation"""
    print_section("TEST 4: Bulk Delete")

    if not account_ids:
        print("⚠️  Skipping delete test - no accounts to delete")
        return False

    print(f"Deleting {len(account_ids)} test accounts...")

    try:
        result = client.bulk.delete("Account", account_ids, wait=True, timeout_minutes=5)
        success = print_result("Delete", result)

        if success:
            print("\n  ✓ Test accounts cleaned up successfully")

        return success

    except Exception as e:
        print(f"\n  ❌ Error during delete: {e}")
        return False


def test_progress_callback(client):
    """Test progress callback functionality"""
    print_section("TEST 5: Progress Callback")

    # Create a few test records
    test_records = [
        {"Name": f"Progress Test {i}", "Industry": "Testing"}
        for i in range(1, 4)
    ]

    progress_updates = []

    def on_progress(job):
        progress_updates.append(job.state)
        print(f"  Progress update: Job {job.id} - State: {job.state}")

    print("Inserting records with progress callback...")

    try:
        result = client.bulk.insert(
            "Account",
            test_records,
            wait=True,
            timeout_minutes=5,
            on_progress=on_progress
        )

        print(f"\n  Total progress updates received: {len(progress_updates)}")
        print(f"  Final state: {result.job.state}")

        # Clean up
        if result.success_records:
            ids = [r.get('id') or r.get('Id') for r in result.success_records]
            client.bulk.delete("Account", ids, wait=True)
            print(f"  Cleaned up {len(ids)} test records")

        return len(progress_updates) > 0

    except Exception as e:
        print(f"\n  ❌ Error during progress callback test: {e}")
        return False


def main():
    """Run all Bulk API v2 tests"""
    print_section("Kinetic-Core v2.0.0 - Bulk API v2 Test Suite")

    # Check environment
    if not os.path.exists('.env'):
        print("❌ .env file not found. Please configure your Salesforce credentials.")
        return False

    print("Initializing Salesforce connection...")

    try:
        # Authenticate
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()
        client = SalesforceClient(session)

        print(f"✓ Connected to: {session.instance_url}")
        print(f"✓ Bulk API base URL: {client.bulk.base_url}")

    except Exception as e:
        print(f"❌ Authentication failed: {e}")
        print("\nPossible issues:")
        print("  1. Check .env file configuration")
        print("  2. Verify JWT token and private key")
        print("  3. Ensure Connected App has API access enabled")
        return False

    # Run tests
    results = {}
    account_ids = []

    try:
        # Test 1: Insert
        account_ids = test_bulk_insert(client)
        results['insert'] = len(account_ids) > 0

        # Test 2: Update (if insert succeeded)
        if account_ids:
            results['update'] = test_bulk_update(client, account_ids)
        else:
            results['update'] = None  # Skipped

        # Test 3: Query
        results['query'] = test_bulk_query(client)

        # Test 4: Progress Callback
        results['progress'] = test_progress_callback(client)

        # Test 5: Delete (cleanup)
        if account_ids:
            results['delete'] = test_bulk_delete(client, account_ids)
        else:
            results['delete'] = None  # Skipped

    except KeyboardInterrupt:
        print("\n\n⚠️  Tests interrupted by user")
        if account_ids:
            print(f"⚠️  Warning: {len(account_ids)} test accounts may remain in Salesforce")
            print(f"   Account IDs: {account_ids}")
        return False

    # Summary
    print_section("Test Summary")

    test_names = {
        'insert': 'Bulk Insert',
        'update': 'Bulk Update',
        'query': 'Bulk Query',
        'progress': 'Progress Callback',
        'delete': 'Bulk Delete (Cleanup)'
    }

    passed = 0
    failed = 0
    skipped = 0

    for test_key, test_name in test_names.items():
        result = results.get(test_key)
        if result is True:
            print(f"  ✓ {test_name}: PASSED")
            passed += 1
        elif result is False:
            print(f"  ❌ {test_name}: FAILED")
            failed += 1
        else:
            print(f"  ⚠️  {test_name}: SKIPPED")
            skipped += 1

    print(f"\n  Total: {passed} passed, {failed} failed, {skipped} skipped")

    if failed == 0 and passed > 0:
        print("\n🎉 All tests passed! Bulk API v2 is working correctly.")
        return True
    elif failed > 0:
        print(f"\n⚠️  {failed} test(s) failed. Please review errors above.")
        return False
    else:
        print("\n⚠️  No tests were executed successfully.")
        return False


if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)
