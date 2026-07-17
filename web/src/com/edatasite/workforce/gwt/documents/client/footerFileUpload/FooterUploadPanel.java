package com.edatasite.workforce.gwt.documents.client.footerFileUpload;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ImageViewerPopup;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileWidget;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.HashMap;

public class FooterUploadPanel extends FooterInformer {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    HashMap<Integer, FileResource> uploadedFiles = new HashMap<>();
    private InDatabaseFileWidgetProvider inDatabaseFileWidgetProvider = new InDatabaseFileWidgetProvider();
    private FooterGWTFileUploadDialog fileUploadDialog;
    private Integer uploadType;
    private Integer objectID;
    private Integer fromUploadType;
    private Integer fromObjectID;
    private boolean editable;
    private boolean downloadable;

    public FooterUploadPanel(Integer uploadType, Integer objectID) {
        this(uploadType, objectID, true);
    }

    public FooterUploadPanel(Integer uploadType, Integer objectID, boolean editable) {
        this(uploadType, objectID, editable, null);
    }

    public FooterUploadPanel(Integer uploadType, Integer objectID, boolean editable, String label) {
        this(uploadType, objectID, editable, true, label);
    }

    public FooterUploadPanel(Integer uploadType, Integer objectID, boolean editable, boolean downloadable, String label) {
        this(uploadType, objectID, null, null, editable, downloadable, label);
    }

    public FooterUploadPanel(Integer uploadType, Integer objectID, Integer fromUploadType, Integer fromObjectID, boolean editable, boolean downloadable, String label) {
        super(SvgEnum.uploadCloud, wfmStrings.attachments(), null);
        this.uploadType = uploadType;
        this.objectID = objectID;
        this.editable = editable;
        this.downloadable = downloadable;
        this.fromUploadType = fromUploadType;
        this.fromObjectID = fromObjectID;
        initialize();
    }

    public void initialize() {
        fileUploadDialog = createFileUploadDIalog();
        if (editable) {
            fileUploadDialog.setActivator(this);
        }
        Integer upload_ = fromUploadType == null ? uploadType : fromUploadType;
        Integer id_ = fromObjectID == null ? objectID : fromObjectID;

        if (objectID != null || fromObjectID != null) {
            DocumentsService.App.get().getFileResources(upload_, id_, id_, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(ArrayList<FileResource> fileResources) {
                    setFilesToPanel(fileResources);
                }
            });
        }
    }

    public FooterGWTFileUploadDialog createFileUploadDIalog() {
        FooterGWTFileUploadDialog result = new FooterGWTFileUploadDialog(uploadType, objectID, objectID, inDatabaseFileWidgetProvider);
        result.onLoadCommand(this::refreshFilePanel);
        return result;
    }

    private void refreshFilePanel() {
        if (objectID == null) {
            setFilesToPanel(getFileResources());
        } else {
            LoadingPanel.loading(true);
            DocumentsService.App.get().getFileResources(uploadType, objectID, objectID, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(ArrayList<FileResource> fileResources) {
                    setFilesToPanel(fileResources);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private void deleteFile(final FileResource file) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        messageBox.setMessage(wfmStrings.areYouSureDeleteThisDocument());
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                CommonService.App.get().deleteFile(file.getObjectId(), new AbstractAsyncCallback<Void>() {
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    public void success(Void result) {
                        fileUploadDialog.getUploadedFiles().remove(file.getObjectId());
                        uploadedFiles.remove(file.getObjectId());
                        refreshFilePanel();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.documents()), Info.Type.INFO);
                    }
                });
            }
        });
        messageBox.open();
    }

    public ArrayList<FileResource> getFileResources() {
        ArrayList<FileResource> fileResources = new ArrayList<>();

        if (fileUploadDialog.getUploadedFiles() != null) {
            uploadedFiles.putAll(fileUploadDialog.getUploadedFiles());
        }
        if (uploadedFiles != null) {
            fileResources.addAll(uploadedFiles.values());
        }
        return fileResources;
    }

    public void setFilesToPanel(ArrayList<FileResource> fileResources) {
        setBadgeCount(fileResources.size());
        fileToPanel(fileResources);
    }

    private void fileToPanel(ArrayList<FileResource> fileResources) {
        fileResources.sort((o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));

        ArrayList<FileWidget> files = new ArrayList<>();
        for (FileResource file : fileResources) {
            FileWidget fileWidget = new FileWidget(String.valueOf(file.getObjectId()), file.getName());
            if (downloadable) {
                fileWidget.addClickHandler(clickEvent -> {
                    Element clickedElement = (Element) clickEvent.getNativeEvent().getEventTarget().cast();
                    // Check if the clicked element is not the delete button
                    if (!clickedElement.getClassName().contains("kpi-upload__upload-file-delete") &&
                            !clickedElement.getClassName().contains("ficon--close")) {
                        String action = file.getDownloadUrl();
                        FileResource file_ = file;

                        if (file_.getBodyId() != null) {
                            if (!Constants.GOOGLE.equals(file_.getUploadType()) && !Constants.OFFICE_365.equals(file_.getUploadType()) && !Constants.OFFICE_365_SHARE_POINT.equals(file_.getUploadType())) {
                                String fileName = file_.getName().toLowerCase();
                                GWT.log(file_.getContentType());
                                GWT.log("" + fileName.endsWith(".pdf"));
                                if (file_.getContentType() != null && file_.getContentType().startsWith("image/") ||
                                        fileName.endsWith(".jpe") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                                        fileName.endsWith(".ico") || fileName.endsWith(".png") || fileName.endsWith(".bmp") || fileName.endsWith(".gif")) {
                                    ImageViewerPopup popup = new ImageViewerPopup(file_.getName(), action);
                                    popup.open();
                                    return;
                                } else if (file_.getName().contains("Approved_By_") && fileName.endsWith(".pdf")) {
                                    Window.open(action, "_blank", "");
                                    return;
                                } else if (file_.getContentType() != null && fileName.endsWith(".pdf")) {
                                    CommonService.App.get().getDynamicImageUrl(file_.getBodyId(), new AbstractAsyncCallback<String>() {
                                        @Override
                                        public void onFailure(Throwable caught) {
                                            Window.open(action, "_blank", "");
                                        }

                                        @Override
                                        public void onSuccess(String result) {
                                            Window.open(result, "_blank", "");
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                        Window.open(action, "_blank", "");
                    }   });
            }
            fileWidget.setCompleted(100);
            fileWidget.setRemoveCommand(createRemoveComand(file));
            files.add(fileWidget);
        }
        inDatabaseFileWidgetProvider.setFileItems(files);
    }


    private Command createRemoveComand(FileResource file) {
        return () -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.warning());
            messageBox.setMessage(wfmStrings.areYouSureDeleteThisDocument());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    CommonService.App.get().deleteFile(file.getObjectId(), new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Void result) {
                            fileUploadDialog.getUploadedFiles().remove(file.getObjectId());
                            uploadedFiles.remove(file.getObjectId());
                            refreshFilePanel();
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.documents()), Info.Type.INFO);
                        }
                    });
                }
            });
            messageBox.open();
        };
    }

    public FileItem[] getAttachedFiles() {
        ArrayList<FileResource> resources = getFileResources();
        FileItem[] attachments = new FileItem[resources.size()];
        int i = 0;
        for (FileResource file : resources) {
            attachments[i] = new FileItem();
            attachments[i].setId(file.getObjectId());
            attachments[i].setFileName(file.getName());
            i++;
        }
        return attachments;
    }
}

