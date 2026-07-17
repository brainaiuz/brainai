package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.AiReportService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("aiReportService")
public class AiReportServiceImpl implements AiReportService {
    private static final String ASK_KIA = "https://aiassistant.kpi.com/api/v1/chat/ask_kia";
    private static final String GET_CHAT = "https://aiassistant.kpi.com/api/v1/chat/get_chat?user_id={userId}&company_id={companyId}";
    private static final String CREATE_CHAT = "https://aiassistant.kpi.com/api/v1/chat/create_chat";
    private static final String AI_API_KEY = "RgiCnxuQ1s953x8";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String[] getAiResponse(String question, boolean reportGenerationEnabled, Long chatId) {

        if (false) {
            return getFromLocalData(question);
        }

        String[] responseAi = new String[3];

        if (chatId == null || chatId <= 0) return responseAi;

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("query", question);
        requestMap.put("chat_id", chatId);
        requestMap.put("api_key", AI_API_KEY);
        requestMap.put("attachment_id", "some-value"); // Optional, If is needed
        requestMap.put("report_generation", reportGenerationEnabled);// optional, default false
        requestMap.put("user_locale", ServerUtils.getUserLocale());


        String requestDataJson = new Gson().toJson(requestMap);

        // 2. Audio fayl yo‘q bo‘lishi mumkin
        File audio = null; // yoki new File("path/to/audio.wav");

        ResponseEntity<Map> response = sendMultipartRequest(ASK_KIA, requestDataJson, audio);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            String markdownAnswer = (String) responseBody.get("answer");
            responseAi[0] = toHtml(markdownAnswer);

            Object reportObject = responseBody.get("report");
            if (reportObject != null) {
                try {
                    String reportJson = new Gson().toJson(reportObject);
                    ReportRpc report = new Gson().fromJson(reportJson, ReportRpc.class);

                    if (report != null) {
                        UUID uuid = UUID.randomUUID();
                        RedisClient.setKey(uuid.toString(), report, ReportRpc.class, 1800);
                        responseAi[1] = uuid.toString();
                        if (report.getChartConf() != null && report.getChartConf().getType() != null){
                            responseAi[2] = report.getChartConf().getType().toString();
                        }
                    }
                } catch (JsonSyntaxException | IllegalStateException e) {
                    // log error if needed, but do not crash
                    responseAi[1] = null; // explicit handling
                }
            }
        }

        return responseAi;
    }

    @Override
    public Long getOrCreateChatId(Integer userId, String companyId) {
        Map<String, Object> createBody = Map.of(
                "user_id", userId.toString(),
                "company_id", companyId
        );

        ResponseEntity<Map> createResponse = sendPostRequest(CREATE_CHAT, createBody);

        if (createResponse.getStatusCode().is2xxSuccessful() && createResponse.getBody() != null) {
            Object chatIdObj = createResponse.getBody().get("chat_id");
            if (chatIdObj instanceof Number) {
                return ((Number) chatIdObj).longValue();
            }
        }

        return null;
    }

    @Override
    public List<Map<String, String>> getChatHistory(Long chatId, Integer limit) {
        if (chatId == null || limit <= 0) return List.of();

        String url = "https://api-kpi.jumanazarov.me/api/v1/chat/get_chat_history?chat_id=" + chatId + "&limit=" + limit;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<Map<String, String>> messages = new ArrayList<>();
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Object history = response.getBody().get("history");
            if (history instanceof List<?>) {
                for (Object item : (List<?>) history) {
                    if (item instanceof Map<?, ?>) {
                        Map<?, ?> raw = (Map<?, ?>) item;
                        Map<String, String> message = new HashMap<>();
                        message.put("role", String.valueOf(raw.get("role")));
                        message.put("content", String.valueOf(raw.get("content")));
                        messages.add(message);
                    }
                }
            }
        }

        return messages;
    }

    private ResponseEntity<Map> sendPostRequest(String url, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, entity, Map.class);
    }

    private ResponseEntity<Map> sendMultipartRequest(String url, String requestDataJson, File audioFile) {
        MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();

        if (requestDataJson != null) {
            multipartRequest.add("request_data", requestDataJson);
        }

        if (audioFile != null && audioFile.exists()) {
            multipartRequest.add("audio_file", new FileSystemResource(audioFile));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multipartRequest, headers);
        return restTemplate.postForEntity(url, entity, Map.class);
    }

    public static String toHtml(String markdown) {
        String html = markdown;
        html = html.replaceAll("\\{(.+?)\\}",
                "<strong onclick=\"sendUserMessage('$1')\" " +
                        "style=\"font-weight:bold; color:black; cursor:default; text-decoration:none; transition: all 0.2s ease;\" " +
                        "onmouseover=\"this.style.textDecoration='underline'; this.style.cursor='pointer';\" " +
                        "onmouseout=\"this.style.color='black'; this.style.textDecoration='none'; this.style.cursor='default';\">" +
                        "$1</strong>");
        html = html.replaceAll("(?m)^# (.+)$", "<h4>$1</h4>");
        html = html.replaceAll("(?m)^## (.+)$", "<h5>$1</h5>");
        html = html.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
        html = html.replaceAll("\\*(.+?)\\*", "<em>$1</em>");
        html = html.replaceAll("`(.+?)`", "<code>$1</code>");
        html = html.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "<a href=\"$2\">$1</a>");
        html = html.replaceAll("\n", "<br>");
        return html;
    }

    private Integer getCurrentUserId() {
        return ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
    }

    private String getCurrentCompanyId() {
        return SecurityContext.getInstance().getCompanyId();
    }

    private String[] getFromLocalData(String txt) {
        String[] responseAi = new String[2];
        responseAi[0] = txt;

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9090/api/report", String.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

            try {

                ReportRpc report = new Gson().fromJson(response.getBody(), ReportRpc.class);

                if (report != null) {
                    UUID uuid = UUID.randomUUID();
                    RedisClient.setKey(uuid.toString(), report, ReportRpc.class, 1800);
                    responseAi[1] = uuid.toString();
                }
            } catch (JsonSyntaxException | IllegalStateException e) {
                // log error if needed, but do not crash
                responseAi[1] = null; // explicit handling
            }
        }
        return responseAi;
    }

    public Map<String, String> getAIVideoLinkMap() {
        String links = EdsContextParams.getAIVideoLinks();
        Map<String, String> videoMap = new HashMap<>();

        if (links != null && !links.trim().isEmpty()) {
            String[] linkArray = links.split(",");
            if (linkArray.length >= 2) {
                videoMap.put("reportVideo", linkArray[0].trim());
                videoMap.put("wikiVideo", linkArray[1].trim());
            } else if (linkArray.length == 1) {
                videoMap.put("reportVideo", linkArray[0].trim());
            }
        }

        return videoMap;
    }
}

