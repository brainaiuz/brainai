package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.backend.client.rpc.PaypalReceiptsListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 3:33:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("usagePlanManager")
public class UsagePlanManagerImpl extends BaseManager<EdsUsagePlan> implements UsagePlanManager {
    public UsagePlanManagerImpl() {
        super(EdsUsagePlan.class);
    }

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public List<EdsUsagePlan> getUsagePlansbyCompany(ListingFilterParameter filterParameter) {
        StringBuilder sqlQuery = new StringBuilder("select up from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.company = ?");
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            sqlQuery.append(" and lower(serviceType.name) like").append("'%").append(filterParameter.getSearchKey().toLowerCase()).append("%'");
        }

        sqlQuery.append(" ORDER BY ");

        if (UsagePlanItem.SERVICE.equals(filterParameter.getSortField())) {
            sqlQuery.append("serviceType.name");
        } else if (UsagePlanItem.START_DATE.equals(filterParameter.getSortField())) {
            sqlQuery.append("startDate");
        } else if (UsagePlanItem.END_DATE.equals(filterParameter.getSortField())) {
            sqlQuery.append("endDate");
        } else if (UsagePlanItem.STATUS.equals(filterParameter.getSortField())) {
            sqlQuery.append("status");
        } else if (UsagePlanItem.USERS.equals(filterParameter.getSortField())) {
            sqlQuery.append("users");
        } /*else if (UsagePlanItem.STORAGE.equals(filterParameter.getSortField())) {
            sqlQuery.append("storage");
        }*/ else {
            sqlQuery.append("startDate");
        }

        if (filterParameter.isAscending()) {
            sqlQuery.append(" DESC ");
        }

        EdsUser user = getUser();
        return find(sqlQuery.toString(), user.getCompany());
    }

    public EdsUsagePlan getCurrentUsagePlan(EdsCompany company) {
        return (EdsUsagePlan) findSingle("from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.startDate <=now() " +
                " and up.endDate >=now() " +
                " and up.company =?", company);
    }

    public EdsUsagePlan getUsagePlanByUID(String unique_guid) {
        return (EdsUsagePlan) findSingle("from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.unique_guid =?", unique_guid);
    }

    public Map<Integer, EdsUsagePlan> getCurrentUsagePlans() {
        List<EdsUsagePlan> usageplans = find("from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.startDate <=now() and up.endDate >=now() ");
        Map<Integer, EdsUsagePlan> edsUsagePlanMap = new HashMap<>();
        for (EdsUsagePlan usageplan : usageplans) {
            edsUsagePlanMap.put(usageplan.getCompany().getObjectID(), usageplan);
        }
        return edsUsagePlanMap;
    }

    public List<Integer> getNotPayedUsagePlansId(EdsReference free, EdsReference pending) {
        Date currentDate = new Date();
        return find("select up.objectID from EdsUsagePlan up where up.deleted<>true and ((up.isPaid<>true and up.status = ?) or (up.messageSended<>true and up.periodType = ?)) and (up.startDate <='" + currentDate + "' " +
                " and up.endDate >='" + currentDate + "') ", pending, free);
    }

    public List<EdsUsagePlan> getNotPayedUsagePlans(EdsReference free, EdsReference pending) {
        Date currentDate = new Date();
        return find("select up from EdsUsagePlan up where up.deleted<>true and ((up.isPaid<>true and up.status = ?) or (up.messageSended<>true and up.periodType = ?)) and (up.startDate <='" + currentDate + "' " +
                " and up.endDate >='" + currentDate + "') ", pending, free);
    }

    public List<EdsUsagePlan> getPendingUsagePlans(EdsReference pending, EdsCompany company) {
        return find("select up from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.isPaid<>true and up.status = ? and up.company=?", pending, company);
    }

    public List<EdsUsagePlan> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select up ");
        getSqlWhereList(sql, fp);

        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            sql.append(" ORDER BY ");
            if (PaypalReceiptsListItem.COMPANY_ID.equals(fp.getSortField())) {
                sql.append(" up.company.objectID ");
            } else if (PaypalReceiptsListItem.COMPANY_NAME.equals(fp.getSortField())) {
                sql.append(" up.company.name ");
            } else if (PaypalReceiptsListItem.SUBSCSTARTDATE.equals(fp.getSortField())) {
                sql.append(" up.startDate ");
            } else if (PaypalReceiptsListItem.SUBSCENDDATE.equals(fp.getSortField())) {
                sql.append(" up.endDate ");
            } else if (PaypalReceiptsListItem.PAIDAMOUNT.equals(fp.getSortField())) {
                sql.append(" up.totalAmount ");
            } else if (PaypalReceiptsListItem.NUMBEROFEMPLOYEES.equals(fp.getSortField())) {
                sql.append(" up.users ");
            } else if (PaypalReceiptsListItem.STATUS.equals(fp.getSortField())) {
                sql.append(" up.status.name ");
            }
            if (fp.isAscending())
                sql.append(" ASC ");
            else sql.append(" DESC ");
        } else {
            sql.append(" ORDER BY up.endDate desc ");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    private void getSqlWhereList(StringBuilder sql, ListingFilterParameter fp) {
        sql.append(" from EdsUsagePlan up where (up.deleted<>true or up.deleted is null) ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and ( ");
            sql.append(" lower('' || up.company.objectID || '') like '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            sql.append(" or lower(up.company.name) like '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            sql.append(" or lower(up.status.name) like '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            sql.append(") ");
        }
        sql.append(" and up.isPaid=true and up.totalAmount>0 ");
        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.getEndDate().compareTo(fp.getStartDate()) >= 0) {
            sql.append(" AND to_date(to_char(up.payment_EndDate,'yyyy-mm-dd'), 'yyyy-mm-dd') between to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') AND to_date('").append(format2.format(fp.getEndDate())).append("', 'yyyy-mm-dd') ");
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND up.status.objectID = '").append(fp.getEmployeeId()).append("'  ");
        }

    }

    public EdsUsagePlan getLastUsagePlan(Integer companyID) {
        return (EdsUsagePlan) findSingle("from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.company.objectID =? ORDER BY up.endDate desc", companyID);
    }

    public EdsUsagePlan getCompanyUsagePlan(Integer usagePlanID) {
        Integer companyID = getUser().getCompany().getObjectID();
        return (EdsUsagePlan) findSingle("from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.company.objectID =? and up.objectID =? ORDER BY up.endDate desc", companyID, usagePlanID);
    }

    public List<Integer> getFreeTrialUsagePlansId(EdsReference rf, EdsReference experid) {
        Date currentDate = new Date();
        return find("select up.objectID from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.periodType = ? and up.status <> ? and up.endDate <='" + currentDate + "') ", rf, experid);
    }

    public List<EdsUsagePlan> getFreeTrialUsagePlans(EdsReference rf, EdsReference experid) {
        Date currentDate = new Date();
        return find("select up from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.periodType = ? and up.status <> ? and up.endDate <='" + currentDate + "') ", rf, experid);
    }

    public EdsUsagePlan getFreeTrialUsagePlanCompany(EdsReference rf, EdsCompany company) {
        return (EdsUsagePlan) findSingle("select up from EdsUsagePlan up where (up.deleted is null or up.deleted<>true) and up.periodType = ? and up.company =? ORDER BY up.endDate desc", rf, company);
    }

    public EdsUsagePlan getUsagePlanCompany(Integer companyId) {
        return (EdsUsagePlan) findSingle("select pl from EdsUsagePlan pl where company=" + companyId);
    }

    @Override
    public List<EdsUsagePlan> getPaidUsagePlan(Integer companyID) {
        final String queryString = "select up from EdsUsagePlan up " +
                "    where (up.deleted is null " +
                "            or up.deleted<>true) " +
                "        and up.isPaid=true " +
                "        and up.totalAmount > 0 " +
                "        and up.company.objectID=? ";

        return this.find(queryString, companyID);
    }

    public List<EdsUsagePlan> getCompanyAllUsagePlans(Integer companyID) {
        return find("select up from EdsUsagePlan up where up.company.objectID = ?", companyID);
    }

    @Override
    public List<Integer> getAllPaidUsagePlansId(EdsReference freeTrial, EdsReference expired) {
        return find("select up.objectID from EdsUsagePlan up where (up.deleted<>true or up.deleted is null) and up.periodType <> ? and up.status <> ? order by up.objectID", freeTrial, expired);
    }

    @Override
    public List<EdsUsagePlan> getUsagePlansbyCompanyByCompanyId(EdsCompany company) {
        return find("select up from EdsUsagePlan up where  (up.deleted is null or up.deleted<>true) and up.company = ? ORDER BY up.endDate desc", company);
    }

    @Override
    public List<EdsUsagePlan> getAllPaidUsagePlans(EdsReference freeTrial, EdsReference expired) {
        return find("select up from EdsUsagePlan up where (up.deleted<>true or up.deleted is null) and up.periodType <> ? and up.status <> ? order by up.objectID", freeTrial, expired);
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(up.objectID) ");
        getSqlWhereList(sql, fp);
        Long result = (Long) findSingle(sql.toString());
        if (result != null) {
            return result.intValue();
        }
        return 0;
    }

    public List<Object[]> getExpiringCompaniesByMonthYear(Integer month, Integer year) {

        return find("select c.name, u.endDate from EdsUsagePlan u  " +
                "join u.company c " +
                "where extract(month from enddate) = ? and extract(year from enddate) = ?", month, year);
    }


    @Override
    @Transactional
    public void updatePaidStatus(Boolean paid, Integer status, Integer companyID) {
        String sql = "update " + getPublic() + ".usageplan set ispaid = " + paid + ", paymentStatus =" + status + " where company_id = " + companyID + ";";
        updateNative(sql);
    }
}
