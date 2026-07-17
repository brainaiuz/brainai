package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 10/31/15 6:09 PM
 */
public class NewTransactionItemView extends FooteredView implements Colapse, AccountingCustomFormConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private HashMap<String, Widget> widgetsMap;
    private WfmButton2 deleteButton;
    private HTMLPanel htmlPanel;
    private final Integer transactionId;
    private Transaction transactionData;

    public NewTransactionItemView(Integer transactionId) {
        super("summary", accountingStrings.transactionItem());
        this.transactionId = transactionId;
    }

    @Override
    protected Widget onInitialize() {
        initForm();
        loadFormData();
        return null;
    }

    private void initForm() {
        widgetsMap = new HashMap<>();
        deleteButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_WHITE_OUTLINE);
        initHander();
    }

    private void loadFormData() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getTransaction(transactionId, new AsyncCallback<Transaction>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Transaction transaction) {
                transactionData = transaction;
                initWidgetsMap();
                htmlPanel = new WftHTMLPanel(transaction.getLayoutHtml(), widgetsMap).getContainer();
                htmlPanel.addStyleName("add-form invoice-form");
                htmlPanel.add(createFooter());
                add(htmlPanel);
                LoadingPanel.loading(false);
            }
        });
    }

    private void initWidgetsMap() {
        widgetsMap.put(INPUT_DATE, new FormGroup(accountingStrings.transactionDate(), getWidgetAsFormControl(DateUtils.format(transactionData.getJournalDate()))));
        widgetsMap.put(INPUT_REFERENCE, new FormGroup(wfmStrings.reference(), getWidgetAsFormControl(transactionData.getReference())));
        widgetsMap.put(LABEL_TITLE, new FormGroup(wfmStrings.title(), getWidgetAsFormControl(transactionData.getJournalName())));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.paidAmount(), getWidgetAsFormControl(AccountingUtils.get().formatPrice(transactionData.getTotalCredit()))));
    }

    private void initHander() {
        deleteButton.addClickHandler(event -> {
            setEnabledButtons(false);
            AccountingService.App.get().deleteSupplierPayment(transactionId, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(Boolean result) {
                    setEnabledButtons(true);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.transaction()), Info.Type.INFO);
                    LoadingPanel.loading(false);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_OPENNING_BALANCE_TRANSACTION_DELETE, "TRANSACTION_ITEM", NewTransactionItemView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, NewTransactionItemView.this);
                    closeTab();
                }
            });
        });

    }

    private void setEnabledButtons(boolean b) {
        if (deleteButton != null) {
            deleteButton.setEnabled(b);
        }
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return NewTransactionItemView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        if (!(Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(transactionData.getJournalDate().getNonConvertedDate()))) {
            List<Widget> result = new ArrayList<>();

            Div deleteWrapper = new Div();
            deleteWrapper.add(deleteButton);

            result.add(deleteWrapper);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
