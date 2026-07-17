package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Djuraev on 10/19/14.
 */
public class ChartOfAccountListExcelHandler extends BaseExcelHandler {

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private UserManager userManager;


    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;


    @Override
    protected void setFileName() {
        filename = "Chart of Account";
    }

    private static final int LIMIT_EXCEL_ROW = 2000;
    private static final Logger logger = LoggerFactory.getLogger(ChartOfAccountListExcelHandler.class);

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = getCalculationScale(fs);

        ListingFilterParameter filterParameters = (ListingFilterParameter) object;

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(LIMIT_EXCEL_ROW);
        }

        List<AccountListItem> accountList = accountingService.getAccountList(filterParameters).getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(AccountListItem.CODE, new ExcelData(accountingLocalizer.localizeWithParam(PdfLocalizationName.code, ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.NAME, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.PARENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.parent), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.TYPE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.CURRENCY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.currency), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.BALANCE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.balance), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(AccountListItem.LAST_UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnCodeName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnCodeName)) {
                    excelDataList.add(mapColumnHeader.get(columnCodeName));
                }
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localizeWithParam(PdfLocalizationName.chartOfAccountsList, company.getName()), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);

            list.add(cellDatas);
            if (accountList != null && accountList.size() > 0) {
                for (AccountListItem item : accountList) {
                    Map<String, ExcelData> mapColumns = new HashMap<>();

                    if (panelTools.getColumnCodeName().contains(AccountListItem.CODE)) {
                        mapColumns.put(AccountListItem.CODE, new ExcelData(item.getCode(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    if (panelTools.getColumnCodeName().contains(AccountListItem.NAME)) {
                        mapColumns.put(AccountListItem.NAME, new ExcelData(item.getName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    if (panelTools.getColumnCodeName().contains(AccountListItem.PARENT)) {
                        mapColumns.put(AccountListItem.PARENT, new ExcelData(item.getParentName(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    if (panelTools.getColumnCodeName().contains(AccountListItem.TYPE)) {
                        mapColumns.put(AccountListItem.TYPE, new ExcelData(item.getAccountType(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    if (panelTools.getColumnCodeName().contains(AccountListItem.CURRENCY)) {
                        mapColumns.put(AccountListItem.CURRENCY, new ExcelData(item.getCurrency(), ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    BigDecimal balance = (item.getBalance() != null ? item.getBalance() : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP);

                    if (panelTools.getColumnCodeName().contains(AccountListItem.BALANCE)) {
                        mapColumns.put(AccountListItem.BALANCE, new ExcelData(balance, ExcelData.CURRENCY, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    if (panelTools.getColumnCodeName().contains(AccountListItem.LAST_UPDATED_DATE)) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            mapColumns.put(AccountListItem.LAST_UPDATED_DATE, new ExcelData(item.getLastUpdatedDate() != null ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getLastUpdatedDate())) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        } else {
                            mapColumns.put(AccountListItem.LAST_UPDATED_DATE, new ExcelData(item.getLastUpdatedDate() != null ? dateFormat(item.getLastUpdatedDate()) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        }
//                        mapColumns.put(AccountListItem.LAST_UPDATED_DATE, new ExcelData(item.getLastUpdatedDate() != null ? dateFormat(item.getLastUpdatedDate()) : "N/A", ExcelData.STRING, 15, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    excelDataList = new ArrayList<>();
                    for (String columnCodeName : panelTools.getColumnCodeName()) {
                        if (mapColumns.containsKey(columnCodeName)) {
                            excelDataList.add(mapColumns.get(columnCodeName));
                        }
                    }
                    cellDatas = new ExcelData[excelDataList.size()];
                    excelDataList.toArray(cellDatas);
                    list.add(cellDatas);
                }
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cannot generate chart of account list excel, exception: " + e.getMessage());
        }
        return null;
    }
}
