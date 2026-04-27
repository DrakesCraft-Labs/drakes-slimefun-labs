# Authentication - Complete Reference

Complete guide to Salesforce authentication in Kinetic Core.

## Overview

Kinetic Core supports two authentication methods:
1. **JWT Bearer Flow** (Recommended for production)
2. **OAuth 2.0 Password Flow** (For development/testing)

Both methods provide a `SalesforceSession` object used by the `SalesforceClient`.

---

## JWT Bearer Flow (Recommended)

### Why JWT?

✅ **More Secure**: No password storage required
✅ **Production-Ready**: Recommended by Salesforce
✅ **No User Interaction**: Fully automated
✅ **Longer Sessions**: 2-hour token validity
✅ **Best for**: Servers, batch jobs, integrations

### Prerequisites

1. **Salesforce Connected App** with:
   - OAuth settings enabled
   - JWT Bearer Flow enabled
   - Certificate uploaded
   - Appropriate scopes granted

2. **Digital Certificate**:
   - Private key (.key file)
   - Certificate (.crt file) uploaded to Salesforce

3. **User with API Access**:
   - API Enabled permission
   - Assigned to Connected App

### Quick Setup

#### 1. Generate Certificate

```bash
# Generate private key
openssl genrsa -out server.key 2048

# Generate certificate
openssl req -new -x509 -key server.key -out server.crt -days 365

# Optional: Generate PKCS12 for backup
openssl pkcs12 -export -in server.crt -inkey server.key -out server.p12
```

#### 2. Configure Connected App

1. Setup → App Manager → New Connected App
2. Enable OAuth Settings
3. Enable "Use Digital Signatures"
4. Upload `server.crt`
5. Add scopes: `api`, `refresh_token`, `offline_access`
6. Save and note the **Consumer Key** (Client ID)

#### 3. Environment Variables

Create `.env` file:

```bash
SF_CLIENT_ID=3MVG9...your_consumer_key
SF_USERNAME=integration@company.com.sandbox
SF_PRIVATE_KEY_PATH=/path/to/server.key
SF_LOGIN_URL=https://test.salesforce.com  # or https://login.salesforce.com
```

### Usage

#### From Environment Variables (Recommended)

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

# Load from .env
auth = JWTAuthenticator.from_env()

# Authenticate
session = auth.authenticate()

# Create client
client = SalesforceClient(session)
```

#### Manual Configuration

```python
from kinetic_core import JWTAuthenticator

auth = JWTAuthenticator(
    client_id="3MVG9...",
    username="user@example.com",
    private_key_path="/path/to/server.key",
    login_url="https://test.salesforce.com"
)

session = auth.authenticate()
```

#### Using Private Key String

```python
private_key_content = """
-----BEGIN RSA PRIVATE KEY-----
MIIEpAIBAAKCAQEA...
-----END RSA PRIVATE KEY-----
"""

auth = JWTAuthenticator(
    client_id="3MVG9...",
    username="user@example.com",
    private_key=private_key_content,  # Use key content instead of path
    login_url="https://test.salesforce.com"
)

session = auth.authenticate()
```

### API Reference

#### JWTAuthenticator Class

```python
class JWTAuthenticator:
    """JWT Bearer Flow authenticator"""

    def __init__(
        self,
        client_id: str,
        username: str,
        private_key: Optional[str] = None,
        private_key_path: Optional[str] = None,
        login_url: str = "https://login.salesforce.com"
    )
```

**Parameters**:
- `client_id` (str): Connected App Consumer Key
- `username` (str): Salesforce username
- `private_key` (str, optional): Private key content as string
- `private_key_path` (str, optional): Path to private key file
- `login_url` (str): Salesforce login URL (production or sandbox)

**Note**: Must provide either `private_key` OR `private_key_path`

#### Methods

##### from_env()

Create authenticator from environment variables.

```python
@classmethod
def from_env(cls) -> JWTAuthenticator
```

**Environment Variables Required**:
- `SF_CLIENT_ID`: Consumer Key
- `SF_USERNAME`: Salesforce username
- `SF_PRIVATE_KEY_PATH`: Path to private key
- `SF_LOGIN_URL`: Login URL (optional, defaults to production)

**Returns**: `JWTAuthenticator` instance

**Example**:
```python
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
```

##### authenticate()

Perform JWT authentication and get session.

```python
def authenticate(self) -> SalesforceSession
```

**Returns**: `SalesforceSession` with:
- `access_token`: OAuth token
- `instance_url`: Salesforce instance URL
- `id`: User identity URL
- `issued_at`: Token issue timestamp

**Raises**:
- `AuthenticationError`: If authentication fails
- `FileNotFoundError`: If private key file not found
- `ValueError`: If JWT creation fails

**Example**:
```python
try:
    session = auth.authenticate()
    print(f"Authenticated: {session.instance_url}")
except Exception as e:
    print(f"Authentication failed: {e}")
```

### Troubleshooting

#### Common Issues

**Issue**: `invalid_grant: user hasn't approved this consumer`
- **Solution**: Pre-approve the Connected App for the user's profile

**Issue**: `invalid_grant: IP restricted or invalid login hours`
- **Solution**: Check IP restrictions and login hours in user profile

**Issue**: `FileNotFoundError: Private key not found`
- **Solution**: Verify `SF_PRIVATE_KEY_PATH` is correct and file exists

**Issue**: `jwt expired`
- **Solution**: JWT tokens expire in 3 minutes. This is normal during generation, not usage.

#### Debug Mode

```python
import logging
logging.basicConfig(level=logging.DEBUG)

auth = JWTAuthenticator.from_env()
session = auth.authenticate()  # Will print debug info
```

---

## OAuth 2.0 Password Flow

### Why OAuth Password Flow?

✅ **Easy Setup**: No certificates required
✅ **Quick Testing**: Fast for development
✅ **User-Based**: Uses user credentials
⚠️ **Less Secure**: Stores password
❌ **Not Recommended**: For production use

### Prerequisites

1. **Salesforce Connected App** with:
   - OAuth settings enabled
   - Password flow enabled
   - Appropriate scopes

2. **User Credentials**:
   - Username
   - Password
   - Security Token (if IP not whitelisted)

3. **Consumer Key & Secret**:
   - From Connected App

### Setup

#### 1. Configure Connected App

1. Setup → App Manager → New Connected App
2. Enable OAuth Settings
3. Add callback URL (can be dummy)
4. Add scopes: `api`, `refresh_token`
5. Save and note:
   - Consumer Key (Client ID)
   - Consumer Secret (Click "Manage Consumer Details")

#### 2. Get Security Token

1. Setup → Reset My Security Token
2. Check email for token
3. Token = password + security token concatenated

#### 3. Environment Variables

```bash
SF_CLIENT_ID=3MVG9...your_consumer_key
SF_CLIENT_SECRET=1234567890ABCDEF...
SF_USERNAME=user@example.com
SF_PASSWORD=YourPassword
SF_SECURITY_TOKEN=ABC123XYZ789
SF_LOGIN_URL=https://login.salesforce.com
```

### Usage

#### From Environment Variables

```python
from kinetic_core import OAuthAuthenticator, SalesforceClient

# Load from .env
auth = OAuthAuthenticator.from_env()

# Authenticate
session = auth.authenticate()

# Create client
client = SalesforceClient(session)
```

#### Manual Configuration

```python
from kinetic_core import OAuthAuthenticator

auth = OAuthAuthenticator(
    client_id="3MVG9...",
    client_secret="1234567890ABCDEF",
    username="user@example.com",
    password="YourPassword",
    security_token="ABC123",
    login_url="https://login.salesforce.com"
)

session = auth.authenticate()
```

### API Reference

#### OAuthAuthenticator Class

```python
class OAuthAuthenticator:
    """OAuth 2.0 Password Flow authenticator"""

    def __init__(
        self,
        client_id: str,
        client_secret: str,
        username: str,
        password: str,
        security_token: str = "",
        login_url: str = "https://login.salesforce.com"
    )
```

**Parameters**:
- `client_id` (str): Consumer Key
- `client_secret` (str): Consumer Secret
- `username` (str): Salesforce username
- `password` (str): User password
- `security_token` (str): Security token (if required)
- `login_url` (str): Login URL

#### Methods

##### from_env()

```python
@classmethod
def from_env(cls) -> OAuthAuthenticator
```

**Environment Variables**:
- `SF_CLIENT_ID`
- `SF_CLIENT_SECRET`
- `SF_USERNAME`
- `SF_PASSWORD`
- `SF_SECURITY_TOKEN` (optional)
- `SF_LOGIN_URL` (optional)

##### authenticate()

```python
def authenticate(self) -> SalesforceSession
```

**Returns**: `SalesforceSession`

**Raises**: `AuthenticationError` if fails

---

## SalesforceSession

The authenticated session object.

### Properties

```python
@dataclass
class SalesforceSession:
    access_token: str      # OAuth access token
    instance_url: str      # Salesforce instance URL
    id: str               # Identity URL
    issued_at: str        # Token issue timestamp
    signature: str = ""   # HMAC signature (optional)
```

### Usage

```python
# Access session properties
print(f"Instance: {session.instance_url}")
print(f"Token: {session.access_token[:20]}...")

# Pass to client
client = SalesforceClient(session)
```

---

## Best Practices

### Production Deployments

1. **Use JWT Bearer Flow**
   - More secure
   - No password storage
   - Better for automation

2. **Store Secrets Securely**
   - Use environment variables
   - Never commit `.env` to git
   - Use secret managers (AWS Secrets, Azure Key Vault)

3. **Rotate Certificates**
   - Plan for certificate expiration
   - Have rollover strategy
   - Test rotation process

### Development

1. **Use OAuth for Quick Testing**
   - Easier initial setup
   - Good for local development

2. **Switch to JWT Before Production**
   - Test thoroughly
   - Update CI/CD pipelines

### Security

1. **Protect Private Keys**
   ```bash
   chmod 600 server.key  # Read/write for owner only
   ```

2. **Use IP Restrictions**
   - Whitelist known IPs in Connected App
   - Reduces attack surface

3. **Limit Scopes**
   - Only grant necessary OAuth scopes
   - Review periodically

4. **Monitor Access**
   - Check login history
   - Set up security alerts

---

## Environment Variables Reference

### Required for JWT

```bash
SF_CLIENT_ID=3MVG9...           # Connected App Consumer Key
SF_USERNAME=user@example.com    # Salesforce username
SF_PRIVATE_KEY_PATH=/path/key   # Path to private key
SF_LOGIN_URL=https://test.salesforce.com  # Login URL
```

### Required for OAuth

```bash
SF_CLIENT_ID=3MVG9...           # Consumer Key
SF_CLIENT_SECRET=ABC123...      # Consumer Secret
SF_USERNAME=user@example.com    # Username
SF_PASSWORD=MyPassword          # Password
SF_SECURITY_TOKEN=XYZ789        # Security token
SF_LOGIN_URL=https://login.salesforce.com
```

### Optional

```bash
SF_API_VERSION=v62.0            # API version (default: v62.0)
LOG_LEVEL=INFO                  # Logging level
LOG_DIR=./logs                  # Log directory
```

---

## Examples

### Example 1: JWT with Docker

```dockerfile
FROM python:3.11-slim

WORKDIR /app

# Copy private key
COPY server.key /app/certs/server.key
RUN chmod 600 /app/certs/server.key

# Install package
RUN pip install kinetic-core

# Set environment
ENV SF_CLIENT_ID=3MVG9...
ENV SF_USERNAME=integration@company.com
ENV SF_PRIVATE_KEY_PATH=/app/certs/server.key
ENV SF_LOGIN_URL=https://test.salesforce.com

CMD ["python", "sync_job.py"]
```

### Example 2: Multi-Environment Setup

```python
import os
from kinetic_core import JWTAuthenticator

# Load config based on environment
env = os.getenv('ENVIRONMENT', 'dev')

configs = {
    'dev': {
        'client_id': os.getenv('DEV_SF_CLIENT_ID'),
        'username': os.getenv('DEV_SF_USERNAME'),
        'private_key_path': '/certs/dev-key.pem',
        'login_url': 'https://test.salesforce.com'
    },
    'prod': {
        'client_id': os.getenv('PROD_SF_CLIENT_ID'),
        'username': os.getenv('PROD_SF_USERNAME'),
        'private_key_path': '/certs/prod-key.pem',
        'login_url': 'https://login.salesforce.com'
    }
}

config = configs[env]
auth = JWTAuthenticator(**config)
session = auth.authenticate()
```

### Example 3: Token Refresh

```python
from kinetic_core import JWTAuthenticator, SalesforceClient

auth = JWTAuthenticator.from_env()

def get_client():
    """Get authenticated client with fresh session"""
    session = auth.authenticate()
    return SalesforceClient(session)

# Use in long-running process
while True:
    client = get_client()  # Fresh session
    # Do work...
    time.sleep(3600)  # Re-authenticate every hour
```

---

## Related Documentation

- **[Salesforce Setup Guide](../SALESFORCE_SETUP_GUIDE.md)** - Configure Connected App
- **[User Guide](../USER_GUIDE.md)** - Using authenticated client
- **[Testing Guide](../TESTING_GUIDE.md)** - Test authentication

---

## External Resources

- [Salesforce JWT Bearer Flow](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_jwt_flow.htm)
- [OAuth 2.0 Password Flow](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_username_password_flow.htm)
- [Connected Apps](https://help.salesforce.com/s/articleView?id=sf.connected_app_overview.htm)
