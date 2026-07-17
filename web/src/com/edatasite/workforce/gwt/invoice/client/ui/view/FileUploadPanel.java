package com.edatasite.workforce.gwt.invoice.client.ui.view;


import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ImageViewerPopup;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.KpiFileUtils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.upload.GWTFileUploadDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/8/12
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadPanel extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    private static FileUploadPanelUiBinder ourUiBinder = GWT.create(FileUploadPanelUiBinder.class);
    @UiField
    Label label;
    @UiField
    Div filePanel;
    @UiField
    Div attachButton;
    @UiField
    Italic attachIcon;
    HashMap<Integer, FileResource> uploadedFiles = new HashMap<>();
    private GWTFileUploadDialog fileUploadDialog;

    private Integer uploadType;
    private Integer objectID;

    private Integer fromUploadType;
    private Integer fromObjectID;

    private boolean editable;
    private boolean downloadable;

    public FileUploadPanel(Integer uploadType, Integer objectID) {
        this(uploadType, objectID, true);
    }

    public FileUploadPanel(Integer uploadType, Integer objectID, boolean editable) {
        this(uploadType, objectID, editable, null);
    }

    public FileUploadPanel(Integer uploadType, Integer objectID, boolean editable, String label) {
        this(uploadType, objectID, editable, true, label);
    }

    public FileUploadPanel(Integer uploadType, Integer objectID, boolean editable, boolean downloadable, String label) {
        this(uploadType, objectID, null, null, editable, downloadable, label);
    }

    public FileUploadPanel(Integer uploadType, Integer objectID, Integer fromUploadType, Integer fromObjectID, boolean editable, boolean downloadable, String label) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.uploadType = uploadType;
        this.objectID = objectID;
        this.editable = editable;
        this.downloadable = downloadable;
        this.fromUploadType = fromUploadType;
        this.fromObjectID = fromObjectID;
        this.label.setText(label);
        initialize();
    }

    public void initialize() {
        fileUploadDialog = new GWTFileUploadDialog(uploadType, objectID, objectID);
        fileUploadDialog.onLoadCommand(this::refreshFilePanel);
        fileUploadDialog.setActivator(attachButton);

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
                    addFilesToPanel(fileResources);
                    if (label != null && fileResources != null && fileResources.size() > 0) {
                        label.setVisible(true);
                    }
                }
            });
        }
        attachButton.setVisible(editable);
        attachIcon.setVisible(editable);
    }

    private void refreshFilePanel() {
        if (objectID == null) {
            addFilesToPanel(getFileResources());
        } else {
            LoadingPanel.loading(true);
            DocumentsService.App.get().getFileResources(uploadType, objectID, objectID, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(ArrayList<FileResource> fileResources) {
                    addFilesToPanel(fileResources);
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

    public void addFilesToPanel(ArrayList<FileResource> fileResources) {
        filePanel.clear();

        fileResources.sort((o1, o2) -> o2.getObjectId().compareTo(o1.getObjectId()));

        for (FileResource fr : fileResources) {
            filePanel.add(new FileItemPanel(fr));
        }
        setHasFiles(!fileResources.isEmpty());
    }

    private void setHasFiles(boolean hasFiles) {
        if (hasFiles) {
            filePanel.setVisible(true);
            attachButton.setClass("btn-upload btn-upload--plus");
            attachIcon.setClass("ficon--plus");
        } else {
            filePanel.setVisible(false);
            attachButton.setClass("btn-upload");
            attachIcon.setClass("ficon--attachment");
        }
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

    interface FileUploadPanelUiBinder extends UiBinder<Widget, FileUploadPanel> {
    }

    private class FileItemPanel extends MaterialPanel {
        Div container;
        Div progressIndicator = new Div("btn-upload__indicator active");
        Italic icon = new Italic("");
        Italic iconClose = new Italic("");
        private FileResource item;

        private FileItemPanel(FileResource item) {
            super();

            setClass("btn-uploaded-group");

            container = new Div("btn-uploaded");
            add(container);

            iconClose.setClass("close");
            container.add(icon);

            if (editable) {
                iconClose.addClickHandler(clickEvent -> deleteFile(item));
                container.add(iconClose);
            }
            container.add(progressIndicator);

            //if fileItem not null then show icon
            if (item != null) {

                Span fileTitle = new Span(item.getFileName());
                fileTitle.setClass("btn-uploaded__title");
                add(fileTitle);

                setItem(item);
            } else {
                //else show display progress
                progressIndicator.setVisible(true);
            }

            initialize();
        }

        private void initialize() {
            //icon = new Italic(item.getName());

            if (downloadable) {
                icon.addClickHandler(clickEvent -> {
                    String action = item.getDownloadUrl();
                    FileResource file = item;

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
        }

        public FileResource getItem() {
            return item;
        }

        public void setItem(FileResource item) {
            this.item = item;
            if (item != null && item.getFileName() != null && item.getFileName().indexOf(".") > -1) {
                String fileExtension = item.getFileName().substring(item.getFileName().lastIndexOf(".") + 1);
                String iconClass = KpiFileUtils.getFileIconByExtension(fileExtension);
                if (iconClass != null) {
                    icon.setClass(iconClass);
                } else {
                    icon.setClass("ficon--attachment");
                }

                progressIndicator.removeFromParent();
            } else {
                icon.setClass("ficon--attachment");
            }
        }
    }
}
