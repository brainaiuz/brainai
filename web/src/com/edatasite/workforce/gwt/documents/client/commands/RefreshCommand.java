package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.List;


/**
 * @author Sherali
 */
public class RefreshCommand implements Command {

    private PopupPanel containerPanel;

    /**
     * @param _containerPanel
     */
    public RefreshCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

    public void execute() {
        containerPanel.hide();
        if (DocumentsView.get().getCurrentSelection() instanceof FileResource || DocumentsView.get().getCurrentSelection() instanceof List) {
            DocumentsView.get().showFileList(true);
        } else if (DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
        } else {
            DnDTreeItem selectedTreeItem = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
            if (selectedTreeItem != null) {
                DocumentsView.get().getFolders().updateFolder(selectedTreeItem);
                DocumentsView.get().showFileList(true);
            }
        }
    }


}
