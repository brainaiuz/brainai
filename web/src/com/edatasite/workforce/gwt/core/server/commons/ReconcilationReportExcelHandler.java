package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankReconcilationReportData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.util.LinkedList;
import java.util.List;


public class ReconcilationReportExcelHandler extends BaseExcelHandler implements Constants {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    private static final Logger log = LoggerFactory.getLogger(SaleQuotesListExcelHandler.class);

    @Autowired
    private AccountingService accountingService;

    @Override
    protected void setFileName() {
        filename = "RECONCILIATION_REPORT";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
//        List<BankReconcilationReportData> reportData = Collections.singletonList(accountingService.getBankAccountReconcilationReport(fp.getStartDateNC(), fp.getEndDateNC(), fp.getObjectId()));
        BankReconcilationReportData report = accountingService.getBankAccountReconcilationReport(fp.getStartDateNC(), fp.getEndDateNC(), fp.getObjectId());
        fp.setLimit(LIMIT_EXCEL_ROW);

        List<ExcelData[]> list = new LinkedList<>();
        ExcelData[] cellDatas = new ExcelData[4];
        cellDatas[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.date), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        cellDatas[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        cellDatas[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.reference), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        cellDatas[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
        list.add(cellDatas);

//        for (BankReconcilationReportData data : reportData){
//            cellDatas = new ExcelData[4];
//            cellDatas[0] = new ExcelData(data.getReconcileBalance(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
//            cellDatas[1] = new ExcelData(data.getReconcileBalance(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
//            cellDatas[2] = new ExcelData(data.getReconcileBalance(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
//            cellDatas[3] = new ExcelData(data.getReconcileBalance(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
//            list.add(cellDatas);
//        }

        for (int i = 0; i < report.getOutstandingPayments().length; i++) {
            cellDatas = new ExcelData[5];
            cellDatas[0] = new ExcelData(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.plusOutstandingPayments)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[1] = new ExcelData(StringUtils.defaultString(report.getOutstandingPayments()[i].getDate().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[2] = new ExcelData(StringUtils.defaultString(report.getOutstandingPayments()[i].getDescription()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[3] = new ExcelData(StringUtils.defaultString(report.getOutstandingPayments()[i].getReference()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[4] = new ExcelData(StringUtils.defaultString(report.getOutstandingPayments()[i].getAmount().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(cellDatas);
        }

        for (int i = 0; i < report.getOutstandingReceipts().length; i++) {
            cellDatas = new ExcelData[5];
            cellDatas[0] = new ExcelData(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.lessOutstandingReceipts)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[1] = new ExcelData(StringUtils.defaultString(report.getOutstandingReceipts()[i].getDate().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[2] = new ExcelData(StringUtils.defaultString(report.getOutstandingReceipts()[i].getDescription()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[3] = new ExcelData(StringUtils.defaultString(report.getOutstandingReceipts()[i].getReference()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[4] = new ExcelData(StringUtils.defaultString(report.getOutstandingReceipts()[i].getAmount().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(cellDatas);
        }

        for (int i = 0; i < report.getUnReconciledBankStatementLines().length; i++) {
            cellDatas = new ExcelData[5];
            cellDatas[0] = new ExcelData(StringUtils.defaultString(commonLocalizer.localize(PdfLocalizationName.plusUnRecBankStatLines)), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[1] = new ExcelData(StringUtils.defaultString(report.getUnReconciledBankStatementLines()[i].getDate().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[2] = new ExcelData(StringUtils.defaultString(report.getUnReconciledBankStatementLines()[i].getDescription()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[3] = new ExcelData(StringUtils.defaultString(report.getUnReconciledBankStatementLines()[i].getReference()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[4] = new ExcelData(StringUtils.defaultString(report.getUnReconciledBankStatementLines()[i].getAmount().toString()), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            list.add(cellDatas);
        }


        WorkBook workBook = new WorkBook(list, false, 0, 1, 0, 1);

        return workBook.getWorkBook(filename, 0, 0, 0, 5);
    }
}
