package com.edatasite.workforce.rest.v2.release10.accounting;


import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferItem;
import com.edatasite.workforce.core.domain.customfields.EdsBankTransferCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.BankAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.BankReceiptsAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.CustomerOrSupplierTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.DepartmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.ProjectTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.bankReceipts.ReceiptAddDTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierShopifyTaxItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BANK_TRANSFER_TRANSACTION;

@Tag(name = "Bank receipts", description = "Bank receipts API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiBankReceiptsControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiBankReceiptsControllerV2.class);

    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;
    @Autowired
    private AccountingService accountingServiceAsync;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private VatManager vatManager;

    @Operation(summary = "Create new bank receipt")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Bank receipt"))
    @RequestMapping(value = "/bankReceipt/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ApiResult getList(@RequestBody ListParamsDTO listParams) {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.BankReceiptListPanel);
        filterParameter.setType(AccountingConstants.RECEIVE_MONEY);
        List<NewManualTransaction> list = spendReceiveMoneyManager.list(filterParameter, true);

        List<ReceiptAddDTO> results = new ArrayList<>();
        list.forEach(bt -> results.add(getRPC(spendReceiveMoneyManager.get(bt.getObjectId()))));
        return successResponse(new ResponseListData<>(results));
    }

    @Operation(summary = "Create new bank receipt")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Bank receipt"))
    @RequestMapping(value = "/bankReceipt", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_ADD})
    public ApiResult createBankReceipt(@Validated @RequestBody ReceiptAddDTO receiptAddDTO) throws RestException, ParseException {

        if (receiptAddDTO.getObjectId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object Id is specified", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (spendReceiveMoneyManager.isNumberExists(receiptAddDTO.getNumber(), null, 0)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "this number already exists", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        receiptAddDTO = createOrUpdate(receiptAddDTO);
        return successResponse(receiptAddDTO);
    }

    @Operation(summary = "Edit bank receipt")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Bank receipt"))
    @RequestMapping(value = "/bankReceipt", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_EDIT})
    public Object edit(@Validated @RequestBody ReceiptAddDTO receiptAddDTO) throws RestException, ParseException {

        if (receiptAddDTO.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object id is not specified", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        createOrUpdate(receiptAddDTO);
        return successResponse(receiptAddDTO);

    }

    @Operation(summary = "Delete bank receipt")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Bank receipt"))
    @RequestMapping(value = "/bankReceipt/{objectId}", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE})
    public Object delete(@PathVariable final Integer objectId) throws RestException {
        if (objectId == null || objectId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(objectId);
        if (bankTransfer == null || bankTransfer.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Bank receipt with id " + objectId + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_BANK_ACCOUNT_RECEIVE_DELETE)) {
            if (!bankTransfer.isPostDatedTransaction()) {
                try {
                    accountingServiceAsync.deleteBankTransfer(objectId, BANK_TRANSFER_TRANSACTION);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "couldn't delete post dated transaction", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get bank receipt details")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Bank receipt details"))
    @RequestMapping(value = "/bankReceipt/{objectId}", method = RequestMethod.GET)
    public Object getById(@PathVariable Integer objectId) throws RestException {
        if (objectId == null || objectId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "objectId is not specified", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(objectId);

        return successResponse(getRPC(bankTransfer));
    }

    private ReceiptAddDTO createOrUpdate(ReceiptAddDTO receiptAddDTO) throws RestException, ParseException {
        NewManualTransaction newReceipt;
        if (receiptAddDTO.getObjectId() != null) {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setObjectId(receiptAddDTO.getObjectId());
            fp.setType(0);
            try {
                newReceipt = accountingService.getBankTransferData(fp);
                newReceipt.setObjectId(receiptAddDTO.getObjectId());
            } catch (Exception e) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "transfer data with this id has not been found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {
            newReceipt = new NewManualTransaction();
        }

        if (receiptAddDTO.getBank_account() != null) {
            BankAccountItem item = new BankAccountItem();
            if (receiptAddDTO.getBank_account().getAccountCode() != null) {
                item.setBankAccountCode(receiptAddDTO.getBank_account().getAccountCode());
            }
            if (receiptAddDTO.getBank_account().getName() != null) {
                item.setAccountNumber(receiptAddDTO.getBank_account().getName());
            }
            if (receiptAddDTO.getBank_account().getId() != null) {
                item.setId(receiptAddDTO.getBank_account().getId());
            }

            newReceipt.setBankAccountItem(item);
        }


        if (receiptAddDTO.getDate() != null) {
            DateNonConvertable dateNonConvertable = new DateNonConvertable();
            dateNonConvertable.setDate(receiptAddDTO.getDate());
            newReceipt.setDate(dateNonConvertable);
        }
        newReceipt.setPostDatedTransaction(receiptAddDTO.getPostDated());

        if (receiptAddDTO.getChqNumber() != null) {
            newReceipt.setCheckNumber(receiptAddDTO.getChqNumber());
        }

        if (receiptAddDTO.getNumber() != null) {
            newReceipt.setNumber(receiptAddDTO.getNumber());
        }

        if (receiptAddDTO.getReference() != null) {
            newReceipt.setReference(receiptAddDTO.getReference());
        }

        if (receiptAddDTO.getNarration() != null) {
            newReceipt.setNarration(receiptAddDTO.getNarration());
        }

        if (receiptAddDTO.getCurrency() != null) {
            CurrencyItem currencyItem = new CurrencyItem();
            if (receiptAddDTO.getCurrency().getCurrency_id() != null) {
                currencyItem.setId(receiptAddDTO.getCurrency().getCurrency_id());
            }
            if (receiptAddDTO.getCurrency().getCurrency_name() != null) {
                currencyItem.setFullName(receiptAddDTO.getCurrency().getCurrency_name());
            }
            newReceipt.setCurrency(currencyItem);
        }
        newReceipt.setExchangeRate(receiptAddDTO.getExchangeRate() != null ? receiptAddDTO.getExchangeRate() : BigDecimal.ONE);
        newReceipt.setTaxCalculationType(receiptAddDTO.getTaxCalculationType() != null ? receiptAddDTO.getTaxCalculationType() : AccountingConstants.TAX_CALCULATION_EXCLUSIVE);

        if (receiptAddDTO.getProject() != null) {
            SelectItem project = new SelectItem();
            if (receiptAddDTO.getProject().getId() != null) {
                project.setId(receiptAddDTO.getProject().getId());
            }
            if (receiptAddDTO.getProject().getName() != null) {
                project.setName(receiptAddDTO.getProject().getName());
            }
            newReceipt.setProject(project);
        }
        BigDecimal taxTotal = BigDecimal.ZERO;

        if (receiptAddDTO.getAccounts() != null && receiptAddDTO.getAccounts().size() > 0) {
            List<NewManualTransactionItem> accounts = new ArrayList<>();
            for (BankReceiptsAccountTO item : receiptAddDTO.getAccounts()) {
                NewManualTransactionItem account = new NewManualTransactionItem();
                if (item.getDescription() != null) {
                    account.setDescription(item.getDescription());
                }
                if (item.getAmount() != null) {
                    account.setAmount(item.getAmount());
                }

                if (item.getReference() != null) {
                    account.setReference(item.getReference());
                }

                if (item.getDepartment() != null) {
                    SelectItem selectItem = new SelectItem();
                    if (item.getDepartment().getId() != null) {
                        selectItem.setId(item.getDepartment().getId());
                    }
                    if (item.getDepartment().getName() != null) {
                        selectItem.setName(item.getDepartment().getName());
                    }
                    account.setDepartment(selectItem);
                }

                if (item.getAccount() != null) {
                    AccountItem accountItem = new AccountItem();
                    if (item.getAccount().getAccount_id() != null) {
                        accountItem.setId(item.getAccount().getAccount_id());
                    }
                    if (item.getAccount().getAccount_name() != null) {
                        accountItem.setName(item.getAccount().getAccount_name());
                    }
                    account.setAccountItem(accountItem);
                }

                if (item.getTax() != null) {
                    ZapierShopifyTaxItemTO taxTo = item.getTax();
                    EdsVat edsVat = null;
                    if (taxTo.getTax_id() != null) {
                        edsVat = vatManager.get(taxTo.getTax_id());
                    } else if (StringUtils.isNotBlank(taxTo.getTax_name())) {
                        edsVat = vatManager.getVatByName(taxTo.getTax_name().trim());
                    }
                    if (edsVat != null) {
                        account.setTaxItem(edsVat.createTaxItem());

                        if (taxTo.getTax_amount() != null) {
                            account.setTaxAmount(taxTo.getTax_amount());
                        } else {
                            account.setTaxAmount(edsVat.calculateAccountTaxes(account.getAmount(), receiptAddDTO.getTaxCalculationType()).values().stream().reduce(BigDecimal.ZERO, BigDecimal::add, BigDecimal::add));
                        }
                        taxTotal = taxTotal.add(account.getTaxAmount() != null ? account.getTaxAmount() : BigDecimal.ZERO);
                    }
                }

                if (item.getName() != null) {
                    SelectItem selectItem = new SelectItem();
                    selectItem.setId(item.getName().getId());
                    selectItem.setName(item.getName().getName());
                    account.setCustomerOrSupplier(selectItem);
                }

                accounts.add(account);
            }
            NewManualTransactionItem[] accountArray = new NewManualTransactionItem[accounts.size()];
            accounts.toArray(accountArray);
            newReceipt.setItems(accountArray);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "items are required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (receiptAddDTO.getTaxCalculationType() != null) {
            newReceipt.setTaxCalculationType(receiptAddDTO.getTaxCalculationType());
        }

        if (receiptAddDTO.getSubtotal() != null) {

        }
        newReceipt.setSubtotal(receiptAddDTO.getSubtotal() != null ? receiptAddDTO.getSubtotal() : BigDecimal.ZERO);
        newReceipt.setTaxTotal(taxTotal);
        newReceipt.setTotal(receiptAddDTO.getTotal() != null ? receiptAddDTO.getTotal() : BigDecimal.ZERO);

        if (receiptAddDTO.getNotes() != null) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteTO note : receiptAddDTO.getNotes()) {
                HistoryListItem newNote = new HistoryListItem();
                newNote.setEmployee(note.getUser_name());
                newNote.setObjectID(note.getId());
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(note.getDate());
                newNote.setEventDate(date);
                newNote.setEmployeeImageUrl(note.getUser_avatar());
                newNote.setSubject(note.getNote());
                notes.add(newNote);
            }
            HistoryListItem[] notesArray = new HistoryListItem[notes.size()];
            notes.toArray(notesArray);
            newReceipt.setHistoryListItems(notesArray);
        }

        newReceipt.setTransferType(0);
        newReceipt.setFormType(0);

        if (receiptAddDTO.getFormType() != null) {
            newReceipt.setFormType(receiptAddDTO.getFormType());
        }

        if (receiptAddDTO.getTaxForeignTotal() != null) {
            newReceipt.setTaxForeignTotal(receiptAddDTO.getTaxForeignTotal());
        }

        if (receiptAddDTO.getExchangeRate() != null) {
            newReceipt.setExchangeRate(receiptAddDTO.getExchangeRate());
        }

        if (receiptAddDTO.getTaxCalculationType() != null) {
            newReceipt.setTaxCalculationType(receiptAddDTO.getTaxCalculationType());
        }


        if (receiptAddDTO.getAttachments() != null) {
            ArrayList<FileItem> attachments = new ArrayList<>();
            for (AttachmentTO attachment : receiptAddDTO.getAttachments()) {
                FileItem fileItem = new FileItem();
                fileItem.setFileName(attachment.getFile_name());
                fileItem.setGoogleDocumentLink(attachment.getLink());
                attachments.add(fileItem);
            }
            FileItem[] fileItems = new FileItem[attachments.size()];
            attachments.toArray(fileItems);
            newReceipt.setAttachments(fileItems);
        }

        try {
            receiptAddDTO.setObjectId(accountingService.spendOrReceiveMoney(newReceipt));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return receiptAddDTO;
    }

    private ReceiptAddDTO getRPC(EdsBankTransfer bankTransfer) {
        ReceiptAddDTO newReceipt = new ReceiptAddDTO();

        BankAccountTO bankAccountTO = new BankAccountTO();
        if (bankTransfer.getBankAccount().getAccountNumber() != null) {
            bankAccountTO.setAccountCode(bankTransfer.getBankAccount().getAccountNumber());
        }
        newReceipt.setBank_account(bankAccountTO);


        newReceipt.setDate(bankTransfer.getDate());
        if (bankTransfer.isPostDatedTransaction() != null) {
            newReceipt.setPostDated(bankTransfer.isPostDatedTransaction());
        }
        if (bankTransfer.getCheckNumber() != null) {
            newReceipt.setChqNumber(bankTransfer.getCheckNumber());
        }

        if (bankTransfer.getNumber() != null) {
            newReceipt.setNumber(bankTransfer.getNumber());
        }

        if (bankTransfer.getReference() != null) {
            newReceipt.setReference(bankTransfer.getReference());
        }

        if (bankTransfer.getExchangeRate() != null) {
            newReceipt.setExchangeRate(bankTransfer.getExchangeRate());
        }
        if (bankTransfer.getProject() != null) {
            ProjectTO project = new ProjectTO();
            project.setId(bankTransfer.getProject().getObjectID());
            project.setName(bankTransfer.getProject().getName());
            project.setNumber(bankTransfer.getProject().getNumber());
            newReceipt.setProject(project);
        }

        if (bankTransfer.getCurrency() != null) {
            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(bankTransfer.getCurrency().getObjectID());
            currency.setCurrency_name(bankTransfer.getCurrency().getName());
            newReceipt.setCurrency(currency);
        }

        if (bankTransfer.getItems() != null && bankTransfer.getItems().size() > 0) {
            ArrayList<BankReceiptsAccountTO> accounts = new ArrayList<>();
            for (EdsBankTransferItem item : bankTransfer.getItems()) {
                BankReceiptsAccountTO account = new BankReceiptsAccountTO();
                if (item.getAmount() != null) {
                    account.setAmount(item.getAmount());
                }
                if (item.getDescription() != null) {
                    account.setDescription(item.getDescription());
                }
                if (item.getReference() != null) {
                    account.setReference(item.getReference());
                }
                if (item.getAccount() != null) {
                    AccountTO accountTO = new AccountTO();
                    accountTO.setAccount_id(item.getAccount().getObjectID());
                    accountTO.setAccount_name(item.getAccount().getName());
                    account.setAccount(accountTO);
                }
                if (item.getDepartment() != null) {
                    DepartmentTO department = new DepartmentTO();
                    department.setId(item.getDepartment().getObjectID());
                    department.setName(item.getDepartment().getName());
                    account.setDepartment(department);
                }
                if (item.getClientOrSupplier() != null) {
                    CustomerOrSupplierTO name = new CustomerOrSupplierTO();
                    name.setId(item.getClientOrSupplier().getObjectID());
                    name.setName(item.getClientOrSupplier().getName());
                    account.setName(name);
                }
                if (item.getTax() != null) {
                    ZapierShopifyTaxItemTO tax = new ZapierShopifyTaxItemTO();
                    if (item.getTax().getObjectID() != null) {
                        tax.setTax_id(item.getTax().getObjectID());
                    }
                    if (item.getTax().getVatAmount() != null) {
                        tax.setTax_amount(item.getTax().getVatAmount());
                    }
                    if (item.getTax().getTaxRate() > 0.0) {
                        tax.setTax_rate(BigDecimal.valueOf(item.getTax().getTaxRate()));
                    }
                    if (item.getTax().getName() != null) {
                        tax.setTax_name(item.getTax().getName());
                    }
                    account.setTax(tax);
                }
                accounts.add(account);
            }
            newReceipt.setAccounts(accounts);
        }
        if (bankTransfer.getSubtotal() != null) {
            newReceipt.setSubtotal(bankTransfer.getSubtotal());
        }
        if (bankTransfer.getTaxCalculationType() != null) {
            newReceipt.setTaxCalculationType(bankTransfer.getTaxCalculationType());
        }
        if (bankTransfer.getTaxTotal() != null) {
            newReceipt.setTaxTotal(bankTransfer.getTaxTotal());
        }
        if (bankTransfer.getTaxForeignTotal() != null) {
            newReceipt.setTaxForeignTotal(bankTransfer.getTaxForeignTotal());
        }
        if (bankTransfer.getTotal() != null)
            newReceipt.setTotal(bankTransfer.getTotal());
        if (bankTransfer.getFormType() != null) {
            newReceipt.setFormType(bankTransfer.getFormType());
        }

        EdsBankTransferCustomFields customFields = bankTransfer.getCustomFields();
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonServiceLocal.getCompanyCustomFields(ViewName.BankTransferList);
        newReceipt.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));
        return newReceipt;
    }
}
