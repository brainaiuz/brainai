package com.edatasite.workforce.gwt.core.client.rpc.website;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Mar 1, 2011
 * Time: 7:50:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterItem extends SelectItem {

    private String filter;

    public FilterItem() {

    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

