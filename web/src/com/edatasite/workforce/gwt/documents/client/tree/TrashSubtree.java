package com.edatasite.workforce.gwt.documents.client.tree;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.List;

/**
 * @author Sherali
 */
public class TrashSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * A constant that denotes the completion of an IncrementalCommand.
     */
    private DnDTreeItem rootItem;

    public TrashSubtree(PopupTree aTree) {
        super(aTree);
        updateInit();
    }

    public void updateInit() {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().getTrashedFolder(new AbstractAsyncCallback<TrashResource>() {
            @Override
            public void failure(Throwable throwable) {
                if (rootItem == null) {
                    rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.trash(), wfmStrings.trashBin()), false, tree);
                    tree.addItem(rootItem);
                }
                rootItem.setUserObject(new TrashResource());
                DocumentsView.get().getFolders().reDraw();
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TrashResource result) {
                LoadingPanel.loading(false);
                if (rootItem == null) {
                    rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.trash(), wfmStrings.trashBin()), false, tree);
                    tree.addItem(rootItem);
                }
                rootItem.setUserObject(result);
                rootItem.removeItems();
                List<FolderResource> res = rootItem.getTrashResource().getTrashedFolders();
                for (FolderResource r : res) {
                    DnDTreeItem child = (DnDTreeItem) addImageItem(rootItem, r.getName(), images.folderYellow(), true, r.isHasChild());
                    child.setUserObject(r);
                    child.setState(false);
                }
                DocumentsView.get().getFolders().reDraw();

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

}
