package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 5:16:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebField implements IsSerializable, WebFormConstants {
    private Integer objectID;
    private String originalLabel;
    private String label;
    private Integer type; // type of the field(DropDown... String)...
    private boolean mandatory; // if true, make field as required...
    private boolean showInForm;
    private String defaultValue;
    private Integer savingField;
    private SelectItem[] values;
    private boolean isCustomField = false;
    private boolean unchangable = false;
    private boolean drawLine = false;
    private String groupTitle;
    private Integer sortOrder;
    private boolean onlyIntegerAllowed;
    private String customFieldCode;

    public WebField() {

    }

    public String getCustomFieldCode() {
        return customFieldCode;
    }

    public void setCustomFieldCode(String customFieldCode) {
        this.customFieldCode = customFieldCode;
    }

    public WebField(Integer sortOrder, Integer savingField, String originalLabel, Integer type, boolean mandatory, boolean showInForm, String defaultValue, String customFieldCode, boolean... unchangable) {
        this.savingField = savingField;
        this.sortOrder = sortOrder;
        this.originalLabel = originalLabel;
        this.label = originalLabel;
        this.type = type;
        this.mandatory = mandatory;
        this.showInForm = showInForm;
        this.defaultValue = defaultValue;
        this.unchangable = (unchangable != null && unchangable.length > 0) && unchangable[0];
        this.customFieldCode = customFieldCode;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOriginalLabel() {
        return originalLabel;
    }

    public void setOriginalLabel(String originalLabel) {
        this.originalLabel = originalLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isShowInForm() {
        return showInForm;
    }

    public void setShowInForm(boolean showInForm) {
        this.showInForm = showInForm;
    }

    public Object getDefaultValue(boolean... asString) {
        if (asString != null && asString.length > 0 && asString[0]) {
            return defaultValue;
        }
        if (defaultValue != null) {
            if (type.equals(WebFormConstants.INPUT_DROPDOWN)) {
                //if(type is DROPDOWN) then the Value must be Integer, the ID of the Selected Item.
                try {
                    return Integer.parseInt(defaultValue);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            } else if (type.equals(WebFormConstants.INPUT_CHECKBOX) || type.equals(WebFormConstants.INPUT_RADIO_BUTTON)) {
                try {
                    return Boolean.parseBoolean(defaultValue);
                } catch (Exception e) {
                    return false;
                }
            } else if (type.equals(WebFormConstants.INPUT_DATEPICKER)) {
                if (!"".equals(defaultValue)) {
                    try {
                        return new Date(Long.parseLong(defaultValue));
                    } catch (Exception e) {
                        try {
                            return DateUtils.parse(defaultValue);
                        } catch (DateFormatException f) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue, boolean... forCheckBoxOrRadioButton) {
        if (this.defaultValue != null && forCheckBoxOrRadioButton != null && forCheckBoxOrRadioButton.length > 0 && forCheckBoxOrRadioButton[0]) {
            if (!this.defaultValue.equals(defaultValue) && !(this.defaultValue.contains(defaultValue + ",") || this.defaultValue.contains("," + defaultValue))) {
                if (this.defaultValue.equals("")) {
                    this.defaultValue = this.defaultValue.trim() + defaultValue;
                } else {
                    this.defaultValue = this.defaultValue.trim() + "," + defaultValue;
                }
            }
        } else {
            this.defaultValue = defaultValue;
        }
    }

    public void removeDefaultValue(String defaultValue, boolean... forCheckBoxOrRadioButton) {
        if (this.defaultValue != null && forCheckBoxOrRadioButton != null && forCheckBoxOrRadioButton.length > 0 && forCheckBoxOrRadioButton[0]) {
            if (this.defaultValue.equals(defaultValue) || this.defaultValue.contains(defaultValue + ",") || this.defaultValue.contains("," + defaultValue)) {
                if (this.defaultValue.equals(defaultValue)) {
                    this.defaultValue = null;
                } else if (this.defaultValue.contains("," + defaultValue)) {
                    this.defaultValue = this.defaultValue.replace("," + defaultValue, "");
                } else if (this.defaultValue.contains(defaultValue + ",")) {
                    this.defaultValue = this.defaultValue.replace(defaultValue + ",", "");
                }
            }
        } else {
            this.defaultValue = null;
        }
    }

    public Integer getSavingField() {
        return savingField;
    }

    public void setSavingField(Integer savingField) {
        this.savingField = savingField;
    }

    public SelectItem[] getValues() {
        return values;
    }

    public void setValues(SelectItem[] values) {
        this.values = values;
    }

    public boolean isUnchangable() {
        return unchangable;
    }

    public void setUnchangable(boolean unchangable) {
        this.unchangable = unchangable;
    }

    public boolean isDrawLine() {
        return drawLine;
    }

    public void setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isCustomField() {
        return isCustomField;
    }

    public void setCustomField(boolean customField) {
        isCustomField = customField;
    }

    public boolean isOnlyIntegerAllowed() {
        return onlyIntegerAllowed;
    }

    public void setOnlyIntegerAllowed(boolean onlyIntegerAllowed) {
        this.onlyIntegerAllowed = onlyIntegerAllowed;
    }
}
