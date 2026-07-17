package com.edatasite.workforce.gwt.core.client.rpc;

/**
 * User: Ilhombek
 * Date: 7/14/12
 * Time: 11:53 AM
 */
public class CalendarItemRpc extends SelectItem {

    private DateNonConvertable nonConvertable;
    private DateNonConvertable endDate;

    public CalendarItemRpc() {
    }

    public CalendarItemRpc(DateNonConvertable nonConvertable) {
        this.nonConvertable = nonConvertable;
    }

    public DateNonConvertable getNonConvertable() {
        return nonConvertable;
    }

    public void setNonConvertable(DateNonConvertable nonConvertable) {
        this.nonConvertable = nonConvertable;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

}
