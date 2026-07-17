package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 12/14/15.
 */
public class EmailFolderTreeItem extends TreeItem implements HasAllMouseHandlers {
    private Widget content;
    public Tree tree;
    private EmailFolder item;
    private EmailFolderTreeItem parent;
    private KpiCheckBox checkBox;
    private List<EmailFolderTreeItem> children;

    public EmailFolderTreeItem(EmailFolder item, Tree tree, EmailFolderTreeItem parent) {
        super();
        this.checkBox = new KpiCheckBox();
        this.checkBox.addValueChangeHandler(booleanValueChangeEvent -> renderChildren(getTreeItem(), true, booleanValueChangeEvent.getValue()));
        if (item.isFetchable()) {
            this.checkBox.setValue(true, true);
        }
        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(3);
        hp.add(checkBox);
        hp.add(new HTML(item.getName()));
        content = new FocusPanel(hp, this);
        this.item = item;
        this.tree = tree;
        this.parent = parent;
        setWidget(content);
        ((Element) this.getElement().getChild(0)).getStyle().clearDisplay();
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);
        if (this.parent != null) {
            parent.addChildren(this);
        }
    }

    public void addChildren(EmailFolderTreeItem child) {
        if (child != null) {
            getChildren().add(child);
        }
    }

    private void renderChildren(EmailFolderTreeItem treeItem, boolean isParent, Boolean value) {
        treeItem.getCheckBox().setValue(value);
        treeItem.getCheckBox().setEnabled((isParent && value) || !value);
        if (treeItem.getChildren().size() > 0) {
            for (EmailFolderTreeItem treeItem_ : treeItem.getChildren()) {
                renderChildren(treeItem_, false, value);
            }
        }
    }

    public EmailFolder getItem() {
        return item;
    }

    public KpiCheckBox getCheckBox() {
        return checkBox;
    }

    public List<EmailFolderTreeItem> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    private EmailFolderTreeItem getTreeItem() {
        return this;
    }

    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler mouseDownHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler mouseMoveHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseOutHandler(MouseOutHandler mouseOutHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler mouseOverHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler mouseUpHandler) {
        return null;
    }

    @Override
    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler mouseWheelHandler) {
        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent) {

    }

    private class FocusPanel extends com.google.gwt.user.client.ui.FocusPanel {
        EmailFolderTreeItem treeItem;

        private FocusPanel(Widget widget, EmailFolderTreeItem anItem) {
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

        private EmailFolderTreeItem getTreeItem() {
            return treeItem;
        }
    }
}
