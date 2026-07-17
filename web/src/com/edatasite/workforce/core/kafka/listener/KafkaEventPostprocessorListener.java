package com.edatasite.workforce.core.kafka.listener;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.kafka.data.EventData;
import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.core.kafka.util.KafkaConstants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventDisposer;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class KafkaEventPostprocessorListener {

    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private BusinessEventDisposer businessEventDisposer;
    @Autowired
    private BusinessEventDispatcherManager businessEventDispatcherManager;
    @Autowired
    private CompanyManager companyManager;

    protected static final Logger log = LoggerFactory.getLogger(KafkaEventPostprocessorListener.class);

    @KafkaListener(topics = {
            KafkaConstants.Topic.genericEventPostProcessorTopic,
            KafkaConstants.Topic.taskEventPostProcessorTopic,
            KafkaConstants.Topic.workflowEventPostProcessorTopic,
            KafkaConstants.Topic.importFileEventPostProcessorTopic,
            KafkaConstants.Topic.customTransactionEventPostProcessorTopic,
            KafkaConstants.Topic.salesInvoiceEventPostProcessorTopic,
            KafkaConstants.Topic.salesQuoteEventPostProcessorTopic,
            KafkaConstants.Topic.salesOrderEventPostProcessorTopic,
            KafkaConstants.Topic.taskDocumentsPostProcessorTopic
    }, groupId = KafkaConstants.Group.defaultGroup,
            concurrency = "50",
            containerFactory = "kafkaEventListenerContainerFactory")
    public void recieveMessage(String message) {
        KafkaData<EventData> data = convert(message);
        SecurityContext.getInstance().setDatabase(data.getClusterType());
        log.info("            *****************************          {} -- >KAFKA MESSAGE LISTENER -> Event ID : {}, COMPANY_ID : {}  ", data.getDataMQ().getType(), data.getDataMQ().getId(), data.getCompanyId());
        if (companyManager.schemaExists(data.getCompanyId())) {
            SecurityContext.getInstance().setCompanyId(data.getCompanyId());
            if (data.getSessionID() != null) {
                SecurityContext.getInstance().setSessionId(data.getSessionID());
            }
            receive(data.getDataMQ());
        }
    }

    public void receive(EventData eventData) {
        EdsBusinessEvent event = businessEventDispatcherManager.getEvent(eventData.getId());
        if (event != null) {
            try {
                baseEventsPostProcessor.dispatchEvent(event,"KafkaEventPostprocessorListener");
            } catch (Exception e) {
                log.error("Caught exception during thread event dispatching invocation:" + e.getMessage(), e);
            } finally {
                businessEventDisposer.disposeEventNative(event);
            }
        }
    }

    public KafkaData<EventData> convert(String message) {
        return new Gson().fromJson(message, new TypeToken<KafkaData<EventData>>() {
        }.getType());
    }
}
