package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.widgets.placeofsupply.PlaceOfSupplyWidget;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.google.gwt.user.client.ui.Panel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/26/12
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class InvoiceQuoteFormPresenter implements Constants {
    private ViewInterface viewInterface;
    protected TypeItem customerSupplierItem;

    public InvoiceQuoteFormPresenter(ViewInterface viewInterface) {
        viewInterface.getCrmAccountWidgets().presenter.bindUI();
        this.viewInterface = viewInterface;
    }

    public abstract void bindUI();


    protected List<SplitButtonItem> generatePdfTemplates(NewInvoice data) {
        Integer defaultTemplateId = null;
        List<SplitButtonItem> pdfButtonItems = new ArrayList<>();

        if (data != null
                && data.getPdfTemplateList() != null
                && data.getPdfTemplateList().getItems() != null
                && data.getPdfTemplateList().getItems().length > 0) {

            for (SelectItem pdfItem : data.getPdfTemplateList().getItems()) {

                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfButtonItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(viewInterface.getHTMLPanel(), pdfItem.getId())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;
        pdfButtonItems.add(new SplitButtonItem("PDF_VERSION", WfmStrings.App.get().pdfVersion(), () -> pdfVersion(viewInterface.getHTMLPanel(), data.getPdfTemplateID() != null ? data.getPdfTemplateID() : finalDefaultTemplateId), true));

        return pdfButtonItems;
    }

    public void alertStockItemsMessage(SelectItem[] items) {
        StringBuilder itemNames = new StringBuilder();
        StringBuilder bookingReservation = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i].getName()).append("\"");
            if (items[i].getDescription() != null && items[i].getDescription().length() > 0) {
                if (i != 0) {
                    bookingReservation.append(", ");
                }
                bookingReservation.append("\"(").append(items[i].getDescription()).append(")\"");
            }
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setWidth(400);
        if (bookingReservation.length() > 0) {
            messageBox.setTitle(WfmStrings.App.get().warning());
            messageBox.setMessage(AccountingMessages.App.get().bookingReservation(itemNames.toString(), bookingReservation.toString()));
        } else {
            messageBox.setTitle(AccountingStrings.App.get().notEnoughQuantity());
            messageBox.setMessage(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(itemNames.toString()));
        }
        messageBox.open();
    }

    protected abstract void pdfVersion(final Panel hp, Integer templateId);

    protected boolean validateApplicableTaxTypeForSale() {
        if (!(GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered())) {
            return true;
        }
        SelectItem taxTreatment = viewInterface.getPlaceOfSupplyWidget() != null ? viewInterface.getPlaceOfSupplyWidget().getTaxTreatment() : null;
        if (taxTreatment == null) {
            return true;
        }

        boolean onlyZeroRateApplicable = false;
        if (NON_GCC.equals(taxTreatment.getCode())) {
            onlyZeroRateApplicable = true;
        }
        if (Arrays.asList(GCC_VAT_REGISTERED, GCC_NON_VAT_REGISTERED).contains(taxTreatment.getCode())) {
            SelectItem companyPlaceOfSupply = viewInterface.getPlaceOfSupplyWidget().getCompanyPlaceOfSupply();

            if (companyPlaceOfSupply == null || !GCC_REGISTERED.contains(companyPlaceOfSupply.getCode())) {
                onlyZeroRateApplicable = true;
            } else {
                SelectItem tranPlaceOfSupply = viewInterface.getPlaceOfSupplyWidget().getSelectedPlaceOfSupply();
                if (tranPlaceOfSupply == null ||
                        !(PLACEOFSUPPLY_CATEGORY.REGION.equals(tranPlaceOfSupply.getCategory()) ||
                                (viewInterface.getPlaceOfSupplyWidget().getDefaultPlaceOfSupply() != null
                                        && viewInterface.getPlaceOfSupplyWidget().getDefaultPlaceOfSupply().getCode().equals(tranPlaceOfSupply.getCode())))) {
                    onlyZeroRateApplicable = true;
                }
            }
        }
        if (onlyZeroRateApplicable) {
            if (viewInterface.getProductTable().getTotalTaxItems() != null) {
                boolean isApplicableTaxRate = true;
                TotalTaxItem[] totalTaxItems = viewInterface.getProductTable().getTotalTaxItems();

                for (TotalTaxItem ttItem : totalTaxItems) {
                    if (!TaxKeyEnum.ZERO_RATE.equals(ttItem.getTaxItem().getTaxKey())) {
                        isApplicableTaxRate = false;
                        break;
                    }
                }

                return isApplicableTaxRate;
            }
        }
        return true;
    }

    protected boolean validateApplicableTaxTypeForPurchase() {
        if (!(GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered())) {
            return true;
        }
        boolean validateChosenTaxes = false;

        if (viewInterface.getProductTable().isReverseChargeApplicable()) {
            validateChosenTaxes = !viewInterface.getProductTable().getReverseChargeBox().getValue();
        }
        SelectItem taxTreatment = viewInterface.getPlaceOfSupplyWidget() != null ? viewInterface.getPlaceOfSupplyWidget().getTaxTreatment() : null;

        if (taxTreatment != null
                && Arrays.asList(GCC_VAT_REGISTERED, GCC_NON_VAT_REGISTERED).contains(taxTreatment.getCode())
                && !viewInterface.getProductTable().getReverseChargeBox().isAttached()) {
            validateChosenTaxes = true;
        }

        if (validateChosenTaxes)
            if (viewInterface.getProductTable().getTotalTaxItems() != null) {
                boolean isApplicableTaxRate = true;
                TotalTaxItem[] totalTaxItems = viewInterface.getProductTable().getTotalTaxItems();
                for (TotalTaxItem ttItem : totalTaxItems) {
                    if (!(TaxKeyEnum.EXEMPT.equals(ttItem.getTaxItem().getTaxKey()) || TaxKeyEnum.OUT_OF_SCOPE.equals(ttItem.getTaxItem().getTaxKey()))) {
                        isApplicableTaxRate = false;
                        break;
                    }
                }
                return isApplicableTaxRate;
            }
        return true;
    }

    protected boolean validateApplicableTypeForUK() {
        if (Utils.isUKVATRegistered() && !Utils.isVATCashBased()) {
            SelectItem taxTreatment = customerSupplierItem.getTaxTreatment();
            if (taxTreatment != null && OVERSEAS.equals(taxTreatment.getCode())) {
                TotalTaxItem[] totalTaxItems = viewInterface.getProductTable().getTotalTaxItems();
                for (TotalTaxItem totalTaxItem : totalTaxItems) {
                    if (!TaxKeyEnum.ZERO_RATE.equals(totalTaxItem.getTaxItem().getTaxKey())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void configurePlaceOfSupply(TypeItem typeItem, SelectItem placeOfSupply, PlaceOfSupplyWidget placeOfSupplyWidget) {
        SelectItem treatment = typeItem.getTaxTreatment();

        if (placeOfSupplyWidget != null) {
            placeOfSupplyWidget.setData(treatment, typeItem.getPlaceOfSupply(), placeOfSupply);
        }
    }

    protected void onChangePriceLevel(NewInvoice data) {

        if (viewInterface.getPriceLevelDropdown().getSelectedId() != null) {
            PriceLevelItem priceLevelItem = (PriceLevelItem) viewInterface.getPriceLevelDropdown().getSelectedItem();
            viewInterface.getProductTable().setPriceLevel(priceLevelItem);
            viewInterface.getProductTable().onClientPriceLevelChange(data);
        } else {
            viewInterface.getProductTable().setPriceLevel(null);
            viewInterface.getProductTable().onClientPriceLevelChange(data);
        }
    }
}
