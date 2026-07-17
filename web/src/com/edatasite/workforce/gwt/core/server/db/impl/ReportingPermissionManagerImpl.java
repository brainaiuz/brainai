package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReportingPermission;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReportingPermissionManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Jan 8, 2012
 * Time: 5:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("reportingPermissionManager")
public class ReportingPermissionManagerImpl extends BaseManager<EdsReportingPermission> implements ReportingPermissionManager {

    public ReportingPermissionManagerImpl() {
        super(EdsReportingPermission.class);
    }

    @Override
    public HashSet<String> getPermissionCodeList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select (CASE WHEN p.modulecode = 'ONBOARDING' and p.code like 'EMPLOYEE_STEP%' and p.companyId is not null THEN replace(p.code, '_'||p.companyid, '') ");
        sql.append(" ELSE p.code END) as pcode from ").append(getCompanyId()).append(".reportingpermission p ");
        sql.append(" inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code  and m.active is true ");
        sql.append(" where (p.companyId is null or p.companyId=").append(getCompanyId().replace("\"", "")).append(") ");

        ArrayList<String> result = (ArrayList<String>) findNative(sql.toString());
        return new HashSet<>(result);
    }

    @Override
    public boolean hasPermission(String code, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code='").append(code).append("'");
        String reportingpermission = (String) findNativeSingle(sql.toString());
        return reportingpermission != null;
    }

    @Override
    public boolean hasPermission(ArrayList<String> codes, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code in ('").append(ServerUtils.getAsCommoDelimited(codes, "0", "','")).append("')");
        String reportingpermission = (String) findNativeSingle(sql.toString());
        return reportingpermission != null;
    }

    private StringBuilder getBasePermissionQuery(EdsUser user) {
        StringBuilder sql = new StringBuilder();
        Optional<EdsRole> optional = user.getRoles().stream()
                .filter(admin -> admin.getObjectID().equals(Constants.ADMIN))
                .findFirst();

        boolean isAdmin = optional.isPresent();

        if (isAdmin) {
            sql.append("select (CASE WHEN p.modulecode = 'ONBOARDING' and p.code like 'EMPLOYEE_STEP%' and p.companyId is not null ");
            sql.append("THEN replace(p.code, '_'||p.companyid, '') ELSE p.code END) as pcode from ").append(getCompanyId()).append(".reportingpermission p ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code  and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId = ").append(getCompanyID()).append(") ");
        } else {
            String roleCodes = user.getRolesCodeAsString();
            sql.append("select (CASE WHEN m.code = 'ONBOARDING' and rp.permissioncode like 'EMPLOYEE_STEP%' and p.companyId is not null ");
            sql.append("THEN replace(rp.permissioncode, '_'||p.companyid, '') ELSE rp.permissioncode END) ");
            sql.append("from ").append(getCompanyId()).append(".rolepermission rp ");
            sql.append("inner join ").append(getCompanyId()).append(".reportingpermission p on rp.permissioncode = p.code ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code  and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId =").append(getCompanyID()).append(") ");
            sql.append("and access ='").append(PermissionConstants.ALLOW).append("' ");
            sql.append("and rp.rolecode in (").append(roleCodes).append(") ");
        }

        return sql;
    }


    @Override
    public EdsReportingPermission findByCode(Integer companyID, String code, String context) {
        return (EdsReportingPermission) findNativeSingle("SELECT t.* FROM \"" + companyID + "\".reportingpermission t WHERE t.code='" + code + "' and t.context='" + context + "' ", EdsReportingPermission.class);
    }

    @Override
    public List<EdsReportingPermission> childByCode(String code, String context) {
        List<EdsReportingPermission> list = find("select t from EdsReportingPermission t, EdsReportingPermission p " +
                        " where t.parent=p.id and p.code=? and p.context=? and (p.companyId=? or p.companyId is null) and (t.companyId=? or t.companyId is null) ",
                code, context, getCompanyID(), getCompanyID());
        LinkedList<EdsReportingPermission> result = new LinkedList<>(list);
        if (!list.isEmpty()) {
            for (EdsReportingPermission reportingpermission : list) {
                result.addAll(childByCode(reportingpermission.getCode(), context));
            }
        }
        return result;
    }

    @Override
    public HashSet<String> getUsersPermissionsListNative(String context, EdsUser user) {
        String roleCodes = user.getRolesCodeAsString();
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select (CASE WHEN m.code = 'ONBOARDING' and rp.permissioncode like 'EMPLOYEE_STEP%' and p.companyId is not null ");
        sql.append("THEN replace(rp.permissioncode, '_'||p.companyid, '') ELSE rp.permissioncode END) ");
        sql.append("from ").append(companyID).append(".rolepermission rp ");
        sql.append("inner join ").append(companyID).append(".reportingpermission p on rp.permissioncode = p.code ");
        sql.append("inner join ").append(companyID).append(".mymodule m on p.modulecode = m.code and m.active is true ");
        sql.append("inner join ").append(companyID).append(".permission_context pc on pc.permissioncode = p.code ");
        sql.append("inner join ").append(getPublic()).append(".context co on co.code = pc.contextcode ");
        sql.append("where (p.companyId is null or p.companyId = ").append(ServerSecurityContext.getInstance().getCompanyId()).append(") ");
        sql.append("and access = 'ALLOW' and (co.code = '").append(context.toUpperCase()).append("' or p.ismainmenu or p.iscore = true) ");
        sql.append("and rp.rolecode in (").append(roleCodes).append(")");

        ArrayList<String> result = (ArrayList<String>) findNative(sql.toString());
        HashSet<String> permCodes = new HashSet<>(result);
        return permCodes;
    }

    private Integer getCompanyID() {
        return Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
    }

}

