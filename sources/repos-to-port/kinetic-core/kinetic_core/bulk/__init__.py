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
Salesforce Bulk API v2 module.

This module provides native support for Salesforce Bulk API v2,
enabling efficient processing of millions of records.

Features:
- Bulk insert, update, upsert, delete operations
- Bulk query for large data exports
- Automatic job polling and status tracking
- CSV serialization/deserialization
- Detailed error reporting per record
- Progress callbacks and async support
"""

from .client import BulkV2Client
from .job import BulkJob, BulkResult, BulkQueryResult
from .operations import BulkOperation

__all__ = [
    'BulkV2Client',
    'BulkJob',
    'BulkResult',
    'BulkQueryResult',
    'BulkOperation',
]
