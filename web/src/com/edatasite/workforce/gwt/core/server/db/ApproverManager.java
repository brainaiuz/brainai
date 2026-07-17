package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.approving.EdsApprover;

import java.util.ArrayList;
import java.util.List;

public interface ApproverManager extends Manager<EdsApprover> {

    List<EdsApprover> list(String entityType, Integer entityID);

    void deleteExistingDefaultApprovers(String entityType);

    void deleteExistingOnboardingApprovers(String formID, String stepType);

    Integer getIdByEmployeeId(Integer id);

    boolean isExistApproverByEntityTypeAndStepType(String entityType, String stepType);

    void deletedAprovers(String entityType, Integer entityID);

    ArrayList<Integer> getEntityIDs(String entityType, Integer exactEmployeeID);

    boolean isExistApproverByEntityType(String entityType);

    boolean isExistApproverByEntityTypeAndEntityId(String entityType, Integer entityId);

    EdsApprover getDefaultByEntityTypeAndOrder(String entityType, Integer approverOrder);
}
