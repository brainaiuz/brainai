package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsConversionBalance;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 02.06.2009
 * Time: 14:34:02
 * To change this template use File | Settings | File Templates.
 */
public interface ConversionBalanceManager extends Manager<EdsConversionBalance> {

    EdsConversionBalance findCompanyConversionBalance();

}
