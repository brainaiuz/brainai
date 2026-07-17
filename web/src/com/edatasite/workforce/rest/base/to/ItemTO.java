package com.edatasite.workforce.rest.base.to;

/**
 * Created by Dilsh0d Madrahimov on 12/3/2016 12:52 PM.
 */
public class ItemTO extends SelectItemTO {

    public ItemTO() {

    }

    public ItemTO(Integer id, String name, String code, String description) {
        super(id, name, code, description);
    }
}
