package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEntity;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsGrade;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.ConversionBalanceManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactItemParamsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmEntityMailListManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EntityManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.GradeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceTermsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.SkillManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.NewTeam;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.PaymentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SalesInvoiceAddTO;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.google.api.client.util.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.EmailType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Email;
import ezvcard.property.Telephone;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 5:06:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Service("importingService")
public class ImportingServiceImpl implements ImportingServiceLocal, Constants, Errors {

    private final int flushLimit = 20;
    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");

    private static final Logger log = LoggerFactory.getLogger(ImportingServiceImpl.class);
    @Autowired
    private CRMService crmService;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingService;
    @Autowired
    private ConversionBalanceManager conversionBalanceManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmContactItemParamsManager crmContactItemParamsManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private GradeManager gradeManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyManager companyManager;
    //LEAD Import Start
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private ExpenseManager expenseManager;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private UnitMeasurementManager unitMeasurementManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private PayrollCategoryManager payrollCategoryManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private CrmEntityMailListManager crmEntityMailListManager;
    @Autowired
    private PayrollServiceLocal payrollService;
    @Autowired
    private ManualEntryServiceLocal manualEntryServiceLocal;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private SkillManager skillManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private AccountingService accountingServiceImpl;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    private final int contactType = ContactListItem.EMPLOYEE_CONTACT;

    private EdsCrmCustomFields createCustomField(EdsCrmCustomFields customField, CompanyCustomFieldItem[] items) {
        if (items != null && items.length > 0) {
            return contactServiceLocal.saveCustomFields(customField, Arrays.asList(items));
        }
        return null;
    }

    private EdsReference getReference(Map<String, Integer> map, String key) {
        Integer objectID = map.get(key);
        if (objectID != null) {
            return referenceManager.get(objectID);
        }
        return null;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importAccounts(ImportFile importFile, List dataBank, String from) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;
        EdsUser employee = userManager.get(importFile.getUserID());
        String type = ImportTypeEnum.SUPPLIER.equals(importFile.getType()) ? EdsCrmAccount.SUPPLIER : ImportTypeEnum.CUSTOMER.equals(importFile.getType()) ? EdsCrmAccount.CUSTOMER : "";
        boolean isLeadImport = ImportCustomEventListenerImpl.EVENT_IMPORT_LEAD.equals(from);
        boolean isContactImport = ImportCustomEventListenerImpl.EVENT_IMPORT_CONTACT.equals(from);
        boolean isOpportunityImport = ImportCustomEventListenerImpl.EVENT_IMPORT_OPPORTUNITY.equals(from);
        //Account Information
        int FIELD_NAME = importFile.getColumnID((isContactImport || isLeadImport) ? ImportField.ContactField.FIELD_ACCOUNT : isOpportunityImport ? ImportField.Opportunity.ACCOUNT_NAME : ImportField.CrmAccountField.FIELD_NAME);
        int FIELD_TYPE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_TYPE);
        int FIELD_PARENT = importFile.getColumnID(ImportField.CrmAccountField.FIELD_PARENT);
        int FIELD_NUMBER = importFile.getColumnID(ImportField.CrmAccountField.FIELD_NUMBER);
        int FIELD_INDUSTRY = importFile.getColumnID(ImportField.CrmAccountField.FIELD_INDUSTRY);
        int FIELD_EMAIL = importFile.getColumnID(ImportField.CrmAccountField.FIELD_EMAIL);
        int FIELD_PHONE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_PHONE);
        int FIELD_FAX = importFile.getColumnID(ImportField.CrmAccountField.FIELD_FAX);
        int FIELD_WEBSITE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_WEBSITE);
        //Address Information
        HashMap<Integer, HashMap<String, ArrayList<Integer>>> FIELD_ADDRESSES = importFile.getExtraColumnsAsMapList(ImportField.CrmAccountField.FIELD_ADDRESSES);
        Set<Integer> addressColumns = new HashSet<>();
        if (FIELD_ADDRESSES != null && FIELD_ADDRESSES.size() > 0) {
            for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> entry : FIELD_ADDRESSES.entrySet()) {
                if (entry != null && entry.getValue() != null) {
                    for (String columns : entry.getValue().keySet()) {
                        if (columns != null) {
                            for (String columnID_ : columns.split(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                                if (columnID_.matches(Constants.REGEX_INTEGER)) {
                                    addressColumns.add(Integer.parseInt(columnID_));
                                }
                            }
                        }
                    }
                }
            }
        }
        //Financial Information
        int FIELD_CURRENCY = importFile.getColumnID(ImportField.CrmAccountField.FIELD_CURRENCY);
        int FIELD_VAT_NUMBER = importFile.getColumnID(ImportField.CrmAccountField.FIELD_VAT_NUMBER);
        int FIELD_REGISTRATION_NUMBER = importFile.getColumnID(ImportField.CrmAccountField.FIELD_REGISTRATION_NUMBER);
        int FIELD_TERMS = importFile.getColumnID(ImportField.CrmAccountField.FIELD_TERMS);
        int FIELD_NOTE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_NOTE);
        int FIELD_PAYMENT_METHOD = importFile.getColumnID(ImportField.CrmAccountField.FIELD_PAYMENT_METHOD);
        int FIELD_BALANCE_DATE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_BALANCE_DATE);
        int FIELD_BALANCE_AMOUNT = importFile.getColumnID(ImportField.CrmAccountField.FIELD_BALANCE_AMOUNT);
        int FIELD_CREDIT_LIMIT = importFile.getColumnID(ImportField.CrmAccountField.FIELD_CREDIT_LIMIT);
        int FIELD_CLIENT_TYPE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_CLIENT_TYPE);
        //Supplier Bank Information
        int FIELD_BANK_NAME = importFile.getColumnID(ImportField.CrmAccountField.FIELD_BANK_NAME);
        int FIELD_ACCOUNT_NAME = importFile.getColumnID(ImportField.CrmAccountField.FIELD_ACCOUNT_NAME);
        int FIELD_ACCOUNT_NO = importFile.getColumnID(ImportField.CrmAccountField.FIELD_ACCOUNT_NO);
        int FIELD_SWIFT_CODE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_SWIFT_CODE);
        int FIELD_SORT_CODE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_SORT_CODE);
        int FIELD_IBAN_CODE = importFile.getColumnID(ImportField.CrmAccountField.FIELD_IBAN_CODE);
        int FIELD_BRANCH = importFile.getColumnID(ImportField.CrmAccountField.FIELD_BRANCH);
        int FIELD_BANK_ADDRESS = importFile.getColumnID(ImportField.CrmAccountField.FIELD_BANK_ADDRESS);

        Map<String, Integer> existingAccounts = crmAccountManager.getMapNumberAndName();
        Map<String, Integer> accountsInTheSystem = ServerUtils.mapNameIDs(crmAccountManager.getListWithAccountNumber());
        Map<String, Integer> typesIDs = listToMapIDs(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE));
        Map<String, Integer> clientTypeIDs = listToMapIDs(referenceManager.listReferences("CLIENT_TYPES"));
        Map<String, Integer> industriesIDs = listToMapIDs(referenceManager.listReferences(_COMPANY_WORKAREA));
        Map<String, Integer> countriesIDs = ServerUtils.listToMapCountryIDs(countryManager.list());
        Map<String, Integer> regionsIDs = ServerUtils.listToMapRegionIDs(regionManager.list());
        Map<String, Integer> paymentMethodsIDs = listToMapIDs(paymentMethodManager.list());
        Map<String, Integer> currenciesIDs = listToMapIDs(currencyManager.getAllCurrency());
        Map<String, Integer> termsMap = invoiceTermsManager.getAsMap();
        DecimalFormat decimalFormatForParse = new DecimalFormat(",##0.00");

        boolean hasHeader = importFile.isHasHeader();
        Integer lastGeneratedNumber = null;
        Map<String, EdsCrmAccount> accounts = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        format.setLenient(false);
        nextRow:
        for (String[] row : (List<String[]>) dataBank) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isRowEmpty = true;
            boolean hasError = false;
            boolean duplicateDetected = false;
            String detectedNumber = null;
            EdsCrmAccount account = null;
            int columnID = 0;
            for (String columnValue : row) {
                rejectedRow[columnID] = new RejectedImportRecord(columnValue);
                if (isValid(columnValue)) {
                    isRowEmpty = false;
                    if (columnID == FIELD_NUMBER) {
                        if (!duplicateDetected && (accountsInTheSystem.containsKey(columnValue) || accounts.containsKey(columnValue))) {
                            duplicateDetected = true;
                            detectedNumber = columnValue;
                        }
                    }
                }
                columnID++;
            }
            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue nextRow;
            }
            if (isRowEmpty) {
                continue nextRow;
            }
            if (duplicateDetected) {
                if ("".equals(from)) {
                    if (importFile.isSkip()) {
                        rejectedRecords.add(rejectedRow);
                        importFile.setSkippedColumns(importFile.getSkippedColumns() + 1);
                        continue nextRow;
                    } else if (importFile.isMerge()) {
                        if (accountsInTheSystem.get(detectedNumber) != null && accountsInTheSystem.get(detectedNumber) > 0) {
                            account = crmAccountManager.get(accountsInTheSystem.get(detectedNumber));
                        }
                    }
                } else {
                    continue nextRow;
                }
            }
            if (account == null) {
                account = new EdsCrmAccount();
                //account.setOwner(employee);
                //account.setOwners(Collections.singletonList(employee));
                EdsAddress billingAddress = new EdsAddress();
                EdsAddress mailingAddress = new EdsAddress();
                account.setBillingAddress(billingAddress);
                account.setMailingAddress(mailingAddress);
            } else if (!importFile.isMerge()) {
                if (account.getBillingAddresses() != null && account.getBillingAddresses().size() > 0) {
                    for (EdsAddress addr : account.getBillingAddresses()) {
                        addr.setDeleted(true);
                        addressManager.update(addr);
                    }
                    account.getBillingAddresses().clear();
                }
                if (account.getMailingAddresses() != null && account.getMailingAddresses().size() > 0) {
                    for (EdsAddress addr : account.getMailingAddresses()) {
                        addr.setDeleted(true);
                        addressManager.update(addr);
                    }
                    account.getMailingAddresses().clear();
                }
                if (account.getBillingAddress() != null) {
                    account.getBillingAddress().setDeleted(true);
                    addressManager.update(account.getBillingAddress());
                    account.setBillingAddress(null);
                }
                if (account.getMailingAddress() != null) {
                    account.getMailingAddress().setDeleted(true);
                    addressManager.update(account.getMailingAddress());
                    account.setMailingAddress(null);
                }
            }
            account.setImportFileID(importFile.getObjectID());
            List<CompanyCustomFieldItem> customFields = new ArrayList<>();
            Map<Integer, EdsAddress> addresses = new HashMap<>();
            columnID = 0;
            for (String columnValue : row) {
                if (isValid(columnValue)) {
                    columnValue = StringUtil.cut(columnValue, 255).trim();
                    String columnLowerCase = columnValue.toLowerCase();
                    if (columnID == FIELD_NAME) {
                        account.setName(columnValue);
                    }
                    if (columnID == FIELD_NUMBER) {
                        account.setNumber(columnValue);
                        accountsInTheSystem.put(columnValue, account.getObjectID() == null ? -1 : account.getObjectID());
                    }
                    if ("".equals(from)) {
                        //Information
                        if (columnID == FIELD_PARENT) {
                            Integer parentID = accountsInTheSystem.get(columnLowerCase);
                            if (parentID != null) {
                                account.setParent(crmAccountManager.get(parentID));
                            } else {
                                rejectedRow[FIELD_PARENT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_NUMBER) {
                            if (!importFile.isMerge() && existingAccounts.containsKey(columnValue)) {
                                String number = crmAccountManager.generateAccountNumber(type);
                                Integer intNumber = existingAccounts.get(number);
                                if (intNumber == null) {
                                    account.setNumber(number);
                                } else {
                                    account.setNumber(crmAccountManager.generateAccountNumber(type, intNumber));
                                }
                            } else {
                                account.setNumber(columnValue);
                            }
                        }
                        if (columnID == FIELD_TYPE) {
                            String wrongType = null;
                            for (String accountType : columnLowerCase.split(Constants.MULTIVALUE_SEPARATOR)) {
                                EdsReference reference = getReference(typesIDs, accountType);
                                if (reference != null) {
                                    account.addAccountType(reference);
                                } else {
                                    wrongType = wrongType == null ? "" : wrongType + ", ";
                                    wrongType += accountType;
                                }
                            }
                            if (wrongType != null) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, wrongType));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_CLIENT_TYPE) {
                            EdsReference reference = getReference(clientTypeIDs, columnLowerCase);
                            if (reference != null) {
                                account.setClientType(reference);
                            } else {
                                rejectedRow[FIELD_CLIENT_TYPE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_INDUSTRY) {
                            EdsReference reference = getReference(industriesIDs, columnLowerCase);
                            if (reference != null) {
                                account.setIndustry(reference);
                            } else {
                                rejectedRow[FIELD_INDUSTRY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_EMAIL) {
                            account.setEmail(columnValue);
                        }
                        if (columnID == FIELD_PHONE) {
                            account.setPhone(columnValue);
                        }
                        if (columnID == FIELD_FAX) {
                            account.setFax(columnValue);
                        }
                        if (columnID == FIELD_WEBSITE) {
                            account.setWebsite(columnValue);
                        }
                        //Address Information
                        if (FIELD_ADDRESSES != null && FIELD_ADDRESSES.size() > 0 && addressColumns.contains(columnID)) {
                            createAccountAddresses(addresses, countriesIDs, regionsIDs, columnID, columnValue, FIELD_ADDRESSES, isContactImport);
                        }
                        if (columnID == FIELD_CURRENCY) {
                            Integer accountID = currenciesIDs.get(columnLowerCase);
                            if (accountID != null) {
                                account.setCurrency(currencyManager.get(accountID));
                            } else {
                                rejectedRow[FIELD_CURRENCY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_VAT_NUMBER) {
                            account.setVatNumber(columnValue);
                        }
                        if (columnID == FIELD_PAYMENT_METHOD) {
                            Integer paymentID = paymentMethodsIDs.get(columnLowerCase);
                            if (paymentID != null) {
                                account.setPaymentMethod(paymentMethodManager.get(paymentID));
                            } else {
                                rejectedRow[FIELD_PAYMENT_METHOD].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_REGISTRATION_NUMBER) {
                            account.setRegistrationNumber(columnValue);
                        }
                        if (columnID == FIELD_TERMS) {
                            Integer termsId = termsMap.get(columnLowerCase);
                            if (termsId != null) {
                                account.setTerms(invoiceTermsManager.get(termsId));
                            } else {
                                rejectedRow[FIELD_TERMS].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_CREDIT_LIMIT) {
                            try {
                                account.setCreditLimit(BigDecimal.valueOf(decimalFormatForParse.parse(columnValue).doubleValue()));
                            } catch (Exception e) {
                                rejectedRow[FIELD_BALANCE_AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_BALANCE_AMOUNT) {
                            try {
                                if (EdsCrmAccount.SUPPLIER.equals(type)) {
                                    account.setSupplierBalanceAmount(BigDecimal.valueOf(decimalFormatForParse.parse(columnValue).doubleValue()));
                                } else {
                                    account.setBalanceAmount(BigDecimal.valueOf(decimalFormatForParse.parse(columnValue).doubleValue()));
                                }
                            } catch (Exception e) {
                                rejectedRow[FIELD_BALANCE_AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, columnValue));
                                hasError = true;
                            }
                        }
                        if (columnID == FIELD_NOTE) {
                            account.setNote(columnValue);
                        }
                        if (columnID == FIELD_BANK_NAME) {
                            account.setBankName(columnValue);
                        }
                        if (columnID == FIELD_ACCOUNT_NAME) {
                            account.setAccountName(columnValue);
                        }
                        if (columnID == FIELD_ACCOUNT_NO) {
                            account.setAccountNo(columnValue);
                        }
                        if (columnID == FIELD_SWIFT_CODE) {
                            account.setSwiftCode(columnValue);
                        }
                        if (columnID == FIELD_SORT_CODE) {
                            account.setSortCode(columnValue);
                        }
                        if (columnID == FIELD_IBAN_CODE) {
                            account.setIbanCode(columnValue);
                        }
                        if (columnID == FIELD_BRANCH) {
                            account.setBranch(columnValue);
                        }
                        if (columnID == FIELD_BANK_ADDRESS) {
                            account.setBankAddress(columnValue);
                        }
                        if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                            for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                                if (columnID != importFile.getExtraColumnID(extraColumnEntry.getValue()) || extraColumnEntry.getKey() < ImportField.ContactField.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                    continue;
                                }
                                if (!rejectedRecords.isEmpty()) {
                                    CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, columnID, columnValue, rejectedRow, rejectedRecords.get(0)[columnID].getData());
                                    if (customField == null) {
                                        hasError = true;
                                        continue;
                                    }
                                    customFields.add(customField);
                                }
                            }
                        }
                    }
                } else if (columnID == FIELD_NAME) {
                    rejectedRow[FIELD_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, columnValue));
                    hasError = true;
                }
                columnID++;
            }
            if (hasError) {
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                rejectedRecords.add(rejectedRow);
                continue nextRow;
            }
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (account.getCurrency() == null && financialSettings != null) {
                account.setCurrency(financialSettings.getCurrency());
            }
            account.setTransientCustomFields(customFields.toArray(new CompanyCustomFieldItem[]{}));
            for (EdsAddress addr : addresses.values()) {
                if (isContactImport || EdsAddress.BILLING_ADDRESS.equals(addr.getRelationType())) {
                    if (importFile.isMerge()) {
                        account.setImportBillingAddress(addr);
                    } else {
                        account.addBillAddressTransient(addr);
                    }
                } else if (EdsAddress.MAILING_ADDRESS.equals(addr.getRelationType())) {
                    if (importFile.isMerge()) {
                        account.setImportMailingAddress(addr);
                    } else {
                        account.addMailAddressTransient(addr);
                    }
                }
            }
            if (account.getNumber() == null) {
                NumberData numberData = crmAccountManager.generateAccountNumberData(type, lastGeneratedNumber);
                lastGeneratedNumber = numberData.getIntNumber();
                account.setNumber(numberData.getNumberString());
            }
            if (isValid(account.getName())) {
                if (account.getCreator() == null) {
                    account.setCreator(employee);
                }
            }
            if (isValid(account.getNumber())) {
                accounts.put(account.getNumber(), account);
                existingAccounts.put(account.getNumber(), account.getNumberInteger());
            }
            accounts = batchImportCrmAccount(accounts, false, accountsInTheSystem, type, !(isContactImport || isOpportunityImport), importFile.getConversionDate());
            account.setLastUpdateTime(new Date());
            if (duplicateDetected) {
                if (importFile.isClone()) {
                    importFile.setClonedColumns(importFile.getClonedColumns() + 1);
                }
                if (importFile.isMerge()) {
                    importFile.setOverwrittenColumns(importFile.getOverwrittenColumns() + 1);
                }
            }
        }
        batchImportCrmAccount(accounts, true, accountsInTheSystem, type, !(isContactImport || isOpportunityImport), importFile.getConversionDate());
        return rejectedRecords;
    }

    @Override
    @Transactional
    public void importVCardAccounts(ImportFile importFile, List<VCard> items, String type, String from) {
        EdsEmployee employee = employeeManager.get(importFile.getUserID());
        boolean isContactImport = ImportCustomEventListenerImpl.EVENT_IMPORT_CONTACT.equals(from);
        boolean isOpportunityImport = ImportCustomEventListenerImpl.EVENT_IMPORT_OPPORTUNITY.equals(from);
        importFile.setDuplicateAction(isContactImport ? ImportFile.MERGE : importFile.getDuplicateAction());
        Map<String, Integer> accountsInTheSystem = ServerUtils.mapNameIDs(crmAccountManager.getList());

        Map<String, EdsCrmAccount> accounts = new HashMap<>();

        for (VCard vCard : items) {
            if (vCard.getOrganization() == null || vCard.getOrganization().toString().isEmpty() || vCard.getOrganization().getValues().isEmpty()) {
                continue;
            }
            EdsCrmAccount account = null;
            String accountName = vCard.getOrganization().getValues().get(0);
            if (accountsInTheSystem.containsKey(accountName) || accounts.containsKey(accountName)) {

                if (accountsInTheSystem.get(accountName) != null && accountsInTheSystem.get(accountName) > 0) {
                    account = crmAccountManager.get(accountsInTheSystem.get(accountName));
                }
                if (account == null && accounts.containsKey(accountName) && accounts.get(accountName) != null) {
                    account = accounts.get(accountName);
                }
            }
            if (account != null) {
                continue;
            }

            account = new EdsCrmAccount();

            //account.setOwner(employee);
            //account.setOwners(Collections.singletonList(employee));

            account.setImportFileID(importFile.getObjectID());

            String orgName = vCard.getOrganization().getValues().get(0);
            account.setName(orgName);
            accountsInTheSystem.put(orgName.toLowerCase().trim(), account.getObjectID() == null ? -1 : account.getObjectID());

            if (!isContactImport) {
                if (vCard.getEmails() != null && vCard.getEmails().size() > 0) {
                    account.setEmail(vCard.getEmails().get(0).getValue());
                }
                for (Telephone telephone : vCard.getTelephoneNumbers()) {
                    for (TelephoneType telephoneType : telephone.getTypes()) {
                        if (telephoneType.equals(TelephoneType.CELL)) {
                            account.setPhone(telephone.getText());
                        }
                        if (telephoneType.equals(TelephoneType.FAX)) {
                            account.setFax(telephone.getText());
                        }
                    }
                }

                if (vCard.getUrls() != null && vCard.getUrls().size() > 0) {
                    account.setWebsite(vCard.getUrls().get(0).getValue());
                }
            }

            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (account.getCurrency() == null && financialSettings != null) {
                account.setCurrency(financialSettings.getCurrency());
            }

            if (account.getName() != null && !"".equals(account.getName())) {
                accounts.put(account.getName().toLowerCase().trim(), account);
            }
            accounts = batchImportCrmAccount(accounts, false, accountsInTheSystem, type, !(isContactImport || isOpportunityImport), null);
            account.setLastUpdateTime(new Date());
        }
        batchImportCrmAccount(accounts, true, accountsInTheSystem, type, !(isContactImport || isOpportunityImport), null);
    }

    @Transactional
    public Map<String, EdsCrmAccount> batchImportCrmAccount(Map<String, EdsCrmAccount> accounts, boolean forceToCommit, final Map<String, Integer> accountsInTheSystem, String type, boolean fromAccountImport, Date conversionBalanceDate) {
        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        if (accounts.size() == flushLimit || (forceToCommit && accounts.size() > 0)) {
            for (EdsCrmAccount account : accounts.values()) {
                if (account.getEntityID() == null) {
                    EdsEntity entity = new EdsEntity();
                    entityManager.create(entity);
                    account.setEntityID(entity.getObjectID());
                }
                if (!"".equals(type)) {
                    account.getAccountTypes().add(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, type));
                }
                if (fromAccountImport) {
                    account.setCustomFields(createCustomField(account.getCustomFields(), account.getTransientCustomFields()));
                }
                if (EdsCrmAccount.CUSTOMER.equals(type)) {
                    account.setBalanceDate(conversionBalanceDate != null ? conversionBalanceDate : edsFinancialSettings.getConversionDate());
                } else if (EdsCrmAccount.SUPPLIER.equals(type)) {
                    account.setSupplierBalanceDate(conversionBalanceDate != null ? conversionBalanceDate : edsFinancialSettings.getConversionDate());
                }
                crmAccountManager.createOrUpdate(account);
                List<EdsAddress> billAddresses = account.getBillAddressesTransient(), mailAddresses = account.getMailAddressesTransient();
                if (billAddresses.size() == 0) {
                    billAddresses.add(account.getBillingAddress(true));
                }
                if (mailAddresses.size() == 0) {
                    mailAddresses.add(account.getMailingAddress(true));
                }
                int i = 0;
                for (EdsAddress addr : billAddresses) {
                    if (addr.getName() == null || "".equals(addr.getName().trim())) {
                        addr.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
                    }
                    addr.setCrmAccount(account);
                    addressManager.createOrUpdate(addr);
                    if (i == 0) {
                        account.setBillingAddress(addr);
                    }
                    i++;
                }
                i = 0;
                for (EdsAddress addr : mailAddresses) {
                    if (addr.getName() == null || "".equals(addr.getName().trim())) {
                        addr.setName(commonLocalizer.localize(PdfLocalizationName.mailingAddress, "Mailing Address"));
                    }
                    addr.setCrmAccount(account);
                    addressManager.createOrUpdate(addr);
                    if (i == 0) {
                        account.setMailingAddress(addr);
                    }
                    i++;
                }

                if (EdsCrmAccount.CUSTOMER.equals(type)) {
                    //accountingService.createOrUpdateCustomerTransaction(account.getObjectID(), account.getOwner());
                    accountingService.createOrUpdateCustomerTransaction(account.getObjectID(), account.getCreator());
                } else if (EdsCrmAccount.SUPPLIER.equals(type)) {
                    //accountingService.createOrUpdateSupplierTransaction(account.getObjectID(), account.getOwner());
                    accountingService.createOrUpdateSupplierTransaction(account.getObjectID(), account.getCreator());
                }
                if (account.getNote() != null && !"".equals(account.getNote())) {
                    crmService.saveCrmNote(RelationItem.TYPE_CRM_ACCOUNT, account.getObjectID(), new HistoryListItem(account.getNote()));
                }

                accountsInTheSystem.put(fromAccountImport ? account.getNumber() : account.getName(), account.getObjectID());
            }
            userManager.flushAndClear();
            accounts = new HashMap<>();
        }
        return accounts;
    }

    @Transactional
    public ArrayList<RejectedImportRecord[]> importOpportunities(ImportFile importFile, List<String[]> rows) {

        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<RejectedImportRecord[]>();

        Map<String, Integer> leadSources = listToMapIDs(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE));
        Map<String, Integer> types = listToMapIDs(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_TYPE));
        Map<String, Integer> stages = listToMapIDs(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE));
        Map<String, Integer> campaignSources = ServerUtils.mapNameIDs(campaignManager.getList());
        Map<String, EdsCrmAccount> accounts = Maps.newHashMap();//crmAccountManager.getAllCrmAccountsMapStringInteger();
        Map<String, EdsCrmContact> contacts = Maps.newHashMap();//crmContactManager.getContactsForImport(EdsCrmContact.LEAD_CONTACT);
        int FIELD_NAME = importFile.getColumnID(ImportField.Opportunity.NAME);
        int FIELD_ASSIGNEE = importFile.getColumnID(ImportField.Opportunity.ASSIGNEE);
//        int FIELD_NUMBER = importFile.getColumnID(ImportField.Opportunity.NUMBER);
        int FIELD_ACCOUNT_NAME = importFile.getColumnID(ImportField.Opportunity.ACCOUNT_NAME);
        int FIELD_CONTACT_NAME = importFile.getColumnID(ImportField.Opportunity.CONTACT_NAME);
        int FIELD_TYPE = importFile.getColumnID(ImportField.Opportunity.TYPE);
        int FIELD_NEXT_STEP = importFile.getColumnID(ImportField.Opportunity.NEXT_STEP);
        int FIELD_AMOUNT = importFile.getColumnID(ImportField.Opportunity.AMOUNT);
        int FIELD_CLOSING_DATE = importFile.getColumnID(ImportField.Opportunity.CLOSING_DATE);
        int FIELD_STAGE = importFile.getColumnID(ImportField.Opportunity.STAGE);
        int FIELD_PROBABILITY = importFile.getColumnID(ImportField.Opportunity.PROBABILITY);
        int FIELD_EXPECTED_REVENUE = importFile.getColumnID(ImportField.Opportunity.EXPECTED_REVENUE);
        int FIELD_CAMPAIGN_SOURCE = importFile.getColumnID(ImportField.Opportunity.CAMPAIGN_SOURCE);
        int FIELD_LEAD_SOURCE = importFile.getColumnID(ImportField.Opportunity.LEAD_SOURCE);
        int FIELD_NOTE = importFile.getColumnID(ImportField.Opportunity.NOTE);
        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        List<EdsOpportunity> opportunities = new ArrayList<>();

        //Check Required Fields
        if (FIELD_NAME >= 0 || FIELD_STAGE >= 0 || FIELD_CLOSING_DATE >= 0) {
            nextRow:
            for (String[] row : rows) {
                List<CompanyCustomFieldItem> customFields = new ArrayList<>();
                if (hasHeader) {
                    RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                    int cellIndex = 0;
                    for (String cellValue : row) {
                        rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                    }
                    //Add Header row into final result
                    rejectedRows.add(rejectedCells);
                    hasHeader = false;
                    continue nextRow;
                }
                EdsOpportunity edsOpportunity = null;
                boolean rowIsEmpty = true;//To check if row is not blank

                for (String cellvalue : row) {
                    if (rowIsEmpty && StringUtils.isNotBlank(cellvalue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue nextRow;
                }
                if (importFile.isClone() || edsOpportunity == null) {
                    edsOpportunity = new EdsOpportunity();
                }

                //We set this value globally for whole import, means all rows will use same value.
                if (FIELD_ASSIGNEE != -1) {
                    edsOpportunity.setAssignee(employeeManager.get(FIELD_ASSIGNEE));
                }
                edsOpportunity.setImportFileID(importFile.getObjectID());
                Integer csvColumnID = 0;
                int errors = 0;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                String crmAccount = null;
                String opportunityName = null;
                String contactName = null;
                String type = null;
                String nextStep = null;
                String amount = null;
                String closingDate = null;
                String stage = null;
                String probability = null;
                String expectedRevenue = null;
                String campaign = null;
                String leadSource = null;
                String note = null;

                for (String cellValue : row) {

                    rejectedCells[csvColumnID] = new RejectedImportRecord(cellValue);
                    //We will process only if CellValue is not blank
                    if (StringUtils.isNotBlank(cellValue)) {
                        //Trim value
                        cellValue = cellValue.trim();

                        if (cellValue != null && cellValue.length() > 250) {
                            System.out.println("!!!!!!!!!!!!!" + csvColumnID + ":::" + cellValue + ":::" + cellValue.length());
                            cellValue = cellValue.substring(0, 250).trim();
                        }

                        //Account Name
                        if (csvColumnID == FIELD_ACCOUNT_NAME) {
                            crmAccount = cellValue;
                        }

                        //Opportunity Name
                        if (csvColumnID == FIELD_NAME) {
                            opportunityName = cellValue;
                        }
                        //Crm Contact Name
                        if (csvColumnID == FIELD_CONTACT_NAME) {
                            contactName = cellValue;
                        }
                        if (csvColumnID == FIELD_TYPE) {
                            type = cellValue;
                        }
                        if (csvColumnID == FIELD_NEXT_STEP) {
                            edsOpportunity.setNextStep(cellValue);
                        }
                        if (csvColumnID == FIELD_AMOUNT) {
                            amount = cellValue;
                        }
                        if (csvColumnID == FIELD_CLOSING_DATE) {
                            closingDate = cellValue;
                        }
                        if (csvColumnID == FIELD_STAGE) {
                            stage = cellValue;
                        }
                        if (csvColumnID == FIELD_PROBABILITY) {
                            probability = cellValue;
                        }
                        if (csvColumnID == FIELD_EXPECTED_REVENUE) {
                            expectedRevenue = cellValue;
                        }
                        if (csvColumnID == FIELD_CAMPAIGN_SOURCE) {
                            campaign = cellValue;
                        }
                        if (csvColumnID == FIELD_LEAD_SOURCE) {
                            leadSource = cellValue;
                        }
                        if (csvColumnID == FIELD_NOTE) {
                            note = cellValue;
                        }
                        if (importFile.getExtraColumns() != null) {
                            for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {

                                if (!csvColumnID.equals(importFile.getExtraColumnID(extraColumnEntry.getValue()))
                                        || extraColumnEntry.getKey() < ImportField.Opportunity.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                    continue;
                                }

                                CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry,
                                        csvColumnID, cellValue, rejectedCells, rejectedCells[csvColumnID].getData());

                                if (customField == null) {
                                    errors++;
                                    break;
                                }

                                customFields.add(customField);
                            }
                        }
                        /*if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                            for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                                if (!(csvColumnID != importFile.getExtraColumnID(extraColumnEntry.getValue()) || extraColumnEntry.getKey() < ImportField.Opportunity.FIELD_CUSTOM_FIELD_START_NUMBER)) {
                                    CompanyCustomFieldItem customField = new CompanyCustomFieldItem();
                                    customField.setDataType(importFile.getExtraColumnValues(extraColumnEntry.getValue())[1]);
                                    customField.setColumnCode(importFile.getExtraColumnValues(extraColumnEntry.getValue())[2]);

                                    if (CompanyCustomFieldItem.DATE.equals(customField.getDataType())) {
                                        try {
                                            format.parse(cellValue);
                                        } catch (ParseException e) {
                                            errors++;
                                            rejectedCells[csvColumnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                                            break;
                                        }
                                    }

                                    try {
                                        customField.setCustomFieldSettingID(Integer.parseInt(importFile.getExtraColumnValues(extraColumnEntry.getValue())[3]));
                                    } catch (NumberFormatException e) {
                                        System.out.print(e.getMessage());
                                    }
                                    if (importFile.getExtraColumnValues(extraColumnEntry.getValue()).length > 4) {
                                        customField.setPredefinedValues(importFile.getExtraColumnValues(extraColumnEntry.getValue())[4]);
                                    }
                                    EdsCustomFields.setValueByDataType(customField, cellValue);
                                    customFields.add(customField);
                                }
                            }
                        }*/
                    }
                    csvColumnID++;
                }

                //Set Custom Fields
                if (customFields != null && !customFields.isEmpty()) {
                    edsOpportunity.setTransientCustomFields(customFields.toArray(new CompanyCustomFieldItem[]{}));
                }

                //CRMACCOUNT
                EdsCrmAccount edsCrmAccount = null;
                if (StringUtils.isNotBlank(crmAccount)) {
                    //If Account exist in map use that value (accountID)
                    edsCrmAccount = accounts.get(crmAccount.toLowerCase().trim());
                    if (edsCrmAccount == null) {
                        edsCrmAccount = crmAccountManager.getCrmAccountByName(crmAccount.toLowerCase().trim());//AllCrmAccountsMapStringInteger();
                        if (edsCrmAccount != null) {
                            //Add to map
                            accounts.put(crmAccount.toLowerCase().trim(), edsCrmAccount);
                        }
                    }

                }
                if (edsCrmAccount != null) {
                    edsOpportunity.setCrmAccount(edsCrmAccount);
                    edsOpportunity.setEntityID(edsCrmAccount.getEntityID());
                } else {
                    errors++;
                    rejectedCells[FIELD_ACCOUNT_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, crmAccount));
                }
                //End of CRMACCOUNT
                //Set Name which is required
                if (StringUtils.isNotBlank(opportunityName)) {
                    edsOpportunity.setName(opportunityName);
                } else {
                    errors++;
                    rejectedCells[FIELD_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, opportunityName));
                }

                //CRMCONTACT
                if (StringUtils.isNotBlank(contactName)) {
                    EdsCrmContact contact = null;
                    //If Contact exist in map use that value (contactID)
                    contact = contacts.get(contactName.toLowerCase().trim());
                    if (contact == null) {
                        //otherwise try to retrieve from database
                        Integer contactID = crmContactManager.findContactIdByNameAndCrmAccount((edsCrmAccount != null ? edsCrmAccount.getObjectID() : null),
                                contactName.toLowerCase().trim(), EdsCrmContact.LEAD_CONTACT, EdsCrmContact.CANDIDATE);
                        if (contactID != null) {
                            contact = crmContactManager.get(contactID);
                            //Put into map
                            contacts.put(contactName.toLowerCase().trim(), contact);
                        }
                    }
                    if (contact != null) {
                        edsOpportunity.setCrmContact(contact);
                    } else {
                        //We must create contact if it doesnt exist
                        ContactListItem newContact = new ContactListItem();
                        newContact.setFirstName(contactName);
                        newContact.setContactType(EdsCrmContact.CRM_CONTACT);
                        if (edsCrmAccount != null) {
                            CrmAccountItem crmAccountItem = edsCrmAccount.getRPC(null, true);
                            newContact.setCrmAccount(crmAccountItem);
                        }
                        Integer newContactId = contactServiceLocal.saveContact(newContact, null, crmContactManager.getUser(), true, true);
                        if (newContactId != null && newContactId > 0) {
                            edsOpportunity.setCrmContact(crmContactManager.get(newContactId));
                        }
//                        errors++;
//                        rejectedCells[FIELD_CONTACT_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, contactName));
                    }
                }

                //End Of CRMCONTACT
                //TYPE (Not Required)
                Integer opportunityTypeId = null;
                if (StringUtils.isNotBlank(type)) {
                    opportunityTypeId = types.get(type.toLowerCase().trim());
                    if (opportunityTypeId != null) {
                        edsOpportunity.setType(referenceManager.get(opportunityTypeId));
                    } else {
                        errors++;
                        rejectedCells[FIELD_TYPE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, type));
                    }
                }
                //End Of Type
                //AMOUNT (Not Required)
                try {
                    if (StringUtils.isNotBlank(amount)) {
                        edsOpportunity.setAmount(Double.valueOf(amount.replaceAll("[, ]", "")));
                        //DURING THE IMPORT WE SUPPOSE ITS BEING CREATED WITH COMPANIY's BASE CURRENCY
                        edsOpportunity.setAmountBaseCurrency(edsOpportunity.getAmount());
                    }
                } catch (Exception e) {
                    errors++;
                    rejectedCells[FIELD_AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, amount));
                }
                //End Of AMOUNT
                //ClosingDate (REQUIRED)
                try {
                    edsOpportunity.setClosingDate(format.parse(closingDate.toLowerCase().trim()));
                } catch (Exception e) {
                    errors++;
                    rejectedCells[FIELD_CLOSING_DATE].setComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                }
                //End Of ClosingDate

                //STAGE (REQUIRED)
                if (StringUtils.isNotBlank(stage)) {
                    Integer stageID = stages.get(stage.toLowerCase().trim());
                    if (stageID != null) {
                        edsOpportunity.setStage(referenceManager.get(stageID));
                    } else {
                        errors++;
                        rejectedCells[FIELD_STAGE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, stage));
                    }
                } else {
                    errors++;
                    rejectedCells[FIELD_STAGE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, stage));
                }
                //End of STAGE
                //PROBABILITY
                if (StringUtils.isNotBlank(probability)) {
                    try {
                        probability = probability.replaceAll("%|\\s", "");
                        edsOpportunity.setProbability(Float.valueOf(probability));
                    } catch (NumberFormatException e) {
                        errors++;
                        rejectedCells[FIELD_PROBABILITY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, probability));
                    }
                }
                //End of PROBABILITY
                //EXPECTED REVENUE (Not Required)
                if (StringUtils.isNotBlank(expectedRevenue)) {
                    try {
                        edsOpportunity.setExpectedRevenue(Double.valueOf(expectedRevenue.replaceAll("[, ]", "")));
                    } catch (NumberFormatException e) {
                        errors++;
                        rejectedCells[FIELD_EXPECTED_REVENUE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, expectedRevenue));
                    }
                }
                //End of EXPECTED REVENUE
                //CAMPAIGN SOURCE (Not Required)
                if (StringUtils.isNotBlank(campaign)) {
                    Integer campaignID = campaignSources.get(campaign.toLowerCase().trim());
                    if (campaignID != null) {
                        edsOpportunity.setCampaign(campaignManager.get(campaignID));
                    } else {
                        errors++;
                        rejectedCells[FIELD_CAMPAIGN_SOURCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, campaign));
                    }
                }
                //End of CAMPAIGN SOURCE
                //LEAD SOURCE (Not Required)
                if (StringUtils.isNotBlank(leadSource)) {
                    Integer leadSourceID = leadSources.get(leadSource.toLowerCase().trim());
                    if (leadSourceID != null) {
                        edsOpportunity.setLeadSource(referenceManager.get(leadSourceID));
                    } else {
                        errors++;
                        rejectedCells[FIELD_LEAD_SOURCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, leadSource));
                    }
                }
                //End of LEAD SOURCE
                //Note
                edsOpportunity.setNote(note);

                if (errors == 0) {
                    //Increment number of imported ROWs
                    importFile.setImportedColumns(importFile.getImportedColumns() + 1);

                    opportunities.add(edsOpportunity);
                    opportunities = batchImportOpportunity(opportunities, false);
                    if (opportunities.size() % flushLimit == 0) {
                        userManager.flushAndClear();
                        System.out.println(new Date());
                    }
                } else {
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                }
            }
        } else {
            //Required Fields are not mapped
            importFile.setIgnoredColumns(rows.size());
        }
        if (!opportunities.isEmpty()) {
            opportunities = batchImportOpportunity(opportunities, true);
            if (opportunities.size() % flushLimit == 0) {
                userManager.flushAndClear();
                System.out.println(new Date());
            }
        }
        return rejectedRows;
    }

    private boolean isValid(String s) {
        return !StringUtils.isEmpty(s);
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importEmployees(ImportFile importFile, List<String[]> list, String type, Integer companyId, Integer parentEmployeeId) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_PATTERN);
        formatter.setLenient(false);
        EdsUser edsUser = parentEmployeeId != null ? employeeManager.get(parentEmployeeId) : companyManager.getUser();

        EdsCompany edsCompany = edsUser.getCompany();
        EdsRole edsMemberRole = roleManager.getByCode(MEM_CODE);
        Integer edsMemberRoleId = edsMemberRole.getObjectID();
        EdsRole edsEssRole = roleManager.getByCode(ESS_USER_CODE);

        Integer isEssHasAccess = employeeServiceLocal.checkUserLimit(true, true, companyId);
        Integer isEssNotAccess = employeeServiceLocal.checkUserLimit(true, false, companyId);
        Integer isNotEssHassAccess = employeeServiceLocal.checkUserLimit(false, true, companyId);
        Integer isNotEssNotAccess = employeeServiceLocal.checkUserLimit(false, false, companyId);

        boolean isUpdate;
        int EMPLOYEE_CODE = importFile.getColumnID(ImportField.EmployeeField.EMPLOYEE_CODE);
        int SALUTATION = importFile.getColumnID(ImportField.EmployeeField.SALUTATION);
        int FIRST_NAME = importFile.getColumnID(ImportField.EmployeeField.FIRST_NAME);
        int LAST_NAME = importFile.getColumnID(ImportField.EmployeeField.LAST_NAME);
        int MIDDLE_NAME = importFile.getColumnID(ImportField.EmployeeField.MIDDLE_NAME);
        int DATE_OF_BIRTH = importFile.getColumnID(ImportField.EmployeeField.DATE_OF_BIRTH);
        int GENDER = importFile.getColumnID(ImportField.EmployeeField.GENGER);
        int NATIONALITY = importFile.getColumnID(ImportField.EmployeeField.NATIONALITY);
        int MARTIAL_STATUS = importFile.getColumnID(ImportField.EmployeeField.MARTIAL_STATUS);
        int SPOKEN_LANGUAGES = importFile.getColumnID(ImportField.EmployeeField.SPOKEN_LANGUAGES);

        int EMAIL_ID = -1;
        int IM_ADDRESS = importFile.getColumnID(ImportField.EmployeeField.IM_ADDRESS);
        int WEB_ADDRESS = importFile.getColumnID(ImportField.EmployeeField.WEB_ADDRESS);

        int ADDRESS_NAME = importFile.getColumnID(ImportField.EmployeeField.ADDRESS_NAME);
        int ADDRESS_LINE = importFile.getColumnID(ImportField.EmployeeField.ADDRESS_LINE);
        int ADDRESS_LINE2 = importFile.getColumnID(ImportField.EmployeeField.ADDRESS_LINE2);
        int ADDRESS_TYPE = importFile.getColumnID(ImportField.EmployeeField.ADDRESS_TYPE);
        int COUNTRY = importFile.getColumnID(ImportField.EmployeeField.COUNTRY);
        int STATE = importFile.getColumnID(ImportField.EmployeeField.STATE);
        int CITY = importFile.getColumnID(ImportField.EmployeeField.CITY);
        int POST_CODE = importFile.getColumnID(ImportField.EmployeeField.POST_CODE);

        int DEPARTMENT_NAME = importFile.getColumnID(ImportField.EmployeeField.DEPARTMENT_NAME);
        int POSITION = importFile.getColumnID(ImportField.EmployeeField.POSITION);
        int LOCATION = importFile.getColumnID(ImportField.EmployeeField.LOCATION);
        int SUPER_VISER = importFile.getColumnID(ImportField.EmployeeField.SUPER_VISER);
        int WAGE_RATE = importFile.getColumnID(ImportField.EmployeeField.WAGE_RATE);
        int CLIENT_CHARGE_RATE = importFile.getColumnID(ImportField.EmployeeField.CLIENT_CHARGE_RATE);
        int HIRE_DATE = importFile.getColumnID(ImportField.EmployeeField.HIRE_DATE);
        int RESIGNATION_DATE = importFile.getColumnID(ImportField.EmployeeField.RESIGNATION_DATE);
        int EMPLOYMENT_MODE = importFile.getColumnID(ImportField.EmployeeField.EMPLOYMENT_MODE);
        int EMPLOYMENT_CONTACT_TERMtS = importFile.getColumnID(ImportField.EmployeeField.EMPLOYMENT_CONTACT_TERMS);
        int SALARY_GRADE = importFile.getColumnID(ImportField.EmployeeField.SALARY_GRADE);
        int BASIC_SALARY = importFile.getColumnID(ImportField.EmployeeField.BASIC_SALARY);
        int QUALIFICATION = importFile.getColumnID(ImportField.EmployeeField.QUALIFICATION);
        int COMPETENCIES = importFile.getColumnID(ImportField.EmployeeField.COMPETENCIES);

        int BANK_NAME = importFile.getColumnID(ImportField.EmployeeField.BANK_NAME);
        int ACCOUNT_NUMBER = importFile.getColumnID(ImportField.EmployeeField.ACCOUNT_NUMBER);
        int ACCOUNT_NAME = importFile.getColumnID(ImportField.EmployeeField.ACCOUNT_NAME);
        int BANK_ADDRESS = importFile.getColumnID(ImportField.EmployeeField.BANK_ADDRESS);
        int SWIFT_BCIC_CODE = importFile.getColumnID(ImportField.EmployeeField.SWIFT_BCIC_CODE);
        int SORT_CODE = importFile.getColumnID(ImportField.EmployeeField.SORT_CODE);
        int IBAN_NUMBER = importFile.getColumnID(ImportField.EmployeeField.IBAN_NUMBER);
        int AGENT_ID = importFile.getColumnID(ImportField.EmployeeField.AGENT_ID);

        int PASSPORT_NUMBER = importFile.getColumnID(ImportField.EmployeeField.PASSPORT_NUMBER);
        int PASSPORT_ISSUE_BY = importFile.getColumnID(ImportField.EmployeeField.PASSPORT_ISSUE_BY);
        int PASSPORT_ISSUE_DATE = importFile.getColumnID(ImportField.EmployeeField.PASSPORT_ISSUE_DATE);
        int PASSPORT_EXPIRY_DATE = importFile.getColumnID(ImportField.EmployeeField.PASSPORT_EXPIRY_DATE);
        int INSURANCE_NUMBER = importFile.getColumnID(ImportField.EmployeeField.INSURANCE_NUMBER);
        int INSURANCE_EXPIRY_DATE = importFile.getColumnID(ImportField.EmployeeField.INSURANCE_EXPIRY_DATE);
        int VISA_NUMBER = importFile.getColumnID(ImportField.EmployeeField.VISA_NUMBER);
        int VISA_ISSUE_DATE = importFile.getColumnID(ImportField.EmployeeField.VISA_ISSUE_DATE);
        int VISA_EXPIRY_DATE = importFile.getColumnID(ImportField.EmployeeField.VISA_EXPIRY_DATE);
        int OPENING_BALANCE_DAYS = importFile.getColumnID(ImportField.EmployeeField.OPENING_BALANCE_DAYS);
        int PROBATION_PERIOD_DAYS = importFile.getColumnID(ImportField.EmployeeField.PROBATION_PERIOD_DAYS);

        int HAS_ACCESS = importFile.getColumnID(ImportField.EmployeeField.HAS_ACCESS);
        int USER_ROLE = importFile.getColumnID(ImportField.EmployeeField.USER_ROLE);
        int WPS_NO = importFile.getColumnID(ImportField.EmployeeField.WPS_NO);

        HashMap<Integer, Integer> categories = null;
        if (StringUtils.isNotBlank(importFile.getCategoryColumns())) {
            Gson gson = new Gson();
            Type dataType = new TypeToken<Map<Integer, Integer>>() {
            }.getType();
            categories = gson.fromJson(importFile.getCategoryColumns(), dataType);
        }

        Map<String, Integer> compRolesMap = listToMapIDs(roleManager.getRoleListByCompany(companyId));
        List<EdsDepartment> companyDepartments = departmentManager.getCompanyDepartments(edsCompany);
        Map<String, Integer> compDepartmentsMap = listToMapIDs(companyDepartments);
        for (EdsDepartment dep : companyDepartments) {
            if (dep.getNumberData() != null)
                compDepartmentsMap.put(dep.getNumberData().toLowerCase(), dep.getObjectID());
        }
        Map<String, Integer> compLocationsMap = listToMapIDs(locationManager.getLocations(new ListingFilterParameter()));
        Map<String, Integer> compMartialStMap = listToMapIDs(referenceManager.listReferences(EdsEmployeeProfile.MARTIAL_STATUS));
        Map<String, Integer> compEmployeeModeMap = listToMapIDs(referenceManager.listReferences(EdsEmployeeProfile.EMPLOYMENT_MODE));
        Map<String, Integer> qualificationsMap = listToMapIDs(referenceManager.listReferences(Constants.Q_QUALIFICATION));
        Map<String, Integer> countriesMap = ServerUtils.listToMapCountryIDs(countryManager.list());
        List<EdsPosition> positions = positionManager.getPositionList(new ListingFilterParameter());
        Map<String, Integer> positionMap = listToMapIDs(positions);
        for (EdsPosition pos : positions) {
            if (pos.getNumberData() != null)
                positionMap.put(pos.getNumberData().toLowerCase(), pos.getObjectID());
        }
        Map<String, Integer> competencyMap = listToMapIDs(skillManager.getSkillList(new ListingFilterParameter()));
        Map<String, EdsEmployee> employeeMapByCode = employeeManager.getEmployeeMapByCode();
        Map<String, Integer> regions = listToMapIDs(regionManager.list());
        Map<Integer, UserBankAccountData> employeeBankAccountByCode = employeeManager.getEmployeeBankAccountMap();
        HashMap<Integer, HashMap<String, String>> employeesPayrollSettingsMap = employeeManager.getEmployeesPayrollSettingsMap();

        List<EdsGrade> salaryGradeList = gradeManager.getGradeListByCompany(companyId);
        Map<String, Integer> gradeListMap = salaryGradeList.stream()
                .collect(Collectors.toMap(x -> x.getGradeCode().toLowerCase() + " " + x.getGradeLevel().toLowerCase(), EdsGrade::getObjectID));


        Map<Integer, Map<Integer, List<Integer>>> importingParams = new HashMap<>();
        Map<Integer, ArrayList<Integer>> FIELD_EMAILS = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_EMAILS);
        Set<Integer> emailColumns = new HashSet<>();
        List<Integer> homeEmails = null;
        List<Integer> workEmails = null;
        List<Integer> otherEmails = null;
        if (FIELD_EMAILS != null && !FIELD_EMAILS.isEmpty()) {
            homeEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_HOME_EMAILS);
            workEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_WORK_EMAILS);
            otherEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_OTHER_EMAILS);
            if (homeEmails != null) {
                emailColumns.addAll(homeEmails);
            }
            if (workEmails != null) {
                emailColumns.addAll(workEmails);
            }
            if (otherEmails != null) {
                emailColumns.addAll(otherEmails);
            }
            importingParams.put(EdsCrmContactItemParams.EMAIL, getImportingParamAsMap(EdsCrmContactItemParams.EMAIL_PARAMS, null, new List[]{homeEmails, workEmails, otherEmails}));
        }
        Map<Integer, ArrayList<Integer>> FIELD_PHONES = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_PHONES);
        Set<Integer> phoneColumns = new HashSet<>();
        List<Integer> homePhones = null;
        List<Integer> workPhones = null;
        List<Integer> mobilePhones = null;
        List<Integer> homeFaxes = null;
        List<Integer> workFaxes = null;
        List<Integer> pagerPhones = null;
        List<Integer> otherPhones = null;
        List<Integer> extensionPhones = null;
        if (FIELD_PHONES != null && !FIELD_PHONES.isEmpty()) {
            homePhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_HOME_PHONES);
            workPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_WORK_PHONES);
            mobilePhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_MOBILE_PHONES);
            homeFaxes = FIELD_PHONES.get(ImportField.ContactField.FIELD_HOMEFAX_PHONES);
            workFaxes = FIELD_PHONES.get(ImportField.ContactField.FIELD_WORKFAX_PHONES);
            pagerPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_PAGER_PHONES);
            otherPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_OTHER_PHONES);
            extensionPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_EXTENSION);
            if (homePhones != null) {
                phoneColumns.addAll(homePhones);
            }
            if (workPhones != null) {
                phoneColumns.addAll(workPhones);
            }
            if (mobilePhones != null) {
                phoneColumns.addAll(mobilePhones);
            }
            if (homeFaxes != null) {
                phoneColumns.addAll(homeFaxes);
            }
            if (workFaxes != null) {
                phoneColumns.addAll(workFaxes);
            }
            if (pagerPhones != null) {
                phoneColumns.addAll(pagerPhones);
            }
            if (otherPhones != null) {
                phoneColumns.addAll(otherPhones);
            }
            if (extensionPhones != null) {
                phoneColumns.addAll(extensionPhones);
            }
            importingParams.put(EdsCrmContactItemParams.PHONE, getImportingParamAsMap(EdsCrmContactItemParams.PHONE_PARAMS, null, new List[]{homePhones, workPhones, mobilePhones, homeFaxes, workFaxes, pagerPhones, otherPhones, extensionPhones}));
        }

        int notEmailCount = 0;
        int insertOrUpdatedCount = 0;
        boolean hasHeader = importFile.isHasHeader();
        boolean withHeader = importFile.isHasHeader();
        int i = 0, j = 0;
        SelectItem[] contactSelectItems = contactServiceLocal.getContactSelectItems(_TITLE);
        for (String[] row : list) {

            try {
                j++;
                log.info("Employee import row {}: {}", j, row[0]);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            Boolean isEssUser = Boolean.FALSE;
            Boolean isNoAccess = Boolean.FALSE;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }
            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }
            if (rowIsEmpty) {
                continue;
            }
            isUpdate = false;
            ProfileItem profileItem = new ProfileItem();
            EdsEmployee employee = null;
            if (EMPLOYEE_CODE > -1 && row != null && StringUtils.isNotBlank(row[EMPLOYEE_CODE])) {
                employee = employeeMapByCode.get(row[EMPLOYEE_CODE].trim());
                if (employee != null) {
                    profileItem = hrmsServiceLocal.editProfile(employee.getObjectID(), EMPLOYEE_IMPORT);
                    isUpdate = true;

                    if (employee.getRoles().contains(edsEssRole)) {
                        isEssUser = Boolean.TRUE;
                    }
                    if (EMPLOYEE_STATUS_NO_ACCCESS.equals(employee.getAccountStatus().getCode())) {
                        isNoAccess = Boolean.TRUE;
                    }
                }
            }

            Address address = new Address();
            profileItem.setFrom(EMPLOYEE_IMPORT);

            if (employee != null && employeesPayrollSettingsMap.get(employee.getObjectID()) != null) {
                HashMap<String, String> epsMap = employeesPayrollSettingsMap.get(employee.getObjectID());
                if (epsMap != null && !epsMap.isEmpty()) {
                    profileItem.setPayrollSettings(epsMap);
                    String salaryValue = epsMap.get(SALARY);
                    if (salaryValue != null && !"".equals(salaryValue)) {
                        profileItem.setSalaryAmount(Double.parseDouble(salaryValue));
                    } else {
                        profileItem.setSalaryAmount(0d);
                    }

                    String jobTitle = epsMap.get(CustomFormConstants.JOB_TITLE);
                    if (jobTitle != null) {
                        String jobTitleText = epsMap.get(JOB_TITLE_TEXT);
                        profileItem.setJobTitleId(Integer.valueOf(jobTitle));
                        profileItem.setJobTitle(jobTitleText);
                    }
                }
            }

            UserBankAccountData bankAccountData = new UserBankAccountData();
            if (employee != null && employeeBankAccountByCode.get(employee.getObjectID()) != null) {
                bankAccountData = employeeBankAccountByCode.get(employee.getObjectID());
            }

            ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();
            if (isUpdate) {
                customFields = profileItem.getCustomFields();
            }
            ArrayList<PaymentDeductionObject> paymentList = new ArrayList<>();
            ArrayList<PaymentDeductionObject> deductionList = new ArrayList<>();
            int columnId = 0;
            boolean isPrimaryEmail = false;
            boolean isPrimaryPhone = false;
            for (String value : row) {
                if (StringUtils.isNotBlank(value)) {
                    if (value.length() > 250) {
                        log.info("!!!!!!!!!!!!!{}:::{}:::{}", columnId, value, value.length());
                        value = value.substring(0, 250).trim();
                    }
                    value = value.trim();

                    //employee code
                    if (columnId == EMPLOYEE_CODE) {
                        profileItem.setEmpCode(value);
                        if (employeeManager.getEmployeeByNumber(value) == null) {
                            Integer employeeId = createEmployee(parentEmployeeId, profileItem, address);
                            profileItem.setObjectId(employeeId);
                            profileItem.setEmployeeId(employeeId);
                            isUpdate = true;
                        }
                    }

                    if (columnId == SALUTATION) {
                        if (contactSelectItems != null) {
                            for (SelectItem item : contactSelectItems) {
                                if (value.equalsIgnoreCase(item.getName())) {
                                    profileItem.setTitle(item.getName());
                                    profileItem.setTitleId(item.getId());
                                }
                            }
                        }
                    }
                    //first name
                    if (columnId == FIRST_NAME) {
                        if (StringUtils.isNotBlank(value)) {
                            profileItem.setFirstName(value);
                        } else if (!isUpdate) {
                            rejectedRow[FIRST_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, withHeader ? rejectedRecords.get(0)[columnId].getData() : "First name"));
                            isValid = false;
                        }
                    }
                    //Last name
                    if (columnId == LAST_NAME) {
                        profileItem.setLastName(value);
                    }
                    //Middle name
                    if (columnId == MIDDLE_NAME) {
                        profileItem.setMiddleName(value);
                    }
                    //Date of birth
                    if (columnId == DATE_OF_BIRTH) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setDob(result != null ? new DateNonConvertable(result) : null);
                        profileItem.setBirthDate(result != null ? new DateNonConvertable(result) : null);
                    }
                    //Gender
                    if (columnId == GENDER) {
                        profileItem.setGender(value);
                    }

                    //Marital Status
                    if (columnId == MARTIAL_STATUS) {
                        Integer maritalStatus = compMartialStMap.get(value.toLowerCase());
                        if (maritalStatus != null) {
                            profileItem.setMartialStatusId(maritalStatus);
                        }
                    }

                    //Spoken Languages
                    if (columnId == SPOKEN_LANGUAGES) {
                        String[] languages = value.split(MULTIVALUE_SEPARATOR);
                        ArrayList<SelectItem> spokenLanguages = new ArrayList<>(languages.length);
                        for (String lan : languages) {
                            EdsReference language = referenceManager.getByCode("LANGUAGE_" + lan.toUpperCase());
                            if (language != null) {
                                spokenLanguages.add(language.getAsSelectItem());
                            }
                        }
                        profileItem.setSpokenLanguages(spokenLanguages.toArray(new SelectItem[]{}));
                    }
                    if (FIELD_EMAILS != null && !FIELD_EMAILS.isEmpty() && emailColumns.contains(columnId)) {

                        EMAIL_ID = addPrimaryData(profileItem, value, columnId, FIELD_EMAILS, ImportField.ContactField.FIELD_EMAILS);
                        if (!isPrimaryEmail) {
                            profileItem.setPrimaryEmail(value);
                            isPrimaryEmail = true;
                        }
                        System.out.println(value);
                    }
                    if (FIELD_PHONES != null && !FIELD_PHONES.isEmpty() && phoneColumns.contains(columnId)) {
                        addPrimaryData(profileItem, value, columnId, FIELD_PHONES, ImportField.ContactField.FIELD_PHONES);
                        if (!isPrimaryPhone) {
                            profileItem.setPrimaryPhone(value);
                            isPrimaryPhone = true;
                        }
                        System.out.println(value);
                    }
                    if (columnId == WEB_ADDRESS) {
                        profileItem.setWorkWebSite(new ArrayList<>(Collections.singletonList(value)));
                    }
                    if (columnId == IM_ADDRESS) {
                        profileItem.setAIM(new ArrayList<>(Collections.singletonList(value)));
                    }
                    //Address Name
                    if (columnId == ADDRESS_NAME) {
                        address.setName(value);

                    }
                    //Address type
                    if (columnId == ADDRESS_TYPE && StringUtils.isNotBlank(value)) {
                        if ("work".equalsIgnoreCase(value)) {
                            address.setRelationType(AddressReference.WORK.getId());
                        } else if ("home".equalsIgnoreCase(value)) {
                            address.setRelationType(AddressReference.HOME.getId());
                        } else if ("other".equalsIgnoreCase(value)) {
                            address.setRelationType(AddressReference.OTHER.getId());
                        }

                    }
                    //Address
                    if (columnId == ADDRESS_LINE) {
                        address.setAddress(value);

                    }
                    //Address2
                    if (columnId == ADDRESS_LINE2) {
                        address.setAddressb(value);

                    }
                    //CIty
                    if (columnId == CITY) {
                        address.setCity(value);

                    }
                    //Country
                    if (columnId == COUNTRY) {
                        address.setCountry(value);

                    }
                    //State
                    if (columnId == STATE) {
                        Integer regiodId = regions.get(value.toLowerCase());
                        if (regiodId != null) {
                            address.setStateId(regiodId);
                            address.setState(value);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }
                    }
                    //Post Code
                    if (columnId == POST_CODE) {
                        address.setZipCode(value);

                    }
                    //Start Date
                    if (columnId == HIRE_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setHireDate(result != null ? new DateNonConvertable(result) : null);

                    }
                    //End date
                    if (columnId == RESIGNATION_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setFireDate(result != null ? new DateNonConvertable(result) : null);

                    }
                    //Employment mode
                    if (columnId == EMPLOYMENT_MODE) {
                        Integer employmentMode = compEmployeeModeMap.get(value.toLowerCase());
                        if (employmentMode != null) {
                            profileItem.setEmpModeId(employmentMode);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }

                    }
                    if (columnId == EMPLOYMENT_CONTACT_TERMtS) {
                        if (value != null) {
                            try {
                                String[] w = value.split(" ");
                                profileItem.setTermsOfContract(Integer.parseInt(w[0]));
                                if (w[1] != null) {
                                    profileItem.setTermsOfCMonthORYear("year".equalsIgnoreCase(w[1]) ? 2 : 1);
                                }
                            } catch (NumberFormatException e) {
                                rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                                isValid = false;
                            }
                        }
                    }
                    if (columnId == SALARY_GRADE) {
                        Integer gradeID = gradeListMap.get(value.toLowerCase());
                        if (gradeID != null) {
                            profileItem.setSalaryGradeId(gradeID);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }
                    }

                    //Qualification
                    if (columnId == QUALIFICATION) {
                        Integer qualificationId = qualificationsMap.get(value.toLowerCase());
                        if (qualificationId != null) {
                            profileItem.setQualificationID(qualificationId);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }
                    }
                    if (columnId == COMPETENCIES) {
                        String[] skills = value.split(MULTIVALUE_SEPARATOR);
                        ArrayList<KpiTreeInfo> infos = new ArrayList<>();
                        for (String sk : skills) {
                            Integer skillID = competencyMap.get(sk.trim());
                            if (skillID != null) {
                                infos.add(new KpiTreeInfo(skillID, ""));
                            }
                        }
                        profileItem.setEmployeeCompetencies(infos);
                    }

                    //Position
                    if (columnId == POSITION) {
                        Integer positionId = positionMap.get(value.toLowerCase());
                        if (positionId != null) {
                            profileItem.setPositionId(positionId);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }

                    }
                    //Department
                    if (columnId == DEPARTMENT_NAME) {
                        Integer departmentId = compDepartmentsMap.get(value.toLowerCase());
                        if (departmentId != null) {
                            profileItem.setPmDepartmentID(departmentId);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }
                    }
                    //Role
                    if (columnId == USER_ROLE) {
                        Integer roleId = compRolesMap.get(value.toLowerCase());
                        if (roleId != null) {
                            profileItem.setRoleId(new Integer[]{roleId});
                        } else {
                            profileItem.setRoleId(new Integer[]{edsMemberRoleId});
                        }

                    }

                    // Has Access
                    if (columnId == HAS_ACCESS) {
                        String access = value != null ? value.toLowerCase() : "";

                        profileItem.setEss(false);
                        profileItem.setNoAccess(false);

                        if ("ess".equals(access)) {
                            profileItem.setEss(true);
                            profileItem.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
                        } else if ("active".equals(access) || "yes".equals(access)) {
                            profileItem.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
                        } else {
                            profileItem.setNoAccess(true);
                            profileItem.setStatusCode(EMPLOYEE_STATUS_NO_ACCCESS);
                        }
                    }

                    //Location
                    if (columnId == LOCATION) {
                        Integer locationId = compLocationsMap.get(value.toLowerCase());
                        if (locationId != null) {
                            profileItem.setLocationId(locationId);
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }
                    }
                    //Salary
                    if (columnId == BASIC_SALARY) {
                        try {
                            profileItem.setSalaryAmount(Double.valueOf(value.replace(",", "")));
                        } catch (NumberFormatException e) {
                        }

                    }
                    if (columnId == OPENING_BALANCE_DAYS) {
                        try {
                            profileItem.setOpeningBalanceDays(Double.valueOf(value.replace(",", "")));
                        } catch (NumberFormatException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, withHeader ? rejectedRecords.get(0)[columnId].getData() : "Opening balance days"));
                            isValid = false;
                        }
                    }
                    if (columnId == PROBATION_PERIOD_DAYS) {
                        try {
                            profileItem.setProbationDays(Double.valueOf(value.replace(",", "")));
                        } catch (NumberFormatException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, withHeader ? rejectedRecords.get(0)[columnId].getData() : "Probation period days"));
                            isValid = false;
                        }
                    }
                    //Supervisior
                    if (columnId == SUPER_VISER) {
                        EdsEmployee edsEmployee = employeeManager.getEmployeeByFirstNameViaLastName(value);
                        if (edsEmployee == null) {
                            edsEmployee = employeeManager.getEmployeeByNumber(value);
                        }
                        if (edsEmployee != null) {
                            profileItem.setReportsToId(edsEmployee.getObjectID());
                        } else {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, value));
                            isValid = false;
                        }

                    }
                    //Wage Rate
                    if (columnId == WAGE_RATE) {
                        try {
                            profileItem.setWageRate(Double.valueOf(value.replace(",", "")));
                        } catch (NumberFormatException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, withHeader ? rejectedRecords.get(0)[columnId].getData() : "Wage rate"));
                            isValid = false;
                        }

                    }
                    //Client Charge Rate
                    if (columnId == CLIENT_CHARGE_RATE) {
                        try {
                            profileItem.setClientChargeRate(Double.valueOf(value.replace(",", "")));
                        } catch (NumberFormatException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, withHeader ? rejectedRecords.get(0)[columnId].getData() : "Client change rate"));
                            isValid = false;
                        }

                    }

                    //Visa number
                    if (columnId == VISA_NUMBER) {
                        profileItem.setVisaNumber(value);

                    }
                    //Visa issue by
                    if (columnId == PASSPORT_ISSUE_BY) {
                        Integer countryId = countriesMap.get(value.toLowerCase());
                        if (countryId != null) {
                            profileItem.setPassportIssueItem(new SelectItem(countryId, value));
                        }

                    }
                    //Visa expiration Date
                    if (columnId == VISA_EXPIRY_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        if (result != null) {
                            result = resetTime(result);
                            profileItem.setVisaExpirationDate(new DateNonConvertable(result));
                        }

                    }
                    //Visa issue Date
                    if (columnId == VISA_ISSUE_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setVisaIssueDate(result != null ? new DateNonConvertable(result) : null);
                    }
                    //Nationality
                    if (columnId == NATIONALITY) {
                        profileItem.setNationality(value);

                    }
                    // Passport Number
                    if (columnId == PASSPORT_NUMBER) {
                        profileItem.setPassportNumber(value);

                    }
                    //PassportIssueDate
                    if (columnId == PASSPORT_ISSUE_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setPassportIssueDate(result != null ? new DateNonConvertable(result) : null);

                    }
                    //PassportExpiryDate
                    if (columnId == PASSPORT_EXPIRY_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setPassportExpiryDate(result != null ? new DateNonConvertable(result) : null);
                    }
                    //InsuranceNumber
                    if (columnId == INSURANCE_NUMBER) {
                        profileItem.setInsuranceNumber(value);

                    }
                    //Insurance Expiry Date
                    if (columnId == INSURANCE_EXPIRY_DATE) {
                        Date result = null;
                        try {
                            result = formatter.parse(value);
                        } catch (ParseException e) {
                            rejectedRow[columnId].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        profileItem.setMedicalInsuranceExpireDate(result != null ? new DateNonConvertable(result) : null);

                    }

                    //payrollSettiings: WPS_NO
                    if (columnId == WPS_NO) {
                        HashMap<String, String> payrollSettings = new HashMap<>();
                        payrollSettings.put(CustomFormConstants.WPS_NUMBER, value);
                        profileItem.setPayrollSettings(payrollSettings);
                    }
                    //BankName
                    if (columnId == BANK_NAME) {
                        bankAccountData.setBankName(value);

                    }
                    //BankAddress
                    if (columnId == BANK_ADDRESS) {
                        bankAccountData.setBankAddress(value);

                    }
                    //AccountNumber
                    if (columnId == ACCOUNT_NUMBER) {
                        bankAccountData.setAccountNumber(value);

                    }
                    //Account Name
                    if (columnId == ACCOUNT_NAME) {
                        bankAccountData.setAccountName(value);

                    }
                    //SwiftCode
                    if (columnId == SWIFT_BCIC_CODE) {
                        bankAccountData.setSwiftCode(value);

                    }
                    //SortCode
                    if (columnId == SORT_CODE) {
                        bankAccountData.setSortCode(value);

                    }
                    //IbanCode
                    if (columnId == IBAN_NUMBER) {
                        bankAccountData.setIbanCode(value);

                    }
                    //Agent ID
                    if (columnId == AGENT_ID) {
                        bankAccountData.setAgentID(value);

                    }

                    if (importFile.getExtraColumns() != null && !importFile.getExtraColumns().isEmpty()) {
                        for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                            if (!importFile.getExtraColumnID(extraColumnEntry.getValue()).equals(columnId) || extraColumnEntry.getKey() < ImportField.EmployeeField.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                continue;
                            }
                            CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, columnId, value, rejectedRow, withHeader ? rejectedRecords.get(0)[columnId].getData() : null);

                            if (customField == null) {
                                isValid = false;
                                break;
                            }

                            customFields.add(customField);
                        }
                    }

                    if (categories != null) {
                        if (categories.containsKey(columnId) && categories.get(columnId) != null) {
                            Integer categoryId = categories.get(columnId);
                            EdsPayrollCategory category = payrollCategoryManager.get(categoryId);
                            if (category != null) {
                                PaymentDeductionSelectItem ci = category.createPaymentDeductionSelectItem();
                                List<PaymentDeductionObject> deductionObjects = PayrollConstants.CATEGORY_PAYMENT.equals(ci.getType()) ? profileItem.getPaymentCategories()
                                        : PayrollConstants.CATEGORY_DEDUCTION.equals(ci.getType()) ? profileItem.getDeductionCategories() : new ArrayList<>();
                                if (!deductionObjects.isEmpty()) {
                                    for (PaymentDeductionObject deduction : deductionObjects) {
                                        if (deduction.getCategoryItem() != null && deduction.getCategoryItem().equals(ci)) {
                                            deduction.setType(0);
                                            deduction.setPaymentAmount(new BigDecimal(value.replace(",", "")));
                                        } else {
                                            PaymentDeductionObject pdo = new PaymentDeductionObject();
                                            pdo.setCategoryItem(ci);
                                            pdo.setType(0);
                                            try {
                                                pdo.setPaymentAmount(new BigDecimal(value.replace(",", "")));
                                            } catch (Exception e) {
                                                rejectedRow[columnId].setErrorComment(category.getName() + " should be in number format.");
                                                isValid = false;
                                            }
                                        }
                                        if (PayrollConstants.CATEGORY_PAYMENT.equals(ci.getType())) {
                                            paymentList.add(deduction);
                                        } else if (PayrollConstants.CATEGORY_DEDUCTION.equals(ci.getType())) {
                                            deductionList.add(deduction);
                                        }
                                    }
                                } else {
                                    PaymentDeductionObject pdo = new PaymentDeductionObject();
                                    pdo.setCategoryItem(ci);
                                    pdo.setType(0);
                                    try {
                                        pdo.setPaymentAmount(new BigDecimal(value.replace(",", "")));
                                    } catch (Exception e) {
                                        rejectedRow[columnId].setErrorComment(category.getName() + " should be in number format.");
                                        isValid = false;
                                    }
                                    if (PayrollConstants.CATEGORY_PAYMENT.equals(ci.getType())) {
                                        paymentList.add(pdo);
                                    } else if (PayrollConstants.CATEGORY_DEDUCTION.equals(ci.getType())) {
                                        deductionList.add(pdo);
                                    }
                                }
                            }
                        }
                    }
                }
                columnId++;
            }

            if (!paymentList.isEmpty()) {
                profileItem.setPayments(paymentList);
            }
            if (!deductionList.isEmpty()) {
                profileItem.setDeductions(deductionList);
            }

            Integer limit = 0;
            if (isUpdate) {
                if (!profileItem.getEss().equals(isEssUser) || !profileItem.getNoAccess().equals(isNoAccess)) {
                    limit = employeeServiceLocal.checkUserLimit(profileItem.getEss(), !profileItem.getNoAccess(), companyId);
                }
            } else {
                if (profileItem.getEss() && !profileItem.getNoAccess()) {
                    limit = isEssHasAccess;
                } else if (profileItem.getEss() && profileItem.getNoAccess()) {
                    limit = isEssNotAccess;
                } else if (!profileItem.getEss() && !profileItem.getNoAccess()) {
                    limit = isNotEssHassAccess;
                } else if (!profileItem.getEss() && profileItem.getNoAccess()) {
                    limit = isNotEssNotAccess;
                }
            }

            if (limit < 0) {
                if (limit == ESS_LIMIT_EXCEEDED) {
                    rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more ESS users. Please contact support@kpi.com.");
                    isValid = false;
                } else if (limit == NO_ACCESS_LIMIT_EXCEEDED) {
                    rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more No access users. Please contact support@kpi.com");
                    isValid = false;
                } else if (limit == ACTIVE_LIMIT_EXCEEDED) {
                    rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more Active users. Please contact support@kpi.com");
                    isValid = false;
                }
            }

            //do not import when row is a empty
            if (!isUpdate && StringUtils.isBlank(profileItem.getFirstName())) {
                if (FIRST_NAME > 0) {
                    rejectedRow[FIRST_NAME].setErrorComment("First name cannot be empty.");
                } else {
                    rejectedRow[0].setErrorComment("First name cannot be empty.");
                }
                rejectedRecords.add(rejectedRow);
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                continue;
            }

            if (!isValid) {
                rejectedRecords.add(rejectedRow);
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                continue;
            }

            ServerSecurityContext.getInstance().setStaticUserID(parentEmployeeId);
            if (profileItem.getEmail() == null || "".equals(profileItem.getEmail())) {
                String emailtest = "no_access_" + new Date().getTime() + "_" + notEmailCount + "@workforcetrack.com";
                profileItem.setPrimaryEmail(emailtest);
                profileItem.setHomeEmail(emailtest);
                notEmailCount = notEmailCount + 1;
            }
            profileItem.setImportFileID(importFile.getObjectID());
            profileItem.setBankAccountData(bankAccountData);
            profileItem.setCustomFields(customFields);

            Integer newEmployeeID;
            if (isUpdate) {
                profileItem.setAddresses(new ArrayList<>(Collections.singletonList(address)));
                newEmployeeID = hrmsServiceLocal.updateProfile(profileItem);
            } else {
                newEmployeeID = createEmployee(parentEmployeeId, profileItem, address);
            }
            log.info(" <<<<<<<<<<<<<  {}- qator yozildi >>>>>>>>>>>>>>>>>>>  <<<<<<<<<<<<<<<<<<<<<  NEW EMPLOYEE ID : {} EMAIL : {}  >>>>>>>>>>>>>>>>>>>>>>>", insertOrUpdatedCount++, newEmployeeID, profileItem.getEmail());
            if (newEmployeeID == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                rejectedRow[EMAIL_ID].setErrorComment("Employee with this email already exists. Change the email address.");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  EMPLOYEE WITH THIS EMAIL : {} IS ALREADY EXIST !!!  >>>>>>>>>>>>>>>>>>>>>>>", profileItem.getEmail());
            } else if (newEmployeeID == EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST) {
                rejectedRow[EMAIL_ID].setErrorComment("Change the employee email address to a valid email");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  EMPLOYEE WITH THIS EMAIL : {} IS INVALID !!!  >>>>>>>>>>>>>>>>>>>>>>>", profileItem.getEmail());
            } else if (newEmployeeID == CAN_NOT_CREATE_EMPLOYEE) {
                log.info(" <<<<<<<<<<<<<<<<<<<<<  ERROR OCCURED WITH THIS EMAIL : {} >>>>>>>>>>>>>>>>>>>>>>>", profileItem.getEmail());
            } else if (newEmployeeID == EMPLOYEE_WITH_THIS_CODE_ALREADY_EXISTS) {
                rejectedRow[EMPLOYEE_CODE].setErrorComment("Employee with this code already exists. Please change the code");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  EMPLOYEE WITH THIS CODE : {} IS ALREADY EXIST !!!  >>>>>>>>>>>>>>>>>>>>>>>", profileItem.getEmpCode());
            } else if (newEmployeeID == ESS_LIMIT_EXCEEDED) {
                rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more ESS users. Please contact support@kpi.com.");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  ESS EMPLOYEE LIMIT EXCEEDED !!!  >>>>>>>>>>>>>>>>>>>>>>>");
            } else if (newEmployeeID == ACTIVE_LIMIT_EXCEEDED) {
                rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more Active users. Please contact support@kpi.com");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  ACTIVE EMPLOYEE LIMIT EXCEEDED !!!  >>>>>>>>>>>>>>>>>>>>>>>");
            } else if (newEmployeeID == NO_ACCESS_LIMIT_EXCEEDED) {
                rejectedRow[HAS_ACCESS].setErrorComment("You do not have enough license to add more No access users. Please contact support@kpi.com");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  NO ACCESS EMPLOYEE LIMIT EXCEEDED !!!  >>>>>>>>>>>>>>>>>>>>>>>");
            } else if (newEmployeeID == SUPERVISOR_CIRCULAR_REFERENCE) {
                rejectedRow[SUPER_VISER].setErrorComment("Selected Supervisor may cause hierarchical circulation issues in structure");
                isValid = false;
                log.info(" <<<<<<<<<<<<<<<<<<<<<  NO ACCESS EMPLOYEE LIMIT EXCEEDED !!!  >>>>>>>>>>>>>>>>>>>>>>>");
            }
            if (!isValid) {
                rejectedRecords.add(rejectedRow);
            } else {
                importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                if (isUpdate) {
                    importFile.setOverwrittenColumns(importFile.getOverwrittenColumns() + 1);
                } else {
                    importFile.setNewColumns(importFile.getNewColumns() + 1);
                }
            }
            if (i == 10) {
                profileManager.flushAndClear();
                i = 0;
            }
            i++;
        }
        return rejectedRecords;
    }

    private Integer createEmployee(Integer parentEmployeeId, ProfileItem profileItem, Address address) {
        EdsReference pendingEmployeeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING);
        profileItem.setStatusId(pendingEmployeeStatus.getObjectID());
        profileItem.setContactType(contactType);
        profileItem.getAddresses().add(address);
        NewEmployee newEmployee = wrap(profileItem);
        return employeeServiceLocal.createEmployeeInternal(newEmployee, parentEmployeeId);
    }

    private int addPrimaryData(ProfileItem profileItem, String value, int columnId, Map<Integer, ArrayList<Integer>> feild, int type) {
        int email_id = -1;
        List<Integer> homeEmails = null;
        List<Integer> workEmails = null;
        List<Integer> otherEmails = null;

        List<Integer> homePhones = null;
        List<Integer> workPhones = null;
        List<Integer> mobilePhones = null;
        List<Integer> homeFaxes = null;
        List<Integer> workFaxes = null;
        List<Integer> pagerPhones = null;
        List<Integer> otherPhones = null;
        List<Integer> extensionPhones = null;

        if (ImportField.ContactField.FIELD_EMAILS == type) {
            homeEmails = feild.get(ImportField.ContactField.FIELD_HOME_EMAILS);
            workEmails = feild.get(ImportField.ContactField.FIELD_WORK_EMAILS);
            otherEmails = feild.get(ImportField.ContactField.FIELD_OTHER_EMAILS);
            email_id = columnId;

            boolean hasEmail = profileItem.getHomeEmail().contains(value) ||
                    profileItem.getWorkEmail().contains(value) ||
                    profileItem.getOtherEmail().contains(value);

            if (homeEmails != null && homeEmails.contains(columnId) && !hasEmail) {
                profileItem.setHomeEmail(value);
            }
            if (workEmails != null && workEmails.contains(columnId) && !hasEmail) {
                profileItem.setWorkEmail(value);
            }
            if (otherEmails != null && otherEmails.contains(columnId) && !hasEmail) {
                profileItem.setOtherEmail(value);
            }
            return email_id;
        }

        if (ImportField.ContactField.FIELD_PHONES == type) {
            homePhones = feild.get(ImportField.ContactField.FIELD_HOME_PHONES);
            workPhones = feild.get(ImportField.ContactField.FIELD_WORK_PHONES);
            mobilePhones = feild.get(ImportField.ContactField.FIELD_MOBILE_PHONES);
            homeFaxes = feild.get(ImportField.ContactField.FIELD_HOMEFAX_PHONES);
            workFaxes = feild.get(ImportField.ContactField.FIELD_WORKFAX_PHONES);
            pagerPhones = feild.get(ImportField.ContactField.FIELD_PAGER_PHONES);
            otherPhones = feild.get(ImportField.ContactField.FIELD_OTHER_PHONES);
            extensionPhones = feild.get(ImportField.ContactField.FIELD_EXTENSION);

            boolean hasPhone = profileItem.getAllPhones().contains(value);

            if (homePhones != null && homePhones.contains(columnId) && !hasPhone) {
                profileItem.setHomePhone(value);
            }
            if (workPhones != null && workPhones.contains(columnId) && !hasPhone) {
                profileItem.setWorkPhone(value);
            }
            if (mobilePhones != null && mobilePhones.contains(columnId) && !hasPhone) {
                profileItem.setMobile(value);
            }
            if (homeFaxes != null && homeFaxes.contains(columnId) && !hasPhone) {
                profileItem.setHomeFax(value);
            }
            if (workFaxes != null && workFaxes.contains(columnId) && !hasPhone) {
                profileItem.setWorkFax(value);
            }
            if (pagerPhones != null && pagerPhones.contains(columnId) && !hasPhone) {
                profileItem.setPager(value);
            }
            if (otherPhones != null && otherPhones.contains(columnId) && !hasPhone) {
                profileItem.setOtherPhone(value);
            }
            if (extensionPhones != null && extensionPhones.contains(columnId) && !hasPhone) {
                profileItem.setExtension(value);
            }
            return 0;
        }

        return email_id;
    }

    private NewEmployee wrap(ProfileItem profileItem) {
        NewEmployee employee = new NewEmployee();
        employee.setFromEmployeeImport(true);
        employee.setImportFileID(profileItem.getImportFileID());
        employee.setAddSingleEmployee(true);
        employee.setFname(profileItem.getFirstName());
        employee.setLname(profileItem.getLastName());
        employee.setMname(profileItem.getMiddleName());
        employee.setOtherName(profileItem.getOtherName());
        employee.setTitle(profileItem.getTitle());
        employee.setEmail(profileItem.getEmail());
        employee.setDepartment(profileItem.getPmDepartmentID());
        employee.setRoleId(profileItem.getRoleId());
        employee.setStartDate(profileItem.getHireDate());
        employee.setEndDate(profileItem.getFireDate());
        employee.setHasAccess(profileItem.getNoAccess() == null || !profileItem.getNoAccess());
        employee.setBirthDate(profileItem.getDob());
        employee.setGender(profileItem.getGender());
        employee.setContactListItem(profileItem);
        employee.setAttachments(profileItem.getAttachments());
        employee.setDriverID(profileItem.getDriverID());
        if (profileItem.getDriverID() != null && !"".equals(profileItem.getDriverID())) {
            employee.setDriverNumber(Long.valueOf(profileItem.getDriverID()));
        }
        employee.setNationality(profileItem.getNationality());
        employee.setMartialStatusId(profileItem.getMartialStatusId());
        employee.setSpokenLanguages(profileItem.getSpokenLanguages());
        employee.setEmployeeCompetencies(profileItem.getEmployeeCompetencies());
        employee.setCreatedFrom(profileItem.getFrom());
        employee.setNumberData(profileItem.getNumberData());
        employee.setEmpCode(profileItem.getEmpCode());
        employee.setDepartment(profileItem.getPmDepartmentID());
        employee.setWageRate(profileItem.getWageRate());
        employee.setClientChargeRate(profileItem.getClientChargeRate());
        employee.setQualificationID(profileItem.getQualificationID());
        employee.setStatusId(profileItem.getStatusId());
        employee.setReportsToId(profileItem.getReportsToId());
        employee.setTermsOfContract(profileItem.getTermsOfContract());
        employee.setTermsOfCMonthORYear(profileItem.getTermsOfCMonthORYear());
        employee.setEmpModeId(profileItem.getEmpModeId());
        employee.setSalaryGradeId(profileItem.getSalaryGradeId());
        employee.setSalaryAmount(profileItem.getSalaryAmount());
        employee.setJobTitleId(profileItem.getJobTitleId());
        employee.setJobTitle(profileItem.getJobTitle());
        employee.setVisaExpirationDate(profileItem.getVisaExpirationDate());
        employee.setVisaExpirationDateReminder(profileItem.getVisaExpirationDateReminder());
        employee.setPosition(profileItem.getPosition());
        employee.setPositionId(profileItem.getPositionId());
        employee.setLocationId(profileItem.getLocationId());
        employee.setApplyPositionLeaveForEmployee(profileItem.isApplyPositionLeaveForEmployee());
        employee.setBankAccountData(profileItem.getBankAccountData());
        employee.setCoursesItems(profileItem.getCoursesItems());
        employee.setPassportNumber(profileItem.getPassportNumber());
        employee.setPassportIssueDate(profileItem.getPassportIssueDate());
        employee.setPassportExpiryDate(profileItem.getPassportExpiryDate());
        employee.setMedicalInsuranceExpireDate(profileItem.getMedicalInsuranceExpireDate());
        employee.setVisaNumber(profileItem.getVisaNumber());
        employee.setVisaIssueDate(profileItem.getVisaIssueDate());
        employee.setInsuranceNumber(profileItem.getInsuranceNumber());
        if (profileItem.getPassportIssueItem() != null) {
            employee.setPassportIssueID(profileItem.getPassportIssueItem().getId());
        }
        employee.setCustomFields(profileItem.getCustomFields());
        employee.setPaymentMethod(profileItem.getPaymentMethod());
        employee.setEssUser(profileItem.getEss());
        employee.setPayrollSettings(profileItem.getPayrollSettings());
        employee.setPayments(profileItem.getPayments());
        employee.setDeductions(profileItem.getDeductions());
        employee.setLoans(profileItem.getLoans());
        employee.setInactiveCategories(profileItem.getInactiveCategories());
        employee.setDeletedCategories(profileItem.getDeletedCategories());
        employee.setOpeningBalanceDays(profileItem.getOpeningBalanceDays());
        employee.setProbationDays(profileItem.getProbationDays());
        return employee;
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importExpense(ImportFile importFile, List<String[]> lists, Integer companyId, Integer reporterID) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;
        int FIRST_NAME_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.FIRST_NAME_FIELD);
        int LAST_NAME_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.LAST_NAME_FIELD);
        int EXPENSE_DATE_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.EXPENSE_DATE_FIELD);
        int REPORT_TITLE_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.REPORT_TITLE_FIELD);
        int DESCRIPTION_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.DESCRIPTION_FIELD);
        int SUPPLIER_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.SUPPLIER_FIELD);
        int RELATED_PROJECT_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.RELATED_PROJECT_FIELD);
        int APPROVER_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.APPROVER_FIELD);
        int CATEGORY_ITEM_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.CATEGORY_ITEM_FIELD);
        int DESCRIPTION_ITEM_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.DESCRIPTION_ITEM_FIELD);
        int UNITS_ITEM_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.UNITS_ITEM_FIELD);
        int COST_UNITS_ITEM_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.COST_UNITS_ITEM_FIELD);
        int TAX_ITEM_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.TAX_ITEM_FIELD);
        int PURCHASE_ORDER_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.PURCHASE_ORDER_FIELD);
        int CURRENCY_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.CURRENCY_FIELD);
        int EXCHANGE_RATE_FIELD = importFile.getColumnID(ImportField.CustomExpenseImportFields.EXCHANGE_RATE_FIELD);

        SimpleDateFormat customInvoiceDateFormat = new SimpleDateFormat(DATE_PATTERN);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        HashMap<Integer, ArrayList<ExpenseListItem>> allItems = new HashMap<>();
        HashMap<Integer, ExpenseReportsListItem> expenseList = new HashMap<>();
        Map<String, EdsCrmAccount> vendors = accountingService.getVendorsMap();
        Map<String, EdsPurchaseOrder> purchaseOrders = accountingService.getPurchaseOrders();
        Map<String, EdsCurrency> currencyMap = getCurrencyMap();
        ApprovalListResult expenseApprovers = expenseService.getExpenseApprovers(reporterID, true);
        boolean hasHeader = importFile.isHasHeader();
        for (String[] row : lists) {
            rejectedRow = new RejectedImportRecord[row.length];
            ExpenseReportsListItem expenseReportsListItem = new ExpenseReportsListItem();
            ExpenseListItem item = new ExpenseListItem();
            if (!hasHeader) {
                int csvColumnID = 0;
                boolean isValid = true;
                String firstName = null, lastName = null;
                for (String s : row) {
                    rejectedRow[csvColumnID] = new RejectedImportRecord(s);
                    if (s == null) {
                        s = "";
                    }
                    s = s.trim();
                    if (s.length() > 250) {
                        System.out.println("!!!!!!!!!!!!!" + csvColumnID + ":::" + s + ":::" + s.length());
                        s = s.substring(0, 250).trim();
                    }
                    if (csvColumnID == FIRST_NAME_FIELD) {
                        firstName = s;
                    } else if (csvColumnID == LAST_NAME_FIELD) {
                        lastName = s;
                    } else if (csvColumnID == EXPENSE_DATE_FIELD) {
                        if (StringUtils.isNotBlank(s)) {
                            try {
                                String[] part = s.split("/");
                                if (part.length >= 3) {
                                    LocalDate localDate = LocalDate.of(Integer.valueOf(part[2]), Integer.valueOf(part[0]), Integer.valueOf(part[1]));
                                    DateNonConvertable date = new DateNonConvertable(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                    expenseReportsListItem.setStartDate(date);
                                } else {
                                    rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateValue));
                                    isValid = false;
                                }
                            } catch (Exception e) {
                                rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateValue));
                                isValid = false;
                            }
                        } else {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty));
                            isValid = false;
                        }
                    } else if (csvColumnID == REPORT_TITLE_FIELD) {
                        if (!s.isEmpty()) {
                            expenseReportsListItem.setTitle(s);
                        } else {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Title"));
                            isValid = false;
                        }
                    } else if (csvColumnID == DESCRIPTION_FIELD) {
                        expenseReportsListItem.setDescription(s);
                    } else if (csvColumnID == SUPPLIER_FIELD) {
                        if (StringUtils.isNotBlank(s)) {
                            EdsCrmAccount supplier = vendors.get(s);
                            if (supplier != null) {
                                expenseReportsListItem.setSupplier(new SelectItem(supplier.getObjectID(), supplier.getName()));
                            } else {
                                rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, s));
                                isValid = false;
                            }
                        }
                    } else if (csvColumnID == CATEGORY_ITEM_FIELD) {
                        ListingFilterParameter filterParametrs = new ListingFilterParameter();
                        filterParametrs.setAccountType("Expense.");
                        filterParametrs.setSearchKey(s);
                        List<EdsAccount> accounts = accountingManager.getAccountsForExpense(filterParametrs);
                        if (accounts != null && accounts.size() > 0) {
                            item.setAccountId(accounts.get(0).getObjectID());
                        } else {
                            if (ImportTypeEnum.EXPENSE.equals(importFile.getType())) {
                                rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, s));
                                isValid = false;
                            }
                        }
                    } else if (csvColumnID == DESCRIPTION_ITEM_FIELD) {
                        item.setDescription(s);
                    } else if (csvColumnID == UNITS_ITEM_FIELD) {
                        try {
                            item.setUnits(new BigDecimal(s));
                        } catch (Exception e) {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, "Units"));
                            isValid = false;
                        }
                    } else if (csvColumnID == COST_UNITS_ITEM_FIELD) {
                        if (!s.isEmpty()) {
                            try {
                                item.setCostPerUnit(new BigDecimal(s));
                            } catch (Exception e) {
                                rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, "Cost per Unit"));
                                isValid = false;
                            }
                        } else {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Cost per Unit"));
                            isValid = false;
                        }
                    } else if (csvColumnID == TAX_ITEM_FIELD) {

                    } else if (csvColumnID == PURCHASE_ORDER_FIELD) {
                        EdsPurchaseOrder purchaseOrder = purchaseOrders.get(s);
                        if (purchaseOrder != null) {
                            item.setPurchaseOrder(new SelectItem(purchaseOrder.getObjectID(), purchaseOrder.getNumber()));
                        }
                    } else if (csvColumnID == CURRENCY_FIELD && currencyMap.containsKey(s)) {
                        if (currencyMap.get(s) != null) {
                            expenseReportsListItem.setExpenseCurrency(currencyMap.get(s).createCurrencyItem());
                        } else {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, s));
                            isValid = false;
                        }
                    } else if (csvColumnID == EXCHANGE_RATE_FIELD) {
                        expenseReportsListItem.setExchangeRate(new BigDecimal(s.replace(",", ".")));
                    } else if (csvColumnID == APPROVER_FIELD) {
                        if (expenseApprovers != null && expenseApprovers.getList().size() > 0) {
                            ArrayList<ApproverItemMini> approverList = new ArrayList<>();
                            ApproverItem approverItem = expenseApprovers.getList().get(0);
                            ApproverItemMini approverItemMini = new ApproverItem();
                            approverItemMini.setAppproveStatusId(approverItem.getAppproveStatusId());
                            approverItemMini.setRejectStatusId(approverItem.getRejectStatusId());
                            approverItemMini.setApproverOrder(approverItem.getApproverOrder());
                            approverItemMini.setClonedFrom(approverItem.getObjectID());
                            if (StringUtils.isNotBlank(s)) {
                                EdsEmployee employee = employeeManager.getEmployeeByFirstNameViaLastName(s);
                                if (employee != null) {
                                    approverItemMini.setExactEmployee(employee.getAsSelectItem());
                                    approverList.add(approverItemMini);
                                    expenseReportsListItem.setApprovers(approverList);
                                } else {
                                    rejectedRow[APPROVER_FIELD].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, s));
                                    isValid = false;
                                }
                            } else {
                                rejectedRow[APPROVER_FIELD].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, s));
                                isValid = false;
                            }
                        } else {
                            rejectedRow[csvColumnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Account"));
                            isValid = false;
                        }
                    } else if (csvColumnID == RELATED_PROJECT_FIELD) {
                        if (s != null && !"".equals(s)) {
                            List<EdsProject> projectList = projectManager.getProjectByName(s.trim());
                            if (projectList != null && projectList.size() > 0) {
                                expenseReportsListItem.setProject(new SelectItem(projectList.get(0).getObjectID(), projectList.get(0).getName()));
                            }
                        }
                    }


                    csvColumnID++;
                }
                if (ImportTypeEnum.EXPENSE.equals(importFile.getType())) {
                    if (firstName != null && lastName != null) {
                        if (expenseReportsListItem.getEmployeeId() == null) {
                            EdsEmployee employee = employeeManager.getEmployeeByFirstNameViaLastName(firstName + " " + lastName);
                            if (employee != null) {
                                expenseReportsListItem.setEmployeeId(employee.getObjectID());
                            } else {
                                rejectedRow[FIRST_NAME_FIELD].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, firstName + " " + lastName));
                                isValid = false;
                            }
                        }
                    } else {
                        rejectedRow[FIRST_NAME_FIELD].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, " FirstName, LastName "));
                        isValid = false;
                    }
                } else {
                    expenseReportsListItem.setEmployeeId(reporterID);
                    expenseReportsListItem.setCompanyExpense(true);
                }
                if (!isValid) {
                    rejectedRecords.add(rejectedRow);
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                } else {
                    if (item.getUnits() == null) {
                        item.setUnits(new BigDecimal("1.00"));
                    }
                    if (expenseReportsListItem.getExchangeRate() == null) {
                        expenseReportsListItem.setExchangeRate(new BigDecimal(1));
                    }
                    if (item.getExchageRate() == null) {
                        item.setExchageRate(new BigDecimal(1));
                    }
                    if (item.getUnits() != null && item.getCostPerUnit() != null) {
                        item.setSubtotal(item.getCostPerUnit().multiply(item.getUnits()));
                    }
                    if (item.getSubtotal() != null && expenseReportsListItem.getExchangeRate() != null) {
                        item.setBaseSubtotal(item.getSubtotal().divide(expenseReportsListItem.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                    }

                    if (expenseReportsListItem.getApproverSelectItem() == null) {
                        expenseReportsListItem.setApproverSelectItem(new SelectItem(reporterID));
                    }

                    if (expenseReportsListItem.getEmployeeId() == null) {
                        expenseReportsListItem.setEmployeeId(reporterID);
                    }

                    if (expenseReportsListItem.getStartDate() == null) {
                        expenseReportsListItem.setStartDate(new DateNonConvertable(new Date()));
                    }

                    EdsEmployee employee = employeeManager.get(expenseReportsListItem.getEmployeeId());
                    if (employee != null) {
                        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
                        EdsCurrency currency = financialSettings != null ? financialSettings.getCurrency() : null;
                        if (currency == null) {
                            currency = employee.getCompany().getCountryZone().getCountry().getCurrency();
                            if (currency == null) {
                                currency = currencyManager.getCurrency(CurrencyManager.USD);
                            }
                        }
                        if (expenseReportsListItem.getExpenseCurrency() == null)
                            expenseReportsListItem.setExpenseCurrency(new CurrencyItem(currency.getObjectID(), null, null));
                    }

                    expenseReportsListItem.setStatusCode(EXPENSE_SUBMITTED);
                    EdsExpenseReport expense = expenseManager.getOldExpense(expenseReportsListItem);
                    if (expense != null) {
                        if (allItems.get(expense.getObjectID()) != null && expenseList.get(expense.getObjectID()) != null) {
                            allItems.get(expense.getObjectID()).add(item);
                            expenseList.get(expense.getObjectID()).setId(expense.getObjectID());
                            //expenseList.get(expense.getObjectID()).setExpenseNumberData(new BankTransferNumberData(expense.getNumber(), expense.getIntNumber()));
                            expenseList.get(expense.getObjectID()).setExpenseNumber(expense.getNumber());
                            expenseList.get(expense.getObjectID()).setIntNumber(expense.getIntNumber());
                            expenseList.get(expense.getObjectID()).setItems(allItems.get(expense.getObjectID()).toArray(new ExpenseListItem[0]));
                            expenseService.saveReport(expenseList.get(expense.getObjectID()));
                            importFile.setOverwrittenColumns(importFile.getOverwrittenColumns() + 1);
                        } else {
                            ArrayList<ExpenseListItem> items = new ArrayList<>();
                            items.add(item);
                            expenseReportsListItem.setId(expense.getObjectID());
                            //expenseReportsListItem.setNumber(new NumberData(expense.getNumber(), expense.getIntNumber()));
                            expenseReportsListItem.setExpenseNumber(expense.getNumber());
                            expenseReportsListItem.setIntNumber(expense.getIntNumber());
                            expenseReportsListItem.setItems(new ExpenseListItem[]{item});
                            allItems.put(expense.getObjectID(), items);
                            expenseList.put(expense.getObjectID(), expenseReportsListItem);
                            importFile.setNewColumns(importFile.getNewColumns() + 1);
                        }
                    } else if (item.getAccountId() != null) {
                        expenseReportsListItem.setItems(new ExpenseListItem[]{item});
                        Integer newExpenseId = expenseService.saveReport(expenseReportsListItem);
                        EdsExpenseReport newExpense = expenseReportManager.get(newExpenseId);
                        expenseReportsListItem.setId(newExpense.getObjectID());
                        //expenseReportsListItem.setNumber(new NumberData(newExpense.getNumber(), newExpense.getIntNumber()));
                        expenseReportsListItem.setExpenseNumber(newExpense.getNumber());
                        expenseReportsListItem.setIntNumber(newExpense.getIntNumber());
                        ArrayList<ExpenseListItem> items = new ArrayList<>();
                        items.add(item);
                        allItems.put(newExpenseId, items);
                        expenseList.put(newExpenseId, expenseReportsListItem);
                        importFile.setNewColumns(importFile.getNewColumns() + 1);
                    }
                }
            } else {
                for (int i = 0; i < row.length; i++) {
                    rejectedRow[i] = new RejectedImportRecord(row[i]);
                }
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
            }
        }
        return rejectedRecords;
    }

    public static Date resetTime(Date date) {
        long msec = safeInMillis(date);
        msec = (msec / 1000) * 1000;
        date.setTime(msec);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        return date;
    }

    private static long safeInMillis(Date date) {
        return date != null ? date.getTime() : 0;
    }

    @Transactional
    public ArrayList<RejectedImportRecord[]> importContacts(ImportFile importFile, List listOfRows, int contactType, Integer mailListId) throws Exception {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;
        EdsUser employee = employeeManager.getUser();
        Date companyDate = ServerUtils.getCompanyDate(new Date(), employee.getCompany());
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        format.setLenient(false);
        boolean isLeadImport = ContactListItem.LEAD_CONTACT.equals(contactType);
        boolean isCandidateImport = ContactListItem.CANDIDATE.equals(contactType);

        List<EdsCrmContact> contactList = new ArrayList<>();
        final List<EdsCrmContactItemParams> itemParams = new ArrayList<>();
        final List<EdsAddress> itemAddresses = new ArrayList<>();
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        EdsCompany company = userManager.getUser().getCompany();
        Map<String, Integer> ownerIDs = employeeManager.getEmployeesByPermissionCodeAsMap(isCandidateImport ? PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER : PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE);
        Map<String, Integer> countriesIDs = ServerUtils.listToMapCountryIDs(countryManager.list());
        Map<String, Integer> regionsIDs = ServerUtils.listToMapRegionIDs(regionManager.list());
        Map<String, Integer> categoriesIDs = listToMapIDs(ContactCategoryListItem.asList(contactCategoryServiceLocal.getContactCategories()));
        Map<String, Integer> sourcesIDs = listToMapIDs(referenceManager.listReferences(isCandidateImport ? EdsCrmContact._CANDIDATE_SOURCE : EdsCrmContact._LEAD_SOURCE));
        Map<String, Integer> statusesIDs = listToMapIDs(referenceManager.listReferences(isCandidateImport ? EdsCrmContact._CANDIDATE_STATUS : EdsCrmContact._LEAD_STATUS));
        Map<String, Integer> leadRatingsIDs = listToMapIDs(referenceManager.listReferences(EdsCrmContact._LEAD_RATING));
        Map<String, Integer> vacancyIDs = ServerUtils.mapNameIDs(vacancyManager.getList());
        Map<String, Integer> locationIDs = ServerUtils.mapNameIDs(locationManager.getList());
        Map<String, Integer> campaignsIDs = ServerUtils.mapNameIDs(campaignManager.getList());
        Map<String, Integer> assigneeIDs = listToMapIDs(employeeManager.getEmployees(company));
        Map<String, Integer> accountIDs = ServerUtils.mapNameIDs(crmAccountManager.getList());
        Map<String, Integer> contactIDs = ServerUtils.mapNameIDs((companySettings.getOverwritePreference() != null && companySettings.getOverwritePreference().equals("BY_PHONE")) ? crmContactManager.getPhoneNumbers(contactType) : crmContactManager.getList(contactType));
        Map<Integer, Map<Integer, Map<Integer, ArrayList<String>>>> contactParams = importFile.isMerge() ? crmContactItemParamsManager.getAllContactParams() : null;

        //Personal Information
        boolean isOwnerFromFile = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_OWNER_FROMFILE));
        EdsEmployee owner = null;
        if (!isOwnerFromFile && importFile.getColumnID(ImportField.ContactField.FIELD_OWNER) != null) {
            owner = employeeManager.get(importFile.getColumnID(ImportField.ContactField.FIELD_OWNER));
        }
        int FIELD_OWNER = importFile.getColumnID(ImportField.ContactField.FIELD_OWNER);
        int FIELD_FIRSTNAME = importFile.getColumnID(ImportField.ContactField.FIELD_FIRSTNAME);
        int FIELD_LASTNAME = importFile.getColumnID(ImportField.ContactField.FIELD_LASTNAME);
        int FIELD_BIRTHDAY = importFile.getColumnID(ImportField.ContactField.FIELD_BIRTHDAY);
        int FIELD_TITLE = importFile.getColumnID(ImportField.ContactField.FIELD_TITLE);
        int FIELD_JOB_TITLE = importFile.getColumnID(ImportField.ContactField.FIELD_JOB_TITLE);
        int FIELD_DEPARTMENT = importFile.getColumnID(ImportField.ContactField.FIELD_DEPARTMENT);
        //Account Information
        int FIELD_ACCOUNT = importFile.getColumnID(ImportField.ContactField.FIELD_ACCOUNT);
        //Contact Information
        Map<Integer, Map<Integer, List>> importingParams = new HashMap<>();
        Map<Integer, ArrayList<Integer>> FIELD_EMAILS = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_EMAILS);
        Set<Integer> emailColumns = new HashSet<>();
        List<Integer> homeEmails = null;
        List<Integer> workEmails = null;
        List<Integer> otherEmails = null;
        if (FIELD_EMAILS != null && FIELD_EMAILS.size() > 0) {
            homeEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_HOME_EMAILS);
            workEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_WORK_EMAILS);
            otherEmails = FIELD_EMAILS.get(ImportField.ContactField.FIELD_OTHER_EMAILS);
            if (homeEmails != null) {
                emailColumns.addAll(homeEmails);
            }
            if (workEmails != null) {
                emailColumns.addAll(workEmails);
            }
            if (otherEmails != null) {
                emailColumns.addAll(otherEmails);
            }
            importingParams.put(EdsCrmContactItemParams.EMAIL, getImportingParamAsMap(EdsCrmContactItemParams.EMAIL_PARAMS, null, new List[]{homeEmails, workEmails, otherEmails}));
        }
        Map<Integer, ArrayList<Integer>> FIELD_PHONES = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_PHONES);
        Set<Integer> phoneColumns = new HashSet<>();
        List<Integer> homePhones = null;
        List<Integer> workPhones = null;
        List<Integer> mobilePhones = null;
        List<Integer> homeFaxes = null;
        List<Integer> workFaxes = null;
        List<Integer> pagerPhones = null;
        List<Integer> otherPhones = null;
        List<Integer> extensionPhones = null;
        if (FIELD_PHONES != null && FIELD_PHONES.size() > 0) {
            homePhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_HOME_PHONES);
            workPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_WORK_PHONES);
            mobilePhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_MOBILE_PHONES);
            homeFaxes = FIELD_PHONES.get(ImportField.ContactField.FIELD_HOMEFAX_PHONES);
            workFaxes = FIELD_PHONES.get(ImportField.ContactField.FIELD_WORKFAX_PHONES);
            pagerPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_PAGER_PHONES);
            otherPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_OTHER_PHONES);
            extensionPhones = FIELD_PHONES.get(ImportField.ContactField.FIELD_EXTENSION);
            if (homePhones != null) {
                phoneColumns.addAll(homePhones);
            }
            if (workPhones != null) {
                phoneColumns.addAll(workPhones);
            }
            if (mobilePhones != null) {
                phoneColumns.addAll(mobilePhones);
            }
            if (homeFaxes != null) {
                phoneColumns.addAll(homeFaxes);
            }
            if (workFaxes != null) {
                phoneColumns.addAll(workFaxes);
            }
            if (pagerPhones != null) {
                phoneColumns.addAll(pagerPhones);
            }
            if (otherPhones != null) {
                phoneColumns.addAll(otherPhones);
            }
            if (extensionPhones != null) {
                phoneColumns.addAll(extensionPhones);
            }
            importingParams.put(EdsCrmContactItemParams.PHONE, getImportingParamAsMap(EdsCrmContactItemParams.PHONE_PARAMS, null, new List[]{homePhones, workPhones, mobilePhones, homeFaxes, workFaxes, pagerPhones, otherPhones, extensionPhones}));
        }
        Map<Integer, ArrayList<Integer>> FIELD_IMS = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_IMS);
        Set<Integer> imColumns = new HashSet<>();
        List<Integer> iMgtalks = null;
        List<Integer> iMAIMs = null;
        List<Integer> iMyahoos = null;
        List<Integer> iMskypes = null;
        List<Integer> iMqqs = null;
        List<Integer> iMmsns = null;
        List<Integer> iMICQs = null;
        List<Integer> iMJabbers = null;
        if (FIELD_IMS != null && FIELD_IMS.size() > 0) {
            iMgtalks = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_GTALKS);
            iMAIMs = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_AIMS);
            iMyahoos = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_YAHOOS);
            iMskypes = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_SKYPES);
            iMqqs = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_QQS);
            iMmsns = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_MSNS);
            iMICQs = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_ICQS);
            iMJabbers = FIELD_IMS.get(ImportField.ContactField.FIELD_IM_JABBERS);
            if (iMgtalks != null) {
                imColumns.addAll(iMgtalks);
            }
            if (iMAIMs != null) {
                imColumns.addAll(iMAIMs);
            }
            if (iMyahoos != null) {
                imColumns.addAll(iMyahoos);
            }
            if (iMskypes != null) {
                imColumns.addAll(iMskypes);
            }
            if (iMqqs != null) {
                imColumns.addAll(iMqqs);
            }
            if (iMmsns != null) {
                imColumns.addAll(iMmsns);
            }
            if (iMICQs != null) {
                imColumns.addAll(iMICQs);
            }
            if (iMJabbers != null) {
                imColumns.addAll(iMJabbers);
            }
            importingParams.put(EdsCrmContactItemParams.IMADDRESS, getImportingParamAsMap(EdsCrmContactItemParams.IM_PARAMS, null, new List[]{iMgtalks, iMAIMs, iMyahoos, iMskypes, iMqqs, iMmsns, iMICQs, iMJabbers}));
        }
        Map<Integer, ArrayList<Integer>> FIELD_WEBS = importFile.getExtraColumnsAsMap(ImportField.ContactField.FIELD_WEBS);
        Set<Integer> webColumns = new HashSet<>();
        List<Integer> webHome = null;
        List<Integer> webWork = null;
        List<Integer> webHomePages = null;
        List<Integer> webFTP = null;
        List<Integer> webBLOG = null;
        List<Integer> webProfile = null;
        List<Integer> webOther = null;
        List<Integer> webLinkedIn = null;
        List<Integer> webFacebook = null;
        List<Integer> webTwitter = null;
        List<Integer> webInstagram = null;
        if (FIELD_WEBS != null && FIELD_WEBS.size() > 0) {
            webHome = FIELD_WEBS.get(ImportField.ContactField.FIELD_HOME_WEB_ADDRESSES);
            webWork = FIELD_WEBS.get(ImportField.ContactField.FIELD_WORK_WEB_ADDRESSES);
            webHomePages = FIELD_WEBS.get(ImportField.ContactField.FIELD_HOMEPAGE_WEB_ADDRESSES);
            webFTP = FIELD_WEBS.get(ImportField.ContactField.FIELD_FTP_WEB_ADDRESSES);
            webBLOG = FIELD_WEBS.get(ImportField.ContactField.FIELD_BLOG_WEB_ADDRESSES);
            webProfile = FIELD_WEBS.get(ImportField.ContactField.FIELD_PROFILE_WEB_ADDRESSES);
            webOther = FIELD_WEBS.get(ImportField.ContactField.FIELD_OTHER_WEB_ADDRESSES);
            webLinkedIn = FIELD_WEBS.get(ImportField.ContactField.FIELD_LINKEDIN_WEB_ADDRESSES);
            webFacebook = FIELD_WEBS.get(ImportField.ContactField.FIELD_FACEBOOK_WEB_ADDRESSES);
            webTwitter = FIELD_WEBS.get(ImportField.ContactField.FIELD_TWITTER_WEB_ADDRESSES);
            webInstagram = FIELD_WEBS.get(ImportField.ContactField.FIELD_INSTAGRAM_WEB_ADDRESSES);
            if (webHome != null) {
                webColumns.addAll(webHome);
            }
            if (webWork != null) {
                webColumns.addAll(webWork);
            }
            if (webHomePages != null) {
                webColumns.addAll(webHomePages);
            }
            if (webFTP != null) {
                webColumns.addAll(webFTP);
            }
            if (webBLOG != null) {
                webColumns.addAll(webBLOG);
            }
            if (webProfile != null) {
                webColumns.addAll(webProfile);
            }
            if (webOther != null) {
                webColumns.addAll(webOther);
            }
            if (webLinkedIn != null) {
                webColumns.addAll(webLinkedIn);
            }
            if (webFacebook != null) {
                webColumns.addAll(webFacebook);
            }
            if (webTwitter != null) {
                webColumns.addAll(webTwitter);
            }
            if (webInstagram != null) {
                webColumns.addAll(webInstagram);
            }
            importingParams.put(EdsCrmContactItemParams.WEBSITE, getImportingParamAsMap(EdsCrmContactItemParams.WEB_PARAMS, null, new List[]{webHome, webWork, webHomePages, webFTP, webBLOG, webProfile, webOther, webLinkedIn, webFacebook, webTwitter, webInstagram}));
        }
        //Address Information
        Map<Integer, HashMap<String, ArrayList<Integer>>> FIELD_ADDRESSES = importFile.getExtraColumnsAsMapList(ImportField.ContactField.FIELD_ADDRESSES);
        Map<Integer, Map<String, ArrayList<Integer>>> importingAddresses = new HashMap<>();
        Set<Integer> addressColumns = new HashSet<>();
        Map<String, ArrayList<Integer>> homeAddresses = null;
        Map<String, ArrayList<Integer>> workAddresses = null;
        Map<String, ArrayList<Integer>> otherAddresses = null;
        if (FIELD_ADDRESSES != null && FIELD_ADDRESSES.size() > 0) {
            homeAddresses = FIELD_ADDRESSES.get(ImportField.ContactField.FIELD_HOME_ADDRESSES);
            workAddresses = FIELD_ADDRESSES.get(ImportField.ContactField.FIELD_WORK_ADDRESSES);
            otherAddresses = FIELD_ADDRESSES.get(ImportField.ContactField.FIELD_OTHER_ADDRESSES);
            importingAddresses.put(EdsAddress.HOME, homeAddresses);
            importingAddresses.put(EdsAddress.WORK, workAddresses);
            importingAddresses.put(EdsAddress.OTHER, otherAddresses);
            for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> entry : FIELD_ADDRESSES.entrySet()) {
                if (entry != null && entry.getValue() != null) {
                    for (String columns : entry.getValue().keySet()) {
                        if (columns != null) {
                            for (String columnID_ : columns.split(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                                if (columnID_.matches(Constants.REGEX_INTEGER)) {
                                    addressColumns.add(Integer.parseInt(columnID_));
                                }
                            }
                        }
                    }
                }
            }
        }
        //Crm Details
        boolean isCategoryFromFile = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_CATEGORY_FROMFILE));
        int FIELD_CATEGORY_ID = importFile.getColumnID(ImportField.ContactField.FIELD_CATEGORY);
        boolean isPrimaryContact = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_PRIMARY_CONTACT));
        boolean isCampaignFromFile = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_CAMPAIGN_FROMFILE));
        int FIELD_CAMPAIGN = importFile.getColumnID(ImportField.ContactField.FIELD_CAMPAIGN);
        boolean isEmailOptOut = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_EMAIL_OPT));
        //Lead Information
        boolean isAssigneeFromFile = ((Integer) 1).equals(importFile.getColumnID(ImportField.ContactField.FIELD_ASSIGNEE_FROMFILE));
        EdsEmployee assignee = null;
        if (!isAssigneeFromFile && importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_ASSIGNEE) != null) {
            assignee = employeeManager.get(importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_ASSIGNEE));
        }
        int FIELD_LEAD_ASSIGNEE = importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_ASSIGNEE);
        int FIELD_LEAD_BACKUP_ASSIGNEE = importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_BACKUP_ASSIGNEE);
        int FIELD_LEAD_SOURCE = importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_SOURCE);
        int FIELD_LEAD_STATUS = importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_STATUS);
        int FIELD_LEAD_RATING = importFile.getColumnID(ImportField.ContactField.FIELD_LEAD_RATING);
        //Candidate Information
        int FIELD_CANDIDATE_PROJECT = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_PROJECT);
        int FIELD_CANDIDATE_SOURCE = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_SOURCE);
        int FIELD_CANDIDATE_STATUS = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_STATUS);
        int FIELD_CANDIDATE_CREATED_DATE = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_CREATED_DATE);
        int FIELD_CANDIDATE_VACANCIES = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_VACANCIES);
        int FIELD_CANDIDATE_WORK_EXPERIENCE = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_WORK_EXPERIENCE);
        int FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR);
        int FIELD_CANDIDATE_CURRENT_EMPLOYER = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_CURRENT_EMPLOYER);
        int FIELD_CANDIDATE_EXPECTED_SALARY = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_EXPECTED_SALARY);
        int FIELD_CANDIDATE_LOCATION = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_LOCATION);
        int FIELD_CANDIDATE_SKILLS = importFile.getColumnID(ImportField.ContactField.FIELD_CANDIDATE_SKILLS);
        boolean hasHeader = importFile.isHasHeader();
        nextRow:
        for (List<String> row : (List<List<String>>) listOfRows) {
            rejectedRow = new RejectedImportRecord[row.size()];
            List<CompanyCustomFieldItem> customFields = new ArrayList<>();
            EdsCrmContact contact = null;
            boolean rowIsEmpty = true;
            boolean duplicateDetected = false;
            boolean errorFound = false;
            String detectionValue = null;
            Integer rowID = 0;
            for (String str : row) {
                rejectedRow[rowID] = new RejectedImportRecord(str);
                if (isValid(str)) {
                    str = str.trim();
                    rowIsEmpty = false;
                    if (companySettings.getOverwritePreference() != null && companySettings.getOverwritePreference().equals("BY_PHONE") && !duplicateDetected && FIELD_PHONES != null && FIELD_PHONES.size() > 0 && phoneColumns.contains(rowID) && contactIDs.containsKey(str.toLowerCase())) {
                        duplicateDetected = true;
                        detectionValue = str.toLowerCase();
                    } else if (!duplicateDetected && FIELD_EMAILS != null && FIELD_EMAILS.size() > 0 && emailColumns.contains(rowID) && contactIDs.containsKey(str.toLowerCase())) {
                        duplicateDetected = true;
                        detectionValue = str.toLowerCase();
                    }
                }
                rowID++;
            }
            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue nextRow;
            }
            if (rowIsEmpty) {
                continue nextRow;
            }
            if (duplicateDetected) {
                if (importFile.isSkip()) {
                    rejectedRecords.add(rejectedRow);
                    importFile.setSkippedColumns(importFile.getSkippedColumns() + 1);
                    continue nextRow;
                } else if (importFile.isMerge()) {
                    contact = crmContactManager.get(contactIDs.get(detectionValue));
                }
            }
            if (contact == null) {
                contact = new EdsCrmContact();
            } else {
                contact.getAddresses().clear();
            }
            List<EdsCrmContactItemParams> params = new ArrayList<>();
            Map<Integer, EdsAddress> addresses = new HashMap<>();
            boolean primaryEmailSet = false;
            boolean primaryPhoneSet = false;
            int csvColumnID = 0;
            for (String columnValue : row) {
                if (isValid(columnValue)) {
                    columnValue = StringUtil.cut(columnValue, 255).trim();
                    String columnLowerCase = columnValue.toLowerCase();
                    if (csvColumnID == FIELD_OWNER && isOwnerFromFile) {
                        Integer ownerID = ownerIDs.get(columnValue);
                        if (ownerID != null) {
                            contact.setOwner(employeeManager.get(ownerID));
                        } else {
                            rejectedRow[FIELD_OWNER].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            errorFound = true;
                        }
                    }
                    if (csvColumnID == FIELD_FIRSTNAME) {
                        contact.setFirstName(columnValue);
                    }
                    if (csvColumnID == FIELD_LASTNAME) {
                        contact.setLastName(columnValue);
                    }
                    if (csvColumnID == FIELD_BIRTHDAY) {
                        if (columnValue.matches("(\\d\\d?)/(\\d\\d?)/(\\d\\d\\d\\d)")) {
                            try {
                                contact.setDateOfBirth(format.parse(columnValue));
                            } catch (Exception e) {
                                rejectedRow[FIELD_BIRTHDAY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateFormat, columnValue));
                                errorFound = true;
                            }
                        } else {
                            rejectedRow[FIELD_BIRTHDAY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateFormat, columnValue));
                            errorFound = true;
                        }
                    }
                    if (csvColumnID == FIELD_TITLE) {
                        contact.setTitle(columnValue);
                    }
                    if (!isCandidateImport) {
                        if (csvColumnID == FIELD_JOB_TITLE) {
                            contact.setJobTitles(columnValue);
                        }
                        if (csvColumnID == FIELD_DEPARTMENT) {
                            contact.setDepartment(columnValue);
                        }
                        if (csvColumnID == FIELD_ACCOUNT) {
                            Integer accountID = accountIDs.get(columnLowerCase);
                            if (accountID != null) {
                                EdsCrmAccount edsCrmAccount = crmAccountManager.get(accountID);
                                contact.setCrmAccount(edsCrmAccount);
                                contact.setEntityID(edsCrmAccount != null ? edsCrmAccount.getEntityID() : null);
                            } else {
                                rejectedRow[FIELD_ACCOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                    }
                    if (FIELD_EMAILS != null && FIELD_EMAILS.size() > 0 && emailColumns.contains(csvColumnID)) {
                        boolean created = createImportedParams(EdsCrmContactItemParams.EMAIL, importingParams, params, columnValue, csvColumnID, contactParams, companyDate, contact.getObjectID());
                        if (!primaryEmailSet && created) {
                            contact.setPrimaryEmail(columnValue);
                            primaryEmailSet = true;
                        }
                    }
                    if (FIELD_PHONES != null && FIELD_PHONES.size() > 0 && phoneColumns.contains(csvColumnID)) {
                        boolean created = createImportedParams(EdsCrmContactItemParams.PHONE, importingParams, params, columnValue, csvColumnID, contactParams, companyDate, contact.getObjectID());
                        if (!primaryPhoneSet && created) {
                            contact.setPrimaryPhone(columnValue);
                            primaryPhoneSet = true;
                        }
                    }
                    if (FIELD_IMS != null && FIELD_IMS.size() > 0 && imColumns.contains(csvColumnID)) {
                        createImportedParams(EdsCrmContactItemParams.IMADDRESS, importingParams, params, columnValue, csvColumnID, contactParams, companyDate, contact.getObjectID());
                    }
                    if (FIELD_WEBS != null && FIELD_WEBS.size() > 0 && webColumns.contains(csvColumnID)) {
                        createImportedParams(EdsCrmContactItemParams.WEBSITE, importingParams, params, columnValue, csvColumnID, contactParams, companyDate, contact.getObjectID());
                    }
                    if (FIELD_ADDRESSES != null && FIELD_ADDRESSES.size() > 0 && addressColumns.contains(csvColumnID)) {
                        createAddresses(addresses, countriesIDs, regionsIDs, csvColumnID, columnValue, importingAddresses);
                    }
                    if (csvColumnID == FIELD_CATEGORY_ID && isCategoryFromFile) {
                        Integer categoryID = categoriesIDs.get(columnLowerCase);
                        if (categoryID != null) {
                            contact.addCategoryID(categoryID);
                        } else {
                            rejectedRow[FIELD_CATEGORY_ID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            errorFound = true;
                        }
                    }
                    if (!isCandidateImport) {
                        if (csvColumnID == FIELD_CAMPAIGN && isCampaignFromFile) {
                            Integer campaingsID = campaignsIDs.get(columnLowerCase);
                            if (campaingsID != null) {
                                contact.setCampaign(campaignManager.get(campaingsID));
                            } else {
                                EdsCampaign campaign = new EdsCampaign();
                                campaign.setName(columnValue);
                                campaignManager.create(campaign);
                                contact.setCampaign(campaign);
                                campaignsIDs.put(columnLowerCase, campaign.getObjectID());
                            }
                        }
                    }
                    if (isLeadImport) {
                        if (csvColumnID == FIELD_LEAD_ASSIGNEE && isAssigneeFromFile) {
                            Integer assigneeID = assigneeIDs.get(columnLowerCase);
                            if (assigneeID != null) {
                                contact.setLeadAssignee(employeeManager.get(assigneeID));
                            } else {
                                rejectedRow[FIELD_LEAD_ASSIGNEE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_LEAD_SOURCE) {
                            EdsReference reference = getReference(sourcesIDs, columnLowerCase);
                            if (reference != null) {
                                contact.setLeadSource(reference);
                            } else {
                                rejectedRow[FIELD_LEAD_SOURCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_LEAD_STATUS) {
                            EdsReference reference = getReference(statusesIDs, columnLowerCase);
                            if (reference != null) {
                                contact.setLeadStatus(reference);
                            } else {
                                rejectedRow[FIELD_LEAD_STATUS].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_LEAD_RATING) {
                            EdsReference reference = getReference(leadRatingsIDs, columnLowerCase);
                            if (reference != null) {
                                contact.setLeadRating(reference);
                            } else {
                                rejectedRow[FIELD_LEAD_RATING].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                    } else if (isCandidateImport) {
                        if (csvColumnID == FIELD_CANDIDATE_PROJECT) {
                            List<EdsProject> projectList = projectManager.getProjectByName(columnValue);
                            if (projectList != null && projectList.size() > 0) {
                                contact.setCandidateProject(projectList.get(0));
                            } else {
                                rejectedRow[FIELD_CANDIDATE_PROJECT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_SOURCE) {
                            EdsReference reference = getReference(sourcesIDs, columnLowerCase);
                            if (reference != null) {
                                contact.setLeadSource(reference);
                            } else {
                                rejectedRow[FIELD_CANDIDATE_SOURCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_STATUS) {
                            EdsReference reference = getReference(statusesIDs, columnLowerCase);
                            if (reference != null) {
                                contact.setLeadStatus(reference);
                            } else {
                                rejectedRow[FIELD_CANDIDATE_STATUS].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_CREATED_DATE) {
                            if (columnValue.matches("(\\d\\d?)/(\\d\\d?)/(\\d\\d\\d\\d)")) {
                                try {
                                    contact.getAuditInfo().setCreationDate(format.parse(columnValue));
                                } catch (Exception e) {
                                    rejectedRow[FIELD_CANDIDATE_CREATED_DATE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateFormat, columnValue));
                                    errorFound = true;
                                }
                            } else {
                                rejectedRow[FIELD_CANDIDATE_CREATED_DATE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.invalidDateFormat, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_VACANCIES) {
                            if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
                                columnValue = columnValue.substring(1, columnValue.length() - 1);
                            }
                            String[] splits = columnValue.split(MULTIVALUE_SEPARATOR);
                            if (splits != null) {
                                for (String vacancy : splits) {
                                    Integer vacancyID = vacancyIDs.get(vacancy.toLowerCase().trim());
                                    if (vacancyID != null) {
                                        contact.getVacancies().add(vacancyManager.get(vacancyID));
                                    } else {
                                        rejectedRow[FIELD_CANDIDATE_VACANCIES].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                        errorFound = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_WORK_EXPERIENCE) {
                            try {
                                contact.setWorkExperience(Integer.valueOf(columnValue));
                            } catch (NumberFormatException e) {
                                rejectedRow[FIELD_CANDIDATE_WORK_EXPERIENCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_EXPECTED_SALARY) {
                            try {
                                contact.setExpectedSalary(Double.valueOf(columnValue));
                            } catch (NumberFormatException e) {
                                rejectedRow[FIELD_CANDIDATE_EXPECTED_SALARY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_CURRENT_EMPLOYER) {
                            contact.setCurrentEmployer(columnValue);
                        }
                        if (csvColumnID == FIELD_CANDIDATE_LOCATION) {
                            Integer locationID = locationIDs.get(columnLowerCase);
                            if (locationID != null) {
                                contact.setPrefferedLocation(locationManager.get(locationID));
                            } else {
                                rejectedRow[FIELD_CANDIDATE_LOCATION].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                errorFound = true;
                            }
                        }
                        if (csvColumnID == FIELD_CANDIDATE_SKILLS) {
                            contact.setSkills(columnValue);
                        }
                    }
                    if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                        for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                            if (csvColumnID != importFile.getExtraColumnID(extraColumnEntry.getValue()) || extraColumnEntry.getKey() < ImportField.ContactField.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                continue;
                            }
                            String fieldName = rejectedRecords != null && rejectedRecords.size() > 0 && rejectedRecords.get(0).length > csvColumnID ? rejectedRecords.get(0)[csvColumnID].getData() : "";
                            CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, csvColumnID, columnValue, rejectedRow, fieldName);
                            if (customField == null) {
                                errorFound = true;
                                continue;
                            }
                            customFields.add(customField);
                        }
                    }
                }
                csvColumnID++;
            }
            if (errorFound) {
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                rejectedRecords.add(rejectedRow);
                continue nextRow;
            }
            contact.setImportFileID(importFile.getObjectID());
            contact.setContactType(contactType);
            contact.setEmailOptOut(isEmailOptOut);
            contact.setPrimaryContact(isPrimaryContact);
            if (!isAssigneeFromFile && assignee != null && FIELD_LEAD_ASSIGNEE != -1) {
                contact.setLeadAssignee(assignee);
            }
            if (FIELD_LEAD_BACKUP_ASSIGNEE != -1 && FIELD_LEAD_BACKUP_ASSIGNEE != FIELD_LEAD_ASSIGNEE) {
                contact.setLeadBackupAssignee(employeeManager.get(FIELD_LEAD_BACKUP_ASSIGNEE));
            }
            if (!isOwnerFromFile && owner != null) {
                contact.setOwner(owner);
            }
            if (!isCandidateImport && !isCampaignFromFile && importFile.getColumnID(ImportField.ContactField.FIELD_CAMPAIGN) != null) {
                contact.setCampaign(campaignManager.get(importFile.getColumnID(ImportField.ContactField.FIELD_CAMPAIGN)));
            }
            if (!(isLeadImport || isCandidateImport) && !isCategoryFromFile && importFile.getColumnID(ImportField.ContactField.FIELD_CATEGORY) != null) {
                contact.addCategoryID(importFile.getColumnID(ImportField.ContactField.FIELD_CATEGORY));
            }
            if (isCandidateImport && FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR != -1) {
                contact.setWorkExperienceMonthOrYear(FIELD_CANDIDATE_WORK_EXPERIENCE_MONTH_YEAR);
            }
            contact.setTransientCustomFields(customFields.toArray(new CompanyCustomFieldItem[]{}));
            contact.setItemParamsTransient(params);
            contact.setAddressesTransient(addresses);
            contactList.add(contact);
            contactList = batchImportContact(contactList, false, itemParams, itemAddresses);
            if (contactList.size() % flushLimit == 0) {
                userManager.flushAndClear();
            }
            if (duplicateDetected) {
                if (importFile.isClone()) {
                    importFile.setClonedColumns(importFile.getClonedColumns() + 1);
                }
                if (importFile.isMerge()) {
                    importFile.setOverwrittenColumns(importFile.getOverwrittenColumns() + 1);
                }
            }
        }
        batchImportContact(contactList, true, itemParams, itemAddresses);
        return rejectedRecords;
    }

    @Transactional
    public Set<Integer> importVCardContacts(ImportFile importFile, List<VCard> listOfRows, Set<Integer> emailHashCodesInTheSystem, int contactType) throws Exception {
        boolean isLeadImport = ContactListItem.LEAD_CONTACT.equals(contactType);
        boolean isCandidateImport = ContactListItem.CANDIDATE.equals(contactType);
        List<EdsCrmContact> contactList = new ArrayList<>();
        EdsUser employee = employeeManager.getUser();
        final List<EdsCrmContactItemParams> itemParams = new ArrayList<>();
        final List<EdsAddress> itemAddresses = new ArrayList<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setCompanyID(employee.getCompany().getObjectID());
        fp.setLookUp(true);

        Date companyDate = ServerUtils.getCompanyDate(new Date(), employee.getCompany());
        for (VCard s : listOfRows) {
            EdsCrmContact contact = null;
            boolean duplicateDetected = false;

            if (contact == null) {
                contact = new EdsCrmContact();
            }
            List<EdsCrmContactItemParams> params = new ArrayList<>();
            Map<Integer, EdsAddress> addresses = new HashMap<>();
            boolean primaryEmailSet = false;
            boolean primaryPhoneSet = false;
            contact.setImportFileID(importFile.getObjectID());
            contact.setOwner(employee);
            contact.setContactType(isLeadImport ? EdsCrmContact.LEAD_CONTACT : (isCandidateImport ? EdsCrmContact.CANDIDATE : EdsCrmContact.CRM_CONTACT));

            if (s.getStructuredName() != null) {
                contact.setFirstName(s.getStructuredName().getGiven());
                contact.setLastName(s.getStructuredName().getFamily());

            }
            if (s.getTitles() != null && s.getTitles().size() > 0) {
                contact.setTitle(s.getTitles().get(0).getValue());
            }

            if (s.getNickname() != null && s.getNickname().getValues() != null && s.getNickname().getValues().size() > 0) {
                contact.setOtherName(s.getNickname().getValues().get(0));
            }

            if (s.getBirthday() != null && s.getBirthday().getDate() != null) {
                contact.setDateOfBirth(s.getBirthday().getDate());
            }

            for (Email email : s.getEmails()) {
                EdsCrmContactItemParams itemParam = new EdsCrmContactItemParams(EdsCrmContactItemParams.EMAIL);
                itemParam.setLastUpdateTime(companyDate);
                itemParam.setValue(email.getValue());
                for (EmailType type : email.getTypes()) {
                    if (type == EmailType.WORK) {
                        itemParam.setRelation(EdsCrmContactItemParams.WORK);
                    } else if (type == EmailType.HOME) {
                        itemParam.setRelation(EdsCrmContactItemParams.HOME);
                    } else {
                        itemParam.setRelation(EdsCrmContactItemParams.OTHER);
                    }
                }
                if (!primaryEmailSet) {
                    contact.setPrimaryEmail(email.getValue());
                    primaryEmailSet = true;
                }
                params.add(itemParam);
            }

            for (Telephone phone : s.getTelephoneNumbers()) {
                EdsCrmContactItemParams itemParam = new EdsCrmContactItemParams(EdsCrmContactItemParams.PHONE);
                itemParam.setLastUpdateTime(companyDate);
                itemParam.setValue(phone.getText());
                for (TelephoneType type : phone.getTypes()) {
                    if (type == TelephoneType.WORK) {
                        itemParam.setRelation(EdsCrmContactItemParams.WORK);
                        if (!primaryPhoneSet) {
                            contact.setPrimaryPhone(phone.getText());
                            primaryPhoneSet = true;
                        }
                    } else if (type == TelephoneType.HOME) {
                        itemParam.setRelation(EdsCrmContactItemParams.HOME);
                        if (!primaryPhoneSet) {
                            contact.setPrimaryPhone(phone.getText());
                            primaryPhoneSet = true;
                        }
                    } else if (type == TelephoneType.CELL) {
                        itemParam.setRelation(EdsCrmContactItemParams.MOBILE);
                        if (!primaryPhoneSet) {
                            contact.setPrimaryPhone(phone.getText());
                            primaryPhoneSet = true;
                        }
                    } else if (type == TelephoneType.PAGER) {
                        itemParam.setRelation(EdsCrmContactItemParams.PAGER);
                    } else if (type == TelephoneType.FAX) {
                        itemParam.setRelation(EdsCrmContactItemParams.WORK_FAX);
                    } else {
                        itemParam.setRelation(EdsCrmContactItemParams.OTHER);
                    }
                }
                if (!primaryPhoneSet) {
                    contact.setPrimaryPhone(phone.getText());
                    primaryPhoneSet = true;
                }
                params.add(itemParam);
            }

            for (ezvcard.property.Address vAddress : s.getAddresses()) {
                EdsAddress address = new EdsAddress();
                address.setName(vAddress.getLabel());
                address.setAddress(vAddress.getStreetAddress());
                address.setAddressb(vAddress.getExtendedAddress());
                address.setZipCode(vAddress.getPostalCode());

                for (AddressType addressType : vAddress.getTypes()) {
                    if (addressType == AddressType.HOME) {
                        address.setRelationType(EdsAddress.HOME);
                        addresses.put(EdsAddress.HOME, address);
                    } else if (addressType == AddressType.WORK) {
                        address.setRelationType(EdsAddress.WORK);
                        addresses.put(EdsAddress.WORK, address);
                    } else {
                        address.setRelationType(EdsAddress.OTHER);
                        addresses.put(EdsAddress.OTHER, address);
                    }
                }

            }

            contact.setItemParamsTransient(params);
            contact.setAddressesTransient(addresses);

            contact.setEmailOptOut(false);

            if (!duplicateDetected || !importFile.isSkip()) {
                contactList.add(contact);
            }
            contactList = batchImportContact(contactList, false, itemParams, itemAddresses);
            if (contactList.size() % flushLimit == 0) {
                userManager.flushAndClear();
            }
        }
        batchImportContact(contactList, true, itemParams, itemAddresses);
        return emailHashCodesInTheSystem;
    }

    private void createAddresses(final Map<Integer, EdsAddress> addresses, Map<String, Integer> countriesIDs, Map<String, Integer> regionsIDs, Integer columnID, String value, Map<Integer, Map<String, ArrayList<Integer>>> setting) {
        for (Map.Entry<Integer, Map<String, ArrayList<Integer>>> entry : setting.entrySet()) {
            Integer relation = entry.getKey();
            Map<String, ArrayList<Integer>> miniAddresses = entry.getValue();
            if (miniAddresses != null && miniAddresses.size() > 0) {
                for (String columns : miniAddresses.keySet()) {
                    if (columns.endsWith(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID + columnID.toString()) || columns.startsWith(columnID + ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID) || columns.contains(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID + columnID + ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                        EdsAddress address = null;
                        if (addresses.containsKey(columns.hashCode())) {
                            address = addresses.get(columns.hashCode());
                        } else {
                            address = new EdsAddress();
                            address.setRelationType(relation);
                            addresses.put(columns.hashCode(), address);
                        }
                        setAddressValue(address, miniAddresses.get(columns).indexOf(columnID), value, countriesIDs, regionsIDs, false);
                    }
                }
            }
        }

    }

    private void createAccountAddresses(final Map<Integer, EdsAddress> addresses, Map<String, Integer> countriesIDs, Map<String, Integer> regionsIDs, Integer columnID, String value, Map<Integer, HashMap<String, ArrayList<Integer>>> setting, boolean isContactImport) {
        for (Map.Entry<Integer, HashMap<String, ArrayList<Integer>>> entry : setting.entrySet()) {
            Integer relation = entry.getKey();
            Map<String, ArrayList<Integer>> miniAddresses = entry.getValue();
            if (miniAddresses != null && miniAddresses.size() > 0) {
                for (String columns : miniAddresses.keySet()) {
                    if (columns.endsWith(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID + columnID.toString()) || columns.startsWith(columnID + ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID) || columns.contains(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID + columnID + ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                        EdsAddress address = null;
                        if (addresses.containsKey(columns.hashCode())) {
                            address = addresses.get(columns.hashCode());
                        } else {
                            address = new EdsAddress();
                            address.setRelationType(relation);
                            addresses.put(columns.hashCode(), address);
                        }
                        setAddressValue(address, miniAddresses.get(columns).indexOf(columnID), value, countriesIDs, regionsIDs, !isContactImport);
                    }
                }
            }
        }

    }

    private Map<Integer, List<Integer>> getImportingParamAsMap(int[] params, Integer[] entityParams, List<Integer>[] listsOfColumnIDs) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        if (params != null && params.length > 0 && listsOfColumnIDs != null && listsOfColumnIDs.length > 0 && params.length == listsOfColumnIDs.length) {
            for (int i = 0; i < params.length; i++) {
                map.put(params[i], listsOfColumnIDs[i]);
            }
        } else if (entityParams != null && entityParams.length > 0 && listsOfColumnIDs != null && listsOfColumnIDs.length > 0 && entityParams.length == listsOfColumnIDs.length) {
            for (int i = 0; i < entityParams.length; i++) {
                if (listsOfColumnIDs[i] != null) {
                    map.put(entityParams[i], listsOfColumnIDs[i]);
                }
            }
        }
        return map;
    }

    private boolean createImportedParams(int param, Map<Integer, Map<Integer, List>> allSettings, final List<EdsCrmContactItemParams> params, String value, Integer columnID, Map<Integer, Map<Integer, Map<Integer, ArrayList<String>>>> existings, Date companyDate, Integer contactID) {
        boolean result = false;
        Map<Integer, List> setting = allSettings.get(param);
        if (setting != null) {
            for (Map.Entry<Integer, List> entry : setting.entrySet()) {
                List<Integer> listOfColumnIDs = (List<Integer>) entry.getValue();
                Integer relation = entry.getKey();
                if (listOfColumnIDs != null) {
                    if (listOfColumnIDs.contains(columnID)) {
                        if (contactID == null || existings == null || !existings.containsKey(contactID) || !existings.get(contactID).containsKey(param) || !existings.get(contactID).get(param).containsKey(relation) || !existings.get(contactID).get(param).get(relation).contains(value)) {
                            result = true;
                            EdsCrmContactItemParams itemParam = new EdsCrmContactItemParams(param);
                            itemParam.setRelation(relation);
                            itemParam.setLastUpdateTime(companyDate);
                            itemParam.setValue(value);
                            if (itemParam.getRelation() != null && !"".equals(itemParam.getRelation()) && !params.contains(itemParam)) {
                                params.add(itemParam);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Transactional
    public List<EdsCrmContact> batchImportContact(List<EdsCrmContact> contacts, boolean forceToCreate, final List<EdsCrmContactItemParams> itemParams, final List<EdsAddress> itemAddresses) {
        if (contacts.size() == flushLimit || (forceToCreate && contacts.size() > 0)) {
            for (EdsCrmContact contact : contacts) {
                if (contact.getEntityID() == null) {
                    EdsEntity entity = new EdsEntity();
                    entityManager.create(entity);
                    contact.setEntityID(entity.getObjectID());
                }
                List<EdsCrmContactItemParams> itemParamArray = null;
                List<EdsAddress> itemAddressArray = null;
                if (contact.getItemParamsTransient() != null && contact.getItemParamsTransient().size() > 0) {
                    itemParamArray = contact.getItemParamsTransient();
                    contact.setItemParamsTransient(null);
                }
                if (contact.getAddressesTransient() != null && contact.getAddressesTransient().size() > 0) {
                    itemAddressArray = new ArrayList<>(contact.getAddressesTransient().values());
                    contact.setAddressesTransient(null);
                }
                if (contact.getCateogryIDs().size() > 0) {
                    for (Integer id : contact.getCateogryIDs()) {
                        if (id != null) {
                            EdsContactCategory edsContactCategory = contactCategoryManager.get(id);
                            contact.addCategories(edsContactCategory);
                        }
                    }
                }
                contact.setCustomFields(createCustomField(contact.getCustomFields(), contact.getTransientCustomFields()));

                if (contact.isPrimaryContact()) {
                    EdsCrmAccount edsCrmAccount = contact.getCrmAccount();
                    if (edsCrmAccount != null) {
                        for (EdsCrmContact crmContact : edsCrmAccount.getCrmContacts()) {
                            if (!crmContact.getObjectID().equals(contact.getObjectID())) {
                                crmContact.setPrimaryContact(Boolean.FALSE);
                            }
                        }
                    }
                }

                crmContactManager.createOrUpdate(contact);
                if (itemParamArray != null && itemParamArray.size() > 0) {
                    for (EdsCrmContactItemParams itemParam : itemParamArray) {
                        if (itemParam != null) {
                            itemParam.setContact(contact);
                            itemParams.add(itemParam);
                        }
                    }
                }
                if (itemAddressArray != null && itemAddressArray.size() > 0) {
                    boolean hasPrimary = true;
                    for (EdsAddress address : itemAddressArray) {
                        address.setContact(contact);
                        if (hasPrimary) {
                            address.setPrimary(hasPrimary);
                            hasPrimary = false;
                        }
                        if (address.isNotEmpty()) {
                            itemAddresses.add(address);
                        }
                    }
                }
            }
            userManager.flushAndClear();
            contacts = new ArrayList<>();
        }
        if (forceToCreate) {
            if ((itemParams != null && itemParams.size() > 0) || (itemAddresses != null && itemAddresses.size() > 0)) {
                createImportedContactParamsAndAddresses(itemParams, itemAddresses);
            }
            userManager.flushAndClear();
        }
        return contacts;
    }

    @Transactional
    public List<EdsOpportunity> batchImportOpportunity(List<EdsOpportunity> opportunities, boolean forceToCreate) {
        //int flushLimit = 20;
        if (opportunities.size() == flushLimit || (forceToCreate && opportunities.size() > 0)) {
            System.out.print("start " + opportunities.size() + " :" + new Date() + "    ::    ");
            int flushingCount = 0;
            for (EdsOpportunity opportunity : opportunities) {
                if (opportunity.getEntityID() == null) {
                    EdsEntity entity = new EdsEntity();
                    entityManager.create(entity);
                    flushingCount++;
                    opportunity.setEntityID(entity.getObjectID());
                }
                NumberData data = crmService.generateOpportunityNumber();
                if (data != null) {
                    opportunity.setNumber(data.getNumberString());
                    opportunity.setIntNumber(data.getIntNumber());
                }
                flushingCount++;
                opportunity.setCustomFields(createCustomField(opportunity.getCustomFields(), opportunity.getTransientCustomFields()));
                opportunityManager.createOrUpdate(opportunity);
                flushingCount++;
                if (opportunity.getNote() != null && !"".equals(opportunity.getNote())) {
                    crmService.saveCrmNote(RelationItem.TYPE_OPPORTUNITY, opportunity.getObjectID(), new HistoryListItem(opportunity.getNote()));
                }
            }
            userManager.flushAndClear();
            opportunities = new ArrayList<>();
            System.out.println(new Date());
        }
        if (forceToCreate) {
            userManager.flushAndClear();
        }
        return opportunities;
    }

    @Transactional
    public void createImportedContactParamsAndAddresses(List<EdsCrmContactItemParams> itemParams, List<EdsAddress> itemAddresses) {
        int flushingCount = 0;
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : itemParams) {
                crmContactItemParamsManager.create(itemParam);
                flushingCount++;
                if (flushingCount >= flushLimit) {
                    flushingCount = 0;
                    userManager.flushAndClear();
                }
            }
            itemParams.clear();
        }
        if (itemAddresses != null && itemAddresses.size() > 0) {
            for (EdsAddress itemAddress : itemAddresses) {
                addressManager.create(itemAddress);
                flushingCount++;
                if (flushingCount >= flushLimit) {
                    flushingCount = 0;
                    userManager.flushAndClear();
                }
            }
            itemAddresses.clear();
        }
    }

    private void setAddressValue(final EdsAddress address, int indexOf, String value, Map<String, Integer> countriesIDs, Map<String, Integer> regionsIDs, boolean isAccountAddress) {
        if (isAccountAddress) {
            switch (indexOf) {
                case 0 -> address.setName(value);
                case 1 -> address.setAddress(value);
                case 2 -> address.setAddressb(value);
                case 3 -> address.setCity(value);
                case 4 -> {
                    Integer countryID = countriesIDs.get(value.toLowerCase().trim());
                    if (countryID != null) {
                        EdsCountry edsCountry = countryManager.get(countryID);
                        address.setCountry(edsCountry);
                    }
                }
                case 5 -> {
                    Integer regionID = regionsIDs.get(value.toLowerCase().trim());
                    if (regionID != null) {
                        EdsRegion edsRegion = regionManager.get(regionID);
                        address.setState(edsRegion);
                    }
                }
                case 6 -> address.setZipCode(value);
            }
        } else {
            switch (indexOf) {
                case 0 -> address.setAddress(value);
                case 1 -> address.setAddressb(value);
                case 2 -> address.setCity(value);
                case 3 -> {
                    Integer countryID = countriesIDs.get(value.toLowerCase().trim());
                    if (countryID != null) {
                        EdsCountry edsCountry = countryManager.get(countryID);
                        address.setCountry(edsCountry);
                    }
                }
                case 4 -> {
                    Integer regionID = regionsIDs.get(value.toLowerCase().trim());
                    if (regionID != null) {
                        EdsRegion edsRegion = regionManager.get(regionID);
                        address.setState(edsRegion);
                    }
                }
                case 5 -> address.setZipCode(value);
                case 6 -> address.setName(value);
            }
        }
    }

    private Map<String, Integer> listToMapIDs(List sources) {
        Map<String, Integer> sourceMap = new HashMap<>();
        if (sources == null || sources.isEmpty()) {
            return sourceMap;
        }
        if (sources.get(0) instanceof ContactCategoryListItem) {
            for (ContactCategoryListItem object : (List<ContactCategoryListItem>) sources) {
                EdsContactCategory contactCategory = contactCategoryManager.get(object.getObjectID());
                if (contactCategory != null && contactCategory.getName() != null && !"".equals(contactCategory.getName())) {
                    sourceMap.put(contactCategory.getName().toLowerCase(), contactCategory.getObjectID());
                    sourceMap.put(contactCategory.getName(), contactCategory.getObjectID());
                }
            }
        } else {
            for (EdsObject object : (List<EdsObject>) sources) {
                if (object.getName() != null && !"".equals(object.getName())) {
                    sourceMap.put(object.getName().toLowerCase(), object.getObjectID());
                    sourceMap.put(object.getName(), object.getObjectID());
                }
            }
        }
        return sourceMap;
    }

    private Map<String, Integer> listToMapIDsByCode(List sources) {
        Map<String, Integer> sourceMap = new HashMap<>();
        if (sources != null && sources.size() > 0) {
            if (sources.get(0) instanceof ContactCategoryListItem) {
                for (ContactCategoryListItem object : (List<ContactCategoryListItem>) sources) {
                    EdsContactCategory contactCategory = contactCategoryManager.get(object.getObjectID());
                    if (contactCategory != null && contactCategory.getName() != null && !"".equals(contactCategory.getName())) {
                        sourceMap.put(contactCategory.getName().toLowerCase(), contactCategory.getObjectID());
                        sourceMap.put(contactCategory.getName(), contactCategory.getObjectID());
                    }
                }
            } else {
                for (EdsObject object : (List<EdsObject>) sources) {
                    if (object.getName() != null && !"".equals(object.getName())) {
                        sourceMap.put(object.getName().toLowerCase(), object.getObjectID());
                        sourceMap.put(object.getName(), object.getObjectID());
                    }
                }
            }
        }
        return sourceMap;
    }

    @Override
    @Transactional
    public int createEntityMailList(EdsMailList mailList, List<Integer> iDs) {
        List<Integer> existingIDs = crmEntityMailListManager.getMailListEntityIDs(mailList.getObjectID(), iDs);
        if (existingIDs != null) {
            iDs.removeAll(existingIDs);
        }
        if (iDs.size() > 0) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("INSERT INTO ").append(BaseManager.getCompanyId()).append(".leadmaillist");
            stringBuffer.append(" (maillistid, entity_id) VALUES ");
            for (Integer id : iDs) {
                stringBuffer.append("(").append(mailList.getObjectID()).append(",").append(id).append("),");
            }
            contactCategoryManager.updateNative(stringBuffer.substring(0, stringBuffer.length() - 1));
        }
        return iDs.size();

    }

    @Override
    @Transactional
    public LinkedList<RejectedImportRecord[]> importManualTransaction(ImportFile importFile, List<String[]> list) {
        LinkedList<RejectedImportRecord[]> rejectedRecords = new LinkedList<>();
        boolean hasHeader = importFile.isHasHeader();

        Integer FIELD_NUMBER = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_NUMBER);
        Integer FIELD_DATE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DATE);
        Integer FIELD_NARRATION = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_NARRATION);
        Integer FIELD_REFERENCE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_REFERENCE);
        Integer FIELD_ACCOUNT_CODE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_ACCOUNT_CODE);
        Integer FIELD_DEBIT = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DEBIT);
        Integer FIELD_CREDIT = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_CREDIT);
        Integer FIELD_DESCRIPTION = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DESCRIPTION);
        Integer FIELD_DEPARTMENT = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DEPARTMENT);
        Integer FIELD_NAME = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_NAME);
        Integer FIELD_PROJECT_CODE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_PROJECT_CODE);
        Integer FIELD_EXCHANGE_RATE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_EXCHANGE_RATE);
        Integer FIELD_CURRENCY = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_CURRENCY);

        EdsUser user = userManager.get(importFile.getUserID());

        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);
        dateFormat.setLenient(false);

        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(null);
        Map<String, EdsProject> projectMap = Maps.newHashMap();//projectManager.getProjectAsMapByNumber();
        Map<String, EdsDepartment> departmentMap = Maps.newHashMap();//departmentManager.getDepartmentAsMap();
        Map<String, EdsCurrency> currencyMap = currencyManager.getListAsMap();
        Map<String, EdsCrmAccount> crmAccountMap = Maps.newHashMap();//crmAccountManager.getAllCrmAccountsMap();

        Map<String, NewManualTransaction> map = new HashMap<>();
        LinkedHashMap<String, LinkedList<RejectedImportRecord[]>> itemsMap = new LinkedHashMap<>();

        String transactionKey = null;
//        final String IGNORING_CONTENT_KEY = "IGNORING_CONTENT_KEY";

        StringBuilder notImportedTransactions = new StringBuilder();

        Integer rowIndex = 0;
        for (String[] row : list) {
            RejectedImportRecord[] rejectedRow = new RejectedImportRecord[row.length];

            if (!hasHeader) {
                int columnID = 0;

                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellvalue : row) {

                    if (rowIsEmpty && StringUtils.isNotBlank(cellvalue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }

                String number = null, narration = null, reference = null, accountCode = null, description = null, currency = null;
                BigDecimal debit = BigDecimal.ZERO, credit = BigDecimal.ZERO, exchangeRate = BigDecimal.ONE;
                Date date = null;
                EdsDepartment department = null;
                EdsCrmAccount client = null;
                EdsProject project = null;

                for (String columnValue : row) {
                    rejectedRow[columnID] = new RejectedImportRecord(columnValue);
                    columnValue = columnValue.trim();

                    if (StringUtils.isNotBlank(columnValue)) {
                        columnValue = columnValue.trim();

                        if (FIELD_NUMBER.equals(columnID)) {
                            number = columnValue;
                        } else if (FIELD_DATE.equals(columnID)) {
                            try {
                                date = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                                e.printStackTrace();
                            }
                        } else if (FIELD_NARRATION.equals(columnID)) {
                            narration = StringUtil.cut(columnValue, 254);
                        } else if (FIELD_REFERENCE.equals(columnID)) {
                            reference = StringUtil.cut(columnValue, 254);
                        } else if (FIELD_ACCOUNT_CODE.equals(columnID)) {
                            accountCode = columnValue.replace("-", "");

                            if (accountMap.get(accountCode) == null) {
                                rejectedRow[FIELD_ACCOUNT_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (FIELD_DEBIT.equals(columnID)) {
                            debit = parseBigDecimal(columnValue);

                            if (debit == null) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedRecords.get(0)[columnID].getData()));
                            }
                        } else if (FIELD_CREDIT.equals(columnID)) {
                            credit = parseBigDecimal(columnValue);

                            if (credit == null) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedRecords.get(0)[columnID].getData()));
                            }
                        } else if (FIELD_DESCRIPTION.equals(columnID)) {
                            description = columnValue;
                        } else if (FIELD_NAME.equals(columnID)) {
                            client = crmAccountMap.get(columnValue.toLowerCase());

                            if (client == null) {
                                client = crmAccountManager.getCrmAccountByName(columnValue);

                                if (client == null) {
                                    rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    crmAccountMap.put(columnValue.toLowerCase(), client);
                                }
                            }
                        } else if (FIELD_PROJECT_CODE.equals(columnID)) {
                            project = projectMap.get(columnValue.toLowerCase());

                            if (project == null) {
                                project = projectManager.getProjectByNumber(columnValue);

                                if (project == null) {
                                    rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    projectMap.put(columnValue.toLowerCase(), project);
                                }
                            }
                        } else if (FIELD_DEPARTMENT.equals(columnID)) {
                            department = departmentMap.get(columnValue.toLowerCase());

                            if (department == null) {
                                List<EdsDepartment> dl = departmentManager.getDepartmentByName(columnValue);

                                if (dl.isEmpty()) {
                                    rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    department = (EdsDepartment) dl.get(0);
                                    departmentMap.put(columnValue.toLowerCase(), department);
                                }
                            }
                        } else if (FIELD_EXCHANGE_RATE.equals(columnID)) {
                            exchangeRate = parseBigDecimal(columnValue);

                            if (exchangeRate == null) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedRecords.get(0)[columnID].getData()));
                            }
                        } else if (FIELD_CURRENCY.equals(columnID)) {
                            currency = columnValue.toLowerCase();

                            if (currencyMap.get(currency) == null) {
                                rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        }
                    }

                    columnID++;
                }
                if (StringUtils.isBlank(accountCode)) {
                    rejectedRow[FIELD_ACCOUNT_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[FIELD_ACCOUNT_CODE].getData()));
                }
                if ((debit == null && credit == null) ||
                        (debit == null && credit.equals(BigDecimal.ZERO)) ||
                        (credit == null && debit.equals(BigDecimal.ZERO)) ||
                        (debit.equals(BigDecimal.ZERO) && credit.equals(BigDecimal.ZERO))
                ) {
                    rejectedRow[FIELD_DEBIT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[FIELD_DEBIT].getData()));
                    rejectedRow[FIELD_CREDIT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[FIELD_CREDIT].getData()));
                }

                if (!ServerUtils.isNullOrEmpty(narration) && date != null) {

                    if (StringUtils.isNotBlank(transactionKey) && map.get(transactionKey) != null/* && !IGNORING_CONTENT_KEY.equals(transactionKey)*/) {
                        int errors = 0;

                        if (validateManualTransaction(map.get(transactionKey), itemsMap.get(transactionKey))) {
                            try {
                                manualEntryServiceLocal.saveManualJournal(map.get(transactionKey));
                                importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                                itemsMap.remove(transactionKey);
                            } catch (Exception e) {
                                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                            }
                        } else {
                            importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                        }
                        map.remove(transactionKey);
                    }

                    transactionKey = rowIndex + "_" + narration.toLowerCase() + "_" + dateFormat.format(date);

                    NewManualTransaction manualTransaction = new NewManualTransaction();
                    manualTransaction.setNarration(narration);
                    manualTransaction.setReference(reference);
                    manualTransaction.setDate(new DateNonConvertable(date));
                    manualTransaction.setExchangeRate(exchangeRate);
                    manualTransaction.setCurrency((currency != null && currencyMap.get(currency) != null) ? currencyMap.get(currency).createCurrencyItem() : baseCurrency.createCurrencyItem());
                    manualTransaction.setStatus("POST");
                    BankTransferNumberData numberData = manualEntryServiceLocal.generateManualTransactionMoneyNumber();

                    if (StringUtils.isBlank(number)) {
                        manualTransaction.setTransferNumberData(numberData);
                        StringBuilder sb = new StringBuilder();
                        sb.append(manualTransaction.getTransferNumberData().getPrefix());
                        sb.append(manualTransaction.getTransferNumberData().getFourDigitNumber());

                        if (manualTransaction.getTransferNumberData().isWithDate()) {
                            sb.append("-");
                            sb.append(ServerUtils.getBankTransferDateNumber(new Date()));
                        }
                        manualTransaction.setNumber(sb.toString());
                        manualTransaction.setIntNumber(Integer.valueOf(manualTransaction.getTransferNumberData().getFourDigitNumber()));

                    } else {
                        manualTransaction.setNumber(number);
                        manualTransaction.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
                    }

                    map.put(transactionKey, manualTransaction);
                } else if (!ServerUtils.isNullOrEmpty(narration) && date == null || ServerUtils.isNullOrEmpty(narration) && date != null) {
                    transactionKey = String.valueOf(rowIndex);
                    int errors = 0;
                    if (StringUtils.isBlank(narration)) {
                        rejectedRow[FIELD_NARRATION].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[FIELD_NARRATION].getData()));
                        errors++;
                    } else {
                        transactionKey += "_" + narration.toLowerCase();
                    }
                    if (date == null) {
                        if (StringUtils.isBlank(row[FIELD_DATE])) {
                            rejectedRow[FIELD_DATE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[FIELD_DATE].getData()));
                        } else {
                            rejectedRow[FIELD_DATE].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                        }
                        if (errors == 0) {
                            errors++;
                        }
                    } else {
                        transactionKey += "_" + dateFormat.format(date);
                    }
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + errors);
                }
                itemsMap.computeIfAbsent(transactionKey, k -> new LinkedList<>());
                itemsMap.get(transactionKey).add(rejectedRow);

                NewManualTransactionItem manualTransactionItem = new NewManualTransactionItem();
                manualTransactionItem.setDepartment(department != null ? department.getAsSelectItem() : null);
                manualTransactionItem.setAccountItem(accountCode != null && accountMap.get(accountCode) != null ? new AccountItem(accountMap.get(accountCode), null, null) : null);
                manualTransactionItem.setDebit(debit != null && debit.compareTo(BigDecimal.ZERO) == 0 ? null : debit);
                manualTransactionItem.setCredit(credit != null && credit.compareTo(BigDecimal.ZERO) == 0 ? null : credit);
                manualTransactionItem.setDescription(description);
                manualTransactionItem.setCustomerOrSupplier(client != null ? client.getAsSelectItem() : null);
                manualTransactionItem.setProject(project != null ? project.getAsSelectItem() : null);

                if (map.get(transactionKey) != null) {
                    NewManualTransaction manualTransaction = map.get(transactionKey);
                    if (manualTransaction.getItems() != null && manualTransaction.getItems().length > 0) {
                        List<NewManualTransactionItem> items = new ArrayList<>();
                        items.add(manualTransactionItem);
                        items.addAll(Arrays.asList(manualTransaction.getItems()));
                        manualTransaction.setItems(items.toArray(new NewManualTransactionItem[0]));
                    } else {
                        manualTransaction.setItems(new NewManualTransactionItem[]{manualTransactionItem});
                    }
                }
            } else {
                for (int i = 0; i < row.length; i++) {
                    rejectedRow[i] = new RejectedImportRecord(row[i]);
                }
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
            }

            rowIndex++;
        }

        if (StringUtils.isNotBlank(transactionKey) && map.get(transactionKey) != null /*&& !IGNORING_CONTENT_KEY.equals(transactionKey)*/) {
            StringBuilder errors = new StringBuilder();

            if (validateManualTransaction(map.get(transactionKey), itemsMap.get(transactionKey))) {
                try {
                    manualEntryServiceLocal.saveManualJournal(map.get(transactionKey));
                    importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                    itemsMap.remove(transactionKey);
                } catch (Exception e) {
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                }
            } else {
                importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
            }
            map.remove(transactionKey);
        }

        for (LinkedList<RejectedImportRecord[]> items : itemsMap.values()) {
            rejectedRecords.addAll(items);
        }

        return rejectedRecords;
    }

    @Override
    @Transactional
    public LinkedHashMap<NewManualTransaction, NewManualTransaction> importManualTransactionTally(ImportFile importFile, List<String[]> list) {
        boolean hasHeader = importFile.isHasHeader();

        Integer FIELD_DATE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DATE);
        Integer FIELD_PARTICULARS = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_PARTICULARS);
        Integer FIELD_EXCHANGE_RATE = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_EXCHANGE_RATE);
        Integer FIELD_VOUCHER_NUMBER = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_VOUCHER_NUMBER);
        Integer FIELD_DEBIT = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_DEBIT);
        Integer FIELD_CREDIT = importFile.getColumnID(ImportField.ManualTransactionImportFields.FIELD_CREDIT);

        //EdsUser user = userManager.get(importFile.getUserID());

        String strDateFormat = "dd/MM/yyyy";
        EdsFinancialSettings edsFinancialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency baseCurrency = edsFinancialSettings.getCurrency();
        Integer calculationScale = ServerUtils.getSystemCalculationScale();
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        HashMap<String, EdsAccount> accountMap = accountingManager.getAccountAsMap(new ListingFilterParameter());
        EdsAccount accountReceivable = accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_RECEIVABLE);
        EdsAccount accountPayable = accountingManager.getAccountByKey(EdsAccount.ACCOUNTS_PAYABLE);
        Map<String, EdsCrmAccount> crmAccountMap = crmAccountManager.getAllCrmAccountsMap();
        Map<String, EdsCurrency> currencyMap = currencyManager.getListAsMap();

        LinkedHashMap<NewManualTransaction, ArrayList<NewManualTransactionItem>> manualTransactionMap = new LinkedHashMap<>();
        LinkedHashMap<NewManualTransaction, NewManualTransaction> errorManualTransactionMap = new LinkedHashMap<>();
        NewManualTransaction manualTransaction = null;
        for (String[] row : list) {
            if (!hasHeader) {
                int columnID = 0;
                SelectItem accountItem = null;
                NewManualTransactionItem manualTransactionItem = new NewManualTransactionItem();
                for (String columnValue : row) {
                    if (columnValue != null && !columnValue.isEmpty()) {
                        columnValue = columnValue.trim();
                        if (FIELD_DATE.equals(columnID)) {
                            try {
                                if (!ServerUtils.isNullOrEmpty(columnValue)) {
                                    manualTransaction = new NewManualTransaction();
                                    manualTransaction.setDate(new DateNonConvertable(dateFormat.parse(columnValue)));
                                    manualTransactionMap.put(manualTransaction, new ArrayList<>());
                                } else {
                                    errorManualTransactionMap.put(manualTransaction, manualTransaction);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                errorManualTransactionMap.put(manualTransaction, manualTransaction);
                            }
                        } else if (FIELD_PARTICULARS.equals(columnID)) {
                            EdsCrmAccount edsCrmAccount = crmAccountMap.get(columnValue.toLowerCase());
                            if (edsCrmAccount != null) {
                                if (edsCrmAccount.isClient() && edsCrmAccount.isSupplier()) {
                                    errorManualTransactionMap.put(manualTransaction, manualTransaction);
                                } else {
                                    accountItem = edsCrmAccount.isClient() ? accountReceivable.getAsSelectItem() : accountPayable.getAsSelectItem();
                                    manualTransactionItem.setCustomerOrSupplier(edsCrmAccount.getAsSelectItem());
                                    manualTransactionItem.setAccountItem(new AccountItem(accountItem.getId(), accountItem.getNumber(), accountItem.getName()));
                                    manualTransactionMap.get(manualTransaction).add(manualTransactionItem);
                                }
                            } else {
                                EdsAccount edsAccount = accountMap.get(columnValue);
                                if (edsAccount == null) {
                                    errorManualTransactionMap.put(manualTransaction, manualTransaction);
                                } else {
                                    accountItem = edsAccount.getAsSelectItem();
                                    manualTransactionItem.setAccountItem(new AccountItem(accountItem.getId(), accountItem.getNumber(), accountItem.getName()));
                                    manualTransactionMap.get(manualTransaction).add(manualTransactionItem);
                                }
                            }
                        } else if (FIELD_EXCHANGE_RATE.equals(columnID)) {
                            String[] exchangeRateParts = columnValue.split("/");//4.10 AED/ E
                            String currency = null;
                            String baseCurrencyName = null;
                            String exchangeRateStr = "1.0";
                            if (exchangeRateParts.length > 0) {
                                exchangeRateStr = exchangeRateParts[0].split(" ")[0];
                                baseCurrencyName = exchangeRateParts[0].split(" ")[1];
                                currency = exchangeRateParts[1].trim();
                            }
                            if ("E".equals(currency)) {
                                currency = "eur";
                            } else if ("$".equals(currency)) {
                                currency = "usd";
                            }
                            BigDecimal exchangeRate = new BigDecimal(1).divide(parseBigDecimal(exchangeRateStr), calculationScale, RoundingMode.HALF_UP);
                            manualTransaction.setExchangeRate(exchangeRate);
                            manualTransaction.setCurrency((currency != null && currencyMap.get(currency.toLowerCase()) != null) ? currencyMap.get(currency.toLowerCase()).createCurrencyItem() : currencyMap.get(baseCurrencyName) != null ? currencyMap.get(baseCurrencyName).createCurrencyItem() : baseCurrency.createCurrencyItem());
                        } else if (FIELD_VOUCHER_NUMBER.equals(columnID)) {
                            manualTransaction.setNarration(columnValue);
                        } else if (FIELD_DEBIT.equals(columnID)) {
                            manualTransactionItem.setDebit(parseBigDecimal(columnValue).multiply((manualTransaction.getExchangeRate() != null ? manualTransaction.getExchangeRate() : BigDecimal.ONE)));
                        } else if (FIELD_CREDIT.equals(columnID)) {
                            manualTransactionItem.setCredit(parseBigDecimal(columnValue).multiply((manualTransaction.getExchangeRate() != null ? manualTransaction.getExchangeRate() : BigDecimal.ONE)));
                        }
                    }
                    columnID++;
                }
            } else {
                hasHeader = false;
            }
        }

        if (manualTransactionMap.size() > 0) {
            for (NewManualTransaction newManualTransaction : manualTransactionMap.keySet()) {
                if (errorManualTransactionMap.get(newManualTransaction) == null) {
                    try {
                        newManualTransaction.setStatus("POST");
                        BankTransferNumberData numberData = manualEntryServiceLocal.generateManualTransactionMoneyNumber();
                        newManualTransaction.setTransferNumberData(numberData);
                        StringBuilder sb = new StringBuilder();
                        sb.append(newManualTransaction.getTransferNumberData().getPrefix());
                        sb.append(newManualTransaction.getTransferNumberData().getFourDigitNumber());
                        if (newManualTransaction.getTransferNumberData().isWithDate()) {
                            sb.append("-");
                            sb.append(ServerUtils.getBankTransferDateNumber(new Date()));
                        }
                        newManualTransaction.setNumber(sb.toString());
                        newManualTransaction.setIntNumber(Integer.valueOf(newManualTransaction.getTransferNumberData().getFourDigitNumber()));

                        newManualTransaction.setItems(manualTransactionMap.get(newManualTransaction).toArray(new NewManualTransactionItem[]{}));

                        manualEntryServiceLocal.saveManualJournal(newManualTransaction);
                    } catch (Exception e) {
                        errorManualTransactionMap.put(newManualTransaction, newManualTransaction);
                    }
                }
            }
        }

        return errorManualTransactionMap;
    }

    private boolean validateManualTransaction(NewManualTransaction manualTransaction, LinkedList<RejectedImportRecord[]> rejectionItems) {
        boolean hasErrors = false;
        BigDecimal totalDebit = BigDecimal.ZERO, totalCredit = BigDecimal.ZERO;

        for (NewManualTransactionItem transactionItem : manualTransaction.getItems()) {

            if (transactionItem.getDebit() == null && transactionItem.getCredit() == null) {
                hasErrors = true;
                continue;
            }
            totalDebit = totalDebit.add(transactionItem.getDebit() != null ? transactionItem.getDebit() : BigDecimal.ZERO);
            totalCredit = totalCredit.add(transactionItem.getCredit() != null ? transactionItem.getCredit() : BigDecimal.ZERO);
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            hasErrors = true;
            rejectionItems.get(0)[0].setErrorComment("Total debits must equal total credits.");
        }
        if (manualJournalManager.isDuplicateMTNumber(manualTransaction.getNumber(), null, manualTransaction.getDate() != null ? manualTransaction.getDate().getNonConvertedDate() : null)) {
            hasErrors = true;
            rejectionItems.get(0)[0].setErrorComment("Manual transaction number " + manualTransaction.getNumber() + " is already exist");
        }

        for (RejectedImportRecord[] row : rejectionItems) {
            for (RejectedImportRecord cell : row) {
                if (StringUtils.isNotBlank(cell.getComment())) {
                    hasErrors = true;
                    break;
                }
            }

            if (hasErrors) {
                break;
            }
        }

        return !hasErrors;
    }

    public ArrayList<RejectedImportRecord[]> importAdditionalPayment(ImportFile importFile, List<String[]> list) {
        boolean hasHeader = importFile.isHasHeader();
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(importFile.getPaymentID());
        List<EdsPaymentDeduction> paymentDeductions = new ArrayList<>();

        Integer FIELD_EMPLOYEE_CODE = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_EMPLOYEE_CODE);
//        Integer FIELD_EMPLOYEE_NAME = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_EMPLOYEE_NAME);
        Integer FIELD_AMOUNT = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_AMOUNT);
        Integer FIELD_CATEGORY = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_CATEGORY);
        Integer FIELD_ADDITIONAL_PAYMENT_DATE = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_ADDITIONAL_PAYMENT_DATE);

        //This Category will be applied in case if category is empty
        Integer systemCategoryId = importFile.getColumnID(ImportField.AdditionalPaymentImportFields.FIELD_SYSTEM_CATEGORY);

        boolean byCommission = BY_COMMISION_TYPE.equals(additionalPayment.getType());

        EdsUser user = userManager.get(importFile.getUserID());
        BigDecimal total = BigDecimal.ZERO;

//        StringBuilder notImportedPayments = new StringBuilder();

//        String shortDateFormatStr = Constants.SHORT_DATE_FORMAT_10;// e.g. 31.01.2018;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<RejectedImportRecord[]>();

        //Integer rowIndex = 0;
        nextRow:
        for (String[] row : list) {

            if (!hasHeader) {

                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellvalue : row) {
                    if (rowIsEmpty && StringUtils.isNotBlank(cellvalue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue nextRow;
                }

                int columnID = 0;

                String employeeCode = null, /*employeeName = null,*/ category = null, additionalPaymentDate = null;
                BigDecimal amount = BigDecimal.ZERO;

                int errors = 0;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                EdsPaymentDeduction paymentDeduction = new EdsPaymentDeduction();

                for (String cellValue : row) {
                    //Create Row for Reject, we will add it to list if there will be an error
                    rejectedCells[columnID] = new RejectedImportRecord(cellValue);

                    if (StringUtils.isNotBlank(cellValue)) {
                        //Trim value
                        cellValue = cellValue.trim();

                        if (cellValue != null && cellValue.length() > 250) {
                            System.out.println("!!!!!!!!!!!!!" + columnID + ":::" + cellValue + ":::" + cellValue.length());
                            cellValue = cellValue.substring(0, 250).trim();
                        }

                        if (FIELD_EMPLOYEE_CODE.equals(columnID)) {
                            employeeCode = cellValue.trim();
                        } /*else if (FIELD_EMPLOYEE_NAME.equals(columnID)) {
                            employeeName = cellValue;
                        }*/ else if (FIELD_AMOUNT.equals(columnID)) {
                            amount = parseBigDecimal(cellValue);
                        } else if (FIELD_CATEGORY.equals(columnID)) {
                            category = cellValue;
                        } else if (FIELD_ADDITIONAL_PAYMENT_DATE.equals(columnID)) {
                            additionalPaymentDate = cellValue;
                        }
                    }

                    columnID++;
                }

                EdsEmployee employee = null;
                EdsPayrollCategory payrollCategory = null;
                Date additionalPaymentDateVal = null;
                //Validate values
                if (StringUtils.isBlank(employeeCode)) {
                    errors++;
                    rejectedCells[FIELD_EMPLOYEE_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, employeeCode));
                } else {
                    employee = employeeManager.getEmployeeByNumber(employeeCode);
                    if (employee == null) {
                        errors++;
                        rejectedCells[FIELD_EMPLOYEE_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, employeeCode));
                    }
                }

                //Try to get category by cellValue
                if (StringUtils.isNotBlank(category)) {
                    payrollCategory = payrollCategoryManager.getCategoryByCode(category, additionalPayment.getCategoryType());
                }
                //if category was not found then use choosen default category
                if (payrollCategory == null) {
                    payrollCategory = payrollCategoryManager.get(systemCategoryId);
                }
                //if category still null then reject row
                if (payrollCategory == null) {
                    errors++;
                    rejectedCells[FIELD_CATEGORY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, category));
                }
                //amount must be greater than 0
                if (amount == null || !(amount.compareTo(BigDecimal.ZERO) > 0)) {
                    errors++;
                    rejectedCells[FIELD_AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[FIELD_AMOUNT].getData()));
                }

                try {
                    additionalPaymentDateVal = dateFormat.parse(additionalPaymentDate);
                } catch (ParseException e) {
                    errors++;
                    rejectedCells[FIELD_ADDITIONAL_PAYMENT_DATE].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                }
                //End validate values

                if (errors == 0) {

                    paymentDeduction.setEmployeeId(employee != null ? employee.getObjectID() : null);
                    paymentDeduction.setCategoryId(payrollCategory != null ? payrollCategory.getObjectID() : null);
                    paymentDeduction.setAdditionalPaymentDate(additionalPaymentDateVal);
                    if (byCommission) {
                        EdsEmployeePayrollSettings employeePayrollSettings = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), EMPLOYEE_COMMISSION);

                        BigDecimal commission = BigDecimal.ZERO, paymentAmount = BigDecimal.ZERO;
                        if (employeePayrollSettings != null) {
                            commission = new BigDecimal(employeePayrollSettings.getValue());
                        }
                        paymentAmount = amount.multiply(commission).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        paymentDeduction.setTotalAmount(amount);
                        paymentDeduction.setCommission(commission);
                        paymentDeduction.setPaymentAmount(paymentAmount);
                        total = total.add(paymentAmount);
                    } else {
                        paymentDeduction.setPaymentAmount(amount);
                        total = total.add(amount);
                    }

                    paymentDeductions.add(paymentDeduction);
                    paymentDeductionManager.createOrUpdate(paymentDeduction);
                    //Increment number of imported ROWs
                    importFile.setImportedColumns(importFile.getImportedColumns() + 1);

                } else {
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                }

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                //Add Header row into final result
                rejectedRows.add(rejectedCells);
            }
        }
        additionalPayment.setDeleted(false);
        additionalPayment.setTotal(total);
        additionalPayment.setItems(paymentDeductions);
        additionalPaymentManager.createOrUpdate(additionalPayment);
        payrollService.addAdditionalPaymentToSolr(additionalPayment.getObjectID());

        return rejectedRows;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public StringBuilder batchImportInvoices(SalesInvoiceAddTO[] items, EdsImportFile importFile) {
        StringBuilder sql = new StringBuilder();
        if (items == null || items.length == 0) {
            sql.append("No items found.");
            return sql;
        }
        int i = 0;
        int ignored = 0;
        EdsCompany company = invoicingSettingsManager.getUser().getCompany();
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        StringBuilder ignoredSql = new StringBuilder();
        for (SalesInvoiceAddTO salesInvoice : items) {
            boolean hasError = false;
            i++;
            BigDecimal invoiceTotal = BigDecimal.ZERO;
            StringBuilder ignoredItem = new StringBuilder("#").append(i).append(" Line ignored due to: \n");
            if (salesInvoice == null) {
                ignored++;
                ignoredItem.append("Invoice is empty \n");
                ignoredSql.append(ignoredItem);
                continue;
            } else {
                SelectItem account = null;
                if (salesInvoice.getInvoice_status() == null || StringUtils.isBlank(salesInvoice.getInvoice_status().getStatus_code())) {
                    ignoredItem.append("Invoice status is empty \n");
                    hasError = true;
                } else if (InvoiceStatusEnum.getStatus(salesInvoice.getInvoice_status().getStatus_code()) == null) {
                    hasError = true;
                    ignoredItem.append("Invoice status note compatible with the system one \n");
                }
                if (StringUtils.isBlank(salesInvoice.getInvoice_date())) {
                    hasError = true;
                    ignoredItem.append("Invoice date is empty \n");
                } else if (ServerUtils.parseDate(salesInvoice.getInvoice_date(), "dd-MM-yyyy'T'hh:mm:ssZ") == null) {
                    hasError = true;
                    ignoredItem.append("Invoice date is not compatible with pattern (dd-MM-yyyy'T'hh:mm:ssZ) \n");
                }
                if (StringUtils.isBlank(salesInvoice.getInvoice_due_date())) {
                    hasError = true;
                    ignoredItem.append("Invoice due date is empty \n");
                } else if (ServerUtils.parseDate(salesInvoice.getInvoice_due_date(), "dd-MM-yyyy'T'hh:mm:ssZ") == null) {
                    hasError = true;
                    ignoredItem.append("Invoice due date is not compatible with pattern (dd-MM-yyyy'T'hh:mm:ssZ) \n");
                }
                if (salesInvoice.getCustomer() == null || salesInvoice.getCustomer().getCustomer_id() == null || salesInvoice.getCustomer().getCustomer_id() <= 0) {
                    hasError = true;
                    ignoredItem.append("Invoice client is empty \n");
                }
                if (salesInvoice.getInvoice_status() != null && PAID.equals(salesInvoice.getInvoice_status().getStatus_code())) {
                    if (salesInvoice.getPayments() != null && salesInvoice.getPayments().size() > 0) {
                        int it = 0;
                        boolean hasItemError = false;
                        StringBuilder ignoredItemsSql = new StringBuilder();
                        for (PaymentTO paymentItem : salesInvoice.getPayments()) {
                            it++;
                            if (paymentItem.getPaid_amount() == null) {
                                hasItemError = true;
                                ignoredItemsSql.append("###").append(it).append(" payment item paid amount is empty \n");
                            }
                            if (paymentItem.getPayment_account_id() == null) {
                                hasItemError = true;
                                ignoredItemsSql.append("###").append(it).append(" payment item payment account is empty \n");
                            }
                            if (StringUtils.isBlank(paymentItem.getPaid_date())) {
                                hasItemError = true;
                                ignoredItemsSql.append("###").append(it).append(" payment item paid date is empty \n");
                            } else if (ServerUtils.parseDate(paymentItem.getPaid_date(), "dd-MM-yyyy'T'hh:mm:ssZ") == null) {
                                hasItemError = true;
                                ignoredItemsSql.append("###").append(it).append(" payment item paid date is not compatible with pattern (dd-MM-yyyy'T'hh:mm:ssZ) \n");
                            }
                        }
                        if (hasItemError) {
                            hasError = true;
                            ignoredItem.append("{ \n");
                            ignoredItem.append(ignoredItemsSql);
                            ignoredItem.append("} \n ");
                        }
                    } else {
                        EdsAccount edsAccount = accountingManager.get(salesInvoice.getPayment_account_id());
                        if (edsAccount == null) {
                            EdsAccount defaultAccount = accountingManager.get(invSettings.getDefaultPaymentAccountId());
                            if (defaultAccount == null) {
                                hasError = true;
                                ignoredItem.append("Payment account not found \n");
                            } else {
                                account = defaultAccount.getAsSelectItem();
                            }
                        } else {
                            account = edsAccount.getAsSelectItem();
                        }
                    }
                }
                if (salesInvoice.getInvoice_items() == null || salesInvoice.getInvoice_items().isEmpty()) {
                    hasError = true;
                    ignoredItem.append("Invoice items is empty \n");
                } else {
                    int it = 0;
                    boolean hasItemError = false;
                    StringBuilder ignoredItemsSql = new StringBuilder();
                    for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                        it++;
                        if (invoiceItem.getItem() == null || invoiceItem.getItem().getItem_id() == null || invoiceItem.getItem().getItem_id() <= 0) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) is empty \n");
                        }
                        if (invoiceItem.getItem_sales_account() == null || invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() <= 0) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) sales account is empty \n");
                        }
                        if (invoiceItem.getItem_quantity() == null) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) quantity is empty \n");
                        } else if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_quantity()) == 0) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) quantity is equal to 0 \n");
                        }
                        /*if (invoiceItem.getItem_price() == null) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) price is empty \n");
                        }
                        if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_price()) == 0) {
                            hasItemError = true;
                            ignoredItemsSql.append("###").append(it).append(" invoice item(product) price is equal to 0 \n");
                        }*/
                    }
                    if (hasItemError) {
                        hasError = true;
                        ignoredItem.append("{ \n");
                        ignoredItem.append(ignoredItemsSql);
                        ignoredItem.append("} \n ");
                    }
                }
                if (hasError) {
                    ignored++;
                    ignoredSql.append(ignoredItem);
                    continue;
                }

                NewInvoice invoice = new NewInvoice();
                invoice.setClientID(salesInvoice.getCustomer().getCustomer_id());
                if (salesInvoice.getCustomer().getBill_to_address() != null && salesInvoice.getCustomer().getBill_to_address().getAddress_id() != null) {
                    invoice.setBillAddressID(salesInvoice.getCustomer().getBill_to_address().getAddress_id());
                } else {
                    List<EdsAddress> billAddrList = addressManager.getAddressesByEntityIdAndType(salesInvoice.getCustomer().getCustomer_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
                    if (billAddrList == null || billAddrList.size() == 0) {
                        ignored++;
                        ignoredItem.append("Bill Address for this client not found \n\n");
                        ignoredSql.append(ignoredItem);
                        continue;
                    } else {
                        invoice.setBillAddressID(billAddrList.get(0).getObjectID());
                    }
                }
                if (salesInvoice.getCustomer().getShip_to_address() != null) {
                    invoice.setMailAddressID(salesInvoice.getCustomer().getShip_to_address().getAddress_id());
                }
                if (salesInvoice.getInvoice_currency() != null) {
                    invoice.setCurrencyID(salesInvoice.getInvoice_currency().getCurrency_id());
                } else {
                    CurrencyItem currencyItem = currencyServiceLocal.getCompanyBaseCurrency();
                    if (currencyItem != null) {
                        invoice.setCurrencyID(currencyItem.getId());
                    }
                }
                if (salesInvoice.getExchange_rate() != null && salesInvoice.getExchange_rate().compareTo(BigDecimal.ZERO) != 0) {
                    invoice.setExchageRate(salesInvoice.getExchange_rate());
                } else {
                    invoice.setExchageRate(BigDecimal.ONE);
                }
                invoice.setInvoiceNumber(salesInvoice.getInvoice_number());
                invoice.setReference(salesInvoice.getReference());
                try {
                    invoice.setInvoiceDate(new DateNonConvertable(ServerUtils.parseDate(salesInvoice.getInvoice_date(), "dd-MM-yyyy'T'hh:mm:ssZ")));
                    invoice.setDueDate(new DateNonConvertable(ServerUtils.parseDate(salesInvoice.getInvoice_due_date(), "dd-MM-yyyy'T'hh:mm:ssZ")));
                } catch (Exception e) {
                    continue;
                }
                invoice.setIntroduction(salesInvoice.getIntroduction());
                invoice.setType(Constants.RECEIVABLE);
                invoice.setStatusCode(InvoiceStatusEnum.getStatus(salesInvoice.getInvoice_status().getStatus_code()));
                if (InvoiceStatusEnum.PAID.getStatus().equals(invoice.getStatusCode())) {
                    invoice.setStatusCode(InvoiceStatusEnum.APPROVE.getStatus());
                }
                invoice.setForceSave(true);

                ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();
                ArrayList<TotalTaxItem> taxItems = new ArrayList<>();
                BigDecimal totalTaxes = BigDecimal.ZERO;

                for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                    NewInvoiceItem item = new NewInvoiceItem();
                    item.setItemID(invoiceItem.getItem().getItem_id());
                    item.setItemName(invoiceItem.getItem().getItem_name());
                    item.setDescription(invoiceItem.getItem_description());
                    if (invoiceItem.getDepartment() != null) {
                        item.setDepartmentItem(new SelectItem(invoiceItem.getDepartment()));
                    }
                    item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                    item.setQuantity(invoiceItem.getItem_quantity());
                    item.setUnitPrice(invoiceItem.getItem_price() == null || invoiceItem.getItem_price().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : invoiceItem.getItem_price());
                    item.setNet(invoiceItem.getItem_net_amount());
                    if (invoiceItem.getItem_warehouse() != null) {
                        item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id(), invoiceItem.getItem_warehouse().getWarehouse_name()));
                    }
                    invoiceItems.add(item);
                    invoiceTotal = invoiceTotal.add(item.getQuantity().multiply(item.getUnitPrice()));
                }
                invoice.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));
                invoice.setTotal(salesInvoice.getInvoice_total() != null ? salesInvoice.getInvoice_total() : invoiceTotal);
                invoice.setBookkeep(true);
                try {
                    SaveResult saveResult = invoiceServiceLocal.saveSaleInvoice(invoice);
                    if (saveResult.isInvoiceExist()) {
                        ignored++;
                        ignoredSql.append("Invoice is exists in system \n");
                    } else {
                        //Generate invoice payment
                        if (salesInvoice.getInvoice_status() != null && PAID.equals(salesInvoice.getInvoice_status().getStatus_code())) {
                            ReceivePaymentData receivePaymentData = new ReceivePaymentData();
                            receivePaymentData.setAccount(account);
                            SelectItem crmAccount = null;
                            if (salesInvoice.getCustomer() != null && salesInvoice.getCustomer().getCustomer_id() != null) {
                                EdsCrmAccount edsCrmAccount = crmAccountManager.get(salesInvoice.getCustomer().getCustomer_id());
                                if (edsCrmAccount != null) {
                                    crmAccount = edsCrmAccount.getAsSelectItem();
                                }
                            }
                            receivePaymentData.setCrmAccount(crmAccount);
                            receivePaymentData.setCurrency(invoice.getCurrencyID() != null ? currencyManager.get(invoice.getCurrencyID()).createCurrencyItem() : null);
                            receivePaymentData.setExRate(invoice.getExchageRate());
                            receivePaymentData.setReference(invoice.getInvoiceNumber());
                            receivePaymentData.setDate(invoice.getInvoiceDate());
                            receivePaymentData.setTotalAmount(invoice.getTotal());
                            receivePaymentData.setProject(invoice.getRelatedProject());
                            receivePaymentData.setPdfTemplateID(invoice.getPdfTemplateID());
                            receivePaymentData.setPaymentTarget("INVOICE");
                            receivePaymentData.setType(RECEIVABLE);

                            ArrayList<PaymentData> payments = new ArrayList<>();
                            if (salesInvoice.getPayments() != null && salesInvoice.getPayments().size() > 0) {
                                EdsAccount defaultAccount = accountingManager.get(invSettings.getDefaultPaymentAccountId());
                                final SelectItem defaultAcc = defaultAccount != null ? defaultAccount.getAsSelectItem() : null;
                                Map<Integer, SelectItem> accountsMap = new HashMap<>();
                                salesInvoice.getPayments().forEach(p -> {
                                    PaymentData paymentData = new PaymentData();
                                    if (accountsMap.containsKey(p.getPayment_account_id())) {
                                        paymentData.setPaymentAccount(accountsMap.get(p.getPayment_account_id()));
                                    } else {
                                        EdsAccount paymentAccount = accountingManager.get(p.getPayment_account_id());
                                        if (paymentAccount != null) {
                                            paymentData.setPaymentAccount(paymentAccount.getAsSelectItem());
                                            accountsMap.put(p.getPayment_account_id(), paymentAccount.getAsSelectItem());
                                        } else {
                                            paymentData.setPaymentAccount(defaultAcc);
                                        }
                                    }
                                    paymentData.setReferenceNumber(p.getReference());
                                    paymentData.setInvoiceID(saveResult.getId());
                                    paymentData.setPaymentAmount(p.getPaid_amount());
                                    paymentData.setExchangeRate(invoice.getExchageRate());
                                    paymentData.setOpeningBalance(false);
                                    paymentData.setManualJournal(false);
                                    paymentData.setDate(new DateNonConvertable(ServerUtils.parseDate(p.getPaid_date(), "dd-MM-yyyy'T'hh:mm:ssZ")));
                                    paymentData.setPaymentTypeID(p.getPayment_method_id());
                                    payments.add(paymentData);
                                });
                            } else {
                                PaymentData paymentData = new PaymentData();
                                paymentData.setReferenceNumber(invoice.getInvoiceNumber());
                                paymentData.setInvoiceID(saveResult.getId());
                                paymentData.setPaymentAmount(invoiceTotal);
                                paymentData.setExchangeRate(invoice.getExchageRate());
                                paymentData.setOpeningBalance(false);
                                paymentData.setManualJournal(false);
                                paymentData.setDate(invoice.getInvoiceDate());
                                paymentData.setPaymentAccount(account);

                                payments.add(paymentData);
                            }
                            receivePaymentData.setPayments(payments.toArray(new PaymentData[]{}));

                            invoiceServiceLocal.saveReceivePaymentData(receivePaymentData, true);
                        }
                    }
                } catch (Exception e) {
                    ignored++;
                    ignoredSql.append("Exception occured while saving invoice: \n");
                    ignoredSql.append(e);
                    log.error("", e);
                }
            }
        }
        importFile.setCsvColumns(items.length);
        importFile.setImportedColumns(items.length - ignored);
        importFile.setIgnoredColumns(ignored);
        sql.append("Items Received : ").append(items.length).append(" \n");
        sql.append("Items Imported : ").append(items.length - ignored).append(" \n");
        if (ignored > 0) {
            sql.append("Items Rejected : ").append(ignored).append(". See details: \n");
            sql.append(ignoredSql);
        }
        sql.append("Thanks for using our system!");
        return sql;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importBatchInvoiceWithoutPayment(ImportFile importFile, List<String[]> list) {
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Map<String, EdsCurrency> currencyMap = currencyManager.getListAsMap();
        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(null);
        Map<String, EdsCrmAccount> crmAccountMap = new HashMap<>();
        Map<String, EdsItem> productMap = new HashMap<>();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
        List<TaxItem> taxItemList = Arrays.asList(accountingServiceLocal.getCompanyTaxesWithFilter(new ListingFilterParameter()));

        HashSet<String> ignoreSet = new HashSet<>();
        LinkedHashMap<String, NewInvoice> invoiceMap = new LinkedHashMap<>();
        Map<String, String> invoiceNumberMap = new LinkedHashMap<>();


        Integer REFERENCE = 0;
        Integer INVOICE_DATE = 1;
        Integer CUSTOMER_NAME = 2;
        Integer AMOUNT = 3;
        Integer CURRENCY = 4;
        Integer INTRODUCTION = 5;
        Integer PRODUCT_NUMBER = 6;
        Integer QUANTITY = 7;
        Integer PRICE = 8;
        Integer ACCOUNT_CODE = 9;
        Integer TAX_TYPE_COLUMN = 10;
        Integer TAX_COLUMN = 11;
        Integer INVOICE_DUE_DATE = 12;
        Integer INVOICE_NUMBER = 13;

        for (String[] row : list) {
            if (!hasHeader) {
                int columnID = 0;
                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellValue : row) {

                    if (rowIsEmpty && StringUtils.isNotBlank(cellValue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }

                String reference = null, accountCode = null, currency = null, introduction = null, invoiceNumber = null, taxType;
                Date invoiceDate = null, invoiceDueDate = null;
                BigDecimal amount = null, price = null, quantity = null;
                Integer taxCalculationType = 0;
                TaxItem taxItem = null;
                EdsCrmAccount client = null;
                EdsItem product = null;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];

                boolean hasError = false;
                for (String columnValue : row) {

                    rejectedCells[columnID] = new RejectedImportRecord(columnValue);

                    columnValue = columnValue.trim();
                    if (StringUtils.isNotBlank(columnValue)) {
                        if (REFERENCE.equals(columnID)) {
                            reference = columnValue;
                        } else if (INVOICE_DATE.equals(columnID)) {
                            try {
                                invoiceDate = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing invoice date : " + columnValue + " Invoice date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (CUSTOMER_NAME.equals(columnID)) {
                            client = crmAccountMap.get(columnValue.toLowerCase());

                            if (client == null) {
                                client = crmAccountManager.getCrmAccountByName(columnValue);

                                if (client == null) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    crmAccountMap.put(columnValue.toLowerCase(), client);
                                }
                            }
                        } else if (AMOUNT.equals(columnID)) {
                            amount = parseBigDecimal(columnValue);
                            if (amount == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[AMOUNT].getData()));
                            }
                        } else if (CURRENCY.equals(columnID)) {
                            currency = columnValue.toLowerCase();

                            if (currencyMap.get(currency) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (INTRODUCTION.equals(columnID)) {
                            introduction = columnValue;
                        } else if (PRODUCT_NUMBER.equals(columnID)) {
                            product = productMap.get(columnValue.toLowerCase());

                            if (product == null) {
                                product = itemManager.getItemByNumber(columnValue);

                                if (product == null) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    productMap.put(columnValue.toLowerCase(), product);
                                }
                            }
                        } else if (QUANTITY.equals(columnID)) {
                            quantity = parseBigDecimal(columnValue);
                            if (quantity == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[QUANTITY].getData()));
                            }
                        } else if (PRICE.equals(columnID)) {
                            price = parseBigDecimal(columnValue);
                            if (price == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[PRICE].getData()));
                            }
                        } else if (ACCOUNT_CODE.equals(columnID)) {
                            accountCode = columnValue.replace("-", "");

                            if (accountMap.get(accountCode) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (TAX_TYPE_COLUMN.equals(columnID)) {
                            taxType = columnValue;
                            if (StringUtils.isBlank(taxType)) {
                                taxCalculationType = 0;
                            } else {
                                taxCalculationType = "TAX INCLUSIVE".equalsIgnoreCase(taxType.toUpperCase(Locale.ROOT)) ? 1 : 2;
                            }
                        } else if (TAX_COLUMN.equals(columnID)) {
                            String finalColumnValue = columnValue;
                            taxItem = taxItemList.stream().filter(item -> item.getName().toLowerCase().contains(finalColumnValue.toLowerCase())).findFirst().orElse(null);
                            if (taxItem == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (INVOICE_DUE_DATE.equals(columnID)) {
                            try {
                                invoiceDueDate = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing invoice due date : " + columnValue + " Invoice Due date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (INVOICE_NUMBER.equals(columnID)) {
                            invoiceNumber = columnValue;
                        }
                    }
                    columnID++;
                }

                //Validate required fields
                if (reference == null) {
                    rejectedCells[REFERENCE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Reference"));
                    hasError = true;
                }
                if (invoiceDate == null) {
                    rejectedCells[INVOICE_DATE].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Invoice Date. Date format should be " + DATE_PATTERN));
                    hasError = true;
                }
                if (client == null) {
                    rejectedCells[CUSTOMER_NAME].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Customer Name"));
                    hasError = true;
                }
                if (amount == null) {
                    rejectedCells[AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Amount"));
                    hasError = true;
                }
                if (currency == null) {
                    rejectedCells[CURRENCY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Currency Code"));
                    hasError = true;
                }
                if (product == null) {
                    rejectedCells[PRODUCT_NUMBER].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Product Number"));
                    hasError = true;
                }
                if (quantity == null) {
                    rejectedCells[QUANTITY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Quantity"));
                    hasError = true;
                }
                if (price == null) {
                    rejectedCells[PRICE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Price"));
                    hasError = true;
                }
                if (accountCode == null) {
                    rejectedCells[ACCOUNT_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Account Code"));
                    hasError = true;
                }
                if (invoiceDueDate == null) {
                    rejectedCells[INVOICE_DUE_DATE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Invoice Due Date"));
                    hasError = true;
                }
                if (invoiceNumber == null) {
                    rejectedCells[INVOICE_NUMBER].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Invoice Number"));
                    hasError = true;
                }

                if (hasError) {
                    ignoreSet.add(reference);
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                } else {
                    if (!ignoreSet.contains(reference)) {
                        NewInvoiceItem invoiceItem = new NewInvoiceItem();
                        invoiceItem.setItemID(product.getObjectID());
                        invoiceItem.setItemName(product.getName());
                        invoiceItem.setDescription(product.getDescription());
                        invoiceItem.setAccountID(accountMap.get(accountCode));
                        invoiceItem.setQuantity(quantity);
                        invoiceItem.setUnitPrice(price);
                        invoiceItem.setNet(quantity.multiply(price));
                        invoiceItem.setWarehouse(defaultWarehouse.getAsSelectItem());
                        invoiceItem.setTaxItem(taxItem);

                        NewInvoice invoice;
                        if (invoiceMap.get(reference) == null) {
                            invoice = new NewInvoice();
                            invoice.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(invoiceDate)));
                            invoice.setDueDate(invoiceDueDate != null ? new DateNonConvertable(ServerUtils.getEndDate(invoiceDueDate)) : new DateNonConvertable(ServerUtils.getEndDate(invoiceDate)));
                            invoice.setClientID(client.getObjectID());
                            invoice.setReference(reference);
                            invoice.setCurrencyID(currencyMap.get(currency.toLowerCase()).getObjectID());
                            invoice.setForceValidNumberGenerate(true);
                            invoice.setIntroduction(introduction);
                            invoice.setType(Constants.RECEIVABLE);
                            invoice.setStatusCode(Constants.APPROVE);
                            invoice.setForceSave(true);
                            invoice.setTotal(amount);
                            invoice.setSubtotal(amount);
                            invoice.setExchageRate(BigDecimal.ONE);
                            invoice.setTaxCalculationType(taxCalculationType);
                            invoice.addItem(invoiceItem);
                            invoiceMap.put(reference, invoice);
                            invoiceNumberMap.put(reference, invoiceNumber);
                        } else {
                            invoice = invoiceMap.get(reference);
                            invoice.addItem(invoiceItem);
                        }
                    }
                }

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                //Add Header row into final result
                rejectedRows.add(rejectedCells);
            }
        }

        InvoiceNumberData numberData = invoiceServiceLocal.getSaleInvoiceNumber();
        for (String reference : invoiceMap.keySet()) {
            if (!ignoreSet.contains(reference)) {
                NewInvoice invoice = invoiceMap.get(reference);
                invoice.setItems(invoice.invoiceItemList.toArray(new NewInvoiceItem[]{}));

                String invoiceNumber = invoiceNumberMap.get(reference);
                if (invoiceNumber != null) {
                    invoice.setInvoiceNumber(invoiceNumber);
                } else {
                    invoice.setInvoiceNumber(invoiceNumber);
                    invoice.setNumberData(numberData);
                }

                invoice.setBookkeep(true);
                calculate(invoice);
                try {
                    SaveResult saveResult = invoiceServiceLocal.saveSaleInvoice(invoice);
                    if (saveResult.isInvoiceExist()) {
                        log.error("Invoice with number " + invoice.getInvoiceNumber() + " is exists!");
                        importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                    } else {
                        importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                    }
                    userManager.flushAndClear();
                } catch (Exception e) {
                    log.error("Error occurred while creating invoice with reference " + reference + " and number " + invoice.getInvoiceNumber(), e);
                    importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                }
            }
        }
        return rejectedRows;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importPurchaseOrder(ImportFile importFile, List<String[]> list) {
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Map<String, EdsCurrency> currencyMap = currencyManager.getListAsMap();
        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(null);

        List<TaxItem> taxItemList = Arrays.asList(accountingServiceLocal.getCompanyTaxesWithFilter(new ListingFilterParameter()));

        Map<String, EdsCrmAccount> crmAccountMap = crmAccountManager.getCrmAccountsMap(EdsCrmAccount.SUPPLIER, true);
        Map<String, EdsItem> productMap = itemManager.getProductsMapByNumber();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        HashSet<String> ignoreSet = new HashSet<>();
        LinkedHashMap<String, NewInvoice> purchaseOrderMap = new LinkedHashMap<>();

        Integer NUMBER_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_PO_NUMBER);
        Integer DATE_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_PO_DATE);
        Integer VALID_DATE_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_PO_VALID_DATE);
        Integer SUPPLIER_NUMBER = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_SUPPLIER_NUMBER);
        Integer CURRENCY_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_CURRENCY);
        Integer EXCHANGE_RATE_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_EXCHANGE_RATE);

        Integer PRODUCT_NUMBER_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_ITEM_NUMBER);
        Integer QTY_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_QTY);
        Integer PRICE_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_PRICE);
        Integer TAX_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_TAX_RATE);
        Integer SALES_ACCOUNT_COLUMN = importFile.getColumnID(ImportField.PurchaseOrderImportFields.FIELD_ACCOUNT_CODE);

        for (String[] row : list) {
            if (!hasHeader) {
                int columnID = 0;
                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellValue : row) {

                    if (rowIsEmpty && StringUtils.isNotBlank(cellValue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }

                String number = null, accountCode = null, currency = null;
                Date date = null, validDate = null;
                BigDecimal price = null, quantity = null, exchangeRate = null;
                EdsCrmAccount supplier = null;
                EdsItem product = null;
                TaxItem taxItem = null;

                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];

                boolean hasError = false;
                for (String columnValue : row) {

                    rejectedCells[columnID] = new RejectedImportRecord(columnValue);

                    columnValue = columnValue.trim();
                    if (StringUtils.isNotBlank(columnValue)) {
                        if (NUMBER_COLUMN.equals(columnID)) {
                            number = columnValue;
                        } else if (SUPPLIER_NUMBER.equals(columnID)) {
                            supplier = crmAccountMap.get(columnValue);
                            if (supplier == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (DATE_COLUMN.equals(columnID)) {
                            try {
                                date = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing date : " + columnValue + " Date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (VALID_DATE_COLUMN.equals(columnID)) {
                            try {
                                validDate = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing valid date : " + columnValue + " Valid date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (CURRENCY_COLUMN.equals(columnID)) {
                            currency = columnValue.toLowerCase();

                            if (currencyMap.get(currency) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (EXCHANGE_RATE_COLUMN.equals(columnID)) {
                            exchangeRate = parseBigDecimal(columnValue);
                            exchangeRate = Optional.ofNullable(exchangeRate).orElse(BigDecimal.ONE);
                        } else if (PRODUCT_NUMBER_COLUMN.equals(columnID)) {
                            product = productMap.get(columnValue);
                            if (product == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (QTY_COLUMN.equals(columnID)) {
                            quantity = parseBigDecimal(columnValue);
                            if (quantity == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[QTY_COLUMN].getData()));
                            }
                        } else if (PRICE_COLUMN.equals(columnID)) {
                            price = parseBigDecimal(columnValue);
                            if (price == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[PRICE_COLUMN].getData()));
                            }
                        } else if (SALES_ACCOUNT_COLUMN.equals(columnID)) {
                            accountCode = columnValue.replace("-", "");

                            if (accountMap.get(accountCode) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (TAX_COLUMN.equals(columnID)) {
                            String finalColumnValue = columnValue;
                            taxItem = taxItemList.stream().filter(item -> item.getName().toLowerCase().contains(finalColumnValue.toLowerCase())).findFirst().orElse(null);
                            if (taxItem == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        }
                    }
                    columnID++;
                }

                //Validate required fields
                if (date == null) {
                    if (DATE_COLUMN >= 0 && rejectedCells[DATE_COLUMN] != null) {
                        rejectedCells[DATE_COLUMN].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Sales Order Date. Date format should be " + DATE_PATTERN));
                    }
                    hasError = true;
                }
                if (validDate == null) {
                    if (VALID_DATE_COLUMN >= 0 && rejectedCells[VALID_DATE_COLUMN] != null) {
                        rejectedCells[VALID_DATE_COLUMN].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Valid Date. Date format should be " + DATE_PATTERN));
                    }
                    hasError = true;
                }
                if (supplier == null) {
                    if (SUPPLIER_NUMBER >= 0 && rejectedCells[SUPPLIER_NUMBER] != null) {
                        rejectedCells[SUPPLIER_NUMBER].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Supplier Number"));
                    }
                    hasError = true;
                }
                if (accountCode == null) {
                    if (SALES_ACCOUNT_COLUMN >= 0 && rejectedCells[SALES_ACCOUNT_COLUMN] != null) {
                        rejectedCells[SALES_ACCOUNT_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Sales Account"));
                    }
                    hasError = true;
                }
                if (number == null) {
                    if (NUMBER_COLUMN >= 0 && rejectedCells[NUMBER_COLUMN] != null) {
                        rejectedCells[NUMBER_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Sales Order Number"));
                    }
                    hasError = true;
                }
                if (currency == null) {
                    if (CURRENCY_COLUMN >= 0 && rejectedCells[CURRENCY_COLUMN] != null) {
                        rejectedCells[CURRENCY_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Currency"));
                    }
                    hasError = true;
                }
                if (product == null) {
                    if (PRODUCT_NUMBER_COLUMN >= 0 && rejectedCells[PRODUCT_NUMBER_COLUMN] != null) {
                        rejectedCells[PRODUCT_NUMBER_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Item"));
                    }
                    hasError = true;
                }
                if (quantity == null) {
                    if (QTY_COLUMN >= 0 && rejectedCells[QTY_COLUMN] != null) {
                        rejectedCells[QTY_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Quantity"));
                    }
                    hasError = true;
                }
                if (price == null) {
                    if (PRICE_COLUMN >= 0 && rejectedCells[PRICE_COLUMN] != null) {
                        rejectedCells[PRICE_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Price"));
                    }
                    hasError = true;
                }

                if (hasError) {
                    ignoreSet.add(number);
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                } else {
                    if (!ignoreSet.contains(number)) {
                        NewInvoiceItem quoteItem = new NewInvoiceItem();
                        quoteItem.setItemID(product.getObjectID());
                        quoteItem.setItemName(product.getName());
                        quoteItem.setAccountID(accountMap.get(accountCode));
                        quoteItem.setQuantity(quantity);
                        quoteItem.setUnitPrice(price);
                        quoteItem.setNet(quantity.multiply(price));
                        quoteItem.setWarehouse(defaultWarehouse.getAsSelectItem());
                        quoteItem.setTaxItem(taxItem);

                        NewInvoice quote;
                        if (purchaseOrderMap.get(number) == null) {
                            quote = new NewInvoice();
                            quote.setClientID(supplier.getObjectID());
                            quote.setCurrencyID(currencyMap.get(currency.toLowerCase()).getObjectID());
                            quote.setExchageRate(Optional.ofNullable(exchangeRate).orElse(BigDecimal.ONE));
                            quote.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(date)));
                            quote.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(validDate)));
                            quote.setType(PAYABLE);
                            quote.setStatusCode(Constants.APPROVE);
                            quote.setSalesOrder(false);
                            quote.setTaxCalculationType(2);

                            quote.addItem(quoteItem);
                            purchaseOrderMap.put(number, quote);
                        } else {
                            quote = purchaseOrderMap.get(number);
                            quote.addItem(quoteItem);
                        }
                    }
                }

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                //Add Header row into final result
                rejectedRows.add(rejectedCells);
            }
        }

        InvoiceNumberData numberData = invoiceCircularResolver.getQuoteOrderNumberData(PURCHASE_ORDER);
        for (String number : purchaseOrderMap.keySet()) {
            if (!ignoreSet.contains(number)) {
                NewInvoice purchaseOrder = purchaseOrderMap.get(number);

                Integer fourDigitNumber = numberData.parseFourDigitNumber(number);
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 1 ? ("000" + fourDigitNumber) : fourDigitNumber.toString());
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 2 ? ("00" + fourDigitNumber) : fourDigitNumber.toString());
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 3 ? ("0" + fourDigitNumber) : fourDigitNumber.toString());
                purchaseOrder.setFourDigitNumber(numberData.getFourDigitNumber());
                purchaseOrder.setInvoiceNumber(numberData.getInvoiceNumber());
                purchaseOrder.setNumberData(numberData);

                purchaseOrder.setItems(purchaseOrder.invoiceItemList.toArray(new NewInvoiceItem[0]));
                calculate(purchaseOrder);
                try {
                    SaveResult saveResult = quoteServiceLocal.savePurchaseOrder(purchaseOrder);
                    if (saveResult.isInvoiceExist()) {
                        log.error("Sales order with number " + purchaseOrder.getInvoiceNumber() + " is exists!");
                        importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                    } else {
                        //Increment number of imported ROWs
                        importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                    }
                } catch (Exception e) {
                    log.error("Error occurred while creating sales order with number " + number, e);
                    //Decrement number of imported ROWs
                    importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                }
            }
        }

        return rejectedRows;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importSalesOrder(ImportFile importFile, List<String[]> list) {
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Map<String, EdsCurrency> currencyMap = currencyManager.getListAsMap();
        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(null);
        Map<String, EdsUnitMeasurement> unitMeasurementMap = unitMeasurementManager.getAsMap();
        Map<String, EdsDepartment> departmentMap = new HashMap<>();

        List<TaxItem> taxItemList = Arrays.asList(accountingServiceLocal.getCompanyTaxesWithFilter(new ListingFilterParameter()));

        Map<String, EdsCrmAccount> crmAccountMap = new HashMap<>();
        Map<String, EdsItem> productMap = new HashMap<>();
        Set<GenericSettingsEnum> genericSettings = genericSettingsManager.getEnabledGenericSettings();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        HashSet<String> ignoreSet = new HashSet<>();
        LinkedHashMap<String, NewInvoice> salesOrderMap = new LinkedHashMap<>();

        Integer NUMBER_COLUMN = 0;
        Integer CUSTOMER_NAME_COLUMN = 1;
        Integer DATE_COLUMN = 2;
        Integer VALID_DATE_COLUMN = 3;
        Integer REFRENCE_COLUMN = 4;
        Integer CURRENCY_COLUMN = 5;
        Integer EXCHANGE_RATE_COLUMN = 6;
        Integer TAX_TYPE_COLUMN = 7;
        Integer SALES_PERSON_COLUMN = 8;//custom field
        Integer PO_DATE_COLUMN = 9; //custom field

        Integer PRODUCT_NUMBER_COLUMN = 10;
        Integer DESCRIPTION_COLUMN = 11;
        Integer QTY_COLUMN = 12;
        Integer UM_COLUMN = 13;
        Integer PRICE_COLUMN = 14;
        Integer DISCOUNT_TYPE_COLUMN = 15;
        Integer DISCOUNT_AMOUNT_COLUMN = 16;
        Integer SALES_ACCOUNT_COLUMN = 17;
        Integer TAX_COLUMN = 18;
        Integer DEPARTMENT_COLUMN = 19;
        Integer DELIVERY_TIME_COLUMN = 20;//custom field
        Integer REMARKS_COLUMN = 21; //custom field
        Integer ARTICLE_COLUMN = 22; //custom field

        for (String[] row : list) {
            if (!hasHeader) {
                int columnID = 0;
                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellValue : row) {

                    if (rowIsEmpty && StringUtils.isNotBlank(cellValue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }

                String number = null, accountCode = null, currency = null, reference = null, taxType, salesPerson, description = null, discountType = null, remarks, article, deliveryTime;
                Date date = null, validDate = null, poDate;
                BigDecimal price = null, quantity = null, exchangeRate = null, discountAmount = null;
                EdsCrmAccount client = null;
                EdsItem product = null;
                EdsUnitMeasurement unitMeasurement = null;
                EdsDepartment department = null;
                TaxItem taxItem = null;

                int taxCalculationType = 0;

                CompanyCustomFieldItem articleCustomFieldItem = null;
                CompanyCustomFieldItem deliveryTimeCustomFieldItem = null;
                CompanyCustomFieldItem remarksCustomFieldItem = null;

                CompanyCustomFieldItem poDateCustomFieldItem = null;
                CompanyCustomFieldItem salesPersonCustomFieldItem = null;

                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];

                boolean hasError = false;
                for (String columnValue : row) {

                    rejectedCells[columnID] = new RejectedImportRecord(columnValue);

                    columnValue = columnValue.trim();
                    if (StringUtils.isNotBlank(columnValue)) {
                        if (NUMBER_COLUMN.equals(columnID)) {
                            number = columnValue;
                        } else if (CUSTOMER_NAME_COLUMN.equals(columnID)) {
                            client = crmAccountMap.get(columnValue.toLowerCase());

                            if (client == null) {
                                client = crmAccountManager.getCrmAccountByName(columnValue);

                                if (client == null) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    crmAccountMap.put(columnValue.toLowerCase(), client);
                                }
                            }
                        } else if (DATE_COLUMN.equals(columnID)) {
                            try {
                                date = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing date : " + columnValue + " Date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (VALID_DATE_COLUMN.equals(columnID)) {
                            try {
                                validDate = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing valid date : " + columnValue + " Valid date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (REFRENCE_COLUMN.equals(columnID)) {
                            reference = columnValue;
                        } else if (CURRENCY_COLUMN.equals(columnID)) {
                            currency = columnValue.toLowerCase();

                            if (currencyMap.get(currency) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (EXCHANGE_RATE_COLUMN.equals(columnID)) {
                            exchangeRate = parseBigDecimal(columnValue);
                            exchangeRate = Optional.ofNullable(exchangeRate).orElse(BigDecimal.ONE);
                        } else if (TAX_TYPE_COLUMN.equals(columnID)) {
                            taxType = columnValue;
                            if (StringUtils.isBlank(taxType)) {
                                taxCalculationType = 0;
                            } else {
                                taxCalculationType = "TAX INCLUSIVE".equalsIgnoreCase(taxType.toUpperCase(Locale.ROOT)) ? 1 : 2;
                            }
                        } else if (SALES_PERSON_COLUMN.equals(columnID)) {
                            salesPerson = columnValue;

                            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getByAliasName(ViewName.SaleQuote.name(), "Sales_Person");
                            if (companyCustomFieldsSettings != null) {
                                salesPersonCustomFieldItem = new CompanyCustomFieldItem();
                                salesPersonCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                                salesPersonCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                                salesPersonCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                                salesPersonCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                                salesPersonCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                                salesPersonCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());

                                salesPersonCustomFieldItem.setFieldStringValue(salesPerson);
                            }

                        } else if (PO_DATE_COLUMN.equals(columnID)) {
                            try {
                                poDate = dateFormat.parse(columnValue);

                                EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getByAliasName(ViewName.SaleOrder.name(), "PO_DATE");
                                if (companyCustomFieldsSettings != null) {
                                    poDateCustomFieldItem = new CompanyCustomFieldItem();
                                    poDateCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                                    poDateCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                                    poDateCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                                    poDateCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                                    poDateCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                                    poDateCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());

                                    poDateCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(poDate));
                                }

                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing date : " + columnValue + " Date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (PRODUCT_NUMBER_COLUMN.equals(columnID)) {
                            product = productMap.get(columnValue.toLowerCase());

                            if (product == null) {
                                product = itemManager.getItemByNumber(columnValue);

                                if (product == null) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    productMap.put(columnValue.toLowerCase(), product);
                                }
                            }
                        } else if (DESCRIPTION_COLUMN.equals(columnID)) {
                            description = columnValue;
                        } else if (QTY_COLUMN.equals(columnID)) {
                            quantity = parseBigDecimal(columnValue);
                            if (quantity == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[QTY_COLUMN].getData()));
                            }
                        } else if (UM_COLUMN.equals(columnID)) {
                            unitMeasurement = unitMeasurementMap.get(columnValue.toLowerCase());
                            if (unitMeasurement == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (PRICE_COLUMN.equals(columnID)) {
                            price = parseBigDecimal(columnValue);
                            if (price == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[PRICE_COLUMN].getData()));
                            }
                        } else if (DISCOUNT_TYPE_COLUMN.equals(columnID)) {
                            discountType = columnValue;
                        } else if (DISCOUNT_AMOUNT_COLUMN.equals(columnID)) {
                            discountAmount = parseBigDecimal(columnValue);
                        } else if (SALES_ACCOUNT_COLUMN.equals(columnID)) {
                            accountCode = columnValue.replace("-", "");

                            if (accountMap.get(accountCode) == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (TAX_COLUMN.equals(columnID)) {
                            String finalColumnValue = columnValue;
                            taxItem = taxItemList.stream().filter(item -> item.getName().toLowerCase().contains(finalColumnValue.toLowerCase())).findFirst().orElse(null);
                            if (taxItem == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (DEPARTMENT_COLUMN.equals(columnID)) {
                            department = departmentMap.get(columnValue.toLowerCase());

                            if (department == null) {
                                List dl = departmentManager.getDepartmentByName(columnValue);

                                if (dl.isEmpty()) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    department = (EdsDepartment) dl.get(0);
                                    departmentMap.put(columnValue.toLowerCase(), department);
                                }
                            }
                        } else if (DELIVERY_TIME_COLUMN.equals(columnID)) {
                            try {
                                deliveryTime = columnValue;

                                EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getByAliasName(ViewName.SaleOrderItem.name(), "DELIVERY_TIME");
                                if (companyCustomFieldsSettings != null) {
                                    deliveryTimeCustomFieldItem = new CompanyCustomFieldItem();
                                    deliveryTimeCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                                    deliveryTimeCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                                    deliveryTimeCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                                    deliveryTimeCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                                    deliveryTimeCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                                    deliveryTimeCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());

                                    deliveryTimeCustomFieldItem.setFieldStringValue(deliveryTime);
                                }
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat, columnValue));
                                log.error("Error occurred while parsing date : " + columnValue + " Date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        } else if (REMARKS_COLUMN.equals(columnID)) {
                            remarks = columnValue;
                            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getByAliasName(ViewName.SaleOrderItem.name(), "REMARKS");
                            if (companyCustomFieldsSettings != null) {
                                remarksCustomFieldItem = new CompanyCustomFieldItem();
                                remarksCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                                remarksCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                                remarksCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                                remarksCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                                remarksCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                                remarksCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());

                                remarksCustomFieldItem.setFieldStringValue(remarks);
                            }
                        } else if (ARTICLE_COLUMN.equals(columnID)) {
                            article = columnValue;
                            EdsCompanyCustomFieldsSettings companyCustomFieldsSettings = companyCustomFieldsManager.getByAliasName(ViewName.SaleOrderItem.name(), "ARTICLE");
                            if (companyCustomFieldsSettings != null) {
                                articleCustomFieldItem = new CompanyCustomFieldItem();
                                articleCustomFieldItem.setEntityId(companyCustomFieldsSettings.getObjectID());
                                articleCustomFieldItem.setFieldName(companyCustomFieldsSettings.getFieldName());
                                articleCustomFieldItem.setAliasName(companyCustomFieldsSettings.getAliasName());
                                articleCustomFieldItem.setColumnCode(companyCustomFieldsSettings.getColumnCode());
                                articleCustomFieldItem.setDataType(companyCustomFieldsSettings.getDataType());
                                articleCustomFieldItem.setUiType(companyCustomFieldsSettings.getUiType());

                                articleCustomFieldItem.setFieldStringValue(article);
                            }
                        }
                    }
                    columnID++;
                }

                //Validate required fields
                if (date == null) {
                    rejectedCells[DATE_COLUMN].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Sales Order Date. Date format should be " + DATE_PATTERN));
                    hasError = true;
                }
                if (validDate == null) {
                    rejectedCells[DATE_COLUMN].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Valid Date. Date format should be " + DATE_PATTERN));
                    hasError = true;
                }
                if (client == null) {
                    rejectedCells[CUSTOMER_NAME_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Customer Name"));
                    hasError = true;
                }
                if (accountCode == null) {
                    rejectedCells[SALES_ACCOUNT_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Sales Account"));
                    hasError = true;
                }
                if (number == null) {
                    rejectedCells[PRODUCT_NUMBER_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Sales Order Number"));
                    hasError = true;
                }
                if (currency == null) {
                    rejectedCells[CURRENCY_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Currency"));
                    hasError = true;
                }
                if (product == null) {
                    rejectedCells[PRODUCT_NUMBER_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Item"));
                    hasError = true;
                }
                if (quantity == null) {
                    rejectedCells[QUANTITY].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Quantity"));
                    hasError = true;
                }
                if (price == null) {
                    rejectedCells[PRICE_COLUMN].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Price"));
                    hasError = true;
                }

                if (hasError) {
                    ignoreSet.add(number);
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                } else {
                    if (!ignoreSet.contains(number)) {
                        NewInvoiceItem quoteItem = new NewInvoiceItem();
                        quoteItem.setItemID(product.getObjectID());
                        quoteItem.setItemName(product.getName());
                        quoteItem.setDescription(Optional.ofNullable(description).orElse(product.getDescription()));
                        quoteItem.setAccountID(accountMap.get(accountCode));
                        quoteItem.setQuantity(quantity);
                        quoteItem.setUnitPrice(price);
                        if ("Percentage".equalsIgnoreCase(discountType)) {
                            quoteItem.setDiscountPercent(discountAmount);
                            quoteItem.setDiscountAmount(null);
                        } else {
                            quoteItem.setDiscountAmount(discountAmount);
                            quoteItem.setDiscountPercent(null);
                        }

                        quoteItem.setNet(quantity.multiply(price));
                        quoteItem.setWarehouse(defaultWarehouse.getAsSelectItem());
                        quoteItem.setMeasurement(unitMeasurement != null ? unitMeasurement.getAsSelectItem() : null);
                        quoteItem.setTaxItem(taxItem);
                        quoteItem.setDepartmentItem(department != null ? department.getAsSelectItem() : null);

                        ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();
                        if (remarksCustomFieldItem != null) {
                            itemCustomFields.add(remarksCustomFieldItem);
                        }
                        if (deliveryTimeCustomFieldItem != null) {
                            itemCustomFields.add(deliveryTimeCustomFieldItem);
                        }
                        if (articleCustomFieldItem != null) {
                            itemCustomFields.add(articleCustomFieldItem);
                        }

                        quoteItem.setCustomFieldItems(itemCustomFields);

                        NewInvoice quote;
                        if (salesOrderMap.get(number) == null) {
                            quote = new NewInvoice();
                            quote.setClientID(client.getObjectID());
                            quote.setCurrencyID(currencyMap.get(currency.toLowerCase()).getObjectID());
                            quote.setExchageRate(Optional.ofNullable(exchangeRate).orElse(BigDecimal.ONE));
                            quote.setReference(reference);
                            quote.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(date)));
                            quote.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(validDate)));
                            quote.setType(RECEIVABLE);
                            quote.setStatusCode(Constants.SALE_ORDER);
                            quote.setSalesOrder(true);
                            quote.setTaxCalculationType(taxCalculationType);

                            ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();
                            if (salesPersonCustomFieldItem != null) {
                                customFields.add(salesPersonCustomFieldItem);
                            }
                            if (poDateCustomFieldItem != null) {
                                customFields.add(poDateCustomFieldItem);
                            }
                            quote.setCustomFieldItems(customFields);

                            quote.addItem(quoteItem);
                            salesOrderMap.put(number, quote);
                        } else {
                            quote = salesOrderMap.get(number);
                            quote.addItem(quoteItem);
                        }
                    }
                }

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                //Add Header row into final result
                rejectedRows.add(rejectedCells);
            }
        }

        InvoiceNumberData numberData = invoiceCircularResolver.getQuoteOrderNumberData(SALE_ORDER);
        for (String number : salesOrderMap.keySet()) {
            if (!ignoreSet.contains(number)) {
                NewInvoice salesQuote = salesOrderMap.get(number);

                Integer fourDigitNumber = numberData.parseFourDigitNumber(number);
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 1 ? ("000" + fourDigitNumber.toString()) : fourDigitNumber.toString());
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 2 ? ("00" + fourDigitNumber.toString()) : fourDigitNumber.toString());
                numberData.setFourDigitNumber(fourDigitNumber.toString().length() == 3 ? ("0" + fourDigitNumber.toString()) : fourDigitNumber.toString());
                salesQuote.setFourDigitNumber(numberData.getFourDigitNumber());
                salesQuote.setInvoiceNumber(numberData.getInvoiceNumber());
                salesQuote.setNumberData(numberData);

                salesQuote.setItems(salesQuote.invoiceItemList.toArray(new NewInvoiceItem[0]));
                calculate(salesQuote);
                try {
                    SaveResult saveResult = quoteServiceLocal.saveSaleQuoteForBatchImport(salesQuote, genericSettings, financialSettings, defaultWarehouse);
                    if (saveResult.isInvoiceExist()) {
                        log.error("Sales order with number " + salesQuote.getInvoiceNumber() + " is exists!");
                        importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                    } else {
                        //Increment number of imported ROWs
                        importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                    }
                    userManager.flushAndClear();
                } catch (Exception e) {
                    log.error("Error occurred while creating sales order with number " + number, e);
                    //Decrement number of imported ROWs
                    importFile.setImportedColumns(importFile.getImportedColumns() - 1);
                }
            }
        }

        return rejectedRows;
    }

    private void calculate(NewInvoice salesQuote) {
        boolean isRoundingModeDisabled = genericSettingsManager.exists(GenericSettingsEnum.ROUNDING_MODE_DISABLED);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal netTotal;
        BigDecimal totalValue;
        BigDecimal totalValueInBase;
        BigDecimal totalTax;

        Map<Integer, BigDecimal> taxTotalMap = new HashMap<>();

        for (NewInvoiceItem quoteItem : salesQuote.getItems()) {
            BigDecimal itemTotalPrice = quoteItem.getQuantity().multiply(quoteItem.getUnitPrice());
            BigDecimal discountedNet = itemTotalPrice;

            discountedNet = quoteItem.getDiscountPercent() != null ? itemTotalPrice.subtract(itemTotalPrice.multiply(quoteItem.getDiscountPercent()).divide(new BigDecimal("100.00"), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)) : itemTotalPrice.subtract(quoteItem.getDiscountAmount());

            totalDiscount = totalDiscount.add(quoteItem.getDiscountPercent() != null
                    ? itemTotalPrice.multiply(quoteItem.getDiscountPercent()).divide(new BigDecimal("100.00"), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)
                    : quoteItem.getDiscountAmount());

            BigDecimal itemTaxAmount = BigDecimal.ZERO;
            if (quoteItem.getTaxItem() != null) {
                itemTaxAmount = calculateTaxAmount(quoteItem.getTaxItem(), taxTotalMap, discountedNet, salesQuote.getTaxCalculationType(), isRoundingModeDisabled);
            }
            quoteItem.setTaxAmount(itemTaxAmount);

            subtotal = subtotal.add(itemTotalPrice);
        }

        totalDiscount = totalDiscount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        netTotal = subtotal.subtract(totalDiscount);
        totalTax = getTotalTaxAmount(taxTotalMap);
        totalValue = subtotal.subtract(totalDiscount).add(!(salesQuote.getTaxCalculationType().equals(1) || salesQuote.getTaxCalculationType().equals(0)) ? totalTax : BigDecimal.ZERO);

        totalValue = totalValue.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        totalValueInBase = totalValue.divide(salesQuote.getExchageRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

        salesQuote.setTotalTaxes(totalTax);
        salesQuote.setTotal(totalValue);
        salesQuote.setTotalInInvoiceCurrency(totalValueInBase);
        salesQuote.setNetAmountTotal(netTotal);
        salesQuote.setSubtotal(subtotal);
        salesQuote.setTotalDiscount(totalDiscount);

    }

    private BigDecimal calculateTaxAmount(TaxItem taxItem, Map<Integer, BigDecimal> taxTotal, BigDecimal discountedNet, Integer taxCalculationType, boolean isRoundingModeDisabled) {
        BigDecimal itemTaxAmount = BigDecimal.ZERO;

        if (taxCalculationType == 1) {
            BigDecimal taxPercent = taxItem.getEffectiveTaxPercent();
            itemTaxAmount = discountedNet.multiply(taxPercent).divide(new BigDecimal("100.00").add(taxPercent), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else if (taxCalculationType == 2) {
            itemTaxAmount = discountedNet.multiply(taxItem.getEffectiveTaxPercent().divide(new BigDecimal("100.00"), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        if (!isRoundingModeDisabled) {
            itemTaxAmount = itemTaxAmount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        }

        BigDecimal totalTax = taxTotal.get(taxItem.getId());
        BigDecimal currentTaxTotal = (totalTax != null ? totalTax : BigDecimal.ZERO).add(itemTaxAmount);

        taxTotal.put(taxItem.getId(), currentTaxTotal);

        return itemTaxAmount;
    }

    private BigDecimal getTotalTaxAmount(Map<Integer, BigDecimal> taxTotal) {
        BigDecimal total = BigDecimal.ZERO;

        for (BigDecimal taxAmount : taxTotal.values()) {
            total = total.add(taxAmount.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        return total;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importBatchInvoicePayment(ImportFile importFile, List<String[]> list) {
        ArrayList<RejectedImportRecord[]> rejectedRows = new ArrayList<>();
        boolean hasHeader = importFile.isHasHeader();

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        Map<String, Integer> accountMap = accountingManager.getAccountAsMapByCode(null);
        Map<String, EdsSaleInvoice> invoiceMap = new HashMap<>();
        LinkedHashMap<String, ArrayList<PaymentData>> invoicePaymentMap = new LinkedHashMap<>();

        HashSet<String> ignoreSet = new HashSet<>();

        Integer INVOICE_NUMBER = 0;
        Integer ACCOUNT_CODE = 1;
        Integer AMOUNT = 2;
        Integer REFERENCE = 3;
        Integer PAYMENT_DATE = 4;

        for (String[] row : list) {
            if (!hasHeader) {
                int columnID = 0;
                boolean rowIsEmpty = true;//To check if row is not blank
                for (String cellValue : row) {

                    if (rowIsEmpty && StringUtils.isNotBlank(cellValue)) {
                        rowIsEmpty = false;
                        break;
                    }
                }
                if (rowIsEmpty) {
                    continue;
                }

                String accountCode = null, reference = null;
                Date paymentDate = null;
                BigDecimal amount = null;
                List<EdsBaseSaleInvoice> edsBaseSaleInvoiceList = null;
                EdsSaleInvoice invoice = null;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];

                boolean hasError = false;
                for (String columnValue : row) {

                    rejectedCells[columnID] = new RejectedImportRecord(columnValue);

                    columnValue = columnValue.trim();
                    if (StringUtils.isNotBlank(columnValue)) {
                        if (INVOICE_NUMBER.equals(columnID)) {

                            invoice = invoiceMap.get(columnValue.toLowerCase());

                            if (invoice == null) {
                                try {
                                    edsBaseSaleInvoiceList = invoiceManager.getSaleInvoiceByNumber(columnValue, null);
                                } catch (Exception e) {
                                    log.error("Error occurred while getting invoice by number " + columnValue, e);
                                }
                                if (edsBaseSaleInvoiceList == null || edsBaseSaleInvoiceList.size() == 0) {
                                    hasError = true;
                                    rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                } else {
                                    for (EdsBaseSaleInvoice saleInvoice : edsBaseSaleInvoiceList) {
                                        invoiceMap.put(columnValue.toLowerCase(), (EdsSaleInvoice) saleInvoice);
                                        invoice = (EdsSaleInvoice) saleInvoice;
                                    }
                                }
                            }

                        } else if (ACCOUNT_CODE.equals(columnID)) {
                            accountCode = columnValue.replace("-", "");

                            if (accountMap.get(accountCode) == null) {
                                hasError = true;
                                accountCode = null;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            }
                        } else if (AMOUNT.equals(columnID)) {
                            amount = parseBigDecimal(columnValue);
                            if (amount == null) {
                                hasError = true;
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, rejectedCells[AMOUNT].getData()));
                            }
                        } else if (REFERENCE.equals(columnID)) {
                            reference = columnValue;
                        } else if (PAYMENT_DATE.equals(columnID)) {
                            try {
                                paymentDate = dateFormat.parse(columnValue);
                            } catch (Exception e) {
                                rejectedCells[columnID].setErrorComment(commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                                log.error("Error occurred while parsing payment date : " + columnValue + " Payment date format should be " + DATE_PATTERN, e);
                                hasError = true;
                            }
                        }
                    }
                    columnID++;
                }

                //Validate required fields
                if (invoice == null) {
                    rejectedCells[INVOICE_NUMBER].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Invoice Number"));
                    hasError = true;
                }
                if (accountCode == null) {
                    rejectedCells[ACCOUNT_CODE].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Account Code"));
                    hasError = true;
                }
                if (amount == null) {
                    rejectedCells[AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, "Amount"));
                    hasError = true;
                } else {
                    if (invoice != null) {
                        BigDecimal fullPayments = invoice.getFullPayments().add(amount);
                        if (invoice.getTotalInInvoiceCurrency().setScale(calculationScale, RoundingMode.HALF_UP).compareTo(fullPayments.setScale(calculationScale, RoundingMode.HALF_UP)) < 0) {
                            rejectedCells[AMOUNT].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.overPaymentAmount, amount));
                            hasError = true;
                        }
                    }
                }
                if (paymentDate == null) {
                    rejectedCells[PAYMENT_DATE].setErrorComment(commonLocalizer.localize(PdfLocalizationName.valueCannotBeEmpty, "Payment Date"));
                    hasError = true;
                }

                if (hasError) {
                    ignoreSet.add(invoice.getNumber());
                    rejectedRows.add(rejectedCells);
                    //Increment number of rejected ROWs
                    importFile.setIgnoredColumns(importFile.getIgnoredColumns() + 1);
                } else {
                    PaymentData invoicePayment = new PaymentData();
                    invoicePayment.setInvoiceID(invoice.getObjectID());
                    invoicePayment.setPaymentAccount(new SelectItem(accountMap.get(accountCode)));
                    invoicePayment.setReferenceNumber(reference);
                    invoicePayment.setDate(new DateNonConvertable(paymentDate));
                    invoicePayment.setPaymentAmount(amount);
                    invoicePayment.setType(Constants.RECEIVABLE);
                    invoicePayment.setExchangeRate(new BigDecimal("1.00"));
                    invoicePayment.setValidateReference(false);

                    if (invoicePaymentMap.get(invoice.getNumber()) == null) {
                        ArrayList<PaymentData> invoicePaymentList = new ArrayList<>();
                        invoicePaymentList.add(invoicePayment);
                        invoicePaymentMap.put(invoice.getNumber(), invoicePaymentList);
                    } else {
                        invoicePaymentMap.get(invoice.getNumber()).add(invoicePayment);
                    }
                }

            } else {
                hasHeader = false;
                RejectedImportRecord[] rejectedCells = new RejectedImportRecord[row.length];
                int cellIndex = 0;
                for (String cellValue : row) {
                    rejectedCells[cellIndex++] = new RejectedImportRecord(cellValue);
                }
                //Add Header row into final result
                rejectedRows.add(rejectedCells);
            }
        }

        for (String invoiceNumber : invoicePaymentMap.keySet()) {
            if (!ignoreSet.contains(invoiceNumber)) {
                try {
                    ArrayList<PaymentData> invoicePayments = invoicePaymentMap.get(invoiceNumber);
                    for (PaymentData paymentData : invoicePayments) {
                        Integer result = invoiceServiceLocal.savePayment(paymentData);
                        if (result > 0) {
                            //Increment number of imported ROWs
                            importFile.setImportedColumns(importFile.getImportedColumns() + 1);
                        } else {
                            importFile.setImportedColumns(importFile.getImportedColumns() > 0 ? importFile.getImportedColumns() - 1 : 0);
                            log.error("Payment that amount is " + paymentData.getPaymentAmount() + " has been rejected due to overpaid amount. Related invoice is " + paymentData.getInvoiceNumber());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occurred while creating invoice with number " + invoiceNumber, e);
                    //Decrement number of imported ROWs
                    importFile.setImportedColumns(importFile.getImportedColumns() > 0 ? importFile.getImportedColumns() - 1 : 0);
                }
            }
        }

        return rejectedRows;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ArrayList<RejectedImportRecord[]> importProducts(ImportFile importFile, List<String[]> dataBank) {
        return accountingService.importProducts(importFile, dataBank);
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importProductCategories(ImportFile importFile, List<String[]> data) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer FIELD_CATEGORY_NAME = importFile.getColumnID(ImportField.ProductCategoriesFields.FIELD_NAME);
        Integer FIELD_CATEGORY_CODE = importFile.getColumnID(ImportField.ProductCategoriesFields.FIELD_CODE);
        Integer FIELD_PARENT_CATEGORY = importFile.getColumnID(ImportField.ProductCategoriesFields.FIELD_PARENT_CATEGORY);
        Integer FIELD_ORDER = importFile.getColumnID(ImportField.ProductCategoriesFields.FIELD_ORDER);

        boolean hasHeader = importFile.isHasHeader();

        int impRows = 0;
        int ignoredRows = 0;
        int overwrittenRows = 0;
        int skippedRows = 0;
        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            ProductCategoryItem item = new ProductCategoryItem();
            int columnID = 0;
            for (String columnValue : row) {
                if (columnID == FIELD_CATEGORY_NAME) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        item.setName(columnValue);
                    }
                } else if (columnID == FIELD_CATEGORY_CODE) {
                    if (StringUtils.isBlank(columnValue)) {
                        NumberData numberData = accountingServiceImpl.generateProductCategoryNumber();
                        item.setCode(numberData.getNumberString());
                        item.setPrefix(numberData.getFirstNumberString());
                        item.setIntNumber(numberData.getIntNumber());
                    } else {
                        String trim = columnValue.trim();
                        item.setCode(trim);
                        Integer index = 0;
                        for (int i = 0; i < trim.length(); i++) {
                            Integer value = null;
                            try {
                                value = Integer.parseInt(trim.substring(i, i + 1));
                            } catch (NumberFormatException ignored) {
                            }
                            if (value != null) {
                                index = i;
                                break;
                            }
                        }
                        item.setPrefix(trim.substring(0, index));
                        item.setIntNumber(Integer.parseInt(trim.substring(index)));
                    }
                } else if (columnID == FIELD_PARENT_CATEGORY) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        EdsProductCategory parent = productCategoryManager.getCategoryByName(columnValue);
                        if (parent == null) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.parentCategoryNotFound, rejectedRecords.get(0)[columnID].getData()));
                            isValid = false;
                        } else {
                            item.setParentCategoryID(parent.getObjectID());
                        }
                    }
                } else if (columnID == FIELD_ORDER) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        item.setOrder(Integer.valueOf(columnValue));
                    }
                }
                columnID++;
            }
            if (isValid) {
                Integer result = accountingServiceImpl.saveProductCategory(item);
                if (result == 0) {
                    if (importFile.isMerge()) {
                        EdsProductCategory existing = productCategoryManager.getCategoryByName(item.getName());
                        item.setId(existing.getObjectID());
                        accountingServiceImpl.saveProductCategory(item);
                        overwrittenRows++;
                        importFile.setClonedColumns(overwrittenRows);
                    } else {
                        skippedRows++;
                        importFile.setSkippedColumns(skippedRows);
                    }
                } else {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
            }
        }


        return rejectedRecords;
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importBrands(ImportFile importFile, List<String[]> data) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer FIELD_BRAND_NAME = importFile.getColumnID(ImportField.BrandFields.FIELD_NAME);
        Integer FIELD_BRAND_PARENT = importFile.getColumnID(ImportField.BrandFields.FIELD_PARENT);
        Integer FIELD_BRAND_DESCRIPTION = importFile.getColumnID(ImportField.BrandFields.FIELD_DESCRIPTION);

        boolean hasHeader = importFile.isHasHeader();

        int impRows = 0;
        int ignoredRows = 0;
        int overwrittenRows = 0;
        int skippedRows = 0;
        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            BrandItem item = new BrandItem();
            int columnID = 0;
            for (String columnValue : row) {
                if (columnID == FIELD_BRAND_NAME) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        item.setName(columnValue);
                    }
                } else if (columnID == FIELD_BRAND_PARENT) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        String trim = columnValue.trim();
                        EdsBrand brand = brandManager.getBrandByName(trim);
                        if (brand != null) {
                            item.setParentBrandID(brand.getObjectID());
                        }
                    }
                } else if (columnID == FIELD_BRAND_DESCRIPTION) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        item.setDescription(columnValue);
                    }
                }
                columnID++;
            }
            if (isValid) {
                Integer result = accountingServiceImpl.saveBrand(item);
                if (result == 0) {
                    if (importFile.isMerge()) {
                        EdsBrand existing = brandManager.getBrandByName(item.getName());
                        item.setId(existing.getObjectID());
                        accountingServiceImpl.saveBrand(item);
                        overwrittenRows++;
                        importFile.setClonedColumns(overwrittenRows);
                    } else {
                        skippedRows++;
                        importFile.setSkippedColumns(skippedRows);
                    }
                } else {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
            }
        }


        return rejectedRecords;
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importDepartment(ImportFile importFile, List<String[]> data, Integer companyId) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer FIELD_DEPARTMENT_NUMBER = importFile.getColumnID(ImportField.DepartmentFields.FIELD_NUMBER);
        Integer FIELD_DEPARTMENT_NAME = importFile.getColumnID(ImportField.DepartmentFields.FIELD_NAME);
        Integer FIELD_DEPARTMENT_LOCATION = importFile.getColumnID(ImportField.DepartmentFields.FILED_LOCATION);
        Integer FIELD_DEPARTMENT_PARENT = importFile.getColumnID(ImportField.DepartmentFields.FILED_PARENT);
        Integer FIELD_DEPARTMENT_DATE = importFile.getColumnID(ImportField.DepartmentFields.FIELD_START_DATE);
        Integer FIELD_DEPARTMENT_STATUS = importFile.getColumnID(ImportField.DepartmentFields.FIELD_STATUS);
        Integer FIELD_DEPARTMENT_AR = importFile.getColumnID(ImportField.DepartmentFields.FIELD_DEPARTMENT_AR);
        Integer FIELD_DEPARTMENT_RU = importFile.getColumnID(ImportField.DepartmentFields.FIELD_DEPARTMENT_RU);
        Integer FIELD_DEPARTMENT_UZ = importFile.getColumnID(ImportField.DepartmentFields.FIELD_DEPARTMENT_UZ);
        Integer FIELD_DEPARTMENT_EN = importFile.getColumnID(ImportField.DepartmentFields.FIELD_DEPARTMENT_EN);

        boolean hasHeader = importFile.isHasHeader();


        Map<String, Integer> locationIDs = ServerUtils.mapNameIDs(locationManager.getListByCode());
        Map<String, Integer> key = new HashMap<>();

        int impRows = importFile.getImportedColumns() != null ? importFile.getImportedColumns() : 0;
        int ignoredRows = importFile.getIgnoredColumns() != null ? importFile.getIgnoredColumns() : 0;
        int overwrittenRows = importFile.getOverwrittenColumns() != null ? importFile.getOverwrittenColumns() : 0;
        int skippedRows = importFile.getSkippedColumns() != null ? importFile.getSkippedColumns() : 0;

        ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);

        Map<String, Integer> departments = new HashMap<>();

        List<Object[]> departmentListByCode = departmentManager.getListByCode();
        for (Object objects : departmentListByCode) {
            Object[] dataa = (Object[]) objects;
            String departmentcode = (String) dataa[0];
            Integer departmentId = (Integer) dataa[1];

            key.put(departmentcode, departmentId);
        }


        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            NewTeam item = new NewTeam();
            int columnID = 0;
            ReferenceLocale referenceLocale = null;
            int parentId;

            for (String columnValue : row) {

                if (columnID == FIELD_DEPARTMENT_NUMBER) {
                    if (StringUtils.isBlank(columnValue)) {
                        NumberData numberData = generateDepartmentNumber();
                        item.setNumberData(numberData);
                    } else {
                        String trim = columnValue.trim();
                        item.setDepartmentCode(trim);
                        Integer index = 0;
                        for (int i = 0; i < trim.length(); i++) {
                            Integer value = null;
                            try {
                                value = Integer.parseInt(trim.substring(i, i + 1));
                            } catch (NumberFormatException ignored) {
                            }
                            if (value != null) {
                                index = i;
                                break;
                            }
                        }
                        item.setNumberData(new NumberData(columnValue));
                    }
                } else if (columnID == FIELD_DEPARTMENT_NAME) {
                    if (!"".equals(columnValue)) {
                        item.setName(columnValue);
                    } else {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = true;
                    }
                } else if ((columnID >= FIELD_DEPARTMENT_AR && columnID <= FIELD_DEPARTMENT_EN)) {
                    if (referenceLocale == null) {
                        referenceLocale = new ReferenceLocale();
                    }

                    if (columnID == FIELD_DEPARTMENT_AR) {
                        referenceLocale.setArabic(columnValue);
                    } else if (columnID == FIELD_DEPARTMENT_RU) {
                        referenceLocale.setRussian(columnValue);
                    } else if (columnID == FIELD_DEPARTMENT_UZ) {
                        referenceLocale.setUzbek(columnValue);
                    } else if (columnID == FIELD_DEPARTMENT_EN) {
                        referenceLocale.setEnglish(columnValue);
                    } else {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = false;
                    }
                } else if (columnID == FIELD_DEPARTMENT_STATUS) {
                    if (!"".equals(columnValue)) {
                        if (TRUE.equals(columnValue)) {
                            item.setActive(true);
                        }
                    } else {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = true;
                    }
                } else if (columnID == FIELD_DEPARTMENT_PARENT) {
                    if (!"".equals(columnValue) && key.get(columnValue.toLowerCase()) != null) {
                        item.setParent(new SelectItem(Integer.valueOf(key.get(columnValue.toLowerCase()))));
                    }
                } else if (columnID == FIELD_DEPARTMENT_DATE) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords));
                        isValid = false;
                    } else {
                        Date startDate;
                        try {
                            startDate = dateFormat.parse(columnValue);
                            item.setStartDate(startDate);
                        } catch (ParseException e) {
                            rejectedRow[columnID].setErrorComment("Can not be parsed. Not a valid date");
                            isValid = false;
                        }
                    }
                } else if (columnID == FIELD_DEPARTMENT_LOCATION) {
                    if (!"".equals(columnValue)) {
                        Integer locationID = locationIDs.get(columnValue.toLowerCase());
                        if (locationID != null) {
                            item.setLocation(new SelectItem(locationID));
                        } else {
                            rejectedRow[FIELD_DEPARTMENT_LOCATION].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    } else {
                        isValid = true;
                    }
                } else if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                    for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                        if (!rejectedRecords.isEmpty()) {
                            if (columnID != importFile.getExtraColumnID(extraColumnEntry.getValue()) || extraColumnEntry.getKey() < ImportField.PositionFields.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                continue;
                            }
                            String fieldName = rejectedRecords.size() > 0 && rejectedRecords.get(0).length > columnID ? rejectedRecords.get(0)[columnID].getData() : "";
                            CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, columnID, columnValue, rejectedRow, fieldName);
                            if (customField == null) {
                                isValid = false;
                                continue;
                            }
                            customFields.add(customField);
                        }
                    }
                }
                columnID++;
            }


            if (!customFields.isEmpty()) {
                item.setCustomFieldItems(customFields);
            }
            if (isValid) {
                item.setReferenceLocale(referenceLocale);
                Integer result = departmentService.createTeam(item);
                if (result != 0 && item.getParent() != null) {
                    departmentTreeManager.addChildTree(result, item.getParent().getId());
                }
                if (result != 0) {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                    log.info("=== Department saved with id: " + result + " successfully! ===");
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
                log.info("=== Department with row: " + ignoredRows + " rejected ===");
            }


        }
        return rejectedRecords;

    }

    public NumberData generateDepartmentNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = departmentManager.getDepartmentLastIntNumber();
        if (settings != null && settings.getDepartmentNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getDepartmentNumberingFormat(), settings.getDelimetrDepartmentNumbering(), null, null, null, "department");
            numberData.setDelimiter(settings.getDelimetrDepartmentNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_DEPARTMENT_PREFIX /*true*/);
        }
    }

    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importPosition(ImportFile importFile, List<String[]> data, Integer companyId) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer FIELD_POSITION_CODE = importFile.getColumnID(ImportField.PositionFields.FIELD_NUMBER);
        Integer FIELD_POSITION = importFile.getColumnID(ImportField.PositionFields.FIELD_POSITION);
        Integer FIELD_POSITION_AR = importFile.getColumnID(ImportField.PositionFields.FIELD_POSITION_AR);
        Integer FIELD_POSITION_RU = importFile.getColumnID(ImportField.PositionFields.FIELD_POSITION_RU);
        Integer FIELD_POSITION_UZ = importFile.getColumnID(ImportField.PositionFields.FIELD_POSITION_UZ);
        Integer FIELD_POSITION_LOCATION = importFile.getColumnID(ImportField.PositionFields.FIELD_LOCATION);
        Integer FIELD_POSITION_DEPARTMENT = importFile.getColumnID(ImportField.PositionFields.FIELD_DEPARTMENT);
        Integer FIELD_POSITION_PLACE_COUNT = importFile.getColumnID(ImportField.PositionFields.FIELD_PLACE_COUNT);
        Integer FIELD_POSITION_TYPE = importFile.getColumnID(ImportField.PositionFields.FIELD_TYPE);
        Integer FIELD_POSITION_COEFFICENT = importFile.getColumnID(ImportField.PositionFields.FIELD_COEFFICENT);

        boolean hasHeader = importFile.isHasHeader();
        Map<String, Integer> locationIDs = ServerUtils.mapNameIDs(locationManager.getListByCode());
        Map<String, Integer> key = new HashMap<>();
        Map<String, Integer> lOckey = new HashMap<>();
        Map<String, Integer> positionTypeIds = listToMapIDs(referenceManager.listReferences("POSITION_TYPE"));

        List<Object[]> departmentListByCode = departmentManager.getListByCode();
        for (Object objects : departmentListByCode) {
            Object[] dataa = (Object[]) objects;
            String departmentcode = (String) dataa[0];
            Integer departmentId = (Integer) dataa[1];
            Integer depLocationId = (Integer) dataa[2];

            key.put(departmentcode, departmentId);
            lOckey.put(departmentcode, depLocationId);
        }
        ArrayList<CompanyCustomFieldItem> customFields = new ArrayList<>();

        int impRows = importFile.getImportedColumns() != null ? importFile.getImportedColumns() : 0;
        int ignoredRows = importFile.getIgnoredColumns() != null ? importFile.getIgnoredColumns() : 0;
        int overwrittenRows = importFile.getOverwrittenColumns() != null ? importFile.getOverwrittenColumns() : 0;
        int skippedRows = importFile.getSkippedColumns() != null ? importFile.getSkippedColumns() : 0;

        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            PositionItem item = new PositionItem();
            int columnID = 0;
            ReferenceLocale referenceLocale = null;
            Integer depByLocationId = null;
            for (String columnValue : row) {
                if (columnID == FIELD_POSITION_CODE) {
                    if (StringUtils.isBlank(columnValue)) {
                        NumberData numberData = hrmsService.generatePositionNumber();
                        item.setCode(numberData.getNumberString());
                        item.setPrefix(numberData.getFirstNumberString());
                        item.setIntNumber(numberData.getIntNumber());
                        item.setNumberData(numberData);
                    } else {
                        String trim = columnValue.trim();
                        item.setCode(trim);
                        Integer index = 0;
                        for (int i = 0; i < trim.length(); i++) {
                            Integer value = null;
                            try {
                                value = Integer.parseInt(trim.substring(i, i + 1));
                            } catch (NumberFormatException ignored) {
                            }
                            if (value != null) {
                                index = i;
                                break;
                            }
                        }
                        item.setPrefix(trim.substring(0, index));
                        item.setIntNumber(Integer.parseInt(trim.substring(index)));
                        item.setNumberData(new NumberData(trim, item.getIntNumber()));
                    }
                } else if ((columnID >= FIELD_POSITION && columnID <= FIELD_POSITION_UZ)) {
                    if (referenceLocale == null) {
                        referenceLocale = new ReferenceLocale();
                    }
                    if (columnID == FIELD_POSITION) {
                        referenceLocale.setEnglish(columnValue);
                        item.setName(columnValue);
                    } else if (columnID == FIELD_POSITION_AR) {
                        referenceLocale.setArabic(columnValue);
                    } else if (columnID == FIELD_POSITION_RU) {
                        referenceLocale.setRussian(columnValue);
                    } else if (columnID == FIELD_POSITION_UZ) {
                        referenceLocale.setUzbek(columnValue);
                    } else {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = false;
                    }
                } else if (columnID == FIELD_POSITION_LOCATION) {
                    if (!"".equals(columnValue)) {
                        Integer locationID = locationIDs.get(columnValue.toLowerCase());
                        depByLocationId = locationID;
                        if (locationID != null) {
                            item.setLocation(new SelectItem(locationID));
                        } else {
                            rejectedRow[FIELD_POSITION_LOCATION].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    } else {
                        isValid = true;
                    }
                } else if (columnID == FIELD_POSITION_DEPARTMENT) {
                    if (!"".equals(columnValue)) {
                        Integer departmentId = key.get(columnValue.toLowerCase());
                        Integer locationByDepID = lOckey.get(columnValue.toLowerCase());
                        if (departmentId != null) {
                            if (locationByDepID != null && depByLocationId != null) {
                                if (locationByDepID.equals(depByLocationId)) {
                                    item.setDepartment(new SelectItem(departmentId));
                                } else {
                                    rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                                    isValid = false;
                                }
                            }
                        } else {
                            rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    }
                } else if (columnID == FIELD_POSITION_PLACE_COUNT) {
                    try {
                        if ("".equals(columnValue)) {
                            isValid = true;
                        } else {
                            item.setCount(String.valueOf(Integer.valueOf(columnValue)));
                        }
                    } catch (Exception e) {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = false;
                    }
                } else if (columnID == FIELD_POSITION_TYPE) {
                    if (!"".equals(columnValue)) {
                        EdsReference reference = getReference(positionTypeIds, columnValue.toLowerCase());
                        if (reference != null) {
                            item.setType(reference.getAsSelectItem());
                        } else {
                            rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                            isValid = false;
                        }
                    }
                } else if (columnID == FIELD_POSITION_COEFFICENT) {
                    try {
                        if ("".equals(columnValue)) {
                            isValid = true;
                        } else {
                            item.setCoefficent(Double.valueOf(columnValue));
                        }
                    } catch (Exception e) {
                        rejectedRow[columnID].setErrorComment(commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        isValid = false;
                    }
                } else if (importFile.getExtraColumns() != null && importFile.getExtraColumns().size() > 0) {
                    for (Map.Entry<Integer, String> extraColumnEntry : importFile.getExtraColumns().entrySet()) {
                        if (!rejectedRecords.isEmpty()) {
                            if (columnID != importFile.getExtraColumnID(extraColumnEntry.getValue()) || extraColumnEntry.getKey() < ImportField.PositionFields.FIELD_CUSTOM_FIELD_START_NUMBER) {
                                continue;
                            }
                            String fieldName = rejectedRecords.size() > 0 && rejectedRecords.get(0).length > columnID ? rejectedRecords.get(0)[columnID].getData() : "";
                            CompanyCustomFieldItem customField = commonServiceLocal.getValidCustomFieldItem(extraColumnEntry, columnID, columnValue, rejectedRow, fieldName);
                            if (customField == null) {
                                isValid = false;
                                continue;
                            }
                            customFields.add(customField);
                        }
                    }
                }
                columnID++;
            }

            if (!customFields.isEmpty()) {
                item.setCustomFieldItems(customFields);
            }
            if (isValid) {
                item.setLocaleItem(referenceLocale);
                Integer result = hrmsService.savePosition(item);
                if (result < 0) {
                    if (importFile.isMerge()) {
                        EdsPosition existing = positionManager.getPositionByCode(item.getNumberData().getNumberString(), null);
                        item.setObjectID(existing.getObjectID());
                        hrmsService.savePosition(item);
                        overwrittenRows++;
                        importFile.setClonedColumns(overwrittenRows);
                        importFile.setOverwrittenColumns(overwrittenRows);
                    } else if (importFile.isClone()) {
                        item.setNumberData(hrmsService.generatePositionNumber());
                        hrmsService.savePosition(item);
                        impRows++;
                    } else {
                        skippedRows++;
                        importFile.setSkippedColumns(skippedRows);
                        log.info("=== Position with row: " + skippedRows + " skipped! ===");
                    }
                } else {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                    log.info("=== Position saved with id: " + result + " successfully! ===");
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
                log.info("=== Position with row: " + ignoredRows + " rejected ===");
            }
        }
        return rejectedRecords;
    }

    private BigDecimal parseBigDecimal(String columnValue) {
        try {
            return new BigDecimal(columnValue.trim().replace(",", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private HashMap<String, EdsCurrency> getCurrencyMap() {
        HashMap<String, EdsCurrency> map = new HashMap<>();
        for (EdsCurrency currency : exchangeCurrencyManager.getCurrencyList()) {
            map.put(currency.getName(), currency);
            map.put(currency.getFullName(), currency);
            map.put(currency.getSymbol(), currency);
        }
        return map;
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importEmployeeLeaveAllowances(ImportFile importFile, List<String[]> data) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer FIELD_PINFL = importFile.getColumnID(ImportField.EmployeeLeaveAllowanceFields.FIELD_PINFL);
        Integer FIELD_START_DATE = importFile.getColumnID(ImportField.EmployeeLeaveAllowanceFields.FIELD_START_DATE);
        Integer FIELD_ALLOWANCE = importFile.getColumnID(ImportField.EmployeeLeaveAllowanceFields.FIELD_ALLOWANCE);
        Integer FIELD_LEFT = importFile.getColumnID(ImportField.EmployeeLeaveAllowanceFields.FIELD_LEFT);

        boolean hasHeader = importFile.isHasHeader();

        int impRows = 0;
        int ignoredRows = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);
        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length + 1];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            ProfileItem item = new ProfileItem();
            int columnID = 0;
            EdsUser user = userManager.getUser();
            for (String columnValue : row) {
                if (columnID == FIELD_PINFL) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords));
                        isValid = false;
                    } else {
                        item.setPinfl(Long.valueOf(columnValue));
                    }
                } else if (columnID == FIELD_START_DATE) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords));
                        isValid = false;
                    } else {
                        Date startDate;
                        try {
                            startDate = dateFormat.parse(columnValue);
                            item.setStartDate(dateFormat.format(startDate));
                        } catch (ParseException e) {
                            rejectedRow[columnID].setErrorComment("Can not be parsed. Not a valid date");
                            isValid = false;
                        }
                    }
                } else if (columnID == FIELD_ALLOWANCE) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords));
                        isValid = false;
                    } else {
                        item.setAllowance(Double.valueOf(columnValue));
                    }
                } else if (columnID == FIELD_LEFT) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords));
                        isValid = false;
                    } else {
                        item.setLeftLeaveDays(Double.valueOf(columnValue));
                    }
                }
                columnID++;
            }
            if (isValid) {
                Integer result = hrmsServiceLocal.saveEmployeeLeaveAllowance(item, user);
                if (result == null) {
                    rejectedRow[1].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, rejectedRecords));
                    rejectedRecords.add(rejectedRow);
                    ignoredRows++;
                    importFile.setIgnoredColumns(ignoredRows);
                    log.info("=== ERROR: employee or period not found with the given data ===");
                } else {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                    log.info("=== period allowances saved with id successfully! ===");
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
            }
        }
        return rejectedRecords;
    }
}
