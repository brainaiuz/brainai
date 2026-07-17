package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.SessionCryptor;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.MassMailServiceLocal;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class MassMailingController implements Constants {

    private static final Logger log = LoggerFactory.getLogger(MassMailingController.class);
    @Autowired
    private MassMailServiceLocal massMailServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    private void trackLink(HttpServletRequest httpReq, HttpServletResponse httpResp) {
        try {
            Integer companyID = Integer.parseInt(EncryptionHelper.decrypt(httpReq.getParameter("c_id")));
            Integer entityID = Integer.parseInt(EncryptionHelper.decrypt(httpReq.getParameter("eid")));
            Integer messageID = Integer.parseInt(EncryptionHelper.decrypt(httpReq.getParameter("mid")));
            String u = EncryptionHelper.decrypt(httpReq.getParameter("link"));
            String ipAddress = httpReq.getRemoteAddr();
            if (u == null || "".equals(u)) {
                httpResp.getWriter().write("Sorry but URL you provided seems to be broken");
            } else {
                SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));
                SecurityContext.setCompanyID(companyID);
                if (!massMailServiceLocal.trackLink(u, entityID, messageID, ipAddress)) {
                    log.error("!!!!!!!!!!!!!!!!!!!!MassMailLink Tracker URL ERROR:c_id" + companyID + " url=" + u);
                }
                try {
                    if (u.contains("crmEntityBodygetEntityID")) {
                        u = u.replace("crmEntityBodygetEntityID", entityID.toString());
                    }
                    new URL(u);
                } catch (MalformedURLException e) {
                    u = "http://" + u;
                }
                httpResp.sendRedirect(u);
                SecurityContext.removeCompanyID();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles e-mail tracking (e.g. whether user opened the e-mail or not)
     *
     * @param httpReq
     * @param httpResp
     * @throws Exception
     */
    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public void messageTrack(HttpServletRequest httpReq, HttpServletResponse httpResp) throws Exception {
        try {
            if (httpReq.getParameterMap().containsKey("link")) {
                trackLink(httpReq, httpResp);
                return;
            }
            Integer companyID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("comid")));
            if (companyID != null) {
                SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));
                SecurityContext.setCompanyID(companyID);
            } else {
                return;
            }
            Integer subscriberID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("subscr")));//lead or contact objectID
            Integer messageID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("msg")));//messageID
            String ipAddress = httpReq.getRemoteAddr();
            massMailServiceLocal.registerMessageOpening(subscriberID, messageID, ipAddress);
        } catch (NumberFormatException ignored) {
        }
        SecurityContext.removeCompanyID();
    }

    /**
     * This methods intends to remove the subscriber from the mailing list
     *
     * @param httpReq
     * @param httpResp
     * @throws Exception
     */
    @RequestMapping(value = "/unsubscribe", method = RequestMethod.GET)
    public void unsubscribe(HttpServletRequest httpReq, HttpServletResponse httpResp) throws Exception {
        try {
            String company = SessionCryptor.decrypt(httpReq.getParameter("comid"));//companyID
            Integer companyID = Integer.parseInt(company);
            if (companyID != null) {
                SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));
                SecurityContext.setCompanyID(companyID);
            } else {
                return;
            }
            Integer subscriberID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("subscr")));
            Integer mailListID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("mailListID")));
            Integer msgID = Integer.parseInt(SessionCryptor.decrypt(httpReq.getParameter("msg")));
            if (mailListID != null && subscriberID != null && msgID != null) {
                massMailServiceLocal.unsubscribeFromMessage(mailListID, msgID, subscriberID);
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        httpResp.sendRedirect(httpReq.getContextPath() + "/successfullyRemovedFromMailingList.html");
        SecurityContext.removeCompanyID();
    }
}
