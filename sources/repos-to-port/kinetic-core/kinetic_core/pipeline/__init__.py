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
Pipeline module for Salesforce Toolkit.

Provides ETL pipeline framework for data synchronization.
"""

from kinetic_core.pipeline.sync_pipeline import (
    SyncPipeline,
    SyncMode,
    SyncStatus,
    SyncResult
)

__all__ = ["SyncPipeline", "SyncMode", "SyncStatus", "SyncResult"]
