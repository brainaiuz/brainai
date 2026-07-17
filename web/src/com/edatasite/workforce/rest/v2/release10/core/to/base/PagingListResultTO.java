
package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.List;


/**
 * Created by Dilsh0d on 01/17/2018.
 */
public class PagingListResultTO<T> extends PagingResultTO {

    private List<T> list;

    public PagingListResultTO() {
    }

    public PagingListResultTO(List<T> list, Integer totalCount) {
        this.list = list;
        this.setTotal_count(totalCount);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

