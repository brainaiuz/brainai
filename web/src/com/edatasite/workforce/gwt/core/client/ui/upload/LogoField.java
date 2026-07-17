package com.edatasite.workforce.gwt.core.client.ui.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import static gwt.material.design.client.js.JsMaterialElement.$;

/**
 * User: jamshid.asatillayev
 * Date: Jun 13, 2011
 * Time: 6:11:59 PM
 */
public abstract class LogoField extends Composite implements Constants, CommandConstants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public WfmFormPanel form;
    public LogoFileUpload logoUpload;
    private MaterialImage logoImage;
    private MaterialPanel imageContainer;
    private MaterialPanel imageWrapper;
    private final String logoType;

    public LogoField(String logoType) {
        this.logoType = logoType;
        show();
    }
    private final String[] allowedTypes = new String[]{CommandConstants.IMG_GIF, CommandConstants.IMG_JPEG, CommandConstants.IMG_JPG, CommandConstants.IMG_BMP, CommandConstants.IMG_PNG};

    private boolean validateImage() {
        boolean isValid = !"".equals(logoUpload.getFilename());
        if (isValid) {
            for (String ext : allowedTypes) {
                if (logoUpload.getFilename().toLowerCase().endsWith(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer getObjectId() {
        return form.getObjectID();
    }

    public abstract AttachmentStrategy attachmentStrategy();

    public void saveLogo() {
        form.setParameter(HOST_ID, attachmentStrategy().getHostId().toString());
        form.setParameter(LOGO_TYPE, logoType);
        form.setParameter(CommandConstants.NOTDOWNLOADABLE, "YES");
        form.submit();
        LoadingPanel.loading(true);
    }

    private void show() {
        form = new WfmFormPanel("/CompanyAttachment");
        form.setParameter(DESCRIPTION_PARAM_NAME, "");
        form.setParameter(UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());

        logoImage = new MaterialImage();
        imageContainer = new MaterialPanel("logo-card-img");
        imageContainer.add(logoImage);

        MaterialLink changeButton = new MaterialLink();
        changeButton.addStyleName("logo-card-img__btn ficon--camera");
        KpiToolTip logoPreviewTooltip = new KpiToolTip(changeButton, wfmStrings.change());
        changeButton.addClickHandler(c -> {
            logoUpload.onClickEvent(c);
        });

        imageWrapper = new MaterialPanel("logo-card-img__wrapper logo-card-img__wrapper--square");
        imageWrapper.add(imageContainer);
        imageWrapper.add(changeButton);
        imageWrapper.setVisible(false);

        LoginService.App.get().getCompanyLogo(logoType, new AbstractAsyncCallback<SelectItem>() {
            public void success(SelectItem result) {
                if (result != null) {
                    logoImage.setUrl(result.getName());
                    form.setObjectID(result.getId());
                    imageWrapper.setVisible(true);
//                    fileUpload.setVisible(false);
                }
            }
        });

        logoUpload = new LogoFileUpload();
        logoUpload.getFileUpload().addChangeHandler(e -> {
            if (validateImage()) {
                saveLogo();
            } else {
                logoUpload.reset();
                Info.show(wfmStrings.chooseYourLogoImage(), Info.Type.WARNING);
            }
        });

        Div div = new Div();
        div.add(imageWrapper);
        div.add(logoUpload);

        form.setWidget(div);
        form.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {

            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                WfmMessageBox message;
                if (form.isSuccess()) {
                    LoginService.App.get().getCompanyLogo(logoType, new AbstractAsyncCallback<SelectItem>() {
                        public void success(SelectItem result) {
                            if (result != null) {
                                logoImage.setUrl(result.getName());
                                form.setObjectID(result.getId());
                                imageWrapper.setVisible(true);
//                                fileUpload.setVisible(false);
                            }
                        }
                    });
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.companyLogo()), Info.Type.INFO);
                } else {
                    imageWrapper.setVisible(false);
                    logoUpload.reset();
                    message = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                    if (form.getErrorString() == null) {
                        message.setTitle(wfmStrings.error());
                        message.setMessage(wfmStrings.errorOccurredUpdate());
                    } else {
                        String s = form.getReturnValue();
                        s = s.replace("<PRE>", "");
                        s = s.replace("</PRE>", "");
                        s = s.replace("<pre>", "");
                        s = s.replace("</pre>", "");
                        message.setTitle(wfmStrings.warning());
                        if (FAIL.equals(s)) {
                            message.setMessage(wfmStrings.errorOccurredUpdate());
                        } else {
                            message.setMessage(s);
                        }
                        message.setSize(270, 150);
                    }
                    message.open();
                }
            }
        });

        initWidget(form);
    }

    class LogoFileUpload extends Div{

        private FileItem item;
        private FileUpload fileUpload;

        public LogoFileUpload() {
            addStyleName("form-upload default-width");
            initWidget();
        }

        private void initWidget() {
            WfmButton2 button = new WfmButton2(wfmStrings.uploadImage(), "btn btn--default", "ficon--plus");
            button.setWidth("100%");
            button.setHeight("60px");
            button.addClickHandler(c -> $(fileUpload.getElement()).trigger("click", null));

            fileUpload = new FileUpload();
            fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);
            fileUpload.addChangeHandler(e -> {
                item = new FileItem();
                item.setFileName(fileUpload.getFilename());
//                setVisible(false);
            });
            fileUpload.setVisible(false);

            add(button);
            add(fileUpload);
        }

        public void reset() {
            item = null;
            setVisible(true);
        }

        public void onClickEvent(ClickEvent event) {
            $(fileUpload.getElement()).trigger("click", null);
        }

        public FileUpload getFileUpload() {
            return fileUpload;
        }

        public String getFilename() {
            return item != null ? item.getFileName() : null;
        }
    }
}

