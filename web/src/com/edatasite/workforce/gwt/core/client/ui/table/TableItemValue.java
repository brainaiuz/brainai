package com.edatasite.workforce.gwt.core.client.ui.table;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 5:32:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableItemValue<T> {

    private T value;

    public TableItemValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
