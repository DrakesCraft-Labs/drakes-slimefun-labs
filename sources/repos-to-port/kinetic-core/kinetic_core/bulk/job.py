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
Bulk API v2 job models and data structures.
"""

from typing import List, Dict, Any, Optional
from dataclasses import dataclass
from datetime import datetime


@dataclass
class BulkJob:
    """Represents a Bulk API v2 job."""

    id: str
    operation: str  # insert, update, upsert, delete, query
    object: str
    state: str  # Open, UploadComplete, InProgress, JobComplete, Failed, Aborted
    created_date: datetime
    system_modstamp: Optional[datetime] = None
    external_id_field_name: Optional[str] = None
    concurrency_mode: str = "Parallel"
    content_type: str = "CSV"
    api_version: str = "v60.0"

    # Job statistics
    number_records_processed: int = 0
    number_records_failed: int = 0
    total_processing_time: int = 0
    api_active_processing_time: int = 0
    apex_processing_time: int = 0

    def is_complete(self) -> bool:
        """Check if job has completed (success or failure)."""
        return self.state in ('JobComplete', 'Failed', 'Aborted')

    def is_successful(self) -> bool:
        """Check if job completed successfully."""
        return self.state == 'JobComplete'

    @property
    def success_count(self) -> int:
        """Number of successfully processed records."""
        return self.number_records_processed - self.number_records_failed

    @property
    def failed_count(self) -> int:
        """Number of failed records."""
        return self.number_records_failed

    @classmethod
    def from_api_response(cls, data: Dict[str, Any]) -> 'BulkJob':
        """Create BulkJob from Salesforce API response."""
        return cls(
            id=data['id'],
            operation=data['operation'],
            object=data['object'],
            state=data['state'],
            created_date=datetime.fromisoformat(data['createdDate'].replace('Z', '+00:00')),
            system_modstamp=datetime.fromisoformat(data['systemModstamp'].replace('Z', '+00:00')) if data.get('systemModstamp') else None,
            external_id_field_name=data.get('externalIdFieldName'),
            concurrency_mode=data.get('concurrencyMode', 'Parallel'),
            content_type=data.get('contentType', 'CSV'),
            api_version=data.get('apiVersion', 'v60.0'),
            number_records_processed=int(data.get('numberRecordsProcessed', 0)),
            number_records_failed=int(data.get('numberRecordsFailed', 0)),
            total_processing_time=int(data.get('totalProcessingTime', 0)),
            api_active_processing_time=int(data.get('apiActiveProcessingTime', 0)),
            apex_processing_time=int(data.get('apexProcessingTime', 0)),
        )


@dataclass
class BulkError:
    """Represents an error for a failed record."""

    fields: List[str]
    message: str
    status_code: str

    @classmethod
    def from_csv_row(cls, row: Dict[str, str]) -> 'BulkError':
        """Create BulkError from CSV error row."""
        return cls(
            fields=row.get('sf__Fields', '').split(',') if row.get('sf__Fields') else [],
            message=row.get('sf__Error', ''),
            status_code=row.get('sf__StatusCode', 'UNKNOWN_ERROR')
        )


@dataclass
class BulkResult:
    """Result of a bulk operation (insert, update, upsert, delete)."""

    job: BulkJob
    success_records: List[Dict[str, Any]]
    failed_records: List[Dict[str, Any]]
    errors: List[BulkError]

    @property
    def success_count(self) -> int:
        """Number of successfully processed records."""
        return len(self.success_records)

    @property
    def failed_count(self) -> int:
        """Number of failed records."""
        return len(self.failed_records)

    @property
    def total_records(self) -> int:
        """Total number of records processed."""
        return self.success_count + self.failed_count

    @property
    def success_rate(self) -> float:
        """Success rate as percentage (0-100)."""
        if self.total_records == 0:
            return 0.0
        return (self.success_count / self.total_records) * 100

    def is_successful(self) -> bool:
        """Check if all records were processed successfully."""
        return self.failed_count == 0

    def to_dict(self) -> Dict[str, Any]:
        """Convert result to dictionary for serialization."""
        return {
            'job_id': self.job.id,
            'success_count': self.success_count,
            'failed_count': self.failed_count,
            'total_records': self.total_records,
            'success_rate': self.success_rate,
            'success_records': self.success_records,
            'failed_records': self.failed_records,
            'errors': [
                {
                    'fields': error.fields,
                    'message': error.message,
                    'status_code': error.status_code
                }
                for error in self.errors
            ]
        }


@dataclass
class BulkQueryResult:
    """Result of a bulk query operation."""

    job: BulkJob
    records: List[Dict[str, Any]]
    locator: Optional[str] = None

    @property
    def record_count(self) -> int:
        """Number of records retrieved."""
        return len(self.records)

    def has_more(self) -> bool:
        """Check if there are more records to fetch."""
        return self.locator is not None

    def to_dict(self) -> Dict[str, Any]:
        """Convert result to dictionary for serialization."""
        return {
            'job_id': self.job.id,
            'record_count': self.record_count,
            'records': self.records,
            'locator': self.locator,
            'has_more': self.has_more()
        }
