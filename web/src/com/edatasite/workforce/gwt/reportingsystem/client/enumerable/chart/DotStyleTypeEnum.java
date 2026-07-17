package com.edatasite.workforce.gwt.reportingsystem.client.enumerable.chart;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 20-Feb-2010
 * Time: 17:18:26
 * To change this template use File | Settings | File Templates.
 */
public enum DotStyleTypeEnum implements IsSerializable {
    POINT("point"),
    DOT("dot"),
    BOW("bow"),
    ANCHOR("anchor"),
    HOLLOW("hollow"),
    STAR("star");

    DotStyleTypeEnum(String type){
        this.type = type;
    }

    private String type;

    DotStyleTypeEnum() {
    }

    public String getType(){
        return type;
    }
}
