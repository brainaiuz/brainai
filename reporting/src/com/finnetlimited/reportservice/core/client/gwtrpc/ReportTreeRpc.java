package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 06-Apr-2010
 * Time: 15:28:16
 */

public class ReportTreeRpc extends HashMap<String, ReportTreeRpc> implements IsSerializable {

    private String name;
    private ArrayList<String> columns;

    public ReportTreeRpc() {
    }

    public ReportTreeRpc(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
