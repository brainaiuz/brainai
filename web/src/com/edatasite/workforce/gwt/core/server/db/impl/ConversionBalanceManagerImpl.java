package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsConversionBalance;
import com.edatasite.workforce.gwt.core.server.db.ConversionBalanceManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 02.06.2009
 * Time: 14:33:50
 * To change this template use File | Settings | File Templates.
 */
@Repository("conversionBalanceManager")
public class ConversionBalanceManagerImpl extends BaseManager<EdsConversionBalance> implements ConversionBalanceManager {

    public ConversionBalanceManagerImpl() {
        super(EdsConversionBalance.class);
    }

    public EdsConversionBalance findCompanyConversionBalance() {
        return (EdsConversionBalance) findSingle("select cb from EdsConversionBalance cb");
    }
}
