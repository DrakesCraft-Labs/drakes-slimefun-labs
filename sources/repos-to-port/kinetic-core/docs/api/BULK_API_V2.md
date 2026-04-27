# Bulk API v2 - Complete API Reference

## Overview

The Bulk API v2 allows you to process millions of Salesforce records asynchronously with high performance and reliability.

**New in Kinetic Core v2.0.0** - Full native implementation of Salesforce Bulk API v2.

## Features

- ✅ **High-volume operations**: Process up to 150 million records per job
- ✅ **Asynchronous processing**: Non-blocking job execution
- ✅ **Smart polling**: Exponential backoff for efficient API usage
- ✅ **Comprehensive error reporting**: Per-record failure details
- ✅ **Progress tracking**: Real-time job status updates
- ✅ **Type safety**: Full type hints throughout

## Supported Operations

| Operation | Description | Requirements |
|-----------|-------------|--------------|
| `insert()` | Create new records | - |
| `update()` | Update existing records | Requires `Id` field |
| `upsert()` | Insert or update | Requires external ID field |
| `delete()` | Soft delete (to recycle bin) | Requires `Id` field |
| `hard_delete()` | Permanent deletion | Requires `Id` field + permission |
| `query()` | Export large datasets | SOQL query |

## Quick Start

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Access Bulk API v2 via .bulk property
bulk_client = client.bulk
```

## API Reference

### BulkV2Client

Main client for Bulk API v2 operations.

**Access**: `client.bulk`

**Base URL**: `{instance_url}/services/data/v60.0/jobs/ingest`

---

### insert()

Bulk insert records into Salesforce.

**Signature**:
```python
def insert(
    sobject: str,
    records: List[Dict[str, Any]],
    wait: bool = True,
    timeout_minutes: Optional[float] = 10,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkResult
```

**Parameters**:
- `sobject` (str): Salesforce object API name (e.g., "Account", "Contact")
- `records` (List[Dict]): Records to insert
- `wait` (bool): Wait for job completion (default: True)
- `timeout_minutes` (float): Max wait time in minutes (default: 10)
- `on_progress` (callable): Progress callback function (optional)

**Returns**: `BulkResult` with success/failure details

**Example**:
```python
records = [
    {"Name": "Acme Corp", "Industry": "Technology"},
    {"Name": "Global Inc", "Industry": "Finance"}
]

result = client.bulk.insert("Account", records)
print(f"Success: {result.success_count}")
print(f"Failed: {result.failed_count}")
```

---

### update()

Bulk update existing records.

**Signature**:
```python
def update(
    sobject: str,
    records: List[Dict[str, Any]],
    wait: bool = True,
    timeout_minutes: Optional[float] = 10,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkResult
```

**Parameters**: Same as `insert()`

**Requirements**: All records **must** include the `Id` field

**Example**:
```python
records = [
    {"Id": "001xxx000001", "Industry": "Software"},
    {"Id": "001xxx000002", "Industry": "Hardware"}
]

result = client.bulk.update("Account", records)
```

**Validation**: Raises `ValueError` if any record is missing `Id` field

---

### upsert()

Insert or update records using an external ID field.

**Signature**:
```python
def upsert(
    sobject: str,
    records: List[Dict[str, Any]],
    external_id_field: str,
    wait: bool = True,
    timeout_minutes: Optional[float] = 10,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkResult
```

**Parameters**:
- `external_id_field` (str): External ID field name (e.g., "External_Key__c")
- Other parameters same as `insert()`

**Requirements**: All records must include the external ID field

**Example**:
```python
records = [
    {"External_Key__c": "EXT001", "Name": "Acme Corp"},
    {"External_Key__c": "EXT002", "Name": "Global Inc"}
]

result = client.bulk.upsert("Account", records, "External_Key__c")
```

---

### delete()

Bulk delete records (soft delete to recycle bin).

**Signature**:
```python
def delete(
    sobject: str,
    ids: List[str],
    wait: bool = True,
    timeout_minutes: Optional[float] = 10,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkResult
```

**Parameters**:
- `ids` (List[str]): Record IDs to delete
- Other parameters same as `insert()`

**Example**:
```python
ids = ["001xxx000001", "001xxx000002", "001xxx000003"]
result = client.bulk.delete("Account", ids)
```

---

### hard_delete()

Permanently delete records, bypassing recycle bin.

**Signature**:
```python
def hard_delete(
    sobject: str,
    ids: List[str],
    wait: bool = True,
    timeout_minutes: Optional[float] = 10,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkResult
```

**Requirements**:
- User must have "Bulk API Hard Delete" permission
- Records are permanently deleted (cannot be recovered)

**Example**:
```python
ids = ["001xxx000001"]
result = client.bulk.hard_delete("Account", ids)
```

---

### query()

Export large datasets using SOQL queries.

**Signature**:
```python
def query(
    soql: str,
    timeout_minutes: Optional[float] = 30,
    on_progress: Optional[Callable[[BulkJob], None]] = None
) -> BulkQueryResult
```

**Parameters**:
- `soql` (str): SOQL query string
- `timeout_minutes` (float): Max wait time (default: 30 minutes)
- `on_progress` (callable): Progress callback (optional)

**Returns**: `BulkQueryResult` with records and job metadata

**Example**:
```python
query = """
    SELECT Id, Name, Industry, AnnualRevenue
    FROM Account
    WHERE CreatedDate = LAST_YEAR
    LIMIT 1000000
"""

result = client.bulk.query(query)
print(f"Retrieved {result.record_count} records")

for record in result.records:
    print(record['Name'])
```

---

## Data Models

### BulkJob

Job metadata and status information.

**Properties**:
- `id` (str): Salesforce job ID
- `operation` (str): Operation type (insert, update, etc.)
- `object` (str): Salesforce object name
- `state` (str): Job state (Open, UploadComplete, InProgress, JobComplete, Failed, Aborted)
- `created_date` (datetime): Job creation timestamp
- `system_modstamp` (datetime): Last modification timestamp
- `number_records_processed` (int): Total records processed
- `number_records_failed` (int): Number of failed records

**Methods**:
- `is_complete() -> bool`: Check if job is finished
- `is_successful() -> bool`: Check if job completed successfully

**Job State Flow**:
```
Open → UploadComplete → InProgress → JobComplete/Failed/Aborted
```

---

### BulkResult

Result of insert/update/upsert/delete operations.

**Properties**:
- `job` (BulkJob): Job metadata
- `success_records` (List[Dict]): Successfully processed records
- `failed_records` (List[Dict]): Failed records
- `errors` (List[BulkError]): Detailed error information
- `success_count` (int): Number of successful records
- `failed_count` (int): Number of failed records
- `total_records` (int): Total records processed

**Example**:
```python
result = client.bulk.insert("Account", records)

if result.failed_count > 0:
    print(f"Errors occurred:")
    for error in result.errors:
        print(f"  - {error.message}")
```

---

### BulkQueryResult

Result of query operations.

**Properties**:
- `job` (BulkJob): Job metadata
- `records` (List[Dict]): Query results
- `record_count` (int): Number of records retrieved
- `locator` (str): Always None for Bulk API v2

**Example**:
```python
result = client.bulk.query("SELECT Id, Name FROM Account")

for record in result.records:
    account_id = record['Id']
    account_name = record['Name']
```

---

### BulkError

Detailed error information for failed records.

**Properties**:
- `fields` (List[str]): Field names that caused the error
- `message` (str): Error message
- `status_code` (str): Salesforce error code

**Example**:
```python
for error in result.errors:
    print(f"Error: {error.message}")
    print(f"Code: {error.status_code}")
    print(f"Fields: {', '.join(error.fields)}")
```

---

## Progress Tracking

Monitor job progress with callbacks:

```python
def progress_callback(job: BulkJob):
    print(f"Job {job.id}: {job.state}")
    print(f"  Processed: {job.number_records_processed}")

result = client.bulk.insert(
    "Account",
    records,
    on_progress=progress_callback
)
```

---

## Error Handling

### Validation Errors

```python
try:
    # Missing Id field
    records = [{"Name": "Test"}]
    client.bulk.update("Account", records)
except ValueError as e:
    print(f"Validation error: {e}")
```

### Job Failures

```python
result = client.bulk.insert("Account", records)

if result.job.state == "Failed":
    print("Job failed!")
    for error in result.errors:
        print(f"  - {error.message}")
```

### Timeouts

```python
try:
    result = client.bulk.query(
        "SELECT * FROM Account",
        timeout_minutes=5
    )
except TimeoutError as e:
    print(f"Query timed out: {e}")
```

---

## Performance Guidelines

### Recommended Batch Sizes

| Records | Recommended Operation |
|---------|----------------------|
| < 2,000 | Standard API (client.create, client.update) |
| 2,000 - 10,000,000 | Bulk API v2 (client.bulk) |
| > 10,000,000 | Bulk API v2 with batching |

### Optimal Settings

```python
# For large datasets (> 100k records)
result = client.bulk.insert(
    "Account",
    records,
    wait=True,
    timeout_minutes=30  # Increase timeout
)

# For quick operations (< 10k records)
result = client.bulk.insert(
    "Account",
    records,
    wait=True,
    timeout_minutes=5
)
```

### Polling Strategy

The Bulk API client uses exponential backoff:
- Initial delay: 2 seconds
- Max delay: 30 seconds
- Backoff factor: 1.5x

This minimizes API calls while ensuring responsive status updates.

---

## Limitations

| Limit | Value |
|-------|-------|
| Max records per job | 150 million |
| Max file size | 100 MB (CSV) |
| Max concurrent jobs | 5,000 per org |
| Job retention | 7 days |
| Query timeout | 10 minutes |

**Note**: Salesforce-imposed limits, not library limitations.

---

## Best Practices

### 1. Use Bulk API for Large Datasets

```python
# ❌ Inefficient for 10k records
for record in records:
    client.create("Account", record)

# ✅ Efficient
result = client.bulk.insert("Account", records)
```

### 2. Handle Partial Failures

```python
result = client.bulk.insert("Account", records)

if result.failed_count > 0:
    # Retry failed records
    failed_data = result.failed_records
    # ... implement retry logic
```

### 3. Use External IDs for Upsert

```python
# Prevents duplicates
result = client.bulk.upsert(
    "Account",
    records,
    external_id_field="External_Key__c"
)
```

### 4. Monitor Progress for Large Jobs

```python
def log_progress(job):
    percent = (job.number_records_processed / total_records) * 100
    print(f"Progress: {percent:.1f}%")

result = client.bulk.insert(
    "Account",
    large_dataset,
    on_progress=log_progress
)
```

---

## Troubleshooting

### Common Issues

**Issue**: Job fails immediately
- **Cause**: Invalid field names or missing required fields
- **Solution**: Verify field API names match Salesforce schema

**Issue**: Timeout errors
- **Cause**: Job takes longer than specified timeout
- **Solution**: Increase `timeout_minutes` parameter

**Issue**: Empty success_records
- **Cause**: All records failed validation
- **Solution**: Check `result.errors` for details

### Debug Mode

```python
# Enable detailed logging
import logging
logging.basicConfig(level=logging.DEBUG)

result = client.bulk.insert("Account", records)
```

---

## Migration from Bulk API v1

If migrating from the old Bulk API v1:

| v1 Concept | v2 Equivalent |
|------------|---------------|
| Batch | Job (single batch) |
| ResultList | BulkResult.success_records |
| Error list | BulkResult.errors |
| Locators | Not needed (automatic) |

**Key Differences**:
- v2 uses single-batch jobs (simpler)
- No manual batch management
- Better error reporting
- Faster processing

---

## Related Documentation

- [Bulk API v2 Quick Start](../guides/BULK_QUICKSTART.md)
- [Migration Guide](../guides/BULK_MIGRATION.md)
- [Examples](../examples/BULK_EXAMPLES.md)
- [Salesforce Official Docs](https://developer.salesforce.com/docs/atlas.en-us.api_asynch.meta/api_asynch/)
