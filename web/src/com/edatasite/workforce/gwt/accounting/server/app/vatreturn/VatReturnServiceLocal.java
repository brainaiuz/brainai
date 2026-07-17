package com.edatasite.workforce.gwt.accounting.server.app.vatreturn;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;

import java.math.BigDecimal;

public interface VatReturnServiceLocal {

    void createTransactionForVatReturn(EdsVatReturn vatReturn, BigDecimal vatOutputAmount, BigDecimal vatInputAmout, EdsUser user);

    <T extends VatReturnData> T generateVatReturn(Integer vatReturnId);
}
