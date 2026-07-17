package com.edatasite.workforce.gwt.client.client.ui.view.tabpanels.supplier;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 13, 2010
 * Time: 4:13:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierBankAccountDetailsTab extends CustomTabWidget {

    private CrmAccountItem supplierItem;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SupplierBankAccountDetailsTab(String tabName, CrmAccountItem supplierItem) {
        super(tabName);
        this.supplierItem = supplierItem;
    }

    @Override
    public void initData() {
    }

    @Override
    public void viewShow() {
        PreviewSectionField field = new PreviewSectionField("40%", "60%");
        field.addField(wfmStrings.bankName(), supplierItem.getBankName() != null ? supplierItem.getBankName() : "");
        field.addField(wfmStrings.accountName(), supplierItem.getAccountName() != null ? supplierItem.getAccountName() : "");
        field.addField(wfmStrings.accountNo(), supplierItem.getAccountNo() != null ? supplierItem.getAccountNo() : "");
        field.addField(wfmStrings.swiftCode(), supplierItem.getSwiftCode() != null ? supplierItem.getSwiftCode() : "");
        field.addField(wfmStrings.sortCode(), supplierItem.getSortCode() != null ? supplierItem.getSortCode() : "");
        field.addField(wfmStrings.ibanCode(), supplierItem.getIbanCode() != null ? supplierItem.getIbanCode() : "");
        add(field);
    }
}
