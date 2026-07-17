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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ProgressInvoicingByPercentageView extends FooteredView implements FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private final Integer objectId;
    private NewInvoice quote;
    private RadioButton remainingPercentButton;
    private RadioButton percentage;
    private TextBox percentageTxtBox;
    private BigDecimal remainingPercent;
    private final boolean isSalesOrder;


    public ProgressInvoicingByPercentageView(Integer objectId, boolean isSalesOrder) {
        super(AccountingConstants.BY_PERCENTAGE, accountingStrings.invoicingbyManual());
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
        BigDecimal convertedPercent = quote.getConvertedPercent() != null ? quote.getConvertedPercent() : BigDecimal.ZERO;
        BigDecimal convertedAmount = quote.getConvertedAmount() != null ? quote.getConvertedAmount() : BigDecimal.ZERO;

        remainingPercentButton = new KpiRadioButton("option", accountingStrings.createInvoiceForRemainingBalance());
        percentage = new KpiRadioButton("option", accountingStrings.createInvoiceForPercentage());
        percentage.setValue(true);

        percentageTxtBox = new TextBox();
        percentageTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(percentageTxtBox, 2);

        remainingPercent = AccountingConstants.HUNDRED.subtract(convertedPercent);

        remainingPercentButton.addValueChangeHandler(booleanValueChangeEvent -> {
            percentageTxtBox.setEnabled(false);
            percentageTxtBox.setText(AccountingUtils.get().format(remainingPercent));
        });
        percentage.addValueChangeHandler(booleanValueChangeEvent -> percentageTxtBox.setEnabled(true));

        TextBox remainingPercentBox = new TextBox();
        remainingPercentBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        remainingPercentBox.setEnabled(false);
        remainingPercentBox.setValue(AccountingUtils.get().formatPrice(remainingPercent.setScale(2, RoundingMode.HALF_UP)));

        HTMLPanel mainPanel = new HTMLPanel("");
        mainPanel.setStyleName("add-form content-box content-box--white");

        FormGroup typeGroup = new FormGroup();
        typeGroup.setLabel(wfmStrings.type());
        typeGroup.addToContent(remainingPercentButton);
        typeGroup.addToContent(percentage);

        GRow firstRow = new GRow(new GColumn(GColumnEnum.COL_4, typeGroup));
        firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(accountingStrings.percentOfQuote(), percentageTxtBox)));
        if (remainingPercent != null) {
            firstRow.add(new GColumn(GColumnEnum.COL_3, new FormGroup(wfmStrings.remaining() + " " + wfmStrings.percentage(), remainingPercentBox)));
        }

        mainPanel.add(firstRow);
        mainPanel.add(this::createFooter);
        add(mainPanel);
    }

    public ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return ProgressInvoicingByPercentageView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return ProgressInvoicingByPercentageView.this.getFooterRightSideWidgets();
            }
        });
    }

    public List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div applyWrapper = new Div();

        WfmButton2 applyButton = new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY);
        applyButton.addClickHandler(clickEvent -> {
            if (!(remainingPercentButton.getValue() || percentage.getValue())) {
                Info.show(accountingMessages.pleaseSelectConvertOption(), Info.Type.WARNING);
                return;
            }
            if (!Validation.validateTextBoxRequired(percentageTxtBox)) {
                return;
            }

            BigDecimal entirePercent = AccountingUtils.get().parseToBigDecimal(percentageTxtBox.getText()).setScale(2, RoundingMode.HALF_UP);
            if (entirePercent.compareTo(remainingPercent.setScale(2, RoundingMode.HALF_UP)) > 0) {
                Info.show(accountingMessages.youCantEnterMoreThanRemainingPercent(), Info.Type.WARNING);
            } else {
                StringBuilder url = new StringBuilder();
                url.append("saleinvoice|add/add/progressInvoicing/");
                url.append("byPercent").append("/").append(objectId).append("/");
                url.append(AccountingUtils.get().format(entirePercent));
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
