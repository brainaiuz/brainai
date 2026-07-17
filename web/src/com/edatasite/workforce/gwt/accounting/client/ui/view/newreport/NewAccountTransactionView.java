package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.AccountTransaction.NewAccountTransaction;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by admin on 15.09.2014.
 */
public class NewAccountTransactionView extends View implements Colapse, AccountingConstants, FittedContent {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private Integer accountID;
    private String[] params;

    public NewAccountTransactionView() {
        super("transactionsByPeriod");
        setDescription(property.getPlural(accountingStrings.accountTransactions()));
    }

    public NewAccountTransactionView(Integer accountID, String[] params) {
        this();
        this.accountID = accountID;
        this.params = params;
    }


    @Override
    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        NewAccountTransaction newAccountTransaction;
        if (this.accountID != null) {
            newAccountTransaction = new NewAccountTransaction(this.accountID, this.params);
        } else {
            newAccountTransaction = new NewAccountTransaction();
        }
        add(newAccountTransaction);
        return null;
    }

    @Override
    public void reInitialize() {
        Utils.frame_affix_fixed_top();
    }

    @Override
    public String getIconStyle() {
        return "accountMark manual-journals";
    }

    @Override
    public FlowPanel getHelpContainer() {
        FlowPanel panel = new FlowPanel();
        // panel.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        panel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        Anchor anchor = new Anchor();
        anchor.setText("+" + wfmStrings.moreReports());
        anchor.addClickHandler((clickEvent -> {
            if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                Utils.openURL(Utils.getHostURL() + Constants.ACCOUTING_REPORT);
            } else {
                Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
            }
        }));
        anchor.getElement().getStyle().setPaddingLeft(10, Style.Unit.PX);
        panel.add(anchor);
        return panel;
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
        return "transactionsByPeriod";
    }
}
