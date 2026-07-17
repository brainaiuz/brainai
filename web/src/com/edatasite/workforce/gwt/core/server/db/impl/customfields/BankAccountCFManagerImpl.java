package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBankAccountCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsDependentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BankAccountCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod Madrahimov
 * Date: 4/4/16
 * Time: 9:02 PM
 */

@Repository("bankAccountCFManager")
public class BankAccountCFManagerImpl extends BaseManager<EdsBankAccountCustomFields> implements BankAccountCFManager {

    public BankAccountCFManagerImpl() {
        super(EdsDependentCustomFields.class);
    }
}
