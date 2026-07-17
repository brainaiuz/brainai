package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 4, 2011
 * Time: 11:31:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsImportItem implements IsSerializable {
    private Integer objectId;
    private CustomisedImportData accounType;
    private Integer parentCodeId;
    private Integer codeId;
    private Integer nameId;
    private Integer decriptionId;
    private CustomisedImportData taxRate;
    private Integer showInExpenseId;
    private Integer enablePaymentsId;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public CustomisedImportData getAccounType() {
        return accounType;
    }

    public void setAccounType(CustomisedImportData accounType) {
        this.accounType = accounType;
    }

    public Integer getParentCodeId() {
        return parentCodeId;
    }

    public void setParentCodeId(Integer parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public Integer getDecriptionId() {
        return decriptionId;
    }

    public void setDecriptionId(Integer decriptionId) {
        this.decriptionId = decriptionId;
    }

    public CustomisedImportData getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(CustomisedImportData taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getShowInExpenseId() {
        return showInExpenseId;
    }

    public void setShowInExpenseId(Integer showInExpenseId) {
        this.showInExpenseId = showInExpenseId;
    }

    public Integer getEnablePaymentsId() {
        return enablePaymentsId;
    }

    public void setEnablePaymentsId(Integer enablePaymentsId) {
        this.enablePaymentsId = enablePaymentsId;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectId());
        return importFile;
    }

    private ImportFile createColumns(ChartOfAccountsImportItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_ACCOUNT_TYPE, item.getAccounType().getCsvColumnId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_PARENT_CODE, item.getParentCodeId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_CODE, item.getCodeId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_NAME, item.getNameId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_DESCRIPTION, item.getDecriptionId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_TAX_RATE, item.getTaxRate().getCsvColumnId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_SHOW_IN_EXPENSE, item.getShowInExpenseId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.FIELD_ENABLE_PAYMENT, item.getEnablePaymentsId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.SYSTEM_ACCOUNT_TYPE, item.getAccounType().getSystemSelectedId());
            importFile.addColumn(ImportField.ChartOfAccountsFields.SYSTEM_TAX_RATE, item.getTaxRate().getSystemSelectedId());
        }
        return importFile;
    }
}
