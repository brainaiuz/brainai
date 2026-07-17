package com.edatasite.workforce.gwt.core.client.ui.selectPanel;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.edatasite.workforce.gwt.core.client.ui.table.Table;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.table.TableItem;
import com.edatasite.workforce.gwt.core.client.ui.table.TableItemValue;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid
 * Date: Aug 11, 2010
 * Time: 3:36:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectPanel extends FlowPanel {
    private final static WfmStrings wfmStrings = WfmStrings.App.get();
    private final HashMap<Integer, TableItem> removeLinkMap = new HashMap<>();
    private Table table;
    public final HashMap<TableItem, NTreeSelectItem> tableItemTreeMap = new HashMap<>();
    private TreeSelect treeSelect;
    private final String width = "50px";
    private boolean fromCalendar = false;
    private ISelectPanel selectPanelAction;

    public SelectPanel(TableColumn[] tableColumns) {
        this(tableColumns, null, false);
    }

    public SelectPanel(TableColumn[] tableColumns, Boolean hideLeftPanel) {
        this(tableColumns, null, hideLeftPanel);
    }

    public SelectPanel(TableColumn[] columns, final Widget widget, Boolean hideLeftPanel) {
        super();
        SuperPuperHandler<NTreeSelectItem> command = nTreeSelectItem -> {
            Set<SelectItem> selectList = new HashSet<>();
            if (nTreeSelectItem.getParentItem() == null) {     //Tick ITEMs
                for (int i = 0; i < nTreeSelectItem.getChildCount(); i++) {
                    NTreeSelectItem selectedItem = (NTreeSelectItem) nTreeSelectItem.getChild(i);
                    selectList.add(new SelectItem(selectedItem.getItem().getId(), selectedItem.getItem().getName()));
                    onTreeItemSelection(selectedItem, widget);
                }
            } else if (nTreeSelectItem.getChildCount() == 0) { //Child Item chnaged value
                //Besides, we have another selection handler, and this one is firing first, therefore we  have reverse value of checked.
                onTreeItemSelection(nTreeSelectItem, widget);
                selectList.add(new SelectItem(nTreeSelectItem.getItem().getId(), nTreeSelectItem.getItem().getName()));
            }
            if(selectPanelAction != null){
                selectPanelAction.addItem(selectList, nTreeSelectItem.isChecked());
            }
        };
        treeSelect = new TreeSelect().setHandler(command);

        initTable(columns);

        MaterialPanel mainPanel = new MaterialPanel("selectPanelWidget");
        MaterialPanel treePanel = new MaterialPanel("selectPanelWidget__tree");

        treePanel.add(treeSelect);
        mainPanel.add(treePanel);

        if (!hideLeftPanel) {
            MaterialPanel tablePanel = new MaterialPanel("selectPanelWidget__table");
            tablePanel.add(table);
            mainPanel.add(tablePanel);
        }
        add(mainPanel);
    }

    /**
     * Adds Style to TreeView
     *
     * @param style
     */
    public void addTreePanelStyle(String style) {
        treeSelect.addStyleName(style);
    }

    public void checkAllItems(boolean checked) {
        treeSelect.checkAllItems(checked);
    }

    /**
     * Clear all items of TreePanel.
     * It's used before setting new values to TreeView.
     */
    public void clearTreeView() {
        tableItemTreeMap.clear();
        removeLinkMap.clear();
        table.clear();
        treeSelect.clearTree();
    }

    public void clearTable() {
        tableItemTreeMap.clear();
        removeLinkMap.clear();
        table.clear();
    }

    /**
     * Expands treeView
     */
    public void expandTreeView() {
        treeSelect.expandAll();
    }

    /*
    * returns all selected items Id
    * */

    public Integer[] getSelectedItems() {
        return treeSelect.getCheckedItemsID();
    }

    /*
   *Returns tree of select panel
    */

    public Tree getTree() {
        return treeSelect.getTree();
    }

    private Table initTable(TableColumn[] tableColumns) {
        table = new Table(tableColumns, false).setWordWrap(false);
        return table;
    }

    public void onTreeItemSelection(final NTreeSelectItem selectedItem, final Widget widget) {
        final int rowCount = widget == null ? (fromCalendar ? 3 : 2) : 2;
        final Integer selectedItemID = selectedItem.getItem().getId();
        if (selectedItem.getChildCount() == 0) {
            if (selectedItem.isChecked()) {
                checkItem(selectedItem, widget, rowCount, selectedItemID);
            } else {
                unCheckItem(selectedItemID);
            }
        }
    }

    private void unCheckItem(Integer selectedItemID) {
        table.removeItem(removeLinkMap.get(selectedItemID));
        tableItemTreeMap.remove(removeLinkMap.get(selectedItemID));
        removeLinkMap.remove(selectedItemID);
    }

    private void checkItem(final NTreeSelectItem selectedItem, final Widget widget, int rowCount, final Integer selectedItemID) {
        if (!removeLinkMap.containsKey(selectedItemID)) {
            final TableItemValue<String> text = new TableItemValue<>(selectedItem.getText());
            final Anchor aRemove = new Anchor(wfmStrings.delete());
            final TableItemValue<Anchor> removeItem = new TableItemValue<>(aRemove);
            final TableItemValue[] tableItemValues = new TableItemValue[rowCount];
            tableItemValues[0] = text;
            if (widget != null) {
                if (widget instanceof TextBox) {
                    final TextBox textBox = new TextBox();
                    textBox.setWidth(width);
                    tableItemValues[1] = new TableItemValue(textBox);
                } else if (widget instanceof KpiCheckBox && !(widget instanceof RadioButton)) {
                    final KpiCheckBox checkBox = new KpiCheckBox();
                    tableItemValues[1] = new TableItemValue(checkBox);
                } else if (widget instanceof RadioButton) {
                    final RadioButton rb = new KpiRadioButton("rb");
                    tableItemValues[1] = new TableItemValue(rb);
                }
            }

            tableItemValues[rowCount - 1] = removeItem;
            final TableItem tableItem = new TableItem(tableItemValues);
            table.addItem(tableItem);
            removeLinkMap.put(selectedItemID, tableItem);
            tableItemTreeMap.put(tableItem, selectedItem);
            treeSelect.removeStyleName("x-form-invalid");
            aRemove.addClickHandler(event -> {
                tableItemTreeMap.get(tableItem).setChecked(false);
                selectedItem.setChecked(false);
                table.removeItem(tableItem);
                tableItemTreeMap.remove(tableItem);
                removeLinkMap.remove(selectedItemID);

                if (selectPanelAction != null) {
                    selectPanelAction.removeItem(new SelectItem(selectedItemID, selectedItem.getItem().getName()));
                }
            });
        }
    }

    public void setHeight(int height) {
        setTableHeight(height + 20);
        setTreePanelHeight(height);
    }

    /*
   * sets width of main panel
   * */

    public void setPanelWidth(int width) {
        this.setWidth(width + "px");
    }

    public void setTableWidth(int width) {
        table.setWidth(width + "px");
    }

    /**
     * Sets height of table which presents selected items
     *
     * @param height
     */
    public void setTableHeight(int height) {
        table.setHeight(height + "px");
    }

    /**
     * Sets new value to TreeView
     *
     * @param treeParentChildrenNodes map contains parent key and child array value
     */
    public void setTreeParentChildrenNodes(LinkedHashMap<WfmTreeItem, LinkedList<WfmTreeItem>> treeParentChildrenNodes) {
        if (treeSelect != null) {
            clearTreeView();
            for (Map.Entry<WfmTreeItem, LinkedList<WfmTreeItem>> treeItemEntry : treeParentChildrenNodes.entrySet()) {
                treeSelect.add(treeItemEntry.getKey(), treeItemEntry.getValue());
            }
        }
    }

    public void addTreeItem(WfmTreeItem parent, LinkedList<WfmTreeItem> children) {
        treeSelect.add(parent, children);
    }

    /*
   * sets default  text to searchbox
   * */

    public void setSearchText(String text) {
        treeSelect.setSearchText(text);
    }
    /*
   * sets width of searchbox
   * */

    public void setSearchBoxWidth(String width) {
        treeSelect.setSearchBoxWidth(width);
    }
    /*
   * sets heightof searchbox
   * */

    public void setSearchBoxHeight(String height) {
        treeSelect.setSearchBoxHeight(height);
    }
    /*
    * sets width of tree-panel
    * */

    public void setTreePanelWidth(int width) {
        treeSelect.setWidth(width);
    }
    /*
   * sets height of tree-panel
   * */

    public void setTreePanelHeight(int height) {
        treeSelect.setHeight(height);
    }
    /*
   * sets size of tree-panel
   * */

    public void setSize(int width, int height) {
        setTableWidth(width);
        setTreePanelWidth(width);
    }
    /*
   * sets height of tree-panel
   * */


    public void setTableStyle(String style) {
        table.addStyleName(style);
    }

    public TreeSelect getTreeSelect() {
        return treeSelect;
    }

    public void setTreeSelect(TreeSelect treeSelect) {
        this.treeSelect = treeSelect;
    }

    public Table getTable() {
        return table;
    }

    public void hideAvailablityCheckBox() {
        treeSelect.hideAvailablityCheckBox();
    }

    public void setFromCalendar(boolean fromCalendar) {
        this.fromCalendar = fromCalendar;
    }

    public void setSelectPanelAction(ISelectPanel iSelectPanel) {
        this.selectPanelAction = iSelectPanel;
    }

    public void setDefaultSettings() {
        hideAvailablityCheckBox();
        getTreeSelect().setLoadAll(true);
    }

    public int addItems(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
        int employeeCount = 0;
        for (WfmTreeItem treeItem : items.keySet()) {
            LinkedList<WfmTreeItem> employeesItems = items.get(treeItem);
            addTreeItem(treeItem, employeesItems);
            employeeCount += employeesItems.size();
        }
        for (int i = 0; i < getTree().getItemCount(); i++) {
            NTreeSelectItem parent = (NTreeSelectItem) getTree().getItem(i);
            for (int j = 0; j < parent.getChildCount(); j++) {
                NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                if (child != null && child.isChecked() && child.isNew()) {
                    child.setChecked(true);
                    onTreeItemSelection(child, null);
                    child.setNew(false);
                }
            }
        }
        return employeeCount;
    }

    public int addSelectedItems(HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> items) {
        int countSelected = 0;
        TreeSelect treeSelect = getTreeSelect();
        Tree tree = getTree();
        for (WfmTreeItem treeItem : items.keySet()) {
            LinkedList<WfmTreeItem> employeesItems = items.get(treeItem);
            for (WfmTreeItem item : employeesItems) {
                NTreeSelectItem nTreeSelectItem = new NTreeSelectItem(item, treeSelect, tree);
                nTreeSelectItem.setChecked(true);
                onTreeItemSelection(nTreeSelectItem, null);
            }
            countSelected += employeesItems.size();
        }
        return countSelected;
    }
}
