package com.edatasite.workforce.gwt.core.server.controllers.login;

import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.GoogleAuthorizationController;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.rpc.SignUpItem;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleAuthTO;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleUserDetailsTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.gdata.client.authn.oauth.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.gwtwidgets.server.spring.ServletUtils;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.Parameter;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * Class is used to provide login/signup for Google and Yahoo federated logins
 * class methods used through both Google and Yahoo
 */
@Controller
public class FederatedLoginController extends BaseLoginController {

    private static Logger log = LoggerFactory.getLogger(FederatedLoginController.class);
    private final ConsumerManager manager;

    @Autowired
    private SignUpService signupService;
    @Autowired
    private GoogleManager googleManager;

    private DiscoveryInformation discovered;

    @Autowired
    GoogleAuthorizationController googleAuthorizationController;


    private final static String YAHOO_OPEN_ID = "https://me.yahoo.com";
    private final static String GOOGLE_ID = "https://www.google.com/accounts/o8/id";

    public FederatedLoginController() {
        manager = new ConsumerManager();
    }

    /**
     * method send request to the federated login web services (google,yahoo,..)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/check")
    public void sendRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userString = GOOGLE_ID;
        ServerUtils.fillHostParameters(request);

        if (request.getParameter("GOOGLE_DOMAIN") != null) {
            //check if the domain is previously registered already, then redirect to Google marketplce URL
            String googleDomain = request.getParameter("GOOGLE_DOMAIN");
            DomainInfo domainInfo = signupService.findByGoogleAppDomain(request.getServerName(), googleDomain, "");
            Cookie gd = new Cookie("GOOGLE_DOMAIN", request.getParameter("GOOGLE_DOMAIN"));
            gd.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(gd);
            response.sendRedirect("/mp/openid/" + googleDomain + "/All");
            return;
        } else if (request.getParameter("ID_PROVIDER") != null) {
            userString = request.getParameter("ID_PROVIDER");
        }
        if (GOOGLE_ID.equals(userString)) {
            response.sendRedirect(googleManager.getAuthSubURL(USER_INFO, EdsContextParams.getFullHost() + "oauth2verify"));
            return;
        }
        try {
            String host = request.getHeader("host").replaceFirst("www.", "");//if header contains "www.", just remove it

            // configure the return_to URL where your application will receive
            // the authentication responses from the OpenID provider

            String realm = "http://" + host + "/verify";
            String returnToUrl = "http://" + host + "/verify?tokenRequest=true";

            //login type, forexample : customLogin -- > when encrypted link send to customLogin page, loginType=link
            String loginType = request.getParameter("loginType");
            if (loginType != null) {
                returnToUrl += "?loginType=" + loginType;
            }

            // perform discovery on the user-supplied identifier
            List discoveries = manager.discover(userString);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            discovered = manager.associate(discoveries);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl);
            authReq.setHandle("");
            authReq.setRealm(realm);

            FetchRequest fetch = FetchRequest.createFetchRequest();
            if (YAHOO_OPEN_ID.equals(userString)) {
                // AX, requesting attributes for Yahoo
                fetch.addAttribute("nickname", "http://axschema.org/namePerson/friendly", true);
                fetch.addAttribute("email", "http://axschema.org/contact/email", true);
                fetch.addAttribute("fullname", "http://axschema.org/namePerson", true);
                fetch.addAttribute("language", "http://axschema.org/pref/language", true);
                fetch.addAttribute("timezone", "http://axschema.org/pref/timezone", false);
                fetch.addAttribute("gender", "http://axschema.org/person/gender", false);
            }


            // attach the extension to the authentication request
            authReq.addExtension(fetch);

            if (host.contains(":")) {//for localhost implementation
                host = host.substring(0, host.indexOf(':'));
            }

            StringBuilder add = new StringBuilder();
            if (YAHOO_OPEN_ID.equals(userString)) {
                String hostName = request.getAttribute("hostName").toString();
                add.append("&openid.sreg.required=email&openid.sreg.policy_url=http%3A%2F%2F" + request + "%2Fprivacy");
            }

            response.sendRedirect(authReq.getDestinationUrl(true) + add.toString());//used for OpenId + OAuth hybrid authentication

        } catch (OpenIDException e) {
            // present error to the user
            log.error(e.getMessage(), e);
        }
    }

    /*@RequestMapping(value = "/oauth2verify", method = RequestMethod.GET)
    public ModelAndView handleOauth2Response(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);

        String code = request.getParameter("code");
        String returnUrl = EdsContextParams.getFullHost() + "oauth2verify";

        OAuthResponse oauthResp = googleAuthorizationController.getAuthorization(EdsContextParams.getOauth2ConsumerKey(), EdsContextParams.getOauth2ConsumerSecret(), code, returnUrl);
        String refreshToken = oauthResp != null ? oauthResp.refresh_token : null;
        String accessToken = oauthResp != null ? oauthResp.access_token : null;
        System.out.println(refreshToken);
        HttpTransport httpTransport = new NetHttpTransport();

        // Make a request to access your profile and display it to console

        try {
            UserInfo user = googleAuthorizationController.extractUserInfo(accessToken);
            return forwardToSignInOrSignUp(new SignUpItem(user.getClaimedId(), user.getEmail(), user.getFirstName(), user.getLastName(), accessToken, RegistrationTypeEnum.GOOGLE), request, response);
        } catch (Exception ex) {
            response.sendRedirect("/index.html");
        }
        return null;
    }*/

    /*
    * After Google Plus Signin API became depricated we implemented this one
    * */
    @RequestMapping(value = "/google-oauth2-verify", method = {RequestMethod.POST/*, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.PATCH*/})
    public ModelAndView handleGoogleOauth2Verify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);

        String code = request.getParameter("code");
        String returnUrl = EdsContextParams.getFullHost() ;
        if(StringUtils.isNotBlank(returnUrl) && returnUrl.endsWith("/")) {
            returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf("/"));
        }

        if (ServletUtils.getRequest() != null && ServletUtils.getRequest().getServerName() != null) {
            System.out.println("Servername: " + ServletUtils.getRequest().getServerName());
//            returnUrl = "https://" + ServletUtils.getRequest().getServerName();
        }
        System.out.println("RETURN_URL OF GOOGLE SIGNIN: " + returnUrl);
        GoogleTokenResponse tokenResponse = googleAuthorizationController.getAuthorizationNew(EdsContextParams.getOauth2ConsumerKey(),
                EdsContextParams.getOauth2ConsumerSecret(),
                code, returnUrl);

        /*String refreshToken = tokenResponse != null ? tokenResponse.getRefreshToken() : null;*/
        String accessToken = tokenResponse != null ? tokenResponse.getAccessToken() : null;

        // Make a request to access your profile and display it to console
        try {
//            UserInfo user = googleAuthorizationController.extractUserInfo(accessToken);
            // Get profile info from ID token
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            return forwardToSignInOrSignUp(new SignUpItem(payload.getSubject(), payload.getEmail(), (String) payload.get("given_name"), (String) payload.get("family_name"),
                    accessToken, RegistrationTypeEnum.GOOGLE), request, response);

        } catch (Exception ex) {
            response.sendRedirect("/index.html");
        }
        return null;
    }

    @RequestMapping(value = "/google-oauth2-verify", method = {RequestMethod.GET/*, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.PATCH*/})
    public ModelAndView handleGoogleOauth2VerifyCustom(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);

        String code = request.getParameter("code");
        String returnUrl = EdsContextParams.getFullHost() ;
        if(StringUtils.isNotBlank(returnUrl) && returnUrl.endsWith("/")) {
            returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf("/"));
        }

        if (ServletUtils.getRequest() != null && ServletUtils.getRequest().getServerName() != null) {
            System.out.println("Servername: " + ServletUtils.getRequest().getServerName());
//            returnUrl = "https://" + ServletUtils.getRequest().getServerName();
        }
        System.out.println("RETURN_URL OF GOOGLE SIGNIN: " + returnUrl);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = googleAuthParams(EdsContextParams.getOauth2ConsumerKey(),EdsContextParams.getOauth2ConsumerSecret(),code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        GoogleAuthTO tokenResponse = restTemplate.postForObject(url, requestEntity, GoogleAuthTO.class);
        var profileDetailsGoogle = getProfileDetailsGoogle(tokenResponse.getAccess_token());

        try {
            return forwardToSignInOrSignUp(new SignUpItem(profileDetailsGoogle.getId(), profileDetailsGoogle.getEmail(), profileDetailsGoogle.getGiven_name(), profileDetailsGoogle.getFamily_name(),
                    tokenResponse.getAccess_token(), RegistrationTypeEnum.GOOGLE), request, response);

        } catch (Exception ex) {
            response.sendRedirect("/index.html");
        }
        return null;
    }

    private MultiValueMap<String, String> googleAuthParams(String client_id, String client_secret, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", EdsContextParams.getHost() + "/google-oauth2-verify");
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");
        return params;
    }

    private GoogleUserDetailsTO getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<GoogleUserDetailsTO> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserDetailsTO.class);
        return response.getBody();
    }


    /**
     * method handle response, provide signup or login process
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/verify")
    public ModelAndView handlingResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        if (request.getParameter("tokenRequest") == null) {
            PrintWriter writer = response.getWriter();
            response.setContentType("application/xrds+xml");
            String realm = "http://" + request.getHeader("host") + "/verify";
            String xrd = "<xrds:XRDS xmlns:xrds=\"xri://$xrds\" xmlns:openid=\"http://openid.net/xmlns/1.0\" xmlns=\"xri://$xrd*($v*2.0)\">\n" +
                    "<XRD>\n" +
                    "<Service xmlns=\"xri://$xrd*($v*2.0)\">\n" +
                    "<Type>http://specs.openid.net/auth/2.0/return_to</Type>\n" +
                    "<URI>" + realm + "</URI>\n" +
                    "</Service>\n" +
                    "</XRD>\n" +
                    "</xrds:XRDS>";
            writer.print(xrd);
        } else {
            try {
                ServerUtils.fillHostParameters(request);
                //check for verified
                // extract the parameters from the authentication response
                // (which comes in as a HTTP request from the OpenID provider)

                ParameterList responseParams = new ParameterList(request.getParameterMap());
                //response.removeParameters("openid.claimed_id");
                //response.removeParameters("openid.identity");

                /* This hack is for distunguishing between normal Google Login and GoogleApps login
               OpenID for java library is not capable for handling GoogleApps login properly, therefore we are adding following prefixes.
                */
                if (request.getParameter("openid.mode") != null && request.getParameter("openid.mode").contains("cancel")) {
                    System.out.println("Cancel was clicked by user, redirecting to index.html");
                    response.sendRedirect("/index.html");
                    return null;
                }
                String endPoint = responseParams.getParameter("openid.op_endpoint").getValue();
                if (!"https://www.google.com/accounts/o8/ud".equals(endPoint) && !"https://open.login.yahooapis.com/openid/op/auth".equals(endPoint)) {
                    responseParams.set(new Parameter("openid.claimed_id", "https://www.google.com/accounts/o8/user-xrds?uri=" + responseParams.getParameter("openid.claimed_id").getValue()));
                    responseParams.set(new Parameter("openid.identity", "https://www.google.com/accounts/o8/user-xrds?uri=" + responseParams.getParameter("openid.identity").getValue()));
                }


                System.out.println("SIZE IS =" + request.getParameterMap().size());

                // retrieve the previously stored discovery information
                /*DiscoveryInformation discovered = (DiscoveryInformation)
                request.getSession().getAttribute("openid-disc");*/

                // extract the receiving URL from the HTTP request
                StringBuffer receivingURL = request.getRequestURL();

                String queryString = request.getQueryString();
                //queryString = queryString.replaceAll("openid.claimed_id=","openid.claimed_id=https%3A%2F%2Fwww.google.com%2Faccounts%2Fo8%2Fuser-xrds?uri=");
                //queryString = queryString.replaceAll("openid.identity=","openid.identity=https%3A%2F%2Fwww.google.com%2Faccounts%2Fo8%2Fuser-xrds?uri=");

                if (queryString != null && queryString.length() > 0) {
                    receivingURL.append("?").append(queryString);
                }

                // verify the response; ConsumerManager needs to be the same
                // (static) instance used to place the authentication request
                VerificationResult verification = manager.verify(receivingURL.toString(), responseParams, null);

                // examine the verification result and extract the verified identifier
                //verification.

                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
                if (verification.getVerifiedId() == null
                        && verification.getStatusMsg() == null
                        && authSuccess != null) {

                    verification.setVerifiedId(manager.getDiscovery().parseIdentifier(authSuccess.getClaimed()));
                }
                Identifier verified = verification.getVerifiedId();

                String accessToken;
                boolean fromYahoo;

                if (verified != null) {

                    if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                        log.info(">>>>> S I N G I N >>>>> OpenId user SignIn");
                        //getting ACCESS TOKEN
                        accessToken = getAccessToken(request);

                        //define yahoo login
                        fromYahoo = authSuccess.getParameterValue("openid.op_endpoint").contains("yahoo");

                        //user login
                        FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
                        String id = fetchResp.getAttributeValue("id");
                        String email = fetchResp.getAttributeValue("email");
                        String firstName = fetchResp.getAttributeValue("firstname");
                        String lastName = fetchResp.getAttributeValue("lastname");
                        String language = fetchResp.getAttributeValue("language");
                        String country = fetchResp.getAttributeValue("country");
                        System.out.println(email + " lan:" + language + "country: " + country);

                        if (fromYahoo) {
                            String[] fullname = fetchResp.getAttributeValue("fullname").split(" ");
                            firstName = fullname[0];
                            lastName = fullname[1];
                        }
                        if (!fromYahoo && (accessToken != null)) {
                            loginServiceLocal.registrGoogleServices(email, accessToken);
                        }

                        return forwardToSignInOrSignUp(new SignUpItem(id, email, firstName, lastName, accessToken, RegistrationTypeEnum.GOOGLE), request, response);
                    }
                }
                response.sendRedirect("/index.html");
            } catch (OpenIDException e) {
                response.sendRedirect("/index.html");
                throw new RuntimeException(e.getMessage());
            }
        }
        return null;
    }


    //request handler methods END


    private String getAccessToken(HttpServletRequest httpReq) {
        String accessToken = null;
        String oauthToken = httpReq.getParameter("openid.ext2.request_token");//used for OpenId+OAuth hybrid authentication
        log.info("oauthToken: " + oauthToken);
        if (oauthToken != null) {
            //We are getting these parametres from www.google.com/accounts/manageDomains.
            //At this site we have registered our application and google get us these below two keys for OAuth.

            String consumerKey = httpReq.getAttribute("auth2ConsumerKey").toString();
            String signatureKey = httpReq.getAttribute("auth2ConsumerSecret").toString();
            String hostName = httpReq.getAttribute("hostName").toString();

            System.out.println("Host is  = " + hostName);
            log.debug("ConsumerKey: " + consumerKey);
            log.debug("Signature Key: " + signatureKey);
            log.debug("OAuthToken: " + oauthToken);

            GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
            oauthParameters.setOAuthConsumerKey(consumerKey);
            oauthParameters.setOAuthConsumerSecret(signatureKey);
            oauthParameters.setOAuthToken(oauthToken);
            oauthParameters.setScope("https://docs.google.com/feeds/");


            OAuthSigner signer = new OAuthHmacSha1Signer();
            GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);

            try {
                accessToken = oauthHelper.getAccessToken(oauthParameters);
            } catch (OAuthException ex) {
                ex.printStackTrace();
            }
        }

        return accessToken;

    }

}
