package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 17.03.2009
 * Time: 18:46:04
 * To change this template use File | Settings | File Templates.
 */
public class FromToDate implements IsSerializable {

    private DateNonConvertable from;
    private DateNonConvertable to;

    public FromToDate() {
    }

    public FromToDate(DateNonConvertable from, DateNonConvertable to) {
        this.from = from;
        this.to = to;
    }

    public DateNonConvertable getFrom() {
        return from;
    }

    public void setFrom(DateNonConvertable from) {
        this.from = from;
    }

    public DateNonConvertable getTo() {
        return to;
    }

    public void setTo(DateNonConvertable to) {
        this.to = to;
    }
}
