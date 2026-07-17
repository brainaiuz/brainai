package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;

public class GoodsDeliveredNotesListView extends AbstractShippingDataListView {

    private Integer warehouseID;

    public GoodsDeliveredNotesListView() {
        super("goodsdeliverednotes", false);
        setDescription(property.getPlural(accountingStrings.goodsDeliveredNotes()));
    }
    public GoodsDeliveredNotesListView(Integer warehouseID) {
        super("goodsdeliverednotes", false);
        setDescription(property.getPlural(accountingStrings.goodsDeliveredNotes()));
        this.warehouseID=warehouseID;
    }

    public GoodsDeliveredNotesListView(ListingFilterParameter filterParameter) {
        super("goodsdeliverednotes", false);
        setDescription(property.getPlural(accountingStrings.goodsDeliveredNotes()));
        this.filterParameter = filterParameter;
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.GoodsDeliveredNoteListPanel;
    }

    @Override
    protected String getShippingDataSummaryLink() {
        return "gdn|summary/";
    }

    @Override
    protected String getPDFTemplateType() {
        return AccountingConstants.GOODS_DELIVERED_NOTES;
    }

    @Override
    protected String getOrderSummaryLink() {
        return Constants.SALE_ORDER_CODE;
    }

    @Override
    protected String getPDFDownloadURL() {
        return "/gdnOrderViewPDFHandler";
    }

    @Override
    protected String getCrmAccountLink() {
        return "client";
    }

    @Override
    protected String getCrmAccountColumnTitle() {
        return wfmStrings.customer();
    }

    @Override
    public String getPropertyCode() {
        return "goodsdeliverednotes";
    }

    @Override
    protected String getInvoiceSummaryLink() {
        return "saleinvoice|summary/";
    }

    @Override
    protected FacetContentConfigure getFacetContentConfigure() {
        boolean isEnableSQPicklist = Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST);
        FacetContentConfigure contentConfigure = new FacetContentConfigure(isEnableSQPicklist ? 5 : 4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[0], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
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
        if (isEnableSQPicklist) {
            contentConfigure.addContentConfigure(FacetContentType.GDNFacetFilter.getContentCode()[4], wfmStrings.type(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrSaleInvoiceRepresenter.FIELD_GDN_IS_SALES_ORDER;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrSaleInvoiceRepresenter.FIELD_GDN_IS_SALES_ORDER;
                }

                @Override
                public boolean isConditionItemId() {
                    return false;
                }
            });
        }
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
        QuoteService.App.get().deleteGoodsDeliveredNotes(id, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.someErrorsOccured(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (MessageCommand.hasConvertedItems.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGdnHasConvertedInvoices(), Info.Type.WARNING);
                } else if (MessageCommand.hasOutTransactions.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGdnHasOutTransactions(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.goodsDeliveredNotes()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.PICKLIST_RELOAD_PAGE, null, null);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, null);
                }
            }
        });

    }
}
