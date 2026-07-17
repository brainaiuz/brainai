package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.edatasite.workforce.gwt.importfile.client.rpc.CustomisedImportData;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Shohruh on 09 Nov 2016.
 */
public class AdditionalPaymentImportItem implements IsSerializable{

    private Integer objectId;
    private Integer employeeCode;
    private Integer amount;
    private Integer additionalPaymentDate;
    private CustomisedImportData category;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(Integer employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAdditionalPaymentDate() {
        return additionalPaymentDate;
    }

    public void setAdditionalPaymentDate(Integer additionalPaymentDate) {
        this.additionalPaymentDate = additionalPaymentDate;
    }

    public CustomisedImportData getCategory() {
        return category;
    }

    public void setCategory(CustomisedImportData category) {
        this.category = category;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectId());
        return importFile;
    }

    private ImportFile createColumns(AdditionalPaymentImportItem item) {
        ImportFile importFile = new ImportFile();
        importFile.addColumn(ImportField.AdditionalPaymentImportFields.FIELD_EMPLOYEE_CODE, item.getEmployeeCode());
        importFile.addColumn(ImportField.AdditionalPaymentImportFields.FIELD_AMOUNT, item.getAmount());
        importFile.addColumn(ImportField.AdditionalPaymentImportFields.FIELD_CATEGORY, item.getCategory().getCsvColumnId());
        importFile.addColumn(ImportField.AdditionalPaymentImportFields.FIELD_SYSTEM_CATEGORY, item.getCategory().getSystemSelectedId());
        importFile.addColumn(ImportField.AdditionalPaymentImportFields.FIELD_ADDITIONAL_PAYMENT_DATE, item.getAdditionalPaymentDate());
        return importFile;
    }
}
