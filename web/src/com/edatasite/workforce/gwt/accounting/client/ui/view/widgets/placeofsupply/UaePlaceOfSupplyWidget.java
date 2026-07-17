package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.Command;

public class UaePlaceOfSupplyWidget extends PlaceOfSupplyWidget {

    public UaePlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges) {
        super(cmdPlaceOfSupplyChanges, cmdReverseChargeChanges);
    }

    public UaePlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges, String type) {
        super(cmdPlaceOfSupplyChanges, cmdReverseChargeChanges, type);
    }

    @Override
    protected void initPlaceOfSupplies(SelectItem placeOfSupply) {
        CommonService.App.get().getPlaceOfSupply(taxTreatment.getCode(), getCallback(placeOfSupply));
    }

    @Override
    protected void onPlaceOfSupplyChanged(SelectItem placeOfSupply) {
        if (placeOfSupply == null) {
            return;
        }
        String taxTreatmentCode = taxTreatment != null ? taxTreatment.getCode() : null;
        if (GCC_NON_VAT_REGISTERED.equals(taxTreatmentCode) || GCC_VAT_REGISTERED.equals(taxTreatmentCode)) {
            reverseChargeField.setVisible(!PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(placeOfSupply.getCategory()));

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
