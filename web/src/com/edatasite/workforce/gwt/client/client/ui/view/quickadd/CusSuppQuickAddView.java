package com.edatasite.workforce.gwt.client.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Heading;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 10/2/12
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CusSuppQuickAddView extends KpiSideNavBox {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private String params = "";
    private String accountType;

    public CusSuppQuickAddView(String accountType, String params) {
        super();

        this.accountType = accountType;
        this.params = params;

        initialize();
    }

    public void initialize() {
        CusSuppQuickAddForm quickAddForm = new CusSuppQuickAddForm(accountType);
        quickAddForm.setDefaultName(params);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(CrmAccountItem.CUSTOMER.equals(accountType) ? Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()) : Property.get(Constants.SUPPLIER_LIST, wfmStrings.addMess(), wfmStrings.supplier()));

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveBtn.ensureDebugId("customer_quickadd--save");

        saveBtn.addClickHandler(event -> {
            saveBtn.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                saveBtn.setEnabled(true);
            }
        });
        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                saveBtn.setEnabled(true);
                if (id > 0) {
                    quickAddForm.clearForm();
                    remove();

                    if (CrmAccountItem.CUSTOMER.equals(accountType)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CLIENT_ADD, id, CusSuppQuickAddView.this);
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_ADD, id, CusSuppQuickAddView.this);
                    }
                }
            }
        });

        addOpenedHandler(event -> quickAddForm.getCustomerQuickData());
        addHeader(header);
        addBody(quickAddForm);
        addFooter(saveBtn);
        show();
    }
}
