package com.edatasite.workforce.core.tools;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.MassMailParams;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.core.client.enums.UITypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleAuthTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * User: Sherali
 * Date: 4/15/11
 * Time: 12:25 PM
 */
@Repository("globalAuthJdbcSpringManager")
public class GlobalAuthJdbcSpringManagerImpl extends NamedParameterJdbcTemplate implements GlobalAuthJdbcSpringManager {

    private static Logger logger = LoggerFactory.getLogger(GlobalAuthJdbcSpringManagerImpl.class);

    @Autowired
    public GlobalAuthJdbcSpringManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        super(globalauthDataSource);
    }

    @Override
    public UsagePlan getUsagePlane(final Integer subsId, final String customType) {
        Map parameters = new HashMap() {{
            put("subscriptionid", subsId);
            put("type", customType);
        }};
        UsagePlan us = null;
        try {
            us = queryForObject("SELECT * FROM usageplan where subscriptionid=:subscriptionid AND type=:type ", parameters, BeanPropertyRowMapper.newInstance(UsagePlan.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return us;
    }

    //username=? and password=?
    private static final String BY_USERNAME_PASSWORD = "SELECT c.id as clusterID, c.domain as domain, userID, ua.username as username, ua.id as authId, cc.serviceid as serviceid, ua.modificationDate as modificationDate, ua.uitype as uitype, cc.companyid, cc.subdomaincompany, cc.ismaintenance\n" +
            "FROM userAuth ua\n" +
            "INNER JOIN userCompany uc on ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid\n" +
            "INNER JOIN cluster c on c.id = cc.clusterid\n" +
            "WHERE ua.username = :username AND (ua.password = :password OR ua.password = :passwordsha or ua.password = :passwordsha2)\n" +
            "ORDER BY cc.companyid;";
    //username
    private static final String BY_USERNAME_ONLY = "SELECT c.id as clusterID, c.domain as domain, userID, cc.companyid,cc.subdomaincompany ,cc.ismaintenance, ua.username as username, ua.id as authId, cc.serviceid as serviceid, ua.modificationDate as modificationDate, ua.uitype as uitype \n" +
            "FROM userAuth ua\n" +
            "INNER JOIN userCompany uc on ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid\n" +
            "INNER JOIN cluster c on c.id = cc.clusterid\n" +
            "WHERE ua.username = :username \n" +
            "ORDER BY cc.companyid;";

    //useruser
    private static final String USER_BY_USERNAME = "SELECT ua.id as userid \n FROM userAuth ua \n" +
            "WHERE ua.username = :username ;";

    //email
    private static final String BY_USER_EMAIL_ONLY = "SELECT userID, cc.companyid, c.domain, c.id as clusterID,cc.subdomaincompany, cc.ismaintenance, ua.username as username, ua.id as authId, cc.serviceid as serviceid, ua.modificationDate as modificationDate, ua.uitype as uitype " +
            "FROM userCompany uc " +
            "INNER JOIN userauth ua on ua.id = uc.authid " +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid " +
            "INNER JOIN cluster c on c.id = cc.clusterid " +
            "WHERE uc.email =? " +
            "ORDER BY cc.companyid; ";

    //username=? and companyid=?
    private static final String BY_USERNAME_COMPANYID = "SELECT c.id as clusterID, c.domain , userID, cc.companyid,cc.subdomaincompany, cc.ismaintenance, ua.username as username, ua.id as authId, cc.serviceid as serviceid, ua.modificationDate as modificationDate, ua.uitype as uitype \n" +
            "FROM userAuth ua\n" +
            "INNER JOIN userCompany uc on ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid\n" +
            "INNER JOIN cluster c on c.id = cc.clusterid\n" +
            "WHERE ua.username= :username AND cc.companyid= :companyid ORDER BY uc.userid LIMIT 1";

    //userid=? and password=? and companyid=?
    private static final String USER_ENTRY_BY_NAME_PASSWORD_COMPANYID = "SELECT c.id as clusterID, c.domain , userID, cc.companyid,cc.subdomaincompany, cc.ismaintenance, ua.username as username, ua.id as authId, cc.serviceid as serviceid, ua.modificationDate as modificationDate, ua.uitype as uitype\n" +
            "FROM userAuth ua\n" +
            "INNER JOIN userCompany uc on ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid\n" +
            "INNER JOIN cluster c on c.id = cc.clusterid\n" +
            "WHERE ua.username= :username AND (ua.password = :password OR ua.password = :passwordsha)AND cc.companyid= :companyid ORDER BY uc.userid LIMIT 1";

    //userid=? and companyid=?
    private static final String BY_USER_AND_COMPANY = "SELECT ua.username as username\n" +
            "FROM userAuth ua\n" +
            "INNER JOIN userCompany uc on ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid\n" +
            "INNER JOIN cluster c on c.id = cc.clusterid\n" +
            "WHERE cc.companyid= :companyid and uc.userid= :userid ORDER BY uc.id LIMIT 1";


    private static final String ALL_DOMAINS = "SELECT * FROM companydomains;";
    private static final String ALL_WHITE_LABEL_DOMAINS = "SELECT * FROM hostbasedsetting;";

    private static final String DOMAIN_EXISTS = "SELECT companyid FROM clustercompany WHERE googleappdomain=?";
    private static final String IS_SUPERUSER = "SELECT count(*) > 0 FROM superuser su WHERE su.password = :passwordsha";
    private static final String GET_SALT = "SELECT salt FROM userAuth WHERE username = :username";
    private static final String GET_DOMAINNAME = "SELECT domainname FROM userAuth WHERE username = :username";
    private static final String UPDATE_DOMAINNAME = "UPDATE userauth SET domainname = :userAuthDomanNames where username = :username and domainname like :olddomainname";
    private static final String GET_SALT_WITH_USERNAME = "SELECT salt FROM userAuth auth" +
            " JOIN usercompany uc on uc.authid = auth.id" +
            " JOIN clustercompany cc on cc.id = uc.clustercompanyid WHERE username = :username and cc.companyid =:companyid LIMIT 1";
    private static final String UPDATE_COMOPANY_CLUSTER_PARENT = "UPDATE clustercompany SET companyparentId= :companyparentId WHERE companyId= :companyId";
    private static final String UPDATE_USER_EMAIL = "UPDATE usercompany SET email= :email WHERE userid= :userid and clustercompanyid in (SELECT cc.id FROM clustercompany as cc WHERE cc.companyid= :companyid)";

    public boolean getAuthInfo(String domain, String username, String password) {
        // username is case insensitive thus we need to lowercase on each retrival
        final String passwordHash = EncryptionHelper.md5(password);
        Map params = new HashMap() {{
//            put("domainname", getDomainNameWithLike(domain, true));
            put("username", username.toLowerCase());
            put("password", passwordHash);
        }};

        try {
            String salt = queryForObject(GET_SALT, params, String.class);
            final String passwordShaHash = EncryptionHelper.sha512(password, salt);
            params.put("passwordsha", passwordShaHash);

            final String passwordWithOutConst = EncryptionHelper.sha512Second(password, salt);
            params.put("passwordsha2", passwordWithOutConst);

            List<UserCompanyDTO> userCompanyList = (List<UserCompanyDTO>) query(BY_USERNAME_PASSWORD, params, new UserCompanyDTOMapper());
            if (userCompanyList == null || userCompanyList.size() == 0) {
                return false;
            } else {
                changePasswordFromMD5ToSha(username, password, salt, getDomainNameWithLike(domain, true));//THIS IS TEMPORARY MEASURE FOR MIGRATION FROM MD5 TO SHA512+SALT. WILL BE REMOVED
                return true;
            }
        } catch (DataAccessException e) {
            logger.error("Get Auth Info ByUsernameAndPassword Incorrect result size: expected 1, actual 0");
        }
        return false;
    }

    public List<UserCompanyDTO> getAuthInfoByUsernameAndPassword(final String domainName, final String username, final String password) {
        // username is case insensitive thus we need to lowercase on each retrival
        final String passwordHash = EncryptionHelper.md5(password);
        Map params = new HashMap() {{
//            put("domainname", getDomainNameWithLike(domainName, true));
            put("username", username.toLowerCase());
            put("password", passwordHash);
        }};

        List<UserCompanyDTO> userCompanyList = new ArrayList<>();
        try {
            String salt = queryForObject(GET_SALT, params, String.class);
            final String passwordShaHash = EncryptionHelper.sha512(password, salt);
            params.put("passwordsha", passwordShaHash);

            final String passwordWithOutConst = EncryptionHelper.sha512Second(password, salt);
            params.put("passwordsha2", passwordWithOutConst);

            userCompanyList = (List<UserCompanyDTO>) query(BY_USERNAME_PASSWORD, params, new UserCompanyDTOMapper());
            if (userCompanyList == null || userCompanyList.isEmpty()) {
                //check if the password belongs to superuser. Superuser can login into any account
                if (isSuperPassword(password, domainName) && !("lochin.shodiev@workforcetrack.com".equals(username) || "aknbdev".equals(username))) {//Lochin Shodievni accountiga superpasswordni tishi otmaydi
                    logger.info("Superuser logged in to account " + username + ", passwordhash = " + EncryptionHelper.sha512(password, "") + "\n");
                    return getAuthInfoByUsername(domainName, username);
                } else {
                    return userCompanyList;
                }
            } else {
                changePasswordFromMD5ToSha(username, password, salt, getDomainNameWithLike(domainName, true));//THIS IS TEMPORARY MEASURE FOR MIGRATION FROM MD5 TO SHA512+SALT. WILL BE REMOVED
            }
        } catch (DataAccessException e) {
            logger.error("Get Auth Info ByUsernameAndPassword Incorrect result size: expected 1, actual 0");
        }
        return userCompanyList;
    }

    //update userauth set domainname = domainname || '|app.kpi.com|' where domainname like('%|app.kpi.com.ru|%');

    public String updateUserAuthDomainName(EdsUser user) {

        Map<String, String> hashMap = new HashMap<>();
        Map<String, String> myMap = new HashMap<>();

        hashMap.put("domainname", getDomainNameWithLike(EdsContextParams.getHostname(), true));
        hashMap.put("username", user.getUserName().toLowerCase());
        String userAuthDomanNames = queryForObject(GET_DOMAINNAME, hashMap, String.class);
        String oldDomainname = userAuthDomanNames;

        if (userAuthDomanNames.contains("kpi.com.ru|")) {

            if (!userAuthDomanNames.contains("|aws.kpi.com|")) {
                userAuthDomanNames = userAuthDomanNames + "|aws.kpi.com|";
            }
            if (!userAuthDomanNames.contains("|app.kpi.com|")) {
                userAuthDomanNames = userAuthDomanNames + "|app.kpi.com|";
            }

            myMap.put("userAuthDomanNames", userAuthDomanNames);
            myMap.put("username", (user.getUserName() != null ? user.getUserName().trim().toLowerCase() : user.getUserName()));
            myMap.put("olddomainname", oldDomainname);
            update(UPDATE_DOMAINNAME, myMap);

        }

        return userAuthDomanNames;
    }

    public void updateUserEmail(EdsUser user) {
        Map<String, Object> myMap = new HashMap<>();

        myMap.put("email", user.getEmail());
        myMap.put("companyid", Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        myMap.put("userid", user.getObjectID());
        update(UPDATE_USER_EMAIL, myMap);
    }
    public void patchForGA(String sql, Object... values) {
        try {
            getJdbcOperations().update(sql, values);
        } catch (Exception e) {
            throw new RuntimeException("Patch execution failed: " + sql, e);
        }
    }

    @Override
    public ArrayList<CompanyDomain> getFingerprintSetup(Integer companyID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM companydomains cd ");
        sql.append(" WHERE company_unique_key is not null and cd.companyid=").append(companyID);
        sql.append(" ORDER BY cd.domain");
        return (ArrayList<CompanyDomain>) getJdbcOperations().query(sql.toString(), new CompanyDomainMapper());
    }

    @Override
    public CompanyDomain getFingerprintSetupById(Integer id) {
        String sql = "SELECT * FROM companydomains cd WHERE cd.id = " + id;
        return (CompanyDomain) getJdbcOperations().queryForObject(sql, new CompanyDomainMapper());
    }

    @Override
    public MassMailParams getMassMailParamsByCompany(final Integer companyID) {
        Map parameters = new HashMap() {{
            put("companyid", companyID);
            put("companyhost", EdsContextParams.getHostname());
        }};
        MassMailParams mmp = null;
        try {
            mmp = (MassMailParams) queryForObject("SELECT * FROM massmailparams where companyid=:companyid ", parameters, new MassMailParamsMapper());
        } catch (EmptyResultDataAccessException e) {
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (mmp == null) {
            try {
                mmp = (MassMailParams) queryForObject("SELECT * FROM massmailparams where companyhost=:companyhost ", parameters, new MassMailParamsMapper());
            } catch (EmptyResultDataAccessException e) {
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        if (mmp == null) {
            parameters.put("companyhost", "app.kpi.com");
            try {
                mmp = (MassMailParams) queryForObject("SELECT * FROM massmailparams where companyhost=:companyhost ", parameters, new MassMailParamsMapper());
            } catch (EmptyResultDataAccessException e) {
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        return mmp;
    }

    //Should be removed after all the users will have SHA encryption. Added on 17.12.12
    public void changePasswordFromMD5ToSha(String username, String password, String salt, String domainName) {
        if (salt != null && !"".equals(salt)) {
            return;
        }
        PasswordGenerator pg = new PasswordGenerator(8);
        salt = pg.generateAsString();
        getJdbcOperations().update("UPDATE userauth SET password = ?, salt = ? WHERE username = ? and length(password) < 128;", EncryptionHelper.sha512(password, salt), salt, (username != null ? username.trim().toLowerCase() : username));
    }

    public static String getDomainNameWithLike(String domainName, boolean isCriteria) {
        if (domainName != null && !"".equals(domainName)) {
            if (!domainName.matches("^\\|.*\\|$")) {
                domainName = "|" + domainName + "|";
                if (isCriteria && !domainName.contains("%")) {
                    domainName = "%" + domainName + "%";
                }
            }
        }
        return domainName;
    }

    public boolean isSuperPassword(final String password, final String domainName) {
        return queryForObject(IS_SUPERUSER, new HashMap() {{
            put("passwordsha", EncryptionHelper.sha512(password, ""));
//            put("domainname", domainName);
        }}, Boolean.class);
    }

    public List<UserCompanyDTO> getAuthInfoByUsername(final String domainName, final String username) {
        // username is case insensitive thus we need to lowercase on each retrival
        String usernameLower = username.toLowerCase();
        Map params = new HashMap() {{
//            put("domainname", getDomainNameWithLike(domainName, true));
            put("username", usernameLower);
        }};
        List<UserCompanyDTO> dbName = new ArrayList<>();
        try {
            dbName = (List<UserCompanyDTO>) query(BY_USERNAME_ONLY, params, new UserCompanyDTOMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return dbName;
    }

    public UserCompanyDTO getUserInfoByUserNamePasswordAndCompany(final String username, final String password, final Integer companyid) {
        // username is case insensitive thus we need to lowercase on each retrival
        final String passwordHash = EncryptionHelper.md5(password);
        UserCompanyDTO userCompany = null;
        Map params = new HashMap() {{
            put("username", username);
            put("companyid", companyid);
            put("password", passwordHash);
        }};
        try {
            String salt = queryForObject(GET_SALT_WITH_USERNAME, params, String.class);
            final String passwordShaHash = EncryptionHelper.sha512(password, salt);
            params.put("passwordsha", passwordShaHash);
            userCompany = (UserCompanyDTO) queryForObject(USER_ENTRY_BY_NAME_PASSWORD_COMPANYID, params, new UserCompanyDTOMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return userCompany;
    }

    /**
     * Get username
     *
     * @param companyID
     * @param userID
     * @return
     */
    public String getUsername(Integer companyID, Integer userID) {
        UserCompanyDTO userCompany;
        Map params = new HashMap() {{
            put("companyid", companyID);
            put("userid", userID);
        }};
        try {
            userCompany = (UserCompanyDTO) queryForObject(BY_USER_AND_COMPANY, params, new UsernameMapper());
            return userCompany.getUserName();
        } catch (EmptyResultDataAccessException e) {
            logger.error("Company ID: " + companyID + "===== UserID: " + userID + "==================== EmptyResultDataAccessException....=================");
        } catch (DataAccessException e) {
            logger.error("", e);
        }
        return "";
    }

    public Boolean createPushNotificationToken(String domainName, String username, String firebasePushNotificationToken, String devicetype, String module) {

        Integer tokenId = 0;

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(firebasePushNotificationToken)) {
            Integer userId = 0;
            try {

                Map params = new HashMap() {{
//                    put("domainname", getDomainNameWithLike(domainName, true));
                    put("username", username.toLowerCase());
                }};

                userId = queryForObject(USER_BY_USERNAME, params, Integer.class);
            } catch (Exception ex) {
                logger.error("User not found in database! username=" + username);
            }
            if (userId > 0) {
                final Integer userAuthId = userId;
                try {
                    getJdbcOperations().update(connection -> {
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO push_notification_token (userid, token, devicetype, modulecode, deleted) VALUES (?, ?, ?, ?, ?)");
                        statement.setInt(1, userAuthId);
                        statement.setString(2, firebasePushNotificationToken);
                        statement.setString(3, devicetype);
                        statement.setString(4, module);
                        statement.setBoolean(5, Boolean.FALSE);
                        return statement;
                    });
                } catch (Exception e) {
                    logger.error("", e);
                }
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean deletePushNotificationToken(String firebasePushNotificationToken, String devicetype) {

        Integer tokenId = 0;

        if (StringUtils.isNotBlank(firebasePushNotificationToken)) {
            try {
                getJdbcOperations().update(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                            PreparedStatement statement = connection.prepareStatement("DELETE FROM push_notification_token WHERE userid = ? and token = ? ");
                        PreparedStatement statement = connection.prepareStatement("DELETE FROM push_notification_token WHERE token = ? AND devicetype = ?");
                        statement.setString(1, firebasePushNotificationToken);
                        statement.setString(2, devicetype);
                        return statement;
                    }
                });
            } catch (Exception e) {
                logger.error("", e);
            }
            return Boolean.TRUE;

        }
        return Boolean.FALSE;
    }

    public UserCompanyDTO getUserInfoByUserNameAndCompany(final String domainName, final String username, final Integer companyid) {

        UserCompanyDTO userCompany = null;
        Map params = new HashMap() {{
            put("companyid", companyid);
            put("username", username);
        }};
        try {
            userCompany = (UserCompanyDTO) queryForObject(BY_USERNAME_COMPANYID, params, new UserCompanyDTOMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return userCompany;
    }

    @Override
    public void blockLogin(final String login) {
        final String sql = "INSERT INTO blocked (username) VALUES (?)";
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, login);
                return statement;
            }
        });

    }

    @Override
    public boolean isLoginBlocked(final String login) {
        String sql = """
                    SELECT (NOW() - (block_time AT TIME ZONE 'UTC') < INTERVAL '14 minutes') AS blocked
                    FROM blocked
                    WHERE username = :login
                    ORDER BY block_time DESC
                    LIMIT 1
                """;

        try {
            Boolean blocked = queryForObject(sql, Map.of("login", login), Boolean.class);
            return Boolean.TRUE.equals(blocked);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public DomainInfo findByGoogleAppDomain(final String domainName, final String googleAppDomain, final String email) {
        String checkdDomainExistence = "SELECT companyid FROM clustercompany WHERE googleappdomain=:googleappdomain";
        Map params = new HashMap() {{
            put("googleappdomain", googleAppDomain);
//            put("domainname", getDomainNameWithLike(domainName, true));
            put("username", email.toLowerCase());
        }};

        Integer companyID = null, authid = null;

        try {
            companyID = queryForObject(checkdDomainExistence, params, Integer.class);
            params.put("companyid", companyID);
        } catch (DataAccessException ex) {
//            ex.printStackTrace();
            logger.info("Company bound to google apps domain was not found!" + googleAppDomain);
        }

        try {
            authid = queryForObject("SELECT ua.id FROM userAuth ua INNER JOIN usercompany uc on uc.authid = ua.id INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid WHERE ua.username = :username and cc.companyid = :companyid", params, Integer.class);
        } catch (DataAccessException ex) {
            //ex.printStackTrace();
            logger.info("User not found in database! " + email);
        }

        return new DomainInfo(companyID != null, authid != null);

    }

    public CompanyDomain findByGoogleAppDomain(final String domain) {

        String sql = "SELECT * FROM (SELECT cd.id as objectID, cd.companyid as companyID, cd.googleappdomain as domain, c.id as clusterID, c.domain as clusterDbName, cd.gadgetenabled FROM clustercompany cd \n" +
                "                INNER JOIN cluster c ON c.id = cd.clusterid) p \n" +
                "                WHERE p.domain=:domain";
        CompanyDomain result = null;
        try {
            result = queryForObject(sql, new HashMap() {{
                put("domain", domain);
            }}, BeanPropertyRowMapper.newInstance(CompanyDomain.class));
        } catch (EmptyResultDataAccessException ex) {

        }
        return result;
    }

    public String getUserDatabaseName(final Integer userid, final Integer companyid) {

        Map params = new HashMap() {{
            put("userid", userid);
            put("companyid", companyid);
        }};
        String query = "SELECT c.domain FROM usercompany  uc " +
                "LEFT JOIN clustercompany cl ON cl.id = uc.clustercompanyid " +
                "LEFT JOIN cluster c ON c.id = cl.clusterid " +
                "WHERE uc.userid = :userid AND cl.companyid = :companyid ORDER BY uc.id DESC LIMIT 1";
        String dbName = null;
        try {
            dbName = queryForObject(query, params, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (dbName != null && !"".equals(dbName)) {
            return dbName;
        } else {
            return null;
        }
    }

    public String getCompanyDatabaseName(final Integer companyid) {
        Map params = new HashMap() {{
            put("companyid", companyid);
        }};
        String query = "SELECT c.domain FROM clustercompany  cl " +
                "LEFT JOIN cluster c ON c.id = cl.clusterid " +
                "WHERE cl.companyid = :companyid";

        String dbName = null;
        try {
            dbName = queryForObject(query, params, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (dbName != null && !"".equals(dbName)) {
            return dbName;
        } else {
            return null;
        }
    }

    public String getCompanyServiceId(final Integer companyid) {
        Map params = new HashMap() {{
            put("companyid", companyid);
        }};
        String query = "SELECT cl.serviceid FROM clustercompany cl " +
                "WHERE cl.companyid = :companyid";

        try {
            return queryForObject(query, params, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getClassicUIHost(Integer companyId) {
        Map params = new HashMap() {{
            put("companyId", companyId);
        }};
        String query = "SELECT cc.classic_ui_host FROM clustercompany  cc " +
                "WHERE cc.companyid = :companyId";

        String host = null;
        try {
            host = queryForObject(query, params, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(host)) {
            return host;
        } else {
            return null;
        }
    }

    @Override
    public String getLightUIHost(Integer companyId) {
        Map params = new HashMap() {{
            put("companyId", companyId);
        }};
        String query = "SELECT cc.light_ui_host FROM clustercompany  cc " +
                "WHERE cc.companyid = :companyId";

        String host = null;
        try {
            host = queryForObject(query, params, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(host)) {
            return host;
        } else {
            return null;
        }
    }

    @Override
    public CompanyDomain getCompanyDomain(final String domain) {
        if (EdsContextParams.companyDomainMap.containsKey(domain)) {
            return EdsContextParams.companyDomainMap.get(domain);
        }
        Map parameters = new HashMap() {{
            put("domain", domain);

        }};

        String sql = "SELECT cd.id objectID, cd.companyid companyID, cd.domain, cd.website_number as websiteNumber, c.id clusterID, c.domain clusterDbName FROM companydomains cd " +
                "INNER JOIN clustercompany cc ON cc.companyid = cd.companyid " +
                "INNER JOIN cluster c ON c.id = cc.clusterid " +
                "WHERE cd.domain=:domain ";

        try {
            CompanyDomain companyDomain = queryForObject(sql, parameters, BeanPropertyRowMapper.newInstance(CompanyDomain.class));
            EdsContextParams.companyDomainMap.put(domain, companyDomain);
            return companyDomain;
        } catch (Exception e) {
            EdsContextParams.companyDomainMap.put(domain, null);
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public CompanyDomain getCompanyDomainByWN(final String number) {
        Map parameters = new HashMap() {{
            put("number", number);

        }};

        String sql = "SELECT cd.id objectID, cd.companyid companyID, cd.domain, cd.website_number as websiteNumber, c.id clusterID, c.domain clusterDbName FROM companydomains cd " +
                "INNER JOIN clustercompany cc ON cc.companyid = cd.companyid " +
                "INNER JOIN cluster c ON c.id = cc.clusterid " +
                "WHERE cd.website_number=:number ";

        try {
            return queryForObject(sql, parameters, BeanPropertyRowMapper.newInstance(CompanyDomain.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public CompanyDomain getCompanyIdByUniqueKey(final String uniqueKey) {
        Map parameters = new HashMap() {{
            put("uniqueKey", uniqueKey);

        }};
        String sql = "SELECT distinct cd.* FROM companydomains cd " +
                "INNER JOIN clustercompany cc ON cc.companyid = cd.companyid " +
                "INNER JOIN cluster c ON c.id = cc.clusterid " +
                "WHERE cd.company_unique_key =:uniqueKey";

        try {
            return (CompanyDomain) queryForObject(sql, parameters, new CompanyDomainMapper());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @param companyID
     * @return LinkedHashMap<String, String>. key=deviceKey,value=device name
     */
    @Override
    public LinkedHashMap<String, String> getCompanyUniqueKeyListMap(Integer companyID) {
        Map paramMap = new HashMap() {{
            put("companyId", companyID);

        }};
        String sql = "SELECT distinct cd.* FROM companydomains cd " +
                "INNER JOIN clustercompany cc ON cc.companyid = cd.companyid " +
                "INNER JOIN cluster c ON c.id = cc.clusterid " +
                "WHERE cd.company_unique_key is not null and cd.companyid =:companyId";

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        try {
            List<CompanyDomain> result = (List<CompanyDomain>) query(sql, paramMap, new CompanyDomainMapper());
            for (CompanyDomain companyDomain : result) {
                map.put(companyDomain.getCompanyUniqueID(), companyDomain.getCompanyBranchName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return map;
    }

    @Override
    public void insertEdsUsagePlanList(final List<EdsUsagePlan> edsUsagePlanList) {
        String sql = "INSERT INTO usageplan (subscriptionid, companyid, type) VALUES (?, ?, ?)";

        getJdbcOperations().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EdsUsagePlan usagePlan = edsUsagePlanList.get(i);
                ps.setLong(1, usagePlan.getObjectID());
                ps.setInt(2, usagePlan.getCompany().getObjectID());
                ps.setString(3, "add");
            }

            @Override
            public int getBatchSize() {
                return edsUsagePlanList.size();
            }
        });
    }

    @Override
    public void insertEdsSubscriptionHistoryList(final List<EdsSubscriptionHistory> edsSubscriptionHistoryList) {
        String sql = "INSERT INTO usageplan (subscriptionid, companyid, type) VALUES (?, ?, ?)";

        getJdbcOperations().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EdsSubscriptionHistory subscriptionHistory = edsSubscriptionHistoryList.get(i);
                ps.setLong(1, subscriptionHistory.getObjectID());
                ps.setInt(2, subscriptionHistory.getUsagePlan().getCompany().getObjectID());
                ps.setString(3, "upg");
            }

            @Override
            public int getBatchSize() {
                return edsSubscriptionHistoryList.size();
            }
        });
    }

    public ApiAccessToken getApiAccessToken(String accessToken) {
        HashMap parameters = new HashMap() {{
            put("token", accessToken);
        }};

        ApiAccessToken apiAccessToken = null;
        try {
            apiAccessToken = queryForObject("SELECT * FROM apiaccesstoken where token=:token AND (blocked is null or blocked is not true) ", parameters, BeanPropertyRowMapper.newInstance(ApiAccessToken.class));
        } catch (DataAccessException e) {
            logger.error("", e);
        }
        return apiAccessToken;

    }

    public ApiAccessToken getApiAccessTokenByID(Integer id) {
        HashMap parameters = new HashMap() {{
            put("id", id);
        }};

        ApiAccessToken apiAccessToken = null;
        try {
            apiAccessToken = queryForObject("SELECT * FROM apiaccesstoken where id=:id", parameters, BeanPropertyRowMapper.newInstance(ApiAccessToken.class));
        } catch (DataAccessException e) {
            logger.error("", e);
        }
        return apiAccessToken;

    }

    public ListResult<ApiAccessToken> getApiAccessTokenList(ListingFilterParameter fp) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT act.* FROM apiaccesstoken act");
        sql.append(" WHERE 1=1 ");
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" AND LOWER(act.description) LIKE '").append(fp.getSqlSearchKey()).append("'");
        }

        Integer totalCount = getJdbcOperations().query(sql.toString(), new ApiAccessTokenMapper()).size();

        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }

        List<ApiAccessToken> list = getJdbcOperations().query(sql.toString(), new ApiAccessTokenMapper());
        ArrayList<ApiAccessToken> arrayList = new ArrayList<>(list);
        return new ListResult<>(arrayList, totalCount);
    }

    @Override
    public Boolean saveAccessToken(ApiAccessToken accessToken) {
        try {
            if (accessToken.getId() != null) {
                String sqlUpdate = "UPDATE apiaccesstoken set description =?, blocked =?, modulecode =? where id =?";
                getJdbcOperations().update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(sqlUpdate);
                    statement.setString(1, accessToken.getDescription());
                    statement.setBoolean(2, accessToken.getBlocked());
                    statement.setString(3, accessToken.getModuleCode());
                    statement.setInt(4, accessToken.getId());
                    return statement;
                });
                return Boolean.TRUE;
            } else {
                String sql = "INSERT INTO apiaccesstoken (token, description, blocked, modulecode) VALUES (?, ?, ?, ?)";
                getJdbcOperations().update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, accessToken.getToken());
                    statement.setString(2, accessToken.getDescription());
                    statement.setBoolean(3, accessToken.getBlocked());
                    statement.setString(4, accessToken.getModuleCode());
                    return statement;
                });

                return Boolean.TRUE;
            }
        } catch (DataAccessException e) {
            logger.error("", e);
            return Boolean.FALSE;
        }
    }


    @Override
    public String insertCompanyDeviceIdByCompanyId(final Integer companyId, final CompanyDomain companyDomain) {
        if (companyDomain.getCompanyUniqueID() != null && !"".equals(companyDomain.getCompanyUniqueID()) && getCompanyIdByUniqueKey(companyDomain.getCompanyUniqueID()) == null) {
            final String sql = "INSERT INTO companydomains (companyid, domain, website_number, company_unique_key, company_branch_name,dynamicStatus) VALUES (?, ?, ?, ?, ?, ?)";
            getJdbcOperations().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, companyId);
                    statement.setString(2, companyDomain.getDomain() != null ? companyDomain.getDomain() : "");
                    statement.setString(3, companyDomain.getWebsiteNumber() != null ? companyDomain.getWebsiteNumber() : "");
                    statement.setString(4, companyDomain.getCompanyUniqueID() != null ? companyDomain.getCompanyUniqueID() : "");
                    statement.setString(5, companyDomain.getCompanyBranchName() != null ? companyDomain.getCompanyBranchName() : "");
                    statement.setBoolean(6, companyDomain.getDynamicStatus() != null ? companyDomain.getDynamicStatus() : false);
                    return statement;
                }
            });
            return null;
        }
        return companyDomain.getCompanyUniqueID();
    }

    @Override
    public Integer saveFingerprintSetup(CompanyDomain companyDomain) {
        if (companyDomain.getObjectID() != null)
            return updateFingerprintSetup(companyDomain);
        return insertCompanyDeviceIdByCompanyId(companyDomain);
    }

    private int updateFingerprintSetup(CompanyDomain companyDomain) {
        String sql = "update companydomains set company_unique_key = ?, company_branch_name = ?, dynamicstatus = ? where id = ?";
        return getJdbcOperations().update(sql, companyDomain.getCompanyUniqueID(), companyDomain.getCompanyBranchName(), companyDomain.getDynamicStatus(), companyDomain.getObjectID());
    }

    private int insertCompanyDeviceIdByCompanyId(CompanyDomain companyDomain) {
        if (companyDomain.getCompanyUniqueID() == null || companyDomain.getCompanyUniqueID().isEmpty() || getCompanyIdByUniqueKey(companyDomain.getCompanyUniqueID()) != null) {
            return 0;
        }
        final String sql = "INSERT INTO companydomains (companyid, domain, website_number, company_unique_key, company_branch_name,dynamicStatus) VALUES (?, ?, ?, ?, ?, ?) returning id";
        return getJdbcOperations().queryForObject(sql, Integer.class,
                companyDomain.getCompanyID(),
                companyDomain.getDomain() != null ? companyDomain.getDomain() : "",
                companyDomain.getWebsiteNumber() != null ? companyDomain.getWebsiteNumber() : "",
                companyDomain.getCompanyUniqueID() != null ? companyDomain.getCompanyUniqueID() : "",
                companyDomain.getCompanyBranchName() != null ? companyDomain.getCompanyBranchName() : "",
                companyDomain.getDynamicStatus() != null ? companyDomain.getDynamicStatus() : false);
    }

    @Override
    public void deleteCompanyDomainByCompanyId(final Integer companyId) {
        final String sql = "DELETE FROM companydomains WHERE company_unique_key is not null AND companyid = ? ";
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, companyId);
                return statement;
            }
        });
    }

    @Override
    public List<UserCompanyDTO> getUserCompanyByEmail(String domainName, final String email) {
//        domainName = getDomainNameWithLike(domainName, true);
        return (List<UserCompanyDTO>) getJdbcOperations().query(BY_USER_EMAIL_ONLY, new UserCompanyDTOMapper(), email);
    }

    @Override
    public List<Integer> getUserCompanyIdsByEmailAndUserId(String email, Integer userId) {
        StringBuilder query = new StringBuilder()
                .append("SELECT cc.companyid FROM userCompany uc ")
                .append("INNER JOIN clustercompany cc on cc.id = uc.clustercompanyid ")
                .append("INNER JOIN cluster c on c.id = cc.clusterid ")
                .append("WHERE uc.authId =(").append("SELECT authId from userCompany ")
                .append("WHERE email='").append(email).append("' AND ").append("userId='").append(userId).append("' LIMIT 1)");
        return (List<Integer>) getJdbcOperations().queryForList(query.toString(), Integer.class);
    }

    @Override
    public void registerCompanyToLoginDispatcher(final List<Integer> companyList) {
        String sql = "SELECT insertClusterCompany(?,?)";

        for (Integer id : companyList) {
            getJdbcOperations().queryForMap(sql, TenantContextHolder.PAID_DB, id);
        }
    }

    @Override
    public void registerCompanyToLoginDispatcherDummy(final List<CompanyidDomain> companyList) {
        String sql = "SELECT insertClusterCompany(?,?,?)";
        for (CompanyidDomain id : companyList) {
            getJdbcOperations().queryForMap(sql, TenantContextHolder.PAID_DB, id.companyid, id.domain);
        }
    }

    class UserCompanyDTOMapper implements RowMapper {
        public UserCompanyDTOMapper() {
        }

        public UserCompanyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserCompanyDTO userCompanyDTO = new UserCompanyDTO();
            userCompanyDTO.setAuthId(rs.getInt("authId"));
            userCompanyDTO.setUserID(rs.getInt("userID"));
            userCompanyDTO.setCompanyID(rs.getInt("companyid"));
            userCompanyDTO.setClusterDbName(rs.getString("domain"));
            userCompanyDTO.setClusterID(rs.getInt("clusterID"));
            userCompanyDTO.setMaintance(rs.getBoolean("isMaintenance"));
            userCompanyDTO.setUserName(rs.getString("username"));
            userCompanyDTO.setServiceID(rs.getString("serviceid"));
            userCompanyDTO.setModificationDate(rs.getDate("modificationDate"));
            userCompanyDTO.setUitype(rs.getString("uitype"));

            if (hasColumn(rs, "subdomaincompany")) {
                userCompanyDTO.setSubdomainCompany(rs.getString("subdomaincompany"));
            }
            userCompanyDTO.setClusterURL(ServerUtils.getWebURL(userCompanyDTO));
            return userCompanyDTO;
        }
    }

    static class UsernameMapper implements RowMapper {
        public UsernameMapper() {
        }

        public UserCompanyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserCompanyDTO userCompanyDTO = new UserCompanyDTO();
            userCompanyDTO.setUserName(rs.getString("username"));
            return userCompanyDTO;
        }
    }

    static class ApiAccessTokenMapper implements RowMapper {
        public ApiAccessTokenMapper() {
        }

        public ApiAccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            ApiAccessToken apiAccessToken = new ApiAccessToken();
            apiAccessToken.setId(rs.getInt("id"));
            apiAccessToken.setToken(rs.getString("token"));
            apiAccessToken.setDescription(rs.getString("description"));
            apiAccessToken.setModuleCode(rs.getString("moduleCode"));
            apiAccessToken.setBlocked(rs.getBoolean("blocked"));

            return apiAccessToken;
        }
    }

    private boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    static class UserRateBuilder implements RowMapper {
        @Override
        public UserRateItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRateItem userRateItem = new UserRateItem();
            userRateItem.setDiscountOneMonth(rs.getDouble("oneMonth"));
            userRateItem.setDiscountThreeMonth(rs.getDouble("threeMonth"));
            userRateItem.setDiscountSixMonth(rs.getDouble("sixMonth"));
            userRateItem.setDiscountTwentyMonth(rs.getDouble("twentyMonth"));
            return userRateItem;
        }
    }

    static class PricePerPricingPackage implements RowMapper {
        @Override
        public UserRateItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserRateItem pricePerPackage = new UserRateItem();
            pricePerPackage.setPricePerPackage(rs.getDouble("price"));
            pricePerPackage.setUserCountMinOneMonth(rs.getInt("min_one_month"));
            pricePerPackage.setUserCountMinThreeMonth(rs.getInt("min_three_month"));
            pricePerPackage.setUserCountMinSixMonth(rs.getInt("min_six_month"));
            pricePerPackage.setUserCountMaxTwentyMonth(rs.getInt("max_twenty_month"));
            pricePerPackage.setCount(rs.getInt("count"));
            pricePerPackage.setDiscountOneMonth(rs.getDouble("onemonth"));
            pricePerPackage.setDiscountThreeMonth(rs.getDouble("threemonth"));
            pricePerPackage.setDiscountSixMonth(rs.getDouble("sixmonth"));
            pricePerPackage.setDiscountTwentyMonth(rs.getDouble("twentymonth"));
            return pricePerPackage;
        }
    }

    public static class SubsidiariesCompanyBuilder implements RowMapper {

        @Override
        public ConsolidationCompanyList mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConsolidationCompanyList consolidation = new ConsolidationCompanyList();
            consolidation.setCompanyId(rs.getInt("companyId"));
            consolidation.setClusterCompanyId(rs.getInt("id"));
            consolidation.setDataBaseName(rs.getString("domain"));
            return consolidation;
        }
    }

    public static class CompaniesClusterTypeBuilder implements RowMapper {

        @Override
        public SelectItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            SelectItem selectItem = new SelectItem();
            selectItem.setId(rs.getInt("companyid"));
            selectItem.setName(rs.getString("domain"));
            return selectItem;
        }
    }


    public Boolean isEnabledAdvancedPassword(String companyIDs) {
        Boolean enabled = false;
        if ("".equals(companyIDs)) {
            return enabled;
        }
        String sql = "SELECT cd.* FROM companydomains cd WHERE cd.companyid in (" + companyIDs + ")";
        List<CompanyDomain> companyDomainList = getJdbcOperations().query(sql, new CompanyDomainMapper());
        if (companyDomainList != null && companyDomainList.size() != 0) {
            for (CompanyDomain company : companyDomainList) {
                if (company.getEnabledAdvancedPassword() != null && company.getEnabledAdvancedPassword()) {
                    enabled = company.getEnabledAdvancedPassword();
                }
            }
        }
        return enabled;
    }

    public static class MassMailParamsMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            MassMailParams massMailParams = new MassMailParams();
            massMailParams.setCompanyHost(rs.getString("companyHost"));
            massMailParams.setCompanyID(rs.getInt("companyID"));
            massMailParams.setHost(rs.getString("host"));
            massMailParams.setPort(rs.getString("port"));
            massMailParams.setLogin(rs.getString("login"));
            massMailParams.setPassword(rs.getString("password"));
            massMailParams.setBouncedEmail(rs.getString("bouncedEmail"));
            massMailParams.setBouncedPassword(rs.getString("bouncedPassword"));
            massMailParams.setAbuseEmail(rs.getString("abuseEmail"));
            massMailParams.setTolerateText(rs.getString("tolerateText"));
            massMailParams.setTolerateHtml(rs.getString("tolerateHtml"));
            massMailParams.setUnsubscribeText(rs.getString("unsubscribeText"));
            massMailParams.setUnsubscribeHtml(rs.getString("unsubscribeHtml"));
            massMailParams.setSsl(rs.getBoolean("ssl"));
            massMailParams.setSmtpAuth(rs.getBoolean("smtpAuth"));
            return massMailParams;
        }
    }


    public static class WhiteLabelDomainMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            CompanyDomain companyDomain = new CompanyDomain();
            companyDomain.setDomain(rs.getString("hostname"));
            companyDomain.setObjectID(rs.getInt("id"));
            return companyDomain;
        }
    }

    public void updateCompanyDomainInfo(final Integer companyid, final String domain) {
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("UPDATE clustercompany SET googleappdomain =? WHERE  companyid =?");
                statement.setString(1, domain);
                statement.setInt(2, companyid);
                return statement;
            }
        });
    }

    @Override
    public void registerCompanyHost(Integer companyId, String host) {
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("UPDATE clustercompany SET light_ui_host = ? WHERE  companyid = ?");
                statement.setString(1, host);
                statement.setInt(2, companyId);
                return statement;
            }
        });
    }

    @Override
    public void registerCompanyService(Integer companyId, String serviceId) {
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("UPDATE clustercompany SET serviceid = ? WHERE  companyid = ?");
                statement.setString(1, serviceId);
                statement.setInt(2, companyId);
                return statement;
            }
        });
    }

    @Override
    public AccountManagementListItem getValidUser(AccountManagementListItem account) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT uc.id as userCompanyId, uc.userid as userid, ua.id as authid, ua.username as login, ua.password as password,ua.modificationDate as modificationDate FROM userCompany uc ");
        sql.append("INNER JOIN clustercompany cc ON cc.id = uc.clustercompanyid ");
        sql.append("INNER JOIN userauth ua ON ua.id = uc.authid ");
        sql.append("WHERE cc.companyid = '" + account.getCompanyID() + "' ");
        sql.append(" AND uc.userid = '" + account.getUserId() + "' limit 1");

        try {
            return getJdbcOperations().queryForObject(sql.toString(), BeanPropertyRowMapper.newInstance(AccountManagementListItem.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Update user password by userauthid.
     *
     * @param item     userauth id
     * @param password user new password
     */
    @Override
    public void chageAccountPasswordByAuthId(final AccountManagementListItem item, String password) {
        PasswordGenerator pg = new PasswordGenerator(8);
        final String salt = pg.generateAsString();
        final String passwordEnc = EncryptionHelper.sha512(password, salt);
        recordChangePassword(item, passwordEnc);

        getJdbcOperations().update(connection -> {
            PreparedStatement statement = connection.prepareStatement("UPDATE userauth SET password = ?, modificationDate = ?, salt = ? WHERE id = ? ");
            statement.setString(1, passwordEnc);
            statement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            statement.setString(3, salt);
            statement.setInt(4, item.getAuthID());
            return statement;
        });

    }

    @Override
    public void recordChangePassword(AccountManagementListItem item, String passwordEnc) {
        getJdbcOperations().update(connection -> {
            PreparedStatement statement = connection.prepareStatement("insert into password_change(company_id, user_name, user_id, old_password, new_password, actor_username, actor_company_id, actor_user_id, action_date) values (?,?,?,?,?,?,?,?,?)");
            statement.setInt(1, item.getCompanyID());
            statement.setString(2, item.getName());
            statement.setInt(3, item.getUserId());
            statement.setString(4, item.getPassword());
            statement.setString(5, passwordEnc);
            statement.setString(6, item.getActorUsername());
            statement.setInt(7, item.getActorCompanyId());
            statement.setInt(8, item.getActorUserId());
            statement.setDate(9, new Date(System.currentTimeMillis()));
            return statement;
        });
    }

    @Override
    public List<String> getLastPasswordList(Integer userID) {
        String query = "select new_password from password_change where user_id=" + userID + " order by id desc limit 4";
        return getJdbcOperations().queryForList(query, String.class);
    }

    @Override
    public Vector<String> getAllDomains() {
        Vector<String> domains = new Vector<>();
        List<CompanyDomain> listCompanyDomains = (List<CompanyDomain>) getJdbcOperations().query(ALL_DOMAINS, new CompanyDomainMapper());
        for (CompanyDomain companyDomain : listCompanyDomains) {
            domains.add(companyDomain.getDomain());
        }
        return domains;
    }

    public Vector<String> getAllWhiteLabelDomains() {
        Vector<String> domains = new Vector<>();
        List<CompanyDomain> listWhiteLabelDomains = (List<CompanyDomain>) getJdbcOperations().query(ALL_WHITE_LABEL_DOMAINS, new WhiteLabelDomainMapper());
        for (CompanyDomain companyDomain : listWhiteLabelDomains) {
            domains.add(companyDomain.getDomain());
        }
        return domains;
    }

    public void changeAccountUserName(final Integer userCompanyId, final String userName) {
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("UPDATE userauth SET username =? WHERE id = (SELECT authid FROM usercompany WHERE id = ?)");
                statement.setString(1, (userName != null ? userName.trim().toLowerCase() : userName));
                statement.setInt(2, userCompanyId);
                return statement;
            }
        });
    }

    public void changeExistingACcountCompany(final Integer existingAuthId, final Integer userId, final Integer companyId, final Integer oldAuthId) {
        final String sql = "update usercompany set authid = ? where userid = ? and authid = ? and clustercompanyid = (select id from clustercompany where companyid = ?);";
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, oldAuthId);
                statement.setInt(2, userId);
                statement.setInt(3, existingAuthId);
                statement.setInt(4, companyId);
                return statement;
            }
        });
    }

    @Override
    public void changeMaintenanceStatus(Integer companyID) {
        String sql = "UPDATE clustercompany SET isMaintenance=not isMaintenance WHERE companyid = '" + companyID + "';";
        getJdbcOperations().update(sql);
    }

    @Override
    public void deleteCompanyUser(Integer userId, Integer companyId) {
        String deleteUserCompany = "DELETE FROM usercompany WHERE userid=? AND clustercompanyid in (SELECT id FROM clustercompany WHERE companyId=?)";
        String deleteGoogleGadgetAuth = "DELETE FROM googlegadgetauth WHERE userauthid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany WHERE authid IS NOT NULL))";
        String deletePushNotificationToken = "DELETE FROM push_notification_token WHERE userid IN (SELECT id FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany WHERE authid IS NOT NULL))";
        String deleteUserAuth = "DELETE FROM userauth WHERE id NOT IN (SELECT authid FROM usercompany WHERE authid IS NOT NULL)";
        getJdbcOperations().update(deleteUserCompany, userId, companyId);
        getJdbcOperations().update(deleteGoogleGadgetAuth);
        getJdbcOperations().update(deletePushNotificationToken);
        getJdbcOperations().update(deleteUserAuth);
    }

    @Override
    public void deleteCompanyFromGlobalAuth(Integer companyID) {
        getJdbcOperations().queryForMap("SELECT removeCompanyGlobalFunc('" + companyID + "')");
    }

    public List<Map<String, Object>> getCompanyAuthList(Integer companyId) {
        String sql = "select cc.id as clustercompanyid,cc.companyid as companyid,uc.userid,uc.authid,uc.email,ua.username,ua.password,ua.domainname from clustercompany as cc\n" +
                "left join usercompany as uc on uc.clustercompanyid=cc.id\n" +
                "left join userauth ua on ua.id = uc.authid\n" +
                "where cc.companyid=?";

        return getJdbcOperations().queryForList(sql, companyId);
    }

    public String getMaintenanceStatus(Integer companyID) {
        String sql = "SELECT isMaintenance FROM clustercompany WHERE companyid = ?";
        List<Map<String, Object>> companyMaintenanceStatus = getJdbcOperations().queryForList(sql, companyID);
        Boolean ismaintenance = null;
        if (companyMaintenanceStatus.size() != 0) {
            ismaintenance = (Boolean) companyMaintenanceStatus.get(0).get("ismaintenance");
        }
        String status = "";
        if (ismaintenance != null && ismaintenance) {
            status = "true";
        }
        if (ismaintenance != null && !ismaintenance) {
            status = "false";
        }
        if (ismaintenance == null) {
            status = "Not Found";
        }
        return status;
    }

    public String getAllCompanyIdsByAuthId(Integer authID) {
        String sql = "select cc.companyid from usercompany uc join clustercompany cc on cc.id=uc.clustercompanyid where uc.authid=?";
        List<Map<String, Object>> listCompanyIds = getJdbcOperations().queryForList(sql, authID);
        StringBuilder companyIds = new StringBuilder();
        listCompanyIds.forEach(stringObjectMap -> stringObjectMap.forEach((k, v) -> {
            companyIds.append(v);
            if (listCompanyIds.iterator().hasNext()) {
                companyIds.append(",");
            }
        }));

        return companyIds.toString();
    }

    public static class CompanyDomainMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            CompanyDomain companyDomain = new CompanyDomain();
            companyDomain.setDomain(rs.getString("domain"));
            companyDomain.setWebsiteNumber(rs.getString("website_number"));
            companyDomain.setCompanyID(rs.getInt("companyid"));
            companyDomain.setObjectID(rs.getInt("id"));
            companyDomain.setCompanyUniqueID(rs.getString("company_unique_key"));
            companyDomain.setCompanyBranchName(rs.getString("company_branch_name"));
            companyDomain.setDynamicStatus(rs.getBoolean("dynamicStatus"));
            companyDomain.setFingerprintDateFormat(rs.getString("fingerprintdateformat"));
            companyDomain.setEnabledAdvancedPassword(rs.getBoolean("enabled_advanced_password"));
            return companyDomain;
        }
    }

    public BigDecimal getUserRatePerHOST(Integer userCount, String hostName) {
        Integer maxPayableUserCount = getMaxPayableUserCount(hostName);
        if (userCount > /*30*/maxPayableUserCount) {
            return new BigDecimal(0d);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userCountPH", userCount);
        String USER_RATE_PER_HOST_NAME = "SELECT pp.price FROM pricing pp WHERE pp.userCount=:userCountPH AND pp.hostName LIKE '" + hostName + "%' limit 1";

        Double aDouble;
        try {
            aDouble = getJdbcOperations().queryForObject(USER_RATE_PER_HOST_NAME, Double.class, params);
        } catch (Exception ex) {
            String USER_RATE_DEFAULT_HOST_NAME = "SELECT pp.price FROM pricing pp WHERE pp.userCount=:userCountPH AND pp.hostName LIKE '" + Constants.HOST_LIVE + "%' limit 1";
            aDouble = getJdbcOperations().queryForObject(USER_RATE_DEFAULT_HOST_NAME, Double.class, params);
        }
        if (aDouble == null) {
            String USER_RATE_DEFAULT_HOST_NAME = "SELECT pp.price FROM pricing pp WHERE pp.userCount=:userCountPH AND pp.hostName LIKE '" + Constants.HOST_LIVE + "%' limit 1";
            aDouble = getJdbcOperations().queryForObject(USER_RATE_DEFAULT_HOST_NAME, Double.class, params);
            if (aDouble == null) {
                return new BigDecimal(0d);
            }
        }
        return new BigDecimal(aDouble);
    }

    public UserRateItem getUserDiscountPerHOST(String hostName, Integer userCount) {
        UserRateItem userRateItem;
        String USER_DISCOUNT_PER_HOST = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.userCount=" + userCount + " limit 1";
        try {
            userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_PER_HOST, new UserRateBuilder());
        } catch (Exception ex) {
            try {
                String USER_DISCOUNT_DEFAULT_HOST_NAME = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%' AND pp.userCount=" + userCount + " limit 1";
                userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_DEFAULT_HOST_NAME, new UserRateBuilder());
            } catch (Exception e) {
                try {
                    Integer maxPayableUserCount = getMaxPayableUserCount(hostName);
                    String USER_DISCOUNT_DEFAULT_HOST_NAME = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.userCount=" + maxPayableUserCount + " limit 1";
                    userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_DEFAULT_HOST_NAME, new UserRateBuilder());
                } catch (DataAccessException e1) {
                    Integer maxPayableUserCount = getMaxPayableUserCount(Constants.HOST_LIVE);
                    String USER_DISCOUNT_DEFAULT_HOST_NAME = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%' AND pp.userCount=" + maxPayableUserCount + " limit 1";
                    userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_DEFAULT_HOST_NAME, new UserRateBuilder());
                }
            }
        }

        if (userRateItem == null) {
            try {
                String USER_DISCOUNT_DEFAULT_HOST_NAME = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%' AND pp.userCount=" + userCount + " limit 1";
                userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_DEFAULT_HOST_NAME, new UserRateBuilder());
            } catch (Exception exc) {
                Integer maxPayableUserCount = getMaxPayableUserCount(hostName);
                String USER_DISCOUNT_DEFAULT_HOST_NAME = "SELECT pp.oneMonth, pp.threeMonth, pp.sixMonth, pp.twentyMonth FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%' AND pp.userCount=" + maxPayableUserCount + " limit 1";
                userRateItem = (UserRateItem) getJdbcOperations().queryForObject(USER_DISCOUNT_DEFAULT_HOST_NAME, new UserRateBuilder());
            }
        }

        return userRateItem;
    }

    public Integer getMaxPayableUserCount(String hostName) {
        String MAX_PAYABLE_USER_COUNT = "SELECT MAX(pp.userCount) FROM pricing pp WHERE pp.hostName LIKE '" + hostName + "%'";
        Integer maxPayableUserCount;
        try {
            maxPayableUserCount = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT, Integer.class);
        } catch (Exception ex) {
            String MAX_PAYABLE_USER_COUNT_FOR_DEFAULT_HOST_NAME = "SELECT MAX(pp.userCount) FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%'";
            maxPayableUserCount = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT_FOR_DEFAULT_HOST_NAME, Integer.class);
        }
        if (maxPayableUserCount == null || maxPayableUserCount == 0) {
            String MAX_PAYABLE_USER_COUNT_FOR_DEFAULT_HOST_NAME = "SELECT MAX(pp.userCount) FROM pricing pp WHERE pp.hostName LIKE '" + Constants.HOST_LIVE + "%'";
            maxPayableUserCount = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT_FOR_DEFAULT_HOST_NAME, Integer.class);
        }
        return maxPayableUserCount;
    }

    //new pricing calculation queries - begin
    //using pricing_package table & support_package tables
    //pricingPackageName  -- category name - PP_SMALL_BUSINESS or PP_KPI_PRO or PP_ENTERPRISE
    public Integer getMaxPayableUserCount2(String hostName, String pricingPackageName) {
        String MAX_PAYABLE_USER_COUNT_2 = getMaxTwentyMonthUserCountPerPackageQUERY(hostName, pricingPackageName);
        Integer maxPayableUserCount2;
        try {
            maxPayableUserCount2 = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT_2, Integer.class);
        } catch (Exception ex) {
            String MAX_PAYABLE_USER_COUNT_2_FOR_DEFAULT_HOST_NAME = getMaxTwentyMonthUserCountPerPackageQUERY(Constants.HOST_LIVE, pricingPackageName);
            maxPayableUserCount2 = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT_2_FOR_DEFAULT_HOST_NAME, Integer.class);
        }
        if (maxPayableUserCount2 == null || maxPayableUserCount2 == 0) {
            String MAX_PAYABLE_USER_COUNT_2_FOR_DEFAULT_HOST_NAME = getMaxTwentyMonthUserCountPerPackageQUERY(Constants.HOST_LIVE, pricingPackageName);
            maxPayableUserCount2 = getJdbcOperations().queryForObject(MAX_PAYABLE_USER_COUNT_2_FOR_DEFAULT_HOST_NAME, Integer.class);
        }
        return maxPayableUserCount2;
    }

    private String getMinOneMonthUserCountPerPackageQUERY(String hostName, String pricingPackageName) {
        return "SELECT pp.min_one_month FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + pricingPackageName + "%' LIMIT 1";
    }

    private String getMinThreeMonthUserCountPerPackageQUERY(String hostName, String pricingPackageName) {
        return "SELECT pp.min_three_month FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + pricingPackageName + "%' LIMIT 1";
    }

    private String getMinSixMonthUserCountPerPackageQUERY(String hostName, String pricingPackageName) {
        return "SELECT pp.min_six_month FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + pricingPackageName + "%' LIMIT 1";
    }

    private String getMaxTwentyMonthUserCountPerPackageQUERY(String hostName, String pricingPackageName) {
        return "SELECT pp.max_twenty_month FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + pricingPackageName + "%' LIMIT 1";
    }

    private String getPricePerPackageQUERY(String hostName, String pricingPackageName) {
        return "SELECT pp.price FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + pricingPackageName + "%' LIMIT 1";
    }

    private String getSupportPricePerSupportPackageQUERY(String hostName, String supportPackageName) {
        return "SELECT pp.price FROM support_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName LIKE '" + supportPackageName + "%' LIMIT 1";
    }

    private String getModulePricePerUserQUERY(String hostName, String pricingPackageName, String modules, boolean pricingType) {
        if (pricingType) {
            return "SELECT mp.price, mp.count, d.onemonth, d.threemonth, d.sixmonth, d.twentymonth, 0 min_one_month, 0 min_three_month, 0 min_six_month, 0 max_twenty_month FROM module_price mp inner join discount d on d.hostname=mp.hostname " +
                    "WHERE mp.hostName LIKE '" + hostName + "%' AND mp.code in (" + modules + ") group by mp.price,d.onemonth, d.threemonth, d.sixmonth, d.twentymonth";
        } else {
            return "SELECT pp.price, pp.min_one_month, pp.min_three_month, pp.min_six_month, pp.max_twenty_month, 0 count, 0 onemonth, 0 threemonth, 0 sixmonth, 0 twentymonth FROM pricing_package pp WHERE pp.hostName LIKE '" + hostName + "%' AND pp.packageName='" + pricingPackageName + "' LIMIT 1";
        }
    }

    public UserRateItem getUserCountsViaPrice(String hostName, String pricingPackageName, String modules, boolean pricingType) {
        pricingPackageName = pricingPackageName != null ? pricingPackageName : Constants.PP_MINI;
        UserRateItem pricePerPackage;
        String USER_COUNT_VIA_PRICE_PER_HOST = getModulePricePerUserQUERY(hostName, pricingPackageName, modules, pricingType);
        try {
            pricePerPackage = (UserRateItem) getJdbcOperations().queryForObject(USER_COUNT_VIA_PRICE_PER_HOST, new PricePerPricingPackage());
        } catch (Exception ex) {
            try {
                String USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME = getModulePricePerUserQUERY(Constants.HOST_LIVE, pricingPackageName, modules, pricingType);
                pricePerPackage = (UserRateItem) getJdbcOperations().queryForObject(USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME, new PricePerPricingPackage());
            } catch (Exception e) {
                String USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME = getModulePricePerUserQUERY(Constants.HOST_LIVE, pricingPackageName, modules, pricingType);
                pricePerPackage = (UserRateItem) getJdbcOperations().queryForObject(USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME, new PricePerPricingPackage());
            }
        }
        if (pricePerPackage == null) {
            try {
                String USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME = getModulePricePerUserQUERY(Constants.HOST_LIVE, pricingPackageName, modules, pricingType);
                pricePerPackage = (UserRateItem) getJdbcOperations().queryForObject(USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME, new PricePerPricingPackage());
            } catch (Exception exc) {
                String USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME = getModulePricePerUserQUERY(Constants.HOST_LIVE, pricingPackageName, modules, pricingType);
                pricePerPackage = (UserRateItem) getJdbcOperations().queryForObject(USER_COUNT_VIA_PRICE_DEFAULT_HOST_NAME, new PricePerPricingPackage());
            }
        }
        return pricePerPackage;
    }

    public BigDecimal getUserRatePerPackage(/*Integer userCount, */String hostName, String pricingPackageName) {
        pricingPackageName = pricingPackageName != null ? pricingPackageName : Constants.PP_MINI;
        if ("".equals(pricingPackageName)) {
            return new BigDecimal(0d);
        }
        String USER_RATE_PER_HOST_PER_PACKAGE = getPricePerPackageQUERY(hostName, pricingPackageName);

        Double aDouble;
        try {
            aDouble = getJdbcOperations().queryForObject(USER_RATE_PER_HOST_PER_PACKAGE, Double.class);
        } catch (Exception ex) {
            String USER_RATE_DEFAULT_HOST_NAME = getPricePerPackageQUERY(Constants.HOST_LIVE, pricingPackageName);
            aDouble = getJdbcOperations().queryForObject(USER_RATE_DEFAULT_HOST_NAME, Double.class);
        }
        if (aDouble == null) {
            String USER_RATE_DEFAULT_HOST_NAME = getPricePerPackageQUERY(Constants.HOST_LIVE, pricingPackageName);
            aDouble = getJdbcOperations().queryForObject(USER_RATE_DEFAULT_HOST_NAME, Double.class);
            if (aDouble == null) {
                return new BigDecimal(0d);
            }
        }
        return new BigDecimal(aDouble);
    }

    public BigDecimal getSupportPricePerPackage(String hostName, String supportPackageName) {
        if (supportPackageName == null || "".equals(supportPackageName)) {
            return new BigDecimal(0d);
        }
        String SUPPORT_PRICE_PER_HOST_PER_PACKAGE = getSupportPricePerSupportPackageQUERY(hostName, supportPackageName);
        Double aDouble;
        try {
            aDouble = getJdbcOperations().queryForObject(SUPPORT_PRICE_PER_HOST_PER_PACKAGE, Double.class);
        } catch (Exception ex) {
            String SUPPORT_PRICE_DEFAULT_HOST_NAME = getSupportPricePerSupportPackageQUERY(Constants.HOST_LIVE, supportPackageName);
            aDouble = getJdbcOperations().queryForObject(SUPPORT_PRICE_DEFAULT_HOST_NAME, Double.class);
        }
        if (aDouble == null) {
            String SUPPORT_PRICE_DEFAULT_HOST_NAME = getSupportPricePerSupportPackageQUERY(Constants.HOST_LIVE, supportPackageName);
            aDouble = getJdbcOperations().queryForObject(SUPPORT_PRICE_DEFAULT_HOST_NAME, Double.class);
            if (aDouble == null) {
                return new BigDecimal(0d);
            }
        }
        return new BigDecimal(aDouble);
    }

    public HashMap<String, Double> getSupportPackagePrices(String hostName) {
        String[] supportPackagePricesNAMES = new String[]{Constants.SP_CONTRACTOR, Constants.SP_SMALL_BUSINESS, Constants.SP_PROFESSIONAL, Constants.SP_ENTERPRISE};

        HashMap<String, Double> supportPackagePricesMap = new HashMap<>();
        for (String supportPackageNAME : supportPackagePricesNAMES) {
            if (supportPackageNAME != null && !"".equals(supportPackageNAME) && !supportPackagePricesMap.containsKey(supportPackageNAME)) {
                Double supportPackagePRICE = getSupportPricePerPackage(hostName, supportPackageNAME).doubleValue();
                supportPackagePricesMap.put(supportPackageNAME, supportPackagePRICE);
            }
        }
        return supportPackagePricesMap;
    }

    public HashMap<String, UserRateItem> getPerPricingSupportUserPrices(String hostName, ArrayList<String> pricingPackageNames) {
        HashMap<String, UserRateItem> perPricingPackageUserPrices = new HashMap<>();

        for (String pricingPackageNAME : pricingPackageNames) {
            if (pricingPackageNAME != null && !"".equals(pricingPackageNAME)) {
                UserRateItem userCountsViaPrice = getUserCountsViaPrice(hostName, pricingPackageNAME, null, false);
                userCountsViaPrice.setDiscountOneMonth(0d);
                userCountsViaPrice.setDiscountThreeMonth(0d);
                userCountsViaPrice.setDiscountSixMonth(0d);
                userCountsViaPrice.setDiscountTwentyMonth(0d);
                userCountsViaPrice.setUserRate(userCountsViaPrice.getPricePerPackage());
                userCountsViaPrice.setMaxPayableUserCount(userCountsViaPrice.getUserCountMaxTwentyMonth());
                if (!perPricingPackageUserPrices.containsKey(pricingPackageNAME)) {
                    perPricingPackageUserPrices.put(pricingPackageNAME, userCountsViaPrice);
                }
            }
        }

        return perPricingPackageUserPrices;
    }

    //new pricing calculation queries - end

    public void updateClusterCompanyParent(final Integer companyId, final Integer parentId) {
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement("UPDATE clustercompany SET companyparentId= ? WHERE companyId= ?");
                statement.setInt(1, parentId);
                statement.setInt(2, companyId);
                return statement;
            }
        });
    }

    public List<ConsolidationCompanyList> getSubsidiariesCompany(Integer companyId) {
        String sql = "SELECT cc.id,cc.companyId,c.domain FROM clustercompany cc \n" +
                " INNER JOIN cluster c on c.id=cc.clusterid \n" +
                " WHERE cc.companyparentId=? \n" +
                " ORDER BY c.domain ";
        return (List<ConsolidationCompanyList>) getJdbcOperations().query(sql, new SubsidiariesCompanyBuilder(), companyId);
    }

    @Override
    public String getCompanyClusterType(Integer companyId) {
        String result = null;
        try {
            String sql = "SELECT c.domain FROM clustercompany cc " +
                    "INNER JOIN cluster c ON c.id=cc.clusterid " +
                    "WHERE cc.companyid=?";
            result = getJdbcOperations().queryForObject(sql, String.class, companyId);
        } catch (DataAccessException e) {

        }
        return result;
    }

    @Override
    public Integer getCompanyClusterId(Integer companyId) {
        Integer result = null;
        try {
            String sql = "SELECT cc.id FROM clustercompany cc " +
                    "WHERE cc.companyid=?";
            result = getJdbcOperations().queryForObject(sql, Integer.class, companyId);
        } catch (Exception e) {
            logger.error("", e);
        }
        return result;
    }

    public Map<String, Set<Integer>> getClusterCompanies() {
        String sql = "SELECT cc.companyid,c.domain FROM clustercompany cc " +
                "INNER JOIN cluster c ON c.id=cc.clusterid " +
                "ORDER BY cc.companyid";
        List<SelectItem> clusterCompanyItem = (List<SelectItem>) getJdbcOperations().query(sql, new CompaniesClusterTypeBuilder());

        Map<String, Set<Integer>> clustercompanyMap = new HashMap<>();
        for (SelectItem item : clusterCompanyItem) {
            clustercompanyMap.computeIfAbsent(item.getName(), k -> new HashSet<>());
            clustercompanyMap.get(item.getName()).add(item.getId());
        }
        return clustercompanyMap;
    }

    @Override
    public Map<Integer, String> getCompaniesClusterType(String companyIds) {
        String sql = "SELECT cc.companyid,c.domain FROM clustercompany cc " +
                "INNER JOIN cluster c ON c.id=cc.clusterid " +
                "WHERE cc.companyid IN (" + companyIds + ") ORDER BY cc.companyid";
        List<SelectItem> companyClusterTypeItem = (List<SelectItem>) getJdbcOperations().query(sql, new CompaniesClusterTypeBuilder());

        Map<Integer, String> companyClusterTypeMap = new HashMap<>();
        for (SelectItem item : companyClusterTypeItem) {
            companyClusterTypeMap.put(item.getId(), item.getName());
        }
        return companyClusterTypeMap;
    }

    @Override
    public Map<Integer, String> getSubsidiariesCompanyClusterType(Integer parentCompanyId) {
        String sql = "SELECT cc.companyid,c.domain FROM clustercompany cc INNER JOIN cluster c ON c.id=cc.clusterid \n" +
                "WHERE cc.companyparentid=? OR cc.companyid=? ORDER BY cc.companyid";
        List<SelectItem> companyClusterTypeItem = (List<SelectItem>) getJdbcOperations().query(sql, new CompaniesClusterTypeBuilder(), parentCompanyId, parentCompanyId);
        Map<Integer, String> companyClusterTypeMap = new HashMap<>();
        for (SelectItem item : companyClusterTypeItem) {
            companyClusterTypeMap.put(item.getId(), item.getName());
        }
        return companyClusterTypeMap;
    }

    //returns list of string arrays where on each item in the list first string array element is token and second one is device type like ANDROID or IOS
    public List<String[]> getUserPushNotificationTokens(String username, String domainName, String modulecode) {

        if (StringUtils.isNotBlank(username)) {
            Integer userId = 0;
            try {

                Map params = new HashMap() {{
//                    put("domainname", getDomainNameWithLike(domainName, true));
                    put("username", username.toLowerCase());
                }};

                userId = queryForObject(USER_BY_USERNAME, params, Integer.class);
            } catch (Exception ex) {
                logger.error("User not found in database! username=" + username);
            }
            if (userId > 0) {
                StringBuilder sql = new StringBuilder("SELECT DISTINCT token, devicetype FROM push_notification_token WHERE userid = ");
                sql.append(userId).append(" AND deleted = false AND modulecode='").append(modulecode).append("'");

                List<String[]> list = getJdbcOperations().query(sql.toString(), new RowMapper() {
                    @Override
                    public String[] mapRow(ResultSet resultSet, int i) throws SQLException {
                        String row[] = {resultSet.getString("token"), resultSet.getString("devicetype")};//new String[2];
                        return row;
                    }
                });
                return list;
            }
        }
        return new ArrayList<>();
    }


    @Override
    public void setCurrentUIToUser(UITypeEnum ui, String username) {
        Map<String, Object> myMap = new HashMap<>();

        myMap.put("uiType", ui.getCode());
        myMap.put("username", username);

        update("UPDATE userauth SET uitype= :uiType WHERE username= :username", myMap);
    }

    @Override
    public void setDomainToUIAccess(String domain, String username) {

        Map<String, Object> myMap = new HashMap<>();

        myMap.put("username", username);
        myMap.put("domainname", "|" + domain + "|");

        update("UPDATE userauth SET domainname= domainname||:domainname WHERE username= :username and domainname not like '%" + domain + "%'", myMap);
    }

    @Override
    public ListResult<TelegramSettingsItem> getTelegramSettingItems(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tbs.* FROM telegrambotsettings tbs");
        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("tbs.deleted"));
        sql.append(" AND tbs.companyid =").append(SecurityContext.getCompanyID());

        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" AND (LOWER(tbs.token) LIKE '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(tbs.botname) LIKE '").append(fp.getSqlSearchKey()).append("')");
        }
        if (StringUtils.isNotBlank(fp.getAccessToken())) {
            sql.append(" AND (tbs.token = '").append(fp.getAccessToken()).append("'");
        }
        if (StringUtils.isNotBlank(fp.getName())) {
            sql.append(" OR tbs.botname = '").append(fp.getName()).append("')");
        }
        if (fp.getObjectId() != null) {
            sql.append(" AND tbs.id <>").append(fp.getObjectId());
        }
        Integer totalCount = getJdbcOperations().query(sql.toString(), new TelegramSettingsMapper()).size();

        if (fp.getLimit() > 0) {
            sql.append(" LIMIT ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET ").append(fp.getStart());
        }

        List<TelegramSettingsItem> list = getJdbcOperations().query(sql.toString(), new TelegramSettingsMapper());
        ArrayList<TelegramSettingsItem> arrayList = new ArrayList<>(list);
        return new ListResult<>(arrayList, totalCount);
    }

    static class TelegramSettingsMapper implements RowMapper {
        public TelegramSettingsMapper() {
        }

        public TelegramSettingsItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            TelegramSettingsItem telegramSettingsItem = new TelegramSettingsItem();
            telegramSettingsItem.setId(rs.getInt("id"));
            telegramSettingsItem.setToken(rs.getString("token"));
            telegramSettingsItem.setBotName(rs.getString("botname"));
            telegramSettingsItem.setCompanyId(rs.getInt("companyid"));

            return telegramSettingsItem;
        }
    }

    @Override
    public void saveTelegramSettingsItem(TelegramSettingsItem telegramSettingsItem) {
        try {
            if (telegramSettingsItem.getId() != null) {
                String sqlUpdate = "UPDATE telegrambotsettings set token =?, botname =?, companyid =? where id =?";
                getJdbcOperations().update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(sqlUpdate);
                    statement.setString(1, telegramSettingsItem.getToken());
                    statement.setString(2, telegramSettingsItem.getBotName());
                    statement.setInt(3, telegramSettingsItem.getCompanyId());
                    statement.setInt(4, telegramSettingsItem.getId());
                    return statement;
                });
            } else {
                String sql = "INSERT INTO telegrambotsettings (token, botname, companyid) VALUES (?, ?, ?)";
                getJdbcOperations().update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, telegramSettingsItem.getToken());
                    statement.setString(2, telegramSettingsItem.getBotName());
                    statement.setInt(3, telegramSettingsItem.getCompanyId());
                    return statement;
                });
            }
        } catch (DataAccessException e) {
            logger.error("", e);
        }
    }

    @Override
    public TelegramSettingsItem getTelegramSettingsItem(Integer id) {
        if (id == null) {
            return null;
        }
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        TelegramSettingsItem telegramSettingsItem = null;
        try {
            telegramSettingsItem = (TelegramSettingsItem) queryForObject("SELECT * FROM telegrambotsettings where id=:id AND " + ServerUtils.checkForDeleted("deleted"), parameters, new TelegramSettingsMapper());
        } catch (DataAccessException e) {
            logger.error("", e);
        }
        return telegramSettingsItem;
    }

    @Override
    public Boolean deleteTelegramSettingsItem(Integer id) {
        if (id != null) {
            try {
                String sqlUpdate = "UPDATE telegrambotsettings set deleted = true where id =?";
                getJdbcOperations().update(connection -> {
                    PreparedStatement statement = connection.prepareStatement(sqlUpdate);
                    statement.setInt(1, id);
                    return statement;
                });
                return true;
            } catch (Exception e) {
                logger.error("", e);
                return false;
            }
        }
        return false;
    }

    @Override
    public List<TelegramSettingsItem> getTelegramSettingsItemByToken(String token) {
        if (token != null && !token.isEmpty()) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("token", token);
            List<TelegramSettingsItem> telegramSettingsItems = null;
            try {
                telegramSettingsItems = (List<TelegramSettingsItem>) query("SELECT * FROM telegrambotsettings where token=:token AND " + ServerUtils.checkForDeleted("deleted"), parameters, new TelegramSettingsMapper());
            } catch (DataAccessException e) {
                logger.error("", e);
            }
            return telegramSettingsItems;
        }
        return null;
    }

    @Override
    public TelegramSettingsItem getTelegramSettingsItemByTokenAndCompanyId(String token, Integer companyId) {
        if (token != null && !token.isEmpty()) {
            if (companyId == null) {
                companyId = SecurityContext.getCompanyID();
            }
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("token", token);
            parameters.put("companyId", companyId);
            TelegramSettingsItem telegramSettingsItem = null;
            try {
                telegramSettingsItem = (TelegramSettingsItem) queryForObject("SELECT * FROM telegrambotsettings where token=:token AND companyid =:companyId AND " + ServerUtils.checkForDeleted("deleted"), parameters, new TelegramSettingsMapper());
            } catch (DataAccessException e) {
                logger.error("", e);
            }
            return telegramSettingsItem;
        }
        return null;
    }

    @Override
    public TelegramSettingsItem getTelegramSettingsItemByBotNameAndCompanyId(String botName, Integer companyId) {
        if (botName != null && !botName.isEmpty()) {
            if (companyId == null) {
                companyId = SecurityContext.getCompanyID();
            }
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("botName", botName);
            parameters.put("companyId", companyId);
            TelegramSettingsItem telegramSettingsItem = null;
            try {
                telegramSettingsItem = (TelegramSettingsItem) queryForObject("SELECT * FROM telegrambotsettings where botname=:botName AND companyid =:companyId AND " + ServerUtils.checkForDeleted("deleted"), parameters, new TelegramSettingsMapper());
            } catch (DataAccessException e) {
                logger.error("", e);
            }
            return telegramSettingsItem;
        }
        return null;
    }

    public void createOauthToken(GoogleAuthTO auth) {
        getJdbcOperations().update(connection -> oatuhTokenStatement(auth, connection));
    }

    private PreparedStatement oatuhTokenStatement(GoogleAuthTO auth, Connection connection) throws SQLException {
        String sql = "INSERT INTO oauth_tokens (access_token, refresh_token, token_type, expires_at, scope, email, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime localDateTime = LocalDateTime.now(ZoneOffset.UTC);
        if (auth.getExpires_in() != null) {
            localDateTime = localDateTime.plusSeconds(auth.getExpires_in());
        }
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, auth.getAccess_token());
        statement.setString(2, auth.getRefresh_token());
        statement.setString(3, auth.getToken_type());
        statement.setDate(4, new Date(localDateTime.getSecond()));
        statement.setString(5, auth.getScope());
        statement.setString(6, auth.getEmail());
        statement.setString(7, auth.getUser_id());
        return statement;
    }

    public boolean existsOAuthToken(String email, String userId) {
        Map<String, ?> parameters = new HashMap<>() {{
            put("email", email);
            put("userId", userId);
        }};
        try {
            Integer id = queryForObject("SELECT id FROM oauth_tokens where email=:email and user_id = :userId limit 1 ", parameters, Integer.class);
            return id >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String updateSmsCode(int companyId, String phoneNumber) {
        String generateSmsCode = """
                INSERT INTO sms_verification_codes (user_id, company_id, phone_number, verification_code, request_count, last_request_at, expires_at, is_verified)
                VALUES (?, ?, ?, ?, 1, NOW(), NOW() + INTERVAL '5 minutes', FALSE)
                ON CONFLICT (phone_number, company_id)
                DO UPDATE
                SET
                    verification_code = ?,
                    request_count = CASE
                        WHEN sms_verification_codes.last_request_at < NOW() - INTERVAL '30 minutes' THEN 1
                        ELSE sms_verification_codes.request_count + 1
                    END,
                    last_request_at = NOW(),
                    expires_at = NOW() + INTERVAL '5 minutes',
                    is_verified = FALSE;
                """;
        String verificationCode = String.valueOf(generateRandor(4));
        getJdbcOperations().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(generateSmsCode);
            statement.setInt(1, 1);
            statement.setInt(2, companyId);
            statement.setString(3, phoneNumber);
            statement.setString(4, verificationCode);
            statement.setString(5, verificationCode);
            return statement;
        });
        return verificationCode;
    }

    @Override
    public boolean verifySmsCode(String phonenumber, int code) {
        Map<String, ?> parameters = new HashMap<>() {{
            put("phonenumber", phonenumber);
            put("code", String.valueOf(code));
        }};
        String verifySmsQuery = """
                SELECT id from sms_verification_codes WHERE phone_number = :phonenumber and verification_code = :code;""";
        Integer id = queryForObject(verifySmsQuery, parameters, Integer.class);
        return id >= 0;
    }

    private static int generateRandor(int d) {
        if (d == 0) return 0;
        int r = (int) (Math.random() * 10);
        if (r == 0 && d == 1) r = 1;
        return generateRandor(d - 1) * 10 + r;
    }

    @Override
    public void deleteFingerprintSetupById(Integer id) {
        String sql = "DELETE FROM companydomains WHERE id = ?";
        getJdbcOperations().update(sql, id);
    }
}
