package com.edatasite.workforce.gwt.documents.client.tree;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SystemResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 27.05.2010
 * Time: 14:41:48
 * To change this template use File | Settings | File Templates.
 */
public class SystemSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DnDTreeItem rootItem;

    public SystemSubtree(PopupTree aTree) {
        super(aTree);
        updateInit();
    }

    private void updateInit() {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().getSystemFolder(new AbstractAsyncCallback<SystemResource>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                DocumentsView.get().displayError("Unable to fetch System folder");
            }

            @Override
            public void success(SystemResource result) {
                rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.systemFolder(), result.getName()), false, tree, true);
                rootItem.setUserObject(result);
                tree.addItem(rootItem);
                if (Window.Location.getHref().contains("systemFolders")) {
                    selectItem(rootItem);
                }
                DocumentsView.get().getFolders().reDraw();
                LoadingPanel.loading(false);
            }
        });
    }

    public void update(final DnDTreeItem folderItem) {
        if (folderItem.getFolderResource() != null) {
            folderItem.removeItems();
            List<FolderResource> res = folderItem.getFolderResource().getFolders();
            for (FolderResource r : res) {

                DnDTreeItem child = (DnDTreeItem) addImageItem(folderItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                child.setUserObject(r);
                child.setState(false);
                child.addItem(new TreeItem());
            }
        }
        if (folderItem.getSystemResource() != null) {
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().getSystemSubFolders(folderItem.getSystemResource().getObjectId(), new AbstractAsyncCallback<ArrayList<FolderResource>>() {
                @Override
                public void success(ArrayList<FolderResource> result) {
                    folderItem.removeItems();
                    folderItem.getSystemResource().setSubFolders(result);
                    for (FolderResource r : result) {
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
}