package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 */

public interface RolePermissionServiceAsync {

    void getPermissionList(String context, AsyncCallback<HashSet<String>> async);

    void getPermissionSettings(String context, AsyncCallback<PermissionSettings> async);

    void getPermissionListByContext(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PermissionItem>> async);

    void getRoleList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<RoleListItem>> asyncCallback);

    void getRole(Integer id, AsyncCallback<RoleListItem> asyncCallback);

    void saveRole(RoleListItem roleListItem, AsyncCallback<Integer> async);

    void getRoles(AsyncCallback<ArrayList<SelectItem>> async);

    void deleteRole(Integer roleID, AsyncCallback<Boolean> async);

    void changeRoleStatus(Integer roleID, AsyncCallback<Void> async);

    void saveRolePermissions(int roleId, boolean priviladge, String context, AsyncCallback<Boolean> async);

    void saveRolePermission(String context, String rolePermission, boolean priviladge, AsyncCallback<Boolean> async); //rolePermission dagi qiymat quydai kurinshda keladi permissionID_roleID;

    void resetRolePermissions(String context, AsyncCallback<Boolean> async);

    void getMainMenuPermissions(AsyncCallback<HashSet<String>> async);

    void hasPermission(String code, AsyncCallback<Boolean> async);

    void getRoleList(String sectionContext, boolean isAll, AsyncCallback<ArrayList<RoleListItem>> asyncCallback);

    void saveRoleSettings(HashMap<String, String> map, AsyncCallback<Boolean> async);

    void getReportingPermissionList(ListingFilterParameter parameter, AsyncCallback<ArrayList<PermissionItem>> async);

    void updateReportingPermission(RoleListItem roleItem, Boolean value, PermissionItem item, AsyncCallback<ArrayList<PermissionItem>> async);
}
