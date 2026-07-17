package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.Command;

public class KsaPlaceOfSupplyWidget extends PlaceOfSupplyWidget {

    public KsaPlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges) {
        super(cmdPlaceOfSupplyChanges, cmdReverseChargeChanges);
    }

    public KsaPlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges, String type) {
        super(cmdPlaceOfSupplyChanges, cmdReverseChargeChanges, type);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        CommonService.App.get().getPlaceOfSupply(null, getCallback(null));
    }

    @Override
    protected void initPlaceOfSupplies(SelectItem placeOfSupply) {
        if (VAT_REGISTERED.equals(taxTreatment.getCode()) || NON_VAT_REGISTERED.equals(taxTreatment.getCode())) {
            placeOfSupplyBox.setSelected(defaultPlaceOfSupply.getId());
            placeOfSupplyBox.setEnabled(false);

            if (cmdPlaceOfSupplyChanges != null) {
                cmdPlaceOfSupplyChanges.execute();
            }
        } else {
            placeOfSupplyBox.setSelected(placeOfSupply != null ? placeOfSupply.getId() : defaultPlaceOfSupply.getId());
            placeOfSupplyBox.setEnabled(true);

            if (placeOfSupply != null) {
                onPlaceOfSupplyChanged(placeOfSupply);
            } else if (cmdPlaceOfSupplyChanges != null) {
                cmdPlaceOfSupplyChanges.execute();
            }
        }
    }

    @Override
    protected void onPlaceOfSupplyChanged(SelectItem placeOfSupply) {
        if (placeOfSupply == null) {
            return;
        }
        String taxTreatmentCode = taxTreatment != null ? taxTreatment.getCode() : null;
        if (GCC_NON_VAT_REGISTERED.equals(taxTreatmentCode) || GCC_VAT_REGISTERED.equals(taxTreatmentCode)) {
            reverseChargeField.setVisible(SA.equalsIgnoreCase(placeOfSupply.getCode()));

            if (reverseChargeField.isVisible()) {
                reverseChargeField.setContent(reverseChargeBox);
            } else {
                reverseChargeField.getGroupContent().clear();
            }
        }
        if (cmdPlaceOfSupplyChanges != null) {
            cmdPlaceOfSupplyChanges.execute();
        }
    }
}
