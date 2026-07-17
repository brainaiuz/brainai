package com.edatasite.workforce.gwt.documents.client.table;

import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;

/**
 * An iterator that serves as the data source for TableOracle requests.
 */
public class DataSourceModel<T> extends MutableTableModel<T> {
    /**
     * The RPC service used to generate data
     */

    /**
     * Override that can optionally throw an error.
     */
    @Override
    public void requestRows(final Request request,
                            final Callback<T> callback) {
    }

    @Override
    protected boolean onRowInserted(int beforeRow) {
        return true;
    }

    @Override
    protected boolean onRowRemoved(int row) {
        return true;
    }

    @Override
    protected boolean onSetRowValue(int row, T rowValue) {
        return true;
    }
}