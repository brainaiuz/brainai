package com.edatasite.workforce.gwt.core.client.rpc.funnel.tags;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Date: 02.08.12
 * Time: 10:34
 */
public class PathC extends PathBase implements IsSerializable {
    public PathC(double x, double y) {
        super(x, y);
        this.setCommand("C");
    }

    public PathC() {
        super();
        this.setCommand("C");
    }
}
