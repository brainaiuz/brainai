package com.edatasite.workforce.gwt.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AiClient {
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;

    private static final String BASE_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private static final String MODEL_MID="gpt-5-mini";
    private static final String MODEL_HIGH = "gpt-4o";

    private static final Logger log = LoggerFactory.getLogger(AiClient.class);


    public AiClient(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();

        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public String generate(String systemPrompt, String userPrompt) throws IOException {

        // 1. Build the JSON Payload
        JsonObject payload = new JsonObject();

        String model = pickRandomModel();

        payload.addProperty("model", model);
        log.debug("AI model used: {}", model);


        JsonObject responseFormat = new JsonObject();
        responseFormat.addProperty("type", "json_object");
        payload.add("response_format", responseFormat);

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", systemPrompt);
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);
        messages.add(userMsg);

        payload.add("messages", messages);

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, payload.toString());

        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorMsg = response.body() != null ? response.body().string() : "Unknown Error";
                throw new IOException("AI API Request Failed. Code: " + response.code() + " | Error: " + errorMsg);
            }

            if (response.body() == null) {
                throw new IOException("API returned empty body");
            }

            String responseBody = response.body().string();

            // Parse response
            JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);

            // Safety check for API errors in the JSON body
            if (jsonResponse.has("error")) {
                throw new IOException("API Error: " + jsonResponse.get("error").toString());
            }

            // Safety check for choices
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices == null || choices.size() == 0) {
                throw new IOException("API returned no choices/candidates.");
            }

            return choices.get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
        }
    }
    private String pickRandomModel() {
        return ThreadLocalRandom.current().nextBoolean()
                ? MODEL_MID
                : MODEL_HIGH;
    }
}