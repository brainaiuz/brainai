package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsPermission;
import com.edatasite.workforce.core.domain.EdsReportingPermission;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: May 8, 2012
 * Time: 5:35:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PermissionManager extends Manager<EdsPermission> {

    HashSet<String> getPermissionCodeList();

    List<EdsPermission> listContext();

    List<EdsPermission> listPermissions(String context);

    List<EdsReportingPermission> listReportingPermissions(String context);

    List<Object[]> getReportingPermissionsList();

    LinkedHashMap <String,String> getPermissionRoleAccess();

    Map<EdsPermission, String> listRolePermission(String context);

    EdsPermission findByCode(String code, String context);

    List<EdsPermission> childByCode(String code, String context);

    HashSet<String> getUsersPermissionsListNative(String context, EdsUser user);

    Integer getLastSorderByParent(String code, String context);

    void deletePermissions(String parentCode, String context);

    HashSet<String> getMainMenuPermissions(EdsUser user);

    boolean hasPermission(String code, EdsUser user);

    List<String> getPermissions(ArrayList<String> codes, EdsUser user);

    boolean hasReportingPermission(String code, EdsUser user);

    boolean hasPermission(ArrayList<String> codes, EdsUser user);

    List<String> getPermissions(List<String> codes, EdsUser user);

    void createPermissionContext(List<String> permissionCodes);

    HashSet<String> loadOnBoardingPermissions();

    void insertPermissionForCustomForm(String form_id, String name, String context, List<EdsRole> roles);

    void deletePermissionForCustomForm(String form_id);

    void insertPermissionForOldCustomForm(String oldFormID, String name, String module, LinkedHashMap<String, List<String>> roleMap);
}
