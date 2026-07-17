package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.ui.ViewContactForm;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemGrid;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.CandidateStatusHistoryGrid;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels.CrmActivityGrid;
import com.edatasite.workforce.gwt.crm.client.ui.view.widgets.ContactStatusHistoryGrid;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: hayot
 * Date: 7/3/12
 * Time: 10:30 AM
 */
public class ViewCandidateForm extends ViewContactForm implements HasLinksInterface {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private HTML number, createdDate, source, workExperience, currentEmployer, expectedSalary,startSalary, passportNumber, martialStatus, preferredLocation, status, project, createdBy, updatedDate, updatedBy, department, position,timeslot;
    private Div vacancies;
    private TextArea2 skills;
    private FlexTable languagesWidget;
    private HasLinks linkingUtil;
    private SplitButton printPdfSplitButton;
    private static final NumberFormat extendedNumberFormat = NumberFormat.getFormat(",##0.00");
    private ProfileImage profileImage;
    private EditableTable allowancesTable;
    private CandidateStatusHistoryGrid candidateStatusHistoryGrid;

    public ViewCandidateForm(Integer objectID) {
        super("summary", wfmStrings.summaryView());
        this.objectId = objectID;
        filterParametrs.setObjectId(objectID);
        filterParametrs.setViewType("candidate");
        filterParametrs.setHasAccessToChange(hasEditPermission());
        setContactType(ContactListItem.CANDIDATE);
        super.valueId = objectId;
        super.relationType = RelationItem.TYPE_CANDIDATE;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected String getRelationType() {
        return RelationItem.TYPE_CANDIDATE;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(ViewCandidateForm.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectId;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_CANDIDATE;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getName() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private boolean hasPermissionToVacancySummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW);
    }

    @Override
    public void setContactItem() {
        super.setContactItem();
        if (item.getObjectId() != null && item.getNumberData() != null) {
            number.setHTML(item.getNumberData().getNumberString());
        }
        //links
        getLinkingUtil().getTaggingView().setSelectedRelations(item.getRelations());
        getLinkingUtil().drawLinks();

        createdDate.setHTML(DateUtils.formatInternal(item.getCreatedDate()) + Utils.getHijriDate(item.getCreatedDate()));
        updatedDate.setHTML(DateUtils.formatInternal(item.getUpdatedDate()) + Utils.getHijriDate(item.getUpdatedDate()));
        createdBy.setHTML(item.getCreator());
        updatedBy.setHTML(item.getUpdater());
        source.setHTML(item.getCandidateSource() != null ? item.getCandidateSource().getName() : wfmStrings.notAvailable());
        String workExperienceT = item.getWorkExperience() == null ? "" : item.getWorkExperience().toString();
        if (item.getWorkExperienceMonthOrYear() != null) {
            if (item.getWorkExperience() != 0 && item.getWorkExperience() > 0) {
                if (item.getWorkExperienceMonthOrYear() != null && item.getWorkExperienceMonthOrYear() == 1) {
                    workExperienceT = item.getWorkExperience().toString() + " " + wfmStrings.month();
                } else if (item.getWorkExperienceMonthOrYear() != null && item.getWorkExperienceMonthOrYear() == 2) {
                    if (item.getWorkExperience() == 1) {
                        workExperienceT = item.getWorkExperience().toString() + " " + wfmStrings.year();
                    } else {
                        workExperienceT = item.getWorkExperience().toString() + " " + wfmStrings.years();
                    }
                }
            }
        }
        workExperience.setHTML(workExperienceT);
        timeslot.setHTML(item.getTimeSlotItem() != null ? item.getTimeSlotItem().getName() : null);
        department.setHTML(item.getDepartmentItem() != null ? item.getDepartmentItem().getName() : null);
        position.setHTML(item.getPositionItem() != null ? item.getPositionItem().getName() : null);
        currentEmployer.setHTML(item.getCurrentEmployer());
        expectedSalary.setHTML(item.getExpectedSalary() != null ? extendedNumberFormat.format(item.getExpectedSalary()).replace(".00", "") : "");
        startSalary.setHTML(item.getStartSalary() != null ? extendedNumberFormat.format(item.getStartSalary()).replace(".00", "") : "");
        passportNumber.setHTML(item.getPassportNumber() != null ? item.getPassportNumber() : "");
        martialStatus.setHTML(item.getMartialStatus() != null ? item.getMartialStatus() : "" );
        skills.setText(item.getSkills());
        preferredLocation.setHTML(item.getPreferredLocation() != null ? item.getPreferredLocation().getName() : wfmStrings.notAvailable());
        status.setHTML(item.getCandidateStatus() != null ? item.getCandidateStatus().getName() : wfmStrings.notAvailable());
        project.setHTML(item.getProjectItem() != null ? item.getProjectItem().getName() : wfmStrings.notAvailable());
        if (item.getVacancies() != null && item.getVacancies().size() > 0) {
            int i = 0;
            for (SelectItem selectItem : item.getVacancies()) {
                Span delimiter = new Span(", ");
                String link = "vacancy|summary/" + selectItem.getId();
                SimpleLink name = new SimpleLink(selectItem.getName());
                name.addClickHandler(clickEvent -> {
                    if (hasPermissionToVacancySummary()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(link);
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                });
                vacancies.add(name);
                if (i != item.getVacancies().size() - 1) {
                    vacancies.add(delimiter);
                }
                i++;
            }
        }
        if (item.getSpokingLanguages() != null) {
            item.getSpokingLanguages().forEach(sl -> {
                int index = languagesWidget.getRowCount();
                languagesWidget.setHTML(index, 0, sl.getLanguage() != null ? sl.getLanguage().getName() : "");
                languagesWidget.setHTML(index, 1, sl.getLevel() != null ? sl.getLevel().getName() : "");
            });
        }
        if (item.getAllowanceCategories().size() > 0) {
            for (PaymentDeductionObject object : item.getAllowanceCategories()) {
                allowancesTable.addRow(new Widget[]{new Label(object.getCategoryname()), new Label(wfmStrings.fixed()), new Label(object.getPaymentAmount() != null ? object.getPaymentAmount().toString() : "0")});
            }
        }
    }

    @Override
    protected void addButtons() {

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        if (Utils.hasPermission(PermissionConstants.HRMS_PRINT_PDF_CANDIDATE)) {
            addRightButton(printPdfSplitButton);
        }

        customizeButton.setVisible(false);
        MaterialDropDown options = addMoreSplitButton(wfmStrings.options());
        if (Utils.hasRole(Constants.ADMIN)) {
            MaterialLink customize = new MaterialLink(wfmStrings.customize());
            customize.addClickHandler(click -> {
                String url = Window.Location.getHash().replace("#", "").replace("%257C", "|");
                SinksContainerFactory.entryPoint.onHistoryChanged("customizeForm|add/add" + "/" + getFormID() + "/" + (url != null ? URL.encodeQueryString(url) : ""));
            });
            options.add(customize);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_DELETE_CANDIDATE)) {
            MaterialLink deleteButton = new MaterialLink(wfmStrings.delete());
            deleteButton.addClickHandler(event -> {
                LoadingPanel.loading(true);
                ArrayList<Integer> ids = new ArrayList<>();
                ids.add(item.getObjectId());
                ContactService.App.get().canDeleteCandidate(ids, new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        LoadingPanel.loading(false);
                        if (aBoolean) {
                            deleteContactItem(item);
                        } else {
                            Info.show(wfmStrings.youCannotDelete(), Info.Type.WARNING);
                        }
                    }
                });

            });
            options.add(deleteButton);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_SEARCH_CANDIDATE)) {
            MaterialLink lookUp = new MaterialLink(crmStrings.searchIn());
            lookUp.add(createSearchMenu());
            options.add(lookUp);
        }

        MaterialLink addButton = new MaterialLink(wfmStrings.add());
        MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);
        boolean callQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_QUICK_CALL_CANDIDATE);
        boolean callAddPermission = Utils.hasPermission(PermissionConstants.HRMS_CALL_CANDIDATE);
        if (callQuickAddPermission || callAddPermission) {
            MaterialLink button = new MaterialLink(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
            button.getElement().setId("candidate_summary_view_log_a_call_button");
            button.addClickHandler(event -> {
                if (callQuickAddPermission) {
                    if (item.getCrmAccount() != null && item.getCrmAccount().getObjectId() != null && item.getCrmAccount().getName() != null) {
                        new ActivityQuickAddForm(Appointment.CALL_LOG,
                                RelationItem.newEventRelation(getRelationType(), objectId, item.getName()),
                                RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT,
                                        item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                    } else {
                        new ActivityQuickAddForm(Appointment.CALL_LOG,
                                RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                    }
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.CALL_LOG + "/" + item.getObjectId() + "/" + getRelationType());
                }
            });
            addSplitButton.addItem(button);
        }

        boolean interviewQuickAddPermission = Utils.hasPermission(PermissionConstants.HRMS_QUICK_INTERVIEW_CANDIDATE);
        boolean interviewAddPermission = Utils.hasPermission(PermissionConstants.HRMS_INTERVIEW_CANDIDATE);
        if (interviewAddPermission || interviewQuickAddPermission) {
            MaterialLink interviewButton = new MaterialLink(wfmStrings.interview());
            interviewButton.getElement().setId("candidate_summary_view_interview_button");
            interviewButton.addClickHandler(event -> {
                if (interviewQuickAddPermission) {
                    if (item.getCrmAccount() != null && item.getCrmAccount().getObjectId() != null && item.getCrmAccount().getName() != null) {
                        new ActivityQuickAddForm(Appointment.INTERVIEW,
                                RelationItem.newEventRelation(getRelationType(), objectId, item.getName()),
                                RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT,
                                        item.getCrmAccount().getObjectId(), item.getCrmAccount().getName()));
                    } else {
                        new ActivityQuickAddForm(Appointment.INTERVIEW,
                                RelationItem.newEventRelation(getRelationType(), objectId, item.getName()));
                    }
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.INTERVIEW + "/" + item.getObjectId() + "/" + getRelationType());
                }
            });
            addSplitButton.addItem(interviewButton);
        }
        if (Utils.hasPermission(PermissionConstants.HRMS_CONDIDATE_SMS_SEND)) {
            MaterialLink addSms = new MaterialLink(wfmStrings.sms());
            addSms.addClickHandler(event -> new ActivityQuickAddForm(Appointment.SMS, item, null, RelationItem.newEventRelation(getRelationType(), item.getObjectId(), item.getName())));
            addSplitButton.addItem(addSms);
        }

        addRightButton(addSplitButton);

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_CANDIDATE)) {
            addButton(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, event -> closeTab("candidateedit|editcandidate" + "/" + item.getObjectId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getName(), item.getName()));
        }


    }

    public void pdfTool(ContactListItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectId);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/candidateFormPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }


    @Override
    public void initialize() {
        super.initialize();
        String candidate_summary_view = "candidate_summary_view_";
        profileImage = new ProfileImage(objectId, LayoutRPC.CONTACT_FORM);
        //numbering
        number = new HTML();
        number.addStyleName(DEFAULT_WIDTH);
        number.setStyleName("file--ViewCandidateForm");
        number.getElement().setId(candidate_summary_view + "number");
        //created date
        createdDate = new HTML();
        createdDate.addStyleName(DEFAULT_WIDTH);
        createdDate.getElement().setId(candidate_summary_view + "created_date");
        //created date
        createdBy = new HTML();
        createdBy.addStyleName(DEFAULT_WIDTH);
        createdBy.getElement().setId(candidate_summary_view + "created_by");
        //created date
        updatedDate = new HTML();
        updatedDate.addStyleName(DEFAULT_WIDTH);
        updatedDate.getElement().setId(candidate_summary_view + "updated_date");
        //created date
        updatedBy = new HTML();
        updatedBy.addStyleName(DEFAULT_WIDTH);
        updatedBy.getElement().setId(candidate_summary_view + "updated_by");
        //source
        source = new HTML();
        source.addStyleName(DEFAULT_WIDTH);
        source.getElement().setId(candidate_summary_view + "source");
        //work experience
        workExperience = new HTML();
        workExperience.addStyleName(DEFAULT_WIDTH);
        workExperience.getElement().setId(candidate_summary_view + "work_experience");
        //current employer
        currentEmployer = new HTML();
        currentEmployer.addStyleName(DEFAULT_WIDTH);
        currentEmployer.getElement().setId(candidate_summary_view + "current_employer");
        //expected salary
        expectedSalary = new HTML();
        expectedSalary.addStyleName(DEFAULT_WIDTH);
        expectedSalary.getElement().setId(candidate_summary_view + "expected_salary");

        startSalary = new HTML();
        startSalary.addStyleName(DEFAULT_WIDTH);
        startSalary.getElement().setId(candidate_summary_view + "start_salary");

        passportNumber = new HTML();
        passportNumber.addStyleName(DEFAULT_WIDTH);
        passportNumber.getElement().setId(candidate_summary_view + "passport_number");


        martialStatus = new HTML();
        martialStatus.addStyleName(DEFAULT_WIDTH);
        martialStatus.getElement().setId(candidate_summary_view + "martialStatus");
        //competencies
        skills = new TextArea2(1000);
        skills.setEnabled(false);
        skills.setReadOnly(true);
        skills.hideCharacterLimitPanel();
        skills.addStyleName("file--ViewCandidateForm");
        skills.getTextArea().getElement().setId(candidate_summary_view + "competencies");
        //preferred location
        preferredLocation = new HTML();
        preferredLocation.addStyleName(DEFAULT_WIDTH);
        preferredLocation.getElement().setId(candidate_summary_view + "preferred_location");
        //candidate status
        status = new HTML();
        status.addStyleName(DEFAULT_WIDTH);
        status.getElement().setId(candidate_summary_view + "candidate_status");
        //matched vacancies
        vacancies = new Div();
        vacancies.addStyleName(DEFAULT_WIDTH);
        vacancies.getElement().setId(candidate_summary_view + "matched_vacancies");

        //Languages
        languagesWidget = new FlexTable();
        languagesWidget.addStyleName("languagesWidget-table");
        languagesWidget.getElement().setId(candidate_summary_view + "language");
        languagesWidget.getRowFormatter().addStyleName(0, "languagesWidget-table__thead");
        languagesWidget.setHTML(0, 0, wfmStrings.language());
        languagesWidget.setHTML(0, 1, wfmStrings.level());
//        languagesWidget.getFlexCellFormatter().getElement(0, 1).getStyle().setPaddingLeft(10d, Style.Unit.PX);


        //project
        project = new HTML();
        project.addStyleName(DEFAULT_WIDTH);
        project.getElement().setId(candidate_summary_view + "project");

        //activities
        activityWidget = new CrmActivityGrid(objectId, RelationItem.TYPE_CANDIDATE);
        activityWidget.getElement().setId(candidate_summary_view + "activities");

        allowancesTable = new EditableTable(getAllowancesTableColumn());
        allowancesTable.ensureDebugId("allowances_");
        candidateStatusHistoryGrid = new CandidateStatusHistoryGrid(objectId);

        department = new HTML();
        department.addStyleName(DEFAULT_WIDTH);

        position = new HTML();
        position.addStyleName(DEFAULT_WIDTH);

        timeslot = new HTML();
        timeslot.addStyleName(DEFAULT_WIDTH);
    }

    public ColumnConfig[] getAllowancesTableColumn() {
        ColumnConfig[] columns = new ColumnConfig[3];
        columns[0] = new ColumnConfig(CustomCell.class, "category", wfmStrings.allowances(), 220, true, "left-align-Cell");
        columns[1] = new ColumnConfig(CustomCell.class, "type", wfmStrings.type(), 100, true, "center-align-Cell");
        columns[2] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount() + " / " + wfmStrings.percentage(), 120, true, "right-align-Cell");
        return columns;
    }

    //status history
    protected void showStatusHistory() {
        if (objectId != null) {
            statusHistoryGrid = new ContactStatusHistoryGrid(objectId, ContactListItem.CANDIDATE, false);
            statusHistoryGrid.getElement().setId("candidate_summary_view_status_history_tab");
        }
    }


    @Override
    protected void drawForm() {
        super.drawForm();
        addTitleField(CANDIDATE.ALLOWANCE_INFORMATION, wfmStrings.allowanceInformation());
        addField(CANDIDATE_PICTURE, profileImage, null, true);
        addField(NUMBER, number, wfmStrings.number());
        addField(CREATED_DATE, createdDate, wfmStrings.createdDate());
        addField(CREATED_BY, createdBy, wfmStrings.createdBy());
        addField(UPDATED_DATE, updatedDate, wfmStrings.modifiedDate());
        addField(UPDATED_BY, updatedBy, wfmStrings.modifiedBy());
        addField(LEAD_SOURCE, source, wfmStrings.source());
        addField(LANGUAGE, languagesWidget, wfmStrings.spokenLanguages());
        addField(CANDIDATE.CANDIDATE_PROJECT, project, Property.get(Constants.PROJECT, wfmStrings.project()));
        addField(CANDIDATE.WORK_EXPERIENCE, workExperience, wfmStrings.workExperience());
        addField(CANDIDATE.CURRENT_EMPLOYER, currentEmployer, wfmStrings.currentEmployer());
        addField(CANDIDATE.EXPECTED_SALARY, expectedSalary, wfmStrings.expectedSalary());
        addField(MARTIAL_STATUS, martialStatus, wfmStrings.maritalStatus());
        addField(CANDIDATE.SKILLS, skills, wfmStrings.skills());
        addField(CANDIDATE.LOCATION, preferredLocation, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        addField(STATUS, status, wfmStrings.status());
        addField(CANDIDATE.VACANCIES, vacancies, wfmStrings.matchedVacancies());
        //links
        addField(LINKS, getLinkingUtil().getLinkAndLinksPanelInVerticalPanel(), wfmStrings.links(), true);
        addField(CANDIDATE.ALLOWANCES, allowancesTable, null, true);
        addField(CustomFormConstants.CANDIDATE.CANDIDATE_STATUS_HISTORY, candidateStatusHistoryGrid, null, true);
        addField(CANDIDATE.TIMESLOT, timeslot, wfmStrings.timeslot());
        addField(CANDIDATE.STARTSALARY, startSalary, wfmStrings.startSalary());
        addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, wfmStrings.passportNumber());
        addField(CANDIDATE.DEPARTMENT, department, wfmStrings.department());
        addField(CANDIDATE.POSITION, position, wfmStrings.position());
        drawItemTable1();
    }


    protected void drawItemTable1() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.CANDIDATE_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        CustomFormItemGrid itemView = new CustomFormItemGrid(objectId, configMap.getKey(), LayoutRPC.CANDIDATE_FORM, configMap.getValue(), 1000);
                        ColumnConfigs[] value = configMap.getValue();
                        addField(configMap.getKey(), itemView, null, true);
                    }
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CANDIDATE_FORM;
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        ContactService.App.get().getContact(objectId, false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = o;
                    long startInitializeProfileImage = System.currentTimeMillis();
                    profileImage.initialize(o.getContactImageUrl(), o.getFirstName(), o.getLastName(), false, o.getGender() != null ? o.getGender() : Constants.VACANT);
                    GWT.log("Took: " + (System.currentTimeMillis() - startInitializeProfileImage) + " ms to initialize profile image");
                    long startInitializeConatctItem = System.currentTimeMillis();
                    setContactItem();
                    GWT.log("Took: " + (System.currentTimeMillis() - startInitializeConatctItem) + " ms to initialize profile");
                    long startInitializePdfTool = System.currentTimeMillis();
                    pdfTool(item);
                    GWT.log("Took: " + (System.currentTimeMillis() - startInitializePdfTool) + " ms to initialize pdf tool");
                });
            }
        });
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