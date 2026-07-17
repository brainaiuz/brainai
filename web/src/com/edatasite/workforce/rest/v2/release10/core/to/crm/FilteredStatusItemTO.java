package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

/**
 * Created by Dilsh0d on 10/16/2017.
 */
public class FilteredStatusItemTO extends FlowSettingsTO {

    private Long count_of_items;

    public FilteredStatusItemTO() {
    }

    public Long getCount_of_items() {
        return count_of_items;
    }

    public void setCount_of_items(Long count_of_items) {
        this.count_of_items = count_of_items;
    }
}
