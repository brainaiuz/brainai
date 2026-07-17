package com.edatasite.workforce.gwt.core.client.rpc.funnel.tags;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Date: 02.08.12
 * Time: 10:34
 */
public class PathL extends PathBase implements IsSerializable {
    public PathL(double x, double y) {
        super(x, y);
        this.setCommand("L");
    }

    public PathL() {
        this.setCommand("L");
    }
}
