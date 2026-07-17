package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

public class ProductCategorySummaryView extends CustomForm2 implements NoColapse {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private final Integer objectID;
    private HTML txtName, txtOrder, categoryCode, active, parentCategory;
    private Div imageUploadDiv;
    private TextArea2 description;
    private Image categoryImage;
    private Div image;
    private Div imagePanel;
    private FormHasCustomField customFieldUtil;
    private ProductCategoryItem item;

    public ProductCategorySummaryView(Integer objectID) {
        super("productcategoryview", wfmStrings.summaryView());
        this.objectID = objectID;
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
                    ProductCategorySummaryView.super.onInitialize();
                }
            }
        });

        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PRODUCT_CATEGORY_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }


    @Override
    public String getIconStyle() {
        return null;
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
    protected void registerFields() {
        initInternal();

        addTitleField(CustomFormConstants.PRODUCT_CATEGORY_TITLE, objectID == null ? wfmStrings.addCategory() : wfmStrings.editCategory());
        addField(CustomFormConstants.NAME, txtName, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.CODE, categoryCode, getTitle(wfmStrings.code(), true));
        addField(CustomFormConstants.CATEGORY, parentCategory, getTitle(wfmStrings.parent()));
        addField(CustomFormConstants.ORDER, txtOrder, getTitle(wfmStrings.order()));
        addField(CustomFormConstants.ACTIVE, active, getTitle(wfmStrings.active()));
        addField(CustomFormConstants.DESCRIPTION, description, null);
        addField(CustomFormConstants.IMAGE_UPLOAD, imageUploadDiv, getTitle(wfmStrings.uploadImage()));

        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initInternal() {

        txtName = new HTML();
        txtName.addStyleName(DEFAULT_WIDTH);

        parentCategory = new HTML();
        parentCategory.addStyleName(DEFAULT_WIDTH);

        categoryCode = new HTML();
        categoryCode.addStyleName(DEFAULT_WIDTH);

        txtOrder = new HTML();
        txtOrder.addStyleName(DEFAULT_WIDTH);

        active = new HTML();
        active.addStyleName(DEFAULT_WIDTH);

        description = new TextArea2(wfmStrings.description());

        categoryImage = new Image();
        categoryImage.setWidth("200px");
        categoryImage.setHeight("250px");
        image = new Div();
        image.setClass("btn-uploaded-img btn-uploaded--has-control");
        image.add(categoryImage);

        imagePanel = new Div();
        imagePanel.setClass("form-upload__attached");
        imagePanel.add(image);
        imagePanel.setVisible(false);

        imageUploadDiv = new Div();
        imageUploadDiv.add(imagePanel);
        imageUploadDiv.setClass("form-upload");

    }

    private void loadData() {
        AccountingService.App.get().getProductCategory(objectID, new AbstractAsyncCallback<ProductCategoryItem>() {
            public void failure(Throwable caught) {
                GWT.log(caught.getMessage());
            }

            public void success(ProductCategoryItem result) {
                item = result;

                txtName.setText(result.getName());
                categoryCode.setText(result.getCode());
                description.setText(result.getDescription());
                String userLanguage = Utils.getUserLanguage();
                if (mapHasValueForLang(result.getDescriptionLocalize(), Utils.getUserLanguage())) {
                    description.setText(result.getDescriptionLocalize().get(userLanguage));
                }

                txtOrder.setText(result.getOrder().toString());
                active.setText(result.isActive() ? wfmStrings.active() : wfmStrings.inactive());
                parentCategory.setText(result.getParentCategoryName());

                if (result.getImageUrl() != null) {
                    categoryImage.setUrl(result.getImageUrl());
                    categoryImage.setVisible(true);
                    imagePanel.setVisible(true);
                }
                getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFields(), true);
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


    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("productcategory|edit/" + item.getId(), item.getName());
        });
        addButton(editButton);
    }

    @Override
    protected void getDataToFillFields() {
        loadData();
    }
}
