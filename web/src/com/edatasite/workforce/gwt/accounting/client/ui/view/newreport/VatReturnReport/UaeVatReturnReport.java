package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea.UaeVatReturn;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;

public class UaeVatReturnReport extends GccVatReturnReport {
    public UaeVatReturnReport(Integer vatReturnId, View view) {
        super(vatReturnId, view);
    }

    @Override
    protected void generateVatReturn(Integer vatReturnId) {
        vatReturnService.generateVatReturn(vatReturnId, new AsyncCallback<UaeVatReturn>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(UaeVatReturn uaeVatReturn) {
                drawContent(uaeVatReturn);
            }
        });
    }

    private void drawContent(UaeVatReturn uaeVatReturn) {
        salesTbody.removeAllChildren();
        BigDecimal A1 = BigDecimal.ZERO, A2 = BigDecimal.ZERO, A3 = BigDecimal.ZERO, A4 = BigDecimal.ZERO,
                A5 = BigDecimal.ZERO, A6 = BigDecimal.ZERO, A7 = BigDecimal.ZERO;

        /**
         * VAT on Sales and all other Outputs
         */
        {
            createItemRow(salesTbody, "1a", template.describeBox(accountingStrings.box1aTitle(), accountingStrings.box1aDescription()).asString(), uaeVatReturn.getAbudhabi(), VatReturnBox.ae_box_1a);
            createItemRow(salesTbody, "1b", template.describeBox(accountingStrings.box1bTitle(), accountingStrings.box1bDescription()).asString(), uaeVatReturn.getDubai(), VatReturnBox.ae_box_1b);
            createItemRow(salesTbody, "1c", template.describeBox(accountingStrings.box1cTitle(), accountingStrings.box1cDescription()).asString(), uaeVatReturn.getSharjah(), VatReturnBox.ae_box_1c);
            createItemRow(salesTbody, "1d", template.describeBox(accountingStrings.box1dTitle(), accountingStrings.box1dDescription()).asString(), uaeVatReturn.getAjman(), VatReturnBox.ae_box_1d);
            createItemRow(salesTbody, "1e", template.describeBox(accountingStrings.box1eTitle(), accountingStrings.box1eDescription()).asString(), uaeVatReturn.getUmmAlQuwain(), VatReturnBox.ae_box_1e);
            createItemRow(salesTbody, "1f", template.describeBox(accountingStrings.box1fTitle(), accountingStrings.box1fDescription()).asString(), uaeVatReturn.getRasAlKhalmah(), VatReturnBox.ae_box_1f);
            createItemRow(salesTbody, "1g", template.describeBox(accountingStrings.box1gTitle(), accountingStrings.box1gDescription()).asString(), uaeVatReturn.getFujairah(), VatReturnBox.ae_box_1g);
            createItemRow(salesTbody, "2", template.describeBox(accountingStrings.box2Title(), accountingStrings.box2Description()).asString(), uaeVatReturn.getReverscharge(), VatReturnBox.ae_box_2);
            createItemRow(salesTbody, "3", template.describeBox(accountingStrings.box3Title(), accountingStrings.box3Description()).asString(), uaeVatReturn.getZeroRated(), VatReturnBox.ae_box_3, false);
            createItemRow(salesTbody, "4", template.describeBox(accountingStrings.box4Title(), accountingStrings.box4Description()).asString(), uaeVatReturn.getExempt(), VatReturnBox.ae_box_4, false);
            createItemRow(salesTbody, "5", template.describeBox(accountingStrings.box5Title(), accountingStrings.box5Description()).asString(), uaeVatReturn.getGoodsImported(), VatReturnBox.ae_box_5);
            createItemRow(salesTbody, "6", template.describeBox(accountingStrings.box6Title(), accountingStrings.box6Description()).asString(), uaeVatReturn.getAdjustment(), VatReturnBox.ae_box_6);

            A1 = A1.add(uaeVatReturn.getAbudhabi().getTaxAmount())
                    .add(uaeVatReturn.getDubai().getTaxAmount())
                    .add(uaeVatReturn.getSharjah().getTaxAmount())
                    .add(uaeVatReturn.getAjman().getTaxAmount())
                    .add(uaeVatReturn.getUmmAlQuwain().getTaxAmount())
                    .add(uaeVatReturn.getRasAlKhalmah().getTaxAmount())
                    .add(uaeVatReturn.getFujairah().getTaxAmount())
                    .add(uaeVatReturn.getReverscharge().getTaxAmount())
                    .add(uaeVatReturn.getAdjustment().getTaxAmount())
                    .add(uaeVatReturn.getGoodsImported().getTaxAmount());

            A2 = A2.add(uaeVatReturn.getAbudhabi().getAdjustment())
                    .add(uaeVatReturn.getDubai().getAdjustment())
                    .add(uaeVatReturn.getSharjah().getAdjustment())
                    .add(uaeVatReturn.getAjman().getAdjustment())

                    .add(uaeVatReturn.getUmmAlQuwain().getAdjustment())
                    .add(uaeVatReturn.getRasAlKhalmah().getAdjustment())
                    .add(uaeVatReturn.getFujairah().getAdjustment())
                    .add(uaeVatReturn.getReverscharge().getAdjustment())
                    .add(uaeVatReturn.getAdjustment().getAdjustment())
                    .add(uaeVatReturn.getGoodsImported().getAdjustment());

            createTotalRow(salesTbody, "7", template.describeBox(wfmStirngs.totals(), accountingStrings.box7Description()).asString(), template.totalCell("A1", getValueAsString(A1)).asString(), template.totalCell("A2", getValueAsString(A2)).asString());
        }
        /**
         * VAT on Expenses and all other Inputs
         */
        {
            createItemRow(expenseTbody, "8", template.describeBox(accountingStrings.box8Title(), accountingStrings.box8Description()).asString(), uaeVatReturn.getExpenses(), VatReturnBox.ae_box_8);
            createItemRow(expenseTbody, "9", template.describeBox(accountingStrings.box9Title(), accountingStrings.box9Description()).asString(), uaeVatReturn.getExpenseReverceCharge(), VatReturnBox.ae_box_9);

            A3 = A3.add(uaeVatReturn.getExpenses().getTaxAmount())
                    .add(uaeVatReturn.getExpenseReverceCharge().getTaxAmount());

            A4 = A4.add(uaeVatReturn.getExpenses().getAdjustment())
                    .add(uaeVatReturn.getExpenseReverceCharge().getAdjustment());

            createTotalRow(expenseTbody, "10", template.describeBox(wfmStirngs.totals(), accountingStrings.box10Description()).asString(), template.totalCell("A3", getValueAsString(A3)).asString(), template.totalCell("A4", getValueAsString(A4)).asString());
        }

        /**
         * Net VAT due
         */
        {
            A5 = A5.add(A1).add(A2);
            A6 = A6.add(A3).add(A4);
            A7 = A7.add(A5.subtract(A6));

            createNetRow(netTbody, "11", template.describeNetBox(accountingStrings.box11Title(), "A5=A1+A2", accountingStrings.box11Description()).asString(), A5);
            createNetRow(netTbody, "12", template.describeNetBox(accountingStrings.box12Title(), "A6=A3+A4", accountingStrings.box12Description()).asString(), A6);
            createNetRow(netTbody, "13", template.describeNetBox(accountingStrings.box13Title(), "A7=A5-A6", accountingStrings.box13Description()).asString(), A7);
            createNetRow(netTbody, "14", template.describeBox(accountingStrings.box14Title(), accountingStrings.box14Description()).asString(), null);
        }
    }

    void createTotalRow(Element container, String boxNumber, String description, String A1, String A2) {
        Element tr = DOM.createTR();
        container.appendChild(tr);

        //box number
        Element td = DOM.createTD();
        td.setInnerHTML(boxNumber);
        tr.appendChild(td);

        //description
        td = DOM.createTD();
        td.setInnerHTML(description);
        tr.appendChild(td);

        td = DOM.createTD();
        tr.appendChild(td);

        //Tax amount
        td = DOM.createTD();
        td.setClassName("disable");
        td.setInnerHTML(A1);
        tr.appendChild(td);

        //Adjustment
        td = DOM.createTD();
        td.setClassName("disable");
        td.setInnerHTML(A2);
        tr.appendChild(td);
    }

    @Override
    protected void generatePdfOrExcel(boolean isExcel) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(vatReturnId);
        String pdfURL;
        if (isExcel) {
            pdfURL = CommandConstants.COMMON_URL + "/uaeVatReturnExcelHandler";
        } else {
            pdfURL = CommandConstants.PDF_URL + "/uaeVatReturnPDFHandler";
        }
        Utils.sendPDFOrExcelRequest(vatReturnPanel, pdfURL, filterParameter.getRequestParams(), "_blank");
    }
}
