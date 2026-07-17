package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.upload.ImageUploadDialog;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by Hurshid on 4/16/2018.
 */
public class ProfileImage extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private MaterialImage image;
    private MaterialPanel panel = new MaterialPanel("wg_photo-upload wg_photo-upload--uploaded");
    private Div uploadInits = new Div("wg_photo-upload__inits");
    private MaterialPanel imgWrapper = new MaterialPanel("profile-card-img__wrapper profile-card-img__wrapper--square");
    private MaterialPanel imageContainer = new MaterialPanel("user-profile-img profile-card-img");
    private Integer entityID;
    private String type;
    private Integer imageID;
    private MaterialPanel photUploadContent;
    private ImageUploadDialog currentDialog;

    public ProfileImage(Integer entityID, String type) {
        this.entityID = entityID;
        this.type = type;
        imgWrapper.add(imageContainer);
        imageContainer.setVisible(false);

        init();
    }

    public ProfileImage() {
        this(null, "CUSTOM_FIELD_ITEM");
    }

    private void init() {
        Icon icon = new Icon();
        icon.addStyleName("ficon--plus");

        MaterialLink btnDashed = new MaterialLink();
        btnDashed.add(icon);
        btnDashed.addStyleName("btn btn--icon btn--dashed");
        btnDashed.addClickHandler(event -> openModal());

        photUploadContent = new MaterialPanel("wg_photo-upload__content");
        photUploadContent.add(uploadInits);
        photUploadContent.add(btnDashed);
        photUploadContent.add(imgWrapper);

        Div uploadTitle = new Div("wg_photo-upload__title");
        uploadTitle.add(new Span(wfmStrings.uploadaPhoto()));

        panel.add(photUploadContent);
        panel.add(uploadTitle);

        initWidget(panel);
    }

    private void openModal() {
        currentDialog = new ImageUploadDialog(entityID, type);
        currentDialog.open();
    }

    public void initialize(String url, String firstname, String lastname, boolean canChange) {
        initialize(url, firstname, lastname, canChange, null);
    }

    public void initialize(String url, String firstname, String lastname, boolean canChange, String gender) {

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USER_IMAGE_UPLOAD_ADD, ProfileImage.this, (sender, args) -> {
            if (args == null) return;
            if (sender != currentDialog) return;

            SelectItem imageItem = (SelectItem) args;
            this.imageID = imageItem.getId();
            updateImage(imageItem.getName());
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USER_OWN_IMAGE_UPLOAD_ADD, ProfileImage.this, (sender, args) -> {
            if (Utils.getUserID().equals(entityID)) {
                updateImage((String) args);
            }
        });

        if (gender != null) {
            if (gender.equals(Constants.MALE)) {
                panel.addStyleName("genderIs--male");
            } else if (gender.equals(Constants.VACANT)){
                panel.addStyleName("genderIs--hire");
            } else {
                panel.addStyleName("genderIs--female");
            }
        }
        MaterialLink imageButton = new MaterialLink();
        imageButton.addStyleName("profile-card-img__btn");
        imgWrapper.add(imageButton);

        image = new MaterialImage();
        imageContainer.add(image);
        if (url == null) {
            String name = "";
            if (firstname != null && !"".equals(firstname.trim())) {
                name = firstname.substring(0, 1);
            }
            if (lastname != null && !"".equals(lastname.trim())) {
                name += lastname.substring(0, 1);
            }
            panel.removeStyleName("wg_photo-upload--uploaded");
            Span span = new Span(name.toUpperCase());
            uploadInits.add(span);
        } else {
            image.setUrl(url);
            image.addLoadHandler(event -> imageContainer.setVisible(true));
        }

        Span span = new Span(wfmStrings.change());
        Icon icon = new Icon();
        icon.addStyleName("ficon--camera");
        if (canChange) {
            imageButton.add(icon);
            imageButton.add(span);
            imageButton.addClickHandler(event -> openModal());
        } else {
            panel.addStyleName("no-permissions");
        }
    }

    private void updateImage(String args) {
        imageContainer.setVisible(false);
        image.setUrl(args);

        image.addLoadHandler(event -> imageContainer.setVisible(true));

        if (!panel.getStyleName().contains("wg_photo-upload--uploaded")) {
            panel.addStyleName("wg_photo-upload--uploaded");
        }
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public void getImageById(Integer imageID, String firstName, String lastName, boolean hasChangeAccess) {
        if (imageID != null) {
            CommonService.App.get().getImageUrl(imageID, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    //
                }

                @Override
                public void success(String imageUrl) {
                    initialize(imageUrl, firstName, lastName, hasChangeAccess);
                }
            });
        } else {
            initialize(null, firstName, lastName, hasChangeAccess);
        }

    }
}