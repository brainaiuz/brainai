package com.edatasite.workforce.gwt.accounting.server.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("nanoBananaClient")
public class NanoBananaClient {

    private static final Logger log = LoggerFactory.getLogger(NanoBananaClient.class);

    private static final String BASE_URL = "https://nano-banana.kpi.com";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String ENHANCE_PATH = "/api/gemini/enhance";
    private static final String GEMINI_CONTENT_PATH = "/api/gemini/content";
    private static final String ROTATE_RIGHT_PATH = "/api/rotate/right";
    private static final String ROTATE_LEFT_PATH = "/api/rotate/left";

    private static final String login = "admin";
    private static final String password = "string";

    private final RestTemplate restTemplate = new RestTemplate();
    private final Gson gson = new Gson();

    private volatile String cachedToken;

    @PostConstruct
    private void init() {
        if (!login.isEmpty() && !password.isEmpty()) {
            try {
                cachedToken = login();
            } catch (Exception e) {
                log.warn("Nano Banana: initial login failed — " + e.getMessage());
            }
        }
    }

    public boolean isAvailable() {
        try {
            String token = getToken();
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enhance image quality using Gemini AI.
     * Returns enhanced image bytes, or null if the service is unavailable.
     */
    public byte[] enhanceImage(byte[] imageBytes, String filename) {
        return postImageFile(ENHANCE_PATH, imageBytes, filename);
    }

    /**
     * Process image with text prompt via Gemini chat.
     * Returns list of generated image base64 data strings.
     */
    public List<String> processWithPrompt(byte[] imageBytes, String filename, String prompt) {
        return doProcessWithPrompt(imageBytes, filename, prompt, false);
    }

    private List<String> doProcessWithPrompt(byte[] imageBytes, String filename, String prompt, boolean isRetry) {
        String token = getToken();
        if (token == null) return null;

        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        String mimeType = resolveMimeType(filename);

        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mimeType", mimeType);
        inlineData.put("data", base64Image);

        Map<String, Object> body = new HashMap<>();
        body.put("content", prompt);
        body.put("inlineData", inlineData);

        HttpHeaders headers = bearerHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BASE_URL + GEMINI_CONTENT_PATH,
                    new HttpEntity<>(gson.toJson(body), headers),
                    String.class
            );
            return extractImages(response.getBody());
        } catch (RestClientException e) {
            if (!isRetry && e.getMessage() != null && e.getMessage().contains("401")) {
                cachedToken = null;
                cachedToken = login();
                return doProcessWithPrompt(imageBytes, filename, prompt, true);
            }
            log.warn("Nano Banana processWithPrompt failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Rotate image clockwise by 90°.
     */
    public byte[] rotateRight(byte[] imageBytes, String filename) {
        return postImageFile(ROTATE_RIGHT_PATH, imageBytes, filename);
    }

    /**
     * Rotate image counterclockwise by 90°.
     */
    public byte[] rotateLeft(byte[] imageBytes, String filename) {
        return postImageFile(ROTATE_LEFT_PATH, imageBytes, filename);
    }

    // ── private helpers ─────────────────────────────────────────────────────

    private String login() {
        log.info("Nano Banana: login started");
        Map<String, String> credentials = new HashMap<>();
        credentials.put("phone", login);
        credentials.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BASE_URL + LOGIN_PATH,
                    new HttpEntity<>(gson.toJson(credentials), headers),
                    String.class
            );
            return extractToken(response.getBody());
        } catch (Exception e) {
            log.error("Nano Banana: login failed: {}", e.getMessage());
        }
        return null;
    }

    private String getToken() {
        log.info("---------token---method: {}", cachedToken);
        if (cachedToken == null) {
            cachedToken = login();
        }
        return cachedToken;
    }

    private byte[] postImageFile(String path, byte[] imageBytes, String filename) {
        String token = getToken();
        if (token == null) return null;

        HttpHeaders headers = bearerHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        ByteArrayResource fileResource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BASE_URL + path,
                    new HttpEntity<>(body, headers),
                    String.class
            );
            return extractBytes(response.getBody());
        } catch (RestClientException e) {
            log.warn("Nano Banana " + path + " failed: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("401")) {
                cachedToken = null;
            }
            return null;
        }
    }

    private HttpHeaders bearerHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());
        return headers;
    }

    private String extractToken(String responseBody) {
        try {
            JsonObject root = new JsonParser().parse(responseBody).getAsJsonObject();
            if (root.has("payload") && root.get("payload").isJsonPrimitive()) {
                return root.get("payload").getAsString();
            }
        } catch (Exception e) {
            log.warn("Nano Banana: failed to extract token — " + e.getMessage());
        }
        return null;
    }

    private byte[] extractBytes(String responseBody) {
        try {
            JsonObject root = new JsonParser().parse(responseBody).getAsJsonObject();
            if (root.has("payload") && !root.get("payload").isJsonNull()) {
                String base64 = root.get("payload").getAsString();
                return Base64.getDecoder().decode(base64);
            }
        } catch (Exception e) {
            log.warn("Nano Banana: failed to extract bytes — " + e.getMessage());
        }
        return null;
    }

    private List<String> extractImages(String responseBody) {
        List<String> result = new ArrayList<>();
        try {
            JsonObject root = new JsonParser().parse(responseBody).getAsJsonObject();
            JsonObject payload = root.getAsJsonObject("payload");
            if (payload != null && payload.has("images")) {
                payload.getAsJsonArray("images").forEach(el -> {
                    JsonObject img = el.getAsJsonObject();
                    String mimeType = img.has("mimeType") ? img.get("mimeType").getAsString() : "image/jpeg";
                    String data = img.has("data") ? img.get("data").getAsString() : null;
                    if (data != null) {
                        result.add("data:" + mimeType + ";base64," + data);
                    }
                });
            }
        } catch (Exception e) {
            log.warn("Nano Banana: failed to extract images — " + e.getMessage());
        }
        return result;
    }

    private String resolveMimeType(String filename) {
        if (filename == null) return "image/jpeg";
        String lower = filename.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        return "image/jpeg";
    }
}
