package com.edatasite.workforce.gwt.core.server.db.myupdate;

import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
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
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPensionScheme;
import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jan 7, 2010
 * Time: 12:13:05 PM
 */
public interface MyUpdateManager extends Manager<EdsMyUpdate> {

    // Task related

    EdsMyUpdate registerTaskUpdate(EdsTask task, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerTaskAddUpdate(EdsTask task, EdsUser creator, Date eventTime);

    EdsMyUpdate registerTaskEditUpdate(EdsTask task, EdsUser updater, Date eventTime);

    EdsMyUpdate registerTaskDeleteUpdate(EdsTask task, EdsUser deleter, Date eventTime);

    //Employee task related

    EdsMyUpdate registerEmployeeTaskUpdate(EdsTask task, EdsUser assignee, EdsUser creator, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerEmployeeTaskStatusChangeUpdate(EdsEmployeeTask empTask, EdsUser updater, Date eventTime);

    EdsMyUpdate registerEmployeeTaskAssignUpdate(EdsTask task, EdsUser assignee, EdsUser creator, Date eventTime);

    EdsMyUpdate registerEmployeeTaskAssigneeDeleteUpdate(EdsTask task, EdsUser assignee, EdsUser deleter, Date eventTime);

    //Project related

    EdsMyUpdate registerProjectUpdate(EdsProject project, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerProjectAddUpdate(EdsProject project, EdsUser creator, Date eventTime);

    EdsMyUpdate registerProjectEditUpdate(EdsProject project, EdsUser updater, Date eventTime);

    EdsMyUpdate registerProjectDeleteUpdate(EdsProject project, EdsUser deleter, Date eventTime);

    EdsMyUpdate registerProjectDeleteForManagersUpdate(EdsProject project, EdsUser manager, Date eventTime);

    //Project Manager (Backup Manager) related

    EdsMyUpdate registerProjectManagerEditUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerProjectBackupManagerEditUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    //Project Employee related

    EdsMyUpdate registerProjectEmployeeUpdate(EdsProject project, EdsUser receiver, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerProjectMemberAddUpdate(EdsProject project, EdsUser assignee, EdsUser creator, Date eventTime);

    EdsMyUpdate registerProjectMemberDeleteUpdate(EdsProject project, EdsUser member, EdsUser deleter, Date eventTime);

    EdsMyUpdate registerProjectBackupManagerAssignUpdate(EdsProject project, EdsUser backupManager, EdsUser creator, Date eventTime);

    EdsMyUpdate registerProjectManagerAssignUpdate(EdsProject project, EdsUser manager, EdsUser creator, Date eventTime);

    EdsMyUpdate registerProjectImportTasksFromMSProject(EdsProject project, EdsUser creator, Date eventDate);

    //User related  (clientContact, employee)

    EdsMyUpdate registerUserUpdate(EdsUser user, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerClientContactAddUpdate(EdsUser user, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerClientContactEditUpdate(EdsUser user, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerClientContactDeleteUpdate(EdsUser user, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerEmployeeAddUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerEmployeeEditUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerEmployeeDeleteUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerEmployeeTerminateUpdate(EdsUser user, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    //Department related

    EdsMyUpdate registerDepartmentUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerDepartmentAddUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerDepartmentEditUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerDepartmentDeleteUpdate(EdsDepartment edsDepartment, EdsUser eventCauser, Date eventTime);

    // Department leader related

    EdsMyUpdate registerDepartmentLeaderEditUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerDepartmentLeaderDeleteUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    //Department Employee related

    EdsMyUpdate registerDepartmentEmployeeUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerDepartmentEmployeeAddUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerDepartmentEmployeeDeleteUpdate(EdsDepartment employeeDep, EdsUser receiver, EdsUser eventCauser, Date eventTime);

    //Client related

    EdsMyUpdate registerClientUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerClientAddUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerClientEditUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerClientDeleteUpdate(EdsCrmAccount client, EdsUser eventCauser, Date eventTime);

    //Issue related

    EdsMyUpdate registerIssueUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerIssueAddUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String issueType);

    EdsMyUpdate registerIssueEditUpdate(EdsIssue issue, EdsUser eventCauser, Date eventTime, String issueType);

    //Location related

    EdsMyUpdate registerLocationUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerLocationAddUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerLocationEditUpdate(EdsLocation location, EdsUser eventCauser, Date eventTime);

    //Opportunity related

    EdsMyUpdate registerOpportunityUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerOpportunityAddUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerOpportunityEditUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerOpportunityDeleteUpdate(EdsOpportunity opportunity, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerOpportunityDraft(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOpportunityReject(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOpportunityApprove(EdsOpportunity opportunity, EdsUser eventCauser, Date evenTime);

    //Lead related

    EdsMyUpdate registerLeadUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerLeadAddUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerLeadEditUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerLeadDeleteUpdate(EdsCrmContact lead, EdsUser eventCauser, Date eventTime);

    //WebForma related
    EdsMyUpdate registerWebFormUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerWebFormAddUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerWebFormEditUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerWebFormDeleteUpdate(EdsWebForm webForm, EdsUser eventCauser, Date eventTime);

    //Solution related
    EdsMyUpdate registerSolutionUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerSolutionAddUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSolutionEditUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSolutionDeleteUpdate(EdsSolution solution, EdsUser eventCauser, Date eventTime);

    //Case related
    EdsMyUpdate registerCaseUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerCaseAddUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerCaseEditUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerCaseDeleteUpdate(EdsCase crmcase, EdsUser eventCauser, Date eventTime);

    //TimeSlot related
    EdsMyUpdate registerTimeSlotUpdate(EdsTimeSlot timeSlot, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerTimeSlotAddUpdate(EdsTimeSlot timeSlot, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerTimeSlotEditUpdate(EdsTimeSlot timeSlot, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerTimeSlotDeleteUpdate(EdsTimeSlot timeSlot, EdsUser eventCauser, Date eventTime);

    //Holiday related
    EdsMyUpdate registerHolidayUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerHolidayAddUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerHolidayEditUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerHolidayDeleteUpdate(EdsHoliday holiday, EdsUser eventCauser, Date eventTime);

    //Account related
    EdsMyUpdate registerAccountUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerAccountAddUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerAccountEditUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerAccountDeleteUpdate(EdsCrmAccount account, EdsUser eventCauser, Date eventTime);

    //Campaign related

    EdsMyUpdate registerCampaignUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerCampaignAddUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerCampaignEditUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerCampaignDeleteUpdate(EdsCampaign campaign, EdsUser eventCauser, Date eventTime);

    // Contact category

    EdsMyUpdate registerContactCategoryUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerContactCategoryAddUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerContactCategoryEditUpdate(EdsContactCategory category, EdsUser eventCauser, Date eventTime);

    // Mailing list

    EdsMyUpdate registerMailingListUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerMailingListAddUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerMailingListEditUpdate(EdsMailList mailList, EdsUser eventCauser, Date eventTime);

    //Attachments related

    EdsMyUpdate registerFileUpload(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerFileUploadForTask(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerFileUploadForIssue(EdsFileHeader fileHeader, EdsUser eventCauser, Date eventTime);

    //Timesheet related

    EdsMyUpdate registerTimesheetUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerTimesheetWaitingUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerTimesheetRejectedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerTimesheetApprovedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser eventCauser, Date eventTime);

    //Timesheet employee related

    EdsMyUpdate registerTimesheetEmployeeUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerTimesheetEmployeeApprovedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime);

    EdsMyUpdate registerTimesheetEmployeeRejectedUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser employee, EdsUser manager, Date eventTime);

    //Timesheet manager or buckupManager related

    EdsMyUpdate registerTimesheetManagerUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser manager, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerTimesheetManagerWaitingUpdate(EdsTimeSheetApprovalSession timesheetSession, EdsUser manager, EdsUser eventCauser, Date eventTime);

    //Note Related

    EdsMyUpdate registerNoteUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerNoteAddUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerNoteEditUpdate(EdsNoteHistory note, EdsUser eventCauser, Date eventTime);

    //Manual Transaction related
    EdsMyUpdate registerManualJournalUpdate(Integer manualJournalId, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerManualJournalAddUpdate(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfqAddUpdate(EdsRFQ rfq, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfqEditUpdate(EdsRFQ rfq, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfqDelete(EdsRFQ rfq, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfpAddUpdate(EdsRFP rfp, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfpEditUpdate(EdsRFP rfp, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRfpDelete(EdsRFP rfp, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockTransferAddUpdate(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockTransferEditUpdate(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockTransferDelete(EdsStockTransfer transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerManualJournalEditUpdate(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerManualJournalDelete(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerManualJournalVoid(EdsManualJournal manualJournal, EdsUser eventCauser, Date eventTime);
    //SaleInvoice related

    EdsMyUpdate registerSaleInvoiceUpdate(Integer invoiceId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, Integer clientContactId, BigDecimal amount);

    EdsMyUpdate registerSaleInvoiceAddUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceEditUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceDeleteUpdate(EdsSaleInvoice invoice, String invoiceName, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceSendToClient(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime, Integer clientContactId);

    EdsMyUpdate registerSaleInvoicePaymentReceive(EdsInvoicePayment paynemt, EdsUser receiver, Date eventTime);

    EdsMyUpdate registerSaleInvoiceConvertedFromSaleQuoteUpdate(Integer invoiceId, String quoteNumber, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceManagerRejectUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceManagerApproveUpdate(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleInvoiceSubmittedToManager(EdsSaleInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSalesInvoicePaymentVoid(EdsInvoicePayment payment, EdsUser user, Date time);

    EdsMyUpdate registerSalesInvoicePaymentDeleteUpdate(EdsInvoicePayment payment, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerSalesInvoiceAddCreditNote(EdsSaleInvoice saleInvoice, EdsUser user, Date time);

    // Additional Payment
    EdsMyUpdate registerAdditionalPayment(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerAdditionalPaymentAdd(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentEdit(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentDraft(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentDelete(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentReject(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentApprove(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerAdditionalPaymentSubmittedToManager(EdsAdditionalPayment additionalPayment, EdsUser eventCauser, Date evenTime);

    //Sale Quote related
    EdsMyUpdate registerSaleQuoteUpdate(Integer quoteObjectId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount);

    EdsMyUpdate registerSaleQuoteAddUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteEditUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteDeleteUpdate(EdsSaleQuote quote, String quoteNumber, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteClientApproveUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteRejectUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteManagerApproveUpdate(EdsSaleQuote quote, EdsUser manager, EdsUser receiver, Date eventTime);

    EdsMyUpdate registerSaleQuoteManagerRejectUpdate(EdsSaleQuote quote, EdsUser manager, EdsUser receiver, Date eventTime);

    EdsMyUpdate registerSaleQuoteConvertToSaleOrderUpdate(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteSendToClient(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleQuoteSubmittedToManager(EdsSaleQuote quote, EdsUser eventCauser, Date eventTime);

    //Expense Report related
    EdsMyUpdate registerExpenseReportUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount);

    EdsMyUpdate registerExpenseReportAddUpdate(EdsExpenseReport expenseReport, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerExpenseReportEditUpdate(EdsExpenseReport expenseReport, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerExpenseReportDeleteUpdate(EdsExpenseReport expenseReport, String expenceTitle, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerExpenseReportApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerExpenseReportDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerExpenseReportSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerStockTransferSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerStockTransferApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockTransferDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockTransferTransferredUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);


    EdsMyUpdate registerStockAdjustmentAddUpdate(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockAdjustmentEditUpdate(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockAdjustmentDelete(EdsStockAdjustment transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockAdjustmentSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerStockAdjustmentApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerStockAdjustmentDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);


    EdsMyUpdate registerPlacementAddUpdate(EdsPlacement transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPlacementEditUpdate(EdsPlacement transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPlacementDelete(EdsPlacement transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPlacementSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerPlacementApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPlacementDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);


    EdsMyUpdate registerRentalOrderAddUpdate(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRentalOrderEditUpdate(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRentalOrderDelete(EdsRentalOrder transfer, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRentalOrderSendToApprover(Integer expenseId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerRentalOrderApproveUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerRentalOrderDeclineUpdate(Integer expenseId, EdsUser eventCauser, Date eventTime);


    //    build assembly
    EdsMyUpdate registerBuildAssemblyAddUpdate(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerBuildAssemblyEditUpdate(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerBuildAssemblyDelete(EdsSavedAssemblyItem buildAssembly, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerBuildAssemblySendToApprover(Integer buildAssemblyId, EdsUser eventCauser, Integer approverId, Date eventTime);

    EdsMyUpdate registerBuildAssemblyApproveUpdate(Integer buildAssemblyId, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerBuildAssemblyDeclineUpdate(Integer buildAssemblyId, EdsUser eventCauser, Date eventTime);


    EdsMyUpdate registerExpensePaymentAdd(EdsExpensePayment expensePayment, EdsUser creator, Date eventDate);

    List<EdsMyUpdate> getAllUpdatesList(ListingFilterParameter fp, boolean isUpdatesList);

    List<EdsMyUpdate> getPeerUpdates();

    ArrayList<EdsMyUpdate> getUpdatesForAffectedID(Integer affectedId, String typeParent);

    List<EdsMyUpdate> getCreditNoteRefundUpdatesForAffectedID(Integer affectedId, String updateType);

    List<EdsMyUpdate> getUpdates(Integer affectedId, String typeParent, String updateType);

    EdsMyUpdate getUpdate(Integer affectedId, String updateType);

    String getDeletedUpdateName(Integer affectedId, String typeParent);

    //Calendar event guests
    EdsMyUpdate registerCalendarEventGuestsStatusChangeUpdate(EdsGoogleCalendarEventGuests calendarEventGuests, EdsUser owner, Date eventTime);

    //leave request related
    EdsMyUpdate registerLeaveRequestUpdate(Integer requestID, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerLeaveRequestDeleteUpdate(Integer requestID, String requestData, EdsUser eventCauser, Date eventTime);

    Integer getListCount(ListingFilterParameter fp);

    // for Purchase Order
    EdsMyUpdate registerPurchaseOrderUpdate(Integer orderId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount);

    EdsMyUpdate registerPurchaseOrderAddUpdate(EdsPurchaseOrder order, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseOrderEditUpdate(EdsPurchaseOrder order, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseOrderDeleteUpdate(EdsPurchaseOrder order, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseOrderSendToClient(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseOrderClientApproveUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseOrderReceivedUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseOrderPartialReceivedUpdate(EdsPurchaseOrder purchaseOrder, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseOrderSubmittedToManager(EdsPurchaseOrder purchaseOrder, EdsUser eventCauser, Date time);

    //for Purchase Invoice
    EdsMyUpdate registerPurchaseInvoiceUpdate(Integer purchaseInvoiceID, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount);

    EdsMyUpdate registerPurchaseInvoiceAddUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseInvoiceEditUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseInvoiceDeleteUpdate(Integer purchaseInvoiceID, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseInvoiceApproveUpdate(EdsPurchaseInvoice purchaseInvoice, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseInvoiceAddCreditNote(EdsPurchaseInvoice purchaseInvoice, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseInvoicePaymentVoid(EdsInvoicePayment payment, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseInvoicePaymentDeleteUpdate(EdsInvoicePayment entityID, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerPurchaseInvoicePay(EdsInvoicePayment payment, EdsUser user, Date time);

    EdsMyUpdate registerPurchaseInvoiceManagerRejectUpdate(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPurchaseInvoiceManagerApproveUpdate(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPurchaseInvoiceSubmittedToManager(EdsPurchaseInvoice invoice, EdsUser eventCauser, Date eventTime);

    //for Sale Order
    EdsMyUpdate registerSaleOrderUpdate(Integer quoteObjectId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, BigDecimal amount);

    EdsMyUpdate registerSaleOrderAddUpdate(EdsSaleQuote quote, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderConvertFromSQ(EdsSaleQuote quote, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderEditUpdate(EdsSaleQuote quote, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderDeleteUpdate(EdsSaleQuote quote, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderPickListUpdate(EdsSaleQuote pickList, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderClosed(EdsSaleQuote order, EdsUser creator, Date time);

    EdsMyUpdate registerSaleQuoteClosed(EdsSaleQuote order, EdsUser creator, Date time);

    EdsMyUpdate registerSaleOrderManagerRejectUpdate(EdsSaleQuote order, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleOrderManagerApproveUpdate(EdsSaleQuote order, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerSaleOrderSubmittedToManager(EdsSaleQuote order, EdsUser eventCauser, Date eventTime);

    EdsMyUpdate registerPurchaseOrderClosed(EdsPurchaseOrder order, EdsUser creator, Date time);

    EdsMyUpdate registerProductAddUpdate(EdsItem product, EdsUser creator, Date time);

    EdsMyUpdate registerProductEditUpdate(EdsItem product, EdsUser creator, Date time);

    EdsMyUpdate registerProductDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time);

    //for Fixed Asset

    EdsMyUpdate registerFixedAssetUpdate(Integer fixedAssetID, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerFixedAssetAddUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time);

    EdsMyUpdate registerFixedAssetOwnerAddUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time);

    EdsMyUpdate registerFixedAssetOwnerEditUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time);

    EdsMyUpdate registerFixedAssetEditUpdate(EdsFixedAsset fixedAsset, EdsUser creator, Date time);

    EdsMyUpdate registerFixedAssetDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerFixedAssetDisposeUpdate(Integer entityID, String customStringField, EdsUser creator, Date time);

    //for Checks
    EdsMyUpdate registerCheckUpdate(Integer checkID, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerCheckAddUpdate(EdsBankCheck bankCheck, EdsUser creator, Date time);

    EdsMyUpdate registerCheckEditUpdate(EdsBankCheck bankCheck, EdsUser creator, Date time);

    EdsMyUpdate registerCheckDeleteUpdate(Integer entityID, String itemName, EdsUser creator, Date time);

    //for Bank Account
    EdsMyUpdate registerBankAccountUpdate(Integer bankAccountID, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerBankAccountAddUpdate(EdsBankAccount bankAccount, EdsUser creator, Date time);

    EdsMyUpdate registerBankAccountEditUpdate(EdsBankAccount bankAccount, EdsUser creator, Date time);

    EdsMyUpdate registerBankAccountDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time);

    //for Chart Of Account
    EdsMyUpdate registerChartOfAccountUpdate(Integer accountID, EdsUser eventCauser, Date eventTime, String eventType, String updateType);

    EdsMyUpdate registerChartOfAccountAddUpdate(EdsAccount chartOfAccount, EdsUser creator, Date time);

    EdsMyUpdate registerChartOfAccountEditUpdate(EdsAccount chartOfAccount, EdsUser creator, Date time);

    EdsMyUpdate registerChartOfAccountDeleteUpdate(Integer entityID, String customStringField, EdsUser creator, Date time);

    EdsMyUpdate registerSICreditNoteRefund(EdsInvoicePayment payment, EdsUser user, Date time);

    EdsMyUpdate registerPICreditNoteRefund(EdsInvoicePayment payment, EdsUser user, Date time);

    //for Company Custom Fields
    EdsMyUpdate registerCustomFieldAddUpdate(EdsCompanyCustomFieldsSettings customFieldsSettings, EdsUser creator, Date time);

    EdsMyUpdate registerCustomFieldEditUpdate(EdsCompanyCustomFieldsSettings customFieldsSettings, EdsUser creator, Date time);

    EdsMyUpdate registerCustomFieldDeleteUpdate(Integer objectId, String customFieldName, EdsUser eventCauser, Date eventTime);

    //for Single Payrun
    EdsMyUpdate registerSinglePayrunUpdate(Integer singlePayrunId, EdsUser creator, Date time, String eventType, String updateType);

    EdsMyUpdate registerSinglePayrunAddUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    EdsMyUpdate registerSinglePayrunEditUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    EdsMyUpdate registerSinglePayrunDeleteUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    EdsMyUpdate registerSinglePayrunSubmitUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    EdsMyUpdate registerSinglePayrunApproveUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    EdsMyUpdate registerSinglePayrunRejectUpdate(EdsPayslipTableItem payslipTableItem, EdsUser creator, Date time);

    //for Group Payrun
    EdsMyUpdate registerGroupPayrunUpdate(Integer groupPayrunId, EdsUser creator, Date time, String eventType, String updateType);

    EdsMyUpdate registerGroupPayrunAddUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    EdsMyUpdate registerGroupPayrunEditUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    EdsMyUpdate registerGroupPayrunDeleteUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    EdsMyUpdate registerGroupPayrunSubmitUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    EdsMyUpdate registerGroupPayrunApproveUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    EdsMyUpdate registerGroupPayrunRejectUpdate(EdsPayslipTable payslipTable, EdsUser creator, Date time);

    //for Cash Advance
    EdsMyUpdate registerCashAdvanceUpdate(Integer cashAdvanceId, EdsUser creator, Date time, String eventType, String updateType);

    EdsMyUpdate registerCashAdvanceAddUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    EdsMyUpdate registerCashAdvanceEditUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    EdsMyUpdate registerCashAdvanceDeleteUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    EdsMyUpdate registerCashAdvanceSubmitUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    EdsMyUpdate registerCashAdvanceApproveUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    EdsMyUpdate registerCashAdvanceRejectUpdate(EdsCashAdvance cashAdvance, EdsUser creator, Date time);

    //for Pension Scheme
    EdsMyUpdate registerPensionSchemeUpdate(Integer pensionSchemeId, EdsUser creator, Date time, String eventType, String updateType);

    EdsMyUpdate registerPensionSchemeAddUpdate(EdsPensionScheme pensionScheme, EdsUser creator, Date time);

    EdsMyUpdate registerPensionSchemeEditUpdate(EdsPensionScheme pensionScheme, EdsUser creator, Date time);

    //for activity/event
    EdsMyUpdate registerEventUpdate(EdsEvent event, EdsUser creator, Date eventDate, String eventType, String updateType);

    EdsMyUpdate registerEventAdd(EdsEvent event, EdsUser creator, Date eventDate);

    EdsMyUpdate registerEventEdit(EdsEvent event, EdsUser creator, Date eventDate);

    EdsMyUpdate registerEventDelete(EdsEvent event, EdsUser creator, Date eventDate);

    //for End of Service Settings
    EdsMyUpdate registerEndOfServiceSettingsUpdate(Integer endOfServiceSettingsId, EdsUser creator, Date time, String eventType, String updateType);

    EdsMyUpdate registerEndOfServiceSettingsEditUpdate(EndOfServiceSettings endOfServiceSettings, EdsUser creator, Date time);

    EdsMyUpdate registerListPanelSettingsEditUpdate(EdsListPanelSettings listPanelSettings, EdsUser user, Date time);

    EdsMyUpdate registerBankTransferAdd(Integer bankTransferId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBankTransferAppliedPayable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate);

    EdsMyUpdate registerManualEntryAppliedReceivablePayable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBankTransferAppliedReceivable(EdsInvoicePayment invoicePayment, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBankTransferEdit(Integer bankTransferId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBankTransferDelete(Integer bankTransferId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerInvoicePaymentAdd(Integer invoicePaymentid, EdsUser creator, Date eventDate);

    EdsMyUpdate registerInvoicePaymentEdit(Integer invoicePaymentid, EdsUser creator, Date eventDate);

    EdsMyUpdate registerInvoicePaymentDelete(Integer invoicePaymentid, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBatchPaymentAdd(Integer batchPaymentId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBatchPaymentEdit(Integer batchPaymentId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBatchPaymentDelete(Integer batchPaymentId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerBatchPaymentVoid(Integer batchPaymentId, EdsUser creator, Date eventDate);

    EdsMyUpdate registerRecurringInvoiceAdd(EdsRecurringInvoice invoice, EdsUser eventCauser, Date eventDate);

    EdsMyUpdate registerRecurringInvoiceEdit(EdsRecurringInvoice invoice, EdsUser eventCauser, Date eventDate);

    EdsMyUpdate registerRecurringInvoiceDelete(EdsRecurringInvoice invoice, String itemName, EdsUser creator, Date eventDate);

    EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType);

    EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType, Integer clientContactId, Integer relationId);

    EdsMyUpdate registerAttachmentCRUD(EdsFileHeader edsFileHeader, EdsUser eventCauser, EdsBusinessEvent event, String eventType);

    List<MyUpdateItem> getAttachmentUpdates(Integer relationID, String relationType);

    EdsMyUpdate registerMyUpdate(Integer affectedId, EdsUser eventCauser, Date eventTime, String eventType, String updateType, String entityType, Integer clientContactId, Integer relationId, String formId, BigDecimal amount);

    EdsMyUpdate registerCustomFormItemAdd(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    EdsMyUpdate registerCustomFormItemEdit(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    EdsMyUpdate registerCustomFormItemDelete(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    EdsMyUpdate registerCustomFormSubmittedToManager(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    EdsMyUpdate registerCustomFormManagerApproveUpdate(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    EdsMyUpdate registerCustomFormManagerRejectUpdate(Integer customFormItemId, EdsUser creator, Date eventDate, String formId);

    List<EdsMyUpdate> getUpdatesForCustomForm(Integer affectedId, String typeParent, String formId);

    EdsMyUpdate registerRFPSubmittedToManager(EdsRFP edsRFP, EdsUser user, Date time);

    EdsMyUpdate registerRfpManagerApproveUpdate(EdsRFP edsRFP, EdsUser manager, Date time);

    EdsMyUpdate registerRfpManagerRejectUpdate(EdsRFP edsRFP, EdsUser manager, Date time);

    EdsMyUpdate registerShift(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerShiftAdd(EdsShift shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerShiftEdit(EdsShift shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerShiftDraft(EdsShift shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerShiftDelete(EdsShift shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerShiftReject(EdsShift shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerShiftApprove(EdsShift shift, EdsUser eventCauser, Date evenTime);


    EdsMyUpdate registerRotation(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerRotationAdd(EdsRotation shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerRotationEdit(EdsRotation shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerRotationDraft(EdsRotation shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerRotationDelete(EdsRotation shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerRotationReject(EdsRotation shift, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerRotationApprove(EdsRotation shift, EdsUser eventCauser, Date evenTime);


    EdsMyUpdate registerGroupPlacement(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerGroupPlacementAdd(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerGroupPlacementEdit(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerGroupPlacementDraft(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerGroupPlacementDelete(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerGroupPlacementReject(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerGroupPlacementApprove(EdsGroupPlacement groupPlacement, EdsUser eventCauser, Date evenTime);


    EdsMyUpdate registerProductCategory(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);
    EdsMyUpdate registerCourseSchedule(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);
    EdsMyUpdate registerCourseScheduleAdd(EdsCourseSchedule productCategory, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerProductCategoryAdd(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerProductCategoryEdit(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerProductCategoryDelete(EdsProductCategory productCategory, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertime(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerOvertimeAdd(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertimeEdit(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertimeDraft(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertimeDelete(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertimeReject(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerOvertimeApprove(EdsOvertimeObject overtimeObject, EdsUser eventCauser, Date evenTime);

    // Hrms Backups Employee
    EdsMyUpdate registerBackupsEmployee(Integer id, EdsUser user, Date evenTime, String eventType, String updateType);

    EdsMyUpdate registerBackupsEmployeeAdd(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerBackupsEmployeeEdit(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerBackupsEmployeeDelete(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerBackupsEmployeeReject(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerBackupsEmployeeApprove(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerBackupsEmployeeSubmittedToManager(EdsBackupsEmployee backupsEmployee, EdsUser eventCauser, Date evenTime);

    EdsMyUpdate registerCompanySettingsEdit(EdsCompanySettings companySettings, EdsUser eventCreator, Date eventTime);
}
