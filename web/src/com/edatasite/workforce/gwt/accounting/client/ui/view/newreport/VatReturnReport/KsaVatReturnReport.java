package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.ksa.KsaVatReturn;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;

public class KsaVatReturnReport extends GccVatReturnReport {

    public KsaVatReturnReport(Integer vatReturnId, View view) {
        super(vatReturnId, view);
    }

    @Override
    protected void generateVatReturn(Integer vatReturnId) {
        vatReturnService.generateVatReturn(vatReturnId, new AsyncCallback<KsaVatReturn>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(KsaVatReturn ksaVatReturn) {
                drawContent(ksaVatReturn);
            }
        });
    }

    private void drawContent(KsaVatReturn ksaVatReturn) {
        //VAT on Sales and all other Outputs
        {
            createItemRow(salesTbody, "1", template.describeBox(accountingStrings.saBox1Title(), accountingStrings.saBox1Description()).asString(), ksaVatReturn.getStandardRateSales(), VatReturnBox.sa_box_1);
            createItemRow(salesTbody, "2", template.describeBox(accountingStrings.saBox2Title(), accountingStrings.saBox2Description()).asString(), ksaVatReturn.getOutOfScope(), VatReturnBox.sa_box_2, false);
            createItemRow(salesTbody, "3", template.describeBox(accountingStrings.saBox3Title(), accountingStrings.saBox3Description()).asString(), ksaVatReturn.getZeroRateSales(), VatReturnBox.sa_box_3, false);
            createItemRow(salesTbody, "4", template.describeBox(accountingStrings.saBox4Title(), accountingStrings.saBox4Description()).asString(), ksaVatReturn.getExports(), VatReturnBox.sa_box_4, false);
            createItemRow(salesTbody, "5", template.describeBox(accountingStrings.saBox5Title(), accountingStrings.saBox5Description()).asString(), ksaVatReturn.getExemptSales(), VatReturnBox.sa_box_5, false);
            //Total purchase row
            createTotalRow(salesTbody, "6", template.describeBox(accountingStrings.saBox6Title(), accountingStrings.saBox6Description()).asString(), ksaVatReturn.getSalesTotal());
        }
        //VAT on Expenses and all other Inputs
        {
            createItemRow(expenseTbody, "7", template.describeBox(accountingStrings.saBox7Title(), accountingStrings.saBox7Description()).asString(), ksaVatReturn.getStandardRatePurchase(), VatReturnBox.sa_box_7);
            createItemRow(expenseTbody, "8", template.describeBox(accountingStrings.saBox8Title(), accountingStrings.saBox8Description()).asString(), ksaVatReturn.getImportsSubjectPaidAtCustom(), VatReturnBox.sa_box_8);
            createItemRow(expenseTbody, "9", template.describeBox(accountingStrings.saBox9Title(), accountingStrings.saBox9Description()).asString(), ksaVatReturn.getImportsSubjectAccountedReverseCharge(), VatReturnBox.sa_box_9);
            createItemRow(expenseTbody, "10", template.describeBox(accountingStrings.saBox10Title(), accountingStrings.saBox10Description()).asString(), ksaVatReturn.getZeroRatePurchase(), VatReturnBox.sa_box_10);
            createItemRow(expenseTbody, "11", template.describeBox(accountingStrings.saBox11Title(), accountingStrings.saBox11Description()).asString(), ksaVatReturn.getExemptPurchase(), VatReturnBox.sa_box_11);
            //Total purchase row
            createTotalRow(expenseTbody, "12", template.describeBox(wfmStirngs.totalPurchases(), accountingStrings.saBox12Description()).asString(), ksaVatReturn.getPurchaseTotal());
        }
        //Net VAT due
        {
            BigDecimal outVatAmount = ksaVatReturn.getSalesTotal().getTaxAmount();
            BigDecimal inVatAmount = ksaVatReturn.getPurchaseTotal().getTaxAmount();
            BigDecimal vatAmount = outVatAmount.subtract(inVatAmount);
            createNetRow(netTbody, "13", template.describeBox(accountingStrings.saBox13Title(), accountingStrings.saBox13Description()).asString(), vatAmount);
            createNetRow(netTbody, "14", template.describeBox(accountingStrings.saBox14Title(), accountingStrings.saBox14Description()).asString(), BigDecimal.ZERO);
            createNetRow(netTbody, "15", template.describeBox(accountingStrings.saBox15Title(), accountingStrings.saBox15Description()).asString(), BigDecimal.ZERO);
            createNetRow(netTbody, "16", template.describeBox(accountingStrings.saBox16Title(), accountingStrings.saBox16Description()).asString(), vatAmount);
        }
    }

    void createTotalRow(Element container, String boxNumber, String description, TaxAmountItem item) {
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

        //Amount
        td = DOM.createTD();
        td.setClassName("disable");
        td.setInnerHTML(getValueAsString(item.getTaxableAmount()));
        tr.appendChild(td);

        //Adjustment
        td = DOM.createTD();
        td.setClassName("disable");
        td.setInnerHTML(getValueAsString(item.getAdjustment()));
        tr.appendChild(td);

        //Tax amount
        td = DOM.createTD();
        td.setClassName("disable");
        td.setInnerHTML(getValueAsString(item.getTaxAmount()));
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
            pdfURL = CommandConstants.PDF_URL + "/ksaVatReturnPDFHandler";
        }
        Utils.sendPDFOrExcelRequest(vatReturnPanel, pdfURL, filterParameter.getRequestParams(), "_blank");
    }
}
