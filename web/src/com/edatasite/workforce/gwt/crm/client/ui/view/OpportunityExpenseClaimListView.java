package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityExpenseClaimListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Dilshod Madrahimov
 * Date: 6/16/14
 * Time: 2:10 PM
 */
public class OpportunityExpenseClaimListView extends BaseListView {
    private Integer opportunityId;
    ListingPanel<OpportunityExpenseClaimListItem> list;

    public OpportunityExpenseClaimListView(Integer opportunityId) {
        super("expenseList");
        setDescription(property.getPlural(wfmStrings.expenseClaims()));
        this.opportunityId = opportunityId;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.OpportunityExpenseClaimListPanel, getColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        columns[0] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, SimpleLink>(wfmStrings.title(), OpportunityExpenseClaimListItem.TITLE, 140) {

            @Override
            public SimpleLink getCellValue(OpportunityExpenseClaimListItem item) {
                return getLinkToExpenseClaimView(item.getTitle(), "Accounting.html#" + "expenseReports|previewReport/" + item.getId() + "/" + Constants.EXPENSE_VIEW);

            }
        };
        columns[1] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, String>(wfmStrings.number(), OpportunityExpenseClaimListItem.expenseNumber, 100) {

            @Override
            public String getCellValue(OpportunityExpenseClaimListItem item) {
                return item.getNumber();
            }
        };
        columns[2] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, String>(wfmStrings.date(), OpportunityExpenseClaimListItem.reportPeriod, 100) {

            @Override
            public String getCellValue(OpportunityExpenseClaimListItem item) {
                return DateUtils.format(item.getStartDate());
            }
        };

        columns[3] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, String>(wfmStrings.reporter(), OpportunityExpenseClaimListItem.reporter, 100) {

            @Override
            public String getCellValue(OpportunityExpenseClaimListItem item) {
                return item.getReporterName() != null ? item.getReporterName() : "";
            }
        };
        columns[4] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, String>(wfmStrings.approver(), OpportunityExpenseClaimListItem.approver, 100) {

            @Override
            public String getCellValue(OpportunityExpenseClaimListItem item) {
                return item.getApproverSelectItem().getName();
            }
        };
        columns[5] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, String>(wfmStrings.status(), OpportunityExpenseClaimListItem.status, 100) {

            @Override
            public String getCellValue(OpportunityExpenseClaimListItem item) {
                return item.getStatusName();
            }
        };
        columns[6] = new ColumnDefinitionConfig<OpportunityExpenseClaimListItem, Double>(wfmStrings.amount(), OpportunityExpenseClaimListItem.amount, 100) {

            @Override
            public Double getCellValue(OpportunityExpenseClaimListItem item) {
                return item.getTotal();
            }
        };
        columns[6].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        return columns;
    }

    private ListingRequestProvider<OpportunityExpenseClaimListItem> getProvider() {
        return (filterParameter, callback) -> initExpenseList(filterParameter, callback, null);
    }

    private void initExpenseList(ListingFilterParameter filterParameter, ListingCallback<OpportunityExpenseClaimListItem> callback, Span container) {
        CRMService.App.get().getOpportunityExpenseClaimList(opportunityId, filterParameter, new AbstractAsyncCallback<ListResult<OpportunityExpenseClaimListItem>>() {
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void success(ListResult<OpportunityExpenseClaimListItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal().intValue() > 0) {
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
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.thereAreNoRelatedExpenseClaimsYet());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    public String getIconStyle() {
        return "opportunity-expense-list";
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
        initExpenseList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.EXPENSES_CLAIM;
    }
}
