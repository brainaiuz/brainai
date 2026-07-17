package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/29/11
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountTransactionsListExcelHandler extends BaseExcelHandler implements AccountingConstants {

    private static final Logger log = LoggerFactory.getLogger(AccountTransactionsListExcelHandler.class);

    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private AccountingService accountingService;


    HashMap<String, ExcelData> mapColumnHeader = new HashMap<>();

    @Override
    protected void setFileName() {
        EdsUser user = bankAccountManager.getUser();
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";
        if (user != null) {
            filename = user.getFirstName() + "_" + user.getLastName() + "_AccountTransactionsList_" + ServerUtils.dateFormat(user.getUserDate(), shortDateFormat);
        } else {
            filename = "AccountTransactionsList";
        }
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        TransactionsBetweenDatesInAccount result = accountingService.findTransactionsByAccountAndJournalDate(filterParametrs,
                (startDate != null ? new DateNonConvertable(startDate) : null),
                (endDate != null ? new DateNonConvertable(endDate) : null));

        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        try {
            LinkedList<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(STATUS_COLUMN, new ExcelData(commonLocalizer.localizeAccounting(PdfLocalizationName.status), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(DATE_COLUMN, new ExcelData(commonLocalizer.localizeAccounting(PdfLocalizationName.date), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(BANK_TRANSFER_DESCRIPTION_COLUMN, new ExcelData(accountingLocalizer.localizeAccounting(PdfLocalizationName.bankTransferDesc), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(DESCRIPTION_COLUMN, new ExcelData(commonLocalizer.localizeAccounting(PdfLocalizationName.description), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(REFERENCE_COLUMN, new ExcelData(accountingLocalizer.localizeAccounting(PdfLocalizationName.referenceNumber), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(SPENT_COLUMN, new ExcelData(accountingLocalizer.localizeAccounting(PdfLocalizationName.spent), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(RECEIVED_COLUMN, new ExcelData(accountingLocalizer.localizeAccounting(PdfLocalizationName.received), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(NUMBER_COLUMN, new ExcelData(commonLocalizer.localizeAccounting(PdfLocalizationName.number), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            ArrayList<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);

            list.add(cellDatas);
            for (Transaction trans : result.getTransactions()) {
                String status;
                if (Constants.RECONCILED.equals(trans.getReconcileStatus())) {
                    status = accountingLocalizer.localizeAccounting(PdfLocalizationName.reconciled);
                } else if (Constants.MARKED_AS_RECONCILED.equals(trans.getReconcileStatus())) {
                    status = accountingLocalizer.localizeAccounting(PdfLocalizationName.markedAsReconciled);
                } else {
                    status = accountingLocalizer.localizeAccounting(PdfLocalizationName.notReconciled);
                }
                String date = ServerUtils.dateFormat(trans.getJournalDate().getNonConvertedDate(), shortDateFormat);
                String description = trans.getJournalName();
                String reference = trans.getReference() != null ? trans.getReference() : "";
                String bankTransferDescription = trans.getDescription();
                ExcelData spentData, receivedData;
                if (trans.getTotalCredit() != null) {
                    spentData = new ExcelData(trans.getTotalCredit(), ExcelData.BIG_DECIMAL, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                } else {
                    spentData = new ExcelData("", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                if (trans.getTotalDebit() != null) {
                    receivedData = new ExcelData(trans.getTotalDebit(), ExcelData.BIG_DECIMAL, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                } else {
                    receivedData = new ExcelData("", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                HashMap<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(STATUS_COLUMN)) {
                    mapColumn.put(STATUS_COLUMN, new ExcelData(status, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(DATE_COLUMN)) {
                    mapColumn.put(DATE_COLUMN, new ExcelData(date, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(BANK_TRANSFER_DESCRIPTION_COLUMN)) {
                    mapColumn.put(BANK_TRANSFER_DESCRIPTION_COLUMN, new ExcelData(bankTransferDescription, ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(DESCRIPTION_COLUMN)) {
                    mapColumn.put(DESCRIPTION_COLUMN, new ExcelData(description, ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(REFERENCE_COLUMN)) {
                    mapColumn.put(REFERENCE_COLUMN, new ExcelData(reference, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(SPENT_COLUMN)) {
                    mapColumn.put(SPENT_COLUMN, spentData);
                }
                if (panelTools.getColumnCodeName().contains(RECEIVED_COLUMN)) {
                    mapColumn.put(RECEIVED_COLUMN, receivedData);
                }
                if (panelTools.getColumnCodeName().contains(NUMBER_COLUMN)) {
                    mapColumn.put(NUMBER_COLUMN, new ExcelData(trans.getSpendReceiveMoneyNumber() != null ? trans.getSpendReceiveMoneyNumber() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate bank account transactions list excel report, exception: " + e);
        }
        return null;
    }

}
