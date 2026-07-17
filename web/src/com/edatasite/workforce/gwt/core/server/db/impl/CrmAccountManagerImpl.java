package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:23:37
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"unchecked"})
@Repository("crmAccountManager")
public class CrmAccountManagerImpl extends BaseManager<EdsCrmAccount> implements CrmAccountManager, CrmConstants, Constants {

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public CrmAccountManagerImpl() {
        super(EdsCrmAccount.class);
    }

    @Override
    public List<EdsCrmAccount> getList(ListingFilterParameter fp, String accountType) {
        fp = fp == null ? new ListingFilterParameter() : fp;
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct a.* from " + getCompanyId() + ".crmaccount a ");
        if (accountType != null && !"".equals(accountType)) {
            fp.setAccountType(accountType);
        }
        if (fp.getAccountType() != null && !"".equals(fp.getAccountType())) {
            sql.append(EdsCrmAccount.getByTypeForNativeQueries("a", fp.getAccountType())).append(" and ");
        } else {
            sql.append(" where ");
        }
        sql.append(ServerUtils.checkForDeleted("a.deleted"));
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(a.name) like '" + fp.getSqlSearchKey() + "' ");
            if (!fp.isLookUp()) {
                sql.append(" or lower(a.email) like '" + fp.getSqlSearchKey() + "'");
                sql.append(" or lower(a.description) like '" + fp.getSqlSearchKey() + "' ");
            }
            sql.append(")");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" and (a.owner= ").append(fp.getEmployeeId()).append(") ");
        }
        if (fp.getAccountID() != null && fp.getAccountID() > 0) {
            sql.append(" and (a.id= ").append(fp.getAccountID()).append(") ");
        }
        if (fp.getEntityID() != null) {
            sql.append(" and a.entity_id = ").append(fp.getEntityID());
        }
        sql.append(" Order By a.id asc ").append(fp.getLimit() != null && fp.getLimit() > 0 ? " limit " + fp.getLimit() : "");
        return findNative(sql.toString(), EdsCrmAccount.class);
    }

    @Override
    public List<EdsCrmAccount> list(ListingFilterParameter fp) {
        String sOrder = " account.objectID ";
        boolean desc = !(fp != null && fp.getSortDir() != null) || fp.getSortDir().equals(Constants.DESC);
        if (fp != null && fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (fp.getSortField().equals(CrmAccountItem.OWNER)) {
                sOrder = " owner.firstName || owner.lastName ";
            } else if (fp.getSortField().equals(CrmAccountItem.ACCOUNT_NAME)) {
                sOrder = " account.name ";
            } else if (fp.getSortField().equals(CrmAccountItem.PHONE)) {
                sOrder = " account.phone ";
            } else if (fp.getSortField().equals(CrmAccountItem.FAX)) {
                sOrder = " account.fax ";
            } else if (fp.getSortField().equals(CrmAccountItem.WEBSITE)) {
                sOrder = " account.website ";
            } else if (fp.getSortField().equals(CrmAccountItem.EMAIL)) {
                sOrder = " account.email ";
            } else if (fp.getSortField().equals(CrmAccountItem.ACCOUNT_TYPE)) {
                sOrder = " accountType.name ";
            } else if (fp.getSortField().equals(CrmAccountItem.STATUS)) {
                sOrder = " account.donotshow ";
            } else {
                sOrder = " account.objectID ";
                desc = true;
            }
        }

        String sql = "select distinct account, " + sOrder + " as orderBy from EdsCrmAccount account " +
                " left join account.owner owner " +
                "where " + ServerUtils.checkForDeleted("account.deleted") +
                getSqlBody(fp) + " order by orderBy " + (desc ? " DESC " : getAscOrDesc(fp));
        List<Object[]> objects = new ArrayList<>();
        if (fp != null && fp.isAllByFilter()) {
            int start = 0;
            int limit = 200;
            List<Object[]> objects_ = new ArrayList<>();
            do {
                objects_ = findInterval(sql, start, limit);
                start += objects_.size();
                objects.addAll(objects_);
            } while (objects_.size() >= limit);
        } else {
            objects = findInterval(sql, fp != null && fp.getStart() != null && fp.getStart() > 0 ? fp.getStart() : 0, fp != null && fp.getLimit() != null && fp.getLimit() > 0 ? fp.getLimit() : 20);
        }
        List<EdsCrmAccount> accounts = new ArrayList<>();
        if (objects != null && objects.size() > 0) {
            for (Object[] account : objects) {
                if (account[0] instanceof EdsCrmAccount) {
                    accounts.add((EdsCrmAccount) account[0]);
                }
            }
        }
        return accounts;
    }

    @Override
    public int getListCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(distinct account) from EdsCrmAccount as account where ").append(ServerUtils.checkForDeleted("account.deleted"));
        sql.append(getSqlBody(fp));
        return ((Number) findSingle(sql.toString())).intValue();
    }

    private String getSqlBody(ListingFilterParameter fp) {
        String sql = "";
        if (fp.isValidSearchKey()) {
            sql += " and (";
            sql += " lower(account.name) like '" + fp.getSqlSearchKey() + "' ";
            sql += " or lower(account.email) like '" + fp.getSqlSearchKey() + "' ";
            sql += ") ";
        }
        if (fp.getGroupByName() != null && !"".equals(fp.getGroupByName()) && !"all".equalsIgnoreCase(fp.getGroupByName())) {
            //search by date
            if (fp.getSearchType() == 0) {
                Date selectedDate = new Date(Long.valueOf(fp.getGroupByName()));
                String sDate = format2.format(selectedDate);
                sql += " and to_char(account.creationTime, 'yyyy-mm-dd')= '" + sDate + "' ";
            }
            //search by user
            if (fp.getSearchType() == 1) {
                sql += " and '" + fp.getGroupByName() + "'=account.owner.objectID ";
            }
        }
        return sql;
    }

    @Override
    public Map<String, EdsCrmAccount> getAllCrmAccountsMap() {
        Map<String, EdsCrmAccount> map = new HashMap<>();
        List<EdsCrmAccount> list = find("select account from EdsCrmAccount  account where " + ServerUtils.checkForDeleted("account.deleted") + " order by account.objectID");
        if (list != null && list.size() > 0) {
            for (EdsCrmAccount account : list) {
                if (account != null && account.getName() != null) {
                    map.put(account.getName().toLowerCase().trim(), account);
                }
            }
        }
        return map;
    }

    @Override
    public HashMap<String, EdsCrmAccount> getCrmAccountsMap(String crmAccountType, boolean mappedByNumber) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct ca.*, 0 as clazz_ from ").append(companyID).append(".crmaccount ca ");
        sql.append("LEFT JOIN ").append(companyID).append(".crmAccount_types cat on cat.crmaccount_id=ca.id ");
        sql.append("LEFT JOIN ").append(companyID).append(".reference ref on ref.id=cat.type_id ");
        sql.append("WHERE 1=1 ").append(crmAccountType != null ? " and ref.code = '" + crmAccountType + "' " : "").append(" and ").append(ServerUtils.checkForDeleted("ca.deleted"));
        List<EdsCrmAccount> crmAccountList = (List<EdsCrmAccount>) findNative(sql.toString(), EdsCrmAccount.class);
        HashMap<String, EdsCrmAccount> map = new HashMap<>();
        if (mappedByNumber) {
            for (EdsCrmAccount crmAccount : crmAccountList) {
                if (crmAccount.getNumber() != null)
                map.put(crmAccount.getNumber().trim(), crmAccount);
            }
        } else {
            for (EdsCrmAccount crmAccount : crmAccountList) {
                if (crmAccount.getName() != null)
                    map.put(crmAccount.getName().toLowerCase().trim(), crmAccount);
            }
        }
        return map;
    }

    @Override
    public HashMap<Integer, EdsCrmAccount> getCrmAccountsAsMap() {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct ca.*, 0 as clazz_ from ").append(companyID).append(".crmaccount ca ");
        sql.append("LEFT JOIN ").append(companyID).append(".crmAccount_types cat on cat.crmaccount_id=ca.id ");
        sql.append("LEFT JOIN ").append(companyID).append(".reference ref on ref.id=cat.type_id ");
        sql.append("WHERE 1=1 ").append(" and ").append(ServerUtils.checkForDeleted("ca.deleted"));
        List<EdsCrmAccount> crmAccountList = (List<EdsCrmAccount>) findNative(sql.toString(), EdsCrmAccount.class);
        HashMap<Integer, EdsCrmAccount> map = new HashMap<>();
        for (EdsCrmAccount crmAccount : crmAccountList) {
            map.put(crmAccount.getObjectID(), crmAccount);
        }
        return map;
    }

    @Override
    public List<Object[]> getList() {
        return findNative("select lower(account.name), account.id from " + getCompanyId() + ".crmaccount account where account.deleted is not true");
    }

    @Override
    public List<Object[]> getListWithAccountNumber() {
        return findNative("select account.number, account.id from " + getCompanyId() + ".crmaccount account where account.deleted is not true");
    }

    public List<Integer> getCompanyDeletedCrmAccountsForSolr(SolrReindexRpc solrReindex) {
        return (List<Integer>) find("select account.objectID from EdsCrmAccount account where account.deleted=true and account.lastUpdateTime>=?"
                + (solrReindex.getLastUpdateEndTime() != null ? " and account.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""), solrReindex.getLastUpdateTime());
    }

    @Override
    public List<EdsCrmAccount> getCompanyCrmAccountsForSolr(SolrReindexRpc solrReindex, int startat, int limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder crmAccountSqlQuery = new StringBuilder("select ac from EdsCrmAccount ac where (ac.deleted is null or ac.deleted <> true) ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            crmAccountSqlQuery.append(" and ac.lastUpdateTime >= : modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                crmAccountSqlQuery.append(" and ac.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        crmAccountSqlQuery.append(" order by ac.id asc ");
        return findIntervalByNamedParams(crmAccountSqlQuery.toString(), startat, limit, params);
    }

    @Override
    public List<Integer> getCrmAccountIDsByIDs(List<Integer> ids, String... types) {
        StringBuilder innerJoins = new StringBuilder();
        StringBuilder innerWheres = new StringBuilder(" 1=1 ");
        if (types != null && types.length > 0) {
            int i = 0;
            innerJoins.append(" inner join ").append(getCompanyId()).append(".crmaccount_types typeIDs on typeIDs.crmaccount_id = account.id ");
            for (String type : types) {
                innerJoins.append(" inner join ").append(getCompanyId()).append(".reference type_").append(i).append(" on type_").append(i).append(".id = typeIDs.type_id ");
                innerWheres.append(" and type_").append(i).append(".code = '").append(type).append("'");
            }
        }
        return findNative("select account.id from " + getCompanyId() + ".crmaccount account " +
                innerJoins +
                "where account.deleted is not true and account.id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")" +
                " and " + innerWheres);
    }

    @Override
    public List<Integer> getCompanyCrmAccountIds(Integer companyID, int startat, int limit) {
        return findNative("select id from \"" + companyID + "\".crmaccount where deleted is not true and id > " + startat + " limit " + limit);
    }

    @Override
    public List<EdsCrmAccount> getCrmAccountsByIDs(List<Integer> ids) {
        return (List<EdsCrmAccount>) findNative("select distinct a.*, 0 as clazz_ from " + getCompanyId() + ".crmaccount a where deleted is not true and id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")", EdsCrmAccount.class);
    }

    @Override
    public List<EdsCrmAccount> getCrmAccountsByImportFileID(Integer entityID, int start, int limit) {
        return (List<EdsCrmAccount>) findLimited("select distinct account from EdsCrmAccount account where " + ServerUtils.checkForDeleted("account.deleted") + " and account.importFileID = " + entityID + " and account.objectID > " + start + " order by account.objectID asc ", limit);
    }

    @Override
    public Map<Integer, String> getMapIdAndName(List<Integer> crmAccountIDs, boolean isDeletedAlso) {
        Map<Integer, String> map = new HashMap<>();
        List<Object[]> result = findNative("select account.id, account.name from " + getCompanyId() + ".crmaccount account where " + (isDeletedAlso ? "" : " deleted is not true and ") + "id in (" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", ",") + ")");
        if (result != null && result.size() > 0) {
            for (Object[] values : result) {
                if (values != null && values.length > 1) {
                    if (values[0] != null && !"".equals(values[0]) && values[1] != null && !"".equals(values[1])) {
                        if (values[0] instanceof Integer && values[1] instanceof String) {
                            map.put((Integer) values[0], values[1].toString());
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getMapNumberAndName() {
        Map<String, Integer> map = new HashMap<>();
        List<Object[]> results = findNative("select account.number, account.number_integer from " + getCompanyId() + ".crmaccount account where account.deleted is not true");
        for (Object[] result : results) {
            if (result[0] != null && result[1] != null) {
                map.put(((String) result[0]).trim(), ((Integer) result[1]));
            }
        }
        return map;
    }

    @Override
    public List<Integer> getCrmAccountIDsByName(String accountName, boolean exactName, boolean withDeleteds) {
        if (accountName != null) {
            Map<String, String> name = new HashMap<>();
            accountName = accountName.toLowerCase().trim();
            if (!exactName) {
                accountName = "%" + accountName + "%";
            }
            name.put("accountName", accountName);
            return findNativeByNamedParams("select id from " + getCompanyId() + ".crmaccount where " + (withDeleteds ? "" : " deleted is not true and ") + " lower(name) like :accountName", name);
        }
        return null;
    }

    @Override
    public Integer getLastNumber(String format) {
        String prefix = null;
        if (format != null && !"".equals(format.trim())) {
            int splitterIndex = format.lastIndexOf("_");
            prefix = format.substring(0, splitterIndex);
        }
        StringBuilder sql = new StringBuilder("select numberInteger from EdsCrmAccount ");
        sql.append(" where " + ServerUtils.checkForDeleted("deleted") + " and numberInteger is not null");
        if (prefix != null) {
            sql.append(" and number like '" + prefix + "_%' ");
        }
        sql.append(" order by numberInteger desc");

        return (Integer) findSingle(sql.toString());
    }

    private String getAscOrDesc(ListingFilterParameter fp) {
        if (fp != null && fp.getSortDir() != null && fp.getSortDir().equals(Constants.DESC)) {
            return " desc ";
        }
        return " asc ";
    }

    @Override
    public List<Object[]> getAccountsByIndustry() {
        StringBuffer sql;
        sql = new StringBuffer();
        sql.append("\n" + "SELECT (select r.name from ").append(getCompanyId()).append(".reference as r where r.id=ca.industry) as an, (SELECT count(a.id) ").append("FROM ").append(getCompanyId()).append(".crmaccount as a WHERE a.industry=ca.industry and a.deleted is not true and a.industry is not null) as count \n").append("FROM ").append(getCompanyId()).append(".crmaccount  as ca where ca.deleted is not true and ca.industry is not null");
        sql.append(" group by ca.industry");
        sql.append(" limit 6");

        return findNative(sql.toString());
    }

    public List<Object[]> getListByAmount() {
        String companyId = getCompanyId();
        StringBuffer sql = null;
        sql = new StringBuffer();
        sql.append("SELECT a.name as name ,COALESCE((select o.amount from ").append(companyId).append(".opportunity as o where o.crmaccount=a.id and o.deleted is not true limit 1),0) as am \n").append("FROM ").append(companyId).append(".crmaccount as a\n").append("where a.deleted is not true ");
        sql.append(" order by am desc limit 6");
        return findNative(sql.toString());
    }

    public EdsCrmAccount getCrmAccountByName(String name, Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountName", name != null ? name.toLowerCase() : "");
        return (EdsCrmAccount) findSingleByNamedParams("select a from EdsCrmAccount a where " + (objectID != null ? "a.objectID <> " + objectID + " and " : "") + " lower(a.name) = :accountName and " + ServerUtils.checkForDeleted("a.deleted"), map);
    }

    @Override
    public EdsCrmAccount getCrmAccountByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name != null ? name.trim().toLowerCase() : "");
        return (EdsCrmAccount) findSingleByNamedParams("select a from EdsCrmAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and lower(trim(a.name)) = :name ", map);

    }

    @Override
    public EdsCrmAccount getCrmAccountByNumber(String number) {
        if (StringUtils.isBlank(number)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("number", StringUtils.isNotBlank(number) ? number.trim().toLowerCase() : "");
        return (EdsCrmAccount) findSingleByNamedParams("select a from EdsCrmAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and lower(trim(a.number)) = :number ", map);
    }

    @Override
    public EdsCrmAccount getCrmAccountByObjectKey(String objectKey) {
        if (StringUtils.isBlank(objectKey)) {
            return null;
        }
        return (EdsCrmAccount) findSingle("select a from EdsCrmAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and a.objectKey = ?", objectKey);
    }

    public ArrayList<EdsCrmAccount> getCrmAccountsByNames(String namesAsCommaSeparated) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountName", namesAsCommaSeparated != null ? namesAsCommaSeparated.toLowerCase() : "");
        return (ArrayList<EdsCrmAccount>) findByNamedParams("select a from EdsCrmAccount a where lower(a.name) in (:accountName) and " + ServerUtils.checkForDeleted("a.deleted"), map);
    }

    public Integer isAccountNameOrNumberAlreadyExists(String name, String number, Integer objectID) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountName", name != null ? name.toLowerCase() : "");
        map.put("accountNumber", number != null ? number.toLowerCase() : "");
        EdsCrmAccount account = (EdsCrmAccount) findSingleByNamedParams("select a from EdsCrmAccount a where " + (objectID != null ? "a.objectID <> " + objectID + " and " : "") + " (lower(a.name) = :accountName " + (map.containsKey("accountNumber") ? " or lower(a.number) = :accountNumber )" : ")") + " and " + ServerUtils.checkForDeleted("a.deleted"), map);
        if (account != null) {
            if (account.getName() != null && account.getName().equalsIgnoreCase(name)) {
                return -1;
            }
            if (account.getNumber() != null && account.getNumber().equalsIgnoreCase(number)) {
                return -2;
            }
        }
        return 0;
    }

    private String extractEmail(String email) {
        if (email.contains("<") && email.contains(">")) {
            email = email.substring(email.indexOf("<") + 1, email.indexOf(">"));
            if ((email.contains("<") && email.contains(">")) || email.contains(",") || email.contains("'")) {
                return extractEmail(email);
            } else {
                return email.toLowerCase();
            }
        }
        if (email.contains(",") || email.contains("'")) {
            email = email.replace(",", "");
            email = email.replace("'", "");
            return email.toLowerCase();
        }
        return email.toLowerCase();
    }

    @Override
    public Map<String, String[]> checkEmailExistence(String[] email) {
        StringBuilder emails = new StringBuilder();
        Map<String, String> params = new HashMap<>();
        if (email != null && email.length == 1 && !"".equals(email[0])) {
            if (email[0].matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                emails = new StringBuilder(" = " + "'" + extractEmail(email[0]) + "' ");
            } else {
                params.put("accountName", email[0]);
            }
        }
        if (email != null && email.length > 1) {
            String delimitr = "";
            boolean showIn = false;
            for (String email_ : email) {
                if (email_ != null && !"".equals(email_) && email_.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                    if (!showIn) {
                        emails = new StringBuilder(" in (");
                        showIn = true;
                    }
                    emails.append(delimitr).append("'").append(extractEmail(email_)).append("'");
                    delimitr = ",";
                } else if (email_ != null && !"".equals(email_) && !email_.matches(Constants.REGEX_EMAIL_SERVERSIDEONLY)) {
                    params.put("accountName", email_.toLowerCase());
                }
            }
            if (!"".contentEquals(emails)) {
                emails.append(")");
            }
        }
        if (!"".contentEquals(emails)) {
            emails.insert(0, (params.containsKey("accountName") ? "" : " and ") + " lower(crmAccount.email) ");
        }
        List<EdsCrmAccount> accounts = null;
        if (params.containsKey("accountName")) {
            accounts = findByNamedParams("select crmAccount from EdsCrmAccount crmAccount where " + ServerUtils.checkForDeleted("crmAccount.deleted") + " and (" + emails + (!"".contentEquals(emails) ? " or " : "") + " lower(name) = :accountName ) ", params);
        } else {
            accounts = find("select crmAccount from EdsCrmAccount crmAccount where " + ServerUtils.checkForDeleted("crmAccount.deleted") + emails);
        }
        Map<String, String[]> result = new HashMap<>();
        if (accounts.size() > 0) {
            List<String> emailList = Arrays.asList(email);
            for (EdsCrmAccount crmAccount : accounts) {
                if (emailList.contains(crmAccount.getEmail())) {
                    result.put(crmAccount.getEmail(), new String[]{crmAccount.getObjectID().toString(), "account"});
                }
                if (emailList.contains(crmAccount.getName())) {
                    result.put(crmAccount.getName(), new String[]{crmAccount.getObjectID().toString(), "account"});
                }
            }
        }
        return result;
    }

    public EdsCrmAccount getByPhone(String phone) {
        List<EdsCrmAccount> companies = getAllByPhone(phone);
        if (companies != null && companies.size() > 0) {
            return companies.get(0);
        }
        return null;
    }

    public List<EdsCrmAccount> getAllByPhone(String phone) {
        phone = StringUtils.replace(phone, "+", "");
        phone = StringUtils.replace(phone, ")", "");
        phone = StringUtils.replace(phone, "(", "");
        phone = StringUtils.replace(phone, "-", "");
        phone = StringUtils.replace(phone, "|", "");
        phone = StringUtils.replace(phone, " ", "");

        StringBuilder sql = new StringBuilder("SELECT company.* from " + getCompanyId() + ".crmAccount company " +
                " WHERE  replace(replace(replace(replace(replace(replace(company.phone, '+', ''), '|',''), ')',''), '(', ''),' ',''),'-','') LIKE  '%' || '" + phone + "' || '%'" +
                " and length(company.phone)<=" + (phone.length() + 5) +
                " ORDER BY length(company.phone)");

        List<EdsCrmAccount> companies = findNative(sql.toString(), EdsCrmAccount.class);
        return companies;
    }

    @Override
    public Integer getAccountsCountByImportFileID(Integer importFileID) {
        Long size = (Long) findSingle("select count(account) from EdsCrmAccount account where " + ServerUtils.checkForDeleted("account.deleted") + " and account.importFileID = " + importFileID);
        return size != null ? size.intValue() : 0;
    }

    @Override
    public EdsCrmAccount getCrmAccountByEmail(String from, Integer companyID) {
        if (from != null) {
            List<EdsCrmAccount> accounts = find("select crmAccount from EdsCrmAccount crmAccount where lower(crmAccount.email) = '" + from.toLowerCase().replace("'", "''") + "' and " + ServerUtils.checkForDeleted("crmAccount.deleted"));
            if (accounts != null && accounts.size() > 0) {
                return accounts.get(0);
            }
        }
        return null;
    }

    public void update(EdsCrmAccount obj, boolean solrUpdateAlso) {
        update(obj);
        solrUpdate(obj);
    }

    private void solrUpdate(EdsCrmAccount account) {
        try {
            if (!account.isDeleted()) {
//                solrManager.addCrmAccountToIndex(account);
                crmAccountSolrComponent.index(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(EdsCrmAccount obj) {
        if (obj.getSaasuGUID() == null) {
            obj.setSasuuLastUpdatedTime(new Date());
        }
        if (obj.getNumber() == null || "".equals(obj.getNumber())) {
            String accountType = CrmConstants.CRM_ACCOUNT;
            if (obj.getAccountTypes() != null && obj.getAccountTypes().size() > 0) {
                accountType = obj.isClient() ? CrmAccountItem.CUSTOMER : (obj.isSupplier() ? CrmAccountItem.SUPPLIER : obj.getAccountTypes().iterator().next().getName());
            }
            obj.setNumber(generateAccountNumber(accountType));
        }
        super.update(obj);
    }

    @Override
    public void create(EdsCrmAccount obj) {
        obj.setSasuuLastUpdatedTime(new Date());
        if (obj.getBillingAddress() == null) {
            obj.setBillingAddress(new EdsAddress());
        }
        if (obj.getMailingAddress() == null) {
            obj.setBillingAddress(new EdsAddress());
        }
        if (obj.getNumber() == null || "".equals(obj.getNumber())) {
            String accountType = CrmConstants.CRM_ACCOUNT;
            if (obj.getAccountTypes() != null && obj.getAccountTypes().size() > 0) {
                accountType = obj.isClient() ? CrmAccountItem.CUSTOMER : (obj.isSupplier() ? CrmAccountItem.SUPPLIER : obj.getAccountTypes().iterator().next().getName());
            }
            obj.setNumber(generateAccountNumber(accountType));
        }
        super.create(obj);
    }

    public String generateAccountNumber(String accountType) {
        return generateAccountNumber(accountType, null);
    }

    public String generateAccountNumber(String accountType, Integer intNumber) {
        if (accountType == null || "".equals(accountType) || CustomFormConstants.CRM_ACCOUNT.equals(accountType)) {
            accountType = CrmConstants.CRM_ACCOUNT;
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        NumberData numberData;
        String format = settings != null ? settings.getAccountFormat() : EdsNumberingSettings.DEF_ACCOUNT_PREFIX + "_" + EdsNumberingSettings.decimalFormat.toPattern();
        if (settings != null) {
            if (CrmAccountItem.CUSTOMER.equals(accountType)) {
                format = settings.getClientFormat();
            } else if (CrmAccountItem.SUPPLIER.equals(accountType)) {
                format = settings.getSupplierFormat();
            }
        }
        if (settings != null && StringUtils.isNotBlank(format)) {
            numberData = settings.parseNumberData(intNumber == null ? getLastNumber(format) : intNumber, format);
        } else {
            format = CrmAccountItem.CUSTOMER.equals(accountType) ? EdsNumberingSettings.DEF_CUSTOMER_PREFIX : (CrmAccountItem.SUPPLIER.equals(accountType) ? EdsNumberingSettings.DEF_SUPPLIER_PREFIX : (accountType.equals(CrmConstants.CRM_ACCOUNT) ? EdsNumberingSettings.DEF_ACCOUNT_PREFIX : accountType.substring(0, 3).toUpperCase() + "_"));
            numberData = EdsNumberingSettings.getDefaultData(intNumber == null ? getLastNumber(format) : intNumber, format);
        }
        return numberData.getNumberString();
    }

    @Override
    public Set<String> getDuplicateNamesSet(List<Integer> idsFromSolrDocument, List<Integer> idsOfAccountDetectingForDuplicates) {
        Set<String> result = new HashSet<>();
        String idsFromSolrDocumentString = idsFromSolrDocument != null && idsFromSolrDocument.size() > 0 ? " id in (" + ServerUtils.getAsCommoDelimited(idsFromSolrDocument, "0", ",") + ") and " : "";
        String idsOfAccountDetectingForDuplicatesString = idsOfAccountDetectingForDuplicates != null && idsOfAccountDetectingForDuplicates.size() > 0 ? " a.name in (" + "select distinct innerA.name from " + getCompanyId() + ".crmaccount innerA where innerA.deleted is not true and id in (" + ServerUtils.getAsCommoDelimited(idsOfAccountDetectingForDuplicates, "0", ",") + ")) and " : "";
        List<Object[]> queryResult = findNative("select distinct a.name, count(a.id) as counter from " + getCompanyId() + ".crmaccount a where " + idsFromSolrDocumentString + idsOfAccountDetectingForDuplicatesString + " a.deleted is not true and a.name is not null group by a.name");
        if (queryResult != null && queryResult.size() > 0) {
            for (Object[] row : queryResult) {
                result.add((String) row[0]);
            }
        }
        return result;
    }

    @Override
    public EdsCrmAccount getAccountByOwner(Integer ownerId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT account FROM EdsCrmAccount account WHERE account.owner.objectID=" + ownerId + " ORDER BY account.objectID");
        return (EdsCrmAccount) findSingle(sql.toString());
    }

    @Override
    public BigDecimal getClientBalance(Integer crmAccountID, boolean... isBaseCurrency) {
        boolean baseCurrency = true;//isBaseCurrency == null || isBaseCurrency.length <= 0 || isBaseCurrency[0]; TODO will back to correction
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(baseCurrency ? "SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) " : "SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) ");
        sql.append("FROM \"").append(schema).append("\".transactionItem ti ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN \"").append(schema).append("\".account a ON a.id=ti.accountid ");
        sql.append("WHERE t.deleted<>true and (t.clientid = '").append(crmAccountID).append("' or ti.crmaccount_id = '").append(crmAccountID).append("') ");
//        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey = " + EdsAccount.ACCOUNTS_RECEIVABLE + ") ");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) ");
        BigDecimal balance = (BigDecimal) findNativeSingle(sql.toString());
        System.out.println("[" + sql + "]");
        return balance != null ? balance : BigDecimal.ZERO;
    }

    @Override
    public Map<Integer, BigDecimal> getClientBalanceByCustomerIds(String crmAccountIDs) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce(t.clientid,ti.crmaccount_id), ");
        sql.append("SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) ");
        sql.append("FROM \"").append(schema).append("\".transactionItem ti ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN \"").append(schema).append("\".account a ON a.id=ti.accountid ");
        sql.append("WHERE t.deleted<>true and (t.clientid in (").append(crmAccountIDs).append(") or ti.crmaccount_id in (").append(crmAccountIDs).append(")) ");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) ");
        sql.append(" group by coalesce(t.clientid,ti.crmaccount_id) ");
        List<Object[]> balance = findNative(sql.toString());
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] objects : balance) {
            result.put((Integer) objects[0], (BigDecimal) objects[1]);
        }
        System.out.println("[" + sql + "]");
        return result;
    }

    @Override
    public BigDecimal getSupplierBalance(Integer crmAccountID, boolean... isBaseCurrency) {
        boolean baseCurrency = true;//isBaseCurrency == null || isBaseCurrency.length <= 0 || isBaseCurrency[0]; TODO will back to correction
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(baseCurrency ? "SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) " : "SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) ");
        sql.append("FROM \"").append(schema).append("\".transactionItem ti ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN \"").append(schema).append("\".account a ON a.id=ti.accountid ");
        sql.append("WHERE t.deleted<>true and (t.supplierid = '").append(crmAccountID).append("' or ti.crmaccount_id = '").append(crmAccountID).append("') ");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ");
        BigDecimal balance = (BigDecimal) findNativeSingle(sql.toString());
        return balance != null ? balance : BigDecimal.ZERO;
    }


    @Override
    public Map<Integer, BigDecimal> getSupplierBalanceWithMap(String crmAccountIds, boolean... isBaseCurrency) {
        boolean baseCurrency = true;//isBaseCurrency == null || isBaseCurrency.length <= 0 || isBaseCurrency[0]; TODO will back to correction
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce(t.supplierid,ti.crmaccount_id), ");
        sql.append(baseCurrency ? "SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) " : "SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) ");
        sql.append("FROM \"").append(schema).append("\".transactionItem ti ");
        sql.append("INNER JOIN \"").append(schema).append("\".transaction t ON t.id = ti.transactionid ");
        sql.append("INNER JOIN \"").append(schema).append("\".account a ON a.id=ti.accountid ");
        sql.append("WHERE t.deleted<>true and (t.supplierid in (").append(crmAccountIds).append(") or ti.crmaccount_id in (").append(crmAccountIds).append(")) ");
        sql.append("AND (a.key in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") or a.groupKey in (" + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ");
        sql.append(" group by coalesce(t.supplierid,ti.crmaccount_id) ");
        List<Object[]> balance = findNative(sql.toString());
        Map<Integer, BigDecimal> result = new LinkedHashMap<>();
        for (Object[] objects : balance) {
            result.put((Integer) objects[0], (BigDecimal) objects[1]);
        }
        return result;
    }

    @Override
    public EdsCrmAccount getAccountBySaasuUID(String saasuUID) {
        return (EdsCrmAccount) findSingle("SELECT account FROM EdsCrmAccount account WHERE account.saasuGUID='" + saasuUID + "' AND " + ServerUtils.checkForDeleted("account.deleted") + " ORDER BY account.objectID");
    }

    @Override
    public List<EdsCrmAccount> getAccountTreeLookUpItems(ListingFilterParameter fp, String treeLevel) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ca FROM EdsCrmAccount ca ");

        if (TREE_LEVEL_3.equals(treeLevel)) {
            sql.append(" join ca.parent p WHERE p.parent is not null ");
        } else if (TREE_LEVEL_2.equals(treeLevel)) {
            sql.append(" join ca.parent p WHERE p.parent is null ");
        } else if (TREE_LEVEL_1.equals(treeLevel)) {
            sql.append(" WHERE ca.parent is null ");
        }

        sql.append(" AND ca.deleted is not true ");

        if (fp.getSqlSearchKey() != null) {
            if (fp.isLookUp()) {
                sql.append("  AND lower(ca.name) like '").append(fp.getSearchKey()).append("%' ");
            }
        }

        if (fp.getParentID() != null) {
            sql.append(" AND ca.parent.objectID = '").append(fp.getParentID()).append("' ");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public EdsCrmAccount findCustomerByRegistrationNum(String registrationNumber) {
        return (EdsCrmAccount) findSingle("SELECT ca FROM EdsCrmAccount ca WHERE lower(ca.registrationNumber)='" + registrationNumber.toLowerCase() + "'");
    }

    @Override
    public List<String> getCrmAccountNumberById(Integer clientId) {
        return find("select p.number from EdsCrmAccount p where p.objectID = ?", clientId);
    }

    @Override
    public EdsCrmAccount getCrmAccountBySubsidiary(Integer externalCompanyID, String crmAccountType) {
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ca.id, ca.* from ").append(companyID).append(".crmaccount ca ");
        sql.append("INNER JOIN ").append(companyID).append(".subsidiariesCompany sc on sc.id=ca.subsidiary ");
        sql.append("LEFT JOIN ").append(companyID).append(".crmAccount_types cat on cat.crmaccount_id=ca.id ");
        sql.append("LEFT JOIN ").append(companyID).append(".reference ref on ref.id=cat.type_id ");
        sql.append("WHERE sc.companyId=").append(externalCompanyID.toString()).append(crmAccountType != null ? " and ref.code = '" + crmAccountType + "' " : "").append(" and ").append(ServerUtils.checkForDeleted("ca.deleted"));
        return (EdsCrmAccount) findNativeSingle(sql.toString(), EdsCrmAccount.class);
    }

    @Override
    public List<EdsCrmAccount> getAllSubAccounts(EdsCrmAccount account, boolean recursively) {
        List<EdsCrmAccount> result = new ArrayList<>();
        List<EdsCrmAccount> subs = find("select a from EdsCrmAccount a where " + ServerUtils.checkForDeleted("a.deleted") + " and a.parent.objectID = " + account.getObjectID());
        if (subs != null && subs.size() > 0) {
            result.addAll(subs);
            for (EdsCrmAccount sub : subs) {
                result.addAll(getAllSubAccounts(sub, recursively));
            }
        }
        return result;
    }

    @Override
    public Object getCustomFieldValue(Integer objectID, String fieldCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT cf.").append(fieldCode).append(" FROM ").append(getCompanyId()).append(".crmaccount c ");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmcustomfields cf ON cf.id = c.customfields_id ");
        sql.append("WHERE c.id = ").append(objectID);
        return findNativeSingle(sql.toString());
    }

    public List<EdsCrmAccount> getAccountByCompanyId(Integer companyId) {
        return find("select account from EdsCrmAccount account where account.signupCompanyId = " + companyId);
    }

    public EdsCrmAccount getAccountByMagentoId(Integer magentoId) {
        return (EdsCrmAccount) findSingle("SELECT account FROM EdsCrmAccount account WHERE " + ServerUtils.checkForDeleted("account.deleted") + " AND account.magentoEntityId = " + magentoId);
    }

    @Override
    public void updateCrmAccountsByAccountType(Integer accountID, boolean isReceivable) {
        if (isReceivable) {
            update("update EdsCrmAccount ca set ca.receivable = null WHERE " + ServerUtils.checkForDeleted("ca.deleted") + " AND ca.receivable.objectID = " + accountID);
        } else {
            update("update EdsCrmAccount ca set ca.payable = null WHERE " + ServerUtils.checkForDeleted("ca.deleted") + " AND ca.payable.objectID = " + accountID);
        }
    }

    @Override
    public EdsCrmAccount getSignupLeadByCompanyId(Integer companyId) {
        return (EdsCrmAccount) findSingle("select account from EdsCrmAccount account " +
                " where " + ServerUtils.checkForDeleted("account.deleted") +
                "     and account.signupCompanyId =? order by account.objectID ", companyId);
    }

    @Override
    public List<Integer> getAccountIDsByOwner(Integer ownerID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select crmaccount_id from ").append(getCompanyId()).append(".crmaccount_owners where owner_id = ").append(ownerID);
        return findNative(sql.toString());
    }

    @Override
    public NumberData generateAccountNumberData(String accountType) {
        return generateAccountNumberData(accountType, null);
    }

    @Override
    public NumberData generateAccountNumberData(String accountType, Integer intNumber) {
        if (accountType == null || "".equals(accountType) || CustomFormConstants.CRM_ACCOUNT.equals(accountType)) {
            accountType = CrmConstants.CRM_ACCOUNT;
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        NumberData numberData;
        String format = settings != null ? settings.getAccountFormat() : EdsNumberingSettings.DEF_ACCOUNT_PREFIX + "_" + EdsNumberingSettings.decimalFormat.toPattern();
        if (settings != null) {
            if (CrmAccountItem.CUSTOMER.equals(accountType)) {
                format = settings.getClientFormat();
            } else if (CrmAccountItem.SUPPLIER.equals(accountType)) {
                format = settings.getSupplierFormat();
            }
        }
        if (settings != null && StringUtils.isNotBlank(format)) {
            numberData = settings.parseNumberData(intNumber == null ? getLastNumber(format) : intNumber, format);
        } else {
            format = CrmAccountItem.CUSTOMER.equals(accountType) ? EdsNumberingSettings.DEF_CUSTOMER_PREFIX : (CrmAccountItem.SUPPLIER.equals(accountType) ? EdsNumberingSettings.DEF_SUPPLIER_PREFIX : (accountType.equals(CrmConstants.CRM_ACCOUNT) ? EdsNumberingSettings.DEF_ACCOUNT_PREFIX : accountType.substring(0, 3).toUpperCase() + "_"));
            numberData = EdsNumberingSettings.getDefaultData(intNumber == null ? getLastNumber(format) : intNumber, format);
        }
        return numberData;
    }

    @Override
    public Integer getCustomerExportInvoiceCount(Integer customerId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select count(*) from ").append(getCompanyId()).append(".crmAccount c ");
        queryBuilder.append("left join ").append(getCompanyId()).append(".reference r on c.tax_treatment = r.id ");
        queryBuilder.append("where c.id =  ").append(customerId).append(" and r.code in ('GCC_VAT_REGISTERED','GCC_NON_VAT_REGISTERED','NON_GCC') ");

        BigInteger bigInteger = (BigInteger) findNativeSingle(queryBuilder.toString());
        return bigInteger.intValue();
    }

    @Override
    public EdsCrmAccount getByEntityId(Integer entityId) {
        String query = """
                select ca from EdsCrmAccount ca
                where ca.entityID = ?
                """;
        return (EdsCrmAccount) findSingle(query, entityId);
    }
}
