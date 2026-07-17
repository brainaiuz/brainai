package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankMoneyTransfer;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankMoneyTransferManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/23/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankMoneyTransferManager")
public class BankMoneyTransferManagerImpl extends BaseManager<EdsBankMoneyTransfer> implements BankMoneyTransferManager {
    public BankMoneyTransferManagerImpl() {
        super(EdsBankMoneyTransfer.class);
    }
}
