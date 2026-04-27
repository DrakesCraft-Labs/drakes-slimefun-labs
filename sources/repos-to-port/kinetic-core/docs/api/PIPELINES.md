# Pipelines - Complete Reference

Orchestrate data synchronization between external sources and Salesforce with flexible, configuration-driven ETL pipelines.

## Overview

The `SyncPipeline` provides a complete framework for Extract-Transform-Load (ETL) operations, enabling you to:

- **Extract** data from any source (databases, APIs, CSV files, etc.)
- **Transform** data using FieldMapper
- **Load** data to Salesforce with comprehensive error handling
- **Track** progress with callbacks and detailed logging

**Key Features**:
- ✅ Multiple sync modes (INSERT, UPDATE, UPSERT, DELETE)
- ✅ Batch processing for efficiency
- ✅ Integrated field mapping
- ✅ Progress callbacks and event hooks
- ✅ Comprehensive error tracking
- ✅ Configuration-driven setup
- ✅ Flexible and extensible architecture

---

## Quick Reference

| Component | Description |
|-----------|-------------|
| `SyncPipeline` | Main pipeline class |
| `SyncMode` | Sync operation modes |
| `SyncResult` | Operation results and metrics |
| `SyncStatus` | Pipeline execution status |

---

## SyncMode Enum

Defines the type of synchronization operation.

**Values**:

| Mode | Description | Requirements |
|------|-------------|--------------|
| `INSERT` | Create new records | - |
| `UPDATE` | Update existing records | Requires `Id` field |
| `UPSERT` | Insert or update | Requires external ID field |
| `DELETE` | Delete records | Requires `Id` field |

**Example**:
```python
from kinetic_core.pipeline import SyncMode

# Use different modes
mode_insert = SyncMode.INSERT
mode_update = SyncMode.UPDATE
mode_upsert = SyncMode.UPSERT
mode_delete = SyncMode.DELETE
```

---

## SyncStatus Enum

Tracks the status of pipeline execution.

**Values**:

| Status | Description |
|--------|-------------|
| `PENDING` | Not yet started |
| `IN_PROGRESS` | Currently running |
| `SUCCESS` | All records processed successfully |
| `FAILED` | All records failed |
| `PARTIAL` | Some succeeded, some failed |

---

## SyncResult Class

Contains results and metrics from a synchronization operation.

**Properties**:
- `status` (SyncStatus): Overall sync status
- `total_records` (int): Total records processed
- `success_count` (int): Number of successful operations
- `error_count` (int): Number of failed operations
- `errors` (List[Dict]): Detailed error information
- `salesforce_ids` (List[str]): Created/updated Salesforce IDs
- `metadata` (Dict): Additional metadata (timing, throughput)

**Methods**:
- `success_rate` (property): Calculate success percentage
- `add_success(salesforce_id)`: Record a successful operation
- `add_error(record_data, error)`: Record a failed operation

**Example**:
```python
result = pipeline.sync(data)

print(f"Status: {result.status.value}")
print(f"Total: {result.total_records}")
print(f"Success: {result.success_count}")
print(f"Errors: {result.error_count}")
print(f"Success Rate: {result.success_rate:.1f}%")

# Access created IDs
for sf_id in result.salesforce_ids:
    print(f"Created: {sf_id}")

# Check errors
if result.error_count > 0:
    for error in result.errors:
        print(f"Failed record: {error['record']}")
        print(f"Error: {error['error']}")

# Metadata
print(f"Elapsed: {result.metadata['elapsed_seconds']}s")
print(f"Throughput: {result.metadata['records_per_second']} rec/sec")
```

---

## SyncPipeline Class

Main pipeline class for orchestrating data synchronization.

### Initialization

**Signature**:
```python
def __init__(
    self,
    client: SalesforceClient,
    sobject: str,
    mapper: Optional[FieldMapper] = None,
    mode: SyncMode = SyncMode.INSERT,
    external_id_field: Optional[str] = None,
    batch_size: int = 200,
    stop_on_error: bool = False,
    callbacks: Optional[Dict[str, Callable]] = None
)
```

**Parameters**:
- `client` (SalesforceClient): Authenticated Salesforce client
- `sobject` (str): Salesforce object API name
- `mapper` (FieldMapper, optional): Field mapping for data transformation
- `mode` (SyncMode): Sync operation mode (default: INSERT)
- `external_id_field` (str, optional): External ID field name (required for UPSERT)
- `batch_size` (int): Records per batch (default: 200)
- `stop_on_error` (bool): Stop on first error (default: False)
- `callbacks` (dict, optional): Event callback functions

**Example**:
```python
from kinetic_core import JWTAuthenticator, SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Setup client
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Setup mapper
mapper = FieldMapper({
    "customer_name": "Name",
    "customer_email": "Email",
    "customer_phone": "Phone"
})

# Create pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    batch_size=100
)
```

---

## Core Methods

### sync()

Execute the synchronization pipeline.

**Signature**:
```python
def sync(self, source_data: List[Dict[str, Any]]) -> SyncResult
```

**Parameters**:
- `source_data` (List[Dict]): List of source data dictionaries

**Returns**: `SyncResult` with operation results

**Example**:
```python
# Prepare source data
source_data = [
    {"customer_name": "ACME Corp", "customer_email": "info@acme.com"},
    {"customer_name": "Globex Inc", "customer_email": "contact@globex.com"},
    {"customer_name": "Initech", "customer_email": "admin@initech.com"}
]

# Execute sync
result = pipeline.sync(source_data)

# Check results
if result.status == SyncStatus.SUCCESS:
    print(f"✓ All {result.success_count} records synced successfully!")
elif result.status == SyncStatus.PARTIAL:
    print(f"⚠ Partial success: {result.success_count}/{result.total_records}")
    print(f"Errors: {result.error_count}")
else:
    print(f"✗ Sync failed: {result.error_count} errors")

# Display errors
for error in result.errors:
    print(f"  - {error['error']}")
```

---

### from_config()

Create pipeline from configuration dictionary.

**Signature**:
```python
@classmethod
def from_config(
    cls,
    config: Dict[str, Any],
    client: SalesforceClient
) -> SyncPipeline
```

**Parameters**:
- `config` (dict): Configuration with keys:
  - `sobject` (str, required): Salesforce object name
  - `mode` (str): Sync mode (default: "insert")
  - `external_id_field` (str): External ID field for upsert
  - `batch_size` (int): Batch size (default: 200)
  - `stop_on_error` (bool): Stop on error flag (default: False)
  - `mapping` (dict): Field mapping configuration
- `client` (SalesforceClient): Salesforce client

**Returns**: Configured `SyncPipeline` instance

**Example**:
```python
config = {
    "sobject": "Account",
    "mode": "upsert",
    "external_id_field": "External_Key__c",
    "batch_size": 100,
    "stop_on_error": False,
    "mapping": {
        "ext_id": "External_Key__c",
        "company": "Name",
        "email": ("Email", lambda x: x.lower()),
        "industry": "Industry"
    }
}

pipeline = SyncPipeline.from_config(config, client)

# Use pipeline
result = pipeline.sync(source_data)
```

---

## Sync Modes in Detail

### INSERT Mode

Create new records in Salesforce.

**Requirements**: None

**Example**:
```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT
)

data = [
    {"Name": "New Company 1"},
    {"Name": "New Company 2"}
]

result = pipeline.sync(data)
print(f"Created {result.success_count} accounts")
```

---

### UPDATE Mode

Update existing records.

**Requirements**: All records must include `Id` field

**Example**:
```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPDATE
)

data = [
    {"Id": "001xxx000001", "Phone": "555-1111"},
    {"Id": "001xxx000002", "Phone": "555-2222"}
]

result = pipeline.sync(data)
print(f"Updated {result.success_count} accounts")
```

**With Field Mapping**:
```python
mapper = FieldMapper({
    "account_id": "Id",
    "new_phone": "Phone",
    "new_industry": "Industry"
})

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.UPDATE
)

data = [
    {"account_id": "001xxx000001", "new_phone": "555-1111", "new_industry": "Tech"}
]

result = pipeline.sync(data)
```

---

### UPSERT Mode

Insert new records or update existing based on external ID.

**Requirements**:
- `external_id_field` parameter must be set
- All records must include the external ID field

**Example**:
```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPSERT,
    external_id_field="External_Key__c"
)

data = [
    {"External_Key__c": "EXT001", "Name": "ACME Corp"},
    {"External_Key__c": "EXT002", "Name": "Globex Inc"}
]

result = pipeline.sync(data)
print(f"Upserted {result.success_count} accounts")
```

**Prevents Duplicates**:
```python
# First run: Creates 2 records
result1 = pipeline.sync(data)
print(f"Created: {result1.success_count}")  # 2

# Second run: Updates same 2 records (no duplicates)
data[0]["Name"] = "ACME Corporation"  # Changed name
result2 = pipeline.sync(data)
print(f"Updated: {result2.success_count}")  # 2 (same records)
```

---

### DELETE Mode

Delete records from Salesforce (moves to recycle bin).

**Requirements**: All records must include `Id` field

**Example**:
```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.DELETE
)

data = [
    {"Id": "001xxx000001"},
    {"Id": "001xxx000002"}
]

result = pipeline.sync(data)
print(f"Deleted {result.success_count} accounts")
```

---

## Callbacks and Event Hooks

Monitor pipeline execution with custom callbacks.

**Available Callbacks**:

| Callback | When Called | Parameters |
|----------|-------------|------------|
| `on_record_start` | Before processing each record | `(record)` |
| `on_record_success` | After successful processing | `(record, salesforce_id)` |
| `on_record_error` | After failed processing | `(record, error)` |
| `on_batch_complete` | After each batch | `(batch_num, total_batches, result)` |

**Example**:
```python
def on_start(record):
    print(f"Processing: {record.get('Name', 'Unknown')}")

def on_success(record, sf_id):
    print(f"✓ Created {sf_id} for {record.get('Name')}")

def on_error(record, error):
    print(f"✗ Failed: {record.get('Name')} - {error}")

def on_batch(batch_num, total_batches, result):
    print(f"Batch {batch_num}/{total_batches} complete")
    print(f"  Success: {result.success_count}, Errors: {result.error_count}")

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    callbacks={
        "on_record_start": on_start,
        "on_record_success": on_success,
        "on_record_error": on_error,
        "on_batch_complete": on_batch
    }
)

result = pipeline.sync(data)
```

**Progress Bar Example**:
```python
from tqdm import tqdm

progress = None

def on_batch(batch_num, total_batches, result):
    global progress
    if progress is None:
        progress = tqdm(total=result.total_records, desc="Syncing")
    progress.update(result.success_count + result.error_count - progress.n)

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    callbacks={"on_batch_complete": on_batch}
)

result = pipeline.sync(large_dataset)
progress.close()
```

---

## Error Handling

### Stop on Error

**Default Behavior** (`stop_on_error=False`):
- Pipeline continues processing even if errors occur
- All errors are collected in `SyncResult.errors`
- Status becomes `PARTIAL` if some succeed

```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    stop_on_error=False  # Default
)

result = pipeline.sync(data)

# Check partial failures
if result.status == SyncStatus.PARTIAL:
    print(f"Processed: {result.total_records}")
    print(f"Success: {result.success_count}")
    print(f"Failed: {result.error_count}")

    for error in result.errors:
        print(f"Error: {error['error']}")
```

**Stop on First Error** (`stop_on_error=True`):
- Pipeline stops immediately on first error
- Remaining records are not processed
- Useful for data integrity scenarios

```python
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    stop_on_error=True
)

try:
    result = pipeline.sync(data)
except Exception as e:
    print(f"Pipeline stopped due to error: {e}")
```

---

## Real-World Examples

### Example 1: CSV Import

```python
import csv
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Read CSV
with open("accounts.csv", "r") as f:
    reader = csv.DictReader(f)
    csv_data = list(reader)

# Setup mapping
mapper = FieldMapper({
    "Company Name": "Name",
    "Email Address": ("Email", lambda x: x.lower()),
    "Phone Number": "Phone",
    "Industry Type": "Industry",
    "Annual Revenue": ("AnnualRevenue", float)
})

# Create pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    batch_size=200
)

# Import
result = pipeline.sync(csv_data)

print(f"Import complete:")
print(f"  Success: {result.success_count}")
print(f"  Failed: {result.error_count}")
print(f"  Duration: {result.metadata['elapsed_seconds']}s")
```

---

### Example 2: Database to Salesforce Sync

```python
import psycopg2
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Fetch from PostgreSQL
conn = psycopg2.connect("dbname=mydb user=user password=pass")
cursor = conn.cursor()
cursor.execute("SELECT id, name, email, phone FROM customers WHERE synced = false")

# Convert to dict
columns = [desc[0] for desc in cursor.description]
db_data = [dict(zip(columns, row)) for row in cursor.fetchall()]

# Setup sync
mapper = FieldMapper({
    "id": "External_Customer_Id__c",
    "name": "Name",
    "email": ("Email", lambda x: x.lower()),
    "phone": "Phone"
})

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.UPSERT,
    external_id_field="External_Customer_Id__c"
)

# Sync
result = pipeline.sync(db_data)

# Update database
if result.success_count > 0:
    success_ids = [r['id'] for r in db_data[:result.success_count]]
    cursor.execute(
        "UPDATE customers SET synced = true WHERE id = ANY(%s)",
        (success_ids,)
    )
    conn.commit()

conn.close()

print(f"Synced {result.success_count} customers from database")
```

---

### Example 3: API to Salesforce with Retry

```python
import requests
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode, SyncStatus

# Fetch from external API
response = requests.get("https://api.example.com/contacts")
api_data = response.json()

# Setup pipeline
mapper = FieldMapper({
    "external_id": "External_Id__c",
    "first_name": "FirstName",
    "last_name": "LastName",
    "email": ("Email", lambda x: x.lower()),
    "company_name": "Account.Name"  # Nested
})

pipeline = SyncPipeline(
    client=client,
    sobject="Contact",
    mapper=mapper,
    mode=SyncMode.UPSERT,
    external_id_field="External_Id__c",
    stop_on_error=False
)

# Sync with retry
max_retries = 3
retry_count = 0
failed_records = api_data

while retry_count < max_retries and failed_records:
    print(f"Attempt {retry_count + 1}: Processing {len(failed_records)} records")

    result = pipeline.sync(failed_records)

    print(f"  Success: {result.success_count}, Failed: {result.error_count}")

    if result.status == SyncStatus.SUCCESS:
        break

    # Prepare failed records for retry
    if result.error_count > 0:
        failed_indices = [i for i, error in enumerate(result.errors)]
        failed_records = [result.errors[i]['record'] for i in failed_indices]
        retry_count += 1
    else:
        break

print(f"Final result: {result.success_count} synced, {result.error_count} failed")
```

---

### Example 4: Scheduled Data Sync

```python
import schedule
import time
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

def sync_daily_leads():
    """Sync new leads from external system"""

    # Fetch today's leads
    leads = fetch_leads_from_crm()

    # Setup pipeline
    mapper = FieldMapper({
        "lead_id": "External_Lead_Id__c",
        "first_name": "FirstName",
        "last_name": "LastName",
        "company": "Company",
        "email": ("Email", lambda x: x.lower()),
        "phone": "Phone",
        "status": ("Status", None, "New")
    })

    pipeline = SyncPipeline(
        client=client,
        sobject="Lead",
        mapper=mapper,
        mode=SyncMode.UPSERT,
        external_id_field="External_Lead_Id__c"
    )

    # Sync
    result = pipeline.sync(leads)

    # Log results
    print(f"[{time.strftime('%Y-%m-%d %H:%M:%S')}] Daily sync complete:")
    print(f"  Leads processed: {result.total_records}")
    print(f"  Success: {result.success_count}")
    print(f"  Errors: {result.error_count}")

    # Send notification if errors
    if result.error_count > 0:
        send_error_notification(result.errors)

# Schedule daily sync at 2 AM
schedule.every().day.at("02:00").do(sync_daily_leads)

print("Scheduler started. Waiting for jobs...")
while True:
    schedule.run_pending()
    time.sleep(60)
```

---

### Example 5: Bulk Update with Progress Tracking

```python
from kinetic_core import SalesforceClient
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Query accounts to update
accounts = client.query(
    "SELECT Id, AnnualRevenue FROM Account WHERE Industry = 'Technology'"
)

# Prepare updates
updates = []
for account in accounts:
    revenue = account.get('AnnualRevenue', 0)
    tier = "Platinum" if revenue > 10000000 else "Gold" if revenue > 1000000 else "Silver"

    updates.append({
        "Id": account['Id'],
        "Customer_Tier__c": tier
    })

# Progress tracking
def on_batch(batch_num, total_batches, result):
    percent = (batch_num / total_batches) * 100
    print(f"Progress: {percent:.1f}% ({batch_num}/{total_batches} batches)")
    print(f"  Processed: {result.success_count + result.error_count}/{result.total_records}")

# Update pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPDATE,
    batch_size=200,
    callbacks={"on_batch_complete": on_batch}
)

# Execute update
result = pipeline.sync(updates)

print(f"\n✓ Update complete!")
print(f"  Total: {result.total_records}")
print(f"  Updated: {result.success_count}")
print(f"  Failed: {result.error_count}")
print(f"  Duration: {result.metadata['elapsed_seconds']:.2f}s")
```

---

## Best Practices

### 1. Use Appropriate Batch Sizes

```python
# Good: Batch size based on data volume
if len(data) < 1000:
    batch_size = 200  # Standard API limit
elif len(data) < 10000:
    batch_size = 500
else:
    batch_size = 1000  # Or use Bulk API

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    batch_size=batch_size
)
```

### 2. Always Use External IDs for Upsert

```python
# Good: Prevents duplicates
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPSERT,
    external_id_field="External_Key__c"
)

# Bad: INSERT mode may create duplicates
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT
)
```

### 3. Handle Errors Gracefully

```python
# Good: Collect errors for review
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    stop_on_error=False
)

result = pipeline.sync(data)

if result.error_count > 0:
    # Log errors for investigation
    with open("sync_errors.log", "w") as f:
        for error in result.errors:
            f.write(f"{error}\n")
```

### 4. Use Field Mapping

```python
# Good: Clean separation of concerns
mapper = FieldMapper({...})
pipeline = SyncPipeline(client=client, sobject="Account", mapper=mapper)

# Bad: Pre-transform data manually
transformed_data = [transform(rec) for rec in data]
pipeline = SyncPipeline(client=client, sobject="Account")
```

### 5. Monitor Performance

```python
result = pipeline.sync(data)

# Check performance metrics
print(f"Throughput: {result.metadata['records_per_second']} rec/sec")
print(f"Duration: {result.metadata['elapsed_seconds']}s")

# Adjust batch size if needed
if result.metadata['records_per_second'] < 50:
    print("Performance low, consider increasing batch_size or using Bulk API")
```

---

## Related Documentation

- **[Field Mapping](FIELD_MAPPING.md)** - Data transformation
- **[CRUD Operations](CRUD_OPERATIONS.md)** - Standard operations
- **[Bulk API v2](BULK_API_V2.md)** - High-volume processing

---

## External Resources

- [ETL Best Practices](https://en.wikipedia.org/wiki/Extract,_transform,_load)
- [Salesforce Data Loader](https://help.salesforce.com/s/articleView?id=sf.data_loader.htm)
- [Python Design Patterns](https://refactoring.guru/design-patterns/python)
