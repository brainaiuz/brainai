package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 11/28/12
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/addLink")
public class GoogleGagdetAddLinksController implements CrmConstants {

    @Autowired
    GoogleGadgetService googleGadgetService;

    @Autowired
    AllInOneService allInOneService;

    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String LINK_TYPE = "linkTypeDropdown";
    private static final String DATA_LIST = "dataList";
    private static final String PROJECT_ID = "projectId";

    private static final String TASK = "TASK";
    private static final String CASE = "case";
    private static final Integer LIMIT = 200;

    @RequestMapping(value = "/googleGadget/addLink")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));


        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (request.getParameter(DEFAULT_FORM) != null && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm();
                } else if (!isInvalid(request.getParameter(LINK_TYPE)) && request.getParameter(LINK_TYPE).equals(TASK)) {
                    if (!isInvalid(request.getParameter(PROJECT_ID))) {
                        Integer projectId = Integer.parseInt(request.getParameter(PROJECT_ID));
                        writer.write(getTasks(companyId, projectId));
                    } else {
                        writer.write(getProjectList(companyId));
                    }
                } else if (!isInvalid(request.getParameter(LINK_TYPE)) && request.getParameter(LINK_TYPE).equals(CASE)) {
                    writer.write(getCases(companyId));
                }
            }
        }
        return null;
    }

    private ModelAndView getForm() {
        return new ModelAndView("googleGadgetAddLink");
    }

    private String getCases(Integer companyId) {
        JSONObject jsonResponse = new JSONObject();

        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setCRM(true);

        SelectItem[] caseItems = allInOneService.getLookUpItems(filterParametrs, CRM_CASE_ID, null);
        jsonResponse.put(DATA_LIST, wrapToArrayList(caseItems));

        return jsonResponse.toJSONString();
    }

    private String getTasks(Integer companyId, Integer projectId) {
        JSONObject jsonResponse = new JSONObject();

        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setProjectId(projectId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setPM(true);

        SelectItem[] taskItems = allInOneService.getLookUpItems(filterParametrs, PM_TASK_ID, null);
        jsonResponse.put(DATA_LIST, wrapToArrayList(taskItems));

        return jsonResponse.toJSONString();
    }

    private String getProjectList(Integer companyId) {
        JSONObject jsonResponse = new JSONObject();

        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setCompanyID(companyId);
        filterParametrs.setLimit(LIMIT);
        filterParametrs.setPM(true);

        SelectItem[] projectItems = allInOneService.getLookUpItems(filterParametrs, PM_PROJECT_ID, null);
        jsonResponse.put(DATA_LIST, wrapToArrayList(projectItems));

        return jsonResponse.toJSONString();
    }


    private ArrayList<String> wrapToArrayList(SelectItem[] selectItems) {
        ArrayList<String> wrap = new ArrayList<>();
        if (selectItems != null) {
            for (SelectItem item : selectItems) {
                wrap.add(item.getId() + "::" + item.getName());
            }
        }
        return wrap;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }


}
