package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsAccountType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.04.2008
 * Time: 19:23:55
 * To change this template use File | Settings | File Templates.
 */
public interface AccountTypeManager extends Manager<EdsAccountType> {

    List<EdsAccountType> getAccounTypeList();
}
