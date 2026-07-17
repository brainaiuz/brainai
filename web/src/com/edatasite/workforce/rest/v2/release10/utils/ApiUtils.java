package com.edatasite.workforce.rest.v2.release10.utils;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 12/14/2017.
 */
public class ApiUtils {

    /**
     * If total is null or zero, it doesn't  appear api response
     *
     * @param total
     * @return total amount or null
     */
    public static BigDecimal getTotal(BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return total;
    }
}
