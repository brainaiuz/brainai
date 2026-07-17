package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.utils.EdsContextParams;
import com.live.login.WindowsLiveLogin;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Abdulaziz
 * Date: Nov 27, 2010
 * Time: 4:06:41 PM
 * to register new domain for login go to
 * https://live.azure.com
 * username: azizjonh@hotmail.com
 * password: App$workforce2010
 */
@Controller
public class LiveIdController extends BaseLoginController {
    private boolean DEBUG = false;
    // Comma-delimited list of offers to be used.
    private static String OFFERS = "Contacts.View";
    // Application key file
    //private String KEYFILE = "DelAuth-Sample2.xml";
    // Name of session variable to use to cache the consent token.
    private static String WEBAUTHTOKEN = "webauthtoken";
    private static int TOKENTTL = -1;
    // URL of Web Authentication sample index page.
    private static String INDEX = "delauth/signin.jsp";
    // Landing pages to use after processing login and logout respectively.
    private static String LOGIN = INDEX;
    private static String LOGOUT = INDEX;

    // The location of the Web Authentication control. You should not have
    // to change this value.
    private static String CONTROLURL = "http://login.live.com/wlogin.srf";
    private static String securityalgorithm = "wsignin1.0";
    private static String returnurl = "/liveidauth";
    private static String policyurl = "/policy.html";
///////////

    private static final Logger log = LoggerFactory.getLogger(LiveIdController.class);
    private WindowsLiveLogin wll;
    public Map<String, UserData> userDetails;

    @RequestMapping("/liveidauth")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (wll == null) {
            String appID = EdsContextParams.getLiveIDAppID();
            String secret = EdsContextParams.getLiveIDSecret();
            String host = EdsContextParams.getHost();
            System.out.println("LiveID host and appID ->" + host + " " + appID);

            wll = new WindowsLiveLogin(appID, secret, securityalgorithm, false, host + policyurl, host + returnurl);
        }

        ServerUtils.fillHostParameters(request);
        String action = request.getParameter("action");
        String loginType = request.getParameter("loginType");

        if ("logout".equals(action)) {
            //request.getSession().setAttribute(WEBAUTHTOKEN, null);
            //Delete Cookie
            setCookieValue(response, WEBAUTHTOKEN, "false");
            response.sendRedirect(LOGOUT);
        } else if ("clearcookie".equals(action)) {
            //Delete Cookie
            setCookieValue(response, WEBAUTHTOKEN, "false");
            response.setContentType(wll.getClearCookieResponseType());
            response.getOutputStream().write(wll.getClearCookieResponseBody());
            response.flushBuffer();
        } else if ("login".equals(action)) {
            processLogin(request, response); //Step 1 - Authorization
            return null;
        } else if ("delauth".equals(action)) {
            return processConsentInfo(request, response); // Step 2 - Getting user's consent and fetching user data.
        } else {
            if (loginType != null) {
                Cookie cookie = new Cookie("loginType", loginType);
                response.addCookie(cookie);
            }

            response.sendRedirect(CONTROLURL + "?appid=" + wll.getAppId() + "&alg=" + securityalgorithm);
            return null;
        }
        return null;
    }

    /**
     * Because creating a large number of
     * components is typically not a concern, but a large number of distinct components is. To
     * further reduce the cost of the component-manufacturing process, many of the systems
     * strive to allow for unreliable components.  Claytronics and TAM may provide this model.
     * Even though there are a lot of issues
     */
    private void processLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WindowsLiveLogin.User user = wll.processLogin(request.getParameterMap());

        if (user != null) {
            //request.getSession().setAttribute(WEBAUTHTOKEN, user.getToken());
            //Write token to Cookie
            setCookieValue(response, WEBAUTHTOKEN, user.getToken());

            if (user.usePersistentCookie()) {
                request.getSession().setMaxInactiveInterval(TOKENTTL);
            }
            String consentUrl = wll.getConsentUrl(OFFERS).toString();
            response.sendRedirect(consentUrl);

        } else {
            setCookieValue(response, WEBAUTHTOKEN, "false");
            response.sendRedirect(LOGIN);
        }
    }

    private ModelAndView processConsentInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //String loginToken = (String) request.getSession().getAttribute(WEBAUTHTOKEN);
        String loginToken = getCookie(request, WEBAUTHTOKEN);
        WindowsLiveLogin.User user = wll.processToken(loginToken);

        //if user is authorized
        if (user != null) {
            WindowsLiveLogin.ConsentToken ct =
                    wll.processConsent(request.getParameterMap());
            //if consent token exists
            if ((ct != null) && ct.isValid()) {

                System.out.println("User Unique hash: " + user.getId());
                System.out.println("Del Token: " + ct.getDelegationToken());
                System.out.println("Login Token: " + loginToken);
                System.out.println("Token: " + ct.getToken());
                System.out.println("LID: " + ct.getLocationID());

                //Fetching user info and Linking User ID with user credentials
                try {
                    userDetails = fetchUserData(user.getId(), ct.getDelegationToken(), ct.getLocationID());
                } catch (XMLStreamException e) {
                    System.out.println("XML error");
                }

                //Login to Workforce
                String id = userDetails.get(user.getId()).getId();
                String email = userDetails.get(user.getId()).getEmail();
                String firstName = userDetails.get(user.getId()).getFirstName();
                String lastName = userDetails.get(user.getId()).getLastName();

                return forwardToSignInOrSignUp(new SignUpItem(id, email, firstName, lastName, null, null), request, response);
            }
        }
        return null;
    }


    static class UserData {
        private String id;
        private String firstName;
        private String email;
        private String lastName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName != null ? firstName : "";
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName != null ? lastName : "";
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public Map<String, UserData> fetchUserData(String userHash, String delToken, String locationId) throws ServletException, IOException, XMLStreamException {
        Map<String, UserData> userData = new HashMap<>();
        UserData user = new UserData();

        URL url = new URL("https://livecontacts.services.live.com/users/@L@" + locationId + "/REST/LiveContacts/");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        String AuthHeaderValue = "DelegatedToken dt=\"" + delToken + "\"";
        connection.setRequestProperty("Authorization", AuthHeaderValue);
        connection.setRequestProperty("Pragma", "No-Cache");
        //request.ContentType = "application/xml; charset=utf-8";

        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLStreamReader parser = factory.createXMLStreamReader(connection.getInputStream());
        String attName = "";
        while (parser.hasNext()) {
            if (parser.hasName()) {
                attName = parser.getName().toString();
            }

            if (parser.next() == XMLStreamReader.CHARACTERS) {
                System.out.println(attName + parser.getText());
                if ("WindowsLiveID".equals(attName)) {
                    user.setEmail(parser.getText());
                } else if ("FirstName".equals(attName)) {
                    user.setFirstName(parser.getText());
                } else if ("LastName".equals(attName)) {
                    user.setLastName(parser.getText());
                } else if ("ID".equals(attName)) {

                    break;
                    //user = new UserData();
                }

            }
        }
        userData.put(userHash, user);

        parser.close();

        return userData;
    }

    public void setCookieValue(HttpServletResponse response, String name, String value) {
        Cookie c = new Cookie(name, value);
        if ("false".equals(value)) {
            c.setMaxAge(0); //Delete Cookie
        } else {
            c.setMaxAge(30 * 24 * 60 * 60);
        }

        c.setPath("/");
        response.addCookie(c);
    }

    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] allCookies;

        if (name == null) {
            throw new IllegalArgumentException("cookie name is null");
        }

        allCookies = request.getCookies();
        if (allCookies != null) {
            for (Cookie candidate : allCookies) {
                if (name.equals(candidate.getName())) {
                    return candidate.getValue();
                }
            }
        }
        return null;
    }
}
