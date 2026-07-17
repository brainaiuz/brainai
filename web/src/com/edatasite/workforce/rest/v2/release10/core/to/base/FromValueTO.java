package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class FromValueTO extends ResponseData {
    private Integer value;
    private Integer from;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }
}
