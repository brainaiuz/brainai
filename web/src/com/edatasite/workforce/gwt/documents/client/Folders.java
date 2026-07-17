package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.RestResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.tree.*;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree displaying the folders in the user's file space.
 */
public class Folders extends Composite {

    /**
     * A constant that denotes the completion of an IncrementalCommand.
     */
    public static final boolean DONE = false;

    /**
     * The tree widget that displays the folder namespace.
     */
    private PopupTree tree;

    /**
     * A cached copy of the currently selected folder widget.
     */
    private SelectAllSubtree selectAllSubtree;

    private SystemSubtree systemSubtree;

    private FolderSubtree folderSubtree;

    private TrashSubtree trashSubtree;

    private MyShareSubtree myShareSubtree;

    private PublicSubtree publicSubtree;

    private OthersSharesSubtree othersSharesSubtree;


    private DnDTreeItem currentTreeItem;
    private final DocumentImages.Images images = DocumentImages.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * default folder id
     */
    private Integer folderId;

    boolean isSystemFolder;

    /**
     * Constructs a new folders widget with a bundle of images.
     */
    public Folders(Integer folderId, boolean isSystemFolder) {


        this.isSystemFolder = isSystemFolder;
        tree = new PopupTree();
        tree.setAnimationEnabled(true);
        initWidget(tree);


        folderSubtree = new FolderSubtree(tree, folderId);
        //Customise for Robert companys
        if (folderId == null) {
            boolean isClient = Utils.hasRole(Constants.CLIENT);
            if (!isClient) {
                systemSubtree = new SystemSubtree(tree);
                publicSubtree = new PublicSubtree(tree);
            }
            selectAllSubtree = new SelectAllSubtree(tree);
            if (!isClient) {
                myShareSubtree = new MyShareSubtree(tree);
            }
            othersSharesSubtree = new OthersSharesSubtree(tree);

            trashSubtree = new TrashSubtree(tree);
        }
    }

    public synchronized void reDraw() {
        tree.clear();

        if (selectAllSubtree != null && selectAllSubtree.getRootItem() != null) {
            tree.addItem(selectAllSubtree.getRootItem());
        }

        if (folderSubtree != null && folderSubtree.getRootItemList() != null) {
//           tree.addItem(folderSubtree.getRootItem());
            for (TreeItem item : folderSubtree.getRootItemList()) {
                tree.addItem(item);
            }
        }
        if (systemSubtree != null && systemSubtree.getRootItem() != null) {
            tree.addItem(systemSubtree.getRootItem());
        }
        if (publicSubtree != null && publicSubtree.getRootItem() != null) {
            tree.addItem(publicSubtree.getRootItem());
        }
        if (myShareSubtree != null && myShareSubtree.getRootItem() != null) {
            tree.addItem(myShareSubtree.getRootItem());
        }
        if (othersSharesSubtree != null && othersSharesSubtree.getRootItem() != null) {
            tree.addItem(othersSharesSubtree.getRootItem());
        }
        if (trashSubtree != null && trashSubtree.getRootItem() != null) {
            tree.addItem(trashSubtree.getRootItem());
        }
    }

    public void select(TreeItem item) {
        tree.processItemSelected(item, true);
    }

    public void clearSelection() {
        tree.clearSelection();
    }

    public void update(TreeItem item) {
        if (isFileItem(item) && !isPublicItem(item)) {
            folderSubtree.updateSubfolders((DnDTreeItem) item);
        } else if (isTrash(item)) {
            trashSubtree.updateInit();
        } else if (isMySharedItem(item)) {
            myShareSubtree.update((DnDTreeItem) item);
        } else if (isPublicItem(item)) {
            publicSubtree.update((DnDTreeItem) item);
        } else if (isOthersSharedItem(item)) {
            othersSharesSubtree.update((DnDTreeItem) item);
        } else if (isSystem(item)) {
            systemSubtree.update((DnDTreeItem) item);
        } else if (isSystemItem(item)) {
            systemSubtree.updateFolderAndSubfolders((DnDTreeItem) item);
        } else {
            folderSubtree.updateSubfolders((DnDTreeItem) item);
        }
    }

    public void updateFolder(final DnDTreeItem folderItem) {
        if (isFileItem(folderItem)) {
            folderSubtree.updateFolderAndSubfolders(folderItem);
            if (myShareSubtree != null) {
                myShareSubtree.updateFolderAndSubfolders((DnDTreeItem) getMySharesItem());
            }
            if (publicSubtree != null) {
                publicSubtree.updateFolderAndSubfolders((DnDTreeItem) getPublicItem());
            }
        } else if (isMySharedItem(folderItem) || isPublicItem(folderItem)) {
            myShareSubtree.updateFolderAndSubfolders(folderItem);
            publicSubtree.updateFolderAndSubfolders(folderItem);
            if (folderItem.getFolderResource() != null) {
                DnDTreeItem fitem = (DnDTreeItem) getUserItem(getRootItem(), folderItem.getFolderResource().getObjectId());
                if (fitem != null) {
                    folderSubtree.updateFolderAndSubfolders(fitem);
                } else {
                    folderSubtree.updateFolderAndSubfolders((DnDTreeItem) getRootItem());
                }
            }

        } else if (isTrashItem(folderItem)) {
            trashSubtree.updateInit();
        } else if (isSystem(folderItem)) {
            systemSubtree.update(folderItem);
        } else if (isSystemItem(folderItem)) {
            systemSubtree.updateFolderAndSubfolders(folderItem);
        } else if (isOthersSharedItem(folderItem)) {
//            othersSharesSubtree.update(folderItem);
            othersSharesSubtree.updateFolderAndSubfolders(folderItem);
        }
    }


    public boolean setCurrentTreeItem(FolderResource folderResource, boolean changeTreeItemState, boolean isParentFolder) {
        if (folderResource != null) {

            DnDTreeItem olderTreeItem = (DnDTreeItem) getCurrent();
            DnDTreeItem newTreeItem = null;
            Integer parentFolderResourceID = null;
            if (olderTreeItem != null && olderTreeItem.getParentItem() != null && ((DnDTreeItem) olderTreeItem.getParentItem()).getFolderResource() != null) {
                parentFolderResourceID = ((DnDTreeItem) olderTreeItem.getParentItem()).getFolderResource().getObjectId();
            }
            if (currentTreeItem == null || parentFolderResourceID == null || !currentTreeItem.getFolderResource().getObjectId().equals(parentFolderResourceID)) {
                currentTreeItem = olderTreeItem;
            }

            if (isParentFolder) {
                newTreeItem = (DnDTreeItem) currentTreeItem.getParentItem();
                currentTreeItem = newTreeItem;
            } else if (changeTreeItemState) {
                newTreeItem = currentTreeItem.getChild(folderResource);
                currentTreeItem = newTreeItem;
            } else {
                updateFolder(currentTreeItem);
                newTreeItem = currentTreeItem.getChild(folderResource);
            }
            setCurrent(newTreeItem);

            if (changeTreeItemState) {
                tree.setSelectedItem(newTreeItem, true);
                if (isParentFolder) {
                    olderTreeItem.setState(false, true);
                } else {
                    newTreeItem.setState(true, true);
                }
            }

            return true;
        }
        return false;
    }

    public boolean setCurrentFolder(final FolderResource folderResource, final Anchor arrow) {
        if (folderResource != null) {
            DnDTreeItem olderTreeItem = (DnDTreeItem) getCurrent();
            Integer parentFolderResourceID = null;
            if (olderTreeItem.getParentItem() != null && ((DnDTreeItem) olderTreeItem.getParentItem()).getFolderResource() != null) {
                parentFolderResourceID = ((DnDTreeItem) olderTreeItem.getParentItem()).getFolderResource().getObjectId();
            }
            if (currentTreeItem == null || (parentFolderResourceID == null || !currentTreeItem.getFolderResource().getObjectId().equals(parentFolderResourceID))) {
                currentTreeItem = olderTreeItem;
            }
            if (currentTreeItem.getChildCount() == 0 || (currentTreeItem.getChildCount() == 1 && !(currentTreeItem.getChild(0) instanceof DnDTreeItem))) {
                DocumentsView.get().showLoadingIndicator();
                final DnDTreeItem folderItem = (DnDTreeItem) getCurrent();
                DocumentsService.App.get().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                    @Override
                    public void success(FolderResource rootResource) {
                        DocumentsView.get().hideLoadingIndicator();
                        ArrayList<FolderResource> res = rootResource.getFolders();
                        folderItem.getFolderResource().setFolders(res);
                        folderSubtree.updateSubFoldersLazily(folderItem, res, images.folderYellow(), images.sharedFolder());
                        for (int i = 0; i < folderItem.getChildCount(); i++) {
                            DnDTreeItem anItem = (DnDTreeItem) folderItem.getChild(i);
                            folderSubtree.updateSubFoldersLazily(anItem, anItem.getFolderResource().getFolders(), images.folderYellow(), images.sharedFolder());
                            anItem.addItem(new TreeItem());
                            anItem.setState(false);
                        }
                        DnDTreeItem selectedFolderTreeItem = currentTreeItem.getChild(folderResource);
                        if (selectedFolderTreeItem == null) {
                            setCurrent(currentTreeItem);
                        } else {
                            setCurrent(selectedFolderTreeItem);
                        }
                        DocumentsView.get().setCurrentSelection(folderResource);
                        new FolderContextMenu().showPopup(arrow.getAbsoluteLeft() + 15, arrow.getAbsoluteTop() + 2);
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        DocumentsView.get().hideLoadingIndicator();
                        try {
                            throw throwable;
                        } catch (ObjectNotFoundException e) {
                            GWT.log("Error fetching folder", e);
                            DocumentsView.get().displayError(wfmStrings.enableToFetchFolder() + folderItem.getFolderResource().getName());
                        } catch (InsufficientPermissionsException e) {
                            e.printStackTrace();
                        } catch (Throwable e) {
                            // last resort  a very unexpected exception
                        }
                    }
                });

            } else {
                Integer folderID = null;
                if (getCurrent().getUserObject() instanceof FolderResource) {
                    folderID = ((DnDTreeItem) getCurrent()).getFolderResource().getObjectId();
                } else if (getCurrent().getUserObject() instanceof TrashResource) {
                    folderID = ((DnDTreeItem) getCurrent()).getTrashResource().getObjectId();
                }
                if (folderID != null && !folderID.equals(folderResource.getObjectId())) {
                    DnDTreeItem selectedFolderTreeItem = currentTreeItem.getChild(folderResource);
                    setCurrent(selectedFolderTreeItem);
                }
                DocumentsView.get().setCurrentSelection(folderResource);
                new FolderContextMenu().showPopup(arrow.getAbsoluteLeft() + 15, arrow.getAbsoluteTop() + 2);
            }

            return true;
        }
        return false;
    }


    /**
     * Retrieve the current.
     *
     * @return the current
     */
    public TreeItem getCurrent() {
        return tree.getTreeSelectedItem();
    }

    /**
     * Modify the current.
     *
     * @param _current the current to set
     */
    public void setCurrent(final TreeItem _current) {
        tree.setTreeSelectedItem(_current);
    }

    public void setCurrentTreeItem(DnDTreeItem currentTreeItem) {
        this.currentTreeItem = currentTreeItem;
    }

    public DnDTreeItem getCurrentTreeItem() {
        return currentTreeItem;
    }

    /**
     * Checks whether a TreeItem is contained in the root folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isSystemItem(TreeItem item) {
        return item != null && (isSystemFolder || getRootOfItem(item).equals(getSystemItem()));
    }

    /**
     * Checks whether a TreeItem is contained in the root folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isFileItem(TreeItem item) {
        return item != null && (getRootOfItem(item).equals(getRootItem()) || getRootOfItem(item).equals(getPublicItem()));
    }

    /**
     * For defining default folders which are: "Shared By Me, Shared With Me, Sub/System Folders , Trash"
     */
    public boolean isDefaultFolders() {
        final Folders folders = DocumentsView.get().getFolders();
        final TreeItem selection = folders.getCurrent();
        return selection == null || folders.isMyShares(selection) || folders.isSystem(selection) ||
                folders.isTrash(selection) || folders.isOthersShared(selection) || folders.isSystemItem(selection) /*|| folders.isPublic(selection)*/;
    }

    /**
     * Checks whether a TreeItem is contained in the trash folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isTrashItem(TreeItem item) {
        return item != null && getRootOfItem(item).equals(getTrashItem());
    }

    /**
     * Checks whether a TreeItem is contained in the trash folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isOthersSharedItem(TreeItem item) {
        return item != null && getRootOfItem(item).equals(getSharesItem());
    }

    /**
     * Checks whether a TreeItem is contained in the trash folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isMySharedItem(TreeItem item) {
        return item != null && getRootOfItem(item).equals(getMySharesItem());
    }

    /**
     * Checks whether a TreeItem is contained in the trash folder structure
     *
     * @param item The TreeItem to check
     */
    public boolean isPublicItem(TreeItem item) {
        return item != null && item.equals(getPublicItem());
    }

    private TreeItem getRootOfItem(TreeItem item) {
        if (item.getParentItem() == null) {
            return item;
        }
        TreeItem toCheck = item;
        while (toCheck.getParentItem() != null) {
            toCheck = toCheck.getParentItem();
            toCheck = getRootOfItem(toCheck);
        }
        return toCheck;
    }

    public TreeItem getUserOfSharedItem(TreeItem item) {
        if (item.getUserObject() instanceof OtherUserResource) {
            return item;
        }
        TreeItem test = item;
        while (test.getParentItem() != null) {
            test = test.getParentItem();
            if (test.getUserObject() instanceof OtherUserResource) {
                return test;
            }
        }
        return null;
    }

    public boolean isSystem(TreeItem item) {
        return item.equals(getSystemItem());
    }

    public boolean isTrash(TreeItem item) {
        return item.equals(getTrashItem());
    }

    public boolean isMyShares(TreeItem item) {
        return item.equals(getMySharesItem());
    }

    public boolean isPublic(TreeItem item) {
        return item.equals(getPublicItem());
    }

    public boolean isOthersShared(TreeItem item) {
        return item.equals(getSharesItem());
    }

    /*
      * Returns the Tree Item corresponding to the FolderResource object
      * since we need to update main file structure for untrashed folders
      */

    public TreeItem getUserItem(FolderResource folder) {
        return getUserItem(getRootItem(), folder);
    }

    public TreeItem getOtherSharedItem(FolderResource folder) {
        return getUserItem(getSharesItem(), folder);
    }

    private TreeItem getUserItem(TreeItem parent, FolderResource folder) {
        TreeItem tmp = null;
        if (parent.getUserObject() instanceof FolderResource &&
                (parent.getUserObject().equals(folder) ||
                        ((FolderResource) parent.getUserObject()).getObjectId().equals(folder.getObjectId()))) {
            return parent;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            TreeItem child = parent.getChild(i);
            if (child.getUserObject() instanceof FolderResource) {
                FolderResource dto = (FolderResource) child.getUserObject();
                if (dto.equals(folder) || dto.getObjectId().equals(folder.getObjectId())) {
                    return child;
                }
            }
            tmp = getUserItem(child, folder);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * Retrieve the trashItem.
     *
     * @return the trashItem
     */
    public TreeItem getTrashItem() {
        if (trashSubtree == null) {
            return null;
        }
        return trashSubtree.getRootItem();
    }

    /**
     * Retrieve the systemItem.
     *
     * @return the systemItem
     */
    public TreeItem getSystemItem() {
        if (systemSubtree != null) {
            return systemSubtree.getRootItem();
        }
        return null;
    }

    /**
     * Retrieve the rootItem.
     *
     * @return the rootItem
     */
    public TreeItem getRootItem() {
        if (folderSubtree == null) {
            return null;
        }
        return folderSubtree.getRootItem();
    }

    /**
     * Retrieve the mySharesItem.
     *
     * @return the mySharesItem
     */
    public TreeItem getMySharesItem() {
        if (myShareSubtree == null) {
            return null;
        }
        return myShareSubtree.getRootItem();
    }

    /**
     * Retrieve the mySharesItem.
     *
     * @return the mySharesItem
     */
    public TreeItem getPublicItem() {
        if (publicSubtree == null) {
            return null;
        }
        return publicSubtree.getRootItem();
    }

    /**
     * Retrieve the sharesItem.
     *
     * @return the sharesItem
     */
    public TreeItem getSharesItem() {
        if (othersSharesSubtree == null) {
            return null;
        }
        return othersSharesSubtree.getRootItem();
    }

    public void onFolderTrash(TreeItem folder) {
        if (folder.getParentItem().getUserObject() instanceof FolderResource) {
            FolderResource folderDTO = (FolderResource) folder.getParentItem().getUserObject();
            updateFileAndShareNodes(folderDTO);
        } else {
            update(getMySharesItem());
        }
        update(getTrashItem());
        clearSelection();
        DocumentsView.get().updateFileCache(false, true /*clear selection*/);
    }

    public void onFolderDelete(TreeItem folder) {
        if (folder.getParentItem().getUserObject() instanceof FolderResource) {
            FolderResource folderDTO = (FolderResource) folder.getParentItem().getUserObject();
            updateFileAndShareNodes(folderDTO);
        } else {
            update(getMySharesItem());
        }
//        DocumentsView.get().getStatusPanel().updateStats();
        clearSelection();
        DocumentsView.get().updateFileCache(true, true /*clear selection*/);
    }

    public void onFolderCopy(TreeItem folder) {
        if (!updateFileAndShareNodes((FolderResource) folder.getUserObject())) {
            update(folder);
        }
        DocumentsView.get().updateFileCache(true, true /*clear selection*/);
//        DocumentsView.get().getStatusPanel().updateStats();
    }

    public void onFolderMove(TreeItem folder, FolderResource initialParent) {
        updateFileAndShareNodes(initialParent);
        updateFileAndShareNodes((FolderResource) folder.getUserObject());
        update(folder);
        DocumentsView.get().updateFileCache(true, true /*clear selection*/);
//        DocumentsView.get().getStatusPanel().updateStats();
        clearSelection();
    }

    private boolean updateFileAndShareNodes(FolderResource folder) {
        boolean updated = false;
        TreeItem sharesFolder = getUserItem(getMySharesItem(), folder);
        if (sharesFolder != null) {
            update(sharesFolder);
            updated = true;
        }
        TreeItem fileFolder = getUserItem(getRootItem(), folder);
        if (fileFolder != null) {
            update(fileFolder);
            updated = true;
        }
        return updated;
    }

    public void initialize() {
        DeferredCommand.addCommand(() -> {
            DocumentsView.get().showLoadingIndicator();
            folderSubtree.getRootItem().removeItems();
            trashSubtree.getRootItem().removeItems();
            myShareSubtree.getRootItem().removeItems();
            othersSharesSubtree.getRootItem().removeItems();

            systemSubtree.getRootItem().removeItems();
            update(systemSubtree.getRootItem());

            update(publicSubtree.getRootItem());
            update(folderSubtree.getRootItem());
            update(trashSubtree.getRootItem());
            update(myShareSubtree.getRootItem());
            update(othersSharesSubtree.getRootItem());
            DocumentsView.get().setCurrentSelection(null);
            clearSelection();
            DocumentsView.get().updateFileCache(true, true /*clear selection*/);
            DocumentsView.get().hideLoadingIndicator();
        });
    }

    /* NEW HANDLING METHODS */

    public TreeItem getUserItem(TreeItem parent, Integer objectId) {
        TreeItem tmp = null;
        if (parent.getUserObject() instanceof RestResource && ((RestResource) parent.getUserObject()).getObjectId() != null && ((RestResource) parent.getUserObject()).getObjectId().equals(objectId)) {
            return parent;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            TreeItem child = parent.getChild(i);
            if (child.getUserObject() instanceof RestResource) {
                RestResource dto = (RestResource) child.getUserObject();
                if (dto.getObjectId().equals(objectId)) {
                    return child;
                }
            }
            tmp = getUserItem(child, objectId);
            if (tmp != null) {
                return tmp;
            }
        }
        return null;
    }

    public List<TreeItem> getItemsOfTreeForPath(Integer objectId) {
        List<TreeItem> result = new ArrayList<>();
        TreeItem item = null;
        item = getUserItem(getRootItem(), objectId);
        if (item != null) {
            result.add(item);
        }
        item = getUserItem(getSystemItem(), objectId);
        if (item != null) {
            result.add(item);
        }
        item = getUserItem(getMySharesItem(), objectId);
        if (item != null) {
            result.add(item);
        }
        item = getUserItem(getTrashItem(), objectId);
        if (item != null) {
            result.add(item);
        }
        item = getUserItem(getSharesItem(), objectId);
        if (item != null) {
            result.add(item);
        }
        return result;
    }

    public void selectAll() {
        select(selectAllSubtree.getRootItem());
    }

    public void selectPublic() {
        select(publicSubtree.getRootItem());
    }

    public void selectSharedByMe() {
        select(myShareSubtree.getRootItem());
    }

    public void selectSharedWithOthers() {
      othersSharesSubtree.updateInit();
    }

    public void onSelectSharedWithOthers() {
        select(othersSharesSubtree.getRootItem());
    }

    public void selectTrash() {
        select(trashSubtree.getRootItem());
    }

    public void selectMyFolders() {
        select(folderSubtree.getRootItem());
    }

    public void selectSystemFolders() {
        select(systemSubtree.getRootItem());
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        DeferredCommand.addCommand(() -> {
            if (folderSubtree.getRootItem() != null) {
                folderSubtree.getRootItem().setSelected(true);
                return false;
            } else {
                return true;
            }
        });
    }



}
