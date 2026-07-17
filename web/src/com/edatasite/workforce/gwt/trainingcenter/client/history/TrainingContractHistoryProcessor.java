package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.TrainingContractAddSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.TrainingContractViewSinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainingContractHistoryProcessor implements HistoryProcessor {

    private TCStrings tcStrings = TCStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] params) {
        return new TrainingContractViewSinksContainer(containerName + params[0], tcStrings.customerContractsView(), params);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        if (params.length == 2 && params[1] != null) {
            return new TrainingContractAddSinksContainer("trainingContractadd", tcStrings.customerContractsEdit(), params);
        } else {
            return new TrainingContractAddSinksContainer("trainingContractadd", tcStrings.customerContractsAdd(), params);
        }

    }
}
