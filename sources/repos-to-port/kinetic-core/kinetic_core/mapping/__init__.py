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
Mapping module for Salesforce Toolkit.

Provides flexible field mapping and data transformation capabilities.
"""

from kinetic_core.mapping.field_mapper import FieldMapper, ConditionalFieldMapper

__all__ = ["FieldMapper", "ConditionalFieldMapper"]
