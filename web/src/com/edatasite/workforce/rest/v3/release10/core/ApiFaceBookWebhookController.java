package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.social.facebook.model.LeadResponse;
import com.edatasite.workforce.gwt.core.server.app.social.facebook.model.WebHookEvent;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.ApiTelegramWebHookController;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Facebook Webhook", description = "Facebook Webhook API")
@RestController
@RequestMapping(value = "/facebook")
public class ApiFaceBookWebhookController extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiTelegramWebHookController.class);
    @Autowired
    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    CrmContactManager crmContactManager;
    @Autowired
    CrmAccountManager crmAccountManager;
    @Autowired
    ProfileManager profileManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private ReferenceManager referenceManager;

    @Operation(summary = "Webhook Verification")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Webhook verified"))
    @RequestMapping(value = "/updates/{companyId}", method = RequestMethod.GET)
    public String verifyFaceBookWebhook(@PathVariable final String companyId, @RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String verifyToken,
                                        @RequestParam("hub.challenge") String challenge) {
        if (mode != null && verifyToken != null) {
            if (mode.equals("subscribe") && verifyToken.equals("token_2001")) {
                System.out.println("Webhook Verified");
                return challenge;
            }
        }
        return null;
    }

    @Operation(summary = "Webhook LeadGen")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Webhook LeadGen"))
    @PostMapping("/updates/{companyId}")
    public ResponseEntity<String> sentFaceBookWebhook(
            @PathVariable String companyId,
            @RequestBody WebHookEvent request
    ) {
        try {
            initSecurityContext(companyId);

            var value = request.entry().get(0).changes().get(0).value();

            String leadId = value.leadgen_id();
            String pageId = value.page_id();
            String formId = value.form_id();

            if ("444444444444".equals(leadId)) {
                return ResponseEntity.ok("ok");
            }

            boolean isCityRentCar = "844317558774070".equals(pageId);
            boolean isPromotion = "748522538353887".equals(pageId);

            String pageAccessToken = getPageAccessTokenByPageId(pageId);

            LeadResponse leadObj = fetchLeadFromFacebook(leadId, pageAccessToken);

            if (isCityRentCar) {
                initSecurityContext("84600");
                processCityRentCarOpportunity(leadObj);
            }else if (isPromotion) {
                initSecurityContext("328088");
                processRegularLead(leadObj, "328088");
            } else {
                processRegularLead(leadObj,companyId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("ok");
    }


    private void initSecurityContext(String companyId) {
        SecurityContext.getInstance().setCompanyId(companyId);
        SecurityContext.getInstance().setDatabase(
                globalAuthJdbcSpringManager.getCompanyDatabaseName(Integer.valueOf(companyId))
        );
        SecurityContext.getInstance().setStaticUserID(
                userManager.getAdmin(Integer.valueOf(companyId)).getObjectID()
        );
    }


    private LeadResponse fetchLeadFromFacebook(String leadId, String pageToken) throws Exception {

        String url = "https://graph.facebook.com/v21.0/" + leadId +
                "?fields=created_time,field_data,form_id,ad_id,ad_name,campaign_name,platform" +
                "&access_token=" + pageToken;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);

        return new ObjectMapper().readValue(response, LeadResponse.class);
    }

    private void processRegularLead(LeadResponse leadObj, String companyId) {

        ParsedLeadData data = parseFields(leadObj.getFieldData(), false);

        ContactListItem lead = new ContactListItem();
        lead.setContactType(ContactListItem.LEAD_CONTACT);
        lead.setFirstName(data.fullName);
        lead.setPrimaryEmail(data.email);
        lead.setWorkEmail(data.email);
        lead.setLeadSource(leadObj.getCampaignName());

        if (!data.phone.isBlank()) {
            lead.setPhones(Map.of(2, new ArrayList<>(List.of(data.phone))));
        }

        if (!data.companyName.isBlank()) {
            lead.setCrmAccount(createAccount(data.companyName,null));
        }

        EdsCampaign campaign = null;
        if (leadObj.getCampaignName() != null) {
            campaign = campaignManager.getCampaignByName(leadObj.getCampaignName());
        }
        if (campaign != null) {
            lead.setSyncID(0);
            lead.setCampaignId(campaign.getObjectID());
        }
        lead.setNote(data.description.append(" source: " + (leadObj.getCampaignName() != null ? leadObj.getCampaignName() : "")).toString());

        if (companyId.equals("65159")) {
            lead.setLeadStatus(new SelectItem(2082));
        } else {
            lead.setLeadStatus(new SelectItem(1880));
        }
        lead.setLeadSourceID("ig".equals(leadObj.getPlatform()) ? 1430 : 1337);

        Address address = new Address();
        address.setCountryId(228);
        address.setCountryCode("UZ");
        address.setPrimary(true);
        lead.setPrimaryAddress(address);

        crmService.saveLead(lead, null);
    }


    private ParsedLeadData parseFields(List<LeadResponse.FieldData> fields, boolean isCityRentCar) {

        ParsedLeadData data = new ParsedLeadData();

        for (var field : fields) {
            if (field.getValues() == null || field.getValues().isEmpty()) continue;

            String name = field.getName().toLowerCase();
            String value = field.getValues().get(0).trim();

            if (isCityRentCar) {
                switch (name) {
                    case "full_name", "first_name" -> data.fullName = value;
                    case "class" -> data.carClass = value;
                    case "email" -> data.email = value;
                    case "phone", "work_phone_number", "phone_number" -> data.phone = value;
                }
            } else {
                switch (name) {
                    case "full_name","имя:","ismingiz:" -> data.fullName = value;
                    case "company_name" -> data.companyName = value;
                    case "email" -> data.email = value;
                    case "phone","phone_number","номер_телефона:","номеr_телеfонa:","telefon_raqamingiz:" -> data.phone = value;
                }
            }

            data.description.append(name).append(": ").append(value).append("\n");
        }

        return data;
    }

    private void processCityRentCarOpportunity(LeadResponse leadObj) {

        ParsedLeadData data = parseFields(leadObj.getFieldData(), true);

        CrmAccountItem account = createAccount(data.fullName,data.phone);

        OpportunityListItem opportunity = new OpportunityListItem();
        opportunity.setOpportunityName(data.fullName);
        opportunity.setContactPrimaryEmail(data.email);
        opportunity.setCrmAccountItem(account);
        opportunity.setNotes(
                new ArrayList<>(
                        List.of(new HistoryListItem(data.description.append(" source: " + (leadObj.getCampaignName() != null ? leadObj.getCampaignName() : "")).toString() ))
                )
        );
        opportunity.setAssigneeId(40);
        opportunity.setCampaignId(2);

        opportunity.setContactPrimaryPhone(data.phone);
        opportunity.setStage(new SelectItem(1868)); // New
        opportunity.setStageId(1868);
        opportunity.setClosingDate(new Date());
        opportunity.setLeadSourceId("ig".equals(leadObj.getPlatform()) ? 332 : 329);
        opportunity.setNumberData(crmServiceLocal.generateOpportunityNumber());

        EdsCampaign campaign = null;
        if (leadObj.getCampaignName() != null) {
            campaign = campaignManager.getCampaignByName(leadObj.getCampaignName());
        }
        if (campaign != null) {
            opportunity.setCampaignId(campaign.getObjectID());
        }


        crmServiceLocal.saveOpportunity(opportunity);
    }

    private CrmAccountItem createAccount(String companyName, String phone){
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(companyName);
        EdsUser user = countryManager.getUser();
        crmAccountItem.setPhone(phone);
        SelectItem owner = new SelectItem(user.getObjectID(), user.getName());
        crmAccountItem.setOwnerItems(new SelectItem[]{owner});

        Integer crmAccountID = crmServiceLocal.saveAccount(
                crmAccountItem,
                null,
                null,
                false,
                false,
                false,
                true
        );

        crmAccountItem.setObjectId(crmAccountID);
    return crmAccountItem;
    }


    private void sendConversionAPIEvent(String email, String phone, String sourceUrl, boolean isInternational) {
        try {
            String pixelId = "1327422285724476";
            String accessToken = "EAANRrCDW9q4BPo4UOTMQ5RTSZAgDCZAkm8aHQO3ozB5d7rSPahMhXJkZBtX1pDXNBT5aIz47oktjdKotyHCHZBHr9SjoLBISdMnsj0KSTD8wkTT4N1QvDUL4yaEOEZBhvZCz366nQYdJgiBvdOXZCJSF8yyEMd4yh4XLaAZBIm7vMLV0avyujN1ldrMxMoh2nwZDZD";

            String url = "https://graph.facebook.com/v19.0/" + pixelId + "/events?access_token=" + accessToken;

            long eventTime = System.currentTimeMillis() / 1000;

            Map<String, Object> userData = new HashMap<>();
            if (email != null && !email.isEmpty()) {
                userData.put("em", sha256(email.toLowerCase().trim()));
            }
            if (phone != null && !phone.isEmpty()) {
                userData.put("ph", sha256(phone.replaceAll("\\D+", "")));
            }

            Map<String, Object> event = new HashMap<>();
            event.put("event_name", "Lead");
            event.put("event_time", eventTime);
            event.put("action_source", "website");
            event.put("event_source_url", sourceUrl);
            event.put("user_data", userData);

            Map<String, Object> data = new HashMap<>();
            data.put("data", Collections.singletonList(event));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(url, request, String.class);

            System.out.println("Facebook Conversion API response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error sending Conversion API event: " + e.getMessage());
        }
    }

    public String getPageAccessTokenByPageId(String pageId) {

        String systemUserToken = "EAAHvY79OJKgBRPpfHjoNZAxDOT8oGGpFyu9NgPej84ZBeZB4M7FB5o9zcJzgUZCq0VlZAeB451hqpZAaX7IPnSPD10KwRVIfygsMa3vLl68lmTnX4epKaPXHofZBUUrZBLs9MWC3AuNliklVzo3eiZB2IaPV8zoaQs99sPI2KOJ1v7yHW2DksQ6rnZBqyEfM1wjB3E5FRRxxfKNr1vDzkYooZCUKehGEcIgfZCseyFdzr6myqBC1ZBgNO0BaNZA5XG7Wy3e65vBe4nVLJ7y7EjFDINMZCV1EedZB07cWi3P4ZCNwZAK6YjyMjQWuTAxgZDZDr";

        String url = "https://graph.facebook.com/v21.0/" + pageId
                + "?fields=access_token"
                + "&access_token=" + systemUserToken;

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                return response.getBody().get("access_token").toString();
            }

            throw new RuntimeException("Page access token not found for pageId=" + pageId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get page token for pageId=" + pageId, e);
        }
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    static class ParsedLeadData {
        String fullName = "";
        String companyName = "";
        String email = "";
        String phone = "";
        String carClass = "Unknown";
        StringBuilder description = new StringBuilder();
    }


}
