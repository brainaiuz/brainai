package com.edatasite.workforce.gwt.accounting.client;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.crm.client.ui.view.ImportClientView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 6, 2011
 * Time: 7:50:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportSupplierView extends ImportClientView {
    private DataListBox balanceAmount;
    private DataListBox bankName;
    private DataListBox accountName;
    private DataListBox accountNo;
    private DataListBox swiftCode;
    private DataListBox sortCode;
    private DataListBox ibanCode;
    private DataListBox branch;
    private DataListBox bankAddress;
    private String importSupplierView = "import_supplier_view_";

    public ImportSupplierView(Integer objectId, Date conversionDate) {
        super("addimportsupplier", Property.get(Constants.SUPPLIER_LIST, wfmStrings.importSupplier(), wfmStrings.supplier()), objectId);
        viewName = Property.get(Constants.SUPPLIER_LIST, wfmStrings.importSupplier(), wfmStrings.supplier());
        successMessage = wfmMessages.messItemSucImported(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
        errorMessage = wfmMessages.messImportItemError(Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()));
        this.conversionDate = conversionDate;
    }

    @Override
    public ImportTypeEnum getImportType() {
        return ImportTypeEnum.SUPPLIER;
    }

    public void initialize() {
        super.initialize();

        balanceAmount = new DataListBox();
        balanceAmount.ensureDebugId(importSupplierView + "balanceAmount");
        balanceAmount.addStyleName(DEFAULT_WIDTH);

        bankName = new DataListBox();
        bankName.ensureDebugId(importSupplierView + "bankName");
        bankName.addStyleName(DEFAULT_WIDTH);

        accountName = new DataListBox();
        accountName.ensureDebugId(importSupplierView + "accountName");
        accountName.addStyleName(DEFAULT_WIDTH);

        accountNo = new DataListBox();
        accountNo.ensureDebugId(importSupplierView + "accountNo");
        accountNo.addStyleName(DEFAULT_WIDTH);

        swiftCode = new DataListBox();
        swiftCode.ensureDebugId(importSupplierView + "swiftCode");
        swiftCode.addStyleName(DEFAULT_WIDTH);

        sortCode = new DataListBox();
        sortCode.ensureDebugId(importSupplierView + "sortCode");
        sortCode.addStyleName(DEFAULT_WIDTH);

        ibanCode = new DataListBox();
        ibanCode.ensureDebugId(importSupplierView + "ibanCode");
        ibanCode.addStyleName(DEFAULT_WIDTH);

        branch = new DataListBox();
        branch.ensureDebugId(importSupplierView + "branch");
        branch.addStyleName(DEFAULT_WIDTH);

        bankAddress = new DataListBox();
        bankAddress.ensureDebugId(importSupplierView + "bankAddress");
        bankAddress.addStyleName(DEFAULT_WIDTH);
    }

    @Override
    public void setItems() {
        super.setItems();
        balanceAmount.setItems(items, wfmStrings.openingBalance());
        bankName.setItems(items, wfmStrings.bankName());
        accountName.setItems(items, wfmStrings.accountName());
        accountNo.setItems(items, wfmStrings.accountNo());
        swiftCode.setItems(items, wfmStrings.swiftCode());
        sortCode.setItems(items, wfmStrings.sortCode());
        ibanCode.setItems(items, wfmStrings.ibanCode());
        branch.setItems(items, wfmStrings.branch());
        bankAddress.setItems(items, wfmStrings.bankAddress());
    }

    @Override
    public CrmAccountItem getValuesIntoItem(CrmAccountItem item) {
        item = super.getValuesIntoItem(item);
        item.setBalanceAmountId(balanceAmount.getSelectedId());
        item.setBankNameID(bankName.getSelectedId());
        item.setAccountNameID(accountName.getSelectedId());
        item.setAccountNoID(accountNo.getSelectedId());
        item.setSwiftCodeID(swiftCode.getSelectedId());
        item.setSortCodeID(sortCode.getSelectedId());
        item.setIbanCodeID(ibanCode.getSelectedId());
        item.setBranchID(branch.getSelectedId());
        item.setBankAddressID(bankAddress.getSelectedId());
        return item;
    }

    @Override
    protected void drawForm() {
        super.drawForm();
        addTitleField(SUPPLIER_BANK_ACCOUNT_DETAILS, wfmStrings.bankAccountDetails());
        addField(OPENING_BALANCE, balanceAmount, getTitle(wfmStrings.openingBalance()));
        addField(SUPPLIER_BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        addField(SUPPLIER_ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        addField(SUPPLIER_ACCOUNT_NUMBER, accountNo, getTitle(wfmStrings.accountNo()));
        addField(SUPPLIER_SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        addField(SUPPLIER_SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        addField(SUPPLIER_IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        addField(SUPPLIER_BRANCH, branch, getTitle(wfmStrings.branch()));
        addField(SUPPLIER_BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));

        getCustomFieldUtil().drawCustomFields(this, objectId);
        show();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.IMPORT_SUPPLIER_FORM;
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