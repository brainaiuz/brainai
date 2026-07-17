package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 12.05.2009
 * Time: 20:31:54
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBaseInvoiceItem extends SelectItem {

    private Date lastInvoicedDate;

    public ProjectBaseInvoiceItem() {

    }

    public ProjectBaseInvoiceItem(Integer id, String name) {
        super(id, name);
    }

    public ProjectBaseInvoiceItem(Integer id, String name, Date lastInvoicedDate) {
        super(id, name);
        this.lastInvoicedDate = lastInvoicedDate;
    }

    public Date getLastInvoicedDate() {
        return lastInvoicedDate;
    }

    public void setLastInvoicedDate(Date lastInvoicedDate) {
        this.lastInvoicedDate = lastInvoicedDate;
    }
}
