package com.edatasite.workforce.gwt.contact.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by dilshod on 12/5/2015.
 */
public class CommonItem implements IsSerializable {
    private Integer objectID;
    private String name;
    private Integer count;

    public CommonItem() {
    }

    public CommonItem(Integer objectID, String name, Integer count) {
        this.objectID = objectID;
        this.name = name;
        this.count = count;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
