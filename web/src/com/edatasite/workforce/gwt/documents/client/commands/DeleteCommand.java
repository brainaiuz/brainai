package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.documents.client.DeleteFileDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.List;


/**
 * Delete selected object command
 *
 * @author Sherali
 */
public class DeleteCommand implements Command {
    private PopupPanel containerPanel;

    /**
     * @param _containerPanel
     */
    public DeleteCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }
    /* (non-Javadoc)
      * @see com.google.gwt.user.client.Command#execute()
      */

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        displayDelete();
    }

    /**
     * Display the delete dialog, according to the selected object.
     */
    void displayDelete() {
        Object selection = DocumentsView.get().getCurrentSelection();
        if (selection == null) {
            return;
        }
        GWT.log("selection: " + selection.toString(), null);
        if (selection instanceof FileResource || selection instanceof List) {
            DeleteFileDialog dlg = new DeleteFileDialog();
            dlg.center();
        }
//           else if (selection instanceof GroupMemberItem) {
//            DeleteUserDialog dlg = new DeleteUserDialog(newImages);
//            dlg.center();
//        } else if (selection instanceof GroupMembersViewItem) {
//            DeleteGroupDialog dlg = new DeleteGroupDialog(newImages);
//            dlg.center();
//        }
    }
}
