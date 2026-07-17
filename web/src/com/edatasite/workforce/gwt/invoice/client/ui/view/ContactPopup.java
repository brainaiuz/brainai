package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.SmartContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class ContactPopup extends KpiModal {

    private SmartContactLookUp contactLookUp;
    private DataListBox supplierBox;
    private WfmButton2 sendEmailButton;

    private Integer supplierId;
    private Integer rfqId;

    public ContactPopup(Integer supplierId, Integer rfqId) {
        this.supplierId = supplierId;
        this.rfqId = rfqId;
        setWidth("400px");
        initialize();
        if (supplierId == null) {
            loadSupplierList();
        }
    }

    private void initialize() {
        contactLookUp = new SmartContactLookUp(() -> {
            new CrmQuickAdd(LayoutRPC.CONTACT_FORM, contactLookUp.getSupplierId() != null ? contactLookUp.getSupplierId() : supplierId, true);
            close();
        }, supplierId);
        contactLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> {
            contactLookUp.islink();
        });
        supplierBox = new DataListBox();
        supplierBox.addValueChangeHandler(valueChangeEvent -> {
            contactLookUp.setSupplierId(supplierBox.getSelectedId());
            contactLookUp.clear();
        });

        sendEmailButton = new WfmButton2(wfmStrings.sendEmail(), Constants.BTN_PRIMARY);
        sendEmailButton.addClickHandler(clickEvent -> sendEmail());

        setTitle("Choose contact to send email");
        if (supplierId == null) {
            add(new FormGroup(wfmStrings.supplier(), supplierBox, true));
        }
        add(new FormGroup(wfmStrings.contact(), contactLookUp, true));
        addButton(sendEmailButton);
        setCloseButton(true);
    }

    private void loadSupplierList() {
        QuoteService.App.get().getRfqItemSuppliersAsSelectItem(rfqId, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                supplierBox.setItems(selectItems);
            }
        });
    }

    private void sendEmail() {
        if (!validate()) {
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + Constants.REQUEST_FOR_QUOTE_CATEGORY + "/" + supplierId + "/" + contactLookUp.getSelectedItemID() + "/" + rfqId);
        }
    }

    public boolean validate() {
        boolean error = false;
        if (!Validation.validateLookUpRequired(contactLookUp)) {
            error = true;
        }
        if (supplierId == null && !Validation.validateListBoxRequired(supplierBox)) {
            error = true;
        }

        return error;
    }

}

