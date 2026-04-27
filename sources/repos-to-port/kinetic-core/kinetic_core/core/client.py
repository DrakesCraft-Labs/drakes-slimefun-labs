"""
Kinetic Core - Salesforce Integration Library
Copyright (c) 2025 Antonio Trento (https://antoniotrento.net)

This file is part of Kinetic Core, the foundational library powering KineticMCP.

Project: https://github.com/antonio-backend-projects/kinetic-core
Website: https://kineticmcp.com
Author: Antonio Trento
License: MIT (see LICENSE file for details)

Part of the KineticMCP ecosystem - AI-powered Salesforce integration tools.
"""

"""
Salesforce Client - Comprehensive CRUD operations for any Salesforce object.

Provides a high-level, generic interface for interacting with Salesforce REST API.
"""

import logging
import urllib.parse
from typing import Dict, List, Any, Optional, Callable

import requests

from kinetic_core.core.session import SalesforceSession


logger = logging.getLogger(__name__)


class SalesforceClient:
    """
    Generic client for Salesforce REST API operations.

    This client works with ANY Salesforce object (standard or custom) and provides:
    - Create (POST)
    - Read (GET, Query)
    - Update (PATCH)
    - Delete (DELETE)
    - Upsert (PATCH with external ID)
    - Bulk operations
    - Metadata describe

    Example:
        ```python
        from kinetic_core import JWTAuthenticator, SalesforceClient

        # Authenticate
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()

        # Create client
        client = SalesforceClient(session)

        # Create a record
        account_id = client.create("Account", {"Name": "ACME Corp"})

        # Query records
        accounts = client.query("SELECT Id, Name FROM Account LIMIT 10")

        # Update record
        client.update("Account", account_id, {"Phone": "555-1234"})

        # Delete record
        client.delete("Account", account_id)
        ```
    """

    def __init__(self, session: SalesforceSession, logger_instance: Optional[logging.Logger] = None):
        """
        Initialize Salesforce client.

        Args:
            session: Authenticated Salesforce session
            logger_instance: Optional custom logger
        """
        self.session = session
        self.logger = logger_instance or logger
        self._bulk_client = None  # Lazy initialization
        self._metadata_client = None  # Lazy initialization

        if not session.is_valid():
            raise ValueError("Invalid session: missing instance_url or access_token")

    @property
    def bulk(self):
        """
        Access Bulk API v2 operations.

        Returns:
            BulkV2Client: Client for bulk operations

        Example:
            ```python
            # Bulk insert 10,000 records
            records = [{"Name": f"Account {i}"} for i in range(10000)]
            result = client.bulk.insert("Account", records)
            print(f"Success: {result.success_count}, Failed: {result.failed_count}")
            ```
        """
        if self._bulk_client is None:
            from kinetic_core.bulk import BulkV2Client
            self._bulk_client = BulkV2Client(self.session)
        return self._bulk_client

    @property
    def metadata(self):
        """
        Access Metadata API operations.

        Returns:
            MetadataClient: Client for metadata operations

        Example:
            ```python
            # Create a custom field
            from kinetic_core.metadata import CustomField, FieldType

            field = CustomField(
                sobject="Account",
                name="Customer_Tier__c",
                type=FieldType.TEXT,
                label="Customer Tier",
                length=50
            )
            result = client.metadata.deploy_field(field)

            # Retrieve metadata
            result = client.metadata.retrieve(
                component_types=["CustomObject"],
                output_dir="./metadata"
            )
            ```
        """
        if self._metadata_client is None:
            from kinetic_core.metadata import MetadataClient
            self._metadata_client = MetadataClient(self.session)
        return self._metadata_client

    # ============================================================================
    # CREATE Operations
    # ============================================================================

    def create(self, sobject: str, data: Dict[str, Any]) -> str:
        """
        Create a new Salesforce record.

        Args:
            sobject: Salesforce object API name (e.g., 'Account', 'CustomObject__c')
            data: Field values to set {field_name: value}

        Returns:
            str: Salesforce ID of created record

        Raises:
            requests.HTTPError: If request fails
            RuntimeError: If response doesn't contain ID

        Example:
            ```python
            account_id = client.create("Account", {
                "Name": "ACME Corp",
                "Industry": "Technology",
                "Phone": "555-0100"
            })
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/"
        headers = {**self.session.auth_header, "Content-Type": "application/json"}

        self.logger.debug(f"Creating {sobject}: {data}")

        response = requests.post(url, headers=headers, json=data, timeout=30)

        if response.status_code != 201:
            self._handle_error(response, f"create {sobject}")

        result = response.json()
        record_id = result.get("id")

        if not record_id:
            raise RuntimeError(f"No ID in response: {result}")

        self.logger.info(f"✓ Created {sobject}: {record_id}")
        return record_id

    def create_batch(self, sobject: str, records: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        """
        Create multiple records in a single request (composite API).

        Args:
            sobject: Salesforce object API name
            records: List of record data dictionaries

        Returns:
            List[Dict]: Results for each record with 'id' and 'success' keys

        Example:
            ```python
            results = client.create_batch("Contact", [
                {"FirstName": "John", "LastName": "Doe"},
                {"FirstName": "Jane", "LastName": "Smith"}
            ])
            for result in results:
                print(f"ID: {result['id']}, Success: {result['success']}")
            ```
        """
        url = f"{self.session.base_url}/composite/sobjects"
        headers = {**self.session.auth_header, "Content-Type": "application/json"}

        payload = {
            "allOrNone": False,
            "records": [{"attributes": {"type": sobject}, **record} for record in records]
        }

        self.logger.debug(f"Creating batch of {len(records)} {sobject} records")

        response = requests.post(url, headers=headers, json=payload, timeout=60)

        if response.status_code != 200:
            self._handle_error(response, f"create batch {sobject}")

        results = response.json()
        success_count = sum(1 for r in results if r.get("success"))

        self.logger.info(f"✓ Created {success_count}/{len(records)} {sobject} records")
        return results

    # ============================================================================
    # READ Operations
    # ============================================================================

    def query(self, soql: str, include_deleted: bool = False) -> List[Dict[str, Any]]:
        """
        Execute a SOQL query and return all results (handles pagination).

        Args:
            soql: SOQL query string
            include_deleted: If True, include deleted records (uses queryAll)

        Returns:
            List[Dict]: Query results

        Example:
            ```python
            accounts = client.query(
                "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology'"
            )
            for account in accounts:
                print(account['Name'])
            ```
        """
        endpoint = "queryAll" if include_deleted else "query"
        url = f"{self.session.base_url}/{endpoint}/"
        params = {"q": soql}
        headers = self.session.auth_header

        all_records = []
        next_url = None

        self.logger.debug(f"Executing query: {soql[:100]}...")

        while True:
            if next_url:
                full_url = f"{self.session.instance_url}{next_url}"
                params = None
            else:
                full_url = url

            response = requests.get(full_url, headers=headers, params=params, timeout=30)

            if response.status_code != 200:
                self._handle_error(response, "query")

            data = response.json()
            records = data.get("records", [])
            all_records.extend(records)

            # Handle pagination
            next_url = data.get("nextRecordsUrl")
            if not next_url:
                break

            self.logger.debug(f"Fetching next page... (total: {len(all_records)})")

        self.logger.info(f"✓ Query returned {len(all_records)} records")
        return all_records

    def query_one(self, soql: str, include_deleted: bool = False) -> Optional[Dict[str, Any]]:
        """
        Execute a SOQL query and return the first result (or None).

        Args:
            soql: SOQL query string
            include_deleted: If True, include deleted records

        Returns:
            Optional[Dict]: First record or None if no results

        Example:
            ```python
            account = client.query_one(
                "SELECT Id, Name FROM Account WHERE Name = 'ACME Corp'"
            )
            if account:
                print(f"Found: {account['Name']}")
            ```
        """
        results = self.query(soql, include_deleted=include_deleted)
        return results[0] if results else None

    def get(self, sobject: str, record_id: str, fields: Optional[List[str]] = None) -> Dict[str, Any]:
        """
        Retrieve a single record by ID.

        Args:
            sobject: Salesforce object API name
            record_id: Salesforce record ID
            fields: Optional list of fields to retrieve (default: all accessible fields)

        Returns:
            Dict: Record data

        Raises:
            requests.HTTPError: If record not found (404) or other error

        Example:
            ```python
            account = client.get("Account", "001XXXXXXXXXXXX", ["Id", "Name", "Industry"])
            print(account['Name'])
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/{record_id}"
        headers = self.session.auth_header
        params = {"fields": ",".join(fields)} if fields else None

        self.logger.debug(f"Getting {sobject}/{record_id}")

        response = requests.get(url, headers=headers, params=params, timeout=30)

        if response.status_code != 200:
            self._handle_error(response, f"get {sobject}/{record_id}")

        data = response.json()
        self.logger.info(f"✓ Retrieved {sobject}/{record_id}")
        return data

    def count(self, sobject: str, where: str = "") -> int:
        """
        Count records of a given object (with optional filter).

        Args:
            sobject: Salesforce object API name
            where: Optional WHERE clause (without 'WHERE' keyword)

        Returns:
            int: Number of records

        Example:
            ```python
            total_accounts = client.count("Account")
            tech_accounts = client.count("Account", "Industry = 'Technology'")
            print(f"Total: {total_accounts}, Tech: {tech_accounts}")
            ```
        """
        where_clause = f" WHERE {where}" if where else ""
        soql = f"SELECT COUNT() FROM {sobject}{where_clause}"

        url = f"{self.session.base_url}/query/"
        headers = self.session.auth_header
        params = {"q": soql}

        response = requests.get(url, headers=headers, params=params, timeout=30)

        if response.status_code != 200:
            self._handle_error(response, "count")

        data = response.json()
        count_value = data.get("totalSize", 0)

        self.logger.info(f"✓ Count {sobject}: {count_value}")
        return count_value

    # ============================================================================
    # UPDATE Operations
    # ============================================================================

    def update(self, sobject: str, record_id: str, data: Dict[str, Any]) -> bool:
        """
        Update an existing Salesforce record.

        Args:
            sobject: Salesforce object API name
            record_id: Salesforce record ID
            data: Field values to update {field_name: value}

        Returns:
            bool: True if successful

        Raises:
            requests.HTTPError: If update fails

        Example:
            ```python
            client.update("Account", "001XXXXXXXXXXXX", {
                "Phone": "555-9999",
                "Industry": "Manufacturing"
            })
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/{record_id}"
        headers = {**self.session.auth_header, "Content-Type": "application/json"}

        self.logger.debug(f"Updating {sobject}/{record_id}: {data}")

        response = requests.patch(url, headers=headers, json=data, timeout=30)

        if response.status_code != 204:
            self._handle_error(response, f"update {sobject}/{record_id}")

        self.logger.info(f"✓ Updated {sobject}/{record_id}")
        return True

    def upsert(
        self,
        sobject: str,
        external_id_field: str,
        external_id_value: str,
        data: Dict[str, Any]
    ) -> str:
        """
        Upsert (insert or update) a record using an external ID field.

        Args:
            sobject: Salesforce object API name
            external_id_field: Name of external ID field (must be indexed in Salesforce)
            external_id_value: Value of external ID
            data: Field values to set

        Returns:
            str: Salesforce ID (existing or newly created)

        Example:
            ```python
            # Upsert Account by external key
            account_id = client.upsert(
                "Account",
                "External_Key__c",
                "EXT-12345",
                {"Name": "ACME Corp", "Industry": "Tech"}
            )
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/{external_id_field}/{external_id_value}"
        headers = {**self.session.auth_header, "Content-Type": "application/json"}

        self.logger.debug(f"Upserting {sobject} by {external_id_field}={external_id_value}")

        response = requests.patch(url, headers=headers, json=data, timeout=30)

        # 201 = created, 204 = updated
        if response.status_code == 201:
            result = response.json()
            record_id = result.get("id")
            self.logger.info(f"✓ Created {sobject}: {record_id}")
            return record_id
        elif response.status_code == 204:
            # For updates, we need to query to get the ID
            record = self.query_one(
                f"SELECT Id FROM {sobject} WHERE {external_id_field} = '{external_id_value}'"
            )
            record_id = record["Id"] if record else None
            self.logger.info(f"✓ Updated {sobject}: {record_id}")
            return record_id
        else:
            self._handle_error(response, f"upsert {sobject}")

    # ============================================================================
    # DELETE Operations
    # ============================================================================

    def delete(self, sobject: str, record_id: str) -> bool:
        """
        Delete a Salesforce record.

        Args:
            sobject: Salesforce object API name
            record_id: Salesforce record ID

        Returns:
            bool: True if successful

        Raises:
            requests.HTTPError: If delete fails

        Example:
            ```python
            client.delete("Account", "001XXXXXXXXXXXX")
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/{record_id}"
        headers = self.session.auth_header

        self.logger.debug(f"Deleting {sobject}/{record_id}")

        response = requests.delete(url, headers=headers, timeout=30)

        if response.status_code != 204:
            self._handle_error(response, f"delete {sobject}/{record_id}")

        self.logger.info(f"✓ Deleted {sobject}/{record_id}")
        return True

    # ============================================================================
    # METADATA Operations
    # ============================================================================

    def describe(self, sobject: str) -> Dict[str, Any]:
        """
        Get metadata for a Salesforce object (fields, relationships, etc.).

        Args:
            sobject: Salesforce object API name

        Returns:
            Dict: Object metadata

        Example:
            ```python
            metadata = client.describe("Account")
            print(f"Label: {metadata['label']}")
            for field in metadata['fields']:
                print(f"  {field['name']} ({field['type']})")
            ```
        """
        url = f"{self.session.base_url}/sobjects/{sobject}/describe/"
        headers = self.session.auth_header

        self.logger.debug(f"Describing {sobject}")

        response = requests.get(url, headers=headers, timeout=30)

        if response.status_code != 200:
            self._handle_error(response, f"describe {sobject}")

        data = response.json()
        self.logger.info(f"✓ Described {sobject}")
        return data

    def list_objects(self) -> List[Dict[str, Any]]:
        """
        List all available Salesforce objects in the org.

        Returns:
            List[Dict]: Object metadata summary

        Example:
            ```python
            objects = client.list_objects()
            for obj in objects:
                print(f"{obj['name']}: {obj['label']}")
            ```
        """
        url = f"{self.session.base_url}/sobjects/"
        headers = self.session.auth_header

        self.logger.debug("Listing all objects")

        response = requests.get(url, headers=headers, timeout=30)

        if response.status_code != 200:
            self._handle_error(response, "list objects")

        data = response.json()
        objects = data.get("sobjects", [])

        self.logger.info(f"✓ Listed {len(objects)} objects")
        return objects

    # ============================================================================
    # UTILITY Methods
    # ============================================================================

    def query_with_callback(
        self,
        soql: str,
        callback: Callable[[Dict[str, Any]], None],
        batch_size: int = 100
    ) -> int:
        """
        Execute a query and process results with a callback function.

        Useful for processing large datasets without loading everything into memory.

        Args:
            soql: SOQL query string
            callback: Function to call for each record
            batch_size: Number of records to process before logging progress

        Returns:
            int: Total number of records processed

        Example:
            ```python
            def process_account(account):
                print(f"Processing: {account['Name']}")
                # Do something with the account

            count = client.query_with_callback(
                "SELECT Id, Name FROM Account",
                callback=process_account,
                batch_size=50
            )
            print(f"Processed {count} accounts")
            ```
        """
        records = self.query(soql)
        total = len(records)

        self.logger.info(f"Processing {total} records with callback")

        for i, record in enumerate(records, 1):
            try:
                callback(record)

                if i % batch_size == 0:
                    self.logger.debug(f"Processed {i}/{total} records")

            except Exception as e:
                self.logger.error(
                    f"Error in callback for record {record.get('Id', 'Unknown')}: {e}",
                    exc_info=True
                )

        self.logger.info(f"✓ Completed processing {total} records")
        return total

    def _handle_error(self, response: requests.Response, context: str) -> None:
        """
        Handle and log API errors.

        Args:
            response: Failed HTTP response
            context: Context description for the error

        Raises:
            requests.HTTPError: Always raises after logging
        """
        try:
            error_body = response.json()
        except Exception:
            error_body = response.text

        self.logger.error(
            f"✗ Salesforce API error ({context}): "
            f"HTTP {response.status_code} - {error_body}"
        )

        response.raise_for_status()
