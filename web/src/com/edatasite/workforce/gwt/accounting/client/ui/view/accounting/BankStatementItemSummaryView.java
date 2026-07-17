package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementItemListItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilshod Madrahimov
 * Date: 28.11.2015
 */
public class BankStatementItemSummaryView extends BankStatementItemAddEditView {

    private HTML date, description, spend, receive, balance;

    public BankStatementItemSummaryView(Integer objectID, Integer bankStatementID) {
        super("summary", "Bank Statement Summary");
        this.objectID = objectID;
        this.bankStatementID = bankStatementID;
    }

    /*protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return this;
    }*/

    public void initialize() {
        date = initHTML();
        description = initHTML();
        spend = initHTML();
        receive = initHTML();
        balance = initHTML();

        addTitleField(INFORMATION, wfmStrings.information());
        addField(DATE_FIELD, date, getTitle(accountingStrings.transactionDate()));
        addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
        addField("SPEND", spend, getTitle(accountingStrings.spent()));
        addField("RECEIVE", receive, getTitle("Received"));
        addField("BALANCE", balance, getTitle(wfmStrings.balance()));

        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.edit(), event -> SinksContainerFactory.entryPoint.onHistoryChanged("bankStatementItem|edit/" + objectID + "/" + bankStatementID));
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        accountingService.getBankAccountStatementItem(objectID, bankStatementID, new AsyncCallback<BankStatementItemListItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log(throwable.getMessage());
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BankStatementItemListItem item) {
                LoadingPanel.loading(false);
                if (item != null) {
                    fillFormWithData(item);
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BANK_STATEMENT_ITEM_FORM;
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

    protected void fillFormWithData(BankStatementItemListItem item) {
        date.setHTML(DateUtils.format(item.getDate()));
        description.setHTML(item.getDescription());
        spend.setHTML(item.getSpent() + "");
        receive.setHTML(item.getReceived() + "");
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
