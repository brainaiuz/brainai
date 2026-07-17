package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.CHECK_NUMBER;

/**
 * User: Dilsh0d Madrahimov
 * Date: 03/03/2017
 */
public class BankTransferListExcelHandler extends BaseExcelHandler implements AccountingConstants {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    private static final Logger log = LoggerFactory.getLogger(BankTransferListExcelHandler.class);


    @Override
    protected void setFileName() {
        filename = "";
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        filename = fp.getViewType();
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParameters.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParameters.setLimit(1000);
        }
        filterParameters.setFromExcelPDF(true);
        filterParameters.setStartDate(parseFilterParameterDate(filterParameters.getStartDateNC()));
        filterParameters.setEndDate(parseFilterParameterDate(filterParameters.getEndDateNC()));

        ListResult<NewManualTransaction> bankTransferList = accountingService.getBankCashTransferList(filterParameters);

        ExcelData[] cellExcelData;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(ACTION);

        LinkedHashMap<String, String> mapColumnHeader = new LinkedHashMap<>();
        mapColumnHeader.put(NUMBER_COLUMN, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(ACCOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.account));
        mapColumnHeader.put(DATE_COLUMN, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(PROJECT_COLUMN, commonLocalizer.localize(PdfLocalizationName.project));
        mapColumnHeader.put(REFERENCE_COLUMN, commonLocalizer.localize(PdfLocalizationName.reference));
        mapColumnHeader.put(AMOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(CURRENCY_COLUMN, commonLocalizer.localize(PdfLocalizationName.currency));
        mapColumnHeader.put(CHECK_NUMBER, commonLocalizer.localize(PdfLocalizationName.checkNumber));
        mapColumnHeader.put(Constants.POST_DATED, commonLocalizer.localize(PdfLocalizationName.postedDate));
        mapColumnHeader.put(Constants.CREATOR, commonLocalizer.localize(PdfLocalizationName.createdBy));

        setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
            if (filterParameters.getPropertyCode().equals("CASH_RECEIPT")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashReceipt);
            } else if (filterParameters.getPropertyCode().equals("CASH_PAYMENT")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashPayment);
            } else if (filterParameters.getPropertyCode().equals("SPEND_MONEY")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.bankPayment);
            } else if (filterParameters.getPropertyCode().equals("RECEIVE_MONEY")) {
                sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.bankReceipt);
            } else {
                return null;
            }
            ArrayList<ExcelData[]> list = new ArrayList<>();
            cellExcelData = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelData[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 25, false, false, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelData);

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            Integer calculationScale = getCalculationScale(financialSettings);
            String temp = "";
            for (NewManualTransaction item : bankTransferList.getList()) {
                temp = "";
                cellExcelData = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (NUMBER_COLUMN.equals(header.get(j))) {
                        temp = item.getNumber() != null ? item.getNumber() : "—";
                    } else if (ACCOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getAccount() != null ? item.getAccount().getName() : "—";
                    } else if (DATE_COLUMN.equals(header.get(j))) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp = ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, shortDateFormat));
                        } else {
                            temp = ServerUtils.dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, shortDateFormat);
                        }
                        temp = ServerUtils.dateFormat(item.getDate() != null ? item.getDate().getNonConvertedDate() : null, shortDateFormat);
                    } else if (PROJECT_COLUMN.equals(header.get(j))) {
                        temp = item.getProject() != null ? item.getProject().getName() : "—";
                    } else if (CHECK_NUMBER.equals(header.get(j))) {
                        temp = item.getCheckNumber() != null ? item.getCheckNumber() : "—";
                    } else if (Constants.CREATOR.equals(header.get(j))) {
                        temp = item.getCreator() != null ? item.getCreator() : "—";
                    } else if (REFERENCE_COLUMN.equals(header.get(j))) {
                        temp = item.getReference() != null ? item.getReference() : "—";
                    } else if (AMOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getTotal() != null ? item.getTotal().setScale(calculationScale, BigDecimal.ROUND_HALF_UP).toString() : BigDecimal.ZERO.toString();
                    } else if (CURRENCY_COLUMN.equals(header.get(j))) {
                        temp = item.getCurrency() != null ? item.getCurrency().getName() : "—";
                    } else if (Constants.POST_DATED.equals(header.get(j))) {
                        temp = item.isPostDatedTransaction() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no);
                    } else {
                        if (item.getCustomFields() != null && item.getCustomFields().get(header.get(j)) != null) {
                            if (item.getCustomFields().get(header.get(j)) instanceof Date) {
                                temp = dateFormat((Date) item.getCustomFields().get(header.get(j)));
                            } else {
                                temp = item.getCustomFields().get(header.get(j)) != null ? item.getCustomFields().get(header.get(j)).toString() : "—";
                            }
                        }
                    }
                    cellExcelData[j] = new ExcelData(temp, ExcelData.STRING, 20, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellExcelData);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + filename + " excel report, exception: " + e);
        }
        return null;
    }

    public void setCustomFieldsPdfHeaderMap(List<CompanyCustomFieldItem> customfields, Map<String, String> pdfHeader) {
        if (customfields != null) {
            for (CompanyCustomFieldItem field : customfields) {
                pdfHeader.put(field.getColumnCode(), field.getFieldName());
            }
        }
    }

}
