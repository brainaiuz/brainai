package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyAddress;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PDFTransferObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceQuoteFormPresenter;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDuePanel;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDueProvider;
import com.edatasite.workforce.gwt.profile.client.ui.view.PaymentTermsConditionsUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Sherzod
 * Date: 2/3/12
 * Time: 2:43 PM
 */
public class PurchaseInvoiceFormPresenter extends InvoiceQuoteFormPresenter implements Constants, AccountingConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final PurchaseInvoiceViewInterface viewInterface;
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd MMM yyyy");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM dd, yyyy");
    private NewInvoice newInvoice;
    private Integer currencyId;
    private String oldStatus;

    public PurchaseInvoiceFormPresenter(PurchaseInvoiceViewInterface viewInterface) {
        super(viewInterface);
        this.viewInterface = viewInterface;
    }

    @Override
    public void bindUI() {
        generateData();

        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyAddress(new AbstractAsyncCallback<CompanyAddress>() {

            @Override
            public void success(CompanyAddress result) {
                if (result.getMailAddresses() != null) {
                    viewInterface.getCrmAccountWidgets().presenter.setShippingAddress(result.getMailAddresses());
                }
            }
        });

        InvoiceService.App.get().getAllInvoiceData(viewInterface.getFormParameters(), new AbstractAsyncCallback<NewInvoice>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NewInvoice result) {
                newInvoice = result;
                viewInterface.initCustomFields(result);
                viewInterface.initSystemCustomFields(result);
                viewInterface.initPdfTemplates(result);
                viewInterface.getProductTable().setRoundingModeDisabled(result.isRoundingModeDisabled());
                viewInterface.getProductTable().setDoubleTaxEnabled(result.isDoubleTaxEnabled());
                viewInterface.getProductTable().setItemCustomFields(result.getItemCustomFields());

                customerSupplierItem = result.getTypeItem();
                currencyId = result.getCurrencyID();
                viewInterface.initProductsTableData(result);
                initFormHandlers();

                viewInterface.initWidgetMap(result);
                viewInterface.generateForm(result.getLayoutHTML());
                viewInterface.setFormData(result);

                setRelatedPriceLevel(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);
                if (!viewInterface.isEditForm() && result.isPurchaseInvoiceNumberingShow()) {
                    applyInvoiceNumberData(result.getNumberData(), result.getTypeItem());
                } else if (viewInterface.isEditForm() && result.isPurchaseInvoiceNumberingShow() && (result.getInvoiceNumber() == null || "".equals(result.getInvoiceNumber()))) {
                    applyInvoiceNumberData(result.getNumberData(), result.getTypeItem());
                }
                getAndApplyTermsConditions(result);
                LoadingPanel.loading(false);
            }
        });

        addFormListeners();
    }

    private void applyInvoiceNumberData(InvoiceNumberData result, TypeItem item) {
        if (result != null) {
            if (result.isWithDate()) {
                result.setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getCurrentDate()));
            }
            if (result.isWithClient() && item != null) {
                result.setClientCode(item.getCode());
            }
            if (result.isWithProject()) {
                result.setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
            }
            if (!OPPORTUNITY.equals(viewInterface.getFormParameters().getCrmFormName()) &&
                    item != null &&
                    !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID()) &&
                    !CONVERT_TO_INVOICE.equals(viewInterface.getFormParameters().getExternalFormID()) &&
                    !CONVERT_TO_INVOICE_FROM_GRN.equals(viewInterface.getFormParameters().getExternalFormID()) &&
                    !COPY_PO_TO_PI.equals(viewInterface.getFormParameters().getExternalFormID()) &&
                    !COPY_FROM_SI_TO_PI.equals(viewInterface.getFormParameters().getExternalFormID()) &&
                    viewInterface.getFormParameters().getConvertFormType() == null) {
                viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());
            }
            viewInterface.getNumberTxtBox().setText(result.getInvoiceNumber());
        }
        viewInterface.setNumberData(result);

    }

    private void initFormHandlers() {

        if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA)) {
            getAndSetInvoiceData(viewInterface.getFormParameters().getExternalObjectID());
        }

        viewInterface.getCrmAccountLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

            if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null) {
                invoiceService.getClientOrSupplier(viewInterface.getCrmAccountLookUp().getSelectedItemID(), Constants.PAYABLE, new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(TypeItem result) {
                        customerSupplierItem = result;

                        if (result != null) {
                            viewInterface.getProductTable().setCustomerSupplierItem(result);

                            if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                                configurePlaceOfSupply(result, customerSupplierItem.getPlaceOfSupply(), viewInterface.getPlaceOfSupplyWidget());
                            }
                            viewInterface.getCurrencyWidget().setCurrency(result.getCurrencyID());

                            getSupplierForAccountsReceivable(result);

                            if (result.getTermsItem() != null) {
                                viewInterface.getTermsAndDueDatePanel().applyCustomerTerms(result.getTermsItem());
                            }
                            if (result.getTaxItem() != null) {
                                viewInterface.getProductTable().setCustomerOrSupplierTaxItem(result.getTaxItem());
                            } else {
                                viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
                            }

                            if (result.getSupplierCustomerBalance() >= 0) {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText(AccountingUtils.get().formatPrice(result.getSupplierCustomerBalance()));
                            } else {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText("(" + AccountingUtils.get().formatPrice((-1) * result.getSupplierCustomerBalance()) + ")");
                            }

                            viewInterface.getProductTable().setCustomerDefaultWarehouseAndDepartment(result.getDefaultDepartment(), result.getDefaultWarehouse());

                            if (viewInterface.getTermsAndDueDatePanel() != null && result != null && result.getTermsItem() != null) {
                                viewInterface.getTermsAndDueDatePanel().setData(TERMS_TYPE, viewInterface.getTermsAndDueDatePanel().getDueDate(), result.getTermsItem());
                            }
                        }

                        if (newInvoice.isPurchaseInvoiceNumberingShow() && !viewInterface.isEditForm() && viewInterface.getCrmAccountLookUp().getSelectedItemID() != null) {
                            onChangeInvoiceNumber(result);
                        }

                    }
                });
                setRelatedPriceLevel(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
                setPurchaseInvoiceCustomFields();
            }
        });

        viewInterface.getSupplierBalanceLink().addClickHandler(clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("supplierBalance|supplierBalance/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + CrmAccountItem.SUPPLIER,
                    wfmStrings.balance() + ": " + viewInterface.getCrmAccountLookUp().getSelectedItem().getName());
        });
        AtomicBoolean isConvert = new AtomicBoolean(true);
        viewInterface.getCurrencyWidget().addListener(() -> {
            if (isConvert.get() && currencyId != null && (CONVERT_TO_INVOICE_FROM_GRN.equals(viewInterface.getFormParameters().getExternalFormID()) || (viewInterface.getFormParameters().getConvertFormType() != null && viewInterface.getFormParameters().getConvertFormType().length() > 0))) {
                isConvert.set(false);
                Scheduler.get().scheduleFixedDelay(() -> {
                    if (newInvoice.getExchageRate() != null) {
                        viewInterface.getCurrencyWidget().setCurrency(currencyId, newInvoice.getExchageRate());
                    } else {
                        viewInterface.getCurrencyWidget().setCurrency(currencyId);
                    }
                    viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(viewInterface.getCurrencyWidget().getCurrencyID());
                    return false;
                }, 500);

            } else {
                viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(viewInterface.getCurrencyWidget().getCurrencyID());
            }

        });

        if (viewInterface.isEditForm()) {

            if (newInvoice.getPaidAmount() != null && !(newInvoice.isSubmitter(Utils.getUserID()) || Utils.hasRoles(Constants.ADMIN))) {
//                viewInterface.getCurrencyWidget().setEnabled(false);
                viewInterface.getApproverLookUp().setEnabled(false);
            }

            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                update(Constants.DRAFT);
            });

            viewInterface.getApproveButton().addClickHandler(sender -> {
                oldStatus = newInvoice.getStatusCode();
                setEnabledButtons(false);
                update(Constants.APPROVE);
            });

            viewInterface.getSubmitButton().addClickHandler(event -> {
                setEnabledButtons(false);
                update(Constants.SUBMITTED_TO_MANAGER);
            });
        } else {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                save(Constants.DRAFT);
            });

            viewInterface.getApproveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                save(Constants.APPROVE);
            });

            viewInterface.getSubmitButton().addClickHandler(event -> {
                setEnabledButtons(false);
                save(Constants.SUBMITTED_TO_MANAGER);
            });
        }

        viewInterface.getPriceLevelDropdown().addValueChangeHandler(vch -> onChangePriceLevel(newInvoice));
        viewInterface.getProductTable().getPaymentTermsConditionsListBox().addValueChangeHandler(changeEvent -> generateTermsConditions(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate())));

        viewInterface.getDatePicker().addChangeHandler(event -> {
            if (newInvoice != null && newInvoice.getDueDays() != null) {
                viewInterface.getTermsAndDueDatePanel().setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(viewInterface.getDatePicker().getDate(), newInvoice.getDueDays()), newInvoice.getInvoiceTermsItem());
            }
            if (newInvoice.isPurchaseInvoiceNumberingShow()) {
                if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithDate()) {
                    viewInterface.getNumberData().setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getDatePicker().getDate()));
                    viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                }

                if (!viewInterface.isEditForm()) {
                    onChangeInvoiceNumber(null);
                }
            }
        });
        if (newInvoice.isPurchaseInvoiceNumberingShow()) {
            viewInterface.getProjectLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject()) {
                    viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                    viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                }
            });
        }
        viewInterface.getTermsAndDueDatePanel().setTermsAndDueProvider(new TermsAndDueProvider() {
            @Override
            public void setDueDateAndTermsLabel(String text) {
                if (viewInterface.getTermsAndDueDateLabel() != null) {
                    HTML textChange = (HTML) viewInterface.getTermsAndDueDateLabel().getWidget(0);
                    MaterialLink link = (MaterialLink) viewInterface.getTermsAndDueDateLabel().getWidget(1);
                    textChange.setText(text);
                    link.setVisible(!viewInterface.getTermsAndDueDatePanel().isDueTypeSelected());
                }
            }

            @Override
            public Date getInvoiceDate() {
                return viewInterface.getDatePicker().getDate();
            }

            @Override
            public void applyPaymentInstructionData() {
                generateTermsConditions(null, dateFormat.format(viewInterface.getDatePicker().getDate()));
            }

            @Override
            public boolean isEditForm() {
                return viewInterface.isEditForm();
            }
        });

        /**
         * Generate pdf button functionality:         *
         * We moved pdf list to split button from form field
         */
        {
            //for PDf
            List<SplitButtonItem> pdfButtonItems = generatePdfTemplates(newInvoice);

            if (Utils.hasRoles(Constants.ADMIN)) {
                pdfButtonItems = pdfButtonItems != null ? pdfButtonItems : new ArrayList<>();
                pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.PURCHASE_INVOICE.name())));
            }

            viewInterface.getSplitButtonPdf().addItemList(pdfButtonItems);
            //End Pdf
        }
    }

    private void getSupplierForAccountsReceivable(TypeItem result) {
        viewInterface.getAccountsReceivablePayableLookUp().clear();
        viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(result.getCurrencyID());

        if (viewInterface.getAccountsReceivablePayableLookUp() != null && result.getAccountsReceivablePayable() != null) {
            viewInterface.getAccountsReceivablePayableLookUp().addAccountItem(result.getAccountsReceivablePayable());
        }
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_EDIT, viewInterface.getView(), (sender, args) -> setSupplierData((Integer) args, false, true));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_ADD, viewInterface.getView(), (sender, args) -> setSupplierData((Integer) args, true, true));
    }

    private void generateData() {
        InvoiceService.App.get().getInvoiceDate(viewInterface.getFormParameters().isFromGettingStarted() ? -1 : 0, new AbstractAsyncCallback<Date>() {
            public void success(Date result) {
                viewInterface.setConversionDate(result);
            }
        });
    }

    private void getAndApplyTermsConditions(final NewInvoice invoice) {
        invoiceService.getPaymentInstructions(Constants.PURCHASE_INVOICE, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                if (result != null && result.length > 0) {
                    viewInterface.getProductTable().getPaymentTermsConditionsListBox().setItems(result);
                    if (invoice.getPaymentInstructionID() != null) {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setSelected(invoice.getPaymentInstructionID());
                    } else {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setSelected(result[0].getId());
                    }
                    if (!viewInterface.isEditForm()) {
                        generateTermsConditions(invoice.getTypeItem(), DateUtils.format(invoice.getInvoiceDate()));
                    }
                    if (result.length > 1) {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setVisible(true);
                    }
                }
            }
        });
    }

    private void generateTermsConditions(TypeItem clientItem, String startDate) {
        String paymentMethod = null, dueDayStr = null, dueDateStr = null;
        Date current = viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date();
        String terms = null;
        if (viewInterface.getTermsAndDueDatePanel().getInvoiceTerms() != null && viewInterface.getTermsAndDueDatePanel().getInvoiceTerms().getName() != null) {
            terms = viewInterface.getTermsAndDueDatePanel().getInvoiceTerms().getName();
        }
        if (viewInterface.getTermsAndDueDatePanel().getDueDate() != null) {
            int i = 0;
            while (current.before(viewInterface.getTermsAndDueDatePanel().getDueDate())) {
                i++;
                current = DateUtil.addDays(current, 1);
            }
            dueDateStr = dateTimeFormat.format(viewInterface.getTermsAndDueDatePanel().getDueDate());
            dueDayStr = String.valueOf(i != 0 ? i : 1);
        }
        if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null && clientItem != null) {
            paymentMethod = clientItem.getPaymentType() != null ? clientItem.getPaymentType() : "";
        }
        if (viewInterface.getProductTable().getPaymentTermsConditionsListBox().isSomethingSelected()) {
            String template = PaymentTermsConditionsUtil.generateSelectedTemplate(viewInterface.getProductTable().getPaymentTermsConditionsTemplate(),
                    dueDayStr, dueDateStr, paymentMethod, startDate, null, null,
                    viewInterface.getNumberTxtBox().getText(), terms);
            viewInterface.getProductTable().getPaymentInstruction().setText(template);
        }
    }

    private void getAndSetInvoiceData(Integer invoiceID) {
        InvoiceService.App.get().getInvoice(invoiceID, new AbstractAsyncCallback<NewInvoice>() {
            public void failure(Throwable caught) {
            }

            public void success(NewInvoice result) {
                viewInterface.setEditValues(result);
                setRelatedPriceLevel(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);
            }
        });
    }

    private void setSupplierData(final Integer id, final Boolean add, final boolean setCurrency) {
        InvoiceService.App.get().getClientOrSupplier(id, Constants.PAYABLE, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {

            }

            public void success(TypeItem typeItem) {
                setSupplierItems(typeItem, add, setCurrency);
            }
        });
    }

    private void setSupplierItems(final TypeItem item, final Boolean add, final boolean setCurrency) {
        if (item.getId() != null) {
            if (add) {
                viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency);
            } else {
                viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency);
            }
        }

        if (item != null && !viewInterface.isEditForm()) {
            viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());
        }

        if (viewInterface.getProjectLookUp() != null) {
            viewInterface.getProjectLookUp().clear();
        }
        if (item != null && item.getTaxItem() != null) {
            viewInterface.getProductTable().setCustomerOrSupplierTaxItem(item.getTaxItem());
        } else {
            viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
        }
        getSupplierForAccountsReceivable(item);
    }

    private void save(final String status) {
        //validate base form fields
        if (!validation(status)) {
            setEnabledButtons(true);
            return;
        }

        //validate custom fields
        if (!Constants.DRAFT.equals(status) && !viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }
        if (customerSupplierItem != null && customerSupplierItem.isSubsidiary() != null && customerSupplierItem.isSubsidiary()) {
            InvoiceService.App.get().validateItemsInConsignment(viewInterface.getProductTable().getQuantityItemsForValidate(), null, new AsyncCallback<String[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    setEnabledButtons(true);
                }

                @Override
                public void onSuccess(String[] items) {
                    if (items.length > 0) {
                        setEnabledButtons(true);
                        Command listener = () -> savePurchaseInvoice(status);
                        alertStockItemsMessage(items, listener);
                    } else {
                        savePurchaseInvoice(status);
                    }
                }
            });
        } else {
            savePurchaseInvoice(status);
        }
    }

    private void savePurchaseInvoice(final String status) {
        final NewInvoice invoiceData = viewInterface.getFormData(status, true);

        if (!Constants.DRAFT.equals(status) && invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }
        if (Constants.APPROVE.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    saveData(invoiceData, status);
                }

                @Override
                public void onCancel() {
                    setEnabledButtons(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.addCloseHandler(closeEvent -> setEnabledButtons(true));
            wfmMessageBox.open();
        } else {
            saveData(invoiceData, status);
        }
    }

    private void saveData(final NewInvoice invoiceData, String status) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().savePurchaseInvoice(invoiceData, new AbstractAsyncCallback<SaveResult>() {

            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                if (result.isInvoiceExist()) {
                    addExistingInvoiceNumberListener();
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.invoice()), Info.Type.WARNING);
                } else {
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.purchaseinvoice()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result.getId(), viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, viewInterface.getView());
                    if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.CONVERT_TO_INVOICE)
                            || viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_PO_TO_PI)
                            || viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_FROM_SI_TO_PI)
                    ) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, viewInterface.getView());
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, result.getFixedAssetID(), viewInterface.getView());
                    if (Constants.APPROVE.equals(status)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, result, viewInterface.getView());
                    }
                    viewInterface.getView().closeTab();


                    if (viewInterface.getFormParameters() != null && viewInterface.getFormParameters().getConvertFormType() != null) {
                        saveConvertedRelations(result.getId(), result.getNumber());
                    }
                }
            }
        });
    }

    private void update(final String status) {
        //validate base form fields
        if (!validation(status)) {
            setEnabledButtons(true);
            return;
        }

        //validate custom fields
        if (!Constants.DRAFT.equals(status) && !viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }

        final NewInvoice invoice = viewInterface.getFormData(status, true);

        if (!Constants.DRAFT.equals(status) && invoice.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }

        if (customerSupplierItem.isSubsidiary() != null && customerSupplierItem.isSubsidiary()) {
            InvoiceService.App.get().validateItemsInConsignment(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new AsyncCallback<String[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    setEnabledButtons(true);
                }

                @Override
                public void onSuccess(String[] items) {
                    if (items.length > 0) {
                        setEnabledButtons(true);
                        Command listener = () -> savePurchaseInvoice(status);
                        alertStockItemsMessage(items, listener);
                    } else {
                        updateData(invoice);
                    }
                }
            });
        } else if (Constants.APPROVE.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    updateData(invoice);
                }

                @Override
                public void onCancel() {
                    setEnabledButtons(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.addCloseHandler(closeEvent -> setEnabledButtons(true));
            wfmMessageBox.open();
        } else {
            updateData(invoice);
        }
    }

    public void pdfVersion(final Panel hp, Integer templateId) {
        //validate base form fields
        if (!validation(null)) {
            return;
        }
        //validate custom fields
        if (!viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }
        generatePDF(hp, templateId);
    }

    private void updateData(final NewInvoice invoice) {
        invoice.setID(viewInterface.getObjectID());
        LoadingPanel.loading(true);
        setEnabledButtons(false);
        invoice.setOldStatus(oldStatus);

        QuantityItem[] quantityItems = viewInterface.getProductTable().getQuantityItemsForValidateStockInconsistency();
        if (!DRAFT.equals(invoice.getStatusCode()) && quantityItems.length > 0) {
            validateForInconsistencyAndUpdate(invoice, quantityItems);
        } else {
            updateInvoiceData(invoice);
        }
    }

    void validateForInconsistencyAndUpdate(final NewInvoice invoice, QuantityItem[] quantityItems) {
        InvoiceService.App.get().validateStockInconsistencyInAdjustProcess(StockTransactionType.PURCHASE_INVOICE, invoice.getID(), quantityItems, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                setEnabledButtons(true);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    updateInvoiceData(invoice);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                    LoadingPanel.loading(false);
                    setEnabledButtons(true);
                }
            }
        });
    }

    void updateInvoiceData(final NewInvoice invoice) {
        InvoiceService.App.get().updatePurchaseInvoice(invoice, new AbstractAsyncCallback<SaveResult>() {
            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                if (result.isInvoiceExist()) {
                    addExistingInvoiceNumberListener();
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.invoice()), Info.Type.WARNING);
                } else if (result.hasStockValidation()) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(result.getMessage());
                    messageBox.open();
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result, viewInterface.getView());
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.purchaseinvoice()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASE_INVOICE_APPROVAL, result, viewInterface.getView());
                    viewInterface.getView().closeTab();
                }
            }
        });
    }

    private void generatePDF(Panel hp, Integer pdfTemplateID) {
        String pdfURL = CommandConstants.PDF_URL + "/purchaseInvoiceViewPDFHandler";
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        NewInvoice invoiceData = viewInterface.getFormData(Constants.DRAFT, true);
        invoiceData.setID(viewInterface.getObjectID());
        invoiceData.setPdfTemplateID(pdfTemplateID);
        new PDFTransferObject(post, invoiceData);
        post.submit();
    }

    public boolean validation(String status) {
        int errors = 0;


        if (newInvoice.getPaidAmount() != null && viewInterface.getProductTable().getTotalInInvoiceCurrency() != null && newInvoice.getPaidAmount().compareTo(viewInterface.getProductTable().getTotalInInvoiceCurrency()) > 0 && !AccountingConstants.COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())) {
            Info.show(accountingStrings.paymentAmountCannotMoreInvoiceAmount(), Info.Type.WARNING);
            return false;
        }

        errors += !Validation.validateLookUpRequired(viewInterface.getCrmAccountLookUp()) ? 1 : 0;

        if (Constants.DRAFT.equals(status)) {
            return errors == 0;
        }

        if (!viewInterface.isReccuringInvoice()) {
            errors += !Validation.validateTextBoxRequired(viewInterface.getNumberTxtBox()) ? 1 : 0;
        }

        errors += !viewInterface.getTermsAndDueDatePanel().validate() ? 1 : 0;

        errors += viewInterface.validateSystemCustomFields() ? 0 : 1;

        if (!viewInterface.validateCustomFields()) {
            errors++;
        }

        if (viewInterface.getPlaceOfSupplyWidget() != null) {
            if (!viewInterface.getPlaceOfSupplyWidget().validate()) {
                errors++;
            }
        }

        if (!viewInterface.getProductTable().validation(status)) {
            errors++;
            Utils.scrollToTop();
        }

        if (viewInterface.getRecurringWidget() != null && !viewInterface.getRecurringWidget().validate()) {
            errors++;
        }
        if (newInvoice.isApprover() && newInvoice.getPaidAmount() == null) {
            if (!viewInterface.getApproverLookUp().isValid()) {
                errors++;
            }
        }
        if (viewInterface.getDatePicker().getDate() != null) {
            DateUtil.resetTime(viewInterface.getDatePicker().getDate());
        }
        if (viewInterface.getTermsAndDueDatePanel().getDueDate() != null) {
            DateUtil.getDayLastTime(viewInterface.getTermsAndDueDatePanel().getDueDate());
        }
        if (viewInterface.getDatePicker().getDate() != null && viewInterface.getTermsAndDueDatePanel().getDueDate() != null &&
                !Validation.validateDateOrder(viewInterface.getDatePicker().getDate(), viewInterface.getTermsAndDueDatePanel().getDueDate())) {

            viewInterface.getDatePicker().addStyleName(ERROR_FORM_STYLE);
            viewInterface.getDatePicker().addChangeHandler(event -> {
                try {
                    DateUtils.parse(viewInterface.getDatePicker().getText(), DateUtils.getFormat());
                    viewInterface.getDatePicker().removeStyleName(ERROR_FORM_STYLE);
                } catch (DateFormatException ignored) {
                }
            });
            Utils.scrollIntoView(viewInterface.getDatePicker().getElement());
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, viewInterface.getProperty().getSingular(accountingStrings.checkInvoiceDate(), accountingStrings.invoice()));
            messageBox.open();
            return false;
        }

        if (!viewInterface.validateProjectMandatory()) {
            errors++;
        }

        if (errors > 0) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.unableToSave());
            messageBox.open();
            return false;
        }

        if (!viewInterface.getFormParameters().isFromGettingStarted() && viewInterface.getConversionDate().after(viewInterface.getTermsAndDueDatePanel().getDueDate())) {
            Info.show(viewInterface.getProperty().getSingular(accountingStrings.invoiceDateShouldBeAfterConversationDate(), accountingStrings.invoice()), Info.Type.WARNING);
            return false;
        }

        if (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Invoice", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (!validateApplicableTaxTypeForPurchase()) {
            Info.show("Selected VAT is not applicable for the VAT treatment of this transaction.", Info.Type.WARNING);
            errors++;
        }
        if (!validateApplicableTypeForUK()) {
            Info.show("The tax rate for export of goods/services outside the UK should be Zero.", Info.Type.WARNING);
            errors++;
        }
        return errors == 0;
    }

    public void addExistingInvoiceNumberListener() {
        viewInterface.getNumberTxtBox().setStyleName("x-form-invalid");
        viewInterface.getNumberTxtBox().addKeyDownHandler(event -> {
            if (!"".equals(viewInterface.getNumberTxtBox().getStyleName())) {
                viewInterface.getNumberTxtBox().removeStyleName(viewInterface.getNumberTxtBox().getStyleName());
            }
        });
    }

    private void setRelatedPriceLevel(Integer clientID, final Integer priceLevelID) {
        viewInterface.getPriceLevelDropdown().clear();

        if (clientID != null) {
            ClientService.App.get().getClientPriceLevels(clientID, new AbstractAsyncCallback<PriceLevelItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void success(PriceLevelItem[] priceLevelItems) {
                    if (priceLevelItems != null && priceLevelItems.length > 0) {
                        viewInterface.getPriceLevelDropdown().setItems(priceLevelItems);
                        if (priceLevelID != null) {
                            viewInterface.getPriceLevelDropdown().setSelected(priceLevelID);
                        } else {
                            if (priceLevelItems.length == 1) {
                                viewInterface.getPriceLevelDropdown().setSelected(priceLevelItems[0].getId());
                            }
                        }
                        viewInterface.getPriceLevel().setVisible(true);
                    } else {
                        viewInterface.getPriceLevelDropdown().clearSelected();
                        viewInterface.getPriceLevel().setVisible(false);
                    }
                    onChangePriceLevel(newInvoice);
                }
            });
        } else {
            viewInterface.getPriceLevel().setVisible(false);
        }
    }

    private void onShellOk(String status, Integer invoiceID, Integer fixedAssetID) {
        if (!viewInterface.getFormParameters().isFromGettingStarted()) {
            viewInterface.getView().closeTab();

            if (fixedAssetID != null) {
                viewInterface.getView().goTo("fixedasset|summary/" + fixedAssetID);
            } else {

                if (!Constants.DRAFT.equals(status)) {
                    if (viewInterface.isReccuringInvoice()) {
                        viewInterface.getView().goTo(Constants.RECURRING_BILL + "|summary/" + invoiceID);
                    } else {
                        if (viewInterface.getNumberData() != null) {
                            viewInterface.getView().goTo(Constants.PURCHASE_INVOICE + "|summary/" + invoiceID, viewInterface.getNumberData().getInvoiceNumber());
                        }
                    }
                }
            }
        } else {
            viewInterface.getView().closeTab();
        }
    }

    private void setEnabledButtons(boolean b) {
        if (viewInterface.getSaveButton() != null) {
            viewInterface.getSaveButton().setEnabled(b);
        }
        if (viewInterface.getApproveButton() != null) {
            viewInterface.getApproveButton().setEnabled(b);
        }
        if (viewInterface.getSubmitButton() != null) {
            viewInterface.getSubmitButton().setEnabled(b);
        }
    }

    private void alertStockItemsMessage(String[] items, final Command listener) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0;
             i < items.length;
             i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setWidth(560);
        messageBox.setTitle("Not enough quantity");
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughConsignmentQuantity(itemNames.toString()));
        messageBox.open();
    }

    private void saveConvertedRelations(Integer _objectId, String number) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_PURCHASE_INVOICE, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number));
        AllInOneService.App.get().getAdditionalRelations(_objectId, RelationItem.TYPE_PURCHASE_INVOICE, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<RelationItem> result) {
                if (result != null && result.size() > 1) {
                    result.remove(0);
                    for (RelationItem item_ : result) {
                        boolean haveRelationItem = false;

                        if (newInvoice.getConvertedRelations() != null) {
                            for (RelationItem item : newInvoice.getConvertedRelations()) {

                                if (item.getToType().equals(item_.getToType()) && item.getToID().equals(item_.getToID())) {
                                    haveRelationItem = true;
                                    break;
                                }
                            }
                        }
                        if (!haveRelationItem) {
                            relationItems.add(item_);
                        }
                    }
                }
                if (relationItems != null && relationItems.size() > 0) {
                    AllInOneService.App.get().saveRelations(RelationItem.TYPE_PURCHASE_INVOICE, _objectId, number, relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(ArrayList<RelationItem> selectItems) {
                            LoadingPanel.loading(false);
                        }
                    });
                }
            }
        });
    }

    private void onChangeInvoiceNumber(TypeItem typeItem) {
        InvoiceService.App.get().generateNewNumberData(Constants.PURCHASE_INVOICE, new DateNonConvertable(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date()), new AbstractAsyncCallback<InvoiceNumberData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(InvoiceNumberData result) {
                super.success(result);
                applyInvoiceNumberData(result, typeItem != null ? typeItem : newInvoice.getTypeItem());
            }
        });
    }

    private void setPurchaseInvoiceCustomFields() {
        invoiceService.getInvoiceCustomFieldItems(viewInterface.getCrmAccountLookUp().getSelectedItemID(), ViewName.PurchaseInvoice, new AsyncCallback<NewInvoice>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(NewInvoice result) {
                viewInterface.initCustomFields(result);
            }
        });
    }
}
