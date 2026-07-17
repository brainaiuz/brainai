package com.edatasite.workforce.rest.v1.release10.crm;

import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountTypeEnum;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.DiscountTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.OpportunityItemTO;
import com.edatasite.workforce.rest.base.to.OpportunityTO;
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

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 11.30.2016.
 */
@Tag(name = "Opportunity", description = "Opportunity API")
@RestController
@RequestMapping(value = "/opportunity", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiOpportunityControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;

    @Autowired
    private ItemManager itemManager;


    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter filterParameter) {
        ArrayList<OpportunityTO> result = new ArrayList<>();
        ListResult<OpportunityListItem> opportunityList = crmServiceLocal.getOpportunityList(filterParameter.convertToFilterParameters());
        for (OpportunityListItem item : opportunityList.getList()) {
            result.add(new OpportunityTO(item));
        }
        return successResponse(new ListResultTO<>(opportunityList.getTotal(), result));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        OpportunityListItem opportunityItem = crmServiceLocal.getOpportunity(id);
        if (opportunityItem == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(new OpportunityTO(opportunityItem, false));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(id);
        try {
            crmServiceLocal.deleteOpportunity(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
        return successResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody OpportunityTO opportunityTO) {
        OpportunityListItem opportunityListItem = opportunityTO.wrap(opportunityTO);
        if (opportunityTO.getId() == null) {
            opportunityListItem.setNumberData(crmServiceLocal.generateOpportunityNumber());
        }
        Integer id;
        try {
            id = crmServiceLocal.saveOpportunity(opportunityListItem);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
        return successResponse(SUCCESS_SAVE, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id,
                         @RequestBody OpportunityTO opportunityTO) {
        opportunityTO.setId(id);
        return add(opportunityTO);
    }

    @RequestMapping(value = "/backupAssignees", method = RequestMethod.GET)
    public Object getBackupAssignees() {
        return successResponse(WrapUtils.wrapSelectItemList(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE)));
    }

    @RequestMapping(value = "/assignees", method = RequestMethod.GET)
    public Object getAssignees() {
        return getBackupAssignees();
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public Object getTypes() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_TYPE)));
    }

    @RequestMapping(value = "/stages", method = RequestMethod.GET)
    public Object getStages() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences(EdsOpportunity._OPPORTUNITY_STAGE)));
    }

    @RequestMapping(value = "/campaignSources", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCampaignSources(@RequestBody MListingFilterParameter mFilterParameter) {
        ListingFilterParameter filterParameter = mFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemObjectList(campaignManager.getCampaignList(filterParameter)));
    }

    @RequestMapping(value = "/leadSources", method = RequestMethod.GET)
    public Object getLeadSources() {
        return successResponse(WrapUtils.wrapSelectItemList(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE)));
    }

    @RequestMapping(value = "/items", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getItems(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setLookUp(true);
        filterParameter.setInvoiceType(Constants.RECEIVED);
//        filterParameter.setShowOnOpportunity(true);

        return successResponse(WrapUtils.wrapSelectItemList(productServiceLocal.getCompanyProductsByType(filterParameter)));
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getItem(@PathVariable(value = "id") Integer id) {
        NewProduct item = productServiceLocal.getProductBaseData(id);
        OpportunityItemTO opportunityItemTO = new OpportunityItemTO();
        opportunityItemTO.setItem(new SelectItemTO(item.getObjectId(), item.getItemName()));
        opportunityItemTO.setDescription(item.getDescription());
        opportunityItemTO.setQuantity(item.getQuantity());
        opportunityItemTO.setUnitMeasurement(item.getUnitMeasurement() != null ? new SelectItemTO(item.getUnitMeasurement().getId(), item.getUnitMeasurement().getName()) : null);
        opportunityItemTO.setPrice(item.getSellingPrice());
        if (item.getItemDiscountID() != null) {
            opportunityItemTO.setDiscount(new DiscountTO(item.getItemDiscountID()));
        }
        if (item.getSuppliers() != null && item.getSuppliers().length > 0) {
            opportunityItemTO.setSupplier(new SelectItemTO(item.getSuppliers()[0].getId(), item.getSuppliers()[0].getName()));
        }

        return successResponse(opportunityItemTO);
    }

    @RequestMapping(value = "/unitMeasurements", method = RequestMethod.GET)
    public Object getUnitMeasurements() {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getUnitMeasurements(filterParameter)));
    }

    @RequestMapping(value = "/discounts/{itemId}", method = RequestMethod.GET)
    public Object getDiscounts(@PathVariable(value = "itemId") Integer itemId) {
        ArrayList<SelectItemTO> discounts = new ArrayList<>();
        DiscountTO percentage = new DiscountTO(DiscountTypeEnum.PERCENTAGE.getId(), DiscountTypeEnum.PERCENTAGE.getName(), DiscountTypeEnum.PERCENTAGE.getCode());
        DiscountTO fixedAmount = new DiscountTO(DiscountTypeEnum.FIXED_AMOUNT.getId(), DiscountTypeEnum.FIXED_AMOUNT.getName(), DiscountTypeEnum.FIXED_AMOUNT.getCode());

        discounts.add(percentage);
        discounts.add(fixedAmount);

        if (itemId != null) {
            EdsItem edsItem = itemManager.get(itemId);
            if (edsItem != null && edsItem.getDiscounts() != null) {
                for (EdsDiscount discount : edsItem.getDiscounts()) {
                    DiscountTO discountTO = new DiscountTO();
                    discountTO.setId(discount.getObjectID());
                    discountTO.setName(discount.getName());
                    discountTO.setFixedAmount(discount.getFixedAmount());
                    discountTO.setPercentage(discount.getPercentage());
                    if (discount.getPercentage() != null) {
                        discountTO.setType(DiscountTypeEnum.PERCENTAGE);
                    } else {
                        discountTO.setType(DiscountTypeEnum.FIXED_AMOUNT);
                    }
                    discounts.add(discountTO);
                }
            }
        }
        return successResponse(discounts);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccounts(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setSearchByParent(true);
        filterParameter.setAccountType(CrmConstants.CUSTOMER);
        return successResponse(WrapUtils.wrapSelectItemList(crmServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_ACCOUNT_ID).getList()));
    }

    @RequestMapping(value = "/contacts/{accountId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getContacts(@PathVariable(value = "accountId") Integer accountId,
                              @RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setSearchByParent(true);
        filterParameter.setAvoidType(Constants.SUPPLIER);
        filterParameter.setCRM(true);
        filterParameter.setAccountID(accountId);
        return successResponse(WrapUtils.wrapSelectItemList(crmServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_CONTACT_ID).getList()));
    }


    @RequestMapping(value = "/suppliers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSuppliers(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setSearchByParent(true);
        filterParameter.setAccountType(CrmConstants.SUPPLIER);
        return successResponse(WrapUtils.wrapSelectItemList(crmServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_ACCOUNT_ID).getList()));
    }
}
