package com.edatasite.workforce.gwt.core.server.db.impl.locking;

import com.edatasite.workforce.core.domain.locking.EdsTransactionLockingReason;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.locking.TransactionLockingReasonManager;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionLockingReasonManagerImpl extends BaseManager<EdsTransactionLockingReason> implements TransactionLockingReasonManager {
    public TransactionLockingReasonManagerImpl() {
        super(TransactionLockingReasonManagerImpl.class);
    }
}
