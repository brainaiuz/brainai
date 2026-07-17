package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.WfmDropdown;
import com.edatasite.workforce.gwt.importfile.client.rpc.ChartOfAccountsImportItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportStatus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 3, 2011
 * Time: 5:49:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportChartOfAccountsView extends View implements AccountingConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private SelectItem[] items;
    private char defaultSeparator = ',';

    private final Integer objectID;
    private WfmForm table;

    private ReferenceInsertionTable accountType;
    private DataListBox parent;
    private DataListBox code;
    private DataListBox name;
    private DataListBox description;
    private ReferenceInsertionTable taxRate;
    private DataListBox showInExpenseClaims;
    private DataListBox enablePayments;

    private WfmForm.Field codeField;
    private WfmForm.Field parentField;
    private WfmForm.Field nameField;

    private WfmDropdown sysAccountTypeItems;
    private WfmDropdown sysTaxRateItems;

    private KpiCheckBox hasHeader;
    private final boolean fromGettingStarted;

    public ImportChartOfAccountsView(Integer objectID, boolean fromGettingStarted) {
        super("add", accountingStrings.importChartOfAccounts());
        this.objectID = objectID;
        this.fromGettingStarted = fromGettingStarted;
    }

    @Override
    protected Widget onInitialize() {
        initialize();
        return null;
    }

    private void initialize() {
        LoadingPanel.loading(true);
        table = new WfmForm();
        table.setLabelSize("150px");

        sysAccountTypeItems = new WfmDropdown();
        sysTaxRateItems = new WfmDropdown();
        parent = new DataListBox();
        code = new DataListBox();
        name = new DataListBox();
        description = new DataListBox();
        showInExpenseClaims = new DataListBox();
        enablePayments = new DataListBox();

        AccountingService.App.get().getAccountTypes(new AsyncCallback<AccountTypesByCategory>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(AccountTypesByCategory atCat) {
                sysAccountTypeItems.addItems(wfmStrings.assets(), atCat.getAssets());
                sysAccountTypeItems.addItems(wfmStrings.liabilities(), atCat.getLiabilities());
                sysAccountTypeItems.addItems(wfmStrings.equity(), atCat.getEquity());
                sysAccountTypeItems.addItems(wfmStrings.revenue(), atCat.getRevenue());
                sysAccountTypeItems.addItems(wfmStrings.expenses(), atCat.getExpenses());
                sysAccountTypeItems.addItems(accountingStrings.creditCard(), atCat.getCreditCard());
            }
        });

        AccountingService.App.get().getCompanyTaxes(new AsyncCallback<TaxItem[]>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(TaxItem[] taxItems) {
                sysTaxRateItems.addItems(taxItems);
            }
        });

        accountType = new ReferenceInsertionTable(wfmStrings.accountType(), sysAccountTypeItems, true, table,
                accountingStrings.accountTypesYourFileShouldMatch());
        parentField = table.addField(accountingStrings.parentCode(), parent);
        codeField = table.addField(wfmStrings.code(), code, true);
        nameField = table.addField(wfmStrings.name(), name, true);
        table.addField(wfmStrings.description(), description);
        taxRate = new ReferenceInsertionTable(wfmStrings.taxRate(), sysTaxRateItems, table);
        table.addField(Property.getPluralWithObjectCodeWithReplace(Constants.EXPENSES_CLAIM, accountingStrings.showInExpenseClaims(), wfmStrings.expenseClaims()), showInExpenseClaims);
        table.addField(wfmStrings.enablePaymentsToThisAccount(), enablePayments);

        hasHeader = new KpiCheckBox("");
        hasHeader.setValue(Boolean.TRUE);
        table.addField(wfmStrings.myCSVFileHasHeaders(), hasHeader);

        WfmButton2 saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        saveAndCloseButton.addClickHandler(clickEvent -> save());
        cancelButton.addClickHandler(clickEvent -> closeTab());


        AccountingService.App.get().getCSVColumns(objectID, new AsyncCallback<HashMap<String, SelectItem[]>>() {
            public void onFailure(Throwable d) {
                LoadingPanel.loading(false);
            }

            public void onSuccess(final HashMap<String, SelectItem[]> o) {
                DeferredCommand.addCommand(() -> {
                    for (Map.Entry<String, SelectItem[]> entry : o.entrySet()) {
                        String key = entry.getKey();
                        items = entry.getValue();
                        if (!key.equals(String.valueOf(defaultSeparator))) {
                            defaultSeparator = key.charAt(0);
                        }
                    }
                    setItems(accountType.getCsvDataListBox(), items, wfmStrings.accountType(), wfmStrings.type());
                    setItems(parent, items, accountingStrings.parentCode());
                    setItems(code, items, wfmStrings.code());
                    setItems(name, items, wfmStrings.name());
                    setItems(description, items, wfmStrings.description());
                    setItems(taxRate.getCsvDataListBox(), items, wfmStrings.taxRate());
                    setItems(showInExpenseClaims, items, Property.getPluralWithObjectCodeWithReplace(Constants.EXPENSES_CLAIM, accountingStrings.showInExpenseClaims(), wfmStrings.expenseClaims()), property != null ? property.getPlural(wfmStrings.expenseClaims()) : wfmStrings.expenseClaims());
                    setItems(enablePayments, items, wfmStrings.enablePaymentsToThisAccount(), wfmStrings.enablePaymentsToThisAccount());

                    LoadingPanel.loading(false);
                });
            }
        });

        table.addButton(saveAndCloseButton);
        table.addButton(cancelButton);

        add(table);
    }

    private void setItems(DataListBox dataListBox, SelectItem[] items, String... labels) {
        dataListBox.setItems(items);

        for (SelectItem item : items) {
            if (asList(labels).contains(item.getName().toLowerCase().trim())) {
                dataListBox.setSelected(item);
            }
        }
    }

    private List<String> asList(String... labelArray) {
        List<String> labels = new LinkedList<>();
        for (String aLabelArray : labelArray) {
            labels.add(aLabelArray.toLowerCase().trim());
        }
        return labels;
    }

    private void save() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);

        ChartOfAccountsImportItem importItem = new ChartOfAccountsImportItem();
        importItem.setObjectId(objectID);
        importItem.setAccounType(accountType.getData());
        importItem.setParentCodeId(parent.getSelectedId());
        importItem.setCodeId(code.getSelectedId());
        importItem.setNameId(name.getSelectedId());
        importItem.setDecriptionId(description.getSelectedId());
        importItem.setTaxRate(taxRate.getData());
        importItem.setShowInExpenseId(showInExpenseClaims.getSelectedId());
        importItem.setEnablePaymentsId(enablePayments.getSelectedId());

        ImportFile importFile = importItem.getImportFile();
        importFile.setDefaultSeparator(defaultSeparator);
        importFile.setType(ImportTypeEnum.CHART_OF_ACCOUNTS);
        importFile.setHasHeader(hasHeader.getValue());
        importFile.setFileID(objectID);

//        if(fromGettingStarted){
        AccountingService.App.get().onChartOfAccountsImport(importFile, fromGettingStarted, new AsyncCallback<ImportStatus>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                showFailureMessage(accountingStrings.errorOccuredWhileImportingChartOfAccounts());
            }

            public void onSuccess(ImportStatus result) {
                LoadingPanel.loading(false);
//                if (result.getImportedAccounts().length > 0) {
                showSuccessMessage(result);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, ImportChartOfAccountsView.this);
//                } else {
//                    showFailureMessage(accountingStrings.yourChartOfAccountsHasNotBeenImportedPleaseCheckAllYourColumnsAndTheAccountTypesWithSample());
//                }
            }
        });
//        }else{
//            AccountingService.App.get().addImportToQueue(importFile, "chartofaccounts", new AsyncCallback<String>() {
//                @Override
//                public void onFailure(Throwable caught) {
//                    LoadingPanel.loading(false);
//                    showFailureMessage();
//                }
//
//                @Override
//                public void onSuccess(String result) {
//                    LoadingPanel.loading(false);
//                    if (result != null && !"".equals(result)) {
//                        String errorMessage = result + " " + wfmStrings.importIsAlreadyInProgress();
//                        showFailureMessage(errorMessage);
//                    } else {
//                        showSuccessMessage();
////                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ACCOUNT_SAVED, result, ImportChartOfAccountsView.this);
//                    }
//                }
//            });
//        }

    }

    private void showSuccessMessage(final ImportStatus result) {
        final KpiModal messageBox = new KpiModal();
        messageBox.setTitle(wfmStrings.success());

        VerticalPanel messagePanel = new VerticalPanel();
        StringBuilder message = new StringBuilder();
        message.append(Utils.textFormat(wfmStrings.messSuccessfullyImported(), wfmStrings.chartOfAccounts()));
        message.append(result.getImportedAccounts().length + " " + (result.getImportedAccounts().length > 1 ? accountingStrings.accountsImported() : accountingStrings.accountImported()));
        if (result.getNotImportedAccounts().length > 0) {
            message.append(", " + result.getNotImportedAccounts().length + " " + (result.getNotImportedAccounts().length > 1 ? accountingStrings.accountsHaveNotBeenImportedPleaseAddManually() : accountingStrings.accountHasNotBeenImportedPleaseAddManually()));
        }
        message.append(" " + accountingStrings.pleaseCheckSystemLogs());
        messagePanel.add(new Label(message.toString()));

//        SimpleLink showImportedAccounts = new SimpleLink(wfmStrings.showImportedAccounts());
//        showImportedAccounts.addClickHandler(clickEvent -> showAccountsPanel(wfmStrings.importedAccounts(), result.getImportedAccounts()));
//        messagePanel.add(showImportedAccounts);
//
//        if (result.getNotImportedAccounts().length > 0) {
//            SimpleLink showNotImportedAccounts = new SimpleLink(accountingStrings.showNotImportedAccounts());
//            showNotImportedAccounts.addClickHandler(clickEvent -> showAccountsPanel(accountingStrings.notImportedAccounts(), result.getNotImportedAccounts()));
//            messagePanel.add(showNotImportedAccounts);
//        }
        messagePanel.setSpacing(10);

        Button close = new Button(wfmStrings.close());
        close.addClickHandler(clickEvent -> messageBox.close());

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, messagePanel);
        table.setWidget(1, 1, close);
        table.setCellSpacing(10);
        table.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        messageBox.add(table);
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
        messageBox.setWidth("300px");
        messageBox.open();
    }

//    private void showAccountsPanel(String name, String[] result) {
//        final DialogBox dialogBox = new DialogBox();
//        dialogBox.setText(name);
//
//        VerticalPanel mainPanel = new VerticalPanel();
//        ScrollPanel scrollPanel = new ScrollPanel();
//        scrollPanel.setSize("300px", "200px");
//        VerticalPanel accountsPanel = new VerticalPanel();
//        for (String aResult : result) {
//            accountsPanel.add(new Label(aResult));
//        }
//        scrollPanel.add(accountsPanel);
//        mainPanel.add(scrollPanel);
//
//        Button closeButton = new Button(wfmStrings.close());
//        closeButton.addClickHandler(clickEvent -> dialogBox.hide());
//        mainPanel.add(closeButton);
//        mainPanel.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
//
//        mainPanel.setSpacing(10);
//        dialogBox.add(mainPanel);
//        dialogBox.center();
//    }

    private void showFailureMessage(String message) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
        messageBox.setTitle(wfmStrings.error());
        messageBox.setMessage(message);
        messageBox.open();
        messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
    }

    private boolean validate() {
        int errors = 0;
        table.cleanupErrors();
        if (!Validation.validateListBoxRequired(accountType.getCsvDataListBox(), accountType.getField(), accountingStrings.pleaseChooseAccountType())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(code, codeField, accountingStrings.pleaseSelectCode())) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(name, nameField, accountingStrings.pleaseSelectName())) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
