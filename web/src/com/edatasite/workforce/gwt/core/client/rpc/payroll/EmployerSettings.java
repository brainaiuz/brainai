package com.edatasite.workforce.gwt.core.client.rpc.payroll;

import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 5/21/15
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmployerSettings implements IsSerializable {

    private KeyValueStruct[] settings;
    private HashMap<String, ArrayList<PaymentDeductionSelectItem>> allowances;
    private PaymentDeductionSelectItem leaveMoneyTypeCategory;

    public KeyValueStruct[] getSettings() {
        return settings;
    }

    public void setSettings(KeyValueStruct[] settings) {
        this.settings = settings;
    }

    public HashMap<String, ArrayList<PaymentDeductionSelectItem>> getAllowancesMap() {
        if (allowances == null) {
            allowances = new HashMap<>();
        }
        return allowances;
    }

    public ArrayList<PaymentDeductionSelectItem> getAllowances(String key) {
        getAllowancesMap().computeIfAbsent(key, k -> new ArrayList<>());
        return getAllowancesMap().get(key);
    }

    public PaymentDeductionSelectItem getLeaveMoneyTypeCategory() {
        return leaveMoneyTypeCategory;
    }

    public void setLeaveMoneyTypeCategory(PaymentDeductionSelectItem leaveMoneyTypeCategory) {
        this.leaveMoneyTypeCategory = leaveMoneyTypeCategory;
    }
}
