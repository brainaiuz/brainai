package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: Ilhombek
 * Date: 6/28/12
 * Time: 2:55 PM
 */
public class EmployeeTaskDailyLoadCopyToEdsTimeSheet implements HttpRequestHandler {
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String paidOrFreeDBName = String.valueOf(request.getParameter("dbName"));//paid or free db name
            Integer companyID = Integer.valueOf(request.getParameter("companyId"));//companyID
            if (paidOrFreeDBName != null && !"".equals(paidOrFreeDBName)) {
                ServerSecurityContext.getInstance().setDatabase(paidOrFreeDBName);
                if (companyID == null) {
                    availabilityCircularResolver.copyEmployeeTaskDailyLoadToTimeSheet();
                } else {
                    availabilityCircularResolver.copyEmployeeTaskDailyLoadToTimeSheet(companyID);
                }
            }
            PrintWriter writer = response.getWriter();
            writer.write("<html>" +
                    "<head>" +
                    "<title>kpi</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1> All Employee Task DailyLoad copied to TimeSheet</h1>" +
                    "</body>" +
                    "</html>");
            writer.close();
        } catch (Exception e) {
            PrintWriter writer = response.getWriter();
            writer.write("<html>" +
                    "<head>" +
                    "<title>kpi</title>" +
                    "</head>" +
                    "<body>" +
                    "<h1>Error occured!!!</h1>" +
                    "</body>" +
                    "</html>");
            writer.close();
        }
    }
}