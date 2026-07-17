package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created with IntelliJ IDEA.
 * User: devkpi
 * Date: 22.10.13
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */

public class IndexCompanyUploadDocumentsServlet extends HttpServlet {

    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void init() throws ServletException {
        wfmCommandServiceLocal = ApplicationContextProvider.applicationContext.getBean(WfmCommandServiceLocal.class);
    }

    private static final Logger log = LoggerFactory.getLogger(IndexCompanyUploadDocumentsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sessionId = ServerUtils.getCookieVal("SESSION_ID", request.getCookies());
        ServerSecurityContext.getInstance().setSessionId(sessionId);
        System.out.println("SessionId: " + sessionId);

        String message = wfmCommandServiceLocal.copyUploadDocumentSize();

        PrintWriter writer = response.getWriter();
        writer.write("<html>" +
                "<head>" +
                "<title>kpi</title>" +
                "</head>" +
                "<body>" +
                "<h1> All Document size copied to Upload Table</h1>" +
                "<h1> Document Size: "+ message+"</h1>"+
                "</body>" +
                "</html>");
        writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
