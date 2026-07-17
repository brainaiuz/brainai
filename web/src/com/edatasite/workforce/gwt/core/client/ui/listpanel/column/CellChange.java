package com.edatasite.workforce.gwt.core.client.ui.listpanel.column;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Nov-2010
 * Time: 20:41:00
 */
public interface CellChange<T> {
    void saveCell(T rowValue, String columnCodeName);
}
