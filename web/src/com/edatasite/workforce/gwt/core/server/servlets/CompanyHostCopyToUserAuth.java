package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 15/06/12
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
public class CompanyHostCopyToUserAuth extends HttpServlet {

    public static String SELECT_ALL_COMPANY_HOST = "SELECT c.id,css.host FROM pg_namespace p \n" +
            " INNER JOIN \"public\".company c ON c.id=CAST(p.nspname as integer) \n" +
            " INNER JOIN \"public\".companysystemsettings css ON css.companyid=c.id \n" +
            " WHERE nspname ~ '^[0-9]+$' ORDER BY nspname";

    public static String SELECT_NOT_SET_HOST_USERCOMPANY = "SELECT uc.id,uc.authid,uc.userid,cc.companyid,ua.domainname FROM usercompany uc \n" +
            "INNER JOIN userauth ua ON ua.id = uc.authid\n" +
            "INNER JOIN clustercompany cc ON cc.id=uc.clustercompanyid \n" +
            "WHERE uc.isadddomain=FALSE AND ua.domainname is NOT NULL AND cc.companyId IN (";

    private DataSource paidDataSource;
    private DataSource freeDataSource;
    private DataSource globalauthDataSource;


    @Override
    public void init() throws ServletException {
        paidDataSource = (DataSource) ApplicationContextProvider.applicationContext.getBean("paidDataSource");
        freeDataSource = (DataSource) ApplicationContextProvider.applicationContext.getBean("freeDataSource");
        globalauthDataSource = (DataSource) ApplicationContextProvider.applicationContext.getBean("globalauthDataSource");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String addNewColumnAndConstRate = "ALTER TABLE userauth DROP CONSTRAINT userauth_username_key; \n" +
                    "ALTER TABLE userauth ADD COLUMN domainname varchar(255);\n" +
                    "ALTER TABLE userauth ADD CONSTRAINT userauth_username_domainname_key UNIQUE (username, domainname);\n" +
                    "ALTER TABLE usercompany ADD COLUMN isadddomain boolean DEFAULT FALSE;\n";

            executeQuery(globalauthDataSource, addNewColumnAndConstRate);


            moveGlobaAuthDBDomain(freeDataSource);
            moveGlobaAuthDBDomain(paidDataSource);

            String clearSqlQuery = "ALTER TABLE usercompany DROP COLUMN isadddomain; \n";
            executeQuery(globalauthDataSource, clearSqlQuery);

            PrintWriter out = resp.getWriter();
            out.write("<html>" +
                        "<head>" +
                            "<title>Workforce</title>" +
                        "</head>" +
                        "<body>" +
                            "<h1> ALL UPDATE SUCCESSFULL (GAPNI QISQASI O'KI DO'KI) </h1>" +
                        "</body>" +
                    "</html>");
            out.close();
        } finally {
            PrintWriter out = resp.getWriter();
            out.write("<html>" +
                    "<head>" +
                    "<title>Workforce</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1> ALL UPDATE FAILER (Siz oldin bu servletni iwlatgansiz ili Querylar run paytida Exception bor (Tadjiev Dilshodga murojat qiling)) </h1>" +
                    "</body>" +
                    "</html>");
            out.close();
        }
    }

    private void moveGlobaAuthDBDomain(DataSource dataSource) {
        // Select All Company user List
        final Map<Integer, String> companyHostMap = new HashMap<>();
        final StringBuffer selectAllUsersqlQuery = new StringBuffer("");
        final StringBuffer companyIds = new StringBuffer(""); ;
        executeQuery(rs -> {
            String compIds = "";
            while (rs.next()) {
                Integer companyId = rs.getInt(1);
                String companyHost = rs.getString(2);
                companyHostMap.put(companyId, companyHost);
                selectAllUsersqlQuery.append("SELECT '" + companyId + "','" + companyHost + "',id,username FROM \"" + companyId + "\".myuser WHERE deleted IS NOT TRUE \n");
                companyIds.append(companyId);
                if (!rs.isLast()) {
                    companyIds.append(",");
                    selectAllUsersqlQuery.append("UNION \n");
                }
            }
        }, dataSource, SELECT_ALL_COMPANY_HOST);



        // Generate UserAuth table add host query
        final StringBuffer userAuthUpdateQuery = new StringBuffer();
        final StringBuffer userCompanyUpdateQuery = new StringBuffer("");
        final Map<Integer,Map<Integer,String>> companyUserListMap = new HashMap<>();
        executeQuery(rs -> {
            while (rs.next()) {
                Integer companyId = rs.getInt(1);
                String companyHost = rs.getString(2);
                Integer userId = rs.getInt(3);
                String username = rs.getString(4);
                userAuthUpdateQuery.append("UPDATE userauth SET domainname='" + companyHost + "' WHERE username='" + username + "' AND domainname IS NULL;\n");
                userCompanyUpdateQuery.append("UPDATE usercompany SET isadddomain = TRUE \n" +
                        "WHERE authid IN (SELECT id FROM userauth WHERE username='" + username + "' AND domainname='" + companyHost + "') \n" +
                        "AND clustercompanyid IN (SELECT id FROM clustercompany WHERE companyid=" + companyId + ") AND userid=" + userId + "; \n");
                if (!companyUserListMap.containsKey(companyId)) {
                    companyUserListMap.put(companyId, new HashMap<>());
                }
                companyUserListMap.get(companyId).put(userId, username);
            }
        }, dataSource, selectAllUsersqlQuery.toString());

        // Update UserAuth table domain column
        executeQuery(globalauthDataSource, userAuthUpdateQuery.toString());

        // Update UserCompany
        executeQuery(globalauthDataSource, userCompanyUpdateQuery.toString());


        // Select UserCompany  table not add domain
        final StringBuffer createNotSetZoneUsernNewUserAccount = new StringBuffer("");
        // Insert multi user new zone
        final StringBuffer updateUserCompanyNewUserAuth = new StringBuffer("");
        final List<String> uniqHostDomainName = new ArrayList<>();
        executeQuery(rs -> {
            while (rs.next()) {
                Integer userCompanyId = rs.getInt(1);
                Integer authId = rs.getInt(2);
                Integer userId = rs.getInt(3);
                Integer companyId = rs.getInt(4);
                String hostName = rs.getString(5);
                if (companyHostMap.containsKey(companyId) && !companyHostMap.get(companyId).equals(hostName) &&
                        companyUserListMap.containsKey(companyId) && companyUserListMap.get(companyId).containsKey(userId)) {
                    if (!uniqHostDomainName.contains(companyHostMap.get(companyId) + "_" + authId)) {
                        createNotSetZoneUsernNewUserAccount.append("INSERT INTO userauth(username,password,domainname) " +
                                "(SELECT username,password,'" + companyHostMap.get(companyId) + "' FROM userauth WHERE id=" + authId +
                                " AND domainname<>'" + companyHostMap.get(companyId) + "');\n");
                        uniqHostDomainName.add(companyHostMap.get(companyId) + "_" + authId);
                    }

                    String selectUserAuth = "(SELECT id FROM userauth WHERE username='" + companyUserListMap.get(companyId).get(userId) + "' AND domainname='" + companyHostMap.get(companyId) + "')";
                    updateUserCompanyNewUserAuth.append("UPDATE usercompany SET authid = " +
                            "(CASE WHEN (" + selectUserAuth + ") IS NOT NULL THEN (" + selectUserAuth + ") ELSE authid END) " +
                            "WHERE id=" + userCompanyId + " AND authid=" + authId + ";\n");
                }
            }
        }, globalauthDataSource, SELECT_NOT_SET_HOST_USERCOMPANY + companyIds.toString() + ")");

        if (!"".equals(createNotSetZoneUsernNewUserAccount.toString())) {
            executeQuery(globalauthDataSource, createNotSetZoneUsernNewUserAccount.toString());
            executeQuery(globalauthDataSource, updateUserCompanyNewUserAuth.toString());
        }
    }

    public void executeQuery(Callback callback,DataSource dataSource, String query, Object... params) {
        Connection conn = null;
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
            callback.handleResults(rs);
            rs.close();
            ps.close();

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

    public void executeQuery(DataSource dataSource, String query, Object... params) {
        Connection conn = null;
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
            ps.execute();
            ps.close();
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

    public interface Callback {
        void handleResults(ResultSet rs) throws SQLException;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
