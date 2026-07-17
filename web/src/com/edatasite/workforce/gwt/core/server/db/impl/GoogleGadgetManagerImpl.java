package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManagerImpl;
import com.edatasite.workforce.gwt.core.server.db.GoogleGadgetManager;
import com.edatasite.workforce.gwt.core.server.rpc.GoogleGadgetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 26.05.12
 * Time: 18:10
 * To change this template use File | Settings | File Templates.
 */
@Repository("googleGadgetManager")
public class GoogleGadgetManagerImpl extends NamedParameterJdbcTemplate implements GoogleGadgetManager {

    private static final String BY_OPEN_SOCIAL_ID = """
            SELECT gg.opensocialid, gg.token, gg.userauthid, uc.userid as userid, cc.companyid, cl.domain as clusterdomain\s
            from googlegadgetauth as gg\s
            LEFT JOIN userauth ua on gg.userauthid=ua.id\s
            LEFT JOIN userCompany uc on ua.id = uc.authid
            LEFT JOIN clustercompany cc on uc.clustercompanyid = cc.id
            LEFT JOIN "cluster" cl on cc.clusterid = cl.id  where gg.opensocialid=:opensocialid""";

    private static final String BY_TOKEN = """
            SELECT gg.opensocialid, gg.userauthid, gg.token, uc.userid as userid, cc.companyid, cl.domain as clusterdomain\s
            from googlegadgetauth as gg\s
            LEFT JOIN userauth ua on gg.userauthid=ua.id\s
            LEFT JOIN userCompany uc on ua.id = uc.authid
            LEFT JOIN clustercompany cc on uc.clustercompanyid = cc.id
            LEFT JOIN "cluster" cl on cc.clusterid = cl.id  where gg.token=:token""";

    private static final String USER_AUTH_ID_BY_USER_NAME = "SELECT ua.id FROM userauth as ua where ua.domainname like :domainname AND ua.username=:username";

    private static final String UPDATE_USER_AUTH_ID = "UPDATE  googlegadgetauth  SET userauthid=? where token=?";

    private static final String UPDATE_TOKEN = "UPDATE googlegadgetauth SET token=? where opensocialid=?";

    private static final String INSERT_OPENSOCIAL_ID_AND_TOKEN = "INSERT INTO googlegadgetauth (opensocialid, token) VALUES (?, ?)";

    private static final String DELETE_USER = "DELETE FROM googlegadgetauth WHERE userauthid in (SELECT authid FROM usercompany WHERE userid=? AND clustercompanyid in (SELECT id FROM clustercompany WHERE companyId=?))";

    @Autowired
    public GoogleGadgetManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        super(globalauthDataSource);
    }

    public ArrayList<GoogleGadgetDTO> authByOpenSocialId(final String openSocialId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("opensocialid", openSocialId);

        ArrayList<GoogleGadgetDTO> dbName = null;
        try {
            dbName = (ArrayList<GoogleGadgetDTO>) query(BY_OPEN_SOCIAL_ID, params, BeanPropertyRowMapper.newInstance(GoogleGadgetDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return dbName;
    }

    public ArrayList<GoogleGadgetDTO> findToken(final String token) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("token", token);

        ArrayList<GoogleGadgetDTO> dbName = null;
        try {
            dbName = (ArrayList<GoogleGadgetDTO>) query(BY_TOKEN, params, BeanPropertyRowMapper.newInstance(GoogleGadgetDTO.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return dbName;
    }

    public Integer getUserAuthIdByUsername(String domainName, String username) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("domainname", GlobalAuthJdbcSpringManagerImpl.getDomainNameWithLike(domainName, true));
        params.addValue("username", username.trim().toLowerCase());

        Integer result = null;
        try {
            result = queryForObject(USER_AUTH_ID_BY_USER_NAME, params, Integer.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateUserAuthId(final GoogleGadgetDTO googleGadgetDTO) {
        getJdbcOperations().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_AUTH_ID);
            statement.setInt(1, googleGadgetDTO.getUserAuthID());
            statement.setString(2, googleGadgetDTO.getToken());
            return statement;
        });
    }

    public void insertTokenAndOpenSocialID(final GoogleGadgetDTO googleGadgetDTO) {
        if (authByOpenSocialId(googleGadgetDTO.getOpenSocialID()).size() > 0) {
            getJdbcOperations().update(connection -> {
                PreparedStatement statement = connection.prepareStatement(UPDATE_TOKEN);
                statement.setString(1, googleGadgetDTO.getToken());
                statement.setString(2, googleGadgetDTO.getOpenSocialID());
                return statement;
            });
        } else {
            getJdbcOperations().update(connection -> {
                PreparedStatement statement = connection.prepareStatement(INSERT_OPENSOCIAL_ID_AND_TOKEN);
                statement.setString(1, googleGadgetDTO.getOpenSocialID());
                statement.setString(2, googleGadgetDTO.getToken());
                return statement;
            });
        }
    }


    public void deleteGoogleGadgetUser(final Integer userid, final Integer companyid) {
        getJdbcOperations().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_USER);
            statement.setInt(1, userid);
            statement.setInt(2, companyid);
            return statement;
        });
    }

    @Deprecated
    static
    class GoogleGadgetDTOMapper implements RowMapper {
        public GoogleGadgetDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            GoogleGadgetDTO googleGadgetDTO = new GoogleGadgetDTO();
            googleGadgetDTO.setUserAuthID(rs.getInt("userauthid"));
            googleGadgetDTO.setUserID(rs.getInt("userid"));
            googleGadgetDTO.setOpenSocialID(rs.getString("opensocialid"));
            googleGadgetDTO.setToken(rs.getString("token"));
            googleGadgetDTO.setClusterDomain(rs.getString("clusterdomain"));
            googleGadgetDTO.setCompanyID(rs.getInt("companyid"));
            return googleGadgetDTO;
        }
    }

}
