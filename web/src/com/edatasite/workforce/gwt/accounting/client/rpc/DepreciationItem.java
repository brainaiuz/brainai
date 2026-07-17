package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/9/11
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DepreciationItem implements IsSerializable{
    private Integer period;
    private Date month;
    private BigDecimal depreciation;

    private boolean isPosted;

    public DepreciationItem() {
    }

    public DepreciationItem(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public BigDecimal getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    public boolean isPosted() {
        return isPosted;
    }

    public void setPosted(boolean posted) {
        isPosted = posted;
    }
}
