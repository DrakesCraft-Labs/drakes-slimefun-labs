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
CSV serialization/deserialization for Bulk API v2.
"""

import csv
import io
from typing import List, Dict, Any


class CSVSerializer:
    """Handles CSV conversion for Bulk API v2 operations."""

    @staticmethod
    def records_to_csv(records: List[Dict[str, Any]]) -> str:
        """
        Convert list of records to CSV string.

        Args:
            records: List of dictionaries representing records

        Returns:
            CSV string with headers and data rows

        Example:
            >>> records = [
            ...     {"Name": "Acme Corp", "Industry": "Technology"},
            ...     {"Name": "Global Inc", "Industry": "Manufacturing"}
            ... ]
            >>> csv_str = CSVSerializer.records_to_csv(records)
        """
        if not records:
            return ""

        # Get all unique field names from all records
        all_fields = set()
        for record in records:
            all_fields.update(record.keys())

        # Sort fields for consistent ordering (Id first if present)
        fields = sorted(all_fields)
        if 'Id' in fields:
            fields.remove('Id')
            fields.insert(0, 'Id')

        # Write CSV
        output = io.StringIO()
        writer = csv.DictWriter(output, fieldnames=fields, extrasaction='ignore', lineterminator='\n')
        writer.writeheader()
        writer.writerows(records)

        return output.getvalue()

    @staticmethod
    def csv_to_records(csv_str: str) -> List[Dict[str, Any]]:
        """
        Convert CSV string to list of records.

        Args:
            csv_str: CSV string with headers

        Returns:
            List of dictionaries representing records

        Example:
            >>> csv_str = "Name,Industry\\nAcme Corp,Technology"
            >>> records = CSVSerializer.csv_to_records(csv_str)
        """
        if not csv_str or not csv_str.strip():
            return []

        input_stream = io.StringIO(csv_str)
        reader = csv.DictReader(input_stream)

        records = []
        for row in reader:
            # Remove empty values
            record = {k: v for k, v in row.items() if v}
            if record:  # Only add non-empty records
                records.append(record)

        return records

    @staticmethod
    def ids_to_csv(ids: List[str]) -> str:
        """
        Convert list of IDs to CSV string for delete operations.

        Args:
            ids: List of Salesforce record IDs

        Returns:
            CSV string with Id header and ID values

        Example:
            >>> ids = ["001xxx", "001yyy"]
            >>> csv_str = CSVSerializer.ids_to_csv(ids)
            # Returns: "Id\\n001xxx\\n001yyy\\n"
        """
        if not ids:
            return ""

        output = io.StringIO()
        writer = csv.writer(output, lineterminator='\n')
        writer.writerow(['Id'])
        for record_id in ids:
            writer.writerow([record_id])

        return output.getvalue()

    @staticmethod
    def validate_csv(csv_str: str) -> bool:
        """
        Validate CSV format.

        Args:
            csv_str: CSV string to validate

        Returns:
            True if valid CSV, False otherwise
        """
        try:
            if not csv_str or not csv_str.strip():
                return False

            input_stream = io.StringIO(csv_str)
            reader = csv.reader(input_stream)

            # Check if has header
            header = next(reader, None)
            if not header:
                return False

            # Check if has at least one data row
            first_row = next(reader, None)
            if not first_row:
                return False

            return True
        except Exception:
            return False
