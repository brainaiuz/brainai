package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: Feb 2, 2010
 * Time: 2:57:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class WfmType<H extends EdsObject> {

    private String stringValue;

    public WfmType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
