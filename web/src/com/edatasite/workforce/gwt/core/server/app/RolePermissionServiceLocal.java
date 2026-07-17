package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.profile.client.rpc.PermissionColumnsItem;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 22.03.2010
 * Time: 20:37:25
 * To change this template use File | Settings | File Templates.
 */
public interface RolePermissionServiceLocal {

    EdsUser checkForArtificateRoles(int candidateID);

    HashSet<String> getPermissionList(String context, EdsUser user);

    boolean hasPermission(String code);

    boolean hasReportingPermission(String code, EdsUser user);

    boolean hasPermission(String code, EdsUser user);

    PermissionColumnsItem getRolesPermissions(boolean isSuperUser);

}
