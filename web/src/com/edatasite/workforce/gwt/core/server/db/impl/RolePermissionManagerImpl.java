package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRolePermission;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 22.05.12
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */

@Repository("rolePermissionManager")
public class RolePermissionManagerImpl extends BaseManager<EdsRolePermission> implements RolePermissionManager {

    public RolePermissionManagerImpl() {
        super(EdsRolePermission.class);
    }

    @Override
    public EdsRolePermission getRolePermission(Integer role, String permissionCode) {
        if (role != null && permissionCode != null) {
            return (EdsRolePermission) findNativeSingle("SELECT rp.* FROM " + getCompanyId() + ".rolepermission rp join " + getCompanyId() + ".role r on rp.rolecode=r.code WHERE rp.permissioncode='" + permissionCode + "' AND r.id=" + role, EdsRolePermission.class);
        }
        return null;
    }

    @Override
    public List<String> getRolesByPermissionCode(String permissionCode) {
        return find("SELECT DISTINCT rp.role.code FROM EdsRolePermission rp WHERE rp.permissioncode = ? AND rp.priviledgeCode = ?", permissionCode, PermissionConstants.ALLOW);
    }

    @Override
    public void deleteRolePermissionsByContext(String context) {
        updateNative("delete from " + getCompanyId() + ".rolepermission where id in " +
                "(select rp.id from " + getCompanyId() + ".rolepermission rp join \"public\".permission p on rp.permissioncode=p.code where p.context='" + context + "')");
//        return super.find("select rp from EdsRolePermission rp join rp.permission p join p.contexts co where co.code =? and rp.role.isSystem=true", context);
    }

    @Override
    public void copyDefaultRolePermissions(String context) {
        String rolesPermissionString = "INSERT INTO " + getCompanyId() + ".rolepermission " +
                "(permissioncode, rolecode, access) " +
                "SELECT rps.permissioncode, rps.rolecode, rps.access FROM " +
                "(SELECT * FROM \"0\".rolepermission rp join \"public\".permission p on rp.permissioncode=p.code where p.context='" + context + "') AS rps;";
        updateNative(rolesPermissionString);
    }

    @Override
    public List<EdsRolePermission> getPermissionRolePermissions(Integer companyID, String permissionCode) {
        return (List<EdsRolePermission>) findNative("select rp.* from \"" + companyID + "\".rolepermission rp where rp.permissioncode='" + permissionCode + "'", EdsRolePermission.class);
    }

    public Boolean hasPermission(EdsEmployee employee, String permissionCode) {
        String sql = "select count(distinct e) > 0 from EdsEmployee e join e.roles er join er.rolePermissions rp where e.deleted<>true and rp.priviledgeCode = 'ALLOW' and rp.permissioncode = ? and e = ?";
        return (Boolean) findSingle(sql, permissionCode, employee);
    }

    public Boolean hasPermissionCheckedForCreator(String permissionCode) {
        if (permissionCode != null) {
            EdsRolePermission rolePermission = (EdsRolePermission) findNativeSingle("SELECT rp.* FROM " + getCompanyId() + ".rolepermission rp WHERE rp.permissioncode='" + permissionCode + "' AND rp.rolecode='CREATOR' AND rp.access='ALLOW'", EdsRolePermission.class);
            return rolePermission != null;
        }
        return false;
    }

    public EdsRolePermission find(String permission, String role) {
        return (EdsRolePermission) findSingle("select t from EdsRolePermission t join t.permission p join t.role r where p.code=? and r.code=?", permission, role);
    }
}
