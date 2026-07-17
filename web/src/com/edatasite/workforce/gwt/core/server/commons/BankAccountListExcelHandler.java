package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.07.2009
 * Time: 19:04:22
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(BankAccountListExcelHandler.class);

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private AccountingService accountingService;

    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Bank Accounts";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(1000);
        ListResult<BankAccount> solutionList = accountingService.getBankAccounts(filterParametrs);
        List<BankAccount> solutionListItems = solutionList.getList();
        EdsUser user = userManager.getUser();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(BankAccount.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BankAccount.CODE_COLUMN, accountingLocalizer.localize(PdfLocalizationName.code));
        mapColumnHeader.put(BankAccount.NUMBER_COLUMN, accountingLocalizer.localize(PdfLocalizationName.accountNumber));
        mapColumnHeader.put(BankAccount.NAME_COLUMN, accountingLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(BankAccount.CURRENCY_COLUMN, accountingLocalizer.localize(PdfLocalizationName.currency));
        mapColumnHeader.put(BankAccount.AMOUNT_COLUMN, accountingLocalizer.localize("balance"));

        setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        Integer calculationScale = getCalculationScale(financialSettings);


        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size()+1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, accountingLocalizer.localize(PdfLocalizationName.bankAccounts), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + "  Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(BankAccount.CODE_COLUMN) || header.get(i).equals(BankAccount.NUMBER_COLUMN) ? 50 : 20, false, header.get(i).equals(BankAccount.NAME_COLUMN) || header.get(i).equals(BankAccount.AMOUNT_COLUMN) || header.get(i).equals(BankAccount.CODE_COLUMN), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (BankAccount bankAccounts : solutionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (BankAccount.CODE_COLUMN.equals(header.get(j))) {
//                        temp = bankAccounts.getCode() != null ? bankAccounts.getCode() : "";
                        cellDatas[j] = new ExcelData(bankAccounts.getCode() != null ? bankAccounts.getCode() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankAccount.NUMBER_COLUMN.equals(header.get(j))) {
//                        temp = bankAccounts.getAccountNumber() == null ? "" : bankAccounts.getAccountNumber();
                        cellDatas[j] = new ExcelData(bankAccounts.getAccountNumber() == null ? "" : bankAccounts.getAccountNumber(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankAccount.NAME_COLUMN.equals(header.get(j))) {
                        temp = bankAccounts.getName() == null ? "" : bankAccounts.getName();
                    } else if(BankAccount.CURRENCY_COLUMN.equals(header.get(j))){
//                       temp = bankAccounts.getCurrency()==null ? "":bankAccounts.getCurrency().getName();
                        cellDatas[j] = new ExcelData(bankAccounts.getCurrency() == null ? "" : bankAccounts.getCurrency().getName(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    } else if (BankAccount.AMOUNT_COLUMN.equals(header.get(j))) {
                        cellDatas[j] = new ExcelData(bankAccounts.getBalance() != null ? bankAccounts.getBalance().setScale(calculationScale, RoundingMode.HALF_UP) : BigDecimal.ZERO, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        continue;
                    }
                    if (bankAccounts.getCustomFieldsMap() != null && bankAccounts.getCustomFieldsMap().get(header.get(j)) != null) {
                        if (bankAccounts.getCustomFieldsMap().get(header.get(j)) instanceof Date) {
                            temp  = dateFormat((Date) bankAccounts.getCustomFieldsMap().get(header.get(j)));
                        } else {
                            temp = bankAccounts.getCustomFieldsMap().get(header.get(j)) != null ? bankAccounts.getCustomFieldsMap().get(header.get(j)).toString() : "";
                        }
                        cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 30, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(BankAccount.CODE_COLUMN) || header.get(j).equals(BankAccount.NUMBER_COLUMN) ? 50 : 25, false, !header.get(j).equals(BankAccount.NAME_COLUMN), ExcelData.NO_BORDER, ExcelData.NORMAL);
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

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    public void setCustomFieldsPdfHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, String> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), field.getFieldName());
            }
        }
    }
}
