# Publishing Guide

This guide explains how to package your toolkit as a library and publish it to the Python Package Index (PyPI), making it installable via `pip install salesforce-toolkit`.

## 1. Prerequisites

You need a generic "build" environment. Since we added `build` and `twine` to `setup.py`, you can install them:

**On your machine (or Docker):**
```bash
pip install build twine
```

## 2. Configuration (`setup.py`)

I have already updated your `setup.py` with your information:
- **Name**: `salesforce-toolkit`
- **Version**: `1.0.0`
- **Author**: `Antonio Trento`
- **URL**: `https://github.com/antonio-backend-projects/salesforce-toolkit`

> **Note**: If the name `salesforce-toolkit` is already taken on PyPI (which is likely), you will need to change the `name` argument in `setup.py` to something unique, like `antonio-salesforce-toolkit` or `sf-toolkit-pro`.

## 3. Build the Package

Run this command to generate the distribution files (`.tar.gz` and `.whl`) in the `dist/` folder:

```bash
python -m build
```

You should see:
```
dist/
  salesforce_toolkit-1.0.0-py3-none-any.whl
  salesforce-toolkit-1.0.0.tar.gz
```

## 4. Test Publishing (TestPyPI)

It is highly recommended to upload to **TestPyPI** first to check if everything looks right.

1.  **Register:** Go to [test.pypi.org](https://test.pypi.org/) and create an account.
2.  **Create Token:** Go to Account Settings → API Tokens → Create a new token (Scope: Entire account). Copy it.
3.  **Upload:**
    ```bash
    python -m twine upload --repository testpypi dist/*
    ```
4.  **Enter Credentials:**
    - Username: `__token__`
    - Password: `<your-api-token>`

5.  **Verify:** Try installing it in a new virtual environment:
    ```bash
    pip install --index-url https://test.pypi.org/simple/ salesforce-toolkit
    ```

## 5. Publish to Production (PyPI)

Once verified:

1.  **Register:** Go to [pypi.org](https://pypi.org/) and create an account.
2.  **Create Token:** Account Settings → API Tokens.
3.  **Upload:**
    ```bash
    python -m twine upload dist/*
    ```
4.  **Success!** Your package is now live. Anyone can run:
    ```bash
    pip install salesforce-toolkit
    ```

## 6. Alternative: Install from GitHub

If you don't want to publish to PyPI publicly, users can install directly from your GitHub repository:

```bash
pip install git+https://github.com/antoniotrento/salesforce-toolkit.git
```

This is great for private testing or internal tools.
