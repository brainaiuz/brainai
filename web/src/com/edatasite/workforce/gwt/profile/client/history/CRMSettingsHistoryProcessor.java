/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/5 2:46:36                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.profile.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.profile.client.CRMSettingsSinksContainer;


/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Mar 24, 2010
 * Time: 1:42:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class CRMSettingsHistoryProcessor implements HistoryProcessor {
    private final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new CRMSettingsSinksContainer(containerName + strings[0], wfmStrings.sales(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
