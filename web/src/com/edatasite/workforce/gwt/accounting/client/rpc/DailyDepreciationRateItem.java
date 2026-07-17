package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Normurod on 7/9/15.
 */
public class DailyDepreciationRateItem implements IsSerializable, Serializable {

    private DateNonConvertable financialYearStart;
    private DateNonConvertable financialYearEnd;
    private BigDecimal dailyDepreciation;

    public DateNonConvertable getFinancialYearStart() {
        return financialYearStart;
    }

    public void setFinancialYearStart(DateNonConvertable financialYearStart) {
        this.financialYearStart = financialYearStart;
    }

    public DateNonConvertable getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(DateNonConvertable financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public BigDecimal getDailyDepreciation() {
        return dailyDepreciation;
    }

    public void setDailyDepreciation(BigDecimal dailyDepreciation) {
        this.dailyDepreciation = dailyDepreciation;
    }
}
