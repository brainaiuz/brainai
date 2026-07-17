package com.edatasite.workforce.gwt.documents.client.dnd;

import com.edatasite.workforce.gwt.documents.client.Folders;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.rest.resource.*;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Sherali
 */
public class DnDTreeItem extends TreeItem implements HasAllMouseHandlers {
    public static final int FOLDER = 0;
    public static final int SHARED = 1;
    public static final int TRASH = 2;
    public static final int OTHERS = 3;
    public static final int SYSTEM = 4;

    private DnDFocusPanel focus;
    private Widget content;
    private boolean draggable = false;
    PopupTree tree;
    public boolean lazy = false;

    public DnDTreeItem(Widget widget, boolean _draggable, PopupTree atree, boolean _lazy) {
        this(widget, _draggable, atree);
        lazy = _lazy;
        if (lazy) {
            addItem(new TreeItem());
        }
    }

    public DnDTreeItem(Widget widget, boolean _draggable, PopupTree atree) {
        super();
        tree = atree;
        draggable = _draggable;
        content = widget;
        focus = new DnDFocusPanel(content, this);
        focus.setTabIndex(-1);
        setWidget(focus);
    }

    public void setFocus() {
        ((DnDFocusPanel) getWidget()).setFocus(true);
    }

    public void updateWidget(Widget widget) {
        content = widget;
        focus.setWidget(content);

    }

    public PopupTree getPopupTree() {
        return tree;
    }

    public Widget getContent() {
        return content;
    }


    /* (non-Javadoc)
      * @see com.google.gwt.user.client.ui.TreeItem#removeItems()
      */

    @Override
    public void removeItems() {
        removeItems(this);
        super.removeItems();

    }

    /* (non-Javadoc)
      * @see com.google.gwt.user.client.ui.TreeItem#removeItem(com.google.gwt.user.client.ui.TreeItem)
      */

    @Override
    public void removeItem(TreeItem item) {
        item.removeItems();
        super.removeItem(item);

    }

    protected void removeItems(TreeItem item) {
        for (int i = 0; i < item.getChildCount(); i++) {
            TreeItem it = item.getChild(i);
            removeItems(it);
        }
    }

    /**
     * Retrieve the focus.
     *
     * @return the focus
     */
    public DnDFocusPanel getFocus() {
        return focus;
    }

    public DnDTreeItem getChild(FolderResource folder) {
        for (int i = 0; i < getChildCount(); i++) {
            DnDTreeItem c = (DnDTreeItem) getChild(i);
            if (c.getUserObject() instanceof FolderResource || c.getUserObject() instanceof OtherUserResource) {
                if (((FolderResource) c.getUserObject()).getObjectId().equals(folder.getObjectId())) {
                    return c;
                }
            }
        }
        return null;
    }

    public DnDTreeItem getChild(OtherUserResource user) {
        for (int i = 0; i < getChildCount(); i++) {
            DnDTreeItem c = (DnDTreeItem) getChild(i);
            if (c.getUserObject() instanceof OtherUserResource) {
                if (((OtherUserResource) c.getUserObject()).getObjectId().equals(user.getObjectId())) {
                    return c;
                }
            }
        }
        return null;
    }

    public void insertItem(TreeItem item, int position) {
        addItem(item);
        //if(position != 0)
        DOM.insertChild(getElement(), item.getElement(), position);
    }


    public int getItemType() {
        Folders f = DocumentsView.get().getFolders();
        if (f.isFileItem(this)) {
            return FOLDER;
        }
        if (f.isMySharedItem(this)) {
            return SHARED;
        }
        if (f.isOthersSharedItem(this)) {
            return OTHERS;
        }
        if (f.isSystemItem(this)) {
            return SYSTEM;
        }
        return TRASH;
    }

    /**
     * Retrieve the systemResource.
     *
     * @return the systemResource
     */
    public SystemResource getSystemResource() {
        if (getUserObject() instanceof SystemResource) {
            return (SystemResource) getUserObject();
        }
        return null;
    }

    /**
     * Retrieve the folderResource.
     *
     * @return the folderResource
     */
    public FolderResource getFolderResource() {
        if (getUserObject() instanceof FolderResource) {
            return (FolderResource) getUserObject();
        }
        return null;
    }


    /**
     * Retrieve the sharedResource.
     *
     * @return the sharedResource
     */
    public SharedResource getSharedResource() {
        if (getUserObject() instanceof SharedResource) {
            return (SharedResource) getUserObject();
        }
        return null;
    }


    /**
     * Retrieve the trashResource.
     *
     * @return the trashResource
     */
    public TrashResource getTrashResource() {
        if (getUserObject() instanceof TrashResource) {
            return (TrashResource) getUserObject();
        }
        return null;
    }


    /**
     * Retrieve the othersResource.
     *
     * @return the othersResource
     */
    public OthersResource getOthersResource() {
        if (getUserObject() instanceof OthersResource) {
            return (OthersResource) getUserObject();
        }
        return null;
    }

    /**
     * Retrieve the allFilesResource.
     *
     * @return the allFilesResource
     */

    public AllFilesResource getAllFilesResource() {
        if (getUserObject() instanceof AllFilesResource) {
            return (AllFilesResource) getUserObject();
        }
        return null;
    }

    /**
     * Retrieve the otherUserResource.
     *
     * @return the otherUserResource
     */
    public OtherUserResource getOtherUserResource() {
        if (getUserObject() instanceof OtherUserResource) {
            return (OtherUserResource) getUserObject();
        }
        return null;
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

}
