package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsPermission;
import com.edatasite.workforce.core.domain.EdsReportingPermission;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ONBOARDING;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Jan 8, 2012
 * Time: 5:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("permissionManager")
public class PermissionManagerImpl extends BaseManager<EdsPermission> implements PermissionManager {

    public PermissionManagerImpl() {
        super(EdsPermission.class);
    }

    @Override
    public HashSet<String> getPermissionCodeList() {
        StringBuilder sql = new StringBuilder();
        sql.append("select (CASE WHEN p.modulecode = 'ONBOARDING' and p.code like 'EMPLOYEE_STEP%' and p.companyId is not null THEN replace(p.code, '_'||p.companyid, '') ");
        sql.append(" ELSE p.code END) as pcode from ").append(getPublic()).append(".permission p ");
        sql.append(" inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and (m.active is null or m.active is true) ");
        sql.append(" where (p.companyId is null or p.companyId=").append(getCompanyId().replaceAll("\"", "")).append(") ");
        sql.append(" and p.code not like ('REPORTING_SAVED_REPORT%') and p.code not like ('REPORTING_TEMPLATE%') and p.code not like ('WFP_BLOCK_%') ");

        ArrayList<String> result = (ArrayList<String>) findNative(sql.toString());
        return new HashSet<>(result);
    }

    @Override
    public HashSet<String> getMainMenuPermissions(EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.isMainMenu is true ");

        ArrayList<String> result = (ArrayList<String>) findNative(sql.toString());
        return new HashSet(result);
    }

    @Override
    public boolean hasPermission(String code, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code='").append(code).append("'");
        String permission = (String) findNativeSingle(sql.toString());
        return permission != null;
    }

    @Override
    public boolean hasReportingPermission(String code, EdsUser user) {
        StringBuilder sql = getBaseReportingPermissionQuery(user);
        sql.append(" and p.code='").append(code).append("'");
        String permission = (String) findNativeSingle(sql.toString());
        System.out.println(permission != null + " -> " + permission);
        return permission != null;
    }

    @Override
    public boolean hasPermission(ArrayList<String> codes, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code in ('").append(ServerUtils.getAsCommoDelimited(codes, "0", "','")).append("')");
        List<String> permissions = findNative(sql.toString());
        return permissions != null && permissions.size() > 0;
    }

    @Override
    public List<String> getPermissions(ArrayList<String> codes, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code in ('").append(ServerUtils.getAsCommoDelimited(codes, "0", "','")).append("')");
        return findNative(sql.toString());
    }

    @Override
    public List<String> getPermissions(List<String> codes, EdsUser user) {
        StringBuilder sql = getBasePermissionQuery(user);
        sql.append(" and p.code in ('").append(ServerUtils.getAsCommoDelimited(codes, "0", "','")).append("')");
        return findNative(sql.toString());
    }

    private StringBuilder getBasePermissionQuery(EdsUser user) {
        StringBuilder sql = new StringBuilder();
        Optional<EdsRole> optional = user != null ? user.getRoles().stream()
                .filter(admin -> admin.getObjectID() != null ? admin.getObjectID().equals(Constants.ADMIN) : null)
                .findFirst() : Optional.empty();

        boolean isAdmin = optional.isPresent();

        if (isAdmin) {
            sql.append("select (CASE WHEN p.modulecode = 'ONBOARDING' and p.code like 'EMPLOYEE_STEP%' and p.companyId is not null ");
            sql.append("THEN replace(p.code, '_'||p.companyid, '') ELSE p.code END) as pcode from ").append(getPublic()).append(".permission p ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId = ").append(getCompanyID()).append(") ");
        } else {
            String roleCodes = user.getRolesCodeAsString();
            sql.append("select (CASE WHEN m.code = 'ONBOARDING' and rp.permissioncode like 'EMPLOYEE_STEP%' and p.companyId is not null ");
            sql.append("THEN replace(rp.permissioncode, '_'||p.companyid, '') ELSE rp.permissioncode END) ");
            sql.append("from ").append(getCompanyId()).append(".rolepermission rp ");
            sql.append("inner join ").append(getPublic()).append(".permission p on rp.permissioncode = p.code ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId =").append(getCompanyID()).append(") ");
            sql.append("and access ='").append(PermissionConstants.ALLOW).append("' ");
            sql.append("and rp.rolecode in (").append(roleCodes).append(") ");
        }
        return sql;
    }

    private StringBuilder getBaseReportingPermissionQuery(EdsUser user) {
        StringBuilder sql = new StringBuilder();
        Optional<EdsRole> optional = user.getRoles().stream()
                .filter(admin -> admin.getObjectID().equals(Constants.ADMIN))
                .findFirst();

        boolean isAdmin = optional.isPresent();

        if (isAdmin) {
            sql.append("select p.code  as pcode from ").append(getCompanyId()).append(".reportingpermission p ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId = ").append(getCompanyID()).append(") ");
        } else {
            String roleCodes = user.getRolesCodeAsString();
            sql.append("select  rp.permissioncode from ").append(getCompanyId()).append(".rolepermission rp ");
            sql.append("inner join ").append(getCompanyId()).append(".reportingpermission p on rp.permissioncode = p.code ");
            sql.append("inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and m.active is true ");
            sql.append("where (p.companyId is null or p.companyId =").append(getCompanyID()).append(") ");
            sql.append("and access ='").append(PermissionConstants.ALLOW).append("' ");
            sql.append("and rp.rolecode in (").append(roleCodes).append(") ");
        }
        return sql;
    }

    public List<EdsPermission> listContext() {
        return find("select p from EdsPermission p where p.isMainMenu=true and (p.companyId is null or p.companyId=?) order by p.sorder", getCompanyID());
    }

    public List<EdsPermission> listPermissions(String context) {
        StringBuilder sql = new StringBuilder()
        .append("select p.*, 0 as clazz_ from ").append(getPublic()).append(".permission p join").append(getCompanyId()).append(".permission_context cs on cs.permissioncode = p.code ")
        .append(" WHERE cs.contextcode ='").append(context).append("'")
        .append(" AND (p.companyId is null OR p.companyId = ").append(getCompanyID()).append(")")
        .append(" order by p.parent asc , p.sorder asc ");
        return findNative(sql.toString(), EdsPermission.class);
    }

    public List<EdsReportingPermission> listReportingPermissions(String context) {
        StringBuilder query = new StringBuilder()
        .append("select p.*, 0 as clazz_ from ").append(getCompanyId()).append(".reportingpermission p ")
                .append("join ").append(getCompanyId()).append(".permission_context cs on cs.permissioncode = p.code ")
                .append("WHERE cs.contextcode = '").append(context).append("' ")
                .append("AND (p.companyId is null OR p.companyId = ").append(getCompanyID()).append(") ")
                .append("order by p.parent asc, p.sorder asc ");

        return findNative(query.toString(), EdsReportingPermission.class);
    }

    public List<Object[]> getReportingPermissionsList() {
        StringBuilder sql = new StringBuilder();
        String companyId = getCompanyId();
        sql.append("select rp.id as permissionId, r.name as reportingname, r.permissioncode, f.name as foldername, c.name as categoryname \n" +
                "\tfrom " + companyId + ".reportingpermission rp \n" +
                " \t\tjoin " + companyId + ".reporting r on rp.code = r.permissioncode  \n" +
                " \t\tjoin " + companyId + ".folders f on r.folderid = f.id \n" +
                " \t\tjoin " + getPublic() + ".reporttemplatecategory c on f.categoryCode = c.code\n" +
                "union \n" +
                "select p.id as permissionId, t.name as reportingname, p.code as permissioncode, ' Templates' as foldername, c.name as categoryname\n" +
                "from " + getPublic() + ".reporttemplate t\n" +
                "\tjoin " + getPublic() + ".reporttemplatecategory c on c.code = t.categorycode\n" +
                "\tjoin " + companyId + ".reportingpermission p on 'REPORTING_TEMPLATE_'||t.code=p.code or 'REPORTING_TEMPLATE_'||t.code||'_'||" + Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()) + "=p.code\n " +
                " union " +
                "select rp.id as permissionId, rp.name as reportingname, rp.code, '  Action' as foldername, '  Action' as categoryname " +
                " from " + companyId + ".reportingpermission rp" +
                " where code in ('REPORTING_SAVE_BUTTON','REPORTING_SAVE_AS_BUTTON','REPORTING_DELETE_BUTTON','REPORTING_SHARE_BUTTON','REPORTING_EXPORT_BUTTON'," +
                "'REPORTING_SHOW_HIDE_OPTIONS_BUTTON','REPORTING_SHOW_HIDE_DETAILS_BUTTON','REPORTING_CUSTOMIZE_COLUMNS_BUTTON','REPORTING_ADD_EDIT_FOLDER','REPORTING_MAIN_MENU', 'REPORTING_FILTER_SHOW', 'REPORTING_SUMMARY_TAB_SHOW') ");
        return findNative(sql.toString());
    }

    @Override
    public LinkedHashMap<String, String> getPermissionRoleAccess() {
        LinkedHashMap<String, String> rolePermissionMap = new LinkedHashMap<>();
        StringBuilder sql = new StringBuilder();
        String companyId = getCompanyId();
        sql.append("select p.code,array_to_string(array_agg(rp.rolecode),',') as role " +
                " from " + companyId + ".rolepermission rp " +
                "join " + companyId + ".reportingpermission p on rp.permissioncode=p.code " +
                " where rp.access='ALLOW' group by p.code ");
        List<Object[]> list = findNative(sql.toString());
        if (list != null && list.size() > 0) {
            for (Object[] item : list) {
                rolePermissionMap.put((String) item[0], (String) item[1]);
            }
        }
        return rolePermissionMap;
    }

    public Map<EdsPermission, String> listRolePermission(String context) {//USED FOR LISTING IN SETTINGS IN PERMISSION
        StringBuilder sql = new StringBuilder();
        String companyID = getCompanyId();
        String id = getCompanyId().replaceAll("\"", "");
        sql.append("select * from (select max(p.code) pcode, p.id, max(p.parent) pparent, ");
        sql.append(" max(p.name) pname, max(p.description) pdescription,  ");
        sql.append(" array_to_string(array_agg(r.code),',') as rolecolumn, max(p.sorder) psorder ");
        if (PermissionConstants.REPORTING.equals(context)) {
            sql.append(" from ").append(companyID).append(".reportingpermission p ");
        } else {
            sql.append(" from ").append(getPublic()).append(".permission p ");
        }
        sql.append("left join ").append(companyID).append(".rolepermission rp on p.code=rp.permissioncode and rp.access = 'ALLOW' ");
        sql.append("left join ").append(companyID).append(".role r on rp.rolecode = r.code ");
        sql.append("inner join ").append(companyID).append(".mymodule m on p.modulecode = m.code and m.active is true ");
        sql.append("where p.context='").append(context).append("' and coalesce(p.companyId,").append(id).append(")=").append(id).append(" ");
        sql.append("GROUP BY  p.id, rp.access order by rp.access) as p order by p.psorder ");

        ArrayList<Object[]> result = (ArrayList<Object[]>) findNative(sql.toString());
        Map<EdsPermission, String> map = new LinkedHashMap<>();
        EdsPermission edsPermission;
        for (Object[] item : result) {
            edsPermission = new EdsPermission();
            edsPermission.setCode(String.valueOf(item[0]));
            edsPermission.setObjectID(Integer.parseInt(item[1].toString()));
            edsPermission.setParent(item[2] != null ? Integer.parseInt(item[2].toString()) : null);
            edsPermission.setName(String.valueOf(item[3].toString()));
            edsPermission.setDescription(String.valueOf(item[4]));
            map.put(edsPermission, String.valueOf(item[5]));
        }
        return map;
    }

    @Override
    public EdsPermission findByCode(String code, String context) {
        return (EdsPermission) findSingle("SELECT t FROM EdsPermission t WHERE t.code=? and t.context=? ", code, context);
    }

    @Override
    public List<EdsPermission> childByCode(String code, String context) {
        List<EdsPermission> list = find("select t from EdsPermission t, EdsPermission p " +
                        " where t.parent=p.id and p.code=? and p.context=? and (p.companyId=? or p.companyId is null) and (t.companyId=? or t.companyId is null) ",
                code, context, getCompanyID(), getCompanyID());
        LinkedList<EdsPermission> result = new LinkedList<>(list);
        if (!list.isEmpty()) {
            for (EdsPermission permission : list) {
                result.addAll(childByCode(permission.getCode(), context));
            }
        }
        return result;
    }

    @Override
    public HashSet<String> getUsersPermissionsListNative(String context, EdsUser user) {
        String roleCodes = user.getRolesCodeAsString();
        String companyID = getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct (CASE WHEN m.code = 'ONBOARDING' and rp.permissioncode like 'EMPLOYEE_STEP%' and p.companyId is not null ");
        sql.append("THEN replace(rp.permissioncode, '_'||p.companyid, '') ELSE rp.permissioncode END) ");
        sql.append("from ").append(companyID).append(".rolepermission rp ");
        if (PermissionConstants.REPORTING.equals(context)) {
            sql.append("join ").append(companyID).append(".reportingpermission p on rp.permissioncode = p.code ");
        } else {
            sql.append("join ").append(getPublic()).append(".permission p on rp.permissioncode = p.code ");
        }
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

    @Override
    public Integer getLastSorderByParent(String code, String context) {
        return (Integer) findSingle("select max(t.sorder) from EdsPermission t, EdsPermission p " +
                        " where t.parent=p.id and p.code=? and p.context=? and (p.companyId=? or p.companyId is null) and (t.companyId=? or t.companyId is null) ",
                code, context, getCompanyID(), getCompanyID());
    }

    @Override
    public void deletePermissions(String parentCode, String context) {
        List<String> codess = (List<String>) find("select t.code from EdsPermission t, EdsPermission p " +
                        " where t.parent=p.id and p.code=? and p.context=? and (p.companyId=? or p.companyId is null) and (t.companyId=? or t.companyId is null) ",
                parentCode, context, getCompanyID(), getCompanyID());
        StringBuilder codes = new StringBuilder();
        for (String s : codess) {
            codes.append("'").append(s).append("',");
        }
        codes.append("'").append(parentCode).append("'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissionCode in (" + codes + ")");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode in (" + codes + ")");
        updateNative("delete from " + getPublic() + ".permission where context = '" + context + "' and code in (" + codes + ")");
    }

    @Override
    public void insertPermissionForCustomForm(String form_id, String name, String context, List<EdsRole> roles) {

        String moduleName = "";
        String parentCode = "";
        switch (context) {
            case "accounting" -> {
                moduleName = "ACCOUNTING_MODULE";
                parentCode = "ACCOUNTING_ACCOUNTING_MENU";
            }
            case "hrms" -> {
                moduleName = "HRMS_MODULE";
                parentCode = "HRMS_SECTION_TAB";
            }
            case "pm" -> {
                moduleName = "PM";
                parentCode = "PM_MAIN_MENU";
            }
            case "crm" -> {
                moduleName = "CRM_MODULE";
                parentCode = "CRM_SALES_TAB";
            }
            case "documents" -> {
                moduleName = "DOCUMENT_MANAGEMENT";
                parentCode = "DOCUMENTS_MAIN_MENU";
            }
            case "payroll" -> {
                moduleName = "PAYROLL";
                parentCode = "PAYROLL";
            }
        }


        insertPermissionToCustomForm(form_id, name, context, moduleName, parentCode);


        StringBuilder roleQuery = new StringBuilder();
        for (EdsRole role : roles) {
            roleQuery.append("insert into" + getCompanyId() + ".rolePermission (permissioncode, rolecode, access) " +
                    "values ( '").append(form_id).append("_" + getCompanyID()).append("','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_ADD_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_EDIT_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_DELETE_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_SUMMARY_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_FULL_LIST_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_SEE_OWN_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_SEE_BY_TYPE_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_ADD_LINKS_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_EXPORT_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_CUSTOMIZE_COLUMN_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_FILTER_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ), " +
                    " ( '").append(form_id).append("_PDF_" + getCompanyID() + "','").append(role.getCode()).append("','ALLOW' ); ");
        }

        updateNative(roleQuery.toString());
    }

    @Override
    public void insertPermissionForOldCustomForm(String form_id, String name, String context, LinkedHashMap<String, List<String>> roleMap) {

        String moduleName = "";
        String parentCode = "";
        switch (context) {
            case "accounting" -> {
                moduleName = "ACCOUNTING_MODULE";
                parentCode = "ACCOUNTING_ACCOUNTING_MENU";
            }
            case "hrms" -> {
                moduleName = "HRMS_MODULE";
                parentCode = "HRMS_SECTION_TAB";
            }
            case "pm" -> {
                moduleName = "PM";
                parentCode = "PM_MAIN_MENU";
            }
            case "crm" -> {
                moduleName = "CRM_MODULE";
                parentCode = "CRM_SALES_TAB";
            }
            case "documents" -> {
                moduleName = "DOCUMENT_MANAGEMENT";
                parentCode = "DOCUMENTS_MAIN_MENU";
            }
            case "payroll" -> {
                moduleName = "PAYROLL";
                parentCode = "PAYROLL";
            }
            case "tc" -> {
                moduleName = "TC";
                parentCode = "TC_MAIN_MENU";
            }
        }


        insertPermissionToCustomForm(form_id, name, context, moduleName, parentCode);


        StringBuilder roleQuery = new StringBuilder();

        if (roleMap != null && roleMap.size() > 0) {
            for (String permission : roleMap.keySet()) {
                List<String> roles = roleMap.get(permission);
                if (roles != null && roles.size() > 0) {
                    for (String roleCode : roles) {
                        roleQuery.append("insert into" + getCompanyId() + ".rolePermission (permissioncode, rolecode, access) " +
                                "values ( '").append(permission).append("','").append(roleCode).append("','ALLOW' ); \n");
                    }
                }
            }
        }

        updateNative(roleQuery.toString());
    }

    private void insertPermissionToCustomForm(String form_id, String name, String context, String moduleName, String parentCode) {

        StringBuilder sql = new StringBuilder();
        StringBuilder sqlForms = new StringBuilder();
        StringBuilder sqlContext = new StringBuilder();

        sql.append("insert into ").append(getPublic()).append(".permission (code, context, name, sorder, parent, modulecode, companyId) ");
        sql.append(" values ( '").append(form_id).append("_" + getCompanyID()).append("','").append(context.toUpperCase()).append("','").append(name).append("',");
        sql.append(" (select max(sorder)+1 from ").append(getPublic()).append(".permission where parent=(select id from ").append(getPublic()).append(".permission where code='" + parentCode + "' LIMIT 1)), ");
        sql.append(" (select id from ").append(getPublic()).append(".permission where code='" + parentCode + "' LIMIT 1), '").append(moduleName).append("', '" + getCompanyID() + "' ); ");

        sqlForms.append("insert into ").append(getPublic()).append(".permission (code, context, name, sorder, parent, modulecode, companyId) ");

        sqlForms.append(" values ( '").append(form_id + "_ADD_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Add',");
        sqlForms.append(" 0, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_EDIT_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Edit',");
        sqlForms.append(" 1, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_DELETE_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Delete',");
        sqlForms.append(" 2, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_SUMMARY_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Summary',");
        sqlForms.append(" 3, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_PDF_" + getCompanyID() + "','").append(context.toUpperCase()).append("','PDF',");
        sqlForms.append(" 4, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_EXPORT_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Export',");
        sqlForms.append(" 5, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_CUSTOMIZE_COLUMN_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Customize Column',");
        sqlForms.append(" 6, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");


        sqlForms.append(" ( '").append(form_id).append("_FULL_LIST_" + getCompanyID() + "','").append(context.toUpperCase()).append("','See All',");
        sqlForms.append(" 7, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_SEE_OWN_" + getCompanyID() + "','").append(context.toUpperCase()).append("','See Own',");
        sqlForms.append(" 8, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_SEE_BY_TYPE_" + getCompanyID() + "','").append(context.toUpperCase()).append("','See By Type',");
        sqlForms.append(" 9, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_ADD_LINKS_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Add Links',");
        sqlForms.append(" 10, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_FILTER_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Filter',");
        sqlForms.append(" 11, (select id from permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ), ");

        sqlForms.append(" ( '").append(form_id).append("_COPY_" + getCompanyID() + "','").append(context.toUpperCase()).append("','Copy',");
        sqlForms.append(" 12, (select id from ").append(getPublic()).append(".permission where code='" + form_id + "_" + getCompanyID() + "'), '").append(moduleName).append("', '" + getCompanyID() + "' ); ");


        List<String> contextCodeList = new ArrayList<>();
        contextCodeList.add("ACCOUNTING");
        contextCodeList.add("CRM");
        contextCodeList.add("HRMS");
        contextCodeList.add("PM");
        contextCodeList.add("PAYROLL");

        sqlContext.append("insert into" + getCompanyId() + ".permission_context (permissioncode, contextcode) ");
        sqlContext.append(" values ");

        int i = 0;
        for (String code : contextCodeList) {
            sqlContext.append(" ('").append(form_id).append("_" + getCompanyID()).append("','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_ADD_" + getCompanyID()).append("','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_EDIT_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_DELETE_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_SUMMARY_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_PDF_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_FULL_LIST_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_SEE_OWN_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_SEE_BY_TYPE_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_ADD_LINKS_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_CUSTOMIZE_COLUMN_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_EXPORT_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_FILTER_" + getCompanyID() + "','").append(code).append("'),");
            sqlContext.append(" ( '").append(form_id).append("_COPY_" + getCompanyID() + "','").append(code).append("')").append(i == contextCodeList.size() - 1 ? ";" : ",");
            i++;
        }

        updateNative(sql.toString());
        updateNative(sqlForms.toString());
        updateNative(sqlContext.toString());

    }

    @Override
    public void deletePermissionForCustomForm(String old_formId) {

        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_ADD_" + getCompanyID() + "' ");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_EDIT_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_DELETE_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_PDF_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_SUMMARY_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_FULL_LIST_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_SEE_OWN_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_SEE_BY_TYPE_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_ADD_LINKS_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_CUSTOMIZE_COLUMN_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_EXPORT_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_FILTER_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_" + getCompanyID() + "'");
        updateNative("delete from " + getPublic() + ".permission where code = '" + old_formId + "_COPY_" + getCompanyID() + "'");

        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_ADD_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_EDIT_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_DELETE_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_SUMMARY_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_PDF_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_FULL_LIST_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_SEE_OWN_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_SEE_BY_TYPE_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_ADD_LINKS_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_CUSTOMIZE_COLUMN_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_EXPORT_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_FILTER_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".rolePermission where permissioncode = '" + old_formId + "_COPY_" + getCompanyID() + "'");


        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_ADD_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_EDIT_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_DELETE_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_SUMMARY_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_PDF_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_FULL_LIST_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_SEE_OWN_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_SEE_BY_TYPE_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_ADD_LINKS_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_CUSTOMIZE_COLUMN_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_EXPORT_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_FILTER_" + getCompanyID() + "'");
        updateNative("delete from " + getCompanyId() + ".permission_context where permissioncode = '" + old_formId + "_COPY_" + getCompanyID() + "'");

    }

    private Integer getCompanyID() {
        return Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
    }

    @Override
    public void createPermissionContext(List<String> permissionCodes) {
        if (permissionCodes == null || permissionCodes.isEmpty()) {
            return;
        }
        StringBuilder query = new StringBuilder();
        query.append("insert into ").append(getCompanyId()).append(".permission_context (permissioncode, contextcode) values ");
        Iterator<String> permissionIterator = permissionCodes.iterator();
        while (permissionIterator.hasNext()) {
            String edsPermission = permissionIterator.next();
            query.append("('").append(edsPermission).append("', ");
            query.append("'").append(PermissionConstants.HRMS_CONTEXT).append("')");
            if (permissionIterator.hasNext()) {
                query.append(",");
            }
        }
        updateNative(query + ";");
    }

    @Override
    public HashSet<String> loadOnBoardingPermissions() {
        StringBuilder sql = new StringBuilder();
        sql.append("select (CASE WHEN p.modulecode = 'ONBOARDING' and p.code like 'EMPLOYEE_STEP%' and p.companyId is not null THEN replace(p.code, '_'||p.companyid, '') ");
        sql.append(" ELSE p.code END) as pcode from ").append(getPublic()).append(".permission p ");
        sql.append(" inner join ").append(getCompanyId()).append(".mymodule m on p.modulecode = m.code and m.active is true ");
        sql.append(" where (p.companyId is null or p.companyId=").append(getCompanyId().replaceAll("\"", "")).append(") ");
        sql.append(" and p.modulecode = ? ");

        ArrayList<String> result = (ArrayList<String>) findNative(sql.toString(), ONBOARDING);
        return new HashSet<>(result);

    }
}

