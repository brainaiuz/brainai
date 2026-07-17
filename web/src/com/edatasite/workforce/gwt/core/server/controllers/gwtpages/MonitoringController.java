package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.workforce.aspects.MethodExecutionStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MonitoringController extends BaseGWTPagesController {

    @Autowired
    MethodExecutionStatisticService methodExecutionStatisticService;

    public ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView view = new ModelAndView("methodExecutionStatistic");
        view.addObject("productName", "Methods Avarage Execution Statistic");
        view.addObject("stats", methodExecutionStatisticService.getStatData());
        return view;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/managementavg.html")
    public ModelAndView getAvgExecTime(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return super.handleRequest(request, response);
    }
}
