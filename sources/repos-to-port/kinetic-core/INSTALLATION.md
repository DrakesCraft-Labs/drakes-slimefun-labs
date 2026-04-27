# Installation Guide

Complete installation guide for **Salesforce Toolkit**.

---

## Table of Contents

1. [Requirements](#requirements)
2. [Installation Methods](#installation-methods)
3. [Salesforce Setup](#salesforce-setup)
4. [Configuration](#configuration)
5. [Verification](#verification)
6. [Troubleshooting](#troubleshooting)

---

## Requirements

### System Requirements

- **Python**: 3.8 or higher
- **Operating System**: Windows, macOS, or Linux
- **Disk Space**: ~50 MB

### Salesforce Requirements

- **Salesforce Edition**: Any edition with API access
- **User Permissions**:
  - API Enabled
  - Modify All Data (or specific object permissions)
- **Connected App**: Required for authentication

---

## Installation Methods

### Method 1: Install from PyPI (Recommended)

```bash
pip install salesforce-toolkit
```

### Method 2: Install from Source

```bash
# Clone the repository
git clone https://github.com/yourusername/salesforce-toolkit.git
cd salesforce-toolkit

# Install in development mode
pip install -e .
```

### Method 3: Install with Optional Dependencies

```bash
# With database support
pip install salesforce-toolkit[database]

# With data manipulation tools
pip install salesforce-toolkit[data]

# With development tools
pip install salesforce-toolkit[dev]

# Install all extras
pip install salesforce-toolkit[database,data,dev]
```

---

## Salesforce Setup

### Step 1: Create a Connected App

1. Log in to Salesforce
2. Navigate to **Setup** → **Apps** → **App Manager**
3. Click **New Connected App**
4. Fill in the required fields:
   - **Connected App Name**: `My Salesforce Integration`
   - **API Name**: `My_Salesforce_Integration`
   - **Contact Email**: your@email.com
5. Enable **OAuth Settings**:
   - Callback URL: `https://localhost`
   - Selected OAuth Scopes:
     - Full access (full)
     - Perform requests on your behalf at any time (refresh_token, offline_access)
6. **Save** and note your **Consumer Key** (Client ID)

### Step 2: Setup JWT Authentication (Recommended)

#### A. Generate RSA Key Pair

```bash
# Generate private key (2048-bit RSA)
openssl genrsa -out server.key 2048

# Generate certificate signing request
openssl req -new -key server.key -out server.csr

# Generate self-signed certificate (valid for 365 days)
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

#### B. Upload Certificate to Salesforce

1. Go to your Connected App
2. Click **Edit**
3. Enable **Use digital signatures**
4. Upload `server.crt`
5. **Save**

#### C. Pre-authorize User

1. Go to your Connected App
2. Click **Manage**
3. Click **Edit Policies**
4. Under **OAuth Policies**:
   - Permitted Users: **Admin approved users are pre-authorized**
5. **Save**
6. Go to **Manage Profiles** or **Manage Permission Sets**
7. Add your user's profile/permission set

### Step 3: Setup OAuth Password Flow (Alternative)

If using OAuth Password Flow instead of JWT:

1. In your Connected App, enable **OAuth Settings**
2. Enable **Enable OAuth Settings for API Integration**
3. Copy your **Consumer Key** and **Consumer Secret**
4. If required, reset your **Security Token**:
   - Go to **Setup** → **My Personal Information** → **Reset My Security Token**
   - Check your email for the new token

---

## Configuration

### Step 1: Create Environment File

Create a `.env` file in your project root:

```bash
# Copy the example file
cp config/.env.example .env
```

### Step 2: Configure Environment Variables

#### For JWT Authentication (Recommended):

```bash
# Salesforce JWT Configuration
SF_CLIENT_ID=3MVG9...YOUR_CONSUMER_KEY_HERE
SF_USERNAME=user@example.com.sandbox
SF_PRIVATE_KEY_PATH=/absolute/path/to/server.key
SF_LOGIN_URL=https://test.salesforce.com

# API Configuration
SF_API_VERSION=v60.0

# Logging Configuration
LOG_DIR=./logs
LOG_LEVEL=INFO
LOG_CONSOLE_OUTPUT=true
LOG_CONSOLE_COLORS=true
```

#### For OAuth Password Flow (Alternative):

```bash
# Salesforce OAuth Configuration
SF_CLIENT_ID=3MVG9...YOUR_CONSUMER_KEY_HERE
SF_CLIENT_SECRET=1234567890ABCDEF...YOUR_CONSUMER_SECRET
SF_USERNAME=user@example.com
SF_PASSWORD=your_password
SF_SECURITY_TOKEN=ABC123...YOUR_SECURITY_TOKEN
SF_LOGIN_URL=https://login.salesforce.com

# API Configuration
SF_API_VERSION=v60.0

# Logging Configuration
LOG_DIR=./logs
LOG_LEVEL=INFO
```

### Important Notes

- **Sandbox URL**: Use `https://test.salesforce.com` for sandboxes
- **Production URL**: Use `https://login.salesforce.com` for production
- **Private Key Path**: Must be an **absolute path**
- **Security Token**: Only needed for OAuth password flow

---

## Verification

### Test Installation

```python
# test_installation.py
from salesforce_toolkit import __version__

print(f"Salesforce Toolkit version: {__version__}")
```

```bash
python test_installation.py
```

### Test Authentication

```python
# test_auth.py
from salesforce_toolkit import JWTAuthenticator

auth = JWTAuthenticator.from_env()
session = auth.authenticate()

print(f"✓ Authentication successful!")
print(f"Instance URL: {session.instance_url}")
print(f"API Version: {session.api_version}")
```

```bash
python test_auth.py
```

### Test CLI

```bash
# Test authentication via CLI
sf-toolkit auth --method jwt

# Query Salesforce
sf-toolkit query "SELECT Id, Name FROM Account LIMIT 5"
```

---

## Troubleshooting

### Common Issues

#### 1. ModuleNotFoundError

**Error**: `ModuleNotFoundError: No module named 'salesforce_toolkit'`

**Solution**:
```bash
# Reinstall the package
pip uninstall salesforce-toolkit
pip install salesforce-toolkit

# Or install in development mode
pip install -e .
```

#### 2. Authentication Failed: invalid_grant

**Error**: `Authentication failed: invalid_grant`

**Possible Causes**:
- Consumer Key is incorrect
- User is not pre-authorized
- Certificate doesn't match private key
- Wrong login URL (check sandbox vs production)

**Solutions**:
1. Verify Consumer Key in `.env` matches Salesforce
2. Check user is pre-authorized in Connected App
3. Regenerate certificate and re-upload to Salesforce
4. Use `https://test.salesforce.com` for sandboxes

#### 3. Private Key Not Found

**Error**: `FileNotFoundError: Private key file not found`

**Solution**:
- Use **absolute path** in `SF_PRIVATE_KEY_PATH`
- Verify file exists: `ls -la /path/to/server.key`
- Check file permissions: `chmod 600 server.key`

#### 4. Import Error: PyJWT

**Error**: `ModuleNotFoundError: No module named 'jwt'`

**Solution**:
```bash
pip install PyJWT cryptography
```

#### 5. Permission Denied

**Error**: `Permission denied: '/logs/salesforce_toolkit.log'`

**Solution**:
```bash
# Create logs directory
mkdir -p logs
chmod 755 logs

# Or change LOG_DIR in .env
LOG_DIR=./logs
```

#### 6. API Version Not Supported

**Error**: `API version v56.0 is not supported`

**Solution**:
- Update `SF_API_VERSION` in `.env` to a supported version (v60.0 recommended)
- Check Salesforce release notes for available API versions

---

## Platform-Specific Instructions

### Windows

```bash
# Install Python (if not installed)
# Download from https://www.python.org/downloads/

# Install package
pip install salesforce-toolkit

# Generate keys (requires OpenSSL for Windows)
# Download: https://slproweb.com/products/Win32OpenSSL.html
openssl genrsa -out server.key 2048
openssl req -new -x509 -key server.key -out server.crt -days 365
```

### macOS

```bash
# Install Python (if not installed)
brew install python3

# Install package
pip3 install salesforce-toolkit

# Generate keys (OpenSSL pre-installed)
openssl genrsa -out server.key 2048
openssl req -new -x509 -key server.key -out server.crt -days 365
```

### Linux

```bash
# Install Python (if not installed)
sudo apt-get update
sudo apt-get install python3 python3-pip

# Install package
pip3 install salesforce-toolkit

# Generate keys (OpenSSL pre-installed)
openssl genrsa -out server.key 2048
openssl req -new -x509 -key server.key -out server.crt -days 365
```

---

## Docker Installation (Optional)

```dockerfile
# Dockerfile
FROM python:3.11-slim

WORKDIR /app

# Install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy application
COPY . .

# Install package
RUN pip install -e .

CMD ["python", "your_script.py"]
```

```bash
# Build image
docker build -t salesforce-toolkit .

# Run container
docker run -it --env-file .env salesforce-toolkit
```

---

## Upgrade

### Upgrade to Latest Version

```bash
pip install --upgrade salesforce-toolkit
```

### Verify Upgrade

```python
from salesforce_toolkit import __version__
print(f"Version: {__version__}")
```

---

## Uninstall

```bash
pip uninstall salesforce-toolkit
```

---

## Next Steps

After successful installation:

1. ✅ Read the [Quick Start Guide](docs/QUICK_START.md)
2. ✅ Try the [Examples](examples/)
3. ✅ Read the [README](README.md) for full documentation
4. ✅ Configure your first [Sync Pipeline](config/sync_config_example.yaml)

---

## Support

Need help? Check these resources:

- **Documentation**: [README.md](README.md)
- **GitHub Issues**: https://github.com/yourusername/salesforce-toolkit/issues
- **Examples**: [examples/](examples/)
- **Quick Start**: [docs/QUICK_START.md](docs/QUICK_START.md)

---

**Installation complete! 🎉**
