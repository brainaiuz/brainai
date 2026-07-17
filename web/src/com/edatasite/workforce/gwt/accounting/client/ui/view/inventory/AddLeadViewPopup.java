package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.crm.client.ui.view.AddLeadView;

/**
 * Created by dilshod on 1/12/2016.
 */
public class AddLeadViewPopup extends KpiModal {

    AddLeadView addLeadView;

    public AddLeadViewPopup(){
        setWidth("1000px");
        setHeight("580px");
        draw();
        setTitle("Add New Lead");
        add(addLeadView);
    }

    private void draw() {
        addLeadView = new AddLeadView(() -> close());
        addLeadView.initializePopupView();
    }
}
