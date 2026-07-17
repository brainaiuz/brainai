package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/17/15
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollPdfTemplateList implements IsSerializable {

    private SelectItem[] items;
    private Integer defaultTemplateID;

    public PayrollPdfTemplateList() {

    }

    public PayrollPdfTemplateList(SelectItem[] items, Integer defaultTemplateID) {
        this.items = items;
        this.defaultTemplateID = defaultTemplateID;
    }


    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public Integer getDefaultTemplateID() {
        return defaultTemplateID;
    }

    public void setDefaultTemplateID(Integer defaultTemplateID) {
        this.defaultTemplateID = defaultTemplateID;
    }
}
