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
Field Mapping Engine - Transform data between different schemas.

Provides flexible field mapping with:
- Simple field renaming
- Value transformations
- Default values
- Conditional mapping
- Nested field access
"""

import logging
from typing import Dict, Any, Callable, Optional, List
from datetime import datetime, date


logger = logging.getLogger(__name__)


class FieldMapper:
    """
    Generic field mapper for transforming data between schemas.

    Supports:
    - Field renaming (source_field -> target_field)
    - Value transformations (via custom functions)
    - Default values (when source field is None/missing)
    - Conditional mapping (map only if condition is met)
    - Nested field access (e.g., "address.city")

    Example:
        ```python
        mapper = FieldMapper({
            "customer_name": "Name",
            "customer_email": "Email",
            "created_date": ("CreatedDate", lambda x: x.strftime("%Y-%m-%d")),
            "phone": ("Phone", None, "000-000-0000")  # with default
        })

        source = {"customer_name": "ACME Corp", "customer_email": "info@acme.com"}
        target = mapper.transform(source)
        # Result: {"Name": "ACME Corp", "Email": "info@acme.com"}
        ```
    """

    def __init__(self, mapping: Dict[str, Any]):
        """
        Initialize field mapper with mapping configuration.

        Args:
            mapping: Mapping configuration with formats:
                - Simple: {"source_field": "target_field"}
                - With transform: {"source": ("target", transform_fn)}
                - With default: {"source": ("target", None, "default_value")}
                - Full: {"source": ("target", transform_fn, "default_value")}

        Example:
            ```python
            mapping = {
                # Simple rename
                "first_name": "FirstName",

                # With transformation
                "email": ("Email", lambda x: x.lower()),

                # With default value
                "status": ("Status", None, "Active"),

                # With both transformation and default
                "created_at": (
                    "CreatedDate",
                    lambda x: x.strftime("%Y-%m-%d") if x else None,
                    datetime.now().strftime("%Y-%m-%d")
                )
            }
            ```
        """
        self.mapping = self._normalize_mapping(mapping)
        self.logger = logger

    def _normalize_mapping(self, mapping: Dict[str, Any]) -> Dict[str, Dict[str, Any]]:
        """
        Normalize mapping configuration to consistent format.

        Args:
            mapping: Raw mapping configuration

        Returns:
            Dict: Normalized mapping with structure:
                {
                    "source_field": {
                        "target": "target_field",
                        "transform": callable or None,
                        "default": default_value or None
                    }
                }
        """
        normalized = {}

        for source_field, config in mapping.items():
            if isinstance(config, str):
                # Simple string mapping: {"source": "target"}
                normalized[source_field] = {
                    "target": config,
                    "transform": None,
                    "default": None
                }
            elif isinstance(config, tuple):
                # Tuple format: ("target", transform_fn, default_value)
                target = config[0]
                transform = config[1] if len(config) > 1 else None
                default = config[2] if len(config) > 2 else None

                normalized[source_field] = {
                    "target": target,
                    "transform": transform,
                    "default": default
                }
            else:
                raise ValueError(
                    f"Invalid mapping config for '{source_field}': {config}. "
                    f"Expected str or tuple."
                )

        return normalized

    def transform(
        self,
        source: Dict[str, Any],
        skip_none: bool = True,
        strict: bool = False
    ) -> Dict[str, Any]:
        """
        Transform source data to target schema using mapping configuration.

        Args:
            source: Source data dictionary
            skip_none: If True, skip fields with None values
            strict: If True, raise error if source field is missing

        Returns:
            Dict: Transformed data in target schema

        Raises:
            KeyError: If strict=True and source field is missing
            Exception: If transformation function fails

        Example:
            ```python
            source = {
                "first_name": "John",
                "last_name": "Doe",
                "email": "JOHN.DOE@EXAMPLE.COM"
            }

            mapping = {
                "first_name": "FirstName",
                "last_name": "LastName",
                "email": ("Email", lambda x: x.lower())
            }

            mapper = FieldMapper(mapping)
            result = mapper.transform(source)

            # Result: {
            #   "FirstName": "John",
            #   "LastName": "Doe",
            #   "Email": "john.doe@example.com"
            # }
            ```
        """
        target = {}

        for source_field, config in self.mapping.items():
            target_field = config["target"]
            transform_fn = config["transform"]
            default_value = config["default"]

            # Get source value (supports nested access with dot notation)
            try:
                value = self._get_nested_value(source, source_field)
            except KeyError:
                if strict:
                    raise KeyError(f"Source field '{source_field}' not found")
                value = None

            # Apply default if value is None
            if value is None and default_value is not None:
                value = default_value

            # Skip None values if requested
            if value is None and skip_none:
                continue

            # Apply transformation function
            if transform_fn is not None and value is not None:
                try:
                    value = transform_fn(value)
                except Exception as e:
                    self.logger.error(
                        f"Transform failed for {source_field} -> {target_field}: {e}"
                    )
                    raise

            # Set target value (supports nested keys)
            self._set_nested_value(target, target_field, value)

        return target

    def transform_batch(
        self,
        source_list: List[Dict[str, Any]],
        skip_none: bool = True,
        strict: bool = False
    ) -> List[Dict[str, Any]]:
        """
        Transform a list of source records to target schema.

        Args:
            source_list: List of source data dictionaries
            skip_none: If True, skip fields with None values
            strict: If True, raise error if source field is missing

        Returns:
            List[Dict]: List of transformed records

        Example:
            ```python
            source_list = [
                {"name": "Alice", "age": 30},
                {"name": "Bob", "age": 25}
            ]

            mapper = FieldMapper({"name": "Name", "age": "Age__c"})
            results = mapper.transform_batch(source_list)
            ```
        """
        results = []

        for i, source in enumerate(source_list):
            try:
                transformed = self.transform(source, skip_none=skip_none, strict=strict)
                results.append(transformed)
            except Exception as e:
                self.logger.error(f"Failed to transform record #{i}: {e}")
                if strict:
                    raise

        return results

    def _get_nested_value(self, data: Dict[str, Any], key: str) -> Any:
        """
        Get value from nested dictionary using dot notation.

        Args:
            data: Source dictionary
            key: Key (supports dot notation, e.g., "address.city")

        Returns:
            Any: Value at key path

        Raises:
            KeyError: If key path doesn't exist

        Example:
            ```python
            data = {"user": {"address": {"city": "NYC"}}}
            value = self._get_nested_value(data, "user.address.city")
            # Returns: "NYC"
            ```
        """
        keys = key.split('.')
        value = data

        for k in keys:
            if isinstance(value, dict):
                value = value.get(k)
                if value is None:
                    raise KeyError(f"Key '{k}' not found in path '{key}'")
            else:
                raise KeyError(f"Cannot access '{k}' in non-dict value")

        return value

    def _set_nested_value(self, data: Dict[str, Any], key: str, value: Any) -> None:
        """
        Set value in nested dictionary using dot notation.

        Args:
            data: Target dictionary (modified in place)
            key: Key (supports dot notation)
            value: Value to set

        Example:
            ```python
            data = {}
            self._set_nested_value(data, "user.address.city", "NYC")
            # Result: {"user": {"address": {"city": "NYC"}}}
            ```
        """
        keys = key.split('.')

        for k in keys[:-1]:
            if k not in data:
                data[k] = {}
            data = data[k]

        data[keys[-1]] = value

    @classmethod
    def from_yaml(cls, yaml_path: str) -> "FieldMapper":
        """
        Create FieldMapper from YAML configuration file.

        Args:
            yaml_path: Path to YAML file

        Returns:
            FieldMapper: Configured mapper

        YAML Format:
            ```yaml
            mapping:
              source_field1: target_field1
              source_field2:
                target: target_field2
                transform: lowercase  # Built-in transform
              source_field3:
                target: target_field3
                default: "default_value"
            ```
        """
        import yaml

        with open(yaml_path, 'r') as f:
            config = yaml.safe_load(f)

        mapping_config = config.get('mapping', {})

        # Convert YAML config to internal format
        mapping = {}
        for source, target_config in mapping_config.items():
            if isinstance(target_config, str):
                mapping[source] = target_config
            elif isinstance(target_config, dict):
                target = target_config['target']
                transform_name = target_config.get('transform')
                default = target_config.get('default')

                # Resolve built-in transforms
                transform_fn = cls._get_builtin_transform(transform_name) if transform_name else None

                mapping[source] = (target, transform_fn, default)

        return cls(mapping)

    @staticmethod
    def _get_builtin_transform(name: str) -> Optional[Callable]:
        """
        Get built-in transformation function by name.

        Args:
            name: Transform name

        Returns:
            Callable: Transform function or None
        """
        transforms = {
            "lowercase": lambda x: x.lower() if isinstance(x, str) else x,
            "uppercase": lambda x: x.upper() if isinstance(x, str) else x,
            "strip": lambda x: x.strip() if isinstance(x, str) else x,
            "int": lambda x: int(x) if x is not None else None,
            "float": lambda x: float(x) if x is not None else None,
            "bool": lambda x: bool(x) if x is not None else None,
            "date_iso": lambda x: x.strftime("%Y-%m-%d") if isinstance(x, (datetime, date)) else x,
            "datetime_iso": lambda x: x.isoformat() if isinstance(x, datetime) else x,
        }

        return transforms.get(name)


class ConditionalFieldMapper(FieldMapper):
    """
    Field mapper with conditional logic.

    Extends FieldMapper to support conditional field mapping based on
    source data values.

    Example:
        ```python
        mapper = ConditionalFieldMapper(
            mapping={
                "name": "Name",
                "type": "Type"
            },
            conditions={
                "Industry": lambda data: "Technology" if data.get("type") == "tech" else "Other"
            }
        )

        source = {"name": "ACME", "type": "tech"}
        result = mapper.transform(source)
        # Result: {"Name": "ACME", "Type": "tech", "Industry": "Technology"}
        ```
    """

    def __init__(self, mapping: Dict[str, Any], conditions: Dict[str, Callable] = None):
        """
        Initialize conditional field mapper.

        Args:
            mapping: Standard field mapping configuration
            conditions: Dict of {target_field: condition_function(source_data)}
        """
        super().__init__(mapping)
        self.conditions = conditions or {}

    def transform(
        self,
        source: Dict[str, Any],
        skip_none: bool = True,
        strict: bool = False
    ) -> Dict[str, Any]:
        """
        Transform with conditional fields.

        Args:
            source: Source data
            skip_none: Skip None values
            strict: Strict mode

        Returns:
            Dict: Transformed data with conditional fields
        """
        # Apply standard mapping
        target = super().transform(source, skip_none=skip_none, strict=strict)

        # Apply conditional fields
        for target_field, condition_fn in self.conditions.items():
            try:
                value = condition_fn(source)
                if value is not None or not skip_none:
                    target[target_field] = value
            except Exception as e:
                self.logger.error(f"Condition failed for {target_field}: {e}")
                if strict:
                    raise

        return target
