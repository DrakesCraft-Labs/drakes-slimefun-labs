"""
Test runner script for Kinetic Core.

This script provides an easy way to run different test suites:
- Unit tests (fast, no Salesforce connection)
- Integration tests (requires Salesforce connection)
- All tests with coverage report

Usage:
    python run_tests.py                 # Run all tests
    python run_tests.py --unit          # Unit tests only
    python run_tests.py --integration   # Integration tests only
    python run_tests.py --coverage      # Run with coverage report
    python run_tests.py --quick         # Quick sanity check
"""

import sys
import os
import subprocess
from pathlib import Path


def run_command(cmd, description):
    """Run a command and print results."""
    print(f"\n{'='*70}")
    print(f"  {description}")
    print(f"{'='*70}\n")

    result = subprocess.run(cmd, shell=True)

    if result.returncode == 0:
        print(f"\n[OK] {description} - PASSED")
    else:
        print(f"\n[FAIL] {description} - FAILED")

    return result.returncode


def main():
    """Main test runner."""
    # Change to tests directory
    tests_dir = Path(__file__).parent
    os.chdir(tests_dir)

    args = sys.argv[1:]

    # Quick sanity check
    if "--quick" in args:
        return run_command(
            "python -m pytest test_sanity.py -v",
            "Quick Sanity Check"
        )

    # Unit tests only
    if "--unit" in args:
        return run_command(
            "python -m pytest test_auth.py test_core.py test_sanity.py -v",
            "Unit Tests (No Salesforce Connection Required)"
        )

    # Integration tests only
    if "--integration" in args:
        print("\n" + "="*70)
        print("  WARNING: Integration tests require Salesforce connection!")
        print("  Make sure .env is configured with valid credentials.")
        print("  Tests will create and delete data in Salesforce.")
        print("="*70)

        response = input("\nContinue? (y/n): ").lower()
        if response != 'y':
            print("Cancelled.")
            return 0

        return run_command(
            "python -m pytest test_integration.py -v --tb=short",
            "Integration Tests (Requires Salesforce)"
        )

    # Coverage report
    if "--coverage" in args:
        print("\n" + "="*70)
        print("  Running ALL tests with coverage report")
        print("="*70)

        # Check if pytest-cov is installed
        try:
            import pytest_cov
        except ImportError:
            print("\n[WARNING] pytest-cov not installed. Installing...")
            subprocess.run("pip install pytest-cov", shell=True)

        return run_command(
            "python -m pytest --cov=../kinetic_core --cov-report=html --cov-report=term -v",
            "All Tests with Coverage"
        )

    # List available options
    if "--help" in args or "-h" in args:
        print(__doc__)
        return 0

    # Default: Run all tests
    print("\n" + "="*70)
    print("  Running ALL Tests")
    print("  This includes unit tests AND integration tests")
    print("="*70)

    # Run unit tests first
    unit_result = run_command(
        "python -m pytest test_auth.py test_core.py test_sanity.py -v",
        "Unit Tests"
    )

    if unit_result != 0:
        print("\n[WARNING] Unit tests failed. Skipping integration tests.")
        return unit_result

    # Ask about integration tests
    print("\n" + "="*70)
    print("  Integration tests require Salesforce connection")
    print("="*70)

    response = input("\nRun integration tests? (y/n): ").lower()
    if response == 'y':
        integration_result = run_command(
            "python -m pytest test_integration.py -v --tb=short",
            "Integration Tests"
        )
        return integration_result
    else:
        print("\nSkipping integration tests.")
        return 0


if __name__ == "__main__":
    try:
        exit_code = main()
        sys.exit(exit_code)
    except KeyboardInterrupt:
        print("\n\nCancelled by user.")
        sys.exit(1)
    except Exception as e:
        print(f"\n[ERROR] Error: {e}")
        sys.exit(1)
