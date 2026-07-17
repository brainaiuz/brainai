package com.edatasite.workforce.gwt.core.server.db.impl.locking;

import com.edatasite.workforce.core.domain.locking.EdsTransactionLocking;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.locking.TransactionLockingManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionLockingManagerimpl extends BaseManager<EdsTransactionLocking> implements TransactionLockingManager {
    public TransactionLockingManagerimpl() {
        super(TransactionLockingManagerimpl.class);
    }

    @Override
    public EdsTransactionLocking getLock() {
        List<EdsTransactionLocking> list = find("from EdsTransactionLocking tl");
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }
}
