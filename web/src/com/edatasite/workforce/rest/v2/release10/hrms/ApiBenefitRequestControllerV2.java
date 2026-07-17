package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.EdsBenefit;
import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.benefit.BenefitManager;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.TitleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.RequestActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitApproverTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitRequestCalcItemListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitRequestCalcItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitRequestDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitRequestListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitTypeAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest.BenefitTypeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.enums.RequestActionEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Benefit Request", description = "Benefit Request API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiBenefitRequestControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiBenefitRequestControllerV2.class);

    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private BenefitManager benefitManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;

    public static Object getStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return null;
        }
        return switch (status.getCode()) {
            case Constants.BR_APPROVED -> new StatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.BR_REJECTED -> new StatusTO(RequestStatusEnum.DECLINED.getStatus());
            case Constants.BR_WAITING_FOR_APPROVAL -> new StatusTO(RequestStatusEnum.PENDING.getStatus());
            default -> new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(), new TitleTO(status.getName()));
        };
    }

    @Operation(summary = "Get Benefit Request List", description = "Retrieves list of benefit requests based on provided year")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of benefit requests."),
            @ApiResponse(responseCode = "400", description = "year field is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/benefits", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU, PermissionConstants.MY_BENEFIT_REQUEST_LIST})
    public Object getBenefitRequestList(@RequestParam(value = "year") Integer year) throws RestException {
        if (year == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "year field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStartDate(ServerUtils.getYearStartDate(year));
        filterParameter.setEndDate(ServerUtils.getYearEndDate(year));
        filterParameter.setEmployeeId(userManager.getUser().getObjectID());

        ListResult<BenefitRequestItem> benefitRequestResultList;
        try {
            benefitRequestResultList = availabilityServiceLocal.getBenefitRequestList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<BenefitRequestTO> benefitRequestList = new ArrayList<>();
        for (BenefitRequestItem item : benefitRequestResultList.getList()) {
            try {
                BenefitRequestTO benefitRequest = new BenefitRequestTO();
                benefitRequest.setId(item.getObjectID());
                benefitRequest.setTitle(item.getBenefitName());
                benefitRequest.setEmployeeImgURL(hrmsServiceLocal.getUserImageUrl(userManager.get(item.getRequesterID())));
                benefitRequest.setQuantity(item.getQuantityType());
                if (StringUtils.isNotBlank(item.getDescription())) {
                    benefitRequest.setDescription(item.getDescription());
                }
                benefitRequest.setApprover(item.getApprover());
                benefitRequest.setRequester(item.getRequester());
                benefitRequest.setStatus(getStatus(item.getStatus()));
                benefitRequestList.add(benefitRequest);
            } catch (NumberFormatException e) {
                log.error("", e);
            }
        }

        return successResponse(new BenefitRequestListResultTO(benefitRequestList));
    }

    @Operation(summary = "Get Benefit Types", description = "Retrieves list of benefit types of the current company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of benefit types.")})
    @RequestMapping(value = "/requests/benefit_types", method = RequestMethod.GET)
    public Object getBenefitTypes() throws RestException {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setActive(true);
        filterParameter.setStartDateWithoutOffset(new Date());//TODO it should be benefit date, but we don't have date param, so I'm using current date. Date field need to validate benefit expire date
        List<EdsBenefit> benefitTypes;                        //TODO We have to add date param to validate expired benefit types
        try {
            benefitTypes = benefitManager.getBenefitList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CategoryTO> benefitTypeList = new ArrayList<>();
        for (EdsBenefit benefit : benefitTypes) {
            benefitTypeList.add(new CategoryTO(benefit.getObjectID(), benefit.getName()));
        }
        BenefitTypeListTO result = new BenefitTypeListTO();
        result.setBenefit_types(benefitTypeList);
        return successResponse(result);
    }

    @Operation(summary = "Add Benefit Request", description = "Adds new benefit request")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have if successfully added or false if not added successfully with error code"),
            @ApiResponse(responseCode = "400", description = "Benefit approver, type, requested_amount, date are required"),
            @ApiResponse(responseCode = "404", description = "User or benefit type is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/benefit_request", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU, PermissionConstants.MY_BENEFIT_REQUEST_LIST, PermissionConstants.ADD_BENEFIT_REQUEST})
    public Object createBenefitRequest(@RequestBody BenefitTypeAddTO benefitTypeAdd) throws RestException {

        EdsEmployee currentUser = employeeManager.get(employeeManager.getUser().getObjectID());

        if (currentUser.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(currentUser.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String currentUserStatus = userManager.getUserStatus(currentUser.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(currentUserStatus)) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        EdsEmployee employee;
        if (benefitTypeAdd.getUser_id() == null || benefitTypeAdd.getUser_id() <= 0) {
            try {
                employee = currentUser;
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            employee = employeeManager.get(benefitTypeAdd.getUser_id());
        }
        if (employee == null) {
            throw new RestException(ERROR_MESSAGE, "User is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (benefitTypeAdd.getApprovers() == null || benefitTypeAdd.getApprovers().isEmpty()) {
            throw new RestException(ERROR_MESSAGE, "Benefit approver are required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (benefitTypeAdd.getBenefit_type() == null || benefitTypeAdd.getBenefit_type() <= 0) {
            throw new RestException(ERROR_MESSAGE, "Benefit type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsBenefit edsBenefit = benefitManager.get(benefitTypeAdd.getBenefit_type());
        if (edsBenefit == null || edsBenefit.getDeleted()) {
            throw new RestException(ERROR_MESSAGE, "Benefit with id " + benefitTypeAdd.getBenefit_type() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(benefitTypeAdd.getDate())) {
            throw new RestException(ERROR_MESSAGE, "Date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (benefitTypeAdd.getRequested_amount() == null) {
            throw new RestException(ERROR_MESSAGE, "Requested amount is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (benefitTypeAdd.getRequested_amount().doubleValue() < 0) {
            throw new RestException(ERROR_MESSAGE, "Requested amount cannot be less than zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (benefitTypeAdd.getRequested_amount().doubleValue() == 0) {
            throw new RestException("Requested amount should be more than zero", "Requested amount should be more than zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }


        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        Date benefitRequestDate;
        try {
            benefitRequestDate = longDateTimezoneFormat.parse(benefitTypeAdd.getDate());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid benefit request date format", "Invalid benefit request date format. Acceptable date format for benefit request is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        //Validate request amount
        EmployeeLeaveStatusListItem leaveStatusListItem = availabilityServiceLocal.getTotalAndLeftRequest(employee.getObjectID(), edsBenefit.getObjectID(), new DateNonConvertable(benefitRequestDate));
        Double totalLeft = Double.valueOf(leaveStatusListItem.getTotalLeftRequest());
        BigDecimal totalLeftAllowances = BigDecimal.valueOf(totalLeft);
        String qtyType = leaveStatusListItem.getQtyType();
        int year = ServerUtils.getYear(benefitRequestDate);
        if (benefitTypeAdd.getRequested_amount().doubleValue() > totalLeftAllowances.doubleValue()) {
            BigDecimal exceededDays = benefitTypeAdd.getRequested_amount().subtract(totalLeftAllowances);
            String infoMessage = "You cannot add this benefit request as it will exceed the " + edsBenefit.getName() + " benefit allowance limit by " + exceededDays + " " + qtyType + " for the year of " + year;
            throw new RestException(infoMessage, infoMessage, CONFLICT, HttpStatus.CONFLICT);
        }

        BenefitRequestItem benefitRequest = new BenefitRequestItem();
        benefitRequest.setRequesterID(employee.getObjectID());
        benefitRequest.setApproverID(benefitTypeAdd.getApprovers().get(0));
        benefitRequest.setDescription(benefitTypeAdd.getDescription());
        benefitRequest.setDate(new DateNonConvertable(benefitRequestDate));
        benefitRequest.setBenefitID(edsBenefit.getObjectID());
        benefitRequest.setRequestedQuantity(benefitTypeAdd.getRequested_amount().doubleValue());
        SelectItem status = new SelectItem();
        status.setCode(Constants.BR_WAITING_FOR_APPROVAL);
        benefitRequest.setStatus(status);

        try {
            availabilityServiceLocal.saveBenefitRequest(benefitRequest);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Benefit Request Details", description = "Retrieves details of benefit request based on provided request_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have details of benefit request."),
            @ApiResponse(responseCode = "400", description = "Benefit request_id is required"),
            @ApiResponse(responseCode = "404", description = "Benefit request with provided request_id is not found")})
    @RequestMapping(value = "/requests/benefit_request", method = RequestMethod.GET)
    public Object getBenefitDetails(@RequestParam(value = "request_id") Integer request_id) throws RestException {
        if (request_id == null || request_id <= 0) {
            throw new RestException(ERROR_MESSAGE, "Benefit request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsBenefitRequest edsBenefitRequest = benefitRequestManager.get(request_id);
        if (edsBenefitRequest == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Benefit request with id " + request_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        BenefitRequestItem benefitRequest = availabilityServiceLocal.getBenefitRequests(request_id);
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        BenefitRequestDetailsTO requestDetails = new BenefitRequestDetailsTO();
        requestDetails.setId(benefitRequest.getObjectID());
        if (benefitRequest.getRequesterID() != null) {
            EdsEmployee requester = employeeManager.get(benefitRequest.getRequesterID());
            if (requester != null) {
                EmployeeTO owner = new EmployeeTO();
                owner.setId(requester.getObjectID());
                owner.setName(requester.getName());
                owner.setDepartment(requester.getTeam() != null ? requester.getTeam().getName() : null);
                if (requester.getPhoto() != null) {
                    owner.setAvatar(commonServiceLocal.getFileUrl(requester.getPhoto().getObjectID()));
                }
                requestDetails.setOwner(owner);
            }
        }
        requestDetails.setTitle(benefitRequest.getBenefitName());
        if (benefitRequest.getDescription() != null && !"".equals(benefitRequest.getDescription())) {
            requestDetails.setDescription(benefitRequest.getDescription());
        }
        requestDetails.setAmount(benefitRequest.getRequestedQuantity());
        requestDetails.setDate(longDateTimezoneFormat.format(benefitRequest.getDate().getNonConvertedDate()));
        requestDetails.setStatus(getStatus(benefitRequest.getStatus()));
        requestDetails.setCreated_at(longDateTimezoneFormat.format(edsBenefitRequest.getCreatedDate()));
        if (benefitRequest.getApproverID() != null) {
            EdsEmployee approverEmployee = employeeManager.get(benefitRequest.getApproverID());
            if (approverEmployee != null) {
                ArrayList<BenefitApproverTO> approvers = new ArrayList<>();
                BenefitApproverTO approver = new BenefitApproverTO();
                approver.setId(approverEmployee.getObjectID());
                approver.setName(approverEmployee.getName());
                approver.setDepartment(approverEmployee.getTeam() != null ? approverEmployee.getTeam().getName() : null);
                if (approverEmployee.getPhoto() != null) {
                    approver.setAvatar(commonServiceLocal.getFileUrl(approverEmployee.getPhoto().getObjectID()));
                }
                if (benefitRequest.getStatus() != null && StringUtils.isNotBlank(benefitRequest.getStatus().getCode())) {
                    if (Constants.BR_APPROVED.equals(benefitRequest.getStatus().getCode())) {
                        approver.setStatus(new StatusTO(RequestStatusEnum.APPROVED.getStatus()));
                    } else if (Constants.BR_REJECTED.equals(benefitRequest.getStatus().getCode())) {
                        approver.setStatus(new StatusTO(RequestStatusEnum.DECLINED.getStatus()));
                    } else if (Constants.BR_WAITING_FOR_APPROVAL.equals(benefitRequest.getStatus().getCode())) {
                        approver.setStatus(new StatusTO(RequestStatusEnum.PENDING.getStatus()));
                    }
                }

                approvers.add(approver);
                requestDetails.setApprovers(approvers);
            }
        }
        RequestUserActionTO requestUserAction = new RequestUserActionTO();

        boolean isBenefitOwnerOrManager = benefitRequest.getUserID().equals(benefitRequest.getApproverID()) || ServerUtils.hasPermission(PermissionConstants.APPROVE_REJECT_ALL_BENEFIT_REQUESTS);
        if (isBenefitOwnerOrManager) {
            if (Constants.BR_WAITING_FOR_APPROVAL.equals(benefitRequest.getStatus().getCode())) {
                requestUserAction.setApprove(true);
                requestUserAction.setReject(true);
                requestUserAction.setApprove_for_all(true);
            }
        }
        requestDetails.setUser_actions(requestUserAction);

        return successResponse(requestDetails);
    }

    @Operation(summary = "Benefit Request Action", description = "Approves or Rejects benefit request \n\n Request action should be APPROVE, REJECT or APPROVE_FOR_ALL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "Benefit request_id is required"),
            @ApiResponse(responseCode = "404", description = "Benefit request with provided request_id is not found"),
            @ApiResponse(responseCode = "409", description = "Benefit request has already been approved or rejected"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/benefit_request", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object benefitRequestAction(@RequestBody RequestActionTO benefitAction) throws RestException {

        if (benefitAction.getRequest_id() == null || benefitAction.getRequest_id() <= 0) {
            throw new RestException(ERROR_MESSAGE, "Benefit request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsBenefitRequest edsBenefitRequest = benefitRequestManager.get(benefitAction.getRequest_id());

        if (edsBenefitRequest == null) {
            throw new RestException(ERROR_MESSAGE, "Benefit request with id " + benefitAction.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if ((RequestActionEnum.APPROVE.name().equals(benefitAction.getAction()) || RequestActionEnum.APPROVE_FOR_ALL.name().equals(benefitAction.getAction())) && Constants.BR_APPROVED.equals(edsBenefitRequest.getStatus().getCode())) {
            throw new RestException(ERROR_MESSAGE, "Benefit request has already approved", CONFLICT, HttpStatus.CONFLICT);
        }
        if ((RequestActionEnum.APPROVE.name().equals(benefitAction.getAction()) || RequestActionEnum.APPROVE_FOR_ALL.name().equals(benefitAction.getAction())) && Constants.BR_REJECTED.equals(edsBenefitRequest.getStatus().getCode())) {
            throw new RestException(ERROR_MESSAGE, "Rejected benefit request cannot be approved", CONFLICT, HttpStatus.CONFLICT);
        }
        if ((RequestActionEnum.REJECT.name().equals(benefitAction.getAction()) && Constants.BR_APPROVED.equals(edsBenefitRequest.getStatus().getCode()))) {
            throw new RestException(ERROR_MESSAGE, "Approved benefit request cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
        }
        if (RequestActionEnum.REJECT.name().equals(benefitAction.getAction()) && Constants.BR_REJECTED.equals(edsBenefitRequest.getStatus().getCode())) {
            throw new RestException(ERROR_MESSAGE, "Benefit request has already rejected", CONFLICT, HttpStatus.CONFLICT);
        }
        EdsEmployee employee;
        try {
            employee = (EdsEmployee) userManager.getUser();
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (employee == null) {
            throw new RestException(ERROR_MESSAGE, "User is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        boolean isBenefitOwnerOrManager = employee.getObjectID().equals(edsBenefitRequest.getApprover().getObjectID()) || ServerUtils.hasPermission(PermissionConstants.APPROVE_REJECT_ALL_BENEFIT_REQUESTS);

        String status;

        if ((RequestActionEnum.APPROVE.name().equals(benefitAction.getAction()) || RequestActionEnum.APPROVE_FOR_ALL.name().equals(benefitAction.getAction())) && isBenefitOwnerOrManager) {
            status = Constants.BR_APPROVED;
        } else if (RequestActionEnum.REJECT.name().equals(benefitAction.getAction()) && isBenefitOwnerOrManager) {
            status = Constants.BR_REJECTED;
        } else {
            throw new RestException(ERROR_MESSAGE, "Benefit request action should be one of APPROVE,REJECT,APPROVE_FOR_ALL", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        Integer result = availabilityServiceLocal.changeBenefitRequestStatus(benefitAction.getRequest_id(), status, null, edsBenefitRequest.getRequestedQuantity());
        if (result > 0) {
            return successResponse(new ResponseData());
        } else {
            EmployeeLeaveStatusListItem leaveStatusListItem = availabilityServiceLocal.getTotalAndLeftRequest(employee.getObjectID(), edsBenefitRequest.getBenefit().getObjectID(), new DateNonConvertable(edsBenefitRequest.getDate()));
            Double totalLeft = Double.valueOf(leaveStatusListItem.getTotalLeftRequest());
            String qtyType = leaveStatusListItem.getQtyType();
            int year = ServerUtils.getYear(edsBenefitRequest.getDate());
            Double exceededDays = edsBenefitRequest.getRequestedQuantity() - totalLeft;
            String infoMessage = "You cannot add this benefit request as it will exceed the " + edsBenefitRequest.getBenefit().getName() + " benefit allowance limit by " + exceededDays + " " + qtyType + " for the year of " + year;
            throw new RestException(infoMessage, infoMessage, CONFLICT, HttpStatus.CONFLICT);
        }

    }

    @Operation(summary = "Get Benefit Calc", description = "Calculates total benefit, used and left allowances based on provided user and request type \n\n Date format should be dd-MM-yyyy'T'hh:mm:ssZ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have calculation of benefit requests"),
            @ApiResponse(responseCode = "400", description = "Benefit type_id is required"),
            @ApiResponse(responseCode = "404", description = "User or benefit request is not found"),
            @ApiResponse(responseCode = "422", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/benefit_calc", method = RequestMethod.GET)
    public Object getBenefitCalc(@RequestParam(value = "user_id", required = false) Integer user_id,
                                 @RequestParam(value = "type_id") Integer type_id,
                                 @RequestParam(value = "date", required = false) String date) throws RestException {
        EdsEmployee employee;
        if (user_id == null || user_id <= 0) {
            try {
                employee = (EdsEmployee) userManager.getUser();
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            employee = employeeManager.get(user_id);
        }
        if (employee == null) {
            throw new RestException(ERROR_MESSAGE, "User is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Date benefitRequestDate = new Date();
        if (StringUtils.isNotBlank(date)) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            try {
                benefitRequestDate = longDateTimezoneFormat.parse(date);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException("Invalid date format", "Invalid date format. Acceptable format for benefit request is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        if (type_id == null) {
            throw new RestException(ERROR_MESSAGE, "Benefit type_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsBenefit edsBenefit = benefitManager.get(type_id);
        if (edsBenefit == null) {
            throw new RestException(ERROR_MESSAGE, "Benefit with id " + type_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (edsBenefit.getExpireDate() != null && ServerUtils.getEndDate(edsBenefit.getExpireDate()).before(ServerUtils.getEndDate(benefitRequestDate))) {
            throw new RestException("Benefit type " + edsBenefit.getName() + " is already expired", "Benefit type " + edsBenefit.getName() + " is already expired", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EmployeeLeaveStatusListItem benefitRequestCalcItem = availabilityServiceLocal.getTotalAndLeftRequest(employee.getObjectID(), edsBenefit.getObjectID(), new DateNonConvertable(benefitRequestDate));
        ArrayList<BenefitRequestCalcItemTO> benefitRequestCalcItemList = new ArrayList<>();

        BenefitRequestCalcItemTO allowance = new BenefitRequestCalcItemTO();
        allowance.setTitle("Allowance");
        allowance.setAmount(benefitRequestCalcItem.getTotalLeaveRequest() + " " + benefitRequestCalcItem.getQtyType());
        benefitRequestCalcItemList.add(allowance);

        BenefitRequestCalcItemTO takenBefore = new BenefitRequestCalcItemTO();
        takenBefore.setTitle("Taken Before");
        takenBefore.setAmount(benefitRequestCalcItem.getTotalUsedRequest() + " " + benefitRequestCalcItem.getQtyType());
        benefitRequestCalcItemList.add(takenBefore);

        BenefitRequestCalcItemTO left = new BenefitRequestCalcItemTO();
        left.setTitle("Left");
        left.setAmount(benefitRequestCalcItem.getTotalLeftRequest() + " " + benefitRequestCalcItem.getQtyType());
        benefitRequestCalcItemList.add(left);

        return successResponse(new BenefitRequestCalcItemListTO(benefitRequestCalcItemList));
    }

    @Operation(summary = "Delete Benefit", description = "Delete Benefit entity  Server should check if current user has permissions to delete this item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/requests/benefit/delete/{item_id}", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.REMOVE_BENEFIT_REQUEST, PermissionConstants.BENEFIT_REQUEST_LIST})
    public Object deleteBenefit(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsBenefitRequest edsBenefitRequest = benefitRequestManager.get(item_id);
        if (edsBenefitRequest == null || edsBenefitRequest.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Benefit with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.REMOVE_BENEFIT_REQUEST)) {
            try {
                availabilityServiceLocal.deleteBenefitRequest(item_id);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

}
