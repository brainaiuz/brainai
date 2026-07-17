package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.gwt.core.server.rpc.HostBasedModuleSettingsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 30.06.14
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
@Repository("moduleGlobalAuthJdbcSpringManager")
public class ModuleGlobalAuthJdbcSpringManagerImpl extends NamedParameterJdbcTemplate implements ModuleGlobalAuthJdbcSpringManager {


    @Autowired
    public ModuleGlobalAuthJdbcSpringManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        super(globalauthDataSource);
    }

    @Override
    public List<HostBasedModuleSettingsItem> getHostBasedModules(final String host) {
        Map parameters = new HashMap() {{
            put("host", host);

        }};
        String sql = "SELECT * FROM hostbasedmodulesettings hm  WHERE hm.host=:host";
        return (List<HostBasedModuleSettingsItem>) query(sql, parameters, new ModuleSettingsMapper());

    }

    @Override
    public void insert(final String code, final String host) {
        final String sql = "INSERT INTO hostbasedmodulesettings (code, host) VALUES (?, ?)";
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, code);
                statement.setString(2, host);
                return statement;
            }
        });
    }

    @Override
    public HostBasedModuleSettingsItem getModulesByCodeByHost(final String code, final String host) {
        String sql = "SELECT * FROM hostbasedmodulesettings hm  WHERE hm.host='" + host + "' and hm.code='" + code + "'";
        try {
            return getJdbcOperations().queryForObject(sql, BeanPropertyRowMapper.newInstance(HostBasedModuleSettingsItem.class));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void delete(final String code, final String host) {
        final String sql = "DELETE FROM hostbasedmodulesettings WHERE code = ? and host = ? ";
        getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, code);
                statement.setString(2, host);
                return statement;
            }
        });
    }

    public static class ModuleSettingsMapper implements RowMapper {

        @Override
        public HostBasedModuleSettingsItem mapRow(ResultSet resultSet, int i) throws SQLException {
            HostBasedModuleSettingsItem settingsItem = new HostBasedModuleSettingsItem();
            settingsItem.setCode(resultSet.getString("code"));
            settingsItem.setHost(resultSet.getString("host"));
            return settingsItem;
        }
    }
}
