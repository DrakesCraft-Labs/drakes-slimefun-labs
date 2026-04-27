# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.1] - 2026-01-03
### Fixed
- **PyPI Documentation**: Fixed relative links in `README.md` to ensure they render correctly on PyPI.

## [2.1.0] - 2026-01-03
### Added
- **Metadata API Support**: Full native support for Salesforce Metadata API
  - `MetadataClient` accessible via `client.metadata`
  - `CustomObject` and `CustomField` data models
  - `deploy()` and `retrieve()` operations
  - `compare()` logic to diff local vs remote schema
  - Helper methods: `deploy_field()`, `deploy_object()`
  - Built-in templates for common business objects (`create_from_template`)
  - Integration tests verifying full deployment lifecycle

## [2.0.1] - 2025-01-02

### Fixed
- **Documentation Links**: Fixed all documentation links in README.md for PyPI compatibility
  - Changed relative paths to absolute URLs pointing to https://kineticmcp.com/kinetic-core/
  - Links now work correctly on both PyPI and GitHub
  - Added proper link structure for MkDocs documentation site

### Changed
- **MkDocs Navigation**: Enhanced documentation structure
  - Added Installation guide to Getting Started section
  - Created dedicated "Bulk API Guide" section with Quick Start and Examples
  - Improved documentation organization and discoverability

---

## [2.0.0] - 2025-01-02

### Added

#### Bulk API v2 Support - Major New Feature
- **NEW**: Complete Salesforce Bulk API v2 implementation for high-volume data operations
- `BulkV2Client` class accessible via `client.bulk` property on `SalesforceClient`
- Support for all Bulk API v2 operations:
  - `insert()` - Bulk insert records
  - `update()` - Bulk update existing records (requires Id field)
  - `upsert()` - Bulk insert or update using external ID field
  - `delete()` - Bulk delete records
  - `hard_delete()` - Permanently delete records (bypasses recycle bin)
  - `query()` - Export large datasets with SOQL queries

#### New Modules and Classes
- `kinetic_core.bulk.client` - `BulkV2Client` main client implementation
- `kinetic_core.bulk.job` - Data models for bulk operations:
  - `BulkJob` - Job status and metadata tracking
  - `BulkResult` - Operation results with success/failure counts
  - `BulkQueryResult` - Query-specific results
  - `BulkError` - Detailed error information for failed records
- `kinetic_core.bulk.operations` - `BulkOperation` enum (INSERT, UPDATE, UPSERT, DELETE, HARD_DELETE, QUERY)
- `kinetic_core.bulk.serializer` - `CSVSerializer` for CSV conversion
- `kinetic_core.bulk.poller` - `JobPoller` with exponential backoff polling

#### Features
- Automatic CSV serialization/deserialization for record data
- Asynchronous job processing with intelligent status polling
- Exponential backoff strategy for efficient API usage (2s initial, 30s max, 1.5x factor)
- Progress callbacks for long-running operations
- Configurable operation timeouts (default 10 minutes for operations, 30 minutes for queries)
- Comprehensive error reporting with per-record failure details
- Support for external ID fields in upsert operations
- Lazy initialization of Bulk client to minimize overhead when not needed
- Full type hints throughout for excellent IDE support
- Field validation (Id required for update/delete, external ID for upsert)

### Changed
- **Package version**: 1.1.0 → 2.0.0 (major version for significant new capability)
- **Development status**: Beta → Production/Stable in setup.py classifiers
- **Package description**: Enhanced to highlight Bulk API v2 support
- **Keywords**: Added `bulk-api`, `bulk-api-v2`, `high-volume`, `data-processing`
- **Public API exports**: Added Bulk classes to `kinetic_core.__init__.py`
- **Legal headers**: Added copyright and attribution headers to all source files
- **Documentation**: Added COPYRIGHT file with project information and attribution guidelines

### Technical Details
- Salesforce API version: v60.0
- CSV format for data upload/download
- HTTP methods: POST (create job), PUT (upload data), PATCH (close/abort job), GET (status/results)
- Job states: Open → UploadComplete → InProgress → JobComplete/Failed/Aborted

### Backward Compatibility
**100% backward compatible** - No breaking changes introduced:

```python
# Existing code continues to work unchanged
client = SalesforceClient(session)
client.create("Account", {"Name": "Test"})  # Still works
client.query("SELECT Id FROM Account")       # Still works
```

### Usage Example
```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Bulk insert 10,000 records
records = [{"Name": f"Account {i}", "Industry": "Technology"} for i in range(10000)]
result = client.bulk.insert("Account", records)

print(f"✓ Successfully inserted: {result.success_count}")
print(f"✗ Failed: {result.failed_count}")

# Check for errors
if result.errors:
    for error in result.errors[:5]:  # Show first 5 errors
        print(f"Error: {error.message}")
```

### Performance
- Optimized for millions of records
- Recommended for operations with >2,000 records
- Async job processing allows non-blocking operations
- Efficient CSV serialization minimizes memory usage
- Supports up to 150 million records per job (Salesforce limit)

## [1.1.0] - 2025-12-20
### Renaming
- Renamed project from `salesforce-toolkit` to **Kinetic Core**.
- Updated internal package structure to `kinetic_core`.
- Added backward compatibility shim for `salesforce_toolkit` imports.

### Changed
- CLI command `sf-toolkit` now points to `kinetic_core` engine.
- Documentation updated to reflect the new rebrand.

## [1.0.0] - 2025-12-05

### Added
- Initial release of Salesforce Toolkit
- JWT Bearer Flow authentication
- OAuth 2.0 Password Flow authentication
- Generic Salesforce client for CRUD operations
- Support for any Salesforce object (standard and custom)
- Field mapping engine with transformations
- Sync pipeline framework for ETL operations
- Comprehensive logging system with rotation
- Command-line interface (CLI)
- Configuration via YAML files
- Utility functions for common Salesforce operations
- Batch operations support
- Automatic query pagination
- Complete documentation and examples

### Features
- Create, Read, Update, Delete operations on any Salesforce object
- Bulk create via Composite API
- Upsert support with external ID fields
- SOQL query execution with automatic pagination
- Field mapping with nested field access (dot notation)
- Built-in transformations (lowercase, uppercase, date formatting, etc.)
- Custom transformation functions
- Multiple sync modes (INSERT, UPDATE, UPSERT, DELETE)
- Progress tracking with callbacks
- Colored console logging
- File logging with rotation
- Environment-based configuration
- CLI commands for common operations

### Documentation
- Comprehensive README with examples
- API reference documentation
- Usage examples for all major features
- Configuration templates
- YAML configuration examples

## [Unreleased]

### Planned
- See `docs/roadmap/` for future plans.

---

For more details, see the [README.md](README.md).
