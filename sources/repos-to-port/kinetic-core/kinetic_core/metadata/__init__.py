"""
Salesforce Metadata API module for kinetic-core.

This module provides support for Salesforce Metadata API operations:
- Retrieve metadata from Salesforce orgs
- Deploy metadata to Salesforce orgs
- Create custom objects and fields
- Manage Salesforce configuration as code
- Compare metadata between sources
- Use pre-built templates for common configurations

Example:
    >>> from kinetic_core import SalesforceClient
    >>> client = SalesforceClient(session)
    >>>
    >>> # Retrieve metadata
    >>> result = client.metadata.retrieve(
    ...     component_types=["CustomObject", "CustomField"],
    ...     output_dir="./metadata"
    ... )
    >>>
    >>> # Deploy metadata
    >>> result = client.metadata.deploy(
    ...     source_dir="./metadata",
    ...     run_tests=True
    ... )
    >>>
    >>> # Compare metadata
    >>> diff = client.metadata.compare(
    ...     source_dir="./source",
    ...     target_dir="./target"
    ... )
    >>>
    >>> # Use templates
    >>> from kinetic_core.metadata import templates
    >>> fields = templates.get_template("enterprise_crm", sobject="Account")
"""

from kinetic_core.metadata.models import (
    CustomField,
    CustomObject,
    ValidationRule,
    WorkflowRule,
    PicklistValue,
    FieldType,
    SharingModel,
)
from kinetic_core.metadata.client import MetadataClient
from kinetic_core.metadata.comparator import MetadataComparator, MetadataDiff
from kinetic_core.metadata import templates

__all__ = [
    "MetadataClient",
    "CustomField",
    "CustomObject",
    "ValidationRule",
    "WorkflowRule",
    "PicklistValue",
    "FieldType",
    "SharingModel",
    "MetadataComparator",
    "MetadataDiff",
    "templates",
]
