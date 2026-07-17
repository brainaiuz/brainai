package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 03-Jun-2011
 * Time: 14:05:01
 * <p/>
 * This is enumerution Facet contents code name
 */
public enum FacetContentType {

    TaskFacetFilter("project", "client", "workstream", "overallStatus", "priority", "assignees", "relatedContact", "relatedCrmAccount", "relatedLead", "relatedCase", "relatedOpportunity", "relatedProject", "relatedEvent", "relatedIssue", "relatedEmployee", "relatedDepartment", "relatedClient", "relatedSupplier", "assigneeStatus", "projectManager", "type"),
    ProjectFacetFilter("manager", "client", "status", "assignees", "location", "relatedContact", "relatedCrmAccount", "relatedLead", "relatedCase", "relatedOpportunity", "relatedEvent", "relatedTask", "relatedIssue", "relatedEmployee", "relatedDepartment", "relatedClient", "relatedSupplier", "backupmanager"),
    PurchaseOrderFacetFilter("client", "status", "dueamount", "project", "currency", "shippingmethod", "creator", "manager"),
    CaseFacetFilter("reportby", "type", "priority", "status", "caseorign", "assignees", "department", "resolver", "relatedContact", "relatedCrmAccount", "relatedLead", "relatedOpportunity", "relatedTask", "relatedProject", "relatedEvent", "relatedIssue", "relatedEmployee", "relatedDepartment", "relatedClient", "relatedSupplier", "relatedSaleQuote", "internaleStatus"),
    SaleInvoiceFacetFilter("client", "status", "dueamount", "paidamount", "currency", "shippingmethod", "project", "originalAmount", "product", "inTarget", "type"),
    SaleQuoteFacetFilter("client", "status", "dueamount", "project", "currency", "shippingmethod"),
    SaleOrderFacetFilter("client", "status", "dueamount", "project", "currency", "shippingmethod"),
    ClientFacetFilter("branchof", "createdby", "ownership", "industy", "organizationtype", "annualRevenue", "billingcountry", "billingstate", "city", "inTarget", "taxRate", "isBlocked", "salesType"),
    SupplierFacetFilter("branchof", "createdby", "ownership", "industy", "organizationtype", "annualRevenue", "billingcountry", "billingstate", "inTarget", "taxRate"),
    LeadFacetFilter("campaign", "leadsource", "status", "country", "jobtitle", "assignedto", "leadowner"),
    ContactFacetFilter("country", "company", "department", "jobtitle", "contactowner", "category", "campaign", "state", "mailList"),
    CandidateFacetFilter("vacancies", "source", "location", "status", "owner", "project","department","position"),
    CrmAccountFacetFilter("parentacoount", "accounttype", "owner", "ownership", "industry", "organizationtype", "annualRevenue", "country", "status", "taxRate"),
    NewsFacetFilter("category", "postedby", "type"),
    OpportunityFacetFilter("opportunitystage", "assignee", "account", "country", "amount", "campaign", "creator", "backupAssignee", "leadSource", "project", "hasAttachment"),
    EventFacetFilter("shared", "type", "callType", "relatedContact", "relatedCrmAccount", "relatedLead", "relatedCase", "relatedOpportunity", "relatedProject", "relatedTask", "relatedIssue", "relatedEmployee", "relatedDepartment", "relatedClient", "relatedSupplier", "relatedCandidate", "createdBy"),
    SchemaFacetFilter("free"),
    ProductsServicesFacetFilter("account", "type", "unitprice", "category", "supplier", "unitmeasurement", "brand", "inventorytype", "status", "purchaseaccount", "assetaccount", "warehouseId", "discountType"),
    ProductsCategoriesFacetFilter( "name", "parent"),
    BenefitRequesFacetFilter( "requester", "type", "status", "approver"),
    PurchaseInvoiceFacetFilter("project", "supplier", "currency", "status", "dueamount", "paidamount", "type"),
    ExpenseReportsClaimsFacetFilter("project", "reporter", "accountant", "approver", "accountantstatus", "status", "amount", "supplier", "currency", "type"),
    CourseBookingFacetFilter("company", "location", "status", "type"),
    CourseScheduleFaceFilter("course", "location", "language", "status", "instructor"),
    DocumentFacetFilter("employee", "type"),
    CompanyDocumentFacetFilter("type"),
    EmployeeFacetFilter("department", "location", "position", "role", "status", "supervisor", "qualification", "positionType"),
    LeaveFacetFilter("employeeName", "reason", "status", "approver", "department", "position"),
    SinglePayrunFacetFilter("employee", "status", "approver", "total", "currency"),
    GroupPayrunFacetFilter("month", "year", "approver", "status", "preparer"),
    CashAdvanceFacetFilter("employee", "approver", "amount", "status"),
    AdditionalPaymentFacetFilter("month", "year", "creator", "approver", "total", "status", "type"),
    VacancyFacetFilter("jobTitle", "jobType", "JobFamily", "manager", "project", "position", "status", "requiredDegree", "department","currency"),
    EmployeeStepFacetFilter("status", "employeeLocation", "type"),
    ChartOfAccountFacetFilter("parent", "type", "category", "status"),
    CustomFormItemFacetFilter("creator", "updater", "status"),
    GDNFacetFilter("customer", "invoicestatus", "status", "creator", "fromSQOrSO"),
    GRNFacetFilter("customer", "invoicestatus", "status", "creator"),
    RFQFacetFilter("project", "status", "client", "country"),
    CertificateFilter("employee", "type", "issuedBy", "status", "createdBy"),
    PositionFilter("department", "location", "type", "status"),
    DepartmentFilter( "location","department");

    FacetContentType(String... contentCode) {
        this.contentCode = contentCode;
    }

    /**
     * Each content name must unique name
     */
    private final String[] contentCode;

    public String[] getContentCode() {
        return contentCode;
    }
}
