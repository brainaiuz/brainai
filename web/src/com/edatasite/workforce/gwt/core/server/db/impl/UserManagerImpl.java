/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/8 8:56:11                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManagerImpl;
import com.edatasite.workforce.core.tools.GlobalAuthManager;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.GoogleGadgetManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 5:31:43 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("userManager")
public class UserManagerImpl extends BaseManager<EdsUser> implements UserManager, Constants {

    public UserManagerImpl() {
        super(EdsUser.class);
    }

    @Autowired
    private GlobalAuthManager globalAuthManager;

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Autowired
    GoogleGadgetManager googleGadgetManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;

    @Override
    public EdsUser findUser(String userName) throws DataAccessException {
        if (ServerSecurityContext.getInstance().getCompanyId() != null) {
            return findUser(userName, Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId()));
        } else {
            return null;
        }
    }

    //this method can be rewrited so that it will use single native query
    @Override
    public EdsUser findUser(String userName, Integer companyid) throws DataAccessException {
        return (EdsUser) findNativeSingleByParams("select u.*, 0 as clazz_ from " + "\"" + companyid + "\"" + ".myuser u where " +
                "lower(u.username)=? and (u.deleted is null or u.deleted<>true)", EdsUser.class, userName.trim().toLowerCase());
    }

    @Override
    public EdsUser searchUserByUserName(String userName, Integer companyid) throws DataAccessException {
        return (EdsUser) findNativeSingleByParams("select u.*, 0 as clazz_ from " + "\"" + companyid + "\"" + ".myuser u where " +
                "(lower(u.username)=? or lower(u.email)=?) and (u.deleted is null or u.deleted<>true)", EdsUser.class, userName.trim().toLowerCase(), userName.trim().toLowerCase());
    }

    @Override
    public EdsUser getUserByEmail(String email) throws DataAccessException {
        return (EdsUser) findSingle("from EdsUser u where u.email =?", email);
    }

    @Override
    public EdsUser getUserByUserID(Integer userID) throws DataAccessException {
        if (userID != null && userID > 0) {
            return (EdsUser) findSingle("from EdsUser u where u.id =?", userID);
        }
        return getUser();
    }

    @Override
    public List<EdsUser> getUsers() {
        String sql = "select u from EdsUser u where (u.deleted is not true and u.accountStatus.code=?)";
        return find(sql, EMPLOYEE_STATUS_ACTIVE);
    }

    @Override
    public void deleteUser(Integer objectID, EdsReference inactive) {
        Map<String, Object> map = new HashMap<>();

        map.put("inactive", inactive);
        map.put("objectID", objectID);
        updateByNamedParams("update EdsUser user set user.accountStatus=:inactive, user.deleted='true' " +
                "where user.objectID=:objectID", map);
        //Delete from googlegadgetauth
        googleGadgetManager.deleteGoogleGadgetUser(objectID, SecurityContext.getCompanyID());
        //Delete from globalauth usercompany and userauth details
        globalAuthJdbcSpringManager.deleteCompanyUser(objectID, SecurityContext.getCompanyID());
    }

    private static final String USER_PASSWORD_SQL = "SELECT ua.password FROM userAuth ua WHERE ua.domainname like ? AND ua.username = ?;";

    public String findActiveAndNonFederateLoginUsers(String domainName, String username) {
        if (domainName == null) {
            throw new IllegalArgumentException("Username can not be null!!!");
        }
        domainName = GlobalAuthJdbcSpringManagerImpl.getDomainNameWithLike(domainName, true);
        return globalAuthManager.executeQuery(rs -> {
            String result = null;
            while (rs.next()) {
                result = rs.getString(1);
            }
            return result;
        }, USER_PASSWORD_SQL, domainName, (username != null ? username.trim().toLowerCase() : username));
    }

    public Integer findExistingUserName(String userName) {
        if (userName == null) {
            throw new IllegalArgumentException("Username can not be null!!!");
        }
        return globalAuthManager.executeQuery(rs -> {
            Integer result = null;
            while (rs.next()) {
                result = rs.getInt(1);
            }
            return result;
        }, "SELECT ua.id FROM userAuth ua WHERE ua.username = ?;", (userName != null ? userName.trim().toLowerCase() : userName));
    }

    @Deprecated
    public List<EdsUser> findCompanyUsers(String userName) throws DataAccessException {
        List<EdsUser> users = new ArrayList<>();//actually it is only one user
        EdsUser user = findUser(userName);
        if (user != null) {
            users.add(user);
        }
        return users;
    }

    public List<EdsUser> getCompanyUsers(EdsCompany company) {
        return getSchemaAllUsers(company.getObjectID().toString());
    }

    public List<EdsEmployee> getUsersNotAssigneeTaskPolicy(EdsCompany company) {
        return find("select distinct user from EdsEmployee user " +
                "where (user.deleted is null or user.deleted<>true) and user.objectID not in " +
                "(select tp.trustee.trusteeID from EdsTaskPolicy tp where tp.trustee is not null and tp.trustee.type.objectID=? ) " +
                "order by user.firstName ", company, EdsTrusteeType.USER);
    }

    @Override
    public void removeEmployeeFromShareReport(Integer employeeID) {
        updateNative("delete from  " + getCompanyId() + ".reportingemployee where employeeid=" + employeeID);
    }

    @Override
    public List<EdsEmployee> getAdmins(Integer companyID) {
        Integer roleID = EdsRole.ADMIN;
        return getUsersByROLE(companyID, roleID, true);
    }

    public EdsEmployee getAdmin(Integer companyID) {
        Integer roleID = EdsRole.ADMIN;
        List<EdsEmployee> admins = getUsersByROLE(companyID, roleID, true);

        if (admins != null && !admins.isEmpty()) {
            return admins.get(0);
        }

        return null;
    }

    public List<EdsEmployee> getUsersByROLE(Integer companyID, Integer roleID) {
        return getUsersByROLE(companyID, roleID, true);
    }

    public List<EdsUser> getByIDs(List<Integer> objectIDs) {
        return find("select u from EdsUser u where u.objectID in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0") + ")");
    }

    public List<EdsUser> getByIDs(String objectIDs) {
        return find("select u from EdsUser u where u.objectID in (" + objectIDs + ")");
    }

    public List<EdsEmployee> getUsersByROLE(Integer companyID, Integer roleID, boolean onlyActive) {
        String sql = "";
        if (companyID != null) {
            String company = "\"" + companyID + "\"";
            sql = "select distinct e.id, e.*, u.*, 0 as clazz_ from " + company + ".employee e inner join " + company + ".myuser as u on e.id = u.id " +
                    "inner join " + company + ".myuser_role as roles on e.id = roles.users_id and roles.roles_id = " + roleID +
                    " LEFT JOIN " + company + ".reference r on r.id = u.accountstatusid " +
                    " where u.deleted is not true " +
                    (onlyActive ? " and r.code = '" + EMPLOYEE_STATUS_ACTIVE + "' " : " and r.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ") +
                    " order by e.id ";
            return (List<EdsEmployee>) findNative(sql, EdsEmployee.class);
        }
        return null;
    }

    @Override
    public List<EdsEmployee> getUsersByROLES(Integer companyID, Integer... roleID) {
        return getUsersByROLES(companyID, true, roleID);
    }

    @Override
    public List<EdsEmployee> getUsersByROLES(Integer companyID, boolean onlyActive, Integer... roleID) {
        if (companyID != null) {
            StringBuilder sql = new StringBuilder();
            String company = "\"" + companyID + "\"";
            String isActive = onlyActive ? " and r.code = '" + EMPLOYEE_STATUS_ACTIVE + "' " : " and r.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ";
            sql.append("select distinct e.id, e.*, u.*, 0 as clazz_ from ").append(company).append(".employee e \n");
            sql.append("inner join ").append(company).append(".myuser as u on e.id = u.id \n");
            sql.append("inner join ").append(company).append(".myuser_role as roles on e.id = roles.users_id \n");
            sql.append(" LEFT JOIN ").append(company).append(".reference r on r.id = u.accountstatusid \n");
            sql.append(" where u.deleted is not true ").append(isActive).append(" and (");

            for (int i = 0; i < roleID.length; i++) {
                sql.append(" roles.roles_id = ").append(roleID[i]);
                if (i != (roleID.length - 1)) {
                    sql.append(" or ");
                }
            }
            sql.append(") order by e.id ");
            return (List<EdsEmployee>) findNative(sql.toString(), EdsEmployee.class);
        }
        return null;
    }

    /**
     * Obtains the user in schema (company)
     *
     * @param id
     * @param schema
     * @return
     */
    @SchemaUnprotected
    public EdsUser getUserInSchema(Integer id, String schema) {
        List<EdsUser> users = findNative("select u.*, 0 as clazz_ from " + "\"" + schema + "\"" + ".myuser u where " +
                "u.id=" + id + " and (u.deleted is null or u.deleted<>true)", EdsUser.class);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * Obtains the user in schema (company)
     *
     * @param userId
     * @return
     */
    @SchemaUnprotected
    public EdsUser getUserInSchema(Integer userId) {
        return getUserInSchema(userId, ServerSecurityContext.getInstance().getCompanyId());
    }

    /**
     * Obtains all users in schema (company)
     *
     * @param schema
     * @return
     */
    @Override
    @SchemaUnprotected
    public List<EdsUser> getSchemaAllUsers(String schema) {
        return getSchemaAllUsers(schema, null);
    }

    /**
     * Obtains all users in schema (company)
     *
     * @param schema
     * @return
     */
    @Override
    @SchemaUnprotected
    public List<EdsUser> getSchemaAllUsers(String schema, Integer limit) {
        String sql = "select u.*, 0 as clazz_ from " + "\"" + schema + "\"" + ".myuser u where " +
                " (u.deleted is null or u.deleted<>true)";
        if (limit != null && limit > 0) {
            sql += " LIMIT " + limit;
        }
        return ((List<EdsUser>) findNative(sql, EdsUser.class));
    }

    @Override
    public List<EdsUser> getCompanyActiveUsers(Integer companyID) {
        String sql = "select u.*,(u.firstName , u.lastName) as fullName, 0 as clazz_ from " + "\"" + companyID + "\"" + ".myuser u left join " + "\"" + companyID + "\"" + ".reference r on r.id = u.accountstatusid"
                + " where (u.deleted is null or u.deleted<>true) and r.code='" + EMPLOYEE_STATUS_ACTIVE + "' order by fullName asc ";
        return ((List<EdsUser>) findNative(sql, EdsUser.class));
    }

    @Override
    public EdsUser getUserByRole(Integer companyID, Integer roleID, boolean onlyActive) {
        String sql = "";
        if (companyID != null) {
            String company = "\"" + companyID + "\"";
            sql = "select distinct e.id, e.*, u.*, 0 as clazz_ from " + company + ".employee e inner join " + company + ".myuser as u on e.id = u.id " +
                    "inner join " + company + ".myuser_role as roles on e.id = roles.users_id and roles.roles_id = " + roleID +
                    " LEFT JOIN " + company + ".reference r on r.id = u.accountstatusid " +
                    " where u.deleted is not true " +
                    (onlyActive ? " and r.code = '" + EMPLOYEE_STATUS_ACTIVE + "' " : " and r.code <> '" + EMPLOYEE_STATUS_RESIGNED + "' ") +
                    " order by e.id ";
            return (EdsUser) findNativeSingle(sql, EdsEmployee.class);
        }
        return null;
    }

    @Override
    public EdsCompanySettings getCompanySettings(Integer id) {
        EdsUser user = null;
        if (id != null) {
            user = get(id);
        } else {
            user = getUser();
        }
        if (user == null) {
            return null;
        }

        Integer companySettingsId = (Integer) findNativeSingle("select companySettingsId from " + getPublic() + ".company u where u.id=" + user.getCompany().getObjectID());
        return (EdsCompanySettings) findNativeSingle("select  u.*, 0 as clazz_  from " + getPublic() + ".companySettings u where u.id=" + companySettingsId, EdsCompanySettings.class);
    }


    @Override
    public EdsUser getUserByUserIdAndCompanyId(Integer userId, Integer companyId) {
        return (EdsUser) findNativeSingle("select u.*, 0 as clazz_ from " + "\"" + companyId + "\"" + ".myuser u where " +
                "u.id=" + userId + " and (u.deleted is null or u.deleted<>true)", EdsUser.class);
    }

    public void changeAccountUserName(Integer userID, Integer companyID, String userName) {
        String sql = "UPDATE " + "\"" + companyID + "\"" + ".myUser SET userName = ? WHERE id = ?";
        updateNativeByParams(sql, (userName != null ? userName.trim().toLowerCase() : userName), userID);
    }

    public void saveUserAuthenticationData(EdsUser user, Integer companyId) {
        saveUserAuthenticationData(user, companyId, true, true);
    }

    public void saveUserAuthenticationData(EdsUser user, Integer companyId, boolean encrypt, boolean indexSolr, boolean passwordChanged) {
        if (user == null || companyId == null) {
            throw new IllegalArgumentException("Could not save authentication data due to some parameters are null");
        }
        if (user.getPassword() == null) {
            PasswordGenerator pg = new PasswordGenerator(8);
            user.setPassword(pg.generateAsString());
            encrypt = true;
        }
        //convert to md5
        encryptPassword(user, encrypt);

        if (passwordChanged) {
            List<String> passList = globalAuthJdbcSpringManager.getLastPasswordList(user.getObjectID());
            if (passList != null && !passList.isEmpty() && passList.contains(user.getPassword())) {
                log.error("Password must not contain the last 4 passwords. Username:" + user.getUserName() + ", Email: " + user.getEmail());
                throw new RuntimeException("Password must not contain the last 4 passwords.");
            }
        }

        Integer result = globalAuthManager.executeQuery(rs -> {
            Integer result1 = null;
            while (rs.next()) {
                result1 = rs.getInt(1);
            }
            return result1;
        }, "SELECT saveUserAuthenticationData(?,?,?,?,?,?,?);", user.getUserName().toLowerCase(), user.getTempSalt(), user.getPassword(), GlobalAuthJdbcSpringManagerImpl.getDomainNameWithLike(EdsContextParams.getHostname(), false), user.getEmail(), user.getObjectID(), user.getCompany().getObjectID());
        AccountManagementListItem item = new AccountManagementListItem();
        item.setPassword(user.getOldPassword());
        item.setCompanyID(user.getCompany().getObjectID());
        item.setName(user.getFullName());
        item.setUserId(user.getObjectID());
        if (user != null) {
            item.setActorUsername(user.getFullName());
            item.setActorUserId(user.getObjectID());
            item.setActorCompanyId(user.getCompany() != null ? user.getCompany().getObjectID() : null);
        }
        if (passwordChanged) {
            globalAuthJdbcSpringManager.recordChangePassword(item, user.getPassword());
        }
        if (result != 0) {
            String userAuthDomanNames = globalAuthJdbcSpringManager.updateUserAuthDomainName(user);
            System.out.println("ADD USER TO MULTIPLE HOST: ===>>>>    USERNAME: " + user.getUserName() + " DOMAINNAME: " + userAuthDomanNames);
        }

        if (indexSolr && user instanceof EdsEmployee) {
            try {
                baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, (EdsEmployee) user, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Saves the user related authentication information that all EdsUser ancestors have to use
     * It is used both during the creation process and during persistence data update process
     *
     * @param user      - the EdsUser
     * @param companyId - the company
     */
    public void saveUserAuthenticationData(EdsUser user, Integer companyId, boolean encrypt, boolean passwordChanged) {
        saveUserAuthenticationData(user, companyId, encrypt, true, passwordChanged);
    }

    private void encryptPassword(EdsUser user, boolean encrypt) {
        if (user.getPassword().length() == 128 && encrypt) {//password is already encrypted
            log.error("Password set incorrectly. Username:" + user.getUserName() + ", Email: " + user.getEmail() + ", Password:" + user.getPassword() + ", Encrypt:" + encrypt);
            throw new RuntimeException("Error while create/updating user password");
        }
        if (encrypt) {
            PasswordGenerator pg = new PasswordGenerator(8);
            String salt = pg.generateAsString();
            user.setPassword(EncryptionHelper.sha512(user.getPassword(), salt));
            user.setTempSalt(salt);
        } else {
            user.setTempSalt(null);
        }
    }

    public String getUserStatus(Integer userID) {
        return (String) findSingle("select u.accountStatus.code from EdsUser u where u.objectID=?", userID);
    }
}
