package com.edatasite.workforce.gwt.accounting.client.ui.view.report;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnDetailItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/15/12
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class VatReturnReportDetailsBox extends KpiModal implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private FlexTable itemsTable;
    private HashMap<Integer, LinkedList<VatReturnDetailItem>> detailItemsMap;

    public VatReturnReportDetailsBox() {
        super();
        setTitle(wfmStrings.details());
        itemsTable = new FlexTable();
        itemsTable.getColumnFormatter().setWidth(0, "300px");
        itemsTable.getColumnFormatter().setWidth(1, "100px");

        ScrollPanel itemsScrollPanel = new ScrollPanel();
        itemsScrollPanel.setSize("410px", "300px");
        itemsScrollPanel.add(itemsTable);

        WfmButton2 closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        closeButton.addClickHandler(event -> close());

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(itemsScrollPanel);
        mainPanel.add(closeButton);
        mainPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
        add(mainPanel);
    }

    public void initForm(Integer boxID) {
        itemsTable.removeAllRows();

        LinkedList<VatReturnDetailItem> itemsList = detailItemsMap.get(boxID);
        if (itemsList != null && itemsList.size() > 0) {
            int i = 0;
            for (VatReturnDetailItem item : itemsList) {
                String tempURL = null;
                if (INVOICE_TRANSACTION.equals(item.getType())) {
                    if (RECEIVABLE.equals(item.getTransferType())) {
                        tempURL = (item.isCreditNote() ? "receivablecreditnote|summary/" : "saleinvoice|summary/") + item.getObjectID();
                    } else {
                        tempURL = (item.isCreditNote() ? "payablecreditnote|summary/" : "purchaseinvoice|summary/") + item.getObjectID();
                    }
                } else if (EXPENSE_TRANSACTION.equals(item.getType())) {
                    tempURL = "expenseReports|previewReport/" + item.getObjectID();
                } else if (MANUAL_TRANSACTION.equals(item.getType())) {
                    tempURL = (AccountingConstants.RECEIVE_MONEY_STR.equals(item.getTransferType()) ? "receivemoney" : AccountingConstants.SPEND_MONEY_STR.equals(item.getTransferType()) ? "spendmoney" : AccountingConstants.CASH_PAYMENT.equals(item.getTransferType()) ? "cashpayment" : "cashreceive") + "|summary/" + item.getObjectID() + "/" + AccountingConstants.VIEW_FORM;
                } else if (BANK_TRANSFER_TRANSACTION.equals(item.getType())) {
                    tempURL = "spendreceivemoney|summary/" + item.getObjectID() + "/" + item.getTransferType();
                } else if (BANK_OPENING_BALANCE_TRANSACTION.equals(item.getType()) || BANK_MONEY_TRANSFER_TRANSACTION.equals(item.getType())) {
                    tempURL = "transfer|summary/" + item.getObjectID();
                }

                if (tempURL != null) {
                    final String actionURL = tempURL;
                    Anchor entityLink = new Anchor(item.getName());
                    entityLink.addClickHandler(event -> {
                        close();
                        SinksContainerFactory.entryPoint.onHistoryChanged(actionURL, item.getNumber());
                    });
                    itemsTable.setWidget(i, 0, entityLink);
                } else {
                    itemsTable.setWidget(i, 0, new Label(item.getName()));
                }
                itemsTable.setWidget(i, 1, new Label(AccountingUtils.get().formatPrice(item.getAmount())));
                i++;
            }
        } else {
            itemsTable.setWidget(0, 0, new HTML("<b>" + wfmStrings.thereAreNoItemsToShow() + "</b>"));
            itemsTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        }

        open();
    }

    public void setDetailItemsMap(HashMap<Integer, LinkedList<VatReturnDetailItem>> detailItemsMap) {
        this.detailItemsMap = detailItemsMap;
    }
}
