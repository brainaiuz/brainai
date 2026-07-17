package com.edatasite.workforce.gwt.invoice.client.ui.view.salequote;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.BankAccountViewPopup;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PostFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
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
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceServiceAsync;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteServiceAsync;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_FROM_EXISTING_DATA;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_FROM_SO_TO_SQ;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_PO_TO_SQ;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TERMS_TYPE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SALES_ORDER_APPROVE_EMAIL_SEND;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SALES_ORDER_SUBMIT_AND_EMAIL_SEND;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/1/12
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesQuoteFormPresenter extends InvoiceQuoteFormPresenter {

    private final InvoiceServiceAsync invoiceService = InvoiceService.App.get();
    private final ClientServiceAsync clientService = ClientService.App.get();
    private final QuoteServiceAsync quoteService = QuoteService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd MMM yyyy");
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM dd, yyyy");

    private final SalesQuoteViewInterface viewInterface;

    protected Property property;

    private NewInvoice newInvoice;

    private Command approveCommand;
    private Command approveAndSendCommand;

    private Command approveSOCommand;
    private Command approveSOAndSendCommand;
    private boolean isSendEmail = false;
    private boolean isSalesOrderSaveApproveBtnClick = false;

    SalesQuoteFormPresenter(SalesQuoteViewInterface viewInterface) {
        super(viewInterface);
        this.viewInterface = viewInterface;
    }

    @Override
    public void bindUI() {
        initBankAccountItems(null);

        generateData();

        if (!viewInterface.isEditForm()) {
            LoadingPanel.loading(true);

            if ("lead".equals(viewInterface.getFormParameters().getCrmFormName())) {
                createClientFromLead();
            } else if ("account".equals(viewInterface.getFormParameters().getCrmFormName()) || "contact".equals(viewInterface.getFormParameters().getCrmFormName())) {
                createClientFromAccount();
            } else {
                loadData();
            }
        } else {
            loadData();
        }

        addFormListeners();
    }

    private void initFormHandlers() {
        viewInterface.getCrmAccountLookUp().getSuggestBox().addSelectionHandler(event -> {
            Integer selectedID = viewInterface.getCrmAccountLookUp().getSelectedItemID();

            if (selectedID != null) {
                setClientData(selectedID, true);
                setQuoteCustomFields(selectedID);
            }
            setRelatedPriceLevel(selectedID, null);
            viewInterface.getProductTable().clearProjectFromLineItems();
        });


        viewInterface.getBankAccountListBox().addValueChangeHandler(event -> onBankAccountChange());

        viewInterface.getBankAccountDetailLink().addClickHandler(sender -> {
            BankAccountViewPopup accountViewPopup = new BankAccountViewPopup(viewInterface.getBankAccountListBox().getSelectedId());
            accountViewPopup.center();
            initBankAccountItems(viewInterface.getBankAccountListBox().getSelectedId());
        });

        viewInterface.getLeadLookUp().getSuggestBox().addSelectionHandler(selectionEvent -> {
            if (viewInterface.getLeadLookUp().getSelectedItemID() != null) {
                getOrCreateCrmAccountFromLead(viewInterface.getLeadLookUp().getSelectedItemID());
            }
        });

        viewInterface.getPriceLevelDropdown().addValueChangeHandler(vch -> onChangePriceLevel(newInvoice));

        viewInterface.getTaxCalcListBox().addValueChangeHandler(changeEvent -> viewInterface.getProductTable().onTaxCalculationTypeChange(viewInterface.getTaxCalcListBox().getSelectedId(), true));

        viewInterface.getProjectLookUp().getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (viewInterface.getNumberData() != null && viewInterface.getNumberData().isWithProject()) {
                viewInterface.getNumberData().setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
                viewInterface.getNumberTxtBox().setText(viewInterface.getNumberData().getInvoiceNumber());
            }
//                viewInterface.getManagerLookUp().clear();
        });

//        viewInterface.getProjectLookUp().getSuggestBox().addKeyUpHandler(new KeyUpHandler() {
//            @Override
//            public void onKeyUp(KeyUpEvent event) {
//                viewInterface.getManagerLookUp().clear();
//            }
//        });

        viewInterface.getDatePicker().addChangeHandler(event -> {
            if (newInvoice != null && newInvoice.getDueDays() != null && viewInterface.getDueDatePicker() != null) {
                viewInterface.getDueDatePicker().setDate(DateUtil.addDays(viewInterface.getDatePicker().getDate(), newInvoice.getDueDays()));
            }
            generateTermsConditions(null, dateFormat.format(viewInterface.getDatePicker().getDate()));
            if (!viewInterface.isEditForm()) {
                onChangeOrderQuoteNumber(null);
            }
        });

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
                generateTermsConditions(null, dateFormat.format(viewInterface.getDatePicker().getDate()));
            }

            @Override
            public boolean isEditForm() {
                return viewInterface.isEditForm();
            }
        });

        viewInterface.getCustomerBalanceLink().addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customerBalance|customerBalance/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + CrmAccountItem.CUSTOMER,
                wfmStrings.balance() + ": " + viewInterface.getCrmAccountLookUp().getSelectedItem().getName()));

        viewInterface.getProductTable().getPaymentTermsConditionsListBox().addValueChangeHandler(changeEvent -> generateTermsConditions(customerSupplierItem, dateFormat.format(viewInterface.getDatePicker().getDate())));

        viewInterface.getSalesOrderButton().addClickHandler(sender -> {
            enabledDisableSaleOrderB(false);
            save(Constants.SALE_ORDER);
        });

        approveSOCommand = () -> {
            enabledDisableSaleOrderB(false);
            enableDisableButtons(false);
            isSalesOrderSaveApproveBtnClick = true;
            save(Constants.SALE_ORDER);
        };

        approveSOAndSendCommand = () -> {
            enabledDisableSaleOrderB(false);
            enableDisableButtons(false);
            isSendEmail = true;
            isSalesOrderSaveApproveBtnClick = false;
            save(Constants.SALE_ORDER);
        };

        if (viewInterface.isEditForm()) {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                enableDisableButtons(false);
                update(Constants.DRAFT);
            });

            viewInterface.getSubmitToManagerButton().addClickHandler(sender -> {
                enableDisableButtons(false);
                update(Constants.SUBMITTED_TO_MANAGER);
            });

            approveCommand = () -> {
                enableDisableButtons(false);
                save(viewInterface.isSalesOrder() ? Constants.SALE_ORDER : Constants.APPROVE);
            };

            approveAndSendCommand = () -> {
                //if (validatePdfTemplateBeforeSending()) {
                enableDisableButtons(false);
                update(Constants.OPEN);
                //}
            };
        } else {
            viewInterface.getSaveButton().addClickHandler(sender -> {
                enableDisableButtons(false);
                save(Constants.DRAFT);
            });
            viewInterface.getSubmitToManagerButton().addClickHandler(sender -> {
                //if (validatePdfTemplateBeforeSending()) {
                enableDisableButtons(false);
                save(Constants.SUBMITTED_TO_MANAGER);
                //}
            });
            approveCommand = () -> {
                enableDisableButtons(false);
                save(viewInterface.isSalesOrder() ? Constants.SALE_ORDER : Constants.APPROVE);
            };

            approveAndSendCommand = () -> {
                //if (validatePdfTemplateBeforeSending()) {
                enableDisableButtons(false);
                save(Constants.OPEN);
                //}
            };
        }

        {
            //for PDf
            List<SplitButtonItem> pdfButtonItems = generatePdfTemplates(newInvoice);

            if (Utils.hasRole(ADMIN)) {
                pdfButtonItems = pdfButtonItems != null ? pdfButtonItems : new ArrayList<>();
                pdfButtonItems.add(new SplitButtonItem("PDF_CUSTOMIZATION", wfmStrings.customize(), () -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#pdftemplate|summary/null/" + (viewInterface.isSalesOrder() ? PdfTemplateTypeEnum.SALES_ORDER.name() : PdfTemplateTypeEnum.SALES_QUOTE.name()))));
            }
            viewInterface.getSplitButtonPdf().addItemList(pdfButtonItems);
            //End Pdf
        }
    }

    /*private boolean validatePdfTemplateBeforeSending() {
        if (viewInterface.getPdfTemplateBox() != null) {
            ValidityResponse validity = viewInterface.getPdfTemplateBox().validate();
            if (!validity.isValid()) {
                for (String errorMessage : validity.getErrorMessages()) {
                    Info.show(errorMessage, Info.Type.WARNING);
                }
                return false;
            }
        }
        return true;
    }*/


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
                onBankAccountChange();
            }
        });
    }

    private void onBankAccountChange() {
//        viewInterface.getBankAccountDetailLink().setVisible(viewInterface.getBankAccountListBox().getSelectedIndex() != 0);
    }

    private void loadData() {
        viewInterface.getFormParameters().setSaleQuote(!viewInterface.isSalesOrder());

        QuoteService.App.get().getAllQuoteData(viewInterface.getFormParameters(), new AbstractAsyncCallback<NewInvoice>() {
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
                viewInterface.getProductTable().setQuoteComissionEnabled(result.isQuoteComissionEnabled());
                viewInterface.getProductTable().setDoubleDiscountEnabled(result.isDoubleDiscountEnabled());
                viewInterface.getProductTable().setItemCustomFields(result.getItemCustomFields());
                viewInterface.getProductTable().setNewInvoice(newInvoice);
                viewInterface.initProductsTableData(result);
                //If PO is converted to SQ, recalculate product table. Otherwise PO items taxes will not be applied
                if (COPY_PO_TO_SQ.equals(viewInterface.getFormParameters().getExternalFormID())) {
                    viewInterface.getProductTable().calculate(true);
                }
                initFormHandlers();

                if (result.isQuoteComissionEnabled()) {
                    viewInterface.createComissionAllocateItem();
                }
                viewInterface.initWidgetMap(result);
                viewInterface.setFormData(result);
                viewInterface.generateForm(result.getLayoutHTML());
                renderButtons(result);

                TypeItem typeItem = result.getTypeItem();
                customerSupplierItem = typeItem;
                if (typeItem != null && typeItem.getTaxItem() != null) {
                    viewInterface.getProductTable().setCustomerOrSupplierTaxItem(typeItem.getTaxItem());
                } else {
                    viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
                }
                if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered() && typeItem != null && typeItem.getPlaceOfSupply() != null) {
                    configurePlaceOfSupply(typeItem, typeItem.getPlaceOfSupply(), viewInterface.getPlaceOfSupplyWidget());
                }
                if (customerSupplierItem != null && result.hasAccess()) {
                    viewInterface.getCrmAccountLookUp().addItem(customerSupplierItem);
                    viewInterface.getCrmAccountLookUp().setEnabled(false);
                    setClientItems(customerSupplierItem, viewInterface.getCrmAccountLookUp() != null && viewInterface.getCrmAccountLookUp().getSelectedItemID() == null, viewInterface.getCrmAccountLookUp() != null && viewInterface.getCrmAccountLookUp().getSelectedItemID() == null);
                }

                setRelatedPriceLevel(typeItem != null ? typeItem.getId() : null, result.getPriceLevel() != null ? result.getPriceLevel().getId() : null);
                initBankAccountItems(result.getBankAccount() != null ? result.getBankAccount().getId() : typeItem != null ? typeItem.getBankAccountID() : null);
                if (!viewInterface.isEditForm()) {
                    onChangeOrderQuoteNumber(typeItem);

                }
                getAndApplyTermsConditions(result);
                getAndApplyIntroduction(result);

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
    }

    private void renderButtons(NewInvoice result) {
        List<SplitButtonItem> splitButtonItems = new ArrayList<>();
        String statusCode = result.getStatusCode();
        SplitButtonItem emailButtonItem;//for Split Button
        SplitButtonItem saveButtonItem;
        if (viewInterface.isSalesOrder()) {
            if (viewInterface.getFormParameters().isEditForm()) {
                if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || (SUBMITTED_TO_MANAGER.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                    viewInterface.getSaveButton().setVisible(true);
                }
                if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                    if (REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)) {
                        saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                        saveButtonItem.ensureDebugId("saveButtonItem");
                        splitButtonItems.add(saveButtonItem);
                    } else {
                        saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveSOCommand, true);
                        saveButtonItem.ensureDebugId("saveButtonItem");
                        splitButtonItems.add(saveButtonItem);
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
//                        viewInterface.getApproveSplitButton().setVisible(false);
                    } else {
                        viewInterface.getApproveSplitButton().setVisible(true);
                    }
                } else {
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode)) {
                        viewInterface.getSaveButton().setVisible(true);
                    }
                    saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveSOCommand, true);
                    emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveSOAndSendCommand, false);
                    saveButtonItem.ensureDebugId("saveButtonItem");
                    emailButtonItem.ensureDebugId("emailButtonItem");
                    splitButtonItems.add(saveButtonItem);
                    if (Utils.hasPermission(SALES_ORDER_APPROVE_EMAIL_SEND)) {
                        splitButtonItems.add(emailButtonItem);
                    }
                }
            } else {
                if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                    viewInterface.getSubmitToManagerButton().setVisible(!result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()) && COPY_FROM_EXISTING_DATA != viewInterface.getFormParameters().getExternalFormID());
                }
                if (result.isApprover()) {
                    saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                    saveButtonItem.ensureDebugId("saveButtonItem");
                    splitButtonItems.add(saveButtonItem);
                } else {
                    saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveSOCommand, true);
                    emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveSOAndSendCommand, false);
                    saveButtonItem.ensureDebugId("saveButtonItem");
                    emailButtonItem.ensureDebugId("emailButtonItem");
                    splitButtonItems.add(saveButtonItem);
                    if (Utils.hasPermission(SALES_ORDER_APPROVE_EMAIL_SEND)) {
                        splitButtonItems.add(emailButtonItem);
                    }
                }
                viewInterface.getSaveButton().setVisible(true);
            }
        } else {
            if (viewInterface.getFormParameters().isEditForm()) {
                if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || (SUBMITTED_TO_MANAGER.equals(statusCode) && !result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()))) {
                        viewInterface.getSaveButton().setVisible(true);
                    }
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode) || OPEN.equals(statusCode)) {
                        saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                        saveButtonItem.ensureDebugId("saveButtonItem");
                        splitButtonItems.add(saveButtonItem);
                    }
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || MANAGER_REJECT.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || SUBMITTED_TO_MANAGER.equals(statusCode)) {
                        emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0);
                        emailButtonItem.ensureDebugId("emailButtonItem");
                        splitButtonItems.add(emailButtonItem);
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
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode)) {
                        viewInterface.getSaveButton().setVisible(true);
                    }
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || OPEN.equals(statusCode) || CLIENT_APPROVE.equals(statusCode)) {
                        splitButtonItems.add(new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true));
                        //saveAndApproveButton.setVisible(true);
                    }
                    if (DRAFT.equals(statusCode) || REJECT.equals(statusCode) || APPROVE.equals(statusCode) || CLIENT_APPROVE.equals(statusCode) || OPEN.equals(statusCode)) {
                        splitButtonItems.add(new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, splitButtonItems.size() == 0));
                    }
                }
            } else {
                if (result.getCurrentApproverSelectItem() != null && result.getCurrentApproverSelectItem().getId() != null) {
                    viewInterface.getSubmitToManagerButton().setVisible(!result.getCurrentApproverSelectItem().getId().equals(Utils.getUserID()) && !(COPY_FROM_EXISTING_DATA == viewInterface.getFormParameters().getExternalFormID() || COPY_PO_TO_SQ == viewInterface.getFormParameters().getExternalFormID()));
                }
                viewInterface.getSaveButton().setVisible(true);
                saveButtonItem = new SplitButtonItem(APPROVE, wfmStrings.saveAndApprove(), approveCommand, true);
                emailButtonItem = new SplitButtonItem(APPROVE_AND_SEND, accountingStrings.approveAndSendToClient(), approveAndSendCommand, false);
                saveButtonItem.ensureDebugId("saveButtonItem");
                emailButtonItem.ensureDebugId("emailButtonItem");
                splitButtonItems.add(saveButtonItem);
                splitButtonItems.add(emailButtonItem);
            }
        }
        viewInterface.getApproveSplitButton().addItemList(splitButtonItems);
    }


    private void addFormListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_EDIT, viewInterface.getView(), (sender, args) -> setClientData((Integer) args, false));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, viewInterface.getView(), (sender, args) -> setClientData((Integer) args, true));
    }

    private void setClientData(final Integer id, final Boolean add) {
        invoiceService.getClientOrSupplier(id, Constants.RECEIVABLE, new AbstractAsyncCallback<TypeItem>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(TypeItem typeItem) {
                customerSupplierItem = typeItem;
                setClientItems(typeItem, add, true);

                if (GCC_COUNTRIES.contains(Utils.getCompanyrCountryCode()) && Utils.isVatRegistered()) {
                    configurePlaceOfSupply(typeItem, typeItem.getPlaceOfSupply(), viewInterface.getPlaceOfSupplyWidget());
                }
                if (!viewInterface.isEditForm()) {
                    onChangeOrderQuoteNumber(typeItem);
                }
                viewInterface.getProductTable().setCustomerDefaultWarehouseAndDepartment(typeItem.getDefaultDepartment(), typeItem.getDefaultWarehouse());
                if (viewInterface.getTermsAndDuePanel() != null && customerSupplierItem != null && customerSupplierItem.getTermsItem() != null) {
                    viewInterface.getTermsAndDuePanel().setData(TERMS_TYPE, viewInterface.getTermsAndDuePanel().getDueDate(), customerSupplierItem.getTermsItem());
                }
            }
        });
    }

    private void setClientItems(final TypeItem item, final Boolean add, final boolean setCurrency) {

        if (item.getId() != null) {
            if (add) {
                viewInterface.getCrmAccountWidgets().presenter.setAddValues(item, setCurrency && !viewInterface.getFormParameters().isExternalForm(COPY_FROM_EXISTING_DATA));
            } else {
                viewInterface.getCrmAccountWidgets().presenter.setEditValues(item, setCurrency && !viewInterface.getFormParameters().isExternalForm(COPY_FROM_EXISTING_DATA));
            }
        }
        generateTermsConditions(item, dateFormat.format(viewInterface.getDatePicker().getDate()));

        if (viewInterface.getProjectLookUp() != null) {
            viewInterface.getProjectLookUp().clear();
        }

        if (item.getTaxItem() != null) {
            viewInterface.getProductTable().setCustomerOrSupplierTaxItem(item.getTaxItem());
        } else {
            viewInterface.getProductTable().resetCustomerOrSupplierTaxItem();
        }
        viewInterface.setCustomerBalance(item.getSupplierCustomerBalance());

        if (item.getSupplierCustomerBalance() >= 0) {
            viewInterface.getCustomerBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
            viewInterface.getCustomerBalanceLink().setText(AccountingUtils.get().formatPrice(item.getSupplierCustomerBalance()));
        } else {
            viewInterface.getCustomerBalanceLink().getElement().getStyle().setProperty("pointerEvents", "visible");
            viewInterface.getCustomerBalanceLink().setText("(" + AccountingUtils.get().formatPrice((-1) * item.getSupplierCustomerBalance()) + ")");
        }
    }

    private void applyQuoteNumberData(InvoiceNumberData result, TypeItem item) {
        if (result != null) {
            if (result.isWithDate()) {
                result.setDate(DateTimeFormat.getFormat("yyyyMMdd").format(new Date()));
            }
            if (result.isWithClient() && item != null) {
                result.setClientCode(item.getCode());
            }
            if (result.isWithProject()) {
                result.setProjectCode(viewInterface.getProjectLookUp().getSelectedProjectCode());
            }
            if (!OPPORTUNITY.equals(viewInterface.getFormParameters().getCrmFormName()) &&
                    item != null &&
                    !COPY_FROM_EXISTING_DATA.equals(viewInterface.getFormParameters().getExternalFormID())
                    && viewInterface.getFormParameters().getConvertFormType() == null) {
                viewInterface.getCurrencyWidget().setCurrency(item.getCurrencyID());
            }
            viewInterface.getNumberTxtBox().setText(result.getInvoiceNumber());
        }
        viewInterface.setNumberData(result);
    }

    private void generateData() {
        invoiceService.getInvoiceDate(0, new AbstractAsyncCallback<Date>() {
            public void success(Date result) {
                viewInterface.setConversionDate(result);
            }
        });
    }

//    private void onManagerChange() {
//        if (viewInterface.getApproverLookUp().getChosenApprovers() != null) {
//            if (viewInterface.getApproverLookUp().getSelectedItemID() == Utils.getUserID()) {
//                viewInterface.getApproveSplitButton().setVisible(true);
//                viewInterface.getSubmitToManagerButton().setVisible(false);
//            } else {
//                viewInterface.getApproveSplitButton().setVisible(false);
//                viewInterface.getSubmitToManagerButton().setVisible(true);
//            }
//
//        } else {
//            viewInterface.getApproveSplitButton().setVisible(true);
//            viewInterface.getSubmitToManagerButton().setVisible(false);
//        }
//    }

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
                        } else {
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


    /*public void onShippingMethodChange() {
        if (viewInterface.getShippingMethod().getSelectedID() != null) {
            if (shippingMap.get(viewInterface.getShippingMethod().getSelectedID()) != null) {
                viewInterface.getProductTable().setShippingMethod(shippingMap.get(viewInterface.getShippingMethod().getSelectedID()));
            } else {
                viewInterface.getProductTable().setShippingMethod((ShippingMethod) viewInterface.getShippingMethod().getShippingMethodLookUp().getSelectedData());
            }
        } else {
            viewInterface.getProductTable().setShippingMethod(null);
        }
    }*/

    private void getAndApplyTermsConditions(final NewInvoice invoice) {
        String type = viewInterface.isSalesOrder() ? Constants.SALE_ORDER_CODE : Constants.SALE_QUOTE;
        invoiceService.getPaymentInstructions(type, new AbstractAsyncCallback<SelectItem[]>() {
            public void failure(Throwable caught) {
            }

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
                        String paymentInstruction = invoice.getInstance().get("paymentInstruction");
                        if (paymentInstruction != null && !"".equals(paymentInstruction) && !paymentInstruction.equals(viewInterface.getProductTable().getPaymentInstruction().getText())) {
                            viewInterface.getProductTable().getPaymentInstruction().setText(paymentInstruction);
                        }
                    }
                    if (result.length > 1) {
                        viewInterface.getProductTable().getPaymentTermsConditionsListBox().setVisible(true);
                    }
                }
            }
        });
    }

    private void getAndApplyIntroduction(final NewInvoice invoice) {
        Integer externalFormId = viewInterface.getFormParameters().getExternalFormID();
        boolean isCopied = (COPY_FROM_EXISTING_DATA.equals(externalFormId) || COPY_FROM_SO_TO_SQ.equals(externalFormId));
        if (invoice.getIntroduction() != null && viewInterface.isEditForm()) {
            viewInterface.getIntroduction().setText(invoice.getIntroduction());

        } else if (viewInterface.isSalesOrder() && invoice.isCopySOIntroduction() && isCopied) {
            viewInterface.getIntroduction().setText(invoice.getIntroduction());
        } else {
            String type = viewInterface.isSalesOrder() ? Constants.SALE_ORDER_INTR : Constants.SALE_QUOTE_INTR;
            invoiceService.getPaymentIntroduction(type, new AbstractAsyncCallback<SelectItem[]>() {
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

    private void generateTermsConditions(TypeItem clientItem, String startDate) {
        String paymentMethod = null, dueDayStr = null, dueDateStr = null;
        Date current = viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date();

        Date dueDate = viewInterface.getTermsAndDuePanel().getDueDate();
        String terms = null;
        if (viewInterface.getTermsAndDuePanel().getInvoiceTerms() != null && viewInterface.getTermsAndDuePanel().getInvoiceTerms().getName() != null) {
            terms = viewInterface.getTermsAndDuePanel().getInvoiceTerms().getName();
        }
        if (dueDate != null) {
            dueDate.setHours(0);
            dueDate.setMinutes(0);
            dueDate.setSeconds(0);
            int i = 0;
            while (current.before(dueDate)) {
                i++;
                current = DateUtil.addDays(current, 1);
            }
            dueDateStr = dateTimeFormat.format(dueDate);
            dueDayStr = String.valueOf(i != 0 ? i : 1);
        }
        if (viewInterface.getCrmAccountLookUp().getSelectedItemID() != null && clientItem != null) {
            paymentMethod = clientItem.getPaymentType() != null ? clientItem.getPaymentType() : "";
        }

        if (viewInterface.getProductTable().getPaymentTermsConditionsListBox().isSomethingSelected()) {
            String template = PaymentTermsConditionsUtil.generateSelectedTemplate(viewInterface.getProductTable().getPaymentTermsConditionsTemplate(),
                    dueDayStr, dueDateStr, paymentMethod, startDate, null, null, viewInterface.getNumberTxtBox().getText(), terms);
            viewInterface.getProductTable().getPaymentInstruction().setText(template);
        }
    }

    private void createClientFromLead() {
        clientService.createClientFromLead(viewInterface.getFormParameters().getExternalObjectID(), null, new AbstractAsyncCallback<TypeItem>() {
            public void success(TypeItem client) {
                viewInterface.getCrmAccountLookUp().addItem(client);
                viewInterface.getCrmAccountLookUp().getTextBox().setEnabled(false);
                viewInterface.getFormParameters().setExternalObjectID(client.getId());
                loadData();
            }
        });
    }

    private void getOrCreateCrmAccountFromLead(Integer leadID) {
        clientService.getOrCreateCrmAccountFromLead(leadID, new AsyncCallback<TypeItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(TypeItem client) {
                viewInterface.getCrmAccountLookUp().addItem(client);
                viewInterface.getCrmAccountLookUp().setSelected(client);
                viewInterface.getFormParameters().setExternalObjectID(client.getId());
                setClientData(client.getId(), true);
                setRelatedPriceLevel(client.getId(), null);

            }
        });
    }

    private void createClientFromAccount() {
        clientService.createClientFromCrmAccount(viewInterface.getFormParameters().getExternalObjectID(), null, false, new AbstractAsyncCallback<TypeItem>() {
            public void success(TypeItem client) {
                viewInterface.getCrmAccountLookUp().addItem(client);
                viewInterface.getCrmAccountLookUp().getTextBox().setEnabled(false);
                viewInterface.getFormParameters().setExternalObjectID(client.getId());
                loadData();
            }
        });
    }

    private void save(final String quoteStatus) {
        if (!validation(quoteStatus)) {
            enabledDisableSaleOrderB(true);
            enableDisableButtons(true);
            return;
        }

        //validate custom fields
        if (!DRAFT.equals(quoteStatus) && !viewInterface.validateCustomFields()) {
            enabledDisableSaleOrderB(true);
            enableDisableButtons(true);
            return;
        }
        if (Constants.SALE_ORDER.equals(quoteStatus)) {

        }
        if ((Constants.APPROVE.equals(quoteStatus) || (viewInterface.isSalesOrder() && isSalesOrderSaveApproveBtnClick)) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.RESERVE_QUOTE_ITEM_ENABLE) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) {
                        quoteService.validateItemsInStock(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new DateNonConvertable(viewInterface.getDatePicker().getDate()), new DateNonConvertable(viewInterface.getTermsAndDuePanel().getDueDate()), new AsyncCallback<String[]>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                enabledDisableSaleOrderB(true);
                                enableDisableButtons(true);
                            }

                            @Override
                            public void onSuccess(String[] items) {
                                if (items.length > 0) {
                                    enabledDisableSaleOrderB(true);
                                    enableDisableButtons(true);

                                    if (viewInterface.getApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() == null) {
                                        Validation.validateLookUpRequired(viewInterface.getApproverLookUp().getFirstApproverLookUp());
                                        alertStockItemsMessage2(items);
                                    } else {
                                        alertStockItemsMessage(items, quoteStatus);
                                    }
                                } else {
                                    saveData(quoteStatus);
                                }
                            }
                        });
                    } else {
                        saveData(quoteStatus);
                    }
                }

                @Override
                public void onCancel() {
                    enabledDisableSaleOrderB(true);
                    enableDisableButtons(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.addCloseHandler(closeEvent -> enableDisableButtons(true));
            wfmMessageBox.open();
        } else {
            if (Utils.hasGenericAccess(GenericSettingsEnum.RESERVE_QUOTE_ITEM_ENABLE) && APPROVE.equals(quoteStatus) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) {
                quoteService.validateItemsInStock(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new DateNonConvertable(viewInterface.getDatePicker().getDate()), new DateNonConvertable(viewInterface.getTermsAndDuePanel().getDueDate()), new AsyncCallback<String[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        enabledDisableSaleOrderB(true);
                        enableDisableButtons(true);
                    }

                    @Override
                    public void onSuccess(String[] items) {
                        if (items.length > 0) {
                            enabledDisableSaleOrderB(true);
                            enableDisableButtons(true);

                            if (viewInterface.getApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() == null) {
                                Validation.validateLookUpRequired(viewInterface.getApproverLookUp().getFirstApproverLookUp());
                                alertStockItemsMessage2(items);
                            } else {
                                alertStockItemsMessage(items, quoteStatus);
                            }
                        } else {
                            saveData(quoteStatus);
                        }
                    }
                });
            } else {
                saveData(quoteStatus);
            }
        }
    }

    private void saveData(final String quoteStatus) {
        if (!viewInterface.isEditForm()) {
            LoadingPanel.loading(true);
            quoteService.saveSaleQuote(viewInterface.getFormData(quoteStatus.equals(Constants.OPEN) ? Constants.APPROVE : quoteStatus, true), new AbstractAsyncCallback<SaveResult>() {
                public void failure(Throwable caught) {
                    enabledDisableSaleOrderB(true);
                    enableDisableButtons(true);
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(SaveResult result) {
                    enabledDisableSaleOrderB(true);
                    enableDisableButtons(true);

                    if (result.isInvoiceExist()) {
                        addExistingQuoteNumberListener();
                        Info.show((viewInterface.isSalesOrder()) ? accountingMessages.orderWithThisNumberIsAlreadyExists() : accountingMessages.quoteWithThisNumberIsAlreadyExists(), Info.Type.WARNING);
                    } else if (result.getExceededCreditLimit()) {
                        WfmMessageBox creditLimitExceedMessage = new WfmMessageBox(IconEnum.WARN, Action.OK);
                        String message = accountingMessages.creditLimitQuoteMessage(viewInterface.getCrmAccountLookUp().getSelectedItem().getName(), AccountingUtils.get().formatPrice(result.getCreditLimit()), AccountingUtils.get().formatPrice(result.getRemainingBalance()));
                        creditLimitExceedMessage.setMessage(message);
                        creditLimitExceedMessage.open();
                    } else {
                        viewInterface.getFormParameters().setObjectID(result.getId());
                        viewInterface.setObjectID(result.getId());
                        if (quoteStatus.equals(Constants.OPEN) || (!viewInterface.isSalesOrder() && quoteStatus.equals(Constants.SUBMITTED_TO_MANAGER))) {
                            viewInterface.getSaveButton().setVisible(false);
                            WfmMessageBox continueButton = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, null, accountingStrings.getPropertyContinue(), new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    sendToClient(viewInterface.getObjectID(), quoteStatus);
                                }
                            });
                            continueButton.setTitle(wfmStrings.information());
                            if (Constants.SUBMITTED_TO_MANAGER.equals(quoteStatus)) {
                                continueButton.setMessage(viewInterface.isSalesOrder() ? viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySubmitted(), accountingStrings.salesOrder()) : viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySubmitted(), wfmStrings.salesQuote()));
                            } else {
                                continueButton.setMessage(viewInterface.isSalesOrder() ? viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyApproved(), accountingStrings.salesOrder()) : viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyApproved(), wfmStrings.salesQuote()));
                            }
                            continueButton.open();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                        } else {
                            Info.show(viewInterface.isSalesOrder() ? viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySubmitted(), accountingStrings.salesOrder()) : viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullySubmitted(), wfmStrings.salesQuote()));
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALESORDER_ADDED, result, viewInterface.getView());
                            if (viewInterface.isSalesOrder()) {
                                if (quoteStatus.equals(Constants.SUBMITTED_TO_MANAGER)) {
                                    /*final WfmMessageBox continueButton = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, null, accountingStrings.getPropertyContinue(), new CloseHandler() {
                                        @Override
                                        public void onSubmit() {
                                            sendToClient(viewInterface.getObjectID(), quoteStatus);
                                        }
                                    });
                                    continueButton.setTitle(accountingStrings.information());
                                    continueButton.setMessage(viewInterface.isSalesOrder() ? viewInterface.getProperty().getSingular(accountingStrings.submittedSuccessfullySalesOrder(), accountingStrings.salesOrder()) : viewInterface.getProperty().getSingular(accountingStrings.submittedSuccessfullySalesQuote(), accountingStrings.salesQuote()));
                                    continueButton.open();*/
                                    if (Utils.hasPermission(SALES_ORDER_SUBMIT_AND_EMAIL_SEND)) {
                                        sendToClient(viewInterface.getObjectID(), quoteStatus);
                                    } else {
                                        viewInterface.getView().closeTab();
                                    }
                                } else if (Constants.DRAFT.equals(quoteStatus)) {
                                    viewInterface.getView().closeTab();
                                } else {
                                    viewInterface.getView().closeTab();
                                    viewInterface.getView().goTo(Constants.SALE_ORDER_CODE + "|summary/" + viewInterface.getObjectID(),
                                            viewInterface.getNumberData() != null && viewInterface.getNumberData().getInvoiceNumber() != null
                                                    ? viewInterface.getNumberData().getInvoiceNumber()
                                                    : "");
                                }
                            } else if (Constants.APPROVE.equals(quoteStatus)) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                                viewInterface.getView().closeTab();
                                viewInterface.getView().goTo(Constants.SALE_QUOTE + "|summary/" + result.getId(), viewInterface.getNumberData() != null && viewInterface.getNumberData().getInvoiceNumber() != null ? viewInterface.getNumberData().getInvoiceNumber() : "");
                            } else {
                                viewInterface.getView().closeTab();

                                if (!DRAFT.equals(quoteStatus)) {
                                    viewInterface.getView().goTo(Constants.SALE_QUOTE + "|summary/" + viewInterface.getObjectID(), viewInterface.getNumberData() != null && viewInterface.getNumberData().getInvoiceNumber() != null ? viewInterface.getNumberData().getInvoiceNumber() : "");
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                            }
                        }
                        if (viewInterface.getFormParameters() != null && viewInterface.getFormParameters().getConvertFormType() != null) {
                            saveConvertedRelations(result.getId(), result.getNumber());
                        }
                    }
                    if (Constants.APPROVE.equals(quoteStatus)
                            || Constants.SUBMITTED_TO_MANAGER.equals(quoteStatus)
                            || Constants.MANAGER_REJECT.equals(quoteStatus)) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, viewInterface.getView());
                    }
                    LoadingPanel.loading(false);
                }
            });
        } else {
            if (quoteStatus.equals(Constants.OPEN)) {
                sendToClient(viewInterface.getObjectID(), quoteStatus);
                enabledDisableSaleOrderB(true);
                enableDisableButtons(true);
            } else if (viewInterface.isSalesOrder()) {
                updateData(quoteStatus);
            } else {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo);
                message.setTitle(wfmStrings.warning());
                message.setMessage(viewInterface.isSalesOrder() ? viewInterface.getProperty().getSingular(accountingStrings.salesOrderAlreadyApproved()) : viewInterface.getProperty().getSingular(accountingStrings.salesQuoteAlreadyApproved()));
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        updateData(quoteStatus);
                    }

                    @Override
                    public void onCancel() {
                        enabledDisableSaleOrderB(true);
                        enableDisableButtons(true);
                    }
                });
                message.open();
            }
        }
    }

    private void update(final String status) {
        if (!validation(status)) {
            enableDisableButtons(true);
            return;
        }
        //validate custom fields
        if (!DRAFT.equals(status) && !viewInterface.validateCustomFields()) {
            enableDisableButtons(true);
            return;
        }
        if (Constants.APPROVE.equals(status) && Utils.isDoubleMessageEnable()) {
            WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouWantToSaveChanges(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.RESERVE_QUOTE_ITEM_ENABLE) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) {
                        quoteService.validateItemsInStock(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new DateNonConvertable(viewInterface.getDatePicker().getDate()), new DateNonConvertable(viewInterface.getTermsAndDuePanel().getDueDate()), new AsyncCallback<String[]>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                enableDisableButtons(true);
                            }

                            @Override
                            public void onSuccess(String[] items) {
                                if (items.length > 0) {
                                    enableDisableButtons(true);

                                    if (viewInterface.getApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() == null) {
                                        Validation.validateLookUpRequired(viewInterface.getApproverLookUp().getFirstApproverLookUp());
                                        alertStockItemsMessage2(items);
                                    } else {
                                        alertStockItemsMessage(items, status);
                                    }
                                } else {
                                    updateData(status);
                                }
                            }
                        });
                    } else {
                        updateData(status);
                    }
                }

                @Override
                public void onCancel() {
                    enableDisableButtons(true);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.addCloseHandler(closeEvent -> enableDisableButtons(true));
            wfmMessageBox.open();
        } else {
            if ((Utils.hasGenericAccess(GenericSettingsEnum.RESERVE_QUOTE_ITEM_ENABLE) && (APPROVE.equals(status) || OPEN.equals(status))) && !Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SALES_QUOTE_PICKLIST)) {
                quoteService.validateItemsInStock(viewInterface.getProductTable().getQuantityItemsForValidate(), viewInterface.getObjectID(), new DateNonConvertable(viewInterface.getDatePicker().getDate()), new DateNonConvertable(viewInterface.getTermsAndDuePanel().getDueDate()), new AsyncCallback<String[]>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        enableDisableButtons(true);
                    }

                    @Override
                    public void onSuccess(String[] items) {
                        if (items.length > 0) {
                            enableDisableButtons(true);

                            if (viewInterface.getApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp() != null && viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() == null) {
                                Validation.validateLookUpRequired(viewInterface.getApproverLookUp().getFirstApproverLookUp());
                                alertStockItemsMessage2(items);
                            } else {
                                alertStockItemsMessage(items, status);
                            }
                        } else {
                            updateData(status);
                        }
                    }
                });
            } else {
                updateData(status);
            }
        }

    }

    private void updateData(final String status) {
        NewInvoice invoice = viewInterface.getFormData(status, true);
        invoice.setID(viewInterface.getObjectID());
        LoadingPanel.loading(true);
        quoteService.updateSaleQuote(invoice, new AbstractAsyncCallback<SaveResult>() {
            public void failure(Throwable caught) {
                enabledDisableSaleOrderB(true);
                enableDisableButtons(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(SaveResult result) {
                enabledDisableSaleOrderB(true);
                enableDisableButtons(true);

                if (result.isInvoiceExist()) {
                    addExistingQuoteNumberListener();
                    Info.show(accountingMessages.quoteWithThisNumberIsAlreadyExists(), Info.Type.WARNING);
                } else if (status.equals(Constants.OPEN)) {
                    viewInterface.setObjectID(result.getId());
                    sendToClient(result.getId(), status);
                } else {
                    viewInterface.setObjectID(result.getId());
                    if (viewInterface.isSalesOrder()) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALESORDER_ADDED, result, viewInterface.getView());
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEORDER_APPROVAL, result, viewInterface.getView());
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), accountingStrings.salesOrder()));
                    } else {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALEQUOTE_ADDED, result, viewInterface.getView());
                        Info.show(viewInterface.getProperty().getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.salesQuote()), Info.Type.INFO);
                    }
                    viewInterface.getView().closeTab();
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void addExistingQuoteNumberListener() {
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
            /*new AccountingComposeView(SALES_QUOTE_MANAGER_CATEGORY,
                    viewInterface.getApproverLookUp().getFirstApproverLookUp() != null ? viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() : null,
                    id, null, viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null, false);*/

            Integer clientID = viewInterface.getApproverLookUp().getFirstApproverLookUp() != null ? viewInterface.getApproverLookUp().getFirstApproverLookUp().getSelectedItemID() : null;
            Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + (viewInterface.isSalesOrder() ? SALES_ORDER_CATEGORY : SALES_QUOTE_MANAGER_CATEGORY) + "/" + clientID + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + false);

        } else {
            /*new AccountingComposeView(viewInterface.isSalesOrder() ? SALES_ORDER_CATEGORY : SALES_QUOTE_CATEGORY,
                    viewInterface.getCrmAccountLookUp().getSelectedItemID(), id, null,
                    viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null, false);*/

            String type = viewInterface.isSalesOrder() ? SALES_ORDER_CATEGORY : SALES_QUOTE_CATEGORY;
            Integer pdfTemplateID = viewInterface.getPdfTemplateBox() != null ? viewInterface.getPdfTemplateBox().getSelectedTemplateID() : null;
            SinksContainerFactory.entryPoint.onHistoryChanged("accountingemailcompose|add/add/" + type + "/" + viewInterface.getCrmAccountLookUp().getSelectedItemID() + "/" + id + "/" + null + "/" + pdfTemplateID + "/" + false);
        }
    }

    public boolean validation(String status) {
        int errors = 0;
        errors += !Validation.validateLookUpRequired(viewInterface.getCrmAccountLookUp()) ? 1 : 0;


        if (DRAFT.equals(status)) {
            return errors == 0;
        }

        if (viewInterface.getPlaceOfSupplyWidget() != null) {
            if (!viewInterface.getPlaceOfSupplyWidget().validate()) {
                errors++;
            }
        }

        /*if (viewInterface.getPlaceOfSupplyBox() != null
                && customerSupplierItem != null
                && customerSupplierItem.getTaxTreatment() != null
                && customerSupplierItem.getTaxTreatment().getDescription() != null
                && !(NON_GCC.equals(customerSupplierItem.getTaxTreatment().getDescription())
                || GCC_NON_VAT_REGISTERED.equals(customerSupplierItem.getTaxTreatment().getDescription())
                || GCC_VAT_REGISTERED.equals(customerSupplierItem.getTaxTreatment().getDescription()))) {
//            errors += !Validation.validateDataListBoxRequired(viewInterface.getPlaceOfSupplyBox()) ? 1:0;
            errors += !Validation.validateWfmDropdown(viewInterface.getPlaceOfSupplyBox()) ? 1 : 0;
        }*/

        errors += !viewInterface.getProductTable().validation(null, true) ? 1 : 0;
        errors += !Validation.validateTextBoxRequired(viewInterface.getNumberTxtBox()) ? 1 : 0;

        if (newInvoice.isApprover()) {
            if (!viewInterface.getApproverLookUp().isValid()) {
                errors++;
            }
        }
        errors += !viewInterface.getTermsAndDuePanel().validate() ? 1 : 0;

        if (!viewInterface.validateCustomFields()) {
            errors++;
        }

        errors += viewInterface.validateSystemCustomFields() ? 0 : 1;

        if (viewInterface.getDatePicker().getDate() != null) {
            DateUtil.resetTime(viewInterface.getDatePicker().getDate());
        }
        if (viewInterface.getTermsAndDuePanel().isDueTypeSelected() && viewInterface.getTermsAndDuePanel().getDueDate() != null) {
            DateUtil.getDayLastTime(viewInterface.getTermsAndDuePanel().getDueDate());
        }

        if (viewInterface.getDatePicker().getDate() != null && viewInterface.getTermsAndDuePanel().getDueDate() != null
                && !Validation.validateDateOrder(viewInterface.getDatePicker().getDate(), viewInterface.getTermsAndDuePanel().getDueDate())) {
            WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, accountingStrings.checkDateMessage());
            messageBox.open();
            return false;
        }
        if (!viewInterface.validateProjectMandatory()) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.unableToSave(), Info.Type.WARNING);
            return false;
        }

        if (viewInterface.getConversionDate().after(viewInterface.getDatePicker().getDate())) {
            Info.show((viewInterface.isSalesOrder() ? accountingStrings.orderDateShouldBeAfterConversationDate() : accountingStrings.quoteDateShouldBeAfterConversationDate()), Info.Type.WARNING);
            return false;
        }
        if (!validateApplicableTaxTypeForSale()) {
            Info.show("The tax rate for export of goods/services outside the GCC should be Zero.", Info.Type.WARNING);
            errors++;
        }
        if (!validateApplicableTypeForUK()) {
            Info.show("The tax rate for export of goods/services outside the UK should be Zero.", Info.Type.WARNING);
            errors++;
        }

        return errors == 0;
    }

    public void pdfVersion(final Panel hp, Integer pdfTemplateID) {

        if (!validation(null)) {
            return;
        }
        generatePDF(hp, pdfTemplateID);
    }

    private void alertStockItemsMessage(String[] items, final String status) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OkCancel, accountingMessages.youDoNotHaveEnoughQuantityToReserve(itemNames.toString()), accountingStrings.getPropertyContinue(), new CloseHandler() {
            @Override
            public void onSubmit() {
                if (viewInterface.getObjectID() != null) {
                    updateData(status);
                } else {
                    saveData(status);
                }
            }
        });
        messageBox.setWidth(560);
        messageBox.setTitle(accountingStrings.notEnoughQuantity());
        messageBox.open();
    }

    private void alertStockItemsMessage2(String[] items) {
        StringBuilder itemNames = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            if (i != 0) {
                itemNames.append(", ");
            }
            itemNames.append("\"").append(items[i]).append("\"");
        }
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.OK, true);
        messageBox.setWidth(560);
        messageBox.setTitle(accountingStrings.notEnoughQuantity());
        messageBox.setMessage(accountingMessages.youDoNotHaveEnoughQuantityToReserveSubmitToManager(itemNames.toString()));
        messageBox.open();
    }

    private void generatePDF(Panel hp, Integer pdfTemplateID) {
        String pdfURL = CommandConstants.PDF_URL + (viewInterface.isSalesOrder() ? "/saleOrderViewPDFHandler" : "/saleQuoteViewPDFHandler");
        PostFormPanel post = new PostFormPanel(pdfURL, "_blank");
        hp.add(post);
        NewInvoice quoteData = viewInterface.getFormData(Constants.DRAFT, true);
        quoteData.setID(viewInterface.getObjectID());
        quoteData.setPdfTemplateID(pdfTemplateID);
        new PDFTransferObject(post, quoteData);
        post.submit();
    }

    private void enableDisableButtons(boolean b) {
        if (viewInterface.getSaveButton() != null) {
            viewInterface.getSaveButton().setEnabled(b);
        }
        if (viewInterface.getSubmitToManagerButton() != null) {
            viewInterface.getSubmitToManagerButton().setEnabled(b);
        }

        if (viewInterface.getApproveSplitButton() != null) {
            viewInterface.getApproveSplitButton().setEnabled(b);
        }
//        if (viewInterface.getApproveAndSendButton() != null) {
//            viewInterface.getApproveAndSendButton().setEnabled(b);
//        }
    }

    private void enabledDisableSaleOrderB(boolean b) {
        if (viewInterface.getSalesOrderButton() != null) {
            viewInterface.getSalesOrderButton().setEnabled(b);
        }
    }

    private void saveConvertedRelations(Integer _objectId, String number) {
        ArrayList<RelationItem> relationItems = new ArrayList<>();

        relationItems.add(new RelationItem(null, _objectId, viewInterface.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, number, viewInterface.getFormParameters().getConvertFormId(), CrmConstants.CRM_EVENT_CALLOG.equals(viewInterface.getFormParameters().getConvertFormType()) ? RelationItem.TYPE_EVENT : viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number));
        AllInOneService.App.get().getAdditionalRelations(_objectId, viewInterface.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, number, viewInterface.getFormParameters().getConvertFormId(), CrmConstants.CRM_EVENT_CALLOG.equals(viewInterface.getFormParameters().getConvertFormType()) ? RelationItem.TYPE_EVENT : viewInterface.getFormParameters().getConvertFormType(), newInvoice != null ? newInvoice.getFromName() : number, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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
                    AllInOneService.App.get().saveRelations(viewInterface.isSalesOrder() ? RelationItem.TYPE_SALEORDER : RelationItem.TYPE_SALEQUOTE, _objectId, number, relationItems, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
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

    private void onChangeOrderQuoteNumber(TypeItem typeItem) {
        InvoiceService.App.get().generateNewNumberData(viewInterface.isSalesOrder() ? Constants.SALE_ORDER : Constants.SALE_QUOTE, new DateNonConvertable(viewInterface.getDatePicker().getDate() != null ? viewInterface.getDatePicker().getDate() : new Date()), new AbstractAsyncCallback<InvoiceNumberData>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(InvoiceNumberData result) {
                super.success(result);
                applyQuoteNumberData(result, typeItem != null ? typeItem : newInvoice.getTypeItem());
            }
        });
    }

    private void setQuoteCustomFields(Integer selectedID){
        quoteService.getQuoteCustomFieldItems(selectedID, viewInterface.isSalesOrder(), new AsyncCallback<NewInvoice>() {
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
