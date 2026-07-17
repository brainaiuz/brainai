package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransferMoneyData;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 14.05.12
 * Time: 17:45
 * To change this template use File | Settings | File Templates.
 */
public class TransferMoneySummaryView extends CustomForm2 implements Colapse {

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private Integer fromAccountID;


    private HTML transferMoneyDate, fromAccount, toAccount, reference, amount;


    private WfmButton2 pdfVersionButton;
    private final Integer objectID;


    public TransferMoneySummaryView(Integer objectId) {
        super("summary", accountingStrings.transferMoneySummary());
        this.objectID = objectId;
    }



    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        transferMoneyDate = initHTML();
        fromAccount = initHTML();
        toAccount = initHTML();
        reference = initHTML();
        amount = initHTML();


        addTitleField(INFORMATION, accountingStrings.transferMoney());
        addField(TRANSFER_MONEY_DATE, transferMoneyDate, wfmStrings.date());
        addField(FROM_ACCOUNT_LOOKUP, fromAccount, wfmStrings.from());
        addField(TO_ACCOUNT_LOOKUP, toAccount, wfmStrings.to());
        addField(REFERENCE, reference, wfmStrings.reference());
        addField(TRANSFER_MONEY_AMOUNT, amount, wfmStrings.amount());
        show();
    }

    @Override
    protected void getDataToFillFields() {
        AccountingService.App.get().getBankAccountSummaryData(objectID, null, new AsyncCallback<TransferMoneyData>() {

            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(TransferMoneyData result) {
                transferMoneyDate.setHTML(DateUtils.format(result.getTransferMoneyDate()));
                fromAccount.setHTML(result.getFromAccount().getName());
                toAccount.setHTML(result.getToAccount().getName());
                reference.setHTML(result.getReference() != null ? result.getReference() : "N/A");
                amount.setHTML(AccountingUtils.get().format(result.getAmount()));
            }
        });

    }

    @Override
    protected void addButtons() {
        pdfVersionButton = new WfmButton2(wfmStrings.pdfVersion());
        pdfVersionButton.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);

        pdfVersionButton.addClickHandler(event -> {
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setObjectId(objectID);
            HashMap<String, String> parametrs = filter.getRequestParams();
            String pdfURL = CommandConstants.PDF_URL + "/transferMoneyViewPDFHandler";
            Utils.sendPDFOrExcelRequest(pdfVersionButton, pdfURL, parametrs, "_blank");
        });
        addButton(pdfVersionButton);
    }


    @Override
    protected String getFormID() {
        return LayoutRPC.TRANSFER_MONEY_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected void initPredefinedValues() {

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
}
