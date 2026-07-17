package com.edatasite.workforce.gwt.availability.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FingerprintTimeDto implements IsSerializable {
    private Integer intime;
    private Integer outtime;
    private Integer actualtime;
    private Integer precalculatedtime;
    private Boolean isInAdjustment;
    private Boolean isOutAdjustment;
    private String inDescription;
    private String outDescription;
    private String inSource;
    private String outSource;

    public FingerprintTimeDto() {
    }

    public FingerprintTimeDto(Integer intime, Integer outtime, Integer actualtime, Integer precalculatedtime, Boolean isInAdjustment, Boolean isOutAdjustment, String inDescription, String outDescription, String inSource, String outSource) {
        this.intime = intime;
        this.outtime = outtime;
        this.actualtime = actualtime;
        this.precalculatedtime = precalculatedtime;
        this.isInAdjustment = isInAdjustment;
        this.isOutAdjustment = isOutAdjustment;
        this.inDescription = inDescription;
        this.outDescription = outDescription;
        this.inSource = inSource;
        this.outSource = outSource;
    }

    public Integer getIntime() {
        return intime;
    }

    public void setIntime(Integer intime) {
        this.intime = intime;
    }

    public Integer getOuttime() {
        return outtime;
    }

    public void setOuttime(Integer outtime) {
        this.outtime = outtime;
    }

    public Integer getActualtime() {
        return actualtime;
    }

    public void setActualtime(Integer actualtime) {
        this.actualtime = actualtime;
    }

    public Integer getPrecalculatedtime() {
        return precalculatedtime;
    }

    public void setPrecalculatedtime(Integer precalculatedtime) {
        this.precalculatedtime = precalculatedtime;
    }

    public Boolean getInAdjustment() {
        return isInAdjustment;
    }

    public void setInAdjustment(Boolean inAdjustment) {
        isInAdjustment = inAdjustment;
    }

    public Boolean getOutAdjustment() {
        return isOutAdjustment;
    }

    public void setOutAdjustment(Boolean outAdjustment) {
        isOutAdjustment = outAdjustment;
    }

    public String getInDescription() {
        return inDescription;
    }

    public void setInDescription(String inDescription) {
        this.inDescription = inDescription;
    }

    public String getOutDescription() {
        return outDescription;
    }



    public String getInSource() {
        return inSource;
    }

    public void setInSource(String inSource) {
        this.inSource = inSource;
    }

    public String getOutSource() {
        return outSource;
    }

    public void setOutSource(String outSource) {
        this.outSource = outSource;
    }
}
