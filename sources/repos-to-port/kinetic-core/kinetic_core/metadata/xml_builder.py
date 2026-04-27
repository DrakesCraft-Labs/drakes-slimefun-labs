"""
XML serialization for Salesforce Metadata API components.

Converts Python data models to Salesforce Metadata API XML format.
"""

import xml.etree.ElementTree as ET
from xml.dom import minidom
from typing import Optional

from kinetic_core.metadata.models import (
    CustomField,
    CustomObject,
    ValidationRule,
    WorkflowRule,
    PicklistValue,
    FieldType,
)


# Salesforce Metadata API namespace
METADATA_NS = "http://soap.sforce.com/2006/04/metadata"
ET.register_namespace("", METADATA_NS)


def _create_element(name: str, text: Optional[str] = None) -> ET.Element:
    """Create an XML element with optional text content."""
    elem = ET.Element(name)
    if text is not None:
        elem.text = str(text).lower() if isinstance(text, bool) else str(text)
    return elem


def _add_subelement(parent: ET.Element, name: str, text: Optional[str] = None):
    """Add a subelement to a parent element."""
    if text is not None or name in ("required", "externalId", "unique"):
        # Include boolean fields even if False
        elem = ET.SubElement(parent, name)
        if text is not None:
            elem.text = str(text).lower() if isinstance(text, bool) else str(text)


def picklist_value_to_xml(picklist: PicklistValue) -> ET.Element:
    """Convert a PicklistValue to XML element."""
    elem = ET.Element("value")

    _add_subelement(elem, "fullName", picklist.full_name)
    _add_subelement(elem, "default", picklist.default)

    if picklist.label and picklist.label != picklist.full_name:
        _add_subelement(elem, "label", picklist.label)

    if picklist.color:
        _add_subelement(elem, "color", picklist.color)

    return elem


def _custom_field_to_element(field: CustomField) -> ET.Element:
    """Internal helper to convert CustomField to ET.Element."""
    root = ET.Element("CustomField")
    root.set("xmlns", METADATA_NS)

    # Full name (API name without __c suffix for the field itself)
    _add_subelement(root, "fullName", field.name)

    # Label and description
    _add_subelement(root, "label", field.label)
    if field.description:
        _add_subelement(root, "description", field.description)
    if field.help_text:
        _add_subelement(root, "inlineHelpText", field.help_text)

    # Type
    _add_subelement(root, "type", field.type.value)

    # Required/Unique/ExternalId flags
    _add_subelement(root, "required", field.required)
    _add_subelement(root, "unique", field.unique)
    _add_subelement(root, "externalId", field.external_id)

    # Type-specific attributes
    if field.type == FieldType.TEXT:
        _add_subelement(root, "length", field.length or 255)

    elif field.type == FieldType.LONG_TEXT_AREA:
        _add_subelement(root, "length", field.length or 32000)
        _add_subelement(root, "visibleLines", 3)

    elif field.type in (FieldType.NUMBER, FieldType.CURRENCY, FieldType.PERCENT):
        _add_subelement(root, "precision", field.precision or 18)
        _add_subelement(root, "scale", field.scale or 0)

    elif field.type in (FieldType.PICKLIST, FieldType.MULTI_PICKLIST):
        # Picklist
        picklist_elem = ET.SubElement(root, "valueSet")
        _add_subelement(picklist_elem, "restricted", True)

        # valueSetDefinition wrapper is required for local picklists
        def_elem = ET.SubElement(picklist_elem, "valueSetDefinition")
        _add_subelement(def_elem, "sorted", False)

        for pv in field.picklist_values:
            def_elem.append(picklist_value_to_xml(pv))

    elif field.type in (FieldType.LOOKUP, FieldType.MASTER_DETAIL):
        _add_subelement(root, "referenceTo", field.reference_to)
        _add_subelement(root, "relationshipLabel", field.label)
        _add_subelement(root, "relationshipName", field.relationship_name)

        if field.type == FieldType.MASTER_DETAIL and field.delete_constraint:
            _add_subelement(root, "deleteConstraint", field.delete_constraint)

    elif field.type == FieldType.FORMULA:
        if field.formula:
            _add_subelement(root, "formula", field.formula)
        if field.formula_treat_blanks_as:
            _add_subelement(
                root, "formulaTreatBlanksAs", field.formula_treat_blanks_as
            )

    # Default value
    if field.default_value:
        _add_subelement(root, "defaultValue", field.default_value)

    return root


def custom_field_to_xml(field: CustomField) -> str:
    """
    Convert a CustomField to Salesforce Metadata API XML format.

    Args:
        field: CustomField instance to serialize

    Returns:
        XML string in Salesforce Metadata API format

    Example:
        >>> field = CustomField(
        ...     sobject="Account",
        ...     name="Customer_Tier__c",
        ...     type=FieldType.TEXT,
        ...     label="Customer Tier",
        ...     length=50
        ... )
        >>> xml = custom_field_to_xml(field)
    """
    root = _custom_field_to_element(field)
    return _prettify_xml(root)


def validation_rule_to_xml(rule: ValidationRule) -> str:
    """Convert a ValidationRule to Salesforce Metadata API XML format."""
    root = ET.Element("ValidationRule")
    root.set("xmlns", METADATA_NS)

    _add_subelement(root, "fullName", rule.name)
    _add_subelement(root, "active", rule.active)

    if rule.description:
        _add_subelement(root, "description", rule.description)

    _add_subelement(root, "errorConditionFormula", rule.formula)
    _add_subelement(root, "errorMessage", rule.error_message)

    if rule.error_display_field:
        _add_subelement(root, "errorDisplayField", rule.error_display_field)

    return _prettify_xml(root)


def workflow_rule_to_xml(rule: WorkflowRule) -> str:
    """Convert a WorkflowRule to Salesforce Metadata API XML format."""
    root = ET.Element("WorkflowRule")
    root.set("xmlns", METADATA_NS)

    _add_subelement(root, "fullName", rule.name)
    _add_subelement(root, "active", rule.active)

    if rule.description:
        _add_subelement(root, "description", rule.description)

    _add_subelement(root, "formula", rule.formula)
    _add_subelement(root, "triggerType", rule.trigger_type)

    return _prettify_xml(root)


def custom_object_to_xml(obj: CustomObject) -> str:
    """
    Convert a CustomObject to Salesforce Metadata API XML format.

    Args:
        obj: CustomObject instance to serialize

    Returns:
        XML string in Salesforce Metadata API format

    Example:
        >>> obj = CustomObject(
        ...     name="Product_Review__c",
        ...     label="Product Review",
        ...     plural_label="Product Reviews"
        ... )
        >>> xml = custom_object_to_xml(obj)
    """
    root = ET.Element("CustomObject")
    root.set("xmlns", METADATA_NS)

    # Basic properties
    _add_subelement(root, "fullName", obj.name)
    _add_subelement(root, "label", obj.label)
    _add_subelement(root, "pluralLabel", obj.plural_label)

    if obj.description:
        _add_subelement(root, "description", obj.description)

    # Name field
    name_field = ET.SubElement(root, "nameField")
    _add_subelement(name_field, "label", obj.name_field_label)
    _add_subelement(name_field, "type", obj.name_field_type)

    # Sharing model
    _add_subelement(root, "sharingModel", obj.sharing_model.value)

    # Feature flags
    _add_subelement(root, "enableActivities", obj.enable_activities)
    _add_subelement(root, "enableBulkApi", obj.enable_bulk_api)
    _add_subelement(root, "enableReports", obj.enable_reports)
    _add_subelement(root, "enableSearch", obj.enable_search)
    _add_subelement(root, "enableSharing", obj.enable_sharing)
    _add_subelement(root, "enableFeeds", obj.enable_feeds)

    # Deployment status
    _add_subelement(root, "deploymentStatus", "Deployed")

    # Fields
    # Fields
    for field in obj.fields:
        # Get field element directly
        field_elem = _custom_field_to_element(field)
        
        # Change tag from CustomField to fields (as expected by CustomObject metadata)
        field_elem.tag = "fields"
        
        # Remove xmlns attribute if present to avoid duplication
        if 'xmlns' in field_elem.attrib:
            del field_elem.attrib['xmlns']
            
        root.append(field_elem)

    return _prettify_xml(root)


def create_package_xml(
    component_types: list[str],
    api_version: str = "60.0"
) -> str:
    """
    Create a package.xml file for metadata retrieval/deployment.

    Args:
        component_types: List of metadata component types to include
        api_version: Salesforce API version

    Returns:
        package.xml content as string

    Example:
        >>> xml = create_package_xml(["CustomObject", "CustomField"])
    """
    root = ET.Element("Package")
    root.set("xmlns", METADATA_NS)

    # Add types
    for comp_type in component_types:
        type_elem = ET.SubElement(root, "types")
        _add_subelement(type_elem, "members", "*")
        _add_subelement(type_elem, "name", comp_type)

    # API version
    _add_subelement(root, "version", api_version)

    return _prettify_xml(root)


def _prettify_xml(elem: ET.Element) -> str:
    """
    Return a pretty-printed XML string for the Element.

    Args:
        elem: XML Element to prettify

    Returns:
        Formatted XML string with proper indentation
    """
    # Convert to string
    rough_string = ET.tostring(elem, encoding="unicode")
    
    # Remove duplicate xmlns attributes if present (ElementTree + manual set complication)
    import re
    rough_string = re.sub(r'(\sxmlns="[^"]+")(\sxmlns="[^"]+")', r'\1', rough_string)
    
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="    ", encoding=None)
