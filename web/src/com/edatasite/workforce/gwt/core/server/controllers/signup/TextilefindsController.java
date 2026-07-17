package com.edatasite.workforce.gwt.core.server.controllers.signup;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.AuthUtils;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CountryCallingCodeLayer;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Controller
public class TextilefindsController extends GeneralFreeSignUpController {

    private static final Logger log = LoggerFactory.getLogger(TextilefindsController.class);

    // textilefinds.com dan olgan username va password
    private static final String TF_USERNAME   = "ucu3ndhyc7";
    private static final String TF_PASSWORD   = "jbop8hdhdfb";
    private static final String TF_AUTH_URL   = "https://api.textilefinds.com/api/v1/kpi/authenticate";
    private static final String TF_REMOTE_URL = "https://api.textilefinds.com/api/v1/kpi/company-remote-id";

    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CrmServiceLocal crmServiceLocal;

    // =======================================================
    // ASOSIY ENDPOINT
    // textilefinds.com shu linkni beradi foydalanuvchiga:
    // https://erp.textilefinds.com/textilefinds-connect.html?token=xxx
    // =======================================================
    @RequestMapping(value = "/textilefinds-connect.html", method = RequestMethod.GET)
    public ModelAndView connect(HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        // 1. URL dan tokenni olamiz
        String token = request.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            return signupError("Token topilmadi");
        }

        // Loading page ga token uzatamiz
        ModelAndView mav = new ModelAndView("textilefindsLoading");
        mav.addObject("token", token);
        return mav;
    }

    @RequestMapping(value = "/textilefinds-connect.html", method = RequestMethod.POST)
    public ModelAndView connectProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. URL dan tokenni olamiz
        String token = request.getParameter("token");
        if (token == null || token.trim().isEmpty()) {
            return signupError("Token topilmadi");
        }

        // 2. textilefinds API ga token yuborib company ma'lumot olamiz
        String tfResponse = callTextilefindsApi(token);
        if (tfResponse == null) {
            return signupError("textilefinds API javob bermadi");
        }
        log.info("textilefinds response: {}", tfResponse);

        // 3. tariff tekshiramiz — tariffga qarab mos schema yaratiladi
        String tariff  = parseJsonField(tfResponse, "tariff");
        if (tariff == null) {
            return signupError("Tariff o'chgan yoki null");
        }

        // Tariffga qarab TemplateSchema tanlash
        TemplateSchema templateSchema;
        switch (tariff.toUpperCase()) {
            case "STARTER":
                templateSchema = TemplateSchema.TEXTILE_FINDS_STARTER;
                break;
            case "PRO":
                templateSchema = TemplateSchema.TEXTILE_FINDS_PRO;
                break;
            case "PREMIUM":
                templateSchema = TemplateSchema.TEXTILE_FINDS_PREMIUM;
                break;
            default:
                log.warn("Noma'lum tariff: {}", tariff);
                return signupError("Noma'lum tariff");
        }

        log.info("Tariff={}, schema={}", tariff, templateSchema);

        String tfCompanyId   = parseJsonField(tfResponse, "company_id");  // textilefinds dagi id: 1835
        String companyName   = parseJsonField(tfResponse, "company_name"); // textilefinds dagi company name
        String companyEmail   = parseJsonField(tfResponse, "email"); // textilefinds dagi company email
        String companyPhone   = parseJsonField(tfResponse, "phone"); // textilefinds dagi company phone
        String remoteId      = parseJsonField(tfResponse, "remote_id");    // bizning KPI dagi id, birinchi marta null keladi

        log.info("tf_company_id={}, company_name={}, remote_id={}",
                tfCompanyId, companyName, remoteId);
        // 4. remote_id null bo'lsa — bu company bizda yo'q, yangi yaratamiz
        if (remoteId == null || remoteId.equals("null")) {

            log.info("Yangi company yaratilmoqda: {}", companyName);

            // NewCompany object to'ldiramiz
            NewCompany newCompany = new NewCompany();
            newCompany.setHost(request.getServerName());
            newCompany.setName(companyName);
            newCompany.setAdminLName(""); // bo'sh bo'lsa ham kerak
            newCompany.setAdminFName(companyName);
            newCompany.setAdminEmail("tf_" + tfCompanyId + "@textilefinds.com"); // unique email
            newCompany.setAdminActive(true);
            newCompany.setActive(true);
            newCompany.setSignedUpPage("textilefinds");
            newCompany.setCountryID(228);        // O'zbekiston
            newCompany.setLocale("ru");
            newCompany.setCallCode("998");
            newCompany.setPhone("000000000");
            newCompany.setSetUp(false);
            newCompany.setAgreeWithCondition(true);
            newCompany.setRedirectToSettings(true);
            newCompany.setCurrencyID(73);        // So'm
            newCompany.setCompanySignedUpFrom("https://textilefinds.com");
            newCompany.setFromFederatedLogin(false);
            newCompany.setClientSingUpIPAddress(request.getRemoteAddr());
            newCompany.setValue3(tariff + " TextileFinds");
            newCompany.setTfData(companyEmail + "|" + companyPhone + "|" + tariff);

            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            SecurityContext.getInstance().removeCompanyId();
            ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            ServerSecurityContext.getInstance().removeCompanyId();

            // ApiAuthControllerV3 dagi registerCompany ni chaqiramiz
            ModelAndView mav = registerCompany(request, response, newCompany, templateSchema);

            // authDetails dan sessionId olamiz
            AuthDetails authDetails = (AuthDetails) mav.getModel().get("authDetails");
            if (authDetails == null) {
                return signupError("Company yaratishda xatolik — authDetails null");
            }

            String sessionId = sessionService.obtainSession(authDetails);
            Integer newCompanyId = authDetails.getCompanyID(); // bizning yangi company id

            log.info("Yangi company yaratildi. KPI company_id={}", newCompanyId);

            // 328384 company ga CUSTOMER yaratish
            String previousTenant = TenantContextHolder.getCustomerType();
            try {
                Integer textilefindsCompanyId = 328384;
                String targetDb = globalAuthJdbcSpringManager.getCompanyDatabaseName(textilefindsCompanyId);

                // 1. Oqimni (Thread) va Contextlarni yangi bazaga o'tkazamiz
                TenantContextHolder.setCustomerType(targetDb);
                SecurityContext.getInstance().setDatabase(targetDb);
                SecurityContext.getInstance().setCompanyId(textilefindsCompanyId.toString());
                ServerSecurityContext.getInstance().setDatabase(targetDb);
                ServerSecurityContext.getInstance().setCompanyId(textilefindsCompanyId);

                // 2. Tranzaksiyali Service metodini chaqiramiz (U faqat bitta parametr qabul qiladi endi)
                crmServiceLocal.createCustomerForTextilefinds(companyName);

            } catch (Exception e) {
                log.error("Customer yaratishda xatolik: {}", e.getMessage(), e);
            } finally {
                // 3. Xatolik bo'lsa ham, bo'lmasa ham, Contextni YANGI YARATILGAN COMPANY bazasiga qaytaramiz
                TenantContextHolder.setCustomerType(previousTenant);
                SecurityContext.getInstance().setDatabase(authDetails.getDatabase());
                SecurityContext.getInstance().setCompanyId(authDetails.getCompanyID().toString());
                ServerSecurityContext.getInstance().setDatabase(authDetails.getDatabase());
                ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            }

            // 5. textilefinds ga bizning company_id ni yuboramiz
            boolean remoteSet = sendRemoteId(token, tfCompanyId, sessionId);
            if (!remoteSet) {
                log.warn("remote_id yuborishda xatolik, lekin davom etamiz");
            }

            // 6. Cookie o'rnatib Accounting.html ga o'tamiz
            return AuthUtils.fillCookieValuesAndRedirectToTheSystem(
                    request, response, sessionId, "/Accounting.html"
            );

        } else {
            // remote_id bor — company bizda mavjud
            // remote_id = bizning KPI company id si
            Integer kpiCompanyId = Integer.parseInt(remoteId);
            log.info("Company mavjud, kpiCompanyId={}", kpiCompanyId);

            // Email ni yasaymiz — signup da ham xuddi shunday yaratilgan
            String adminEmail = "tf_" + tfCompanyId + "@textilefinds.com";

            String database = globalAuthJdbcSpringManager.getCompanyDatabaseName(kpiCompanyId);
            if (database == null) {
                log.error("Company uchun database topilmadi, kpiCompanyId={}", kpiCompanyId);
                return signupError("Bunday company mavjud emas");
            }
            SecurityContext.getInstance().setDatabase(database);
            SecurityContext.getInstance().setCompanyId(kpiCompanyId.toString());
            ServerSecurityContext.getInstance().setDatabase(database);
            ServerSecurityContext.getInstance().setCompanyId(kpiCompanyId);

            //vaqtinchaga ishlamaydi
            /*ShadowAccount shadowAccount = loginServiceLocal.getShadowAccount(kpiCompanyId);
            if (shadowAccount == null) {
                log.error("Company topilmadi yoki admin yo'q, companyId={}", kpiCompanyId);
                return signupError("Company topilmadi yoki admin yo'q");
            }*/

            EdsUser drUser = userManager.findUser(adminEmail, kpiCompanyId);
            if (drUser == null) {
                log.error("DR user topilmadi, email={}, companyId={}", adminEmail, kpiCompanyId);
                return signupError("DR user topilmadi");
            }

            AuthDetails authDetails = new AuthDetails(kpiCompanyId, drUser.getObjectID(), database);
            authDetails.setIpAddress(ServerUtils.obtainClientIP(request));
            String userAgent = request.getHeader("user-agent");
            authDetails.setUserAgent(userAgent != null ? userAgent : "UNDEFINED");

            String sessionId = sessionService.obtainSession(authDetails);

            // Ehtiyot uchun remote_id ni yangilaymiz
            try {
                sendRemoteId(token, tfCompanyId, sessionId);
            } catch (Exception e) {
                log.warn("remote_id yangilashda xatolik, lekin davom etamiz: {}", e.getMessage());
            }

            return AuthUtils.fillCookieValuesAndRedirectToTheSystem(
                    request, response, sessionId, "/Accounting.html"
            );
        }
    }

    @Override
    protected void createSignUpUsagePlan(Integer companyId, boolean isCurrencyGBP, int usersCount, String hostName, String pricingPackageNAME) {
        signupServiceLocal.createOneYearUsagePlan(companyId, hostName);
    }

    private ModelAndView signupError(String logMessage) {
        log.warn(logMessage);
        ModelAndView mav = new ModelAndView("textilefindsSignup");
        mav.addObject("newCompany", new NewCompany());
        mav.addObject("countryCallCodes", CountryCallingCodeLayer.getCountryCallCodes());
        mav.addObject("errorMessage", logMessage);
        return mav;
    }

    // =======================================================
    // textilefinds API ga token yuborish
    // POST https://api.textilefinds.com/api/v1/kpi/authenticate
    // Basic Auth + {"token": "..."}
    // =======================================================
    private String callTextilefindsApi(String token) {
        HttpURLConnection conn = null;
        try {
            String credentials = TF_USERNAME + ":" + TF_PASSWORD;
            String basicAuth   = "Basic " + Base64.encodeBase64String(
                    credentials.getBytes(StandardCharsets.UTF_8));

            String body = "{\"token\":\"" + token + "\"}";

            URL url = new URL(TF_AUTH_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            if (conn.getResponseCode() != 200) {
                log.error("textilefinds API status: {}", conn.getResponseCode());
                return null;
            }

            return readResponse(conn);

        } catch (Exception e) {
            log.error("textilefinds API xatolik: {}", e.getMessage(), e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // =======================================================
    // textilefinds ga bizning company_id ni yuborish
    // POST https://api.textilefinds.com/api/v1/kpi/company-remote-id
    // {"company_id": 1835, "remote_id": 4}
    // =======================================================
    private boolean sendRemoteId(String token, String tfCompanyId, String sessionId) {
        HttpURLConnection conn = null;
        try {
            String credentials = TF_USERNAME + ":" + TF_PASSWORD;
            String basicAuth   = "Basic " + Base64.encodeBase64String(
                    credentials.getBytes(StandardCharsets.UTF_8));

            // Buni yozing (remote_id string):
            String body = "{\"company_id\":" + tfCompanyId + ",\"remote_id\":\"" + sessionId + "\"}";

            URL url = new URL(TF_REMOTE_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", basicAuth);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            log.info("remote_id yuborish status: {}", status);
            return status == 200;

        } catch (Exception e) {
            log.error("remote_id yuborishda xatolik: {}", e.getMessage(), e);
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    // =======================================================
    // HTTP response o'qish
    // =======================================================
    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
        );
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    // =======================================================
    // JSON dan field olish
    // =======================================================
    private String parseJsonField(String json, String field) {
        String key = "\"" + field + "\":";
        int idx = json.indexOf(key);
        if (idx < 0) return null;

        int start = idx + key.length();

        // string qiymat: "company_name":"test"
        if (start < json.length() && json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            if (end < 0) return null;
            return json.substring(start, end);
        }

        // number yoki null: "company_id":1835 yoki "remote_id":null
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        if (end < 0) return null;
        String value = json.substring(start, end).trim();
        return value.equals("null") ? null : value;
    }
}