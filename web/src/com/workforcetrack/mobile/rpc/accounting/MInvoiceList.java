package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/13/11
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "invoiceList")
public class MInvoiceList {

    private List<MInvoiceListItem> invoiceListItem;
    private Integer totalCount;

    public MInvoiceList() {

    }

    public MInvoiceList(InvoiceList invoiceList) {
        if (invoiceList != null) {
            this.invoiceListItem = new ArrayList<>();
            for (NewInvoice invoiceItem : invoiceList.getList()) {
                this.invoiceListItem.add(new MInvoiceListItem(invoiceItem));
            }
            this.totalCount = invoiceList.getTotal();
        }
    }

    public MInvoiceList(ListResult<NewInvoice> invoiceList) {
        if (invoiceList != null) {
            this.invoiceListItem = new ArrayList<>();
            for (NewInvoice invoiceItem : invoiceList.getList()) {
                this.invoiceListItem.add(new MInvoiceListItem(invoiceItem));
            }
            this.totalCount = invoiceList.getTotal();
        }
    }

    public List<MInvoiceListItem> getInvoiceListItem() {
        return invoiceListItem;
    }

    public void setInvoiceListItem(List<MInvoiceListItem> invoiceListItem) {
        this.invoiceListItem = invoiceListItem;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}