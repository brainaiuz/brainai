package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.KsaVatReturnReport;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.NewVatReturnReport;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.UKVatReturnReportView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.VatReturnReport.UaeVatReturnReport;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by admin on 19.09.2014.
 */
public class NewVatReturnReportView extends View implements Colapse, AccountingConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private Integer vatReturnId;

    public NewVatReturnReportView() {
        super("vatReturn");
        setDescription(property.getPlural((Utils.getCustomTaxName() != null && !"".equals(Utils.getCustomTaxName())) ? (Utils.getCustomTaxName() + " Return") : accountingStrings.vatReturn()));
    }

    public NewVatReturnReportView(Integer vatReturnId) {
        super("vatReturn");
        setDescription(property.getPlural((Utils.getCustomTaxName() != null && !"".equals(Utils.getCustomTaxName())) ? (Utils.getCustomTaxName() + " Return") : accountingStrings.vatReturn()));
        this.vatReturnId = vatReturnId;
    }

    public interface VatResources extends ClientBundle {
        @CssResource.NotStrict
        @ClientBundle.Source("com/edatasite/workforce/gwt/accounting/resource/vatreturn/vatreturn.css")
        CssResource vatreturn();
    }

    public static VatResources resource = GWT.create(VatResources.class);

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);

        if (Utils.isUAECompany()) {
            resource.vatreturn().ensureInjected();
            add(new UaeVatReturnReport(vatReturnId, this));
        } else if (Utils.isSaudiCompany()) {
            resource.vatreturn().ensureInjected();
            add(new KsaVatReturnReport(vatReturnId, this));
        } else if (Utils.isUKVATRegistered()){
            resource.vatreturn().ensureInjected();
            add(new UKVatReturnReportView(vatReturnId, this));
        } else {
            add(new NewVatReturnReport());
        }
        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "salQuoLits sales-quote-list";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return "vatReturn";
    }
}
