package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.documents.client.clipboard.ClipboardItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.List;

/**
 * Command for copying a file, folder or user to Documents Clipboard
 *
 * @author Sherali
 */
public class CopyCommand implements Command {
    private PopupPanel containerPanel;

    public CopyCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }
    /* (non-Javadoc)
      * @see com.google.gwt.user.client.Command#execute()
      */

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        Object selection = DocumentsView.get().getCurrentSelection();
        if (selection == null) {
            return;
        }

        if (selection instanceof FolderResource) {
            ClipboardItem clipboardItem = new ClipboardItem((FolderResource) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof FileResource) {
            ClipboardItem clipboardItem = new ClipboardItem((FileResource) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof GroupMemberItem) {
            ClipboardItem clipboardItem = new ClipboardItem((GroupMemberItem) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof List) {
            ClipboardItem clipboardItem = new ClipboardItem((List<FileResource>) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        }

    }

}
