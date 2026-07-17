package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Lochin
 * Date: 22-Nov-2010
 * Time: 14:46:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class OrgChartController implements Constants {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "/OrgChart.html", method = RequestMethod.GET)
    public ModelAndView handleRequestInternal(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);
        ModelAndView model = new ModelAndView("orgChart");

        model.addObject("orgChart", employeeService.getOrgChart());

        return model;
    }


}
