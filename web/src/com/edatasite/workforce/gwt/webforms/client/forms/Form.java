package com.edatasite.workforce.gwt.webforms.client.forms;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Hayot
 * Date: Aug 9, 2010
 * Time: 9:08:04 PM
 */
public abstract class Form {
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public static final String TEXTBOX = "TextBox";
    public static final String TEXTAREA = "TextArea";
    public static final String DROPDOWN = "DropDown";
    public static final String CHECKBOX = "CheckBox";
    public static final String RADIOBUTTON = "RadioButton";
    public static final String DATEPICKER = "DatePicker";
    public static final String ATTACHMENT = "Attachment";
    public static final String MAILING_LIST = "Mailing List";
    public static final String FILE_UPLOAD_ITEM = "FileUploadItem";

    private List<WebField> webFields;
    protected boolean updateWebFields = false;
    protected boolean dropDownsFilled = false;
    private Map<Integer, WebField> customFields = new HashMap<>();

    public Map<Integer, WebField> getCustomFields() {
        return customFields;
    }

    public WebField[] getWebFields() {
        if (webFields == null) {
            webFields = new ArrayList<>();
        }
        return webFields.toArray(new WebField[]{});
    }

    public void setWebFields(WebField[] webFields) {
        this.webFields = new ArrayList<>();
        this.webFields.addAll(Arrays.asList(webFields));
    }

    public void addWebField(WebField webField) {
        if (webFields == null) {
            webFields = new ArrayList<>();
        }
        webFields.add(webField);
    }

    public void addCustomField(CompanyCustomFieldItem customField) {
        if ((!updateWebFields || !getCustomFields().containsKey(customField.getObjectId())) && !Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(customField.getUiType())) {
            WebField webField = new WebField(getSortOrder(true, 0), customField.getObjectId(), customField.getFieldName(), getUiTypeAsType(customField.getUiType()), false, false, "", customField.getColumnCode());
            webField.setCustomField(true);
            webField.setValues(customField.getPredefinedValuesWithSorting());
            if (Constants.DATA_TYPE_NUMBER.equals(customField.getDataType())
                    && Constants.UI_TYPE_TEXTBOX.equals(customField.getUiType())) {
                webField.setOnlyIntegerAllowed(true);
            }
            addWebField(webField);
            getCustomFields().put(webField.getSavingField(), webField);
        }
    }

    public int getUiTypeAsType(String uiType) {
        switch (uiType) {
            case TEXTBOX:
                return WebFormConstants.INPUT_TEXTBOX;
            case DROPDOWN:
                return WebFormConstants.INPUT_DROPDOWN;
            case CHECKBOX:
                return WebFormConstants.INPUT_CHECKBOX;
            case RADIOBUTTON:
                return WebFormConstants.INPUT_RADIO_BUTTON;
            case DATEPICKER:
                return WebFormConstants.INPUT_DATEPICKER;
            case ATTACHMENT:
            case FILE_UPLOAD_ITEM:
                return WebFormConstants.INPUT_ATTACHMENT;
            case MAILING_LIST:
                return WebFormConstants.INPUT_MAILING_LIST;
            case TEXTAREA:
                return WebFormConstants.INPUT_TEXTAREA;
        }
        return 0;
    }

    /**
     * every form must init the fields when starts...
     */
    public abstract void init();

    public abstract void fillFieldsWithDataFromServer();

    public boolean isDropDownsFilled() {
        return dropDownsFilled;
    }

    public void setDropDownsFilled(boolean dropDownsFilled) {
        this.dropDownsFilled = dropDownsFilled;
    }

    protected int getSortOrder(boolean lastPlace, int sortOrder) {
        if (lastPlace) {
            return getWebFields().length + 1;
        }
        return sortOrder;
    }

    public void setCustomFieldValues(Map<String, SelectItem[]> result) {
        if (getCustomFields() != null && getCustomFields().size() > 0 && result.containsKey(WebFormConstants.DROPDOWNITEMS_CUSTOMFIELDS)) {
            if (result.get(WebFormConstants.DROPDOWNITEMS_CUSTOMFIELDS) != null && result.get(WebFormConstants.DROPDOWNITEMS_CUSTOMFIELDS).length > 0) {
                for (SelectItem customField : result.get(WebFormConstants.DROPDOWNITEMS_CUSTOMFIELDS)) {
                    if (customField != null && getCustomFields() != null && getCustomFields().get(customField.getId()) != null) {
                        getCustomFields().get(customField.getId()).setValues(customField.getRelatedItems());
                    }
                }
            }
        }
    }
}