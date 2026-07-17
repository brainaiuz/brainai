package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBankTransferCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BankTransferCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Omonullo on 3/1/2017.
 */
@Repository("bankTransferCFManager")
public class BankTransferCFManagerImpl extends BaseManager<EdsBankTransferCustomFields> implements BankTransferCFManager {
    public BankTransferCFManagerImpl() {
        super(EdsBankTransferCustomFields.class);
    }



}
