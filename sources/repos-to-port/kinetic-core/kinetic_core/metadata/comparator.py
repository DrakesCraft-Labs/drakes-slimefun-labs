"""
Metadata comparison utilities.

Compares metadata between different sources (local vs org, org vs org).
"""

from typing import Dict, List, Any, Optional, Set
from pathlib import Path
from dataclasses import dataclass, field

from kinetic_core.metadata.xml_parser import (
    parse_custom_field,
    parse_custom_object,
)


@dataclass
class MetadataDiff:
    """Represents differences between metadata sources."""

    added: List[Dict[str, Any]] = field(default_factory=list)
    """Components that exist in target but not in source."""

    modified: List[Dict[str, Any]] = field(default_factory=list)
    """Components that exist in both but have differences."""

    deleted: List[Dict[str, Any]] = field(default_factory=list)
    """Components that exist in source but not in target."""

    unchanged: List[str] = field(default_factory=list)
    """Components that are identical in both sources."""

    @property
    def has_changes(self) -> bool:
        """Check if there are any differences."""
        return bool(self.added or self.modified or self.deleted)

    @property
    def summary(self) -> Dict[str, int]:
        """Get summary counts."""
        return {
            "added": len(self.added),
            "modified": len(self.modified),
            "deleted": len(self.deleted),
            "unchanged": len(self.unchanged),
        }


class MetadataComparator:
    """
    Compares metadata between different sources.

    Can compare:
    - Local directory vs Salesforce org
    - Two different Salesforce orgs
    - Two local directories
    """

    def __init__(self):
        """Initialize comparator."""
        pass

    def compare_directories(
        self,
        source_dir: str,
        target_dir: str,
        component_types: Optional[List[str]] = None,
    ) -> MetadataDiff:
        """
        Compare metadata between two local directories.

        Args:
            source_dir: Source metadata directory
            target_dir: Target metadata directory
            component_types: Optional list of component types to compare
                           (default: compare all)

        Returns:
            MetadataDiff with differences

        Example:
            >>> comparator = MetadataComparator()
            >>> diff = comparator.compare_directories(
            ...     "./source_metadata",
            ...     "./target_metadata"
            ... )
            >>> print(f"Added: {diff.summary['added']}")
        """
        source_path = Path(source_dir)
        target_path = Path(target_dir)

        # Scan both directories
        source_files = self._scan_directory(source_path, component_types)
        target_files = self._scan_directory(target_path, component_types)

        # Find differences
        diff = MetadataDiff()

        # Get all file keys
        all_keys = set(source_files.keys()) | set(target_files.keys())

        for key in all_keys:
            source_file = source_files.get(key)
            target_file = target_files.get(key)

            if source_file and not target_file:
                # Deleted (exists in source, not in target)
                diff.deleted.append({
                    "type": self._get_component_type(key),
                    "name": key,
                    "path": str(source_file),
                })

            elif target_file and not source_file:
                # Added (exists in target, not in source)
                diff.added.append({
                    "type": self._get_component_type(key),
                    "name": key,
                    "path": str(target_file),
                })

            else:
                # Both exist - check if modified
                if self._files_differ(source_file, target_file):
                    diff.modified.append({
                        "type": self._get_component_type(key),
                        "name": key,
                        "source_path": str(source_file),
                        "target_path": str(target_file),
                    })
                else:
                    diff.unchanged.append(key)

        return diff

    def compare_fields(
        self,
        source_fields: List[Dict[str, Any]],
        target_fields: List[Dict[str, Any]],
    ) -> MetadataDiff:
        """
        Compare custom fields between two sets.

        Args:
            source_fields: Source field definitions
            target_fields: Target field definitions

        Returns:
            MetadataDiff with field differences
        """
        diff = MetadataDiff()

        # Create field maps by name
        source_map = {f["name"]: f for f in source_fields}
        target_map = {f["name"]: f for f in target_fields}

        all_names = set(source_map.keys()) | set(target_map.keys())

        for name in all_names:
            source_field = source_map.get(name)
            target_field = target_map.get(name)

            if source_field and not target_field:
                diff.deleted.append({
                    "type": "CustomField",
                    "name": name,
                    "details": source_field,
                })

            elif target_field and not source_field:
                diff.added.append({
                    "type": "CustomField",
                    "name": name,
                    "details": target_field,
                })

            else:
                # Compare field properties
                if self._field_properties_differ(source_field, target_field):
                    diff.modified.append({
                        "type": "CustomField",
                        "name": name,
                        "source": source_field,
                        "target": target_field,
                        "changes": self._get_field_changes(source_field, target_field),
                    })
                else:
                    diff.unchanged.append(name)

        return diff

    def get_deployment_order(
        self,
        components: List[Dict[str, Any]]
    ) -> List[List[Dict[str, Any]]]:
        """
        Order components for deployment based on dependencies.

        Returns components grouped by deployment phase.
        Phase 1: Independent components (Custom Objects, etc.)
        Phase 2: Dependent components (Custom Fields, etc.)
        Phase 3: Automation (Workflows, Validation Rules, etc.)

        Args:
            components: List of component definitions

        Returns:
            List of component groups in deployment order
        """
        phase_1 = []  # Objects
        phase_2 = []  # Fields
        phase_3 = []  # Automation

        for component in components:
            comp_type = component.get("type", "")

            if comp_type in ("CustomObject",):
                phase_1.append(component)
            elif comp_type in ("CustomField", "FieldSet", "RecordType"):
                phase_2.append(component)
            elif comp_type in (
                "ValidationRule",
                "WorkflowRule",
                "ApexTrigger",
                "ApexClass",
            ):
                phase_3.append(component)
            else:
                # Unknown - add to phase 1
                phase_1.append(component)

        # Return non-empty phases
        result = []
        if phase_1:
            result.append(phase_1)
        if phase_2:
            result.append(phase_2)
        if phase_3:
            result.append(phase_3)

        return result

    def filter_components(
        self,
        diff: MetadataDiff,
        include_types: Optional[List[str]] = None,
        exclude_types: Optional[List[str]] = None,
        include_names: Optional[List[str]] = None,
        exclude_names: Optional[List[str]] = None,
    ) -> MetadataDiff:
        """
        Filter components based on criteria.

        Args:
            diff: Original MetadataDiff
            include_types: Only include these component types
            exclude_types: Exclude these component types
            include_names: Only include components matching these names (regex)
            exclude_names: Exclude components matching these names (regex)

        Returns:
            Filtered MetadataDiff
        """
        import re

        def matches_filter(component: Dict[str, Any]) -> bool:
            """Check if component matches filters."""
            comp_type = component.get("type", "")
            comp_name = component.get("name", "")

            # Type filters
            if include_types and comp_type not in include_types:
                return False
            if exclude_types and comp_type in exclude_types:
                return False

            # Name filters
            if include_names:
                if not any(re.search(pattern, comp_name) for pattern in include_names):
                    return False
            if exclude_names:
                if any(re.search(pattern, comp_name) for pattern in exclude_names):
                    return False

            return True

        # Apply filters
        filtered_diff = MetadataDiff(
            added=[c for c in diff.added if matches_filter(c)],
            modified=[c for c in diff.modified if matches_filter(c)],
            deleted=[c for c in diff.deleted if matches_filter(c)],
            unchanged=diff.unchanged,  # Keep unchanged as-is
        )

        return filtered_diff

    # Helper methods

    def _scan_directory(
        self,
        directory: Path,
        component_types: Optional[List[str]] = None,
    ) -> Dict[str, Path]:
        """
        Scan directory for metadata files.

        Returns dict mapping component key to file path.
        """
        files = {}

        if not directory.exists():
            return files

        # Scan for metadata files
        patterns = [
            "**/*.object-meta.xml",
            "**/*.field-meta.xml",
            "**/*.validationRule-meta.xml",
            "**/*.workflow-meta.xml",
        ]

        for pattern in patterns:
            for file_path in directory.glob(pattern):
                # Extract component key (type + name)
                key = self._get_component_key(file_path)
                if key:
                    # Check if type filter applies
                    if component_types:
                        comp_type = self._get_component_type(key)
                        if comp_type not in component_types:
                            continue
                    files[key] = file_path

        return files

    def _get_component_key(self, file_path: Path) -> Optional[str]:
        """Extract component key from file path."""
        filename = file_path.name

        if filename.endswith(".object-meta.xml"):
            name = filename.replace(".object-meta.xml", "")
            return f"CustomObject:{name}"
        elif filename.endswith(".field-meta.xml"):
            name = filename.replace(".field-meta.xml", "")
            # Get parent directory (object name)
            parent = file_path.parent.name
            return f"CustomField:{parent}.{name}"
        elif filename.endswith(".validationRule-meta.xml"):
            name = filename.replace(".validationRule-meta.xml", "")
            parent = file_path.parent.name
            return f"ValidationRule:{parent}.{name}"
        elif filename.endswith(".workflow-meta.xml"):
            name = filename.replace(".workflow-meta.xml", "")
            parent = file_path.parent.name
            return f"WorkflowRule:{parent}.{name}"

        return None

    def _get_component_type(self, key: str) -> str:
        """Extract component type from key."""
        if ":" in key:
            return key.split(":")[0]
        return "Unknown"

    def _files_differ(self, file1: Path, file2: Path) -> bool:
        """Check if two files have different content."""
        try:
            content1 = file1.read_text(encoding="utf-8")
            content2 = file2.read_text(encoding="utf-8")

            # Normalize whitespace for comparison
            content1 = " ".join(content1.split())
            content2 = " ".join(content2.split())

            return content1 != content2
        except Exception:
            # If we can't read files, assume they differ
            return True

    def _field_properties_differ(
        self,
        field1: Dict[str, Any],
        field2: Dict[str, Any],
    ) -> bool:
        """Check if two field definitions differ in important properties."""
        # Properties to compare
        properties = [
            "type",
            "label",
            "length",
            "precision",
            "scale",
            "required",
            "unique",
            "externalId",
            "description",
        ]

        for prop in properties:
            if field1.get(prop) != field2.get(prop):
                return True

        return False

    def _get_field_changes(
        self,
        source_field: Dict[str, Any],
        target_field: Dict[str, Any],
    ) -> Dict[str, Dict[str, Any]]:
        """Get detailed changes between two field definitions."""
        changes = {}

        properties = [
            "type",
            "label",
            "length",
            "precision",
            "scale",
            "required",
            "unique",
            "externalId",
            "description",
        ]

        for prop in properties:
            source_val = source_field.get(prop)
            target_val = target_field.get(prop)

            if source_val != target_val:
                changes[prop] = {
                    "from": source_val,
                    "to": target_val,
                }

        return changes
