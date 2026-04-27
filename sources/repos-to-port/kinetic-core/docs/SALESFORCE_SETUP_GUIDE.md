# Salesforce Setup Guide - Complete Walkthrough

> **Step-by-step guide to configure Salesforce access for the Salesforce Toolkit**

This guide will walk you through **every single step** to set up authentication with Salesforce, from creating a Connected App to testing your first API call.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Method 1: JWT Bearer Flow (Recommended)](#method-1-jwt-bearer-flow-recommended)
3. [Method 2: OAuth Password Flow](#method-2-oauth-password-flow)
4. [Testing Your Setup](#testing-your-setup)
5. [Troubleshooting](#troubleshooting)
6. [Security Best Practices](#security-best-practices)

---

## Prerequisites

Before starting, make sure you have:

- ✅ **Salesforce Account** with admin access (or API permissions)
- ✅ **Python 3.8+** installed
- ✅ **OpenSSL** installed (for JWT method)
  - Windows: Download from [slproweb.com](https://slproweb.com/products/Win32OpenSSL.html)
  - macOS: Pre-installed
  - Linux: Pre-installed (or `sudo apt-get install openssl`)
- ✅ **Salesforce Toolkit** installed (`pip install salesforce-toolkit`)

---

## Method 1: JWT Bearer Flow (Recommended)

**Why JWT?**
- ✅ More secure (no passwords stored)
- ✅ No security token needed
- ✅ Recommended for production
- ✅ Supports server-to-server integration

### Step 1: Generate RSA Key Pair

Open your terminal and run:

```bash
# Navigate to your project directory
cd /path/to/your/project

# Create a directory for certificates (optional but recommended)
mkdir certs
cd certs

# Generate private key (2048-bit RSA)
openssl genrsa -out server.key 2048

# Generate self-signed certificate (valid for 365 days)
openssl req -new -x509 -key server.key -out server.crt -days 365
```

**When prompted, enter:**
```
Country Name (2 letter code): IT
State or Province Name: Lombardia
Locality Name: Milan
Organization Name: Your Company
Organizational Unit Name: IT
Common Name: localhost
Email Address: your@email.com
```

**Result**: You'll have two files:
- `server.key` - **Private key** (NEVER share this!)
- `server.crt` - **Certificate** (upload to Salesforce)

**Important**: Note the **absolute path** to `server.key`. You'll need it later.

---

### Step 2: Create Connected App in Salesforce

#### 2.1 Navigate to App Manager

1. **Login** to your Salesforce org
2. Click the **⚙️ gear icon** (top right) → **Setup**
3. In the Quick Find box (left sidebar), type: **"App Manager"**
4. Click **"App Manager"** under Platform Tools → Apps
5. Click **"New Connected App"** button (top right)

#### 2.2 Fill Basic Information

In the **Basic Information** section:

```
Connected App Name: My Salesforce Integration
API Name: My_Salesforce_Integration (auto-generated, leave as is)
Contact Email: your@email.com
Description: Integration for Salesforce Toolkit (optional)
```

#### 2.3 Configure API (Enable OAuth Settings)

Scroll down to **API (Enable OAuth Settings)**:

1. ✅ Check **"Enable OAuth Settings"**

2. **Callback URL**: Enter `https://localhost`
   - Note: For JWT, the callback URL doesn't matter, but it's required

3. **Selected OAuth Scopes**: Click **"Add"** to move these from Available to Selected:
   - `Access the identity URL service (id, profile, email, address, phone)`
   - `Full access (full)`
   - `Perform requests at any time (refresh_token, offline_access)`

4. ✅ Check **"Use digital signatures"**

5. **Upload Certificate**:
   - Click **"Choose File"**
   - Select `server.crt` (the certificate, NOT server.key!)
   - The file will upload

6. ✅ Check **"Require Proof Key for Code Exchange (PKCE)"** (optional, for extra security)

#### 2.4 Save and Wait

1. Click **"Save"** at the bottom
2. Click **"Continue"** on the confirmation screen
3. ⏰ **IMPORTANT**: Wait **2-10 minutes** for Salesforce to process your Connected App

---

### Step 3: Copy Consumer Key

After waiting 2-10 minutes:

1. Go back to **Setup** → **App Manager**
2. Find your Connected App in the list
3. Click the **▼ dropdown** at the right → **"View"**
4. You'll see the **API (Enable OAuth Settings)** section
5. **Copy the "Consumer Key"**
   - It's a long string like: `3MVG9XPlQYHF2jxAtyNlZrULLGJ06jOs...`
   - Keep this safe! You'll need it in your `.env` file

**Tip**: Click the copy icon (📋) next to Consumer Key to copy it.

---

### Step 4: Pre-Authorize Users (CRITICAL!)

**This is the most commonly missed step!**

Without pre-authorization, you'll get an `invalid_grant` error.

#### 4.1 Edit Policies

1. In the Connected App view, click **"Manage"** (top of page)
2. Click **"Edit Policies"**
3. Scroll to **"OAuth Policies"** section
4. **Permitted Users**: Change from "All users may self-authorize" to:
   - **"Admin approved users are pre-authorized"**
5. Click **"Save"**

#### 4.2 Add User Profiles/Permission Sets

1. Scroll down on the same page
2. You'll see two sections:
   - **Profiles** (Manage Profiles)
   - **Permission Sets** (Manage Permission Sets)

3. Click **"Manage Profiles"**
4. **Add your user's profile**:
   - For admin users: Select "System Administrator"
   - For regular users: Select their profile (e.g., "Standard User")
5. Click **"Save"**

**Alternative**: If using Permission Sets:
1. Click **"Manage Permission Sets"**
2. Select the appropriate permission set
3. Click **"Save"**

---

### Step 5: Configure Environment Variables

Create a `.env` file in your project root:

```bash
# Salesforce JWT Authentication
SF_CLIENT_ID=3MVG9XPlQYHF2jxAtyNlZrULLGJ...YOUR_CONSUMER_KEY_HERE
SF_USERNAME=your.username@company.com.sandbox
SF_PRIVATE_KEY_PATH=/absolute/path/to/certs/server.key
SF_LOGIN_URL=https://test.salesforce.com

# API Version
SF_API_VERSION=v60.0

# Logging
LOG_DIR=./logs
LOG_LEVEL=INFO
LOG_CONSOLE_OUTPUT=true
LOG_CONSOLE_COLORS=true
```

**Replace with your values**:

| Variable | Value | Example |
|----------|-------|---------|
| `SF_CLIENT_ID` | Consumer Key from Step 3 | `3MVG9XPlQYHF2jx...` |
| `SF_USERNAME` | Your Salesforce username | `admin@vuscom.com.dev1` |
| `SF_PRIVATE_KEY_PATH` | **ABSOLUTE** path to server.key | `/Users/antonio/project/certs/server.key` |
| `SF_LOGIN_URL` | Sandbox: `https://test.salesforce.com`<br>Production: `https://login.salesforce.com` | `https://test.salesforce.com` |

**Critical**: `SF_PRIVATE_KEY_PATH` must be an **absolute path**, not relative!

**How to get absolute path**:
- Windows: `C:\Users\YourName\project\certs\server.key`
- macOS/Linux: Run `pwd` in the certs folder, then append `/server.key`

---

### Step 6: Test Authentication

Create a test script `test_auth.py`:

```python
from salesforce_toolkit import JWTAuthenticator

print("Testing JWT authentication...")

try:
    # Authenticate using .env configuration
    auth = JWTAuthenticator.from_env()
    session = auth.authenticate()

    print("\n✅ SUCCESS! Authentication successful!")
    print(f"📍 Instance URL: {session.instance_url}")
    print(f"🔧 API Version: {session.api_version}")
    print(f"👤 Username: {session.username}")
    print(f"🔑 Access Token: {session.access_token[:20]}...")

except FileNotFoundError as e:
    print(f"\n❌ ERROR: Private key file not found")
    print(f"   Make sure SF_PRIVATE_KEY_PATH in .env is an absolute path")
    print(f"   Current value causes error: {e}")

except Exception as e:
    print(f"\n❌ ERROR: Authentication failed")
    print(f"   Error: {e}")
    print("\n💡 Common issues:")
    print("   1. Consumer Key is incorrect")
    print("   2. User is not pre-authorized (Step 4)")
    print("   3. Wrong login URL (sandbox vs production)")
    print("   4. Connected App not fully processed (wait 10 minutes)")
```

Run the test:

```bash
python test_auth.py
```

**Expected output**:
```
Testing JWT authentication...

✅ SUCCESS! Authentication successful!
📍 Instance URL: https://vuscom--dev1.sandbox.my.salesforce.com
🔧 API Version: v60.0
👤 Username: admin@vuscom.com.dev1
🔑 Access Token: 00D5e000000abcd!AR...
```

---

## Method 2: OAuth Password Flow

**When to use**: Development/testing only (not recommended for production)

### Step 1: Create Connected App

Follow **Steps 2.1 and 2.2** from JWT method above, BUT:

**In Step 2.3 (OAuth Settings)**:
- ✅ Enable OAuth Settings
- Callback URL: `https://localhost`
- Selected OAuth Scopes: Same as JWT
- ❌ **DO NOT** check "Use digital signatures"

Click **"Save"** and wait 2-10 minutes.

---

### Step 2: Get Consumer Key and Secret

1. Go to **Setup** → **App Manager** → Your Connected App → **View**
2. In the **API (Enable OAuth Settings)** section:
   - **Copy "Consumer Key"**
   - Click **"Click to reveal"** next to "Consumer Secret"
   - **Or** click **"Manage Consumer Details"**
   - Verify your identity (code via email)
   - **Copy "Consumer Secret"**

---

### Step 3: Get Security Token

1. Click your profile picture (top right) → **"Settings"**
2. In the left menu, search for **"Reset My Security Token"**
   - Or navigate: My Personal Information → Reset My Security Token
3. Click **"Reset Security Token"**
4. Check your email - you'll receive the new Security Token
5. **Copy the Security Token**

**Note**: The Security Token is appended to your password when authenticating.

---

### Step 4: Configure .env

```bash
# Salesforce OAuth Password Flow
SF_CLIENT_ID=3MVG9XPlQYHF2jx...CONSUMER_KEY
SF_CLIENT_SECRET=1234567890ABCDEF...CONSUMER_SECRET
SF_USERNAME=your.username@company.com.sandbox
SF_PASSWORD=YourPassword
SF_SECURITY_TOKEN=ABC123XYZ...TOKEN_FROM_EMAIL
SF_LOGIN_URL=https://test.salesforce.com

# Logging
LOG_DIR=./logs
LOG_LEVEL=INFO
```

**Important**:
- For sandbox, use `https://test.salesforce.com`
- For production, use `https://login.salesforce.com`

---

### Step 5: Test Authentication

```python
from salesforce_toolkit import OAuthAuthenticator

print("Testing OAuth authentication...")

try:
    auth = OAuthAuthenticator.from_env()
    session = auth.authenticate()

    print("\n✅ SUCCESS! Authentication successful!")
    print(f"📍 Instance URL: {session.instance_url}")
    print(f"👤 Username: {session.username}")

except Exception as e:
    print(f"\n❌ ERROR: {e}")
```

---

## Testing Your Setup

Once authenticated, test basic operations:

```python
from salesforce_toolkit import JWTAuthenticator, SalesforceClient

# Authenticate
auth = JWTAuthenticator.from_env()
session = auth.authenticate()
client = SalesforceClient(session)

# Test 1: Query existing records
print("\n📊 Test 1: Querying Accounts...")
accounts = client.query("SELECT Id, Name FROM Account LIMIT 5")
print(f"✅ Found {len(accounts)} accounts")
for acc in accounts:
    print(f"  - {acc['Name']} ({acc['Id']})")

# Test 2: Create a test record
print("\n➕ Test 2: Creating test Account...")
account_id = client.create("Account", {
    "Name": "Test Account - DELETE ME",
    "Phone": "555-0100"
})
print(f"✅ Created Account: {account_id}")

# Test 3: Update the record
print("\n✏️ Test 3: Updating Account...")
client.update("Account", account_id, {"Phone": "555-9999"})
print(f"✅ Updated Account")

# Test 4: Delete the record
print("\n🗑️ Test 4: Deleting Account...")
client.delete("Account", account_id)
print(f"✅ Deleted Account")

print("\n🎉 All tests passed!")
```

---

## Troubleshooting

### Error: "invalid_grant"

**Symptoms**: `Authentication failed: invalid_grant`

**Causes & Solutions**:

1. **User not pre-authorized** (most common)
   - ✅ Go to Connected App → Manage → Edit Policies
   - ✅ Set "Permitted Users" to "Admin approved users are pre-authorized"
   - ✅ Add user profile via "Manage Profiles"

2. **Consumer Key is incorrect**
   - ✅ Double-check `SF_CLIENT_ID` in `.env`
   - ✅ Make sure you copied the entire key

3. **Certificate mismatch** (JWT only)
   - ✅ Regenerate certificate and re-upload to Salesforce
   - ✅ Make sure `server.crt` and `server.key` are from the same generation

4. **Wrong login URL**
   - ✅ Sandbox: `https://test.salesforce.com`
   - ✅ Production: `https://login.salesforce.com`

5. **Connected App not ready**
   - ✅ Wait 2-10 minutes after creating/editing the Connected App

---

### Error: "FileNotFoundError: Private key file not found"

**Cause**: `SF_PRIVATE_KEY_PATH` is incorrect or relative

**Solutions**:
```bash
# Get absolute path (macOS/Linux)
cd /path/to/certs
pwd  # Copy this path
# Then in .env: SF_PRIVATE_KEY_PATH=/absolute/path/to/certs/server.key

# Get absolute path (Windows)
cd C:\path\to\certs
cd  # Shows current directory
# Then in .env: SF_PRIVATE_KEY_PATH=C:\path\to\certs\server.key
```

---

### Error: "invalid_client_id"

**Cause**: Consumer Key is incorrect

**Solution**:
1. Go to Setup → App Manager → Your App → View
2. Copy "Consumer Key" again
3. Update `SF_CLIENT_ID` in `.env`

---

### Error: "invalid_client_credentials" (OAuth only)

**Cause**: Consumer Secret is incorrect

**Solution**:
1. Go to Setup → App Manager → Your App → View
2. Click "Manage Consumer Details"
3. Verify identity
4. Copy "Consumer Secret"
5. Update `SF_CLIENT_SECRET` in `.env`

---

### Error: "authentication failure" (OAuth only)

**Cause**: Incorrect password or security token

**Solution**:
1. Verify `SF_PASSWORD` is correct
2. Reset Security Token (Settings → Reset My Security Token)
3. Update `SF_SECURITY_TOKEN` in `.env`

---

## Security Best Practices

### DO ✅

1. **Store keys securely**
   ```bash
   chmod 600 certs/server.key  # Restrict access
   ```

2. **Use .gitignore**
   ```
   .env
   certs/server.key
   *.key
   *.pem
   ```

3. **Rotate certificates regularly**
   - Regenerate every 6-12 months

4. **Use separate Connected Apps**
   - One for development
   - One for production

5. **Restrict IP ranges** (if possible)
   - Connected App → Manage → Edit Policies → IP Relaxation

6. **Monitor API usage**
   - Setup → System Overview → API Usage

### DON'T ❌

1. ❌ Commit `.env` or `server.key` to git
2. ❌ Share Consumer Secret publicly
3. ❌ Use OAuth password flow in production
4. ❌ Use same credentials for dev and prod
5. ❌ Give "Full Access" to users who don't need it

---

## Quick Reference

### File Locations
```
your-project/
├── .env                    # Configuration (NEVER commit!)
├── certs/
│   ├── server.key          # Private key (NEVER commit!)
│   └── server.crt          # Certificate (upload to Salesforce)
└── your_script.py
```

### Environment Variables (JWT)
```bash
SF_CLIENT_ID=3MVG9...       # From Salesforce Connected App
SF_USERNAME=user@domain     # Your Salesforce username
SF_PRIVATE_KEY_PATH=/abs/path/server.key  # ABSOLUTE path
SF_LOGIN_URL=https://test.salesforce.com  # or login.salesforce.com
```

### Environment Variables (OAuth)
```bash
SF_CLIENT_ID=3MVG9...       # Consumer Key
SF_CLIENT_SECRET=12345...   # Consumer Secret
SF_USERNAME=user@domain
SF_PASSWORD=yourpassword
SF_SECURITY_TOKEN=ABC123... # From email
SF_LOGIN_URL=https://test.salesforce.com
```

---

## Next Steps

After successful authentication:

1. ✅ Try the [Quick Start Guide](QUICK_START.md)
2. ✅ Run the [Examples](../examples/)
3. ✅ Read the [README](../README.md)
4. ✅ Build your first pipeline

---

**Need help?**
- Check [INSTALLATION.md](../INSTALLATION.md) for more details
- Open an issue on GitHub
- Read the [Troubleshooting](#troubleshooting) section above

---

**✨ You're all set! Happy coding!**
