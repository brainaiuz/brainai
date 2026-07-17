package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

//import org.json.simple.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 02.07.2010
 * Time: 14:08:12
 */
@Controller
public class FacebookLoginController extends BaseLoginController implements Constants {

    public FacebookLoginController() {
    }

    /**
     * This method implements Single Sign On using facebook account
     * We get access token from the cookie which has been set up by facebook script in index page
     * Afterwards, we obtain User details through Graph API using our access token and send the user to sign-in/sign-up page
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(
            value = "/facebookLogin"
    )
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        final String appID = hostSetting.getFacebookAppID();
        final String secret = hostSetting.getFacebookSecret();
        final String accessToken = request.getParameter("access_token");
        URL url = new URL("https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token&client_id=" +
                          appID +
                          "&client_secret=" +
                          secret +
                          "&fb_exchange_token=" +
                          accessToken);

        final JsonObject jsonrf = this.parseJsonResponse(url);
        final String refreshToken = jsonrf.get("access_token").getAsString();
        //email, firstname,lastname
        url = new URL("https://graph.facebook.com/me?access_token=" + refreshToken + "&fields=id,first_name,last_name,email");

        //Network updates - https://graph.facebook.com/me/home?access_token=

        final OAuthUser user = new OAuthUser();
        final JsonObject json = this.parseJsonResponse(url);

        /* We will not validate for existency of email because we may also use facebookid
        if (json == null || json.get("email") == null) {
            return new ModelAndView("redirect:/?error=User not found!");
        }*/
        user.setFacebookToken(refreshToken);

        if (json.get("id") != null) {
            user.setId(json.get("id").getAsString());
        }
        if (json.get("email") != null) {
            user.setEmail(json.get("email").getAsString());
        }
        if (json.get("first_name") != null) {
            user.setFirstName(json.get("first_name").getAsString());
        }
        if (json.get("last_name") != null) {
            user.setLastName(json.get("last_name").getAsString());
        }
        return redirectToSignInOrSignUp(user, request, response);
    }


    /**
     * Facebook returns info as JSON you can get:
     * id, name, first_name, last_name, link, birthday, email, timezone, locale, verified, updated_time
     * If User exists, sign the user in, otherwise redirect it to Sign Up page.
     *
     * @throws ServletException
     * @throws IOException
     */

    private ModelAndView redirectToSignInOrSignUp(OAuthUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return forwardToSignInOrSignUp(new SignUpItem(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null, RegistrationTypeEnum.FACEBOOK), request, response);

    }
}
