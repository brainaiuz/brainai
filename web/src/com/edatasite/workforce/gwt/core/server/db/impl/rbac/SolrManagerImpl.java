package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSinxDocumentsSettings;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseBooking;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.solr.component.AdditionalPaymentSolrComponent;
import com.edatasite.workforce.core.solr.component.CaseSolrComponent;
import com.edatasite.workforce.core.solr.component.CashAdvanceSolrComponent;
import com.edatasite.workforce.core.solr.component.CertificateSolrComponent;
import com.edatasite.workforce.core.solr.component.ChartOfAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.CustomFormItemSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.GroupPayrunSolrComponent;
import com.edatasite.workforce.core.solr.component.LeaveRequestSolrComponent;
import com.edatasite.workforce.core.solr.component.NewsSolrComponent;
import com.edatasite.workforce.core.solr.component.OpportunitySolrComponent;
import com.edatasite.workforce.core.solr.component.ProductsServicesSolrComponent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.PurchaseOrderSolrComponent;
import com.edatasite.workforce.core.solr.component.RequestForQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleQuoteSolrComponent;
import com.edatasite.workforce.core.solr.component.SinglePayrunSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.solr.component.VacancySolrComponent;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseBookingRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCourseScheduleRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrDepartmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeAssessmentRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaOperations;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.core.server.db.NewsCommentManager;
import com.edatasite.workforce.gwt.core.server.db.NewsManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.SinxDocumentsSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserBankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CourseScheduleStudentManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.client.server.app.ClientServiceImpl.ZERO;

/**
 * User: Abdulaziz
 * Date: Oct 30, 2009
 * Time: 9:10:20 PM
 * This class responsible for indexing Domain Objects to the Solr
 * Every time before indexing domain object it calls remove to avoid dublication in SolrIndex
 * It calls singleton solr server according to the domain object , domain objects such as EdsTask, EdsProject are indexing to the one core
 * where EdsAttachment indexing to the another core
 */

@Service("solrManager")
public class SolrManagerImpl implements SolrManager, Constants {

    private static final Logger logger = LoggerFactory.getLogger(SolrManagerImpl.class.getName());
    @Autowired
    UserBankAccountManager userBankAccountManager;
    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private EmailAttachmentManager emailAttachmentManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private CourseScheduleStudentManager courseScheduleStudentManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private JobFamilyManager jobFamilyManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private NewsCommentManager newsCommentManager;
    private WfmJpaOperations jpaTemplate;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private MailListManager mailListManager;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CertificateOfEmploymentManager certificateOfEmploymentManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private PayslipTableManager payslipTableManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private RFQManager rfqManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private AdditionalPaymentSolrComponent additionalPaymentSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CaseSolrComponent caseSolrComponent;
    @Autowired
    private CashAdvanceSolrComponent cashAdvanceSolrComponent;
    @Autowired
    private CertificateSolrComponent certificateSolrComponent;
    @Autowired
    private ChartOfAccountSolrComponent chartOfAccountSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private CustomFormItemSolrComponent customFormItemSolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;
    @Autowired
    private GroupPayrunSolrComponent groupPayrunSolrComponent;
    @Autowired
    private SinglePayrunSolrComponent singlePayrunSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private LeaveRequestSolrComponent leaveRequestSolrComponent;
    @Autowired
    private NewsSolrComponent newsSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private RequestForQuoteSolrComponent requestForQuoteSolrComponent;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private VacancySolrComponent vacancySolrComponent;


    public void addTaskRbacEntryToSolr(EdsTaskRbac tRbac, EdsCompany company) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        String compositID = "";
        String trusteeType = String.valueOf(tRbac.getTrusteeType());

        if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType())) {
            compositID = company.getObjectID() + "_" + tRbac.getTask().getObjectID() + "_" + tRbac.getUser().getObjectID() + "_" + tRbac.getTrusteeType();
        } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
            compositID = company.getObjectID() + "_" + tRbac.getTask().getObjectID() + "_" + tRbac.getGroup().getObjectID() + "_" + tRbac.getTrusteeType();
        }
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrTaskRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrTaskRepresenter.FIELD_COMPANY_ID, company.getObjectID());


        doc.addField(SolrTaskRepresenter.FIELD_TASK_ID, tRbac.getTask().getObjectID());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_NUMBER, tRbac.getTask().getNumber());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_NAME, tRbac.getTask().getName());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_DESCRIPTION, tRbac.getTask().getDescription());

        doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME, tRbac.getTask().getProject().getName());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_NUMBER, tRbac.getTask().getProject().getNumber());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID, tRbac.getTask().getProject().getObjectID());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME, tRbac.getTask().getProject().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getTask().getProject().getName());

        if (tRbac.getTask().getParentWS() != null) {
            doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_NAME, tRbac.getTask().getParentWS().getName());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID, tRbac.getTask().getParentWS().getObjectID());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_WORKSTREAM_ID_NAME, tRbac.getTask().getParentWS().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getTask().getParentWS().getName());
        }

        if (tRbac.getClient() != null) {
            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME, tRbac.getClient().getName());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID, tRbac.getClient().getObjectID());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME, tRbac.getClient().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getClient().getName());
        }

        if (tRbac.getDepartment() != null) {
            doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_NAME, tRbac.getDepartment().getName());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_ID, tRbac.getDepartment().getObjectID());
            doc.addField(SolrTaskRepresenter.FIELD_TASK_USER_DEPARTMENT_ID_NAME, tRbac.getDepartment().getObjectID() + SolrTaskRepresenter.SPLIT + tRbac.getDepartment().getName());
        }

        if (EdsTrusteeType.USER.equals(tRbac.getTrusteeType())) {
            EdsEmployee edsEmployee = tRbac.getUser().getEmployee();
            String employeeNumber = edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null ? edsEmployee.getProfile().getEmployeeCode() + " " : "";
            doc.addField(SolrTaskRepresenter.FIELD_USER_ID_NAME, tRbac.getUser().getObjectID() + SolrTaskRepresenter.SPLIT + employeeNumber + tRbac.getUser().getName());
            doc.addField(SolrTaskRepresenter.FIELD_USER_ID, tRbac.getUser().getObjectID());
            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, tRbac.getUser().getObjectID() + SolrTaskRepresenter.FIELD_USER_ID);
        } else if (EdsTrusteeType.GROUP.equals(tRbac.getTrusteeType())) {
            doc.addField(SolrTaskRepresenter.FIELD_GROUP_ID, tRbac.getGroup().getObjectID());
            doc.addField(SolrTaskRepresenter.FIELD_VIEWERS, tRbac.getGroup().getObjectID() + SolrTaskRepresenter.FIELD_GROUP_ID);
        }
        Map<Integer, List<String>> assigneeUserListMap = taskManager.getTaskAssigneeUserList(Collections.singletonList(tRbac.getTask().getObjectID()));
        for (String userName : assigneeUserListMap.get(tRbac.getTask().getObjectID())) {
            doc.addField(SolrTaskRepresenter.FIELD_ASSIGNEE_NAMES, userName);
        }

        doc.addField(SolrTaskRepresenter.FIELD_TRUSTEE_TYPE, trusteeType);

        doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS, tRbac.getStatus().getName());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID, tRbac.getStatus().getObjectID());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_CODE, tRbac.getStatus().getCode());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE, tRbac.getStatus().getObjectID() + SolrProjectListRepresenter.SPLIT + tRbac.getStatus().getCode());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_STATUS_ID_CODE_NAME, tRbac.getStatus().getObjectID() + SolrProjectListRepresenter.SPLIT + tRbac.getStatus().getCode() + SolrProjectListRepresenter.SPLIT + tRbac.getStatus().getName());
        doc.addField(SolrTaskRepresenter.FILED_TASK_PERCENT_COMPLETED, tRbac.getPercent() != null ? tRbac.getPercent() : tRbac.getTask().getPercent());
        doc.addField(SolrTaskRepresenter.FIELD_ESTIMATED_TIME, tRbac.getEstimatedTime());

        doc.addField(SolrTaskRepresenter.FIELD_DUE_DATE, tRbac.getTask().getDueDate());
        doc.addField(SolrTaskRepresenter.FIELD_START_DATE, tRbac.getTask().getStartDate());
        doc.addField(SolrTaskRepresenter.FIELD_END_DATE, tRbac.getTask().getActualEndDate());
        doc.addField(SolrTaskRepresenter.FIELD_TASK_PRIORITY, tRbac.getTask().getPriority().getName());
        doc.addField(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, tRbac.getTask().getLastUpdateTime());

        doc.setField(SolrTaskRepresenter.FIELD_RANK, tRbac.getRelationRank());
        String[] permissions = ServerUtils.getPermissions(tRbac.getTaskPermission());
        for (String perm : permissions) {
            doc.addField(SolrTaskRepresenter.FIELD_PERMISSIONS, perm);
        }
        doc.addField(SolrTaskRepresenter.FIELD_RELATIONSHIPS, tRbac.getRelationship());
        CustomFieldsUtils.setInSolrCustomFields(doc, tRbac.getTask().getTaskCustomFields());

        solr.add(doc);
        solr.commit();
    }

    private void addCommentToDoc(SolrInputDocument doc, String comment) {
        Metadata metadata = new Metadata();
        HtmlParser parser = new HtmlParser();
        ContentHandler textHandler = new BodyContentHandler();
        ByteArrayInputStream bais = new ByteArrayInputStream(comment.getBytes());
        try {
            parser.parse(bais, textHandler, metadata);
        } catch (IOException | TikaException | SAXException e) {
        }

        try {
            bais.close();
        } catch (IOException e) {
        }
        doc.addField(SolrEmployeeAssessmentRepresenter.FIELD_COMMENTS, textHandler.toString());
    }

    /**
     * Removes task from solr index
     *
     * @param task
     * @param company
     */
    public void removeTask(EdsTask task, EdsCompany company) throws SolrServerException, SolrException, IOException {
        removeCompanyTasksbyIds(company.getObjectID(), task.getObjectID());
    }

    public void removeTasks(List<EdsTask> tasks) throws SolrServerException, SolrException, IOException {
        removeCompanyTasksbyIds(getObjectIDs(tasks));
    }

    public void removeCompanyTasksbyIds(Integer... taskIDs) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrTaskRepresenter.FIELD_TASK_ID + ":", SOLR_TASK_CORE, taskIDs);
    }

    public void removeCompanyPurchaseOrdersbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":", SOLR_PURCHASE_ORDER_CORE, ids);
    }

    public void removeCompanySaleQuotesbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":", SOLR_SALEQUOTE_CORE, ids);
    }

    public void removeCompanyPurchaseInvoicebyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":", SOLR_PURCHASE_INVOICE_CORE, ids);
    }

    public void removeCompanyShippingDatasbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":", SOLR_SHIPPING_DATA_CORE, ids);
    }

    public void removeCompanyCertificatesbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID + ":", SOLR_CERTIFICATE_CORE, ids);
    }

    public void removeCompanyDepartmentsbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID + ":", SOLR_DEPARTMENT_CORE, ids);
    }

    public void removeCompanyPositionsbyIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrPositionRepresenter.FIELD_POSITION_ID + ":", SOLR_POSITION_CORE, ids);
    }

//    /**
//     * Removes trustee tasks
//     */
//    public void removeTasksAllRbacRecord(EdsTask task, EdsCompany company, EdsTrustee trustee, int trusteeType) throws SolrServerException, SolrException, IOException {
//        String removeQuery = SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + company.getObjectID() + " AND " + SolrTaskRepresenter.FIELD_TASK_ID + ":" + task.getObjectID()
//                + " AND " + SolrTaskRepresenter.FIELD_TRUSTEE_TYPE + ":" + trusteeType + " AND " + SolrTaskRepresenter.FIELD_USER_ID + ":" + trustee.getTrusteeID();
//        removeEntity(removeQuery, SOLR_TASK_CORE);
//    }

    /**
     * removes rbac entries of task when employee is deleted or terminated
     * in order to correctly show assignee facet filter data
     */
    @Override
    public void removeEmployeeAllRbacRecord(Integer companyID, int assigneeID) throws SolrServerException, SolrException, IOException {
        String removeQuery = SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrTaskRepresenter.FIELD_USER_ID + ":" + assigneeID;
        removeEntity(removeQuery, SOLR_TASK_CORE);
    }

    public void removeProjectRelatedAllTaskRbacRecords(EdsProject project, EdsCompany company) throws SolrServerException, SolrException, IOException {
        String removeQuery = SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + company.getObjectID() + " AND " + SolrTaskRepresenter.FIELD_TASK_PROJECT_ID + ":" + project.getObjectID();
        removeEntity(removeQuery, SOLR_TASK_CORE);
    }

    public void removeCompanyTasks(Integer companyID) throws SolrServerException, SolrException, IOException {
        removeEntity(SolrTaskRepresenter.FIELD_COMPANY_ID + ":" + companyID, SOLR_TASK_CORE);
    }

    public void removeCompanyNews(EdsCompany company) throws SolrServerException, SolrException, IOException {
        removeEntity(SolrNewsRepresenter.FIELD_COMPANY_ID + ":" + company.getObjectID(), SOLR_NEWS_CORE);
    }

    /**
     * @param trackerIDs@return ids which have attachment(s)
     */
    private List<Integer> getCaseTrackerIDsOnlyWithAttachments(List<Integer> trackerIDs) {
        List<Integer> ids = caseManager.getTrackerIDsWithAttachments(trackerIDs);
        ids.addAll(emailAttachmentManager.getTrackerIDsWithAttachments(trackerIDs));
        return ids;
    }

    /**
     * @param caseID
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void removeCompanyCaseByIds(Integer... caseID) throws IOException, SolrServerException {
        removeMultiEntry(SolrCaseRepresenter.CASE_ID + ":", SOLR_CASE_CORE, caseID);
    }

    /**
     * @param eventID
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void removeCompanyEventByIds(Integer... eventID) throws IOException, SolrServerException {
        removeMultiEntry(SolrEventRepresenter.FIELD_EVENT_ID + ":", SOLR_EVENT_CORE, eventID);
    }

    /**
     * <h1>....REMOVE COMPANY CASES IN SOLR REPOSITORY...</h1>
     * <br/>
     * <h2>...METHOD WRITE BY DEVELOPER - { DILSHOD.T }...</h2>
     * <br/>
     * <h3>...METHOD CREATED DATE - { 21:19 27/04/2011 }...</h3>
     * <br/>
     * <br/>
     *
     * @param companyID
     * @throws IOException
     * @throws SolrServerException
     */
    @Override
    public void removeCompanyCase(Integer companyID) {
        removeCase(null, companyID);
    }

    public void removeCase(Integer caseID, Integer companyID) {
        String removeQuery = "";
        if (caseID != null && companyID != null) {
            removeQuery = SolrCaseRepresenter.COMPANY_ID + ":" + companyID + " AND " + SolrCaseRepresenter.CASE_ID + ":" + caseID;
        } else if (companyID != null) {
            removeQuery = SolrCaseRepresenter.COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_CASE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes entity by given removewQuery from given core
     *
     * @param removeQuery
     * @param coreName
     */
    public void removeEntity(String removeQuery, String coreName) throws SolrServerException, SolrException, IOException {
        try (SolrClient solr = WfmJpaTemplate.getSolrServerForCore(coreName)) {
            solr.deleteByQuery(removeQuery);
            solr.commit();
        } catch (SolrServerException | SolrException | IOException e) {
            System.err.println("Error removing documents: " + e.getMessage());
            // Handle the exception as needed (log, rethrow, etc.)
        }
    }

    /**
     * <b> This is method task index to solr </b>
     *
     * @param task
     * @param companyId
     * @throws SolrServerException
     * @throws IOException
     */
    public void addTaskToIndex(EdsTask task, Integer companyId) throws SolrServerException, IOException {
        addTaskToIndex(Collections.singletonList(task), companyId);
    }

    public void addTaskToIndex(EdsTask task) throws SolrServerException, IOException {
        Integer companyID = SecurityContext.getCompanyID();
        addTaskToIndex(Collections.singletonList(task), companyID);
    }

    /**
     * <b> This is method tasks index to solr </b>
     *
     * @param tasks
     * @param companyId
     * @throws SolrServerException
     * @throws SolrException
     * @throws IOException
     */


    public void addTaskToIndex(List<EdsTask> tasks, Integer companyId) throws SolrServerException, SolrException, IOException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        removeTasks(tasks);
        Integer id = null;
        try {
            String ids = "";
            List<Integer> tids = tasks.stream().map(EdsTask::getObjectID).toList();
            Map<Integer, List<EdsTaskRbac>> edsTaskRbacEntriesMap = taskRbacManager.getTaskRbacEntries(tids);
            Map<Integer, List<String>> edsAssigneeUserList = taskManager.getTaskAssigneeUserList(tids);
            for (EdsTask edsTask : tasks) {
                if (edsTask.getDeleted() != null && edsTask.getDeleted()) {
                    continue;
                }
                id = edsTask.getObjectID();
                List<EdsRelation> edsRelationList = relationManager.getAllRelations(EdsRelation.TYPE_TASK, edsTask.getObjectID());
                logger.info(String.format("cId=%s, TaskID >>>= %s", companyId, edsTask.getObjectID() + " >>>= is going to be added to solr"));
                solrDocs.addAll(edsTask.indexToSolr(edsTaskRbacEntriesMap.get(id), edsAssigneeUserList.get(id), companyId, edsRelationList));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
            }
            logger.info(String.format("cId=%s, TaskIDs >>>=%s", companyId, ids + " added to batch solr"));
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId >>>= " + companyId + ", TaskId >>>= " + id + " Solr Index Exception. ----------------------->>>>=");
//            e.printStackTrace();
        }
    }

    @Override
    public void addFolderToIndex(EdsFolder folder, EdsCompany company) throws SolrServerException, SolrException, IOException {
        addFolderToIndex(Collections.singletonList(folder), company);
    }

    @Override
    public void addFolderToIndex(List<EdsFolder> folders, EdsCompany company) throws SolrServerException, SolrException, IOException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_FOLDER_CORE);
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        removeFolders(folders);//before indexing remove existing taskindex
        Integer id = null;
        try {
            String ids = "";
            for (EdsFolder edsFolder : folders) {
                id = edsFolder.getObjectID();
                List<EdsFolderRbac> folderRbacEntries = folderRbacManager.getFolderRbacEntries(edsFolder.getObjectID());
                logger.info(String.format("cId=%s, Folder >>>=%s", company.getObjectID(), edsFolder.getName() + "_" + edsFolder.getObjectID() + " >>>= is going to be added to solr"));
                solrDocs.addAll(edsFolder.indexToSolr(folderRbacEntries, company.getObjectID()));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
            }
            solrDocs = commit100ToSolr(solrServer, solrDocs, true);
            logger.info(String.format("cId=%s, FolderIDs >>>=%s", company.getObjectID(), ids + " added to batch solr"));
        } catch (IOException | SolrServerException e) {
            logger.info("cId >>>=" + company.getObjectID() + ", FolderId >>>=" + id + " Solr Index Exception. ----------------------->>>>=");
//            e.printStackTrace();
        }
    }

    @Override
    public void removeFolders(List<EdsFolder> folders) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrFolderRepresenter.FIELD_FOLDER_ID + ":", SOLR_FOLDER_CORE, getObjectIDs(folders));
    }

    @Override
    public void removeFolder(Integer folderId) throws SolrServerException, SolrException, IOException {
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        String removeQuery = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrFolderRepresenter.FIELD_FOLDER_ID + ":" + folderId + " AND " + SolrFolderRepresenter.FIELD_IS_FILE + ":" + false;
        removeEntity(removeQuery, SOLR_FOLDER_CORE);
    }

    @Override
    public void removeCompanyFolders(Integer companyid) {
        String removeQuery = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyid + " AND " + SolrFolderRepresenter.FIELD_IS_FILE + ":" + false;
        try {
            removeEntity(removeQuery, SOLR_FOLDER_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeGroupEntries(Integer groupId) throws SolrServerException, SolrException, IOException {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String removeQuery = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrFolderRepresenter.FIELD_GROUP_ID + ":" + groupId;
        removeEntity(removeQuery, SOLR_FOLDER_CORE);
    }

    @Override
    public void removeUserEntries(Integer userId) throws SolrServerException, SolrException, IOException {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String removeQuery = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrFolderRepresenter.FIELD_USER_ID + ":" + userId;
        removeEntity(removeQuery, SOLR_FOLDER_CORE);
    }

    @Override
    public void addFileToIndex(EdsFileHeader file) throws SolrServerException, SolrException, IOException {
        addFileToIndex(Collections.singletonList(file));
    }

    @Override
    public void addFileToIndex(List<EdsFileHeader> files) throws SolrServerException, SolrException, IOException {
        addFileToIndex(files, null);
    }

    public void addFileToIndex(List<EdsFileHeader> files, EdsUser user) throws SolrServerException, SolrException, IOException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_FOLDER_CORE);
        removeFiles(files);
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        Integer id = null;
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        try {
            String ids = "";
            for (EdsFileHeader edsFileHeader : files) {
                String downloadUrl = "";
                if (edsFileHeader.getFolder() != null && Constants.F_EMPLOYEE_PROFILE == edsFileHeader.getFolder().getFolderType()) {
                    //T4056
                    /*user = user == null ? (edsFileHeader.getEntityId() != null ? userManager.get(edsFileHeader.getEntityId()) : userManager.getUser()) : user;
                    if (user == null || user.getDeleted()) {
                        continue;
                    }*/
                    downloadUrl = getDownloadUrl(edsFileHeader);
                }

                id = edsFileHeader.getObjectID();
                List<EdsFolderRbac> fileRbacEntries = folderRbacManager.getFileRbacEntries(edsFileHeader.getObjectID());
                logger.info(String.format("cId=%s, File >>>= %s", companyId, edsFileHeader.getName() + "_" + edsFileHeader.getObjectID() + " >>>= is going to be added to solr"));
                solrDocs.add(edsFileHeader.indexToSolr(fileRbacEntries, companyId, isIntegerNumberEnabled, user, downloadUrl));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
                user = null;
            }
            commit100ToSolr(solrServer, solrDocs, true);
            logger.info(String.format("cId=%s, FolderIDs >>>= %s", companyId, ids + " added to batch solr"));
        } catch (IOException | SolrServerException e) {
            logger.info("cId >>>=" + companyId + ", FileId >>>=" + id + " Solr Index Exception. ----------------------->>>>=");
//            e.printStackTrace();
        }
    }

    private String getDownloadUrl(EdsFileHeader edsFileHeader) {
        String downloadUrl = "";
        EdsFileBody fileBody = edsFileHeader.getCurrentBody();
        if (fileBody != null) {
            String uploadType = fileBody.getType() != null ? fileBody.getType().getCode() : "";
            if (Constants.AMAZON.equals(uploadType)) {
                downloadUrl = getFileUrl(edsFileHeader.getCurrentBody().getObjectID());
            } else if (Constants.GOOGLE.equals(uploadType) || Constants.OFFICE_365.equals(uploadType) || Constants.OFFICE_365_SHARE_POINT.equals(uploadType)) {
                EdsSinxDocumentsSettings googleDocumentsSettings = sinxDocumentsSettingsManager.getSinxDocsSettings(fileBody);
                if (googleDocumentsSettings != null) {
                    if (Constants.GOOGLE.equals(uploadType)) {
                        downloadUrl = googleDocumentsSettings.getDownloadLink();
                    } else {
                        downloadUrl = googleDocumentsSettings.getDocumentLink();
                    }
                }
            } else {
                downloadUrl = getFileUrl(edsFileHeader.getCurrentBody().getObjectID());
                if (StringUtil.isEmpty(downloadUrl)) {
                    downloadUrl = CommandConstants.COMMON_URL + "/downloadFile?id=" + fileBody.getObjectID();
                }
            }
        }
        return downloadUrl;
    }

    public String getFileUrl(Integer fileId) {
        return uploadManager.getFileURL(fileId);
    }

    @Override
    public void removeFile(Integer fileId) throws SolrServerException, SolrException, IOException {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String removeQuery = SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrFolderRepresenter.FIELD_FOLDER_ID + ":" + fileId + " AND " + SolrFolderRepresenter.FIELD_IS_FILE + ":" + true;
        removeEntity(removeQuery, SOLR_FOLDER_CORE);
    }

    public void removeFiles(List<EdsFileHeader> files) throws SolrServerException, SolrException, IOException {
        removeCompanyFilesIds(getObjectIDs(files));
    }

    public void removeCompanyFilesIds(Integer... fileIds) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrFolderRepresenter.FIELD_IS_FILE + ":true AND " + SolrFolderRepresenter.FIELD_FOLDER_ID + ":", SOLR_FOLDER_CORE, fileIds);
    }

    public void removeCompanyFiles(Integer companyId) throws SolrServerException, SolrException, IOException {
        removeEntity(SolrFolderRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrFolderRepresenter.FIELD_IS_FILE + ":true", SOLR_FOLDER_CORE);
    }

    @Override
    public void addLeadToIndex(EdsCrmContact... leads) throws SolrServerException, SolrException, IOException {
        addContactToIndex(leads);
    }

    @Override
    public void addCrmAccountToIndex(EdsCrmAccount... crmAccounts) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (crmAccounts != null && crmAccounts.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCrmAccount crmAccount : crmAccounts) {
                if (crmAccount != null) {
                    if (!crmAccount.isDeleted()) {
                        docs.addAll(crmAccount.getSolrDocument(companyID));
                        logger.info("CRMACCOUNT adding to solr index cId=" + companyID + ",  crmAccountId=" + crmAccount.getObjectID().toString());
                    }
                    idsToRemove.add(crmAccount.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeCrmAccountByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeCrmAccountByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }

    }

    @Override
    public void addCrmAccountWithContactToIndex(EdsCrmAccount... crmAccounts) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (crmAccounts != null && crmAccounts.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCrmAccount crmAccount : crmAccounts) {
                docs.addAll(crmAccount.getSolrDocument(companyID));
                logger.info("CRMACCOUNT adding to solr index cId=" + companyID + ",  crmAccountId=" + crmAccount.getObjectID().toString());
                addContactToIndex(crmAccount.getCrmContacts().toArray(new EdsCrmContact[0]));
                idsToRemove.add(crmAccount.getObjectID());
                if (docs.size() >= SOLR_LIMIT) {
                    removeCrmAccountByIds(idsToRemove.toArray(new Integer[]{}));
                    idsToRemove.clear();
                    docs = commit100ToSolr(solr, docs, false);
                }
            }
            removeCrmAccountByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    public void addNewsToIndex(EdsNews... newses) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_NEWS_CORE);
        removeCompanyNewsByIds(getObjectIDs(newses));
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        List<SolrInputDocument> docs = new ArrayList<>();
        for (EdsNews news : newses) {
            int categoriesCount = 1;
            if (news.getCategories() != null && news.getCategories().size() != 0) {
                categoriesCount = news.getCategories().size() - 1;
            }
            logger.info("NEWS cId=" + companyId + ", newsId=" + news.getObjectID() + " is going to be added to solr");
            String compositID = news.getUser().getCompany().getObjectID() + "_" + news.getObjectID();
            SolrInputDocument doc = new SolrInputDocument();
            for (int b = 0; b <= categoriesCount; b++) {
                if (news.getUser() != null && news.getUser().getObjectID() != null && news.getUser().getCompany() != null && news.getUser().getCompany().getObjectID() != null) {
                } else {
                    b = categoriesCount + 1;
                    logger.info("NEWS cId=" + companyId + ", newsId=" + news.getObjectID() + " is not going to be added to solr");
                    continue;
                }

                if (news.getDeleted() != null) {
                    if (news.getDeleted()) {
                        b = categoriesCount + 1;
                        logger.info("NEWS cId=" + companyId + ", newsId=" + news.getObjectID() + " is not going to be added to solr! (It has 'deleted' status)");
                        continue;
                    }
                }
                if (news.getCategories() != null && news.getCategories().size() != 0) {
                    if (news.getCategories().get(b) != null) {
                        doc.addField(SolrNewsRepresenter.FIELD_CATEGORY_ID, news.getCategories().get(b).getObjectID());
                        doc.addField(SolrNewsRepresenter.FIELD_CATEGORY_NAME, news.getCategories().get(b).getName());
                    }
                }
            }
            doc.addField(SolrNewsRepresenter.FIELD_COMPOSITE_ID, compositID);
            doc.addField(SolrNewsRepresenter.FIELD_NEWS_ID, news.getObjectID());
            doc.addField(SolrNewsRepresenter.FIELD_COMPANY_ID, news.getUser() != null && news.getUser().getCompany() != null ? news.getUser().getCompany().getObjectID() : null);
            doc.addField(SolrNewsRepresenter.FIELD_SUBJECT, news.getSubject());
            doc.addField(SolrNewsRepresenter.FIELD_SUBJECT_COMPOSITE, news.getSubject());
            doc.addField(SolrNewsRepresenter.FIELD_DATE, news.getDate());
            doc.addField(SolrNewsRepresenter.FIELD_FULL_TEXT, news.getFullText());
            doc.addField(SolrNewsRepresenter.FIELD_COMPOSITE, (news.getSubject() != null ? news.getSubject() : "") + (news.getFullText() != null ? news.getFullText() : ""));
            doc.addField(SolrNewsRepresenter.FIELD_NEWS_VISIBILITY, news.getVisibility() != null ? news.getVisibility() : false);
            doc.addField(SolrNewsRepresenter.FIELD_NEWS_IS_GENERAL, news.isGeneralNews() != null ? news.isGeneralNews() : false);
            doc.addField(SolrNewsRepresenter.FIELD_IS_BLOG, news.getBlog() != null ? news.getBlog() : false);
            doc.addField(SolrNewsRepresenter.FIELD_CREATION_DATE, news.getCreationTime());
            if (news.getViews() != null && news.getViews().size() > 0) {
                doc.addField(SolrNewsRepresenter.FIELD_COMMENTS, news.getViews().size());
            }
            if (news.getLocation() != null) {
                doc.addField(SolrNewsRepresenter.FIELD_LOCATION_ID, news.getLocation().getObjectID());
                doc.addField(SolrNewsRepresenter.FIELD_LOCATION, news.getLocation().getName());
                doc.addField(SolrNewsRepresenter.FIELD_LOCATION_ID_NAME, news.getLocation().getObjectID() + SolrNewsRepresenter.SPLIT + news.getLocation().getName());
            }

            String text = "";
            if (news.isGeneralNews() != null && news.getBlog() != null) {
                if (news.isGeneralNews() != null && news.isGeneralNews()) {
                    if (news.getUser().getCompany().getObjectID() == 8934) {
                        if (news.getBlog() != null && news.getBlog()) {
                            text = "Thought Leadership";
                        } else {
                            text = "News";
                        }
                    } else if (news.getUser().getCompany().getObjectID() == 5377) {
                        if (news.getBlog() != null && news.getBlog()) {
                            text = "Opinion";
                        } else {
                            text = "News";
                        }
                    }
                } else {
                    if (news.getUser().getCompany().getObjectID() == 5377) {
                        if (news.getBlog() != null && news.getBlog()) {
                            text = "Network Opinion";
                        } else {
                            text = "Network News";
                        }
                    } else if (news.getUser().getCompany().getObjectID() == 8934) {
                        if (news.getBlog() != null && news.getBlog()) {
                            text = "Network Discussion";
                        } else {
                            text = "Network News";
                        }
                    }
                }
            }

            doc.addField(SolrNewsRepresenter.FIELD_NEWS_TYPE, text);
            if (news.isGeneralNews() != null && news.isGeneralNews()) {
                if (news.getOwner() != null && !"".equals(news.getOwner())) {
                    String userName = news.getOwner();
                    if (news.getAnonym() != null && news.getAnonym()) {
                        userName = "Anonymous";
                    }
                    doc.addField(SolrNewsRepresenter.FIELD_NEWS_OWNER, userName);
                } else {
                    if (news.getAnonym() != null && news.getAnonym()) {
                        doc.addField(SolrNewsRepresenter.FIELD_NEWS_OWNER, "Anonymous");
                    }

                }
            } else {
                String userName = "";
                if (news.getUser() != null) {
                    userName = news.getUser().getFullName();
                }
                if (news.getAnonym() != null && news.getAnonym()) {
                    userName = "Anonymous";

                }
                doc.addField(SolrNewsRepresenter.FIELD_NEWS_OWNER, userName);
            }


            if (news.getUser() != null) {
                String userName = news.getUser().getFullName();
                Integer userId = news.getUser().getObjectID();
                if (news.getAnonym() != null && news.getAnonym()) {
                    userName = "Anonymous";
                    userId = 0;
                }
                doc.addField(SolrNewsRepresenter.FIELD_USER_ID, userId);
                doc.addField(SolrNewsRepresenter.FIELD_USER, userName);
                doc.addField(SolrNewsRepresenter.FIELD_USER_ID_NAME, userId + SolrNewsRepresenter.SPLIT + userName);
            }
            docs.add(doc);
            docs = commit100ToSolr(solr, docs, false);

        }
        commit100ToSolr(solr, docs, true);
    }

    private Integer[] getObjectIDs(EdsObject... objects) {
        if (objects == null || objects.length == 0) {
            return null;
        }
        final Integer[] ids = new Integer[objects.length];
        int i = 0;

        for (EdsObject object : objects) {
            ids[i++] = object.getObjectID();
        }
        return ids;
    }

    private <T extends EdsObject> Integer[] getObjectIDs(List<T> objects) {
        if (objects == null || objects.isEmpty()) {
            return null;
        }
        final Integer[] ids = new Integer[objects.size()];
        int i = 0;

        for (EdsObject object : objects) {
            ids[i++] = object.getObjectID();
        }
        return ids;
    }

    @Override
    public void removeCompanyLeadByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrContactRepresenter.FIELD_CONTACT_ID + ":", SOLR_CONTACT_CORE, ids);
    }

    @Override
    public void removeCompanyNewsByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrNewsRepresenter.FIELD_NEWS_ID + ":", SOLR_NEWS_CORE, ids);
    }

    private void removeMultiEntry(String queryStart, String solrCore, Integer[] ids) throws SolrServerException, SolrException, IOException {
        Integer companyID = Integer.valueOf(SecurityContext.getInstance().getCompanyId());
        StringBuilder removeQuery = new StringBuilder(" " + SolrNewsRepresenter.FIELD_COMPANY_ID + " : " + companyID + " AND " + queryStart);
        if (ids != null && ids.length > 0) {
            if (ids.length == 1) {
                removeQuery.append(ids[0]);
                removeEntity(removeQuery.toString(), solrCore);
            } else if (ids.length > 1) {
                int limit = 0;
                for (int i = 0; i < Math.ceil((double) ids.length / (double) 1000); i++) {
                    int start = i * 1000;
                    limit = ids.length - start < 1000 ? ids.length - start : 1000;
                    String delimitr = "";
                    removeQuery.append("(");
                    for (int s = start; s < start + limit; s++) {
                        if (ids[s] != null && !"".equals(ids[s])) {
                            removeQuery.append(delimitr).append(ids[s]);
                            delimitr = " ";
                        }
                    }
                    removeQuery.append(")");
                    removeEntity(removeQuery.toString(), solrCore);
                    removeQuery = new StringBuilder(queryStart);
                }
            }
        }
    }

    @Override
    @Transactional
    public void addContactToIndex(EdsCrmContact... crmContacts) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (crmContacts != null && crmContacts.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCrmContact crmContact : crmContacts) {
                if (crmContact != null) {
                    List<EdsMailList> edsMailLists = mailListManager.getContactsEdsMailingLists(crmContact.getObjectID());
                    addContactSolrDocument(docs, crmContact, companyID, edsMailLists);
                    idsToRemove.add(crmContact.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeCompanyCrmContactBuIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeCompanyCrmContactBuIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

//    @Override
//    public void addContactToIndexWithOwnSession(org.hibernate.Session session, EdsCrmContact... crmContacts) throws SolrServerException, SolrException, IOException {
//        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
//        Integer[] ids = getObjectIDs(crmContacts);
//        Integer companyID = SecurityContext.getCompanyID();
//
//        List<SolrInputDocument> docs = new ArrayList<>();
//        List<Integer> idsToRemove = new ArrayList<>();
//        for (Integer crmContactID : ids) {
//            EdsCrmContact crmContact = (EdsCrmContact) session.get(EdsCrmContact.class, crmContactID);
//            List<EdsMailList> edsMailLists = mailListManager.getContactsEdsMailingLists(crmContact.getObjectID());
//            ;
//            addContactSolrDocument(docs, crmContact, companyID, edsMailLists);
//            if (crmContact != null) {
//                idsToRemove.add(crmContactID);
//            }
//            if (idsToRemove.size() == SOLR_LIMIT) {
//                removeCompanyCrmContactBuIds(idsToRemove.toArray(new Integer[]{}));
//                idsToRemove.clear();
//            }
//            docs = commit100ToSolr(solr, docs, false);
//        }
//        removeCompanyCrmContactBuIds(idsToRemove.toArray(new Integer[]{}));
//        idsToRemove.clear();
//        commit100ToSolr(solr, docs, true);
//    }

    /**
     * After move to new Apache Solr version remove this is mthod
     *
     * @param docs
     * @param crmContact
     * @param companyID
     * @param edsMailLists
     */
    private void addContactSolrDocument(final List<SolrInputDocument> docs, EdsCrmContact crmContact, Integer companyID, List<EdsMailList> edsMailLists) {
        long start = System.currentTimeMillis();
        Set<EdsContactCategory> categories = crmContact.getCategories();
        boolean isLead = false;
        boolean isCandidate = false;
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + crmContact.getObjectID();
        doc.addField(SolrContactRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrContactRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrContactRepresenter.FIELD_CONTACT_ID, crmContact.getObjectID());
        doc.addField(SolrContactRepresenter.FIELD_CONTACT_NAME, crmContact.getName());
        doc.addField(SolrContactRepresenter.FIELD_IS_PRIMARY_CONTACT, crmContact.getPrimaryContact());
        doc.addField(SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE, crmContact.getName());
        doc.addField(SolrContactRepresenter.FIELD_LEAD_NAME_COMPOSITE, crmContact.getName());
        doc.addField(SolrContactRepresenter.FIELD_FIRST_NAME, crmContact.getFirstName());
        doc.addField(SolrContactRepresenter.FIELD_MIDDLE_NAME, crmContact.getMiddleName());
        doc.addField(SolrContactRepresenter.FIELD_LAST_NAME, crmContact.getLastName());
        doc.addField(SolrContactRepresenter.FIELD_REF_IND_NUMBER, crmContact.getRefIndNumber());
        doc.addField(SolrContactRepresenter.FIELD_TITLE, crmContact.getTitle());
        doc.addField(SolrContactRepresenter.FIELD_JOB_TITLE, crmContact.getJobTitles());
        doc.addField(SolrContactRepresenter.FIELD_EXTENSION, EdsCrmContactItemParams.getFirstItemParamValue(crmContact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.EXTENSION));
        doc.addField(SolrContactRepresenter.FIELD_PRIMARY_EMAIL, crmContact.getPrimaryEmail());
        doc.addField(SolrContactRepresenter.FIELD_PRIMARY_PHONE, crmContact.getPrimaryPhone());
        doc.addField(SolrContactRepresenter.FIELD_FAX, EdsCrmContactItemParams.getFirstItemParamValue(crmContact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX));
        doc.addField(SolrContactRepresenter.FIELD_MOBILE, EdsCrmContactItemParams.getFirstItemParamValue(crmContact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.MOBILE));
        doc.addField(SolrContactRepresenter.FIELD_WORK_PHONE, EdsCrmContactItemParams.getFirstItemParamValue(crmContact.getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.WORK));
        doc.addField(SolrContactRepresenter.FIELD_WEBSITE, EdsCrmContactItemParams.getFirstItemParamValue(crmContact.getItemParams(EdsCrmContactItemParams.WEBSITE), true));
        doc.addField(SolrContactRepresenter.FIELD_CONTACT_TYPE, crmContact.getContactType());
        doc.addField(SolrContactRepresenter.FIELD_DEPARTMENT, crmContact.getDepartment());
        doc.addField(SolrContactRepresenter.FIELD_UPDATE_DATE, crmContact.getAuditInfo().getModificationDate());
        doc.addField(SolrContactRepresenter.FIELD_CREATION_DATE, crmContact.getAuditInfo().getCreationDate());
        doc.addField(SolrContactRepresenter.FIELD_DATE_OF_BIRTH, crmContact.getDateOfBirth());
        doc.addField(SolrContactRepresenter.FIELD_REPORTS_TO, crmContact.getReportsTo());
        doc.addField(SolrContactRepresenter.FIELD_REPORTS_TO_ID, crmContact.getReportsToId());
        doc.addField(SolrContactRepresenter.FIELD_EMAIL_ALLOWED, crmContact.getEmailOptOut());
        doc.addField(SolrContactRepresenter.FIELD_GOOGLEID, crmContact.getGoogleId());
        doc.addField(SolrContactRepresenter.FIELD_LEAD_KANBAN_ORDER, crmContact.getKanbanorder());
        if (crmContact.getCreator() != null) {
            doc.addField(SolrContactRepresenter.FIELD_CREATOR_ID, crmContact.getCreator().getObjectID());
            doc.addField(SolrContactRepresenter.FIELD_CREATOR_NAME, crmContact.getCreator().getFullName());
            doc.addField(SolrContactRepresenter.FIELD_CREATOR_ID_NAME, SolrUtils.getIdName(crmContact.getCreator().getObjectID(), crmContact.getCreator().getFullName()));
        }
        if (crmContact.getAuditInfo() != null && crmContact.getAuditInfo().getModifiedBy() != null) {
            doc.addField(SolrContactRepresenter.FIELD_UPDATER_ID, crmContact.getAuditInfo().getModifiedBy().getObjectID());
            doc.addField(SolrContactRepresenter.FIELD_UPDATER_NAME, crmContact.getAuditInfo().getModifiedBy().getFullName());
            doc.addField(SolrContactRepresenter.FIELD_UPDATER_ID_NAME, SolrUtils.getIdName(crmContact.getAuditInfo().getModifiedBy().getObjectID(), crmContact.getAuditInfo().getModifiedBy().getFullName()));
        }
        StringBuilder categoryNames = new StringBuilder();
        if (categories != null && categories.size() > 0) {
            for (EdsContactCategory category : categories) {
                doc.addField(SolrContactRepresenter.FIELD_CATEGORY_ID, category.getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_CATEGORY_NAME, category.getName());
                doc.addField(SolrContactRepresenter.FIELD_CATEGORY_ID_NAME, category.getObjectID() + SolrTaskRepresenter.SPLIT + category.getName());
                if ("".contentEquals(categoryNames)) {
                    categoryNames.append(category.getName() != null ? category.getName() : "");
                }
            }
            doc.addField(SolrContactRepresenter.FIELD_CATEGORY_NAME_SORT, categoryNames.toString());
        }
        if (edsMailLists != null && edsMailLists.size() > 0) {
            for (EdsMailList edsMailList : edsMailLists) {
                doc.addField(SolrContactRepresenter.FIELD_MAIL_LIST_ID, edsMailList.getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_MAIL_LIST_NAME, edsMailList.getName());
                doc.addField(SolrContactRepresenter.FIELD_MAIL_LIST_ID_NAME, edsMailList.getObjectID() + SolrTaskRepresenter.SPLIT + edsMailList.getName());
            }
        }
        if (crmContact.getOwner() != null) {
            doc.addField(SolrContactRepresenter.FIELD_OWNER_ID, crmContact.getOwner().getObjectID());
            doc.addField(SolrContactRepresenter.FIELD_OWNER_NAME, crmContact.getOwner().getFullName());
            doc.addField(SolrContactRepresenter.FIELD_OWNER_ID_NAME, crmContact.getOwner().getObjectID() + SolrTaskRepresenter.SPLIT + crmContact.getOwner().getFullName());
        }
        if (crmContact.getCrmAccount() != null) {
            doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID, crmContact.getCrmAccount().getObjectID());
            doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_NAME, crmContact.getCrmAccount().getName());
            doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID_NAME, SolrUtils.getIdName(crmContact.getCrmAccount()));
            doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_NUMBER, crmContact.getCrmAccount().getNumber());
            if (crmContact.getCrmAccount().getIndustry() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY, crmContact.getCrmAccount().getIndustry().getName());
                doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY_ID, crmContact.getCrmAccount().getIndustry().getObjectID());
            }

            /*if (crmContact.getCrmAccount().getOwner() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_OWNER_ID, crmContact.getCrmAccount().getOwner().getObjectID());
            }*/
            if (crmContact.getCrmAccount().getOwners() != null) {
                crmContact.getCrmAccount().getOwners().forEach(owner -> doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_OWNER_ID, owner.getObjectID()));
            }
            //Adding ACCOUNT_TYPEs like CUSTOMER,SUPPLIER etc
            if (crmContact.getCrmAccount().getAccountTypes() != null) {
                for (EdsReference accountType : crmContact.getCrmAccount().getAccountTypes()) {
                    doc.addField(SolrContactRepresenter.FIELD_CRM_ACCOUNT_TYPE, accountType.getCode());
                }
            }
        }
        if (Boolean.TRUE.equals(crmContact.isAccessEnabled())) {
            doc.addField(SolrContactRepresenter.FIELD_ACCESS_ENABLED, crmContact.isAccessEnabled());
            EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(crmContact.getObjectID());
            if (clientContact != null) {
                doc.addField(SolrContactRepresenter.FIELD_CLIENTCONTACT_ID, clientContact.getObjectID());
            }
        } else {
            doc.addField(SolrContactRepresenter.FIELD_ACCESS_ENABLED, Boolean.FALSE);
        }
        if (crmContact.is(EdsCrmContact.CANDIDATE)) {
            if (crmContact.getLeadStatus() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS, crmContact.getLeadStatus().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID, crmContact.getLeadStatus().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE, SolrUtils.getIdName(crmContact.getLeadStatus().getObjectID(), crmContact.getLeadStatus().getCode()));
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME, crmContact.getLeadStatus().getObjectID() + SolrContactRepresenter.SPLIT + crmContact.getLeadStatus().getCode() + SolrContactRepresenter.SPLIT + crmContact.getLeadStatus().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_CODE, crmContact.getLeadStatus().getCode());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_SORDER, crmContact.getLeadStatus().getSorder());
            }
            if (crmContact.getLeadSource() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE, crmContact.getLeadSource().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID, crmContact.getLeadSource().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE, SolrUtils.getIdName(crmContact.getLeadSource().getObjectID(), crmContact.getLeadSource().getCode()));
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME, crmContact.getLeadSource().getObjectID() + SolrContactRepresenter.SPLIT + crmContact.getLeadSource().getCode());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_CODE, crmContact.getLeadSource().getCode());
            }
            doc.addField(SolrContactRepresenter.FIELD_NUMBER, crmContact.getNumber());
            doc.addField(SolrContactRepresenter.FIELD_WORK_EXPERIENCE, crmContact.getWorkExperience());
            doc.addField(SolrContactRepresenter.FIELD_WORK_EXPERIENCE_MONTH_YEAR, crmContact.getWorkExperienceMonthOrYear());
            doc.addField(SolrContactRepresenter.FIELD_CURRENT_EMPLOYER, crmContact.getCurrentEmployer());
            doc.addField(SolrContactRepresenter.FIELD_EXPECTED_SALARY, crmContact.getExpectedSalary());
            doc.addField(SolrContactRepresenter.FIELD_IS_SHORT_LIST, crmContact.getShortList());
            doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_SKILLS, crmContact.getSkills());
            if (crmContact.getCandidateProject() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_PROJECT_ID, crmContact.getCandidateProject().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_PROJECT, crmContact.getCandidateProject().getName());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_PROJECT_ID_NAME, SolrUtils.getIdName(crmContact.getCandidateProject().getObjectID(), crmContact.getCandidateProject().getName()));
            }
            if (crmContact.getPrefferedLocation() != null) {
                doc.addField(SolrContactRepresenter.FIELD_PREFERRED_LOCATION, crmContact.getPrefferedLocation().getAsSelectItem().getName());
                doc.addField(SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID, crmContact.getPrefferedLocation().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_PREFERRED_LOCATION_ID_NAME, SolrUtils.getIdName(crmContact.getPrefferedLocation().getObjectID(), crmContact.getPrefferedLocation().getAsSelectItem().getName()));
            }
            if (crmContact.getCandidateDepartment() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT, crmContact.getCandidateDepartment().getName());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT_ID, crmContact.getCandidateDepartment().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT_ID_NAME, SolrUtils.getIdName(crmContact.getCandidateDepartment().getObjectID(), crmContact.getCandidateDepartment().getAsSelectItem().getName()));
            }
            if (crmContact.getCandidatePosition() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_POSITION, crmContact.getCandidatePosition().getName());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_POSITION_ID, crmContact.getCandidatePosition().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_POSITION_ID_NAME, SolrUtils.getIdName(crmContact.getCandidatePosition().getObjectID(), crmContact.getCandidatePosition().getAsSelectItem().getName()));
            }


            if (crmContact.getVacancies().size() > 0) {
                for (EdsVacancy vacancy : crmContact.getVacancies()) {
                    doc.addField(SolrContactRepresenter.FIELD_VACANCY_ID, vacancy.getObjectID());
                    doc.addField(SolrContactRepresenter.FIELD_VACANCY_NAME, !vacancy.getName().equals("") ? vacancy.getName() : vacancy.getPosition().getName());
                    doc.addField(SolrContactRepresenter.FIELD_VACANCY_ID_NAME, SolrUtils.getIdName(vacancy));
                }
            }
            if (crmContact.getLeadStatus() != null) {
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_STATUS_ID, crmContact.getLeadStatus().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_STATUS, crmContact.getLeadStatus().getName());
                doc.addField(SolrContactRepresenter.FIELD_CANDIDATE_STATUS_ID_NAME, SolrUtils.getIdName(crmContact.getLeadStatus().getObjectID(), crmContact.getLeadStatus().getName()));
            }
            isCandidate = true;
        } else if (crmContact.is(EdsCrmContact.LEAD_CONTACT)) {
            isLead = true;
            if (crmContact.getLeadAssignee() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID, crmContact.getLeadAssignee().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE, crmContact.getLeadAssignee().getFullName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME, SolrUtils.getIdName(crmContact.getLeadAssignee().getObjectID(), crmContact.getLeadAssignee().getFullName()));
            }
            if (crmContact.getLeadBackupAssignee() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID, crmContact.getLeadBackupAssignee().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE, crmContact.getLeadBackupAssignee().getFullName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID_NAME, SolrUtils.getIdName(crmContact.getLeadBackupAssignee().getObjectID(), crmContact.getLeadBackupAssignee().getFullName()));
            }
            if (crmContact.getLeadRating() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_RATING_ID, crmContact.getLeadRating().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_RATING, crmContact.getLeadRating().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_RATING_CODE, crmContact.getLeadRating().getCode());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_RATING_ID_CODE, SolrUtils.getIdName(crmContact.getLeadRating().getObjectID(), crmContact.getLeadRating().getCode()));
            }
            if (crmContact.getLeadSource() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID, crmContact.getLeadSource().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE, crmContact.getLeadSource().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_CODE, crmContact.getLeadSource().getCode());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE, SolrUtils.getIdName(crmContact.getLeadSource().getObjectID(), crmContact.getLeadSource().getCode()));
                doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_ID_CODE_NAME, crmContact.getLeadSource().getObjectID() + SolrContactRepresenter.SPLIT + crmContact.getLeadSource().getCode() + SolrContactRepresenter.SPLIT + crmContact.getLeadSource().getName());

            }
            doc.addField(SolrContactRepresenter.FIELD_LEAD_SOURCE_OTHER, crmContact.getOtherLeadSource());
            if (crmContact.getLeadStatus() != null) {
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID, crmContact.getLeadStatus().getObjectID());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS, crmContact.getLeadStatus().getName());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_CODE, crmContact.getLeadStatus().getCode());
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE, SolrUtils.getIdName(crmContact.getLeadStatus().getObjectID(), crmContact.getLeadStatus().getCode()));

                String leadStatusNameForFacet = crmContact.getLeadStatus().getObjectID() + SolrContactRepresenter.SPLIT + crmContact.getLeadStatus().getCode();//.append(SolrContactRepresenter.SPLIT);
                //if status name changed then we must reindex all lead, so we cant index status name
//                leadStatusNameForFacet.append(crmContact.getLeadStatus().getName()).append(SolrContactRepresenter.SPLIT);
                /*leadStatusNameForFacet.append(crmContact.getLeadStatus().getSorder()).append(SolrContactRepresenter.SPLIT);

                We cant add color to solr because if color changed we need to update all leads
                if(crmContact.getLeadStatus().getReferenceColor()!=null) {
                    leadStatusNameForFacet.append(crmContact.getLeadStatus().getReferenceColor().getObjectID()).append(SolrContactRepresenter.SPLIT);
                    leadStatusNameForFacet.append(crmContact.getLeadStatus().getReferenceColor().getName()).append(SolrContactRepresenter.SPLIT);
                    leadStatusNameForFacet.append(crmContact.getLeadStatus().getReferenceColor().getHex());
                }*/
                doc.addField(SolrContactRepresenter.FIELD_LEAD_STATUS_ID_CODE_NAME, leadStatusNameForFacet);
            }
        }
        if (crmContact.getCampaign() != null) {
            doc.addField(SolrContactRepresenter.FIELD_CAMPAIGN_ID, crmContact.getCampaign().getObjectID());
            doc.addField(SolrContactRepresenter.FIELD_CAMPAIGN_NAME, crmContact.getCampaign().getName());
            doc.addField(SolrContactRepresenter.FIELD_CAMPAIGN_ID_NAME, crmContact.getCampaign().getObjectID() + SolrTaskRepresenter.SPLIT + crmContact.getCampaign().getName());
        }
        Address primaryAddress = crmContact.getPrimaryAddressFromAll();
        if (primaryAddress != null) {
            if (primaryAddress.getCountryId() != null) {
                doc.addField(SolrContactRepresenter.FIELD_COUNTRY_ID, primaryAddress.getCountryId());
                doc.addField(SolrContactRepresenter.FIELD_COUNTRY_NAME, primaryAddress.getCountry());
                doc.addField(SolrContactRepresenter.FIELD_COUNTRY_CODE, primaryAddress.getCountryCode());
                doc.addField(SolrContactRepresenter.FIELD_COUNTRY_ID_CODE, primaryAddress.getCountryId() + SolrTaskRepresenter.SPLIT + primaryAddress.getCountryCode());
                doc.addField(SolrContactRepresenter.FIELD_COUNTRY_ID_CODE_NAME, primaryAddress.getCountryId() + SolrTaskRepresenter.SPLIT + primaryAddress.getCountryCode() + SolrTaskRepresenter.SPLIT + primaryAddress.getCountry());
            }
            if (primaryAddress.getStateId() != null) {
                doc.addField(SolrContactRepresenter.FIELD_STATE_ID, primaryAddress.getStateId());
                doc.addField(SolrContactRepresenter.FIELD_STATE_NAME, primaryAddress.getState());
                doc.addField(SolrContactRepresenter.FIELD_STATE_ID_NAME, primaryAddress.getStateId() + SolrTaskRepresenter.SPLIT + primaryAddress.getState());
            }
            doc.addField(SolrContactRepresenter.FIELD_CITY, primaryAddress.getCity());
            doc.addField(SolrContactRepresenter.FIELD_STREET, primaryAddress.getAddress());
            doc.addField(SolrContactRepresenter.FIELD_STREET2, primaryAddress.getAddressb());
            doc.addField(SolrContactRepresenter.FIELD_POST_CODE, primaryAddress.getZipCode());
            doc.addField(SolrContactRepresenter.FIELD_LONGITUDE, primaryAddress.getLongitude());
            doc.addField(SolrContactRepresenter.FIELD_LATITUDE, primaryAddress.getLatitude());
        }
        if (crmContact.getCustomFields() != null) {
            CustomFieldsUtils.setInSolrCustomFields(doc, crmContact.getCustomFields());
        }
        doc.addField(SolrContactRepresenter.FIELD_IS_FAVOURITED, crmContact.getFavourited());
        docs.add(doc);
        logger.info(String.format("cId=%s, %s", companyID, isLead ? "LEAD -> " : (isCandidate ? "Candidate -> " : "Contact -> ") + "_" + crmContact.getObjectID() + " is going to be added to solr" + " time=" + (System.currentTimeMillis() - start) + "ms"));
    }

    private List<SolrInputDocument> commit100ToSolr(SolrClient solr, List<SolrInputDocument> docs, boolean commit) throws IOException, SolrServerException {
        if (docs != null && (docs.size() >= SOLR_LIMIT || (docs.size() > 0 && commit))) {
            solr.add(docs);
            solr.commit();
            return new ArrayList<>();
        } else {
            return docs;
        }
    }

    private boolean removeAndAdd100ToSolr(SolrClient solr, List<SolrInputDocument> docs, String removeQuery, String removeIDsStr, boolean commit) throws IOException, SolrServerException {
        boolean commited = false;
        if (docs != null && (docs.size() >= SOLR_LIMIT || (docs.size() > 0 && commit))) {
            solr.deleteByQuery(removeQuery + " (" + removeIDsStr + ") ");
            solr.add(docs);
            solr.commit();
            commited = true;
        }
        return commited;
    }

    @Override
    public void removeCrmAccountByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID + ":", SOLR_CRM_ACCOUNT_CORE, ids);
    }

    public void removeCompanyCrmAccount(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            removeEntity(SolrCrmAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID, SOLR_CRM_ACCOUNT_CORE);
        }
    }

    @Override
    public void removeCompanyCrmContactBuIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrContactRepresenter.FIELD_CONTACT_ID + ":", SOLR_CONTACT_CORE, ids);
    }

    @Override
    public void deleteEvents(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrEventRepresenter.FIELD_EVENT_ID + ":", SOLR_EVENT_CORE, ids);
    }

    public void removeProductsServicesByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrProductServiceRepresenter.FIELD_PRODUCT_ID + ":", SOLR_PRODUCTS_SERVICES_CORE, ids);
    }

    @Override
    public void removeCourseBookingByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID + ":", SOLR_COURSE_BOOKING_CORE, ids);
    }

    @Override
    public void removeCourseSchedulesByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID + ":", SOLR_COURSE_SCHEDULE_CORE, ids);
    }

    @Override
    public void removeEmployeesByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID + ":", SOLR_EMPLOYEE_CORE, ids);
    }

//    @Override
//    public void removeShippingDataByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
//        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":", SOLR_SHIPPING_DATA_CORE, ids);
//    }

    @Override
    public void removeLeaveRequestByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrLeaveRequestConst.FIELD_OBJECT_ID + ":", SOLR_LEAVE_REQUEST_CORE, ids);
    }

    @Override
    public void removeSinglePayrunByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID + ":", SOLR_SINGLE_PAYRUN_CORE, ids);
    }

    @Override
    public void removeGroupPayrunByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID + ":", SOLR_GROUP_PAYRUN_CORE, ids);
    }

    @Override
    public void removeCashAdvanceByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID + ":", SOLR_CASH_ADVANCE_CORE, ids);
    }

    public void removeExpenseReportByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrExpenseReportRepresenter.FIELD_REPORT_ID + ":", SOLR_EXPENSE_REPORT_CLAIMS_CORE, ids);
    }

    @Override
    public void removeOpportunitiesByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID + ":", SOLR_OPPORTUNITY_CORE, ids);
    }

    public void removeAdditionalPaymentByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID + ":", SOLR_ADDITIONAL_PAYMENT_CORE, ids);
    }

    public void removeCompanyCrmContact(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeCrmContactQuery = SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND -(" + SolrContactRepresenter.FIELD_CONTACT_TYPE + ":" + EdsCrmContact.LEAD_CONTACT + ") AND -(" + SolrContactRepresenter.FIELD_CONTACT_TYPE + ":" + EdsCrmContact.CANDIDATE + ")";
            removeEntity(removeCrmContactQuery, SOLR_CONTACT_CORE);
        }
    }

    @Override
    public void removeAllLead(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeLeadQuery = SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrContactRepresenter.FIELD_CONTACT_TYPE + ":" + EdsCrmContact.LEAD_CONTACT;
            removeEntity(removeLeadQuery, SOLR_CONTACT_CORE);
        }
    }

    @Override
    public void removeAllCandidate(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeLeadQuery = SolrContactRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrContactRepresenter.FIELD_CONTACT_TYPE + ":" + EdsCrmContact.CANDIDATE;
            removeEntity(removeLeadQuery, SOLR_CONTACT_CORE);
        }
    }

    public void removeCompanyNews(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeNewsQuery = SolrNewsRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeNewsQuery, SOLR_NEWS_CORE);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void addOpportunityToIndex(EdsOpportunity... opportunities) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_OPPORTUNITY_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (opportunities != null && opportunities.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            EdsCompany edsCompany = companyManager.get(companyID);
            String locale = edsCompany.getLocale();
            List<Integer> opportunityAttachmentsIds = fileHeaderManager.getEntityIDsByFileType(Constants.F_OPPORTUNITY);
            for (EdsOpportunity opportunity : opportunities) {
                if (opportunity != null) {
                    List<EdsRelation> relationList = relationManager.getAllRelations(EdsRelation.TYPE_OPPORTUNITY, opportunity.getObjectID());
                    docs.add(opportunity.getAsSolrDocument(companyID, relationList, locale, opportunityAttachmentsIds));
                    idsToRemove.add(opportunity.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeOpportunitiesByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeOpportunitiesByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void addEmployeeStepToIndex(EdsStepEmployee step) throws SolrServerException, SolrException, IOException {
        indexEmployeeStepList(Collections.singletonList(step), SecurityContext.getCompanyID());
    }

    @Override
    @Transactional
    public void addEventToIndex(EdsEvent... events) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (events != null && events.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            Map<Integer, Set<EdsUser>> mapOfUsers = eventManager.getEventSharedUsers(EdsEvent.getObjectIDs(Arrays.asList(events)));
            for (EdsEvent event : events) {
                if (event != null && (event.isDeleted() == null || !event.isDeleted())) {
                    List<EdsRelation> edsRelationList = relationManager.getAllRelations(EdsRelation.TYPE_EVENT, event.getObjectID());
                    if (mapOfUsers.containsKey(event.getObjectID()) && mapOfUsers.get(event.getObjectID()).size() > 0) {
                        docs.add(event.wrapToSolrDocument(mapOfUsers.get(event.getObjectID()), edsRelationList));
                    } else {
                        docs.add(event.wrapToSolrDocument(edsRelationList));
                    }
                    idsToRemove.add(event.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        deleteEvents(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            deleteEvents(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    private BigDecimal getAverageCost(EdsItem item) {

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        Object beginningBalance = itemStockManager.getInventoryTransactionBalanceToDate(item.getObjectID(), null, null);

        BigDecimal bBalance = null;
        BigDecimal bQty = null;
        BigDecimal bResult = null;
        if (beginningBalance != null) {
            bQty = (((Object[]) beginningBalance)[0] != null && !((Object[]) beginningBalance)[0].equals(ZERO)) ? (BigDecimal) ((Object[]) beginningBalance)[0] : null;
            bBalance = (((Object[]) beginningBalance)[1] != null && !((Object[]) beginningBalance)[1].equals(ZERO)) ? (BigDecimal) ((Object[]) beginningBalance)[1] : null;
        }

        if (bBalance != null && bQty != null && bQty.compareTo(BigDecimal.ZERO) != 0) {
            bResult = bBalance.divide(bQty, calculationScale, RoundingMode.HALF_UP);
        }
        return bResult;
    }

    @Override
    @Transactional
    public void addProductsServicesToIndex(EdsItem... items) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_PRODUCTS_SERVICES_CORE);
        boolean isCustomSubItemsEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CUSTOM_INVOICE_ITEM_PRODUCT_DESCRIPTION_ENABLED);
        if (items != null && items.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsItem item : items) {
                if (item != null) {
                    BigDecimal averageCost = getAverageCost(item);
                    averageCost = averageCost != null ? averageCost.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP) : averageCost;
                    item.setAverageCost(averageCost);
                    docs.add(item.wrapToSolrDocument(isCustomSubItemsEnabled));
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeProductsServicesByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeProductsServicesByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    public void addCourseBookingToIndex(EdsCourseBooking... courseBookings) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_BOOKING_CORE);
        if (courseBookings != null && courseBookings.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCourseBooking courseBooking : courseBookings) {
                if (courseBooking != null) {
                    docs.add(courseBooking.wrapToSolrDocument());
                    idsToRemove.add(courseBooking.getObjectID());

                    if (docs.size() >= SOLR_LIMIT) {
                        removeCourseBookingByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }

            removeCourseBookingByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    @Transactional
    public void addCourseScheduleToIndex(EdsCourseSchedule... courseSchedule) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_COURSE_SCHEDULE_CORE);
        if (courseSchedule != null && courseSchedule.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCourseSchedule courseSheduleItem : courseSchedule) {
                if (courseSheduleItem != null) {
                    SolrInputDocument doc = courseSheduleItem.wrapCourseScheduleToSolrDocument();
                    doc.addField(SolrCourseScheduleRepresenter.FIELD_COUNT_OF_STUDENT, courseScheduleStudentManager.getCourseScheduleStudentCount(courseSheduleItem.getObjectID()));
                    doc.addField(SolrCourseScheduleRepresenter.FIELD_COUNT_OF_CONFIRMED_STUDENT, courseScheduleStudentManager.getCourseScheduleConfirmedStudentCount(courseSheduleItem.getObjectID()));
                    docs.add(doc);
                    idsToRemove.add(courseSheduleItem.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeCourseSchedulesByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeCourseSchedulesByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    @Transactional
    public void addEmployeeToIndex(EdsEmployee... employees) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        if (employees != null && employees.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsEmployee employee : employees) {
                if (employee != null) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    SolrInputDocument doc = employee.indexToSolr(companyID, userBankAccount, isIntegerNumberEnabled);
                    docs.add(doc);
                    logger.info("EMPLOYEE is added to solr: cId=" + companyID + ", eId=" + employee.getObjectID());
                    idsToRemove.add(employee.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeEmployeesByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeEmployeesByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    public void addEmployeeToIndex(Boolean isIntegerEmployeeCodeEnabled, EdsEmployee... employees) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (employees != null && employees.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsEmployee employee : employees) {
                if (employee != null) {
                    EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                    SolrInputDocument doc = employee.indexToSolr(companyID, userBankAccount, isIntegerEmployeeCodeEnabled);
                    docs.add(doc);
                    logger.info("EMPLOYEE is added to solr: cId=" + companyID + ", eId=" + employee.getObjectID());
                    idsToRemove.add(employee.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeEmployeesByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeEmployeesByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    public void addleaveRequestToIndex(EdsSickRequest... requests) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_LEAVE_REQUEST_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (requests != null && requests.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            for (EdsSickRequest request : requests) {
                if (request != null) {
                    SolrInputDocument doc = request.indexToSolr(companyID);
                    docs.add(doc);
                    logger.info("LEAVE REQUEST is added to solr: cId=" + companyID + ", rId=" + request.getObjectID());
                    if (docs.size() >= SOLR_LIMIT * 10) {
                        commitLeaveRequestToSolr(solr, docs);
                        docs.clear();
                    }
                }
            }
            commitLeaveRequestToSolr(solr, docs);
        }
    }

    private void commitLeaveRequestToSolr(SolrClient solr, List<SolrInputDocument> docs) throws IOException, SolrServerException {
        if (CollectionUtils.isNotEmpty(docs)) {
            solr.add(docs);
            solr.commit();
        }
    }

    @Override
    @Transactional
    public void addExpenseReportToIndex(EdsExpenseReport... expenseReport) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_EXPENSE_REPORT_CLAIMS_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (expenseReport != null && expenseReport.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsExpenseReport item : expenseReport) {
                if (item != null) {
                    docs.add(item.wrapToSolrDocument());
                    logger.info("ExpenseReport is added to solr: cId=" + companyID + ", eId=" + item.getObjectID());
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeExpenseReportByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeExpenseReportByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    public void indexVacancy(EdsVacancy edsVacancy) throws IOException, SolrServerException {
        indexVacancyList(Collections.singletonList(edsVacancy), SecurityContext.getCompanyID());
    }

    @Override
    public void indexVacancyList(List<EdsVacancy> edsVacancyList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_VACANCY_CORE);
        removeVacances(getObjectIDs(edsVacancyList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        Integer id = null;
        try {
            String ids = "";
            for (EdsVacancy edsVacancy : edsVacancyList) {
                if (edsVacancy.getDeleted() != null && edsVacancy.getDeleted()) {
                    continue;
                }
                id = edsVacancy.getObjectID();
                EdsReference jobType = null;
                if (edsVacancy.getFullPartTime() != null) {
                    jobType = referenceManager.get(edsVacancy.getFullPartTime());
                }
                EdsJobFamily jobFamily = null;
                if (edsVacancy.getJobFamily() != null) {
                    jobFamily = jobFamilyManager.get(edsVacancy.getJobFamily());
                }
                logger.info("Vacancy is added to solr: cId=" + companyID + ", eId=" + edsVacancy.getObjectID());
                solrDocs.add(edsVacancy.indexToSolr(jobType, jobFamily, companyID));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
            }
            logger.info("VacancyIDs is added to batch solr: cId=" + companyID + ", ids=" + ids);
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId >>>= " + companyID + ", VacancyId >>>= " + id + " Solr Index Exception. ----------------------->>>>=");
//            e.printStackTrace();
        }
    }

    @Override
    public void indexEmployeeStepList(List<EdsStepEmployee> edsStepEmployees, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_EMPLOYEE_STEP_CORE);
        removeEmployeeSteps(getObjectIDs(edsStepEmployees));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        Integer id = null;
        try {
            String ids = "";
            for (EdsStepEmployee stepEmployee : edsStepEmployees) {
                if (stepEmployee.isDeleted()) {
                    continue;
                }
                id = stepEmployee.getObjectID();
                logger.info("Employee Step is going to be added to solr: cId=" + companyID + ", stepEmpId=" + stepEmployee.getObjectID());
                solrDocs.add(stepEmployee.indexToSolr(companyID));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
            }
            logger.info("Employee StepIDs is added to batch solr: cId=" + companyID + ", ids=" + ids);
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + ", EmployeeStepID = " + id + " Solr Index Exception.");
//            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void addSinglePayrunToIndex(EdsPayslipTableItem... items) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_SINGLE_PAYRUN_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        Boolean isIntegerNumberEnabled = employeeManager.isIntegerEmployeeCodeEnabled();
        if (items != null && items.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsPayslipTableItem item : items) {
                if (item != null) {
                    docs.add(item.indexToSolr(companyID, isIntegerNumberEnabled));
                    logger.info("SINGLE_PAYRUN is added to solr: cId=" + companyID + ", id=" + item.getObjectID());
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeSinglePayrunByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeSinglePayrunByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    @Transactional
    public void addGroupPayrunToIndex(EdsPayslipTable... items) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_GROUP_PAYRUN_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (items != null && items.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsPayslipTable item : items) {
                if (item != null) {
                    docs.add(item.indexToSolr(companyID));
                    logger.info("GROUP_PAYRUN is added to solr: cId=" + companyID + ", id=" + item.getObjectID());
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeGroupPayrunByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeGroupPayrunByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    @Override
    @Transactional
    public void addCashAdvanceToIndex(EdsCashAdvance... items) throws SolrServerException, SolrException, IOException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CASH_ADVANCE_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (items != null && items.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsCashAdvance item : items) {
                if (item != null) {
                    item.setRemainingAmount(cashAdvanceManager.getCashAdvanceRemainingAmount(item.getObjectID()));
                    docs.add(item.indexToSolr(companyID));
                    logger.info("CashAdvance is added to solr: cId=" + companyID + ", id= " + item.getObjectID().toString());
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeCashAdvanceByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeCashAdvanceByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    public void addAdditionalPaymentToIndex(EdsAdditionalPayment... items) throws IOException, SolrServerException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_ADDITIONAL_PAYMENT_CORE);
        Integer companyID = SecurityContext.getCompanyID();
        if (items != null && items.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();
            for (EdsAdditionalPayment item : items) {
                if (item != null) {
                    docs.add(item.indexToSolr(companyID));
                    logger.info("AdditionalPayment is added to solr: cId=" + companyID + ", id= " + item.getObjectID().toString());
                    idsToRemove.add(item.getObjectID());
                    if (docs.size() >= SOLR_LIMIT) {
                        removeAdditionalPaymentByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeAdditionalPaymentByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    public void indexAddSaleInvoice(EdsSaleInvoice invoice) throws IOException, SolrServerException {
        Integer companyID = SecurityContext.getCompanyID();
        indexAddSaleInvoice(Collections.singletonList(invoice), companyID);
    }

    @Override
    public void indexAddSaleInvoice(EdsSaleInvoice invoice, Integer companyID) throws IOException, SolrServerException {
        indexAddSaleInvoice(Collections.singletonList(invoice), companyID);
    }

    @Override
    public void indexAddSaleInvoice(List<EdsSaleInvoice> invoiceList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEINVOICE_CORE);
//        removeSaleInvoiceByIds(getObjectIDs(invoiceList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        String removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + " : " + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":";
        String ids = "";
        try {
            logger.info("cId = " + companyID + " --- start SaleInvoice wrapping");
            for (EdsSaleInvoice invoice : invoiceList) {
                SolrInputDocument doc = invoice.wrapToSolrDocument(invoice, companyID);

                if (invoice.getOpportunityID() != null) {
                    EdsOpportunity opportunity = opportunityManager.get(invoice.getOpportunityID());
                    if (opportunity != null) {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, opportunity.getNumber());
                    }
                }
                ids += " " + invoice.getObjectID();
                solrDocs.add(doc);
                boolean commited = removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, false);
                if (commited) {
                    solrDocs = new ArrayList<>();
                    ids = "";
                }
            }
            logger.info("cId = " + companyID + " --- end SaleInvoice wrapping");
            removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- Sale Invoice Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddCase(EdsCase edsCase) throws IOException, SolrServerException {
        Integer companyID = SecurityContext.getCompanyID();
        indexAddCase(edsCase, companyID);
    }

    @Override
    public void indexAddCase(EdsCase edsCase, Integer companyID) throws IOException, SolrServerException {
        indexAddCase(Collections.singletonList(edsCase), companyID);
    }

    @Override
    public void indexAddCase(List<EdsCase> caseList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_CASE_CORE);
//        removeCompanyCaseByIds(getObjectIDs(caseList));
        String removeQuery = SolrCaseRepresenter.COMPANY_ID + " : " + companyID + " AND " + SolrCaseRepresenter.CASE_ID + ":";
        String ids = "";
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start Case wrapping");
            List<Integer> trackerIDs = caseManager.getTrackerIDsByCaseIDs(EdsObject.getObjectIDs(caseList));
            List<Integer> casesWithAttachments = getCaseTrackerIDsOnlyWithAttachments(trackerIDs);
            for (EdsCase edsCase : caseList) {
                List<EdsRelation> relationList = relationManager.getAllRelations(EdsRelation.TYPE_CASE, edsCase.getObjectID());
                solrDocs.add(edsCase.wrapToSolrDocument(companyID, casesWithAttachments, relationList));
                ids += " " + edsCase.getObjectID();

                boolean commited = removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, false);
                if (commited) {
                    solrDocs = new ArrayList<>();
                    ids = "";
                }
            }
            logger.info("cId = " + companyID + " --- end Case wrapping");
            removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- Case Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddSaleQuote(List<EdsSaleQuote> quoteList, Integer companyID, List<EdsPickList> pickList) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_SALEQUOTE_CORE);
//        removeCompanySaleQuoteByIds(getObjectIDs(quoteList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        String removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + " : " + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":";
        String ids = "";
        try {
            logger.info("cId = " + companyID + " --- start SaleQuote wrapping");
            for (EdsSaleQuote quote : quoteList) {
                Integer pickListID = null;
                if (pickList != null && pickList.size() != 0) {
                    for (EdsPickList pick : pickList) {
                        if (pick.getSaleQuote() != null && pick.getSaleQuote().getObjectID().equals(quote.getObjectID())) {
                            pickListID = pick.getObjectID();
                        }
                    }
                }
                SolrInputDocument doc = quote.wrapToSolrDocument(quote, companyID, pickListID);
                if (quote.getOpportunityID() != null) {
                    EdsOpportunity opportunity = opportunityManager.get(quote.getOpportunityID());
                    if (opportunity != null) {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, opportunity.getNumber());
                    }
                }
                ids += " " + quote.getObjectID();
                solrDocs.add(doc);
                boolean commited = removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, false);
                if (commited) {
                    solrDocs = new ArrayList<>();
                    ids = "";
                }
            }
            logger.info("cId = " + companyID + " --- end SaleQuote wrapping");
            removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- SaleQuote Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddShippingData(List<EdsShippingData> shippingDataList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_SHIPPING_DATA_CORE);
        removeCompanyShippingDataByIds(getObjectIDs(shippingDataList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start Shipping Data wrapping");
            for (EdsShippingData shippingData : shippingDataList) {
                Integer invoiceId = shippingDataManager.getGrnGdnRelatedInvoiceNumber(shippingData.getObjectID());
                EdsInvoice invoice = invoiceManager.get(invoiceId);

                SolrInputDocument doc = shippingData.wrapToSolrDocument(shippingData, companyID);

                doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER, invoice != null ? invoice.getNumber() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, invoice != null ? invoice.getObjectID() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, invoice != null ? invoice.getInvoiceDate() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_DUE_DATE, invoice != null ? invoice.getDueDate() : null);

                doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID, invoice != null && invoice.getStatus() != null ? invoice.getStatus().getObjectID() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, invoice != null && invoice.getStatus() != null ? invoice.getStatus().getName() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_ID_NAME, invoice != null && invoice.getStatus() != null ? invoice.getStatus().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + invoice.getStatus().getName() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_SORDER, invoice != null && invoice.getStatus() != null ? invoice.getStatus().getSorder() : null);
                doc.addField(SolrSaleInvoiceRepresenter.FIELD_STATUS_CODE, invoice != null && invoice.getStatus() != null ? invoice.getStatus().getCode() : null);

                if (shippingData.getQuote() != null && shippingData.getQuote().getObjectID() != null) {
                    EdsSaleQuote saleQuote = quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());
                    if (saleQuote != null) {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_GDN_IS_SALES_ORDER, saleQuote.isSalesOrder());

                    }
                }

                solrDocs.add(doc);
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
            }
            logger.info("cId = " + companyID + " --- end ShippingData wrapping");
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- ShippingData Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddCertificate(List<EdsCertificateOfEmployment> certificateList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_CERTIFICATE_CORE);
        removeCompanyCertificatesByIds(getObjectIDs(certificateList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start Certificate wrapping");
            for (EdsCertificateOfEmployment certificate : certificateList) {
                solrDocs.add(certificate.wrapToSolrDocument(certificate, companyID));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
            }
            logger.info("cId = " + companyID + " --- end Certificate wrapping");
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- Certificate Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddPosition(List<EdsPosition> positionList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_POSITION_CORE);
        removeCompanyCertificatesByIds(getObjectIDs(positionList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start Position wrapping");
            for (EdsPosition position : positionList) {
                solrDocs.add(position.wrapToSolrDocument(companyID, employeeManager.getEmployeePosition(position.getObjectID())));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
            }
            logger.info("cId = " + companyID + " --- end Position wrapping");
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- Position Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddDepartment(List<EdsDepartment> departmentList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_DEPARTMENT_CORE);
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start Department wrapping");
            for (EdsDepartment department : departmentList) {
                SelectItem item = departmentTreeManager.getParentItemByChildId(department.getObjectID());
                EdsReferenceLocale locale = null;
                if (item != null && item.getId() != null) {
                    locale = departmentManager.getDeparmentLocalization(item.getId());
                }
                solrDocs.add(department.wrapToSolrDocument(companyID, item, locale, employeeManager.getEmployeesCountByDepartment(department)));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
            }
            logger.info("cId = " + companyID + " --- end Department wrapping");
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- Department Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddPurchaseOrder(EdsPurchaseOrder purchaseOrder, Integer companyID) throws IOException, SolrServerException {
        indexAddPurchaseOrder(Collections.singletonList(purchaseOrder), companyID);
    }

    @Override
    public void indexAddPurchaseOrder(EdsPurchaseOrder purchaseOrder) throws IOException, SolrServerException {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        indexAddPurchaseOrder(Collections.singletonList(purchaseOrder), companyID);
    }

    @Override
    public void indexAddPurchaseOrder(List<EdsPurchaseOrder> purchaseList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_ORDER_CORE);
//        removeCompanyPurchaseOrderByIds(getObjectIDs(purchaseList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        String removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + " : " + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":";
        String ids = "";
        try {
            logger.info("cId = " + companyID + " --- start PurchaseOrder wrapping");
            for (EdsPurchaseOrder order : purchaseList) {
                SolrInputDocument doc = order.wrapToSolrDocument(order, companyID);
                if (order.getOpportunityID() != null) {
                    EdsOpportunity opportunity = opportunityManager.get(order.getOpportunityID());
                    if (opportunity != null) {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, opportunity.getNumber());
                    }
                }
                ids += " " + order.getObjectID();
                solrDocs.add(doc);
                boolean commited = removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, false);
                if (commited) {
                    solrDocs = new ArrayList<>();
                    ids = "";
                }
            }
            logger.info("cId = " + companyID + " --- end PurchaseOrder wrapping");
            removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- PurchaseOrder Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void indexAddRFQ(EdsRFQ rfq) throws IOException, SolrServerException {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        indexAddRFQ(Collections.singletonList(rfq), companyID);
    }

    @Override
    public void indexAddRFQ(List<EdsRFQ> rfqList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_REQUEST_FOR_QUOTE_CORE);
        removeRFQByIds(getObjectIDs(rfqList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        try {
            logger.info("cId = " + companyID + " --- start RFQ wrapping");
            for (EdsRFQ rfq : rfqList) {
                solrDocs.add(rfq.wrapToSolrDocument(rfq, companyID));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
            }
            logger.info("cId = " + companyID + " --- end RFQ wrapping");
            commit100ToSolr(solrServer, solrDocs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- RFQ Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void removeRFQSolr(Integer saleInvoiceID, Integer companyID) {
        String removeQuery = "";
        if (saleInvoiceID != null && companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_RFQ_ID + ":" + saleInvoiceID;
        } else if (companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_REQUEST_FOR_QUOTE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void indexAddPurchaseInvoice(EdsPurchaseInvoice invoice, Integer companyID) throws IOException, SolrServerException {
        indexAddPurchaseInvoice(Collections.singletonList(invoice), companyID);
    }

    @Override
    public void indexAddPurchaseInvoice(EdsPurchaseInvoice purchaseInvoice) throws IOException, SolrServerException {
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        indexAddPurchaseInvoice(Collections.singletonList(purchaseInvoice), companyID);
    }

    @Override
    public void indexAddPurchaseInvoice(List<EdsPurchaseInvoice> purchaseInvoiceList, Integer companyID) throws IOException, SolrServerException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_PURCHASE_INVOICE_CORE);
//        removeCompanyPurchaseInvoiceByIds(getObjectIDs(purchaseInvoiceList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        String removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + " : " + companyID + " AND " + SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":";
        String ids = "";

        try {
            logger.info("cId = " + companyID + " --- start PurchaseInvoice wrapping");
            for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoiceList) {

                SolrInputDocument doc = purchaseInvoice.wrapToSolrDocument(purchaseInvoice, companyID);
                if (purchaseInvoice.getOpportunityID() != null) {
                    EdsOpportunity opportunity = opportunityManager.get(purchaseInvoice.getOpportunityID());
                    if (opportunity != null) {
                        doc.addField(SolrSaleInvoiceRepresenter.FIELD_OPPORTUNITY_NUMBER, opportunity.getNumber());
                    }
                }
                ids += " " + purchaseInvoice.getObjectID();
                solrDocs.add(doc);
                boolean commited = removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, false);
                if (commited) {
                    solrDocs = new ArrayList<>();
                    ids = "";
                }
            }
            logger.info("cId = " + companyID + " --- end PurchaseInvoice wrapping");
            removeAndAdd100ToSolr(solrServer, solrDocs, removeQuery, ids, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyID + " --- PurchaseInvoice Solr Index Exception.");
            logger.error(e.getMessage());
        }
    }

    @Override
    public void removeCompanySaleQuote(Integer companyID) {
        removeSaleQuote(null, companyID);
    }

    public void removeOpportunity(Integer opportunityId, Integer companyId) throws IOException, SolrServerException {
        String removeQuery = SolrOpportunityRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID + ":" + opportunityId;
        removeEntity(removeQuery, SOLR_OPPORTUNITY_CORE);
    }

//    public void removeSaleInvoiceByIds(Integer... ids) throws IOException, SolrServerException {
//        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":", SOLR_SALEINVOICE_CORE, ids);
//    }

    @Override
    public void removeSaleInvoice(Integer saleInvoiceID, Integer companyID) {
        String removeQuery = "";
        if (saleInvoiceID != null && companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":" + saleInvoiceID;
        } else if (companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_SALEINVOICE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

//    public void removeCompanySaleQuoteByIds(Integer... ids) throws IOException, SolrServerException {
//        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":", SOLR_SALEQUOTE_CORE, ids);
//    }

    public void removeCompanyShippingDataByIds(Integer... ids) throws IOException, SolrServerException {
        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":", SOLR_SHIPPING_DATA_CORE, ids);
    }

    public void removeCompanyCertificatesByIds(Integer... ids) throws IOException, SolrServerException {
        removeMultiEntry(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID + ":", SOLR_CERTIFICATE_CORE, ids);
    }

    @Override
    public void removeSaleQuote(Integer saleQuoteID, Integer companyID) {
        String removeQuery = "";
        if (saleQuoteID != null && companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":" + saleQuoteID;
        } else if (companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_SALEQUOTE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

//    public void removeCompanyPurchaseOrderByIds(Integer... ids) throws IOException, SolrServerException {
//        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":", SOLR_PURCHASE_ORDER_CORE, ids);
//    }

    public void removeRFQByIds(Integer... ids) throws IOException, SolrServerException {
        removeMultiEntry(SolrSaleInvoiceRepresenter.FIELD_RFQ_ID + ":", SOLR_REQUEST_FOR_QUOTE_CORE, ids);
    }

    @Override
    public void removeCompanyPurchaseOrder(Integer companyID) {
        removePurchaseOrder(null, companyID);
    }

    @Override
    public void removePurchaseOrder(Integer purchaseOrderID, Integer companyID) {
        String removeQuery = "";
        if (purchaseOrderID != null && companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID + ":" + purchaseOrderID;
        } else if (companyID != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_PURCHASE_ORDER_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCompanyOpportunity(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeLeadQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
            removeEntity(removeLeadQuery, SOLR_OPPORTUNITY_CORE);
        }
    }

    public void removeCompanyEvents(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeLeadQuery = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyId;
            removeEntity(removeLeadQuery, SOLR_EVENT_CORE);
        }
    }

    public void removeCompanyProductsServices(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeQuery = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyId;
            removeEntity(removeQuery, SOLR_PRODUCTS_SERVICES_CORE);
        }
    }

    @Override
    public void removeCompanyCourseSchedule(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_COURSE_SCHEDULE_CORE);
        }
    }

    @Override
    public void removeCompanyEmployee(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrEmployeeRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_EMPLOYEE_CORE);
        }
    }

    @Override
    public void removeLeaveRequests(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrLeaveRequestConst.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_LEAVE_REQUEST_CORE);
        }
    }

    @Override
    public void removeCustomFormItems(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrCustomFormConst.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_CUSTOM_FORM_ITEM_CORE);
        }
    }

    @Override
    public void removeCompanySinglePayrun(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrSinglePayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_SINGLE_PAYRUN_CORE);
        }
    }

    @Override
    public void removeCompanyGroupPayrun(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrGroupPayrunRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_GROUP_PAYRUN_CORE);
        }
    }

    @Override
    public void removeCompanyCashAdvance(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrCashAdvanceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_CASH_ADVANCE_CORE);
        }
    }

    @Override
    public void removeCompanyAdditionalPayment(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            String removeQuery = SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID + ":" + companyID;
            removeEntity(removeQuery, SOLR_ADDITIONAL_PAYMENT_CORE);
        }
    }

    @Override
    public void removeCompanyPurchaseInvoice(Integer companyID) throws IOException, SolrServerException {
        removePurchaseInvoice(null, companyID);
    }

//    public void removeCompanyPurchaseInvoiceByIds(Integer... ids) throws IOException, SolrServerException {
//        removeMultiEntry(SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":", SOLR_PURCHASE_INVOICE_CORE, ids);
//    }

    @Override
    public void removePurchaseInvoice(Integer purchaseInvoiceID, Integer companyID) throws IOException, SolrServerException {
        String removeQuery = "";
        if (purchaseInvoiceID != null && companyID != null) {
            removeQuery = SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrPurchaseInvoiceRepresenter.FIELD_PURCHASEINVOICE_ID + ":" + purchaseInvoiceID;
        } else if (companyID != null) {
            removeQuery = SolrPurchaseInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_PURCHASE_INVOICE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCompanyExpenseReportClaims(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeQuery = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyId;
            removeEntity(removeQuery, SOLR_EXPENSE_REPORT_CLAIMS_CORE);
        }
    }

    public void removeShippingData(Integer shippingDataId, Integer companyId) throws IOException, SolrServerException {
        String removeQuery = "";
        if (shippingDataId != null && companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID + ":" + shippingDataId;
        } else if (companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
        }
        try {
            removeEntity(removeQuery, SOLR_SHIPPING_DATA_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCertificate(Integer certificateId, Integer companyId) {
        String removeQuery = "";
        if (certificateId != null && companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrCertificateRepresenter.FIELD_CERTIFICATE_ID + ":" + certificateId;
        } else if (companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
        }
        try {
            removeEntity(removeQuery, SOLR_CERTIFICATE_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removePosition(Integer positionId, Integer companyId) {
        String removeQuery = "";
        if (positionId != null && companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrPositionRepresenter.FIELD_POSITION_ID + ":" + positionId;
        } else if (companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
        }
        try {
            removeEntity(removeQuery, SOLR_POSITION_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeDepartment(Integer departmentId, Integer companyId) {
        String removeQuery = "";
        if (departmentId != null && companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrDepartmentRepresenter.FIELD_DEPARTMENT_ID + ":" + departmentId;
        } else if (companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
        }
        try {
            removeEntity(removeQuery, SOLR_DEPARTMENT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCompanyCourseBooking(Integer companyId) throws IOException, SolrServerException {
        if (companyId != null) {
            String removeQuery = SolrEventRepresenter.FIELD_COMPANY_ID + ":" + companyId;
            removeEntity(removeQuery, SOLR_COURSE_BOOKING_CORE);
        }
    }

    public void setJpaTemplate(WfmJpaOperations jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    public void rollbackEvent(SolrEvent event) throws SolrServerException, SolrException, IOException {
        if (SolrEvent.TASK_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyTasksbyIds(event.getCompanyID(), event.getEntityID());
        } else if (SolrEvent.PROJECT_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyProject(event.getEntityID(), event.getCompanyID());
        } else if (SolrEvent.FOLDER_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeFolder(event.getEntityID());
        } else if (SolrEvent.LEAD_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyLeadByIds(event.getEntityID());
        } else if (SolrEvent.CRM_CONTACT_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyCrmContactBuIds(event.getEntityID());
        } else if (SolrEvent.CRM_ACCOUNT_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCrmAccountByIds(event.getEntityID());
        } else if (SolrEvent.CRM_CASE_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyCaseByIds(event.getEntityID());
        } else if (SolrEvent.EVENT_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeCompanyEventByIds(event.getEntityID());
        } else if (SolrEvent.LEAD_REMOVE.getStringValue().equals(event.getEventType().getStringValue())) {
            javax.persistence.EntityManager em = jpaTemplate.getHibernateEntityManager();
            try (org.hibernate.Session session = (org.hibernate.Session) em.getDelegate()) {
                session.beginTransaction();
                addLeadToIndex(em.find(EdsCrmContact.class, event.getEntityID()));
                session.getTransaction().commit();
            }
            em.close();
        } /*else if (SolrEvent.NETWORK_ADD.getStringValue().equals(event.getEventType().getStringValue())) {
            removeNetworks(event.getEntityID());
        }*/
    }

    public void analyzeSolrDbconsistence() {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_TASK_CORE);
        SolrQuery sQuery = new SolrQuery();
        int start = 0;
        int limit = 1000;
        sQuery.setQuery("*:*");
        sQuery.setStart(start);
        sQuery.setRows(limit);
        sQuery.setFields(SolrTaskRepresenter.FIELD_COMPOSITE);
        sQuery.setFields(SolrTaskRepresenter.FIELD_TASK_ID);
        sQuery.setFields(SolrTaskRepresenter.FIELD_COMPANY_ID);
        sQuery.setFields(SolrTaskRepresenter.FIELD_TASK_NAME);
        QueryResponse resp = null;
        try {
            resp = server.query(sQuery);
            while (resp.getResults().size() > 0) {
                for (SolrDocument sd : resp.getResults()) {
                    Integer taskid = Integer.valueOf(sd.getFieldValue(SolrTaskRepresenter.FIELD_TASK_ID).toString());

                }
                start += limit;
                sQuery.setRows(limit);
                sQuery.setStart(start);
                resp = server.query(sQuery);

            }

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }

/*    public void addNetworksToIndex(EdsNetwork... networks) throws SolrServerException, SolrException, IOException {
        SolrServer solr = WfmJpaTemplate.getSolrServerForCore(SOLR_NETWORK_CORE);
        removeNetworks(getObjectIDs(networks));

        Integer companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        String companyName = companyManager.get(companyId).getName();
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        for (EdsNetwork network : networks) {
            logger.info("NETWORK ->" + network.getName() + "_" + network.getObjectID() + " is going to be added to solr");

            String compositeId = companyId + "_" + network.getObjectID() + "_" + network.getCreator().getObjectID();

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField(SolrNetworkRepresenter.FIELD_COMPOSITE_ID, compositeId);
            doc.addField(SolrNetworkRepresenter.FIELD_COMPANY_ID, companyId);
            doc.addField(SolrNetworkRepresenter.FIELD_COMPANY_NAME, companyName);
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_ID, network.getObjectID());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_NAME, network.getName());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_WEBSITE, network.getWebsite());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_DESCRIPTION, network.getDescription());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_TYPE, network.getType());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_CREATOR_ID, network.getCreator().getObjectID());
            doc.addField(SolrNetworkRepresenter.FIELD_NETWORK_CREATOR_NAME, network.getCreator().getName());

            docs.add(doc);
            docs = commit100ToSolr(solr, docs, false);
        }
        commit100ToSolr(solr, docs, true);
    }*/

/*    public void removeNetworks(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrNetworkRepresenter.FIELD_NETWORK_ID + ":", SOLR_NETWORK_CORE, ids);
    }*/

/*    public void removeCompanyNetworks(Integer companyID) throws SolrServerException, SolrException, IOException {
        StringBuffer sb = new StringBuffer();
        sb.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(companyID);
        removeEntity(sb.toString(), SOLR_NETWORK_CORE);
    }*/

    /**
     * This is method index project to solr
     *
     * @param project
     * @param companyid
     */
    public void indexAddProject(EdsProject project, Integer companyid) throws SolrServerException, SolrException, IOException {
        indexAddProject(Collections.singletonList(project), companyid);
    }

    public void indexAddProject(EdsProject project) throws SolrServerException, SolrException, IOException {
        Integer companyID = SecurityContext.getCompanyID();
        indexAddProject(Collections.singletonList(project), companyID);
    }

    /**
     * This is method index project to solr
     *
     * @param projectList
     * @param companyID
     */
    @Override
    public void indexAddProject(List<EdsProject> projectList, Integer companyID) throws SolrServerException, SolrException, IOException {
        SolrClient solrServer = WfmJpaTemplate.getSolrServerForCore(SOLR_PROJECT_CORE);
        removeCompanyProjectsById(getObjectIDs(projectList));
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        Integer id = null;
        try {
            String ids = "";
            boolean isAutomatic = numberingSettingsManager.getNumberingSetting() != null && numberingSettingsManager.getNumberingSetting().isAutomatic();
            for (EdsProject edsProject : projectList) {
                List<EdsRelation> edsRelationList = relationManager.getAllRelations(EdsRelation.TYPE_PROJECT, edsProject.getObjectID());
                id = edsProject.getObjectID();
                logger.info("ProjectID = " + edsProject.getObjectID() + " >>>=Solr Index Begin, cId=" + companyID);
                String projectLastInvoiceNumber = projectManager.getProjectLastInvoiceNumber(edsProject.getObjectID());
                List<EdsEmployee> edsAssigneesList = projectEmployeeManager.getEmployeesByProject(edsProject.getObjectID());
                Float estimatedtime = 0f;
                Float timespent = 0f;
                Double[] projectCostAndTimeSpent = timeSheetManager.getProjectCostAndTimeSpent(edsProject.getObjectID(), null);
                timespent = projectCostAndTimeSpent != null && projectCostAndTimeSpent[2] != null && projectCostAndTimeSpent[2].toString() != "0.0" ? projectCostAndTimeSpent[2].floatValue() : 0;//PROJECT_ACTUAL_TIME_SPENT
                estimatedtime = projectCostAndTimeSpent != null && projectCostAndTimeSpent[5] != null && projectCostAndTimeSpent[5].toString() != "0.0" ? projectCostAndTimeSpent[5].floatValue() : 0;//PROJECT_HOURS_SPENT

                boolean newProjectPercon = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT);
                solrDocs.add(edsProject.indexToSolr(edsAssigneesList, projectLastInvoiceNumber, companyID, edsRelationList, newProjectPercon, timespent, estimatedtime, isAutomatic));
                solrDocs = commit100ToSolr(solrServer, solrDocs, false);
                ids = ServerUtils.contactToStringAttr(ids, id);
            }
            commit100ToSolr(solrServer, solrDocs, true);
            logger.info("ProjectIDs >>>=" + ids + " added to batch solr, cId=" + companyID);
        } catch (IOException | SolrServerException e) {
            logger.info("cId >>>= " + companyID + ", ProjectId >>>=" + id + " Solr Index Exception. ----------------------->>>>=");
            e.printStackTrace();
        }
    }

    @Override
    public void addChartOfAccountToIndex(EdsAccount... edsaccounts) throws SolrServerException, SolrException, IOException {
        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        if (companyId == null) {
            return;
        }
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CHART_OF_ACCOUNT_CORE);
        removeChartOfAccountByIds(getObjectIDs(edsaccounts));
        List<SolrInputDocument> docs = new ArrayList<>();
        try {
            for (EdsAccount edsAccount : edsaccounts) {
                logger.info("ChartOfAccountID = " + edsAccount.getObjectID() + " >>>=Solr Index Begin, cId=" + companyId);
                docs.add(edsAccount.indexToSolr(companyId));
                docs = commit100ToSolr(solr, docs, false);
            }
            commit100ToSolr(solr, docs, true);
        } catch (IOException | SolrServerException e) {
            logger.info("cId = " + companyId + " --- Chart Of Account Solr Index Exception.");
//            e.printStackTrace();
        }
    }

    @Override
    public void removeCompanyChartOfAccount(Integer companyID) throws IOException, SolrServerException {
        if (companyID != null) {
            removeEntity((SolrChartOfAccountRepresenter.FIELD_COMPANY_ID + ":" + companyID), SOLR_CHART_OF_ACCOUNT_CORE);
        }
    }

    public void removeChartOfAccount(Integer accountID) throws IOException, SolrServerException {
        if (accountID != null) {
            removeEntity((SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID + ":" + accountID), SOLR_CHART_OF_ACCOUNT_CORE);
        }
    }

    public void removeChartOfAccount(Integer accountID, Integer companyId) {
        String removeQuery = "";
        if (accountID != null && companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId + " AND " + SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID + ":" + accountID;
        } else if (companyId != null) {
            removeQuery = SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID + ":" + companyId;
        }
        try {
            removeEntity(removeQuery, SOLR_CHART_OF_ACCOUNT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeChartOfAccountByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID + ":", SOLR_CHART_OF_ACCOUNT_CORE, ids);
    }

    /**
     * This is method remove company projects in solr
     *
     * @param companyID
     */
    public void removeCompanyProjects(Integer companyID) throws IOException, SolrServerException {
        removeCompanyProject(null, companyID);
    }

    /**
     * This is method remove company invoices in solr
     *
     * @param companyID
     */
    public void removeCompanySaleInvoice(Integer companyID) {
        removeSaleInvoice(null, companyID);
    }

    /**
     * This is method remove project in solr
     *
     * @param projectID
     * @param companyID
     */
    @Override
    public void removeCompanyProject(Integer projectID, Integer companyID) throws IOException, SolrServerException {
        String removeQuery = "";
        if (projectID != null && companyID != null) {
            removeQuery = SolrProjectListRepresenter.FIELD_COMPANY_ID + ":" + companyID + " AND " + SolrProjectListRepresenter.FIELD_PROJECT_ID + ":" + projectID;
        } else if (companyID != null) {
            removeQuery = SolrProjectListRepresenter.FIELD_COMPANY_ID + ":" + companyID;
        }
        try {
            removeEntity(removeQuery, SOLR_PROJECT_CORE);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCompanyProjectsById(Integer... projectIDs) throws IOException, SolrServerException {
        removeMultiEntry(SolrProjectListRepresenter.FIELD_PROJECT_ID + ":", SOLR_PROJECT_CORE, projectIDs);
    }

    @Override
    public void removeVacances(Integer... vacancyIds) throws IOException, SolrServerException {
        removeMultiEntry(SolrVacancyRepresenter.FIELD_VACANCY_ID + ":", SOLR_VACANCY_CORE, vacancyIds);
    }

    @Override
    public void removeCompanyVacancy(Integer companyID) throws IOException, SolrServerException {
        removeEntity(SolrProjectListRepresenter.FIELD_COMPANY_ID + ":" + companyID, SOLR_VACANCY_CORE);
    }

    @Override
    public void removeEmployeeSteps(Integer... stepIds) throws IOException, SolrServerException {
        removeMultiEntry(SolrEmployeeStepRepresenter.FIELD_STEP_ID + ":", SOLR_EMPLOYEE_STEP_CORE, stepIds);
    }

    @Override
    public void removeCompanyEmployeeStep(Integer companyID) throws IOException, SolrServerException {
        removeEntity(SolrEmployeeStepRepresenter.FIELD_COMPANY_ID + ":" + companyID, SOLR_EMPLOYEE_STEP_CORE);
    }

    @Override
    public void addCustomFormItemToIndex(EdsCustomFormItems... edsItems) throws IOException, SolrServerException {
        SolrClient solr = WfmJpaTemplate.getSolrServerForCore(SOLR_CUSTOM_FORM_ITEM_CORE);
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        if (edsItems != null && edsItems.length > 0) {
            List<SolrInputDocument> docs = new ArrayList<>();
            List<Integer> idsToRemove = new ArrayList<>();

            for (EdsCustomFormItems request : edsItems) {
                if (request != null) {
                    SolrInputDocument doc = request.indexToSolr(companyID);
                    if (request.getFormCustomFields() != null && request.getFormCustomFields().getJsonEntities() != null && request.getFormCustomFields().getJsonEntities().trim().length() > 0 && !"{}".equals(request.getFormCustomFields().getJsonEntities())) {
                        Gson gson = new Gson();
                        Type dataType = new TypeToken<HashMap<String, String>>() {
                        }.getType();
                        HashMap<String, String> map = gson.fromJson(request.getFormCustomFields().getJsonEntities(), dataType);
                        List<SolrInputDocument> customFormPropertiesDoc = new ArrayList<>();
                        String compositID = companyID + "_" + (request.getCustomForm() != null ? request.getCustomForm().getObjectID() : null) + "_" + request.getObjectID();

                        for (Map.Entry<String, String> mapValues : map.entrySet()) {
                            SolrInputDocument customFormProDoc = new SolrInputDocument();
                            customFormProDoc.setField(SolrCustomFormConst.FIELD_DOC_TYPE, SolrCustomFormConst.CUSTOM_FORM_PROPERTIES_SOLR_DOC);
                            customFormProDoc.addField(SolrCustomFormConst.FIELD_COMPOSITE_ID, compositID);
                            customFormProDoc.addField(SolrCustomFormConst.FIELD_COMPANY_ID, companyID);
                            customFormProDoc.addField(SolrCustomFormConst.FIELD_ITEM_ID, request.getCustomForm().getObjectID());
                            customFormProDoc.addField(SolrCustomFormConst.FIELD_CUSTOM_FIELD_KEY, mapValues.getKey().toUpperCase());
                            customFormProDoc.addField(SolrCustomFormConst.FIELD_CUSTOM_FIELD_VALUE, mapValues.getValue());
                            customFormPropertiesDoc.add(customFormProDoc);
                        }
                        doc.addChildDocuments(customFormPropertiesDoc);
                    }
                    docs.add(doc);
                    logger.info("Custom form item is added to solr: cId=" + companyID + ", rId=" + request.getObjectID());
                    if (request.getObjectID() != null) {
                        idsToRemove.add(request.getObjectID());
                    }
                    if (docs.size() >= SOLR_LIMIT * 10) {
                        removeCustomFormByIds(idsToRemove.toArray(new Integer[]{}));
                        idsToRemove.clear();
                        docs = commit100ToSolr(solr, docs, false);
                    }
                }
            }
            removeCustomFormByIds(idsToRemove.toArray(new Integer[]{}));
            idsToRemove.clear();
            commit100ToSolr(solr, docs, true);
        }
    }

    public void removeCustomFormByIds(Integer... ids) throws SolrServerException, SolrException, IOException {
        removeMultiEntry(SolrCustomFormConst.FIELD_OBJECT_ID + ":", SOLR_CUSTOM_FORM_ITEM_CORE, ids);
    }

    @Override
    public void indexDynamicEntity(String type, Integer id) throws Exception {
        if (type == null || id == null) {
            return;
        }
        switch (type) {
            case RelationItem.TYPE_ADDITIONAL_PAYMENT ->
                    additionalPaymentSolrComponent.index(additionalPaymentManager.get(id));
            case RelationItem.TYPE_CANDIDATE, RelationItem.TYPE_CONTACT, RelationItem.TYPE_LEAD ->
                    contactSolrComponent.index(crmContactManager.get(id));
            case RelationItem.TYPE_CASE -> caseSolrComponent.index(caseManager.get(id));
            case RelationItem.TYPE_CASH_ADVANCE -> cashAdvanceSolrComponent.index(cashAdvanceManager.get(id));
            case RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT ->
                    certificateSolrComponent.index(certificateOfEmploymentManager.get(id));
            case RelationItem.TYPE_CHART_OF_ACCOUNT -> chartOfAccountSolrComponent.index(accountingManager.get(id));
            case RelationItem.TYPE_CRM_ACCOUNT -> crmAccountSolrComponent.index(crmAccountManager.get(id));
            case RelationItem.TYPE_CUSTOM_FORM_ITEM -> customFormItemSolrComponent.index(customFormItemManager.get(id));
            case RelationItem.TYPE_EMPLOYEE -> employeeSolrComponent.index(employeeManager.get(id));
            case RelationItem.TYPE_EMPLOYEE_STEP -> employeeStepSolrComponent.index(stepEmployeeManager.get(id));
            case RelationItem.TYPE_EVENT -> eventSolrComponent.index(eventManager.get(id));
            case RelationItem.TYPE_EXPENSE_CLAIM ->
                    expenseReportClaimsSolrComponent.index(expenseReportManager.get(id));
            case RelationItem.TYPE_GROUP_PAYRUN -> groupPayrunSolrComponent.index(payslipTableManager.get(id));
            case RelationItem.TYPE_SINGLE_PAYRUN -> singlePayrunSolrComponent.index(payslipTableItemManager.get(id));
            case RelationItem.TYPE_SALEINVOICE -> saleInvoiceSolrComponent.index(invoiceManager.getSaleInvoice(id));
            case RelationItem.TYPE_LEAVE_REQUEST -> leaveRequestSolrComponent.index(sickRequestManager.get(id));
            case RelationItem.TYPE_NEWS -> newsSolrComponent.index(newsManager.get(id));
            case RelationItem.TYPE_OPPORTUNITY -> opportunitySolrComponent.index(opportunityManager.get(id));
            case RelationItem.TYPE_PRODUCT -> productsServicesSolrComponent.index(itemManager.get(id));
            case RelationItem.TYPE_PROJECT -> projectSolrComponent.index(projectManager.get(id));
            case RelationItem.TYPE_PURCHASE_INVOICE ->
                    purchaseInvoiceSolrComponent.index(invoiceManager.getPurchaseInvoice(id));
            case RelationItem.TYPE_PURCHASE_ORDER ->
                    purchaseOrderSolrComponent.index(quoteManager.getPurchaseOrderByID(id));
            case RelationItem.TYPE_RFQ -> requestForQuoteSolrComponent.index(rfqManager.get(id));
            case RelationItem.TYPE_SALEQUOTE, RelationItem.TYPE_SALEORDER ->
                    saleQuoteSolrComponent.index(quoteManager.getSaleQuote(id));
            case RelationItem.TYPE_TASK -> taskSolrComponent.index(taskManager.get(id));
            case RelationItem.TYPE_VACANCY -> vacancySolrComponent.index(vacancyManager.get(id));
        }
    }

    @Override
    public void removeDynamicEntity(String type, Integer id) throws IOException, SolrServerException {
        if (type == null || id == null) {
            return;
        }
        switch (type) {
            case RelationItem.TYPE_ADDITIONAL_PAYMENT -> removeAdditionalPaymentByIds(id);
            case RelationItem.TYPE_CANDIDATE, RelationItem.TYPE_CONTACT -> removeCompanyCrmContactBuIds(id);
            case RelationItem.TYPE_CASE -> removeCompanyCaseByIds(id);
            case RelationItem.TYPE_CASH_ADVANCE -> removeCashAdvanceByIds(id);
            case RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT -> removeCompanyCertificatesByIds(id);
            case RelationItem.TYPE_CHART_OF_ACCOUNT -> removeChartOfAccount(id, SecurityContext.getCompanyID());
            case RelationItem.TYPE_CRM_ACCOUNT -> removeCrmAccountByIds(id);
            case RelationItem.TYPE_CUSTOM_FORM_ITEM -> removeCustomFormByIds(id);
            case RelationItem.TYPE_EMPLOYEE_STEP -> removeEmployeeSteps(id);
            case RelationItem.TYPE_EVENT -> removeCompanyEventByIds(id);
            case RelationItem.TYPE_EXPENSE_CLAIM -> removeExpenseReportByIds(id);
            case RelationItem.TYPE_GROUP_PAYRUN -> removeGroupPayrunByIds(id);
            case RelationItem.TYPE_SINGLE_PAYRUN -> removeSinglePayrunByIds(id);
            case RelationItem.TYPE_SALEINVOICE -> removeSaleInvoice(id, SecurityContext.getCompanyID());
            case RelationItem.TYPE_LEAD -> removeCompanyLeadByIds(id);
            case RelationItem.TYPE_LEAVE_REQUEST -> removeLeaveRequestByIds(id);
            case RelationItem.TYPE_NEWS -> removeCompanyNewsByIds(id);
            case RelationItem.TYPE_OPPORTUNITY -> removeOpportunitiesByIds(id);
            case RelationItem.TYPE_PRODUCT -> removeProductsServicesByIds(id);
            case RelationItem.TYPE_PROJECT -> removeCompanyProjectsById(id);
            case RelationItem.TYPE_PURCHASE_INVOICE -> removePurchaseInvoice(id, SecurityContext.getCompanyID());
            case RelationItem.TYPE_PURCHASE_ORDER -> removePurchaseOrder(id, SecurityContext.getCompanyID());
            case RelationItem.TYPE_RFQ -> removeRFQByIds(id);
            case RelationItem.TYPE_SALEQUOTE, RelationItem.TYPE_SALEORDER ->
                    removeSaleQuote(id, SecurityContext.getCompanyID());
            case RelationItem.TYPE_TASK -> removeTask(taskManager.get(id), userManager.getUser().getCompany());
            case RelationItem.TYPE_VACANCY -> removeVacances(id);
        }
    }
}
