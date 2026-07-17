package com.edatasite.workforce.gwt.location.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.ui.ListData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 02.12.2009
 * Time: 15:28:29
 * To change this template use File | Settings | File Templates.
 */
public class LocationList implements IsSerializable {

    private int totalCount;
    private CompLocationRpc[] locations;

    public LocationList() {
    }

    public LocationList(CompLocationRpc[] locations, int totalCount) {
        this.locations = locations;
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public CompLocationRpc[] getLocations() {
        return locations;
    }

    public void setLocations(CompLocationRpc[] locations) {
        this.locations = locations;
    }

    public ListData getListData() {
        return new ListData(locations, totalCount);
    }
}
