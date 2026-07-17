package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.GroupPlacementManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("groupPlacementManager")
public class GroupPlacementManagerImpl extends BaseManager<EdsGroupPlacement> implements GroupPlacementManager {
    public GroupPlacementManagerImpl() {
        super(EdsGroupPlacement.class);
    }

    @Override
    public Integer getGroupPlacementLastIntNumber() {
        return (Integer) findSingle("select gp.intNumber from EdsGroupPlacement gp where (gp.deleted = false or gp.deleted is null) and gp.intNumber is not null order by gp.intNumber desc");

    }

    @Override
    public List<EdsGroupPlacement> getList(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct gp from  EdsGroupPlacement gp where (gp.deleted = false or gp.deleted is null) ");
        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (lower(gp.placementCode) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.getSortField() != null) {
            sql.append(" order by gp.");
            if (fp.getSortField().equals(GroupPlacementItem.NUMBER)) {
                sql.append("placementCode");
            } else if (fp.getSortField().equals(GroupPlacementItem.STATUS)) {
                sql.append("overallStatus");
            } else if (fp.getSortField().equals(GroupPlacementItem.APPROVER)) {
                sql.append("currentApprover");
            } else if (fp.getSortField().equals(GroupPlacementItem.DATE)) {
                sql.append("date");
            } else if (fp.getSortField().equals(GroupPlacementItem.CREATED_DATE)) {
                sql.append("createdDate");
            } else if (fp.getSortField().equals(GroupPlacementItem.CREATOR)) {
                sql.append("creator");
            } else if (fp.getSortField().equals(GroupPlacementItem.UPDATED_DATE)) {
                sql.append("updatedDate");
            } else if (fp.getSortField().equals(GroupPlacementItem.UPDATER)) {
                sql.append("updater");
            }
            sql.append(fp.isAscending() ? "" : " desc");
        } else {
            sql.append(" order by  gp.objectID  desc");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count (*) from ").append(getCompanyId());
        sql.append(".group_placement gp  where (gp.deleted = false or gp.deleted is null) ");
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public boolean isGroupPlacementNumberExist(String numberString, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select gp.intNumber from EdsGroupPlacement gp where (gp.deleted = false or gp.deleted is null)  " + " and gp.placementCode = ? and gp.objectID <> ? ", numberString, objectID);
        } else {
            numberList = find("select gp.intNumber from EdsGroupPlacement gp where (gp.deleted = false or gp.deleted is null)  " + " and gp.placementCode = ?", numberString);
        }
        return numberList != null && numberList.size() > 0;
    }
}
