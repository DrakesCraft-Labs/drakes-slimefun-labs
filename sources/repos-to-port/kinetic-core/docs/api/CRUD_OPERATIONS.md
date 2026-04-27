# CRUD Operations - Complete Reference

Complete guide to Create, Read, Update, and Delete operations in Kinetic Core.

## Overview

The `SalesforceClient` provides comprehensive CRUD operations for any Salesforce object (standard or custom).

**Key Features**:
- ✅ Works with **any** Salesforce object
- ✅ Type-safe with full type hints
- ✅ Automatic error handling
- ✅ Query pagination
- ✅ Bulk create via Composite API
- ✅ Flexible upsert operations

---

## Quick Reference

| Operation | Method | Use Case |
|-----------|--------|----------|
| Create | `create()` | Insert single record |
| Read | `get()` | Retrieve by ID |
| Update | `update()` | Update existing record |
| Delete | `delete()` | Remove record |
| Upsert | `upsert()` | Insert or update |
| Query | `query()` | SOQL queries |
| Query All | `query_all()` | Include deleted/archived |
| Describe | `describe()` | Get object metadata |

---

## Create Operations

### create()

Create a single Salesforce record.

**Signature**:
```python
def create(
    self,
    sobject: str,
    data: Dict[str, Any]
) -> str
```

**Parameters**:
- `sobject` (str): Salesforce object API name
- `data` (dict): Field name-value pairs

**Returns**: Record ID (str)

**Raises**: `SalesforceAPIError` if creation fails

**Example**:
```python
from kinetic_core import JWTAuthenticator, SalesforceClient

auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Create Account
account_id = client.create("Account", {
    "Name": "Acme Corporation",
    "Industry": "Technology",
    "AnnualRevenue": 5000000,
    "BillingCity": "San Francisco"
})

print(f"Created Account: {account_id}")  # 001xxx000001AAA
```

**Custom Objects**:
```python
# Custom object (note __c suffix)
custom_id = client.create("Custom_Object__c", {
    "Name": "Record 1",
    "Custom_Field__c": "Value"
})
```

### create_bulk()

Create multiple records using Composite API.

**Signature**:
```python
def create_bulk(
    self,
    sobject: str,
    records: List[Dict[str, Any]],
    all_or_none: bool = False
) -> Dict[str, Any]
```

**Parameters**:
- `sobject` (str): Object name
- `records` (list): List of record dictionaries
- `all_or_none` (bool): Rollback all if any fails

**Returns**: Dictionary with results

**Limit**: 200 records per call (Salesforce Composite API limit)

**Example**:
```python
# Create multiple accounts
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

**For > 200 records**: Use [Bulk API v2](BULK_API_V2.md)

---

## Read Operations

### get()

Retrieve a record by ID.

**Signature**:
```python
def get(
    self,
    sobject: str,
    record_id: str,
    fields: Optional[List[str]] = None
) -> Dict[str, Any]
```

**Parameters**:
- `sobject` (str): Object name
- `record_id` (str): Salesforce record ID
- `fields` (list, optional): Specific fields to retrieve

**Returns**: Dictionary with record data

**Example**:
```python
# Get all fields
account = client.get("Account", "001xxx000001AAA")
print(account['Name'])
print(account['Industry'])

# Get specific fields only
account = client.get(
    "Account",
    "001xxx000001AAA",
    fields=["Name", "Industry", "AnnualRevenue"]
)
```

### query()

Execute SOQL query.

**Signature**:
```python
def query(
    self,
    soql: str
) -> List[Dict[str, Any]]
```

**Parameters**:
- `soql` (str): SOQL query string

**Returns**: List of records

**Features**:
- ✅ Automatic pagination (handles > 2000 results)
- ✅ Returns all records
- ✅ Follows `nextRecordsUrl` automatically

**Example**:
```python
# Simple query
accounts = client.query(
    "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology'"
)

for account in accounts:
    print(f"{account['Name']} - {account['Industry']}")

# Query with relationships
contacts = client.query("""
    SELECT Id, FirstName, LastName, Account.Name
    FROM Contact
    WHERE Account.Industry = 'Technology'
    LIMIT 100
""")

for contact in contacts:
    print(f"{contact['FirstName']} - {contact['Account']['Name']}")

# Aggregate queries
result = client.query("""
    SELECT Industry, COUNT(Id) total
    FROM Account
    GROUP BY Industry
""")

for row in result:
    print(f"{row['Industry']}: {row['total']}")
```

**SOQL Tips**:
```python
# Date literals
query = "SELECT Id FROM Account WHERE CreatedDate = TODAY"
query = "SELECT Id FROM Account WHERE CreatedDate = LAST_WEEK"
query = "SELECT Id FROM Account WHERE CreatedDate >= 2025-01-01"

# IN clause
query = "SELECT Id FROM Account WHERE Industry IN ('Tech', 'Finance')"

# ORDER BY
query = "SELECT Id, Name FROM Account ORDER BY Name ASC"

# LIMIT
query = "SELECT Id FROM Account LIMIT 1000"
```

### query_all()

Query including deleted and archived records.

**Signature**:
```python
def query_all(
    self,
    soql: str
) -> List[Dict[str, Any]]
```

**Use Cases**:
- Retrieve soft-deleted records
- Access archived data
- Audit trails

**Example**:
```python
# Include deleted records
all_accounts = client.query_all(
    "SELECT Id, Name, IsDeleted FROM Account"
)

deleted = [a for a in all_accounts if a.get('IsDeleted')]
print(f"Found {len(deleted)} deleted accounts")
```

---

## Update Operations

### update()

Update an existing record.

**Signature**:
```python
def update(
    self,
    sobject: str,
    record_id: str,
    data: Dict[str, Any]
) -> bool
```

**Parameters**:
- `sobject` (str): Object name
- `record_id` (str): Record ID to update
- `data` (dict): Fields to update

**Returns**: `True` if successful

**Example**:
```python
# Update single field
client.update("Account", "001xxx000001", {
    "Phone": "555-1234"
})

# Update multiple fields
client.update("Account", "001xxx000001", {
    "Phone": "555-1234",
    "Industry": "Software",
    "AnnualRevenue": 10000000
})

# Update custom fields
client.update("Custom_Object__c", record_id, {
    "Status__c": "Completed",
    "Completion_Date__c": "2025-01-02"
})
```

**Null Values**:
```python
# Set field to null (clear value)
client.update("Account", record_id, {
    "Fax": None,  # Clears the field
    "Description": None
})
```

---

## Delete Operations

### delete()

Delete a record (soft delete to recycle bin).

**Signature**:
```python
def delete(
    self,
    sobject: str,
    record_id: str
) -> bool
```

**Parameters**:
- `sobject` (str): Object name
- `record_id` (str): Record ID

**Returns**: `True` if successful

**Example**:
```python
# Delete single record
client.delete("Account", "001xxx000001")

# Delete multiple (loop)
ids_to_delete = ["001xxx001", "001xxx002", "001xxx003"]
for record_id in ids_to_delete:
    client.delete("Account", record_id)
```

**Note**: For > 100 deletes, use [Bulk API v2](BULK_API_V2.md#delete)

---

## Upsert Operations

### upsert()

Insert new record or update existing based on external ID.

**Signature**:
```python
def upsert(
    self,
    sobject: str,
    external_id_field: str,
    external_id_value: str,
    data: Dict[str, Any]
) -> Dict[str, Any]
```

**Parameters**:
- `sobject` (str): Object name
- `external_id_field` (str): External ID field name
- `external_id_value` (str): External ID value
- `data` (dict): Record data

**Returns**: Dictionary with result details

**Use Cases**:
- Prevent duplicates
- Sync from external systems
- Idempotent operations

**Example**:
```python
# Upsert with custom external ID field
result = client.upsert(
    sobject="Account",
    external_id_field="External_Key__c",
    external_id_value="EXT-001",
    data={
        "Name": "Acme Corp",
        "Industry": "Technology"
    }
)

if result['created']:
    print(f"Created new record: {result['id']}")
else:
    print(f"Updated existing: {result['id']}")

# Upsert Contact with Email as external ID
result = client.upsert(
    sobject="Contact",
    external_id_field="Email",
    external_id_value="john@example.com",
    data={
        "FirstName": "John",
        "LastName": "Doe",
        "Title": "Manager"
    }
)
```

**External ID Fields**:
```python
# Must be configured in Salesforce:
# Setup → Object Manager → Custom Field → "External ID" checkbox
```

---

## Describe Operations

### describe()

Get metadata for a Salesforce object.

**Signature**:
```python
def describe(
    self,
    sobject: str
) -> Dict[str, Any]
```

**Parameters**:
- `sobject` (str): Object name

**Returns**: Complete object metadata

**Example**:
```python
# Get Account metadata
metadata = client.describe("Account")

# Object properties
print(f"Label: {metadata['label']}")
print(f"Createable: {metadata['createable']}")
print(f"Updateable: {metadata['updateable']}")
print(f"Deletable: {metadata['deletable']}")

# Fields
for field in metadata['fields']:
    print(f"Field: {field['name']} ({field['type']})")
    print(f"  Required: {not field['nillable']}")
    print(f"  Unique: {field.get('unique', False)}")

# Record types
for record_type in metadata.get('recordTypeInfos', []):
    print(f"RecordType: {record_type['name']}")
```

**Use Cases**:
- Dynamic field mapping
- Validation
- UI generation
- Documentation

---

## Advanced Examples

### Example 1: Create with Relationships

```python
# Create Contact with Account
account_id = client.create("Account", {
    "Name": "Parent Company"
})

contact_id = client.create("Contact", {
    "FirstName": "John",
    "LastName": "Doe",
    "AccountId": account_id,  # Lookup relationship
    "Email": "john@example.com"
})
```

### Example 2: Query with Parent-Child

```python
# Parent to Child (Contacts from Account)
result = client.query("""
    SELECT Id, Name,
           (SELECT Id, FirstName, LastName FROM Contacts)
    FROM Account
    WHERE Industry = 'Technology'
""")

for account in result:
    print(f"Account: {account['Name']}")
    if account['Contacts']:
        for contact in account['Contacts']['records']:
            print(f"  - {contact['FirstName']} {contact['LastName']}")
```

### Example 3: Batch Create with Error Handling

```python
def safe_create_bulk(client, sobject, records):
    """Create with error handling"""
    batch_size = 200  # Composite API limit

    all_results = []

    for i in range(0, len(records), batch_size):
        batch = records[i:i + batch_size]

        try:
            result = client.create_bulk(sobject, batch)

            # Process results
            for item in result['compositeResponse']:
                if item['httpStatusCode'] == 201:
                    all_results.append({
                        'success': True,
                        'id': item['body']['id']
                    })
                else:
                    all_results.append({
                        'success': False,
                        'error': item['body'][0]['message']
                    })

        except Exception as e:
            print(f"Batch failed: {e}")
            # Add failures
            all_results.extend([
                {'success': False, 'error': str(e)}
                for _ in batch
            ])

    return all_results

# Usage
records = [{"Name": f"Account {i}"} for i in range(500)]
results = safe_create_bulk(client, "Account", records)

successes = sum(1 for r in results if r['success'])
print(f"Created {successes}/{len(records)} records")
```

### Example 4: Conditional Update

```python
# Update only if condition met
accounts = client.query(
    "SELECT Id, AnnualRevenue FROM Account WHERE Industry = 'Technology'"
)

for account in accounts:
    if account.get('AnnualRevenue', 0) > 1000000:
        client.update("Account", account['Id'], {
            "Rating": "Hot",
            "Priority__c": "High"
        })
```

### Example 5: Pagination with Large Result Sets

```python
# Query handles pagination automatically
# Even for > 10,000 results
accounts = client.query(
    "SELECT Id, Name FROM Account"  # No LIMIT = all records
)

print(f"Retrieved {len(accounts)} total records")
```

---

## Best Practices

### 1. Field Validation

```python
# Validate before create/update
def validate_account(data):
    required = ['Name']
    for field in required:
        if field not in data or not data[field]:
            raise ValueError(f"Missing required field: {field}")

    # Validate data types
    if 'AnnualRevenue' in data and not isinstance(data['AnnualRevenue'], (int, float)):
        raise ValueError("AnnualRevenue must be numeric")

data = {"Name": "Acme", "AnnualRevenue": 5000000}
validate_account(data)
client.create("Account", data)
```

### 2. Use External IDs for Upsert

```python
# Good: Prevents duplicates
client.upsert(
    "Account",
    "External_Key__c",
    "EXT-001",
    {"Name": "Acme"}
)

# Bad: May create duplicates
client.create("Account", {"Name": "Acme"})
```

### 3. Batch Operations

```python
# Good: Batch creates
client.create_bulk("Account", records)

# Bad: Loop with single creates (slow)
for record in records:
    client.create("Account", record)
```

### 4. Query Efficiently

```python
# Good: Select only needed fields
accounts = client.query(
    "SELECT Id, Name FROM Account"
)

# Bad: SELECT * equivalent (slower)
accounts = client.query(
    "SELECT FIELDS(ALL) FROM Account"
)
```

### 5. Error Handling

```python
from kinetic_core.exceptions import SalesforceAPIError

try:
    client.create("Account", data)
except SalesforceAPIError as e:
    print(f"Error: {e.error_code}")
    print(f"Message: {e.message}")
    # Handle specific errors
    if e.error_code == "REQUIRED_FIELD_MISSING":
        # Handle missing field
        pass
```

---

## Performance Tips

| Records | Recommended Method |
|---------|-------------------|
| 1 | `create()` |
| 2-200 | `create_bulk()` |
| 200-2000 | Loop with `create_bulk()` in batches |
| > 2000 | [Bulk API v2](BULK_API_V2.md) |

### Optimization Strategies

1. **Use Bulk API v2 for Large Datasets**
2. **Select Only Necessary Fields**
3. **Use WHERE Clauses**
4. **Leverage Indexes** (External ID fields)
5. **Avoid N+1 Queries** (use relationship queries)

---

## Limitations

### API Limits

| Operation | Limit |
|-----------|-------|
| Single Record | 1 per call |
| Composite API | 200 per call |
| SOQL Query | 2000 records (auto-paginated) |
| Query Total | 50,000 rows |
| Daily API Calls | Org-dependent |

### Field Limits

- **String**: 255 characters (standard), 131,072 (long text)
- **Number**: 18 digits
- **Currency**: 18 digits with 2 decimal places

### Best Practice: Monitor API Usage

```python
# Check API limits (requires additional API call)
# Available via Salesforce Limits API
```

---

## Related Documentation

- **[Bulk API v2](BULK_API_V2.md)** - High-volume operations
- **[Field Mapping](FIELD_MAPPING.md)** - Data transformation
- **[Authentication](AUTHENTICATION.md)** - Setup client

---

## External Resources

- [Salesforce REST API](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/)
- [SOQL Reference](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/)
- [Composite API](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_composite.htm)
