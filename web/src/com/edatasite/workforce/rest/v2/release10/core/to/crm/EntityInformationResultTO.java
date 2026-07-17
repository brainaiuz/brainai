package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
/**
 * Created by Abdurakhmonov Farrukh on 3/27/2018.
 **/
public class EntityInformationResultTO extends ResponseData {
    private Object item;

    public EntityInformationResultTO(Object item) {
        this.item = item;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
