package com.edatasite.workforce.gwt.invoice.client.ui.view.payment;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.InvoicePaymentRequestObject;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderData;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.ui.view.AccountsReceivablePayableLookUp;
import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.AccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.DepartmentLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.InvoiceQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notesPanel.NoteHistoryWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.footerFileUpload.FooterUploadPanel;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceFormFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartAccountLookUpForExpense;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartCrmAccountLookup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PREPAYMENT;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRE_PAYMENT_OPEN_STATUS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.INVOICE_PAYMENT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RENTAL_ORDERS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALE_INVOICE;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.PURCHASE_ACCOUNT;
import static com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants.SALES_ACCOUNT;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 10/27/11
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEditPrepaymentView extends CustomForm2 implements Colapse, FittedContent {

    public static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final String RELATED_INVOICE = "relatedInvoice";
    private static final String NO_FEE = "No Fee";
    private static final String FIXED_AMOUNT = "Fixed Amount";
    private static final String PERCENTAGE = "Percentage";
    private static final PrepaymentServiceAsync prepaymentService = PrepaymentService.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private FooterUploadPanel footerUploadPanel;
    private CrmAccountLookUp crmAccountLookUp;
    private ProjectLookUp relatedProject;
    private DepartmentLookUp departmentLookUp;
    private AccountsLookUp accountsLookUp;
    private PaymentAccountsLookUp payAccountLookUp;
    private InvoiceQuoteLookUp quoteLookUp;
    private InvoiceQuoteLookUp invoiceLookUp;
    //private PurchaseOrderLookUp purchaseOrderLookUp;
    private NoteHistoryWidget noteHistoryWidget;

    private DatePicker date;
    private TextBox prepaymentNumber;
    private TextBox reference;
    private KpiSwitcher postDated;
    private TextBox prePaymentAmount;
    private SmartAccountLookUpForExpense bankFee;
    private DataListBox type;
    private TextBox value;
    private FormGroup prePaymentAmountWidget, currencyWidgetParent, valueWidget, feeTypeWidget;
    FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private MaterialSplitButton splitButton;


    //    private HTML currencyLabel;
    private CurrencyWidget currencyWidget;
    private Params externalParams;
    private Integer objectID;
    private final boolean isReceivable;
    private String paymentStatus;
    private PrePaymentData localData;
    private WfmButton2 save;
    private MaterialLink customerBalanceLink;
    private boolean isCashRefund = false;
    private boolean isCopy = false;

    private Integer quoteId;
    private Integer invocieId;
    private NewInvoice invoice;
    private Integer rentalOrderId;
    // when opened from a customer/supplier summary tab, this account is preselected and cannot be changed
    private Integer summaryCrmAccountId;


    public AddEditPrepaymentView(boolean isReceivable, String[] params) {
        super((isReceivable ? "prepayment" : "supplierCredit"), (isReceivable ? Property.get(Constants.CUSTOMER_PREPAYMENT, wfmStrings.addMess(), accountingStrings.customerPrepayment()) : Property.get(Constants.SUPPLIER_LIST, accountingStrings.addSupplierCredit(), wfmStrings.supplier())));
        this.isReceivable = isReceivable;
        initParams(params);
    }


    public AddEditPrepaymentView(Integer objectID, boolean isReceivable, String[] params) {
        super("edit", wfmStrings.edit());
        this.objectID = objectID;
        this.isReceivable = isReceivable;
        if (params != null && params[0] != null && params[1] != null && params.length == 2 && "copyFromOrder".equals(params[0])) {
            this.quoteId = Integer.valueOf(params[1]);
        }
        if (params != null && params[0] != null && params[1] != null && params.length == 2 && "addFromRentalOrder".equals(params[0])) {
            this.rentalOrderId = Integer.valueOf(params[1]);
        }
        if (params != null && params[0] != null && params[1] != null && params.length == 2 && "prePaymentFromSI".equals(params[0])) {
            this.invocieId = Integer.valueOf(params[1]);
        }
        if (params != null && params.length == 2) {
            this.isCashRefund = (params[1] != null && "cashRefund".equals(params[1]));
        }
        if (params != null && params.length > 1 && params[1] != null && params[1].equals("copy")) {
            this.isCopy = true;
        }
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(isReceivable ? ViewName.Prepayment : ViewName.Supplier, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {

            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formPropertyMap = result.getFormPropertyMap();
                    AddEditPrepaymentView.super.onInitialize();
                    initialize();
                }
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initialize() {
        footerUploadPanel = new FooterUploadPanel(Constants.F_PREPAYMENT, objectID);

        boolean hasPermissonCustomerQuick = Utils.hasPermission(ACCOUNTING_CUSTOMER_QUICK_ADD);
        boolean hasPermissonCustomerAdd = Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD);

        boolean hasPermissonSupplierQuick = Utils.hasPermission(ACCOUNTING_SUPPLIER_QUICK_ADD);
        boolean hasPermissonSupplierAdd = Utils.hasPermission(ACCOUNTING_SUPPLIER_ADD);
        crmAccountLookUp = new SmartCrmAccountLookup(isReceivable ? CrmAccountLookUp.CUSTOMER : CrmAccountLookUp.SUPPLIER, true, () -> {
            if (isReceivable) {
                if (hasPermissonCustomerQuick) {
                    new CusSuppQuickAddView(CrmAccountLookUp.CUSTOMER, crmAccountLookUp.getLastValueBeforeClick());
                } else if (hasPermissonCustomerAdd) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("client|add/add");
                }
            } else {
                if (hasPermissonSupplierQuick) {
                    new CusSuppQuickAddView(CrmAccountLookUp.SUPPLIER, crmAccountLookUp.getLastValueBeforeClick());
                } else if (hasPermissonSupplierAdd) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("supplier|add/add");
                }
            }
        }, false, isReceivable ? hasPermissonCustomerQuick || hasPermissonCustomerAdd : hasPermissonSupplierQuick || hasPermissonSupplierAdd);
        crmAccountLookUp.ensureDebugId("prepayment_customer_look");
        crmAccountLookUp.setAutocompleteOff();
        crmAccountLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            onChangeLookUp();
        });
        crmAccountLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        addFormListeners();

        relatedProject = new ProjectLookUp(null, isReceivable ? crmAccountLookUp : null);
        String prepaymentFormStrings = "prepayment_form_";
        relatedProject.ensureDebugId(prepaymentFormStrings + "relatedProject");
        relatedProject.setEnsureDebugId(prepaymentFormStrings + "relatedProject");
        relatedProject.setAutocompleteOff();
        relatedProject.getSuggestBox().addStyleName(DEFAULT_WIDTH);

        departmentLookUp = new DepartmentLookUp();
        departmentLookUp.ensureDebugId(prepaymentFormStrings + "department");
        departmentLookUp.setAutocompleteOff();
        departmentLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);

        if (Utils.isEnableBonnardCustomization()) {
            accountsLookUp = new AccountsLookUp(PREPAYMENT);
        } else {
            accountsLookUp = new AccountsReceivablePayableLookUp(isReceivable ? Constants.RECEIVABLE : Constants.PAYABLE, true);
        }

        accountsLookUp.ensureDebugId(prepaymentFormStrings + "receivablePayable");
        accountsLookUp.setAutocompleteOff();
        accountsLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        payAccountLookUp = new PaymentAccountsLookUp(true);
        payAccountLookUp.setAutocompleteOff();
        payAccountLookUp.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        payAccountLookUp.ensureDebugId(prepaymentFormStrings + "payAccountLookUp");
        bankFee = new SmartAccountLookUpForExpense("all");
        bankFee.ensureDebugId(prepaymentFormStrings + "bankFee");
        bankFee.setAutocompleteOff();
        bankFee.getSuggestBox().addStyleName(DEFAULT_WIDTH);

        customerBalanceLink = new MaterialLink(AccountingUtils.get().formatPrice(BigDecimal.ZERO));
        customerBalanceLink.setHref("javaScript:void(0)");
        customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "none");

        quoteLookUp = new InvoiceQuoteLookUp(isReceivable ? Constants.SALES_QUOTE_RENTAL_ORDER : Constants.PURCHASE_ORDER, crmAccountLookUp);
        quoteLookUp.ensureDebugId("addedit_prepayment_view_quoteAndRentalLookUp");
        quoteLookUp.addStyleName(DEFAULT_WIDTH);
        quoteLookUp.setAutocompleteOff();
        quoteLookUp.setEnabled(false);

        invoiceLookUp = new InvoiceQuoteLookUp(SALE_INVOICE, crmAccountLookUp);
        invoiceLookUp.ensureDebugId("addedit_prepayment_view_invoiceLookUp");
        invoiceLookUp.addStyleName(DEFAULT_WIDTH);
        invoiceLookUp.setAutocompleteOff();
        invoiceLookUp.setEnabled(false);

        /*if(isReceivable) {
            saleQuoteLookUp = new InvoiceQuoteLookUp(Constants.SALE_QUOTE, crmAccountLookUp);
            saleQuoteLookUp.ensureDebugId("addedit_prepayment_view_saleQuoteLookUp");
            saleQuoteLookUp.addStyleName(DEFAULT_WIDTH);
            saleQuoteLookUp.setEnabled(false);
        } else {
            purchaseOrderLookUp = new PurchaseOrderLookUp();
            purchaseOrderLookUp.ensureDebugId("addedit_prepayment_purchaseOrderLookUp");
            purchaseOrderLookUp.addStyleName(DEFAULT_WIDTH);
            purchaseOrderLookUp.setEnabled(false);
        }*/
        noteHistoryWidget = new NoteHistoryWidget(callback -> {
            if (objectID == null) {
                return;
            }
            prepaymentService.getPaymentHistoryNotes(objectID, INVOICE_PAYMENT, callback);
        });

//        currencyLabel = new HTML();

        prePaymentAmount = new TextBox(true);
        prePaymentAmount.ensureDebugId(prepaymentFormStrings + "amount-textBox");
        Validation.addNumericKeyboardListener(prePaymentAmount, AccountingUtils.calculationScale);
        String title = wfmStrings.amount();
        if(formPropertyMap !=null && formPropertyMap.get(PREPAYMENT_AMOUNT) != null) {
            title = getTitle(formPropertyMap.get(PREPAYMENT_AMOUNT).isChanged() ? formPropertyMap.get(PREPAYMENT_AMOUNT).getTitle() : wfmStrings.amount() , formPropertyMap.get(PREPAYMENT_AMOUNT).isRequired());
        }
        prePaymentAmountWidget = new FormGroup(title, prePaymentAmount);
        prePaymentAmountWidget.setWidth("100%");
        prePaymentAmountWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        prePaymentAmount.addChangeHandler(c -> {
            prePaymentAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(prePaymentAmount.getText())));
        });
        //        prePaymentAmount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
//        AdvancedInputGroup prepaymentAmountPanel = new AdvancedInputGroup(prePaymentAmount, currencyLabel);

        type = new DataListBox();
        type.ensureDebugId(prepaymentFormStrings + "type");
        type.setWithoutNullLabel(true);
        type.setItems(new SelectItem[]{
                new SelectItem(0, NO_FEE),
                new SelectItem(1, FIXED_AMOUNT),
                new SelectItem(2, PERCENTAGE)
        });
        type.setSelected(0);
        type.addValueChangeHandler(changeEvent -> {
            updateValueFieldLabel();
            applyNoFeeState();
        });

        value = new TextBox(true);
        value.ensureDebugId(prepaymentFormStrings + "value");
        value.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(value, AccountingUtils.calculationScale);
        value.addChangeHandler(c -> value.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(value.getText()))));
        valueWidget = new FormGroup();
        valueWidget.setWidth("100%");
        valueWidget.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        valueWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);
        updateValueFieldLabel();
        valueWidget.setContent(value);

        boolean feeTypeRequired = formPropertyMap != null && formPropertyMap.get(FEE_TYPE) != null && formPropertyMap.get(FEE_TYPE).isRequired();
        feeTypeWidget = new FormGroup(getTitle(getFeeTypeTitle(), feeTypeRequired), type);
        feeTypeWidget.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        reference = new TextBox(true);
        reference.ensureDebugId(prepaymentFormStrings + "reference");
        reference.addStyleName(DEFAULT_WIDTH);

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addListener(this::onCurrencyChange);
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.ensureDebugId("prepayment_form-currency");
        currencyWidgetParent = new FormGroup(wfmStrings.currency(), currencyWidget);
        currencyWidgetParent.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        currencyWidgetParent.getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        date = new DatePicker(new Date(), true);
        date.ensureDebugId(prepaymentFormStrings + "date");
        date.addStyleName(DEFAULT_WIDTH);
        currencyWidget.setDatePicker(date);

        prepaymentNumber = new TextBox(true);
        prepaymentNumber.ensureDebugId(prepaymentFormStrings + "prepaymentNumber");
        prepaymentNumber.addStyleName(DEFAULT_WIDTH);

        postDated = new KpiSwitcher();
        postDated.ensureDebugId(prepaymentFormStrings + "postDated");

        if (objectID != null) {
            crmAccountLookUp.setEnabled(false);
            payAccountLookUp.setEnabled(false);
            postDated.setEnabled(false);
        } else {
            paymentStatus = PRE_PAYMENT_OPEN_STATUS;
            date.addChangeHandler(changeEvent -> {
                if (isPostDated()) {
                    clearDatePicker();
                }
            });

            postDated.addValueChangeHandler(valueChangeEvent -> {
                if (isPostDated()) {
                    clearDatePicker();
                }
            });

        }
        FormGroup clientField = new FormGroup(crmAccountLookUp);
        clientField.ensureDebugId(isReceivable ? InvoiceFormFields.CUSTOMER : InvoiceFormFields.SUPPLIER);

        Div clientFieldLabel = clientField.getGroupLabel();
        clientFieldLabel.addStyleName("label-group");

        clientFieldLabel.add(new Span(getTitle(formPropertyMap.get(CRM_ACCOUNT_LOOKUP).isChanged() ? formPropertyMap.get(CRM_ACCOUNT_LOOKUP).getTitle() : isReceivable ? wfmStrings.customer() : wfmStrings.supplier())));

        Span balance = new Span(wfmStrings.balance() + ": ");
        balance.add(customerBalanceLink);
        clientFieldLabel.add(balance);

        getCustomFieldUtil().drawCustomFields(this, objectID);

        addTitleField(PREPAYMENT_FORM_TITLE, (isReceivable ? Property.get(Constants.CUSTOMER_PREPAYMENT, wfmStrings.addMess(), accountingStrings.customerPrepayment()) : Property.get(Constants.SUPPLIER_LIST, accountingStrings.addSupplierCredit(), wfmStrings.supplier())));
        if (formPropertyMap != null && formPropertyMap.get(CRM_ACCOUNT_LOOKUP) != null) {
            addField(CRM_ACCOUNT_LOOKUP, clientField, null);
            crmAccountLookUp.setEnabled(!formPropertyMap.get(CRM_ACCOUNT_LOOKUP).isDisabled());
        } else {
            addField(CRM_ACCOUNT_LOOKUP, clientField,null);
        }
        if (summaryCrmAccountId != null) {
            crmAccountLookUp.setEnabled(false);
        }
        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP) != null) {
            addField(PAYMENT_ACCOUNT_LOOKUP, payAccountLookUp, getTitle(formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).isChanged() ? formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).getTitle() : isReceivable ? wfmStrings.paidTo() : wfmStrings.paidFrom(), formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).isRequired()));
            payAccountLookUp.setEnabled(!formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).isDisabled());
        } else {
            addField(PAYMENT_ACCOUNT_LOOKUP, payAccountLookUp, getTitle(isReceivable ? wfmStrings.paidTo() : wfmStrings.paidFrom(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null) {
            addField(REFERENCE, reference, getTitle(formPropertyMap.get(REFERENCE).isChanged() ? formPropertyMap.get(REFERENCE).getTitle() : wfmStrings.reference(), formPropertyMap.get(REFERENCE).isRequired()));
            reference.setEnabled(!formPropertyMap.get(REFERENCE).isDisabled());
        } else {
            addField(REFERENCE, reference, wfmStrings.reference());
        }
        if (formPropertyMap != null && formPropertyMap.get(DATE_FIELD) != null) {
            addField(DATE_FIELD, date, getTitle(formPropertyMap.get(DATE_FIELD).isChanged() ? formPropertyMap.get(DATE_FIELD).getTitle() : wfmStrings.date(), formPropertyMap.get(DATE_FIELD).isRequired()));
            date.setEnabled(!formPropertyMap.get(DATE_FIELD).isDisabled());
        } else {
            addField(DATE_FIELD, date, wfmStrings.date());
        }
        if (formPropertyMap != null && formPropertyMap.get(POST_DATE) != null) {
            addField(POST_DATE, postDated, getTitle(formPropertyMap.get(POST_DATE).isChanged() ? formPropertyMap.get(POST_DATE).getTitle() : wfmStrings.postDated(), formPropertyMap.get(POST_DATE).isRequired()));
            postDated.setEnabled(!formPropertyMap.get(POST_DATE).isDisabled());
        } else {
            addField(POST_DATE, postDated, wfmStrings.postDated());
        }
        if (formPropertyMap != null && formPropertyMap.get(DEPARTMENT) != null) {
            addField(DEPARTMENT, departmentLookUp, getTitle(formPropertyMap.get(DEPARTMENT).isChanged() ? formPropertyMap.get(DEPARTMENT).getTitle() : wfmStrings.department(), formPropertyMap.get(DEPARTMENT).isRequired()));
            departmentLookUp.setEnabled(!formPropertyMap.get(DEPARTMENT).isDisabled());
        } else {
            addField(DEPARTMENT, departmentLookUp, wfmStrings.department());
        }
        if (formPropertyMap != null && formPropertyMap.get(PREPAYMENT_NUMBER) != null) {
            addField(PREPAYMENT_NUMBER, prepaymentNumber, getTitle(formPropertyMap.get(PREPAYMENT_NUMBER).isChanged() ? formPropertyMap.get(PREPAYMENT_NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(PREPAYMENT_NUMBER).isRequired()));
            prepaymentNumber.setEnabled(!formPropertyMap.get(PREPAYMENT_NUMBER).isDisabled());
        } else {
            addField(PREPAYMENT_NUMBER, prepaymentNumber, wfmStrings.number());
        }
        if (Utils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            if (formPropertyMap != null && formPropertyMap.get(PROJECT_) != null) {
                addField(PROJECT_, relatedProject, getTitle(formPropertyMap.get(PROJECT_).isChanged() ? formPropertyMap.get(PROJECT_).getTitle() : wfmStrings.project(), formPropertyMap.get(PROJECT_).isRequired()));
                relatedProject.setEnabled(!formPropertyMap.get(PROJECT_).isDisabled());
            } else {
                addField(PROJECT_, relatedProject, wfmStrings.project());
            }
        }
        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_INVOICE_LIST)) {
            if (formPropertyMap != null && formPropertyMap.get(SALE_INVOICE_LOOKUP) != null) {
                addField(SALE_INVOICE_LOOKUP, invoiceLookUp, getTitle(formPropertyMap.get(SALE_INVOICE_LOOKUP).isChanged() ? formPropertyMap.get(SALE_INVOICE_LOOKUP).getTitle() : wfmStrings.salesInvoice(), formPropertyMap.get(SALE_INVOICE_LOOKUP).isRequired()));
                invoiceLookUp.setEnabled(!formPropertyMap.get(SALE_INVOICE_LOOKUP).isDisabled());
            } else {
                String invoiceString = Property.get(SALE_INVOICE, wfmStrings.salesInvoice());
                addField(SALE_INVOICE_LOOKUP, invoiceLookUp, invoiceString);
            }
        }
        if (isReceivable && (Utils.hasPermission(PermissionConstants.ACCOUNTING_SALES_QUOTE_LIST) || Utils.hasPermission(PermissionConstants.ACCOUNTING_RENTAL_ORDER_LIST))) {
            if (formPropertyMap != null && formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP) != null) {
                addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, getTitle(formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isChanged() ? formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).getTitle() : wfmStrings.salesQuote(), formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isRequired()));
                quoteLookUp.setEnabled(!formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isDisabled());
            } else {
                addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, wfmStrings.salesQuote());
            }
//            String quoteString = Property.get(SALE_QUOTE, accountingStrings.salesQuote());
//            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, quoteString);
        } else if (Utils.hasPermission(PermissionConstants.ACCOUNTING_PURCHASE_ORDER_LIST)) {
            if (formPropertyMap != null && formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP) != null) {
                addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, getTitle(formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isChanged() ? formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).getTitle() : wfmStrings.purchaseorder(), formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isRequired()));
                quoteLookUp.setEnabled(!formPropertyMap.get(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).isDisabled());
            } else {
                addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, wfmStrings.salesQuote());
            }
//            String orderString = Property.get(PURCHASE_ORDER, accountingStrings.purchaseOrder());
//            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, orderString);
        }
        if (Utils.isEnableBonnardCustomization()) {
            if (formPropertyMap != null && formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE) != null) {
                addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, getTitle(formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isChanged() ? formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).getTitle() : wfmStrings.account(), formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isRequired()));
                accountsLookUp.setEnabled(!formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isDisabled());
            } else {
                addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, wfmStrings.account());
            }
//            addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, wfmStrings.account());
        } else {
            if (formPropertyMap != null && formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE) != null) {
                addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, getTitle(formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isChanged() ? formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).getTitle() : isReceivable ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable(), formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isRequired()));
                accountsLookUp.setEnabled(!formPropertyMap.get(ACCOUNTS_RECEIVABLE_PAYABLE).isDisabled());
            } else {
                addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, wfmStrings.account());
            }
//            addField(ACCOUNTS_RECEIVABLE_PAYABLE, accountsLookUp, getTitle(isReceivable ? wfmStrings.accountsReceivable() : wfmStrings.accountsPayable()));
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            if (formPropertyMap != null && formPropertyMap.get(PREPAYMENT_AMOUNT) != null) {
                addField(PREPAYMENT_AMOUNT, new InputGroup(prePaymentAmountWidget, currencyWidgetParent), null, formPropertyMap.get(PREPAYMENT_AMOUNT).isRequired());
                prePaymentAmountWidget.setEnabled(!formPropertyMap.get(PREPAYMENT_AMOUNT).isDisabled());
            } else {
//                addField(PREPAYMENT_AMOUNT, new InputGroup(prePaymentAmountWidget, currencyWidgetParent), null);
                addField(PREPAYMENT_AMOUNT, new InputGroup(prePaymentAmountWidget, currencyWidgetParent), null);
            }
        } else {
            if (formPropertyMap != null && formPropertyMap.get(PREPAYMENT_AMOUNT) != null) {
                addField(PREPAYMENT_AMOUNT, prePaymentAmountWidget, null, formPropertyMap.get(PREPAYMENT_AMOUNT).isRequired());
                prePaymentAmountWidget.setEnabled(!formPropertyMap.get(PREPAYMENT_AMOUNT).isDisabled());
            } else {
                addField(PREPAYMENT_AMOUNT, prePaymentAmountWidget, null);
            }
        }
        addField("BANK_FEE_ACCOUNT", bankFee, accountingStrings.bankFee());
        if (formPropertyMap != null && formPropertyMap.get(FEE_TYPE) != null) {
            type.setEnabled(!formPropertyMap.get(FEE_TYPE).isDisabled());
        }
        if (formPropertyMap != null && formPropertyMap.get(AMOUNT_PERCENTAGE) != null) {
            addField(AMOUNT_PERCENTAGE, new InputGroup(feeTypeWidget, valueWidget), null, formPropertyMap.get(AMOUNT_PERCENTAGE).isRequired());
            valueWidget.setEnabled(!formPropertyMap.get(AMOUNT_PERCENTAGE).isDisabled());
        } else {
            addField(AMOUNT_PERCENTAGE, new InputGroup(feeTypeWidget, valueWidget), null);
        }
        /*if(isReceivable) {
            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, quoteLookUp, accountingStrings.salesQuote());
        } else {
            addField(SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP, purchaseOrderLookUp, accountingStrings.purchaseOrder());
        }*/
        show();
        applyNoFeeState();

    }

    private void onChangeLookUp() {
        if (isReceivable) {
            relatedProject.clear();
        }
        accountsLookUp.clear();
        quoteLookUp.clear();
        quoteLookUp.clearOracleItems();

        invoiceLookUp.clear();
        invoiceLookUp.clearOracleItems();
//            quoteLookUp.clearAndClearItems();

        if (crmAccountLookUp.getSelectedItemID() != null) {
            quoteLookUp.setEnabled(true);
            invoiceLookUp.setEnabled(true);
        }
        loadPrePaymentData(objectID, false);
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, AddEditPrepaymentView.this, (sender, args) -> setSupplierClientData((Integer) args, false, Constants.RECEIVABLE));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, AddEditPrepaymentView.this, (sender, args) -> setSupplierClientData((Integer) args, true, Constants.RECEIVABLE));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_EDIT, AddEditPrepaymentView.this, (sender, args) -> setSupplierClientData((Integer) args, false, Constants.PAYABLE));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_ADD, AddEditPrepaymentView.this, (sender, args) -> setSupplierClientData((Integer) args, true, Constants.PAYABLE));
    }

    private void setSupplierClientData(Integer args, boolean b, String type) {
        InvoiceService.App.get().getClientOrSupplier(args, type, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(TypeItem typeItem) {
                crmAccountLookUp.setSelected(typeItem);
                onChangeLookUp();
                if (currencyWidget != null && typeItem.getCurrencyID() != null) {
                    currencyWidget.setCurrency(typeItem.getCurrencyID());
                }
            }
        });
    }

    private void initParams(String[] params) {
        externalParams = new Params();
        if (params != null && params.length > 2) {
            if (params[1] != null && RELATED_INVOICE.equals(params[1]) && params[2] != null && !"".equals(params[2])) {
                externalParams.setRelatedInvoiceId(Integer.parseInt(params[2]));
            }
            if ("account".equals(params[1]) && params[2] != null && !"".equals(params[2])) {
                this.summaryCrmAccountId = Integer.valueOf(params[2]);
            }
        }
    }


    private boolean isPostDated() {
        Date _today = new Date();
        Date today = new Date(_today.getYear(), _today.getMonth(), _today.getDate());
        Date _pick = date.getDate();
        if (_pick != null) {
            Date datePickerDay = new Date(_pick.getYear(), _pick.getMonth(), _pick.getDate());
            return postDated.getValue() && datePickerDay.before(today);
        } else {
            return false;
        }
    }


    private void clearDatePicker() {
        date.clearSelected();
        Info.warn(accountingStrings.youCannotSelectPastDate());
    }


    private void pdfVersion() {
        new PDFTemplateSelector(isReceivable ? AccountingConstants.PREPAYMENT : AccountingConstants.SUPPLIER_CREDIT, new ExtendedCommand() {
            @Override
            public void execute(Integer pdfTemplateID) {
                generatePDF(pdfTemplateID, objectID);
            }
        });
    }

    protected void generatePDF(Integer pdfTemplateID, Integer objectId) {
        InvoicePaymentRequestObject requestObject = new InvoicePaymentRequestObject(objectId, pdfTemplateID);
        String pdfURL = CommandConstants.PDF_URL + "/invoicePaymentViewPDFHandler";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        parametrs.put("isPrePayment", "true");
        parametrs.put("isReceivable", isReceivable ? "true" : "false");
        parametrs.put("isSupplierCredit", isReceivable ? "false" : "true");
        parametrs.put("isCustomerPrepayment", isReceivable ? "true" : "false");
        if (pdfTemplateID != null) {
            parametrs.put("templateID", String.valueOf(pdfTemplateID));
        }
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    @Override
    protected void addButtons() {
        List<Widget> result = new ArrayList<>();
        FooterInformer informer = new FooterInformer(SvgEnum.messageSquare, wfmStrings.historyAndNotes(), noteHistoryWidget);
        informer.setInitialClasses("informer-item history-notes-container");

        footerUploadPanel.setInitialClasses("informer-item history-notes-container");

        footer.addToLeftSide(informer);
        footer.addToLeftSide(footerUploadPanel);

        WfmButton2 pdf = new WfmButton2(wfmStrings.pdfVersion(), BTN_DEFAULT_OUTLINE, clickEvent -> pdfVersion());
        if (objectID != null) {
            addButton(pdf);
        }

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save(true));
        addButton(save);
    }

    @Override
    protected void getDataToFillFields() {
        loadPrePaymentData(objectID, true);
        if (objectID == null) {
//            setDefaultValues();
            setDefaultValuesByFormProperty();
        }
        if (quoteId != null) {
            loadSOAction();
        } else if (invocieId != null) {
            loadSIAction();
        } else if (rentalOrderId != null) {
            loadROAction();
        } else if (summaryCrmAccountId != null) {
            setSupplierClientData(summaryCrmAccountId, false, isReceivable ? Constants.RECEIVABLE : Constants.PAYABLE);
        }
    }



    @Override
    protected String getFormID() {
        return isReceivable ? LayoutRPC.PREPAYMENT_FORM : LayoutRPC.SUPPLIER_CREDIT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void loadPrePaymentData(final Integer prePaymentID, final boolean loadFormData) {
        prepaymentService.getPrePaymentData(prePaymentID, crmAccountLookUp.getSelectedItemID(), isReceivable, isCopy, externalParams, new AsyncCallback<PrePaymentData>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(PrePaymentData prePaymentData) {
                localData = prePaymentData;
                setDataToWidgets(prePaymentData, loadFormData, prePaymentID);
                LoadingPanel.loading(false);
            }
        });
    }

    private void setDataToWidgets(PrePaymentData prePaymentData, boolean loadFormData, Integer prePaymentID) {

        if (prePaymentData.getReceivablePayable() != null) {
            accountsLookUp.addAccountItem(prePaymentData.getReceivablePayable());
        }
        if (loadFormData) {
            PaymentData paymentData = prePaymentData.getPaymentData();
            currencyWidget.setCurrency((prePaymentData.getCurrency() != null ? prePaymentData.getCurrency().getId() : null), paymentData != null ? paymentData.getExchangeRate() : null);
            onCurrencyChange();
            if (paymentData != null) {
                if (objectID != null) {
                    quoteLookUp.setEnabled(true);
                    invoiceLookUp.setEnabled(true);
                    /*if(isReceivable) {
                        quoteLookUp.setEnabled(true);
                    } else {
                        purchaseOrderLookUp.setEnabled(true);
                    }*/
                    crmAccountLookUp.addItem(paymentData.getCrmAccount());
                    payAccountLookUp.addItem(paymentData.getPaymentAccount());
                    date.setDate(paymentData.getDate().getNonConvertedDate());
                    reference.setText(paymentData.getReferenceNumber());
                    prePaymentAmount.setText(AccountingUtils.get().formatPrice(paymentData.getPaymentAmount()));
                    postDated.setValue(paymentData.isPostDatedTransaction());
                    paymentStatus = paymentData.getPaymentStatus();

                    if (paymentData.getProject() != null) {
                        relatedProject.addItem(paymentData.getProject());
                    }
                    if (paymentData.getDepartment() != null) {
                        departmentLookUp.addItem(paymentData.getDepartment());
                    }
                    if (paymentData.getReceivablePayable() != null) {
                        accountsLookUp.addAccountItem(paymentData.getReceivablePayable());
                        if (paymentData.getProject() != null) {
                            relatedProject.addItem(paymentData.getProject());
                        }
                        if (paymentData.getReceivablePayable() != null) {
                            accountsLookUp.addAccountItem(paymentData.getReceivablePayable());
                        }
                    }
                    if (paymentData.getSaleQuoteItem() != null) {
                        quoteLookUp.addItem(paymentData.getSaleQuoteItem());
                    } else if (paymentData.getPurchaseOrderItem() != null) {
                        quoteLookUp.addItem(paymentData.getPurchaseOrderItem());
//                                purchaseOrderLookUp.addItem(paymentData.getPurchaseOrderItem());
                    } else if (paymentData.getRentalOrderItem() != null) {
                        quoteLookUp.addItem(paymentData.getRentalOrderItem());
                    }
                    if (paymentData.getSaleInvoiceItem() != null) {
                        invoiceLookUp.addItem(paymentData.getSaleInvoiceItem());
                    }
                    if (paymentData.getBankFee() != null) {
                        bankFee.addAccountItem(paymentData.getBankFee());
                    }
                    if (paymentData.getBankFeeType() != null) {
                        type.setSelected(PERCENTAGE.equals(paymentData.getBankFeeType()) ? 2 : (FIXED_AMOUNT.equals(paymentData.getBankFeeType()) ? 1 : 0));
                    }
                    if (paymentData.getBankFeeValue() != null) {
                        value.setText(AccountingUtils.get().formatQty(paymentData.getBankFeeValue()));
                    }
                    applyNoFeeState();
                } else {
                    if (paymentData.getCrmAccount() != null) {
                        crmAccountLookUp.addItem(paymentData.getCrmAccount());
                    }
                    if (paymentData.getDate() != null) {
                        date.setDate(paymentData.getDate().getNonConvertedDate());
                    }
                    reference.setText(paymentData.getReferenceNumber() != null ? paymentData.getReferenceNumber() : "");
                }

                getCustomFieldUtil().fillCustomFieldsWithData(paymentData.getCustomFields());
            }
            if (!prePaymentData.isEnabledPostDatedTransaction()) {
                postDated.setEnabled(false);
            }
            if (!prePaymentData.isEnabledPostDatedTransaction()) {
                postDated.setEnabled(false);
            }

        } else {

            currencyWidget.setCurrency(prePaymentData.getCurrency().getId());
            onCurrencyChange();
        }

        if (prePaymentData.getSupplierCustomerBalance() != null) {
            if (prePaymentData.getSupplierCustomerBalance().compareTo(BigDecimal.ZERO) >= 0) {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText(AccountingUtils.get().formatPrice(prePaymentData.getSupplierCustomerBalance()));
            } else {
                customerBalanceLink.getElement().getStyle().setProperty("pointerEvents", "visible");
                customerBalanceLink.setText("(" + AccountingUtils.get().formatPrice(prePaymentData.getSupplierCustomerBalance().multiply(new BigDecimal(-1))) + ")");
            }
        }
        if (isReceivable) {
            customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER));
        } else {
            customerBalanceLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierBalance|supplierBalance/" + crmAccountLookUp.getSelectedItemID() + "/" + CrmAccountItem.SUPPLIER));
        }

        boolean isPaymentStatusOpen = PRE_PAYMENT_OPEN_STATUS.equals(paymentStatus);
        if (isPaymentStatusOpen) {
            crmAccountLookUp.setEnabled(true);
            payAccountLookUp.setEnabled(true);
            postDated.setEnabled(true);
        }
        if (summaryCrmAccountId != null) {
            // account is fixed when the form is opened from a customer/supplier summary tab
            crmAccountLookUp.setEnabled(false);
        }
        if (prePaymentData.getBankTransferNumberData() != null) {
            if (prePaymentID == null) {
                prepaymentNumber.setText(prePaymentData.getBankTransferNumberData().getTransferNumber());
            } else {
                prepaymentNumber.setText(prePaymentData.getBankTransferNumberData().getNumberString());
            }
        }
    }

    private void onCurrencyChange() {
//        if (isBankMultiCurrencyEnabled) {
//            currencyLabel.setText(currencyWidget.getCurrencyName());
//        }

        //payAccountLookUp.clear();
        payAccountLookUp.setCurrencyID(currencyWidget.getCurrencyID());
        accountsLookUp.setCurrencyID(currencyWidget.getCurrencyID());
        bankFee.setCurrencyID(currencyWidget.getCurrencyID());
    }

    private String getAmountPercentageTitle() {
        FormProperty amountPercentageProperty = formPropertyMap != null ? formPropertyMap.get(AMOUNT_PERCENTAGE) : null;
        if (amountPercentageProperty != null && amountPercentageProperty.isChanged()) {
            return amountPercentageProperty.getTitle();
        }
        return PERCENTAGE.equals(type.getSelectedItem(true) != null ? type.getSelectedItem(true).getName() : FIXED_AMOUNT)
                ? wfmStrings.percentage()
                : wfmStrings.feeAmount();
    }

    private String getFeeTypeTitle() {
        FormProperty feeTypeProperty = formPropertyMap != null ? formPropertyMap.get(FEE_TYPE) : null;
        return feeTypeProperty != null && feeTypeProperty.isChanged() ? feeTypeProperty.getTitle() : wfmStrings.type();
    }

    private void updateValueFieldLabel() {
        FormProperty amountPercentageProperty = formPropertyMap != null ? formPropertyMap.get(AMOUNT_PERCENTAGE) : null;
        valueWidget.setLabel(getTitle(getAmountPercentageTitle(), amountPercentageProperty != null && amountPercentageProperty.isRequired()));
    }

    private boolean isNoFeeSelected() {
        SelectItem selected = type.getSelectedItem(true);
        return selected == null || NO_FEE.equals(selected.getName());
    }

    private void applyNoFeeState() {
        boolean noFee = isNoFeeSelected();
        boolean bankFeeFormDisabled = formPropertyMap != null && formPropertyMap.get("BANK_FEE_ACCOUNT") != null && formPropertyMap.get("BANK_FEE_ACCOUNT").isDisabled();
        boolean amountFormDisabled = formPropertyMap != null && formPropertyMap.get(AMOUNT_PERCENTAGE) != null && formPropertyMap.get(AMOUNT_PERCENTAGE).isDisabled();
        bankFee.setEnabled(!noFee && !bankFeeFormDisabled);
        value.setEnabled(!noFee && !amountFormDisabled);
        if (noFee) {
            bankFee.clear();
            value.setText("");
        }
    }

    private Integer extractLastFourDigits(String number) {
        try {
            return Integer.parseInt(rightMostCharacters(number));
        } catch (Exception e) {
            Info.warn(accountingMessages.valueInNUmberFieldIsInvalid() + " || " + e.getMessage());
        }
        return null;
    }

    private String rightMostCharacters(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() <= 4) {
            return str;
        }
        if (localData.getBankTransferNumberData().getPrefix() != null && localData.getBankTransferNumberData().isWithDate()) {
            String prefix = localData.getBankTransferNumberData().getPrefix();
            return str.replace(prefix, "").substring(0, 4);
        }
        return str.substring(str.length() - 4);
    }

    public void save(boolean checkExistingReference) {
        enableButton(false);
        if (!validateForm()) {
            enableButton(true);
            return;
        }

        LoadingPanel.loading(true);

        final PaymentData prePayment = new PaymentData();
        prePayment.setAttachedFiles(footerUploadPanel.getAttachedFiles());
        prePayment.setObjectID(!isCopy ? objectID : null);
        prePayment.setCrmAccount(crmAccountLookUp.getSelectedItem());
        prePayment.setProject(relatedProject.getSelectedItem());
        prePayment.setDepartment(departmentLookUp.getSelectedItem());
        prePayment.setReceivablePayable(accountsLookUp.getSelectedData());
        boolean isNoFee = isNoFeeSelected();
        prePayment.setBankFee(isNoFee ? null : bankFee.getSelectedData());
        prePayment.setBankFeeType(isNoFee || type.getSelectedItem(true) == null ? null : type.getSelectedItem(true).getName());
        prePayment.setBankFeeValue(isNoFee || Utils.isNullOrEmpty(value.getText()) ? null : AccountingUtils.get().parseToBigDecimal(value.getText()));
        prePayment.setPaymentAccount(payAccountLookUp.getSelectedItem());
        prePayment.setHistoryList(noteHistoryWidget.getNotes().toArray(new HistoryListItem[]{}));

        if (isReceivable) {
            if (quoteLookUp.getSelectedItem() != null && RENTAL_ORDERS.equals(quoteLookUp.getSelectedItem().getDescription())) {
                prePayment.setRentalOrderItem(quoteLookUp.getSelectedItem());
            } else {
                prePayment.setSaleQuoteItem(quoteLookUp.getSelectedItem());
            }
        } else {
            prePayment.setPurchaseOrderItem(quoteLookUp.getSelectedItem());
        }
        prePayment.setSaleInvoiceItem(invoiceLookUp.getSelectedItem());

        prePayment.setDate(new DateNonConvertable(date.getDate()));
        prePayment.setReferenceNumber(reference.getText());
        prePayment.setPaymentAmount(AccountingUtils.get().parseToBigDecimal(prePaymentAmount.getText()));
        prePayment.setType(isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT : AccountingConstants.PAYABLE_SUPPLIER_CREDIT);
        prePayment.setNumber(prepaymentNumber.getValue());
        prePayment.setIntNumber(extractLastFourDigits(prepaymentNumber.getValue()));
        prePayment.setPaymentStatus(paymentStatus);
        if (postDated.getValue()) {
            if (date.getDate().after(DateUtil.getDayLastTime(new Date()))) {
                prePayment.setPostDatedTransaction(true);
            }
        }

        if (Utils.hasGenericAccess(GenericSettingsEnum.MULTICURRENCY_ENABLED)) {
            prePayment.setCurrency(currencyWidget.getCurrency());
            prePayment.setExchangeRate(currencyWidget.getExchangeRate());
        } else {
            prePayment.setCurrency(currencyWidget.getBaseCurrency());
            prePayment.setExchangeRate(AccountingConstants.ONE);
        }
        if (externalParams != null && externalParams.getRelatedInvoiceId() != null) {
            prePayment.setInvoiceID(externalParams.getRelatedInvoiceId());
        }
        prePayment.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());


        if (objectID == null) {
            ReceivePaymentData paymentData = new ReceivePaymentData();
            paymentData.setBatchPayment(true);
            paymentData.setDepartment(departmentLookUp.getSelectedItem());
            paymentData.setCrmAccount(crmAccountLookUp.getSelectedItem());
            paymentData.setAccount(payAccountLookUp.getSelectedItem());
            paymentData.setProject(relatedProject.getSelectedItem());
            paymentData.setExRate(currencyWidget.getExchangeRate());
            paymentData.setCurrency(new CurrencyItem(currencyWidget.getCurrencyID(), null, null));
            paymentData.setReference(reference.getText());
            paymentData.setDate(new DateNonConvertable(date.getDate()));
            paymentData.setTotalAmount(AccountingUtils.get().parseToBigDecimal(prePaymentAmount.getText()));
            paymentData.setValidateReferences(checkExistingReference);
            paymentData.setType(isReceivable ? RECEIVABLE : PAYABLE);
            paymentData.setIncludeSubAccountTransaction(false);

            if (footerUploadPanel != null && footerUploadPanel.getAttachedFiles() != null && footerUploadPanel.getAttachedFiles().length > 0) {
                paymentData.setAttachments(footerUploadPanel.getAttachedFiles());
            }
            paymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);

            ArrayList<PaymentData> payments = new ArrayList<>();
            payments.add(prePayment);
            paymentData.setPayments(payments.toArray(new PaymentData[1]));

            InvoiceService.App.get().saveReceivePaymentData(paymentData, isReceivable, new AbstractAsyncCallback<BatchPaymentResult>() {
                @Override
                public void failure(Throwable throwable) {
                    save.setEnabled(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void success(BatchPaymentResult result) {
                    save.setEnabled(true);
                    LoadingPanel.loading(false);
                    if (result.getResult() != null && result.getResult().equals(-1)) {
                        Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                        return;
                    }
                    if (result.getDuplicatedReferences() != null && result.getDuplicatedReferences().length > 0) {
                        StringBuilder references = new StringBuilder();
                        for (int i = 0; i < result.getDuplicatedReferences().length; i++) {
                            if (i != 0) {
                                references.append(", ");
                            }
                            references.append(result.getDuplicatedReferences()[i]);
                        }
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.setMessage(accountingMessages.referenceWithNumberExists(references.toString()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onSubmit() {
                                save(false);
                            }
                        });
                        messageBox.setWidth("300px");
                        messageBox.open();
                        return;
                    }

                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INVOICEPAYMENT_CHANGE, null, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, null, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, null, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, null, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BANK_TRANSFER_LIST_UPDATE, result, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, AddEditPrepaymentView.this);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, null, AddEditPrepaymentView.this);

                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.payment()), Info.Type.INFO);
                    closeTab();
                }
            });
        } else {
            prepaymentService.savePrePayment(prePayment, isCashRefund, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Integer result) {
                    enableButton(true);
                    LoadingPanel.loading(false);
                    if (result == -1) {
                        Info.show(accountingStrings.totalPrepaymentLessThanAmountAppliedErrorMessage(), Info.Type.WARNING);
                    } else if (AccountingConstants.NUMBER_EXISTS.equals(result)) {
                        String param1 = isReceivable ? accountingStrings.prepayment() : accountingStrings.supplierCredit();
                        Info.warn(accountingMessages.numberAlreadyExists(param1, prePayment.getNumber()));
                    } else {
                        Info.show((Utils.textFormat(wfmStrings.messSuccessfullySaved(), isReceivable ? accountingStrings.prepayment() : accountingStrings.supplierCredit())), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PREPAYMENT_SAVE, null, AddEditPrepaymentView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PAYMENT_TO_BANK_ACCOUNT, null, AddEditPrepaymentView.this);
                        closeTab();
                    }
                }
            });
        }
    }

    @Override
    protected void enableButton(boolean enable) {
        save.setEnabled(enable);
    }

    private boolean validateForm() {
        int errors = 0;
        if (date.getDate() != null && Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(date.getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(isReceivable ? accountingStrings.prepayment() : accountingStrings.supplierCredit(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }
        if (formPropertyMap != null && formPropertyMap.get(CRM_ACCOUNT_LOOKUP) != null && formPropertyMap.get(CRM_ACCOUNT_LOOKUP).isRequired()) {
            errors += markAsError(crmAccountLookUp, !Validation.validateLookUpRequired(crmAccountLookUp));
        }
        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP) != null && formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).isRequired()) {
            errors += markAsError(payAccountLookUp, !Validation.validateLookUpRequired(payAccountLookUp));
        }
        if (formPropertyMap != null && formPropertyMap.get(DATE_FIELD) != null && formPropertyMap.get(DATE_FIELD).isRequired()) {
            errors += markAsError(date, !Validation.validateDate(date));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_AMOUNT).isRequired()){
            errors += markAsError(prePaymentAmount, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PREPAYMENT_AMOUNT).isChanged() ?
                    formPropertyMap.get(PREPAYMENT_AMOUNT).getTitle() : accountingStrings.prepaymentAmount(), prePaymentAmount, formPropertyMap.get(PREPAYMENT_AMOUNT).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(FEE_TYPE) != null && formPropertyMap.get(FEE_TYPE).isRequired()) {
            errors += markAsError(type, !Validation.validateDataListBoxRequired(type));
        }
        if (formPropertyMap != null && formPropertyMap.get(AMOUNT_PERCENTAGE) != null && formPropertyMap.get(AMOUNT_PERCENTAGE).isRequired()) {
            errors += markAsError(value, !Validation.validateTextBoxRequiredAndCharLimit(getAmountPercentageTitle(), value, formPropertyMap.get(AMOUNT_PERCENTAGE).getMinChar()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null && formPropertyMap.get(CustomFormConstants.REFERENCE).isRequired()){
            errors += markAsError(reference, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(REFERENCE).isChanged() ?
                    formPropertyMap.get(REFERENCE).getTitle() : wfmStrings.reference(), reference, formPropertyMap.get(REFERENCE).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_NUMBER).isRequired()){
            errors += markAsError(prepaymentNumber, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(PREPAYMENT_NUMBER).isChanged() ?
                    formPropertyMap.get(PREPAYMENT_NUMBER).getTitle() : wfmStrings.number(), prepaymentNumber, formPropertyMap.get(PREPAYMENT_NUMBER).getMinChar()));
        }

        return errors <= 0;
    }

    @Override
    public String getIconStyle() {
        return null;
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
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void setDefaultValuesByFormProperty() {

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_LOOKUP) != null && formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_LOOKUP).getSelectedId() != null && crmAccountLookUp != null) {
            crmAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_LOOKUP).getSelectedId(), formPropertyMap.get(CustomFormConstants.CRM_ACCOUNT_LOOKUP).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP) != null && formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).getSelectedId() != null && payAccountLookUp != null) {
            payAccountLookUp.setSelected(new SelectItem(formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).getSelectedId(), formPropertyMap.get(PAYMENT_ACCOUNT_LOOKUP).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null && formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue() != null && reference != null) {
            reference.setText(formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DATE_FIELD) != null && formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue() != null && date != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                date.setDate(currentDate);
            } else {
                date.setDate(new Date(formPropertyMap.get(CustomFormConstants.DATE_FIELD).getDefaultValue()));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_AMOUNT).getDefaultValue() != null && prePaymentAmount != null) {
            prePaymentAmount.setText(formPropertyMap.get(CustomFormConstants.PREPAYMENT_AMOUNT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(FEE_TYPE) != null && type != null) {
            if (formPropertyMap.get(FEE_TYPE).getSelectedId() != null) {
                type.setSelected(formPropertyMap.get(FEE_TYPE).getSelectedId());
            } else if (formPropertyMap.get(FEE_TYPE).getDefaultValue() != null) {
                type.setSelectedByValue(formPropertyMap.get(FEE_TYPE).getDefaultValue(), true);
            }
            updateValueFieldLabel();
        }
        if (formPropertyMap != null && formPropertyMap.get(AMOUNT_PERCENTAGE) != null && formPropertyMap.get(AMOUNT_PERCENTAGE).getDefaultValue() != null && value != null) {
            value.setText(formPropertyMap.get(AMOUNT_PERCENTAGE).getDefaultValue());
        }
        applyNoFeeState();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE) != null && formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getSelectedId() != null && accountsLookUp != null) {
            accountsLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getSelectedId(), formPropertyMap.get(CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_NUMBER) != null && formPropertyMap.get(CustomFormConstants.PREPAYMENT_NUMBER).getSelectedId() != null && prepaymentNumber != null) {
            prepaymentNumber.setText(formPropertyMap.get(PURCHASE_ACCOUNT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP) != null && formPropertyMap.get(CustomFormConstants.SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP).getSelectedId() != null && quoteLookUp != null) {
            quoteLookUp.setSelected(new SelectItem(formPropertyMap.get(SALES_ACCOUNT).getSelectedId(), formPropertyMap.get(SALES_ACCOUNT).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PROJECT_) != null && formPropertyMap.get(CustomFormConstants.PROJECT_).getSelectedId() != null && relatedProject != null) {
            relatedProject.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PROJECT_).getSelectedId(), formPropertyMap.get(CustomFormConstants.PROJECT_).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SALE_INVOICE_LOOKUP) != null && formPropertyMap.get(CustomFormConstants.SALE_INVOICE_LOOKUP).getSelectedId() != null && invoiceLookUp != null) {
            invoiceLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.SALE_INVOICE_LOOKUP).getSelectedId(), formPropertyMap.get(CustomFormConstants.SALE_INVOICE_LOOKUP).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT) != null && formPropertyMap.get(CustomFormConstants.DEPARTMENT).getSelectedId() != null && departmentLookUp != null) {
            departmentLookUp.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.DEPARTMENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.DEPARTMENT).getDefaultValue()));
        }
    }

    private void loadSOAction() {
        LoadingPanel.loading(true);
        QuoteService.App.get().getQuote(quoteId, null, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(NewInvoice newInvoice) {
                LoadingPanel.loading(false);
                invoice = newInvoice;
                crmAccountLookUp.setSelected(invoice.getClientID(), invoice.getClientName());
                quoteLookUp.setSelected(quoteId, invoice.getInvoiceNumber() +" -> "+ invoice.getClientName());
                if (invoice.getReference() != null) {
                    reference.setText(invoice.getReference());
                }
            }
        });
    }

    private void loadSIAction() {
        LoadingPanel.loading(true);
        InvoiceService.App.get().getInvoice(invocieId, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(NewInvoice newInvoice) {
                LoadingPanel.loading(false);
                invoice = newInvoice;
                crmAccountLookUp.setSelected(invoice.getClientID(), invoice.getClientName());
                quoteLookUp.setSelected(quoteId, invoice.getInvoiceNumber() +" -> "+ invoice.getClientName());
            }
        });
    }

    private void loadROAction() {
        LoadingPanel.loading(true);
        RentalOrderService.App.get().getRentalOrderData(rentalOrderId, false, new AbstractAsyncCallback<RentalOrderData>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(RentalOrderData result) {
                LoadingPanel.loading(false);
                crmAccountLookUp.setSelected(result.getCustomer().getId(), result.getCustomer().getName());
                quoteLookUp.setSelected(new SelectItem(result.getObjectID(), result.getNumberData().getNumberString(), RENTAL_ORDERS));
                quoteLookUp.setEnabled(false);
            }
        });
    }
}
