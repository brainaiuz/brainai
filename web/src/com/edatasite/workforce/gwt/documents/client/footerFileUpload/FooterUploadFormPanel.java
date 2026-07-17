package com.edatasite.workforce.gwt.documents.client.footerFileUpload;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ImageViewerPopup;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileWidget;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.base.MaterialWidget;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dilshod Madrahimov on 02/20/19.
 */

/**
 * This is a new version of KpiFileUploadForm that mostly used for message center.
 * Email template attachments considers as old attachments.
 * New uploaded attachments stores to "uploadedFiles" map.
 * While sending messages, old and new attachments together will be sent.
 * This upload panel is similar to FooterUploadPanel. FooterUploadPanel works with unique folder id like F_TASK or F_CASE. After upload attachments to this folder, old attachments will be cleared and gets all the folder's attachments from DB
 */
public class FooterUploadFormPanel extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private HashMap<Integer, FileResource> uploadedFiles = new HashMap<>();
    private InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider = new InDatabaseFileWidgetProvider();
    private FooterGWTFileUploadDialog fileUploadDialog;
    private Integer uploadType;
    private Integer objectID;
    private boolean editable;
    private boolean downloadable;
    private FooterInformer footerWrapper;
    private ArrayList<FileResource> oldAttachments = new ArrayList<>();

    public FooterUploadFormPanel(Integer uploadType) {
        this(uploadType, null);
    }

    public FooterUploadFormPanel(Integer uploadType, Integer objectID) {
        this(uploadType, objectID, true);
    }

    public FooterUploadFormPanel(Integer uploadType, Integer objectID, boolean editable) {
        this(uploadType, objectID, editable, true);
    }

    public FooterUploadFormPanel(Integer uploadType, Integer objectID, boolean editable, boolean downloadable) {
        this.uploadType = uploadType;
        this.objectID = objectID;
        this.editable = editable;
        this.downloadable = downloadable;
        initialize();
    }

    public void initialize() {
        fileUploadDialog = createFileUploadDIalog();
    }

    public void setActivator(MaterialWidget widget) {
        if (editable) {
            fileUploadDialog.setActivator(widget);
        }
        if (widget instanceof FooterInformer) {
            this.footerWrapper = (FooterInformer) widget;
        }
    }

    private FooterGWTFileUploadDialog createFileUploadDIalog() {
        FooterGWTFileUploadDialog result = new FooterGWTFileUploadDialog(uploadType, objectID, objectID, inDatabaseFileWidgetProvider);
        result.onLoadCommand(this::refreshFilePanel);
        return result;
    }

    /**
     * Refresh upload panel after attachment is uploaded
     */
    private void refreshFilePanel() {
        addFilesToPanel(getUploadedFileResources());
    }

    private ArrayList<FileResource> getUploadedFileResources() {
        ArrayList<FileResource> fileResources = new ArrayList<>();

        if (fileUploadDialog.getUploadedFiles() != null) {
            uploadedFiles.putAll(fileUploadDialog.getUploadedFiles());
        }
        if (uploadedFiles != null) {
            fileResources.addAll(uploadedFiles.values());
        }

        return fileResources;
    }

    /**
     * Get new and old attachments(when entity has attachments) to save
     *
     * @return
     */
    public ArrayList<FileResource> getFileResources() {
        ArrayList<FileResource> fileResources = getUploadedFileResources();
        if (!oldAttachments.isEmpty()) {
            fileResources.addAll(oldAttachments);
        }
        return fileResources;
    }

    /**
     * When entity is edited the entity attachments will be set to upload panel and assigned to old attachments
     *
     * @param fileResources
     */
    public void setFilesToPanel(ArrayList<FileResource> fileResources) {
        footerWrapper.setBadgeCount(fileResources.size());
        fileToPanel(fileResources);
        this.oldAttachments = fileResources;
    }

    /**
     * New uploaded attachments will be set to upload panel and keeps old attachments also
     *
     * @param fileResources
     */
    private void addFilesToPanel(ArrayList<FileResource> fileResources) {
        footerWrapper.setBadgeCount(fileResources.size());
        fileToPanel(fileResources);
    }

    /**
     * New Attachments and then old attachments will be set to upload panel
     *
     * @param fileResources
     */
    private void fileToPanel(ArrayList<FileResource> fileResources) {
        fileResources.sort((o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));

        ArrayList<FileWidget> files = new ArrayList<>();
        for (FileResource file : fileResources) {
            FileWidget fileWidget = new FileWidget(String.valueOf(file.getObjectId()), file.getName());
            fileWidget.addClickHandler(e -> {
                if (downloadable) {
                    fileWidget.addClickHandler(clickEvent -> {
                        String action = file.getDownloadUrl();

                        if (file.getBodyId() != null) {
                            if (!Constants.GOOGLE.equals(file.getUploadType()) && !Constants.OFFICE_365.equals(file.getUploadType()) && !Constants.OFFICE_365_SHARE_POINT.equals(file.getUploadType())) {
                                String fileName = file.getName().toLowerCase();
                                if (file.getContentType() != null && file.getContentType().startsWith("image/") ||
                                        fileName.endsWith(".jpe") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                                        fileName.endsWith(".ico") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".gif")) {
                                    ImageViewerPopup popup = new ImageViewerPopup(file.getName(), action);
                                    popup.open();
                                    return;
                                }
                            }
                        }
                        Window.open(action, "_blank", "");
                    });
                }
            });
            fileWidget.setCompleted(100);
            fileWidget.setRemoveCommand(createRemoveCommand(file));
            files.add(fileWidget);
        }
        if (oldAttachments.size() > 0) {
            for (FileResource file : oldAttachments) {
                FileWidget fileWidget = new FileWidget(String.valueOf(file.getObjectId()), file.getName());
                fileWidget.addClickHandler(e -> {
                    if (downloadable) {
                        fileWidget.addClickHandler(clickEvent -> {
                            String action = file.getDownloadUrl();
                            if (file.getBodyId() != null) {
                                if (!Constants.GOOGLE.equals(file.getUploadType()) && !Constants.OFFICE_365.equals(file.getUploadType()) && !Constants.OFFICE_365_SHARE_POINT.equals(file.getUploadType())) {
                                    String fileName = file.getName().toLowerCase();
                                    if (file.getContentType() != null && file.getContentType().startsWith("image/") ||
                                            fileName.endsWith(".jpe") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                                            fileName.endsWith(".ico") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".gif")) {
                                        ImageViewerPopup popup = new ImageViewerPopup(file.getName(), action);
                                        popup.open();
                                        return;
                                    }
                                }
                            }
                            Window.open(action, "_blank", "");
                        });
                    }
                });
                fileWidget.setCompleted(100);
                fileWidget.setRemoveCommand(createRemoveCommand(file));
                files.add(fileWidget);
            }
        }
        footerWrapper.setBadgeCount(files.size());
        inDatabaseFileWidgetProvider.setFileItems(files);
    }

    /**
     * Delete provided file. If the file is old attachment, it will be removed from old attachments list
     *
     * @param file
     * @return
     */
    private Command createRemoveCommand(FileResource file) {
        return () -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.areYouSureDeleteThisDocument());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    if (file.isEmailTemplateAttachment()) {
                        fileUploadDialog.getUploadedFiles().remove(file.getObjectId());
                        uploadedFiles.remove(file.getObjectId());
                        if (oldAttachments.contains(file)) {
                            oldAttachments.remove(file);
                        }
                        refreshFilePanel();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.documents()), Info.Type.INFO);
                    } else {
                        CommonService.App.get().deleteFile(file.getObjectId(), new AbstractAsyncCallback<Void>() {
                            public void failure(Throwable caught) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            public void success(Void result) {
                                fileUploadDialog.getUploadedFiles().remove(file.getObjectId());
                                uploadedFiles.remove(file.getObjectId());
                                if (oldAttachments.contains(file)) {
                                    oldAttachments.remove(file);
                                }
                                refreshFilePanel();
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.documents()), Info.Type.INFO);
                            }
                        });
                    }
                }
            });
            messageBox.open();
        };
    }

    /**
     * Clear all stores
     * Clear count icon
     */
    public void clearFiles() {
        oldAttachments.clear();
        inDatabaseFileWidgetProvider.clear();
        uploadedFiles.clear();
        footerWrapper.setBadgeCount(null);

    }
}
