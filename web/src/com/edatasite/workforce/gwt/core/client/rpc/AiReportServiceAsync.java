package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface AiReportServiceAsync {

    void getAiResponse(String str, boolean reportGenerationEnabled, Long chatId, AsyncCallback<String[]> async);

    void getOrCreateChatId(Integer userId, String companyId, AsyncCallback<Long> async);

    void getChatHistory(Long chatId, Integer limit, AsyncCallback<List<Map<String, String>>> async);

    void getAIVideoLinkMap(AsyncCallback<Map<String, String>> async);

}
