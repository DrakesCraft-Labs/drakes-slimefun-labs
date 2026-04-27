import sys
import importlib
import pytest
from pathlib import Path

def test_core_imports():
    """Verify that all core modules can be imported."""
    modules = [
        "kinetic_core",
        "kinetic_core.auth",
        "kinetic_core.core",
        "kinetic_core.mapping",
        "kinetic_core.pipeline",
        "kinetic_core.logging",
        "kinetic_core.utils",
    ]
    for module_name in modules:
        try:
            importlib.import_module(module_name)
        except ImportError as e:
            pytest.fail(f"Failed to import {module_name}: {e}")

def test_cli_import():
    """Verify cli module is importable and has main function."""
    try:
        # Add root dir to path to find cli.py
        root_dir = Path(__file__).parent.parent
        sys.path.insert(0, str(root_dir))
        
        import cli
        assert hasattr(cli, 'main')
        assert callable(cli.main)
    except ImportError as e:
        pytest.fail(f"Failed to import cli: {e}")
