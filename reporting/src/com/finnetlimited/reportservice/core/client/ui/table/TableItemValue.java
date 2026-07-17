package com.finnetlimited.reportservice.core.client.ui.table;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 21:23:27
 * To change this template use File | Settings | File Templates.
 */
public final class TableItemValue<T> {

    private T value;

    public TableItemValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
