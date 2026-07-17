package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mansur
 * Date: Jan 8, 2008
 * Time: 5:38:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("roleManager")
public class RoleManagerImpl extends BaseManager<EdsRole> implements RoleManager {

    public RoleManagerImpl() {
        super(EdsRole.class);
    }

    public void addRole(EdsUser user, Integer role) {
        user.getRoles().add(get(role));
    }

    public void removeRole(EdsUser user, Integer role) {
        user.getRoles().remove(get(role));
    }

    public boolean hasRole(EdsUser user, Integer role) {
        return user.getRoles().contains(get(role));
    }

    public boolean hasRoles(EdsUser user, Integer... roles) {//and
        for (Integer sRole : roles) {
            if (!user.hasRole(get(sRole))) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEitherRoles(EdsUser user, Integer... roles) {//or
        for (Integer sRole : roles) {
            if (user.hasRole(get(sRole))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasOnlyRoles(EdsUser user, Integer... roles) {
        return hasRoles(user, roles) && (roles.length == user.getRoles().size());
    }

    @Override
    public boolean hasOnlySalesPersonRole (EdsUser user) {
        Integer[] roles = new Integer[]{EdsUser.MEM, EdsUser.SALESPERSON};
        return hasOnlyRoles(user, EdsUser.SALESPERSON) || hasOnlyRoles(user, roles);
    }

    @Override
    public EdsRole getByName(String roleName) {
        if (!"".equals(roleName) && roleName != null) {
            return (EdsRole) findSingle("select r from EdsRole r where r.isDeleted=false and LOWER(r.name)=?", roleName.toLowerCase());
        }
        return null;
    }

    @Override
    public EdsRole getByCode(String code) {
        return (EdsRole) findSingle("select r from EdsRole r where r.code=?", code);
    }

    @Override
    public List<EdsRole> getRoleList(ListingFilterParameter fp) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select r from EdsRole r where r.isDeleted=false ");
        if (fp.getSearchKey() != null && !fp.getSearchKey().isEmpty()) {
            sqlQuery.append(" and lower (r.name) like '%" + fp.getSearchKey() + "%' ");
        }
        if (fp.getSortField() != null) {
            sqlQuery.append(" order by ");
            if (fp.getSortField().equals(RoleListItem.NAME)) {
                sqlQuery.append(" r.name ");
            } else if (fp.getSortField().equals(RoleListItem.ACTIVE)) {
                sqlQuery.append(" r.active ");
            } else if (fp.getSortField().equals(RoleListItem.IS_SYSTEM)) {
                sqlQuery.append(" r.isSystem ");
            } else {
                sqlQuery.append(" r.name ");
            }
            sqlQuery.append(fp.isAscending() ? "" : " desc");
        }
        return findInterval(sqlQuery.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getRoleDeletedItemCount() {
        Long total = (Long) findSingle("select count(*) from EdsRole r where r.isDeleted=false ");
        return total.intValue();
    }

    @Override
    public List<EdsRole> getRoleListByCompany(Integer companyID) {
        String query = "select r.* from \"" + companyID + "\".role r where r.CODE is not null  and r.isdeleted is not true ORDER BY r.name";
        return findNative(query, EdsRole.class);
    }

    public List<EdsRole> list() {
        return find("select r from EdsRole r where r.isDeleted = false and r.isEntitySpecific = false and r.active is true order by r.isSystem desc, r.sorder asc ");
    }

    public List<EdsRole> getListWithEntitySpecificRoles() {
        return find("select r from EdsRole r where r.isDeleted = false order by r.isSystem desc, r.sorder asc ");
    }

    public List<EdsRole> getCustomRoleList() {
        return find("select r from EdsRole r where r.isDeleted = false and r.isSystem = false order by r.sorder asc ");
    }

    @Override
    public List<EdsRole> findByContext(String context) {
        if (StringUtils.isBlank(context)) {
            return find("select r from EdsRole r where r.isDeleted = false and r.active is true order by r.isSystem desc, r.sorder asc ");
        }
        return find("select r from EdsRole r where r.isDeleted = false and r.active is true and module_code like('%" + context + "%') order by r.isSystem desc, r.sorder asc ");
    }

    public List<EdsRole> listAll() {
        return find("select r from EdsRole r where r.isDeleted = false and r.active is true order by r.isSystem desc, r.sorder asc ");
    }

    public List<EdsUser> getRoleUsers(int company, int roleID) {
        String sql = "select u.*, 0 as clazz_ from \"" + company + "\".myuser u join \"" + company + "\".myuser_role r on u.id = r.users_id where r.roles_id=" + roleID;
        return findNative(sql, EdsUser.class);
    }

    public boolean removeRoleFromUser(int roleId, int userID) {
        String company = ServerSecurityContext.getInstance().getCompanyId();
        String sql = "delete from \"" + company + "\".myuser_role where users_id=" + userID + " and roles_id=" + roleId;
        updateNative(sql);
        return true;
    }
}

