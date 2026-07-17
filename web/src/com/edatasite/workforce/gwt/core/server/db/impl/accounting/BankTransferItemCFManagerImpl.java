package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsBankTransferItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankTransferItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("bankTransferItemCFManager")
public class BankTransferItemCFManagerImpl extends BaseManager<EdsBankTransferItemCustomFields> implements BankTransferItemCFManager {

    public BankTransferItemCFManagerImpl() {
        super(EdsBankTransferItemCustomFields.class);
    }
}