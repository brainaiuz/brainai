package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.expenses.client.ui.ExpenseConstants;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectExpenseReportsListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;


/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 12-May-2010
 * Time: 14:57:06
 * To change this template use File | Settings | File Templates.
 */
public class ProjectExpenseClaimsListView extends BaseListView {
    private Integer projectId;

    public ProjectExpenseClaimsListView(Integer projectId) {
        super("expenseList");
        setDescription(property.getPlural(wfmStrings.expenseClaims()));
        this.projectId = projectId;
    }

    protected Widget onInitialize() {
        add(new ListingPanel<>(ListPanelType.ProjectExpenseClaimsListPanel, getColumns(), getProvider(), getDesigner()));
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];

        columns[0] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, SimpleLink>(wfmStrings.number(), ProjectExpenseReportsListItem.NUMBER, 140) {

            @Override
            public SimpleLink getCellValue(ProjectExpenseReportsListItem item) {
                return getLinkToExpenseClaimView(item.getNumber(), "Accounting.html#" + "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW);

            }
        };
        columns[1] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, SimpleLink>(wfmStrings.title(), ProjectExpenseReportsListItem.TITLE, 140) {

            @Override
            public SimpleLink getCellValue(ProjectExpenseReportsListItem item) {
                return getLinkToExpenseClaimView(item.getTitle(), "Accounting.html#" + "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW);

            }
        };

        columns[2] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, String>(wfmStrings.date(), ProjectExpenseReportsListItem.reportPeriod, 140) {

            @Override
            public String getCellValue(ProjectExpenseReportsListItem item) {
                return DateUtils.format(item.getStartDate());
            }
        };
        columns[3] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedSupplier(), wfmStrings.project()), ProjectExpenseReportsListItem.relatedProject, 100) {

            @Override
            public String getCellValue(ProjectExpenseReportsListItem item) {
                return item.getProjectName();
            }
        };
        columns[4] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, String>(wfmStrings.reporter(), ProjectExpenseReportsListItem.reporter, 100) {

            @Override
            public String getCellValue(ProjectExpenseReportsListItem item) {
                return item.getReporterName() != null ? item.getReporterName() : "";
            }
        };
        columns[5] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, String>(wfmStrings.approver(), ProjectExpenseReportsListItem.approver, 100) {

            @Override
            public String getCellValue(ProjectExpenseReportsListItem item) {
                return item.getApproverSelectItem().getName();
            }
        };
        columns[6] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, String>(wfmStrings.status(), ProjectExpenseReportsListItem.status, 100) {

            @Override
            public String getCellValue(ProjectExpenseReportsListItem item) {
                return item.getStatusName();
            }
        };
        columns[7] = new ColumnDefinitionConfig<ProjectExpenseReportsListItem, Double>(wfmStrings.amount(), ProjectExpenseReportsListItem.amount, 100) {

            @Override
            public Double getCellValue(ProjectExpenseReportsListItem item) {
                return item.getTotal();
            }
        };
        columns[7].setColumnSortable(false);
        columns[7].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        return columns;
    }

    private ListingRequestProvider<ProjectExpenseReportsListItem> getProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            initExpenseReportList(filterParametrs, callback, null);
        };
    }

    private void initExpenseReportList(ListingFilterParameter filterParametrs, ListingCallback<ProjectExpenseReportsListItem> callback, Span container) {
        ProjectService.App.get().getExpenseReportList(projectId, filterParametrs, new AbstractAsyncCallback<ListResult<ProjectExpenseReportsListItem>>() {
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void success(ListResult<ProjectExpenseReportsListItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private SimpleLink getLinkToExpenseClaimView(String name, final String action) {
        SimpleLink link = new SimpleLink(name);
        link.addClickHandler(event -> Utils.redirect(GWT.getHostPageBaseURL() + action));
        return link;
    }

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                MenuBar menuBar = new MenuBar(true);

                if (Utils.isEnableBonnardCustomization()) {
                    MenuPopItem expense = new MenuPopItem(wfmStrings.expense());
                    MenuPopItem timesheetExpense = new MenuPopItem(wfmStrings.projectBaseExpense());

                    expense.setCommand(() -> Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add/" + ExpenseConstants.DISBURSEMENT));
                    timesheetExpense.setCommand(() -> Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#projectBaseExpense|add/add"));
                    menuBar.addItem(expense);
                    menuBar.addItem(timesheetExpense);
                } else {
                    if (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD) || Utils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_ADD)) {
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_COMPANY_EXPENSE_ADD)) {
                            MenuPopItem companyExpenseItem = new MenuPopItem(wfmStrings.companyExpense());
                            companyExpenseItem.setCommand(() -> Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add/" + ExpenseConstants.COMPANY_EXPENSE));
                            menuBar.addItem(companyExpenseItem);
                        }
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD)) {
                            MenuPopItem expenseItem = new MenuPopItem(Property.get(Constants.EXPENSES_CLAIM, wfmStrings.expenseClaim()));
                            expenseItem.setCommand(() -> Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add"));
                            menuBar.addItem(expenseItem);
                        }
                    }
                }
                ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                addNew.setMenu(menuBar);
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.thereAreNoRelatedSomethingItemsYet(wfmStrings.expenseClaims()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    public String getIconStyle() {
        return "bgMark project-expence-claims-list";
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        projectId = parentId;
        initExpenseReportList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.EXPENSES_CLAIM;
    }
}
