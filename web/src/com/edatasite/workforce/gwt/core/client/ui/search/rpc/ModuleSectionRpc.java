package com.edatasite.workforce.gwt.core.client.ui.search.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 29/11/11
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class ModuleSectionRpc implements IsSerializable{
    private Integer total;
    private Integer qTime;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getqTime() {
        return qTime;
    }

    public void setqTime(Integer qTime) {
        this.qTime = qTime;
    }
}
