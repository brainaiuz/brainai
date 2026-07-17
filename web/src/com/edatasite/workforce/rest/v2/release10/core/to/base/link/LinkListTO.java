package com.edatasite.workforce.rest.v2.release10.core.to.base.link;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 3/28/2018.
 */
public class LinkListTO extends ResponseData {

    private ArrayList<LinkTO> list;

    public LinkListTO() {
    }

    public ArrayList<LinkTO> getList() {
        return list;
    }

    public void setList(ArrayList<LinkTO> list) {
        this.list = list;
    }
}
