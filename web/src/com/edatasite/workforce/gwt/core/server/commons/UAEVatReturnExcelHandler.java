package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.TaxAmountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uea.UaeVatReturn;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class UAEVatReturnExcelHandler extends BaseExcelHandler {

    @Autowired
    private VatReturnService vatReturnService;

    private final int aCellSize = 7;
    private final int bCellSize = 50;
    private final int amountCellSize = 18;
    private final int lastColumnIndex = 5;

    private BigDecimal A1;
    private BigDecimal A2;
    private BigDecimal A3;
    private BigDecimal A4;
    private BigDecimal A5;
    private BigDecimal A6;
    private BigDecimal A7;

    @Override
    protected void setFileName() {
        filename = "VAT_RETURN_" + dateFormat(userManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        VatReturnItem vatReturnItem = vatReturnService.getVatReturn(filterParameter.getObjectId());
        UaeVatReturn uaeVatReturn = vatReturnService.generateVatReturn(filterParameter.getObjectId());
        List<ExcelData[]> list = new LinkedList<>();


        ExcelData titleData = ExcelData.getReportNameData(accountingLocalizer.localizeAccounting(PdfLocalizationName.vatReturn), aCellSize, lastColumnIndex);
        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), aCellSize, lastColumnIndex);

        list.add(new ExcelData[]{new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});
        list.add(new ExcelData[]{titleData});
        list.add(new ExcelData[]{companyData});

        if (vatReturnItem != null) {
            ExcelData dateData = ExcelData.getReportNameChildData(accountingLocalizer.localizeAccounting(PdfLocalizationName.from) + " "
                    + Utils.formatDate(vatReturnItem.getFromDate().getNonConvertedDate(), company) + " " + accountingLocalizer.localizeAccounting(PdfLocalizationName.to) + " "
                    + Utils.formatDate(vatReturnItem.getToDate().getNonConvertedDate(), company), aCellSize, lastColumnIndex);
            list.add(new ExcelData[]{dateData});
        }
        list.addAll(getVatSalesTable(uaeVatReturn));
        list.addAll(getVatExpenseTable(uaeVatReturn));
        list.addAll(getVatNetTable(uaeVatReturn));
        HSSFWorkbook wb = new WorkBook(list).getWorkBook(filename, 0, 0, 0, 3);
        return wb;
    }

    private List<ExcelData[]> getVatSalesTable(UaeVatReturn vatReturn) {

        List<ExcelData[]> salesTable = new LinkedList<>(getTableHeader(PdfLocalizationName.vatOnSales, false));

        salesTable.add(createDataRow("1a", vatReturn.getAbudhabi()));
        salesTable.add(createDataRow("1b", vatReturn.getDubai()));
        salesTable.add(createDataRow("1c", vatReturn.getSharjah()));
        salesTable.add(createDataRow("1d", vatReturn.getAjman()));
        salesTable.add(createDataRow("1e", vatReturn.getUmmAlQuwain()));
        salesTable.add(createDataRow("1f", vatReturn.getRasAlKhalmah()));
        salesTable.add(createDataRow("1g", vatReturn.getFujairah()));
        salesTable.add(createDataRow("2", vatReturn.getReverscharge()));
        salesTable.add(createDataRow("3", vatReturn.getZeroRated()));
        salesTable.add(createDataRow("4", vatReturn.getExempt()));
        salesTable.add(createDataRow("5", vatReturn.getGoodsImported()));
        salesTable.add(createDataRow("6", vatReturn.getAdjustment()));

        A1 = BigDecimal.ZERO;
        A2 = BigDecimal.ZERO;

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

        salesTable.add(createLastDataRow("7", A1, A2));

        return salesTable;
    }

    private List<ExcelData[]> getVatExpenseTable(UaeVatReturn uaeVatReturn) {

        List<ExcelData[]> expenseTable = new LinkedList<>(getTableHeader(PdfLocalizationName.vatOnExpense, false));

        expenseTable.add(createDataRow("8", uaeVatReturn.getExpenses()));
        expenseTable.add(createDataRow("9", uaeVatReturn.getExpenseReverceCharge()));

        A3 = BigDecimal.ZERO;
        A4 = BigDecimal.ZERO;

        A3 = A3.add(uaeVatReturn.getExpenses().getTaxAmount())
                .add(uaeVatReturn.getExpenseReverceCharge().getTaxAmount());

        A4 = A4.add(uaeVatReturn.getExpenseReverceCharge().getAdjustment())
                .add(uaeVatReturn.getExpenseReverceCharge().getAdjustment());

        expenseTable.add(createLastDataRow("10", A3, A4));

        return expenseTable;
    }

    private List<ExcelData[]> getVatNetTable(UaeVatReturn vatReturn) {
        List<ExcelData[]> netTable = new LinkedList<>(getTableHeader(PdfLocalizationName.netVatDue, true));
        A5 = BigDecimal.ZERO;
        A6 = BigDecimal.ZERO;
        A7 = BigDecimal.ZERO;

        A5 = A5.add(A1).add(A2);
        A6 = A6.add(A3).add(A4);
        A7 = A7.add(A5.subtract(A6));
        netTable.add(createLastDataRow("11", A5));
        netTable.add(createLastDataRow("12", A6));
        netTable.add(createLastDataRow("13", A7));
        netTable.add(createLastDataRow("14"));

        return netTable;
    }

    private List<ExcelData[]> getTableHeader(String titleCode, boolean isNetTable) {

        List<ExcelData[]> headerTable = new LinkedList<>();
        headerTable.add(new ExcelData[]{new ExcelData("", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)});
        ExcelData tableTitle = ExcelData.getReportNameData(accountingLocalizer.localize(titleCode), aCellSize, lastColumnIndex);
        tableTitle.setHorizontalAlignment(CellStyle.ALIGN_LEFT);
        headerTable.add(new ExcelData[]{tableTitle});
        ExcelData boxData = new ExcelData("#BOX", ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData descriptionData = new ExcelData("DESCRIPTION", ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData taxableAmountData = new ExcelData("TAXABLE AMOUNT", ExcelData.STRING, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);

        if (isNetTable) {
            headerTable.add(new ExcelData[]{boxData, descriptionData, taxableAmountData});
            return headerTable;
        }
        ExcelData taxAmountData = new ExcelData("TAX AMOUNT", ExcelData.STRING, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData adjustmentsData = new ExcelData("ADJUSTMENTS", ExcelData.STRING, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        headerTable.add(new ExcelData[]{boxData, descriptionData, taxableAmountData, taxAmountData, adjustmentsData});
        return headerTable;
    }

    private ExcelData[] createDataRow(String rowNumber, TaxAmountItem taxAmountItem) {
        ExcelData boxData = new ExcelData(rowNumber, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData descriptionData = new ExcelData(createDescriptionCell(rowNumber), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData taxableAmountData = new ExcelData(taxAmountItem.getTaxableAmount(), ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        ExcelData taxAmountData = new ExcelData(taxAmountItem.getTaxAmount(), ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        ExcelData adjustmentsData = new ExcelData(taxAmountItem.getAdjustment(), ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        return new ExcelData[]{boxData, descriptionData, taxableAmountData, taxAmountData, adjustmentsData};
    }

    private ExcelData[] createLastDataRow(String rowNumber, BigDecimal... addition) {
        ExcelData boxData = new ExcelData(rowNumber, ExcelData.STRING, aCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData descriptionData = new ExcelData(createDescriptionCell(rowNumber), ExcelData.STRING, bCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);

        if (addition.length < 2) {
            ExcelData taxableAmountData;
            if (addition.length > 0) {
                taxableAmountData = new ExcelData(addition[0], ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
            } else {
                taxableAmountData = new ExcelData("", ExcelData.STRING, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
            }
            return new ExcelData[]{boxData, descriptionData, taxableAmountData};
        }
        ExcelData taxableAmountData = new ExcelData("", ExcelData.STRING, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        ExcelData taxAmountData = new ExcelData(addition[0], ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        ExcelData adjustmentsData = new ExcelData(addition[1], ExcelData.BIG_DECIMAL, amountCellSize, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        return new ExcelData[]{boxData, descriptionData, taxableAmountData, taxAmountData, adjustmentsData};
    }

    private String createDescriptionCell(String rowNumber) {
        String baseMessageTitle = "box" + rowNumber + "Title";
        String baseMessageDescription = "box" + rowNumber + "Description";
        return accountingLocalizer.localize(baseMessageTitle) + "\n\n" + accountingLocalizer.localize(baseMessageDescription);
    }
}
