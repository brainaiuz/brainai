package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;

import java.util.List;

/**
 * The 'delete file' dialog box.
 */
public class DeleteFileDialog extends WfmMessageBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public DeleteFileDialog() {
        super(IconEnum.QUESTION, Action.YesNo);
        String text;
        final Object selection = DocumentsView.get().getCurrentSelection();
        if (selection instanceof FileResource) {
            text = "Are you sure you want to <b>permanently</b> delete file '" + ((FileResource) selection).getName() + "'?";
        } else {
            text = "Are you sure you want to <b>permanently</b> delete the selected files?";
        }
        setMessage(text);

        // Create the 'Delete' button, along with a listener that hides the dialog
        // when the button is clicked and deletes the file.
        addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteFile();
                close();
            }
        });
    }

    /**
     * Generate an RPC request to delete a file.
     */
    private void deleteFile() {
        Object selection = DocumentsView.get().getCurrentSelection();
        if (selection == null) {
            DocumentsView.get().displayError(wfmStrings.noFileWasSelected());
            return;
        }
        if (selection instanceof FileResource) {
            FileResource file = (FileResource) selection;
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().deleteFile(file.getObjectId(), new AbstractAsyncCallback() {
                @Override
                public void success(Object result) {
                    LoadingPanel.loading(false);
                    DocumentsView.get().updateFileCache(true, true /*clear selection*/);
//                    DocumentsView.get().getStatusPanel().updateStats();
                }

                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError(wfmStrings.fileNotFound());
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }
            });
        } else if (selection instanceof List) {
            List<FileResource> files = (List<FileResource>) selection;
            for (FileResource f : files) {
                if (f.getPermission().isDelete()) {
                    deleteFileMethod(f);
                }
            }
        }
    }

    private void deleteFileMethod(FileResource f) {
        DocumentsView.get().getDocumentsService().deleteFile(f.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                DocumentsView.get().showFileList(true);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.fileNotFound());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }
}
