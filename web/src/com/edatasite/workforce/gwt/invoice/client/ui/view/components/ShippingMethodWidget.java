package com.edatasite.workforce.gwt.invoice.client.ui.view.components;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShippingMethodWidget extends Composite {

    private CrmAccountLookUp crmAccountLookUp;
    private Command onChangeCommand;

    private DataListBox shippingListBox;
    private TextBox shippingAmountBox;

    //map for changed shipping
    private HashMap<Integer, ShippingMethod> map;
    private ShippingMethod selectedMethod;

    public ShippingMethodWidget(CrmAccountLookUp crmAccountLookUp) {
        map = new HashMap<>();
        this.crmAccountLookUp = crmAccountLookUp;

        shippingListBox = new DataListBox();
        shippingListBox.setWithoutNullLabel(true);

        shippingAmountBox = new TextBox();
        shippingAmountBox.setEnabled(false);
        Validation.addNumericKeyboardListener(shippingAmountBox, AccountingUtils.calculationScale);
        initWidget(shippingListBox);

        loadShippingMethods(crmAccountLookUp.getSelectedItemID());
        initHandlers();
    }

    private void initHandlers() {

        if (crmAccountLookUp != null) {
            crmAccountLookUp.getSuggestBox().addSelectionHandler(sh -> {
                shippingListBox.clear();
                loadShippingMethods(crmAccountLookUp.getSelectedItemID());
            });
        }

        shippingAmountBox.addChangeHandler(ku -> {

            if (shippingListBox.getSelectedId() != null && shippingListBox.getSelectedId().intValue() > 0) {
                ShippingMethod shm = map.get(shippingListBox.getSelectedId());

                if (shm == null) {
                    shm = (ShippingMethod) shippingListBox.getSelectedItem();
                }
                shm.setPriceChanged(true);
                shm.setPrice(AccountingUtils.get().parseToBigDecimal(shippingAmountBox.getText()));
                map.put(shm.getId(), shm);
            }

            if (onChangeCommand != null) {
                onChangeCommand.execute();
            }
        });

        shippingListBox.addValueChangeHandler(vh -> {

            if (shippingListBox.getSelectedId() == null || shippingListBox.getSelectedId().intValue() == 0) {
                shippingAmountBox.setText(null);
                shippingAmountBox.setEnabled(false);
            } else {
                shippingAmountBox.setEnabled(true);

                if (getSelectedMethod() != null)
                    shippingAmountBox.setText(AccountingUtils.get().formatPrice(getSelectedMethod().getPrice()));
            }

            if (onChangeCommand != null) {
                onChangeCommand.execute();
            }
        });
    }

    private void loadShippingMethods(Integer customerId) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(customerId);

        InvoiceService.App.get().getShippinhMethodsForLookUp(fp, new AsyncCallback<ShippingMethod[]>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ShippingMethod[] result) {
                List<ShippingMethod> list = new ArrayList<>();

                ShippingMethod df = new ShippingMethod();
                df.setName(AccountingStrings.App.get().shippingMethod());
                df.setId(0);
                list.add(df);

                if (result != null && result.length > 0) {
                    list.addAll(Arrays.asList(result));
                }
                shippingListBox.setItems(list.toArray(new ShippingMethod[]{}));
            }
        });
    }

    public void setOnChangeCommand(Command command) {
        onChangeCommand = command;
    }

    public Integer getSelectedID() {
        return shippingListBox.getSelectedId();
    }

    public ShippingMethod getSelectedMethod() {

        if (shippingListBox.getSelectedId() != null) {

            if (map.get(shippingListBox.getSelectedId()) != null) {
                return map.get(shippingListBox.getSelectedId());
            } else {
                return (ShippingMethod) shippingListBox.getSelectedItem();
            }
        }
        return null;
    }

    public void setSelectedMethod(ShippingMethod shippingMethod) {
        this.selectedMethod = shippingMethod;

        if (selectedMethod != null) {
            shippingListBox.setSelected(shippingMethod);
            selectedMethod.setPriceChanged(true);

            shippingAmountBox.setText(AccountingUtils.get().formatPrice(selectedMethod.getPrice()));
            shippingAmountBox.setEnabled(true);
        }
    }

    public void setAppliedCrmAccountId(Integer crmAccountId) {
        loadShippingMethods(crmAccountId);
    }

    public TextBox getShippingAmountBox() {
        return shippingAmountBox;
    }
}