package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.documents.client.clipboard.Clipboard;
import com.edatasite.workforce.gwt.documents.client.clipboard.ClipboardItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.ArrayList;
import java.util.List;


/**
 * Command for cutting a file, folder or user to Documents Clipboard
 *
 * @author Sherali
 */
public class CutCommand implements Command {
    private PopupPanel containerPanel;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public CutCommand(PopupPanel _containerPanel) {
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
        GWT.log("selection: " + selection.toString(), null);
        if (selection instanceof FolderResource) {
            ClipboardItem clipboardItem = new ClipboardItem(Clipboard.CUT, (FolderResource) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof FileResource) {
            ClipboardItem clipboardItem = new ClipboardItem(Clipboard.CUT, (FileResource) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof GroupMemberItem) {
            ClipboardItem clipboardItem = new ClipboardItem(Clipboard.CUT, (GroupMemberItem) selection);
            DocumentsView.get().getClipboard().setItem(clipboardItem);
        } else if (selection instanceof List) {
            List<FileResource> fRes = (List<FileResource>) selection;
            ArrayList<FileResource> fileResources = new ArrayList<>();
            ArrayList<FileResource> cantDelFileResources = new ArrayList<>();
            for (FileResource fileResource : fRes) {
                if (fileResource.getPermission().isDelete()) {
                    fileResources.add(fileResource);
                } else {
                    cantDelFileResources.add(fileResource);
                }
            }
            if(!fileResources.isEmpty()) {
                ClipboardItem clipboardItem = new ClipboardItem(Clipboard.CUT, fileResources);
                DocumentsView.get().getClipboard().setItem(clipboardItem);
            } else {
                DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
            }
        }
    }

}
