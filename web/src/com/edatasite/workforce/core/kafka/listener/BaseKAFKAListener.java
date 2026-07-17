package com.edatasite.workforce.core.kafka.listener;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.kafka.data.KafkaData;
import com.edatasite.workforce.core.kafka.producer.SolrEventProducer;
import com.edatasite.workforce.gwt.accounting.server.app.EventHandlerService;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.fifo.ProcessedEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public abstract class BaseKAFKAListener<T> {

    protected static final Logger log = LoggerFactory.getLogger(BaseKAFKAListener.class);

    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected CompanyManager companyManager;
    @Autowired
    protected ProcessedEventManager processedEventRepository;
    @Autowired
    protected EventHandlerService handlerService;
    @Autowired
    protected SolrEventProducer solrEventProducer;
    @Autowired
    protected XSync<String> sync;

    protected abstract void receiveMessage(@Payload String data, @Header("eventID") String messageID, @Header(KafkaHeaders.RECEIVED_KEY) String key);

    protected abstract KafkaData<T> convertMessage(String message);

}