package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.QuoteInvoicedItemsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class ProgressInvoicingByAmountView extends FooteredView implements FittedContent {

    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Integer objectId;
    private NewInvoice quote;
    private KpiRadioButton remainingAmountButton;
    private KpiRadioButton amountButton;
    private TextBox amountTxtBox;
    private BigDecimal remainingAmount;
    private final Boolean isSalesOrder;


    public ProgressInvoicingByAmountView(Integer objectId, boolean isSalesOrder) {
        super(AccountingConstants.BY_AMOUNT, accountingStrings.byAmount());
        this.objectId = objectId;
        this.isSalesOrder = isSalesOrder;
    }

    @Override
    protected Widget onInitialize() {
        loadData();
        return null;
    }

    public void loadData() {
        QuoteService.App.get().getQuoteSummaryData(objectId, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewInvoice newInvoice) {
                quote = newInvoice;
                initialize();
            }
        });
    }

    private void initialize() {
        BigDecimal invoicedAmount = quote.getInvoicedAmount() != null ? quote.getInvoicedAmount() : BigDecimal.ZERO;

        remainingAmountButton = new KpiRadioButton("option", accountingStrings.createInvoiceForRemainingBalance());
        amountButton = new KpiRadioButton("option", accountingStrings.createInvoiceForAmount());
        amountButton.setValue(true);

        amountTxtBox = new TextBox();
        amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(amountTxtBox, Utils.getAccountingCalculationScale());

        remainingAmount = quote.getTotalInInvoiceCurrency().subtract(invoicedAmount);

        remainingAmountButton.addValueChangeHandler(booleanValueChangeEvent -> {
            amountTxtBox.setEnabled(false);
            amountTxtBox.setValue(AccountingUtils.get().formatPrice(remainingAmount.setScale(Utils.getAccountingCalculationScale(), RoundingMode.HALF_EVEN)));
        });
        amountButton.addValueChangeHandler(booleanValueChangeEvent -> amountTxtBox.setEnabled(true));

        TextBox remainingAmountBox = new TextBox();
        remainingAmountBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        remainingAmountBox.setEnabled(false);
        remainingAmountBox.setValue(AccountingUtils.get().formatPrice(remainingAmount));

        HTMLPanel mainPanel = new HTMLPanel("");
        mainPanel.setStyleName("add-form content-box content-box--white");

        FormGroup typeGroup = new FormGroup();
        typeGroup.setLabel(wfmStrings.type());
        typeGroup.addToContent(remainingAmountButton);
        typeGroup.addToContent(amountButton);

        GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_4, typeGroup));
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(accountingStrings.amountOfQuote(), amountTxtBox)));
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.remainingAmount(), remainingAmountBox)));

        mainPanel.add(firstRow);
        mainPanel.add(this::createFooter);
        add(mainPanel);
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ProgressInvoicingByAmountView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ProgressInvoicingByAmountView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div applyWrapper = new Div();

        WfmButton2 applyButton = new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY);
        applyButton.addClickHandler(clickEvent -> {
            if (!(remainingAmountButton.getValue() || amountButton.getValue())) {
                Info.show(accountingMessages.pleaseSelectConvertOption(), Info.Type.WARNING);
                return;
            }
            if (!Validation.validateTextBoxRequired(amountTxtBox)) {
                return;
            }

            BigDecimal entireAmount = AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(Utils.getAccountingCalculationScale(), RoundingMode.HALF_EVEN);
            if (entireAmount.compareTo(remainingAmount.setScale(Utils.getAccountingCalculationScale(), RoundingMode.HALF_EVEN)) > 0) {
                Info.show(accountingMessages.youCantEnterMoreThanRemainingAmount(), Info.Type.WARNING);
            } else {
                StringBuilder url = new StringBuilder();
                url.append("saleinvoice|add/add/progressInvoicing/");
                url.append("byAmount").append("/").append(objectId).append("/");
                url.append(AccountingUtils.get().format(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText())));
                if (isSalesOrder) {
                    url.append("/saleOrder");
                }
                redirectProperly(url.toString());
                closeTab();
            }
        });

        applyWrapper.add(applyButton);
        result.add(applyWrapper);
        return result;
    }

    public List<Widget> getFooterLeftSideWidgets() {
        if (quote.getInvoicedItems() != null && quote.getInvoicedItems().length > 0) {
            List<Widget> leftSideWidgets = new ArrayList<>();
            FooterInformer invInformer = new FooterInformer(SvgEnum.invoice, wfmStrings.invoices(), null);
            invInformer.setBadgeCount(quote.getInvoicedItems().length);
            invInformer.addClickHandler(event -> {
                new QuoteInvoicedItemsWidget(quote.getInvoicedItems(), Constants.RECEIVABLE.equals(quote.getType())).show();
            });
            leftSideWidgets.add(invInformer);
            return leftSideWidgets;
        }

        return null;
    }

    private void redirectProperly(String url) {
        if (Utils.isAccounting()) {
            goTo(url);
        } else {
            Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + url);
        }
    }

    @Override
    public String getIconStyle() {
        return null;
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
