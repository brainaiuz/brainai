package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 7/3/12
 * Time: 3:43 PM
 */
@Repository("placementManager")
public class PlacementManagerImpl extends BaseManager<EdsPlacement> implements PlacementManager {

    public PlacementManagerImpl() {
        super(EdsPlacement.class);
    }

    public List<EdsPlacement> getPlacementList(ListingFilterParameter fp, EdsUser user) {

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        boolean sortByBustomField = false;
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            sortByBustomField = fp.getSortField().contains("string_value") || fp.getSortField().contains("double_value") || fp.getSortField().contains("date_value");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pl.id, pl.* \n");
        sql.append("FROM ").append(getCompanyId()).append(".placement pl \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".crmContact candid ON (candid.id = pl.candidate_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".team t ON (t.id = pl.department_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".position po ON (po.id = pl.position_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".reference st ON (st.id = pl.status_id) \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".placementcustomfields c ON (c.id = pl.placementcustomfieldsid) \n");
        sql.append("WHERE pl.deleted is not true \n");
        if (sortByBustomField) {
            sql.append(" AND c." + fp.getSortField() + " IS NOT NULL ");
        }
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_PLACEMENT_SEE_All)) {
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_PLACEMENT_SEE_OWN)) {
                Integer departmentId = user.getEmployee().getEmployeeDepartment().getTeam().getObjectID();
                sql.append(" AND pl.department_id=" + departmentId);
            }
        }
        //filter by department ID
        if (fp.getDepartmentId() != null && fp.getDepartmentId() > 0) {
            sql.append(" AND t.id=").append(fp.getDepartmentId()).append(" AND t.isDeleted is not true \n");
        }
        //searching
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" LOWER(candid.firstName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(candid.lastName) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(po.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("OR LOWER(st.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") \n");
        }
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (PlacementItem.PLACEMENT_CANDIDATE_NAME.equals(fp.getSortField())) {
                sql.append("candid.firstName");
            } else if (PlacementItem.PLACEMENT_POSITION_OFFERED.equals(fp.getSortField())) {
                sql.append("po.name");
            } else if (PlacementItem.PLACEMENT_DATE_OFFERED.equals(fp.getSortField())) {
                sql.append("pl.offer_date");
            } else if (PlacementItem.PLACEMENT_STATUS_OFFER.equals(fp.getSortField())) {
                sql.append("st.name");
            } else if (PlacementItem.PLACEMENT_CODE.equals(fp.getSortField())) {
                sql.append("pl.plalcemantCode");
            } else if (sortByBustomField) {
                sql.append("c." + fp.getSortField());
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append(" pl.lastUpdateTime DESC nulls last");
        }

        return findNative(sql.toString(), EdsPlacement.class);
    }

    @Override
    public EdsPlacement getPlacementByCandidateId(Integer candidateId) {
        return (EdsPlacement) findSingle("select e from EdsPlacement e where e.candidate = " + candidateId + " and e.deleted = false");
    }

    @Override
    public Integer getPlacementLastIntNumber() {
        return (Integer) findSingle("select s.intNumber from EdsPlacement s where s.intNumber is not null and s.deleted <> true order by s.intNumber desc");
    }

    @Override
    public List<EdsPlacement> getPlacementByGroupPlacement(Integer id) {
        return findNative("select * from " + getCompanyId() + ".placement where groupplacementid =  " + id, EdsPlacement.class);
    }

}