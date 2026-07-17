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
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OthersResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.List;

/**
 * @author Sherali
 */
public class OthersSharesSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DnDTreeItem rootItem;

    public OthersSharesSubtree(PopupTree aTree) {
        super(aTree);
//        updateInit();
    }

    public void updateInit() {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().getOthersShared(new AbstractAsyncCallback<OthersResource>() {
            @Override
            public void success(OthersResource result) {
                rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.othersShared(), wfmStrings.sharedWithMe()), false, tree, true);
                rootItem.setUserObject(result);
                tree.addItem(rootItem);
                if (Window.Location.getHref().contains("sharedWithMe")) {
                    selectItem(rootItem);
                }
                DocumentsView.get().getFolders().onSelectSharedWithOthers();
                LoadingPanel.loading(false);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError("Unable to fetch Others Root folder");
                    if (rootItem != null) {
                        rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.othersShared(), "ERROR"), false, tree);
                        tree.addItem(rootItem);
                        DocumentsView.get().getFolders().reDraw();
                    }
                } catch (Throwable e) {
                    // last resort � a very unexpected exception
                }
            }
        });
    }

    public void update(final DnDTreeItem folderItem) {
       /* if (!folderItem.getState()) {
            //do not update sub folders
            return;
        }*/
        if (folderItem.getOthersResource() != null) {
            LoadingPanel.loading(true);
            OthersResource othersResource = (OthersResource) rootItem.getUserObject();
            List<OtherUserResource> res = othersResource.getOtherUsers();
            folderItem.removeItems();
            for (OtherUserResource r : res) {
                DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem,
                                                               r.getUsername(), images.folderYellow(), true);
                GWT.log("Setting username:" + r.getUsername(), null);
                child.setUserObject(r.getFolders().get(0));
                child.setState(false);
                child.addItem(new TreeItem());
            }
            LoadingPanel.loading(false);
        }
        if (folderItem.getOtherUserResource() != null) {
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getOtherUserResource(folderItem.getOtherUserResource().getObjectId(), new AbstractAsyncCallback<OtherUserResource>() {
                @Override
                public void success(OtherUserResource result) {
                    folderItem.removeItems();
                    for (FolderResource r : result.getFolders()) {
                        DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                        child.setUserObject(r);
                        updateFolderAndSubfolders(child);
                    }
                    LoadingPanel.loading(false);
                }

                @Override
                public void failure(Throwable throwable) {
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError("Unable to fetch Others Root folder");
                    } catch (Throwable e) {
                        // last resort � a very unexpected exception
                    }
                }
            });


        }
        if (folderItem.getFolderResource() != null) {
            GWT.log("UPDATING :" + folderItem.getFolderResource().getName(), null);
            LoadingPanel.loading(true);
            DocumentsService.App.get().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void success(FolderResource rootResource) {
                    List<FolderResource> res = rootResource.getFolders();
                    folderItem.removeItems();
                    GWT.log("UPDATING :" + folderItem.getFolderResource().getName() + " :" + res.size(), null);
                    for (FolderResource r : res) {
                        DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                        child.setUserObject(r);
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
                        // last resort � a very unexpected exception
                    }
                }
            });
        }
    }

    public void updateFolderAndSubfolders(final DnDTreeItem folderItem) {
        if (folderItem.getFolderResource() != null) {
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    DocumentsView.get().displayError(wfmStrings.enableToFetchFolder() + folderItem.getFolderResource().getName());
                }

                @Override
                public void success(FolderResource rootResource) {
                    folderItem.updateWidget(DocUtils.imageItemHTML(images.folderYellow(), rootResource.getName()));
                    folderItem.setUserObject(rootResource);
                    LoadingPanel.loading(false);
                }
            });
        } else if (folderItem.getOthersResource() != null) {
            update(folderItem);
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


}
