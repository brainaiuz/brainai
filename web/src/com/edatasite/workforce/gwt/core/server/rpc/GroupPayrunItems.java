package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/23/15
 * Time: 4:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupPayrunItems implements Serializable {

    private Integer id;
    private String name;
    private String code;
    private DateNonConvertable hireDate;
    private Integer countryid;
    private List<PaymentDeductionObject> paymentdeductions;
    private KeyValueStruct[] settings;
    private HashMap<String, String> settingsMap;
    private Integer currencyId;

    public GroupPayrunItems() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DateNonConvertable getHireDate() {
        return hireDate;
    }

    public void setHireDate(DateNonConvertable hireDate) {
        this.hireDate = hireDate;
    }

    public Integer getCountryid() {
        return countryid;
    }

    public void setCountryid(Integer countryId) {
        this.countryid = countryId;
    }

    public List<PaymentDeductionObject> getPaymentDeductions() {
        if (paymentdeductions == null) {
            paymentdeductions = new ArrayList<>();
        }
        return paymentdeductions;
    }

    public void setPaymentDeductions(List<PaymentDeductionObject> paymentdeductions) {
        this.paymentdeductions = paymentdeductions;
    }

    public HashMap<String, String> getSettingsMap() {
        if (settingsMap == null) {
            settingsMap = new HashMap<>();
        }
        return settingsMap;
    }

    public void setSettingsMap(HashMap<String, String> settingsMap) {
        this.settingsMap = settingsMap;
    }

    public List<PaymentDeductionObject> getPaymentdeductions() {
        return paymentdeductions;
    }

    public void setPaymentdeductions(List<PaymentDeductionObject> paymentdeductions) {
        this.paymentdeductions = paymentdeductions;
    }

    public KeyValueStruct[] getSettings() {
        return settings;
    }

    public void setSettings(KeyValueStruct[] settings) {
        this.settings = settings;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }
}
