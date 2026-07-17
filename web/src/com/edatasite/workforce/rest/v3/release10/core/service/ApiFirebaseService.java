package com.edatasite.workforce.rest.v3.release10.core.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.settings.EdsFirebaseCredentials;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.FirebaseCredentialsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.firebase.ActionDto;
import com.edatasite.workforce.rest.v3.release10.core.to.firebase.message.Message;
import com.edatasite.workforce.rest.v3.release10.core.to.firebase.message.MessageSend;
import com.edatasite.workforce.rest.v3.release10.core.to.firebase.message.Notification;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.OPPORTUNITY_ADD;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.OPPORTUNITY_EDIT;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.TASK_ADD;
import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.TASK_UPDATE;

@Service
public class ApiFirebaseService {

    private final FirebaseCredentialsManager firebaseCredentialsManager;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final TaskManager taskManager;
    private final OpportunityManager opportunityManager;
    private final WfmMessageSource commonLocalizer;

    public ApiFirebaseService(FirebaseCredentialsManager firebaseCredentialsManager,
                              TaskManager taskManager,
                              OpportunityManager opportunityManager,
                              @Qualifier("commonLocalizer") WfmMessageSource commonLocalizer) {
        this.firebaseCredentialsManager = firebaseCredentialsManager;
        this.taskManager = taskManager;
        this.opportunityManager = opportunityManager;
        this.commonLocalizer = commonLocalizer;
    }

    @Transactional
    public void saveCredentialFromJson(MultipartFile file) throws IOException {
        JsonNode json = mapper.readTree(file.getInputStream());

        EdsFirebaseCredentials credential = new EdsFirebaseCredentials();
        credential.setType(json.get("type").asText());
        credential.setProjectId(json.get("project_id").asText());
        credential.setPrivateKeyId(json.get("private_key_id").asText());
        credential.setPrivateKey(json.get("private_key").asText());
        credential.setClientEmail(json.get("client_email").asText());
        credential.setClientId(json.get("client_id").asText());
        credential.setAuthUri(json.get("auth_uri").asText());
        credential.setTokenUri(json.get("token_uri").asText());
        credential.setAuthProviderX509CertUrl(json.get("auth_provider_x509_cert_url").asText());
        credential.setClientX509CertUrl(json.get("client_x509_cert_url").asText());
        credential.setUniverseDomain(json.get("universe_domain").asText());
        firebaseCredentialsManager.deleteFirebaseCredential();
        firebaseCredentialsManager.create(credential);
    }

    public String generateAssertionToken() throws Exception {
        EdsFirebaseCredentials firebaseCredential = firebaseCredentialsManager.getFirebaseCredential();
        if (firebaseCredential == null) throw new RuntimeException("Firebase Credential not found");
        Instant now = Instant.now();
        Instant exp = now.plus(1, ChronoUnit.HOURS);

        Algorithm algorithm = Algorithm.RSA256(null, getPrivateKey(firebaseCredential.getPrivateKey()));

        String jwt = JWT.create()
                .withIssuer(firebaseCredential.getClientEmail())
                .withAudience(firebaseCredential.getTokenUri())
                .withClaim("scope", "https://www.googleapis.com/auth/firebase.messaging")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);

        firebaseCredentialsManager.updateFirebaseCredential(jwt, Date.from(now), Date.from(exp));
        return jwt;
    }

    @Transactional
    public Map<String, String> generateAccessToken() throws Exception {
        String assertionToken = generateAssertionToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        body.add("assertion", assertionToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE,
                    "Invalid response from API: " + response.getStatusCode(),
                    ApiConstants.INVALID,
                    HttpStatus.BAD_REQUEST);
        }
        DynamicDto data = mapper.readValue(response.getBody(), DynamicDto.class);
        String accessToken = data.getProperties() != null ? (String) data.getProperties().get("access_token") : null;

        firebaseCredentialsManager.updateFirebaseAccessToken(accessToken);

        Map<String, String> res = new HashMap<>();
        res.put("assertionToken", assertionToken);
        res.put("accessToken", accessToken);
        return res;
    }

    private RSAPrivateKey getPrivateKey(String privateKeyPem) throws Exception {
        String privateKeyClean = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKeyClean);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) kf.generatePrivate(keySpec);
    }

    public void sendNotification(Integer id, String action, String column, Map<String, String> authKeys) {
        if (action == null) return;
        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Constants.DEFAULT_SPRING_PROFILES);
        boolean validateOnly = !profile.contains("apps") && !"65159".equals(ServerSecurityContext.getInstance().getCompanyId());
        List<ActionDto> actions = switch (action) {
            case TASK_ADD, TASK_UPDATE -> handleTaskEvent(id, action, column, validateOnly);
            case OPPORTUNITY_ADD, OPPORTUNITY_EDIT -> handleOpportunityEvent(id, action, validateOnly);
            default -> List.of();
        };
        for (ActionDto actionDto : actions) {
            sendFirebaseNotification(actionDto, authKeys);
        }
    }

    private List<ActionDto> handleTaskEvent(Integer id, String action, String column, boolean validateOnly) {
        EdsTask edsTask = taskManager.get(id);
        if (edsTask == null) return List.of();
        // fixme, now I need to go though that chain, replace with sql query for better performance
        List<String> deviceTokens = edsTask.getAssignments().stream()
                .filter(Objects::nonNull)
                .map(EdsEmployeeTask::getProjectEmployee)
                .filter(Objects::nonNull)
                .map(EdsProjectEmployee::getEmployeeDepartment)
                .filter(Objects::nonNull)
                .map(EdsEmployeeDepartment::getEmployee)
                .filter(Objects::nonNull)
                .map(EdsEmployee::getDeviceToken)
                .filter(Objects::nonNull)
                .toList();
        ActionDto actionDto = new ActionDto();
        actionDto.setId(id);
        actionDto.setAction(action);
        actionDto.setDeviceTokens(deviceTokens);
        actionDto.setValidateOnly(validateOnly);
        String title;
        String body;

        if (TASK_ADD.equals(action)) {
            title = commonLocalizer.localize("newTask");
            body = commonLocalizer.localizeWithParam("newTaskAssigned", edsTask.getNumber());
        } else if (TASK_UPDATE.equals(action) && column.equals("status")) {
            title = commonLocalizer.localize("statusChanged");
            body = commonLocalizer.localizeWithParam("taskStatusChanged", edsTask.getNumber(), edsTask.getStatus().getName());
        } else {
            return List.of();
        }
        actionDto.setTitle(title);
        actionDto.setBody(body);
        return List.of(actionDto);
    }

    private List<ActionDto> handleOpportunityEvent(Integer id, String action, boolean validateOnly) {
        EdsOpportunity edsOpportunity = opportunityManager.get(id);
        List<String> deviceTokens = new ArrayList<>();
        EdsEmployee assignee = edsOpportunity.getAssignee();
        if (assignee != null) {
            deviceTokens.add(assignee.getDeviceToken());
        }
        EdsEmployee backupAssignee = edsOpportunity.getBackupAssignee();
        if (backupAssignee != null) {
            deviceTokens.add(backupAssignee.getDeviceToken());
        }
        ActionDto actionDto = new ActionDto();
        actionDto.setId(id);
        actionDto.setAction(action);
        actionDto.setDeviceTokens(deviceTokens);
        actionDto.setTitle("New opportunity");
        actionDto.setBody("Opportunity " + edsOpportunity.getNumber() + " is assigned to you");
        actionDto.setValidateOnly(validateOnly);
        return List.of(actionDto);
    }

    private void sendFirebaseNotification(ActionDto actionDto, Map<String, String> authKeys) {
        Message message = new Message();
        message.setData(Map.of("id", String.valueOf(actionDto.getId()), "type", actionDto.getAction()));
        Notification notification = new Notification();
        notification.setTitle(actionDto.getTitle());
        notification.setBody(actionDto.getBody());
        message.setNotification(notification);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authKeys.get("accessToken"));
        for (String deviceToken : actionDto.getDeviceTokens()) {
            message.setToken(deviceToken);
            MessageSend messageSend = new MessageSend(actionDto.isValidateOnly(), message);

            restTemplate.exchange("https://fcm.googleapis.com/v1/projects/kpi-mobile-6064f/messages:send", HttpMethod.POST, new HttpEntity<>(messageSend, headers), String.class); // TODO update project id dynamically when needed
        }
    }
}
