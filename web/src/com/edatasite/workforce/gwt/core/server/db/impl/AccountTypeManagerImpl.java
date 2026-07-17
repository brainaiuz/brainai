package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.gwt.core.server.db.AccountTypeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.04.2008
 * Time: 19:25:53
 * To change this template use File | Settings | File Templates.
 */
@Repository("accountTypeManager")
public class AccountTypeManagerImpl extends BaseManager<EdsAccountType> implements AccountTypeManager {

    public AccountTypeManagerImpl() {
        super(EdsAccountType.class);
    }

    @Override
    public List<EdsAccountType> getAccounTypeList() {
        return find("SELECT at FROM EdsAccountType at ORDER BY at.order ");
    }
}
