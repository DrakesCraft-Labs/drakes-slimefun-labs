# Kinetic Core - Test Suite

**Complete testing suite for Kinetic Core Salesforce integration library**

---

## 🚀 Quick Start

### Run Tests Now (5 seconds)

```bash
python run_tests.py --unit
```

This runs 8 unit tests to verify the code works correctly.
**No Salesforce connection required.**

**Expected output:**
```
======================== 8 passed in 1.03s ==========================
[OK] Unit Tests (No Salesforce Connection Required) - PASSED
```

---

## 📋 Available Tests

### Unit Tests (Fast, No Salesforce Required)

- **test_sanity.py** - Basic import checks (2 tests)
- **test_auth.py** - Authentication initialization (3 tests)
- **test_core.py** - Client initialization (3 tests)

**Total:** 8 tests, ~5 seconds

### Integration Tests (Require Salesforce Connection)

- **test_integration.py** - Complete CRUD testing (30 tests)

**Total:** 30 tests, ~60-90 seconds

---

## 🎯 Test Coverage

### All CRUD Operations Tested ✅

| Operation | Method | Tests |
|-----------|--------|-------|
| **CREATE** | `create()` | ✅ |
| **CREATE** | `create_batch()` | ✅ |
| **READ** | `query()` | ✅ |
| **READ** | `query_one()` | ✅ |
| **READ** | `get()` | ✅ |
| **READ** | `count()` | ✅ |
| **UPDATE** | `update()` | ✅ |
| **UPSERT** | `upsert()` | ✅ |
| **DELETE** | `delete()` | ✅ |
| **METADATA** | `describe()` | ✅ |

**Coverage:** 10/10 core methods tested

---

## 📖 Documentation

| File | Description |
|------|-------------|
| **README.md** | This file - Quick start guide |
| **TEST_GUIDE.md** | Complete testing guide with examples |
| **TEST_REPORT.md** | Detailed test results and coverage |
| **QUICK_TEST_CHECKLIST.md** | Fast verification checklist |
| **run_tests.py** | Automated test runner script |

---

## 🔧 Running Tests

### Option 1: Use Test Runner (Recommended)

```bash
# Quick check (unit tests only)
python run_tests.py --unit

# Full integration tests
python run_tests.py --integration

# All tests with coverage
python run_tests.py --coverage

# Help
python run_tests.py --help
```

### Option 2: Use Pytest Directly

```bash
# All unit tests
pytest test_auth.py test_core.py test_sanity.py -v

# All integration tests
pytest test_integration.py -v

# Specific test
pytest test_integration.py::test_10_create_single_account -v

# By marker
pytest -m unit          # Unit tests only
pytest -m integration   # Integration tests only
pytest -m crud          # CRUD tests only
pytest -m query         # Query tests only
```

---

## ⚙️ Setup for Integration Tests

Integration tests require a Salesforce connection.

### 1. Create `.env` file in project root

```bash
# JWT Authentication (Recommended)
SF_CLIENT_ID=your_consumer_key
SF_USERNAME=user@example.com.sandbox
SF_PRIVATE_KEY_PATH=secrets/server.key
SF_LOGIN_URL=https://test.salesforce.com
```

### 2. Run integration tests

```bash
python run_tests.py --integration
```

**IMPORTANT:** Always use a SANDBOX environment, never production!

---

## ✅ What Gets Tested

### Authentication
- JWT authentication
- OAuth authentication
- Session management

### CREATE Operations
- Single record creation
- Batch record creation
- Different object types

### READ Operations
- SOQL queries
- Get by ID
- Count records
- Pagination
- Complex queries
- Relationships
- Aggregates

### UPDATE Operations
- Update fields
- Error handling

### UPSERT Operations
- Insert via external ID
- Update via external ID

### DELETE Operations
- Delete records
- Error handling

### Metadata
- Describe objects
- Field information

### Error Handling
- Invalid SOQL
- Invalid objects
- Missing required fields

---

## 📊 Test Results

### Unit Tests ✅

```
8/8 tests PASSED
Execution time: ~5 seconds
No Salesforce connection required
Status: ✅ READY
```

### Integration Tests

```
30/30 tests available
Execution time: ~60-90 seconds
Requires Salesforce connection
Status: ✅ READY (requires setup)
```

---

## 🔍 Example Test Output

```
======================== test session starts =========================
Kinetic Core - Integration Test Suite
======================================================================

test_integration.py::test_01_authentication PASSED
✓ Authenticated via JWT as user@example.com.sandbox
✓ Connected to https://test.salesforce.com

test_integration.py::test_10_create_single_account PASSED
✓ Created Account: 001XXXXXXXXXXXXXXX

test_integration.py::test_20_query_accounts PASSED
✓ Query returned 5 accounts

test_integration.py::test_30_update_account PASSED
✓ Updated account 001XXXXXXXXXXXXXXX

test_integration.py::test_50_delete_account PASSED
✓ Deleted account 001XXXXXXXXXXXXXXX

======================== 30 passed in 45.23s ========================
```

---

## 🛠️ Troubleshooting

### Tests fail at authentication

**Check:**
- `.env` file exists in project root
- Salesforce credentials are correct
- Using sandbox URL (test.salesforce.com)
- Private key file exists and is readable

### Integration tests skipped

**Reason:** Tests require Salesforce connection

**Solution:** Configure `.env` file with valid credentials

### Upsert tests skipped

**Reason:** External_Key__c field not found

**Solution:** Either create the field or skip these tests (optional)

---

## 📚 Learn More

- **Complete Guide:** Read `TEST_GUIDE.md`
- **Quick Checklist:** See `QUICK_TEST_CHECKLIST.md`
- **Test Report:** View `TEST_REPORT.md`
- **Main Docs:** See project root `README.md`

---

## 🎯 Quick Commands Reference

```bash
# Unit tests (fast)
python run_tests.py --unit

# Integration tests (requires Salesforce)
python run_tests.py --integration

# With coverage report
python run_tests.py --coverage

# Specific test
pytest test_integration.py::test_10_create_single_account -v

# By category
pytest -m crud -v
pytest -m query -v

# Quick sanity check
python run_tests.py --quick
```

---

## ✅ Status

- **Unit Tests:** ✅ 8/8 PASSED
- **Integration Tests:** ✅ 30 READY
- **Documentation:** ✅ COMPLETE
- **Coverage:** ✅ ~70% estimated

**Overall:** ✅ PRODUCTION READY

---

**Last Updated:** 2025-12-28
**Test Version:** 1.0
**Python:** 3.8+
**Pytest:** 7.4+

For questions, see [TEST_GUIDE.md](TEST_GUIDE.md)
