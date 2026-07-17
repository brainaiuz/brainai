package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class LRSettingsItem implements IsSerializable {
    private boolean copyPreviousYearAllowances;
    private BigDecimal prevYearAllowanceCopyPercent;
    private Long usageDeadline;
    private boolean payremainingallowance;

    public boolean getCopyPreviousYearAllowances() {
        return copyPreviousYearAllowances;
    }

    public void setCopyPreviousYearAllowances(boolean copyPreviousYearAllowances) {
        this.copyPreviousYearAllowances = copyPreviousYearAllowances;
    }

    public BigDecimal getPrevYearAllowanceCopyPercent() {
        return prevYearAllowanceCopyPercent;
    }

    public void setPrevYearAllowanceCopyPercent(BigDecimal prevYearAllowanceCopyPercent) {
        this.prevYearAllowanceCopyPercent = prevYearAllowanceCopyPercent;
    }

    public Long getUsageDeadline() {
        return usageDeadline;
    }

    public void setUsageDeadline(Long usageDeadline) {
        this.usageDeadline = usageDeadline;
    }

    public boolean getPayremainingallowance() {
        return payremainingallowance;
    }

    public void setPayremainingallowance(boolean payremainingallowance) {
        this.payremainingallowance = payremainingallowance;
    }
}
