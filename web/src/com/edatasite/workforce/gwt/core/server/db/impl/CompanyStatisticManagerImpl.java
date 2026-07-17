package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanyStatistic;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyStatisticManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository("companyStatisticManager")
public class CompanyStatisticManagerImpl extends BaseManager<EdsCompanyStatistic> implements
        CompanyStatisticManager, Constants {

    @Autowired
    private CompanyManager companyManager;

    @Autowired
    private JdbcSpringManager jdbcSpringManager;


    public CompanyStatisticManagerImpl() {
        super(EdsCompanyStatistic.class);
    }

    public List<EdsCompanyStatistic> getAllCompanyStatistic() {
        return find("select companystatistic from EdsCompanyStatistic companystatistic order by companystatistic.registrationDate desc");
    }

    public List<Object[]> getCompanyStatistic() {
        List<Object[]> arrayList = new ArrayList<>();
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        List<String> existingCompanyList = companyManager.getExistingSchemas();
        if (existingCompanyList.contains(companyId)) {
            String queryStr = "select \n" +
                    "\t t20.companyId,t20.companyName,t20.countryName, t20.industryName,\n" +
                    "\t (select mu.firstname || ' ' || mu.lastname || ' ' || '[' || email || ']' from " + getCompanyId() + ".myuser mu order by id limit 1) as contactPerson,\n" +
                    "\t t20.phone,t20.signedUpPage,css.companysignedupfrom as companySignedUpFrom,\n" +
                    "\t css.googleAppSection as googleAppSection, css.host as host, t20.SignupCompIP as SignupCompIP,t20.active as Active,\n" +
                    "\t (select count(id)  from " + getCompanyId() + ".myusersession) accesscount,\n" +
                    "\t (select min(signiinDate)  from " + getCompanyId() + ".myusersession) firstaccess,\n" +
                    "\t (select max(signiinDate)  from " + getCompanyId() + ".myusersession) lastaccess,\n" +
                    "\t t20.registrationDate as registrationDate,\n" +
                    "\t (select count(mu.id) from " + getCompanyId() + ".myuser mu " +
                    "\t join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid where re.code in ('" + EMPLOYEE_STATUS_ACTIVE + "','" + EMPLOYEE_STATUS_PENDING + "','" + EMPLOYEE_STATUS_NO_ACCCESS + "') " +
                    "\t and mu.deleted is not true) as usercount,\n" +
                    "\t (select count(mu.id) from " + getCompanyId() + ".myuser mu " +
                    "\t join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid where re.code in ('" + EMPLOYEE_STATUS_ACTIVE + "','" + EMPLOYEE_STATUS_PENDING + "') " +
                    "\t and mu.deleted is not true ) as activeusercount,\n" +
                    "\t (select count(p.id) from " + getCompanyId() + ".project p where isdeleted is not true) as projectcount,\n" +
                    "\t (select count(t.id) from " + getCompanyId() + ".task t where deleted is not true) as taskcount,\n" +
                    "\t (select count(t.id) from " + getCompanyId() + ".timesheet t) as timesheetcount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".crmaccount c " +
                    "\t inner join " + getCompanyId() + ".crmaccount_types types on types.crmaccount_id = c.id where types.type_id = " +
                    "\t (select id " + EdsReference.getSql(EdsCrmAccount.CUSTOMER, EdsCrmAccount._CRM_ACCOUNT_TYPE, false) + ")" +
                    "\t ) asclientcount,\n" +
                    "\t (select count(s.id) from " + getCompanyId() + ".crmaccount s " +
                    "\t inner join " + getCompanyId() + ".crmaccount_types types on types.crmaccount_id = s.id where types.type_id = " +
                    "\t (select id " + EdsReference.getSql(EdsCrmAccount.SUPPLIER, EdsCrmAccount._CRM_ACCOUNT_TYPE, false) + ")" +
                    "\t ) as suppliercount,\n" +
                    "\t (select count(l.id) from " + getCompanyId() + ".crmcontact l where deleted is not true and contacttype = 5) as leadcount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".crmcontact c where deleted is not true and contacttype <> 5) as contactcount,\n" +
                    "\t (0) as crmtaskcount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".event c where deleted is not true) as eventcount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".crmcase c where deleted is not true) as casecount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".invoice c) as invoicecount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".expensereport c where isdeleted is not true) as expensecount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".item c where deleted is not true) as productcount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".folder c where deleted is not true) as foldercount,\n" +
                    "\t (select count(c.id) from " + getCompanyId() + ".fileheader c where deleted is not true) as filecount,\n" +
                    "\t (select username from " + getCompanyId() + ".myuser mu inner join " + getCompanyId() + ".myuser_role urole on mu.id= urole.users_id \n" +
                    "\t  join " + getCompanyId() + ".role  mrole on urole.roles_id= mrole.id  " +
                    "\t where mrole.code='" + EdsRole.ADMIN_CODE + "' and mu.deleted is not true\n" +
                    "\t order by mu.id limit 1) as  adminEmail, \n" +
                    "\t (select (mu.firstname || ' ' || mu.lastname) from " + getCompanyId() + ".myuser mu inner join " + getCompanyId() + ".myuser_role urole on mu.id= urole.users_id \n" +
                    "\t  join " + getCompanyId() + ".role  mrole on urole.roles_id= mrole.id  " +
                    "\t where mrole.code='" + EdsRole.ADMIN_CODE + "' and mu.deleted is not true\n" +
                    "\t order by mu.id limit 1) as adminName,\n" +
                    "\t css.parameter1 as affiliate,css.parameter2 as compaing, css.parameter3  as source, css.medium as medium,\n" +
                    "\t css.redirected  as redirected,css.referrer as referrer,css.gclid as gclid,\n" +
                    "\t (select count(mu.id) from " + getCompanyId() + ".myuser mu " +
                    "\t join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid where re.code = '" + EMPLOYEE_STATUS_NO_ACCCESS + "' and mu.deleted is not true ) noAccessUsersCount, " +
                    "\t (select count(mu.id) from " + getCompanyId() + ".myuser mu join " + getCompanyId() + ".myuser_role urole on mu.id= urole.users_id join " + getCompanyId() + ".role  mrole on urole.roles_id= mrole.id  \n" +
                    "\t join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid where re.code in ('" + EMPLOYEE_STATUS_ACTIVE + "','" + EMPLOYEE_STATUS_PENDING + "') " +
                    "\t and mu.deleted is not true  and mrole.code='" + EdsRole.ESS_USER_CODE + "' and mu.deleted is not true ) essUsersCount, \n" +
                    "\t usp.id as currentUsagePlanId,usp.users as plannedActiveUser,usp.essUsers as plannedEssUser,usp.noAccessUsers as plannedNoAccessUser, \n" +
                    "\t usp.startDate usageStartdate,usp.endDate usageEnddate,usp.userRate usageUserRate, ptype.name usagePaymentType,pstatus.name usagePaymentstatus, \n" +
                    "\t (select count(mu.id) from " + getCompanyId() + ".clientcontact cc join " + getCompanyId() + ".myuser mu on cc.id=mu.id " +
                    "\t join " + getCompanyId() + ".reference re on re.id = mu.accountstatusid where re.code in ('" + EMPLOYEE_STATUS_ACTIVE + "','" + EMPLOYEE_STATUS_PENDING + "') \n" +
                    "\t and cc.access is true and mu.deleted is not true ) clientcontactcount, \n" +
                    "\t t20.cOrgType, \n" +
                    "\t (select con.primaryPhone\n" +
                    "        from " + getCompanyId() + ".myuser mu\n" +
                    "                 inner join " + getCompanyId() + ".myuser_role urole on mu.id = urole.users_id\n" +
                    "                 join " + getCompanyId() + ".role mrole on urole.roles_id = mrole.id\n" +
                    "                join " + getCompanyId() + ".employee e on mu.id = e.id\n" +
                    "                join " + getCompanyId() + ".employeeprofile empp on e.profileId = empp.id\n" +
                    "                join " + getCompanyId() + ".crmContact con on empp.contact_id = con.id\n" +
                    "        where mrole.code = 'ADMIN'\n" +
                    "          and mu.deleted is not true\n" +
                    "        order by mu.id\n" +
                    "        limit 1)                                                                                            as adminPhone\n" +
                    " from (select c.id as companyId, c.name as companyName,ct.name as countryName,ind.name as industryName, c.phone as phone,\n" +
                    " c.signeduppage as SignedUpPage, c.sigupCompIP as SignupCompIP,c.active as Active,c.registrationDate as registrationDate, c.orgType as cOrgType \n" +
                    " from " + getPublic() + ".company as c\n" +
                    " left join " + getPublic() + ".countryzone cz on cz.id=c.countryzoneid\n" +
                    " left join " + getPublic() + ".country ct on ct.id=cz.countryid\n" +
                    " left join " + getCompanyId() + ".reference ind on ind.id=c.workarea\n" +
                    " where c.id = " + companyId + " ) t20\n" +
                    " left join " + getPublic() + ".companysystemsettings css on t20.companyId=css.companyid\n" +
                    " left join (select up.* from  " + getPublic() + ".usageplan up\n" +
                    "           join (select max(id) id from " + getPublic() + ".usageplan where deleted is not true\n" +
                    "           and startDate <=now() and endDate >=now()\n" +
                    "            group by company_id) crp on up.id=crp.id ) usp on t20.companyId=usp.company_id\n" +
                    " left join " + getCompanyId() + ".reference ptype on usp.periodtype =ptype.id" +
                    " left join " + getCompanyId() + ".reference pstatus on usp.paymentStatus =pstatus.id";

            arrayList = findNative(queryStr);
        }
        return arrayList;
    }

    public List<EdsCompanyStatistic> searchCompanies(String searchKey, ListLoadConfig config) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct cs.* from " + getPublic() + ".companystatistic cs");
        sql.append(" where ( ");
        sql.append(" lower(cs.companyname) like '" + searchKey + "' ");
        sql.append("or lower(cs.contactperson) like '" + searchKey + "' ");
        sql.append("or lower(cs.email) like '" + searchKey + "' ");
        sql.append("or lower(cs.country) like '" + searchKey + "' ");
        sql.append("or lower(cs.industry) like '" + searchKey + "' ");
        sql.append(") ");
        return findNative(sql.toString(), EdsCompanyStatistic.class);
    }

    public void clearTable() {
        updateNative("delete from " + getPublic() + ".companystatistic");
    }


    public List<Object[]> getCompanyStatistics(ListingFilterParameter searchKey, Boolean isCount, Integer viewAsFilter, Integer backendUsersId) {
        String paid = "%paid%";
        paid.toLowerCase();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cs, usage FROM EdsCompanyStatistic cs, EdsUsagePlan usage");
        sb.append(" WHERE usage.company.objectID = cs.companyID and (usage.deleted is null or usage.deleted is not true)");
        if (searchKey.getParams() != null && !"".equals(searchKey.getParams())) {
            if (!"All".equals(searchKey.getParams())) {
                sb.append(" and cs.host in (").append(Utils.getStringEachValueWithParentheses(searchKey.getParams())).append(") ");
            }
        }
        if (searchKey.getAccountCode() != null && !"".equals(searchKey.getAccountCode())) {
            sb.append(" and usage.company.promoCode='").append(searchKey.getAccountCode()).append("' "); // with promotional code
        }
        if (searchKey.getCompanyID() != null && searchKey.getCompanyID() > 0) {
            sb.append(" and usage.company.objectID=").append(searchKey.getCompanyID());
        }
        if (searchKey.getSqlSearchKey() != null) {
            sb.append(" and ( ");
            sb.append(" lower('' || cs.companyID || '') like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(cs.companyName) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(cs.contactPerson) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(cs.email) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(cs.country) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(cs.industry) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(usage.status.name) like '").append(searchKey.getSqlSearchKey()).append("' ");
            sb.append(" or lower(usage.periodType.name) like '").append(searchKey.getSqlSearchKey()).append("' ");
            if (searchKey.getSqlSearchKey().toLowerCase().equals(paid)) {
                sb.append(" or usage.isPaid is true");
            }
            sb.append(") ");
        }
        String usageOrder = "usage.status.sorder, usage.id desc ";
        if (isCount) {
            sb.append(" and cs.accessCount > 1 ORDER BY cs.lastAccessDate").append(", ").append(usageOrder);
        } else {
            if (backendUsersId != null) {
                if (backendUsersId.equals(0)) {
                    sb.append(" and cs.accessCount > 1 ORDER BY cs.lastAccessDate").append(", ").append(usageOrder);
                    ;
                } else if (backendUsersId.equals(1)) {
                    sb.append(" and cs.activated = true AND cs.accessCount <= 1 ORDER BY cs.lastAccessDate").append(", ").append(usageOrder);
                    ;
                } else if (backendUsersId.equals(2)) {
                    sb.append(" and cs.activated = false ORDER BY cs.registrationDate").append(", ").append(usageOrder);
                    ;
                }
            } else {
                sb.append(" ORDER BY cs.registrationDate").append(", ").append(usageOrder);
                ;
            }
        }
        System.out.println(sb);
        return find(sb.toString());
    }


    public List<CompanyListItem> getWeeklySubscriptions(Date start, Date end) {

        String query = "SELECT NEW com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem" +
                "(" +
                "cs.companyName,cs.phone,cs.lastAccessDate,cs.registrationDate," +
                "cs.accessCount,cs.employeesCount,cs.projectsCount,cs.tasksCount,cs.departmentsCount," +
                "cs.country,cs.contactPerson,cs.email,cs.overallUsersCount,cs.activeUsersCount," +
                "cs.clientsCount,cs.tasksinProgressCount,cs.tasksCompletedCount," +
                "cs.activated,cs.clientSignUpCompIP,usage.periodType.name,usage.status.name,usage.startDate,usage.endDate" +
                ")" +
                " FROM EdsCompanyStatistic cs, EdsUsagePlan usage" +
                " WHERE usage.company.objectID = cs.companyID " +
                " and cs.registrationDate >=? and cs.registrationDate <=? and usage.company.testCompany=false " +
                " ORDER BY cs.registrationDate,usage.id desc";

        return find(query, start, end);
    }

    public List<Object[]> getLimitedSubscriptions(ListingFilterParameter fp, Date start, Date end, Integer limit) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT cs, usage FROM EdsCompanyStatistic cs, EdsUsagePlan usage");
        sb.append(" WHERE usage.company.objectID = cs.companyID and (usage.deleted is not null or usage.deleted is not true) ");
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                sb.append(" and cs.host in (").append(Utils.getStringEachValueWithParentheses(fp.getParams())).append(") ");
            }
        }
        sb.append(" and cs.registrationDate>=? and cs.registrationDate<=?");
        return findLimited(sb.toString(), limit, start, end);
    }

    public Object[] getCompany(Integer companyID) {
        return (Object[]) findSingle("SELECT cs, usage FROM EdsCompanyStatistic cs, EdsUsagePlan usage WHERE usage.company.objectID = cs.companyID and cs.companyID = ?", companyID);
    }

    public Object getOverallStatistics() {
        return findSingle("select count(*) as companiesCount, sum(0) as usersCount, " +
                " sum(cs.accessCount) as systemAccessCount, sum(0) as departmentCount, " +//cs.departmentsCount
                " sum(cs.projectCount) as projectCount, sum(cs.taskCount) as taskCount, " +
                " sum(cs.timesheetCount) as timesheetCount, sum(cs.clientCount) as clientsCount, " +
                " sum(cs.leadCount) as leadCount" +
                " from EdsCompanyStatistic cs ");

    }

    public Long getInactiveSignupersCountByDateLimit(Date sdate, Date edate) {
        return (Long) findSingle("select count(*) from EdsCompany c where c.isSetUp=false" +
                " and (c.creationTime >=? and c.creationTime <=?)" +
                " and (c.testCompany=false or c.testCompany is null)", sdate, edate);
    }

    public Long getSignUpsCountByDateLimit(Date sdate, Date edate) {
        return (Long) findSingle("select count(*) from EdsCompany c where" +
                " c.creationTime >=? and c.creationTime <=?" +
                " and  (c.testCompany=false or c.testCompany is null)", sdate, edate);
    }

    public Long getSystemUsedUsersCountByDateLimit(Date sdate, Date edate) {
        return null;
    }

    public Long getActivatedSignupersCountByDateLimit(Date sdate, Date edate) {
        return (Long) findSingle("select count(*) from EdsCompany c where c.isSetUp=true" +
                " and c.creationTime >=? and c.creationTime <=?" +
                " and  (c.testCompany=false or c.testCompany is null)", sdate, edate);
    }

    public Long getLastUpdationCount(Date sdate, Date edate) {
        return (Long) findSingle("select count(*) from EdsCompany c where" +
                " c.lastUpdateTime >=? and c.lastUpdateTime <=? " +
                " and  (c.testCompany=false or c.testCompany is null)", sdate, edate);
    }

    public Long getBouncedUsersCountByDateLimit(Date sdate, Date edate) {
        return null;
    }

    public List<Object[]> getCountriesByDate(Date sdate, Date edate) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c.countryzoneid) as cntcount,ct.name from " + getPublic() + ".company c");
        sb.append(" inner join " + getPublic() + ".countryzone cz on(cz.id=c.countryzoneid)");
        sb.append(" inner join " + getPublic() + ".country ct on(ct.id=cz.countryid)");
        sb.append(" where c.isfree=false and c.lastupdatetime >=? and c.lastupdatetime <=? ");
        sb.append(" and  (c.testCompany=false or c.testCompany is null)");
        sb.append(" GROUP BY ct.name order by cntcount  desc");
        return findNativeLimited(sb.toString(), 10, sdate, edate);
    }

    public List<Object[]> getIndustriesByDate(Date sdate, Date edate) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(c.workarea) as industryCount,r.name from " + getPublic() + ".company c ");
        sb.append(" inner join \"0\".reference r on(r.id=c.workarea)");
        sb.append(" where c.lastupdatetime >=? and c.lastupdatetime <=? ");
        sb.append(" and  (c.testCompany=false or c.testCompany is null)");
        sb.append(" GROUP BY r.name order by industryCount  desc ");
        return findNativeLimited(sb.toString(), 10, sdate, edate);
    }

    public Object getSignuppersRate(Date sdate, Date edate) {
        StringBuilder sb = new StringBuilder();
        Map<String, Date> map = new HashMap<>();
        map.put("startDate", sdate);
        map.put("endDate", edate);
        sb.append("select distinct (select count(*) from " + getPublic() + ".company c where");
        sb.append(" c.creationTime >=:startDate and c.creationTime <=:endDate) as mewSingupperscount,");
        sb.append(" (select count(*) from company c where ");
        sb.append(" c.creationTime >=:startDate and c.creationTime <=:endDate and c.issetup=true) as activeCount,");
        sb.append(" (select count(*) from company c where ");
        sb.append(" c.creationTime >=:startDate and c.creationTime <=:endDate and c.issetup=false) as inactiveCount");
        sb.append(" from " + getPublic() + ".company c ");
        sb.append(" where  (c.testCompany=false or c.testCompany is null)");
        return findNativeSingleByNamedParams(sb.toString(), map);

    }

    public void deleteByCompanyID(Integer companyID) {
        update("delete from EdsCompanyStatistic companystatistic where companystatistic.companyID = ?", companyID);
    }

    public EdsCompanyStatistic getStatisticByCompanyID(Integer companyID) {
        List<EdsCompanyStatistic> list = slaveEntityManager.createQuery("select cs from EdsCompanyStatistic cs " +
                "    where cs.companyID=:companyId", EdsCompanyStatistic.class)
                .setParameter("companyId", companyID)
                .setMaxResults(1)
                .getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Object[]> getCompanyStatistics(Integer companyID) {

        StringBuilder sb = new StringBuilder();
        sb.append("select c.name, (select count(mus.id) from \"" + companyID + "\".myusersession mus) as countaccess, (select max(mus.lastaccesstime) from \"" + companyID + "\".myusersession mus) as lastupdatetime, c.creationtime, c.issetup, ");
        sb.append("(select count(my.id) from \"").append(companyID).append("\".myuser my ) ");
        sb.append("as employeeCount,");
        sb.append("(select count(t.id) from \"").append(companyID).append("\".task t where t.deleted is not true) ");
        sb.append("as taskCount,");
        sb.append("(select count(tm.id) from \"").append(companyID).append("\".team tm where tm.isdeleted is not true) ");
        sb.append(" as teamCount,");
        sb.append("(select count(ta.id) from \"").append(companyID).append("\".task ta where ta.deleted is not true and ta.statusid = 79) ");
        sb.append(" as completedTasks, ");
        sb.append("(select count(p.id) from \"").append(companyID).append("\".project p where p.isdeleted is not true) as projectsCount, ");
        sb.append("(select count(c.id) from \"").append(companyID).append("\".crmaccount c ");
        sb.append("inner join \"").append(companyID).append("\".crmaccount_types types on types.crmaccount_id = c.id where types.type_id = ");
        sb.append("(select id ").append(EdsReference.getSql(EdsCrmAccount.CUSTOMER, EdsCrmAccount._CRM_ACCOUNT_TYPE, false)).append(")");
        sb.append(") as clientsCount, ");
        sb.append("(select count(i.id) from \"").append(companyID).append("\".issue i ) as issuesCount, ");
        sb.append("(select count(a.id) from \"").append(companyID).append("\".assessment a) as appraisalCount, ");
        sb.append("(select ct.name from company co inner join " + getPublic() + ".countryzone cz on (co.countryzoneid = cz.id) inner join " + getPublic() + ".country ct on (cz.countryid = ct.id) where co.id = ").append(companyID).append(" ) ");
        sb.append(" as countryName,");
        sb.append("(select r.name from \"").append(companyID).append("\".reference r inner join " + getPublic() + ".company co on (co.workarea = r.id) where co.id = ").append(companyID).append(") as industry, ");
        sb.append("(select my.email from \"").append(companyID).append("\".myuser my order by my.id limit 1) as email, ");
        sb.append("(select my.firstname||' '||my.lastname || ' ' || '[' || email || ']' from \"").append(companyID).append("\".myuser my order by my.id limit 1) as ContactPerson, ");
        sb.append("(c.phone) as phone,");
        sb.append("(select signeduppage from " + getPublic() + ".company where id = ").append(companyID).append(") as signeduppage, ");
        sb.append("(select count(*) from \"").append(companyID).append("\".employee e inner join \"").append(companyID).append("\".myuser my on my.id=e.id) as overalluserscount, ");
        sb.append("(select count(*) from \"").append(companyID).append("\".myuser u ");
        sb.append(" left join \"").append(companyID).append("\".reference re on re.id = u.accountstatusid ");
        sb.append(" where re.code = '").append(EMPLOYEE_STATUS_ACTIVE).append("') as activeuserscount, ");
        sb.append("(select count(*) from \"").append(companyID).append("\".myuser u ");
        sb.append(" left join \"").append(companyID).append("\".reference re on re.id = u.accountstatusid ");
        sb.append(" where (re.code = '").append(EMPLOYEE_STATUS_INACTIVE).append("' )) as inactiveuserscount, ");
        sb.append("(select count(ta.id) from \"").append(companyID).append("\".task ta where ta.deleted is not true and ta.statusid = 3) as tasksinprogres, ");
        sb.append("(select sum(t.timespent) from \"").append(companyID).append("\".task t where t.deleted is not true) as totaltimeentries, ");
        sb.append("(select count(*) from \"").append(companyID).append("\".sickrequest) as leaverequestcount, ");
        sb.append("(select count(*) from \"").append(companyID).append("\".invoice ) as invoiceCount, ");
        sb.append(" (select adminEmail from " + getPublic() + ".companySystemSettings where companyid = ").append(companyID).append(") as adminEmail, ");
        sb.append(" c.orgType ");
        sb.append("from " + getPublic() + ".company c where c.id = ").append(companyID).append(" ");
        sb.append(" and  (c.testCompany=false or c.testCompany is null)");
        sb.append(" group by  c.name, countaccess, lastupdatetime, c.creationtime,c.phone, c.issetup," +
                " employeeCount, taskCount, teamCount," +
                " completedTasks, projectsCount, clientsCount, issuesCount, appraisalCount, invoiceCount, adminEmail");
        return findNative(sb.toString());

    }

    /*@Override
    public List<CompanyListItem> getCompanyStatisticListNative(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cs.* from companystatistic cs ");
        sql.append("left join company c on cs.companyID = c.id ");
        sql.append("left join usageplan usage on usage.company_id = c.id ");
        sql.append("left join usageplan usage on usage.company_id = c.id ");
        sql.append("where (usage.deleted is null or usage.deleted is not true) ");
    }*/

    @Override
    public ListResult<CompanyListItem> getCompanyStatisticList(ListingFilterParameter fp) {
        List<String> existingCompanyList = companyManager.getExistingSchemas();
        ListResult<CompanyListItem> result = new ListResult<>();
        String sortDir = !fp.isAscending() ? " DESC " : " ";
        Map<String, Object> param = new HashMap<>();

        String sql = " with cst as (select c.id companyID,c.name companyName ,c.promoCode promoCode ,ct.name country, c.registrationDate as registrationDate," +
                " cs.accessCount ,cs.firstAccessDate,cs.lastAccessDate,css.host as host, c.active as activated,cs.usagePlanPaymentStatus,\n" +
                " cs.userCount,cs.activeUsersCount,cs.essUsersCount,cs.noAccessUsersCount,cs.clientcontactcount, cs.adminEmail, cs.adminName,\n" +
                " usp.id as currentUsagePlan,usp.users as plannedActiveUsers,usp.essUsers as plannedEssUsers,usp.noAccessUsers as plannedNoAccessUsers, \n" +
                " usp.startDate usagPlanStartDate,usp.endDate usagPlanEndDate,usp.userRate usagePlanUserRate, COALESCE(usp.isPaid,false) usagePlanPaymentType, \n" +
                " c.orgtype, cs.adminPhone \n" +
                " from " + getPublic() + ".company as c \n" +
                " left join " + getPublic() + ".countryzone cz on cz.id=c.countryzoneid \n" +
                " left join " + getPublic() + ".country ct on ct.id=cz.countryid \n" +
                " left join " + getPublic() + ".companysystemsettings css on c.id=css.companyid \n" +
                " left join " + getPublic() + ".companystatistic cs on c.id=cs.companyid \n" +
                " left join (select up.* from  " + getPublic() + ".usageplan up\n" +
                "           join (select max(id) id from " + getPublic() + ".usageplan where deleted is not true \n" +
                "           and startDate <=now()";
        if (!fp.isFromExcelPDF()) {
            sql += "     and endDate >=now()\n";
        }
        sql += """
                            group by company_id) crp on up.id=crp.id ) usp on c.id=usp.company_id )\s
                 select companyID, companyName, promoCode, country, registrationDate, accessCount, firstAccessDate ,lastAccessDate,\s
                 host,activated,usagePlanPaymentStatus,userCount,activeUsersCount,essUsersCount,noAccessUsersCount,clientcontactcount,adminEmail,adminName, currentUsagePlan,\s
                 plannedActiveUsers,plannedEssUsers,plannedNoAccessUsers,usagPlanStartDate,usagPlanEndDate,usagePlanUserRate,usagePlanPaymentType, orgType, adminPhone\s
                 from cst where 1=1\
                """;
        StringBuilder query = new StringBuilder(sql);
        if (existingCompanyList != null && existingCompanyList.size() > 0) {
            query.append(" and cst.companyID in (").append(ServerUtils.getAsCommoDelimited(existingCompanyList, "0", ",")).append(")");
        }
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                query.append(" and cst.host in (").append(Utils.getStringEachValueWithParentheses(fp.getParams())).append(") ");
            }
        }
        if (fp.getBackendUsersId() != null) {
            if (fp.getBackendUsersId().equals(1)) {
                query.append(" and cst.accessCount > 1 ");
            } else if (fp.getBackendUsersId().equals(2)) {
                query.append(" and cst.activated = true AND cst.accessCount <= 1 ");
            } else if (fp.getBackendUsersId().equals(3)) {
                query.append(" and cst.activated = false ");
            }
        }
        if (fp.getAccountCode() != null) {
            query.append(" and cst.promoCode ='").append(fp.getAccountCode()).append("' ");
        }
        if (fp.getCountryCode() != null) {
            query.append(" and cst.country ='").append(fp.getCountryCode()).append("' ");
        }
        if (fp.getStatusCode() != null) {
            if ("ACTIVE".equals(fp.getStatusCode())) {
                query.append(" and activated is true ");
            } else {
                query.append(" and activated is not true ");
            }
        }
        if (fp.getFromRegistrationDate() != null && fp.getToRegistrationDate() != null) {
            query.append(" and (cst.registrationDate >='").append(fp.getFromRegistrationDate()).append("' and ");
            query.append(" cst.registrationDate <='").append(fp.getToRegistrationDate()).append("') ");
        }
        if (fp.getFromExpirationDate() != null && fp.getToExpirationDate() != null) {
            query.append(" and (usagPlanEndDate >='").append(fp.getFromExpirationDate()).append("' and ");
            query.append(" usagPlanEndDate <='").append(fp.getToExpirationDate()).append("') ");
        }
        if (fp.getSubscriptionTypeName() != null) {
            if ("Yes".equals(fp.getSubscriptionTypeName())) {
                query.append(" and usagePlanPaymentType is true ");
            } else {
                query.append(" and cst.usagePlanPaymentType is not true ");
            }
        }
        if (fp.getSqlSearchKey() != null) {
            query.append(" and ( ");
            query.append(" lower(cst.companyID||'') like '").append(fp.getSqlSearchKey()).append("' ");
            query.append(" or lower(cst.companyName) like '").append(fp.getSqlSearchKey()).append("' ");
            query.append(" or lower(cst.adminEmail) like '").append(fp.getSqlSearchKey()).append("' ");
            query.append(" or lower(cst.country) like '").append(fp.getSqlSearchKey()).append("' ");
            query.append(") ");
        }

        if (!fp.isFromExcelPDF()) {
            List<CompanyListItem> dublicateCompaniesStatisticList = jdbcSpringManager.getSimpleJdbcTemplate().query(query.toString(), param, BeanPropertyRowMapper.newInstance(CompanyListItem.class));
            LinkedHashMap<Integer, CompanyListItem> companyStatisticMap = new LinkedHashMap<>();
            for (CompanyListItem item : dublicateCompaniesStatisticList) {
                if (companyStatisticMap.get(item.getCompanyID()) != null && !"Active".equals(item.getUsagePlanPaymentStatus())) {
                    companyStatisticMap.remove(item.getObjectID());
                } else {
                    companyStatisticMap.put(item.getCompanyID(), item);
                }
            }
            result.setTotal(companyStatisticMap.size());
        }


        query.append(" order by ");
        if ("".equals(sortDir.trim()))
            sortDir = " ASC ";
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CompanyListItem.COMPANY_ID.equals(fp.getSortField())) {
                query.append("cst.companyID" + " " + sortDir);
            } else if (CompanyListItem.COMPANY_NAME.equals(fp.getSortField())) {
                query.append("cst.companyName" + " " + sortDir);
            } else if (CompanyListItem.COMPANY_STATUS.equals(fp.getSortField())) {
                query.append("cst.activated " + " " + sortDir);
            } else if (CompanyListItem.REGISTRATION_DATE.equals(fp.getSortField())) {
                query.append("cst.registrationDate" + " " + sortDir);
            } else if (CompanyListItem.EXPIRATION_DATE.equals(fp.getSortField())) {
                query.append("cst.usagPlanEndDate " + " " + sortDir);
            } else if (CompanyListItem.FIRST_ACCESS_DATE.equals(fp.getSortField())) {
                query.append("cst.firstAccessDate" + " " + sortDir);
            } else if (CompanyListItem.LAST_ACCESS_DATE.equals(fp.getSortField())) {
                query.append("cst.lastAccessDate" + " " + sortDir);
            } else if (CompanyListItem.ACCESS_COUNT.equals(fp.getSortField())) {
                query.append("cst.accessCount" + " " + sortDir);
            } else if (CompanyListItem.SUBSCRIPTION_TYPE.equals(fp.getSortField())) {
                query.append("cst.usagePlanPaymentType" + " " + sortDir);
            } else if (CompanyListItem.COUNTRY.equals(fp.getSortField())) {
                query.append("cst.country" + " " + sortDir);
            } else if (CompanyListItem.ADMIN_EMAIL.equals(fp.getSortField())) {
                query.append("cst.adminEmail" + " " + sortDir);
            } else if (CompanyListItem.ACTIVE_USERS_COUNT_SUBS.equals(fp.getSortField())) {
                query.append("cst.plannedActiveUsers" + " " + sortDir);
            } else if (CompanyListItem.ACTIVE_USERS_COUNT.equals(fp.getSortField())) {
                query.append("cst.activeUsersCount" + " " + sortDir);
            } else if (CompanyListItem.ESS_USERS_COUNT_SUBS.equals(fp.getSortField())) {
                query.append("cst.plannedEssUsers" + " " + sortDir);
            } else if (CompanyListItem.ESS_USERS_COUNT_ACTUAL.equals(fp.getSortField())) {
                query.append("cst.essUsersCount" + " " + sortDir);
            } else if (CompanyListItem.NO_ACCESS_USER_COUNT_SUBS.equals(fp.getSortField())) {
                query.append("cst.plannedNoAccessUsers" + " " + sortDir);
            } else if (CompanyListItem.NO_ACCESS_USER_COUNT.equals(fp.getSortField())) {
                query.append("cst.noAccessUsersCount" + " " + sortDir);
            } else if (CompanyListItem.PAYMENT_STATUS.equals(fp.getSortField())) {
                query.append("cst.usagePlanPaymentStatus" + " " + sortDir);
            } else if (CompanyListItem.PERIOD_ACCESS.equals(fp.getSortField())) {
                query.append("cst.periodAccess" + " " + sortDir);
            } else if (CompanyListItem.HOST_NAME.equals(fp.getSortField())) {
                query.append("cst.host" + " " + sortDir);
            } else {
                query.append(" cst.registrationDate " + sortDir);
            }
        } else {
            query.append(" cst.registrationDate desc");
        }
        query.append(" limit " + ((fp.getLimit() != null && fp.getLimit() > 0) ? fp.getLimit() : 20));
        query.append(" offset " + ((fp.getStart() != null && fp.getStart() > 0) ? fp.getStart() : 0));


        List<CompanyListItem> dublicateCompaniesStatisticList = jdbcSpringManager.getSimpleJdbcTemplate().query(query.toString(), param, BeanPropertyRowMapper.newInstance(CompanyListItem.class));
        result.setList((ArrayList) dublicateCompaniesStatisticList);
        return result;
    }


    public CompanyManager getCompanyManager() {
        return companyManager;
    }

    public void setCompanyManager(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    @Override
    public List<Object[]> getnotLoggedCompaniesSinceMonthYear() {
        return findNative("select companyName, lastaccessdate from companystatistic where lastAccessDate<=now()");
    }

}
