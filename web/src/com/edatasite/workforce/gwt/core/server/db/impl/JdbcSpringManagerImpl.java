package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogList;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagerItemList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.utils.GenerateSqlQuery;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23-Oct-2010
 * Time: 14:15:47
 * To change this template use File | Settings | File Templates.
 */
@Repository("jdbcSpringManager")
public class JdbcSpringManagerImpl extends NamedParameterJdbcTemplate implements JdbcSpringManager, Constants {

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CompanyManager companyManager;

    @Autowired
    public JdbcSpringManagerImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public AccountManagerItemList findOverallInActiveUsers(ListingFilterParameter fp) {
        List<AccountManagementListItem> users;
        StringBuilder sql = new StringBuilder();
        Map parameters = new HashMap();

        if (fp.getStatusCode() == null) {
            fp.setStatusCode(EMPLOYEE_STATUS_ACTIVE);
        }
        if (fp.getCompanyID() != null) {
            String compnayId = "\"" + fp.getCompanyID() + "\"";
            sql.append(" SELECT co.id as companyID, co.name as companyName, to_char(co.registrationDate, 'yyyy-mm-dd') as signUpDate,  ");
            sql.append("u.id as userId, coalesce(u.firstname, '') || ' ' || coalesce(u.lastname, '') as name, u.email as email, ");
            sql.append(" re.code employeeStatus, array_to_string(array_agg(r.name), ',') as roles ");
            sql.append(" FROM ").append(compnayId).append(".myuser u");
            sql.append(" JOIN \"public\".company co on co.id=").append(fp.getCompanyID());
            sql.append(" LEFT JOIN ").append(compnayId).append(".reference re on re.id = u.accountstatusid ");
            sql.append(" LEFT JOIN ").append(compnayId).append(".myuser_role roles on roles.users_id = u.id ");
            sql.append(" LEFT JOIN ").append(compnayId).append(".role r on r.id = roles.roles_id ");
            sql.append(" WHERE u.deleted is not true and re.code='").append(fp.getStatusCode()).append("' ");

            if (fp.getSqlSearchKey() != null) {
                parameters.put("keyword", fp.getSqlSearchKey());
                sql.append(" and (lower(u.userName) like lower(:keyword) or lower(u.firstname||u.lastname) like  lower(:keyword)")
                        .append("or EXISTS (" )
                        .append("        SELECT 1" )
                        .append("        FROM ").append(compnayId).append(".myuser_role mr")
                        .append("        JOIN ").append(compnayId).append(".role r ON r.id = mr.roles_id")
                        .append("        WHERE mr.users_id = u.id" )
                        .append("            AND LOWER(r.name) LIKE lower(:keyword)" )
                        .append("    )) ");
            }

            sql.append(" group by co.id, co.name, re.code, u.id, u.firstname, u.lastname, u.email");
        } else {
            List<String> existingCompanyList = companyManager.getExistingSchemas();
            for (EdsCompany company : getSchemaNameList()) {
                if (!company.getTestCompany() && existingCompanyList.contains(String.valueOf(company.getObjectID()))) {
                    String compnayId = "\"" + company.getObjectID() + "\"";

                    if (!"".contentEquals(sql)) {
                        sql.append(" UNION ALL");
                        sql.append(" \n");
                    }

                    sql.append(" (SELECT co.id as companyID, co.name as companyName, to_char(registrationDate, 'yyyy-mm-dd') as signUpDate, ");
                    sql.append(" u.id as userId, coalesce(u.firstname, '') || ' ' || coalesce(u.lastname, '') as name, u.email as email, ");
                    sql.append(" re.code employeeStatus, array_to_string(array_agg(r.name), ',') as roles ");
                    sql.append(" FROM ").append(compnayId).append(".myuser u");
                    sql.append(" join \"public\".company co on co.id=").append(compnayId);
                    sql.append(" LEFT JOIN ").append(compnayId).append(".reference re on re.id = u.accountstatusid ");
                    sql.append(" LEFT JOIN ").append(compnayId).append(".myuser_role roles on roles.users_id = u.id ");
                    sql.append(" LEFT JOIN ").append(compnayId).append(".role r on r.id = roles.roles_id ");
                    sql.append(" WHERE u.deleted is not true and re.code='").append(fp.getStatusCode()).append("' ");

                    if (fp.getSqlSearchKey() != null) {
                        parameters.put("keyword", fp.getSqlSearchKey());
                        sql.append(" and (lower(u.userName) like lower(:keyword) or lower(u.firstname||u.lastname) like  lower(:keyword)")
                                .append("or EXISTS (" )
                                .append("        SELECT 1" )
                                .append("        FROM ").append(compnayId).append(".myuser_role mr")
                                .append("        JOIN ").append(compnayId).append(".role r ON r.id = mr.roles_id")
                                .append("        WHERE mr.users_id = u.id" )
                                .append("            AND LOWER(r.name) LIKE lower(:keyword)" )
                                .append("    )) ");
                    }

                    sql.append(" group by co.id, co.name, re.code, u.id, u.firstname, u.lastname, u.email");

                    sql.append(")");
                }
            }
        }


        if (fp.getSortField() != null) {
            if ("companyID".equals(fp.getSortField())) {
                sql.append(" order by companyID").append(fp.getSortDir() == 2 ? " desc" : "");
            } else if ("account".equals(fp.getSortField())) {
                sql.append(" order by name").append(fp.getSortDir() == 2 ? " desc" : "");
            } else if ("companyName".equals(fp.getSortField())) {
                sql.append(" order by companyName").append(fp.getSortDir() == 2 ? " desc" : "");
            } else if ("date".equals(fp.getSortField())) {
                sql.append(" order by signUpDate").append(fp.getSortDir() == 2 ? " desc" : "");
            } else {
                sql.append(" order by companyID desc");
            }
        } else {
            sql.append(" order by companyID ");
        }

        String countSqlQuery = "SELECT count(companyID) FROM (" + sql + ") t";

        sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());

        Integer totalCount = 0;
        if (fp.getSqlSearchKey() != null) {
            users = query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(AccountManagementListItem.class));
            totalCount = getSimpleJdbcTemplate().queryForObject(countSqlQuery, parameters, Integer.class);
        } else {
            users = getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(AccountManagementListItem.class));
            totalCount = getJdbcOperations().queryForObject(countSqlQuery, Integer.class);
        }

        AccountManagerItemList list = new AccountManagerItemList();
        list.setTotalCount(totalCount);
        list.setListItems(users.toArray(new AccountManagementListItem[]{}));

        return list;
    }

    @Override
    public AccessLogList getAccessLogSection(ListingFilterParameter fp) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        List<String> schemaList = new ArrayList<>();
        try {
            EdsSchemaUpdater.setDbUrl(WfmJpaTemplate.getDataBaseURL());
            schemaList = EdsSchemaUpdater.getSchemaList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (EdsCompany company : getSchemaNameList()) {
            if (company.getObjectID() == 1 || company.getObjectID() == 3737 && company.getTestCompany() || !company.hasSchema(schemaList)) {
                continue;//not shown 1 == Finnet Technologies Pvt. Ltd. and 3737 == FinnetLimited company
            }
            Integer object = company.getObjectID();
            if (!"".contentEquals(sql)) {
                sql.append(" UNION ALL ");
            }
            sql.append(" (select ms.id as objectID, co.name as companyName, mu.firstname || ' ' || mu.lastname as userName, uc.email, ms.lastaccesstime as lastAccessDate, ms.useragent as browserType, ms.ipaddress as clientIpAddress, co.id as companyid ");
            sql.append(" from \"").append(object).append("\".myuser mu ");
            sql.append(" LEFT OUTER JOIN \"public\".usercompany uc on uc.userid = mu.id and uc.companyid=").append(object);
            sql.append(" LEFT OUTER JOIN \"public\".company co on co.id = uc.companyid and co.id=").append(object);
            sql.append(" LEFT OUTER JOIN userAuth ua on ua.id = uc.authID");
            sql.append(" LEFT OUTER JOIN \"").append(object).append("\".myusersession ms on mu.id = ms.userid where ms.useragent is not null ");
            sql.append(" and (ua.username not like '%workforcetrack.com%' and "); // not shown workforcetrack.com domain
            sql.append(" ua.username not like '%finnetlimited.com%' and ");   // not shown finnetlimited.com domain
            sql.append(" ua.username not like '%edatasite.com%' and "); // not shown edatasite.com domain
            sql.append(" ua.username not like '%globalcustodian.com%' and  "); // not shown globalcustodian.com domain
            sql.append(" ua.username not like '%venommedia.biz%' and "); // not shown wvenommedia.biz domain
            sql.append(" ua.username not like '%atmonitor.co.uk%') and"); // not shown atmonitor.co.uk domain
            sql.append(" ms.lastaccesstime >= '").append(ServerUtils.getLastOneDate()).append("' "); // and last one year updates shown
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and (lower(co.name) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" or lower(mu.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" or lower(mu.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" or lower(uc.email) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" or lower(ms.useragent) like '").append(fp.getSqlSearchKey()).append("')");
            }
            sql.append(" group by objectID, companyName, mu.firstname, mu.lastname, uc.email, lastAccessDate, browserType, clientIpAddress,co.id");
            if (AccessLogListItem.COMPANY_ID.equals(fp.getSortField())) {
                sql.append(" order by co.id ");
            } else {
                sql.append(" order by lastAccessDate ");
            }
            if (fp.isAscending()) {
                sql.append(" ASC )");
            } else {
                sql.append(" DESC )");
            }
        }

        String totalQuery = " SELECT count(objectID) FROM (" + sql + ") t ";
        if (fp != null) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        Integer totalCount = getJdbcOperations().queryForObject(totalQuery, Integer.class);
        List<AccessLogListItem> accessLogs = getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(AccessLogListItem.class));

        return new AccessLogList(totalCount, accessLogs.toArray(new AccessLogListItem[]{}));
    }

    @Override
    public Long getEmployeesCountByDateLimit(final Timestamp sdate, final Timestamp edate) {
        StringBuffer sql = null;
        sql = new StringBuffer();
        Map parameters = new HashMap() {{
            put("startDate", sdate);
            put("endDate", edate);
        }};
        sql.append("select sum(emp.count) from (");
        boolean hasBegin = false;
        for (EdsCompany company : getSchemaNameList()) {
            if (!company.getTestCompany()) {
                Integer object = company.getObjectID();
                if (hasBegin) {
                    sql.append(" UNION ALL ");
                }

                hasBegin = true;

                sql.append(" (select count(e.*) from \"").append(object).append("\".employee e ");
                sql.append(" left outer join \"public\".usercompany uc on uc.userid = e.id and  uc.companyid=" + object);
                sql.append(" left outer join \"public\".company c on c.id = uc.companyid");
                sql.append(" where e.creationTime >=:startDate and e.creationTime <=:endDate )");
            }
        }
        sql.append(") as emp");
        return getSimpleJdbcTemplate().queryForObject(sql.toString(), parameters, Long.class);
    }

    @Override
    public List<EdsCompany> getSchemaNameList() {
        return getSimpleJdbcTemplate().query("SELECT c.id as objectID, c.name, c.testCompany, c.registrationDate FROM company as c where c.isFree=false order by c.id desc", BeanPropertyRowMapper.newInstance(EdsCompany.class));
    }

    @Override
    public String generateSqlQuery(final Integer companyId) {
        StringBuilder sql = new StringBuilder();

        Map parameters = new HashMap() {{
            put("companyId", companyId);
        }};

        List<String> rs = null;
        Integer csId = null;
        try {
            csId = getSimpleJdbcTemplate().queryForObject("SELECT companysettingsid FROM company where id=:companyId", parameters, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if (csId != null) {
            sql.append("\n--COMPANY for companySettings= ").append(csId).append("\n");
            rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getCompanySettingsInsert(csId), new QueryMapper());
            for (String financialsettings : rs) {
                String tmp = financialsettings + ";\n";
                tmp = tmp.replace("1999-01-01 00:00:00", "null");
                tmp = tmp.replace(",,", ",null,");
                tmp = tmp.replace(",'0'", ",null");
                tmp = tmp.replace(",'null'", ",null");
                sql.append(tmp);
            }
        }

        sql.append("\n--COMPANY for companyid= " + companyId + "\n");
        rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getCompanyForInsert(companyId), new QueryMapper());
        for (String company : rs) {
            String tmp = company + ";\n";
            tmp = tmp.replace("1999-01-01 00:00:00", "null");
            tmp = tmp.replace(",,", ",null,");
            tmp = tmp.replace(",'0'", ",null");
            tmp = tmp.replace(",'null'", ",null");
            sql.append(tmp);
        }

        sql.append("\n--CompanySystemSettings for companyid= ").append(companyId).append("\n");
        rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getCompanySystemSettings(companyId), new QueryMapper());
        for (String company : rs) {
            String tmp = company + ";\n";
            tmp = tmp.replace("1999-01-01 00:00:00", "null");
            tmp = tmp.replace(",,", ",null,");
            tmp = tmp.replace(",'0'", ",null");
            tmp = tmp.replace(",'null'", ",null");
            sql.append(tmp);
        }

        sql.append("\n --Wfp_website for companyid= ").append(companyId).append("\n");
        rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getCompanyWebsites(companyId), new QueryMapper());
        for (String company : rs) {
            String tmp = company + ";\n";
            tmp = tmp.replace("1999-01-01 00:00:00", "null");
            tmp = tmp.replace(",,", ",null,");
            tmp = tmp.replace(",'0'", ",null");
            tmp = tmp.replace(",'null'", ",null");
            sql.append(tmp);
        }

        sql.append("\n--USAGEPLAN for companyid= ").append(companyId).append("\n");
        rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getUsagePlan(companyId), new QueryMapper());
        for (String company : rs) {
            String tmp = company + ";\n";
            tmp = tmp.replace("1999-01-01 00:00:00", "null");
            tmp = tmp.replace(",,", ",null,");
            tmp = tmp.replace(",'0'", ",null");
            tmp = tmp.replace(",'null'", ",null");
            sql.append(tmp);
        }

        sql.append("\n--CompanyStatistic for companyid= ").append(companyId).append("\n");
        rs = getSimpleJdbcTemplate().query(GenerateSqlQuery.getCompanyStatistic(companyId), new QueryMapper());
        for (String company : rs) {
            String tmp = company + ";\n";
            tmp = tmp.replace("1999-01-01 00:00:00", "null");
            tmp = tmp.replace(",,", ",null,");
            tmp = tmp.replace(",'0'", ",null");
            tmp = tmp.replace(",'null'", ",null");
            sql.append(tmp);
        }

        sql.append("\n--USERAUTH for companyid= ").append(companyId).append("\n");
        for (Map<String, Object> userAuth : globalAuthJdbcSpringManager.getCompanyAuthList(companyId)) {
            String tmp = "SELECT saveUserAuthenticationData ('" + userAuth.get("username") + "','" + userAuth.get("password") + "','" + userAuth.get("domainname") + "','" + userAuth.get("email") + "'," + userAuth.get("userid") + "," + companyId + ");\n";
            sql.append(tmp);
        }

        return sql.toString();
    }

    @Override
    public void deleteCompanyAndSchema(Integer companyID) {
        getJdbcOperations().queryForMap("SELECT deleteCompanyFunc3('" + companyID + "')");
    }

    @Override
    public JdbcSpringManagerImpl getSimpleJdbcTemplate() {
        return this;
    }

    @Override
    public JdbcOperations getSimJdbcOperations() {
        return getJdbcOperations();
    }

    @Override
    public Map<String, Object> getUploadAmazonSettingsList(Integer uploadId) {
        return getJdbcOperations().queryForMap("select u.accessKey, u.id from \"" + ServerSecurityContext.getInstance().getCompanyId() + "\".uploadamazonsettings as u where u.uploadid=" + uploadId + "");
    }

    @Override
    public Map<String, Object> getUploadMinIOSettingsList(Integer uploadId) {
        return getJdbcOperations().queryForMap("select u.accessKey, u.id from \"" + ServerSecurityContext.getInstance().getCompanyId() + "\".uploadminiosettings as u where u.uploadid=" + uploadId + "");
    }

    @Override
    public List<Integer> getFileRbacEntries(Integer fileObjectID) {
        List<Integer> idList = new ArrayList<>();
        List<Map<String, Object>> idMapList = getJdbcOperations().queryForList("SELECT fr.id FROM \"" + ServerSecurityContext.getInstance().getCompanyId() + "\".folderrbac as fr WHERE fr.fileHeader_id = " + fileObjectID + " and fr.file=true");
        for (Map<String, Object> map : idMapList) {
            idList.add((Integer) map.get("id"));
        }
        return idList;
    }

    @Override
    public List<Integer> getFolderRbacEntries(Integer folderObjectID) {
        List<Integer> idList = new ArrayList<>();
        List<Map<String, Object>> idMapList = getJdbcOperations().queryForList("SELECT fr.id FROM \"" + ServerSecurityContext.getInstance().getCompanyId() + "\".folderrbac as fr WHERE fr.folder_id = " + folderObjectID + " and fr.file<>true");
        for (Map<String, Object> map : idMapList) {
            idList.add((Integer) map.get("id"));
        }
        return idList;
    }

    static class QueryMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    }

    @Override
    public List<EdsCompany> getCompanyList() {
        return getSimpleJdbcTemplate().query("SELECT c.id as objectID FROM company as c order by c.id desc", BeanPropertyRowMapper.newInstance(EdsCompany.class));
    }

    @Override
    public String getTemplateSchema(String pattern) {
        return getJdbcOperations().queryForObject("SELECT t.schema_name FROM (SELECT schema_name, regexp_replace(schema_name, E'\\\\D','','g') as number from information_schema.schemata where schema_name like '"+ pattern + "%') t ORDER BY t.number::int LIMIT 1", String.class);
    }

    @Override
    public List<String> getExistingTemplateSchemas(String pattern) {
        return getJdbcOperations().queryForList("SELECT t.number FROM (SELECT regexp_replace(schema_name, E'\\\\D','','g') as number from information_schema.schemata where schema_name like '" + pattern + "%') t ORDER BY t.number::int DESC ", String.class);
    }

}
