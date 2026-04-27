# Kinetic Core - Complete User Guide

Complete guide to using Kinetic Core for Salesforce integration.

> **Part of the [KineticMCP](https://kineticmcp.com) ecosystem** - AI-powered Salesforce integration tools by [Antonio Trento](https://antoniotrento.net)

---

## Table of Contents

1. [Getting Started](#getting-started)
2. [Authentication](#authentication)
3. [Basic Operations](#basic-operations)
4. [Bulk Operations](#bulk-operations)
5. [Field Mapping](#field-mapping)
6. [ETL Pipelines](#etl-pipelines)
7. [Best Practices](#best-practices)
8. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Installation

```bash
pip install kinetic-core
```

### Quick Start

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# 1. Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()

# 2. Create client
client = SalesforceClient(session)

# 3. Use it!
accounts = client.query("SELECT Id, Name FROM Account LIMIT 5")
for account in accounts:
    print(f"{account['Name']} - {account['Id']}")
```

---

## Authentication

Kinetic Core supports two authentication methods:

### JWT Authentication (Recommended)

**Best for**: Production environments, CI/CD pipelines, automated scripts

**Setup**:

1. Create a Connected App in Salesforce
2. Upload your certificate
3. Configure environment variables

**Environment Variables**:
```bash
# Required
SALESFORCE_CLIENT_ID=your_consumer_key
SALESFORCE_USERNAME=your_username
SALESFORCE_PRIVATE_KEY_PATH=/path/to/server.key

# Optional
SALESFORCE_LOGIN_URL=https://login.salesforce.com  # or test.salesforce.com for sandbox
```

**Usage**:
```python
from kinetic_core import JWTAuthenticator

# Load from environment
auth = JWTAuthenticator.from_env()
session = auth.authenticate()

# Or configure manually
auth = JWTAuthenticator(
    client_id="your_consumer_key",
    username="your_username",
    private_key_path="/path/to/server.key",
    login_url="https://login.salesforce.com"
)
session = auth.authenticate()
```

### OAuth 2.0 Password Flow

**Best for**: Development, testing, quick prototypes

**Environment Variables**:
```bash
SALESFORCE_CLIENT_ID=your_consumer_key
SALESFORCE_CLIENT_SECRET=your_consumer_secret
SALESFORCE_USERNAME=your_username
SALESFORCE_PASSWORD=your_password
SALESFORCE_SECURITY_TOKEN=your_security_token
```

**Usage**:
```python
from kinetic_core import OAuthAuthenticator

# Load from environment
auth = OAuthAuthenticator.from_env()
session = auth.authenticate()
```

**See Also**: [Complete Authentication Reference](api/AUTHENTICATION.md)

---

## Basic Operations

### CRUD Operations

#### Create Records

```python
from kinetic_core import SalesforceClient

# Create single record
account_id = client.create("Account", {
    "Name": "ACME Corporation",
    "Industry": "Technology",
    "AnnualRevenue": 5000000,
    "BillingCity": "San Francisco"
})

print(f"Created Account: {account_id}")
```

#### Read Records

```python
# Get by ID
account = client.get("Account", "001xxx000001AAA")
print(account['Name'])

# Query with SOQL
accounts = client.query(
    "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology'"
)

for account in accounts:
    print(f"{account['Name']} - {account['Industry']}")
```

#### Update Records

```python
# Update single record
client.update("Account", "001xxx000001", {
    "Phone": "555-1234",
    "Industry": "Software"
})

print("Account updated!")
```

#### Delete Records

```python
# Delete single record
client.delete("Account", "001xxx000001")
print("Account deleted!")
```

#### Upsert Records

```python
# Insert or update based on external ID
result = client.upsert(
    sobject="Account",
    external_id_field="External_Key__c",
    external_id_value="EXT-001",
    data={
        "Name": "ACME Corp",
        "Industry": "Technology"
    }
)

if result['created']:
    print(f"Created new record: {result['id']}")
else:
    print(f"Updated existing: {result['id']}")
```

### Batch Operations

For 2-200 records, use Composite API:

```python
# Create multiple records
accounts = [
    {"Name": "Account 1", "Industry": "Tech"},
    {"Name": "Account 2", "Industry": "Finance"},
    {"Name": "Account 3", "Industry": "Retail"}
]

result = client.create_bulk("Account", accounts)

# Check results
for item in result['compositeResponse']:
    if item['httpStatusCode'] == 201:
        print(f"Created: {item['body']['id']}")
    else:
        print(f"Failed: {item['body'][0]['message']}")
```

**See Also**: [Complete CRUD Reference](api/CRUD_OPERATIONS.md)

---

## Bulk Operations

For high-volume operations (> 2,000 records), use Bulk API v2.

### Bulk Insert

```python
# Prepare large dataset
accounts = [
    {"Name": f"Account {i}", "Industry": "Technology"}
    for i in range(10000)
]

# Bulk insert
result = client.bulk.insert("Account", accounts)

print(f"Success: {result.success_count}")
print(f"Failed: {result.failed_count}")

# Get created IDs
for record in result.success_records:
    print(f"Created: {record['sf__Id']}")
```

### Bulk Update

```python
# Prepare updates (must include Id)
updates = [
    {"Id": "001xxx000001", "Industry": "Software"},
    {"Id": "001xxx000002", "Industry": "Hardware"}
]

result = client.bulk.update("Account", updates)

if result.failed_count > 0:
    for error in result.errors:
        print(f"Error: {error.message}")
```

### Bulk Upsert

```python
# Use external ID to prevent duplicates
records = [
    {"External_Key__c": "EXT001", "Name": "Company 1"},
    {"External_Key__c": "EXT002", "Name": "Company 2"}
]

result = client.bulk.upsert(
    "Account",
    records,
    external_id_field="External_Key__c"
)

print(f"Inserted/Updated: {result.success_count}")
```

### Bulk Query

```python
# Query large dataset
query = """
    SELECT Id, Name, Industry, AnnualRevenue
    FROM Account
    WHERE CreatedDate = THIS_YEAR
"""

result = client.bulk.query(query)

print(f"Retrieved {result.record_count} records")

# Process results
for account in result.records:
    print(f"{account['Name']} - {account['Industry']}")
```

### Progress Tracking

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

**See Also**:
- [Bulk API v2 Reference](api/BULK_API_V2.md)
- [Bulk Quick Start](guides/BULK_QUICKSTART.md)
- [Bulk Examples](examples/BULK_EXAMPLES.md)

---

## Field Mapping

Transform data between different schemas with `FieldMapper`.

### Basic Mapping

```python
from kinetic_core import FieldMapper

# Simple field renaming
mapper = FieldMapper({
    "customer_name": "Name",
    "customer_email": "Email",
    "customer_phone": "Phone"
})

source = {
    "customer_name": "ACME Corp",
    "customer_email": "info@acme.com",
    "customer_phone": "555-1234"
}

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Corp",
#   "Email": "info@acme.com",
#   "Phone": "555-1234"
# }
```

### With Transformations

```python
# Apply custom transformations
mapper = FieldMapper({
    "name": "Name",
    "email": ("Email", lambda x: x.lower()),
    "revenue": ("AnnualRevenue", lambda x: float(x.replace("$", "").replace(",", ""))),
    "employees": ("NumberOfEmployees", int)
})

source = {
    "name": "ACME Corp",
    "email": "INFO@ACME.COM",
    "revenue": "$5,000,000",
    "employees": "250"
}

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Corp",
#   "Email": "info@acme.com",
#   "AnnualRevenue": 5000000.0,
#   "NumberOfEmployees": 250
# }
```

### With Default Values

```python
# Provide defaults for missing fields
mapper = FieldMapper({
    "name": "Name",
    "email": "Email",
    "status": ("Status__c", None, "Active"),
    "created_date": ("CreatedDate", None, "2025-01-02")
})

source = {"name": "ACME Corp", "email": "info@acme.com"}

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Corp",
#   "Email": "info@acme.com",
#   "Status__c": "Active",         # Used default
#   "CreatedDate": "2025-01-02"    # Used default
# }
```

### Nested Fields

```python
# Access nested data with dot notation
source = {
    "company": {
        "name": "ACME Corp",
        "contact": {
            "email": "info@acme.com",
            "phone": "555-1234"
        }
    }
}

mapper = FieldMapper({
    "company.name": "Name",
    "company.contact.email": "Email",
    "company.contact.phone": "Phone"
})

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Corp",
#   "Email": "info@acme.com",
#   "Phone": "555-1234"
# }
```

### Batch Transformation

```python
# Transform multiple records
records = [
    {"name": "Alice", "age": 30},
    {"name": "Bob", "age": 25},
    {"name": "Charlie", "age": 35}
]

mapper = FieldMapper({
    "name": "Name",
    "age": "Age__c"
})

results = mapper.transform_batch(records)

for result in results:
    print(result)
# {"Name": "Alice", "Age__c": 30}
# {"Name": "Bob", "Age__c": 25}
# {"Name": "Charlie", "Age__c": 35}
```

**See Also**: [Complete Field Mapping Reference](api/FIELD_MAPPING.md)

---

## ETL Pipelines

Orchestrate complete data synchronization workflows with `SyncPipeline`.

### Basic Pipeline

```python
from kinetic_core import FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Setup mapping
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
    mode=SyncMode.INSERT
)

# Prepare data
source_data = [
    {"customer_name": "ACME", "customer_email": "info@acme.com"},
    {"customer_name": "Globex", "customer_email": "contact@globex.com"}
]

# Execute sync
result = pipeline.sync(source_data)

print(f"Success: {result.success_count}/{result.total_records}")
print(f"Failed: {result.error_count}")
```

### Sync Modes

#### INSERT Mode

```python
# Create new records
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT
)

data = [{"Name": "Company 1"}, {"Name": "Company 2"}]
result = pipeline.sync(data)
```

#### UPDATE Mode

```python
# Update existing records (requires Id)
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
```

#### UPSERT Mode

```python
# Insert or update based on external ID
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPSERT,
    external_id_field="External_Key__c"
)

data = [
    {"External_Key__c": "EXT001", "Name": "ACME"},
    {"External_Key__c": "EXT002", "Name": "Globex"}
]

result = pipeline.sync(data)
```

### Progress Callbacks

```python
def on_batch(batch_num, total_batches, result):
    print(f"Batch {batch_num}/{total_batches} complete")
    print(f"  Success: {result.success_count}")
    print(f"  Errors: {result.error_count}")

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.INSERT,
    callbacks={"on_batch_complete": on_batch}
)

result = pipeline.sync(large_dataset)
```

### Configuration-Driven Pipeline

```python
# Define configuration
config = {
    "sobject": "Account",
    "mode": "upsert",
    "external_id_field": "External_Key__c",
    "batch_size": 100,
    "mapping": {
        "ext_id": "External_Key__c",
        "company": "Name",
        "email": ("Email", lambda x: x.lower())
    }
}

# Create from config
pipeline = SyncPipeline.from_config(config, client)

# Use as normal
result = pipeline.sync(data)
```

**See Also**: [Complete Pipelines Reference](api/PIPELINES.md)

---

## Best Practices

### 1. Choose the Right API

| Records | Recommended Method |
|---------|-------------------|
| 1 | `client.create()` |
| 2-200 | `client.create_bulk()` |
| 200-2,000 | Loop with batches |
| > 2,000 | `client.bulk.insert()` |

```python
# Good: Use Bulk API for large datasets
if len(records) > 2000:
    result = client.bulk.insert("Account", records)
else:
    result = client.create_bulk("Account", records)
```

### 2. Use External IDs

```python
# Good: Prevents duplicates
result = client.bulk.upsert(
    "Account",
    records,
    external_id_field="External_Key__c"
)

# Bad: May create duplicates
result = client.bulk.insert("Account", records)
```

### 3. Handle Errors Gracefully

```python
result = client.bulk.insert("Account", records)

if result.failed_count > 0:
    print(f"⚠️  {result.failed_count} records failed")

    # Log errors
    with open("errors.log", "w") as f:
        for error in result.errors:
            f.write(f"{error.message}\n")

    # Retry failed records
    failed_records = result.failed_records
    # ... implement retry logic
```

### 4. Validate Data

```python
def validate_account(data):
    """Validate before creating"""
    required = ['Name']
    for field in required:
        if field not in data or not data[field]:
            raise ValueError(f"Missing required field: {field}")

    if 'AnnualRevenue' in data:
        if not isinstance(data['AnnualRevenue'], (int, float)):
            raise ValueError("AnnualRevenue must be numeric")

# Validate before insert
for record in records:
    validate_account(record)

result = client.bulk.insert("Account", records)
```

### 5. Use Field Mapping

```python
# Good: Centralized transformation logic
mapper = FieldMapper({
    "cust_name": "Name",
    "cust_email": ("Email", lambda x: x.lower())
})

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper
)

# Bad: Manual transformation
transformed = [
    {"Name": rec["cust_name"], "Email": rec["cust_email"].lower()}
    for rec in records
]
```

### 6. Monitor Performance

```python
result = pipeline.sync(data)

# Check metrics
print(f"Duration: {result.metadata['elapsed_seconds']}s")
print(f"Throughput: {result.metadata['records_per_second']} rec/sec")

# Adjust if needed
if result.metadata['records_per_second'] < 50:
    print("Consider using larger batch_size or Bulk API")
```

---

## Troubleshooting

### Authentication Errors

**Problem**: `AuthenticationError: Invalid credentials`

**Solutions**:
1. Check environment variables are set correctly
2. Verify certificate path for JWT
3. Ensure security token is appended for OAuth
4. Check login URL (login.salesforce.com vs test.salesforce.com)

```python
# Debug authentication
import os
print("Client ID:", os.getenv("SALESFORCE_CLIENT_ID"))
print("Username:", os.getenv("SALESFORCE_USERNAME"))
print("Key exists:", os.path.exists(os.getenv("SALESFORCE_PRIVATE_KEY_PATH")))
```

### Query Errors

**Problem**: `SalesforceAPIError: INVALID_FIELD`

**Solution**: Verify field API names

```python
# Check object metadata
metadata = client.describe("Account")

# List all fields
for field in metadata['fields']:
    print(f"{field['name']} ({field['type']})")
```

### Bulk API Timeouts

**Problem**: `TimeoutError: Job did not complete`

**Solution**: Increase timeout

```python
# Increase timeout for large datasets
result = client.bulk.query(
    "SELECT * FROM Account",
    timeout_minutes=30  # Default is 10
)
```

### Rate Limit Errors

**Problem**: `REQUEST_LIMIT_EXCEEDED`

**Solutions**:
1. Use Bulk API instead of standard API
2. Reduce batch sizes
3. Implement exponential backoff
4. Check org API limits

```python
# Check API usage (requires custom implementation)
# Salesforce Limits API: /services/data/vXX.0/limits
```

### Data Validation Errors

**Problem**: `REQUIRED_FIELD_MISSING` or `INVALID_TYPE_FOR_FIELD`

**Solution**: Validate data before sync

```python
# Validate against Salesforce schema
metadata = client.describe("Account")

required_fields = [
    field['name'] for field in metadata['fields']
    if not field['nillable'] and field['createable']
]

# Check records
for record in records:
    for field in required_fields:
        if field not in record:
            print(f"Missing required field: {field}")
```

---

## Real-World Examples

### Example 1: CSV Import

```python
import csv
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Read CSV
with open("customers.csv", "r") as f:
    reader = csv.DictReader(f)
    csv_data = list(reader)

# Setup mapping
mapper = FieldMapper({
    "Company Name": "Name",
    "Email": ("Email", lambda x: x.lower()),
    "Phone": "Phone",
    "Industry": "Industry"
})

# Create pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT
)

# Import
result = pipeline.sync(csv_data)

print(f"Imported {result.success_count} records")
```

### Example 2: Database Sync

```python
import psycopg2
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Fetch from database
conn = psycopg2.connect("dbname=mydb user=user")
cursor = conn.cursor()
cursor.execute("SELECT id, name, email FROM customers")

# Convert to dict
columns = [desc[0] for desc in cursor.description]
db_data = [dict(zip(columns, row)) for row in cursor.fetchall()]

# Setup sync
mapper = FieldMapper({
    "id": "External_Id__c",
    "name": "Name",
    "email": ("Email", lambda x: x.lower())
})

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.UPSERT,
    external_id_field="External_Id__c"
)

# Sync
result = pipeline.sync(db_data)

print(f"Synced {result.success_count} records")
conn.close()
```

### Example 3: API Integration

```python
import requests
from kinetic_core import SalesforceClient, FieldMapper
from kinetic_core.pipeline import SyncPipeline, SyncMode

# Fetch from API
response = requests.get("https://api.example.com/customers")
api_data = response.json()

# Setup mapping for nested API response
mapper = FieldMapper({
    "company.name": "Name",
    "company.industry": "Industry",
    "contact.email": "Email",
    "contact.phone": "Phone"
})

# Sync to Salesforce
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT
)

result = pipeline.sync(api_data)

print(f"Imported {result.success_count} accounts from API")
```

---

## Additional Resources

### Documentation

- [Authentication Guide](api/AUTHENTICATION.md)
- [CRUD Operations Reference](api/CRUD_OPERATIONS.md)
- [Bulk API v2 Reference](api/BULK_API_V2.md)
- [Field Mapping Guide](api/FIELD_MAPPING.md)
- [Pipelines Reference](api/PIPELINES.md)
- [Bulk Quick Start](guides/BULK_QUICKSTART.md)
- [Bulk Examples](examples/BULK_EXAMPLES.md)

### External Resources

- [Salesforce REST API Documentation](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/)
- [SOQL Reference](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/)
- [Salesforce Bulk API v2](https://developer.salesforce.com/docs/atlas.en-us.api_asynch.meta/api_asynch/)

### Community

- [GitHub Repository](https://github.com/antonio-backend-projects/kinetic-core)
- [KineticMCP Website](https://kineticmcp.com)
- [Report Issues](https://github.com/antonio-backend-projects/kinetic-core/issues)

---

## About

**Kinetic Core** is the foundational library powering the KineticMCP ecosystem.

- **Author**: [Antonio Trento](https://antoniotrento.net)
- **Website**: [KineticMCP.com](https://kineticmcp.com)
- **License**: MIT
- **Version**: 2.0.0

Part of the KineticMCP ecosystem - AI-powered Salesforce integration tools.
