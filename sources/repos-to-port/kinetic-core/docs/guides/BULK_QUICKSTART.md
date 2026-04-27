# Bulk API v2 - Quick Start Guide

Get started with Bulk API v2 in 5 minutes.

## Installation

```bash
pip install kinetic-core>=2.0.0
```

## Basic Setup

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()

# Create client
client = SalesforceClient(session)

# Access Bulk API
bulk = client.bulk
```

## 5 Common Use Cases

### 1. Bulk Insert (Create Records)

```python
# Prepare data
accounts = [
    {"Name": "Acme Corp", "Industry": "Technology"},
    {"Name": "Global Inc", "Industry": "Finance"},
    {"Name": "Tech Solutions", "Industry": "Software"}
]

# Insert
result = client.bulk.insert("Account", accounts)

# Check results
print(f"✓ Created: {result.success_count}")
print(f"✗ Failed: {result.failed_count}")

# Get created IDs
for record in result.success_records:
    print(f"Created: {record['sf__Id']}")
```

### 2. Bulk Update

```python
# Prepare updates (must include Id)
updates = [
    {"Id": "001xxx000001", "Industry": "Software"},
    {"Id": "001xxx000002", "AnnualRevenue": 5000000}
]

# Update
result = client.bulk.update("Account", updates)

if result.failed_count > 0:
    for error in result.errors:
        print(f"Error: {error.message}")
```

### 3. Bulk Upsert (Insert or Update)

```python
# Use external ID to prevent duplicates
records = [
    {"External_Key__c": "EXT001", "Name": "New or Existing 1"},
    {"External_Key__c": "EXT002", "Name": "New or Existing 2"}
]

result = client.bulk.upsert(
    "Account",
    records,
    external_id_field="External_Key__c"
)

print(f"Inserted/Updated: {result.success_count}")
```

### 4. Bulk Delete

```python
# Get IDs to delete
ids_to_delete = ["001xxx000001", "001xxx000002"]

# Delete (moves to recycle bin)
result = client.bulk.delete("Account", ids_to_delete)

print(f"Deleted: {result.success_count}")
```

### 5. Bulk Query (Export Data)

```python
# Query large dataset
query = """
    SELECT Id, Name, Industry, CreatedDate
    FROM Account
    WHERE CreatedDate = THIS_YEAR
"""

result = client.bulk.query(query)

print(f"Retrieved {result.record_count} records")

# Process results
for account in result.records:
    print(f"{account['Name']} - {account['Industry']}")
```

## Progress Tracking

Monitor long-running jobs:

```python
def show_progress(job):
    print(f"State: {job.state}")
    print(f"Processed: {job.number_records_processed}")

result = client.bulk.insert(
    "Account",
    large_dataset,
    on_progress=show_progress
)
```

## Error Handling

```python
result = client.bulk.insert("Account", records)

if result.failed_count > 0:
    print(f"⚠️  {result.failed_count} records failed")

    for error in result.errors[:5]:  # Show first 5 errors
        print(f"  - {error.message}")
        print(f"    Fields: {', '.join(error.fields)}")
```

## Async Mode (Non-blocking)

```python
# Start job without waiting
result = client.bulk.insert(
    "Account",
    records,
    wait=False  # Returns immediately
)

print(f"Job started: {result.job.id}")
print(f"State: {result.job.state}")

# Check status later
job = client.bulk.get_job(result.job.id)
print(f"Current state: {job.state}")
```

## Performance Tips

### When to Use Bulk API

- ✅ **Use Bulk API** for > 2,000 records
- ✅ **Use Bulk API** for heavy data migrations
- ✅ **Use Bulk API** for scheduled batch jobs
- ❌ **Don't use** for < 200 records (standard API is faster)
- ❌ **Don't use** for real-time operations

### Optimize Your Batches

```python
# Good: Batch 10k-50k records per operation
records = load_records(limit=25000)
result = client.bulk.insert("Account", records)

# Bad: Too many small operations
for batch in small_batches:
    client.bulk.insert("Account", batch)  # Inefficient
```

## Common Patterns

### Pattern 1: Retry Failed Records

```python
result = client.bulk.insert("Account", records)

if result.failed_count > 0:
    # Extract failed records for retry
    failed_data = [
        records[i] for i, record in enumerate(result.failed_records)
    ]

    # Retry after fixing issues
    retry_result = client.bulk.insert("Account", failed_data)
```

### Pattern 2: Export and Transform

```python
# Export data
result = client.bulk.query("SELECT Id, Name FROM Account LIMIT 10000")

# Transform
transformed = [
    {"Id": r["Id"], "Name": r["Name"].upper()}
    for r in result.records
]

# Import back
client.bulk.update("Account", transformed)
```

### Pattern 3: Incremental Updates

```python
# Get records created today
query = "SELECT Id, Name FROM Account WHERE CreatedDate = TODAY"
result = client.bulk.query(query)

# Apply updates
updates = [
    {"Id": r["Id"], "Status__c": "Processed"}
    for r in result.records
]

client.bulk.update("Account", updates)
```

## Next Steps

- 📖 [Complete API Reference](../api/BULK_API_V2.md)
- 📝 [Migration Guide](BULK_MIGRATION.md)
- 💡 [Advanced Examples](../examples/BULK_EXAMPLES.md)
- 🔧 [Troubleshooting](../api/BULK_API_V2.md#troubleshooting)

## Need Help?

- [GitHub Issues](https://github.com/antonio-backend-projects/kinetic-core/issues)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/kinetic-core)
- [KineticMCP Community](https://kineticmcp.com)
