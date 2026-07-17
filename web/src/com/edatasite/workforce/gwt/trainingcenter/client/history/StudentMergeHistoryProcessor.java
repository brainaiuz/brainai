package com.edatasite.workforce.gwt.trainingcenter.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.trainingcenter.client.container.StudentMergeSinksContainer;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 12/4/12
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentMergeHistoryProcessor implements HistoryProcessor {
    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new StudentMergeSinksContainer(Constants.MERGE + "add", "Merge", params);
    }

}