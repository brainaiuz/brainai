package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hurshid on 12/15/2018
 */
@Repository("leaveReasonManager")
public class LeaveReasonManagerImpl extends BaseManager<EdsLeaveReason> implements LeaveReasonManager {

    public LeaveReasonManagerImpl() {
        super(EdsLeaveReason.class);
    }

    @Override
    public ArrayList<EdsLeaveReason> listLeaveReasons(ListingFilterParameter fp) {
        return (ArrayList<EdsLeaveReason>) findInterval(getSqlForListing(fp, false), fp.getStart(), fp.getLimit());
    }

    @Override
    public int countReasons(ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getSqlForListing(filterParameter, true));
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    @Override
    public EdsLeaveReason getReasonByName(String name, String code) {
        return (EdsLeaveReason) findSingleByNamedParams("select lr from EdsLeaveReason lr where (lower(lr.name)=:reasonName or lr.code=:code) and (lr.deleted is false or lr.deleted is null)"
                , preparing(new Entry("reasonName", name), new Entry("code", code)));
    }

    @Override
    public EdsLeaveReason findByCode(String code) {
        return (EdsLeaveReason) findSingleByNamedParams("select lr from EdsLeaveReason lr where lr.code=:code and (lr.deleted is false or lr.deleted is null)", preparing(new Entry("code", code)));
    }

    @Override
    public EdsLeaveReason getReasonByShortName(String shortName) {
        return (EdsLeaveReason) findSingleByNamedParams("select lr from EdsLeaveReason lr where lower(lr.shortName)=:shortName and (lr.deleted is false or lr.deleted is null)"
                , preparing(new Entry("shortName", shortName)));
    }

    @Override
    public List<EdsLeaveReason> listActiveReasons() {
        return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and lr.isActive is true order by name");
    }

    @Override
    public List<EdsLeaveReason> listActiveReasonsForAR() {
        return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and (lr.markAsDraft <> true or lr.markAsDraft is null) and lr.isActive is true order by name");
    }

    @Override
    public List<EdsLeaveReason> listActiveReasonsByGender(String gender) {
        if (gender != null) {
            return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and lr.isActive is true and " +
                    " (lr.gender = '" + gender + "' or lr.gender is null) order by name");
        } else {
            return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and lr.isActive is true order by name");
        }
    }

    @Override
    public List<EdsLeaveReason> listActiveReasonsByLimit(String gender,String searchText, Integer limit) {
        String sql;
            sql = "select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and lr.isActive is true  ";
            if(gender != null) {
                sql +=" and (lr.gender = '" + gender + "' or lr.gender is null) ";
            }
            if(searchText != null && !searchText.equals("")) {
                sql += " and lower(lr.name) like '%" + searchText.toLowerCase()  + "%' ";
            }

          sql += " order by name ";



    Query query = masterEntityManager.createQuery(sql);
        if (limit != null) {
            query.setMaxResults(limit);
        } else {
            query.setMaxResults(20);
        }
        return (ArrayList<EdsLeaveReason>) query.getResultList();
    }



    @Override
    public List<EdsLeaveReason> listActiveReasonsByGenderWithoutDrafts(String gender) {
        if (gender != null) {
            return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and (lr.markAsDraft <> true or lr.markAsDraft is null) and lr.isActive is true and " +
                    " (lr.gender = '" + gender + "' or lr.gender is null) order by name");
        } else {
            return (ArrayList<EdsLeaveReason>) find("select lr from EdsLeaveReason lr where (lr.deleted is false or lr.deleted is null) and (lr.markAsDraft <> true or lr.markAsDraft is null) and lr.isActive is true order by name");
        }
    }

    @Override
    public SelectItem[] getAttendanceLRReasons(boolean includeDayOffs) {
        List<EdsLeaveReason> reasons;
        if (includeDayOffs) {
            reasons = find("select r from EdsLeaveReason r where r.deleted<>true and r.isActive is true and attendanceLR is true");
        } else {
            reasons = find("select r from EdsLeaveReason r where r.deleted<>true and r.isActive is true and attendanceLR is true and (r.includeDayOffs is false or r.includeDayOffs is null)");
        }
        SelectItem[] item = new SelectItem[reasons.size()];
        final int[] i = {0};
        reasons.forEach(x -> {
            item[i[0]] = new SelectItem(x.getObjectID(), x.getShortName() != null ? x.getShortName() : x.getName(), x.getCode());
            i[0]++;
        });
        return item;

    }

    private String getSqlForListing(ListingFilterParameter filterParameter, boolean forCount) {
        StringBuilder sql = new StringBuilder("select ").append(forCount ? "count(r.objectID)" : "r").append(" from EdsLeaveReason r where r.deleted is not true ");

        if (!StringUtils.isEmpty(filterParameter.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(r.name) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(r.description) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(r.code) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(")");
        }
        if (!forCount) {
            sql.append(" order by ").append(!StringUtils.isEmpty(filterParameter.getSortField()) ? " r." + filterParameter.getSortField() : " r.name ");
            sql.append(filterParameter.isAscending() ? "" : " desc");
        }
        return sql.toString();
    }

    @Override
    public List<EdsLeaveReason> getLRHolidayIncludedReasons() {
        return findByNamedParams("select r from EdsLeaveReason r where r.deleted<>true and r.isActive is true and r.includeDayOffs is true", preparing());
    }
}
