package com.edatasite.workforce.gwt.invoice.client.ui.view.creditnote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.container.accounting.AccountingSinksContainer;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.AddEditBankAccountView;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceQuoteFormPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/16/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreditNoteFormPresenter extends InvoiceQuoteFormPresenter implements Constants, AccountingConstants {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final ClientServiceAsync clientService = ClientService.App.get();
    protected static Property property;

    private final CreditNoteViewInterface viewInterface;
    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    //for Split Button
    private Command approveCommand;
    private Command approveAndSendCommand;
    private NewInvoice data;
    private HashMap<Integer, ShippingMethod> shippingMap;

    public CreditNoteFormPresenter(CreditNoteViewInterface viewInterface) {
        super(viewInterface);
        this.viewInterface = viewInterface;
    }

    @Override
    public void bindUI() {
        shippingMap = new HashMap<>();

        LoadingPanel.loading(true);
        initBankAccountItems(null);

        requestAndSetConversionDate();

        InvoiceService.App.get().getAllCreditNoteData(viewInterface.getFormParameters(), new AbstractAsyncCallback<NewInvoice>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NewInvoice result) {
                data = result;
                viewInterface.initPdfTemplates(result);
                viewInterface.getProductTable().setProjectBasedInvoice(result.isProjectBasedInvoice());
                viewInterface.getProductTable().setRoundingModeDisabled(result.isRoundingModeDisabled());
                viewInterface.getProductTable().setDoubleTaxEnabled(result.isDoubleTaxEnabled());
                viewInterface.getProductTable().setItemCustomFields(result.getItemCustomFields());
                customerSupplierItem = result.getTypeItem();
                viewInterface.initProductsTableData(result);
                initFormHandlers();
                viewInterface.initWidgetMap(result);
                viewInterface.setFormData(result);
                renderButtons(result);


                if (viewInterface.getObjectID() != null || viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_INVOICE_TO_CREDITNOTE)
                        || viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA)) {
                    setClientSupplierItems(result.getTypeItem(), true, false, null);
                    shippingMap.put(result.getShippingMethodID(), result.getShippingMethod());
                }
                if (!viewInterface.isEditForm()) {
                    applyCreditNoteNumberData(result.getNumberData(), result.getTypeItem());
                }
                setRelatedPriceLevel(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);

                initBankAccountItems(result.getBankAccount() != null ? result.getBankAccount().getId() : null);
                viewInterface.initCustomFields(result);
                viewInterface.generateForm(result.getLayoutHTML());
                LoadingPanel.loading(false);
            }
        });
    }

    private void requestAndSetConversionDate() {
        InvoiceService.App.get().getInvoiceDate(viewInterface.getFormParameters().isFromGettingStarted() ? -1 : 0, new AbstractAsyncCallback<Date>() {
            public void success(Date result) {
                viewInterface.setConversionDate(result);
            }
        });
    }

    public void renderButtons(NewInvoice result) {
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        String statusCode = result.getStatusCode();
        if (viewInterface.getFormParameters().isEditForm()) {
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
                if (!viewInterface.getFormParameters().isExternalForm(COPY_INVOICE_TO_CREDITNOTE) && DRAFT.equals(statusCode)) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                if (DRAFT.equals(statusCode) || APPROVE.equals(statusCode) || OPEN.equals(statusCode)) {
                    splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true));
                    //saveAndApproveButton.setVisible(true);
                }
                if ((RECEIVABLE.equals(viewInterface.getFormParameters().getType()) || PAYABLE.equals(viewInterface.getFormParameters().getType())) && (DRAFT.equals(statusCode) || APPROVE.equals(statusCode) || OPEN.equals(statusCode) || OVER_DUE.equals(statusCode))) {
                    splitButtonItems.add(new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0));
                    //approveAndSendButton.setVisible(true);
                }
            }
        } else {
            if (!viewInterface.getFormParameters().isExternalForm(COPY_INVOICE_TO_CREDITNOTE)) {
                viewInterface.getSaveButton().setVisible(true);
            }
            splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true));

            if (RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                splitButtonItems.add(new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0));
            }
        }
        viewInterface.getApproveSplitButton().addItemList(splitButtonItems);
    }

    private void initFormHandlers() {

        viewInterface.getDatePicker().getPopup().addChangeHandler(event -> {
            if ((Constants.PAYABLE.equals(viewInterface.getFormParameters().getType()) && data.isPurchaseInvoiceNumberingShow())
                    || Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                if (viewInterface.getNumberData().isWithDate()) {
                    viewInterface.getNumberData().setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getDatePicker().getDate()));
                    viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                }

                if (!viewInterface.isEditForm()) {
                    onChangeInvoiceNumber(null);
                }
            }
            if (data != null && data.getDueDays() != null) {
                viewInterface.getDueDatePicker().setDate(DateUtil.addDays(viewInterface.getDatePicker().getDate(), data.getDueDays()));
            }
        });
        if ((Constants.PAYABLE.equals(viewInterface.getFormParameters().getType()) && data.isPurchaseInvoiceNumberingShow())
                || Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
            viewInterface.getProjectLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject()) {
                    viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                    viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
                }
            });
        }

        viewInterface.getCrmAccountLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {

            if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null) {
                onChangeClientHandler(false);
                invoiceService.getClientOrSupplier(viewInterface.getCrmAccountLookUp().getSelectedItemID(), viewInterface.getFormParameters().getType(), new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onSuccess(TypeItem result) {
                        customerSupplierItem = result;

                        if (result != null) {
                            viewInterface.getProductTable().setCustomerSupplierItem(result);

                            if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                                configurePlaceOfSupply(result, Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType()) ? result.getPlaceOfSupply() : null, viewInterface.getPlaceOfSupplyWidget());
                            }
                            viewInterface.getCurrencyWidget().setCurrency(result.getCurrencyID());
                        }
                        if (viewInterface.getProjectLookUp() != null) {
                            viewInterface.getProjectLookUp().clear();
                        }
                        if (result != null && result.getTaxItem() != null) {
                            viewInterface.getProductTable().setCustomerOrSupplierTaxItem(result.getTaxItem());
                        } else {
                            viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
                        }
                        if (Constants.PAYABLE.equals(viewInterface.getFormParameters().getType()) && data.isPurchaseInvoiceNumberingShow() != null && data.isPurchaseInvoiceNumberingShow()) {
                            onChangeInvoiceNumber(result);
                        }

                        if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                            viewInterface.getProductTable().clearProjectFromLineItems();
                        }
                        getSupplierForAccountsReceivable(result);

                        if (result != null) {
                            viewInterface.getProductTable().setCustomerDefaultWarehouseAndDepartment(result.getDefaultDepartment(), result.getDefaultWarehouse());

                            if (result.getSupplierCustomerBalance() >= 0) {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText(AccountingUtils.get().formatPrice(result.getSupplierCustomerBalance()));
                            } else {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText("(" + AccountingUtils.get().formatPrice((-1) * result.getSupplierCustomerBalance()) + ")");
                            }
                        }

                    }
                });
                setCreditNoteCustomFields();
            }
        });

        viewInterface.getSupplierBalanceLink().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("supplierBalance|supplierBalance/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + CrmAccountItem.SUPPLIER));
        viewInterface.getBankAccountListBox().addValueChangeHandler(event -> onBankAccountChange());
        viewInterface.getBankAccountDetailLink().addClickHandler(sender -> new AddEditBankAccountView(viewInterface.getBankAccountListBox().getSelectedId(), () -> initBankAccountItems(viewInterface.getBankAccountListBox().getSelectedId())));

        viewInterface.getCurrencyWidget().addListener(() -> {
//            viewInterface.getAccountsReceivablePayableLookUp().clear();
            viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(viewInterface.getCurrencyWidget().getCurrencyID());
        });

        if (viewInterface.isEditForm()) {
            if (data.getPaidAmount() != null && !(data.isSubmitter(Utils.getUserID()) || Utils.hasRoles(Constants.ADMIN))) {
                viewInterface.getApproverLookUp().setEnabled(false);
            }
            viewInterface.getSaveButton().addClickHandler(sender -> {
                if (!Utils.hasPermission(CreditNotePermissionConstants.CREDIT_NOTE_DRAFT)) {
                    Info.show(accountingMessages.youDontHaveNecessaryPermissionsTo(wfmStrings.draft()), Info.Type.WARNING);
                    return;
                }
                update(Constants.DRAFT);
            });

            approveCommand = () -> {
                if (!Utils.hasPermission(CreditNotePermissionConstants.CREDIT_NOTE_APPROVE)) {
                    Info.show(accountingMessages.youDontHaveNecessaryPermissionsTo(wfmStrings.approve()));
                    return;
                }
                update(Constants.APPROVE);
            };
            viewInterface.getSubmitButton().addClickHandler(event -> {
                if (data.getPaymentItems().length == 0) {
                    setEnabledButtons(false);
                    update(Constants.SUBMITTED_TO_MANAGER);
                } else {
                    Info.warn(accountingStrings.cantEditInvoiceWithPayments());
                }
            });
            approveAndSendCommand = () -> {
                setEnabledButtons(false);
                update(Constants.OPEN);
            };

        } else {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                if (!Utils.hasPermission(CreditNotePermissionConstants.CREDIT_NOTE_DRAFT)) {
                    Info.show(accountingMessages.youDontHaveNecessaryPermissionsTo(wfmStrings.draft()), Info.Type.WARNING);
                    return;
                }
                save(Constants.DRAFT);
            });

            approveCommand = () -> {
                if (!Utils.hasPermission(CreditNotePermissionConstants.CREDIT_NOTE_APPROVE)) {
                    Info.show(accountingMessages.youDontHaveNecessaryPermissionsTo(wfmStrings.approve()));
                    return;
                }
                save(Constants.APPROVE);
            };

            //for Submit
            viewInterface.getSubmitButton().addClickHandler(event -> {
                setEnabledButtons(false);
                save(Constants.SUBMITTED_TO_MANAGER);
            });

            approveAndSendCommand = () -> {
                if (!Utils.hasPermission(CreditNotePermissionConstants.CREDIT_NOTE_APPROVE)) {
                    Info.show(accountingMessages.youDontHaveNecessaryPermissionsTo(wfmStrings.approve()));
                    return;
                }
                save(Constants.OPEN);
            };
        }

        /**
         * Generate pdf button functionality:         *
         * We moved pdf list to split button from form field
         */
        {
            //for PDf
            List<SplitButtonItem> pdfButtonItems = generatePdfTemplates(data);

            if (Utils.hasRoles(Constants.ADMIN)) {
                pdfButtonItems = pdfButtonItems != null ? pdfButtonItems : new ArrayList<>();

                if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                    pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.RECEIVABLE_CREDIT_NOTE.name())));
                } else {
                    pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", "Customize", () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.PAYABLE_CREDIT_NOTE.name())));
                }
            }

            viewInterface.getSplitButtonPdf().addItemList(generatePdfTemplates(data));
            //End Pdf
        }
    }

    public void onChangeClientHandler(final boolean isEditFormData) {
        setRelatedPriceLevel(viewInterface.getCrmAccountLookUp().getSelectedItemID(), null);
    }

    private void getSupplierForAccountsReceivable(TypeItem result) {
        viewInterface.getAccountsReceivablePayableLookUp().clear();
        viewInterface.getAccountsReceivablePayableLookUp().setCurrencyID(result.getCurrencyID());

        if (viewInterface.getAccountsReceivablePayableLookUp() != null && result.getAccountsReceivablePayable() != null) {
            viewInterface.getAccountsReceivablePayableLookUp().addAccountItem(result.getAccountsReceivablePayable());
            viewInterface.getAccountsReceivablePayableLookUp().setEnabled(false);
        } else {
            viewInterface.getAccountsReceivablePayableLookUp().setEnabled(true);
        }
    }

    private void setRelatedPriceLevel(Integer clientID, final Integer priceLevelID) {
        boolean somethingSelected = viewInterface.getPriceLevelDropdown().isSomethingSelected();
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
                        onChangePriceLevel(data);
                    } else {
                        viewInterface.getPriceLevel().setVisible(false);
                        if (somethingSelected) {
                            onChangePriceLevel(data);
                        }
                    }
                }
            });
        } else {
            viewInterface.getPriceLevel().setVisible(false);
            if (somethingSelected) {
                onChangePriceLevel(data);
            }
        }
    }

    private void initBankAccountItems(final Integer bankAccountId) {
        AccountingService.App.get().getBankAccountItemsForReference(new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {

            }

            public void success(SelectItem[] result) {
                viewInterface.getBankAccountListBox().setItems(result);
                if (bankAccountId != null) {
                    viewInterface.getBankAccountListBox().setSelected(bankAccountId);
                }
                onBankAccountChange();
            }
        });
    }

    private void onBankAccountChange() {
        viewInterface.getBankAccountDetailLink().setVisible(viewInterface.getBankAccountListBox().getSelectedIndex() != 0);
    }

    private void applyCreditNoteNumberData(InvoiceNumberData numberData, TypeItem item) {
        if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
            viewInterface.setNumberData(numberData);
            if (viewInterface.getNumberData() != null) {
                if (viewInterface.getNumberData().isWithDate()) {
                    viewInterface.getNumberData().setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getCurrentDate()));
                }
                if (viewInterface.getNumberData().isWithClient() && item != null) {
                    viewInterface.getNumberData().setClientCode(item.getCode());
                }
            }
            viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
        } else if (data.isPurchaseInvoiceNumberingShow()) {
            viewInterface.setNumberData(numberData);
            if (viewInterface.getNumberData().isWithDate()) {
                viewInterface.getNumberData().setDate(DateTimeFormat.getFormat("yyyyMMdd").format(viewInterface.getCurrentDate()));
            }
            if (viewInterface.getNumberData().isWithClient() && item != null) {
                viewInterface.getNumberData().setClientCode(item.getCode());
            }
            if (viewInterface.getNumberData().isWithProject()) {
                viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
            }
            viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
        }

    }

    private void setClientSupplierItems(final TypeItem item, final Boolean add, final boolean setCurrency, TypeItem[] items) {
        if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
            if (item.getId() != null) {
                if (add) {
                    viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency);
                } else {
                    viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency);
                }
            }
            if (!viewInterface.isEditForm()) {
                onClientChange(item);
            } else if (viewInterface.getNumberData() != null) {
                if (viewInterface.getNumberData().isWithClient()) {
                    viewInterface.getNumberData().setClientCode(item.getCode());
                }
                if (viewInterface.getNumberData().isWithProject()) {
                    viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                }
            }
        } else {
            if (item.getId() != null) {
                if (add) {
                    viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency);
                } else {
                    viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency);
                }
            }
        }
    }

    private void onClientChange(TypeItem item) {
        if (item != null) {
            if (!(viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_INVOICE_TO_CREDITNOTE)
                    || viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_FROM_EXISTING_DATA))) {
                viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());

                getSupplierForAccountsReceivable(item);
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
    }

    private void save(final String invoiceStatus) {
        setEnabledButtons(false);

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
        final NewInvoice invoiceData = viewInterface.getFormData(invoiceStatus, true);

        if (!Constants.DRAFT.equals(invoiceStatus) && invoiceData.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }

        if (PAYABLE.equals(invoiceData.getType()) && !invoiceStatus.equals(Constants.DRAFT)) {
            invoiceService.validateStockAvailability(viewInterface.getProductTable().getQuantityItemsForValidate(), null, StockOutFlow.FROM_DEBIT_NOTE, invoiceData.getInvoiceDate(), new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                    setEnabledButtons(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(SelectItem[] items) {

                    if (items.length > 0) {
                        setEnabledButtons(true);
                        alertStockItemsMessage(items);
                    } else {
                        saveCreditNote(invoiceData, invoiceStatus);
                    }
                }
            });
        } else {
            saveCreditNote(invoiceData, invoiceStatus);
        }
    }

    private void saveCreditNote(NewInvoice invoiceData, final String invoiceStatus) {
        LoadingPanel.loading(true);
        InvoiceService.App.get().saveCreditNote(invoiceData, new AbstractAsyncCallback<SaveResult>() {

            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                if (result.isInvoiceExist()) {
                    Info.show(accountingMessages.creditNoteWithThisNumberIsAlreadyExists(), Info.Type.WARNING);
                } else {
                    viewInterface.setObjectID(result.getId());
                    if (invoiceStatus.equals(Constants.OPEN)) {
                        final WfmMessageBox continueButton = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, wfmStrings.messSuccessfullyApproved(), accountingStrings.getPropertyContinue(), new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                sendToClient(viewInterface.getObjectID());
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, viewInterface.getObjectID(), viewInterface.getView());
                            }
                        });
                        continueButton.setTitle(wfmStrings.information());
                        continueButton.open();
                    } else {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.salesInvoice()), Info.Type.INFO);
                        viewInterface.getView().closeTab();
                        if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                            if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_INVOICE_TO_CREDITNOTE)) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALE_INVOICE_ADD_CREDIT_NOTE, result, viewInterface.getView());
                            } else {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());

                                viewInterface.getView().closeTab();
                            }
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result, viewInterface.getView());

                            if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_INVOICE_TO_CREDITNOTE)) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASE_INVOICE_ADD_CREDIT_NOTE, result, viewInterface.getView());
                            }
                            viewInterface.getView().closeTab();
                        }
                    }
                }
                LoadingPanel.loading(false);
                setEnabledButtons(true);
            }
        });
    }

    private void update(final String status) {
        setEnabledButtons(false);
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
        NewInvoice creditNote = viewInterface.getFormData(status, true);

        if (PAYABLE.equals(creditNote.getType()) && !status.equals(Constants.DRAFT)) {
            invoiceService.validateStockAvailability(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), StockOutFlow.FROM_DEBIT_NOTE, data.getInvoiceDate(), new AbstractAsyncCallback<SelectItem[]>() {
                public void failure(Throwable caught) {
                    setEnabledButtons(true);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(SelectItem[] items) {

                    if (items.length > 0) {
                        setEnabledButtons(true);
                        alertStockItemsMessage(items);
                    } else {
                        updateCreditNote(status, creditNote);
                    }
                }
            });
        } else if (RECEIVABLE.equals(creditNote.getType()) && !status.equals(Constants.DRAFT)) {
            QuantityItem[] quantityItems = viewInterface.getProductTable().getQuantityItemsForValidateStockInconsistency();
            if (quantityItems.length > 0) {
                validateForInconsistencyAndUpdate(creditNote, quantityItems);
            } else {
                updateCreditNote(status, creditNote);
            }
        } else {
            updateCreditNote(status, creditNote);
        }
    }

    private void validateForInconsistencyAndUpdate(final NewInvoice creditNote, QuantityItem[] quantityItems) {
        InvoiceService.App.get().validateStockInconsistencyInAdjustProcess(StockTransactionType.CREDIT_NOTE, viewInterface.getObjectID(), quantityItems, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                setEnabledButtons(true);
            }

            @Override
            public void success(SelectItem result) {
                if (result == null) {
                    updateCreditNote(creditNote.getStatusCode(), creditNote);
                } else {
                    Info.warn(AccountingMessages.App.get().youDoNotHaveEnoughQuantity(result.getName()), 5000);
                    LoadingPanel.loading(false);
                    setEnabledButtons(true);
                }
            }
        });
    }

    private void updateCreditNote(final String invoiceStatus, NewInvoice creditNote) {

        if (!Constants.DRAFT.equals(invoiceStatus) && creditNote.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            setEnabledButtons(true);
            Info.show(accountingMessages.totalAmountCantLessThanZero(), Info.Type.WARNING);
            return;
        }

        creditNote.setID(viewInterface.getObjectID());
        LoadingPanel.loading(true);
        InvoiceService.App.get().updateCreditNote(creditNote, new AbstractAsyncCallback<SaveResult>() {
            public void failure(Throwable caught) {
                setEnabledButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), accountingStrings.creditNote()), Info.Type.INFO);

                if (result.isPaymentExist()) {
                    Info.show(accountingMessages.youCantEditCNwhichHasPayment(), Info.Type.WARNING);
                } else if (result.isInvoiceExist()) {
                    Info.show(accountingMessages.creditNoteWithThisNumberIsAlreadyExists(), Info.Type.WARNING);
                } else {
                    viewInterface.setObjectID(result.getId());

                    if (Constants.OPEN.equals(invoiceStatus)) {
                        sendToClient(result.getId());
                    } else {
                        if (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType())) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEINVOICE_ADDED, result, viewInterface.getView());
                        } else {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEINVOICE_ADDED, result, viewInterface.getView());
                        }
                        viewInterface.getView().closeTab();
                    }
                }

                LoadingPanel.loading(false);
                setEnabledButtons(true);
            }
        });
    }

    protected void pdfVersion(final Panel hp, Integer templateId) {
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

    private void generatePDF(Panel hp, Integer pdfTemplateID) {
        String pdfURL = CommandConstants.PDF_URL + (Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType()) ? "/receivableCreditNoteViewPDFHandler" : "/payableCreditNoteViewPDFHandler");
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        NewInvoice creditNoteData = viewInterface.getFormData(Constants.DRAFT, true);
        creditNoteData.setID(viewInterface.getObjectID());
        creditNoteData.setPdfTemplateID(pdfTemplateID);
        new PDFTransferObject(post, creditNoteData);
        post.submit();
    }

    public boolean validation(String status) {
        int errors = 0;
        errors += !Validation.validateLookUpRequired(viewInterface.getCrmAccountLookUp()) ? 1 : 0;

        if (DRAFT.equals(status)) {
            return errors == 0;
        }
        errors += !Validation.validateTextBoxRequired(viewInterface.getNumberTxtBox()) ? 1 : 0;
        errors += !Validation.validateDate(viewInterface.getDueDatePicker(), new HTML(), true) ? 1 : 0;

        if (data.isApprover() && (data.getPaidAmount() == null || data.getPaidAmount() == BigDecimal.ZERO)) {
            if (!viewInterface.getApproverLookUp().isValid()) {
                errors++;
            }
        }
        if (viewInterface.getDatePicker() != null && viewInterface.getDatePicker().getDate() != null) {
            DateUtil.resetTime(viewInterface.getDatePicker().getDate());
        }
        if (viewInterface.getDueDatePicker() != null && viewInterface.getDueDatePicker().getDate() != null) {
            DateUtil.getDayLastTime(viewInterface.getDueDatePicker().getDate());
        }
        errors += !viewInterface.getProductTable().validation() ? 1 : 0;

        if (!viewInterface.validateProjectMandatory()) {
            errors++;
        }

        if (errors > 0) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, wfmStrings.unableToSave());
            messageBox.open();
            return false;
        }

        if (viewInterface.getFormParameters().isExternalForm(AccountingConstants.COPY_INVOICE_TO_CREDITNOTE) && viewInterface.getFormParameters().getMaximalAmount() != null) {
            if (viewInterface.getProductTable().getTotalInInvoiceCurrency().compareTo(viewInterface.getFormParameters().getMaximalAmount()) > 0) {
                Info.show(accountingMessages.creditNoteTotalAmountShouldBeLess(AccountingUtils.get().formatPrice(viewInterface.getFormParameters().getMaximalAmount()) + ""), Info.Type.WARNING);
                return false;
            }
        }

        if (!viewInterface.getFormParameters().isFromGettingStarted() && viewInterface.getConversionDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterconversionDate(accountingStrings.creditNote()), Info.Type.WARNING);
            return false;
        }

        if (Utils.isSalesLocked() && DateUtils.getTransactionLockDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate(accountingStrings.creditNote(), Utils.getTransactionLockDate()), Info.Type.WARNING);
            return false;
        }

        if (PAYABLE.equals(viewInterface.getFormParameters().getType()) && !validateApplicableTaxTypeForPurchase()) {
            Info.show("Selected VAT is not applicable for the VAT treatment of this transaction.", Info.Type.WARNING);
            errors++;
        } else if (RECEIVABLE.equals(viewInterface.getFormParameters().getType()) && !validateApplicableTaxTypeForSale()) {
            Info.show("The tax rate for export of goods/services outside the GCC should be Zero.", Info.Type.WARNING);
            errors++;
        }
        if (!validateApplicableTypeForUK()) {
            Info.show("The tax rate for export of goods/services outside the UK should be Zero.", Info.Type.WARNING);
            errors++;
        }
        errors += viewInterface.validateReason();
//        errors += viewInterface.validatePaymentTypeCode();
        return errors == 0;
    }

    public void sendToClient(Integer id) {
        Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
        SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + CREDIT_NOTE_CATEGORY + "/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + false);

        setEnabledButtons(true);
    }

    private void onChangeInvoiceNumber(TypeItem typeItem) {
        InvoiceService.App.get().generateNewNumberData(Constants.RECEIVABLE.equals(viewInterface.getFormParameters().getType()) ? Constants.CREDIT_NOTE : Constants.PAYABLE.equals(viewInterface.getFormParameters().getType()) ? "DEBIT_NOTE" : null, new DateNonConvertable(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date()), new AbstractAsyncCallback<InvoiceNumberData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(InvoiceNumberData result) {
                super.success(result);
                applyCreditNoteNumberData(result, typeItem != null ? typeItem : data.getTypeItem());
            }
        });
    }

    private void setCreditNoteCustomFields() {
        invoiceService.getInvoiceCustomFieldItems(viewInterface.getCrmAccountLookUp().getSelectedItemID(), ViewName.CreditNote, new AsyncCallback<NewInvoice>() {
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
