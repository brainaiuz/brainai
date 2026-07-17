package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 3/24/14
 * Time: 7:00 PM
 */
@Transactional
@Controller
public class LinkedInLoginViaWfpController  {

    @Autowired
    protected UserManager userManager;

    @RequestMapping(value ="/linkedInLoginViaWfp")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        String apiKey = hostSetting.getLinkedinAPIKey();
        String secret = hostSetting.getLinkedinSecret();
        String scope = "r_basicprofile r_emailaddress r_contactinfo rw_nus";

        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String error = request.getParameter("error");

        if (code != null && state != null) {
            JSONObject result = (JSONObject) new JSONParser().parse(ServerUtils.parseURLResponse(new URL("https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=" + EdsContextParams.getFullHost() + "linkedInLoginViaWfp" +
                    "&client_id=" + apiKey +
                    "&client_secret=" + secret)));

            String refreshToken = (String) result.get("access_token");
            System.out.println(refreshToken);

            ServerSecurityContext.getInstance().setSessionId(ServerUtils.getCookie(request, Constants.SESSION_ID));
            EdsUser user = (EdsUser)ServerSecurityContext.getInstance().getUser();
            user = userManager.get(user.getObjectID());
            user.setLinkedInToken(refreshToken);
            userManager.update(user);
        } else {
            response.sendRedirect("https://www.linkedin.com/uas/oauth2/authorization?response_type=code" +
                    "&client_id=" + apiKey +
                    "&scope=" + scope +
                    "&state=1" +
                    "&redirect_uri=" + EdsContextParams.getFullHost() + "linkedInLoginViaWfp");
            return null;
        }
        return new ModelAndView("redirect:BetaWorkspace.html");
    }
}
