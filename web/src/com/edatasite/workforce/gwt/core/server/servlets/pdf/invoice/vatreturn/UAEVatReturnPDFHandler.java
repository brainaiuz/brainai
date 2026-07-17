package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea.UaeVatReturn;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;

import java.math.BigDecimal;

public class UAEVatReturnPDFHandler extends GccVatReturnPDFHandler {

    private BigDecimal A1 = BigDecimal.ZERO;
    private BigDecimal A2 = BigDecimal.ZERO;
    private BigDecimal A3 = BigDecimal.ZERO;
    private BigDecimal A4 = BigDecimal.ZERO;
    private BigDecimal A5 = BigDecimal.ZERO;
    private BigDecimal A6 = BigDecimal.ZERO;
    private BigDecimal A7 = BigDecimal.ZERO;


    protected CustomisedITextTable getVatSalesTable(VatReturnData vatReturnData) {
        UaeVatReturn vatReturn = (UaeVatReturn) vatReturnData;
        CustomisedITextTable salesTable = new CustomisedITextTable();

        salesTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT, COLUMN_TAX_AMOUNT, COLUMN_ADJUSTMENTS);
        salesTable.addRow("1a", createDescriptionBox("1a"), getValueAsString(vatReturn.getAbudhabi().getTaxableAmount()), getValueAsString(vatReturn.getAbudhabi().getTaxAmount()), getValueAsString(vatReturn.getAbudhabi().getAdjustment()));
        salesTable.addRow("1b", createDescriptionBox("1b"), getValueAsString(vatReturn.getDubai().getTaxableAmount()), getValueAsString(vatReturn.getDubai().getTaxAmount()), getValueAsString(vatReturn.getDubai().getAdjustment()));
        salesTable.addRow("1c", createDescriptionBox("1c"), getValueAsString(vatReturn.getSharjah().getTaxableAmount()), getValueAsString(vatReturn.getSharjah().getTaxAmount()), getValueAsString(vatReturn.getSharjah().getAdjustment()));
        salesTable.addRow("1d", createDescriptionBox("1d"), getValueAsString(vatReturn.getAjman().getTaxableAmount()), getValueAsString(vatReturn.getAjman().getTaxAmount()), getValueAsString(vatReturn.getAjman().getAdjustment()));
        salesTable.addRow("1e", createDescriptionBox("1e"), getValueAsString(vatReturn.getUmmAlQuwain().getTaxableAmount()), getValueAsString(vatReturn.getUmmAlQuwain().getTaxAmount()), getValueAsString(vatReturn.getUmmAlQuwain().getAdjustment()));
        salesTable.addRow("1f", createDescriptionBox("1f"), getValueAsString(vatReturn.getRasAlKhalmah().getTaxableAmount()), getValueAsString(vatReturn.getRasAlKhalmah().getTaxAmount()), getValueAsString(vatReturn.getRasAlKhalmah().getAdjustment()));
        salesTable.addRow("1g", createDescriptionBox("1g"), getValueAsString(vatReturn.getFujairah().getTaxableAmount()), getValueAsString(vatReturn.getFujairah().getTaxAmount()), getValueAsString(vatReturn.getFujairah().getAdjustment()));
        salesTable.addRow("2", createDescriptionBox("2"), getValueAsString(vatReturn.getReverscharge().getTaxableAmount()), getValueAsString(vatReturn.getReverscharge().getTaxAmount()), getValueAsString(vatReturn.getReverscharge().getAdjustment()));
        salesTable.addRow("3", createDescriptionBox("3"), getValueAsString(vatReturn.getZeroRated().getTaxableAmount()), getValueAsString(vatReturn.getZeroRated().getTaxAmount()), getValueAsString(vatReturn.getZeroRated().getAdjustment()));
        salesTable.addRow("4", createDescriptionBox("4"), getValueAsString(vatReturn.getExempt().getTaxableAmount()), getValueAsString(vatReturn.getExempt().getTaxAmount()), getValueAsString(vatReturn.getExempt().getAdjustment()));
        salesTable.addRow("5", createDescriptionBox("5"), getValueAsString(vatReturn.getGoodsImported().getTaxableAmount()), getValueAsString(vatReturn.getGoodsImported().getTaxAmount()), getValueAsString(vatReturn.getGoodsImported().getAdjustment()));
        salesTable.addRow("6", createDescriptionBox("6"), getValueAsString(vatReturn.getAdjustment().getTaxableAmount()), getValueAsString(vatReturn.getAdjustment().getTaxAmount()), getValueAsString(vatReturn.getAbudhabi().getAdjustment()));

        A1 = A1.add(vatReturn.getAbudhabi().getTaxAmount())
                .add(vatReturn.getDubai().getTaxAmount())
                .add(vatReturn.getSharjah().getTaxAmount())
                .add(vatReturn.getAjman().getTaxAmount())
                .add(vatReturn.getUmmAlQuwain().getTaxAmount())
                .add(vatReturn.getRasAlKhalmah().getTaxAmount())
                .add(vatReturn.getFujairah().getTaxAmount())
                .add(vatReturn.getReverscharge().getTaxAmount())
                .add(vatReturn.getGoodsImported().getTaxAmount())
                .add(vatReturn.getAdjustment().getTaxAmount());

        A2 = A2.add(vatReturn.getAbudhabi().getAdjustment())
                .add(vatReturn.getDubai().getAdjustment())
                .add(vatReturn.getSharjah().getAdjustment())
                .add(vatReturn.getAjman().getAdjustment())
                .add(vatReturn.getUmmAlQuwain().getAdjustment())
                .add(vatReturn.getRasAlKhalmah().getAdjustment())
                .add(vatReturn.getFujairah().getAdjustment())
                .add(vatReturn.getReverscharge().getAdjustment())
                .add(vatReturn.getGoodsImported().getAdjustment())
                .add(vatReturn.getAdjustment().getAdjustment());

        salesTable.addRow("7", createDescriptionBox("7"), null, getValueAsString(A1), getValueAsString(A2));

        return salesTable;
    }

    protected CustomisedITextTable getVatExpenseTable(VatReturnData vatReturnData) {
        UaeVatReturn vatReturn = (UaeVatReturn) vatReturnData;
        CustomisedITextTable expenseTable = new CustomisedITextTable();

        expenseTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT, COLUMN_TAX_AMOUNT, COLUMN_ADJUSTMENTS);
        expenseTable.addRow("8", createDescriptionBox("8"), getValueAsString(vatReturn.getExpenses().getTaxableAmount()), getValueAsString(vatReturn.getExpenses().getTaxAmount()), getValueAsString(vatReturn.getExpenses().getAdjustment()));
        expenseTable.addRow("9", createDescriptionBox("9"), getValueAsString(vatReturn.getExpenseReverceCharge().getTaxableAmount()), getValueAsString(vatReturn.getExpenseReverceCharge().getTaxAmount()), getValueAsString(vatReturn.getExpenseReverceCharge().getAdjustment()));

        A3 = A3.add(vatReturn.getExpenses().getTaxAmount())
                .add(vatReturn.getExpenseReverceCharge().getTaxAmount());
        A4 = A4.add(vatReturn.getExpenses().getAdjustment())
                .add(vatReturn.getExpenseReverceCharge().getAdjustment());

        expenseTable.addRow("10", createDescriptionBox("10"), null, getValueAsString(A3), getValueAsString(A4));
        return expenseTable;
    }

    protected CustomisedITextTable getNetVatTable(VatReturnData vatReturnData) {
        CustomisedITextTable netVatTable = new CustomisedITextTable();
        netVatTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT, COLUMN_FORMULA);
        A5 = A5.add(A1).add(A2);
        A6 = A6.add(A3).add(A4);
        A7 = A7.add(A5.subtract(A6));
        netVatTable.addRow("11", createDescriptionBox("11"), getValueAsString(A5), "A5=A1+A2");
        netVatTable.addRow("12", createDescriptionBox("12"), getValueAsString(A6), "A6=A3+A4");
        netVatTable.addRow("13", createDescriptionBox("13"), getValueAsString(A7), "A7=A5-A6");
        netVatTable.addRow("14", createDescriptionBox("14"), null, null);

        return netVatTable;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.UAE_VAT_RETURN;
    }

    private String createDescriptionBox(String rowNumber) {
        String baseMessageTitle = "box" + rowNumber + "Title";
        String baseMessageDescription = "box" + rowNumber + "Description";
        return accountingLocalizer.localize(baseMessageTitle) + "<span>" + accountingLocalizer.localize(baseMessageDescription) + "</span>";
    }
}
