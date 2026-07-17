package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MultiLanguageRichEditorWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextAreaWithSuggestionPopup;
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
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LocationLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiAddPositionView extends CustomForm2 implements Constants, FittedContent, Colapse {
    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private TextBox count, wageRateBox;
    private final String test_code_ID_name = "add_position_view_";
    private String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.position());
    private ReferenceLookUp positionTitle;
    ArrayList<PositionItem> positionItems = new ArrayList<>();
    private PositionItem position;
    private Numbering positionCode;
    private LocationLookUpWithCode location;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private KpiCellTree requiredSelector;
    private Integer objectID;
    private FormHasCustomField customFieldUtil;
    private Div locationContainer;
    private Div departmentContainer;
    private DepartmentLookUp departmentLookUp;
    private PositionLookUp positionLookUp;
    private DatePicker established;
    private DataListBox status;
    private boolean saveAndNew = false;
    private AddEditLocaleView localeView;
    private ReferenceLookUp type;
    private TextAreaWithSuggestionPopup jobRequirement, responsibilities, description;
    private MultiLanguageRichEditorWidget jobRequirementLocalize, responsibilityLocalize, descriptionLocalize;
    private TextBox coefficient;
    private Integer positionRefId;


    public MultiAddPositionView() {
        super("multiaddposition", wfmStrings.add() + " " + Property.get(POSITION1, wfmStrings.position()));
    }

    public MultiAddPositionView(Integer objectId) {
        super("multiaddposition", wfmStrings.add() + " " + Property.get(POSITION1, wfmStrings.position()));
        this.objectID = objectId;
    }


    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    protected void addButtons() {

        if (objectID == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            save.addClickHandler(event -> {
                saveAndNew = false;
                save();
            });
            MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
            saveAdd.addClickHandler(event -> {
                saveAndNew = true;
                save();
            });
            splitButton.addItem(saveAdd);
            addButton(splitButton);
        } else {
            addButton(new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY, event -> save()));
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getPositionForEdit(null, "EDIT", new AbstractAsyncCallback<PositionItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PositionItem result) {
                LoadingPanel.loading(false);
                fillTable(result);
                positionRefId = result.getPositionRefId();
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                descriptionLocalize.setValueMap(result.getDescriptionLocalize());
                jobRequirementLocalize.setValueMap(result.getJobRequirementLocalize());
                responsibilityLocalize.setValueMap(result.getResponsibilitiesLocalize());
            }
        });
        successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.position());
    }

    //Involved Employees
    private void drawLeftMenu() {
        requiredSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                //employee name
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName() + "(" + object.getDepartmentName() + ")";
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                selectedDataGrid.addColumn(employee, wfmStrings.departments());
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
    protected String getFormID() {
        return LayoutRPC.MULTI_POSITION_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.MultiPosition, LayoutRPC.POSITION_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                MultiAddPositionView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.HRMS_POSITION_ADD;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected void registerFields() {

        positionLookUp = new PositionLookUp();
        positionLookUp.getFilterParametrs().setFromPositionBulkUpdate(true);
        positionLookUp.addStyleName(DEFAULT_WIDTH);
        positionLookUp.getSuggestBox().addSelectionHandler(event -> {
            CommonService.App.get().getPositionsForKpiTree(positionLookUp.getSelectedItem(), new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    requiredSelector.setItems(result);
                    LoadingPanel.loading(false);
                }
            });

        });

        //status
        status = new DataListBox();
        status.addStyleName(DEFAULT_WIDTH);
        status.setWithoutNullLabel(true);
        status.ensureDebugId(test_code_ID_name + "status");

        //involved employees
        requiredSelector = new KpiCellTree();
        requiredSelector.setLimit(5000);
        if (objectID == null) {
            CommonService.App.get().getPositionDepartments(null, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                    requiredSelector.setItems(result);
                    LoadingPanel.loading(false);
                }
            });
        }
        drawLeftMenu();

        //location
        location = new LocationLookUpWithCode();
        location.addStyleName(DEFAULT_WIDTH);
        locationSelectionHandler(location);

        //Department
        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.addStyleName(DEFAULT_WIDTH);
        departmentLookUp.getSuggestBox().addSelectionHandler(e -> updateLocation(departmentLookUp.getSelectedItemID()));
        departmentContainer = new Div();
        departmentContainer.add(departmentLookUp);

        //location
        locationContainer = new Div();
        locationContainer.add(location);

        //type
        type = new ReferenceLookUp("POSITION_TYPE");

        //Count
        count = new TextBox();
        count.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(count);

        //job requirements
        jobRequirement = new TextAreaWithSuggestionPopup(wfmStrings.jobRequirements());
        jobRequirement.getMaterialRichEditor().setTitle(wfmStrings.jobRequirements());
        jobRequirement.setHeight("400");

        //responsibilities
        responsibilities = new TextAreaWithSuggestionPopup(wfmStrings.responsibilities());
        responsibilities.getElement().setId("position_add_edit_view_responsibilities");
        responsibilities.setHeight("400");

        //description
        description = new TextAreaWithSuggestionPopup(wfmStrings.description());
        description.addStyleName("AddVacancyView--desctiption");
        description.setHeight("400");
        description.getMaterialRichEditor().getElement().setId("position_add_edit_view_description");

        //localization
        responsibilityLocalize = new MultiLanguageRichEditorWidget(this.responsibilities);
        jobRequirementLocalize = new MultiLanguageRichEditorWidget(this.jobRequirement);
        descriptionLocalize = new MultiLanguageRichEditorWidget(this.description);

        //coefficient
        coefficient = new TextBox();
        coefficient.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(coefficient, 2);

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


        //Number
        positionCode = new Numbering(false);
        positionCode.addStyleName(Constants.DEFAULT_WIDTH);
        positionCode.ensureDebugId(test_code_ID_name + "position_code");
        if (objectID != null) {
            positionCode.getTxtPrefix().setWidth("100%");
        }

        //position name
        positionTitle = new ReferenceLookUp(POSITION_TITLES, () -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("reference|edit/" + positionRefId);
        });
        positionTitle.setWidth("95%");
        positionTitle.ensureDebugId(test_code_ID_name + "position_title");


        //position established date
        established = new DatePicker(true);
        established.addStyleName(DEFAULT_WIDTH);
        established.ensureDebugId(test_code_ID_name + "established");

        //wage rate
        wageRateBox = new TextBox();
        Validation.addNumericKeyboardListener(wageRateBox);
        wageRateBox.addStyleName(DEFAULT_WIDTH);
        wageRateBox.ensureDebugId(test_code_ID_name + "wageRate");


        addTitleField(POSITIONS.POSITION_DETAILS, wfmStrings.basicDetails());


        if (objectID != null) {
            if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_LOOKUP) != null) {
                addField(POSITIONS.POSITION_LOOKUP, positionLookUp, getTitle(formPropertyMap.get(POSITIONS.POSITION_LOOKUP).isChanged() ? formPropertyMap.get(POSITIONS.POSITION_LOOKUP).getTitle() : wfmStrings.position(), formPropertyMap.get(POSITIONS.POSITION_LOOKUP).isRequired()), false,
                        formPropertyMap.get(POSITIONS.POSITION_LOOKUP).isInformation());
                positionTitle.setEnabled(!formPropertyMap.get(POSITIONS.POSITION_LOOKUP).isDisabled());
                if (formPropertyMap.get(POSITIONS.POSITION_LOOKUP).isInformation()) {
                    new KpiToolTip(positionLookUp, formPropertyMap.get(POSITIONS.POSITION_LOOKUP).getInformationText());
                }
            } else {
                addField(POSITIONS.POSITION_LOOKUP, positionLookUp, getTitle(wfmStrings.position(), true));
            }
        }

        //Position Title
        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_TITLE) != null) {
            addField(POSITIONS.POSITION_TITLE, positionTitle, getTitle(formPropertyMap.get(POSITIONS.POSITION_TITLE).isChanged() ? formPropertyMap.get(POSITIONS.POSITION_TITLE).getTitle() : wfmStrings.name(), formPropertyMap.get(POSITIONS.POSITION_TITLE).isRequired()), false,
                    formPropertyMap.get(POSITIONS.POSITION_TITLE).isInformation());
            positionTitle.setEnabled(!formPropertyMap.get(POSITIONS.POSITION_TITLE).isDisabled());
            if (formPropertyMap.get(POSITIONS.POSITION_TITLE).isInformation()) {
                new KpiToolTip(positionTitle, formPropertyMap.get(POSITIONS.POSITION_TITLE).getInformationText());
            }
        } else {
            addField(POSITIONS.POSITION_TITLE, positionTitle, getTitle(wfmStrings.name(), true));
        }

        //Status
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


        //Count
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

        //coefficent
        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.COEFFICENT) != null) {
            addField(POSITIONS.COEFFICENT, coefficient, getTitle(formPropertyMap.get(POSITIONS.COEFFICENT).isChanged() ? formPropertyMap.get(POSITIONS.COEFFICENT).getTitle() : wfmStrings.coefficent(), formPropertyMap.get(POSITIONS.COEFFICENT).isRequired()), false,
                    formPropertyMap.get(POSITIONS.COEFFICENT).isInformation());
            positionCode.setEnabled(!formPropertyMap.get(POSITIONS.COEFFICENT).isDisabled());
            if (formPropertyMap.get(POSITIONS.COEFFICENT).isInformation()) {
                new KpiToolTip(positionCode, formPropertyMap.get(POSITIONS.COEFFICENT).getInformationText());
            }
        } else {
            addField(POSITIONS.COEFFICENT, coefficient, getTitle(wfmStrings.coefficent(), false));
        }


        //Type
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

        addTitleField(POSITIONS.POSITION_DEPARTMENTS, wfmStrings.departments());
        addField(POSITIONS.DEPARTMENTS, requiredSelector, null);
        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        show();
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void fillTable(PositionItem item) {
        //Status items
        if (item.getPosStatus() != null) {
            status.setItems(item.getPosStatus());
        }
        //Number
        if (item.getNumberData() != null) {
            positionCode.setNumberData(item.getNumberData());
            if (objectID != null) {
                positionCode.getTxtNumber().removeFromParent();
            }
        }
        //position name
        if (item.getPositionName() != null) {
            positionTitle.setSelected(item.getPositionName());
        }
        if (item.getEstablished() != null) {
            established.setDate(item.getEstablished());
        }
        //Status
        if (item.getStatus() != null) {
            status.setSelected(item.getStatus());
        } else {
            status.setSelectedByDescription(POS_STATUS_ACTIVE);
        }

        //Location-auto set creators location
        if (item.getCreatorLocation() != null && objectID == null) {
            location.setSelected(item.getCreatorLocation());

        } else {
            location.setSelected(item.getLocation());
        }

        //department-auto set creators department
        if (item.getCreatorDepartment() != null && objectID == null) {
            departmentLookUp.setSelected(item.getCreatorDepartment());
        } else {
            departmentLookUp.setSelected(item.getDepartment());
        }

        //position wage rate
        if (item.getWageRate() != null) {
            wageRateBox.setText(Utils.getNumberFormat().format(item.getWageRate()));
        }

        //position count rate
        if (item.getCount() != null) {
            count.setText(item.getCount());
        }
        //Location-auto set creators location
        if (item.getCreatorLocation() != null && objectID == null) {
            location.setSelected(item.getCreatorLocation());
        } else {
            location.setSelected(item.getLocation());
        }

        if (objectID == null) {
            setDefaultValuesByFormProperty();
        }
        if (item.getType() != null) {
            type.setSelected(item.getType());
        }

        String userLanguage = Utils.getUserLanguage();

        responsibilities.getMaterialRichEditor().setData(item.getResponsibility() != null ? item.getResponsibility() : "");
        if (mapHasValueForLang(item.getResponsibilitiesLocalize(), userLanguage)) {
            responsibilities.getMaterialRichEditor().setData(item.getResponsibilitiesLocalize().get(userLanguage));
        }

        description.getMaterialRichEditor().setData(item.getPositionDescription() != null ? item.getPositionDescription() : "");
        if (mapHasValueForLang(item.getDescriptionLocalize(), userLanguage)) {
            description.getMaterialRichEditor().setData(item.getDescriptionLocalize().get(userLanguage));
        }

        jobRequirement.getMaterialRichEditor().setData(item.getJobRequirements());
        if (mapHasValueForLang(item.getJobRequirementLocalize(), userLanguage)) {
            jobRequirement.getMaterialRichEditor().setData(item.getJobRequirementLocalize().get(userLanguage));
        }
        jobRequirement.getElement().addClassName("jobRequirement");
        jobRequirement.setHeight("400");
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        savePosition();
    }

    private void setValues() {
        positionItems.clear();
        for (KpiTreeInfo departments : requiredSelector.getSelectedData()) {
            position = new PositionItem();
            if (objectID != null) {
                position.setObjectID(departments.getId());
            }
            if (objectID == null) {
                position.setDepartment(new SelectItem(departments.getId(), departments.getName()));
            }
            position.setPositionName(positionTitle.getSelectedItem());
            position.setName(positionTitle.getText());
            position.setStatus(status.getSelectedItem());
            position.setLocation(new SelectItem(departments.getDepartmentId(), departments.getDepartmentName()));
            position.setCount(count.getText());
            position.setType(type.getSelectedItem());
            position.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            position.setNumberData(positionCode.getNumberData(true));
            position.setPositionCode(positionCode.getNumberData(true).getNumberString());
            if (coefficient.getValue() != null && !coefficient.getValue().isEmpty()) {
                position.setCoefficent(Double.valueOf(coefficient.getValue()));
            }
            positionItems.add(position);
        }


    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += getCustomFieldUtil().validateCustomFields();

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.POSITION_TITLE) != null && formPropertyMap.get(POSITIONS.POSITION_TITLE).isRequired()) {
            errors += markAsError(POSITIONS.POSITION_TITLE, positionTitle, positionTitle.getSelectedItem() == null);

        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.STATUS) != null && formPropertyMap.get(POSITIONS.STATUS).isRequired()) {
            errors += markAsError(POSITIONS.STATUS, status, status.getSelectedItem() == null);
        }
        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.DEPARTMENT) != null && formPropertyMap.get(POSITIONS.DEPARTMENT).isRequired()) {
            errors += markAsError(POSITIONS.DEPARTMENT, departmentLookUp, !Validation.validateLookUpRequired(departmentLookUp));
        }
        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.LOCATION) != null && formPropertyMap.get(POSITIONS.LOCATION).isRequired()) {
            errors += markAsError(POSITIONS.LOCATION, location, !Validation.validateLookUpRequired(location));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null && formPropertyMap.get(CustomFormConstants.TYPE).isRequired()) {
            errors += markAsError(CustomFormConstants.TYPE, type, !Validation.validateLookUpRequired(type));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.COUNT) != null && formPropertyMap.get(CustomFormConstants.COUNT).isRequired()) {
            errors += markAsError(CustomFormConstants.COUNT, count, !Validation.validateTextAreaRequired(count));
        }
        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.ESTIBLISHED) != null && formPropertyMap.get(POSITIONS.ESTIBLISHED).isRequired()) {
            errors += markAsError(POSITIONS.ESTIBLISHED, established, Validation.validateDate(established));
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.STATUS) != null && formPropertyMap.get(POSITIONS.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(POSITIONS.STATUS).getSelectedId(), formPropertyMap.get(POSITIONS.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(POSITIONS.ESTIBLISHED) != null && formPropertyMap.get(POSITIONS.ESTIBLISHED).getDefaultValue() != null) {
            established.setDate(new Date(formPropertyMap.get(POSITIONS.ESTIBLISHED).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TYPE) != null && formPropertyMap.get(CustomFormConstants.TYPE).getDefaultValue() != null) {
            type.setSelected(formPropertyMap.get(CustomFormConstants.TYPE).getSelectedId());
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

    private void validationForSameName(PositionItem item) {
        HrmsService.App.get().getPositionsSizeByNameForValidation(item, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Integer result) {
                if (result > 0) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(wfmStrings.position() + " " + wfmStrings.withTheSameNameAlreadyExist() + ". " + wfmStrings.doYouWantToSaveChanges());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onCancel() {
                            enableButton(true);
                        }

                        @Override
                        public void onSubmit() {
                            savePosition();
                        }
                    });
                    messageBox.open();
                } else {
                    savePosition();
                }
            }
        });

    }

    private void savePosition() {
        HrmsService.App.get().savePositionItems(positionItems, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                enableButton(true);
                LoadingPanel.loading(false);
                closeTab();
                Info.show(successMessage, Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_POSITION_ADD_EDIT, result, MultiAddPositionView.this);
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
