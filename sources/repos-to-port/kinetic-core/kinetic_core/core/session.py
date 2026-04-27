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
Salesforce Session Management.

Provides a unified session object for authenticated Salesforce connections.
"""

from dataclasses import dataclass
from typing import Optional


import os

@dataclass
class SalesforceSession:
    """
    Represents an authenticated Salesforce session.

    This session object is authentication-agnostic and can be used with
    any authentication method (JWT, OAuth, etc.).

    Attributes:
        instance_url: Salesforce instance URL (e.g., https://xxx.my.salesforce.com)
        access_token: OAuth access token
        api_version: Salesforce API version (default: v62.0 or SF_API_VERSION env var)
        username: Optional username for logging/debugging
        org_id: Optional Salesforce organization ID

    Example:
        ```python
        session = SalesforceSession(
            instance_url="https://myorg.my.salesforce.com",
            access_token="00D...!AR...",
            api_version="v62.0"
        )
        ```
    """

    instance_url: str
    access_token: str
    api_version: str = os.getenv("SF_API_VERSION", "v62.0")
    username: Optional[str] = None
    org_id: Optional[str] = None

    def __post_init__(self):
        """Validate and normalize session data."""
        # Remove trailing slash from instance_url
        self.instance_url = self.instance_url.rstrip('/')

        # Ensure api_version has 'v' prefix
        if not self.api_version.startswith('v'):
            self.api_version = f"v{self.api_version}"

    @property
    def base_url(self) -> str:
        """
        Get the base API URL for Salesforce REST API.

        Returns:
            str: Base URL (e.g., https://xxx.my.salesforce.com/services/data/v60.0)
        """
        return f"{self.instance_url}/services/data/{self.api_version}"

    @property
    def auth_header(self) -> dict:
        """
        Get the authorization header for API requests.

        Returns:
            dict: Authorization header with Bearer token
        """
        return {"Authorization": f"Bearer {self.access_token}"}

    def is_valid(self) -> bool:
        """
        Check if the session has required credentials.

        Returns:
            bool: True if session has instance_url and access_token
        """
        return bool(self.instance_url and self.access_token)

    def __repr__(self) -> str:
        """String representation (masks access token for security)."""
        token_preview = f"{self.access_token[:10]}..." if self.access_token else "None"
        return (
            f"SalesforceSession("
            f"instance_url='{self.instance_url}', "
            f"api_version='{self.api_version}', "
            f"username='{self.username}', "
            f"access_token='{token_preview}')"
        )
