package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid
 * Date: Aug 28, 2010
 * Time: 12:10:55 PM
 */
public class DeleteFolderCommand implements Command {
    private PopupPanel containerPanel;

    public DeleteFolderCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }
    /* (non-Javadoc)
    * @see com.google.gwt.user.client.Command#execute()
    */

    @Override
    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        TreeItem folder = DocumentsView.get().getFolders().getCurrent();
        if (folder == null) {
            return;
        }
        DocumentsView.get().getFolders().select(folder);
        GWT.log("selection: " + folder.toString(), null);
        DeleteFolderDialog dlg = new DeleteFolderDialog();
        dlg.center();

    }
}
