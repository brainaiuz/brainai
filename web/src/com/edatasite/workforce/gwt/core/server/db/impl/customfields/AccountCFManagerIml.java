package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsAccountCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.AccountCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User : Bekhruz on 11/12/2021
 */
@Repository("accountCFManager")
public class AccountCFManagerIml extends BaseManager<EdsAccountCustomFields> implements AccountCFManager {

    public AccountCFManagerIml() {
        super(EdsAccountCustomFields.class);
    }
}
