package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ProjectBudgetManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudget;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by FARRUH OTABOYEV on 24-Mar-15.
 */
public class ProjectBudgetSheetExcelHandler extends BaseExcelHandler {

    private static Logger log = LoggerFactory.getLogger(ProjectBudgetSheetExcelHandler.class);

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private ProjectBudgetManager projectBudgetManager;
    @Autowired
    private ProjectService projectService;

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Project Budget Sheet");
        sheet.setDefaultColumnWidth(20);
        generatePjectBudget(workbook, sheet, filterParametrs);

        return workbook;
    }

    private HSSFRow genetateOneRowWithEmpityCell(HSSFSheet sheet, int rowNumber, int cells, boolean withBorderCell) {
        HSSFRow row = sheet.createRow(rowNumber);
        for (int i = 0; i <= cells; i++) {
            Cell cell = createCell(row, i, withBorderCell);
        }
        return row;
    }

    private Cell createCell(HSSFRow row, int columnIndex, boolean withBorder) {
        return createCell(row, columnIndex, withBorder, null);
    }

    private Cell createCell(HSSFRow row, int columnIndex, boolean withBorder, Integer rowNumber) {
        return row.createCell(columnIndex);
    }

    private void generatePjectBudget(HSSFWorkbook workbook, HSSFSheet sheet, ListingFilterParameter filterParametrs) {
        //Styles
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        headerStyle.setFont(font);

        CellStyle bgColor = workbook.createCellStyle();
        bgColor.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        bgColor.setFillPattern(CellStyle.SOLID_FOREGROUND);
        bgColor.setFont(font);

        CellStyle bgColor1 = workbook.createCellStyle();
        bgColor1.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        bgColor1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        bgColor1.setAlignment(CellStyle.ALIGN_RIGHT);
        bgColor1.setFont(font);

        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle centerStyleWithFont = workbook.createCellStyle();
        centerStyleWithFont.setAlignment(CellStyle.ALIGN_CENTER);
        centerStyleWithFont.setFont(font);

        CellStyle onlyFont = workbook.createCellStyle();
        onlyFont.setFont(font);

        CellStyle alignRight = workbook.createCellStyle();
        alignRight.setAlignment(CellStyle.ALIGN_RIGHT);

        ProjectBudget projectBudget = projectService.getProjectBudgetItems(filterParametrs.getObjectId(), filterParametrs.isWithTax());

        Integer cells = 1;
        HSSFRow row = genetateOneRowWithEmpityCell(sheet, 0, cells, false);
        sheet.getRow(0).getCell(1).setCellValue(projectBudget.getProjectName() + " Budget Sheet");
        cells = 5;
        row = genetateOneRowWithEmpityCell(sheet, 1, cells, false);

        sheet.setColumnWidth(0, 10000);

        // table header
        sheet.getRow(1).getCell(0).setCellValue("");
        sheet.getRow(1).getCell(0).setCellStyle(headerStyle);
        sheet.getRow(1).getCell(1).setCellValue(pmLocalizer.localize(PdfLocalizationName.plannedAmount));
        sheet.getRow(1).getCell(1).setCellStyle(headerStyle);
        sheet.getRow(1).getCell(2).setCellValue(pmLocalizer.localize(PdfLocalizationName.actualAmount));
        sheet.getRow(1).getCell(2).setCellStyle(headerStyle);
        sheet.getRow(1).getCell(3).setCellValue(commonLocalizer.localize(PdfLocalizationName.variance));
        sheet.getRow(1).getCell(3).setCellStyle(headerStyle);
        sheet.getRow(1).getCell(4).setCellValue(pmLocalizer.localize(PdfLocalizationName.variancecost));
        sheet.getRow(1).getCell(4).setCellStyle(headerStyle);

        // Revenue
        row = genetateOneRowWithEmpityCell(sheet, 2, cells, false);
        sheet.getRow(2).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.revenue));
        sheet.getRow(2).getCell(0).setCellStyle(bgColor);
        sheet.getRow(2).getCell(1).setCellStyle(bgColor);
        sheet.getRow(2).getCell(2).setCellStyle(bgColor);
        sheet.getRow(2).getCell(3).setCellStyle(bgColor);
        sheet.getRow(2).getCell(4).setCellStyle(bgColor);

        // Sales quotes
        row = genetateOneRowWithEmpityCell(sheet, 3, cells, false);
        sheet.getRow(3).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.salesquotes));
        sheet.getRow(3).getCell(0).setCellStyle(centerStyleWithFont);
        int rowNum = 4;
        // Sales quotes list
        if (projectBudget.getSalesQuotes() != null) {
            for (ProjectBudgetItem salesQuot : projectBudget.getSalesQuotes()) {
                HSSFRow salesQuotRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                String vendorName = salesQuot.getVendor() != null ? salesQuot.getVendor().getName() != null ? (" - [" + salesQuot.getVendor().getName() + "]") : "" : "";
                sheet.getRow(rowNum).getCell(0).setCellValue(salesQuot.getName() + vendorName);
                if (salesQuot.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(salesQuot.getPlannedWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(1).setCellValue("");
                }
                if (salesQuot.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(salesQuot.getActualWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(2).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Sales orders
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.salesOrders));
        sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyleWithFont);
        rowNum++;
        // Sales orders list
        if (projectBudget.getSalesOrders() != null) {
            for (ProjectBudgetItem salesOrder : projectBudget.getSalesOrders()) {
                HSSFRow salesOrderRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                String vendorName = salesOrder.getVendor() != null ? salesOrder.getVendor().getName() != null ? (" - [" + salesOrder.getVendor().getName() + "]") : "" : "";
                sheet.getRow(rowNum).getCell(0).setCellValue(salesOrder.getName() + vendorName);
                if (salesOrder.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(salesOrder.getPlannedWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(1).setCellValue("");
                }
                if (salesOrder.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(salesOrder.getActualWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(2).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Sales invoices
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.salesInvoices));
        sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyleWithFont);
        rowNum++;
        // Sales invoices list
        if (projectBudget.getSalesInvoices() != null) {
            for (ProjectBudgetItem salesInv : projectBudget.getSalesInvoices()) {
                HSSFRow salesInvRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                String vendorName = salesInv.getVendor() != null ? salesInv.getVendor().getName() != null ? (" - [" + salesInv.getVendor().getName() + "]") : "" : "";
                sheet.getRow(rowNum).getCell(0).setCellValue(salesInv.getName() + vendorName);
                if (salesInv.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(salesInv.getPlannedWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(1).setCellValue("");
                }
                if (salesInv.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(salesInv.getActualWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(2).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Bank Receipt
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue("Bank Receipt");
        sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyleWithFont);
        rowNum++;
        // Bank Receipt list
        if (projectBudget.getBankReceipts() != null) {
            for (ProjectBudgetItem item : projectBudget.getBankReceipts()) {
                HSSFRow itemRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(item.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue("");
                if (item.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(item.getActualWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(2).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Cash Receipt
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue("Cash Receipt");
        sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyleWithFont);
        rowNum++;
        // Cash Receipt list
        if (projectBudget.getCashReceipts() != null) {
            for (ProjectBudgetItem item : projectBudget.getCashReceipts()) {
                HSSFRow itemRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(item.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue("");
                if (item.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(item.getActualWageAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(2).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Sales invoices Subtotal
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getSubTotalRevenue().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(3).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(4).setCellStyle(alignRight);
        rowNum++;

        // Expenses
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.expenses));
        sheet.getRow(rowNum).getCell(0).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(1).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(2).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(3).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(4).setCellStyle(bgColor);
        rowNum++;

        // Employee cost
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.employeecost));
        rowNum++;
        ProjectBudget projectBudgetClientCharge = new ProjectBudget();
        if (!filterParametrs.isWageRate()){
            projectBudgetClientCharge = projectService.getEmployeeCostClientCharge(filterParametrs.getObjectId());
            projectBudgetClientCharge.getTotalProjectCost().setPlannedWageAmount(projectBudgetClientCharge.getSubTotalExpences().getPlannedWageAmount().add(projectBudgetClientCharge.getSubTotalEmployees().getPlannedWageAmount()).add(projectBudgetClientCharge.getSubTotalPurchases().getPlannedWageAmount()));
            projectBudgetClientCharge.getTotalProjectCost().setActualWageAmount(projectBudgetClientCharge.getSubTotalExpences().getActualWageAmount().add(projectBudgetClientCharge.getSubTotalEmployees().getActualWageAmount()).add(projectBudgetClientCharge.getSubTotalPurchases().getActualWageAmount()));
            projectBudgetClientCharge.getTotalProjectCost().setVarianceAmount(projectBudgetClientCharge.getTotalProjectCost().getPlannedWageAmount().subtract(projectBudgetClientCharge.getTotalProjectCost().getActualWageAmount()));
            if (projectBudgetClientCharge.getTotalProjectCost().getActualWageAmount() != null && projectBudgetClientCharge.getTotalProjectCost().getActualWageAmount().doubleValue() != 0d) {
                projectBudgetClientCharge.getTotalProjectCost().setVariancePerCent(projectBudgetClientCharge.getTotalProjectCost().getVarianceAmount().divide(projectBudgetClientCharge.getTotalProjectCost().getActualWageAmount(), 4, BigDecimal.ROUND_HALF_UP));
            }

            projectBudgetClientCharge.getTotalProfit().setPlannedWageAmount(projectBudgetClientCharge.getSubTotalRevenue().getPlannedWageAmount().subtract(projectBudgetClientCharge.getTotalProjectCost().getPlannedWageAmount()));
            projectBudgetClientCharge.getTotalProfit().setActualWageAmount(projectBudgetClientCharge.getSubTotalRevenue().getActualWageAmount().subtract(projectBudgetClientCharge.getTotalProjectCost().getActualWageAmount()));
            projectBudgetClientCharge.getTotalProfit().setVarianceAmount(projectBudgetClientCharge.getTotalProfit().getActualWageAmount().subtract(projectBudgetClientCharge.getTotalProfit().getPlannedWageAmount()));
            if (projectBudgetClientCharge.getTotalProfit().getPlannedWageAmount() != null && projectBudgetClientCharge.getTotalProfit().getPlannedWageAmount().doubleValue() != 0d) {
                projectBudgetClientCharge.getTotalProfit().setVariancePerCent(projectBudgetClientCharge.getTotalProfit().getVarianceAmount().divide(projectBudgetClientCharge.getTotalProfit().getPlannedWageAmount(), 4, BigDecimal.ROUND_HALF_UP));
            }
        }
        // Employees list
        if (!filterParametrs.isWageRate()) {
            rowNum = fillEmployeesList(sheet, centerStyle, alignRight, projectBudgetClientCharge, cells, rowNum);
            // Emp Subtotal
            rowNum = fillEmployeesSubtotal(sheet, alignRight, projectBudgetClientCharge, cells, rowNum);
        } else {
            rowNum = fillEmployeesList(sheet, centerStyle, alignRight, projectBudget, cells, rowNum);
            // Emp Subtotal
            rowNum = fillEmployeesSubtotal(sheet, alignRight, projectBudget, cells, rowNum);
        }

        // Expense Claims
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.expenseClaims));
        sheet.getRow(rowNum).getCell(0).setCellStyle(onlyFont);
        rowNum++;
        // Expense Claims list
        if (projectBudget.getExpenseClaims() != null) {
            for (ProjectBudgetItem exClaim : projectBudget.getExpenseClaims()) {
                HSSFRow exClaimRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(exClaim.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(exClaim.getPlannedWageAmount()));
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(exClaim.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Expense Claims Subtotal
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalExpences().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getSubTotalExpences().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getSubTotalExpences().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getSubTotalExpences().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(3).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(4).setCellStyle(alignRight);
        rowNum++;


        // Bank Payment
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue("Bank Payment");
        sheet.getRow(rowNum).getCell(0).setCellStyle(onlyFont);
        rowNum++;
        // Bank Payment List
        if (projectBudget.getBankPayments() != null) {
            for (ProjectBudgetItem item : projectBudget.getBankPayments()) {
                HSSFRow itemRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(item.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue("");
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(item.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Bank Payment SubTotal
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalBankPayments().getActualWageAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        rowNum++;

       // Cash Payment
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue("Cash Payment");
        sheet.getRow(rowNum).getCell(0).setCellStyle(onlyFont);
        rowNum++;
        // Cash Payment List
        if (projectBudget.getCashPayments() != null) {
            for (ProjectBudgetItem item : projectBudget.getCashPayments()) {
                HSSFRow itemRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(item.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue("");
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(item.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Bank Payment SubTotal
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalCashPayments().getActualWageAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        rowNum++;

        // Product & Services cost
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.ProductServicescost));
        sheet.getRow(rowNum).getCell(0).setCellStyle(onlyFont);
        rowNum++;

        // Purchase Orders
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.purchaseOrders));
        rowNum++;
        // Purchase Orders list
        if (projectBudget.getPurchaseOrders() != null) {
            for (ProjectBudgetItem purchaseOrder : projectBudget.getPurchaseOrders()) {
                HSSFRow purchaseOrderRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                String vendorName = purchaseOrder.getVendor() != null ? purchaseOrder.getVendor().getName() != null ? (" - [" + purchaseOrder.getVendor().getName() + "]") : "" : "";
                sheet.getRow(rowNum).getCell(0).setCellValue(purchaseOrder.getName() + vendorName);
                sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(purchaseOrder.getPlannedWageAmount()));
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(purchaseOrder.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Purchase Invoices
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.purchaseInvoices));
        rowNum++;
        // Purchase Invoices list
        if (projectBudget.getPurchaseInvoices() != null) {
            for (ProjectBudgetItem purchaseInvoice : projectBudget.getPurchaseInvoices()) {
                HSSFRow purchaseInvoiceRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                String vendorName = purchaseInvoice.getVendor() != null ? purchaseInvoice.getVendor().getName() != null ? (" - [" + purchaseInvoice.getVendor().getName() + "]") : "" : "";
                sheet.getRow(rowNum).getCell(0).setCellValue(purchaseInvoice.getName() + vendorName);
                sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(purchaseInvoice.getPlannedWageAmount()));
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(purchaseInvoice.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // Purchase Subtotal
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalPurchases().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getSubTotalPurchases().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getSubTotalPurchases().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getSubTotalPurchases().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(3).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(4).setCellStyle(alignRight);
        rowNum++;

        // Stock Adjustments
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.stockAdjustments));
        rowNum++;
        // Purchase Invoices list
        if (projectBudget.getStockAdjustments() != null) {
            for (ProjectBudgetItem stockAdjustment : projectBudget.getStockAdjustments()) {
                HSSFRow stockAdjustmentRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(stockAdjustment.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(BigDecimal.ZERO));
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(stockAdjustment.getActualWageAmount()));

                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                rowNum++;
            }
        }

        // PROJECT REVENUE
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.PROJECTREVENUE));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getSubTotalRevenue().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getSubTotalRevenue().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(1).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(2).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(3).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(4).setCellStyle(bgColor1);
        rowNum++;

        // TOTAL PROJECT COST
        if (!filterParametrs.isWageRate()) {
            rowNum = fillTotalProjectCost(sheet, bgColor, bgColor1, projectBudgetClientCharge, cells, rowNum);
            // Total Profit
            fillTotalProfil(sheet, bgColor, bgColor1, projectBudgetClientCharge, cells, rowNum);
        } else {
            rowNum = fillTotalProjectCost(sheet, bgColor, bgColor1, projectBudget, cells, rowNum);
            // Total Profit
            fillTotalProfil(sheet, bgColor, bgColor1, projectBudget, cells, rowNum);
        }
    }

    private void fillTotalProfil(HSSFSheet sheet, CellStyle bgColor, CellStyle bgColor1, ProjectBudget projectBudget, Integer cells, int rowNum) {
        HSSFRow row;
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.TOTALPROFIT));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getTotalProfit().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getTotalProfit().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getTotalProfit().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getTotalProfit().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(1).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(2).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(3).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(4).setCellStyle(bgColor1);
    }

    private int fillTotalProjectCost(HSSFSheet sheet, CellStyle bgColor, CellStyle bgColor1, ProjectBudget projectBudget, Integer cells, int rowNum) {
        HSSFRow row;
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.TOTALPROJECTCOST));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getTotalProjectCost().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getTotalProjectCost().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getTotalProjectCost().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getTotalProjectCost().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(bgColor);
        sheet.getRow(rowNum).getCell(1).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(2).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(3).setCellStyle(bgColor1);
        sheet.getRow(rowNum).getCell(4).setCellStyle(bgColor1);
        rowNum++;
        return rowNum;
    }

    private int fillEmployeesSubtotal(HSSFSheet sheet, CellStyle alignRight, ProjectBudget projectBudget, Integer cells, int rowNum) {
        HSSFRow row;
        row = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
        sheet.getRow(rowNum).getCell(0).setCellValue(pmLocalizer.localize(PdfLocalizationName.subtotal));
        sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(projectBudget.getSubTotalEmployees().getPlannedWageAmount()));
        sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(projectBudget.getSubTotalEmployees().getActualWageAmount()));
        sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(projectBudget.getSubTotalEmployees().getVariancePerCent()));
        sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(projectBudget.getSubTotalEmployees().getVarianceAmount()));

        sheet.getRow(rowNum).getCell(0).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(3).setCellStyle(alignRight);
        sheet.getRow(rowNum).getCell(4).setCellStyle(alignRight);
        rowNum++;
        return rowNum;
    }

    private int fillEmployeesList(HSSFSheet sheet, CellStyle centerStyle, CellStyle alignRight, ProjectBudget projectBudget, Integer cells, int rowNum) {
        if (projectBudget.getEmployeeCosts() != null) {
            for (ProjectBudgetItem emp : projectBudget.getEmployeeCosts()) {
                HSSFRow empRow = genetateOneRowWithEmpityCell(sheet, rowNum, cells, false);
                sheet.getRow(rowNum).getCell(0).setCellValue(emp.getName());
                sheet.getRow(rowNum).getCell(1).setCellValue(getSheetItem(emp.getPlannedWageAmount()));
                sheet.getRow(rowNum).getCell(2).setCellValue(getSheetItem(emp.getActualWageAmount()));
                if (emp.getVariancePerCent().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(3).setCellValue(getSheetItemPercentage(emp.getVariancePerCent()));
                } else {
                    sheet.getRow(rowNum).getCell(3).setCellValue("");
                }
                if (emp.getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                    sheet.getRow(rowNum).getCell(4).setCellValue(getSheetItem(emp.getVarianceAmount()));
                } else {
                    sheet.getRow(rowNum).getCell(4).setCellValue("");
                }
                sheet.getRow(rowNum).getCell(0).setCellStyle(centerStyle);
                sheet.getRow(rowNum).getCell(1).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(2).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(3).setCellStyle(alignRight);
                sheet.getRow(rowNum).getCell(4).setCellStyle(alignRight);
                rowNum++;
            }
        }
        return rowNum;
    }

    private String getSheetItem(BigDecimal amount) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        return ((amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + priceScaleNumberFormat.format(amount.abs()) + ")" : (priceScaleNumberFormat.format(amount))));
    }

    private String getSheetItemPercentage(BigDecimal itemPerc) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        return ((itemPerc.compareTo(BigDecimal.ZERO) < 0 ? "(" + priceScaleNumberFormat.format(itemPerc.abs().multiply(new BigDecimal(100))) + "%)" : (priceScaleNumberFormat.format(itemPerc.multiply(new BigDecimal(100))) + "%")));
    }

    @Override
    protected void setFileName() {
        EdsUser user = userManager.getUser();
        filename = ("ProjectBudgetSheet_" + dateFormat(user.getUserDate()));

    }

}
