package com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.image;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.MULTIPLE_FILES;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.SESSION_ID_PARAM_NAME;
import static gwt.material.design.client.js.JsMaterialElement.$;

/**
 * Created by Anvar Akramov on 12/13/17.
 */
public class KpiImageUploadForm extends Composite implements Constants {
    interface KpiImageUploadFormUiBinder extends UiBinder<Widget, KpiImageUploadForm> {
    }

    private static KpiImageUploadFormUiBinder ourUiBinder = GWT.create(KpiImageUploadFormUiBinder.class);
    @UiField
    Div uploadedFilesContainer;
    @UiField
    Div attachButtonContainer;
    @UiField
    Italic uploadBtnIcon;
    @UiField
    FileUpload uploadFile;
    @UiField
    WfmFormPanel form;


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer width;
    private Integer height;
    private boolean multi = true;
    private boolean dynamicWidth = false;
    ArrayList<KpiImageFile> uploadedFiles = new ArrayList<>();

    public KpiImageUploadForm(int width, int height, boolean multi,  boolean dynamicWidth) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.width = width;
        this.height = height;
        this.multi = multi;
        this.dynamicWidth = dynamicWidth;
        initForm();
    }

    public KpiImageUploadForm(int width, int height, boolean multi) {
        this(width, height, multi, false);
    }

    public KpiImageUploadForm(int width, int height) {
        this(width, height, true);
    }

    private void initForm() {
        setHasFiles(false);
        attachButtonContainer.addClickHandler(clickEvent -> {
            if (uploadedFiles.size() == 0) {
                $(uploadFile.getElement()).trigger("click", null);
            }
        });

        form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
        if (!dynamicWidth) {
            form.setParameter(CommandConstants.IMAGE_WIDTH, width + "");
        }
        form.setParameter(CommandConstants.IMAGE_HEIGHT, height + "");
        if (multi) {
            form.setParameter(MULTIPLE_FILES, "true");
        } else {
            form.setParameter(CommandConstants.DESCRIPTION_PARAM_NAME, "");
            form.setParameter(CommandConstants.UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
        }
        uploadFile.setName(ATTACHMENT_PARAM_BASE + 0);

        form.addSubmitCompleteHandler(event -> {
            Integer id = form.getObjectID();
            if (id != null || form.isSuccess()) {
                try {
                    Integer attachmentId = Integer.parseInt(form.getReturnValue());
                    MessageCenterService.App.get().getAttachedFilesByAttachmentId(attachmentId, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                        public void success(final ArrayList<FileResource> files) {
                            for (FileResource file : files) {
                                if (file != null) {
                                    KpiImageFile kpiFile = uploadedFiles.get(uploadedFiles.size() - 1);
                                    kpiFile.setDone(true);
                                    kpiFile.setFile(file);
                                    uploadFile.getElement().setPropertyString("value", "");
                                }
                            }
                            setHasFiles(uploadedFiles.size() > 0);
                        }
                    });
                } catch (Exception e) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                    messageBox.setTitle(wfmStrings.error());
                    if (form.getErrorString() == null) {
                        messageBox.setMessage(wfmStrings.errorOnUploadingDocument());
                    } else {
                        messageBox.setMessage(form.getErrorString());
                    }
                    messageBox.open();
                }
            } else {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                messageBox.setTitle(wfmStrings.error());
                if (form.getErrorString() == null) {
                    messageBox.setMessage(wfmStrings.errorOnUploadingDocument());
                } else {
                    messageBox.setMessage(form.getErrorString());
                }
                messageBox.open();
            }
            //SHOW or HIDE divs
            setHasFiles(uploadedFiles.size() > 0);
        });
        uploadFile.addChangeHandler(valueChangeEvent -> {
            if ("".equals(uploadFile.getFilename())) {
                Info.show(wfmStrings.chooseFile(), Info.Type.WARNING);
            } else {
                if (uploadFile.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                        uploadFile.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                        uploadFile.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                        uploadFile.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                        uploadFile.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                        uploadFile.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                    form.setParameter(CommandConstants.IMAGE_TYPE, uploadFile.getFilename().substring(uploadFile.getFilename().toLowerCase().lastIndexOf(".") + 1));
                } else {
                    Info.show(wfmStrings.thisNotImage(), Info.Type.WARNING);
                }
            }
            if (uploadedFiles.size() > 0) {
                return;
            }
            KpiImageFile kpiFile = new KpiImageFile(dynamicWidth);
            kpiFile.addCloseHandler(closeEvent -> {
                if (uploadedFilesContainer != null) {
                    uploadedFilesContainer.remove(kpiFile);
                }
                if (uploadedFiles.contains(kpiFile)) {
                    uploadedFiles.remove(kpiFile);
                }
                setHasFiles(uploadedFiles.size() > 0);
            });
            //Display on UI
            uploadedFilesContainer.add(kpiFile);
            //Keep references
            uploadedFiles.add(kpiFile);
            setHasFiles(true);

            $(form.getElement()).trigger("submit", null);
        });

    }

    public void addImage(Integer imageId) {
        MessageCenterService.App.get().getAttachedFilesByAttachmentId(imageId, new AbstractAsyncCallback<ArrayList<FileResource>>() {
            public void success(final ArrayList<FileResource> files) {
                for (FileResource file : files) {
                    KpiImageFile kpiFile = new KpiImageFile(dynamicWidth);
                    kpiFile.setDone(true);
                    kpiFile.setFile(file);
                    addFile(kpiFile);
                    uploadFile.getElement().setPropertyString("value", "");
                }
//                setHasFiles(uploadedFiles.size() > 0);
            }
        });
    }

    private void setHasFiles(boolean hasFiles) {
        if (hasFiles) {
            uploadedFilesContainer.setVisible(true);
            attachButtonContainer.setClass("btn-upload btn-upload--plus");
            uploadBtnIcon.setClass("ficon--plus");
            attachButtonContainer.setVisible(false);//temp solution
        } else {
            uploadedFilesContainer.setVisible(false);
            attachButtonContainer.setClass("btn-upload");
            uploadBtnIcon.setClass("ficon--attachment");
            attachButtonContainer.setVisible(true);//temp solution
        }
    }

    public void addFile(KpiImageFile kpiFile) {
        kpiFile.addCloseHandler(closeEvent -> {
            if (uploadedFilesContainer != null) {
                uploadedFilesContainer.remove(kpiFile);
            }
            if (uploadedFiles.contains(kpiFile)) {
                uploadedFiles.remove(kpiFile);
            }
            setHasFiles(uploadedFiles.size() > 0);
        });
        uploadedFilesContainer.add(kpiFile);
        uploadedFiles.add(kpiFile);
        setHasFiles(true);
    }

    public void setParameter(String name, String value) {
        form.setParameter(name, value, false);
    }

    public ArrayList<KpiImageFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public boolean isFinished() {
        for (KpiImageFile uploadFile : uploadedFiles) {
            if (!uploadFile.isDone()) {
                return false;
            }
        }
        return true;
    }

    public Integer getImageId() {
        if (getUploadedFiles() != null && getUploadedFiles().size() > 0 && getUploadedFiles().get(0).getFile() != null) {
            return getUploadedFiles().get(0).getFile().getBodyId();
        }
        return null;
    }

    public void clearFiles() {
        for (KpiImageFile uploadFile : uploadedFiles) {
            try {
                uploadedFilesContainer.remove(uploadFile);
            } catch (Exception e) {
                GWT.log("", e);
            }
        }
        uploadedFiles.clear();
    }
}
