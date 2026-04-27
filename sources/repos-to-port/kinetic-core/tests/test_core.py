import pytest
from unittest import mock
from kinetic_core import SalesforceClient

@pytest.fixture
def mock_session():
    session = mock.Mock()
    session.instance_url = "https://mock.salesforce.com"
    session.access_token = "mock_token"
    session.api_version = "v57.0"
    # Mock the base_url property behavior
    session.base_url = "https://mock.salesforce.com/services/data/v57.0"
    return session

def test_client_init(mock_session):
    """Test SalesforceClient initialization."""
    client = SalesforceClient(mock_session)
    assert client.session == mock_session
    # Now this works because we set it on the mock
    assert client.session.base_url == "https://mock.salesforce.com/services/data/v57.0"

@mock.patch("kinetic_core.core.client.requests.post")
def test_create_record(mock_post, mock_session):
    """Test creating a record."""
    # Setup mock session header
    mock_session.auth_header = {"Authorization": "Bearer mock_token"}
    
    # Setup mock response
    mock_response = mock.Mock()
    mock_response.status_code = 201
    mock_response.json.return_value = {"id": "001XXXXXXXXXXXX"}
    mock_post.return_value = mock_response

    client = SalesforceClient(mock_session)
    record_id = client.create("Account", {"Name": "Test Account"})
    
    assert record_id == "001XXXXXXXXXXXX"
    mock_post.assert_called_once()
    
    # Verify proper URL and headers were used
    args, kwargs = mock_post.call_args
    assert args[0] == "https://mock.salesforce.com/services/data/v57.0/sobjects/Account/"
    assert kwargs["headers"]["Authorization"] == "Bearer mock_token"
    assert kwargs["json"] == {"Name": "Test Account"}

@mock.patch("kinetic_core.core.client.requests.get")
def test_query_records(mock_get, mock_session):
    """Test querying records."""
    # Setup mock response
    mock_response = mock.Mock()
    mock_response.status_code = 200
    mock_response.json.return_value = {
        "done": True,
        "totalSize": 1,
        "records": [{"Id": "001XXX", "Name": "Test"}]
    }
    mock_get.return_value = mock_response

    client = SalesforceClient(mock_session)
    results = client.query("SELECT Id, Name FROM Account")
    
    assert len(results) == 1
    assert results[0]["Name"] == "Test"
    mock_get.assert_called_once()
