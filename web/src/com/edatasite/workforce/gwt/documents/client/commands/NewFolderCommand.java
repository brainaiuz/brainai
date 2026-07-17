package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.FolderPropertiesDialog;
import com.edatasite.workforce.gwt.documents.client.Folders;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * Display the 'new folder' dialog for creating a new folder.
 *
 * @author Sherali
 */
public class NewFolderCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupPanel containerPanel;

    /**
     * @param aContainerPanel
     */
    public NewFolderCommand(PopupPanel aContainerPanel) {
        containerPanel = aContainerPanel;
    }

    public void execute() {
        containerPanel.hide();
		Folders folders = DocumentsView.get().getFolders();
		TreeItem selection = folders.getCurrent();
		FolderResource current = null;
		if (selection != null) {
			current = (FolderResource) selection.getUserObject();
		}
		boolean isPublic = false;
		if (current != null) {
			if (current.getFileType() == Constants.F_COMPANY_PUBLIC_ROOT) {
				isPublic = true;
			} else {
				FolderResource parent = current.getParent();
				while (parent != null) {
					if (parent.getFileType() == Constants.F_COMPANY_PUBLIC_ROOT) {
						isPublic = true;
						break;
					} else {
						parent = parent.getParent();
					}
				}
			}
		}
		if (!DocumentsView.get().getFolders().isDefaultFolders() || isPublic) {
            if (containerPanel != null) {
                containerPanel.hide();
            }
            displayNewFolder();
        } else {
            String createFolder;
            if (DocumentsView.get().getFolders() != null && DocumentsView.get().getFolders().getCurrent() != null) {
                createFolder = wfmStrings.youcantCreateNewFolderUnder() + DocumentsView.get().getFolders().getCurrent().getText();
            } else {
                createFolder = wfmStrings.youCantCreateNewFolder();
            }
            Info.show(createFolder, Info.Type.WARNING);
        }
    }

    void displayNewFolder() {
        TreeItem currentFolder = DocumentsView.get().getFolders().getCurrent();
        if (currentFolder == null) {
            DocumentsView.get().displayError(wfmStrings.youHaveToSelectTheParentFolderFirst());
            return;
        }
        FolderPropertiesDialog dlg = new FolderPropertiesDialog(true);
        dlg.center();
    }

}
