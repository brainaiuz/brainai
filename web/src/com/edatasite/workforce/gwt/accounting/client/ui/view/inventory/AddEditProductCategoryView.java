package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingServiceAsync;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.MultiLanguageTextAreaWidget;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static gwt.material.design.client.js.JsMaterialElement.$;

public class AddEditProductCategoryView extends CustomForm2 implements Constants, Colapse {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    Integer objectID;
    private final String editProductCategoryView = "edit_prouct_category_view_";
    Boolean closeTab = false;
    private FormHasCustomField customFieldUtil;
    private final AccountingServiceAsync accountingService = AccountingService.App.get();
    private MaterialLink copyCustomFields,saveAndClose,saveAndCopyCustomFields,saveAndAnother;
    private FormGroup categoryCode;
    private TextArea2 description;
    private DataListBox dwCategories;
    private TextBox txtName,categoryPrefix,categoryIntNumber;
    private ExtendedCommand commandProvider;
    private Command closeCommand;
    private boolean newItem;
    private TextBox txtPrice,txtOrder;
    private Image categoryImage;
    private FileUpload imageUpload;
    private WfmFormPanel imageUploadForm;
    private Div imageUploadDiv,imagePanel,txtNameDiv;
    private Integer imageID,parentId;
    private KpiModal box;
    private KpiSwitcher activeCheckBox;
    private boolean fromPopUp;
    private MultiLanguageTextAreaWidget nameLocalize;
    private MultiLanguageTextAreaWidget descriptionLocalize;

    public AddEditProductCategoryView(boolean add, Integer parentId) {
        super("productcategoryadd", wfmStrings.addCategory());
        this.parentId = parentId;
    }

    public AddEditProductCategoryView(Integer objectID) {
        super("productcategoryadd", wfmStrings.editCategory());
        this.objectID = objectID;
    }

    /*
     * Add category by other part
     */
    AddEditProductCategoryView(ExtendedCommand commandProvider, Command closeCommand, KpiModal box) {

        this.commandProvider = commandProvider;
        this.closeCommand = closeCommand;
        this.box = box;
        this.fromPopUp = true;
    }

    @Override
    protected void getDataToFillFields() {
        loadData();
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRODUCT_CATEGORY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected void addButtons() {

        saveAndClose = new MaterialLink(wfmStrings.save());
        saveAndClose.ensureDebugId(editProductCategoryView + "saveAndClose");
        MaterialSplitButton splitButton = new MaterialSplitButton(saveAndClose);

        saveAndAnother = new MaterialLink(wfmStrings.saveAndNew());
        saveAndAnother.ensureDebugId(editProductCategoryView + "saveAndAnother");

        splitButton.addItem(saveAndAnother);

        saveAndCopyCustomFields = new MaterialLink(accountingStrings.saveAndCopyParentCustomFields());
        saveAndCopyCustomFields.ensureDebugId(editProductCategoryView + "saveAndCopyCustomFields");
        saveAndCopyCustomFields.setVisible(false);
        splitButton.addItem(saveAndCopyCustomFields);

        copyCustomFields = new MaterialLink(accountingStrings.copyCustomFieldsToSubCategories());
        copyCustomFields.ensureDebugId(editProductCategoryView + "copyCustomFields");

        copyCustomFields.addClickHandler(event -> {
            LoadingPanel.loading(true);
            enableButtons(false);
            accountingService.copyCustomFieldsToSubCategories(objectID, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                    GWT.log(caught.getMessage());
                }

                @Override
                public void success(Boolean result) {
                    LoadingPanel.loading(false);
                    enableButtons(true);

                    Info.show(accountingStrings.customFieldsSuccCopied(), Info.Type.INFO);
                }
            });
        });
        saveAndClose.addClickHandler(event -> {
            closeTab = true;
            save(false);
        });
        saveAndAnother.addClickHandler(event -> {
            newItem = true;
            save(false);
        });

        saveAndCopyCustomFields.addClickHandler(event -> {
            closeTab = true;
            save(true);
        });

        if (objectID != null) {
            splitButton.addItem(copyCustomFields);
        }
        if (box != null) {
            footer.removeFromParent();
            box.addButton(splitButton);
        } else {
            addButton(splitButton);
        }
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.ProductCategoryStoreFront, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    AddEditProductCategoryView.super.onInitialize();
                }
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {
        initInternal();

        addTitleField(CustomFormConstants.PRODUCT_CATEGORY_TITLE, objectID == null ? wfmStrings.addCategory() : wfmStrings.editCategory());

        addField(CustomFormConstants.CODE, categoryCode, getTitle(wfmStrings.code(), true));
        addField(CustomFormConstants.NAME, txtNameDiv, null);
        addField(CustomFormConstants.CATEGORY, dwCategories, getTitle(wfmStrings.parent()));
        addField(CustomFormConstants.ORDER, txtOrder, getTitle(wfmStrings.order()));
        addField(CustomFormConstants.IMAGE_UPLOAD, imageUploadDiv, getTitle(wfmStrings.uploadImage()));
        addField(CustomFormConstants.ACTIVE, activeCheckBox, wfmStrings.active());
        addField(CustomFormConstants.DESCRIPTION, description, null);

        getCustomFieldUtil().drawCustomFields(this, objectID);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initInternal() {

        txtName = new TextBox();
        txtName.ensureDebugId(editProductCategoryView + "txtName");
        txtName.addStyleName(Constants.DEFAULT_WIDTH);
        txtName.addValueChangeHandler(valueChangeEvent -> nameLocalize.setCurrentLangText(txtName.getValue()));

        txtNameDiv = new Div();
        HTML child = new HTML(getTitle(wfmStrings.name(), true));
        child.setStyleName("form-group__label");
        txtNameDiv.add(child);
        txtNameDiv.add(txtName);

        categoryPrefix = new TextBox();
        categoryPrefix.ensureDebugId(editProductCategoryView + "categoryPrefix");

        categoryIntNumber = new TextBox();
        categoryIntNumber.ensureDebugId(editProductCategoryView + "categoryIntNumber");
        Validation.addPhoneNumberKeyboardListener(categoryIntNumber);

        categoryCode = new FormGroup(new InputGroup(categoryPrefix, categoryIntNumber));
        categoryCode.getGroupLabel().removeFromParent();
        categoryCode.addStyleName(editProductCategoryView + "categoryCode");

        dwCategories = new DataListBox();
        dwCategories.ensureDebugId(editProductCategoryView + "dwCategories");
        dwCategories.addStyleName(Constants.DEFAULT_WIDTH);
        dwCategories.addValueChangeHandler(changeEvent -> {
            saveAndCopyCustomFields.setVisible(dwCategories.getSelectedId() != null);
        });

        txtPrice = new TextBox();
        txtPrice.ensureDebugId(editProductCategoryView + "txtPrice");
        txtPrice.addStyleName(Constants.DEFAULT_WIDTH);
        txtPrice.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtPrice, 2);

        txtOrder = new TextBox();
        txtOrder.ensureDebugId(editProductCategoryView + "txtOrder");
        txtOrder.addStyleName(Constants.DEFAULT_WIDTH);
        txtOrder.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(txtOrder, 0);

        //Image Upload
        imageUploadForm = new WfmFormPanel("/CreateAttachment");
        imageUpload = new FileUpload();
        imageUpload.setName(CommandConstants.ATTACHMENT_PARAM_BASE + "0");
        TextArea desc = new TextArea();
        desc.setName(CommandConstants.DESCRIPTION_PARAM_NAME);
        desc.setVisible(false);
        desc.setText("");

        activeCheckBox = new KpiSwitcher();
        activeCheckBox.setEnabled(!fromPopUp);
        activeCheckBox.ensureDebugId(editProductCategoryView + "active");

        description = new TextArea2(wfmStrings.description());
        description.ensureDebugId(editProductCategoryView + "description");

        nameLocalize = new MultiLanguageTextAreaWidget(this.txtNameDiv);
        descriptionLocalize = new MultiLanguageTextAreaWidget(this.description);

        TextBox uploadType = new TextBox();
        uploadType.setName(CommandConstants.UPLOAD_TYPE_PARAM_NAME);
        uploadType.setVisible(false);
        uploadType.setText(Utils.getUploadTypeParam());

        HorizontalPanel hp = new HorizontalPanel();
        hp.add(imageUpload);
        hp.add(desc);
        hp.add(uploadType);
        imageUploadForm.setWidget(hp);

        Italic saveImageButton = new Italic();
        Div iconDiv = new Div();
        iconDiv.setClass("btn-upload__icon");
        iconDiv.add(saveImageButton);

        categoryImage = new Image();
        Italic iconClose = new Italic("");
        iconClose.setClass("close");
        categoryImage.setWidth("200px");
        categoryImage.setHeight("250px");

        Div image = new Div();
        image.setClass("btn-uploaded-img btn-uploaded--has-control");
        image.add(categoryImage);
        image.add(iconClose);

        imagePanel = new Div();
        imagePanel.setClass("form-upload__attached");
        imagePanel.add(image);
        imagePanel.setVisible(false);

        Div uploadDiv = new Div();
        uploadDiv.add(imageUploadForm);
        uploadDiv.add(iconDiv);
        uploadDiv.setClass("btn-upload");

        imageUploadDiv = new Div();
        imageUploadDiv.add(imagePanel);
        imageUploadDiv.add(uploadDiv);
        imageUploadDiv.setClass("form-upload");

        saveImageButton.addClickHandler(c -> $(imageUpload.getElement()).trigger("click", null));
        saveImageButton.setClass("ficon--attachment");

        imageUpload.addChangeHandler(c -> {
            if ("".equals(imageUpload.getFilename())) {
                Info.show(wfmStrings.choose(), Info.Type.WARNING);
            } else {
                if (imageUpload.getFilename().toLowerCase().lastIndexOf(".jpg") != -1 ||
                        imageUpload.getFilename().toLowerCase().lastIndexOf(".jpeg") != -1 ||
                        imageUpload.getFilename().toLowerCase().lastIndexOf(".gif") != -1 ||
                        imageUpload.getFilename().toLowerCase().lastIndexOf(".png") != -1 ||
                        imageUpload.getFilename().toLowerCase().lastIndexOf(".ico") != -1 ||
                        imageUpload.getFilename().toLowerCase().lastIndexOf(".bmp") != -1) {
                    imageUploadForm.setParameter(CommandConstants.IMAGE_TYPE, imageUpload.getFilename().substring(imageUpload.getFilename().toLowerCase().lastIndexOf(".") + 1));
                    saveImage();
                    LoadingPanel.loading(true);
                } else {
                    Info.show(wfmStrings.thisNotImage(), Info.Type.WARNING);
                }
            }

            $(imageUploadForm.getElement()).trigger("submit", null);
        });

        imageUploadForm.addSubmitCompleteHandler(event -> {
            LoadingPanel.loading(false);
            if (imageUploadForm.isSuccess()) {
                imageID = imageUploadForm.getObjectID();
                if (imageID != null) {
                    CommonService.App.get().getImageUrl(imageID, new AsyncCallback<String>() {

                        @Override
                        public void onFailure(Throwable throwable) {
                            GWT.log(throwable.getMessage());
                        }

                        @Override
                        public void onSuccess(String url) {
                            if (!Utils.isNullOrEmpty(url)) {
                                categoryImage.setUrl(url);
                                imagePanel.setVisible(true);
                            }
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_USER_IMAGE_UPLOAD_ADD, imageID, AddEditProductCategoryView.this);
                        }
                    });
                }
            }
        });

        iconClose.addClickHandler(clickEvent -> {
            if (imageID != null) {
                accountingService.deleteProductCategoryImage(objectID, imageID, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        GWT.log(throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            imagePanel.setVisible(false);
                        }
                    }
                });
            }
        });

    }

    private void loadData() {
        accountingService.getProductCategory(objectID, new AbstractAsyncCallback<ProductCategoryItem>() {
            public void failure(Throwable caught) {
                GWT.log(caught.getMessage());
            }

            public void success(ProductCategoryItem result) {
                String currentLocale = Utils.getUserLanguage();

                txtName.setText(result.getName());
                if (mapHasValueForLang(result.getNameLocalize(), currentLocale)) {
                    txtName.setTitle(result.getNameLocalize().get(currentLocale));
                }
                nameLocalize.setValueMap(result.getNameLocalize());

                categoryPrefix.setText(result.getPrefix());
                categoryIntNumber.setText(String.valueOf(result.getIntNumber()));

                txtPrice.setText(result.getPrice() != null ? AccountingUtils.get().formatUnitPrice(result.getPrice()) : "");
                txtOrder.setText(result.getOrder().toString());
                activeCheckBox.setValue(result.isActive());
                description.setText(result.getDescription());
                if (mapHasValueForLang(result.getDescriptionLocalize(), currentLocale)) {
                    description.setText(result.getDescriptionLocalize().get(currentLocale));
                }
                descriptionLocalize.setValueMap(result.getDescriptionLocalize());
                initCategoryList(parentId != null ? parentId : result.getParentCategoryID());

                if (result.getImageUrl() != null) {
                    categoryImage.setUrl(result.getImageUrl());
                    categoryImage.setVisible(true);
                    imagePanel.setVisible(true);
                }
                imageID = result.getImageID();
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFields());
            }
        });
    }

    private void initCategoryList(Integer categoryID) {
        accountingService.getCategoriesAsTreeSelectItemForSettings(new AbstractAsyncCallback<TreeSelectItem[]>() {
            public void failure(Throwable caught) {
                GWT.log(caught.getMessage());
            }

            public void success(TreeSelectItem[] result) {
                dwCategories.clear();
                List<TreeSelectItem> selectItemList = new ArrayList<>();

                for (TreeSelectItem item : result) {
                    if (!item.getId().equals(objectID)) {
                        selectItemList.add(item);
                    }
                }
                dwCategories.setItems(result);
                if (categoryID != null) {
                    dwCategories.setSelected(categoryID);
                }
            }
        });
    }

    private void saveImage() {
        if (imageID != null) {
            imageUploadForm.setParameter(CommandConstants.ATTACHMENT_ID, imageID.toString());
        }
        imageUploadForm.setParameter(CommandConstants.ATTACHMENT_FOLDER, CommandConstants.STATIC_FOLDER);
        imageUploadForm.submit();
        LoadingPanel.loading(true);
    }

    private void save(boolean isCopyCustomFieldsFromParent) {
        enableButtons(false);
        ProductCategoryItem category = getProductCategoryItem();
        category.setCopyCustomFieldsFromParent(isCopyCustomFieldsFromParent);

        if (validateProductCategory()) {
            accountingService.saveProductCategory(category, new AbstractAsyncCallback<Integer>() {
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                    enableButtons(true);
                }

                public void success(Integer result) {
                    LoadingPanel.loading(false);
                    enableButtons(true);

                    if (result.equals(0)) {
                        Window.alert(accountingStrings.categoryNameAlreadyExist());
                    } else {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.productCategory()), Info.Type.INFO);
                        if (closeTab) {
                            if (closeCommand != null) {
                                commandProvider.execute(result);
                                closeCommand.execute();
                            } else {
                                closeTab();
                            }
                        } else {
                            reinit();
                        }
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PRODUCTCATEGORY_SAVED, null, null);
                    }
                }
            });
        } else {
            enableButtons(true);
        }
    }

    private ProductCategoryItem getProductCategoryItem() {
        ProductCategoryItem categoryItem = new ProductCategoryItem();
        categoryItem.setId(objectID);
        categoryItem.setName(txtName.getText());
        categoryItem.setNameLocalize(nameLocalize.getValueMap());
        categoryItem.setActive(activeCheckBox.getValue());
        categoryItem.setCode(categoryPrefix.getText() + categoryIntNumber.getText());
        categoryItem.setPrefix(categoryPrefix.getText());
        categoryItem.setIntNumber(Integer.valueOf(categoryIntNumber.getText()));
        categoryItem.setDescription(description.getText());
        categoryItem.setDescriptionLocalize(descriptionLocalize.getValueMap());
        if (!"".equals(txtPrice.getText().trim())) {
            categoryItem.setPrice(AccountingUtils.get().parseToBigDecimal(txtPrice.getText()));
        } else {
            categoryItem.setPrice(BigDecimal.ZERO);
        }
        if (!"".equals(txtOrder.getText().trim())) {
            categoryItem.setOrder(Integer.parseInt(txtOrder.getText()));
        } else {
            categoryItem.setOrder(0);
        }


        categoryItem.setParentCategoryID(dwCategories.getSelectedId());
        categoryItem.setImageID(imageID);
        categoryItem.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());

        return categoryItem;
    }

    private boolean validateProductCategory() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(categoryPrefix) && !Validation.validateTextBoxRequired(categoryIntNumber)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(txtName)) {
            errors++;
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void enableButtons(boolean enable) {
        saveAndCopyCustomFields.setEnabled(enable);
        copyCustomFields.setEnabled(enable);
        saveAndClose.setEnabled(enable);
        saveAndAnother.setEnabled(enable);
    }

    private void reinit() {

        if (newItem) {
            objectID = null;
        }
        newItem = false;
        closeTab = false;
        txtName.setValue("");
        description.setText("");
        txtPrice.setText("");
        txtOrder.setText("");
        activeCheckBox.setValue(true);

        initCategoryList(null);
        initCategoryCode();
    }

    private void initCategoryCode() {
        accountingService.generateProductCategoryNumber(new AbstractAsyncCallback<NumberData>() {
            public void failure(Throwable caught) {
                GWT.log(caught.getMessage());
            }

            public void success(NumberData result) {
                categoryPrefix.setText(result.getFirstNumberString());
                categoryIntNumber.setText(String.valueOf(result.getIntNumber()));
            }
        });
    }

    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }
}
