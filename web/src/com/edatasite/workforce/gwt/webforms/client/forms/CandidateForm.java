package com.edatasite.workforce.gwt.webforms.client.forms;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.webforms.client.WebFormConstants;
import com.edatasite.workforce.gwt.webforms.client.WebFormsService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Ilhom
 * Date: 20.11.13
 * Time: 17:16
 */
public class CandidateForm extends Form implements CandidateField {

    WebField ownerField;
    WebField firstNameField;
    WebField lastNameField;
    WebField dobField;
    WebField sourceField;
    WebField matchedVacanciesField;
    WebField workExperienceField;
    WebField currentEmployerField;
    WebField expectedSalaryField;
    WebField statusField;
    WebField preferredLocationField;
    WebField skillsField;
    WebField uploadField;


    public CandidateForm(Boolean updateWebFields, WebField... fields) {
        if (fields != null && fields.length > 0) {
            setWebFields(fields);
            for (WebField webField : fields) {
                if (!webField.isCustomField()) {
                    switch (webField.getSavingField()) {
                        case CandidateField.FIELD_OWNER:
                            ownerField = webField;
                            break;
                        case CandidateField.FIELD_FIRST_NAME:
                            firstNameField = webField;
                            break;
                        case CandidateField.FIELD_LAST_NAME:
                            lastNameField = webField;
                            break;
                        case CandidateField.FIELD_DOB:
                            dobField = webField;
                            break;
                        case CandidateField.FIELD_SOURCE:
                            sourceField = webField;
                            break;
                        case CandidateField.FIELD_MATCHED_VACANCIES:
                            matchedVacanciesField = webField;
                            break;
                        case CandidateField.FIELD_WORK_EXPERIENCE:
                            workExperienceField = webField;
                            break;
                        case CandidateField.FIELD_CURRENT_EMPLOYER:
                            currentEmployerField = webField;
                            break;
                        case CandidateField.FIELD_EXPECTED_SALARY:
                            expectedSalaryField = webField;
                            break;
                        case CandidateField.FIELD_STATUS:
                            statusField = webField;
                            break;
                        case CandidateField.FIELD_PREFERRED_LOCATION:
                            preferredLocationField = webField;
                            break;
                        case CandidateField.FIELD_SKILLS:
                            skillsField = webField;
                            break;
                        case CandidateField.FIELD_ATTACHMENT:
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

    @Override
    public void init() {
        int sortOrder = 1;
        if (ownerField == null) {
            ownerField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_OWNER, wfmStrings.owner(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(ownerField);
        }
        if (firstNameField == null) {
            firstNameField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_FIRST_NAME, wfmStrings.firstName(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null);
            addWebField(firstNameField);
        }
        if (lastNameField == null) {
            lastNameField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_LAST_NAME, wfmStrings.lastName(), WebFormConstants.INPUT_TEXTBOX, true, false, "", null);
            addWebField(lastNameField);
        }
        if (dobField == null) {
            dobField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_DOB, wfmStrings.dateOfBirth(), WebFormConstants.INPUT_DOB, false, false, "", null);
            addWebField(dobField);
        }
        if (sourceField == null) {
            sourceField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_SOURCE, wfmStrings.source(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(sourceField);
        }
        if (matchedVacanciesField == null) {
            matchedVacanciesField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_MATCHED_VACANCIES, wfmStrings.matchedVacancies(), WebFormConstants.INPUT_VACANCIES, false, false, "", null);
            addWebField(matchedVacanciesField);
        }
        if (workExperienceField == null) {
            workExperienceField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_WORK_EXPERIENCE, wfmStrings.workExperience(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(workExperienceField);
        }
        if (currentEmployerField == null) {
            currentEmployerField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_CURRENT_EMPLOYER, wfmStrings.currentEmployer(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(currentEmployerField);
        }
        if (expectedSalaryField == null) {
            expectedSalaryField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_EXPECTED_SALARY, wfmStrings.expectedSalary(), WebFormConstants.INPUT_TEXTBOX, false, false, "", null);
            addWebField(expectedSalaryField);
        }
        if (statusField == null) {
            statusField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_STATUS, wfmStrings.status(), WebFormConstants.INPUT_DROPDOWN, true, false, "", null);
            addWebField(statusField);
        }
        if (preferredLocationField == null) {
            preferredLocationField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_PREFERRED_LOCATION, wfmStrings.location(), WebFormConstants.INPUT_DROPDOWN, false, false, "", null);
            addWebField(preferredLocationField);
        }
        if (skillsField == null) {
            skillsField = new WebField(getSortOrder(updateWebFields, sortOrder++), CandidateField.FIELD_SKILLS, wfmStrings.skills(), WebFormConstants.INPUT_TEXTAREA2, false, false, "", null);
            addWebField(skillsField);
        }
        if (uploadField == null) {
            uploadField = new WebField(getSortOrder(updateWebFields, sortOrder), CandidateField.FIELD_ATTACHMENT, wfmStrings.attachments(), WebFormConstants.INPUT_ATTACHMENT, false, false, "", null);
            addWebField(uploadField);
        }
        WebFormsService.App.get().getCustomFields(ViewName.Candidate, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
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
        WebFormsService.App.get().fillDropDowns(null, WebFormConstants.CANDIDATE_FORM, new AbstractAsyncCallback<HashMap<String, SelectItem[]>>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(HashMap<String, SelectItem[]> result) {
                if (result != null && result.size() > 0) {
                    if (ownerField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_OWNERS)) {
                        ownerField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_OWNERS));
                    }
                    if (sourceField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_SOURCES)) {
                        sourceField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_SOURCES));
                    }
                    if (statusField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_STATUSES)) {
                        statusField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_STATUSES));
                    }
                    if (preferredLocationField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_LOCATIONS)) {
                        preferredLocationField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_LOCATIONS));
                    }
                    if (matchedVacanciesField != null && result.containsKey(WebFormConstants.DROPDOWNITEMS_VACANCIES)) {
                        matchedVacanciesField.setValues(result.get(WebFormConstants.DROPDOWNITEMS_VACANCIES));
                    }
                    setCustomFieldValues(result);
                }
                setDropDownsFilled(true);
            }
        });
    }
}