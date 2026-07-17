package com.edatasite.workforce.gwt.core.client.ui.treeselect;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.LinkedList;

/**
 * User: Abdulaziz
 * Date: Dec 4, 2009
 * Time: 4:43:15 PM
 */
public class TreeSelectShell extends KpiModal {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private TreeSelect tree;

    public TreeSelectShell(String header, TreeSelectCallback callback) {
        super();
        setTitle(header);
        tree = new TreeSelect();
        tree.setTreeCallback(callback);
        MaterialPanel mainPanel = new MaterialPanel("selectPanelWidget");
        MaterialPanel treePanel = new MaterialPanel("selectPanelWidget__tree");

        treePanel.add(tree);
        mainPanel.add(treePanel);

        WfmButton2 okButton = new WfmButton2(wfmStrings.ok().toUpperCase(), WfmButton2.BTN_PRIMARY);
        okButton.addClickHandler(event -> {
            if (onItemSelected != null) {
                if (tree.getCheckedItems().length > 0) {
                    onItemSelected.selection(tree.getCheckedItems());
                }
            }
            close();
        });
        setCloseButton(true);
        addButton(okButton);
        add(mainPanel);

    }

    public void addRootItems(LinkedList<WfmTreeItem> items) {
        for (WfmTreeItem item : items) {
            tree.add(item, true);
        }
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        tree.setWidth(width - 61);
        tree.setHeight(height - 70);
    }

    public interface OnItemSelected {
        void selection(WfmTreeItem[] items);
    }

    private OnItemSelected onItemSelected;

    public OnItemSelected getOnItemSelected() {
        return onItemSelected;
    }

    public void setOnItemSelected(OnItemSelected onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    public void setTreeSelectCallback(TreeSelectCallback callback) {
        tree.setTreeCallback(callback);
    }

    public TreeSelect getTreeSelect() {
        return tree;
    }
}