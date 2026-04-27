# Kinetic Core

![Kinetic Core Header](assets/kinetic-core.jpg)

> The core engine for Salesforce AI agents. A comprehensive, production-ready Python library for Salesforce integration.

> **Part of the [KineticMCP](https://kineticmcp.com) ecosystem** - AI-powered Salesforce integration tools by [Antonio Trento](https://antoniotrento.net)

> [!IMPORTANT]
> **Legal Disclaimer**: This project is an independent open-source library and is not affiliated with, sponsored by, or endorsed by Salesforce, Inc. "Salesforce" is a trademark of Salesforce, Inc.

> [!WARNING]
> **Deprecation Notice**: The package `salesforce-toolkit` is deprecated. Please use `kinetic-core` instead.

[![PyPI version](https://badge.fury.io/py/kinetic-core.svg)](https://badge.fury.io/py/kinetic-core)
[![Python Version](https://img.shields.io/badge/python-3.8%2B-blue.svg)](https://pypi.org/project/kinetic-core/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Code Style](https://img.shields.io/badge/code%20style-black-000000.svg)](https://github.com/psf/black)

---

## 🚀 Features

### Core Capabilities
- **🔐 Multiple Authentication Methods**
  - JWT Bearer Flow (recommended for production)
  - OAuth 2.0 Password Flow
  - Environment-based configuration

- **📊 Complete CRUD Operations**
  - Works with **any** Salesforce object (standard or custom)
  - Create, Read, Update, Delete, Upsert
  - Bulk operations via Composite API
  - Query with automatic pagination

- **⚡ Bulk API v2 Support** _(NEW in v2.0.0)_
  - Process **millions of records** efficiently
  - Full support for all Bulk API v2 operations
  - Smart exponential backoff polling
  - Progress tracking with callbacks
  - Comprehensive per-record error reporting
  - Async job processing

- **🗺️ Flexible Field Mapping**
  - Simple field renaming
  - Value transformations with custom functions
  - Default values
  - Nested field access (dot notation)
  - Conditional mapping

- **🔄 ETL Pipeline Framework**
  - Configuration-driven sync pipelines
  - Multiple sync modes (INSERT, UPDATE, UPSERT, DELETE)
  - Batch processing
  - Progress tracking with callbacks
  - Comprehensive error handling

- **📝 Production-Ready Logging**
  - File and console output
  - Automatic log rotation
  - Colored console output
  - Contextual logging
  - Configurable log levels

- **🛠️ Command-Line Interface**
  - Query, create, update, delete from terminal
  - Run sync pipelines from YAML config
  - Describe Salesforce objects
  - Test authentication

---

## 📦 Installation

### From PyPI
```bash
pip install kinetic-core
```

### From Source
```bash
git clone https://github.com/yourusername/kinetic-core.git
cd kinetic-core
pip install -e .
```

### With Optional Dependencies
```bash
# Database support
pip install kinetic-core[database]

# Data manipulation (pandas, numpy)
pip install kinetic-core[data]

# Development tools
pip install kinetic-core[dev]
```

---

## 🎯 Quick Start

### 1. Setup Environment Variables

Create a `.env` file in your project root:

```bash
# JWT Authentication (Recommended)
SF_CLIENT_ID=3MVG9...
SF_USERNAME=user@example.com.sandbox
SF_PRIVATE_KEY_PATH=/path/to/server.key
SF_LOGIN_URL=https://test.salesforce.com

# Logging
# Logging
LOG_DIR=./logs
LOG_LEVEL=INFO

# Optional: Salesforce API Version
SF_API_VERSION=v62.0  # Defaults to v62.0 if not set
```

### 2. Basic Usage

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()

# Create client
client = SalesforceClient(session)

# Create a record
account_id = client.create("Account", {
    "Name": "ACME Corporation",
    "Industry": "Technology"
})

# Query records
accounts = client.query("SELECT Id, Name FROM Account LIMIT 10")

# Update a record
client.update("Account", account_id, {"Phone": "555-1234"})

# Delete a record
client.delete("Account", account_id)
```

### 3. Data Sync Pipeline

```python
from kinetic_core import (
    JWTAuthenticator,
    SalesforceClient,
    FieldMapper,
    SyncPipeline,
    SyncMode
)

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Define field mapping
mapper = FieldMapper({
    "customer_name": "Name",
    "customer_email": "Email",
    "industry_code": ("Industry", lambda x: x.title())  # Transform
})

# Create pipeline
pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    batch_size=200
)

# Sync data
source_data = [
    {"customer_name": "ACME", "customer_email": "info@acme.com"},
    {"customer_name": "Globex", "customer_email": "contact@globex.com"}
]

result = pipeline.sync(source_data)
print(f"Synced {result.success_count}/{result.total_records} records")
```

### 4. Bulk API v2 (High-Volume Operations)

**✨ NEW in v2.0.0** - Process millions of records efficiently:

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Bulk insert 10,000 records
records = [
    {"Name": f"Account {i}", "Industry": "Technology"}
    for i in range(10000)
]

result = client.bulk.insert("Account", records)
print(f"✓ Success: {result.success_count}")
print(f"✗ Failed: {result.failed_count}")

# Bulk query for large exports
query = "SELECT Id, Name FROM Account WHERE CreatedDate = THIS_YEAR"
result = client.bulk.query(query)
print(f"Retrieved {result.record_count} records")

# Bulk update with external ID
updates = [
    {"External_Key__c": "EXT001", "Name": "Updated Name 1"},
    {"External_Key__c": "EXT002", "Name": "Updated Name 2"}
]
result = client.bulk.upsert("Account", updates, "External_Key__c")
```

**Key Features**:
- ⚡ Up to **150 million records** per job
- 🔄 Async processing with progress tracking
- 📊 Detailed per-record error reporting
- 🎯 Smart retry and exponential backoff

**[📖 Complete Bulk API Documentation →](docs/api/BULK_API_V2.md)**

---

### 5. Command-Line Interface

```bash
# Test authentication
sf-toolkit auth --method jwt

# Query Salesforce
sf-toolkit query "SELECT Id, Name FROM Account LIMIT 10"

# Create a record
sf-toolkit create Account --data '{"Name": "ACME Corp"}'

# Run a sync pipeline
sf-toolkit sync --config sync_config.yaml

# Describe an object
sf-toolkit describe Account --fields
```

---

## 📚 Documentation

### 📖 Complete Guides

#### Bulk API v2 (NEW)
- **[Complete API Reference](api/BULK_API_V2.md)** - Full API documentation
- **[Quick Start Guide](guides/BULK_QUICKSTART.md)** - Get started in 5 minutes
- **[Practical Examples](examples/BULK_EXAMPLES.md)** - Real-world use cases

#### Core Documentation
- **[User Guide](USER_GUIDE.md)** - Complete usage documentation
- **[Quick Start](QUICK_START.md)** - Getting started guide
- **[Salesforce Setup](SALESFORCE_SETUP_GUIDE.md)** - Configure Salesforce
- **[Testing Guide](TESTING_GUIDE.md)** - Test your integration
- **[Docker Guide](DOCKER_GUIDE.md)** - Containerized deployment

---

### Authentication

#### JWT Bearer Flow (Recommended)

```python
from kinetic_core import JWTAuthenticator

# From environment variables
auth = JWTAuthenticator.from_env()

# Or manual configuration
auth = JWTAuthenticator(
    client_id="3MVG9...",
    username="user@example.com",
    private_key_path="/path/to/server.key",
    login_url="https://test.salesforce.com"
)

session = auth.authenticate()
```

#### OAuth Password Flow

```python
from kinetic_core import OAuthAuthenticator

# From environment variables
auth = OAuthAuthenticator.from_env()

# Or manual configuration
auth = OAuthAuthenticator(
    client_id="3MVG9...",
    client_secret="1234567890ABCDEF",
    username="user@example.com",
    password="your_password",
    security_token="ABC123",
    login_url="https://login.salesforce.com"
)

session = auth.authenticate()
```

### CRUD Operations

#### Create Records

```python
# Single record
account_id = client.create("Account", {
    "Name": "ACME Corp",
    "Industry": "Technology"
})

# Batch create (up to 200 records)
results = client.create_batch("Contact", [
    {"FirstName": "John", "LastName": "Doe"},
    {"FirstName": "Jane", "LastName": "Smith"}
])
```

#### Query Records

```python
# SOQL query with automatic pagination
accounts = client.query(
    "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology'"
)

# Query first result
account = client.query_one(
    "SELECT Id, Name FROM Account WHERE Name = 'ACME Corp'"
)

# Get by ID
account = client.get("Account", "001XXXXXXXXXXXX")

# Count records
total = client.count("Account")
tech_count = client.count("Account", "Industry = 'Technology'")
```

#### Update Records

```python
# Update by ID
client.update("Account", "001XXXXXXXXXXXX", {
    "Phone": "555-9999",
    "Industry": "Manufacturing"
})

# Upsert (requires External ID field)
account_id = client.upsert(
    "Account",
    "External_Key__c",
    "EXT-12345",
    {"Name": "ACME Corp", "Industry": "Tech"}
)
```

#### Delete Records

```python
client.delete("Account", "001XXXXXXXXXXXX")
```

### Field Mapping

#### Basic Mapping

```python
from kinetic_core import FieldMapper

mapper = FieldMapper({
    "first_name": "FirstName",
    "last_name": "LastName",
    "email": "Email"
})

source = {"first_name": "John", "last_name": "Doe", "email": "john@example.com"}
target = mapper.transform(source)
# Result: {"FirstName": "John", "LastName": "Doe", "Email": "john@example.com"}
```

#### Advanced Mapping with Transformations

```python
mapper = FieldMapper({
    # Simple rename
    "customer_name": "Name",

    # With transformation
    "email": ("Email", lambda x: x.lower()),

    # With default value
    "status": ("Status__c", None, "Active"),

    # With both transformation and default
    "created_at": (
        "CreatedDate",
        lambda x: x.strftime("%Y-%m-%d") if x else None,
        datetime.now().strftime("%Y-%m-%d")
    ),

    # Nested field access
    "address.city": "BillingCity",
    "address.state": "BillingState"
})
```

#### Built-in Transformations

```python
# Available via YAML configuration
transforms = [
    "lowercase",    # Convert to lowercase
    "uppercase",    # Convert to uppercase
    "strip",        # Strip whitespace
    "int",          # Convert to integer
    "float",        # Convert to float
    "bool",         # Convert to boolean
    "date_iso",     # Format date as YYYY-MM-DD
    "datetime_iso"  # Format datetime as ISO 8601
]
```

### Sync Pipeline

#### Basic Pipeline

```python
from kinetic_core import SyncPipeline, SyncMode

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    batch_size=200,
    stop_on_error=False
)

result = pipeline.sync(source_data)
```

#### Pipeline with Callbacks

```python
def on_record_success(record, salesforce_id):
    print(f"✓ Synced: {record['name']} -> {salesforce_id}")

def on_record_error(record, error):
    print(f"✗ Failed: {record['name']} - {error}")

def on_batch_complete(batch_num, total_batches, result):
    print(f"Batch {batch_num}/{total_batches} done")

pipeline = SyncPipeline(
    client=client,
    sobject="Account",
    mapper=mapper,
    mode=SyncMode.INSERT,
    callbacks={
        "on_record_success": on_record_success,
        "on_record_error": on_record_error,
        "on_batch_complete": on_batch_complete
    }
)
```

#### Pipeline from YAML Configuration

```yaml
# sync_config.yaml
source:
  type: json
  path: data/accounts.json

pipeline:
  sobject: Account
  mode: upsert
  external_id_field: External_Key__c
  batch_size: 200
  mapping:
    customer_name: Name
    customer_email: Email
    industry_code:
      target: Industry
      transform: uppercase
```

```python
import yaml
from kinetic_core import SyncPipeline

with open("sync_config.yaml") as f:
    config = yaml.safe_load(f)

pipeline = SyncPipeline.from_config(config["pipeline"], client)
result = pipeline.sync(source_data)
```

### Logging

#### Basic Logger Setup

```python
from kinetic_core.logging import setup_logger
import logging

logger = setup_logger(
    name="my_app",
    log_dir="./logs",
    log_level=logging.INFO,
    console_colors=True
)

logger.info("Application started")
logger.error("An error occurred", exc_info=True)
```

#### Contextual Logging

```python
from kinetic_core.logging import ContextLogger, setup_logger

base_logger = setup_logger("my_app")
context_logger = ContextLogger(base_logger, context={
    "transaction_id": "TX-12345",
    "user_id": "user@example.com"
})

context_logger.info("Processing record")
# Logs: "Processing record [transaction_id=TX-12345, user_id=user@example.com]"
```

### Utilities

```python
from kinetic_core.utils import (
    sanitize_soql,
    build_soql_query,
    validate_salesforce_id,
    format_datetime_for_sf,
    generate_external_id,
    batch_records
)

# Sanitize SOQL
safe_name = sanitize_soql("O'Brien & Associates")

# Build SOQL query
query = build_soql_query(
    sobject="Account",
    fields=["Id", "Name", "Industry"],
    where="Industry = 'Technology'",
    limit=100
)

# Validate Salesforce ID
if validate_salesforce_id("001XXXXXXXXXXXXXXX"):
    print("Valid ID")

# Format datetime
sf_datetime = format_datetime_for_sf(datetime.now())

# Generate external ID
ext_id = generate_external_id("CUST", timestamp=True)
# Returns: "CUST-20251205-103000-abc123"

# Batch records
batches = batch_records(records, batch_size=200)
```

---

## 🎨 Examples

The `examples/` directory contains comprehensive examples:

1. **[01_basic_authentication.py](examples/01_basic_authentication.py)** - Authentication methods
2. **[02_crud_operations.py](examples/02_crud_operations.py)** - CRUD operations
3. **[03_data_sync_pipeline.py](examples/03_data_sync_pipeline.py)** - Data synchronization

Run an example:
```bash
cd examples
python 01_basic_authentication.py
```

---

## ⚙️ Configuration

### Environment Variables

Copy [config/.env.example](config/.env.example) to `.env` and configure:

```bash
# Salesforce
SF_CLIENT_ID=your_consumer_key
SF_USERNAME=user@example.com
SF_PRIVATE_KEY_PATH=/path/to/server.key
SF_LOGIN_URL=https://test.salesforce.com

# Logging
LOG_DIR=./logs
LOG_LEVEL=INFO
```

### YAML Configuration

See [config/sync_config_example.yaml](config/sync_config_example.yaml) for pipeline configuration.

---

## 🧪 Testing

```bash
# Install dev dependencies
pip install -e ".[dev]"

# Run tests
pytest

# Run tests with coverage
pytest --cov=salesforce_toolkit --cov-report=html

# Run linter
flake8 salesforce_toolkit/

# Run type checker
mypy salesforce_toolkit/

# Format code
black salesforce_toolkit/
```

---

## 📖 API Reference

### Core Classes

- **`SalesforceSession`** - Authenticated session object
- **`SalesforceClient`** - Main API client for CRUD operations
- **`JWTAuthenticator`** - JWT Bearer Flow authentication
- **`OAuthAuthenticator`** - OAuth Password Flow authentication
- **`FieldMapper`** - Field mapping and transformation engine
- **`SyncPipeline`** - ETL pipeline for data synchronization

### Modules

- **`salesforce_toolkit.auth`** - Authentication providers
- **`salesforce_toolkit.core`** - Core client and session management
- **`salesforce_toolkit.mapping`** - Field mapping engine
- **`salesforce_toolkit.pipeline`** - Sync pipeline framework
- **`salesforce_toolkit.logging`** - Logging system
- **`salesforce_toolkit.utils`** - Utility functions

---

## 🛣️ Roadmap

- [x] Support for Bulk API 2.0 (async bulk operations)
- [x] Metadata API support (deploy/retrieve)
- [ ] Streaming API (PushTopic, Generic Streaming)
- [x] Built-in retry mechanism with exponential backoff
- [ ] Dry-run mode for pipelines
- [ ] Performance monitoring and metrics
- [ ] Integration with popular ORMs (SQLAlchemy, Django)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup

```bash
git clone https://github.com/yourusername/kinetic-core.git
cd kinetic-core
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -e ".[dev]"
```

---

## 🌐 Part of KineticMCP

Kinetic Core is the foundational library powering **KineticMCP**, an AI-powered Salesforce integration platform.

**KineticMCP** combines the capabilities of Kinetic Core with the Model Context Protocol (MCP) to enable intelligent automation, natural language interactions, and advanced data management for Salesforce.

- 🌐 **Website**: [kineticmcp.com](https://kineticmcp.com)
- 🤖 **AI Integration**: Natural language Salesforce operations
- 🔌 **MCP Protocol**: Seamless integration with AI assistants
- 📊 **Advanced Analytics**: AI-driven insights and reporting

Built and maintained by **Antonio Trento** - connecting Salesforce with the future of AI.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2025 Antonio Trento. All rights reserved.

See [COPYRIGHT](COPYRIGHT) for detailed attribution and trademark information.

---

## 👤 Author

**Antonio Trento**

- GitHub: [@antoniotrento](https://github.com/antoniotrento)
- LinkedIn: [Antonio Trento](https://linkedin.com/in/antoniotrento)
- Portfolio: [Salesforce Toolkit Case Study](https://antoniotrento.net/portfolio/kinetic-core/)

---

## 🙏 Acknowledgments

- Inspired by [Simple Salesforce](https://github.com/simple-salesforce/simple-salesforce)
- Built with [Requests](https://requests.readthedocs.io/)
- Powered by [PyJWT](https://pyjwt.readthedocs.io/)

---

## 📊 Project Stats

![GitHub stars](https://img.shields.io/github/stars/antonio-backend-projects/kinetic-core?style=social)
![GitHub forks](https://img.shields.io/github/forks/antonio-backend-projects/kinetic-core?style=social)
![GitHub issues](https://img.shields.io/github/issues/antonio-backend-projects/kinetic-core/)
![GitHub pull requests](https://img.shields.io/github/issues-pr/antonio-backend-projects/kinetic-core/)

---

<p align="center">
  Made with ❤️ by Antonio Trento
</p>
