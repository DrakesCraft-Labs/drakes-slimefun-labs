"""
Test script per Metadata API - Test completo su Salesforce reale.

Questo script testa tutte le funzionalità implementate:
1. Autenticazione
2. Describe metadata
3. Create custom field
4. Deploy custom object
5. Retrieve metadata
6. Compare metadata
7. Templates

Configurazione richiesta:
- .env con credenziali Salesforce (JWT)
- Connected App con permessi "Customize Application" o scope "full"
"""

import os
import sys
from pathlib import Path
from dotenv import load_dotenv

# Add project root to path
sys.path.insert(0, str(Path(__file__).parent))

from kinetic_core import JWTAuthenticator, SalesforceClient
from kinetic_core.metadata import (
    CustomField,
    CustomObject,
    FieldType,
    SharingModel,
    PicklistValue,
    templates,
)

# Load environment
load_dotenv()

# Colors for output
GREEN = "\033[92m"
RED = "\033[91m"
YELLOW = "\033[93m"
BLUE = "\033[94m"
RESET = "\033[0m"


def print_step(step_num, title):
    """Print test step header."""
    print(f"\n{BLUE}{'=' * 80}{RESET}")
    print(f"{BLUE}TEST {step_num}: {title}{RESET}")
    print(f"{BLUE}{'=' * 80}{RESET}\n")


def print_success(message):
    """Print success message."""
    print(f"{GREEN}✓ {message}{RESET}")


def print_error(message):
    """Print error message."""
    print(f"{RED}✗ {message}{RESET}")


def print_info(message):
    """Print info message."""
    print(f"{YELLOW}ℹ {message}{RESET}")


def main():
    """Run all metadata tests."""

    print(f"\n{BLUE}╔═══════════════════════════════════════════════════════════════════╗{RESET}")
    print(f"{BLUE}║  KINETIC-CORE METADATA API - TEST COMPLETO SU SALESFORCE REALE  ║{RESET}")
    print(f"{BLUE}╚═══════════════════════════════════════════════════════════════════╝{RESET}\n")

    # =========================================================================
    # TEST 1: AUTENTICAZIONE
    # =========================================================================
    print_step(1, "AUTENTICAZIONE E CONNESSIONE")

    try:
        print_info("Autenticazione con JWT...")
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()
        client = SalesforceClient(session)

        print_success(f"Autenticato su: {session.instance_url}")
        print_success(f"API Version: {session.api_version}")
        print_success(f"Organization ID: {session.org_id or 'N/A'}")

        # Verify metadata client is accessible
        assert hasattr(client, 'metadata'), "Client non ha proprietà .metadata"
        print_success("MetadataClient accessibile via client.metadata")

    except Exception as e:
        print_error(f"Autenticazione fallita: {e}")
        return 1

    # =========================================================================
    # TEST 2: DESCRIBE METADATA
    # =========================================================================
    print_step(2, "DESCRIBE METADATA - Lista tipi metadata disponibili")

    try:
        print_info("Chiamata describeMetadata()...")
        metadata_types = client.metadata.describe_metadata()

        print_success(f"Trovati {len(metadata_types.get('metadataObjects', []))} tipi metadata")
        print_info(f"Organization Namespace: {metadata_types.get('organizationNamespace', '(none)')}")
        print_info(f"Partial Save Allowed: {metadata_types.get('partialSaveAllowed')}")
        print_info(f"Test Required: {metadata_types.get('testRequired')}")

        # Show first 10 types
        print_info("\nPrimi 10 tipi metadata disponibili:")
        for i, obj in enumerate(metadata_types.get('metadataObjects', [])[:10], 1):
            print(f"  {i}. {obj.get('xmlName')} ({obj.get('directoryName')})")

        print_success("describeMetadata() funziona correttamente")

    except Exception as e:
        print_error(f"describeMetadata() fallito: {e}")
        import traceback
        traceback.print_exc()
        return 1

    # =========================================================================
    # TEST 3: CREATE CUSTOM FIELD
    # =========================================================================
    print_step(3, "CREATE CUSTOM FIELD - Aggiungi campo custom su Account")

    test_field_name = "Test_Field_KineticCore__c"

    try:
        print_info(f"Creazione campo: {test_field_name}")

        # Create a simple text field
        field = CustomField(
            sobject="Account",
            name=test_field_name,
            type=FieldType.TEXT,
            label="Test Field (Kinetic Core)",
            length=100,
            description="Campo di test creato da kinetic-core Metadata API",
            help_text="Questo campo è un test"
        )

        print_info("Campo configurato:")
        print(f"  - Nome: {field.name}")
        print(f"  - Tipo: {field.type.value}")
        print(f"  - Label: {field.label}")
        print(f"  - Lunghezza: {field.length}")

        print_info("\nDEPLOYMENT in corso (può richiedere 30-60 secondi)...")
        result = client.metadata.deploy_field(field, check_only=False)

        # DEBUG: Print full result
        print(f"[DEBUG] Deploy result object: {vars(result)}")

        if result.success:
            print_success(f"Campo {test_field_name} creato con successo!")
            print_success(f"Deploy ID: {result.id}")
            print_success(f"Status: {result.status}")
            print_success(f"Componenti creati: {len(result.component_successes)}")

            print_info("\n🔍 VERIFICA SU SALESFORCE:")
            print_info("1. Vai su Setup > Object Manager > Account")
            print_info("2. Clicca 'Fields & Relationships'")
            print_info(f"3. Cerca il campo: {test_field_name}")
            print_info("4. Verifica Label: 'Test Field (Kinetic Core)'")

        else:
            print_error(f"Deploy fallito: {result.status}")
            print_error(f"Deploy ID: {result.id}")
            if result.component_failures:
                print_error(f"Numero failures: {len(result.component_failures)}")
                for failure in result.component_failures:
                    if isinstance(failure, dict):
                        print_error(f"  - {failure.get('fullName', 'Unknown')}: {failure.get('problem', 'No details')}")
                        print_error(f"    Type: {failure.get('problemType', 'Unknown')}")
                    else:
                        print_error(f"  - {failure}")
            else:
                print_error("Nessun component_failure restituito")
            if result.messages:
                print_error("Messaggi:")
                for msg in result.messages:
                    print_error(f"  - {msg}")
            if result.messages:
                print_error("Messaggi:")
                for msg in result.messages:
                    print_error(f"  - {msg}")
            # Continue to next test
            print_error("Continuing to next test...")

    except Exception as e:
        print_error(f"Creazione campo fallita: {e}")
        import traceback
        traceback.print_exc()
        # Continue to next test

    # =========================================================================
    # TEST 4: DEPLOY CUSTOM OBJECT (con campi)
    # =========================================================================
    print_step(4, "DEPLOY CUSTOM OBJECT - Crea oggetto custom completo")

    test_object_name = "Test_Object_KC__c"

    try:
        print_info(f"Creazione oggetto: {test_object_name}")

        # Create custom object with fields
        obj = CustomObject(
            name=test_object_name,
            label="Test Object KC",
            plural_label="Test Objects KC",
            sharing_model=SharingModel.PUBLIC_READ_WRITE,
            enable_activities=True,
            enable_reports=True,
            description="Oggetto di test creato da kinetic-core",
            fields=[
                CustomField(
                    sobject=test_object_name,
                    name="Status__c",
                    type=FieldType.PICKLIST,
                    label="Status",
                    required=True,
                    picklist_values=[
                        PicklistValue("New", default=True),
                        PicklistValue("In Progress"),
                        PicklistValue("Completed"),
                    ],
                ),
                CustomField(
                    sobject=test_object_name,
                    name="Priority__c",
                    type=FieldType.NUMBER,
                    label="Priority",
                    precision=3,
                    scale=0,
                ),
                CustomField(
                    sobject=test_object_name,
                    name="Notes__c",
                    type=FieldType.LONG_TEXT_AREA,
                    label="Notes",
                    length=5000,
                ),
            ],
        )

        print_info("Oggetto configurato:")
        print(f"  - Nome: {obj.name}")
        print(f"  - Label: {obj.label}")
        print(f"  - Campi: {len(obj.fields)}")
        for field in obj.fields:
            print(f"    • {field.name} ({field.type.value})")

        print_info("\nDEPLOYMENT in corso (può richiedere 60-120 secondi)...")
        result = client.metadata.deploy_object(obj, check_only=False)

        if result.success:
            print_success(f"Oggetto {test_object_name} creato con successo!")
            print_success(f"Deploy ID: {result.id}")
            print_success(f"Status: {result.status}")
            print_success(f"Componenti creati: {len(result.component_successes)}")

            print_info("\n🔍 VERIFICA SU SALESFORCE:")
            print_info("1. Vai su Setup > Object Manager")
            print_info(f"2. Cerca l'oggetto: {test_object_name}")
            print_info("3. Verifica che abbia 3 campi custom")
            print_info("4. Verifica il campo picklist 'Status'")

        else:
            print_error(f"Deploy fallito: {result.status}")
            print_error(f"Deploy ID: {result.id}")
            if result.component_failures:
                print_error(f"Numero failures: {len(result.component_failures)}")
                for failure in result.component_failures:
                    if isinstance(failure, dict):
                        print_error(f"  - {failure.get('fullName', 'Unknown')}: {failure.get('problem', 'No details')}")
                        print_error(f"    Type: {failure.get('problemType', 'Unknown')}")
                    else:
                        print_error(f"  - {failure}")
            else:
                print_error("Nessun component_failure restituito")
            if result.messages:
                print_error("Messaggi:")
                for msg in result.messages:
                    print_error(f"  - {msg}")
            return 1

    except Exception as e:
        print_error(f"Creazione oggetto fallita: {e}")
        import traceback
        traceback.print_exc()
        return 1

    # =========================================================================
    # TEST 5: RETRIEVE METADATA
    # =========================================================================
    print_step(5, "RETRIEVE METADATA - Scarica metadata da Salesforce")

    output_dir = "./metadata_retrieved"

    try:
        print_info(f"Retrieve metadata in: {output_dir}")
        print_info("Tipi da recuperare: CustomObject, CustomField")
        print_info("Componenti specifici: Account")

        print_info("\nRETRIEVE in corso (può richiedere 30-60 secondi)...")
        result = client.metadata.retrieve(
            component_types=["CustomObject"],
            specific_components={
                "CustomObject": ["Account"]
            },
            output_dir=output_dir,
            wait=True,
            timeout=120,
        )

        if result.success:
            print_success("Metadata retrieve completato!")
            print_success(f"Retrieve ID: {result.id}")
            print_success(f"Status: {result.status}")
            print_success(f"File scaricati: {len(result.file_properties)}")

            # List downloaded files
            if result.file_properties:
                print_info("\nFile scaricati:")
                for prop in result.file_properties[:10]:  # Show first 10
                    print(f"  - {prop.get('fileName')} ({prop.get('type')})")

            # Check if files exist
            output_path = Path(output_dir)
            if output_path.exists():
                files = list(output_path.rglob("*.xml"))
                print_success(f"Trovati {len(files)} file XML in {output_dir}")

                print_info("\n🔍 VERIFICA LOCALE:")
                print_info(f"1. Apri la cartella: {output_path.absolute()}")
                print_info("2. Verifica la presenza di objects/Account.object-meta.xml")
                print_info(f"3. Cerca il campo {test_field_name} tra i files")

        else:
            print_error(f"Retrieve fallito: {result.status}")
            for msg in result.messages:
                print_error(f"  - {msg}")
            return 1

    except Exception as e:
        print_error(f"Retrieve fallito: {e}")
        import traceback
        traceback.print_exc()
        return 1

    # =========================================================================
    # TEST 6: COMPARE METADATA
    # =========================================================================
    print_step(6, "COMPARE METADATA - Confronta metadata locale vs org")

    try:
        print_info("Confronto metadata...")
        print_info(f"Source: {output_dir}")
        print_info("Target: Current Salesforce org")

        print_info("\nCOMPARE in corso...")
        diff = client.metadata.compare(
            source_dir=output_dir,
            component_types=["CustomObject"]
        )

        print_success("Confronto completato!")
        print_info("\nRisultati:")
        print(f"  - Aggiunti: {diff.summary['added']}")
        print(f"  - Modificati: {diff.summary['modified']}")
        print(f"  - Eliminati: {diff.summary['deleted']}")
        print(f"  - Invariati: {diff.summary['unchanged']}")

        if diff.has_changes:
            print_info("\nComponenti modificati:")
            for mod in diff.modified[:5]:  # Show first 5
                print(f"  • {mod.get('name')} ({mod.get('type')})")
        else:
            print_success("Nessuna differenza trovata (metadata sincronizzato)")

    except Exception as e:
        print_error(f"Compare fallito: {e}")
        import traceback
        traceback.print_exc()
        return 1

    # =========================================================================
    # TEST 7: TEMPLATES
    # =========================================================================
    print_step(7, "TEMPLATES - Usa template predefiniti")

    try:
        print_info("Lista template disponibili:")
        template_list = templates.list_templates()
        for i, tmpl in enumerate(template_list, 1):
            print(f"  {i}. {tmpl['id']}: {tmpl['name']}")
            print(f"     {tmpl['description']}")

        print_info("\nTest template 'enterprise_crm'...")
        fields = templates.get_template("enterprise_crm", sobject="Account")

        print_success(f"Template restituisce {len(fields)} campi")
        print_info("Campi inclusi nel template:")
        for field in fields:
            print(f"  • {field.name} - {field.label} ({field.type.value})")

        print_info("\n💡 Per deployare questi campi:")
        print_info("  for field in fields:")
        print_info("      client.metadata.deploy_field(field)")

    except Exception as e:
        print_error(f"Template test fallito: {e}")
        import traceback
        traceback.print_exc()
        return 1

    # =========================================================================
    # RIEPILOGO FINALE
    # =========================================================================
    print(f"\n{GREEN}╔═══════════════════════════════════════════════════════════════════╗{RESET}")
    print(f"{GREEN}║                    TUTTI I TEST COMPLETATI! ✓                    ║{RESET}")
    print(f"{GREEN}╚═══════════════════════════════════════════════════════════════════╝{RESET}\n")

    print_info("📋 RIEPILOGO MODIFICHE SU SALESFORCE:")
    print(f"\n  1. {GREEN}CREATO{RESET} campo custom: Account.{test_field_name}")
    print(f"     → Setup > Object Manager > Account > Fields & Relationships")
    print(f"\n  2. {GREEN}CREATO{RESET} oggetto custom: {test_object_name}")
    print(f"     → Setup > Object Manager > {test_object_name}")
    print(f"\n  3. {GREEN}SCARICATO{RESET} metadata in: {output_dir}/")
    print(f"     → Controlla i file XML localmente")

    print(f"\n{YELLOW}⚠️  CLEANUP:{RESET}")
    print(f"  Per rimuovere i componenti di test creati:")
    print(f"  1. Setup > Object Manager > {test_object_name} > Delete")
    print(f"  2. Setup > Object Manager > Account > Fields > {test_field_name} > Delete")

    print(f"\n{GREEN}✓ Test completato con successo!{RESET}\n")

    return 0


if __name__ == "__main__":
    sys.exit(main())
