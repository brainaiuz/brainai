package com.edatasite.workforce.gwt.documents.client.tree;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sherali
 */
public class FolderSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DnDTreeItem rootItem;
    private List<DnDTreeItem> rootItemList;
    private final Integer folderId;

    public FolderSubtree(PopupTree aTree) {
        this(aTree, null);
    }

    public FolderSubtree(PopupTree aTree, Integer folderId) {
        super(aTree);
        aTree.clear();
        this.folderId = folderId;
        fetchRootFolder();
    }

    public void fetchRootFolder() {
        LoadingPanel.loading(true);
        DocumentsService.App.get().getFolders(folderId, new AbstractAsyncCallback<ArrayList<FolderResource>>() {
            @Override
            public void success(ArrayList<FolderResource> rootResources) {
                tree.clear();
                rootItemList = new ArrayList<>();
                int i = 0;
                for (FolderResource rootResource : rootResources) {
                    i++;
                    rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.home(), rootResource.getName()), false, tree, true);
                    rootItem.setUserObject(rootResource);
                    tree.addItem(rootItem);
                    rootItemList.add(rootItem);
                    updateSubFoldersLazily(rootItem, rootResource.getFolders(), images.folderYellow(), images.sharedFolder());
                    rootItem.addItem(new TreeItem());
                    if (i == 0) {
                        selectItem(rootItem);
                    }
                    if (isRootPage()) {
                        tree.setSelectedItem(rootItem);
                        selectItem(rootItem);
                    }
                }
                DocumentsView.get().getFolders().reDraw();
                LoadingPanel.loading(false);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log("Error fetching root folder", throwable);
                DocumentsView.get().displayError("Unable to fetch root folder");
                if (rootItem == null) {
                    rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.home(), "FOLDER"), false, tree);
                    tree.clear();
                    tree.addItem(rootItem);
                }
            }
        });
    }

    private boolean isRootPage() {
        return (!(Window.Location.getHref().contains("sharedByMe") ||
                Window.Location.getHref().contains("sharedWithMe") ||
                Window.Location.getHref().contains("systemFolders")));
    }

    public void updateSubfolders(final DnDTreeItem folderItem) {
        if (!folderItem.getState() || folderItem.getFolderResource() == null) {
            GWT.log("folder resource is null", null);
            return;
        }
        updateNodes(folderItem);
    }

    private void updateNodes(final DnDTreeItem folderItem) {
        DocumentsView.get().showLoadingIndicator();
        DocumentsService.App.get().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource rootResource) {
                DocumentsView.get().hideLoadingIndicator();
                List<FolderResource> res = rootResource.getFolders();
                folderItem.removeItems();
                for (FolderResource r : res) {
                    DnDTreeItem child;
                    if (r.isShared()) {
                        child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.sharedFolder(), true, r.isHasChild());
                    } else {
                        child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                    }

                    child.setUserObject(r);
                    child.setState(false);
                }
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
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
    }

    public void updateFolderAndSubfolders(final DnDTreeItem folderItem) {
        LoadingPanel.loading(true);
        DocumentsService.App.get().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource rootResource) {
                if (!folderItem.equals(rootItem)) {
                    if (rootResource.isShared()) {
                        folderItem.updateWidget(DocUtils.imageItemHTML(images.sharedFolder(), rootResource.getName()));
                    } else {
                        folderItem.updateWidget(DocUtils.imageItemHTML(images.folderYellow(), rootResource.getName()));
                    }
                    folderItem.setUserObject(rootResource);
                } else {
                    folderItem.setUserObject(rootResource);
                    folderItem.updateWidget(DocUtils.imageItemHTML(images.home(), rootResource.getName()));
                }
                updateSubfolders(folderItem);
                LoadingPanel.loading(false);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
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
    }

    /**
     * Retrieve the rootItem.
     *
     * @return the rootItem
     */
    public TreeItem getRootItem() {
        return rootItem;
    }

    /**
     * Retrieve the rootItem list.
     *
     * @return the List<DnDTreeItem> rootItemList
     */
    public List<DnDTreeItem> getRootItemList() {
        return rootItemList;
    }
}
