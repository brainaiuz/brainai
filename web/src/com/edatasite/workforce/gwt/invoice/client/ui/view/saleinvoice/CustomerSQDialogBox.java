package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.salequote.progressinvoice.ProgressInvoicingView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import gwt.material.design.client.ui.MaterialPanel;

import java.math.BigDecimal;
import java.util.HashMap;

public class CustomerSQDialogBox {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();
    private KpiModal dialogBox;
    private NewInvoice[] items;
    private Command listener;
    private FlexTable table;

    public interface CustomerSQInterface {
        void setExternalQuoteID(Integer quoteID);

        void setQuotePercent(BigDecimal percent);

        void setQuoteAmount(BigDecimal amount);

        void setProgressInvoiceDialogBpxType(String type);

        void setProgressInvoicingByItem(boolean value);

        void setProgressInvoiciningMap(HashMap<Integer, BigDecimal> valuesMap);

        void applyProgressInvoicingData(NewInvoice result);

        View getView();

        void goTo(String url);

        boolean isMultiQuoteConvert();
    }

    private final CustomerSQInterface viewInterface;

    public CustomerSQDialogBox(CustomerSQInterface viewInterface) {
        this.viewInterface = viewInterface;
    }

    public void alertMessage(final NewInvoice[] items, final Command listener) {
        this.items = items;
        this.listener = listener;
        dialogBox = new KpiModal();
        dialogBox.setWidth(700);
        dialogBox.setTitle(wfmStrings.information());

        if (viewInterface.isMultiQuoteConvert()) {
            dialogBox.add(new HTML("<p>" + accountingStrings.invoiceMultiConvertMessage() + "</p>"));
        } else {
            dialogBox.add(new HTML("<p>" + accountingStrings.invoiceConvertMessage() + "</p>"));
        }

        WfmButton2 okButton = new WfmButton2(wfmStrings.ok());
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        table = new FlexTable();
        table.setStyleName("flexTable");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, new HTML(""));
        table.setWidget(0, 1, new HTML("<span  style='width:50%'>" + wfmStrings.number() + "</span>"));
        table.setWidget(0, 2, new HTML("<span  style='width:15%'>" + wfmStrings.type() + "</span>"));
        table.setWidget(0, 3, new HTML("<span  style='width:15%'>" + wfmStrings.date() + "</span>"));
        table.setWidget(0, 4, new HTML("<span  style='width:15%'>" + wfmStrings.poNumber() + "</span>"));

        if (viewInterface.isMultiQuoteConvert()) {
            table.setWidget(0, 5, new HTML("<span  style='width:15%'>" + Property.get(Constants.PROJECT, wfmStrings.project()) + "</span>"));
            table.setWidget(0, 6, new HTML("<span  style='width:20%'>" + wfmStrings.total() + "</span>"));
        } else {
            table.setWidget(0, 5, new HTML("<span  style='width:20%'>" + wfmStrings.total() + "</span>"));
        }

        table.getFlexCellFormatter().setStyleName(0, 0, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 1, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 2, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 3, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 4, "flexTable-Label");
        table.getFlexCellFormatter().setStyleName(0, 5, "flexTable-Label text-right");

        if (viewInterface.isMultiQuoteConvert()) {
            table.getFlexCellFormatter().setStyleName(0, 5, "flexTable-Label");
            table.getFlexCellFormatter().setStyleName(0, 6, "flexTable-Label text-right");
        }
        int i = 1;
        for (NewInvoice item : items) {
            if (viewInterface.isMultiQuoteConvert()) {
                table.setWidget(i, 0, new KpiCheckBox());
            } else {
                table.setWidget(i, 0, new KpiRadioButton("items"));
            }
            table.setWidget(i, 1, new HTML(item.getQuoteNumber()));
            table.setWidget(i, 2, new HTML(item.getStatus()));
            table.setWidget(i, 3, new HTML(DateUtils.format(item.getInvoiceDate())));
            table.setWidget(i, 4, new HTML(item.getPoNumber()));
            if (viewInterface.isMultiQuoteConvert()) {
                table.setWidget(i, 5, new KpiRadioButton("projects"));
                table.setWidget(i, 6, new HTML(AccountingUtils.get().formatPrice(item.getTotal())));
            } else {
                table.setWidget(i, 5, new HTML(AccountingUtils.get().formatPrice(item.getTotal())));
            }
            table.getFlexCellFormatter().setStyleName(i, 0, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 1, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 2, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 3, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 4, "flexTable-td");
            table.getFlexCellFormatter().setStyleName(i, 5, "flexTable-td text-right");

            if (viewInterface.isMultiQuoteConvert()) {
                table.getFlexCellFormatter().setStyleName(i, 5, "flexTable-td");
                table.getFlexCellFormatter().setStyleName(i, 6, "flexTable-td text-right");
            }
            i++;
        }

        okButton.addClickHandler(clickEvent -> {
            if (viewInterface.isMultiQuoteConvert()) {
                getMultiQuoteDataForConvert();
            } else {
                getConvertItem();
            }
        });
        cancelButton.addClickHandler(clickEvent -> {
            dialogBox.close();
            if (listener != null) {
                listener.execute();
            }
        });

        MaterialPanel pnlTableContainer = new MaterialPanel();
        pnlTableContainer.add(table);

        dialogBox.add(pnlTableContainer);

        dialogBox.addButton(cancelButton);
        dialogBox.addButton(okButton);
        dialogBox.open();
    }


    private void getMultiQuoteDataForConvert() {
        StringBuilder url = new StringBuilder();
        url.append("saleinvoice|add/add/multiQuoteConvert/");
        int i = 1;
        for (NewInvoice item : items) {
            KpiCheckBox box = (KpiCheckBox) table.getWidget(i, 0);
            RadioButton projectBox = (RadioButton) table.getWidget(i, 5);
            if (box.getValue()) {
                url.append(item.getID());
                if (projectBox.getValue()) {
                    url.append(",").append("true");
                } else {
                    url.append(",").append("false");
                }
                url.append(";");
            }
            i++;
        }
        dialogBox.close();
        viewInterface.getView().closeTab();
        viewInterface.goTo(url.toString());

    }

    private void getConvertItem() {
        int i = 1;
        NewInvoice quoteObject = null;
        for (NewInvoice item : items) {
            RadioButton rd = (RadioButton) table.getWidget(i, 0);
            if (rd.getValue()) {
                quoteObject = item;
            }
            i++;
        }
        if (quoteObject != null) {
            if (quoteObject.isProgressInvoicing()) {
                final Integer quoteID = quoteObject.getID();
                viewInterface.setExternalQuoteID(quoteID);
                final ProgressInvoicingView progressInvoicingView = new ProgressInvoicingView(quoteObject);
                progressInvoicingView.setOnCloseListener(() -> {
                    if (progressInvoicingView.isTypeCustomizedPercentage() || progressInvoicingView.isTypeCustomizedMulti()) {
                        viewInterface.getView().closeTab();
                        return;
                    }

                    viewInterface.setQuotePercent(progressInvoicingView.getEntirePercent());
                    viewInterface.setQuoteAmount(progressInvoicingView.getEntireAmount());
                    viewInterface.setProgressInvoicingByItem(progressInvoicingView.isTypeByItem());
                    viewInterface.setProgressInvoiceDialogBpxType(progressInvoicingView.getSelectedType());
                    if (progressInvoicingView.isTypeByItem()) {
                        viewInterface.setProgressInvoiciningMap(progressInvoicingView.generateConvertMap());
                    }
                    LoadingPanel.loading(true);
                    quoteService.getQuote(quoteID, null, new AsyncCallback<NewInvoice>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(NewInvoice newInvoice) {
                            LoadingPanel.loading(false);
                            viewInterface.applyProgressInvoicingData(newInvoice);
                        }
                    });
                });
            } else {
                convertToSaleInvoice(quoteObject.getID());
            }
            dialogBox.close();
        } else {
            Info.show(accountingStrings.convertErrorMessage(), Info.Type.WARNING);
        }
    }

    private void convertToSaleInvoice(Integer quoteID) {
        LoadingPanel.loading(true);
        quoteService.convertToInvoice(quoteID, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(final Integer invoiceID) {
                LoadingPanel.loading(false);
                Info.show(accountingStrings.convertedSuccessfullyQuote(), Info.Type.INFO);
                Cookies.setCookie(Constants.INVOICE_STATUS, Constants.DRAFT);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, invoiceID, viewInterface.getView());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALESORDER_ADDED, invoiceID, viewInterface.getView());
                viewInterface.getView().closeTab();

                final String redirectUrl = Constants.SALE_INVOICE + "|edit/" + invoiceID;
                if (AccountingUtils.get().enableInvoiceCustomTypes()) {
                    quoteService.getQuoteConvertToInvoiceCustomType(new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            throwable.printStackTrace();
                        }

                        @Override
                        public void onSuccess(String invoiceCustomType) {
                            viewInterface.goTo(redirectUrl + "/" + invoiceCustomType);
                        }
                    });
                } else
                    viewInterface.goTo(redirectUrl);
            }
        });
    }
}