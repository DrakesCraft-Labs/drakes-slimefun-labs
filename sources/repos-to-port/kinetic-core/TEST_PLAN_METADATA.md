# Piano di Test - Metadata API

Test completo delle funzionalità Metadata API su Salesforce reale.

## Prerequisiti

### 1. Configurazione Salesforce

**Connected App con permessi estesi:**
- Scope OAuth: `api`, `full` (per Metadata API)
- Oppure: User permission "Customize Application"

**Verifica permessi:**
```bash
# Testa se hai accesso Metadata API
python -c "
from kinetic_core import JWTAuthenticator, SalesforceClient
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)
types = client.metadata.describe_metadata()
print(f'✓ Metadata API accessibile: {len(types[\"metadataObjects\"])} tipi disponibili')
"
```

### 2. File .env

```env
SF_USERNAME=your.email@company.com
SF_CLIENT_ID=your_connected_app_client_id
SF_PRIVATE_KEY=-----BEGIN PRIVATE KEY-----
...your private key...
-----END PRIVATE KEY-----
```

## Esecuzione Test

```bash
cd /c/Users/hp/Documents/GitHub/kinetic-core
python test_metadata_real.py
```

---

## Test 1: Autenticazione e Connessione

### Cosa testa
- Autenticazione JWT
- Creazione SalesforceClient
- Accesso al MetadataClient

### Codice coinvolto
- `kinetic_core/auth/jwt_auth.py` - JWTAuthenticator
- `kinetic_core/core/client.py` - SalesforceClient (proprietà `.metadata`)
- `kinetic_core/metadata/client.py` - MetadataClient

### Output atteso
```
✓ Autenticato su: https://your-instance.salesforce.com
✓ API Version: 60.0
✓ Organization ID: 00D...
✓ MetadataClient accessibile via client.metadata
```

### Cosa verifica
- JWT authentication funziona
- Session è valida
- MetadataClient è inizializzato correttamente

---

## Test 2: Describe Metadata

### Cosa testa
- Chiamata SOAP `describeMetadata()`
- Parsing risposta SOAP
- Lista tipi metadata disponibili

### Codice coinvolto
- `kinetic_core/metadata/soap_client.py` - MetadataSOAPClient.describe_metadata()
- `kinetic_core/metadata/client.py` - MetadataClient.describe_metadata()

### Output atteso
```
✓ Trovati ~100+ tipi metadata
ℹ Organization Namespace: (none)
ℹ Partial Save Allowed: True
ℹ Test Required: False

Primi 10 tipi metadata disponibili:
  1. CustomObject (objects)
  2. CustomField (fields)
  3. ValidationRule (validationRules)
  ...
```

### Cosa verifica
- SOAP client funziona
- Parsing XML risposta corretto
- Tipi standard presenti (CustomObject, CustomField, etc.)

---

## Test 3: Create Custom Field

### Cosa testa
- Creazione CustomField model
- Serializzazione a XML
- Deploy singolo campo
- Validazione Salesforce

### Codice coinvolto
- `kinetic_core/metadata/models.py` - CustomField model
- `kinetic_core/metadata/xml_builder.py` - custom_field_to_xml()
- `kinetic_core/metadata/client.py` - deploy_field()
- `kinetic_core/metadata/soap_client.py` - deploy() + wait_for_deploy()

### Campo creato
```python
Account.Test_Field_KineticCore__c
- Type: Text(100)
- Label: "Test Field (Kinetic Core)"
- Description: "Campo di test creato da kinetic-core Metadata API"
```

### Output atteso
```
✓ Campo Test_Field_KineticCore__c creato con successo!
✓ Deploy ID: 0Af...
✓ Status: Succeeded
✓ Componenti creati: 1
```

### VERIFICA SU SALESFORCE

**Dove controllare:**
1. Setup → Object Manager → Account
2. Clicca "Fields & Relationships"
3. Cerca `Test_Field_KineticCore__c`
4. Verifica:
   - ✓ Field Type: Text(100)
   - ✓ Label: "Test Field (Kinetic Core)"
   - ✓ Description popolata
   - ✓ Help Text presente

**Screenshot da prendere:**
- Field detail page su Salesforce

---

## Test 4: Deploy Custom Object

### Cosa testa
- Creazione CustomObject con campi
- Serializzazione oggetto completo
- Deploy con fields + validation
- Package.xml generation

### Codice coinvolto
- `kinetic_core/metadata/models.py` - CustomObject, PicklistValue
- `kinetic_core/metadata/xml_builder.py` - custom_object_to_xml()
- `kinetic_core/metadata/client.py` - deploy_object()
- `kinetic_core/metadata/soap_client.py` - deploy() con ZIP

### Oggetto creato
```
Test_Object_KC__c
├── Status__c (Picklist: New, In Progress, Completed)
├── Priority__c (Number 3,0)
└── Notes__c (Long Text Area 5000)
```

### Output atteso
```
✓ Oggetto Test_Object_KC__c creato con successo!
✓ Deploy ID: 0Af...
✓ Status: Succeeded
✓ Componenti creati: 4 (1 object + 3 fields)
```

### VERIFICA SU SALESFORCE

**Dove controllare:**
1. Setup → Object Manager
2. Cerca `Test Object KC`
3. Verifica:
   - ✓ Oggetto presente
   - ✓ Label: "Test Object KC"
   - ✓ Plural: "Test Objects KC"
   - ✓ 3 campi custom presenti
4. Clicca sul campo `Status__c`
5. Verifica picklist values:
   - ✓ New (default)
   - ✓ In Progress
   - ✓ Completed

**Screenshot da prendere:**
- Object Manager listing
- Object detail page
- Status__c field detail (picklist values)

**Test funzionale:**
- Crea un record di Test_Object_KC__c dall'UI
- Verifica che lo Status di default sia "New"

---

## Test 5: Retrieve Metadata

### Cosa testa
- Generazione package.xml
- Chiamata SOAP retrieve()
- Polling status asincrono
- Download e estrazione ZIP
- Parsing file metadata

### Codice coinvolto
- `kinetic_core/metadata/client.py` - retrieve(), _create_retrieve_package()
- `kinetic_core/metadata/soap_client.py` - retrieve(), check_retrieve_status(), wait_for_retrieve()
- `kinetic_core/metadata/xml_builder.py` - create_package_xml()

### Metadata scaricato
```
metadata_retrieved/
├── package.xml
├── unpackaged/
│   └── objects/
│       └── Account.object-meta.xml
```

### Output atteso
```
✓ Metadata retrieve completato!
✓ Retrieve ID: 09S...
✓ Status: Succeeded
✓ File scaricati: 1+

File scaricati:
  - unpackaged/objects/Account.object-meta.xml (CustomObject)

✓ Trovati X file XML in metadata_retrieved/
```

### VERIFICA LOCALE

**Dove controllare:**
1. Apri la cartella `metadata_retrieved/`
2. Verifica presenza file:
   ```
   metadata_retrieved/
   ├── package.xml
   └── unpackaged/
       └── objects/
           └── Account.object-meta.xml
   ```
3. Apri `Account.object-meta.xml`
4. Cerca `<fullName>Test_Field_KineticCore__c</fullName>`
5. Verifica che il campo creato nel Test 3 sia presente nel file

**Screenshot da prendere:**
- File explorer mostrando metadata_retrieved/
- Contenuto Account.object-meta.xml con il campo evidenziato

---

## Test 6: Compare Metadata

### Cosa testa
- Confronto directory locali
- Confronto locale vs org
- Retrieve temporaneo per confronto
- MetadataComparator e MetadataDiff

### Codice coinvolto
- `kinetic_core/metadata/comparator.py` - MetadataComparator.compare_directories()
- `kinetic_core/metadata/client.py` - compare()
- `kinetic_core/metadata/models.py` - MetadataDiff

### Output atteso
```
✓ Confronto completato!

Risultati:
  - Aggiunti: X
  - Modificati: Y
  - Eliminati: Z
  - Invariati: N

Componenti modificati:
  • CustomObject:Account (CustomObject)
```

### VERIFICA

Il confronto dovrebbe mostrare:
- **Modificati**: Account (perché abbiamo aggiunto Test_Field_KineticCore__c)
- **Aggiunti**: Test_Object_KC__c (se non era nel retrieve iniziale)

**Test manuale:**
1. Modifica qualcosa nell'org (es. cambia description di un campo)
2. Riesegui il compare
3. Verifica che rilevi la modifica

---

## Test 7: Templates

### Cosa testa
- Template system
- Pre-built templates
- Template registry
- Field generation

### Codice coinvolto
- `kinetic_core/metadata/templates.py` - tutti i template
- `kinetic_core/metadata/__init__.py` - export templates

### Templates disponibili
1. **enterprise_crm**: 6 campi per Account management
   - Customer_Tier__c (Picklist)
   - Annual_Revenue_Verified__c (Currency)
   - Last_Contact_Date__c (Date)
   - Primary_Contact__c (Lookup → Contact)
   - Health_Score__c (Number)
   - Internal_Notes__c (Long Text Area)

2. **support_case**: Oggetto Support_Case__c completo
3. **product_catalog**: 5 campi per Product2
4. **audit_trail**: 5 campi per sync tracking

### Output atteso
```
Lista template disponibili:
  1. enterprise_crm: Enterprise CRM Fields
  2. support_case: Support Case Object
  3. product_catalog: Product Catalog Fields
  4. audit_trail: Audit Trail Fields

✓ Template restituisce 6 campi

Campi inclusi nel template:
  • Customer_Tier__c - Customer Tier (Picklist)
  • Annual_Revenue_Verified__c - Annual Revenue (Verified) (Currency)
  • Last_Contact_Date__c - Last Contact Date (Date)
  • Primary_Contact__c - Primary Contact (Lookup)
  • Health_Score__c - Account Health Score (Number)
  • Internal_Notes__c - Internal Notes (LongTextArea)
```

### VERIFICA (opzionale - non deploy automatico)

**Deploy manuale template:**
```python
from kinetic_core.metadata import templates

# Get template
fields = templates.get_template("enterprise_crm", sobject="Account")

# Deploy each field
for field in fields:
    result = client.metadata.deploy_field(field)
    print(f"Deployed: {field.name} - {result.success}")
```

**Verifica su Salesforce:**
1. Setup → Object Manager → Account → Fields
2. Cerca i 6 campi del template
3. Verifica Customer_Tier__c (picklist con Bronze/Silver/Gold/Platinum)

---

## Modifiche nei File

### File Creati (Sprint 1-4)

#### Sprint 1 - Foundation
```
kinetic_core/metadata/
├── __init__.py          # Module exports
├── models.py            # CustomField, CustomObject, etc. (400+ lines)
├── xml_builder.py       # XML serialization (300+ lines)
├── xml_parser.py        # XML deserialization (300+ lines)
└── client.py            # MetadataClient (300+ lines - base)

tests/metadata/
└── test_models.py       # Unit tests (450+ lines)
```

#### Sprint 2 - Retrieve & Deploy
```
kinetic_core/metadata/
└── soap_client.py       # SOAP operations (500+ lines)

Modifiche:
- client.py: Added retrieve(), deploy(), check_*_status() methods
```

#### Sprint 3-4 - Advanced Features
```
kinetic_core/metadata/
├── comparator.py        # Metadata comparison (400+ lines)
└── templates.py         # Pre-built templates (400+ lines)

Modifiche:
- client.py: Added compare() method
- __init__.py: Export comparator and templates
```

### File Modificati (Core Integration)

```
kinetic_core/
├── __init__.py          # Added MetadataClient, CustomField, CustomObject exports
└── core/
    └── client.py        # Added .metadata property (lazy loading)
```

### Totale Codice Scritto

- **~4,000 linee** di codice produzione
- **~450 linee** di unit tests
- **~600 linee** di documentation/examples

---

## Cleanup dopo i Test

### Rimuovi componenti di test

**Via UI Salesforce:**
1. Setup → Object Manager → Test Object KC → Delete
2. Setup → Object Manager → Account → Fields → Test_Field_KineticCore__c → Delete
3. Conferma le eliminazioni

**Via Metadata API (avanzato):**
```python
# TODO: Implement delete in future sprint
# result = client.metadata.delete_field("Account", "Test_Field_KineticCore__c")
```

### Rimuovi file locali

```bash
rm -rf metadata_retrieved/
rm test_metadata_real.py  # Opzionale
```

---

## Troubleshooting

### Errore: "Insufficient Privileges"

**Causa:** Connected App non ha permessi Metadata API

**Soluzione:**
1. Setup → Apps → App Manager → [Your Connected App] → Edit
2. OAuth Scopes: Aggiungi `full` (Full access)
3. Oppure: Setup → Users → [Your User] → Permission Sets → Assign "Customize Application"

### Errore: "INVALID_SESSION_ID"

**Causa:** Session scaduta o credenziali errate

**Soluzione:**
1. Verifica `.env` file
2. Controlla private key formato (con -----BEGIN/END-----)
3. Rigenera session: `auth.authenticate()`

### Errore: "This field is defined as Deprecated"

**Causa:** Campo già esiste ed è deprecato

**Soluzione:**
1. Usa un nome diverso per il campo di test
2. Oppure elimina il campo esistente prima

### Deploy timeout

**Causa:** Salesforce è lento o org ha molte customizations

**Soluzione:**
```python
# Aumenta timeout
result = client.metadata.deploy_field(field, timeout=300)  # 5 minuti
```

---

## Success Criteria

Tutti i test devono:
- ✓ Completarsi senza errori
- ✓ Mostrare `result.success = True`
- ✓ Creare componenti visibili su Salesforce
- ✓ Scaricare metadata localmente
- ✓ Rilevare differenze nel compare

Se tutti i test passano:
- **Metadata API implementation is READY for release! 🚀**
