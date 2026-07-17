package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.placeofsupply.PlaceOfSupplyItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;

public class GccTreatmentSettingWidget extends AbstractTreatmentSettingsWidget {
    private static final String widgetCode = "UAETreatmentWidget";
    private DataListBox taxTreatments;
    private TextBox trnBox;
    private WfmDropdown placeOfSupplyBox;

    private FormGroup taxTreatmentField;
    private FormGroup trnField;
    private FormGroup placeOfSupplyField;

    private SelectItem defaultPlaceOfSupply;

    @Override
    public CrmAccountItem getData(CrmAccountItem item) {
        item.setTaxTreatment(taxTreatments.getSelectedItem());
        item.setTaxTreatmentId(taxTreatments.getSelectedId());
        item.setTrn(trnBox.getText());

        if (placeOfSupplyBox.getSelectedId() != null) {
            SelectItem placeOfSupply = placeOfSupplyBox.getSelectedItem();

            if (PLACEOFSUPPLY_CATEGORY.COUNTRY.equals(placeOfSupply.getCategory())) {
                item.setPlaceOfSupplyCountry(placeOfSupply);
                item.setPlaceOfSupplyCountryId(placeOfSupply.getId());
            } else {
                item.setPlaceOfSupplyState(placeOfSupply);
                item.setPlaceOfSupplyStateId(placeOfSupply.getId());
            }
        }
        return item;
    }

    @Override
    public boolean validate() {
        boolean isFormValid = true;

        if (!Validation.validateListBoxRequired(taxTreatments)) {
            isFormValid = false;
        }
        if (trnBox.isAttached()) {

            if (!Validation.validateTextBoxRequired(trnBox)) {
                isFormValid = false;
            } else if (trnBox.getText().length() != 15) {
                Info.show(wfmStrings.trnNumberErrorMessage(), Info.Type.WARNING);
                isFormValid = false;
            }
        }
        if (placeOfSupplyBox.isAttached() && !Validation.validateWfmDropdown(placeOfSupplyBox) && !Utils.isOmanCompany()) {
            isFormValid = false;
        }
        return isFormValid;
    }

    @Override
    public void setTreatment(SelectItem treatment) {
        taxTreatments.setSelected(treatment);
        onTreatmentChanged(taxTreatments.getSelectedItem(), null);
    }

    @Override
    protected void onInitialize() {
        taxTreatments = new DataListBox();
        taxTreatments.ensureDebugId(widgetCode + "taxTreatment");
        taxTreatments.addValueChangeHandler(valueChangeEvent -> onTreatmentChanged(valueChangeEvent.getValue()));
        trnBox = new TextBox();
        Validation.addNumericKeyboardListener(trnBox);
        placeOfSupplyBox = new WfmDropdown();
        placeOfSupplyBox.ensureDebugId(widgetCode + "placeOfSupplyBox");
        taxTreatmentField = new FormGroup(wfmStrings.taxTreatment(), taxTreatments, true);

        if (Utils.isSaudiCompany() || Utils.isOmanCompany()) {
            trnField = new FormGroup(wfmStrings.trn(), trnBox);
        } else if (Utils.isUAECompany()) {
            MaterialLink validateTrn = new MaterialLink();
            validateTrn.setText(wfmStrings.validateTrn());
            validateTrn.setTarget("_blank");
            validateTrn.setHref("https://eservices.tax.gov.ae/en-us/trn-verify");
            validateTrn.setClass("ml-1");
            trnField = new FormGroup(wfmStrings.trn(), new InputGroup(trnBox, validateTrn), true);
        } else {
            trnField = new FormGroup();
        }
        placeOfSupplyField = new FormGroup(wfmStrings.sourceOfSupply(), placeOfSupplyBox, !Utils.isOmanCompany());

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
            loadPlaceOfSupply(null, null);
        }
        container.add(taxTreatmentField);
    }

    @Override
    public void setData(CrmAccountItem crmAccountItem) {
        taxTreatments.clear();
        taxTreatments.setItems(crmAccountItem.getTaxTreatments());

        if (crmAccountItem.getTaxTreatmentId() != null) {
            SelectItem placeOfSupply = null;

            if (crmAccountItem.getPlaceOfSupplyStateId() != null || crmAccountItem.getPlaceOfSupplyCountryId() != null) {
                placeOfSupply = new SelectItem(crmAccountItem.getPlaceOfSupplyStateId() != null ? crmAccountItem.getPlaceOfSupplyStateId() : crmAccountItem.getPlaceOfSupplyCountryId());
            }
            taxTreatments.setSelected(crmAccountItem.getTaxTreatment());
            onTreatmentChanged(taxTreatments.getSelectedItem(), placeOfSupply);
        }
        trnBox.setText(crmAccountItem.getTrn());
    }

    private void onTreatmentChanged(SelectItem selectedTaxTreatmentItem) {
        onTreatmentChanged(selectedTaxTreatmentItem, null);
    }

    private void onTreatmentChanged(SelectItem taxTreatmentItem, SelectItem placeOfSupply) {
        container.clear();
        placeOfSupplyBox.setSelected(null);
        container.add(taxTreatmentField);

        if (taxTreatmentItem == null || taxTreatmentItem.getId() == null || taxTreatmentItem.getId() == 0) {
            return;
        }
        if (VAT_REGISTERED.equals(taxTreatmentItem.getCode()) || GCC_VAT_REGISTERED.equals(taxTreatmentItem.getCode())) {
            container.add(trnField);
        }
        if (!NON_GCC.equals(taxTreatmentItem.getCode())) {
            container.add(placeOfSupplyField);
        }

        if (Utils.isUAECompany()) {
            loadPlaceOfSupply(taxTreatmentItem.getCode(), placeOfSupply);
        } else if (Utils.isSaudiCompany()) {

            if (VAT_REGISTERED.equals(taxTreatmentItem.getCode()) || NON_VAT_REGISTERED.equals(taxTreatmentItem.getCode())) {
                placeOfSupplyBox.setSelected(defaultPlaceOfSupply != null ? defaultPlaceOfSupply.getId() : null);
                placeOfSupplyBox.setEnabled(false);
            } else {
                placeOfSupplyBox.setSelected(placeOfSupply != null ? placeOfSupply.getId() : null);
                placeOfSupplyBox.setEnabled(true);
            }
        }
    }

    private void loadPlaceOfSupply(String treatmentCode, SelectItem placeOfSupply) {
        CommonService.App.get().getPlaceOfSupply(treatmentCode, new AsyncCallback<PlaceOfSupplyItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(PlaceOfSupplyItem placeOfSupplyItem) {
                placeOfSupplyBox.clear();

                if (placeOfSupplyItem != null) {

                    if (placeOfSupplyItem.getCountries() != null && placeOfSupplyItem.getCountries().length > 0) {
                        placeOfSupplyBox.addItems("GCC Member States", placeOfSupplyItem.getCountries());
                    } else if (placeOfSupplyItem.getStates() != null && placeOfSupplyItem.getStates().length > 0) {
                        placeOfSupplyBox.addItems("UAE Emirates", placeOfSupplyItem.getStates());
                    }
                    if (placeOfSupply != null) {
                        placeOfSupplyBox.setSelected(placeOfSupply.getId());
                    } else if (defaultPlaceOfSupply != null) {
                        placeOfSupplyBox.setSelected(defaultPlaceOfSupply.getId());
                    }
                }
            }
        });
    }
    public DataListBox getTaxTreatments() {
        return taxTreatments;
    }
}
