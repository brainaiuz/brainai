package com.edatasite.workforce.gwt.ai.service;


import com.edatasite.workforce.gwt.ai.AiClient;
import com.edatasite.workforce.gwt.ai.AiResponseParser;
import com.edatasite.workforce.gwt.ai.PromptBuilder;
import com.edatasite.workforce.gwt.core.client.rpc.PositionAiResponse;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;

import java.io.IOException;

public class PositionAiService {

    private final AiClient aiClient;
    private final AiResponseParser parser;

    public PositionAiService(String apiKey) {
        this.aiClient = new AiClient(apiKey);
        this.parser = new AiResponseParser();
    }

    public PositionAiResponse generatePositionData(String pName, String pDesc, String dName, String dDesc) {
        try {

            String locale = ServerUtils.getUserLocale().getLanguage();

            // 1. Build Prompts
            String systemPrompt = PromptBuilder.buildSystemPrompt(locale);
            String userPrompt = PromptBuilder.buildUserPrompt(pName, pDesc, dName, dDesc);

            // 2. Call AI
            String rawJson = aiClient.generate(systemPrompt, userPrompt);

            // 3. Parse and Return
            return parser.parse(rawJson);

        } catch (IOException e) {
            e.printStackTrace();
            return new PositionAiResponse();
        }
    }
}