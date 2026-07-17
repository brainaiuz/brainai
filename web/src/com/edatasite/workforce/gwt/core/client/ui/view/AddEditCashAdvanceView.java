package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.approvers.ChosenApproversWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PaymentAccountsLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButton;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.payslip.CategoryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CASH_ADVANCE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;
import static com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2.BTN_PRIMARY;

/**
 * Created by Omonullo Abdullaev on 8/22/2016.
 */
public class AddEditCashAdvanceView extends CustomForm2 implements Colapse {

    private final Integer calculationScale = Utils.getAccountingCalculationScale() != null ? Utils.getAccountingCalculationScale() : 2;
    public TextArea2 purpose;
    public CashAdvanceItem cashAdvanceItem;
    public GeneralFileUpload uploadForm;
    protected CurrencyWidget currencyWidget;
    private Integer objectID;
    private PayrollEmployeeLookUp employeeLookUp;
    private DatePicker requestedDate;
    private TextBox requestedAmount;
    private DataListBox paymentMethod;
    private String statusCode;
    private DataListBox terms;
    private TextBox paymentAmount;
    private WfmButton2 submitToManager;
    private WfmButton2 saveAndApprove;
    private WfmButton2 saveClose;
    private WfmButton2 draftButton;
    private PaymentAccountsLookUp paidFrom;
    private CategoryLookUp categoryLookUp;
    private ChosenApproversWidget approver;
    private TextBox reference;
    private TextBox number;
    private BankTransferNumberData transferNumberData;
    private boolean enabledMultiCurrency;
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/yyyy");
    private SplitButton printPdfSplitButton;
    public LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    public AddEditCashAdvanceView() {
        super("addCashAdvance");
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
    }

    public AddEditCashAdvanceView(Integer objectID, String statusCode) {
        super("edit");
        setDescription(property.getSingular(wfmStrings.cashAdvance()));
        this.objectID = objectID;
        this.statusCode = statusCode;
    }

    public AddEditCashAdvanceView(String name, String desc, Integer objectID) {
        super(name, desc);
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.CashAdvanceList, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddEditCashAdvanceView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    protected void registerFields() {

        employeeLookUp = new PayrollEmployeeLookUp(false);
        employeeLookUp.addStyleName(DEFAULT_WIDTH);

        employeeLookUp.getSuggestBox().addSelectionHandler(selectionEvent -> employeeLookupSelected());

        requestedDate = new DatePicker();
        requestedDate.addStyleName(DEFAULT_WIDTH);
        requestedDate.addChangeHandler(changeEvent -> {
            if (transferNumberData != null && transferNumberData.isWithDate()) {
                transferNumberData.setDate(dateFormat.format(requestedDate.getDate()));
                String[] numberParts = number.getText().split("-"); //MT0001 or MT0001-05/2015
                number.setText(numberParts[0] + "-" + transferNumberData.getDate());
            }
        });
        requestedAmount = new TextBox();
        requestedAmount.addStyleName(DEFAULT_WIDTH);
        requestedAmount.addChangeHandler(c -> {
            requestedAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(requestedAmount.getText())));
        });
        Validation.addNumericKeyboardListener(requestedAmount, 2);

        paymentAmount = new TextBox();
        paymentAmount.setPlaceHolder(wfmStrings.amount());
        paymentAmount.addChangeHandler(c -> {
            paymentAmount.setText(AccountingUtils.get().formatQty(AccountingUtils.parsePriceToBigDecimal(paymentAmount.getText())));
        });
        paymentAmount.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(paymentAmount, 2);

        reference = new TextBox();
        reference.addStyleName(DEFAULT_WIDTH);

        number = new TextBox();
        number.addStyleName(DEFAULT_WIDTH);

        terms = new DataListBox();
        terms.addStyleName(DEFAULT_WIDTH);
        terms.setWithoutNullLabel(true);
        terms.setItems(new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed() + " " + wfmStrings.amount()),
                new SelectItem(1, wfmStrings.percentage())
        });
        terms.setChangeEvent(() -> {
            if (terms.getSelectedItem().getId() == 0) {
                paymentAmount.setPlaceHolder(wfmStrings.amount());
            } else {
                paymentAmount.setPlaceHolder("%");
            }
        });

        paymentMethod = new DataListBox();
        paymentMethod.addStyleName(DEFAULT_WIDTH);

        paidFrom = new PaymentAccountsLookUp();
        paidFrom.addStyleName(DEFAULT_WIDTH);

        categoryLookUp = new CategoryLookUp(PayrollConstants.CATEGORY_DEDUCTION, true);
        categoryLookUp.addStyleName(DEFAULT_WIDTH);

        /*approver = new ChosenApproversWidget("CASH_ADVANCE", objectID);
        approver.setWidth("250px");*/

        uploadForm = new GeneralFileUpload(Constants.F_CASH_ADVANCE, objectID, objectID);
        uploadForm.ensureDebugId("addCashAdvance" + "uploadForm");

        currencyWidget = new CurrencyWidget(objectID == null);
        currencyWidget.addStyleName(DEFAULT_WIDTH);
        currencyWidget.setEnabled(false);
        currencyWidget.setDatePicker(requestedDate);
        currencyWidget.setOnloadListener(() -> employeeLookupSelected());

        addTitleField(DETAILS, wfmStrings.basicDetails());
        addTitleField(CustomFormConstants.PAYROLL_STARTER.PURPOSE, null);
        addTitleField(ATTACHMENTS, getTitle(wfmStrings.attachments()));

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE) != null) {
            addField(PAYROLL_STARTER.EMPLOYEE, employeeLookUp, getTitle(formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isChanged() ? formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getTitle() : wfmStrings.requester(), formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isRequired()), false,
                    formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isInformation());
            if (formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isInformation()) {
                new KpiToolTip(employeeLookUp, formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getInformationText());
            }

            employeeLookUp.setEnabled(!formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isDisabled());
        } else {
            addField(PAYROLL_STARTER.EMPLOYEE, employeeLookUp, getTitle(wfmStrings.requester(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT, requestedAmount, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).getTitle() : wfmStrings.requestedAmount(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isInformation()) {
                new KpiToolTip(requestedAmount, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).getInformationText());
            }

            requestedAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT, requestedAmount, getTitle(wfmStrings.requestedAmount(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getTitle() : wfmStrings.date(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isInformation()) {
                new KpiToolTip(requestedDate, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getInformationText());
            }

            requestedDate.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE, requestedDate, getTitle(wfmStrings.date(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, terms, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).getTitle() : wfmStrings.paymentTerms(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isInformation()) {
                new KpiToolTip(terms, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).getInformationText());
            }

            terms.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS, terms, getTitle(wfmStrings.paymentTerms()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).getTitle() : wfmStrings.paymentAmount(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isInformation()) {
                new KpiToolTip(paymentAmount, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).getInformationText());
            }

            paymentAmount.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT, paymentAmount, getTitle(wfmStrings.paymentAmount(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(REFERENCE) != null) {
            addField(REFERENCE, reference, getTitle(formPropertyMap.get(REFERENCE).isChanged() ? formPropertyMap.get(REFERENCE).getTitle() : wfmStrings.reference(), formPropertyMap.get(REFERENCE).isRequired()), false,
                    formPropertyMap.get(REFERENCE).isInformation());
            if (formPropertyMap.get(REFERENCE).isInformation()) {
                new KpiToolTip(reference, formPropertyMap.get(REFERENCE).getInformationText());
            }

            reference.setEnabled(!formPropertyMap.get(REFERENCE).isDisabled());
        } else {
            addField(REFERENCE, reference, wfmStrings.reference());
        }

        if (formPropertyMap != null && formPropertyMap.get(NUMBER) != null) {
            addField(NUMBER, number, getTitle(formPropertyMap.get(NUMBER).isChanged() ? formPropertyMap.get(NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(NUMBER).isRequired()), false,
                    formPropertyMap.get(NUMBER).isInformation());
            if (formPropertyMap.get(NUMBER).isInformation()) {
                new KpiToolTip(number, formPropertyMap.get(NUMBER).getInformationText());
            }

            number.setEnabled(!formPropertyMap.get(NUMBER).isDisabled());
        } else {
            addField(NUMBER, number, getTitle(wfmStrings.number(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYMENT_METHOD) != null) {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(formPropertyMap.get(PAYMENT_METHOD).isChanged() ? formPropertyMap.get(PAYMENT_METHOD).getTitle() : wfmStrings.paymentMethod(), formPropertyMap.get(PAYMENT_METHOD).isRequired()), false,
                    formPropertyMap.get(PAYMENT_METHOD).isInformation());
            if (formPropertyMap.get(PAYMENT_METHOD).isInformation()) {
                new KpiToolTip(paymentMethod, formPropertyMap.get(PAYMENT_METHOD).getInformationText());
            }

            paymentMethod.setEnabled(!formPropertyMap.get(PAYMENT_METHOD).isDisabled());
        } else {
            addField(PAYMENT_METHOD, paymentMethod, getTitle(wfmStrings.paymentMethod()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null) {
            addField(CATEGORY, categoryLookUp, getTitle(formPropertyMap.get(CATEGORY).isChanged() ? formPropertyMap.get(CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CATEGORY).isRequired()), false,
                    formPropertyMap.get(CATEGORY).isInformation());
            if (formPropertyMap.get(CATEGORY).isInformation()) {
                new KpiToolTip(categoryLookUp, formPropertyMap.get(CATEGORY).getInformationText());
            }

            categoryLookUp.setEnabled(!formPropertyMap.get(CATEGORY).isDisabled());
        } else {
            addField(CATEGORY, categoryLookUp, getTitle(wfmStrings.category(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.PURPOSE) != null) {
            purpose = new TextArea2(1000, getTitle(formPropertyMap.get(PAYROLL_STARTER.PURPOSE).isChanged() ? formPropertyMap.get(PAYROLL_STARTER.PURPOSE).getTitle() : wfmStrings.purpose()));
            addField(PAYROLL_STARTER.PURPOSE, purpose, null, formPropertyMap.get(PAYROLL_STARTER.PURPOSE).isRequired(), formPropertyMap.get(PAYROLL_STARTER.PURPOSE).isInformation());
            if (formPropertyMap.get(PAYROLL_STARTER.PURPOSE).isInformation()) {
                new KpiToolTip(purpose, formPropertyMap.get(PAYROLL_STARTER.PURPOSE).getInformationText());
            }

            purpose.setEnabled(!formPropertyMap.get(PAYROLL_STARTER.PURPOSE).isDisabled());
        } else {
            purpose = new TextArea2(1000, getTitle(wfmStrings.purpose()));
            addField(CustomFormConstants.PAYROLL_STARTER.PURPOSE, purpose);
        }
        purpose.setHeight("150px");
        getCustomFieldUtil().drawCustomFields(this, objectID, false);


        addField(ATTACHMENTS, uploadForm);
        initPredefinedValues();
        setDefaultValues();
        show();
    }

    private void employeeLookupSelected() {
        if (enabledMultiCurrency && employeeLookUp.getSelectedItemID() != null) {
            CurrencyService.App.get().getEmployeeCurrencies(employeeLookUp.getSelectedItemID(), false, new AsyncCallback<CurrencyItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(CurrencyItem[] currencyItems) {
                    addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, wfmStrings.exchangeRate());
                    if (currencyItems.length > 1)
                        currencyWidget.setCurrency(currencyItems[1].getId());
                    else
                        currencyWidget.setCurrency(currencyItems[0].getId());
                }
            });
        }
    }


    @Override
    protected void addButtons() {

        if (objectID == null || Constants.DRAFT.equals(statusCode)) {
            draftButton = addButton(wfmStrings.draft(), WfmButton2.BTN_WHITE_OUTLINE, clickEvent -> {
                draftButton.setEnabled(false);
                save(Constants.DRAFT);
            });
        }

        submitToManager = addButton(Constants.REJECTED.equals(statusCode) ? wfmStrings.resubmitForApproval() : wfmStrings.submitForApproval(), clickEvent -> {
            submitToManager.setEnabled(false);
            save(Constants.SUBMITTED_TO_MANAGER);
        });

        saveAndApprove = addButton(wfmStrings.approve(), BTN_PRIMARY, clickEvent -> {
            saveAndApprove.setEnabled(false);
            save(Constants.APPROVED);
        });

        if (objectID != null) {
            printPdfSplitButton = new SplitButton(100, WfmButton2.BTN_WHITE_OUTLINE);
            addRightButton(printPdfSplitButton);

        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_APPROVERS_LOADED, AddEditCashAdvanceView.this, (sender, args) -> {
            if (approver.getFirstApproverLookUp() != null) {
                approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    Integer itemId = item != null ? item.getId() : null;
                    Integer currentUserId = cashAdvanceItem.getCurrentUserId() != null ? cashAdvanceItem.getCurrentUserId() : Utils.getUserID();
                    if (currentUserId.equals(itemId)) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        submitToManager.setVisible(true);
                        saveAndApprove.setVisible(false);
                    }
                });
                if (saveAndApprove != null && submitToManager != null && approver.getFirstApproverLookUp().getSelectedItem() != null) {
                    SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                    if (item != null && item.getId() != null && Utils.getUserID().equals(item.getId())) {
                        saveAndApprove.setVisible(true);
                        submitToManager.setVisible(false);
                    } else {
                        saveAndApprove.setVisible(false);
                        submitToManager.setVisible(true);
                    }
                }
            }
        });
    }


    private void generatePDF(HTMLPanel panel, Integer templateID, boolean landscape) {
        LeaveRequestObject requestObject = new LeaveRequestObject(objectID);
        HashMap<String, String> parameters = requestObject.getRequestParams();
        if (templateID != null) {
            parameters.put("pdfTemplateID", String.valueOf(templateID));
        }
        if (landscape) {
            parameters.put("IS_LANDSCAPE", "true");
        }
        String pdfURL = CommandConstants.PDF_URL + "/cashAdvancePdfHandler";
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parameters, "_blank");
    }

    public void pdfTool(CashAdvanceItem result) {
        if (printPdfSplitButton == null) {
            return;
        }
        List<SplitButtonItem> pdfTemplatesList = new ArrayList<>();
        Integer defaultTemplateId = null;
        if (result != null && result.getTemplates() != null && result.getTemplates().length > 0) {
            for (SelectItem pdfItem : result.getTemplates()) {
                if (pdfItem.isDefaultSelected()) {
                    defaultTemplateId = pdfItem.getId();
                }
                pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> generatePDF(panel, pdfItem.getId(), false)));
            }
        } else {
            pdfTemplatesList.add(new SplitButtonItem("PDF_TEMPLATE_", wfmStrings.landscape(), () -> generatePDF(panel, null, true)));
        }
        Integer finalDefaultTemplateId = defaultTemplateId;

        SplitButtonItem pdfVersion = new SplitButtonItem(PDF_VERSION, wfmStrings.pdfVersion(), () -> generatePDF(panel, finalDefaultTemplateId, false), true);
        pdfTemplatesList.add(pdfVersion);
        printPdfSplitButton.addItemList(pdfTemplatesList);
    }

    private boolean validation(String status) {
        clearErrorStyle();
        int errors = customValidate();
        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE) != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).isRequired()) {
            errors += markAsError(employeeLookUp, !Validation.validateLookUpRequired(employeeLookUp));
        }

        if (formPropertyMap != null && formPropertyMap.get(APPROVER) != null && formPropertyMap.get(APPROVER).isRequired() && !approver.isValid()) {
            errors += getCustomFieldUtil().validateCustomFields();
        }

        if (!Validation.validateLookUpRequired(employeeLookUp)) {
            errors++;
        }

        if (!Constants.DRAFT.equals(status) && formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).isRequired()) {
            errors += markAsError(requestedAmount, !Validation.validateTextBoxRequired(requestedAmount));
        }

        if (!Constants.DRAFT.equals(status) && formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).isRequired()) {
            errors += markAsError(requestedDate, !Validation.validateDate(requestedDate));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()) {
            errors += markAsError(number, !Validation.validateTextBoxRequired(number));
        }

        if (!Constants.DRAFT.equals(status) && formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).isRequired()) {
            errors += markAsError(categoryLookUp, !Validation.validateLookUpRequired(categoryLookUp));
        }

        if (!Constants.DRAFT.equals(status) && formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).isRequired()) {
            errors += markAsError(paymentAmount, !Validation.validateTextBoxRequired(paymentAmount));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS).isRequired()) {
            errors += markAsError(terms, !Validation.validateListBoxRequired(terms));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null && formPropertyMap.get(CustomFormConstants.REFERENCE).isRequired()) {
            errors += markAsError(reference, !Validation.validateTextAreaRequired(reference));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD) != null && formPropertyMap.get(CustomFormConstants.PAYMENT_METHOD).isRequired()) {
            errors += markAsError(paymentMethod, !Validation.validateListBoxRequired(paymentMethod));
        }


        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).isRequired()) {
            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.PURPOSE, purpose, Utils.isNullOrEmpty(purpose.getText()));
        }
//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).isRequired()) {
//            errors += markAsError(CustomFormConstants.PAYROLL_STARTER.PURPOSE, purpose, !Validation.validateTextAreaRequired(purpose));
//        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE) != null && formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).isRequired()) {
            errors += markAsError(CustomFormConstants.EXCHANGE_RATE, currencyWidget, currencyWidget.getCurrencyID() == null);
        }

        if (errors > 0) {
            Info.show(wfmStrings.fillRequiredField(), Info.Type.WARNING);
            return false;
        }

        if (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(requestedDate.getDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.cashAdvance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void enableButtons() {
        if (draftButton != null)
            draftButton.setEnabled(true);
        if (submitToManager != null)
            submitToManager.setEnabled(true);
        if (saveAndApprove != null)
            saveAndApprove.setEnabled(true);
        if (saveClose != null)
            saveClose.setEnabled(true);
    }

    protected void save(String status) {
        if (!validation(status)) {
            enableButtons();
            return;
        }
        CashAdvanceItem item = new CashAdvanceItem();
        item.setObjectID(objectID);
        if (objectID == null) {
            item.setCreationDate(new DateNonConvertable(new Date()));
        }
        item.setPurpose(purpose.getText());
        item.setEmployee(employeeLookUp.getSelectedItem());
        item.setDate(new DateNonConvertable(requestedDate.getDate()));
        item.setType("Loan");
        item.setPaymentMethod(paymentMethod.getSelectedItem());
        item.setTotalAmount(Utils.parseToBigDecimal(requestedAmount.getText()));
        item.setStatus(new SelectItem(status));
        if (terms.getSelectedId(true) != 0) {
            Double percent = Double.valueOf(paymentAmount.getText());
            item.setPercent(percent);
            item.setPaymentAmount(item.getTotalAmount().multiply(BigDecimal.valueOf(percent)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
        } else {
            item.setPaymentAmount(Utils.parseToBigDecimal(paymentAmount.getText()));
        }
        if (Constants.APPROVED.equals(status)) {
            item.setApprovedDate(new DateNonConvertable(new Date()));
            item.setPaidFromAccount(paidFrom.getSelectedItem());
        }
        item.setCategoryItem(categoryLookUp.getSelectedData());
        item.setApprovers(approver.getChosenApprovers());
        if (enabledMultiCurrency) {
            item.setTotalInBaseAmount(item.getTotalAmount().divide(currencyWidget.getExchangeRate(), calculationScale, RoundingMode.HALF_UP));
            item.setCurrency(currencyWidget.getCurrency());
            item.setExchangeRate(currencyWidget.getExchangeRate());
        }
        item.setReference(reference.getValue());
        item.setNumber(number.getValue());
        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        if (objectID == null) {
            if (!"".equals(number.getText()) && number.getText() != null) {
                String prefix = transferNumberData.getPrefix();
                String[] numberParts = number.getText().split("-"); //MT0001 or MT0001-05/2015
                String numberPrefix = numberParts[0].substring(0, 2);
                if (prefix.equals(numberPrefix)) {
                    try {
                        String fourDigitNumber = numberParts[0].substring(prefix.length());
                        item.setIntNumber(Integer.valueOf(fourDigitNumber));
                    } catch (Exception e) {
                        item.setIntNumber(0);
                    }
                } else {
                    item.setIntNumber(0);
                }
            }
            item.setNumber(number.getText());
            item.setAttachments(uploadForm.getAttachedFiles());
        }
        if (item.getPaymentAmount().compareTo(item.getTotalAmount()) > 0) {
            Info.show(wfmStrings.paymentAmountMoreThanRequestAmount(), Info.Type.WARNING);
            enableButtons();
            return;
        }
        if (Utils.isCashAdvancesLocked() && DateUtils.getTransactionLockDate().after(item.getDate().getNonConvertedDate())) {
            Info.show(wfmMessages.dateShouldBeAfterClosedBeforeDate(wfmStrings.cashAdvance(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return;
        }
        LoadingPanel.loading(true);
        CoreService.App.get().saveCashAdvance(item, new AbstractAsyncCallback<TestRPC>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TestRPC result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    if (CashAdvanceItem.NUMBER_EXISTS.equals(result.getMessage())) {
                        Info.show(wfmStrings.numberAlreadyExist(), Info.Type.WARNING);
                        enableButtons();
                    } else if (CashAdvanceItem.NOT_SUFFICIENT_AMOUNT.equalsIgnoreCase(result.getMessage())) {
                        Info.show(wfmStrings.insufficientAmount(), Info.Type.WARNING);
                        enableButtons();
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CASH_SAVED, null, AddEditCashAdvanceView.this);
                        closeTab();
                    }
                }
            }
        });

    }

    @Override
    protected void getDataToFillFields() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(objectID);
        LoadingPanel.loading(true);
        CoreService.App.get().getCashAdvancedItem(filterParameter, new AsyncCallback<CashAdvanceItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CashAdvanceItem result) {
                if (result != null) {
                    cashAdvanceItem = result;
                    enabledMultiCurrency = cashAdvanceItem.isEnabledMultiCurrency();
                    setValues(result);
                    transferNumberData = result.getBankTransferNumberData();
                    number.setText(result.getNumber());
                    pdfTool(result);
                    if (objectID != null && transferNumberData != null) {
                        String dateString = result.getDate() != null ? dateFormat.format(result.getDate().getDate()) : "";
                        transferNumberData.setWithDate(result.getNumber() != null && result.getNumber().contains(dateString));
                        transferNumberData.setDate(transferNumberData.isWithDate() ? dateString : "");
                    }
                    if (cashAdvanceItem.getPaymentMethods() != null && cashAdvanceItem.getPaymentMethods().length > 0) {
                        paymentMethod.setItems(cashAdvanceItem.getPaymentMethods());
                        if (cashAdvanceItem.getPaymentMethod() != null) {
                            paymentMethod.setSelected(cashAdvanceItem.getPaymentMethod());
                        }
                    }
                }
                if (approver.getFirstApproverLookUp() != null) {
                    approver.getFirstApproverLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
                        SelectItem item = approver.getFirstApproverLookUp().getSelectedItem();
                        Integer itemId = item != null ? item.getId() : null;
                        Integer currentUserId = cashAdvanceItem.getCurrentUserId() != null ? cashAdvanceItem.getCurrentUserId() : Utils.getUserID();
                        if (currentUserId.equals(itemId)) {
                            saveAndApprove.setVisible(true);
                            submitToManager.setVisible(false);
                        } else {
                            submitToManager.setVisible(true);
                            saveAndApprove.setVisible(false);
                        }
                    });
                } else {
                    if (submitToManager != null) {
                        submitToManager.setVisible(false);
                    }
                }
                LoadingPanel.loading(false);

                if (objectID == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });

    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE) != null && formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getDefaultValue() != null) {
            employeeLookUp.setSelected(new SelectItem(formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getSelectedId(), formPropertyMap.get(PAYROLL_STARTER.EMPLOYEE).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).getDefaultValue() != null) {
            requestedAmount.setText(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue() != null) {
            if (!"".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue()) && ("TODAY".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue()) || "TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue())
                    || "YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue()))) {
                Date currentDate = new Date();
                if ("TOMORROW".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue())) {
                    currentDate = DateUtil.addDays(currentDate, 1);
                } else if ("YESTERDAY".equals(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue())) {
                    currentDate = DateUtil.minusDays(currentDate, 1);
                }
                requestedDate.setDate(currentDate);
            } else {
                requestedDate.setDate(new Date(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE).getDefaultValue()));
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS) != null && formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS).getDefaultValue() != null) {
            terms.setSelected(new SelectItem(formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS).getSelectedId(), formPropertyMap.get(PAYROLL_STARTER.PAYMENT_TERMS).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).getDefaultValue() != null) {
            paymentAmount.setText(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null && formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue() != null) {
            reference.setText(formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(PAYROLL_STARTER.PAYMENT_METHOD) != null && formPropertyMap.get(PAYROLL_STARTER.PAYMENT_METHOD).getDefaultValue() != null) {
            paymentMethod.setSelected(new SelectItem(formPropertyMap.get(PAYROLL_STARTER.PAYMENT_METHOD).getSelectedId(), formPropertyMap.get(PAYROLL_STARTER.PAYMENT_METHOD).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CATEGORY) != null && formPropertyMap.get(CATEGORY).getDefaultValue() != null) {
            categoryLookUp.setSelected(new SelectItem(formPropertyMap.get(CATEGORY).getSelectedId(), formPropertyMap.get(CATEGORY).getDefaultValue()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REFERENCE) != null && formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue() != null) {
            reference.setText(formPropertyMap.get(CustomFormConstants.REFERENCE).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE) != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).getDefaultValue() != null) {
            purpose.setText(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.PURPOSE).getDefaultValue());
        }

    }

    protected void setValues(CashAdvanceItem result) {
        //Cash advance can be created as a draft without all fields by API. In this case, if you edit the cash advance, approver widget will data will be null.
        //So if a cash advance is created by API, approver widget should work as it works edit mode
        approver = new ChosenApproversWidget("CASH_ADVANCE", result.getApprover() != null ? objectID : null);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER) != null) {
            addField(CustomFormConstants.PAYROLL_STARTER.APPROVER, approver, getTitle(formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isChanged() ? formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).getTitle() : wfmStrings.approver(), formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isRequired()), false,
                    formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isInformation()) {
                    new KpiToolTip(approver, formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).getInformationText());
            }

            approver.setEnabled(!formPropertyMap.get(CustomFormConstants.PAYROLL_STARTER.APPROVER).isDisabled());
        } else {
            addField(CustomFormConstants.PAYROLL_STARTER.APPROVER, approver, getTitle(wfmStrings.approver(), true));
        }

        enabledMultiCurrency = result.isEnabledMultiCurrency();
        boolean isPercentage = false;
        if (result.getPurpose() != null) {
            purpose.setText(result.getPurpose());
        }

        if (result.getEmployee() != null) {
            employeeLookUp.addItem(result.getEmployee());
            employeeLookUp.setSelected(result.getEmployee());
            employeeLookupSelected();
        }

        if (result.getDate() != null && result.getDate().getDate() != null) {
            requestedDate.setDate(result.getDate().getNonConvertedDate());
        }
        if (result.getTotalAmount() != null) {
            requestedAmount.setText(Utils.getCalculationNumberFormat().format(result.getTotalAmount()));
        }
        if (result.getPercent() != null) {
            paymentAmount.setText(Utils.getNumberFormat().format(BigDecimal.valueOf(result.getPercent())));
            isPercentage = true;
        }
        if (result.getCategoryItem() != null) {
            categoryLookUp.addCategoryItem(result.getCategoryItem());
        }

        if (!isPercentage && result.getPaymentAmount() != null) {
            paymentAmount.setText(Utils.getCalculationNumberFormat().format(result.getPaymentAmount()));
        }
        if (result.getReference() != null) {
            reference.setValue(result.getReference());
        }
        if (result.getNumber() != null) {
            number.setValue(result.getNumber());
        }
        if (isPercentage) {
            terms.setSelected(1);
        } else {
            terms.setSelected(0);
        }
        if (enabledMultiCurrency && result.getCurrency() != null) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE) != null) {
                addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, getTitle(formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).isChanged() ? formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).getTitle() : wfmStrings.exchangeRate(), formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).isRequired()));
                currencyWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.EXCHANGE_RATE).isDisabled());
            } else {
                addField(CustomFormConstants.EXCHANGE_RATE, currencyWidget, wfmStrings.exchangeRate());
            }
            currencyWidget.setCurrency(result.getCurrency().getId(), result.getExchangeRate());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(PAYROLL_STARTER.PAYMENT_TERMS, new SelectItem[]{
                new SelectItem(0, wfmStrings.fixed() + " " + wfmStrings.amount().toLowerCase()),
                new SelectItem(1, wfmStrings.percentage())
        });
    }

    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return property.getSingular(accountingStrings.cashAdvanceDetails(), wfmStrings.cashAdvance());
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.contract() + " " + wfmStrings.number();
            } else if (CustomFormConstants.PAYROLL_STARTER.EMPLOYEE.equals(fieldID)) {
                return wfmStrings.requester();
            } else if (PAYROLL_STARTER.DRIVER_NUMBER.equals(fieldID)) {
                return "Driver ID";
            } else if (CustomFormConstants.PAYROLL_STARTER.APPROVER.equals(fieldID)) {
                return wfmStrings.approver();
            } else if (CustomFormConstants.PAYROLL_STARTER.REQUESTED_AMOUNT.equals(fieldID)) {
                return wfmStrings.requestedAmount();
            } else if (CustomFormConstants.PAYROLL_STARTER.REQUESTED_DATE.equals(fieldID)) {
                return wfmStrings.date();
            } else if (CustomFormConstants.PAYROLL_STARTER.PAYMENT_TERMS.equals(fieldID)) {
                return wfmStrings.paymentTerms();
            } else if (CustomFormConstants.PAYROLL_STARTER.PAYMENT_AMOUNT.equals(fieldID)) {
                return wfmStrings.paymentAmount();
            } else if (PAYMENT_METHOD.equals(fieldID)) {
                return wfmStrings.paymentMethod();
            } else if (CustomFormConstants.PAYROLL_STARTER.PAY_FROM.equals(fieldID)) {
                return wfmStrings.paidFrom();
            } else if (CustomFormConstants.EXCHANGE_RATE.equals(fieldID)) {
                return wfmStrings.exchangeRate();
            } else if (CATEGORY.equals(fieldID)) {
                return wfmStrings.category();
            } else if (CustomFormConstants.PAYROLL_STARTER.PURPOSE.equals(fieldID)) {
                return wfmStrings.purpose();
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.PAYROLL_STARTER.CASH_ADVANCE_ACCOUNT.equals(fieldID)) {
                return property.getSingular(wfmStrings.cashAdvanceAccount(), wfmStrings.cashAdvance());
            } else if (REFERENCE.equals(fieldID)) {
                return wfmStrings.reference();
            } else if (NUMBER.equals(fieldID)) {
                return wfmStrings.number();
            }
        }
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PAYROLL_CASH_ADVANCE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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

    @Override
    public String getPropertyCode() {
        return CASH_ADVANCE_LIST;
    }
}
