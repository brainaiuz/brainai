/*
package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AmazonManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Sep 7, 2011
 * Time: 6:01:41 PM
 * To change this template use File | Settings | File Templates.
 *//*

@Controller
public class OneTimeExecuteController {


    @Autowired
    public CompanyManager companyManager;
    @Autowired
    public AmazonManager amazonManager;


    private static Logger log = LogManager.getLogger(OneTimeExecuteController.class);

    @RequestMapping(value = "/changeAmazonFilename.html", method = RequestMethod.GET)
    public ModelAndView chatPassChange(HttpServletRequest request) throws Exception {
        log.info(">>!!!Entering Change File Name in Amazon<<");
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
        List<EdsCompany> companyList = companyManager.getOccupiedCompanies();
        List<String> schemas = companyManager.getExistingSchemas();
        for (EdsCompany company : companyList) {
            if (company.getObjectID() != null && company.getObjectID() < 20000) {
                continue;
            }
            try {
                if (company.hasSchema(schemas)) {
                    amazonManager.changeAccessKeyAndHeaderForOldUploads(company.getObjectID());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Change file Name in Amazon FAILED for companyID: " + company.getObjectID());
            }
            log.info("Change file Name in Amazon is SUCCESSFUL for companyID: " + company.getObjectID());
        }
        log.info(">>!!!Change file Name in Amazon process is COMPLETED<<");
        return new ModelAndView("redirect:" + request.getContextPath() + "/");
    }
}*/
