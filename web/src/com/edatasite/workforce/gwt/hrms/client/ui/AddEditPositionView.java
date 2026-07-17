package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.POSITIONS.POSITION_CODE;

public class AddEditPositionView extends CustomForm2 implements Constants, FittedContent, Colapse {

    private static String TEST_CODE_PREFIX = "add_position_view_";
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    // Position data
    private PositionItem position;
    private Integer objectID;
    private Integer positionRefId;
    private Integer basicObjectID;
    private boolean saveAndNew = false;
    private String originalPositionName;
    private Integer originalLocationId;

    // Containers & form properties
    private Div locationContainer;
    private Div departmentContainer;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    // Lookups & selectors
    private LocationLookUpWithCode location;
    private DepartmentLookUp departmentLookUp;
    private ReferenceLookUp positionTitle;
    private ReferenceLookUp type;
    private KpiCellTree requiredSelector;
    private Numbering positionCode;

    // Basic inputs
    private TextBox count;
    private TextBox wageRateBox;
    private TextBox coefficient;
    private TextBox salaryBasis;
    private DataListBox status;
    private DatePicker established;

    // Text areas
    private TextAreaWithSuggestionPopup jobRequirement;
    private TextAreaWithSuggestionPopup responsibilities;
    private TextAreaWithSuggestionPopup description;
    private TextAreaWithSuggestionPopup measuringEmployeePerformance;
    private TextAreaWithSuggestionPopup personalQualities;
    private TextAreaWithSuggestionPopup knowledge;

    // Localization rich editors
    private MultiLanguageRichEditorWidget jobRequirementLocalize;
    private MultiLanguageRichEditorWidget responsibilitiesLocalize;
    private MultiLanguageRichEditorWidget descriptionLocalize;
    private MultiLanguageRichEditorWidget measuringEmployeePerformanceLocalize;
    private MultiLanguageRichEditorWidget personalQualitiesLocalize;
    private MultiLanguageRichEditorWidget knowledgeLocalize;

    // Custom fields
    private FormHasCustomField customFieldUtil;

    private final String userLanguage = Utils.getUserLanguage();


    public AddEditPositionView() {
        super("addposition", wfmStrings.add() + " " + Property.get(POSITION1, wfmStrings.position()));
    }

    public AddEditPositionView(Integer basicObjectID) {
        super("addposition", wfmStrings.add() + " " + Property.get(POSITION1, wfmStrings.position()));
        this.basicObjectID = basicObjectID;
    }

    public AddEditPositionView(String name, String description, String test_code_ID_name, Integer objectID) {
        super(name, description);
        this.TEST_CODE_PREFIX = test_code_ID_name;
        this.objectID = objectID;
    }


    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(
                ViewName.Positions,
                getFormID(),
                new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {

                    @Override
                    public void success(CompanyCfAndPropertyItems result) {
                        LoadingPanel.loading(false);

                        getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                        formPropertyMap = result.getFormPropertyMap();

                        AddEditPositionView.super.onInitialize();
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        super.failure(throwable);
                    }
                }
        );
        return null;
    }

    @Override
    protected void addButtons() {
        if (this.objectID != null) {
            addButton(new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY, event -> save()));
            return;
        }

        MaterialLink save = new MaterialLink(wfmStrings.save());
        save.addClickHandler(event -> {
            this.saveAndNew = false;
            save();
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> {
            this.saveAndNew = true;
            save();
        });

        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        splitButton.addItem(saveAdd);
        addButton(splitButton);
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);

        Integer targetId = (basicObjectID != null) ? basicObjectID : objectID;

        HrmsService.App.get().getPositionForEdit(targetId, "EDIT", new AbstractAsyncCallback<PositionItem>() {
            @Override
            public void success(PositionItem result) {
                LoadingPanel.loading(false);
                positionRefId = result.getPositionRefId();

                fillTable(result);
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());

                descriptionLocalize.setValueMap(result.getDescriptionLocalize());
                jobRequirementLocalize.setValueMap(result.getJobRequirementLocalize());
                responsibilitiesLocalize.setValueMap(result.getResponsibilitiesLocalize());

                measuringEmployeePerformanceLocalize.setValueMap(result.getMeasuringEmployeePerformanceLocalize());
                personalQualitiesLocalize.setValueMap(result.getPersonalQualitiesLocalize());
                knowledgeLocalize.setValueMap(result.getKnowledgeLocalize());
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }
        });

    }

    private void drawLeftMenu() {
        requiredSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 40, Style.Unit.PCT);
                //remove
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new SimpleLinkCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return wfmStrings.delete();
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                selectedDataGrid.addColumn(action, wfmStrings.action());
                selectedDataGrid.setColumnWidth(action, 20, Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });
    }


    @Override
    protected void registerFields() {
        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.setWithoutNullLabel(true);
        status.ensureDebugId(TEST_CODE_PREFIX + "status");

        requiredSelector = new KpiCellTree();
        requiredSelector.setLimit(5000);
        LoadingPanel.loading(true);
        CommonService.App.get().getPositionEmployees(objectID, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                requiredSelector.setItems(result);
                LoadingPanel.loading(false);
            }
        });
        drawLeftMenu();


        location = new LocationLookUpWithCode();
        location.addStyleName(DEFAULT_WIDTH);
        locationSelectionHandler(location);

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.addStyleName(DEFAULT_WIDTH);
        departmentLookUp.getSuggestBox().addSelectionHandler(e -> updateLocation(departmentLookUp.getSelectedItemID()));
        departmentContainer = new Div();
        departmentContainer.add(departmentLookUp);

        locationContainer = new Div();
        locationContainer.add(location);

        type = new ReferenceLookUp("POSITION_TYPE");

        count = new TextBox();
        count.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(count);

        jobRequirement = new TextAreaWithSuggestionPopup(wfmStrings.jobRequirements());
        jobRequirement.getMaterialRichEditor().setTitle(wfmStrings.jobRequirements());
        jobRequirement.getMaterialRichEditor().getElement().setId("position_add_edit_view_jobrequirement");
        jobRequirement.setHeight("400px");

        responsibilities = new TextAreaWithSuggestionPopup(wfmStrings.responsibilities());
        responsibilities.getMaterialRichEditor().getElement().setId("position_add_edit_view_responsibilities");
        responsibilities.setHeight("400px");
//        Todo Stanislav aka shoudl check
//      responsibilities.addStyleName("materialRichEditor-container");


        description = new TextAreaWithSuggestionPopup(wfmStrings.description());
        description.addStyleName("AddVacancyView--desctiption");
        description.setHeight("400px");
        description.getMaterialRichEditor().getElement().setId("position_add_edit_view_description");

        measuringEmployeePerformance = new TextAreaWithSuggestionPopup(wfmStrings.measuringEmployeePerformance());
        measuringEmployeePerformance.setHeight("400px");

        personalQualities = new TextAreaWithSuggestionPopup(wfmStrings.personalQualiteis());
        personalQualities.setHeight("400px");

        knowledge = new TextAreaWithSuggestionPopup(wfmStrings.knowledge());
        knowledge.setHeight("400px");

        responsibilitiesLocalize = new MultiLanguageRichEditorWidget(this.responsibilities);
        jobRequirementLocalize = new MultiLanguageRichEditorWidget(this.jobRequirement);
        descriptionLocalize = new MultiLanguageRichEditorWidget(this.description);

        measuringEmployeePerformanceLocalize = new MultiLanguageRichEditorWidget(this.measuringEmployeePerformance);
        personalQualitiesLocalize = new MultiLanguageRichEditorWidget(this.personalQualities);
        knowledgeLocalize = new MultiLanguageRichEditorWidget(this.knowledge);

        coefficient = new TextBox();
        coefficient.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(coefficient, 2);

        salaryBasis = new TextBox();
        salaryBasis.addStyleName(DEFAULT_WIDTH);

        if (objectID == null) {
            HrmsService.App.get().getPositionParams(new AbstractAsyncCallback<PositionItem>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(PositionItem result) {
                    if (result.getPosStatus() != null) {
                        status.setItems(result.getPosStatus());
                        for (SelectItem item : result.getPosStatus()) {
                            if (Constants.POS_STATUS_OPEN.equals(item.getDescription())) {
                                status.setSelected(item.getId());
                            }
                        }
                    }
                    if (result.getNumberData() != null) {
                        positionCode.setNumberData(result.getNumberData());
                    }
                }
            });
        }

        positionCode = new Numbering(false);
        positionCode.addStyleName(Constants.DEFAULT_WIDTH);
        positionCode.ensureDebugId(TEST_CODE_PREFIX + "position_code");
        if (objectID != null) {
            positionCode.getTxtPrefix().setWidth("100%");
        }

        WfmButton2 aiHelper = new WfmButton2(wfmStrings.aiHelper(), WfmButton2.BTN_SUCCESS);
        aiHelper.addClickHandler(event -> {
            if (!positionTitle.isSelected() || !departmentLookUp.isSelected()) {
                WfmMessageBox validationBox = new WfmMessageBox(IconEnum.WARN, Action.OK);
                validationBox.setTitle(wfmStrings.fillRequiredField());
                validationBox.setMessage(wfmStrings.pleaseSelectPositionAndDepartmentFields());
                validationBox.open();
            } else {
                aiHelper.setEnabled(false);
                LoadingPanel.loading(true);
                HrmsService.App.get().fetchPositionAiData(new PositionAiRequest(positionTitle.getSelectedItemID(), departmentLookUp.getSelectedItemID()), new AsyncCallback<PositionAiResponse>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        aiHelper.setEnabled(true);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(PositionAiResponse result) {
                        LoadingPanel.loading(false);
                        aiHelper.setEnabled(true);

                        salaryBasis.setValue(result.getSalaryBasis());
                        setRichTextData(description, result.getPositionDescription(), null, null);
                        setRichTextData(jobRequirement, result.getJobRequirements(), null, null);
                        setRichTextData(responsibilities, result.getResponsibilities(), null, null);

                        setRichTextData(measuringEmployeePerformance, result.getMeasuringEmployeePerformance(), null, null);
                        setRichTextData(personalQualities, result.getPersonalQualities(), null, null);
                        setRichTextData(knowledge, result.getKnowledge(), null, null);
                    }
                });
            }
        });

        positionTitle = new ReferenceLookUp(POSITION_TITLES, () -> Utils.openURL("Settings.html#reference|edit/" + positionRefId));
        positionTitle.setWidth("99%");
        positionTitle.ensureDebugId(TEST_CODE_PREFIX + "position_title");

        FlexTable position = new FlexTable();
        position.setWidth("100%");

        position.setWidget(0, 0, positionTitle);

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_AI_POSITION_FILL)) {
            position.setWidget(0, 1, aiHelper);
        }

        position.getCellFormatter().setWidth(0, 0, "100%");
        position.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        position.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);


        established = new DatePicker(true);
        established.addStyleName(DEFAULT_WIDTH);
        established.ensureDebugId(TEST_CODE_PREFIX + "established");

        wageRateBox = new TextBox();
        Validation.addNumericKeyboardListener(wageRateBox);
        wageRateBox.addStyleName(DEFAULT_WIDTH);
        wageRateBox.ensureDebugId(TEST_CODE_PREFIX + "wageRate");

        if (formPropertyMap != null && formPropertyMap.get(POSITION_CODE) != null) {
            addField(POSITION_CODE, positionCode, getTitle(formPropertyMap.get(POSITION_CODE).isChanged() ? formPropertyMap.get(POSITION_CODE).getTitle() : wfmStrings.number(), formPropertyMap.get(POSITION_CODE).isRequired()), false,
                    formPropertyMap.get(POSITION_CODE).isInformation());
            positionCode.setEnabled(!formPropertyMap.get(POSITION_CODE).isDisabled());
            if (formPropertyMap.get(POSITIONS.POSITION_CODE).isInformation()) {
                new KpiToolTip(positionCode, formPropertyMap.get(POSITIONS.POSITION_CODE).getInformationText());
            }
        } else {
            addField(POSITION_CODE, positionCode, getTitle(wfmStrings.code(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_TITLE) != null) {
            addField(POSITIONS.POSITION_TITLE, position, getTitle(formPropertyMap.get(POSITIONS.POSITION_TITLE).isChanged() ? formPropertyMap.get(POSITIONS.POSITION_TITLE).getTitle() : wfmStrings.position(), formPropertyMap.get(POSITIONS.POSITION_TITLE).isRequired()), false,
                    formPropertyMap.get(POSITIONS.POSITION_TITLE).isInformation());
            positionTitle.setEnabled(!formPropertyMap.get(POSITIONS.POSITION_TITLE).isDisabled());
            if (formPropertyMap.get(POSITIONS.POSITION_TITLE).isInformation()) {
                new KpiToolTip(positionTitle, formPropertyMap.get(POSITIONS.POSITION_TITLE).getInformationText());
            }
        } else {
            addField(POSITIONS.POSITION_TITLE, position, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.STATUS) != null) {
            addField(POSITIONS.STATUS, status, getTitle(formPropertyMap.get(POSITIONS.STATUS).isChanged() ? formPropertyMap.get(POSITIONS.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(POSITIONS.STATUS).isRequired()), false,
                    formPropertyMap.get(POSITIONS.STATUS).isInformation());
            status.setEnabled(!formPropertyMap.get(POSITIONS.STATUS).isDisabled());

            if (formPropertyMap.get(POSITIONS.STATUS).isInformation()) {
                new KpiToolTip(status, formPropertyMap.get(POSITIONS.STATUS).getInformationText());
            }

        } else {
            addField(POSITIONS.STATUS, status, getTitle(wfmStrings.status(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.ESTIBLISHED) != null) {
            addField(POSITIONS.ESTIBLISHED, established, getTitle(formPropertyMap.get(POSITIONS.ESTIBLISHED).isChanged() ? formPropertyMap.get(POSITIONS.ESTIBLISHED).getTitle() : wfmStrings.created(), formPropertyMap.get(POSITIONS.ESTIBLISHED).isRequired()), false,
                    formPropertyMap.get(POSITIONS.ESTIBLISHED).isInformation());
            established.setEnabled(!formPropertyMap.get(POSITIONS.ESTIBLISHED).isDisabled());
            if (formPropertyMap.get(POSITIONS.ESTIBLISHED).isInformation()) {
                new KpiToolTip(established, formPropertyMap.get(POSITIONS.ESTIBLISHED).getInformationText());
            }
        } else {
            addField(POSITIONS.ESTIBLISHED, established, getTitle(wfmStrings.created(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.LOCATION) != null) { //
            addField(POSITIONS.LOCATION, locationContainer, getTitle(formPropertyMap.get(POSITIONS.LOCATION).isChanged() ? formPropertyMap.get(POSITIONS.LOCATION).getTitle() : Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(POSITIONS.LOCATION).isRequired()), false,
                    formPropertyMap.get(POSITIONS.LOCATION).isInformation());
            location.setEnabled(!formPropertyMap.get(POSITIONS.LOCATION).isDisabled());
            if (formPropertyMap.get(POSITIONS.LOCATION).isInformation()) {
                new KpiToolTip(location, formPropertyMap.get(POSITIONS.LOCATION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PROJECT.LOCATION, location, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.DEPARTMENT) != null) {
            addField(POSITIONS.DEPARTMENT, departmentContainer, getTitle(formPropertyMap.get(POSITIONS.DEPARTMENT).isChanged() ? formPropertyMap.get(POSITIONS.DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(POSITIONS.DEPARTMENT).isRequired()), false,
                    formPropertyMap.get(POSITIONS.DEPARTMENT).isInformation());
            departmentLookUp.setEnabled(!formPropertyMap.get(POSITIONS.DEPARTMENT).isDisabled());
            if (formPropertyMap.get(POSITIONS.DEPARTMENT).isInformation()) {
                new KpiToolTip(departmentLookUp, formPropertyMap.get(POSITIONS.DEPARTMENT).getInformationText());
            }
        } else {
            addField(POSITIONS.DEPARTMENT, departmentContainer, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNT) != null) {
            addField(CustomFormConstants.COUNT, count, getTitle(formPropertyMap.get(CustomFormConstants.COUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.COUNT).getTitle() : wfmStrings.vacantPlaceCount(), formPropertyMap.get(CustomFormConstants.COUNT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.COUNT).isInformation());
            count.setEnabled(!formPropertyMap.get(CustomFormConstants.COUNT).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.COUNT).isInformation()) {
                new KpiToolTip(count, formPropertyMap.get(CustomFormConstants.COUNT).getInformationText());
            }
        } else {
            addField(CustomFormConstants.COUNT, count, getTitle(wfmStrings.count(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null) {
            addField(CustomFormConstants.TYPE, type, getTitle(formPropertyMap.get(CustomFormConstants.TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.TYPE).getTitle() : wfmStrings.type(), formPropertyMap.get(CustomFormConstants.TYPE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.TYPE).isInformation());
            type.setEnabled(!formPropertyMap.get(CustomFormConstants.TYPE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TYPE).isInformation()) {
                new KpiToolTip(type, formPropertyMap.get(CustomFormConstants.TYPE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TYPE, type, getTitle(wfmStrings.type(), false));
        }


        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.SALARY_BASIS) != null) {
            addField(POSITIONS.SALARY_BASIS, salaryBasis, getTitle(formPropertyMap.get(POSITIONS.SALARY_BASIS).isChanged() ? formPropertyMap.get(POSITIONS.SALARY_BASIS).getTitle() : wfmStrings.salaryBasis(), formPropertyMap.get(POSITIONS.SALARY_BASIS).isRequired()), false,
                    formPropertyMap.get(POSITIONS.SALARY_BASIS).isInformation());
            salaryBasis.setEnabled(!formPropertyMap.get(POSITIONS.SALARY_BASIS).isDisabled());
            if (formPropertyMap.get(POSITIONS.SALARY_BASIS).isInformation()) {
                new KpiToolTip(salaryBasis, formPropertyMap.get(POSITIONS.SALARY_BASIS).getInformationText());
            }
        } else {
            addField(POSITIONS.SALARY_BASIS, salaryBasis, getTitle(wfmStrings.salaryBasis(), false));
        }


        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.COEFFICENT) != null) {
            addField(POSITIONS.COEFFICENT, coefficient, getTitle(formPropertyMap.get(POSITIONS.COEFFICENT).isChanged() ? formPropertyMap.get(POSITIONS.COEFFICENT).getTitle() : wfmStrings.coefficent(), formPropertyMap.get(POSITIONS.COEFFICENT).isRequired()), false,
                    formPropertyMap.get(POSITIONS.COEFFICENT).isInformation());
            coefficient.setEnabled(!formPropertyMap.get(POSITIONS.COEFFICENT).isDisabled());
            if (formPropertyMap.get(POSITIONS.COEFFICENT).isInformation()) {
                new KpiToolTip(coefficient, formPropertyMap.get(POSITIONS.COEFFICENT).getInformationText());
            }
        } else {
            addField(POSITIONS.COEFFICENT, coefficient, getTitle(wfmStrings.coefficent(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.JOB_REQUIREMENT) != null) {
            addField(POSITIONS.JOB_REQUIREMENT, jobRequirement, null);
            if (formPropertyMap.get(POSITIONS.JOB_REQUIREMENT).isInformation()) {
                new KpiToolTip(jobRequirement, formPropertyMap.get(POSITIONS.JOB_REQUIREMENT).getInformationText());
            }
        } else {
            addField(POSITIONS.JOB_REQUIREMENT, jobRequirement, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_DESCRIPTION) != null) {
            addField(POSITIONS.POSITION_DESCRIPTION, description, null);
            if (formPropertyMap.get(POSITIONS.POSITION_DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(POSITIONS.POSITION_DESCRIPTION).getInformationText());
            }
        } else {
            addField(POSITIONS.POSITION_DESCRIPTION, description, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_RESPONSIBILITIES) != null) {
            addField(POSITIONS.POSITION_RESPONSIBILITIES, responsibilities, null);
            if (formPropertyMap.get(POSITIONS.POSITION_RESPONSIBILITIES).isInformation()) {
                new KpiToolTip(responsibilities, formPropertyMap.get(POSITIONS.POSITION_RESPONSIBILITIES).getInformationText());
            }
        } else {
            addField(POSITIONS.POSITION_RESPONSIBILITIES, responsibilities, null);
        }


        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE) != null) {
            addField(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE, measuringEmployeePerformance, null);
            if (formPropertyMap.get(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE).isInformation()) {
                new KpiToolTip(measuringEmployeePerformance, formPropertyMap.get(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE).getInformationText());
            }
        } else {
            addField(POSITIONS.MEASURING_EMPLOYEE_PERFORMANCE, measuringEmployeePerformance, null);
        }


        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.PERSONAL_QUALITIES) != null) {
            addField(POSITIONS.PERSONAL_QUALITIES, personalQualities, null);
            if (formPropertyMap.get(POSITIONS.PERSONAL_QUALITIES).isInformation()) {
                new KpiToolTip(personalQualities, formPropertyMap.get(POSITIONS.PERSONAL_QUALITIES).getInformationText());
            }
        } else {
            addField(POSITIONS.PERSONAL_QUALITIES, personalQualities, null);
        }


        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.KNOWLEDGE) != null) {
            addField(POSITIONS.KNOWLEDGE, knowledge, null);
            if (formPropertyMap.get(POSITIONS.KNOWLEDGE).isInformation()) {
                new KpiToolTip(knowledge, formPropertyMap.get(POSITIONS.KNOWLEDGE).getInformationText());
            }
        } else {
            addField(POSITIONS.KNOWLEDGE, knowledge, null);
        }

        addField(POSITIONS.EMPLOYEES, requiredSelector, null);

        addTitleField(POSITIONS.BASIC_INFORMATION, wfmStrings.basicDetails());
        this.addTitleField(CustomFormConstants.VACANCY.DETAILED_INFORMATION, wfmStrings.detailedInformation());
        addTitleField(POSITIONS.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
        addTitleField(POSITIONS.JOB_INFORMATION, hrmsStrings.jobInformation());
        addTitleField(POSITIONS.KNOWLEDE_AND_SKILLS, wfmStrings.knowledgeAndSkills());

        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        if (basicObjectID != null) {
            getDataToFillFields();
        }
        show();
    }

    private void fillTable(PositionItem item) {

        if (item.getPosStatus() != null) {
            status.setItems(item.getPosStatus());
        }

        if (item.getStatus() != null) {
            status.setSelected(item.getStatus());
        } else {
            status.setSelectedByDescription(POS_STATUS_ACTIVE);
        }

        if (item.getEstablished() != null) {
            established.setDate(item.getEstablished());
        }

        if (item.getType() != null) {
            type.setSelected(item.getType());
        }

        if (basicObjectID == null) {
            if (item.getNumberData() != null) {
                positionCode.setNumberData(item.getNumberData());
                if (objectID != null) {
                    positionCode.getTxtNumber().removeFromParent();
                }
            }

            if (item.getPositionName() != null) {
                positionTitle.setSelected(item.getPositionName());
            }
        }

        boolean isNewOrCopy = (objectID == null && basicObjectID == null);

        if (isNewOrCopy && item.getCreatorLocation() != null) {
            location.setSelected(item.getCreatorLocation());
        } else {
            location.setSelected(item.getLocation());
        }

        if (isNewOrCopy && item.getCreatorDepartment() != null) {
            departmentLookUp.setSelected(item.getCreatorDepartment());
        } else {
            departmentLookUp.setSelected(item.getDepartment());
        }

        if (item.getWageRate() != null) {
            wageRateBox.setText(Utils.getNumberFormat().format(item.getWageRate()));
        }

        if (item.getCoefficent() != null) {
            coefficient.setText(String.valueOf(item.getCoefficent()));
        }

        if (item.getSalaryBasis() != null) {
            salaryBasis.setText(item.getSalaryBasis());
        }

        if (item.getCount() != null) {
            count.setText(item.getCount());
        }

        setRichTextData(responsibilities, item.getResponsibility(), item.getResponsibilitiesLocalize(), userLanguage);
        setRichTextData(description, item.getPositionDescription(), item.getDescriptionLocalize(), userLanguage);
        setRichTextData(jobRequirement, item.getJobRequirements(), item.getJobRequirementLocalize(), userLanguage);
        setRichTextData(measuringEmployeePerformance, item.getMeasuringEmployeePerformance(), item.getMeasuringEmployeePerformanceLocalize(), userLanguage);
        setRichTextData(personalQualities, item.getPersonalQualities(), item.getPersonalQualitiesLocalize(), userLanguage);
        setRichTextData(knowledge, item.getKnowledge(), item.getKnowledgeLocalize(), userLanguage);

        if (objectID != null && basicObjectID == null) {
            originalPositionName = item.getPositionName() != null ? item.getPositionName().getName() : null;
            originalLocationId = item.getLocation() != null ? item.getLocation().getId() : null;
        }

        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private void setValues() {
        position = new PositionItem();

        if (objectID != null) {
            position.setObjectID(objectID);
        }
        position.setMembers(requiredSelector.getSelectedData());
        position.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        position.setNumberData(positionCode.getNumberData(true));
        position.setPositionCode(positionCode.getNumberData(true).getNumberString());
        position.setName(positionTitle.getText());
        position.setPositionName(positionTitle.getSelectedItem());

        position.setStatus(status.getSelectedItem());
        position.setDepartment(departmentLookUp.getSelectedItem());
        position.setLocation(location.getSelectedItem());
        position.setType(type.getSelectedItem());
        position.setEstablished(established.getDate());

        position.setCount(count.getText());
        position.setSalaryBasis(salaryBasis.getValue());

        String coeffVal = coefficient.getValue();
        position.setCoefficent((coeffVal != null && !coeffVal.isEmpty()) ? Double.valueOf(coeffVal) : null);

        String wageVal = wageRateBox.getText();
        if (wageVal != null && !wageVal.isEmpty()) {
            position.setWageRate(new BigDecimal(wageVal.replace(",", "")));
        }

        position.setJobRequirements(jobRequirement.getMaterialRichEditor().getData());
        position.setJobRequirementLocalize(jobRequirementLocalize.getValueMap());

        position.setResponsibility(responsibilities.getMaterialRichEditor().getData());
        position.setResponsibilitiesLocalize(responsibilitiesLocalize.getValueMap());

        position.setPositionDescription(description.getMaterialRichEditor().getData());
        position.setDescriptionLocalize(descriptionLocalize.getValueMap());

        position.setMeasuringEmployeePerformance(measuringEmployeePerformance.getMaterialRichEditor().getData());
        position.setMeasuringEmployeePerformanceLocalize(measuringEmployeePerformanceLocalize.getValueMap());

        position.setPersonalQualities(personalQualities.getMaterialRichEditor().getData());
        position.setPersonalQualitiesLocalize(personalQualitiesLocalize.getValueMap());

        position.setKnowledge(knowledge.getMaterialRichEditor().getData());
        position.setKnowledgeLocalize(knowledgeLocalize.getValueMap());
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = getCustomFieldUtil().validateCustomFields();

        Map<String, FormProperty> props = formPropertyMap;

        if (props != null) {

            if (isReq(props, POSITIONS.STATUS)) {
                errors += markAsError(POSITIONS.STATUS, status, status.getSelectedItem() == null);
            }

            if (isReq(props, POSITIONS.DEPARTMENT)) {
                errors += markAsError(POSITIONS.DEPARTMENT, departmentLookUp, !Validation.validateLookUpRequired(departmentLookUp));
            }

            if (isReq(props, POSITIONS.LOCATION)) {
                errors += markAsError(POSITIONS.LOCATION, location, !Validation.validateLookUpRequired(location));
            }

            if (isReq(props, CustomFormConstants.TYPE)) {
                errors += markAsError(CustomFormConstants.TYPE, type, !Validation.validateLookUpRequired(type));
            }

            if (isReq(props, CustomFormConstants.COUNT)) {
                errors += markAsError(CustomFormConstants.COUNT, count, !Validation.validateTextAreaRequired(count));
            }

            if (isReq(props, POSITIONS.ESTIBLISHED)) {
                errors += markAsError(POSITIONS.ESTIBLISHED, established, Validation.validateDate(established));
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void setDefaultValuesByFormProperty() {
        FormProperty prop;

        prop = getProp(POSITIONS.STATUS);
        if (prop != null && prop.getDefaultValue() != null) {
            status.setSelected(new SelectItem(prop.getSelectedId(), prop.getDefaultValue()));
        }

        prop = getProp(POSITIONS.ESTIBLISHED);
        if (prop != null && prop.getDefaultValue() != null) {
            try {
                established.setDate(new Date(prop.getDefaultValue()));
            } catch (Exception e) {
                GWT.log("Invalid date for ESTABLISHED: " + prop.getDefaultValue(), e);
            }
        }

        prop = getProp(CustomFormConstants.TYPE);
        if (prop != null && prop.getDefaultValue() != null) {
            type.setSelected(prop.getSelectedId());
        }

        prop = getProp(POSITIONS.SALARY_BASIS);
        if (prop != null && prop.getDefaultValue() != null) {
            salaryBasis.setValue(prop.getDefaultValue());
        }
    }


    private void updateLocation(Integer departmentId) {
        HrmsService.App.get().getLocationByDepartmentId(departmentId, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(SelectItem result) {
                location.removeFromParent();
                locationContainer.remove(location);
                location = new LocationLookUpWithCode();
                locationSelectionHandler(location);
                location.setSelected(result);
                locationContainer.add(location);
            }
        });
    }


    private void savePosition() {
        HrmsService.App.get().savePosition(position, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                enableButton(true);
                LoadingPanel.loading(false);
                if (result.equals(Errors.THIS_NAME_IS_ALREADY_EXIST)) {
                    Info.show(hrmsStrings.thisPositionAlreadyExists(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.position()));
                    if (!saveAndNew) {
                        closeTab("positionsummary|positionsummaryview/" + result, position.getName());
                    } else {
                        closeTab("position|add/add");
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_ADD_EDIT, result, AddEditPositionView.this);
                }
            }
        });
    }

    private void locationSelectionHandler(LocationLookUpWithCode locationLookUpWithCode) {
        locationLookUpWithCode.getSuggestBox().addSelectionHandler(e -> {
            departmentLookUp.removeFromParent();
            departmentContainer.remove(departmentLookUp);
            departmentLookUp = new DepartmentLookUp();
            departmentLookUp.getFilterParametrs().setLocationId(location.getSelectedItemID());
            departmentContainer.add(departmentLookUp);
        });
        locationLookUpWithCode.getTextBox().addKeyDownHandler(e -> {
            departmentLookUp.removeFromParent();
            departmentContainer.remove(departmentLookUp);
            departmentLookUp = new DepartmentLookUp();
            departmentLookUp.getSuggestBox().addSelectionHandler(event -> updateLocation(departmentLookUp.getSelectedItemID()));
            departmentContainer.add(departmentLookUp);
        });

    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        if (!Utils.isNullOrEmpty(position.getCount()) && Integer.valueOf(position.getCount()) < position.getMembers().size()) {
            Info.warn(hrmsStrings.actualMorePlan());
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        boolean nameOrLocationChanged = objectID == null
                || !Objects.equals(positionTitle.getText(), originalPositionName)
                || !Objects.equals(location.getSelectedItemID(), originalLocationId);
        if (nameOrLocationChanged) {
            validationForSameName(position);
        } else {
            savePosition();
        }
    }

    private void validationForSameName(PositionItem item) {
        HrmsService.App.get().getPositionsSizeByNameForValidation(item, new AsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                if (result == 0) {
                    savePosition();
                    return;
                }

                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(wfmStrings.position() + " " + wfmStrings.withTheSameNameAlreadyExist() + ". " + wfmStrings.doYouWantToSaveChanges());

                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                        enableButton(true);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSubmit() {
                        savePosition();
                    }
                });
                messageBox.open();
            }

            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
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

    private void setRichTextData(TextAreaWithSuggestionPopup field, String defaultValue, HashMap<String, String> localizedMap, String lang) {
        String data = (defaultValue != null) ? defaultValue : "";
        if (mapHasValueForLang(localizedMap, lang)) {
            data = localizedMap.get(lang);
        }
        field.getMaterialRichEditor().setData(data);
    }

    private boolean isReq(Map<String, FormProperty> props, String key) {
        FormProperty prop = props.get(key);
        return prop != null && prop.isRequired();
    }

    private FormProperty getProp(String key) {
        return formPropertyMap != null ? formPropertyMap.get(key) : null;
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        return fieldID != null ? getLocalizer().localizeByFieldID(getFormID(), fieldID) : null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.POSITION_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.HRMS_POSITION_ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        return customFieldUtil == null ? customFieldUtil = new FormHasCustomField() : customFieldUtil;
    }

}
