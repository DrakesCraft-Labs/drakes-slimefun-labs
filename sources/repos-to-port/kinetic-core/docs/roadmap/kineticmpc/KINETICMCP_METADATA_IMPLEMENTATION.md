# KineticMCP Metadata Tools Implementation Roadmap

**Project:** KineticMCP Metadata API Tools
**Target Version:** v2.1.0
**Depends On:** kinetic-core v2.1.0
**Status:** Planning
**Last Updated:** 2025-01-02

---

## 🎯 Executive Summary

### Obiettivo

Implementare **MCP Tools per Salesforce Metadata API** in KineticMCP, utilizzando kinetic-core v2.1.0 come libreria base.

### Prerequisiti

- ✅ kinetic-core v2.1.0 deve essere pubblicato su PyPI
- ✅ Metadata API funzionante in kinetic-core
- ✅ KineticMCP v2.0.1 già operativo

### Nuovi Tools da Implementare

Total: **6-8 nuovi MCP tools**

1. `sf_create_field` - Crea campo custom
2. `sf_create_object` - Crea oggetto custom
3. `sf_backup_metadata` - Backup configurazione
4. `sf_deploy_metadata` - Deploy package metadata
5. `sf_compare_metadata` - Confronta due org
6. `sf_retrieve_metadata` - Scarica metadata
7. (Optional) `sf_create_validation_rule` - Crea validation rule
8. (Optional) `sf_deploy_workflow` - Deploy workflow

---

## 📊 Scope & Features

### Nuovi MCP Tools Dettagliati

#### 1. sf_create_field

Crea un campo custom su oggetto Salesforce.

**Signature:**
```python
@mcp.tool()
def sf_create_field(
    ctx: Context,
    sobject: str,
    field_name: str,
    field_type: str,
    label: str,
    length: Optional[int] = None,
    description: Optional[str] = None,
    help_text: Optional[str] = None,
    required: bool = False,
    unique: bool = False,
    external_id: bool = False,
    default_value: Optional[str] = None,
    picklist_values: Optional[List[str]] = None,
    reference_to: Optional[str] = None,
    formula: Optional[str] = None,
    check_only: bool = False
) -> Dict[str, Any]:
    """
    Create custom field on Salesforce object.

    Args:
        sobject: Object name (e.g., "Account")
        field_name: API name (e.g., "Phone_Verified__c")
        field_type: Field type (Text, Number, Checkbox, Picklist, Lookup, Formula, etc.)
        label: Display label
        ... other parameters
        check_only: Dry-run mode (preview without deploying)

    Returns:
        {
            "success": true,
            "field": "Phone_Verified__c",
            "sobject": "Account",
            "deployment_id": "0Af..."
        }
    """
```

**Claude Usage Example:**
```
User: "Crea un campo Phone_Verified__c (checkbox) sull'oggetto Account"

Claude: [Uses sf_create_field]
sobject: "Account"
field_name: "Phone_Verified__c"
field_type: "Checkbox"
label: "Phone Verified"
description: "Indicates if phone number has been verified"
help_text: "Check this when customer confirms their phone number"
default_value: "false"

Result: ✓ Field Phone_Verified__c created successfully on Account
```

---

#### 2. sf_create_object

Crea un oggetto custom completo con campi.

**Signature:**
```python
@mcp.tool()
def sf_create_object(
    ctx: Context,
    object_name: str,
    label: str,
    plural_label: str,
    fields: List[Dict[str, Any]],
    description: Optional[str] = None,
    enable_activities: bool = False,
    enable_reports: bool = True,
    enable_search: bool = True,
    sharing_model: str = "ReadWrite",
    check_only: bool = False
) -> Dict[str, Any]:
    """
    Create custom object with fields.

    Args:
        object_name: API name (e.g., "Customer_Feedback__c")
        label: Display label
        plural_label: Plural label
        fields: List of field definitions
        ... other parameters

    Returns:
        {
            "success": true,
            "object": "Customer_Feedback__c",
            "fields_created": 4,
            "deployment_id": "0Af..."
        }
    """
```

**Claude Usage Example:**
```
User: "Crea un oggetto Customer_Feedback__c con questi campi:
- Rating__c (number, 1-5)
- Comment__c (text area)
- Customer__c (lookup to Account)
- Submitted_Date__c (date)"

Claude: [Uses sf_create_object]
object_name: "Customer_Feedback__c"
label: "Customer Feedback"
plural_label: "Customer Feedbacks"
fields: [
    {
        "name": "Rating__c",
        "type": "Number",
        "label": "Rating",
        "precision": 1,
        "scale": 0
    },
    {
        "name": "Comment__c",
        "type": "TextArea",
        "label": "Comment",
        "length": 1000
    },
    {
        "name": "Customer__c",
        "type": "Lookup",
        "label": "Customer",
        "reference_to": "Account"
    },
    {
        "name": "Submitted_Date__c",
        "type": "Date",
        "label": "Submitted Date"
    }
]

Result: ✓ Object Customer_Feedback__c created with 4 fields
```

---

#### 3. sf_backup_metadata

Backup completo configurazione Salesforce.

**Signature:**
```python
@mcp.tool()
def sf_backup_metadata(
    ctx: Context,
    component_types: List[str],
    output_dir: str = "./salesforce_backup",
    include_all_custom: bool = True
) -> Dict[str, Any]:
    """
    Backup Salesforce metadata to local directory.

    Args:
        component_types: Types to backup (CustomObject, CustomField, etc.)
        output_dir: Local directory for backup
        include_all_custom: Include all custom components (*__c)

    Returns:
        {
            "success": true,
            "components_retrieved": 150,
            "output_dir": "./salesforce_backup",
            "file_count": 150
        }
    """
```

**Claude Usage Example:**
```
User: "Fai il backup di tutti i custom objects e fields"

Claude: [Uses sf_backup_metadata]
component_types: ["CustomObject", "CustomField"]
output_dir: "./salesforce_backup_2025-01-02"
include_all_custom: true

Result:
✓ Backup completed
  - 45 Custom Objects
  - 320 Custom Fields
  - Output: ./salesforce_backup_2025-01-02/
  - Ready for Git commit
```

---

#### 4. sf_deploy_metadata

Deploy package metadata da directory.

**Signature:**
```python
@mcp.tool()
def sf_deploy_metadata(
    ctx: Context,
    source_dir: str,
    run_tests: bool = True,
    rollback_on_error: bool = True,
    check_only: bool = False
) -> Dict[str, Any]:
    """
    Deploy metadata package to Salesforce.

    Args:
        source_dir: Directory with metadata package
        run_tests: Run Apex tests during deployment
        rollback_on_error: Auto-rollback if deployment fails
        check_only: Dry-run mode (validation only)

    Returns:
        {
            "success": true,
            "components_deployed": 25,
            "tests_run": 15,
            "tests_passed": 15,
            "deployment_id": "0Af..."
        }
    """
```

**Claude Usage Example:**
```
User: "Deploya la configurazione da ./sandbox_metadata in Production"

Claude: [Uses sf_deploy_metadata with check_only=true first]

Step 1: Validation (dry-run)
source_dir: "./sandbox_metadata"
check_only: true
run_tests: true

Validation result:
✓ 25 components validated
✓ 15 tests would run
✓ No errors found

Step 2: Real deployment
[Uses sf_deploy_metadata with check_only=false]
source_dir: "./sandbox_metadata"
check_only: false
run_tests: true
rollback_on_error: true

Result:
✓ Deployment successful
  - 25 components deployed
  - 15 tests passed
  - Time: 3 minutes
```

---

#### 5. sf_compare_metadata

Confronta metadata tra due org.

**Signature:**
```python
@mcp.tool()
def sf_compare_metadata(
    ctx: Context,
    source_org_name: str,
    target_org_name: str,
    component_types: List[str]
) -> Dict[str, Any]:
    """
    Compare metadata between two Salesforce orgs.

    Args:
        source_org_name: Source org identifier (from session)
        target_org_name: Target org identifier
        component_types: Types to compare

    Returns:
        {
            "missing_in_target": [...],
            "different_values": [...],
            "extra_in_target": [...]
        }
    """
```

**Claude Usage Example:**
```
User: "Quali sono le differenze tra Sandbox e Production?"

Claude: [Uses sf_compare_metadata]
source_org_name: "sandbox"
target_org_name: "production"
component_types: ["CustomObject", "CustomField"]

Result:
Missing in Production:
  - Customer_Feedback__c (object)
  - Account.Phone_Verified__c (field)
  - Opportunity.Expected_Close_Date__c (field)

Different values:
  - Account.Industry (picklist values differ)

Extra in Production:
  - Legacy_System__c (object - only in prod)
```

---

#### 6. sf_retrieve_metadata

Scarica metadata specifici.

**Signature:**
```python
@mcp.tool()
def sf_retrieve_metadata(
    ctx: Context,
    component_type: str,
    component_names: List[str],
    output_dir: str = "./metadata"
) -> Dict[str, Any]:
    """
    Retrieve specific metadata components.

    Args:
        component_type: Type (CustomObject, CustomField, etc.)
        component_names: Specific components to retrieve
        output_dir: Output directory

    Returns:
        {
            "success": true,
            "components_retrieved": 5,
            "output_dir": "./metadata"
        }
    """
```

---

## 📅 Implementation Timeline & Release Process

### Sprint 1 (Week 1): Setup & Basic Tools

**Goals:**
- ✅ Update kinetic-core dependency
- ✅ Implement sf_create_field
- ✅ Implement sf_create_object

**Tasks:**

1. **Create Branch:**
   ```bash
   cd kineticmcp
   git checkout -b feature/metadata-tools
   ```

2. **Update Dependencies:**
   - Edit `requirements.txt`:
     ```
     kinetic-core>=2.1.0  # Updated from 2.0.1
     ```

3. **Implement sf_create_field:**
   - File: `src/mcp_salesforce_server/server.py`
   - Add tool function
   - Parameter validation
   - Error handling
   - Use `client.metadata.deploy_field()`

4. **Implement sf_create_object:**
   - File: `src/mcp_salesforce_server/server.py`
   - Add tool function
   - Handle fields array
   - Use `client.metadata.deploy_object()`

5. **Unit Tests:**
   - File: `tests/test_metadata_tools.py`
   - Test field creation
   - Test object creation
   - Mock Salesforce responses

6. **Commit Changes:**
   ```bash
   git commit -m "Add sf_create_field and sf_create_object tools"
   ```

**Deliverables:**
- 2 new MCP tools working
- Unit tests passing
- Documentation comments

**Time Estimate:** 8-10 hours

---

### Sprint 2 (Week 2): Backup & Deploy Tools

**Goals:**
- ✅ Implement sf_backup_metadata
- ✅ Implement sf_deploy_metadata
- ✅ Implement sf_retrieve_metadata

**Tasks:**

1. **Implement sf_backup_metadata:**
   - Use `client.metadata.retrieve()`
   - Handle component types
   - Directory management
   - Progress feedback

2. **Implement sf_deploy_metadata:**
   - Use `client.metadata.deploy()`
   - Dry-run support
   - Test execution
   - Rollback handling

3. **Implement sf_retrieve_metadata:**
   - Selective retrieval
   - Component filtering

4. **Integration Tests:**
   - Test with real Salesforce org
   - Backup → Deploy cycle
   - Error scenarios

5. **Commit Changes:**
   ```bash
   git commit -m "Add metadata backup and deploy tools"
   ```

**Deliverables:**
- 3 more MCP tools working
- Integration tests passing

**Time Estimate:** 10-12 hours

---

### Sprint 3 (Week 3): Advanced Tools & Polish

**Goals:**
- ✅ Implement sf_compare_metadata
- ✅ Add helper utilities
- ✅ Polish error messages
- ✅ Add progress indicators

**Tasks:**

1. **Implement sf_compare_metadata:**
   - Multi-session support
   - Diff logic
   - Clear reporting

2. **Helper Functions:**
   - Validation helpers
   - Error formatting
   - Progress callbacks

3. **Error Messages:**
   - User-friendly messages
   - Actionable suggestions
   - Clear formatting

4. **Testing:**
   - All tools tested
   - Error cases covered
   - Edge cases handled

5. **Commit Changes:**
   ```bash
   git commit -m "Add metadata comparison and polish"
   ```

**Deliverables:**
- All tools complete
- Error handling polished
- Full test coverage

**Time Estimate:** 8-10 hours

---

### Sprint 4 (Week 4): Documentation ⭐ CRITICO

**Goals:**
- ✅ Update TOOLS_REFERENCE.md
- ✅ Create METADATA_TOOLS_GUIDE.md
- ✅ Update README.md
- ✅ Update CHANGELOG.md

**Tasks:**

1. **Update TOOLS_REFERENCE.md:**
   - Add section "Metadata Tools (6)"
   - Document each tool:
     - Parameters
     - Return values
     - Examples
     - Use cases

2. **Create METADATA_TOOLS_GUIDE.md:**
   ```markdown
   # Metadata Tools User Guide

   ## Quick Start
   - Create your first custom field
   - Backup your org
   - Deploy between environments

   ## Use Cases
   - Provisioning new customers
   - Sandbox → Production deployment
   - Configuration as Code

   ## Examples
   - Real-world scenarios
   - Claude conversation examples

   ## Best Practices
   - When to use which tool
   - Error handling
   - Testing strategies
   ```

3. **Update README.md:**
   - Update tools count: 15 → 21-23 tools
   - Add Metadata Tools section:
     ```markdown
     **Metadata Tools (6):**
     - `sf_create_field`: Create custom field
     - `sf_create_object`: Create custom object
     - `sf_backup_metadata`: Backup configuration
     - `sf_deploy_metadata`: Deploy package
     - `sf_compare_metadata`: Compare orgs
     - `sf_retrieve_metadata`: Retrieve specific metadata
     ```

4. **Update CHANGELOG.md:**
   ```markdown
   ## [2.1.0] - 2025-XX-XX

   ### Added

   #### Metadata API Tools (6 new tools)
   - `sf_create_field` - Create custom fields programmatically
   - `sf_create_object` - Create custom objects with fields
   - `sf_backup_metadata` - Backup Salesforce configuration
   - `sf_deploy_metadata` - Deploy metadata packages
   - `sf_compare_metadata` - Compare two orgs
   - `sf_retrieve_metadata` - Retrieve specific metadata

   ### Changed
   - Updated dependency: `kinetic-core>=2.1.0` (from 2.0.1)
   - Tools count: 15 → 21 (6 new metadata tools)

   ### Features
   - Configuration as Code with Git versioning
   - Automated customer provisioning
   - Sandbox → Production deployment automation
   - Metadata backup and restore
   - Cross-org comparison

   ### Breaking Changes
   ❌ **NONE** - Fully backward compatible
   ```

5. **Test Documentation:**
   - Build locally: `mkdocs serve` (if using MkDocs)
   - Check all links
   - Verify examples

6. **Commit Documentation:**
   ```bash
   git commit -m "Add complete Metadata Tools documentation"
   ```

**Deliverables:**
- Complete tool reference
- User guide
- Updated README and CHANGELOG

**Time Estimate:** 6-8 hours

---

### Sprint 5 (Week 5): Pre-Release Preparation ⭐ CRITICO

**Goals:**
- ✅ Merge to main
- ✅ Version bump
- ✅ Final testing

**Tasks:**

1. **Merge to Main:**
   ```bash
   git checkout main
   git merge feature/metadata-tools
   ```

2. **Version Bump:**
   - Update version number if needed
   - Currently KineticMCP doesn't have version in code
   - But update CHANGELOG date

3. **Pre-Release Checks:**
   - All tests pass
   - Documentation complete
   - No broken links
   - README rendering correct

4. **Integration Test with kinetic-core v2.1.0:**
   ```bash
   # Create test environment
   python -m venv test_integration
   source test_integration/bin/activate

   # Install kinetic-core v2.1.0
   pip install kinetic-core==2.1.0

   # Install KineticMCP
   pip install -e .

   # Test all metadata tools
   python -c "
   from mcp_salesforce_server.server import (
       sf_create_field,
       sf_create_object,
       sf_backup_metadata,
       sf_deploy_metadata,
       sf_compare_metadata,
       sf_retrieve_metadata
   )
   print('✓ All metadata tools imported successfully')
   "

   deactivate
   rm -rf test_integration
   ```

5. **Final Commit:**
   ```bash
   git commit -m "Prepare for v2.1.0 release"
   ```

**Deliverables:**
- Feature merged to main
- All tests passing
- Integration verified

**Time Estimate:** 2-3 hours

---

### Sprint 6: Git Tag & Push ⭐ CRITICO

**Goals:**
- ✅ Create Git tag
- ✅ Push to GitHub

**Tasks:**

1. **Create Git Tag:**
   ```bash
   git tag -a v2.1.0 -m "Release v2.1.0 - Metadata API Tools"
   ```

2. **Push to GitHub:**
   ```bash
   git push origin main
   git push origin v2.1.0
   ```

3. **Verify:**
   - Check tag on GitHub: https://github.com/KineticMCP/kineticmcp/tags

**Deliverables:**
- Git tag created and pushed

**Time Estimate:** 15 minutes

---

### Sprint 7: GitHub Release ⭐ CRITICO

**Goals:**
- ✅ Create GitHub Release
- ✅ Add release notes

**Tasks:**

1. **Create GitHub Release:**
   - Go to: https://github.com/KineticMCP/kineticmcp/releases/new
   - Select tag: v2.1.0
   - Title: `v2.1.0 - Metadata API Tools`

2. **Release Description:**
   ```markdown
   # KineticMCP v2.1.0 - Metadata API Tools 🚀

   ## Major New Features

   ### 6 New Metadata Tools ⭐

   Manage Salesforce configuration through Claude with natural language!

   **New Tools:**
   1. `sf_create_field` - Create custom fields
   2. `sf_create_object` - Create custom objects
   3. `sf_backup_metadata` - Backup configuration
   4. `sf_deploy_metadata` - Deploy packages
   5. `sf_compare_metadata` - Compare orgs
   6. `sf_retrieve_metadata` - Retrieve metadata

   **Use Cases:**

   ```
   User: "Create a Phone_Verified__c checkbox field on Account"
   Claude: [Uses sf_create_field] ✓ Field created

   User: "Backup all custom objects and fields"
   Claude: [Uses sf_backup_metadata] ✓ 150 components backed up

   User: "What's different between Sandbox and Production?"
   Claude: [Uses sf_compare_metadata] Shows differences
   ```

   **Powered by:**
   - kinetic-core v2.1.0 (Metadata API support)
   - 21 total MCP tools (was 15)

   ## What's Changed

   See [CHANGELOG.md](CHANGELOG.md) for complete details.

   ## Installation

   ```bash
   pip install --upgrade kineticmcp
   ```

   **Requirements:**
   - kinetic-core >= 2.1.0
   - Connected App with "full" OAuth scope

   ## Documentation

   - [Tools Reference](docs/TOOLS_REFERENCE.md)
   - [Metadata Tools Guide](docs/METADATA_TOOLS_GUIDE.md)
   - [Changelog](CHANGELOG.md)

   ## Breaking Changes

   ❌ **NONE** - Fully backward compatible with v2.0.x

   ---

   **Full Changelog**: https://github.com/KineticMCP/kineticmcp/compare/v2.0.1...v2.1.0
   ```

3. **Publish Release:**
   - Click "Publish release"

**Deliverables:**
- GitHub Release published
- Release notes complete

**Time Estimate:** 45 minutes

---

### Sprint 8: Post-Release Verification & Communication

**Goals:**
- ✅ End-to-end testing
- ✅ Update documentation website
- ✅ Announce release

**Tasks:**

1. **End-to-End Testing:**
   ```bash
   # Fresh Claude Desktop test
   1. Restart Claude Desktop
   2. Test: "Create a test field on Account"
   3. Test: "Backup my Salesforce configuration"
   4. Test: "What metadata tools are available?"
   5. Verify all 6 new tools work
   ```

2. **Update Documentation Website:**
   - If using MkDocs/GitHub Pages:
     ```bash
     mkdocs gh-deploy
     ```

3. **Announce Release:**
   - GitHub Discussions
   - Twitter/LinkedIn:
     ```
     🚀 KineticMCP v2.1.0 released!

     6 new Metadata API tools:
     - Create fields & objects via Claude
     - Backup Salesforce config
     - Deploy between environments
     - Compare orgs

     Configuration as Code with AI!

     https://github.com/KineticMCP/kineticmcp/releases/tag/v2.1.0
     ```

4. **Monitor for Issues:**
   - Watch GitHub Issues
   - Monitor Claude Desktop integrations
   - Check for error reports

**Deliverables:**
- End-to-end test passed
- Release announced
- Monitoring active

**Time Estimate:** 1-2 hours

---

**Total Implementation + Release Time:** 40-50 hours (1-1.5 months part-time)

---

## 🎯 Success Criteria

### All Tools Working When:
- ✅ `sf_create_field` can create any field type
- ✅ `sf_create_object` can create object with multiple fields
- ✅ `sf_backup_metadata` backs up full org configuration
- ✅ `sf_deploy_metadata` deploys packages successfully
- ✅ `sf_compare_metadata` shows accurate differences
- ✅ `sf_retrieve_metadata` retrieves specific components
- ✅ All tools have proper error handling
- ✅ All tools work in Claude Desktop
- ✅ Documentation is complete and accurate

---

## 📋 Release Checklist

```markdown
## KineticMCP v2.1.0 Release Checklist

### Development
- [ ] Feature branch created
- [ ] kinetic-core dependency updated to >=2.1.0
- [ ] sf_create_field implemented
- [ ] sf_create_object implemented
- [ ] sf_backup_metadata implemented
- [ ] sf_deploy_metadata implemented
- [ ] sf_compare_metadata implemented
- [ ] sf_retrieve_metadata implemented
- [ ] Unit tests written and passing
- [ ] Integration tests passing

### Documentation
- [ ] TOOLS_REFERENCE.md updated (21 tools)
- [ ] METADATA_TOOLS_GUIDE.md created
- [ ] README.md updated
- [ ] CHANGELOG.md updated
- [ ] All examples tested
- [ ] Links verified

### Pre-Release
- [ ] Feature merged to main
- [ ] All tests pass
- [ ] Documentation complete
- [ ] Integration with kinetic-core v2.1.0 verified

### Release
- [ ] Git tag created (v2.1.0)
- [ ] Tag pushed to GitHub
- [ ] GitHub Release created
- [ ] Release notes complete

### Post-Release
- [ ] End-to-end test in Claude Desktop
- [ ] Documentation website updated
- [ ] Release announced
- [ ] Monitoring active
```

---

## 🔄 Dependencies

### On kinetic-core

**Critical Dependency:**
- kinetic-core v2.1.0 MUST be released to PyPI first
- KineticMCP cannot be released until kinetic-core v2.1.0 is available

**Dependency Chain:**
```
kinetic-core v2.1.0 (PyPI)
         ↓
KineticMCP requirements.txt: kinetic-core>=2.1.0
         ↓
KineticMCP v2.1.0 uses metadata API from kinetic-core
```

---

## 🚨 Risks & Mitigations

### Risk 1: kinetic-core v2.1.0 Not Ready

**Mitigation:**
- Wait for kinetic-core v2.1.0 PyPI release
- Test with kinetic-core from TestPyPI during development

### Risk 2: API Changes in kinetic-core

**Mitigation:**
- Stay aligned with kinetic-core development
- Test frequently during development

### Risk 3: Complex Error Messages

**Mitigation:**
- Wrap kinetic-core exceptions
- Provide user-friendly messages
- Include actionable suggestions

---

## 📚 Documentation Structure

```
kineticmcp/
├── README.md (updated)
├── CHANGELOG.md (updated)
└── docs/
    ├── TOOLS_REFERENCE.md (updated - 21 tools)
    ├── METADATA_TOOLS_GUIDE.md (NEW)
    └── BULK_API_GUIDE.md (existing)
```

---

## ✅ Next Steps

1. **Wait for kinetic-core v2.1.0 release**
2. **Start Sprint 1** when kinetic-core is on PyPI
3. **Follow release process** as documented

---

**Roadmap Prepared By:** Claude Code
**For Project:** KineticMCP v2.1.0
**Date:** 2025-01-02

**Status:** 📋 Ready for Implementation (after kinetic-core v2.1.0 release)
