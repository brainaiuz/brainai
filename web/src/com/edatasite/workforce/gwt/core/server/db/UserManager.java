package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import org.springframework.dao.DataAccessException;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 2:10:40 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserManager extends Manager<EdsUser> {

    EdsUser findUser(String userName) throws DataAccessException;

    EdsUser findUser(String userName, Integer companyID) throws DataAccessException;

    EdsUser searchUserByUserName(String userName, Integer companyID) throws DataAccessException;

    EdsUser getUserByEmail(String email) throws NonUniqueResultException;

    EdsUser getUserByUserID(Integer userID);

    List<EdsUser> getUsers();

    void deleteUser(Integer objectID, EdsReference inactive);

    String findActiveAndNonFederateLoginUsers(String domainName, String username);

    Integer findExistingUserName(String userName);

    List<EdsUser> getByIDs(List<Integer> objectIDs);

    List<EdsUser> getByIDs(String ids);

    @Deprecated
    List<EdsUser> findCompanyUsers(String userName);

    List<EdsEmployee> getAdmins(Integer companyID);

    EdsEmployee getAdmin(Integer companyID);

    List<EdsEmployee> getUsersByROLE(Integer companyID, Integer roleID);

    List<EdsEmployee> getUsersByROLE(Integer companyID, Integer roleID, boolean onlyActive);

    List<EdsEmployee> getUsersByROLES(Integer companyID, Integer... roleID);

    List<EdsEmployee> getUsersByROLES(Integer companyID, boolean onlyActive, Integer... roleID);

    List<EdsUser> getCompanyUsers(EdsCompany company);

    List<EdsEmployee> getUsersNotAssigneeTaskPolicy(EdsCompany company);

    EdsUser getUserInSchema(Integer id, String schema);

    EdsUser getUserInSchema(Integer id);

    EdsUser getUserByUserIdAndCompanyId(Integer userId, Integer companyId);

    void changeAccountUserName(Integer userID, Integer companyID, String userName);

    void saveUserAuthenticationData(EdsUser user, Integer companyId);

    void saveUserAuthenticationData(EdsUser user, Integer companyId, boolean encrypt, boolean indexSolr, boolean passwordChanged);

    void saveUserAuthenticationData(EdsUser user, Integer companyId, boolean encrypt, boolean passwordChanged);

    String getUserStatus(Integer userID);

    List<EdsUser> getSchemaAllUsers(String schema);

    List<EdsUser> getSchemaAllUsers(String schema, Integer limit);

    List<EdsUser> getCompanyActiveUsers(Integer companyId);

    void removeEmployeeFromShareReport(Integer employeeID);

    EdsUser getUserByRole(Integer companyID, Integer roleID, boolean onlyActive);

    EdsCompanySettings getCompanySettings(Integer id);
}
