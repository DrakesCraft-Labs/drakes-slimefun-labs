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
OAuth 2.0 Password Flow Authentication for Salesforce.

Implements username/password authentication with consumer key/secret.
Note: This method is less secure than JWT and requires a security token.
"""

import logging
from typing import Tuple

import requests

from kinetic_core.core.session import SalesforceSession
import os


logger = logging.getLogger(__name__)


class OAuthAuthenticator:
    """
    Authenticates to Salesforce using OAuth 2.0 Password Flow.

    Note: This authentication method is less secure than JWT Bearer Flow
    and requires storing passwords. Consider using JWT for production.

    Requirements:
        - Connected App with OAuth enabled
        - Consumer Key and Consumer Secret
        - Username, password, and security token

    Example:
        ```python
        authenticator = OAuthAuthenticator(
            client_id="3MVG9...",
            client_secret="12345...",
            username="user@example.com",
            password="mypassword",
            security_token="ABC123",
            login_url="https://test.salesforce.com"
        )

        session = authenticator.authenticate()
        ```
    """

    def __init__(
        self,
        client_id: str,
        client_secret: str,
        username: str,
        password: str,
        security_token: str = "",
        login_url: str = "https://login.salesforce.com",
        api_version: str = os.getenv("SF_API_VERSION", "v62.0"),
    ):
        """
        Initialize OAuth authenticator.

        Args:
            client_id: Consumer Key from Connected App
            client_secret: Consumer Secret from Connected App
            username: Salesforce username
            password: Salesforce password
            security_token: Salesforce security token (append to password if required)
            login_url: Salesforce login URL
            api_version: Salesforce API version
        """
        self.client_id = client_id
        self.client_secret = client_secret
        self.username = username
        self.password = password
        self.security_token = security_token
        self.login_url = login_url.rstrip('/')
        self.api_version = api_version

    def authenticate(self) -> SalesforceSession:
        """
        Authenticate to Salesforce and return a session.

        Returns:
            SalesforceSession: Authenticated session object

        Raises:
            requests.HTTPError: If authentication request fails
            RuntimeError: If authentication fails for any other reason
        """
        try:
            logger.info(f"OAuth password flow authentication for: {self.username}")

            # Request access token
            access_token, instance_url = self._request_access_token()

            # Create session
            session = SalesforceSession(
                instance_url=instance_url,
                access_token=access_token,
                api_version=self.api_version,
                username=self.username
            )

            logger.info(f"✓ OAuth authentication successful: {instance_url}")
            return session

        except requests.HTTPError as e:
            logger.error(f"✗ Authentication HTTP error: {e}")
            raise
        except Exception as e:
            logger.error(f"✗ Authentication failed: {e}")
            raise RuntimeError(f"OAuth authentication failed: {e}") from e

    def _request_access_token(self) -> Tuple[str, str]:
        """
        Request access token using OAuth password flow.

        Returns:
            Tuple[str, str]: (access_token, instance_url)

        Raises:
            requests.HTTPError: If request fails
            RuntimeError: If response doesn't contain required data
        """
        token_url = f"{self.login_url}/services/oauth2/token"

        # Combine password with security token
        full_password = f"{self.password}{self.security_token}"

        data = {
            'grant_type': 'password',
            'client_id': self.client_id,
            'client_secret': self.client_secret,
            'username': self.username,
            'password': full_password
        }

        logger.debug(f"Requesting token from: {token_url}")

        response = requests.post(token_url, data=data, timeout=30)

        if response.status_code != 200:
            error_detail = self._extract_error_detail(response)
            logger.error(f"✗ Token request failed: {error_detail}")
            response.raise_for_status()

        response_data = response.json()

        access_token = response_data.get('access_token')
        instance_url = response_data.get('instance_url')

        if not access_token or not instance_url:
            raise RuntimeError(
                f"Invalid response from Salesforce: {response_data}"
            )

        return access_token, instance_url

    def _extract_error_detail(self, response: requests.Response) -> str:
        """
        Extract error details from failed authentication response.

        Args:
            response: Failed HTTP response

        Returns:
            str: Error detail message
        """
        try:
            error_data = response.json()
            error = error_data.get('error', 'unknown')
            error_description = error_data.get('error_description', 'No description')
            return f"{error}: {error_description}"
        except Exception:
            return response.text

    @classmethod
    def from_env(cls, api_version: str = os.getenv("SF_API_VERSION", "v62.0")) -> "OAuthAuthenticator":
        """
        Create authenticator from environment variables.

        Expected environment variables:
            - SF_CLIENT_ID: Consumer Key
            - SF_CLIENT_SECRET: Consumer Secret
            - SF_USERNAME: Salesforce username
            - SF_PASSWORD: Salesforce password
            - SF_SECURITY_TOKEN: Security token (optional)
            - SF_LOGIN_URL: Login URL (optional)

        Args:
            api_version: Salesforce API version

        Returns:
            OAuthAuthenticator: Configured authenticator

        Raises:
            ValueError: If required environment variables are missing
        """
        import os.path # os is already imported at top level, but let's keep this clean or remove it if redundant? 
        # The file has 'import os' inside the function in the original. I added 'import os' at top. 
        # I should simply remove the inner import or leave it (it's harmless but redundant). 
        # The original code had:
        # import os
        # from dotenv import load_dotenv
        
        from dotenv import load_dotenv

        load_dotenv()

        client_id = os.getenv("SF_CLIENT_ID")
        client_secret = os.getenv("SF_CLIENT_SECRET")
        username = os.getenv("SF_USERNAME")
        password = os.getenv("SF_PASSWORD")
        security_token = os.getenv("SF_SECURITY_TOKEN", "")
        login_url = os.getenv("SF_LOGIN_URL", "https://login.salesforce.com")

        if not all([client_id, client_secret, username, password]):
            raise ValueError(
                "Missing required environment variables: "
                "SF_CLIENT_ID, SF_CLIENT_SECRET, SF_USERNAME, SF_PASSWORD"
            )

        return cls(
            client_id=client_id,
            client_secret=client_secret,
            username=username,
            password=password,
            security_token=security_token,
            login_url=login_url,
            api_version=api_version
        )
