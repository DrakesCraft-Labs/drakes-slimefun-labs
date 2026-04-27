"""
Unit tests for Metadata API data models.

Tests CustomField, CustomObject, ValidationRule, WorkflowRule models.
"""

import pytest
from kinetic_core.metadata.models import (
    CustomField,
    CustomObject,
    ValidationRule,
    WorkflowRule,
    PicklistValue,
    FieldType,
    SharingModel,
)


class TestPicklistValue:
    """Tests for PicklistValue model."""

    def test_create_basic_picklist_value(self):
        """Test creating a basic picklist value."""
        pv = PicklistValue("Bronze")

        assert pv.full_name == "Bronze"
        assert pv.label == "Bronze"  # Auto-set to full_name
        assert pv.default is False
        assert pv.color is None

    def test_create_picklist_value_with_custom_label(self):
        """Test picklist value with custom label."""
        pv = PicklistValue(full_name="GOLD", label="Gold Tier", default=True, color="#FFD700")

        assert pv.full_name == "GOLD"
        assert pv.label == "Gold Tier"
        assert pv.default is True
        assert pv.color == "#FFD700"


class TestCustomField:
    """Tests for CustomField model."""

    def test_create_text_field(self):
        """Test creating a text custom field."""
        field = CustomField(
            sobject="Account",
            name="Customer_Tier__c",
            type=FieldType.TEXT,
            label="Customer Tier",
            length=50,
            required=True,
        )

        assert field.sobject == "Account"
        assert field.name == "Customer_Tier__c"
        assert field.type == FieldType.TEXT
        assert field.label == "Customer Tier"
        assert field.length == 50
        assert field.required is True

    def test_create_checkbox_field(self):
        """Test creating a checkbox field."""
        field = CustomField(
            sobject="Account",
            name="Phone_Verified__c",
            type=FieldType.CHECKBOX,
            label="Phone Verified",
            default_value="false",
        )

        assert field.type == FieldType.CHECKBOX
        assert field.default_value == "false"

    def test_create_number_field(self):
        """Test creating a number field with precision/scale."""
        field = CustomField(
            sobject="Opportunity",
            name="Commission_Rate__c",
            type=FieldType.PERCENT,
            label="Commission Rate",
            precision=5,
            scale=2,
        )

        assert field.type == FieldType.PERCENT
        assert field.precision == 5
        assert field.scale == 2

    def test_create_picklist_field(self):
        """Test creating a picklist field."""
        field = CustomField(
            sobject="Account",
            name="Rating__c",
            type=FieldType.PICKLIST,
            label="Rating",
            picklist_values=[
                PicklistValue("Bronze"),
                PicklistValue("Silver"),
                PicklistValue("Gold", default=True),
                PicklistValue("Platinum"),
            ],
        )

        assert field.type == FieldType.PICKLIST
        assert len(field.picklist_values) == 4
        assert field.picklist_values[2].default is True
        assert field.picklist_values[2].full_name == "Gold"

    def test_create_lookup_field(self):
        """Test creating a lookup field."""
        field = CustomField(
            sobject="Contact",
            name="Primary_Account__c",
            type=FieldType.LOOKUP,
            label="Primary Account",
            reference_to="Account",
            relationship_name="Primary_Contacts__r",
        )

        assert field.type == FieldType.LOOKUP
        assert field.reference_to == "Account"
        assert field.relationship_name == "Primary_Contacts__r"

    def test_create_master_detail_field(self):
        """Test creating a master-detail field."""
        field = CustomField(
            sobject="Order_Item__c",
            name="Order__c",
            type=FieldType.MASTER_DETAIL,
            label="Order",
            reference_to="Order__c",
            delete_constraint="Cascade",
        )

        assert field.type == FieldType.MASTER_DETAIL
        assert field.reference_to == "Order__c"
        assert field.delete_constraint == "Cascade"

    def test_auto_relationship_name_generation(self):
        """Test automatic relationship name generation."""
        field = CustomField(
            sobject="Contact",
            name="Account__c",
            type=FieldType.LOOKUP,
            label="Account",
            reference_to="Account",
        )

        # Should auto-generate relationship name
        assert field.relationship_name == "Account__r"

    def test_default_text_length(self):
        """Test default text field length."""
        field = CustomField(
            sobject="Account",
            name="Notes__c",
            type=FieldType.TEXT,
            label="Notes",
        )

        # Should default to 255
        assert field.length == 255

    def test_default_number_precision_scale(self):
        """Test default number field precision/scale."""
        field = CustomField(
            sobject="Opportunity",
            name="Discount__c",
            type=FieldType.NUMBER,
            label="Discount",
        )

        # Should default to 18 precision, 0 scale
        assert field.precision == 18
        assert field.scale == 0

    def test_invalid_field_name_raises_error(self):
        """Test that non-custom field names raise error."""
        with pytest.raises(ValueError, match="must end with __c"):
            CustomField(
                sobject="Account",
                name="InvalidName",  # Missing __c
                type=FieldType.TEXT,
                label="Invalid",
            )

    def test_picklist_without_values_raises_error(self):
        """Test that picklist without values raises error."""
        with pytest.raises(ValueError, match="requires picklist_values"):
            CustomField(
                sobject="Account",
                name="Status__c",
                type=FieldType.PICKLIST,
                label="Status",
                picklist_values=[],  # Empty
            )

    def test_lookup_without_reference_raises_error(self):
        """Test that lookup without reference_to raises error."""
        with pytest.raises(ValueError, match="requires reference_to"):
            CustomField(
                sobject="Contact",
                name="Account__c",
                type=FieldType.LOOKUP,
                label="Account",
                # Missing reference_to
            )

    def test_external_id_field(self):
        """Test creating an external ID field."""
        field = CustomField(
            sobject="Account",
            name="External_Key__c",
            type=FieldType.TEXT,
            label="External Key",
            length=100,
            external_id=True,
            unique=True,
        )

        assert field.external_id is True
        assert field.unique is True


class TestValidationRule:
    """Tests for ValidationRule model."""

    def test_create_validation_rule(self):
        """Test creating a validation rule."""
        rule = ValidationRule(
            sobject="Opportunity",
            name="Amount_Required_When_Closed",
            active=True,
            formula="AND(ISPICKVAL(StageName, 'Closed Won'), Amount = NULL)",
            error_message="Amount is required when stage is Closed Won",
            error_display_field="Amount",
        )

        assert rule.sobject == "Opportunity"
        assert rule.name == "Amount_Required_When_Closed"
        assert rule.active is True
        assert "ISPICKVAL" in rule.formula
        assert rule.error_message == "Amount is required when stage is Closed Won"
        assert rule.error_display_field == "Amount"

    def test_validation_rule_without_error_field(self):
        """Test validation rule without specific error field (top of page)."""
        rule = ValidationRule(
            sobject="Account",
            name="Phone_Format_Check",
            active=True,
            formula="AND(NOT(ISBLANK(Phone)), LEN(Phone) < 10)",
            error_message="Phone number must be at least 10 digits",
        )

        assert rule.error_display_field is None  # Will show at top


class TestWorkflowRule:
    """Tests for WorkflowRule model."""

    def test_create_workflow_rule(self):
        """Test creating a workflow rule."""
        rule = WorkflowRule(
            sobject="Lead",
            name="Hot_Lead_Alert",
            active=True,
            formula="AND(Rating = 'Hot', Owner.Email != NULL)",
            trigger_type="OnCreateOrTriggeringUpdate",
            description="Send alert when hot lead is created or updated",
        )

        assert rule.sobject == "Lead"
        assert rule.name == "Hot_Lead_Alert"
        assert rule.active is True
        assert rule.trigger_type == "OnCreateOrTriggeringUpdate"
        assert "Send alert" in rule.description


class TestCustomObject:
    """Tests for CustomObject model."""

    def test_create_basic_custom_object(self):
        """Test creating a basic custom object."""
        obj = CustomObject(
            name="Product_Review__c",
            label="Product Review",
            plural_label="Product Reviews",
            sharing_model=SharingModel.PUBLIC_READ_ONLY,
        )

        assert obj.name == "Product_Review__c"
        assert obj.label == "Product Review"
        assert obj.plural_label == "Product Reviews"
        assert obj.sharing_model == SharingModel.PUBLIC_READ_ONLY

    def test_create_object_with_fields(self):
        """Test creating object with fields."""
        obj = CustomObject(
            name="Product_Review__c",
            label="Product Review",
            plural_label="Product Reviews",
            fields=[
                CustomField(
                    sobject="Product_Review__c",
                    name="Rating__c",
                    type=FieldType.NUMBER,
                    label="Rating",
                    precision=2,
                    scale=1,
                ),
                CustomField(
                    sobject="Product_Review__c",
                    name="Review_Text__c",
                    type=FieldType.LONG_TEXT_AREA,
                    label="Review",
                    length=32000,
                ),
            ],
        )

        assert len(obj.fields) == 2
        assert obj.fields[0].name == "Rating__c"
        assert obj.fields[1].type == FieldType.LONG_TEXT_AREA

    def test_object_with_validation_rules(self):
        """Test creating object with validation rules."""
        obj = CustomObject(
            name="Product_Review__c",
            label="Product Review",
            plural_label="Product Reviews",
            validation_rules=[
                ValidationRule(
                    sobject="Product_Review__c",
                    name="Rating_Range",
                    active=True,
                    formula="OR(Rating__c < 1, Rating__c > 5)",
                    error_message="Rating must be between 1 and 5",
                )
            ],
        )

        assert len(obj.validation_rules) == 1
        assert obj.validation_rules[0].name == "Rating_Range"

    def test_object_feature_flags(self):
        """Test object feature flags."""
        obj = CustomObject(
            name="Customer_Support_Case__c",
            label="Support Case",
            plural_label="Support Cases",
            enable_activities=True,
            enable_feeds=True,
            enable_reports=True,
        )

        assert obj.enable_activities is True
        assert obj.enable_feeds is True
        assert obj.enable_reports is True

    def test_object_with_autonumber_name_field(self):
        """Test object with autonumber name field."""
        obj = CustomObject(
            name="Invoice__c",
            label="Invoice",
            plural_label="Invoices",
            name_field_label="Invoice Number",
            name_field_type="AutoNumber",
        )

        assert obj.name_field_label == "Invoice Number"
        assert obj.name_field_type == "AutoNumber"

    def test_invalid_object_name_raises_error(self):
        """Test that non-custom object names raise error."""
        with pytest.raises(ValueError, match="must end with __c"):
            CustomObject(
                name="InvalidObject",  # Missing __c
                label="Invalid",
                plural_label="Invalids",
            )

    def test_field_sobject_mismatch_raises_error(self):
        """Test that field sobject mismatch raises error."""
        with pytest.raises(ValueError, match="doesn't match object name"):
            CustomObject(
                name="Product_Review__c",
                label="Product Review",
                plural_label="Product Reviews",
                fields=[
                    CustomField(
                        sobject="Account",  # Wrong sobject!
                        name="Rating__c",
                        type=FieldType.NUMBER,
                        label="Rating",
                    )
                ],
            )

    def test_default_sharing_model(self):
        """Test default sharing model."""
        obj = CustomObject(
            name="Test__c",
            label="Test",
            plural_label="Tests",
        )

        # Should default to PUBLIC_READ_WRITE
        assert obj.sharing_model == SharingModel.PUBLIC_READ_WRITE


class TestFieldTypeEnum:
    """Tests for FieldType enum."""

    def test_all_field_types_exist(self):
        """Test that all expected field types exist."""
        expected_values = [
            "AutoNumber",
            "Checkbox",
            "Currency",
            "Date",
            "DateTime",
            "Email",
            "Lookup",
            "MasterDetail",
            "Number",
            "Percent",
            "Phone",
            "Picklist",
            "MultiselectPicklist",
            "Text",
            "TextArea",
            "LongTextArea",
            "Url",
            "Formula",
        ]

        for value in expected_values:
            assert any(ft.value == value for ft in FieldType), f"Missing FieldType for {value}"


class TestSharingModelEnum:
    """Tests for SharingModel enum."""

    def test_all_sharing_models_exist(self):
        """Test that all expected sharing models exist."""
        assert SharingModel.PRIVATE == "Private"
        assert SharingModel.PUBLIC_READ_ONLY == "ReadOnly"
        assert SharingModel.PUBLIC_READ_WRITE == "ReadWrite"
        assert SharingModel.CONTROLLED_BY_PARENT == "ControlledByParent"
