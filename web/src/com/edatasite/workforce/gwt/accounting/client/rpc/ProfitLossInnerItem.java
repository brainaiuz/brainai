package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 03.03.2009
 * Time: 12:13:54
 * To change this template use File | Settings | File Templates.
 */
public class ProfitLossInnerItem implements IsSerializable {
    private String name;
    private Double[] values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double[] getValues() {
        return values;
    }

    public void setValues(Double[] values) {
        this.values = values;
    }
}
