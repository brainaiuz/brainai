package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.Date;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidityPeriodItem extends SelectItem {

    //---------------- period type ---------------//
    public static final String _VALIDITY_PERIOD_TYPE = "_VALIDITY_PERIOD_TYPE";
    public static final String VALIDITY_PERIOD_APPRAISAL = "VALIDITY_PERIOD_APPRAISAL";
    public static final String VALIDITY_PERIOD_GOAL = "VALIDITY_PERIOD_GOAL";
    public static final String VALIDITY_PERIOD_BONUS = "VALIDITY_PERIOD_BONUS";

    //
    public static final String ACTION = "action";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PERIOD = "period";
    public static final String PERIOD_TYPE = "periodType";

    private Date fromDate;
    private Date toDate;
    private String period;
    private HashSet<SelectItem> periodTypeItems = new HashSet<>();
    private HashSet<String> periodTypeCodeItems = new HashSet<>();
    private boolean isDefault = false;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public HashSet<String> getPeriodTypeCodeItems() {
        return periodTypeCodeItems;
    }

    public void setPeriodTypeCodeItems(HashSet<String> periodTypeCodeItems) {
        this.periodTypeCodeItems = periodTypeCodeItems;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public HashSet<SelectItem> getPeriodTypeItems() {
        return periodTypeItems;
    }

    public void setPeriodTypeItems(HashSet<SelectItem> periodTypeItems) {
        this.periodTypeItems = periodTypeItems;
    }
}
