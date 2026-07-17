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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectInvoice;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 02.09.2009
 * Time: 12:32:21
 * To change this template use File | Settings | File Templates.
 */
public class ProjectInvoicesListView extends BaseListView implements Constants {

    
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();


    private Integer projectID;

    public ProjectInvoicesListView(Integer projectID) {
        super("ProjectInvoice");
        setDescription(property.getPlural(wfmStrings.saleInvoices()));
        this.projectID = projectID;
    }

    public String getIconStyle() {
        return "bgMark project-invoice-list";
    }

    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        ListingPanel<ProjectInvoice> listingPanel = new ListingPanel<>(ListPanelType.SaleInvoiceListPanel, getColumns(), getProvider(), getDesigner());
        add(listingPanel);

        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[12];
        columns[0] = new ColumnDefinitionConfig<ProjectInvoice, SimpleLink>(" " + wfmStrings.number(), ProjectInvoice.INVOICENUMBER, 150) {

            @Override
            public SimpleLink getCellValue(ProjectInvoice rowValue) {
                return getLinkToInvoiceView(String.valueOf(rowValue.getInvoiceNumber()), "Accounting.html#" + SALE_INVOICE + "|summary/" + rowValue.getID());
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
        columns[6] = new ColumnDefinitionConfig<ProjectInvoice, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), ProjectInvoice.CLIENT, 150) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getClientName();
            }
        };

        columns[7] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.currency(), ProjectInvoice.CURRENCY, 60) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getCurrencyName() != null ? invoice.getCurrencyName() : "";
            }
        };

        columns[8] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.originalAmount(), ProjectInvoice.ORIGINAL_AMOUNT, 80) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return Utils.getCalculationNumberFormat().format(invoice.getTotalInInvoiceCurrency());
            }
        };
        columns[8].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columns[9] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.paidAmount(), ProjectInvoice.FULL_AMOUNT, 80) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getFullPayment() != null ? invoice.getFullPayment() + "" : "";
            }
        };
        columns[9].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        columns[10] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.poNumber(), ProjectInvoice.PO_NUMBER, 80) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getPoNumber();
            }
        };

        columns[11] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.createdBy(), ProjectInvoice.CREATOR, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                return invoice.getCreatorName() != null ? invoice.getCreatorName() : "";
            }
        };
        return columns;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.thereAreNoRelatedSomethingItemsYet(wfmStrings.salesInvoice()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    private ListingRequestProvider<ProjectInvoice> getProvider() {
        return (filterParametrs, listingCallback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            initInvoiceList(filterParametrs, listingCallback, null);
        };
    }

    private void initInvoiceList(ListingFilterParameter filterParametrs, ListingCallback<ProjectInvoice> callback, Span container) {
        ProjectService.App.get().getInvoiceList(projectID, filterParametrs, new AbstractAsyncCallback<ListResult<ProjectInvoice>>() {
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

    private SimpleLink getLinkToInvoiceView(String name, final String action) {
        SimpleLink link = new SimpleLink(name);
        link.addClickHandler(event -> Utils.openURL(GWT.getHostPageBaseURL() + action));
        return link;
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
        projectID = parentId;
        initInvoiceList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.SALE_INVOICE;
    }
}