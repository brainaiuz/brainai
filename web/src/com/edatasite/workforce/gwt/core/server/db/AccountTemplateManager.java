package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsAccountTemplate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23.02.2009
 * Time: 13:24:32
 * To change this template use File | Settings | File Templates.
 */
public interface AccountTemplateManager extends Manager<EdsAccountTemplate> {

    List<EdsAccountTemplate> getAccountsFromTemplate();

    EdsAccountTemplate getAccountByCode(int code);

    void deleteAlAccountTemplates();
}
