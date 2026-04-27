"""
XML deserialization for Salesforce Metadata API components.

Parses Salesforce Metadata API XML format to Python data models.
"""

import xml.etree.ElementTree as ET
from typing import Optional, List

from kinetic_core.metadata.models import (
    CustomField,
    CustomObject,
    ValidationRule,
    WorkflowRule,
    PicklistValue,
    FieldType,
    SharingModel,
)


# Salesforce Metadata API namespace
METADATA_NS = "{http://soap.sforce.com/2006/04/metadata}"


def _get_text(elem: Optional[ET.Element], tag: str, default: str = "") -> str:
    """Get text content of a subelement."""
    if elem is None:
        return default
    child = elem.find(f"{METADATA_NS}{tag}")
    return child.text if child is not None and child.text else default


def _get_bool(elem: Optional[ET.Element], tag: str, default: bool = False) -> bool:
    """Get boolean value of a subelement."""
    text = _get_text(elem, tag, str(default).lower())
    return text.lower() == "true"


def _get_int(elem: Optional[ET.Element], tag: str, default: Optional[int] = None) -> Optional[int]:
    """Get integer value of a subelement."""
    text = _get_text(elem, tag)
    if not text:
        return default
    try:
        return int(text)
    except ValueError:
        return default


def parse_picklist_value(elem: ET.Element) -> PicklistValue:
    """Parse a picklist value XML element."""
    return PicklistValue(
        full_name=_get_text(elem, "fullName"),
        default=_get_bool(elem, "default", False),
        label=_get_text(elem, "label") or None,
        color=_get_text(elem, "color") or None,
    )


def parse_custom_field(xml_content: str, sobject: str) -> CustomField:
    """
    Parse CustomField from Salesforce Metadata API XML format.

    Args:
        xml_content: XML string in Salesforce Metadata API format
        sobject: The SObject this field belongs to

    Returns:
        CustomField instance

    Example:
        >>> xml = '''<?xml version="1.0"?>
        ... <CustomField xmlns="http://soap.sforce.com/2006/04/metadata">
        ...     <fullName>Customer_Tier__c</fullName>
        ...     <label>Customer Tier</label>
        ...     <type>Text</type>
        ...     <length>50</length>
        ... </CustomField>'''
        >>> field = parse_custom_field(xml, "Account")
    """
    root = ET.fromstring(xml_content)

    # Basic attributes
    name = _get_text(root, "fullName")
    label = _get_text(root, "label")
    field_type_str = _get_text(root, "type")

    # Convert type string to FieldType enum
    try:
        field_type = FieldType(field_type_str)
    except ValueError:
        # Default to TEXT if type not recognized
        field_type = FieldType.TEXT

    # Create base field
    field = CustomField(
        sobject=sobject,
        name=name,
        type=field_type,
        label=label,
        description=_get_text(root, "description") or None,
        help_text=_get_text(root, "inlineHelpText") or None,
        required=_get_bool(root, "required", False),
        unique=_get_bool(root, "unique", False),
        external_id=_get_bool(root, "externalId", False),
    )

    # Type-specific attributes
    if field_type in (FieldType.TEXT, FieldType.LONG_TEXT_AREA):
        field.length = _get_int(root, "length")

    elif field_type in (FieldType.NUMBER, FieldType.CURRENCY, FieldType.PERCENT):
        field.precision = _get_int(root, "precision")
        field.scale = _get_int(root, "scale")

    elif field_type in (FieldType.PICKLIST, FieldType.MULTI_PICKLIST):
        # Parse picklist values
        value_set = root.find(f"{METADATA_NS}valueSet")
        if value_set is not None:
            picklist_values = []
            for value_elem in value_set.findall(f"{METADATA_NS}value"):
                picklist_values.append(parse_picklist_value(value_elem))
            field.picklist_values = picklist_values

    elif field_type in (FieldType.LOOKUP, FieldType.MASTER_DETAIL):
        field.reference_to = _get_text(root, "referenceTo") or None
        field.relationship_name = _get_text(root, "relationshipName") or None
        field.delete_constraint = _get_text(root, "deleteConstraint") or None

    elif field_type == FieldType.FORMULA:
        field.formula = _get_text(root, "formula") or None
        field.formula_treat_blanks_as = _get_text(root, "formulaTreatBlanksAs") or None

    # Default value
    field.default_value = _get_text(root, "defaultValue") or None

    return field


def parse_validation_rule(xml_content: str, sobject: str) -> ValidationRule:
    """Parse ValidationRule from Salesforce Metadata API XML format."""
    root = ET.fromstring(xml_content)

    return ValidationRule(
        sobject=sobject,
        name=_get_text(root, "fullName"),
        active=_get_bool(root, "active", True),
        formula=_get_text(root, "errorConditionFormula"),
        error_message=_get_text(root, "errorMessage"),
        description=_get_text(root, "description") or None,
        error_display_field=_get_text(root, "errorDisplayField") or None,
    )


def parse_workflow_rule(xml_content: str, sobject: str) -> WorkflowRule:
    """Parse WorkflowRule from Salesforce Metadata API XML format."""
    root = ET.fromstring(xml_content)

    return WorkflowRule(
        sobject=sobject,
        name=_get_text(root, "fullName"),
        active=_get_bool(root, "active", True),
        formula=_get_text(root, "formula"),
        trigger_type=_get_text(root, "triggerType", "OnCreateOrTriggeringUpdate"),
        description=_get_text(root, "description") or None,
    )


def parse_custom_object(xml_content: str) -> CustomObject:
    """
    Parse CustomObject from Salesforce Metadata API XML format.

    Args:
        xml_content: XML string in Salesforce Metadata API format

    Returns:
        CustomObject instance

    Example:
        >>> xml = '''<?xml version="1.0"?>
        ... <CustomObject xmlns="http://soap.sforce.com/2006/04/metadata">
        ...     <fullName>Product_Review__c</fullName>
        ...     <label>Product Review</label>
        ...     <pluralLabel>Product Reviews</pluralLabel>
        ... </CustomObject>'''
        >>> obj = parse_custom_object(xml)
    """
    root = ET.fromstring(xml_content)

    # Basic attributes
    name = _get_text(root, "fullName")
    label = _get_text(root, "label")
    plural_label = _get_text(root, "pluralLabel")

    # Sharing model
    sharing_model_str = _get_text(root, "sharingModel", "ReadWrite")
    try:
        sharing_model = SharingModel(sharing_model_str)
    except ValueError:
        sharing_model = SharingModel.PUBLIC_READ_WRITE

    # Name field
    name_field = root.find(f"{METADATA_NS}nameField")
    name_field_label = _get_text(name_field, "label", "Name")
    name_field_type = _get_text(name_field, "type", "Text")

    # Create object
    obj = CustomObject(
        name=name,
        label=label,
        plural_label=plural_label,
        sharing_model=sharing_model,
        description=_get_text(root, "description") or None,
        name_field_label=name_field_label,
        name_field_type=name_field_type,
        enable_activities=_get_bool(root, "enableActivities", False),
        enable_reports=_get_bool(root, "enableReports", True),
        enable_search=_get_bool(root, "enableSearch", True),
        enable_bulk_api=_get_bool(root, "enableBulkApi", True),
        enable_sharing=_get_bool(root, "enableSharing", True),
        enable_feeds=_get_bool(root, "enableFeeds", False),
    )

    return obj


def parse_package_xml(xml_content: str) -> dict:
    """
    Parse a package.xml file.

    Args:
        xml_content: package.xml content as string

    Returns:
        Dictionary with component types and API version

    Example:
        >>> result = parse_package_xml(package_xml_content)
        >>> print(result["version"])
        60.0
        >>> print(result["types"])
        [{"name": "CustomObject", "members": ["*"]}, ...]
    """
    root = ET.fromstring(xml_content)

    result = {
        "version": _get_text(root, "version", "60.0"),
        "types": []
    }

    for type_elem in root.findall(f"{METADATA_NS}types"):
        type_info = {
            "name": _get_text(type_elem, "name"),
            "members": []
        }

        for member_elem in type_elem.findall(f"{METADATA_NS}members"):
            if member_elem.text:
                type_info["members"].append(member_elem.text)

        result["types"].append(type_info)

    return result
