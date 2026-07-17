package com.edatasite.workforce.gwt.core.server.servlets.csv;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: JavaZone
 * Date: Jun 11, 2011
 * Time: 6:34:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaticCSVDownloader extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestURI().split()
        resp.setHeader("Content-disposition", "attachment;");
        resp.setContentType("application/vnd.ms-excel;charset=ISO-8859-1");
        super.service(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-disposition", "attachment;");
        resp.setContentType("application/vnd.ms-excel;charset=ISO-8859-1");
        super.doHead(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Content-disposition", "attachment;");
        resp.setContentType("application/vnd.ms-excel;charset=ISO-8859-1");
        super.doTrace(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
