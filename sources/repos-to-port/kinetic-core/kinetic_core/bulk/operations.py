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
Bulk API v2 operation types.
"""

from enum import Enum


class BulkOperation(str, Enum):
    """Supported Bulk API v2 operations."""

    INSERT = "insert"
    UPDATE = "update"
    UPSERT = "upsert"
    DELETE = "delete"
    HARD_DELETE = "hardDelete"
    QUERY = "query"

    def __str__(self) -> str:
        return self.value

    @classmethod
    def from_string(cls, operation: str) -> 'BulkOperation':
        """
        Convert string to BulkOperation enum.

        Args:
            operation: Operation name as string

        Returns:
            BulkOperation enum value

        Raises:
            ValueError: If operation is not valid
        """
        operation_lower = operation.lower()
        for op in cls:
            if op.value.lower() == operation_lower:
                return op
        raise ValueError(
            f"Invalid bulk operation: {operation}. "
            f"Valid operations: {', '.join([op.value for op in cls])}"
        )

    def requires_id(self) -> bool:
        """Check if operation requires Id field."""
        return self in (self.UPDATE, self.DELETE, self.HARD_DELETE)

    def requires_external_id(self) -> bool:
        """Check if operation requires external ID field."""
        return self == self.UPSERT

    def is_data_operation(self) -> bool:
        """Check if operation involves data modification."""
        return self != self.QUERY

    def is_query_operation(self) -> bool:
        """Check if operation is a query."""
        return self == self.QUERY
