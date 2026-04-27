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
Authentication module for Salesforce Toolkit.

Provides multiple authentication strategies for Salesforce:
- JWT Bearer Flow (recommended for production)
- OAuth 2.0 Password Flow (for development/testing)
"""

from kinetic_core.auth.jwt_auth import JWTAuthenticator
from kinetic_core.auth.oauth_auth import OAuthAuthenticator

__all__ = ["JWTAuthenticator", "OAuthAuthenticator"]
