"""
Pytest configuration and shared fixtures for Kinetic Core tests.

This file is automatically loaded by pytest and provides:
- Shared fixtures for all test files
- Test configuration
- Custom markers
"""

import pytest
import sys
from pathlib import Path

# Add parent directory to path so we can import kinetic_core
sys.path.insert(0, str(Path(__file__).parent.parent))


def pytest_configure(config):
    """Configure pytest with custom markers."""
    config.addinivalue_line(
        "markers", "unit: Unit tests (fast, no external dependencies)"
    )
    config.addinivalue_line(
        "markers", "integration: Integration tests (require Salesforce connection)"
    )
    config.addinivalue_line(
        "markers", "slow: Slow tests"
    )
    config.addinivalue_line(
        "markers", "auth: Authentication tests"
    )
    config.addinivalue_line(
        "markers", "crud: CRUD operation tests"
    )
    config.addinivalue_line(
        "markers", "query: Query operation tests"
    )
    config.addinivalue_line(
        "markers", "batch: Batch operation tests"
    )
    config.addinivalue_line(
        "markers", "error: Error handling tests"
    )


def pytest_collection_modifyitems(config, items):
    """
    Automatically mark tests based on their location and name.
    """
    for item in items:
        # Mark integration tests
        if "test_integration" in item.nodeid:
            item.add_marker(pytest.mark.integration)

        # Mark unit tests
        if "test_core" in item.nodeid or "test_auth" in item.nodeid:
            item.add_marker(pytest.mark.unit)

        # Mark based on function name
        if "auth" in item.name.lower():
            item.add_marker(pytest.mark.auth)
        if "create" in item.name.lower() or "update" in item.name.lower() or "delete" in item.name.lower():
            item.add_marker(pytest.mark.crud)
        if "query" in item.name.lower():
            item.add_marker(pytest.mark.query)
        if "batch" in item.name.lower():
            item.add_marker(pytest.mark.batch)
        if "error" in item.name.lower() or "invalid" in item.name.lower():
            item.add_marker(pytest.mark.error)


@pytest.fixture(scope="session")
def test_config():
    """
    Provide test configuration.
    """
    return {
        "timeout": 30,
        "max_retries": 3,
        "batch_size": 200
    }


def pytest_report_header(config):
    """Add custom header to test report."""
    return [
        "Kinetic Core - Integration Test Suite",
        "=" * 70,
        "Testing Salesforce CRUD operations",
        "=" * 70
    ]
