package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.documents.client.FileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.base.MaterialWidget;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 6/8/11
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class GWTFileUploadDialog {

    private FileUploadDialog dlg;
    private isDeleteFile isDeleteFile;
    private isWriteFolder isWriteFolder;
    private Integer maxFileSize;
    private Command loadCommand;
    private FolderResource folderResource;
    private int folderType;

    public GWTFileUploadDialog(int folderType, Integer folderID, final Integer entityID) {
        this.folderType = folderType;
        if (folderID == null) {
            getTempFolder();
        } else {
            DocumentsService.App.get().getFolderResource(folderType, folderID, new AbstractAsyncCallback<FolderResource>() {
                @Override
                public void failure(Throwable throwable) {
                    getTempFolder();
                }

                @Override
                public void success(FolderResource fr) {
                    folderResource = fr;
                    if (entityID != null && folderResource != null) {
                        folderResource.setEntityId(entityID);
                        getFileMaxSize();
                    }
                }
            });
        }
    }

    public void setActivator(MaterialWidget widget) {
        if (widget == null) {
            return;
        }
        widget.addClickHandler(e -> initialize(true));
    }

    public void onLoadCommand(Command loadCommand) {
        this.loadCommand = loadCommand;
    }

    public void isDeleteCommand(isDeleteFile isDeleteFile) {
        this.isDeleteFile = isDeleteFile;
    }

    public void isWriteCommand(isWriteFolder isWriteCommand) {
        this.isWriteFolder = isWriteCommand;
    }

    private void initialize() {
        initialize(false);
    }

    public void initialize(boolean isFromDocumentSection) {
        folderResource.setFileType(folderType);
        dlg = createFileUploadDIalog(folderResource, Utils.getFileUploadType(), maxFileSize, isFromDocumentSection);

        Command command = () -> {
            if (loadCommand != null) {
                loadCommand.execute();
            }
        };
        dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
    }

    public FileUploadDialog createFileUploadDIalog(FolderResource folderResource, FileUploadType fileUploadType, Integer maxFileSize, boolean isFromDocumentSection) {
        return new FileUploadDialog(folderResource, fileUploadType, maxFileSize, isFromDocumentSection);
    }

    private void getFileMaxSize() {
        if (isDeleteFile != null) {
            isDeleteFile.isDelete(folderResource != null && folderResource.getPermission().isDelete());
        }
        if (isWriteFolder != null) {
            isWriteFolder.isWrite(folderResource != null && folderResource.getPermission().isWrite());
        }
        if (Utils.getMaxFileUploadSize() == null) {
            DocumentsService.App.get().getCompanyFileUploadMaxSize(new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable caught) {
                    Utils.setMaxFileUploadSize(null);
                    maxFileSize = null;
                    initialize();
                }

                @Override
                public void success(Integer result) {
                    maxFileSize = result;
                    Utils.setMaxFileUploadSize(result);
                    initialize();
                }
            });
        }
    }

    private void getTempFolder() {
        CommonService.App.get().getTempFolderByCompanyID(Utils.isWebForm() ? Utils.getEncryptedCompanyID() : null, null, new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource tempFolderResource) {
                folderResource = tempFolderResource;
                getFileMaxSize();
            }
        });
    }

    public HashMap<Integer, FileResource> getUploadedFiles() {
        return dlg != null ? dlg.getUploadedFiles() : null;
    }

    public interface isDeleteFile {
        void isDelete(boolean isDelete);
    }

    public interface isWriteFolder {
        void isWrite(boolean isWrite);
    }
}
