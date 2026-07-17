package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetColumn;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.List;

public class ColumnManagementBudgetManager extends KpiModal {
    private final FlowPanel panel;
    private final Integer budgetID;
    private TextBox name;
    private DataListBox type;
    private DataListBox moduleType;
    private WfmButton2 saveButton;
    private BudgetManagerColumnFilter columnFilter;
    private final String columnCode;
    private BudgetColumn data = new BudgetColumn();
    private List<SelectItem> customForms;


    public ColumnManagementBudgetManager(SelectItem budgetManager) {
        this(null, budgetManager);
    }

    public ColumnManagementBudgetManager(String columnCode, SelectItem budgetManager) {
        super();
        panel = new FlowPanel();
        this.columnCode = columnCode;
        this.budgetID = budgetManager != null ? budgetManager.getId() : null;
        setTitle(budgetManager != null ? budgetManager.getName() : "");
        CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                super.onSuccess(result);
                customForms = result;
            }
        });
        if (columnCode != null) {
            AccountingService.App.get().getBudgetManagerColumnData(budgetID, columnCode, new AbstractAsyncCallback<BudgetColumn>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                    initialize();
                }

                @Override
                public void success(BudgetColumn result) {
                    super.success(result);
                    data = result;
                    initialize();
                }
            });
        } else {
            initialize();
        }
    }

    private void initialize() {
        setStyleName(getElement(), "quick-add", true);
        name = new TextBox();
        if (data.getName() != null) {
            name.setText(data.getName());
        }
        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.name(), name)));
        panel.add(row);

        type = new DataListBox();
        type.setItems(getColumnType());
        if (data.getType() != null) {
            type.setSelectedByDescription(data.getType());
            type.setEnabled(false);
        }

        GRow rowType = new GRow();
        rowType.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.type(), type)));
        panel.add(rowType);

        GRow row2 = new GRow();
        moduleType = new DataListBox();
        moduleType.setWithoutNullLabel(true);
        moduleType.setItems(getConditionModuleTypes());
        if (data.getReportType() != null) {
            moduleType.setSelectedByDescription(data.getReportType());
            moduleType.setEnabled(false);
        } else {
            moduleType.setSelected(getConditionModuleTypes()[0]);
        }
        FormGroup moduleFormGroup = new FormGroup(wfmStrings.section(), moduleType);
        moduleFormGroup.setVisible(type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription()));
        row2.add(new GColumn(GColumnEnum.COL_8, moduleFormGroup));

        columnFilter = new BudgetManagerColumnFilter(data.getReportType() != null ? data.getReportType() : moduleType.getSelectedItem(true).getDescription(), data);
        moduleType.addValueChangeHandler(event -> {
            columnFilter = new BudgetManagerColumnFilter(moduleType.getSelectedItem().getDescription(), null);
        });

        ActionButton filterBtn = new ActionButton("", "btn btn--icon btn--white");
        filterBtn.setVisible(type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription()));
        filterBtn.ensureDebugId("filter_button");
        Icon filterIcon = new Icon();
        filterIcon.addStyleName("ficon--filter");
        filterBtn.add(filterIcon);
        filterBtn.addClickHandler(click -> {
            if (Utils.getCompanyID().equals("79898")) {
                Integer budgetID = this.budgetID != null ? this.budgetID : 0;
                Window.open(GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + 344 + "/savedreport/" + Utils.encrypt(name.getText()) + "/" + budgetID, "_blank", "");
            } else {
                columnFilter.open();
            }
        });
        row2.add(new GColumn(GColumnEnum.COL_4, new FormGroup(filterBtn)));
        if (type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription())) {
            panel.add(row2);
        }
        type.addValueChangeHandler(value -> {
            if (type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription())) {
                filterBtn.setVisible(true);
                moduleFormGroup.setVisible(true);
                panel.remove(row2);
                panel.add(row2);
            } else {
                panel.remove(row2);
            }
        });

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(click -> save());

        WfmButton2 closeBtn = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_WHITE_OUTLINE);
        closeBtn.addClickHandler(click -> {
            close();
        });
        add(panel);
        addButton(closeBtn);
        addButton(saveButton);
        open();
    }


    private SelectItem[] getColumnType() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(1, wfmStrings.editable(), Constants.EDITABLE));
        types.add(new SelectItem(2, wfmStrings.selectFromSystem(), Constants.FROM_SYSTEM));
        return types.toArray(new SelectItem[]{});
    }

    private SelectItem[] getConditionModuleTypes() {

        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(1, wfmStrings.purchaseorder(), Constants.PURCHASE_ORDER));
        types.add(new SelectItem(2, wfmStrings.product(), Constants.PRODUCTS));
        types.add(new SelectItem(3, wfmStrings.employee(), Constants.EMPLOYEES));
        types.add(new SelectItem(4, wfmStrings.customer(), Constants.CUSTOMER));
        types.add(new SelectItem(5, wfmStrings.chartOfAccounts(), Constants.CHART_OF_ACCOUNT));
        types.add(new SelectItem(6, wfmStrings.opportunity(), Constants.OPPORTUNITY));
        if (customForms != null && !customForms.isEmpty()) {
            types.addAll(customForms);
        }
        return types.toArray(new SelectItem[]{});
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(type)) {
            errors++;
        }
        if (type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription())) {
            if (!Validation.validateDataListBoxRequired(moduleType)) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        saveButton.setEnabled(false);
        if (!validate()) {
            saveButton.setEnabled(true);
            return;
        }
        LoadingPanel.loading(true, panel);
        BudgetColumn budgetColumn = new BudgetColumn();
        String code = name.getText().toUpperCase() + "_" + budgetID;
        budgetColumn.setCode(columnCode != null ? columnCode : code);
        budgetColumn.setName(name.getText());
        budgetColumn.setType(type.getSelectedItem().getDescription());
        if (type.getSelectedItem() != null && Constants.FROM_SYSTEM.equals(type.getSelectedItem().getDescription())) {
            budgetColumn.setReportType(moduleType.getSelectedItem().getDescription());
            columnFilter.getValues(budgetColumn);
        }

        AccountingService.App.get().saveBudgetManagerColumn(budgetID, budgetColumn, columnCode != null, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(Integer result) {
                super.success(result);
                LoadingPanel.loading(false, panel);
                if (result == -1) {
                    saveButton.setEnabled(true);
                    Info.show(wfmStrings.dataAlreadyExist(), Info.Type.WARNING);
                } else {
                    close();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BUDGET_SHEET_UPDATE, null, ColumnManagementBudgetManager.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BUDGET_SHEET_RELOAD_COLUMNS, budgetColumn, ColumnManagementBudgetManager.this);
                }
            }
        });
    }
}
