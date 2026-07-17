package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.ui.TreeItem;

public class DeleteFolderDialog extends WfmMessageBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public DeleteFolderDialog() {
        super(IconEnum.QUESTION, Action.YesNo);
        final FolderResource folder = (FolderResource) DocumentsView.get().getCurrentSelection();

        String message = "Are you sure you want to <b>permanently</b> delete folder '" + folder.getName() +
                "'?</br>If this folder is shared, it will no longer be shared with other people/employee";
        setTitle(wfmStrings.confirmation());
        addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteFolder();
                close();
            }
        });
        setMessage(message);
    }

    /**
     * Generate an RPC request to delete a folder.
     */
    private void deleteFolder() {
        final DnDTreeItem folder = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
        if (folder == null) {
            DocumentsView.get().displayError(wfmStrings.noFileWasSelected());
            return;
        }
        if (folder.getFolderResource() == null) {
            return;
        }
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().deleteFolder(folder.getFolderResource().getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                TreeItem curFolder = DocumentsView.get().getFolders().getCurrent();
                if (curFolder.getParentItem() != null) {
                    DocumentsView.get().getFolders().select(curFolder.getParentItem());
                    DocumentsView.get().getFolders().updateFolder((DnDTreeItem) curFolder.getParentItem());
                }
                DocumentsView.get().showFileList(true);
//                DocumentsView.get().getStatusPanel().updateStats();
                LoadingPanel.loading(false);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.fileNotFound());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }
}
