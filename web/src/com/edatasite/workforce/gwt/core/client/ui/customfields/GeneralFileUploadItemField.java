package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUploadItem;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 08.04.2017 16:58
 */
public class GeneralFileUploadItemField extends AbstractCustomField {

    private GeneralFileUploadItem generalFileUploadItem;

    public GeneralFileUploadItemField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public GeneralFileUploadItemField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        generalFileUploadItem = new GeneralFileUploadItem(Constants.F_CUSTOM_FIELD_ITEM);
        addField(customFieldItem.getFieldName(), generalFileUploadItem);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null) {
            generalFileUploadItem.setFiles(Double.valueOf(customFields.getFieldStringValue()).intValue(), customFields.getObjectId(), false);
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setAttachments(generalFileUploadItem.getAttachedFiles1());
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        generalFileUploadItem = new GeneralFileUploadItem(Constants.F_CUSTOM_FIELD_ITEM);
        return generalFileUploadItem;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setAttachments(generalFileUploadItem.getAttachedFiles1());
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return generalFileUploadItem.getAttachedFiles() != null && generalFileUploadItem.getAttachedFiles().length > 0;
    }
}
