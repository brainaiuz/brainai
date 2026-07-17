package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.07.2009
 * Time: 19:33:50
 * To change this template use File | Settings | File Templates.
 */
public class ManualTransactionListExcelHandler extends BaseExcelHandler implements AccountingConstants {
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    private static final Logger log = LoggerFactory.getLogger(ManualTransactionListExcelHandler.class);
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private ManualEntryServiceLocal manualEntryServiceLocal;
    @Autowired
    private UserManager userManager;
    private String sheetName;

    @Override
    protected void setFileName() {
        filename = "Manual Entries";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String shortDateFormat = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "MMM dd, yyyy";

        filterParametrs.setFromExcelPDF(true);

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }


        ListResult<ManualJournalListItem> manualTransactionList = manualEntryServiceLocal.getManualTransactions(filterParametrs);
        List<ManualJournalListItem> manualJournalListItems = manualTransactionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header2 = new ArrayList<>();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(ManualJournalListItem.ACTION);

        Set<String> columnsForExport = new HashSet<>();
        for (ManualJournalListItem items : manualJournalListItems){
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

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ManualJournalListItem.NARRATION, commonLocalizer.localize(PdfLocalizationName.narration));
        mapColumnHeader.put(ManualJournalListItem.DATE, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(ManualJournalListItem.DEBIT, commonLocalizer.localize(PdfLocalizationName.debit));
        mapColumnHeader.put(ManualJournalListItem.CRETID, commonLocalizer.localize(PdfLocalizationName.credit));
        mapColumnHeader.put(ManualJournalListItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(ManualJournalListItem.REFERENCENUMBER, commonLocalizer.localize(PdfLocalizationName.referenceNumber));
        mapColumnHeader.put(ManualJournalListItem.TYPE, commonLocalizer.localize(PdfLocalizationName.type));
        mapColumnHeader.put(ManualJournalListItem.REPEATS, commonLocalizer.localize(PdfLocalizationName.repeats));
        mapColumnHeader.put(ManualJournalListItem.ENDDATE, commonLocalizer.localize(PdfLocalizationName.endDate));
        mapColumnHeader.put(ManualJournalListItem.NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(ManualJournalListItem.CURRENCY, commonLocalizer.localize(PdfLocalizationName.currency));
        mapColumnHeader.put(ManualJournalListItem.CREATOR, commonLocalizer.localize(PdfLocalizationName.createdBy));
        mapColumnHeader.put(ManualJournalListItem.PROJECT, commonLocalizer.localize(PdfLocalizationName.project));
        mapColumnHeader.put(ManualJournalListItem.APPROVER, commonLocalizer.localize(PdfLocalizationName.approver));
        final List<String> header2Keys = new ArrayList<>();
        for (String aHeader : header) {
            if (columnsForExport.contains(aHeader)) {
                header2.add(mapColumnHeader.get(aHeader));
                header2Keys.add(aHeader);
            }
        }

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.manualEntries);
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[columnsForExport.size()];

            list.add(generateOneRowWithValue(columnsForExport.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(columnsForExport.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(columnsForExport.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));


            for (int i = 0; i < columnsForExport.size(); i++) {
                if (columnsForExport.contains(header2Keys.get(i))) {
                    cellDatas[i] = new ExcelData(
                            mapColumnHeader.get(header2Keys.get(i)),
                            ExcelData.STRING,
                            header2Keys.get(i).equals(ManualJournalListItem.NARRATION) || header2Keys.get(i).equals(ManualJournalListItem.DATE) ? 50 : 20,
                            false,
                            header2Keys.get(i).equals(ManualJournalListItem.NARRATION) || header2Keys.get(i).equals(ManualJournalListItem.DATE) || header2Keys.get(i).equals(ManualJournalListItem.DEBIT),
                            ExcelData.NO_BORDER,
                            ExcelData.HEADER);
                }
            }

            list.add(cellDatas);

            for (ManualJournalListItem items : manualJournalListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < columnsForExport.size(); j++) {
                    temp = "";
                    if (ManualJournalListItem.NARRATION.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getNarration() == null ? "N/A" : items.getNarration(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.DATE.equals(header2Keys.get(j))) {
                        String date = " ";
                        if (items.getDate() != null) {
                            date = ServerUtils.dateFormat(items.getDate().getNonConvertedDate(), shortDateFormat);
                        }
                        temp = date;
                    }
                    if (ManualJournalListItem.DEBIT.equals(header2Keys.get(j))) {
                        BigDecimal debit = ZERO;
                        if (items.getDebit().compareTo(ZERO) != 0) {
                            debit = items.getDebit();
                        }

                        cellDatas[j] = new ExcelData(debit, ExcelData.CURRENCY, 20, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.CRETID.equals(header2Keys.get(j))) {
                        BigDecimal credit = ZERO;
                        if (items.getCredit().compareTo(ZERO) != 0) {
                            credit = items.getCredit();
                        }
                        cellDatas[j] = new ExcelData(credit, ExcelData.CURRENCY, 20, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.REFERENCENUMBER.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getReferenceNumber() == null ? "" : items.getReferenceNumber(), ExcelData.STRING, 20, false, !header.get(j).equals(ManualJournalListItem.CRETID), ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.STATUS.equals(header2Keys.get(j))) {
                        temp = items.getStatus() == null ? "N/A" : items.getStatus();
                    }
                    if (ManualJournalListItem.TYPE.equals(header2Keys.get(j))){
                        cellDatas[j] = new ExcelData(items.isRecurringTemplate() ? accountingLocalizer.localize(PdfLocalizationName.recurring) : accountingLocalizer.localize(PdfLocalizationName.single), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.CURRENCY.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getCurrency() == null ? "N/A" : items.getCurrency(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.CREATOR.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getCreator() == null ? "N/A" : items.getCreator(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.REPEATS.equals(header2Keys.get(j))){
                        cellDatas[j] = new ExcelData(items.getRepeats() == null ? "N/A" : items.getRepeats(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.ENDDATE.equals(header2Keys.get(j))) {
                        String date = " ";
                        if (items.getEndDate() != null) {
                            date = ServerUtils.dateFormat(items.getEndDate(), shortDateFormat);
                        }
                        temp = date;
                    }
                    if (ManualJournalListItem.NUMBER.equals(header2Keys.get(j))){
                        cellDatas[j] = new ExcelData(items.getNumber() == null ? "N/A" : items.getNumber(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, !header.get(j).equals(ManualJournalListItem.CRETID), ExcelData.NO_BORDER, ExcelData.NORMAL);
                    if (ManualJournalListItem.PROJECT.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getProject() == null ? "N/A" : items.getProject(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (ManualJournalListItem.APPROVER.equals(header2Keys.get(j))) {
                        cellDatas[j] = new ExcelData(items.getCurrentApprover() == null ? "N/A" : items.getCurrentApprover(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                }
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate holidays list excel report, exception: " + e);
        }
        return null;
    }

}
