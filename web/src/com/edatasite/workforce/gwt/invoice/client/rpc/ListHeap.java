package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

public class ListHeap implements IsSerializable {

    private HashMap<String, String> userInfo;
    private TypeItem[] typeItem;
    private NewInvoiceItem[] invoiceItem;

    public ListHeap() {

    }

    public ListHeap(HashMap<String, String> userInfo, TypeItem[] typeItem) {
        this.userInfo = userInfo;
        this.typeItem = typeItem;
    }

    public ListHeap(HashMap<String, String> userInfo, TypeItem[] typeItem, NewInvoiceItem[] invoiceItem) {
        this.userInfo = userInfo;
        this.typeItem = typeItem;
        this.invoiceItem = invoiceItem;
    }

    public HashMap<String, String> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(HashMap<String, String> userInfo) {
        this.userInfo = userInfo;
    }

    public void setTypeItem(TypeItem[] typeItem) {
        this.typeItem = typeItem;
    }

    public TypeItem[] getTypeItem() {
        return typeItem;
    }

    public void setInvoiceItem(NewInvoiceItem[] invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public NewInvoiceItem[] getInvoiceItem() {
        return invoiceItem;
    }
}
