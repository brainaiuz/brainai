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
import com.edatasite.workforce.gwt.documents.client.rest.resource.SharedResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.List;

/**
 * @author Sherali
 */
public class MyShareSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DnDTreeItem rootItem;

    public MyShareSubtree(PopupTree aTree) {
        super(aTree);
        updateInit();
    }

    public void updateInit() {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().getSharedFolder(new AbstractAsyncCallback<SharedResource>() {
            @Override
            public void success(SharedResource result) {
                rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.myShared(), wfmStrings.sharedByMe()), false, tree, true);
                rootItem.setUserObject(result);
                tree.addItem(rootItem);
                //rootItem.removeItems();
                rootItem.addItem(new TreeItem());
                if (Window.Location.getHref().contains("sharedByMe")) {
                    selectItem(rootItem);
                }
                DocumentsView.get().getFolders().reDraw();
                LoadingPanel.loading(false);
                //update(rootItem);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError("Unable to fetch Shared Root folder");
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    public void update(final DnDTreeItem folderItem) {
        /*if (!folderItem.getState()) {
            //do not update closed treeitem
            return;
        }*/
        if (folderItem.getFolderResource() != null) {
            LoadingPanel.loading(true);
            DocumentsService.App.get().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void success(FolderResource rootResource) {
                    folderItem.removeItems();
                    List<FolderResource> res = rootResource.getFolders();
                    for (FolderResource r : res) {
                        if (r.isShared()) {
                            DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                            child.setUserObject(r);
                            child.setState(false);
                            child.addItem(new TreeItem());
//                                if (folderItem.getState())
//                                    update(child);
                        }
                    }
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
        if (folderItem.getSharedResource() != null) {
            folderItem.removeItems();
            LoadingPanel.loading(true);
            List<FolderResource> res = folderItem.getSharedResource().getSubFolders();
            for (FolderResource r : res) {
                if (!r.getName().contains(wfmStrings.sysTemFolder())) {
                    DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                    child.setUserObject(r);
                    child.setState(false);
//                    update(child);
                    child.addItem(new TreeItem());
                }
            }
            LoadingPanel.loading(false);
        }
    }

    private boolean isRoot(String f, List<String> folders) {
        for (String t : folders) {
            if (!f.equals(t) && f.startsWith(t)) {
                return false;
            }
        }
        return true;
    }

    public void updateFolderAndSubfolders(final DnDTreeItem folderItem) {
        if (folderItem.getFolderResource() != null) {
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        GWT.log("Error fetching folder", e);
                        DocumentsView.get().displayError("Unable to fetch Shared Root folder");
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(e.getMessage());
                    } catch (Throwable e) {
                        DocumentsView.get().displayError(wfmStrings.enableToFetchFolder() + folderItem.getFolderResource().getName());
                    }
                }

                @Override
                public void success(FolderResource rootResource) {
                    if (rootResource.isShared()) {
                        if (!rootResource.getName().contains(wfmStrings.sysTemFolder())) {
                            folderItem.updateWidget(DocUtils.imageItemHTML(images.folderYellow(), rootResource.getName()));
                            folderItem.setUserObject(rootResource);
                            update(folderItem);
                        }
                    } else {
                        folderItem.getParentItem().removeItem(folderItem);
                    }
                    LoadingPanel.loading(false);
                }
            });
        } else if (folderItem.getSharedResource() != null) {
            folderItem.removeItems();
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getSharedFolder(new AbstractAsyncCallback<SharedResource>() {
                @Override
                public void success(SharedResource result) {
                    List<FolderResource> res = result.getSubFolders();
                    for (FolderResource r : res) {
                        DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true);
                        child.setUserObject(r);
                        child.setState(false);
//                        folderItem.addItem(new TreeItem());
                        update(child);
                    }
                    LoadingPanel.loading(false);
//                    rootItem.setUserObject(result);
//                    if (rootItem.getState()) {
//                        rootItem.removeItems();
//                            update(rootItem);
//                    }
//                    folderItem.addItem(new TreeItem());
                }

                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError("Unable to fetch Shared Root folder");
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }
            });
        }
    }

    /**
     * Retrieve the rootItem.
     *
     * @return the rootItem
     */
    public TreeItem getRootItem() {
        return rootItem;
    }

    public void updateNode(TreeItem node, FolderResource folder) {
        node.getWidget().removeStyleName("doc-SelectedRow");
        if (node instanceof DnDTreeItem) {
            ((DnDTreeItem) node).updateWidget(DocUtils.imageItemHTML(images.folderYellow(), folder.getName()));
        } else {
            node.setWidget(DocUtils.imageItemHTML(images.folderYellow(), folder.getName()));
        }
        node.setUserObject(folder);
    }
}
