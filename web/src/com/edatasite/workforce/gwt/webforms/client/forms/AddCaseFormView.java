package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class AddCaseFormView extends AbstractAddFormView {

    private WfmForm.Field assigneeField;
    private WfmForm.Field resolverField;
    private WfmForm.Field typeField;
    private WfmForm.Field statusField;
    private WfmForm.Field caseOriginField;
    private WfmForm.Field priorityField;
    private WfmForm.Field caseReasonField;
    private WfmForm.Field otherReasonField;
    private WfmForm.Field subjectField;
    private WfmForm.Field descriptionField;
    private WfmForm.Field reportedByOtherFirstNameField;
    private WfmForm.Field reportedByOtherLastNameField;
    private WfmForm.Field reportedByOtherCompanyField;
    private WfmForm.Field reportedByOtherEmailField;
    private WfmForm.Field reportedByOtherPhoneField;
    private WfmForm.Field reportedByOtherFaxField;
    private WfmForm.Field uploadField;
//    private ChooseCRMItemAndSearch reportedBy;

    private CaseItem item;
    private WebFormItem webFormItem;

    private final String nickDebugId = "add_case_form_view_";

    public AddCaseFormView(WebForm webForm, VerticalPanel antibotPanel) {
        super(webForm, antibotPanel);
    }

    @Override
    protected void setWebFormID(Integer formID) {
        if (item == null) {
            item = new CaseItem();
        }
        if (webFormItem == null) {
            webFormItem = new WebFormItem();
        }
        item.setWebFormID(formID);
    }

    public void addField(final WebField webField) {
        if (!webField.isCustomField()) {
            switch (webField.getSavingField()) {
                case CaseField.FIELD_ASSIGNEE:
                    assigneeField = addFieldToForm(webField);
                    assigneeField.ensureDebugId(this.nickDebugId + "assigneeField");
                    break;
                case CaseField.FIELD_SUBJECT:
                    subjectField = addFieldToForm(webField);
                    subjectField.ensureDebugId(this.nickDebugId + "subjectField");
                    break;
                case CaseField.FIELD_DESCRIPTION:
                    descriptionField = addFieldToForm(webField);
                    descriptionField.ensureDebugId(this.nickDebugId + "descriptionField");
                    break;
                case CaseField.FIELD_CASE_ORIGIN:
                    caseOriginField = addFieldToForm(webField);
                    caseOriginField.ensureDebugId(this.nickDebugId + "caseOriginField");
                    break;
                case CaseField.FIELD_STATUS:
                    statusField = addFieldToForm(webField);
                    statusField.ensureDebugId(this.nickDebugId + "statusField");
                    break;
                case CaseField.FIELD_TYPE:
                    typeField = addFieldToForm(webField);
                    typeField.ensureDebugId(this.nickDebugId + "typeField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME:
                    reportedByOtherFirstNameField = addFieldToForm(webField);
                    reportedByOtherFirstNameField.ensureDebugId(this.nickDebugId + "reportedByOtherLastNameField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME:
                    reportedByOtherLastNameField = addFieldToForm(webField);
                    reportedByOtherLastNameField.ensureDebugId(this.nickDebugId + "reportedByOtherLastNameField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_COMPANY:
                    reportedByOtherCompanyField = addFieldToForm(webField);
                    reportedByOtherCompanyField.ensureDebugId(this.nickDebugId + "reportedByOtherCompanyField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_EMAIL:
                    reportedByOtherEmailField = addFieldToForm(webField);
                    reportedByOtherEmailField.ensureDebugId(this.nickDebugId + "reportedByOtherEmailField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_PHONE:
                    reportedByOtherPhoneField = addFieldToForm(webField);
                    reportedByOtherPhoneField.ensureDebugId(this.nickDebugId + "reportedByOtherPhoneField");
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_FAX:
                    reportedByOtherFaxField = addFieldToForm(webField);
                    reportedByOtherFaxField.ensureDebugId(this.nickDebugId + "reportedByOtherFaxField");
                    break;
//            case CaseField.FIELD_REPORTED_BY:
//                table.addTitleField("Reported By");
//                reportedBy = new ChooseCRMItemAndSearch(table, "case", webForm.getCompanyID());
//                if (webField.getDefaultValue() != null && !"".equals(webField.getDefaultValue())) {
//                    reportedBy.setValues((String) webField.getDefaultValue());
//                }
//                reportedBy.addValueChangeHandler(new Command() {
//                    @Override
//                    public void execute() {
//                        fillItem(webField, reportedBy.getValue(), reportedBy);
//                    }
//                });
//                table.addHorizontalLine();
//                break;
                case CaseField.FIELD_PRIORITY:
                    priorityField = addFieldToForm(webField);
                    priorityField.ensureDebugId(this.nickDebugId + "priorityField");
                    break;
                case CaseField.FIELD_CASE_REASON:
                    caseReasonField = addFieldToForm(webField);
                    caseReasonField.ensureDebugId(this.nickDebugId + "caseReasonField");
                    break;
                case CaseField.FIELD_RESOLVER:
                    resolverField = addFieldToForm(webField);
                    resolverField.ensureDebugId(this.nickDebugId + "resolverField");
                    break;
                case CaseField.FIELD_ATTACHMENT:
                    uploadField = addFieldToForm(webField);
                    uploadField.ensureDebugId(this.nickDebugId + "uploadField");
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

    public void addListeners() {
        if(types != null && types.getSelectedId() != null){
            if (caseReason != null && caseReasons != null && caseReasons.length > 0) {
                Integer caseReasonSelected = caseReason.getSelectedId();
                ArrayList<SelectItem> list = new ArrayList<>();
                for (SelectItem val : caseReasons) {
                    ReferenceItem rItem = (ReferenceItem) val;
                    if (rItem.getRelative() == null || "".equals(rItem.getRelative())) {
                        list.add(val);
                    } else {
                        String[] ids = rItem.getRelative().split(",");
                        for (String id : ids) {
                            if (types.getSelectedId().toString().equals(id)) {
                                list.add(val);
                                break;
                            }
                        }
                    }
                }
                caseReason.setItems(list.toArray(new SelectItem[]{}));
                caseReason.setSelected(caseReasonSelected);
            }
        }
        if (types != null) {
            types.addValueChangeHandler(changeEvent -> {
                if (types.getSelectedId() == null && caseReasons != null) {
                    caseReason.setItems(caseReasons);
                } else {
                    if (caseReasons != null && caseReasons.length > 0) {
                        ArrayList<SelectItem> list = new ArrayList<>();
                        for (SelectItem val : caseReasons) {
                            ReferenceItem rItem = (ReferenceItem) val;
                            if (rItem.getRelative() == null || "".equals(rItem.getRelative())) {
                                list.add(val);
                            } else {
                                String[] ids = rItem.getRelative().split(",");
                                for (String id : ids) {
                                    if (types.getSelectedId().toString().equals(id)) {
                                        list.add(val);
                                        break;
                                    }
                                }
                            }
                        }
                        caseReason.setItems(list.toArray(new SelectItem[]{}));
                    }
                }
            });
        }
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
                case CaseField.FIELD_ASSIGNEE:
                    item.setCaseAssigneeId(getAsSelectItem(value).getId());
                    item.setCaseAssigneeName(getAsSelectItem(value).getName());
                    field = assigneeField;
                    break;
                case CaseField.FIELD_SUBJECT:
                    item.setSubject(getAsString(value));
                    field = subjectField;
                    break;
                case CaseField.FIELD_DESCRIPTION:
                    item.setDescription(getAsString(value));
                    field = descriptionField;
                    break;
                case CaseField.FIELD_CASE_ORIGIN:
                    item.setCaseOrigin(getAsSelectItem(value).getName());
                    item.setCaseOriginId(getAsSelectItem(value).getId());
                    field = caseOriginField;
                    break;
                case CaseField.FIELD_STATUS:
                    item.setStatus(getAsSelectItem(value));
                    field = statusField;
                    break;
                case CaseField.FIELD_TYPE:
                    item.setTypeId(getAsSelectItem(value).getId());
                    item.setType(getAsSelectItem(value).getName());
                    field = typeField;
                    break;
//            case CaseField.FIELD_REPORTED_BY:
//                fillValueOfReportedBy((String) value);
//                break;
                case CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME:
                    item.setFirstName(getAsString(value));
                    field = reportedByOtherFirstNameField;
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME:
                    item.setLastName(getAsString(value));
                    field = reportedByOtherLastNameField;
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_COMPANY:
                    item.setCompany(getAsString(value));
                    field = reportedByOtherCompanyField;
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_EMAIL:
                    item.setEmail(getAsString(value));
                    isEmail = true;
                    field = reportedByOtherEmailField;
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_PHONE:
                    item.setPhone(getAsString(value));
                    field = reportedByOtherPhoneField;
                    break;
                case CaseField.FIELD_REPORTED_BY_OTHER_FAX:
                    item.setFax(getAsString(value));
                    field = reportedByOtherFaxField;
                    break;
                case CaseField.FIELD_PRIORITY:
                    item.setPriorityId(getAsSelectItem(value).getId());
                    item.setPriority(getAsSelectItem(value).getName());
                    field = priorityField;
                    break;
                case CaseField.FIELD_CASE_REASON:
                    item.setCaseReasonId(getAsSelectItem(value).getId());
                    item.setCaseReason(getAsSelectItem(value).getName());
                    field = caseReasonField;
                    break;
                case CaseField.FIELD_RESOLVER:
                    item.setResolverId(getAsSelectItem(value).getId());
                    item.setResolverName(getAsSelectItem(value).getName());
                    field = resolverField;
                    break;
                case LeadField.FIELD_ATTACHMENT:
                    if (value instanceof FileItem || value instanceof FileItem[]) {
                        item.setAttachments((FileItem[]) value);
                    }
                    field = uploadField;
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
            }
        }
    }

//    private void fillValueOfReportedBy(String value) {
//        if (value != null && !"".equals(value)) {
//            String[] items = value.split(ChooseCRMItemAndSearch.DELIMITR);
//            item.removeReportedByAll();
//            if (items[0].equals(ChooseCRMItemAndSearch.OTHER)) {
//                Map<String, String> otherInformation = reportedBy.getOtherReporterInformation();
//                item.setFirstName(items[1].equals("null") ? null : items[1]);
//                item.setLastName(items[2].equals("null") ? null : items[2]);
//                item.setCompany(items[3].equals("null") ? null : items[3]);
//                item.setEmail(items[4].equals("null") ? null : items[4]);
//                item.setPhone(items[5].equals("null") ? null : items[5]);
//                item.setFax(items[6].equals("null") ? null : items[6]);
//            } else {
//                Integer objectID = null;
//                try {
//                    objectID = Integer.parseInt(items[1]);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                if (items[0].equals(ChooseCRMItemAndSearch.LEAD)) {
//                    item.setLeadId(objectID);
//                } else if (items[0].equals(ChooseCRMItemAndSearch.ACCOUNT)) {
//                    item.setAccountId(objectID);
//                } else if (items[0].equals(ChooseCRMItemAndSearch.CONTACT)) {
//                    item.setRelatedToId(objectID);
//                }
//            }
//        }
//    }
//

    private void validate(boolean validate, final WfmForm.Field field, WebField webField, Widget widget, boolean... isEmails) {
        if (validate) {
            validate(field, webField, widget, isEmails);
        }
    }

    public void save(String antibot) {
        LoadingPanel.loading(true);
        webFormItem.setCaseItem(item);
        if (uploadField != null) {
            if (uploadField.getWidgets() != null) {
                if (uploadField.getWidgets()[0] != null && uploadField.getWidgets()[0] instanceof WebFormsFileUpload) {
                    WebFormsFileUpload fileUpload = (WebFormsFileUpload) uploadField.getWidgets()[0];
                    item.setAttachments(fileUpload.getAttachedFiles());
                }
            }
        }
        prepareCustomFields();
        webFormItem.setWebformType(WebFormConstants.CASE_FORM);
        webFormItem.setWebformID(item.getWebFormID());
        webFormItem.setCompanyID(Integer.valueOf(Utils.getEncryptedCompanyID()));
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
                    if (result != null && result.size() > 0) {
                        boolean noError = false;
                        if (result.containsKey("ERROR_CAPTCHA")) {
                            Info.show(result.get("ERROR_CAPTCHA"), Info.Type.WARNING);
                        } else if (result.containsKey("ERROR")) {
                            Info.show(result.get("ERROR"), Info.Type.WARNING);
                        } else if (result.containsKey("ID")) {
                            noError = true;
                        } else if (result.containsKey("ID")) {
                            if (addedSuccessfully != null) {
                                addedSuccessfully.execute();
                            }
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

    private void prepareCustomFields() {
        if (customFields != null && customFields.size() > 0) {
            ArrayList<CompanyCustomFieldItem> customFieldsArray = new ArrayList<>();
            int indicator = 0;
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
