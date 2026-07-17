package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 4:51:18 PM To
 * change this template use File | Settings | File Templates.
 */
@Repository("clientManager")
public class ClientManagerImpl extends BaseManager<EdsCrmAccount> implements ClientManager {

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ReferenceManager referenceManager;

    @Autowired
    private CrmAccountManager crmAccountManager;

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public ClientManagerImpl() {
        super(EdsCrmAccount.class);
    }

    public List<EdsCrmAccount> list() {
        return null;

    }

    public List<EdsCrmContact> getContacts(Integer clientId) {
        return find("select cc from EdsCrmContact cc where cc.crmAccount.objectID = ? and " + ServerUtils.checkForDeleted("cc.deleted") + " order by cc.objectID", clientId);
    }

    /**
     * Returns the list of clients filtered by the given params and the viewer Role.
     * You can filter by Project, Department and Employee, if you don't want to filter them, just supply null values.
     * viewAsFilter - EdsRole.DR, ADMIN, TL, PM, MEM, CLIENT values can be supplied. if supplied value, it will isolate
     * the results for that role only. Supplying null will show only the related results for the current user.
     * Mostly null should be used for the viewer, but in reports.
     */
    public SelectItem[] list(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsUser user = (fp.getUserID() != null && fp.getUserID() > 0) ? userManager.get(fp.getUserID()) : getUser();

        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();
        if (fp.isLookUp()) {
            sql.append(" SELECT c.id, c.name, c.number, ");
            sql.append("CASE ")
                    .append(" WHEN LOWER(c.name)=LOWER('").append(fp.getSearchKey()).append("') THEN 0")
                    .append(" WHEN LOWER(c.name) LIKE LOWER('").append(fp.getSearchKey()).append("%') THEN 1")
                    .append(" WHEN LOWER(c.name) LIKE LOWER('%").append(fp.getSearchKey()).append("%') THEN 3")
                    .append(" ELSE 4 ")
                    .append("END weight");
        } else {
            sql.append(" SELECT c.id, c.name, c.number ");
        }
        sql.append(" FROM ").append(companyId).append(".crmaccount c ");
        sql.append(" LEFT JOIN ").append(companyId).append(".project p ON (c.id=p.clientid) ");
        sql.append(" LEFT JOIN ").append(companyId).append(".projectemployee pe ON (p.id=pe.projectid) ");
        sql.append(" LEFT JOIN ").append(companyId).append(".teamemployee te ON (pe.employeedepartmentid=te.id) ");
        sql.append(" LEFT JOIN ").append(companyId).append(".myuser mu ON (te.employeeid=mu.id) ");
        sql.append(" LEFT JOIN ").append(companyId).append(".employee e ON (e.id=te.employeeid) ");
        sql.append(" LEFT JOIN ").append(companyId).append(".team t ON (t.id=te.teamid) ");

        if (fp.isSpecialOffer()) {
            sql.append(EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.SUPPLIER));
        } else {
            sql.append(EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.CUSTOMER));
        }

        sql.append(" AND c.deleted IS NOT TRUE ");
        if (fp.isLookUp()) {
            sql.append(" AND c.blocked IS NOT TRUE ");
        }

        if (fp.getProjectId() != null && fp.getProjectId() > 0) {
            sql.append(" AND p.id=").append(fp.getProjectId()).append(" ");
        }
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" AND t.id=").append(fp.getDepartmentId()).append(" ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND (e.id=").append(fp.getEmployeeId()).append(" OR mu.id=").append(fp.getEmployeeId()).append(") ");
        }
        if (fp.getCurrencyID() != null) {
            sql.append(" AND c.currencyid = '").append(fp.getCurrencyID()).append("' ");
        }

        if (fp.isDoNotExportToQB()) {
            sql.append(" AND quickbook_customer_id IS NULL ");
        }

        if (fp.getViewAsId() == null) {
            // Admin, Director, Accountant, Sales Manager/Sales Person/Customer Service should be able see ALl clients
            if (user.hasEitherRoles(EdsRole.ADMIN, EdsRole.DR, EdsRole.ACCOUNTANT, EdsRole.SALESMAN, EdsRole.SALESPERSON, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE)) {
                // he should be able to see all employees
            } else if (user.hasEitherRoles(EdsRole.ADMIN_LOCATION)) {
                //Location Admin should see all the clients (Not resticting for his location only, coz clients might get duplicated)
            } else if (user.hasEitherRoles(EdsRole.CLIENT) || user.isClientContact()) {
                //Not need for this, but still for security purposes
                sql.append(" AND p.clientid=").append(user.getClientContact().getClientID()).append(" ");
            } else {
                //Other roles should only see employees who matters to them
                //check if the user is TL or PM or Backup PM for this client
                sql.append(" AND (t.leaderid=").append(user.getObjectID()).append(" OR p.managerid=").append(user.getObjectID()).append(" OR p.backup_managerid=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid2=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid3=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid4=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid5=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid6=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid7=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid8=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid9=").append(user.getObjectID());
                sql.append(" OR p.backup_managerid10=").append(user.getObjectID());
                sql.append(" OR p IS NULL OR ");
                //check if the user is in the Department working for this client
                sql.append(" (t.id in (SELECT DISTINCT teamid FROM ").append(companyId).append(".teamemployee WHERE employeeid=").append(user.getObjectID()).append(")) OR ");
                //check if the user is in the Project related to this client
                sql.append(" (p.id in (SELECT DISTINCT pe2.projectid FROM ").append(companyId).append(".projectemployee pe2 LEFT JOIN ").append(companyId).append(".teamemployee te2 on te2.id=pe2.employeedepartmentid WHERE te2.employeeid=").append(user.getObjectID()).append("))) ");
            }
        }

        if (fp.getViewAsId() == null
                || EdsRole.DEFAULT.equals(fp.getViewAsId())) {
            if (user.isClientContact()) {
                sql.append(" AND p.clientid=").append(user.getClientContact().getClientID()).append(" ");
            }
        } else if (EdsRole.DR.equals(fp.getViewAsId())
                || EdsRole.ADMIN.equals(fp.getViewAsId())) {
            // if he is director or admin should see
            // all the projects of the company
        } else if (EdsRole.TL.equals(fp.getViewAsId())) {
            sql.append(" AND (t.leaderid=").append(user.getObjectID()).append(" OR p IS NULL) ");
        } else if (EdsRole.PM.equals(fp.getViewAsId())) {
            sql.append(" AND (p.managerid=").append(user.getObjectID()).append(" OR p.backup_managerid=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid2=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid3=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid4=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid5=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid6=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid7=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid8=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid9=").append(user.getObjectID());
            sql.append(" OR p.backup_managerid10=").append(user.getObjectID());
            sql.append(" OR c.owner=").append(user.getObjectID()).append(") ");
        } else if (EdsRole.MEM.equals(fp.getViewAsId())) {
            sql.append(" AND ((t.id IN (SELECT DISTINCT teamid FROM ").append(companyId).append(".teamemployee WHERE employeeid=").append(user.getObjectID()).append(") OR p IS NULL) AND ");
            sql.append(" (p.id IN (SELECT DISTINCT pe2.projectid FROM ").append(companyId).append(".projectemployee pe2 LEFT JOIN ").append(companyId).append(".teamemployee te2 ON te2.id=pe2.employeedepartmentid WHERE te2.employeeid=").append(user.getObjectID()).append(") OR p IS NULL)) ");
        } else if (EdsRole.CLIENT.equals(fp.getViewAsId())
                || user.isClientContact()) {
            sql.append(" AND (p.clientid=").append(user.getClientContact().getClientID()).append(") ");
        }

        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            if (fp.isLookUp()) {
                sql.append(" LOWER(c.name) LIKE LOWER('%").append(fp.getSearchKey()).append("%') ");
                sql.append("OR lower(c.number) LIKE LOWER('").append(fp.getSearchKey()).append("%') ");
            } else {
                sql.append("   LOWER(c.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
                sql.append("OR LOWER(c.description) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            }
            sql.append(") ");
        }

        if (fp.getEntityID() != null) {
            sql.append(" AND (c.entity_id = ").append(fp.getEntityID()).append(" ");
            if (fp.getClientName() != null && !"".equals(fp.getClientName())) {
                sql.append(" AND c.name = '").append(fp.getClientName().replace("'", "''")).append("'");
            }
            sql.append(" ) ");
        }
        sql.append(" GROUP BY c.id ");

        if (fp.isLookUp()) {
            sql.append(" ORDER BY weight, c.name ");
        } else if (fp.getSortField() == null || fp.getSortField().isEmpty()) {
            sql.append(" ORDER BY c.creationtime DESC");
        } else {
            sql.append(" ORDER BY ").append(fp.getSortField()).append(" ").append(fp.isAscending() ? "ASC" : "DESC");
        }

        if (fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        } else if (fp.isLookUp()) {
            sql.append(" OFFSET 0 LIMIT 20 ");
        } else {
            sql.append(" OFFSET 0 LIMIT 1800 ");
        }
        ArrayList<SelectItem> result = new ArrayList<>(jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class)));

        return result.toArray(new SelectItem[0]);
    }

    public List<EdsCrmAccount> customerList(Integer currencyId) {

        String companyId = getCompanyId();
        StringBuilder sql = new StringBuilder();

        sql.append(" select distinct c.id, c.* ");
        sql.append(" from ").append(companyId).append(".crmaccount c ");

        sql.append(EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.CUSTOMER));

        sql.append(" and c.deleted is not true ");

        if (currencyId != null) {
            sql.append(" and c.currencyid = ").append(currencyId);
        }

        return findNative(sql.toString(), EdsCrmAccount.class);
    }

    @Override
    public List<EdsCrmAccount> getClientsForInvoice(String searchKey) {
        EdsUser user = getUser();
        searchKey = searchKey == null ? "" : searchKey;
        if (roleManager.hasRole(user, EdsRole.DR) || roleManager.hasRole(user, EdsRole.ADMIN) || roleManager.hasRole(user, EdsRole.ACCOUNTANT)) {

            return (List<EdsCrmAccount>) findNative("SELECT c.* FROM " + getCompanyId() + ".crmaccount c " +
                    " " + EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.CUSTOMER) +
                    " and (c.deleted IS NULL or c.deleted=false) and (lower(c.name) like '" + (searchKey + "%").replace("'", "") + "' ) ORDER BY c.name", EdsCrmAccount.class);
        } else if (roleManager.hasRole(user, EdsRole.PM)) {
            return (List<EdsCrmAccount>) findNative("select distinct c.id, c.*  from " + getCompanyId() + ".crmaccount c " +
                    " inner join " + getCompanyId() + ".project p on (c.id=p.clientid ) " +
                    EdsCrmAccount.getByTypeForNativeQueries("c", EdsCrmAccount.CUSTOMER) +
                    " and c.deleted deleted is not true and (p.managerid=" + user.getObjectID() + " or p.backup_managerid=" + user.getObjectID() +
                    " or p.backup_managerid2=" + user.getObjectID() + " or p.backup_managerid3=" + user.getObjectID() + " or p.backup_managerid4=" + user.getObjectID() +
                    " or p.backup_managerid5=" + user.getObjectID() + " or p.backup_managerid6=" + user.getObjectID() + " or p.backup_managerid7=" + user.getObjectID() +
                    " or p.backup_managerid8=" + user.getObjectID() + " or p.backup_managerid9=" + user.getObjectID() + " or p.backup_managerid10=" + user.getObjectID() +
                    " or c.owner=" + user.getObjectID() + ")  " +
                    " and (lower(c.name) like '" + searchKey + "%' ) ORDER BY c.name", EdsCrmAccount.class);
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public List<EdsCrmAccount> getClientsWithInvoice(EdsCompany company) {
        return find("SELECT distinct c FROM EdsSaleInvoice si inner join si.client c  " +
                " WHERE " + ServerUtils.checkForDeleted("c.deleted"));
    }

    @Override
    public List<EdsCrmAccount> getClientsByRegDate(Date sTime, Date eTime, EdsCompany company) {
        List list;
        if (company != null) {
            list = find("FROM EdsCrmAccount cl " + EdsCrmAccount.getByTypeForHQLQueries("cl", EdsCrmAccount.CUSTOMER) + " and (((cl.lastUpdatedDate between '" + sTime + "' and '" + eTime + "') or (cl.createdDate between'" + sTime + "' and '" + eTime + "')))");
        } else {
            list = find("FROM EdsCrmAccount cl " + EdsCrmAccount.getByTypeForHQLQueries("cl", EdsCrmAccount.CUSTOMER) + " and (cl.createdDate between '" + sTime + "' and '" + eTime + "')");
        }
        return list;
    }

    @Override
    public List<EdsCrmAccount> getClientByName(String name) {
        return find("select cl FROM EdsCrmAccount cl  " + EdsCrmAccount.getByTypeForHQLQueries("cl", EdsCrmAccount.CUSTOMER) + " and " + ServerUtils.checkForDeleted("cl.deleted") + " and lower(trim(cl.name))= ? ", name.toLowerCase());
    }

    @Override
    public Boolean deleteClient(final EdsCrmAccount client) {
        updateNative("delete from " + getCompanyId() + ".crmAccount_Types where crmaccount_id = " + client.getObjectID() + " " +
                "and type_id = (select id " + EdsReference.getSql(EdsCrmAccount.CUSTOMER, EdsCrmAccount._CRM_ACCOUNT_TYPE, false) + ")");
        return Boolean.TRUE;
    }

    @Override
    public Integer getClientIdByQBCustomerId(String qbCustomerId) {
        return (Integer) findSingle("SELECT cl.objectID FROM EdsCrmAccount cl WHERE cl.quickbookCustomerID = '" + qbCustomerId + "' AND " + ServerUtils.checkForDeleted("cl.deleted"));
    }

    @Override
    /**
     * deletedga tekshirmaslik kerak...
     */
    public List<EdsCrmAccount> getClientsByIds(String Ids) {
        return (List<EdsCrmAccount>) find("SELECT c FROM EdsCrmAccount c WHERE c.objectID IN (" + Ids + ")");
    }

    @Override
    public List<String> getNames(Integer companyID) {
        return (List<String>) find("select c.name from EdsCrmAccount c where " + ServerUtils.checkForDeleted("c.deleted"));
    }

    @Override
    public List<Integer> getClientIdsByIds(List<Integer> list) {
        return crmAccountManager.getCrmAccountIDsByIDs(list, EdsCrmAccount.CUSTOMER);
    }

    @Override
    public HashMap<String, Integer> getNimbleCrmAccountsMap() {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select trim(c.name), c.id from" + companyID + ".crmaccount c ");
        sql.append("WHERE " + ServerUtils.checkForDeleted("c.deleted") + " order by c.id desc");
        List<Object[]> dataList = findNative(sql.toString());

        HashMap<String, Integer> customersMap = new HashMap<>();
        for (Object[] data : dataList) {
            if (data.length > 1 && data[0] != null && data[1] != null) {
                customersMap.put(((String) data[0]).trim().toLowerCase().replaceAll(" {2}", " "), (Integer) data[1]);
            }
        }

        return customersMap;
    }

    @Override
    public HashMap<String, Integer> getNimbleUniqueIDsMap() {
        List<Object[]> dataList = find("select si.nimbleUniqueID, si.objectID from EdsSaleInvoice si where si.nimbleUniqueID is not null");
        HashMap<String, Integer> uniqueIDsMap = new HashMap<>();
        for (Object[] data : dataList) {
            if (data.length > 1 && data[0] != null && data[1] != null) {
                uniqueIDsMap.put(((String) data[0]).trim(), (Integer) data[1]);
            }
        }
        return uniqueIDsMap;
    }

    @Override
    public Integer checkGWDCustomerForExists(String customerName) {
        EdsCrmAccount client = (EdsCrmAccount) findSingle("FROM EdsCrmAccount cra where " + ServerUtils.checkForDeleted("cra.deleted") + " and cra.name=?", customerName);
        if (client != null) {
            return client.getObjectID();
        }
        return null;
    }

    public void create(EdsCrmAccount client, boolean solrUpdate) {
        create(client);
        solrUpdate(client);
    }

    private void solrUpdate(EdsCrmAccount client) {
        try {
            if (!client.isDeleted()) {
//                solrManager.addCrmAccountToIndex(client);
                crmAccountSolrComponent.index(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(EdsCrmAccount client) {
        client.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
        crmAccountManager.create(client);
    }

    @Override
    public void update(EdsCrmAccount client) {
        crmAccountManager.update(client);
    }

    public void update(EdsCrmAccount client, boolean solrUpdate) {
        crmAccountManager.update(client, solrUpdate);
    }

    @Override
    public ArrayList<Integer> getEmployeeClients(Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct c.id FROM ").append(getCompanyId()).append(".projectEmployee pe \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".teamEmployee te on te.id = pe.employeeDepartmentId \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".project p on p.id = pe.projectid \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".project_clients pc on pc.projectid = p.id \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmaccount c on c.id = p.clientId or c.id = pc.clientid \n");
        sql.append("WHERE pe.isdeleted is not true and p.isdeleted is not true and c.id is not null \n");
        sql.append(" AND te.employeeId = ").append(employeeId);

        return (ArrayList<Integer>) findNative(sql.toString());
    }
}
