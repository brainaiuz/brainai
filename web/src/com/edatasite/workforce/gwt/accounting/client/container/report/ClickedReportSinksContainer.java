package com.edatasite.workforce.gwt.accounting.client.container.report;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewAccountTransactionView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewAgingSummaryView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewBalanceSheetView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewBudgetSheetView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewCashFlowView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewJournalReportView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewProfitAndLossView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewStockValuationView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewTrialBalanceView;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.NewVatReturnReportView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 02.03.2009
 * Time: 0:09:27
 * To change this template use File | Settings | File Templates.
 */
public class ClickedReportSinksContainer extends SinksContainer implements PermissionConstants {

    public ClickedReportSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        if (Utils.hasPermission(ACCOUNTING_PROFIT_AND_LOSS)) {
            if (params != null) {
                addView(new NewProfitAndLossView(params));
            } else {
                addView(new NewProfitAndLossView());
            }
        }
        if (Utils.hasPermission(ACCOUNTING_BALANCE_SHEET)) {
            addView(new NewBalanceSheetView());
        }

        if (Utils.hasPermission(ACCOUNTING_BUDGET_SHEET)) {
            addView(new NewBudgetSheetView());
        }
        if (Utils.hasPermission(ACCOUNTING_JOURNAL_REPORT)) {
            if (params.length == 2) {
                if (params[1].equals("REFUND") || params[1].equals("GROUP_PAYRUN")) {
                    addView(new NewJournalReportView(Integer.valueOf(params[0]), true, params[1]));
                } else {
                    addView(new NewJournalReportView(Integer.valueOf(params[0]), "true".equals(params[1])));
                }
            } else {
                addView(new NewJournalReportView(Integer.valueOf(params[0])));
            }
        }
        if (Utils.hasPermission(ACCOUNTING_TRIAL_BALANCE)) {
            addView(new NewTrialBalanceView());
        }
        if (Utils.hasPermission(ACCOUNTING_CASH_FLOW)) {
            addView(new NewCashFlowView());
        }
        if (Utils.hasPermission(ACCOUNTING_ACCOUNT_TRANSACTIONS)) {
            if (id != null) {
                addView(new NewAccountTransactionView(id, params));
            } else {
                addView(new NewAccountTransactionView());
            }
        }

        if (Utils.isShowVatReturnReport() || Utils.isArabicCompany()) {
            if (Utils.hasPermission(ACCOUNTING_VAT_RETURN)) {
                addView(new NewVatReturnReportView());
            }
//            if (Utils.hasPermission(ACCOUNTING_VAT_RETURNS_LIST)) {
//                addView(new VatReturnReportsListView());
//            }
        }

        if (Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_RECEIVABLE)) {
            addView(new NewAgingSummaryView());
        }
        if (Utils.hasPermission(ACCOUNTING_AGING_SUMMARY_PAYABLE)) {
            addView(new NewAgingSummaryView(PAYABLE));
        }

        if (Utils.hasPermission(ACCOUNTING_STOCK_VALUATION)) {
            addView(new NewStockValuationView(true, (this.params != null && this.params.length > 0) ? Integer.parseInt(params[0]) : null));
        }
    }
}
