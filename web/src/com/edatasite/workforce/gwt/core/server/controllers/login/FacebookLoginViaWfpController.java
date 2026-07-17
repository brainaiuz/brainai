package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gson.JsonObject;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
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
 * Date: 3/5/14
 * Time: 9:32 PM
 */
@Transactional
@Controller
public class FacebookLoginViaWfpController extends BaseLoginController {

    @Autowired
    protected UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @RequestMapping(value = "/facebookLoginViaWfp")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        String appID = hostSetting.getFacebookAppID();
        String secret = hostSetting.getFacebookSecret();

        String accessToken = request.getParameter("access_token");

        URL url = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" + appID + "&client_secret=" + secret + "&fb_exchange_token=" + accessToken);
        JsonObject jsonrf = parseJsonResponse(url);
        String refreshToken = jsonrf.get("access_token").getAsString();
        ServerSecurityContext.getInstance().setSessionId(ServerUtils.getCookie(request, Constants.SESSION_ID));
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        user = userManager.get(user.getObjectID());
        user.setFacebookToken(refreshToken);
        userManager.update(user);

        return new ModelAndView("redirect:BetaWorkspace.html");
    }
}
