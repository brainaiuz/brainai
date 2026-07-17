package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 7/19/12
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomFormValidation implements IsSerializable {
    private String fieldCode;
    private String widgetType;
    private Integer validationTypeID;
    private String joinedFieldCode;

    public CustomFormValidation() {

    }

    public CustomFormValidation(String validationsString) {
        this();
        if (validationsString != null && !"".equals(validationsString)) {
            String[] splits = validationsString.split("\\#\\#");
            if (splits != null && splits.length > 0) {
                setFieldCode(splits[0]);
                setWidgetType(splits[1]);
                setValidationTypeID(Integer.valueOf(splits[2]));
                if (splits.length > 3) {
                    setJoinedFieldCode(splits[3]);
                }
            }
        }
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public Integer getValidationTypeID() {
        return validationTypeID;
    }

    public void setValidationTypeID(Integer validationTypeID) {
        this.validationTypeID = validationTypeID;
    }

    public String getJoinedFieldCode() {
        return joinedFieldCode;
    }

    public void setJoinedFieldCode(String joinedFieldCode) {
        this.joinedFieldCode = joinedFieldCode;
    }

    public static ArrayList<CustomFormValidation> parse(String validationsString) {
        ArrayList<CustomFormValidation> result = new ArrayList<>();
        if (validationsString != null && !"".equals(validationsString)) {
            String[] validations = validationsString.split("\\$\\$");
            if (validations != null && validations.length > 0) {
                for (String validationString : validations) {
                    CustomFormValidation validation = new CustomFormValidation(validationString);
                    if (validation.isValid()) {
                        result.add(validation);
                    }
                }
            }
        }
        return result;
    }

    private boolean isValid() {
        return fieldCode != null && !"".equals(fieldCode) && validationTypeID != null && !"".equals(validationTypeID);
    }

    public static String parse(ArrayList<CustomFormValidation> validations) {
        StringBuilder result = new StringBuilder();
        if (validations != null && validations.size() > 0) {
            for (CustomFormValidation validation : validations) {
                result.append(validation.toString()).append("$$");
            }
        }
        return result.toString();
    }

    public String toString() {
        return fieldCode + "##" + widgetType + "##" + validationTypeID + "##" + (joinedFieldCode == null ? "" : joinedFieldCode);
    }
}
