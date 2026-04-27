"""
Data models for Salesforce Metadata API components.

These models represent Salesforce metadata components and provide
serialization to/from XML format used by the Metadata API.
"""

from dataclasses import dataclass, field
from typing import Optional, List, Literal
from enum import Enum


class FieldType(str, Enum):
    """Salesforce custom field types."""

    AUTO_NUMBER = "AutoNumber"
    CHECKBOX = "Checkbox"
    CURRENCY = "Currency"
    DATE = "Date"
    DATETIME = "DateTime"
    EMAIL = "Email"
    LOOKUP = "Lookup"
    MASTER_DETAIL = "MasterDetail"
    NUMBER = "Number"
    PERCENT = "Percent"
    PHONE = "Phone"
    PICKLIST = "Picklist"
    MULTI_PICKLIST = "MultiselectPicklist"
    TEXT = "Text"
    TEXT_AREA = "TextArea"
    LONG_TEXT_AREA = "LongTextArea"
    URL = "Url"
    FORMULA = "Formula"


class SharingModel(str, Enum):
    """Object sharing models."""

    PRIVATE = "Private"
    PUBLIC_READ_ONLY = "ReadOnly"
    PUBLIC_READ_WRITE = "ReadWrite"
    CONTROLLED_BY_PARENT = "ControlledByParent"


@dataclass
class PicklistValue:
    """Represents a picklist value."""

    full_name: str
    """The value shown to users."""

    default: bool = False
    """Whether this is the default value."""

    label: Optional[str] = None
    """Display label (defaults to full_name if not specified)."""

    color: Optional[str] = None
    """Color for this picklist value (hex format)."""

    def __post_init__(self):
        """Set label to full_name if not provided."""
        if self.label is None:
            self.label = self.full_name


@dataclass
class CustomField:
    """
    Represents a Salesforce Custom Field.

    Example:
        >>> field = CustomField(
        ...     sobject="Account",
        ...     name="Customer_Tier__c",
        ...     type=FieldType.PICKLIST,
        ...     label="Customer Tier",
        ...     required=True,
        ...     picklist_values=[
        ...         PicklistValue("Bronze"),
        ...         PicklistValue("Silver"),
        ...         PicklistValue("Gold", default=True),
        ...         PicklistValue("Platinum")
        ...     ]
        ... )
    """

    sobject: str
    """The SObject this field belongs to (e.g., 'Account', 'Contact')."""

    name: str
    """Field API name (must end with __c for custom fields)."""

    type: FieldType
    """Field type (Text, Checkbox, Picklist, etc.)."""

    label: str
    """Field label shown in UI."""

    # Optional attributes
    description: Optional[str] = None
    """Field description."""

    help_text: Optional[str] = None
    """Inline help text."""

    required: bool = False
    """Whether field is required."""

    unique: bool = False
    """Whether field values must be unique."""

    external_id: bool = False
    """Whether field is an external ID."""

    # Text field attributes
    length: Optional[int] = None
    """Length for text fields (max 255 for Text, 131072 for LongTextArea)."""

    # Number/Currency/Percent attributes
    precision: Optional[int] = None
    """Total digits for number fields."""

    scale: Optional[int] = None
    """Decimal places for number fields."""

    # Picklist attributes
    picklist_values: List[PicklistValue] = field(default_factory=list)
    """Values for picklist fields."""

    # Lookup/Master-Detail attributes
    reference_to: Optional[str] = None
    """Target object for lookup/master-detail fields."""

    relationship_name: Optional[str] = None
    """Relationship name for lookup/master-detail fields."""

    delete_constraint: Optional[Literal["Cascade", "Restrict", "SetNull"]] = None
    """Delete behavior for master-detail fields."""

    # Formula attributes
    formula: Optional[str] = None
    """Formula expression for formula fields."""

    formula_treat_blanks_as: Optional[Literal["BlankAsBlank", "BlankAsZero"]] = None
    """How to treat blanks in formula fields."""

    # Default value
    default_value: Optional[str] = None
    """Default value for the field."""

    def __post_init__(self):
        """Validate field configuration."""
        # Ensure custom field names end with __c
        if not self.name.endswith("__c") and not self.name.endswith("__r"):
            if not self.name.startswith("Id") and self.name != "Name":
                raise ValueError(
                    f"Custom field name must end with __c: {self.name}"
                )

        # Validate type-specific requirements
        if self.type == FieldType.TEXT and self.length is None:
            self.length = 255  # Default text length

        if self.type in (FieldType.LOOKUP, FieldType.MASTER_DETAIL):
            if not self.reference_to:
                raise ValueError(
                    f"Lookup/MasterDetail field requires reference_to: {self.name}"
                )
            if not self.relationship_name:
                # Auto-generate relationship name
                self.relationship_name = self.name.replace("__c", "__r")

        if self.type in (FieldType.NUMBER, FieldType.CURRENCY, FieldType.PERCENT):
            if self.precision is None:
                self.precision = 18  # Default precision
            if self.scale is None:
                self.scale = 0  # Default scale

        if self.type in (FieldType.PICKLIST, FieldType.MULTI_PICKLIST):
            if not self.picklist_values:
                raise ValueError(
                    f"Picklist field requires picklist_values: {self.name}"
                )


@dataclass
class ValidationRule:
    """
    Represents a Salesforce Validation Rule.

    Example:
        >>> rule = ValidationRule(
        ...     sobject="Opportunity",
        ...     name="Amount_Required_When_Closed_Won",
        ...     active=True,
        ...     formula="AND(ISPICKVAL(StageName, 'Closed Won'), Amount = NULL)",
        ...     error_message="Amount is required when stage is Closed Won",
        ...     error_display_field="Amount"
        ... )
    """

    sobject: str
    """The SObject this rule applies to."""

    name: str
    """Validation rule API name."""

    active: bool
    """Whether the rule is active."""

    formula: str
    """Formula expression that must evaluate to FALSE for record to be valid."""

    error_message: str
    """Error message shown when validation fails."""

    description: Optional[str] = None
    """Rule description."""

    error_display_field: Optional[str] = None
    """Field where error should be displayed (default: top of page)."""


@dataclass
class WorkflowRule:
    """
    Represents a Salesforce Workflow Rule.

    Example:
        >>> rule = WorkflowRule(
        ...     sobject="Lead",
        ...     name="Hot_Lead_Alert",
        ...     active=True,
        ...     formula="AND(Rating = 'Hot', Owner.Email != NULL)",
        ...     trigger_type="OnCreateOrTriggeringUpdate"
        ... )
    """

    sobject: str
    """The SObject this rule applies to."""

    name: str
    """Workflow rule API name."""

    active: bool
    """Whether the rule is active."""

    formula: str
    """Formula expression for rule criteria."""

    trigger_type: Literal[
        "OnCreateOnly",
        "OnCreateOrTriggeringUpdate",
        "OnAllChanges"
    ]
    """When the rule should trigger."""

    description: Optional[str] = None
    """Rule description."""


@dataclass
class CustomObject:
    """
    Represents a Salesforce Custom Object.

    Example:
        >>> obj = CustomObject(
        ...     name="Product_Review__c",
        ...     label="Product Review",
        ...     plural_label="Product Reviews",
        ...     sharing_model=SharingModel.PUBLIC_READ_ONLY,
        ...     enable_reports=True,
        ...     enable_activities=True,
        ...     fields=[
        ...         CustomField(
        ...             sobject="Product_Review__c",
        ...             name="Rating__c",
        ...             type=FieldType.NUMBER,
        ...             label="Rating",
        ...             precision=2,
        ...             scale=1,
        ...             required=True
        ...         ),
        ...         CustomField(
        ...             sobject="Product_Review__c",
        ...             name="Review_Text__c",
        ...             type=FieldType.LONG_TEXT_AREA,
        ...             label="Review",
        ...             length=32000
        ...         )
        ...     ]
        ... )
    """

    name: str
    """Object API name (must end with __c)."""

    label: str
    """Singular label shown in UI."""

    plural_label: str
    """Plural label shown in UI."""

    sharing_model: SharingModel = SharingModel.PUBLIC_READ_WRITE
    """Sharing model for the object."""

    # Optional attributes
    description: Optional[str] = None
    """Object description."""

    name_field_label: str = "Name"
    """Label for the Name field."""

    name_field_type: Literal["Text", "AutoNumber"] = "Text"
    """Type of the Name field."""

    enable_activities: bool = False
    """Allow tasks and events."""

    enable_reports: bool = True
    """Allow reporting on this object."""

    enable_search: bool = True
    """Allow searching this object."""

    enable_bulk_api: bool = True
    """Allow Bulk API access."""

    enable_sharing: bool = True
    """Allow sharing settings."""

    enable_feeds: bool = False
    """Enable Chatter feeds."""

    # Related metadata
    fields: List[CustomField] = field(default_factory=list)
    """Custom fields for this object."""

    validation_rules: List[ValidationRule] = field(default_factory=list)
    """Validation rules for this object."""

    workflow_rules: List[WorkflowRule] = field(default_factory=list)
    """Workflow rules for this object."""

    def __post_init__(self):
        """Validate object configuration."""
        if not self.name.endswith("__c"):
            raise ValueError(f"Custom object name must end with __c: {self.name}")

        # Ensure all fields reference this object
        for fld in self.fields:
            if fld.sobject != self.name:
                raise ValueError(
                    f"Field {fld.name} sobject ({fld.sobject}) "
                    f"doesn't match object name ({self.name})"
                )


@dataclass
class DeployResult:
    """Result of a metadata deploy operation."""

    success: bool
    """Whether the deployment succeeded."""

    id: str
    """Deployment ID."""

    status: str
    """Deployment status (Succeeded, Failed, InProgress, etc.)."""

    component_successes: List[str] = field(default_factory=list)
    """List of successfully deployed components."""

    component_failures: List[dict] = field(default_factory=list)
    """List of failed components with error details."""

    run_test_result: Optional[dict] = None
    """Test execution results if run_tests was True."""

    messages: List[str] = field(default_factory=list)
    """Deployment messages."""


@dataclass
class RetrieveResult:
    """Result of a metadata retrieve operation."""

    success: bool
    """Whether the retrieval succeeded."""

    id: str
    """Retrieve request ID."""

    status: str
    """Retrieve status (Succeeded, Failed, InProgress, etc.)."""

    file_properties: List[dict] = field(default_factory=list)
    """List of retrieved file properties."""

    messages: List[str] = field(default_factory=list)
    """Retrieve messages."""

    zip_file: Optional[bytes] = None
    """Retrieved metadata as ZIP file (if successful)."""
