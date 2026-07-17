package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.ReferenceAddSinksContainer;
import com.edatasite.workforce.gwt.profile.client.ReferenceSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 7/24/11
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceHistoryProcessor implements HistoryProcessor {

    private WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new ReferenceSinksContainer(containerName + strings[0], "".equals(strings[0]) ? wfmStrings.referencces() : wfmStrings.editReference(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new ReferenceAddSinksContainer("referenceadd", wfmStrings.addReference(), params);
    }
}
