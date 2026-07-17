package com.edatasite.workforce.gwt.hrms.client.ui.talentprofile;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.EducationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileService;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * User: unni
 * Date: Dec 2, 2009
 * Time: 3:12:24 PM
 */
public class EducationSummaryView extends CustomForm2 implements Constants, Colapse {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final TalentProfileServiceAsync talentProfileService = TalentProfileService.App.get();

    private final Integer objectID;
    private EducationItem item;

    private HTML country, schoolName, degree, fieldOfStudy, startDate, endDate;
    private TextArea2 activitiesAndSocieties, additionalComments;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;

    private final String ensureDebugId = "education_summary_form_";

    public EducationSummaryView(Integer objectID) {
        super("summary", wfmStrings.summaryView());
        this.objectID = objectID;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {

        addRemoveButton().addClickHandler(event -> {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.sureYouWantToDelete());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    talentProfileService.deleteTalentProfileItem(objectID, TalentProfileEnum.EDUCATION, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            if (result) {
                                closeTab();
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.education()), Info.Type.INFO);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, result, EducationSummaryView.this);
                            } else {
                                Info.show(wfmStrings.errorOccuredWhileDeleting(), Info.Type.INFO);
                            }
                        }
                    });
                }
            });
            messageBox.open();
        });

        addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|edit/" + objectID)));
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
        return LayoutRPC.VIEW;
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
                EducationSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void fillFormWithData() {
        Optional.ofNullable(item.getCountry()).ifPresent(countryValue -> country.setHTML(item.getCountry().getName()));
        schoolName.setHTML(Optional.ofNullable(item.getSchoolName()).orElse(""));
        degree.setHTML(item.getDegree() != null ? item.getDegree().getName() : "");
        fieldOfStudy.setHTML(Optional.ofNullable(item.getFieldOfStudy()).orElse(""));

        Optional.ofNullable(item.getStartDate()).ifPresent(startDateValue -> startDate.setHTML(DateUtils.format(item.getStartDate())));
        Optional.ofNullable(item.getEndDate()).ifPresent(startDateValue -> endDate.setHTML(DateUtils.format(item.getEndDate())));
        activitiesAndSocieties.setText(Optional.ofNullable(item.getActivitiesAndSocieties()).orElse(""));
        additionalComments.setText(Optional.ofNullable(item.getComment()).orElse(""));
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
    }

    @Override
    protected void registerFields() {
        country = new HTML();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(ensureDebugId.concat("country"));

        schoolName = new HTML();
        schoolName.addStyleName(DEFAULT_WIDTH);
        schoolName.ensureDebugId(ensureDebugId.concat("school_name"));

        degree = new HTML();
        degree.addStyleName(DEFAULT_WIDTH);
        degree.ensureDebugId(ensureDebugId.concat("degree"));

        fieldOfStudy = new HTML();
        fieldOfStudy.addStyleName(DEFAULT_WIDTH);
        fieldOfStudy.ensureDebugId(ensureDebugId.concat("field_of_study"));

        startDate = new HTML();
        startDate.addStyleName(NORMAL_WIDTH);
        startDate.ensureDebugId(ensureDebugId.concat("start_date"));

        endDate = new HTML();
        endDate.addStyleName(NORMAL_WIDTH);
        endDate.ensureDebugId(ensureDebugId.concat("end_date"));

        activitiesAndSocieties = new TextArea2();
        activitiesAndSocieties.setHeight("12em");
        activitiesAndSocieties.setWidth(MAX_DEFAULT_WIDTH);
        activitiesAndSocieties.setReadOnly(true);
        activitiesAndSocieties.hideCharacterLimitPanel();
        activitiesAndSocieties.addStyleName(DEFAULT_WIDTH);
        activitiesAndSocieties.ensureDebugId(ensureDebugId.concat("activities_and_societies"));

        additionalComments = new TextArea2();
        additionalComments.setHeight("12em");
        additionalComments.setWidth(MAX_DEFAULT_WIDTH);
        additionalComments.setReadOnly(true);
        additionalComments.hideCharacterLimitPanel();
        additionalComments.addStyleName(DEFAULT_WIDTH);
        additionalComments.ensureDebugId(ensureDebugId.concat("additional_comments"));

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.educationDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNTRY_) != null) {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(formPropertyMap.get(CustomFormConstants.COUNTRY_).isChanged() ? formPropertyMap.get(CustomFormConstants.COUNTRY_).getTitle() : wfmStrings.country()));
        } else {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(wfmStrings.country(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SCHOOL_NAME) != null) {
            addField(CustomFormConstants.SCHOOL_NAME, schoolName, getTitle(formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.SCHOOL_NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.SCHOOL_NAME, schoolName, getTitle(wfmStrings.name()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEGREE) != null) {
            addField(CustomFormConstants.DEGREE, degree, getTitle(formPropertyMap.get(CustomFormConstants.DEGREE).isChanged() ? formPropertyMap.get(CustomFormConstants.DEGREE).getTitle() : wfmStrings.degree()));
        } else {
            addField(CustomFormConstants.DEGREE, degree, getTitle(wfmStrings.degree()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY) != null) {
            addField(CustomFormConstants.FIELD_OF_STUDY, fieldOfStudy, getTitle(formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).isChanged() ? formPropertyMap.get(CustomFormConstants.FIELD_OF_STUDY).getTitle() : hrmsStrings.fieldOfStudy()));
        } else {
            addField(CustomFormConstants.FIELD_OF_STUDY, fieldOfStudy, getTitle(hrmsStrings.fieldOfStudy()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, endDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.endDate()));
        } else {
            addField(CustomFormConstants.DUE_DATE, endDate, getTitle(wfmStrings.endDate()));
        }
//        addField(CustomFormConstants.ACTIVITIES, activitiesAndSocieties, getTitle(Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, hrmsStrings.activitiesAndSocieties(), wfmStrings.activities())));
        addField(CustomFormConstants.COMMENTS, additionalComments, getTitle(wfmStrings.description()));

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID, true);

        show();
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