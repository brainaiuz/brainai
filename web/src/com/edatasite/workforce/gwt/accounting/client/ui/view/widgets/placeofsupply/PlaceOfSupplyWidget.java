package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.placeofsupply.PlaceOfSupplyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import java.util.ArrayList;
import java.util.List;

public abstract class PlaceOfSupplyWidget extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String widgetCode = "PlaceOfSupplyWidget";
    protected WfmDropdown placeOfSupplyBox;
    protected KpiCheckBox reverseChargeBox;
    protected FormGroup placeOfSupplyField;
    protected FormGroup reverseChargeField;
    /**
     * Customer/Supplier's tax treatment
     */
    protected SelectItem taxTreatment;
    /**
     * Customer/Supplier's place of supply
     */
    protected SelectItem companyPlaceOfSupply;
    /**
     * Organization default place of supply
     */
    protected SelectItem defaultPlaceOfSupply;
    protected Command cmdPlaceOfSupplyChanges;
    protected Command cmdReverseChargeChanges;
    protected String type = PAYABLE;
    private GRow container;

    public PlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges) {
        this(cmdPlaceOfSupplyChanges, cmdReverseChargeChanges, PAYABLE);
    }

    public PlaceOfSupplyWidget(Command cmdPlaceOfSupplyChanges, Command cmdReverseChargeChanges, String type) {
        this.cmdPlaceOfSupplyChanges = cmdPlaceOfSupplyChanges;
        this.cmdReverseChargeChanges = cmdReverseChargeChanges;
        this.type = !Utils.isNullOrEmpty(type) ? type : PAYABLE;

        container = new GRow();
        initWidget(container);
        onInitialize();
    }

    /**
     * Company(Customer/Supplier) tax settings and transaction place of supply will be initialized in this form
     *
     * @param taxTreatment         - Customer/Supplier tax treatment settings
     * @param companyPlaceOfSupply - Customer/Supplier place of supply
     * @param tranPlaceOfSupply    - transaction place of supply. Transaction consists SI, PI, SO/SQ, PI, CN and DN
     */
    public void setData(SelectItem taxTreatment, SelectItem companyPlaceOfSupply, SelectItem tranPlaceOfSupply) {
        container.clear();

        if (taxTreatment == null || companyPlaceOfSupply == null) {
            return;
        }
        this.taxTreatment = taxTreatment;
        this.companyPlaceOfSupply = companyPlaceOfSupply;
        List<GColumn> fields = new ArrayList<>();

        boolean isNonGccCompany = NON_GCC.equals(taxTreatment.getCode());
        boolean isGccCompany = GCC_VAT_REGISTERED.equals(taxTreatment.getCode()) || GCC_NON_VAT_REGISTERED.equals(taxTreatment.getCode());

        if (isNonGccCompany && PAYABLE.equals(type)) {
            fields.add(new GColumn(GColumnEnum.COL_4, reverseChargeField));
        } else if (isGccCompany) {
            if (companyPlaceOfSupply != null && GCC_REGISTERED.contains(companyPlaceOfSupply.getCode())) {
                fields.add(new GColumn(GColumnEnum.COL_3, placeOfSupplyField));
            }
            if (PAYABLE.equals(type)) {
                fields.add(new GColumn(GColumnEnum.COL_4, reverseChargeField));
            }
        } else {
            if (!isNonGccCompany) {
                fields.add(new GColumn(GColumnEnum.COL_3, placeOfSupplyField));
            }
        }
        container.addAll(fields.toArray(new GColumn[]{}));

        initPlaceOfSupplies(tranPlaceOfSupply);
    }

    public boolean validate() {
        boolean isFormValid = true;

        if (placeOfSupplyBox.isAttached() && !Validation.validateWfmDropdown(placeOfSupplyBox)) {
            isFormValid = false;
        }
        return isFormValid;
    }

    public SelectItem getTaxTreatment() {
        return taxTreatment;
    }

    public SelectItem getCompanyPlaceOfSupply() {
        return companyPlaceOfSupply;
    }

    public SelectItem getDefaultPlaceOfSupply() {
        return defaultPlaceOfSupply;
    }

    public SelectItem getSelectedPlaceOfSupply() {
        if (!placeOfSupplyBox.isAttached() || !placeOfSupplyField.isAttached() || !placeOfSupplyField.isVisible()) {
            return null;
        }
        if (placeOfSupplyBox.getSelectedId() != null && placeOfSupplyBox.getSelectedId() > 0) {
            return placeOfSupplyBox.getSelected();
        }
        return null;
    }

    public WfmDropdown getPlaceOfSupplyBox() {
        return placeOfSupplyBox;
    }

    public KpiCheckBox getReverseChargeBox() {
        return reverseChargeBox;
    }

    protected void onInitialize() {
        placeOfSupplyBox = new WfmDropdown();
        placeOfSupplyBox.ensureDebugId(widgetCode + "placeOfSupplyBox");
        placeOfSupplyBox.addValueChangeHandler(ch -> {
            onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
        });

        reverseChargeBox = new KpiCheckBox();
        reverseChargeBox.ensureDebugId(widgetCode + "reverseCharge");
        reverseChargeBox.setText("This transaction is applicable for reverse charge");
        reverseChargeBox.addValueChangeHandler(valueChangeEvent -> {
            if (cmdPlaceOfSupplyChanges != null) {
                cmdPlaceOfSupplyChanges.execute();
            }
            if (cmdReverseChargeChanges != null) {
                cmdReverseChargeChanges.execute();
            }
        });

        placeOfSupplyField = new FormGroup(wfmStrings.sourceOfSupply(), placeOfSupplyBox, true);
        reverseChargeField = new FormGroup("&nbsp;", reverseChargeBox);

        CommonService.App.get().getDefaultPlaceOfSupply(new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem selectItem) {
                defaultPlaceOfSupply = selectItem;
            }
        });
    }

    protected abstract void initPlaceOfSupplies(SelectItem placeOfSupply);

    protected abstract void onPlaceOfSupplyChanged(SelectItem placeOfSupply);

    protected AsyncCallback getCallback(SelectItem placeOfSupply) {
        return new AsyncCallback<PlaceOfSupplyItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PlaceOfSupplyItem placeOfSupplyItem) {
                placeOfSupplyBox.clear();

                if (placeOfSupplyItem != null) {

                    if (placeOfSupplyItem.getStates() != null && placeOfSupplyItem.getStates().length > 0) {
                        placeOfSupplyBox.addItems("UAE Emirates", placeOfSupplyItem.getStates());
                    }
                    if (placeOfSupplyItem.getCountries() != null && placeOfSupplyItem.getCountries().length > 0) {
                        placeOfSupplyBox.addItems("GCC Member States", placeOfSupplyItem.getCountries());
                    }
                    if (placeOfSupply != null) {
                        placeOfSupplyBox.setSelected(placeOfSupply.getId());
                        //onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
                    } else if (defaultPlaceOfSupply != null) {
                        placeOfSupplyBox.setSelected(defaultPlaceOfSupply.getId());
                        onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
                    }
                }
            }
        };
    }

}
