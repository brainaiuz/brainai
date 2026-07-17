package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.client.client.rpc.ClientSupplierAddressData;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 17.08.11
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "clientSupplierAddressData")
public class MClientSupplierAddressData {

    private Integer clientSupplierID;
    private Integer currencyID;
    private Integer crmAccountID;
    private Integer primaryBillAddressID;
    private Integer primaryMailAddressID;
    private List<MSelectItem> billAddresses;
    //private List<MSelectItem>  mailAddresses;
    private String code;

    public MClientSupplierAddressData() {

    }

    public MClientSupplierAddressData(ClientSupplierAddressData clientSupplierAddressData) {
        if (clientSupplierAddressData != null) {
            this.clientSupplierID = clientSupplierAddressData.getClientSupplierID();
            this.currencyID = clientSupplierAddressData.getCurrencyID();
            this.crmAccountID = clientSupplierAddressData.getCrmAccountID();
            this.primaryBillAddressID = clientSupplierAddressData.getPrimaryBillAddressID();
            this.primaryMailAddressID = clientSupplierAddressData.getPrimaryMailAddressID();
            this.billAddresses = WebServiceUtils.getAsMSelectItemList(clientSupplierAddressData.getBillAddresses());
            //this.mailAddresses = WebServiceUtils.getAsMSelectItemList(clientSupplierAddressData.getMailAddresses());
        }
    }


    public Integer getClientSupplierID() {
        return clientSupplierID;
    }

    public void setClientSupplierID(Integer clientSupplierID) {
        this.clientSupplierID = clientSupplierID;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public Integer getCrmAccountID() {
        return crmAccountID;
    }

    public void setCrmAccountID(Integer crmAccountID) {
        this.crmAccountID = crmAccountID;
    }

    public Integer getPrimaryBillAddressID() {
        return primaryBillAddressID;
    }

    public void setPrimaryBillAddressID(Integer primaryBillAddressID) {
        this.primaryBillAddressID = primaryBillAddressID;
    }

    public Integer getPrimaryMailAddressID() {
        return primaryMailAddressID;
    }

    public void setPrimaryMailAddressID(Integer primaryMailAddressID) {
        this.primaryMailAddressID = primaryMailAddressID;
    }

    public List<MSelectItem> getBillAddresses() {
        return billAddresses;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
