package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * This is a WFP specific logic implementation for custom transaction management
 * Potentially keeps track whether the transaction commited or rolled back.
 * Consequently, performs some additional operations, to provide database - solr data integrity
 * <p/>
 * <p/>
 * User: Anvarbek
 * Date: Oct 28, 2010
 * Time: 11:28:53 AM
 */
public class WfmJpaTransactionManager extends JpaTransactionManager {

    private static final Logger log = LoggerFactory.getLogger(WfmJpaTransactionManager.class);
    @Autowired
    BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    SolrTransactionManager solrTransactionManager;

    @Override
    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        super.prepareSynchronization(status, definition);

        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new EventSynchronization(baseEventPostProcessor, solrTransactionManager)
            );
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);
        solrTransactionManager.clearList();
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null ||
                ServerSecurityContext.getInstance().getDatabase() == null) {
            log.error("Transaction Rollback, Company ID = {}, Database ID = {}",
                    ServerSecurityContext.getInstance().getCompanyId(),
                    ServerSecurityContext.getInstance().getDatabase());
        }
        solrTransactionManager.rollbackTransaction();
        super.doRollback(status);
    }

    private static class EventSynchronization extends TransactionSynchronizationAdapter {

        private final BaseEventsPostProcessor baseEventPostProcessor;
        private final SolrTransactionManager solrTransactionManager;

        public EventSynchronization(BaseEventsPostProcessor baseEventPostProcessor, SolrTransactionManager solrTransactionManager) {
            this.baseEventPostProcessor = baseEventPostProcessor;
            this.solrTransactionManager = solrTransactionManager;
        }

        @Override
        public void afterCommit() {
            baseEventPostProcessor.handleActivity();
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
                solrTransactionManager.rollbackTransaction();
            }
            solrTransactionManager.clearList();
        }
    }
}