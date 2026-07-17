package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LeaveRequestCorrectionController implements Constants {
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;


    @RequestMapping(value = "/leave-request-correction", method = RequestMethod.GET)
    public String linkRedirect(@RequestParam(value = "db") String database,
                               @RequestParam(value = "ci") String companyId,
                               @RequestParam(value = "session") String session,
                               @RequestParam(value = "reasonshortname") String reasonShortName) {

        SecurityContext.getInstance().setDatabase(database);
        SecurityContext.getInstance().setCompanyId(companyId);
        SecurityContext.getInstance().setSessionId(session);


        try {
            availabilityServiceLocal.leaveRequestCorrection(reasonShortName);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAILED";
        }
    }
}
