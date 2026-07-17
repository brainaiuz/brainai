package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Eminem
 * Date: 6/15/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class MindsharePatch implements HttpRequestHandler {

    @Autowired
    DepartmentService departmentService;

    @Override
    public void handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        int oldEmployeeDepartment = Integer.valueOf(httpServletRequest.getParameter("old"));
        int newEmployeeDepartment = Integer.valueOf(httpServletRequest.getParameter("new"));
        int companyID = Integer.valueOf(httpServletRequest.getParameter("companyid"));
        String db = httpServletRequest.getParameter("db").toUpperCase();

        ServerSecurityContext.getInstance().setDatabase(db);
        departmentService.runMindsharePatch(companyID, oldEmployeeDepartment, newEmployeeDepartment);


    }

}
