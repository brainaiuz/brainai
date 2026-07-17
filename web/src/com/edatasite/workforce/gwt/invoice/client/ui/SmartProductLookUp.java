package com.edatasite.workforce.gwt.invoice.client.ui;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataItem;
import com.google.gwt.user.client.Command;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 8/21/12
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartProductLookUp extends ProductLookUp {


    private Command linkCommand;

    private Integer rfpItemid; //TODO resolve when EditableTable can store property
    private boolean soldOut;//Purchase Invoice Item is Sold Out
    private ShippingDataItem usedInGrn;//Purchase Order Item is Used In Grn

    public SmartProductLookUp(String type) {
        super(type);
    }

    public SmartProductLookUp(String type, String formType) {
        super(type, formType);
    }

    public SmartProductLookUp(String formType, Integer rentalItemId, Date startDate, Date endDate) {
        super(null, formType, rentalItemId, startDate, endDate);
    }

    public SmartProductLookUp(String type, Command linkcommand) {
        super(type);
        oracle.setLinkCommand(linkcommand);
        oracle.setIsvisiblelink(Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_ADD : PermissionConstants.ACCOUNTING_PRODUCT_ADD));
    }

//    public SmartProductLookUp(String type, Boolean showOnOpportunity) {
//        super(type, showOnOpportunity);
//    }


    @Override
    public ProductSelectItem getSelectedItem() {
        if (getSelectedData() != null && getSelectedData() instanceof ProductSelectItem) {
            return (ProductSelectItem) getSelectedData();
        }
        return (ProductSelectItem) super.getSelectedItem();
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

    public Command getLinkCommand() {
        return linkCommand;
    }

    public void setLinkCommand(Command linkCommand) {
        this.linkCommand = linkCommand;
        oracle.setLinkCommand(linkCommand);
        oracle.setIsvisiblelink(Utils.hasPermission(Utils.isLogistics() ? PermissionConstants.LOGISTICS_PRODUCT_ADD : !Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_QUICK_ADD) ? Utils.hasPermission(PermissionConstants.ACCOUNTING_PRODUCT_ADD) ? PermissionConstants.ACCOUNTING_PRODUCT_ADD : PermissionConstants.ACCOUNTING_INVENTORY_ADD : PermissionConstants.ACCOUNTING_PRODUCT_QUICK_ADD));
    }

    public Integer getRfpItemid() {
        return rfpItemid;
    }

    public void setRfpItemid(Integer rfpItemid) {
        this.rfpItemid = rfpItemid;
    }

    public void setSoldOut(boolean soldOut) {
        this.soldOut = soldOut;
    }

    public boolean isSoldOut() {
        return soldOut;
    }

    public void setCategoryId(Integer categoryId) {
        super.setCategoryId(categoryId);
    }

    public void setBrandId(Integer brandId) {
        super.setBrandId(brandId);
    }

    public ShippingDataItem getUsedInGrn() {
        return usedInGrn;
    }

    public void setUsedInGrn(ShippingDataItem usedInGrn) {
        this.usedInGrn = usedInGrn;
    }
}
