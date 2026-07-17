package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
public class CaseForm extends Form implements CaseField {
    final static WebFormsServiceAsync webFormsService = WebFormsService.App.get();

    WebField assigneeField;
    WebField subjectField;
    WebField descriptionField;
    WebField caseOriginField;
    WebField statusField;
    WebField typeField;
    //    WebField reportedByField;
    WebField reportedByOtherFirstNameField;
    WebField reportedByOtherLastNameField;
    WebField reportedByOtherCompanyField;
    WebField reportedByOtherEmailField;
    WebField reportedByOtherPhoneField;
    WebField reportedByOtherFaxField;
    WebField priorityField;
    WebField caseReasonField;
    WebField resolverField;
    WebField ratingField;
    WebField uploadField;

    public CaseForm(Boolean updateWebFields, WebField... fields) {
        if (fields != null && fields.length > 0) {
            setWebFields(fields);
            for (WebField webField : fields) {
                if (!webField.isCustomField()) {
                    switch (webField.getSavingField()) {
                        case CaseField.FIELD_ASSIGNEE:
                            assigneeField = webField;
                            break;
                        case CaseField.FIELD_SUBJECT:
                            subjectField = webField;
                            break;
                        case CaseField.FIELD_DESCRIPTION:
                            descriptionField = webField;
                            break;
                        case CaseField.FIELD_CASE_ORIGIN:
                            caseOriginField = webField;
                            break;
                        case CaseField.FIELD_STATUS:
                            statusField = webField;
                            break;
                        case CaseField.FIELD_TYPE:
                            typeField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME:
                            reportedByOtherFirstNameField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME:
                            reportedByOtherLastNameField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_COMPANY:
                            reportedByOtherCompanyField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_EMAIL:
                            reportedByOtherEmailField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_PHONE:
                            reportedByOtherPhoneField = webField;
                            break;
                        case CaseField.FIELD_REPORTED_BY_OTHER_FAX:
                            reportedByOtherFaxField = webField;
                            break;
                        case CaseField.FIELD_PRIORITY:
                            priorityField = webField;
                            break;
                        case CaseField.FIELD_CASE_REASON:
                            caseReasonField = webField;
                            break;
                        case CaseField.FIELD_RESOLVER:
                            resolverField = webField;
                            break;
                        case CaseField.FIELD_ATTACHMENT:
                            uploadField = webField;
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
        if (assigneeField == null) {
            assigneeField = new WebField(getSortOrder(updateWebFields, 1), CaseField.FIELD_ASSIGNEE, wfmStrings.assignee(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(assigneeField);
        }
        if (subjectField == null) {
            subjectField = new WebField(getSortOrder(updateWebFields, 2), CaseField.FIELD_SUBJECT, wfmStrings.subject(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null, true);
            addWebField(subjectField);
        }
        if (descriptionField == null) {
            descriptionField = new WebField(getSortOrder(updateWebFields, 3), CaseField.FIELD_DESCRIPTION, wfmStrings.description(), WebFormConstants.INPUT_TEXTAREA2, false, false, "", null);
            addWebField(descriptionField);
        }
        if (caseOriginField == null) {
            caseOriginField = new WebField(getSortOrder(updateWebFields, 4), CaseField.FIELD_CASE_ORIGIN, "Case Origin", WebFormConstants.INPUT_DROPDOWN, true, false, "", null, true);
            addWebField(caseOriginField);
        }
        if (statusField == null) {
            statusField = new WebField(getSortOrder(updateWebFields, 5), CaseField.FIELD_STATUS, wfmStrings.status(), WebFormConstants.INPUT_DROPDOWN, true, false, "", null, true);
            addWebField(statusField);
        }
        if (typeField == null) {
            typeField = new WebField(getSortOrder(updateWebFields, 6), CaseField.FIELD_TYPE, wfmStrings.type(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(typeField);
        }
        if (reportedByOtherFirstNameField == null) {
            reportedByOtherFirstNameField = new WebField(getSortOrder(updateWebFields, 7), CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME, wfmStrings.firstName(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(reportedByOtherFirstNameField);
        }
        if (reportedByOtherLastNameField == null) {
            reportedByOtherLastNameField = new WebField(getSortOrder(updateWebFields, 8), CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME, wfmStrings.lastName(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null, true);
            addWebField(reportedByOtherLastNameField);
        }
        if (reportedByOtherCompanyField == null) {
            reportedByOtherCompanyField = new WebField(getSortOrder(updateWebFields, 9), CaseField.FIELD_REPORTED_BY_OTHER_COMPANY, wfmStrings.company(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null, true);
            addWebField(reportedByOtherCompanyField);
        }
        if (reportedByOtherEmailField == null) {
            reportedByOtherEmailField = new WebField(getSortOrder(updateWebFields, 10), CaseField.FIELD_REPORTED_BY_OTHER_EMAIL, wfmStrings.email(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null, true);
            addWebField(reportedByOtherEmailField);
        }
        if (reportedByOtherPhoneField == null) {
            reportedByOtherPhoneField = new WebField(getSortOrder(updateWebFields, 11), CaseField.FIELD_REPORTED_BY_OTHER_PHONE, wfmStrings.phone(), WebFormConstants.INPUT_PHONENUMBER, false, false, "", null);
            addWebField(reportedByOtherPhoneField);
        }
        if (reportedByOtherFaxField == null) {
            reportedByOtherFaxField = new WebField(getSortOrder(updateWebFields, 12), CaseField.FIELD_REPORTED_BY_OTHER_FAX, wfmStrings.fax(), WebFormConstants.INPUT_PHONENUMBER, false, false, "", null);
            addWebField(reportedByOtherFaxField);
        }
        if (priorityField == null) {
            priorityField = new WebField(getSortOrder(updateWebFields, 13), CaseField.FIELD_PRIORITY, wfmStrings.priority(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(priorityField);
        }
        if (caseReasonField == null) {
            caseReasonField = new WebField(getSortOrder(updateWebFields, 14), CaseField.FIELD_CASE_REASON, Property.get(Constants.CASE_LIST, wfmStrings.caseReason(), wfmStrings.crmCase()), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(caseReasonField);
        }
        if (resolverField == null) {
            resolverField = new WebField(getSortOrder(updateWebFields, 15), CaseField.FIELD_RESOLVER, wfmStrings.resolver(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(resolverField);
        }
        if (uploadField == null) {
            uploadField = new WebField(getSortOrder(updateWebFields, 16), CaseField.FIELD_ATTACHMENT, wfmStrings.attachments(), WebFormConstants.INPUT_ATTACHMENT, false, false, "", null);
            addWebField(uploadField);
        }
        webFormsService.getCustomFields(ViewName.CrmCase, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null && result.size() > 0) {
                    for (CompanyCustomFieldItem customFieldItem : result) {
                        addCustomField(customFieldItem);
                    }
                }
            }
        });
    }

    @Override
    public void fillFieldsWithDataFromServer() {
        webFormsService.fillDropDowns(null, WebFormConstants.CASE_FORM, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(HashMap<String, SelectItem[]> result) {
                if (result != null && result.size() > 0) {
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_ASSIGNEES)) {
                        assigneeField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_ASSIGNEES));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_CASEORIGINS) && caseOriginField != null) {
                        caseOriginField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_CASEORIGINS));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_STATUSES) && statusField != null) {
                        statusField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_TYPES) && typeField != null) {
                        typeField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_TYPES));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_PRIORITIES) && priorityField != null) {
                        priorityField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_PRIORITIES));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_CASEREASONS) && caseReasonField != null) {
                        caseReasonField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_CASEREASONS));
                    }
                    if (result.containsKey(WebFormConstants.DROPDOWNITEMS_RESOLVERS) && resolverField != null) {
                        resolverField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_RESOLVERS));
                    }
                    setCustomFieldValues(result);
                }
                setDropDownsFilled(true);
            }
        });
    }
}