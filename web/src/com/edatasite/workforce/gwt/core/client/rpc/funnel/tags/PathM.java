package com.edatasite.workforce.gwt.core.client.rpc.funnel.tags;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Date: 02.08.12
 * Time: 10:34
 */
public class PathM extends PathBase implements IsSerializable {
    public PathM(double x, double y) {
        super(x, y);
        this.setCommand("M");
    }

    public PathM() {
        super();
        this.setCommand("M");
    }
}
