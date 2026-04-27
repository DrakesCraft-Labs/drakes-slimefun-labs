# Kinetic Core - Testing Guide

## Overview

This directory contains comprehensive tests for Kinetic Core, including:
- **Unit tests**: Fast tests with mocked dependencies
- **Integration tests**: Real tests against Salesforce API

## Test Files

| File | Description | Type |
|------|-------------|------|
| `test_auth.py` | Authentication tests | Unit |
| `test_core.py` | Core client tests | Unit |
| `test_sanity.py` | Basic sanity checks | Unit |
| `test_integration.py` | Full CRUD integration tests | Integration |

## Prerequisites

### 1. Install Dependencies

```bash
pip install -e ".[dev]"
```

This installs:
- `pytest` - Test framework
- `pytest-cov` - Coverage reporting
- All project dependencies

### 2. Configure Environment

Create a `.env` file in the project root with valid Salesforce credentials:

```bash
# JWT Authentication (Recommended)
SF_CLIENT_ID=your_consumer_key
SF_USERNAME=your_username@example.com.sandbox
SF_PRIVATE_KEY_PATH=secrets/server.key
SF_LOGIN_URL=https://test.salesforce.com

# OR OAuth Authentication
SF_CLIENT_ID=your_consumer_key
SF_CLIENT_SECRET=your_consumer_secret
SF_USERNAME=your_username@example.com.sandbox
SF_PASSWORD=your_password
SF_SECURITY_TOKEN=your_security_token
SF_LOGIN_URL=https://test.salesforce.com
```

**IMPORTANT**:
- Always use a **SANDBOX** environment for testing
- Never run tests against production
- Tests will create and delete data in Salesforce

## Running Tests

### Run All Tests

```bash
# From project root
pytest

# From tests directory
cd tests
pytest
```

### Run Specific Test Categories

```bash
# Unit tests only (fast, no Salesforce connection needed)
pytest -m unit

# Integration tests only (requires Salesforce connection)
pytest -m integration

# Authentication tests
pytest -m auth

# CRUD operation tests
pytest -m crud

# Query operation tests
pytest -m query

# Batch operation tests
pytest -m batch

# Error handling tests
pytest -m error
```

### Run Specific Test Files

```bash
# Run only integration tests
pytest tests/test_integration.py

# Run only auth tests
pytest tests/test_auth.py

# Run only core client tests
pytest tests/test_core.py
```

### Run Specific Tests

```bash
# Run a specific test function
pytest tests/test_integration.py::test_10_create_single_account

# Run tests matching a pattern
pytest -k "create"
pytest -k "query"
pytest -k "delete"
```

### Verbose Output

```bash
# Show detailed output with print statements
pytest -v -s

# Show only test names
pytest -v

# Show extra summary info
pytest -v --tb=short
```

## Integration Test Coverage

The `test_integration.py` file tests all CRUD operations:

### ✅ CREATE Operations
- `test_10_create_single_account` - Create single Account
- `test_11_create_single_contact` - Create single Contact
- `test_12_create_batch_accounts` - Create multiple Accounts in batch

### ✅ READ Operations
- `test_20_query_accounts` - Basic SOQL query
- `test_21_query_one_account` - Query single record
- `test_22_get_account_by_id` - Get record by ID
- `test_23_get_account_with_specific_fields` - Get with field selection
- `test_24_count_accounts` - Count records
- `test_25_count_accounts_with_filter` - Count with WHERE clause
- `test_26_query_with_pagination` - Automatic pagination
- `test_60_query_with_complex_where` - Complex WHERE clauses
- `test_61_query_with_relationships` - Relationship queries
- `test_62_query_aggregate` - Aggregate functions (COUNT, SUM)

### ✅ UPDATE Operations
- `test_30_update_account` - Update Account fields
- `test_31_update_contact` - Update Contact fields
- `test_32_update_nonexistent_record` - Error handling

### ✅ UPSERT Operations
- `test_40_upsert_new_account` - Upsert (insert new)
- `test_41_upsert_existing_account` - Upsert (update existing)

### ✅ DELETE Operations
- `test_50_delete_account` - Delete Account
- `test_51_delete_contact` - Delete Contact
- `test_52_delete_nonexistent_record` - Error handling

### ✅ Metadata Operations
- `test_70_describe_account` - Describe Account metadata
- `test_71_describe_contact` - Describe Contact metadata

### ✅ Error Handling
- `test_80_invalid_soql` - Invalid SOQL queries
- `test_81_invalid_sobject` - Invalid object names
- `test_82_missing_required_field` - Missing required fields

### ✅ Performance
- `test_90_batch_performance` - Batch operation performance

## Test Output

### Successful Run Example

```
============= Kinetic Core - Integration Test Suite =============
Testing Salesforce CRUD operations
==================================================================

tests/test_integration.py::test_01_authentication PASSED
✓ Authenticated via JWT as user@example.com.sandbox
✓ Connected to https://test.salesforce.com
✓ API Version: v60.0

tests/test_integration.py::test_10_create_single_account PASSED
✓ Created Account: 001XXXXXXXXXXXXXXX

tests/test_integration.py::test_20_query_accounts PASSED
✓ Query returned 5 accounts

... (more tests)

==================================================================
TEST SUMMARY
==================================================================
✓ Test accounts created: 15
✓ Test contacts created: 3
✓ Connected to: https://test.salesforce.com
✓ API Version: v60.0
==================================================================
All cleanup will be performed automatically
==================================================================

======================== 30 passed in 45.23s =======================
```

## Coverage Report

Generate a coverage report:

```bash
# Terminal report
pytest --cov=kinetic_core --cov-report=term

# HTML report (creates htmlcov/index.html)
pytest --cov=kinetic_core --cov-report=html

# Open HTML report
open htmlcov/index.html  # macOS
start htmlcov/index.html # Windows
```

## Test Data Cleanup

All integration tests automatically clean up test data:

- Test accounts are tracked in `test_account_ids` fixture
- Test contacts are tracked in `test_contact_ids` fixture
- Cleanup happens automatically after tests complete
- Even if tests fail, cleanup still runs

## Troubleshooting

### Authentication Fails

```
FAILED tests/test_integration.py::test_01_authentication
```

**Solutions:**
1. Check `.env` file exists and has valid credentials
2. Verify Salesforce login URL (sandbox vs production)
3. For JWT: Check private key path and permissions
4. For OAuth: Verify client secret and security token

### Tests Skipped

```
SKIPPED [1] test_integration.py:42: No test accounts available
```

**Cause:** Some tests depend on previous tests creating data.

**Solution:** Run all tests together, not individually.

### External Field Not Found

```
SKIPPED: External_Key__c field not available on Account object
```

**Cause:** Upsert tests require a custom External ID field.

**Solution:**
1. Create a custom field `External_Key__c` on Account
2. Mark it as External ID
3. Or skip upsert tests

### Connection Timeout

```
requests.exceptions.Timeout: HTTPSConnectionPool...
```

**Solutions:**
1. Check internet connection
2. Verify Salesforce org is accessible
3. Increase timeout in tests

### Too Many Requests

```
requests.exceptions.HTTPError: 403 Client Error: REQUEST_LIMIT_EXCEEDED
```

**Solution:** Wait a few minutes. Salesforce has API rate limits.

## Best Practices

### 1. Run Unit Tests First

```bash
pytest -m unit  # Fast, no API calls
```

### 2. Then Run Integration Tests

```bash
pytest -m integration  # Slower, requires Salesforce
```

### 3. Use Sandbox Only

Never run integration tests against production:
- Use `https://test.salesforce.com` (sandbox)
- NOT `https://login.salesforce.com` (production)

### 4. Check Coverage

Aim for >80% code coverage:

```bash
pytest --cov=kinetic_core --cov-report=term
```

### 5. Run Before Commits

```bash
# Quick check
pytest -m unit

# Full check before pushing
pytest --cov=kinetic_core
```

## Continuous Integration (CI)

For CI/CD pipelines:

```bash
# Install dependencies
pip install -e ".[dev]"

# Run unit tests (fast)
pytest -m unit --tb=short

# Run integration tests if credentials available
if [ -f .env ]; then
  pytest -m integration --tb=short
fi
```

## Adding New Tests

### 1. Unit Tests

Add to existing files or create new `test_*.py`:

```python
import pytest
from unittest import mock
from kinetic_core import SalesforceClient

@mock.patch("kinetic_core.core.client.requests.get")
def test_new_feature(mock_get):
    """Test description."""
    # Setup mocks
    mock_get.return_value.status_code = 200
    mock_get.return_value.json.return_value = {...}

    # Test code
    result = client.method()

    # Assertions
    assert result is not None
```

### 2. Integration Tests

Add to `test_integration.py`:

```python
def test_70_new_integration_test(auth_client, test_account_ids):
    """Test description."""
    # Create test data
    data = {...}

    # Test operation
    result = auth_client.method(data)

    # Verify result
    assert result is not None

    # Cleanup if needed
    test_account_ids.append(result_id)
```

## Test Markers Reference

| Marker | Description | Example |
|--------|-------------|---------|
| `@pytest.mark.unit` | Fast unit test | `pytest -m unit` |
| `@pytest.mark.integration` | Salesforce API test | `pytest -m integration` |
| `@pytest.mark.slow` | Slow test | `pytest -m "not slow"` |
| `@pytest.mark.auth` | Auth test | `pytest -m auth` |
| `@pytest.mark.crud` | CRUD test | `pytest -m crud` |
| `@pytest.mark.query` | Query test | `pytest -m query` |
| `@pytest.mark.batch` | Batch test | `pytest -m batch` |
| `@pytest.mark.error` | Error handling | `pytest -m error` |

## Support

If tests fail:
1. Check this guide
2. Review test output carefully
3. Verify `.env` configuration
4. Check Salesforce org status
5. Review [TESTING_GUIDE.md](../docs/TESTING_GUIDE.md) in main docs

---

**Last Updated:** 2025-12-27
**Test Coverage:** 30+ integration tests covering all CRUD operations
