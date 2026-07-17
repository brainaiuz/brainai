package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 12/19/2016.
 */
public class PaymentDeductionCategoriesExcelHandler extends BaseExcelHandler {
    @Autowired
    PayrollService payrollService;
    @Autowired
    PayslipTableItemManager payslipTableItemManager;
    @Autowired
    FinancialSettingsManager financialSettingsManager;
    private static final String CATEGORY = "CATEGORY";
    private static final String TYPE = "TYPE";
    private static final String AMOUNT = "AMOUNT";
    private static final String NAME = "NAME";

    private Integer calculationScale = 2;

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private static final Logger log = LoggerFactory.getLogger(PaymentDeductionCategoriesExcelHandler.class);
    private String fileHeaderName;

    @Override
    protected void setFileName() {
        this.filename = fileHeaderName;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        calculationScale = getCalculationScale(fs);
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        fileHeaderName = filterParameters.getCategory();

        String category = filterParameters.getCategory();
        SinglePayrunItem spItem = payrollService.getSinglePayrunItemPaymentDeductionCategories(filterParameters.getObjectId());
        if (spItem == null || category == null) {
            return null;
        }

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnData = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filterParameters.getCategory());

            List<ExcelData[]> list = new LinkedList<>();
            List<ExcelData> excelDataList = new ArrayList<>();
            if (category.equals(PayrollConstants.CATEGORY_PAYMENT) || category.equals(PayrollConstants.CATEGORY_DEDUCTION)) {
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.category), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
                for (PaymentDeductionObject item : category.equals(PayrollConstants.CATEGORY_PAYMENT) ? spItem.getPaymentCategories() : spItem.getDeductionCategories()) {
                    excelDataList = new ArrayList<>();
                    if (item != null && item.getCategoryItem() != null) {
                        excelDataList.add(new ExcelData(item.getCategoryItem().getName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        excelDataList.add(new ExcelData("", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                    excelDataList.add(new ExcelData(getType(item), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    excelDataList.add(new ExcelData(getAmount(item, spItem, filterParameters.getCategory().equals(PayrollConstants.CATEGORY_PAYMENT)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));

                    cellDatas = new ExcelData[excelDataList.size()];
                    excelDataList.toArray(cellDatas);
                    list.add(cellDatas);
                }
            } else {
                excelDataList = new ArrayList<>();
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.expense), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.amount), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.paidFrom), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 7, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);

                for (ExpenseData exp : spItem.getEmployeeExpenses().getExpenses()) {
                    excelDataList = new ArrayList<>();
                    excelDataList.add(new ExcelData(exp.getTitle(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    excelDataList.add(new ExcelData(exp.getAmount(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    excelDataList.add(new ExcelData(exp.getAccountID() != null ? exp.getAccount() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    excelDataList.add(new ExcelData(commonLocalizer.localize(PdfLocalizationName.payment), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    cellDatas = new ExcelData[excelDataList.size()];
                    excelDataList.toArray(cellDatas);
                    list.add(cellDatas);
                }
            }

            workBook.setList(list);
            return workBook.getWorkBook(fileHeaderName, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate group payrun list excel report, exception: " + e);
        }
        return null;
    }

    private String getType(PaymentDeductionObject item) {
        String type = "";

        if (item != null && item.getType() != null) {
            if (item.getType() == 0 || item.isLoan()) {
                type = "Fixed";
            } else if (item.getType() == 1) {
                type = item.getPercentage() + " % of Basic Salary";
            } else {
                type = item.getPercentage() + " % of Basic + Allowances";
            }
        } else {
            type = "Fixed";
        }
        return type;
    }

    private BigDecimal getAmount(PaymentDeductionObject paymentDeduction, SinglePayrunItem spItem, boolean isPayment) {
        BigDecimal salary = spItem.getSalary();
        BigDecimal basicSalary = spItem.getBasicSalary();
        BigDecimal result = BigDecimal.ZERO;
        if (paymentDeduction != null) {
            if (paymentDeduction.getType() == null || paymentDeduction.getType() == 0 || paymentDeduction.isLoan()) {
                result = paymentDeduction.getPaymentAmount();
            } else if (paymentDeduction.getPercentage() != null) {
                if (paymentDeduction.getPaymentAmount() != null) {
                    result = paymentDeduction.getPaymentAmount();
                } else if (!isPayment) {
                    if (paymentDeduction.isFromAllAllowances()) {
                        result = paymentDeduction.getPaymentAmount() != null ? paymentDeduction.getPaymentAmount() : BigDecimal.ZERO;
                        BigDecimal allowanceTotal = ZERO;
//                            CustomItemWidget allowance = (CustomItemWidget) employeeTable.getColumnById(grid.getCurrentRow(), ALLOWANCE);
//                            allowanceTotal = allowance.getAllowanceTotal(null).add(basicSalary);
                        allowanceTotal = allowanceTotal.add(basicSalary);
                        result = allowanceTotal.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                    } else if (paymentDeduction.getLinkedCategories() != null && paymentDeduction.getLinkedCategories().size() > 0) {
                        BigDecimal allowanceTotal = ZERO;
                        result = paymentDeduction.getPaymentAmount() != null ? paymentDeduction.getPaymentAmount() : BigDecimal.ZERO;
//                            allowanceTotal = allowance.getAllowanceTotal(paymentDeduction.getLinkedCategories()).add(basicSalary);
//                            result = allowanceTotal.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                    } else {
                        result = salary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                    }
                } else {
                    result = salary.multiply(paymentDeduction.getPercentage()).divide(BigDecimal.valueOf(100), calculationScale, BigDecimal.ROUND_HALF_UP);
                }
            }

        }
        return result;
    }
}
