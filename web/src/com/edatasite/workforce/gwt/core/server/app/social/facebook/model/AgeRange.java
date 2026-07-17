package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
public class AgeRange {

    private Integer min;
    private Integer max;

    private AgeRange(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    /**
     * @return The minimum integer value for the range (possibly null).
     */
    public Integer getMin() {
        return min;
    }

    /**
     * @return The maximum integer value for the range (possibly null).
     */
    public Integer getMax() {
        return max;
    }

}
