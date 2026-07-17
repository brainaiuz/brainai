/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/13 6:50:14                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.CrmAccountEditSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:27:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountEditHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CrmAccountEditSinksContainer(containerName + strings[0], wfmStrings.editAccount(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}