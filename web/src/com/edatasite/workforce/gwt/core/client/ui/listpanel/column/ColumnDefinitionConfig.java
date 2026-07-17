package com.edatasite.workforce.gwt.core.client.ui.listpanel.column;

import com.edatasite.workforce.gwt.core.client.WordUtils;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Aug-2010
 * Time: 15:55:09
 * <p/>
 * A definition of a column in a table.
 *
 * @param <T> the type of the row value
 * @param <E> the data type of the column
 */
public abstract class ColumnDefinitionConfig<T, E> extends CustomColumnDefinitionConfig<T, E, String> {

    public ColumnDefinitionConfig(String name, String codeName, int width) {
        this(WordUtils.capitalizeFirst(name), codeName, width, DataType.String);
    }

    public ColumnDefinitionConfig(String name, String codeName, int width, String referenceCode) {
        super(WordUtils.capitalizeFirst(name), codeName, width, DataType.String, referenceCode);
    }

    public ColumnDefinitionConfig(String name, String codeName, int width, DataType dataType) {
        super(WordUtils.capitalizeFirst(name), codeName, width, dataType);
    }

    public ColumnDefinitionConfig(String name, String codeName, int width, boolean hasLink) {
        super(WordUtils.capitalizeFirst(name), codeName, width, hasLink);
    }

    public String getName() {
        return getColumnName();
    }
}
