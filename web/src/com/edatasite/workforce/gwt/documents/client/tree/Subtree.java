package com.edatasite.workforce.gwt.documents.client.tree;

import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Sherali
 */
public abstract class Subtree {

    protected final DocumentImages.Images images = DocumentImages.get();

    protected PopupTree tree;

    public Subtree(PopupTree aTree) {
        tree = aTree;
    }

    protected TreeItem addImageItem(final TreeItem parent, final String title, final ImageResource imageProto, boolean draggable, boolean hasChild) {
        if (hasChild) {
            return addImageItem(parent, title, imageProto, draggable);
        } else {
            final DnDTreeItem item = new DnDTreeItem(DocUtils.imageItemHTML(imageProto, title), draggable, tree, true);
            item.setStyleName("removeTreeItemPlus");
            parent.addItem(item);
            return item;
        }
    }

    /**
     * A helper method to simplify adding tree items that have attached images.
     * @param parent     the tree item to which the new item will be added.
     * @param title      the text associated with this item.
     * @param imageProto the image of the item
     * @return
     */
    protected TreeItem addImageItem(final TreeItem parent, final String title, final ImageResource imageProto, boolean draggable) {
        final DnDTreeItem item = new DnDTreeItem(DocUtils.imageItemHTML(imageProto, title), draggable, tree, true);
        parent.addItem(item);
        return item;
    }

    public void updateSubFoldersLazily(DnDTreeItem folderItem, List<FolderResource> subfolders, ImageResource image, ImageResource sharedImage) {
        for (int i = 0; i < folderItem.getChildCount(); i++) {
            TreeItem initialItem = folderItem.getChild(i);
            if (initialItem instanceof DnDTreeItem) {
                DnDTreeItem c = (DnDTreeItem) initialItem;
                FolderResource f = (FolderResource) c.getUserObject();
                if (!listContainsFolder(f, subfolders)) {
                    folderItem.removeItem(c);
                }
            } else {
                folderItem.removeItem(initialItem);
            }
        }

        LinkedList<DnDTreeItem> itemList = new LinkedList<>();
        for (FolderResource subfolder : subfolders) {
            DnDTreeItem item = folderItem.getChild(subfolder);
            if (item == null) {
                if (subfolder.isShared()) {
                    item = (DnDTreeItem) addImageItem(folderItem, subfolder.getName(), sharedImage, true);
                } else {
                    item = (DnDTreeItem) addImageItem(folderItem, subfolder.getName(), image, true);
                }
            } else if (subfolder.isShared()) {
                item.updateWidget(DocUtils.imageItemHTML(sharedImage, subfolder.getName()));
            } else {
                item.updateWidget(DocUtils.imageItemHTML(image, subfolder.getName()));
            }
            item.setUserObject(subfolder);
            itemList.add(item);
        }
        for (DnDTreeItem it : itemList) {
            it.remove();
        }
        for (DnDTreeItem it : itemList) {
            folderItem.addItem(it);
        }
    }

    public void selectItem(final DnDTreeItem treeItem) {
        DeferredCommand.addCommand(() -> {
            DocumentsView.get().getFolders().select(treeItem);
            DocumentsView.get().getFolders().getCurrent().setState(true);
        });
    }

    private boolean listContainsFolder(FolderResource folder, List<FolderResource> subfolders) {
        for (FolderResource f : subfolders) {
            if (f.getObjectId().equals(folder.getObjectId())) {
                return true;
            }
        }
        return false;
    }

}
