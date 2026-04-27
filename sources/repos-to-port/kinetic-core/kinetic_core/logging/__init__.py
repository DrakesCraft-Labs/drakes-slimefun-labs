"""
Kinetic Core - Salesforce Integration Library
Copyright (c) 2025 Antonio Trento (https://antoniotrento.net)

This file is part of Kinetic Core, the foundational library powering KineticMCP.

Project: https://github.com/antonio-backend-projects/kinetic-core
Website: https://kineticmcp.com
Author: Antonio Trento
License: MIT (see LICENSE file for details)

Part of the KineticMCP ecosystem - AI-powered Salesforce integration tools.
"""

"""
Logging module for Salesforce Toolkit.

Provides comprehensive logging capabilities with file rotation,
console output, and contextual logging.
"""

from kinetic_core.logging.logger import (
    setup_logger,
    get_logger,
    ContextLogger,
    configure_logging_from_env
)

__all__ = [
    "setup_logger",
    "get_logger",
    "ContextLogger",
    "configure_logging_from_env"
]
