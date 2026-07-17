package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import java.util.HashSet;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 30-Oct-2010
 * Time: 14:12:33
 */
public interface ListingPanelRowSelectionHandler<T> {
    void onSelectedRows(HashSet<T> selectedRows);
}
