package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.form.AbstractGdnGrnSummaryView;

public class GoodsDeliveredNotesSummaryView extends AbstractGdnGrnSummaryView implements Colapse {
    protected final Integer id;

    public GoodsDeliveredNotesSummaryView(Integer id) {
        super("summary", accountingStrings.gdnNumber(), false);
        this.id = id;
    }

    @Override
    protected void initializeData() {
        LoadingPanel.loading(true);
        QuoteService.App.get().getShippingData(id, true, new AbstractAsyncCallback<ShippingData>() {
            @Override
            public void failure(Throwable throwable) {
                failure(throwable);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ShippingData result) {
                setData(result);
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected String pdfUrl() {
        return CommandConstants.PDF_URL + "/gdnOrderViewPDFHandler";
    }

    @Override
    protected String excelUrl() {
        return CommandConstants.COMMON_URL + "/gdnOrderViewExcelHandler";
    }

    @Override
    protected String convertToInvoiceLink() {
        return "saleinvoice|add/add/convertFromGrn/";
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
                if (MessageCommand.hasConvertedItems.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGdnHasConvertedInvoices(), Info.Type.WARNING);
                } else if (MessageCommand.hasOutTransactions.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGdnHasOutTransactions(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.goodsDeliveredNotes()), Info.Type.INFO);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.PICKLIST_RELOAD_PAGE, null, null);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, null);
                }
            }
        });

    }
}
