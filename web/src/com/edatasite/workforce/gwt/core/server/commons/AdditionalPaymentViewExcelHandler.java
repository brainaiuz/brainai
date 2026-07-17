package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Majidov Abdulkhamid on 10/01/2022 1:56 PM.
 */
public class AdditionalPaymentViewExcelHandler extends BaseExcelHandler implements Constants {

    final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    protected static final Logger logger = LoggerFactory.getLogger(GroupPayrunViewExcelHandler.class);
    @Autowired
    protected CompanyPayrollSettingsManager companyPayrollSettingsManager;
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    int rowIndex = 0;
    SimpleDateFormat shortDateFormat;
    DecimalFormat defaultScaleFormat;
    boolean fromSummary;
    DataFormat format;
    @Autowired
    protected AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    protected PayrollService payrollService;
    @Autowired
    protected PayrollCategoryManager payrollCategoryManager;
    @Autowired
    @Qualifier("commonService")
    protected CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;


    protected HSSFWorkbook getWorkBook(Object object) {
        rowIndex = 0;
        AdditionalPayment additionalPaymentData = getAdditionalPayment(object);
        EdsUser user = userManager.getUser();
        String shortDateFormatStr = user.getCompany().getCompanySettings().getShortDateFormat();
        shortDateFormat = new SimpleDateFormat(shortDateFormatStr != null ? shortDateFormatStr : " ", Locale.ENGLISH);
        defaultScaleFormat = new DecimalFormat(",##0.00");

        try {
            workbook = new HSSFWorkbook();
            sheet = workbook.createSheet(commonLocalizer.localize("additionalPayment", "additional Payment"));
            sheet.setDefaultColumnWidth(20);
            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(1, 10000);

            format = workbook.createDataFormat();

            createHeaderTable(user, additionalPaymentData);
            createItemTable(additionalPaymentData);

            return workbook;


        } catch (Exception exp) {
            exp.printStackTrace();
            logger.error("Cannot generate " + filename + " excel report, exception: " + exp);
        }


        return null;
    }

    protected AdditionalPayment getAdditionalPayment(Object object) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(filterParameter.getObjectId());
        return additionalPayment != null ? additionalPayment.getRPC() : new AdditionalPayment();
    }

    protected void createHeaderTable(EdsUser user, AdditionalPayment additionalPayment) {
        int cellIndex = 1;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(user.getCompany().getName());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());

        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        StringBuilder str = new StringBuilder();
        str.append(commonLocalizer.localize("additionalPayment", "Additional Payment"));
        str.append(additionalPayment.getObjectID() != null ? " № " + additionalPayment.getObjectID() : "").append(commonLocalizer.localize("forDate", "For"));
        String period = "";
        if (additionalPayment.getYear() != null) {
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                period = ServerUtils.convertToUzbDateFormat(additionalPayment.getMonth() + ", " + additionalPayment.getYear());
            } else {
                period = additionalPayment.getMonth() + ", " + additionalPayment.getYear();
            }
        } else {
            period = additionalPayment.getMonth();
        }
        str.append(" ( ").append(period).append(" ) ");
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(str.toString());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());
        StringBuilder paymentType = new StringBuilder();
        EdsPayrollCategory payrollCategory = payrollCategoryManager.get(additionalPayment.getDefaultPayrollCategoryId());
        if (payrollCategory != null && payrollCategory.getName() != null) {
            paymentType.append("( ").append(payrollCategory.getName()).append(" )");
        }
        rowIndex++;
        row = generateOneRowWithEmptyCell(rowIndex, cellIndex);
        sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(paymentType.toString());
        sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderTitleStyleCell());
    }

    protected void createItemTable(AdditionalPayment additionalPayment) {
        int cellIndex = 0;
        int additionalColumns = 0;


        int cellCount = 12 + additionalColumns;

        //Just empty row
        rowIndex++;
        HSSFRow emptyCell = generateOneRowWithEmptyCell(rowIndex, cellIndex);

        rowIndex++;
        HSSFRow row = generateOneRowWithEmptyCell(rowIndex, cellCount);
        row.setHeight((short) 500);
        int colCounter = 0;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.code, "Code"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.employee, "Employee"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;
        sheet.getRow(rowIndex).getCell(colCounter).setCellValue(commonLocalizer.localize(PdfLocalizationName.totalAmount, "Total Amount"));
        sheet.getRow(rowIndex).getCell(colCounter).setCellStyle(getBlueStyleCell());
        colCounter++;

        rowIndex++;
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setDataFormat(format.getFormat("#,##0.00"));
        int counter = 0;
        BigDecimal total = BigDecimal.ZERO;
        additionalPayment.getItems().sort(new Comparator<PaymentDeductionObject>() {
            @Override
            public int compare(PaymentDeductionObject o1, PaymentDeductionObject o2) {
                return o1.getEmployee() != null && o2.getEmployee() != null ? o1.getEmployee().getName().compareTo(o2.getEmployee().getName()) : -1;
            }
        });
        for (PaymentDeductionObject item : additionalPayment.getItems()) {

            SelectItem employee = item.getEmployee();
            row = generateOneRowWithEmptyCell(rowIndex, cellCount);
            cellIndex = 0;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(employee.getDescription());
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(employee.getName());
            cellIndex++;
            sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0.0);
            sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
            cellIndex++;

            total = total.add(BigDecimal.valueOf(item.getTotalAmount() != null ? item.getTotalAmount().doubleValue() : 0.0));

            rowIndex++;
            counter++;
            if (additionalPayment.getItems().size() == counter) {
                row = generateOneRowWithEmptyCell(rowIndex, cellCount);
                cellIndex = 0;
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(commonLocalizer.localize(PdfLocalizationName.total, "Total"));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(getHeaderFieldStyleCell());
                cellIndex++;
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
                cellIndex++;
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(total.doubleValue());
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
                cellIndex = 0;
                rowIndex++;
                generateOneRowWithEmptyCell(rowIndex, cellCount);
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 2));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellValue(ServerUtils.getAmountInWords(total));
                sheet.getRow(rowIndex).getCell(cellIndex).setCellStyle(blueStyleCell);
                cellIndex++;
                rowIndex++;
            }
        }
            rowIndex++;
    }

    public String getCompanyPayrollSettings(String key) {
        final EdsCompanyPayrollSettings settings = companyPayrollSettingsManager.getCompanySettingValue(key);
        return settings != null ? settings.getValue() : null;
    }


    private CellStyle getBlueStyleCell() {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }

    private HSSFRow generateOneRowWithEmptyCell(int rowNumber, int cells) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= cells; i++) {
            Cell cell = row.createCell(i);
        }
        return row;
    }

    private CellStyle getHeaderTitleStyleCell() {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderFieldStyleCell() {
        CellStyle titleStyleCell = workbook.createCellStyle();
        titleStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleStyleCell.setFont(font);
        return titleStyleCell;
    }

    private CellStyle getHeaderStyleCell() {
        CellStyle blueStyleCell = workbook.createCellStyle();
        blueStyleCell.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
        blueStyleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        blueStyleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        blueStyleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        blueStyleCell.setBorderRight(CellStyle.BORDER_THIN);
        blueStyleCell.setRightBorderColor(HSSFColor.BLACK.index);

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        blueStyleCell.setFont(font);
        return blueStyleCell;
    }


    @Override
    protected void setFileName() {
        filename = "Additional Payment";
    }

    protected void setFileName(Object object) {
        try {
            String reference = getAdditionalPayment(object).getReference();
            if (!ServerUtils.isNullOrEmpty(reference)) {
                filename = reference;
            } else {
                filename = commonLocalizer.localize("additionalPayment", "Additional Payment");
            }
        } catch (Exception e) {
            filename = commonLocalizer.localize("additionalPayment", "Additional Payment");
        }
    }
}
