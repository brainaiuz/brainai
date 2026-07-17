package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.vatreturn;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UKVatReturnPDFHandler extends GccVatReturnPDFHandler {
    @Override
    protected CustomisedITextTable getVatSalesTable(VatReturnData vatReturnData) {
        UKVatReturn vatReturn = (UKVatReturn) vatReturnData;
        CustomisedITextTable salesTable = new CustomisedITextTable();

        salesTable.addColumnOrder(COLUMN_CODE, COLUMN_DESCRIPTION, COLUMN_BOX, COLUMN_ADJUSTMENTS);

        HashMap<VatReturnBox, String> boxes = new HashMap<>();
        for (Map.Entry<VatReturnBox, BigDecimal> entry : vatReturn.getValuesMap().entrySet()) {
            boxes.put(entry.getKey(), getValueAsString(entry.getValue()));
        }


        salesTable.addRow("1", "VAT due on sales and other outputs", "Box 1", boxes.get(VatReturnBox.BOX_1));
        salesTable.addRow("2", "VAT due from you (but not paid) on acquisitions of goods made in Northern Ireland from EU Member States", "Box 2", boxes.get(VatReturnBox.BOX_2));
        salesTable.addRow("3", "Total VAT due", "Box 3", boxes.get(VatReturnBox.BOX_3));
        salesTable.addRow("4", "VAT reclaimed in the period on purchases and other inputs (including acquisitions in Northern Ireland from EU member states)", "Box 4", boxes.get(VatReturnBox.BOX_4));
        salesTable.addRow("5", "VAT payable or reclaimable", "Box 5", boxes.get(VatReturnBox.BOX_5));
        salesTable.addRow("6", "Your total sales excluding VAT", "Box 6", boxes.get(VatReturnBox.BOX_6));
        salesTable.addRow("7", "Your total purchases excluding VAT", "Box 7", boxes.get(VatReturnBox.BOX_7));
        salesTable.addRow("8", "Total value of dispatches of goods and related costs (excluding VAT) from Northern Ireland to EU Member States", "Box 8", boxes.get(VatReturnBox.BOX_8));
        salesTable.addRow("9", "Total value of acquisitions of goods and related costs (excluding VAT) made in Northern Ireland from EU Member States", "Box 9", boxes.get(VatReturnBox.BOX_9));

        return salesTable;
    }

    @Override
    protected CustomisedITextTable getVatExpenseTable(VatReturnData vatReturnData) {
        return new CustomisedITextTable();
    }

    @Override
    protected CustomisedITextTable getNetVatTable(VatReturnData vatReturnData) {
        return new CustomisedITextTable();
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.UK_VAT_RETURN;
    }
}
