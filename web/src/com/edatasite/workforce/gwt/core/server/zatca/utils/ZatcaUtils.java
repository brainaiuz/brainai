package com.edatasite.workforce.gwt.core.server.zatca.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ZatcaUtils {
    public final static int DEFAULT_SCALE_NUMBER = 2; /// Discount/ allowance percentage values  decimal point 6
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String TIME_FOMRAT = "HH:mm:ss";

    public final static String TAX_INVOICE = "TAX_INVOICE";
    public final static String DEBIT_NOTE = "DEBIT_NOTE";
    public final static String CREDIT_NOTE = "CREDIT_NOTE";

    public static BigDecimal getFormattedValue(BigDecimal val, int scale) {
        if (val == null) {
            val = BigDecimal.ZERO;
        }
        return val.setScale(scale, RoundingMode.HALF_UP);
    }

    public static String getTaxRateCodeByPercent(BigDecimal percent) {
        if (BigDecimal.ZERO.equals(percent)) {
            return "Z";
        }
        return "S";
    }

    public static BigDecimal getTaxRateValue(double taxRate) {
        if (getFormattedValue(BigDecimal.ZERO, DEFAULT_SCALE_NUMBER)
                .equals(getFormattedValue(BigDecimal.valueOf(taxRate), DEFAULT_SCALE_NUMBER))) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(taxRate).setScale(0, RoundingMode.HALF_UP);
        }
    }
}
