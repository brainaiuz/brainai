package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.08.2009
 * Time: 11:37:42
 * To change this template use File | Settings | File Templates.
 */
public class ManualTransactionListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Autowired
    private ManualEntryServiceLocal manualEntryServiceLocal;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("manualEntries");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();

        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";
        filterParametrs.setFromExcelPDF(true);
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(1000);
        }

        ListResult<ManualJournalListItem> manualTransactionList = manualEntryServiceLocal.getManualTransactions(filterParametrs);
        List<ManualJournalListItem> manualJournalListItems = manualTransactionList.getList();
        List<ManualJournalListItem> subList = ListUtils.getSublist(manualJournalListItems, filterParametrs.getStart(), filterParametrs.getLimit());
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(ManualJournalListItem.ACTION);

        Set<String> columnsForExport = new HashSet<>();
        for (ManualJournalListItem items : subList){
            if(items.getDate() != null && header.contains(ManualJournalListItem.DATE)) columnsForExport.add(ManualJournalListItem.DATE);
            if(items.getStatus() != null && !"".equals(items.getStatus()) && header.contains(ManualJournalListItem.STATUS)) columnsForExport.add(ManualJournalListItem.STATUS);
            if(items.getReferenceNumber() != null && !"".equals(items.getReferenceNumber()) && header.contains(ManualJournalListItem.REFERENCENUMBER)) {columnsForExport.add(ManualJournalListItem.REFERENCENUMBER);}
            if(items.getNarration() != null && !"".equals(items.getNarration()) && header.contains(ManualJournalListItem.NARRATION)) columnsForExport.add(ManualJournalListItem.NARRATION);
            if(items.getDebit() != null && header.contains(ManualJournalListItem.DEBIT)) columnsForExport.add(ManualJournalListItem.DEBIT);
            if(items.getCredit() != null && header.contains(ManualJournalListItem.CRETID)) columnsForExport.add(ManualJournalListItem.CRETID);
            if(header.contains(ManualJournalListItem.TYPE)) columnsForExport.add(ManualJournalListItem.TYPE);
            if(items.getRepeats() != null && !"".equals(items.getRepeats()) && header.contains(ManualJournalListItem.REPEATS)) columnsForExport.add(ManualJournalListItem.REPEATS);
            if(items.getEndDate() != null && header.contains(ManualJournalListItem.ENDDATE)) columnsForExport.add(ManualJournalListItem.ENDDATE);
            if(items.getNumber() != null && !"".equals(items.getNumber()) && header.contains(ManualJournalListItem.NUMBER)) columnsForExport.add(ManualJournalListItem.NUMBER);
            if (items.getCurrency() != null && !"".equals(items.getCurrency()) && header.contains(ManualJournalListItem.CURRENCY))
                columnsForExport.add(ManualJournalListItem.CURRENCY);
            if (items.getCreator() != null && !"".equals(items.getCreator()) && header.contains(ManualJournalListItem.CREATOR))
                columnsForExport.add(ManualJournalListItem.CREATOR);
            if (header.contains(ManualJournalListItem.PROJECT))
                columnsForExport.add(ManualJournalListItem.PROJECT);
            if (header.contains(ManualJournalListItem.APPROVER))
                columnsForExport.add(ManualJournalListItem.APPROVER);
        }

        ITextTableList tableList = new ITextTableList(columnsForExport.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ManualJournalListItem.NARRATION, new CellData(accountingLocalizer.localize(PdfLocalizationName.narration), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.DEBIT, new CellData(accountingLocalizer.localize(PdfLocalizationName.debit), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ManualJournalListItem.CRETID, new CellData(accountingLocalizer.localize(PdfLocalizationName.credit), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ManualJournalListItem.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.REFERENCENUMBER, new CellData(accountingLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.TYPE, new CellData(accountingLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.REPEATS, new CellData(accountingLocalizer.localize(PdfLocalizationName.repeats), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.ENDDATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.endDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.NUMBER, new CellData(accountingLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.CURRENCY, new CellData(accountingLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.PROJECT, new CellData(accountingLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(ManualJournalListItem.APPROVER, new CellData(accountingLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        List<String> header2Keys = new ArrayList<>();
        for (String aHeader : header) {
            if (columnsForExport.contains(aHeader)) {
                header2.add(mapColumnHeader.get(aHeader));
                header2Keys.add(aHeader);
            }
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (ManualJournalListItem items : subList) {
            String[] temp = new String[columnsForExport.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < columnsForExport.size(); j++) {
                if (ManualJournalListItem.NARRATION.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getNarration());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.DATE.equals(header2Keys.get(j))) {
                    String date = "—";
                    if (items.getDate() != null) {
                        date = ServerUtils.dateFormat(items.getDate().getNonConvertedDate(), shortDateFormat);
                    }
                    temp[j] = date;
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.DEBIT.equals(header2Keys.get(j))) {
                    String debit = "0.00";
                    if (items.getDebit().compareTo(ZERO) != 0) {
                        debit = priceScaleNumberFormat.format(items.getDebit());
                        debit = debit.substring(debit.indexOf("$") + 1);
                        if (debit.indexOf(")") > 0) {
                            debit = debit.substring(0, debit.indexOf(")"));
                            if (!debit.equals("0.00")) {
                                debit = "-" + debit;
                            }
                        }
                    }
                    temp[j] = debit;
                    cell.add(new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (ManualJournalListItem.CRETID.equals(header2Keys.get(j))) {
                    String credit = "0.00";
                    if (items.getCredit().compareTo(ZERO) != 0) {
                        credit = priceScaleNumberFormat.format(items.getCredit());
                        credit = credit.substring(credit.indexOf("$") + 1);
                        if (credit.indexOf(")") > 0) {
                            credit = credit.substring(0, credit.indexOf(")"));
                            if (!credit.equals("0.00")) {
                                credit = "-" + credit;
                            }
                        }
                    }
                    temp[j] = credit;
                    cell.add(new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (ManualJournalListItem.STATUS.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getStatus());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if(ManualJournalListItem.REFERENCENUMBER.equals(header2Keys.get(j))){
                    temp[j] = getResultOrLongDash(items.getReferenceNumber());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if(ManualJournalListItem.TYPE.equals(header2Keys.get(j))){
                    temp[j] = items.isRecurringTemplate() ? accountingLocalizer.localizeAccounting(PdfLocalizationName.recurring) : accountingLocalizer.localizeAccounting(PdfLocalizationName.single);
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.CURRENCY.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getCurrency());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.CREATOR.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getCreator());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if(ManualJournalListItem.REPEATS.equals(header2Keys.get(j))){
                    temp[j] = getResultOrLongDash(items.getRepeats());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if(ManualJournalListItem.ENDDATE.equals(header2Keys.get(j))){
                    String date = "—";
                    if (items.getNextCreationDate() != null) {
                        date = ServerUtils.dateFormat(items.getEndDate(), shortDateFormat);
                    }
                    temp[j] = date;
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if(ManualJournalListItem.NUMBER.equals(header2Keys.get(j))){
                    temp[j] = getResultOrLongDash(items.getNumber());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.PROJECT.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getProject());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ManualJournalListItem.APPROVER.equals(header2Keys.get(j))) {
                    temp[j] = getResultOrLongDash(items.getCurrentApprover());
                    cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[columnsForExport.size()]));
        }
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ManualTransactionList_" + dateFormat(user.getUserDate()));
    }

}
