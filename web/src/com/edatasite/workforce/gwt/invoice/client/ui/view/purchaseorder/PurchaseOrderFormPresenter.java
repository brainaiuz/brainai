package com.edatasite.workforce.gwt.invoice.client.ui.view.purchaseorder;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.ui.view.InvoiceQuoteFormPresenter;
import com.edatasite.workforce.gwt.invoice.client.ui.view.saleinvoice.TermsAndDueProvider;
import com.edatasite.workforce.gwt.profile.client.ui.view.PaymentTermsConditionsUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_FROM_EXISTING_DATA;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_FROM_SQ_SO_TO_PO;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TERMS_TYPE;

/**
 * User: Sherzod
 * Date: 2/3/12
 * Time: 6:11 PM
 */
public class PurchaseOrderFormPresenter extends InvoiceQuoteFormPresenter implements Constants, PermissionConstants {

    private final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private final PurchaseOrderViewInterface viewInterface;
    private final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd MMM yyyy");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM dd, yyyy");

    private NewInvoice orderItem;
    //for Split Button
    private SplitButtonItem saveButtonItem, emailButtonItem;
    private Command approveCommand;
    private Command approveAndSendCommand;
    //    private Anchor approveAction;
    private HashMap<Integer, ShippingMethod> shippingMap;

    public PurchaseOrderFormPresenter(PurchaseOrderViewInterface viewInterface) {
        super(viewInterface);
        this.viewInterface = viewInterface;
    }

    @Override
    public void bindUI() {
        shippingMap = new HashMap<>();
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

        QuoteService.App.get().getAllQuoteData(viewInterface.getFormParameters(), new AbstractAsyncCallback<NewInvoice>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(NewInvoice result) {
                orderItem = result;
                viewInterface.initCustomFields(result);
                viewInterface.initSystemCustomFields(result);
                viewInterface.initPdfTemplates(result);
                viewInterface.getProductTable().setNewInvoice(result);
                viewInterface.getProductTable().setRoundingModeDisabled(result.isRoundingModeDisabled());
                viewInterface.getProductTable().setDoubleTaxEnabled(result.isDoubleTaxEnabled());
                viewInterface.getProductTable().setItemCustomFields(result.getItemCustomFields());

                viewInterface.initProductsTableData(result);
                //viewInterface.getProductTable().setReverseChargeApplicable(orderItem != null && orderItem.getTypeItem() != null && orderItem.getTypeItem().isReverseChargeApplicable());
                initFormHandlers();

                viewInterface.initWidgetMap(result);
                viewInterface.generateForm(result.getLayoutHTML());
                viewInterface.setFormData(result);
                if (viewInterface.isEditForm() && (result.isDeleteAndAddDsiabled() || result.isConvertedToInvoice())) {
                    viewInterface.getTaxCalcListBox().setEnabled(false);
                    viewInterface.getProductTable().setEnabled(false, true, viewInterface.getFormParameters().getExternalFormID());
                }
                renderButtons(result);

                customerSupplierItem = result.getTypeItem();

                if (viewInterface.isEditForm()) {
                    setPaymentMethods(result.getPaymentMethodID());
                } else {
                    if (result.getTypeItem() != null && result.getTypeItem().getPaymentTypeID() != null) {
                        setPaymentMethods(result.getTypeItem().getPaymentTypeID());
                    } else {
                        setPaymentMethods(result.getPaymentMethodID());
                    }
                }
                setRelatedPriceLevel(result.getTypeItem() != null ? result.getTypeItem().getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);
                getAndApplyTermsConditions(result);
                if (!viewInterface.isEditForm()) {
                    applyOrderNumberData(result.getNumberData(), result.getTypeItem());
                }

                AtomicBoolean firstClick = new AtomicBoolean(true);
                viewInterface.getLinkWidget().addClickHandler(event -> {
                    if (firstClick.get()) {
                        viewInterface.getLinkingUtils().getAddLinkSideNavBox();
                        viewInterface.getLinkingUtils().getAddLinkSideNavBox().setSelectedRelations(result.getRelations(), false);
                        firstClick.set(false);
                    } else {
                        viewInterface.getLinkingUtils().getAddLinkSideNavBox().show();
                    }

                });
                viewInterface.getLinkWidget().setBadgeCount(result.getRelations().size());

                LoadingPanel.loading(false);
            }
        });

        addFormListeners();
    }

    private void renderButtons(NewInvoice result) {
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        if (viewInterface.getFormParameters().isEditForm()) {
            String statusCode = result.getStatusCode();

            boolean received = PARTIAL_RECEIVED.equals(statusCode) || RECEIVED.equals(statusCode) || INVOICED.equals(statusCode);
            if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                if (Utils.hasPermission(PO_DRAFT) && !result.isDeleteAndAddDsiabled() && (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || (SUBMITTED_TO_MANAGER.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID())))) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                if ((DRAFT.equals(statusCode) || REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)) ||
                        (received && result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                    if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_SAVE_AND_APPROVE_BUTTON : ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON)) {
                        approveCommand = () -> {
                            setEnabledButtons(false);
                            update(received ? statusCode : Constants.APPROVE);
                        };
                        boolean hasDocument = result.isConvertedToInvoice() || result.isDeleteAndAddDsiabled();
                        saveButtonItem = new SplitButtonItem(APPROVE,hasDocument ? wfmStrings.update() : wfmStrings.saveAndApprove(), approveCommand, true);
                        saveButtonItem.ensureDebugId("saveButtonItem_purchase");
                        splitButtonItems.add(saveButtonItem);
                    }
                }
                if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)) {
                    if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_APPROVE_AND_SEND_BUTTON : ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON)) {
                        emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0);
                        emailButtonItem.ensureDebugId("emailButtonItem_purchase");
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
                    viewInterface.getSubmitToManagerButton().setVisible(true);
                    viewInterface.getApproveSplitButton().setVisible(false);
                } else {
                    viewInterface.getApproveSplitButton().setVisible(true);
                }

            } else {
                if (Utils.hasPermission(PO_DRAFT) && (DRAFT.equals(statusCode) || REJECT.equals(statusCode))) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                boolean canApprove = DRAFT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || received;
                if (canApprove) {
                    if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_SAVE_AND_APPROVE_BUTTON : ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON)) {
                        approveCommand = () -> {
                            setEnabledButtons(false);
                            update(received ? statusCode : Constants.APPROVE);
                        };
                        splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true));
                    }
                }
                if (canApprove) {
                    if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_APPROVE_AND_SEND_BUTTON : ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON)) {
                        approveAndSendCommand = () -> {
                            setEnabledButtons(false);
                            update(received ? statusCode : Constants.APPROVE_AND_SEND);
                        };
                        splitButtonItems.add(new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0));
                    }
                }
            }
        } else {
            if (Utils.hasPermission(PO_DRAFT)) {
                viewInterface.getSaveButton().setVisible(true);
            }
            if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                viewInterface.getSubmitToManagerButton().setVisible(!result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()) && !(COPY_FROM_EXISTING_DATA == viewInterface.getFormParameters().getExternalFormID() || COPY_FROM_SQ_SO_TO_PO == viewInterface.getFormParameters().getExternalFormID()));
            }
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_SAVE_AND_APPROVE_BUTTON : ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON)) {
                saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                saveButtonItem.ensureDebugId("approveButtonItem_purchase");
                splitButtonItems.add(saveButtonItem);
            }
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_APPROVE_AND_SEND_BUTTON : ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON)) {
                emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() <= 0);
                emailButtonItem.ensureDebugId("emailButtonItem_purchase");
                splitButtonItems.add(emailButtonItem);
            }
        }
        viewInterface.getApproveSplitButton().addItemList(splitButtonItems);
    }

    private void initFormHandlers() {
        viewInterface.getCrmAccountLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            Integer selectedID = viewInterface.getCrmAccountLookUp().getSelectedItemID();

            if (selectedID != null) {
                InvoiceService.App.get().getClientOrSupplier(viewInterface.getCrmAccountLookUp().getSelectedItemID(), Constants.PAYABLE, new AsyncCallback<TypeItem>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(TypeItem result) {
                        viewInterface.getProductTable().setCustomerSupplierItem(result);
                        viewInterface.getProductTable().setCustomerDefaultWarehouseAndDepartment(result.getDefaultDepartment(), result.getDefaultWarehouse());

                        if (result != null) {
                            if (Constants.GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                                configurePlaceOfSupply(result, null, viewInterface.getPlaceOfSupplyWidget());
                            }

                            if (result.getSupplierCustomerBalance() >= 0) {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText(AccountingUtils.get().formatPrice(result.getSupplierCustomerBalance()));
                            } else {
                                viewInterface.getSupplierBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
                                viewInterface.getSupplierBalanceLink().setText("(" + AccountingUtils.get().formatPrice((-1) * result.getSupplierCustomerBalance()) + ")");
                            }

                            if (viewInterface.getTermsAndDueDatePanel() != null && result != null && result.getTermsItem() != null) {
                                viewInterface.getTermsAndDueDatePanel().setData(TERMS_TYPE, viewInterface.getTermsAndDueDatePanel().getDueDate(), result.getTermsItem());
                            }
                        }
                    }
                });

                setSupplierData(selectedID, true, true);
                setRelatedPriceLevel(selectedID, null);
                setPurchaseOrderCustomFields();
            } else {
                viewInterface.getPaymentTypeDropdown().setSelected(null);
            }
        });

        viewInterface.getDatePicker().addChangeHandler(event -> {

            if (!viewInterface.isEditForm()) {
                onChangeOrderNumber(null);
            }
        });

        viewInterface.getSupplierBalanceLink().addClickHandler(clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("supplierBalance|supplierBalance/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + CrmAccountItem.SUPPLIER,
                    wfmStrings.balance() + ": " + viewInterface.getCrmAccountLookUp().getSelectedItem().getName());
        });
        viewInterface.getProjectLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            viewInterface.getManagerLookUp().clear();
            if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject()) {
                viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
            }
        });
        viewInterface.getProjectLookUp().getSuggestBox().addKeyUpHandler(event -> viewInterface.getManagerLookUp().clear());

        viewInterface.getPriceLevelDropdown().addValueChangeHandler(vch -> onChangePriceLevel(orderItem));

        viewInterface.getTaxCalcListBox().addValueChangeHandler(changeEvent -> viewInterface.getProductTable().onTaxCalculationTypeChange(viewInterface.getTaxCalcListBox().getSelectedId(), true));
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

            }

            @Override
            public boolean isEditForm() {
                return viewInterface.isEditForm();
            }
        });
        if (viewInterface.isEditForm()) {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                update(Constants.DRAFT);
            });

            approveCommand = () -> {
                setEnabledButtons(false);
                update(Constants.APPROVE);
            };

            approveAndSendCommand = () -> {
                setEnabledButtons(false);
                update(Constants.APPROVE_AND_SEND);
            };

            viewInterface.getSubmitToManagerButton().addClickHandler(sender -> update(Constants.SUBMITTED_TO_MANAGER));
        } else {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                setEnabledButtons(false);
                save(Constants.DRAFT);
            });

            approveCommand = () -> {
                setEnabledButtons(false);
                save(Constants.APPROVE);
            };

            approveAndSendCommand = () -> {
                setEnabledButtons(false);
                save(Constants.APPROVE_AND_SEND);
            };

            viewInterface.getSubmitToManagerButton().addClickHandler(sender -> save(Constants.SUBMITTED_TO_MANAGER));
        }

//        viewInterface.getPdfButton().addClickHandler(sender -> pdfVersion(viewInterface.getHTMLPanel()));
        viewInterface.getProductTable().getPaymentTermsConditionsListBox().addValueChangeHandler(changeEvent -> generateTermsConditions(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate())));

        ClientService.App.get().getPaymentMethod(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                viewInterface.getPaymentTypeDropdown().addItems(result);
            }
        });

        /**
         * Generate pdf button functionality:         *
         * We moved pdf list to split button from form field
         */
        {
            //for PDf
            List<SplitButtonItem> pdfButtonItems = generatePdfTemplates(orderItem);

            if (Utils.hasRoles(Constants.ADMIN)) {
                pdfButtonItems = pdfButtonItems != null ? pdfButtonItems : new ArrayList<>();
                pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + PdfTemplateTypeEnum.PURCHASE_ORDER.name())));
            }
            viewInterface.getSplitButtonPdf().addItemList(pdfButtonItems);
            //End Pdf
        }
    }

    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_EDIT, viewInterface.getView(), (sender, args) -> setSupplierData((Integer) args, false, true));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SUPPLIER_ADD, viewInterface.getView(), (sender, args) -> setSupplierData((Integer) args, true, true));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, viewInterface.getView(), (sender, args) -> setDropShipToAddressData((Integer) args));
        // bu boshqa line itemlar uchun ham ishlayveradi
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INVOICE_LINE_ITEM_FOCUS, viewInterface.getView(), (sender, args) -> showMessageToUser());

    }
    private void showMessageToUser() {
        viewInterface.getMessageToUser().setVisible(true);
    }

    private void generateData() {
        InvoiceService.App.get().getInvoiceDate(0, new AbstractAsyncCallback<Date>() {
            public void success(Date result) {
                viewInterface.setConversionDate(result);
            }
        });
    }

    private void getAndApplyTermsConditions(final NewInvoice invoice) {
        InvoiceService.App.get().getPaymentInstructions(Constants.PURCHASE_ORDER, new AbstractAsyncCallback<SelectItem[]>() {
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
                    dueDayStr, dueDateStr, paymentMethod, startDate, null, null, null, terms);
            viewInterface.getProductTable().getPaymentInstruction().setText(template);
        }
    }

    /*
    Advertisement is the best way to sell stuffs successfully in a short period.  When born needs or requirements,
    one begins to seek information everywhere. Advertising products immediately get stuck in that time.
    */

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
        customerSupplierItem = item;

        if (item.getId() != null) {
            if (add) {
                viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency);
            } else {
                viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency);
            }
            if (item.getCurrencyID() != null) {
                viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());
            }
            if (item.getPaymentTypeID() != null) {
                viewInterface.getPaymentTypeDropdown().setSelected(item.getPaymentTypeID());
            }
            if (!viewInterface.isEditForm()) {
                onChangeOrderNumber(item);
            }
            if (item.getTaxItem() != null) {
                viewInterface.getProductTable().setCustomerOrSupplierTaxItem(item.getTaxItem());
            } else {
                viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
            }
        }
    }

    private void setDropShipToAddressData(Integer clientId) {
        if (clientId != null) {
            if (viewInterface.getCrmAccountWidgets().getDropShipToCustomerLookUp() != null) {
                viewInterface.getCrmAccountWidgets().presenter.loadDropShipToAddressData(clientId, null);
            }
        }
    }

    private void applyOrderNumberData(InvoiceNumberData result, TypeItem item) {
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
            viewInterface.getNumberTxtBox().setText(result.getInvoiceNumber());
        }
        viewInterface.setNumberData(result);
    }

    private void setPaymentMethods(final Integer paymentMethodID) {
        ClientService.App.get().getPaymentMethod(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] result) {
                viewInterface.getPaymentTypeDropdown().addItems(result);
                if (paymentMethodID != null && paymentMethodID > 0) {
                    viewInterface.getPaymentTypeDropdown().setSelected(paymentMethodID);
                }
            }
        });
    }

    private void save(final String orderStatus) {
        //validate base form fields
        if (!validation(orderStatus)) {
            setEnabledButtons(true);
            return;
        }
        //validate custom fields
        if (!viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }
        if ((Constants.APPROVE.equals(orderStatus) || Constants.APPROVE_AND_SEND.equals(orderStatus)) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    saveData(orderStatus);
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
            saveData(orderStatus);
        }
    }

    private void saveData(String orderStatus) {
        String actualStatus = orderStatus;
        if (orderStatus.equals(Constants.APPROVE_AND_SEND)) {
            actualStatus = Constants.APPROVE_AND_SEND;
            orderStatus = Constants.APPROVE;
        }
        String finalOrderStatus = orderStatus;
        if (!viewInterface.isEditForm()) {
            LoadingPanel.loading(true);
            String finalActualStatus = actualStatus;
            QuoteService.App.get().savePurchaseOrder(viewInterface.getFormData(finalOrderStatus, true), new AbstractAsyncCallback<SaveResult>() {
                public void failure(Throwable caught) {
                    setEnabledButtons(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(SaveResult result) {
                    setEnabledButtons(true);
                    if (result.isInvoiceExist()) {
                        addExistingOrderNumberListener();
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.purchaseorder()), Info.Type.WARNING);
                    } else {
                        viewInterface.setObjectID(result.getId());
                        viewInterface.getFormParameters().setObjectID(result.getId());
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.purchaseorder()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, viewInterface.getView());
                        viewInterface.getView().closeTab();

                        if (!DRAFT.equals(finalOrderStatus) && !orderItem.isApprover()) {
                            viewInterface.getView().goTo(Constants.PURCHASE_ORDER + "|summary/" + viewInterface.getObjectID(), result.getNumber());
                        }
                        if (viewInterface.getFormParameters() != null && viewInterface.getFormParameters().getConvertFormType() != null) {
                            saveConvertedRelations(result.getId(), result.getNumber());
                        }

                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_FIXED_ASSET_SAVED, result.getFixedAssetID(), viewInterface.getView());
                    }
                    LoadingPanel.loading(false);
                }
            });
        } else {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo);
            message.setTitle(wfmStrings.warning());
            message.setMessage(viewInterface.getProperty().getSingular(accountingStrings.purchaseOrderAlreadySaved(), wfmStrings.purchaseorder()));
            String finalActualStatus = actualStatus;
            message.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    NewInvoice order = viewInterface.getFormData(finalOrderStatus, true);
                    order.setID(viewInterface.getObjectID());
                    QuoteService.App.get().updatePurchaseOrder(order, false, new AbstractAsyncCallback<SaveResult>() {

                        public void failure(Throwable caught) {
                            setEnabledButtons(true);
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(SaveResult result) {
                            setEnabledButtons(true);
                            LoadingPanel.loading(false);
                            if (result.isInvoiceExist()) {
                                addExistingOrderNumberListener();
                                Info.show(viewInterface.getProperty().getSingular(wfmStrings.numberAlreadyExist(), wfmStrings.purchaseorder()), Info.Type.WARNING);
                            } else {
                                viewInterface.setObjectID(result.getId());
                                if (finalOrderStatus.equals(Constants.APPROVE) || finalOrderStatus.equals(Constants.SUBMITTED_TO_MANAGER)) {
                                    sendToClient(viewInterface.getObjectID(), finalActualStatus);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, viewInterface.getView());
                                } else {
                                    Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.purchaseorder()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PURCHASEORDER_ADDED, result, viewInterface.getView());
                                    viewInterface.getView().closeTab();
                                    if (!orderItem.isApprover()) {
                                        viewInterface.getView().goTo(Constants.PURCHASE_ORDER + "|summary/" + viewInterface.getObjectID(), order.getInvoiceNumber());
                                    }
                                }
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {
                    setEnabledButtons(true);
                }
            });
            message.open();
        }
    }

    private void update(final String status) {
        //validate base form fields
        if (!validation(status)) {
            return;
        }
        //validate custom fields
        if (!viewInterface.validateCustomFields()) {
            setEnabledButtons(true);
            return;
        }
        //validate quantity and price for received data
        if (!viewInterface.getProductTable().validateUseInGrn()) {
            setEnabledButtons(true);
            return;
        }
        if ((Constants.APPROVE.equals(status) || Constants.APPROVE_AND_SEND.equals(status)) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    saveData(status);
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
            saveData(status);
        }
    }

    public void addExistingOrderNumberListener() {
        viewInterface.getNumberTxtBox().setStyleName("x-form-invalid");
        viewInterface.getNumberTxtBox().addKeyDownHandler(event -> {
            if (!"".equals(viewInterface.getNumberTxtBox().getStyleName())) {
                viewInterface.getNumberTxtBox().removeStyleName(viewInterface.getNumberTxtBox().getStyleName());
            }
        });
    }

    public void sendToClient(Integer id, String status) {
        viewInterface.getView().closeTab();
        if (Constants.SUBMITTED_TO_MANAGER.equals(status)) {
            /*new AccountingComposeView(PURCHASE_ORDER_MANAGER_CATEGORY,
                    viewInterface.getManagerLookUp().getSelectedItemID(), id, null,
                    viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null, false);*/

            Integer clientID = viewInterface.getApproverLookUp().getFirstApproverLookUp() != null ? viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() : null;
            Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + PURCHASE_ORDER_MANAGER_CATEGORY + "/" + clientID + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + false);
        } else if (Constants.APPROVE_AND_SEND.equals(status)) {
            /*new AccountingComposeView(PURCHASE_ORDER_CATEGORY,
                    viewInterface.getCrmAccountLookUp().getSelectedItemID(), id, null,
                    viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null, false);*/

            Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + PURCHASE_ORDER_CATEGORY + "/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + false);
        }

        setEnabledButtons(true);
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
                        onChangePriceLevel(orderItem);
                    } else {
                        viewInterface.getPriceLevel().setVisible(false);
                    }
                }
            });
        } else {
            viewInterface.getPriceLevel().setVisible(false);
        }
    }

    private void generatePDF(Panel hp, Integer pdfTemplateID) {
        String pdfURL = CommandConstants.PDF_URL + "/purchaseOrderViewPDFHandler";
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        NewInvoice orderData = viewInterface.getFormData(Constants.DRAFT, true);
        orderData.setID(viewInterface.getObjectID());
        orderData.setPdfTemplateID(pdfTemplateID);
        new PDFTransferObject(post, orderData);
        post.submit();
    }

    public boolean validation(String status) {
        int errors = 0;
        errors += !Validation.validateLookUpRequired(viewInterface.getCrmAccountLookUp()) ? 1 : 0;

        if (Constants.DRAFT.equals(status)) {
            return errors == 0;
        }
        errors += !Validation.validateTextBoxRequired(viewInterface.getNumberTxtBox()) ? 1 : 0;
        errors += !viewInterface.getTermsAndDueDatePanel().validate() ? 1 : 0;
        //errors += !viewInterface.getCrmAccountWidgets().validateAddress() ? 1 : 0; TODO remporary commented
        errors += viewInterface.validateSystemCustomFields() ? 0 : 1;
        errors += !viewInterface.getProductTable().validation() ? 1 : 0;

//        if (viewInterface.getManagerBox().isVisible() && !Validation.validateLookUpRequired(viewInterface.getManagerLookUp())) {
//            errors++;
//        }
        if (!viewInterface.validateCustomFields()) {
            errors++;
        }
        if (viewInterface.getDatePicker().getDate() != null) {
            DateUtil.resetTime(viewInterface.getDatePicker().getDate());
        }
        if (viewInterface.getTermsAndDueDatePanel().getDueDate() != null) {
            DateUtil.getDayLastTime(viewInterface.getTermsAndDueDatePanel().getDueDate());
        }
//        if (orderItem.isApprover()) {
//            if (!viewInterface.getApproverLookUp().isValid()) {
//                errors++;
//            }
//        }
        if (viewInterface.getDatePicker().getDate() != null && viewInterface.getTermsAndDueDatePanel().getDueDate() != null
                && !Validation.validateDateOrder(viewInterface.getDatePicker().getDate(), viewInterface.getTermsAndDueDatePanel().getDueDate())) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, viewInterface.getProperty().getSingular(accountingStrings.checkPurchaseDate(), wfmStrings.purchaseorder()));
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

        if (viewInterface.getConversionDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(viewInterface.getProperty().getSingular(accountingStrings.orderDateShouldBeAfterConversationDate(), wfmStrings.order()), Info.Type.WARNING);
            return false;
        }

        if (Utils.isPurchasesLocked() && DateUtils.getTransactionLockDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show(accountingMessages.dateShouldBeAfterClosedBeforeDate("Order", Utils.getTransactionLockDate()), Info.Type.WARNING);
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

    private void setEnabledButtons(boolean b) {

        if (viewInterface.getSaveButton() != null) {
            viewInterface.getSaveButton().setEnabled(b);
        }
        if (viewInterface.getApproveSplitButton() != null) {
            viewInterface.getApproveSplitButton().setEnabled(b);
        }
    }

    private void saveConvertedRelations(Integer _objectId, String number) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, RelationItem.TYPE_PURCHASE_ORDER, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), orderItem != null ? orderItem.getFromName() : number));
        AllInOneService.App.get().getAdditionalRelations(_objectId, RelationItem.TYPE_PURCHASE_ORDER, number, viewInterface.getFormParameters().getConvertFormId(), viewInterface.getFormParameters().getConvertFormType(), orderItem != null ? orderItem.getFromName() : number, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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

                        if (orderItem.getConvertedRelations() != null) {
                            for (RelationItem item : orderItem.getConvertedRelations()) {

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
                    AllInOneService.App.get().saveRelations(RelationItem.TYPE_PURCHASE_ORDER, _objectId, number, relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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

    private void onChangeOrderNumber(TypeItem typeItem) {
        InvoiceService.App.get().generateNewNumberData(Constants.PURCHASE_ORDER, new DateNonConvertable(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date()), new AbstractAsyncCallback<InvoiceNumberData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(InvoiceNumberData result) {
                super.success(result);
                applyOrderNumberData(result, typeItem != null ? typeItem : orderItem.getTypeItem());
            }
        });
    }
    private void setPurchaseOrderCustomFields() {
        InvoiceService.App.get().getInvoiceCustomFieldItems(viewInterface.getCrmAccountLookUp().getSelectedItemID(), ViewName.PurchaseOrder, new AsyncCallback<NewInvoice>() {
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
