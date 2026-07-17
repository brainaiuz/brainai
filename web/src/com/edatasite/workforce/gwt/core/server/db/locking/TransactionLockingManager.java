package com.edatasite.workforce.gwt.core.server.db.locking;

import com.edatasite.workforce.core.domain.locking.EdsTransactionLocking;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface TransactionLockingManager extends Manager<EdsTransactionLocking> {

    EdsTransactionLocking getLock();
}
