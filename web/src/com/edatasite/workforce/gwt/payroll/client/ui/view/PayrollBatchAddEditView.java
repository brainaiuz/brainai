package com.edatasite.workforce.gwt.payroll.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeByPermissionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.ISelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollBatchData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.project.client.ui.PmClientsLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/21/15
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollBatchAddEditView extends CustomForm implements Colapse{

    protected static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    private Integer objectID;
    private TextBox name;
    private TextArea2 description;
    private KpiRadioButton byPosition;
    private KpiRadioButton byDepartment;
    private KpiRadioButton byLocation;
    private DataListBox currencyListBox;
    private boolean enabledMultiCurrency;
    private MultiTableNewUI managerTable;
    private PmClientsLookUp clientsLookUp;
    private ProjectLookUp projectLookUp;
    private SelectPanel employeesPanel;
    private int limit = 200;
    private int offset = 0;
    private int employeeCount = 0;
    private boolean isEmpty = false;
    private final HashSet<Integer> employees = new HashSet<>();
    private FormGroup currency;

    public PayrollBatchAddEditView() {
        super("addPayrollBatch", payrollStrings.payrollBatches());
    }

    public PayrollBatchAddEditView(Integer objectID) {
        super("editPayrollBatch", payrollStrings.payrollBatches());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        init();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMPLOYE_TREE_WIDGET_REFRESH, PayrollBatchAddEditView.this, (sender, args) -> getEmployeeList(true));
        return null;
    }

    private void init() {
        name = new TextBox();
        name.setName("name");
        description = new TextArea2(wfmStrings.description());
        description.setHeight("140px");
        byPosition = new KpiRadioButton("type", payrollStrings.byPosition());
        byDepartment = new KpiRadioButton("type", payrollStrings.byDepartment());
        byLocation = new KpiRadioButton("type", payrollStrings.byLocation());
        ValueChangeHandler<Boolean> changeHandler = booleanValueChangeEvent -> reloadAssignees(false);
        byDepartment.addValueChangeHandler(changeHandler);
        byPosition.addValueChangeHandler(changeHandler);
        byLocation.addValueChangeHandler(changeHandler);
        HorizontalPanel typePanel = new HorizontalPanel();
        typePanel.add(byDepartment);
        typePanel.add(byPosition);
        typePanel.add(byLocation);
        byDepartment.setValue(true);

        currencyListBox = new DataListBox();

        currencyListBox.ensureDebugId("currencyListBox");
        currencyListBox.setWithoutNullLabel(true);
        currencyListBox.addValueChangeHandler(changeEvent -> reloadAssignees(false));
        currency = new FormGroup(wfmStrings.currency(), currencyListBox);

        clientsLookUp = new PmClientsLookUp();
        clientsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            projectLookUp.clear();
            reloadAssignees(true);
        });
        clientsLookUp.addStyleName(DEFAULT_WIDTH);

        projectLookUp = new ProjectLookUp(Constants.RECEIVABLE, clientsLookUp);
        projectLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> reloadAssignees(true));
        projectLookUp.addStyleName(DEFAULT_WIDTH);

        managerTable = new MultiTableNewUI(10, new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getManagersMap(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });

        final TableColumn[] assignColumns = new TableColumn[2];
        assignColumns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee());
        assignColumns[1] = new TableColumn(wfmStrings.action(), wfmStrings.action());
        employeesPanel = new SelectPanel(assignColumns);
        employeesPanel.setDefaultSettings();
        employeesPanel.setSelectPanelAction(new ISelectPanel() {
            @Override
            public void removeItem(SelectItem item) {
                employees.clear();
                employees.add(item.getId());

                if (objectID != null) {
                    saveEmployee(employees, false);
                }
            }

            @Override
            public void addItem(Set<SelectItem> selectedItems, boolean isSelected) {
                employees.clear();
                employees.addAll(selectedItems.stream().map(SelectItem::getId).collect(Collectors.toSet()));
                saveEmployee(employees, isSelected);
            }
        });

        addTitleField(CustomFormConstants.DETAILS, wfmStrings.basicDetails());
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, description, null);
        addField(PAYROLL_STARTER.SETTINGS_TYPE, typePanel, wfmStrings.type());
        addTitleField(CustomFormConstants.ASSIGNEE, wfmStrings.assignedEmployees());
        addField(CustomFormConstants.ASSIGNEES, employeesPanel, getTitle(wfmStrings.assignedEmployees()));
        addField(CustomFormConstants.MANAGER, managerTable, getTitle(payrollStrings.payrollManager()));
        addField(PROJECT.CLIENT, clientsLookUp, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        addField(CustomFormConstants.PROJECT_, projectLookUp, getTitle(Property.get(Constants.PROJECT, wfmStrings.project())));
        show();

    }

    private void reloadAssignees(boolean clearTable) {
        if (clearTable) {
            employeesPanel.getTable().clear();
        }
        employeesPanel.getTreeSelect().clearTree();
        limit = 200;
        offset = 0;
        employeeCount = 0;
        isEmpty = false;
        getEmployeeList();
    }

    private int offsetSelected = 0;
    private int countSelected = 0;
    private boolean isEmptySelected = false;

    public static native void selectedEmpoyeeListScrollDownEvent(PayrollBatchAddEditView view) /*-{
        var timerID;
        $wnd.jQuery("div.blue-border").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollBatchAddEditView::getSelectedEmployees()();
                }, 200)
            }
        });
    }-*/;

    private void getSelectedEmployees() {
        countSelected = 0;
        if (!isEmptySelected && objectID != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(offsetSelected);
            int limitSelected = 200;
            fp.setLimit(limitSelected);
            fp.setPayrollBatchID(objectID);
            if (byDepartment.getValue()) {
                fp.setType(0);
            } else if (byPosition.getValue()) {
                fp.setType(1);
            } else {
                fp.setType(2);
            }
            fp.setResignedEmployeesIncluded(true);
            if (offsetSelected != 0) {
                LoadingPanel.loading(true);
            }
            if (enabledMultiCurrency) {
                fp.setCurrencyID(currencyListBox.getSelectedId(true));
            }
            PayrollService.App.get().getEmployeesMap(fp, LayoutRPC.PAYROLL_BATCH_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        countSelected = employeesPanel.addSelectedItems(items);
                        if (countSelected < 200) {
                            isEmptySelected = true;
                        }
                    } else {
                        isEmptySelected = true;
                    }
                }
            });
            offsetSelected += limitSelected;
        }
    }

    public static native void empoyeeListScrollDownEvent(PayrollBatchAddEditView view) /*-{
        var timerID;
        $wnd.jQuery(".treePanel-class").scroll(function () {
            clearTimeout(timerID);
            if ($wnd.jQuery(this).scrollTop() + $wnd.jQuery(this).innerHeight() + 100 >= $wnd.jQuery(this)[0].scrollHeight) {
                timerID = setTimeout(function () {
                    view.@com.edatasite.workforce.gwt.payroll.client.ui.view.PayrollBatchAddEditView::getEmployeeList()();
                }, 200)
            }
        });
    }-*/;


    public void getEmployeeList() {
        getEmployeeList(true);
    }

    public void getEmployeeList(boolean search) {
        employeeCount = 0;
        int limitAll = 10000;
        if (!isEmpty && limit < limitAll) {
            ListingFilterParameter fp = new ListingFilterParameter();
            if (search) {
                limit = limitAll;
            }
            fp.setStart(offset);
            fp.setLimit(limit);
            fp.setRelationToID(objectID);
            if (byDepartment.getValue()) {
                fp.setType(0);
            } else if (byPosition.getValue()) {
                fp.setType(1);
            } else {
                fp.setType(2);
            }
            fp.setProjectId(projectLookUp.getSelectedItemID());
            if (enabledMultiCurrency) {
                fp.setCurrencyID(currencyListBox.getSelectedId(true));
            }
//            fp.setShowActive(true);
            LoadingPanel.loading(true);
            PayrollService.App.get().getEmployeesMap(fp, LayoutRPC.PAYROLL_BATCH_FORM, new AbstractAsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
                    LoadingPanel.loading(false);
                    TreeSelect.setTickAllVisible(items.size() != 0);
                    if (items.size() > 0) {
                        employeeCount = employeesPanel.addItems(items);
                        employeesPanel.expandTreeView();
                        isEmpty = employeeCount < 200;
                    } else {
                        isEmpty = true;
                    }
                }
            });
            offset += limit;
        }
    }

    private void saveEmployee(final HashSet<Integer> employeeID, boolean isChecked) {
        if (objectID != null) {
            LoadingPanel.loading(true);
            PayrollService.App.get().saveGroupEmployees(employeeID, objectID, isChecked, new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void o) {
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    @Override
    protected void addButtons() {
        addButton(objectID != null ? wfmStrings.update() : wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());

    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            PayrollService.App.get().getPayrollBatchData(objectID, new AsyncCallback<PayrollBatchData>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(PayrollBatchData payrollBatchData) {
                    fillFormWithData(payrollBatchData);
                    reloadAssignees(false);
                    getSelectedEmployees();
                }
            });
        } else {
            PayrollService.App.get().getCompanyPayrollSettings(Constants.MULTI_CURRENCY_FOR_PAYROLL, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(String s) {
                    enabledMultiCurrency = "true".equals(s);
                    //if (enabledMultiCurrency) {
                        addField(CustomFormConstants.CURRENCY, currency);
                        CurrencyService.App.get().getCurrencies(true, new AsyncCallback<CurrencyItem[]>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(CurrencyItem[] currencyItems) {
                                currencyListBox.setItems(currencyItems);
                                reloadAssignees(false);
                            }
                        });
                    //} else {
                        //reloadAssignees(false);
                    //}
                }
            });
        }
        selectedEmpoyeeListScrollDownEvent(this);
        empoyeeListScrollDownEvent(this);
    }

    private void fillFormWithData(PayrollBatchData payrollBatchData) {

        enabledMultiCurrency = payrollBatchData.isEnabledMultiCurrency();

        if (payrollBatchData.getName() != null) {
            name.setText(payrollBatchData.getName());
        }
        if (payrollBatchData.getDescription() != null) {
            description.setText(payrollBatchData.getDescription());
        }
        if (payrollBatchData.getType() != null) {
            if (payrollBatchData.getType() == 0) {
                byDepartment.setValue(true);
            } else if (payrollBatchData.getType() == 1) {
                byPosition.setValue(true);
            } else {
                byLocation.setValue(true);
            }
        }
        if (payrollBatchData.getManagers() != null && payrollBatchData.getManagers().length > 0) {
            managerTable.removeAllRows();
            for (SelectItem manager : payrollBatchData.getManagers()) {
                managerTable.addWidgets(getManagersMap(manager));
            }
        }
        //if (payrollBatchData.isEnabledMultiCurrency()) {
            addField(CustomFormConstants.CURRENCY, currency);
            currencyListBox.setItems(payrollBatchData.getCurrencies());
            currencyListBox.setSelected(payrollBatchData.getCurrency());
        //}
        if (payrollBatchData.getClient() != null) {
            clientsLookUp.setSelected(payrollBatchData.getClient());
        }
        if (payrollBatchData.getProject() != null) {
            projectLookUp.setSelected(payrollBatchData.getProject());
        }
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        PayrollBatchData data = new PayrollBatchData();
        data.setObjectID(objectID);
        data.setName(name.getText());
        data.setDescription(description.getText());
        if (byDepartment.getValue()) {
            data.setType(0);
        } else if (byPosition.getValue()) {
            data.setType(1);
        } else {
            data.setType(2);
        }
        data.setCurrency((CurrencyItem) currencyListBox.getSelectedItem(true));
        if (objectID == null) {
            if (employeesPanel.getTreeSelect() != null && employeesPanel.getTreeSelect().getCheckedItems() != null && employeesPanel.getTreeSelect().getCheckedItems().length > 0) {
                WfmTreeItem[] checkedItems = employeesPanel.getTreeSelect().getCheckedItems();
                employees.clear();
                for (WfmTreeItem wfmTreeItem : checkedItems) {
                    Integer employeeID = wfmTreeItem.getId();
                    employees.add(employeeID);
                }
                data.setSelectedEmployeeIds(employees);
            }
        }
        data.setClient(clientsLookUp.getSelectedItem());
        data.setProject(projectLookUp.getSelectedItem());

        ArrayList<SelectItem> managers = new ArrayList<>();
        for (HashMap<String, Widget> row : managerTable.getWidgets()) {
            if (row != null) {
                EmployeeByPermissionLookUp managerLookUp = (EmployeeByPermissionLookUp) row.get(MultiTable.LOOK_UP_BOX);
                if (managerLookUp != null && managerLookUp.getSelectedItemID() != null) {
                    managers.add(managerLookUp.getSelectedItem());
                }
            }
        }
        data.setManagers(managers.toArray(new SelectItem[]{}));

        LoadingPanel.loading(true);
        PayrollService.App.get().savePayrollBatch(data, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYROLL_BATCH_ADD, result, PayrollBatchAddEditView.this);
                closeTab();
            }
        });

    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        markAsError(CustomFormConstants.ASSIGNEES, employeesPanel, false);
        errors += markAsError(CustomFormConstants.NAME, name, !Validation.validateTextBoxRequired(name));

        if (employeesPanel.getTreeSelect() != null && employeesPanel.getTreeSelect().getCheckedItems().length < 1) {
            errors += markAsError(CustomFormConstants.ASSIGNEES, employeesPanel, true);
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private WidgetsMap getManagersMap(SelectItem manager) {
        WidgetsMap widgetsMap = new WidgetsMap();
        EmployeeByPermissionLookUp managerLookUp = new EmployeeByPermissionLookUp();
        managerLookUp.addStyleName(DEFAULT_WIDTH);
        managerLookUp.setPermissionCode(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);
        widgetsMap.addWidgets(managerLookUp);

        if (manager != null && manager.getId() != null) {
            managerLookUp.addItem(manager);
        }
        widgetsMap.addToCenter(MultiTable.LOOK_UP_BOX, managerLookUp);
        return widgetsMap;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_BATCH_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
