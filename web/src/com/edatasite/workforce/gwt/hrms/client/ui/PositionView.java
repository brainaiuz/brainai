package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PositionView extends CustomForm2 implements Constants {

    private static final String DEBUG_ID_PREFIX = "position_summary_view_";

    // KPI Editors
    private KpiEditor description, responsibilities, jobRequirement, measuringEmployeePerformance, personalQualities, knowledge;

    // Basic Fields
    private HTML positionCode, positionTitle, established, status, type, plannedPlaceCount, headCount, coefficent, salaryBasis;
    private Anchor department, location;

    // UI Controls
    private SplitButton printPdfSplitButton;

    // Data
    private final Integer int_positionID;
    private PositionItem positionItem;
    private FormHasCustomField customFieldUtil;

    public PositionView(Integer int_positionID) {
        super("summary", wfmStrings.summaryView());
        this.int_positionID = int_positionID;
    }


    @Override
    public String getIconStyle() {
        return "hrms position-list";
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.POSITION_FORM;
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

    // -- Initialization Logic --

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Positions, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                PositionView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void getDataToFillFields() {
        loadPositionData();
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    // -- Button Configuration --

    @Override
    protected void addButtons() {
        // PDF Button
        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        addRightButton(printPdfSplitButton);

        // Delete Button
        addRemoveButton().addClickHandler(event -> confirmAndExecuteDelete());

        // Edit Listener
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_POSITION_ADD_EDIT, PositionView.this, (sender, args) -> loadPositionData());

        // Add Vacancy Button
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_VACANCY)) {
            addRightButton(createAddVacancyButton());
        }

        // Edit Button
        if (Utils.hasPermission(HRMS_POSITION_EDIT)) {
            addEditButton().addClickHandler(event -> closeTab(
                    "positions|editpositions/" + int_positionID,
                    wfmStrings.edit() + " " + positionItem.getNumberData().getFirstNumberString()
            ));
        }
    }

    private Widget createAddVacancyButton() {
        MaterialLink addButton = new MaterialLink(wfmStrings.add());
        MaterialSplitButton addSplitButton = new MaterialSplitButton(addButton, Constants.BTN_DEFAULT_OUTLINE);

        MaterialLink addVacancy = new MaterialLink(Property.get(Constants.VACANCY, wfmStrings.request(), wfmStrings.vacancy()));
        addVacancy.ensureDebugId("addVacancy");

        addVacancy.addClickHandler(event -> {
            if (positionItem.getObjectID() != null) {
                goTo("vacancy|add/add/positionId/" + int_positionID, wfmStrings.request() + " " + positionItem.getNumberData().getFirstNumberString());
            } else {
                goTo("vacancy|add/add/");
            }
        });

        addSplitButton.addItem(addVacancy);
        return addSplitButton;
    }

    private void confirmAndExecuteDelete() {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
        message.setTitle(wfmStrings.deleting());
        message.setMessage(wfmStrings.sureYouWantToDelete());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                performDelete();
            }
        });
        message.open();
    }

    private void performDelete() {
        LoadingPanel.loading(true);
        HrmsService.App.get().deletePosition(positionItem.getObjectID(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_DELETE, result, PositionView.this);
                closeTab();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.position()), Info.Type.INFO);
            }
        });
    }

    // -- Field Registration --

    @Override
    protected void registerFields() {
        registerBasicInformation();
        registerDetailedInformation();
        registerAdditionalInformation();

        show();
    }

    private void registerBasicInformation() {
        addTitleField(POSITIONS.BASIC_INFORMATION, wfmStrings.basicDetails());

        positionCode = createHtmlField("position_code");
        positionTitle = createHtmlField("position_title");
        established = createHtmlField("established");
        status = createHtmlField("status");
        coefficent = createHtmlField("position_coefficent");
        salaryBasis = createHtmlField("position_salaryBasis");
        plannedPlaceCount = createHtmlField("count");
        headCount = createHtmlField("headCount");
        type = createHtmlField("type");

        // Anchors with special logic
        location = createAnchor(wfmStrings.notAvailable(), "location");
        configureLocationClick();

        department = createAnchor(wfmStrings.notAvailable(), "department");
        configureDepartmentClick();

        // Adding fields to layout
        addField(POSITIONS.POSITION_CODE, positionCode, getTitle(wfmStrings.number()));
        addField(POSITIONS.POSITION_TITLE, positionTitle, getTitle(wfmStrings.position()));
        addField(POSITIONS.ESTIBLISHED, established, getTitle(wfmStrings.createdDate()));
        addField(POSITIONS.DEPARTMENT, department, getTitle(wfmStrings.department()));
        addField(CustomFormConstants.PROJECT.LOCATION, location, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()));
        addField(CustomFormConstants.COUNT, plannedPlaceCount, wfmStrings.vacantPlaceCount());
        addField(POSITIONS.CURRENT_HEAD_COUNT, headCount, wfmStrings.headCount());
        addField(CustomFormConstants.TYPE, type, wfmStrings.type());
        addField(POSITIONS.STATUS, status, getTitle(wfmStrings.status()));
        addField(POSITIONS.COEFFICENT, coefficent, wfmStrings.coefficent());
        addField(POSITIONS.SALARY_BASIS, salaryBasis, wfmStrings.salaryBasis());
    }

    private void registerDetailedInformation() {
        this.addTitleField(CustomFormConstants.VACANCY.DETAILED_INFORMATION, wfmStrings.detailedInformation());

        description = createEditor("description");
        jobRequirement = createEditor("jobRequirement");
        responsibilities = createEditor("responsibilities");

        addField(POSITIONS.POSITION_DESCRIPTION, description, wfmStrings.description());
        addField(POSITIONS.JOB_REQUIREMENT, jobRequirement, wfmStrings.jobRequirements());
        addField(POSITIONS.POSITION_RESPONSIBILITIES, responsibilities, wfmStrings.responsibilities());

        addTitleField(CustomFormConstants.POSITIONS.KNOWLEDE_AND_SKILLS, "Knowledge And Skills");

        knowledge = createEditor("knowledge");
        knowledge.setEnabled(false);
        measuringEmployeePerformance = createEditor("measuringEmployeePerformance");
        personalQualities = createEditor("personalQualities");

        addField(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE, measuringEmployeePerformance, wfmStrings.measuringEmployeePerformance());
        addField(POSITIONS.PERSONAL_QUALITIES, personalQualities, wfmStrings.personalQualiteis());
        addField(POSITIONS.KNOWLEDGE, knowledge, wfmStrings.knowledge());


        addTitleField(POSITIONS.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
    }

    private void registerAdditionalInformation() {
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, int_positionID, true);
    }

    // -- UI Component Factories --

    private HTML createHtmlField(String debugIdSuffix) {
        HTML field = new HTML();
        field.addStyleName(DEFAULT_WIDTH);
        field.ensureDebugId(DEBUG_ID_PREFIX + debugIdSuffix);
        return field;
    }

    private Anchor createAnchor(String text, String debugIdSuffix) {
        Anchor anchor = new Anchor(text);
        anchor.addStyleName(DEFAULT_WIDTH);
        anchor.ensureDebugId(DEBUG_ID_PREFIX + debugIdSuffix);
        return anchor;
    }

    private KpiEditor createEditor(String debugIdSuffix) {
        KpiEditor editor = new KpiEditor(false, true, " ", false);
        editor.setHeight("415px");
        editor.getRichEditor().setHeight("350px");
        editor.getElement().setId(DEBUG_ID_PREFIX + debugIdSuffix);
        return editor;
    }

    // -- Link Event Handlers --

    private void configureLocationClick() {
        location.addClickHandler(event -> {
            if (!Utils.hasPermission(HRMS_EDIT_LOCATION)) {
                Info.warn(wfmStrings.youDontHavePermission());
                return;
            }
            if (positionItem != null && positionItem.getLocation() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("location|summary/" + positionItem.getLocation().getId(), positionItem.getLocation().getName());
            } else {
                Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.location());
            }
        });
    }

    private void configureDepartmentClick() {
        department.addClickHandler(event -> {
            if (!Utils.hasPermission(HRMS_DEPARTMENT_SUMMARY_VIEW)) {
                Info.warn(wfmStrings.youDontHavePermission());
                return;
            }
            if (positionItem != null && positionItem.getDepartment() != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + positionItem.getDepartmentId(), positionItem.getDepartment().getName());
            } else {
                Info.warn(wfmStrings.noDataAvailable() + "- " + wfmStrings.department());
            }
        });
    }

    @Override
    protected void initPredefinedValues() {
        // No predefined values needed
    }

    // -- Data Loading --

    private void loadPositionData() {
        HrmsService.App.get().getPositionForEdit(int_positionID, "", new AbstractAsyncCallback<PositionItem>() {
            @Override
            public void success(PositionItem object) {
                if (object != null) {
                    positionItem = object;
                    setDescription(positionItem.getNumber() + wfmStrings.summaryView());
                    populateFields();
                    configurePdfTools(object);
                }
            }
        });
    }

    private void populateFields() {
        // Basic Info
        safeSetHtml(positionCode, positionItem.getNumberData() != null ? positionItem.getNumberData().getFirstNumberString() : "");
        safeSetHtml(positionTitle, positionItem.getName());
        safeSetHtml(established, positionItem.getEstablished() != null ? DateUtils.format(positionItem.getEstablished()) : "");
        safeSetHtml(status, positionItem.getStatus() != null ? positionItem.getStatus().getName() : "");
        safeSetHtml(department, positionItem.getDepartment() != null ? positionItem.getDepartment().getName() : "");
        safeSetHtml(plannedPlaceCount, positionItem.getCount());
        safeSetHtml(headCount, positionItem.getHeadCount() != null ? String.valueOf(positionItem.getHeadCount()) : "");
        safeSetHtml(location, positionItem.getLocation() != null ? positionItem.getLocation().getName() : null);
        safeSetHtml(type, positionItem.getType() != null ? positionItem.getType().getName() : null);
        safeSetHtml(coefficent, String.valueOf(positionItem.getCoefficent() != null ? positionItem.getCoefficent() : ""));
        safeSetHtml(salaryBasis, String.valueOf(positionItem.getSalaryBasis() != null ? positionItem.getSalaryBasis() : ""));

        // Detailed Info (with localization)
        description.setData(resolveLocalizedText(positionItem.getPositionDescription(), positionItem.getDescriptionLocalize()));
        jobRequirement.setData(resolveLocalizedText(positionItem.getJobRequirements(), positionItem.getJobRequirementLocalize()));
        responsibilities.setData(resolveLocalizedText(positionItem.getResponsibility(), positionItem.getResponsibilitiesLocalize()));

        measuringEmployeePerformance.setData(resolveLocalizedText(positionItem.getMeasuringEmployeePerformance(), positionItem.getMeasuringEmployeePerformanceLocalize()));
        personalQualities.setData(resolveLocalizedText(positionItem.getPersonalQualities(), positionItem.getPersonalQualitiesLocalize()));
        knowledge.setData(resolveLocalizedText(positionItem.getKnowledge(), positionItem.getKnowledgeLocalize()));

        // Custom Fields
        getCustomFieldUtil().fillCustomFieldsWithData(positionItem.getCustomFieldItems(), true);
    }

    // -- Data Helper Methods --

    private void safeSetHtml(HTML widget, String text) {
        widget.setHTML(text != null ? text : "");
    }

    private void safeSetHtml(HasHTML widget, String text) {
        widget.setHTML(text != null ? text : "");
    }
    private String resolveLocalizedText(String defaultText, HashMap<String, String> localizedMap) {
        String userLanguage = Utils.getUserLanguage();
        if (mapHasValueForLang(localizedMap, userLanguage)) {
            return localizedMap.get(userLanguage);
        }
        return defaultText != null ? defaultText : "";
    }

    // -- PDF Tools --

    public void configurePdfTools(PositionItem result) {
        if (printPdfSplitButton == null) return;

        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;

        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(),
                        () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(),
                    () -> generatePDF(panel, null, true)));
        }

        final Integer finalDefaultTemplateId = defaultTemplateId;
        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(),
                () -> generatePDF(panel, finalDefaultTemplateId, false), true);

        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(int_positionID);
        HashMap<String, String> parameters = requestObject.getRequestParams();

        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/positionListViewPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }
}