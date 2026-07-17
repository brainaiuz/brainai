/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/13 6:50:14                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.crm.client.WebFormEditSinksContainer;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 3, 2010
 * Time: 6:27:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebFormEditHistoryProcessor implements HistoryProcessor {
    private CrmStrings crmStrings = CrmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new WebFormEditSinksContainer(containerName + strings[0], crmStrings.editCrmForm(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}