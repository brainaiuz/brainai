package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * User: Satimov Murad
 * Date: 2/5/18 3:20 PM
 */
public class PayrollTotalTO extends PayrollAmountsTO implements IsSerializable {
    private BigDecimal totalAmount;
    private BigDecimal totalApprovedAmount;

    public PayrollTotalTO() {
        this(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public PayrollTotalTO(BigDecimal totalAmount, BigDecimal totalApprovedAmount) {
        this.totalAmount = totalAmount;
        this.totalApprovedAmount = totalApprovedAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalApprovedAmount() {
        return totalApprovedAmount;
    }

    public void setTotalApprovedAmount(BigDecimal totalApprovedAmount) {
        this.totalApprovedAmount = totalApprovedAmount;
    }
}
