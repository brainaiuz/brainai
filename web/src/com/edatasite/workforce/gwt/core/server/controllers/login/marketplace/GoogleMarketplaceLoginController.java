package com.edatasite.workforce.gwt.core.server.controllers.login.marketplace;


import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.GoogleGadgetService;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.GoogleAuthorizationController;
import com.edatasite.workforce.gwt.core.server.db.GoogleGadgetManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleMarketplaceManager;
import com.edatasite.workforce.gwt.core.server.db.googleoauth.OAuthResponse;
import com.edatasite.workforce.gwt.core.server.rpc.GoogleGadgetDTO;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.util.ServiceException;
import com.google.step2.AuthRequestHelper;
import com.google.step2.AuthResponseHelper;
import com.google.step2.ConsumerHelper;
import com.google.step2.Step2;
import com.google.step2.discovery.IdpIdentifier;
import com.google.step2.openid.ui.UiMessageRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ParameterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * The class encapsulates the Google marketplace OpenID based authentication
 * <p/>
 * IMPORTANT!!!! note________________________________________________________________________________
 * Before moving to the cluster environment the following points must be taken under consideration -
 * 1. InMemoryConsumerAssociationStore should be replaced with database storage implementation like
 * WfmJdbcConsumerAssociationStore in the main application
 * 2. ConsumerHelper must be the same instance for OpenId round trips
 * 3. In order to store OpenId discovery information (helper.getDiscoveryInformation()) as a session attribute,
 * the Session object should be replicated between cluster nodes or verify(receivingUrl, openidResp, null) method should
 * be used.
 */
@Controller
public class GoogleMarketplaceLoginController implements Constants {

    Logger logger = LoggerFactory.getLogger(GoogleMarketplaceLoginController.class);

    @Autowired
    GoogleGadgetService googleGadgetService;
    @Autowired
    GoogleGadgetManager googleGadgetManager;
    @Autowired
    GoogleManager googleManager;
    @Autowired
    GoogleAuthorizationController googleAuthorizationController;
    @Autowired
    private SignUpService signUpService;
    private ConsumerHelper consumerHelper;
    private String realm;
    private String returnToPath;
    public static final String SUBSECTION = "subsection";
    public static final String INVALID_TOKEN = "invalid_token";


    public GoogleMarketplaceLoginController() {
        ConsumerFactory factory = new ConsumerFactory(new InMemoryConsumerAssociationStore());
        consumerHelper = factory.getConsumerHelper();
        returnToPath = "/mp/openidret";
//        realm = EdsContextParams.getGoogleMarketplaceRealm();
    }

    @RequestMapping(value = "/openid/{domain}/{section}", method = RequestMethod.GET)
    public ModelAndView sendRequest(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable String domain, @PathVariable String section) throws ServletException, IOException {
        //get admin's email, name, etc. and sign up/signin using oauth2 request
        response.sendRedirect(googleManager.getAuthSubURL(USER_INFO, EdsContextParams.getFullHost() + "mp/oauth2ret"));
        return null;
//        return sendRequest(request, response, domain, section, null);
    }

    @RequestMapping(value = "/gadgetopenid/{domain}/{token}", method = RequestMethod.GET)
    public ModelAndView sendGadgetRequest(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable String domain, @PathVariable String token) throws ServletException, IOException {
        //get admin's email, name, etc. and sign up/signin using oauth2 request
        response.sendRedirect(googleManager.getAuthSubURL(USER_INFO, EdsContextParams.getFullHost() + "mp/oauth2ret", token));
        return null;
    }

    @Deprecated
    @RequestMapping(value = "/googlegadgetopenid/{domain}/{token}", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable String domain, @PathVariable String token) throws ServletException, IOException {
        Boolean checkToken = googleGadgetService.checkToken(token);
        if (checkToken) {
            request.setAttribute("token", token);
            sendRequest(request, response, domain, null, null);
        }

        return null;
    }

    /**
     * Either initiates a login to a given provider or processes a response from an IDP.
     *
     * @param request  - user request
     * @param response - redirect response
     * @throws javax.servlet.ServletException - servlet exception
     * @throws java.io.IOException            exp  - io exception
     */
    @RequestMapping(value = "/openid/{domain}/{section}/{subsection}", method = RequestMethod.GET)
    public ModelAndView sendRequest(HttpServletRequest request, HttpServletResponse response,
                                    @PathVariable String domain, @PathVariable String section, @PathVariable String subsection) throws ServletException, IOException {

        if (section != null && !"all".equals(section.toLowerCase())) {
            request.setAttribute("section", section);
        }
        if (subsection != null && !"all".equals(subsection.toLowerCase())) {
            request.setAttribute(SUBSECTION, subsection);
        }
        if (domain != null) {
            // User attempting to login with provided domain, build and OpenID request and redirect
            try {
                AuthRequest authRequest = startAuthentication(domain, request);
                String url = authRequest.getDestinationUrl(true);
                response.sendRedirect(url);
            } catch (OpenIDException e) {
                throw new ServletException("Error initializing OpenID request", e);
            }
        }
        return null;
    }

    /**
     * Handle the response from the OpenID Provider.
     *
     * @param request  Current servlet request
     * @param response Current servlet response
     * @throws ServletException if unable to process request
     * @throws IOException      if unable to process request
     */
    @RequestMapping(value = "/openidret", method = RequestMethod.GET)
    public ModelAndView handleResponse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("/openidret" + request.getQueryString());
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);
        try {
            setDefaultSection(request, response);
            UserInfo user = completeAuthentication(request);
            handleUserInfo(user, request, response); //decide what to do next
        } catch (OpenIDException e) {
            throw new ServletException("Error processing OpenID response", e);
        }
        return null;
    }

    @RequestMapping(value = "/oauth2ret", method = RequestMethod.GET)
    public ModelAndView handleOauth2Response(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("/oauth2ret" + request.getQueryString());
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);

        String code = request.getParameter("code");
        //if this method is called from google gadget then in the state we have the token which is saved in googlegadgetauth table
        String token = request.getParameter("state");
        if (!StringUtils.isEmpty(token)) {
            Boolean checkToken = googleGadgetService.checkToken(token);
            if (checkToken) {
                request.setAttribute("token", token);
            } else {
                request.setAttribute("token", INVALID_TOKEN);
            }
        }
        String returnUrl = EdsContextParams.getFullHost() + "mp/oauth2ret";
        setDefaultSection(request, response);
        OAuthResponse oauthResp = googleAuthorizationController.getAuthorization(EdsContextParams.getOauth2ConsumerKey(), EdsContextParams.getOauth2ConsumerSecret(), code, returnUrl);
        String refreshToken = oauthResp != null ? oauthResp.refresh_token : null;
        String accessToken = oauthResp != null ? oauthResp.access_token : null;
        System.out.println(refreshToken +" " + accessToken);
        HttpTransport httpTransport = new NetHttpTransport();

        // Make a request to access your profile and display it to console
        UserInfo user = new UserInfo();
        try {
            user = googleAuthorizationController.extractUserInfo(accessToken);
        } catch (Exception e) {
            logger.error("", e);
        }
//        UserInfo user = completeAuthentication(request);
        handleUserInfo(user, request, response); //decide what to do next
        return null;
    }

    @RequestMapping(value = "/gadgetret/{token}", method = RequestMethod.GET)
    public ModelAndView handleResponseGadget(HttpServletRequest request, HttpServletResponse response, @PathVariable String token) throws ServletException, IOException {
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);
        if (token != null && !token.equals("")) {
            request.setAttribute("token", token);
        }
        try {
            setDefaultSection(request, response);
            UserInfo user = completeAuthentication(request);
            handleUserInfo(user, request, response); //decide what to do next
        } catch (OpenIDException e) {
            throw new ServletException("Error processing OpenID response", e);
        }

        return null;
    }

    /**
     * Builds an auth request for a given OpenID provider.
     *
     * @param op      OpenID Provider URL.  In the context of Google Apps, this can be a naked domain
     *                name such as "saasycompany.com".  The length of the domain can exceed 100 chars.
     * @param request Current servlet request
     * @return Auth request
     * @throws org.openid4java.OpenIDException if unable to discover the OpenID endpoint
     */
    private AuthRequest startAuthentication(String op, HttpServletRequest request)
            throws OpenIDException {
        IdpIdentifier openId = new IdpIdentifier(op);

        String realm = realm(request);
        String returnToUrl = returnTo(request);

        AuthRequestHelper helper = consumerHelper.getAuthRequestHelper(openId, returnToUrl);
        addAttributes(helper);

        HttpSession session = request.getSession();
        AuthRequest authReq = helper.generateRequest();
        authReq.setRealm(realm);

        UiMessageRequest uiExtension = new UiMessageRequest();
        uiExtension.setIconRequest(true);
        authReq.addExtension(uiExtension);

        session.setAttribute("discovered", helper.getDiscoveryInformation());
        return authReq;
    }

    /**
     * Adds the requested AX attributes to the request
     *
     * @param helper Request builder
     */
    private void addAttributes(AuthRequestHelper helper) {
        helper.requestAxAttribute(Step2.AxSchema.EMAIL, true)
                .requestAxAttribute(Step2.AxSchema.FIRST_NAME, true)
                .requestAxAttribute(Step2.AxSchema.LAST_NAME, true)
                .requestAxAttribute(Step2.AxSchema.COUNTRY, true);
    }

    /**
     * Gets the <code>openid.return_to</code> URL to advertise to the IDP.  Dynamically constructs
     * the URL based on the current request.
     *
     * @param request Current servlet request
     * @return Return to URL
     */
    private String returnTo(HttpServletRequest request) {
        StringBuilder result = new StringBuilder(baseUrl(request));
        result.append(request.getContextPath());

        if (request.getAttribute("token") != null && !request.getAttribute("token").equals("")) {
            result.append("/mp/gadgetret/" + request.getAttribute("token"));
        } else {
            result.append(returnToPath);
        }

        Object section = request.getAttribute("section");
        Object subsection = request.getAttribute("subsection");
        if (section != null) {
            result.append("?" + Constants.SECTION_HTML + "=");
            result.append(section);
            result.append(".html");
            if (subsection != null) {
                result.append("&" + SUBSECTION + "=");
                result.append(subsection);
            }
        }
        return result.toString();
    }

    /**
     * Gets the realm to advertise to the IDP.  If not specified in the servlet configuration.
     * it dynamically constructs the realm based on the current request.
     *
     * @param request Current servlet request
     * @return Realm
     */
    private String realm(HttpServletRequest request) {
        if (StringUtils.isNotBlank(realm)) {
            return realm;
        } else {
            return baseUrl(request);
        }
    }

    /**
     * Dynamically constructs the base URL for the application based on the current request
     *
     * @param request Current servlet request
     * @return Base URL (path to servlet context)
     */
    private String baseUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder(request.getScheme())
                .append("://").append(request.getServerName());

        if ((request.getScheme().equalsIgnoreCase("http")
                && request.getServerPort() != 80)
                || (request.getScheme().equalsIgnoreCase("https")
                && request.getServerPort() != 443)) {
            url.append(":").append(request.getServerPort());
        }

        return url.toString();
    }


    /**
     * Validates the response to an auth request, returning an authenticated user object if
     * successful.
     *
     * @param request Current servlet request
     * @return User
     * @throws org.openid4java.OpenIDException if unable to verify response
     */

    UserInfo completeAuthentication(HttpServletRequest request)
            throws OpenIDException {
        logger.info("/completeAuthentication entering...");
        HttpSession session = request.getSession();
        ParameterList openidResp = Step2.getParameterList(request);
        String receivingUrl = currentUrl(request);
        DiscoveryInformation discovered = (DiscoveryInformation) session.getAttribute("discovered");


        AuthResponseHelper authResponse = consumerHelper.verify(receivingUrl, openidResp, discovered);
        if (authResponse.getAuthResultType() == AuthResponseHelper.ResultType.AUTH_SUCCESS) {
            logger.info("/completeAuthentication onSuccess");
            return onSuccess(authResponse, request);
        } else {
            logger.info("/completeAuthentication error");
        }
        return onFail(authResponse, request);
    }

    /**
     * Reconstructs the current URL of the request, as sent by the user
     *
     * @param request Current servlet request
     * @return URL as sent by user
     */
    String currentUrl(HttpServletRequest request) {
        return Step2.getUrlWithQueryString(request);
    }

    /**
     * Map the OpenID response into a user for our app.
     *
     * @param helper  Auth response
     * @param request Current servlet request
     * @return User representation
     */
    UserInfo onSuccess(AuthResponseHelper helper, HttpServletRequest request) {
        UserInfo userInfo = new UserInfo(helper.getClaimedId().toString(),
                helper.getAxFetchAttributeValue(Step2.AxSchema.EMAIL),
                helper.getAxFetchAttributeValue(Step2.AxSchema.FIRST_NAME),
                helper.getAxFetchAttributeValue(Step2.AxSchema.LAST_NAME),
                helper.getAxFetchAttributeValue(Step2.AxSchema.COUNTRY));
        userInfo.setUserNameBeforeAt(userInfo.getEmail().split("@")[0]);   //anvar.abidov
        userInfo.setDomain(userInfo.getEmail().split("@")[1]);    //edatasite.com
        return userInfo;
    }

    /**
     * Handles the case where authentication failed or was canceled.  Just a no-op
     * here.
     *
     * @param helper  Auth response
     * @param request Current servlet request
     * @return User representation
     */
    UserInfo onFail(AuthResponseHelper helper, HttpServletRequest request) {
        return null;
    }

//    private UserManager userManager;

    /**
     * Handles the user info
     *
     * @param userInfo The fetched user information
     * @param request  request
     * @param response response
     * @throws IOException      if.....
     * @throws ServletException if ...
     */
    public void handleUserInfo(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("Enter: handleUserInfo UserInfo: " + userInfo.toString());
        String firstName = userInfo.getFirstName();
        String lastName = userInfo.getLastName();
        String email = userInfo.getEmail();
        ServerUtils.fillHostParameters(request);
        String URL = request.getRequestURL().toString();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("First Name: ").append(firstName).append("<br/>");
        stringBuffer.append("Last Name: ").append(lastName).append("<br/>");
        stringBuffer.append("Email: ").append(email).append("<br/>");
        stringBuffer.append("URL: ").append(URL).append("<br/>");
        //At this point it makes sense to check the the user email with the login because the login is "immutable" :)
        DomainInfo domainInfo = signUpService.findByGoogleAppDomain(request.getServerName(), userInfo.getDomain(), userInfo.getEmail());
        logger.info("handleUserInfo UserInfo: " + userInfo.toString());

        String productName = request.getAttribute("productName").toString();
        if (domainInfo.isDomainExists()) { //if the domain has been registered from marketplace sometime in the past
            //just  login if the company registered with google apps domain
            //in case admin created an account for them
            if (domainInfo.isUserExists()) {
                if (request.getAttribute("token") != null && !request.getAttribute("token").equals("")) {
                    if (INVALID_TOKEN.equals(request.getAttribute("token"))) {
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/information.jsp");
                        ServerUtils.fillHostParameters(request);
                        request.setAttribute("message", "Invalid request! Refresh your gmail page.");
                        request.setAttribute("fromGoogleGadget", true);
                        dispatcher.forward(request, response);
                    }
                    Integer userAuthId = googleGadgetManager.getUserAuthIdByUsername(request.getServerName(), userInfo.getEmail());

                    GoogleGadgetDTO user = new GoogleGadgetDTO();
                    user.setUserAuthID(userAuthId);
                    user.setToken((String) request.getAttribute("token"));
                    googleGadgetManager.updateUserAuthId(user);
                    //redirect to imformation page which will close automatically in 5 seconds
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/information.jsp");
                    ServerUtils.fillHostParameters(request);
                    request.setAttribute("message", "Authorization Process Completed Successfully!");
                    request.setAttribute("fromGoogleGadget", true);
                    dispatcher.forward(request, response);
                }
                try {
                    signUpService.getParamsFromMarketPlace(stringBuffer, " Sign In ");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                logger.info(">>>>> S I N G I N >>>>> Google Market Place user SignIn");
                String requestLink = "/auth/federatedLogin";
                RequestDispatcher dispatcher = request.getRequestDispatcher(requestLink);
                request.setAttribute("username", userInfo.getEmail());
                dispatcher.forward(request, response);
            } else { //send to warning page(Seams like your Google apps domain is registered but you are out of members list)
                try {
                    stringBuffer.append("Message: Seems like your Google apps domain is registered with " + productName + " but you are out of members list<br/>");
                    signUpService.getParamsFromMarketPlace(stringBuffer, " Sign In Error");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/information.jsp");
                ServerUtils.fillHostParameters(request);
                request.setAttribute("message", "Seems like the Google apps domain - " + userInfo.getDomain() + " is registered with " + productName + ", but the " + userInfo.getEmail() + " account was not  authorized. " +
                        "<br /> Please refer to " + userInfo.getDomain() + " domain administrator!");
                dispatcher.forward(request, response);
            }

        } else {//the domain was not registered before
            boolean isAdmin = false;
            try {
                isAdmin = checkIfAdmin(userInfo, request);//check if the email owner is admin of the domain
            } catch (Throwable t) {
                t.printStackTrace();
                logger.debug("checkIfAdmin(userInfo)", t);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/information.jsp");
                ServerUtils.fillHostParameters(request);
                request.setAttribute("message", "Seems like you have revoked access to our application. " +
                        "Please, sign in to your Google App domain account on https://www.google.com/a/" + userInfo.getDomain() + "/\n" +
                        "and enable access to kpi.com application.");
                dispatcher.forward(request, response);
                return;
            }
            if (isAdmin) {//goto registration page
                try {
                    signUpService.getParamsFromMarketPlace(stringBuffer, "Sign Up");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                request.setAttribute("newCompany", createCompanyObject(userInfo));
                ServerUtils.fillHostParameters(request);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/marketplaceSignUp");

                //forward to federated sign up controller
                dispatcher.forward(request, response);
            } else {  //send to warning page(The domain is not registered and you are not the Google apps domain administrator to register the domain with workforcetrack)
                try {
                    stringBuffer.append("User warned that he/she can sign up only if he/she is marketplace domain administrator " + productName + "<br/>");
                    signUpService.getParamsFromMarketPlace(stringBuffer, "Sign Up Warning");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/information.jsp");
                ServerUtils.fillHostParameters(request);
                request.setAttribute("message", "The domain " + userInfo.getDomain() + " was not registered with " + productName + " and you are not the administrator of the domain to register it.");
                dispatcher.forward(request, response);
            }
        }
    }

    /**
     * Checks if the given email owner is an administrator of the domain
     *
     * @param userInfo - user related information
     * @return true - the email owner is admin false - not admin or unspecified(the admin property is optional according to gdata spec.)
     * @throws IOException      - io exception
     * @throws ServiceException - servlet exception
     */
    private boolean checkIfAdmin(UserInfo userInfo, HttpServletRequest request) throws IOException, ServiceException, OAuthException {
        String hostName = request.getAttribute("hostName").toString();
        logger.debug("checkIfAdmin:>>>" + hostName);
        String section, subsection = "";
        if (request.getParameter(SECTION_HTML) != null) {
            section = request.getParameter(SECTION_HTML).replace(".html", "");
        } else {
            section = "All";
        }
        if (request.getParameter(SUBSECTION) != null) {
            subsection = "#" + request.getParameter(SUBSECTION);
        }
        userInfo.setMarketplaceSection(section + subsection);
        //Registering and authorizing service
        String key = getMarketplaceKeyBySection(section, request.getServerName());
        String secret = getMarketplaceSecretBySection(section, request.getServerName());
        System.out.println("Key - " + key + " Secret - " + secret);
        try {
            HttpTransport httpTransport = new NetHttpTransport();
            GoogleCredential credential =
                    new GoogleCredential.Builder()
                            .setTransport(httpTransport)
                            .setJsonFactory(GoogleMarketplaceManager.JSON_FACTORY)
                            .setServiceAccountId(key)
                            .setServiceAccountUser(userInfo.getEmail())
                            .setServiceAccountScopes(GoogleMarketplaceManager.SCOPES)
                            .setServiceAccountPrivateKeyFromP12File(
                                    new File(secret)).build();

            Directory admin =
                    new Directory.Builder(httpTransport, GoogleMarketplaceManager.JSON_FACTORY, credential)
                            .setApplicationName("kpi.com")
                            .setHttpRequestInitializer(credential).build();

            User user = admin.users().get(userInfo.getEmail()).execute();
            return user.getIsAdmin();
        } catch (Exception e) {
            System.out.println(userInfo.getEmail());
            e.printStackTrace();
        }
        return false;
    }


    private NewCompany createCompanyObject(UserInfo user) {
        NewCompany company = new NewCompany();
        company.setAdminEmail(user.getEmail());
        company.setAdminFName(user.getFirstName());
        company.setAdminLName(user.getLastName());
        if (user.getCompany() != null) {
            company.setName(user.getCompany());
        } else if (user.getDomain() != null) {
            company.setName(user.getDomain() + " Company");
        } else if (company.getAdminFName() != null && company.getAdminFName().length() > 0) {
            company.setName(company.getAdminFName().trim().substring(0, 1).toUpperCase() + company.getAdminFName().trim().substring(1).toLowerCase() + " Company");
        } else {
            company.setName(company.getAdminEmail() + " Company");
        }
        company.setGoogleAppsDomain(user.getDomain());
        company.setGoogleAppsSection(user.getMarketplaceSection());
        logger.debug("NewCompany createCompanyObject:>>>" + user.toString());
        return company;
    }

    private void setDefaultSection(HttpServletRequest request, HttpServletResponse response) {
        String marketplaceSection = request.getParameter(Constants.SECTION_HTML);
        String marketplaceSubsection = request.getParameter(SUBSECTION);

        if (marketplaceSection != null && !marketplaceSection.equals("")) {
            if (marketplaceSubsection != null) {
                marketplaceSection = marketplaceSection + "#" + marketplaceSubsection;
            }

            Cookie sectionCookie = new Cookie(SECTION_HTML, marketplaceSection);
            sectionCookie.setPath("/");
            sectionCookie.setMaxAge(60 * 60 * 24);

            Cookie marketplaceCookie = new Cookie(FROM_MARKETPLACE, Boolean.TRUE.toString());
            marketplaceCookie.setPath("/");
            marketplaceCookie.setMaxAge(60 * 5);

            response.addCookie(sectionCookie);
            response.addCookie(marketplaceCookie);
            request.setAttribute(SECTION_HTML, marketplaceSection);
            request.setAttribute(FROM_MARKETPLACE, Boolean.TRUE);
        } else {//remove marketplace cookie if user signs up to all sections
            Cookie cookie = new Cookie(FROM_MARKETPLACE, null);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

    }

    public static String getMarketplaceKeyBySection(String sec) {
        return getMarketplaceKeyBySection(sec, EdsContextParams.getHost());
    }

    public static String getMarketplaceKeyBySection(String sec, String host) {
        Map<String, String> keys = new HashMap<>();
        ////        Getting section - key map from value in database which is stored in form of section1:key1;section2:key2:...sectionN:keyN
        ////        e.g. " All:296775148492-a15rcjqpknvvd9iup11eh28otmhfjovp@developer.gserviceaccount.com"
        try {
            EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(host);

            for (String sectionKey : hostSetting.getMarketplaceServiceAccount().split(";")) {
                String section = sectionKey.split(":", 2)[0];
                String key = sectionKey.split(":", 2)[1].trim();
                keys.put(section, key);
            }
        } catch (Exception ex) {
            System.out.println("Host" + host);
            System.out.println("Error while getting/parsing marketplace key/secret");
        }
        if (keys.get(sec) != null) return keys.get(sec);
        return keys.get("All");
    }

    public static String getMarketplaceSecretBySection(String sec) {
        return getMarketplaceSecretBySection(sec, EdsContextParams.getHost());
    }

    public static String getMarketplaceSecretBySection(String sec, String host) {
        Map<String, String> secrets = new HashMap<>();
//        Getting section - secret map from value in database which is stored in form of section1:key1;section2:key2:...sectionN:keyN
//        e.g. "All:/mnt/webapps/projects/app.workforcetrack.com/ROOT/WEB-INF/marketplace/f5717ba77753ea9cabcc645d59af8235ce1d48fd-privatekey.p12;"
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(host);
        for (String sectionSecret : hostSetting.getMarketplacePrivateKey().split(";")) {
            String section = sectionSecret.split(":", 2)[0];
            String key = sectionSecret.split(":", 2)[1].trim();
            secrets.put(section, key);
        }
        return secrets.get("All");
    }

}
