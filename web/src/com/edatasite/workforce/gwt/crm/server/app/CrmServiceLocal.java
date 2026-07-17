package com.edatasite.workforce.gwt.crm.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.CrmAccountInvoiceTO;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailFilter;
import com.edatasite.workforce.core.solr.document.CrmAccountSolrDoc;
import com.edatasite.workforce.gwt.client.client.rpc.ClientCurrency;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.crm.client.rpc.*;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CrmActivityDTO;
import ezvcard.VCard;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 19, 2010
 * Time: 3:57:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CrmServiceLocal {

    ArrayList<RejectedImportRecord[]> importAccounts(ImportFile importFile, List dataBank, String from);

    void importVCardAccounts(ImportFile importFile, List<VCard> items, String type, String from);

    ArrayList<RejectedImportRecord[]> importContacts(ImportFile importFile, List listOfRows, int contactType, Integer mailListId) throws Exception;

    Set<Integer> importVCardContacts(ImportFile importFile, List<VCard> listOfRows, Set<Integer> savedLeadsHashCodes, int contactType) throws Exception;

    int createEntityMailList(EdsMailList mailList, List<Integer> iDs);

    SolrQuery getSolrQueryForContact(ListingFilterParameter fp, ListLoadConfig config, EdsUser user);

    SelectItem[] getEmailTemplates(String templateCategory);

    void sendAutoResponseToCase(Integer crmCaseID, Integer emailSettingID, Integer filterID);

    boolean createContactHistory(String message, EdsCrmContact contact);

    boolean createContactHistory(String message, Integer contactId, Integer userId);

    List<SolrDocument> getDocumentsExistingInBase(String core, SolrDocumentList results, String fieldObjectID, String type);

    List<Integer> getCRMEntityIDs(String entityType, ListingFilterParameter filterParametrs);

    void updateCrmAccountAndAddToSolr(EdsCrmAccount account, boolean newCreated, EdsUser user);

    Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm);

    Integer disableAccess(Integer contactID);

    NumberData generateOpportunityNumber();

    OpportunityListItem editOpportunity(Integer objectId);

    boolean updateAddresses(Address[] data, EdsObject obj, Integer entityType, boolean isMerging);

    boolean updateAddresses(Address[] data, EdsObject obj, Integer entityType, boolean isMerging, boolean isHrms);

    List<Integer> getOpportunityIdsByIDs(String ids);

    List<Integer> getOpportunityIdsWithLimit(int startat, int limit);

    ListResult<EventItem> getEventList(ListingFilterParameter filterParametrs);

    List<Integer> getEventIdsByIDs(String ids);

    List<Integer> getEventIdsWithLimit(int startat, int limit);

    EdsEvent getEventByID(Integer eventID);

    void indexCompanyCrmCase(SolrReindexRpc solrReindex);

    Integer convertEmailToCase(EdsEmail email, List<EdsEmailFilter> filters);

    Map<Boolean, List<Integer>> convertEmailToCase(List<EdsEmail> messages);

    void onCasesHaveBeenFetchedAndCreated(List<Integer> caseIDs, boolean isNewCreateCases, Integer emailSettingID, boolean fromRecurrence);

    Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress);

    Integer saveAccount(CrmAccountItem item, String type, Integer userID, boolean changeOnlyGivenValue, boolean doNotAddToSolr, boolean isMerging, boolean populateAddress, boolean runWebhook);

    CrmAccountList getCrmAccounts(ListingFilterParameter filterParametrs);

    CrmAccountItem getAccount(EdsCrmAccount account, boolean brief);

    void initClientGroups(EdsUser client);

    CrmAccountItem getAccountSolrDocumentAsRPC(CrmAccountSolrDoc doc, ListingFilterParameter fp, List<CompanyCustomFieldItem> cfResultForFiltering, List<SelectItem> types);

    SolrParams getSolrQueryForCandidate(ListingFilterParameter filterParameter, EdsUser user);

    void createLeadFromSignUpper(String data);

    void createActualLeadFromSignUpper(String data);

    void createCustomerForTextilefinds(String companyName);

    Integer saveSubsidiaryCrmAccount(CrmAccountItem crmAccountItem, Integer companyID, String transactionType);

    TypeItem getInterCompanyCrmAccountAsTypeItem(Integer crmAccountID);

    LeadList getLeadList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    String getCaseDescription(Integer caseID, boolean stripHtmls);

    Integer saveCrmNote(String entityType, Integer entityID, HistoryListItem note);

    String generateAccountNumber(String accountType);

    Integer saveCallLog(Appointment appointment);

    SelectItem[] getCaseStatus();

    ListResult<ActivityItem> getActivityList(ListingFilterParameter filterParametrs);

    String invoicePaidStatus(Date expireDate);

    Date crmAccountInvoiceExpire(List<CrmAccountInvoiceTO> activePaidInvoice, Integer crmAccountId);

    Optional<CrmAccountInvoiceTO> getFirstActivePaidInvoice(List<CrmAccountInvoiceTO> activePaidInvoice, Integer crmAccountId);

    HistoryList getCrmNoteHistory(ListingFilterParameter fp);

    ArrayList<HistoryListItem> saveCrmNotes(String entityType, Integer entityID, ArrayList<HistoryListItem> notes);

    Map<String, String[]> checkEmailExistenceInternally(Integer leadID, String[] email);

    EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID);

    ClientCurrency getClientCurrency();

    ContactListItem getIncomingCallerID();

    SelectItem saveCase(CaseItem item, boolean isFromWebForm);

    void saveFeedBack(BugReportItem feedBack);

    Integer saveLead(ContactListItem leadRPC, ArrayList<Integer> subscribedMailLists);

    CrmAccountItem getAccount(Integer objectId, String entityType);

    ListResult<ContactListItem> getNewLeads(ListingFilterParameter filterParameter);

    SelectItem[] getCurrencies();

    ContactListItem getLead(Integer leadID);

    SelectItem[] getContactsByAccount(Integer accountID, Integer contactID);

    ListResult<ActivityItem> getNewActivityList(ListingFilterParameter filterParametrs);

    ListResult<SelectItem> getLookUpItems(ListingFilterParameter filterParametrs, Integer type);

    ListResult<OpportunityListItem> getOpportunityList(ListingFilterParameter filterParametrs);

    SolrQuery getEventSolrQuery(ListingFilterParameter filterParameter, String solrQuery);

    OpportunityListItem getOpportunity(Integer objectId);

    ArrayList<Integer> deleteOpportunity(ArrayList<Integer> objectIDs);

    Integer saveOpportunity(OpportunityListItem item);

    Integer saveOpportunityWithAttachments(OpportunityListItem item, List<MultipartFile> attachments);

    boolean saveOppotunityEditCellValue(OpportunityListItem rowValue, String columnCodeName);

    SelectItem[] getBankAccounts();

    ListResult getKanbanLeadList(ListingFilterParameter filterParametrs, ListLoadConfig config);

    ArrayList<Integer> deleteCrmAccount(ArrayList<Integer> objectIDs, boolean removeContactsAlso);

    ArrayList<Integer> deleteEvent(ArrayList<Integer> objectIDs);

    ArrayList<Integer> convertLead(HashMap<Integer, OpportunityListItem> items, boolean withOpportunity);

    ContactListItem editLead(Integer objectId, Integer webFormID);

    EventItem getEvent(Integer objectId);

    ListResult<CampaignItem> getCampaigns(ListingFilterParameter filterParametrs);

    CaseList getCases(ListingFilterParameter fp);

    CaseItem getCase(Integer caseId, boolean fromSummary);

    ContactListItem makePrimaryContact(Integer accountID, Integer contactID);

    CrmAccountItem editAccount(Integer objectId, String type);

    Boolean updateOpportunity(EdsOpportunity opportunity);

    Boolean updateOpportunity(EdsOpportunity opportunity, boolean withoutUpdateAuditLog);

    SelectItem[] getOwnersListByPermission(String permissionCode);

    String getOpportunityCoreSolrQuery(EdsUser edsUser, ListingFilterParameter filterParameter);

    String getContactListSolrQuery(ListingFilterParameter fp,
                                   FacetFilterRpc contactFilter,
                                   EdsUser edsUser,
                                   String categoryIdForUserForSolrQuery);

    void updateLeadData(CompanyData companyData);

    SelectItem[] getCrmSubItemsLookUpItems(ListingFilterParameter filterParameter, CustomFieldLookUpTypeEnum typeEnum);

    List<EdsReference> getTaxTreatments();

    String getEventCoreSolrQuery(EdsUser edsUser, FacetFilterRpc eventFacetFilter, ListingFilterParameter filterParameter);

    ResponseResultListData<CrmActivityDTO> getNextEventList();

    void addEmployeeToEvent(Integer eventId, Integer employeeId);

    void updateCaseStatus(Integer caseID, Integer statusID, String note);

    void deleteCase(ArrayList<Integer> idsList);

    NumberData generateAccountNumberData(String accountType);

    String getOpportunityFacetQuery(ListingFilterParameter filterParameter, FacetFilterRpc opportunityFacetFilter);

    SelectItem[] getOpportunityStages(boolean sortByName);

    SolrQuery getOpportunitySolrQuery(ListingFilterParameter filterParameter, String solrQuery);

    OpportunityItem[] getOpportunityItems(Integer objectId);

    List<EdsReference> getUKTaxTreatments();

    void updateOpportunityNoteAndRejectReason(Integer opportunityId, String note, Integer rejectReasonId);

    void updateOpportunityStatus(OpportunityListItem data);

}


