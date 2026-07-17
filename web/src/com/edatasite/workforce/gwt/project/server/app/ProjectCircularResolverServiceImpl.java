package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 25.06.12
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
@Service("projectCircularResolverServiceImpl")
public class ProjectCircularResolverServiceImpl implements ProjectCircularResolverService, Constants{
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;

    public HashSet<String> getProjectSpecificPermissions(Integer projectID) {
        EdsUser user = employeeManager.getUser();
        if (user == null) {
            return new HashSet<>();
        }
        if (projectID != null){
            EdsProject project = projectManager.get(projectID);
            if (project.getManager() != null && user.getObjectID().equals(project.getManager().getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.PMOFPR));
            }
            if (project.isUserBackupManager(user.getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.BMOFPR));
            }
            if (project.getCreator() != null && user.getObjectID().equals(project.getCreator().getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.CREATOR));
            }
            if (projectManager.isDepartmentLeaderOfProject(projectID, user.getObjectID())) {
                user.addArtificialRole(roleManager.getByCode(Constants.DLOFPR));
            }
        }
        return rolePermissionServiceLocal.getPermissionList(PermissionConstants.PM_CONTEXT, user);

    }

}
