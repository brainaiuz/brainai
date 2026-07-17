package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov 03/28/2018.
 */
public class EntityContactAddressListTO extends ResponseData {

    private ArrayList<EntityContactAddressTO> list;

    public ArrayList<EntityContactAddressTO> getList() {
        return list;
    }

    public void setList(ArrayList<EntityContactAddressTO> list) {
        this.list = list;
    }
}
