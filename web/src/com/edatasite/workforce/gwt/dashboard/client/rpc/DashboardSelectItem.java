package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 06.11.2009
 * Time: 17:40:27
 * To change this template use File | Settings | File Templates.
 */
public class DashboardSelectItem implements IsSerializable {

    private Integer selectId;
    private SelectItem[] iteams;

    public Integer getSelectId() {
        return selectId;
    }

    public void setSelectId(Integer selectId) {
        this.selectId = selectId;
    }

    public SelectItem[] getIteams() {
        return iteams;
    }

    public void setIteams(SelectItem[] iteams) {
        this.iteams = iteams;
    }
}
