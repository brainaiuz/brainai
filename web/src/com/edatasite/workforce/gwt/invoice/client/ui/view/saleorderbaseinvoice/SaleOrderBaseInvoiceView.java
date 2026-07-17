package com.edatasite.workforce.gwt.invoice.client.ui.view.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.SalesInvoiceView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SaleOrderBaseInvoiceView extends FooteredView implements Constants, AccountingConstants, Colapse, FittedContent {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private SaleOrderBaseInvoice saleOrderBaseInvoice;
    private WfmButton2 continueButton;

    private String type;
    private SalesInvoiceView salesInvoiceView;

    public SaleOrderBaseInvoiceView() {
        super("saleorderBaseInvoice", accountingMessages.saleBaseInvoice(Property.get(SALE_ORDER_CODE, wfmStrings.saleorder())));
    }

    public SaleOrderBaseInvoiceView(String type) {
        super("saleorderBaseInvoice", SaleOrderBaseInvoiceItem.SALE_ORDER.equals(type) ? accountingMessages.saleBaseInvoice(Property.get(SALE_ORDER_CODE, wfmStrings.saleorder())) : accountingMessages.saleBaseInvoice(Property.get(SALE_QUOTE, wfmStrings.salesQuote())));
        this.type = type;
    }

    protected Widget onInitialize() {
        saleOrderBaseInvoice = new SaleOrderBaseInvoice(type);
        continueButton = new WfmButton2(accountingStrings.getPropertyContinue(), WfmButton2.BTN_PRIMARY);
        continueButton.addClickHandler(ch -> {
            List<SaleOrderBaseInvoiceItem> selectedItems = saleOrderBaseInvoice.getSelectedItems();
            if (selectedItems == null || selectedItems.isEmpty()) {
                Info.warn("You have to select at least one item");
                return;
            }
            clear();

            if (SOBaseInvoiceGroups.DETAILED_INVOICE.equals(saleOrderBaseInvoice.getInvoiceType())) {
                initDetailedInvoice(selectedItems);
            } else {
                initGroupedInvoice();
            }
        });

        add(saleOrderBaseInvoice);
        add(createFooter());
        return null;
    }

    private void initDetailedInvoice(List<SaleOrderBaseInvoiceItem> selectedItems) {
        StringBuilder ids = new StringBuilder();
        if (SaleOrderBaseInvoiceItem.GDN.equals(saleOrderBaseInvoice.getObjectType())) {
            selectedItems.stream().map(SaleOrderBaseInvoiceItem::getQuoteId).distinct().forEach(quoteId -> ids.append(quoteId).append(";"));
        } else {
            selectedItems.forEach(sItem -> ids.append(sItem.getObjectId()).append(";"));
        }
        salesInvoiceView = new SalesInvoiceView(new String[]{"add", "multiQuoteConvert", ids.toString()}, Constants.SALE_INVOICE);
        salesInvoiceView.setContainer(getContainer());
        salesInvoiceView.getFormParams().getMultiQuoteConvertItem().setObjectType(saleOrderBaseInvoice.getObjectType());

        if (saleOrderBaseInvoice.isGroupedByItem()) {
            salesInvoiceView.getFormParams().getMultiQuoteConvertItem().setGroupByItem(true);
            salesInvoiceView.getFormParams().getMultiQuoteConvertItem().setGroupingFields(saleOrderBaseInvoice.getGrouppingFields());
        }
        salesInvoiceView.asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Widget widget) {
                add(salesInvoiceView);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEORDER_BASE_INVOICE_LOADED, SaleOrderBaseInvoiceView.this, (sender, args) -> {
            //needed for chrome extension don't change please!
            Utils.triggerCustomJSEvent("saleOrderBaseInvoiceLoaded");

            salesInvoiceView.setPeriod(saleOrderBaseInvoice.getFromDate(), saleOrderBaseInvoice.getToDate());
        });
    }

    private void initGroupedInvoice() {
        salesInvoiceView = new SalesInvoiceView(new String[]{"add", "contact", saleOrderBaseInvoice.getClientId().toString()}, Constants.SALE_INVOICE);
        salesInvoiceView.setContainer(getContainer());
        salesInvoiceView.asyncOnInitialize(new AsyncCallback<Widget>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Widget widget) {
                add(salesInvoiceView);

                WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALEORDER_BASE_INVOICE_LOADED, SaleOrderBaseInvoiceView.this, (sender, args) -> {
                    salesInvoiceView.setPeriod(saleOrderBaseInvoice.getFromDate(), saleOrderBaseInvoice.getToDate());
                    salesInvoiceView.getProductsTable().setConvertedQuoteIds(SaleOrderBaseInvoiceItem.GDN.equals(saleOrderBaseInvoice.getObjectType()) ?
                            new ArrayList<>(saleOrderBaseInvoice.getSelectedItems().stream().map(SaleOrderBaseInvoiceItem::getQuoteId).collect(Collectors.toList())) :
                            new ArrayList<>(saleOrderBaseInvoice.getSelectedItems().stream().map(SaleOrderBaseInvoiceItem::getObjectId).collect(Collectors.toList())));
                    generateItems((NewInvoice) args);
                });
            }
        });
    }

    private void generateItems(NewInvoice invoice) {
        QuoteService.App.get().getGroupedItems(saleOrderBaseInvoice.getObjectType(),
                saleOrderBaseInvoice.getSelectedItems().stream().map(SaleOrderBaseInvoiceItem::getObjectId).collect(Collectors.toCollection(ArrayList::new)),
                saleOrderBaseInvoice.getSelectedNameFields(),
                saleOrderBaseInvoice.getSelectedDescFields(), new AsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(SelectItem[] selectItems) {
                        if (selectItems != null && selectItems.length > 0) {
                            ArrayList<NewInvoiceItem> invItems = new ArrayList<>();

                            for (SelectItem item : selectItems) {
                                NewInvoiceItem invItem = new NewInvoiceItem();
                                invItem.setItemID(null);
                                invItem.setItemName(item.getName());
                                invItem.setFullItemName(item.getName());
                                invItem.setDescription(item.getDescription());
                                invItem.setUnitPrice(item.getTotalAmount() != null ? BigDecimal.valueOf(item.getTotalAmount()) : BigDecimal.ZERO);
                                invItem.setQuantity(BigDecimal.ONE);
                                invItems.add(invItem);
                            }

                            if (!invItems.isEmpty()) {
                                invoice.setItems(invItems.toArray(new NewInvoiceItem[]{}));
                                salesInvoiceView.getProductsTable().setValues(invoice);
                            }
                        }
                    }
                });
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return SaleOrderBaseInvoiceView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return SaleOrderBaseInvoiceView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();

        Div continueWrapper = new Div();

        continueWrapper.add(continueButton);

        result.add(continueWrapper);
        return result;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

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
