import pytest
from unittest import mock
from pathlib import Path
from kinetic_core import JWTAuthenticator, OAuthAuthenticator

@pytest.fixture
def mock_path_exists():
    with mock.patch("kinetic_core.auth.jwt_auth.Path.exists") as mock_exists:
        mock_exists.return_value = True
        yield mock_exists

def test_jwt_authenticator_init(mock_path_exists):
    """Test JWTAuthenticator initialization."""
    auth = JWTAuthenticator(
        client_id="test_id",
        username="test@example.com",
        private_key_path="/path/to/server.key",
    )
    assert auth.client_id == "test_id"
    assert auth.username == "test@example.com"
    assert str(auth.private_key_path) == str(Path("/path/to/server.key"))

def test_jwt_from_env():
    """Test initializing JWTAuthenticator from environment."""
    env_vars = {
        "SF_CLIENT_ID": "env_client_id",
        "SF_USERNAME": "env@example.com",
        "SF_PRIVATE_KEY_PATH": "/env/path/key",
        "SF_LOGIN_URL": "https://test.salesforce.com"
    }
    
    with mock.patch.dict("os.environ", env_vars), \
         mock.patch("kinetic_core.auth.jwt_auth.Path.exists") as mock_exists:
        mock_exists.return_value = True
        
        auth = JWTAuthenticator.from_env()
        assert auth.client_id == "env_client_id"
        assert auth.username == "env@example.com"

def test_oauth_authenticator_init():
    """Test OAuthAuthenticator initialization."""
    auth = OAuthAuthenticator(
        client_id="test_id",
        client_secret="test_secret",
        username="test@example.com",
        password="password",
        security_token="token"
    )
    assert auth.client_id == "test_id"
    assert auth.client_secret == "test_secret"
    assert auth.username == "test@example.com"
