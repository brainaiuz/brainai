package com.edatasite.workforce.gwt.core.client.ui.treetable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 01-Aug-2010
 * Time: 15:11:13
 */
public interface WfmTreeTableDesigner {

    void treeTableTopPanel(WfmToolBar topPanel);

    void treeTableBottomPanel(WfmToolBar bottomPanel);

    void initDataEmptyTable(WfmTreeTableEmptyDataMessage widget);
}
