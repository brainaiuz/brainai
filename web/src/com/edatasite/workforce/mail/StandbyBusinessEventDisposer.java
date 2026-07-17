package com.edatasite.workforce.mail;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventDisposer;
import com.edatasite.workforce.scheduler.BaseRecurrenceJob;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * User: Abdulaziz
 * Date: Jan 16, 2010
 * Time: 5:26:01 PM
 */
public class StandbyBusinessEventDisposer extends BaseRecurrenceJob {

    private static Logger log = LoggerFactory.getLogger(StandbyBusinessEventDisposer.class);
    @Autowired private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired private BusinessEventDispatcherManager businessEventDispatcherManager;
    @Autowired private BusinessEventDisposer businessEventDisposer;
    @Autowired private MessageManager messageManager;
    @Autowired private CompanyManager companyManager;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);

        List<Integer> companyIds = companyManager.getReallyExistingCompanyIds();
        if (CollectionUtils.isEmpty(companyIds)) {
            return;
        }

        int threadCount = Math.max(
                Runtime.getRuntime().availableProcessors(),
                Constants.DEFAULT_THREAD_COUNT_FOR_MULTI_PROCESS
        );

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        List<Future<?>> futures = new ArrayList<>();

        for (Integer companyId : companyIds) {
            futures.add(pool.submit(() -> processCompany(companyId)));
        }

        for (Future<?> future : futures) {
            try {
                future.get(); // yoki timeout bilan: future.get(5, TimeUnit.MINUTES)
            } catch (Exception e) {
                log.error("Company processing task failed: {}", e.getMessage(), e);
            }
        }

        pool.shutdown();
    }

    private void processCompany(Integer companyId) {
        ServerSecurityContext.getInstance().setDatabase(getDataBaseType());
        ServerSecurityContext.getInstance().setCompanyId(companyId);

        try {
            List<EdsBusinessEvent> queuedEvents = businessEventDispatcherManager.getUnprocessedEvents();

            if (CollectionUtils.isEmpty(queuedEvents)) {
                return;
            }

            log.debug("Processing {} events for companyId={}", queuedEvents.size(), companyId);

            for (EdsBusinessEvent event : queuedEvents) {
                processEvent(event, companyId);
            }
        } finally {
            ServerSecurityContext.getInstance().clear(); // agar mavjud bo'lsa
        }
    }

    private void processEvent(EdsBusinessEvent event, Integer companyId) {
        try {
            baseEventsPostProcessor.dispatchEvent(event, "StandbyBusinessEventDisposer");
        } catch (Exception e) {
            log.error("Event dispatch failed [companyId={}, eventId={}]: {}", companyId, event.getObjectID(), e.getMessage(), e);
        } finally {
            try {
                businessEventDisposer.disposeEventNative(event);
            } catch (Exception e) {
                log.error("Event dispose failed [companyId={}, eventId={}]: {}", companyId, event.getObjectID(), e.getMessage(), e);
            }
        }
    }
}
