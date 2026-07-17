package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Restore trashed files and folders.
 *
 * @author Sherali
 */
public class RestoreTrashCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupPanel containerPanel;

    public RestoreTrashCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

    public void execute() {
        containerPanel.hide();
        Object selection = DocumentsView.get().getCurrentSelection();
        if (selection == null) {
            // Check to see if Trash Node is selected.
            List folderList = new ArrayList();
            TreeItem trashItem = DocumentsView.get().getFolders().getTrashItem();
            for (int i = 0; i < trashItem.getChildCount(); i++) {
                folderList.add(trashItem.getChild(i).getUserObject());
            }
            return;
        }
        GWT.log("selection: " + selection.toString(), null);
        if (selection instanceof FileResource) {
            final FileResource resource = (FileResource) selection;
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().removeFileFromTrash(resource.getObjectId(), new AbstractAsyncCallback() {
                @Override
                public void success(Object result) {
                    LoadingPanel.loading(false);
                    DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
                    DocumentsView.get().showFileList();
//                    DocumentsView.get().showFileList(true);
                }

                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError(wfmStrings.fileDoesNotExist());
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }
            });
        } else if (selection instanceof List) {
            final List<FileResource> fdtos = (List<FileResource>) selection;
            final List<Integer> fileIds = new ArrayList<>();
            for (FileResource f : fdtos) {
                if (f.isDeleted()) {
                    fileIds.add(f.getObjectId());
                }
            }
            removeFileFromTrash(0, fileIds);
        } else if (selection instanceof FolderResource) {
            final FolderResource resource = (FolderResource) selection;
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().removeFolderFromTrash(resource.getObjectId(), new AbstractAsyncCallback() {
                @Override
                public void success(Object result) {
                    LoadingPanel.loading(false);
                    DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getRootItem());
                    DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
                }

                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError("Folder does not exist");
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }
            });
        }
    }

    private void removeFileFromTrash(final int index, final List<Integer> ids) {
        if (index >= ids.size()) {
            DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
            DocumentsView.get().showFileList(true);
            return;
        }
        DocumentsView.get().getDocumentsService().removeFileFromTrash(ids.get(index), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                removeFileFromTrash(index + 1, ids);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.fileDoesNotExist());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

}
