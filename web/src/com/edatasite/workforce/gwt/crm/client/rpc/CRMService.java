package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 13:29:49
 */
public interface CRMService extends RemoteService {

    ArrayList<CrmAccountItem> getAccountsForMerge(Integer[] accountIDs);

    Boolean mergeAccounts(CrmAccountItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIDs);

    ListResult<OpportunityListItem> getOpportunityList(ListingFilterParameter filterParametrs);

    ListResult<OpportunityListItem> getOpportunityListByCategoryID(Integer categoryId);

    ListResult<OpportunityListItem> getOpportunityListByProductID(Integer productId);

    OpportunityListItem getDefaultOne();

    ListResult<EventItem> getEventList(ListingFilterParameter filterParametrs);

    ContactListItem getLead(Integer leadID);

    CrmAccountList getCrmAccounts(ListingFilterParameter filterParametrs);

    ListResult<CampaignItem> getCampaigns(ListingFilterParameter filterParametrs);

    CampaignItem editCampaign(Integer objectId);

    Integer saveCampaign(CampaignItem item);

    CampaignItem getCampign(Integer objectId);

    CrmAccountItem editAccount(Integer objectId, String type);

    Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress);

    Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress, boolean runWebhook);

    void updateCreditLimit(Integer objectId, BigDecimal creditLimit);

    CrmAccountItem getAccount(Integer objectId, String entityType);

    Integer saveLead(ContactListItem item, ArrayList<Integer> selectedMailList);

    Integer changeLeadKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                  Integer prevItemId, Integer afterItemId);

//    ArrayList<SelectItem> getKanbanDefaultColumns(ReferenceParentEnum parentCode);

    ContactListItem editLead(Integer objectId, Integer webFormID);

    Integer saveOpportunity(OpportunityListItem item);

    SelectItem saveOpportunity(OpportunityListItem item, Integer convertingTo) throws NumberExistingException;

    OpportunityListItem editOpportunity(Integer objectId);

    OpportunityListItem editOpportunity(Integer objectId, String formType, Integer convertedFormId, Integer contactID);

    OpportunityListItem getOpportunity(Integer objectId);

    ArrayList<OpportunityListItem> getSubOpportunities(Integer objectId);

    OpportunityItem[] getOpportunityItems(Integer objectId);

    OpportunityItem[] getLeadItems(Integer objectId);

    SelectItem[] getOpportunityStages(boolean sortByName);

    CaseList getCases(ListingFilterParameter fp);

    Email getCaseEmail(String emailID, Integer trackerID);

    ArrayList<Email> getCaseEmails(Integer trackerID);

    CaseItem getCase(Integer id, boolean fromSummary);

    CaseItem editCase(Integer objectId, String formType, Integer convertFormId);

    CaseItem editCase(Integer objectId, String formType, Integer convertFormId, boolean fromUI);

    SelectItem getCrmEntityAsSelectItem(String crmEntity, Integer objectId);

    SelectItem saveCase(CaseItem item, boolean isFromWebForm);

    void saveFeedBack(BugReportItem feedBack);

    ListResult<SolutionItem> getSolutionList(ListingFilterParameter filterParametrs);

    SolutionItem getSolution(Integer objectId);

    void saveSolution(SolutionItem item);

    void saveCaseAndSolution(CaseItem caseItem, SolutionItem solutionItem);

    void deleteSolution(Integer objectId);

    ListResult<ActivityItem> getNewActivityList(ListingFilterParameter filterParametrs);

    ListResult<ActivityItem> getTaskList(ListingFilterParameter filterParametrs);

    FileResource[] getCrmAttachments(Integer objectID, String from);

    Boolean hasAttachment(Integer objectId, String from);

    ListResult<SelectItem> getLookUpItems(ListingFilterParameter filterParametrs, Integer type);

    EventItem getEvent(Integer objectId);

    SelectItem[] getCaseStatusLisItems();

    SelectItem[] getCaseReasonItems();

    void saveCrmNote(ListingFilterParameter fp, String comment);

    Integer saveCrmNote(String entityType, Integer entityID, HistoryListItem note);

    SelectItem[] getContactsByAccount(Integer accountID, Integer contactID);

    void saveLeadAssignee(ArrayList<Integer> leadIDs, Integer assigneeId);

    void changeAccountsOwners(ArrayList<Integer> accountIDs, ArrayList<Integer> ownerIDs, UpdateModeEnum updateMode, ListingFilterParameter filterParameter);

    SelectItem[] getOwnersListByPermission(String permissionCode);

    ArrayList<SelectItem> getAccountOwnersList();

    ArrayList<Integer> deleteCrmAccount(ArrayList<Integer> objectIDs, boolean removeContactsAlso);

    void deleteCase(Integer objectId);

    void deleteCase(ArrayList<Integer> idsList);

    void deleteWebForm(Integer objectId);

    ArrayList<Integer> deleteOpportunity(ArrayList<Integer> objectIDs);

    void deleteCampaign(Integer objectId);

    ArrayList<Integer> deleteEvent(ArrayList<Integer> objectIDs);

    void deleteAttachment(Integer objectID);

    EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID);

    Integer saveCallLog(Appointment appointment);

    SelectItem[] getEmailTemplates(String templateCategory);

    SmsSendItem getSMSItem(String moduleType);

    CrmHistoryList[] getCrmHistories(Integer crmCaseID, String relationType);

    void updateCaseStatus(Integer caseID, Integer statusID, String note);

    Boolean convertTrashToCase(Integer trashID);

    void updateCompanyWebForms(Integer companyId);

    Integer saveWebForm(WebForm webForm);

    WebForm getWebForm(Integer objectID);

    WebForm editWebForm(Integer objectID, String type);

    HashMap<String, SelectItem[]> fillDropDowns(String form);

    ListResult<WebForm> getWebForms(ListingFilterParameter filterParametr);

    TypeItem[] getInvoicesOrQuotes(ListingFilterParameter fp);

    ListResult<ContactListItem> getNewLeads(ListingFilterParameter filterParametr);

    SelectItem[] getCurrencies();

    //SelectItem[] getPaymentMethod();

    String generateAccountNumber(String accountType);

    NumberData generateOpportunityNumber();

    Boolean convertAccounts(ArrayList<Integer> ids, Integer typeID);

    Boolean changeLeadStatus(ArrayList<Integer> ids, Integer statusId);

    Boolean updateCases(Integer statusIdID, ArrayList<Integer> ids, String type);

    Boolean updateOpportunities(Integer statusIdID, ArrayList<Integer> ids, String type);

    boolean resendClientActivationLink(Integer clientContactID);

    void activateOrDeActivateClientContact(Integer clientContactID, boolean activate);

    Boolean smsSendTo(SmsSendItem smsSendItem);

    ArrayList<SmsSendItem> getSmsNotes(ListingFilterParameter fp);

    void deleteSmsSendItem(Integer objectID);

    String generateSMSTemplate(Integer i, ContactListItem lead, EmployeeListItem employee);

    String generateSMSTemplateForSalesInvoice(Integer i, Integer saleInvoiceId);

    LinkedHashMap<String, String> generateEmployeeEventTemplate(Integer templateId, EmployeeListItem employee);

    Appointment generateCandidateEventTemplate(Integer templateId, ContactListItem lead);

    String generateCrmAccountSMSTemplate(Integer templateId, CrmAccountItem crmAccount);

    ListResult<ActivityItem> getCrmTaskListForActivityTab(ListingFilterParameter fp);

    Integer convertLead(OpportunityListItem opportunity, Integer leadID);

    boolean saveOppotunityEditCellValue(OpportunityListItem rowValue, String columnCodeName);

    boolean saveAccountsEditCellView(CrmAccountItem rowValue, String columnCodeName);

    Integer opportunityConvertToProject(Integer opportunityId, Integer accountId, FileItem contract);

    Double getCrmAccountBalance(Integer accountId);

    CaseList getCaseChangeHistory(Integer caseId);

    FileResource[] getTrackerAttachments(Integer trackerID);

    void saveLeadCampaign(ArrayList<Integer> itemIDs, Integer id, String type);

    void saveCaseEditCellValue(CaseItem rowValue, String columnCode);

    void deleteCampaigns(ArrayList<Integer> ids);

    void switchvoxCall(String phone);

    ListResult<OpportunityExpenseClaimListItem> getOpportunityExpenseClaimList(Integer opportunityId, ListingFilterParameter filterParameter);

    HashMap<Integer, SelectItem[]> getStatesByCountryName();

    HashMap<String, String[]> getCountriesKey();

    OpportunityListItem addAccountOrContactToOpportunity(Integer opportunityID, boolean customer);

    ContactListItem addAccountToContact(ContactListItem item, boolean customer);

    String getCrmAccountNameByID(Integer crmAccountID);

    ArrayList<ContactListItem> getContactsForMerge(Integer[] contactIds);

    Boolean mergeContacts(ContactListItem mainItem, boolean deleteOthers, ArrayList<Integer> otherObjectIds);

    Boolean validateContactInvoices(ContactListItem mainItem, ArrayList<Integer> otherObjectIds);

    FileResource[] getTrackerAttachments(HashSet<Integer> trackerIDSet);

    ContactListItem makePrimaryContact(Integer accountID, Integer contactID);

    ContactListItem getPrimaryContactAddresses(Integer contactID);

    void saveEventEditCellValue(EventItem rowValue, String columnCodeName);

    void saveLeadMultiAssignee(boolean hasCheckedAllTableItems, ArrayList<Integer> itemIDs, ListingFilterParameter fp, ArrayList<SelectItem> selectItems);

    OpportunityListItem getOpportunityQuickData();

    ContactListItem getContactQuickData(Integer crmAccountId, int contactType);

    ContactListItem getCandidateQuickData();

    CaseItem getCaseQuickData();

    CaseItem getCaseTypes();

    ListResult<ContactListItem> getNewKanbanLeads(ListingFilterParameter filterParametr, SelectItem columnMetadata);

    OpportunitiesList<OpportunityListItem> getNewKanbanOpportunities(ListingFilterParameter filterParameter, SelectItem columnMetadata);

    Integer changeOpportunityKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                         Integer prevItemId, Integer afterItemId);

    ListResult<CaseItem> getNewKanbanCases(ListingFilterParameter filterParametr, SelectItem columnMetadata);

    Integer changeCaseKanbanOrder(SelectItem columnLayoutData, Integer itemId, Integer widgetIndex,
                                  Integer prevItemId, Integer afterItemId);

    ListResult<Appointment> getLastActivities(Integer objectId, String type);

    ArrayList<SelectItem> getCrmAccountOwners(Integer crmAccountID);

    Integer getOrCreateCrmAccount(String name);

    SelectItem[] getCrmSubItemsLookUpItems(ListingFilterParameter filterParameter, CustomFieldLookUpTypeEnum typeEnum);

    void saveClientCellValue(CrmAccountItem rowValue, String columnCodeName);

    SelectItem getAccountCurrency(Integer accountId);

    boolean updateAddress(ListingFilterParameter filterParametrs, Address address, Integer type);

    ListResult<LogHistoryItem> getOpportunityLogHistoryList(ListingFilterParameter listingFilterParameter);

    Integer changeOpportunityKanbanOrder(String statusCode, Integer itemId, Integer widgetIndex);

    SelectItem takeReference(String statusCode);

    void updateOpportunityNoteAndRejectReason(Integer opportunityId, String note, Integer rejectReasonId);

    void updateOpportunityStatus(OpportunityListItem data);

    SelectItem getEmployeeByCode(String employeeCode);

    SelectItem getEmployeeByPassportNumber(String passportNumber);

    SelectItem getEmployeeByFirstAndLastName(String firstName, String lastName);


    class App {
        public static CRMServiceAsync get() {
            ServiceDefTarget target = GWT.create(CRMService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/crm");
            return (CRMServiceAsync) target;
        }
    }
}
