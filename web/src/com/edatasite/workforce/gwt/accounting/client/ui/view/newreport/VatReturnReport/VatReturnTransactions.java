package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.VatReturnTransactionType;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnTransactionItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Paragraph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VatReturnTransactions extends Composite {
    interface VatReturnTransactionsUiBinder extends UiBinder<HTMLPanel, VatReturnTransactions> {
    }

    private static VatReturnTransactionsUiBinder ourUiBinder = GWT.create(VatReturnTransactionsUiBinder.class);
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HeadingElement companyName;
    @UiField
    HeadingElement boxTitle;
    @UiField
    Paragraph periodAsReportTitle;
    @UiField
    TableSectionElement tbody;
    @UiField
    TableCellElement taxableAmountTitle;
    @UiField
    TableCellElement taxAmountTitle;
    @UiField
    Div noResultMessage;

    public VatReturnTransactions(Integer vatReturnId, VatReturnBox box) {

        initWidget(ourUiBinder.createAndBindUi(this));
        noResultMessage.getElement().setInnerHTML(WfmStrings.App.get().noDataAvailable());
        if (Utils.isUKCompany()) {
            taxableAmountTitle.removeFromParent();
            taxAmountTitle.setInnerHTML(wfmStrings.amount().toUpperCase(Locale.ROOT));
        }

        VatReturnService.App.get().getReturnTransactionsByBox(vatReturnId, box, new AsyncCallback<ArrayList<VatReturnTransactionItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<VatReturnTransactionItem> vatReturnTransactionItems) {
                drawItems(vatReturnTransactionItems);
            }
        });

        VatReturnService.App.get().getVatReturn(vatReturnId, new AsyncCallback<VatReturnItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(VatReturnItem vatReturnItem) {
                boxTitle.setInnerHTML(getBoxTitle(box));
                companyName.setInnerHTML(Utils.getCompanyName());
                periodAsReportTitle.setText("From " + DateUtils.format(vatReturnItem.getFromDate().getNonConvertedDate()) + " To " + DateUtils.format(vatReturnItem.getToDate().getNonConvertedDate()));
            }
        });
    }

    private void drawItems(List<VatReturnTransactionItem> items) {
        tbody.removeAllChildren();

        if (items != null && items.size() > 0) {
            noResultMessage.setVisible(false);
            BigDecimal totalTaxableAmount = BigDecimal.ZERO, totaTaxAmount = BigDecimal.ZERO;

            for (VatReturnTransactionItem item : items) {
                totalTaxableAmount = totalTaxableAmount.add(item.getAmountItem().getTaxableAmount());
                totaTaxAmount = totaTaxAmount.add(item.getAmountItem().getTaxAmount());

                createItemRow(tbody, item);
            }

            createTotalRow(tbody, totalTaxableAmount, totaTaxAmount);
        }
    }

    private void createItemRow(Element container, VatReturnTransactionItem item) {
        Element tr = DOM.createTR();
        container.appendChild(tr);

        //date
        Element td = DOM.createTD();
        td.setInnerHTML(item.getDate() != null ? DateUtils.format(item.getDate().getNonConvertedDate()) : "");
        tr.appendChild(td);

        //number
        td = DOM.createTD();
        Element link = DOM.createAnchor();
        link.setInnerHTML(item.getNumber());
        DOM.sinkEvents(link.cast(), Event.ONCLICK);
        String url = "";
        if (VatReturnTransactionType.SALES_INVOICE.getTitle().equals(item.getType().getTitle())) {
            url = "saleinvoice|summary/" + item.getObjectId();
        } else if (VatReturnTransactionType.PURCHASE_INVOICE.getTitle().equals(item.getType().getTitle())) {
            url = "purchaseinvoice|summary/" + item.getObjectId();
        } else if (VatReturnTransactionType.EXPENSE.getTitle().equals(item.getType().getTitle())) {
            url = "expenseReports|previewReport/" + item.getObjectId() + "/" + Constants.EXPENSE_VIEW;
        } else if (Arrays.asList(VatReturnTransactionType.CASH_RECEIPT, VatReturnTransactionType.CASH_PAYMENT, VatReturnTransactionType.RECEIVE_MONEY, VatReturnTransactionType.SPEND_MONEY).contains(item.getType())) {
            url = "spendreceivemoney|summary/" + item.getObjectId() + "/" + item.getType().name();
        }
        String finalUrl = url;
        DOM.setEventListener(link.cast(), event -> SinksContainerFactory.entryPoint.onHistoryChanged(finalUrl, item.getNumber()));
        td.appendChild(link);
        tr.appendChild(td);

        //type
        td = DOM.createTD();
        td.setInnerHTML(item.getType().getTitle());
        tr.appendChild(td);

        //crmAccountName
        td = DOM.createTD();
        td.setInnerHTML(item.getCrmAccountName());
        tr.appendChild(td);

        //crmAccountName
        td = DOM.createTD();
        td.setInnerHTML(item.getCrmAccountTrn());
        tr.appendChild(td);

        //taxable amount
        if (!Utils.isUKCompany()) {
            td = DOM.createTD();
            td.setInnerHTML(AccountingUtils.get().format(item.getAmountItem().getTaxableAmount()));
            td.addClassName("text-right");
            tr.appendChild(td);
        }
        //tax amount
        td = DOM.createTD();
        td.setInnerHTML(AccountingUtils.get().format(item.getAmountItem().getTaxAmount()));
        td.addClassName("text-right");
        tr.appendChild(td);
    }

    private void createTotalRow(Element element, BigDecimal totalTaxableAmount, BigDecimal totalTaxAmount) {
        Element tr = DOM.createTR();
        tr.addClassName("total_row");
        element.appendChild(tr);

        Element td = DOM.createTD();
        td.setInnerHTML(WfmStrings.App.get().total());
        td.setAttribute("colspan", "5");
        tr.appendChild(td);

        if (!Utils.isUKCompany()) {
            td = DOM.createTD();
            td.setInnerHTML(AccountingUtils.get().format(totalTaxableAmount));
            td.addClassName("text-right");
            tr.appendChild(td);
        }

        td = DOM.createTD();
        td.setInnerHTML(AccountingUtils.get().format(totalTaxAmount));
        td.addClassName("text-right");
        tr.appendChild(td);
    }

    private String getBoxTitle(VatReturnBox box) {

        if (VatReturnBox.ae_box_1a.equals(box)) {
            return "Box 1a - " + accountingStrings.box1aTitle();
        } else if (VatReturnBox.ae_box_1b.equals(box)) {
            return "Box 1b - " + accountingStrings.box1bTitle();
        } else if (VatReturnBox.ae_box_1c.equals(box)) {
            return "Box 1c - " + accountingStrings.box1cTitle();
        } else if (VatReturnBox.ae_box_1d.equals(box)) {
            return "Box 1d - " + accountingStrings.box1dTitle();
        } else if (VatReturnBox.ae_box_1e.equals(box)) {
            return "Box 1e - " + accountingStrings.box1eTitle();
        } else if (VatReturnBox.ae_box_1f.equals(box)) {
            return "Box 1f - " + accountingStrings.box1fTitle();
        } else if (VatReturnBox.ae_box_1g.equals(box)) {
            return "Box 1g - " + accountingStrings.box1gTitle();
        } else if (VatReturnBox.ae_box_2.equals(box)) {
            return "Box 2 - " + accountingStrings.box2Title();
        } else if (VatReturnBox.ae_box_3.equals(box)) {
            return "Box 3 - " + accountingStrings.box3Title();
        } else if (VatReturnBox.ae_box_4.equals(box)) {
            return "Box 4 - " + accountingStrings.box4Title();
        } else if (VatReturnBox.ae_box_5.equals(box)) {
            return "Box 5 - " + accountingStrings.box5Title();
        } else if (VatReturnBox.ae_box_8.equals(box)) {
            return "Box 8 - " + accountingStrings.box8Title();
        } else if (VatReturnBox.ae_box_9.equals(box)) {
            return "Box 9 - " + accountingStrings.box9Title();
        }
        return "";
    }
}
