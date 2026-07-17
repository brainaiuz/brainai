package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
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
 * User: Ilhom
 * Date: 22.11.13
 * Time: 15:12
 */
public class AddCandidateFormView extends AbstractAddFormView implements Constants {


    private WfmForm.Field ownerField;
    private WfmForm.Field firstNameField;
    private WfmForm.Field lastNameField;
    private WfmForm.Field dobField;
    private WfmForm.Field sourceField;
    private WfmForm.Field matchedVacanciesField;
    private WfmForm.Field workExperienceField;
    private WfmForm.Field currentEmployerField;
    private WfmForm.Field expectedSalaryField;
    private WfmForm.Field statusField;
    private WfmForm.Field preferredLocationField;
    private WfmForm.Field skillsField;
    private WfmForm.Field uploadField;

    private ContactListItem item;
    private WebFormItem webFormItem;

    private final String nickIdName = "add_candidate_form_view_";

    public AddCandidateFormView(WebForm webForm, VerticalPanel antibotPanel) {
        super(webForm, antibotPanel);
    }

    @Override
    protected void setWebFormID(Integer formID) {
        if (item == null) {
            item = new ContactListItem();
        }
        if (webFormItem == null) {
            webFormItem = new WebFormItem();
        }
        item.setWebFormID(formID);
    }

    @Override
    public void save(String antibot) {
        LoadingPanel.loading(true);
        prepareCustomFields();
        item.setWebFormID(webForm.getObjectId());
        webFormItem.setContactListItem(item);
        webFormItem.setCompanyID(Integer.valueOf(Utils.getEncryptedCompanyID()));
        webFormItem.setWebformID(item.getWebFormID());
        webFormItem.setWebformType(WebFormConstants.CANDIDATE_FORM);
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

    @Override
    public void setAddedSuccessfully(Command addedSuccessfully) {
        this.addedSuccessfully = addedSuccessfully;
    }

    @Override
    public void setErrorOn(Command errorOn) {
        this.errorOn = errorOn;
    }

    @Override
    protected void fillItem(WebField webField, Object value, Widget widget) {
        boolean validate = false;
        if (webField.isMandatory() && webField.isShowInForm()) {
            validate = true;
        }
        if (!webField.isCustomField()) {
            WfmForm.Field field = null;
            switch (webField.getSavingField()) {
                case CandidateField.FIELD_OWNER:
                    item.setOwnerId(getAsSelectItem(value).getId());
                    item.setOwner(getAsSelectItem(value).getName());
                    field = ownerField;
                    break;
                case CandidateField.FIELD_FIRST_NAME:
                    item.setFirstName(getAsString(value));
                    field = firstNameField;
                    break;
                case CandidateField.FIELD_LAST_NAME:
                    item.setLastName(getAsString(value));
                    field = lastNameField;
                    break;
                case CandidateField.FIELD_DOB:
                    item.setBirthDate(getNonConvertable(value));
                    field = dobField;
                    break;
                case CandidateField.FIELD_SOURCE:
                    item.setCandidateSource(getAsSelectItem(value));
                    field = sourceField;
                    break;
                case CandidateField.FIELD_MATCHED_VACANCIES:
                    item.setVacancies(getAsSelectItemArray(value));
                    field = matchedVacanciesField;
                    break;
                case CandidateField.FIELD_WORK_EXPERIENCE:
                    if (value != null && getAsString(value) != null) {
                        item.setWorkExperience(Integer.valueOf(getAsString(value)));
                    }
                    field = workExperienceField;
                    break;
                case CandidateField.FIELD_CURRENT_EMPLOYER:
                    item.setCurrentEmployer(getAsString(value));
                    field = currentEmployerField;
                    break;
                case CandidateField.FIELD_EXPECTED_SALARY:
                    item.setExpectedSalary(getStringAsDouble(value));
                    field = expectedSalaryField;
                    break;
                case CandidateField.FIELD_STATUS:
                    item.setCandidateStatus(getAsSelectItem(value));
                    field = statusField;
                    break;
                case CandidateField.FIELD_PREFERRED_LOCATION:
                    item.setPreferredLocation(getAsSelectItem(value));
                    field = preferredLocationField;
                    break;
                case CandidateField.FIELD_SKILLS:
                    item.setSkills(getAsString(value));
                    field = skillsField;
                    break;
                case CandidateField.FIELD_ATTACHMENT:
                    if (value instanceof FileItem || value instanceof FileItem[]) {
                        item.setAttachments((FileItem[]) value);
                    }
                    field = uploadField;
                    break;
            }
            validate(validate, field, webField, widget);
        } else {
            if (customFields != null && customFields.containsKey(webField.getSavingField())) {
                List fieldCustomFieldAndWidgets = customFields.get(webField.getSavingField());
                WfmForm.Field wfmField = (WfmForm.Field) fieldCustomFieldAndWidgets.get(0);
                CompanyCustomFieldItem customFieldItem = (CompanyCustomFieldItem) fieldCustomFieldAndWidgets.get(1);
                Widget[] widgets = (Widget[]) fieldCustomFieldAndWidgets.get(2);
                setCustomFieldValues(wfmField, webField, customFieldItem, widgets);
                fieldCustomFieldAndWidgets = new ArrayList();
                fieldCustomFieldAndWidgets.add(wfmField);
                fieldCustomFieldAndWidgets.add(customFieldItem);
                fieldCustomFieldAndWidgets.add(widgets);
                customFields.remove(webField.getSavingField());
                customFields.put(webField.getSavingField(), fieldCustomFieldAndWidgets);
            }
        }
    }

    @Override
    protected void addField(WebField webField) {
        if (!webField.isCustomField()) {
            switch (webField.getSavingField()) {
                case CandidateField.FIELD_OWNER:
                    ownerField = addFieldToForm(webField);
                    ownerField.ensureDebugId(nickIdName + "ownerField");
                    break;
                case CandidateField.FIELD_FIRST_NAME:
                    firstNameField = addFieldToForm(webField);
                    firstNameField.ensureDebugId(nickIdName + "firstNameField");
                    break;
                case CandidateField.FIELD_LAST_NAME:
                    lastNameField = addFieldToForm(webField);
                    lastNameField.ensureDebugId(nickIdName + "lastNameField");
                    break;
                case CandidateField.FIELD_DOB:
                    dobField = addFieldToForm(webField);
                    dobField.ensureDebugId(nickIdName + "dobField");
                    break;
                case CandidateField.FIELD_SOURCE:
                    sourceField = addFieldToForm(webField);
                    sourceField.ensureDebugId(nickIdName + "sourceField");
                    break;
                case CandidateField.FIELD_MATCHED_VACANCIES:
                    matchedVacanciesField = addFieldToForm(webField);
                    matchedVacanciesField.ensureDebugId(nickIdName + "matchedVacanciesField");
                    break;
                case CandidateField.FIELD_WORK_EXPERIENCE:
                    workExperienceField = addFieldToForm(webField);
                    workExperienceField.ensureDebugId(nickIdName + "workExperienceField");
                    break;
                case CandidateField.FIELD_CURRENT_EMPLOYER:
                    currentEmployerField = addFieldToForm(webField);
                    currentEmployerField.ensureDebugId(nickIdName + "currentEmployerField");
                    break;
                case CandidateField.FIELD_EXPECTED_SALARY:
                    expectedSalaryField = addFieldToForm(webField);
                    expectedSalaryField.ensureDebugId(nickIdName + "expectedSalaryField");
                    break;
                case CandidateField.FIELD_STATUS:
                    statusField = addFieldToForm(webField);
                    statusField.ensureDebugId(nickIdName + "statusField");
                    break;
                case CandidateField.FIELD_PREFERRED_LOCATION:
                    preferredLocationField = addFieldToForm(webField);
                    preferredLocationField.ensureDebugId(nickIdName + "preferredLocationField");
                    break;
                case CandidateField.FIELD_SKILLS:
                    skillsField = addFieldToForm(webField);
                    skillsField.ensureDebugId(nickIdName + "skillsField");
                    break;
                case CandidateField.FIELD_ATTACHMENT:
                    uploadField = addFieldToForm(webField);
                    uploadField.ensureDebugId(this.nickIdName + "uploadField");
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

    private void validate(boolean validate, final WfmForm.Field field, WebField webField, Widget widget) {
        if (validate) {
            validate(field, webField, widget);
        }
    }
}