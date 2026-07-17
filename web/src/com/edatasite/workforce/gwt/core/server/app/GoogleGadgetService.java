package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleGadgetManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.GoogleGadgetDTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthServiceProvider;
import net.oauth.SimpleOAuthValidator;
import net.oauth.signature.RSA_SHA1;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 25.05.12
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
@Service("googleGadgetService")
public class GoogleGadgetService {
    private static final Logger log = LoggerFactory.getLogger(GoogleGadgetService.class);

    public static final String DATE_PATTERN = "MMM dd, yyyy";
    public static final String SAVED = "saved";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String OPEN_SOCIAL_VIEWER_ID = "opensocial_viewer_id";
    public static final String USER_EXISTS = "user_exists";
    public static final String TOKEN = "token";
    public static final String TRUE = "true";
    public static final String ERROR_MESSAGE = "error_msg";
    public static final String OPENID_URL = "openid_url";
    public static final String COMPANY_ID = "companyId";
    public static final String PERMISSIONS = "permissions";
    public static final String GOOGLE_GADGET_IS_ENABLE = "google_gadget_is_enable";
    public static final String GOOGLE_APP_DOMAIN = "google_app_domain";
    //messages
    public static final String YOU_ARE_NOT_AUTHORIZED = "You are not authorized";
    public static final String YOUR_REQUEST_IS_NOT_SIGNED = "Your request is not signed";
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String PLEASE_CHOOSE_YOUR_COMPANY = "Please choose your company";
    public static final String RELATION_SAVED_FAILED = "Relation saved failed!";

    private final static String CERTIFICATE =
            """
                    -----BEGIN CERTIFICATE-----
                    MIIDBDCCAm2gAwIBAgIJAK8dGINfkSTHMA0GCSqGSIb3DQEBBQUAMGAxCzAJBgNV
                    BAYTAlVTMQswCQYDVQQIEwJDQTEWMBQGA1UEBxMNTW91bnRhaW4gVmlldzETMBEG
                    A1UEChMKR29vZ2xlIEluYzEXMBUGA1UEAxMOd3d3Lmdvb2dsZS5jb20wHhcNMDgx
                    MDA4MDEwODMyWhcNMDkxMDA4MDEwODMyWjBgMQswCQYDVQQGEwJVUzELMAkGA1UE
                    CBMCQ0ExFjAUBgNVBAcTDU1vdW50YWluIFZpZXcxEzARBgNVBAoTCkdvb2dsZSBJ
                    bmMxFzAVBgNVBAMTDnd3dy5nb29nbGUuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GN
                    ADCBiQKBgQDQUV7ukIfIixbokHONGMW9+ed0E9X4m99I8upPQp3iAtqIvWs7XCbA
                    bGqzQH1qX9Y00hrQ5RRQj8OI3tRiQs/KfzGWOdvLpIk5oXpdT58tg4FlYh5fbhIo
                    VoVn4GvtSjKmJFsoM8NRtEJHL1aWd++dXzkQjEsNcBXwQvfDb0YnbQIDAQABo4HF
                    MIHCMB0GA1UdDgQWBBSm/h1pNY91bNfW08ac9riYzs3cxzCBkgYDVR0jBIGKMIGH
                    gBSm/h1pNY91bNfW08ac9riYzs3cx6FkpGIwYDELMAkGA1UEBhMCVVMxCzAJBgNV
                    BAgTAkNBMRYwFAYDVQQHEw1Nb3VudGFpbiBWaWV3MRMwEQYDVQQKEwpHb29nbGUg
                    SW5jMRcwFQYDVQQDEw53d3cuZ29vZ2xlLmNvbYIJAK8dGINfkSTHMAwGA1UdEwQF
                    MAMBAf8wDQYJKoZIhvcNAQEFBQADgYEAYpHTr3vQNsHHHUm4MkYcDB20a5KvcFoX
                    gCcYtmdyd8rh/FKeZm2me7eQCXgBfJqQ4dvVLJ4LgIQiU3R5ZDe0WbW7rJ3M9ADQ
                    FyQoRJP8OIMYW3BoMi0Z4E730KSLRh6kfLq4rK6vw7lkH9oynaHHWZSJLDAp17cP
                    j+6znWkN9/g=
                    -----END CERTIFICATE-----""";


    @Autowired
    GoogleGadgetManager googleGadgetManager;
    @Autowired
    AllInOneService allInOneService;
    @Autowired
    @Qualifier("allInOneService")
    AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    UserManager userManager;
    @Autowired
    CompanyManager companyManager;
    @Autowired
    RolePermissionService rolePermissionService;
    @Autowired
    SessionService sessionService;
    @Autowired
    NumberingSettingsManager numberingSettingsManager;
    @Autowired
    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    public Boolean checkSignedRequest(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        String method = request.getMethod();
        String url = getUrl(request, false);

        try {
            OAuthServiceProvider provider = new OAuthServiceProvider(null, null, null);
            OAuthConsumer consumer = new OAuthConsumer(null, "www.google.com", null, provider);
            consumer.setProperty(RSA_SHA1.X509_CERTIFICATE, CERTIFICATE);

            List<OAuth.Parameter> requestParameters = new ArrayList<>();

            for (Map.Entry<String, String[]> e : params.entrySet()) {
                Map.Entry<String, String[]> entry = e;
                String entryKey = entry.getKey();
                for (String value : entry.getValue()) {
                    requestParameters.add(new OAuth.Parameter(entry.getKey(), value));
                    if (entryKey.equals("companyId") || entryKey.equals("google_app_domain")) {
                        log.info("{} - {}", entryKey, value);
                    }
                }
            }
            log.info("url - {}", url);
            OAuthMessage message = new OAuthMessage(method, url, requestParameters);

            OAuthAccessor accessor = new OAuthAccessor(consumer);
            message.validateMessage(accessor, new SimpleOAuthValidator());
            return true;
        } catch (OAuthProblemException ope) {
            log.error(ope.getProblem());
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean googleGadgetSignIn(String openSocialId, Integer companyID) {
        if (openSocialId != null) {
            ArrayList<GoogleGadgetDTO> googleGadgetDTO = googleGadgetManager.authByOpenSocialId(openSocialId);
            if (googleGadgetDTO != null && googleGadgetDTO.size() > 0 && googleGadgetDTO.get(0).getUserID() != null) {
                ArrayList<GoogleGadgetDTO> checked = checkUserAccess(googleGadgetDTO);
                for (GoogleGadgetDTO gg : checked) {
                    ServerSecurityContext.getInstance().setDatabase(gg.getClusterDomain());
                    ServerSecurityContext.getInstance().setCompanyId(gg.getCompanyID());
                    ServerSecurityContext.getInstance().setStaticUserID(gg.getUserID());
                    if (gg.getCompanyID().equals(companyID)) {
                        break;
                    }
                }
                return ServerSecurityContext.getInstance().getUser() != null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ArrayList<String> getCompanyForCurrentUser(String openSocialId) {
        ArrayList<String> companys = new ArrayList<>();
        ArrayList<GoogleGadgetDTO> googleGadgetDTO = googleGadgetManager.authByOpenSocialId(openSocialId);

        ArrayList<GoogleGadgetDTO> checked = checkUserAccess(googleGadgetDTO);
        for (GoogleGadgetDTO gg : checked) {
            ServerSecurityContext.getInstance().setDatabase(gg.getClusterDomain());
            ServerSecurityContext.getInstance().setCompanyId(gg.getCompanyID());
            EdsCompany company = companyManager.get(gg.getCompanyID());
            companys.add(gg.getCompanyID() + "::" + company.getName());
        }
        return companys;

    }

    private ArrayList<GoogleGadgetDTO> checkUserAccess(ArrayList<GoogleGadgetDTO> googleGadgetDTOs) {
        ArrayList<GoogleGadgetDTO> checked = new ArrayList<>();
        for (GoogleGadgetDTO gg : googleGadgetDTOs) {
            ServerSecurityContext.getInstance().setDatabase(gg.getClusterDomain());
            ServerSecurityContext.getInstance().setCompanyId(gg.getCompanyID());
            EdsCompany company = companyManager.get(gg.getCompanyID());
            EdsUser user = null;
            if (company != null) {
                try {
                    user = userManager.get(gg.getUserID());
                } catch (Exception e) {
                    //schema dosnt exists
                }
            }
            if (company != null && user != null && !user.getDeleted() && Constants.EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
                if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                    continue;
                }
                if (user instanceof EdsClientContact) {
                    if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                        continue;
                    }
                }

                checked.add(gg);
            }
        }
        return checked;
    }


    public void addGoogleGadgetTokenOpenSocialId(String token, String openSocialId) {
        GoogleGadgetDTO googleGadgetAuth = new GoogleGadgetDTO();
        googleGadgetAuth.setToken(token);
        googleGadgetAuth.setOpenSocialID(openSocialId);
        googleGadgetManager.insertTokenAndOpenSocialID(googleGadgetAuth);

    }

    public Boolean checkToken(String token) {
        ArrayList<GoogleGadgetDTO> googleGadgetAuth = googleGadgetManager.findToken(token);
        return googleGadgetAuth != null && googleGadgetAuth.size() > 0;
    }


    public String getUrl(HttpServletRequest request, Boolean onlyHostname) {
        StringBuilder requestUrl = new StringBuilder();
        String scheme = request.getScheme();
        int port = request.getLocalPort();

        requestUrl.append(scheme);
        requestUrl.append("://");
        requestUrl.append(request.getServerName());

        if ((scheme.equals("http") && port != 80)
                || (scheme.equals("https") && port != 443)) {
            requestUrl.append(":");
            requestUrl.append(port);
        }
        if (!onlyHostname) {
            requestUrl.append(request.getContextPath());
            requestUrl.append(request.getServletPath());
        }

        return requestUrl.toString();
    }

    public Integer getInteger(String number) {
        StringBuilder nNumber = new StringBuilder();
        if (number != null && !number.equals("")) {
            for (char n : number.toCharArray()) {
                if (!Character.isDigit(n)) {
                    return null;
                } else {
                    nNumber.append(n);
                }
            }
            return Integer.parseInt(nNumber.toString());
        } else {
            return null;
        }
    }

    public boolean saveRelation(String[] relations, Integer relationId, String type, Email email, Boolean linkToEmail) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();
        if (relations != null) {
            for (String relation : relations) {
                if (relation != null && !"".endsWith(relation)) {
                    String[] relationData = relation.split("::");
                    if (relationData.length > 0) {
                        RelationItem relationItem = new RelationItem();
                        relationItem.setFromType(type);
                        relationItem.setToType(relationData[0]);
                        if (relationData[1] != null && !relationData[1].equals("")) {
                            relationItem.setToID(Integer.parseInt(relationData[1]));
                        }
                        relationItem.setToName(relationData[2]);
                        relationItems.add(relationItem);
                    }
                }
            }
        }
        if (linkToEmail) {
            Integer systemEmailTrackerID = allInOneServiceLocal.saveGoogleGadgetMail(email);
            RelationItem relationItem = new RelationItem();
            relationItem.setFromType(type);
            relationItem.setToType(RelationItem.TYPE_EMAIL_TRACKER);
            relationItem.setToID(systemEmailTrackerID);
            relationItem.setToName(email.getSubject());
            relationItems.add(relationItem);
        }
        if (relationItems.size() > 0) {
            ArrayList<RelationItem> saved = allInOneService.saveRelations(type, relationId, email.getSubject(), relationItems, true);
            return saved != null && saved.size() > 0;
        } else {
            return true;
        }
    }

    public HashMap<String, Boolean> getPermissionsForCurrentUser() {
        HashMap<String, Boolean> permissions = new HashMap<>();

        HashSet<String> permissionListCRM = rolePermissionService.getPermissionList(PermissionConstants.CRM_CONTEXT);
        permissions.put(PermissionConstants.CRM_CASE_ADD, permissionListCRM.contains(PermissionConstants.CRM_CASE_ADD) || permissionListCRM.contains(PermissionConstants.ADD_NEW_CASE));
        permissions.put(PermissionConstants.ADD_NEW_LEAD, permissionListCRM.contains(PermissionConstants.ADD_NEW_LEAD));
        permissions.put(PermissionConstants.CRM_CONTACT_ADD, permissionListCRM.contains(PermissionConstants.CRM_CONTACT_ADD) || permissionListCRM.contains(PermissionConstants.CRM_ADD_NEW_CONTACT));
        permissions.put(PermissionConstants.CRM_TASKS_ADD, permissionListCRM.contains(PermissionConstants.CRM_TASKS_ADD));
        permissions.put(PermissionConstants.CRM_MAIN_MENU, permissionListCRM.contains(PermissionConstants.CRM_MAIN_MENU));
        permissions.put(PermissionConstants.CRM_OPPORTUNITIES_ADD, permissionListCRM.contains(PermissionConstants.CRM_OPPORTUNITIES_ADD));

        HashSet<String> permissionListPM = rolePermissionService.getPermissionList(PermissionConstants.PM_CONTEXT);
        permissions.put(PermissionConstants.PM_TASKS_ADD, permissionListCRM.contains(PermissionConstants.PM_TASKS_ADD));
        permissions.put(PermissionConstants.PM_MAIN_MENU, permissionListCRM.contains(PermissionConstants.PM_MAIN_MENU));


        HashSet<String> permissionListAccounting = rolePermissionService.getPermissionList(PermissionConstants.ACCOUNTING_CONTEXT);
        permissions.put(PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD, permissionListAccounting.contains(PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD));
        permissions.put(PermissionConstants.ACCOUNTING_MAIN_MENU, permissionListCRM.contains(PermissionConstants.ACCOUNTING_MAIN_MENU));
        return permissions;
    }

    public boolean isGoogleGagdetEnabled(String googleAppDomain) {
        CompanyDomain companyDomain = globalAuthJdbcSpringManager.findByGoogleAppDomain(googleAppDomain);
        if (companyDomain != null) {
            return companyDomain.isGadgetEnabled();
        }
        return false;
    }

    public HashMap<String, Boolean> getPermissionsViewForCurrentUser() {
        HashMap<String, Boolean> permissions = new HashMap<>();

        HashSet<String> permissionListCRM = rolePermissionService.getPermissionList(PermissionConstants.CRM_CONTEXT);
        permissions.put(PermissionConstants.CRM_CASES_LIST, permissionListCRM.contains(PermissionConstants.CRM_CASES_LIST));
        permissions.put(PermissionConstants.CRM_LEADS_LIST, permissionListCRM.contains(PermissionConstants.CRM_LEADS_LIST));
        permissions.put(PermissionConstants.CRM_CONTACTS_LIST, permissionListCRM.contains(PermissionConstants.CRM_CONTACTS_LIST));
        permissions.put(PermissionConstants.CRM_TASKS_LIST, permissionListCRM.contains(PermissionConstants.CRM_TASKS_LIST));
        permissions.put(PermissionConstants.CRM_ACCOUNTS_LIST, permissionListCRM.contains(PermissionConstants.CRM_ACCOUNTS_LIST));
        permissions.put(PermissionConstants.CRM_OPPORTUNITIES_LIST, permissionListCRM.contains(PermissionConstants.CRM_OPPORTUNITIES_LIST));

        return permissions;
    }

    public String getTaskNumberingFormat(Integer number) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null) {
            return settings.getTaskNumberingFormat();
        } else {
            NumberData settingsDefault = EdsNumberingSettings.getDefaultData(number, EdsNumberingSettings.DEF_TASK_PREFIX/*false*/);
            if (settingsDefault != null) {
                return settingsDefault.getNumberFormat();
            } else {
                return "";
            }
        }
    }

}
