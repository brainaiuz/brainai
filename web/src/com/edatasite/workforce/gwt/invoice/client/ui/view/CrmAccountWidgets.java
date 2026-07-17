package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.ui.view.components.CusSupAddress;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jun 2, 2009
 * Time: 11:12:34 PM
 */
public class CrmAccountWidgets extends Composite implements Constants, AccountingCustomFormConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public LookUp crmAccountLookUp;
    public LookUp dropShipToCustomerLookUp;

    private GBoxItem billAddreeField, mailAddreeField;

    private CusSupAddress billAddress;
    private CusSupAddress mailAddress;

    private String type;
    private String formType;
    private String actionAdd;
    private String actionEdit;
    private String crmAccountWidget = "crm_account_widget_";

    private boolean isEditForm;
    public CrmAccountWidgetsPresenter presenter;
    private CurrencyWidget currencyWidget;

    public CrmAccountWidgets(LookUp crmAccountLookUp, String type, String formType, boolean isEditForm) {
        this.crmAccountLookUp = crmAccountLookUp;
        this.type = type;
        this.formType = formType;
        this.isEditForm = isEditForm;
        if (Constants.RECEIVABLE.equals(type)) {
            this.actionEdit = "clientedit|editclient/";
            this.actionAdd = "client|add/add";
        } else {
            this.actionEdit = "supplier|editsupplier/";
            this.actionAdd = "supplier|add/add";
        }
        initialize();
    }

    private void initialize() {

        billAddress = new CusSupAddress();
        mailAddress = new CusSupAddress();

        billAddress.getAddAddressLink().ensureDebugId(crmAccountWidget + "addBillingAddrLink");
        billAddress.getEditAddressLink().ensureDebugId(crmAccountWidget + "editBillingAddrLink");

        mailAddress.getAddAddressLink().ensureDebugId(crmAccountWidget + "addMailingAddrLink");
        mailAddress.getEditAddressLink().ensureDebugId(crmAccountWidget + "editMailingAddrLink");

        billAddress.getAddressList().ensureDebugId(crmAccountWidget + "billAddrListBox");
        mailAddress.getAddressList().ensureDebugId(crmAccountWidget + "mailAddrListBox");

        billAddress.getAddAddressLink().setVisible(false);
        billAddress.getEditAddressLink().setVisible(false);

        mailAddress.getAddAddressLink().setVisible(false);
        mailAddress.getEditAddressLink().setVisible(false);

//        billAddress.getAddAddressLink().addStyleName("disabled");
//        billAddress.getEditAddressLink().addStyleName("disabled");

//        mailAddress.getAddAddressLink().addStyleName("disabled");
//        mailAddress.getEditAddressLink().addStyleName("disabled");

        mailAddress.setVisible(RECEIVABLE.equals(type));

        this.presenter = new CrmAccountWidgetsPresenter(new CrmAccountWidgetsPresenter.CrmAccountWidgetsInterface() {
            @Override
            public boolean isEditForm() {
                return isEditForm;
            }

            @Override
            public LookUp getCrmAccLookUp() {
                return crmAccountLookUp;
            }

            @Override
            public LookUp getDropShipToCustomerLookUp() {
                return dropShipToCustomerLookUp;
            }

            @Override
            public CusSupAddress billAddress() {
                return billAddress;
            }

            @Override
            public CusSupAddress mailAddress() {
                return mailAddress;
            }

            @Override
            public String getType() {
                return type;
            }

            @Override
            public String getFormType() {
                return formType;
            }

            @Override
            public String getAddActionLink() {
                return actionAdd + crmAccountLookUp.getSelectedItemID() + "/" + INVOICE_QUOTE_FORM;
            }

            @Override
            public String getEditActionLink() {
                return actionEdit + crmAccountLookUp.getSelectedItemID() + "/" + INVOICE_QUOTE_FORM;
            }

            @Override
            public CurrencyWidget getCurrencyWidget() {
                return currencyWidget;
            }
        });
    }

    public void setCurrencyWidget(CurrencyWidget currencyWidget) {
        this.currencyWidget = currencyWidget;
    }

    public boolean validateAddress() {
        int errors = 0;
        if (!billAddress.getAddressList().isSomethingSelected()) {
            billAddress.getAddressList().setStyleName("x-form-invalid");
            billAddress.getAddressList().addValueChangeHandler(changeEvent -> removeAddressErrorStyle(billAddress.getAddressList()));
            Utils.scrollIntoView(billAddress.getElement());
            errors++;
        }
        if (PAYABLE.equals(type) && dropShipToCustomerLookUp != null && dropShipToCustomerLookUp.getSelectedItemID() != null && !mailAddress.getAddressList().isSomethingSelected()) {
            mailAddress.getAddressList().setStyleName("x-form-invalid");
            mailAddress.getAddressList().addValueChangeHandler(changeEvent -> removeAddressErrorStyle(mailAddress.getAddressList()));
            Utils.scrollIntoView(mailAddress.getElement());
            errors++;
        }
        return errors <= 0;
    }

    private void removeAddressErrorStyle(DataListBox address) {
        if (!"".equals(address.getStyleName())) {
            address.removeStyleName(address.getStyleName());
        }
    }

    public CusSupAddress getBillAddress() {
        return billAddress;
    }

    public CusSupAddress getMailAddress() {
        return mailAddress;
    }

    public Integer getBillAddressID() {
        return billAddress.getAddressList().getSelectedId();
    }

    public Integer getMailAddressID() {
        return mailAddress.getAddressList().getSelectedId();
    }

    public void initWidgetMap(HashMap<String, Widget> widgetsMap) {

        billAddreeField = new GBoxItem(accountingStrings.billing(), billAddress);
        billAddreeField.ensureDebugId("inv_bill_to");
        billAddreeField.setStyleNoBorder(true);
        widgetsMap.put(INPUT_BILL_ADDRESS, billAddreeField);

        mailAddreeField = new GBoxItem(accountingStrings.mailing(), mailAddress);
        mailAddreeField.ensureDebugId("inv_ship_to");
        mailAddreeField.setStyleNoBorder(true);
        widgetsMap.put(INPUT_MAIL_ADDRESS, mailAddreeField);
    }

    public void setDropShipToCustomerLookUp(LookUp dropShipToCustomerLookUp) {
        this.dropShipToCustomerLookUp = dropShipToCustomerLookUp;
    }

    public LookUp getDropShipToCustomerLookUp() {
        return dropShipToCustomerLookUp;
    }

    public void setVisible(boolean visible) {

        if (billAddreeField != null) {
            billAddreeField.setVisible(visible);
        }
        if (mailAddreeField != null) {
            mailAddreeField.setVisible(visible);
        }
    }
}
