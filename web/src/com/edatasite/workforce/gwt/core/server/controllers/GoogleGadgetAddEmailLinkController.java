package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetLookUpService;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Extreme
 * Date: 5/17/13
 * Time: 11:38 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/addEmailLink")
public class GoogleGadgetAddEmailLinkController {
    @Autowired
    GoogleGadgetService googleGadgetService;

    @Autowired
    GoogleGadgetLookUpService gadgetLookUpService;

    @Autowired
    @Qualifier("allInOneService")
    AllInOneServiceLocal allInOneServiceLocal;

    @Autowired
    TaskService taskService;


    private static final String PROJECT_ID = "projectId";
    private static final String DEFAULT_FORM = "getDefaultForm";
    private static final String LINK_TYPE = "linkTypeDropdown";
    private static final String LOOK_UP_TYPE = "lookUpType";
    private static final String LINK_ITEM = "linkItemDropdown";
    private static final String DATA_LIST = "dataList";
    private static final String SAVE_EMAIL_LINK = "saveEmailLink";
    private static final String EMAIL_SUBJECT = "emailSubject";
    private static final String EMAIL_DESCIPTION = "emailDescription";
    private static final String EMAIL_FROM_EMAIL = "emailFromEmail";
    private static final String EMAIL_TO_EMAIL = "emailToEmail";
    private static final String EMAIL_EMAIL_ID = "emailEmailId";
    private static final String RELATIONS = "relations";
    private static final String SEARCH_KEYWORD = "searchKeyword";

    private static final String RELATION_ITEMS = "relationItems";


    @RequestMapping(value = "/googleGadget/addEmailLink")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        boolean isSigned = googleGadgetService.checkSignedRequest(request);


        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));


        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                if (!isInvalid(request.getParameter(DEFAULT_FORM)) && request.getParameter(DEFAULT_FORM).equals(GoogleGadgetService.TRUE)) {
                    return getForm(request.getParameter(EMAIL_EMAIL_ID), companyId);
                } else if (!isInvalid(request.getParameter(SAVE_EMAIL_LINK)) && request.getParameter(SAVE_EMAIL_LINK).equals(GoogleGadgetService.TRUE)) {
                    writer.write(saveEmailLink(request));
                } else if (!isInvalid(request.getParameter(LOOK_UP_TYPE))) {
                    Integer projectId = null;
                    String keyword = "";
                    if (!isInvalid(request.getParameter(PROJECT_ID))) {
                        projectId = Integer.parseInt(request.getParameter(PROJECT_ID));
                    }
                    if (!isInvalid(request.getParameter(SEARCH_KEYWORD))) {
                        keyword = request.getParameter(SEARCH_KEYWORD);
                    }
                    writer.write(getJsonString(gadgetLookUpService.getItems(request.getParameter(LOOK_UP_TYPE), companyId, projectId, keyword, null)));
                }
            } else {
                writer.write(GoogleGadgetService.YOU_ARE_NOT_AUTHORIZED);
            }
        } else {
            writer.write(GoogleGadgetService.YOUR_REQUEST_IS_NOT_SIGNED);
        }
        return null;
    }

    private ModelAndView getForm(String generatedGoogleEmailId, Integer companyId) {
        ModelAndView modelAndView = new ModelAndView("googleGadgetAddEmailLink");

        long generatedEmailId = Long.valueOf(generatedGoogleEmailId);
        ArrayList<RelationItem> relations = allInOneServiceLocal.getEmailRelations(generatedEmailId);

        if (relations != null && relations.size() > 0) {
            ArrayList<GoogleGadgetLookUpService.EmailLinkItem> emailLinkItems = gadgetLookUpService.getSelectExistingItems(relations, companyId);
            modelAndView.addObject(RELATION_ITEMS, emailLinkItems);
        } else {
            ArrayList<GoogleGadgetLookUpService.EmailLinkItem> item = new ArrayList<>();

            GoogleGadgetLookUpService.EmailLinkItem emailLinkItem = new GoogleGadgetLookUpService.EmailLinkItem();
            emailLinkItem.setLinkTypes(gadgetLookUpService.getType(""));
            emailLinkItem.setLinkItems(null);
            emailLinkItem.setExist(false);
            item.add(emailLinkItem);

            modelAndView.addObject(RELATION_ITEMS, item);
        }


        return modelAndView;
    }


    private String saveEmailLink(HttpServletRequest emailLinkData) {
        JSONObject jsonResponse = new JSONObject();
        String emailSubject = emailLinkData.getParameter(EMAIL_SUBJECT);
        String emailDescription = emailLinkData.getParameter(EMAIL_DESCIPTION);
        String emailFromEmail = emailLinkData.getParameter(EMAIL_FROM_EMAIL);
        String emailToEmail = emailLinkData.getParameter(EMAIL_TO_EMAIL);
        String emailEmailId = emailLinkData.getParameter(EMAIL_EMAIL_ID);

        String[] items = emailLinkData.getParameterValues(LINK_ITEM);
        String[] types = emailLinkData.getParameterValues(LINK_TYPE);

        String[] relations = wrapToArray(types, items);

        Email email = new Email();
        email.setSubject(emailSubject);
        email.setContent(emailDescription);
        email.setFromEmail(emailFromEmail);
        email.setToEmails(emailToEmail);
        email.setGeneratedGoogleID(emailEmailId);

        Integer systemEmailId = allInOneServiceLocal.saveGoogleGadgetMail(email);
        boolean deleteRelationResult = allInOneServiceLocal.deleteRelationByGeneratedGoogleID(email.getGeneratedGoogleID());
        if (deleteRelationResult) {
            boolean saveRelationResult = googleGadgetService.saveRelation(relations,
                    systemEmailId, RelationItem.TYPE_EMAIL_TRACKER, email, false);
            if (!saveRelationResult) {
                jsonResponse.put(GoogleGadgetService.SAVED, false);
                jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.RELATION_SAVED_FAILED);
            } else {
                jsonResponse.put(GoogleGadgetService.SAVED, true);
            }
        } else {
            jsonResponse.put(GoogleGadgetService.SAVED, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.RELATION_SAVED_FAILED);
        }

        return jsonResponse.toJSONString();
    }

    private String getJsonString(SelectItem[] items) {
        JSONObject jsonResponse = new JSONObject();

        jsonResponse.put(DATA_LIST, wrapToArrayList(items));

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

    private String[] wrapToArray(String[] types, String[] items) {
        String[] relations = new String[items.length];

        for (int i = 0; i < items.length; i++) {
            relations[i] = types[i] + "::" + items[i];
        }

        return relations;
    }

    private boolean validate(HttpServletRequest emailLinkData) {
        int errors = 0;
        if (isInvalidArray(emailLinkData.getParameterValues(LINK_ITEM))) {
            errors++;
        }
        if (isInvalidArray(emailLinkData.getParameterValues(LINK_TYPE))) {
            errors++;
        }

        return errors <= 0;
    }

    private boolean isInvalid(String param) {
        return param == null || param.equals("");
    }

    private boolean isInvalidArray(String[] params) {
        if (params != null && params.length > 0) {
            for (String param : params) {
                if (param == null || "".equals(param)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }


}
