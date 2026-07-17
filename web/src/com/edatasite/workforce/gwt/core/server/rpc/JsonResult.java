package com.edatasite.workforce.gwt.core.server.rpc;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 10/26/15
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonResult implements Serializable {

    private String result;

    public JsonResult() {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
