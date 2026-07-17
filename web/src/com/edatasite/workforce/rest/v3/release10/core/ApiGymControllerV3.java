package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.CrmAccountInvoiceTO;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.GymInOutTO;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsSmsSendItem;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceItemCustomFields;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.client.server.app.ClientSupplierAccessService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSocketObject;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.service.ApiGymService;
import com.edatasite.workforce.rest.v3.release10.core.service.AppleTokenValidator;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleAuthTO;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleUserDetailsTO;
import com.edatasite.workforce.rest.v3.release10.core.to.LocationSessionTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ScanResponseTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.GymCrmAccountTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.crmAccount.CrmAccountSaveDto;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiContactService;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiGymCrmAccountService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_ACTIVE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;

@Tag(name = "Gym", description = "Gym Public API")
@RestController
@RequestMapping(path = "/gym")
public class ApiGymControllerV3 {
    private final UserManager userManager;
    private final SessionService sessionService;
    private final CommonService commonService;
    private final ClientContactManager clientContactManager;
    private final GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private final AppleTokenValidator tokenValidator;
    private final MessageManager messageManager;
    private final SmsManager smsManager;
    private final ReferenceManager referenceManager;
    private final ClientSupplierAccessService clientSupplierAccessService;
    private final ApiContactService apiContactService;
    private final CrmContactManager crmContactManager;
    private final UserFingerPrintmanager userFingerPrintManager;
    private final ApiGymService apiGymService;
    private final ApiGymCrmAccountService apiGymCrmAccountService;
    private final InvoiceManager invoiceManager;
    private final CrmServiceLocal crmServiceLocal;
    private final CompanyCustomFieldsManager companyCFSettingsManager;

    public ApiGymControllerV3(UserManager userManager,
                              SessionService sessionService,
                              CommonService commonService,
                              ClientContactManager clientContactManager,
                              GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager,
                              AppleTokenValidator tokenValidator,
                              MessageManager messageManager,
                              SmsManager smsManager,
                              ReferenceManager referenceManager,
                              ClientSupplierAccessService clientSupplierAccessService,
                              ApiContactService apiContactService,
                              CrmContactManager crmContactManager,
                              UserFingerPrintmanager userFingerPrintManager,
                              ApiGymService apiGymService,
                              ApiGymCrmAccountService apiGymCrmAccountService,
                              InvoiceManager invoiceManager,
                              CrmServiceLocal crmServiceLocal,
                              CompanyCustomFieldsManager companyCFSettingsManager) {
        this.userManager = userManager;
        this.sessionService = sessionService;
        this.commonService = commonService;
        this.clientContactManager = clientContactManager;
        this.globalAuthJdbcSpringManager = globalAuthJdbcSpringManager;
        this.tokenValidator = tokenValidator;
        this.messageManager = messageManager;
        this.smsManager = smsManager;
        this.referenceManager = referenceManager;
        this.clientSupplierAccessService = clientSupplierAccessService;
        this.apiContactService = apiContactService;
        this.crmContactManager = crmContactManager;
        this.userFingerPrintManager = userFingerPrintManager;
        this.apiGymService = apiGymService;
        this.apiGymCrmAccountService = apiGymCrmAccountService;
        this.invoiceManager = invoiceManager;
        this.crmServiceLocal = crmServiceLocal;
        this.companyCFSettingsManager = companyCFSettingsManager;
    }


    @PostMapping(path = "/scan", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ScanResponseTO scanQr(@RequestParam String token) {
        String decrypt = EncryptionHelper.decrypt(token);
        String[] tokenSplit = decrypt.split(":");
        Integer companyId = Integer.parseInt(SecurityContext.getInstance().getCompanyId());
        if (isUnauthorizedCompany(Integer.parseInt(tokenSplit[0]), companyId))
            return new ScanResponseTO(false, "Unauthorized");

        EdsUser user = userManager.getUser();
        Integer crmAccountId = crmAccountId(user);
        var activePaidInvoice = invoiceManager.getPrioritizedInvoices(List.of(crmAccountId));
        Optional<CrmAccountInvoiceTO> firstActivePaidInvoice = crmServiceLocal.getFirstActivePaidInvoice(activePaidInvoice, crmAccountId);
        var expireDate = firstActivePaidInvoice.map(CrmAccountInvoiceTO::getInvoiceExpireDate)
                .orElse(null);
        var systemStatus = crmServiceLocal.invoicePaidStatus(expireDate);
        Optional<EdsInvoiceItem> edsInvoiceItem = firstActivePaidInvoice.map(CrmAccountInvoiceTO::getInvoiceId)
                .map(invoiceManager::get)
                .map(EdsInvoice::getInvoiceItems)
                .filter(Objects::nonNull)
                .filter(ii -> !ii.isEmpty())
                .map(ii -> ii.get(0));
        boolean isActive = List.of("Paid", "Оплачено", "To'langan").contains(systemStatus);
        if (isActive) {
            Optional<String> checkMembershipConstraints = checkMembershipConstraints(edsInvoiceItem, user);
            if (checkMembershipConstraints.isPresent()) {
                systemStatus = checkMembershipConstraints.get();
                isActive = false;
            }
        }

        if (user != null && user.getFullName() != null) {
            WebSocketServerObject notification = new WebSocketServerObject();
            List<EdsEmployee> receptions = userManager.getUsersByROLE(companyId, 102);

            RedisSocketObject redisSocketObject = new RedisSocketObject();
            notification.setData(systemStatus + "-#-" + user.getFullName());
            notification.setEventType(WfmUiEventType.ON_CHECKING_CHECKOUT_NOTIFICATION);
            for (EdsEmployee reception : receptions) {
                notification.setUserId(reception.getObjectID());
                redisSocketObject.setCompanyId(companyId);
                redisSocketObject.setWebSocketServerObject(notification);
                RedisClient.publish(redisSocketObject);
            }
        }
        apiGymService.createCase(user, Optional.ofNullable(systemStatus));

        return new ScanResponseTO(isActive, systemStatus);
    }

    private Optional<String> checkMembershipConstraints(Optional<EdsInvoiceItem> edsInvoiceItemOpt, EdsUser user) {
        String systemStatus = null;
        if (edsInvoiceItemOpt.isEmpty()) {
            return Optional.empty();
        }
        EdsInvoiceItem edsInvoiceItem = edsInvoiceItemOpt.get();
        EdsInvoiceItemCustomFields customFields = edsInvoiceItem.getCustomFields();
        if (customFields == null) {
            return Optional.empty();
        }
        EdsCompanyCustomFieldsSettings memberchipTypeCFS = companyCFSettingsManager.getByAliasName(ViewName.SaleInvoiceItem.name(), "MEMBERCHIP_TYPE");
        EdsCompanyCustomFieldsSettings numberOfVisitsCFS = companyCFSettingsManager.getByAliasName(ViewName.SaleInvoiceItem.name(), "NUMBER_OF_VISITS");
        EdsCompanyCustomFieldsSettings allowedHourFromCFS = companyCFSettingsManager.getByAliasName(ViewName.SaleInvoiceItem.name(), "ALLOWED_HOUR_FROM");
        EdsCompanyCustomFieldsSettings allowedHourToCFS = companyCFSettingsManager.getByAliasName(ViewName.SaleInvoiceItem.name(), "ALLOWED_HOUR_TO");
        TimeZone timeZone = user.getCompany().getTimeZone();
        Date companyCurrentTime = new Date(new Date().getTime() + timeZone.getRawOffset());
        String membershipType = customFields.getStringValue(memberchipTypeCFS.getColumnCode());
        if ("Number of Visits".equals(membershipType) && numberOfVisitsCFS != null) {
            Double numberOfVisits = customFields.getDoubleValue(numberOfVisitsCFS.getColumnCode());
            if (numberOfVisits != null && numberOfVisits > 0) {
                Date fromDate = edsInvoiceItem.getInvoice().getInvoiceDate();
                Date toDate = edsInvoiceItem.getInvoice().getDueDate();
                if (userFingerPrintManager.countAttendanceDays(user.getObjectID(), fromDate, toDate != null && toDate.before(companyCurrentTime) ? new Date(toDate.getTime() + 86400000L) : companyCurrentTime) >= numberOfVisits) {
                    systemStatus = "Limit reached";
                }
            }
        } else if ("Allowed Times".equals(membershipType) && allowedHourFromCFS != null && allowedHourToCFS != null) {
            String allowedHourFrom = customFields.getStringValue(allowedHourFromCFS.getColumnCode());
            String allowedHourTo = customFields.getStringValue(allowedHourToCFS.getColumnCode());
            if (allowedHourFrom != null && allowedHourTo != null && allowedHourFrom.contains(":") && allowedHourTo.contains(":")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime now = LocalTime.of(companyCurrentTime.getHours(), companyCurrentTime.getMinutes());
                    LocalTime from = LocalTime.parse(allowedHourFrom, formatter);
                    LocalTime to = LocalTime.parse(allowedHourTo, formatter);

                    if (now.isBefore(from) || now.isAfter(to)) {
                        systemStatus = "Out of allowed hours";
                    }
                } catch (DateTimeParseException e) {
                    // Handle invalid time format if necessary
                    systemStatus = "Invalid allowed hour format";
                }
            }
        }
        return Optional.ofNullable(systemStatus);
    }

    private Integer crmAccountId(EdsUser user) {
        EdsClientContact edsClientContact = clientContactManager.get(user.getObjectID());
        EdsCrmContact crmContact = edsClientContact.getCrmContact();
        EdsCrmAccount crmAccount = crmContact.getCrmAccount();

        return crmAccount.getObjectID();
    }

    private static boolean isUnauthorizedCompany(int tokenCompanyId, Integer companyId) {
        return tokenCompanyId != companyId;
    }

    @PostMapping(path = "/generate/location", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public String generateLocationQr(@RequestParam Integer locationId) {
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String database = ServerSecurityContext.getInstance().getDatabase();
        Integer userId = userManager.getUser().getObjectID();
        String encrypt = EncryptionHelper.encrypt(companyId + ":" + database + ":" + userId + ":" + locationId);
        return "https://api.qrserver.com/v1/create-qr-code/?size=350x350&margin=40&data=" + URLEncoder.encode(encrypt, StandardCharsets.UTF_8);
    }

    @PostMapping(path = "/scan/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public LocationSessionTO scanLocationQr(@RequestParam String token, HttpServletRequest request, HttpServletResponse response) throws RestException {
        String decrypt = EncryptionHelper.decrypt(token);
        String[] tokenSplit = decrypt.split(":");
        Integer companyId = Integer.valueOf(tokenSplit[0]);
        String database = tokenSplit[1];
        Integer userId = Integer.valueOf(tokenSplit[2]);
        Integer locationId = Integer.valueOf(tokenSplit[3]);
        AuthDetails authDetails = new AuthDetails(companyId, userId, database);
        authDetails.setUserAgent("restapi");
        ServerSecurityContext.getInstance().setDatabase(authDetails.getDatabase());
        try {
            String sessionId = sessionService.obtainSessionAndRegisterInSystem(request, response, authDetails);
            return new LocationSessionTO(sessionId, locationId);
        } catch (IOException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Session obtain exception", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/auth/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public GoogleUserDetailsTO grantCode(@RequestParam("code") String code, @RequestParam("scope") String scope, @RequestParam("authuser") String authUser, @RequestParam("prompt") String prompt) {
        return getOauthAccessTokenGoogle(code);
    }

    private GoogleUserDetailsTO getOauthAccessTokenGoogle(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = googleAuthParams(code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

        String url = "https://oauth2.googleapis.com/token";
        GoogleAuthTO response = restTemplate.postForObject(url, requestEntity, GoogleAuthTO.class);
        var profileDetailsGoogle = getProfileDetailsGoogle(response.getAccess_token());
        response.setEmail(profileDetailsGoogle.getEmail());
        response.setUser_id(profileDetailsGoogle.getId());
        globalAuthJdbcSpringManager.createOauthToken(response);
        return profileDetailsGoogle;
    }

    private MultiValueMap<String, String> googleAuthParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("redirect_uri", EdsContextParams.getHost() + "/services/api/v3/gym/auth/google");
        params.add("client_id", "799916480062-h16sa18cm9jq767d1kc51htjm7c8gm0r.apps.googleusercontent.com");
        params.add("client_secret", "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");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        params.add("scope", "openid");
        params.add("grant_type", "authorization_code");
        return params;
    }

    @GetMapping(path = "/google/user/details", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    private GoogleUserDetailsTO getProfileDetailsGoogle(@RequestParam String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        String url = "https://www.googleapis.com/oauth2/v2/userinfo";
        ResponseEntity<GoogleUserDetailsTO> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserDetailsTO.class);
        return response.getBody();
    }


    @PostMapping(path = "/auth/crm/account/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GymCrmAccountTO crmAccountLogin(@RequestBody CrmAccountSaveDto requestBody,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
        if (!globalAuthJdbcSpringManager.existsOAuthToken(requestBody.getEmail(), requestBody.getGoogleUserId())) {
            return new GymCrmAccountTO();
        }
        GymCrmAccountTO crmAccount = apiGymCrmAccountService.gymCrmAccount(request, response, null, null, requestBody.getEmail(), null);
        if (crmAccount.getSessionId() == null) {
            return new GymCrmAccountTO();
        }
        return crmAccount;
    }

    @PostMapping("/auth/apple/signin")
    public ResponseEntity<?> signInWithApple(@RequestBody String request) throws Exception {
        JSONObject jsonObject = new JSONObject(request);
        String identityToken = jsonObject.getString("identityToken");

        if (!tokenValidator.verifyAppleToken(identityToken, "com.praaktis-mobile")) {
            return ResponseEntity.status(401).body("Invalid Token");
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/sms", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public void sendSms(@RequestParam("phoneNumber") String phoneNumber) throws RestException {
        EdsSmsSendItem item = new EdsSmsSendItem();
        EdsUser currentUser = this.userManager.getUser();
        if (currentUser != null) item.setUserID(currentUser.getObjectID());
        item.setToNumber(phoneNumber);
        item.setSentDate(new Date());
        String verificationCode = globalAuthJdbcSpringManager.updateSmsCode(321864, phoneNumber);
        item.setMessageText("signup verification code on gym.kpi.com: %s".formatted(verificationCode));
        item.setProvider(smsManager.getDefault());
        messageManager.generateAndSendSms(item);
    }

    @PostMapping(path = "/sms/verify", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = MediaType.APPLICATION_JSON_VALUE)
    public GymCrmAccountTO verifySmsCode(@RequestParam Integer contactId, @RequestParam int code, HttpServletRequest request, HttpServletResponse response) {
        EdsCrmContact contact = crmContactManager.get(contactId);

        if (!isSmsCodeVerified(contact.getPrimaryPhone(), code)) {
            return new GymCrmAccountTO();
        }

        updateClientContactStatusToActive(contact.getPrimaryPhone());
        try {
            return apiGymCrmAccountService.gymCrmAccount(request, response, null, null, contact.getPrimaryPhone(), null);
        } catch (IOException e) {
            return new GymCrmAccountTO();
        }
    }

    private boolean isSmsCodeVerified(String phone, int code) {
        return globalAuthJdbcSpringManager.verifySmsCode(phone, code);
    }

    private void updateClientContactStatusToActive(String phone) {
        EdsReference activeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE);
        EdsCrmContact contact = crmContactManager.getByPhone(phone);
        Integer clientContactId = clientSupplierAccessService.enableAccess(contact.getObjectID(), false, false);
        apiContactService.updateClientContact(clientContactId, activeStatus);
    }

    @GetMapping(path = "/user/check-times", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResponseEntity<List<GymInOutTO>> getUserCheckInOutTimes(@RequestParam(value = "userId", required = false) Integer userId) {
        try {
            List<GymInOutTO> records = userFingerPrintManager.getUserGymInOutRecords(Optional.ofNullable(userId).orElse(userFingerPrintManager.getUser().getObjectID()));
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/user/checkin-count", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResponseEntity<Long> countAttendance(@RequestParam @Parameter(description = "ISO date format, e.g. 2025-06-02") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                                @RequestParam @Parameter(description = "ISO date format, e.g. 2025-06-02") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        EdsUser user = userManager.getUser();
        TimeZone timeZone = user.getCompany().getTimeZone();
        Date companyCurrentTime = new Date(new Date().getTime() + timeZone.getRawOffset());
        Long countAttendance = userFingerPrintManager.countAttendanceDays(user.getObjectID(), fromDate, toDate != null && toDate.before(companyCurrentTime) ? new Date(toDate.getTime() + 86400000L) : companyCurrentTime);
        return ResponseEntity.ok(countAttendance);
    }
}

