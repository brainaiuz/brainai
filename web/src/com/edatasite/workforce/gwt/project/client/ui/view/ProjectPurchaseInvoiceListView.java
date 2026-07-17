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
 * User: xushnud
 * Date: 12-May-2010
 * Time: 10:53:10
 * To change this template use File | Settings | File Templates.
 */
public class ProjectPurchaseInvoiceListView extends BaseListView {
    private Integer projectId;
    ListingPanel<ProjectInvoice> list;
    ProjectServiceAsync projectService = ProjectService.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    public ProjectPurchaseInvoiceListView(Integer projectId) {
        super("PurchaseInvoice");
        setDescription(property.getPlural(wfmStrings.purchaseInvoices()));
        this.projectId = projectId;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PurchaseInvoicePanel /*ProjectPurchaseInvoiceListPanel*/, getColumns(), getProvider(), getDesigner());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        columns[0] = new ColumnDefinitionConfig<ProjectInvoice, SimpleLink>(" " + wfmStrings.invoiceNumber(), ProjectInvoice.INVOICENUMBER, 150) {
            @Override
            public SimpleLink getCellValue(ProjectInvoice item) {
                return getLinkToPurchaseInvoiceView(String.valueOf(item.getInvoiceNumber()), "Accounting.html#" + "purchaseinvoice|summary/" + item.getID());
            }
        };
        columns[1] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.invoiceDate(), ProjectInvoice.INVOICEDATE, 75) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return DateUtils.format(item.getInvoiceDate());
            }
        };
        columns[2] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.dueDate(), ProjectInvoice.DUEDATE, 75) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return DateUtils.format(item.getDueDate());
            }
        };
        columns[3] = new ColumnDefinitionConfig<ProjectInvoice, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), ProjectInvoice.CLIENT, 150) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return item.getClientName();
            }
        };
        columns[4] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.currency(), ProjectInvoice.CURRENCY, 75) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return item.getCurrencyName();
            }
        };
        columns[5] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.dueAmount(), ProjectInvoice.DUEAMOUNT, 75) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return Utils.getCalculationNumberFormat().format(item.getTotalInInvoiceCurrency());

            }
        };
        columns[5].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[6] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.paidAmount(), ProjectInvoice.PAIDAMUOUNT, 100) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return Utils.getCalculationNumberFormat().format(item.getPayments());
            }
        };
        columns[6].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns[7] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.status(), ProjectInvoice.STATUS, 75) {

            @Override
            public String getCellValue(ProjectInvoice item) {
                return item.getStatus();
            }
        };

        columns[8] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.createdBy(), ProjectInvoice.CREATOR, 100) {
            @Override
            public String getCellValue(ProjectInvoice item) {
                return item.getCreatorName() != null ? item.getCreatorName() : "";
            }
        };

        columns[9] = new ColumnDefinitionConfig<ProjectInvoice, String>(wfmStrings.taxTotal(), ProjectInvoice.TAX_TOTAL, 100) {
            @Override
            public String getCellValue(ProjectInvoice invoice) {
                BigDecimal taxTotal = invoice.getTotalTaxes() != null ? invoice.getTotalTaxes().multiply(invoice.getExchageRate() != null ? invoice.getExchageRate() : BigDecimal.ONE) : BigDecimal.ZERO;
                return Utils.getCalculationNumberFormat().format(taxTotal);
            }
        };
        columns[9].setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);

        return columns;
    }

    private ListingRequestProvider<ProjectInvoice> getProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            initPurchaseInvoiceList(filterParametrs, callback, null);
        };

    }

    private void initPurchaseInvoiceList(ListingFilterParameter filterParametrs, ListingCallback<ProjectInvoice> callback, Span container) {
        projectService.getPurchaseInvoiceList(projectId, filterParametrs, new AbstractAsyncCallback<ListResult<ProjectInvoice>>() {
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            public void success(ListResult<ProjectInvoice> result) {
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.thereAreNoRelatedSomethingItemsYet(wfmStrings.purchaseInvoices()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    private SimpleLink getLinkToPurchaseInvoiceView(String name, final String action) {
        SimpleLink link = new SimpleLink(name);
        link.addClickHandler(event -> Utils.openURL(GWT.getHostPageBaseURL() + action));
        return link;
    }

    public String getIconStyle() {
        return "bgMark project-purchase-order-list";
    }

    public String getDescription() {
        return super.getDescription();
    }


    /*public AbstractImagePrototype getIconImage() {
        return ProjectViewImageBundles.App.get().purchaseInvoices();
    }*/

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
        initPurchaseInvoiceList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.PURCHASE_INVOICE;
    }
}
