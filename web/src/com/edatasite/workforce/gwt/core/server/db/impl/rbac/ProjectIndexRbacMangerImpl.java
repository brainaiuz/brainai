package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsProjectIndexRbac;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.ProjectIndexRbacManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 10, 2009
 * Time: 9:40:08 PM
 */
@Repository("projectIndexRbacManager")
public class ProjectIndexRbacMangerImpl extends BaseManager<EdsProjectIndexRbac> implements ProjectIndexRbacManager {
    public ProjectIndexRbacMangerImpl() {
        super(EdsProjectIndexRbac.class);
    }

    @Autowired
    private ProjectManager projectManager;

    public EdsProjectIndexRbac createProjectIndex(EdsProject project, EdsUser user, int permissions) {
        EdsProjectIndexRbac projectIndex = new EdsProjectIndexRbac();
        projectIndex.setProject(project);
        projectIndex.setUser(user);
        projectIndex.setPermission(permissions);
        create(projectIndex);
        return projectIndex;
    }

    public EdsProjectIndexRbac updateProjectIndex(EdsProject project, EdsUser user, int permissions) {
        EdsProjectIndexRbac projectIndex = getProjectIndex(project, user);
        if (projectIndex != null) {
            projectIndex.setPermission(permissions);
            return projectIndex;
        } else {
            return createProjectIndex(project, user, permissions);
        }
    }

    public EdsProjectIndexRbac getProjectIndex(EdsProject project, EdsUser user) {
        return (EdsProjectIndexRbac) findSingle("SELECT pi FROM EdsProjectIndexRbac pi WHERE pi.project = ? AND pi.user = ? ", project, user);  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeProjectIndex(EdsProject project) {
        updateNative("DELETE FROM " + getCompanyId() + ".projectindexrbac WHERE projectid = " + project.getObjectID());
    }

    public void indexProject(EdsProject project) {
        //Adding index for search
        removeProjectIndex(project);
        jpaTemplate.flush();
        List<EdsProjectEmployee> pEmployees = projectManager.getProjectInvolvedEmployees(project);
        for (EdsProjectEmployee pe : pEmployees) {
            if (pe.getEmployeeDepartment() != null && pe.getEmployeeDepartment().getEmployee() != null) {
                updateProjectIndex(project, pe.getEmployeeDepartment().getEmployee(), EdsProjectIndexRbac.READ);
            }
        }
        if (project.getCreator() != null) {
            updateProjectIndex(project, project.getCreator(), EdsProjectIndexRbac.EDIT);
        }
        if (project.getManager() != null) {
            updateProjectIndex(project, project.getManager(), EdsProjectIndexRbac.DELETE);
        }
        List<EdsEmployee> backupManagers = project.getBackupManagers();
        for (EdsEmployee backupManager : backupManagers) {
            updateProjectIndex(project, backupManager, EdsProjectIndexRbac.DELETE);
        }
    }

    public List<EdsProject> list(ListingFilterParameter fp) {
        EdsUser user = getUser();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        Map params = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT pi.project FROM EdsProjectIndexRbac pi WHERE pi.user = :user");
        params.put("user", user);
        if (fp.getClientId() != null) {
            sql.append(" AND pi.project.client.objectID = :clientid");
            params.put("clientid", fp.getClientId());
        }
        if (fp.getProjectStatusId() != null) {
            sql.append(" AND pi.project.status.objectID = :statusid");
            params.put("statusid", fp.getProjectStatusId());
        }
        return (List<EdsProject>) findByNamedParams(sql.toString(), params);
    }

    public List<EdsProjectIndexRbac> getCompanyProjectIndex() {
        return (List<EdsProjectIndexRbac>) find("SELECT pi FROM EdsProjectIndexRbac pi ", EdsProjectIndexRbac.class);
    }

    public List<EdsProjectIndexRbac> getProjectIndexes(EdsProject project) {
        return (List<EdsProjectIndexRbac>) find("SELECT pi FROM EdsProjectIndexRbac pi WHERE  pi.project = ?", project);
    }

    public List<Integer> getProjectOwners(EdsProject project) {
        return (List<Integer>) find("SELECT pi.user.objectID FROM EdsProjectIndexRbac pi WHERE pi.project = ?", project);
    }

    public List<EdsProjectIndexRbac> getProjectIndexesById(EdsUser user, String projectIds) {
        return (List<EdsProjectIndexRbac>) find("SELECT pi FROM EdsProjectIndexRbac pi WHERE pi.user = ? AND pi.project.objectID in (" + projectIds + ") ", user);
    }

    public List<EdsProjectIndexRbac> getProjectIndexesByIdForAdminAndDir(String projectIds) {
        return (List<EdsProjectIndexRbac>) find("SELECT DISTINCT pi FROM EdsProjectIndexRbac pi WHERE pi.project.objectID in (" + projectIds + ") ");
    }

    public void removeCompanyRelatedRbacEntries() {
        List<EdsProjectIndexRbac> pEntries = getCompanyProjectIndex();
        for (EdsProjectIndexRbac pEntry : pEntries) {
            delete(pEntry);
        }
    }
}
