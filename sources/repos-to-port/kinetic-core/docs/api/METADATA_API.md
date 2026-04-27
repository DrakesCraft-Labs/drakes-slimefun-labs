# Metadata API

The Metadata API allows you to manage Salesforce customization (fields, objects, etc.) programmatically. `kinetic-core` provides native support for these operations, enabling **Configuration as Code** workflows.

## Overview

The `MetadataClient` is accessible via the `client.metadata` property on the main `SalesforceClient`.

```python
from kinetic_core import SalesforceClient

client = SalesforceClient(session)

# Access Metadata API
meta = client.metadata
```

## Supported Operations

| Operation | Description |
|-----------|-------------|
| `describe_metadata()` | Lists all available metadata types in the org. |
| `retrieve()` | Downloads metadata components to a local directory. |
| `deploy()` | Deploys local metadata to the Salesforce org. |
| `deploy_field()` | Helper to create/update a single `CustomField`. |
| `deploy_object()` | Helper to create/update a `CustomObject` with its fields. |
| `compare()` | Compares local metadata with the org state. |

## Data Models

### CustomField

Represents a field on a Salesforce object.

```python
from kinetic_core.metadata import CustomField

field = CustomField(
    sobject="Account",
    name="Status__c",
    label="Account Status",
    type="Picklist",
    description="Current status of the account",
    required=False,
    options=[
        {"fullName": "Active", "default": True},
        {"fullName": "Inactive", "default": False},
        {"fullName": "Pending", "default": False}
    ]
)
```

**Parameters:**

- `sobject` (str): API Name of the object (e.g., `Account`, `MyObject__c`).
- `name` (str): API Name of the field (must end in `__c`).
- `type` (str): Field type (e.g., `Text`, `Number`, `Picklist`, `Checkbox`).
- `label` (str): User-facing label.
- `length` (int, optional): Length for text fields.
- `precision` (int, optional): Total digits for number fields.
- `scale` (int, optional): Decimal places for number fields.
- `required` (bool, optional): If the field is mandatory.
- `options` (list, optional): List of dicts for Picklist values.

### CustomObject

Represents a custom object definition.

```python
from kinetic_core.metadata import CustomObject, CustomField

# Create fields first
name_field = CustomField(sobject="MyObject__c", name="Name", type="Text", label="Name")
status_field = CustomField(sobject="MyObject__c", name="Status__c", type="Text", label="Status")

# Create object
obj = CustomObject(
    name="MyObject__c",
    label="My Object",
    label_plural="My Objects",
    description="A custom object for tracking things",
    fields=[status_field]  # Standard name field is handled automatically by Salesforce
)
```

## Examples

### 1. Deploy a New Field

```python
field = CustomField(
    sobject="Contact",
    name="Loyalty_Score__c",
    label="Loyalty Score",
    type="Number",
    precision=18,
    scale=0
)

result = client.metadata.deploy_field(field)
print(f"Success: {result['success']}")
```

### 2. Deploy a New Custom Object

```python
# Define object and fields
obj = CustomObject(
    name="Vehicle__c",
    label="Vehicle",
    label_plural="Vehicles",
    deployment_status="Deployed",
    sharing_model="ReadWrite"
)

# Add fields
obj.add_field(CustomField(
    sobject="Vehicle__c",
    name="Vin_Number__c",
    label="VIN Number",
    type="Text",
    length=20,
    unique=True
))

# Deploy
result = client.metadata.deploy_object(obj)
```

### 3. Retrieve Metadata

Retrieve existing metadata from Salesforce for backup or analysis.

```python
result = client.metadata.retrieve(
    component_types=["CustomObject", "CustomField"],
    output_dir="./salesforce_backup"
)

print(f"Retrieved {result.file_count} files to {result.output_path}")
```

### 4. Create from Template

Use built-in templates to create standard business objects quickly.

```python
# Create a standard SaaS "Subscription" object
subscription = client.metadata.create_from_template(
    template_name="subscription",
    object_name="SaaS_Subscription__c",
    label="Subscription"
)

client.metadata.deploy_object(subscription)
```

### 5. Compare Metadata

Check the difference between your local implementation and the org.

```python
diff_report = client.metadata.compare(
    local_path="./salesforce_schema",
    component_types=["CustomObject"]
)

for change in diff_report.changes:
    print(f"{change.type}: {change.component_name} ({change.action})")
```
