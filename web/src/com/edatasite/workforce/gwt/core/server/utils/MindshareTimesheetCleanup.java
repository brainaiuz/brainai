package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Eminem
 * Date: 6/15/12
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MindshareTimesheetCleanup implements HttpRequestHandler {

    @Autowired
    DepartmentService departmentService;

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date start = new Date();
        try {
            start = dateFormat.parse(httpServletRequest.getParameter("start"));
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String oldEmployeeDepartment = httpServletRequest.getParameter("employees");
        int companyID = Integer.valueOf(httpServletRequest.getParameter("companyid"));
        int oldDepartmentID = Integer.valueOf(httpServletRequest.getParameter("old"));
        String db = httpServletRequest.getParameter("db").toUpperCase();

        String oldEmployees[] = oldEmployeeDepartment.split(",");
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        for (String oldEmployee : oldEmployees) {
            employeeIDs.add(Integer.valueOf(oldEmployee));
        }

        ServerSecurityContext.getInstance().setDatabase(db);
        departmentService.runMindshareCleanup(companyID, start, employeeIDs, oldDepartmentID);
    }
}
