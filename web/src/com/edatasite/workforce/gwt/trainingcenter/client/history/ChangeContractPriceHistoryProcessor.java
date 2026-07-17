package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.ChangeContractPriceSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 09.01.14
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */
public class ChangeContractPriceHistoryProcessor implements HistoryProcessor {

    private static TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new ChangeContractPriceSinksContainer(containerName + strings[0], tcStrings.changePrices(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
