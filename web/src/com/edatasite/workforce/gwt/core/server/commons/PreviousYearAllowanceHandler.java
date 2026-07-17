package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PreviousYearAllowanceHandler implements HttpRequestHandler {
    @Autowired
    AvailabilityServiceLocal availabilityServiceLocal;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyId_ = request.getParameter("companyid");
        String year_ = request.getParameter("year");
        if (companyId_ != null && year_ != null) {
            if (companyId_.matches("^\\d+") && year_.matches("^\\d+")) {
                Integer companyId = Integer.parseInt(companyId_);
                Integer year = Integer.parseInt(year_);
                ServerSecurityContext.getInstance().setCompanyId(companyId);
                availabilityServiceLocal.copyLastYearLeaveAllowanceMinutes(year);
                response.getWriter().write("COPY LAST YEAR ALLOWANCES COMMAND SEND SUCCESSFULLY ! ! !");
            } else {
                response.getWriter().write("companyid or year parameters are not represented as number");
            }
        } else {
            response.getWriter().write("One of the (or both) paremater(s) (companyid or year) is missing ! ! ! Cannot execute last year allowance calculation");
        }
    }
}
