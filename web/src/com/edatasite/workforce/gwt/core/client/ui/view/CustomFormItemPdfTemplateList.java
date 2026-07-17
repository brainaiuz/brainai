package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Azam Ahmadjonov
 * Date: 10/14/19
 * Time: 10:58 PM
 */
public class CustomFormItemPdfTemplateList implements IsSerializable {

    private SelectItem[] items;
    private Integer defaultTemplateID;

    public CustomFormItemPdfTemplateList() {

    }

    public CustomFormItemPdfTemplateList(SelectItem[] items, Integer defaultTemplateID) {
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