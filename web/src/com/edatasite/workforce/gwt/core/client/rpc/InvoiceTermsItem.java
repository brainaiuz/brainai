package com.edatasite.workforce.gwt.core.client.rpc;


/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceTermsItem extends SelectItem {
    private Integer days;

    public InvoiceTermsItem() {
    }

    public InvoiceTermsItem(Integer id, String name, Integer days) {
        super(id, name);
        this.days = days;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
