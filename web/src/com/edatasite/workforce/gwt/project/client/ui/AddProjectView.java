package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
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
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.CloneTaskItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
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
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroupAppend;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
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
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
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
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.edatasite.workforce.gwt.project.client.rpc.ContractViewItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.view.projectposition.ProjectPositionWidget;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;

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

/**
 * User: Anvarbek
 * Date: 15.01.2008
 * Time: 13:39:20
 */
public class AddProjectView extends CustomForm2 implements HasLinksInterface, CommandConstants, Constants, Colapse {

    private Integer parentId = null;
    private FooterInformer link;
    AtomicBoolean firstClick = new AtomicBoolean(true);

    public AddProjectView(String[] params) {
        super("addproject");
        setDescription(property.getSingular(Property.get(Constants.PROJECT, wfmStrings.project())));
        setParams(params);
    }

    private void setParams(String[] params) {
        if (params != null && params.length > 2 && "contract".equals(params[1])) {
            try {
                contractID = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {

            }
        } else if (params != null && params.length > 2) {
            projectFrom = params[1];
            try {
                projectFromID = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {

            }
        } else if (params != null && params.length == 2) {
            try {
                parentId = Integer.valueOf(params[1]);
            } catch (NumberFormatException ignored) {

            }
        }
    }

    private void getContractPositions(Integer contractID) {
        LoadingPanel.loading(true);
        ProjectService.App.get().viewContract(contractID, new AbstractAsyncCallback<ContractViewItem>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ContractViewItem item) {
                projectPositionWidget.setValues(item.getProjectPositions());
                refreshEmployeeAssignmentContainer();
                if (item.getClientId() != null) {
                    client.addItem(new SelectItem(item.getClientId(), item.getClient()));
                }
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddProjectView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                protected Integer getRelationID() {
                    return null;
                }

                @Override
                protected String getRelationType() {
                    return RelationItem.TYPE_PROJECT;
                }

                @Override
                protected String getRelationName() {
                    return null;
                }
            };
        }
        return linkingUtil;
    }

    private Integer projectID = null;
    private String projectFrom;
    private Integer projectFromID;
    private Integer contractID;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private final ProjectServiceAsync projectService = ProjectService.App.get();
    private TextBox name;
    private Numbering number;
    private DatePicker startDate;
    private DatePicker dueDate;
    public MultiTableNewUI checkInLocations;
    private DataListBox parent;
    private DataListBox manager;
    private HashSet<SelectItem> systemManagers = new HashSet<>();
    private MultiTableNewUI backupManagerTable;
    private SelectItem[] backupManagerItems;
    private PmClientsLookUp client;
    private MultiTableNewUI multiClientTable;
    private DataListBox status;

    private DataListBox employeeAssignment;
    private FlowPanel pnlEmployeeAssignmentContainer;

    private MaterialLink saveAndTaskButton;
    private MaterialLink saveButton;
    private MaterialLink saveCloseButton;
    private boolean saveAndClose = false;
    private boolean saveAndNew = false;
    private boolean addTask = false;

    private TextArea2 projectDescription;
    private TextArea2 area;
    private NoteWidget noteWidget =new NoteWidget(projectID, RelationItem.TYPE_PROJECT);

    private KpiCellTree membersSelector;
    private ProjectPositionWidget projectPositionWidget;

    private SimpleLink addNewEmployee;
    private WfmButton2 addNewClient;
    private Anchor addNewLocation;

    private GeneralFileUpload fileUpload;
    private boolean isSetupSubProject;

    private DataListBox locationBox;

    private NumberData numberData;

    private HasLinks linkingUtil;

    private KpiCheckBox copyClient;
    private KpiCheckBox copyLocation;
    private KpiCheckBox copyProjectAssignments;
    private KpiCheckBox copyTasks;
    private KpiCheckBox copyTaskAssignments;
    private KpiCheckBox adjustTaskDates;
    private KpiCheckBox resetTaskStatuses;
    private KpiCheckBox assignAllTaskToProjectMembers;
    private KpiCheckBox copyWorkstreams;
    private KpiCheckBox billable;
    private CRMLookUp templateProjects;
    private RadioButton rdBlankProject;
    private RadioButton rdFromTemplate;
    private WfmButton2 applyButton;
    private DisclosurePanel evenMore;
    private DataListBox inProgress;
    private final String addProject = "add_project_";
    private boolean hasEmployeeAssignRole = false;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private Map<String, EditableTable> editableTableMap = new HashMap<>();
    private Map<String, ColumnConfigs[]> configMap = new HashMap<>();
    private Map<String, List<CompanyCustomFieldItem>> itemCustomCFs = new LinkedHashMap<>();


    protected Widget onInitialize() {
        isSetupSubProject = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SUB_PROJECT) || Boolean.valueOf(Utils.userSettings.get(Constants.IS_SETUP_SUPPROJECT_TWO_LEVEL));
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Project, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                initFields();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                }
                initFields();
            }
        });

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
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, AddProjectView.this, (sender, args) -> setManagers());
        return null;
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

                        AddProjectView.this.configMap = result;

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
                    CustomDatePicker d = new CustomDatePicker(cfItem);
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

    private void initFields() {
        super.onInitialize();
    }

    @Override
    protected void registerFields() {
        // Create Project Clone Widgets
        createCloneWidgets();
        // Project Number

        number = new Numbering();
        number.addStyleName(DEFAULT_WIDTH);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PROJECT_NUMBERING)) {
            number.setEnabled(true);
        }
        //Description
        projectDescription = createRichText();
        projectDescription.addStyleName(DEFAULT_WIDTH);
        // Project Name
        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);
        // Project StartDate
        startDate = new DatePicker(true);
        startDate.addStyleName(DEFAULT_WIDTH);
        Date resetValue = new Date();
        DateUtil.resetTime(resetValue);
        startDate.setDate(resetValue);
        startDate.addChangeHandler(event -> {
            projectService.isDateExistsInNumbering(new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable caught) {
                }

                public void success(Boolean isExists) {
                    if (isExists) {
                        generateProjectNumber(startDate.getDate(), client.getSelectedItemID());
                    }
                }
            });
        });

        // Project DueDate
        dueDate = new DatePicker(true);
        dueDate.addStyleName(DEFAULT_WIDTH);
        // Project Parent
        parent = new DataListBox();
        parent.addStyleName(DEFAULT_WIDTH);
        // Project Manager
        manager = new DataListBox();
        manager.setVisibleItemCount(1);
        manager.addStyleName(DEFAULT_WIDTH);
        manager.setEnabled(false);
        manager.addValueChangeHandler(widget -> fillBackupManagersList());
        if (Utils.isEmployeeAssignmentEnable()) {
            getAllProjectManagers();
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
        // Project Client
        client = new PmClientsLookUp();
        client.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectService.isClientExistsInNumbering(new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable caught) {
                }

                public void success(Boolean isExists) {
                    if (isExists) {
                        generateProjectNumber(startDate.getDate(), client.getSelectedItemID());
                    }
                }
            });
        });
        client.addStyleName(DEFAULT_WIDTH);
        // Project multi client
        multiClientTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getClientMap(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        // Location
        locationBox = new DataListBox();
        locationBox.setVisibleItemCount(1);
        locationBox.addStyleName(DEFAULT_WIDTH);
        // Project Status
        status = new DataListBox();
        status.setVisibleItemCount(1);
        status.addStyleName(DEFAULT_WIDTH);

        checkInLocations = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getCheckInLocations();
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        // Employee Assignment
        employeeAssignment = new DataListBox();
        employeeAssignment.setVisibleItemCount(1);
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.setWithoutNullLabel(true);
        SelectItem[] eaTypes = new SelectItem[]{new SelectItem(EmployeeAssignmentEnum.BY_POSITION.getId(), EmployeeAssignmentEnum.BY_POSITION.getTitle()),
                new SelectItem(EmployeeAssignmentEnum.BY_EMPLOYEE.getId(), EmployeeAssignmentEnum.BY_EMPLOYEE.getTitle())};
        addPredefinedValues(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, eaTypes);
        employeeAssignment.setItems(eaTypes);
        employeeAssignment.setSelected(EmployeeAssignmentEnum.BY_POSITION.getId());
        employeeAssignment.addValueChangeHandler(changeEvent -> onEmployeeAssignmentChange());
        employeeAssignment.setVisible(Utils.isEmployeeAssignmentEnable());

        pnlEmployeeAssignmentContainer = new FlowPanel();
        pnlEmployeeAssignmentContainer.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        // Project Attachment
        fileUpload = new GeneralFileUpload(F_PROJECT, null, null);
        saveAndClose = false;

        addNewEmployee = new SimpleLink(wfmStrings.addEmployee());
        addNewEmployee.setWidth("520px");
        addNewEmployee.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        addNewEmployee.addClickHandler(widget -> goTo("employee|add/add"));

        addNewClient = new WfmButton2("", WfmButton2.BTN_WHITE);
        addNewClient.addStyleName("btn--icon");
        addNewClient.add(new SvgIcon(SvgEnum.plus));
        addNewClient.addClickHandler(widget -> goTo("client|add/add"));
        new KpiToolTip(addNewClient, projectStrings.addNewClient());

        final Command projLocation = () -> getCompanyLocations(null);

        addNewLocation = new Anchor(wfmStrings.addLocation());
        addNewLocation.addClickHandler(event -> new AddLocationView(projLocation));
        membersSelector = new KpiCellTree();
        membersSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
                    setManagers();
                    if (!manager.isSomethingSelected()) {
                        manager.setSelected(Utils.getUserID());
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
                        final TextInputCell wageRateCell = new TextInputCell();
                        wageRate = new Column<KpiTreeInfo, String>(wageRateCell) {
                            @Override
                            public String getValue(final KpiTreeInfo object) {
                                return object.getWageRate() != null ? object.getWageRate().toString() : "0.00";
                            }
                        };
                        wageRate.setFieldUpdater((index, object, value) -> object.setWageRate(Double.valueOf(value)));
                    }
                    selectedDataGrid.addColumn(wageRate, wfmStrings.wageRate());
                    selectedDataGrid.setColumnWidth(wageRate, 17, Style.Unit.PCT);
                    //client Wage Rate
                    final TextInputCell clientRateCell = new TextInputCell();
                    Column<KpiTreeInfo, String> clientRate = new Column<KpiTreeInfo, String>(clientRateCell) {
                        @Override
                        public String getValue(final KpiTreeInfo object) {
                            return object.getClientChargeRate() != null ? object.getClientChargeRate().toString() : "0.00";
                        }
                    };
                    clientRate.setFieldUpdater((index, object, value) -> object.setClientChargeRate(Double.valueOf(value)));
                    selectedDataGrid.addColumn(clientRate, wfmStrings.clientChargeRate());
                    selectedDataGrid.setColumnWidth(clientRate, 17, Style.Unit.PCT);

                    //Workload Percentage
                    final TextInputCell employeeWorkloadPercentage = new TextInputCell();
                    Column<KpiTreeInfo, String> workloadPercentage = new Column<KpiTreeInfo, String>(employeeWorkloadPercentage) {
                        @Override
                        public String getValue(KpiTreeInfo object) {
                            return object.getWorkloadPercentage() != null ? object.getWorkloadPercentage().toString() : "0.00";
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
                action.setFieldUpdater ((index, object, value) -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(wfmStrings.areYouSureWantToDeleteThe()+"  "+object.getName());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            object.setSelected(false);
                            selectionModel.setSelected(object, false);
                            List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                            contacts.remove(object);
                        }
                    });
                    messageBox.open();
                });

                selectedDataGrid.addColumn(action, "");
                selectedDataGrid.setColumnWidth(action, 10, Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });

        projectPositionWidget = new ProjectPositionWidget(null, false);
        projectPositionWidget.getPnlContainer().addStyleName("scroll-box--x");
        refreshEmployeeAssignmentContainer();
        if (Utils.isEmployeeAssignmentEnable() && contractID != null) {
            getContractPositions(contractID);
        }
        number.ensureDebugId(addProject + "number");
        parent.ensureDebugId(addProject + "parent");
        name.ensureDebugId(addProject + "projectName");
        projectDescription.ensureDebugId(addProject + "projectDescription");
        startDate.ensureDebugId(addProject + "startDate");
        dueDate.ensureDebugId(addProject + "dueDate");
        employeeAssignment.ensureDebugId(addProject + "employeeAssignment");
        membersSelector.ensureDebugId(addProject + "membersSelector");
        projectPositionWidget.ensureDebugId(addProject + "projectPositionWidget");
        projectPositionWidget.addStyleName("file--AddProjectView");
        manager.ensureDebugId(addProject + "manager");
        backupManagerTable.ensureDebugId(addProject + "backupManagerTable");
        status.ensureDebugId(addProject + "status");
        fileUpload.ensureDebugId(addProject + "fileUpload");


        //billable
        billable = new KpiCheckBox("");
        billable.ensureDebugId(addProject + "billable");
        billable.setValue(true);

        registrationEventBus();
        getCompanyLocations(null);
        getProjectAllData();
        if (Utils.hasPermission(PermissionConstants.PM_ADD_ASSIGNEES_TO_PROJECT)) {
            hasEmployeeAssignRole = true;
        }
        initMembers();
        if (projectFrom != null && projectFromID != null) {
            getProjectDataFrom();
        }
        addFields();
        setDefaultValues();
        if (projectFromID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    private WidgetsMap getCheckInLocations() {
        final WidgetsMap widgetsMap = new WidgetsMap();
        CheckInLocationWidget checkInLocationWidget = new CheckInLocationWidget();
        widgetsMap.addWidgetToMap("LOCATION", checkInLocationWidget);
        widgetsMap.add("latitude", checkInLocationWidget.latitude);
        widgetsMap.add("longitude", checkInLocationWidget.longitude);
        widgetsMap.add("radius", checkInLocationWidget.radius);

        return widgetsMap;
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue() != null) {
            number.getTxtNumber().setText(formPropertyMap.get(CustomFormConstants.NUMBER).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null) {
            name.setText(formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
            projectDescription.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue() != null) {
//            startDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.START_DATE).getDefaultValue()));
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
//            dueDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()));
            if (!"".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                dueDate.setDate(currentDate);
            } else {
                try {
                    dueDate.setDate(DateUtils.parse(formPropertyMap.get(CustomFormConstants.DUE_DATE).getDefaultValue()));
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }

        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getDefaultValue() != null) {
            employeeAssignment.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getDefaultValue()));
            refreshEmployeeAssignmentContainer();
        }

        if (isSetupSubProject) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null && formPropertyMap.get(CustomFormConstants.PARENT).getDefaultValue() != null) {
                parent.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PARENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PARENT).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).getDefaultValue() != null) {
            locationBox.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).getDefaultValue()));
        }

//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getDefaultValue() != null) {
//            backupManagerTable.setOnLinesRemoved(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getDefaultValue()));
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getDefaultValue() != null) {
            manager.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getDefaultValue() != null) {
            noteWidget.getTextBox().setText(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getDefaultValue());
        }
    }

    private void getAllProjectManagers() {
        projectService.getManagers(new AbstractAsyncCallback<HashSet<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                System.out.println("sorry");
            }

            @Override
            public void success(HashSet<SelectItem> selectItems) {
                systemManagers = selectItems;
                setManagers();
                addPredefinedValues(CustomFormConstants.PROJECT.MANAGER, systemManagers != null && systemManagers.size() > 0 ? systemManagers.toArray(new SelectItem[]{}) : null);
                setDefaultValues();
            }
        });
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

    private WidgetsMap getClientMap(SelectItem client) {
        WidgetsMap widgetsMap = new WidgetsMap();
        CRMLookUp clientLookUp = new CRMLookUp(LookUpConstants.CLIENT_ID);
        clientLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        widgetsMap.addWidgets(clientLookUp);

        if (client != null && client.getId() != null) {
            clientLookUp.addItem(client);
        }
        widgetsMap.addWidgetToMap(MultiTable.LOOK_UP_BOX, clientLookUp);
        return widgetsMap;
    }

    private void getProjectDataFrom() {
        if (projectFrom != null && projectFromID != null) {
            projectService.getProjectDetailsFrom(projectFrom, projectFromID, new AbstractAsyncCallback<EditProject>() {
                @Override
                public void failure(Throwable throwable) {
                    System.out.println();
                }

                @Override
                public void success(EditProject editProject) {
                    name.setText(editProject.getName());
                    projectDescription.setText(editProject.getName());
                    manager.setSelected(editProject.getManagerId());
                    client.setSelected(editProject.getClientId(), editProject.getClientName());
                    locationBox.setSelected(editProject.getLocationId());
                    setItemTableValues(editProject.getCustomTableItems());
                }
            });
        }
    }

    /**
     * Get Project All Data
     */
    private void getProjectAllData() {
        generateProjectNumber(new Date(), null);

        projectService.getProjectStatuses(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    addPredefinedValues(CustomFormConstants.STATUS, object);
                    status.setItems(object);
                    if (object != null && object.length > 0) {
                        status.setSelected(object[0].getId());
                    }
                    setDefaultValues();
                });
            }
        });


        if (isSetupSubProject) {
            projectService.getParentIsNullProjects(null, new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] selectItems) {
                    parent.setItems(selectItems);
                    if (parentId != null) {// when add project in sub project list view
                        parent.setSelected(parentId);
                        parent.setEnabled(false);
                    }
                }
            });
        }
    }

    private void generateProjectNumber(Date date, Integer clientId) {
        projectService.generateProjectNumber(date, clientId, null, new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable caught) {
            }

            public void success(NumberData result) {
                if (result != null) {
                    numberData = result;
                    number.setNumberData(numberData);
                }
            }
        });
    }

    /**
     * User Event Bus
     */
    private void registrationEventBus() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, AddProjectView.this, (sender, args) -> initMembers());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, AddProjectView.this, (sender, args) -> {
            if (args != null) {
                if (args instanceof Integer) {
                    initClients((Integer) args);
                } else {
                    initClients(((CrmAccountItem) args).getObjectId());
                }
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LOCATION_ADD, AddProjectView.this, (sender, args) -> {
            if (args != null && (args instanceof Integer)) {
                getCompanyLocations((Integer) args);
            } else {
                getCompanyLocations(null);
            }
        });
    }

    private Reminder reminder;

    private void addFields() {
        addTitleField(CustomFormConstants.DETAILS, property.getSingular(wfmStrings.basicDetails(), wfmStrings.project()));
//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET) != null) {
//            addField(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET, createCloneWidgets(), getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET).getTitle() : wfmStrings.owner(), formPropertyMap.get(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET).isRequired()));
//            ownerLookUp.setEnabled(!formPropertyMap.get(OWNER).isDisabled());
//        } else {
        addField(CustomFormConstants.PROJECT.CREATE_CLONE_WIDGET, createCloneWidgets());
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }

            number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(CustomFormConstants.NAME).getInformationText());
            }

            name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, projectDescription, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(projectDescription, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }

            projectDescription.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
        } else {
            addField(CustomFormConstants.DESCRIPTION, projectDescription, null);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null) {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(formPropertyMap.get(CustomFormConstants.START_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.START_DATE).getTitle() : wfmStrings.startDate(), formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.START_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.START_DATE).isInformation()) {
                new KpiToolTip(startDate, formPropertyMap.get(CustomFormConstants.START_DATE).getInformationText());
            }
            startDate.setEnabled(!formPropertyMap.get(CustomFormConstants.START_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.START_DATE, startDate, getTitle(wfmStrings.startDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null) {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(formPropertyMap.get(CustomFormConstants.DUE_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.DUE_DATE).getTitle() : wfmStrings.dueDate(), formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.DUE_DATE).isInformation()) {
                new KpiToolTip(dueDate, formPropertyMap.get(CustomFormConstants.DUE_DATE).getInformationText());
            }

            dueDate.setEnabled(!formPropertyMap.get(CustomFormConstants.DUE_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.DUE_DATE, dueDate, getTitle(wfmStrings.dueDate(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.STATUS).isInformation()) {
                new KpiToolTip(status, formPropertyMap.get(CustomFormConstants.STATUS).getInformationText());
            }

            status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.STATUS, status, getTitle(wfmStrings.status(), true));
        }


        if (Utils.isEmployeeAssignmentEnable()) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null) {
                addField(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getTitle() : wfmStrings.employeeAssignment(), formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isInformation());
                if (formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isInformation()) {
                    new KpiToolTip(employeeAssignment, formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getInformationText());
                }

                employeeAssignment.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isDisabled());
            } else {
                addField(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, getTitle(wfmStrings.employeeAssignment()));
            }
        }

        reminder = new Reminder(false);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER) != null) {
            addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).getTitle() : wfmStrings.duedatereminder(), formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).isInformation()) {
                new KpiToolTip(reminder, formPropertyMap.get(CustomFormConstants.PROJECT.DUE_DATE_REMINDER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(wfmStrings.duedatereminder(), false));
        }
//        addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER_ADD_LINK, pnlAddLink);
        if (isSetupSubProject) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null) {
                addField(CustomFormConstants.PARENT, parent, getTitle(formPropertyMap.get(CustomFormConstants.PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PARENT).getTitle() : wfmStrings.parent(), formPropertyMap.get(CustomFormConstants.PARENT).isRequired()), false,
                        formPropertyMap.get(CustomFormConstants.PARENT).isInformation());
                if (formPropertyMap.get(CustomFormConstants.PARENT).isInformation()) {
                    new KpiToolTip(parent, formPropertyMap.get(CustomFormConstants.PARENT).getInformationText());
                }

                parent.setEnabled(!formPropertyMap.get(CustomFormConstants.PARENT).isDisabled());
            } else {
                addField(CustomFormConstants.PARENT, parent, getTitle(wfmStrings.parent(), true));
            }
        }

        client.ensureDebugId(addProject + "client");
        WfmButton2 clearClient = new WfmButton2("", WfmButton2.BTN_WHITE);
        clearClient.addStyleName("btn--icon");
        clearClient.add(new SvgIcon(SvgEnum.x));
        clearClient.addClickHandler(sender -> {
            client.clearAndClearItems();
            client.setEnabled(true);
        });

        //client Widget
        AdvancedInputGroup clientGroup = new AdvancedInputGroup(client);
        InputGroupAppend clientAppend = new InputGroupAppend(clearClient, false);
        if (Utils.hasRole(PM) || Utils.hasRole(TL) || Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            addNewClient.ensureDebugId(addProject + "addNewClient");
//            addNewClient.addStyleName("btn--icon");
            clientAppend.add(addNewClient, false);
        }
        clientGroup.add(clientAppend);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
            addField(CustomFormConstants.PROJECT.CLIENT, multiClientTable, getTitle(Property.getPluralWithObjectCode(Constants.CLIENT_LIST, wfmStrings.customers())));
        } else {
            addField(CustomFormConstants.PROJECT.CLIENT, clientGroup, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        }

        //only Roberts company and AWS host and Company ID: 10520; Company Name: Catenate Group
        locationBox.ensureDebugId(addProject + "locationBox");
        addNewLocation.ensureDebugId(addProject + "addNewLocation");
        FlexTable locationPanel = new FlexTable();
        locationPanel.setWidget(0, 0, locationBox);
        locationPanel.setWidget(1, 0, addNewLocation);
        locationPanel.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null) {
            addField(CustomFormConstants.PROJECT.LOCATION, locationBox, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isInformation()) {
                new KpiToolTip(locationBox, formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).getInformationText());
            }

            locationBox.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isDisabled());
        } else {
            addField(CustomFormConstants.PROJECT.LOCATION, locationBox, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE) != null) {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).getTitle() : wfmStrings.billable(), formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isInformation()) {
                new KpiToolTip(billable, formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).getInformationText());
            }

            billable.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.BILLIBLE).isDisabled());
        } else {
            addField(CustomFormConstants.PROJECT.BILLIBLE, billable, getTitle(wfmStrings.billable()));
        }

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, null);

        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
            addNewEmployee.ensureDebugId(addProject + "addNewEmployee");
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS) != null) {
                addField(CustomFormConstants.ADD_NEW_ITEMS, addNewEmployee, getTitle(formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS).isChanged() ? formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS).getTitle() : wfmStrings.addEmployee(), formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS).isRequired()));
            } else {
                addField(CustomFormConstants.ADD_NEW_ITEMS, addNewEmployee);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE) != null) {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).getTitle() : wfmStrings.members(), formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer/*membersSelector*/, getTitle(wfmStrings.members()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers(), formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, getTitle(wfmStrings.backupManagers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager(), formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isInformation()) {
                new KpiToolTip(manager, formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getInformationText());
            }

            manager.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isDisabled());
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(wfmStrings.manager(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ATTACHMENTS) != null) {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, getTitle(formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isChanged() ? formPropertyMap.get(CustomFormConstants.ATTACHMENTS).getTitle() : wfmStrings.attachments(), formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isRequired()));
        } else {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null) {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isRequired()));
            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isDisabled());
            }
        } else {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        addTitleField(CustomFormConstants.PROJECT.CHECK_IN_LOCATIONS, wfmStrings.checkInLocations());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION) != null) {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).getTitle() : null, formPropertyMap.get(CustomFormConstants.PROJECT.CHECK_IN_LOCATION).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.CHECK_IN_LOCATION, checkInLocations,null);
        }
    }

    /**
     * Create Clone Project Widgets
     */
    private VerticalPanel createCloneWidgets() {
        // Clone Project Widgets Began
        evenMore = new DisclosurePanel();
        evenMore.addStyleName("clone-project__main");
        // Create Project Blank
        rdBlankProject = new KpiRadioButton("projecttype", "<b class=customTitle>" + property.getSingular(projectStrings.createBlankProject(), wfmStrings.project().toLowerCase()) + "</b>", true);
        rdBlankProject.ensureDebugId(addProject + "rdBlankProject");
        rdBlankProject.setValue(true);
        // Copy from existing project
        rdFromTemplate = new KpiRadioButton("projecttype", "<b class=customTitle>" + property.getSingular(projectStrings.copyFromExistingProject(), wfmStrings.project().toLowerCase()) + "</b>", true);
        rdFromTemplate.ensureDebugId(addProject + "rdFromTemplate");
        // Template Projects
        templateProjects = new CRMLookUp(LookUpConstants.PROJECT);
        templateProjects.setFullSearch(true);
        templateProjects.ensureDebugId(addProject + "project");
        templateProjects.setBeforeSearch(() -> {
            templateProjects.getFilterParametrs().setShowPA(true);  //retrieve completed/closed projects
        });
        // Applay Button
        applyButton = new WfmButton2(wfmStrings.apply());
        applyButton.ensureDebugId(addProject + "applyButton");
        // Copy Clients
        copyClient = new KpiCheckBox(projectStrings.copyClient(), true);
        copyClient.ensureDebugId(addProject + "copyClient");
        // Copy Locations
        copyLocation = new KpiCheckBox(projectStrings.copyLocation(), true);
        copyLocation.ensureDebugId(addProject + "copyLocation");
        // Copy Project Assigments
        copyProjectAssignments = new KpiCheckBox(property.getSingular(projectStrings.copyProjectAssignments(), wfmStrings.project()), true);
        copyProjectAssignments.ensureDebugId(addProject + "copyProjectAssignments");
        // Copy Taskks
        copyTasks = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(TASK, wfmStrings.copyTasks(), wfmStrings.tasks()), true);
        copyTasks.ensureDebugId(addProject + "copyTasks");
        // Copy Task Assigness
        copyTaskAssignments = new KpiCheckBox(projectStrings.copyTaskAssignments(), true);
        copyTaskAssignments.ensureDebugId(addProject + "copyTaskAssignments");
        // Task Dates
        adjustTaskDates = new KpiCheckBox(projectStrings.adjustTaskDates(), true);
        adjustTaskDates.ensureDebugId(addProject + "adjustTaskDates");
        // Reset Task Status
        resetTaskStatuses = new KpiCheckBox(projectStrings.resetTaskStatuses(), true);
        resetTaskStatuses.ensureDebugId(addProject + "resetTaskStatuses");
        // Enable copyWorkstreams to all
        copyWorkstreams = new KpiCheckBox(projectStrings.copyWorkstream());
        copyWorkstreams.ensureDebugId(addProject + "copyWorkstreams");
        // Costomise Fields
        String companyID = Utils.getEncryptedCompanyID();
        boolean isCustomized = CompanyConstants.C1.equals(companyID) || Utils.getHostURL().contains(HOST_AWS);
        if (isCustomized) {
            assignAllTaskToProjectMembers = new KpiCheckBox(projectStrings.assignAllTasksToProjectMembers());
            assignAllTaskToProjectMembers.ensureDebugId(addProject + "assignAllTaskToProjectMembers");
            assignAllTaskToProjectMembers.addClickHandler(event -> copyTaskAssignments.setValue(!assignAllTaskToProjectMembers.getValue()));
        }
        // Project Status
        SelectItem[] items = new SelectItem[4];
        items[0] = new SelectItem(2, wfmStrings.notStarted());
        items[1] = new SelectItem(3, wfmStrings.inProgress());
        items[2] = new SelectItem(79, wfmStrings.completed());
        items[3] = new SelectItem(173, wfmStrings.waitingForSomeone());
        inProgress = new DataListBox();
        inProgress.setWidth("6.7em");
        inProgress.ensureDebugId(addProject + "reset_task_statuses_texbox");
        inProgress.setItems(items);
        HorizontalPanel sHp = new HorizontalPanel();
        sHp.add(resetTaskStatuses);
        sHp.add(inProgress);
        // Clone Project Table
        FlexTable clonePanelFields = new FlexTable();
        clonePanelFields.setCellSpacing(5);

        HorizontalPanel hpp = new HorizontalPanel();
        hpp.setSpacing(5);
        hpp.add(new HTML("<b class=customTitle>" + property.getSingular(wfmStrings.project()) + ":</b>"));
        hpp.add(templateProjects);

        clonePanelFields.setWidget(0, 0, hpp);
        clonePanelFields.setWidget(1, 0, copyTasks);
        clonePanelFields.setWidget(1, 1, copyProjectAssignments);
        clonePanelFields.setWidget(2, 0, copyWorkstreams);
        clonePanelFields.setWidget(2, 1, copyTaskAssignments);
        clonePanelFields.setWidget(3, 0, copyClient);
        clonePanelFields.setWidget(3, 1, adjustTaskDates);
        if (CompanyConstants.C10520.equals(Utils.getEncryptedCompanyID()) || Utils.getHostURL().contains(HOST_AWS)) {//only Roberts company and AWS host and Company ID: 10520; Company Name: Catenate Group
            clonePanelFields.setWidget(4, 0, copyLocation);
            if (isCustomized) {
                clonePanelFields.setWidget(5, 0, assignAllTaskToProjectMembers);
            }
            clonePanelFields.setWidget(5, 1, applyButton);
        } else {
            if (isCustomized) {
                clonePanelFields.setWidget(4, 0, assignAllTaskToProjectMembers);
            }
            clonePanelFields.setWidget(5, 0, applyButton);
        }
        clonePanelFields.setWidget(4, 1, sHp);

        VerticalPanel vep = new VerticalPanel();
        vep.add(hpp);
        vep.add(clonePanelFields);
        evenMore.add(vep);
        HorizontalPanel hop = new HorizontalPanel();
        hop.addStyleName("clone-project__switch");
        hop.add(rdBlankProject);
        hop.add(new HTML("<div style='width:10px;'></div>"));
        hop.add(rdFromTemplate);

        VerticalPanel vp = new VerticalPanel();
        vp.add(hop);
        vp.add(evenMore);
        vp.setSpacing(15);
        registrationCloneProjectEvent();
        return vp;
    }

    /**
     * Clone Project Registration Events
     */
    private void registrationCloneProjectEvent() {
        rdBlankProject.addClickHandler(sender -> evenMore.setOpen(rdFromTemplate.getValue()));

        rdFromTemplate.addClickHandler(sender -> {
            evenMore.setOpen(rdFromTemplate.getValue());
            copyClient.setValue(rdFromTemplate.getValue());
            copyLocation.setValue(rdFromTemplate.getValue());
            copyProjectAssignments.setValue(rdFromTemplate.getValue());
            copyTasks.setValue(rdFromTemplate.getValue());
            copyTaskAssignments.setValue(rdFromTemplate.getValue());
            copyTaskAssignments.setValue(rdFromTemplate.getValue());
            adjustTaskDates.setValue(rdFromTemplate.getValue());
            resetTaskStatuses.setValue(rdFromTemplate.getValue());
        });

        templateProjects.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (templateProjects.getSuggestBox().getStyleName() != null) {
                templateProjects.getSuggestBox().removeStyleName("x-form-invalid");
            }

        });

        applyButton.addClickHandler(sender -> {
            if (rdFromTemplate.getValue()) {
                if (templateProjects.getSelectedItem() == null) {
                    templateProjects.addStyleName("x-form-invalid");
                } else {
                    ProjectService.App.get().getProjectForEdit(templateProjects.getSelectedItemID(), startDate.getDate(), client.getSelectedItemID(), new AbstractAsyncCallback<EditProject>() {
                        public void failure(Throwable caught) {

                        }

                        public void success(final EditProject project) {

                            name.setText(project.getName() + "#" + (DateUtils.format(new Date())));
                            area.setText(project.getDescription());
                            if (copyClient.getValue()) {
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
                            }

                            if (copyProjectAssignments.getValue()) {
                                EmployeeService.App.get().getProjectEmployeesForAddEdit(templateProjects.getSelectedItemID(), hasEmployeeAssignRole, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
                                    public void failure(Throwable caught) {

                                    }

                                    public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> members) {

                                        membersSelector.setItems(members);
                                        if (project.getManagerId() != null) {
                                            manager.setSelected(project.getManagerId());
                                        }
                                        setManagers();
                                    }
                                });
                            }

                            if (copyLocation.getValue()) {
                                if (locationBox.getItems().length > 0 && project.getLocationId() != null) {
                                    locationBox.setSelected(project.getLocationId());
                                }
                            }

                            //clear existing project's custom field ids for creating new project
                            if (project.getCustomFieldItems() != null && !project.getCustomFieldItems().isEmpty()) {
                                for (CompanyCustomFieldItem customFieldItem : project.getCustomFieldItems()) {
                                    customFieldItem.setObjectId(null);
                                }
                            }
                            getCustomFieldUtil().setCompanyCustomFieldItems(project.getCustomFieldItems());
                            getCustomFieldUtil().fillCustomFieldsWithData(project.getCustomFieldItems(), false);
                        }
                    });
                }
            } else {
                copyClient.setValue(false);
                copyLocation.setValue(false);
                copyProjectAssignments.setValue(false);
                copyTaskAssignments.setValue(false);
                copyTasks.setValue(false);
                adjustTaskDates.setValue(false);
                resetTaskStatuses.setValue(false);
                reinit();
            }
        });

        if (assignAllTaskToProjectMembers != null) {
            assignAllTaskToProjectMembers.addClickHandler(sender -> {
                copyTasks.setValue(true);
                copyTaskAssignments.setValue(!assignAllTaskToProjectMembers.getValue());
            });
        }

        copyTasks.addClickHandler(sender -> {
            if (assignAllTaskToProjectMembers != null) {
                assignAllTaskToProjectMembers.setValue(false);
            }
            copyTaskAssignments.setValue(copyTasks.getValue());
            adjustTaskDates.setValue(copyTasks.getValue());
            resetTaskStatuses.setValue(copyTasks.getValue());
        });

        copyTaskAssignments.addClickHandler(event -> {
            copyTasks.setValue(true);
            if (assignAllTaskToProjectMembers != null) {
                assignAllTaskToProjectMembers.setValue(!copyTaskAssignments.getValue());
            }
        });

        adjustTaskDates.addClickHandler(event -> {
            if (!copyTasks.getValue()) {
                adjustTaskDates.setValue(false);
            }
        });

        resetTaskStatuses.addClickHandler(event -> {
            if (!copyTasks.getValue()) {
                resetTaskStatuses.setValue(false);
            }
        });

        inProgress.addValueChangeHandler(sender -> {
            if (inProgress.getStyleName() != null) {
                inProgress.removeStyleName("x-form-invalid");
            }
        });

    }

    /**
     *
     */
    private void setManagers() {
        int selectedLeader = manager.getSelectedItem() != null ? manager.getSelectedItem().getId() : 0;
        manager.clear();
        manager.setSelectedNullLabel();
        manager.setEnabled(false);

        SelectItem[] selection = membersSelector.getSelectedItems();
        Set<SelectItem> managerList = new HashSet<>();
        if (Utils.isEmployeeAssignmentEnable() && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()))
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
            if (selectedLeader != item.getId()) {
                backupManagerItems[i] = item;
                i++;
            }
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
        if (Utils.isEmployeeAssignmentEnable() && employeeAssignment.getSelectedId() != null && EmployeeAssignmentEnum.BY_POSITION.getId() == employeeAssignment.getSelectedId()) {
            return;
        }
        if (!(membersSelector.getItems() == null || membersSelector.getItems().isEmpty())) {
            return;
        }
        EmployeeService.App.get().getProjectEmployeesForAddEdit(null, hasEmployeeAssignRole, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> object) {
                membersSelector.setItems(object);
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
            }
        });
    }

    private void initClients(final Integer clientId) {
        projectService.getClients(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    client.setItems("", object);
                    if (clientId != null) {
                        client.setSelected(clientId);
                    }
                    setDefaultValues();
                });
            }
        });
    }

    private void getCompanyLocations(final Integer locationId) {
        ReportService.App.get().getLocationList(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable throwable) {
            }

            public void success(SelectItem[] selectItems) {
                addPredefinedValues(CustomFormConstants.PROJECT.LOCATION, selectItems);
                locationBox.setItems(selectItems);
                if (locationId != null) {
                    locationBox.setSelected(locationId);
                }
                setDefaultValues();
            }
        });
    }

    private boolean validate() {
        clearErrorStyle();
        int errors;

        errors = super.customValidate();

        if (rdFromTemplate.getValue()) {
            if (templateProjects.getSelectedItem() == null) {
                templateProjects.addStyleName("x-form-invalid");
                errors++;
            }
            if (resetTaskStatuses.getValue() && inProgress.getSelectedItem() == null) {
                inProgress.addStyleName("x-form-invalid");
                errors++;
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.NUMBER, number, !number.validate() && !Validation.validateTextBoxRequired(number.getTxtPrefix()) && !Validation.validateTextBoxRequired(number.getTxtNumber()) && !Validation.validateTextBoxRequired(number.getLastTxt()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged()
                    ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(projectDescription, !Validation.validateTextAreaRequired(projectDescription));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.START_DATE) != null && formPropertyMap.get(CustomFormConstants.START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.START_DATE, startDate, !Validation.validateDate(startDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DUE_DATE) != null && formPropertyMap.get(CustomFormConstants.DUE_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.DUE_DATE, dueDate, !Validation.validateDate(dueDate));
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
        if (isSetupSubProject) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null && formPropertyMap.get(CustomFormConstants.PARENT).isRequired()) {
                errors += markAsError(CustomFormConstants.PARENT, parent, !Validation.validateListBoxRequired(parent));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION) != null && formPropertyMap.get(CustomFormConstants.PROJECT.LOCATION).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.LOCATION, locationBox, !Validation.validateListBoxRequired(locationBox));
        }

        if (Utils.hasPermission(PermissionConstants.PM_EMPLOYEE_ADD)) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS) != null && formPropertyMap.get(CustomFormConstants.ADD_NEW_ITEMS).isRequired()) {
                errors += markAsError(CustomFormConstants.ADD_NEW_ITEMS, addNewEmployee, !addNewEmployee.isVisible());
            }
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.MANAGER) != null && formPropertyMap.get(CustomFormConstants.MANAGER).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.MANAGER, manager, manager.getSelectedId() == null);
        }

        NoteWidget noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isRequired()) {
            if (noteWidget != null && !Validation.validateTextAreaRequired(noteWidget.getTextBox())) {
                errors++;
            }
        }
        if (errors == 0) {
            errors += markAsError(CustomFormConstants.START_DATE, startDate,
                    !Validation.validateDateEqualOrAfter(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()),
                            DateTimePicker.getDateTime(dueDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()), true)
            );
        }
        if (!Utils.isEmployeeAssignmentEnable()) {
            errors += markAsError(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, membersSelector, membersSelector.getSelectedData().size() == 0);
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

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        saveProject();
    }

    public void saveProject() {
        if (rdFromTemplate.getValue()) {
            saveAndTaskButton.setEnabled(false);
            saveButton.setEnabled(false);
            saveCloseButton.setEnabled(false);
            CloneProjectItem cloneItem = new CloneProjectItem();
            cloneItem.setProjectName(name.getText());
            cloneItem.setProjectDescription(area.getText());
            if (contractID != null) {
                cloneItem.setContractId(contractID);
            }
            cloneItem.setDueDate(DateTimePicker.getDateTime(dueDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
            cloneItem.setStartDate(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
            cloneItem.setStatusId(status.getSelectedItem() != null ? status.getSelectedItem().getId() : null);
            cloneItem.setClientId(client.getSelectedItem() != null ? client.getSelectedItem().getId() : null);
            if (isSetupSubProject) {
                cloneItem.setParentId(parent.getSelectedId());
                parentId = parent.getSelectedId();
            }
            if (templateProjects.getSelectedItem().getId() != null) {
                cloneItem.setProjectId(templateProjects.getSelectedItem().getId());
            }
            if (assignAllTaskToProjectMembers != null && assignAllTaskToProjectMembers.getValue()) {
                cloneItem.setCopyAssignmentsToAllProjectMembers(assignAllTaskToProjectMembers.getValue());
            }
            if (copyWorkstreams != null && copyWorkstreams.getValue()) {
                cloneItem.setCopyWorkstream(copyWorkstreams.getValue());
            }
            if (copyClient.getValue()) {
                cloneItem.setCopyClient(true);
            }
            if (copyLocation.getValue()) {
                cloneItem.setCopyProjectLocation(true);
            }
            if (copyProjectAssignments.getValue()) {
                cloneItem.setCopyAssignments(true);
            }
            if (copyTasks.getValue()) {
                cloneItem.setCopyTasks(true);
            }

            CloneTaskItem cloneTask = new CloneTaskItem();
            if (adjustTaskDates.getValue()) {
                cloneTask.setAdjustByProjectStartDate(true);
            }
            if (copyTaskAssignments.getValue()) {
                cloneTask.setCopyTaskAssignments(true);
            }
            if (resetTaskStatuses.getValue()) {
                cloneTask.setStatus(inProgress.getSelectedItem() != null ? inProgress.getSelectedItem().getId() : null);
            }
            cloneItem.setTaskItem(cloneTask);

            cloneItem.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
            cloneItem.setManager(manager.getSelectedItem().getId());
            if (client.getSelectedItem() != null) {
                cloneItem.setClientId(client.getSelectedItem().getId());
            }

            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
                ArrayList<SelectItem> clients = new ArrayList<>();
                for (HashMap<String, Widget> row : multiClientTable.getWidgets()) {
                    if (row != null) {
                        CRMLookUp clientLookUp = (CRMLookUp) row.get(MultiTable.LOOK_UP_BOX);
                        if (clientLookUp != null && clientLookUp.getSelectedItemID() != null) {
                            clients.add(clientLookUp.getSelectedItem());
                        }
                    }
                }
                cloneItem.setClients(clients.toArray(new SelectItem[]{}));
            } else if (client.getSelectedItem() != null) {
                cloneItem.setClientId(client.getSelectedItem().getId());
            }

            if (locationBox.getSelectedItem() != null) {
                cloneItem.setLocationId(locationBox.getSelectedItem().getId());
            }
            if (numberData != null) {
                numberData = number.getNumberData(true);
                cloneItem.setNumberData(numberData);
            }
            cloneItem.setBillable(billable.getValue());
//            cloneItem.setCustomFieldItems(getCustomFieldUtil().getCompanyCustomFieldItems());
            cloneItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            cloneItem.setAttachments(fileUpload.getAttachedFiles());
            if (!firstClick.get()) {
                cloneItem.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
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
            cloneItem.setBackupManagerIDs(backupManagerIDs);
            //project source
            if (templateProjects.getSelectedItemID() != null) {
                cloneItem.setProjectSource(PROJECT_SOURCE_COPY_FROM_PROJECT + templateProjects.getSelectedItemID());
            }

            LoadingPanel.loading(true);
            projectService.saveCloneProject(cloneItem, new AbstractAsyncCallback<Integer>() {

                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    saveAndTaskButton.setEnabled(true);
                    saveButton.setEnabled(true);
                    saveCloseButton.setEnabled(true);
                    try {
                        throw caught;
                    } catch (NumberExistingException ex) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK,
                                ex.getDetailedMessage(), null) {

                        };
                        messageBox.setTitle(wfmStrings.error());
                        messageBox.open();
                    } catch (Throwable ex) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }
                }

                public void success(Integer result) {
                    if (parentId != null) {// in sub project list add project event
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_ADD, result, AddProjectView.this);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_ADD, result, AddProjectView.this);
                    }
                    projectID = result;
                    LoadingPanel.loading(false);
                    saveAndTaskButton.setEnabled(true);
                    saveButton.setEnabled(true);
                    saveCloseButton.setEnabled(true);
                    Info.show(property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.project()), Info.Type.INFO);
                    shellOk();
                }
            });
        } else {
            ProjectSingleItem newProject = new ProjectSingleItem();
            newProject.setName(name.getText());
            if (contractID != null) {
                newProject.setContractId(contractID);
            }
            newProject.setDescription(area.getText());
            newProject.setStartDate(DateTimePicker.getDateTime(startDate.getDate(), Utils.getDefaultCurrentUserTimeSlotStartTIME()));
            newProject.setEndDate(DateTimePicker.getDateTime(dueDate.getDate(), Utils.getDefaultCurrentUserTimeSlotEndTIME()));
            ArrayList<HistoryListItem> unsavedNotes = noteWidget.getNewNotesToSave();
            if (unsavedNotes != null) {
                newProject.setNotes(unsavedNotes);
            }
            if (Utils.isEmployeeAssignmentEnable()) {
                newProject.setEmployeeAssignment(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()));
            }

            if (Utils.isEmployeeAssignmentEnable() && EmployeeAssignmentEnum.BY_POSITION.equals(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()))) {
                newProject.setProjectPositions(projectPositionWidget.getProjectPositions());
                newProject.setProjectMemberFromTreeInfo(projectPositionWidget.getProjectMembers());
            } else {
                newProject.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
            }

            if (manager.getSelectedItem() != null) {
                newProject.setManagerId(manager.getSelectedItem().getId());
            }

            if (isSetupSubProject) {
                newProject.setParentId(parent.getSelectedId());
                parentId = parent.getSelectedId();
            }
            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT)) {
                ArrayList<SelectItem> clients = new ArrayList<>();
                for (HashMap<String, Widget> row : multiClientTable.getWidgets()) {
                    if (row != null) {
                        CRMLookUp clientLookUp = (CRMLookUp) row.get(MultiTable.LOOK_UP_BOX);
                        if (clientLookUp != null && clientLookUp.getSelectedItemID() != null) {
                            clients.add(clientLookUp.getSelectedItem());
                        }
                    }
                }
                newProject.setClients(clients.toArray(new SelectItem[]{}));
            } else if (client.getSelectedItem() != null) {
                newProject.setClientId(client.getSelectedItem().getId());
            }
            if (locationBox.getSelectedItem() != null) {
                newProject.setLocationId(locationBox.getSelectedItem().getId());
            }
            if (numberData != null) {
                numberData = number.getNumberData(true);
                newProject.setNumberData(numberData);
            }

            if (getCustomObjectData() != null) {
                newProject.setCustomTableItems(getCustomObjectData());
            }

            newProject.setStatusId(status.getSelectedItem().getId());
            newProject.setAttachments(fileUpload.getAttachedFiles());
            newProject.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
            ArrayList<Integer> backupManagerIDs = new ArrayList<>();
            for (HashMap<String, Widget> row : backupManagerTable.getWidgets()) {
                if (row != null) {
                    DataListBox db = (DataListBox) row.get(MultiTable.LIST_BOX);
                    if (db != null && db.getSelectedItem() != null && db.getSelectedItem().getId() != null) {
                        if (!backupManagerIDs.contains(db.getSelectedItem().getId())) {
                            backupManagerIDs.add(db.getSelectedItem().getId());
                        }
                    }
                }
            }
            newProject.setBackupManagerIDs(backupManagerIDs);

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
            newProject.setCheckInLocations(checkInLocationItems);

            newProject.setReminder(reminder.getReminderDatas());
            LoadingPanel.loading(true);

            saveButton.setEnabled(false);
            saveCloseButton.setEnabled(false);
            saveAndTaskButton.setEnabled(false);
            if (!firstClick.get()) {
                newProject.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
            }

            newProject.setBillable(billable.getValue());

            projectService.saveProject(newProject, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    saveButton.setEnabled(true);
                    saveCloseButton.setEnabled(true);
                    saveAndTaskButton.setEnabled(true);
                    try {
                        throw caught;
                    } catch (NumberExistingException ex) {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK,
                                ex.getDetailedMessage(), null) {

                        };
                        messageBox.setTitle(wfmStrings.error());
                        messageBox.open();
                    } catch (Throwable ex) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }
                }

                public void success(final Integer result) {
                    LoadingPanel.loading(false);
                    if (parentId != null) {// in sub project list add project event
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_ADD, result, AddProjectView.this);
                        Info.show(Property.getPluralWithObjectCodeWithReplace(SUB_PROJECT_LIST, wfmStrings.messSuccessfullyAdded(), projectStrings.subproject()), Info.Type.INFO);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_ADD, result, AddProjectView.this);
                        Info.show(property.getSingular(wfmStrings.messSuccessfullyAdded(), wfmStrings.project()), Info.Type.INFO);
                    }
                    saveButton.setEnabled(true);
                    saveCloseButton.setEnabled(true);
                    saveAndTaskButton.setEnabled(true);
                    projectID = result;
                    shellOk();
                }

            });
        }

    }

    private void shellOk() {
        if (saveAndClose) {
            parentId = null;
            closeTab();
            if (addTask) {
                goTo("task|add/add/" + projectID.toString());
            } else {
                goTo("project|summary/" + projectID);
            }
        } else if (saveAndNew) {
            onInitialize();
        } else {
            reinit();
        }
    }

    public void reinit() {
        initForm();
        addFields();
        name.setText("");
        projectDescription.setText("");
        area.setText("");
        dueDate.clearSelected();

        manager.clear();
        backupManagerTable.removeAllRows();
        initMembers();
        generateProjectNumber(new Date(), null);
        client.clear();
        locationBox.clearSelected();
        fileUpload.clearAndAdd();
        startDate.setDate(new Date());
        if (status != null && status.getItems() != null) {
            for (SelectItem item : status.getItems()) {
                if (item.getName().trim().equals(wfmStrings.notStarted())) {
                    status.setSelected(item.getId());
                    break;
                }
            }
        }
    }

    private void fillBackupManagersList() {
        setManagers();
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_PROJECT_ADD;
    }

    private FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PROJECT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void addButtons() {
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
//                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);

                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        saveAndTaskButton = new MaterialLink(Property.get(TASK, projectStrings.saveAddProject(), wfmStrings.task()));
        saveButton = new MaterialLink(property.getSingular(projectStrings.saveAddProject(), wfmStrings.project()));

        saveCloseButton = new MaterialLink(wfmStrings.save());

        MaterialSplitButton splitButton = new MaterialSplitButton(saveCloseButton);
        splitButton.addItem(saveAndTaskButton);
        splitButton.addItem(saveButton);

        saveButton.ensureDebugId(addProject + "saveButton");
        saveAndTaskButton.ensureDebugId(addProject + "saveAndTaskButton");
        saveCloseButton.ensureDebugId(addProject + "saveCloseButton");

        saveButton.addClickHandler(sender -> {
            saveAndNew = true;
            save();
        });

        saveAndTaskButton.addClickHandler(sender -> {
            addTask = true;
            saveAndClose = true;
            saveAndNew = false;
            save();
        });

        saveCloseButton.addClickHandler(sender -> {
            saveAndClose = true;
            saveAndNew = false;
            save();
        });

        addButton(splitButton);
    }

    public String getIconStyle() {
        return null;
    }

    private TextArea2 createRichText() {
        area = new TextArea2(Constants.DEFAULT_DESCRIPTION_CHARACTER_LIMIT);
        area.setWidth("100%");
        return area;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            return getLocalizer().localizeByFieldID(getFormID(), fieldID);
        }
        return null;
    }

    private void onEmployeeAssignmentChange() {
        refreshEmployeeAssignmentContainer();
    }

    private void refreshEmployeeAssignmentContainer() {
        pnlEmployeeAssignmentContainer.clear();
        if (Utils.isEmployeeAssignmentEnable() && employeeAssignment.getSelectedId() != null && EmployeeAssignmentEnum.BY_POSITION.getId() == employeeAssignment.getSelectedId()) {
            pnlEmployeeAssignmentContainer.add(projectPositionWidget);
        } else {
            pnlEmployeeAssignmentContainer.add(membersSelector);
            initMembers();
        }
    }

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
