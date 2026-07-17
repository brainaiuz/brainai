package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
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
import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.IMPORT_BRAND_FORM;

public class ImportBrandView extends ImportAbstractView implements Constants, FormHasCustomFieldInterface {

    private DataListBox name;
    private DataListBox parent;
    private DataListBox description;

    public ImportBrandView(Integer objectId) {
        super("importbrandadd", "Import Brand");
        this.objectId = objectId;
    }

    public void initialize() {
        initInternal();
        super.initialize();
    }

    private void initInternal() {
        name = new DataListBox();
        String importbrandview = "import_brand_view_";
        name.ensureDebugId(importbrandview + "name");
        name.addStyleName(DEFAULT_WIDTH);

        parent = new DataListBox();
        parent.ensureDebugId(importbrandview + "parent");
        parent.addStyleName(DEFAULT_WIDTH);

        description = new DataListBox();
        description.ensureDebugId(importbrandview + "code");
        description.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void drawForm() {
        super.drawForm();
        addField(CustomFormConstants.NAME, name, getTitle(wfmStrings.brand(), true));
        addField(CustomFormConstants.PARENT, parent, getTitle(wfmStrings.parent(), false));
        addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description(), false));
    }

    @Override
    protected ViewName getViewName() {
        return ViewName.Brand;
    }

    @Override
    public void setItems(SelectItem[] items) {
        name.setItems(items, wfmStrings.brand());
        parent.setItems(items, wfmStrings.parent());
        description.setItems(items, wfmStrings.description());
    }

    private ImportFile createColumns(BrandItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.BrandFields.FIELD_NAME, item.getNameId() != null ? item.getNameId() : -1);
        importFile.addColumn(ImportField.BrandFields.FIELD_PARENT, item.getParentBrandID() != null ? item.getParentBrandID() : -1);
        importFile.addColumn(ImportField.BrandFields.FIELD_DESCRIPTION, item.getDescriptionId() != null ? item.getDescriptionId() : -1);
        return importFile;
    }

    private BrandItem getRPC() {
        BrandItem item = new BrandItem();
        item.setId(objectId);
        item.setNameId(getSelectedItem(name));
        item.setParentBrandID(getSelectedItem(parent));
        item.setDescriptionId(getSelectedItem(description));
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
        return ImportTypeEnum.BRAND;
    }

    @Override
    protected String getFormID() {
        return IMPORT_BRAND_FORM;
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
