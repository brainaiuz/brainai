package com.edatasite.workforce.gwt.core.client.ui.treeselect;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.filterparams.SuperPuperHandler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Sherali
 * Date: 26.03.12
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public class NTreeSelectItem extends TreeItem implements HasAllMouseHandlers {

    private NFocusPanel focus;
    private Widget content;
    private Tree tree;
    private WfmTreeItem item;
    private final static ArrayList<KpiCheckBox> parentItems = new ArrayList<>();
    public static final int UNCHECK = 0;
    public static final int CHECK = 1;
    private SuperPuperHandler<NTreeSelectItem> command;
    private TreeSelect treeSelector;
    private KpiCheckBox checkBox;
    private boolean childPopulated;
    private Command openChildrenCommand;
    private boolean customChildPopulate;
    private String color;
    private boolean isNew = true;

    public NTreeSelectItem(WfmTreeItem item, TreeSelect treeSelector, Tree atree) {
        this(item, treeSelector, atree, true);
    }

    public NTreeSelectItem(WfmTreeItem item, TreeSelect treeSelector, Tree atree, boolean isChildItem) {
        this(item, treeSelector, atree, isChildItem, false);
    }

    public NTreeSelectItem(WfmTreeItem item, TreeSelect treeSelector, Tree atree, boolean isChildItem, boolean customChildPopulate) {
        super();
        this.item = item;
        this.treeSelector = treeSelector;
        this.tree = atree;
        this.content = getItemText(item.getName(), isChildItem, item.isChecked());
        this.focus = new NFocusPanel(content, this);
        this.focus.setTabIndex(-1);
        this.childPopulated = false;
        this.customChildPopulate = customChildPopulate;
        setWidget(this.focus);
    }

    public void setFocus() {
        ((NFocusPanel) getWidget()).setFocus(true);
    }

    public NTreeSelectItem setSuperPuperHandler(SuperPuperHandler<NTreeSelectItem> command) {
        this.command = command;
        return this;
    }

    public void addItems(LinkedList<WfmTreeItem> childItems) {
        for (WfmTreeItem childItem : childItems) {
            childItem.setParent(item);
            NTreeSelectItem nTreeSelectItem = new NTreeSelectItem(childItem, treeSelector, tree);
            nTreeSelectItem.setSuperPuperHandler(command);
            nTreeSelectItem.setColor(childItem.getColor());
            super.addItem(nTreeSelectItem);
        }
    }

    /**
     * Adds WfmTreeItem as a tree leaf with its name.
     *
     * @param childItem
     */
    public void addItem(final WfmTreeItem childItem) {
        childItem.setParent(item);
        final NTreeSelectItem treeItem = new NTreeSelectItem(childItem, treeSelector, tree);
        treeItem.setSuperPuperHandler(command);
        super.addItem(treeItem);
    }

    public Widget getContent() {
        return content;
    }

    protected void removeItems(TreeItem item) {
        for (int i = 0; i < item.getChildCount(); i++) {
            TreeItem it = item.getChild(i);
            removeItems(it);
        }
    }

    /**
     * Returns the item that is stored.
     *
     * @return
     */
    public WfmTreeItem getItem() {
        return item;
    }

    /**
     * Returns the value of the item whether checked or not.
     *
     * @return
     */
    public boolean isChecked() {
        return checkBox.getValue();
    }

    /**
     * Sets the value to the item whether checked or not and puts necessary image according to the status.
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checkBox.setValue(checked);
        getItem().setChecked(checked);
        getItem().setType(checked ? CHECK : UNCHECK);
        if (checked) {
            treeSelector.addCheckedItem(this);
        } else {
            treeSelector.removeCheckedItem(this);
        }
    }

    /**
     * Puts the necessary style after determining of the item type.
     *
     * @param text
     * @param isChildItem
     * @return
     */
    private Widget getItemText(final String text, final boolean isChildItem, boolean isChecked) {
        HorizontalPanel hp = new HorizontalPanel();

        checkBox = new KpiCheckBox("", false);
        HTML html = new HTML("<span>" + text + "</span>");
        hp.add(checkBox);
        checkBox.setValue(isChecked);
        hp.add(html);
        if (!isChildItem) {
            parentItems.add(checkBox);
        }
        checkBox.addClickHandler(event -> {
            if (customChildPopulate && checkBox.getValue() != null && checkBox.getValue() && parentItems.contains(checkBox) && !NTreeSelectItem.this.isChildPopulated()) {
                openChildrenCommand = () -> fireCommand();
                NTreeSelectItem.this.setState(true);
            } else {
                fireCommand();
            }
        });
        return hp;
    }

    public void fireCommand() {
        final NTreeSelectItem item = this;
        boolean isCheked = isChecked();//item.getItem().isChecked();
        setChecked(isCheked);
        setChild(isCheked);
        if (command != null) {
            command.onFire(item);
        }
    }

    public void setChild(boolean isCheked) {
        for (int i = 0; i < getChildCount(); i++) {
            NTreeSelectItem selectItem = (NTreeSelectItem) getChild(i);
            selectItem.setChecked(isCheked);
        }
    }

    public static ArrayList<KpiCheckBox> getParentItems() {
        return parentItems;
    }

    public Command getOpenChildrenCommand() {
        return openChildrenCommand;
    }

    public boolean isChildPopulated() {
        return childPopulated;
    }

    public void setChildPopulated(boolean childPopulated) {
        this.childPopulated = childPopulated;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void insertItem(TreeItem item, int position) {
        addItem(item);
        DOM.insertChild(getElement(), item.getElement(), position);
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return focus.addMouseDownHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        focus.fireEvent(event);

    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return focus.addMouseUpHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return focus.addMouseOutHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return focus.addMouseOverHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return focus.addMouseMoveHandler(handler);
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return focus.addMouseWheelHandler(handler);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
