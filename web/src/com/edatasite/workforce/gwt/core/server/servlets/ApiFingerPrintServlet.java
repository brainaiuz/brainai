package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.app.StatusServiceLocal;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintItem;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

/**
 * Created by Dilshod Madrahimov on 8/31/15 3:09 PM
 */
public class ApiFingerPrintServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ApiFingerPrintServlet.class);
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private StatusServiceLocal statusServiceLocal;

    @Override
    public void init() throws ServletException {
        globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
        statusServiceLocal = (StatusServiceLocal) ApplicationContextProvider.applicationContext.getBean("statusService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String companyUniqueID = req.getHeader("X-Unique-ID");
        resp.setContentType("text/html;charset=UTF-8");
        log.warn("FINGER_PRINT UNIQE KEY:" + companyUniqueID);
        if (companyUniqueID == null) {
            log.info("Company unique key from request is null.");
            resp.getWriter().write("Company unique key from request is null.");
            return;
        }
        CompanyDomain companyDomain = globalAuthJdbcSpringManager.getCompanyIdByUniqueKey(companyUniqueID);
        if (companyDomain == null || companyDomain.getCompanyID() == null) {
            log.info("Getting company ID by company unique key is null. Set an unique key to company domains table.");
            resp.getWriter().write("Getting company ID by company unique key is null. Set an unique key to company domains table.");
            return;
        }
        ServerSecurityContext.getInstance().setCompanyId(companyDomain.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyDomain.getCompanyID()));
        StringBuilder sb = new StringBuilder();
        BufferedReader br = req.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        try {
            if ("".equals(sb.toString())) {
                log.info("Finger Print Json data is null.");
                resp.getWriter().write("Finger Print Json data is null.");
                return;
            }
            JSONObject jObj = new JSONObject(sb.toString().trim());
            FingerPrintItem printItem = new Gson().fromJson(jObj.toString(), FingerPrintItem.class);
            if (printItem == null || printItem.getUsers() == null || printItem.getUsers().isEmpty()) {
                return;
            }
            if (!companyDomain.getDynamicStatus()) {
                printItem.getUsers().sort(Comparator.comparing(o -> o.getLogDate(companyDomain.getFingerprintDateFormat())));
            }
            statusServiceLocal.addFingerPrintItemsToTimeTrackAll(printItem, companyDomain);
            ServerSecurityContext.getInstance().removeCompanyId();
        } catch (JSONException e) {
            ServerSecurityContext.getInstance().removeCompanyId();
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            resp.getWriter().write(e.getMessage());
        }
        resp.getWriter().write("Success!");
    }


}
