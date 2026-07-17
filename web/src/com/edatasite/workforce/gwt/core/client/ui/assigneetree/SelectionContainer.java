package com.edatasite.workforce.gwt.core.client.ui.assigneetree;

import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.MultiSelectionModel;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 25.05.12
 * Time: 15:12
 */
public interface SelectionContainer {

    void selectedDataGrid(KpiDataGrid<KpiTreeInfo> selectedDataGrid, ColumnSortEvent.ListHandler<KpiTreeInfo> sortHandler, MultiSelectionModel<KpiTreeInfo> selectionModel);

    void additionalActions(HTMLPanel actionsPanel);
}
