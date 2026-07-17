package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountSingleItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 18.01.2011
 * Time: 0:18:24
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsSummaryView extends CustomForm2 implements NoColapse {

    private Integer objectID;
    private HTML accountType, parentAccount, description, taxRate, code, name, currencyListBox, openingBalanceDate, openingBalanceAmount, showInExpense,active, showInLookUp, enablePayments, defaultAccount;

    private AccountSingleItem item;
    private FormHasCustomField customFieldUtil;


    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer accountID;

    public ChartOfAccountsSummaryView(Integer accountID) {
        super("chartOfAccountSummary", wfmStrings.summaryView());
        this.accountID = accountID;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.Account, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    ChartOfAccountsSummaryView.super.onInitialize();
                }
            }
        });

        return null;
    }


    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("account|edit/" + accountID, item.getCode(), item.getName());
        });
        addButton(editButton);
    }

    @Override
    protected void getDataToFillFields() {
        loadData();
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CHART_OF_ACCOUNT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
    protected void registerFields() {
        initInternal();

        addTitleField(CustomFormConstants.GENERAL_INFORMATION, wfmStrings.basicDetails());

        addField(CHART_ACCOUNT_TYPE, accountType, getTitle(wfmStrings.type()));

        addField(CHART_ACCOUNT_PARENT, parentAccount, getTitle(wfmStrings.parent()));

        addField(CHART_ACCOUNT_CODE, code, getTitle(wfmStrings.code()));

        addField(ACCOUNT_TAX_RATE, taxRate, getTitle(wfmStrings.taxRate()));

        addField(CHART_ACCOUNT_NAME, name, getTitle(wfmStrings.name()));

        addField(CHART_ACCOUNT_DESCRIPTION, description, getTitle(wfmStrings.description()));

        addField(SHOW_IN_EXPENCE, showInExpense, getTitle(accountingStrings.showInExpenseClaim()));
        addField(STATUS_OF_ACCOUNT, active, getTitle(wfmStrings.active()));
        addField(ENABLE_PAYMENTS, enablePayments, getTitle(wfmStrings.enablePaymentsToThisAccount()));
        getCustomFieldUtil().drawCustomFields(this, objectID, true);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        show();

    }

    private void initInternal() {

        name = new HTML();
        name.addStyleName(DEFAULT_WIDTH);

        accountType = new HTML();
        accountType.addStyleName(DEFAULT_WIDTH);

        parentAccount = new HTML();
        parentAccount.addStyleName(DEFAULT_WIDTH);

        code = new HTML();
        code.addStyleName(DEFAULT_WIDTH);

        showInExpense = new HTML();
        showInExpense.addStyleName(DEFAULT_WIDTH);

        active = new HTML();
        active.addStyleName(DEFAULT_WIDTH);

        enablePayments = new HTML();
        enablePayments.addStyleName(DEFAULT_WIDTH);

        taxRate = new HTML();
        taxRate.addStyleName(DEFAULT_WIDTH);

        description = new HTML();
        description.addStyleName(DEFAULT_WIDTH);

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void loadData() {
        AccountingService.App.get().getAccountById(accountID, new AsyncCallback<AccountSingleItem>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(AccountSingleItem accountSingleItem) {
                item = accountSingleItem;
                code.setText(accountSingleItem.getCode());
                name.setText(accountSingleItem.getName());
                description.setText(accountSingleItem.getDescription());
                accountType.setText(accountSingleItem.getType());
                taxRate.setText(accountSingleItem.getTaxRate());
                parentAccount.setText(accountSingleItem.getParentAccount());
                showInExpense.setText(accountSingleItem.getShowInExpense());
                active.setText(accountSingleItem.getActive());
                enablePayments.setText(accountSingleItem.getEnablePayments());
                getCustomFieldUtil().fillCustomFieldsWithData(accountSingleItem.getCustomFieldItems(), true);
            }
        });
    }

}
