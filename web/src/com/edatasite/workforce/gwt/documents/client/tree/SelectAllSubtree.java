package com.edatasite.workforce.gwt.documents.client.tree;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.PopupTree;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.AllFilesResource;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 06.07.12
 * Time: 12:30
 * To change this template use File | Settings | File Templates.
 */
public class SelectAllSubtree extends Subtree {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private DnDTreeItem rootItem;

    public SelectAllSubtree(PopupTree aTree) {
        super(aTree);
        updateInit();
    }

    private void updateInit() {
        update();
    }

    public void update() {
        LoadingPanel.loading(true);
        rootItem = new DnDTreeItem(DocUtils.imageItemHTML(images.totalFiles(), wfmStrings.allFiles()), false, tree);

        rootItem.setUserObject(new AllFilesResource());
        tree.addItem(rootItem);
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
