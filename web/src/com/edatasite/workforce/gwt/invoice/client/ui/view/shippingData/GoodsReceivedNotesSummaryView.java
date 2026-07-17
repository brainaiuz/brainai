package com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.AllocationTextBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder.GrnExpenseAllocationPanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.shippingData.form.AbstractGdnGrnSummaryView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class GoodsReceivedNotesSummaryView extends AbstractGdnGrnSummaryView implements Colapse {
    protected final Integer id;
    protected static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    public GoodsReceivedNotesSummaryView(Integer id) {
        super("summary", accountingStrings.grnNumber(), true);
        this.id = id;
    }


    @Override
    protected void initializeData() {
        LoadingPanel.loading(true);
        QuoteService.App.get().getShippingData(id, false, new AbstractAsyncCallback<ShippingData>() {
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
        return CommandConstants.PDF_URL + "/grnOrderViewPDFHandler";
    }

    @Override
    protected String excelUrl() {
        return null;
    }

    @Override
    protected String convertToInvoiceLink() {
        return "purchaseinvoice|add/add/convertFromGrn/";
    }

    @Override
    protected void deleteGdnGrn(Integer id) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().validateStockInconsistencyInDeleteProcess(StockTransactionType.GRN, id, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem result) {
                if (result == null) {
                    deleteGRN(id);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    private void deleteGRN(Integer objectID) {
        QuoteService.App.get().deleteGoodsReceivedNotes(id, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.someErrorsOccured(), Info.Type.WARNING);
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (MessageCommand.hasConvertedItems.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGrnHasConvertedInvoices(), Info.Type.WARNING);
                } else if (MessageCommand.hasOutTransactions.equals(result.getMessageCommand())) {
                    Info.show(accountingStrings.cannotDeleteGrnHasOutTransactions(), Info.Type.WARNING);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), accountingStrings.goodsDeliveredNotes()), Info.Type.INFO);
                    closeTab();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, null);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.PURCHASE_ORDER_SUMMARY_RELOAD_PAGE, null, null);
                }
            }
        });
    }

    ExpenseListItem[] expenseListItems;
    GrnExpenseAllocationPanel expenseAllocationPanel;
    Div applyAllocationContainer = null;

    private void applyAllocation() {
        if (expenseAllocationPanel != null) {
            for (int index = 0; index < shippingData.getItems().size(); index++) {
                DynamicTableItem tableItem = itemTable.getItem(index);
                AllocationTextBox txtAllocationBox = (AllocationTextBox) tableItem.getColumnById(ProductsTable.ALLOCATION);
                shippingData.getItems().get(index).setReceivedAllocation(txtAllocationBox.getAllocatedAmount());
            }

            BigDecimal totalAllocatedAmount = shippingData.getItems().stream().reduce(BigDecimal.ZERO, (total, item) -> total.add(Optional.ofNullable(item.getReceivedAllocation()).orElse(BigDecimal.ZERO)), BigDecimal::add);
            if (totalAllocatedAmount.compareTo(expenseAllocationPanel.getTotalExpenses()) > 0) {
                Info.show(accountingMessages.youCantAllocateMoreThanRemainingBalance(), Info.Type.WARNING);
                return;
            }

            LoadingPanel.loading(true);
            QuoteService.App.get().allocateExpensesToGrn(shippingData, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Integer result) {
                    LoadingPanel.loading(false);
                    if (result.equals(Errors.COMPLETED)) {
                        Info.show(accountingStrings.landedCostSuccessfullyAllocated(), Info.Type.INFO);
                        closeTab();
                    } else if (result.equals(Errors.MORE_THAN_UNALLOCATED_AMOUNT)) {
                        Info.show(accountingStrings.youCantAllocateMoreThanUnallocatedAmount(), Info.Type.WARNING);
                    } else {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }
                }
            });
        }
    }

    @Override
    public List<Widget> getFooterLeftSideWidgets() {
        List<Widget> leftSideWidgets = super.getFooterLeftSideWidgets();

        if (shippingData != null && shippingData.getRelatedExpenses() != null && shippingData.getRelatedExpenses().size() > 0) {
            FooterInformer reletedExpenses = new FooterInformer(SvgEnum.invoice, accountingStrings.relatedExpenses(), null);
            reletedExpenses.addClickHandler(event -> {
                expenseAllocationPanel = new GrnExpenseAllocationPanel(shippingData.getTotalAllocatedAmount(), itemTable, shippingData, false, () -> {
                    applyAllocationContainer.setVisible(true);
                });
                expenseAllocationPanel.setExpenseItems(expenseListItems);
                expenseAllocationPanel.show();
            });
            leftSideWidgets.add(reletedExpenses);
            loadRelatedExpenses(reletedExpenses);
        }
        return leftSideWidgets;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        List<Widget> rightSideWidgets = super.getFooterRightSideWidgets();

        applyAllocationContainer = new Div();
        applyAllocationContainer.setVisible(false);

        WfmButton2 btnApplyAllocation = new WfmButton2(accountingStrings.applyAllocation(), WfmButton2.BTN_PRIMARY);
        btnApplyAllocation.addClickHandler(ch -> applyAllocation());

        applyAllocationContainer.add(btnApplyAllocation);
        rightSideWidgets.add(applyAllocationContainer);

        return rightSideWidgets;
    }

    private void loadRelatedExpenses(FooterInformer reletedExpenses) {
        ExpenseService.App.get().getExpenseItemsForPOAllocation(shippingData.getQuoteId(), new AsyncCallback<ExpenseListItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ExpenseListItem[] items) {
                if (items != null && items.length > 0 && reletedExpenses != null) {
                    reletedExpenses.setBadgeCount(items.length);
                }
                expenseListItems = items;
            }
        });
    }
}
