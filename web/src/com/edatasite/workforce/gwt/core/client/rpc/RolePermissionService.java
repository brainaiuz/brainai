package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * User: Aziz
 * Date: 11.05.2012
 */


public interface RolePermissionService extends RemoteService {

    HashSet<String> getPermissionList(String context);

    PermissionSettings getPermissionSettings(String context);

    ListResult<PermissionItem> getPermissionListByContext(ListingFilterParameter filterParameter);

    ListResult<RoleListItem> getRoleList(ListingFilterParameter filterParameter);

    Integer saveRole(RoleListItem roleListItem);

    ArrayList<SelectItem> getRoles();

    RoleListItem getRole(Integer id);

    Boolean deleteRole(Integer roleID);

    void changeRoleStatus(Integer roleID);

    Boolean saveRolePermissions(int roleId, boolean priviladge, String context);

    Boolean saveRolePermission(String context, String rolePermission, boolean priviladge); //rolePermission dagi qiymat quydai kurinshda keladi permissionID_roleID;

    Boolean resetRolePermissions(String context);

    boolean hasPermission(String code);

     HashSet<String> getMainMenuPermissions();

    ArrayList<RoleListItem> getRoleList(String sectionContext, boolean isAll);

    boolean saveRoleSettings(HashMap<String, String> map);

    ArrayList<PermissionItem> getReportingPermissionList(ListingFilterParameter filterParameter);

    ArrayList<PermissionItem> updateReportingPermission( RoleListItem roleItem, Boolean value, PermissionItem item);


    class App {
        public static RolePermissionServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/rolepermission");
            return (RolePermissionServiceAsync) target;
        }
    }

}
