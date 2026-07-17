package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.rbac.*;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.core.domain.rbac.policy.EdsTaskPolicy;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.policy.TaskPolicyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: Abdulaziz
 * Date: Oct 10, 2009
 * Time: 9:40:27 PM
 */
@Repository("taskRbacManager")
public class TaskRbacManagerImpl extends BaseManager<EdsTaskRbac> implements TaskRbacManager, Constants {

    @Autowired
    private TaskPolicyManager taskPolicyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private GroupManager groupManager;

    public TaskRbacManagerImpl() {
        super(EdsTaskRbac.class);
    }


    /**
     * The mtehod creates taskRbace entries
     * It reads task policy for this company and creates rbac entries for specific groups, users and trustees that have
     * DIRECT or INDIRECT relation to this task
     * for example TASK_ASSIGNEE has direct relationship thus he can add Timesheet entries to this task
     * PROJECT_MANAGER, or ADMINISTRATORS of this task has indirect relationship they can edit task assignees
     * CLIENT can only view the task
     * all this rules has been desrcibed in company's task policy
     *
     * @param task
     */
    public void addRbacEntries(EdsTask task) {
        //Old indexes should be removed in order to keep table clean without dublication or old entries
        if (task.getProject() != null) {
            EdsCrmAccount client = task.getProject().getClient();
            EdsProject project = task.getProject();
            removeTaskEntries(task);
            Set<EdsEmployeeTask> empTasks = task.getUnDeletedAssignments();
            EdsTaskPolicy tPolicy = taskPolicyManager.getCompanyDirectRelationPolicy(EdsRelationship.TASK_ASSIGNEE);

            if (empTasks.size() != 0) {// task assigness has
                for (EdsEmployeeTask empTask : empTasks) {
                    if (empTask.getProjectEmployee() != null && empTask.getProjectEmployee().getEmployeeDepartment() != null) {
                        EdsDepartment department = empTask.getProjectEmployee().getEmployeeDepartment().getTeam();
                        createDirectRelationEntry(task, project, client, empTask, department, EdsRelationship.TASK_ASSIGNEE, tPolicy.getRelation().getRank(), tPolicy.getPermission());
                    }
                }

                if (task.getCreator() != null && task.getCreator() instanceof EdsClientContact) {
                    taskPolicyManager.getCompanyDirectRelationPolicy(EdsRelationship.TASK_NOT_ASSIGNEE);
                    createIndirectRelationEntryForUser(task, project, client, task.getCreator(), EdsRelationship.TASK_NOT_ASSIGNEE, tPolicy.getRelation().getRank(), tPolicy.getPermission());
                }
            } else if (task.getCreator() != null) {
                taskPolicyManager.getCompanyDirectRelationPolicy(EdsRelationship.TASK_NOT_ASSIGNEE);
                createIndirectRelationEntryForUser(task, project, client, task.getCreator(), EdsRelationship.TASK_NOT_ASSIGNEE, tPolicy.getRelation().getRank(), tPolicy.getPermission());
            }

            //Adds rbac entry for user who is manager of project
            if (project.getManager() != null) {
                EdsTaskPolicy pmPolicy = taskPolicyManager.getCompanyRelationPolicy(EdsRelationship.TASK_PROJECT_MANAGER);
                createIndirectRelationEntryForUser(task, project, client, project.getManager(), pmPolicy.getRelation().getCode(), pmPolicy.getRelation().getRank(), pmPolicy.getPermission());
            }
            // Adds rbac entry for user who is backup manager of project
            List<EdsEmployee> backupManagers = project.getBackupManagers();
            for (EdsEmployee backupManager : backupManagers) {
                EdsTaskPolicy pmBPolicy = taskPolicyManager.getCompanyRelationPolicy(EdsRelationship.TASK_PROJECT_BACKUP_MANAGER);
                createIndirectRelationEntryForUser(task, project, client, backupManager, pmBPolicy.getRelation().getCode(), pmBPolicy.getRelation().getRank(), pmBPolicy.getPermission());
            }
            // Adds rbac entries for groups that has been predefined in taskpolicy
            List<EdsTaskPolicy> indirectPolicies = taskPolicyManager.getCompanyIndirectRelationPolicies();
            for (EdsTaskPolicy tPolic : indirectPolicies) {
                createIndirectRelationEntry(task, project, client, tPolic.getTrustee(), tPolic.getRelation().getCode(), tPolic.getRelation().getRank(), tPolic.getPermission());
            }
        }
    }

    private EdsTaskRbac createDirectRelationEntry(EdsTask task, EdsProject project, EdsCrmAccount client, EdsEmployeeTask empTask, EdsDepartment department, String relationship, Integer relationRank, EdsTaskPermission permission) {
        EdsTaskRbac tRbac = new EdsTaskRbac();
        EdsEmployee empl = empTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
        tRbac.setUser(empl);
        tRbac.setTrusteeType(EdsTrusteeType.USER);
        tRbac.setEntryType(EdsTaskRbac.INHERITED);
        tRbac.setTask(task);
        tRbac.setDepartment(department);
        tRbac.setEstimatedTime(empTask.getEstimatedTime());
        tRbac.setPercent(task.getPercent());
        tRbac.setStatus(empTask.getStatus());
        tRbac.setProject(project);
        tRbac.setClient(client);
        tRbac.setDailyLoad(empTask.getDailyLoad());
        tRbac.setActualClientChargeAmmount(empTask.getActualClientChargeAmmount());
        tRbac.setActualWageAmmount(empTask.getActualWageAmmount());
        tRbac.setActualStartDate(empTask.getActualStartDate());
        tRbac.setRelationship(relationship);
        tRbac.setRelationRank(relationRank);
        tRbac.setTaskPermission(permission);
        create(tRbac);
        return tRbac;
    }

    private EdsTaskRbac createIndirectRelationEntryForUser(EdsTask task, EdsProject project, EdsCrmAccount client, EdsUser user, String relationship, Integer relationRank, EdsTaskPermission permission) {
        EdsTaskRbac tRbac = new EdsTaskRbac();
        tRbac.setRelationship(relationship);
        tRbac.setRelationRank(relationRank);
        tRbac.setUser(user);
        tRbac.setTrusteeType(EdsTrusteeType.USER);
        tRbac.setEntryType(EdsTaskRbac.INHERITED);
        tRbac.setTask(task);
        tRbac.setProject(project);
        tRbac.setClient(client);
        tRbac.setStatus(task.getStatus());
        tRbac.setTaskPermission(permission);
        tRbac.setEstimatedTime(task.getEstimatedTime());
        tRbac.setPercent(task.getPercent());
        create(tRbac);
        return tRbac;
    }


    private EdsTaskRbac createIndirectRelationEntry(EdsTask task, EdsProject project, EdsCrmAccount client, EdsTrustee trustee, String relationship, Integer relationRank, EdsTaskPermission permission) {
        EdsTaskRbac tRbac = new EdsTaskRbac();
        tRbac.setTask(task);
        tRbac.setProject(project);
        tRbac.setClient(client);
        if (EdsTrusteeType.USER.equals(trustee.getType().getObjectID())) {
            EdsUser user = userManager.get(trustee.getTrusteeID());
            tRbac.setUser(user);
            tRbac.setTrusteeType(EdsTrusteeType.USER);
        } else if (EdsTrusteeType.GROUP.equals(trustee.getType().getObjectID())) {
            EdsGroup group = groupManager.get(trustee.getTrusteeID());
            tRbac.setGroup(group);
            tRbac.setTrusteeType(EdsTrusteeType.GROUP);
        }
        tRbac.setStatus(task.getStatus());
        tRbac.setEstimatedTime(task.getEstimatedTime());
        tRbac.setPercent(task.getPercent());
        tRbac.setEntryType(EdsTaskRbac.INHERITED);
        tRbac.setRelationship(relationship);
        tRbac.setRelationRank(relationRank);
        tRbac.setTaskPermission(permission);
        create(tRbac);
        return tRbac;
    }

    /**
     * Removes all rbac entries related to this project tasks
     *
     * @param project
     */
    public void removeProjectRelatedEntries(EdsProject project) {
        List<EdsTaskRbac> entries = getTaskProjectRelatedRbacEntries(project);
        for (EdsTaskRbac tRbac : entries) {
            delete(tRbac);
        }
    }

    @Transactional
    public void removeProjectRelatedEntriesNative(Integer projectID) {
        updateNative("DELETE FROM taskrbac WHERE projectid = " + projectID);
        jpaTemplate.flush();
    }

    /**
     * Updates task entries related to Client of project
     * All entries to old Client will be deleted and new entries for new backup manager will be created
     *
     * @param project
     */
    public void updateRbacEntriesOnProjectClientChange(EdsProject project) {
        EdsCrmAccount client = project.getClient();
        if (client != null) {
            List<EdsTaskRbac> tEntries = getTaskProjectRelatedRbacEntries(project);
            for (EdsTaskRbac tEntry : tEntries) {
                tEntry.setClient(client);
            }
        } else {
            List<EdsTaskRbac> tEntries = getTaskProjectRelatedRbacEntries(project);
            for (EdsTaskRbac tEntry : tEntries) {
                tEntry.setClient(client);
                if (EdsRelationship.TASK_CLIENT.equals(tEntry.getRelationship())) {
                    delete(tEntry);
                }
            }
        }
    }

    public void createIndirectRelation(EdsTaskRbac taskRbac, EdsTrustee trustee, EdsRelationship relationship, EdsTaskPermission permission) {
        createIndirectRelationEntry(taskRbac.getTask(), taskRbac.getProject(), taskRbac.getClient(), trustee, relationship.getCode(), relationship.getRank(), permission);
    }

    /**
     * Returns all Rbac entries for given User according to his relationship tasksk
     *
     * @param trusteeID
     * @param relationShip
     * @return
     */
    public List<EdsTaskRbac> getUserTaskRbacList(Integer trusteeID, String relationShip) {
        return find("SELECT ti FROM EdsTaskRbac ti WHERE ti.user.objectID=? and ti.trusteeType=? and ti.relationship=?", trusteeID, EdsTrusteeType.USER, relationShip);
    }

    /**
     * Returns all Rbac entries for given Group according to it's relationship to tasks
     *
     * @param trusteeID
     * @param relationShip
     * @return
     */
    public List<EdsTaskRbac> getGroupTaskRbacList(Integer trusteeID, String relationShip) {
        return find("SELECT ti FROM EdsTaskRbac ti WHERE ti.group.objectID=? and ti.trusteeType=? and ti.relationship=?", trusteeID, EdsTrusteeType.GROUP, relationShip);
    }

    public List<EdsTaskRbac> getRelationTaskRbacList(String code) {
        return find("SELECT ti FROM EdsTaskRbac ti WHERE ti.relationship=?", code);
    }

    /**
     * Retrieves all rbacEntries related to project
     *
     * @param project
     * @return
     */
    public List<EdsTaskRbac> getTaskProjectRelatedRbacEntries(EdsProject project) {
        return (List<EdsTaskRbac>) find("SELECT ti FROM EdsTaskRbac ti WHERE ti.project = ?", project);
    }

    /**
     * Removes existing Rbac entries for given task
     *
     * @param task
     */
    public void removeTaskEntries(EdsTask task) {
        if (task.isNewItem()) return;
        update("DELETE FROM EdsTaskRbac ti where ti.task = ?", task);
    }

    /**
     * This method removes task rbac entries when deleting employee
     *
     * @param employeeID
     */
    @Override
    public void removeTaskEntriesForDeletedEmployee(Integer employeeID) {
        update("DELETE FROM EdsTaskRbac t WHERE t.user.id = " + employeeID);
    }

    public LinkedHashMap<Integer, List<EdsTaskRbac>> getTaskRbacEntries(List<Integer> taskIds) {
        List<EdsTaskRbac> items = (List<EdsTaskRbac>) find("SELECT ti FROM EdsTaskRbac ti WHERE ti.task.objectID in (?) ", taskIds);
        LinkedHashMap<Integer, List<EdsTaskRbac>> taskRbacs = new LinkedHashMap();
        for (EdsTaskRbac item : items) {
            Integer taskId = item.getTask().getObjectID();
            List<EdsTaskRbac> rbacs = taskRbacs.get(taskId);
            if (rbacs == null) {
                rbacs = new ArrayList<>();
                rbacs.add(item);
                taskRbacs.put(taskId, rbacs);
                continue;
            }
            rbacs.add(item);
        }
        return taskRbacs;
    }

    @Override
    public void updateTaskRbacEntries(Integer taskID, Integer projectID) {
        update("UPDATE EdsTaskRbac etr SET etr.project.objectID=? WHERE etr.task.objectID=?", projectID, taskID);
    }

    public List<Integer> taskIds(EdsUser user) {
        Map params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ti.task.objectID from EdsTaskRbac ti WHERE ti.user = :user");
        params.put("user", user);
        return (List<Integer>) findByNamedParams(sql.toString(), params);
    }

    private String groupsToSqlString(String pseudoName, Set<EdsGroup> groups, Map params) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        String tempName = "group";
        int i = 0;
        sb.append(" ( ");
        for (EdsGroup group : groups) {
            if (!firstTime) {
                sb.append(" OR ");
            }
            sb.append(pseudoName).append(" = ").append(":").append(tempName).append(i);
            params.put(tempName + i, group);
            firstTime = false;
            i++;
        }
        sb.append(" ) ");
        return sb.toString();
    }

    public List<EdsTaskRbac> getEntriesForUserOrHisMemberGoups(EdsTask task, EdsUser user, Set<EdsGroup> memberships) {
        StringBuilder query = new StringBuilder();
        Map params = new HashMap();
        query.append("SELECT ti FROM EdsTaskRbac ti WHERE ti.task = :task");
        if (memberships.size() > 0) {
            String groupsQuery = groupsToSqlString("ti.group", memberships, params);
            query.append(" AND ( ti.user = :user OR ").append(groupsQuery).append(" )");

        } else {
            query.append(" AND ti.user = :user");

        }

        params.put("user", user);
        params.put("task", task);

        return (List<EdsTaskRbac>) findByNamedParams(query.toString(), params);
    }


    public void batchIndexTask(List<EdsTask> tasks) {
        for (EdsTask task : tasks) {
            if (task.getProject() != null) {
                addRbacEntries(task);
            }
        }
    }

    public List<Object[]> getDublicatTrustees() {
        return findNative("select count(id) as a,trusteeid, trusteetype from trustee group by trusteeid, trusteetype order by a desc");
    }

    public List<EdsTrustee> getDublicateTrustee(Integer trusteeid, Integer truseetype) {
        return (List<EdsTrustee>) find("SELECT ti FROM EdsTrustee ti WHERE ti.trusteeID = " + trusteeid + " AND ti.type.objectID = " + truseetype);
    }

    @Override
    public void updateTaskAssigneeRbacStatus(Integer taskID, Integer userID, Integer statusID) {
        update("UPDATE EdsTaskRbac etr SET etr.status.objectID=? WHERE etr.task.objectID=? and etr.user.objectID=? and etr.relationship=?", statusID, taskID, userID, EdsRelationship.TASK_ASSIGNEE);
    }
}
