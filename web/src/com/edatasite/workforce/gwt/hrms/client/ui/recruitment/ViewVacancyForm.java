package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NotesWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.VACANCY.VACANCY_QUESTIONS;

public class ViewVacancyForm extends AddVacancyView implements NoColapse {
    private final Integer objectID;
    MaterialLink deleteButton;
    private KpiEditor description, responsibilities, jobRequirement;
    private NotesWidget notesPanel;
    private Anchor positionName, locationName, department;
    private HTML number, managerName, jobTitle, startDate,
            endDate, statusName, plannedPlaceCount, jobType, jobFamily, requiredDegree, matchedCandidates,
            vacancyType, genderTable, proposedSalary, project, approver, currency;
    private WfmButton2 rejectButton, editButton;
    private FlexTable languagesWidget;
    private VacancyQuestionsWidget vacancyQuestionsWidget;


    public ViewVacancyForm(Integer objectID) {
        super("summary", wfmStrings.summaryView(), objectID);
        this.objectID = objectID;
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
        boolean hasAdminRole = Utils.hasRole(Constants.ADMIN);
        boolean hasDeletePermission = Utils.hasPermission(PermissionConstants.HRMS_DELETE_VACANCY);
        if (hasAdminRole || hasDeletePermission) {
            MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
            if (hasAdminRole) {
                MaterialLink customize = new MaterialLink(wfmStrings.customize());
                customize.addClickHandler(click -> {
                    String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
                });
                options.add(customize);
            }

            //Delete Vacancy
            if (hasDeletePermission) {
                deleteButton = new MaterialLink(wfmStrings.delete());
                deleteButton.addClickHandler(event -> {
                    //register delete vacancy logic
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    wfmMessageBox.setTitle(wfmStrings.warning());
                    wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            RecruitmentService.App.get().deleteVacancy(objectID, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    super.onFailure(caught);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    super.onSuccess(result);
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.vacancy()));
                                    closeTab();
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_DELETE, result, ViewVacancyForm.this);
                                }
                            });
                        }
                    });
                    wfmMessageBox.open();
                });
                options.add(deleteButton);
            }
        }
        printPdfSplitButton = new SplitButton(100, BTN_DEFAULT_OUTLINE);
        addRightButton(printPdfSplitButton);

        //Edit Vacancy
        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_VACANCY)) {
            editButton = addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|editVacancy/" + objectID, item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getJobTitle(), item.getJobTitle()));
        }

        if (Utils.hasPermission(PermissionConstants.VACANCY_LINKS)) {
            createLinkButton();
        }

        //Reject Button
        rejectButton = addButton(wfmStrings.reject(), WfmButton2.BTN_REJECT, clickEvent -> {
//            setButtonsEnabled(false);
            notesPanel.setNoteListener(() -> updateStatus(Constants.VACANCY_APPROVAL_STATUS_REJECTED));
            notesPanel.setCloseListener(() -> setButtonsEnabled(true));
            notesPanel.noteShell();
        });
        rejectButton.setVisible(false);
        //Approve Button
        approve = addButton(wfmStrings.approve(), WfmButton2.BTN_SUCCESS, clickEvent -> updateStatus(Constants.VACANCY_APPROVAL_STATUS_APPROVED));
        approve.setVisible(false);

    }

    //update action
    public void updateStatus(String statusCode) {
        LoadingPanel.loading(true);
        String note = "";
        if (Constants.VACANCY_APPROVAL_STATUS_REJECTED.equals(statusCode)) {
            if (notesPanel.getLastHistoryItem() != null && notesPanel.getLastHistoryItem().getComment() != null
                    && !"".equals(notesPanel.getLastHistoryItem().getComment().trim())) {
                note = notesPanel.getLastHistoryItem().getComment();
            }
        }
        RecruitmentService.App.get().updateVacancyStatus(objectID, statusCode, note, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                LoadingPanel.loading(false);
                setButtonsEnabled(true);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VACANCY_REJECTED_OR_APROVED, null, ViewVacancyForm.this);
                closeTab();

            }
        });
    }

    //activate buttons
    private void setButtonsEnabled(boolean enabled) {
        printPdfSplitButton.setEnabled(enabled);
        if (deleteButton != null) {
            deleteButton.setEnabled(enabled);
        }
        if (editButton != null) {
            editButton.setEnabled(enabled);
        }
        approve.setEnabled(enabled);
        rejectButton.setEnabled(enabled);
    }

    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    public String getIconStyle() {
        return super.getIconStyle();
    }

    public void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.VACANCY_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {
                        CustomFormItemGrid itemView = new CustomFormItemGrid(objectID, configMap.getKey(), LayoutRPC.VACANCY_FORM, configMap.getValue(), 1000);
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    @Override
    protected void fillFormWithData() {

        //Number
        number.setHTML(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");

        //Manager
        managerName.setHTML(item.getManager() != null ? item.getManager().getName() : "");

        //Position name
        positionName.setHTML(item.getPositionItem() != null ? item.getPositionItem().getName() : "");

        //Job type
        jobType.setHTML(item.getJobType() != null ? item.getJobType().getName() : "");

        //job family
        jobFamily.setHTML(item.getJobfamily() != null ? item.getJobfamily().getName() : "");

        //Location
        locationName.setHTML(item.getLocation() != null ? item.getLocation().getName() : "");

        //Job name
        String userLanguage = Utils.getUserLanguage();
        jobTitle.setHTML(item.getJobTitle() != null ? item.getJobTitle() : "");

        //Department
        department.setHTML(item.getDepartment() != null ? item.getDepartment().getName() : "");

        //Description
        description.setData(item.getDescription() != null ? item.getDescription() : "");
        if (mapHasValueForLang(item.getDescriptionLocalize(), userLanguage)) {
            description.setData(item.getDescriptionLocalize().get(userLanguage));
        }
        //Gender Table
        genderTable.setHTML(item.getGender() != null ? item.getGender() : "");

        //Proposed Salary
        if (item.getProposedSalary() != null && !item.getProposedSalary().equals("")) {
            double parsePrice = Utils.universalParse(Utils.getNumberFormat(), item.getProposedSalary());
            proposedSalary.setHTML(Utils.getNumberFormat().format(parsePrice).replace(".00", ""));
        } else {
            proposedSalary.setHTML("");
        }

        //job Requirements
        jobRequirement.setData(item.getJobRequirements() != null ? item.getJobRequirements() : "");
        if (mapHasValueForLang(item.getJobRequirementLocalize(), userLanguage)) {
            jobRequirement.setData(item.getJobRequirementLocalize().get(userLanguage));
        }
        project.setHTML(item.getProjectName() != null ? item.getProjectName() : "");

        //Matched candidates
        if (item.getMatchedCandidates() != null) {
            matchedCandidates.setHTML(CRMUtils.getSelectItemsAsCommaDelimeted(item.getMatchedCandidates().toArray(new SelectItem[]{}), false));
        }

        //Start date
        startDate.setHTML((item.getStartDate() != null ? DateUtils.format(item.getStartDate()) + Utils.getHijriDate(item.getStartDate()) : "") + " - " + (item.getEndDate() != null ? DateUtils.format(item.getEndDate()) + Utils.getHijriDate(item.getEndDate()) : ""));

        //Status
        statusName.setHTML(item.getStatus() != null ? item.getStatus().getName() : "");

        //Vacancy Type
        vacancyType.setHTML(item.getVacancyTypeName() != null ? item.getVacancyTypeName() : "");

        //vacant place planned count
        plannedPlaceCount.setHTML(item.getVacantPlaces() != null ? String.valueOf(item.getVacantPlaces()) : "");

        //Responsibilities
        responsibilities.setData(item.getResponsibility() != null ? item.getResponsibility() : "");
        if (mapHasValueForLang(item.getResponsibilitiesLocalize(), userLanguage)) {
            responsibilities.setData(item.getResponsibilitiesLocalize().get(userLanguage));
        }
        currency.setHTML(item.getCurrency() != null ? item.getCurrency().getName() : "");

        //Required Degree
        requiredDegree.setHTML(item.getRequiredDegree() != null ? item.getRequiredDegree().getName() : "");
        if (item.getCurrentApprover() != null && item.getCurrentApprover().getExactEmployee() != null) {
            approver.setText(item.getCurrentApprover().getExactEmployee().getName());
        }
        drawFooter();
        boolean isApprove = false;
        if (item.getCurrentApproverEmployeeID() != null) {
            isApprove = Utils.isAdmin() || Utils.getUserID().equals(item.getCurrentApproverEmployeeID());
        }
        if ((VACANCY_APPROVAL_STATUS_DRAFT.equals(item.getApprovalStatusCode()) || VACANCY_APPROVAL_STATUS_SUBMITTED.equals(item.getApprovalStatusCode())) && isApprove) {
            approve.setVisible(true);
            rejectButton.setVisible(true);

        } else {
            approve.setVisible(false);
            rejectButton.setVisible(false);
        }

        if (item.getSpokenLanguages() != null) {
            item.getSpokenLanguages().forEach(sl -> {
                int index = languagesWidget.getRowCount();
                languagesWidget.setHTML(index, 0, sl.getLanguage() != null ? sl.getLanguage().getName() : "");
                languagesWidget.setHTML(index, 1, sl.getLevel() != null ? sl.getLevel().getName() : "");
            });
        }
        vacancyQuestionsWidget.setData(item.getVacancyQiestionItems());
        addRelationBadgeCount();
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems(), true);
    }

    private void drawFooter() {
        NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectID == null) {
                return;
            }
            RecruitmentService.App.get().loadVacancyHistory(objectID, callback);
        });
        if (objectID != null) {
            noteHistoryWidget.setSaveIntoDatabase(historyItem -> {
                if (historyItem != null) {
                    LoadingPanel.loading(true);
                    RecruitmentService.App.get().createVacancyHistory(objectID, historyItem, new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Integer hisItemId) {
                            historyItem.setObjectID(hisItemId);
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });

            noteHistoryWidget.setRemoveFromDatabase((hisItem) -> {
                if (hisItem != null && hisItem.getObjectID() != null) {
                    LoadingPanel.loading(true);
                    RecruitmentService.App.get().deleteVacancyComment(hisItem.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            });
        }

        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");
        footer.addToLeftSide(informer);
    }

    @Override
    protected void registerFields() {
        String test_code_ID_name = "vacancy_summary_view_"; //for testing

        //Number
        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);
        number.getElement().setId(test_code_ID_name + "number");

        //Manager
        managerName = new HTML();
        managerName.addStyleName(DEFAULT_WIDTH);
        managerName.getElement().setId(test_code_ID_name + "manager");

        //Position
        positionName = new Anchor(wfmStrings.notAvailable());
        positionName.addStyleName(DEFAULT_WIDTH);
        positionName.getElement().setId(test_code_ID_name + "position");

        positionName.addClickHandler(event -> {
            if (Utils.hasPermission(HRMS_POSITION_SUMMARRY)) {
                if (item.getPositionItem() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("positionsummary|positionsummaryview/" + item.getPositionItem().getObjectID(), item.getPositionItem().getName());
                } else {
                    Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.position());
                }

            } else {
                Info.warn(wfmStrings.youDontHavePermission());
            }
        });

        //Location
        locationName = new Anchor(wfmStrings.notAvailable());
        locationName.getElement().setId(test_code_ID_name + "location");
        locationName.addStyleName(DEFAULT_WIDTH);

        locationName.addClickHandler(event -> {
            if (Utils.hasPermission(HRMS_LOCATION)) {
                if (item.getLocation() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("location|summary/" + item.getLocation().getId().toString(), item.getLocation().getName());
                } else {
                    Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.location());
                }

            } else {
                Info.warn(wfmStrings.youDontHavePermission());
            }
        });


        //Job title
        jobTitle = new HTML();
        jobTitle.addStyleName("file--ViewVacancyForm");
        jobTitle.getElement().setId(test_code_ID_name + "job_title");

        //Department
        department = new Anchor(wfmStrings.notAvailable());
        department.addStyleName(DEFAULT_WIDTH);
        department.getElement().setId(test_code_ID_name + "department");

        department.addClickHandler(event -> {
            if (Utils.hasPermission(HRMS_DEPARTMENT_SUMMARY_VIEW)) {
                if (item.getDepartment() != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + item.getObjectID(), item.getDepartment().getName());
                } else {
                    Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.department());
                }

            } else {
                Info.warn(wfmStrings.youDontHavePermission());
            }
        });

        vacancyQuestionsWidget = new VacancyQuestionsWidget(true);
        vacancyQuestionsWidget.getElement().setId("vacancy_questions");


        description = createEditor("description");
        jobRequirement = createEditor("jobRequirement");
        responsibilities = createEditor("responsibilities");

        //Matched candidates
        matchedCandidates = new HTML();
        matchedCandidates.addStyleName(DEFAULT_WIDTH);
        matchedCandidates.getElement().setId(test_code_ID_name + "matched_candidates");

        //Start date
        startDate = new HTML();
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.getElement().setId(test_code_ID_name + "start_date");

        //End date
        endDate = new HTML();
        endDate.addStyleName(DEFAULT_WIDTH);
        endDate.getElement().setId(test_code_ID_name + "end_date");

        //Status
        statusName = new HTML();
        statusName.addStyleName(DEFAULT_WIDTH);
        statusName.getElement().setId(test_code_ID_name + "status");

        //VacancyType
        vacancyType = new HTML();
        vacancyType.addStyleName(DEFAULT_WIDTH);
        vacancyType.getElement().setId(test_code_ID_name + "vacancy_type");

        //Planned place count
        plannedPlaceCount = new HTML();
        plannedPlaceCount.addStyleName(DEFAULT_WIDTH);
        plannedPlaceCount.getElement().setId(test_code_ID_name + "vacant_place_count");

        //job type
        jobType = new HTML();
        jobType.addStyleName(DEFAULT_WIDTH);
        jobType.getElement().setId(test_code_ID_name + "job_type");

        //job family
        jobFamily = new HTML();
        jobFamily.addStyleName(DEFAULT_WIDTH);
        jobFamily.getElement().setId(test_code_ID_name + "job_family");

        //Required degree
        requiredDegree = new HTML();
        requiredDegree.addStyleName(DEFAULT_WIDTH);
        requiredDegree.getElement().setId(test_code_ID_name + "required_degree");

        //Vacancy attachments
        GeneralFileUpload attachments = new GeneralFileUpload(F_VACANCY, objectID, objectID);
        attachments.getPanel().getElement().setId(test_code_ID_name + "attachment");

        //Notes widget
        NoteWidget noteWidget = new NoteWidget(objectID, VACANCY);
        noteWidget.getTextBox().getElement().setId(test_code_ID_name + "notes");

        //Gender Table
        genderTable = new HTML();
        genderTable.addStyleName(DEFAULT_WIDTH);
        genderTable.getElement().setId(test_code_ID_name + "genderTable");

        //proposedSalary
        proposedSalary = new HTML();
        proposedSalary.addStyleName(DEFAULT_WIDTH);
        proposedSalary.getElement().setId(test_code_ID_name + "proposedSalary");

        //Project link
        project = new HTML();
        project.addStyleName(DEFAULT_WIDTH);
        project.getElement().setId(test_code_ID_name + "proposedSalary");
        approver = new HTML();
        approver.addStyleName(DEFAULT_WIDTH);
        approver.getElement().setId(test_code_ID_name + "approver");

        //Currency
        currency = new HTML();
        currency.addStyleName(DEFAULT_WIDTH);
        currency.getElement().setId(test_code_ID_name + "currency");

        //Notes Widget
        notesPanel = new NotesWidget(false);

        //Language
        languagesWidget = new FlexTable();
        languagesWidget.addStyleName("languagesWidget-table");
        languagesWidget.getElement().setId("candidate_summary_view_language");
        languagesWidget.getRowFormatter().addStyleName(0, "languagesWidget-table__thead");
        languagesWidget.setHTML(0, 0, wfmStrings.language());
        languagesWidget.setHTML(0, 1, wfmStrings.level());

        addTitleField(CustomFormConstants.VACANCY.BASIC_INFORMATION, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.VACANCY.INTERNAL_DETAILS, wfmStrings.internalDetails());
        addTitleField(CustomFormConstants.VACANCY.POSITION_INFORMATION, wfmStrings.positionInformation());
        addTitleField(CustomFormConstants.VACANCY.DETAILED_INFORMATION, wfmStrings.detailedInformation());
        addTitleField(CustomFormConstants.VACANCY.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        addField(CustomFormConstants.VACANCY.VACANCY_ATTACHMENTS, attachments, getTitle(wfmStrings.attachments()));
        addField(CustomFormConstants.VACANCY.APPROVER, approver, wfmStrings.approver());
        addField(CustomFormConstants.VACANCY.DEPARTMENT, department, getTitle(wfmStrings.department()));
        addField(CustomFormConstants.VACANCY.GENDER, genderTable, getTitle(wfmStrings.sexDesire()));
        addField(CustomFormConstants.VACANCY.JOB_FAMILY, jobFamily, getTitle(wfmStrings.jobFamily()));
        addField(CustomFormConstants.VACANCY.DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField(CustomFormConstants.VACANCY.JOB_REQUIREMENT, jobRequirement, getTitle(wfmStrings.jobRequirements()));
        addField(CustomFormConstants.VACANCY.RESPONSIBILITIES, responsibilities, getTitle(wfmStrings.responsibilities()));
        addField(CustomFormConstants.VACANCY.JOB_TITLE, jobTitle, getTitle(wfmStrings.name()));
        addField(CustomFormConstants.VACANCY.JOB_TYPE, jobType, getTitle(wfmStrings.jobType()));
        addField(CustomFormConstants.VACANCY.LOCATION, locationName, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        addField(CustomFormConstants.VACANCY.MANAGER, managerName, getTitle(hrmsStrings.orderedBy()));
        addField(CustomFormConstants.VACANCY.MATCHED_CANDIDATES, matchedCandidates, getTitle(hrmsStrings.listOfmatchedCandidates()));
        addField(CustomFormConstants.VACANCY.POSITION, positionName, getTitle(wfmStrings.position()));
        addField(CustomFormConstants.VACANCY.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        addField(CustomFormConstants.VACANCY.PROPOSED_SALARY, new InputGroup(proposedSalary, currency), getTitle(wfmStrings.proposedSalary()));
        addField(CustomFormConstants.VACANCY.REQUIRED_DEGREE, requiredDegree, getTitle(wfmStrings.requiredDegree()));
        addField(CustomFormConstants.VACANCY.START_DATE, startDate, getTitle(wfmStrings.closePeriod()));
        addField(CustomFormConstants.VACANCY.STATUS, statusName, getTitle(wfmStrings.status()));
        addField(CustomFormConstants.VACANCY.VACANCY_NOTES, noteWidget, wfmStrings.notes());
        addField(CustomFormConstants.VACANCY.VACANCY_NUMBER, number, getTitle(wfmStrings.number()));
        addField(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT, plannedPlaceCount, getTitle(wfmStrings.requestedQuantity()));
        addField(CustomFormConstants.VACANCY.VACANCY_TYPE, vacancyType, getTitle(wfmStrings.vacancyType()));
        addField(LANGUAGE, languagesWidget, wfmStrings.spokenLanguages());
        addField(VACANCY_QUESTIONS, vacancyQuestionsWidget, "", true);
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        show();
    }

    private KpiEditor createEditor(String debugIdSuffix) {
        KpiEditor editor = new KpiEditor(false, true, " ", false);
        editor.setHeight("415px");
        editor.getRichEditor().setHeight("350px");
        editor.getElement().setId(DEBUG_ID_PREFIX + debugIdSuffix);
        return editor;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
