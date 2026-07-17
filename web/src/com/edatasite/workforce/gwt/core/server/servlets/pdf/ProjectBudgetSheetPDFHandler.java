package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ProjectBudgetManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextFontTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudget;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectBudgetItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by FARRUH OTABOYEV on 19-Feb-15.
 */
public class ProjectBudgetSheetPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private ProjectBudgetManager projectBudgetManager;

    @Autowired
    private ProjectService projectService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        Color bgColor = new Color(153, 187, 232);
        Color bgColor1 = new Color(220, 242, 255);

        ITextTableList tableList = new ITextTableList(5);
        // table header
        CellData[] cells = new CellData[5];
        cells[0] = new CellData(" ");
        cells[0].setBgColor(bgColor);
        cells[1] = new CellData(pmLocalizer.localize(PdfLocalizationName.plannedAmount));
        cells[1].setBgColor(bgColor);
        cells[1].setFont(createFont());
        cells[2] = new CellData(pmLocalizer.localize(PdfLocalizationName.actualAmount));
        cells[2].setBgColor(bgColor);
        cells[2].setFont(createFont());
        cells[3] = new CellData(commonLocalizer.localize(PdfLocalizationName.variance));
        cells[3].setBgColor(bgColor);
        cells[3].setFont(createFont());
        cells[4] = new CellData(pmLocalizer.localize(PdfLocalizationName.variancecost));
        cells[4].setBgColor(bgColor);
        cells[4].setFont(createFont());

        cells[1].setAlignment(Element.ALIGN_RIGHT);
        cells[2].setAlignment(Element.ALIGN_RIGHT);
        cells[3].setAlignment(Element.ALIGN_RIGHT);
        cells[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(cells);

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.SUMMARYVIEW);
        ITextSummaryView summaryView = new ITextSummaryView();

        ProjectBudget projectBudget = projectService.getProjectBudgetItems(filterParameter.getObjectId(), filterParameter.isWithTax());
        pdfData.setTableName(projectBudget.getProjectName() + " Budget Sheet");

        tableList.addTableWidthPercentage(0.32f, 0.17f, 0.17f, 0.17f, 0.17f);
        // Revenue
        CellData revenue = new CellData(pmLocalizer.localize(PdfLocalizationName.revenue));
        revenue.setBgColor(bgColor1);
        revenue.setFont(createFont());
        revenue.setBorder(0);
        revenue.setBorderLeft(1);
        revenue.setAlignment(Element.ALIGN_LEFT);

        CellData emptyCell = new CellData("");
        emptyCell.setBgColor(bgColor1);
        emptyCell.setBorder(0);
        CellData emptyCellRight = new CellData("");
        emptyCellRight.setBgColor(bgColor1);
        emptyCellRight.setBorder(0);
        emptyCellRight.setBorderRight(1);

        tableList.addPdfTableRows(revenue, emptyCell, emptyCell, emptyCell, emptyCellRight);

        // Sales quotes
        CellData[] salesQuotes = new CellData[1];
        salesQuotes[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.salesquotes));
        salesQuotes[0].setAlignment(Element.ALIGN_CENTER);
        salesQuotes[0].setFont(createFont());
        tableList.addPdfTableRows(salesQuotes);
        // Sales quotes list
        if (projectBudget.getSalesQuotes() != null) {
            for (ProjectBudgetItem salesQuot : projectBudget.getSalesQuotes()) {
                CellData[] salesQuote = new CellData[3];
                String vendorName = salesQuot.getVendor() != null ? salesQuot.getVendor().getName() != null ? (" - [" + salesQuot.getVendor().getName() + "]") : "" : "";
                salesQuote[0] = new CellData(salesQuot.getName() + vendorName);
                if (salesQuot.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesQuote[1] = new CellData(getSheetItem(salesQuot.getPlannedWageAmount()));
                } else {
                    salesQuote[1] = new CellData("");
                }
                if (salesQuot.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesQuote[2] = new CellData(getSheetItem(salesQuot.getActualWageAmount()));
                } else {
                    salesQuote[2] = new CellData("");
                }
                salesQuote[0].setAlignment(Element.ALIGN_CENTER);
                salesQuote[1].setAlignment(Element.ALIGN_RIGHT);
                salesQuote[2].setAlignment(Element.ALIGN_RIGHT);

                tableList.addPdfTableRows(salesQuote);
            }
        }

        // Sales orders
        CellData[] salesOrders = new CellData[1];
        salesOrders[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.salesOrders));
        salesOrders[0].setAlignment(Element.ALIGN_CENTER);
        salesOrders[0].setFont(createFont());
        tableList.addPdfTableRows(salesOrders);
        // Sales orders list
        if (projectBudget.getSalesOrders() != null) {
            for (ProjectBudgetItem order : projectBudget.getSalesOrders()) {
                CellData[] salesOrder = new CellData[3];
                String vendorName = order.getVendor() != null ? order.getVendor().getName() != null ? (" - [" + order.getVendor().getName() + "]") : "" : "";
                salesOrder[0] = new CellData(order.getName() + vendorName);
                if (order.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesOrder[1] = new CellData(getSheetItem(order.getPlannedWageAmount()));
                } else {
                    salesOrder[1] = new CellData("");
                }
                if (order.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesOrder[2] = new CellData(getSheetItem(order.getActualWageAmount()));
                } else {
                    salesOrder[2] = new CellData("");
                }
                salesOrder[0].setAlignment(Element.ALIGN_CENTER);
                salesOrder[1].setAlignment(Element.ALIGN_RIGHT);
                salesOrder[2].setAlignment(Element.ALIGN_RIGHT);

                tableList.addPdfTableRows(salesOrder);
            }
        }
        // Sales invoices
        CellData[] salesInvoices = new CellData[1];
        salesInvoices[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.salesInvoices));
        salesInvoices[0].setAlignment(Element.ALIGN_CENTER);
        salesInvoices[0].setFont(createFont());
        tableList.addPdfTableRows(salesInvoices);
        // Sales invoices list
        if (projectBudget.getSalesInvoices() != null) {
            for (ProjectBudgetItem salesInv : projectBudget.getSalesInvoices()) {
                CellData[] salesInvoice = new CellData[3];
                String vendorName = salesInv.getVendor() != null ? salesInv.getVendor().getName() != null ? (" - [" + salesInv.getVendor().getName() + "]") : "" : "";
                salesInvoice[0] = new CellData(salesInv.getName() + vendorName);
                salesInvoice[0].setAlignment(Element.ALIGN_CENTER);
                if (salesInv.getPlannedWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesInvoice[1] = new CellData(getSheetItem(salesInv.getPlannedWageAmount()));
                } else {
                    salesInvoice[1] = new CellData("");
                }
                if (salesInv.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    salesInvoice[2] = new CellData(getSheetItem(salesInv.getActualWageAmount()));
                } else {
                    salesInvoice[2] = new CellData("");
                }
                salesInvoice[0].setAlignment(Element.ALIGN_CENTER);
                salesInvoice[1].setAlignment(Element.ALIGN_RIGHT);
                salesInvoice[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(salesInvoice);
            }
        }

        //Bank Receipt
        CellData[] bankReceipts = new CellData[1];
        bankReceipts[0] = new CellData("Bank Receipt");
        bankReceipts[0].setAlignment(Element.ALIGN_CENTER);
        bankReceipts[0].setFont(createFont());
        tableList.addPdfTableRows(bankReceipts);
        // Bank Receipt list
        if (projectBudget.getBankReceipts() != null) {
            for (ProjectBudgetItem item : projectBudget.getBankReceipts()) {
                CellData[] bankReceipt = new CellData[3];
                bankReceipt[0] = new CellData(item.getName());
                bankReceipt[0].setAlignment(Element.ALIGN_CENTER);
                bankReceipt[1] = new CellData("");
                if (item.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    bankReceipt[2] = new CellData(getSheetItem(item.getActualWageAmount()));
                } else {
                    bankReceipt[2] = new CellData("");
                }
                bankReceipt[0].setAlignment(Element.ALIGN_CENTER);
                bankReceipt[1].setAlignment(Element.ALIGN_RIGHT);
                bankReceipt[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(bankReceipt);
            }
        }

       //Cash Receipt
        CellData[] cashReceipts = new CellData[1];
        cashReceipts[0] = new CellData("Cash Receipt");
        cashReceipts[0].setAlignment(Element.ALIGN_CENTER);
        cashReceipts[0].setFont(createFont());
        tableList.addPdfTableRows(cashReceipts);
        // Bank Receipt list
        if (projectBudget.getCashReceipts() != null) {
            for (ProjectBudgetItem item : projectBudget.getCashReceipts()) {
                CellData[] cashReceipt = new CellData[3];
                cashReceipt[0] = new CellData(item.getName());
                cashReceipt[0].setAlignment(Element.ALIGN_CENTER);
                cashReceipt[1] = new CellData("");
                if (item.getActualWageAmount().compareTo(BigDecimal.ZERO) != 0) {
                    cashReceipt[2] = new CellData(getSheetItem(item.getActualWageAmount()));
                } else {
                    cashReceipt[2] = new CellData("");
                }
                cashReceipt[0].setAlignment(Element.ALIGN_CENTER);
                cashReceipt[1].setAlignment(Element.ALIGN_RIGHT);
                cashReceipt[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(cashReceipt);
            }
        }


        // Sales invoices Subtotal
        CellData[] subtotal = new CellData[5];
        subtotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        subtotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getPlannedWageAmount()));
        subtotal[2] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getActualWageAmount()));
        subtotal[3] = new CellData(getSheetItemPercentage(projectBudget.getSubTotalRevenue().getVariancePerCent()));
        subtotal[4] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getVarianceAmount()));

        subtotal[0].setAlignment(Element.ALIGN_RIGHT);
        subtotal[1].setAlignment(Element.ALIGN_RIGHT);
        subtotal[2].setAlignment(Element.ALIGN_RIGHT);
        subtotal[3].setAlignment(Element.ALIGN_RIGHT);
        subtotal[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(subtotal);
        // Expenses
        CellData expenses = new CellData(pmLocalizer.localize(PdfLocalizationName.expenses));
        expenses.setBgColor(bgColor1);
        expenses.setFont(createFont());
        expenses.setBorder(0);
        expenses.setBorderLeft(1);
        expenses.setAlignment(Element.ALIGN_LEFT);

        tableList.addPdfTableRows(expenses, emptyCell, emptyCell, emptyCell, emptyCellRight);

        // Employee cost
        CellData[] employeeCost = new CellData[1];
        employeeCost[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.employeecost));
        tableList.addPdfTableRows(employeeCost);

        ProjectBudget projectBudgetClientCharge = new ProjectBudget();
        if (!filterParameter.isWageRate()){
            projectBudgetClientCharge = projectService.getEmployeeCostClientCharge(filterParameter.getObjectId());
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
        if (!filterParameter.isWageRate()) {
            fillEmployeeCost(tableList, projectBudgetClientCharge);
            // Emp Subtotal
            fillEmployeeSubtotal(tableList, projectBudgetClientCharge);
        } else {
            fillEmployeeCost(tableList, projectBudget);
            // Emp Subtotal
            fillEmployeeSubtotal(tableList, projectBudget);
        }



        // Expense Claims
        CellData[] expenseClaims = new CellData[1];
        expenseClaims[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.expenseClaims));
        expenseClaims[0].setFont(createFont());
        tableList.addPdfTableRows(expenseClaims);
        // Expense Claims list
        if (projectBudget.getExpenseClaims() != null) {
            for (ProjectBudgetItem exClaim : projectBudget.getExpenseClaims()) {
                CellData[] expenseClaimsList = new CellData[3];
                expenseClaimsList[0] = new CellData(exClaim.getName());
                expenseClaimsList[1] = new CellData(getSheetItem(exClaim.getPlannedWageAmount()));
                expenseClaimsList[2] = new CellData(getSheetItem(exClaim.getActualWageAmount()));

                expenseClaimsList[0].setAlignment(Element.ALIGN_CENTER);
                expenseClaimsList[1].setAlignment(Element.ALIGN_RIGHT);
                expenseClaimsList[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(expenseClaimsList);
            }
        }
        // Expense Claims Subtotal
        CellData[] exClainsSubtotal = new CellData[5];
        exClainsSubtotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        exClainsSubtotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalExpences().getPlannedWageAmount()));
        exClainsSubtotal[2] = new CellData(getSheetItem(projectBudget.getSubTotalExpences().getActualWageAmount()));
        exClainsSubtotal[3] = new CellData(getSheetItemPercentage(projectBudget.getSubTotalExpences().getVariancePerCent()));
        exClainsSubtotal[4] = new CellData(getSheetItem(projectBudget.getSubTotalExpences().getVarianceAmount()));

        exClainsSubtotal[0].setAlignment(Element.ALIGN_RIGHT);
        exClainsSubtotal[1].setAlignment(Element.ALIGN_RIGHT);
        exClainsSubtotal[2].setAlignment(Element.ALIGN_RIGHT);
        exClainsSubtotal[3].setAlignment(Element.ALIGN_RIGHT);
        exClainsSubtotal[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(exClainsSubtotal);

        //Bank Payment
        CellData[] bankPaymens = new CellData[1];
        bankPaymens[0] = new CellData("Bank Payment");
        bankPaymens[0].setFont(createFont());
        tableList.addPdfTableRows(bankPaymens);
        // Bank Payment list
        if (projectBudget.getBankPayments() != null) {
            for (ProjectBudgetItem item : projectBudget.getBankPayments()) {
                CellData[] bankPaymen = new CellData[3];
                bankPaymen[0] = new CellData(item.getName());
                bankPaymen[1] = new CellData("");
                bankPaymen[2] = new CellData(getSheetItem(item.getActualWageAmount()));

                bankPaymen[0].setAlignment(Element.ALIGN_CENTER);
                bankPaymen[1].setAlignment(Element.ALIGN_RIGHT);
                bankPaymen[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(bankPaymen);
            }
        }
        // Bank Payment Subtotal
        CellData[] bankPaymentSubTotal = new CellData[2];
        bankPaymentSubTotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        bankPaymentSubTotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalBankPayments().getActualWageAmount()));

        bankPaymentSubTotal[0].setAlignment(Element.ALIGN_RIGHT);
        bankPaymentSubTotal[1].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(bankPaymentSubTotal);

        //Cash Payment
        CellData[] cashPayments = new CellData[1];
        cashPayments[0] = new CellData("Cash Payment");
        cashPayments[0].setFont(createFont());
        tableList.addPdfTableRows(cashPayments);
        // Cash Payment list
        if (projectBudget.getCashPayments() != null) {
            for (ProjectBudgetItem item : projectBudget.getCashPayments()) {
                CellData[] bankPaymen = new CellData[3];
                bankPaymen[0] = new CellData(item.getName());
                bankPaymen[1] = new CellData("");
                bankPaymen[2] = new CellData(getSheetItem(item.getActualWageAmount()));

                bankPaymen[0].setAlignment(Element.ALIGN_CENTER);
                bankPaymen[1].setAlignment(Element.ALIGN_RIGHT);
                bankPaymen[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(bankPaymen);
            }
        }
        // Cash Payment Subtotal
        CellData[] cashPaymentSubTotal = new CellData[2];
        cashPaymentSubTotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        cashPaymentSubTotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalCashPayments().getActualWageAmount()));

        cashPaymentSubTotal[0].setAlignment(Element.ALIGN_RIGHT);
        cashPaymentSubTotal[1].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(cashPaymentSubTotal);


        // Product & Services cost
        CellData[] productAndServicesCost = new CellData[1];
        productAndServicesCost[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.ProductServicescost));
        productAndServicesCost[0].setFont(createFont());
        tableList.addPdfTableRows(productAndServicesCost);
        // Purchase Orders
        CellData[] purchaseOrders = new CellData[1];
        purchaseOrders[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.purchaseOrders));
        tableList.addPdfTableRows(purchaseOrders);

        // Purchase Orders list
        if (projectBudget.getPurchaseOrders() != null) {
            for (ProjectBudgetItem purchaseOrder : projectBudget.getPurchaseOrders()) {
                CellData[] purchaseOrdersList = new CellData[3];
                String vendorName = purchaseOrder.getVendor() != null ? purchaseOrder.getVendor().getName() != null ? (" - [" + purchaseOrder.getVendor().getName() + "]") : "" : "";
                purchaseOrdersList[0] = new CellData(purchaseOrder.getName() + vendorName);
                purchaseOrdersList[1] = new CellData(getSheetItem(purchaseOrder.getPlannedWageAmount()));
                purchaseOrdersList[2] = new CellData(getSheetItem(purchaseOrder.getActualWageAmount()));

                purchaseOrdersList[0].setAlignment(Element.ALIGN_CENTER);
                purchaseOrdersList[1].setAlignment(Element.ALIGN_RIGHT);
                purchaseOrdersList[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(purchaseOrdersList);
            }
        }

        // Purchase Invoices
        CellData[] purchaseInvoices = new CellData[1];
        purchaseInvoices[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.purchaseInvoices));
        tableList.addPdfTableRows(purchaseInvoices);
        // Purchase Invoices list
        if (projectBudget.getPurchaseInvoices() != null) {
            for (ProjectBudgetItem purchaseInvoice : projectBudget.getPurchaseInvoices()) {
                CellData[] purchaseInvoicesList = new CellData[3];
                String vendorName = purchaseInvoice.getVendor() != null ? purchaseInvoice.getVendor().getName() != null ? (" - [" + purchaseInvoice.getVendor().getName() + "]") : "" : "";
                purchaseInvoicesList[0] = new CellData(purchaseInvoice.getName() + vendorName);
                purchaseInvoicesList[1] = new CellData(getSheetItem(purchaseInvoice.getPlannedWageAmount()));
                purchaseInvoicesList[2] = new CellData(getSheetItem(purchaseInvoice.getActualWageAmount()));

                purchaseInvoicesList[0].setAlignment(Element.ALIGN_CENTER);
                purchaseInvoicesList[1].setAlignment(Element.ALIGN_RIGHT);
                purchaseInvoicesList[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(purchaseInvoicesList);
            }
        }

        // Purchase Subtotal
        CellData[] purchasesSubtotal = new CellData[5];
        purchasesSubtotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        purchasesSubtotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalPurchases().getPlannedWageAmount()));
        purchasesSubtotal[2] = new CellData(getSheetItem(projectBudget.getSubTotalPurchases().getActualWageAmount()));
        purchasesSubtotal[3] = new CellData(getSheetItemPercentage(projectBudget.getSubTotalPurchases().getVariancePerCent()));
        purchasesSubtotal[4] = new CellData(getSheetItem(projectBudget.getSubTotalPurchases().getVarianceAmount()));

        purchasesSubtotal[0].setAlignment(Element.ALIGN_RIGHT);
        purchasesSubtotal[1].setAlignment(Element.ALIGN_RIGHT);
        purchasesSubtotal[2].setAlignment(Element.ALIGN_RIGHT);
        purchasesSubtotal[3].setAlignment(Element.ALIGN_RIGHT);
        purchasesSubtotal[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(purchasesSubtotal);

        //Stock Adjustments
        CellData[] stockAdjustments = new CellData[1];
        stockAdjustments[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.stockAdjustments));
        tableList.addPdfTableRows(stockAdjustments);
        // Purchase Invoices list
        if (projectBudget.getStockAdjustments() != null) {
            for (ProjectBudgetItem stockAdjustment : projectBudget.getStockAdjustments()) {
                CellData[] stockAdjustmentsList = new CellData[3];
                String vendorName = stockAdjustment.getVendor() != null ? stockAdjustment.getVendor().getName() != null ? (" - [" + stockAdjustment.getVendor().getName() + "]") : "" : "";
                stockAdjustmentsList[0] = new CellData(stockAdjustment.getName() + vendorName);
                stockAdjustmentsList[1] = new CellData(getSheetItem(BigDecimal.ZERO));
                stockAdjustmentsList[2] = new CellData(getSheetItem(stockAdjustment.getActualWageAmount()));

                stockAdjustmentsList[0].setAlignment(Element.ALIGN_CENTER);
                stockAdjustmentsList[1].setAlignment(Element.ALIGN_RIGHT);
                stockAdjustmentsList[2].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(stockAdjustmentsList);
            }
        }
        // PROJECT REVENUE
        CellData[] projectRevenue = new CellData[5];
        projectRevenue[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.PROJECTREVENUE));
        projectRevenue[0].setBgColor(bgColor1);
        projectRevenue[0].setFont(createFont());
        projectRevenue[1] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getPlannedWageAmount()));
        projectRevenue[1].setBgColor(bgColor1);
        projectRevenue[2] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getActualWageAmount()));
        projectRevenue[2].setBgColor(bgColor1);
        projectRevenue[3] = new CellData(getSheetItemPercentage(projectBudget.getSubTotalRevenue().getVariancePerCent()));
        projectRevenue[3].setBgColor(bgColor1);
        projectRevenue[4] = new CellData(getSheetItem(projectBudget.getSubTotalRevenue().getVarianceAmount()));
        projectRevenue[4].setBgColor(bgColor1);

        projectRevenue[1].setAlignment(Element.ALIGN_RIGHT);
        projectRevenue[2].setAlignment(Element.ALIGN_RIGHT);
        projectRevenue[3].setAlignment(Element.ALIGN_RIGHT);
        projectRevenue[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(projectRevenue);

        // TOTAL PROJECT COST
        if (!filterParameter.isWageRate()) {
            fillTotalProjectCost(bgColor1, tableList, projectBudgetClientCharge);
            // Total Profit
            fillTotalProfil(bgColor1, tableList, projectBudgetClientCharge);
        } else {
            fillTotalProjectCost(bgColor1, tableList, projectBudget);
            // Total Profit
            fillTotalProfil(bgColor1, tableList, projectBudget);
        }

        summaryView.addTable(tableList);

        pdfData.setSummaryView(summaryView);
        return pdfData;
    }

    private void fillEmployeeSubtotal(ITextTableList tableList, ProjectBudget projectBudget) {
        CellData[] empSubtotal = new CellData[5];
        empSubtotal[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.subtotal));
        empSubtotal[0].setAlignment(Element.ALIGN_RIGHT);
        empSubtotal[1] = new CellData(getSheetItem(projectBudget.getSubTotalEmployees().getPlannedWageAmount()));
        empSubtotal[2] = new CellData(getSheetItem(projectBudget.getSubTotalEmployees().getActualWageAmount()));
        empSubtotal[3] = new CellData(getSheetItemPercentage(projectBudget.getSubTotalEmployees().getVariancePerCent()));
        empSubtotal[4] = new CellData(getSheetItem(projectBudget.getSubTotalEmployees().getVarianceAmount()));

        empSubtotal[0].setAlignment(Element.ALIGN_RIGHT);
        empSubtotal[1].setAlignment(Element.ALIGN_RIGHT);
        empSubtotal[2].setAlignment(Element.ALIGN_RIGHT);
        empSubtotal[3].setAlignment(Element.ALIGN_RIGHT);
        empSubtotal[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(empSubtotal);
    }

    private void fillTotalProfil(Color bgColor1, ITextTableList tableList, ProjectBudget projectBudget) {
        CellData[] totalProfit = new CellData[5];
        totalProfit[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.TOTALPROFIT));
        totalProfit[0].setBgColor(bgColor1);
        totalProfit[0].setFont(createFont());
        totalProfit[1] = new CellData(getSheetItem(projectBudget.getTotalProfit().getPlannedWageAmount()));
        totalProfit[1].setBgColor(bgColor1);
        totalProfit[2] = new CellData(getSheetItem(projectBudget.getTotalProfit().getActualWageAmount()));
        totalProfit[2].setBgColor(bgColor1);
        totalProfit[3] = new CellData(getSheetItemPercentage(projectBudget.getTotalProfit().getVariancePerCent()));
        totalProfit[3].setBgColor(bgColor1);
        totalProfit[4] = new CellData(getSheetItem(projectBudget.getTotalProfit().getVarianceAmount()));
        totalProfit[4].setBgColor(bgColor1);

        totalProfit[1].setAlignment(Element.ALIGN_RIGHT);
        totalProfit[2].setAlignment(Element.ALIGN_RIGHT);
        totalProfit[3].setAlignment(Element.ALIGN_RIGHT);
        totalProfit[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(totalProfit);
    }

    private void fillTotalProjectCost(Color bgColor1, ITextTableList tableList, ProjectBudget projectBudget) {
        CellData[] totalProjectCost = new CellData[5];
        totalProjectCost[0] = new CellData(pmLocalizer.localize(PdfLocalizationName.TOTALPROJECTCOST));
        totalProjectCost[0].setBgColor(bgColor1);
        totalProjectCost[0].setFont(createFont());
        totalProjectCost[1] = new CellData(getSheetItem(projectBudget.getTotalProjectCost().getPlannedWageAmount()));
        totalProjectCost[1].setBgColor(bgColor1);
        totalProjectCost[2] = new CellData(getSheetItem(projectBudget.getTotalProjectCost().getActualWageAmount()));
        totalProjectCost[2].setBgColor(bgColor1);
        totalProjectCost[3] = new CellData(getSheetItemPercentage(projectBudget.getTotalProjectCost().getVariancePerCent()));
        totalProjectCost[3].setBgColor(bgColor1);
        totalProjectCost[4] = new CellData(getSheetItem(projectBudget.getTotalProjectCost().getVarianceAmount()));
        totalProjectCost[4].setBgColor(bgColor1);

        totalProjectCost[1].setAlignment(Element.ALIGN_RIGHT);
        totalProjectCost[2].setAlignment(Element.ALIGN_RIGHT);
        totalProjectCost[3].setAlignment(Element.ALIGN_RIGHT);
        totalProjectCost[4].setAlignment(Element.ALIGN_RIGHT);
        tableList.addPdfTableRows(totalProjectCost);
    }

    private void fillEmployeeCost(ITextTableList tableList, ProjectBudget projectBudget) {
        if (projectBudget.getEmployeeCosts() != null) {
            for (ProjectBudgetItem emp : projectBudget.getEmployeeCosts()) {
                CellData[] employees = new CellData[5];
                employees[0] = new CellData(emp.getName());
                employees[1] = new CellData(getSheetItem(emp.getPlannedWageAmount()));
                employees[2] = new CellData(getSheetItem(emp.getActualWageAmount()));
                if (emp.getVariancePerCent().compareTo(BigDecimal.ZERO) != 0) {
                    employees[3] = new CellData(getSheetItemPercentage(emp.getVariancePerCent()));
                } else {
                    employees[3] = new CellData("");
                }
                if (emp.getVarianceAmount().compareTo(BigDecimal.ZERO) != 0) {
                    employees[4] = new CellData(getSheetItem(emp.getVarianceAmount()));
                } else {
                    employees[4] = new CellData("");
                }
                employees[0].setAlignment(Element.ALIGN_CENTER);
                employees[1].setAlignment(Element.ALIGN_RIGHT);
                employees[2].setAlignment(Element.ALIGN_RIGHT);
                employees[3].setAlignment(Element.ALIGN_RIGHT);
                employees[4].setAlignment(Element.ALIGN_RIGHT);
                tableList.addPdfTableRows(employees);
            }
        }
    }

    private com.lowagie.text.Font createFont() {
        return FontFactory.getFont(ITextFontTypeEnum.TIMES_NEW_ROMAN.getName(), BaseFont.IDENTITY_H, 8, com.lowagie.text.Font.BOLD);
    }

    private String getSheetItemPercentage(BigDecimal itemPerc) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        return ((itemPerc.compareTo(BigDecimal.ZERO) < 0 ? "(" + priceScaleNumberFormat.format(itemPerc.abs().multiply(new BigDecimal(100))) + "%)" : (priceScaleNumberFormat.format(itemPerc.multiply(new BigDecimal(100))) + "%")));
    }

    private String getSheetItem(BigDecimal amount) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        return ((amount.compareTo(BigDecimal.ZERO) < 0 ? "(" + priceScaleNumberFormat.format(amount.abs()) + ")" : (priceScaleNumberFormat.format(amount))));
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("ProjectBudgetSheet_" + dateFormat(user.getUserDate()));
    }
}
