package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsAccountTemplate;
import com.edatasite.workforce.gwt.core.server.db.AccountTemplateManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 23.02.2009
 * Time: 13:24:45
 * To change this template use File | Settings | File Templates.
 */
@Repository("accountTemplateManager")
public class AccountTemplateManagerImpl extends BaseManager<EdsAccountTemplate> implements AccountTemplateManager {

    public AccountTemplateManagerImpl() {
        super(EdsAccountTemplate.class);
    }

    public List<EdsAccountTemplate> getAccountsFromTemplate() {
        return find("from EdsAccountTemplate ac");
    }

    public EdsAccountTemplate getAccountByCode(int code) {
        return (EdsAccountTemplate) findSingle("from EdsAccountTemplate at where at.code=?", code);
    }

    public void deleteAlAccountTemplates() {
        update("delete from EdsAccountTemplate");
    }
}
