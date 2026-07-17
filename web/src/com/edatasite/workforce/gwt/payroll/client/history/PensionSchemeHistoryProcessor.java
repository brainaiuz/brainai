package com.edatasite.workforce.gwt.payroll.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.payroll.client.PensionSchemeAddSinksContainer;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionSchemeListSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 4:19:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class PensionSchemeHistoryProcessor implements HistoryProcessor {
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new PensionSchemeListSinksContainer(containerName + strings[0], (strings[0].equals(""))?payrollStrings.pensionSchemes():payrollStrings.pensionSchemeDetails(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new PensionSchemeAddSinksContainer("pensionschemeadd", (params.length > 1)?payrollStrings.editPensionScheme():payrollStrings.newPensionScheme(), params);
    }
}
