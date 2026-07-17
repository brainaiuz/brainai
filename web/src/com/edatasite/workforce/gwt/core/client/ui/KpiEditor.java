package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.*;

/**
 * Created by Hurshid on 12/7/2017.
 */
public class KpiEditor extends Composite {

    interface MaterialEditorBinder extends UiBinder<Widget, KpiEditor> {
    }

    private static final MaterialEditorBinder ourUiBinder = GWT.create(MaterialEditorBinder.class);

    @UiField
    MaterialRichEditor richEditor;

    boolean isFromIntroPage;
    boolean isPdfSetting = false;

    public KpiEditor() {
        this(false);
    }


    public KpiEditor(boolean isSimpleMode) {
        this(false, isSimpleMode, null);
    }

    public KpiEditor(boolean isAirMode, boolean isSimpleMode) {
        this(isAirMode, isSimpleMode, null);
    }

    public KpiEditor(boolean isAirMode, boolean isSimpleMode, boolean isPdfSetting) {
        this(isAirMode, isSimpleMode, null, isPdfSetting);
    }

    public KpiEditor(boolean isAirMode, boolean isSimpleMode, String customForm) {
        this(isAirMode, isSimpleMode, customForm, false);
    }

    public KpiEditor(boolean isAirMode, boolean isSimpleMode, String customForm, boolean isPdfSetting) {
        this.isPdfSetting = isPdfSetting;

        initWidget(ourUiBinder.createAndBindUi(this));
        richEditor.setAirMode(isAirMode);
        setPlaceHolder("");

        if (isSimpleMode) {
            setSimpleModeOptions(customForm);
        }

        richEditor.addAttachHandler(attachEvent -> {
            StyleInjector.inject(".note-editable ol { list-style-type: decimal !important; padding-left: 10px !important; margin: 10px 0 !important; }" +
                    ".note-editable li { display: list-item !important; list-style-type: inherit !important; }");

            richEditor.getEditor().find(".note-dialog").detach();
            richEditor.getEditor().find("div[data-event='showImageDialog']").off("click").on("click", (e, param1) -> {
                KpiEditorFileUploadDialog imageDialog = new KpiEditorFileUploadDialog();
                imageDialog.setWidth("600px");
                imageDialog.open();
                return false;
            });

            richEditor.getEditor().find("div[data-event='showLinkDialog']").off("click").on("click", (e, param1) -> {
                KpiEditorLinkDialog imageDialog = new KpiEditorLinkDialog();
                imageDialog.setWidth("600px");
                imageDialog.addStyleName("file--KpiEditor");
                imageDialog.open();
                return false;
            });

            richEditor.getEditor().find("div[data-event='showHelpDialog']").detach();
        });
    }

    public void setSimpleMode(boolean simpleMode) {
        if (simpleMode) {
            setSimpleModeOptions(null);
        }
    }

    private void setSimpleModeOptions(String customForm) {
        // Do not remove !!!
        richEditor.setMiscOptions();
        richEditor.setUndoOptions();
        ToolbarButton[] toolbarStyleOptions = isPdfSetting ? new ToolbarButton[]{ToolbarButton.BOLD, ToolbarButton.ITALIC, ToolbarButton.UNDERLINE, ToolbarButton.STRIKETHROUGH} : new ToolbarButton[]{ToolbarButton.STYLE, ToolbarButton.BOLD, ToolbarButton.ITALIC, ToolbarButton.UNDERLINE, ToolbarButton.STRIKETHROUGH};
        if (isPdfSetting) {
            richEditor.setColorOptions(ToolbarButton.COLOR);
            richEditor.setFontOptions(ToolbarButton.FONT_SIZE);
        } else {
            richEditor.setColorOptions();
            richEditor.setHeightOptions();
        }


        if (customForm != null) {
            richEditor.setStyleOptions();
            richEditor.setParaOptions();
            richEditor.setCkMediaOptions();
            richEditor.setMiscOptions(ToolbarButton.FULLSCREEN);
        } else {
            richEditor.setMiscOptions(ToolbarButton.TABLE, ToolbarButton.HR, ToolbarButton.LINK, ToolbarButton.FULLSCREEN);
            richEditor.setStyleOptions(toolbarStyleOptions);
            richEditor.setParaOptions(ToolbarButton.OL, ToolbarButton.UL, ToolbarButton.LEFT, ToolbarButton.CENTER, ToolbarButton.RIGHT, ToolbarButton.JUSTIFY);
        }
    }

    private void insertImage(String url) {
        if (!Utils.isNullOrEmpty(url)) {
            richEditor.setHTML(richEditor.getHTML() + "<img src='" + url + "' />");
        }
    }

    private void insertLink(String text, String url) {
        if (!Utils.isNullOrEmpty(url)) {
            richEditor.setHTML(richEditor.getHTML() + "<a href='" + url + "' target='blank'>" + text + "</a>");
        }
    }

    public String getData() {
        return richEditor.getHTML();
    }

    public void setData(String html) {
        richEditor.setHTML(html);
    }

    public void setPlaceHolder(String placeHolder) {
        richEditor.setPlaceholder(placeHolder);
    }

    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return richEditor.addHandler(handler, KeyDownEvent.getType());
    }

    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return richEditor.addHandler(handler, KeyUpEvent.getType());
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public void setEnabled(boolean enabled) {
        richEditor.setEnabled(enabled);
    }

    public MaterialRichEditor getRichEditor() {
        return richEditor;
    }

    class KpiEditorFileUploadDialog extends KpiModal {

        WfmFormPanel form;
        FileUpload fileUpload;
        TextBox urlBox;
        WfmButton2 saveButton;
        WfmButton2 closeButton;

        public KpiEditorFileUploadDialog() {
            super();
            init();
        }

        private void init() {
            setTitle(wfmStrings.uploadImage());

            urlBox = new TextBox();

            form = new WfmFormPanel("/CreateAttachment");
            form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(Constants.SESSION_ID_COOKIE));
            form.setParameter(MULTIPLE_FILES, "true");

            fileUpload = new FileUpload();
            fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);
            if (!isFromIntroPage) {
                form.setWidget(fileUpload);
            }

            form.addSubmitCompleteHandler(event -> {
                enableButtons(true);
                LoadingPanel.loading(false);
                if (form.isSuccess()) {
                    try {
                        Integer attachmentId = Integer.parseInt(form.getReturnValue());
                        MessageCenterService.App.get().getAttachedFilesByAttachmentId(attachmentId, new AbstractAsyncCallback<ArrayList<FileResource>>() {
                            public void success(final ArrayList<FileResource> files) {
                                if (files.get(0) != null) {
                                    String url = files.get(0).getUrlFromSolr();

                                    insertImage(url);

                                    fileUpload.getElement().setPropertyString("value", "");
                                    close();
                                }
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
            });
            fileUpload.addChangeHandler(valueChangeEvent -> {
                if ("".equals(fileUpload.getFilename())) {
                    Info.show(wfmStrings.chooseFile(), Info.Type.WARNING);
                } else {
                    if (fileUpload.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                            fileUpload.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                        form.setParameter(CommandConstants.DESCRIPTION_PARAM_NAME, "");
                        form.setParameter(CommandConstants.UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
                        form.setParameter(CommandConstants.IMAGE_TYPE, fileUpload.getFilename().substring(fileUpload.getFilename().toLowerCase().lastIndexOf(".") + 1));
                        form.submit();
                        enableButtons(false);
                        LoadingPanel.loading(true, this);
                    } else {
                        Info.show(wfmStrings.thisNotImage(), Info.Type.WARNING);
                    }
                }
            });

            addWidget(form, null);
            addWidget(urlBox, "url");

            saveButton = new WfmButton2(wfmStrings.upload(), event -> save());
            closeButton = new WfmButton2(wfmStrings.close(), event -> close());

            addButton(saveButton);
            addButton(closeButton);
        }

        private void enableButtons(boolean enable) {
            saveButton.setEnabled(enable);
            closeButton.setEnabled(enable);
        }

        public void save() {
            if (!Validation.validateTextBoxRequired(urlBox)) {
                Info.warn("Invalid url");
                return;
            }

            insertImage(urlBox.getValue());
            urlBox.setValue("");
            close();
        }
    }

    class KpiEditorLinkDialog extends KpiModal {
        TextBox textBox;
        TextBox urlBox;
        WfmButton2 saveButton;
        WfmButton2 closeButton;

        public KpiEditorLinkDialog() {
            super();
            init();
        }

        private void init() {
            setTitle(wfmStrings.linkText());

            textBox = new TextBox();
            urlBox = new TextBox();

            urlBox.setText("http://");

            addWidget(textBox, wfmStrings.text());
            addWidget(urlBox, "url");

            saveButton = new WfmButton2(wfmStrings.add(), event -> save());
            closeButton = new WfmButton2(wfmStrings.close(), event -> close());

            addButton(saveButton);
            addButton(closeButton);
        }

        private void save() {
            if (!Validation.validateTextBoxRequired(textBox)) {
                Info.warn("Empty text");
                return;
            }

            if (!Validation.validateTextBoxRequired(urlBox)) {
                Info.warn("Invalid url");
                return;
            }

            insertLink(textBox.getValue(), urlBox.getValue());
            textBox.setValue("");
            urlBox.setValue("");
            close();
        }
    }

    public boolean isFromIntroPage() {
        return isFromIntroPage;
    }

    public void setFromIntroPage(boolean fromIntroPage) {
        isFromIntroPage = fromIntroPage;
    }
}
