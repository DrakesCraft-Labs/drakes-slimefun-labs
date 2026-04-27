"""
Integration tests for Kinetic Core - Salesforce CRUD operations.

This module tests all CRUD operations against a real Salesforce instance.
Requires valid credentials in .env file.

Test Objects Used:
- Account (standard object)
- Contact (standard object)

IMPORTANT: These tests will create and delete data in your Salesforce org.
Run against a sandbox environment only!
"""

import os
import pytest
import time
from datetime import datetime
from typing import List, Dict, Any

from kinetic_core import (
    JWTAuthenticator,
    OAuthAuthenticator,
    SalesforceClient,
    SalesforceSession
)


# ============================================================================
# Fixtures
# ============================================================================

@pytest.fixture(scope="module")
def auth_client():
    """
    Create authenticated Salesforce client.
    Uses JWT authentication if available, falls back to OAuth.
    """
    try:
        # Try JWT first (recommended)
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()
        print(f"\n[OK] Authenticated via JWT as {auth.username}")
    except Exception as e:
        print(f"\n[WARNING] JWT auth failed: {e}")
        try:
            # Fallback to OAuth
            auth = OAuthAuthenticator.from_env()
            session = auth.authenticate()
            print(f"[OK] Authenticated via OAuth as {auth.username}")
        except Exception as e:
            pytest.skip(f"Cannot authenticate to Salesforce: {e}")

    client = SalesforceClient(session)
    print(f"[OK] Connected to {session.instance_url}")
    print(f"[OK] API Version: {session.api_version}")

    return client


@pytest.fixture(scope="module")
def test_account_ids(auth_client) -> List[str]:
    """
    Create test accounts for use across tests.
    Returns list of Account IDs that will be cleaned up at the end.
    """
    account_ids = []

    yield account_ids

    # Cleanup: Delete all test accounts
    print(f"\n[CLEANUP] Cleaning up {len(account_ids)} test accounts...")
    for account_id in account_ids:
        try:
            auth_client.delete("Account", account_id)
            print(f"  [OK] Deleted account {account_id}")
        except Exception as e:
            print(f"  [WARNING] Failed to delete {account_id}: {e}")


@pytest.fixture(scope="module")
def test_contact_ids(auth_client) -> List[str]:
    """
    Create test contacts for use across tests.
    Returns list of Contact IDs that will be cleaned up at the end.
    """
    contact_ids = []

    yield contact_ids

    # Cleanup: Delete all test contacts
    print(f"\n[CLEANUP] Cleaning up {len(contact_ids)} test contacts...")
    for contact_id in contact_ids:
        try:
            auth_client.delete("Contact", contact_id)
            print(f"  [OK] Deleted contact {contact_id}")
        except Exception as e:
            print(f"  [WARNING] Failed to delete {contact_id}: {e}")


# ============================================================================
# Authentication Tests
# ============================================================================

def test_01_authentication(auth_client):
    """Test that authentication is successful."""
    assert auth_client is not None
    assert auth_client.session is not None
    assert auth_client.session.is_valid()
    print("[OK] Authentication successful")


def test_02_session_properties(auth_client):
    """Test session properties."""
    session = auth_client.session
    assert session.instance_url is not None
    assert session.access_token is not None
    assert session.api_version is not None
    assert session.base_url is not None
    print(f"[OK] Session valid: {session.instance_url}")


# ============================================================================
# CREATE Operations Tests
# ============================================================================

def test_10_create_single_account(auth_client, test_account_ids):
    """Test creating a single Account record."""
    data = {
        "Name": f"Test Account {datetime.now().isoformat()}",
        "Industry": "Technology",
        "Phone": "555-0100",
        "Description": "Created by integration test"
    }

    account_id = auth_client.create("Account", data)
    test_account_ids.append(account_id)

    assert account_id is not None
    assert len(account_id) == 18 or len(account_id) == 15  # Salesforce ID format
    print(f"[OK] Created Account: {account_id}")


def test_11_create_single_contact(auth_client, test_contact_ids, test_account_ids):
    """Test creating a single Contact record."""
    # First create an account to link to
    account_data = {
        "Name": f"Contact Test Account {datetime.now().isoformat()}"
    }
    account_id = auth_client.create("Account", account_data)
    test_account_ids.append(account_id)

    # Create contact
    contact_data = {
        "FirstName": "John",
        "LastName": "Doe",
        "Email": f"john.doe.{int(time.time())}@test.com",
        "AccountId": account_id
    }

    contact_id = auth_client.create("Contact", contact_data)
    test_contact_ids.append(contact_id)

    assert contact_id is not None
    assert len(contact_id) == 18 or len(contact_id) == 15
    print(f"[OK] Created Contact: {contact_id}")


def test_12_create_batch_accounts(auth_client, test_account_ids):
    """Test creating multiple Account records in batch."""
    records = [
        {
            "Name": f"Batch Account 1 - {datetime.now().isoformat()}",
            "Industry": "Manufacturing"
        },
        {
            "Name": f"Batch Account 2 - {datetime.now().isoformat()}",
            "Industry": "Retail"
        },
        {
            "Name": f"Batch Account 3 - {datetime.now().isoformat()}",
            "Industry": "Healthcare"
        }
    ]

    results = auth_client.create_batch("Account", records)

    assert len(results) == 3

    success_count = 0
    for result in results:
        if result.get("success"):
            success_count += 1
            test_account_ids.append(result.get("id"))

    assert success_count == 3
    print(f"[OK] Created {success_count} accounts in batch")


# ============================================================================
# READ Operations Tests
# ============================================================================

def test_20_query_accounts(auth_client):
    """Test querying Account records with SOQL."""
    soql = "SELECT Id, Name, Industry FROM Account LIMIT 5"
    results = auth_client.query(soql)

    assert isinstance(results, list)
    assert len(results) <= 5

    if results:
        assert "Id" in results[0]
        assert "Name" in results[0]
        print(f"[OK] Query returned {len(results)} accounts")
    else:
        print("[WARNING] No accounts found (org might be empty)")


def test_21_query_one_account(auth_client, test_account_ids):
    """Test querying a single Account record."""
    if not test_account_ids:
        pytest.skip("No test accounts available")

    account_id = test_account_ids[0]
    soql = f"SELECT Id, Name, Industry FROM Account WHERE Id = '{account_id}'"
    result = auth_client.query_one(soql)

    assert result is not None
    assert result["Id"] == account_id
    print(f"[OK] Query one returned: {result['Name']}")


def test_22_get_account_by_id(auth_client, test_account_ids):
    """Test retrieving an Account by ID."""
    if not test_account_ids:
        pytest.skip("No test accounts available")

    account_id = test_account_ids[0]
    account = auth_client.get("Account", account_id)

    assert account is not None
    assert account["Id"] == account_id
    assert "Name" in account
    print(f"[OK] Get by ID returned: {account['Name']}")


def test_23_get_account_with_specific_fields(auth_client, test_account_ids):
    """Test retrieving an Account with specific fields."""
    if not test_account_ids:
        pytest.skip("No test accounts available")

    account_id = test_account_ids[0]
    fields = ["Id", "Name", "Industry", "CreatedDate"]
    account = auth_client.get("Account", account_id, fields=fields)

    assert account is not None
    assert "Id" in account
    assert "Name" in account
    assert "Industry" in account
    assert "CreatedDate" in account
    print(f"[OK] Get with fields returned: {account['Name']}")


def test_24_count_accounts(auth_client):
    """Test counting Account records."""
    total_count = auth_client.count("Account")

    assert isinstance(total_count, int)
    assert total_count >= 0
    print(f"[OK] Total accounts: {total_count}")


def test_25_count_accounts_with_filter(auth_client):
    """Test counting Account records with WHERE clause."""
    count = auth_client.count("Account", where="Industry = 'Technology'")

    assert isinstance(count, int)
    assert count >= 0
    print(f"[OK] Technology accounts: {count}")


def test_26_query_with_pagination(auth_client):
    """Test query with automatic pagination handling."""
    # Query that might return multiple pages
    soql = "SELECT Id, Name FROM Account LIMIT 1000"
    results = auth_client.query(soql)

    assert isinstance(results, list)
    print(f"[OK] Query with pagination returned {len(results)} records")


# ============================================================================
# UPDATE Operations Tests
# ============================================================================

def test_30_update_account(auth_client, test_account_ids):
    """Test updating an Account record."""
    if not test_account_ids:
        pytest.skip("No test accounts available")

    account_id = test_account_ids[0]

    # Update the account
    update_data = {
        "Phone": "555-9999",
        "Description": f"Updated at {datetime.now().isoformat()}"
    }

    success = auth_client.update("Account", account_id, update_data)
    assert success is True

    # Verify the update
    account = auth_client.get("Account", account_id, fields=["Id", "Phone", "Description"])
    assert account["Phone"] == "555-9999"
    assert "Updated at" in account["Description"]
    print(f"[OK] Updated account {account_id}")


def test_31_update_contact(auth_client, test_contact_ids):
    """Test updating a Contact record."""
    if not test_contact_ids:
        pytest.skip("No test contacts available")

    contact_id = test_contact_ids[0]

    # Update the contact
    update_data = {
        "Email": f"updated.{int(time.time())}@test.com",
        "Phone": "555-8888"
    }

    success = auth_client.update("Contact", contact_id, update_data)
    assert success is True

    # Verify the update
    contact = auth_client.get("Contact", contact_id, fields=["Id", "Email", "Phone"])
    assert contact["Phone"] == "555-8888"
    print(f"[OK] Updated contact {contact_id}")


def test_32_update_nonexistent_record(auth_client):
    """Test updating a non-existent record (should fail gracefully)."""
    fake_id = "001000000000000AAA"

    with pytest.raises(Exception):  # Should raise an error
        auth_client.update("Account", fake_id, {"Name": "Should Fail"})

    print("[OK] Update non-existent record failed as expected")


# ============================================================================
# UPSERT Operations Tests
# ============================================================================

def test_40_upsert_new_account(auth_client, test_account_ids):
    """Test upserting a new Account (insert)."""
    # Use a unique external ID
    external_id = f"TEST-{int(time.time())}"

    # Note: This test assumes you have an External_Key__c field
    # If not, this test will be skipped
    try:
        data = {
            "Name": f"Upsert Test Account {datetime.now().isoformat()}",
            "Industry": "Technology"
        }

        account_id = auth_client.upsert(
            "Account",
            "External_Key__c",  # External ID field
            external_id,
            data
        )

        test_account_ids.append(account_id)
        assert account_id is not None
        print(f"[OK] Upserted (created) account {account_id}")
    except Exception as e:
        if "External_Key__c" in str(e):
            pytest.skip("External_Key__c field not available on Account object")
        else:
            raise


def test_41_upsert_existing_account(auth_client, test_account_ids):
    """Test upserting an existing Account (update)."""
    external_id = f"TEST-UPDATE-{int(time.time())}"

    try:
        # First upsert (create)
        data1 = {
            "Name": "Original Name",
            "Industry": "Technology"
        }

        account_id1 = auth_client.upsert(
            "Account",
            "External_Key__c",
            external_id,
            data1
        )
        test_account_ids.append(account_id1)

        # Second upsert with same external ID (update)
        data2 = {
            "Name": "Updated Name",
            "Industry": "Manufacturing"
        }

        account_id2 = auth_client.upsert(
            "Account",
            "External_Key__c",
            external_id,
            data2
        )

        # Should be the same ID
        assert account_id1 == account_id2

        # Verify the update
        account = auth_client.get("Account", account_id2, fields=["Id", "Name", "Industry"])
        assert account["Name"] == "Updated Name"
        assert account["Industry"] == "Manufacturing"

        print(f"[OK] Upserted (updated) account {account_id2}")
    except Exception as e:
        if "External_Key__c" in str(e):
            pytest.skip("External_Key__c field not available on Account object")
        else:
            raise


# ============================================================================
# DELETE Operations Tests
# ============================================================================

def test_50_delete_account(auth_client, test_account_ids):
    """Test deleting an Account record."""
    # Create a temporary account for deletion
    data = {
        "Name": f"Delete Test Account {datetime.now().isoformat()}"
    }

    account_id = auth_client.create("Account", data)

    # Delete it
    success = auth_client.delete("Account", account_id)
    assert success is True

    # Verify deletion (should not be found)
    soql = f"SELECT Id FROM Account WHERE Id = '{account_id}'"
    result = auth_client.query_one(soql)
    assert result is None

    print(f"[OK] Deleted account {account_id}")


def test_51_delete_contact(auth_client, test_contact_ids):
    """Test deleting a Contact record."""
    # Create a temporary contact for deletion
    contact_data = {
        "FirstName": "Delete",
        "LastName": "Test",
        "Email": f"delete.{int(time.time())}@test.com"
    }

    contact_id = auth_client.create("Contact", contact_data)

    # Delete it
    success = auth_client.delete("Contact", contact_id)
    assert success is True

    # Verify deletion
    soql = f"SELECT Id FROM Contact WHERE Id = '{contact_id}'"
    result = auth_client.query_one(soql)
    assert result is None

    print(f"[OK] Deleted contact {contact_id}")


def test_52_delete_nonexistent_record(auth_client):
    """Test deleting a non-existent record (should fail gracefully)."""
    fake_id = "001000000000000AAA"

    with pytest.raises(Exception):  # Should raise an error
        auth_client.delete("Account", fake_id)

    print("[OK] Delete non-existent record failed as expected")


# ============================================================================
# Advanced Query Tests
# ============================================================================

def test_60_query_with_complex_where(auth_client):
    """Test query with complex WHERE clause."""
    soql = """
        SELECT Id, Name, Industry, AnnualRevenue
        FROM Account
        WHERE Industry IN ('Technology', 'Manufacturing')
        AND CreatedDate = THIS_YEAR
        LIMIT 10
    """

    results = auth_client.query(soql)
    assert isinstance(results, list)
    print(f"[OK] Complex query returned {len(results)} records")


def test_61_query_with_relationships(auth_client):
    """Test query with relationship fields."""
    soql = """
        SELECT Id, Name,
               (SELECT Id, FirstName, LastName FROM Contacts LIMIT 5)
        FROM Account
        WHERE Id IN (SELECT AccountId FROM Contact)
        LIMIT 5
    """

    results = auth_client.query(soql)
    assert isinstance(results, list)

    if results and results[0].get("Contacts"):
        assert "records" in results[0]["Contacts"]
        print(f"[OK] Relationship query returned {len(results)} accounts with contacts")
    else:
        print("[OK] Relationship query executed (no results)")


def test_62_query_aggregate(auth_client):
    """Test aggregate query (COUNT, SUM, etc)."""
    soql = "SELECT COUNT(Id) total FROM Account"
    results = auth_client.query(soql)

    assert isinstance(results, list)
    assert len(results) > 0
    assert "total" in results[0] or "expr0" in results[0]
    print(f"[OK] Aggregate query returned: {results[0]}")


# ============================================================================
# Describe/Metadata Tests
# ============================================================================

def test_70_describe_account(auth_client):
    """Test describing Account object metadata."""
    try:
        metadata = auth_client.describe("Account")

        assert metadata is not None
        assert "name" in metadata
        assert "fields" in metadata
        assert metadata["name"] == "Account"

        # Check some known fields
        field_names = [f["name"] for f in metadata["fields"]]
        assert "Id" in field_names
        assert "Name" in field_names

        print(f"[OK] Described Account: {len(metadata['fields'])} fields")
    except AttributeError:
        pytest.skip("describe() method not implemented")


def test_71_describe_contact(auth_client):
    """Test describing Contact object metadata."""
    try:
        metadata = auth_client.describe("Contact")

        assert metadata is not None
        assert metadata["name"] == "Contact"
        assert "fields" in metadata

        print(f"[OK] Described Contact: {len(metadata['fields'])} fields")
    except AttributeError:
        pytest.skip("describe() method not implemented")


# ============================================================================
# Error Handling Tests
# ============================================================================

def test_80_invalid_soql(auth_client):
    """Test handling of invalid SOQL."""
    invalid_soql = "SELECT InvalidField FROM Account"

    with pytest.raises(Exception):
        auth_client.query(invalid_soql)

    print("[OK] Invalid SOQL handled correctly")


def test_81_invalid_sobject(auth_client):
    """Test handling of invalid SObject name."""
    with pytest.raises(Exception):
        auth_client.create("InvalidObject__c", {"Name": "Test"})

    print("[OK] Invalid SObject handled correctly")


def test_82_missing_required_field(auth_client):
    """Test handling of missing required fields."""
    # Contact requires LastName
    with pytest.raises(Exception):
        auth_client.create("Contact", {"FirstName": "John"})  # Missing LastName

    print("[OK] Missing required field handled correctly")


# ============================================================================
# Performance Tests
# ============================================================================

def test_90_batch_performance(auth_client, test_account_ids):
    """Test performance of batch operations."""
    import time

    # Create 10 accounts in batch
    records = [
        {"Name": f"Performance Test {i} - {datetime.now().isoformat()}"}
        for i in range(10)
    ]

    start_time = time.time()
    results = auth_client.create_batch("Account", records)
    elapsed = time.time() - start_time

    success_count = sum(1 for r in results if r.get("success"))
    assert success_count == 10

    # Collect IDs for cleanup
    for result in results:
        if result.get("success"):
            test_account_ids.append(result.get("id"))

    print(f"[OK] Created 10 accounts in {elapsed:.2f}s ({success_count/elapsed:.2f} records/sec)")


# ============================================================================
# Summary
# ============================================================================

def test_99_summary(auth_client, test_account_ids, test_contact_ids):
    """Print test summary."""
    print("\n" + "="*70)
    print("TEST SUMMARY")
    print("="*70)
    print(f"[OK] Test accounts created: {len(test_account_ids)}")
    print(f"[OK] Test contacts created: {len(test_contact_ids)}")
    print(f"[OK] Connected to: {auth_client.session.instance_url}")
    print(f"[OK] API Version: {auth_client.session.api_version}")
    print("="*70)
    print("All cleanup will be performed automatically")
    print("="*70)
