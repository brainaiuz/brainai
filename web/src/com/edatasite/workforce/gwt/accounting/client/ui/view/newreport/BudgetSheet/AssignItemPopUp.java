package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiCellTree;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.SelectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.cell.IconCell;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class AssignItemPopUp extends KpiModal implements Constants {
    private final String type;
    private final Integer budegetManagerId;
    private KpiCellTree assignSelector;
    private HorizontalPanel pnlEmployeeAssignmentContainer;
    private WfmButton2 saveAndClose;


    public AssignItemPopUp(SelectItem budgetManager, LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> filterData) {
        this.type = budgetManager.getDescription();
        this.budegetManagerId = budgetManager.getId();
        setTitle(budgetManager.getName());
        setWidth("100%");
        open();
        init();
        if (filterData == null || filterData != null && filterData.isEmpty()) {
            getDataByType();
        } else {
            assignSelector.setItems(filterData);
        }
    }

    private void getDataByType() {
        LoadingPanel.loading(true);
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setEntityID(budegetManagerId);
        AllInOneService.App.get().getAssignesByType(filterParameter, type, new AbstractAsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> result) {
                super.success(result);
                assignSelector.setItems(result);
                LoadingPanel.loading(false);
            }
        });
    }


    private void init() {
        pnlEmployeeAssignmentContainer = new HorizontalPanel();
        pnlEmployeeAssignmentContainer.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);

        assignSelector = new KpiCellTree(true);
        assignSelector.setOpen(false);
        assignSelector.drawSelectedSide(new SelectionContainer() {
            @Override
            public void selectedDataGrid(final KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, final MultiSelectionModel<KpiTreeInfo> selectionModel) {
                selectedDataGrid.addRowCountChangeHandler(event -> {
//                    setManagers();
//                    if (!manager.isSomethingSelected()) {
//                        manager.setSelected(Utils.getUserID());
//                        setManagers();
//                    }
                });
                //Employee Name Blow
                Column<KpiTreeInfo, String> employee = new Column<KpiTreeInfo, String>(new TextCell()) {
                    @Override
                    public String getValue(final KpiTreeInfo object) {
                        return object.getName();
                    }
                };
                employee.setSortable(true);
                String nameColumn = Constants.PRODUCTS.equalsIgnoreCase(type) ? wfmStrings.product() : Constants.EMPLOYEES.equals(type) ? wfmStrings.employee() : Constants.CUSTOMER.equals(type) ? wfmStrings.customer() : Constants.OPPORTUNITY.equals(type) ? wfmStrings.opportunity() : wfmStrings.chartOfAccounts();
                sortHandler.setComparator(employee, Comparator.comparing(KpiTreeInfo::getName));
                selectedDataGrid.addColumn(employee, nameColumn);
                selectedDataGrid.setColumnWidth(employee, 90, Style.Unit.PCT);

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
        pnlEmployeeAssignmentContainer.add(assignSelector);
        add(new GRow(new GColumn(GColumnEnum.COL_12, new FormGroup(pnlEmployeeAssignmentContainer))));

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> {
            close();
        }));
        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(saveAndClose);
    }

    private void save() {
        LoadingPanel.loading(true);
        AccountingService.App.get().saveBudgetManagerAssignItems(budegetManagerId, assignSelector.getBudgetTreeItems(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                super.success(result);
                LoadingPanel.loading(false);
                close();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BUDGET_SHEET_UPDATE, null, AssignItemPopUp.this);
            }
        });
    }
}
