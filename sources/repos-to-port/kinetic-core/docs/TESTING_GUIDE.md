# Testing Quick Start Guide

This guide helps you verify that `salesforce-toolkit` is correctly installed and functional.

## 🐳 Docker Method (Recommended)

If you are using Docker, verification is extremely simple.

**1. Run Tests:**
```bash
docker-compose build
docker-compose run tests
```

**2. Verify CLI:**
```bash
docker-compose run toolkit --help
```

---

## 🐍 Manual Python Method

If you prefer to run locally without Docker, follow these steps:

## 1. Environment Setup

First, ensure all dependencies are installed. The toolkit requires Python 3.8+.

```bash
# Install production dependencies
pip install -r requirements.txt

# Install development dependencies (needed for testing)
pip install -e ".[dev]"
```

## 2. Verification Steps

### A. CLI Sanity Check
Verify that the Command Line Interface loads correctly. This confirms that all Python modules are importable.

```bash
python cli.py --help
```
*Expected Output:* A help message listing available commands (`auth`, `query`, `create`, etc.).

### B. Authentication Test (Dry Run)
You can verify the authentication modules without connecting to Salesforce by checking if the classes instantiate.

Create a file named `verify_install.py`:

```python
import sys
from salesforce_toolkit import JWTAuthenticator, SalesforceClient

print("Verifying imports and classes...")
try:
    # Check if classes are available
    assert JWTAuthenticator
    assert SalesforceClient
    print("✅ Core modules imported successfully.")
except ImportError as e:
    print(f"❌ Import failed: {e}")
    sys.exit(1)
except Exception as e:
    print(f"❌ Unexpected error: {e}")
    sys.exit(1)
```

Run it:
```bash
python verify_install.py
```

## 3. Running Tests
> **Note:** The `tests/` directory is currently empty. To fully verify functionality, unit tests needed to be added.

Once tests are added, you can run them using `pytest`:

```bash
pytest
```

## 4. Connection Test (Requires Credentials)
To test actual connectivity, you need a Salesforce instance.

1. Create a `.env` file based on `config/.env.example`.
2. run:
   ```bash
   python cli.py auth --method jwt
   ```
   *or*
   ```bash
   python cli.py auth --method oauth
   ```
