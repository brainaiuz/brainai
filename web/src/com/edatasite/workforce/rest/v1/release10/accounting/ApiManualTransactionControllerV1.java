package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.gwt.accounting.client.rpc.ManualJournalListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.ManualEntryTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 6/24/15 12:07 PM
 */

@Tag(name = "Manual Transaction", description = "Manual Transaction API")
@RestController
@RequestMapping(value = "/manualTransaction", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiManualTransactionControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private ManualEntryServiceLocal manualEntryServiceLocal;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private ManualJournalManager manualJournalManager;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        ArrayList<ManualEntryTO> result = new ArrayList<>();
        ListResult<ManualJournalListItem> manualEntryResultList = manualEntryServiceLocal.getManualTransactions(filterParameter);
        for (ManualJournalListItem item : manualEntryResultList.getList()) {
            result.add(new ManualEntryTO(item));
        }
        return successResponse(new ListResultTO<>(manualEntryResultList.getTotal(), result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(new ManualEntryTO(manualEntryServiceLocal.getManualJournal(id)));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody ManualEntryTO manualEntryTO) {
        if (manualEntryTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        NewManualTransaction manualTransaction = manualEntryTO.wrap(manualEntryTO);
        manualTransaction.setStatus(Constants.DRAFT);

        BankTransferNumberData numberData = manualEntryServiceLocal.generateManualTransactionMoneyNumber();
        manualTransaction.setIntNumber(Integer.valueOf(numberData.getFourDigitNumber()));
        StringBuilder sb = new StringBuilder();
        sb.append(numberData.getPrefix());
        sb.append(numberData.getFourDigitNumber());
        if (numberData.isWithDate()) {
            sb.append("-");
            sb.append(ServerUtils.getBankTransferDateNumber(new Date()));
        }
        manualTransaction.setNumber(sb.toString());
        manualTransaction.setTransferNumberData(numberData);

        Integer manualJournalId = manualEntryServiceLocal.saveManualJournal(manualTransaction);
        return manualJournalId > 0 ? this.successResponse(SUCCESS_SAVE, manualJournalId) : this.errorResponse("Manual Entry with this number already exists.");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @RequestBody ManualEntryTO manualEntryTO) {
        if (id == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (manualEntryTO == null) {
            this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        manualEntryTO.setId(id);
        NewManualTransaction manualTransaction = manualEntryTO.wrap(manualEntryTO);
        manualTransaction.setStatus(Constants.DRAFT);
        return manualEntryServiceLocal.saveManualJournal(manualTransaction) > 0 ? this.successResponse(SUCCESS_UPDATE) : this.errorResponse("Manual Entry with this number already exists.");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return manualEntryServiceLocal.deleteManualJournal(id) ? this.successResponse(SUCCESS_DELETE) : this.errorResponse(ERROR_FAIL_DELETE);
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object post(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return manualEntryServiceLocal.updateManualTransaction(id, null) ? this.successResponse(SUCCESS_UPDATE) : this.errorResponse(ERROR_FAILED_UPDATE);
    }

    @RequestMapping(value = "/void/{voidDate}/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object voidManualEntry(@PathVariable(value = "voidDate") Long voidDate, @PathVariable(value = "id") Integer id) {
        if (voidDate == null || id == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        EdsManualJournal edsManualJournal = manualJournalManager.get(id);
        if (edsManualJournal == null) {
            this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        Integer validationResult = validateVoidDate(WrapUtils.longToDate(voidDate), edsManualJournal.getDate());
        if (validationResult == -1) {
            return this.errorResponse("Void date should be greater than transaction date");
        }
        if (validationResult == -2) {
            return this.errorResponse("Void date should not be greater than today");
        }
        return manualEntryServiceLocal.voidManualJournal(id, new DateNonConvertable(WrapUtils.longToDate(voidDate))) ? this.successResponse(SUCCESS_UPDATE) : this.errorResponse(ERROR_FAILED_UPDATE);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccountList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setSystem(false);
        filterParameter.setValidateChildAccounts(false);
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getAccountsForInvoice(filterParameter, null)));
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getProjectList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setSystem(false);
        filterParameter.setValidateChildAccounts(false);
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(invoiceServiceLocal.getRelatedProjectsWithFilter(filterParameter)));
    }

    @RequestMapping(value = "/departments", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getDepartmentList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getDepartmentsForLookUp(filterParameter)));
    }

    @RequestMapping(value = "/suppliers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSupplierList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setSearchByParent(true);
        filterParameter.setAccountType(CrmConstants.SUPPLIER);
        filterParameter.setWithCode(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, filterParameter).getList()));
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCustomerList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setSearchByParent(true);
        filterParameter.setAccountType(CrmConstants.CUSTOMER);
        filterParameter.setWithCode(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, filterParameter).getList()));
    }

    private Integer validateVoidDate(Date voidDate, Date manualEntryDate) {
        Date today = new Date();
        if (!(voidDate.compareTo(manualEntryDate) >= 0)) {
            //Info.show("", "Void date should be greater than transaction date", Info.Type.WARNING);
            return -1;
        }
        if (!(today.compareTo(voidDate) >= 0)) {
            //Info.show("", "Void date should not be greater than today", Info.Type.WARNING);
            return -2;
        }
        return 0;
    }

}
