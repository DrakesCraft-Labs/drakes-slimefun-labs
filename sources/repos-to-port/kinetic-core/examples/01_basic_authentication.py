"""
Example 1: Basic Authentication

This example demonstrates how to authenticate to Salesforce using
both JWT Bearer Flow and OAuth Password Flow.
"""

from kinetic_core import JWTAuthenticator, OAuthAuthenticator
from kinetic_core.logging import setup_logger

# Setup logging
logger = setup_logger("example_auth", console_colors=True)


def authenticate_with_jwt():
    """Authenticate using JWT Bearer Flow (recommended for production)."""
    print("\n" + "="*80)
    print("JWT BEARER FLOW AUTHENTICATION")
    print("="*80 + "\n")

    try:
        # Method 1: From environment variables (.env file)
        authenticator = JWTAuthenticator.from_env()

        # Authenticate
        session = authenticator.authenticate()

        print(f"✓ Authentication successful!")
        print(f"  Instance URL: {session.instance_url}")
        print(f"  API Version: {session.api_version}")
        print(f"  Username: {session.username}")
        print(f"  Access Token: {session.access_token[:20]}...")

        return session

    except Exception as e:
        print(f"✗ Authentication failed: {e}")
        return None


def authenticate_with_oauth():
    """Authenticate using OAuth Password Flow."""
    print("\n" + "="*80)
    print("OAUTH PASSWORD FLOW AUTHENTICATION")
    print("="*80 + "\n")

    try:
        # Method 1: From environment variables (.env file)
        authenticator = OAuthAuthenticator.from_env()

        # Authenticate
        session = authenticator.authenticate()

        print(f"✓ Authentication successful!")
        print(f"  Instance URL: {session.instance_url}")
        print(f"  API Version: {session.api_version}")
        print(f"  Username: {session.username}")

        return session

    except Exception as e:
        print(f"✗ Authentication failed: {e}")
        return None


def authenticate_with_jwt_manual():
    """Authenticate using JWT with manual configuration."""
    print("\n" + "="*80)
    print("JWT AUTHENTICATION - MANUAL CONFIGURATION")
    print("="*80 + "\n")

    try:
        # Method 2: Manual configuration
        authenticator = JWTAuthenticator(
            client_id="3MVG9...",  # Your Consumer Key
            username="user@example.com",
            private_key_path="/path/to/server.key",
            login_url="https://test.salesforce.com",  # Sandbox
            api_version="v60.0"
        )

        session = authenticator.authenticate()

        print(f"✓ Authentication successful!")
        return session

    except Exception as e:
        print(f"✗ Authentication failed: {e}")
        return None


def main():
    """Main function."""
    print("\n" + "="*80)
    print("SALESFORCE TOOLKIT - AUTHENTICATION EXAMPLES")
    print("="*80)

    # Example 1: JWT Authentication (recommended)
    jwt_session = authenticate_with_jwt()

    # Example 2: OAuth Authentication
    # oauth_session = authenticate_with_oauth()

    # Example 3: Manual JWT Configuration
    # manual_session = authenticate_with_jwt_manual()

    if jwt_session:
        print("\n✓ All authentication examples completed successfully!")
    else:
        print("\n✗ Authentication failed. Check your credentials.")


if __name__ == "__main__":
    main()
