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
Salesforce Toolkit - A comprehensive Python library for Salesforce integration.

This toolkit provides a flexible, configuration-driven framework for:
- Authentication (JWT Bearer Flow, OAuth Password Flow)
- CRUD operations on any Salesforce object
- Bulk API v2 for high-volume data operations
- Metadata API for configuration management
- Field mapping and data transformation
- ETL pipelines for data synchronization
- Comprehensive logging and error handling

Author: Antonio Trento
License: MIT
"""

__version__ = "2.1.1"
__author__ = "Antonio Trento"

from kinetic_core.auth.jwt_auth import JWTAuthenticator
from kinetic_core.auth.oauth_auth import OAuthAuthenticator
from kinetic_core.core.session import SalesforceSession
from kinetic_core.core.client import SalesforceClient
from kinetic_core.mapping.field_mapper import FieldMapper
from kinetic_core.pipeline.sync_pipeline import SyncPipeline, SyncMode
from kinetic_core.bulk import BulkV2Client, BulkJob, BulkResult, BulkQueryResult, BulkOperation
from kinetic_core.metadata import MetadataClient, CustomField, CustomObject

__all__ = [
    "JWTAuthenticator",
    "OAuthAuthenticator",
    "SalesforceSession",
    "SalesforceClient",
    "FieldMapper",
    "SyncPipeline",
    "SyncMode",
    "BulkV2Client",
    "BulkJob",
    "BulkResult",
    "BulkQueryResult",
    "BulkOperation",
    "MetadataClient",
    "CustomField",
    "CustomObject",
]
