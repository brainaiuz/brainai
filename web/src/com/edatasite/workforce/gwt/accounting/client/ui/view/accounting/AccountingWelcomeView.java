package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.landing.KpiWelcomeView;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 06.03.2009
 * Time: 11:25:24
 * To change this template use File | Settings | File Templates.
 */
public class AccountingWelcomeView extends View implements Constants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private FlexPanel mainContainer;
    private final boolean ACOUNTING_ISSETUP = "true".equals(Utils.userSettings.get(ACCOUNTING_IS_SETUP));
    private WfmButton2 gettingStarted;
    private FlexTable topTable;
    private FlexTable.FlexCellFormatter topFormatter;
    private WfmButton2 viewDemo;
    private WfmButton2 viewUserGuide;
    private static final String welcomePage = Utils.getHostName().contains("telemanaged") ? accountingStrings.moneyOverview() : accountingStrings.accountingWelcome();


    public AccountingWelcomeView() {
        super(ACCOUNTING_WELCOME, welcomePage);
    }

    public String getIconStyle() {
        return "accountMark af-welcome";
    }

    protected Widget onInitialize() {
        mainContainer = new FlexPanel();
        mainContainer.setStyleName("mainTable");
        drawTopPanel();
        drawBlockSchemaPanel();
        /*if (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(ACCOUNTANT) || Utils.hasRole(PM)) {
        } else if (!ACOUNTING_ISSETUP && (Utils.hasRole(CLIENT) || Utils.hasRole(MEM) || Utils.hasRole(TL))) {
            HorizontalPanel panel = new HorizontalPanel();
            panel.setWidth("97%");
            panel.setHeight("97%");
            HTML message = new HTML("<b>" + accountingStrings.welcome0() + " " + Utils.getSupportEmail() + " " + accountingStrings.welcome01() + "</b>");
            panel.add(message);
            panel.setCellHorizontalAlignment(message, HasHorizontalAlignment.ALIGN_CENTER);
            panel.setCellVerticalAlignment(message, HasVerticalAlignment.ALIGN_MIDDLE);
            mainContainer.add(panel);
        }*/

        add(mainContainer);
        return null;
    }

    private void drawTopPanel() {
        topTable = new FlexTable();
        topTable.setCellPadding(0);
        topTable.setCellSpacing(0);
        if (!Utils.isArabicLanguage()) {
            topTable.setStyleName("topTableStyle");
        } else {
            topTable.setStyleName("topTableStyleRTL");
        }
        topFormatter = topTable.getFlexCellFormatter();

        topTable.setHTML(0, 0, " ");
        topFormatter.setStyleName(0, 0, "top-l");

        topFormatter.setStyleName(0, 1, "top-c");
        VerticalPanel topCenter = new VerticalPanel();
        topCenter.setStyleName("topCenter");
        topCenter.setSpacing(4);
        if (ACOUNTING_ISSETUP) {
            topCenter.add(new HTML("<b class=welcomeTitle>" + accountingStrings.welcome10() + "</b>"));
            topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome11() + "</span> "));
            topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome12() + "</span> "));
            topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome13() + "</span>"));
        } else {
            topCenter.add(new HTML("<b class=welcomeTitle>" + accountingStrings.welcome10() + "</b>"));
            topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome21() + " " + "<b>" + wfmStrings.gettingStarted() + "</b>.</span> "));
            topCenter.add(new HTML("<span class=topText>" + accountingStrings.welcome22() + "</span>"));
        }
        topTable.setWidget(0, 1, topCenter);

        topTable.setHTML(0, 2, " ");
        topFormatter.setStyleName(0, 2, "top-r");
        mainContainer.add(topTable);
    }

   private void drawBlockSchemaPanel(){
       KpiWelcomeView welcomePanel = new KpiWelcomeView("accountingCover");
      // welcomePanel.addNoLink(accountingStrings.salesOrInvoice(),"salesOrInvoice");
       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD)) {
           welcomePanel.addSimpleLink(accountingStrings.registerQuote(), "accountingLink-1", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("salequote|add/add");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(accountingStrings.registerQuote(), "accountingLink-1");
       }
       welcomePanel.addNoLink(accountingStrings.sendToClient(), "accountingLink-4");
       welcomePanel.addNoLink(accountingStrings.approveORrejectQuote(), "accountingLink-7");
       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_ADD)) {
           welcomePanel.addSimpleLink(accountingStrings.registerSalesORinvoice(), "accountingLink-8", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("saleinvoice|add/add");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(accountingStrings.registerSalesORinvoice(), "accountingLink-8");
       }


       welcomePanel.addNoLink(accountingStrings.sendSalesORinvoiceToClient(), "accountingLink-11");
       welcomePanel.addNoLink(accountingStrings.registerPayment(), "accountingLink-13");
       welcomePanel.addNoLink(accountingStrings.generateReports(), "accountingLink-14");

      //  welcomePanel.addNoLink(accountingStrings.purchaseORbill(),"purchaseORbill");
       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_ADD)) {
           welcomePanel.addSimpleLink(accountingStrings.registerPurchaseORbill(), "accountingLink-2", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("purchaseinvoice|add/add");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(accountingStrings.registerPurchaseORbill(), "accountingLink-2");
       }

       welcomePanel.addNoLink(accountingStrings.approvePurchase(), "accountingLink-5");
       welcomePanel.addNoLink(accountingStrings.payForPurchase(), "accountingLink-9");
       welcomePanel.addNoLink(accountingStrings.generateReports(), "accountingLink-12");


     //  welcomePanel.addNoLink(accountingStrings.otherActions(),"other-actions");
       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_EXPENSE_REPORT_ADD)) {
           welcomePanel.addSimpleLink(accountingStrings.trackYourExpenses(), "accountingLink-3", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("expenseReports|add/add");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(accountingStrings.trackYourExpenses(), "accountingLink-3");
       }

       welcomePanel.addNoLink(accountingStrings.approveExpenseReports(), "accountingLink-6");
       welcomePanel.addNoLink(accountingStrings.payForExpense(), "accountingLink-10");

      // welcomePanel.addNoLink(accountingStrings.quickLinks(),"quick-links");
       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_ACCOUNT_LIST)) {
           welcomePanel.addSimpleLink(wfmStrings.chartOfAccounts(), "accountingLink-15", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("accounting|accountList/");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(wfmStrings.chartOfAccounts(), "accountingLink-15");
       }

       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_LIST)) {
           welcomePanel.addSimpleLink(wfmStrings.bankAccounts(), "accountingLink-16", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("accounting|bankaccount/");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(wfmStrings.bankAccounts(), "accountingLink-16");
       }

       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_TAX_RATES_LIST)) {
           welcomePanel.addSimpleLink(accountingStrings.taxRates(), "accountingLink-17", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("accounting|texes/");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(accountingStrings.taxRates(), "accountingLink-17");
       }

       if (Utils.hasPermission(PermissionConstants.ACCOUNTING_CUSTOMER_LIST)) {
           welcomePanel.addSimpleLink(wfmStrings.customers(), "accountingLink-18", clickEvent -> {
               if (ACOUNTING_ISSETUP) {
                   goTo("accounting|clientList/");
               } else {
                   showMessage();
               }
           });
       } else {
           welcomePanel.addNoLink(wfmStrings.customers(), "accountingLink-18");
       }
       mainContainer.add(welcomePanel);
   }

    private void showMessage() {
        Info.show(wfmStrings.workspaceWelcome4(), Info.Type.INFO);
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
}
