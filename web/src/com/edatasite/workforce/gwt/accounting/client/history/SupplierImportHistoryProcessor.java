package com.edatasite.workforce.gwt.accounting.client.history;

import com.edatasite.workforce.gwt.accounting.client.SupplierImportSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Aug 5, 2009
 * Time: 7:45:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierImportHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return null;
    }

    public SinksContainer processAdd(String[] params) {
        return new SupplierImportSinksContainer("importsupplieradd", wfmStrings.importSupplier(), params);
    }
}