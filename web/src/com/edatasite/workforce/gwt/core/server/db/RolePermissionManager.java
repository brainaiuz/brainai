package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRolePermission;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 22.05.12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public interface RolePermissionManager extends Manager<EdsRolePermission> {

    EdsRolePermission getRolePermission(Integer role, String permissionCode);

    List<String> getRolesByPermissionCode(String permissionCode);

    void deleteRolePermissionsByContext(String context);

    void copyDefaultRolePermissions(String context);

    List<EdsRolePermission> getPermissionRolePermissions(Integer companyID, String permissionCode);

    EdsRolePermission find(String permission, String role);

    Boolean hasPermission(EdsEmployee employee, String permissionCode);

    Boolean hasPermissionCheckedForCreator(String permissionCode);

}
