package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.importfile.client.ImportAbstractView;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT;
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_PRODUCT_CATEGORIES_FORM;

public class ImportProductCategoriesView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox code;
    private DataListBox name;
    private DataListBox parent;
    private DataListBox order;

    public ImportProductCategoriesView(Integer objectId) {
        super("importpaymentdeductionadd", "Import Product Categories");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        code = new DataListBox();
        String importPCView = "import_product_categories_view_";
        code.ensureDebugId(importPCView + "code");
        code.addStyleName(DEFAULT_WIDTH);

        name = new DataListBox();
        name.ensureDebugId(importPCView + "name");
        name.addStyleName(DEFAULT_WIDTH);

        parent = new DataListBox();
        parent.ensureDebugId(importPCView + "parent");
        parent.addStyleName(DEFAULT_WIDTH);

        order = new DataListBox();
        order.ensureDebugId(importPCView + "order");
        order.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.CODE, code, getTitle(wfmStrings.code(), true));
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.categoryName(), true));
        addField(CustomFormConstants.PARENT, parent, getTitle(wfmStrings.parent(), false));
        addField(CustomFormConstants.ORDER, order, getTitle(wfmStrings.order(), false));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Project;
    }

    @Override
    public void setItems(SelectItem[] items) {
        code.setItems(items, wfmStrings.code());
        name.setItems(items, wfmStrings.name());
        parent.setItems(items, wfmStrings.parent());
        order.setItems(items, wfmStrings.order());
    }

    private ImportFile createColumns(ProductCategoryItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.ProductCategoriesFields.FIELD_NAME, item.getNameId() != null ? item.getNameId() : -1);
        importFile.addColumn(ImportField.ProductCategoriesFields.FIELD_CODE, item.getCodeId() != null ? item.getCodeId() : -1);
        importFile.addColumn(ImportField.ProductCategoriesFields.FIELD_PARENT_CATEGORY, item.getParentCategoryID() != null ? item.getParentCategoryID() : -1);
        importFile.addColumn(ImportField.ProductCategoriesFields.FIELD_ORDER, item.getOrder() != null ? item.getOrder() : -1);

        return importFile;
    }

    private ProductCategoryItem getRPC() {
        ProductCategoryItem item = new ProductCategoryItem();
        item.setId(objectId);
        item.setCodeId(getSelectedItem(code));
        item.setNameId(getSelectedItem(name));
        item.setParentCategoryID(getSelectedItem(parent));
        item.setOrder(getSelectedItem(order));
        return item;
    }

    @Override
    protected ImportFile getImportFile() {
        ImportFile importFile = createColumns(getRPC());
        importFile.setFileID(objectId);
        return importFile;
    }

    @Override
    protected ImportTypeEnum getType() {
        return ImportTypeEnum.PRODUCT_CATEGORIES;
    }

    @Override
    protected String getFormID() {
        return IMPORT_PRODUCT_CATEGORIES_FORM;
    }

    @Override
    protected String getFormType() {
        return IMPORT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
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
