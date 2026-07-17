package com.edatasite.workforce.gwt.documents.client.tree;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.List;

/**
 * @author Sherali
 */
public class PublicSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    

    private DnDTreeItem rootItem;

    public PublicSubtree(PopupTree aTree) {
        super(aTree);
        updateInit();
    }

    public void updateInit() {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().getPublicFolder(new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource result) {
                rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.myShared(), wfmStrings.pub()), false, tree, true);
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
                    DocumentsView.get().displayError("Unable to fetch Public Root folder");
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    public void update(final DnDTreeItem folderItem) {
        if (folderItem.getFolderResource() != null) {
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getSubFolders(folderItem.getFolderResource().getObjectId(), new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void success(FolderResource result) {
                    List<FolderResource> res = result.getFolders();
                    folderItem.removeItems();
                    for (FolderResource r : res) {
                        DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                        child.setUserObject(r);
                        child.setState(false);
                        child.addItem(new TreeItem());
                    }
                    LoadingPanel.loading(false);
                }
            });
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
                    DocumentsView.get().displayError(wfmStrings.enableToFetchFolder() + folderItem.getFolderResource().getName());
                }

                @Override
                public void success(FolderResource rootResource) {
                    folderItem.updateWidget(DocUtils.imageItemHTML(images.folderYellow(), rootResource.getName()));
                    folderItem.setUserObject(rootResource);
                    update(folderItem);
                    LoadingPanel.loading(false);
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
