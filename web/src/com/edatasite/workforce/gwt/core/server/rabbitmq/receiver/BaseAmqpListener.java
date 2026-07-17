package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class BaseAmqpListener<T> {

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ExecutorService executor;

    public void receive(String message) {
        System.out.println("Rabbit Message Came! ");
        DataMQ<T> data = convertMessage(message);

        if (data.getClusterType() == null || data.getClusterType().isEmpty() || data.getCompanyId() == null) {
            System.out.println("Incorrect data ");
            return;
        }

        String currentDatabase = data.getClusterType();
        String currentCompanyId = String.valueOf(data.getCompanyId());

        Future<?> future = executor.submit(() -> {
            try {
                TenantContextHolder.setCustomerType(currentDatabase);
                if (companyManager.schemaExists(currentCompanyId)) {
                    System.out.println("DB---:--- " + currentDatabase + "; schemaExists ---:--- : " + currentCompanyId);
                    ServerSecurityContext.getInstance().setCompanyId(currentCompanyId);
                    receiveMessage(data.getDataMQ());
                } else {
                    System.out.println("DB---:--- " + currentDatabase + "; schema NOT Exists ---:--- : " + currentCompanyId);
                }
            } finally {
                TenantContextHolder.clearCustomerType();
                ServerSecurityContext.setServerSecurityContext(null);
            }
        });

        try {
            future.get(); // Thread waiting process finished
        } catch (Exception e) {
            System.out.println("Error processing message  : " + message);
        }
        System.out.println("Rabbit Message Done! ");
    }

    protected abstract void receiveMessage(T message);

    protected abstract DataMQ<T> convertMessage(String message);
}
