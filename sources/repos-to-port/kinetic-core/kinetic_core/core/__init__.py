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
Core module for Salesforce Toolkit.

Provides the fundamental building blocks for Salesforce integration:
- Session management
- HTTP client for API requests
- CRUD operations on Salesforce objects
"""

from kinetic_core.core.session import SalesforceSession
from kinetic_core.core.client import SalesforceClient

__all__ = ["SalesforceSession", "SalesforceClient"]
