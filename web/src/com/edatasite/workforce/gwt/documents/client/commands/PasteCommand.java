package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.clipboard.Clipboard;
import com.edatasite.workforce.gwt.documents.client.clipboard.ClipboardItem;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
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
 * @author Sherali Command for pasting Clipboard contents
 */
public class PasteCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private PopupPanel containerPanel;

    public PasteCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        Object selection = DocumentsView.get().getCurrentSelection();

        if (selection != null && selection instanceof GroupMembersViewItem) {

            final ClipboardItem citem = DocumentsView.get().getClipboard().getItem();
            GroupMembersViewItem group = (GroupMembersViewItem) DocumentsView.get().getCurrentSelection();
            if (citem.getMember() != null) {
                DocumentsView.get().getDocumentsService().addUserToGroup(group.getGroupID(), citem.getMember().getTrusteeID(), new AbstractAsyncCallback() {

                    @Override
                    public void success(Object result) {
                        DocumentsView.get().getGroups().updateGroups();
                        DocumentsView.get().showUserList();
                    }

                    @Override
                    public void failure(Throwable throwable) {
                        try {
                            throw throwable;
                        } catch (ObjectNotFoundException e) {
                            DocumentsView.get().displayError(wfmStrings.userDoesNotExist());
                        } catch (DuplicateNameException e) {
                            DocumentsView.get().displayError(wfmStrings.user() + " " + wfmStrings.withTheSameNameAlreadyExist());
                        } catch (InsufficientPermissionsException e) {
                            DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                        } catch (Throwable e) {
                            // last resort  a very unexpected exception
                        }
                    }
                });
                return;
            }
        }
        FolderResource selectedFolder = null;
        if (DocumentsView.get().getFolderResource() != null) {
            selectedFolder = DocumentsView.get().getFolderResource();
        } else if (selection != null && selection instanceof FolderResource) {
            selectedFolder = (FolderResource) selection;
        } else if (DocumentsView.get().getFolders().getCurrent() != null && ((DnDTreeItem) DocumentsView.get().getFolders().getCurrent()).getFolderResource() != null) {
            selectedFolder = ((DnDTreeItem) DocumentsView.get().getFolders().getCurrent()).getFolderResource();
        }
        GWT.log("Selected Folder: " + selectedFolder);
        if (selectedFolder != null) {

            final ClipboardItem citem = DocumentsView.get().getClipboard().getItem();

            if (citem != null) {
                if (citem.getFolderResource() != null) {
                    if (citem.getOperation() == Clipboard.COPY) {
                        //Copy Single Folder
                        copyFolder(selectedFolder, citem.getFolderResource().getObjectId());
                    } else if (citem.getOperation() == Clipboard.CUT) {
                        //Move Single Folder
                        moveFolder(selectedFolder, citem.getFolderResource().getObjectId());
                    }
                } else if (citem.getFile() != null) {

                    if (citem.getOperation() == Clipboard.COPY) {
                        LoadingPanel.loading(true);
                        DocumentsView.get().getDocumentsService().copyFile(citem.getFile().getObjectId(), selectedFolder.getObjectId(), null, new AbstractAsyncCallback() {
                            @Override
                            public void success(Object result) {
                                LoadingPanel.loading(false);
                                DocumentsView.get().showFileList(true);
//                            DocumentsView.get().getStatusPanel().updateStats();
                            }

                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                try {
                                    throw throwable;
                                } catch (QuotaExceededException e) {
                                    DocumentsView.get().displayError(wfmStrings.yourQuoteHesBeenExceeded());
                                } catch (ObjectNotFoundException e) {
                                    DocumentsView.get().displayError(wfmStrings.fileNotFound());
                                } catch (InsufficientPermissionsException e) {
                                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                                } catch (DuplicateNameException e) {
                                    DocumentsView.get().displayError(wfmStrings.file() + " " + wfmStrings.withTheSameNameAlreadyExist());
                                } catch (Throwable e) {
                                    // last resort  a very unexpected exception
                                }
                            }
                        });

                    } else if (citem.getOperation() == Clipboard.CUT) {
                        LoadingPanel.loading(true);
                        DocumentsView.get().getDocumentsService().moveFile(citem.getFile().getObjectId(), selectedFolder.getObjectId(), new AbstractAsyncCallback() {
                            @Override
                            public void success(Object result) {
                                LoadingPanel.loading(false);
                                DocumentsView.get().showFileList(true);
//                                DocumentsView.get().getStatusPanel().updateStats();
                                //After success move, buffer must be reset
                                DocumentsView.get().getClipboard().setItem(null);
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

                    }
                } else if (citem.getFiles() != null) {
                    List<FileResource> res = citem.getFiles();
                    List<Integer> fileIds = new ArrayList<>();
                    List<Integer> folderIds = new ArrayList<>();
                    for (FileResource fileResource : res) {
                        if (fileResource.isFolder()) {
                            folderIds.add(fileResource.getObjectId());
                        } else {
                            fileIds.add(fileResource.getObjectId());
                        }
                    }

                    if (citem.getOperation() == Clipboard.COPY) {
                        if (!fileIds.isEmpty()) {
                            int index = 0;
                            executeCopyFile(index, fileIds, selectedFolder.getObjectId());
                        }
                        if (!folderIds.isEmpty()) {
                            //Copy List Of Folder
                            for (Integer folderId : folderIds) {
                                copyFolder(selectedFolder, folderId);
                            }
                        }

                    } else if (citem.getOperation() == Clipboard.CUT) {

                        if (!fileIds.isEmpty()) {
                            int index = 0;
                            executeMoveFile(index, fileIds, selectedFolder.getObjectId());
                        }
                        if (!folderIds.isEmpty()) {
                            //Move List Of Folder
                            for (Integer folderId : folderIds) {
                                moveFolder(selectedFolder, folderId);
                            }
                        }
                    }
                }
            }
        }
    }

    private void copyFolder(FolderResource selectedFolder, Integer folderId) {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().copyFolder(folderId, selectedFolder.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                LoadingPanel.loading(false);
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent());
//                            DocumentsView.get().getStatusPanel().updateStats();
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (QuotaExceededException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    private void moveFolder(FolderResource selectedFolder, Integer folderId) {
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().moveFolder(folderId, selectedFolder.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                List<TreeItem> items = DocumentsView.get().getFolders().getItemsOfTreeForPath(folderId);
                for (TreeItem item : items)
                    if (item.getParentItem() != null && !item.equals(DocumentsView.get().getFolders().getCurrent())) {
                        DocumentsView.get().getFolders().updateFolder((DnDTreeItem) item.getParentItem());
                    }
//                            DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent().getParentItem());
//                            DocumentsView.get().getStatusPanel().updateStats();
                DocumentsView.get().showFileList(true);
                LoadingPanel.loading(false);
                //After success move, buffer must be reset
                DocumentsView.get().getClipboard().setItem(null);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    private void executeCopyFile(final int index, final List<Integer> ids, final Integer folderId) {
        if (index >= ids.size()) {
            DocumentsView.get().showFileList(true);
            return;
        }
        DocumentsView.get().getDocumentsService().moveFile(ids.get(index), folderId, new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                executeMoveFile(index + 1, ids, folderId);
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

    private void executeMoveFile(final int index, final List<Integer> ids, final Integer folderId) {
        if (index >= ids.size()) {
            DocumentsView.get().showFileList(true);
            return;
        }
        DocumentsView.get().getDocumentsService().copyFile(ids.get(index), folderId, null, new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                executeCopyFile(index + 1, ids, folderId);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (QuotaExceededException e) {
                    DocumentsView.get().displayError(wfmStrings.yourQuoteHesBeenExceeded());
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.fileNotFound());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(wfmStrings.file() + " " + wfmStrings.withTheSameNameAlreadyExist());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }
}
