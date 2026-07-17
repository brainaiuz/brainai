package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileInputElement;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.File;
import com.edatasite.workforce.gwt.documents.client.gwtupload.file.FileList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.CommandConstants.ATTACHMENT_PARAM_BASE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.DESCRIPTION_PARAM_NAME;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.IMAGE_TYPE;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.MULTIPLE_FILES;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.PRODUCT_ID;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.SESSION_ID_PARAM_NAME;
import static com.edatasite.workforce.gwt.core.client.CommandConstants.UPLOAD_TYPE_PARAM_NAME;
import static gwt.material.design.client.js.JsMaterialElement.$;

/**
 * Created by Anvar Akramov on 12/13/17.
 */
public class ProductPictureForm extends Composite implements Constants {
    interface FileUploadFormUiBinder extends UiBinder<Widget, ProductPictureForm> {
    }

    private static FileUploadFormUiBinder ourUiBinder = GWT.create(FileUploadFormUiBinder.class);

    @UiField
    Label attachmmentsUploadLabel;
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
    @UiField
    Div aiEnhanceButton;

    private Integer objectId;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static ProductServiceAsync productService = ProductService.App.get();

    private ArrayList<ProductFile> uploadedFiles = new ArrayList<>();

    public ProductPictureForm(Integer objectId) {
        this.objectId = objectId;
        initWidget(ourUiBinder.createAndBindUi(this));
        initForm();
        loadFiles();
    }

    private void initForm() {
        uploadFile.getElement().setPropertyBoolean("multiple", true);
        attachmmentsUploadLabel.setText(Property.get(Constants.PRODUCTS_OR_SERVICES, accountingStrings.productPictures(), wfmStrings.product()));
        setHasFiles(false);
        attachButtonContainer.addClickHandler(clickEvent -> $(uploadFile.getElement()).trigger("click", null));

        form.setParameter(SESSION_ID_PARAM_NAME, Cookies.getCookie(SESSION_ID_COOKIE));
        form.setParameter(MULTIPLE_FILES, "true");
        uploadFile.setName(ATTACHMENT_PARAM_BASE + 0);

        form.addSubmitCompleteHandler(event -> {
            Integer id = form.getObjectID();
            if (id != null || form.isSuccess()) {
                try {
                    String formReturnValue = form.getReturnValue();
                    String[] numbersArray = formReturnValue.split(",");
                    for (int i = 0; i < numbersArray.length; i++) {
                        Integer attachmentId = Integer.parseInt(numbersArray[i]);
                        int finalI = i;
                        productService.getProductPictureByID(attachmentId, new AbstractAsyncCallback<ProductPicture>() {
                            public void success(ProductPicture file) {
                                if (file != null) {
                                    ProductFile kpiFile = uploadedFiles.get(uploadedFiles.size() - 1 - finalI);
                                    if (uploadedFiles.size() == 1) {
                                        setAsDefault(file);
                                        file.setDefaultPicture(true);
                                    }
                                    kpiFile.setDone(true);
                                    kpiFile.setFile(file);
                                    setHandlers(kpiFile);
                                    uploadFile.getElement().setPropertyString("value", "");
                                }
                                setHasFiles(uploadedFiles.size() > 0);
                            }
                        });
                    }
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
            FileList fileList = ((FileInputElement) uploadFile.getElement().cast()).getFiles();
            for (int i = 0; i < fileList.getLength(); i++) {
                File file = fileList.getItem(i);
                String filename = file.getName().trim();
                if (filename != null && filename != "") {
                    if (".png".equals(filename.toLowerCase().substring(filename.lastIndexOf(".")))
                            || ".jpg".equals(filename.toLowerCase().substring(filename.lastIndexOf(".")))
                            || ".gif".equals(filename.toLowerCase().substring(filename.lastIndexOf(".")))
                            || ".jpeg".equals(filename.toLowerCase().substring(filename.lastIndexOf(".")))) {
                        if (objectId != null) {
                            form.setParameter(PRODUCT_ID, objectId.toString());
                        }
                        form.setParameter(DESCRIPTION_PARAM_NAME, "");
                        form.setParameter(UPLOAD_TYPE_PARAM_NAME, Utils.getUploadTypeParam());
                        form.setParameter(IMAGE_TYPE, filename.substring(filename.toLowerCase().lastIndexOf(".") + 1));
                    } else {
                        Info.show(wfmStrings.thisNotImage(), Info.Type.WARNING);
                        return;
                    }
                }
                ProductFile productFile = new ProductFile(null);
                //Display on UI
                uploadedFilesContainer.add(productFile);
                //Keep references
                uploadedFiles.add(productFile);
                setHasFiles(true);

            }
            $(form.getElement()).trigger("submit", null);
        });
        aiEnhanceButton.addClickHandler(event -> {
            if (uploadedFiles == null || uploadedFiles.isEmpty()) {
                Info.show("No images to enhance", Info.Type.WARNING);
                return;
            }

            AiImageEditorDialog dialog = new AiImageEditorDialog(uploadedFiles);
            dialog.setOnSaveCallback(this::loadFiles);
            dialog.open();
        });
    }

    private void loadFiles() {
        if (objectId == null) return;
        productService.getProductPictures(objectId, FILE_SIZE_DEFAULT, new AbstractAsyncCallback<ProductPicture[]>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(final ProductPicture[] result) {
                LoadingPanel.loading(false);
                uploadedFiles.clear();
                uploadedFilesContainer.clear();
                if (result != null) {
                    for (int i = 0; i < result.length; i++) {
                        ProductFile pf = new ProductFile(result[i]);
                        pf.setDone(true);
                        addFile(pf);
                    }
                }
                setHasFiles(!uploadedFiles.isEmpty());
            }
        });
    }

    private void setHasFiles(boolean hasFiles) {
        if (hasFiles) {
            uploadedFilesContainer.setVisible(true);
            attachButtonContainer.setClass("btn-upload btn-upload--plus");
            uploadBtnIcon.setClass("ficon--plus");
        } else {
            uploadedFilesContainer.setVisible(false);
            attachButtonContainer.setClass("btn-upload");
            uploadBtnIcon.setClass("ficon--attachment");
        }
    }

    public void addFile(ProductFile productFile) {
        setHandlers(productFile);
        uploadedFilesContainer.add(productFile);
        uploadedFiles.add(productFile);
        setHasFiles(true);
    }

    private void setAsDefault(ProductPicture productPicture) {
        LoadingPanel.loading(true);
        productService.setDefaultProductPicture(productPicture.getPictureID(), objectId, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Boolean result) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.setAsDefault(), Info.Type.INFO);
            }
        });
    }

    private void setHandlers(ProductFile productFile) {
        if (objectId != null) {
            productFile.addDefulatRadioButtonHandler(event -> {

                LoadingPanel.loading(true);
                productService.setDefaultProductPicture(productFile.getPicture().getPictureID(), objectId, new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Boolean result) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.setAsDefault(), Info.Type.INFO);
                    }
                });
            });
        }
        productFile.addCloseHandler(closeEvent -> {
            LoadingPanel.loading(true);
            productService.deleteProductPicture(productFile.getPicture().getPictureID(), new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Boolean isDefaultPicture) {
                    LoadingPanel.loading(false);
                    if (isDefaultPicture) {
                        Info.show(accountingStrings.thisImageIsSetAsDefaultImage(), Info.Type.WARNING);
                    } else {
                        if (uploadedFilesContainer != null) {
                            uploadedFilesContainer.remove(productFile);
                        }
                        if (uploadedFiles.contains(productFile)) {
                            uploadedFiles.remove(productFile);
                        }
                        setHasFiles(uploadedFiles.size() > 0);
                    }
                }
            });
        });
    }

    public void setParameter(String name, String value) {
        form.setParameter(name, value, false);
    }

//    public ArrayList<ProductFile> getUploadedFiles() {
//        return uploadedFiles;
//    }

    public ProductPicture[] getUploadedFiles() {
        ArrayList<ProductPicture> result = new ArrayList<>();
        if (uploadedFiles != null && uploadedFiles.size() > 0) {
            for (ProductFile uploadedFile : uploadedFiles) {
                ProductPicture picture = uploadedFile.getPicture();
                if (picture != null) {
                    picture.setDefaultPicture(uploadedFile.isSelectedAsDefault());
                    result.add(picture);
                }
            }
        }
        return result.toArray(new ProductPicture[0]);
    }

    public boolean isFinished() {
        for (ProductFile uploadFile : uploadedFiles) {
            if (!uploadFile.isDone()) {
                return false;
            }
        }
        return true;
    }

    public void clearFiles() {
        for (ProductFile uploadFile : uploadedFiles) {
            try {
                uploadedFilesContainer.remove(uploadFile);
            } catch (Exception e) {
                GWT.log("", e);
            }
        }
        uploadedFiles.clear();
    }
}
