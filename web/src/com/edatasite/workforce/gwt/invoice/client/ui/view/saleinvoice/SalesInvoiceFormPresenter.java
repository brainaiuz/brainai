package com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.ProductLookUp;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.CustomerCreditLimitExceedPopup;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Printer;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.SplitButtonItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.core.client.ui.wfmDropdown.listener.DropdownListener;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceNumberData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PDFTransferObject;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.saleorderbaseinvoice.SaleOrderBaseInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceQuoteFormPresenter;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.ProductsTable;
import com.edatasite.workforce.gwt.profile.client.ui.view.PaymentTermsConditionsUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/26/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesInvoiceFormPresenter extends InvoiceQuoteFormPresenter implements Constants, AccountingConstants {

    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private final ClientServiceAsync clientService = ClientService.App.get();

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM dd, yyyy");

    private final SalesInvoiceViewInterface viewInterface;
    private NewInvoice newInvoice;
    private Date projectBasedInvoiceStartDate;
    private Date projectBasedInvoiceEndDate;
    private Command approveCommand;
    private Command approveAndSendCommand;
    private SelectItem paymentInstruction = null;
    private boolean isCopyPaymentInstruction;
    private final Set<Integer> expItemsConvertedAsLineItem = new HashSet<>();

    SalesInvoiceFormPresenter(SalesInvoiceViewInterface viewInterface) {
        super(viewInterface);
        this.viewInterface = viewInterface;
    }

    public void bindUI() {

        initBankAccountItems(null);

        requestAndSetConversionDate();

        if (!viewInterface.isEditForm()) {
            if ("lead".equals(viewInterface.getFormParameters().getCrmFormName())) {
                createClientFromLead();
            } else if ("account".equals(viewInterface.getFormParameters().getCrmFormName()) || "contact".equals(viewInterface.getFormParameters().getCrmFormName())) {
                createClientFromCrmAccount();
            } else {
                loadData();
            }
        } else {
            loadData();
        }

        addFormListeners();
    }

    private void initFormHandlers() {
        viewInterface.getCrmAccountLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null) {
                if (viewInterface.getCrmAccountLookUp().getSelectedItem().isNewItem() || viewInterface.getCrmAccountLookUp().getSelectedItem().getName().contains("<html><font color=")) {
                    viewInterface.getCrmAccountLookUp().clear();
                    Info.warn(accountingStrings.youCantBlockedCustomer());
                } else {
                    onChangeClientHandler(false);

                    if (viewInterface.isEditForm() && GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                        invoiceService.getClientOrSupplier(viewInterface.getCrmAccountLookUp().getSelectedItemID(), Constants.RECEIVABLE, new AbstractAsyncCallback<TypeItem>() {
                            public void success(TypeItem typeItem) {
                                customerSupplierItem = typeItem;
                                configurePlaceOfSupply(typeItem, typeItem.getPlaceOfSupply(), viewInterface.getPlaceOfSupplyWidget());
                            }
                        });
                    }
                }
            }
            viewInterface.getProductTable().clearProjectFromLineItems();

            invoiceService.getClientOrSupplier(viewInterface.getCrmAccountLookUp().getSelectedItemID(), Constants.RECEIVABLE, new AbstractAsyncCallback<TypeItem>() {
                public void failure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void success(TypeItem typeItem) {
                    if (typeItem.getTermsItem() != null) {
                        viewInterface.getTermsAndDuePanel().applyCustomerTerms(typeItem.getTermsItem());
                    }
                }
            });
        });

        Validation.addNumericKeyboardListener(viewInterface.getPreviousBalance(), 2);
        Validation.addNumericKeyboardListener(viewInterface.getPaymentsReceived(), 2);

        viewInterface.getPreviousBalance().addKeyUpHandler(keyUpEvent -> calculatePayAdjustment());
        viewInterface.getPaymentsReceived().addKeyUpHandler(keyUpEvent -> calculatePayAdjustment());
        viewInterface.getPriceLevelDropdown().addValueChangeHandler(vch -> onChangePriceLevel(newInvoice));
        viewInterface.getClientDiscountDropdown().addEventHandler(new DropdownListener() {
            @Override
            public void itemSelected() {
                onChangeClientDiscount();
            }

            @Override
            public void saveNewItem() {
            }
        });
        viewInterface.getDatePicker().addChangeHandler(event -> {
            if (newInvoice.getDueDays() != null) {
                viewInterface.getTermsAndDuePanel().setData(TermsAndDuePanel.DUE_TYPE, DateUtil.addDays(viewInterface.getDatePicker().getDate(),
                        newInvoice.getDueDays()), null);
            }
            generatePaymentInstruction(null, dateFormat.format(viewInterface.getDatePicker().getDate()));
            if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithDate()) {
                viewInterface.getNumberData().setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getDatePicker().getDate()));
                viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                generatePaymentInstruction(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate()));
            }
            if (!viewInterface.isEditForm()) {
                onChangeInvoiceNumber();
            }
        });
        viewInterface.getProjectLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject()) {
                viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                generatePaymentInstruction(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate()));
            }
        });
        viewInterface.getNumberTxtBox().addChangeHandler(event -> generatePaymentInstruction(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate())));
        viewInterface.getTermsAndDuePanel().setTermsAndDueProvider(new TermsAndDueProvider() {
            @Override
            public void setDueDateAndTermsLabel(String text) {
                if (viewInterface.getTermsAndDueDateLabel() != null) {
                    HTML textChange = (HTML) viewInterface.getTermsAndDueDateLabel().getWidget(0);
                    MaterialLink link = (MaterialLink) viewInterface.getTermsAndDueDateLabel().getWidget(1);
                    textChange.setText(text);
                    link.setVisible(!viewInterface.getTermsAndDuePanel().isDueTypeSelected());
                }
            }

            @Override
            public Date getInvoiceDate() {
                return viewInterface.getDatePicker().getDate();
            }

            @Override
            public void applyPaymentInstructionData() {
                generatePaymentInstruction(null, dateFormat.format(viewInterface.getDatePicker().getDate()));
            }

            @Override
            public boolean isEditForm() {
                return viewInterface.isEditForm();
            }
        });

        viewInterface.getPeriodStart().addChangeHandler(event -> generatePaymentInstruction(null, dateFormat.format(viewInterface.getDatePicker().getDate())));

        viewInterface.getPeriodEnd().addChangeHandler(event -> generatePaymentInstruction(null, dateFormat.format(viewInterface.getDatePicker().getDate())));

        viewInterface.getTaxCalcListBox().addValueChangeHandler(changeEvent -> viewInterface.getProductTable().onTaxCalculationTypeChange(viewInterface.getTaxCalcListBox().getSelectedId(), true));

        viewInterface.getProductTable().getPaymentTermsConditionsListBox().addValueChangeHandler(changeEvent -> generatePaymentInstruction(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate())));

            registerEventHandlers();
            viewInterface.getExpenseMarkupPopup().setExpensesButtonListener(() -> {
                if (viewInterface.getExpenseMarkupPopup().validate()) {
                    ArrayList<NewInvoiceItem> invoiceItems = collectValidItemsForItemTable(viewInterface.getExpenseMarkupPopup().getExpanseItemsAsLineItems());
                    if (invoiceItems.size() > 0) {
                        deleteEmptyAndInValidRowsFromItemTable();
                    }
                    addExpanceItemsToItemTableAsLineItem(invoiceItems);
//                loadBillableExpenseTotal();
                    viewInterface.getProductTable().calculate(true);
                    viewInterface.getExpenseMarkupPopup().close();
                }
            });

        viewInterface.getExpenseMarkupPopup().setCancelButtonListener(() -> {
            if (viewInterface.isEditForm()) {
                boolean oldClient = newInvoice.getClientID().equals(viewInterface.getCrmAccountLookUp().getSelectedItemID());
                if (!oldClient) {
                    viewInterface.getExpenseMarkupPopup().clearAllData();
                }
            } else {
                viewInterface.getExpenseMarkupPopup().clearAllData();
            }
            viewInterface.getExpenseMarkupPopup().remove();
        });

        viewInterface.getCustomerBalanceLink().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER,
                wfmStrings.balance() + ": " + viewInterface.getCrmAccountLookUp().getSelectedItem().getName()));

        viewInterface.getCurrencyWidget().addListener(() -> {
            //viewInterface.getAccountsReceivablePayableLookUp().clear();
            viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(viewInterface.getCurrencyWidget().getCurrencyID());

            if (viewInterface.getBillableExpenseButton().isVisible()) {
                viewInterface.getExpenseMarkupPopup().onCurrencyChange(viewInterface.getCurrencyWidget().getCurrencyID(), viewInterface.getCurrencyWidget().getExchangeRate());

                if (viewInterface.getProductTable().hasBillableExp()) {
                    loadBillableExpenseTotal();
                }
            }
        });
        if (viewInterface.isEditForm()) {
            if (newInvoice.getPaidAmount() != null && !(newInvoice.isSubmitter(Utils.getUserID()) || Utils.hasRoles(Constants.ADMIN))) {
                viewInterface.getApproverLookUp().setEnabled(false);
            }
            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                update(Constants.DRAFT, false);
            });
            //for Approve
            approveCommand = () -> {
                setEnabledButtons(false);
                update(Constants.APPROVE, false);
            };
            //for Submit
            viewInterface.getSubmitButton().addClickHandler(event -> {
                if (newInvoice.getPaymentItems().length == 0) {
                    setEnabledButtons(false);
                    update(Constants.SUBMITTED_TO_MANAGER, false);
                } else {
                    Info.warn(accountingStrings.cantEditInvoiceWithPayments());
                }
            });
            //for Approve And Email
            approveAndSendCommand = () -> {
                setEnabledButtons(false);
                update(Constants.OPEN, true);
            };
        } else {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                save(Constants.DRAFT);
            });
            //for Approve
            approveCommand = () -> {
                setEnabledButtons(false);
                save(Constants.APPROVE);
            };
            //for Submit
            viewInterface.getSubmitButton().addClickHandler(event -> {
                setEnabledButtons(false);
                save(Constants.SUBMITTED_TO_MANAGER);
            });
            //for Approve And Email
            approveAndSendCommand = () -> {
                setEnabledButtons(false);
                save(Constants.OPEN);
            };
        }

        /*viewInterface.getBillableExpenseButton().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("billableExpense|expense/" + viewInterface.getCrmAccountLookUp().getSelectedItemID(), "Add Billable Expense", "billable"));*/

        viewInterface.getBillableExpenseButton().addClickHandler(clickEvent -> viewInterface.getExpenseMarkupPopup().open());

        {
            //for PDf
            Integer defaultTemplateId = null;
            List<SplitButtonItem> pdfButtonItems = new ArrayList<>();

            if (newInvoice != null
                    && newInvoice.getPdfTemplateList() != null
                    && newInvoice.getPdfTemplateList().getItems() != null) {
                newInvoice.getPdfTemplateList().getItems();
                for (SelectItem pdfItem : newInvoice.getPdfTemplateList().getItems()) {

                    if (pdfItem.isDefaultSelected()) {
                        defaultTemplateId = pdfItem.getId();
                    }
                    pdfButtonItems.add(new SplitButtonItem("PDF_TEMPLATE_" + pdfItem.getId(), pdfItem.getName(), () -> pdfVersion(viewInterface.getHTMLPanel(), pdfItem.getId(), false)));
                }
            }
            Integer finalDefaultTemplateId = defaultTemplateId;
            pdfButtonItems.add(new SplitButtonItem("PDF_VERSION", wfmStrings.pdfVersion(), () -> pdfVersion(viewInterface.getHTMLPanel(), newInvoice.getPdfTemplateID() != null ? newInvoice.getPdfTemplateID() : finalDefaultTemplateId, false), true));

            if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PACK_SHIP_PDF_FOR_SALE_INVOICE) && newInvoice != null) {
                pdfButtonItems.add(new SplitButtonItem(PACKING_SLIP, accountingStrings.packingSlip(), () -> pdfVersion(viewInterface.getHTMLPanel(), null, true), false));
                pdfButtonItems.add(new SplitButtonItem("SHIPPING_LABEL", accountingStrings.shippingLabel(), () -> {
                    if (viewInterface.getObjectID() == null) {
                        WfmWindow.alert(accountingMessages.pleaseSaveInvoiceFirst());
                        return;
                    }
                    viewInterface.setShippingLabelDialogBox(new ShippingLabelDialogBox(viewInterface.getObjectID()));
                    viewInterface.getShippingLabelDialogBox().open();
                }, false));
            }

            if (Utils.hasRoles(Constants.ADMIN)) {
                pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.SALES_INVOICE.name())));
            }

            viewInterface.getSplitButtonPdf().addItemList(pdfButtonItems);
            //End Pdf
        }
    }

    private ArrayList<NewInvoiceItem> collectValidItemsForItemTable(ArrayList<NewInvoiceItem> expanseItemsAsLineItems) {
        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();
        expanseItemsAsLineItems.forEach(item -> {
            if (!expItemsConvertedAsLineItem.contains(item.getExpanceItemId())) {
                invoiceItems.add(item);
            }
        });
        return invoiceItems;
    }

    private void deleteEmptyAndInValidRowsFromItemTable() {
        EditableTable editableTable = viewInterface.getProductTable().getItemsTable();
        for (int i = editableTable.getRowCount() - 1; i >= 0; i--) {
            final Widget widget = editableTable.getColumnById(i, ProductsTable.PRODUCT);
            if (widget == null) {
                editableTable.getGrid().getModel().removeRow(i);
                continue;
            }
            if (widget instanceof ProductLookUp) {
                ProductLookUp productsTable = (ProductLookUp) widget;
//                GWT.log("produst select item name " + (productsTable.getSelectedItem() != null ? productsTable.getSelectedItem().getName() : " null ") + productsTable.getText() + " is empty " + productsTable.getText().isEmpty());
                if ((productsTable.getSelectedItem() == null || productsTable.getSelectedItem().getName() == null) && "Type here to search...".equals(productsTable.getText())) {
                    editableTable.getGrid().getModel().removeRow(i);
                }
            }
        }
    }

    private void addExpanceItemsToItemTableAsLineItem(ArrayList<NewInvoiceItem> invoiceItems) {
        EditableTable editableTable = viewInterface.getProductTable().getItemsTable();
        for (int i = 0; i < invoiceItems.size(); i++) {
            NewInvoiceItem invoiceItem = invoiceItems.get(i);
            if (invoiceItem.getFullItemName() == null && invoiceItem.getItemName() != null) {
                invoiceItem.setFullItemName(invoiceItem.getItemName());
            }
            editableTable.addRow(viewInterface.getProductTable().getWidgets(invoiceItem));
            expItemsConvertedAsLineItem.add(invoiceItem.getExpanceItemId());
        }
    }
private void showMessageToUser() {
    viewInterface.getMessageToUser().setVisible(true);
}

    private void registerEventHandlers() {
        EditableTable editableTable = viewInterface.getProductTable().getItemsTable();
        editableTable.setRemoveRowListener(() -> {
            if (editableTable.getGrid().getRowCount() > 1) {
                int gridRow = editableTable.getGrid().getCurrentRow();
                NewInvoiceItem invoiceItem = viewInterface.getProductTable().getDataItems(Constants.DRAFT)[gridRow];
                editableTable.getGrid().getModel().removeRow(gridRow);
                if (invoiceItem != null && invoiceItem.getExpanceItemId() != null && expItemsConvertedAsLineItem.contains(invoiceItem.getExpanceItemId())) {
                    expItemsConvertedAsLineItem.remove(invoiceItem.getExpanceItemId());
                    viewInterface.getExpenseMarkupPopup().changeCheckBoxValue(invoiceItem.getExpanceItemId(), Boolean.TRUE);
                    loadBillableExpenseTotal();
                }
                viewInterface.getProductTable().calculate();
                viewInterface.getProductTable().reDrawTaxesDropdown();

            } else {
                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
            }
        });
    }

    private void selectExpenceItems() {
        NewInvoiceItem[] newInvoiceItems = viewInterface.getProductTable().getDataItems(Constants.DRAFT);
        expItemsConvertedAsLineItem.clear();
        for (NewInvoiceItem item : newInvoiceItems) {
            if (item != null && item.getExpanceItemId() != null) {
                expItemsConvertedAsLineItem.add(item.getExpanceItemId());
                viewInterface.getExpenseMarkupPopup().changeCheckBoxValue(item.getExpanceItemId(), Boolean.FALSE);
            }
        }
    }

    private void onChangeInvoiceNumber() {
        InvoiceService.App.get().generateNewNumberData(Constants.SALE_INVOICE, new DateNonConvertable(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date()), new AbstractAsyncCallback<InvoiceNumberData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(InvoiceNumberData result) {
                super.success(result);
                applyInvoiceNumberData(result, newInvoice.getTypeItem());
            }
        });
    }

    private void loadData() {
        InvoiceService.App.get().getAllInvoiceData(viewInterface.getFormParameters(), new AbstractAsyncCallback<NewInvoice>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NewInvoice result) {
                newInvoice = result;
                viewInterface.initCustomFields(result);
                viewInterface.initSystemCustomFields(result);
                viewInterface.initPdfTemplates(result);
                viewInterface.getProductTable().setFromMultiQuoteConvert(CONVERT_MULTI_QUOTE_TO_INVOICE.equals(viewInterface.getFormParameters().getExternalFormID()));
                viewInterface.getProductTable().setRoundingModeDisabled(result.isRoundingModeDisabled());
                viewInterface.getProductTable().setDoubleTaxEnabled(result.isDoubleTaxEnabled());
                viewInterface.getProductTable().setConvertedQuoteIds(result.getConvertedQuoteIDs());
                viewInterface.getProductTable().setItemCustomFields(result.getItemCustomFields());
                viewInterface.initProductsTableData(result);
                initFormHandlers();
                if (!(viewInterface.isEditForm() || viewInterface.getFormParameters().isRecurringInvoice())) {
                    applyInvoiceNumberData(result.getNumberData(), result.getTypeItem());
                }

                if ((viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject() && result.getRelatedProject() != null) &&
                        (PROGRESS_INVOICING.equals(viewInterface.getFormParameters().getExternalFormID()) || COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID()))) {
                    viewInterface.getNumberData().setProjectCode(result.getRelatedProject().getDescription());
                    viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                }

                viewInterface.initWidgetMap(result);
                viewInterface.generateForm(result.getLayoutHTML());
                viewInterface.setFormData(result);
                renderButtons(result);

                viewInterface.getExpenseMarkupPopup().setValues(newInvoice.getExpenses(), newInvoice.getCurrencyID(), newInvoice.getExchageRate());

                if (newInvoice.getExpenses() != null && !newInvoice.getExpenses().isEmpty()) {
                    viewInterface.getBillableExpenseButton().setVisible(true);
                    viewInterface.getProductTable().setMarkupWidgets(viewInterface.getExpenseMarkupPopup().getMarkupWidgets());
                    viewInterface.getProductTable().calculate();
                }
                customerSupplierItem = result.getTypeItem();
                viewInterface.getProductTable().setNewInvoice(newInvoice);

                setRelatedPriceLevel(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);
                setRelatedClientDiscount(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getClientDiscount() != null ? result.getClientDiscount().getId() : null);

                if (result.getBankAccount() != null) {
                    initBankAccountItems(result.getBankAccount().getId());
                } else if (result.getTypeItem() != null) {
                    initBankAccountItems(result.getTypeItem().getBankAccountID());
                } else if (result.getBankAccountItem() != null) {
                    initBankAccountItems(result.getBankAccountItem().getObjectId());
                }

                getAndApplyPaymentInstruction(result);
                getAndApplyIntroduction(result);

                if (!viewInterface.isEditForm() && viewInterface.getFormParameters().getRelatedProjectID() != null) {
                    onChangeClientHandler(false);
                } else if (newInvoice.getID() == null && viewInterface.getCrmAccountLookUp().getSelectedItemID() != null && (newInvoice != null && newInvoice.isMultiQuoteConvertEnabled())) {
                    setClientData(viewInterface.getCrmAccountLookUp().getSelectedItemID(), true, viewInterface.getProjectLookUp().getSelectedItem());
                }

                if (viewInterface.onProjectBaseInvoiceInit() != null) {
                    viewInterface.onProjectBaseInvoiceInit().execute();
                }

                if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_BASE_INVOICE_LOADED, result, viewInterface.getView());
                }
                selectExpenceItems();
                LoadingPanel.loading(false);
            }
        });
    }

    private void renderButtons(NewInvoice result) {
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        if (!viewInterface.getFormParameters().isFromGettingStarted()) {
            SplitButtonItem emailButtonItem;
            if (viewInterface.getFormParameters().isEditForm()) {
                String statusCode = result.getStatusCode();
                if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || (SUBMITTED_TO_MANAGER.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                    if (DRAFT.equals(statusCode) || OPEN.equals(statusCode) || APPROVE.equals(statusCode) || OVER_DUE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode) || PAID.equals(statusCode) || (MANAGER_REJECT.equals(statusCode) && result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_SAVE_APPROVE) || AccountingSinksContainer.isHashAccessForPMRole) {
                            SplitButtonItem updateButtonItem = new SplitButtonItem(APPROVE, accountingStrings.updateAndApprove(), approveCommand, true);
                            updateButtonItem.ensureDebugId("updateButtonItem_invoice");
                            splitButtonItems.add(updateButtonItem);
                        }
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_APPROVE_SENT) || AccountingSinksContainer.isHashAccessForPMRole) {
                            emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, false);
                            emailButtonItem.ensureDebugId("emailButtonItem_invoice");
                            splitButtonItems.add(emailButtonItem);
                        }
                    }
                    if ((SUBMITTED_TO_MANAGER.equals(statusCode) && result.isSubmitter(Utils.getUserID()))
                            || (SUBMITTED_TO_MANAGER.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))
                            || (MANAGER_REJECT.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                        viewInterface.getApproveSplitButton().setVisible(false);
                    }
                    if (viewInterface.getApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp() != null
                            && viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItem() != null
                            && !viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItem().getId().equals(Utils.getUserID())) {
                        viewInterface.getSubmitButton().setVisible(true);
//                        viewInterface.getApproveSplitButton().setVisible(false);
                    } else {
                        viewInterface.getApproveSplitButton().setVisible(true);
                    }
                } else {
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode)) {
                        viewInterface.getSaveButton().setVisible(true);
                    }
                    if (DRAFT.equals(statusCode) || OPEN.equals(statusCode) || APPROVE.equals(statusCode) || OVER_DUE.equals(statusCode) || PAID.equals(statusCode)) {
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_SAVE_APPROVE) || AccountingSinksContainer.isHashAccessForPMRole) {
                            SplitButtonItem updateButtonItem = new SplitButtonItem(APPROVE, accountingStrings.updateAndApprove(), approveCommand, true);
                            updateButtonItem.ensureDebugId("updateButtonItem_invoice");
                            splitButtonItems.add(updateButtonItem);
                        }
                        if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_APPROVE_SENT) || AccountingSinksContainer.isHashAccessForPMRole) {
                            emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, false);
                            emailButtonItem.ensureDebugId("emailButtonItem_invoice");
                            splitButtonItems.add(emailButtonItem);
                        }
                    }
                }
            } else {
                if (!COPY_FROM_FIXED_ASSET.equals(viewInterface.getFormParameters().getExternalFormID())) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_SAVE_APPROVE) || AccountingSinksContainer.isHashAccessForPMRole) {
                    SplitButtonItem saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                    saveButtonItem.ensureDebugId("saveButtonItem-invoice");
                    splitButtonItems.add(saveButtonItem);
                }
                if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_APPROVE_SENT) || AccountingSinksContainer.isHashAccessForPMRole) {
                    emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, false);
                    emailButtonItem.ensureDebugId("emailButtonItem");
                    splitButtonItems.add(emailButtonItem);
                }
            }
        } else {
            if (Utils.hasPermission(PermissionConstants.ACCOUNTING_SI_SAVE_APPROVE) || AccountingSinksContainer.isHashAccessForPMRole) {
                splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true));
            }
        }
        viewInterface.getApproveSplitButton().addItemList(splitButtonItems);
    }

    private void createClientFromLead() {
        //send lead Id to the server. In server we get the lead basing on this id and create the client.
        clientService.createClientFromLead(viewInterface.getFormParameters().getExternalObjectID(), null, new AbstractAsyncCallback<TypeItem>() {
            public void success(TypeItem client) {
                viewInterface.getCrmAccountLookUp().addItem(client);
                viewInterface.getCrmAccountLookUp().getTextBox().setEnabled(false);
                viewInterface.getCrmAccountWidgets().presenter.initContactAddress(client, true, Address.EntityType.CrmAccount);
                viewInterface.getFormParameters().setExternalObjectID(client.getId());
                loadData();
            }
        });

    }

    private void createClientFromCrmAccount() {
        //send accountID and ContactID to the server. In server we get the account and contact basing on this IDs and create the client.
        clientService.createClientFromCrmAccount(viewInterface.getFormParameters().getExternalObjectID(), null, false, new AbstractAsyncCallback<TypeItem>() {
            public void success(TypeItem client) {
                viewInterface.getCrmAccountLookUp().addItem(client);
                viewInterface.getCrmAccountLookUp().getTextBox().setEnabled(false);
                viewInterface.getCrmAccountWidgets().presenter.initContactAddress(client, true, Address.EntityType.CrmAccount);
                viewInterface.getFormParameters().setExternalObjectID(client.getId());
                loadData();
            }
        });
    }

    private void setEnabledButtons(boolean b) {
        if (viewInterface.getSaveButton() != null) {
            viewInterface.getSaveButton().setEnabled(b);
        }
        if (viewInterface.getApproveSplitButton() != null) {
            viewInterface.getApproveSplitButton().setEnabled(b);
        }
        if (viewInterface.getSubmitButton() != null) {
            viewInterface.getSubmitButton().setEnabled(b);
        }
//        if (viewInterface.getApproveSplitButton() != null) {
//            viewInterface.getApproveSplitButton().setDisable(b);
//        }
//        if (viewInterface.getApproveAndSendButton() != null) {
//            viewInterface.getApproveAndSendButton().setEnabled(b);
//        }
    }

    private void initBankAccountItems(final Integer bankAccountId) {
        AccountingService.App.get().getBankAccountItemsForReference(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(SelectItem[] result) {
                viewInterface.getBankAccountListBox().setItems(result);
                if (bankAccountId != null) {
                    viewInterface.getBankAccountListBox().setSelected(bankAccountId);
                }
//                onBankAccountChange();
            }
        });
    }

    /*private void onBankAccountChange() {
        viewInterface.getBankAccountDetailLink().setVisible(viewInterface.getBankAccountListBox().getSelectedIndex() != 0);
    }*/

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, viewInterface.getView(), (sender, args) -> setClientData((Integer) args, false, null));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, viewInterface.getView(), (sender, args) -> setClientData((Integer) args, true, null));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, viewInterface.getView(), (sender, args) -> showMessageToUser());
    }

    private void calculatePayAdjustment() {
        if (!"".equals(viewInterface.getPreviousBalance().getText().trim()) && !"".equals(viewInterface.getPaymentsReceived().getText().trim())) {
            BigDecimal prevBalance = AccountingUtils.get().parseToBigDecimal(viewInterface.getPreviousBalance().getText().trim());
            BigDecimal receiveAmount = AccountingUtils.get().parseToBigDecimal(viewInterface.getPaymentsReceived().getText().trim());
            BigDecimal difference = prevBalance.subtract(receiveAmount);
            if (difference.compareTo(AccountingConstants.ZERO) >= 0) {
                viewInterface.getPayAdjusmentLabel().setText(AccountingUtils.get().formatPrice(difference));
            } else {
                viewInterface.getPayAdjusmentLabel().setText("(" + AccountingUtils.get().formatPrice(difference.abs()) + ")");
            }
        } else {
            viewInterface.getPayAdjusmentLabel().setText("");
        }
    }

    private void save(final String invoiceStatus) {
        //validate base form fields
        if (!validation(invoiceStatus)) {
            setEnabledButtons(true);
            return;
        }

        //validate custom fields
        if (!DRAFT.equals(invoiceStatus) && !viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }
        final NewInvoice invoiceData = viewInterface.getFormData(invoiceStatus.equals(Constants.OPEN) ? Constants.APPROVE : invoiceStatus, false);

        if (!Constants.DRAFT.equals(invoiceStatus) && invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }

        if (!Constants.DRAFT.equals(invoiceStatus)) {
            if (!viewInterface.getFormParameters().isProjectBasedInvoice()
                    && !SHIPPED.equals(newInvoice.getStatusCode())
                    && !PARTIAL_SHIPPED.equals(newInvoice.getStatusCode())
                    && !PARTIAL_SHIPPED.equals(newInvoice.getStatusCode())
                    && !(CONVERT_TO_INVOICE_FROM_GRN.equals(viewInterface.getFormParameters().getExternalFormID()))
                    && (viewInterface.getFormParameters().getMultiQuoteConvertItem() == null
                    || (viewInterface.getFormParameters().getMultiQuoteConvertItem() != null && !SaleOrderBaseInvoiceItem.GDN.equals(viewInterface.getFormParameters().getMultiQuoteConvertItem().getObjectType())))) {

                invoiceService.validateStockAvailability(viewInterface.getProductTable().getQuantityItemsForValidate(), null, StockOutFlow.FROM_SALE_INVOICE, invoiceData.getInvoiceDate(), new AbstractAsyncCallback<SelectItem[]>() {
                    public void failure(Throwable caught) {
                        setEnabledButtons(true);
                        showItemsValidationFailureMessage();
                    }

                    public void success(SelectItem[] items) {
                        if (items.length > 0) {
                            setEnabledButtons(true);
                            alertStockItemsMessage(items);
                            viewInterface.getProductTable().markAsError(items);
                        } else {
                            saveSaleInvoice(invoiceData, invoiceStatus);
                        }
                    }
                });
            } else {
                saveSaleInvoice(invoiceData, invoiceStatus);
            }
        } else if (Constants.DRAFT.equals(invoiceStatus) || newInvoice.getStatus() == null || "".equals(newInvoice.getStatus())) {
            saveSaleInvoice(invoiceData, invoiceStatus);
        } else {
            if (Constants.OPEN.equals(newInvoice.getStatus())) {
                sendToClient(viewInterface.getObjectID());
            } else {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo);
                message.setTitle(wfmStrings.warning());
                message.setMessage(viewInterface.getProperty().getSingular(accountingStrings.salesInvoiceAlreadyApproved(), wfmStrings.salesInvoice()));
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        if (!viewInterface.getFormParameters().isProjectBasedInvoice()) {
                            invoiceService.validateStockAvailability(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), StockOutFlow.FROM_SALE_INVOICE, invoiceData.getInvoiceDate(), new AbstractAsyncCallback<SelectItem[]>() {
                                public void failure(Throwable caught) {
                                    setEnabledButtons(true);
                                    showItemsValidationFailureMessage();
                                }

                                public void success(SelectItem[] items) {
                                    setEnabledButtons(true);
                                    if (items.length > 0) {
                                        alertStockItemsMessage(items);
                                    } else {
                                        updateSaleInvoice(invoiceData);
                                    }
                                }
                            });
                        } else {
                            updateSaleInvoice(invoiceData);
                        }
                    }
                });
                message.open();
            }
        }
    }

    /*private boolean validateSerials(String invoiceStatus, NewInvoice invoiceData) {
        if (Utils.isProductTableCustomizationEnable() && !RECURRING_INVOICE.equals(viewInterface.getInvoiceType())) {
            if (invoiceData.getItems() != null && invoiceData.getItems().length > 0) {
                for (NewInvoiceItem item : invoiceData.getItems()) {
                    if (item.getAssignedSerials() != null && item.getAssignedSerials().length > 0) {
                        for (ProductSerialItem serialItem : item.getAssignedSerials()) {
                            if (serialItem.getSerial() == null || serialItem.getSerial() == "") {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        return true;
    }*/

    private void saveSaleInvoice(final NewInvoice invoiceData, final String invoiceStatus) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.PRODUCT_SERIAL_ENABLED) && invoiceData.getProductSerialItems() != null && !invoiceData.getProductSerialItems().isEmpty()) {
            checkForAvailyBatchSerial(invoiceData.getProductSerialItems(), invoiceData, invoiceStatus);
        } else {
            saveSalesInvoice(invoiceData, invoiceStatus);
        }
    }

    private void checkForAvailyBatchSerial(HashMap<Integer, ArrayList<ProductSerialItem>> serialItems, final NewInvoice invoiceData, final String invoiceStatus) {
        InvoiceService.App.get().validateBatchSerials(serialItems, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(String[] result) {
                if (result != null && result.length > 0) {
                    alertSerialsMessage(result);
                    setEnabledButtons(true);
                } else {
                    saveSalesInvoice(invoiceData, invoiceStatus);
                }
            }
        });
    }

    private void alertSerialsMessage(String[] items) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.OK, true);
        messageBox.setTitle(wfmStrings.confirmationMessage());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughSerail(itemNames.toString()));
        messageBox.open();
    }

    private void saveSalesInvoice(NewInvoice invoiceData, String invoiceStatus) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE)) {
            invoiceService.validateItemsInConsignmentToSell(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new AbstractAsyncCallback<String[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    setEnabledButtons(true);
                }

                @Override
                public void onSuccess(String[] items) {
                    if (items.length > 0) {
                        setEnabledButtons(true);
                        saveSaleInvoiceInternal(invoiceData, invoiceStatus);
                        alertConsignmentItemsMessage(items);
                    } else {
                        saveSaleInvoiceInternal(invoiceData, invoiceStatus);
                    }
                }
            });
        } else {
            saveSaleInvoiceInternal(invoiceData, invoiceStatus);
        }
    }

    private void saveSaleInvoiceInternal(final NewInvoice invoiceData, final String invoiceStatus) {
        if (Constants.APPROVE.equals(invoiceStatus) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION,
                    Action.YesNo,
                    wfmStrings.doYouWantToSaveChanges(),
                    new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            saveData(invoiceData, invoiceStatus);
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
            saveData(invoiceData, invoiceStatus);
        }

    }

    private void saveData(final NewInvoice invoiceData, final String invoiceStatus) {
        LoadingPanel.loading(true);
        invoiceService.saveSaleInvoice(invoiceData, new AbstractAsyncCallback<SaveResult>() {
            @Override
            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(final SaveResult result) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);

                if (result == null) {
                    return;
                }
                if (result.isInvoiceExist()) {
                    addExistingInvoiceNumberListener();
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.invoice()), accountingStrings.nextNumberWillBeAutoGenerated());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            invoiceData.setForceValidNumberGenerate(true);
                            saveSaleInvoice(invoiceData, invoiceStatus);
                        }
                    });
                    messageBox.open();
                    return;
                }
                if (result.getExceededCreditLimit() && result.getRemainingBalance() != null && result.getRemainingBalance() != null) {
                    String message = accountingStrings.creditLimitExceeded().replace("{Customer}", viewInterface.getCrmAccountLookUp().getSelectedItem().getName()).replace("{Credit Limit}", AccountingUtils.get().formatPrice(result.getCreditLimit())).replace("{Remaining Balance + Invoice Amount}", AccountingUtils.get().formatPrice(result.getRemainingBalance().add(viewInterface.getTotalInBaseCurrency())));
                    if (result.isRestrictCreatingOrUpdatingInvoices()) {
                        CustomerCreditLimitExceedPopup popup = new CustomerCreditLimitExceedPopup(viewInterface.getCrmAccountLookUp().getSelectedItem().getId(), message, invoiceData, invoiceStatus);
                        popup.getSaveButton().addClickHandler(event -> {
                            if (popup.updateCreditLimit()) {
                                popup.close();
                                saveSaleInvoice(invoiceData, invoiceStatus);
                            }
                        });
                        return;
                    } else {
                        WfmMessageBox creditLimitExceedMessage = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo);
                        creditLimitExceedMessage.setMessage(message);
                        creditLimitExceedMessage.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onCancel() {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSubmit() {
                                invoiceData.setForceSave(true);
                                saveSaleInvoice(invoiceData, invoiceStatus);
                            }
                        });
                        creditLimitExceedMessage.open();
                        return;
                    }
                }
                viewInterface.setObjectID(result.getId());
                viewInterface.getFormParameters().setObjectID(result.getId());

                if (invoiceStatus.equals(Constants.OPEN)) {
                    viewInterface.getSaveButton().setVisible(false);
                    final WfmMessageBox continueButton = new WfmMessageBox(IconEnum.INFO, Action.OK, viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyApproved(), wfmStrings.salesInvoice()),
                            accountingStrings.getPropertyContinue(), new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            sendToClient(viewInterface.getObjectID());
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, viewInterface.getObjectID(), viewInterface.getView());
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                        }
                    });
                    continueButton.setTitle(wfmStrings.information());
                    continueButton.open();
                } else {

                    if (Utils.hasGenericAccess(GenericSettingsEnum.PAID_AND_PRINT_INVOICE) &&
                            invoiceData.getHtmlTemplateId() != null &&
                            invoiceData.getPaymentData() != null &&
                            result.getPdfTemplate() != null) {
                        Printer.openPrintWindow(result.getPdfTemplate());

                        viewInterface.getView().goTo(Constants.SALE_INVOICE + "|add/add");
                    } else {
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.salesInvoice()), Info.Type.INFO);
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());

                    if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.CONVERT_TO_INVOICE) || viewInterface.getFormParameters().isExternalForm(AccountingConstants.PROGRESS_INVOICING)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                    }

                    // this is for timesheet invoice, it closes the ProjectBasedInvoiceAddView tab
                    if (projectBasedInvoiceStartDate != null && projectBasedInvoiceEndDate != null) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_INVOICE_ADDED, result, viewInterface.getView());
                    }
                    viewInterface.getView().closeTab();
                }

                if (AccountingConstants.CONVERT_TO_INVOICE_FROM_RENTAL_ORDER.equals(viewInterface.getFormParameters().getExternalFormID())) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALES_INVOICE_CONVERT_AND_ADD, result, viewInterface.getView());
                }
                if (AccountingConstants.COPY_FROM_FIXED_ASSET.equals(viewInterface.getFormParameters().getExternalFormID())) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, result, viewInterface.getView());
                }
                if (invoiceData.getConvertedItemID() != null) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALESORDER_ADDED, null, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, null, viewInterface.getView());
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, viewInterface.getView());
                if (viewInterface.getFormParameters() != null && viewInterface.getFormParameters().getConvertFormType() != null) {
                    saveConvertedRelations(result.getId(), result.getNumber());
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void updateSaleInvoice(NewInvoice invoiceData) {
        invoiceData.setID(viewInterface.getObjectID());
        invoiceService.updateSaleInvoice(invoiceData, new AbstractAsyncCallback<SaveResult>() {

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
                    viewInterface.setObjectID(result.getId());
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.salesInvoice()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GDN_GRN_LIST_RELOAD, null, viewInterface.getView());
                    viewInterface.getView().closeTab();
                    if (!viewInterface.getFormParameters().isFromGettingStarted()) {
                        SinksContainerFactory.entryPoint.onHistoryChanged((viewInterface.isReccuringInvoice() ? Constants.RECURRING_INVOICE : Constants.SALE_INVOICE) + "|summary/" + viewInterface.getObjectID(), viewInterface.getNumberData() != null && viewInterface.getNumberData().getInvoiceNumber() != null ? viewInterface.getNumberData().getInvoiceNumber() : "");
                    }
                }
            }
        });
    }

    private void update(final String status, final boolean showSendToClient) {
        //validate base form fields
        if (!validation(status)) {
            setEnabledButtons(true);
            return;
        }
        //validate custom fields
        if (!DRAFT.equals(status) && !viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }

        NewInvoice invoiceData = viewInterface.getFormData(status, true);
        if (!Constants.DRAFT.equals(status) && invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }
        if (!viewInterface.getFormParameters().isProjectBasedInvoice() && !Constants.DRAFT.equals(status) && !newInvoice.isFromGdn()) {
            invoiceService.validateStockAvailability(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), StockOutFlow.FROM_SALE_INVOICE, invoiceData.getInvoiceDate(), new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                    setEnabledButtons(true);
                    showItemsValidationFailureMessage();
                }

                public void success(SelectItem[] items) {

                    if (items.length > 0) {
                        setEnabledButtons(true);
                        alertStockItemsMessage(items);
                    } else {
                        updateSaleInvoice(status, invoiceData, showSendToClient);
                    }
                }
            });
        } else {
            updateSaleInvoice(status, invoiceData, showSendToClient);
        }
    }

    private void updateSaleInvoice(final String status, NewInvoice invoiceData, final boolean showSendToClient) {
        if (Utils.hasGenericAccess(GenericSettingsEnum.CONSIGNMENT_FUNCTION_ENABLE)) {
            invoiceService.validateItemsInConsignmentToSell(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new AbstractAsyncCallback<String[]>() {
                @Override
                public void onFailure(Throwable caught) {
                    setEnabledButtons(true);
                }

                @Override
                public void onSuccess(String[] items) {
                    if (items.length > 0) {
                        setEnabledButtons(true);
                        updateSaleInvoiceInternal(status, invoiceData, showSendToClient);
                        alertConsignmentItemsMessage(items);
                    } else {
                        updateSaleInvoiceInternal(status, invoiceData, showSendToClient);
                    }
                }
            });
        } else {
            updateSaleInvoiceInternal(status, invoiceData, showSendToClient);
        }

    }

    private void updateSaleInvoiceInternal(final String status, NewInvoice invoiceData, final boolean showSendToClient) {

        if (Constants.APPROVE.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    updateData(status, invoiceData, showSendToClient);
                }

                @Override
                public void onCancel() {
                    setEnabledButtons(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.open();
        } else {
            updateData(status, invoiceData, showSendToClient);
        }

    }

    private void updateData(final String status, NewInvoice invoiceData, final boolean showSendToClient) {
//        NewInvoice invoice = viewInterface.getFormData(status);
        invoiceData.setID(viewInterface.getObjectID());
        LoadingPanel.loading(true);
        setEnabledButtons(false);
        invoiceService.updateSaleInvoice(invoiceData, new AbstractAsyncCallback<SaveResult>() {
            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                setEnabledButtons(true);
                if (result.isInvoiceExist()) {
                    addExistingInvoiceNumberListener();
                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), accountingStrings.invoice()), Info.Type.WARNING);
                } else {
                    if (status.equals(Constants.OPEN) && showSendToClient) {
                        sendToClient(result.getId());
                    } else {
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.salesInvoice()), Info.Type.INFO);
                        viewInterface.getView().closeTab();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                    }
                }
                LoadingPanel.loading(false);
            }
        });

    }

    public void sendToClient(Integer id) {
        String type = (viewInterface.getFormParameters().isRecurringInvoice() ? RECURRING_INVOICE_CATEGORY : viewInterface.getFormParameters().isProjectBasedInvoice() ? PROJECT_BASE_INVOICE_CATEGORY : SALES_INVOICE_CATEGORY);
        viewInterface.getView().closeTab();
        /*new AccountingComposeView(type,
                viewInterface.getCrmAccountLookUp().getSelectedItemID(), id, null,
                viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null, false);*/
        setEnabledButtons(true);

        Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
        SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + type + "/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + Boolean.FALSE);
    }

    private void addExistingInvoiceNumberListener() {
        viewInterface.getNumberTxtBox().setStyleName("x-form-invalid");
        viewInterface.getNumberTxtBox().addKeyDownHandler(event -> {
            if (!"".equals(viewInterface.getNumberTxtBox().getStyleName())) {
                viewInterface.getNumberTxtBox().removeStyleName(viewInterface.getNumberTxtBox().getStyleName());
            }
        });
    }

    private void alertConsignmentItemsMessage(String[] items) {
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

    public void pdfVersion(final Panel hp, Integer templateId, final boolean isPacking) {
        //validate base form fields
        if (!validation(null)) {
            return;
        }
        //validate custom fields
        if (!viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }

        if (isPacking) {
            new PDFTemplateSelector(Constants.PACKING_SLIP, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    generatePDF(hp, id, isPacking);
                }
            });
        } else {
            generatePDF(hp, templateId, isPacking);
        }
    }

    @Override
    protected void pdfVersion(Panel hp, Integer templateId) {

    }

    private void generatePDF(Panel hp, Integer pdfTemplateID, boolean isPacking) {
        String pdfURL = CommandConstants.PDF_URL + (viewInterface.getFormParameters().isProjectBasedInvoice() ? "/projectBaseInvoiceViewPDFHandler" : "/saleInvoceViewPDFHandler");
        if (isPacking) {
            pdfURL = CommandConstants.PDF_URL + ("/packingSlipPDFHandler");
        }

        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        NewInvoice invoiceData = viewInterface.getFormData(Constants.DRAFT, true);
        invoiceData.setID(viewInterface.getObjectID());
        invoiceData.setPdfTemplateID(pdfTemplateID);
        new PDFTransferObject(post, invoiceData);
        post.submit();
    }


    private void showItemsValidationFailureMessage() {
        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
    }

    public boolean validation(String status) {
        int errors = 0;

        if (newInvoice.getPaidAmount() != null
                && viewInterface.getProductTable().getTotalInInvoiceCurrency() != null
                && newInvoice.getPaidAmount().compareTo(viewInterface.getProductTable().getTotalInInvoiceCurrency()) > 0
                && !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())) {
            Info.show(accountingStrings.paymentAmountCannotMoreInvoiceAmount(), Info.Type.WARNING);
            return false;
        }
        if (!Validation.validateLookUpRequired(viewInterface.getCrmAccountLookUp())) {
            errors++;
        }

        if (!viewInterface.getFormParameters().isRecurringInvoice()
                && !Validation.validateTextBoxRequired(viewInterface.getNumberTxtBox())) {
            errors++;
        }

        if (DRAFT.equals(status)) {
            return errors == 0;
        }
        if (!viewInterface.validateSystemCustomFields()) {
            errors++;
        }
        if (!Validation.validateDate(viewInterface.getDatePicker())) {
            errors++;
        }
        if (!viewInterface.getTermsAndDuePanel().validate()) {
            errors++;
        }

        if (viewInterface.getPlaceOfSupplyWidget() != null && !viewInterface.getPlaceOfSupplyWidget().validate()) {
            errors++;
        }
        if (!viewInterface.getProductTable().validation(status)) {
            errors++;
            Utils.scrollIntoView(viewInterface.getProductTable().getItemsTable().getElement());
        }

        if (viewInterface.getRecurringWidget() != null && !viewInterface.getRecurringWidget().validate()) {
            errors++;
            Utils.scrollIntoView(viewInterface.getRecurringWidget().getElement());
        }

        if (viewInterface.getDatePicker().getDate() != null) {
            DateUtil.resetTime(viewInterface.getDatePicker().getDate());
        }
        if (viewInterface.getTermsAndDuePanel().isDueTypeSelected() && viewInterface.getTermsAndDuePanel().getDueDate() != null) {
            DateUtil.getDayLastTime(viewInterface.getTermsAndDuePanel().getDueDate());
        }
        if (viewInterface.getDatePicker().getDate() != null && viewInterface.getTermsAndDuePanel().getDueDate() != null
                && !Validation.validateDateOrder(viewInterface.getDatePicker().getDate(), viewInterface.getTermsAndDuePanel().getDueDate())) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, viewInterface.getProperty().getSingular(accountingStrings.checkInvoiceDate(), accountingStrings.invoice()));
            messageBox.open();
            return false;
        }
        if (newInvoice.isApprover() && (newInvoice.getPaidAmount() == null || newInvoice.getPaidAmount() == BigDecimal.ZERO)) {
            if (!viewInterface.getApproverLookUp().isValid()) {
                errors++;
            }
        }

        if (!viewInterface.validateProjectMandatory()) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave(), Info.Type.WARNING);
            return false;
        }

        if (!viewInterface.getFormParameters().isFromGettingStarted() && viewInterface.getConversionDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(viewInterface.getProperty().getSingular(accountingStrings.invoiceDateShouldBeAfterConversationDate(), accountingStrings.invoice()), Info.Type.WARNING);
            return false;
        }

        if (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Invoice", Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (!Utils.hasGenericAccess(GenericSettingsEnum.GCC_TAX_RATE_VALIDATION_IGNORE) && !validateApplicableTaxTypeForSale()) {
            Info.show("The tax rate for export of goods/services outside the GCC should be Zero.", Info.Type.WARNING);
            errors++;
        }
        if (!validateApplicableTypeForUK()) {
            Info.show("The tax rate for export of goods/services outside the UK should be Zero.", Info.Type.WARNING);
            errors++;
        }

        return errors == 0;
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
            /*if (!OPPORTUNITY.equals(viewInterface.getFormParameters().getCrmFormName()) &&
                    item != null &&
                    !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())
                    && viewInterface.getFormParameters().getConvertFormType() == null) {
                GWT.log("Client currency: " + item.getCurrencyID());
                viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());
            }*/
            viewInterface.getNumberTxtBox().setText(result.getInvoiceNumber());
        }

        viewInterface.setNumberData(result);
    }

    private void getAndApplyPaymentInstruction(final NewInvoice invoice) {
        //copy sales quote payment introduction to sales invoice
        if (!viewInterface.isEditForm() && CONVERT_TO_INVOICE.equals(viewInterface.getFormParameters().getExternalFormID()) && RECEIVABLE.equals(viewInterface.getFormParameters().getType()) &&
                (invoice.isSalesQuoteTermCopyToSalesInvoice() || invoice.isSalesOrderTermCopyToSalesInvoice())) {
            isCopyPaymentInstruction = true;
            if (invoice.getPaymentInstructionID() != null) {
                String name = invoice.getPaymentInstruction().trim().length() > 30 ? invoice.getPaymentInstruction().trim().substring(0, 30) + "..." : invoice.getPaymentInstruction();
                paymentInstruction = new SelectItem(invoice.getPaymentInstructionID(), name, invoice.getPaymentInstruction());
            }
        } else if (viewInterface.isEditForm() && RECEIVABLE.equals(viewInterface.getFormParameters().getType()) &&
                (invoice.isSalesQuoteTermCopyToSalesInvoice() || invoice.isSalesOrderTermCopyToSalesInvoice())) {
            if (invoice.getPaymentInstructionID() != null) {
                String name = invoice.getPaymentInstruction().trim().length() > 30 ? invoice.getPaymentInstruction().trim().substring(0, 30) + "..." : invoice.getPaymentInstruction();
                paymentInstruction = new SelectItem(invoice.getPaymentInstructionID(), name, invoice.getPaymentInstruction());
                isCopyPaymentInstruction = true;
            }
        }
        invoiceService.getPaymentInstructions(Constants.SALE_INVOICE, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

            public void success(SelectItem[] result) {
                if (isCopyPaymentInstruction && paymentInstruction != null) {
                    if (result.length == 0) {
                        result = new SelectItem[1];
                        result[0] = paymentInstruction;
                    } else {
                        SelectItem[] items = new SelectItem[result.length + 1];
                        items[0] = paymentInstruction;
                        int i = 1;
                        for (SelectItem item : result) {
                            items[i] = item;
                            i++;
                        }
                        result = items;
                    }
                    viewInterface.getProductTable().getPaymentTermsConditionsListBox().setItems(result);
                    viewInterface.getProductTable().getPaymentTermsConditionsListBox().setSelected(result[0].getId());
                    if (!viewInterface.isEditForm()) {
                        generatePaymentInstruction(invoice.getTypeItem(), (invoice.getInvoiceDate() != null ? dateFormat.format(invoice.getInvoiceDate().getNonConvertedDate()) : null));
                    }
                    if (result.length > 1) {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setVisible(true);
                    }
                } else {
                    if (result != null && result.length > 0) {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setItems(result);
                        if (invoice.getPaymentInstruction() != null && !viewInterface.isEditForm() && !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())) {
                            invoice.setPaymentInstructionID(null);
                        }
                        if (invoice.getPaymentInstructionID() != null) {
                            viewInterface.getProductTable().getPaymentTermsConditionsListBox().setSelected(invoice.getPaymentInstructionID());
                        } else {
                            viewInterface.getProductTable().getPaymentTermsConditionsListBox().setSelected(result[0].getId());
                        }
                        if (!viewInterface.isEditForm() && !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())) {
                            generatePaymentInstruction(invoice.getTypeItem(), (invoice.getInvoiceDate() != null ? dateFormat.format(invoice.getInvoiceDate().getNonConvertedDate()) : null));
                        }
                        if (result.length > 1) {
                            viewInterface.getProductTable().getPaymentTermsConditionsListBox().setVisible(true);
                        }
                    }
                }
            }
        });
    }

    private void getAndApplyIntroduction(final NewInvoice invoice) {
        if (invoice.getIntroduction() != null && (viewInterface.isEditForm() || COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID()))) {
            viewInterface.getIntroduction().setText(invoice.getIntroduction());
        } else {
            invoiceService.getPaymentIntroduction(Constants.SALE_INVOICE_INTR, new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                }

                public void success(SelectItem[] result) {
                    if (result != null && result.length > 0) {
                        viewInterface.getIntroduction().setText(result[0].getDescription());
                    }
                }
            });
        }
    }

    private String paymentMethod;

    private void generatePaymentInstruction(TypeItem clientItem, String startDate) {
        String dueDayStr = null, dueDateStr = null;
        String fromDate = null, toDate = null;
        Date invDate = DateUtil.resetTime(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date());

        String terms = null;
        if (viewInterface.getTermsAndDuePanel().getInvoiceTerms() != null && viewInterface.getTermsAndDuePanel().getInvoiceTerms().getName() != null) {
            terms = viewInterface.getTermsAndDuePanel().getInvoiceTerms().getName();
        }
        Date dueDate = viewInterface.getTermsAndDuePanel().getDueDate();

        if (dueDate != null) {
            dueDate = DateUtil.resetTime(dueDate);
            int i = 0;
            while (invDate.before(dueDate)) {
                i++;
                invDate = DateUtil.addDays(invDate, 1);
            }
            dueDateStr = DateUtils.format(dueDate);
            dueDayStr = String.valueOf(i != 0 ? i : 1);
        }

        if (clientItem == null) {
            clientItem = customerSupplierItem;
        }

        if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null && clientItem != null) {
            paymentMethod = clientItem.getPaymentType() != null ? clientItem.getPaymentType() : "";
        }

        if (AccountingUtils.get().enablePaymentPeriod()) {
            if (viewInterface.getPeriodStart().getDate() != null) {
                fromDate = dateFormat.format(viewInterface.getPeriodStart().getDate());
            }
            if (viewInterface.getPeriodEnd().getDate() != null) {
                toDate = dateFormat.format(viewInterface.getPeriodEnd().getDate());
            }
        }

        if (viewInterface.getProductTable().getPaymentTermsConditionsListBox().isSomethingSelected()) {
            String template = PaymentTermsConditionsUtil.generateSelectedTemplate(viewInterface.getProductTable().getPaymentTermsConditionsTemplate(),
                    dueDayStr, dueDateStr, paymentMethod, startDate, fromDate,
                    toDate, viewInterface.getNumberTxtBox().getText(), terms);
            viewInterface.getProductTable().getPaymentInstruction().setText(template);
        }
    }

    public void onClientChange(TypeItem item) {
        if (item.getCurrencyID() != null) {
            viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());

            viewInterface.getAccountsReceivablePayableLookUp().clear();
            viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(item.getCurrencyID());
        }
        if (viewInterface.getNumberData() != null) {
            if (viewInterface.getNumberData().isWithClient()) {
                viewInterface.getNumberData().setClientCode(item.getCode());
            }
            if (viewInterface.getNumberData().isWithProject()) {
                viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
            }
            viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
        }
    }

    private void setClientData(final Integer id, final Boolean add, final SelectItem projectItem) {
        invoiceService.getClientOrSupplier(id, Constants.RECEIVABLE, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(TypeItem typeItem) {
                applyTypeItemData(typeItem, add, true, projectItem, true);

                if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                    configurePlaceOfSupply(typeItem, typeItem.getPlaceOfSupply(), viewInterface.getPlaceOfSupplyWidget());
                }
                if (!(viewInterface.isEditForm() || viewInterface.getFormParameters().isRecurringInvoice())) {
                    onChangeInvoiceNumber();

                    if (typeItem.getBankAccountID() != null) {
                        initBankAccountItems(typeItem.getBankAccountID());
                    }
                } else if (viewInterface.getFormParameters().isRecurringInvoice() && typeItem.getBankAccountID() != null) {
                    initBankAccountItems(typeItem.getBankAccountID());
                }
                viewInterface.getProductTable().setCustomerDefaultWarehouseAndDepartment(typeItem.getDefaultDepartment(), typeItem.getDefaultWarehouse());
                setRelatedBillableExpenses(viewInterface.getCrmAccountLookUp().getSelectedItemID());
            }
        });
    }

    public void applyTypeItemData(TypeItem typeItem, Boolean add, boolean setCurrency, SelectItem projectItem, boolean changeTermItem) {
        customerSupplierItem = typeItem;

        if (projectItem != null) {
            viewInterface.getProjectLookUp().setSelected(projectItem);
        }
        if (changeTermItem && typeItem.getTermsItem() != null) {
            viewInterface.getTermsAndDuePanel().applyCustomerTerms(typeItem.getTermsItem());
        }
        setClientItems(typeItem, add, setCurrency);
        if (typeItem.getCurrencyID() != null) {
            viewInterface.getCurrencyWidget().setCurrency(typeItem.getCurrencyID());
        }
        if (typeItem.getTaxItem() != null) {
            viewInterface.getProductTable().setCustomerOrSupplierTaxItem(typeItem.getTaxItem());
        } else {
            viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
        }
        if (viewInterface.getAccountsReceivablePayableLookUp() != null && typeItem.getAccountsReceivablePayable() != null) {
            viewInterface.getAccountsReceivablePayableLookUp().addAccountItem(typeItem.getAccountsReceivablePayable());
        }
    }

    private void setClientItems(final TypeItem item, final Boolean add, final boolean setCurrency) {
        if (item.getId() != null) {
            if (add) {
                viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency);
            } else {
                viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency);
            }
        }
        if (!viewInterface.isEditForm()/* && copyFromExistingInvoiceID == null*/) {
            onClientChange(item);
        }/* else if (invoiceNumberData != null && invoiceNumberData.isWithClient()) {
            applyInvoiceNumberData(invoiceNumberData, item);
        }*/
        generatePaymentInstruction(item, dateFormat.format(viewInterface.getDatePicker().getDate()));

        if (item.getSupplierCustomerBalance() >= 0) {
            viewInterface.getCustomerBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
            viewInterface.getCustomerBalanceLink().setText(AccountingUtils.get().formatPrice(item.getSupplierCustomerBalance()));
        } else {
            viewInterface.getCustomerBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
            viewInterface.getCustomerBalanceLink().setText("(" + AccountingUtils.get().formatPrice((-1) * item.getSupplierCustomerBalance()) + ")");
        }
    }

    private void setRelatedPriceLevel(Integer clientID, final Integer priceLevelID) {
        boolean somethingSelected = viewInterface.getPriceLevelDropdown().isSomethingSelected();
        viewInterface.getPriceLevelDropdown().setSelectedNullLabel();
        viewInterface.getPriceLevelDropdown().clear();

        if (clientID != null) {
            clientService.getClientPriceLevels(clientID, new AbstractAsyncCallback<PriceLevelItem[]>() {
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
                        } else if (!viewInterface.isEditForm()) {
                            if (priceLevelItems.length == 1) {
                                viewInterface.getPriceLevelDropdown().setSelected(priceLevelItems[0].getId());
                            }
                        }
                        viewInterface.getPriceLevel().setVisible(true);
                        onChangePriceLevel(newInvoice);
                    } else {
                        viewInterface.getPriceLevel().setVisible(false);
                        if (somethingSelected) {
                            onChangePriceLevel(newInvoice);
                        }
                    }
                }
            });
        } else {
            viewInterface.getPriceLevel().setVisible(false);
            if (somethingSelected) {
                onChangePriceLevel(newInvoice);
            }
        }
    }

    private void setRelatedClientDiscount(Integer clientID, final Integer discountID) {
        viewInterface.getClientDiscountDropdown().clear();
        viewInterface.getClientDiscountField().setVisible(false);

        if (clientID != null) {
            clientService.getClientDiscounts(clientID, new AbstractAsyncCallback<DiscountItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                    throwable.printStackTrace();
                }

                @Override
                public void success(DiscountItem[] discountItems) {
                    if (discountItems != null && discountItems.length > 0) {
                        viewInterface.getClientDiscountDropdown().addItems(discountItems);
                        if (discountID != null) {
                            viewInterface.getClientDiscountDropdown().setSelected(discountID);
                        } else if (!viewInterface.isEditForm()) {
                            if (discountItems.length == 1) {
                                viewInterface.getClientDiscountDropdown().setSelected(discountItems[0].getId());}
                        }
                        else {
                            viewInterface.getClientDiscountDropdown().getValues().clear();
                            viewInterface.getProductTable().setClientDiscountsSelectItem(null);
                        }
                        viewInterface.getClientDiscountField().setVisible(true);
                    } else {
                        viewInterface.getClientDiscountDropdown().getValues().clear();
                        viewInterface.getProductTable().setClientDiscountsSelectItem(null);
                    }
                    onChangeClientDiscount();

                }
            });
        }
    }

    private void requestAndSetConversionDate() {
        invoiceService.getInvoiceDate(viewInterface.getFormParameters().isFromGettingStarted() ? -1 : 0, new AbstractAsyncCallback<Date>() {
            public void success(Date result) {
                viewInterface.setConversionDate(result);
            }
        });
    }

    private void onChangeClientDiscount() {
        if (viewInterface.getClientDiscountDropdown().getSelectedId() != null) {
            DiscountItem discountItem = (DiscountItem) viewInterface.getClientDiscountDropdown().getSelectedData();
            viewInterface.getProductTable().setClientDiscount(discountItem);
            viewInterface.getProductTable().onClientDiscountChange();
        } else if (!viewInterface.getClientDiscountDropdown().getValues().isEmpty()) {
            viewInterface.getProductTable().setClientDiscountsSelectItem(viewInterface.getClientDiscountDropdown().getValues());
            viewInterface.getProductTable().onClientDiscountChange();
        } else {
            viewInterface.getProductTable().setClientDiscount(null);
            viewInterface.getProductTable().onClientDiscountChange();
        }
    }

    public void onChangeClientHandler(final boolean isEditFormData) {
        setRelatedPriceLevel(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
        if (viewInterface.isEditForm() || viewInterface.isReccuringInvoice() || viewInterface.getFormParameters().isProjectBasedInvoice()) {
            return;
        }
        if (!Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_QUOTE_CONVERT)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setInvoiceClientId(viewInterface.getCrmAccountLookUp().getSelectedItemID());
            filterParameter.setInvoicesOnly(true);
            filterParameter.setStartDate(projectBasedInvoiceStartDate);
            filterParameter.setEndDate(projectBasedInvoiceEndDate);

            invoiceService.getSaleQuoteByClient(filterParameter, new AbstractAsyncCallback<ArrayList<NewInvoice>>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ArrayList<NewInvoice> saleQuotes) {

                    if (saleQuotes.size() > 0) {

                        Command listener = () -> {
                            setRelatedPriceLevel(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
                            setRelatedClientDiscount(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
                            setClientData(viewInterface.getCrmAccountLookUp().getSelectedItemID(), true, null);
                        };
                        new CustomerSQDialogBox(new CustomerSQDialogBox.CustomerSQInterface() {
                            @Override
                            public void setExternalQuoteID(Integer quoteID) {
                                viewInterface.getFormParameters().setExternalFormID(AccountingConstants.PROGRESS_INVOICING);
                                viewInterface.getFormParameters().setExternalObjectID(quoteID);
                            }

                            @Override
                            public void setQuotePercent(BigDecimal percent) {
                                viewInterface.setQuotePercent(percent);
                            }

                            @Override
                            public void setQuoteAmount(BigDecimal amount) {
                                viewInterface.setQuoteAmount(amount);
                            }

                            @Override
                            public void setProgressInvoiceDialogBpxType(String type) {
                                viewInterface.setProgressInvoiceDialogBoxType(type);
                            }

                            @Override
                            public void setProgressInvoicingByItem(boolean value) {
                                viewInterface.setProgressInvoicingByItem(value);
                            }

                            @Override
                            public void setProgressInvoiciningMap(HashMap<Integer, BigDecimal> valuesMap) {
                                viewInterface.setProgressInvoiciningMap(valuesMap);
                            }

                            @Override
                            public void applyProgressInvoicingData(NewInvoice result) {
                                viewInterface.applyProgressInvoicingParameters(result);
                            }

                            @Override
                            public View getView() {
                                return viewInterface.getView();
                            }

                            @Override
                            public void goTo(String url) {
                                viewInterface.getView().goTo(url);
                            }

                            @Override
                            public boolean isMultiQuoteConvert() {
                                return newInvoice.isMultiQuoteConvertEnabled();
                            }
                        }).alertMessage(saleQuotes.toArray(new NewInvoice[0]), listener);
                    }
                }
            });
        }

        if (!isEditFormData) {
            setRelatedClientDiscount(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
            setClientData(viewInterface.getCrmAccountLookUp().getSelectedItemID(), true, null);
        } else {
            setRelatedBillableExpenses(viewInterface.getCrmAccountLookUp().getSelectedItemID());
        }
        setInvoiceCustomFields();
    }

    //Call the method when client currency is applied
    private void setRelatedBillableExpenses(Integer clientID) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setInvoiceClientId(clientID);
        filterParameter.setStartDate(projectBasedInvoiceStartDate);
        filterParameter.setEndDate(projectBasedInvoiceEndDate);

        invoiceService.getBillableExpensesByClient(filterParameter, new AbstractAsyncCallback<ArrayList<BillableExpenseItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                viewInterface.getBillableExpenseButton().setVisible(false);
            }

            @Override
            public void onSuccess(ArrayList<BillableExpenseItem> expenseListItems) {
                ArrayList<BillableExpenseItem> beList = new ArrayList<>();

                if (expenseListItems.size() > 0) {
                    beList.addAll(expenseListItems);
                }
                if (newInvoice != null && viewInterface.getCrmAccountLookUp().getSelectedItem().getName().equalsIgnoreCase(newInvoice.getClientName())
                        && newInvoice.getExpenses() != null && !newInvoice.getExpenses().isEmpty()) {
                    beList.addAll(newInvoice.getExpenses());
                }


                viewInterface.getBillableExpenseButton().setVisible(!beList.isEmpty());

                viewInterface.getExpenseMarkupPopup().setValues(beList, viewInterface.getCurrencyWidget().getCurrencyID(), viewInterface.getCurrencyWidget().getExchangeRate());
            }
        });
    }

    private void loadBillableExpenseTotal() {
        viewInterface.getProductTable().setHasBillableExp(true);
        BigDecimal totalAmount = viewInterface.getExpenseMarkupPopup().getTotalWithMarkup();
        BigDecimal taxTotal = viewInterface.getExpenseMarkupPopup().getTaxTotal();
        viewInterface.getProductTable().setBillableExpenseTotal(totalAmount);
        viewInterface.getProductTable().setBillableExpenseTaxTotal(taxTotal);
        viewInterface.getProductTable().getBillableExpenseAmount().setHTML(AccountingUtils.get().formatPrice(totalAmount));
        viewInterface.getProductTable().getBillableExpenseTaxAmount().setHTML(AccountingUtils.get().formatPrice(taxTotal));
        viewInterface.getProductTable().setMarkupWidgets(viewInterface.getExpenseMarkupPopup().getMarkupWidgets());
        viewInterface.getProductTable().drawTotalsTable();
        viewInterface.getProductTable().calculate();
    }

    public void setProjectBasedInvoiceStartDate(Date projectBasedInvoiceStartDate) {
        this.projectBasedInvoiceStartDate = projectBasedInvoiceStartDate;
    }

    public void setProjectBasedInvoiceEndDate(Date projectBasedInvoiceEndDate) {
        this.projectBasedInvoiceEndDate = projectBasedInvoiceEndDate;
    }

    private void saveConvertedRelations(Integer _objectId, String number) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_SALEINVOICE, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number));
        AllInOneService.App.get().getAdditionalRelations(_objectId, RelationItem.TYPE_SALEINVOICE, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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
                    AllInOneService.App.get().saveRelations(RelationItem.TYPE_SALEINVOICE, _objectId, number, relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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

    private void setInvoiceCustomFields(){

        invoiceService.getInvoiceCustomFieldItems(viewInterface.getCrmAccountLookUp().getSelectedItemID(), ViewName.SaleInvoice, new AsyncCallback<NewInvoice>() {
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
