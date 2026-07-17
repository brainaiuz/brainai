package com.edatasite.workforce.gwt.profile.client;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.ChartOfAccountListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CurrencyListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.DiscountListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.PaymentMethodListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.TaxListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.pricelevel.PriceLevelListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.BrandsListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.ProductCategoriesListView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.UnitMeasurementsListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceTermsListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.ShippingMethodsListView;
import com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings.ItemTableSettingsDraggableView;
import com.edatasite.workforce.gwt.profile.client.ui.view.AccountTypeNumberSettingView;
import com.edatasite.workforce.gwt.profile.client.ui.view.ConversionBalanceForm;
import com.edatasite.workforce.gwt.profile.client.ui.view.FinancialSettingsForm;
import com.edatasite.workforce.gwt.profile.client.ui.view.InvoiceSettingsForm;
import com.edatasite.workforce.gwt.profile.client.ui.view.ProductNumberingSettingsForm;
import com.edatasite.workforce.gwt.profile.client.ui.view.locking.TransactionLockingView;

import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 17.03.2010
 * Time: 15:53:57
 */
public class InvoiceSettingsSinksContainer extends SinksContainer implements PermissionConstants {
    public InvoiceSettingsSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    public InvoiceSettingsSinksContainer(String name, String description) {
        super(name, description, null, NONE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(ACCOUNTING_INVOICE_SETTINGS)) {
            addView(new InvoiceSettingsForm());
        }
        if (Utils.hasPermission(ACCOUNTING_FINANCIAL_SETTINGS)) {
            addView(new FinancialSettingsForm());
            if (Utils.hasPermission(ACCOUNTING_TRANSACTION_LOOKING_LIST)) {
                addView(new TransactionLockingView());
            }
        }
        if (Utils.isAccountingSetup()) {

            if (Utils.hasPermission(ACCOUNTING_ACCOUNT_NUMBERING)) {
                addView(new AccountTypeNumberSettingView());
            }
        }
        if (Utils.hasPermission(ACCOUNTING_NUMBERING_SETTINGS)) {
            addView(new ProductNumberingSettingsForm());
        }
        if (Utils.hasPermission(ACCOUNTING_CONVERSION_BALANCE)) {
            addView(new ConversionBalanceForm());
        }
        if (Utils.hasPermission(ACCOUNTING_TAX_RATES_LIST)) {
            addView(new TaxListView());
        }
        if (Utils.hasPermission(ACCOUNTING_CURRENCY_RATES_LIST)) {
            addView(new CurrencyListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PRICE_LEVELS_LIST)) {
            addView(new PriceLevelListView());
        }
        if (Utils.hasPermission(ACCOUNTING_TERMS_LIST)) {
            addView(new InvoiceTermsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_DISCOUNTS_LIST)) {
            addView(new DiscountListView());
        }
        if (Utils.hasPermission(ACCOUNTING_UNIT_MEASUREMENTS_LIST)) {
            addView(new UnitMeasurementsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PRODUCT_CATEGORIES_LIST)) {
            addView(new ProductCategoriesListView());
        }
        if (Utils.hasPermission(ACCOUNTING_BRANDS_LIST)) {
            addView(new BrandsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_SHIPPING_METHODS_LIST)) {
            addView(new ShippingMethodsListView());
        }
        if (Utils.hasPermission(ACCOUNTING_PAYMENT_METHOD_LIST)) {
            addView(new PaymentMethodListView());
        }

        if (Utils.hasPermission(ACCOUNTING_PRODUCT_TABLE_SETTINGS)) {
            addView(new ItemTableSettingsDraggableView());
        }
        if (Utils.hasPermission(ACCOUNTING_ACCOUNT_LIST)) {
            addView(new ChartOfAccountListView());
        }
    }


}
