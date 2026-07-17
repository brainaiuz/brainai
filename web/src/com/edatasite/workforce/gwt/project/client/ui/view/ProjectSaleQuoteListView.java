package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectInvoice;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/1/12
 * Time: 2:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectSaleQuoteListView extends BaseListView implements Constants {

    private Integer projectID;
    ListingPanel<ProjectInvoice> list;
    ProjectServiceAsync projectService = ProjectService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private final boolean isSalesOrder;

    public ProjectSaleQuoteListView(Integer projectID, boolean isSalesOrder) {
        super(isSalesOrder ? "SalesOrders" : "SalesQuotes");
        setDescription(isSalesOrder ? Property.getPluralWithObjectCode(Constants.SALE_ORDER_CODE, wfmStrings.salesOrders()) : Property.getPluralWithObjectCode(Constants.SALE_QUOTE, wfmStrings.salesQuotes()));
        this.projectID = projectID;
        this.isSalesOrder = isSalesOrder;
    }


    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        list = new ListingPanel<>(isSalesOrder ? ListPanelType.SaleOrderListPanel : ListPanelType.SaleQuoteListPanel/*ProjectSaleQuoteListView*/, getColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[13];
        columns[0] = new ColumnDefinitionConfig<ProjectInvoice, SimpleLink>(" " + wfmStrings.number(), ProjectInvoice.INVOICENUMBER, 150) {

            @Override
            public SimpleLink getCellValue(ProjectInvoice rowValue) {
                return getLinkToSalesQuoteView(String.valueOf(rowValue.getInvoiceNumber()), "Accounting.html#" + (isSalesOrder ? SALE_ORDER_CODE : SALE_QUOTE) + "|summary/" + rowValue.getID());
            }
        };
        columns[1] = new ColumnDefinitionConfig<ProjectInvoice, String>(" " + wfmStrings.date(), ProjectInvoice.INVOICEDATE, 75) {

            @Override
            public String getCellValue(ProjectInvoice rowValue) {
                return DateUtils.format(rowValue.getInvoiceDate());
            }
        };
        columns[2] = new ColumnDefinitionConfig<ProjectInvoice, String>(" " + wfmStrings.dueDate(), ProjectInvoice.DUEDATE, 75) {

            @Override
            public String getCellValue(ProjectInvoice rowValue) {
                return DateUtils.format(rowValue.getDueDate());
            }
        };
        columns[3] = new ColumnDefinitionConfig<ProjectInvoice, String>(" " + Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), ProjectInvoice.CLIENT, 150) {

            @Override
            public String getCellValue(ProjectInvoice rowValue) {
                return rowValue.getClientName();
            }
        };
        columns[4] = new ColumnDefinitionConfig<ProjectInvoice, String>(" " + wfmStrings.amount(), ProjectInvoice.PAIDAMUOUNT, 75) {

            @Override
            public String getCellValue(ProjectInvoice rowValue) {
                return Utils.getCalculationNumberFormat().format(rowValue.getTotal());
            }
        };
        columns[4].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[5] = new ColumnDefinitionConfig<ProjectInvoice, String>(" " + wfmStrings.status(), ProjectInvoice.STATUS, 75) {

            @Override
            public String getCellValue(ProjectInvoice rowValue) {
                return rowValue.getStatus();
            }
        };

        columns[6] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.currency(), ProjectInvoice.CURRENCY, 60) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };

        columns[7] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.createdBy(), ProjectInvoice.CREATOR, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getCreatorName();
            }
        };

        columns[8] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.manager(), ProjectInvoice.MANAGER, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getManagerName();
            }
        };

        columns[9] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.reference(), ProjectInvoice.REFERENCE, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getReference() != null ? invoice.getReference() : "";
            }
        };
        columns[10] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.poNumber(), ProjectInvoice.PO_NUMBER, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getPoNumber() != null ? invoice.getPoNumber() : "";
            }
        };
        columns[11] = new ColumnDefinitionConfig<ProjectInvoice, String>(Property.get(Constants.Opportunities, wfmStrings.opportunity()), ProjectInvoice.OPPORTUNITY_NUMBER, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getOpportunity() != null ? invoice.getOpportunity() : "";
            }
        };

        columns[12] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.taxTotal(), ProjectInvoice.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                BigDecimal taxTotal = invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().multiply(invoice.getExchageRate() != null ? invoice.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO;
                return Utils.getCalculationNumberFormat().format(taxTotal);
            }
        };
        columns[12].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        return columns;
    }

    private ListingRequestProvider<ProjectInvoice> getProvider() {
        return (filterParametrs, listingCallback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setSaleOrder(isSalesOrder);
            initSaleQuoteList(filterParametrs, listingCallback, null);
        };
    }

    private void initSaleQuoteList(ListingFilterParameter fp, ListingCallback<ProjectInvoice> callback, Span container) {
        ProjectService.App.get().getSaleQuoteList(projectID, fp, new AbstractAsyncCallback<ListResult<ProjectInvoice>>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<ProjectInvoice> result) {
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

    private ListingPanelDesign getDesigner() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.thereAreNoRelatedSomethingItemsYet(isSalesOrder ? Property.getPluralWithObjectCode(Constants.SALE_ORDER_CODE, wfmStrings.salesOrders()) : Property.getPluralWithObjectCode(Constants.SALE_QUOTE, wfmStrings.salesQuotes())));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }


    public String getIconStyle() {
        return "bgMark project-purchase-order-list";
    }

    private SimpleLink getLinkToSalesQuoteView(String name, final String action) {
        SimpleLink link = new SimpleLink(name);
        link.addClickHandler(event -> Utils.openURL(GWT.getHostPageBaseURL() + action));
        return link;
    }

    @Override
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
        projectID = parentId;
        initSaleQuoteList(fp, null, container);
    }
}
