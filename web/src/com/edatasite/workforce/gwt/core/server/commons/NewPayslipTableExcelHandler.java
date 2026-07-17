package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.*;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 13.03.14
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
public class NewPayslipTableExcelHandler implements HttpRequestHandler {

    @Autowired
    private PayslipTableManager payslipTableManager;

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;

    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;

    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
    private ByteArrayOutputStream baos;
    private SimpleDateFormat shortDateFormat;
    private Integer calculationScale;
    private GroupPayrunData data;
    private WpsReportData wpsReportData;
    private Map<String, CellStyle> styles;
    boolean isPayslipTable;

    Map<Integer, String> paymentColumnMap = new HashMap<>();
    Map<Integer, String> deductionColumnMap = new HashMap<>();
    Map<Integer, String> taxColumnMap = new HashMap<>();

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("objectId") != null) {
            Integer objectID = Integer.parseInt(request.getParameter("objectId"));
            PayslipFilter filter = new PayslipFilter();
            filter.setObjectID(objectID);
            filter.setFromExcelHandler(true);
            data = payrollService.getPayslipTable(filter);
            isPayslipTable = true;
        } else {
            ListingFilterParameter lfp = getDataClass(request);
            lfp.setFromExcelPDF(true);
            wpsReportData = payrollService.getWpsReportData(lfp);
            isPayslipTable = false;
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        setCalculationScale(fs);
        writeHeader(response);
        prepareOutputStream();
        returnResponse(response);
    }

    private void writeHeader(HttpServletResponse response) {
        String fileName;
        if (isPayslipTable) {
            fileName = data.getApprover().getName() + "_" + data.getMonth() + "_Payslip";
        } else {
            fileName = "0000000" + "_" + wpsReportData.getMonth() + "_WPS_Report";
        }
        response.setHeader("content-disposition", "attachment; filename=\"" + fileName +".xls\"");
        response.setContentType(CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding("UTF8");
    }

    private void prepareOutputStream() {
        baos = new ByteArrayOutputStream();

        try {
            HSSFWorkbook wb = generateWorkBook();
            if (wb != null) {
                wb.write(baos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createStyles(HSSFWorkbook wb) {
        styles = new HashMap<>();
        CellStyle style;
        Font monthFont = wb.createFont();
        HSSFDataFormat df = wb.createDataFormat();
        monthFont.setFontHeightInPoints((short) 11);
        monthFont.setColor(IndexedColors.BLACK.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillForegroundColor(setColor(wb, (byte) 192, (byte) 192, (byte) 192).getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderLeft(CellStyle.BORDER_MEDIUM);
        style.setBorderRight(CellStyle.BORDER_MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillForegroundColor(setColor(wb, (byte) 219, (byte) 229, (byte) 241).getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("headerATS", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setWrapText(true);
        styles.put("cell", style);

        Font boldFont = wb.createFont();
        boldFont.setFontHeightInPoints((short) 12);
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(boldFont);
        styles.put("cellBold", style);


        style = wb.createCellStyle();
        style.setWrapText(false);
        style.setDataFormat(df.getFormat("#,##0.00"));
        styles.put("numberCell", style);
    }

    public HSSFColor setColor(HSSFWorkbook workbook, byte r, byte g, byte b) {
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor hssfColor = null;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(HSSFColor.LAVENDER.index, r, g, b);
                hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hssfColor;
    }

    private HSSFWorkbook generateWorkBook() {
        int row = 0;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        sheet.getPrintSetup().setLandscape(true);
        sheet.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        createStyles(workbook);
        if (isPayslipTable && data.isAtsCustomizationEnabled()) {
            row = drawPayrunItems(sheet);
        } else {
            row = drawItems(sheet, row);
        }
        if (isPayslipTable) {
            row += 5;
            drawFooter(sheet, row);
        }
        return workbook;
    }

    private Integer drawPayrunItems(HSSFSheet sheet) {
        int rowIndex = 0;
        Map<Integer, BigDecimal> sumList = new LinkedHashMap<>(10);

        //HEADER
        rowIndex = drawATSHeader(sheet, rowIndex);

        Cell cell;
        Row row;

        ExcelItem[] excelItem;
        List<ExcelItem[]> rowItemList = new ArrayList<>();
        int k = 0;
        for (SinglePayrunItem item : data.getTableItems()) {
            item.getPaymentCategories();
            int i = 0;
            excelItem = new ExcelItem[7 + data.getAllPaymentCategories().size() + data.getAllDeductionCategories().size()];
            excelItem[i++] = new ExcelItem(String.valueOf(++k), 0);
            excelItem[i++] = new ExcelItem(item.getEmployee(), 2, false);
            excelItem[i++] = new ExcelItem(item.getActualMonthPay() != null ? item.getActualMonthPay().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() : 0.0D, 0, true);

            putToSumMap(i, item.getActualMonthPay(), sumList);
            excelItem[i++] = new ExcelItem(item.getAdditionalPay() != null ? item.getAdditionalPay().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() : 0.0D, 0, true);
            putToSumMap(i, item.getAdditionalPay() != null ? item.getAdditionalPay() : BigDecimal.ZERO, sumList);


            while (paymentColumnMap.get(i) != null) {
                String pdoCode = paymentColumnMap.get(i);
                BigDecimal pamount = BigDecimal.valueOf(0);
                for (PaymentDeductionObject pdo : item.getPaymentCategories()) {
                    if (pdoCode.equals(pdo.getCategoryItem().getCode())) {
                        pamount = pdo.getPaymentAmount();
                        break;
                    }
                }
                excelItem[i++] = new ExcelItem(pamount.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
                putToSumMap(i, pamount, sumList);
            }

            excelItem[i++] = new ExcelItem(item.getAllowance().add(item.getActualMonthPay() != null ? item.getActualMonthPay() : BigDecimal.ZERO).add(item.getAdditionalPay() != null ? item.getAdditionalPay() : BigDecimal.ZERO).setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
            putToSumMap(i, item.getAllowance().add(item.getActualMonthPay() != null ? item.getActualMonthPay() : BigDecimal.ZERO).add(item.getAdditionalPay() != null ? item.getAdditionalPay() : BigDecimal.ZERO), sumList);

            while (deductionColumnMap.get(i) != null) {
                String pdoCode = deductionColumnMap.get(i);
                BigDecimal pamount = BigDecimal.valueOf(0);
                for (PaymentDeductionObject pdo : item.getDeductionCategories()) {
                    if (pdoCode.equals(pdo.getCategoryItem().getCode())) {
                        pamount = pdo.getPaymentAmount();
                        break;
                    }
                }
                excelItem[i++] = new ExcelItem(pamount.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
                putToSumMap(i, pamount, sumList);
            }

            excelItem[i++] = new ExcelItem(item.getDeduction().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
            putToSumMap(i, item.getDeduction(), sumList);

            while (taxColumnMap.get(i) != null) {
                String pdoCode = taxColumnMap.get(i);
                BigDecimal pamount = BigDecimal.valueOf(0);
                for (PaymentDeductionObject pdo : item.getTaxCategories()) {
                    if (pdoCode.equals(pdo.getCategoryItem().getCode())) {
                        pamount = pdo.getPaymentAmount();
                        break;
                    }
                }
                excelItem[i++] = new ExcelItem(pamount.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
                putToSumMap(i, pamount, sumList);
            }

            excelItem[i++] = new ExcelItem(item.getTax().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
            putToSumMap(i, item.getTax(), sumList);

            excelItem[i++] = new ExcelItem(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
            putToSumMap(i, item.getTotal(), sumList);
            rowItemList.add(excelItem);
        }
        sheet.setColumnWidth(0, 1000);
        for (ExcelItem[] rowItem : rowItemList) {
            row = sheet.createRow(++rowIndex);
            int i = 0;
            for (ExcelItem cellItem : rowItem) {
                cell = row.createCell(i);
                if (cellItem.isNumberField()) {
                    cell.setCellValue(cellItem.getDoubleValue());
                } else {
                    cell.setCellValue(cellItem.getValue());
                }
                cell.setCellStyle(cellItem.isNumberField() ? styles.get("numberCell") : styles.get("cell"));
                if (i == 0) {
                    cell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                }
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + cellItem.getMergeCount()));
                i += cellItem.getMergeCount() + 1;
            }
        }

        int i = 0;
        ExcelItem[] footer = new ExcelItem[6 + data.getAllPaymentCategories().size() + data.getAllDeductionCategories().size()];
        footer[i++] = new ExcelItem("TOTAL", 3);

        for (Map.Entry<Integer, BigDecimal> sum : sumList.entrySet()) {
            footer[i++] = new ExcelItem(sum.getValue().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 0, true);
        }

        row = sheet.createRow(++rowIndex);
        i = 0;
        for (ExcelItem ft : footer) {
            if (ft != null) {
                cell = row.createCell(i);
                if (ft.isNumberField()) {
                    cell.setCellValue(ft.getDoubleValue());
                } else {
                    cell.setCellValue(ft.getValue());
                }

                cell.setCellStyle(styles.get("headerATS"));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + ft.getMergeCount()));
                i += ft.getMergeCount() + 1;
            }
        }

        return rowIndex;
    }

    private void putToSumMap(int i, BigDecimal item, Map<Integer, BigDecimal> sumList) {
        sumList.merge(i, item, BigDecimal::add);

    }

    private int drawATSHeader(HSSFSheet sheet, int rowIndex) {
        int paymentCount = data.getAllPaymentCategories().size();
        int deductionCount = data.getAllDeductionCategories().size();
        sheet.setRightToLeft(true);
        Row row = sheet.createRow(++rowIndex);
        row.setHeightInPoints(50);
        Cell cell = row.createCell(0);
        cell.setCellStyle(styles.get("header"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paymentCount + deductionCount + 8));
        cell.setCellValue("الشركة الفنية لتوطين التقنية");
        row = sheet.createRow(++rowIndex);
        cell = row.createCell(0);
        cell.setCellStyle(styles.get("header"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, paymentCount + deductionCount + 8));
        shortDateFormat = new SimpleDateFormat("yyyy");
        cell.setCellValue("Salary for " + data.getMonth() + ", " + shortDateFormat.format(data.getCreatedDate().getNonConvertedDate()));

        ExcelItem[] tableHeader = new ExcelItem[]{
                new ExcelItem(commonLocalizer.localize(PdfLocalizationName.information), 5),
                new ExcelItem(commonLocalizer.localize(PdfLocalizationName.payments), paymentCount != 0 ? paymentCount - 1 : 0),
                new ExcelItem("TOTAL", 0),
                new ExcelItem(commonLocalizer.localize(PdfLocalizationName.deductions), deductionCount != 0 ? deductionCount - 1 : 0),
                new ExcelItem("TOTAL", 0),
                new ExcelItem("Compensation", 0)
        };

        row = sheet.createRow(++rowIndex);

        int i = 0;
        for (ExcelItem header : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(header.getValue());
            cell.setCellStyle(styles.get("headerATS"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
            i += header.getMergeCount() + 1;
        }

        tableHeader = new ExcelItem[7 + paymentCount + deductionCount];
        int k = 0;
        tableHeader[k++] = new ExcelItem("#", 0);
        tableHeader[k++] = new ExcelItem(commonLocalizer.localize(PdfLocalizationName.employee), 2);
        tableHeader[k++] = new ExcelItem("Basic Salary", 0);
        tableHeader[k++] = new ExcelItem("Addit. Pay", 0);

        for (PaymentDeductionSelectItem pdo : data.getAllPaymentCategories()) {
            tableHeader[k++] = new ExcelItem(pdo.getName(), 0);
            paymentColumnMap.put(k - 1, pdo.getCode());
        }
        tableHeader[k++] = new ExcelItem("TOTAL", 0);

        for (PaymentDeductionSelectItem pdo : data.getAllDeductionCategories()) {
            tableHeader[k++] = new ExcelItem(pdo.getName(), 0);
            deductionColumnMap.put(k - 1, pdo.getCode());
        }

        tableHeader[k++] = new ExcelItem("TOTAL", 0);
        tableHeader[k++] = new ExcelItem("PAYED.SAL", 0);

        for (PaymentDeductionSelectItem pdo : data.getAllTaxCategories()) {
            tableHeader[k++] = new ExcelItem(pdo.getName(), 0);
            deductionColumnMap.put(k - 1, pdo.getCode());
        }

        tableHeader[k++] = new ExcelItem("TOTAL", 0);
        tableHeader[k++] = new ExcelItem("PAYED.SAL", 0);


        row = sheet.createRow(++rowIndex);
        i = 0;
        for (ExcelItem header : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(header.getValue());
            cell.setCellStyle(styles.get("headerATS"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
            i += header.getMergeCount() + 1;
        }

        return rowIndex;

    }

    private Integer drawItems(HSSFSheet sheet, int rowIndex) {
        BigDecimal sumAW = BigDecimal.ZERO, sumP = BigDecimal.ZERO, sumDD = BigDecimal.ZERO, sumTotal = BigDecimal.ZERO;
        Integer sumDays = 0;
        ExcelItem[] tableHeader;
        List<ExcelItem[]> rowItemList = new ArrayList<>();
        ExcelItem[] excelItem;
        Cell cell = null;
        Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(50);
        if (isPayslipTable) {
            tableHeader = new ExcelItem[]{
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.employee), 2),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.month), 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.from), 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.to), 1),
                    new ExcelItem("Basic Salary", 1),
                    new ExcelItem("Allowances", 1),
                    new ExcelItem("Gov/Pension", 1),
                    new ExcelItem("Deductions", 1),
                    new ExcelItem("Bank Name", 1),
                    new ExcelItem("IBAN Code", 2),
                    new ExcelItem("Comments", 3),
                    new ExcelItem("Total Monthly Salary", 1)
            };
            shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (SinglePayrunItem item : data.getTableItems()) {
                String bankName = "";
                UserBankAccountData bankAccountData = allInOneService.getBankDetail(item.getEmployeeID());
                if (bankAccountData != null && bankAccountData.getBankName() != null) {
                    bankName = bankAccountData.getBankName();
                }
                excelItem = new ExcelItem[]{
                        new ExcelItem(item.getEmployee(), 2, false),
                        new ExcelItem(data.getMonth(), 1, false),
                        new ExcelItem(shortDateFormat.format(item.getFromDate().getNonConvertedDate()), 1, false),
                        new ExcelItem(shortDateFormat.format(item.getToDate().getNonConvertedDate()), 1, false),
                        new ExcelItem(item.getBasicSalary().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 1, true),
                        new ExcelItem(item.getAllowance().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 1, true),
                        new ExcelItem(item.getPensionAmount() != null ? item.getPensionAmount().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue() : BigDecimal.ZERO.doubleValue(), 1, true),
                        new ExcelItem(item.getDeduction().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 1, true),
                        new ExcelItem(bankName, 1, false),
                        new ExcelItem(item.getBankIBAN() != null ? item.getBankIBAN() : "", 2, false),
                        new ExcelItem(item.getDescription(), 3, false),
                        new ExcelItem(item.getTotal().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 1, true)
                };
                sumAW = sumAW.add(item.getAllowance());
                sumP = sumP.add(item.getPensionAmount() != null ? item.getPensionAmount() : BigDecimal.ZERO);
                sumDD = sumDD.add(item.getDeduction());
                sumTotal = sumTotal.add(item.getTotal());
                rowItemList.add(excelItem);
            }
            Integer i = 0;
            for (ExcelItem header : tableHeader) {
                cell = row.createCell(i);
                cell.setCellValue(header.getValue());
                cell.setCellStyle(styles.get("header"));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
                i += header.getMergeCount() + 1;
            }

            for (ExcelItem[] rowItem : rowItemList) {
                row = sheet.createRow(++rowIndex);
                i = 0;
                for (ExcelItem cellItem : rowItem) {
                    cell = row.createCell(i);
                    if (cellItem.isNumberField()) {
                        cell.setCellValue(cellItem.getDoubleValue());
                    } else {
                        cell.setCellValue(cellItem.getValue());
                    }
                    cell.setCellStyle(cellItem.isNumberField() ? styles.get("numberCell") : styles.get("cell"));
                    if (i.equals(0)) {
                        cell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                    } else if (i.equals(8)) {
                        cell.getCellStyle().setAlignment(CellStyle.ALIGN_CENTER);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + cellItem.getMergeCount()));
                    i += cellItem.getMergeCount() + 1;
                }
            }

            row = sheet.createRow(++rowIndex);
            cell = row.createCell(i - 12);
            cell.setCellValue(sumAW.doubleValue());
            cell.setCellStyle(styles.get("cell"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i - 12, i - 11));
            cell = row.createCell(i - 10);
            cell.setCellValue(sumP.doubleValue());
            cell.setCellStyle(styles.get("cell"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i - 10, i - 9));
            cell = row.createCell(i - 8);
            cell.setCellValue(sumDD.doubleValue());
            cell.setCellStyle(styles.get("cell"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i - 8, i - 7));
            cell = row.createCell(i - 2);
            cell.setCellValue(sumTotal.doubleValue());
            cell.setCellStyle(styles.get("cell"));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i - 2, i - 1));

        } else {
            tableHeader = new ExcelItem[]{
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.employee), 2),
                    new ExcelItem("EDR", 1),
                    new ExcelItem("PN", 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.code), 1),
                    new ExcelItem("IBAN", 2),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.from), 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.to), 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.days), 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.totalSalary), 2),
                    new ExcelItem("Net Salary", 1),
                    new ExcelItem(commonLocalizer.localize(PdfLocalizationName.leaveDays), 1)

            };

            Integer i = 0;
            for (ExcelItem header : tableHeader) {
                cell = row.createCell(i);
                cell.setCellValue(header.getValue());
                cell.setCellStyle(styles.get("header"));
                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + header.getMergeCount()));
                i += header.getMergeCount() + 1;
            }
            shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (WpsReportItem item : wpsReportData.getWpsReportItems()) {
                excelItem = new ExcelItem[]{
                        new ExcelItem(item.getEmployeeName(), 2),
                        new ExcelItem("EDR", 1),
                        new ExcelItem(item.getWpsNumber(), 1),
                        new ExcelItem(item.getBankCode(), 1),
                        new ExcelItem(item.getIbanNumber(), 2),
                        new ExcelItem(shortDateFormat.format(item.getFromDate()), 1),
                        new ExcelItem(shortDateFormat.format(item.getToDate()), 1),
                        new ExcelItem(String.valueOf(item.getWorkedDays() - item.getLeaveDays()), 1),
                        new ExcelItem(item.getRecurringPayments().setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 2, true),
                        new ExcelItem(item.getTotal().subtract(item.getRecurringPayments()).setScale(calculationScale, RoundingMode.HALF_UP).toString(), 1),
                        new ExcelItem(item.getLeaveDays().toString(), 1)
                };

                sumDays += item.getWorkedDays();
                sumTotal = sumTotal.add(item.getTotal());
                rowItemList.add(excelItem);

            }
            Date currentDate = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("Hm");
            SimpleDateFormat yearFormat = new SimpleDateFormat("Myyyy");
            excelItem = new ExcelItem[]{
                    new ExcelItem("", 2),
                    new ExcelItem("SCR", 1),
                    new ExcelItem(wpsReportData.getCompanyWpsNumber(), 1),
                    new ExcelItem(wpsReportData.getCompanyBankCode(), 1),
                    new ExcelItem(shortDateFormat.format(currentDate), 2),
                    new ExcelItem(timeFormat.format(currentDate), 1),
                    new ExcelItem(yearFormat.format(currentDate), 1),
                    new ExcelItem(String.valueOf(wpsReportData.getTotalCount()), 1),
                    new ExcelItem(sumTotal.setScale(calculationScale, RoundingMode.HALF_UP).doubleValue(), 2, true),
                    new ExcelItem("AED", 1),
                    new ExcelItem("", 1)
            };
            rowItemList.add(excelItem);


            for (ExcelItem[] rowItem : rowItemList) {
                row = sheet.createRow(++rowIndex);
                i = 0;
                for (ExcelItem cellItem : rowItem) {
                    cell = row.createCell(i);
                    if (cellItem.isNumberField()) {
                        cell.setCellValue(cellItem.getDoubleValue());
                    } else {
                        cell.setCellValue(cellItem.getValue());
                    }
                    cell.setCellStyle(cellItem.isNumberField() ? styles.get("numberCell") : styles.get("cell"));
                    if (i.equals(0)) {
                        cell.getCellStyle().setAlignment(CellStyle.ALIGN_LEFT);
                    }
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, i, i + cellItem.getMergeCount()));
                    i += cellItem.getMergeCount() + 1;
                }
            }

        }

        return rowIndex;
    }

    private void drawFooter(HSSFSheet sheet, int rowIndex) {
        Cell cell = null;
        Row row = sheet.createRow(++rowIndex);
        cell = row.createCell(0);
        cell.setCellValue("Prepared By:");
        cell.setCellStyle(styles.get("cellBold"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell = row.createCell(19);
        cell.setCellValue("Approved By:");
        cell.setCellStyle(styles.get("cellBold"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 19, 22));
        row = sheet.createRow(++rowIndex);
        cell = row.createCell(0);
        cell.setCellValue(data.getCreator().getName());
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell = row.createCell(19);
        cell.setCellValue(data.getApprover().getName());
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 19, 22));
        row = sheet.createRow(++rowIndex);
        cell = row.createCell(0);
        cell.setCellValue(data.getCreator().getDescription());
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell = row.createCell(19);
        cell.setCellValue(data.getApprover().getDescription());
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 19, 22));
        rowIndex += 3;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellValue("_________________________");
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell = row.createCell(19);
        cell.setCellValue("__________________________");
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 19, 22));
        rowIndex += 2;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        cell.setCellValue("Date:  " + data.getCreatedDate().getNonConvertedDate().toString());
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell = row.createCell(19);
        if (data.getApproveDate() != null) {
            cell.setCellValue("Date:  " + data.getApproveDate());
        } else {
            cell.setCellValue("Date:  ");
        }
        cell.setCellStyle(styles.get("cell"));
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 19, 22));
    }

    public void setCalculationScale(EdsFinancialSettings fs) {
        if (fs != null && fs.getCalculationScale() != null && fs.getCalculationScale() > 0) {
            calculationScale = fs.getCalculationScale();
        } else {
            calculationScale = 2;
        }
    }

    private void returnResponse(HttpServletResponse response) {
        try {
            byte[] data = baos.toByteArray();
            response.setContentLength(data.length);
            response.getOutputStream().write(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected ListingFilterParameter getDataClass(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        Iterator<Map> iterator = filterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        fp.setFacetFilter(WfmJsonUtils.jsonConvertToFacetFilterRpc(fp.getFacetFilterJson()));
        return fp;
    }

    public class ExcelItem {
        String value;
        Double doubleValue;
        Integer mergeCount;
        boolean isNumberField;

        public ExcelItem(String value, Integer mergeCount) {
            this.value = value;
            this.mergeCount = mergeCount;
        }

        public ExcelItem(String value, Integer mergeCount, boolean isNumberField) {
            this.value = value;
            this.mergeCount = mergeCount;
            this.isNumberField = isNumberField;
        }

        public ExcelItem(Double value, Integer mergeCount, boolean isNumberField) {
            this.doubleValue = value;
            this.mergeCount = mergeCount;
            this.isNumberField = isNumberField;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getMergeCount() {
            return mergeCount;
        }

        public void setMergeCount(Integer mergeCount) {
            this.mergeCount = mergeCount;
        }

        public boolean isNumberField() {
            return isNumberField;
        }

        public Double getDoubleValue() {
            return doubleValue;
        }

        public void setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
        }
    }
}
