package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/*
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: Feb 22, 2011
 * Time: 7:21:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class PdfLogoSettings extends View implements CommandConstants, Constants {

    public PdfLogoSettings() {
        super("pdfLogoSettings", backendStrings.pdfLogoSettings());
    }

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private final WfmFormPanel formPanel = new WfmFormPanel("/CompanyAttachment");
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private FileUpload fileUpload;
    private Image logoImage;
    private Integer pdfAttachmentID;
    private SchemaLookUp schemaLookUp;
    private Integer companyID = null;
    private TextBox imageShowWidth;
    private TextBox imageShowHeight;
    private Label statusLabel;

    private final WfmFormPanel customisedFormPanel = new WfmFormPanel("/CompanyAttachment");
    private FileUpload customisedFileUpload;
    private Image customisedLogoImage;
    private Integer customisedPDFAttachmentID;
    private TextBox customisedImageShowWidth;
    private TextBox customisedImageShowHeight;
    private Label customisedStatusLabel;

    private final WfmFormPanel stampFormPanel = new WfmFormPanel("/CompanyAttachment");
    private FileUpload stampUpload;
    private Image stampImage;
    private Integer stampPdfAttachmentID;
    private TextBox stampImageShowWidth;
    private TextBox stampImageShowHeight;
    private Label stampStatusLabel;
    private DataListBox stamps;
    private String stampType;
    private KpiCheckBox pdfStamperEnabled;

    protected Widget onInitialize() {
        FlexTable content = new FlexTable();
        content.setCellPadding(2);
        content.setCellSpacing(2);
        content.setWidth("70%");
        content.setHeight("30%");

        HTMLPanel companyLabel = new HTMLPanel("b", backendStrings.selectCompany());
        companyLabel.setStyleName("customTitle");

        schemaLookUp = new SchemaLookUp();

        schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (schemaLookUp.getSelectedItemID() != null) {
                LoadingPanel.loading(true);
                BackendService.App.get().isPdfStamperEnabled(schemaLookUp.getSelectedItemID(), new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Boolean result) {
                        LoadingPanel.loading(false);
                        pdfStamperEnabled.setValue(result);
                    }
                });
            }
        });

        HTMLPanel pdfLogoLabel = new HTMLPanel("b", backendStrings.pdfLogoUpload());
        pdfLogoLabel.setStyleName("customTitle");

        HTMLPanel invoicePdfLogoLabel = new HTMLPanel("b", backendStrings.invoicePdfLogoUpload());
        invoicePdfLogoLabel.setStyleName("customTitle");

        HTMLPanel pdfStampLabel = new HTMLPanel("b", backendStrings.pdfStampUpload());
        pdfStampLabel.setStyleName("customTitle");

        content.setWidget(0, 0, companyLabel);
        content.setWidget(0, 1, schemaLookUp);

        content.setWidget(1, 0, new HTML("<hr>"));
        content.setWidget(1, 1, new HTML("<hr>"));


        content.setWidget(2, 0, pdfLogoLabel);
        content.setWidget(2, 1, initLogo());

        content.setWidget(3, 0, new HTML("<hr>"));
        content.setWidget(3, 1, new HTML("<hr>"));

        content.setWidget(4, 0, invoicePdfLogoLabel);
        content.setWidget(4, 1, initCustomisedLogoForm());

        content.setWidget(5, 0, new HTML("<hr>"));
        content.setWidget(5, 1, new HTML("<hr>"));

        content.setWidget(6, 0, pdfStampLabel);
        content.setWidget(6, 1, initStampUploadForm());

        content.setWidget(7, 0, new HTML("<hr>"));
        content.setWidget(7, 1, new HTML("<hr>"));

        pdfStamperEnabled = new KpiCheckBox();
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new HTML("<span style='padding-right:5px'>" + backendStrings.enablePdfStamper() + ":</span>"));
        hp.add(pdfStamperEnabled);
        WfmButton2 saveStamperEnabled = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveStamperEnabled.addClickHandler(clickEvent -> {
            if (schemaLookUp.getSelectedItemID() != null) {
                BackendService.App.get().enabledCompanyPdfStamper(pdfStamperEnabled.getValue(), schemaLookUp.getSelectedItemID(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        Info.show(wfmStrings.errorOccurred(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(Void result) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.companyPdfLogo()), Info.Type.WARNING);
                    }
                });
            } else {
                Info.show(wfmStrings.pleaseSelectCompany(), Info.Type.WARNING);
            }
        });

        content.setWidget(8, 0, hp);
        content.setWidget(9, 1, saveStamperEnabled);

        content.getFlexCellFormatter().getElement(1, 0).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.getFlexCellFormatter().getElement(1, 1).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.getFlexCellFormatter().getElement(4, 0).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.getFlexCellFormatter().getElement(4, 1).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.getFlexCellFormatter().getElement(6, 0).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.getFlexCellFormatter().getElement(6, 1).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        content.setCellSpacing(10);

        add(content);
        return null;
    }


    private WfmFormPanel initLogo() {
        logoImage = new Image();
        logoImage.setVisible(false);

        fileUpload = new FileUpload();
        fileUpload.setName(ATTACHMENT_PARAM_BASE + "0");

        Button saveLogoButton = new Button(backendStrings.saveLogo(), (ClickHandler) event -> {
            companyID = schemaLookUp.getSelectedItemID();
            if (companyID == null) {
                Window.alert(wfmStrings.pleaseSelectCompany());
            }
            formPanel.setParameter(LOGO_TYPE, FOR_PDF);
            formPanel.setParameter(COMPANY__ID, companyID.toString());
            if (fileUpload.getFilename().equals("")) {
                Info.show(backendStrings.chooseAnImageForYourPdf(), Info.Type.WARNING);
            } else {
                saveLogo();
                saveLogoSizeForShow();
            }
        });
        saveLogoButton.setStyleName("button-background");

        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final TextBox imageWidth = new TextBox();
        imageWidth.setName(IMAGE_WIDTH);
        imageWidth.setValue("130");
        Validation.addNumericKeyboardListener(imageWidth);

        final TextBox imageHeight = new TextBox();
        imageHeight.setName(IMAGE_HEIGHT);
        imageHeight.setValue("150");
        Validation.addNumericKeyboardListener(imageHeight);

        imageShowWidth = new TextBox();
        Validation.addNumericKeyboardListener(imageWidth);

        imageShowHeight = new TextBox();
        Validation.addNumericKeyboardListener(imageHeight);

        final FlexTable table = new FlexTable();

        HTMLPanel width = new HTMLPanel("p", backendStrings.maximumAllowedSizeWidth() + ":");
        width.setStyleName("customTitle");

        HTMLPanel height = new HTMLPanel("p", wfmStrings.height() + ":");
        height.setStyleName("customTitle");

        HTMLPanel showWidth = new HTMLPanel("p", backendStrings.sizeForShowWidth() + ":");
        showWidth.setStyleName("customTitle");

        HTMLPanel showHeight = new HTMLPanel("p", wfmStrings.height() + ":");
        showHeight.setStyleName("customTitle");

        table.setWidget(0, 0, fileUpload);
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.setWidget(1, 0, width);
        table.setWidget(1, 1, imageWidth);
        table.setWidget(1, 2, height);
        table.setWidget(1, 3, imageHeight);
        table.setWidget(2, 0, description);
        table.setWidget(2, 1, uploadType);
        table.setWidget(3, 0, showWidth);
        table.setWidget(3, 1, imageShowWidth);
        table.setWidget(3, 2, showHeight);
        table.setWidget(3, 3, imageShowHeight);
        table.setWidget(3, 4, statusLabel = new Label());
        table.setWidget(4, 0, logoImage);
//        table.setWidget(4, 0, imageSizeNotification);
        table.setWidget(5, 1, saveLogoButton);


        formPanel.setWidget(table);
        formPanel.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);

            pdfAttachmentID = formPanel.getObjectID();
            if (formPanel.isSuccess() && pdfAttachmentID != null) {
                BackendService.App.get().getCompanyLogoURL(companyID, FOR_PDF, new AbstractAsyncCallback<String>() {
                    public void success(String result) {
                        if (result != null) {
                            logoImage.setUrl(result);
                            logoImage.setVisible(true);
                        }
                    }
                });
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.logos()), Info.Type.INFO);
            } else {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.ERROR, Action.OK);
                if (formPanel.getErrorString() == null) {
                    message.setTitle(wfmStrings.error());
                    message.setMessage(wfmStrings.errorOccurredSavingChanges() /* for your network*/);
                } else {
                    String error = formPanel.getErrorString();
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(error);
                    message.setSize(320, 150);
                }
                message.open();
            }
        });
        return formPanel;
    }

    private void saveLogoSizeForShow() {
        if (!"".equals(imageShowWidth.getText().trim()) && !"".equals(imageShowHeight.getText().trim()) && companyID != null) {
            try {
                Integer width = Double.valueOf(imageShowWidth.getText().trim()).intValue();
                Integer height = Double.valueOf(imageShowHeight.getText().trim()).intValue();
                BackendService.App.get().savePdfLogoSize(width, height, companyID, new AbstractAsyncCallback<String>() {
                    public void failure(Throwable throwable) {
                        statusLabel.setText(wfmStrings.errorOccurredSavingChanges());
                    }

                    public void success(String s) {
                        statusLabel.setText(s);
                    }
                });
            } catch (NumberFormatException e) {

            }
        }
    }

    private void saveLogo() {
        formPanel.submit();
        LoadingPanel.loading(true);
    }

    //Invoice PDF Logo Upload
    private WfmFormPanel initCustomisedLogoForm() {
        customisedLogoImage = new Image();
        customisedLogoImage.setVisible(false);

        customisedFileUpload = new FileUpload();
        customisedFileUpload.setName(ATTACHMENT_PARAM_BASE + "0");

        Button saveLogoButton = new Button(backendStrings.saveLogo(), (ClickHandler) event -> {
            companyID = schemaLookUp.getSelectedItemID();
            if (companyID == null) {
                Window.alert(wfmStrings.pleaseSelectCompany());
            }
            customisedFormPanel.setParameter(LOGO_TYPE, FOR_INVOICEPDF);
            customisedFormPanel.setParameter(COMPANY__ID, companyID.toString());
            if (customisedFileUpload.getFilename().equals("")) {
                Info.show(backendStrings.chooseAnImageForYourPdf(), Info.Type.WARNING);
            } else {
                saveCustomisedInvoiceLogo();
                saveCustomisedInvoiceLogoSizeForShow();
            }
        });
        saveLogoButton.setStyleName("button-background");

        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final TextBox imageWidth = new TextBox();
        imageWidth.setName(IMAGE_WIDTH);
        imageWidth.setValue("130");
        Validation.addNumericKeyboardListener(imageWidth);

        final TextBox imageHeight = new TextBox();
        imageHeight.setName(IMAGE_HEIGHT);
        imageHeight.setValue("150");
        Validation.addNumericKeyboardListener(imageHeight);

        customisedImageShowWidth = new TextBox();
        Validation.addNumericKeyboardListener(imageWidth);

        customisedImageShowHeight = new TextBox();
        Validation.addNumericKeyboardListener(imageHeight);

        final FlexTable table = new FlexTable();

        HTMLPanel width = new HTMLPanel("p", backendStrings.maximumAllowedSizeWidth() + ":");
        width.setStyleName("customTitle");

        HTMLPanel height = new HTMLPanel("p", wfmStrings.height() + ":");
        height.setStyleName("customTitle");

        HTMLPanel showWidth = new HTMLPanel("p", backendStrings.sizeForShowWidth() + ":");
        showWidth.setStyleName("customTitle");

        HTMLPanel showHeight = new HTMLPanel("p", wfmStrings.height() + ":");
        showHeight.setStyleName("customTitle");

        table.setWidget(0, 0, customisedFileUpload);
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.setWidget(1, 0, width);
        table.setWidget(1, 1, imageWidth);
        table.setWidget(1, 2, height);
        table.setWidget(1, 3, imageHeight);
        table.setWidget(2, 0, description);
        table.setWidget(2, 1, uploadType);
        table.setWidget(3, 0, showWidth);
        table.setWidget(3, 1, customisedImageShowWidth);
        table.setWidget(3, 2, showHeight);
        table.setWidget(3, 3, customisedImageShowHeight);
        table.setWidget(3, 4, customisedStatusLabel = new Label());
        table.setWidget(4, 0, customisedLogoImage);
        table.setWidget(5, 1, saveLogoButton);


        customisedFormPanel.setWidget(table);
        customisedFormPanel.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);

            customisedPDFAttachmentID = customisedFormPanel.getObjectID();
            if (customisedFormPanel.isSuccess() && customisedPDFAttachmentID != null) {
                BackendService.App.get().getCompanyLogoURL(companyID, FOR_INVOICEPDF, new AbstractAsyncCallback<String>() {
                    public void success(String result) {
                        if (result != null) {
                            customisedLogoImage.setUrl(result);
                            customisedLogoImage.setVisible(true);
                        }
                    }
                });
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.logos()), Info.Type.INFO);
            } else {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.ERROR, Action.OK);
                if (customisedFormPanel.getErrorString() == null) {
                    message.setTitle(wfmStrings.error());
                    message.setMessage(wfmStrings.errorOccurredSavingChanges() /* for your network*/);
                } else {
                    String error = customisedFormPanel.getErrorString();
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(error);
                    message.setSize(320, 150);
                }
                message.open();
            }
        });
        return customisedFormPanel;
    }

    private WfmFormPanel initStampUploadForm() {

        stamps = new DataListBox();
        stamps.setItems(new SelectItem[]{
                new SelectItem(1, APPROVE),
                new SelectItem(2, RECEIVED),
                new SelectItem(3, PAID),
                new SelectItem(4, OVER_DUE)
        });

        stampType = "";

        stamps.addValueChangeHandler(changeEvent -> {
            if (stamps.getSelectedItem(false) != null) {
                String type = stamps.getSelectedItem(false).getName();
                switch (type) {
                    case APPROVE:
                        type = FOR_APPROVE;
                        break;
                    case RECEIVED:
                        type = FOR_RECEIVED;
                        break;
                    case PAID:
                        type = FOR_PAID;
                        break;
                    default:
                        type = FOR_OVERDUE;
                        break;
                }

                if (!stampType.equals(type)) {
                    stampType = type;
                    if (schemaLookUp.getSelectedItemID() != null) {
                        BackendService.App.get().getCompanyStampURL(schemaLookUp.getSelectedItemID(), stampType, new AbstractAsyncCallback<String>() {
                            @Override
                            public void failure(Throwable throwable) {
                            }

                            @Override
                            public void success(String result) {
                                if (result != null) {
                                    stampImage.setUrl(result);
                                    stampImage.setVisible(true);
                                } else {
                                    stampImage.setVisible(false);
                                }
                            }
                        });
                    }
                } else {
                    stampImage.setVisible(true);
                }
            } else {
                stampImage.setVisible(false);
            }
        });

        stampImage = new Image();
        stampImage.setVisible(false);

        stampUpload = new FileUpload();
        stampUpload.setName(ATTACHMENT_PARAM_BASE + "0");

        Button saveStampButton = new Button(backendStrings.saveStamp(), (ClickHandler) clickEvent -> {
            companyID = schemaLookUp.getSelectedItemID();
            if (companyID == null) {
                Info.show(wfmStrings.pleaseSelectCompany(), Info.Type.WARNING);
            }
            stampFormPanel.setParameter(COMPANY__ID, companyID.toString());
            if (stamps.getSelectedItem(false) == null) {
                Info.show(backendStrings.pleaseSelectStatusForUploadStamp(), Info.Type.WARNING);
            } else {
                if (stamps.getSelectedItem(false).getName().equals(APPROVE)) {
                    stampFormPanel.setParameter("logoType", CommandConstants.FOR_APPROVE);
                    stampType = CommandConstants.FOR_APPROVE;
                } else if (stamps.getSelectedItem(false).getName().equals(RECEIVED)) {
                    stampFormPanel.setParameter("logoType", CommandConstants.FOR_RECEIVED);
                    stampType = CommandConstants.FOR_RECEIVED;
                } else if (stamps.getSelectedItem(false).getName().equals(OVER_DUE)) {
                    stampFormPanel.setParameter("logoType", CommandConstants.FOR_OVERDUE);
                    stampType = CommandConstants.FOR_OVERDUE;
                } else {
                    stampFormPanel.setParameter("logoType", CommandConstants.FOR_PAID);
                    stampType = CommandConstants.FOR_PAID;
                }
            }

            if (fileUpload.getFilename().equals("")) {
                Info.show(backendStrings.pleaseChooseAnImageForYourStamp(), Info.Type.WARNING);
            } else {
                saveStamp();
            }
        });
        saveStampButton.setStyleName("button-background");

        TextArea description = new TextArea();
        description.setName(DESCRIPTION_PARAM_NAME);
        description.setVisible(false);
        description.setText("");
        TextBox uploadType = new TextBox();
        uploadType.setName(UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        final TextBox imageWidth = new TextBox();
        imageWidth.setName(IMAGE_WIDTH);
        imageWidth.setValue("130");
        Validation.addNumericKeyboardListener(imageWidth);

        final TextBox imageHeight = new TextBox();
        imageHeight.setName(IMAGE_HEIGHT);
        imageHeight.setValue("150");
        Validation.addNumericKeyboardListener(imageHeight);

        stampImageShowWidth = new TextBox();
        Validation.addNumericKeyboardListener(imageWidth);

        stampImageShowHeight = new TextBox();
        Validation.addNumericKeyboardListener(imageHeight);

        final FlexTable table = new FlexTable();

        HTMLPanel width = new HTMLPanel("p", backendStrings.maximumAllowedSizeWidth() + ":");
        width.setStyleName("customTitle");

        HTMLPanel height = new HTMLPanel("p", wfmStrings.height() + ":");
        height.setStyleName("customTitle");

        HTMLPanel showWidth = new HTMLPanel("p", backendStrings.sizeForShowWidth() + ":");
        showWidth.setStyleName("customTitle");

        HTMLPanel showHeight = new HTMLPanel("p", wfmStrings.height() + ":");
        showHeight.setStyleName("customTitle");

        HorizontalPanel hp = new HorizontalPanel();

        hp.add(new Label(wfmStrings.pleaseSelectStatus() + ":"));
        hp.add(stamps);

        table.setWidget(0, 0, hp);
        table.setWidget(1, 0, new HTML("&nbsp;"));
        table.setWidget(1, 1, new HTML("&nbsp;"));
        table.setWidget(2, 1, fileUpload);
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.setWidget(3, 0, width);
        table.setWidget(3, 1, imageWidth);
        table.setWidget(3, 2, height);
        table.setWidget(3, 3, imageHeight);
        table.setWidget(4, 0, description);
        table.setWidget(4, 1, uploadType);
        table.setWidget(5, 0, showWidth);
        table.setWidget(5, 1, stampImageShowWidth);
        table.setWidget(5, 2, showHeight);
        table.setWidget(5, 3, stampImageShowHeight);
        table.setWidget(5, 4, stampStatusLabel = new Label());
        table.setWidget(6, 0, stampImage);
        table.setWidget(7, 1, saveStampButton);

        stampFormPanel.setWidget(table);
        stampFormPanel.addSubmitCompleteHandler(submitCompleteEvent -> {
            LoadingPanel.loading(false);

            stampPdfAttachmentID = stampFormPanel.getObjectID();
            if (stampFormPanel.isSuccess() && stampPdfAttachmentID != null) {
                BackendService.App.get().getCompanyStampURL(companyID, stampType, new AbstractAsyncCallback<String>() {
                    @Override
                    public void success(String result) {
                        if (result != null) {
                            stampImage.setUrl(result);
                            stampImage.setVisible(true);
                        }
                    }
                });
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.stamp()), Info.Type.INFO);
            } else {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.ERROR, Action.OK);
                if (formPanel.getErrorString() == null) {
                    message.setTitle(wfmStrings.error());
                    message.setMessage(wfmStrings.errorOccurredSavingChanges());
                } else {
                    String error = formPanel.getErrorString();
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(error);
                    message.setSize(320, 150);
                }
                message.open();
            }
        });

        return stampFormPanel;
    }

    private void saveStamp() {
        stampFormPanel.submit();
        LoadingPanel.loading(true);
    }

    private void saveStampSizeForShow() {
        if (!"".equals(stampImageShowWidth.getText().trim()) && !"".equals(stampImageShowHeight.getText().trim()) && companyID != null) {
            try {
                Integer width = Double.valueOf(stampImageShowWidth.getText().trim()).intValue();
                Integer height = Double.valueOf(stampImageShowHeight.getText().trim()).intValue();
                BackendService.App.get().saveStampLogoSize(width, height, companyID, new AbstractAsyncCallback<String>() {
                    public void failure(Throwable throwable) {
                        statusLabel.setText(wfmStrings.errorOccurredSavingChanges());
                    }

                    public void success(String s) {
                        stampStatusLabel.setText(s);
                    }
                });
            } catch (NumberFormatException e) {

            }
        }
    }

    private void saveCustomisedInvoiceLogo() {
        customisedFormPanel.submit();
        LoadingPanel.loading(true);
    }

    private void saveCustomisedInvoiceLogoSizeForShow() {
        if (!"".equals(customisedImageShowWidth.getText().trim()) && !"".equals(customisedImageShowHeight.getText().trim()) && companyID != null) {
            try {
                Integer width = Double.valueOf(customisedImageShowWidth.getText().trim()).intValue();
                Integer height = Double.valueOf(customisedImageShowHeight.getText().trim()).intValue();
                BackendService.App.get().saveInvoiceLogoSize(width, height, companyID, new AbstractAsyncCallback<String>() {
                    public void failure(Throwable throwable) {
                        statusLabel.setText(wfmStrings.errorOccurredSavingChanges());
                    }

                    public void success(String s) {
                        customisedStatusLabel.setText(s);
                    }
                });
            } catch (NumberFormatException e) {

            }
        }
    }

    @Override
    public String getIconStyle() {
        return "backend pdfLogSet";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
