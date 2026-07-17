package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 03/03/2018.
 */
public class BatchLeadImportRequest extends ResponseData {

    private ArrayList<BatchLeadItemTO> list;

    public ArrayList<BatchLeadItemTO> getList() {
        return list;
    }

    public void setList(ArrayList<BatchLeadItemTO> list) {
        this.list = list;
    }

    public BatchLeadImportRequest() {

    }

    public BatchLeadImportRequest(ArrayList<BatchLeadItemTO> list) {
        this.list = list;
    }
}

