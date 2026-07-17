package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.documents.client.RenameFolderDialog;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;


/**
 * Display the 'new folder' dialog for creating a new folder.
 *
 * @author Sherali
 */
public class RenameFolderCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupPanel containerPanel;

    /**
     * @param aContainerPanel
     */
    public RenameFolderCommand(PopupPanel aContainerPanel) {
        containerPanel = aContainerPanel;
    }

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        displayNewFolder();
    }

    void displayNewFolder() {
        TreeItem currentFolder = DocumentsView.get().getFolders().getCurrent();
        if (currentFolder == null) {
            DocumentsView.get().displayError(wfmStrings.youHaveToSelectTheParentFolderFirst());
            return;
        }
        RenameFolderDialog dlg = new RenameFolderDialog();
        dlg.center();
    }
}