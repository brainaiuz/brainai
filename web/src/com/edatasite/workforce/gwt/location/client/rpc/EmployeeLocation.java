package com.edatasite.workforce.gwt.location.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02.12.2009
 * Time: 21:09:58
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeLocation implements IsSerializable {

    private CompLocationRpc location;
    private SelectItem[] items;
    private boolean isLocationUsed;
    public CompLocationRpc getLocation() {
        return location;
    }

    public void setLocation(CompLocationRpc location) {
        this.location = location;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public boolean isLocationUsed() {
        return isLocationUsed;
    }

    public void setLocationUsed(boolean locationUsed) {
        isLocationUsed = locationUsed;
    }
}
