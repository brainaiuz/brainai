package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Faxriddin Taslimov on 14.08.19.
 */
public class ReportDataImportItem implements IsSerializable {

    private Integer objectID;
    private Integer categoryID;
    private SelectItem[] items;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectID());
        importFile.setDynamicColumns(getItems());
        return importFile;
    }

    private ImportFile createColumns(ReportDataImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.ReportDataImportFields.CATEGORY_ID, item.getCategoryID());
        return importFile;
    }
}
