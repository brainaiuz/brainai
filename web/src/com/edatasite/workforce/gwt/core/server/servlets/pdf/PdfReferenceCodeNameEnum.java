package com.edatasite.workforce.gwt.core.server.servlets.pdf;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 14-Oct-2010
 * Time: 19:30:36
 */
public enum PdfReferenceCodeNameEnum {
    //Accounting
    SALES_INVOICE("accounting/sales_invoice.html"),
    SALES_RECEIPT("accounting/sales_receipt.html"),
    PROJECT_BASED_INVOICE(""),
    SALES_QUOTE("accounting/sales_quote.html"),
    SALES_ORDER("accounting/sales_order.html"),
    PURCHASE_INVOICE("accounting/purchase_invoice.html"),
    PURCHASE_ORDER("accounting/purchase_order.html"),
    RECEIVABLE_CREDIT_NOTE("accounting/invoice_credit_note.html"),
    PAYABLE_CREDIT_NOTE("accounting/purchase_invoice_credit_note.html"),
    PACKING_SLIP("accounting/sales_invoice_packing_slip.html"),
    SO_PACKING_SLIP("accounting/sales_order_packing_slip.html"),
    EXPENSE_REPORT("accounting/expense_report.html"),
    EXPENSE_PAYMENT_FORM("accounting/expense_payment.html"),
    STOCK_ADJUSTMENT("accounting/stock_adjustment.html"),
    FIXED_ASSET("accounting/fixed_assets.html"),
    CUSTOMER("accounting/customer.html"),
    SUPPLIER("accounting/supplier.html"),
    RFQ("accounting/request_for_quote.html"),
    DASHBOARD_CHARTS("accounting/dashboard_charts.html"),
    RFP("accounting/request_for_purchase.html"),
    //Transactions
    PREPAYMENT("accounting/transactions/invoice_payment.html"),
    SUPPLIER_CREDIT("accounting/transactions/supplier_credit.html"),
    BANK_RECEIPT("accounting/transactions/bank_receipt.html"),
    BANK_PAYMENT("accounting/transactions/bank_payment.html"),
    CASH_RECEIPT("accounting/transactions/cash_receipt.html"),
    CASH_PAYMENT("accounting/transactions/cash_payment.html"),
    MANUAL_ENTRY("accounting/transactions/manual_entry.html"),
    BANK_CHECK("accounting/transactions/bank_check.html"),
    BATCH_RECEIVE_PAYMENT("accounting/transactions/batch_receive_payment.html"),
    BATCH_PAY_BILL("accounting/transactions/batch_pay_bill.html"),
    CUSTOMER_SUPPLIER_BALANCE("accounting/custom_supplier_balance.html"),
    //Statements
    PROFIT_AND_LOSS("accounting/statements/profit_and_lost.html"),
    BALANCE_SHEET("accounting/statements/balance_sheet.html"),
    TRIAL_BALANCE("accounting/statements/trial_balance.html"),
    CASH_FLOW("accounting/statements/cash_flow.html"),
    AGING_SUMMARY("accounting/statements/aging_summary.html"),
    JOURNAL_REPORT("accounting/statements/journal_report.html"),
    ACCOUNT_TRANSACTIONS("accounting/statements/account_transactions.html"),
    STOCK_VALUATION("accounting/statements/stock_valuation.html"),
    VAT_RETURN("accounting/statements/vat_return.html"),
    UAE_VAT_RETURN("accounting/statements/uae_vat_return.html"),
    UK_VAT_RETURN("accounting/statements/uk_vat_return.html"),
    KSA_VAT_RETURN("accounting/statements/ksa_vat_return.html"),
    //Warehouse
    STOCK_TRANSFER("accounting/warehouse/stock_transfer.html"),
    WARE_HOUSE_PRODUCT_LIST("accounting/warehouse/ware_house_products_list.html"), //WAREHOUSE_SUMMARY_PDF
    //Payroll
    PAYSLIP(""),
    SINGLE_PAYRUN("payroll/single_payrun.html"),
    GROUP_PAYRUN("payroll/group_payrun.html"),
    CASH_ADVANCE("payroll/cash_advance.html"),
    ADDITIONAL_PAYMENT("payroll/additional_payment.html"),
    SINGLE_PAYMENT("payroll/single_payment.html"),
    //Reporting
    REPORTING_SYSTEM("reporting_system.html"),
    //Hrms
    EMPLOYEE_PROFILE("hrms/employee_profile.html"),
    NEWS("hrms/news.html"),
    DEPARTMENT("hrms/department.html"),
    LOCATION("hrms/location.html"),
    VACANCY("hrms/vacancy.html"),
    PLACEMENT("hrms/placement.html"),
    SHIFT("hrms/shift.html"),
    ROTATION("hrms/rotation.html"),
    POSITION("hrms/position.html"),
    GROUP_PLACEMENT("hrms/groupPlacement.html"),
    //PM
    ISSUE("pm/issue.html"),
    TIMESHEET("pm/timesheet.html"),
    TASK("pm/task.html"),
    //Others
    PROJECT_SUMMARY("pm/project.html"),
    BRIGADA("hrms/brigada.html"),
    ATTENDANCE_REPORT("hrms/attendance_report.html"),
    TERMINAL_ATTENDANCE("hrms/terminalAttendance.html"),
    WORK_STREAM_LIST(""),
    ID_CARD(""),
    TC_CONSOLIDATED_INVOICE(""),
    CASE_SUMMARY(""),
    LEAD_SUMMARY("crm/lead_summary.html"),
    COURSE_BOOKING(""),
    CERTIFICATE_SUMMARY(""),
    ATTENDANCE_TRACKING(""),
    MEETING_MINUTES("hrms/meeting_minutes.html"),
    ORGANIZATION_CHART("hrms/organizationChart.html"),
    SUPERVISOR_STRUCTURE("hrms/supervisorStructure.html"),
    LEAVE_REQUEST("hrms/leave_request.html"),
    GOODS_RECEIVED_NOTES("accounting/grn.html"),
    GOODS_DELIVERED_NOTES("accounting/gdn.html"),
    KPI_PAYMENT_VIEW("kpi_payment.html"),
    CRM_CONTACT("crm/crm_contact.html"),
    OPPORTUNITY("crm/opportunity.html"),
    EMPLOYEE("pm/employee.html"),
    CONTRACT("pm/contract.html"),
    BUDGET_BETA("pm/budget_beta.html"),
    PICK_LIST_VIEW("accounting/picklistView.html"),
    CUSTOM_FORM_ITEM_VIEW("customFormItemView.html"),
    PROGRESS_INVOICING_VIEW("accounting/progressInvoicing.html"),
    PRODUCTS_BARCODE("accounting/barcodes/products_barcode.html"),
    RECONCILATION_REPORT("accounting/reconcilationReport.html"),
    Goal("hrms/goal.html"),
    RENTAL_PRODUCT("accounting/rentalProduct.html"),
    RENTAL_ORDER("accounting/rentalOrder.html"),
    CANDIDATE_FORM("hrms/candidate.html"),
    APPRAISALS_ARCHIVE("hrms/appraisal.html"),
    BACKUP_EMPLOYEE("hrms/backup_employee.html"),
    BUILD_ASSEMBLY("accounting/build_assembly.html");

    PdfReferenceCodeNameEnum(String url) {
        this.url = url;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static PdfReferenceCodeNameEnum get(String type) {
        if (type == null) {
            return null;
        }
        for (PdfReferenceCodeNameEnum value : PdfReferenceCodeNameEnum.values()) {
            if (type.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}
