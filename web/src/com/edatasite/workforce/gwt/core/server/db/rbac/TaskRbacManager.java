package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Abdulaziz
 * Date: Oct 10, 2009
 * Time: 9:38:36 PM
 */
public interface TaskRbacManager extends Manager<EdsTaskRbac> {

    void addRbacEntries(EdsTask task);

    List<Integer> taskIds(EdsUser user);

    Map<Integer, List<EdsTaskRbac>> getTaskRbacEntries(List<Integer> taskIds);

    void updateTaskRbacEntries(Integer taskID, Integer projectID);

    void removeTaskEntries(EdsTask task);

    void batchIndexTask(List<EdsTask> tasks);

    void removeProjectRelatedEntries(EdsProject project);

    void removeProjectRelatedEntriesNative(Integer projectID);

    void updateRbacEntriesOnProjectClientChange(EdsProject project);

    void createIndirectRelation(EdsTaskRbac taskRbac, EdsTrustee trustee, EdsRelationship relationship, EdsTaskPermission permission);

    List<EdsTaskRbac> getUserTaskRbacList(Integer trusteeID, String relationShip);

    List<EdsTaskRbac> getGroupTaskRbacList(Integer trusteeID, String relationShip);

    List<EdsTaskRbac> getRelationTaskRbacList(String code);

    List<EdsTaskRbac> getEntriesForUserOrHisMemberGoups(EdsTask task, EdsUser user, Set<EdsGroup> memberships);

    List<Object[]> getDublicatTrustees();
    
    List<EdsTrustee> getDublicateTrustee(Integer trusteeid, Integer truseetype);

    void removeTaskEntriesForDeletedEmployee(Integer employeeID);

    void updateTaskAssigneeRbacStatus(Integer taskID, Integer userID, Integer statusID);

}
