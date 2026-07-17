package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EntityToEmailTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.webforms.client.forms.WebForm;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 13:30:26
 */
public interface CRMServiceAsync {

    void getAccountsForMerge(Integer[] accountIDs, AsyncCallback<ArrayList<CrmAccountItem>> asyncCallback);

    void mergeAccounts(CrmAccountItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs, AsyncCallback<Boolean> asyncCallback);

    Request getOpportunityList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<OpportunityListItem>> async);

    Request getOpportunityListByCategoryID(Integer categoryId, AsyncCallback<ListResult<OpportunityListItem>> async);

    Request getOpportunityListByProductID(Integer productId, AsyncCallback<ListResult<OpportunityListItem>> async);

    void getDefaultOne(AsyncCallback<OpportunityListItem> async);

    Request getEventList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<EventItem>> async);

    void getLead(Integer leadID, AsyncCallback<ContactListItem> async);

    Request getCrmAccounts(ListingFilterParameter filterParametrs, AsyncCallback<CrmAccountList> async);

    Request getCampaigns(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CampaignItem>> async);

    void editCampaign(Integer objectId, AsyncCallback<CampaignItem> async);

    void saveCampaign(CampaignItem item, AsyncCallback<Integer> async);

    void getCampign(Integer objectId, AsyncCallback<CampaignItem> async);

    void editAccount(Integer objectId, String type, AsyncCallback<CrmAccountItem> async);

    void saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress, AsyncCallback<Integer> async);

    void saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress, boolean runWebhook, AsyncCallback<Integer> async);

    void updateCreditLimit(Integer objectId, BigDecimal creditLimit, AsyncCallback<Void> async);

    void getAccount(Integer objectId, String entityType, AsyncCallback<CrmAccountItem> async);

    void saveLead(ContactListItem item, ArrayList<Integer> selectedMailList, AsyncCallback<Integer> async);

    void changeLeadKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                               Integer prevItemId, Integer afterItemId, AsyncCallback<Integer> async);

    void editLead(Integer objectId, Integer webFormID, AsyncCallback<ContactListItem> async);

    void saveOpportunity(OpportunityListItem item, AsyncCallback<Integer> async);

    void editOpportunity(Integer objectId, AsyncCallback<OpportunityListItem> async);

    void editOpportunity(Integer objectId, String formType, Integer convertedFormId, Integer contactID, AsyncCallback<OpportunityListItem> async);

    void getOpportunity(Integer objectId, AsyncCallback<OpportunityListItem> async);

    void getSubOpportunities(Integer objectId, AsyncCallback<ArrayList<OpportunityListItem>> async);

    void getOpportunityItems(Integer objectId, AsyncCallback<OpportunityItem[]> async);

    void getLeadItems(Integer objectId, AsyncCallback<OpportunityItem[]> async);

    void getOpportunityStages(boolean sortByName, AsyncCallback<SelectItem[]> async);

    Request getCases(ListingFilterParameter fp, AsyncCallback<CaseList> async);

    void getCaseEmail(String emailID, Integer trackerID, AsyncCallback<Email> async);

    void getCaseEmails(Integer objectId, AsyncCallback<ArrayList<Email>> async);

    void getCase(Integer id, boolean fromSummary, AsyncCallback<CaseItem> async);

    void editCase(Integer objectId, String formType, Integer convertFormId, boolean fromUI, AsyncCallback<CaseItem> async);

    void editCase(Integer objectId, String formType, Integer convertFormId, AsyncCallback<CaseItem> async);

    void getCrmEntityAsSelectItem(String crmEntity, Integer objectId, AsyncCallback<SelectItem> async);

    void saveCase(CaseItem item, boolean isFromWebForm, AsyncCallback<SelectItem> async);

    void saveFeedBack(BugReportItem feedBack, AsyncCallback<Void> async);

    Request getSolutionList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SolutionItem>> async);

    void getSolution(Integer objectId, AsyncCallback<SolutionItem> async);

    void saveSolution(SolutionItem item, AsyncCallback<Void> async);

    void saveCaseAndSolution(CaseItem caseItem, SolutionItem solutionItem, AsyncCallback<Void> async);

    void deleteSolution(Integer objectId, AsyncCallback async);

    Request getNewActivityList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ActivityItem>> async);

    Request getTaskList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ActivityItem>> async);

    void getLookUpItems(ListingFilterParameter filterParametrs, Integer type, AsyncCallback<ListResult<SelectItem>> async);

    void getEvent(Integer objectId, AsyncCallback<EventItem> async);

    void getCaseStatusLisItems(AsyncCallback<SelectItem[]> async);

    void getCaseReasonItems(AsyncCallback<SelectItem[]> async);

    void saveCrmNote(ListingFilterParameter fp, String comment, AsyncCallback<Void> async);

    void saveCrmNote(String entityType, Integer entityID, HistoryListItem note, AsyncCallback<Integer> async);

    void getContactsByAccount(Integer accountID, Integer contactID, AsyncCallback<SelectItem[]> async);

    void getCrmAttachments(Integer objectID, String from, AsyncCallback<FileResource[]> callback);

    void hasAttachment(Integer objectId, String from, AsyncCallback<Boolean> async);

    //Message

    void saveLeadAssignee(ArrayList<Integer> leadIDs, Integer assigneeId, AsyncCallback<Void> async);

    void changeAccountsOwners(ArrayList<Integer> accountIDs, ArrayList<Integer> ownerIDs, UpdateModeEnum updateMode, ListingFilterParameter filterParameter, AsyncCallback<Void> async);

    void getOwnersListByPermission(String leadOrCandidatePermissionCode, AsyncCallback<SelectItem[]> async);

    void getAccountOwnersList(AsyncCallback<ArrayList<SelectItem>> async);

    void deleteCrmAccount(ArrayList<Integer> objectIDs, boolean removeContactsAlso, AsyncCallback<ArrayList<Integer>> async);

    void deleteCase(Integer objectId, AsyncCallback async);

    void deleteOpportunity(ArrayList<Integer> objectIDs, AsyncCallback<ArrayList<Integer>> async);

    void deleteCampaign(Integer objectId, AsyncCallback async);

    void deleteEvent(ArrayList<Integer> objectIDs, AsyncCallback<ArrayList<Integer>> async);

    void deleteAttachment(Integer objectID, AsyncCallback async);

    void generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID, AsyncCallback<EmailTemplateItem> callback);

    void saveCallLog(Appointment appointment, AsyncCallback<Integer> callback);

    void getEmailTemplates(String templateCategory, AsyncCallback<SelectItem[]> callback);

    void getSMSItem(String moduleType, AsyncCallback<SmsSendItem> callback);

    void getCrmHistories(Integer crmCaseID, String relationType, AsyncCallback<CrmHistoryList[]> callback);

    void convertTrashToCase(Integer trashID, AsyncCallback<Boolean> asyncCallback);

    void updateCompanyWebForms(Integer companyId, AsyncCallback callback);

    void fillDropDowns(String form, AsyncCallback<HashMap<String, SelectItem[]>> callback);

    void saveWebForm(WebForm webForm, AsyncCallback<Integer> asyncCallback);

    void getWebForm(Integer objectID, AsyncCallback<WebForm> asyncCallback);

    Request getWebForms(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WebForm>> asyncCallback);

    void deleteWebForm(Integer objectId, AsyncCallback<Void> asyncCallback);

    void editWebForm(Integer objectId, String type, AsyncCallback<WebForm> asyncCallback);

    void getInvoicesOrQuotes(ListingFilterParameter fp, AsyncCallback<TypeItem[]> async);

    void getNewLeads(ListingFilterParameter filterParametr, AsyncCallback<ListResult<ContactListItem>> callback);

    void updateCaseStatus(Integer caseID, Integer statusID, String note, AsyncCallback<Void> async);

    void saveOpportunity(OpportunityListItem item, Integer convertingTo, AsyncCallback<SelectItem> async) throws NumberExistingException;

    void deleteCase(ArrayList<Integer> idsList, AsyncCallback<Void> async);

    void getCurrencies(AsyncCallback<SelectItem[]> async);

    void generateAccountNumber(String accountType, AsyncCallback<String> async);

    void generateOpportunityNumber(AsyncCallback<NumberData> async);

    void convertAccounts(ArrayList<Integer> ids, Integer typeID, AsyncCallback<Boolean> asyncCallback);

    void changeLeadStatus(ArrayList<Integer> ids, Integer statusId, AsyncCallback<Boolean> asyncCallback);

    void updateCases(Integer statusIdID, ArrayList<Integer> ids, String type, AsyncCallback<Boolean> asyncCallback);

    void updateOpportunities(Integer statusIdID, ArrayList<Integer> ids, String type, AsyncCallback<Boolean> asyncCallback);

    void resendClientActivationLink(Integer clientContactID, AsyncCallback<Boolean> callback);

    void activateOrDeActivateClientContact(Integer clientContactID, boolean activate, AsyncCallback<Void> callback);

    void smsSendTo(SmsSendItem smsSendItem, AsyncCallback<Boolean> asyncCallback);

    void getSmsNotes(ListingFilterParameter fp, AsyncCallback<ArrayList<SmsSendItem>> asyncCallback);

    void deleteSmsSendItem(Integer objectID, AsyncCallback asyncCallback);

    void generateSMSTemplate(Integer i, ContactListItem lead, EmployeeListItem employee, AsyncCallback<String> asyncCallback);

    void generateSMSTemplateForSalesInvoice(Integer i, Integer saleInvoiceId, AsyncCallback<String> async);

    void generateEmployeeEventTemplate(Integer templateId, EmployeeListItem employee, AsyncCallback<LinkedHashMap<String, String>> asyncCallback);

    void generateCandidateEventTemplate(Integer templateId, ContactListItem lead, AsyncCallback<Appointment> asyncCallback);

    void generateCrmAccountSMSTemplate(Integer templateId, CrmAccountItem crmAccount, AsyncCallback<String> asyncCallback);

    void getCrmTaskListForActivityTab(ListingFilterParameter fp, AsyncCallback<ListResult<ActivityItem>> asyncCallback);

    void convertLead(OpportunityListItem opportunity, Integer leadID, AsyncCallback<Integer> callback);

    void saveOppotunityEditCellValue(OpportunityListItem rowValue, String columnCodeName, AsyncCallback<Boolean> callback);

    void saveAccountsEditCellView(CrmAccountItem rowValue, String columnCodeName, AsyncCallback<Boolean> async);

    void opportunityConvertToProject(Integer opportunityId, Integer accountId, FileItem contract, AsyncCallback<Integer> async);

    void getCrmAccountBalance(Integer accountId, AsyncCallback<Double> async);

    void getCaseChangeHistory(Integer caseId, AsyncCallback<CaseList> callback);

    void getTrackerAttachments(Integer trackerID, AsyncCallback<FileResource[]> abstractAsyncCallback);

    void saveLeadCampaign(ArrayList<Integer> itemIDs, Integer id, String type, AsyncCallback<Void> callback);

    void saveCaseEditCellValue(CaseItem rowValue, String columnCode, AsyncCallback<Void> abstractAsyncCallback);

    void deleteCampaigns(ArrayList<Integer> ids, AsyncCallback<Void> callback);

    void switchvoxCall(String phone, AsyncCallback<Void> callback);

    void getOpportunityExpenseClaimList(Integer opportunityId, ListingFilterParameter filterParameter, AsyncCallback<ListResult<OpportunityExpenseClaimListItem>> asyncCallback);

    void getStatesByCountryName(AsyncCallback<HashMap<Integer, SelectItem[]>> asyncCallback);

    void getCountriesKey(AsyncCallback<HashMap<String, String[]>> asyncCallback);

    void addAccountOrContactToOpportunity(Integer opportunityID, boolean customer, AsyncCallback<OpportunityListItem> callback);

    void addAccountToContact(ContactListItem item, boolean customer, AsyncCallback<ContactListItem> callback);

    void getCrmAccountNameByID(Integer crmAccountID, AsyncCallback<String> callback);

    void getContactsForMerge(Integer[] contactIDs, AsyncCallback<ArrayList<ContactListItem>> callback);

    void mergeContacts(ContactListItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs, AsyncCallback<Boolean> asyncCallback);

    void validateContactInvoices(ContactListItem mainItem, ArrayList<Integer> objectIDs, AsyncCallback<Boolean> asyncCallback);

    void getTrackerAttachments(HashSet<Integer> trackerIDSet, AsyncCallback<FileResource[]> async);

    void makePrimaryContact(Integer accountID, Integer contactID, AsyncCallback<ContactListItem> abstractAsyncCallback);

    void getPrimaryContactAddresses(Integer contactID, AsyncCallback<ContactListItem> asyncCallback);

    void saveEventEditCellValue(EventItem rowValue, String columnCodeName, AsyncCallback<Void> callback);

    void saveLeadMultiAssignee(boolean allTableItems, ArrayList<Integer> itemIDs, ListingFilterParameter fp, ArrayList<SelectItem> selectItems, AsyncCallback<Void> callback);

    void getOpportunityQuickData(AsyncCallback<OpportunityListItem> callback);

    void getCaseQuickData(AsyncCallback<CaseItem> callback);

    void getCaseTypes(AsyncCallback<CaseItem> callback);

    void getContactQuickData(Integer crmAccountID, int contactType, AsyncCallback<ContactListItem> callback);

    void getCandidateQuickData(AsyncCallback<ContactListItem> callback);

    void getNewKanbanLeads(ListingFilterParameter filterParametr, SelectItem columnMetadata, AsyncCallback<ListResult<ContactListItem>> callback);

    void getNewKanbanOpportunities(ListingFilterParameter filterParameter, SelectItem columnMetadata, AsyncCallback<OpportunitiesList<OpportunityListItem>> callback);

    void changeOpportunityKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                      Integer prevItemId, Integer afterItemId, AsyncCallback<Integer> async);

    void changeOpportunityKanbanOrder(String statusCode, Integer itemId, Integer widgetIndex, AsyncCallback<Integer> async);

    void takeReference(String statusCode, AsyncCallback<SelectItem> async);

    void getNewKanbanCases(ListingFilterParameter filterParametr, SelectItem columnMetadata, AsyncCallback<ListResult<CaseItem>> callback);

    void changeCaseKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                               Integer Id, Integer afterItemId, AsyncCallback<Integer> async);

    void getLastActivities(Integer objectId, String type, AsyncCallback<ListResult<Appointment>> callback);

    void getCrmAccountOwners(Integer crmAccountID, AsyncCallback<ArrayList<SelectItem>> callback);

    void getOrCreateCrmAccount(String name, AsyncCallback<Integer> callback);

    void getCrmSubItemsLookUpItems(ListingFilterParameter filterParameter, CustomFieldLookUpTypeEnum typeEnum, AsyncCallback<SelectItem[]> callback);

    void saveClientCellValue(CrmAccountItem rowValue, String columnCodeName, AsyncCallback<Void> async);

    void getAccountCurrency(Integer accountId, AsyncCallback<SelectItem> asyncCallback);

    void updateAddress(ListingFilterParameter filterParametrs, Address address, Integer type, AsyncCallback<Boolean> asyncCallback);

    void getOpportunityLogHistoryList(ListingFilterParameter listingFilterParameter, AsyncCallback<ListResult<LogHistoryItem>> async);

    void updateOpportunityNoteAndRejectReason(Integer opportunityId, String note, Integer rejectReasonId, AsyncCallback<Void> callback);

    void updateOpportunityStatus(OpportunityListItem data, AsyncCallback<Void> async);

    void getEmployeeByCode(String employeeCode,AsyncCallback<SelectItem> asyncCallback);

    void getEmployeeByPassportNumber(String passportNumber,AsyncCallback<SelectItem> asyncCallback);

    void getEmployeeByFirstAndLastName(String firstName, String lastName,AsyncCallback<SelectItem> asyncCallback);

}
