package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * Date: 27.07.2009
 * Time: 20:34:14
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseReportListExcelHandler extends BaseExcelHandler implements AccountingConstants {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    private String sheetname;
    private static final Logger log = LoggerFactory.getLogger(ExpenseReportListExcelHandler.class);


    @Override
    protected void setFileName() {
        filename = "Expense Claims";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        boolean isDoubleApprovedEnabled = filterParametrs.isShowYTD();
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        String shortDateFormat = "MMM dd, yyyy";
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }

        filterParametrs.setStartDate(parseFilterParameterDate(filterParametrs.getStartDateNC()));
        filterParametrs.setEndDate(parseFilterParameterDate(filterParametrs.getEndDateNC()));

        ListResult<ExpenseReportsListItem> reportsList = expenseService.getExpenseReportsDataFromSolr(filterParametrs);
        List<ExpenseReportsListItem> reports = reportsList.getList();

        ExcelData[] cellExcelDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(ExpenseReportsListItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(NUMBER_COLUMN, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(TITLE_COLUMN, commonLocalizer.localize(PdfLocalizationName.title));
        mapColumnHeader.put(PERIOD_COLUMN, commonLocalizer.localize(PdfLocalizationName.date));
        mapColumnHeader.put(TAX_AMOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.taxAmount));
        mapColumnHeader.put(PROJECT_COLUMN, commonLocalizer.localize(PdfLocalizationName.relatedProject_exportFiles));
        mapColumnHeader.put(REPORTER_COLUMN, commonLocalizer.localize(PdfLocalizationName.reporter));
        if (isDoubleApprovedEnabled) {
            mapColumnHeader.put(ACCOUNTANT_COLUMN, commonLocalizer.localize(PdfLocalizationName.accountant));
            mapColumnHeader.put(APPROVER_COLUMN, commonLocalizer.localize(PdfLocalizationName.approver));
            mapColumnHeader.put(STATUS_COLUMN, commonLocalizer.localize(PdfLocalizationName.accountantStatus));
            mapColumnHeader.put(APPROVER_STATUS_COLUMN, commonLocalizer.localize(PdfLocalizationName.approverStatus));
        } else {
            mapColumnHeader.put(APPROVER_COLUMN, commonLocalizer.localize(PdfLocalizationName.approver));
            mapColumnHeader.put(STATUS_COLUMN, commonLocalizer.localize(PdfLocalizationName.status));
        }
        mapColumnHeader.put(ORIGINAL_AMOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.originalAmount));
        mapColumnHeader.put(PAID_AMOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.paidAmount));
        mapColumnHeader.put(DUE_AMOUNT_COLUMN, commonLocalizer.localize(PdfLocalizationName.amount));
        mapColumnHeader.put(RELATED_PO, accountingLocalizer.localize(PdfLocalizationName.relatedPO));
        mapColumnHeader.put(FIXED_ASSET, commonLocalizer.localize(PdfLocalizationName.fixedAsset));
        mapColumnHeader.put(Constants.SUPPLIER, commonLocalizer.localize(PdfLocalizationName.supplier));
        mapColumnHeader.put(CURRENCY_COLUMN, commonLocalizer.localize(PdfLocalizationName.currency));
        mapColumnHeader.put(TYPE_COLUMN, commonLocalizer.localize(PdfLocalizationName.type));

        setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            ListingFilterParameter fp = (ListingFilterParameter) object;
            EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
            sheetname = property != null ? property.getPlural() : accountingLocalizer.localize(PdfLocalizationName.expenseClaims);
            List<ExcelData[]> list = new LinkedList<>();
            cellExcelDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, sheetname, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 25, false, false, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelDatas);

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            Integer calculationScale = getCalculationScale(financialSettings);
            String temp = "";
            boolean isNumberFiled = false;
            for (ExpenseReportsListItem item : reports) {
                temp = "";
                isNumberFiled = false;
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    isNumberFiled = false;
                    if (NUMBER_COLUMN.equals(header.get(j))) {
                        temp = item.getExpenseNumber() != null ? item.getExpenseNumber() : "";
                    } else if (TITLE_COLUMN.equals(header.get(j))) {
                        temp = item.getTitle() != null ? item.getTitle() : "";
                    } else if (PERIOD_COLUMN.equals(header.get(j))) {
                        temp = ServerUtils.dateFormat(item.getStartDate() != null ? item.getStartDate().getNonConvertedDate() : null, shortDateFormat);
                        temp = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(temp) : temp;
                    } else if (PROJECT_COLUMN.equals(header.get(j))) {
                        temp = item.getProjectName() != null ? item.getProjectName() : "";
                    } else if (REPORTER_COLUMN.equals(header.get(j))) {
                        temp = item.getReporterName() != null ? item.getReporterName() : "";
                    }
                    if (isDoubleApprovedEnabled) {
                        if (APPROVER_COLUMN.equals(header.get(j))) {
                            temp = item.getApproverSelectItem() != null ? item.getApproverSelectItem().getName() : "";
                        } else if (STATUS_COLUMN.equals(header.get(j))) {
                            temp = item.getOverallStatusName();
                        }
                    } else {
                        if (APPROVER_COLUMN.equals(header.get(j))) {
                            temp = item.getApproverSelectItem() != null ? item.getApproverSelectItem().getName() : "";
                        } else if (STATUS_COLUMN.equals(header.get(j))) {
                            temp = item.getOverallStatusName();
                        }
                    }
                    if (ORIGINAL_AMOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getTotal() != null ? item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString() : BigDecimal.ZERO.toString();
                        isNumberFiled = true;
                    } else if (PAID_AMOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getPaidTotal() != null ? item.getPaidTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString() : BigDecimal.ZERO.toString();
                        isNumberFiled = true;
                    } else if (TAX_AMOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getTaxTotal() != null ? item.getTaxTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString() : BigDecimal.ZERO.toString();
                        isNumberFiled = true;
                    } else if (DUE_AMOUNT_COLUMN.equals(header.get(j))) {
                        temp = item.getDueTotal() != null ? item.getDueTotal().setScale(calculationScale, RoundingMode.HALF_UP).toString() : BigDecimal.ZERO.toString();
                        isNumberFiled = true;
                    } else if (RELATED_PO.equals(header.get(j))) {
                        temp = item.getPurchaseOrderNumber() != null ? item.getPurchaseOrderNumber() : "";
                    } else if (FIXED_ASSET.equals(header.get(j))) {
                        temp = item.getFixedAsset() != null ? item.getFixedAsset().getName() : "";
                    } else if (Constants.SUPPLIER.equals(header.get(j))) {
                        temp = item.getSupplier() != null ? item.getSupplier().getName() : "";
                    } else if (CURRENCY_COLUMN.equals(header.get(j))) {
                        temp = item.getExpenseCurrency() != null ? item.getExpenseCurrency().getName() : "";
                    } else if (TYPE_COLUMN.equals(header.get(j))) {
                        temp = item.isCompanyExpense() ? commonLocalizer.localize("companyExpense") : commonLocalizer.localize("employeeExpense");
                    } else {
                        if (item.getCustomFields() != null && item.getCustomFields().get(header.get(j)) != null) {
                            if (item.getCustomFields().get(header.get(j)) instanceof Date) {
                                temp = dateFormat((Date) item.getCustomFields().get(header.get(j)));
                            } else {
                                temp = item.getCustomFields().get(header.get(j)) != null ? item.getCustomFields().get(header.get(j)).toString() : "";
                            }
                        }
                    }
                    cellExcelDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate expense reports list excel report, exception: " + e);
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
