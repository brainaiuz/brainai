package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionPDFObject;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.WftHTMLPanel;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CommonLookup;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import org.gwt.advanced.client.ui.widget.EditableGrid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckAddEditView extends FooteredView implements Colapse, AccountingConstants, FittedContent, AccountingCustomFormConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private BankAccountLookUp bankAccountLookUp;
    private CurrencyWidget currencyWidget;
    private Numbering numberWidget;
    private TextBox payToTxtBox;
    private DatePicker datePicker;
    private TextBox amountTxtBox;
    private Label amountAsStringLabel;
    private Label currencyLabel;
    private TextArea addressTxtArea;
    private TextBox memoTxtBox;
    private ProjectLookUp projectLookUp;
    private Label projectItem;
    private KpiCheckBox checkToBePrintCheckBox;
    private HTMLPanel htmlPanel;
    private MaterialLink bankBalanceLink;

    private FormGroup postedDateGroup;
    private GColumn postedDateColumn;
    private KpiCheckBox postDated;

    private EditableTable bankCheckItemsTable;
    private HashMap<String, Widget> widgetsMap;

    private WfmButton2 saveAndCloseButton, saveAndNewButton;
    private SplitButton printPdfSplitButton;

    private Integer objectID;

    private NumberData numberData;

    private boolean isSummaryView;

    private final boolean projectInLineItem = Utils.isProjectInLineItemEnable();

    private boolean isPostDatedFeatureEnabled = false;

    private LinkedHashMap<String, DynamicTableColumn> columnsMap;

    private final String checkView = "check_View";
    private CurrencyItem[] availableCurrencies;
    private BankCheckData checkData;

    public CheckAddEditView() {
        super("check", accountingStrings.writeCheck());
    }

    public CheckAddEditView(Integer objectID, boolean isSummaryView) {
        super(isSummaryView ? "summary" : "edit", accountingStrings.writeCheck());
        this.objectID = objectID;
        this.isSummaryView = isSummaryView;
    }

    @Override
    protected Widget onInitialize() {
        widgetsMap = new HashMap<>();
        initForm();
        loadFormData();
        return null;
    }

    private void initForm() {
        bankAccountLookUp = new BankAccountLookUp();
        bankAccountLookUp.ensureDebugId(checkView + "bankAccountLookUp");
        bankAccountLookUp.setEnsureDebugId(checkView + "bankAccountLookUp");
        bankAccountLookUp.setEnsureSuggestBox(checkView + "bankAccountLookUp");
        bankAccountLookUp.setAutocompleteOff();
        bankAccountLookUp.getSuggestBox().addSelectionHandler(h -> {
            if (availableCurrencies == null) {
                availableCurrencies = (CurrencyItem[]) currencyWidget.getCurrencyListBox().getItems();
            }
            onBankChange(bankAccountLookUp.getSelectedData());
        });

        bankBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        bankBalanceLink.setHref("javaScript:void(0)");
        bankBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");
        numberWidget = new Numbering();
        numberWidget.getTxtPrefix().setWidth("80px");
        numberWidget.ensureDebugId(checkView + "numberWidget");
        payToTxtBox = new TextBox(true);
        payToTxtBox.ensureDebugId(checkView + "payToTxtBox");
        datePicker = new DatePicker(new Date());
        datePicker.ensureDebugId(checkView + "datePicker");
        amountTxtBox = new TextBox(true);
        amountTxtBox.ensureDebugId(checkView + "amountTxtBox");
        amountAsStringLabel = new Label();
        amountAsStringLabel.getElement().getStyle().setDisplay(Style.Display.TABLE);
        currencyLabel = new Label();
        addressTxtArea = new TextArea();
        addressTxtArea.ensureDebugId(checkView + "addressTxtArea");
        addressTxtArea.setHeight("50px");
        memoTxtBox = new TextBox(true);
        memoTxtBox.ensureDebugId(checkView + "memoTxtBox");
        projectLookUp = new ProjectLookUp(null, null);
        projectLookUp.ensureDebugId(checkView + "projectLookUp");
        projectLookUp.setAutocompleteOff();
        projectItem = new Label();
        projectItem.ensureDebugId(checkView + "projectItem");
        checkToBePrintCheckBox = new KpiCheckBox(accountingStrings.checkToBePrinted());
        checkToBePrintCheckBox.ensureDebugId(checkView + "checkTobePrinted");

        postDated = new KpiCheckBox();
        postDated.ensureDebugId(checkView + "postDated");

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addListener(() -> currencyLabel.setText(currencyWidget.getCurrencyName()));
        currencyWidget.setValidator(() -> {
            if (!isCurrencyRequirementsValid(currencyWidget.getCurrencyID())) {
                Info.show(accountingMessages.theCurrencyOfTheManualEntryMust(), Info.Type.WARNING);

                if (checkData.getCurrencyItem() != null) {
                    currencyWidget.setCurrency(checkData.getCurrencyItem().getId());
                }
                return false;
            }
            return true;
        });
        currencyWidget.setDatePicker(datePicker);
        currencyWidget.ensureDebugId(checkView + "currency");
        currencyWidget.setEnabled(objectID != null && !isSummaryView);


        amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(amountTxtBox, AccountingUtils.calculationScale);
        amountTxtBox.addBlurHandler(blurEvent -> fillAmountLabelAsString());
        amountTxtBox.addChangeHandler(c -> {
            amountTxtBox.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(amountTxtBox.getText())));
        });

        bankAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> onBankAccountChange(bankAccountLookUp.getSelectedData()));
        checkToBePrintCheckBox.addClickHandler(event -> onCheckToBePrintedChange());
        postedDateGroup = new FormGroup(wfmStrings.postDated(), postDated);
        postedDateGroup.setVisible(false);
        postedDateColumn = new GColumn(postedDateGroup);
        postedDateColumn.setVisible(false);
        if (isSummaryView) {
            bankAccountLookUp.setEnabled(false);
            numberWidget.getTxtPrefix().setEnabled(false);
            numberWidget.getTxtNumber().setEnabled(false);
        }
        new KpiToolTip(postDated, accountingStrings.postedDateInfo());

        initWidgetsMap();
        if (isSummaryView) {
            disableWidgets();
        }

        datePicker.addChangeHandler(changeEvent -> {
            if (isDatePosted()) {
                clearDatePicker();
            }
        });
        postDated.addValueChangeHandler(valueChangeEvent -> {
            if (isDatePosted()) {
                clearDatePicker();
            }
        });
    }

    private void initWidgetsMap() {
        widgetsMap.put(INPUT_NUMBER, new FormGroup(wfmStrings.no(), numberWidget));
        widgetsMap.put(INPUT_DATE, new FormGroup(wfmStrings.date(), datePicker));
        if (!projectInLineItem) {
            if (isSummaryView) {
                FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), wrapWidgetToFormControl(projectItem));
                projectField.setMarginBottom(5);
                widgetsMap.put(INPUT_PROJECT, projectField);
            } else {
                FormGroup projectField = new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp);
                projectField.setMarginBottom(5);
                widgetsMap.put(INPUT_PROJECT, projectField);
            }
        }
        widgetsMap.put(INPUT_CHECKBOX_OPTION_PANEL, checkToBePrintCheckBox);
        widgetsMap.put(INPUT_POST_DATED, postedDateColumn);

        FormGroup bankAccountField = new FormGroup(bankAccountLookUp);
        bankAccountField.ensureDebugId(checkView + "bankLookUp");

        Div bankaccountLabel = bankAccountField.getGroupLabel();
        bankaccountLabel.addStyleName("label-group");

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(bankBalanceLink);

        bankaccountLabel.add(new Span(accountingStrings.bank()));
        bankaccountLabel.add(balance);

        widgetsMap.put(INPUT_BANK, bankAccountField);
        widgetsMap.put(INPUT_PAY_TO, new FormGroup(wfmStrings.payTo(), payToTxtBox, true));
        widgetsMap.put(INPUT_AMOUNT, new FormGroup(wfmStrings.amount(), amountTxtBox, true));
        widgetsMap.put(INPUT_CURRENCY, new FormGroup(wfmStrings.currency(), currencyWidget, true));

        HorizontalPanel hp = new HorizontalPanel();
        VerticalPanel vp = new VerticalPanel();
        DOM.setStyleAttribute(vp.getElement(), "borderBottom", "1px solid black");
        vp.add(amountAsStringLabel);
        hp.add(vp);
        hp.add(currencyLabel);

        widgetsMap.put(LABEL_AMOUNT, new FormGroup(accountingStrings.amountInWords(), hp));
        widgetsMap.put(INPUT_MEMO, new FormGroup(wfmStrings.description(), memoTxtBox));
        widgetsMap.put(INPUT_MAIL_ADDRESS, new FormGroup(wfmStrings.address(), addressTxtArea));
        widgetsMap.put(INPUT_ITEM_TABLE, createBottomPanel());
    }

    private boolean isDatePosted() {

        Date _today = new Date();
        Date today = new Date(_today.getYear(), _today.getMonth(), _today.getDate());
        Date _pick = datePicker.getDate();
        if (_pick != null) {
            Date datePickerDay = new Date(_pick.getYear(), _pick.getMonth(), _pick.getDate());
            return postDated.getValue() && datePickerDay.before(today);
        } else {
            return false;
        }
    }


    private void disableWidgets() {
        bankAccountLookUp.setEnabled(false);
        payToTxtBox.setEnabled(false);
        datePicker.setEnabled(false);
        amountTxtBox.setEnabled(false);
        //amountAsStringLabel;
        addressTxtArea.setEnabled(false);
        memoTxtBox.setEnabled(false);
        projectLookUp.setEnabled(false);
        checkToBePrintCheckBox.setEnabled(false);
    }

    private void loadFormData() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getBankCheckData(objectID, new AsyncCallback<BankCheckData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(BankCheckData result) {
                checkData = result;
                LoadingPanel.loading(false);
                htmlPanel = new WftHTMLPanel(result.getLayoutHtml(), widgetsMap).getContainer();
                htmlPanel.setStyleName("add-form invoice-form");
                htmlPanel.add(createFooter());
                add(htmlPanel);
                setFormData(result);
            }
        });
    }


    private void fillAmountLabelAsString() {
        if (amountTxtBox.getText() != null && !amountTxtBox.getText().isEmpty()) {
            amountAsStringLabel.setText(AccountingUtils.get().numberToWord(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)));
        }

    }

    private void setFormData(BankCheckData result) {
        numberData = result.getNumberData();

        if (result.getObjectID() == null && result.isEnabledPostDatedTransaction()) {
            postedDateGroup.setVisible(true);
            postedDateColumn.setVisible(true);
            isPostDatedFeatureEnabled = true;
        }

        if (objectID != null) {
            bankAccountLookUp.addBankAccountItem(result.getBankAccount());
            onBankAccountChange(bankAccountLookUp.getSelectedData());
            payToTxtBox.setText(result.getPayTo());
            datePicker.setDate(result.getDate().getNonConvertedDate());
            amountTxtBox.setText(AccountingUtils.get().formatPrice(result.getAmount()));
            addressTxtArea.setText(result.getAddress());
            memoTxtBox.setText(result.getMemo());
            if (result.getProject() != null && !projectInLineItem) {
                if (isSummaryView) {
                    projectItem.setText(result.getProject().getName());
                } else {
                    projectLookUp.setSelected(result.getProject());
                }
            }
            checkToBePrintCheckBox.setValue(result.getToBePrinted());
            onCheckToBePrintedChange();

            for (BankCheckItem bci : result.getItems()) {
                bankCheckItemsTable.addRow(getWidgetArray(bci));
            }
            currencyLabel.setText(result.getCurrencyName());
            postDated.setValue(result.isPostDatedTransaction());
            fillAmountLabelAsString();

            if (result.getCurrencyItem() != null) {
                currencyWidget.setCurrency(result.getCurrencyItem().getId(), result.getExchageRate());
            }
        } else {
            numberWidget.setNumberData(result.getNumberData());
            bankCheckItemsTable.addRow(getWidgetArray(null));
            bankCheckItemsTable.addRow(getWidgetArray(null));
            bankCheckItemsTable.addRow(getWidgetArray(null));
            currencyLabel.setText(result.getCurrencyName());
            fillAmountLabelAsString();
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null) {
            result.getTemplates();
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(htmlPanel, pdfItem.getId())));
            }
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem("PDF_VERSION", wfmStrings.pdfVersion(), () -> generatePDF(htmlPanel, finalDefaultTemplateId), true);
        pdfVersion.ensureDebugId("check_" + "pdfVersionItem");
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
        printPdfSplitButton.setVisible(true);
    }

    private void onBankAccountChange(BankAccountItem selectedData) {
        if (selectedData != null && selectedData.getBalance() != null) {
            if (selectedData.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
                bankBalanceLink.setText(AccountingUtils.get().formatPrice(selectedData.getBalance()) + " " + currencyLabel.getText());
            } else {
                bankBalanceLink.setText("(" + AccountingUtils.get().formatPrice(selectedData.getBalance().multiply(new BigDecimal(-1))) + ") " + currencyLabel.getText());
            }
        }
    }

    private void onCheckToBePrintedChange() {
        if (checkToBePrintCheckBox.getValue()) {
            numberWidget.getTxtPrefix().setEnabled(false);
            numberWidget.getTxtNumber().setEnabled(false);
            numberWidget.getTxtPrefix().setText("");
            numberWidget.getTxtNumber().setText("");
        } else {
            if (!isSummaryView) {
                numberWidget.getTxtPrefix().setEnabled(true);
                numberWidget.getTxtNumber().setEnabled(true);
            }
            numberWidget.setNumberData(numberData);
        }
    }

    private ColumnConfig[] getColumns() {
        int index = 0;
        int columnCount = 5;
        if (projectInLineItem) {
            columnCount++;
        }
        ColumnConfig[] columns = new ColumnConfig[columnCount];
        columns[index] = new ColumnConfig(LookUpCell.class, "account", wfmStrings.account(), 200, true);
        columns[++index] = new ColumnConfig(CustomCell.class, "amount", wfmStrings.amount(), 100, true);
        columns[++index] = new ColumnConfig(CustomCell.class, "description", wfmStrings.description(), 250, false);
        columns[++index] = new ColumnConfig(LookUpCell.class, "crmaccount", wfmStrings.name(), 200, false);
        columns[++index] = new ColumnConfig(LookUpCell.class, "client", accountingStrings.billing(), 150, false);
        if (projectInLineItem) {
            columns[++index] = new ColumnConfig(LookUpCell.class, PROJECT_COLUMN, Property.get(Constants.PROJECT, wfmStrings.project()), 150, false);
        }

        return columns;
    }

    private EditableTable createBottomPanel() {

        bankCheckItemsTable = new EditableTable(getColumns(), true, true);
        bankCheckItemsTable.setDraggable(true);

        bankCheckItemsTable.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                bankCheckItemsTable.addRow(getWidgetArray(null));
            }

            @Override
            public void removeRow() {

            }
        });

        return bankCheckItemsTable;
    }

    private Object[] getWidgetArray(BankCheckItem bankCheckItem) {
        int index = 0;
        int colCount = 5;
        if (projectInLineItem) {
            colCount++;
        }
        Object[] objects = new Object[colCount];
        final AccountLookUpForExpense accountsLookUp = new AccountLookUpForExpense(AccountingConstants.BANK_CHECK);
        accountsLookUp.setAutocompleteOff();
        ExtendedTextBox amountTxtBox = new ExtendedTextBox();
        amountTxtBox.getElement().setAttribute("autocomplete", "off");
        amountTxtBox.addChangeHandler(c -> {
            amountTxtBox.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(amountTxtBox.getText())));
        });

        ExtendedTextBox descriptionTxtBox = new ExtendedTextBox();
        CrmAccountLookUp crmAccountLookUp = new CrmAccountLookUp(null, true);
        CommonLookup clientLooUp = new CommonLookup(CommonLookup.CUSTOMER, true);
        ProjectLookUp itemProjectLookUp = new ProjectLookUp(null, clientLooUp);
        itemProjectLookUp.setAutocompleteOff();
        clientLooUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            itemProjectLookUp.clearOracleItems();
            itemProjectLookUp.clearAndClearItems();
            itemProjectLookUp.setClientSupplierLookUp(clientLooUp);
            LookUpCell lookUpCell = (LookUpCell) bankCheckItemsTable.getColumnCellWidgetById(bankCheckItemsTable.getGrid().getCurrentRow(), "project");
            lookUpCell.getWidget().getElement().setInnerHTML("");

        });

        accountsLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            Integer accountKey = accountsLookUp.getSelectedData().getAccountKey();

            LookUpCell lookUpCell = (LookUpCell) bankCheckItemsTable.getColumnCellWidgetById(bankCheckItemsTable.getGrid().getCurrentRow(), "crmaccount");
            if (ACCOUNTS_PAYABLE_KEY.equals(accountKey)) {
                lookUpCell.getLookUp().setEnabled(true);
                ((CrmAccountLookUp) lookUpCell.getLookUp()).setTypeCode(CrmAccountLookUp.SUPPLIER);
            } else {
                lookUpCell.getLookUp().setEnabled(false);
            }
            lookUpCell.InActive();
        });

        amountTxtBox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(amountTxtBox, AccountingUtils.calculationScale);
        amountTxtBox.addKeyUpHandler(keyUpEvent -> calculate());

        if (bankCheckItem != null) {
            accountsLookUp.addAccountItem(bankCheckItem.getAccount());
            amountTxtBox.setText(AccountingUtils.get().formatPrice(bankCheckItem.getAmount()));
            amountTxtBox.setItemQbId(bankCheckItem.getQuickbookItemID());
            descriptionTxtBox.setText(bankCheckItem.getDescription());
            if (bankCheckItem.getCrmAccount() != null) {
                crmAccountLookUp.addItem(bankCheckItem.getCrmAccount());
            }

            if (bankCheckItem.getAccount() != null && ACCOUNTS_PAYABLE_KEY.equals(bankCheckItem.getAccount().getAccountKey())) {
                crmAccountLookUp.setEnabled(true);
                crmAccountLookUp.setTypeCode(CrmAccountLookUp.SUPPLIER);
            } else {
                crmAccountLookUp.setEnabled(false);
            }
            if (bankCheckItem.getClient() != null) {
                clientLooUp.setSelected(bankCheckItem.getClient());
            }
            if (bankCheckItem.getProject() != null) {
                itemProjectLookUp.setSelected(bankCheckItem.getProject());
            }
        }

        objects[index] = accountsLookUp;
        objects[++index] = amountTxtBox;
        objects[++index] = descriptionTxtBox;
        objects[++index] = crmAccountLookUp;
        objects[++index] = clientLooUp;
        if (projectInLineItem) {
            objects[++index] = itemProjectLookUp;
        }
        return objects;
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return CheckAddEditView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> rigthSideWidgets = new ArrayList<>();

        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndCloseButton.ensureDebugId(checkView + "saveAndCloseButton");
        saveAndCloseButton.addClickHandler(event -> save(true));

        saveAndNewButton = new WfmButton2(wfmStrings.saveAndNew(), WfmButton2.BTN_PRIMARY);
        saveAndNewButton.ensureDebugId(checkView + "saveAndNew");
        saveAndNewButton.addClickHandler(event -> save(false));

        printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
        printPdfSplitButton.setVisible(false);


        Div saveAndCloseWrapper = new Div();
        Div saveAndNewWrapper = new Div();
        Div pdfButtonWrapper = new Div();

        saveAndCloseWrapper.add(saveAndCloseButton);
        saveAndNewWrapper.add(saveAndNewButton);
        pdfButtonWrapper.add(printPdfSplitButton);

        if (isSummaryView) {
            rigthSideWidgets.add(pdfButtonWrapper);
        } else {
            rigthSideWidgets.add(saveAndCloseWrapper);
            rigthSideWidgets.add(saveAndNewWrapper);
        }

        return rigthSideWidgets;
    }

    private void generatePDF(HTMLPanel hp, Integer templateID) {
        String pdfURL = null;
        TransactionPDFObject requestObject = new TransactionPDFObject(objectID);
        pdfURL = CommandConstants.PDF_URL + "/checkPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        if (templateID != null) {
            parametrs.put("templateID", String.valueOf(templateID));
        }
        Utils.sendPDFOrExcelRequest(hp, pdfURL, parametrs, "_blank");
    }

    private void save(final boolean closeTab) {
        setEnabledButtons(false);
        if (!validate()) {
            setEnabledButtons(true);
            return;
        }

        BankCheckData bankCheckData = new BankCheckData();
        bankCheckData.setObjectID(objectID);
        bankCheckData.setBankAccount(bankAccountLookUp.getSelectedData());
        bankCheckData.setPayTo(payToTxtBox.getText());
        bankCheckData.setDate(new DateNonConvertable(datePicker.getDate()));
        bankCheckData.setAmount(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()));
        bankCheckData.setAddress(addressTxtArea.getText());
        bankCheckData.setMemo(memoTxtBox.getText());
        bankCheckData.setCurrencyItem(currencyWidget.getCurrency());
        bankCheckData.setExchageRate(currencyWidget.getExchangeRate());
        if (!projectInLineItem) {
            bankCheckData.setProject(projectLookUp.getSelectedItem());
        }
        bankCheckData.setAmountStringWord(amountAsStringLabel.getText());
        bankCheckData.setToBePrinted(checkToBePrintCheckBox.getValue());
        if (!checkToBePrintCheckBox.getValue()) {
            bankCheckData.setNumberData(numberWidget.getNumberData(false));
        }

        if (postDated.getValue()) {
            if (datePicker.getDate().after(DateUtil.getDayLastTime(new Date()))) {
                bankCheckData.setPostDatedTransaction(true);
            }
        }

        BigDecimal itemsTotalAmount = BigDecimal.ZERO;
        EditableGrid editableGrid = bankCheckItemsTable.getGrid();
        LinkedList<BankCheckItem> checkItemsList = new LinkedList<>();
        for (int i = 0; i < editableGrid.getRowCount(); i++) {
            if (bankCheckItemsTable.isItemValid(i)) {
                AccountLookUpForExpense accountLookUpForExpense = (AccountLookUpForExpense) bankCheckItemsTable.getColumnById(i, "account");
                ExtendedTextBox amountTxtBox = (ExtendedTextBox) bankCheckItemsTable.getColumnById(i, "amount");
                ExtendedTextBox descriptionTxtBox = (ExtendedTextBox) bankCheckItemsTable.getColumnById(i, "description");
                ProjectLookUp itemProjectLookUp = (ProjectLookUp) bankCheckItemsTable.getColumnById(i, PROJECT_COLUMN);
                CrmAccountLookUp crmAccountLookUp = (CrmAccountLookUp) bankCheckItemsTable.getColumnById(i, "crmaccount");
                CommonLookup clientLookUp = (CommonLookup) bankCheckItemsTable.getColumnById(i, "client");

                BankCheckItem checkItem = new BankCheckItem();
                checkItem.setAccount(accountLookUpForExpense.getSelectedData());
                checkItem.setAmount(AccountingUtils.get().parseToBigDecimal(amountTxtBox.getText()));
                checkItem.setQuickbookItemID(amountTxtBox.getItemQbId());
                checkItem.setDescription(descriptionTxtBox.getText());
                if (projectInLineItem) {
                    checkItem.setProject(itemProjectLookUp.getSelectedItem());
                }
                if (accountLookUpForExpense.getSelectedData() != null && ACCOUNTS_PAYABLE_KEY.equals(accountLookUpForExpense.getSelectedData().getAccountKey())) {
                    checkItem.setCrmAccount(crmAccountLookUp.getSelectedItem());
                }
                checkItem.setClient(clientLookUp.getSelectedItem());
                checkItemsList.add(checkItem);

                itemsTotalAmount = itemsTotalAmount.add(checkItem.getAmount());
            }
        }

        if (itemsTotalAmount.setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(bankCheckData.getAmount().setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP)) != 0) {
            WfmWindow.alert(accountingMessages.amountShouldBeEqualToItemsTotalAmount());
            return;
        }

        bankCheckData.setItems(checkItemsList.toArray(new BankCheckItem[checkItemsList.size()]));

        AccountingService.App.get().saveBankCheckData(bankCheckData, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.error(accountingMessages.errorOccuredWhileSavingCheck());
            }

            @Override
            public void onSuccess(Void result) {
                WfmWindow.info(accountingMessages.checkSavedSuccessfully());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_CHECK_SAVED, result, CheckAddEditView.this);
                closeTab();
                if (!closeTab) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("check|add/add");
                }
            }
        });
    }


    private void clearDatePicker() {
        datePicker.clearSelected();
        Info.warn(accountingStrings.youCannotSelectPastDate());
    }

    private boolean validate() {
        int errors = 0;
        if (datePicker.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(datePicker.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Check", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateLookUpRequired(bankAccountLookUp)) {
            errors++;
        }
        if (!checkToBePrintCheckBox.getValue() && !Validation.validateTextBoxRequired(numberWidget.getTxtNumber())) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(payToTxtBox)) {
            errors++;
        }
        if (!Validation.validateDate(datePicker)) {
            errors++;
        }
        if (!Validation.validateTextBoxRequired(amountTxtBox)) {
            errors++;
        }
        if (!validateItems()) {
            errors++;
        }

        if (isPostDatedFeatureEnabled) {
            if (isDatePosted()) {
                clearDatePicker();
                return false;
            }
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validateItems() {
        int errors = 0;
        boolean onlyAccount;
        boolean accountPayableNot;
        boolean isValid = true;
        bankCheckItemsTable.setValidRows(0);
        EditableGrid editableGrid = bankCheckItemsTable.getGrid();
        for (int rowID = 0; rowID < editableGrid.getRowCount(); rowID++) {
            bankCheckItemsTable.resetValidation(rowID);
            onlyAccount = true;
            accountPayableNot = false;

            AccountLookUpForExpense account = (AccountLookUpForExpense) bankCheckItemsTable.getColumnById(rowID, "account");
            ExtendedTextBox amountTxtBox = (ExtendedTextBox) bankCheckItemsTable.getColumnById(rowID, "amount");
            LookUpCell lookUpCell = (LookUpCell) bankCheckItemsTable.getColumnCellWidgetById(rowID, "crmaccount");
            if (LookUp.wfmStrings.searchTypeMessage().equals(account.getTextBox().getText().trim())) {
                bankCheckItemsTable.setColumnValid("account");
                errors++;
            }

            if (!validateCalculatable(amountTxtBox.getText())) {
                bankCheckItemsTable.setColumnValid("amount");
                errors++;
            }

            if (account.getSelectedData() != null &&
                    /*(*/ACCOUNTS_PAYABLE_KEY.equals(account.getSelectedData().getAccountKey()) /*|| (ACCOUNTS_RECEIVABLE_KEY.equals(account.getSelectedData().getAccountKey())))*/ &&
                    (lookUpCell == null || lookUpCell.getLookUp().getSelectedItem() == null)) {
                accountPayableNot = true;
                errors++;
            }

            if (errors > 0) {
                if (errors == bankCheckItemsTable.getRequiredFieldCount()) {
                    bankCheckItemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (errors == bankCheckItemsTable.getRequiredFieldCount() - 1 && onlyAccount) {
                    bankCheckItemsTable.setItemValid(rowID, false);
                    errors = 0;
                } else if (bankCheckItemsTable.validateFields(rowID)) {
                    bankCheckItemsTable.setItemValid(rowID, true);
                    bankCheckItemsTable.incValidRow();
                    errors = 0;
                } else {
                    bankCheckItemsTable.setItemValid(rowID, false);
                    return false;
                }
            } else {
                bankCheckItemsTable.setItemValid(rowID, true);
                bankCheckItemsTable.incValidRow();
            }
            if (bankCheckItemsTable.getValidRows() == 0) {
                bankCheckItemsTable.notValid(rowID, "account");
                bankCheckItemsTable.notValid(rowID, "amount");
                if (accountPayableNot) {
                    bankCheckItemsTable.notValid(rowID, "crmaccount");
                }
                isValid = false;
            }
        }
        return isValid;
    }

    private boolean validateCalculatable(String text) {
        if (text == null || text.equals("")) {
            return false;
        } else
            return AccountingUtils.get().parseToBigDecimal(text).setScale(AccountingUtils.calculationScale, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) != 0;
    }

    private void onBankChange(BankAccountItem item) {
        currencyWidget.setEnabled(true);

        if (!isCurrencyRequirementsValid(item.getCurrency().getId())) {
            bankAccountLookUp.clear();

            if (checkData.getBankAccount() != null) {
                bankAccountLookUp.addBankAccountItem(checkData.getBankAccount());
            }
            Info.show(accountingMessages.theCurrencyOfTheManualEntryMust(), Info.Type.WARNING);
            return;
        }
        CurrencyItem baseCurrency = currencyWidget.getBaseCurrency();
        CurrencyItem bankCurrency = item.getCurrency() != null ? item.getCurrency() : baseCurrency;

        if (baseCurrency.getName().equals(bankCurrency.getName())) {

            if (availableCurrencies != null && availableCurrencies.length > 0) {
                currencyWidget.setCurrencies(availableCurrencies);
            }
            currencyWidget.setCurrency(baseCurrency.getId());
        } else {
            currencyWidget.setCurrencies(new CurrencyItem[]{baseCurrency, bankCurrency});
            currencyWidget.setCurrency(bankCurrency.getId());
        }
        setCurrencyToAccounts(bankCurrency.getId());
    }

    private void setCurrencyToAccounts(Integer currencyID) {
        EditableGrid grid = bankCheckItemsTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) bankCheckItemsTable.getColumnById(i, ACCOUNT_COLUMN);
            account.setCurrencyID(currencyID);
        }
    }

    private boolean isCurrencyRequirementsValid(Integer currencyID) {
        Set<Integer> currencyIDs = new HashSet<>();
        currencyIDs.add(currencyWidget.getBaseCurrency().getId());
        currencyIDs.add(currencyID);
        int allowedCurrencyLimit = currencyIDs.size();

        EditableGrid grid = bankCheckItemsTable.getGrid();
        for (int i = 0; i < grid.getRowCount(); i++) {
            AccountsLookUp account = (AccountsLookUp) bankCheckItemsTable.getColumnById(i, ACCOUNT_COLUMN);
            AccountItem selectedData = account.getSelectedData();
            if (selectedData != null && selectedData.getCurrencyID() != null) {
                currencyIDs.add(selectedData.getCurrencyID());
            }
        }

        return currencyIDs.size() <= allowedCurrencyLimit;
    }

    private void setEnabledButtons(boolean b) {
        if (saveAndCloseButton != null) {
            saveAndCloseButton.setEnabled(b);
        }
        if (saveAndNewButton != null) {
            saveAndNewButton.setEnabled(b);
        }
    }

    private void calculate() {
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < bankCheckItemsTable.getRowCount(); i++) {
            ExtendedTextBox amount = (ExtendedTextBox) bankCheckItemsTable.getColumnById(i, "amount");
            total = total.add(AccountingUtils.get().parseToBigDecimal(amount.getText()).setScale(AccountingUtils.customUnitPriceScale, RoundingMode.HALF_UP));
        }

        amountTxtBox.setValue(AccountingUtils.get().formatPrice(total));
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    public class ExtendedTextBox extends TextBox implements CustomCellInterface {
        public String itemQbId;

        public ExtendedTextBox() {
            super();
        }

        @Override
        public String getDisplayValue() {
            return getText();
        }

        @Override
        public void setItemValue(Object value) {
            setText(String.valueOf(value));
        }

        @Override
        public void setItemFocus(boolean focused) {
            setFocus(focused);
        }

        public String getItemQbId() {
            return itemQbId;
        }

        public void setItemQbId(String itemQbId) {
            this.itemQbId = itemQbId;
        }
    }

    @Override
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
