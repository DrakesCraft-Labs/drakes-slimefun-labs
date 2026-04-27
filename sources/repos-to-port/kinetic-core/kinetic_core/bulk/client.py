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
Salesforce Bulk API v2 client implementation.
"""

import requests
import time
from typing import List, Dict, Any, Optional, Callable
from ..core.session import SalesforceSession
from .job import BulkJob, BulkResult, BulkQueryResult, BulkError
from .operations import BulkOperation
from .serializer import CSVSerializer
from .poller import JobPoller, create_default_poller


class BulkV2Client:
    """
    Salesforce Bulk API v2 client for high-volume data operations.

    Supports:
    - insert: Create new records
    - update: Update existing records (requires Id)
    - upsert: Insert or update using external ID
    - delete: Delete records (requires Id)
    - hard_delete: Permanently delete records
    - query: Export large datasets

    Example:
        >>> from kinetic_core import JWTAuthenticator, SalesforceClient
        >>> auth = JWTAuthenticator.from_env()
        >>> session = auth.authenticate()
        >>> client = SalesforceClient(session)
        >>>
        >>> # Bulk insert 10,000 records
        >>> records = [{"Name": f"Account {i}"} for i in range(10000)]
        >>> result = client.bulk.insert("Account", records)
        >>> print(f"Success: {result.success_count}, Failed: {result.failed_count}")
    """

    def __init__(self, session: SalesforceSession):
        """
        Initialize Bulk API v2 client.

        Args:
            session: Authenticated Salesforce session
        """
        self.session = session
        self.base_url = f"{session.instance_url}/services/data/{self.session.api_version}/jobs/ingest"
        self.serializer = CSVSerializer()

    def insert(
        self,
        sobject: str,
        records: List[Dict[str, Any]],
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """
        Bulk insert records.

        Args:
            sobject: Salesforce object name (e.g., "Account")
            records: List of records to insert
            wait: Wait for job completion (default: True)
            timeout_minutes: Timeout in minutes (default: 10)
            on_progress: Optional callback for progress updates

        Returns:
            BulkResult with success/failure details

        Example:
            >>> records = [
            ...     {"Name": "Acme Corp", "Industry": "Technology"},
            ...     {"Name": "Global Inc", "Industry": "Manufacturing"}
            ... ]
            >>> result = client.bulk.insert("Account", records)
        """
        return self._execute_operation(
            BulkOperation.INSERT,
            sobject,
            records,
            wait=wait,
            timeout_minutes=timeout_minutes,
            on_progress=on_progress
        )

    def update(
        self,
        sobject: str,
        records: List[Dict[str, Any]],
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """
        Bulk update records (requires Id field).

        Args:
            sobject: Salesforce object name
            records: List of records to update (must include Id field)
            wait: Wait for job completion
            timeout_minutes: Timeout in minutes
            on_progress: Optional callback for progress updates

        Returns:
            BulkResult with success/failure details
        """
        # Validate all records have Id
        missing_id = [i for i, r in enumerate(records) if 'Id' not in r]
        if missing_id:
            raise ValueError(
                f"Update operation requires 'Id' field in all records. "
                f"Missing in records at indices: {missing_id[:10]}"
            )

        return self._execute_operation(
            BulkOperation.UPDATE,
            sobject,
            records,
            wait=wait,
            timeout_minutes=timeout_minutes,
            on_progress=on_progress
        )

    def upsert(
        self,
        sobject: str,
        records: List[Dict[str, Any]],
        external_id_field: str,
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """
        Bulk upsert records using external ID field.

        Args:
            sobject: Salesforce object name
            records: List of records to upsert
            external_id_field: External ID field name (e.g., "External_Key__c")
            wait: Wait for job completion
            timeout_minutes: Timeout in minutes
            on_progress: Optional callback for progress updates

        Returns:
            BulkResult with success/failure details

        Example:
            >>> records = [
            ...     {"External_Key__c": "EXT001", "Name": "Acme Corp"},
            ...     {"External_Key__c": "EXT002", "Name": "Global Inc"}
            ... ]
            >>> result = client.bulk.upsert("Account", records, "External_Key__c")
        """
        # Validate external ID field exists in records
        missing_ext_id = [
            i for i, r in enumerate(records)
            if external_id_field not in r
        ]
        if missing_ext_id:
            raise ValueError(
                f"Upsert operation requires '{external_id_field}' field in all records. "
                f"Missing in records at indices: {missing_ext_id[:10]}"
            )

        return self._execute_operation(
            BulkOperation.UPSERT,
            sobject,
            records,
            external_id_field=external_id_field,
            wait=wait,
            timeout_minutes=timeout_minutes,
            on_progress=on_progress
        )

    def delete(
        self,
        sobject: str,
        ids: List[str],
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """
        Bulk delete records.

        Args:
            sobject: Salesforce object name
            ids: List of record IDs to delete
            wait: Wait for job completion
            timeout_minutes: Timeout in minutes
            on_progress: Optional callback for progress updates

        Returns:
            BulkResult with success/failure details

        Example:
            >>> ids = ["001xxx000001", "001xxx000002"]
            >>> result = client.bulk.delete("Account", ids)
        """
        # Convert IDs to CSV format
        csv_data = self.serializer.ids_to_csv(ids)

        # Create and execute job
        job = self._create_job(BulkOperation.DELETE, sobject)
        self._upload_data(job.id, csv_data)
        job = self._close_job(job.id)

        if wait:
            job = self._poll_job(job.id, timeout_minutes, on_progress)

        return self._get_results(job)

    def hard_delete(
        self,
        sobject: str,
        ids: List[str],
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """
        Bulk hard delete records (permanent deletion, bypasses recycle bin).

        Args:
            sobject: Salesforce object name
            ids: List of record IDs to hard delete
            wait: Wait for job completion
            timeout_minutes: Timeout in minutes
            on_progress: Optional callback for progress updates

        Returns:
            BulkResult with success/failure details

        Note:
            Requires "Bulk API Hard Delete" permission.
        """
        csv_data = self.serializer.ids_to_csv(ids)

        job = self._create_job(BulkOperation.HARD_DELETE, sobject)
        self._upload_data(job.id, csv_data)
        job = self._close_job(job.id)

        if wait:
            job = self._poll_job(job.id, timeout_minutes, on_progress)

        return self._get_results(job)

    def query(
        self,
        soql: str,
        timeout_minutes: Optional[float] = 30,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkQueryResult:
        """
        Bulk query for exporting large datasets.

        Args:
            soql: SOQL query string
            timeout_minutes: Timeout in minutes
            on_progress: Optional callback for progress updates

        Returns:
            BulkQueryResult with query results

        Example:
            >>> result = client.bulk.query(
            ...     "SELECT Id, Name, Industry FROM Account WHERE CreatedDate = LAST_YEAR"
            ... )
            >>> print(f"Retrieved {result.record_count} records")
        """
        # Create query job
        job = self._create_query_job(soql)

        # Poll for completion
        job = self._poll_job(job.id, timeout_minutes, on_progress, is_query=True)

        # Get results
        records = self._get_query_results(job.id)

        return BulkQueryResult(
            job=job,
            records=records,
            locator=None  # v2.0 doesn't use locators like v1.0
        )

    def _execute_operation(
        self,
        operation: BulkOperation,
        sobject: str,
        records: List[Dict[str, Any]],
        external_id_field: Optional[str] = None,
        wait: bool = True,
        timeout_minutes: Optional[float] = 10,
        on_progress: Optional[Callable[[BulkJob], None]] = None
    ) -> BulkResult:
        """Execute a bulk data operation (insert, update, upsert)."""
        # Convert records to CSV
        csv_data = self.serializer.records_to_csv(records)

        # Create job
        job = self._create_job(operation, sobject, external_id_field)

        # Upload data
        self._upload_data(job.id, csv_data)

        # Close job to start processing
        job = self._close_job(job.id)

        # Wait for completion if requested
        if wait:
            job = self._poll_job(job.id, timeout_minutes, on_progress)

        # Get results
        return self._get_results(job)

    def _create_job(
        self,
        operation: BulkOperation,
        sobject: str,
        external_id_field: Optional[str] = None
    ) -> BulkJob:
        """Create a new bulk job."""
        payload = {
            "object": sobject,
            "operation": operation.value,
            "contentType": "CSV"
        }

        if external_id_field:
            payload["externalIdFieldName"] = external_id_field

        response = requests.post(
            self.base_url,
            headers=self._get_headers(),
            json=payload
        )
        response.raise_for_status()

        return BulkJob.from_api_response(response.json())

    def _create_query_job(self, soql: str) -> BulkJob:
        """Create a bulk query job."""
        url = f"{self.session.instance_url}/services/data/{self.session.api_version}/jobs/query"

        payload = {
            "operation": "query",
            "query": soql
        }

        response = requests.post(
            url,
            headers=self._get_headers(),
            json=payload
        )
        response.raise_for_status()

        return BulkJob.from_api_response(response.json())

    def _upload_data(self, job_id: str, csv_data: str) -> None:
        """Upload CSV data to job."""
        url = f"{self.base_url}/{job_id}/batches"

        headers = self._get_headers()
        headers['Content-Type'] = 'text/csv'

        response = requests.put(
            url,
            headers=headers,
            data=csv_data.encode('utf-8')
        )
        response.raise_for_status()

    def _close_job(self, job_id: str) -> BulkJob:
        """Close job to begin processing."""
        url = f"{self.base_url}/{job_id}"

        response = requests.patch(
            url,
            headers=self._get_headers(),
            json={"state": "UploadComplete"}
        )
        response.raise_for_status()

        return BulkJob.from_api_response(response.json())

    def _get_job_status(self, job_id: str, is_query: bool = False) -> BulkJob:
        """
        Get current job status.

        Args:
            job_id: Job ID
            is_query: True if this is a query job, False for ingest job
        """
        if is_query:
            url = f"{self.session.instance_url}/services/data/{self.session.api_version}/jobs/query/{job_id}"
        else:
            url = f"{self.base_url}/{job_id}"

        response = requests.get(url, headers=self._get_headers())
        response.raise_for_status()

        return BulkJob.from_api_response(response.json())

    def _poll_job(
        self,
        job_id: str,
        timeout_minutes: Optional[float],
        on_progress: Optional[Callable[[BulkJob], None]] = None,
        is_query: bool = False
    ) -> BulkJob:
        """
        Poll job until completion.

        Args:
            job_id: Job ID to poll
            timeout_minutes: Timeout in minutes
            on_progress: Optional progress callback
            is_query: True if this is a query job
        """
        poller = create_default_poller(timeout_minutes)

        return poller.poll(
            check_status=lambda: self._get_job_status(job_id, is_query=is_query),
            on_progress=on_progress
        )

    def _get_results(self, job: BulkJob) -> BulkResult:
        """Get job results (successful and failed records)."""
        success_records = self._get_successful_results(job.id)
        failed_records = self._get_failed_results(job.id)

        # Parse errors from failed records
        errors = [
            BulkError.from_csv_row(record)
            for record in failed_records
        ]

        return BulkResult(
            job=job,
            success_records=success_records,
            failed_records=failed_records,
            errors=errors
        )

    def _get_successful_results(self, job_id: str) -> List[Dict[str, Any]]:
        """Get successful records from job."""
        url = f"{self.base_url}/{job_id}/successfulResults"

        response = requests.get(url, headers=self._get_headers())
        response.raise_for_status()

        csv_data = response.text
        return self.serializer.csv_to_records(csv_data)

    def _get_failed_results(self, job_id: str) -> List[Dict[str, Any]]:
        """Get failed records from job."""
        url = f"{self.base_url}/{job_id}/failedResults"

        response = requests.get(url, headers=self._get_headers())
        response.raise_for_status()

        csv_data = response.text
        return self.serializer.csv_to_records(csv_data)

    def _get_query_results(self, job_id: str) -> List[Dict[str, Any]]:
        """Get query results."""
        url = f"{self.session.instance_url}/services/data/{self.session.api_version}/jobs/query/{job_id}/results"

        response = requests.get(url, headers=self._get_headers())
        response.raise_for_status()

        csv_data = response.text
        return self.serializer.csv_to_records(csv_data)

    def _get_headers(self) -> Dict[str, str]:
        """Get HTTP headers for API requests."""
        return {
            'Authorization': f'Bearer {self.session.access_token}',
            'Content-Type': 'application/json'
        }

    def abort_job(self, job_id: str) -> BulkJob:
        """
        Abort a running bulk job.

        Args:
            job_id: Job ID to abort

        Returns:
            Updated BulkJob with Aborted state
        """
        url = f"{self.base_url}/{job_id}"

        response = requests.patch(
            url,
            headers=self._get_headers(),
            json={"state": "Aborted"}
        )
        response.raise_for_status()

        return BulkJob.from_api_response(response.json())

    def get_job(self, job_id: str) -> BulkJob:
        """
        Get job information.

        Args:
            job_id: Job ID

        Returns:
            BulkJob with current status
        """
        return self._get_job_status(job_id)
