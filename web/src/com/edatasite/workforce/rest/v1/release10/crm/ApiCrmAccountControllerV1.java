package com.edatasite.workforce.rest.v1.release10.crm;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.CheckListItemTO;
import com.edatasite.workforce.rest.base.to.CrmAccountTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dilshod madrahimov on 3/24/15.
 */
@Tag(name = "Crm Account", description = "Crm Account API")
@RestController
@RequestMapping(value = "/crmAccount", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCrmAccountControllerV1 extends BaseApiControllerV1 implements ApiConstants {

    @Autowired
    private ClientServiceLocal clientServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;

    @RequestMapping(value = "/{type}/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@PathVariable(value = "type") String type, @RequestBody MListingFilterParameter mFilterParameter) {
        if (type == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (mFilterParameter == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        ListingFilterParameter filterParameter = mFilterParameter.convertToFilterParameters();
        if (CUSTOMER.equalsIgnoreCase(type)) {
            NewClientList customerList = clientServiceLocal.getNewClients(filterParameter);
            ArrayList<CrmAccountTO> result = new ArrayList<>();
            for (CrmAccountItem crmAccountItem : customerList.getList()) {
                result.add(new CrmAccountTO(crmAccountItem, true));
            }
            return successResponse(new ListResultTO<>(customerList.getTotal(), result));
        }
        if (SUPPLIER.equalsIgnoreCase(type)) {
            SupplierList supplierList = clientServiceLocal.getSuppliers(filterParameter);
            ArrayList<CrmAccountTO> result = new ArrayList<>();
            for (CrmAccountItem crmAccountItem : supplierList.getList()) {
                result.add(new CrmAccountTO(crmAccountItem, true));
            }
            return successResponse(new ListResultTO<>(supplierList.getTotal(), result));
        }
        return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "type") String type, @PathVariable(value = "id") Integer id) {
        if (id == null || type == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(new CrmAccountTO(crmServiceLocal.getAccount(id, null), false));
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "type") String type, @PathVariable(value = "id") Integer id) {
        if (id == null || type == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        try {
            ArrayList<Integer> result = clientServiceLocal.deleteClientsOrSuppliers(new ArrayList<>(id), CrmConstants.CUSTOMER.equalsIgnoreCase(type), false);
            return result.size() == 0 ? this.successResponse(SUCCESS_DELETE) : this.errorResponse("You cannot delete " + type + " if it has a balance or it is used in at least one transaction");
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "type") String type, @PathVariable(value = "id") Integer id, @RequestBody CrmAccountTO crmAccountTO) {
        if (id == null || type == null) {
            return this.errorResponse(ERROR_INVALID_QUERY_PARAM);
        }
        if (crmAccountTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        try {
            crmAccountTO.setId(id);
            Integer result = crmServiceLocal.saveAccount(crmAccountTO.wrap(crmAccountTO), type, null, false, false, false, true);
            if (result == -1) {
                return this.errorResponse(type + " name already exists");
            } else if (result == -2) {
                return this.errorResponse(type + " number already exists");
            } else {
                return successResponse(SUCCESS_UPDATE);
            }
        } catch (WebApplicationException e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object create(@PathVariable(value = "type") String type, @RequestBody CrmAccountTO crmAccountTO) {
        if (type == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (crmAccountTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        try {
            Integer result = crmServiceLocal.saveAccount(crmAccountTO.wrap(crmAccountTO), null, null, false, false, false, true);
            if (result == -1) {
                return this.errorResponse(type + " name already exists");
            } else if (result == -2) {
                return this.errorResponse(type + " number already exists");
            } else {
                return successResponse(SUCCESS_SAVE, get(type, result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/owners", method = RequestMethod.GET)
    public Object getOwners() {
        return successResponse(WrapUtils.wrapSelectItemList(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE)));
    }

    @RequestMapping(value = "/subsidiaries", method = RequestMethod.GET)
    public Object getSubsidiaries() {
        return successResponse(WrapUtils.wrapSelectItemList(clientServiceLocal.getSubsidiaries(new ListingFilterParameter())));
    }

    @RequestMapping(value = "/parentAccounts", method = RequestMethod.GET)
    public Object getParentAccounts() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCRM(true);
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getLookUpItems(filterParameter, LookUpConstants.CRM_ACCOUNT_ID, null)));
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public Object getAccounts() {
        return getParentAccounts();
    }

    @RequestMapping(value = "/clientTypes", method = RequestMethod.GET)
    public Object getClientTypes() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("CLIENT_TYPES")));
    }

    @RequestMapping(value = "/accountTypes", method = RequestMethod.GET)
    public Object getAccountTypes() {
        List<EdsReference> referenceList = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
        ArrayList<CheckListItemTO> result = new ArrayList<>();
        if (referenceList != null) {
            for (EdsReference reference : referenceList) {
                CheckListItemTO checkListItemTO = new CheckListItemTO();
                checkListItemTO.setId(reference.getObjectID());
                checkListItemTO.setName(reference.getName());
                checkListItemTO.setCode(reference.getCode());
                result.add(checkListItemTO);
            }
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/ownerships", method = RequestMethod.GET)
    public Object getOwnerships() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("_OWNERSHIP")));
    }

    @RequestMapping(value = "/ratings", method = RequestMethod.GET)
    public Object getRatings() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("_LEAD_RATING")));
    }

    @RequestMapping(value = "/industries", method = RequestMethod.GET)
    public Object getIndustries() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("_COMPANY_WORKAREA")));
    }

    @RequestMapping(value = "/annualRevenues", method = RequestMethod.GET)
    public Object getAnnualRevenues() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("CONTACT_ANNUAL_REVENUE")));
    }

    @RequestMapping(value = "/numberOfEmployees", method = RequestMethod.GET)
    public Object getNumberOfEmployees() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("CONTACT_NUMBER_OF_EMPLOYEES")));
    }

    @RequestMapping(value = "/organizationTypes", method = RequestMethod.GET)
    public Object getOrganizationTypes() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences("CONTACT_ORGANIZATION_TYPES")));
    }

    @RequestMapping(value = "/paymentMethods", method = RequestMethod.GET)
    public Object getPaymentMethods() {
        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getPaymentMethodList()));
    }

    @RequestMapping(value = "/bankAccounts", method = RequestMethod.GET)
    public Object getBankAccounts() {
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getBankAccountItems()));
    }

    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public Object getTerms() {
        return successResponse(WrapUtils.wrapSelectItemList(clientServiceLocal.getInvoiceTermsForLookUp(new ListingFilterParameter())));
    }

    @RequestMapping(value = "/taxes", method = RequestMethod.GET)
    public Object getTaxes() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLookUp(true);
        filterParameter.setInvoiceType(Constants.RECEIVABLE);
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getCompanyTaxesWithFilter(filterParameter)));
    }

    @RequestMapping(value = "/{type}/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccountsReceivablePayable(@PathVariable(value = "type") String type, @RequestBody MListingFilterParameter mListingFilterParameter) {
        if (type == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (SUPPLIER.equalsIgnoreCase(type)) {
            type = Constants.PAYABLE;
        } else if (CUSTOMER.equalsIgnoreCase(type)) {
            type = Constants.RECEIVABLE;
        } else {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        mListingFilterParameter = mListingFilterParameter != null ? mListingFilterParameter : new MListingFilterParameter();
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setAccountType(type);
        ArrayList<AccountItem> accountItems = accountingServiceLocal.getAccountsReceivablePayable(filterParameter);
        ArrayList<SelectItemTO> result = new ArrayList<>();
        for (AccountItem accountItem : accountItems) {
            result.add(new SelectItemTO(accountItem));
        }
        return successResponse(result);
    }
}
