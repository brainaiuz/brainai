package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactPermission;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.crm.contact.EdsDeviceCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.impl.rbac.contact.ContactCategoryRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 16:43:45
 * To change this template use File | Settings | File Templates.
 */
@Repository("crmContactManager")
public class CrmContactManagerImpl extends BaseManager<EdsCrmContact> implements CrmContactManager {
    public CrmContactManagerImpl() {
        super(EdsCrmContact.class);
    }

    @Autowired
    protected UserManager userManager;
    @Autowired
    protected RoleManager roleManager;
    @Autowired
    protected ContactCategoryManager contactCategoryManager;
    @Autowired
    private ContactCategoryRbacManager contactCategoryRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public void deleteAllLeads(String inIDs) {
        deleteAll(inIDs, false, EdsCrmContact.LEAD_CONTACT);
    }

    public void deleteAllCandidates(String inIDs) {
        deleteAll(inIDs, false, EdsCrmContact.CANDIDATE);
    }

    public void deleteAllContacts(String inIDs) {
        deleteAll(inIDs, true, EdsCrmContact.LEAD_CONTACT);
    }

    private void deleteAll(String inIDs, boolean exceptTypes, Integer... contactTypeIDs) {
        updateNative("update crmcontact contact set deleted = true where " + getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs) + " and contact.id " + inIDs);
    }

    public List<EdsCrmContact> list(ListingFilterParameter fp) {
        return list(fp, getUser());
    }

    public List<EdsCrmContact> list(ListingFilterParameter fp, EdsUser user) {
        String sql = "";
        Date selectedDate = null;
        String sDate = "";
        if (fp.isValidSearchKey()) {
            sql += " and emails.contact.objectID = cont.objectID and emails.param = " + EdsCrmContactItemParams.EMAIL + " and " +
                    " (";
            sql += " lower(cont.firstName) like '" + fp.getSqlSearchKey().toLowerCase() + "' ";
            sql += " or lower(cont.lastName) like '" + fp.getSqlSearchKey().toLowerCase() + "' ";
            sql += " or lower(cont.assistentName) like '" + fp.getSqlSearchKey().toLowerCase() + "' ";
            sql += " or lower(emails.value) like '" + fp.getSqlSearchKey().toLowerCase() + "' ";
            sql += ") ";
        }
        //search by date
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 0) {
            try {
                selectedDate = format.parse(fp.getGroupByName());
                sDate = format2.format(selectedDate);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            sql += " and to_char(cont.creationTime, 'yyyy-mm-dd')= '" + sDate + "' ";
        }
        //search by company
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 1) {
            sql += "No Company name".equals(fp.getGroupByName()) ? " and (cont.companyName is null or trim(cont.companyName)='')" : " and cont.companyName='" + fp.getGroupByName() + "' ";
        }
        //search by contactType
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 3) {
            if ("No Type".equals(fp.getGroupByName())) {
                sql += " and (cont.contactType is null) ";
            } else {
                Integer contactType = "Crm Contact".equals(fp.getGroupByName()) ? 1 : ("Client Contact".equals(fp.getGroupByName()) ? 2 : ("Supplier Contact".equals(fp.getGroupByName()) ? 3 : 1));
                sql += " and cont.contactType=" + contactType + " ";
            }
        }
        //search by user
        if (fp.getGroupByName() != null && !"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getSearchType() == 2) {
            sql += "Without User".equals(fp.getGroupByName()) ? " and cont.owner is null " : " and '" + fp.getGroupByName() + "'=CONCAT(cont.owner.firstName, CONCAT(' ', cont.owner.lastName)) ";
        }
        //search by visibility
        String clientJ = "";
        if (user.isClientContact()) {
            clientJ = " and cont.owner.objectID in (select cc.objectID from EdsClientContact cc) ";
        }
        if (fp.getAccountID() != null) {
            sql += " and cont.crmAccount.objectID =" + fp.getAccountID();
        }
        return find("select distinct cont from EdsCrmContact cont" + (fp.isValidSearchKey() ? " left join cont.itemParams emails " : "") +
                        " where (cont.owner.objectID =?) and " +
                        ServerUtils.checkForDeleted("cont.deleted") +
                        sql + clientJ + " order by cont.objectID desc ",
                user.getObjectID());
    }

    public void deleteContact(Integer contactId, Integer inactiveID) {
        Integer userID = getUser() != null ? getUser().getObjectID() : null;
        updateNative("update " + getCompanyId() + ".myuser set deleted = true, accountstatusid = " + inactiveID + " where id in (select id from " + getCompanyId() + ".clientcontact where crmcontactid is not null and crmcontactid = " + contactId + ")");
        updateNative("update " + getCompanyId() + ".crmcontact  set deleted = true, modificationdate = now()" + (userID != null ? ", modifiedby_id = " + userID : "") + " where id = " + contactId);
        updateNative("update " + getCompanyId() + ".devicecrmcontact set status = '" + ContactListItem.DELETED + "' where contactID = '" + contactId + "'");
    }

    public List<EdsCrmContact> getContactsByOwner(EdsUser user) {
        return getByOwner(user, true, EdsCrmContact.LEAD_CONTACT);
    }

    public List<EdsCrmContact> getLeadsByOwner(EdsUser user) {
        return getByOwner(user, false, EdsCrmContact.LEAD_CONTACT);
    }

    public List<EdsCrmContact> getCandidatesByOwner(EdsUser user) {
        return getByOwner(user, false, EdsCrmContact.CANDIDATE);
    }

    public List<EdsCrmContact> getByOwner(EdsUser user, boolean exceptTypes, Integer... contactTypeIDs) {
        if (user == null) {
            return null;
        }
        return find("select cont from EdsCrmContact cont where " + getExceptedOrAdmittedTypes("cont", exceptTypes, contactTypeIDs) + " and cont.owner.objectID = " + user.getObjectID() + " and " + ServerUtils.checkForDeleted("cont.deleted"));
    }

    public List<EdsCrmContact> getByMyOwnCrmContacts(EdsUser user) {
        if (user == null) {
            return null;
        }
        return find("SELECT cont FROM EdsCrmContact cont WHERE cont.contactType=1 AND cont.owner.objectID = " + user.getObjectID() + " AND " + ServerUtils.checkForDeleted("cont.deleted"));
    }

    public List<EdsCrmContact> getMyContactsByFolderId(EdsUser user, Integer folderId) {
        if (user == null) {
            return null;
        }
        StringBuilder sql = new StringBuilder("SELECT cont FROM EdsCrmContact cont INNER JOIN cont.categories category ");
        sql.append(" WHERE ")
                .append(" cont.contactType=").append(ContactListItem.CRM_CONTACT)
                .append(" AND cont.owner.objectID = ").append(user.getObjectID());
        sql.append(" AND ").append(ServerUtils.checkForDeleted("cont.deleted"));

        if (folderId != null && folderId > 0) {
            sql.append(" AND category.objectID = ").append(folderId);
        } else {
            sql.append(" AND category.objectID IS NULL ");
        }


        return find(sql.toString());
    }

    public Integer getContactsCountByOwner(EdsUser user) {
        return getCountByOwner(user, true, EdsCrmContact.LEAD_CONTACT, EdsCrmContact.CANDIDATE, EdsCrmContact.STUDENT_CONTACT);
    }

    public Integer getLeadsCountByOwner(EdsUser user) {
        return getCountByOwner(user, false, EdsCrmContact.LEAD_CONTACT);
    }

    public Integer getCandidatesCountByOwner(EdsUser user) {
        return getCountByOwner(user, false, EdsCrmContact.CANDIDATE);
    }

    public Integer getCountByOwner(EdsUser user, boolean exceptTypes, Integer... contactTypeIDs) {
        if (user == null) {
            return null;
        }
        Long count = (Long) findSingle("select count(cont) from EdsCrmContact cont where " + getExceptedOrAdmittedTypes("cont", exceptTypes, contactTypeIDs)
                + " and cont.owner.objectID = " + user.getObjectID() + " and " + ServerUtils.checkForDeleted("cont.deleted"));
        return count != null ? count.intValue() : 0;
    }

    @Override
    public EdsCrmContact getContactByEmail(String email, Integer companyID) {
        return getByEmail(email, companyID, true, EdsCrmContact.LEAD_CONTACT); //returns not Lead contacts
    }

    @Override
    public EdsCrmContact getLeadByEmail(String email, Integer companyID) {
        return getByEmail(email, companyID, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public EdsCrmContact getCandidateByEmail(String email, Integer companyID) {
        return getByEmail(email, companyID, false, EdsCrmContact.CANDIDATE);
    }

    public EdsCrmContact getByEmail(String email, Integer companyID, boolean exceptTypes, Integer... entityTypeIDs) {
        String company = "\"" + companyID + "\"";
        email = email.toLowerCase();
        List<Integer> contacts = findNative("select contact.id from " + company + ".crmcontact contact " +
                " left join " + company + ".crmcontactitemparams emails on (emails.contactid = contact.id and emails.paramid = " + EdsCrmContactItemParams.EMAIL + ") " +
                " where contact.deleted is not true " +
                " and " + getExceptedOrAdmittedTypes("contact", exceptTypes, entityTypeIDs) +
                " and lower(emails.value) = '" + (email.contains("'") ? email.replace("'", "''") : email) + "'");
        if (contacts != null && contacts.size() > 0) {
            return get(contacts.get(0));
        }
        return null;
    }

    public EdsCrmContact getByEmailPDF(String email, Integer companyID, boolean exceptTypes, Integer... entityTypeIDs) {
        String company = "\"" + companyID + "\"";
        email = email.toLowerCase();
        List<Integer> contacts = findNative("select contact.id from " + company + ".crmcontact contact " +
                " left join " + company + ".crmcontactitemparams emails on (emails.contactid = contact.id and emails.paramid = " + EdsCrmContactItemParams.EMAIL + ") " +
                " where contact.deleted is not true and contact.crmaccount is not null" +
                " and lower(emails.value) = '" + (email.contains("'") ? email.replace("'", "''") : email) + "'");
        if (contacts != null && contacts.size() > 0) {
            return get(contacts.get(0));
        }
        return null;
    }

    public EdsCrmContact getByPhone(String phone) {

        List<EdsCrmContact> contacts = getAllByPhone(phone);
        if (contacts != null && contacts.size() > 0) {
            Optional<EdsCrmContact> lead = contacts.stream().filter(c -> EdsCrmContact.LEAD_CONTACT.equals(c.getContactType())).findFirst();
            if (lead.isPresent()) {
                return lead.get();
            }
            Optional<EdsCrmContact> contact = contacts.stream().filter(c -> EdsCrmContact.CRM_CONTACT.equals(c.getContactType())
                            || EdsCrmContact.CLIENT_CONTACT.equals(c.getContactType())
                            || EdsCrmContact.SUPPLIER_CONTACT.equals(c.getContactType())
                            || EdsCrmContact.EMPLOYEE_CONTACT.equals(c.getContactType()))
                    .findFirst();
            if (contact.isPresent()) {
                return contact.get();
            }
            Optional<EdsCrmContact> candidate = contacts.stream().filter(c -> EdsCrmContact.CANDIDATE.equals(c.getContactType())).findFirst();
            return candidate.orElseGet(() -> contacts.get(0));
        }
        return null;
    }

    public List<EdsCrmContact> getAllByPhone(String phone) {
        phone = org.apache.commons.lang.StringUtils.replace(phone, "+", "");
        phone = org.apache.commons.lang.StringUtils.replace(phone, ")", "");
        phone = org.apache.commons.lang.StringUtils.replace(phone, "(", "");
        phone = org.apache.commons.lang.StringUtils.replace(phone, "-", "");
        phone = org.apache.commons.lang.StringUtils.replace(phone, "|", "");
        phone = org.apache.commons.lang.StringUtils.replace(phone, " ", "");
        StringBuilder sql = new StringBuilder("select contact.* from " + getCompanyId() + ".crmcontact contact " +
                " inner join " + getCompanyId() + ".crmcontactitemparams phones on (phones.contactid = contact.id and phones.paramid = " + EdsCrmContactItemParams.PHONE + ") " +
//                " and replace(replace(replace(replace(replace(replace(phones.value, '+', ''), '|',''), ')',''), '(', ''),' ',''),'-','') = '" + phone + "'" +
                " and  replace(replace(replace(replace(replace(replace(phones.value, '+', ''), '|',''), ')',''), '(', ''),' ',''),'-','') LIKE  '%' || '" + phone + "' " +
                /*" and length(phones.value)<=" + (phone.length() + 5) +*/
                " and (contact.deleted is null OR contact.deleted=false) order by length(phones.value)");

        List<EdsCrmContact> contacts = findNative(sql.toString(), EdsCrmContact.class);
        return contacts;
    }

    public List<Integer> getCompanyDeletedContactsForSolr(SolrReindexRpc solrRenindex) {
        return getCompanyDeletedListForSolr(solrRenindex, true, EdsCrmContact.LEAD_CONTACT);
    }

    public List<Integer> getCompanyDeletedLeadsForSolr(SolrReindexRpc solrRenindex) {
        return getCompanyDeletedListForSolr(solrRenindex, false, EdsCrmContact.LEAD_CONTACT);
    }

    public List<Integer> getCompanyDeletedCandidatesForSolr(SolrReindexRpc solrRenindex) {
        return getCompanyDeletedListForSolr(solrRenindex, false, EdsCrmContact.CANDIDATE);
    }

    private List<Integer> getCompanyDeletedListForSolr(SolrReindexRpc solrRenindex, boolean exceptTypes, Integer... contactTypeIDs) {
        StringBuilder contactSqlQuery = new StringBuilder("select contact.id from " + getCompanyId() + ".crmcontact contact where ");
        contactSqlQuery.append(getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs) + " and contact.deleted=true ");
        contactSqlQuery.append(" and contact.modificationDate>=").append("'").append(solrRenindex.getLastUpdateTime()).append("'");
        if (solrRenindex.getLastUpdateEndTime() != null) {
            contactSqlQuery.append(" and contact.modificationDate<='").append(solrRenindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) findNative(contactSqlQuery.toString());
    }

    public List<EdsCrmContact> getCompanyContactsForSolr(SolrReindexRpc solrRenindex, Integer startAt, Integer limit) {
        return getContactListForSolr(solrRenindex, startAt, limit, true, EdsCrmContact.LEAD_CONTACT);
    }

    public List<EdsCrmContact> getCompanyLeadsForSolr(SolrReindexRpc solrRenindex, int startat, int limit) {
        return getContactListForSolr(solrRenindex, startat, limit, false, EdsCrmContact.LEAD_CONTACT);
    }

    public List<EdsCrmContact> getCompanyCandidatesForSolr(SolrReindexRpc solrRenindex, int startat, int limit) {
        return getContactListForSolr(solrRenindex, startat, limit, false, EdsCrmContact.CANDIDATE);
    }

    private List<EdsCrmContact> getContactListForSolr(SolrReindexRpc solrRpc, Integer start, Integer limit, boolean exceptTypes, Integer... contactTypeIDs) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder contactSqlQuery = new StringBuilder("select contact from EdsCrmContact contact where ");
        contactSqlQuery.append(getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs)).append(" and (contact.deleted is null or contact.deleted is not true) ");
        if (!solrRpc.isAllReindex() && solrRpc.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrRpc.getLastUpdateTime());
            contactSqlQuery.append(" and contact.auditInfo.modificationDate >= :modifiedDate");
            if (solrRpc.getLastUpdateEndTime() != null) {
                contactSqlQuery.append(" and contact.auditInfo.modificationDate<='").append(solrRpc.getLastUpdateEndTime()).append("'");
            }
        }
        contactSqlQuery.append(" order by contact.id asc");
        return findIntervalByNamedParams(contactSqlQuery.toString(), start, limit, params);
    }

    public List<EdsCrmContact> getCompanyContacts(Integer companyID, Integer startAt, Integer limit) {
        return getCompanyList(companyID, startAt, limit, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getCompanyLeads(Integer companyID, int startat, int limit) {
        return getCompanyList(companyID, startat, limit, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getCompanyCandidates(Integer companyID, int startat, int limit) {
        return getCompanyList(companyID, startat, limit, false, EdsCrmContact.CANDIDATE);
    }

    @Override
    public List<EdsCrmContact> getContactListByCompanyId(Integer companyID, Integer startAt) {
        return getCompanyList(companyID, startAt, 500, false);
    }

    private List<EdsCrmContact> getCompanyList(Integer companyID, Integer startAt, Integer limit, boolean exceptTypes, Integer... contactTypeIDs) {
        String query = "select * from \"" + companyID + "\".crmcontact contact where " + getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs) + " and contact.deleted is not true and contact.id>" + startAt + " order by contact.id asc limit " + limit;
        return findNative(query, EdsCrmContact.class);
    }

    public List<EdsCrmContact> getCandidatesAfterInterview(EdsUser user) {
//        EdsReference interviewStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_INTERVIEW);
//        EdsReference shortListStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_SHORTLIST);
//        EdsReference offerMadeStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_OFFER_MADE);
        boolean showAll = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_ALL_CANDIDATES);
        boolean showOwned = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_OWNED_CANDIDATES);
        boolean showRelated = ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_RELATED_CANDIDATES);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT crmC.* FROM " + getCompanyId() + ".crmContact crmC WHERE ");
        sql.append(getExceptedOrAdmittedTypes("crmC", false, EdsCrmContact.CANDIDATE));
        sql.append(" AND crmC.deleted is not true ");
//        sql.append("(crmC.isShortList is true OR ");
//        sql.append("(crmC.status=" + interviewStatus.getObjectID() + " OR crmC.status=" + shortListStatus.getObjectID() + "))");
        if (!showAll) {
            if (showOwned && showRelated) {
                sql.append(" AND (");
                sql.append("crmC.owner=" + user.getObjectID());
                sql.append(" OR ").append("crmC.creatorid=").append(user.getObjectID());
                sql.append(")");
            } else if (showOwned) {
                sql.append(" AND crmC.owner=").append(user.getObjectID());
            } else if (showRelated) {
                sql.append(" AND crmC.creatorid=").append(user.getObjectID());
            } else {
                sql.append(" AND crmC.id=").append(0);
            }
        }
        return findNative(sql.toString(), EdsCrmContact.class);
    }

    public Map<Integer, String> getContactIdAndRolesMap(String contactIds) {
        Map<Integer, String> map = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT contact.id as contactID, role.name \n");
        sql.append("FROM ").append(getCompanyId()).append(".crmContact contact \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".employeeprofile pl ON (pl.contact_id=contact.id ) \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".employee e ON (e.profileId=pl.id ) \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".myuser_role user_role ON (user_role.users_id=e.id) \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".role role ON (user_role.roles_id=role.id) \n");
        sql.append("WHERE contact.contactType=").append(EdsCrmContact.EMPLOYEE_CONTACT).append(" AND contact.deleted is not true").append(" AND contact.id in (" + contactIds + ")");
        List<Object[]> objects = findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object[] values : objects) {
                if (values[0] != null && values[0] instanceof Integer contactId) {
                    String roles = String.valueOf(values[1]);
                    if (map.containsKey(contactId)) {
                        roles = roles + ", " + map.get(contactId);
                    }
                    map.put((Integer) values[0], roles);
                }
            }
        }
        return map;
    }

    public Map<Integer, Integer> getMapIdAndHasPlacement(List<Integer> candidateIDs) {
        Map<Integer, Integer> booleanMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT candid.id as candidateID, \n");
//        sql.append("(CASE WHEN pl.id is not null THEN true ELSE false END) as hasPlacementID, \n");
        sql.append("pl.id as placementID \n");
        sql.append("FROM ").append(getCompanyId()).append(".crmContact candid \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".placement pl ON (pl.candidate_id=candid.id AND pl.deleted is not true) \n");
        sql.append("WHERE candid.contactType=").append(EdsCrmContact.CANDIDATE).append(" AND candid.deleted is not true");
        List<Object[]> objects = findNative(sql.toString());
        if (objects != null && objects.size() > 0) {
            for (Object[] values : objects) {
                if (values[0] != null && values[0] instanceof Integer) {
                    booleanMap.put((Integer) values[0], (Integer) values[1]);
                }
            }
        }
        return booleanMap;
    }

    @Override
    public List<Integer> getCompanyContactIds(Integer companyID, int startat, int limit) {
        return getIds(companyID, startat, limit, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<Integer> getCompanyLeadIds(Integer companyID, int startat, int limit) {
        return getIds(companyID, startat, limit, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<Integer> getCompanyCandidateIds(Integer companyID, int startat, int limit) {
        return getIds(companyID, startat, limit, false, EdsCrmContact.CANDIDATE);
    }

    private List<Integer> getIds(Integer companyID, int startat, int limit, boolean exceptTypes, Integer... contactTypeIDs) {
        String query = "select id from \"" + companyID + "\".crmcontact t where " + getExceptedOrAdmittedTypes("t", exceptTypes, contactTypeIDs) + " and t.deleted is not true and t.id>" + startat + " order by t.id asc limit " + limit;
        return (List<Integer>) findNative(query);
    }

    @Override
    public EdsCrmContact getContactByAccountName(String name) {
        return getByCompanyName(name, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public EdsCrmContact getLeadByAccountName(String name) {
        return getByCompanyName(name, false, EdsCrmContact.LEAD_CONTACT);
    }

    private EdsCrmContact getByCompanyName(String name, boolean exceptTypes, Integer... contactTypeIDs) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyName", name);
        List<EdsCrmContact> contacts = findByNamedParams("select contact from EdsCrmContact contact where " + getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs) + " and contact.crmAccount.name = :companyName and " + ServerUtils.checkForDeleted("contact.deleted"), map);
        return contacts != null && contacts.size() > 0 ? contacts.get(0) : null;
    }

    @Override
    public List<EdsCrmContact> getContactsByImportFileID(Integer importFileID, int start, int limit) {
        return getByImportFileID(importFileID, start, limit, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getLeadsByImportFileID(Integer importFileID, int start, int limit) {
        return getByImportFileID(importFileID, start, limit, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getCandidatesByImportFileID(Integer importFileID, int start, int limit) {
        return getByImportFileID(importFileID, start, limit, false, EdsCrmContact.CANDIDATE);
    }

    private List<EdsCrmContact> getByImportFileID(Integer importFileID, int start, int limit, boolean exceptTypes, Integer... contactTypeIDs) {
        return findLimited("from EdsCrmContact cc where " + ServerUtils.checkForDeleted("cc.deleted") + " and " + getExceptedOrAdmittedTypes("cc", exceptTypes, contactTypeIDs) + " and cc.importFileID = " + importFileID + " and cc.objectID > " + start + " order by objectID asc", limit);
    }

    public void clearGoogleIdFromContact(Integer userId) {
        update("update EdsCrmContact contact set contact.googleId = null where contact.owner.objectID = ?", userId);
    }

    /*@Override
    public ArrayList<ContactCategoryListItem> getContactCategories() {
        EdsUser user = contactCategoryManager.getUser();
        boolean isPrivileged = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST);
        List<EdsContactCategory> list = contactCategoryManager.getAllCategories(user.getObjectID());
        List<EdsContactCategory> list2 = contactCategoryManager.getSharedCategories(user.getObjectID(), isPrivileged);
        if (list2 == null) {
            list2 = contactCategoryManager.getSharedPrivateCategories(user.getObjectID(), isPrivileged);
        } else {
            list2.addAll(contactCategoryManager.getSharedPrivateCategories(user.getObjectID(), isPrivileged));
        }
        return EdsContactCategory.getRPCsWithChildren(list, list2);
    }*/

    @Override
    public List<EdsCrmContact> getSharedOrOwnedContactsByIDs(List<Integer> objectIDs) {
        EdsUser user = getUser();
        String owner = user != null ? user.getObjectID().toString() : "0";
        if (user != null && user.hasEitherRoles(EdsRole.ADMIN)) {

            return find("select contact from EdsCrmContact contact where " + ServerUtils.checkForDeleted("contact.deleted") + " and contact.objectID in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")");
        } else {
            return findNative("select contact.* from " + getCompanyId() + ".crmcontact contact " +
                    "inner join " + getCompanyId() + ".crmcontact_contactcategory category on category.crmcontact_id = contact.id " +
                    "inner join " + getCompanyId() + ".contactcategoryrbac rbac on rbac.contactcategory_id = category.categories_id " +
                    "inner join " + getCompanyId() + ".contactpermission permission on permission.id = rbac.contactpermissionid " +
                    "where ((rbac.userid = " + owner + " and permission.write is true) or contact.owner = " + owner + ") and contact.deleted is not true and contact.id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ");", EdsCrmContact.class);
        }
    }

    @Override
    public void updateContactWithAccountID(Integer accountID, List<Integer> otherAccountIDs) {
        update("update EdsCrmContact set crmAccount = " + accountID + " where crmAccount in (" + ServerUtils.getAsCommoDelimited(otherAccountIDs, "0", ",") + ")");
    }

    @Override
    public List<EdsCrmContact> getContactsByCrmAccount(Integer accountID) {
        return find("select contact from EdsCrmContact contact where " + ServerUtils.checkForDeleted("contact.deleted") + " and contact.crmAccount is not null and contact.crmAccount = " + accountID + " ORDER BY contact.firstName");
    }

    @Override
    public boolean hasContactsByCrmAccount(Integer accountID) {
        Long count = (Long) findSingle("select count(contact.objectID) from EdsCrmContact contact where " + ServerUtils.checkForDeleted("contact.deleted") + " and contact.crmAccount is not null and contact.crmAccount = " + accountID);
        return count != null && count > 0;
    }

    @Override
    public List<EdsCrmContact> getContactsByCampaign(Integer campaignID) {
        return find("select contact from EdsCrmContact contact where " + ServerUtils.checkForDeleted("contact.deleted") + " and contact.campaign is not null and contact.campaign = " + campaignID);
    }

    @Override
    public List<EdsCrmContact> getContactsByIDs(List<Integer> objectIDs) {
        return getByIDs(objectIDs, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getCandidatesByIDs(List<Integer> objectIDs) {
        return getByIDs(objectIDs, false, EdsCrmContact.CANDIDATE);
    }

    @Override
    public EdsCrmContact getCandidateById(Integer id) {
        return (EdsCrmContact) findSingle("select c from EdsCrmContact c where c.objectID = " + id);
    }

    @Override
    public List<EdsCrmContact> getLeadsByIDs(List<Integer> objectIDs) {
        return getByIDs(objectIDs, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<MContactListItem> getContactIDsWithStatuses(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("SELECT distinct c.id as objectID, dc.deviceContactID, c.modificationDate as updatedDate, ");
        sql.append("CASE WHEN c.deleted is not NULL and c.deleted = TRUE THEN '").append(ContactListItem.DELETED).append("' ELSE ");
        sql.append("dc.status END as status FROM ");
        sql.append(getCompanyId()).append(".crmcontact c ");
        if (fp.getCategories() != null && fp.getCategories().length > 0) {
            sql.append("INNER JOIN ").append(getCompanyId()).append(".crmcontact_contactcategory cc on c.id = cc.crmcontact_id AND ");
            sql.append("cc.categories_id IN (").append(ServerUtils.getAsCommoDelimited(Arrays.asList(fp.getCategories()), "0", ",")).append(") ");
        }

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".devicecrmcontact dc ON c.id = dc.contactID");
        if (fp.getDeviceID() != null && !"".equals(fp.getDeviceID())) {
            sql.append(" AND dc.deviceID = '").append(fp.getDeviceID()).append("'");
        }

        sql.append(" WHERE c.owner = ").append(userManager.getUser().getObjectID()).append(" AND ");
        //sql.append("NOT (c.deleted is not null and c.deleted is TRUE and dc.devicecontactid is NULL) AND ");
        sql.append(getExceptedOrAdmittedTypes("c", true, ContactListItem.LEAD_CONTACT));
        sql.append(" ORDER BY c.id");
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }
        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(MContactListItem.class));
    }

    @Override
    public Integer getContactIDsWithStatusesCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("SELECT COUNT(distinct c.id) FROM ");

        sql.append(getCompanyId()).append(".crmcontact c ");
        if (fp.getCategories() != null && fp.getCategories().length > 0) {
            sql.append("INNER JOIN ").append(getCompanyId()).append(".crmcontact_contactcategory cc on c.id = cc.crmcontact_id AND ");
            sql.append("cc.categories_id IN (").append(ServerUtils.getAsCommoDelimited(Arrays.asList(fp.getCategories()), "0", ",")).append(") ");
        }

        sql.append("LEFT JOIN ").append(getCompanyId()).append(".devicecrmcontact dc ON c.id = dc.contactID");
        if (fp.getDeviceID() != null && !"".equals(fp.getDeviceID())) {
            sql.append(" AND dc.deviceID = '").append(fp.getDeviceID()).append("'");
        }

        sql.append(" WHERE c.owner = ").append(userManager.getUser().getObjectID()).append(" AND ");
        //sql.append("NOT (c.deleted is not null and c.deleted is TRUE and dc.devicecontactid is NULL) AND ");
        sql.append(getExceptedOrAdmittedTypes("c", true, ContactListItem.LEAD_CONTACT));
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public EdsDeviceCrmContact getDeviceContact(String deviceID, String deviceContactID) {
        return (EdsDeviceCrmContact) find("select dc from EdsDeviceCrmContact dc where dc.deviceID = ? and dc.deviceContactID = ?", deviceID, deviceContactID);
    }

    @Override
    public EdsDeviceCrmContact getDeviceContact(String deviceID, Integer contactID) {
        return (EdsDeviceCrmContact) find("select dc from EdsDeviceCrmContact dc where dc.deviceID = ? and dc.contactID = ?", deviceID, contactID);
    }

    private List<EdsCrmContact> getByIDs(List<Integer> objectIDs, boolean exceptTypes, Integer... contactTypeIDs) {
        return (List<EdsCrmContact>) find("select distinct crmContact from EdsCrmContact crmContact " +
                " where " + getExceptedOrAdmittedTypes("crmContact", exceptTypes, contactTypeIDs) + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + " and crmContact.objectID in ( " + ServerUtils.getAsCommoDelimited(objectIDs, "0") + ")");
    }

    @Override
    public List<Integer> getContactIDsByIDs(List<Integer> objectIDs) {
        return getIDsByIDs(objectIDs, true, EdsCrmContact.LEAD_CONTACT, EdsCrmContact.CANDIDATE);
    }

    @Override
    public List<Integer> getLeadIDsByIDs(List<Integer> objectIDs) {
        return getIDsByIDs(objectIDs, false, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<Integer> getCandidateIDsByIDs(List<Integer> objectIDs) {
        return getIDsByIDs(objectIDs, false, EdsCrmContact.CANDIDATE);
    }

    private List<Integer> getIDsByIDs(List<Integer> objectIDs, boolean exceptTypes, Integer... contactTypeIDs) {
        return (List<Integer>) find("select crmContact.objectID from EdsCrmContact crmContact where " + getExceptedOrAdmittedTypes("crmContact", exceptTypes, contactTypeIDs) + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + " and objectID in ( " + ServerUtils.getAsCommoDelimited(objectIDs, "0") + ")");
    }

    @Override
    public List<EdsCrmContact> getContactByEntityID(Integer entityID) {
        return getByEntityID(entityID, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public List<EdsCrmContact> getLeadByEntityID(Integer entityID) {
        return getByEntityID(entityID, false, EdsCrmContact.LEAD_CONTACT);
    }

    public List<EdsCrmContact> getByEntityID(Integer entityID, boolean exceptTypes, Integer... contactTypeIDs) {
        return (List<EdsCrmContact>) find("select crmContact from EdsCrmContact crmContact where " + getExceptedOrAdmittedTypes("crmContact", exceptTypes, contactTypeIDs) + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + " and crmContact.entityID = ?", entityID);
    }

    public Integer getByLeadID(Integer leadID) {
        return (Integer) findNativeSingle("select cc.entity_id from" + getCompanyId() + ".crmContact cc where " +    ServerUtils.checkForDeleted("cc.deleted") + " and cc.id = " + leadID);
    }

    @Override
    public PermissionHolder getPermission(Integer contactID) {
        EdsUser user = userManager.getUser();
        if (contactID != null && user != null) {
            EdsCrmContact contact = get(contactID);
            if (contact != null) {
                EdsContactPermission permission = getPermission(contact, user);
                return permission != null ? permission.getDTO() : null;
            }
        }
        return null;
    }

    private EdsContactPermission getPermission(EdsCrmContact contact, EdsUser user) {
        if (user != null && ((contact.getOwner() != null && user.getObjectID().equals(contact.getOwner().getObjectID())) || user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_MANAGER))) {
            return EdsContactPermission.getOwnersEdsPermission();
        }
        return contactCategoryRbacManager.getContactCategoryPermissionForUser(user, contact.getCategories().toArray(new EdsContactCategory[]{}));
    }

    @Override
    public boolean canDelete(EdsCrmContact contact, EdsUser user) {
        EdsContactPermission permission = getPermission(contact, user);
        return permission != null && permission.isDelete();
    }

    @Override
    public List<Object> getColumnNumbersForCSV(String ids, Integer param, Integer... relations) {
        Integer companyID = SecurityContext.getCompanyID();
        if (ids != null && !"".equals(ids)) {
            List<Object> result = null;
            // if param == null it means we must select from address zone(table)...
            if (param != null && relations != null && relations.length > 0) {
                StringBuilder sql = new StringBuilder();
                String union = "";
                for (Integer relation : relations) {
                    sql.append(union).append("select * from ( select ").append(relation.toString()).append(" as \"relation\", count(id) as counter from \"").append(companyID).append("\".crmcontactitemparams where paramid = ").append(param).append(" and relationid = ").append(relation).append(" group by contactid order by counter desc limit 1) t").append(relation);
                    union = " UNION ";
                }
                sql = new StringBuilder("".contentEquals(sql) ? sql.toString() : (sql.append(" order by relation ")));
                result = findNative(sql.toString());
            } else {
                StringBuilder sql = new StringBuilder();
                String union = "";
                for (Integer relation : relations) {
                    sql.append(union).append("select * from ( select ").append(relation.toString()).append(" as \"relation\", count(id) as counter from \"").append(companyID).append("\".address ").append("where contactid is not null and relationType = ").append(relation).append(" group by contactID order by counter desc limit 1) t").append(relation);
                    union = " UNION ";
                }
                sql = new StringBuilder("".contentEquals(sql) ? sql.toString() : (sql.append(" order by relation ")));
                result = findNative(sql.toString());
            }
            return result;
        }
        return null;
    }

    @Override
    public List<EdsCrmContact> getDuplicates(Integer objectID, String firstName, String lastName, String primaryEmail, String primaryPhone, boolean isLead) {
        EdsUser user = getUser();
        Map<String, Object> map = new HashMap<>();
        String objectId = "";
        if (objectID != null) {
            map.put("objectID", objectID);
            objectId = " and crmContact.objectID <> :objectID ";
        }
        map.put("primaryEmail", primaryEmail);
        String inCategory = "";
        if (!isLead) {
            boolean isPrivileged = ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST);
            Set<Integer> sharedCategoryIDs = contactCategoryManager.getSharedCategoryIDsForUser(isPrivileged);
            List<Integer> privateCategory = EdsObject.getObjectIDs(contactCategoryManager.getPrivateCategory(isPrivileged));
            privateCategory.forEach(sharedCategoryIDs::remove);
            if (sharedCategoryIDs != null && sharedCategoryIDs.size() > 0) {
                inCategory = " and ((category.objectID in (" + ServerUtils.getAsCommoDelimited(new ArrayList<>(sharedCategoryIDs), "0", ",") + ")";
                if (user != null && privateCategory != null && privateCategory.size() > 0) {
                    inCategory += " or ( category.objectID in (" + ServerUtils.getAsCommoDelimited(privateCategory, "0", ",") + ") and crmContact.owner.objectID = " + user.getObjectID();
                    inCategory += ")";
                }
                inCategory += ") or category.objectID is null)";
            }
        } else {
            inCategory += " and crmContact.contactType = " + EdsCrmContact.LEAD_CONTACT;
        }
        List<EdsCrmContact> emailDuplicates = findByNamedParams("select crmContact from EdsCrmContact crmContact " +
                "left join crmContact.categories category where " +
                " crmContact.primaryEmail = :primaryEmail " +
                objectId + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + inCategory, map);
        map.remove("primaryEmail");

        map.put("phone", primaryPhone);
        List<EdsCrmContact> phoneDuplicates = findByNamedParams("select crmContact from EdsCrmContact crmContact " +
                "left join crmContact.categories category where " +
                " crmContact.primaryPhone = :phone " +
                objectId + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + inCategory, map);
        emailDuplicates.addAll(phoneDuplicates);
//        map.remove("phone");

//        map.put("firstName", firstName);
//        map.put("lastName", lastName);

//        List<EdsCrmContact> nameDuplicates = findByNamedParams("select crmContact from EdsCrmContact crmContact " +
//                "left join crmContact.categories category where " +
//                " crmContact.firstName = :firstName and crmContact.lastName = :lastName " +
//                objectId + " and " + ServerUtils.checkForDeleted("crmContact.deleted") + inCategory, map);
//        emailDuplicates.addAll(nameDuplicates);
        return new ArrayList<>();
    }

    @Override
    public List<EdsCrmContact> getContactsByCategoryIDs(List<Integer> integers, int startAt, int limit) {
        return findLimited("select distinct contact from EdsCrmContact contact inner join contact.categories cc where " + ServerUtils.checkForDeleted("contact.deleted") + " and cc.objectID in (" + ServerUtils.getAsCommoDelimited(integers, "0") + ") and contact.objectID > " + startAt, limit);
    }

    private void fillBlankFields(final EdsCrmContact contact) {
        if (contact.getPrimaryEmail() == null || "".equals(contact.getPrimaryEmail())) {
            contact.setPrimaryEmail(contact.getPrimaryEmailFromAll());
        }
        if (contact.getPrimaryPhone() == null || "".equals(contact.getPrimaryPhone())) {
            contact.setPrimaryPhone(contact.getPrimaryPhoneFromAll());
        }
        if (contact.getCategories().size() == 0) {
            contact.addCategories(contactCategoryManager.getDefaultCategoryByContactType(contact.getContactType()));
        }
    }

    public List<Object[]> getLeadBySource() {
        String companyId = getCompanyId();
        StringBuilder sql = null;
        sql = new StringBuilder();
        sql.append("SELECT (select r.name from " + companyId + ".reference as r where r.id=l.source) as so, (SELECT count(ll.id) " +
                "FROM " + companyId + ".crmcontact as ll WHERE ll.source=l.source and ll.deleted is not true and ll.contacttype = " + EdsCrmContact.LEAD_CONTACT + " and ll.source is not null) as so_count " +
                "FROM " + companyId + ".crmcontact as l where l.deleted is not true and l.contacttype = " + EdsCrmContact.LEAD_CONTACT + " and l.source is not null ");
        sql.append(" group by l.source limit 6");
        return findNative(sql.toString());
    }

    public List<Object[]> getLeadByStatus() {
        String companyId = getCompanyId();
        StringBuilder sql = null;
        sql = new StringBuilder();
        sql.append("SELECT (select r.name from " + companyId + ".reference as r where r.id=l.status) as sta, (SELECT count(ll.id) " +
                "FROM " + companyId + ".crmcontact as ll WHERE ll.status=l.status and ll.deleted is not true and ll.contacttype = " + EdsCrmContact.LEAD_CONTACT + "and ll.status is not null) as stacount " +
                "FROM " + companyId + ".crmcontact as l where l.deleted is not true and l.contacttype = " + EdsCrmContact.LEAD_CONTACT + "and l.status is not null ");
        sql.append(" group by l.status limit 6");
        return findNative(sql.toString());
    }

    public Integer getCountByContactTypeAndStatus(Long position, Integer contactType, String leadStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(cc) ")
                .append(" FROM EdsCrmContact cc ")
                .append(" where (cc.deleted is null or cc.deleted <> true) ")
                .append(" and cc.contactType = ").append(contactType);
//                .append(" and cc.leadStatus.code = ?");
        if (leadStatus != null) {
            sql.append(" and cc.leadStatus.code = ?");
        } else {
            sql.append(" and cc.leadStatus IS NULL ");
        }
        if (position != null) {
            sql.append(" and cc.kanbanorder > ").append(position);
        }
        if (leadStatus != null) {
            return ((Long) findSingle(sql.toString(), leadStatus)).intValue();
        } else {
            return ((Long) findSingle(sql.toString())).intValue();
        }
    }

    public List<EdsCrmContact> getContactsByContactTypeAndStatus(Long position, Integer contactType, String leadStatus, int start, int limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cc ")
                .append(" FROM EdsCrmContact cc ")
                .append(" where (cc.deleted is null or cc.deleted <> true) ")
                .append(" and cc.contactType = ").append(contactType);
        if (leadStatus != null) {
            sql.append(" and cc.leadStatus.code = ?");
        } else {
            sql.append(" and cc.leadStatus IS NULL ");
        }
        if (position != null) {
            sql.append(" and kanbanorder > ").append(position);
        }
        sql.append(" order by cc.kanbanorder desc ");
        if (leadStatus != null) {
            return findInterval(sql.toString(), start, limit, leadStatus);
        } else {
            return findInterval(sql.toString(), start, limit);
        }
    }

    public EdsCrmContact getSiblingContactByKanbanOrderAndContactType(Integer contactId, Integer contactType, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cc ")
                .append(" FROM EdsCrmContact cc ")
                .append(" where (cc.deleted is null or cc.deleted <> true) ")
                .append(" and cc.contactType = ").append(contactType);

        if (status != null) {
            sql.append(" and cc.leadStatus.code = '").append(status).append("'");
        } else {
            sql.append(" and cc.leadStatus is NULL");
        }

        sql.append(" and cc.kanbanorder > ").append("(select cc2.kanbanorder from EdsCrmContact cc2 where cc2.objectID = ").append(contactId).append(")");

        return (EdsCrmContact) findSingle(sql.toString());
    }

    public Map<String, String[]> checkEmailExistence(Integer leadID, String[] email, EdsUser user, boolean isSaleManagerOrAdmin) {
        StringBuilder emails = new StringBuilder();
        if (email != null && email.length == 1 && !"".equals(email[0]) && email[0] != null && email[0].matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
            emails = new StringBuilder(" = " + "'" + extractEmail(email[0]) + "' ");
        }
        if (email != null && email.length > 1) {
            String delimitr = "";
            boolean wroteIn = false;
            for (String email_ : email) {
                if (email_ != null && !"".equals(email_) && email_.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                    if (!wroteIn) {
                        emails = new StringBuilder(" in (");
                        wroteIn = true;
                    }
                    emails.append(delimitr).append("'").append(extractEmail(email_)).append("'");
                    delimitr = ",";
                }
            }
            if (emails.toString().contains(" in (")) {
                emails.append(")");
            }
        }
        List<EdsCrmContact> leads = null;
        if (!"".contentEquals(emails)) {
            leads = (List<EdsCrmContact>) find("select lead from EdsCrmContact lead " +
                    " where " +
                    " lead.contactType = " + EdsCrmContact.LEAD_CONTACT +
                    " and (lower(lead.primaryEmail) " + emails + ") and " +
                    ServerUtils.checkForDeleted("lead.deleted"));
        }
        Map<String, String[]> result = new HashMap<>();
        if (leads != null && leads.size() > 0) {
            List<String> emailList = Arrays.asList(email);

            for (EdsCrmContact lead : leads) {
                if (lead != null && (leadID == null || (leadID != null && !lead.getObjectID().equals(leadID)))) {
                    boolean cantSee = false;
                    if (!isSaleManagerOrAdmin && user != null) {
                        if (lead.getLeadAssignee() != null && !user.getObjectID().equals(lead.getLeadAssignee().getObjectID())) {
                            cantSee = lead.getLeadBackupAssignee() == null || !user.getObjectID().equals(lead.getLeadBackupAssignee().getObjectID());
                        }
                    }
                    String[] value = cantSee ? new String[]{lead.getObjectID().toString(), "lead", "cantsee"} : new String[]{lead.getObjectID().toString(), "lead"};
                    if (emailList.contains(lead.getPrimaryEmail())) {
                        result.put(lead.getPrimaryEmail(), value);
                    }
                }
            }
        }
        return result;
    }

    private String extractEmail(String email) {
        if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.indexOf("<") + 1, email.indexOf(">"));
            return (email.contains("<") && email.contains(">")) || email.contains(",") || email.contains("'") ? extractEmail(email) : email.toLowerCase();
        }
        if (email.contains(",") || email.contains("'")) {
            email = email.replace(",", "");
            email = email.replace("'", "");
            return email.toLowerCase();
        }
        return email.toLowerCase();
    }

    @Override
    public EdsCrmContact getContactByPrimaryEmail(String email) {
        return getByPrimaryEmail(email, true, EdsCrmContact.LEAD_CONTACT);
    }

    @Override
    public EdsCrmContact getLeadByPrimaryEmail(String email) {
        return getByPrimaryEmail(email, false, EdsCrmContact.LEAD_CONTACT);
    }

    private EdsCrmContact getByPrimaryEmail(String email, boolean exceptTypes, Integer... contactTypeIDs) {
        return (EdsCrmContact) findNativeSingle("select * from " + getCompanyId() + ".crmContact contact where " + getExceptedOrAdmittedTypes("contact", exceptTypes, contactTypeIDs) + " and contact.primaryemail ='" + email + "' and contact.deleted is not true", EdsCrmContact.class);
    }

    public EdsCrmContact getByFirstName(String name) {
        return (EdsCrmContact) findNativeSingle("select * from " + getCompanyId() + ".crmContact contact where " + " contact.firstName ='" + name + "' and contact.deleted is not true", EdsCrmContact.class);
    }


    @Override
    public String getCountryOfPrimaryAddress(Integer contactID) {
        if (contactID == null) {
            return null;
        }
        return (String) findNativeSingle("select name from " + getPublic() + ".country where id in (select a.countryid from " + getCompanyId() + ".address a where a.contactid = " + contactID + " and a.countryid is not null order by a.relationtype asc limit 1)");
    }

    @Override
    public Set<String> getEmailSetOfSharedContacts(List<Integer> categoryIDs) {
        EdsUser user = getUser();
        String owner = user != null ? user.getObjectID().toString() : "0";
        List<String> emails = findNative("select lower(contact.primaryemail) from " + getCompanyId() + ".crmcontact_contactcategory category inner join " + getCompanyId() + ".crmcontact contact on contact.deleted is not true and contact.id = category.crmcontact_id and contact.contacttype <> " + EdsCrmContact.LEAD_CONTACT + " where  category.categories_id in (" + ServerUtils.getAsCommoDelimited(categoryIDs, "0", ",") + ");");
        return ServerUtils.listToSet(emails);
    }

    @Override
    public Set<String> getEmailSetOfLeads() {
        return ServerUtils.listToSet(findNative("select lower(lead.primaryemail) from " + getCompanyId() + ".crmcontact lead where lead.primaryemail is not null and lead.deleted is not true and lead.contacttype = " + EdsCrmContact.LEAD_CONTACT));
    }

    @Override
    public Map<String, Integer> getContactsForImport(Integer... contactTypes) {
        StringBuilder contactType = new StringBuilder(contactTypes == null || contactTypes.length == 0 ? "0" : "");
        if (contactTypes != null && contactTypes.length > 0) {
            for (Integer type : contactTypes) {
                contactType.append(",").append(type.toString());
            }
            contactType = new StringBuilder(contactType.toString().replace(',', ' '));
        }
        List<Object[]> contacts = findNative("select lower(contact.firstName||' '||contact.lastName), contact.id from " + getCompanyId() + ".crmcontact contact where contact.deleted is not true and contacttype not in (" + contactType + ")");
        Map<String, Integer> result = new HashMap<>();
        if (contacts != null && contacts.size() > 0) {
            for (Object[] contact : contacts) {
                if (contact[0] != null) {
                    result.put(contact[0].toString(), Integer.valueOf(contact[1].toString()));
                }
            }
        }
        return result;
    }

    public Integer findContactIdByNameAndCrmAccount(Integer crmAccountId, String contactName, Integer... notInContactTypes) {
        if (StringUtils.isNotBlank(contactName)) {
            StringBuilder contactType = new StringBuilder(notInContactTypes == null || notInContactTypes.length == 0 ? "0" : "");
            if (notInContactTypes != null && notInContactTypes.length > 0) {
                for (Integer type : notInContactTypes) {
                    contactType.append(type.toString()).append(",");
                }
                contactType = new StringBuilder(contactType.substring(0, contactType.length() - 1));
            }

            /*String splittedName[] = contactName.split(" ");
            String firstname = null;
            String lastname = "";
            if(splittedName.length>1) {
                firstname = splittedName[0].toLowerCase();
                for(int i = 1; i<splittedName.length;i++) {
                    lastname = lastname + " " + splittedName[i].toLowerCase();
                }
                lastname.trim();
            } else {
                firstname = splittedName[0].toLowerCase();
            }*/
            StringBuilder query = new StringBuilder("SELECT contact.id FROM ");
            query.append(getCompanyId()).append(".crmcontact contact WHERE contact.deleted IS NOT TRUE AND trim( lower(COALESCE(contact.firstName, '')||' '||COALESCE(contact.lastName, '')) )='");
            query.append(contactName).append("' AND contacttype NOT IN (").append(contactType).append(")");
            if (crmAccountId != null && crmAccountId > 0) {
                query.append(" AND contact.crmAccount=").append(crmAccountId);
            }
            return (Integer) findNativeSingle(query.toString());
        } else {
            return null;
        }
    }

    @Override
    public void changeLeadStatus(Integer statusID, List<Integer> ids) {
        update("update EdsCrmContact set auditInfo.modificationDate = ?, auditInfo.modifiedBy= ?, leadStatus = ? where objectID in (" + ServerUtils.getAsCommoDelimited(ids, "0") + ")", new Date(), getUser(), referenceManager.get(statusID));
    }

    @Override
    public void changeLeadAssignee(Integer assigneeID, List<Integer> ids) {
        update("update EdsCrmContact set auditInfo.modificationDate = ?,  auditInfo.modifiedBy= ?, assignee = ? where objectID in (" + ServerUtils.getAsCommoDelimited(ids, "0") + ")", new Date(), getUser(), userManager.get(assigneeID));
    }

    /**
     * esdan chiqmasligi uchun ... bu faqat csv export uchun ishlatilsin...
     * boshqa joyda ishlatilmasin...
     *
     * @param lessObjectIDs
     * @return
     */
    @Override
    public List<ContactListItem> getContactRPCsByIDsForCSVExport(List<Integer> lessObjectIDs) {
        List<ContactListItem> items = new ArrayList<>();
        List<Object[]> contacts = findNative("select " +
                "contact.id, " +
                "contact.firstname, " +
                "contact.middlename, " +
                "contact.lastname, " +
                "contact.othername, " +
                "contact.title, " +
                "contact.dateOfBirth, " +
                "account.name, " +
                "contact.jobTitles, " +
                "contact.department" +
                " from " + getCompanyId() + ".crmcontact contact " +
                " left join " + getCompanyId() + ".crmaccount account on contact.crmaccount = account.id " +
                " where contact.deleted is not true and contact.id in (" + ServerUtils.getAsCommoDelimited(lessObjectIDs, "0", ",") + ")" +
                " order by contact.modificationDate desc");
        if (contacts != null && contacts.size() > 0) {
            for (Object[] contact : contacts) {
                Integer contactID = contact[0] != null ? Integer.parseInt(contact[0].toString()) : null;
                if (contactID == null) {
                    continue;
                }
                String firstName = contact[1] != null ? String.valueOf(contact[1]) : null;
                String middleName = contact[2] != null ? String.valueOf(contact[2]) : null;
                String lastName = contact[3] != null ? String.valueOf(contact[3]) : null;
                String otherName = contact[4] != null ? String.valueOf(contact[4]) : null;
                String title = contact[5] != null ? String.valueOf(contact[5]) : null;
                Date dateOfBirth = null;
                try {
                    dateOfBirth = contact[6] != null && !"".equals(contact[6]) ? new Date(((Timestamp) contact[6]).getTime()) : null;
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                String companyName = contact[7] != null ? String.valueOf(contact[7]) : null;
                String position = contact[8] != null ? String.valueOf(contact[8]) : null;
                String department = contact[9] != null ? String.valueOf(contact[9]) : null;
                ContactListItem item = new ContactListItem();
                item.setObjectId(contactID);
                item.setFirstName(firstName);
                item.setMiddleName(middleName);
                item.setLastName(lastName);
                item.setOtherName(otherName);
                item.setTitle(title);
                item.setBirthDate(dateOfBirth != null ? new DateNonConvertable(dateOfBirth) : null);
                item.getCrmAccount().setName(companyName);
                item.setJobTitle(position);
                item.setDepartment(department);
                items.add(item);
            }
        }
        return items;
    }

    @Override
    public void create(EdsCrmContact contact) {
        if (!contact.getHistorical()) {
            fillBlankFields(contact);
            if (contact.getCrmAccount() != null && !contact.getCrmAccount().isDeleted()) {
                contact.setEntityID(contact.getCrmAccount().getEntityID());
            }
        }
        EdsAuditInfo info = contact.getAuditInfo() != null ? contact.getAuditInfo() : new EdsAuditInfo();
        if (info.getCreatedBy() == null) {
            info.setCreatedBy(getUser());
        }
        if (info.getCreationDate() == null) {
            info.setCreationDate(new Date());
        }
        info.setModificationDate(new Date());
        info.setModifiedBy(getUser());
        info.setSuperUser(ServerUtils.isSuperUser());
        contact.setAuditInfo(info);
        super.create(contact);
    }

    @Override
    public void update(EdsCrmContact contact) {
        fillBlankFields(contact);
        if (contact.getCrmAccount() != null && !contact.getCrmAccount().isDeleted()) {
            contact.setEntityID(contact.getCrmAccount().getEntityID());
        }

        EdsAuditInfo info = contact.getAuditInfo();
        if (info != null) {
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            info.setSuperUser(ServerUtils.isSuperUser());
        } else {
            info = new EdsAuditInfo();
            if (info.getCreatedBy() == null) {
                info.setCreatedBy(getUser());
            }
            if (info.getCreationDate() == null) {
                info.setCreationDate(new Date());
            }
            info.setModificationDate(new Date());
            info.setModifiedBy(getUser());
            info.setSuperUser(ServerUtils.isSuperUser());
            contact.setAuditInfo(info);
        }
        super.update(contact);
    }

    private void cloneCrmContact(EdsCrmContact obj, EdsCrmContact clonedCase) {
        if (clonedCase != null) {
            EdsAuditInfo auditInfo = new EdsAuditInfo();
            auditInfo.setSuperUser(ServerUtils.isSuperUser());

            clonedCase.setAuditInfo(auditInfo);
            clonedCase.setDeleted(true);
            clonedCase.setHistorical(true);
            clonedCase.setHistoricalParent(obj);
            clonedCase.setHistories(new ArrayList<>());
            clonedCase.setCustomFields(null);
            clonedCase.setOldCustomFields(null);
            clonedCase.setItemParams(new ArrayList<>());
            clonedCase.setAddresses(new ArrayList<>());
            clonedCase.setCategories(null);
            clonedCase.setVacancies(null);
            clonedCase.setDeviceCrmContacts(null);
            clonedCase.setTrackerIdSet(new HashSet<>());
            clonedCase.setAllowances(new ArrayList<>());
            clonedCase.setItemTables(new HashSet<>());
            clonedCase.setCrmContactItems(new ArrayList<>());
            clonedCase.setCandidateHistory(new HashSet<>());
            clonedCase.setStatusHistories(new ArrayList<>());
            clonedCase.setCandidateItemTables(new HashSet<>());
            this.create(clonedCase);
        }
    }

    @Override
    public void update(EdsCrmContact contact, boolean addToSolr) {
        update(contact);
        if (addToSolr) {
            try {
                contactSolrComponent.index(contact);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void createHistory(EdsCrmContact contact) {
        EdsCrmContact clonedContact = null;
        if ((contact.is(EdsCrmContact.LEAD_CONTACT) || contact.is(EdsCrmContact.CANDIDATE)) && contact.isPropertiesChanged()) {
            clonedContact = contact.cloneShallow();
            contact.setPropertiesChanged(false);
            //Create History
            cloneCrmContact(contact, clonedContact);
        }
    }

    @Override
    public Integer getCandidateLastNumber() {
        return (Integer) findSingle("select c.numberInteger from EdsCrmContact c where contactType = " + EdsCrmContact.CANDIDATE + " or numberInteger is not null order by c.objectID desc");
    }

    @Override
    public void changeCampaign(Integer campaignId, ArrayList<Integer> leadIDs, Integer objectID) {
        updateNative("update " + getCompanyId() + ".crmcontact set modificationDate = '" + new Date() + "',  modifiedby_id = " + objectID + ", campaign = " + campaignId + " where id in (" + ServerUtils.getAsCommoDelimited(leadIDs, "0") + ")");
    }

    @Override
    public List<EdsCrmContact> getContactsByCrmAccounts(List<Integer> objectIDs) {
        return find("select d from EdsCrmContact d where " + ServerUtils.checkForDeleted("d.deleted") + " and d.crmAccount.objectID in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0") + ")");
    }

    @Override
    public Map<Integer, ContactListItem> getPrimaryContactsRPCsShort(List<Integer> idsFromSolrDocument) {
        Map<Integer, ContactListItem> map = new HashMap<>();
        List<Object[]> contacts = findNative("select contact.crmaccount, contact.id, contact.firstname, contact.lastname, contact.primaryemail from " + getCompanyId() + ".crmcontact contact where contact.crmaccount is not null and " + ServerUtils.checkForDeleted("contact.deleted") + " and contact.crmaccount in (" + ServerUtils.getAsCommoDelimited(idsFromSolrDocument, "0") + ") and contact.primaryContact is true");
        if (contacts != null && contacts.size() > 0) {
            for (Object[] contact : contacts) {
                if (contact != null && contact.length > 3) {
                    Integer accountID = (Integer) contact[0];
                    Integer contactID = (Integer) contact[1];
                    String contactFirstName = (String) contact[2];
                    String contactLastName = (String) contact[3];
                    String contactEmail = null;
                    if (contact.length > 4) {
                        contactEmail = (String) contact[4];
                    }
                    if (accountID != null && contactID != null) {
                        ContactListItem item = new ContactListItem();
                        item.setObjectId(contactID);
                        item.setFirstName(contactFirstName);
                        item.setLastName(contactLastName);
                        item.setPrimaryEmail(contactEmail);
                        map.put(accountID, item);
                    }
                }
            }
        }
        return map;
    }

    private String getExceptedOrAdmittedTypes(String field, boolean exceptTypes, Integer... contactTypeIDs) {
        StringBuilder contactTypes = new StringBuilder("1=1");
        if (contactTypeIDs != null && contactTypeIDs.length > 0) {
            String equalOrNotEqual = exceptTypes ? "<>" : "=";
            for (Integer contactTypeID : contactTypeIDs) {
                if (contactTypeID != null && !"".equals(contactTypeID)) {
                    contactTypes.append(" and ").append(field).append(".contactType ").append(equalOrNotEqual).append(contactTypeID);
                }
            }
        }
        return contactTypes.toString();
    }

    public List<EdsContactCategory> getContactCategoriesByContactID(Integer contactID) {
        return find("select cc.categories from EdsCrmContact cc  where cc.objectID = ?", contactID);
    }

    @Override
    public List<EdsEmployee> getBirthdayEmployees(ListingFilterParameter fp) {
        boolean hasLimit = fp.getEndDate() == null;
        String sql = "select mu.*, e.* from " + getCompanyId() + ".employee e  \n" +
                "left join " + getCompanyId() + ".myuser mu on (e.id=mu.id)  \n" +
                "left join " + getCompanyId() + ".teamemployee te on (e.id = te.employeeid and te.isdeleted=false)  \n" +
                "left join " + getCompanyId() + ".team tem on (tem.id=te.teamid)  \n" +
                "left join " + getCompanyId() + ".employeeprofile pr on (pr.id = e.profileid) \n" +
                "left join " + getCompanyId() + ".crmContact c on (c.id = pr.contact_id)  \n" +
                "  where mu.deleted<>true and c.dateofbirth is not null \n" +
                "  and mu.id not in(" + ServerUtils.getAsCommoDelimited(fp.getObjectIDs(), "0") + ") \n";
        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql += "  and to_char(c.dateofbirth, 'MM.dd') \n" +
                    "  between to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) + "', 'yyyy.MM.dd'), 'MM.dd') \n" +
                    "  and to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate()) + "', 'yyyy.MM.dd'), 'MM.dd') \n";
        } else if (fp.getStartDate() != null) {
            sql += " and to_char(c.dateofbirth, 'MM.dd') >= to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) + "', 'yyyy.MM.dd'), 'MM.dd') \n";
        }
        sql += "  order by to_char(c.dateofbirth, 'MM.dd')";
        if (hasLimit) {
            sql += "  limit 20";
        }
        return findNative(sql, EdsEmployee.class);
    }

    @Override
    public Integer getClientResultListCount(ListingFilterParameter fp) {
        return Integer.parseInt(findNativeSingle("select count(distinct contact.id) " +
                getSQLClientResultList(fp)).toString());
    }

    private String getSQLClientResultList(ListingFilterParameter fp) {
        return " from " + getCompanyId() + ".crmcontact contact " +
                " where contact.contacttype = " + EdsCrmContact.EMPLOYEE_CONTACT + " and contact.deleted is not true" + " and contact.dateofbirth is not null " +
                " and to_char(contact.dateofbirth, 'MM.dd') between "
                + " to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate()) +
                "', 'yyyy.MM.dd'), 'MM.dd') and to_char(to_date('" + new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate()) + "', 'yyyy.MM.dd'), 'MM.dd')" + "order by to_char(contact.dateofbirth, 'MM.dd') desc ";

    }

    @Override
    public List<Integer> getStatusChangedLeads(Integer statusId, ArrayList<Integer> ids) {
        return findNative("select id from " + getCompanyId() + ".crmcontact where (status is null or status<>" + statusId + ") and id in (" + ServerUtils.getAsCommoDelimited(ids, ",") + ")");
    }

    @Override
    public EdsCrmContact getContactBySaasuUID(String saasuUID) {
        return (EdsCrmContact) findSingle("SELECT c FROM EdsCrmContact c WHERE c.saasuGUID='" + saasuUID + "' AND " + ServerUtils.checkForDeleted("c.deleted") + " ORDER BY c.objectID");
    }

    @Override
    public List<EdsCrmContact> getByPrimaryEmail(String email) {
        if (email == null || "".equals(email.trim())) {
            return null;
        }
        return find("select c from EdsCrmContact c where " + ServerUtils.checkForDeleted("c.deleted") + " and lower(c.primaryEmail) like '" + email.toLowerCase().replace("'", "''") + "'");
    }

    @Override
    public EdsCrmContact getOneByPrimaryEmail(String email) {
        if (email == null || "".equals(email.trim())) {
            return null;
        }
        return (EdsCrmContact) findSingle("select c from EdsCrmContact c where " + ServerUtils.checkForDeleted("c.deleted") + " and lower(c.primaryEmail) like '" + email.toLowerCase().replace("'", "''") + "'");
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setContactCategoryManager(ContactCategoryManager contactCategoryManager) {
        this.contactCategoryManager = contactCategoryManager;
    }

    public void setContactCategoryRbacManager(ContactCategoryRbacManager contactCategoryRbacManager) {
        this.contactCategoryRbacManager = contactCategoryRbacManager;
    }

    public void setSolrManager(SolrManager solrManager) {
        this.solrManager = solrManager;
    }

    @Override
    public Set<String> getDuplicateNamesSet(List<Integer> idsFromSolrDocument, List<Integer> inIDs) {
        Set<String> result = new HashSet<>();
        String idsFromSolrDocumentString = idsFromSolrDocument != null
                && idsFromSolrDocument.size() > 0 ? " id in (" + ServerUtils.getAsCommoDelimited(idsFromSolrDocument, "0", ",") + ") and " : "";
        String idsOfAccountDetectingForDuplicatesString = inIDs != null && inIDs.size() > 0 ? " a.firstname in " +
                "(" + "select distinct innerA.firstname from " + getCompanyId() + ".crmContact innerA where " +
                "innerA.deleted is not true and id in (" + ServerUtils.getAsCommoDelimited(inIDs, "0", ",") + ")) " +
                "and a.lastname in (select distinct innerB.lastname from " + getCompanyId() + ".crmContact innerB where " +
                "innerB.deleted is not true and id in (" + ServerUtils.getAsCommoDelimited(inIDs, "0", ",") + ")) and " : "";
        List<String> queryResult = findNative("select distinct a.firstname||' '||a.lastname as name from " + getCompanyId() + ".crmContact a " +
                "where " + idsFromSolrDocumentString + idsOfAccountDetectingForDuplicatesString + " " +
                "a.deleted is not true and a.firstname is not null and a.lastname is not null");
        if (queryResult != null && queryResult.size() > 0) {
            result.addAll(queryResult);
        }
        return result;
    }

    @Override
    public List<EdsCrmContact> getContactListByIds(String ids) {
        return findNative("SELECT c.* FROM " + getCompanyId() + ".crmcontact c WHERE c.deleted=FALSE AND c.id IN (" + ids + ")", EdsCrmContact.class);
    }

    @Override
    public Integer getProjectIDByContact(Integer contactID) {
        return (Integer) findSingle("select c.candidateProject.objectID from EdsCrmContact c where c.objectID=?", contactID);
    }

    @Override
    public List<Object[]> getList(int contactType) {
        return findNative("select lower(primaryEmail), id from " + getCompanyId() + ".crmContact where deleted is not true and primaryEmail is not null and primaryEmail <> '' and contactType = " + contactType);
    }

    @Override
    public List<Object[]> getPhoneNumbers(int contactType) {
        return findNative("select lower(primaryPhone), id from " + getCompanyId() + ".crmContact where deleted is not true and primaryPhone is not null and primaryPhone <> '' and contactType = " + contactType);
    }

    @Override
    public void addTrackerToEmails(Integer companyID, Integer trackerID, Set<String> emails) {
    }

    @Override
    public void deleteItems(Integer objectID) {
        update("DELETE FROM EdsCrmContactItem c WHERE c.crmContact.objectID = ?", objectID);
    }

    @Override
    public Long getMinKanbanOrder(Integer contactType, Integer statusId) {
        if (statusId == null || statusId == 0) {
            return slaveEntityManager.createQuery("SELECT min(cc.kanbanorder) FROM EdsCrmContact cc  where (cc.deleted is null or cc.deleted <> true) " +
                            "and cc.contactType=:contactType and cc.leadStatus IS NULL", Long.class)
                    .setParameter("contactType", contactType).getSingleResult();
        } else {
            return slaveEntityManager.createQuery("SELECT min(cc.kanbanorder) FROM EdsCrmContact cc  where (cc.deleted is null or cc.deleted <> true) " +
                            "and cc.contactType=:contactType and cc.leadStatus.objectID=:leadStatusID", Long.class)
                    .setParameter("contactType", contactType).setParameter("leadStatusID", statusId).getSingleResult();
        }
    }

    @Override
    public EdsCrmContact getByObjectKey(String objectKey) {
        return (EdsCrmContact) findSingle("select s from EdsCrmContact s where s.objectKey = ? and " + ServerUtils.checkForDeleted("s.deleted"), objectKey);
    }
}
