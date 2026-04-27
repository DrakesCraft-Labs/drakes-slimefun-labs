# Bulk API v2 - Practical Examples

Real-world examples for common Bulk API use cases.

## Table of Contents

1. [Data Migration](#data-migration)
2. [CSV Import](#csv-import)
3. [Scheduled Batch Jobs](#scheduled-batch-jobs)
4. [Data Synchronization](#data-synchronization)
5. [Large-Scale Updates](#large-scale-updates)
6. [Error Recovery](#error-recovery)

---

## Data Migration

Migrate large datasets from external systems to Salesforce.

### Complete Migration Script

```python
from kinetic_core import JWTAuthenticator, SalesforceClient
import pandas as pd

def migrate_accounts_from_csv(csv_file: str):
    """Migrate accounts from CSV to Salesforce"""

    # Load data
    df = pd.read_csv(csv_file)

    # Transform to Salesforce format
    records = df.to_dict('records')

    # Authenticate
    auth = JWTAuthenticator.from_env()
    session = auth.authenticate()
    client = SalesforceClient(session)

    # Bulk insert with progress tracking
    total = len(records)

    def show_progress(job):
        processed = job.number_records_processed
        percent = (processed / total) * 100 if total > 0 else 0
        print(f"Migration progress: {processed}/{total} ({percent:.1f}%)")

    result = client.bulk.insert(
        "Account",
        records,
        wait=True,
        timeout_minutes=30,
        on_progress=show_progress
    )

    # Report results
    print(f"\n✓ Migration completed!")
    print(f"  Successful: {result.success_count}")
    print(f"  Failed: {result.failed_count}")

    # Save results
    if result.success_count > 0:
        pd.DataFrame(result.success_records).to_csv('migrated_accounts.csv', index=False)

    if result.failed_count > 0:
        pd.DataFrame(result.failed_records).to_csv('failed_accounts.csv', index=False)
        print(f"\nErrors:")
        for error in result.errors[:10]:
            print(f"  - {error.message}")

    return result

# Run migration
if __name__ == "__main__":
    result = migrate_accounts_from_csv("accounts_to_migrate.csv")
```

---

## CSV Import

Import data from CSV files with validation.

```python
import csv
from typing import List, Dict
from kinetic_core import SalesforceClient

class BulkCSVImporter:
    """Import CSV files using Bulk API v2"""

    def __init__(self, client: SalesforceClient):
        self.client = client

    def import_csv(
        self,
        csv_path: str,
        sobject: str,
        field_mapping: Dict[str, str] = None,
        batch_size: int = 10000
    ):
        """
        Import CSV file to Salesforce.

        Args:
            csv_path: Path to CSV file
            sobject: Salesforce object name
            field_mapping: Map CSV headers to SF fields (optional)
            batch_size: Records per batch
        """
        records = self._read_csv(csv_path, field_mapping)

        # Process in batches
        results = []
        for i in range(0, len(records), batch_size):
            batch = records[i:i + batch_size]

            print(f"Processing batch {i//batch_size + 1}...")
            result = self.client.bulk.insert(sobject, batch)

            results.append(result)
            print(f"  Success: {result.success_count}, Failed: {result.failed_count}")

        return results

    def _read_csv(self, csv_path: str, field_mapping: Dict = None) -> List[Dict]:
        """Read and transform CSV"""
        records = []

        with open(csv_path, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)

            for row in reader:
                if field_mapping:
                    # Apply field mapping
                    record = {
                        sf_field: row[csv_field]
                        for csv_field, sf_field in field_mapping.items()
                        if csv_field in row
                    }
                else:
                    record = dict(row)

                records.append(record)

        return records

# Usage
from kinetic_core import JWTAuthenticator

auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

importer = BulkCSVImporter(client)

# Import with field mapping
field_mapping = {
    'company_name': 'Name',
    'industry_type': 'Industry',
    'annual_revenue': 'AnnualRevenue'
}

results = importer.import_csv(
    'data.csv',
    'Account',
    field_mapping=field_mapping,
    batch_size=5000
)
```

---

## Scheduled Batch Jobs

Automate regular data operations.

```python
from kinetic_core import SalesforceClient
from datetime import datetime, timedelta
import schedule
import time

class ScheduledBulkJob:
    """Run bulk operations on a schedule"""

    def __init__(self, client: SalesforceClient):
        self.client = client

    def cleanup_old_records(self, days_old: int = 90):
        """Delete records older than specified days"""

        # Query old records
        cutoff_date = (datetime.now() - timedelta(days=days_old)).strftime('%Y-%m-%d')

        query = f"""
            SELECT Id
            FROM Account
            WHERE CreatedDate < {cutoff_date}
            AND Status__c = 'Inactive'
        """

        result = self.client.bulk.query(query)

        if result.record_count > 0:
            ids = [r['Id'] for r in result.records]

            print(f"Deleting {len(ids)} old records...")
            delete_result = self.client.bulk.delete("Account", ids)

            print(f"Deleted: {delete_result.success_count}")

            # Log to custom object
            self._log_cleanup(delete_result)

    def _log_cleanup(self, result):
        """Log cleanup operation"""
        log_record = {
            "Operation__c": "Cleanup",
            "Records_Processed__c": result.success_count,
            "Run_Date__c": datetime.now().strftime('%Y-%m-%d'),
            "Job_Id__c": result.job.id
        }

        self.client.create("Bulk_Job_Log__c", log_record)

    def sync_external_system(self):
        """Sync data from external system"""

        # Fetch from external API
        external_data = self._fetch_external_data()

        # Upsert to Salesforce
        result = self.client.bulk.upsert(
            "Account",
            external_data,
            external_id_field="External_Id__c"
        )

        print(f"Synced: {result.success_count} records")

    def _fetch_external_data(self):
        """Fetch data from external system (mock)"""
        # Replace with actual API call
        return [
            {"External_Id__c": "EXT001", "Name": "External Account 1"},
            {"External_Id__c": "EXT002", "Name": "External Account 2"}
        ]

# Setup scheduled jobs
from kinetic_core import JWTAuthenticator

auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

job_runner = ScheduledBulkJob(client)

# Schedule cleanup every day at 2 AM
schedule.every().day.at("02:00").do(job_runner.cleanup_old_records, days_old=90)

# Schedule sync every 6 hours
schedule.every(6).hours.do(job_runner.sync_external_system)

# Run scheduler
print("Scheduler started...")
while True:
    schedule.run_pending()
    time.sleep(60)
```

---

## Data Synchronization

Bi-directional sync between Salesforce and external systems.

```python
from kinetic_core import SalesforceClient
from datetime import datetime

class DataSync:
    """Synchronize data between Salesforce and external system"""

    def __init__(self, client: SalesforceClient):
        self.client = client

    def sync_to_salesforce(self, external_records: list):
        """Sync external data to Salesforce"""

        # Prepare for upsert
        sf_records = [
            {
                "External_Id__c": rec['external_id'],
                "Name": rec['name'],
                "Status__c": rec['status'],
                "Last_Sync__c": datetime.now().isoformat()
            }
            for rec in external_records
        ]

        result = self.client.bulk.upsert(
            "Account",
            sf_records,
            external_id_field="External_Id__c"
        )

        return result

    def sync_from_salesforce(self, last_sync_date: str):
        """Sync Salesforce changes to external system"""

        # Query changed records
        query = f"""
            SELECT Id, External_Id__c, Name, Status__c
            FROM Account
            WHERE LastModifiedDate > {last_sync_date}
            AND External_Id__c != null
        """

        result = self.client.bulk.query(query)

        # Transform for external system
        external_updates = [
            {
                'external_id': rec['External_Id__c'],
                'name': rec['Name'],
                'status': rec['Status__c']
            }
            for rec in result.records
        ]

        # Send to external system
        self._update_external_system(external_updates)

        return len(external_updates)

    def _update_external_system(self, records):
        """Update external system (mock)"""
        print(f"Updating {len(records)} records in external system")
        # Replace with actual API calls

# Usage
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

sync = DataSync(client)

# Sync to Salesforce
external_data = [
    {'external_id': 'EXT001', 'name': 'Company A', 'status': 'Active'},
    {'external_id': 'EXT002', 'name': 'Company B', 'status': 'Inactive'}
]

result = sync.sync_to_salesforce(external_data)
print(f"Synced to SF: {result.success_count}")

# Sync from Salesforce
updated = sync.sync_from_salesforce("2025-01-01T00:00:00Z")
print(f"Synced from SF: {updated}")
```

---

## Large-Scale Updates

Update millions of records efficiently.

```python
from kinetic_core import SalesforceClient

def mass_update_with_retry(
    client: SalesforceClient,
    sobject: str,
    updates: list,
    max_retries: int = 3
):
    """
    Update records with automatic retry for failures.

    Args:
        client: SalesforceClient instance
        sobject: Object name
        updates: Records to update
        max_retries: Maximum retry attempts
    """

    remaining = updates.copy()
    attempt = 0

    while remaining and attempt < max_retries:
        attempt += 1
        print(f"\nAttempt {attempt}: Processing {len(remaining)} records...")

        result = client.bulk.update(sobject, remaining)

        print(f"  Success: {result.success_count}")
        print(f"  Failed: {result.failed_count}")

        if result.failed_count == 0:
            print("✓ All records updated successfully!")
            return result

        # Prepare failed records for retry
        remaining = []
        for i, record in enumerate(result.failed_records):
            error = result.errors[i] if i < len(result.errors) else None

            # Only retry if error is recoverable
            if error and 'UNABLE_TO_LOCK_ROW' in error.status_code:
                # Re-add original record for retry
                original_record = updates[i]
                remaining.append(original_record)
            else:
                print(f"  Permanent failure: {error.message if error else 'Unknown'}")

        if remaining:
            print(f"  Will retry {len(remaining)} records...")
            time.sleep(5)  # Wait before retry

    if remaining:
        print(f"\n✗ {len(remaining)} records failed after {max_retries} attempts")

    return result

# Usage
from kinetic_core import JWTAuthenticator

auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Mass update with field formula
query_result = client.bulk.query(
    "SELECT Id, Annual Revenue FROM Account WHERE AnnualRevenue > 1000000"
)

updates = [
    {
        "Id": rec["Id"],
        "Tier__c": "Premium" if rec["AnnualRevenue"] > 5000000 else "Standard"
    }
    for rec in query_result.records
]

result = mass_update_with_retry(client, "Account", updates)
```

---

## Error Recovery

Handle and recover from bulk operation failures.

```python
from kinetic_core import SalesforceClient
import json
from datetime import datetime

class BulkErrorRecovery:
    """Recover from bulk operation failures"""

    def __init__(self, client: SalesforceClient):
        self.client = client

    def safe_bulk_insert(self, sobject: str, records: list):
        """Insert with comprehensive error handling"""

        result = self.client.bulk.insert(sobject, records)

        if result.failed_count > 0:
            self._handle_failures(sobject, result, records)

        return result

    def _handle_failures(self, sobject, result, original_records):
        """Process and log failures"""

        # Save failed records
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        error_file = f"bulk_errors_{sobject}_{timestamp}.json"

        error_data = {
            'job_id': result.job.id,
            'sobject': sobject,
            'timestamp': timestamp,
            'total_failed': result.failed_count,
            'errors': [
                {
                    'record': result.failed_records[i] if i < len(result.failed_records) else {},
                    'error_message': error.message,
                    'error_code': error.status_code,
                    'fields': error.fields
                }
                for i, error in enumerate(result.errors)
            ]
        }

        with open(error_file, 'w') as f:
            json.dump(error_data, f, indent=2)

        print(f"✗ Errors saved to: {error_file}")

        # Categorize errors
        self._categorize_errors(result.errors)

    def _categorize_errors(self, errors):
        """Categorize errors by type"""

        categories = {}

        for error in errors:
            error_type = error.status_code
            categories[error_type] = categories.get(error_type, 0) + 1

        print("\nError summary:")
        for error_type, count in sorted(categories.items()):
            print(f"  {error_type}: {count}")

    def retry_from_error_log(self, error_file: str):
        """Retry failed records from error log"""

        with open(error_file, 'r') as f:
            error_data = json.load(f)

        sobject = error_data['sobject']
        failed_records = [e['record'] for e in error_data['errors']]

        print(f"Retrying {len(failed_records)} failed records...")

        result = self.client.bulk.insert(sobject, failed_records)

        print(f"Retry result: {result.success_count} succeeded, {result.failed_count} failed")

        return result

# Usage
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

recovery = BulkErrorRecovery(client)

# Insert with error handling
records = [
    {"Name": "Valid Account"},
    {"InvalidField__c": "This will fail"},  # Invalid field
    {"Name": None}  # Missing required field
]

result = recovery.safe_bulk_insert("Account", records)

# Later, retry from error log
# recovery.retry_from_error_log("bulk_errors_Account_20250102_120000.json")
```

---

## Performance Monitoring

Track and optimize bulk operation performance.

```python
from kinetic_core import SalesforceClient
import time

class BulkPerformanceMonitor:
    """Monitor bulk operation performance"""

    def __init__(self, client: SalesforceClient):
        self.client = client

    def benchmark_operation(self, sobject: str, records: list, operation: str = "insert"):
        """Benchmark a bulk operation"""

        print(f"\n{'='*60}")
        print(f"Benchmarking {operation} on {sobject}")
        print(f"Records: {len(records):,}")
        print(f"{'='*60}\n")

        start_time = time.time()

        # Execute operation
        if operation == "insert":
            result = self.client.bulk.insert(sobject, records)
        elif operation == "update":
            result = self.client.bulk.update(sobject, records)
        else:
            raise ValueError(f"Unknown operation: {operation}")

        end_time = time.time()
        duration = end_time - start_time

        # Calculate metrics
        records_per_second = len(records) / duration if duration > 0 else 0

        print(f"\nPerformance Metrics:")
        print(f"  Total time: {duration:.2f} seconds")
        print(f"  Records/second: {records_per_second:.0f}")
        print(f"  Success rate: {(result.success_count/len(records)*100):.1f}%")

        return {
            'duration': duration,
            'records_per_second': records_per_second,
            'success_rate': result.success_count / len(records)
        }

# Usage
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

monitor = BulkPerformanceMonitor(client)

# Generate test data
test_records = [
    {"Name": f"Test Account {i}"}
    for i in range(10000)
]

metrics = monitor.benchmark_operation("Account", test_records, "insert")
```

---

## Next Steps

- 📖 [API Reference](../api/BULK_API_V2.md)
- 🚀 [Quick Start Guide](../guides/BULK_QUICKSTART.md)
- 🔧 [Troubleshooting](../api/BULK_API_V2.md#troubleshooting)

## Need More Examples?

Open an issue on [GitHub](https://github.com/antonio-backend-projects/kinetic-core/issues) with your use case!
