package com.edatasite.workforce.scheduler;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.kafka.producer.KafkaEventProducer;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFODataMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.FailTarget;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.FifoFailureService;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static RabbitMQService rabbitMQService = (RabbitMQService) ApplicationContextProvider.applicationContext.getBean("rabbitMQService");
    private static FifoFailureService fifoFailureService = (FifoFailureService) ApplicationContextProvider.applicationContext.getBean("fifoFailureService");
    protected static EmailSettingsManager emailSettingsManager = (EmailSettingsManager) ApplicationContextProvider.applicationContext.getBean("emailSettingsManager");
    protected static CompanyManager companyManager = (CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager");
    private static GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = (GlobalAuthJdbcSpringManager) ApplicationContextProvider.applicationContext.getBean("globalAuthJdbcSpringManager");
    private static KafkaEventProducer kafkaEventProducer = (KafkaEventProducer) ApplicationContextProvider.applicationContext.getBean("kafkaEventProducer");

    @Inject
    private Environment env;

    @Scheduled(fixedRate = 60000)
    public void messageCenter() {
        Map<String, Set<Integer>> clusterCompanies = globalAuthJdbcSpringManager.getClusterCompanies();

        for (Map.Entry<String, Set<Integer>> entry : clusterCompanies.entrySet()) {
            ServerSecurityContext.getInstance().setDatabase(entry.getKey());
            List<Integer> companyIds = companyManager.getReallyExistingCompanyIds();
            for (Integer companyID : companyIds) {
                try {
                    EdsCase.clearContents(companyID);
                    ServerSecurityContext.getInstance().setCompanyId(companyID);

                    List<EdsEmailSetting> emailSettings = emailSettingsManager.getAllActiveEmailSettings();
                    if (emailSettings != null && emailSettings.size() > 0) {
                        for (EdsEmailSetting emailSetting : emailSettings) {
                            rabbitMQService.emailFetchMQ(emailSetting.getObjectID());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Scheduled(fixedDelay = 300_000) // Every 5 minutes
    public void retryFailedFifoItems() {

        try {
            List<String> clusterList = new Gson().fromJson(env.getProperty("tenant.datasource.clusters", ""), List.class);
            for (String cluster : clusterList) {
                ServerSecurityContext.getInstance().setDatabase(cluster);
                log.info("Retrying SCHEDULED FIFO items");
                List<FIFODataMQ> failedItems = fifoFailureService.getPendingFailures();
                log.info("Found {} SCHEDULED FIFO items", failedItems != null ? failedItems.size() : 0);
                for (FIFODataMQ data : failedItems) {
                    ServerSecurityContext.getInstance().setCompanyId(data.getCompanyId());
                    log.info("Retrying FIFO items for company: {}", data.getCompanyId());
                    fifoFailureService.updateFifoFailure(data.getObjectId());
                    sendEvent(data, UUID.randomUUID().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendEvent(FIFODataMQ fifoDataMQ, String key) {
        fifoDataMQ.setTarget(FailTarget.SENDING);

        try {
            kafkaEventProducer.sendMessage(fifoDataMQ, key)
                    .addCallback(
                            success -> log.info("MESSAGE SENT SUCCESSFULLY"),
                            failure -> {
                                log.error("====================== FAILED TO SEND THE EVENT ======================", failure);
                                handleFailure(fifoDataMQ, key, failure.getMessage());
                            });
        } catch (Exception e) {
            log.error("====================== EXCEPTION OCCURRED WHILE SENDING THE MESSAGE ======================", e);
        }
    }

    private void handleFailure(FIFODataMQ fifoDataMQ, String key, String failMessage) {
        fifoFailureService.trackFailur(fifoDataMQ, key, failMessage);
    }
}
