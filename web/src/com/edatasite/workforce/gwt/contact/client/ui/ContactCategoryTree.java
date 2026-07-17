package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: hayot
 * Date: Nov 27, 2010
 * Time: 5:32:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactCategoryTree extends Tree {
    private ContextMenu menu;

    private boolean ctrlKeyPressed = false;

    private boolean leftClicked = false;

    private boolean rightClicked = false;

    private TreeItem treeSelectedItem = null;

    private Command menuGetter;

    private Command onCheckBoxSelected;

    public ContactCategoryTree() {
        super();
        sinkEvents(Event.ONCONTEXTMENU);
        sinkEvents(Event.ONMOUSEUP);

        addSelectionHandler(event -> {
            TreeItem item = event.getSelectedItem();
            processItemSelected(item, true);
        });
        addHandler(event -> {
            TreeItem item = getSelectedItem();
            if (item != null) {
                int left = item.getAbsoluteLeft() + 40;
                int top = item.getAbsoluteTop() + 20;
                showPopup(left, top);
            }
        }, ContextMenuEvent.getType());
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
        int left = x;
        int top = y;
        if (menuGetter != null) {
            menuGetter.execute();
        }
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
        if (!item.equals(treeSelectedItem)) {
            processSelection(item);
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
            ((ContactCategoryTreeItem) treeSelectedItem).getContent().removeStyleName("doc-SelectedRow");
        }

        treeSelectedItem = null;
        setSelectedItem(null, true);
    }

    public void clearSelections() {
        if (treeSelectedItem != null) {
            ((ContactCategoryTreeItem) treeSelectedItem).getContent().removeStyleName("doc-SelectedRow");
        }

        treeSelectedItem = null;
        setSelectedItem(null, true);

        for (int i = 0; i < this.getItemCount(); i++) {
            ContactCategoryTreeItem treeItem = (ContactCategoryTreeItem) getItem(i);
            if (treeItem != null) {
                treeItem.renderChildren(treeItem, true, false);
            }
        }
    }

    private void processSelection(TreeItem item) {
        if (treeSelectedItem != null) {
            ((ContactCategoryTreeItem) treeSelectedItem).getContent().removeStyleName("doc-SelectedRow");
            treeSelectedItem = null;
            setSelectedItem(null, true);
        }
        treeSelectedItem = item;
        setSelectedItem(item, true);
        ((ContactCategoryTreeItem) item).getContent().addStyleName("doc-SelectedRow");
    }


    public void onCheckBoxSelected() {
        if (onCheckBoxSelected != null) {
            onCheckBoxSelected.execute();
        }
    }

    public SelectItem[] getSelectedIDs(boolean isAndOperatorUsed) {
        Set<SelectItem> selectedIDs = new HashSet<>();
        for (int i = 0; i < this.getItemCount(); i++) {
            ContactCategoryTreeItem treeItem = (ContactCategoryTreeItem) getItem(i);
            if (treeItem != null) {
                renderSelectedID(treeItem, selectedIDs, isAndOperatorUsed, null);
            }
        }
        selectedIDs.remove(null);
        return selectedIDs.toArray(new SelectItem[]{});
    }

    private void renderSelectedID(ContactCategoryTreeItem treeItem, final Set<SelectItem> selectedIDs, boolean isAndOperatorUsed, SelectItem firstCheckedParentCategory) {
        if (treeItem != null) {
            if (treeItem.isChecked()) {
                SelectItem selectItem = treeItem.getAsSelectedItem();
                if (firstCheckedParentCategory == null) {
                    selectedIDs.add(selectItem);
                    if (isAndOperatorUsed) {
                        firstCheckedParentCategory = selectItem;
                    }
                } else {
                    firstCheckedParentCategory.setDescription((firstCheckedParentCategory.getDescription() == null ? "" : firstCheckedParentCategory.getDescription() + " ") + selectItem.getId());
                }
            }
            if (treeItem.getChildren().size() > 0) {
                for (ContactCategoryTreeItem treeItem_ : treeItem.getChildren()) {
                    renderSelectedID(treeItem_, selectedIDs, isAndOperatorUsed, firstCheckedParentCategory);
                }
            }
        }

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

    public ContextMenu getMenu() {
        return menu;
    }

    public void setMenu(ContextMenu menu) {
        this.menu = menu;
    }

    public Command getMenuGetter() {
        return menuGetter;
    }

    public void setMenuGetter(Command menuGetter) {
        this.menuGetter = menuGetter;
    }

    public Command getOnCheckBoxSelected() {
        return onCheckBoxSelected;
    }

    public void setOnCheckBoxSelected(Command onCheckBoxSelected) {
        this.onCheckBoxSelected = onCheckBoxSelected;
    }
}
