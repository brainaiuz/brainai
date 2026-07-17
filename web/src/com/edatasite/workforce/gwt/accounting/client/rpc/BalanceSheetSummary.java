package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Shohruh
 * Date: 02.07.15
 * Time: 18:01:19
 * To change this template use File | Settings | File Templates.
 */
public class BalanceSheetSummary implements IsSerializable{
    private BalanceSheetItem assets;
    private BalanceSheetItem liabilities;

    public BalanceSheetItem getAssets() {
        return assets;
    }

    public void setAssets(BalanceSheetItem assets) {
        this.assets = assets;
    }

    public BalanceSheetItem getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(BalanceSheetItem liabilities) {
        this.liabilities = liabilities;
    }
}
