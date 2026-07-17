package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.CrmAccountBalance.NewCrmAccountBalance;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by admin on 10/9/2014.
 */
public class NewCrmAccountBalanceView extends View implements AccountingConstants, Constants, Colapse, FittedContent {

    private Integer crmAccountID;
    private String crmAccountType;

    public NewCrmAccountBalanceView(Integer crmAccountID, String crmAccountType) {
        super(CrmAccountItem.CUSTOMER.equals(crmAccountType) ? "customerBalance" : "supplierBalance",
                CrmAccountItem.CUSTOMER.equals(crmAccountType) ? wfmStrings.customerBalance() : Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplierBalance(), wfmStrings.supplier()));
        this.crmAccountID = crmAccountID;
        this.crmAccountType = crmAccountType;
    }

    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        add(new NewCrmAccountBalance(this.crmAccountID, this.crmAccountType));
        return null;
    }

    @Override
    public String getIconStyle() {
        return "payroll efile-to-hmrc";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                fireChanges();
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void fireChanges() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_OPENNING_BALANCE_TRANSACTION_DELETE, this, (sender, args) -> loadContent());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, this, (sender, args) -> loadContent());
    }

    private void loadContent() {
        NewCrmAccountBalanceView.super.clear();
        onInitialize();
    }
//    @Override
//    public FlowPanel getHelpContainer() {
//        FlowPanel panel = new FlowPanel();
//        panel.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
//        panel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
//        Anchor anchor = new Anchor();
//        anchor.setText("+" + wfmStrings.moreReports());
//        anchor.setHref(Utils.getHostURL() + "/Reporting.html");
//        anchor.setTarget("_blank");
//        panel.add(anchor);
//        return panel;
//    }
}
