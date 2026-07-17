package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.TaxLookUp;
import com.google.gwt.user.client.ui.TextBox;

public class UKTreatmentSettingsWidget extends AbstractTreatmentSettingsWidget {
    private DataListBox taxTreatments;
    private TextBox trnBox;

    @Override
    protected void onInitialize() {
        taxTreatments = new DataListBox();
        trnBox = new TextBox();
        if (!Utils.isVATCashBased()) {
            container.add(new FormGroup(wfmStrings.taxTreatment(), taxTreatments, true));
        }
        container.add(new FormGroup(wfmStrings.trn(), trnBox));
    }

    @Override
    public void setData(CrmAccountItem crmAccountItem) {
        taxTreatments.setItems(crmAccountItem.getTaxTreatments());
        taxTreatments.setSelected(crmAccountItem.getTaxTreatment());
        trnBox.setText(crmAccountItem.getTrn());
    }

    @Override
    public CrmAccountItem getData(CrmAccountItem crmAccountItem) {
        crmAccountItem.setTaxTreatment(taxTreatments.getSelectedItem());
        crmAccountItem.setTaxTreatmentId(taxTreatments.getSelectedId());
        crmAccountItem.setTrn(trnBox.getText());
        return crmAccountItem;
    }

    @Override
    public boolean validate() {
        return Utils.isVATCashBased() || Validation.validateListBoxRequired(taxTreatments);
    }

    @Override
    public void setTreatment(SelectItem treatment) {
        taxTreatments.setSelected(treatment);
    }

}
