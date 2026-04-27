# Quick Start Guide

Get up and running with Salesforce Toolkit in 5 minutes!

## Table of Contents

1. [Installation](#installation)
2. [Setup](#setup)
3. [Authentication](#authentication)
4. [Basic Operations](#basic-operations)
5. [Data Sync](#data-sync)
6. [CLI Usage](#cli-usage)
7. [Next Steps](#next-steps)

---

## Installation

```bash
pip install salesforce-toolkit
```

Or install from source:

```bash
git clone https://github.com/yourusername/salesforce-toolkit.git
cd salesforce-toolkit
pip install -e .
```

---

## Setup

### 1. Create a Connected App in Salesforce

1. Go to **Setup** → **App Manager** → **New Connected App**
2. Fill in basic information:
   - Connected App Name: `My Integration`
   - API Name: `My_Integration`
   - Contact Email: your@email.com
3. Enable OAuth Settings:
   - Callback URL: `https://localhost`
   - Selected OAuth Scopes: `Full access (full)`
4. Enable **Use digital signatures** and upload your certificate
5. **Save** and note your **Consumer Key**

### 2. Generate RSA Key Pair (for JWT)

```bash
# Generate private key
openssl genrsa -out server.key 2048

# Generate certificate
openssl req -new -x509 -key server.key -out server.crt -days 365

# Upload server.crt to Salesforce Connected App
```

### 3. Create .env File

Create a `.env` file in your project root:

```bash
SF_CLIENT_ID=3MVG9...YOUR_CONSUMER_KEY
SF_USERNAME=user@example.com.sandbox
SF_PRIVATE_KEY_PATH=/path/to/server.key
SF_LOGIN_URL=https://test.salesforce.com
```

---

## Authentication

```python
from salesforce_toolkit import JWTAuthenticator

# Authenticate using .env configuration
auth = JWTAuthenticator.from_env()
session = auth.authenticate()

print(f"✓ Connected to: {session.instance_url}")
```

---

## Basic Operations

### Create a Record

```python
from salesforce_toolkit import JWTAuthenticator, SalesforceClient

# Setup
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Create Account
account_id = client.create("Account", {
    "Name": "ACME Corporation",
    "Industry": "Technology",
    "Phone": "555-0100"
})

print(f"Created Account: {account_id}")
```

### Query Records

```python
# Query Accounts
accounts = client.query(
    "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology' LIMIT 10"
)

for account in accounts:
    print(f"{account['Name']} ({account['Id']})")
```

### Update a Record

```python
client.update("Account", account_id, {
    "Phone": "555-9999",
    "Website": "https://acme.com"
})

print(f"Updated Account: {account_id}")
```

### Delete a Record

```python
client.delete("Account", account_id)
print(f"Deleted Account: {account_id}")
```

---

## Data Sync

Sync data from any source to Salesforce:

```python
from salesforce_toolkit import FieldMapper, SyncPipeline, SyncMode

# Define field mapping
mapper = FieldMapper({
    "customer_name": "Name",
    "customer_email": "Email",
    "customer_phone": "Phone",
    "industry_code": ("Industry", lambda x: x.title())  # Transform to title case
})

# Create pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    batch_size=200
)

# Your source data (from database, CSV, API, etc.)
source_data = [
    {
        "customer_name": "Tech Innovations Inc",
        "customer_email": "contact@techinnovations.com",
        "customer_phone": "555-1001",
        "industry_code": "technology"
    },
    {
        "customer_name": "Global Manufacturing Co",
        "customer_email": "info@globalmanufacturing.com",
        "customer_phone": "555-1002",
        "industry_code": "manufacturing"
    }
]

# Run sync
result = pipeline.sync(source_data)

print(f"Synced {result.success_count}/{result.total_records} records")
print(f"Success Rate: {result.success_rate:.1f}%")
```

---

## CLI Usage

The toolkit includes a powerful CLI for common operations:

### Test Authentication

```bash
sf-toolkit auth --method jwt
```

### Query Salesforce

```bash
sf-toolkit query "SELECT Id, Name FROM Account LIMIT 10"
```

### Create a Record

```bash
sf-toolkit create Account --data '{"Name": "ACME Corp", "Industry": "Technology"}'
```

### Run a Sync Pipeline

Create a `sync_config.yaml` file:

```yaml
source:
  type: json
  path: data/accounts.json

pipeline:
  sobject: Account
  mode: insert
  batch_size: 200
  mapping:
    customer_name: Name
    customer_email: Email
    industry_code:
      target: Industry
      transform: uppercase
```

Run the sync:

```bash
sf-toolkit sync --config sync_config.yaml
```

### Describe a Salesforce Object

```bash
sf-toolkit describe Account --fields
```

---

## Next Steps

### Learn More

- **[README.md](../README.md)** - Complete documentation
- **[Examples](../examples/)** - Code examples for all features
- **[API Reference](API_REFERENCE.md)** - Detailed API documentation
- **[Configuration Guide](CONFIGURATION.md)** - Advanced configuration options

### Advanced Topics

- **Field Mapping** - Learn about transformations and nested fields
- **Error Handling** - Implement robust error handling
- **Batch Operations** - Optimize performance with batching
- **Custom Pipelines** - Build custom ETL pipelines
- **Logging** - Configure logging for production

### Get Help

- **GitHub Issues**: https://github.com/yourusername/salesforce-toolkit/issues
- **Documentation**: https://github.com/yourusername/salesforce-toolkit#readme
- **Examples**: [examples/](../examples/)

---

## Common Issues

### Authentication Failed

**Problem**: `Authentication failed: invalid_grant`

**Solution**:
- Verify your Consumer Key is correct
- Ensure the username is pre-authorized in the Connected App
- Check that your certificate matches the private key
- For sandboxes, use `https://test.salesforce.com` as login URL

### Private Key Not Found

**Problem**: `FileNotFoundError: Private key file not found`

**Solution**:
- Use absolute path in `SF_PRIVATE_KEY_PATH`
- Verify the file exists: `ls -la /path/to/server.key`
- Check file permissions

### Field Not Found

**Problem**: `Invalid field: CustomField__c`

**Solution**:
- Verify the field exists in Salesforce
- Check field API name (it should end with `__c` for custom fields)
- Ensure you have read/write permissions on the field

---

**Ready to build? Start with the [examples](../examples/) or dive into the [full documentation](../README.md)!**
