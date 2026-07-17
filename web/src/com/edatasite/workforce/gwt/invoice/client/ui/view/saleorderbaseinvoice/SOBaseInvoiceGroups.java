package com.edatasite.workforce.gwt.invoice.client.ui.view.saleorderbaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.KpiSelect2;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.ArrayList;
import java.util.HashMap;

public class SOBaseInvoiceGroups extends Composite implements Constants {
    public static final String DETAILED_INVOICE = "DETAILED_INVOICE";
    public static final String GROUPED_BY_OBJECT = "GROUPED_BY_OBJECT";
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SOBaseInvoiceGroupsUiBinder ourUiBinder = GWT.create(SOBaseInvoiceGroupsUiBinder.class);
    @UiField
    HTMLPanel pnlFieldsContainer;
    @UiField
    FormGroup fgNameFields;
    @UiField
    FormGroup fgDescFields;

    HashMap<String, ArrayList<SelectItem>> nameFieldsMap = new HashMap<>();
    HashMap<String, ArrayList<SelectItem>> descFieldsMap = new HashMap<>();

    private final KpiSelect2 nameFieldList;
    private final KpiSelect2 descFieldList;

    private String invoiceType = DETAILED_INVOICE;
    private String objectType; //SALE_ORDER, SALE_QUOTE, GDN

    public SOBaseInvoiceGroups() {
        initWidget(ourUiBinder.createAndBindUi(this));
        pnlFieldsContainer.setVisible(false);

        nameFieldList = new KpiSelect2(true);
        descFieldList = new KpiSelect2(true);

        fgNameFields.setLabel(accountingStrings.fieldsToBeIncluded());
        fgNameFields.addToContent(nameFieldList);

        fgDescFields.setLabel(accountingStrings.informationToBeIncluded());
        fgDescFields.addToContent(descFieldList);
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String type) {
        this.objectType = type;

        if (GROUPED_BY_OBJECT.equals(invoiceType)) {
            generateFields();
        }
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public HashMap<String, Boolean> getSelectedNameFields() {
        HashMap<String, Boolean> selectedNameFields = new HashMap<>();
        for (SelectItem item : nameFieldList.getSelectedItems()) {
            selectedNameFields.put(item.getDescription(), item.isNewItem());
        }
        return selectedNameFields;
    }

    public HashMap<String, Boolean> getSelectedDescFields() {
        HashMap<String, Boolean> selectedDescFields = new HashMap<>();
        for (SelectItem item : descFieldList.getSelectedItems()) {
            selectedDescFields.put(item.getDescription(), item.isNewItem());
        }
        return selectedDescFields;
    }


    public void onClickInvoiceType(String type) {
        invoiceType = type;

        switch (type) {
            case DETAILED_INVOICE:
                pnlFieldsContainer.setVisible(false);
                break;
            case GROUPED_BY_OBJECT:
                pnlFieldsContainer.setVisible(true);
                generateFields();
                break;
        }
    }

    private void generateFields() {

        if (nameFieldsMap.get(objectType) != null) {
            renderFields(nameFieldsMap.get(objectType), nameFieldList);
        } else {
            generateFields(objectType);
        }

        if (descFieldsMap.get(objectType) != null) {
            renderFields(descFieldsMap.get(objectType), descFieldList);
        } else {
            generateFields(objectType);
        }
    }

    private void generateFields(String objectType) {
        ArrayList<SelectItem> nameFields = new ArrayList<>();
        ArrayList<SelectItem> descFields = new ArrayList();

        if (SaleOrderBaseInvoiceItem.SALE_ORDER.equals(objectType) || SaleOrderBaseInvoiceItem.SALE_QUOTE.equals(objectType)) {
            String dateFieldtitle = "";
            {
                String numberFieldTitle = "";
                if (SaleOrderBaseInvoiceItem.SALE_ORDER.equals(objectType)) {
                    numberFieldTitle = Property.get(SALE_ORDER_CODE, wfmStrings.saleorder()) + " " + wfmStrings.number();
                    dateFieldtitle = Property.get(SALE_ORDER_CODE, wfmStrings.saleorder()) + " " + wfmStrings.date();
                } else {
                    numberFieldTitle = Property.get(SALE_QUOTE, wfmStrings.salesQuote()) + " " + wfmStrings.number();
                    dateFieldtitle = Property.get(SALE_QUOTE, wfmStrings.salesQuote()) + " " + wfmStrings.date();
                }
                nameFields.add(new SelectItem(nameFields.size(), numberFieldTitle, FIELDS.NUMBER, true));
            }

            //reference field
            nameFields.add(new SelectItem(nameFields.size(), wfmStrings.reference(), FIELDS.REFERENCE));

            descFields.add(new SelectItem(descFields.size(), wfmStrings.reference(), FIELDS.REFERENCE));
            descFields.add(new SelectItem(descFields.size(), dateFieldtitle, FIELDS.DATE));
        } else {
            nameFields.add(new SelectItem(nameFields.size(), accountingStrings.gdnNumber(), FIELDS.NUMBER, true));

            //shipping label field
            nameFields.add(new SelectItem(nameFields.size(), accountingStrings.shippingLabel(), FIELDS.SHIPPING_LABEL));
            descFields.add(new SelectItem(descFields.size(), accountingStrings.shippingLabel(), FIELDS.SHIPPING_LABEL));

            descFields.add(new SelectItem(descFields.size(), Property.get(SALE_ORDER_CODE, wfmStrings.saleorder()) + " " + wfmStrings.number(), FIELDS.SO_NUMBER));
            //ship date field
            descFields.add(new SelectItem(descFields.size(), wfmStrings.shipDate(), FIELDS.SHIP_DATE));
        }

        descFields.add(new SelectItem(descFields.size(), wfmStrings.poNumber(), FIELDS.PO_NUMBER));

        if (!Utils.isProjectInLineItemEnable()) {
            descFields.add(new SelectItem(descFields.size(), wfmStrings.project(), FIELDS.PROJECT));
        }

        //custom fields
        CommonService.App.get().getCompanyCustomFields(SaleOrderBaseInvoiceItem.SALE_ORDER.equals(objectType) || SaleOrderBaseInvoiceItem.GDN.equals(objectType) ? ViewName.SaleOrder : ViewName.SaleQuote, new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<CompanyCustomFieldItem> customFields) {
                if (customFields != null && !customFields.isEmpty()) {
                    customFields.stream().filter(cfield -> DATA_TYPE_TEXT.equals(cfield.getDataType())
                            || DATA_TYPE_NUMBER.equals(cfield.getDataType())
                            || DATA_TYPE_DATE.equals(cfield.getDataType()))
                            .forEach(cfield -> {
                                SelectItem cNameField = new SelectItem(nameFields.size(), cfield.getFieldName(), cfield.getColumnCode());
                                cNameField.setNewItem(true);
                                nameFields.add(cNameField);

                                SelectItem cDescField = new SelectItem(descFields.size(), cfield.getFieldName(), cfield.getColumnCode());
                                cDescField.setNewItem(true);
                                descFields.add(cDescField);
                            });
                }

                nameFieldsMap.put(objectType, nameFields);
                descFieldsMap.put(objectType, descFields);

                renderFields(nameFields, nameFieldList);
                renderFields(descFields, descFieldList);
            }
        });
    }

    private void renderFields(ArrayList<SelectItem> fields, KpiSelect2 container) {
        container.clear();
        container.setItems(fields);
    }

    interface SOBaseInvoiceGroupsUiBinder extends UiBinder<HTMLPanel, SOBaseInvoiceGroups> {
    }

    public interface FIELDS {
        String NUMBER = "NUMBER";
        String REFERENCE = "REFERENCE";
        String DATE = "DATE";
        String SHIP_DATE = "SHIP_DATE";
        String SHIPPING_LABEL = "SHIPPING_LABEL";
        String PO_NUMBER = "PO_NUMBER";
        String SO_NUMBER = "SO_NUMBER";
        String PROJECT = "PROJECT";
    }
}