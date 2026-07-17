package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.placeofsupply.PlaceOfSupplyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GccTaxTreatmentWidget extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String widgetCode = "GccTaxTreatmentWidget";
    private GRow container;
    private DataListBox taxTreatments;
    private WfmDropdown placeOfSupplyBox;
    private KpiCheckBox reverseChargeBox;
    private FormGroup taxTreatmentField;
    private FormGroup placeOfSupplyField;
    private FormGroup reverseChargeField;

    private Command cmdTreatmentChanges;
    private Command cmdReverseChargeChanges;
    private PlaceOfSupplyItem placeOfSupplyItem;
    private SelectItem defaultPlaceOfSupply;

    private boolean editMode;
    private boolean showTreatmentField;
    private boolean gccRegisteredCusSupp;
    private String type = PAYABLE;

    public GccTaxTreatmentWidget(Command cmdTreatmentChanges, Command cmdReverseChargeChanges) {
        this(cmdTreatmentChanges, cmdReverseChargeChanges, true);
    }

    public GccTaxTreatmentWidget(Command cmdTreatmentChanges, Command cmdReverseChargeChanges, boolean showTreatmentField) {
        this(cmdTreatmentChanges, cmdReverseChargeChanges, showTreatmentField, PAYABLE);
    }

    public GccTaxTreatmentWidget(Command cmdTreatmentChanges, Command cmdReverseChargeChanges, boolean showTreatmentField, String type) {
        this.cmdTreatmentChanges = cmdTreatmentChanges;
        this.cmdReverseChargeChanges = cmdReverseChargeChanges;
        this.showTreatmentField = showTreatmentField;
        this.type = type;
        container = new GRow();
        initWidget(container);
        onInitialize();
    }

    public boolean isPlaceOfSupplyInGccRegistered() {
        ArrayList<String> gccRegisteredPlaces = new ArrayList<>(GCC_REGISTERED);
        gccRegisteredPlaces.remove(Utils.getCompanyrCountryCode());
        SelectItem placeOfSupply = getSelectedPlaceOfSupply();

        if (placeOfSupply != null) {
            return gccRegisteredPlaces.contains(placeOfSupply.getCode());
        }
        return false;
    }

    public FormGroup getReverseChargeField() {
        return reverseChargeField;
    }

    public KpiCheckBox getReverseChargeBox() {
        return reverseChargeBox;
    }

    public WfmDropdown getPlaceOfSupplyBox() {
        return placeOfSupplyBox;
    }

    public SelectItem getSelectedTreatment() {
        if (taxTreatments.getSelectedId() != null && taxTreatments.getSelectedId() > 0) {
            return taxTreatments.getSelectedItem();
        }
        return null;
    }

    public SelectItem getSelectedPlaceOfSupply() {
        if (placeOfSupplyBox.getSelectedId() != null && placeOfSupplyBox.getSelectedId() > 0) {
            return placeOfSupplyBox.getSelected();
        }
        return null;
    }

    public boolean validate() {
        boolean isFormValid = true;

        if (!Validation.validateListBoxRequired(taxTreatments)) {
            isFormValid = false;
        }
        if (placeOfSupplyBox.isAttached() && !Validation.validateWfmDropdown(placeOfSupplyBox)) {
            isFormValid = false;
        }
        return isFormValid;
    }

    public void setTreatment(SelectItem treatment, SelectItem placeOfSupply) {
        gccRegisteredCusSupp = false;
        if (placeOfSupply != null && PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(placeOfSupply.getCategory())) {
            if (GCC_REGISTERED.contains(placeOfSupply.getCode())) {
                placeOfSupply = PAYABLE.equals(type) ? null : placeOfSupply;
                gccRegisteredCusSupp = true;
            }
        }
        if (RECEIVABLE.equals(type)) {
            gccRegisteredCusSupp = true;
        }
        taxTreatments.setSelected(treatment);
        onTreatmentChanged(treatment, placeOfSupply);
    }

    public void setTreatmentList(SelectItem[] treatmentList) {
        taxTreatments.clear();
        taxTreatments.setItems(treatmentList);
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private void onInitialize() {
        taxTreatments = new DataListBox();
        taxTreatments.ensureDebugId(widgetCode + "taxTreatment");
        taxTreatments.addValueChangeHandler(new ValueChangeHandler<SelectItem>() {
            @Override
            public void onValueChange(ValueChangeEvent<SelectItem> valueChangeEvent) {
                onTreatmentChanged(valueChangeEvent.getValue());
            }
        });

        placeOfSupplyBox = new WfmDropdown();
        placeOfSupplyBox.ensureDebugId(widgetCode + "placeOfSupplyBox");
        placeOfSupplyBox.addValueChangeHandler(ch -> {
            onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
        });

        reverseChargeBox = new KpiCheckBox();
        reverseChargeBox.ensureDebugId(widgetCode + "reverseCharge");
        reverseChargeBox.setText("This transaction is applicable for reverse charge");
        reverseChargeBox.addValueChangeHandler(valueChangeEvent -> {
            if (cmdTreatmentChanges != null) {
                cmdTreatmentChanges.execute();
            }
            if (cmdReverseChargeChanges != null) {
                cmdReverseChargeChanges.execute();
            }
        });

        taxTreatmentField = new FormGroup(wfmStrings.taxTreatment(), taxTreatments, true);
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

        if (Utils.isSaudiCompany()) {
            CommonService.App.get().getPlaceOfSupply(null, getCallback(null));
        }
        container.addAll(getDefaultFields().toArray(new GColumn[]{}));
    }

    private void onTreatmentChanged(SelectItem selectedTaxTreatmentItem) {
        onTreatmentChanged(selectedTaxTreatmentItem, null);
    }

    private void onTreatmentChanged(SelectItem taxTreatmentItem, SelectItem placeOfSupply) {
        onTreatmentChanged(taxTreatmentItem, placeOfSupply, true);
    }

    private void onTreatmentChanged(SelectItem taxTreatmentItem, SelectItem placeOfSupply, boolean firePlaceOfSupplyEvent) {
        container.clear();
        placeOfSupplyBox.setSelected(null);
        reverseChargeField.setVisible(true);
        List<GColumn> fields = getDefaultFields();

        if (taxTreatmentItem == null || taxTreatmentItem.getId() == null || taxTreatmentItem.getId() == 0) {
            container.addAll(fields.toArray(new GColumn[]{}));
            return;
        }

        /**
         * show/hide "place of supply" & "reverse charge" widgets
         */
        if (NON_GCC.equals(taxTreatmentItem.getCode())
                || NON_VAT.equals(taxTreatmentItem.getCode())
                || OUT_OF_SCOPE.equals(taxTreatmentItem.getCode())) {

            if (NON_GCC.equals(taxTreatmentItem.getCode()) && PAYABLE.equals(type)) {
                fields.add(new GColumn(GColumnEnum.COL_4, reverseChargeField));
            }
        } else if (GCC_VAT_REGISTERED.equals(taxTreatmentItem.getCode())
                || GCC_NON_VAT_REGISTERED.equals(taxTreatmentItem.getCode())) {
            fields.add(new GColumn(GColumnEnum.COL_3, placeOfSupplyField));

            if (PAYABLE.equals(type)) {
                fields.add(new GColumn(GColumnEnum.COL_4, reverseChargeField));
            }
        } else {
            fields.add(new GColumn(GColumnEnum.COL_3, placeOfSupplyField));
        }
        container.addAll(fields.toArray(new GColumn[]{}));

        if (Utils.isUAECompany()) {
            loadUAEPlaceOfSupply(taxTreatmentItem.getCode(), placeOfSupply);
        } else if (Utils.isSaudiCompany()) {

            if (VAT_REGISTERED.equals(taxTreatmentItem.getCode()) || NON_VAT_REGISTERED.equals(taxTreatmentItem.getCode())) {
                placeOfSupplyBox.setSelected(defaultPlaceOfSupply.getId());
                placeOfSupplyBox.setEnabled(false);
            } else {
                placeOfSupplyBox.setSelected(placeOfSupply != null ? placeOfSupply.getId() : defaultPlaceOfSupply.getId());
                placeOfSupplyBox.setEnabled(true);

                if (firePlaceOfSupplyEvent && placeOfSupply != null) {
                    onPlaceOfSupplyChanged(placeOfSupply);
                }
            }
        }
        if (cmdTreatmentChanges != null) {
            cmdTreatmentChanges.execute();
        }
    }
    private void onPlaceOfSupplyChanged(SelectItem placeOfSupply) {
        if (placeOfSupply == null) {
            return;
        }
        String taxTreatmentCode = taxTreatments.getSelectedId() != null && taxTreatments.getSelectedId() > 0 ? taxTreatments.getSelectedItem().getCode() : null;
        if (GCC_NON_VAT_REGISTERED.equals(taxTreatmentCode) || GCC_VAT_REGISTERED.equals(taxTreatmentCode)) {
            if (PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(placeOfSupply.getCategory())) {
                boolean gccRegistered = GCC_REGISTERED.contains(placeOfSupply.getCode());

                if (!gccRegisteredCusSupp && !gccRegistered) {
                    taxTreatments.setSelectedByCode(NON_GCC);
                    onTreatmentChanged(taxTreatments.getSelectedItem(), placeOfSupply, false);
                    return;
                } else {
                    if (Utils.isSaudiCompany() && SA.equals(placeOfSupply.getCode())) {
                        reverseChargeField.setVisible(true);
                    } else if (Utils.isUAECompany() && AE.equals(placeOfSupply.getCode())) {
                        reverseChargeField.setVisible(true);
                    } else {
                        reverseChargeField.setVisible(false);
                    }
                    if (!reverseChargeField.isVisible() && cmdTreatmentChanges != null) {
                        cmdTreatmentChanges.execute();
                    }
                }
            } else {
                reverseChargeField.setVisible(true);
            }
        }
    }

    private void loadUAEPlaceOfSupply(String treatmentCode, SelectItem placeOfSupply) {
        CommonService.App.get().getPlaceOfSupply(treatmentCode, getCallback(placeOfSupply));
    }

    private AsyncCallback getCallback(SelectItem placeOfSupply) {
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
                        onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
                    } else if (defaultPlaceOfSupply != null) {
                        placeOfSupplyBox.setSelected(defaultPlaceOfSupply.getId());
                        onPlaceOfSupplyChanged(placeOfSupplyBox.getSelected());
                    }
                }
            }
        };
    }

    private List<GColumn> getDefaultFields() {
        return new LinkedList<GColumn>() {{
            if (showTreatmentField) {
                add(new GColumn(GColumnEnum.COL_4, taxTreatmentField));
            }
        }};
    }
}
