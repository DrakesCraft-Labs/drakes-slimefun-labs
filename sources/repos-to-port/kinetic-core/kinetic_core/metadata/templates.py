"""
Metadata templates for common use cases.

Provides pre-built templates for typical Salesforce configurations.
"""

from typing import List, Dict
from kinetic_core.metadata.models import (
    CustomField,
    CustomObject,
    ValidationRule,
    FieldType,
    SharingModel,
    PicklistValue,
)


def create_enterprise_crm_fields(sobject: str = "Account") -> List[CustomField]:
    """
    Create enterprise CRM fields for Account management.

    Args:
        sobject: Object to create fields for (default: Account)

    Returns:
        List of CustomField definitions

    Example:
        >>> fields = create_enterprise_crm_fields("Account")
        >>> for field in fields:
        ...     client.metadata.deploy_field(field)
    """
    return [
        # Customer tier
        CustomField(
            sobject=sobject,
            name="Customer_Tier__c",
            type=FieldType.PICKLIST,
            label="Customer Tier",
            required=False,
            picklist_values=[
                PicklistValue("Bronze"),
                PicklistValue("Silver"),
                PicklistValue("Gold", default=True),
                PicklistValue("Platinum"),
            ],
            description="Customer tier for segmentation",
        ),
        # Annual revenue
        CustomField(
            sobject=sobject,
            name="Annual_Revenue_Verified__c",
            type=FieldType.CURRENCY,
            label="Annual Revenue (Verified)",
            precision=18,
            scale=2,
            description="Verified annual revenue",
        ),
        # Last contact date
        CustomField(
            sobject=sobject,
            name="Last_Contact_Date__c",
            type=FieldType.DATE,
            label="Last Contact Date",
            description="Date of last customer contact",
        ),
        # Primary contact
        CustomField(
            sobject=sobject,
            name="Primary_Contact__c",
            type=FieldType.LOOKUP,
            label="Primary Contact",
            reference_to="Contact",
            relationship_name="Primary_Accounts__r",
            description="Primary contact person",
        ),
        # Account health score
        CustomField(
            sobject=sobject,
            name="Health_Score__c",
            type=FieldType.NUMBER,
            label="Account Health Score",
            precision=3,
            scale=0,
            description="Account health score (0-100)",
        ),
        # Notes
        CustomField(
            sobject=sobject,
            name="Internal_Notes__c",
            type=FieldType.LONG_TEXT_AREA,
            label="Internal Notes",
            length=32000,
            description="Internal notes about the account",
        ),
    ]


def create_support_case_object() -> CustomObject:
    """
    Create a custom Support Case tracking object.

    Returns:
        CustomObject definition with fields

    Example:
        >>> support_obj = create_support_case_object()
        >>> result = client.metadata.deploy_object(support_obj)
    """
    return CustomObject(
        name="Support_Case__c",
        label="Support Case",
        plural_label="Support Cases",
        sharing_model=SharingModel.PUBLIC_READ_WRITE,
        enable_activities=True,
        enable_reports=True,
        enable_feeds=True,
        name_field_label="Case Number",
        name_field_type="AutoNumber",
        description="Custom support case tracking",
        fields=[
            CustomField(
                sobject="Support_Case__c",
                name="Priority__c",
                type=FieldType.PICKLIST,
                label="Priority",
                required=True,
                picklist_values=[
                    PicklistValue("Low"),
                    PicklistValue("Medium", default=True),
                    PicklistValue("High"),
                    PicklistValue("Critical"),
                ],
            ),
            CustomField(
                sobject="Support_Case__c",
                name="Status__c",
                type=FieldType.PICKLIST,
                label="Status",
                required=True,
                picklist_values=[
                    PicklistValue("New", default=True),
                    PicklistValue("In Progress"),
                    PicklistValue("Waiting"),
                    PicklistValue("Resolved"),
                    PicklistValue("Closed"),
                ],
            ),
            CustomField(
                sobject="Support_Case__c",
                name="Description__c",
                type=FieldType.LONG_TEXT_AREA,
                label="Description",
                length=32000,
                required=True,
            ),
            CustomField(
                sobject="Support_Case__c",
                name="Resolution__c",
                type=FieldType.LONG_TEXT_AREA,
                label="Resolution",
                length=32000,
            ),
            CustomField(
                sobject="Support_Case__c",
                name="Customer__c",
                type=FieldType.LOOKUP,
                label="Customer",
                reference_to="Account",
                relationship_name="Support_Cases__r",
                required=True,
            ),
        ],
        validation_rules=[
            ValidationRule(
                sobject="Support_Case__c",
                name="Resolution_Required_When_Closed",
                active=True,
                formula="AND(ISPICKVAL(Status__c, 'Closed'), ISBLANK(Resolution__c))",
                error_message="Resolution is required when case is closed",
                error_display_field="Resolution__c",
            )
        ],
    )


def create_product_catalog_fields() -> List[CustomField]:
    """
    Create product catalog management fields for Product2.

    Returns:
        List of CustomField definitions for Product2

    Example:
        >>> fields = create_product_catalog_fields()
        >>> for field in fields:
        ...     client.metadata.deploy_field(field)
    """
    return [
        CustomField(
            sobject="Product2",
            name="SKU__c",
            type=FieldType.TEXT,
            label="SKU",
            length=50,
            unique=True,
            external_id=True,
            required=True,
            description="Stock Keeping Unit",
        ),
        CustomField(
            sobject="Product2",
            name="Cost__c",
            type=FieldType.CURRENCY,
            label="Cost",
            precision=18,
            scale=2,
            description="Product cost",
        ),
        CustomField(
            sobject="Product2",
            name="Weight__c",
            type=FieldType.NUMBER,
            label="Weight (kg)",
            precision=10,
            scale=2,
            description="Product weight in kilograms",
        ),
        CustomField(
            sobject="Product2",
            name="Category__c",
            type=FieldType.PICKLIST,
            label="Category",
            picklist_values=[
                PicklistValue("Electronics"),
                PicklistValue("Clothing"),
                PicklistValue("Home & Garden"),
                PicklistValue("Sports"),
                PicklistValue("Books"),
                PicklistValue("Other", default=True),
            ],
        ),
        CustomField(
            sobject="Product2",
            name="In_Stock__c",
            type=FieldType.CHECKBOX,
            label="In Stock",
            default_value="false",
        ),
    ]


def create_audit_trail_fields(sobject: str) -> List[CustomField]:
    """
    Create audit trail fields for any object.

    Args:
        sobject: Object to create fields for

    Returns:
        List of CustomField definitions

    Example:
        >>> fields = create_audit_trail_fields("Opportunity")
        >>> for field in fields:
        ...     client.metadata.deploy_field(field)
    """
    return [
        CustomField(
            sobject=sobject,
            name="External_Created_Date__c",
            type=FieldType.DATETIME,
            label="External Created Date",
            description="Created date from external system",
        ),
        CustomField(
            sobject=sobject,
            name="External_Modified_Date__c",
            type=FieldType.DATETIME,
            label="External Modified Date",
            description="Last modified date from external system",
        ),
        CustomField(
            sobject=sobject,
            name="External_ID__c",
            type=FieldType.TEXT,
            label="External ID",
            length=255,
            unique=True,
            external_id=True,
            description="Unique ID from external system",
        ),
        CustomField(
            sobject=sobject,
            name="Sync_Status__c",
            type=FieldType.PICKLIST,
            label="Sync Status",
            picklist_values=[
                PicklistValue("Synced", default=True),
                PicklistValue("Pending"),
                PicklistValue("Error"),
            ],
            description="Data synchronization status",
        ),
        CustomField(
            sobject=sobject,
            name="Last_Sync_Date__c",
            type=FieldType.DATETIME,
            label="Last Sync Date",
            description="Last successful sync date",
        ),
    ]


# Template registry
TEMPLATES = {
    "enterprise_crm": {
        "name": "Enterprise CRM Fields",
        "description": "Standard fields for enterprise account management",
        "function": create_enterprise_crm_fields,
    },
    "support_case": {
        "name": "Support Case Object",
        "description": "Custom support case tracking object with fields",
        "function": create_support_case_object,
    },
    "product_catalog": {
        "name": "Product Catalog Fields",
        "description": "Product catalog management fields",
        "function": create_product_catalog_fields,
    },
    "audit_trail": {
        "name": "Audit Trail Fields",
        "description": "Audit trail and sync tracking fields",
        "function": create_audit_trail_fields,
    },
}


def list_templates() -> List[Dict[str, str]]:
    """
    List all available templates.

    Returns:
        List of template metadata

    Example:
        >>> templates = list_templates()
        >>> for template in templates:
        ...     print(f"{template['id']}: {template['name']}")
    """
    return [
        {
            "id": template_id,
            "name": info["name"],
            "description": info["description"],
        }
        for template_id, info in TEMPLATES.items()
    ]


def get_template(template_id: str, **kwargs):
    """
    Get template by ID.

    Args:
        template_id: Template identifier
        **kwargs: Arguments to pass to template function

    Returns:
        Template result (CustomObject or List[CustomField])

    Raises:
        KeyError: If template not found

    Example:
        >>> fields = get_template("enterprise_crm", sobject="Account")
        >>> obj = get_template("support_case")
    """
    if template_id not in TEMPLATES:
        raise KeyError(f"Template '{template_id}' not found")

    return TEMPLATES[template_id]["function"](**kwargs)
