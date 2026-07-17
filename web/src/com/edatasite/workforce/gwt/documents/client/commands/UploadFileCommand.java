package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.Office365AuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.documents.client.FileUploadDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.OFFICE_365_DOCUMENTS;

/**
 * Upload a file command
 *
 * @author Sherali
 */
public class UploadFileCommand implements Command, CommandConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private PopupPanel containerPanel;
    private FileUploadType fileUploadType;
    private SelectItem listItem;
    private FolderResource folderResource;

    public UploadFileCommand(PopupPanel _containerPanel, FileUploadType fileUploadType) {
        containerPanel = _containerPanel;
        this.fileUploadType = fileUploadType;
    }

    public UploadFileCommand(PopupPanel _containerPanel, FileUploadType fileUploadType, SelectItem item, FolderResource folderResource) {
        containerPanel = _containerPanel;
        this.fileUploadType = fileUploadType;
        this.listItem = item;
        this.folderResource = folderResource;
    }

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        if (fileUploadType.equals(FileUploadType.GOOGLE_DOCUMENTS) || fileUploadType.equals(FileUploadType.LINK_TO_GOOGLE_DOCUMENTS)) {
            LoginService.App.get().isValid_User_For_Google_Gocs(new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable throwable) {
                    DocumentsView.get().displayError(wfmStrings.error());
                }

                public void success(Boolean result) {
                    boolean isValid = result;
                    if (isValid) {
                        Cookies.setCookie(GOOGLE_DOCS_COOKIE, CommandConstants.SUCCESS);
                        displayNewFile();

                    } else {
                        Cookies.setCookie(GOOGLE_DOCS_COOKIE, FAIL);
                        final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouGoogleDocument());
                        confirm.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                new GoogleAuthorizationPanel(Constants.GOOGLE_DOCUMENTS, true);
                            }
                        });
                        confirm.open();
                    }
                }
            });
        } else if (fileUploadType.equals(FileUploadType.AMAZON) || fileUploadType.equals(FileUploadType.LINK_TO_KPI_DOCUMENTS) || fileUploadType.equals(FileUploadType.MINIO)) {
            displayNewFile();
        } else if (fileUploadType.equals(FileUploadType.OFFICE_DOCUMENTS) || fileUploadType.equals(FileUploadType.LINK_TO_OFFICE_DOCUMENTS)) {
            LoginService.App.get().isValidUserOfficeAndGoogle(Constants.OFFICE_365, new AbstractAsyncCallback<ArrayList<Boolean>>() {
                public void failure(Throwable throwable) {
                    DocumentsView.get().displayError(wfmStrings.error());
                }

                public void success(ArrayList<Boolean> result) {
                    /*if (isValid) {
                        displayNewFile();
                    } else {

                        final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouOfficeDocument());
                        confirm.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                new Office365AuthorizationPanel(OFFICE_365_DOCUMENTS, true);
                            }
                        });
                        confirm.open();
                    }*/

                    if (result.get(0)) {
                        displayNewFile();
                    } else {
                        final WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouOfficeDocument());
                        confirm.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                new Office365AuthorizationPanel(OFFICE_365_DOCUMENTS, true);
                            }
                        });
                        confirm.open();
                    }
                }
            });
        }
    }

    /**
     * Display the 'new file' dialog for uploading a new file to the system.
     */
    private void displayNewFile() {
        if (folderResource == null) {
            TreeItem currentFolder = DocumentsView.get().getFolders().getCurrent();
            if (currentFolder == null) {
                DocumentsView.get().displayError(wfmStrings.youHaveToSelectTheParentFolderFirst());
                return;
            }
            folderResource = (FolderResource) currentFolder.getUserObject();
        }
        FileUploadDialog dlg = new FileUploadDialog(folderResource, fileUploadType, DocumentsView.get().getMaxSize(), true, listItem);
        Command command = () -> {
            DocumentsView.get().showFileList(true);
//                DocumentsView.get().getStatusPanel().updateStats();
        };
        dlg.addUploadListener(command, FileUploadDialog.FileUploadEvents.FILE_UPLOADED, FileUploadDialog.FileUploadEvents.FILE_REMOVED);
        dlg.center();
    }
}
