package com.edatasite.workforce.gwt.hrms.client.ui.talentprofile;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.EducationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileService;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:12:09 PM
 */
public class EducationAddView extends CustomForm2 implements Constants, Colapse {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final TalentProfileServiceAsync talentProfileService = TalentProfileService.App.get();

    private Integer employeeID;
    private Integer objectID;
    private EducationItem item;

    private DatePicker startDate;
    private DatePicker endDate;
    private CountryLookUp country;
    private DataListBox degree;
    private TextArea2 activitiesAndSociety;
    private TextArea2 comment;
    private TextBox fieldOfStudy;
    private TextBox schoolName;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;


    protected String ensureDebugId = "education_add_edit_form_";
    private boolean isFromCandidate;

    public EducationAddView(Integer employeeID,boolean isFromCandidate) {
        super("add", hrmsStrings.addEducation());
        this.employeeID = employeeID;
        this.isFromCandidate = isFromCandidate;
        item = new EducationItem();
    }

    public EducationAddView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        addButton(objectID == null ? wfmStrings.save() : wfmStrings.update(), BTN_PRIMARY, null, ensureDebugId.concat("save_and_close_button"), event -> save());
    }

    @Override
    protected void getDataToFillFields() {
            LoadingPanel.loading(true);
            talentProfileService.getEducation(objectID, new AbstractAsyncCallback<EducationItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(EducationItem object) {
                    LoadingPanel.loading(false);
                    item = object;
                    fillFormWithData();
                }
            });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.EDUCATION_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.TalentProfileView, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                EducationAddView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    protected void fillFormWithData() {
        Optional.ofNullable(item.getCountry()).ifPresent(selectedCountry -> country.setSelected(selectedCountry));
        schoolName.setText(Optional.ofNullable(item.getSchoolName()).orElse(""));
        degree.setItems(item.getDegrees());
        degree.setSelected(item.getDegree() != null ? item.getDegree().getObjectID() : null);
        fieldOfStudy.setText(Optional.ofNullable(item.getFieldOfStudy()).orElse(""));
        Optional.ofNullable(item.getStartDate()).ifPresent(startDateValue -> startDate.setDate(startDateValue.getNonConvertedDate()));
        Optional.ofNullable(item.getEndDate()).ifPresent(endDateValue -> endDate.setDate(endDateValue.getNonConvertedDate()));
        activitiesAndSociety.setText(Optional.ofNullable(item.getActivitiesAndSocieties()).orElse(""));
        comment.setText(Optional.ofNullable(item.getComment()).orElse(""));
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());
    }

    @Override
    protected void registerFields() {
        country = new CountryLookUp();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(ensureDebugId.concat("country"));

        schoolName = new TextBox();
        schoolName.addStyleName(DEFAULT_WIDTH);
        schoolName.ensureDebugId(ensureDebugId.concat("school_name"));

        degree = new DataListBox();
        degree.addStyleName(DEFAULT_WIDTH);
        degree.ensureDebugId(ensureDebugId.concat("degree"));

        fieldOfStudy = new TextBox();
        fieldOfStudy.addStyleName(DEFAULT_WIDTH);
        fieldOfStudy.ensureDebugId(ensureDebugId.concat("field_of_study"));

        startDate = new DatePicker();
        startDate.addStyleName(DEFAULT_WIDTH);
        //startDate.setWidth(NORMAL_WIDTH);
        startDate.ensureDebugId(ensureDebugId.concat("start_date"));

        endDate = new DatePicker();
        endDate.addStyleName(DEFAULT_WIDTH);
        //endDate.setWidth(NORMAL_WIDTH);
        endDate.ensureDebugId(ensureDebugId.concat("end_date"));

        activitiesAndSociety = new TextArea2(Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, hrmsStrings.activitiesAndSocieties(), wfmStrings.activities()));
        activitiesAndSociety.setHeight("12em");
        activitiesAndSociety.setWidth(MAX_DEFAULT_WIDTH);
        activitiesAndSociety.addStyleName(DEFAULT_WIDTH);
        activitiesAndSociety.ensureDebugId(ensureDebugId.concat("activities_and_society"));

        comment = new TextArea2(wfmStrings.description());
        comment.setHeight("12em");
        comment.setWidth(MAX_DEFAULT_WIDTH);
        comment.addStyleName(DEFAULT_WIDTH);
        comment.ensureDebugId(ensureDebugId.concat("additional_comments"));

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.educationDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY_) != null) {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(formPropertyMap.get(CustomFormConstants.COUNTRY_).isChanged() ? formPropertyMap.get(CustomFormConstants.COUNTRY_).getTitle() : wfmStrings.country(),
                    formPropertyMap.get(CustomFormConstants.COUNTRY_).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.COUNTRY_).isInformation());
            country.setEnabled(!formPropertyMap.get(CustomFormConstants.COUNTRY_).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.COUNTRY_).isInformation()){
                new KpiToolTip(country,formPropertyMap.get(CustomFormConstants.COUNTRY_).getInformationText());
            }
        } else {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(wfmStrings.country()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME) != null) {
            addField(CustomFormConstants.SCHOOL_NAME, schoolName, getTitle(formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getTitle() : wfmStrings.name(),
                    formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isInformation());
            schoolName.setEnabled(!formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isInformation()){
                new KpiToolTip(schoolName,formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getInformationText());
            }

        } else {
            addField(CustomFormConstants.SCHOOL_NAME, schoolName, getTitle(wfmStrings.name(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEGREE) != null) {
            addField(CustomFormConstants.DEGREE, degree, getTitle(formPropertyMap.get(CustomFormConstants.DEGREE).isChanged() ? formPropertyMap.get(CustomFormConstants.DEGREE).getTitle() : wfmStrings.degree(),
                    formPropertyMap.get(CustomFormConstants.DEGREE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DEGREE).isInformation());
            degree.setEnabled(!formPropertyMap.get(CustomFormConstants.DEGREE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DEGREE).isInformation()){
                new KpiToolTip(degree,formPropertyMap.get(CustomFormConstants.DEGREE).getInformationText());
            }

        } else {
            addField(CustomFormConstants.DEGREE, degree, getTitle(wfmStrings.degree(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY) != null) {
            addField(CustomFormConstants.FIELD_OF_STUDY, fieldOfStudy, getTitle(formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isChanged() ? formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getTitle() : hrmsStrings.fieldOfStudy(),
                    formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isInformation());
            fieldOfStudy.setEnabled(!formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isInformation()){
                new KpiToolTip(fieldOfStudy,formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getInformationText());
            }

        } else {
            addField(CustomFormConstants.FIELD_OF_STUDY, fieldOfStudy, getTitle(hrmsStrings.fieldOfStudy()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : hrmsStrings.issueDate(),
                    formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.START_DATE).isInformation());
            startDate.setEnabled(!formPropertyMap.get(CustomFormConstants.START_DATE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.START_DATE).isInformation()){
                new KpiToolTip(startDate,formPropertyMap.get(CustomFormConstants.START_DATE).getInformationText());
            }

        } else {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(hrmsStrings.issueDate()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, endDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.expiryDate(),
                    formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation());
            endDate.setEnabled(!formPropertyMap.get(CustomFormConstants.DUE_DATE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation()){
                new KpiToolTip(endDate,formPropertyMap.get(CustomFormConstants.DUE_DATE).getInformationText());
            }

        } else {
            addField(CustomFormConstants.DUE_DATE, endDate, getTitle(wfmStrings.expiryDate()));
        }
//        addField(CustomFormConstants.ACTIVITIES, activitiesAndSociety, null);
        addField(CustomFormConstants.COMMENTS, comment, null);

        getCustomFieldUtil().drawCustomFields(this, objectID, false);

        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
        show();
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        talentProfileService.saveEducation(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void o) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.education()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, o, EducationAddView.this);
                closeTab();
                if (objectID != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase() + "|summary/" + objectID);
                }
            }
        });
    }

    private void setValues() {
        item.setObjectId(objectID);
        if (employeeID != null) {
            item.setEmployeeId(employeeID);
        }
        if (degree.getSelectedItem() != null) {
            item.setDegree(new ReferenceItem(degree.getSelectedItem().getId(), degree.getSelectedItem().getName()));
        }
        item.setSchoolName(schoolName.getText());
        if (startDate.getDate() != null) {
            item.setStartDate(new DateNonConvertable(startDate.getDate()));
        } else {
            item.setStartDate(null);
        }
        if (endDate.getDate() != null) {
            item.setEndDate(new DateNonConvertable(endDate.getDate()));
        } else {
            item.setEndDate(null);
        }
        item.setFromCandidate(isFromCandidate);
        item.setFieldOfStudy(fieldOfStudy.getText());
        item.setActivitiesAndSocieties(activitiesAndSociety.getText());
        item.setComment(comment.getText());
        item.setCountry(country.getSelectedItem());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;

        errors += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEGREE) != null && formPropertyMap.get(CustomFormConstants.DEGREE).isRequired()) {
            errors += markAsError(CustomFormConstants.DEGREE, degree, !Validation.validateDataListBoxRequired(degree));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME) != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.SCHOOL_NAME, schoolName, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getTitle() : wfmStrings.name(), schoolName, formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY) != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isRequired()) {
            errors += markAsError(CustomFormConstants.FIELD_OF_STUDY, fieldOfStudy, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getTitle() : hrmsStrings.fieldOfStudy(), fieldOfStudy, formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.START_DATE, startDate, !Validation.validateDate(startDate));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.DUE_DATE, endDate, !Validation.validateDate(endDate));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY_) != null && formPropertyMap.get(CustomFormConstants.COUNTRY_).isRequired()) {
            errors += markAsError(CustomFormConstants.COUNTRY_, country, !Validation.validateLookUpRequired(country));
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEGREE) != null && formPropertyMap.get(CustomFormConstants.DEGREE).getDefaultValue() != null) {
            degree.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEGREE).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEGREE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME) != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getDefaultValue() != null) {
            schoolName.setText(formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY) != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getDefaultValue() != null) {
            fieldOfStudy.setText(formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                startDate.setDate(currentDate);
            } else {
                try {
                    startDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                endDate.setDate(currentDate);
            } else {
                try {
                    endDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY_) != null && formPropertyMap.get(CustomFormConstants.COUNTRY_).getDefaultValue() != null) {
            country.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.COUNTRY_).getSelectedId(), formPropertyMap.get(CustomFormConstants.COUNTRY_).getDefaultValue()));
        }
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}