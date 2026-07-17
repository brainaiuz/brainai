package com.edatasite.workforce.core.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Ula
 * Date: Jan 31, 2011
 * Time: 7:27:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("globalAuthManager")
public class GlobalAuthConnectionManagerImpl implements GlobalAuthManager {

    private DataSource dataSource;

    @Autowired
    public GlobalAuthConnectionManagerImpl(@Qualifier("globalauthDataSource") DataSource globalauthDataSource) {
        this.dataSource = globalauthDataSource;
    }


    public <T> T executeQuery(Callback<T> callback, String query, Object... params) {
        Connection conn = null;
        T result;
        try {
            try {
                conn = dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Datasource connection could not be obtained. See the exceptions above");
            }
            PreparedStatement ps = conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            result = callback.handleResults(rs);
            rs.close();
            ps.close();
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public interface Callback<T> {
        T handleResults(ResultSet rs) throws SQLException;
    }
}
