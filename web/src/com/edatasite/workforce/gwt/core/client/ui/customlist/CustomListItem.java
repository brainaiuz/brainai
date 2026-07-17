package com.edatasite.workforce.gwt.core.client.ui.customlist;

import com.edatasite.workforce.gwt.client.client.ui.DeleteHandler;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Mar 27, 2010
 * Time: 3:55:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomListItem extends Composite {

    @UiTemplate("com.edatasite.workforce.gwt.core.client.ui.customlist.CustomListItem.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, CustomListItem> {
    }

    @UiField
    FlexTable canvas;

    private CustomList list;
    private KpiCheckBox checkbox;
    private Tree tree;
    private TreeItem lastParentItem;

    private LinkedHashMap<SelectItem, TreeItem> treeItems;

    private SelectItem item;
    private DeleteHandler<CustomListItem> onRemove;

    public CustomListItem(SelectItem item) {
        this.item = item;

        MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));

        int row = canvas.getRowCount();

        canvas.setCellSpacing(2);
        canvas.setCellPadding(2);
        canvas.setHTML(row, 0, item.getName());
    }

    /**
     * This method will work only in case of list's design is TREE. The principle of
     * working of this method is hierarchically adding items to the tree and its
     * children. Every added item will be inderted the last child and that added item
     * becomes child that accepts the next item as its child.
     *
     * @param item - item that has to be added as a child.
     */
    public void addChild(SelectItem item) {
        if (list.getDesign() == Design.TREE) {
//            TreeItem treeItem = new TreeItem(item.getName());
//            TreeItem treeItem = new TreeItem(item.getName());
//            addChild(treeItem);
            TreeItem treeItem;
            if (tree.getItemCount() == 0) {
                treeItem =  tree.addTextItem(item.getName());
            } else {
                treeItem = lastParentItem.addTextItem(item.getName());
            }

            lastParentItem = treeItem;

            setExpand(true);

            treeItems.put(item, lastParentItem);
        }
    }

    /**
     * Adds tree item either to the tree or its child item.
     *
     * @param item - item that has to be added to proper place.
     */
    private void addChild(TreeItem item) {
        if (tree.getItemCount() == 0) {
            tree.addItem(item);
        } else {
            lastParentItem.addItem(item);
        }

        lastParentItem = item;

        setExpand(true);
    }

    /**
     * This method will work only in case of list's design is CHECK. The principle of
     * working of this method is firing handler when the item is checked or not.
     *
     * @param handler - handler that has to be added to checkbox as a value change handler.
     */
    public void addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        if (list.getDesign() == Design.CHECK) {
            checkbox.addValueChangeHandler(handler);
        }
    }

    public void addWidget(Widget widget) {
        int row = canvas.getRowCount();
        canvas.setWidget(row, 0, widget);
    }

    /**
     * Returns added item to the list item.
     *
     * @return - returns the main item that have been added through constructor.
     */
    public SelectItem getItem() {
        return item;
    }

    public LinkedHashMap<SelectItem, TreeItem> getTreeItems() {
        return treeItems;
    }

    /**
     * Returns value or state of the checkbox, if list's design is CHECK.
     * Otherwise it will return null instead of true or false.
     *
     * @return - returns current state of the checkbox.
     */
    public Boolean getValue() {
        if (list.getDesign() == Design.CHECK) {
            return checkbox.getValue();
        }

        return null;
    }

    public Widget getWidget(int index) {
        return canvas.getWidget(index, 0);
    }

    /**
     * Current method will work only in case of Design.DELETE,
     * otherwise this method redundant for other options.
     *
     * @param onRemove
     */
    public void onRemoveRow(DeleteHandler<CustomListItem> onRemove) {
        this.onRemove = onRemove;
    }

    /**
     * When list's design is TREE, this method will work. The principle of working of this
     * method is removing given child from the tree and re-draw the whole tree in order to
     * keep binding with other items.
     *
     * @param item - item that has to be removed.
     */
    public void removeChild(SelectItem item) {
        if (list.getDesign() == Design.TREE) {
            tree.clear();
            treeItems.remove(item);

            for (TreeItem treeItem : treeItems.values()) {
                treeItem.removeItems();

                addChild(treeItem);
            }
        }
    }

    /**
     * It removes all its children from the current list item when the list's design is TREE.
     */
    public void removeChildren() {
        if (list.getDesign() == Design.TREE) {
            tree.removeItems();
        }
    }

    /**
     * Whether checks the checkbox or not. This is only feasible when the design is CHECK
     * and now there is no need to check to such condition, because at CustomList it's inspecting.
     *
     * @param check - value that whether checks or not.
     */
    public void setCheck(boolean check) {
        checkbox.setValue(check);
    }

    /**
     * Whether checks the checkbox or not. This is only feasible when the design is CHECKBOX
     * and now there is no need to check such condition, because at CustomList it's inspecting.
     * There is also fireevents that true value fires events of checkbox.
     *
     * @param check
     * @param fireEvents
     */
    protected void setCheck(boolean check, boolean fireEvents) {
        checkbox.setValue(check, fireEvents);
    }

    public void setEnabled(boolean enabled) {
        checkbox.setEnabled(enabled);
    }

    /**
     * Set's CustomList to it's item in order to access all of applicable methods of that list.
     * When this method is called, it will re-draw panel according to the design of Custom List.
     *
     * @param list - list that includes this item.
     */
    protected void setList(CustomList list) {
        this.list = list;

        if (list.getDesign() == Design.NONE) {
            return;
        } else {
            canvas.clear();
            canvas.removeAllRows();
        }

        final int row = canvas.getRowCount();
        if (list.getDesign() == Design.CHECK) {
            checkbox = new KpiCheckBox(" " + item.getName(), true);

            if (item.isSelected()) {
                checkbox.setChecked(true);
            }
            canvas.setWidget(row, 0, checkbox);
        } else if (list.getDesign() == Design.TREE) {
            tree = new Tree();
            lastParentItem = tree.addTextItem(item.getName());//new TreeItem(item.getName());
//            tree.addItem(lastParentItem);

            treeItems = new LinkedHashMap<>();
            treeItems.put(item, lastParentItem);

            canvas.setWidget(row, 0, tree);
        } else if (list.getDesign() == Design.DELETE) {
            Image clearImage = new Image("mainStyles/icons/remove-icon.png");
            clearImage.setStyleName("search-pager");//cursor:pointer;
            clearImage.addClickHandler(event -> {
                CustomListItem.this.list.removeItem(CustomListItem.this);

                if (onRemove != null) {
                    onRemove.onDelete(CustomListItem.this);
                }

                canvas.removeRow(row);
                removeFromParent();
            });

            canvas.setWidget(row, 0, clearImage);
            canvas.setText(row, 1, item.getName());
            canvas.getCellFormatter().setWidth(row, 0, "16px");
        }
    }

    /**
     * This method will work only in case of list's design is TREE. The principle of
     * working of this method is expanding or collapsing tree according to the value.
     *
     * @param expand - value that clarifies either expanding or collapsing.
     */
    private void setExpand(boolean expand) {
        for (TreeItem item : treeItems.values()) {
            item.setState(expand, false);
        }
    }
}