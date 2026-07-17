package com.edatasite.workforce.gwt.core.client.ui.treetable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jul-2010
 * Time: 07:08:27
 */
public interface WfmTreeTableCallbackProvider {
    /**
     * Callback Provider
     * @param treeItem
     * @param item
     * @param callback
     */
    void getTreeTableData(Object treeItem, TreeItem item, TreeListDataCallback callback);
}
