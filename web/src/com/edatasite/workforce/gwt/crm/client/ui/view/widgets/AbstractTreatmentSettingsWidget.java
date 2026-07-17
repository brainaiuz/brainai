package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.html.Div;

public abstract class AbstractTreatmentSettingsWidget extends Composite implements Constants {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected Div container;

    public AbstractTreatmentSettingsWidget() {
        container = new Div();
        initWidget(container);
        onInitialize();
    }

    protected abstract void onInitialize();

    public abstract void setData(CrmAccountItem crmAccountItem);

    public abstract CrmAccountItem getData(CrmAccountItem crmAccountItem);

    public abstract boolean validate();

    public abstract void setTreatment(SelectItem treatment);
}
