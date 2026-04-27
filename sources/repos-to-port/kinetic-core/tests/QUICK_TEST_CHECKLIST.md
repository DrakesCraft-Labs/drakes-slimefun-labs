# Quick Test Checklist - Kinetic Core

Use this checklist to quickly verify that all CRUD operations work correctly.

---

## ✅ Pre-Test Setup

- [ ] `.env` file configured with Salesforce credentials
- [ ] Salesforce org is accessible (sandbox recommended)
- [ ] Python and pytest installed
- [ ] Located in `tests/` directory

---

## 🚀 Quick Verification (5 seconds)

Run unit tests to verify basic functionality:

```bash
python run_tests.py --unit
```

**Expected:** All 8 tests pass ✅

---

## 🔧 Full Integration Test (60-90 seconds)

Run against your Salesforce org:

```bash
python run_tests.py --integration
```

**Expected:** All 30 tests pass ✅

---

## 📋 Method-by-Method Verification

If integration tests fail, test methods individually:

### 1. Authentication ✅

```bash
pytest test_integration.py::test_01_authentication -v
```

**Verifies:** JWT/OAuth authentication works

---

### 2. CREATE Operations ✅

```bash
# Single record
pytest test_integration.py::test_10_create_single_account -v

# Batch create
pytest test_integration.py::test_12_create_batch_accounts -v
```

**Verifies:**
- ✅ `client.create()` works
- ✅ `client.create_batch()` works

---

### 3. READ Operations ✅

```bash
# Basic query
pytest test_integration.py::test_20_query_accounts -v

# Get by ID
pytest test_integration.py::test_22_get_account_by_id -v

# Count records
pytest test_integration.py::test_24_count_accounts -v
```

**Verifies:**
- ✅ `client.query()` works
- ✅ `client.query_one()` works
- ✅ `client.get()` works
- ✅ `client.count()` works

---

### 4. UPDATE Operations ✅

```bash
pytest test_integration.py::test_30_update_account -v
```

**Verifies:**
- ✅ `client.update()` works

---

### 5. UPSERT Operations ✅

```bash
pytest test_integration.py::test_40_upsert_new_account -v
pytest test_integration.py::test_41_upsert_existing_account -v
```

**Verifies:**
- ✅ `client.upsert()` works (insert)
- ✅ `client.upsert()` works (update)

**Note:** Requires `External_Key__c` field on Account object

---

### 6. DELETE Operations ✅

```bash
pytest test_integration.py::test_50_delete_account -v
```

**Verifies:**
- ✅ `client.delete()` works

---

### 7. Advanced Queries ✅

```bash
# Complex WHERE
pytest test_integration.py::test_60_query_with_complex_where -v

# Relationships
pytest test_integration.py::test_61_query_with_relationships -v

# Aggregates
pytest test_integration.py::test_62_query_aggregate -v
```

**Verifies:**
- ✅ Complex SOQL queries work
- ✅ Relationship queries work
- ✅ Aggregate functions work

---

### 8. Metadata Operations ✅

```bash
pytest test_integration.py::test_70_describe_account -v
```

**Verifies:**
- ✅ `client.describe()` works

---

### 9. Error Handling ✅

```bash
pytest test_integration.py::test_80_invalid_soql -v
pytest test_integration.py::test_81_invalid_sobject -v
```

**Verifies:**
- ✅ Invalid SOQL handled correctly
- ✅ Invalid object names handled correctly
- ✅ Missing required fields handled correctly

---

## 🎯 Manual Quick Test

If you prefer manual testing:

### 1. Open Python console

```bash
python
```

### 2. Test Authentication

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
print(f"✓ Connected to {session.instance_url}")

# Create client
client = SalesforceClient(session)
print("✓ Client created")
```

### 3. Test CREATE

```python
# Create an account
account_id = client.create("Account", {
    "Name": "Manual Test Account"
})
print(f"✓ Created account: {account_id}")
```

### 4. Test READ

```python
# Get the account
account = client.get("Account", account_id)
print(f"✓ Retrieved: {account['Name']}")

# Query accounts
accounts = client.query("SELECT Id, Name FROM Account LIMIT 5")
print(f"✓ Query returned {len(accounts)} accounts")

# Count accounts
count = client.count("Account")
print(f"✓ Total accounts: {count}")
```

### 5. Test UPDATE

```python
# Update the account
client.update("Account", account_id, {
    "Phone": "555-1234"
})
print("✓ Updated account")

# Verify update
account = client.get("Account", account_id, fields=["Phone"])
print(f"✓ Phone is now: {account['Phone']}")
```

### 6. Test DELETE

```python
# Delete the account
client.delete("Account", account_id)
print("✓ Deleted account")

# Verify deletion
result = client.query_one(f"SELECT Id FROM Account WHERE Id = '{account_id}'")
print(f"✓ Account deleted: {result is None}")
```

---

## 🔍 Troubleshooting

### Tests Fail at Authentication

**Problem:** Cannot authenticate to Salesforce

**Check:**
- [ ] `.env` file exists in project root
- [ ] `SF_CLIENT_ID` is correct
- [ ] `SF_USERNAME` is correct
- [ ] `SF_PRIVATE_KEY_PATH` points to valid key file
- [ ] `SF_LOGIN_URL` is correct (test.salesforce.com for sandbox)
- [ ] Private key file exists and is readable

**Fix:**
```bash
# Verify .env file
cat ../.env

# Verify private key exists
ls -la ../secrets/server.key
```

---

### Create/Update/Delete Fail

**Problem:** API operations fail

**Check:**
- [ ] User has API Enabled permission
- [ ] User has Create/Edit/Delete permissions on objects
- [ ] Org is not in maintenance mode
- [ ] API rate limits not exceeded

---

### Upsert Tests Skipped

**Problem:** `External_Key__c` field not found

**Solution:** This is optional. Either:
1. Create the field in Salesforce (Setup > Object Manager > Account > Fields)
2. Or skip upsert tests (they're optional)

---

### Unicode Errors on Windows

**Problem:** Console shows encoding errors

**Solution:**
```bash
# Set console encoding
chcp 65001

# Or run without emoji output
pytest test_integration.py -v --tb=line
```

---

## ✅ Success Criteria

All methods working correctly if:

- [x] ✅ All unit tests pass (8/8)
- [ ] ✅ All integration tests pass (30/30) OR
- [ ] ✅ All critical methods tested manually

### Critical Methods Checklist

- [ ] ✅ `create()` - Creates records
- [ ] ✅ `query()` - Queries records
- [ ] ✅ `get()` - Gets record by ID
- [ ] ✅ `update()` - Updates records
- [ ] ✅ `delete()` - Deletes records
- [ ] ✅ `count()` - Counts records
- [ ] ✅ `create_batch()` - Batch creates

### Optional Methods

- [ ] ✅ `upsert()` - Upsert via external ID
- [ ] ✅ `describe()` - Get object metadata
- [ ] ✅ `query_one()` - Query single record

---

## 📊 Quick Status Check

After running tests, you should see:

```
======================== test session starts =========================
...
✓ Created Account: 001XXXXXXXXXXXXXXX
✓ Query returned 5 accounts
✓ Updated account 001XXXXXXXXXXXXXXX
✓ Deleted account 001XXXXXXXXXXXXXXX
...
======================== 30 passed in 45.23s ========================
```

**If you see this:** ✅ Everything works!

**If tests fail:** See troubleshooting section above

---

## 🎉 Final Verification

Run this one command to test everything:

```bash
python run_tests.py
```

1. Runs unit tests (should pass instantly)
2. Asks if you want to run integration tests
3. If yes, tests all CRUD operations
4. Reports success/failure

**Expected output:** All tests pass ✅

---

**Last Updated:** 2025-12-28
