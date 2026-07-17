package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 5, 2008 Time: 3:34:30 PM To
 * change this template use File | Settings | File Templates.
 */

public class ListData implements IsSerializable, Serializable {

    private static final long serialVersionUID = -7357156575572247664L;

    private Object[] data;
    private int total;

	public ListData() {
	}

	public ListData(Object[] data, int total) {
        this.data = data;
        this.total = total;
    }

    public Object[] getData() {
        return data;
    }


    public int getTotal() {
        return total;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
