# Field Mapping - Complete Reference

Transform data between different schemas with flexible field mapping and value transformations.

## Overview

The `FieldMapper` provides powerful data transformation capabilities for converting data between external systems and Salesforce schemas.

**Key Features**:
- ✅ Simple field renaming
- ✅ Value transformations with custom functions
- ✅ Default values for missing fields
- ✅ Nested field access (dot notation)
- ✅ Conditional mapping logic
- ✅ Batch processing support
- ✅ Built-in transformation functions
- ✅ YAML configuration support

---

## Quick Reference

| Feature | Example |
|---------|---------|
| Simple Rename | `{"source": "Target"}` |
| With Transform | `{"source": ("Target", lambda x: x.upper())}` |
| With Default | `{"source": ("Target", None, "default")}` |
| Nested Access | `{"user.email": "Email"}` |
| Conditional | `ConditionalFieldMapper` |

---

## FieldMapper Class

### Initialization

**Signature**:
```python
def __init__(self, mapping: Dict[str, Any])
```

**Parameters**:
- `mapping` (dict): Mapping configuration with multiple formats

**Mapping Formats**:

1. **Simple String Mapping**:
```python
mapping = {
    "source_field": "target_field"
}
```

2. **With Transformation Function**:
```python
mapping = {
    "source_field": ("target_field", transform_function)
}
```

3. **With Default Value**:
```python
mapping = {
    "source_field": ("target_field", None, "default_value")
}
```

4. **Full Format** (transformation + default):
```python
mapping = {
    "source_field": ("target_field", transform_function, "default_value")
}
```

**Example**:
```python
from kinetic_core import FieldMapper

mapper = FieldMapper({
    # Simple rename
    "customer_name": "Name",

    # With transformation
    "email": ("Email", lambda x: x.lower()),

    # With default value
    "status": ("Status__c", None, "Active"),

    # Full format
    "created_at": (
        "CreatedDate",
        lambda x: x.strftime("%Y-%m-%d") if x else None,
        "2025-01-02"
    )
})
```

---

## Core Methods

### transform()

Transform a single record from source to target schema.

**Signature**:
```python
def transform(
    source: Dict[str, Any],
    skip_none: bool = True,
    strict: bool = False
) -> Dict[str, Any]
```

**Parameters**:
- `source` (dict): Source data dictionary
- `skip_none` (bool): Skip fields with None values (default: True)
- `strict` (bool): Raise error if source field missing (default: False)

**Returns**: Transformed dictionary in target schema

**Raises**:
- `KeyError`: If strict=True and source field is missing
- `Exception`: If transformation function fails

**Example**:
```python
source = {
    "first_name": "John",
    "last_name": "Doe",
    "email": "JOHN.DOE@EXAMPLE.COM"
}

mapping = {
    "first_name": "FirstName",
    "last_name": "LastName",
    "email": ("Email", lambda x: x.lower())
}

mapper = FieldMapper(mapping)
result = mapper.transform(source)

print(result)
# {
#   "FirstName": "John",
#   "LastName": "Doe",
#   "Email": "john.doe@example.com"
# }
```

**Strict Mode**:
```python
# Raises KeyError if field missing
result = mapper.transform(source, strict=True)
```

**Include None Values**:
```python
# Include fields even if None
result = mapper.transform(source, skip_none=False)
```

---

### transform_batch()

Transform a list of records.

**Signature**:
```python
def transform_batch(
    source_list: List[Dict[str, Any]],
    skip_none: bool = True,
    strict: bool = False
) -> List[Dict[str, Any]]
```

**Parameters**: Same as `transform()` but accepts a list

**Returns**: List of transformed dictionaries

**Example**:
```python
source_list = [
    {"name": "Alice", "age": 30, "city": "NYC"},
    {"name": "Bob", "age": 25, "city": "LA"},
    {"name": "Charlie", "age": 35, "city": "SF"}
]

mapping = {
    "name": "Name",
    "age": "Age__c",
    "city": "BillingCity"
}

mapper = FieldMapper(mapping)
results = mapper.transform_batch(source_list)

for record in results:
    print(record)
# {"Name": "Alice", "Age__c": 30, "BillingCity": "NYC"}
# {"Name": "Bob", "Age__c": 25, "BillingCity": "LA"}
# {"Name": "Charlie", "Age__c": 35, "BillingCity": "SF"}
```

---

## Advanced Features

### Nested Field Access

Access nested fields using dot notation.

**Example**:
```python
source = {
    "user": {
        "profile": {
            "email": "john@example.com",
            "name": "John Doe"
        },
        "address": {
            "city": "New York",
            "zip": "10001"
        }
    }
}

mapping = {
    "user.profile.email": "Email",
    "user.profile.name": "Name",
    "user.address.city": "BillingCity",
    "user.address.zip": "BillingPostalCode"
}

mapper = FieldMapper(mapping)
result = mapper.transform(source)

print(result)
# {
#   "Email": "john@example.com",
#   "Name": "John Doe",
#   "BillingCity": "New York",
#   "BillingPostalCode": "10001"
# }
```

---

### Transformation Functions

Custom transformation functions for value processing.

**Common Transformations**:

```python
# String transformations
mapping = {
    "email": ("Email", lambda x: x.lower()),
    "name": ("Name", lambda x: x.strip().title()),
    "phone": ("Phone", lambda x: x.replace("-", ""))
}

# Type conversions
mapping = {
    "revenue": ("AnnualRevenue", lambda x: float(x)),
    "employee_count": ("NumberOfEmployees", lambda x: int(x)),
    "is_active": ("IsActive__c", lambda x: bool(x))
}

# Date/time formatting
from datetime import datetime

mapping = {
    "created_at": (
        "CreatedDate",
        lambda x: x.strftime("%Y-%m-%d") if isinstance(x, datetime) else x
    ),
    "updated_at": (
        "LastModifiedDate",
        lambda x: x.isoformat() if isinstance(x, datetime) else x
    )
}

# Complex transformations
def format_address(addr):
    """Format address object to single string"""
    if not addr:
        return None
    return f"{addr['street']}, {addr['city']}, {addr['state']} {addr['zip']}"

mapping = {
    "address": ("BillingAddress", format_address)
}
```

**Safe Transformations**:
```python
# Handle None values gracefully
def safe_upper(value):
    """Convert to uppercase, handle None"""
    return value.upper() if value is not None else None

mapping = {
    "industry": ("Industry", safe_upper)
}
```

---

### Default Values

Provide fallback values for missing fields.

**Example**:
```python
mapping = {
    # Static default
    "status": ("Status__c", None, "Active"),

    # Dynamic default
    "created_date": (
        "CreatedDate",
        None,
        datetime.now().strftime("%Y-%m-%d")
    ),

    # Transformation with default
    "revenue": (
        "AnnualRevenue",
        lambda x: float(x) if x else 0,
        0  # Default if missing
    )
}

# Source without 'status' field
source = {"name": "ACME Corp"}

mapper = FieldMapper(mapping)
result = mapper.transform(source)

print(result)
# {
#   "Status__c": "Active",  # Used default
#   "CreatedDate": "2025-01-02"
# }
```

---

## Built-in Transformations

Pre-defined transformation functions accessible by name.

**Available Transforms**:

| Name | Function | Example |
|------|----------|---------|
| `lowercase` | Convert to lowercase | "HELLO" → "hello" |
| `uppercase` | Convert to uppercase | "hello" → "HELLO" |
| `strip` | Remove whitespace | " text " → "text" |
| `int` | Convert to integer | "123" → 123 |
| `float` | Convert to float | "12.5" → 12.5 |
| `bool` | Convert to boolean | "true" → True |
| `date_iso` | Format date as ISO | datetime → "2025-01-02" |
| `datetime_iso` | Format datetime as ISO | datetime → "2025-01-02T10:30:00" |

**Usage with YAML**:
```yaml
# config.yaml
mapping:
  email:
    target: Email
    transform: lowercase

  name:
    target: Name
    transform: uppercase

  revenue:
    target: AnnualRevenue
    transform: float
    default: 0
```

```python
mapper = FieldMapper.from_yaml("config.yaml")
```

---

## ConditionalFieldMapper

Extended mapper with conditional field logic.

**Signature**:
```python
class ConditionalFieldMapper(FieldMapper):
    def __init__(
        self,
        mapping: Dict[str, Any],
        conditions: Dict[str, Callable] = None
    )
```

**Parameters**:
- `mapping` (dict): Standard field mapping
- `conditions` (dict): Conditional field functions

**Example**:
```python
from kinetic_core.mapping import ConditionalFieldMapper

mapper = ConditionalFieldMapper(
    mapping={
        "name": "Name",
        "revenue": "AnnualRevenue"
    },
    conditions={
        # Add 'Industry' based on revenue
        "Industry": lambda data: (
            "Enterprise" if data.get("revenue", 0) > 10000000
            else "SMB"
        ),

        # Add 'Rating' based on multiple fields
        "Rating": lambda data: (
            "Hot" if data.get("revenue", 0) > 5000000 and data.get("employees", 0) > 100
            else "Warm" if data.get("revenue", 0) > 1000000
            else "Cold"
        ),

        # Add 'Type' based on name
        "Type": lambda data: (
            "Partner" if "partner" in data.get("name", "").lower()
            else "Customer"
        )
    }
)

source = {
    "name": "ACME Partner Corp",
    "revenue": 15000000,
    "employees": 250
}

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Partner Corp",
#   "AnnualRevenue": 15000000,
#   "Industry": "Enterprise",    # From condition
#   "Rating": "Hot",              # From condition
#   "Type": "Partner"             # From condition
# }
```

---

## YAML Configuration

Load mapping configuration from YAML files.

### from_yaml()

**Signature**:
```python
@classmethod
def from_yaml(cls, yaml_path: str) -> FieldMapper
```

**Parameters**:
- `yaml_path` (str): Path to YAML configuration file

**Returns**: Configured FieldMapper instance

**YAML Format**:

```yaml
# mapping_config.yaml
mapping:
  # Simple mapping
  customer_name: Name
  customer_email: Email

  # With transformation
  industry_type:
    target: Industry
    transform: uppercase

  # With default
  status:
    target: Status__c
    default: Active

  # With both
  annual_revenue:
    target: AnnualRevenue
    transform: float
    default: 0

  # Nested fields
  address.city:
    target: BillingCity

  address.postal_code:
    target: BillingPostalCode
```

**Usage**:
```python
from kinetic_core import FieldMapper

# Load from YAML
mapper = FieldMapper.from_yaml("mapping_config.yaml")

# Use as normal
source = {
    "customer_name": "ACME Corp",
    "customer_email": "info@acme.com",
    "industry_type": "technology",
    "annual_revenue": "5000000",
    "address": {
        "city": "San Francisco",
        "postal_code": "94105"
    }
}

result = mapper.transform(source)

print(result)
# {
#   "Name": "ACME Corp",
#   "Email": "info@acme.com",
#   "Industry": "TECHNOLOGY",
#   "Status__c": "Active",
#   "AnnualRevenue": 5000000.0,
#   "BillingCity": "San Francisco",
#   "BillingPostalCode": "94105"
# }
```

---

## Real-World Examples

### Example 1: CSV to Salesforce

```python
import csv
from kinetic_core import FieldMapper, SalesforceClient

# Define mapping
mapper = FieldMapper({
    "Company Name": "Name",
    "Email Address": ("Email", lambda x: x.lower()),
    "Phone Number": ("Phone", lambda x: x.replace("-", "")),
    "Industry Type": "Industry",
    "Annual Revenue": ("AnnualRevenue", lambda x: float(x.replace("$", "").replace(",", ""))),
    "Employee Count": ("NumberOfEmployees", int),
    "Status": ("Status__c", None, "Active")
})

# Read CSV
with open("companies.csv", "r") as f:
    reader = csv.DictReader(f)
    csv_records = list(reader)

# Transform
sf_records = mapper.transform_batch(csv_records)

# Insert to Salesforce
client = SalesforceClient(session)
result = client.bulk.insert("Account", sf_records)

print(f"Imported {result.success_count} records")
```

### Example 2: External API to Salesforce

```python
import requests
from kinetic_core import FieldMapper, SalesforceClient

# Fetch from external API
response = requests.get("https://api.example.com/customers")
customers = response.json()

# Define mapping for nested API response
mapper = FieldMapper({
    "company.name": "Name",
    "company.industry": ("Industry", lambda x: x.upper()),
    "contact.email": "Email",
    "contact.phone": "Phone",
    "metadata.created_at": (
        "CreatedDate",
        lambda x: datetime.fromisoformat(x).strftime("%Y-%m-%d")
    ),
    "billing.address.city": "BillingCity",
    "billing.address.state": "BillingState",
    "billing.address.zip": "BillingPostalCode"
})

# Transform
sf_records = mapper.transform_batch(customers)

# Upsert to Salesforce
client = SalesforceClient(session)
result = client.bulk.upsert("Account", sf_records, "Email")

print(f"Synced {result.success_count} accounts")
```

### Example 3: Conditional Business Logic

```python
from kinetic_core.mapping import ConditionalFieldMapper

# Complex business rules
mapper = ConditionalFieldMapper(
    mapping={
        "name": "Name",
        "email": ("Email", lambda x: x.lower()),
        "revenue": "AnnualRevenue",
        "employees": "NumberOfEmployees"
    },
    conditions={
        # Tier based on revenue
        "Tier__c": lambda data: (
            "Platinum" if data.get("revenue", 0) > 50000000
            else "Gold" if data.get("revenue", 0) > 10000000
            else "Silver" if data.get("revenue", 0) > 1000000
            else "Bronze"
        ),

        # Priority scoring
        "Priority__c": lambda data: (
            "High" if (
                data.get("revenue", 0) > 10000000 or
                data.get("employees", 0) > 500
            ) else "Medium" if data.get("revenue", 0) > 1000000
            else "Low"
        ),

        # Account source
        "AccountSource": lambda data: (
            "Partner" if "partner" in data.get("name", "").lower()
            else "Direct"
        ),

        # Region based on phone
        "Region__c": lambda data: (
            "North America" if data.get("phone", "").startswith("+1")
            else "Europe" if data.get("phone", "").startswith("+44")
            else "Other"
        )
    }
)

source = {
    "name": "Global Partner Solutions",
    "email": "INFO@GPS.COM",
    "revenue": 25000000,
    "employees": 750,
    "phone": "+1-555-0100"
}

result = mapper.transform(source)

print(result)
# {
#   "Name": "Global Partner Solutions",
#   "Email": "info@gps.com",
#   "AnnualRevenue": 25000000,
#   "NumberOfEmployees": 750,
#   "Tier__c": "Gold",
#   "Priority__c": "High",
#   "AccountSource": "Partner",
#   "Region__c": "North America"
# }
```

### Example 4: Data Validation

```python
from kinetic_core import FieldMapper

# Validation functions
def validate_email(email):
    """Validate and normalize email"""
    if not email or "@" not in email:
        raise ValueError(f"Invalid email: {email}")
    return email.lower().strip()

def validate_phone(phone):
    """Validate and format phone"""
    digits = "".join(filter(str.isdigit, phone))
    if len(digits) < 10:
        raise ValueError(f"Invalid phone: {phone}")
    return f"+1-{digits[-10:-7]}-{digits[-7:-4]}-{digits[-4:]}"

def validate_revenue(revenue):
    """Validate revenue is positive"""
    value = float(revenue)
    if value < 0:
        raise ValueError(f"Revenue cannot be negative: {value}")
    return value

# Mapping with validation
mapper = FieldMapper({
    "name": "Name",
    "email": ("Email", validate_email),
    "phone": ("Phone", validate_phone),
    "revenue": ("AnnualRevenue", validate_revenue)
})

# Transform with strict validation
try:
    result = mapper.transform(source, strict=True)
except ValueError as e:
    print(f"Validation failed: {e}")
```

---

## Integration with Pipelines

Use FieldMapper with SyncPipeline for complete ETL workflows.

**Example**:
```python
from kinetic_core import FieldMapper, SyncPipeline, SyncMode

# Define mapping
mapper = FieldMapper({
    "external_id": "External_Id__c",
    "company_name": "Name",
    "industry": ("Industry", lambda x: x.upper()),
    "revenue": ("AnnualRevenue", float)
})

# Transform data
external_data = fetch_from_external_api()
salesforce_data = mapper.transform_batch(external_data)

# Sync to Salesforce
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mode=SyncMode.UPSERT,
    external_id_field="External_Id__c"
)

result = pipeline.sync(salesforce_data)
print(f"Synced {result.success_count} records")
```

---

## Best Practices

### 1. Use Type-Safe Transformations

```python
# Good: Handle None values
def safe_float(value):
    try:
        return float(value) if value is not None else None
    except (ValueError, TypeError):
        return None

mapping = {"revenue": ("AnnualRevenue", safe_float)}

# Bad: May raise exceptions
mapping = {"revenue": ("AnnualRevenue", float)}
```

### 2. Validate Data

```python
# Good: Validate during transformation
def validate_email(email):
    if not email or "@" not in email:
        raise ValueError(f"Invalid email: {email}")
    return email.lower()

# Bad: No validation
mapping = {"email": ("Email", lambda x: x.lower())}
```

### 3. Use Default Values

```python
# Good: Provide defaults
mapping = {
    "status": ("Status__c", None, "Active"),
    "created_date": ("CreatedDate", None, datetime.now().strftime("%Y-%m-%d"))
}

# Bad: Missing fields cause errors in strict mode
mapping = {
    "status": "Status__c",
    "created_date": "CreatedDate"
}
```

### 4. Keep Transformations Simple

```python
# Good: Simple, focused functions
def format_phone(phone):
    """Remove non-digits and format"""
    return "".join(filter(str.isdigit, phone))

# Bad: Complex logic in lambda
mapping = {
    "phone": (
        "Phone",
        lambda x: f"+1-{x[0:3]}-{x[3:6]}-{x[6:]}" if len(x.replace("-", "")) >= 10 else x
    )
}
```

### 5. Use YAML for Configuration

```python
# Good: External configuration
mapper = FieldMapper.from_yaml("config/account_mapping.yaml")

# Bad: Hardcoded mapping
mapper = FieldMapper({...})  # Long mapping dict in code
```

---

## Error Handling

### Handle Missing Fields

```python
# Graceful handling
try:
    result = mapper.transform(source, strict=False)
except Exception as e:
    logger.error(f"Transform failed: {e}")

# Strict mode (raises errors)
try:
    result = mapper.transform(source, strict=True)
except KeyError as e:
    logger.error(f"Missing required field: {e}")
```

### Handle Transformation Failures

```python
# Batch with error logging
results = []
for i, source in enumerate(source_list):
    try:
        result = mapper.transform(source)
        results.append(result)
    except Exception as e:
        logger.error(f"Record #{i} failed: {e}")
        # Continue processing other records
        continue
```

---

## Performance Considerations

### Batch Processing

```python
# Good: Use transform_batch for large datasets
results = mapper.transform_batch(large_dataset)

# Bad: Loop with transform (slower)
results = [mapper.transform(rec) for rec in large_dataset]
```

### Caching Transformations

```python
from functools import lru_cache

# Cache expensive lookups
@lru_cache(maxsize=1000)
def lookup_industry_code(name):
    """Look up industry code (cached)"""
    return industry_lookup.get(name, "Other")

mapping = {
    "industry_name": ("Industry", lookup_industry_code)
}
```

---

## Related Documentation

- **[CRUD Operations](CRUD_OPERATIONS.md)** - Data operations
- **[Pipelines](PIPELINES.md)** - ETL workflows
- **[Bulk API v2](BULK_API_V2.md)** - High-volume processing

---

## External Resources

- [Python typing module](https://docs.python.org/3/library/typing.html)
- [Salesforce Field Types](https://help.salesforce.com/s/articleView?id=sf.custom_field_types.htm)
- [Data Transformation Patterns](https://en.wikipedia.org/wiki/Extract,_transform,_load)
