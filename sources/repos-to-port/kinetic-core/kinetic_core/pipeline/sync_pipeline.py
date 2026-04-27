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
Sync Pipeline - Orchestrate data synchronization between external sources and Salesforce.

Provides a flexible, configuration-driven framework for ETL operations:
- Read from any source (database, CSV, API, etc.)
- Transform data using FieldMapper
- Load to Salesforce with error handling
- Track progress and log operations
"""

import logging
from typing import Dict, List, Any, Optional, Callable
from dataclasses import dataclass, field
from enum import Enum


logger = logging.getLogger(__name__)


class SyncMode(Enum):
    """Synchronization modes."""
    INSERT = "insert"  # Create new records only
    UPDATE = "update"  # Update existing records only
    UPSERT = "upsert"  # Insert or update based on external ID
    DELETE = "delete"  # Delete records


class SyncStatus(Enum):
    """Synchronization status."""
    PENDING = "pending"
    IN_PROGRESS = "in_progress"
    SUCCESS = "success"
    FAILED = "failed"
    PARTIAL = "partial"  # Some records succeeded, some failed


@dataclass
class SyncResult:
    """
    Result of a synchronization operation.

    Attributes:
        status: Overall sync status
        total_records: Total number of records processed
        success_count: Number of successful operations
        error_count: Number of failed operations
        errors: List of error details
        salesforce_ids: List of created/updated Salesforce IDs
        metadata: Additional metadata (timing, etc.)
    """
    status: SyncStatus
    total_records: int = 0
    success_count: int = 0
    error_count: int = 0
    errors: List[Dict[str, Any]] = field(default_factory=list)
    salesforce_ids: List[str] = field(default_factory=list)
    metadata: Dict[str, Any] = field(default_factory=dict)

    @property
    def success_rate(self) -> float:
        """Calculate success rate percentage."""
        if self.total_records == 0:
            return 0.0
        return (self.success_count / self.total_records) * 100

    def add_success(self, salesforce_id: str) -> None:
        """Record a successful operation."""
        self.success_count += 1
        self.salesforce_ids.append(salesforce_id)

    def add_error(self, record_data: Any, error: str) -> None:
        """Record a failed operation."""
        self.error_count += 1
        self.errors.append({
            "record": record_data,
            "error": str(error)
        })

    def __str__(self) -> str:
        """String representation of sync result."""
        return (
            f"SyncResult(status={self.status.value}, "
            f"total={self.total_records}, "
            f"success={self.success_count}, "
            f"errors={self.error_count}, "
            f"rate={self.success_rate:.1f}%)"
        )


class SyncPipeline:
    """
    Generic synchronization pipeline for Salesforce integration.

    This pipeline provides a framework for:
    1. Extracting data from any source
    2. Transforming data using FieldMapper
    3. Loading data to Salesforce
    4. Tracking progress and errors

    Example:
        ```python
        from kinetic_core import JWTAuthenticator, SalesforceClient, FieldMapper, SyncPipeline

        # Setup
        auth = JWTAuthenticator.from_env()
        session = auth.authenticate()
        client = SalesforceClient(session)

        mapper = FieldMapper({
            "customer_name": "Name",
            "customer_email": "Email"
        })

        # Create pipeline
        pipeline = SyncPipeline(
            client=client,
            sobject="Account",
            mapper=mapper,
            mode=SyncMode.INSERT
        )

        # Sync data
        source_data = [
            {"customer_name": "ACME", "customer_email": "info@acme.com"},
            {"customer_name": "Globex", "customer_email": "contact@globex.com"}
        ]

        result = pipeline.sync(source_data)
        print(f"Synced {result.success_count}/{result.total_records} records")
        ```
    """

    def __init__(
        self,
        client: "SalesforceClient",
        sobject: str,
        mapper: Optional["FieldMapper"] = None,
        mode: SyncMode = SyncMode.INSERT,
        external_id_field: Optional[str] = None,
        batch_size: int = 200,
        stop_on_error: bool = False,
        callbacks: Optional[Dict[str, Callable]] = None,
    ):
        """
        Initialize sync pipeline.

        Args:
            client: Salesforce client instance
            sobject: Salesforce object API name (e.g., 'Account', 'Contact')
            mapper: Optional FieldMapper for data transformation
            mode: Sync mode (INSERT, UPDATE, UPSERT, DELETE)
            external_id_field: External ID field name (required for UPSERT mode)
            batch_size: Number of records to process per batch
            stop_on_error: If True, stop on first error; if False, continue and collect errors
            callbacks: Optional callbacks for lifecycle events:
                - on_record_start: Called before processing each record
                - on_record_success: Called after successful processing
                - on_record_error: Called after failed processing
                - on_batch_complete: Called after each batch
        """
        self.client = client
        self.sobject = sobject
        self.mapper = mapper
        self.mode = mode
        self.external_id_field = external_id_field
        self.batch_size = batch_size
        self.stop_on_error = stop_on_error
        self.callbacks = callbacks or {}
        self.logger = logger

        # Validate configuration
        self._validate_config()

    def _validate_config(self) -> None:
        """Validate pipeline configuration."""
        if self.mode == SyncMode.UPSERT and not self.external_id_field:
            raise ValueError("external_id_field is required for UPSERT mode")

    def sync(self, source_data: List[Dict[str, Any]]) -> SyncResult:
        """
        Synchronize data from source to Salesforce.

        Args:
            source_data: List of source data dictionaries

        Returns:
            SyncResult: Result of synchronization operation

        Example:
            ```python
            source = [
                {"name": "Alice", "email": "alice@example.com"},
                {"name": "Bob", "email": "bob@example.com"}
            ]

            result = pipeline.sync(source)

            if result.status == SyncStatus.SUCCESS:
                print("All records synced successfully!")
            else:
                for error in result.errors:
                    print(f"Error: {error}")
            ```
        """
        import time

        start_time = time.time()

        result = SyncResult(
            status=SyncStatus.IN_PROGRESS,
            total_records=len(source_data)
        )

        self.logger.info(
            f"Starting sync: {result.total_records} records to {self.sobject} "
            f"(mode: {self.mode.value})"
        )

        # Process records in batches
        for i in range(0, len(source_data), self.batch_size):
            batch = source_data[i:i + self.batch_size]
            batch_num = (i // self.batch_size) + 1
            total_batches = (len(source_data) + self.batch_size - 1) // self.batch_size

            self.logger.info(f"Processing batch {batch_num}/{total_batches} ({len(batch)} records)")

            self._process_batch(batch, result)

            # Call batch complete callback
            if "on_batch_complete" in self.callbacks:
                self.callbacks["on_batch_complete"](batch_num, total_batches, result)

            # Stop if requested
            if self.stop_on_error and result.error_count > 0:
                self.logger.warning("Stopping sync due to error (stop_on_error=True)")
                break

        # Determine final status
        if result.error_count == 0:
            result.status = SyncStatus.SUCCESS
        elif result.success_count == 0:
            result.status = SyncStatus.FAILED
        else:
            result.status = SyncStatus.PARTIAL

        # Add metadata
        elapsed_time = time.time() - start_time
        result.metadata = {
            "elapsed_seconds": round(elapsed_time, 2),
            "records_per_second": round(result.total_records / elapsed_time, 2) if elapsed_time > 0 else 0,
            "mode": self.mode.value,
            "sobject": self.sobject
        }

        self.logger.info(
            f"✓ Sync complete: {result.status.value} - "
            f"{result.success_count} success, {result.error_count} errors - "
            f"{elapsed_time:.2f}s"
        )

        return result

    def _process_batch(self, batch: List[Dict[str, Any]], result: SyncResult) -> None:
        """
        Process a batch of records.

        Args:
            batch: Batch of source records
            result: SyncResult to update (modified in place)
        """
        for record in batch:
            try:
                # Call start callback
                if "on_record_start" in self.callbacks:
                    self.callbacks["on_record_start"](record)

                # Transform data if mapper is provided
                if self.mapper:
                    transformed = self.mapper.transform(record)
                else:
                    transformed = record

                # Sync to Salesforce based on mode
                salesforce_id = self._sync_record(transformed, record)

                # Record success
                result.add_success(salesforce_id)

                # Call success callback
                if "on_record_success" in self.callbacks:
                    self.callbacks["on_record_success"](record, salesforce_id)

            except Exception as e:
                # Record error
                result.add_error(record, str(e))

                self.logger.error(f"Error processing record: {e}")

                # Call error callback
                if "on_record_error" in self.callbacks:
                    self.callbacks["on_record_error"](record, e)

                # Stop if requested
                if self.stop_on_error:
                    raise

    def _sync_record(self, transformed_data: Dict[str, Any], original_data: Dict[str, Any]) -> str:
        """
        Sync a single record to Salesforce based on mode.

        Args:
            transformed_data: Transformed record data (for Salesforce)
            original_data: Original record data (for reference)

        Returns:
            str: Salesforce ID

        Raises:
            Exception: If sync operation fails
        """
        if self.mode == SyncMode.INSERT:
            return self.client.create(self.sobject, transformed_data)

        elif self.mode == SyncMode.UPDATE:
            # For UPDATE, expect 'Id' field in transformed data
            record_id = transformed_data.pop('Id', None)
            if not record_id:
                raise ValueError("UPDATE mode requires 'Id' field in data")
            self.client.update(self.sobject, record_id, transformed_data)
            return record_id

        elif self.mode == SyncMode.UPSERT:
            # For UPSERT, use external ID field
            external_id_value = transformed_data.get(self.external_id_field)
            if not external_id_value:
                raise ValueError(
                    f"UPSERT mode requires '{self.external_id_field}' field in data"
                )
            return self.client.upsert(
                self.sobject,
                self.external_id_field,
                str(external_id_value),
                transformed_data
            )

        elif self.mode == SyncMode.DELETE:
            # For DELETE, expect 'Id' field
            record_id = transformed_data.get('Id')
            if not record_id:
                raise ValueError("DELETE mode requires 'Id' field in data")
            self.client.delete(self.sobject, record_id)
            return record_id

        else:
            raise ValueError(f"Unsupported sync mode: {self.mode}")

    @classmethod
    def from_config(cls, config: Dict[str, Any], client: "SalesforceClient") -> "SyncPipeline":
        """
        Create pipeline from configuration dictionary.

        Args:
            config: Configuration dictionary with keys:
                - sobject: Salesforce object name (required)
                - mode: Sync mode (default: 'insert')
                - external_id_field: External ID field for upsert
                - batch_size: Batch size (default: 200)
                - stop_on_error: Stop on error flag (default: False)
                - mapping: Field mapping configuration (for FieldMapper)
            client: Salesforce client instance

        Returns:
            SyncPipeline: Configured pipeline

        Example:
            ```python
            config = {
                "sobject": "Account",
                "mode": "upsert",
                "external_id_field": "External_Key__c",
                "batch_size": 100,
                "mapping": {
                    "customer_name": "Name",
                    "customer_email": "Email"
                }
            }

            pipeline = SyncPipeline.from_config(config, client)
            ```
        """
        from kinetic_core.mapping import FieldMapper

        sobject = config.get("sobject")
        if not sobject:
            raise ValueError("'sobject' is required in config")

        mode_str = config.get("mode", "insert")
        mode = SyncMode(mode_str)

        external_id_field = config.get("external_id_field")
        batch_size = config.get("batch_size", 200)
        stop_on_error = config.get("stop_on_error", False)

        # Create mapper if mapping is provided
        mapper = None
        if "mapping" in config:
            mapper = FieldMapper(config["mapping"])

        return cls(
            client=client,
            sobject=sobject,
            mapper=mapper,
            mode=mode,
            external_id_field=external_id_field,
            batch_size=batch_size,
            stop_on_error=stop_on_error
        )
