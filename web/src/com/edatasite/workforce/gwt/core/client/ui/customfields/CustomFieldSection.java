package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilshod Madrahimov
 * Date: 8/26/14
 * Time: 1:32 PM
 */
public enum CustomFieldSection implements IsSerializable {

    //CRM
    CrmAccount(ViewName.CrmAccount, "Crm Account"),
    Contact(ViewName.Contact, "Contact"),
    Lead(ViewName.Lead, "Lead"),
    Opportunity(ViewName.Opportunity, "Opportunity"),
    CrmCase(ViewName.CrmCase, "Crm Case"),
    Estimate(ViewName.Estimate, "Estimate"),
    Activity(ViewName.Activity, "Activity"),
    LogACall(ViewName.LogACall, "LogACall"),

    //PM section
    Project(ViewName.Project, "Project"),
    Task(ViewName.Task, "Task"),
    Issues(ViewName.Issues, "Issues"),
    Contract(ViewName.Contract, "Contract"),

    //HRMS
    Employee(ViewName.Employee, "Employee"),
    Dependent(ViewName.Dependent, "Dependent"),
    PersonalGoal(ViewName.PersonalGoal, "Personal Goal"),
    DepartmentGoal(ViewName.DepartmentGoal, "Department Goal"),
    ProjectGoal(ViewName.ProjectGoal, "Project Goal"),
    BusinessGoal(ViewName.BusinessGoal, "Business Goal"),
    CompanyGoal(ViewName.CompanyGoal, "Company Goal"),
    Candidate(ViewName.Candidate, "Candidate"),
    OnboardingStep(ViewName.OnboardingStep, "Onboarding Step"),
    Vacancy(ViewName.Vacancy, "Vacancy"),
    Placement(ViewName.Placement, "Placement"),
    MeetingMInutesView(ViewName.MeetingMInutesView, "Meeting Minutes"),
    LeaveRequest(ViewName.LeaveRequest, "Leave Request"),
    BenefitRequest(ViewName.BenefitRequestList, "Benefit Request"),
    Certificates(ViewName.Certificates, "Certificates"),
    Department(ViewName.Department, "Department"),
    Positions(ViewName.Positions, "Positions"),
    Location(ViewName.Location, "Location"),
    Brand(ViewName.Brand, "Brand"),
    TalentProfileView(ViewName.TalentProfileView, "Talent Profile View"),

    //Accounting
    ProductServiceView(ViewName.ProductServiceView, "Product/Service View"),
    SaleInvoice(ViewName.SaleInvoice, "Sales Invoice"),
    SaleInvoiceSystem(ViewName.SaleInvoiceSystem, "Sales Invoice"),
    PurchaseInvoice(ViewName.PurchaseInvoice, "Purchase Invoice"),
    PurchaseInvoiceSystem(ViewName.PurchaseInvoiceSystem, "Purchase Invoice"),
    PurchaseOrderSystem(ViewName.PurchaseOrderSystem, "Purchase Order"),
    SaleQuote(ViewName.SaleQuote, "Sales Quote"),
    SaleOrder(ViewName.SaleOrder, "Sales Order"),
    SaleOrderSystem(ViewName.SaleOrderSystem, "Sales Order"),
    SaleQuoteSystem(ViewName.SaleQuoteSystem, "Sales Quote"),
    PurchaseOrder(ViewName.PurchaseOrder, "Purchase Order"),
    ExpenseReportView(ViewName.ExpenceReportView, "Expense Report View"),
    ExpenseReportViewSystem(ViewName.ExpenceReportViewSystem, "Expense Report View"),
    FixedAsset(ViewName.FixedAsset, "Fixed Asset"),
    BatchInvoicePaymentView(ViewName.BatchInvoicePaymentView, "Invoice Payments"),
    BatchPayBillView(ViewName.BatchPayBillView, "Paid Bills"),
    BatchPayBillViewSystem(ViewName.BatchPayBillViewSystem, "Paid Bills"),
    CompanySettings(ViewName.CompanySettings, "Company Settings"),
    BankAccounts(ViewName.BankAccounts, "Bank Account"),
    BankTransferList(ViewName.BankTransferList, "Bank Transfer"),
    RequestForQuote(ViewName.RequestForQuote, "Request For Quote"),
    RequestForPurchase(ViewName.RequestForPurchase, "Request For Purchase"),
    CustomerPrepayment(ViewName.Prepayment, "Customer Prepayment"),
    SupplierPrepayment(ViewName.Supplier, "Supplier Prepayment"),
    RentalProductsView(ViewName.RentalProductsView, "Rental Product"),
    RentalOrdersView(ViewName.RentalOrdersView, "Rental Order"),
    CashAdvanceView(ViewName.CashAdvanceList, "Cash Advance"),
    Account(ViewName.Account, "Chart of Account Form"),
    //Other
    ProductCategory(ViewName.ProductCategory, "Product Category"),
    ProductCategoryStoreFront(ViewName.ProductCategoryStoreFront, "Product Category Form"),
    SinglePayrun(ViewName.SinglePayrun, "Single Payrun"),

    //Item table custom fields
    SaleQuoteItem(ViewName.SaleQuoteItem, "Sale Quote Item"),
    SaleOrderItem(ViewName.SaleOrderItem, "Sale Order Item"),
    PurchaseOrderItem(ViewName.PurchaseOrderItem, "Purchase Order Item"),
    SaleInvoiceItem(ViewName.SaleInvoiceItem, "Sale Invoice Item"),
    PurchaseInvoiceItem(ViewName.PurchaseInvoiceItem, "Purchase Invoice Item"),
    OpportunitySubItem(ViewName.OpportunitySubItem, "Opportunity Sub Item"),
    ExpenseReportItem(ViewName.ExpenseReportItem, "Expense Report Item"),
    RFQItem(ViewName.RFQItem, "RFQ Item"),
    RentalOrderItem(ViewName.RentalOrdersView, "Rental Order Item"),
    RFPItem(ViewName.RFPItem, "RFP Item"),
    ClientItem(ViewName.ClientItem, "Customer Item"),
    SupplierItem(ViewName.SupplierItem, "SupplierItem"),
    CustomFormItems(ViewName.CustomFormItems, "CustomFormItems"),
    CustomFormItemTable(ViewName.CustomFormItemTable, "CustomFormItemTable"),
    OpportunityItemTable(ViewName.OpportunityItemTable, "OpportunityItemTable"),
    ProjectItemTable(ViewName.ProjectItemTable, "ProjectItemTable"),
    EmployeeItemTable(ViewName.EmployeeItemTable, "EmployeeItemTable"),
    ManualJournalItem(ViewName.ManualTransaction, "ManualJournalItem"),
    BankPaymentItem(ViewName.BankPaymentItem, "BankPaymentItem"),
    BankReceiptItem(ViewName.BankReceiptItem, "BankReceiptItem"),
    CashPaymentItem(ViewName.CashPaymentItem, "CashPaymentItem"),
    CashReceiptItem(ViewName.CashReceiptItem, "CashReceiptItem"),
    LeadItem(ViewName.LeadItem, "Lead Item"),
    AdditionalPaymentItem(ViewName.AdditionalPaymentItem, "Additional Payment Item"),
    PlacementItemTable(ViewName.PlacementItemTable, "Placement Item Table"),
    VacancyItemTable(ViewName.VacancyItemTable, "Vacancy Item Table"),
    RotationItemTable(ViewName.RotationItemTable, "Rotation Item Table"),
    GroupPlacementItemTable(ViewName.GroupPlacementItemTable, "Group Placement Item Table"),

    CandidateCustomItemTable(ViewName.CandidateCustomItemTable, "Candidate Item Table"),

    ExperienceItemTable(ViewName.ExperienceItemTable, "Employee Experience Item Table");


    private ViewName name;
    private String title;

    CustomFieldSection(ViewName name, String title) {
        this.name = name;
        this.title = title;
    }

    CustomFieldSection() {
    }

    public ViewName getName() {
        return name;
    }

    public void setName(ViewName name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static CustomFieldSection getBySectionName(String sectionName) {
        ViewName viewName = ViewName.valueOf(sectionName);
        CustomFieldSection[] values = values();
        if (values != null) {
            for (CustomFieldSection section : values) {
                if (section.getName() != null && section.getName().equals(viewName)) {
                    return section;
                }
            }
        }
        return null;
    }

}
