package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormsService;
import com.edatasite.workforce.gwt.webforms.client.WebFormsServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Hayot
 * Date: Aug 9, 2010
 * Time: 9:02:18 PM
 */
public class LeadForm extends Form implements LeadField {

    final static WebFormsServiceAsync webFormsService = WebFormsService.App.get();

    private WebField assigneeField;
    private WebField backupAssigneeField;
    private WebField companyNameField;
    private WebField firstNameField;
    private WebField lastNameField;
    private WebField jobTitleField;
    private WebField streetField;
    private WebField cityTownField;
    private WebField countryField;
    private WebField stateField;
    private WebField postCodeField;
    private WebField leadSourceField;
    private WebField campaignSourceField;
    private WebField leadStatusField;
    private WebField industryField;
    private WebField email1Field;
    private WebField email2Field;
    private WebField email3Field;
    private WebField phoneField;
    private WebField phone2Field;
    private WebField phone3Field;
    private WebField faxField;
    private WebField mobileField;
    private WebField websiteField;
    private WebField emailOptOutField;
    private WebField ratingField;
    private WebField noteField;
    private WebField uploadField;
    private WebField subscriptionField;

    public LeadForm(boolean updateWebFields, WebField... fields) {
        if (fields != null && fields.length > 0) {
            setWebFields(fields);
            for (WebField webField : fields) {
                if (!webField.isCustomField()) {
                    switch (webField.getSavingField()) {
                        case LeadField.FIELD_ASSIGNEE:
                            assigneeField = webField;
                            break;
                        case LeadField.FIELD_BACKUP_ASSIGNEE:
                            backupAssigneeField = webField;
                            break;
                        case LeadField.FIELD_COMPANYNAME:
                            companyNameField = webField;
                            break;
                        case LeadField.FIELD_FIRSTNAME:
                            firstNameField = webField;
                            break;
                        case LeadField.FIELD_LASTNAME:
                            lastNameField = webField;
                            break;
                        case LeadField.FIELD_JOBTITLE:
                            jobTitleField = webField;
                            break;
                        case LeadField.FIELD_CITYTOWN:
                            cityTownField = webField;
                            break;
                        case LeadField.FIELD_STREET:
                            streetField = webField;
                            break;
                        case LeadField.FIELD_COUNTRY:
                            countryField = webField;
                            break;
                        case LeadField.FIELD_STATE:
                            stateField = webField;
                            break;
                        case LeadField.FIELD_POSTCODE:
                            postCodeField = webField;
                            break;
                        case LeadField.FIELD_LEADSOURCE:
                            leadSourceField = webField;
                            break;
                        case LeadField.FIELD_CAMPAIGNSOURCE:
                            campaignSourceField = webField;
                            break;
                        case LeadField.FIELD_LEADSTATUS:
                            leadStatusField = webField;
                            break;
                        case LeadField.FIELD_INDUSTRY:
                            industryField = webField;
                            break;
                        case LeadField.FIELD_EMAIL1:
                            email1Field = webField;
                            break;
                        case LeadField.FIELD_EMAIL2:
                            email2Field = webField;
                            break;
                        case LeadField.FIELD_EMAIL3:
                            email3Field = webField;
                            break;
                        case LeadField.FIELD_PHONE1:
                            phoneField = webField;
                            break;
                        case LeadField.FIELD_PHONE2:
                            phone2Field = webField;
                            break;
                        case LeadField.FIELD_PHONE3:
                            phone3Field = webField;
                            break;
                        case LeadField.FIELD_FAX:
                            faxField = webField;
                            break;
                        case LeadField.FIELD_MOBILE:
                            mobileField = webField;
                            break;
                        case LeadField.FIELD_WEBSITE:
                            websiteField = webField;
                            break;
                        case LeadField.FIELD_EMAILOPTOUT:
                            emailOptOutField = webField;
                            break;
                        case LeadField.FIELD_RATING:
                            ratingField = webField;
                            break;
                        case LeadField.FIELD_NOTE:
                            noteField = webField;
                            break;
                        case LeadField.FIELD_ATTACHMENT:
                            uploadField = webField;
                            break;
                        case LeadField.FIELD_MAILING_LIST:
                            subscriptionField = webField;
                            break;
                    }
                } else {
                    getCustomFields().put(webField.getSavingField(), webField);
                }
            }
            this.updateWebFields = updateWebFields;
            if (this.updateWebFields) {
                init();
            }
        } else {
            init();
        }
        fillFieldsWithDataFromServer();
    }

    public void init() {
        int sortOrder = 1;
        if (assigneeField == null) {
            assigneeField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_ASSIGNEE, wfmStrings.assignee(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(assigneeField);
        }
        if (backupAssigneeField == null) {
            backupAssigneeField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_BACKUP_ASSIGNEE, wfmStrings.backupAssignee(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(backupAssigneeField);
        }
        if (companyNameField == null) {
            companyNameField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_COMPANYNAME, wfmStrings.company(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(companyNameField);
        }
        if (firstNameField == null) {
            firstNameField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_FIRSTNAME, wfmStrings.firstName(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(firstNameField);
        }
        if (lastNameField == null) {
            lastNameField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_LASTNAME, wfmStrings.lastName(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(lastNameField);
        }
        if (jobTitleField == null) {
            jobTitleField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_JOBTITLE, wfmStrings.jobTitle(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(jobTitleField);
        }
        if (streetField == null) {
            streetField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_STREET, wfmStrings.street(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(streetField);
        }
        if (cityTownField == null) {
            cityTownField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_CITYTOWN, wfmStrings.city(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(cityTownField);
        }
        if (countryField == null) {
            countryField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_COUNTRY, wfmStrings.country(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(countryField);
        }
        if (stateField == null) {
            stateField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_STATE, wfmStrings.state(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(stateField);
        }
        if (postCodeField == null) {
            postCodeField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_POSTCODE, wfmStrings.postCode(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(postCodeField);
        }
        if (leadSourceField == null) {
            leadSourceField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_LEADSOURCE, Property.get(Constants.LEADS, wfmStrings.leadSource(), wfmStrings.lead()), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(leadSourceField);
        }
        if (campaignSourceField == null) {
            campaignSourceField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_CAMPAIGNSOURCE, wfmStrings.campaignSource(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(campaignSourceField);
        }
        if (leadStatusField == null) {
            leadStatusField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_LEADSTATUS, Property.get(Constants.LEADS, wfmStrings.status(), wfmStrings.lead()), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(leadStatusField);
        }
        if (industryField == null) {
            industryField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_INDUSTRY, wfmStrings.industry(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(industryField);
        }
        if (email1Field == null) {
            email1Field = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_EMAIL1, wfmStrings.email(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(email1Field);
        }
        if (email2Field == null) {
            email2Field = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_EMAIL2, wfmStrings.email2(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(email2Field);
        }
        if (email3Field == null) {
            email3Field = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_EMAIL3, wfmStrings.email2(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(email3Field);
        }
        if (phoneField == null) {
            phoneField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_PHONE1, wfmStrings.phone(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(phoneField);
        }
        if (phone2Field == null) {
            phone2Field = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_PHONE2, wfmStrings.phone()+" 2", WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(phone2Field);
        }
        if (phone3Field == null) {
            phone3Field = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_PHONE3, wfmStrings.phone()+" 3", WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(phone3Field);
        }
        if (faxField == null) {
            faxField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_FAX, wfmStrings.fax(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(faxField);
        }
        if (mobileField == null) {
            mobileField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_MOBILE, wfmStrings.mobile(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(mobileField);
        }
        if (websiteField == null) {
            websiteField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_WEBSITE, wfmStrings.website(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(websiteField);
        }
        if (emailOptOutField == null) {
            emailOptOutField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_EMAILOPTOUT, wfmStrings.emailOptOut(), WebFormConstants.INPUT_CHECKBOX, false, false, "", null);
            addWebField(emailOptOutField);
        }
        if (ratingField == null) {
            ratingField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_RATING, wfmStrings.rating(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(ratingField);
        }
        if (noteField == null) {
            noteField = new WebField(getSortOrder(updateWebFields, sortOrder++), LeadField.FIELD_NOTE, wfmStrings.note(), WebFormConstants.INPUT_TEXTAREA2, false, false, "", null);
            addWebField(noteField);
        }
        if (subscriptionField == null) {
            subscriptionField = new WebField(getSortOrder(updateWebFields, sortOrder), LeadField.FIELD_MAILING_LIST, wfmStrings.subscriptions(), WebFormConstants.INPUT_MAILING_LIST, false, false, "", null);
            addWebField(subscriptionField);
        }
        if (uploadField == null) {
            uploadField = new WebField(getSortOrder(updateWebFields, sortOrder), LeadField.FIELD_ATTACHMENT, wfmStrings.attachments(), WebFormConstants.INPUT_ATTACHMENT, false, false, "", null);
            addWebField(uploadField);
        }
        webFormsService.getCustomFields(ViewName.Lead, new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(ArrayList<CompanyCustomFieldItem> customFields) {
                if (customFields != null && customFields.size() > 0) {
                    for (CompanyCustomFieldItem customFieldItem : customFields) {
                        addCustomField(customFieldItem);
                    }
                }
            }
        });
    }

    @Override
    public void fillFieldsWithDataFromServer() {
        webFormsService.fillDropDowns(null, WebFormConstants.LEAD_FORM, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(HashMap<String, SelectItem[]> result) {
                if (result != null && result.size() > 0) {
                    if (assigneeField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_ASSIGNEES)) {
                        assigneeField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_ASSIGNEES));
                        if (backupAssigneeField != null) {
                            backupAssigneeField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_ASSIGNEES));
                        }
                    }
                    if (countryField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_COUNTIRES)) {
                        countryField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_COUNTIRES));
                    }
                    if (stateField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_STATES)) {
                        stateField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_STATES));
                    }
                    if (leadSourceField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_SOURCES)) {
                        leadSourceField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_SOURCES));
                    }
                    if (campaignSourceField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_CAMPAIGNS)) {
                        campaignSourceField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_CAMPAIGNS));
                    }
                    if (leadStatusField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_STATUSES)) {
                        leadStatusField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                    }
                    if (industryField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_INDUSTRIES)) {
                        industryField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_INDUSTRIES));
                    }
                    if (ratingField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_RATINGS)) {
                        ratingField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_RATINGS));
                    }
                    setCustomFieldValues(result);
                }
                setDropDownsFilled(true);
            }
        });
    }
}
