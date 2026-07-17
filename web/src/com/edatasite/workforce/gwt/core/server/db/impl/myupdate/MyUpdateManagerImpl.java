package com.edatasite.workforce.gwt.core.server.db.impl.myupdate;

import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.core.domain.accounting.EdsRFQ;
import com.edatasite.workforce.core.domain.accounting.EdsRecurringInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.EdsSolution;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdateType;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPensionScheme;
import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.WebHookResponseManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FixedAssetManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:30:07 PM
 */
@Repository("myUpdateManager")
public class MyUpdateManagerImpl extends BaseManager<EdsMyUpdate> implements MyUpdateManager, Constants {

    private static final DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private MyUpdateTypeManager myUpdateTypeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private FixedAssetManager fixedAssetManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    @Qualifier("myActivityLocalizer")
    private WfmMessageSource activityWfmMessageSource;
    @Autowired
    private WebHookResponseManager webHookResponseManager;

    public MyUpdateManagerImpl() {
        super(EdsMyUpdate.class);
    }

    /**
     * Registers events related to Task
     *
     * @param task
     * @param eventCauser
     * @param eventTime   - time when event occured
     * @param eventType   - it may be ADD, UPDATE, STATUS_CHANGE and etc. (see EdsMyUpdate static fields)
     * @param updateType  - it may be TASK_ADD, TASK_UPDATE, TASK_DELETE and etc.( see MyUpdateTypeManager static fields)
     * @return
     */
    public EdsMyUpdate registerTaskUpdate(EdsTask task, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(task.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.TASK);

    }

    public EdsMyUpdate registerClientUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(client.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CLIENT);
    }

    public EdsMyUpdate registerClientAddUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime) {
        return registerClientUpdate(client, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CLIENT_ADD);
    }

    public EdsMyUpdate registerClientEditUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime) {
        return registerClientUpdate(client, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.CLIENT_EDIT);
    }

    public EdsMyUpdate registerClientDeleteUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime) {
        return registerClientUpdate(client, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.CLIENT_DELETE);
    }

    public EdsMyUpdate registerIssueUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(issue.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ISSUE);
    }

    public EdsMyUpdate registerListPanelSettingsEditUpdate(EdsListPanelSettings listPanelSettings, EdsUser user, Date time) {
        return registerMyUpdate(listPanelSettings.getObjectID(), user, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.PANEL_SETTING_EDIT, MyUpdateTypeManager.PANEL_SETTING);
    }

    public EdsMyUpdate registerIssueAddUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String issueType) {
        String updateType = MyUpdateTypeManager.ISSUE_ADD;
        /*if (issueType.equals(TASK_ISSUE)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_TASK_ADD;
        } else if (PROJECT_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_PROJECT_ADD;
        } else if (CLIENT_CUSTOMER_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_CLIENT_CUSTOMER_ADD;
        } else if (EMPLOYEE_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_EMPLOYEE_ADD;
        } else if (DEPARTMENT_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_DEPARTMENT_ADD;
        }*/
        return registerIssueUpdate(issue, eventCauser, eventTime, EdsMyUpdate.ADD, updateType);
    }

    public EdsMyUpdate registerIssueEditUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String issueType) {
        String updateType = MyUpdateTypeManager.ISSUE_EDIT;
        /*if (TASK_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_TASK_EDIT;
        } else if (PROJECT_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_PROJECT_EDIT;
        } else if (CLIENT_CUSTOMER_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_CLIENT_CUSTOMER_EDIT;
        } else if (EMPLOYEE_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_EMPLOYEE_EDIT;
        } else if (DEPARTMENT_ISSUE.equals(issueType)) {
            updateType = MyUpdateTypeManager.ISSUE_TYPE_DEPARTMENT_EDIT;
        }*/
        return registerIssueUpdate(issue, eventCauser, eventTime, EdsMyUpdate.EDIT, updateType);
    }

    public EdsMyUpdate registerLocationUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(location.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.LOCATION);
    }

    public EdsMyUpdate registerLocationAddUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime) {
        return registerLocationUpdate(location, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.LOCATION_ADD);
    }

    public EdsMyUpdate registerLocationEditUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime) {
        return registerLocationUpdate(location, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.LOCATION_EDIT);
    }

    // Campaign
    public EdsMyUpdate registerCampaignUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(campaign.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CAMPAIGN);
    }

    public EdsMyUpdate registerCampaignAddUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime) {
        return registerCampaignUpdate(campaign, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CAMPAIGN_ADD);
    }

    public EdsMyUpdate registerCampaignEditUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime) {
        return registerCampaignUpdate(campaign, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.CAMPAIGN_EDIT);
    }

    public EdsMyUpdate registerCampaignDeleteUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime) {
        return registerCampaignUpdate(campaign, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.CAMPAIGN_DELETE);
    }

    // Lead
    public EdsMyUpdate registerLeadUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(lead.getObjectID(), eventCauser, eventTime, eventType, updateType, (lead.getContactType().equals(EdsCrmContact.LEAD_CONTACT) ? MyUpdateTypeManager.LEAD : MyUpdateTypeManager.CONTACT));
    }

    public EdsMyUpdate registerLeadAddUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime) {
        if (lead.getContactType().equals(EdsCrmContact.LEAD_CONTACT)) {
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.LEAD_ADD);
        } else { // else its CRM_CONTACT
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CONTACT_ADD);
        }
    }

    public EdsMyUpdate registerLeadEditUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime) {
        if (lead.getContactType().equals(EdsCrmContact.LEAD_CONTACT)) {
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.LEAD_EDIT);
        } else { // else its CRM_CONTACT
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CONTACT_EDIT);
        }
    }

    public EdsMyUpdate registerLeadDeleteUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime) {
        if (lead.getContactType().equals(EdsCrmContact.LEAD_CONTACT)) {
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.LEAD_DELETE);
        } else { // else its CRM_CONTACT
            return registerLeadUpdate(lead, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CONTACT_DELETE);
        }
    }

    // WebForm
    public EdsMyUpdate registerWebFormUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(webForm.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.WEB_FORM);
    }

    public EdsMyUpdate registerWebFormAddUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime) {
        return registerWebFormUpdate(webForm, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.WEB_FORM_ADD);
    }

    public EdsMyUpdate registerWebFormEditUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime) {
        return registerWebFormUpdate(webForm, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.WEB_FORM_EDIT);
    }

    public EdsMyUpdate registerWebFormDeleteUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime) {
        return registerWebFormUpdate(webForm, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.WEB_FORM_DELETE);
    }

    // Solution
    public EdsMyUpdate registerSolutionUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(solution.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.SOLUTION);
    }

    public EdsMyUpdate registerSolutionAddUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime) {
        return registerSolutionUpdate(solution, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.SOLUTION_ADD);
    }

    public EdsMyUpdate registerSolutionEditUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime) {
        return registerSolutionUpdate(solution, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.SOLUTION_EDIT);
    }

    public EdsMyUpdate registerSolutionDeleteUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime) {
        return registerSolutionUpdate(solution, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.SOLUTION_DELETE);
    }

    // Case
    public EdsMyUpdate registerCaseUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(crmcase.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CASE);
    }

    public EdsMyUpdate registerCaseAddUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime) {
        return registerCaseUpdate(crmcase, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CASE_ADD);
    }

    public EdsMyUpdate registerCaseEditUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime) {
        return registerCaseUpdate(crmcase, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.CASE_EDIT);
    }

    public EdsMyUpdate registerCaseDeleteUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime) {
        return registerCaseUpdate(crmcase, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.CASE_DELETE);
    }

    //Timeslot
    public EdsMyUpdate registerTimeSlotUpdate(EdsTimeSlot timeslot, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(timeslot.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.TIMESLOT);
    }

    public EdsMyUpdate registerTimeSlotAddUpdate(EdsTimeSlot timeslot, EdsUser eventCauser, Date eventTime) {
        return registerTimeSlotUpdate(timeslot, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.TIMESLOT_ADD);
    }

    public EdsMyUpdate registerTimeSlotEditUpdate(EdsTimeSlot timeslot, EdsUser eventCauser, Date eventTime) {
        return registerTimeSlotUpdate(timeslot, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.TIMESLOT_EDIT);
    }

    public EdsMyUpdate registerTimeSlotDeleteUpdate(EdsTimeSlot timeslot, EdsUser eventCauser, Date eventTime) {
        return registerTimeSlotUpdate(timeslot, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.TIMESLOT_DELETE);
    }

    //Holiday
    public EdsMyUpdate registerHolidayUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(holiday.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.HOLIDAY);
    }

    public EdsMyUpdate registerHolidayAddUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime) {
        return registerHolidayUpdate(holiday, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.HOLIDAY_ADD);
    }

    public EdsMyUpdate registerHolidayEditUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime) {
        return registerHolidayUpdate(holiday, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.HOLIDAY_EDIT);
    }

    public EdsMyUpdate registerHolidayDeleteUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime) {
        return registerHolidayUpdate(holiday, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.HOLIDAY_DELETE);
    }

    // Account
    public EdsMyUpdate registerAccountUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(account.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNT);
    }

    public EdsMyUpdate registerAccountAddUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime) {
        return registerAccountUpdate(account, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNT_ADD);
    }

    public EdsMyUpdate registerAccountEditUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime) {
        return registerAccountUpdate(account, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNT_EDIT);
    }

    public EdsMyUpdate registerAccountDeleteUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime) {
        return registerAccountUpdate(account, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNT_DELETE);
    }

    // Contact Cateory
    public EdsMyUpdate registerContactCategoryUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(category.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CATEGORY);
    }

    public EdsMyUpdate registerContactCategoryAddUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime) {
        return registerContactCategoryUpdate(category, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CATEGORY_ADD);
    }

    public EdsMyUpdate registerContactCategoryEditUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime) {
        return registerContactCategoryUpdate(category, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CATEGORY_EDIT);
    }

    // Mailing list
    public EdsMyUpdate registerMailingListUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(mailList.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.MAILING);
    }

    public EdsMyUpdate registerMailingListAddUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime) {
        return registerMailingListUpdate(mailList, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.MAILING_ADD);
    }

    public EdsMyUpdate registerMailingListEditUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime) {
        return registerMailingListUpdate(mailList, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.MAILING_EDIT);
    }

    //Opportunity
    public EdsMyUpdate registerOpportunityUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(opportunity.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.OPPORTUNITY);
    }

    public EdsMyUpdate registerOpportunityAddUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.OPPORTUNITY_ADD);
    }

    public EdsMyUpdate registerOpportunityEditUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.OPPORTUNITY_EDIT);
    }

    public EdsMyUpdate registerOpportunityDeleteUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.OPPORTUNITY_DELETE);
    }

    @Override
    public EdsMyUpdate registerOpportunityDraft(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.OPPORTUNITY_DRAFT);
    }


    @Override
    public EdsMyUpdate registerOpportunityReject(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.OPPORTUNITY_REJECTED);
    }

    @Override
    public EdsMyUpdate registerOpportunityApprove(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime) {
        return registerOpportunityUpdate(opportunity, eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.OVERTIME_APPROVED);
    }

    @Override
    public EdsMyUpdate registerFileUpload(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime) {
        return registerMyUpdate(fileHeader.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_FILE_UPLOAD, MyUpdateTypeManager.PROJECT);
    }

    @Override
    public EdsMyUpdate registerFileUploadForTask(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime) {
        return registerMyUpdate(fileHeader.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.TASK_FILE_UPLOAD, MyUpdateTypeManager.TASK);
    }

    @Override
    public EdsMyUpdate registerFileUploadForIssue(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime) {
        return registerMyUpdate(fileHeader.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ISSUE_FILE_UPLOAD, MyUpdateTypeManager.ISSUE);
    }


// timesheet

    public EdsMyUpdate registerTimesheetUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(timesheetSession.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.TIMESHEET);
    }

    public EdsMyUpdate registerTimesheetWaitingUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime) {
        return registerTimesheetUpdate(timesheetSession, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_WAITING);
    }

    public EdsMyUpdate registerTimesheetRejectedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime) {
        return registerTimesheetUpdate(timesheetSession, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_REJECTED);
    }

    public EdsMyUpdate registerTimesheetApprovedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime) {
        return registerTimesheetUpdate(timesheetSession, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_APPROVED);
    }

    // timesheet employee

    public EdsMyUpdate registerTimesheetEmployeeUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(timesheetSession.getObjectID(), employee.getObjectID(), manager, eventTime, eventType, updateType, MyUpdateTypeManager.TIMESHEET);
    }

    public EdsMyUpdate registerTimesheetEmployeeApprovedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime) {
        return registerTimesheetEmployeeUpdate(timesheetSession, employee, manager, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_APPROVED_FOR_EMPLOYEE);
    }

    public EdsMyUpdate registerTimesheetEmployeeRejectedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime) {
        return registerTimesheetEmployeeUpdate(timesheetSession, employee, manager, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_REJECTED_FOR_EMPLOYEE);
    }

    //timesheet manager

    public EdsMyUpdate registerTimesheetManagerUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser manager, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(timesheetSession.getObjectID(), manager.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.TIMESHEET);
    }

    public EdsMyUpdate registerTimesheetManagerWaitingUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser manager, EdsUser eventCauser, Date eventTime) {
        return registerTimesheetManagerUpdate(timesheetSession, manager, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TIMESHEET_STATUS_WAITING_FOR_MANAGER);
    }

    @Override
    public EdsMyUpdate registerNoteUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        if (EdsNoteHistory.CRM_CONTACT == note.getRelatedTo()) {
            EdsCrmContact contact = crmContactManager.get(note.getRelatedId());
            if (!EdsCrmContact.CANDIDATE.equals(contact.getContactType()) && !EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerMyUpdate(note.getRelatedId(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CONTACT_NOTE);
            } else if (EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerMyUpdate(note.getRelatedId(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.LEAD_NOTE);
            }
            return null;
        } else {
            return registerMyUpdate(note.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.WORKSPACE_NOTE);
        }
    }

    @Override
    public EdsMyUpdate registerNoteAddUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime) {
        if (EdsNoteHistory.CRM_CONTACT == note.getRelatedTo()) {
            EdsCrmContact contact = crmContactManager.get(note.getRelatedId());
            if (!EdsCrmContact.CANDIDATE.equals(contact.getContactType()) && !EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.CONTACT_NOTE_ADD);
            } else if (EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.LEAD_NOTE_ADD);
            }
            return null;
        } else {
            return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.WORKSPACE_NOTE_ADD);
        }
    }

    @Override
    public EdsMyUpdate registerNoteEditUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime) {
        if (EdsNoteHistory.CRM_CONTACT == note.getRelatedTo()) {
            EdsCrmContact contact = crmContactManager.get(note.getRelatedId());
            if (!EdsCrmContact.CANDIDATE.equals(contact.getContactType()) && !EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.CONTACT_NOTE_EDIT);
            } else if (EdsCrmContact.LEAD_CONTACT.equals(contact.getContactType())) {
                return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.LEAD_NOTE_EDIT);
            }
            return null;
        } else {
            return registerNoteUpdate(note, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.WORKSPACE_NOTE_EDIT);
        }
    }

    @Override
    public EdsMyUpdate registerCustomFieldAddUpdate(EdsCompanyCustomFieldsSettings customFieldsSettings, EdsUser creator, Date time) {
        return registerMyUpdate(customFieldsSettings.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD_ADD, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD);
    }

    @Override
    public EdsMyUpdate registerCustomFieldEditUpdate(EdsCompanyCustomFieldsSettings customFieldsSettings, EdsUser creator, Date time) {
        return registerMyUpdate(customFieldsSettings.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD_EDIT, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD);
    }

    @Override
    public EdsMyUpdate registerCustomFieldDeleteUpdate(Integer objectId, String customFieldName, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerMyUpdate(objectId, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD_DELETE, MyUpdateTypeManager.COMPANY_CUSTOM_FIELD);
        myUpdate.setItemName(customFieldName);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerAttachmentCRUD(EdsFileHeader edsFileHeader, EdsUser eventCauser, EdsBusinessEvent event, String eventType) {
        EdsMyUpdate myUpdate = null;
        if (EdsMyUpdate.ADD.equals(eventType)) {
            myUpdate = registerMyUpdate(edsFileHeader.getObjectID(), eventCauser, event.getTime(), eventType, MyUpdateTypeManager.ATTACHMENT_ADD, MyUpdateTypeManager.ATTACHMENT);
        } else if (EdsMyUpdate.DELETE.equals(eventType)) {
            myUpdate = registerMyUpdate(edsFileHeader.getObjectID(), eventCauser, event.getTime(), eventType, MyUpdateTypeManager.ATTACHMENT_DELETE, MyUpdateTypeManager.ATTACHMENT);
        }

        if (myUpdate != null) {
            myUpdate.setRelationID(event.getRelationID());
            myUpdate.setRelationType(event.getRelationType());
            myUpdate.setItemName(edsFileHeader.getName());
            update(myUpdate);
        }
        return myUpdate;
    }

    @Override
    public List<MyUpdateItem> getAttachmentUpdates(Integer relationID, String relationType) {
        if (relationID == null || relationType == null) {
            return null;
        }
        List<EdsMyUpdate> myUpdates = find("select up from EdsMyUpdate up where up.relationID = ? and up.relationType = ? ", relationID, relationType);
        if (myUpdates != null && myUpdates.size() > 0) {
            List<MyUpdateItem> result = new ArrayList<>();
            for (EdsMyUpdate myUpdate : myUpdates) {
                MyUpdateItem item = null;

                String typeCode = myUpdate.getTypeCode();
                String fileName = myUpdate.getItemName();

                String userName = "";
                EdsUser user = userManager.get(myUpdate.getReceiver());
                if (user != null) {
                    userName = user.getFullName();
                }

                if (MyUpdateTypeManager.ATTACHMENT_ADD.equalsIgnoreCase(typeCode)) {
                    item = new MyUpdateItem();
                    item.setSubType(MyUpdateItem.ADD);
                    item.setTitle(activityWfmMessageSource.localize(MyUpdateTypeManager.ATTACHMENT_ADD));
                    item.setMessage(activityWfmMessageSource.localizeWithParam(MyUpdateTypeManager.ADDED_ATTACHMENT, userName, fileName));
                    item.setType(myUpdate.getEventType());
                    item.setEventDate(myUpdate.getDate());
                } else if (MyUpdateTypeManager.ATTACHMENT_DELETE.equalsIgnoreCase(typeCode)) {
                    item = new MyUpdateItem();
                    item.setSubType(MyUpdateItem.DELETE);
                    item.setTitle(activityWfmMessageSource.localize(MyUpdateTypeManager.ATTACHMENT_DELETE));
                    item.setMessage(activityWfmMessageSource.localizeWithParam(MyUpdateTypeManager.DELETED_ATTACHMENT, userName, fileName));
                    item.setType(myUpdate.getEventType());
                    item.setEventDate(myUpdate.getDate());
                }

                if (item != null) {
                    result.add(item);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType) {
        return registerMyUpdate(affectedId, eventCauser, eventTime, eventType, updateType, entityType, null, null);
    }

    @Override
    public EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType, Integer clientContactId, Integer relaionId) {
        return registerMyUpdate(affectedId, eventCauser, eventTime, eventType, updateType, entityType, clientContactId, relaionId, null, null);
    }

    @Override
    public EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType,
                                        String updateType, String entityType, Integer clientContactId, Integer relaionId, String formId, BigDecimal amount) {
        EdsCompany company = eventCauser.getCompany();
        EdsMyUpdate update = new EdsMyUpdate();
        update.setReceiverType(EdsTrusteeType.USER);
        update.setReceiver(eventCauser.getObjectID());
        update.setCompanyID(company.getObjectID());
        update.setDate(eventTime);
        boolean in = false;
        if (MyUpdateTypeManager.SALES_INVOICE_SEND_TO_CLIENT.equals(updateType) && EdsMyUpdate.STATUS_CHANGE.equals(eventType)) {
            EdsSaleInvoice invoice = (EdsSaleInvoice) invoiceManager.get(affectedId);
            if (invoice != null) {
                if (clientContactId != null) {
                    update.setInducerID(clientContactId);
                } else if (invoice.getClientContact() != null) {
                    update.setInducerID(invoice.getClientContact().getObjectID());
                }
                update.setInducerType(EdsTrusteeType.CONTACT);
                in = true;
            }
        } else if (MyUpdateTypeManager.SALES_QUOTE_SEND_TO_CLIENT.equals(updateType) && EdsMyUpdate.STATUS_CHANGE.equals(eventType)) {
            EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(affectedId);
            if (quote != null && quote.getClientContact() != null) {
                update.setInducerID(quote.getClientContact().getObjectID());
                update.setInducerType(EdsTrusteeType.CONTACT);
                in = true;
            }
        } else if (MyUpdateTypeManager.PURCHASE_ORDER_SEND_TO_CLIENT.equals(updateType) && EdsMyUpdate.STATUS_CHANGE.equals(eventType)) {
            EdsPurchaseOrder purchaseOrder = (EdsPurchaseOrder) quoteManager.get(affectedId);
            if (purchaseOrder != null && purchaseOrder.getClientContact() != null) {
                update.setInducerID(purchaseOrder.getClientContact().getObjectID());
                update.setInducerType(EdsTrusteeType.CONTACT);
                in = true;
            }
        } else if (MyUpdateTypeManager.FIXED_ASSET_OWNER_ADD.equals(updateType) || MyUpdateTypeManager.FIXED_ASSET_OWNER_EDIT.equals(updateType)) {
            EdsFixedAsset fixedAsset = fixedAssetManager.get(affectedId);
            if (fixedAsset != null && fixedAsset.getOwner() != null) {
                update.setInducerID(fixedAsset.getOwner().getObjectID());
                update.setInducerType(EdsTrusteeType.USER);
                in = true;
            }
        }
        //When lead,task or opportunity is created, set it's status
        if (MyUpdateTypeManager.LEAD_ADD.equals(updateType)) {
            EdsCrmContact lead = crmContactManager.get(affectedId);
            update.setStatusCode(lead.getLeadStatus() != null ? lead.getLeadStatus().getCode() : null);
        } else if (MyUpdateTypeManager.OPPORTUNITY_ADD.equals(updateType)) {
            EdsOpportunity opportunity = opportunityManager.get(affectedId);
            update.setStatusCode(opportunity.getStage() != null ? opportunity.getStage().getCode() : null);
        } else if (MyUpdateTypeManager.TASK_ADD.equals(updateType) || MyUpdateTypeManager.TASK_UPDATE.equals(updateType)) {
            EdsTask task = taskManager.get(affectedId);
            update.setStatusCode(task.getStatus() != null ? task.getStatus().getCode() : null);
        } else if (MyUpdateTypeManager.WEBHOOK_ADD.equals(eventType)) {
            EdsWebHookResponse response = webHookResponseManager.get(relaionId);
            update.setStatusCode(response.getStatus());
        }

        if (!in) {
            update.setInducerID(eventCauser.getObjectID());
            update.setInducerType(EdsTrusteeType.USER);
        }

        update.setAffectedID(affectedId);
        update.setAffectedType(EdsTrusteeType.ENTITY);

        update.setRelationID(relaionId);
        update.setFormId(formId);

        update.setEventType(eventType);
        EdsMyUpdateType type = myUpdateTypeManager.getType(updateType, entityType);

        update.setTypeCode(type.getCode());
        update.setAmount(amount);
        create(update);
        return update;
    }

    /**
     * @param task
     * @param receiver    user who will get this note
     * @param eventCauser user who has been cause for this update ( in cases wen task or project has been creatod and receiver has been added as assigne, eventcause ir creator of thi task project)
     * @param eventTime   - time when event occured
     * @param eventType   - it may be ADD, UPDATE, STATUS_CHANGE and etc. (see EdsMyUpdate static fields)
     * @param updateType  - it may be TASK_ADD, TASK_UPDATE, TASK_DELETE and etc.( see MyUpdateTypeManager static fields)
     * @return
     */
    public EdsMyUpdate registerEmployeeTaskUpdate(EdsTask task, EdsUser receiver, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(task.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.TASK);

    }

    /**
     * @param project
     * @param eventCauser
     * @param eventTime
     * @param eventType
     * @param updateType
     * @return
     */


    public EdsMyUpdate registerProjectUpdate(EdsProject project, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(project.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.PROJECT);
    }

    /**
     * @param project
     * @param receiver    user who will get this note
     * @param eventCauser user who has been cause for this update ( in cases wen task or project has been creatod and receiver has been added as assigne, eventcause ir creator of thi task project)
     * @param eventTime   - time when event occured
     * @param eventType   - it may be ADD, UPDATE, STATUS_CHANGE and etc. (see EdsMyUpdate static fields)
     * @param updateType  - it may be PROEJCT_ADD, PROEJCT_UPDATE, PROEJCT_DELETE and etc.( see MyUpdateTypeManager static fields)
     * @return
     */
    public EdsMyUpdate registerProjectEmployeeUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(project.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.PROJECT);

    }

    /**
     * Registers task add event
     *
     * @param task
     * @param creator
     * @param eventTime
     * @return
     */
    public EdsMyUpdate registerTaskAddUpdate(EdsTask task, EdsUser creator, Date eventTime) {
        return registerTaskUpdate(task, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.TASK_ADD);
    }

    /**
     * Registers employeetask status change event
     *
     * @param empTask   employeetask which status has been changed
     * @param updater   user who has changed employeetask status
     * @param eventTime time when this event occured
     * @return
     */
    public EdsMyUpdate registerEmployeeTaskStatusChangeUpdate(EdsEmployeeTask empTask, EdsUser updater, Date eventTime) {
        if (referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED) != null && empTask != null && referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED).equals(empTask.getStatus())) {
            return registerEmployeeTaskUpdate(empTask.getTask(), updater, updater, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TASK_STATUS_COMPELETED);
        } else if (referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CANCELLED) != null && empTask != null && referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CANCELLED).equals(empTask.getStatus())) {
            return registerEmployeeTaskUpdate(empTask.getTask(), updater, updater, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TASK_STATUS_CANCELLED);
        } else if (referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED) != null && empTask != null && referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED).equals(empTask.getStatus())) {
            return registerEmployeeTaskUpdate(empTask.getTask(), updater, updater, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.TASK_STATUS_CLOSED);
        }
        return null;

    }

    public EdsMyUpdate registerCalendarEventGuestsStatusChangeUpdate(EdsGoogleCalendarEventGuests calendarEventGuests, EdsUser owner, Date eventTime) {
        if ("Accepted".equals(calendarEventGuests.getStatus())) {
            return registerCalendarEventGuest(calendarEventGuests, owner, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CALENDAR_EVENT_GUEST_STATUS_ACCEPTED);
        } else if ("Tentatively".equals(calendarEventGuests.getStatus())) {
            return registerCalendarEventGuest(calendarEventGuests, owner, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CALENDAR_EVENT_GUEST_STATUS_TENTATIVELY);
        } else if ("Declined".equals(calendarEventGuests.getStatus())) {
            return registerCalendarEventGuest(calendarEventGuests, owner, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CALENDAR_EVENT_GUEST_STATUS_DECLINED);
        }
        return null;
    }

    @Override
    public Integer getListCount(ListingFilterParameter fp) {
        EdsUser user = getUser();
        Integer userRole = fp.getViewAsId();
        String sqlS = "";
        String sorted = "";

        if (EdsRole.CLIENT.equals(userRole)) {
            sqlS += " mu.receiver = '" + user.getObjectID() + "' ";
        } else {
            if (fp.getSearchType() == 1) {
                if ("".equals(sqlS)) {
                    sqlS += " mu.receiver <> '" + user.getObjectID() + "' ";
                } else {
                    sqlS += " and mu.receiver <> '" + user.getObjectID() + "' ";
                }
            } else {
                if ("".equals(sqlS)) {
                    sqlS += " mu.receiver = '" + user.getObjectID() + "' ";
                } else {
                    sqlS += " and mu.receiver = '" + user.getObjectID() + "' ";
                }
            }
        }

        if (fp.getGroupById() != null && fp.getGroupById() == 1) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.ADD + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 2) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.EDIT + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 3) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.DELETE + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 4) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.ALERT + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 5) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.NOTE + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 6) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.MESSAGE + "' ";
        }
        if (fp.getGroupById() != null && fp.getGroupById() == 7) {
            sorted = " and mu.eventtype = '" + EdsMyUpdate.STATUS_CHANGE + "' ";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct mu.id) from ");
        sql.append("(select * from ").append(getCompanyId()).append(".myupdate where companyid=").append(user.getCompany().getObjectID()).append(" order by id desc limit 2000) mu ");

        if (fp.getSearchType() == 1) {
            sql.append(" left outer join ").append(getCompanyId()).append(".teamemployee te on (te.employeeid=mu.receiver) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".projectemployee pe on (pe.employeeDepartmentId = te.id) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".team t on (t.id = te.teamid) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".project p on (p.id = pe.projectid) ");
        }
        sql.append(" where ");
        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.getEndDate().compareTo(fp.getStartDate()) >= 0) {
            sql.append(" to_date(to_char(mu.date,'yyyy-mm-dd'), 'yyyy-mm-dd') between to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') AND to_date('").append(format2.format(fp.getEndDate())).append("', 'yyyy-mm-dd') and ");
        } else if (fp.getStartDate() != null) {
            sql.append(" to_date(to_char(mu.date,'yyyy-mm-dd'), 'yyyy-mm-dd') <= to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') and ");
        }

        sql.append(sqlS);
        sql.append(sorted);

        if (userRole != null && !EdsRole.CLIENT.equals(userRole)) {
            if (fp.getSearchType() == 1) {
                sql.append(" and mu.privateUpdate = false ");
                if (EdsRole.DEFAULT.equals(userRole) || EdsRole.TL.equals(userRole) || EdsRole.PM.equals(userRole)) {
                    sql.append(" and (t.leaderid = ").append(user.getObjectID()).append(" ");
                    sql.append(" or p.managerid = ").append(user.getObjectID()).append(" ");
                    sql.append(" or p.backup_managerid = ").append(user.getObjectID());
                    sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
                } else if (EdsRole.MEM.equals(userRole) || EdsRole.ESS_USER.equals(userRole)) {
                    sql.append(" and (te.employeeid = ").append(user.getObjectID()).append(") ");
                }
            }
        }

        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    private EdsMyUpdate registerCalendarEventGuest(EdsGoogleCalendarEventGuests calendarEventGuests, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(calendarEventGuests.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CALENDAR_EVENT_GUEST);
    }

    public EdsMyUpdate registerTaskEditUpdate(EdsTask task, EdsUser updater, Date eventTime) {
        return registerTaskUpdate(task, updater, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.TASK_UPDATE);
    }

    /**
     * Registers update for taskassignee
     *
     * @param task
     * @param assignee  user who has been assigned to task
     * @param creator   user who has assigned assignee task
     * @param eventTime
     * @return
     */
    public EdsMyUpdate registerEmployeeTaskAssignUpdate(EdsTask task, EdsUser assignee, EdsUser creator, Date eventTime) {
        return registerEmployeeTaskUpdate(task, assignee, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.TASK_ASSIGN);
    }

    public EdsMyUpdate registerEmployeeTaskAssigneeDeleteUpdate(EdsTask task, EdsUser assignee, EdsUser deleter, Date eventTime) {
        return registerEmployeeTaskUpdate(task, assignee, deleter, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.TASK_ASSIGNEE_DELETE);
    }

    public EdsMyUpdate registerTaskDeleteUpdate(EdsTask task, EdsUser deleter, Date eventTime) {
        return registerTaskUpdate(task, deleter, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.TASK_DELETE);
    }

    public EdsMyUpdate registerProjectAddUpdate(EdsProject project, EdsUser creator, Date eventTime) {
        return registerProjectUpdate(project, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_ADD);
    }

    public EdsMyUpdate registerProjectEditUpdate(EdsProject project, EdsUser updater, Date eventTime) {
        return registerProjectUpdate(project, updater, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.PROJECT_UPDATE);
    }

    public EdsMyUpdate registerProjectDeleteUpdate(EdsProject project, EdsUser deleter, Date eventTime) {
        return registerProjectUpdate(project, deleter, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.PROJECT_DELETE);
    }

    public EdsMyUpdate registerProjectDeleteForManagersUpdate(EdsProject project, EdsUser manager, Date eventTime) {
        return registerProjectUpdate(project, manager, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.PROJECT_DELETE_FOR_MANAGERS);
    }

    public EdsMyUpdate registerProjectMemberAddUpdate(EdsProject project, EdsUser assignee, EdsUser creator, Date eventTime) {
        return registerProjectEmployeeUpdate(project, assignee, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_MEMBER_ASSIGN);
    }

    public EdsMyUpdate registerProjectMemberDeleteUpdate(EdsProject project, EdsUser member, EdsUser deleter, Date eventTime) {
        return registerProjectEmployeeUpdate(project, member, deleter, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.PROJECT_MEMBER_DELETE);
    }

    public EdsMyUpdate registerProjectBackupManagerAssignUpdate(EdsProject project, EdsUser backupManager, EdsUser creator, Date eventTime) {
        return registerProjectEmployeeUpdate(project, backupManager, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_BACKUP_MANAGER_ASSIGN);
    }

    public EdsMyUpdate registerProjectManagerAssignUpdate(EdsProject project, EdsUser manager, EdsUser creator, Date eventTime) {
        return registerProjectEmployeeUpdate(project, manager, creator, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_MANAGER_ASSIGN);
    }

    public EdsMyUpdate registerProjectImportTasksFromMSProject(EdsProject project, EdsUser creator, Date eventDate) {
        return registerProjectUpdate(project, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.PROJECT_IMPORT_TASKS_FROM_MS_PROJECT);
    }

    public EdsMyUpdate registerUserUpdate(EdsUser user, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(user.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.USER);
    }

    public EdsMyUpdate registerClientContactAddUpdate(EdsUser user, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.USER_CLIENT_CONTACT_ADD);
    }

    public EdsMyUpdate registerClientContactEditUpdate(EdsUser user, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.USER_CLIENT_CONTACT_EDIT);
    }

    public EdsMyUpdate registerClientContactDeleteUpdate(EdsUser user, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.USER_CLIENT_CONTACT_DELETE);
    }

    public EdsMyUpdate registerEmployeeAddUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.USER_EMPLOYEE_ADD);
    }

    public EdsMyUpdate registerEmployeeEditUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.USER_EMPLOYEE_EDIT);
    }

    public EdsMyUpdate registerEmployeeDeleteUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.USER_EMPLOYEE_DELETE);
    }

    public EdsMyUpdate registerEmployeeTerminateUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerUserUpdate(user, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.USER_EMPLOYEE_TERMINATE);
    }

    public EdsMyUpdate registerDepartmentEmployeeUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(employeeDep.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.DEPARTMENT);
    }

    public EdsMyUpdate registerLeaveRequestUpdate(Integer requestID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(requestID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.LEAVE_REQUEST);
    }

    public EdsMyUpdate registerLeaveRequestDeleteUpdate(Integer requestID, String requestData, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerLeaveRequestUpdate(requestID, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.LEAVE_REQUEST_DELETE);
        myUpdate.setItemName(requestData);
        return myUpdate;
    }

    // Event
    public EdsMyUpdate registerEventUpdate(EdsEvent event, EdsUser creator, Date eventDate, String eventType, String updateType) {
        return registerMyUpdate(event.getObjectID(), creator, eventDate, eventType, updateType, MyUpdateTypeManager.EVENT);
    }

    public EdsMyUpdate registerEventAdd(EdsEvent event, EdsUser creator, Date eventDate) {
        return registerEventUpdate(event, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.EVENT_ADD);
    }

    public EdsMyUpdate registerEventEdit(EdsEvent event, EdsUser creator, Date eventDate) {
        return registerEventUpdate(event, creator, eventDate, EdsMyUpdate.EDIT, MyUpdateTypeManager.EVENT_EDIT);
    }

    public EdsMyUpdate registerEventDelete(EdsEvent event, EdsUser creator, Date eventDate) {
        return registerEventUpdate(event, creator, eventDate, EdsMyUpdate.DELETE, MyUpdateTypeManager.EVENT_DELETE);
    }

    private EdsMyUpdate registerReceiverMyUpdate(Integer affectedId, Integer receiverID, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType) {
        EdsCompany company = eventCauser.getCompany();
        EdsMyUpdate update = new EdsMyUpdate();
        update.setReceiverType(EdsTrusteeType.USER);
        update.setReceiver(receiverID);
        update.setCompanyID(company.getObjectID());
        update.setDate(eventTime);

        update.setInducerID(eventCauser.getObjectID());
        update.setInducerType(EdsTrusteeType.USER);
        update.setAffectedID(affectedId);
        update.setAffectedType(EdsTrusteeType.ENTITY);
        update.setEventType(eventType);

        EdsMyUpdateType type = myUpdateTypeManager.getType(updateType, entityType);

        if (type != null) {
            update.setTypeCode(type.getCode());
        }
        create(update);
        return update;
    }

    public EdsMyUpdate registerDepartmentEmployeeAddUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerDepartmentEmployeeUpdate(employeeDep, receiver, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.DEPARTMENT_EMPLOYEE_ADD);
    }

    public EdsMyUpdate registerDepartmentEmployeeDeleteUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerDepartmentEmployeeUpdate(employeeDep, receiver, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.DEPARTMENT_EMPLOYEE_DELETE);
    }

    public EdsMyUpdate registerDepartmentUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(edsDepartment.getObjectID(), eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.DEPARTMENT);
    }

    public EdsMyUpdate registerDepartmentAddUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime) {
        return registerDepartmentUpdate(edsDepartment, eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.DEPARTMENT_ADD);
    }

    public EdsMyUpdate registerDepartmentEditUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime) {
        return registerDepartmentUpdate(edsDepartment, eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.DEPARTMENT_EDIT);
    }

    public EdsMyUpdate registerDepartmentDeleteUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime) {
        return registerDepartmentUpdate(edsDepartment, eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.DEPARTMENT_DELETE);
    }

    public EdsMyUpdate registerDepartmentLeaderEditUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerReceiverMyUpdate(employeeDep.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.DEPARTMENT_EDIT_FOR_LEADER, MyUpdateTypeManager.DEPARTMENT);
    }

    public EdsMyUpdate registerDepartmentLeaderDeleteUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerReceiverMyUpdate(employeeDep.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.DEPARTMENT_DELETE_FOR_LEADER, MyUpdateTypeManager.DEPARTMENT);
    }

    public EdsMyUpdate registerProjectManagerEditUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerReceiverMyUpdate(project.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.PROJECT_UPDATE_FOR_MANAGER, MyUpdateTypeManager.PROJECT);
    }

    public EdsMyUpdate registerProjectBackupManagerEditUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime) {
        return registerReceiverMyUpdate(project.getObjectID(), receiver.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.PROJECT_UPDATE_FOR_BACKUP_MANAGER, MyUpdateTypeManager.PROJECT);
    }

    public EdsMyUpdate registerRfqUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE);
    }

    public EdsMyUpdate registerRfqAddUpdate(EdsRFQ rfq, EdsUser eventCauser, Date eventTime) {
        return registerRfqUpdate(rfq.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE_ADD);
    }

    public EdsMyUpdate registerRfqEditUpdate(EdsRFQ rfq, EdsUser eventCauser, Date eventTime) {
        return registerRfqUpdate(rfq.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE_EDIT);
    }

    public EdsMyUpdate registerRfqDelete(EdsRFQ rfq, EdsUser eventCauser, Date eventTime) {
        return registerRfqUpdate(rfq.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_QUOTE_DELETE);
    }

    public EdsMyUpdate registerStockTransferUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER);
    }

    public EdsMyUpdate registerStockTransferAddUpdate(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_ADD);
    }

    public EdsMyUpdate registerStockTransferEditUpdate(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_EDIT);
    }

    public EdsMyUpdate registerStockTransferDelete(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_DELETE);
    }

    @Override
    public EdsMyUpdate registerStockTransferSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(expenseId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_SEND_TO_APPROVER, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER);
    }

    @Override
    public EdsMyUpdate registerStockTransferApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(expenseId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_APPROVE);
    }

    @Override
    public EdsMyUpdate registerStockTransferDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(expenseId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_DECLINE);
    }

    @Override
    public EdsMyUpdate registerStockTransferTransferredUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime) {
        return registerStockTransferUpdate(expenseId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_TRANSFER_TRANSFERRED);
    }

    public EdsMyUpdate registerRfpUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE);
    }

    public EdsMyUpdate registerRfpAddUpdate(EdsRFP rfp, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(rfp.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE_ADD);
    }

    public EdsMyUpdate registerRfpEditUpdate(EdsRFP rfp, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(rfp.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE_EDIT);
    }

    public EdsMyUpdate registerRfpDelete(EdsRFP rfp, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(rfp.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_REQUEST_FOR_PURCHASE_DELETE);
    }

    @Override
    public EdsMyUpdate registerRFPSubmittedToManager(EdsRFP invoice, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.RFP_SUBMITTED_TO_MANAGER);
    }

    @Override
    public EdsMyUpdate registerRfpManagerApproveUpdate(EdsRFP invoice, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.RFP_MANAGER_APPROVE);
    }

    @Override
    public EdsMyUpdate registerRfpManagerRejectUpdate(EdsRFP invoice, EdsUser eventCauser, Date eventTime) {
        return registerRfpUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.RFP_MANAGER_REJECT);
    }


    public EdsMyUpdate registerStockAdjustmentUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT);
    }

    public EdsMyUpdate registerStockAdjustmentAddUpdate(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockAdjustmentUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_ADD);
    }

    public EdsMyUpdate registerStockAdjustmentEditUpdate(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockAdjustmentUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_EDIT);
    }

    public EdsMyUpdate registerStockAdjustmentDelete(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime) {
        return registerStockAdjustmentUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_DELETE);
    }

    @Override
    public EdsMyUpdate registerStockAdjustmentSendToApprover(Integer adjustmentId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(adjustmentId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_SEND_TO_APPROVER, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT);
    }

    @Override
    public EdsMyUpdate registerStockAdjustmentApproveUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerStockAdjustmentUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_APPROVE);
    }

    @Override
    public EdsMyUpdate registerStockAdjustmentDeclineUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerStockAdjustmentUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_STOCK_ADJUSTMENT_DECLINE);
    }

    public EdsMyUpdate registerPlacementUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.HRMS_PLACEMENT);
    }

    public EdsMyUpdate registerPlacementAddUpdate(EdsPlacement transfer, EdsUser eventCauser, Date eventTime) {
        return registerPlacementUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.HRMS_PLACEMENT_ADD);
    }

    public EdsMyUpdate registerPlacementEditUpdate(EdsPlacement transfer, EdsUser eventCauser, Date eventTime) {
        return registerPlacementUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.HRMS_PLACEMENT_EDIT);
    }

    public EdsMyUpdate registerPlacementDelete(EdsPlacement transfer, EdsUser eventCauser, Date eventTime) {
        return registerPlacementUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.HRMS_PLACEMENT_DELETE);
    }

    @Override
    public EdsMyUpdate registerPlacementSendToApprover(Integer adjustmentId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(adjustmentId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.HRMS_PLACEMENT_SEND_TO_APPROVER, MyUpdateTypeManager.HRMS_PLACEMENT);
    }

    @Override
    public EdsMyUpdate registerPlacementApproveUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerPlacementUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.HRMS_PLACEMENT_APPROVE);
    }

    @Override
    public EdsMyUpdate registerPlacementDeclineUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerPlacementUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.HRMS_PLACEMENT_DECLINE);
    }

    public EdsMyUpdate registerManualJournalUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL);
    }

    public EdsMyUpdate registerManualJournalAddUpdate(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime) {
        return registerManualJournalUpdate(manualJournal.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL_ADD);
    }

    public EdsMyUpdate registerManualJournalEditUpdate(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime) {
        return registerManualJournalUpdate(manualJournal.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL_EDIT);
    }

    public EdsMyUpdate registerManualJournalDelete(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime) {
        return registerManualJournalUpdate(manualJournal.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL_DELETE);
    }

    public EdsMyUpdate registerManualJournalVoid(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime) {
        return registerManualJournalUpdate(manualJournal.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_MANUAL_JOURNAL_VOID);
    }

    public EdsMyUpdate registerSaleInvoiceUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, Integer clientContactId, BigDecimal amount) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.SALES_INVOICE, clientContactId, null, null, amount);
    }

    public EdsMyUpdate registerSaleInvoicePaymentUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, Integer relationId, BigDecimal amount) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.SALES_INVOICE, null, relationId, null, amount);
    }

    public EdsMyUpdate registerSaleInvoiceAddUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.SALES_INVOICE_ADD, null, invoice.getTotal());
    }

    public EdsMyUpdate registerSaleInvoiceEditUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.SALES_INVOICE_EDIT, null, invoice.getTotal());
    }

    public EdsMyUpdate registerSaleInvoiceDeleteUpdate(EdsSaleInvoice invoice, String invoiceName, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.SALES_INVOICE_DELETE, null, invoice.getTotal());
        myUpdate.setItemName(invoiceName);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerSaleInvoiceManagerRejectUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_MANAGER_REJECT, null, null);
    }

    @Override
    public EdsMyUpdate registerSaleInvoiceManagerApproveUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_MANAGER_APPROVE, null, null);
    }

    @Override
    public EdsMyUpdate registerSaleInvoiceSubmittedToManager(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_SUBMITTED_TO_MANAGER, null, null);
    }

    @Override
    public EdsMyUpdate registerSalesInvoicePaymentVoid(EdsInvoicePayment payment, EdsUser user, Date time) {
        return registerSaleInvoicePaymentUpdate(payment.getInvoice().getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_PAYMENT_VOID, payment.getObjectID(), payment.getAmount());
    }

    @Override
    public EdsMyUpdate registerSalesInvoicePaymentDeleteUpdate(EdsInvoicePayment payment, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerSaleInvoicePaymentUpdate(payment.getInvoice().getObjectID(), creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.SALES_INVOICE_PAYMENT_DELETE, payment.getObjectID(), payment.getAmount());
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerSaleInvoicePaymentReceive(EdsInvoicePayment payment, EdsUser receiver, Date eventTime) {
        return registerSaleInvoicePaymentUpdate(payment.getInvoice().getObjectID(), receiver, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_PAYMENT_RECEIVE, payment.getObjectID(), payment.getAmount());
    }

    public EdsMyUpdate registerSaleInvoiceSendToClient(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime, Integer clientContactId) {
        return registerSaleInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_SEND_TO_CLIENT, clientContactId, null);
    }

    @Override
    public EdsMyUpdate registerSaleInvoiceConvertedFromSaleQuoteUpdate(Integer invoiceId, String quoteNumber, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerSaleInvoiceUpdate(invoiceId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_CONVERTED_FROM_SALES_QUOTE, null, null);
        myUpdate.setItemName(quoteNumber);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerSalesInvoiceAddCreditNote(EdsSaleInvoice saleInvoice, EdsUser user, Date time) {
        return registerSaleInvoiceUpdate(saleInvoice.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_ADD_CREDIT_NOTE, null, null);
    }

    public EdsMyUpdate registerSaleQuoteUpdate(Integer quoteObjectId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount) {
        return registerMyUpdate(quoteObjectId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.SALES_QUOTE, null, null, null, amount);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteAddUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.SALES_QUOTE_ADD, quote.getTotal());
    }

    @Override
    public EdsMyUpdate registerSaleQuoteEditUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.SALES_QUOTE_EDIT, quote.getTotal());
    }

    @Override
    public EdsMyUpdate registerSaleQuoteDeleteUpdate(EdsSaleQuote quote, String quoteNumber, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.SALES_QUOTE_DELETE, quote.getTotal());
        myUpdate.setItemName(quoteNumber);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerSaleQuoteClientApproveUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_CLIENT_APPROVE, null);

    }

    @Override
    public EdsMyUpdate registerSaleQuoteRejectUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_REJECT, null);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteManagerApproveUpdate(EdsSaleQuote quote, EdsUser manager, EdsUser receiver, Date eventTime) {
        return registerReceiverMyUpdate(quote.getObjectID(), receiver.getObjectID(), manager, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_MANAGER_APPROVE, MyUpdateTypeManager.SALES_QUOTE);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteManagerRejectUpdate(EdsSaleQuote quote, EdsUser manager, EdsUser receiver, Date eventTime) {
        return registerReceiverMyUpdate(quote.getObjectID(), receiver.getObjectID(), manager, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_MANAGER_REJECT, MyUpdateTypeManager.SALES_QUOTE);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteClosed(EdsSaleQuote quote, EdsUser creator, Date time) {
        return registerSaleQuoteUpdate(quote.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_CLOSED, null);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteConvertToSaleOrderUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_CONVERT_TO_SALE_ORDER, null);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteSendToClient(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_SEND_TO_CLIENT, null);
    }

    @Override
    public EdsMyUpdate registerSaleQuoteSubmittedToManager(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime) {
        return registerSaleQuoteUpdate(quote.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_QUOTE_SUBMITTED_TO_MANAGER, null);
    }

    public EdsMyUpdate registerSaleOrderUpdate(Integer quoteObjectId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount) {
        return registerMyUpdate(quoteObjectId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.SALES_ORDER, null, null, null, amount);
    }

    @Override
    public EdsMyUpdate registerSaleOrderAddUpdate(EdsSaleQuote quote, EdsUser creator, Date time) {
        return registerSaleOrderUpdate(quote.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.SALES_ORDER_ADD, quote.getTotal());
    }

    @Override
    public EdsMyUpdate registerSaleOrderConvertFromSQ(EdsSaleQuote quote, EdsUser creator, Date time) {
        return registerSaleOrderUpdate(quote.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.SALES_ORDER_CONVERT_FROM_SQ, null);
    }

    @Override
    public EdsMyUpdate registerSaleOrderEditUpdate(EdsSaleQuote quote, EdsUser creator, Date time) {
        return registerSaleOrderUpdate(quote.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.SALES_ORDER_EDIT, quote.getTotal());
    }

    @Override
    public EdsMyUpdate registerSaleOrderDeleteUpdate(EdsSaleQuote quote, String orderNumber, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerSaleOrderUpdate(quote.getObjectID(), creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.SALES_ORDER_DELETE, quote.getTotal());
        myUpdate.setItemName(orderNumber);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerSaleOrderPickListUpdate(EdsSaleQuote pickList, EdsUser creator, Date time) {
        return registerSaleOrderUpdate(pickList.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_ORDER_PICKLIST, pickList.getTotal());
    }

    @Override
    public EdsMyUpdate registerSaleOrderClosed(EdsSaleQuote order, EdsUser creator, Date time) {
        return registerSaleOrderUpdate(order.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_ORDER_CLOSED, null);
    }

    @Override
    public EdsMyUpdate registerSaleOrderManagerRejectUpdate(EdsSaleQuote order, EdsUser eventCauser, Date eventTime) {
        return registerSaleOrderUpdate(order.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_ORDER_MANAGER_REJECT, null);
    }

    @Override
    public EdsMyUpdate registerSaleOrderManagerApproveUpdate(EdsSaleQuote order, EdsUser eventCauser, Date eventTime) {
        return registerSaleOrderUpdate(order.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_ORDER_MANAGER_APPROVE, null);
    }

    @Override
    public EdsMyUpdate registerSaleOrderSubmittedToManager(EdsSaleQuote order, EdsUser eventCauser, Date eventTime) {
        return registerSaleOrderUpdate(order.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_ORDER_SUBMITTED_TO_MANAGER, null);
    }

    public EdsMyUpdate registerPurchaseOrderUpdate(Integer orderId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount) {
        return registerMyUpdate(orderId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.PURCHASE_ORDER, null, null, null, amount);
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderAddUpdate(EdsPurchaseOrder order, EdsUser creator, Date time) {
        return registerPurchaseOrderUpdate(order.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.PURCHASE_ORDER_ADD, order.getTotal());
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderEditUpdate(EdsPurchaseOrder order, EdsUser creator, Date time) {
        return registerPurchaseOrderUpdate(order.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.PURCHASE_ORDER_EDIT, order.getTotal());
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderDeleteUpdate(EdsPurchaseOrder order, String orderNumber, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerPurchaseOrderUpdate(order.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.PURCHASE_ORDER_DELETE, order.getTotal());
        myUpdate.setItemName(orderNumber);
        return myUpdate;
    }

    //for Approve & Email
    @Override
    public EdsMyUpdate registerPurchaseOrderSendToClient(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time) {
        return registerPurchaseOrderUpdate(purchaseOrder.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_SEND_TO_CLIENT, null);
    }
    //for Approve

    @Override
    public EdsMyUpdate registerPurchaseOrderClientApproveUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time) {
        return registerPurchaseOrderUpdate(purchaseOrder.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_APPROVE, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderReceivedUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time) {
        return registerPurchaseOrderUpdate(purchaseOrder.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_RECEIVED, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderPartialReceivedUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time) {
        return registerPurchaseOrderUpdate(purchaseOrder.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_PARTIAL_RECEIVED, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderSubmittedToManager(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time) {
        return registerPurchaseOrderUpdate(purchaseOrder.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_SUBMITTED_TO_MANAGER, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseOrderClosed(EdsPurchaseOrder order, EdsUser creator, Date time) {
        return registerPurchaseOrderUpdate(order.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_ORDER_CLOSED, null);
    }

    public EdsMyUpdate registerPurchaseInvoiceUpdate(Integer purchaseInvoiceID, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount) {
        return registerMyUpdate(purchaseInvoiceID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.PURCHASE_INVOICE, null, null, null, amount);
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceAddUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser creator, Date time) {
        return registerPurchaseInvoiceUpdate(purchaseInvoice.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.PURCHASE_INVOICE_ADD, purchaseInvoice.getTotal());
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceEditUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser creator, Date time) {
        return registerPurchaseInvoiceUpdate(purchaseInvoice.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.PURCHASE_INVOICE_EDIT, purchaseInvoice.getTotal());
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceDeleteUpdate(Integer entityID, String invoiceName, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerPurchaseInvoiceUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.PURCHASE_INVOICE_DELETE, null);
        myUpdate.setItemName(invoiceName);
        return myUpdate;
    }

    //for Approve
    @Override
    public EdsMyUpdate registerPurchaseInvoiceApproveUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser user, Date time) {
        return registerPurchaseInvoiceUpdate(purchaseInvoice.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_APPROVE, null);
    }

    //for Add Credit Note
    @Override
    public EdsMyUpdate registerPurchaseInvoiceAddCreditNote(EdsPurchaseInvoice purchaseInvoice, EdsUser user, Date time) {
        return registerPurchaseInvoiceUpdate(purchaseInvoice.getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_ADD_CREDIT_NOTE, null);
    }

    //for Payment Void
    @Override
    public EdsMyUpdate registerPurchaseInvoicePaymentVoid(EdsInvoicePayment payment, EdsUser user, Date time) {
        return registerPurchaseInvoiceUpdate(payment.getInvoice().getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_PAYMENT_VOID, payment.getAmount());
    }
    //for Payment Delete


    @Override
    public EdsMyUpdate registerPurchaseInvoicePaymentDeleteUpdate(EdsInvoicePayment payment, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerPurchaseInvoiceUpdate(payment.getInvoice().getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_PAYMENT_DELETE, payment.getAmount());
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }


    public EdsMyUpdate registerRentalOrderUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(invoiceId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER);
    }

    public EdsMyUpdate registerRentalOrderAddUpdate(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime) {
        return registerRentalOrderUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_ADD);
    }

    public EdsMyUpdate registerRentalOrderEditUpdate(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime) {
        return registerRentalOrderUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_EDIT);
    }

    public EdsMyUpdate registerRentalOrderDelete(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime) {
        return registerRentalOrderUpdate(transfer.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_DELETE);
    }

    @Override
    public EdsMyUpdate registerRentalOrderSendToApprover(Integer adjustmentId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(adjustmentId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_SEND_TO_APPROVER, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_ADD);
    }

    @Override
    public EdsMyUpdate registerRentalOrderApproveUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerRentalOrderUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_APPROVE);
    }

    @Override
    public EdsMyUpdate registerRentalOrderDeclineUpdate(Integer adjustmentId, EdsUser eventCauser, Date eventTime) {
        return registerRentalOrderUpdate(adjustmentId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_RENTAL_ORDER_DELETE);
    }

    public EdsMyUpdate registerBuildAssemblyUpdate(Integer buildAssemblyId, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(buildAssemblyId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY);
    }

    public EdsMyUpdate registerBuildAssemblyAddUpdate(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime) {
        return registerBuildAssemblyUpdate(buildAssembly.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_ADD);
    }

    public EdsMyUpdate registerBuildAssemblyEditUpdate(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime) {
        return registerBuildAssemblyUpdate(buildAssembly.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_EDIT);
    }

    public EdsMyUpdate registerBuildAssemblyDelete(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime) {
        return registerBuildAssemblyUpdate(buildAssembly.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_DELETE);
    }

    @Override
    public EdsMyUpdate registerBuildAssemblySendToApprover(Integer buildAssemblyId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(buildAssemblyId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_SEND_TO_APPROVER, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_ADD);
    }

    @Override
    public EdsMyUpdate registerBuildAssemblyApproveUpdate(Integer buildAssemblyId, EdsUser eventCauser, Date eventTime) {
        return registerBuildAssemblyUpdate(buildAssemblyId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_APPROVE);
    }

    @Override
    public EdsMyUpdate registerBuildAssemblyDeclineUpdate(Integer buildAssemblyId, EdsUser eventCauser, Date eventTime) {
        return registerBuildAssemblyUpdate(buildAssemblyId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ACCOUNTING_BUILD_ASSEMBLY_DELETE);
    }

    @Override
    public EdsMyUpdate registerAdditionalPayment(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.ADDITIONAL_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentAdd(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ADDITIONAL_PAYMENT_ADD);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentEdit(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ADDITIONAL_PAYMENT_EDIT);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentDraft(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.ADDITIONAL_PAYMENT_DRAFT);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentDelete(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ADDITIONAL_PAYMENT_DELETED);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentReject(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ADDITIONAL_PAYMENT_REJECTED);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentApprove(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ADDITIONAL_PAYMENT_APPROVED);
    }

    @Override
    public EdsMyUpdate registerAdditionalPaymentSubmittedToManager(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime) {
        return registerAdditionalPayment(additionalPayment.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ADDITIONAL_PAYMENT_SUBMITTED_TO_MANAGER);
    }


    //for Purchase Invoice Pay
    @Override
    public EdsMyUpdate registerPurchaseInvoicePay(EdsInvoicePayment payment, EdsUser user, Date time) {
        return registerPurchaseInvoiceUpdate(payment.getInvoice().getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_PAYMENT_PAY, payment.getAmount());
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceManagerRejectUpdate(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerPurchaseInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_MANAGER_REJECT, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceManagerApproveUpdate(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerPurchaseInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_MANAGER_APPROVE, null);
    }

    @Override
    public EdsMyUpdate registerPurchaseInvoiceSubmittedToManager(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime) {
        return registerPurchaseInvoiceUpdate(invoice.getObjectID(), eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_SUBMITTED_TO_MANAGER, null);
    }

    public EdsMyUpdate registerProductUpdate(Integer productid, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(productid, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.PRODUCT);
    }

    @Override
    public EdsMyUpdate registerProductAddUpdate(EdsItem product, EdsUser creator, Date time) {
        return registerProductUpdate(product.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.PRODUCT_ADD);
    }

    @Override
    public EdsMyUpdate registerProductEditUpdate(EdsItem product, EdsUser creator, Date time) {
        return registerProductUpdate(product.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.PRODUCT_EDIT);
    }

    @Override
    public EdsMyUpdate registerProductDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerProductUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.PRODUCT_DELETE);
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    public EdsMyUpdate registerFixedAssetUpdate(Integer fixedAssetID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(fixedAssetID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.FIXED_ASSET);
    }

    @Override
    public EdsMyUpdate registerFixedAssetAddUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time) {
        return registerFixedAssetUpdate(fixedAsset.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.FIXED_ASSET_ADD);
    }

    @Override
    public EdsMyUpdate registerFixedAssetOwnerAddUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time) {
        return registerFixedAssetUpdate(fixedAsset.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.FIXED_ASSET_OWNER_ADD);
    }

    @Override
    public EdsMyUpdate registerFixedAssetOwnerEditUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time) {
        return registerFixedAssetUpdate(fixedAsset.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.FIXED_ASSET_OWNER_EDIT);
    }

    @Override
    public EdsMyUpdate registerFixedAssetEditUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time) {
        return registerFixedAssetUpdate(fixedAsset.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.FIXED_ASSET_EDIT);
    }

    @Override
    public EdsMyUpdate registerFixedAssetDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerFixedAssetUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.FIXED_ASSET_DELETE);
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerFixedAssetDisposeUpdate(Integer entityID, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerFixedAssetUpdate(entityID, creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.FIXED_ASSET_DISPOSE);
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    public EdsMyUpdate registerCheckUpdate(Integer checkID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(checkID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CHECK);
    }

    @Override
    public EdsMyUpdate registerCheckAddUpdate(EdsBankCheck bankCheck, EdsUser creator, Date time) {
        return registerCheckUpdate(bankCheck.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.CHECK_ADD);
    }

    @Override
    public EdsMyUpdate registerCheckEditUpdate(EdsBankCheck bankCheck, EdsUser creator, Date time) {
        return registerCheckUpdate(bankCheck.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.CHECK_EDIT);
    }

    @Override
    public EdsMyUpdate registerCheckDeleteUpdate(Integer entityID, String itemName, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerCheckUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.CHECK_DELETE);
        myUpdate.setItemName(itemName);
        return myUpdate;
    }

    public EdsMyUpdate registerBankAccountUpdate(Integer bankAccountID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(bankAccountID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.BANK_ACCOUNT);
    }

    @Override
    public EdsMyUpdate registerBankAccountAddUpdate(EdsBankAccount bankAccount, EdsUser creator, Date time) {
        return registerBankAccountUpdate(bankAccount.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.BANK_ACCOUNT_ADD);
    }

    @Override
    public EdsMyUpdate registerBankAccountEditUpdate(EdsBankAccount bankAccount, EdsUser creator, Date time) {
        return registerBankAccountUpdate(bankAccount.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.BANK_ACCOUNT_EDIT);
    }

    @Override
    public EdsMyUpdate registerBankAccountDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerBankAccountUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.BANK_ACCOUNT_DELETE);
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    public EdsMyUpdate registerChartOfAccountUpdate(Integer accountID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerMyUpdate(accountID, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.CHART_OF_ACCOUNT);
    }


    @Override
    public EdsMyUpdate registerChartOfAccountAddUpdate(EdsAccount chartOfAccount, EdsUser creator, Date time) {
        return registerChartOfAccountUpdate(chartOfAccount.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.CHART_OF_ACCOUNT_ADD);
    }

    @Override
    public EdsMyUpdate registerChartOfAccountEditUpdate(EdsAccount chartOfAccount, EdsUser creator, Date time) {
        return registerChartOfAccountUpdate(chartOfAccount.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.CHART_OF_ACCOUNT_EDIT);
    }

    @Override
    public EdsMyUpdate registerChartOfAccountDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time) {
        EdsMyUpdate myUpdate = registerChartOfAccountUpdate(entityID, creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.CHART_OF_ACCOUNT_DELETE);
        myUpdate.setItemName(customStringField);
        return myUpdate;
    }

    //Sale Invoice refund
    @Override
    public EdsMyUpdate registerSICreditNoteRefund(EdsInvoicePayment payment, EdsUser user, Date time) {
        return registerSaleInvoiceUpdate(payment.getCreditNote().getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SALES_INVOICE_REFUND, null, payment.getAmount());
    }

    //Purchase Invoice Refund
    @Override
    public EdsMyUpdate registerPICreditNoteRefund(EdsInvoicePayment payment, EdsUser user, Date time) {
        return registerPurchaseInvoiceUpdate(payment.getCreditNote().getObjectID(), user, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.PURCHASE_INVOICE_REFUND, null);
    }

    public EdsMyUpdate registerExpensePaymentAdd(EdsExpensePayment expensePayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(expensePayment.getObjectID(), creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.EXPENSE_PAYMENT_ADD, MyUpdateTypeManager.EXPENSE_PAYMENT, null, null, null, expensePayment.getAmount());
    }

    @Override
    public EdsMyUpdate registerExpenseReportUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount) {
        return registerMyUpdate(expenseId, eventCauser, eventTime, eventType, updateType, MyUpdateTypeManager.EXPENSE_REPORT, null, null, null, amount);
    }

    @Override
    public EdsMyUpdate registerExpenseReportAddUpdate(EdsExpenseReport expense, EdsUser eventCauser, Date eventTime) {
        return registerExpenseReportUpdate(expense.getObjectID(), eventCauser, eventTime, EdsMyUpdate.ADD, MyUpdateTypeManager.EXPENSE_REPORT_ADD, expense.getBaseTotal());
    }

    @Override
    public EdsMyUpdate registerExpenseReportEditUpdate(EdsExpenseReport expense, EdsUser eventCauser, Date eventTime) {
        return registerExpenseReportUpdate(expense.getObjectID(), eventCauser, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.EXPENSE_REPORT_EDIT, expense.getBaseTotal());
    }

    @Override
    public EdsMyUpdate registerExpenseReportDeleteUpdate(EdsExpenseReport expense, String expenseTitle, EdsUser eventCauser, Date eventTime) {
        EdsMyUpdate myUpdate = registerExpenseReportUpdate(expense.getObjectID(), eventCauser, eventTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.EXPENSE_REPORT_DELETE, expense.getBaseTotal());
        myUpdate.setItemName(expenseTitle);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerExpenseReportApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime) {
        return registerExpenseReportUpdate(expenseId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.EXPENSE_REPORT_APPROVE, null);
    }

    @Override
    public EdsMyUpdate registerExpenseReportDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime) {
        return registerExpenseReportUpdate(expenseId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.EXPENSE_REPORT_DECLINE, null);
    }

    @Override
    public EdsMyUpdate registerExpenseReportSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime) {
        return registerReceiverMyUpdate(expenseId, approverId, eventCauser, eventTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.EXPENSE_REPORT_GET_FROM_REPORTER, MyUpdateTypeManager.EXPENSE_REPORT);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunUpdate(Integer singlePayrunId, EdsUser creator, Date time, String eventType, String updateType) {
        return registerMyUpdate(singlePayrunId, creator, time, eventType, updateType, MyUpdateTypeManager.SINGLE_PAYRUN);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunAddUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.SINGLE_PAYRUN_ADD);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunEditUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.SINGLE_PAYRUN_EDIT);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunDeleteUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.SINGLE_PAYRUN_DELETE);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunSubmitUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SINGLE_PAYRUN_SUBMIT);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunApproveUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SINGLE_PAYRUN_APPROVE);
    }

    @Override
    public EdsMyUpdate registerSinglePayrunRejectUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time) {
        return registerSinglePayrunUpdate(payslipTableItem.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SINGLE_PAYRUN_REJECT);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunUpdate(Integer groupPayrunId, EdsUser creator, Date time, String eventType, String updateType) {
        return registerMyUpdate(groupPayrunId, creator, time, eventType, updateType, MyUpdateTypeManager.GROUP_PAYRUN);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunAddUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.GROUP_PAYRUN_ADD);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunEditUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.GROUP_PAYRUN_EDIT);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunDeleteUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.GROUP_PAYRUN_DELETE);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunSubmitUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.GROUP_PAYRUN_SUBMIT);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunApproveUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.GROUP_PAYRUN_APPROVE);
    }

    @Override
    public EdsMyUpdate registerGroupPayrunRejectUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time) {
        return registerGroupPayrunUpdate(payslipTable.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.GROUP_PAYRUN_REJECT);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceUpdate(Integer cashAdvanceId, EdsUser creator, Date time, String eventType, String updateType) {
        return registerMyUpdate(cashAdvanceId, creator, time, eventType, updateType, MyUpdateTypeManager.CASH_ADVANCE);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceAddUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.CASH_ADVANCE_ADD);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceEditUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.CASH_ADVANCE_EDIT);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceDeleteUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.DELETE, MyUpdateTypeManager.CASH_ADVANCE_DELETE);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceSubmitUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CASH_ADVANCE_SUBMIT);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceApproveUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CASH_ADVANCE_APPROVE);
    }

    @Override
    public EdsMyUpdate registerCashAdvanceRejectUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time) {
        return registerCashAdvanceUpdate(cashAdvance.getObjectID(), creator, time, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.CASH_ADVANCE_REJECT);
    }

    @Override
    public EdsMyUpdate registerPensionSchemeUpdate(Integer pensionSchemeId, EdsUser creator, Date time, String eventType, String updateType) {
        return registerMyUpdate(pensionSchemeId, creator, time, eventType, updateType, MyUpdateTypeManager.PENSION_SCHEME);
    }

    @Override
    public EdsMyUpdate registerPensionSchemeAddUpdate(EdsPensionScheme pensionScheme, EdsUser creator, Date time) {
        return registerPensionSchemeUpdate(pensionScheme.getObjectID(), creator, time, EdsMyUpdate.ADD, MyUpdateTypeManager.PENSION_SCHEME_ADD);
    }

    @Override
    public EdsMyUpdate registerPensionSchemeEditUpdate(EdsPensionScheme pensionScheme, EdsUser creator, Date time) {
        return registerPensionSchemeUpdate(pensionScheme.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.PENSION_SCHEME_EDIT);
    }

    @Override
    public EdsMyUpdate registerEndOfServiceSettingsUpdate(Integer endOfServiceSettingsId, EdsUser creator, Date time, String eventType, String updateType) {
        return registerMyUpdate(endOfServiceSettingsId, creator, time, eventType, updateType, MyUpdateTypeManager.END_OF_SERVICE_SETTINGS);
    }

    @Override
    public EdsMyUpdate registerEndOfServiceSettingsEditUpdate(EndOfServiceSettings endOfServiceSettings, EdsUser creator, Date time) {
        return registerEndOfServiceSettingsUpdate(endOfServiceSettings.getObjectID(), creator, time, EdsMyUpdate.EDIT, MyUpdateTypeManager.END_OF_SERVICE_SETTINGS_EDIT);
    }

    public EdsMyUpdate registerBankTransferAdd(Integer bankTransferId, EdsUser creator, Date eventDate) {
        return registerMyUpdate(bankTransferId, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.BANK_TRANSFER_ADD, MyUpdateTypeManager.BANK_TRANSFER);
    }

    public EdsMyUpdate registerBankTransferAppliedPayable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePayment.getObjectID(), creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.BANK_TRANSFER_APPLIED_PAYABLE, MyUpdateTypeManager.BANK_TRANSFER_APPLIED, null, null, null, invoicePayment.getAmount());
    }

    public EdsMyUpdate registerBankTransferAppliedReceivable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePayment.getObjectID(), creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.BANK_TRANSFER_APPLIED_RECEIVABLE, MyUpdateTypeManager.BANK_TRANSFER_APPLIED, null, null, null, invoicePayment.getAmount());
    }

    public EdsMyUpdate registerBankTransferEdit(Integer bankTransferId, EdsUser creator, Date eventDate) {
        return registerMyUpdate(bankTransferId, creator, eventDate, EdsMyUpdate.EDIT, MyUpdateTypeManager.BANK_TRANSFER_EDIT, MyUpdateTypeManager.BANK_TRANSFER);
    }

    public EdsMyUpdate registerBankTransferDelete(Integer bankTransferId, EdsUser creator, Date eventDate) {
        return registerMyUpdate(bankTransferId, creator, eventDate, EdsMyUpdate.DELETE, MyUpdateTypeManager.BANK_TRANSFER_DELETE, MyUpdateTypeManager.BANK_TRANSFER);
    }

    public EdsMyUpdate registerManualEntryAppliedReceivablePayable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePayment.getObjectID(), creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.MANUAL_JOURNAL_APPLIED_RECEIVABLE_PAYABLE, MyUpdateTypeManager.MANUAL_JOURNAL_APPLIED, null, null, null, invoicePayment.getAmount());
    }

    public EdsMyUpdate registerInvoicePaymentAdd(Integer invoicePaymentid, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePaymentid, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.INVOICE_PAYMENT_ADD, MyUpdateTypeManager.INVOICE_PAYMENT);
    }

    public EdsMyUpdate registerInvoicePaymentEdit(Integer invoicePaymentid, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePaymentid, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.INVOICE_PAYMENT_EDIT, MyUpdateTypeManager.INVOICE_PAYMENT);
    }

    public EdsMyUpdate registerInvoicePaymentDelete(Integer invoicePaymentid, EdsUser creator, Date eventDate) {
        return registerMyUpdate(invoicePaymentid, creator, eventDate, EdsMyUpdate.DELETE, MyUpdateTypeManager.INVOICE_PAYMENT_DELETE, MyUpdateTypeManager.INVOICE_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerBatchPaymentAdd(Integer batchPayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(batchPayment, creator, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.BATCH_PAYMENT_ADD, MyUpdateTypeManager.BATCH_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerBatchPaymentEdit(Integer batchPayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(batchPayment, creator, eventDate, EdsMyUpdate.EDIT, MyUpdateTypeManager.BATCH_PAYMENT_EDIT, MyUpdateTypeManager.BATCH_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerBatchPaymentDelete(Integer batchPayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(batchPayment, creator, eventDate, EdsMyUpdate.DELETE, MyUpdateTypeManager.BATCH_PAYMENT_DELETE, MyUpdateTypeManager.BATCH_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerBatchPaymentVoid(Integer batchPayment, EdsUser creator, Date eventDate) {
        return registerMyUpdate(batchPayment, creator, eventDate, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.BATCH_PAYMENT_VOID, MyUpdateTypeManager.BATCH_PAYMENT);
    }

    @Override
    public EdsMyUpdate registerRecurringInvoiceAdd(EdsRecurringInvoice invoice, EdsUser eventCauser, Date eventDate) {
        return registerMyUpdate(invoice.getObjectID(), eventCauser, eventDate, EdsMyUpdate.ADD, MyUpdateTypeManager.RECURRING_INVOICE_ADD, MyUpdateTypeManager.RECURRING_INVOICE, null, null, null, invoice.getTotal());
    }

    @Override
    public EdsMyUpdate registerRecurringInvoiceEdit(EdsRecurringInvoice invoice, EdsUser eventCauser, Date eventDate) {
        return registerMyUpdate(invoice.getObjectID(), eventCauser, eventDate, EdsMyUpdate.EDIT, MyUpdateTypeManager.RECURRING_INVOICE_EDIT, MyUpdateTypeManager.RECURRING_INVOICE, null, null, null, invoice.getTotal());
    }

    // Shift
    @Override
    public EdsMyUpdate registerShift(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.SHIFT);
    }

    @Override
    public EdsMyUpdate registerShiftAdd(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.SHIFT_ADD);
    }

    @Override
    public EdsMyUpdate registerShiftEdit(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.SHIFT_EDIT);
    }

    @Override
    public EdsMyUpdate registerShiftDraft(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.SHIFT_DRAFT);
    }

    @Override
    public EdsMyUpdate registerShiftDelete(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.SHIFT_DELETE);
    }

    @Override
    public EdsMyUpdate registerShiftReject(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SHIFT_REJECTED);
    }

    @Override
    public EdsMyUpdate registerShiftApprove(EdsShift shift, EdsUser eventCauser, Date evenTime) {
        return registerShift(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.SHIFT_APPROVED);
    }

    //Rotation

    @Override
    public EdsMyUpdate registerRotation(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.ROTATION);
    }

    @Override
    public EdsMyUpdate registerRotationAdd(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.ROTATION_ADD);
    }

    @Override
    public EdsMyUpdate registerRotationEdit(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.ROTATION_EDIT);
    }

    @Override
    public EdsMyUpdate registerRotationDraft(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.ROTATION_DRAFT);
    }

    @Override
    public EdsMyUpdate registerRotationDelete(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.ROTATION_DELETE);
    }

    @Override
    public EdsMyUpdate registerRotationReject(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ROTATION_REJECTED);
    }

    @Override
    public EdsMyUpdate registerRotationApprove(EdsRotation shift, EdsUser eventCauser, Date evenTime) {
        return registerRotation(shift.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.ROTATION_APPROVED);
    }

    @Override
    public EdsMyUpdate registerGroupPlacement(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.GROUP_PLACEMENT);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementAdd(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.GROUP_PLACEMENT_ADD);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementEdit(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.GROUP_PLACEMENT_EDIT);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementDraft(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.GROUP_PLACEMENT_DRAFT);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementDelete(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.GROUP_PLACEMENT_DELETE);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementReject(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.GROUP_PLACEMENT_REJECTED);

    }

    @Override
    public EdsMyUpdate registerGroupPlacementApprove(EdsGroupPlacement placement, EdsUser eventCauser, Date evenTime) {
        return registerGroupPlacement(placement.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.GROUP_PLACEMENT_APPROVED);

    }
    @Override
    public EdsMyUpdate registerCourseSchedule(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.COURSE_SCHEDULE);
    }

    @Override
    public EdsMyUpdate registerCourseScheduleAdd(EdsCourseSchedule productCategory, EdsUser eventCauser, Date evenTime) {
        return registerProductCategory(productCategory.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PRODUCT_CATEGORY_ADD);
    }

    @Override
    public EdsMyUpdate registerProductCategory(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.PRODUCT_CATEGORY);
    }

    @Override
    public EdsMyUpdate registerProductCategoryAdd(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime) {
        return registerProductCategory(productCategory.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.PRODUCT_CATEGORY_ADD);
    }

    @Override
    public EdsMyUpdate registerProductCategoryEdit(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime) {
        return registerProductCategory(productCategory.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.PRODUCT_CATEGORY_EDIT);
    }

    @Override
    public EdsMyUpdate registerProductCategoryDelete(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime) {
        return registerProductCategory(productCategory.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.PRODUCT_CATEGORY_DELETE);
    }

    @Override
    public EdsMyUpdate registerRecurringInvoiceDelete(EdsRecurringInvoice invoice, String invoiceName, EdsUser creator, Date eventDate) {
        EdsMyUpdate myUpdate = registerMyUpdate(invoice.getObjectID(), creator, eventDate, EdsMyUpdate.DELETE, MyUpdateTypeManager.RECURRING_INVOICE_DELETE, MyUpdateTypeManager.RECURRING_INVOICE, null, null, null, invoice.getTotal());
        myUpdate.setItemName(invoiceName);
        return myUpdate;
    }

    @Override
    public EdsMyUpdate registerCustomFormItemAdd(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.ADD,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_ADD, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    @Override
    public EdsMyUpdate registerCustomFormItemEdit(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.ADD,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_EDIT, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    @Override
    public EdsMyUpdate registerCustomFormItemDelete(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.DELETE,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_DELETE, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    @Override
    public EdsMyUpdate registerCustomFormSubmittedToManager(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.DELETE,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_SUBMITTED_TO_MANAGER, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    @Override
    public EdsMyUpdate registerCustomFormManagerApproveUpdate(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.DELETE,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_APPROVE, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    @Override
    public EdsMyUpdate registerCustomFormManagerRejectUpdate(Integer customFormItemId, EdsUser creator, Date eventDate, String formId) {
        return registerMyUpdate(customFormItemId, creator, eventDate, EdsMyUpdate.DELETE,
                MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_REJECT, MyUpdateTypeManager.CUSTOM_FORM_ITEM, null, null, formId, null);
    }

    private EdsMyUpdate registerNetworkUpdate(Integer affectedID, Integer receiverID, EdsUser eventCauser, Date eventTime, String eventType, String updateType) {
        return registerReceiverMyUpdate(affectedID, receiverID, eventCauser, eventTime, eventType, updateType, NETWORK);
    }

    public List<EdsMyUpdate> getAllUpdatesList(ListingFilterParameter fp, boolean isUpdatesList) {
        EdsUser user = getUser();
        Integer userRole = fp.getViewAsId();
        String sqlS = "";
        String sorted = "";

        if (EdsRole.CLIENT.equals(userRole)) {
            sqlS += " mu.receiver = " + user.getObjectID() + " ";
        } else {
            if (fp.getSearchType() == 1) {
                if ("".equals(sqlS)) {
                    sqlS += " mu.receiver <> '" + user.getObjectID() + "' ";
                } else {
                    sqlS += " and mu.receiver <> '" + user.getObjectID() + "' ";
                }
            } else {
                if ("".equals(sqlS)) {
                    sqlS += " mu.receiver = '" + user.getObjectID() + "' ";
                } else {
                    sqlS += " and mu.receiver = '" + user.getObjectID() + "' ";
                }
            }
        }

        if (fp.getGroupById() != null) {
            switch (fp.getGroupById()) {
                case 1 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.ADD + "' ";
                case 2 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.EDIT + "' ";
                case 3 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.DELETE + "' ";
                case 4 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.ALERT + "' ";
                case 5 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.NOTE + "' ";
                case 6 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.MESSAGE + "' ";
                case 7 -> sorted = " and mu.eventtype = '" + EdsMyUpdate.STATUS_CHANGE + "' ";
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct mu.id, mu.* from ");
        sql.append("(select * from ").append(getCompanyId()).append(".myupdate where companyid=").append(user.getCompany().getObjectID()).append(" order by id desc limit 2000) mu ");

        if (fp.getSearchType() == 1) {
            sql.append(" left outer join ").append(getCompanyId()).append(".teamemployee te on (te.employeeid=mu.receiver) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".projectemployee pe on (pe.employeeDepartmentId = te.id) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".team t on (t.id = te.teamid) ");
            sql.append(" left outer join ").append(getCompanyId()).append(".project p on (p.id = pe.projectid) ");
        }

        sql.append(" where ");
        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.getEndDate().compareTo(fp.getStartDate()) >= 0) {
            sql.append(" to_date(to_char(mu.date,'yyyy-mm-dd'), 'yyyy-mm-dd') between to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') AND to_date('").append(format2.format(fp.getEndDate())).append("', 'yyyy-mm-dd') and ");
        } else if (fp.getStartDate() != null) {
            sql.append(" to_date(to_char(mu.date,'yyyy-mm-dd'), 'yyyy-mm-dd') <= to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') and ");
        }
        sql.append(sqlS);
        sql.append(sorted);

        /*Sorted User Role*/
        if (userRole != null && !EdsRole.CLIENT.equals(userRole)) {
            if (fp.getSearchType() == 1) {
                sql.append(" and mu.privateUpdate = false ");
                if (EdsRole.DEFAULT.equals(userRole) || EdsRole.TL.equals(userRole) || EdsRole.PM.equals(userRole)) {
                    sql.append(" and (t.leaderid = ").append(user.getObjectID()).append(" ");
                    sql.append(" or p.managerid = ").append(user.getObjectID()).append(" ");
                    sql.append(" or p.backup_managerid = ").append(user.getObjectID());
                    sql.append(" or p.backup_managerid2=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid3=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid4=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid5=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid6=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid7=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid8=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid9=").append(user.getObjectID());
                    sql.append(" or p.backup_managerid10=").append(user.getObjectID()).append(") ");
                } else if (EdsRole.MEM.equals(userRole) || EdsRole.ESS_USER.equals(userRole)) {
                    sql.append(" and (te.employeeid = ").append(user.getObjectID()).append(") ");
                }
            }
        }

        sql.append(" order by mu.date desc ");
        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (isUpdatesList) {
            if (fp.getStart() > 0) {
                sql.append(" OFFSET ").append(fp.getStart());
            }
        }
        return findNative(sql.toString(), EdsMyUpdate.class);
    }

    public List<EdsMyUpdate> getPeerUpdates() {
        EdsUser user = getUser();
        return find("from EdsMyUpdate mu where mu.inducerID <> ? order by mu.date desc", user.getObjectID());
    }

    public ArrayList<EdsMyUpdate> getUpdatesForAffectedID(Integer affectedId, String typeParent) {
        StringBuilder sql = new StringBuilder();
        sql.append("select up.*,0 as clazz_ from ").append(getCompanyId()).append(".myupdate up ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype mut on mut.code=up.typeCode ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype p on p.id=mut.parentId ");

        sql.append("where up.affectedID= ").append(affectedId);
        sql.append(" and p.code= '").append(typeParent).append("'");
        return (ArrayList<EdsMyUpdate>) findNative(sql.toString(), EdsMyUpdate.class);
    }

    public List<EdsMyUpdate> getUpdatesForCustomForm(Integer affectedId, String typeParent, String formId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select up.*,0 as clazz_ from ").append(getCompanyId()).append(".myupdate up ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype mut on mut.code=up.typeCode ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype p on p.id=mut.parentId ");

        sql.append("where up.affectedID= ").append(affectedId);
        sql.append(" and up.formId= '").append(formId).append("'");
        sql.append(" and p.code= '").append(typeParent).append("'");
        return findNative(sql.toString(), EdsMyUpdate.class);
    }

    @Override
    public List<EdsMyUpdate> getCreditNoteRefundUpdatesForAffectedID(Integer affectedId, String updateType) {
        String status = "";
        if (MyUpdateTypeManager.PURCHASE_INVOICE.equals(updateType)) {
            status = "('" + MyUpdateTypeManager.PURCHASE_INVOICE_REFUND + "')";
        } else {
            status = "('" + MyUpdateTypeManager.SALES_INVOICE_REFUND + "')";
        }
        return find("select up from EdsMyUpdate up where up.affectedID = ? and up.typeCode in " + status, affectedId);
    }

    public List<EdsMyUpdate> getUpdates(Integer affectedId, String typeParent, String updateType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select up.*,0 as clazz_ from ").append(getCompanyId()).append(".myupdate up ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype mut on mut.code=up.typeCode ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype p on p.id=mut.parentId ");

        sql.append("where up.affectedID= ").append(affectedId);
        sql.append(" and p.code='").append(typeParent).append("'");
        sql.append(" and up.eventType='").append(updateType).append("'");
        return findNative(sql.toString(), EdsMyUpdate.class);
    }

    public EdsMyUpdate getUpdate(Integer affectedId, String updateType) {
        return (EdsMyUpdate) findSingle("select up from EdsMyUpdate up where up.affectedID = ? and up.typeCode = ?", affectedId, updateType);
    }

    public String getDeletedUpdateName(Integer affectedId, String typeParent) {
        Map<String, Object> params = new HashMap<>();
        params.put("affectedId", affectedId);
        params.put("parentCode", typeParent);
        params.put("eventType", EdsMyUpdate.DELETE);

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct up.itemName from ").append(getCompanyId()).append(".myupdate up ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype mut on mut.code=up.typeCode ");
        sql.append("left join ").append(getPublic()).append(".myupdatetype p on p.id=mut.parentId ");

        sql.append("where up.affectedID=:affectedId ");
        sql.append("and p.code=:parentCode ");
        sql.append("and up.eventType=:eventType ");
        List<String> res = findNativeByNamedParams(sql.toString(), params);

        return res.size() > 0 ? res.get(0) : null;
    }

    @Override
    public EdsMyUpdate registerOvertime(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.OVERTIME);
    }

    @Override
    public EdsMyUpdate registerOvertimeAdd(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.OVERTIME_ADD);
    }

    @Override
    public EdsMyUpdate registerOvertimeEdit(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.OVERTIME_EDIT);
    }

    @Override
    public EdsMyUpdate registerOvertimeDraft(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DRAFT, MyUpdateTypeManager.OVERTIME_DRAFT);
    }

    @Override
    public EdsMyUpdate registerOvertimeDelete(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.OVERTIME_DELETE);
    }

    @Override
    public EdsMyUpdate registerOvertimeReject(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.OVERTIME_REJECTED);
    }

    @Override
    public EdsMyUpdate registerOvertimeApprove(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime) {
        return registerOvertime(overtimeObject.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.OVERTIME_APPROVED);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployee(Integer id, EdsUser user, Date evenTime, String eventType, String updateType) {
        return registerMyUpdate(id, user, evenTime, eventType, updateType, MyUpdateTypeManager.BACKUPS_EMPLOYEE);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeAdd(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.ADD, MyUpdateTypeManager.BACKUPS_EMPLOYEE_ADD);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeEdit(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.BACKUPS_EMPLOYEE_EDIT);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeDelete(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.DELETE, MyUpdateTypeManager.BACKUPS_EMPLOYEE_DELETE);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeReject(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.BACKUPS_EMPLOYEE_REJECTED);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeApprove(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.BACKUPS_EMPLOYEE_APPROVED);
    }

    @Override
    public EdsMyUpdate registerBackupsEmployeeSubmittedToManager(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime) {
        return registerBackupsEmployee(backupsEmployee.getObjectID(), eventCauser, evenTime, EdsMyUpdate.STATUS_CHANGE, MyUpdateTypeManager.BACKUPS_EMPLOYEE_SUBMITTED_TO_MANAGER);
    }

    @Override
    public EdsMyUpdate registerCompanySettingsEdit(EdsCompanySettings companySettings, EdsUser eventCreator, Date eventTime) {
        return registerMyUpdate(companySettings.getObjectID(), eventCreator, eventTime, EdsMyUpdate.EDIT, MyUpdateTypeManager.COMPANY_SETTINGS_EDIT, MyUpdateTypeManager.COMPANY_SETTINGS);
    }
}
