# Metadata API Implementation Roadmap

**Project:** kinetic-core Metadata API Support
**Target Version:** v2.1.0
**Status:** Planning
**Last Updated:** 2025-01-02

---

## 🎯 Executive Summary

### Obiettivo

Implementare **Salesforce Metadata API** in kinetic-core per permettere la gestione programmatica della configurazione Salesforce (campi custom, oggetti, validation rules, workflow, ecc.).

### Perché è Importante

**Problema attuale:**
- ❌ Configurazione Salesforce solo via Web UI (lento, manuale, error-prone)
- ❌ Nessun versionamento delle configurazioni
- ❌ Deployment tra ambienti difficile e manuale
- ❌ Setup clienti richiede ore di click

**Soluzione con Metadata API:**
- ✅ Configurazione programmatica via codice
- ✅ Versionamento con Git (Configuration as Code)
- ✅ Deployment automatizzato tra ambienti
- ✅ Setup clienti in minuti invece di ore
- ✅ Rollback facile a configurazioni precedenti

### Benefici Attesi

| Aspetto | Prima | Dopo | Miglioramento |
|---------|-------|------|---------------|
| **Setup nuovo cliente** | 2-3 ore | 5 minuti | 24-36x più veloce |
| **Deployment Sandbox→Prod** | 1-2 ore | 10 minuti | 6-12x più veloce |
| **Errori configurazione** | Frequenti | Rari | Validazione automatica |
| **Documentazione** | Assente | Automatica | XML versionato |
| **Rollback** | Difficile | Immediato | Git revert |

---

## 📊 Scope & Features

### FASE 1: Metadata Retrieval (Read-Only) ⭐ PRIORITÀ ALTA

Implementare capacità di **leggere** metadata esistenti.

#### Features

**1.1 Retrieve Metadata Components**
```python
client.metadata.retrieve(
    component_types=["CustomObject", "CustomField"],
    package_names=["*__c"],  # Tutti custom objects
    output_dir="./metadata_backup"
)
```

**Componenti supportati (Fase 1):**
- ✅ Custom Objects (`CustomObject`)
- ✅ Custom Fields (`CustomField`)
- ✅ Validation Rules (`ValidationRule`)
- ✅ Workflow Rules (`WorkflowRule`)
- ✅ Page Layouts (`Layout`)
- ✅ Record Types (`RecordType`)
- ✅ Profiles (`Profile`)
- ✅ Permission Sets (`PermissionSet`)

**Output:**
- File XML per ogni componente
- `package.xml` con manifest
- Struttura compatibile con SFDX/Salesforce CLI

**Casi d'Uso:**
1. **Backup automatico** configurazione org
2. **Documentazione** automatica
3. **Analisi differenze** tra org
4. **Versionamento** con Git

---

### FASE 2: Metadata Deployment (Write) ⭐ PRIORITÀ ALTA

Implementare capacità di **creare/modificare** metadata.

#### Features

**2.1 Deploy Metadata Package**
```python
result = client.metadata.deploy(
    source_dir="./metadata_to_deploy",
    run_tests=True,
    rollback_on_error=True,
    check_only=False  # False = deploy reale, True = dry-run
)
```

**2.2 Create Custom Field**
```python
field = CustomField(
    sobject="Account",
    name="Phone_Verified__c",
    label="Phone Verified",
    type="Checkbox",
    default_value=False,
    description="Indicates if phone has been verified"
)

result = client.metadata.deploy_field(field)
```

**2.3 Create Custom Object**
```python
obj = CustomObject(
    name="Customer_Feedback__c",
    label="Customer Feedback",
    plural_label="Customer Feedbacks",
    fields=[
        CustomField(name="Rating__c", type="Number", ...),
        CustomField(name="Comment__c", type="TextArea", ...),
        CustomField(name="Customer__c", type="Lookup", ...)
    ]
)

result = client.metadata.deploy_object(obj)
```

**2.4 Validation & Safety**
- ✅ XML schema validation
- ✅ Dry-run mode (preview changes)
- ✅ Dependency checking
- ✅ Rollback automatico su errore
- ✅ Test execution obbligatoria per Production

**Casi d'Uso:**
1. **Provisioning clienti** automatizzato
2. **Deployment CI/CD** Sandbox → Production
3. **Setup ambienti** identici (Training, Demo)
4. **Migrazione** configurazioni tra org

---

### FASE 3: Advanced Metadata Operations ⭐ PRIORITÀ MEDIA

Features avanzate per gestione metadata.

#### Features

**3.1 Metadata Comparison**
```python
diff = client.metadata.compare(
    source_org=sandbox_client,
    target_org=production_client,
    component_types=["CustomObject", "CustomField"]
)

# Output
{
    "missing_in_target": [...],
    "different_values": [...],
    "extra_in_target": [...]
}
```

**3.2 Selective Deployment**
```python
# Deploy solo componenti modificati
changed = client.metadata.get_changed_components(
    since_date="2025-01-01"
)

client.metadata.deploy(
    components=changed,
    target_org=production_client
)
```

**3.3 Metadata Templates**
```python
# Template per setup standard
template = MetadataTemplate("enterprise_crm")

template.apply(client, params={
    "company_name": "Acme Corp",
    "industry": "Technology"
})

# Crea automaticamente:
# - Custom objects standard
# - Fields configurati
# - Workflows
# - Validation rules
```

**3.4 Conflict Resolution**
```python
result = client.metadata.deploy(
    source_dir="./metadata",
    on_conflict="prompt"  # ask, overwrite, skip, merge
)
```

---

### FASE 4: Code Components Deployment ⭐ PRIORITÀ BASSA

Deploy di componenti codice.

#### Features

**4.1 Apex Classes & Triggers**
```python
apex_class = ApexClass(
    name="AccountTriggerHandler",
    body="""
    public class AccountTriggerHandler {
        public static void beforeInsert(List<Account> accounts) {
            // Logic here
        }
    }
    """
)

client.metadata.deploy_apex(apex_class, run_tests=True)
```

**4.2 Lightning Components**
```python
lwc = LightningWebComponent(
    name="customerFeedback",
    markup="<template>...</template>",
    javascript="import { LightningElement } from 'lwc';...",
    css=".container { ... }"
)

client.metadata.deploy_lwc(lwc)
```

**Note:** Fase 4 opzionale, priorità bassa rispetto a configurazioni.

---

## 🏗️ Architettura Proposta

### Struttura Moduli

```
kinetic_core/
├── metadata/                   # ⭐ NUOVO MODULO
│   ├── __init__.py
│   ├── client.py              # MetadataClient
│   ├── models.py              # CustomObject, CustomField, etc.
│   ├── xml_builder.py         # Genera XML Salesforce
│   ├── xml_parser.py          # Parse XML responses
│   ├── deployer.py            # Deploy operations
│   ├── retriever.py           # Retrieve operations
│   ├── validator.py           # Validazione componenti
│   ├── comparator.py          # Confronto metadata
│   └── templates/             # Template predefiniti
│       ├── enterprise_crm.py
│       ├── sales_pipeline.py
│       └── customer_support.py
```

### API Design

```python
# kinetic_core/core/client.py (modificato)

class SalesforceClient:
    def __init__(self, session):
        self.session = session
        self._bulk_client = None
        self._metadata_client = None  # ⭐ NUOVO

    @property
    def metadata(self):
        """Access Metadata API operations."""
        if not self._metadata_client:
            from kinetic_core.metadata import MetadataClient
            self._metadata_client = MetadataClient(self.session)
        return self._metadata_client
```

### MetadataClient API

```python
# kinetic_core/metadata/client.py

class MetadataClient:
    """Salesforce Metadata API client."""

    def __init__(self, session: SalesforceSession):
        self.session = session
        self.base_url = f"{session.instance_url}/services/Soap/m/{session.api_version}"

    # RETRIEVE Operations
    def retrieve(
        self,
        component_types: List[str],
        package_names: Optional[List[str]] = None,
        output_dir: str = "./metadata"
    ) -> RetrieveResult:
        """Retrieve metadata components from org."""
        pass

    def describe_metadata(self) -> MetadataDescription:
        """Get list of all metadata types available."""
        pass

    # DEPLOY Operations
    def deploy(
        self,
        source_dir: str,
        run_tests: bool = False,
        rollback_on_error: bool = True,
        check_only: bool = False
    ) -> DeployResult:
        """Deploy metadata package to org."""
        pass

    def deploy_field(
        self,
        field: CustomField,
        check_only: bool = False
    ) -> DeployResult:
        """Deploy single custom field."""
        pass

    def deploy_object(
        self,
        obj: CustomObject,
        check_only: bool = False
    ) -> DeployResult:
        """Deploy custom object with fields."""
        pass

    # COMPARISON Operations
    def compare(
        self,
        source_org: 'SalesforceClient',
        target_org: 'SalesforceClient',
        component_types: List[str]
    ) -> MetadataDiff:
        """Compare metadata between two orgs."""
        pass

    # UTILITY Operations
    def validate_package(self, source_dir: str) -> ValidationResult:
        """Validate metadata package before deployment."""
        pass

    def get_deployment_status(self, async_process_id: str) -> DeployResult:
        """Check status of async deployment."""
        pass
```

---

## 📋 Data Models

### CustomField Model

```python
from dataclasses import dataclass
from typing import Optional, List

@dataclass
class CustomField:
    """Represents a Salesforce custom field."""

    sobject: str                    # Parent object
    name: str                       # API name (e.g., "Phone_Verified__c")
    label: str                      # Display label
    type: str                       # Checkbox, Text, Number, etc.

    # Optional attributes
    length: Optional[int] = None
    precision: Optional[int] = None
    scale: Optional[int] = None
    default_value: Optional[Any] = None
    description: Optional[str] = None
    help_text: Optional[str] = None
    required: bool = False
    unique: bool = False
    external_id: bool = False

    # For formula fields
    formula: Optional[str] = None

    # For lookup/master-detail
    reference_to: Optional[str] = None
    relationship_name: Optional[str] = None

    # Picklist values
    picklist_values: Optional[List[str]] = None

    def to_xml(self) -> str:
        """Convert to Salesforce metadata XML."""
        pass

    @classmethod
    def from_xml(cls, xml: str) -> 'CustomField':
        """Parse from Salesforce metadata XML."""
        pass
```

### CustomObject Model

```python
@dataclass
class CustomObject:
    """Represents a Salesforce custom object."""

    name: str                       # API name (e.g., "Customer_Feedback__c")
    label: str                      # Display label
    plural_label: str               # Plural label

    # Object properties
    description: Optional[str] = None
    deployment_status: str = "Deployed"  # Deployed or InDevelopment
    sharing_model: str = "ReadWrite"     # Private, Read, ReadWrite

    # Features
    enable_activities: bool = False
    enable_reports: bool = True
    enable_search: bool = True
    enable_bulk_api: bool = True
    enable_sharing: bool = True
    enable_streaming_api: bool = True

    # Fields
    fields: List[CustomField] = field(default_factory=list)

    # Name field configuration
    name_field_label: str = "Name"
    name_field_type: str = "Text"  # Text or AutoNumber

    def to_xml(self) -> str:
        """Convert to Salesforce metadata XML."""
        pass

    @classmethod
    def from_xml(cls, xml: str) -> 'CustomObject':
        """Parse from Salesforce metadata XML."""
        pass
```

### Deploy/Retrieve Results

```python
@dataclass
class DeployResult:
    """Result of metadata deployment."""

    id: str                         # Async process ID
    status: str                     # Succeeded, Failed, InProgress
    success: bool
    done: bool

    # Component details
    components_deployed: int = 0
    components_total: int = 0

    # Test results (if run_tests=True)
    tests_run: int = 0
    tests_failed: int = 0

    # Errors
    errors: List[Dict[str, Any]] = field(default_factory=list)

    # Deployment details
    created_date: Optional[datetime] = None
    completed_date: Optional[datetime] = None

    @property
    def is_complete(self) -> bool:
        return self.done

    @property
    def is_successful(self) -> bool:
        return self.success and self.done


@dataclass
class RetrieveResult:
    """Result of metadata retrieval."""

    id: str
    status: str
    success: bool
    done: bool

    # Retrieved files
    zip_file: Optional[bytes] = None
    file_count: int = 0

    # Errors
    errors: List[Dict[str, Any]] = field(default_factory=list)

    def extract_to(self, directory: str) -> None:
        """Extract ZIP to directory."""
        pass
```

---

## 🔄 Integration Flow

### Example 1: Retrieve & Version Control

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Retrieve all custom metadata
result = client.metadata.retrieve(
    component_types=["CustomObject", "CustomField", "ValidationRule"],
    package_names=["*__c"],  # All custom components
    output_dir="./salesforce_metadata"
)

# Result structure:
# ./salesforce_metadata/
# ├── package.xml
# ├── objects/
# │   ├── Account.object
# │   ├── Customer_Feedback__c.object
# │   └── ...
# ├── fields/
# │   ├── Account/Phone_Verified__c.field
# │   └── ...
# └── validationRules/
#     └── ...

# Commit to Git
import subprocess
subprocess.run(["git", "add", "salesforce_metadata/"])
subprocess.run(["git", "commit", "-m", "Backup Salesforce config"])
subprocess.run(["git", "push"])
```

---

### Example 2: Create Custom Field

```python
from kinetic_core.metadata import CustomField

# Define field
field = CustomField(
    sobject="Account",
    name="Phone_Verified__c",
    label="Phone Verified",
    type="Checkbox",
    default_value=False,
    description="Indicates if phone number has been verified by customer",
    help_text="Check this box when customer confirms their phone number"
)

# Deploy (dry-run first)
dry_run_result = client.metadata.deploy_field(field, check_only=True)

if dry_run_result.success:
    print("✓ Validation passed")

    # Deploy for real
    result = client.metadata.deploy_field(field)

    if result.is_successful:
        print(f"✓ Field created: {field.name}")
    else:
        print(f"✗ Errors: {result.errors}")
```

---

### Example 3: Deploy Package Sandbox → Production

```python
# Step 1: Retrieve from Sandbox
sandbox_auth = JWTAuthenticator(
    client_id=SANDBOX_CLIENT_ID,
    username=SANDBOX_USERNAME,
    private_key_path=SANDBOX_KEY_PATH,
    login_url="https://test.salesforce.com"
)
sandbox_session = sandbox_auth.authenticate()
sandbox_client = SalesforceClient(sandbox_session)

sandbox_client.metadata.retrieve(
    component_types=["CustomObject", "CustomField"],
    output_dir="./sandbox_metadata"
)

# Step 2: Deploy to Production
prod_auth = JWTAuthenticator(
    client_id=PROD_CLIENT_ID,
    username=PROD_USERNAME,
    private_key_path=PROD_KEY_PATH,
    login_url="https://login.salesforce.com"
)
prod_session = prod_auth.authenticate()
prod_client = SalesforceClient(prod_session)

# Dry-run first
dry_run = prod_client.metadata.deploy(
    source_dir="./sandbox_metadata",
    run_tests=True,
    check_only=True
)

if dry_run.success:
    print(f"✓ Validation passed, {dry_run.tests_run} tests run")

    # Deploy for real
    result = prod_client.metadata.deploy(
        source_dir="./sandbox_metadata",
        run_tests=True,
        rollback_on_error=True
    )

    if result.is_successful:
        print("✓ Deployment successful")
    else:
        print(f"✗ Deployment failed: {result.errors}")
```

---

## 🧪 Testing Strategy

### Unit Tests

```python
# tests/test_metadata_client.py

def test_custom_field_to_xml():
    field = CustomField(
        sobject="Account",
        name="Test__c",
        label="Test Field",
        type="Text",
        length=255
    )

    xml = field.to_xml()
    assert "<fullName>Test__c</fullName>" in xml
    assert "<label>Test Field</label>" in xml
    assert "<type>Text</type>" in xml
    assert "<length>255</length>" in xml


def test_custom_field_from_xml():
    xml = """
    <CustomField>
        <fullName>Test__c</fullName>
        <label>Test Field</label>
        <type>Text</type>
        <length>255</length>
    </CustomField>
    """

    field = CustomField.from_xml(xml)
    assert field.name == "Test__c"
    assert field.label == "Test Field"
    assert field.type == "Text"
    assert field.length == 255
```

### Integration Tests

```python
# tests/test_metadata_integration.py

@pytest.mark.integration
def test_deploy_custom_field():
    client = create_test_client()

    field = CustomField(
        sobject="Account",
        name=f"Test_{uuid.uuid4().hex[:8]}__c",
        label="Integration Test Field",
        type="Text",
        length=100
    )

    # Deploy
    result = client.metadata.deploy_field(field)
    assert result.is_successful

    # Verify field exists
    metadata = client.describe("Account")
    field_names = [f['name'] for f in metadata['fields']]
    assert field.name in field_names

    # Cleanup
    # TODO: Delete field (future feature)


@pytest.mark.integration
def test_retrieve_metadata():
    client = create_test_client()

    result = client.metadata.retrieve(
        component_types=["CustomObject"],
        package_names=["Account"],
        output_dir=f"./test_metadata_{uuid.uuid4().hex[:8]}"
    )

    assert result.is_successful
    assert result.file_count > 0

    # Cleanup temp directory
```

---

## 📦 Dependencies

### New Dependencies

```python
# requirements.txt additions

# XML processing
lxml>=4.9.0              # XML parsing & generation
xmltodict>=0.13.0        # XML to dict conversion

# ZIP handling
zipfile36>=0.1.3         # Enhanced ZIP support

# Testing
pytest-mock>=3.12.0      # Mocking for tests
```

### Salesforce API Versions

- **Metadata API**: v60.0+
- **SOAP API**: Required for Metadata operations
- **REST API**: Used for describe operations

---

## 📅 Implementation Timeline & Release Process

### Sprint 1 (Week 1-2): Foundation ⭐ FASE 1

**Goals:**
- ✅ Setup metadata module structure
- ✅ Implement data models (CustomField, CustomObject)
- ✅ XML serialization/deserialization
- ✅ Basic retrieve functionality

**Tasks:**
1. Create branch: `git checkout -b feature/metadata-api`
2. Setup module structure:
   - `kinetic_core/metadata/__init__.py`
   - `kinetic_core/metadata/models.py`
   - `kinetic_core/metadata/xml_builder.py`
   - `kinetic_core/metadata/xml_parser.py`
3. Implement CustomField and CustomObject models
4. Implement XML serialization/deserialization
5. Unit tests for models
6. Commit incrementally

**Deliverables:**
- `metadata/` module with structure
- `CustomField` and `CustomObject` models
- `xml_builder.py` and `xml_parser.py`
- `retriever.py` basic implementation
- Unit tests for models (>80% coverage)

**Time Estimate:** 15-20 hours

---

### Sprint 2 (Week 3): Retrieve Operations ⭐ FASE 1

**Goals:**
- ✅ Complete retrieve implementation
- ✅ Package.xml generation
- ✅ ZIP extraction
- ✅ Integration tests

**Tasks:**
1. Implement `MetadataClient.retrieve()`
2. Implement `MetadataClient.describe_metadata()`
3. Package.xml generation logic
4. ZIP file handling
5. Integration tests with Salesforce org
6. Commit changes

**Deliverables:**
- Full `MetadataClient.retrieve()` working
- Support for all Fase 1 component types
- Integration tests with real Salesforce org
- Error handling

**Time Estimate:** 10-15 hours

---

### Sprint 3 (Week 4-5): Deploy Operations ⭐ FASE 2

**Goals:**
- ✅ Implement deploy functionality
- ✅ Validation logic
- ✅ Dry-run mode
- ✅ Rollback mechanism

**Tasks:**
1. Implement `MetadataClient.deploy()`
2. Implement `MetadataClient.deploy_field()`
3. Implement `MetadataClient.deploy_object()`
4. Validation logic (XML schema)
5. Dry-run mode (check_only parameter)
6. Rollback mechanism
7. Integration tests
8. Commit changes

**Deliverables:**
- `deployer.py` complete
- `validator.py` with XML validation
- Deploy single field/object helpers
- Error handling and reporting
- Rollback on error

**Time Estimate:** 20-25 hours

---

### Sprint 4 (Week 6): Advanced Features ⭐ FASE 3

**Goals:**
- ✅ Metadata comparison
- ✅ Selective deployment
- ✅ Templates system

**Tasks:**
1. Implement `MetadataClient.compare()`
2. Implement selective deployment
3. Create template system
4. Add enterprise_crm template
5. Performance optimizations
6. Final integration tests
7. Commit changes

**Deliverables:**
- `comparator.py` with diff logic
- Template examples (enterprise_crm, etc.)
- Performance optimizations
- Complete test suite

**Time Estimate:** 15-20 hours

---

### Sprint 5 (Week 7): Documentation ⭐ CRITICO

**Goals:**
- ✅ Complete MkDocs documentation
- ✅ Update README.md
- ✅ Update CHANGELOG.md
- ✅ Create examples

**Tasks:**
1. **MkDocs Documentation:**
   - Create `docs/api/METADATA_API.md`
   - Create `docs/guides/METADATA_QUICKSTART.md`
   - Create `docs/examples/METADATA_EXAMPLES.md`
   - Update `mkdocs.yml` (add new pages)
   - Test locally: `mkdocs serve`

2. **README.md:**
   - Add Metadata API section
   - Update features list
   - Add usage examples
   - Update installation instructions

3. **CHANGELOG.md:**
   - Add [2.1.0] section
   - List all new features
   - List changes
   - Breaking changes (if any)

4. **Commit Documentation:**
   ```bash
   git commit -m "Add Metadata API documentation"
   ```

**Deliverables:**
- Complete API reference
- User guides
- Examples
- Updated README and CHANGELOG

**Time Estimate:** 8-10 hours

---

### Sprint 6 (Week 8): Pre-Release Preparation ⭐ CRITICO

**Goals:**
- ✅ Merge to main
- ✅ Version bump
- ✅ Build and test package

**Tasks:**

1. **Merge to Main:**
   ```bash
   git checkout main
   git merge feature/metadata-api
   ```

2. **Version Bump:**
   - Update `setup.py`: `version="2.1.0"`
   - Update `kinetic_core/__init__.py`: `__version__ = "2.1.0"`
   - Commit:
     ```bash
     git commit -m "Bump version to v2.1.0"
     ```

3. **Pre-Release Checks:**
   - Run full test suite: `pytest tests/ -v`
   - Code quality: `flake8 kinetic_core/`
   - Test MkDocs locally: `mkdocs serve`
   - Verify README rendering

4. **Build Package:**
   ```bash
   # Clean previous builds
   rm -rf dist/ build/ *.egg-info/

   # Build
   python -m build

   # Verify contents
   tar -tzf dist/kinetic-core-2.1.0.tar.gz
   unzip -l dist/kinetic_core-2.1.0-py3-none-any.whl

   # Verify README in package
   unzip -p dist/kinetic_core-2.1.0-py3-none-any.whl */README.md
   ```

5. **Test Installation in Clean Virtualenv:**
   ```bash
   python -m venv test_env
   source test_env/bin/activate  # Windows: test_env\Scripts\activate
   pip install dist/kinetic_core-2.1.0-py3-none-any.whl

   # Test imports
   python -c "from kinetic_core import SalesforceClient, MetadataClient; print('OK')"
   python -c "from kinetic_core.metadata import CustomField, CustomObject; print('OK')"

   # Deactivate and cleanup
   deactivate
   rm -rf test_env
   ```

**Deliverables:**
- Feature merged to main
- Version bumped
- Package built and tested locally

**Time Estimate:** 3-4 hours

---

### Sprint 7: TestPyPI Validation ⭐ RECOMMENDED

**Goals:**
- ✅ Upload to TestPyPI
- ✅ Verify installation
- ✅ Verify documentation

**Tasks:**

1. **Upload to TestPyPI:**
   ```bash
   twine upload --repository testpypi dist/*
   ```

2. **Verify on TestPyPI:**
   - Visit: https://test.pypi.org/project/kinetic-core/
   - Check version: 2.1.0
   - Check README rendering
   - Check links work

3. **Test Installation from TestPyPI:**
   ```bash
   python -m venv testpypi_env
   source testpypi_env/bin/activate

   pip install --index-url https://test.pypi.org/simple/ kinetic-core==2.1.0

   # Test functionality
   python -c "from kinetic_core import MetadataClient; print('TestPyPI OK')"

   deactivate
   rm -rf testpypi_env
   ```

4. **If Issues Found:**
   - Fix them
   - Rebuild package (Sprint 6, step 4)
   - Re-upload to TestPyPI

**Deliverables:**
- Package verified on TestPyPI
- Installation tested
- Documentation verified

**Time Estimate:** 1-2 hours

---

### Sprint 8: Git Tag & MkDocs Deploy ⭐ CRITICO

**Goals:**
- ✅ Create Git tag
- ✅ Push to GitHub
- ✅ Deploy documentation

**Tasks:**

1. **Create Git Tag:**
   ```bash
   git tag -a v2.1.0 -m "Release v2.1.0 - Metadata API Support"
   ```

2. **Push to GitHub:**
   ```bash
   git push origin main
   git push origin v2.1.0
   ```

3. **Deploy MkDocs:**
   ```bash
   mkdocs gh-deploy
   ```

4. **Verify:**
   - Check tag on GitHub: https://github.com/KineticMCP/kinetic-core/tags
   - Check documentation: https://kineticmcp.com/kinetic-core/

**Deliverables:**
- Git tag created and pushed
- Documentation deployed and live

**Time Estimate:** 30 minutes

---

### Sprint 9: PyPI Production Release ⭐ CRITICO

**Goals:**
- ✅ Upload to production PyPI
- ✅ Verify release
- ✅ Test installation

**Tasks:**

1. **Final Verification:**
   - ✅ Git tag exists on GitHub
   - ✅ MkDocs is live
   - ✅ README verified in dist/
   - ✅ CHANGELOG updated
   - ✅ All tests pass

2. **Upload to Production PyPI:**
   ```bash
   twine upload dist/*
   ```

3. **Verify on PyPI:**
   - Visit: https://pypi.org/project/kinetic-core/
   - Check version: 2.1.0
   - Check README rendering
   - Check "Project Links" work
   - Check classifiers

4. **Test Installation:**
   ```bash
   python -m venv prod_test_env
   source prod_test_env/bin/activate

   pip install kinetic-core==2.1.0

   # Verify version
   python -c "import kinetic_core; print(kinetic_core.__version__)"
   # Output should be: 2.1.0

   # Test imports
   python -c "from kinetic_core import MetadataClient; print('Production PyPI OK')"

   deactivate
   rm -rf prod_test_env
   ```

**Deliverables:**
- Package live on PyPI
- Installation verified
- Version confirmed

**Time Estimate:** 1 hour

---

### Sprint 10: GitHub Release ⭐ CRITICO

**Goals:**
- ✅ Create GitHub Release
- ✅ Add release notes
- ✅ Attach assets

**Tasks:**

1. **Create GitHub Release:**
   - Go to: https://github.com/KineticMCP/kinetic-core/releases/new
   - Select tag: v2.1.0
   - Title: `v2.1.0 - Metadata API Support`

2. **Release Description:**
   ```markdown
   # kinetic-core v2.1.0 - Metadata API Support 🚀

   ## Major New Features

   ### Metadata API Support ⭐
   Native support for Salesforce Metadata API - manage Salesforce configuration as code!

   **New Module:**
   ```python
   from kinetic_core import SalesforceClient

   client = SalesforceClient(session)

   # Retrieve metadata
   result = client.metadata.retrieve(
       component_types=["CustomObject", "CustomField"],
       output_dir="./salesforce_metadata"
   )

   # Deploy metadata
   result = client.metadata.deploy(
       source_dir="./salesforce_metadata",
       run_tests=True
   )

   # Create custom field
   field = CustomField(
       sobject="Account",
       name="Phone_Verified__c",
       type="Checkbox"
   )
   client.metadata.deploy_field(field)
   ```

   **Features:**
   - ✅ Retrieve metadata from org (backup, documentation)
   - ✅ Deploy metadata to org (create fields, objects, validation rules)
   - ✅ Compare metadata between orgs
   - ✅ Configuration as Code with Git versioning
   - ✅ Dry-run mode for safe testing
   - ✅ Automatic rollback on errors

   **Benefits:**
   - 24-36x faster customer provisioning
   - 6-12x faster Sandbox→Production deployments
   - Full audit trail with Git
   - Instant rollback capabilities

   ## What's Changed

   See [CHANGELOG.md](CHANGELOG.md) for complete details.

   ## Installation

   ```bash
   pip install --upgrade kinetic-core
   ```

   ## Documentation

   - [Metadata API Reference](https://kineticmcp.com/kinetic-core/api/METADATA_API/)
   - [Quick Start Guide](https://kineticmcp.com/kinetic-core/guides/METADATA_QUICKSTART/)
   - [Examples](https://kineticmcp.com/kinetic-core/examples/METADATA_EXAMPLES/)

   ## Breaking Changes

   ❌ **NONE** - Fully backward compatible with v2.0.x

   ---

   **Full Changelog**: https://github.com/KineticMCP/kinetic-core/compare/v2.0.1...v2.1.0
   ```

3. **Attach Assets:**
   - Upload: `kinetic-core-2.1.0.tar.gz`
   - Upload: `kinetic_core-2.1.0-py3-none-any.whl`
   - Upload: `CHANGELOG.md`

4. **Publish Release:**
   - Click "Publish release"

**Deliverables:**
- GitHub Release published
- Release notes complete
- Assets attached

**Time Estimate:** 1 hour

---

### Sprint 11: Post-Release Verification & Communication

**Goals:**
- ✅ End-to-end verification
- ✅ Update dependent projects
- ✅ Announce release

**Tasks:**

1. **End-to-End Verification:**
   ```bash
   # Fresh environment
   python -m venv final_test_env
   source final_test_env/bin/activate

   # Install from PyPI
   pip install kinetic-core

   # Should get latest (2.1.0)
   python -c "import kinetic_core; print(kinetic_core.__version__)"

   # Test basic functionality
   python << EOF
   from kinetic_core import JWTAuthenticator, SalesforceClient

   # Authenticate (use your credentials)
   auth = JWTAuthenticator.from_env()
   session = auth.authenticate()
   client = SalesforceClient(session)

   # Test REST API
   result = client.query("SELECT Id FROM Account LIMIT 1")
   print(f"✓ REST API works: {len(result['records'])} records")

   # Test Bulk API
   print("✓ Bulk API client available:", hasattr(client, 'bulk'))

   # Test Metadata API (NEW!)
   print("✓ Metadata API client available:", hasattr(client, 'metadata'))

   # Test metadata describe
   metadata_types = client.metadata.describe_metadata()
   print(f"✓ Metadata API works: {len(metadata_types)} types available")
   EOF

   deactivate
   rm -rf final_test_env
   ```

2. **Update Dependent Projects:**
   - If KineticMCP or other projects depend on kinetic-core
   - Update their `requirements.txt`: `kinetic-core>=2.1.0`
   - Test integration

3. **Announce Release:**
   - **GitHub Discussions:** Post announcement
   - **Twitter/LinkedIn:**
     ```
     🚀 kinetic-core v2.1.0 released!

     New Metadata API support:
     - Manage Salesforce config as code
     - 24-36x faster provisioning
     - Git versioning & rollback

     https://pypi.org/project/kinetic-core/
     https://github.com/KineticMCP/kinetic-core/releases/tag/v2.1.0
     ```
   - **Email Newsletter:** (if you have one)
   - **Slack/Discord:** Community announcement

4. **Monitor for Issues:**
   - Watch GitHub Issues
   - Monitor PyPI download stats
   - Check for error reports

**Deliverables:**
- Complete end-to-end test passed
- Dependent projects updated
- Release announced
- Monitoring in place

**Time Estimate:** 2-3 hours

---

**Total Implementation + Release Time:** 70-90 hours (2-2.5 months part-time)

---

## 🎯 Success Criteria

### Fase 1 Complete When:
- ✅ Can retrieve all custom objects from org
- ✅ Can retrieve all custom fields
- ✅ Output is valid Salesforce package format
- ✅ Can extract to directory with correct structure
- ✅ Integration tests pass

### Fase 2 Complete When:
- ✅ Can deploy custom field to org
- ✅ Can deploy custom object with fields
- ✅ Validation prevents invalid deployments
- ✅ Dry-run mode works correctly
- ✅ Rollback works on error
- ✅ Can deploy between Sandbox and Production

### Fase 3 Complete When:
- ✅ Can compare two orgs
- ✅ Can deploy only changed components
- ✅ Templates work for common scenarios
- ✅ Performance is acceptable (< 5 min for 100 components)

---

## 🚨 Risks & Mitigations

### Risk 1: Salesforce API Complexity

**Risk:** Metadata API è complessa, XML verbose, molte edge cases

**Mitigation:**
- Start with simple components (CustomField)
- Incremental implementation
- Extensive testing with real org
- Follow Salesforce best practices

---

### Risk 2: Breaking Changes

**Risk:** Modifiche metadata possono rompere org production

**Mitigation:**
- ✅ Dry-run mode ALWAYS default
- ✅ Rollback automatico su errore
- ✅ Test execution obbligatoria per Production
- ✅ Backup automatico prima di deploy
- ✅ Validazione rigorosa input

---

### Risk 3: Performance

**Risk:** Retrieve/deploy di 1000+ componenti può essere lento

**Mitigation:**
- Async operations con polling
- Parallel processing dove possibile
- Caching metadata descrittivi
- Progress callbacks per feedback utente

---

### Risk 4: XML Schema Changes

**Risk:** Salesforce può cambiare schema XML tra versioni

**Mitigation:**
- Use Salesforce metadata describe API
- Version-agnostic parsing where possible
- Test against multiple API versions
- Clear error messages for unsupported features

---

## 🔄 KineticMCP Integration

### New MCP Tools

Quando Metadata API sarà in kinetic-core, aggiungeremo questi tools in KineticMCP:

```python
# kineticmcp/src/mcp_salesforce_server/server.py

@mcp.tool()
def sf_create_field(
    ctx: Context,
    sobject: str,
    field_name: str,
    field_type: str,
    label: str,
    **options
) -> Dict[str, Any]:
    """Create custom field on Salesforce object."""
    client = get_client_for_session(ctx)

    field = CustomField(
        sobject=sobject,
        name=field_name,
        label=label,
        type=field_type,
        **options
    )

    result = client.metadata.deploy_field(field)
    return result.to_dict()


@mcp.tool()
def sf_backup_metadata(
    ctx: Context,
    component_types: List[str],
    output_dir: str = "./salesforce_backup"
) -> Dict[str, Any]:
    """Backup Salesforce metadata to local directory."""
    client = get_client_for_session(ctx)

    result = client.metadata.retrieve(
        component_types=component_types,
        output_dir=output_dir
    )

    return {
        "success": result.success,
        "file_count": result.file_count,
        "output_dir": output_dir
    }


@mcp.tool()
def sf_deploy_metadata(
    ctx: Context,
    source_dir: str,
    run_tests: bool = True,
    check_only: bool = False
) -> Dict[str, Any]:
    """Deploy metadata package to Salesforce."""
    client = get_client_for_session(ctx)

    result = client.metadata.deploy(
        source_dir=source_dir,
        run_tests=run_tests,
        check_only=check_only,
        rollback_on_error=True
    )

    return {
        "success": result.success,
        "components_deployed": result.components_deployed,
        "tests_run": result.tests_run,
        "tests_failed": result.tests_failed,
        "errors": result.errors
    }
```

---

## 📚 Documentation Plan

### kinetic-core Docs

Create:
- `docs/api/METADATA_API.md` - Complete API reference
- `docs/guides/METADATA_QUICKSTART.md` - Quick start guide
- `docs/examples/METADATA_EXAMPLES.md` - Practical examples

Update:
- `README.md` - Add Metadata API section
- `CHANGELOG.md` - Document v2.1.0 changes

### KineticMCP Docs

Update when integrated:
- `docs/TOOLS_REFERENCE.md` - Add new metadata tools
- `docs/METADATA_GUIDE.md` - User guide for metadata operations
- `CHANGELOG.md` - New version with metadata support

---

## 🎓 References

- [Salesforce Metadata API Developer Guide](https://developer.salesforce.com/docs/atlas.en-us.api_meta.meta/api_meta/)
- [Metadata Types Reference](https://developer.salesforce.com/docs/atlas.en-us.api_meta.meta/api_meta/meta_types_list.htm)
- [SFDX Project Structure](https://developer.salesforce.com/docs/atlas.en-us.sfdx_dev.meta/sfdx_dev/sfdx_dev_source_file_format.htm)

---

## ✅ Next Steps

1. **Review & Approval**: Review questa roadmap
2. **Prioritization**: Confermare priorità Fase 1-2
3. **Environment Setup**: Configurare Salesforce org per testing
4. **Sprint Kickoff**: Iniziare Sprint 1

---

**Roadmap Prepared By:** Claude Code
**For Project:** kinetic-core v2.1.0
**Date:** 2025-01-02

**Status:** 📋 Ready for Review
