package com.edatasite.workforce.gwt.ai;

import com.edatasite.workforce.gwt.core.client.rpc.PositionAiResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class AiResponseParser {

    private final Gson gson;

    public AiResponseParser() {
        this.gson = new Gson();
    }

    public PositionAiResponse parse(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return new PositionAiResponse();
        }

        // Clean up Markdown code blocks if the AI adds them (e.g., ```json ... ```)
        String cleanJson = jsonString
                .replaceAll("^```json", "")
                .replaceAll("^```", "")
                .replaceAll("```$", "")
                .trim();

        try {
            return gson.fromJson(cleanJson, PositionAiResponse.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Failed to parse JSON: " + cleanJson);
            return new PositionAiResponse();
        }
    }
}