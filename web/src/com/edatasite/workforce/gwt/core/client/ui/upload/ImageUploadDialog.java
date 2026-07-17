package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.code.gwt.crop.client.GWTCropper;
import com.google.code.gwt.crop.client.GWTCropperPreview;
import com.google.code.gwt.crop.client.common.Dimension;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Abror Abdukadirov
 * Date: 17.01.2018 16:06
 */
public class ImageUploadDialog extends KpiModal {



    private WfmFormPanel formPanel;
    private FileUpload imageUploadForm;
    private TextArea description;
    private TextBox uploadType;
    private GWTCropper crop;
    private Integer imageID;
    private final Integer id;
    private String type;
    private FileResource fileResource;
    private ExtendedItemUploadForm uploadForm;
    private Boolean fromExtendedUploadForm = false;

    public ImageUploadDialog(Integer itemID, ExtendedItemUploadForm uploadForm) {
        super();
        fromExtendedUploadForm = true;
        this.id = itemID;
        this.uploadForm = uploadForm;
        init();
    }

    public ImageUploadDialog(Integer employeeID, String type) {
        super();
        this.id = employeeID;
        this.type = type;
        init();
    }

    public void init() {
        setWidth("400px");
        setTitle(wfmStrings.uploadaPhoto());
        setDismissible(false);

        formPanel = new WfmFormPanel("/CreateAttachment");

        imageUploadForm = new FileUpload();
        imageUploadForm.setWidth("200px");
        imageUploadForm.setName(CommandConstants.ATTACHMENT_PARAM_BASE + "0");

        TextArea description = new TextArea();
        description.setName(CommandConstants.DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(CommandConstants.UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        HorizontalPanel hp = new HorizontalPanel();
        hp.add(imageUploadForm);
        hp.add(description);
        hp.add(uploadType);
        VerticalPanel vp = new VerticalPanel();
        vp.add(hp);

        formPanel.setWidget(vp);

        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setVisible(false);

        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 skipButton = new WfmButton2(wfmStrings.skip(), WfmButton2.BTN_PRIMARY);
        WfmButton2 uploadButton = new WfmButton2(WfmStrings.App.get().uploadImage(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancelButton = new WfmButton2(WfmStrings.App.get().cancel(), WfmButton2.BTN_DEFAULT);

        cancelButton.addClickHandler(event -> close());

        uploadButton.addClickHandler(event -> {
            if (imageUploadForm.getFilename() != null && !imageUploadForm.getFilename().equals("")) {
                if (imageUploadForm.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                        imageUploadForm.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                        imageUploadForm.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                        imageUploadForm.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                        imageUploadForm.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                        imageUploadForm.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                    formPanel.setParameter(CommandConstants.IMAGE_TYPE, imageUploadForm.getFilename().substring(imageUploadForm.getFilename().toLowerCase().lastIndexOf(".") + 1));
                    formPanel.setParameter(CommandConstants.ATTACHMENT_FOLDER, "static");
                    formPanel.setParameter(CommandConstants.NOTDOWNLOADABLE, "YES");
                    formPanel.setParameter(CommandConstants.WITHOUT_RESIZE, "true");
                    //formPanel.setParameter(CommandConstants.XandY, "0&30");
                    formPanel.submit();
                    uploadButton.setEnabled(false);
                    LoadingPanel.loading(true, this);
                } else {
                    Info.show(wfmStrings.provideImageType(), Info.Type.WARNING);
                }
            } else {
                Info.show(wfmStrings.pleaseChooseImage(), Info.Type.WARNING);
            }
        });

        GWTCropperPreview cropperPreview = new GWTCropperPreview(Dimension.WIDTH, 100);
        MaterialPanel preview = new MaterialPanel("profile-image--right");
        preview.setWidth("105px");
        MaterialPanel prevImage = new MaterialPanel("profile-imagePreview--preview");
        MaterialPanel prevText = new MaterialPanel("profile-imagePreview--text");

        prevImage.add(cropperPreview);
        prevText.add(new HTML(wfmStrings.previewImageUpload()));

        preview.add(prevImage);
        final Double[] width = {null}, originalWidth = {null};
        final Double[] height = {null}, originalHeight = { null };
        formPanel.addSubmitCompleteHandler(event -> {
            imageID = formPanel.getObjectID();
            if (formPanel.isSuccess()) {
                Info.show(wfmStrings.successfullyUploaded());
                LoadingPanel.loading(true, this);

                CommonService.App.get().getFileUrl(formPanel.getObjectID(), null, true, true, new AbstractAsyncCallback<String[]>() {
                    @Override
                    public void success(String[] result) {
                        clearContent();
                        clearFooter();
                        crop = new GWTCropper(result[0]);
                        crop.setAspectRatio((double) 10 / 11);

                        width[0] = result[1] != null ? Double.parseDouble(result[1]) : 200;
                        height[0] = result[2] != null ? Double.parseDouble(result[2]) : 220;
                        originalWidth[0] = result[1] != null ? Double.parseDouble(result[1]) : 200;
                        originalHeight[0] = result[2] != null ? Double.parseDouble(result[2]) : 220;

                        double scaleX = 1, scaleY = 1;
                        Integer imageWidth = result[1] != null ? Integer.parseInt(result[1]) : null;
                        if (width[0] > 300d || height[0] > 330d) {
                            scaleX = width[0] / 300d;
                            scaleY = height[0] / 330d;
                            if (scaleX >= scaleY) {
                                width[0] = 300d;
                                height[0] = height[0] / scaleX;
                            } else {
                                height[0] = 330d;
                                width[0] = width[0] / scaleY;
                            }
                        }

                        setWidth("700px");
                        setMaxHeight("85%");

                        crop.setSize(width[0].intValue(), height[0].intValue());
                        crop.setInitialSelection(10, 10, width[0].intValue(), height[0].intValue());
                        crop.registerPreviewWidget(cropperPreview);

                        horizontalPanel.add(crop);
                        preview.add(prevText);
                        horizontalPanel.add(preview);
                        add(horizontalPanel);


                        getElement().getStyle().setTop(5, Style.Unit.PCT);
                        crop.addCanvasLoadHandler(event1 -> {
                            horizontalPanel.setVisible(true);
                            LoadingPanel.loading(false);
                            addButton(cancelButton);
                            addButton(skipButton);
                            addButton(save);
                        });
                    }
                });
            } else {
                Info.warn(wfmStrings.errorOccurredUpdate());
                LoadingPanel.loading(false, this);
                uploadButton.setEnabled(true);
            }
        });

        save.addClickHandler(clickEvent -> {
            saveImage(save, null, null);
        });

        skipButton.addClickHandler(clickEvent -> {
            saveImage(skipButton, originalWidth[0], originalHeight[0]);
        });

        addWidget(formPanel, null);
        addWidget(new Span(wfmStrings.youCanUploadaJPGGIFBMPorPNGfile()), null);
        addWidget(new Span(wfmStrings.minimumSize()), null);

        addButton(cancelButton);
        addButton(uploadButton);
    }

    private void saveImage(WfmButton2 wfmButton, Double width, Double height) {
        wfmButton.setEnabled(false);

        boolean skip = width != null && height != null;
        int x = skip ? 0 : crop.getSelectionXCoordinate();
        int y = skip ? 0 : crop.getSelectionYCoordinate();
        int imageWidth = skip ? width.intValue() : crop.getSelectionWidth();
        int imageHeight = skip ? height.intValue() : crop.getSelectionHeight();

        if (imageID != null) {
            if (!fromExtendedUploadForm) {
                LoadingPanel.loading(true, this);
                CommonService.App.get().saveCroppedImage(id, type, imageID, x, y, imageWidth, imageHeight, new AbstractAsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        wfmButton.setEnabled(false);
                    }

                    public void success(String result) {
                        LoadingPanel.loading(false);
                        close();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.profilePicture()));

                        if (type == null || (id != null && id.equals(Utils.getUserID()))) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_USER_OWN_IMAGE_UPLOAD_ADD, result, ImageUploadDialog.this);
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_USER_IMAGE_UPLOAD_ADD, new SelectItem(imageID, result), ImageUploadDialog.this);
                        }

                    }
                });

            } else {
                CommonService.App.get().saveItemCroppedImage(imageID, id, Constants.F_SALE_QUOTE_ITEM, x, y, imageWidth, imageHeight, new AbstractAsyncCallback<FileResource>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        wfmButton.setEnabled(false);
                    }

                    public void success(FileResource result) {
                        fileResource = result;
                        LoadingPanel.loading(false);
                        close();
                        Info.show("Image saved successfully", Info.Type.INFO);
                        uploadForm.setFile(result);
                    }
                });
            }
        }
    }
}
