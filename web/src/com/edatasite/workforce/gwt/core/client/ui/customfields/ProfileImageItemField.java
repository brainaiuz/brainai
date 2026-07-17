package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

/**
 * User: Faxriddin Taslimov
 * Date: 13.11.2019
 */
public class ProfileImageItemField extends AbstractCustomField {

    private ProfileImage profileImage;

    public ProfileImageItemField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public ProfileImageItemField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    @Override
    public void initilazation() {
        profileImage = new ProfileImage();
        addField(customFieldItem.getFieldName(), profileImage);
        setValue(customFieldItem);
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        profileImage.getImageById(customFields.getProfielImageId(), "Profile", "Image", true);
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        customFieldItem.setProfielImageId(profileImage.getImageID());
        customFieldItem.setFieldStringValue(String.valueOf(profileImage.getImageID()));
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        profileImage = new ProfileImage();
        profileImage.getImageById(null, "Profile", "Image", true);
        return profileImage;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        customFieldItem.setProfielImageId(profileImage.getImageID());
        customFieldItem.setFieldStringValue(String.valueOf(profileImage.getImageID()));
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return true;
    }
}
