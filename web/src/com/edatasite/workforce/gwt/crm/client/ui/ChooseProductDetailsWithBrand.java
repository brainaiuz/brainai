package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.BrandLookUp;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ProductCategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChooseProductDetailsWithBrand extends FlexTable implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private BrandLookUp brands;
    private ProductCategoryLookUp productCategories;
    private ProductLookUp products;

    private CustomForm customForm;


    public ChooseProductDetailsWithBrand(CustomForm customForm) {
        this.customForm = customForm;
        setWidth("100%");
        init();
    }

    private void init() {
        setWidget(0, 0, getBrandLookUpField());
        setWidget(1, 0, getProductCategoryLookUpField());
        setWidget(2, 0, getProductLookUpField());
    }

    private FormGroup getBrandLookUpField() {
        if (brands == null) {
            brands = new BrandLookUp();
            brands.getSuggestBox().addSelectionHandler(event -> {
                productCategories.setEnabled(true);
                productCategories.clearAndClearItems();
                productCategories.refreshOracle(true);
            });
        }
        FormGroup f = new FormGroup();
        f.setLabel(wfmStrings.brand());
        f.setContent(brands);
        return f;
    }

    private FormGroup getProductCategoryLookUpField() {
        if (productCategories == null) {
            productCategories = new ProductCategoryLookUp();
            productCategories.getSuggestBox().addSelectionHandler(event -> {
                products.setEnabled(true);
                products.clearAndClearItems();
                products.refreshOracle(true);
            });
            productCategories.setBeforeSearch(() -> {
                productCategories.getFilterParametrs().setBrandID(brands.getSelectedItemID());
                productCategories.getFilterParametrs().setIDsOnly(true);
            });
        }
        FormGroup f = new FormGroup();
        f.setLabel(wfmStrings.product() + " " + wfmStrings.category());
        f.setContent(productCategories);
        return f;
    }

    private FormGroup getProductLookUpField() {
        if (products == null) {
            products = new ProductLookUp(Constants.RECEIVABLE);
            products.setBeforeSearch(() -> {
                products.getFilterParametrs().setCategoryID(productCategories.getSelectedItemID());
                products.getFilterParametrs().setBrandID(brands.getSelectedItemID());
                products.getFilterParametrs().setIDsOnly(true);
            });
        }
        FormGroup f = new FormGroup();
        f.setLabel(wfmStrings.product());
        f.setContent(products);
        return f;
    }

    public int validate(int errors) {
        errors += customForm.markAsError(brands, brands.getSelectedItem() == null);
        errors += customForm.markAsError(productCategories, productCategories.getSelectedItem() == null);
        errors += customForm.markAsError(products, products.getSelectedItem() == null);
        return errors;
    }

    public Map<String, Integer> getSelectedItems() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("brand", brands.getSelectedItemID());
        map.put("productCategory", productCategories.getSelectedItemID());
        map.put("product", products.getSelectedItemID());
        return map;
    }

    public void setDataItems(CaseItem item) {
        if (brands != null && item.getBrand() != null) {
            brands.setSelected(item.getBrand());
        }
        if (productCategories != null && item.getProductCategory() != null) {
            productCategories.setSelected(item.getProductCategory());
        }
        if (products != null && item.getProduct() != null) {
            products.setSelected(item.getProduct());
        }
    }
}
