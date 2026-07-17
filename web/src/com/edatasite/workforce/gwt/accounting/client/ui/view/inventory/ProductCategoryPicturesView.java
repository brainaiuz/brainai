package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.grayForm.GrayForm;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 6:51:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryPicturesView extends View implements CommandConstants, Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmFormPanel form;
    private FlexTable filesDisplayTable;
    private FileUpload fileUpload;
    private WfmButton2 uploadButton;
    private WfmButton2 cancel;
    private final int MAX_PICTURE_PER_ROW = 5;

    private final Integer objectId;

    public ProductCategoryPicturesView(Integer objectId) {
        super("productPictures", accountingStrings.categoryPictures());
        this.objectId = objectId;
    }

    @Override
    public String getIconStyle() {
        return "accountMark product-category";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected Widget onInitialize() {
        initUploadForm();

        GrayForm grayForm = new GrayForm();
        grayForm.setWidth("100%");
        grayForm.addBookmark(accountingStrings.categoryPictures());

        filesDisplayTable = new FlexTable();

        VerticalPanel vp = grayForm.addInnerPanel();
        vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        vp.add(form);
        vp.add(filesDisplayTable);
        loadFiles();

        add(grayForm);
        return null;
    }

    private void initUploadForm() {
        form = new WfmFormPanel("/CreateProductCategoryPicturesHandler");
        form.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                Info.show(form.getErrorString() != null ? wfmStrings.messParseErrorCompareFile() : Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.file()), Info.Type.INFO);
                loadFiles();
            }
        });
        uploadButton = new WfmButton2(wfmStrings.upload());
        uploadButton.addClickHandler(event -> {
            if (fileUpload.getFilename() != null && fileUpload.getFilename() != "") {
                if (".png".equals(fileUpload.getFilename().toLowerCase().substring(fileUpload.getFilename().lastIndexOf(".")))
                        || ".jpg".equals(fileUpload.getFilename().toLowerCase().substring(fileUpload.getFilename().lastIndexOf(".")))
                        || ".gif".equals(fileUpload.getFilename().toLowerCase().substring(fileUpload.getFilename().lastIndexOf(".")))
                        || ".jpeg".equals(fileUpload.getFilename().toLowerCase().substring(fileUpload.getFilename().lastIndexOf(".")))) {
                    form.setParameter(PRODUCT_CATEGORY_ID, objectId.toString());
                    form.setParameter(DESCRIPTION_PARAM_NAME, "");
                    form.setParameter(UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
                    form.setParameter(IMAGE_TYPE, fileUpload.getFilename().substring(fileUpload.getFilename().toLowerCase().lastIndexOf(".") + 1));
                    form.submit();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(accountingStrings.wrongFileType(), Info.Type.WARNING);
                }
            }
        });
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        fileUpload = new FileUpload();
        fileUpload.setWidth("350px");
        fileUpload.setName(ATTACHMENT_PARAM_BASE + 0);

        uploadButton.addClickHandler(event -> {

        });
        cancel.addClickHandler(event -> closeTab());
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(uploadButton);
        hp.add(cancel);

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, fileUpload);
        table.setWidget(1, 0, hp);
        table.setCellSpacing(15);
        form.setWidget(table);
    }

    private void loadFiles() {
        DeferredCommand.addCommand(() -> {
            LoadingPanel.loading(true);
            AccountingService.App.get().getProductCategoryPictures(objectId, FILE_SIZE_DEFAULT, new AbstractAsyncCallback<ProductPicture[]>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                public void success(final ProductPicture[] result) {
                    LoadingPanel.loading(false);
                    if (result != null) {
                        filesDisplayTable.removeAllRows();
                        int rowCount = 0;
                        int cellCount = 0;

                        for (int i = 0; i < result.length; i++) {
                            Image productPicture = new Image(result[i].getUrl());
                            productPicture.setHeight("100px");
                            VerticalPanel imagePanel = new VerticalPanel();
                            imagePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                            imagePanel.setStyleName("imagePanel");
                            imagePanel.setSpacing(2);
                            imagePanel.add(new HTML("<b class=customTitle>" + result[i].getName() + "<b>"));
                            imagePanel.add(productPicture);
                            SimpleLink removeLink = new SimpleLink(wfmStrings.delete());
                            final Integer pictureID = result[i].getPictureID();
                            removeLink.addClickHandler(event -> {
                                LoadingPanel.loading(true);
                                AccountingService.App.get().deleteProductCategoryPicture(pictureID, new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void success(Boolean result12) {
                                        LoadingPanel.loading(false);
                                        if (result12) {
                                            loadFiles();
                                        }
                                    }
                                });
                            });
                            imagePanel.add(removeLink);
                            RadioButton defaultPictureRadioButton = new KpiRadioButton("default");
                            defaultPictureRadioButton.setValue(result[i].isDefaultPicture());
                            defaultPictureRadioButton.addClickHandler(event -> {
                                LoadingPanel.loading(true);
                                AccountingService.App.get().setDefaultProductCategoryPicture(pictureID, objectId, new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void success(Boolean result1) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.setAsDefault(), Info.Type.INFO);
                                    }
                                });
                            });
                            imagePanel.add(defaultPictureRadioButton);
                            if (i % MAX_PICTURE_PER_ROW == 0) {
                                rowCount++;
                                cellCount = 0;
                            } else {
                                cellCount++;
                            }
                            filesDisplayTable.setWidget(rowCount, cellCount, imagePanel);
                        }
                    }
                }
            });
        });

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