package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class SerialItem implements IsSerializable, Serializable {
    Integer id;
    String number;
    Boolean used;
    ArrayList<SerialDetailItem> items = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getUsed() {
        return used != null ? used : false;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public ArrayList<SerialDetailItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<SerialDetailItem> items) {
        this.items = items;
    }
}
