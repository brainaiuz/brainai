package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GoodsReceivedNotesListView extends AbstractShippingDataListView {
    private Integer warehouseID;

    public GoodsReceivedNotesListView() {
        super("goodsreceivednotes", true);
        setDescription(property.getPlural(accountingStrings.goodsReceivedNotes()));
    }

    public GoodsReceivedNotesListView(Integer warehouseID) {
        super("goodsreceivednotes", true);
        setDescription(property.getPlural(accountingStrings.goodsReceivedNotes()));
        this.warehouseID=warehouseID;
    }

    public GoodsReceivedNotesListView(ListingFilterParameter filterParameter) {
        super("goodsreceivednotes", true);
        setDescription(property.getPlural(accountingStrings.goodsReceivedNotes()));
        this.filterParameter = filterParameter;
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.GoodsReceivedNoteListPanel;
    }

    @Override
    protected String getShippingDataSummaryLink() {
        return "grn|summary/";
    }

    @Override
    protected String getPDFTemplateType() {
        return AccountingConstants.GOODS_RECEIVED_NOTES;
    }

    @Override
    protected String getOrderSummaryLink() {
        return Constants.PURCHASE_ORDER;
    }

    @Override
    protected String getPDFDownloadURL() {
        return "/grnOrderViewPDFHandler";
    }

    @Override
    protected String getCrmAccountLink() {
        return "suppliersummary";
    }

    @Override
    protected String getCrmAccountColumnTitle() {
        return wfmStrings.supplier();
    }

    @Override
    public String getPropertyCode() {
        return "goodsreceivednotes";
    }

    @Override
    protected String getInvoiceSummaryLink() {
        return "purchaseinvoice|summary/";
    }

    @Override
    protected FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(5, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[0], Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[1], accountingStrings.invoiceStatus(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME;
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[2], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_STATUS_NAME;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_STATUS_NAME;
            }

        });

        contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[3], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME;
            }
        });
        return contentConfigure;
    }

    @Override
    protected Integer getWarehouseId() {
        if (filterParameter != null && filterParameter.getWarehouseID() != null) {
            return filterParameter.getWarehouseID();
        }
        return warehouseID;
    }

    @Override
    protected void deleteGdnGrn(Integer id) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().validateStockInconsistencyInDeleteProcess(StockTransactionType.GRN, id, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem result) {
                if (result == null) {
                    deleteGRN(id);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    private void deleteGRN(Integer objectID) {
        QuoteService.App.get().deleteGoodsReceivedNotes(objectID, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.someErrorsOccured(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (MessageCommand.hasConvertedItems.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGrnHasConvertedInvoices(), Info.Type.WARNING);
                } else if (MessageCommand.hasOutTransactions.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGrnHasOutTransactions(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.goodsDeliveredNotes()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, null);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.PURCHASE_ORDER_SUMMARY_RELOAD_PAGE, null, null);
                }
            }
        });
    }
}
