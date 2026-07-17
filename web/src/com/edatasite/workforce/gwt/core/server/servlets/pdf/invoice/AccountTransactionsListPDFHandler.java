package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionsBetweenDatesInAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/29/11
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountTransactionsListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private BankAccountManager bankAccountManager;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.accountTransactions);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ListPanelToolRpc listPanelTools = filterParametrs.getListPanelTool();
        Date startDate = parseFilterParameterDate(filterParametrs.getStartDateNC());
        Date endDate = parseFilterParameterDate(filterParametrs.getEndDateNC());
        TransactionsBetweenDatesInAccount result = accountingService.findTransactionsByAccountAndJournalDate(filterParametrs,
                (startDate != null ? new DateNonConvertable(startDate) : null),
                (endDate != null ? new DateNonConvertable(endDate) : null));

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.LISTTABLE);

        String[] headers = getTableHeaders(filterParametrs, listPanelTools);

        ITextTableList table = new ITextTableList(headers.length);
        pdfData.setListTable(table);

        table.addPdfTableHeader(headers);

        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";

        for (Transaction trans : result.getTransactions()) {
            CellData status;
            if (RECONCILED.equals(trans.getReconcileStatus())) {
                status = new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.reconciled));
            } else if (MARKED_AS_RECONCILED.equals(trans.getReconcileStatus())) {
                status = new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.markedAsReconciled));
            } else {
                status = new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.notReconciled));
            }
            CellData date = new CellData(ServerUtils.dateFormat(trans.getJournalDate().getNonConvertedDate(), shortDateFormat));
            CellData description = new CellData(trans.getJournalName());
            CellData bankTransferDescription = new CellData(trans.getDescription());
            CellData reference = new CellData(trans.getReference() != null ? trans.getReference() : "");
            CellData spent = new CellData(trans.getTotalCredit() != null ? getMoneyFormat(trans.getTotalCredit()) : "");
            CellData received = new CellData(trans.getTotalDebit() != null ? getMoneyFormat(trans.getTotalDebit()) : "");
            spent.setAlignment(Element.ALIGN_RIGHT);
            received.setAlignment(Element.ALIGN_RIGHT);
            HashMap<String, CellData> columnMap = new HashMap<>();
            if (listPanelTools.getColumnCodeName().contains(STATUS_COLUMN)) {
                columnMap.put(STATUS_COLUMN, status);
            }
            if (listPanelTools.getColumnCodeName().contains(DATE_COLUMN)) {
                columnMap.put(DATE_COLUMN, date);
            }
            if (listPanelTools.getColumnCodeName().contains(BANK_TRANSFER_DESCRIPTION_COLUMN)) {
                columnMap.put(BANK_TRANSFER_DESCRIPTION_COLUMN, bankTransferDescription);
            }
            if (listPanelTools.getColumnCodeName().contains(DESCRIPTION_COLUMN)) {
                columnMap.put(DESCRIPTION_COLUMN, description);
            }
            if (listPanelTools.getColumnCodeName().contains(REFERENCE_COLUMN)) {
                columnMap.put(REFERENCE_COLUMN, reference);
            }
            if (listPanelTools.getColumnCodeName().contains(SPENT_COLUMN)) {
                columnMap.put(SPENT_COLUMN, spent);
            }
            if (listPanelTools.getColumnCodeName().contains(RECEIVED_COLUMN)) {
                columnMap.put(RECEIVED_COLUMN, received);
            }
            if (listPanelTools.getColumnCodeName().contains(NUMBER_COLUMN)) {
                columnMap.put(NUMBER_COLUMN, new CellData(trans.getSpendReceiveMoneyNumber() != null ? trans.getSpendReceiveMoneyNumber() : ""));
            }

            ArrayList<CellData> columns = new ArrayList<>();
            for (int i = 0; i < listPanelTools.getColumnCodeName().size(); i++) {
                if (columnMap.containsKey(listPanelTools.getColumnCodeName().get(i))) {
                    columns.add(columnMap.get(listPanelTools.getColumnCodeName().get(i)));
                }
            }

            CellData[] datas = new CellData[columns.size()];
            columns.toArray(datas);
            table.addPdfTableRows(datas);
        }
        return pdfData;
    }

    String[] getTableHeaders(ListingFilterParameter filterParameter, ListPanelToolRpc panelTools) {
        HashMap<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(STATUS_COLUMN, commonLocalizer.localizeAccounting(PdfLocalizationName.status));
        mapColumnHeader.put(DATE_COLUMN, commonLocalizer.localizeAccounting(PdfLocalizationName.date));
        mapColumnHeader.put(BANK_TRANSFER_DESCRIPTION_COLUMN, accountingLocalizer.localizeAccounting(PdfLocalizationName.bankTransferDesc));
        mapColumnHeader.put(DESCRIPTION_COLUMN, commonLocalizer.localizeAccounting(PdfLocalizationName.description));
        mapColumnHeader.put(REFERENCE_COLUMN, accountingLocalizer.localizeAccounting(PdfLocalizationName.referenceNumber));
        mapColumnHeader.put(SPENT_COLUMN, accountingLocalizer.localizeAccounting(PdfLocalizationName.spent));
        mapColumnHeader.put(RECEIVED_COLUMN, accountingLocalizer.localizeAccounting(PdfLocalizationName.received));
        mapColumnHeader.put(NUMBER_COLUMN, commonLocalizer.localizeAccounting(PdfLocalizationName.number));

        ArrayList header = new ArrayList<String>();
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                header.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
            }
        }
        return (String[]) header.toArray(new String[0]);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_BankAccountTransactionsList_" + ServerUtils.dateFormat(user.getUserDate(), shortDateFormat));
    }

}
