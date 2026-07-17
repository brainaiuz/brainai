package com.edatasite.workforce.gwt.core.server.controllers;

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
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 24.05.12
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
@Controller
//@RequestMapping("/googleGadget/auth")
public class GoogleGadgetAuthController {
    @Autowired
    GoogleGadgetService googleGadgetService;

    @RequestMapping(value = "/googleGadget/auth")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(GoogleGadgetService.JSON_CONTENT_TYPE);
        PrintWriter writer = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        boolean isSigned = googleGadgetService.checkSignedRequest(request);
        String openSocialViewerId = request.getParameter(GoogleGadgetService.OPEN_SOCIAL_VIEWER_ID);
        String googleAppDomain = request.getParameter(GoogleGadgetService.GOOGLE_APP_DOMAIN);
        Integer companyId = googleGadgetService.getInteger(request.getParameter(GoogleGadgetService.COMPANY_ID));


        if (isSigned) {
            boolean isUserExist = googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
            if (isUserExist) {
                ArrayList<String> companyForCurrentUser = googleGadgetService.getCompanyForCurrentUser(openSocialViewerId);
                if (companyForCurrentUser.size() > 1 && companyId == null) {
                    jsonResponse.put(GoogleGadgetService.COMPANY_ID, companyForCurrentUser);
                    jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.PLEASE_CHOOSE_YOUR_COMPANY);
                } else {
                    googleGadgetService.googleGadgetSignIn(openSocialViewerId, companyId);
                    HashMap<String, Boolean> permissions = googleGadgetService.getPermissionsForCurrentUser();
                    jsonResponse.put(GoogleGadgetService.PERMISSIONS, permissions);

                }
                jsonResponse.put(GoogleGadgetService.USER_EXISTS, true);
                jsonResponse.put(GoogleGadgetService.GOOGLE_GADGET_IS_ENABLE, googleGadgetService.isGoogleGagdetEnabled(googleAppDomain));
                writer.write(jsonResponse.toJSONString());

            } else {
                System.out.println("User from gadget does not exist openSocialViewerID = " + openSocialViewerId + ", companyID = " + companyId);
                String token = UUID.randomUUID().toString();
                googleGadgetService.addGoogleGadgetTokenOpenSocialId(token, openSocialViewerId);
                jsonResponse.put(GoogleGadgetService.USER_EXISTS, false);
                jsonResponse.put(GoogleGadgetService.GOOGLE_GADGET_IS_ENABLE, googleGadgetService.isGoogleGagdetEnabled(googleAppDomain));
                jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.YOU_ARE_NOT_AUTHORIZED);
                jsonResponse.put(GoogleGadgetService.TOKEN, token);
                jsonResponse.put(GoogleGadgetService.OPENID_URL, googleGadgetService.getUrl(request, true) + "/mp/gadgetopenid");
                writer.write(jsonResponse.toJSONString());
            }
        } else {
            jsonResponse.put(GoogleGadgetService.USER_EXISTS, false);
            jsonResponse.put(GoogleGadgetService.ERROR_MESSAGE, GoogleGadgetService.YOUR_REQUEST_IS_NOT_SIGNED);
            jsonResponse.put(GoogleGadgetService.GOOGLE_GADGET_IS_ENABLE, googleGadgetService.isGoogleGagdetEnabled(googleAppDomain));
            writer.write(jsonResponse.toJSONString());
        }
        writer.close();
        return null;
    }



}