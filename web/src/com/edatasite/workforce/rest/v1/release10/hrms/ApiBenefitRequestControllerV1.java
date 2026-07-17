package com.edatasite.workforce.rest.v1.release10.hrms;

import com.edatasite.workforce.core.domain.EdsBenefit;
import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.benefit.BenefitManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.BenefitRequestTO;
import com.edatasite.workforce.rest.base.to.BenefitTypeTO;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov.
 */
@Tag(name = "Benefit Request", description = "Benefit Request API")
@RestController
@RequestMapping(value = "/benefitRequest", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiBenefitRequestControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private BenefitManager benefitManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter filterParameter) {
        ListResult<BenefitRequestItem> benefitRequestList = availabilityServiceLocal.getBenefitRequestList(filterParameter.convertToFilterParameters());
        ArrayList<BenefitRequestTO> benefitRequestTOs = new ArrayList<>();
        for (BenefitRequestItem item : benefitRequestList.getList()) {
            benefitRequestTOs.add(new BenefitRequestTO(item));
        }

        return successResponse(new ListResultTO<>(benefitRequestList.getTotal(), benefitRequestTOs));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        BenefitRequestItem item = availabilityServiceLocal.getBenefitRequests(id);
        EmployeeLeaveStatusListItem userLeftRequest = availabilityServiceLocal.getTotalAndLeftRequest(item.getRequesterID(), item.getBenefitID(), item.getDate());
        BenefitRequestTO result = new BenefitRequestTO(item, true);
        result.setTotalUsedRequest(userLeftRequest.getTotalUsedRequest());
        result.setTotalLeftRequest(userLeftRequest.getTotalLeftRequest());
        result.setQuantityType(userLeftRequest.getQtyType());

        return successResponse(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody BenefitRequestTO benefitRequestTO) {
        BenefitRequestItem item = benefitRequestTO.wrap(benefitRequestTO);
        SelectItem status = new SelectItem();
        status.setCode(Constants.BR_WAITING_FOR_APPROVAL);
        item.setStatus(status);
        try {
            Integer result = availabilityServiceLocal.saveBenefitRequest(item);
            return successResponse(SUCCESS_SAVE, result);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id,
                         @RequestBody BenefitRequestTO benefitRequestTO) {
        benefitRequestTO.setId(id);
        BenefitRequestItem item = benefitRequestTO.wrap(benefitRequestTO);
        try {
            Integer result = availabilityServiceLocal.saveBenefitRequest(item);
            return successResponse(SUCCESS_UPDATE, result);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        availabilityServiceLocal.deleteBenefitRequest(id);
        return successResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public Object getStatuses() {
        return successResponse(getStatusList());
        //return WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(EdsBenefitRequest._BENEFIT_REQUEST_STATUSES));
    }

    @RequestMapping(value = "/benefitTypes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getBenefitTypes(@RequestBody MListingFilterParameter filterParameter) {
        List<EdsBenefit> benefits = benefitManager.getBenefitList(filterParameter.convertToFilterParameters());
        ArrayList<BenefitTypeTO> result = new ArrayList<>();
        for (EdsBenefit edsBenefit : benefits) {
            BenefitTypeTO benefitType = new BenefitTypeTO();
            benefitType.setId(edsBenefit.getObjectID());
            benefitType.setName(edsBenefit.getName());
            result.add(benefitType);
        }

        return successResponse(result);
    }

    @RequestMapping(value = "/requesters", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getRequesters(@RequestBody MListingFilterParameter filterParameter) {
        return successResponse(WrapUtils.wrapSelectItemTOs(allInOneServiceLocal.getEmployeesAsSelectItem(filterParameter.convertToFilterParameters())));
    }

    @RequestMapping(value = "/approvers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getApprovers(@RequestBody MListingFilterParameter filterParameter) {
        ListingFilterParameter fp = filterParameter.convertToFilterParameters();
        fp.setParams(PermissionConstants.BENEFIT_REQUEST_APPROVER);
        return successResponse(WrapUtils.wrapSelectItemTOs(allInOneServiceLocal.getEmployeesAsSelectItem(fp)));
    }

    @RequestMapping(value = "/takenRequest", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getTotalAndLeftRequest(@RequestBody MListingFilterParameter fp) {
        if (fp.getEmployeeId() == null || fp.getEntityId() == null || fp.getDate() == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        EmployeeLeaveStatusListItem userLeftRequest = availabilityServiceLocal.getTotalAndLeftRequest(fp.getEmployeeId(), fp.getEntityId(), new DateNonConvertable(WrapUtils.longToDate(fp.getDate())));
        return successResponse(new TotalUsedLeftRequest(userLeftRequest.getTotalUsedRequest(), userLeftRequest.getTotalLeftRequest(), userLeftRequest.getQtyType()));
    }

    @RequestMapping(value = "/changeStatus/{id}/{status}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object changeStatus(@PathVariable(value = "id") Integer id,
                               @PathVariable(value = "status") String status,
                               @RequestBody(required = false) String comment) {
        SelectItemTO statusItem = getStatusList().stream()
                .filter(statusName -> statusName.getCode().equalsIgnoreCase(status.trim()))
                .findFirst().orElse(null);

        if (statusItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        try {
            EdsBenefitRequest benefitRequest = benefitRequestManager.get(id);
            availabilityServiceLocal.changeBenefitRequestStatus(id, statusItem.getCode(), comment, benefitRequest.getRequestedQuantity());
            return successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    @RequestMapping(value = "/quantityTypes", method = RequestMethod.GET)
    public Object getQuantityTypes() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(EdsBenefit.BENEFIT_QTY_TYPE)));
    }

    private ArrayList<SelectItemTO> getStatusList() {
        ArrayList<SelectItemTO> statusList = new ArrayList<>();
        statusList.add(new SelectItemTO(0, "Waiting For Approval", Constants.BR_WAITING_FOR_APPROVAL, "'"));
        statusList.add(new SelectItemTO(1, "Approved", Constants.BR_APPROVED, ""));
        statusList.add(new SelectItemTO(2, "Rejected", Constants.BR_REJECTED, ""));
        return statusList;
    }

    private class TotalUsedLeftRequest {
        private String totalUsedRequest;
        private String totalLeftRequest;
        private String quantityType;

        private TotalUsedLeftRequest() {

        }

        private TotalUsedLeftRequest(String totalUsedRequest, String totalLeftRequest, String quantityType) {
            this.totalUsedRequest = totalUsedRequest;
            this.totalLeftRequest = totalLeftRequest;
            this.quantityType = quantityType;
        }

        public String getTotalUsedRequest() {
            return totalUsedRequest;
        }

        public void setTotalUsedRequest(String totalUsedRequest) {
            this.totalUsedRequest = totalUsedRequest;
        }

        public String getTotalLeftRequest() {
            return totalLeftRequest;
        }

        public void setTotalLeftRequest(String totalLeftRequest) {
            this.totalLeftRequest = totalLeftRequest;
        }

        public String getQuantityType() {
            return quantityType;
        }

        public void setQuantityType(String quantityType) {
            this.quantityType = quantityType;
        }
    }

}

