package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
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
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES;
import static com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType.ON_PROJECT_ADD;

public class AddBrigadaView extends CustomForm2 implements HasLinksInterface, CommandConstants, Constants, Colapse {

    AtomicBoolean firstClick = new AtomicBoolean(true);

    public AddBrigadaView(String[] params) {
        super("addproject");
        setDescription(property.getSingular(wfmStrings.addMess(), wfmStrings.project()));
        setParams(params);
    }

    private void setParams(String[] params) {

        if (params != null && params.length > 2) {
            projectFrom = params[1];
            try {
                projectFromID = Integer.parseInt(params[2]);
            } catch (NumberFormatException e) {
            }
        }
    }

    private NoteWidget noteWidget;

    private Integer projectID = null;
    private String projectFrom;
    private Integer projectFromID;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    private final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private TextBox name;
    private Numbering number;
    private DataListBox manager;
    private HashSet<SelectItem> systemManagers = new HashSet<>();
    private MultiTableNewUI backupManagerTable;
    private SelectItem[] backupManagerItems;
    private DataListBox status;
    private DataListBox employeeAssignment;
    private FlowPanel pnlEmployeeAssignmentContainer;
    private TextArea2 projectDescription;
    private TextArea2 area;
    private KpiCellTree membersSelector;
    private SimpleLink addNewEmployee;
    private GeneralFileUpload fileUpload;
    private MultiSelectEmployeeLookUp employeeLookUp;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(AddBrigadaView.this) {
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

    private NumberData numberData;
    private HasLinks linkingUtil;
    private final String addProject = "add_project_";
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BrigadaList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
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
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, AddBrigadaView.this, (sender, args) -> setManagers());
        return null;
    }

    private void initFields() {
        super.onInitialize();
    }

    @Override
    protected void registerFields() {

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

        Date resetValue = new Date();
        DateUtil.resetTime(resetValue);
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
        // Project Status
        status = new DataListBox();
        status.setVisibleItemCount(1);
        status.addStyleName(DEFAULT_WIDTH);

        // Employee Assignment
        employeeAssignment = new DataListBox();
        employeeAssignment.setVisibleItemCount(1);
        employeeAssignment.addStyleName(DEFAULT_WIDTH);
        employeeAssignment.setWithoutNullLabel(true);
        employeeAssignment.addValueChangeHandler(changeEvent -> onEmployeeAssignmentChange());

        pnlEmployeeAssignmentContainer = new FlowPanel();
        pnlEmployeeAssignmentContainer.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        // Project Attachment
        fileUpload = new GeneralFileUpload(F_PROJECT, null, null);

        addNewEmployee = new SimpleLink(wfmStrings.addEmployee());
        addNewEmployee.setWidth("520px");
        addNewEmployee.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        addNewEmployee.addClickHandler(widget -> goTo("employee|add/add"));

        employeeLookUp = new MultiSelectEmployeeLookUp();
        employeeLookUp.getFilterParametrs().setType(LookUpConstants.BRIGADA_ID);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);

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


                //client Wage Rate
                final TextInputCell labelcell = new TextInputCell();
                Column<KpiTreeInfo, String> label = new Column<KpiTreeInfo, String>(labelcell) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getUnit() != null ? object.getUnit() : "";
                    }
                };
                label.setFieldUpdater((index, object, value) -> object.setUnit(value));
                selectedDataGrid.addColumn(label, wfmStrings.note());
                selectedDataGrid.setColumnWidth(label, 17, Style.Unit.PCT);

                //Remove Action
                final Column<KpiTreeInfo, String> action = new Column<KpiTreeInfo, String>(new IconCell("ficon--trash pointer")) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return null;
                    }
                };
                action.setFieldUpdater((index, object, value) -> {
                    object.setSelected(false);
                    selectionModel.setSelected(object, false);
                    List<KpiTreeInfo> contacts = selectedDataGrid.getList();
                    contacts.remove(object);
                });
                selectedDataGrid.addColumn(action, "");
                selectedDataGrid.setColumnWidth(action, 10, Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });

        pnlEmployeeAssignmentContainer.add(membersSelector);
        number.ensureDebugId(addProject + "number");
        name.ensureDebugId(addProject + "projectName");
        projectDescription.ensureDebugId(addProject + "projectDescription");
        employeeAssignment.ensureDebugId(addProject + "employeeAssignment");
        membersSelector.ensureDebugId(addProject + "membersSelector");
        manager.ensureDebugId(addProject + "manager");
        backupManagerTable.ensureDebugId(addProject + "backupManagerTable");
        status.ensureDebugId(addProject + "status");
        fileUpload.ensureDebugId(addProject + "fileUpload");

        registrationEventBus();
        getProjectAllData();
        initMembers();
        addFields();
        setDefaultValues();
        if (projectFromID == null) {
            setDefaultValuesByFormProperty();
        }
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

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getDefaultValue() != null) {
            employeeAssignment.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getDefaultValue() != null) {
            manager.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getDefaultValue()));
        }
        noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getDefaultValue() != null) {
            noteWidget.getTextBox().setText(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getDefaultValue());
        }
    }

    private void getAllProjectManagers() {
        projectService.getManagers(new AbstractAsyncCallback<HashSet<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
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
                    if (formPropertyMap.get(STATUS).getDefaultValue() == null && object != null && object.length > 0) {
                        status.setSelected(object[0].getId());
                    }
                    setDefaultValues();
                });
            }
        });

    }

    private void generateProjectNumber(Date date, Integer clientId) {
        hrmsService.generateBrigadaCode(new AbstractAsyncCallback<NumberData>() {
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
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYEE_ADD, AddBrigadaView.this, (sender, args) -> initMembers());
    }

    private void addFields() {
        addTitleField(BASIC_INFORMATION, wfmStrings.basicDetails());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()));
            number.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()));
            name.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, status, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.STATUS).isRequired()));
            status.setEnabled(!formPropertyMap.get(CustomFormConstants.STATUS).isDisabled());
        } else {
            addField(CustomFormConstants.STATUS, status, getTitle(wfmStrings.status(), true));
        }
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, null);
        addTitleField(INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE) != null) {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, null, false);
        } else {
            addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer/*membersSelector*/, null, false);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).getTitle() : wfmStrings.backupManagers(), formPropertyMap.get(CustomFormConstants.PROJECT.BACKUP_MANAGER).isRequired()));
        } else {
            addField(CustomFormConstants.PROJECT.BACKUP_MANAGER, backupManagerTable, getTitle(wfmStrings.backupManagers()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER) != null) {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).getTitle() : wfmStrings.manager(), formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isRequired()));
            manager.setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.MANAGER).isDisabled());
        } else {
            addField(CustomFormConstants.PROJECT.MANAGER, manager, getTitle(wfmStrings.manager(), true));
        }

        addTitleField(NOTES_AND_ATTACHMENTS, wfmStrings.meetingNotesAttachments());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ATTACHMENTS) != null) {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, getTitle(formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isChanged() ? formPropertyMap.get(CustomFormConstants.ATTACHMENTS).getTitle() : wfmStrings.attachments(), formPropertyMap.get(CustomFormConstants.ATTACHMENTS).isRequired()));
        } else {
            addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());
        }

        noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null) {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getTitle() : wfmStrings.notes(), formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isRequired()));
            if (noteWidget.getTextBox() != null) {
                noteWidget.getTextBox().setEnabled(!formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isDisabled());
            }
        } else {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes(), true);
        }

        addField(CustomFormConstants.OWNER, employeeLookUp, wfmStrings.owners());
    }

    private void setManagers() {
        int selectedLeader = manager.getSelectedItem() != null ? manager.getSelectedItem().getId() : 0;
        manager.clear();
        manager.setSelectedNullLabel();
        manager.setEnabled(false);

        SelectItem[] selection = membersSelector.getSelectedItems();
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

    private void initMembers() {

        if (!(membersSelector.getItems() == null || membersSelector.getItems().isEmpty())) {
            return;
        }
        EmployeeService.App.get().getBrigadaEmployeesForAddEdit(null, true, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
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

    private boolean validate() {
        clearErrorStyle();
        int errors;

        errors = super.customValidate();

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

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, !Validation.validateListBoxRequired(employeeAssignment));
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

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        saveProject();
        closeTab();
    }

    public void saveProject() {

        ProjectSingleItem newProject = new ProjectSingleItem();
        newProject.setName(name.getText());
        newProject.setDescription(area.getText());
        newProject.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());
        newProject.setManagerId(manager.getSelectedItem().getId());


        if (numberData != null) {
            numberData = number.getNumberData(true);
            newProject.setNumberData(numberData);
        }
        newProject.setStatusId(status.getSelectedItem().getId());
        newProject.setAttachments(fileUpload.getAttachedFiles());
        newProject.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        newProject.setOwnersId(employeeLookUp.getSelectedItemsIdsAsString());
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

        LoadingPanel.loading(true);

        if (!firstClick.get()) {
            newProject.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }


        hrmsService.saveBrigada(newProject, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
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
                projectID = result;
                shellOk();
                WfmUiEventsBus.fireWfmUiEvent(ON_PROJECT_ADD, result, AddBrigadaView.this);
            }

        });
    }

    private void shellOk() {
        reinit();
    }

    public void reinit() {
        initForm();
        addFields();
        name.setText("");
        projectDescription.setText("");
        area.setText("");
        manager.clear();
        backupManagerTable.removeAllRows();
        initMembers();
        generateProjectNumber(new Date(), null);
        fileUpload.clearAndAdd();

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
        return LayoutRPC.BRIGADA_FORM;
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
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> save());
        addButton(save);
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
        pnlEmployeeAssignmentContainer.clear();
        pnlEmployeeAssignmentContainer.add(membersSelector);
        initMembers();
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
        return Constants.BRIGADA;
    }
}