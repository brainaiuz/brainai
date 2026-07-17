package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
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
 * Move file or folder to trash.
 *
 * @author Sherali
 */
public class ToTrashCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupPanel containerPanel;

    public ToTrashCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

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
            //Move Single Folder to Trash
            moveFolderToTrash( ((FolderResource) selection).getObjectId() );
        } else if (selection instanceof FileResource) {
            FileResource fdto = (FileResource) selection;
            LoadingPanel.loading(true);
            DocumentsView.get().getDocumentsService().moveFileToTrash(fdto.getObjectId(), new AbstractAsyncCallback() {
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

                @Override
                public void success(Object result) {
                    LoadingPanel.loading(false);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.file()), Info.Type.INFO);
                    DocumentsView.get().showFileList();
//                    DocumentsView.get().showFileList(true);

                }
            });


        } else if (selection instanceof List) {
            List<FileResource> fdtos = (List<FileResource>) selection;
            ArrayList<Integer> fileIds = new ArrayList<>();
            List<Integer> folderIds = new ArrayList<>();
            for (FileResource fileResource : fdtos) {
                if (fileResource.getPermission()!=null && fileResource.getPermission().isDelete()) {
                    if(fileResource.isFolder()) {
                        folderIds.add(fileResource.getObjectId());
                    } else {
                        fileIds.add(fileResource.getObjectId());
                    }
                }
            }

            //Move Files to Trash
            moveFilesToTrash(fileIds);
            //Move Folders to Trash
            for (Integer folderId : folderIds) {
                moveFolderToTrash(folderId);
            }
        }
    }

    private void moveFolderToTrash(Integer folderId) {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().moveFolderToTrash(folderId, new AbstractAsyncCallback() {
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
                    GWT.log("Error moving folder to trash", e);
                }
            }

            @Override
            public void success(Object result) {
                TreeItem folder = DocumentsView.get().getFolders().getCurrent();
                if (folder.getParentItem() != null) {
                    DocumentsView.get().getFolders().select(folder.getParentItem());
                    DocumentsView.get().getFolders().updateFolder((DnDTreeItem) folder.getParentItem());
                }
                DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
                DocumentsView.get().showFileList(true);
                LoadingPanel.loading(false);
            }
        });
    }

    private void moveFilesToTrash(ArrayList<Integer> fileIds) {
		/*for (Integer fileId : fileIds) {
            DocumentsView.get().getDocumentsService().moveFileToTrash(fileId, new AbstractAsyncCallback() {
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

                @Override
                public void success(Object result) {
                    DocumentsView.get().showFileList();
                    //DocumentsView.get().showFileList(true);
                }
            });
        }*/
		LoadingPanel.loading(true);
		DocumentsView.get().getDocumentsService().batchDeleteFiles(fileIds, new AbstractAsyncCallback() {
			@Override
			public void failure(Throwable throwable) {
				super.failure(throwable);
				LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

			@Override
			public void success(Object result) {
				DocumentsView.get().showFileList();
				LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.file()), Info.Type.INFO);
            }
		});
    }

}
