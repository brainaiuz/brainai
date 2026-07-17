package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author Sherali
 */
public class PopupTree extends Tree {

    private FolderContextMenu menu;

    private boolean ctrlKeyPressed = false;

    private boolean leftClicked = false;

    private boolean rightClicked = false;

    private TreeItem treeSelectedItem = null;

    private void reDraw() {

    }

    public PopupTree() {
        super(DocumentImages.get());
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);
//		sinkEvents(Event.ONMOUSEDOWN);

        addSelectionHandler(event -> {
            TreeItem item = event.getSelectedItem();
            /*if (item != null) {
                DocumentsView.get().getFolders().update(item);
            }*/
            processItemSelected(item, true);
        });

        addOpenHandler(event -> {
            TreeItem item = event.getTarget();
            if (item != null && item.getState()) {
                DocumentsView.get().getFolders().update(item);
            }

        });
        addHandler(event -> {
            final TreeItem item = getSelectedItem();
            if (item != null) {
                int left = item.getAbsoluteLeft() + 40;
                int top = item.getAbsoluteTop() + 20;
                showPopup(left, top);
            }
        }, ContextMenuEvent.getType());
        // DOM.setStyleAttribute(getElement(), "position", "static");

    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
            return;
        }

        switch (DOM.eventGetType(event)) {
            case Event.ONKEYDOWN:
                int key = DOM.eventGetKeyCode(event);
                if (key == KeyCodes.KEY_CTRL) {
                    ctrlKeyPressed = true;
                }
                break;

            case Event.ONKEYUP:
                key = DOM.eventGetKeyCode(event);
                if (key == KeyCodes.KEY_CTRL) {
                    ctrlKeyPressed = false;
                }
                break;

            case Event.ONMOUSEDOWN:
                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT) {
                    rightClicked = true;
                } else if (DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                    leftClicked = true;
                }
                break;

            case Event.ONMOUSEUP:
                if (DOM.eventGetButton(event) == NativeEvent.BUTTON_RIGHT) {
                    rightClicked = false;
                } else if (DOM.eventGetButton(event) == NativeEvent.BUTTON_LEFT) {
                    leftClicked = false;
                }
                break;
        }

        super.onBrowserEvent(event);
    }

    protected void showPopup(final int x, final int y) {
        if (treeSelectedItem == null) {
            return;
        }
        if (menu != null) {
            menu.hide();
        }
        menu = new FolderContextMenu();
        int left = x;
        int top = y;

        if (left < 0) {
            left = 0;
        }
        if (top < 0) {
            top = 0;
        }
        if (Window.getClientHeight() - top < menu.getSize() * 25) {
            top = Window.getClientHeight() - menu.getSize() * 25 - 15;
        }
        menu.setPopupPosition(left, top);
        menu.show();
    }

    public void processItemSelected(TreeItem item, boolean fireEvents) {

        if (DocumentsView.get().getCurrentSelection() == null || !DocumentsView.get().getCurrentSelection().equals(item.getUserObject())) {
            DocumentsView.get().setCurrentSelection(item.getUserObject());
        }
        if (!item.equals(treeSelectedItem)) {
            processSelection(item);
        } else {
            DocumentsView.get().getFolders().update(item);
        }
        if (!DocumentsView.get().isFileListShowing()) {
            DocumentsView.get().setFileListShowing(true);
            DocumentsView.get().showFileList();
        }

        if (rightClicked) {
            rightClicked = false;
            int left = item.getAbsoluteLeft() + 40;
            int top = item.getAbsoluteTop() + 20;
            showPopup(left, top);
        } else if (leftClicked && ctrlKeyPressed) {
            leftClicked = false;
            ctrlKeyPressed = false;
            int left = item.getAbsoluteLeft() + 40;
            int top = item.getAbsoluteTop() + 20;
            showPopup(left, top);
        }
    }

    public void clearSelection() {
        if (treeSelectedItem != null) {
            ((DnDTreeItem) treeSelectedItem).getContent().removeStyleName("doc-SelectedRow");
        }
        // treeSelectedItem.getWidget().removeStyleName("doc-SelectedRow");

        treeSelectedItem = null;
        setSelectedItem(null, true);
        DocumentsView.get().setCurrentSelection(null);
    }

    private void processSelection(TreeItem item) {
        if (treeSelectedItem != null) {
            clearSelection();
        }
        treeSelectedItem = item;
        setSelectedItem(item, true);
        if (((DnDTreeItem) item).getFolderResource() != null) {
            DocumentsView.get().setCurrentSelection(((DnDTreeItem) item).getFolderResource());
        }
        if (item.getUserObject() instanceof FolderResource) {
            DocumentsView.get().setCurrentSelection(item.getUserObject());
        } else if (item.getUserObject() instanceof OtherUserResource) {
            DocumentsView.get().setCurrentSelection(item.getUserObject());
        } else if (DocumentsView.get().getFolders().isTrash(item)) {
            DocumentsView.get().setCurrentSelection(null);
        }
        ((DnDTreeItem) item).getContent().addStyleName("doc-SelectedRow");
        DocumentsView.get().showFileList(false);
    }

    /**
     * Retrieve the selectedItem.
     *
     * @return the selectedItem
     */
    public TreeItem getTreeSelectedItem() {
        return treeSelectedItem;
    }

    /**
     * Modify the selectedItem.
     *
     * @param newSelectedItem the selectedItem to set
     */
    public void setTreeSelectedItem(TreeItem newSelectedItem) {
        treeSelectedItem = newSelectedItem;
    }

}
