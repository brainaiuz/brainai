package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.magento.EdsMagentoApiSettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.MagentoService;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.MagentoApiSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.google.code.magja.service.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Shohruh on 26 Dec 2016.
 */
public class MagentoEventNotificationHandler implements HttpRequestHandler, Constants {

    private static Logger log = LoggerFactory.getLogger(MagentoEventNotificationHandler.class);

    @Autowired
    private MagentoService magentoService;
    @Autowired
    private MagentoApiSettingsManager magentoApiSettingsManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private UserManager userManager;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        log.info("-------- Magento Event Notification: --------");
        log.info("Request URL: " + request.getRequestURI());

        Integer entity_id = Integer.valueOf(request.getParameter("entity_id"));
        String entity_type = request.getParameter("entity_type");
        String company_id = request.getParameter("company_id");
        String db_type = request.getParameter("db_type");

        ServerSecurityContext.getInstance().setCompanyId(company_id);
        ServerSecurityContext.getInstance().setDatabase(db_type);

        EdsMagentoApiSettings magentoApiSettings = magentoApiSettingsManager.getSettings();

        EdsUser user = magentoApiSettings.getKpiUser();
        if (user == null) {
            user = userManager.getAdmin(Integer.valueOf(company_id));
        }
        ServerSecurityContext.getInstance().setStaticUserID(user.getObjectID());

        try {
            if ("ORDER".equals(entity_type)) {
                magentoService.syncOrder(entity_id);
            } else if ("CUSTOMER".equals(entity_type)) {
                magentoService.syncCustomer(entity_id);
            } else if ("CUSTOMER_ADDRESS".equals(entity_type)) {
                EdsCrmAccount client = crmAccountManager.getAccountByMagentoId(entity_id);
                if (client != null) {
                    magentoService.syncCustomer(entity_id);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            log.error("Error occured during syncronization: " + e.getMessage());
        }
        ServerSecurityContext.getInstance().setStaticUserID(null);
    }
}
