package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.GoogleAuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.InsertSiteUlrPopUp;
import com.edatasite.workforce.gwt.core.client.ui.Office365AuthorizationPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 4, 2010
 * Time: 3:01:10 PM
 */
public class GWTFileUpload extends Composite implements CommandConstants, Constants {
    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public enum UploadType {
        AMAZON,
        GOOGLE_DOCUMENTS,
        OFFICE_365_DOCUMENTS,
        OFFICE_SHARE_POINT_DOCUMENTS,
        MINIO,
        LOCAL
    }

    
    private final DocumentsServiceAsync documentsService = DocumentsService.App.get();

    private AttachmentDeleteHandler cancelUpload;
    private final MultiUploader defaultUploader;
    private Command finishUpload;
    private Command startUpload;
    private FolderResource folder;
    private final Map<String, Command> listeners = new HashMap<>();

    public interface UploadFileEvents {
        String FILE_UPLOADED = "FILE_UPLOADED";
        String FILE_REMOVED = "FILE_REMOVED";
        String FILE_UPLOAD_ERRROR = "FILE_UPLOAD_ERROR";
    }

    // Load the image in the document and in the case of success attach it to the viewer
    private final IUploader.OnCancelUploaderHandler onCancelUploaderHandler = new IUploader.OnCancelUploaderHandler() {
        public void onCancel(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                String serverResponse = uploader.getServerResponse();
                Integer fileID = Integer.parseInt(parseResponse(serverResponse, "file-1-id"));

                uploadFiles.remove(fileID);

                if (cancelUpload != null) {
                    cancelUpload.onDelete(fileID);
                }
                if (listeners.size() > 0) {
                    Command command = listeners.get(UploadFileEvents.FILE_UPLOAD_ERRROR);
                    if (command != null) {
                        command.execute();
                    }
                    command = listeners.get(UploadFileEvents.FILE_REMOVED);
                    if (command != null) {
                        command.execute();
                    }
                }
            }
        }
    };

    // Load the image in the document and in the case of success attach it to the viewer
    private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                String serverResponse = uploader.getServerResponse();
                Integer fileID = Integer.parseInt(parseResponse(serverResponse, "file-1-id"));

                FileResource fileResource = new FileResource();
                fileResource.setObjectId(fileID);
                fileResource.setBodyId(fileID);//We have to separate them and use only one, either objectID or bodyID;
                fileResource.setName(getFilename(uploader.getInputName()));
                fileResource.setContentType(parseResponse(serverResponse, "file-1-type"));
                uploadFiles.put(fileID, fileResource);

                if (finishUpload != null) {
                    finishUpload.execute();
                }
                if (listeners.size() > 0) {
                    Command command = listeners.get(UploadFileEvents.FILE_UPLOADED);
                    if (command != null) {
                        command.execute();
                    }
                }
            }
        }
    };

    private final IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {
        public void onStart(IUploader uploader) {
            if (!"".equals(uploader.getInputName())) {
                prepareAndSubmit(uploader.getInputName(), uploader);
                if (startUpload != null) {
                    startUpload.execute();
                }
            }
        }
    };

    private final HashMap<Integer, FileResource> uploadFiles = new HashMap<>();
    private UploadType uploadType;
    private boolean withoutServerRadioButtons = false;

    public GWTFileUpload() {
        this(UploadType.AMAZON);
    }

    public GWTFileUpload(boolean withoutServerRadioButtons) {
        this(UploadType.AMAZON, withoutServerRadioButtons);
    }

    public GWTFileUpload(UploadType uploadType, boolean... withoutRadioButtons) {
        this.withoutServerRadioButtons = withoutRadioButtons != null && withoutRadioButtons.length > 0 && withoutRadioButtons[0];
        this.uploadType = uploadType;
            CommonService.App.get().getTempFolderByCompanyID(Utils.isWebForm()? Utils.getEncryptedCompanyID() : null, Utils.isWebForm()? Utils.getUserID().toString() : null, new AsyncCallback<FolderResource>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(FolderResource result) {
                    folder = result;
                    // We are able to add customized parameters to servlet call
                    setServlet();
                }
            });
        defaultUploader = new MultiUploader();
        // Add a finish handler which will load the image once the upload finishes
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
        defaultUploader.setFileInputPrefix("default");
        defaultUploader.avoidRepeatFiles(false);
        defaultUploader.addOnStartUploadHandler(onStartUploaderHandler);
        defaultUploader.addOnCancelUploadHandler(onCancelUploaderHandler);

        final VerticalPanel vp = new VerticalPanel();
        if (!withoutServerRadioButtons) {
            ProfileService.App.get().getEnableUploadTypes(new AbstractAsyncCallback<HashMap<String, Boolean>>() {
                public void failure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(HashMap<String, Boolean> result) {
                    intialiazeData(vp, result);
                }
            });
        }
        vp.add(defaultUploader);
        vp.setSpacing(4);
        initWidget(vp);
    }

    private void intialiazeData(VerticalPanel vp, HashMap<String, Boolean> result) {

        if (result.get(AMAZON) != null && result.get(AMAZON)) {
            final RadioButton toAmazon = new KpiRadioButton("upload", wfmStrings.uploadFileToKpiStorage() + " " + Utils.getProductName() + " " + wfmStrings.storage(), true);
            toAmazon.setValue(true);
            toAmazon.addClickHandler(event -> setServlet(UploadType.AMAZON));
            vp.add(toAmazon);
        }

        if (result.get(GOOGLE) != null && result.get(GOOGLE)) {
            final RadioButton toGoogle = new KpiRadioButton("upload", wfmStrings.uploadToGoogleDocuments(), true);
            toGoogle.addClickHandler(event -> LoginService.App.get().isValid_User_For_Google_Gocs(new AbstractAsyncCallback<Boolean>() {
                public void failure(Throwable throwable) {
                    com.edatasite.workforce.gwt.documents.client.view.DocumentsView.get().displayError(wfmStrings.error());
                }

                public void success(Boolean result13) {
                    if (result13) {
                        toGoogle.setValue(true);
                        setServlet(UploadType.GOOGLE_DOCUMENTS);
                    } else {
                        toGoogle.setValue(false);
//                        toAmazon.setValue(true);
                        WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, wfmStrings.youNeedToAutorizeToYouGoogleDocument());
                        confirm.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                new GoogleAuthorizationPanel(GOOGLE_DOCUMENTS, true);
                            }
                        });
                        confirm.open();
                    }
                }
            }));
            vp.add(toGoogle);
        }

        if (result.get(OFFICE_365) != null && result.get(OFFICE_365)) {
            final RadioButton toOffice = new KpiRadioButton("upload", wfmStrings.uploadToOfficeDocuments(), true);
            toOffice.addClickHandler(event -> LoginService.App.get().isValidUserOfficeAndGoogle(Constants.OFFICE_365, new AbstractAsyncCallback<ArrayList<Boolean>>() {
                public void failure(Throwable throwable) {
                    com.edatasite.workforce.gwt.documents.client.view.DocumentsView.get().displayError(wfmStrings.error());
                }

                public void success(ArrayList<Boolean> result12) {
                    if (result12.get(0)) {
                        if (result12.get(1)) {
                            toOffice.setValue(true);
                            setServlet(UploadType.OFFICE_365_DOCUMENTS);
                        } else {
                            toOffice.setValue(false);
//                            toAmazon.setValue(true);
                            openOffice365AuthPanel(wfmStrings.youTokenExpiredPleaseTryAgain(), false);
                        }
                    } else {
                        toOffice.setValue(false);
//                        toAmazon.setValue(true);
                        openOffice365AuthPanel(wfmStrings.youNeedToAutorizeToYouOfficeDocument(), false);
                    }
                }
            }));
            vp.add(toOffice);
        }

        if (result.get(UPLOAD_SHARE_POINT) != null && result.get(UPLOAD_SHARE_POINT)) {
            final RadioButton toOfficeSharePoint = new KpiRadioButton("upload", wfmStrings.uploadToOfficeSharePointDocuments(), true);
            toOfficeSharePoint.addClickHandler(event -> LoginService.App.get().isValidUserOfficeAndGoogle(Constants.OFFICE_365_SHARE_POINT, new AbstractAsyncCallback<ArrayList<Boolean>>() {
                public void failure(Throwable throwable) {
                    com.edatasite.workforce.gwt.documents.client.view.DocumentsView.get().displayError(wfmStrings.error());
                }

                public void success(ArrayList<Boolean> result1) {
                    if (result1.get(0)) {
                        if (result1.get(1)) {
                            toOfficeSharePoint.setValue(true);
                            setServlet(UploadType.OFFICE_SHARE_POINT_DOCUMENTS);
                        } else {
                            toOfficeSharePoint.setValue(false);
//                            toAmazon.setValue(true);
                            openOffice365AuthPanel(wfmStrings.youTokenExpiredPleaseTryAgain(), true);
                        }
                    } else {
                        toOfficeSharePoint.setValue(false);
//                        toAmazon.setValue(true);
                        openOffice365AuthPanel(wfmStrings.youNeedToAutorizeToYouOfficeDocument(), true);
                    }
                }
            }));
            vp.add(toOfficeSharePoint);
        }

        if (result.get(MINIO) != null && result.get(MINIO)) {
            final RadioButton toLocal = new KpiRadioButton("upload", wfmStrings.uploadToLocalStorage(), true);
            toLocal.setValue(true);
            toLocal.addClickHandler(event -> setServlet(UploadType.MINIO));
            vp.add(toLocal);
        }

        if (result.get(LOCAL) != null && result.get(LOCAL)) {
            final RadioButton toLocal = new KpiRadioButton("upload", wfmStrings.uploadToLocalStorage(), true);
            toLocal.setValue(true);
            toLocal.addClickHandler(event -> setServlet(UploadType.LOCAL));
            vp.add(toLocal);
        }
    }

    private void openOffice365AuthPanel(String message, final boolean isSharepoint) {
        WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.OkCancel, message);
        confirm.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                if (isSharepoint) {
                    new InsertSiteUlrPopUp(OFFICE_365_DOCUMENTS).open();
                } else {
                    new Office365AuthorizationPanel(OFFICE_365_DOCUMENTS, true);
                }
            }
        });
        confirm.open();
    }

    public void clearAndAdd() {
        defaultUploader.reset();
    }

    public FileItem[] getAttachedFiles() {
        FileItem[] attachments = new FileItem[uploadFiles.size()];

        int i = 0;
        for (FileResource file : uploadFiles.values()) {
            attachments[i] = new FileItem();
            attachments[i].setId(file.getObjectId());
            attachments[i].setFileName(file.getName());
            i++;
        }

        return attachments;
    }

    public HashMap<Integer, FileResource> getUploadFiles() {
        return uploadFiles;
    }

    public boolean isEmpty() {
        return uploadFiles == null || uploadFiles.size() == 0;
    }

    public boolean isFinished() {
        boolean finished = uploadFiles == null || uploadFiles.size() <= 0;
        if (finished) {
            if (finishUpload != null) {
                finishUpload.execute();
            }
        }
        return finished;
    }

    public void onCancelUpload(AttachmentDeleteHandler cancelUpload) {
        this.cancelUpload = cancelUpload;
    }

    public void onFinishUpload(Command finishUpload) {
        this.finishUpload = finishUpload;
    }

    public void onStartUpload(Command startUpload) {
        this.startUpload = startUpload;
    }

    /**
     * Make any last minute checks and start the upload.
     */
    public void prepareAndSubmit(final String fname, final IUploader uploader) {
        if (getFileForName(fname) == null) {
            //we are going to create a file, so we check to see if there is a trashed file with the same name
            FileResource theSame = null;
            for (FileResource fres : folder.getFiles()) {
                if (fres.isDeleted() && fres.getName().equals(fname)) {
                    theSame = fres;
                }
            }
            if (theSame != null) {
                final FileResource sameFile = theSame;
                GWT.log("The same deleted file", null);
                WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, "A file with " +
                                                                                           "the same name exists in the trash. If you continue,<br/>the trashed " +
                                                                                           "file  '" + fname + "' will be renamed automatically for you.");
                confirm.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onCancel() {
                        uploader.cancel();
                    }

                    @Override
                    public void onSubmit() {
                        updateTrashedFile(getBackupFilename(fname), sameFile, uploader);
                    }
                });
                confirm.open();
            }
        } else {
            // We are going to update an existing file, so show a confirmation dialog.
            WfmMessageBox confirm = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.areYouSureThatYouWantToUpdate() + " " + fname + "?");
            confirm.addCloseHandler(new CloseHandler() {
                @Override
                public void onCancel() {
                    uploader.cancel();
                }

                @Override
                public void onSubmit() {
                    uploader.submit();
                }
            });
            confirm.open();
        }
    }

    public void setServlet(UploadType uploadType) {
        this.uploadType = uploadType;
        setServlet();
    }

    protected void setServlet() {
        Integer entityID = null;
        // You can add customized parameters to servlet call
        String upTo = "";
        if (uploadType == UploadType.AMAZON) {
            upTo = AMAZON_PARAM_NAME;
        } else if (uploadType == UploadType.GOOGLE_DOCUMENTS) {
            upTo = GOOGLE_DOCS_PARAM_NAME;
        } else if (uploadType == UploadType.OFFICE_365_DOCUMENTS) {
            upTo = OFFICE_365_DOCS_PARAM_NAME;
        } else if (uploadType == UploadType.OFFICE_SHARE_POINT_DOCUMENTS) {
            upTo = OFFICE_365_DOCS_SHARE_POINT_PARAM_NAME;
        } else if (uploadType == UploadType.MINIO) {
            upTo = MINIO_PARAM_NAME;
        } else if (uploadType == UploadType.LOCAL) {
            upTo = LOCAL_PARAM_NAME;
        }
        defaultUploader.setServletPath(GWT.getHostPageBaseURL() + "servlet.docupld" + "?" + FOLDER_ID + "=" + folder.getObjectId()
                + "&" + SESSION_ID_PARAM_NAME + "=" + getSessionID()
                + "&" + UPLOAD_TYPE_PARAM_NAME + "=" + upTo
                + "&" + ENTITY_ID + "=" + entityID
                + "&" + FOLDER_TYPE_ID + "=" + folder.getFileType()
                + (Utils.isWebForm() ? "&" + CommandConstants.COMPANY__ID + "=" + Utils.getEncryptedCompanyID() : "")
                + (Utils.isWebForm() ? "&" + CommandConstants.USER__ID + "=" + Utils.getUserID() : "")
        );
    }

    protected String getSessionID() {
        return Cookies.getCookie(SESSION_ID_COOKIE);
    }

    /**
     * Returns the file name from a potential full path argument. Apparently IE
     * insists on sending the full path name of a file when uploading, forcing
     * us to trim the extra path info. Since this is only observed on Windows we
     * get to check for a single path separator value.
     *
     * @param name the potentially full path name of a file
     * @return the file name without extra path information
     */
    protected String getFilename(String name) {
        int pathSepIndex = name.lastIndexOf("\\");
        if (pathSepIndex == -1) {
            pathSepIndex = name.lastIndexOf("/");
            if (pathSepIndex == -1) {
                return name;
            }
        }
        return name.substring(pathSepIndex + 1);
    }

    protected String getBackupFilename(String filename) {
        List<FileResource> filesInSameFolder = new ArrayList<>();
        for (FileResource deleted : folder.getFiles()) {
            if (deleted.isDeleted()) {
                filesInSameFolder.add(deleted);
            }
        }
        int i = 1;
        for (FileResource same : filesInSameFolder) {
            if (same.getName().startsWith(filename)) {
                String toCheck = same.getName().substring(filename.length());
                if (toCheck.startsWith(" ")) {
                    int test = -1;
                    try {
                        test = Integer.valueOf(toCheck.replace(" ", ""));
                    } catch (NumberFormatException e) {
                        // Do nothing since string is not a number.
                    }
                    if (test >= i) {
                        i = test + 1;
                    }
                }
            }
        }

        return filename + " " + i;
    }

    protected FileResource getFileForName(String name) {
        for (FileResource f : folder.getFiles()) {
            if (!f.isDeleted() && f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }

    private String parseResponse(String response, String key) {
        return response.toLowerCase().split("<" + key + ">")[1].split("</" + key + ">")[0];
    }

    /**
     * Rename the conflicting trashed file with the supplied new name.
     */
    private void updateTrashedFile(String newName, FileResource trashedFile, final IUploader uploader) {
        documentsService.updateFile(trashedFile.getObjectId(), newName, null, new ArrayList<>(), new AbstractAsyncCallback() {
            public void success(Object result) {
                uploader.submit();
            }

            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (DuplicateNameException e) {
                    Info.show(wfmStrings.file() + " " + wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (InsufficientPermissionsException e) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                } catch (Throwable e) {

                }
            }
        });
    }

    public void addUploadListener(Command command, String... events) {
        if (events != null && events.length > 0 && command != null) {
            for (String event : events) {
                listeners.put(event, command);
            }
        }
    }

    public void setEnabled(boolean b) {
        defaultUploader.setEnabled(b);
    }
}
