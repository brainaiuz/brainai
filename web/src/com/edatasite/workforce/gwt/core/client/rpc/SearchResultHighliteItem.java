package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Nov 4, 2009
 * Time: 4:54:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResultHighliteItem implements IsSerializable {
    private String field;
    private String higlite;

    public String getField() {

        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getHiglite() {
        return higlite;
    }

    public void setHiglite(String higlite) {
        this.higlite = higlite;
    }
}
