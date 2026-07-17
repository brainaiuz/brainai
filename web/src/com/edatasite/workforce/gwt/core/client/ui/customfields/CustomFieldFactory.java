package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;

import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:46:51
 */
public class CustomFieldFactory implements Constants {
    // Custom Fields create for WfmForm
    public static AbstractCustomField getCustomWidget(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmform, List<String> fieldCodeName, String customWidgetStyle) {
        if (UI_TYPE_TEXTBOX.equals(customFieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(customFieldItem.getUiType()) || UI_TYPE_URL.equals(customFieldItem.getUiType())) {
            return new TextBoxCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
            return new PercentageCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_DATEPICKER.equals(customFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            return new DatePickerCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_DROPDOWN.equals(customFieldItem.getUiType()) || UI_TYPE_ENTITY_DROPDOWN.equals(customFieldItem.getUiType())) {
            return new DropDownCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_RADIOBUTTON.equals(customFieldItem.getUiType())) {
            return new RadioButtonCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_CHECKBOX.equals(customFieldItem.getUiType())) {
            return new CheckBoxCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new TextAreaCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_HTML_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new HTMLTextAreaCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityMultiLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
            return new GeneralFileUploadItemField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customFieldItem.getUiType())) {
            return new ProfileImageItemField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
            return new LookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new MultiLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else {
            return new RadioButtonCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        }
    }

    // Custom Fields Create For
    public static AbstractCustomField getCustomWidget(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        if (UI_TYPE_TEXTBOX.equals(customFieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(customFieldItem.getUiType()) || UI_TYPE_URL.equals(customFieldItem.getUiType())) {
            return new TextBoxCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
            return new PercentageCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_DATEPICKER.equals(customFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            return new DatePickerCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_DROPDOWN.equals(customFieldItem.getUiType()) || UI_TYPE_ENTITY_DROPDOWN.equals(customFieldItem.getUiType())) {
            return new DropDownCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_RADIOBUTTON.equals(customFieldItem.getUiType())) {
            return new RadioButtonCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_CHECKBOX.equals(customFieldItem.getUiType())) {
            return new CheckBoxCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new TextAreaCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_HTML_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new HTMLTextAreaCustomField(customFieldItem, fieldCodeName);
        } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityLookUpCustomField(customFieldItem, fieldCodeName);
        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityMultiLookUpCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
            return new GeneralFileUploadItemField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customFieldItem.getUiType())) {
            return new ProfileImageItemField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
            return new LookUpCustomField(customFieldItem, fieldCodeName);
        } else if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new MultiLookUpCustomField(customFieldItem, fieldCodeName);
        } else {
            return new RadioButtonCustomField(customFieldItem, fieldCodeName);
        }
    }


    public static AbstractCustomField getCustomWidgetWithLocale(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmform, List<String> fieldCodeName, String customWidgetStyle, String localeCode) {
        if (UI_TYPE_TEXTBOX.equals(customFieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(customFieldItem.getUiType()) || UI_TYPE_URL.equals(customFieldItem.getUiType())) {
            return new TextBoxCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_PERCENTAGE.equals(customFieldItem.getUiType())) {
            return new PercentageCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_DATEPICKER.equals(customFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(customFieldItem.getUiType())) {
            return new DatePickerCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_DROPDOWN.equals(customFieldItem.getUiType()) || UI_TYPE_ENTITY_DROPDOWN.equals(customFieldItem.getUiType())) {
            return new DropDownCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle, localeCode);
        } else if (UI_TYPE_RADIOBUTTON.equals(customFieldItem.getUiType())) {
            return new RadioButtonCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle, localeCode);
        } else if (UI_TYPE_CHECKBOX.equals(customFieldItem.getUiType())) {
            return new CheckBoxCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new TextAreaCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_HTML_TEXTAREA.equals(customFieldItem.getUiType())) {
            return new HTMLTextAreaCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (TYPE_ENTITY_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (TYPE_ENTITY_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new EntityMultiLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
            return new GeneralFileUploadItemField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customFieldItem.getUiType())) {
            return new ProfileImageItemField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
            return new LookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else if (UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
            return new MultiLookUpCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        } else {
            return new RadioButtonCustomField(customFieldItem, wfmform, fieldCodeName, customWidgetStyle);
        }
    }
}
