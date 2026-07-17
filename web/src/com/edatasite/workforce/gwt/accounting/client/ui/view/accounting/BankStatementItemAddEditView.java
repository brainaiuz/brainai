package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementItemListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilshod Madrahimov
 * Date: 28.11.2015
 */
public class BankStatementItemAddEditView extends CustomForm implements Constants {
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingServiceAsync accountingService =  AccountingService.App.get();
    private DatePicker date;
    private TextArea2 description;
    private TextBox spend;
    private TextBox receive;
    private TextBox balance;

    protected Integer bankStatementID;
    protected Integer objectID;

    public BankStatementItemAddEditView(Integer objectID,Integer bankStatementID) {
        super("edit", "Bank Statement Edit");
        this.objectID = objectID;
        this.bankStatementID = bankStatementID;
    }

    public BankStatementItemAddEditView(String name, String description) {
        super(name, description);
    }

    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return this;
    }

    public void initialize() {
        date = new DatePicker(true);
        description = new TextArea2(1000, wfmStrings.description());
        spend = new TextBox();
        receive = new TextBox();
        balance = new TextBox();

        date.addStyleName(DEFAULT_WIDTH);
        description.addStyleName(DEFAULT_WIDTH);
        spend.addStyleName(DEFAULT_WIDTH);
        receive.addStyleName(DEFAULT_WIDTH);
        balance.addStyleName(DEFAULT_WIDTH);

        addTitleField(INFORMATION, wfmStrings.information());
        addField(DATE_FIELD, date, getTitle(accountingStrings.transactionDate()));
        addField(DESCRIPTION, description, null);
        addField("SPEND", spend, getTitle(accountingStrings.spent()));
        addField("RECEIVE", receive, getTitle("Received"));
        addField("BALANCE", balance, getTitle(wfmStrings.balance()));

        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());
        addButton(wfmStrings.close(), event -> closeTab());

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        accountingService.getBankAccountStatementItem(objectID,bankStatementID, new AsyncCallback<BankStatementItemListItem>() {
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

    private void save(){
        LoadingPanel.loading(true);
        accountingService.saveBankAccountStatementItem(getBankStatementItemData(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.bankStatements()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_STATEMENT_ITEM_CHANGE, result, BankStatementItemAddEditView.this);
                closeTab();
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BANK_STATEMENT_ITEM_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
        date.setDate(item.getDate());
        description.setText(item.getDescription());

        spend.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(spend, AccountingUtils.getPriceScale());
        spend.setText(AccountingUtils.get().formatPrice(item.getSpent()));

        receive.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(receive, AccountingUtils.getPriceScale());
        receive.setText(AccountingUtils.get().formatPrice(item.getReceived()));

        balance.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(balance, AccountingUtils.getPriceScale());
        if (item.getBalance() != null) {
            balance.setText(AccountingUtils.get().formatPrice(item.getBalance()));
        }
    }

    private BankStatementItemListItem getBankStatementItemData(){
        BankStatementItemListItem item = new BankStatementItemListItem();
        item.setObjectID(objectID);
        item.setBankStatementID(bankStatementID);
        item.setDate(date.getDate());
        item.setDescription(description.getText());
        item.setSpent(AccountingUtils.get().parseToBigDecimal(spend.getText()));
        item.setReceived(AccountingUtils.get().parseToBigDecimal(receive.getText()));
        item.setBalance(AccountingUtils.get().parseToBigDecimal(balance.getText()));
        return item;
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
