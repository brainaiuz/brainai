package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet.BalancesheetSettings;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
/**
 * Created by IntelliJ IDEA.
 * User: Sherzod Makhmudov
 * Date: 04.05.2011
 * Time: 12:07:07
 * To change this template use File | Settings | File Templates.
 */
public class BalanceSheet implements IsSerializable {
    private BalancesheetSettings settings;
    private HashMap<String, BalanceSheetItem> map;
    private BalanceSheetInnerItem totalAsset;
    private BalanceSheetInnerItem totalLiability;

    public BalanceSheet() {
        map = new HashMap<>();
    }

    public BalancesheetSettings getSettings() {
        return settings;
    }

    public void setSettings(BalancesheetSettings settings) {
        this.settings = settings;
    }

    public BalanceSheetItem getItemByKey(String key) {
        return map.get(key);
    }

    public void putItemsByKey(String key, BalanceSheetItem item){
        map.put(key, item);
    }

    public BalanceSheetInnerItem getTotalAsset() {
        return totalAsset;
    }

    public void setTotalAsset(BalanceSheetInnerItem totalAsset) {
        this.totalAsset = totalAsset;
    }

    public BalanceSheetInnerItem getTotalLiability() {
        return totalLiability;
    }

    public void setTotalLiability(BalanceSheetInnerItem totalLiability) {
        this.totalLiability = totalLiability;
    }
}
