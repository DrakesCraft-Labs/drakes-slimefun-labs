# Kinetic Core - Documentation Hub

Welcome to the Kinetic Core documentation! This page helps you find the right documentation for your needs.

## 🚀 Quick Navigation

### New to Kinetic Core?
Start here:
1. **[Quick Start Guide](QUICK_START.md)** - Get up and running in 5 minutes
2. **[Salesforce Setup Guide](SALESFORCE_SETUP_GUIDE.md)** - Configure your Salesforce org
3. **[User Guide](USER_GUIDE.md)** - Complete feature walkthrough

### Working with Bulk API v2? ⚡ NEW
- **[Bulk API v2 Reference](api/BULK_API_V2.md)** - Complete API documentation
- **[Bulk Quick Start](guides/BULK_QUICKSTART.md)** - Start using Bulk API in 5 minutes
- **[Bulk Examples](examples/BULK_EXAMPLES.md)** - Real-world implementation patterns

---

## 📖 Documentation by Category

### API Reference
Complete technical reference for all modules:

- **[Bulk API v2](api/BULK_API_V2.md)** ⚡ NEW in v2.0.0
  - Full Salesforce Bulk API v2 implementation
  - Process millions of records
  - All operations: insert, update, upsert, delete, query

### Guides & Tutorials

#### Getting Started
- **[Quick Start](QUICK_START.md)** - 5-minute getting started guide
- **[Salesforce Setup](SALESFORCE_SETUP_GUIDE.md)** - Configure connected app and permissions
- **[User Guide](USER_GUIDE.md)** - Comprehensive usage guide
- **[Testing Guide](TESTING_GUIDE.md)** - Test your Salesforce integration

#### Bulk API v2 Guides ⚡ NEW
- **[Bulk Quick Start](guides/BULK_QUICKSTART.md)** - Get started with Bulk API
- **[Migration Guide](guides/BULK_MIGRATION.md)** - Migrate from Bulk API v1 (coming soon)

#### Deployment
- **[Docker Guide](DOCKER_GUIDE.md)** - Run in containers
- **[Publishing Guide](PUBLISHING_GUIDE.md)** - Publish your own package

### Examples & Use Cases

- **[Bulk API Examples](examples/BULK_EXAMPLES.md)** ⚡ NEW
  - Data migration
  - CSV import/export
  - Scheduled batch jobs
  - Large-scale updates
  - Error recovery

### Configuration

- **[Bulk API Configuration](SALESFORCE_BULK_CONFIG.md)** - Bulk API setup
- **[Environment Variables](QUICK_START.md#1-setup-environment-variables)** - .env configuration

### Advanced Topics

- **[Bulk API Analysis](BULK_API_ANALYSIS.md)** - Deep dive into Bulk API architecture
- **[Roadmap](roadmap/KINETICMCP_UPGRADE_IMPACT.md)** - Future plans and KineticMCP integration

---

## 🎯 Documentation by Use Case

### "I want to..."

#### Process Large Datasets
→ **[Bulk API v2 Reference](api/BULK_API_V2.md)**
- Insert/update/delete millions of records
- Export large datasets
- Handle high-volume operations

#### Get Started Quickly
→ **[Quick Start Guide](QUICK_START.md)**
- Setup authentication
- Basic CRUD operations
- First data sync

#### Import Data from CSV
→ **[Bulk Examples - CSV Import](examples/BULK_EXAMPLES.md#csv-import)**
- Read CSV files
- Map fields
- Batch import

#### Sync Data Regularly
→ **[Bulk Examples - Scheduled Jobs](examples/BULK_EXAMPLES.md#scheduled-batch-jobs)**
- Automated sync
- Scheduling
- Error handling

#### Migrate from Another System
→ **[Bulk Examples - Data Migration](examples/BULK_EXAMPLES.md#data-migration)**
- Large-scale migration
- Progress tracking
- Failure recovery

#### Set Up Salesforce
→ **[Salesforce Setup Guide](SALESFORCE_SETUP_GUIDE.md)**
- Create connected app
- Generate certificates
- Configure permissions

#### Deploy with Docker
→ **[Docker Guide](DOCKER_GUIDE.md)**
- Build images
- Run containers
- Production deployment

#### Test My Integration
→ **[Testing Guide](TESTING_GUIDE.md)**
- Unit tests
- Integration tests
- Bulk API testing

---

## 📚 API Documentation Structure

```
docs/
├── README.md                           # This file
├── QUICK_START.md                      # Quick start guide
├── USER_GUIDE.md                       # Complete user guide
├── SALESFORCE_SETUP_GUIDE.md           # Salesforce configuration
├── TESTING_GUIDE.md                    # Testing guide
├── DOCKER_GUIDE.md                     # Docker deployment
│
├── api/                                # API Reference
│   └── BULK_API_V2.md                  # Bulk API v2 complete reference ⚡ NEW
│
├── guides/                             # Tutorial Guides
│   ├── BULK_QUICKSTART.md              # Bulk API quick start ⚡ NEW
│   └── BULK_MIGRATION.md               # Migration guide (coming soon)
│
├── examples/                           # Code Examples
│   └── BULK_EXAMPLES.md                # Bulk API examples ⚡ NEW
│
└── roadmap/                            # Project Roadmap
    └── KINETICMCP_UPGRADE_IMPACT.md    # KineticMCP integration plan
```

---

## 🆕 What's New in v2.0.0

### Bulk API v2 Support
Complete native implementation of Salesforce Bulk API v2:

✨ **New Features**:
- Process up to 150 million records per job
- Full support for all operations (insert, update, upsert, delete, hard_delete, query)
- Smart exponential backoff polling
- Progress tracking with callbacks
- Comprehensive error reporting
- Type-safe with full type hints

📖 **New Documentation**:
- [Complete API Reference](api/BULK_API_V2.md)
- [Quick Start Guide](guides/BULK_QUICKSTART.md)
- [Practical Examples](examples/BULK_EXAMPLES.md)

---

## 💡 Tips for Finding Documentation

### By Experience Level

**Beginner**:
1. Start with [Quick Start](QUICK_START.md)
2. Read [User Guide](USER_GUIDE.md)
3. Try [Bulk Quick Start](guides/BULK_QUICKSTART.md) for large datasets

**Intermediate**:
1. Review [Bulk API Reference](api/BULK_API_V2.md)
2. Explore [Examples](examples/BULK_EXAMPLES.md)
3. Check [Testing Guide](TESTING_GUIDE.md)

**Advanced**:
1. Study [Bulk API Analysis](BULK_API_ANALYSIS.md)
2. Review [Roadmap](roadmap/KINETICMCP_UPGRADE_IMPACT.md)
3. Contribute via [GitHub](https://github.com/antonio-backend-projects/kinetic-core)

### By Topic

| Topic | Documentation |
|-------|--------------|
| Authentication | [Quick Start](QUICK_START.md#1-setup-environment-variables) |
| CRUD Operations | [User Guide](USER_GUIDE.md) |
| Bulk Operations | [Bulk API v2](api/BULK_API_V2.md) |
| Field Mapping | [User Guide](USER_GUIDE.md) |
| Data Pipelines | [User Guide](USER_GUIDE.md) |
| Error Handling | [Bulk Examples](examples/BULK_EXAMPLES.md#error-recovery) |
| Testing | [Testing Guide](TESTING_GUIDE.md) |
| Deployment | [Docker Guide](DOCKER_GUIDE.md) |

---

## 🔗 External Resources

### Official Salesforce Documentation
- [Bulk API v2 Developer Guide](https://developer.salesforce.com/docs/atlas.en-us.api_asynch.meta/api_asynch/)
- [REST API Developer Guide](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/)
- [SOQL Reference](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/)

### Kinetic Core Resources
- [GitHub Repository](https://github.com/antonio-backend-projects/kinetic-core)
- [PyPI Package](https://pypi.org/project/kinetic-core/)
- [Issue Tracker](https://github.com/antonio-backend-projects/kinetic-core/issues)
- [KineticMCP Website](https://kineticmcp.com)

### Community
- [Stack Overflow](https://stackoverflow.com/questions/tagged/kinetic-core)
- [GitHub Discussions](https://github.com/antonio-backend-projects/kinetic-core/discussions)

---

## 📝 Contributing to Documentation

Found an issue or want to improve the docs?

1. Fork the repository
2. Edit the documentation
3. Submit a pull request

All documentation files are in Markdown format and located in the `docs/` directory.

---

## 📧 Need Help?

Can't find what you're looking for?

- **GitHub Issues**: [Report a documentation issue](https://github.com/antonio-backend-projects/kinetic-core/issues)
- **Discussions**: [Ask a question](https://github.com/antonio-backend-projects/kinetic-core/discussions)
- **Email**: info@antoniotrento.net

---

<p align="center">
  <strong>Kinetic Core</strong> - Part of the <a href="https://kineticmcp.com">KineticMCP</a> ecosystem
  <br>
  Built with ❤️ by <a href="https://antoniotrento.net">Antonio Trento</a>
</p>
