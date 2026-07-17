package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gson.JsonObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.02.2014
 * Time: 14:08:12
 */
@Controller
public class LinkedinLoginController extends BaseLoginController implements Constants {

    private static Logger log = LoggerFactory.getLogger(LinkedinLoginController.class);

    protected UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @RequestMapping(value = "/sendtolinkedinauthorization")
    public ModelAndView sendRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        String apiKey = hostSetting.getLinkedinAPIKey();
        String secret = hostSetting.getLinkedinSecret();
        String scope = "r_basicprofile r_liteprofile r_emailaddress";

        /*response.sendRedirect("https://www.linkedin.com/uas/oauth2/authorization?response_type=code" +
                "&client_id=" + apiKey +
                "&scope=" + scope +
                "&state=1" +
                "&redirect_uri=" + EdsContextParams.getFullHost() + "linkedinLogin");*/
        response.sendRedirect("https://www.linkedin.com/oauth/v2/authorization?response_type=code" +
                "&client_id=" + apiKey +
                "&scope=" + scope +
                "&state=1" +
                "&redirect_uri=" + EdsContextParams.getFullHost() + "linkedinLogin");

        return null;
    }


    @RequestMapping(value = "/linkedinLogin")
    public ModelAndView handleResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(request.getServerName());
        String apiKey = hostSetting.getLinkedinAPIKey();
        String secret = hostSetting.getLinkedinSecret();
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String error = request.getParameter("error");

//        JsonObject result = parseJsonResponse(new URL("https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code" +
        try {
            JsonObject result = parseJsonResponse(new URL("https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=" + EdsContextParams.getFullHost() + "linkedinLogin" +
                    "&client_id=" + apiKey +
                    "&client_secret=" + secret));

            String refreshToken = result.get("access_token").getAsString();
            System.out.println(refreshToken);

        /*OAuthUser user = new OAuthUser();
        user.setLinkedinToken(refreshToken);*/
            OAuthUser user = getProfileInfo(refreshToken);
            //Firstname, Lastname
//        parseXMLResponse(new URL("https://api.linkedin.com/v1/people/~?oauth2_access_token=" + refreshToken), user);
            //Email
//        parseXMLResponse(new URL("https://api.linkedin.com/v1/people/~/email-address?oauth2_access_token=" + refreshToken), user);

            //Network Updates
//        parseXMLResponse(new URL("https://api.linkedin.com/v1/people/~/network/updates?oauth2_access_token=" + refreshToken), user);
            return redirectToSignInOrSignUp(user, request, response);
        } catch (Exception e) {
            log.error("", e);
            return new ModelAndView("redirect:/");
        }
    }

    public OAuthUser parseXMLResponse(URL url, OAuthUser user) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        XMLStreamReader parser = factory.createXMLStreamReader(connection.getInputStream());
        String attName = "";
        while (parser.hasNext()) {
            if (parser.hasName()) {
                attName = parser.getName().toString();
            }

            if (parser.next() == XMLStreamReader.CHARACTERS && parser.getText() != null && !"".equals(parser.getText().trim())) {
                System.out.println(attName + parser.getText());
                if ("id".equals(attName)) {
                    user.setId(parser.getText());
                } else if ("first-name".equals(attName)) {
                    user.setFirstName(parser.getText());
                } else if ("last-name".equals(attName)) {
                    user.setLastName(parser.getText());
                } else if ("headline".equals(attName)) {
                    user.setLinkedinCompany(parser.getText());
                } else if ("email-address".equals(attName)) {
                    user.setEmail(parser.getText());
                }
            }
        }
        return user;
    }

    public OAuthUser getProfileInfo(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> requestEntity
                    = new HttpEntity<>("", createHeaders(accessToken));
            ResponseEntity<String> resp = restTemplate.exchange("https://api.linkedin.com/v2/me?projection=(id,firstName,lastName)", HttpMethod.GET, requestEntity, String.class);

            JSONObject jsonObject = new JSONObject(resp.getBody());
            if (!jsonObject.isNull("id")) {
                String userId = jsonObject.getString("id");
                String firstName = "";
                try {
                    if(jsonObject.getJSONObject("firstName")!=null
                            && jsonObject.getJSONObject("firstName").getJSONObject("localized")!=null
                            && jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale")!=null
                            && jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("country")!=null
                            && jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("language")!=null
                            && jsonObject.getJSONObject("firstName").getJSONObject("localized").getString(jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("language") + "_" + jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("country"))!=null) {
                        firstName = jsonObject.getJSONObject("firstName").getJSONObject("localized").getString(jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("language") + "_" + jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("country") );
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
//                                        String lastName = jsonObject.getString("lastName");
                String lastName = "";
                try {
                    if(jsonObject.getJSONObject("lastName")!=null
                            && jsonObject.getJSONObject("lastName").getJSONObject("localized")!=null
                            && jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale")!=null
                            && jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("country")!=null
                            && jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("language")!=null
                            && jsonObject.getJSONObject("lastName").getJSONObject("localized").getString(jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("language") + "_" + jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("country"))!=null) {
                        lastName = jsonObject.getJSONObject("lastName").getJSONObject("localized").getString(jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("language") + "_" + jsonObject.getJSONObject("lastName").getJSONObject("preferredLocale").getString("country"));
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
                OAuthUser result = new OAuthUser();
                result.setId(userId);
                result.setFirstName(firstName);
                result.setLastName(lastName);


                String emailJson = getProfileEmail(accessToken);

                try {
                    JSONObject emailJsonObject = new JSONObject(emailJson);
                    if ( emailJsonObject.has("elements") && !emailJsonObject.getJSONArray("elements").getJSONObject(0).isNull("handle")/* !emailJsonObject.isNull("handle")*/) {
                        JSONObject handle = emailJsonObject.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~")/*emailJsonObject.getJSONObject("handle~")*/;
                        if (handle != null) {
                            result.setEmail(handle.getString("emailAddress"));
                        }
                    }
                } catch (JSONException e1) {
                    log.error("Error parsing JSON response: ", e1);
                }
                return result;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getProfileEmail(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> requestEntity
                    = new HttpEntity<>("", createHeaders(accessToken));
            ResponseEntity<String> resp = restTemplate
                    .exchange("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))" , HttpMethod.GET, requestEntity, String.class);

            return resp.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("X-Restli-Protocol-Version", "2.0.0");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }


    private ModelAndView redirectToSignInOrSignUp(OAuthUser user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return forwardToSignInOrSignUp(new SignUpItem(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null, RegistrationTypeEnum.LINKEDIN), request, response);
    }
}
