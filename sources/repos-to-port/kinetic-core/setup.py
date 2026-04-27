"""
Salesforce Toolkit - Setup Configuration

A comprehensive Python library for Salesforce integration with:
- Multiple authentication methods (JWT, OAuth)
- Generic CRUD operations on any Salesforce object
- Bulk API v2 for high-volume data operations (NEW in v2.0)
- Field mapping and data transformation
- ETL pipeline framework
- Comprehensive logging
"""

from setuptools import setup, find_packages
from pathlib import Path

# Read long description from README
readme_file = Path(__file__).parent / "README.md"
long_description = readme_file.read_text(encoding="utf-8") if readme_file.exists() else ""

# Read requirements
requirements_file = Path(__file__).parent / "requirements.txt"
with open(requirements_file, "r", encoding="utf-8") as f:
    requirements = [
        line.strip()
        for line in f
        if line.strip() and not line.startswith("#")
    ]

setup(
    name="kinetic-core",
    version="2.1.1",
    author="Antonio Trento",
    author_email="info@antoniotrento.net",
    description="Salesforce integration library with Bulk API v2 support",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/antonio-backend-projects/kinetic-core",
    packages=find_packages(exclude=["tests", "examples", "docs"]),
    classifiers=[
        "Development Status :: 5 - Production/Stable",
        "Intended Audience :: Developers",
        "Topic :: Software Development :: Libraries :: Python Modules",
        "Topic :: Office/Business",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
        "Programming Language :: Python :: 3.12",
        "Operating System :: OS Independent",
    ],
    python_requires=">=3.8",
    install_requires=requirements,
    extras_require={
        "dev": [
            "pytest>=7.4.3",
            "pytest-cov>=4.1.0",
            "black>=23.12.1",
            "flake8>=7.0.0",
            "mypy>=1.8.0",
            "sphinx>=7.2.6",
            "twine>=4.0.2",
            "build>=1.0.3",
        ],
        "database": [
            "mysql-connector-python>=8.0.33",
            "psycopg2-binary>=2.9.9",
            "pymongo>=4.6.1",
        ],
        "data": [
            "pandas>=2.1.4",
            "numpy>=1.26.3",
        ],
    },
    entry_points={
        "console_scripts": [
            "sf-toolkit=cli:main",
        ],
    },
    keywords=[
        "salesforce",
        "crm",
        "integration",
        "api",
        "bulk-api",
        "bulk-api-v2",
        "etl",
        "data-sync",
        "rest-api",
        "jwt",
        "oauth",
        "ai",
        "agent",
        "mcp",
        "high-volume",
        "data-processing"
    ],
    project_urls={
        "Documentation": "https://github.com/antonio-backend-projects/kinetic-core#readme",
        "Source": "https://github.com/antonio-backend-projects/kinetic-core",
        "Bug Reports": "https://github.com/antonio-backend-projects/kinetic-core/issues",
        "Portfolio": "https://antoniotrento.net/portfolio/kinetic-core/",
    },
    include_package_data=True,
    zip_safe=False,
)
