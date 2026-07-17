package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sherali
 */
public class ContactCategoryTreeItem extends TreeItem implements HasAllMouseHandlers {
    private Widget content;
    public ContactCategoryTree tree;
    private ContactCategoryListItem item;
    private SelectItem selectItem;
    private ContactCategoryTreeItem parent;
    private KpiCheckBox checkBox;
    private List<ContactCategoryTreeItem> children;

    public ContactCategoryTreeItem(SimpleLink link) {
        super(link);
        this.content = link;
        setWidget(content);
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);
    }

    public ContactCategoryTreeItem(ContactCategoryListItem item, ContactCategoryTree tree, ContactCategoryTreeItem parent) {
        super();
        this.checkBox = new KpiCheckBox();
        this.checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
            renderChildren(getTreeItem(), true, booleanValueChangeEvent.getValue());
            if (getTree() != null) {
                getContactCategoryTree().onCheckBoxSelected();
            }
        });
        if (item.isSelected()) {
            this.checkBox.setValue(true, true);
        }
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(3);
        checkBox.setHTML(item.getName());
        hp.add(checkBox);
        content = new FocusPanel(hp, this);
        this.item = item;
        this.tree = tree;
        this.parent = parent;
        setWidget(content);
        ((Element)this.getElement().getChild(0)).getStyle().clearDisplay();
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);
        if (this.parent != null) {
            parent.addChildren(this);
        }
    }

    public void renderChildren(ContactCategoryTreeItem treeItem, boolean isParent, Boolean value) {
        treeItem.getCheckBox().setValue(value);
        treeItem.getCheckBox().setEnabled((isParent && value) || !value);
        if (treeItem.getChildren().size() > 0) {
            for (ContactCategoryTreeItem treeItem_ : treeItem.getChildren()) {
                renderChildren(treeItem_, false, value);
            }
        }
    }

    public ContactCategoryTree getContactCategoryTree() {
        return tree;
    }

    public Widget getContent() {
        return content;
    }

    private ContactCategoryTreeItem getTreeItem() {
        return this;
    }

    public ContactCategoryListItem getItem() {
        return item;
    }

    public void setItem(ContactCategoryListItem item) {
        this.item = item;
    }

    public Integer getObjectID() {
        return this.item != null ? item.getObjectID() : null;
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler mouseUpHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler mouseOutHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler mouseMoveHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler mouseWheelHandler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent) {

    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler mouseDownHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler mouseOverHandler) {
        return null;
    }

    public ContactCategoryTreeItem getParent() {
        return parent;
    }

    public void setParent(ContactCategoryTreeItem parent) {
        this.parent = parent;
    }

    public KpiCheckBox getCheckBox() {
        return checkBox;
    }

    public List<ContactCategoryTreeItem> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<ContactCategoryTreeItem> children) {
        this.children = children;
    }

    public void addChildren(ContactCategoryTreeItem child) {
        if (child != null) {
            getChildren().add(child);
        }
    }

    public boolean isChecked() {
        return checkBox.getValue();
    }

    public SelectItem getAsSelectedItem() {
        if (getItem() != null && selectItem == null) {
            selectItem = new SelectItem(getItem().getObjectID(), getItem().getName());
        }
        return selectItem;
    }

    private class FocusPanel extends com.google.gwt.user.client.ui.FocusPanel {
        ContactCategoryTreeItem treeItem;

        private FocusPanel(Widget widget, ContactCategoryTreeItem anItem) {
            super(widget);
            sinkEvents(Event.ONMOUSEDOWN);
            this.treeItem = anItem;
        }

        @Override
        public void onBrowserEvent(Event event) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN:
                    if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT || DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                        getTreeItem().tree.setSelectedItem(getTreeItem());
                    }
                    break;
            }
            super.onBrowserEvent(event);
        }

        private ContactCategoryTreeItem getTreeItem() {
            return treeItem;
        }
    }
}