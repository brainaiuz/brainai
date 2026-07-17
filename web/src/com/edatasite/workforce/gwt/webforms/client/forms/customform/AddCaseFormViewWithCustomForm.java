package com.edatasite.workforce.gwt.webforms.client.forms.customform;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.upload.WebFormsFileUpload;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormItem;
import com.edatasite.workforce.gwt.webforms.client.forms.CaseField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebField;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Hayot
 * Date: Aug 12, 2010
 * Time: 5:30:05 PM
 */
public class AddCaseFormViewWithCustomForm extends AbstractAddFormViewWithCustomForm {

    private CaseItem item;
    private WebFormItem webFormItem;
    private Map<String, Widget> widgetsMap;

    public AddCaseFormViewWithCustomForm(WebForm webForm, VerticalPanel antibotPanel) {
        super(webForm, antibotPanel);
        this.webForm = webForm;
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
        widgetsMap = new HashMap<>();
    }

    public void addField(final WebField webField) {
        Widget widget = addFieldToForm(webField);
        if (!webField.isCustomField()) {
            widgetsMap.put("SF_" + webField.getSavingField(), widget);
        }
    }

    @Override
    protected String getFormID() {
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void fillItem(WebField webField, Object value, Widget widget) {
        boolean validate = false;
        if (webField.isMandatory() && webField.isShowInForm()) {
            validate = true;
        }
        boolean isEmail = false;
        switch (webField.getSavingField()) {
            case CaseField.FIELD_ASSIGNEE:
                item.setCaseAssigneeId(getAsSelectItem(value).getId());
                item.setCaseAssigneeName(getAsSelectItem(value).getName());
                break;
            case CaseField.FIELD_SUBJECT:
                item.setSubject(getAsString(value));
                break;
            case CaseField.FIELD_DESCRIPTION:
                item.setDescription(getAsString(value));
                break;
            case CaseField.FIELD_CASE_ORIGIN:
                item.setCaseOrigin(getAsSelectItem(value).getName());
                item.setCaseOriginId(getAsSelectItem(value).getId());
                break;
            case CaseField.FIELD_STATUS:
                item.setStatus(getAsSelectItem(value));
                break;
            case CaseField.FIELD_TYPE:
                item.setTypeId(getAsSelectItem(value).getId());
                item.setType(getAsSelectItem(value).getName());
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME:
                item.setFirstName(getAsString(value));
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME:
                item.setLastName(getAsString(value));
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_COMPANY:
                item.setCompany(getAsString(value));
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_EMAIL:
                item.setEmail(getAsString(value));
                isEmail = true;
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_PHONE:
                item.setPhone(getAsString(value));
                break;
            case CaseField.FIELD_REPORTED_BY_OTHER_FAX:
                item.setFax(getAsString(value));
                break;
            case CaseField.FIELD_PRIORITY:
                item.setPriorityId(getAsSelectItem(value).getId());
                item.setPriority(getAsSelectItem(value).getName());
                break;
            case CaseField.FIELD_CASE_REASON:
                item.setCaseReasonId(getAsSelectItem(value).getId());
                item.setCaseReason(getAsSelectItem(value).getName());
                break;
            case CaseField.FIELD_RESOLVER:
                item.setResolverId(getAsSelectItem(value).getId());
                item.setResolverName(getAsSelectItem(value).getName());
                break;
            case CaseField.FIELD_ATTACHMENT:
                if (value instanceof FileItem || value instanceof FileItem[]) {
                    item.setAttachments((FileItem[]) value);
                }
                if (validate) {
                    validate(webField, widget);
                }
                break;

        }
        validate(validate, webField, widget, isEmail);
    }

    private void validate(boolean validate, WebField webField, Widget widget, boolean... isEmails) {
        if (validate) {
            validate(webField, widget, isEmails);
        }
    }

    public void save(String antibot) {
        LoadingPanel.loading(true);
        if (widgetsMap != null) {
            if (widgetsMap.size() > 0) {
                if (widgetsMap.get("SF_" + CaseField.FIELD_ATTACHMENT) != null && widgetsMap.get("SF_" + CaseField.FIELD_ATTACHMENT) instanceof WebFormsFileUpload) {
                    WebFormsFileUpload fileUpload = (WebFormsFileUpload) widgetsMap.get("SF_" + CaseField.FIELD_ATTACHMENT);
                    item.setAttachments(fileUpload.getAttachedFiles());
                }
            }
        }
        webFormItem.setCaseItem(item);
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
