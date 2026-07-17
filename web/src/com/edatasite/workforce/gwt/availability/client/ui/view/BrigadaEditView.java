package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.NoteWidget;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class BrigadaEditView extends CustomForm2 implements Constants, HasLinksInterface, Colapse {

    private static final ProjectServiceAsync projectService = ProjectService.App.get();
    private static final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Numbering number;
    private TextBox name;
    private Widget richText;
    private TextArea2 area;
    private DataListBox parent;
    private DataListBox manager;
    private Set<SelectItem> systemManagers = new HashSet<>();
    private DataListBox status;
    private FlexTable statusTable;
    private final Integer projectID;
    private EditProject project;
    private GeneralFileUpload fileUpload;
    private DataListBox employeeAssignment;
    private FlowPanel pnlEmployeeAssignmentContainer;
    private KpiCellTree membersSelector;
    private SelectItem[] backupManagerItems;
    private MultiTableNewUI backupManagerTable;
    private final String editProject = "edit_project_";
    private HasLinks linkingUtil;
    private FormHasCustomField customFieldUtil;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private MultiSelectEmployeeLookUp employeeLookUp;


    public BrigadaEditView(Integer projectID) {
        super("edit");
        this.projectID = projectID;
    }


    @Override
    public String getIconStyle() {
        return "bgMark project-edit";
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(BrigadaEditView.this) {
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
        hrmsService.getBrigadaForEdit(projectID, null, null, new AbstractAsyncCallback<EditProject>() {
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

                if (project.getEmployeeAssignment() != null) {
                    employeeAssignment.setSelected(project.getEmployeeAssignment().getId());

                    pnlEmployeeAssignmentContainer.clear();
                    pnlEmployeeAssignmentContainer.add(membersSelector);
                    initMembers();
                } else {
                    employeeAssignment.setSelected(EmployeeAssignmentEnum.BY_EMPLOYEE.getId());
                    pnlEmployeeAssignmentContainer.add(membersSelector);
                    initMembers();
                }
                employeeLookUp.setSelectedItems(project.getOwners());

                getCustomFieldUtil().fillCustomFieldsWithData(project.getCustomFieldItems());

                defferedLoading();
                if (object.getBackupManagerIDs() != null && object.getBackupManagerIDs().size() > 0) {
                    backupManagerTable.removeAllRows();
                    for (Integer backupManagerID : object.getBackupManagerIDs()) {
                        backupManagerTable.addWidgets(getBackupManagersMap(backupManagerID));
                    }
                }
                setManagers();
            }
        });

    }

    private void initialize() {
        super.onInitialize();
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

        statusTable = new FlexTable();
        status = new DataListBox();
        status.setIdAttribute("project-status");
        statusTable.setWidget(0, 0, status);
        status.addStyleName(DEFAULT_WIDTH);
        status.setAllowFirstItem(true);
        fileUpload = new GeneralFileUpload(F_PROJECT, projectID, projectID);
        fileUpload.ensureDebugId(editProject + "fileUpload");

        employeeLookUp = new MultiSelectEmployeeLookUp();
        employeeLookUp.getFilterParametrs().setType(LookUpConstants.BRIGADA_ID);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);

        number.ensureDebugId(editProject + "number");
        parent.ensureDebugId(editProject + "parent");
        name.ensureDebugId(editProject + "name");
        richText.ensureDebugId(editProject + "richText");
        statusTable.ensureDebugId(editProject + "statusTable");
        addFields();
        show();
    }

    public void defferedLoading() {
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

    }

    private void addFields() {
        addTitleField(CustomFormConstants.DETAILS, property.getSingular("Brigada Details"));
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, number, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number()));
        } else {
            addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, name, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name()));
        } else {
            addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        }


        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.involvedEmployees());


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null) {
            addField(CustomFormConstants.STATUS, statusTable, getTitle(formPropertyMap.get(CustomFormConstants.STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.STATUS).getTitle() : wfmStrings.status()));
        } else {
            addField(CustomFormConstants.STATUS, statusTable, getTitle(wfmStrings.status()));
        }

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(BrigadaEditView.this, projectID, false);
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());

        //Notes
        NoteWidget noteWidget = new NoteWidget(projectID, RelationItem.TYPE_PROJECT);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE) != null) {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, getTitle(formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).isChanged() ? formPropertyMap.get(CustomFormConstants.PROJECT.PROJECT_NOTE).getTitle() : wfmStrings.notes()));
        } else {
            addField(CustomFormConstants.PROJECT.PROJECT_NOTE, noteWidget, wfmStrings.notes());
        }

        addField(CustomFormConstants.OWNER, employeeLookUp, wfmStrings.owners());

    }

    private boolean validate() {
        int errors;
        errors = super.customValidate();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.NAME, name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(area, !Validation.validateTextAreaRequired(area));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, status, !Validation.validateListBoxRequired(status));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT) != null && formPropertyMap.get(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT).isRequired()) {
            errors += markAsError(CustomFormConstants.PROJECT.EMPLOYEE_ASSIGNMENT, employeeAssignment, !Validation.validateListBoxRequired(employeeAssignment));
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
        project.setAttachments(fileUpload.getAttachedFiles());

        if (firstClick.get()) {
            project.setRelations(project.getRelations());
        } else {
            project.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }
        project.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());

        if (manager.getSelectedItem() != null) {
            project.setManagerId(manager.getSelectedId());
            project.setManagerName(manager.getSelectedItemText());
        }

        project.setEmployeeAssignment(EmployeeAssignmentEnum.buildWithId(employeeAssignment.getSelectedId()));

        project.setProjectMemberFromTreeInfo(membersSelector.getSelectedData());

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
        project.setOwnersId(employeeLookUp.getSelectedItemsIdsAsString());

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
                    Info.show(wfmStrings.youAlreadyHaveAIdWithThisNumber(), Info.Type.WARNING); //need to fix the propertie
                    enableButton(true);
                } else {
                    LoadingPanel.loading(true);
                    hrmsService.updateBrigada(project, new AbstractAsyncCallback<Void>() {
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
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_EDIT, projectID, BrigadaEditView.this);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                            closeTab("brigada|summary/" + projectID);
                        }
                    });
                }
            }
        });
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BrigadaList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
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
                BrigadaEditView.super.onInitialize();
            }
        });
        getFirstRequest();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ASSIGN_EMPLOYEE_TO_PROJECT, BrigadaEditView.this, (sender, args) -> setManagers());
        return null;
    }

    private void getFirstRequest() {
        clear();
        initEditForm();
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
        employeeAssignment.setEnabled(false);
        pnlEmployeeAssignmentContainer = new FlowPanel();

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
                final TextInputCell clientRateCell = new TextInputCell("debug_input-client--chargeRate form-control-sm");
                Column<KpiTreeInfo, String> clientRate = new Column<KpiTreeInfo, String>(clientRateCell) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getUnit() != null ? object.getUnit() : "";
                    }
                };
                clientRate.setFieldUpdater((index, object, value) -> object.setUnit(value));
                selectedDataGrid.addColumn(clientRate, wfmStrings.note());
                selectedDataGrid.setColumnWidth(clientRate, 17, com.google.gwt.dom.client.Style.Unit.PCT);

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
                selectedDataGrid.addColumn(action, "");
                selectedDataGrid.setColumnWidth(action, 10, com.google.gwt.dom.client.Style.Unit.PCT);
            }

            @Override
            public void additionalActions(HTMLPanel actionsPanel) {
            }
        });


        addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, null);
        pnlEmployeeAssignmentContainer.add(membersSelector);


        manager = new DataListBox();
        manager.addStyleName(DEFAULT_WIDTH);
        manager.addValueChangeHandler(event -> {
            project.setManagerId(manager.getSelectedId());
        });
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


        getDataToFillFields();
    }

    private void initMembers() {
        EmployeeService.App.get().getBrigadaEmployeesForAddEdit(projectID, true, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
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

    private void setManagers() {
        int selectedLeader = manager.getSelectedItem() != null ? manager.getSelectedItem().getId() : 0;
        manager.clear();
        manager.setSelectedNullLabel();
        manager.setEnabled(false);

        SelectItem[] selection = membersSelector.getSelectedItems();

        Set<SelectItem> managerList = new HashSet<>();
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
            backupManagerItems[i] = item;
            i++;
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
        manager.setSelectedItem(new SelectItem(project.getManagerId(), project.getManagerName()));
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
        return LayoutRPC.BRIGADA_FORM;
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
        WfmButton2 saveButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(editProject + "saveButton");
        saveButton.addClickHandler(sender -> updateProject());
        addButton(saveButton);
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
