package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("approveManager")
public class ApproverManagerImpl extends BaseManager<EdsApprover> implements ApproverManager {

    public ApproverManagerImpl() {
        super(EdsApprover.class);
    }

    @Override
    public List<EdsApprover> list(String entityType, Integer entityID) {
        if (entityID != null) {
            return find("FROM EdsApprover a where " + ServerUtils.checkForDeleted("a.deleted") + " and entityType=? and entityID=? order by approverOrder", entityType, entityID);
        } else {
            return find("FROM EdsApprover a where " + ServerUtils.checkForDeleted("a.deleted") + " and entityType=? and is_default is true order by approverOrder", entityType);
        }
    }

    @Override
    public EdsApprover getDefaultByEntityTypeAndOrder(String entityType, Integer approverOrder) {
        if (entityType != null) {
            return (EdsApprover) findSingle("FROM EdsApprover a where " + ServerUtils.checkForDeleted("a.deleted") + " and entityType=? and approverOrder=? and is_default is true", entityType, approverOrder);
        }
        return null;
    }

    @Override
    public void deleteExistingDefaultApprovers(String entityType) {
        update("UPDATE EdsApprover set deleted = true where is_default = true and entityType=? ", entityType);
    }

    @Override
    public void deleteExistingOnboardingApprovers(String entityType, String stepType) {
        update("UPDATE EdsApprover set deleted = true where entityType=? and stepEmployeeType=? ", entityType, stepType);
    }

    @Override
    public Integer getIdByEmployeeId(Integer empId) {
        return (Integer) findSingle("select objectID from EdsApprover where exactEmployee.objectID = " + empId);
    }

    @Override
    public boolean isExistApproverByEntityTypeAndStepType(String entityType, String stepType) {
        Integer result = (Integer) findSingle("select objectID from EdsApprover where " + ServerUtils.checkForDeleted("deleted") + " and is_default = true and entityType = ? and stepEmployeeType = ? ", entityType, stepType);
        return result != null && result > 0;
    }


    @Override
    public void deletedAprovers(String entityType, Integer entityID) {
        update("UPDATE EdsApprover set deleted = true where is_default is not true and entityType=? and entityID=? ", entityType, entityID);
    }

    @Override
    public ArrayList<Integer> getEntityIDs(String entityType, Integer exactEmployeeID) {
        return (ArrayList<Integer>) find("select entityID from EdsApprover where (deleted is null or deleted is not true) AND entityType=? and exactApprover=? ", entityType, exactEmployeeID);
    }

    @Override
    public boolean isExistApproverByEntityType(String entityType) {
        Integer result = (Integer) findSingle("select objectID from EdsApprover " +
                " where " + ServerUtils.checkForDeleted("deleted") +
                "     and is_default = true " +
                "     and entityType = ? ", entityType);
        return result != null && result > 0;
    }

    @Override
    public boolean isExistApproverByEntityTypeAndEntityId(String entityType, Integer entityId) {
        Integer result = (Integer) findSingle("select objectID from EdsApprover " +
                " where " + ServerUtils.checkForDeleted("deleted") +
                "     and is_default = false " +
                "     and entityType = ? " +
                "     and entityID = ? ", entityType, entityId);
        return result != null && result > 0;
    }
}
