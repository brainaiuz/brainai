package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.crm.contact.EdsDeviceCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 16:42:58
 * To change this template use File | Settings | File Templates.
 */
public interface CrmContactManager extends Manager<EdsCrmContact> {

    void deleteAllLeads(String ids);

    void deleteAllContacts(String ids);

    void deleteAllCandidates(String ids);

    List<EdsCrmContact> list(ListingFilterParameter fp);

    List<EdsCrmContact> list(ListingFilterParameter fp, EdsUser user);

    void deleteContact(Integer contactId, Integer referenceID);

    List<EdsCrmContact> getContactsByOwner(EdsUser user);

    List<EdsCrmContact> getByMyOwnCrmContacts(EdsUser user);

    List<EdsCrmContact> getMyContactsByFolderId(EdsUser user, Integer folderId);

    List<EdsCrmContact> getLeadsByOwner(EdsUser user);

    List<EdsCrmContact> getCandidatesByOwner(EdsUser user);

    Integer getContactsCountByOwner(EdsUser user);

    Integer getLeadsCountByOwner(EdsUser user);

    Integer getCandidatesCountByOwner(EdsUser user);

    EdsCrmContact getContactByEmail(String email, Integer companyID);

    EdsCrmContact getByPhone(String phone);

    List<EdsCrmContact> getAllByPhone(String phone);

    EdsCrmContact getLeadByEmail(String email, Integer companyID);

    EdsCrmContact getCandidateByEmail(String email, Integer companyID);

    EdsCrmContact getByEmail(String email, Integer companyID, boolean exceptTypes, Integer... entityTypeIDs);

    EdsCrmContact getByEmailPDF(String email, Integer companyID, boolean exceptTypes, Integer... entityTypeIDs);

    List<Integer> getCompanyDeletedContactsForSolr(SolrReindexRpc solrRenindex);

    List<Integer> getCompanyDeletedLeadsForSolr(SolrReindexRpc solrRenindex);

    List<Integer> getCompanyDeletedCandidatesForSolr(SolrReindexRpc solrRenindex);

    List<EdsCrmContact> getCompanyContactsForSolr(SolrReindexRpc solrRenindex, Integer startAt, Integer limit);

    List<EdsCrmContact> getCompanyLeadsForSolr(SolrReindexRpc solrRenindex, int startat, int limit);

    List<EdsCrmContact> getCompanyCandidatesForSolr(SolrReindexRpc solrRenindex, int startat, int limit);

    List<EdsCrmContact> getCompanyContacts(Integer companyID, Integer startAt, Integer limit);

    List<EdsCrmContact> getContactsByImportFileID(Integer importFileID, int start, int limit);

    List<EdsCrmContact> getLeadsByImportFileID(Integer importFileID, int start, int limit);

    List<EdsCrmContact> getCandidatesByImportFileID(Integer importFileID, int start, int limit);

    void clearGoogleIdFromContact(Integer userId);

    List<EdsCrmContact> getContactsByIDs(List<Integer> objectIDs);

    List<EdsCrmContact> getCandidatesByIDs(List<Integer> objectIDs);

    EdsCrmContact getCandidateById(Integer id);

    List<EdsCrmContact> getLeadsByIDs(List<Integer> objectIDs);

    //FOR DEVICE SYNC
    List<MContactListItem> getContactIDsWithStatuses(ListingFilterParameter fp);

    Integer getContactIDsWithStatusesCount(ListingFilterParameter fp);

    EdsDeviceCrmContact getDeviceContact(String deviceID, Integer contactID);

    EdsDeviceCrmContact getDeviceContact(String deviceID, String deviceContactID);

    /*
    List<MContactListItem> getContactIDsAndDatesByIDs(List<Integer> objectIDs);

    List<EdsCrmContact> getContactsByiPhoneIDs(String IDs, String lastName, String firstName, String primaryEmail);

    List<EdsCrmContact> getContactsByiPhoneIDs(List<String> IDs);

    List<EdsCrmContact> getContactssByAndroidIDs(String IDs);
    */

    List<Integer> getContactIDsByIDs(List<Integer> objectIDs);

    List<Integer> getLeadIDsByIDs(List<Integer> objectIDs);

    List<Integer> getCandidateIDsByIDs(List<Integer> objectIDs);

    List<EdsCrmContact> getContactByEntityID(Integer entityID);

    Integer getByLeadID(Integer leadID);

    List<EdsCrmContact> getLeadByEntityID(Integer entityID);

    List<EdsCrmContact> getDuplicates(Integer objectID, String firstName, String lastName, String primaryEmail, String primaryPhone, boolean isLead);

    PermissionHolder getPermission(Integer contactID);

    boolean canDelete(EdsCrmContact contact, EdsUser user);

    List<Object> getColumnNumbersForCSV(String ids, Integer param, Integer... relation);

    List<Integer> getCompanyContactIds(Integer companyID, int startat, int limit);

    List<Integer> getCompanyLeadIds(Integer companyID, int startat, int limit);

    List<Integer> getCompanyCandidateIds(Integer companyID, int startat, int limit);

    EdsCrmContact getContactByAccountName(String name);

    EdsCrmContact getLeadByAccountName(String name);

    List<EdsCrmContact> getCompanyLeads(Integer companyID, int startat, int limit);

    List<EdsCrmContact> getCompanyCandidates(Integer companyID, int startat, int limit);

    List<EdsCrmContact> getContactListByCompanyId(Integer companyID, Integer startAt);

    List<EdsCrmContact> getCandidatesAfterInterview(EdsUser user);

    Map<Integer, Integer> getMapIdAndHasPlacement(List<Integer> candidateIDs);

    Map<Integer, String> getContactIdAndRolesMap(String contactIds) ;

    List<Object[]> getLeadBySource();

    List<Object[]> getLeadByStatus();

    Integer getCountByContactTypeAndStatus(Long position, Integer contactType, String leadStatus);

    List<EdsCrmContact> getContactsByContactTypeAndStatus(Long position, Integer contactType, String leadStatus, int start, int limit);

    EdsCrmContact getSiblingContactByKanbanOrderAndContactType(Integer prevContact, Integer contactType, String status);

    Map<String, String[]> checkEmailExistence(Integer leadID, String[] email, EdsUser user, boolean isOrSaleManagerAndAdmin);

    EdsCrmContact getContactByPrimaryEmail(String email);

    EdsCrmContact getByFirstName(String name);

    EdsCrmContact getLeadByPrimaryEmail(String email);

    List<EdsCrmContact> getContactsByCategoryIDs(List<Integer> integers, int startAt, int limit);

    String getCountryOfPrimaryAddress(Integer contactID);

    Set<String> getEmailSetOfSharedContacts(List<Integer> categoryIDs);

    Set<String> getEmailSetOfLeads();

    List<ContactListItem> getContactRPCsByIDsForCSVExport(List<Integer> lessObjectIDs);

    Map<String, Integer> getContactsForImport(Integer... contactType);

    Integer findContactIdByNameAndCrmAccount(Integer crmAccountId, String contactName, Integer... notInContactTypes);

    void changeLeadStatus(Integer statusID, List<Integer> ids);

    void changeLeadAssignee(Integer assigneeID, List<Integer> ids);

    //ArrayList<ContactCategoryListItem> getContactCategories();

    List<EdsCrmContact> getSharedOrOwnedContactsByIDs(List<Integer> objectIDs);

    void updateContactWithAccountID(Integer accountID, List<Integer> otherAccountIDs);

    List<EdsCrmContact> getContactsByCrmAccount(Integer accountID);

    boolean hasContactsByCrmAccount(Integer accountID);

    List<EdsCrmContact> getContactsByCampaign(Integer campaignID);

    void update(EdsCrmContact contact, boolean addToSolr);

    Map<Integer, ContactListItem> getPrimaryContactsRPCsShort(List<Integer> idsFromSolrDocument);

    List<EdsContactCategory> getContactCategoriesByContactID(Integer contactID);

    List<EdsEmployee> getBirthdayEmployees(ListingFilterParameter fp);

    Integer getClientResultListCount(ListingFilterParameter fp);

    void createHistory(EdsCrmContact contact);

    List<Integer> getStatusChangedLeads(Integer statusId, ArrayList<Integer> ids);

    EdsCrmContact getContactBySaasuUID(String saasuUID);

    List<EdsCrmContact> getByPrimaryEmail(String email);

    EdsCrmContact getOneByPrimaryEmail(String email);

    Integer getCandidateLastNumber();

    void changeCampaign(Integer campaignId, ArrayList<Integer> leadIDs, Integer objectID);

    List<EdsCrmContact> getContactsByCrmAccounts(List<Integer> objectIDs);

    List<EdsCrmContact> getContactListByIds(String ids);

    Set<String> getDuplicateNamesSet(List<Integer> idsFromSolrDocument, List<Integer> inIDs);

    Integer getProjectIDByContact(Integer contactID);

    List<Object[]> getList(int contactType);

    Long getMinKanbanOrder(Integer contactType, Integer statusId);

    List<Object[]> getPhoneNumbers(int contactType);

    void addTrackerToEmails(Integer companyID, Integer objectID, Set<String> emails);

    void deleteItems(Integer objectID);

    EdsCrmContact getByObjectKey(String objectKey);
}
