package com.edatasite.workforce.gwt.core.server.office365.managers.impl;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManagerImpl;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365AuthManager;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365UserDTO;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Created by umakarimov on 9/21/15.
 */
@Repository("office365AuthManager")
public class Office365AuthManagerImpl extends NamedParameterJdbcTemplate implements Office365AuthManager, Office365Constants {
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;


    @Autowired
    public Office365AuthManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        super(globalauthDataSource);
    }


    @Override
    public List<UserCompanyDTO> getAuthInfoByObjectId(final String objectId, final String domainName) {
        String sql = "" +
                "SELECT " +
                "  ua.id               AS authId, " +
                "  ua.username         AS username, " +
                "  ua.modificationDate AS modificationDate, " +

                "  uc.userID           AS userId, " +

                "  cc.companyid        AS companyId, " +
                "  cc.ismaintenance    AS isMaintenance, " +

                "  c.id                AS clusterID, " +
                "  c.domain            AS clusterDbName " +

                "FROM office365user ou " +
                "  INNER JOIN userAuth ua ON ou.userId = ua.id " +
                "  INNER JOIN userCompany uc ON ua.id = uc.authid " +
                "  INNER JOIN clustercompany cc ON cc.id = uc.clustercompanyid " +
                "  INNER JOIN cluster c ON c.id = cc.clusterid " +

                "WHERE ou.objectId = :objectId " +
                "      AND (ua.domainname LIKE :domainname OR ua.domainname = :domainname) " +

                "ORDER BY uc.id";

        Map params = new HashMap() {{
            this.put("objectId", objectId);
            this.put("domainname", GlobalAuthJdbcSpringManagerImpl.getDomainNameWithLike(domainName, true));
        }};

        List<UserCompanyDTO> companies = new ArrayList<>();

        try {
            companies = (List<UserCompanyDTO>) query(sql, params, BeanPropertyRowMapper.newInstance(UserCompanyDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if (companies.isEmpty()) {
            return companies;
        }

        String sessionId = ServerSecurityContext.getInstance().getSessionId();

        List<UserCompanyDTO> result = new ArrayList<>();

        for (UserCompanyDTO company : companies) {
            try {
                company = this.validateCompany(company);
            } catch (Exception e) {
                company = null;
                e.printStackTrace();
            }

            if (company != null) {
                result.add(company);
            }
        }

        ServerSecurityContext.getInstance().setSessionId(sessionId);

        return result;
    }

    @Override
    public Office365AccessTokenDTO getAccessToken(final Integer authId, final Integer companyId, final String storageType) {

        Map parameters = new HashMap() {{
            put("authId", authId);
            put("companyId", companyId);
        }};
        final String query = "SELECT * FROM office365authtoken WHERE userId =:authId AND companyid = :companyId AND issharepoint is " + Constants.OFFICE_365_SHARE_POINT.equals(storageType);

        List<Office365AccessTokenDTO> tokens = (List<Office365AccessTokenDTO>) query(query, parameters, new AuthTokenMapper());

        if (tokens.isEmpty()) {
            return null;
        }

        return tokens.get(0);
    }

    @Override
    public boolean hasAccessToken(Integer authId, Integer companyId, String storageType) {
        final String query = "SELECT COUNT(*) > 0 FROM office365authtoken WHERE userId = ? AND companyId = ?" + " AND issharepoint is " + Constants.OFFICE_365_SHARE_POINT.equals(storageType);

        return getJdbcOperations().queryForObject(query, Boolean.class, authId, companyId);
    }

    @Override
    @Transactional
    public Office365AccessTokenDTO saveAccessToken(final Office365AccessTokenDTO token, String storageType) {
        final String insertQuery = "INSERT INTO office365authtoken (userid, companyId, objectid, expireson, accesstoken, refreshtoken,issharepoint,siteurl) VALUES (?, ?, ?, ?, ?, ?,?, ?)";
        final String updateQuery = "UPDATE office365authtoken SET expireson = ?, accesstoken = ?, refreshtoken = ? WHERE userid = ? AND companyid = ? AND  issharepoint is " + Constants.OFFICE_365_SHARE_POINT.equals(storageType);
        String selectQuery = "SELECT * FROM office365authtoken WHERE userid = :userId AND companyid = :companyId AND issharepoint is " + Constants.OFFICE_365_SHARE_POINT.equals(storageType);

        HashMap<String, Object> selectParams = new HashMap<String, Object>() {{
            this.put("userId", token.getUserId());
            this.put("companyId", token.getCompanyId());

        }};

        List<Office365AccessTokenDTO> tokens = new ArrayList<>();

        try {
            tokens = query(selectQuery, selectParams, BeanPropertyRowMapper.newInstance(Office365AccessTokenDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if (token.getId() == null && (tokens == null || tokens.isEmpty())) {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            this.getJdbcOperations().update(connection -> {
                PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

                ps.setInt(1, token.getUserId());
                ps.setInt(2, token.getCompanyId());
                ps.setString(3, token.getObjectId());
                ps.setTimestamp(4, new Timestamp(token.getExpiresOn().getTime()));
                ps.setString(5, token.getAccessToken());
                ps.setString(6, token.getRefreshToken());
                ps.setBoolean(7, token.issharepoint());
                ps.setString(8, token.getSiteUrl());

                return ps;
            }, keyHolder);

            token.setId((Integer) keyHolder.getKeys().get("id"));
        } else {
            this.getJdbcOperations().update(updateQuery, token.getExpiresOn(), token.getAccessToken(), token.getRefreshToken(), token.getUserId(), token.getCompanyId());
        }

        return token;
    }

    @Override
    @Transactional
    public void sendValidationEmail(final String email, Integer authTokenId, String hostUrl, Integer companyId) {
        String uuid = UUID.randomUUID().toString();
        String secret = EncryptionHelper.sha512(uuid, HASH_SECRET);

        String url = hostUrl + AUTH_EMAIL_VERIFY_PAGE_URL + "?s=" + secret;

        String deleteQuery = "DELETE FROM office365secretcode WHERE email = ?";
        String insertQuery = "INSERT INTO office365secretcode (email, secretcode, authtokenid) VALUES (?, ?, ?)";

        try {
            this.getJdbcOperations().update(deleteQuery, email);
            this.getJdbcOperations().update(insertQuery, email, secret, authTokenId);

            String oldCompanyId = ServerSecurityContext.getInstance().getCompanyId();

            ServerSecurityContext.getInstance().setCompanyId(companyId);
            messageManager.sendOffice365EmailVerification(email, url);
            ServerSecurityContext.getInstance().setCompanyId(oldCompanyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void deleteOfficeTokens(String storageType) {
        UserCompanyDTO userCompany = getUserCompany();
        if (userCompany != null) {
            String deleteToken = "DELETE FROM office365authtoken WHERE userid = ? and companyid = ? " + " and issharepoint is " + Constants.OFFICE_365_SHARE_POINT.equals(storageType);
            String deleteUser = "DELETE FROM office365user WHERE userid = ? and companyid = ?";

            try {
                this.getJdbcOperations().update(deleteToken, userCompany.getAuthId(), userCompany.getCompanyID());
                this.getJdbcOperations().update(deleteUser, userCompany.getAuthId(), userCompany.getCompanyID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Office365AccessTokenDTO getAuthTokenFromSecretCode(final String secretCode) {
        String selectQuery = "" +
                "SELECT at.* " +
                "FROM office365authtoken AS at " +
                " RIGHT JOIN office365secretcode AS sc ON sc.authtokenid = at.id " +
                "WHERE sc.secretcode = :secretCode";

        String deleteQuery = "DELETE FROM office365secretcode WHERE secretcode = ?";

        List<Office365AccessTokenDTO> tokens = new ArrayList<>();

        Map params = new HashMap() {{
            this.put("secretCode", secretCode);
        }};

        try {
            tokens = (List<Office365AccessTokenDTO>) query(selectQuery, params, BeanPropertyRowMapper.newInstance(Office365AccessTokenDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if (tokens.isEmpty()) {
            return null;
        }

        try {
            this.getJdbcOperations().update(deleteQuery, secretCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tokens.get(0);
    }

    @Override
    @Transactional
    public void assignOfficeUser(final Integer userId, final Integer companyId, final String objectId) {
        String insertQuery = "INSERT INTO office365user (userid, objectid, companyId) VALUES (?, ?, ?)";
        String selectQuery = "SELECT * FROM office365user WHERE userid = :userId AND objectid = :objectId AND companyid = :companyId";

        HashMap<String, Object> selectParams = new HashMap<String, Object>() {{
            this.put("userId", userId);
            this.put("objectId", objectId);
            this.put("companyId", companyId);

        }};

        List<Office365UserDTO> users = new ArrayList<>();

        try {
            users = query(selectQuery, selectParams, BeanPropertyRowMapper.newInstance(Office365UserDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if (users == null || users.isEmpty()) {
            this.getJdbcOperations().update(insertQuery, userId, objectId, companyId);
        }
    }

    private UserCompanyDTO validateCompany(UserCompanyDTO userCompany) {
        ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());

        EdsCompany company = companyManager.get(userCompany.getCompanyID());

        if (company == null) {
            return null;
        }

        EdsUser user;

        try {
            user = userManager.get(userCompany.getUserID());
        } catch (Exception ignored) {
            return null;
        }

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            return null;
        }

        if (user instanceof EdsClientContact) {
            if (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess()) {
                return null;
            }
        }

        String status = userManager.getUserStatus(user.getObjectID());

        if (Constants.EMPLOYEE_STATUS_ACTIVE.equals(status)) {

            userCompany.setCompanyName(company.getName());

            return userCompany;
        }

        return null;
    }

    public static class AuthTokenMapper implements RowMapper {
        public AuthTokenMapper() {
        }

        public Office365AccessTokenDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Office365AccessTokenDTO(rs);
        }
    }

    @Override
    public UserCompanyDTO getUserCompany() {
        return this.getUserCompany(userManager.getUser());
    }

    @Override
    public UserCompanyDTO getUserCompany(EdsUser user) {
        return this.getUserCompany(user, SecurityContext.getCompanyID());
    }

    @Override
    public UserCompanyDTO getUserCompany(EdsUser user, Integer companyID) {
        if (user == null || companyID == null) {
            return null;
        }

        return globalAuthJdbcSpringManager.getUserInfoByUserNameAndCompany(null, user.getUserName(), companyID);
    }
}
