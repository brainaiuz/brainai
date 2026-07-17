package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.CountryStates;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Hayot
 * Date: Aug 12, 2010
 * Time: 5:30:05 PM
 */
public class AddLeadFormView extends AbstractAddFormView implements Constants {

    private WfmForm.Field assigneeField;
    private WfmForm.Field backupAssigneeField;
    private WfmForm.Field companyNameField;
    private WfmForm.Field firstNameField;
    private WfmForm.Field lastNameField;
    private WfmForm.Field jobTitleField;
    private WfmForm.Field streetField;
    private WfmForm.Field cityTownField;
    private WfmForm.Field countryField;
    private WfmForm.Field stateField;
    private WfmForm.Field postCodeField;
    private WfmForm.Field leadSourceField;
    private WfmForm.Field leadStatusField;
    private WfmForm.Field industryField;
    private WfmForm.Field campaignSourceField;
    private WfmForm.Field email1Field;
    private WfmForm.Field email2Field;
    private WfmForm.Field email3Field;
    private WfmForm.Field phone1Field;
    private WfmForm.Field phone2Field;
    private WfmForm.Field phone3Field;
    private WfmForm.Field faxField;
    private WfmForm.Field mobileField;
    private WfmForm.Field websiteField;
    private WfmForm.Field emailOptOutField;
    private WfmForm.Field ratingField;
    private WfmForm.Field noteField;
    private WfmForm.Field uploadField;
    private WfmForm.Field mailingListField;
    private CountryStates countryStates;
    private ContactListItem item;
    private final WebFormItem webFormItem;
    private final boolean enableAccess;
    private final boolean fromSubscriptionForm;

    public AddLeadFormView(WebForm webForm, VerticalPanel antibotPanel, boolean enableAccess, boolean fromSubscriptionForm) {
        super(webForm, antibotPanel);
        this.enableAccess = enableAccess;
        this.fromSubscriptionForm = fromSubscriptionForm;
        webFormItem = new WebFormItem();
    }

    @Override
    public void setWebFormID(Integer webFormID) {
        if (item == null) {
            item = new ContactListItem();
        }
        item.setWebFormID(webFormID);
        countryStates = new CountryStates();
    }

    public void addField(WebField webField) {
        if (!webField.isCustomField()) {
            String nickIdName = "add_lead_form_view_";
            switch (webField.getSavingField()) {
                case LeadField.FIELD_ASSIGNEE:
                    assigneeField = addFieldToForm(webField);
                    assigneeField.ensureDebugId(nickIdName + "assigneeField");
                    break;
                case LeadField.FIELD_BACKUP_ASSIGNEE:
                    backupAssigneeField = addFieldToForm(webField);
                    backupAssigneeField.ensureDebugId(nickIdName + "backupAssigneeField");
                    break;
                case LeadField.FIELD_COMPANYNAME:
                    companyNameField = addFieldToForm(webField);
                    companyNameField.ensureDebugId(nickIdName + "companyNameField");
                    break;
                case LeadField.FIELD_FIRSTNAME:
                    firstNameField = addFieldToForm(webField);
                    firstNameField.ensureDebugId(nickIdName + "firstNameField");
                    break;
                case LeadField.FIELD_LASTNAME:
                    lastNameField = addFieldToForm(webField);
                    lastNameField.ensureDebugId(nickIdName + "lastNameField");
                    break;
                case LeadField.FIELD_JOBTITLE:
                    jobTitleField = addFieldToForm(webField);
                    jobTitleField.ensureDebugId(nickIdName + "jobTitleField");
                    break;
                case LeadField.FIELD_STREET:
                    streetField = addFieldToForm(webField);
                    streetField.ensureDebugId(nickIdName + "stateField");
                    break;
                case LeadField.FIELD_CITYTOWN:
                    cityTownField = addFieldToForm(webField);
                    cityTownField.ensureDebugId(nickIdName + "cityTownField");
                    break;
                case LeadField.FIELD_COUNTRY:
                    countryField = addFieldToForm(webField);
                    countryField.ensureDebugId(nickIdName + "countryField");
                    countryStates.setCountryField((DataListBox) countryField.getWidgets()[0]);
                    countryStates.setCountries(countryStates.getCountryField().getItems());
                    break;
                case LeadField.FIELD_STATE:
                    stateField = addFieldToForm(webField);
                    stateField.ensureDebugId(nickIdName + "stateField");
                    countryStates.setStateField((DataListBox) stateField.getWidgets()[0]);
                    countryStates.setStates(countryStates.getStateField().getItems());
                    Integer countryID = item.getPrimaryAddress(true).getCountryId();
                    SelectItem selectedState = countryStates.getStateField().getSelectedItem();
                    countryStates.getStateField().removeListItems();
                    countryID = countryID == null ? countryStates.getCountryField().getSelectedId() : countryID;
                    if (countryID != null) {
                        countryStates.checkForStates(countryID, countryStates.getStateField());
                        if (selectedState != null) {
                            countryStates.getStateField().setSelected(selectedState);
                        }
                    }
                    break;
                case LeadField.FIELD_POSTCODE:
                    postCodeField = addFieldToForm(webField);
                    postCodeField.ensureDebugId(nickIdName + "postCodeField");
                    break;
                case LeadField.FIELD_LEADSOURCE:
                    leadSourceField = addFieldToForm(webField);
                    leadSourceField.ensureDebugId(nickIdName + "leadSourceField");
                    break;
                case LeadField.FIELD_CAMPAIGNSOURCE:
                    campaignSourceField = addFieldToForm(webField);
                    campaignSourceField.ensureDebugId(nickIdName + "campaignSourceField");
                    break;
                case LeadField.FIELD_LEADSTATUS:
                    leadStatusField = addFieldToForm(webField);
                    leadStatusField.ensureDebugId(nickIdName + "leadStatusField");
                    break;
                case LeadField.FIELD_INDUSTRY:
                    industryField = addFieldToForm(webField);
                    industryField.ensureDebugId(nickIdName + "industryField");
                    break;
                case LeadField.FIELD_EMAIL1:
                    email1Field = addFieldToForm(webField);
                    email1Field.ensureDebugId(nickIdName + "email1Field");
                    break;
                case LeadField.FIELD_EMAIL2:
                    email2Field = addFieldToForm(webField);
                    email2Field.ensureDebugId(nickIdName + "email2Field");
                    break;
                case LeadField.FIELD_EMAIL3:
                    email3Field = addFieldToForm(webField);
                    email3Field.ensureDebugId(nickIdName + "email3Field");
                    break;
                case LeadField.FIELD_PHONE1:
                    phone1Field = addFieldToForm(webField);
                    phone1Field.ensureDebugId(nickIdName + "phone1Field");
                    break;
                case LeadField.FIELD_PHONE2:
                    phone2Field = addFieldToForm(webField);
                    phone2Field.ensureDebugId(nickIdName + "phone2Field");
                    break;
                case LeadField.FIELD_PHONE3:
                    phone3Field = addFieldToForm(webField);
                    phone3Field.ensureDebugId(nickIdName + "phone3Field");
                    break;
                case LeadField.FIELD_FAX:
                    faxField = addFieldToForm(webField);
                    faxField.ensureDebugId(nickIdName + "faxField");
                    break;
                case LeadField.FIELD_MOBILE:
                    mobileField = addFieldToForm(webField);
                    mobileField.ensureDebugId(nickIdName + "mobileField");
                    break;
                case LeadField.FIELD_WEBSITE:
                    websiteField = addFieldToForm(webField);
                    websiteField.ensureDebugId(nickIdName + "websiteField");
                    break;
                case LeadField.FIELD_EMAILOPTOUT:
                    emailOptOutField = addFieldToForm(webField);
                    emailOptOutField.ensureDebugId(nickIdName + "emailOptOutField");
                    break;
                case LeadField.FIELD_RATING:
                    ratingField = addFieldToForm(webField);
                    ratingField.ensureDebugId(nickIdName + "ratingField");
                    break;
                case LeadField.FIELD_NOTE:
                    noteField = addFieldToForm(webField);
                    noteField.ensureDebugId(nickIdName + "noteField");
                    break;
                case LeadField.FIELD_ATTACHMENT:
                    uploadField = addFieldToForm(webField);
                    uploadField.ensureDebugId(nickIdName + "uploadField");
                    break;
                case LeadField.FIELD_MAILING_LIST:
                    mailingListField = addFieldToForm(webField);
                    mailingListField.ensureDebugId(nickIdName + "mailingListField");
                    break;
            }
        } else {
            if (customFields == null) {
                customFields = new HashMap<>();
            }
            if (!customFields.containsKey(webField.getSavingField())) {
                addCustomFieldToMap(webField);
            }
        }
    }

    @Override
    protected void addListeners() {
    }

    public void fillItem(WebField webField, Object value, Widget widget) {
        boolean validate = false;
        if (webField.isMandatory() && webField.isShowInForm()) {
            validate = true;
        }
        if (!webField.isCustomField()) {
            WfmForm.Field field = null;
            boolean isEmail = false;
            switch (webField.getSavingField()) {
                case LeadField.FIELD_ASSIGNEE:
                    item.setLeadAssigneeID(getAsSelectItem(value).getId());
                    item.setLeadAssignee(getAsSelectItem(value).getName());
                    field = assigneeField;
                    break;
                case LeadField.FIELD_BACKUP_ASSIGNEE:
                    item.setLeadBackupAssigneeID(getAsSelectItem(value).getId());
                    item.setLeadBackupAssignee(getAsSelectItem(value).getName());
                    field = backupAssigneeField;
                    break;
                case LeadField.FIELD_COMPANYNAME:
                    item.getCrmAccount().setName(getAsString(value));
                    field = companyNameField;
                    break;
                case LeadField.FIELD_FIRSTNAME:
                    item.setFirstName(getAsString(value));
                    field = firstNameField;
                    break;
                case LeadField.FIELD_LASTNAME:
                    item.setLastName(getAsString(value));
                    field = lastNameField;
                    break;
                case LeadField.FIELD_JOBTITLE:
                    item.setJobTitle(getAsString(value));
                    field = jobTitleField;
                    break;
                case LeadField.FIELD_CITYTOWN:
                    item.getAddresses(true).get(0).setCity(getAsString(value));
                    field = cityTownField;
                    break;
                case LeadField.FIELD_STREET:
                    item.getAddresses(true).get(0).setAddress(getAsString(value));
                    field = streetField;
                    break;
                case LeadField.FIELD_COUNTRY:
                    item.getAddresses(true).get(0).setCountryId(getAsSelectItem(value).getId());
                    item.getAddresses(true).get(0).setCountry(getAsSelectItem(value).getName());
                    field = countryField;
                    break;
                case LeadField.FIELD_STATE:
                    item.getAddresses(true).get(0).setStateId(getAsSelectItem(value).getId());
                    item.getAddresses(true).get(0).setState(getAsSelectItem(value).getName());
                    field = stateField;
                    break;
                case LeadField.FIELD_POSTCODE:
                    item.getAddresses(true).get(0).setZipCode(getAsString(value));
                    field = postCodeField;
                    break;
                case LeadField.FIELD_LEADSOURCE:
                    item.setLeadSourceID(getAsSelectItem(value).getId());
                    item.setLeadSource(getAsSelectItem(value).getName());
                    field = leadSourceField;
                    break;
                case LeadField.FIELD_CAMPAIGNSOURCE:
                    item.setCampaignId(getAsSelectItem(value).getId());
                    item.setCampaign(getAsSelectItem(value).getName());
                    field = campaignSourceField;
                    break;
                case LeadField.FIELD_LEADSTATUS:
                    item.setLeadStatus(getAsSelectItem(value));
                    field = leadStatusField;
                    break;
                case LeadField.FIELD_INDUSTRY:
                    item.getCrmAccount().setIndustryID(getAsSelectItem(value).getId());
                    item.getCrmAccount().setIndustry(getAsSelectItem(value).getName());
                    field = industryField;
                    break;
                case LeadField.FIELD_EMAIL1:
                    item.setHomeEmail(new ArrayList<>());
                    item.getHomeEmail().add(getAsString(value));
                    field = email1Field;
                    isEmail = true;
                    if (!validate && widget != null && !((TextBox) widget).getText().equals("") && email1Field != null && !validateTextBox(email1Field, (TextBox) widget)) {
                        addErrors(webField, webField.getSavingField());
                    }
                    break;
                case LeadField.FIELD_EMAIL2:
                    item.setWorkEmail(new ArrayList<>());
                    item.getWorkEmail().add(getAsString(value));
                    field = email2Field;
                    isEmail = true;
                    if (!validate && widget != null && !((TextBox) widget).getText().equals("") && email2Field != null && !validateTextBox(email2Field, (TextBox) widget)) {
                        addErrors(webField, webField.getSavingField());
                    }
                    break;
                case LeadField.FIELD_EMAIL3:
                    item.setOtherEmail(new ArrayList<>());
                    item.getOtherEmail().add(getAsString(value));
                    field = email3Field;
                    isEmail = true;
                    if (!validate && widget != null && !((TextBox) widget).getText().equals("") && email3Field != null && !validateTextBox(email3Field, (TextBox) widget)) {
                        addErrors(webField, webField.getSavingField());
                    }
                    break;
                case LeadField.FIELD_PHONE1:
                    item.setHomePhone(new ArrayList<>());
                    item.getHomePhone().add(getAsString(value));
                    field = phone1Field;
                    break;
                case LeadField.FIELD_PHONE2:
                    item.setWorkPhone(new ArrayList<>());
                    item.getWorkPhone().add(getAsString(value));
                    field = phone2Field;
                    break;
                case LeadField.FIELD_PHONE3:
                    item.setOtherPhone(new ArrayList<>());
                    item.getOtherPhone().add(getAsString(value));
                    field = phone3Field;
                    break;
                case LeadField.FIELD_FAX:
                    item.setHomeFax(new ArrayList<>());
                    item.setHomeFax(getPhoneAsString(value));
                    field = faxField;
                    break;
                case LeadField.FIELD_MOBILE:
                    item.setMobile(new ArrayList<>());
                    item.setMobile(getPhoneAsString(value));
                    field = mobileField;
                    break;
                case LeadField.FIELD_WEBSITE:
                    item.setHomeWebSite(new ArrayList<>());
                    item.getHomeWebSite().add(getAsString(value));
                    field = websiteField;
                    break;
                case LeadField.FIELD_EMAILOPTOUT:
                    item.setEmailOptOut(getAsBoolean(value));
                    field = emailOptOutField;
                    break;
                case LeadField.FIELD_RATING:
                    item.setLeadRatingID(getAsSelectItem(value).getId());
                    item.setLeadRating(getAsSelectItem(value).getName());
                    field = ratingField;
                    break;
                case LeadField.FIELD_NOTE:
                    item.setNote(getAsString(value));
                    field = noteField;
                    break;
                case LeadField.FIELD_ATTACHMENT:
                    if (value instanceof FileItem || value instanceof FileItem[]) {
                        item.setAttachments((FileItem[]) value);
                    }
                    field = uploadField;
                    break;
                case LeadField.FIELD_MAILING_LIST:
                    if (value instanceof ArrayList) {
                        item.setSubscriptionIDs((ArrayList<Integer>) value);
                    }
                    field = mailingListField;
                    break;
            }
            validate(validate, field, webField, widget, isEmail);
        } else {
            if (customFields != null && customFields.containsKey(webField.getSavingField())) {
                List fieldCustomFieldAndWidgets = customFields.get(webField.getSavingField());
                WfmForm.Field wfmField = (WfmForm.Field) fieldCustomFieldAndWidgets.get(0);
                CompanyCustomFieldItem customField = (CompanyCustomFieldItem) fieldCustomFieldAndWidgets.get(1);
                Widget[] widgets = (Widget[]) fieldCustomFieldAndWidgets.get(2);
                setCustomFieldValues(wfmField, webField, customField, widgets);
                fieldCustomFieldAndWidgets = new ArrayList();
                fieldCustomFieldAndWidgets.add(wfmField);
                fieldCustomFieldAndWidgets.add(customField);
                fieldCustomFieldAndWidgets.add(widgets);
                customFields.remove(webField.getSavingField());
                customFields.put(webField.getSavingField(), fieldCustomFieldAndWidgets);
            }/* else {
                addCustomFieldToMap(webField);
            }*/
        }
    }

    private void validate(boolean validate, final WfmForm.Field field, WebField webField, Widget widget, boolean... isEmails) {
        if (validate) {
            validate(field, webField, widget, isEmails);
        }
    }

    private void prepareCustomFields() {
        if (customFields != null && customFields.size() > 0) {
            ArrayList<CompanyCustomFieldItem> customFieldsArray = new ArrayList<>();
            for (List wfmFieldCustomFieldAndWidgets : customFields.values()) {
                if (wfmFieldCustomFieldAndWidgets.size() > 1) {
                    CompanyCustomFieldItem customField = (CompanyCustomFieldItem) wfmFieldCustomFieldAndWidgets.get(1);
                    if (customField != null) {
                        customFieldsArray.add(customField);
                    }
                } else if (wfmFieldCustomFieldAndWidgets.size() == 1) {
                    if (wfmFieldCustomFieldAndWidgets.get(0) instanceof CompanyCustomFieldItem) {
                        CompanyCustomFieldItem customField = (CompanyCustomFieldItem) wfmFieldCustomFieldAndWidgets.get(0);
                        if (customField != null) {
                            customFieldsArray.add(customField);
                        }
                    }
                }
            }
            item.setCustomFields(customFieldsArray);
        }
    }

    public void save(String antibot) {
        LoadingPanel.loading(true);
        if (uploadField != null) {
            if (uploadField.getWidgets() != null) {
                if (uploadField.getWidgets()[0] != null && uploadField.getWidgets()[0] instanceof WebFormsFileUpload) {
                    WebFormsFileUpload fileUpload = (WebFormsFileUpload) uploadField.getWidgets()[0];
                    item.setAttachments(fileUpload.getAttachedFiles());
                }
            }
        }
        item.setWebFormID(webForm.getObjectId());
        prepareCustomFields();
        webFormItem.setContactListItem(item);
        webFormItem.setCompanyID(Integer.valueOf(Utils.getEncryptedCompanyID()));
        webFormItem.setWebformID(item.getWebFormID());
        webFormItem.setWebformType(WebFormConstants.LEAD_FORM);
        webFormItem.setEnableAccess(enableAccess ? Boolean.TRUE : Boolean.FALSE);
        webFormItem.setFromSubscriptionForm(fromSubscriptionForm ? Boolean.TRUE : Boolean.FALSE);
        if (haveErrors()) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            LoadingPanel.loading(false);
            if (errorOn != null) {
                errorOn.execute();
            }
        } else {
            antibot += "|" + Cookies.getCookie("JSESSIONID");
            webFormItem.setAntibot(webForm.getUseCatpcha() ? antibot : null);
            webFormsService.saveForm(webFormItem, new AsyncCallback<HashMap<String, String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                    if (errorOn != null) {
                        errorOn.execute();
                    }
                }

                @Override
                public void onSuccess(HashMap<String, String> result) {
                    LoadingPanel.loading(false);
                    if (result != null) {
                        boolean noError = false;
                        if (result.containsKey("ERROR_CAPTCHA")) {
                            Info.show(result.get("ERROR_CAPTCHA"), Info.Type.WARNING);
                        } else if (result.containsKey("ID")) {
                            if (addedSuccessfully != null) {
                                Cookies.setCookie(LEAD_ID_COOKIE, result.get("ID"));
                                addedSuccessfully.execute();
                            }
                            noError = true;
                        } else {
                            Info.show(wfmStrings.emailAlreadyExists(), Info.Type.WARNING);
                        }
                        if (!noError && errorOn != null) {
                            errorOn.execute();
                        } else {
                            if (addedSuccessfully != null) {
                                addedSuccessfully.execute();
                            }
                        }
                    } else {
                        if (addedSuccessfully != null) {
                            addedSuccessfully.execute();
                        }
                    }
                }
            });
        }
    }

    public Command getAddedSuccessfully() {
        return addedSuccessfully;
    }

    public void setAddedSuccessfully(Command addedSuccessfully) {
        this.addedSuccessfully = addedSuccessfully;
    }

    public Command getErrorOn() {
        return errorOn;
    }

    public void setErrorOn(Command errorOn) {
        this.errorOn = errorOn;
    }
}