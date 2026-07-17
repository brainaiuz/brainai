package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.project.CheckInLocationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDatePicker;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDateTime;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomDropDownField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldCurrencyWidget;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomFieldMultiLookUpField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomHTMLTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextAreaField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.edatasite.workforce.gwt.location.client.ui.AddLocationView;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.CheckInLocationWidget;
import com.edatasite.workforce.gwt.project.client.ui.PmClientsLookUp;
import com.edatasite.workforce.gwt.project.client.ui.view.projectposition.ProjectPositionWidget;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectEditView extends CustomForm2 implements Constants, HasLinksInterface, Colapse {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final ProjectServiceAsync projectService = ProjectService.App.get();

    private Numbering number;
    private TextBox name;
    private Widget richText;
    private TextArea2 area;
    private DatePicker startDate;
    private DatePicker dueDate;
    private DataListBox parent;
    private DataListBox manager;
    private Set<SelectItem> systemManagers = new HashSet<>();
    private PmClientsLookUp client;
    private MultiTableNewUI multiClientTable;
    private DataListBox status;
    private FlexTable statusTable;
    private final Integer projectID;
    private EditProject project;
    private boolean isSetupSubProject;
    private KpiCheckBox changeTasksStatus;
    private GeneralFileUpload fileUpload;

    private DataListBox employeeAssignment;
    private FlowPanel pnlEmployeeAssignmentContainer;
    private KpiCellTree membersSelector;
    private ProjectPositionWidget projectPositionWidget;
    private boolean hasEmployeeAssignRole = false;
    private KpiCheckBox copyNewEmployeesToProjectTasks;
    private SelectItem[] backupManagerItems;
    private MultiTableNewUI backupManagerTable;
    private KpiDataGrid<KpiTreeInfo> members;

    private DataListBox locationBox;
    private final KpiCheckBox billable = new KpiCheckBox("");
    private final String editProject = "edit_project_";
    private HasLinks linkingUtil;
    private FormHasCustomField customFieldUtil = null;
    private Reminder reminder;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Map<String, EditableTable> editableTableMap = new HashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();
    public MultiTableNewUI checkInLocations;


    public ProjectEditView(Integer projectID) {
        super("edit");
        setDescription(property.getSingular(projectStrings.editProject(), wfmStrings.project()));
        this.projectID = projectID;
    }

    @Override
    public String getIconStyle() {
        return "bgMark project-edit";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(ProjectEditView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return projectID;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_PROJECT;
                }

                @Override
                protected String getRelationName() {
                    return project != null ? project.getName() : null;
                }
            };
        }
        return linkingUtil;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void getEditProject() {
        LoadingPanel.loading(true);
        projectService.getProjectForEdit(projectID, null, null, new AbstractAsyncCallback<EditProject>() {
            @Override
            public void success(EditProject object) {
                LoadingPanel.loading(false);
                project = object;
                systemManagers = project.getManagers();
                NumberData numberData = project.getNumberData();
                number.setNumberData(numberData);
                if (numberData.getIntNumber() != null) {
                    String preFix = number.getPrefixValue(number.getNumberFormat().format(numberData.getIntNumber().doubleValue()));
                    number.getTxtPrefix().setValue(preFix);
                }
                name.setText(project.getName());
                area.setText(project.getDescription());
                link.setBadgeCount(project.getRelations() != null ? project.getRelations().size() : 0);

                setItemTableValues(project.getCustomTableItems());

                if (project.getEmployeeAssignment() != null) {
                    employeeAssignment.setSelected(project.getEmployeeAssignment().getId());

                    pnlEmployeeAssignmentContainer.clear();
                    if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
                        projectPositionWidget.setValues(project.getProjectPositions());
                        pnlEmployeeAssignmentContainer.add(projectPositionWidget);
                        setManagers();
                    } else {
                        pnlEmployeeAssignmentContainer.add(membersSelector);
                        initMembers();
                    }
                } else {
                    employeeAssignment.setSelected(EmployeeAssignmentEnum.BY_EMPLOYEE.getId());
                    pnlEmployeeAssignmentContainer.add(membersSelector);
                    initMembers();
                }

                if (project.getDueDate() != null) {
                    dueDate.setDate(project.getDueDate());
                }
                if (project.getStartDate() != null) {
                    startDate.setDate(project.getStartDate());
                }
                billable.setValue(project.isBillable());
                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
                    if (project.getClients() != null && project.getClients().length > 0) {
                        multiClientTable.removeAllRows();
                        for (SelectItem client : project.getClients()) {
                            multiClientTable.addWidgets(getClientMap(client));
                        }
                    }
                } else if (project.getClientId() != null) {
                    client.addItem(new SelectItem(project.getClientId(), project.getClientName()));
                }

                if (project.getReminders() != null && !project.getReminders().isEmpty()) {
                    reminder.setReminderDatas(project.getReminders());
                }
                getCustomFieldUtil().fillCustomFieldsWithData(project.getCustomFieldItems());

                defferedLoading();
                manager.setSelected(project.getManagerId());
                if (Utils.hasGenericAccess(GenericSettingsEnum.IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS)) {
                    copyNewEmployeesToProjectTasks.setVisible(true);
                }
                if (object.getBackupManagerIDs() != null && object.getBackupManagerIDs().size() > 0) {
                    backupManagerTable.removeAllRows();
                    for (Integer backupManagerID : object.getBackupManagerIDs()) {
                        backupManagerTable.addWidgets(getBackupManagersMap(backupManagerID));
                    }
                }
                if (!object.getCheckInLocations().isEmpty()) {
                    checkInLocations.clear();
                    for (CheckInLocationItem checkInLocation : object.getCheckInLocations()) {
                        checkInLocations.addWidgets(getCheckInLocations(checkInLocation));
                    }
                }
                setManagers();
            }
        });

        if (Utils.getDefaultProjectID().equals(projectID) || !hasEmployeeAssignRole)
            membersSelector.showTreePanel(false);
    }

    private void initialize() {
        super.onInitialize();
    }

    protected void drawItemTable() {
        ItemTableSettingService.App.get().getColumnConfigs(LayoutRPC.PROJECT_FORM, new AbstractAsyncCallback<HashMap<String, ColumnConfigs[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(HashMap<String, ColumnConfigs[]> result) {
                if (result != null && result.size() > 0) {
                    for (Map.Entry<String, ColumnConfigs[]> configMap : result.entrySet()) {

                        ProjectEditView.this.configMap = result;

                        String fieldID = configMap.getKey();
                        ColumnConfigs[] configs = configMap.getValue();
                        if (configs != null && configs.length == 0) {
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


    protected void registerFields() {
        richText = createRichText();
        richText.addStyleName(DEFAULT_WIDTH);

        parent = new DataListBox();
        parent.addStyleName(DEFAULT_WIDTH);

        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);

        number = new Numbering();
        number.addStyleName(DEFAULT_WIDTH);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_NUMBERING)) {
            number.setEnabled(true);
        }
        number.getTxtNumber().addValueChangeHandler(handler);
        number.getLastTxt().addValueChangeHandler(handler);
        number.getTxtPrefix().addValueChangeHandler(handler);

        startDate = new DatePicker(true);
        startDate.addStyleName(DEFAULT_WIDTH);
        dueDate = new DatePicker(true);
        dueDate.addStyleName(DEFAULT_WIDTH);

        client = new PmClientsLookUp();
        client.showClearButton();
        client.addStyleName(DEFAULT_WIDTH);

        multiClientTable = new MultiTableNewUI(10, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getClientMap(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        checkInLocations = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getCheckInLocations(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        locationBox = new DataListBox();
        locationBox.addStyleName(DEFAULT_WIDTH);
        locationBox.setAllowFirstItem(true);

        statusTable = new FlexTable();
        status = new DataListBox();
        status.setIdAttribute("project-status");
        changeTasksStatus = new KpiCheckBox(wfmStrings.alsoCloseAllTasksForAllMembersWithinTheProject(), true);
        statusTable.setWidget(0, 0, status);
        status.addStyleName(DEFAULT_WIDTH);
        status.setAllowFirstItem(true);
        //project change status permission
        status.setEnabled(Utils.hasPermission(PermissionConstants.PM_PROJECT_CHANGE_STATUS));
        changeTasksStatus.setEnabled(Utils.hasPermission(PermissionConstants.PM_PROJECT_CHANGE_STATUS));

        status.addValueChangeHandler(changeEvent -> {

            if (Constants.PS_CLOSED.equals(status.getSelectedItem().getDescription()) || Constants.PS_COMPLETED.equals(status.getSelectedItem().getDescription())) {
                if (statusTable.getRowCount() < 2) {
                    statusTable.setWidget(statusTable.getRowCount(), 0, changeTasksStatus);
                }
                changeTasksStatus.setVisible(true);
                changeTasksStatus.setValue(Utils.isLockCompletedProjecItems());
            } else {
                changeTasksStatus.setVisible(false);
            }

        });
        fileUpload = new GeneralFileUpload(F_PROJECT, projectID, projectID);
        fileUpload.ensureDebugId(editProject + "fileUpload");

        number.ensureDebugId(editProject + "number");
        parent.ensureDebugId(editProject + "parent");
        name.ensureDebugId(editProject + "name");
        richText.ensureDebugId(editProject + "richText");
        startDate.ensureDebugId(editProject + "startDate");
        dueDate.ensureDebugId(editProject + "dueDate");
        client.ensureDebugId(editProject + "client");
        locationBox.ensureDebugId(editProject + "locationBox");
        statusTable.ensureDebugId(editProject + "statusTable");

        copyNewEmployeesToProjectTasks = new KpiCheckBox("&nbsp;" + wfmStrings.assignNewMembersToProjectTasks(), true);
        copyNewEmployeesToProjectTasks.setVisible(false);

        addFields();

        show();
    }

    public void defferedLoading() {
        if (project.getClientId() != null) {
            projectService.getClient(project.getClientId(), new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem result) {
                    if (result != null) {
                        client.addItem(result);
                    }
                }
            });
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, ProjectEditView.this, (sender, args) -> {
            if (args != null && (args instanceof Integer)) {
                getCompanyLocations((Integer) args);
            } else {
                getCompanyLocations(project.getLocationId());
            }
        });
        getCompanyLocations(project.getLocationId());

        final Command projLocation = () -> getCompanyLocations(project.getLocationId());
        SimpleLink addNewLocation = new SimpleLink(wfmStrings.addLocation());
        addNewLocation.ensureDebugId(editProject + "addNewLocation");
        addNewLocation.setWidth("90px");
        addNewLocation.addClickHandler(event -> new AddLocationView(projLocation));

        projectService.getProjectStatuses(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void success(final SelectItem[] statusItems) {
                Scheduler.get().scheduleDeferred(() -> {
                    addPredefinedValues(CustomFormConstants.STATUS, statusItems);
                    status.setItems(statusItems);
                    status.setSelected(project.getStatusId());

                });
            }
        });

        if (isSetupSubProject) {
            projectService.getParentIsNullProjects(project.getObjectId(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] selectItems) {
                    parent.setItems(selectItems);
                    parent.setSelected(project.getParentId());
                }
            });
        }

    }

    private void addFields() {
        addTitleField(CustomFormConstants.DETAILS, property.getSingular(wfmStrings.basicDetails(), wfmStrings.project()));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }


        if (isSetupSubProject) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null) {
                addField(CustomFormConstants.PARENT, parent, getTitle(formPropertyMap.get(CustomFormConstants.PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PARENT).getTitle() : wfmStrings.parent()));
            } else {
                addField(CustomFormConstants.PARENT, parent, getTitle(wfmStrings.parent()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, richText, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description()));
        } else {
            addField(CustomFormConstants.DESCRIPTION, richText, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate()));
        } else {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate()));
        } else {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(wfmStrings.dueDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE) != null) {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).getTitle() : wfmStrings.billable()));
        } else {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }

        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
            addField(CustomFormConstants.PROJECT.CLIENT, multiClientTable, getTitle(Property.getPluralWithObjectCode(Constants.CLIENT_LIST, wfmStrings.customers())));
        } else {
            addField(CustomFormConstants.PROJECT.CLIENT, client, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null) {
            addField(CustomFormConstants.PROJECT.LOCATION, locationBox, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.LOCATION, locationBox, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, statusTable, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(CustomFormConstants.STATUS, statusTable, getTitle(wfmStrings.status()));
        }

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(ProjectEditView.this, projectID);
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());

        reminder = new Reminder(false);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER) != null) {
            addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).getTitle() : wfmStrings.duedatereminder()));
        } else {
            addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(wfmStrings.duedatereminder()));
        }
        //Notes
        NoteWidget noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null) {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getTitle() : wfmStrings.notes()));
        } else {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes());
        }

        addTitleField(CustomFormConstants.PROJECT.CHECK_IN_LOCATIONS, wfmStrings.checkInLocations());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION) != null) {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).getTitle() : null, formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations,null);
        }

    }

    private boolean validate() {
        int errors;
        boolean skipNumberValidation = project.isCrmActivityProject() || project.isDefaultProject();
        errors = super.customValidate();
        if (!skipNumberValidation) {
            errors += markAsError(CustomFormConstants.NUMBER, number, !number.validate());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.NAME, name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(area, !Validation.validateTextAreaRequired(area));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.START_DATE, startDate, !Validation.validateDate(startDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.DUE_DATE, dueDate, !Validation.validateDate(dueDate));
        }

        if (errors == 0) {
            errors += markAsError(CustomFormConstants.START_DATE, startDate, !Validation.validateDateEqualOrAfter(DateUtil.resetTime(startDate.getDate()), DateUtil.resetTime(dueDate.getDate()), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, !Validation.validateListBoxRequired(employeeAssignment));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER) != null && formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, !reminder.validateDueReminder());
        }

        if (isSetupSubProject && formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null && formPropertyMap.get(CustomFormConstants.PARENT).isRequired()) {
            errors += markAsError(CustomFormConstants.PARENT, parent, !Validation.validateListBoxRequired(parent));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.LOCATION, locationBox, !Validation.validateListBoxRequired(locationBox));
        }

        errors += getCustomFieldUtil().validateCustomFields();
        if (!Utils.isEmployeeAssignmentEnable()) {
            errors += markAsError(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, membersSelector, membersSelector.getSelectedData().size() == 0);
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANAGER) != null && formPropertyMap.get(CustomFormConstants.MANAGER).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.MANAGER, manager, manager.getSelectedId() == null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, !backupManagerTable.isFilled());
        }

        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private Widget createRichText() {
        area = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        area.getTextArea().addStyleName("textArea-white-space");
        area.setWidth("100%");
        return area;
    }

    public void updateProject() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        project.setNumberData(number.getNumberData(true));
        project.setNumber(number.getNumberData(true).getNumberString());
        project.setName(name.getText());
        project.setDescription(area.getText());
        project.setDueDate(DateTimePicker.getDateTime(dueDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
        project.setStartDate(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
        project.setAttachments(fileUpload.getAttachedFiles());
        project.setBillable(billable.getValue());
        if (isSetupSubProject) {
            project.setParentId(parent.getSelectedId());
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
            ArrayList<SelectItem> clients = new ArrayList<>();
            for (WidgetsMap map : multiClientTable.getWidgetsMaps()) {
                CRMLookUp clientLookUp = (CRMLookUp) map.getWidget(CUSTOMER);
                if (clientLookUp != null && clientLookUp.getSelectedItemID() != null) {
                    clients.add(clientLookUp.getSelectedItem());

                }
            }
            project.setClients(clients.toArray(new SelectItem[]{}));
        }
        if (client.getSelectedItem() != null) {
            project.setClientId(client.getSelectedItem().getId());
        } else {
            project.setClientId(null);
        }
        if (locationBox.getSelectedItem() != null) {
            project.setLocationId(locationBox.getSelectedItem().getId());
        }
        if (status.getSelectedItem() != null) {
            project.setStatusId(status.getSelectedItem().getId());
            if (Constants.PS_CLOSED.equals(status.getSelectedItem().getDescription()) || Constants.PS_COMPLETED.equals(status.getSelectedItem().getDescription())) {
                project.setChangeTaskStatus(changeTasksStatus.getValue());
            } else {
                project.setChangeTaskStatus(false);
            }
        }

        ArrayList<CheckInLocationItem> checkInLocationItems = new ArrayList<>();
        for (HashMap<String, Widget> widget : checkInLocations.getWidgets()) {
            if (widget != null) {
                if (widget.get("LOCATION") instanceof CheckInLocationWidget) {
                    CheckInLocationWidget locationWidget = (CheckInLocationWidget) widget.get("LOCATION");
                    if (!locationWidget.latitude.getValue().isEmpty() && !locationWidget.longitude.getValue().isEmpty()) {
                        checkInLocationItems.add(locationWidget.getCheckInLocationItem());
                    }
                }
            }
        }
        project.setCheckInLocations(checkInLocationItems);


        project.setReminders(reminder.getReminderDatas());
        if (firstClick.get()) {
            project.setRelations(project.getRelations());
        } else {
            project.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        project.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        if (manager.getSelectedItem() != null) {
            project.setManagerId(manager.getSelectedItem().getId());
        }
        project.setCustomTableItems(getCustomObjectData());
        project.setCopyNewEmployeesToProjectTasks(copyNewEmployeesToProjectTasks.getValue());

        if (Utils.isEmployeeAssignmentEnable()) {
            project.setEmployeeAssignment(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()));
        }

        if (Utils.isEmployeeAssignmentEnable() && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()))) {
            project.setProjectPositions(projectPositionWidget.getProjectPositions());
            project.setProjectMemberFromTreeInfo(projectPositionWidget.getProjectMembers());
        } else {
            project.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
        }

        ArrayList<Integer> backupManagerIDs = new ArrayList<>();
        for (Map<String, Widget> row : backupManagerTable.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                    if (!backupManagerIDs.contains(db.getSelectedItem().getId())) {
                        backupManagerIDs.add(db.getSelectedItem().getId());
                    }
                }
            }
        }
        project.setBackupManagerIDs(backupManagerIDs);

        projectService.isProjectNumberExists(project.getNumber(), projectID, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                enableButton(true);
                try {
                    throw caught;
                } catch (NumberExistingException ex) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
                    messageBox.setTitle(wfmStrings.error());
                    messageBox.open();
                } catch (Throwable ex) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }

            @Override
            public void success(Boolean exist) {
                if (exist) {
                    Info.show(projectStrings.youAlreadyHaveAProjectWithThisNumber(), Info.Type.WARNING);
                    enableButton(true);
                } else {
                    LoadingPanel.loading(true);
                    projectService.updateProject(project, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            enableButton(true);
                            LoadingPanel.loading(false);
                            try {
                                throw caught;
                            } catch (NumberExistingException ex) {
                                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
                                messageBox.setTitle(wfmStrings.error());
                                messageBox.open();
                            } catch (Throwable ex) {
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }
                        }

                        @Override
                        public void success(Void result) {
                            enableButton(true);
                            LoadingPanel.loading(false);
                            if (project.getParentId() != null) {// in sub project list add project event
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_EDIT, projectID, ProjectEditView.this);
                            } else {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_EDIT, projectID, ProjectEditView.this);
                            }
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                            closeTab("project|summary/" + project.getObjectId());
                        }
                    });
                }
            }
        });
    }

    protected Widget onInitialize() {
        isSetupSubProject = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SUB_PROJECT) || Boolean.valueOf(Utils.userSettings.get(Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL));
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Project, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                ProjectEditView.super.onInitialize();
            }
        });
        getFirstRequest();
        CommonService.App.get().getCompanyCustomFields(ViewName.ProjectItemTable, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    result.forEach(item -> itemCustomCFs.computeIfAbsent(item.getEntityCategoryName(), v -> new ArrayList<>()).add(item));
                }
                drawItemTable();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, ProjectEditView.this, (sender, args) -> setManagers());
        return null;
    }

    private void getFirstRequest() {
        clear();
        LoadingPanel.loading(true);
        if (Utils.hasPermission(PermissionConstants.PM_ADD_ASSIGNEES_TO_PROJECT)) {
            hasEmployeeAssignRole = true;
        }

        if (Utils.getDefaultProjectID() == 0) {
            projectService.getDefaultProjectID(new AbstractAsyncCallback<Integer>() {
                @Override
                public void success(Integer projectID) {
                    Utils.setDefaultProjectID(projectID);
                }
            });
        }
        EmployeeService.App.get().getProjectEmployeeEditablePermmission(projectID, new AbstractAsyncCallback<Integer>() {
            @Override
            public void success(Integer result) {
                if (result == EDIT || Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT)) {
                    initEditForm();
                } else {
                    initViewForm();
                }

            }
        });
    }

    private void initEditForm() {
        // Employee Assignment
        employeeAssignment = new DataListBox();
        employeeAssignment.setVisibleItemCount(1);
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.setWithoutNullLabel(true);
        SelectItem[] eaTypes = new SelectItem[]{new SelectItem(EmployeeAssignmentEnum.BY_POSITION.getId(), EmployeeAssignmentEnum.BY_POSITION.getTitle()),
                new SelectItem(EmployeeAssignmentEnum.BY_EMPLOYEE.getId(), EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle())};
        addPredefinedValues(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, eaTypes);
        employeeAssignment.setItems(eaTypes);
        employeeAssignment.setVisible(Utils.isEmployeeAssignmentEnable());
        employeeAssignment.setEnabled(false);

        pnlEmployeeAssignmentContainer = new FlowPanel();

        projectPositionWidget = new ProjectPositionWidget(projectID, false);

        membersSelector = new KpiCellTree();
        membersSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    setManagers();
                    if (!manager.isSomethingSelected()) {
                        setManagers();
                    }
                });
                //Employee Name Blow
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, wfmStrings.employee());
                selectedDataGrid.setColumnWidth(employee, 32, Style.Unit.PCT);

                if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_RATE_HISTORY)) {
                    //Wage Rate
                    Column<KpiTreeInfo, String> wageRate = null;
                    if (Utils.hasGenericAccess(GenericSettingsEnum.IS_DISABLED_WAGE_RATE)) {
                        wageRate = new Column<KpiTreeInfo, String>(new TextCell()) {
                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                            }
                        };
                    } else {
                        final TextInputCell wageRateCell = new TextInputCell("debug_input-wageRate form-control-sm");
                        wageRate = new Column<KpiTreeInfo, String>(wageRateCell) {
                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                            }
                        };
                        wageRate.setFieldUpdater((index, object, value) -> object.setWageRate(Double.valueOf(value)));
                    }
                    selectedDataGrid.addColumn(wageRate, wfmStrings.wageRate());
                    selectedDataGrid.setColumnWidth(wageRate, 17, com.google.gwt.dom.client.Style.Unit.PCT);
                    //client Wage Rate
                    final TextInputCell clientRateCell = new TextInputCell("debug_input-client--chargeRate form-control-sm");
                    Column<KpiTreeInfo, String> clientRate = new Column<KpiTreeInfo, String>(clientRateCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return object.getClientChargeRate() != null ? object.getClientChargeRate().toString() : "0.00";
                        }
                    };
                    clientRate.setFieldUpdater((index, object, value) -> object.setClientChargeRate(Double.valueOf(value)));
                    selectedDataGrid.addColumn(clientRate, wfmStrings.clientChargeRate());
                    selectedDataGrid.setColumnWidth(clientRate, 17, com.google.gwt.dom.client.Style.Unit.PCT);

                    //Workload Percentage
                    final TextInputCell employeeWorkloadPercentage = new TextInputCell("debug_input-workload--percentage form-control-sm");
                    Column<KpiTreeInfo, String> workloadPercentage = new Column<KpiTreeInfo, String>(employeeWorkloadPercentage) {
                        @Override
                        public String getValue(KpiTreeInfo object) {
                            return object.getWorkloadPercentage() != null ? object.getWorkloadPercentage().toString() : "0";
                        }
                    };
                    workloadPercentage.setFieldUpdater((index, object, value) -> {

                        try {
                            Float percentageFloatValue = Float.valueOf(value);
                            if (percentageFloatValue < 0) {
                                Info.show(wfmStrings.percentInvolvementValueCanOnlyBeBetween0And100(), Info.Type.WARNING);
                                percentageFloatValue = 0f;
                                employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            } else if (percentageFloatValue > 100) {
                                Info.show(wfmStrings.percentInvolvementValueCanOnlyBeBetween0And100(), Info.Type.WARNING);
                                percentageFloatValue = !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED) ? 100f : percentageFloatValue;
                                employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                            }
                            object.setWorkloadPercentage(percentageFloatValue);
                        } catch (NumberFormatException ex) {
                            object.setWorkloadPercentage(0f);
                            employeeWorkloadPercentage.clearViewData(KpiTreeInfo.KEY_PROVIDER.getKey(object));
                        }
                    });
                    selectedDataGrid.addColumn(workloadPercentage, wfmStrings.workloadPercent());
                    selectedDataGrid.setColumnWidth(workloadPercentage, 18, Style.Unit.PCT);
                }
                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new IconCell("ficon--trash pointer")) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return null;
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                });
                if ((!Utils.getDefaultProjectID().equals(projectID) && hasEmployeeAssignRole)) {
                    selectedDataGrid.addColumn(action, "");
                    selectedDataGrid.setColumnWidth(action, 10, com.google.gwt.dom.client.Style.Unit.PCT);
                }
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });

        if (Utils.isEmployeeAssignmentEnable()) {
            addField(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, getTitle(wfmStrings.employeeAssignment()));
        }
        addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer/*membersSelector*/, getTitle(wfmStrings.members()));

        if (!Utils.isEmployeeAssignmentEnable()) {
            pnlEmployeeAssignmentContainer.add(membersSelector);
        }

        copyNewEmployeesToProjectTasks = new KpiCheckBox("&nbsp;" + wfmStrings.assignNewMembersToProjectTasks(), true);
        addField(CustomFormConstants.PROJECT.ASSIGN_NEW_MEMBERS_TO_PROJECT, copyNewEmployeesToProjectTasks, null);
        copyNewEmployeesToProjectTasks.setVisible(false);

        manager = new DataListBox();
        manager.addStyleName(DEFAULT_WIDTH);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager()));
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(wfmStrings.manager()));
        }
        backupManagerTable = new MultiTableNewUI(10, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getBackupManagersMap(null);
            }

            @Override
            public boolean isFilled() {
                for (Map<String, Widget> backupManagerItem : backupManagerTable.getWidgets()) {
                    DataListBox listBox = (DataListBox) backupManagerItem.get(MultiTable.LIST_BOX);
                    if (listBox.getSelectedIndex() > 0) {
                        return true;
                    }
                }
                return false;
            }
        });
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers(), formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, wfmStrings.backupManagers());
        }


        manager.addValueChangeHandler(sender -> setManagers());

        if (Utils.getDefaultProjectID().equals(projectID) || !hasEmployeeAssignRole)
            membersSelector.showTreePanel(false);

        getDataToFillFields();
    }

    private WidgetsMap getCheckInLocations(CheckInLocationItem item) {
        final WidgetsMap widgetsMap = new WidgetsMap();
        CheckInLocationWidget checkInLocationWidget = item != null ? new CheckInLocationWidget(item.getId(), item.getLatitude(), item.getLongitude(), item.getRadius()) : new CheckInLocationWidget();
        widgetsMap.addWidgetToMap("LOCATION", checkInLocationWidget);
        widgetsMap.add("latitude", checkInLocationWidget.latitude);
        widgetsMap.add("longitude", checkInLocationWidget.longitude);
        widgetsMap.add("radius", checkInLocationWidget.radius);

        return widgetsMap;
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
                            CustomDatePicker t = (CustomDatePicker) productTable.getColumnById(i, columnCode);
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


    private void initMembers() {
        if (!(Utils.isEmployeeAssignmentEnable() && employeeAssignment.getSelectedId() != null && EmployeeAssignmentEnum.BY_POSITION.getId() == employeeAssignment.getSelectedId())) {
            EmployeeService.App.get().getProjectEmployeesForAddEdit(projectID, hasEmployeeAssignRole, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> members) {
                    if (membersSelector.getItems() == null || membersSelector.getItems().size() <= 0) {
                        membersSelector.setItems(members);
                    }
                    Integer userId = Utils.getUserID();
                    if (membersSelector.getSelectedData().size() == 1) {
                        setManagers();
                        if (manager.getItems().length == 1 && manager.getItems()[0] != null && manager.getSelectedItem() == null) {
                            manager.setSelected(manager.getItems()[0]);
                        }
                    }
                    if (!manager.isSomethingSelected()) {
                        manager.setSelected(userId);
                    }
                    Scheduler.get().scheduleDeferred(() -> {
                        setManagers();
                        manager.setSelected(project.getManagerId());
                        if (Utils.hasGenericAccess(GenericSettingsEnum.IS_COPY_NEW_EMPLOYEES_TO_PROJECT_TASKS)) {
                            copyNewEmployeesToProjectTasks.setVisible(true);
                        }
                        if (project.getBackupManagerIDs() != null && project.getBackupManagerIDs().size() > 0) {
                            backupManagerTable.removeAllRows();
                            for (Integer backupManagerID : project.getBackupManagerIDs()) {
                                backupManagerTable.addWidgets(getBackupManagersMap(backupManagerID));
                            }
                        }
                        setManagers();

                    });
                }
            });
        }
    }

    private void initViewForm() {
        members = new KpiDataGrid<>(KpiTreeInfo.KEY_PROVIDER);
        members.setWidth("300px");
        members.setHeight("200px");
        members.getElement().getStyle().setBorderWidth(1d, Style.Unit.PX);
        members.getElement().getStyle().setBorderColor("Grey");
        members.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        Column<KpiTreeInfo, String> department = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(final KpiTreeInfo object) {
                return members.refactor(object.getDepartmentName());
            }
        };

        members.addColumn(department, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()));
        members.setColumnWidth(department, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
            @Override
            public String getValue(final KpiTreeInfo object) {
                return members.refactor(object.getName());
            }
        };

        members.addColumn(employee, wfmStrings.employee());
        members.setColumnWidth(employee, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        Label managerLabel = new Label();
        Label backupManagerLabel = new Label();

        managerLabel.setText(project.getManagerName());
        backupManagerLabel.setText(project.getBackupManagerName());

        projectService.getProjectEmployeesForView(projectID, new AbstractAsyncCallback<KpiTreeInfo[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(KpiTreeInfo[] result) {
                LoadingPanel.loading(false);
                members.supplyProvider(result);
                members.refresh();
            }
        });
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE) != null) {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, members, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).getTitle() : wfmStrings.members()));
        } else {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, members, getTitle(wfmStrings.members()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerLabel, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerLabel, wfmStrings.backupManagers());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, managerLabel, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager()));
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, managerLabel, wfmStrings.manager());
        }
    }

    private void setManagers() {
        int selectedLeader = manager.getSelectedItem() != null ? manager.getSelectedItem().getId() : 0;
        manager.clear();
        manager.setSelectedNullLabel();
        manager.setEnabled(false);

        SelectItem[] selection = membersSelector.getSelectedItems();

        Set<SelectItem> managerList = new HashSet<>();
        if (Utils.isEmployeeAssignmentEnable() && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId() != null ? employeeAssignment.getSelectedId() : 0))
                && projectPositionWidget != null) {
            if (projectPositionWidget.getSelectedMemebers().length > 0) {
                managerList.addAll(Arrays.asList(projectPositionWidget.getSelectedMemebers()));
            }
        }
        if (Utils.isEmployeeAssignmentEnable() && systemManagers.size() > 0) {
            managerList.addAll(systemManagers);
            selection = managerList.toArray(new SelectItem[]{});
        }

        manager.setItems(selection);
        backupManagerItems = new SelectItem[selection.length];
        if (selectedLeader != 0) {
            manager.setSelected(selectedLeader);
            if (manager.getSelectedItem() != null) {
                backupManagerItems = new SelectItem[selection.length > 0 ? selection.length - 1 : 0];
            }
        }
        manager.setEnabled(manager.getItemCount() > 0);
        int i = 0;
        for (SelectItem item : selection) {
//            if (selectedLeader != item.getId()) {
                backupManagerItems[i] = item;
                i++;
//            }
        }

        int[] selectedBackupLeaderIDs = new int[backupManagerTable.getWidgets().size()];
        int count = 0;
        for (Map<String, Widget> row : backupManagerTable.getWidgets()) {
            if (row != null) {
                DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                    selectedBackupLeaderIDs[count] = db.getSelectedItem().getId();
                }
                count++;
            }
        }
        backupManagerTable.removeAllRows();
        for (int selectedBackupLeaderID : selectedBackupLeaderIDs) {
            backupManagerTable.addWidgets(getBackupManagersMap(selectedBackupLeaderID));
        }
    }

    private WidgetsMap getBackupManagersMap(Integer backupManagerID) {
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox backupManagersBox = new DataListBox();
        backupManagersBox.setEnabled(false);
        backupManagersBox.addStyleName(DEFAULT_WIDTH);
        widgetsMap.addWidgets(backupManagersBox);
        if (backupManagerItems != null) {
            backupManagersBox.setItems(backupManagerItems);
            backupManagersBox.setEnabled(true);
        }
        if (backupManagerID != null) {
            backupManagersBox.setSelected(backupManagerID);
        }
        widgetsMap.addWidgetToMap(MultiTable.LIST_BOX, backupManagersBox);
        return widgetsMap;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_PROJECT_EDIT;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PROJECT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void getDataToFillFields() {
        getEditProject();
    }

    @Override
    protected void addButtons() {

        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(project.getRelations(), false);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        WfmButton2 saveButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(editProject + "saveButton");
        saveButton.addClickHandler(sender -> updateProject());
        addButton(saveButton);
//        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
//        cancel.ensureDebugId(editProject + "cancelButton");
//        cancel.addClickHandler(clickEvent -> closeTab());
//        addButton(cancel);
    }

    private void getCompanyLocations(final Integer locationId) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(final SelectItem[] result) {
                Scheduler.get().scheduleDeferred(() -> {
                    addPredefinedValues(CustomFormConstants.PROJECT.LOCATION, result);
                    locationBox.setItems(result);
                    locationBox.setSelected(locationId);

                });
            }
        });
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

    private WidgetsMap getClientMap(SelectItem client) {
        WidgetsMap widgetsMap = new WidgetsMap();

        CRMLookUp clientLookUp = new CRMLookUp(LookUpConstants.CLIENT_ID);
        //clientLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        //widgetsMap.addWidgets(clientLookUp);

        if (client != null && client.getId() != null) {
            clientLookUp.addItem(client);
            clientLookUp.setSelected(client);
        }
        widgetsMap.addToCenter(Constants.CUSTOMER, clientLookUp);
        return widgetsMap;
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

    @Override
    public String getPropertyCode() {
        return PROJECT;
    }
}
