# Metadata API Phase 3 Implementation Plan: Advanced Operations

**Project:** kinetic-core Metadata API Support
**Phase:** 3 (Advanced Operations)
**Target Version:** v2.1.0
**Status:** Planning
**Last Updated:** 2026-01-03

---


# Metadata API Implementation - Remaining Tasks

This document tracks the features and tasks remaining to complete the full vision of the Metadata API support in `kinetic-core`, following the successful implementation of the Core (Phase 1 & 2).

## 🚀 Phase 3: Advanced Operations Extensions
*Enhancements to optimize the deployment workflow.*

- [ ] **Selective Deployment (Smart Delta)**
    - Implement logic to calculate hash/diff of local vs remote files.
    - Deploy *only* components that have changed.
    - Add `force_deploy` flag to override this behavior.
- [ ] **Conflict Resolution Strategies**
    - Implement `on_conflict` parameter in `deploy()`.
    - Support strategies: `overwrite` (default), `skip`, `fail`.
- [ ] **Expanded Template Library**
    - `sales_pipeline`: Standard Opportunity stages and fields.
    - `customer_support`: Case management, queues, and email handlers.
    - `saas_metrics`: Custom objects for MRR/ARR tracking.

## 💻 Phase 4: Code Components Deployment
*Support for deploying programmatic logic, not just declarative XML.*

- [ ] **Apex Support**
    - Handle `.cls` (Class) and `.cls-meta.xml` pairings.
    - Handle `.trigger` (Trigger) and `.trigger-meta.xml` pairings.
    - Enforce test coverage requirements for Production deployments.
- [ ] **Lightning Web Components (LWC)**
    - Support for component bundles (HTML, JS, CSS, XML in one folder).
    - Logic to zip lwc folders correctly for valid deployment.
- [ ] **Static Resources**
    - Support for uploading binary files/archives as Static Resources.

## 📚 Phase 5: Documentation
*Preparing the knowledge base for users.*

- [ ] **MkDocs Integration**
    - Create `docs/api/METADATA_API.md` (Technical Reference).
    - Create `docs/guides/METADATA_QUICKSTART.md` (Tutorial).
- [ ] **Examples**
    - Add `examples/manage_schema.py` to the repo.
- [ ] **Changelog**
    - Update `CHANGELOG.md` with a detailed entry for v2.1.0 features.

## 📦 Phase 6: Release Process (v2.1.0)
*Steps to publish the new version.*

- [ ] **Version Bump**
    - Update `setup.py` and `__init__.py` to `2.1.0`.
- [ ] **Packaging**
    - Run `python -m build` to create sdist and wheel.
    - Verify package contents (ensure new `metadata` module is included).
- [ ] **Distribution**
    - Upload to PyPI (`twine upload`).
    - Create GitHub Release `v2.1.0` with release notes.


## 🎯 Executive Summary

### Obiettivo

Evolvere la Metadata API da uno strumento "funzionale" a uno strumento **"intelligente" ed "Enterprise-ready"**. La Fase 3 si concentra sull'ottimizzazione delle performance (deploy selettivo), sulla sicurezza delle operazioni (conflict resolution) e sull'accelerazione del setup (template avanzati).

### Perché è Importante

**Problema Attuale (Fase 1 & 2):**
- ❌ **Lentezza:** Ogni deploy invia *tutti* i file, anche se non modificati.
- ❌ **Rischi di Sovrascrittura:** Se un campo esiste già in org con tipo diverso, il deploy lo sovrascrive ciecamente.
- ❌ **Setup Manuale:** Creare oggetti complessi (es. Sales Pipeline con 10 stadi) richiede molto codice boilerplate.

**Soluzione Fase 3:**
- ✅ **Smart Delta:** Deploy solo dei componenti modificati (calcolo hash locale vs remoto).
- ✅ **Safety Checks:** Rilevamento conflitti prima del deploy con strategie configurabili.
- ✅ **Business Templates:** Creazione di interi processi di business (es. Support Ticket system) con una riga di codice.

---

## 📊 Scope & Features

### FEATURE 3.1: Selective Deployment (Smart Delta) ⭐ PERFORMANCE

Implementare un meccanismo di calcolo differenziale per ottimizzare i tempi di deploy.

#### Capabilities
- **Hash calculation:** Calcolo MD5 dei file locali.
- **Remote Caching:** Cache dello stato remoto (o retrieve leggero) per confronto.
- **Delta Manifest:** Generazione dinamica di `package.xml` contenente solo i file cambiati.
- **Force Mode:** Flag `force=True` per bypassare il controllo.

**Esempio Utilizzo:**
```python
# Deploy intelligente (default)
# Calcola hash locale, confronta con cache/retrieve, deploya solo delta
result = client.metadata.deploy(
    source_dir="./metadata",
    selective=True  # New parameter
)
print(f"Deployed {result.components_deployed} changed components")

# Force deploy (comportamento attuale)
client.metadata.deploy(source_dir="./metadata", force=True)
```

---

### FEATURE 3.2: Intelligent Conflict Resolution ⭐ SAFETY

Gestire i casi in cui i metadata locali collidono con quelli remoti in modi distruttivi o imprevisti.

#### Capabilities
- **Conflict Detection:** Rilevare se un componente remoto è stato modificato dopo l'ultimo retrieve.
- **Resolution Strategies:**
    - `OVERWRITE`: (Default) Sovrascrive sempre (comportamento attuale).
    - `SKIP`: Se esiste remoto e diverso, non deployare.
    - `FAIL`: Se c'è conflitto, ferma tutto e lancia errore.
    - `MERGE` (Future): Tenta di fondere (es. aggiungere valori picklist senza rimuovere quelli esistenti).

**Esempio Utilizzo:**
```python
from kinetic_core.metadata import ConflictStrategy

# Se il campo esiste già e differisce, salta il deploy per quel campo
client.metadata.deploy_field(
    field,
    on_conflict=ConflictStrategy.SKIP
)

# Se trovi modifiche non trackate in produzione, blocca il deploy
client.metadata.deploy(
    source_dir="./prod_deploy",
    on_conflict=ConflictStrategy.FAIL
)
```

---

### FEATURE 3.3: Advanced Comparison & Reporting ⭐ VISIBILITY

Migliorare la funzione `compare()` per fornire report dettagliati e actionables.

#### Capabilities
- **Deep Diff:** Confronto attributo per attributo (es. "Length changed from 50 to 100").
- **HTML Reporting:** Generazione di report HTML visivi delle differenze.
- **Drift Detection:** Identificare modifiche fatte direttamente in produzione (hotfixes) non riportate nel codice.

**Esempio Utilizzo:**
```python
# Genera report differenze dettagliato
diff = client.metadata.compare(
    output_format="html",
    output_file="deployment_preview.html"
)
# Apre report nel browser
diff.show()
```

---

### FEATURE 3.4: Expanded Template Engine ⭐ PRODUCTIVITY

Estendere il sistema di template per supportare scenari di business complessi e parametrizzazione.

#### Capabilities
- **Multi-Object Templates:** Un template può creare N oggetti, campi, tab e permission sets.
- **Jinja2 Support:** Uso di Jinja2 per parametrizzare nomi, label e valori nei template XML.
- **Library Standard:** Inclusione di template per:
    - *SaaS Metrics* (MRR, Churn, Subscription)
    - *Sales Pipeline* (Lead, Deal, Stage History)
    - *Support Ticketing* (Ticket, SLA, Agent Performance)

**Esempio Utilizzo:**
```python
# Creazione intero modulo "Subscription Management"
client.metadata.create_from_template(
    template="saas_subscriptions",
    params={
        "currency": "EUR",
        "prefix": "SaaS",
        "features": ["churn_tracking", "upsell_alerts"]
    }
)
```

---

## 🏗️ Architettura Proposta

### Nuovi Moduli

```
kinetic_core/metadata/
├── delta/                      # ⭐ NUOVO
│   ├── calculator.py          # Logica hashing e diff
│   ├── cache.py               # Cache stato remoto
│   └── manifest.py            # Generatore package.xml parziale
├── conflict/                   # ⭐ NUOVO
│   ├── detector.py            # Rilevamento conflitti
│   └── resolver.py            # Strategie di risoluzione
└── templates/
    ├── engine.py              # Jinja2 render engine
    └── definitions/           # YAML/JSON definitions
        ├── saas_metrics.yaml
        └── sales_loop.yaml
```

### Class Design Updates

#### `MetadataClient`
Nuovi metodi e firme aggiornate:
```python
def deploy(self, source_dir, selective=False, on_conflict='overwrite', ...)
def preview_deployment(self, source_dir) -> DeploymentPlan
```

#### `DeploymentPlan` (New Class)
Rappresenta cosa *succederà* durante il deploy:
```python
class DeploymentPlan:
    to_create: List[Component]
    to_update: List[Component]
    to_delete: List[Component]
    conflicts: List[Conflict]
    ignored: List[Component]
```

---

## 📅 Implementation Timeline (Sprints)

### Sprint 1: Smart Delta & Hashing (Week 1)
**Focus:** Performance
- [ ] Implementare `FileHasher` utility.
- [ ] Implementare `DeltaCalculator` (Local vs Local cache).
- [ ] Aggiornare `deploy()` per supportare `package.xml` dinamico.
- [ ] Unit tests hashing.

### Sprint 2: Conflict Resolution Framework (Week 2)
**Focus:** Safety
- [ ] Implementare `ConflictDetector`.
- [ ] Aggiornare `deploy_field/object` con paramentro `on_conflict`.
- [ ] Implementare strategie `SKIP` e `FAIL`.
- [ ] Integration tests scenari di conflitto.

### Sprint 3: Advanced Templates (Week 3)
**Focus:** Productivity
- [ ] Integrare `jinja2` nel progetto.
- [ ] Refactoring `xml_builder` per supportare template string.
- [ ] Creare definizioni YAML per `saas_metrics` e `sales_pipeline`.
- [ ] Test end-to-end creazione moduli complessi.

---

## 🚨 Risks & Mitigations

### Risk 1: False Negatives in Delta
**Risk:** Il calcolo hash potrebbe non rilevare una modifica (es. cambio whitespace XML) o rilevarne troppe.
**Mitigation:**
- Normalizzare XML prima dell'hashing (Canonical XML).
- Opzione `force=True` sempre disponibile come "escape hatch".

### Risk 2: Remote State Drift
**Risk:** La cache locale dello stato remoto non è aggiornata, portando a decisioni delta errate.
**Mitigation:**
- Hash basato su `lastModifiedDate` di Salesforce se disponibile (richiede retrieve leggero).
- Avviso all'utente se la cache è più vecchia di X ore.

### Risk 3: Complexity Overload
**Risk:** L'API diventa troppo complessa con troppi flag.
**Mitigation:**
- Mantenere le impostazioni di default "sicure" e "semplici" (Overwirte + Full Deploy).
- Le feature avanzate sono opt-in (`selective=True`).

---

## 🔄 Dependencies

- **Jinja2**: Per il templating engine avanzato.
- **DeepDiff**: (Opzionale) Libreria Python per confronto profondo dizionari/oggetti.

---

**Roadmap Prepared By:** AGENTIC (Kinetic Core Team)
**Date:** 2026-01-03
**Status:** 📋 Proposed
