package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

/**
 * Created by Abdurakhmonov Farrukh on 12/26/2017.
 */
public class OpportunityFilteredStatusItemTO extends FlowSettingsTO {


    private CurrencyValueTO status_price;
    private Long count_of_items;

    public OpportunityFilteredStatusItemTO() {
    }

    public CurrencyValueTO getStatus_price() {
        return status_price;
    }

    public void setStatus_price(CurrencyValueTO status_price) {
        this.status_price = status_price;
    }

    public Long getCount_of_items() {
        return count_of_items;
    }

    public void setCount_of_items(Long count_of_items) {
        this.count_of_items = count_of_items;
    }
}
