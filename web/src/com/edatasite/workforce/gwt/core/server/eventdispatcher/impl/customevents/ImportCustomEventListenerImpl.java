package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import au.com.bytecode.opencsv.CSVReader;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ImportingServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.commons.RejectedImportRecordsExcelHandler;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.server.app.CustomInvoiceImportService;
import com.edatasite.workforce.gwt.invoice.server.app.NimbleService;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SalesInvoiceAddTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BATCH_LIMIT;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Aug 17, 2010
 * Time: 4:26:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ImportCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsImportFile> TYPE = new WfmType<>(EventTypes.importFileCustomEventListener);
    public static final String EVENT_IMPORT_LEAD = "IMPORT_LEAD";
    public static final String EVENT_IMPORT_CANDIDATE = "IMPORT_CANDIDATE";
    public static final String EVENT_IMPORT_CONTACT = "IMPORT_CONTACT";
    public static final String EVENT_IMPORT_CRMACCOUNT = "IMPORT_CRMACCOUNT";
    public static final String EVENT_IMPORT_CLIENT = "IMPORT_CLIENT";
    public static final String EVENT_IMPORT_SUPPLIER = "IMPORT_SUPPLIER";
    public static final String EVENT_IMPORT_PRODUCT = "IMPORT_PRODUCT";
    public static final String EVENT_IMPORT_PRODUCT_FROM_PARENT = "IMPORT_PRODUCT_FROM_PARENT";
    public static final String EVENT_IMPORT_NIMBLE_COMMERCE = "IMPORT_NIMBLE_COMMERCE";
    public static final String EVENT_IMPORT_CUSTOM_INVOICE = "IMPORT_CUSTOM_INVOICE";
    public static final String EVENT_IMPORT_CHART_OF_ACCOUNTS = "EVENT_IMPORT_CHART_OF_ACCOUNTS";
    public static final String EVENT_IMPORT_OPPORTUNITY = "EVENT_IMPORT_OPPORTUNITY";
    public static final String EVENT_IMPORT_LEAD_END = "IMPORT_LEAD_END";
    public static final String EVENT_SOLR_IMPORTED_CONTACT = "ADD_TO_SOLR_IMPORTED_CONTACTS";
    public static final String EVENT_SOLR_IMPORTED_CANDIDATE = "ADD_TO_SOLR_IMPORTED_CANDIDATES";
    public static final String EVENT_SOLR_SYNC_CONTACT = "ADD_TO_SOLR_SYNC_CONTACTS";
    public static final String EVENT_SOLR_IMPORTED_CRM_ACCOUNT = "ADD_TO_SOLR_IMPORTED_CRM_ACCOUNTS";
    public static final String EVENT_SOLR_IMPORTED_OPPORTUNITY = "ADD_TO_SOLR_IMPORTED_OPPORTUNITY";
    public static final String EVENT_IMPORT_EMPLOYEE = "EVENT_IMPORT_EMPLOYEE";
    public static final String EVENT_IMPORT_EXPENSE = "EVENT_IMPORT_EXPENSE";
    public static final String EVENT_IMPORT_COMPANY_EXPENSE = "EVENT_IMPORT_COMPANY_EXPENSE";
    public static final String EVENT_IMPORT_PROJECT = "EVENT_IMPORT_PROJECT";
    public static final String EVENT_IMPORT_VCARD_CONTACT = "EVENT_IMPORT_VCARD_CONTACT";
    public static final String EVENT_IMPORT_MANUAL_TRANSACTION = "EVENT_IMPORT_MANUAL_TRANSACTION";
    public static final String EVENT_IMPORT_MANUAL_TRANSACTION_TALLY = "EVENT_IMPORT_MANUAL_TRANSACTION_TALLY";
    public static final String EVENT_IMPORT_ADDITIONAL_PAYMENT = "EVENT_IMPORT_ADDITIONAL_PAYMENT";
    public static final String EVENT_IMPORT_BANK_TRANSFER_TRANSACTION = "EVENT_IMPORT_BANK_TRANSFER_TRANSACTION";
    public static final String EVENT_IMPORT_BUDGET_MANAGER = "EVENT_IMPORT_BANK_TRANSFER";
    public static final String EVENT_IMPORT_LOCALIZATION_PROPERTY = "EVENT_IMPORT_LOCALIZATION_PROPERTY";
    public static final String EVENT_IMPORT_BATCH_INVOICE = "EVENT_IMPORT_BATCH_INVOICE";
    public static final String EVENT_IMPORT_BATCH_SALES_ORDER = "EVENT_IMPORT_BATCH_SALES_ORDER";
    public static final String EVENT_IMPORT_BATCH_INVOICE_WITHOUT_PAYMENT = "EVENT_IMPORT_BATCH_INVOICE_WITHOUT_PAYMENT";
    public static final String EVENT_IMPORT_BATCH_INVOICE_PAYMENT = "EVENT_IMPORT_BATCH_INVOICE_PAYMENT";
    public static final String EVENT_IMPORT_REPORT_DATA = "EVENT_IMPORT_REPORT_DATA";
    public static final String EVENT_IMPORT_GROUP_PAYRUN = "EVENT_IMPORT_GROUP_PAYRUN";
    public static final String EVENT_IMPORT_PAYMENT = "EVENT_IMPORT_PAYMENT";
    public static final String EVENT_IMPORT_DEDUCTION = "EVENT_IMPORT_DEDUCTION";
    public static final String EVENT_IMPORT_PRODUCT_CATEGORIES = "EVENT_PRODUCT_CATEGORIES";
    public static final String EVENT_IMPORT_BRAND = "EVENT_BRAND";
    public static final String EVENT_IMPORT_PURCHASE_ORDER = "EVENT_IMPORT_PURCHASE_ORDER";
    public static final String EVENT_IMPORT_EMPLOYEE_LEAVE_ALLOWANCE = "EVENT_IMPORT_EMPLOYEE_LEAVE_ALLOWANCE";
    public static final String EVENT_IMPORT_POSITION = "EVENT_IMPORT_POSITION";
    public static final String EVENT_IMPORT_DEPARTMENT = "EVENT_IMPORT_DEPARTMENT";

    @Autowired
    private ImportingServiceLocal importingServiceLocal;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    @Qualifier("accountingService")
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private NimbleService nimbleService;
    @Autowired
    private CustomInvoiceImportService customInvoiceImportService;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private CoreServiceLocal coreServiceLocal;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;

    private Integer mailingListId;
    private static Logger log = LoggerFactory.getLogger(ImportCustomEventListenerImpl.class);

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (event.getAdditionalSourceID() != null && !event.getAdditionalSourceID().equals(0)) {
            this.mailingListId = event.getAdditionalSourceID();
        }
        switch (event.getEventType()) {
            case EVENT_IMPORT_LEAD:
            case EVENT_IMPORT_CANDIDATE:
            case EVENT_IMPORT_CONTACT:
                try {
                    onContactImport(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_IMPORT_CRMACCOUNT:
            case EVENT_IMPORT_CLIENT:
            case EVENT_IMPORT_SUPPLIER:
                try {
                    onCrmAccountImport(event, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_IMPORT_LEAD_END:
            case EVENT_SOLR_IMPORTED_CONTACT:
            case EVENT_SOLR_IMPORTED_CANDIDATE:
                addImportedContactsToSolr(event, mailingListId);
                if (this.mailingListId != null) {
                    this.mailingListId = null;
                }
                break;
            case EVENT_SOLR_SYNC_CONTACT:
                addSyncContactsToSolr(event);
                break;
            case EVENT_SOLR_IMPORTED_CRM_ACCOUNT:
                addImportedCrmAccountsToSolr(event, true);
                break;
            case EVENT_SOLR_IMPORTED_OPPORTUNITY:
                addImportedOpportunitiesToSolr(event);
                break;
            case EVENT_IMPORT_PRODUCT:
                try {
                    onProductImport(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_IMPORT_PRODUCT_FROM_PARENT:
                onProductImportFromParent(event);
                break;
            case EVENT_IMPORT_NIMBLE_COMMERCE:
                onNimbleCommerceImport(event);
                break;
            case EVENT_IMPORT_CUSTOM_INVOICE:
                onCustomInvoiceImport(event);
                break;
            case EVENT_IMPORT_CHART_OF_ACCOUNTS:
                onChartOfAccountsImport(event);
                break;
            case EVENT_IMPORT_OPPORTUNITY:
                onOpportunityImport(event);
                break;
            case EVENT_IMPORT_EMPLOYEE:
                onEmployeeImport(event);
                break;
            case EVENT_IMPORT_EXPENSE:
            case EVENT_IMPORT_COMPANY_EXPENSE:
                onExpenseImport(event);
                break;
            case EVENT_IMPORT_PROJECT:
                onProjectImport(event);
                break;
            case EVENT_IMPORT_VCARD_CONTACT:
                onVCardContactImport(event);
                break;
            case EVENT_IMPORT_MANUAL_TRANSACTION:
                onManualTransactionImport(event);
                break;
            case EVENT_IMPORT_MANUAL_TRANSACTION_TALLY:
                onManualTransactionTallyImport(event);
                break;
            case EVENT_IMPORT_ADDITIONAL_PAYMENT:
                onAdditionalPaymentImport(event);
                break;
            case EVENT_IMPORT_BANK_TRANSFER_TRANSACTION:
                onBankTransferImport(event);//Cash Bank Receipt/Payments

                break;
            case EVENT_IMPORT_LOCALIZATION_PROPERTY:
                onLocalizationPropertyImport(event);
                break;
            case EVENT_IMPORT_BATCH_INVOICE:
                onBatchInvoiceImport(event);
                break;
            case EVENT_IMPORT_BATCH_INVOICE_WITHOUT_PAYMENT:
                onBatchInvoiceImportWithoutPayment(event);
                break;
            case EVENT_IMPORT_BATCH_SALES_ORDER:
                onBatchSalesOrderImport(event);
                break;
            case EVENT_IMPORT_BATCH_INVOICE_PAYMENT:
                onBatchInvoicePaymentImport(event);
                break;
            case EVENT_IMPORT_REPORT_DATA:
                onReportDataImport(event);
                break;
            case EVENT_IMPORT_GROUP_PAYRUN:
                onGroupPayrunImport(event);
                break;
            case EVENT_IMPORT_PAYMENT:
            case EVENT_IMPORT_DEDUCTION:
                onPaymentDeductionImport(event);
                break;
            case EVENT_IMPORT_PRODUCT_CATEGORIES:
                onProductCategoriesImport(event);
                break;
            case EVENT_IMPORT_BUDGET_MANAGER:
                try {
                    onBudgetItemDataImport(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case EVENT_IMPORT_BRAND:
                try {
                    onBrandImport(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_IMPORT_PURCHASE_ORDER:
                onPurchaseOrderImport(event);
                break;
            case EVENT_IMPORT_EMPLOYEE_LEAVE_ALLOWANCE:
                onEmployeeLeaveAllowanceImport(event);
                break;
            case EVENT_IMPORT_POSITION:
                try {
                    onPositonImport(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EVENT_IMPORT_DEPARTMENT:
                try {
                    onDepartmentImport(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void onReportDataImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started import report data from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        InputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();

        if (attachment.getOriginalName().toLowerCase().contains(".xls") || attachment.getOriginalName().toLowerCase().contains(".xlsx")) {
            rejectedRecords.addAll(coreServiceLocal.importReportDataExcel(attachment, importFile, inputStream));
        } else {
            InputStreamReader isr;
            try {
                isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            } catch (Exception e) {
                isr = new InputStreamReader(inputStream);
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
                rejectedRecords.addAll(coreServiceLocal.importReportDataFromCSV(attachment, importFile, reader.readAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        edsImportFile.setNewColumns(importFile.getNewColumns());
        edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
        edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
        edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
        edsImportFile.setImportedColumns(importFile.getNewColumns());
        edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));
        edsImportFile.setCommitted(true);
        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);
        event.setStatus(EventStatus.COMPLETED.name());
        if (!rejectedRecords.isEmpty()) {
            RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
            initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Report_Data"), "Rejected_Report_Data.xls");
        }
        System.out.println("Finished import report data " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        SecurityContext.getInstance().setStaticUserID(null);
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
    }

    private void onBatchSalesOrderImport(EdsBusinessEvent event) {
        ServerSecurityContext.getInstance().setStaticUserID(event.getSourceID());
        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started sales order batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        EdsFileHeader fileHeader = fileHeaderManager.get(edsImportFile.getFileID());
        EdsFileBody fileBody = fileHeader.getBodies().get(0);
        ImportFile importFile = edsImportFile.getRPC();
        InputStream inputStream = uploadManager.getInputStream(fileBody);

        BufferedReader isr = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        LinkedList<RejectedImportRecord[]> rejectedRecords = new LinkedList<>();
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(importingServiceLocal.importSalesOrder(importFile, listOfRows));

            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            edsImportFile.setCommitted(true);
            //Set Total number of created Opportunities
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of REJECTED Opportunities
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Sales_Orders"), "Rejected_Sales_Orders_Import_Records.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("Finished sales order batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onBatchInvoiceImportWithoutPayment(EdsBusinessEvent event) {
        ServerSecurityContext.getInstance().setStaticUserID(event.getSourceID());
        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started invoice batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        EdsFileHeader fileHeader = fileHeaderManager.get(edsImportFile.getFileID());
        EdsFileBody fileBody = fileHeader.getBodies().get(0);
        ImportFile importFile = edsImportFile.getRPC();
        InputStream inputStream = uploadManager.getInputStream(fileBody);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
            exception = e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LinkedList<RejectedImportRecord[]> rejectedRecords = new LinkedList<>();
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(importingServiceLocal.importBatchInvoiceWithoutPayment(importFile, listOfRows));

            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            edsImportFile.setCommitted(true);
            //Set Total number of created Opportunities
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of REJECTED Opportunities
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Invoices"), "Rejected_Invoice_Import_Records.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("Finished invoice batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onBatchInvoicePaymentImport(EdsBusinessEvent event) {
        ServerSecurityContext.getInstance().setStaticUserID(event.getSourceID());
        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started invoice payment batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        EdsFileHeader fileHeader = fileHeaderManager.get(edsImportFile.getFileID());
        EdsFileBody fileBody = fileHeader.getBodies().get(0);
        ImportFile importFile = edsImportFile.getRPC();
        InputStream inputStream = uploadManager.getInputStream(fileBody);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
            exception = e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LinkedList<RejectedImportRecord[]> rejectedRecords = new LinkedList<>();
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(importingServiceLocal.importBatchInvoicePayment(importFile, listOfRows));

            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            edsImportFile.setCommitted(true);
            //Set Total number of created Opportunities
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of REJECTED Opportunities
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Invoice_Payments"), "Rejected_Invoice_Payments_Import_Records.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("Finished invoice payment batch import from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onBatchInvoiceImport(EdsBusinessEvent event) {
        ServerSecurityContext.getInstance().setStaticUserID(event.getSourceID());
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Batch Invoice import started " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        EdsFileHeader fileHeader = fileHeaderManager.get(edsImportFile.getFileID());
        EdsFileBody fileBody = fileHeader.getBodies().get(0);
        StringBuilder log = new StringBuilder();
        try {
            InputStream is = uploadManager.getInputStream(fileBody);
            String jsonString = IOUtils.toString(is);
            if (StringUtils.isEmpty(jsonString)) {
                log.append("Your file is empty.");
            } else {
                try {
                    SalesInvoiceAddTO[] items = new Gson().fromJson(jsonString, SalesInvoiceAddTO[].class);
                    log.append(importingServiceLocal.batchImportInvoices(items, edsImportFile));
                    edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
                } catch (Exception e) {
                    edsImportFile.setStatus(ImportStatusEnum.FAILED);
                    edsImportFile.setException(e.getMessage());
                    e.printStackTrace();
                    log.append("Error occured while parsing to Json : \n");
                    log.append(e.getMessage());
                }
            }
        } catch (Exception e) {
            edsImportFile.setStatus(ImportStatusEnum.FAILED);
            edsImportFile.setException(e.getMessage());
            e.printStackTrace();
            log.append("Error occured while parsing file content : \n");
            log.append(e.getMessage());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(log.toString().getBytes());
        System.out.print("*****************Upload to Amazon S3 server****************");
        EdsUpload upload = new EdsUpload();
        upload.setContentType("text/plain");
        upload.setOriginalName("Import Log.txt");
        upload.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(bais);
        try {
            uploadManager.create(upload);
            edsImportFile.setRejectedRecords(upload);
            System.out.print("****************File Uploaded******************");
        } catch (Exception ex) {
            System.err.println("****************Failed to Upload File******************");
        }
        try {
            bais.close();
        } catch (IOException ex) {
            System.err.println("Unable to close stream");
        }
        event.setStatus(EventStatus.COMPLETED.name());
        importFileManager.update(edsImportFile);
        System.out.println("Batch invoice import ended");
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onProjectImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing project" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(projectServiceLocal.importProjects(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT)));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Projects"), "Rejected_Projects.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import project " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        messageManager.sendImportReportMessage(edsImportFile.getObjectID());

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onGroupPayrunImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing project" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(payrollServiceLocal.importGroupPayrun(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT)));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Payruns"), "Rejected_Payruns.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import group payrun " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onProductCategoriesImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing project" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importProductCategories(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size()))));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Product_Categories"), "Rejected_Product_Categories.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Product Categories" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        messageManager.sendImportReportMessage(edsImportFile.getObjectID());

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onPaymentDeductionImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing project" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(payrollServiceLocal.importPaymentDeduction(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size())), event.getEventType()));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Categories"), "Rejected_Categories.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import " + (event.getEventType().equals(EVENT_IMPORT_PAYMENT) ? " payment " : " deduction ") + "categories" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onLocalizationPropertyImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Localization property import started " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr;
        String charsetName = "utf8";
        if ("ru".equalsIgnoreCase(edsImportFile.getCategoryColumns())) {
            charsetName = "windows-1251";
        } else if ("ar".equalsIgnoreCase(edsImportFile.getCategoryColumns())) {
            charsetName = "utf16";
        }
        try {
            isr = new InputStreamReader(inputStream, Charset.forName(charsetName));
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }

        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            HashMap<String, String> map = new HashMap<>();
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                List<String[]> list = listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT);
                for (String[] l : list) {
                    if (l.length > 1 && l[0] != null && !"".equalsIgnoreCase(l[0].trim()) && l[1] != null && !"".equalsIgnoreCase(l[1].trim())) {
                        map.put(l[0], l[1]);
                    }
                }
            }
            localizationService.update(edsImportFile.getViewType(), edsImportFile.getCategoryColumns(), null, map, new LinkedHashMap<>(), true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
        edsImportFile.setCommitted(true);
        importFileManager.update(edsImportFile);

        System.out.println("Localization property import process ended");
    }

    private void onExpenseImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started import expense from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        InputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importExpense(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT), event.getCompanyId(), event.getSourceID()));
                importFile.setHasHeader(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setNewColumns(importFile.getNewColumns());
        edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
        edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
        edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
        edsImportFile.setImportedColumns(importFile.getNewColumns());
        edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));
        edsImportFile.setCommitted(true);
        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);
        event.setStatus(EventStatus.COMPLETED.name());
        if (!rejectedRecords.isEmpty()) {
            RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
            initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Expenses"), "Rejected_Expense_Records.xls");
        }
        System.out.println("Finished import expense " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        SecurityContext.getInstance().setStaticUserID(null);
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
    }

    private void onBankTransferImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : event.getSourceID());
        String viewType = edsImportFile.getViewType();
        System.out.println("Started import " + viewType + " from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        InputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(accountingServiceLocal.importBankTransfer(importFile, listOfRows, event.getCompanyId(), event.getSourceID()));
            importFile.setHasHeader(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setNewColumns(importFile.getNewColumns());
        edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
        edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
        edsImportFile.setCsvColumns(importFile.getNewColumns() + importFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));
        edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
        edsImportFile.setImportedColumns(importFile.getNewColumns() + importFile.getOverwrittenColumns());
        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        edsImportFile.setType("SPEND_MONEY".equals(edsImportFile.getViewType()) ? ImportTypeEnum.BANK_PAYMENT :
                "RECEIVE_MONEY".equals(edsImportFile.getViewType()) ? ImportTypeEnum.BANK_RECEIPT :
                        "CASH_PAYMENT".equals(edsImportFile.getViewType()) ? ImportTypeEnum.CASH_PAYMENT : ImportTypeEnum.CASH_RECEIPT);
        edsImportFile.setCommitted(true);
        importFileManager.update(edsImportFile);
        event.setStatus(EventStatus.COMPLETED.name());
        if (!rejectedRecords.isEmpty()) {
            RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
            initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Bank_Transfers"), "Rejected_Bank_Transfer_Records.xls");
        }

        System.out.println("Finished import " + viewType + " " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);

    }

    private void onEmployeeImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing employee" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importEmployees(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT), CrmConstants.EMPLOYEE, event.getCompanyId(), event.getSourceID()));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Employees"), "Rejected_Employees.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());
        log.info("Finished import employee " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        List<Integer> employeeIds = employeeManager.getByImportFileID(edsImportFile.getObjectID()); //getting imported employees ids
        importFile.setImportedColumns(employeeIds.size());
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());

        EdsBusinessEvent edsBusinessEvent = baseEventPostProcessor.registerEvent(ImportEmployeeAttandanceEventListenerImpl.TYPE, ImportEmployeeAttandanceEventListenerImpl.EVENT_ATTANDANCE_ADD_TO_DATABASE, null, userManager.getUser());
        edsBusinessEvent.setCustomStringField(StringUtils.join(employeeIds, ","));
        edsBusinessEvent.setCompanyId(Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        event.setStatus(EventStatus.COMPLETED.name());

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private ImportStatusEnum onCrmAccountImport(EdsBusinessEvent event, String from) throws IOException {

        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Start onCrmAccountImport id=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        InputStream inputStream = uploadManager.getInputStream(attachment);
        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        List<String[]> dataBank = new ArrayList<>();
        try {
            dataBank = reader.readAll();
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            reader.close();
            if (inputStream != null) {
                inputStream.close();
            }
        }
        String type = EVENT_IMPORT_SUPPLIER.equals(event.getEventType()) ? EdsCrmAccount.SUPPLIER : EVENT_IMPORT_CLIENT.equals(event.getEventType()) ? EdsCrmAccount.CUSTOMER : "";
        try {
            ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
            for (int i = 0; i < Math.ceil((double) dataBank.size() / (double) BATCH_LIMIT); i++) {
                log.info("importAccounts i=" + i + ", dataBank.size=" + dataBank.size() + ", from=" + (i * BATCH_LIMIT) + " to =" + (i * BATCH_LIMIT + BATCH_LIMIT > dataBank.size() ? dataBank.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                rejectedRecords.addAll(crmServiceLocal.importAccounts(importFile, dataBank.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > dataBank.size() ? dataBank.size() : i * BATCH_LIMIT + BATCH_LIMIT), from));
                importFile.setHasHeader(false);
            }
            if ("".equals(from)) {
                if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                    String sheetName = "Rejected_";
                    sheetName += EVENT_IMPORT_SUPPLIER.equals(event.getEventType()) ? "Suppliers" : EVENT_IMPORT_CLIENT.equals(event.getEventType()) ? "Clients" : "Accounts";
                    RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();

                    initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, sheetName), sheetName + ".xls");
                }
                edsImportFile.setCsvColumns(dataBank.size());
                edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
                edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
                edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
                edsImportFile.setClonedColumns(importFile.getClonedColumns());
                edsImportFile.setCommitted(true);
                EdsBusinessEvent process = baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_SOLR_IMPORTED_CRM_ACCOUNT, edsImportFile, edsImportFile.getOwner());
                process.setCustomStringField(type);
            }
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                edsImportFile.setException(exception.getMessage() != null ? exception.getMessage() : "");
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        return edsImportFile.getStatus();
    }

    private void onBudgetItemDataImport(EdsBusinessEvent event) throws IOException {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Import Budget Manager  started: event.id=" + event.getObjectID() + ", event.entityid=" + event.getEntityID() + ", importfile.fileid=" + edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        accountingService.onBudgetManagerImport(edsImportFile.getRPC());
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("End Of Import Budget Manager ");
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onProductImport(EdsBusinessEvent event) throws IOException {
        int PRODUCT_BATCH_LIMIT = 100;

        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Start onProductImport id=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);


        BufferedReader isr = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        List<String[]> listOfRows = new ArrayList<>();
        try {
            listOfRows = reader.readAll();
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            reader.close();
            if (inputStream != null) {
                inputStream.close();
            }
        }

        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) PRODUCT_BATCH_LIMIT); i++) {
                System.out.println("Import Start: " + i * PRODUCT_BATCH_LIMIT);
                rejectedRecords.addAll(importingServiceLocal.importProducts(importFile, listOfRows.subList(i * PRODUCT_BATCH_LIMIT, i * PRODUCT_BATCH_LIMIT + PRODUCT_BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * PRODUCT_BATCH_LIMIT + PRODUCT_BATCH_LIMIT)));
                importFile.setHasHeader(false);
            }

            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            edsImportFile.setCommitted(true);
            //Set Total number of created Products
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of overwritten Products
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            //Set Total number of skipped Products
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            //Set Total number of REJECTED Products
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() +
                    edsImportFile.getIgnoredColumns() +
                    edsImportFile.getOverwrittenColumns() +
                    edsImportFile.getSkippedColumns() +
                    (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Products"), "Rejected_Product_Import_Records.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("End ProductImport");
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onProductImportFromParent(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("PRODUCT IMPORT FROM PARENT IS STARTED");
        ImportFile importFile = edsImportFile.getRPC();
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        accountingService.importProductsFromParentCompany(importFile);
        log.info("PRODUCT IMPORT FROM PARENT IS FINISHED");
        event.setStatus(EventStatus.COMPLETED.name());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onNimbleCommerceImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("NIMBLE COMMERCE IMPORT STARTED  id=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }

        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                nimbleService.importNimbleCommerceData(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                importFile.setHasHeader(false);
            }
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setStatus(EventStatus.COMPLETED.name());
        try {
            attachmentManager.delete(attachment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("NIMBLE COMMERCE IMPORT FINISHED");
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onCustomInvoiceImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("CUSTOM INVOICE IMPORT STARTED id=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }

        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        StringBuilder notImportedInvoices = new StringBuilder();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                String notImportedInvoice = customInvoiceImportService.importCustomInvoices(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                notImportedInvoices.append(i + "-Part \n" + notImportedInvoice);
                importFile.setHasHeader(false);
            }
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setStatus(EventStatus.COMPLETED.name());
        try {
            attachmentManager.delete(attachment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("CUSTOM INVOICE IMPORT FINISHED");
        SecurityContext.getInstance().setStaticUserID(null);

        if (!notImportedInvoices.toString().isEmpty()) {
            messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        }
    }

    private void onOpportunityImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Start onOpportunityImport id=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        if (edsImportFile.getNextSteps() != null && edsImportFile.getNextSteps().contains(EVENT_IMPORT_CRMACCOUNT)) {
            log.info("Accounts importing..." + event.getObjectID());
            ImportStatusEnum status = null;
            try {
                status = onCrmAccountImport(event, EVENT_IMPORT_OPPORTUNITY);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            edsImportFile.setNextSteps(edsImportFile.getNextSteps().replace(EVENT_IMPORT_CRMACCOUNT, ""));
            importFileManager.update(edsImportFile);
            if (!ImportStatusEnum.FAILED.equals(status)) {
                baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, event.getEventType(), edsImportFile, userManager.getUser());
            }
        } else {
            try {
                EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
                SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
                InputStream inputStream = uploadManager.getInputStream(attachment);

                InputStreamReader isr = getInputStreamReader(inputStream);
                CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
                ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();

                //Read all rows from CSV File which being imported
                List<String[]> listOfRows = reader.readAll();
                //Process 1000 rows at a time
                for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                    rejectedRecords.addAll(importingServiceLocal.importOpportunities(importFile, listOfRows.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > listOfRows.size() ? listOfRows.size() : i * BATCH_LIMIT + BATCH_LIMIT)));
                    importFile.setHasHeader(false);
                }
                edsImportFile = importFileManager.get(edsImportFile.getObjectID());
                edsImportFile.setCommitted(true);
                //Set Total number of created Opportunities
                edsImportFile.setImportedColumns(importFile.getImportedColumns());
                //Set Total number of REJECTED Opportunities
                edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
                //Set Total non empty rows which were processed
                edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

                if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                    RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                    initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Opportunities"), "Rejected_Opportunities.xls");
                }
                //Set Status as COMPLETED
                edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
                //Update import file which will displayed in the UI (Settings->System Logs)
                importFileManager.update(edsImportFile);
                messageManager.sendImportReportMessage(importFile.getObjectID());
            } catch (Exception e) {
                log.error("", e);

                edsImportFile = importFileManager.get(importFile.getObjectID());
                edsImportFile.setException(e.getMessage() != null ? e.getMessage() : "");
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
                importFileManager.update(edsImportFile);
            }
            event.setStatus(EventStatus.COMPLETED.name());
            baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_SOLR_IMPORTED_OPPORTUNITY, edsImportFile, edsImportFile.getOwner());
            log.info("End OpportunityImport");
            SecurityContext.getInstance().setStaticUserID(null);
        }
    }

    private void onChartOfAccountsImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Import ChartOfAccounts started: event.id=" + event.getObjectID() + ", event.entityid=" + event.getEntityID() + ", importfile.fileid=" + edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        accountingService.onChartOfAccountsImport(edsImportFile.getRPC(), false);
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("End Of Import ChartOfAccounts");
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onContactImport(EdsBusinessEvent event) throws IOException {
        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile != null && edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        System.out.println("Boshlandi contactImport" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + ":::" + new Date().toString());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        InputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);
        InputStreamReader isr = getInputStreamReader(inputStream);
        CsvPreference preference = CsvPreference.STANDARD_PREFERENCE;
        if (importFile.getDefaultSeparator() == ';') {
            preference = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;
        } else if (importFile.getDefaultSeparator() == '\t') {
            preference = CsvPreference.TAB_PREFERENCE;
        }
        ICsvListReader listReader = new CsvListReader(isr, preference);
        List<List<String>> dataBank = new ArrayList<>();
        try {
            List<String> read = null;
            while ((read = listReader.read()) != null) {
                dataBank.add(read);
            }
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        } finally {
            listReader.close();
            if (inputStream != null) {
                inputStream.close();
            }
        }
        boolean isLeadImport = EVENT_IMPORT_LEAD.equals(event.getEventType());
        boolean isCandidateImport = EVENT_IMPORT_CANDIDATE.equals(event.getEventType());
        try {
            if (edsImportFile.getNextSteps() != null && edsImportFile.getNextSteps().contains(EVENT_IMPORT_CRMACCOUNT)) {
                System.out.println("Accounts importing..." + event.getObjectID());
                onCrmAccountImport(event, EVENT_IMPORT_CONTACT);
                edsImportFile.setNextSteps(edsImportFile.getNextSteps().replace(EVENT_IMPORT_CRMACCOUNT, ""));
                importFileManager.update(edsImportFile);
                baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, event.getEventType(), edsImportFile, userManager.getUser());
            } else {
                System.out.println("Contacts importing..." + event.getObjectID());
                ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
                for (int i = 0; i < Math.ceil((double) dataBank.size() / (double) BATCH_LIMIT); i++) {
                    log.info("importContacts i=" + i + ", dataBank.size=" + dataBank.size() + ", from=" + (i * BATCH_LIMIT) + " to =" + (i * BATCH_LIMIT + BATCH_LIMIT > dataBank.size() ? dataBank.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                    rejectedRecords.addAll(crmServiceLocal.importContacts(importFile, dataBank.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > dataBank.size() ? dataBank.size() : i * BATCH_LIMIT + BATCH_LIMIT), isLeadImport ? ContactListItem.LEAD_CONTACT : (isCandidateImport ? ContactListItem.CANDIDATE : ContactListItem.CRM_CONTACT), mailingListId));
                    importFile.setHasHeader(false);
                }
                edsImportFile = importFileManager.get(importFile.getObjectID());
                if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                    String sheetName = "Rejected_";
                    sheetName += isLeadImport ? "Leads" : isCandidateImport ? "Candidates" : "Contacts";
                    RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                    initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, sheetName), sheetName + ".xls");
                }
                edsImportFile.setCsvColumns(dataBank.size());
                edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
                edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
                edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
                edsImportFile.setClonedColumns(importFile.getClonedColumns());
                edsImportFile.setCommitted(true);
                baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, isLeadImport ? ImportCustomEventListenerImpl.EVENT_IMPORT_LEAD_END : (isCandidateImport ? ImportCustomEventListenerImpl.EVENT_SOLR_IMPORTED_CANDIDATE : ImportCustomEventListenerImpl.EVENT_SOLR_IMPORTED_CONTACT), edsImportFile, edsImportFile.getOwner());
            }
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                edsImportFile.setException(exception.getMessage() != null ? exception.getMessage() : "");
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onVCardContactImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        if (edsImportFile == null) {
            return;
        }
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        log.info("Start vcard contact import eventId=" + event.getObjectID() + ", entityId=" + event.getEntityID() + ", fileId=" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(importFile.getFileID());
        InputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);
        List<VCard> vcards = new ArrayList<>();
        try {
            vcards = Ezvcard.parse(inputStream).all();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Integer> emailHasCodesInTheSystem = new HashSet<>();
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(edsImportFile.getOwner().getCompany().getObjectID());
        List<Integer> categoryIDs = ContactCategoryListItem.getIDs(contactCategoryServiceLocal.getContactCategories());
        Set<String> emailsInTheSystem = crmContactManager.getEmailSetOfSharedContacts(categoryIDs);
        emailsInTheSystem.addAll(crmContactManager.getEmailSetOfLeads());
        if (emailsInTheSystem.size() > 0) {
            for (String email : emailsInTheSystem) {
                if (email != null) {
                    emailHasCodesInTheSystem.add(email.hashCode());
                }
            }
        }
        edsImportFile.setCsvColumns(vcards.size());
        Exception exception = null;
        try {
            log.info("VCard Accounts importing..." + event.getObjectID());
            String type = EVENT_IMPORT_SUPPLIER.equals(event.getEventType()) ? EdsCrmAccount.SUPPLIER : EVENT_IMPORT_CLIENT.equals(event.getEventType()) ? EdsCrmAccount.CUSTOMER : "";
            for (int i = 0; i < Math.ceil((double) vcards.size() / (double) BATCH_LIMIT); i++) {
                log.info("VCard importAccounts i=" + i + ", vcards.size=" + vcards.size() + ", from=" + (i * BATCH_LIMIT) + " to =" + (i * BATCH_LIMIT + BATCH_LIMIT > vcards.size() ? vcards.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                crmServiceLocal.importVCardAccounts(importFile, vcards.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > vcards.size() ? vcards.size() : i * BATCH_LIMIT + BATCH_LIMIT), type, EVENT_IMPORT_CONTACT);
            }
            edsImportFile.setCommitted(true);
            edsImportFile.setImportedColumns(crmAccountManager.getAccountsCountByImportFileID(edsImportFile.getObjectID()));

            log.info("VCard Accounts importing done" + event.getObjectID());

            log.info("VCard Contacts importing..." + event.getObjectID());
            for (int i = 0; i < Math.ceil((double) vcards.size() / (double) BATCH_LIMIT); i++) {
                log.info("VCard import contacts i=" + i + ", vcards.size=" + vcards.size() + ", from=" + (i * BATCH_LIMIT) + " to =" + (i * BATCH_LIMIT + BATCH_LIMIT > vcards.size() ? vcards.size() : i * BATCH_LIMIT + BATCH_LIMIT));
                crmServiceLocal.importVCardContacts(importFile, vcards.subList(i * BATCH_LIMIT, i * BATCH_LIMIT + BATCH_LIMIT > vcards.size() ? vcards.size() : i * BATCH_LIMIT + BATCH_LIMIT), emailHasCodesInTheSystem, ContactListItem.CRM_CONTACT);
            }

            addImportedContactsToSolr(event, mailingListId);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
            }
            importFileManager.update(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private InputStreamReader getInputStreamReader(InputStream inputStream) {
        InputStreamReader isr;
        Charset charset = Charset.defaultCharset();
        if (inputStream instanceof FindEncodeInputStream) {
            charset = ((FindEncodeInputStream) inputStream).getCharset();
        }
        try {
            isr = new InputStreamReader(inputStream, charset);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }
        return isr;
    }

    private CellProcessor[] getProcessors(int length) {
        List<CellProcessor> results = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            results.add(new Optional());
        }
        return results.toArray(new CellProcessor[]{});
    }

    private void addImportedContactsToSolr(EdsBusinessEvent event, Integer mailingListId) {
        boolean isLead = EVENT_IMPORT_LEAD_END.equals(event.getEventType());
        boolean isCandidate = EVENT_SOLR_IMPORTED_CANDIDATE.equals(event.getEventType());
        EdsImportFile importFile = importFileManager.get(event.getEntityID());
        EdsMailList mailList = new EdsMailList();
        if (mailingListId != null) {
            mailList = mailListManager.get(mailingListId);
        }
        if (importFile != null && importFile.getCommitted() != null && importFile.getCommitted()) {
            log.info("Start addImportedContactsToSolr objectId=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + importFile.getFileID());
            int start = 0;
            int limit = 200;
            int membersCount = 0;
            boolean stop = false;
            int overAllCount = 0;
            do {
                List<Integer> ids = new ArrayList<>();
                crmContactManager.flushAndClear();
                List<EdsCrmContact> contacts = isLead ? crmContactManager.getLeadsByImportFileID(importFile.getObjectID(), start, limit) : (isCandidate ? crmContactManager.getCandidatesByImportFileID(importFile.getObjectID(), start, limit) : crmContactManager.getContactsByImportFileID(importFile.getObjectID(), start, limit));
                if (contacts.size() == 0) {
                    stop = true;
                }
                if (contacts.size() > 0) {
                    if (mailList != null && mailList.getObjectID() != null) {
                        for (EdsCrmContact contact : contacts) {
                            if (contact != null) {
                                ids.add(contact.getObjectID());
                            }
                        }
                    }
                    start = contacts.get(contacts.size() - 1).getObjectID();
                    overAllCount += contacts.size();
                    if (contacts.size() < limit) {
                        stop = true;
                    }
                    try {
                        contactSolrComponent.indexes(contacts);
                    } catch (InterruptedException e) {
                        log.error("addImportedContactsToSolr " + e.getMessage());
                        stop = true;
                    }
                    if (mailList != null && mailList.getObjectID() != null) {
                        membersCount = membersCount + crmServiceLocal.createEntityMailList(mailList, ids);
                    }
                } else {
                    stop = true;
                }
            } while (!stop);
            importFile.setImportedColumns(overAllCount);
            importFile.setStatus(ImportStatusEnum.COMPLETED);
            importFileManager.update(importFile);
            importFileManager.merge(importFile);
            messageManager.sendImportReportMessage(importFile.getObjectID());
            addImportedCrmAccountsToSolr(event, false);
            event.setStatus(EventStatus.COMPLETED.name());
        } else {
            log.info("Failed addImportedContactsToSolr objectId=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + importFile.getFileID());
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void addSyncContactsToSolr(EdsBusinessEvent event) {
        if (event.getCustomStringField() != null && !"".equals(event.getCustomStringField())) {
            List<Integer> contactIDs = ServerUtils.getStringAsList(event.getCustomStringField(), ",");
            List<EdsCrmContact> contacts = crmContactManager.getContactsByIDs(contactIDs);
            log.info("Save Contacts finished starting SOLR INDEX {}", LocalDateTime.now());
            if (!contacts.isEmpty()) {
                try {
                    log.info("Contact Sync solrManager.addContactToIndex() started at {}", LocalDateTime.now());
                    contactSolrComponent.indexes(contacts);
                    log.info("Contact Sync solrManager.addContactToIndex() finished at {}", LocalDateTime.now());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void addImportedCrmAccountsToSolr(EdsBusinessEvent event, boolean isCrmAccountImport) {
        EdsImportFile importFile = importFileManager.get(event.getEntityID());
        if (importFile != null && importFile.getCommitted() != null && importFile.getCommitted()) {
            log.info("Start addImportedCrmAccountsToSolr objectId = {}; entityId = {}; fileId = {};", event.getObjectID(), event.getEntityID(), importFile.getFileID());
            int start = 0;
            int limit = 200;
            boolean stop = false;
            boolean noneWasFound = true;
            int size = 0;
            do {
                crmAccountManager.flushAndClear();
                List<EdsCrmAccount> accounts = crmAccountManager.getCrmAccountsByImportFileID(event.getEntityID(), start, limit);
                if (accounts.size() == 0) {
                    stop = true;
                }
                if (accounts.size() > 0) {
                    start = accounts.get(accounts.size() - 1).getObjectID();
                    size += accounts.size();
                    if (accounts.size() < limit) {
                        stop = true;
                    }
                    noneWasFound = false;
                    try {
                        crmAccountSolrComponent.indexes(accounts);
                    } catch (InterruptedException e) {
                        event.setStatus(EventStatus.FAILED.name());
                        log.error("addImportedCrmAccountsToSolr" + e.getMessage());
                        stop = true;
                    }
                } else {
                    if (start == 0 && noneWasFound) {
                        messageManager.sendImportReportMessage(importFile.getObjectID());
                        event.setStatus(EventStatus.COMPLETED.name());
                    }
                    stop = true;
                }
            } while (!stop);
            if (EVENT_SOLR_IMPORTED_CRM_ACCOUNT.equals(event.getEventType())) {
                importFile.setImportedColumns(size);
                importFile.setStatus(ImportStatusEnum.COMPLETED);
                importFileManager.merge(importFile);
                if (isCrmAccountImport) {
                    messageManager.sendImportReportMessage(importFile.getObjectID());
                }
            }
            log.info("End addImportedCrmAccountsToSolr");
            event.setStatus(EventStatus.COMPLETED.name());
        } else {
            log.info("Failed addImportedCrmAccountsToSolr objectId=" + event.getObjectID() + "; entityId=" + event.getEntityID() + "; fileId=" + importFile.getFileID());
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void addImportedOpportunitiesToSolr(EdsBusinessEvent event) {
        EdsImportFile importFile = importFileManager.get(event.getEntityID());
        if (importFile != null && importFile.getCommitted() != null && importFile.getCommitted()) {
            System.out.println("Solr Boshlandi" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + importFile.getFileID());
            int start = 0;
            int limit = 200;
            boolean stop = false;
            int size = 0;
            do {
                List<EdsOpportunity> opportunities = opportunityManager.getByImportFileID(event.getEntityID(), start, limit);
                if (opportunities.size() == 0) {
                    stop = true;
                }
                if (opportunities.size() > 0) {
                    start = opportunities.get(opportunities.size() - 1).getObjectID();
                    size += opportunities.size();
                    if (opportunities.size() < limit) {
                        stop = true;
                    }
                    try {
                        opportunitySolrComponent.indexes(opportunities);
                    } catch (Exception e) {
                        event.setStatus(EventStatus.FAILED.name());
                        stop = true;
                    }
                } else {
                    stop = true;
                }
            } while (!stop);
            importFile.setImportedColumns(size);
            messageManager.sendImportReportMessage(importFile.getObjectID());
            addImportedCrmAccountsToSolr(event, false);
            event.setStatus(EventStatus.COMPLETED.name());
            //Update ImportFile status
            importFile.setStatus(ImportStatusEnum.COMPLETED);
        } else {
            System.out.println("Solr Boshlanmadi " + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + importFile.getFileID());
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManualTransactionImport(EdsBusinessEvent event) {
        Exception exception = null;
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started import manual transaction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());

        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
            exception = e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LinkedList<RejectedImportRecord[]> rejectedRecords = new LinkedList<>();
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        String notImportedTransactions = null;
        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(importingServiceLocal.importManualTransaction(importFile, listOfRows));

            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            edsImportFile.setCommitted(true);
            //Set Total number of created Opportunities
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of REJECTED Opportunities
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_ManualJournals"), "Rejected_ManualJournal_Import_Records.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
        } finally {
            edsImportFile = importFileManager.get(importFile.getObjectID());
            if (exception != null) {
                StringBuilder buf = new StringBuilder();
                if (exception.getStackTrace() != null && exception.getStackTrace().length > 0) {
                    for (StackTraceElement stack : exception.getStackTrace()) {
                        buf.append(stack.toString()).append("\n\r");
                    }
                }
                edsImportFile.setException(exception.getMessage() + ":::" + buf.toString());
                edsImportFile.setStatus(ImportStatusEnum.FAILED);
            }
            importFileManager.update(edsImportFile);
            importFileManager.merge(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        System.out.println("Finished import manual transaction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        messageManager.sendImportReportMessage(edsImportFile.getObjectID());
        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onManualTransactionTallyImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Started import tally manual transaction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());

        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr;
        try {
            isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            isr = new InputStreamReader(inputStream);
        }
        LinkedHashMap<NewManualTransaction, NewManualTransaction> notImportedTransactionMap = new LinkedHashMap<>();
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        try {
            List<String[]> listOfRows = reader.readAll();
            notImportedTransactionMap = importingServiceLocal.importManualTransactionTally(importFile, listOfRows);
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.setStatus(EventStatus.COMPLETED.name());
        try {
            attachmentManager.delete(attachment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Finished import tally manual transaction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        SecurityContext.getInstance().setStaticUserID(null);

        if (!notImportedTransactionMap.isEmpty()) {
            messageManager.sendImportReportMessage(importFile.getObjectID());
        }
    }

    private void onAdditionalPaymentImport(EdsBusinessEvent event) {

        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Started import additional payment/deduction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        ImportFile importFile = edsImportFile.getRPC();
        try {
            EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());

            SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
            InputStream inputStream = uploadManager.getInputStream(attachment);

            InputStreamReader isr;
            try {
                isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            } catch (Exception e) {
                isr = new InputStreamReader(inputStream);
            }
            CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
            //Read all rows from CSV File which being imported
            List<String[]> listOfRows = reader.readAll();

            //Process All rows
            ArrayList<RejectedImportRecord[]> rejectedRecords = importingServiceLocal.importAdditionalPayment(importFile, listOfRows);

            edsImportFile.setCommitted(true);

            //Set Total number of created items
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            //Set Total number of REJECTED items
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            //Set Total non empty rows which were processed
            edsImportFile.setCsvColumns(edsImportFile.getImportedColumns() + edsImportFile.getIgnoredColumns() + (edsImportFile.isHasHeader() ? 1 : 0));

            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_AdditionalPaymentsOrDeductions"), "Rejected_AdditionalPaymentsOrDeductions.xls");
            }
            //Set Status as COMPLETED
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
            //Update import file which will displayed in the UI (Settings->System Logs)
            importFileManager.update(edsImportFile);

            //Send email
            messageManager.sendImportReportMessage(importFile.getObjectID());
        } catch (Exception e) {
            log.error("", e);
//            edsImportFile = importFileManager.get(importFile.getObjectID());
            edsImportFile.setException(e.getMessage() != null ? e.getMessage() : "");
            edsImportFile.setStatus(ImportStatusEnum.FAILED);
            importFileManager.update(edsImportFile);
        }
        event.setStatus(EventStatus.COMPLETED.name());
        /*try {
            attachmentManager.delete(attachment);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.out.println("Finished import additional payment/deduction from csv :" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());
        SecurityContext.getInstance().setStaticUserID(null);

    }

    private void onBrandImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        System.out.println("Importing project" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID());
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importBrands(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size()))));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Brands"), "Rejected_Brands.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Brand" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date().toString());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);
    }

    private void onEmployeeLeaveAllowanceImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Importing Employee Leave Allowances" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date());
        ImportFile importFile = edsImportFile.getRPC();
        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        InputStreamReader isr = getInputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();

        try {
            List<String[]> listOfRows = reader.readAll();
            rejectedRecords.addAll(importingServiceLocal.importEmployeeLeaveAllowances(importFile, listOfRows));
            importFile.setHasHeader(true);
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Leave_Allowances"), "Rejected_Leave_Allowances.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);

            messageManager.sendImportReportMessage(importFile.getObjectID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Employee Leave Allowances" + event.getObjectID() + "<==>" + event.getEntityID() + "<==>" + edsImportFile.getFileID() + " :: " + new Date());

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onPurchaseOrderImport(EdsBusinessEvent event) {
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("Importing Purchase Order{}<==>{}<==>{} :: {}", event.getObjectID(), event.getEntityID(), edsImportFile != null ? edsImportFile.getFileID() : null, new Date());

        try {
            ImportFile importFile = edsImportFile.getRPC();
            EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
            SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
            InputStream inputStream = uploadManager.getInputStream(attachment);

            InputStreamReader isr = getInputStreamReader(inputStream);
            CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
            ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
            List<String[]> listOfRows = reader.readAll();
            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importPurchaseOrder(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size()))));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Purchase_Orders"), "Rejected_Purchase_Orders.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);

            messageManager.sendImportReportMessage(importFile.getObjectID());
            edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            edsImportFile.setStatus(ImportStatusEnum.FAILED);
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Purchase Order{}<==>{}<==>{} :: {}", event.getObjectID(), event.getEntityID(), edsImportFile.getFileID(), new Date());

        importFileManager.update(edsImportFile);

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onDepartmentImport(EdsBusinessEvent event) {
        long start = System.currentTimeMillis();
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("=================== Importing position " + event.getObjectID() + " <==> " + event.getEntityID() + " <==> " + edsImportFile.getFileID() + " :: " + new Date() + " ===================");
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        BufferedReader isr = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();

            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importDepartment(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size())), event.getCompanyId()));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Departments"), "Rejected_Departments.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Department " + event.getObjectID() + " <==> " + event.getEntityID() + " <==> " + edsImportFile.getFileID() + " in " + (System.currentTimeMillis() - start) / 1000 + " seconds");

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        messageManager.sendImportReportMessage(edsImportFile.getObjectID());

        SecurityContext.getInstance().setStaticUserID(null);
    }

    private void onPositonImport(EdsBusinessEvent event) {
        long start = System.currentTimeMillis();
        EdsImportFile edsImportFile = importFileManager.get(event.getEntityID());
        log.info("=================== Importing position " + event.getObjectID() + " <==> " + event.getEntityID() + " <==> " + edsImportFile.getFileID() + " :: " + new Date() + " ===================");
        ImportFile importFile = edsImportFile.getRPC();

        EdsAttachment attachment = attachmentManager.get(edsImportFile.getFileID());
        SecurityContext.getInstance().setStaticUserID(edsImportFile.getOwner() != null ? edsImportFile.getOwner().getObjectID() : null);
        InputStream inputStream = uploadManager.getInputStream(attachment);

        BufferedReader isr = new BufferedReader(getInputStreamReader(inputStream));
        CSVReader reader = new CSVReader(isr, edsImportFile.getDefaultSeparator().charAt(0));
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        try {
            List<String[]> listOfRows = reader.readAll();

            for (int i = 0; i < Math.ceil((double) listOfRows.size() / (double) BATCH_LIMIT); i++) {
                rejectedRecords.addAll(importingServiceLocal.importPosition(importFile, listOfRows.subList(i * BATCH_LIMIT, Math.min(i * BATCH_LIMIT + BATCH_LIMIT, listOfRows.size())), event.getCompanyId()));
                importFile.setHasHeader(false);
            }
            edsImportFile = importFileManager.get(edsImportFile.getObjectID());
            if (rejectedRecords.size() > (edsImportFile.isHasHeader() ? 1 : 0)) {
                RejectedImportRecordsExcelHandler rejectedRecordsExcelHandler = new RejectedImportRecordsExcelHandler();
                initRejectedRecordsForSending(edsImportFile, rejectedRecordsExcelHandler.run2(rejectedRecords, "Rejected_Positions"), "Rejected_Positions.xls");
            }
            if (edsImportFile.getCsvColumns() == null) {
                edsImportFile.setCsvColumns(listOfRows.size());
            }
            edsImportFile.setImportedColumns(importFile.getImportedColumns());
            edsImportFile.setNewColumns(importFile.getNewColumns());
            edsImportFile.setIgnoredColumns(importFile.getIgnoredColumns());
            edsImportFile.setOverwrittenColumns(importFile.getOverwrittenColumns());
            edsImportFile.setSkippedColumns(importFile.getSkippedColumns());
            edsImportFile.setCommitted(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        edsImportFile.setCommitted(true);
        event.setStatus(EventStatus.COMPLETED.name());

        log.info("Finished import Position " + event.getObjectID() + " <==> " + event.getEntityID() + " <==> " + edsImportFile.getFileID() + " in " + (System.currentTimeMillis() - start) / 1000 + " seconds");

        edsImportFile.setStatus(ImportStatusEnum.COMPLETED);
        importFileManager.update(edsImportFile);

        messageManager.sendImportReportMessage(edsImportFile.getObjectID());

        SecurityContext.getInstance().setStaticUserID(null);

    }


    @Transactional
    public void initRejectedRecordsForSending(EdsImportFile edsImportFile, ByteArrayOutputStream baos, String filename) {
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            baos.flush();
            baos.close();
        } catch (Exception ex) {
            System.out.print("......................................");
        }
        System.out.print("*****************Upload to Amazon S3 server****************");
        EdsUpload upload = new EdsUpload();
        upload.setContentType("application/xls");
        upload.setOriginalName(filename);
        upload.setType(referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType()));
        upload.setInputStream(bais);
        try {
            uploadManager.create(upload);
            edsImportFile.setRejectedRecords(upload);
            System.out.print("****************File Uploaded******************");
        } catch (Exception ex) {
            System.err.println("****************Failed to Upload File******************");
        }
        try {
            bais.close();
        } catch (IOException ex) {
            System.err.println("Unable to close stream");
        }
    }
}
