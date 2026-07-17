package com.edatasite.workforce.gwt.hrms.client.ui.recruitment;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectVacancyLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CandidateQuickValidate;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldEntityLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT;

public class AddPlacementView extends CustomForm2 implements Constants, Colapse, HasLinksInterface {
    public static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer objectID;
    public PlacementItem item;
    private Integer maxNoAccessEmp = 0;
    private Integer convertFormId, candidateID;
    private boolean isIntervalCandidate;
    protected String formType;
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private final Map<String, EditableTable> editableTableMap = new HashMap<>();
    private final Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();
    public LinkedHashMap<String, FormProperty> formPropertyMap;

    private Numbering placementCode;
    private CRMLookUp candidate, project;
    private DatePicker dateOffered;
    private LocationLookUpWithCode location;
    private DepartmentLookUp department;
    private PositionLookUp position;
    private Div inputGroup;
    private TextBox headCount, plannedPlaceCount;
    private ChosenApproversWidget approver;
    private KpiRadioButton radioButton, radioButton2;
    private MultiSelectVacancyLookUp vacancies;
    public PlacementStatusLogic placementStatusLogic;

    private GeneralFileUpload fileUpload;
    private HasLinks linkingUtil;
    protected SplitButton printPdfSplitButton;
    private NoteWidget noteWidget;
    private FooterInformer link;

    private Div positionContainer;
    private Div departmentContainer;
    private AdvancedInputGroup locationContainer;

    public AddPlacementView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }

    public AddPlacementView(Integer objectID, Integer candidateID) {
        super("addPlacement", objectID == null ? hrmsStrings.addPlacement() : hrmsStrings.editPlacement());
        this.objectID = objectID;
        this.candidateID = candidateID;
    }

    public AddPlacementView(Integer convertFormId, String formType) {
        super("addPlacement", hrmsStrings.addPlacement());
        this.formType = formType;
        this.convertFormId = convertFormId;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Placement, LayoutRPC.PLACEMENT_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                AddPlacementView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                }
                AddPlacementView.super.onInitialize();
            }
        });
        getEmployeesMaxCount();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, AddPlacementView.this, (sender, args) -> ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(SelectItem[] selectItems) {
                if (selectItems != null) {
                    if (args instanceof Integer) {
                        location.setItems(null, selectItems);
                        location.setSelected((Integer) args);
                    }
                }
            }
        }));

        CommonService.App.get().getCompanyCustomFields(ViewName.PlacementItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                AddPlacementView.super.onInitialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                drawItemTable();
            }
        });
        return null;
    }

    protected void registerFields() {

        String placement_add_edit_view = "placement_add_edit_view_";

        //Number
        placementCode = new Numbering(false);
        placementCode.addStyleName(DEFAULT_WIDTH);
        placementCode.ensureDebugId(placement_add_edit_view + "placement_code");
        if (objectID != null) {
            placementCode.getTxtPrefix().setWidth("100%");
        }

        //Date offered
        dateOffered = new DatePicker(DateUtil.resetTime(new Date())/*, true*/);
        dateOffered.setDate(DateUtil.resetTime(new Date()));
        dateOffered.addStyleName(DEFAULT_WIDTH);
        dateOffered.getElement().setId(placement_add_edit_view + "date_offered");

        //Department
        departmentContainer = new Div();
        department = new DepartmentLookUp();
        department.addStyleName(DEFAULT_WIDTH);
        department.getElement().setId(placement_add_edit_view + "department");
        department.getSuggestBox().addSelectionHandler(event -> {
            updateLocation(department);
        });
        departmentContainer.add(department);

        //Position
        positionContainer = new Div();
        position = new PositionLookUp();
        position.addStyleName(DEFAULT_WIDTH);
        position.getElement().setId(placement_add_edit_view + "position");
        position.getSuggestBox().addSelectionHandler(event -> {
            setPositionItems();
        });
        positionContainer.add(position);

        // planned place count, head count
        inputGroup = new Div("input-group");

        Div placeCount = new Div("input-group-append");
        Span vacantPlaceCountTxt = new Span(wfmStrings.planned());
        vacantPlaceCountTxt.addStyleName("input-group-text");
        placeCount.add(vacantPlaceCountTxt);
        inputGroup.add(placeCount);

        plannedPlaceCount = new TextBox();
        plannedPlaceCount.addStyleName(DEFAULT_WIDTH);
        plannedPlaceCount.setEnabled(false);
        inputGroup.add(plannedPlaceCount);

        Div headCount = new Div("input-group-append");
        Span headCountTxt = new Span(wfmStrings.headCount());
        headCountTxt.addStyleName("input-group-text");
        headCount.add(headCountTxt);
        inputGroup.add(headCount);

        this.headCount = new TextBox();
        this.headCount.addStyleName(DEFAULT_WIDTH);
        this.headCount.setEnabled(false);
        inputGroup.add(this.headCount);

        //location
        locationContainer = new AdvancedInputGroup();
        location = new LocationLookUpWithCode();
        location.getElement().setId(placement_add_edit_view + "location");
//        locationContainer.setAppender("ficon--plus");
//        locationContainer.appenderClickHandler(() -> goTo("location|add/add"));
        locationContainer.add(location);

        //Project
        project = new CRMLookUp(LookUpConstants.PROJECT);
        project.showClearButton();
        project.setFullSearch(true);
        project.addStyleName(DEFAULT_WIDTH);
        project.getElement().setId(placement_add_edit_view + "project");

        //Candidate
        candidate = new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE);
        candidate.setLinkCommand(()->{
            CandidateQuickValidate candidateQuickValidate = new CandidateQuickValidate(true);
            candidateQuickValidate.show();
        });
        addFormListeners();
        candidate.addStyleName(DEFAULT_WIDTH);
        candidate.addStyleName("file--AddPlacementView");
        candidate.getElement().setId(placement_add_edit_view + "candidate");
        candidate.getSuggestBox().addSelectionHandler(event -> setVacancyItems());

        //New candidate
        Widget newCandidateBox = inputGroupPrependedRadioCheckbox();

        //Attachment
        fileUpload = new GeneralFileUpload(F_PLACEMENT, objectID, true, objectID, null);
        fileUpload.getPanel().getElement().setId(placement_add_edit_view + "attachment");

        //Notes
        noteWidget = new NoteWidget(objectID, PLACEMENT);
        noteWidget.getTextBox().getTextArea().getElement().setId(placement_add_edit_view + "notes");

        //Vacancies
        vacancies = new MultiSelectVacancyLookUp();
        vacancies.getElement().setId(placement_add_edit_view + "vacancies");

        //Number
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE) != null) {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_CODE, placementCode, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).isRequired()));
            placementCode.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).isInformation()) {
                new KpiToolTip(placementCode, formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_CODE, placementCode, getTitle(wfmStrings.number(), true));
        }

        //Candidate
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE) != null) {
            addField(CustomFormConstants.PLACEMENT.CANDIDATE, newCandidateBox, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).getTitle() : wfmStrings.candidate(), formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).isRequired()));
            candidate.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).isInformation()) {
                new KpiToolTip(candidate, formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PLACEMENT.CANDIDATE, newCandidateBox, getTitle(wfmStrings.candidate(), true));
        }

        //Date offered
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED) != null) {
            addField(CustomFormConstants.PLACEMENT.DATE_OFFERED, dateOffered, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getTitle() : hrmsStrings.dateOffered(), formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).isRequired()));
            dateOffered.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).isInformation()) {
                new KpiToolTip(dateOffered, formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getInformationText());
            }


        } else {
            addField(CustomFormConstants.PLACEMENT.DATE_OFFERED, dateOffered, getTitle(hrmsStrings.dateOffered(), true));
        }

        //Department
//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT) != null) {
//            addField(CustomFormConstants.PLACEMENT.DEPARTMENT, department, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isRequired()));
//            department.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isDisabled());
//
//            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isInformation()) {
//                new KpiToolTip(department, formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getInformationText());
//            }
//
//        } else {
//            addField(CustomFormConstants.PLACEMENT.DEPARTMENT, department, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), true));
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT) != null) {
            addField(CustomFormConstants.PLACEMENT.DEPARTMENT, departmentContainer, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isRequired()));
            department.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isDisabled());
        } else {
            addField(CustomFormConstants.PLACEMENT.DEPARTMENT, departmentContainer, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), true));
        }

        //Position
//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION) != null) {
//            addField(CustomFormConstants.PLACEMENT.POSITION, position, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).getTitle() : wfmStrings.position(), formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isRequired()));
//            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isInformation()) {
//                new KpiToolTip(position, formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).getInformationText());
//            }
//
//        } else {
//            addField(CustomFormConstants.PLACEMENT.POSITION, position, getTitle(wfmStrings.position()));
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION) != null) {
            addField(CustomFormConstants.PLACEMENT.POSITION, positionContainer, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).getTitle() : wfmStrings.position(), formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isRequired()));
        } else {
            addField(CustomFormConstants.PLACEMENT.POSITION, positionContainer, getTitle(wfmStrings.position()));
        }

        //Location
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION) != null) {
            addField(CustomFormConstants.PLACEMENT.LOCATION, locationContainer, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).isRequired()));
            locationContainer.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).isInformation()) {
                new KpiToolTip(location, formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PLACEMENT.LOCATION, locationContainer, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).isRequired()));
        }

        //Place Count
        if (this.formPropertyMap != null && this.formPropertyMap.get(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT) != null) {
            this.addField(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT, inputGroup, this.getTitle(this.formPropertyMap.get(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT).isChanged()
                                    ? this.formPropertyMap.get(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT).getTitle() : wfmStrings.planned() + "/ " + wfmStrings.headCount(),
                            this.formPropertyMap.get(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT).isRequired()), false,
                    formPropertyMap.get(PLANNED_PLACE_COUNT).isInformation());
            if (formPropertyMap.get(PLANNED_PLACE_COUNT).isInformation()) {
                new KpiToolTip(inputGroup, formPropertyMap.get(PLANNED_PLACE_COUNT).getInformationText());
            }

        } else {
            this.addField(CustomFormConstants.PLACEMENT.PLANNED_PLACE_COUNT, this.inputGroup, wfmStrings.planned() + "/ " + wfmStrings.headCount());
        }

        //Project
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT) != null) {
            addField(CustomFormConstants.PLACEMENT.PROJECT, project, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).getTitle() : Property.get(Constants.PROJECT, wfmStrings.project()), formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).isRequired()));
            project.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).isInformation()) {
                new KpiToolTip(project, formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PLACEMENT.PROJECT, project, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        }

        //Vacancies
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES) != null) {
            addField(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES, vacancies, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).getTitle() : wfmStrings.matchedVacancies(), formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).isRequired()));
            vacancies.setEnabled(!formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).isInformation()) {
                new KpiToolTip(vacancies, formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PLACEMENT.CANDIDATE_VACANCIES, vacancies, getTitle(wfmStrings.matchedVacancies()));
        }

        //Attachment
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_FILE) != null) {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_FILE, fileUpload, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_FILE).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_FILE).getTitle() : wfmStrings.attachments(), formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_FILE).isRequired()));
        } else {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_FILE, fileUpload, wfmStrings.attachments());
        }

        //Note
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE) != null) {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE).isRequired()));
        } else {
            addField(CustomFormConstants.PLACEMENT.PLACEMENT_NOTE, noteWidget, wfmStrings.notes());
        }

        addTitleField(CustomFormConstants.PLACEMENT.BASIC_INFORMATION, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.PLACEMENT.PLACEMENT, wfmStrings.placementDetails());
        addTitleField(CustomFormConstants.PLACEMENT.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);
        setDefaultValues();
        registerSelectionHandlers();
        show();
    }

    private void addButton() {
        boolean buttonsIsVisible = false;
        WfmButton2 saveAsDraftButton = new WfmButton2(wfmStrings.draft(), BTN_DEFAULT_OUTLINE);
        saveAsDraftButton.setVisible(false);
        saveAsDraftButton.getElement().setId("placement_add_view_save_as_draft_button");
        saveAsDraftButton.addClickHandler(click -> save(Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT));

        if (Utils.hasPermission(HRMS_DRAFT_PLACEMENT) && (objectID == null || Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(item.getStatusCode()))) {
            addRightButton(saveAsDraftButton);
            saveAsDraftButton.setVisible(true);
            buttonsIsVisible = true;
        }

        WfmButton2 approveButton = new WfmButton2(wfmStrings.approve(), WfmButton2.BTN_SUCCESS);
        approveButton.setVisible(false);
        approveButton.getElement().setId("placement_add_view_approve_button");
        approveButton.addClickHandler(click -> save(Constants.PLACEMENT_STATUS_APPROVED));

        WfmButton2 rejectButton = new WfmButton2(wfmStrings.reject(), WfmButton2.BTN_REJECT);
        rejectButton.setVisible(false);
        rejectButton.getElement().setId("placement_add_view_reject_button");
        rejectButton.addClickHandler(click -> {
            save(Constants.PLACEMENT_STATUS_REJECTED);
        });

        if (Utils.hasPermission(HRMS_APPROVE_PLACEMENT)) {
            addRightButton(approveButton);
            addRightButton(rejectButton);
            buttonsIsVisible = true;
        }


        WfmButton2 submitBtn = new WfmButton2(wfmStrings.submit(), BTN_PRIMARY);
        submitBtn.setVisible(false);
        submitBtn.getElement().setId("placement_add_view_approve_button");
        submitBtn.addClickHandler(click -> save(Constants.PLACEMENT_STATUS_SUBMITTED));
        if (Utils.hasPermission(HRMS_APPROVE_PLACEMENT)) {
            addRightButton(submitBtn);
            buttonsIsVisible = true;
        }

        if (item.isApproveProcessEnabled()) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddPlacementView.this, (sender, args) -> {
                if (approver.getFirstApproverLookUp() != null) {
                    approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        Integer itemId = item != null ? item.getId() : null;
                        Integer currentUserId = Utils.getUserID();
                        if (currentUserId.equals(itemId)) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitBtn.setVisible(false);
                        } else {
                            submitBtn.setVisible(true);
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                        }
                    });
                    if (approveButton != null && submitBtn != null && approver.getFirstApproverLookUp().getSelectedItem() != null) {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                            approveButton.setVisible(true);
                            rejectButton.setVisible(true);
                            submitBtn.setVisible(false);
                        } else {
                            approveButton.setVisible(false);
                            rejectButton.setVisible(false);
                            submitBtn.setVisible(true);
                        }
                    }
                    if (approver != null && item != null && item.getObjectID() != null) {
                        approver.setEnabled(PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(item.getStatusCode()) || PLACEMENT_STATUS_REJECTED.equals(item.getStatusCode()));
                    }
                }
            });

        } else {
            saveAsDraftButton.setVisible(Utils.hasPermission(HRMS_DRAFT_PLACEMENT));
            approveButton.setVisible(Utils.hasPermission(HRMS_APPROVE_PLACEMENT));
        }

        if (objectID == null && !buttonsIsVisible) {
            Info.warn(wfmStrings.youDontHavePermission());
        }

        if (objectID != null) {
            if (Utils.hasPermission(HRMS_PRINT_PDF_PLACEMENT)) {
                printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
                addRightButton(printPdfSplitButton);
            }

            NoteHistoryWidget noteHistoryWidget = new NoteHistoryWidget(callback -> RecruitmentService.App.get().loadPlacementNoteAndHistory(objectID, callback), false);
            FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
            informer.setInitialClasses("informer-item history-notes-container");

            footer.addToLeftSide(informer);
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        RecruitmentService.App.get().getPlacementItem(objectID, formType, convertFormId, new AbstractAsyncCallback<PlacementItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final PlacementItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = result;
                    addButton();
                    if (item.isApproveProcessEnabled()) {
                        approver = new ChosenApproversWidget(RelationItem.TYPE_PLACEMENT, item.getApprover() != null ? objectID : null);
                        addField(APPROVERS, approver, getTitle(wfmStrings.approver(), true));
                    }
                    if (placementStatusLogic != null) {
                        placementStatusLogic.placementStatusL(item.isShowHireButton());
                    }
                    fillFormWithData();
                    if (objectID == null) {
                        setDefaultValuesByFormProperty();
                    }
                    pdfTool(item);
                    setDefaultValues();
                    setItemTableValues(item.getCustomTableItems());
                });
            }
        });
    }

    protected void fillFormWithData() {
        //Candidate
        if (item.getCandidateID() != null) {
            candidate.setSelected(new SelectItem(item.getCandidateID(), item.getCandidateName()));
        }
        if (objectID == null && candidateID != null) {
            candidate.setSelected(candidateID);
            setVacancyItems();
        }
        if (objectID != null) {
            candidate.setEnabled(false);
        }
        //date offered
        if (item.getDateOffed() != null) {
            dateOffered.setDate(item.getDateOffed());
        }
        //department
        if (item.getDepartmentID() != null) {
            department.setSelected(new SelectItem(item.getDepartmentID(), item.getDepartmentName()));
        }
        //location
        if (item.getLocationName() != null) {
            location.setSelected(item.getLocationName());
        }
        //placement code
        if (item.getNumberData() != null) {
            placementCode.setNumberData(item.getNumberData());
        }
        //position
        if (item.getPositionID() != null) {
            position.setSelected(new SelectItem(item.getPositionID(), item.getPositionName()));
        }
        //vacancies
//        if (item.getVacancies() != null) {
//            setVacancies(item.getVacancies());
//        }
        if (item.getNotes() != null) {
            for (HistoryListItem note : item.getNotes()) {
                noteWidget.createNote(note, true);
            }
        }
        if (item.getPositionID() != null) {
            position.setSelected(item.getPositionID());
            plannedPlaceCount.setText(item.getPlannedPlaceCount() != null ? item.getPlannedPlaceCount() : "0");
            headCount.setText(item.getHeadCount() != null ? item.getHeadCount() : "0");
        }
        //project
        if (item.getProjectID() != null) {
            project.setSelected(new SelectItem(item.getProjectID(), item.getProjectName()));
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());

        AtomicBoolean firstClick = new AtomicBoolean(true);
        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }

        });

        link.setBadgeCount(item.getRelations().size());
        if (objectID != null) {
            boolean interval = item.getCandidateType() != null && item.getCandidateType().equals(Constants.INTERVAL_CANDIDATE);
            isIntervalCandidate = interval;
            radioButton.setValue(item.getCandidateType() == null || item.getCandidateType().equals(Constants.SIMPLE_CANDIDATE));
            radioButton2.setValue(interval);
        }
    }

    private void updateLocation(DepartmentLookUp department) {
        if (department.getSelectedItem() != null) {
            AllInOneService.App.get().getLocationByDepartmentId(department.getSelectedItemID(), new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem result) {
                    location.removeFromParent();
                    locationContainer.remove(location);
                    location = new LocationLookUpWithCode();
                    location.setSelected(result);
                    locationSelectionHandler(location, department.getSelectedItem());
                    locationContainer.add(location);
                    position.removeFromParent();
                    positionContainer.remove(position);
                    position = new PositionLookUp();
                    position.ensureDebugId("employee_position");
                    if (department.getSelectedItem() != null) {
                        position.getFilterParametrs().setDepartmentId(department.getSelectedItemID());
                    }
                    if (result != null) {
                        position.getFilterParametrs().setLocationId(result.getId());
                    }
                    position.getSuggestBox().addSelectionHandler(e -> {
                        setPositionItems();
                    });
                    positionContainer.add(position);
                }
            });
        }

    }

    private void registerSelectionHandlers() {
        department.getSuggestBox().addSelectionHandler(event -> {
            updateLocation(department);
        });

        locationSelectionHandler(location, null);

        position.getSuggestBox().addSelectionHandler(e -> {
            setPositionItems();
        });
    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode, SelectItem departmentLookup) {
        if (locationLookUpWithCode.getSelectedItem() != null) {
            setLocationItems(locationLookUpWithCode, departmentLookup, true);
        }
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            setLocationItems(locationLookUpWithCode, departmentLookup, false);
        });

        locationLookUpWithCode.getTextBox().addKeyDownHandler(e -> {
            department.removeFromParent();
            departmentContainer.remove(department);
            department = new DepartmentLookUp();
            departmentContainer.add(department);
            position.removeFromParent();
            positionContainer.remove(position);
            position = new PositionLookUp();
            position.getSuggestBox().addSelectionHandler(event -> {
                setPositionItems();
            });
            positionContainer.add(position);
        });

        department.getTextBox().addKeyDownHandler(e -> {
            position.removeFromParent();
            positionContainer.remove(position);
            position = new PositionLookUp();
            position.getSuggestBox().addSelectionHandler(event -> {
                setPositionItems();
            });
            positionContainer.add(position);
        });
    }

    private void setLocationItems(LocationLookUpWithCode locationLookUpWithCode, SelectItem departmentItem, boolean fromDepartment) {
        department.removeFromParent();
        departmentContainer.remove(department);
        department = new DepartmentLookUp(locationLookUpWithCode.getSelectedItemID());
        if (fromDepartment) {
            department.setSelected(departmentItem);
        }
        department.getSuggestBox().addSelectionHandler(event -> {
            updateLocation(department);
        });

        departmentContainer.add(department);

        position.removeFromParent();
        positionContainer.remove(position);
        position = new PositionLookUp();
        position.getFilterParametrs().setLocationId(locationLookUpWithCode.getSelectedItemID());
        if (departmentItem != null && fromDepartment) {
            position.getFilterParametrs().setDepartmentId(departmentItem.getId());
        }
        position.getSuggestBox().addSelectionHandler(event -> {
            setPositionItems();
        });
        positionContainer.add(position);
    }

    private void setPositionItems() {
        AllInOneService.App.get().getPositionItems(position.getSelectedItemID(), new AsyncCallback<PositionsSelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(PositionsSelectItem result) {
                SelectItem selectedPosition = position.getSelectedItem();
                location.setSelected(result.getLocation());
                department.removeFromParent();
                departmentContainer.remove(department);
                department = result.getLocation() != null ? new DepartmentLookUp(result.getLocation().getId()) : new DepartmentLookUp();
                department.setSelected(result.getDepartment());
                department.getSuggestBox().addSelectionHandler(event -> {
                    updateLocation(department);
                });
                department.setSelected(result.getDepartment());
                departmentContainer.add(department);
                position.removeFromParent();
                positionContainer.remove(position);
                position = new PositionLookUp();
                position.getFilterParametrs().setLocationId(result.getLocation() != null ? result.getLocation().getId() : null);
                position.getFilterParametrs().setDepartmentId(result.getDepartment() != null ? result.getDepartment().getId() : null);
                position.getSuggestBox().addSelectionHandler(event -> {
                    setPositionItems();
                });
                position.setSelected(selectedPosition);
                positionContainer.add(position);
                plannedPlaceCount.setText(result.getPlannedPlaceCount());
                headCount.setText(result.getHeadCount());
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PLACEMENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void addButtons() {
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);
    }

    protected void getEmployeesMaxCount() {
        ReportService.App.get().getEmployeesMaxCount(null, new AbstractAsyncCallback<Integer[]>() {
            @Override
            public void success(Integer[] result) {
                maxNoAccessEmp = result[Constants.NO_ACCESS];
            }
        });
    }

//    private void setVacancies(List<SelectItem> vacancyList) {
//        vacancies.clear();
//        if (vacancyList != null && vacancyList.size() > 0) {
//            vacancies.setItems(null,vacancyList.toArray(new SelectItem[]{}));
//        }
//    }

    public void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.PLACEMENT_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        AddPlacementView.this.configMap = result;

                        String fieldID = configMap.getKey();
                        ColumnConfigs[] configs = configMap.getValue();
                        if (configs == null || configs.length == 0) {
                            continue;
                        }

                        Map<String, ColumnConfigs> columnsMap = Stream.of(configs)
                                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

                        EditableTable editableTable = new EditableTable(getCustomColumns(columnsMap), true, true);

                        editableTableMap.put(fieldID, editableTable);

                        editableTable.setLayoutData(fieldID);
                        editableTable.setDraggable(true);
                        editableTable.setWidth("100%");
                        editableTable.setListener(new EditableTableListener() {
                            @Override
                            public void addRow() {
                                editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                            }

                            @Override
                            public void removeRow() {

                            }
                        });
                        for (int i = 0; i < 3; i++) {
                            editableTable.addRow(getCustomWidgets(new CustomTableRpc(), fieldID));
                        }
                        addField(fieldID, editableTable, null, true);
                    }
                }
            }
        });
    }

    //Custom Item table
    private Widget[] getCustomWidgets(CustomTableRpc item, String fieldID) {
        int index = 0;

        Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(fieldID))
                .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

        final Widget[] widgets = new Widget[columnsMap.keySet().size()];
        for (String columnCode : columnsMap.keySet()) {
            if (itemCustomCFs.containsKey(fieldID)) {

                CompanyCustomFieldItem cfItem = getCustomFieldItem(itemCustomCFs.get(fieldID), columnCode);

                if (UI_TYPE_TEXTBOX.equals(cfItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(cfItem.getUiType()) || UI_TYPE_URL.equals(cfItem.getUiType())) {
                    CustomTextBoxField t = new CustomTextBoxField(cfItem);
                    t.setWidth("100%");
                    if (DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                        t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
                        Validation.addNumericKeyboardListener(t, 5, true);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_PERCENTAGE.equals(cfItem.getUiType())) {
                    CustomPercentageField t = new CustomPercentageField(cfItem);
                    t.setWidth("100%");
                    t.setAlignment(ValueBoxBase.TextAlignment.RIGHT);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        t.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    t.setTitle(columnCode);
                    widgets[index++] = t;
                } else if (UI_TYPE_DROPDOWN.equals(cfItem.getUiType())) {
                    CustomDropDownField d = new CustomDropDownField(cfItem);
                    d.setWidth("100%");
                    if (cfItem.getPredefinedValues() != null) {
                        SelectItem[] sItems = new SelectItem[cfItem.getPredefinedValues().length];
                        int x = 0;
                        for (String s : cfItem.getPredefinedValues()) {
                            sItems[x] = new SelectItem(x, s);
                            x++;
                        }
                        d.setItems(sItems);
                    }
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        d.setSelectedByValue(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (UI_TYPE_DATEPICKER.equals(cfItem.getUiType())) {
                    com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker d = new com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker(cfItem);
                    d.setWidth("100%");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        d.setDate(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    d.setTitle(columnCode);
                    widgets[index++] = d;
                } else if (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfItem.getUiType())) {
                    CustomDateTime customDateTime = new CustomDateTime(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode) && item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue() != null) {
                        customDateTime.setDateTime(item.getCustomFieldValuesAsMap().get(columnCode).getFieldDateNonConvertedValue().getNonConvertedDate());
                    }
                    customDateTime.setTitle(columnCode);
                    widgets[index++] = customDateTime;

                } else if (Constants.UI_TYPE_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        textAreaField.setText(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    textAreaField.setTitle(columnCode);
                    widgets[index++] = textAreaField;
                } else if (Constants.UI_TYPE_HTML_TEXTAREA.equals(cfItem.getUiType())) {
                    CustomHTMLTextAreaField htmlTextAreaField = new CustomHTMLTextAreaField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        htmlTextAreaField.setData(item.getCustomFieldValuesAsMap().get(columnCode).getFieldStringValue());
                    }
                    htmlTextAreaField.setTitle(columnCode);
                    widgets[index++] = htmlTextAreaField;
                } else if (Constants.UI_TYPE_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }
                    lookup.setTitle(columnCode);
                    widgets[index++] = lookup;
                } else if (Constants.UI_TYPE_CURRENCY.equals(cfItem.getUiType())) {
                    CustomFieldCurrencyWidget currencyWidget = new CustomFieldCurrencyWidget(cfItem, "CustomForm");
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getSelectedId() != null) {
                            currencyWidget.setCurrency(new SelectItem(customFieldItem.getSelectedId(), customFieldItem.getFieldStringValue()));
                        }
                    }

                    currencyWidget.setTitle(columnCode);
                    widgets[index++] = currencyWidget;
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldMultiLookUpField multiLookUp = new CustomFieldMultiLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        ArrayList<SelectItem> list = new ArrayList<>();
                        if (customFieldItem.getSelectItems() != null && customFieldItem.getSelectItems().size() > 0) {
                            multiLookUp.setSelectedItems(list);
                        }
                    }

                    multiLookUp.setTitle(columnCode);
                    widgets[index++] = multiLookUp;
                } else if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(cfItem.getUiType())) {

                    CustomFieldLookUpField lookup = new CustomFieldLookUpField(cfItem);
                    CustomTextAreaField textAreaField = new CustomTextAreaField(cfItem);

                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem.getItem() != null) {
                            lookup.addItem(new SelectItem(customFieldItem.getItem().getId(), customFieldItem.getItem().getName()));
                            textAreaField.setText(customFieldItem.getItem().getDescription());
                        }
                    }
                    lookup.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

                        if (lookup.getSelectedItem() != null && lookup.getSelectedItem().getId() != null) {
                            AllInOneService.App.get().getProductDescription(lookup.getSelectedItem().getId(), new AbstractAsyncCallback<String>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    super.failure(throwable);
                                }

                                @Override
                                public void success(String result) {
                                    if (result != null) {
                                        textAreaField.setText(result);
                                        lookup.getSelectedItem().setDescription(result);
                                        int currentRowId = editableTableMap.get(fieldID).getGrid().getCurrentRow();
                                        CustomCell cel = (CustomCell) editableTableMap.get(fieldID).getColumnCellWidgetById(currentRowId, columnCode + "_DESCRIPTION");
                                        cel.InActive();
                                    }
                                }
                            });
                        }
                    });

                    lookup.setTitle(columnCode);

                    textAreaField.setTitle(wfmStrings.description());
                    widgets[index++] = lookup;
                    widgets[index++] = textAreaField;

                } else if (Constants.TYPE_ENTITY_LOOKUP.equals(cfItem.getUiType())) {
                    CustomFieldEntityLookUpField entityLookUp = new CustomFieldEntityLookUpField(cfItem);
                    if (item.getCustomFieldValuesAsMap() != null && item.getCustomFieldValuesAsMap().containsKey(columnCode)) {
                        CompanyCustomFieldItem customFieldItem = item.getCustomFieldValuesAsMap().get(columnCode);
                        if (customFieldItem != null && customFieldItem.getFieldStringValue() != null) {
                            Integer id = null;
                            try {
                                id = Integer.valueOf(customFieldItem.getFieldStringValue());
                            } catch (final NumberFormatException e) {
                                e.printStackTrace();
                            }
                            if (id != null && customFieldItem.getQueryItems() != null) {
                                for (final SelectItem selectItem : customFieldItem.getQueryItems()) {
                                    if (selectItem.getId().equals(id)) {
                                        entityLookUp.setSelected(new SelectItem(id, selectItem.getName()));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    entityLookUp.setTitle(columnCode);
                    widgets[index++] = entityLookUp;
                }
            }
        }
        return widgets;
    }

    private ColumnConfig[] getCustomColumns(Map<String, ColumnConfigs> columnsMap) {
        ColumnConfig[] columns = new ColumnConfig[columnsMap.keySet().size()];
        int i = 0;
        for (String cc : columnsMap.keySet()) {
            switch (cc) {
                case ItemTableConstants.PRODUCT:
                    columns[i++] = new ColumnConfig(LookUpCell.class, ItemTableConstants.PRODUCT, wfmStrings.item(), 100, columnsMap.get(cc).isRequired());
                    break;
                case ItemTableConstants.DESCRIPTION:
                    columns[i++] = new ColumnConfig(CustomCell.class, ItemTableConstants.DESCRIPTION, wfmStrings.description(), 100, columnsMap.get(cc).isRequired());
                    break;
                default:
                    ColumnConfig columnConfig = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), 100, columnsMap.get(cc).isRequired(), true);
                    if (columnsMap.get(cc).getWidth() != null && columnsMap.get(cc).getWidth() > 0) {
                        columnConfig.setWidth(columnsMap.get(cc).getWidth());
                        columnConfig.setPixel(false);
                        columnConfig.setForceWidthInPercent(true);
                    }
                    if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(columnsMap.get(cc).getUiType())) {
                        ColumnConfig columnConfigItem = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode(), columnsMap.get(cc).getTitle(), columnsMap.get(cc).getWidth() * 60 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigItem.setPixel(false);
                        columnConfigItem.setForceWidthInPercent(true);
                        columns[i++] = columnConfigItem;

                        ColumnConfig columnConfigDescription = new ColumnConfig(CustomCell.class, columnsMap.get(cc).getCode() + "_DESCRIPTION", wfmStrings.description(), columnsMap.get(cc).getWidth() * 40 / 100, columnsMap.get(cc).isRequired(), true);
                        columnConfigDescription.setPixel(false);
                        columnConfigDescription.setForceWidthInPercent(true);
                        columns[i++] = columnConfigDescription;
                    } else {
                        columns[i++] = columnConfig;
                    }
                    break;
            }
        }
        return columns;
    }

    private CompanyCustomFieldItem getCustomFieldItem(List<CompanyCustomFieldItem> companyCustomFieldItems, String columnCode) {
        return companyCustomFieldItems.stream()
                .filter(item -> columnCode.equals(item.getColumnCode()))
                .findFirst()
                .orElse(new CompanyCustomFieldItem());
    }

    protected void save(String status) {
        enableButton(false);
        if (validate(Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(status))) {
            enableButton(true);
            return;
        }
        setValues();
        //register save logic
        LoadingPanel.loading(true);
        item.setStatusCode(status);
        RecruitmentService.App.get().savePlacement(item, null, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PLACEMENT_ADD_EDIT, result, AddPlacementView.this);
                closeTab();
                if (objectID != null && !Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(status)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("placement|summary/" + objectID + "/true", item.getCandidateName());
                }
            }
        });
    }

    protected void setVacancyItems() {
        Integer candidateID = candidate.getSelectedItem() != null ? candidate.getSelectedItem().getId() : null;
        DocumentsService.App.get().getFileResources(Constants.F_CANDIDATE, candidateID, candidateID, new AbstractAsyncCallback<ArrayList<FileResource>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(ArrayList<FileResource> fileResources) {
                FileResource[] cloneFileResource = new FileResource[fileResources.size()];
                int i = 0;
                for (FileResource item : fileResources) {
                    cloneFileResource[i] = item;
                    i++;
                }
                fileUpload.addAdditionalAttachments(cloneFileResource, true);
            }
        });
//        RecruitmentService.App.get().getPlacementVacancies(objectID, candidateID, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
//            @Override
//            public void failure(Throwable throwable) {
//                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
//            }
//
//            @Override
//            public void success(ArrayList<SelectItem> result) {
//                if (result != null && result.size() > 0) {
//                    vacancies.setItems( null,result.toArray(new SelectItem[]{}));
//                } else {
//                    vacancies.clear();
//                }
//            }
//        });
        RecruitmentService.App.get().getCandidateData(candidateID, new AbstractAsyncCallback<PlacementItem>() {
            @Override
            public void success(PlacementItem result) {
                candidate.setSelected(result.getCandidate());
                department.setSelected(result.getDepartment());
                location.setSelected(result.getLocation());
                position.setSelected(result.getPosition());
                vacancies.setSelectedItems(result.getVacancies());
            }
        });

        RecruitmentService.App.get().getCandidateProject(candidateID, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void success(SelectItem result) {
                if (result != null) {
                    project.setSelected(result);
                } else {
                    project.clearAndClearItems();
                }
            }
        });

//        getLinkingUtil().getAddLinkSideNavBox().addItem(new RelationItem(null, candidate.getSelectedItem().getId(), RelationItem.TYPE_CANDIDATE, candidate.getSelectedItem().getName(), null, null, null));

    }

    private void setValues() {
        item.setObjectID(objectID);
        item.setCandidateID(candidate.getSelectedItem() != null ? candidate.getSelectedItem().getId() : null);
        item.setDateOffed(dateOffered.getDate());
        item.setVacancies(vacancies.getSelectedItems());

        item.setDepartmentID(department.getSelectedItem() != null ? department.getSelectedItem().getId() : null);
        item.setLocationID(location.getSelectedItem() != null ? location.getSelectedItem().getId() : null);
        if (position.isSelected()) {
            item.setPositionID(position.getSelectedItemID());
        }
        item.setProjectID(project.getSelectedItemID());
        item.setAttachments(fileUpload.getAttachedFiles());
        item.setNotes(noteWidget.getNewNotesToSave());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        item.setNumberData(placementCode.getNumberData(true));
        item.setPlacementCode(placementCode.getNumberData(true).getNumberString());
        if (getLinkingUtil().getAddLinkSideNavBoxSecondWay() != null) {
            item.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        if (getCustomObjectData() != null) {
            item.setCustomTableItems(getCustomObjectData());
        }
        if (item.isApproveProcessEnabled()) {
            item.setApprovers(approver.getChosenApprovers());
        }
        item.setCandidateType(isIntervalCandidate ? Constants.INTERVAL_CANDIDATE : Constants.SIMPLE_CANDIDATE);
    }

    private HashMap<String, ArrayList<CustomTableRpc>> getCustomObjectData() {
        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
        for (Map.Entry<String, EditableTable> mapTable : editableTableMap.entrySet()) {

            String uuid = mapTable.getKey();

            List<CompanyCustomFieldItem> itemCustom = itemCustomCFs.get(uuid);

            Map<String, ColumnConfigs> columnsMap = Stream.of(configMap.get(uuid))
                    .collect(Collectors.toMap(ColumnConfigs::getCode, x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            EditableTable productTable = mapTable.getValue();
            ArrayList<CustomTableRpc> tableItem = new ArrayList<>();
            ArrayList<CompanyCustomFieldItem> resultItemList;
            for (int i = 0; i < productTable.getGrid().getRowCount(); i++) {
                CustomTableRpc result = new CustomTableRpc();
                resultItemList = new ArrayList<>();
                for (String columnCode : columnsMap.keySet()) {
                    if (itemCustomCFs.containsKey(uuid)) {
                        Object customFieldValue = null;
                        Integer customFieldValueId = null;
                        SelectItem itemValue = null;
                        if (getCustomFieldItem(itemCustom, columnCode).isRequired()) {
                            boolean isItemTableRequared = true;
                        }
                        if (UI_TYPE_TEXTBOX.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomTextBoxField t = (CustomTextBoxField) productTable.getColumnById(i, columnCode);
                            if (t.getText() != null && !t.getText().isEmpty()) {
                                customFieldValue = t.getText();
                            }
                        }
                        if (Constants.UI_TYPE_PERCENTAGE.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomPercentageField percentageField = (CustomPercentageField) productTable.getColumnById(i, columnCode);
                            if (percentageField != null && !percentageField.getText().isEmpty()) {
                                customFieldValue = percentageField.getText();
                            }

                        } else if (UI_TYPE_DROPDOWN.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomDropDownField t = (CustomDropDownField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                            }
                        } else if (UI_TYPE_DATEPICKER.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker t = (com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker) productTable.getColumnById(i, columnCode);
                            if (t.getDate() != null) {
                                customFieldValue = t.getDate();
                            }
                        } else if (UI_TYPE_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUpField t = (CustomFieldLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItem() != null) {
                                customFieldValue = t.getSelectedItem().getName();
                                customFieldValueId = t.getSelectedItem().getId();
                            }
                        } else if (UI_TYPE_CURRENCY.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldCurrencyWidget t = (CustomFieldCurrencyWidget) productTable.getColumnById(i, columnCode);
                            if (t.getCurrencyID() != null) {
                                customFieldValue = t.getCurrencyName();
                                customFieldValueId = t.getCurrencyID();
                            }
                        } else if (UI_TYPE_MULTI_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldMultiLookUpField t = (CustomFieldMultiLookUpField) productTable.getColumnById(i, columnCode);
                            if (t.getSelectedItems() != null && t.getSelectedItems().size() > 0) {
                                customFieldValue = t.getSelectedItems();
                            }
                        } else if (UI_TYPE_ITEM_WITH_DESCRIPTION.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldLookUp item = (CustomFieldLookUp) productTable.getColumnById(i, columnCode);
                            CustomTextAreaField desc = (CustomTextAreaField) productTable.getColumnById(i, columnCode + "_DESCRIPTION");
                            if (item.getSelectedItem() != null) {
                                itemValue = new SelectItem(item.getSelectedItemID(), item.getSelectedItem().getName(), desc.getText());
                            }
                        } else if (TYPE_ENTITY_LOOKUP.equals(getCustomFieldItem(itemCustom, columnCode).getUiType())) {
                            CustomFieldEntityLookUpField lookUp = (CustomFieldEntityLookUpField) productTable.getColumnById(i, columnCode);
                            if (lookUp.getSelectedItem() != null) {
                                customFieldValue = lookUp.getSelectedItem().getId().toString();
                            }
                        }
                        CompanyCustomFieldItem companyCustomFieldItem = getCustomFieldItem(itemCustom, columnCode);
                        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
                        resultItem.setObjectId(companyCustomFieldItem.getObjectId());
                        resultItem.setDataType(companyCustomFieldItem.getDataType());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());
                        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
                        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
                        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
                        resultItem.setFileUploadFieldId(companyCustomFieldItem.getFileUploadFieldId());
                        resultItem.setUiType(companyCustomFieldItem.getUiType());

                        if (customFieldValue != null) {
                            if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType())) {
                                resultItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) customFieldValue));
                            } else {
                                resultItem.setFieldStringValue((String) customFieldValue);
                            }
                            if (customFieldValueId != null) {
                                resultItem.setSelectedId(customFieldValueId);
                            }
                        }
                        if (itemValue != null) {
                            resultItem.setItem(itemValue);
                        }
                        resultItemList.add(resultItem);
                    }
                }
                result.setUuid(uuid);
                result.setItemCustomFields(resultItemList);
                tableItem.add(result);
            }
            map.put(uuid, tableItem);
        }
        return map;
    }

    private void setItemTableValues(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
        if (tableItems != null && tableItems.size() > 0) {
            for (Map.Entry map : tableItems.entrySet()) {
                String uuid = (String) map.getKey();
                if (editableTableMap.get(uuid) != null) {
                    editableTableMap.get(uuid).removeAllRows();
                }
                for (CustomTableRpc item : (List<CustomTableRpc>) map.getValue()) {
                    if (editableTableMap.get(uuid) != null) {
                        editableTableMap.get(uuid).addRow(getCustomWidgets(item, uuid));
                    }
                }
            }
        }
    }

    public void pdfTool(final PlacementItem result) {
        if (this.printPdfSplitButton == null) {
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
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/placementInfoPDFHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    private boolean validate(boolean isDraft) {
        int errors = 0;
        clearErrorStyle();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).isRequired()) {
            errors += markAsError(CustomFormConstants.PLACEMENT.CANDIDATE, candidate, !Validation.validateLookUpRequired(candidate));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).isRequired() && !isDraft) {
            errors += markAsError(CustomFormConstants.PLACEMENT.DATE_OFFERED, dateOffered, !Validation.validateDate(dateOffered, new HTML(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).isRequired() && !isDraft) {
            errors += markAsError(CustomFormConstants.PLACEMENT.DEPARTMENT, department, !Validation.validateLookUpRequired(department));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).isRequired() && !isDraft) {
            errors += markAsError(CustomFormConstants.PLACEMENT.POSITION, position, position.getSelectedItemID() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).isRequired() && !isDraft) {
            errors += markAsError(CustomFormConstants.PLACEMENT.PROJECT, project, project.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).isRequired() && !isDraft) {
            errors += markAsError(CustomFormConstants.PLACEMENT.LOCATION, location, !Validation.validateLookUpRequired(location));
        }
        if (formPropertyMap != null && formPropertyMap.get(APPROVER) != null && formPropertyMap.get(APPROVER).isRequired() && item != null && item.isApproveProcessEnabled() && !approver.isValid()) {
            errors++;
        }

        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    public void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).getDefaultValue() != null) {
            candidate.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).getSelectedId(), formPropertyMap.get(CustomFormConstants.PLACEMENT.CANDIDATE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                dateOffered.setDate(currentDate);
            } else {
                dateOffered.setDate(new Date(formPropertyMap.get(CustomFormConstants.PLACEMENT.DATE_OFFERED).getDefaultValue()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).getDefaultValue() != null) {
            position.setSelected(formPropertyMap.get(CustomFormConstants.PLACEMENT.POSITION).getSelectedId());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getDefaultValue() != null) {
            department.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PLACEMENT.DEPARTMENT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).getDefaultValue() != null) {
            project.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PLACEMENT.PROJECT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION) != null && formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).getDefaultValue() != null) {
            location.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).getSelectedId(), formPropertyMap.get(CustomFormConstants.PLACEMENT.LOCATION).getDefaultValue()));
        }
    }

    private Widget inputGroupPrependedRadioCheckbox() {
        Div inputGroup = new Div("input-group");
        Div prepend = new Div("input-group-prepend");
        inputGroup.add(prepend);

        Div prependedContent2 = new Div("input-group-text");
        radioButton2 = new KpiRadioButton("radio_button_name", wfmStrings.employee());
        radioButton2.setValue(objectID == null);
        prependedContent2.add(radioButton2);
        prepend.add(prependedContent2);

        Div prependedContent1 = new Div("input-group-text");
        radioButton = new KpiRadioButton("radio_button_name", wfmStrings.candidate());
        radioButton.setValue(objectID != null);
        prependedContent1.add(radioButton);
        prepend.add(prependedContent1);


        radioButton.addValueChangeHandler(e -> {
            if (radioButton.getValue()) {
                isIntervalCandidate = false;
                inputGroup.remove(candidate);
                candidate = new CRMLookUp(LookUpConstants.CANDIDATE);
                candidate.setLinkCommand(()->{
                    CandidateQuickValidate candidateQuickValidate = new CandidateQuickValidate(true);
                    candidateQuickValidate.show();
                });
                inputGroup.add(candidate);
                candidate.getSuggestBox().addSelectionHandler(event -> setVacancyItems());
            }
        });
        radioButton2.addValueChangeHandler(e -> {
            if (radioButton2.getValue()) {
                isIntervalCandidate = true;
                inputGroup.remove(candidate);
                vacancies.clear();
                candidate = new CRMLookUp(LookUpConstants.HRMS_EMPLOYEE);
                candidate.setLinkCommand(()->{
                    CandidateQuickValidate candidateQuickValidate = new CandidateQuickValidate(true);
                    candidateQuickValidate.show();
                });
                inputGroup.add(candidate);
            }
        });

        inputGroup.add(candidate);
        return inputGroup;
    }

    public interface PlacementStatusLogic {
        void placementStatusL(boolean showHireButton);
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddPlacementView.this) {
                @Override
                protected boolean isActionEditing() {
                    return false;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_PLACEMENT;
                }

                @Override
                public String getRelationName() {
                    return item.getRelationName();
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CANDIDATE_ADD, candidate, (sender, args) -> setClientData((Integer) args));
    }

    private void setClientData(final Integer id) {
        ContactService.App.get().getContact(id, false, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final ContactListItem o) {
                radioButton.setValue(true);
                candidate.setType(LookUpConstants.CANDIDATE_ID);
                candidate.setSelected(new SelectItem(o.getObjectId(), o.getFirstName() + " " + o.getLastName()));
            }
        });
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