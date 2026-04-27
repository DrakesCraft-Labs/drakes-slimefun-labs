# Salesforce External App Configuration for Bulk API v2

**Data:** 2025-12-28
**Scope:** Configurazione Connected App per supportare Bulk API v2
**Domanda:** Serve una External App separata per Bulk API?

---

## 🎯 RISPOSTA RAPIDA

### Serve creare una nuova External App?

**❌ NO - Puoi usare la stessa Connected App esistente**

**MA** devi aggiungere:

1. ✅ OAuth Scope "full" (o "web")
2. ✅ User Permission "Bulk API Hard Delete"
3. ✅ User Permission "Modify All Data"
4. ✅ Rigenerare il JWT token dopo le modifiche

---

## 📊 CONFIGURAZIONE ATTUALE vs BULK API v2

### ✅ Configurazione Attuale (REST API funzionante)

**Tua Connected App:**
```
Connected App: Kinetic-Core
├── OAuth Scopes
│   ├── api              ✅ Funziona per REST
│   └── refresh_token    ✅ Funziona per refresh
│
├── JWT Bearer Flow: Enabled
├── Certificate: Installato
└── Permessi Base: API Enabled
```

**Funziona per:**
- ✅ REST API standard
- ✅ Composite API (batch <200)
- ✅ Query/SOQL
- ✅ CRUD operations

**NON funziona per:**
- ❌ Bulk API v2 jobs
- ❌ Upload CSV massivi
- ❌ Processi asincroni bulk

---

## 🚨 MODIFICHE NECESSARIE PER BULK API v2

### 1️⃣ OAuth Scopes (CRITICO!)

#### ❌ Configurazione Attuale (insufficiente)

```yaml
OAuth Scopes:
  - api              # ✅ REST funziona
  - refresh_token    # ✅ Refresh funziona
```

**Problema:** Salesforce restituisce 403 Forbidden su `/jobs/ingest`

#### ✅ Configurazione Corretta (Bulk API v2)

```yaml
OAuth Scopes richiesti:
  - api              # ✅ REST API base
  - refresh_token    # ✅ Token refresh
  - full             # ⭐ OBBLIGATORIO per Bulk API v2
```

**ALTERNATIVA (più granulare):**

```yaml
OAuth Scopes alternativi:
  - api              # ✅ REST API base
  - refresh_token    # ✅ Token refresh
  - web              # ⭐ Include accesso Bulk API
```

### ⚠️ ATTENZIONE: Rigenerare Token!

```bash
# Dopo aver modificato gli scope OAuth nella Connected App:

1. Salva modifiche in Salesforce
2. Attendi 2-10 minuti (propagazione cache Salesforce)
3. Rigenera un nuovo JWT token
4. NON riutilizzare token cached/salvati

# I token esistenti NON acquisiscono automaticamente i nuovi scope!
```

---

## 2️⃣ User Permissions (Profile/Permission Set)

### ✅ Permessi Attuali (REST API)

```yaml
User Permissions (minimo per REST):
  - API Enabled                     ✅ Base
  - View Setup and Configuration    ✅ Setup
```

### ✅ Permessi Aggiuntivi per Bulk API v2

```yaml
User Permissions OBBLIGATORI per Bulk:
  - API Enabled                     ✅ Già presente
  - Bulk API Hard Delete            ⭐ NUOVO - Obbligatorio
  - View All Data                   ⭐ NUOVO - Per query bulk
  - Modify All Data                 ⭐ NUOVO - Per insert/update/delete bulk
```

**Dove configurarli:**

```
Setup → Users → Permission Sets → Create New

Name: Bulk API Access
API Name: Bulk_API_Access

System Permissions:
☑ API Enabled
☑ Bulk API Hard Delete        ⭐ CRITICO
☑ View All Data               ⭐ Per query bulk
☑ Modify All Data             ⭐ Per operazioni bulk

Assigned Users:
→ Aggiungi l'utente JWT/OAuth
```

---

## 3️⃣ Object-Level Security (OLS)

### Per ogni oggetto usato con Bulk API:

```yaml
Setup → Object Manager → Account (esempio)

Object Permissions richiesti:
  - Read         ✅ Per query
  - Create       ✅ Per insert/upsert
  - Edit         ✅ Per update/upsert
  - Delete       ✅ Per delete
  - View All     ⭐ CONSIGLIATO per Bulk
  - Modify All   ⭐ CONSIGLIATO per Bulk
```

**Configurazione tramite Permission Set:**

```
Permission Set → Object Settings → Account

☑ Read
☑ Create
☑ Edit
☑ Delete
☑ View All Records      ⭐ Evita SOQL restrictions
☑ Modify All Records    ⭐ Evita sharing restrictions
```

---

## 🔧 GUIDA STEP-BY-STEP

### Step 1: Modifica Connected App Esistente

```
Setup → App Manager → [Your Connected App] → Edit

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Section: OAuth Policies

Selected OAuth Scopes:
  Disponibili                    Selezionati
  ────────────────              ───────────────────
  Access and manage data (api)  →  [MOVE] →  api ✅
  Perform requests at any time  →  [MOVE] →  refresh_token ✅
  Full access (full)            →  [MOVE] →  full ⭐ NUOVO

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Section: Permitted Users
  ☑ Admin approved users are pre-authorized

Section: IP Relaxation
  Development: ☑ Relax IP restrictions
  Production:  ☑ Enforce IP restrictions (whitelist)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[Save]
```

**⏱ Tempo di propagazione:** 2-10 minuti

---

### Step 2: Crea Permission Set per Bulk API

```
Setup → Permission Sets → New

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Label: Bulk API Access
API Name: Bulk_API_Access
License: --None--

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[Save] → [System Permissions]

System Permissions to Enable:
  ☑ API Enabled
  ☑ Bulk API Hard Delete              ⭐ CRITICO
  ☑ View All Data                     ⭐ Per query bulk
  ☑ Modify All Data                   ⭐ Per operazioni bulk

[Save]

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[Object Settings] → Edit → Account

Account Object Permissions:
  ☑ Read
  ☑ Create
  ☑ Edit
  ☑ Delete
  ☑ View All Records                  ⭐ Bulk queries
  ☑ Modify All Records                ⭐ Bulk operations

[Save]

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

[Manage Assignments] → Add Assignments

Select Users:
  ☑ [Your JWT/OAuth User]

[Assign]
```

---

### Step 3: Rigenera JWT Token

```python
# Attendi 5-10 minuti dopo modifiche scope
# Poi rigenera token con kinetic-core:

from kinetic_core import JWTAuthenticator

auth = JWTAuthenticator.from_env()
session = auth.authenticate()  # Nuovo token con scope "full"

print(f"New token generated: {session.access_token[:30]}...")
print(f"Instance URL: {session.instance_url}")
```

**⚠️ IMPORTANTE:**
- Elimina token cached
- Non riutilizzare vecchi token
- Il nuovo token contiene i nuovi scope

---

### Step 4: Test Bulk API Access

```python
import requests

# Test creazione job Bulk API v2
headers = {
    "Authorization": f"Bearer {session.access_token}",
    "Content-Type": "application/json"
}

url = f"{session.instance_url}/services/data/v62.0/jobs/ingest"

response = requests.post(
    url,
    headers=headers,
    json={
        "object": "Account",
        "operation": "insert",
        "contentType": "CSV"
    }
)

print(f"Status: {response.status_code}")

if response.status_code == 201:
    print("✅ Bulk API v2 WORKS!")
    job = response.json()
    print(f"Job ID: {job['id']}")
    print(f"State: {job['state']}")

    # Cleanup: abort test job
    requests.patch(
        f"{url}/{job['id']}",
        headers=headers,
        json={"state": "Aborted"}
    )
    print("Test job aborted")

elif response.status_code == 403:
    print("❌ FAILED: Missing permissions")
    print("→ Add 'Bulk API Hard Delete' permission")
    print("→ Add 'Modify All Data' permission")

elif response.status_code == 400 and "invalid_grant" in response.text:
    print("❌ FAILED: OAuth scope issue")
    print("→ Add 'full' scope to Connected App")
    print("→ Wait 5-10 minutes")
    print("→ Regenerate JWT token")

else:
    print(f"❌ Error: {response.text}")
```

**Output atteso:**

```
Status: 201
✅ Bulk API v2 WORKS!
Job ID: 750XXXXXXXXXXXXXXX
State: Open
Test job aborted
```

---

## 🔍 TROUBLESHOOTING

### Errore: 403 Forbidden

```json
{
  "errorCode": "INSUFFICIENT_ACCESS",
  "message": "Insufficient privileges"
}
```

**Diagnosi:** Mancano permessi utente

**Soluzione:**
1. ✅ Verifica Permission Set assegnato
2. ✅ Aggiungi "Bulk API Hard Delete"
3. ✅ Aggiungi "Modify All Data"
4. ✅ Verifica Object Permissions (View All, Modify All)

---

### Errore: 400 invalid_grant

```json
{
  "error": "invalid_grant",
  "error_description": "user hasn't approved this consumer"
}
```

**Diagnosi:** Scope OAuth insufficienti o token non aggiornato

**Soluzione:**
1. ✅ Aggiungi scope "full" alla Connected App
2. ✅ Attendi 5-10 minuti (propagazione)
3. ✅ Rigenera nuovo JWT token
4. ✅ NON usare token cached

---

### Errore: 404 Not Found

```json
{
  "errorCode": "NOT_FOUND",
  "message": "The requested resource does not exist"
}
```

**Diagnosi:** Endpoint sbagliato o API version troppo vecchia

**Soluzione:**
1. ✅ Usa API version >= v52.0
2. ✅ Endpoint corretto: `/services/data/v62.0/jobs/ingest`
3. ✅ Non confondere con Bulk API v1 (`/async/`)

---

### Errore: Token funziona per REST ma non per Bulk

**Diagnosi:** Token generato prima di aggiungere scope "full"

**Soluzione:**
```bash
# 1. Verifica scope nella Connected App
Setup → App Manager → [App] → View → OAuth Scopes

# Deve contenere "full" o "web"

# 2. Elimina token cached
rm .token_cache  # o equivalente

# 3. Rigenera token
python -c "from kinetic_core import JWTAuthenticator;
auth = JWTAuthenticator.from_env();
auth.authenticate()"
```

---

## 📋 CHECKLIST COMPLETA

### ✅ Connected App

- [ ] OAuth Scope "api" presente
- [ ] OAuth Scope "refresh_token" presente
- [ ] OAuth Scope "full" o "web" aggiunto ⭐ NUOVO
- [ ] Certificate JWT configurato
- [ ] Permitted Users configurati
- [ ] IP Relaxation configurata

### ✅ Permission Set

- [ ] Permission Set "Bulk API Access" creato
- [ ] System Permission: API Enabled
- [ ] System Permission: Bulk API Hard Delete ⭐ NUOVO
- [ ] System Permission: View All Data ⭐ NUOVO
- [ ] System Permission: Modify All Data ⭐ NUOVO
- [ ] Permission Set assegnato all'utente JWT

### ✅ Object Permissions (per ogni oggetto)

- [ ] Read permission
- [ ] Create permission
- [ ] Edit permission
- [ ] Delete permission
- [ ] View All Records ⭐ CONSIGLIATO
- [ ] Modify All Records ⭐ CONSIGLIATO

### ✅ Testing

- [ ] Atteso 5-10 minuti dopo modifiche
- [ ] Rigenerato JWT token
- [ ] Testato endpoint: `POST /jobs/ingest`
- [ ] Ottenuto risposta 201 Created
- [ ] Verificato Job ID valido

---

## 💡 UNA APP o DUE APP?

### Opzione 1: Una Sola App (✅ CONSIGLIATO)

```yaml
Connected App: Kinetic-Core
  OAuth Scopes:
    - api           # REST API
    - refresh_token # Refresh
    - full          # Bulk API v2

  Permessi Utente:
    - API Enabled
    - Bulk API Hard Delete
    - View All Data
    - Modify All Data
```

**Vantaggi:**
- ✅ Gestione semplificata
- ✅ Un solo token per tutto
- ✅ Meno configurazione
- ✅ Stesso auth flow

**Quando usare:**
- ✅ La maggior parte dei casi
- ✅ Sviluppo e test
- ✅ Applicazioni che usano sia REST che Bulk

---

### Opzione 2: Due App Separate (⚠️ Solo casi specifici)

```yaml
App 1: Kinetic-Core-REST
  OAuth Scopes: [api, refresh_token]
  Uso: CRUD, Query, Composite

App 2: Kinetic-Core-Bulk
  OAuth Scopes: [api, refresh_token, full]
  Uso: Solo Bulk API v2
```

**Quando usare:**
- ⚠️ Policy di sicurezza che separano REST da Bulk
- ⚠️ Audit trail separati richiesti
- ⚠️ Team diversi gestiscono REST e Bulk
- ⚠️ Limiti API da separare

**Svantaggi:**
- ❌ Doppia gestione
- ❌ Due token da gestire
- ❌ Più complessità
- ❌ Più certificati/configurazioni

---

## 🎯 BEST PRACTICE PRODUCTION

### Configurazione Raccomandata (Una App)

```yaml
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Connected App: Kinetic-Core
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

OAuth Configuration:
  Scopes:
    - api              # Base REST API
    - refresh_token    # Token refresh
    - full             # Bulk API v2 + advanced features

  Permitted Users:
    - Admin approved users are pre-authorized

  IP Restrictions:
    Development: Relaxed
    Production: Enforced (whitelist IP ranges)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Permission Set: Kinetic Core API Access
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

System Permissions:
  ✅ API Enabled
  ✅ Bulk API Hard Delete
  ✅ View All Data
  ✅ Modify All Data

Object Permissions (Account, Contact, etc):
  ✅ Read
  ✅ Create
  ✅ Edit
  ✅ Delete
  ✅ View All Records
  ✅ Modify All Records

Assigned To:
  - JWT Service User (lantoniotrento343@agentforce.com)
```

---

## 🧪 SCRIPT DI VERIFICA AUTOMATICA

Salva come: `tests/verify_bulk_config.py`

```python
#!/usr/bin/env python3
"""
Verify Salesforce configuration for Bulk API v2.

Tests:
1. JWT Authentication
2. Bulk API endpoint access
3. Job creation
4. Permissions check
"""

import requests
import sys
from kinetic_core import JWTAuthenticator


def test_authentication():
    """Test JWT authentication."""
    print("\n1️⃣ Testing JWT Authentication...")
    try:
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()
        print(f"   ✅ Authenticated: {session.instance_url}")
        print(f"   ✅ User: {auth.username}")
        print(f"   ✅ API Version: {session.api_version}")
        return session
    except Exception as e:
        print(f"   ❌ FAILED: {e}")
        return None


def test_bulk_api_access(session):
    """Test Bulk API v2 endpoint access."""
    print("\n2️⃣ Testing Bulk API v2 Access...")

    headers = {
        "Authorization": f"Bearer {session.access_token}",
        "Content-Type": "application/json"
    }

    url = f"{session.instance_url}/services/data/{session.api_version}/jobs/ingest"

    # Create test job
    response = requests.post(
        url,
        headers=headers,
        json={
            "object": "Account",
            "operation": "insert",
            "contentType": "CSV"
        }
    )

    if response.status_code == 201:
        job = response.json()
        print(f"   ✅ Bulk API v2 accessible")
        print(f"   ✅ Job created: {job['id']}")
        print(f"   ✅ State: {job['state']}")

        # Cleanup: abort test job
        requests.patch(
            f"{url}/{job['id']}",
            headers=headers,
            json={"state": "Aborted"}
        )
        print(f"   ✅ Test job aborted")
        return True

    else:
        print(f"   ❌ FAILED: HTTP {response.status_code}")
        print(f"   Error: {response.text[:200]}")

        # Diagnose
        if response.status_code == 403:
            print("\n   💡 Missing permissions:")
            print("   → Add 'Bulk API Hard Delete' to Permission Set")
            print("   → Add 'Modify All Data' to Permission Set")
            print("   → Verify Permission Set is assigned to user")

        elif "invalid_grant" in response.text:
            print("\n   💡 OAuth scope issue:")
            print("   → Add 'full' scope to Connected App")
            print("   → Wait 5-10 minutes for propagation")
            print("   → Regenerate JWT token")

        elif response.status_code == 404:
            print("\n   💡 Endpoint issue:")
            print("   → Check API version >= v52.0")
            print("   → Verify endpoint: /jobs/ingest")

        return False


def test_permissions(session):
    """Test user permissions."""
    print("\n3️⃣ Testing User Permissions...")

    # This is informational - we can't directly query permissions via API
    # but we infer from Bulk API access test

    print("   ℹ️  Permission verification via Bulk API test")
    print("   ℹ️  If Bulk API works, permissions are correct")
    return True


def main():
    """Run all verification tests."""
    print("=" * 70)
    print("  Salesforce Bulk API v2 Configuration Verification")
    print("=" * 70)

    # Test 1: Authentication
    session = test_authentication()
    if not session:
        print("\n❌ Authentication failed. Fix .env configuration.")
        return 1

    # Test 2: Bulk API Access
    bulk_works = test_bulk_api_access(session)
    if not bulk_works:
        print("\n❌ Bulk API not accessible. Check configuration above.")
        return 1

    # Test 3: Permissions
    test_permissions(session)

    # Summary
    print("\n" + "=" * 70)
    print("  ✅ ALL TESTS PASSED")
    print("=" * 70)
    print("\n  Salesforce is correctly configured for Bulk API v2!")
    print("\n  Connected App Scopes: ✅")
    print("  User Permissions: ✅")
    print("  Bulk API Access: ✅")
    print("\n  You can now implement Bulk API v2 in kinetic-core.")
    print("=" * 70)

    return 0


if __name__ == "__main__":
    sys.exit(main())
```

**Esegui:**

```bash
python tests/verify_bulk_config.py
```

**Output atteso (successo):**

```
======================================================================
  Salesforce Bulk API v2 Configuration Verification
======================================================================

1️⃣ Testing JWT Authentication...
   ✅ Authenticated: https://your-instance.salesforce.com
   ✅ User: your-user@example.com
   ✅ API Version: v62.0

2️⃣ Testing Bulk API v2 Access...
   ✅ Bulk API v2 accessible
   ✅ Job created: 750XXXXXXXXXXXXXXX
   ✅ State: Open
   ✅ Test job aborted

3️⃣ Testing User Permissions...
   ℹ️  Permission verification via Bulk API test
   ℹ️  If Bulk API works, permissions are correct

======================================================================
  ✅ ALL TESTS PASSED
======================================================================

  Salesforce is correctly configured for Bulk API v2!

  Connected App Scopes: ✅
  User Permissions: ✅
  Bulk API Access: ✅

  You can now implement Bulk API v2 in kinetic-core.
======================================================================
```

---

## 📊 CONFIGURAZIONE FINALE RIASSUNTA

### Prima (REST API only)

```yaml
Connected App OAuth Scopes:
  - api
  - refresh_token

User Permissions:
  - API Enabled

Funziona:
  ✅ REST API
  ✅ Composite API (<200 records)
  ❌ Bulk API v2
```

### Dopo (REST + Bulk API v2)

```yaml
Connected App OAuth Scopes:
  - api
  - refresh_token
  - full              ⭐ AGGIUNTO

User Permissions:
  - API Enabled
  - Bulk API Hard Delete        ⭐ AGGIUNTO
  - View All Data               ⭐ AGGIUNTO
  - Modify All Data             ⭐ AGGIUNTO

Funziona:
  ✅ REST API
  ✅ Composite API
  ✅ Bulk API v2     ⭐ NUOVO
```

---

## ✅ CONCLUSIONE

### Risposta alla Domanda Originale

**Serve una External App separata per Bulk API v2?**

**❌ NO - Una sola Connected App è sufficiente e consigliata**

### Cosa Serve Fare

1. ✅ Aggiungere scope OAuth "full" alla Connected App esistente
2. ✅ Creare Permission Set con permessi Bulk API
3. ✅ Assegnare Permission Set all'utente JWT
4. ✅ Rigenerare JWT token (IMPORTANTE!)
5. ✅ Testare con script di verifica

### Tempo Richiesto

- Modifica Connected App: 5 minuti
- Creazione Permission Set: 10 minuti
- Propagazione Salesforce: 5-10 minuti
- Test e verifica: 5 minuti

**Totale:** ~30 minuti

### Benefici

- ✅ Un solo token per REST e Bulk
- ✅ Configurazione più semplice
- ✅ Meno overhead di gestione
- ✅ Performance 20-50x migliori su grandi volumi

---

**Documento creato:** 2025-12-28
**Configurazione testata:** Salesforce Developer Edition
**Kinetic Core version:** 1.1.0
**Status:** ✅ Pronto per implementazione Bulk API v2





bene ora rileggi l'intero workspace

oltre kinetic-core ho aggiunto kinetincmcp

praticamente l'aggiornamento di Kinetic-Core serve per essere integrato in kineticmcp

quindi leggi la codebase di kineticmcp e dimmi gli impatti generati da questo upgrade

e fammi capire quanti e quali step di implementazione dovremo compiere per avere l'aggiornamento bulk api v2 completo anzi direi tutte le funzionalità possibili per connttere il server mcp a salesforce