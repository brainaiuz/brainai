package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk.UKVatReturn;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class UKVatReturnExcelHandler extends BaseExcelHandler {
    @Autowired
    private VatReturnService vatReturnService;

    public UKVatReturnExcelHandler() {
    }

    private final int A_CELL_SIZE = 7;

    @Override
    protected void setFileName() {
        super.filename = "VAT_RETURN_" + dateFormat(userManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        LinkedList<ExcelData[]> excelData = new LinkedList<>();

        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();

        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        VatReturnItem vatReturnItem = vatReturnService.getVatReturn(filterParameter.getObjectId());
        UKVatReturn ukVatReturn = vatReturnService.generateVatReturn(filterParameter.getObjectId());


        int LAST_COLUMN_INDEX = 4;
        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), A_CELL_SIZE, LAST_COLUMN_INDEX);
        companyData.setFontSize(20);
        excelData.add(new ExcelData[]{companyData});
        ExcelData excelTitle = ExcelData.getReportNameData(accountingLocalizer.localizeAccounting(PdfLocalizationName.vatReturn), A_CELL_SIZE, LAST_COLUMN_INDEX);
        excelTitle.setFontSize(18);
        excelData.add(new ExcelData[]{excelTitle});
        if (vatReturnItem != null) {
            ExcelData dateData = ExcelData.getReportNameChildData(accountingLocalizer.localizeAccounting(PdfLocalizationName.from) + " "
                    + Utils.formatDate(vatReturnItem.getFromDate().getNonConvertedDate(), company) + " " + accountingLocalizer.localizeAccounting(PdfLocalizationName.to) + " "
                    + Utils.formatDate(vatReturnItem.getToDate().getNonConvertedDate(), company), A_CELL_SIZE, LAST_COLUMN_INDEX);
            dateData.setFontSize(14);
            excelData.add(new ExcelData[]{dateData});
        }
        excelData.addAll(getVatSalesTable(ukVatReturn));

        return new WorkBook(excelData).getWorkBook(super.filename, 0, 0, 0, 3);
    }

    private Collection<? extends ExcelData[]> getVatSalesTable(UKVatReturn vatReturnItem) {
        List<ExcelData[]> salesTable = new LinkedList<>(getTableHeader());

        salesTable.add(getRow("1", "VAT due on sales and other outputs", "Box 1", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_1)));
        salesTable.add(getRow("2", "VAT due from you (but not paid) on acquisitions of goods made in Northern Ireland from EU Member States", "Box 2", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_2)));
        salesTable.add(getRow("3", "Total VAT due", "Box 3", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_3)));
        salesTable.add(getRow("4", "VAT reclaimed in the period on purchases and other inputs (including acquisitions in Northern Ireland from EU member states)", "Box 4", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_4)));
        salesTable.add(getRow("5", "VAT payable or reclaimable", "Box 5", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_5)));
        salesTable.add(getRow("6", "Your total sales excluding VAT", "Box 6", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_6)));
        salesTable.add(getRow("7", "Your total purchases excluding VAT", "Box 7", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_7)));
        salesTable.add(getRow("8", "Total value of dispatches of goods and related costs (excluding VAT) from Northern Ireland to EU Member States", "Box 8", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_8)));
        salesTable.add(getRow("9", "Total value of acquisitions of goods and related costs (excluding VAT) made in Northern Ireland from EU Member States", "Box 9", vatReturnItem.getValuesMap().get(VatReturnBox.BOX_9)));
        return salesTable;
    }

    private static ExcelData[] getRow(String number, String description, String boxNumber, BigDecimal amount) {
        ExcelData[] excelDataRow = new ExcelData[4];
        excelDataRow[0] = new ExcelData(number, ExcelData.STRING);
        excelDataRow[1] = new ExcelData(description, ExcelData.STRING);
        excelDataRow[2] = new ExcelData(boxNumber, ExcelData.STRING);
        excelDataRow[3] = new ExcelData(amount.doubleValue(), ExcelData.DOUBLE);
        excelDataRow[0].setFontSize(12);
        excelDataRow[1].setFontSize(12);
        excelDataRow[2].setFontSize(12);
        excelDataRow[3].setFontSize(12);
        return excelDataRow;
    }

    private List<ExcelData[]> getTableHeader() {
        List<ExcelData[]> headerTable = new LinkedList<>();
        headerTable.add(new ExcelData[]{new ExcelData("", ExcelData.STRING, A_CELL_SIZE, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});

        ExcelData boxData = new ExcelData("No", ExcelData.STRING, A_CELL_SIZE, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        int B_CELL_SIZE = 50;
        ExcelData descriptionData = new ExcelData("DESCRIPTION", ExcelData.STRING, B_CELL_SIZE, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        int AMOUNT_CELL_SIZE = 18;
        ExcelData boxNumber = new ExcelData("BOX#", ExcelData.STRING, AMOUNT_CELL_SIZE, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData amount = new ExcelData("AMOUNT", ExcelData.STRING, AMOUNT_CELL_SIZE, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);

        headerTable.add(new ExcelData[]{boxData, descriptionData, boxNumber, amount});
        return headerTable;
    }
}
