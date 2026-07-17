package com.edatasite.workforce.gwt.core.client.history;


import com.edatasite.workforce.gwt.core.client.CustomFormItemSinksContainer;
import com.edatasite.workforce.gwt.core.client.CustomFormItemViewSinksContainer;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.google.gwt.core.client.GWT;

public class CustomFormItemHistoryProcessor implements HistoryProcessor {

    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        String name = Property.findByFormId(strings[2]);
        return new CustomFormItemViewSinksContainer(containerName + strings[0], wfmStrings.summaryView() + (name.length() > 0 ? " - " + name : ""), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        String name = null;
        if (params.length > 4) {
            name = params[4];
        } else if (params.length > 3) {
            name = params[3];
        }
        return new CustomFormItemSinksContainer("itemListadd", name, params);
    }
}
