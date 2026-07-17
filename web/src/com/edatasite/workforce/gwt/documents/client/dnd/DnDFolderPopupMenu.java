/*
package com.edatasite.workforce.gwt.documents.client.dnd;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.edatasite.workforce.gwt.documents.client.Folders;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.RestResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author Sherali
 *//*

public class DnDFolderPopupMenu extends PopupPanel {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final DocumentImages.Images images = DocumentImages.get();

    public DnDFolderPopupMenu(final FolderResource target, final Object toCopy, boolean othersShared) {
        // The popup's constructor's argument is a boolean specifying that it
        // auto-close itself when the user clicks outside of it.
        super(true);
        setAnimationEnabled(true);

        // A dummy command that we will execute from unimplemented leaves.
        final Command cancelCmd = new Command() {

            public void execute() {
                hide();
            }
        };

        final MenuBar contextMenu = new MenuBar(true);
        final Folders folders = DocumentsView.get().getFolders();

        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.cut()).getHTML() + "&nbsp;Move</span>", true, new Command() {

            public void execute() {
                if (toCopy instanceof FolderResource) {
                    List<TreeItem> treeItems = folders.getItemsOfTreeForPath(((RestResource) toCopy).getObjectId());
                    List<TreeItem> parents = new ArrayList();
                    for (TreeItem item : treeItems) {
                        if (item.getParentItem() != null) {
                            parents.add(item.getParentItem());
                        }
                    }
                    moveFolder(target, (FolderResource) toCopy, parents);
                } else if (toCopy instanceof List) {
                    moveFiles(target, (List<FileResource>) toCopy);
                }
                hide();
            }

        }).setVisible(target != null);

        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.copy()).getHTML() + "&nbsp;Copy</span>", true, new Command() {

            public void execute() {
                if (toCopy instanceof FolderResource) {
                    copyFolder(target, (FolderResource) toCopy);
                } else if (toCopy instanceof List) {
                    copyFiles(target, (List<FileResource>) toCopy);
                }
                hide();
            }

        }).setVisible(target != null);

        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;Delete (Trash)</span>", true, new Command() {

            public void execute() {
                if (toCopy instanceof FolderResource) {
                    final List<TreeItem> treeItems = folders.getItemsOfTreeForPath(((RestResource) toCopy).getObjectId());
                    List<TreeItem> parents = new ArrayList();
                    for (TreeItem item : treeItems) {
                        if (item.getParentItem() != null) {
                            parents.add(item.getParentItem());
                        }
                    }
                    trashFolder((FolderResource) toCopy, parents);
                } else if (toCopy instanceof List) {
                    trashFiles((List<FileResource>) toCopy);
                }
                hide();
            }

        }).setVisible(target == null);
        contextMenu.addItem("<span>" + AbstractImagePrototype.create(images.delete()).getHTML() + "&nbsp;Cancel</span>", true, cancelCmd);

        add(contextMenu);

    }

    private void copyFolder(final FolderResource target, FolderResource toCopy) {
        DocumentsView.get().getDocumentsService().copyFolder(target.getObjectId(), toCopy.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(wfmStrings.folder() + " " + wfmStrings.withTheSameNameAlreadyExist());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }

            @Override
            public void success(Object result) {
                final TreeItem folder;
                TreeItem folderTemp = DocumentsView.get().getFolders().getUserItem(target);
                if (folderTemp == null) {
                    folder = DocumentsView.get().getFolders().getOtherSharedItem(target);
                } else {
                    folder = folderTemp;
                }
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) folder);
//                DocumentsView.get().getStatusPanel().updateStats();
            }
        });

    }

    private void moveFolder(final FolderResource target, final FolderResource toCopy, final List<TreeItem> items) {
        DocumentsView.get().getDocumentsService().moveFolder(target.getObjectId(), toCopy.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }

            @Override
            public void success(Object result) {
                final TreeItem folder;
                for (TreeItem i : items) {
                    DnDTreeItem id = (DnDTreeItem) i;
                    if (id.getChild(toCopy) != null) {
                        id.removeItem(id.getChild(toCopy));
                    }
                }
                DocumentsView.get().getFolders().clearSelection();
                TreeItem folderTemp = DocumentsView.get().getFolders().getUserItem(target);
                if (folderTemp == null) {
                    folder = DocumentsView.get().getFolders().getOtherSharedItem(target);
                } else {
                    folder = folderTemp;
                }
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) folder);
                DocumentsView.get().showFileList(true);
//                DocumentsView.get().getStatusPanel().updateStats();
            }
        });
    }

    private void copyFiles(final FolderResource ftarget, List<FileResource> files) {
        List<Integer> fileIds = new ArrayList<Integer>();
        for (FileResource fileResource : files) {
            fileIds.add(fileResource.getObjectId());
        }
        int index = 0;
        executeCopyOrMoveFiles(ftarget.getObjectId(), index, fileIds, false);

    }

    private void moveFiles(final FolderResource ftarget, List<FileResource> files) {
        List<Integer> fileIds = new ArrayList<Integer>();
        for (FileResource fileResource : files) {
            fileIds.add(fileResource.getObjectId());
        }
        int index = 0;
        executeCopyOrMoveFiles(ftarget.getObjectId(), index, fileIds, true);
    }

    private void trashFolder(final FolderResource folder, final List<TreeItem> items) {
        DocumentsView.get().getDocumentsService().moveFolderToTrash(folder.getObjectId(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                for (TreeItem item : items) {
                    DocumentsView.get().getFolders().updateFolder((DnDTreeItem) item);
                }
                DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
                DocumentsView.get().showFileList(true);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    private void trashFiles(List<FileResource> files) {
        for (FileResource f : files) {
            DocumentsView.get().getDocumentsService().moveFileToTrash(f.getObjectId(), new AbstractAsyncCallback() {
                @Override
                public void success(Object result) {
                    DocumentsView.get().showFileList(true);
                }

                @Override
                public void failure(Throwable throwable) {
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }
            });
        }
    }

    private void executeCopyOrMoveFiles(final Integer folderId, final int index, final List<Integer> paths, final boolean move) {
        if (index >= paths.size()) {
            DocumentsView.get().showFileList(true);
//            DocumentsView.get().getStatusPanel().updateStats();
            return;
        }
        if (move) {//move files to folder
            DocumentsView.get().getDocumentsService().moveFile(paths.get(index), folderId, new AbstractAsyncCallback() {
                @Override
                public void failure(Throwable throwable) {
                    try {
                        throw throwable;
                    } catch (ObjectNotFoundException e) {
                        e.printStackTrace();
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }

                @Override
                public void success(Object result) {
                    executeCopyOrMoveFiles(folderId, index + 1, paths, move);
                }
            });

        } else if (!move) {//copy files to folder
            DocumentsView.get().getDocumentsService().copyFile(paths.get(index), folderId, new AbstractAsyncCallback() {
                @Override
                public void failure(Throwable throwable) {
                    try {
                        throw throwable;
                    } catch (QuotaExceededException e) {
                        DocumentsView.get().displayError(wfmStrings.yourQuoteHesBeenExceeded());
                    } catch (ObjectNotFoundException e) {
                        DocumentsView.get().displayError(wfmStrings.fileNotFound());
                    } catch (DuplicateNameException e) {
                        DocumentsView.get().displayError(wfmStrings.file() + " " + wfmStrings.withTheSameNameAlreadyExist());
                    } catch (InsufficientPermissionsException e) {
                        DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                    } catch (Throwable e) {
                        // last resort  a very unexpected exception
                    }
                }

                @Override
                public void success(Object result) {
                    executeCopyOrMoveFiles(folderId, index + 1, paths, move);
                }
            });
        }
    }

}
*/
