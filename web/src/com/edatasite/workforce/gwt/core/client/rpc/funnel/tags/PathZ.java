package com.edatasite.workforce.gwt.core.client.rpc.funnel.tags;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Date: 02.08.12
 * Time: 10:34
 */
public class PathZ extends PathBase implements IsSerializable {
    public PathZ() {
        this.setCommand("z");
    }

    @Override
    public String getString(double offsetX, double offsetY) {
        return " " + getCommand();
    }
}
