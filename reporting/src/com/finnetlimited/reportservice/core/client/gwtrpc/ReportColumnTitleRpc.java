package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: ${Dilsh0d}
 * Date: 30-Mar-2010
 * Time: 19:06:00
 */
public final class ReportColumnTitleRpc implements IsSerializable {

    private HashMap<String, String> colunmTitle;
    private ArrayList<String> columns;

    public HashMap<String, String> getColunmTitle() {
        return colunmTitle;
    }

    public void setColunmTitle(HashMap<String, String> colunmTitle) {
        this.colunmTitle = colunmTitle;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
}
