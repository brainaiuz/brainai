package com.edatasite.workforce.gwt.invoice.server.app;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsPrepaymentCustomFields;
import com.edatasite.workforce.core.solr.component.PurchaseInvoiceSolrComponent;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.CustomerPrepaymentNoteManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PaymentRefundManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.PrepaymentCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting.PrepaymentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.PrePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.PrepaymentService;
import com.edatasite.workforce.gwt.invoice.server.app.prepayment.NullPrepaymentException;
import com.edatasite.workforce.gwt.invoice.server.app.prepayment.NullPrepaymentTransactionException;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.NO_TAX_CALCULATION;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PAYABLE_PREPAYMENT_REFUND;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PERCENTAGE_STR;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RECEIVABLE_PREPAYMENT;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.SPEND_MONEY;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.SPEND_RECEIVE_FORM;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;


@Service("prepaymentService")
public class PrepaymentServiceImpl implements PrepaymentService, PrepaymentServiceLocal, Constants {

    @Autowired
    protected CrmAccountManager crmAccountManager;
    @Autowired
    protected ReferenceManager referenceManager;
    @Autowired
    protected ProjectManager projectManager;
    @Autowired
    protected AccountingManager accountingManager;
    @Autowired
    @Qualifier("accountingService")
    protected AccountingServiceLocal accountingServiceLocal;
    @Autowired
    protected DepartmentManager departmentManager;
    @Autowired
    protected PrepaymentCFManager prepaymentCFManager;
    @Autowired
    protected BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    protected UserManager userManager;
    @Autowired
    protected CurrencyManager currencyManager;
    @Autowired
    protected CommonService commonService;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private PaymentRefundManager paymentRefundManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private RentalOrderManager rentalOrderManager;
    @Autowired
    private CustomerPrepaymentNoteManager customerPrepaymentNoteManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    protected SolrManager solrManager;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;

    @Override
    @Transactional
    public Integer savePrePayment(PaymentData prePaymentData, boolean isCashRefund) {
        boolean isReceivable = RECEIVABLE_PREPAYMENT.equals(prePaymentData.getType());

        EdsCrmAccount crmAccount = crmAccountManager.get(prePaymentData.getCrmAccount().getId());
        EdsInvoicePayment prePayment;
        BigDecimal balanceDifference;
        if (prePaymentData.getObjectID() != null) {
            prePayment = invoicePaymentManager.get(prePaymentData.getObjectID());
            BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(crmAccount.getObjectID(), prePayment.getObjectID(), isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE, isReceivable ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
            balanceDifference = prePaymentData.getPaymentAmount().subtract(appliedAmount).divide(prePaymentData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);

            if (balanceDifference.compareTo(ZERO) > 0 && appliedAmount.compareTo(ZERO) > 0) {
                prePaymentData.setPaymentStatus(AccountingConstants.PRE_PAYMENT_PARTIAL_APPLIED_STATUS);
            } else if (balanceDifference.compareTo(ZERO) == 0) {
                prePaymentData.setPaymentStatus(AccountingConstants.PRE_PAYMENT_APPLIED_STATUS);
            } else {
                prePaymentData.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
            }
        } else {
            prePayment = new EdsInvoicePayment();
            prePayment.setBatchPaymentID(prePaymentData.getBatchPaymentID());
            if (prePaymentData.getInvoiceID() != null) {
                prePayment.setFromInvoice(invoiceManager.get(prePaymentData.getInvoiceID()));
            }
            prePaymentData.setPaymentStatus(AccountingConstants.PRE_PAYMENT_OPEN_STATUS);
            balanceDifference = prePaymentData.getPaymentAmountInInvoiceCurrency() != null?prePaymentData.getPaymentAmountInInvoiceCurrency().divide(prePaymentData.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP):prePaymentData.getPaymentAmount();
        }

        if (balanceDifference.compareTo(ZERO) < 0) {
            return -1;
        }

        if (prePaymentData.isPostDatedTransaction()) {
            prePayment.setStatus(referenceManager.findReference(EdsInvoicePayment.INVOICEPAYMENT_STATUS, EdsInvoicePayment.POST_DATED));
        } else {
            prePayment.setStatus(null); //this is a temporary solution
        }

        if (prePaymentData.getSaleQuoteItem() != null) {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(prePaymentData.getSaleQuoteItem().getId());
            prePayment.setSaleQuote(saleQuote);
        } else if (prePaymentData.getPurchaseOrderItem() != null) {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(prePaymentData.getPurchaseOrderItem().getId());
            prePayment.setPurchaseOrder(purchaseOrder);
        }
        if (prePaymentData.getSaleInvoiceItem() != null) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(prePaymentData.getSaleInvoiceItem().getId());
            prePayment.setSaleInvoice(saleInvoice);
        }
        if (prePaymentData.getRentalOrderItem() != null) {
            EdsRentalOrder rentalOrder = rentalOrderManager.get(prePaymentData.getRentalOrderItem().getId());
            prePayment.setRentalOrder(rentalOrder);
        }

        prePayment.setCrmAccount(crmAccount);
        prePayment.setProject(prePaymentData.getProject() != null ? projectManager.get(prePaymentData.getProject().getId()) : null);
        prePayment.setDepartment(prePaymentData.getDepartment() != null ? departmentManager.get(prePaymentData.getDepartment().getId()) : null);
        prePayment.setAccount(accountingManager.get(prePaymentData.getPaymentAccount().getId()));
        if (prePaymentData.getReceivablePayable() != null) {
            prePayment.setReceivablePayable(accountingManager.get(prePaymentData.getReceivablePayable().getId()));
        }
        prePayment.setBankFee(prePaymentData.getBankFee() != null ? accountingManager.get(prePaymentData.getBankFee().getId()) : null);
        prePayment.setBankFeeType(prePaymentData.getBankFeeType());
        prePayment.setBankFeeValue(prePaymentData.getBankFeeValue());
        prePayment.setNote(prePaymentData.getNote());
        prePayment.setPaymentDate(prePaymentData.getDate().getNonConvertedDate());
        prePayment.setReference(prePaymentData.getReferenceNumber());
        prePayment.setAmount(prePaymentData.getPaymentAmount());
        prePayment.setAmountInInvoiceCurrency(prePaymentData.getPaymentAmountInInvoiceCurrency());

        //Validate if prepayment/supplierCredit with given number exists in database
        String transferType = "";
        if (prePaymentData.getType() != null && prePaymentData.getType().equals(RECEIVABLE_PREPAYMENT)) {
            transferType = "PREPAYMENT";
        } else if (prePaymentData.getType() != null && prePaymentData.getType().equals(AccountingConstants.PAYABLE_SUPPLIER_CREDIT)) {
            transferType = "SUPPLIER";
        }

        if (prePaymentData.getNumber() == null || prePaymentData.getNumber().isEmpty()) {
            BankTransferNumberData numberData = generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT");
            prePayment.setNumber(numberData.getTransferNumber());
            prePayment.setNumberInt(Integer.parseInt(numberData.getFourDigitNumber()));
        } else {
            Integer paymentIDByNumber = invoicePaymentManager.getInvoicePamyentIdIfPresent(prePaymentData.getNumber(), transferType);
            if (prePaymentData.getObjectID() == null && paymentIDByNumber != null) {
                BankTransferNumberData numberData = generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT");
                prePayment.setNumber(numberData.getTransferNumber());
                prePayment.setNumberInt(Integer.parseInt(numberData.getFourDigitNumber()));
            } else if (prePaymentData.getObjectID() != null && paymentIDByNumber != null && !prePaymentData.getObjectID().equals(paymentIDByNumber)) {
                BankTransferNumberData numberData = generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT");
                prePayment.setNumber(numberData.getTransferNumber());
                prePayment.setNumberInt(Integer.parseInt(numberData.getFourDigitNumber()));
            } else {
                prePayment.setNumber(prePaymentData.getNumber());
                prePayment.setNumberInt(prePaymentData.getIntNumber());
            }
            //End of number existance validation
        }

        if (isCashRefund) {
            invoiceServiceLocal.saveCreditNoteRefund(prePaymentData);
            return prePaymentData.getObjectID();
        } else {
            if (prePaymentData.getCurrency() != null && prePaymentData.getCurrency().getId() != null) {
                prePayment.setCurrencyID(prePaymentData.getCurrency().getId());
                prePayment.setExchangeRate(prePaymentData.getExchangeRate());
            } else {
                prePayment.setExchangeRate(BigDecimal.ONE);
            }
            prePayment.setUser(invoiceManager.getUser());
            prePayment.setType(prePaymentData.getType());
            prePayment.setPaymentStatus(prePaymentData.getPaymentStatus());
            prePayment.setPrepaymentCustomFields(saveCustomFields(prePayment.getPrepaymentCustomFields(), prePaymentData.getCustomFields()));

            invoicePaymentManager.createOrUpdate(prePayment);
            if (prePayment.getCreditNote() != null) {
                checkAndUpdateInvoiceCreditNoteStatuses(prePayment.getCreditNote());
            }

            savePaymentNote(prePaymentData, prePayment);
            if (prePaymentData.getAttachedFiles() != null && prePaymentData.getAttachedFiles().length > 0) {
                savePrepaymentAttachments(prePaymentData.getAttachedFiles(), prePayment);
            }
            if (!(prePayment.getStatus() != null && EdsInvoicePayment.POST_DATED.equals(prePayment.getStatus().getCode()))) {
                accountingServiceLocal.createTransactionForPayment(prePayment);
                createOrUpdateBankFeeTransfer(prePayment);
            }
            if (prePaymentData.getObjectID() != null) {
                baseEventPostProcessor.registerEvent(PrepaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, prePayment, userManager.getUser());
            } else {
                baseEventPostProcessor.registerEvent(PrepaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, prePayment, userManager.getUser());
            }
            return prePayment.getObjectID();
        }
    }

    private void createOrUpdateBankFeeTransfer(EdsInvoicePayment prePayment) {
        Integer existingTransferID = prePayment.getBankFeeTransferID();
        if (existingTransferID != null) {
            accountingServiceLocal.deleteBankTransfer(existingTransferID, Constants.BANK_TRANSFER_TRANSACTION);
            prePayment.setBankFeeTransferID(null);
        }

        if (prePayment.getBankFee() == null || prePayment.getBankFeeValue() == null
                || prePayment.getBankFeeValue().compareTo(ZERO) <= 0
                || prePayment.getAccount() == null
                || prePayment.getAmount() == null) {
            if (existingTransferID != null) {
                invoicePaymentManager.update(prePayment);
            }
            return;
        }

        BigDecimal feeAmount = PERCENTAGE_STR.equals(prePayment.getBankFeeType())
                ? prePayment.getAmount().multiply(prePayment.getBankFeeValue()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)
                : prePayment.getBankFeeValue();
        if (feeAmount.compareTo(ZERO) <= 0) {
            if (existingTransferID != null) {
                invoicePaymentManager.update(prePayment);
            }
            return;
        }

        NewManualTransaction tx = new NewManualTransaction();
        tx.setTransferType(SPEND_MONEY);
        tx.setFormType(SPEND_RECEIVE_FORM);
        tx.setTaxCalculationType(NO_TAX_CALCULATION);
        tx.setDate(new DateNonConvertable(prePayment.getPaymentDate()));
        tx.setNarration("Bank fee for prepayment " + prePayment.getNumber());
        tx.setReference(prePayment.getReference());
        tx.setForceValidNumberGenerate(true);

        BankTransferNumberData numberData = accountingServiceLocal.reGenerateMoneyNumber(SPEND_MONEY);
        tx.setTransferNumberData(numberData);
        tx.setNumber(numberData.getTransferNumber());
        try {
            tx.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
        } catch (NumberFormatException ignored) {
            tx.setIntNumber(null);
        }

        EdsCurrency currency = prePayment.getCurrencyID() != null
                ? currencyManager.get(prePayment.getCurrencyID())
                : financialSettingsManager.getFinancialSettings().getCurrency();
        tx.setCurrency(currency.createCurrencyItem());
        tx.setExchangeRate(prePayment.getExchangeRate() != null ? prePayment.getExchangeRate() : BigDecimal.ONE);

        EdsAccount paymentAccount = prePayment.getAccount();
        EdsBankAccount bankAccount = bankAccountManager.getBankAccountByAccountID(paymentAccount.getObjectID());
        if (bankAccount != null) {
            BankAccountItem bankItem = new BankAccountItem();
            bankItem.setId(bankAccount.getObjectID());
            bankItem.setName(paymentAccount.getName());
            tx.setBankAccountItem(bankItem);
        } else {
            tx.setCashAccount(paymentAccount.getAsSelectItem());
        }

        if (prePayment.getProject() != null) {
            tx.setProject(prePayment.getProject().getAsSelectItem());
        }

        NewManualTransactionItem item = new NewManualTransactionItem();
        AccountItem feeAccountItem = prePayment.getBankFee().createAccountItem();
        item.setAccountItem(feeAccountItem);
        item.setDescription("Bank fee");
        item.setAmount(feeAmount);
        item.setDebit(feeAmount);
        if (prePayment.getDepartment() != null) {
            item.setDepartment(prePayment.getDepartment().getAsSelectItem());
        }
        tx.setItems(new NewManualTransactionItem[]{item});

        tx.setSubtotal(feeAmount);
        tx.setTaxTotal(BigDecimal.ZERO);
        tx.setTotal(feeAmount);
        tx.setRequiredTotal(feeAmount);

        Integer transferID = accountingServiceLocal.spendOrReceiveMoney(tx);
        if (transferID != null && transferID > 0) {
            prePayment.setBankFeeTransferID(transferID);
            invoicePaymentManager.update(prePayment);
        }
    }

    private EdsPrepaymentCustomFields saveCustomFields(EdsPrepaymentCustomFields edsPrepaymentCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            if (edsPrepaymentCustomFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty())) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsPrepaymentCustomFields = new EdsPrepaymentCustomFields();
                prepaymentCFManager.create(edsPrepaymentCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsPrepaymentCustomFields, customFieldItems);
            return edsPrepaymentCustomFields;
        }
        return null;
    }

    private void checkAndUpdateInvoiceCreditNoteStatuses(EdsInvoice creditNote) {
        EdsUser user = invoiceManager.getUser();

        Integer calculationScale = financialSettingsManager.getFinancialSettings().getAccountingCalculationScale();

        List<EdsInvoicePayment> refunds = invoicePaymentManager.getRefunds(creditNote);

        BigDecimal fullPayments = ZERO;
        for (EdsInvoicePayment item : refunds) {
            if (!item.isReversed()) {
                fullPayments = fullPayments.add(item.getAmountInInvoiceCurrency() != null ? item.getAmountInInvoiceCurrency() : item.getAmount());
            }
        }

        BigDecimal totalInInvoiceCurrency = creditNote.getTotalInInvoiceCurrency() != null ? creditNote.getTotalInInvoiceCurrency()
                : creditNote.getTotal().multiply(creditNote.getExchangeRate());

        if (totalInInvoiceCurrency.setScale(calculationScale, RoundingMode.HALF_UP).compareTo(fullPayments.setScale(calculationScale, RoundingMode.HALF_UP)) <= 0) {
            Calendar companyTime = new GregorianCalendar(TimeZone
                    .getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
            creditNote.setPaidDate(companyTime.getTime());
            creditNote.setStatus(referenceManager.findReference(INVOICE_STATUS, PAID));
        } else if (totalInInvoiceCurrency.compareTo(fullPayments) >= 0 && PAID.equals(creditNote.getStatus().getCode())) {
            creditNote.setStatus(referenceManager.findReference(INVOICE_STATUS, APPROVE));
        }
        if (RECEIVABLE.equals(creditNote.getType())) {
            EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(creditNote.getObjectID());
            if (saleInvoice != null) {
                try {
                    saleInvoice.setRefunds(refunds);
                    saleInvoiceSolrComponent.index(saleInvoice);
                } catch (IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (PAYABLE.equals(creditNote.getType())) {
            EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(creditNote.getObjectID());
            if (purchaseInvoice != null) {
                try {
                    purchaseInvoice.setRefunds(refunds);
                    purchaseInvoiceSolrComponent.index(purchaseInvoice);
                } catch (IOException | SolrServerException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void savePaymentNote(PaymentData prePaymentData, EdsInvoicePayment prePayment) {
        HistoryListItem[] noteItems = prePaymentData.getHistoryList();
        List<EdsCustomerPrepaymentNote> paymentNotes = customerPrepaymentNoteManager.getPrepaymentNotesByPaymentId(prePayment.getObjectID());
        if (noteItems != null && noteItems.length > 0) {
            HashMap<Integer, Integer> existingNotesMap = new HashMap<>();
            for (HistoryListItem noteItem : noteItems) {
                if (noteItem.getObjectID() == null && noteItem.getComment() != null && !"".equals(noteItem.getComment())) {
                    EdsCustomerPrepaymentNote note = new EdsCustomerPrepaymentNote();
                    note.setComment(noteItem.getComment());
                    note.setCommentator(invoiceManager.getUser());
                    note.setPayment(prePayment);
                    note.setDate(new Date());
                    customerPrepaymentNoteManager.create(note);
                }
                if (noteItem.getObjectID() != null) {
                    existingNotesMap.put(noteItem.getObjectID(), noteItem.getObjectID());
                }
            }

            for (EdsCustomerPrepaymentNote purchaseNote : paymentNotes) {
                if (!existingNotesMap.containsKey(purchaseNote.getObjectID())) {
                    customerPrepaymentNoteManager.delete(purchaseNote);
                }
            }
        } else {
            for (EdsCustomerPrepaymentNote noteForDelete : paymentNotes) {
                customerPrepaymentNoteManager.delete(noteForDelete);
            }
        }
    }

    private void savePrepaymentAttachments(FileItem[] attachments, EdsInvoicePayment prepayment) {
        attachmentUtilsManager.saveAttachments(F_PREPAYMENT, prepayment.getObjectID(), prepayment.getObjectID(), attachments);
    }

    @Override
    @Transactional
    public PrePaymentData getPrePaymentData(Integer prePaymentID, Integer customerID, Boolean isReceivable, Boolean isCopy, Params params) {
        PrePaymentData formData = new PrePaymentData();
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        formData.setEnabledPostDatedTransaction(financialSettings != null ? financialSettings.isEnablePostedDateTransaction() : false);
        if (customerID != null) {
            EdsCrmAccount customer = crmAccountManager.get(customerID);
            if (customer != null) {
                formData.setSupplierCustomerBalance(isReceivable ? crmAccountManager.getClientBalance(customerID) : crmAccountManager.getSupplierBalance(customerID));
                if (customer.getCurrency() != null) {
                    formData.setCurrency(customer.getCurrency().createCurrencyItem());
                    if (customer.isClient() && customer.getReceivable() != null) {
                        formData.setReceivablePayable(customer.getReceivable().createAccountItem());
                    } else if (customer.isSupplier() && customer.getPayable() != null) {
                        formData.setReceivablePayable(customer.getPayable().createAccountItem());
                    }
                }
            } else {
                formData.setCurrency(currencyService.getBaseCurrency());
            }
        } else {
            formData.setCurrency(currencyService.getBaseCurrency());
        }

        if (prePaymentID != null) {
            PaymentData paymentData = new PaymentData();
            EdsInvoicePayment prePayment = invoicePaymentManager.get(prePaymentID);
            paymentData.setObjectID(prePayment.getObjectID());
            if (prePayment.getCrmAccount() != null) {
                paymentData.setCrmAccount(prePayment.getCrmAccount().getAsSelectItem());
                formData.setSupplierCustomerBalance(isReceivable ? crmAccountManager.getClientBalance(prePayment.getCrmAccount().getObjectID()) : crmAccountManager.getSupplierBalance(prePayment.getCrmAccount().getObjectID()));
            }
            paymentData.setProject(prePayment.getProject() != null ? prePayment.getProject().getAsSelectItem() : null);
            paymentData.setDepartment(prePayment.getDepartment() != null ? prePayment.getDepartment().getAsSelectItem() : null);
            if (prePayment.getAccount() != null) {
                paymentData.setPaymentAccount(prePayment.getAccount().getAsSelectItem());
            }
            paymentData.setReceivablePayable(prePayment.getReceivablePayable() != null ? prePayment.getReceivablePayable().createAccountItem() : null);
            paymentData.setBankFee(prePayment.getBankFee() != null ? prePayment.getBankFee().createAccountItem() : null);
            paymentData.setBankFeeType(prePayment.getBankFeeType());
            paymentData.setBankFeeValue(prePayment.getBankFeeValue());
            paymentData.setNote(prePayment.getNote());
            paymentData.setPaymentStatus(prePayment.getPaymentStatus());
            paymentData.setDate(new DateNonConvertable(prePayment.getPaymentDate()));
            paymentData.setReferenceNumber(prePayment.getReference());
            paymentData.setType(prePayment.getType());
            if (prePayment.getCurrencyID() != null) {
                CurrencyItem currencyItem = currencyManager.get(prePayment.getCurrencyID()).createCurrencyItem();
                paymentData.setCurrency(currencyItem);
                formData.setCurrency(currencyItem);
            }

            if (prePayment.getStatus() != null && EdsInvoicePayment.POST_DATED.equals(prePayment.getStatus().getCode())) {
                paymentData.setPostDatedTransaction(true);
            }
            paymentData.setExchangeRate(prePayment.getExchangeRate());
            paymentData.setPaymentAmount(prePayment.getAmountInInvoiceCurrency()!=null?prePayment.getAmountInInvoiceCurrency():prePayment.getAmount());

            paymentData.setCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(prePayment.getPrepaymentCustomFields(), commonService.getCompanyCustomFields(isReceivable ? ViewName.Prepayment : ViewName.Supplier)));

            if (prePayment.getNumber() != null && !"".equals(prePayment.getNumber()) && !isCopy) {
                BankTransferNumberData btnd = new BankTransferNumberData();
                btnd.setNumberString(prePayment.getNumber());
                formData.setBankTransferNumberData(btnd);

            } else {
                formData.setBankTransferNumberData(generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT"));
                BankTransferNumberData bankTransferNumberData = formData.getBankTransferNumberData();
                if (bankTransferNumberData != null) {
                    bankTransferNumberData.setNumberString(bankTransferNumberData.getTransferNumber());
                }
            }
            if (prePayment.getSaleQuote() != null) {
                SelectItem saleQuoteItem = new SelectItem(prePayment.getSaleQuote().getObjectID(), prePayment.getSaleQuote().getNumber());
                paymentData.setSaleQuoteItem(saleQuoteItem);
            } else if (prePayment.getPurchaseOrder() != null) {
                SelectItem purchaseOrderItem = new SelectItem(prePayment.getPurchaseOrder().getObjectID(), prePayment.getPurchaseOrder().getNumber());
                paymentData.setPurchaseOrderItem(purchaseOrderItem);
            } else if (prePayment.getRentalOrder() != null) {
                SelectItem purchaseOrderItem = new SelectItem(prePayment.getRentalOrder().getObjectID(), prePayment.getRentalOrder().getNumber(), RENTAL_ORDERS);
                paymentData.setRentalOrderItem(purchaseOrderItem);
            }
            if (prePayment.getSaleInvoice() != null) {
                SelectItem saleInvoiceItem = new SelectItem(prePayment.getSaleInvoice().getObjectID(), prePayment.getSaleInvoice().getNumber());
                paymentData.setSaleInvoiceItem(saleInvoiceItem);
            }
            formData.setPaymentData(paymentData);
        } else {
            formData.setBankTransferNumberData(generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT"));
        }
        if (params != null && params.getRelatedInvoiceId() != null) {
            EdsInvoice invoice = invoiceManager.get(params.getRelatedInvoiceId());
            PaymentData paymentData;
            if (formData.getPaymentData() != null) {
                paymentData = formData.getPaymentData();
            } else {
                paymentData = new PaymentData();
            }
            paymentData.setDate(invoice.getInvoiceDate() != null ? new DateNonConvertable(invoice.getInvoiceDate()) : null);
            paymentData.setCrmAccount(invoice.getClientOrSupplier().getAsSelectItem());
            paymentData.setReferenceNumber(invoice.getNumber());
            formData.setPaymentData(paymentData);
        }

        formData.setEnabledBankMultiCurrency(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED));
        return formData;
    }

    @Override
    @Transactional
    public Integer deletePrePayment(Integer objectID) {
        EdsInvoicePayment prePayment = invoicePaymentManager.get(objectID);

        if (prePayment.getBatchPaymentID() != null) {
            return -2;
        }
        boolean isReceivable = RECEIVABLE_PREPAYMENT.equals(prePayment.getType());

        BigDecimal appliedAmount = invoicePaymentManager.getAppliedPrePaymentAmounts(
                prePayment.getCrmAccount().getObjectID(),
                prePayment.getObjectID(),
                isReceivable ? AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE : AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE,
                isReceivable ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND
        );
        if (appliedAmount.compareTo(ZERO) > 0) {
            return -1;
        }

        List<EdsInvoicePayment> refundPayments = invoicePaymentManager.getAppliedPrepayments(prePayment.getCrmAccount().getObjectID(), prePayment.getObjectID(), RECEIVABLE_PREPAYMENT.equals(prePayment.getType()) ? RECEIVABLE_PREPAYMENT_REFUND : PAYABLE_PREPAYMENT_REFUND);
        if (refundPayments != null && !refundPayments.isEmpty()) {
            for (EdsInvoicePayment refund : refundPayments) {
                if (refund.getClosedAmount() != null && refund.getClosedAmount().compareTo(BigDecimal.ZERO) > 0) {
                    return -1;
                }
            }
        }

        accountingServiceLocal.deleteInvoicePaymentTransaction(prePayment);
        prePayment.setDeleted(true);
        invoicePaymentManager.update(prePayment);
        baseEventPostProcessor.registerEvent(PrepaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, prePayment, userManager.getUser());
        customerPrepaymentNoteManager.deletePaymentNotes(objectID);
        return objectID;
    }

    @Override
    @Transactional
    public ListResult<PrePaymentListItem> getPrePaymentList(ListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        filterParameter.setCustomFieldsShown(true);
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(AccountingConstants.RECEIVABLE_PREPAYMENT.equals(filterParameter.getViewType()) ? ViewName.Prepayment : ViewName.Supplier));
            filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        }

        return invoicePaymentManager.getPrePayments(filterParameter);
    }

    @Transactional
    public Integer createPaymentHistoryNotes(Integer paymentId, HistoryListItem hisItem) {
        if (paymentId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            EdsCustomerPrepaymentNote customerPrepaymentNote = new EdsCustomerPrepaymentNote();
            customerPrepaymentNote.setPayment(invoicePaymentManager.get(paymentId));
            customerPrepaymentNote.setDate(new Date());
            customerPrepaymentNote.setCommentator(user);
            customerPrepaymentNote.setComment(hisItem.getComment());
            customerPrepaymentNote.setSuperUser(ServerUtils.isSuperUser());

            customerPrepaymentNoteManager.create(customerPrepaymentNote);
            return customerPrepaymentNote.getObjectID();
        }
        return null;
    }

    public void deletePaymentHistoryNote(Integer commentID) {
        EdsCustomerPrepaymentNote customerPrepaymentNote = customerPrepaymentNoteManager.get(commentID);
        customerPrepaymentNoteManager.delete(customerPrepaymentNote);
    }


    @Override
    @Transactional
    public void savePrepaymentCellValue(PrePaymentListItem rowValue, String columnCodeName) {
        EdsInvoicePayment edsPrepayment = invoicePaymentManager.get(rowValue.getObjectID());

        EdsPrepaymentCustomFields prepaymentCF = edsPrepayment.getPrepaymentCustomFields();
        if (prepaymentCF == null) {
            prepaymentCF = new EdsPrepaymentCustomFields();
            prepaymentCFManager.create(prepaymentCF);
            edsPrepayment.setPrepaymentCustomFields(prepaymentCF);
        }
        CustomFieldsUtils.setDomenObjectFieldChange(prepaymentCF, rowValue.getCustomFieldsMap(), columnCodeName);

    }

    @Transactional
    public List<HistoryNote> getPaymentHistoryNotes(Integer objectId, String viewType) {
        if (objectId == null) {
            return null;
        }
        List<MyUpdateItem> historyItems = invoiceService.getAllHistory(objectId, viewType);
        List<HistoryNote> notes = new ArrayList<>(getPaymentNotes(objectId));

        List<HistoryNote> result = new ArrayList<>(historyItems);
        result.addAll(notes);
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HistoryListItem> getPaymentNotes(Integer objectID) {
        return customerPrepaymentNoteManager.getPaymentNotesAsHistoryListItem(objectID);
    }

    @Override
    @Transactional
    public TestRPC voidPrepayment(Integer prepaymentId, DateNonConvertable date) {
        TestRPC result = new TestRPC();
        EdsInvoicePayment prepayment = invoicePaymentManager.get(prepaymentId);
        if (prepayment == null) {
            result.setMessage("Prepayment/Supplier Credit with this id: {" + prepaymentId + "} cannot be found in database");
            result.setId(prepaymentId);
            result.setError(true);
            return result;
        }
        try {
            createVoidTransactionForPrepayment(prepayment, date.getNonConvertedDate());
        } catch (NullPrepaymentException e) {
            result.setMessage(e.getMessage());
            result.setError(true);
            return result;
        } catch (NullPrepaymentTransactionException e) {
            result.setError(true);
            result.setMessage(e.getMessage());
            return result;
        }

        prepayment.setPaymentStatus(AccountingConstants.VOID);
        invoicePaymentManager.createOrUpdate(prepayment);
        return result;
    }

    private void createVoidTransactionForPrepayment(EdsInvoicePayment prepayment, Date date) throws NullPrepaymentException, NullPrepaymentTransactionException {
        if (prepayment == null) {
            throw new NullPrepaymentException();
        }
        EdsInvoicePaymentTransaction previousTransaction = transactionManager.getTransactionByPayment(prepayment);
        if (previousTransaction == null) {
            throw new NullPrepaymentTransactionException();
        }
        EdsUser user = transactionManager.getUser();

        EdsInvoicePaymentTransaction reverseTransaction = new EdsInvoicePaymentTransaction();
        reverseTransaction.setReversalTransaction(previousTransaction);

        reverseTransaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);

        reverseTransaction.setBankStatementItem(previousTransaction.getBankStatementItem());
        reverseTransaction.setInvoicePayment(prepayment);
        reverseTransaction.setJournalDate(date);
        reverseTransaction.setPostedDate(user.getUserDate());
        reverseTransaction.setPostedBy(user);
        reverseTransaction.setReference(previousTransaction.getReference());
        reverseTransaction.setSupplier(previousTransaction.getSupplier());
        reverseTransaction.setClient(previousTransaction.getClient());
        reverseTransaction.setCurrencyID(previousTransaction.getCurrencyID());
        reverseTransaction.setExchangeRate(previousTransaction.getExchangeRate());
        reverseTransaction.setPostedBy(user);
        reverseTransaction.setName("Void for " + previousTransaction.getName());
        for (EdsTransactionItem previousTItem : previousTransaction.getTransactionItems()) {
            EdsTransactionItem reverseTItem = new EdsTransactionItem();
            reverseTItem.setDebit(previousTItem.getCredit());
            reverseTItem.setCredit(previousTItem.getDebit());
            reverseTItem.setAccount(previousTItem.getAccount());
            reverseTItem.setForeignDebit(previousTItem.getForeignCredit());
            reverseTItem.setForeignCredit(previousTItem.getForeignDebit());
            reverseTItem.setCrmAccount(previousTItem.getCrmAccount());
            reverseTItem.setTransaction(reverseTransaction);
            reverseTItem.setReconcileStatus(previousTItem.getReconcileStatus());
            reverseTransaction.addTransactionItem(reverseTItem);
        }

        transactionManager.create(reverseTransaction);
    }

    /**
     * On method implementation transferType considered to be
     * only and only either "SUPPLIER_CREDIT" or "PREPAYMENT"
     *
     * @param transferType
     * @return
     */
    @Transactional
    public BankTransferNumberData generatePrepaymentNumber(String transferType) {
        BankTransferNumberData numberData;
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = null;
        if (transferType != null) {
            intNumber = invoicePaymentManager.getLastIntNumberByType(transferType);
        }
        intNumber = (intNumber == null) ? 0 : intNumber;
        String numberingFormat = null;
        if (transferType.contains("PREPAYMENT")) {
            if (settings != null && settings.getPrNumberingFormat() != null) {
                numberingFormat = settings.getPrNumberingFormat();
                transferType = "PR";
            } else {
                transferType = "PR";
                numberingFormat = "PR_0001";
            }
        } else if (transferType.contains("SUPPLIER_CREDIT")) {
            if (settings != null && settings.getScNumberingFormat() != null) {
                numberingFormat = settings.getScNumberingFormat();
                transferType = "SC";
            } else {
                numberingFormat = "SC_0001";
                transferType = "SC";
            }
        }
        numberData = prepaymentNumberDataSettings(numberingFormat, intNumber, transferType);
        return numberData;
    }

    private BankTransferNumberData prepaymentNumberDataSettings(String numberingFormat, Integer fourDigitNumber, String defaultPrefix) {
        BankTransferNumberData bankTransferNumberData = new BankTransferNumberData();

        if (numberingFormat != null) {
            parsePrepaymentNumber(numberingFormat, bankTransferNumberData, fourDigitNumber);
        } else {
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, defaultPrefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            bankTransferNumberData.setPrefix(numberParts[0]);
            bankTransferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            bankTransferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return bankTransferNumberData;
    }

    private void parsePrepaymentNumber(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g CP_0001-05/2015 or CR_0001-05/2015
        String[] datePartNumbers = mainPartNumbers[1].split("-");  // e.g 0001-05/2015 or 0001-05/2015

        numberData.setPrefix(mainPartNumbers[0]);
        numberData.setWithDate(datePartNumbers.length == 2);

        String lastFourNumber = datePartNumbers[0];

        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber(fourDigitNumber != null ? format.format(fourDigitNumber + 1) : lastFourNumber);
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
    }

}
