package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;
import java.util.Map;

public interface AiReportService extends RemoteService {

    String [] getAiResponse(String str, boolean reportGenerationEnabled, Long chatId);

    Long getOrCreateChatId(Integer userId, String companyId);

    List<Map<String, String>> getChatHistory(Long chatId, Integer limit);

    Map<String, String> getAIVideoLinkMap();

    class App {
        public static AiReportServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/aireport");
            return (AiReportServiceAsync) target;
        }
    }
}
