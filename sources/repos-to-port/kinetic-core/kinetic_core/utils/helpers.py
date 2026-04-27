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
Utility helpers for Salesforce Toolkit.

Provides common utility functions for data manipulation,
validation, and Salesforce-specific operations.
"""

import re
from typing import Any, Dict, List, Optional
from datetime import datetime, date


def sanitize_soql(value: str) -> str:
    """
    Sanitize a string value for use in SOQL queries.

    Escapes single quotes and handles special characters.

    Args:
        value: String value to sanitize

    Returns:
        str: Sanitized string safe for SOQL

    Example:
        ```python
        name = "O'Brien & Associates"
        safe_name = sanitize_soql(name)
        query = f"SELECT Id FROM Account WHERE Name = '{safe_name}'"
        ```
    """
    if not isinstance(value, str):
        return str(value)

    # Escape single quotes
    return value.replace("'", "\\'")


def build_soql_query(
    sobject: str,
    fields: List[str],
    where: Optional[str] = None,
    order_by: Optional[str] = None,
    limit: Optional[int] = None
) -> str:
    """
    Build a SOQL query string.

    Args:
        sobject: Salesforce object name
        fields: List of field names to select
        where: Optional WHERE clause (without 'WHERE' keyword)
        order_by: Optional ORDER BY clause (without 'ORDER BY' keyword)
        limit: Optional LIMIT value

    Returns:
        str: Complete SOQL query

    Example:
        ```python
        query = build_soql_query(
            sobject="Account",
            fields=["Id", "Name", "Industry"],
            where="Industry = 'Technology'",
            order_by="Name ASC",
            limit=100
        )
        # Returns: "SELECT Id, Name, Industry FROM Account WHERE Industry = 'Technology' ORDER BY Name ASC LIMIT 100"
        ```
    """
    query = f"SELECT {', '.join(fields)} FROM {sobject}"

    if where:
        query += f" WHERE {where}"

    if order_by:
        query += f" ORDER BY {order_by}"

    if limit:
        query += f" LIMIT {limit}"

    return query


def validate_salesforce_id(sf_id: str) -> bool:
    """
    Validate a Salesforce ID format.

    Salesforce IDs are either 15 or 18 characters (case-sensitive or case-insensitive).

    Args:
        sf_id: Salesforce ID to validate

    Returns:
        bool: True if valid ID format

    Example:
        ```python
        if validate_salesforce_id("001XXXXXXXXXXXXXXX"):
            print("Valid ID")
        ```
    """
    if not isinstance(sf_id, str):
        return False

    # Salesforce IDs are 15 or 18 alphanumeric characters
    return bool(re.match(r'^[a-zA-Z0-9]{15}([a-zA-Z0-9]{3})?$', sf_id))


def chunk_list(lst: List[Any], chunk_size: int) -> List[List[Any]]:
    """
    Split a list into chunks of specified size.

    Args:
        lst: List to chunk
        chunk_size: Size of each chunk

    Returns:
        List[List]: List of chunks

    Example:
        ```python
        items = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        chunks = chunk_list(items, 3)
        # Returns: [[1, 2, 3], [4, 5, 6], [7, 8, 9], [10]]
        ```
    """
    return [lst[i:i + chunk_size] for i in range(0, len(lst), chunk_size)]


def format_datetime_for_sf(dt: datetime) -> str:
    """
    Format a datetime for Salesforce (ISO 8601 format).

    Args:
        dt: Datetime to format

    Returns:
        str: ISO 8601 formatted datetime

    Example:
        ```python
        now = datetime.now()
        sf_datetime = format_datetime_for_sf(now)
        # Returns: "2025-12-05T10:30:00Z"
        ```
    """
    if isinstance(dt, datetime):
        return dt.strftime("%Y-%m-%dT%H:%M:%SZ")
    elif isinstance(dt, date):
        return dt.strftime("%Y-%m-%d")
    else:
        raise ValueError(f"Expected datetime or date, got {type(dt)}")


def parse_sf_datetime(sf_datetime: str) -> datetime:
    """
    Parse a Salesforce datetime string to Python datetime.

    Args:
        sf_datetime: Salesforce datetime string

    Returns:
        datetime: Parsed datetime

    Example:
        ```python
        sf_str = "2025-12-05T10:30:00.000Z"
        dt = parse_sf_datetime(sf_str)
        ```
    """
    # Handle different Salesforce datetime formats
    formats = [
        "%Y-%m-%dT%H:%M:%S.%fZ",  # With milliseconds
        "%Y-%m-%dT%H:%M:%SZ",      # Without milliseconds
        "%Y-%m-%d"                  # Date only
    ]

    for fmt in formats:
        try:
            return datetime.strptime(sf_datetime, fmt)
        except ValueError:
            continue

    raise ValueError(f"Cannot parse Salesforce datetime: {sf_datetime}")


def extract_field_names_from_query(soql: str) -> List[str]:
    """
    Extract field names from a SOQL SELECT query.

    Args:
        soql: SOQL query string

    Returns:
        List[str]: List of field names

    Example:
        ```python
        query = "SELECT Id, Name, Email FROM Contact WHERE Email != null"
        fields = extract_field_names_from_query(query)
        # Returns: ['Id', 'Name', 'Email']
        ```
    """
    # Simple regex to extract SELECT ... FROM
    match = re.search(r'SELECT\s+(.+?)\s+FROM', soql, re.IGNORECASE)

    if not match:
        return []

    fields_str = match.group(1)

    # Split by comma and strip whitespace
    fields = [f.strip() for f in fields_str.split(',')]

    return fields


def get_sobject_from_id(sf_id: str) -> Optional[str]:
    """
    Get Salesforce object type from ID prefix.

    Args:
        sf_id: Salesforce record ID

    Returns:
        Optional[str]: Object type or None if unknown

    Note: This uses common prefixes but is not exhaustive.

    Example:
        ```python
        obj_type = get_sobject_from_id("001XXXXXXXXXXXXXXX")
        # Returns: "Account"
        ```
    """
    if not validate_salesforce_id(sf_id):
        return None

    # Common object prefixes
    prefixes = {
        '001': 'Account',
        '003': 'Contact',
        '005': 'User',
        '006': 'Opportunity',
        '00Q': 'Lead',
        '00G': 'Task',
        '00T': 'Event',
        '500': 'Case',
        '701': 'Campaign',
        '00O': 'Report',
        '01t': 'Product2',
        '0Q0': 'Quote',
        '0QL': 'QuoteLineItem',
    }

    prefix = sf_id[:3]
    return prefixes.get(prefix)


def flatten_dict(d: Dict[str, Any], parent_key: str = '', sep: str = '.') -> Dict[str, Any]:
    """
    Flatten a nested dictionary.

    Args:
        d: Dictionary to flatten
        parent_key: Parent key prefix
        sep: Separator for nested keys

    Returns:
        Dict: Flattened dictionary

    Example:
        ```python
        nested = {
            "user": {
                "name": "John",
                "address": {
                    "city": "NYC"
                }
            }
        }

        flat = flatten_dict(nested)
        # Returns: {"user.name": "John", "user.address.city": "NYC"}
        ```
    """
    items = []

    for k, v in d.items():
        new_key = f"{parent_key}{sep}{k}" if parent_key else k

        if isinstance(v, dict):
            items.extend(flatten_dict(v, new_key, sep=sep).items())
        else:
            items.append((new_key, v))

    return dict(items)


def unflatten_dict(d: Dict[str, Any], sep: str = '.') -> Dict[str, Any]:
    """
    Unflatten a dictionary with dot-notation keys.

    Args:
        d: Flattened dictionary
        sep: Separator used in keys

    Returns:
        Dict: Nested dictionary

    Example:
        ```python
        flat = {"user.name": "John", "user.address.city": "NYC"}
        nested = unflatten_dict(flat)
        # Returns: {"user": {"name": "John", "address": {"city": "NYC"}}}
        ```
    """
    result = {}

    for key, value in d.items():
        parts = key.split(sep)
        d_ref = result

        for part in parts[:-1]:
            if part not in d_ref:
                d_ref[part] = {}
            d_ref = d_ref[part]

        d_ref[parts[-1]] = value

    return result


def compare_records(record1: Dict[str, Any], record2: Dict[str, Any], ignore_fields: Optional[List[str]] = None) -> Dict[str, Any]:
    """
    Compare two Salesforce records and return differences.

    Args:
        record1: First record
        record2: Second record
        ignore_fields: Fields to ignore in comparison (e.g., 'Id', 'LastModifiedDate')

    Returns:
        Dict: Dictionary with differences {field: (value1, value2)}

    Example:
        ```python
        rec1 = {"Id": "001XXX", "Name": "ACME", "Phone": "555-0100"}
        rec2 = {"Id": "001XXX", "Name": "ACME Corp", "Phone": "555-0100"}

        diffs = compare_records(rec1, rec2, ignore_fields=["Id"])
        # Returns: {"Name": ("ACME", "ACME Corp")}
        ```
    """
    ignore_fields = ignore_fields or []
    differences = {}

    all_fields = set(record1.keys()) | set(record2.keys())

    for field in all_fields:
        if field in ignore_fields:
            continue

        val1 = record1.get(field)
        val2 = record2.get(field)

        if val1 != val2:
            differences[field] = (val1, val2)

    return differences


def generate_external_id(prefix: str = "EXT", timestamp: bool = True) -> str:
    """
    Generate a unique external ID.

    Args:
        prefix: Prefix for the ID
        timestamp: If True, include timestamp

    Returns:
        str: Generated external ID

    Example:
        ```python
        ext_id = generate_external_id("CUST", timestamp=True)
        # Returns: "CUST-20251205-103000-abc123"
        ```
    """
    import uuid

    parts = [prefix]

    if timestamp:
        parts.append(datetime.now().strftime("%Y%m%d-%H%M%S"))

    # Add random UUID suffix
    parts.append(str(uuid.uuid4())[:8])

    return "-".join(parts)


def batch_records(records: List[Dict[str, Any]], batch_size: int = 200) -> List[List[Dict[str, Any]]]:
    """
    Batch records for bulk operations (alias for chunk_list).

    Args:
        records: List of records
        batch_size: Size of each batch (Salesforce limit: 200 for composite API)

    Returns:
        List[List[Dict]]: Batched records

    Example:
        ```python
        records = [{"Name": f"Account {i}"} for i in range(500)]
        batches = batch_records(records, batch_size=200)
        # Returns: 3 batches of 200, 200, and 100 records
        ```
    """
    return chunk_list(records, batch_size)
