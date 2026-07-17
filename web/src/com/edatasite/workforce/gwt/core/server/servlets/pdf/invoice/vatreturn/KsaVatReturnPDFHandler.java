package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.ksa.KsaVatReturn;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;

import java.math.BigDecimal;

public class KsaVatReturnPDFHandler extends GccVatReturnPDFHandler {

    @Override
    protected CustomisedITextTable getVatSalesTable(VatReturnData vatReturnData) {
        KsaVatReturn ksaVatReturn = (KsaVatReturn) vatReturnData;
        CustomisedITextTable salesTable = new CustomisedITextTable();

        salesTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT, COLUMN_ADJUSTMENTS, COLUMN_TAX_AMOUNT);
        salesTable.addRow("1", createDescriptionBox("1"), getValueAsString(ksaVatReturn.getStandardRateSales().getTaxableAmount()), getValueAsString(ksaVatReturn.getStandardRateSales().getAdjustment()), getValueAsString(ksaVatReturn.getStandardRateSales().getTaxAmount()));
        salesTable.addRow("2", createDescriptionBox("2"), getValueAsString(ksaVatReturn.getOutOfScope().getTaxableAmount()), getValueAsString(ksaVatReturn.getOutOfScope().getAdjustment()), getValueAsString(ksaVatReturn.getOutOfScope().getTaxAmount()));
        salesTable.addRow("3", createDescriptionBox("3"), getValueAsString(ksaVatReturn.getZeroRateSales().getTaxableAmount()), getValueAsString(ksaVatReturn.getZeroRateSales().getAdjustment()), getValueAsString(ksaVatReturn.getZeroRateSales().getTaxAmount()));
        salesTable.addRow("4", createDescriptionBox("4"), getValueAsString(ksaVatReturn.getExports().getTaxableAmount()), getValueAsString(ksaVatReturn.getExports().getAdjustment()), getValueAsString(ksaVatReturn.getExports().getTaxAmount()));
        salesTable.addRow("5", createDescriptionBox("5"), getValueAsString(ksaVatReturn.getExemptSales().getTaxableAmount()), getValueAsString(ksaVatReturn.getExemptSales().getAdjustment()), getValueAsString(ksaVatReturn.getExemptSales().getTaxAmount()));

        //Total Sales
        salesTable.addRow("6", createDescriptionBox("7"), getValueAsString(ksaVatReturn.getSalesTotal().getTaxableAmount()), getValueAsString(ksaVatReturn.getSalesTotal().getAdjustment()), getValueAsString(ksaVatReturn.getSalesTotal().getTaxAmount()));
        return salesTable;
    }

    @Override
    protected CustomisedITextTable getVatExpenseTable(VatReturnData vatReturnData) {
        KsaVatReturn ksaVatReturn = (KsaVatReturn) vatReturnData;
        CustomisedITextTable expenseTable = new CustomisedITextTable();

        expenseTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT, COLUMN_ADJUSTMENTS, COLUMN_TAX_AMOUNT);
        expenseTable.addRow("7", createDescriptionBox("7"), getValueAsString(ksaVatReturn.getStandardRatePurchase().getTaxableAmount()), getValueAsString(ksaVatReturn.getStandardRatePurchase().getAdjustment()), getValueAsString(ksaVatReturn.getStandardRatePurchase().getTaxAmount()));
        expenseTable.addRow("8", createDescriptionBox("8"), getValueAsString(ksaVatReturn.getImportsSubjectPaidAtCustom().getTaxableAmount()), getValueAsString(ksaVatReturn.getImportsSubjectPaidAtCustom().getAdjustment()), getValueAsString(ksaVatReturn.getImportsSubjectPaidAtCustom().getTaxAmount()));
        expenseTable.addRow("9", createDescriptionBox("9"), getValueAsString(ksaVatReturn.getImportsSubjectAccountedReverseCharge().getTaxableAmount()), getValueAsString(ksaVatReturn.getImportsSubjectAccountedReverseCharge().getAdjustment()), getValueAsString(ksaVatReturn.getImportsSubjectAccountedReverseCharge().getTaxAmount()));
        expenseTable.addRow("10", createDescriptionBox("10"), getValueAsString(ksaVatReturn.getZeroRatePurchase().getTaxableAmount()), getValueAsString(ksaVatReturn.getZeroRatePurchase().getAdjustment()), getValueAsString(ksaVatReturn.getZeroRatePurchase().getTaxAmount()));
        expenseTable.addRow("11", createDescriptionBox("11"), getValueAsString(ksaVatReturn.getExemptPurchase().getTaxableAmount()), getValueAsString(ksaVatReturn.getExemptPurchase().getAdjustment()), getValueAsString(ksaVatReturn.getExemptPurchase().getTaxAmount()));

        //Total Expense
        expenseTable.addRow("12", createDescriptionBox("12"), getValueAsString(ksaVatReturn.getPurchaseTotal().getTaxableAmount()), getValueAsString(ksaVatReturn.getPurchaseTotal().getAdjustment()), getValueAsString(ksaVatReturn.getPurchaseTotal().getTaxAmount()));

        return expenseTable;
    }

    @Override
    protected CustomisedITextTable getNetVatTable(VatReturnData vatReturnData) {
        KsaVatReturn ksaVatReturn = (KsaVatReturn) vatReturnData;
        CustomisedITextTable netVatTable = new CustomisedITextTable();
        netVatTable.addColumnOrder(COLUMN_BOX, COLUMN_DESCRIPTION, COLUMN_TAXABLE_AMOUNT);

        BigDecimal outVatAmount = ksaVatReturn.getSalesTotal().getTaxAmount();
        BigDecimal inVatAmount = ksaVatReturn.getPurchaseTotal().getTaxAmount();
        BigDecimal vatAmount = outVatAmount.subtract(inVatAmount);
        netVatTable.addRow("13", createDescriptionBox("13"), getValueAsString(vatAmount));
        netVatTable.addRow("14", createDescriptionBox("14"), getValueAsString(BigDecimal.ZERO));
        netVatTable.addRow("15", createDescriptionBox("15"), getValueAsString(BigDecimal.ZERO));
        netVatTable.addRow("16", createDescriptionBox("16"), getValueAsString(vatAmount));
        return netVatTable;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.KSA_VAT_RETURN;
    }

    private String createDescriptionBox(String rowNumber) {
        String baseMessageTitle = "saBox" + rowNumber + "Title";
        String baseMessageDescription = "saBox" + rowNumber + "Description";
        return accountingLocalizer.localize(baseMessageTitle) + "<span>" + accountingLocalizer.localize(baseMessageDescription) + "</span>";
    }
}
