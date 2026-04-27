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
JWT Bearer Flow Authentication for Salesforce.

Implements secure server-to-server authentication using JWT tokens
signed with RSA private keys.
"""

import time
import logging
from typing import Tuple, Optional
from pathlib import Path

import jwt
import requests

from kinetic_core.core.session import SalesforceSession
import os


logger = logging.getLogger(__name__)


class JWTAuthenticator:
    """
    Authenticates to Salesforce using JWT Bearer Flow.

    This is the recommended authentication method for server-to-server
    integrations as it doesn't require storing passwords.

    Requirements:
        - Connected App in Salesforce with JWT Bearer Flow enabled
        - RSA key pair (private key for signing JWT)
        - Consumer Key (Client ID) from Connected App
        - Pre-authorized username

    Example:
        ```python
        authenticator = JWTAuthenticator(
            client_id="3MVG9...",
            username="user@example.com",
            private_key_path="/path/to/server.key",
            login_url="https://test.salesforce.com"
        )

        session = authenticator.authenticate()
        print(f"Authenticated: {session.instance_url}")
        ```
    """

    def __init__(
        self,
        client_id: str,
        username: str,
        private_key_path: str,
        login_url: str = "https://login.salesforce.com",
        api_version: str = os.getenv("SF_API_VERSION", "v62.0"),
    ):
        """
        Initialize JWT authenticator.

        Args:
            client_id: Consumer Key from Salesforce Connected App
            username: Salesforce username to authenticate as
            private_key_path: Path to RSA private key file
            login_url: Salesforce login URL (default: production, use
                      https://test.salesforce.com for sandboxes)
            api_version: Salesforce API version (default: v60.0)
        """
        self.client_id = client_id
        self.username = username
        self.private_key_path = Path(private_key_path)
        self.login_url = login_url.rstrip('/')
        self.api_version = api_version

    def authenticate(self) -> SalesforceSession:
        """
        Authenticate to Salesforce and return a session.

        Returns:
            SalesforceSession: Authenticated session object

        Raises:
            FileNotFoundError: If private key file not found
            jwt.PyJWTError: If JWT encoding fails
            requests.HTTPError: If authentication request fails
            RuntimeError: If authentication fails for any other reason
        """
        try:
            logger.info(f"JWT authentication for user: {self.username}")

            # Read private key
            private_key = self._read_private_key()

            # Create JWT claim
            claim = self._build_jwt_claim()

            # Sign JWT with private key
            assertion = jwt.encode(claim, private_key, algorithm='RS256')

            # Request access token
            access_token, instance_url = self._request_access_token(assertion)

            # Create session
            session = SalesforceSession(
                instance_url=instance_url,
                access_token=access_token,
                api_version=self.api_version,
                username=self.username
            )

            logger.info(f"✓ JWT authentication successful: {instance_url}")
            return session

        except FileNotFoundError:
            logger.error(f"✗ Private key file not found: {self.private_key_path}")
            raise
        except jwt.PyJWTError as e:
            logger.error(f"✗ JWT encoding error: {e}")
            raise
        except requests.HTTPError as e:
            logger.error(f"✗ Authentication HTTP error: {e}")
            raise
        except Exception as e:
            logger.error(f"✗ Authentication failed: {e}")
            raise RuntimeError(f"JWT authentication failed: {e}") from e

    def _read_private_key(self) -> str:
        """
        Read the RSA private key from file.

        Returns:
            str: Private key content

        Raises:
            FileNotFoundError: If key file doesn't exist
        """
        if not self.private_key_path.exists():
            raise FileNotFoundError(
                f"Private key file not found: {self.private_key_path}"
            )

        with open(self.private_key_path, 'r') as key_file:
            return key_file.read()

    def _build_jwt_claim(self) -> dict:
        """
        Build JWT claim for Salesforce authentication.

        Returns:
            dict: JWT claim with iss, sub, aud, exp
        """
        return {
            'iss': self.client_id,  # Issuer: Consumer Key
            'sub': self.username,   # Subject: Username
            'aud': self.login_url,  # Audience: Login URL
            'exp': int(time.time()) + 300  # Expiration: 5 minutes
        }

    def _request_access_token(self, assertion: str) -> Tuple[str, str]:
        """
        Request access token from Salesforce using JWT assertion.

        Args:
            assertion: Signed JWT token

        Returns:
            Tuple[str, str]: (access_token, instance_url)

        Raises:
            requests.HTTPError: If request fails
            RuntimeError: If response doesn't contain required data
        """
        token_url = f"{self.login_url}/services/oauth2/token"

        data = {
            'grant_type': 'urn:ietf:params:oauth:grant-type:jwt-bearer',
            'assertion': assertion
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
    def from_env(cls, api_version: str = os.getenv("SF_API_VERSION", "v62.0")) -> "JWTAuthenticator":
        """
        Create authenticator from environment variables.

        Expected environment variables:
            - SF_CLIENT_ID: Consumer Key
            - SF_USERNAME: Salesforce username
            - SF_PRIVATE_KEY_PATH: Path to private key
            - SF_LOGIN_URL: Login URL (optional, defaults to production)

        Args:
            api_version: Salesforce API version

        Returns:
            JWTAuthenticator: Configured authenticator

        Raises:
            ValueError: If required environment variables are missing
        """
        from dotenv import load_dotenv

        load_dotenv()

        client_id = os.getenv("SF_CLIENT_ID")
        username = os.getenv("SF_USERNAME")
        private_key_path = os.getenv("SF_PRIVATE_KEY_PATH")
        login_url = os.getenv("SF_LOGIN_URL", "https://login.salesforce.com")

        if not all([client_id, username, private_key_path]):
            raise ValueError(
                "Missing required environment variables: "
                "SF_CLIENT_ID, SF_USERNAME, SF_PRIVATE_KEY_PATH"
            )

        return cls(
            client_id=client_id,
            username=username,
            private_key_path=private_key_path,
            login_url=login_url,
            api_version=api_version
        )
