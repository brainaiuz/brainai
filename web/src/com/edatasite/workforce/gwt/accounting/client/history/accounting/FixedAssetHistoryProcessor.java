package com.edatasite.workforce.gwt.accounting.client.history.accounting;

import com.edatasite.workforce.gwt.accounting.client.container.accounting.FixedAssetAddSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.FixedAssetViewSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/6/11
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new FixedAssetViewSinksContainer(containerName + strings[0], Property.get(Constants.FIXED_ASSETS, wfmStrings.summaryView(), wfmStrings.fixedAsset()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new FixedAssetAddSinksContainer("fixedassetadd", wfmStrings.add() + " " + Property.get(Constants.FIXED_ASSETS, wfmStrings.fixedAsset()));
    }
}
