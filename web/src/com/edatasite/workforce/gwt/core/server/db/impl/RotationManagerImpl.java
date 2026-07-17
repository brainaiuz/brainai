package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RotationManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("rotationManager")
public class RotationManagerImpl extends BaseManager<EdsRotation> implements RotationManager {

    @Autowired
    protected EmployeeManager employeeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ApproverManager approverManager;

    public RotationManagerImpl() {
        super(EdsRotation.class);
    }

    @Override
    public Integer getRotationLastIntNumber() {
        return (Integer) findSingle("select bce.intNumber from EdsRotation bce where (bce.deleted = false or bce.deleted is null) and bce.intNumber is not null order by bce.intNumber desc");
    }

    @Override
    public List<EdsRotation> getList(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct r from  EdsRotation r  where (r.deleted = false or r.deleted is null) ");
        if (fp.getStatusCode() != null) {
            sql.append(" and r.overallStatus = ").append(referenceManager.getByCode(fp.getStatusCode()).getObjectID().toString());
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and r.creator = ").append(fp.getEmployeeId().toString());
        }
        if (fp.getApproverID() != null) {
            sql.append(" and r.currentApprover in (select a.id from EdsApprover a where a.exactEmployee = ").append(fp.getApproverID()).append(" and a.entityType = 'ROTATION') ");
        }
        if(fp.getSqlSearchKey()!=null) {
            sql.append(" and r.rotationCode like '%").append(fp.getSearchKey()).append("%' ");
        }
        if (fp.getSortField() != null) {
            sql.append(" ORDER BY r.");
            if (fp.getSortField().equals(RotationItem.NUMBER)) {
                sql.append("intNumber");
            } else if (fp.getSortField().equals(RotationItem.DATE)) {
                sql.append("date");
            } else if (fp.getSortField().equals(RotationItem.STATUS)) {
                sql.append("overallStatus");
            } else if (fp.getSortField().equals(RotationItem.CREATOR)) {
                sql.append("creator");
            } else if (fp.getSortField().equals(RotationItem.UPDATER)) {
                sql.append("updater");
            } else if (fp.getSortField().equals(RotationItem.CREATED_DATE)) {
                sql.append("createdDate");
            } else if (fp.getSortField().equals(RotationItem.UPDATED_DATE)) {
                sql.append("updatedDate");
            } else {
                sql.append("id");
            }
            sql.append(fp.isAscending() ? "" : " desc");
        } else {
            sql.append(" order by  r.objectID  desc ");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count (*) from ").append(getCompanyId());
        sql.append(".rotation where deleted is not true");
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public boolean isRotationNumberExist(String numberString, Integer objectID) {
        List numberList;
        if (objectID != null) {
            numberList = find("select sh.intNumber from EdsRotation sh where (sh.deleted = false or sh.deleted is null)  " + " and sh.rotationCode = ? and sh.objectID <> ? ", numberString, objectID);
        } else {
            numberList = find("select sh.intNumber from EdsRotation sh where (sh.deleted = false or sh.deleted is null)  " + " and sh.rotationCode = ?", numberString);
        }
        return numberList != null && numberList.size() > 0;
    }
}
