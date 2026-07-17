package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2011
 * Time: 3:48:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDFTemplatesList implements IsSerializable{
    private int totalCount;
    private PDFTemplatesListItem[] items;

    public PDFTemplatesList() {
    }

    public PDFTemplatesList(int totalCount, PDFTemplatesListItem[] items) {
        this.totalCount = totalCount;
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PDFTemplatesListItem[] getItems() {
        return items;
    }

    public void setItems(PDFTemplatesListItem[] items) {
        this.items = items;
    }

    public ListData getListData() {
        return new ListData(items, totalCount);
    }
}
