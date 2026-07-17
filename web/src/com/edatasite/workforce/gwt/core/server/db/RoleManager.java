package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 5:35:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RoleManager extends Manager<EdsRole> {
    List<EdsRole> list();

    List<EdsRole> listAll();
    //boolean isDirector(EdsUser user);
    //boolean isAdministrator(EdsUser user);
    //boolean isHRLeader(EdsUser user);
    //boolean isTeamLeader(EdsUser user);

    //boolean isMember(EdsUser user);
    //boolean isClient(EdsUser user);
    //boolean isSimpleMember(EdsUser user);

    void addRole(EdsUser user, Integer role);

    void removeRole(EdsUser user, Integer role);

    boolean hasRole(EdsUser user, Integer role);

    boolean hasRoles(EdsUser user, Integer... roles);

    boolean hasEitherRoles(EdsUser user, Integer... roles);

    boolean hasOnlyRoles(EdsUser user, Integer... roles);

    boolean hasOnlySalesPersonRole(EdsUser user);

    EdsRole getByName(String roleName);

    EdsRole getByCode(String code);

    List<EdsRole> getRoleList(ListingFilterParameter filterParameter);

    Integer getRoleDeletedItemCount();


    List<EdsRole> getRoleListByCompany(Integer companyID);

    List<EdsUser> getRoleUsers(int company, int roleID);

    boolean removeRoleFromUser(int roleId, int userID);

    List<EdsRole> getListWithEntitySpecificRoles();

    List<EdsRole> getCustomRoleList();

    List<EdsRole> findByContext(String context);
}
