package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeLeaveStatusListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.LeaveRequestChartRpc;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.ApproversCountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.GetApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.RequestActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.CreateLeaveRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.DateRangeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveReasonStateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveRequestApproverTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveRequestDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveRequestResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveRequestStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveStatesCalResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.enums.LeaveRequestStatusTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestActionEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_LEAVE_REQUEST;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Leave Request", description = "Leave Request API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiLeaveRequestControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiLeaveRequestControllerV2.class);
    private final String REQUEST_TYPE_LEAVE = "LEAVE";
    private final String REQUEST_TYPE_BENEFIT = "BENEFIT";
    private final String REQUEST_TYPE_CASH_ADVANCED = "CASH_ADVANCED";
    private final String REQUEST_TYPE_EXPENSES_CLAIM = "EXPENSES_CLAIM";
    @Autowired
    RolePermissionManager rolePermissionManager;
    @Autowired
    AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    HrmsService hrmsService;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;

    @Operation(summary = "Get Leave Info", description = "Retrieves information about the Leave status and requests for the current user")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have leave statuses and requests."),
            @ApiResponse(responseCode = "400", description = "Year field required"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/leave_info", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getLeaveInfo(@RequestParam(value = "year") Integer year) throws RestException {
        if (year == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Year field required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsUser user = sickRequestManager.getUser();
        if (user == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        //Retrieving Leave Requests
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSelectedYear(year);
        filterParameter.setEmployeeId(user.getEmployee().getObjectID());
        filterParameter.setFromMobile(true);
        filterParameter.setLimit(MAX_LIMIT);

        List<EdsSickRequest> edsSickRequests = sickRequestManager.getLeaveRequestList(filterParameter);
        ArrayList<LeaveRequestTO> leaveRequestList = new ArrayList<>();

        if (edsSickRequests != null) {
            Map<Integer, Double[]> durationMap = sickRequestDurationManager.getEmployeesLeaveRequestsDuration(edsSickRequests);
            for (EdsSickRequest edsSickRequest : edsSickRequests) {
                try {
                    LeaveRequestTO leaveRequest = new LeaveRequestTO();
                    leaveRequest.setId(edsSickRequest.getObjectID());
                    if (edsSickRequest.getLeaveReason() != null) {
                        //When LR reason is other and other reason is not provided, I set "Other" as static into title instead of other reference type name
                        //Because "Other" was set such case as hard coded on "Latest Leave Requests" Tab. See EmployeeLeaveRequestsListTab.class
                        //If the hard code will be changed on Latest Leave Requests tab, we need to change it as well to avoid the difference of WEB & Mobile
                        if (CustomFormConstants.LR_TYPE_OTHER_LEAVE.equalsIgnoreCase(edsSickRequest.getLeaveReason().getCode())) {
                            leaveRequest.setTitle(StringUtils.isNotBlank(edsSickRequest.getOtherReason()) ? edsSickRequest.getOtherReason() : "Other");
                        } else {
                            leaveRequest.setTitle(edsSickRequest.getLeaveReason().getName());
                        }
                    }
                    leaveRequest.setDescription(edsSickRequest.getDescription());

                    SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

                    DateRangeTO dateRange = new DateRangeTO();
                    dateRange.setFrom(longDateTimezoneFormat.format(edsSickRequest.getStartDate()));
                    dateRange.setTo(longDateTimezoneFormat.format(edsSickRequest.getEndDate()));
                    leaveRequest.setDate_range(dateRange);
                    EdsEmployee employee = edsSickRequest.getEmployee();
                    if (employee != null) {
                        UserTO leaveRequestEmployee = new UserTO();
                        leaveRequestEmployee.setFirstName(employee.getFirstName());
                        leaveRequestEmployee.setLastName(employee.getLastName());
                        leaveRequestEmployee.setMiddleName(employee.getMiddleName());
                        leaveRequestEmployee.setId(employee.getObjectID());
                        leaveRequestEmployee.setImageUrl(hrmsServiceLocal.getEmployeeImageURL(employee.getObjectID()));
                        leaveRequestEmployee.setHireDate(employee.getStartDate());
                        leaveRequest.setUser(leaveRequestEmployee);
                    }
                    List<EdsApprover> approvers = edsSickRequest.getApprovers();
                    ArrayList<ApproverItemMini> leaveRequestApprovers = new ArrayList<>();
                    if (approvers != null && approvers.size() > 0) {
                        ApproverListStatusTO leaveStatus = new ApproverListStatusTO();
                        //if only one approver
                        if (approvers.size() == 1) {
                            if (approvers.get(0).getStatus() == null) {
                                continue;//todo temporary solution
                            }
                            if (EdsSickRequest.APPROVED.equals(approvers.get(0).getStatus().getCode())) {
                                leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (EdsSickRequest.DENIED.equals(approvers.get(0).getStatus().getCode())) {
                                leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else if (EdsSickRequest.NOT_DEFINED.equals(approvers.get(0).getStatus().getCode())) {
                                leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                            }
                            leaveRequestApprovers.add(approvers.get(0).getRPC());
                        } else {
                            //Means there are more than one approvers and we must set statuses based on them
                            FromValueTO dataTO = new FromValueTO();
                            dataTO.setFrom(approvers.size());
                            dataTO.setValue(0);
                            for (EdsApprover approver : approvers) {
                                if (approvers.get(0).getStatus() == null) {
                                    continue;//todo temporary solution
                                }
                                if (EdsSickRequest.APPROVED.equals(approver.getStatus().getCode())) {
                                    dataTO.setValue(dataTO.getValue() + 1);
                                }
                                leaveRequestApprovers.add(approvers.get(0).getRPC());
                            }
                            leaveStatus.setData(dataTO);
                            if (edsSickRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(edsSickRequest.getOverallStatus().getCode())) {
                                leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (edsSickRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(edsSickRequest.getOverallStatus().getCode())) {
                                leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else {
                                if (dataTO.getValue() == 0) {
                                    leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                    leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                    leaveStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                }
                            }
                        }
                        leaveRequest.setStatus(leaveStatus);
                    }
                    leaveRequest.setApproversList(leaveRequestApprovers);
                    if (edsSickRequest.getObjectID().equals(831)) {
                        System.currentTimeMillis();
                    }

                    Double[] leaveDays = durationMap.get(edsSickRequest.getObjectID());
                    if (leaveDays != null && leaveDays[2] != null) {
                        Double paidDays = leaveDays[2];
                        if (paidDays > 0) {
                            leaveRequest.setDays_paid(paidDays);
                        }
                    }
                    if (leaveDays != null && leaveDays[3] != null) {
                        Double daysNotPaid = leaveDays[3];
                        if (daysNotPaid > 0) {
                            leaveRequest.setDays_not_paid(daysNotPaid);
                        }
                    }
                    leaveRequestList.add(leaveRequest);
                } catch (NumberFormatException e) {
                    log.error("", e);
                }
            }
        }
        //End of Retrieving Leave Requests

        //Leave Statuses
        ArrayList<LeaveRequestStatusTO> requestStatusList = new ArrayList<>();
        SelectItem[] reasons = availabilityServiceLocal.getReasons(null);
        if (reasons != null) {
            Date startdate = ServerUtils.getYearStartDate(year);

            for (SelectItem reason : reasons) {
                LeaveRequestStatusTO leaveRequestStatusTO = new LeaveRequestStatusTO();
                leaveRequestStatusTO.setId(reason.getId());
                leaveRequestStatusTO.setCategory(StringUtils.isNotBlank(reason.getName()) ? reason.getName().trim() : null);
                //leaveRequestStatusTO.setType(reason.get);

                EmployeeLeaveStatusListItem employeeLeaveBalance = availabilityServiceLocal.getEmployeeLeaveBalanceBase(user.getObjectID(), reason.getDescription(), startdate, null);
                if (employeeLeaveBalance != null) {
                    if (StringUtils.isNotBlank(employeeLeaveBalance.getTotalLeaveRequest())) {
                        double totalDays = Double.valueOf(employeeLeaveBalance.getTotalLeaveRequest().split("\\|\\|")[0]);
                        //double totalHours = Double.valueOf(employeeLeaveBalance.getTotalLeaveRequest().split("\\|\\|")[1]);
                        leaveRequestStatusTO.setType(totalDays <= 0 ? LeaveRequestStatusTypeEnum.FIXED.getType() : LeaveRequestStatusTypeEnum.RANGE.getType());

                        FromValueTO data = new FromValueTO();
                        data.setValue(0);

                        if (StringUtils.isNotBlank(employeeLeaveBalance.getTotalUsedRequest())) {
                            double usedDays = Double.valueOf(employeeLeaveBalance.getTotalUsedRequest().split("\\|\\|")[0]);
                            //double usedHours = Double.valueOf(employeeLeaveBalance.getTotalUsedRequest().split("\\|\\|")[1]);
                            data.setValue((int) usedDays);
                            if (totalDays > 0) {
                                data.setFrom((int) totalDays);
                            } else if (totalDays == 0 && StringUtils.isNotBlank(employeeLeaveBalance.getTotalExceededRequest())) {
                                double totalExceededDays = Double.valueOf(employeeLeaveBalance.getTotalExceededRequest().split("\\|\\|")[0]);
                                data.setValue((int) totalExceededDays);
                            }
                        }
                        leaveRequestStatusTO.setData(data);
                    }
                }

                requestStatusList.add(leaveRequestStatusTO);
            }
        }
        //End of Leave Statuses
        LeaveRequestResultTO result = new LeaveRequestResultTO();
        result.setLeave_statuses(requestStatusList);
        result.setLeave_requests(leaveRequestList);

        return successResponse(result);
    }

    @Operation(summary = "Get Approvers Count", description = " Retrieves a number of approvers for certain type of request based on request type \n\n Request type should be LEAVE, BENEFIT, CASH_ADVANCED or EXPENSES_CLAIM ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have number of approvers"),
            @ApiResponse(responseCode = "400", description = "request_type field required")})
    @RequestMapping(value = "/approvers_count", method = RequestMethod.GET)
    public Object getApproversCount(@RequestParam(value = "request_type") String request_type) throws RestException {

        ApprovalListResult approvalListResult;
        if (REQUEST_TYPE_LEAVE.equalsIgnoreCase(request_type)) {
            approvalListResult = allInOneServiceLocal.getApprovers(RelationItem.TYPE_LEAVE_REQUEST, null, true, null, false);
        } else if (REQUEST_TYPE_BENEFIT.equalsIgnoreCase(request_type)) {
            //There is no multiple approvers for benefit requests, so there is only one approver
            return successResponse(new ApproversCountTO(1));
        } else if (REQUEST_TYPE_CASH_ADVANCED.equalsIgnoreCase(request_type)) {
            approvalListResult = allInOneServiceLocal.getApprovers(RelationItem.TYPE_CASH_ADVANCE, null, true, null, false);
        } else if (REQUEST_TYPE_EXPENSES_CLAIM.equalsIgnoreCase(request_type)) {
            approvalListResult = allInOneServiceLocal.getApprovers(RelationItem.TYPE_EXPENSE_CLAIM, null, true, null, false);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of LEAVE, BENEFIT, CASH_ADVANCED, EXPENSES_CLAIM", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        return successResponse(new ApproversCountTO(approvalListResult != null && approvalListResult.getList() != null ? approvalListResult.getList().size() : 0));
    }

    @Operation(summary = "Get Approvers List", description = "Retrieves list of approvers for certain type of request\n\n Request type should be LEAVE, BENEFIT, CASH_ADVANCED or EXPENSES_CLAIM")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of approvers"),
            @ApiResponse(responseCode = "400", description = "approver_index should be greater than 1"),
            @ApiResponse(responseCode = "400", description = "request_type is required")})
    @RequestMapping(value = "/requests/approvers_list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getApprovers(@RequestBody GetApproversTO request) throws RestException {

        if (request.getApprover_index() == null || request.getApprover_index() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "approver_index should be greater than 1", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        List<EdsApprover> approvalListResult;
        if (REQUEST_TYPE_LEAVE.equalsIgnoreCase(request.getRequest_type())) {
            approvalListResult = approverManager.list(RelationItem.TYPE_LEAVE_REQUEST, null);
        } else if (REQUEST_TYPE_BENEFIT.equalsIgnoreCase(request.getRequest_type())) {
            //There is no approval process for benefit request
            if (request.getApprover_index() != 1) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "approver_index is out of scope. Appover index for benefit request should be one only", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setStart(0);
            filterParameter.setLimit(MAX_LIMIT);
            filterParameter.setParams(PermissionConstants.BENEFIT_REQUEST_APPROVER);
            SelectItem[] approversList = allInOneServiceLocal.getEmployeesAsSelectItem(filterParameter);
            ArrayList<EmployeeTO> approvers = new ArrayList<>();
            if (approversList != null) {
                for (SelectItem employeeItem : approversList) {
                    EmployeeTO employeeTO = new EmployeeTO();
                    employeeTO.setId(employeeItem.getId());
                    employeeTO.setName(employeeItem.getName());
                    if (employeeItem.getName().contains("-")) {
                        employeeTO.setName(employeeItem.getName().split("-")[1].trim());
                    }
                    EdsEmployee edsEmployee = employeeManager.get(employeeItem.getId());
                    if (edsEmployee.getTeam() != null) {
                        employeeTO.setDepartment(edsEmployee.getTeam().getName());
                    }

                    EdsUpload photo = edsEmployee.getPhoto();
                    if (photo != null) {
                        employeeTO.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                    }
                    approvers.add(employeeTO);
                }
            }
            return successResponse(new EmployeeListTO(approvers));
        } else if (REQUEST_TYPE_CASH_ADVANCED.equalsIgnoreCase(request.getRequest_type())) {
            approvalListResult = approverManager.list(RelationItem.TYPE_CASH_ADVANCE, null);
        } else if (REQUEST_TYPE_EXPENSES_CLAIM.equalsIgnoreCase(request.getRequest_type())) {
            approvalListResult = approverManager.list(RelationItem.TYPE_EXPENSE_CLAIM, null);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of LEAVE, BENEFIT, CASH_ADVANCED, EXPENSES_CLAIM", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ArrayList<EmployeeTO> approvers = new ArrayList<>();
        if (approvalListResult != null && (approvalListResult.size()) >= request.getApprover_index()) {
            EdsApprover approverItem = approvalListResult.get(request.getApprover_index() - 1);

            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setListEmployees(true);
            filterParameter.setApproverID(approverItem.getObjectID());
            ArrayList<SelectItem> employees = allInOneServiceLocal.getApproverEmployeesAsSelectItem(new ListLoadConfig(), filterParameter);

            if (employees != null) {
                employees.forEach(employeeItem -> {
                    EmployeeTO employeeTO = new EmployeeTO();
                    employeeTO.setId(employeeItem.getId());
                    employeeTO.setName(employeeItem.getName());
                    if (employeeItem.getName().contains("-")) {
                        employeeTO.setName(employeeItem.getName().split("-")[1].trim());
                    }
                    EdsEmployee edsEmployee = employeeManager.get(employeeItem.getId());
                    if (edsEmployee != null && edsEmployee.getTeam() != null) {
                        employeeTO.setDepartment(edsEmployee.getTeam().getName());
                    }

                    if (edsEmployee != null) {
                        EdsUpload photo = edsEmployee.getPhoto();
                        if (photo != null) {
                            employeeTO.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                        }
                    }
                    approvers.add(employeeTO);
                });
            }

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "approver_index is out of scope", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        return successResponse(new EmployeeListTO(approvers));
    }

    @Operation(summary = "Get Users", description = "Retrieves list of employees for whom the manager can create request of a certain type \n\n request_type should be LEAVE, BENEFIT, CASH_ADVANCED or EXPENSES_CLAIM")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of users"),
            @ApiResponse(responseCode = "400", description = "request_type is required")})
    @RequestMapping(value = "/requests/users_list", method = RequestMethod.GET)
    public Object getUsers(@RequestParam(value = "request_type") String request_type,
                           @RequestParam(value = "query") String query,
                           @RequestParam(value = "limit", required = false) Integer limit,
                           @RequestParam(value = "offset", required = false) Integer start) throws RestException {

        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        query = query.replace("%20", " ").trim();

        List<EdsApprover> approvalListResult;
        if (REQUEST_TYPE_LEAVE.equalsIgnoreCase(request_type)) {
            approvalListResult = approverManager.list(RelationItem.TYPE_LEAVE_REQUEST, null);
        } else if (REQUEST_TYPE_BENEFIT.equalsIgnoreCase(request_type)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setStart(start != null && start >= 0 ? start : 0);
            filterParameter.setLimit(limit != null && limit > 0 ? limit : MAX_LIMIT);
            filterParameter.setSearchKey(query);

            filterParameter.setFromMobile(true);
            filterParameter.setSearchButton(true);
            filterParameter.setResignedEmployeesIncluded(false);

            List<String> roles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.ADD_BENEFIT_REQUEST);
            filterParameter.setRoles(ServerUtils.getAsCommoDelimited(roles, "", " "));

            ListResult<EmployeeListItem> employeeFromSolr = employeeServiceLocal.getEmployeeList(filterParameter);
            ArrayList<EmployeeTO> users = new ArrayList<>();
            if (employeeFromSolr != null) {
                for (EmployeeListItem userItem : employeeFromSolr.getList()) {
                    EmployeeTO employee = new EmployeeTO();
                    employee.setId(userItem.getObjectID());
                    employee.setName(userItem.getFullName());
                    if (userItem.getFullName().contains("-")) {
                        employee.setName(userItem.getFullName().split("-")[1].trim());
                    }
                    EdsEmployee edsEmployee = employeeManager.get(userItem.getObjectID());
                    if (edsEmployee.getTeam() != null) {
                        employee.setDepartment(edsEmployee.getTeam().getName());
                    }

                    EdsUpload photo = edsEmployee.getPhoto();
                    if (photo != null) {
                        employee.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                    }
                    users.add(employee);
                }
            }
            EmployeeListTO result = new EmployeeListTO(users);
            if (employeeFromSolr != null) {
                result.setTotal_count(employeeFromSolr.getTotal());
                if (employeeFromSolr.getTotal() < (filterParameter.getLimit() + filterParameter.getStart())) {
                    result.setLeft(0);
                } else {
                    result.setLeft(employeeFromSolr.getTotal() - (filterParameter.getStart() + filterParameter.getLimit()));
                }
                result.setCount(employeeFromSolr.getList() != null ? employeeFromSolr.getList().size() : 0);
                result.setOffset(filterParameter.getStart());
            } else {
                result.setLeft(0);
                result.setOffset(0);
                result.setCount(0);
                result.setTotal_count(0);
            }
            return successResponse(result);

        } else if (REQUEST_TYPE_CASH_ADVANCED.equalsIgnoreCase(request_type)) {
            approvalListResult = approverManager.list(RelationItem.TYPE_CASH_ADVANCE, null);
        } else if (REQUEST_TYPE_EXPENSES_CLAIM.equalsIgnoreCase(request_type)) {
            approvalListResult = approverManager.list(RelationItem.TYPE_EXPENSE_CLAIM, null);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of LEAVE, BENEFIT, CASH_ADVANCED, EXPENSES_CLAIM", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        boolean currentUserIsApprover = false;

        EdsUser user = approverManager.getUser();
        Set<EdsRole> userRoles = user.getRoles();

        if (userRoles != null && !userRoles.isEmpty()) {
            for (EdsApprover approver : approvalListResult) {
                if (approver.getApproverRoles() != null && !approver.getApproverRoles().isEmpty()) {
                    for (EdsApproverRoles edsApproverRoles : approver.getApproverRoles()) {
                        if (userRoles.contains(edsApproverRoles.getRole())) {
                            currentUserIsApprover = true;
                            break;
                        }
                    }
                }
            }
        }

        if (!currentUserIsApprover) {
            for (EdsApprover approver : approvalListResult) {
                if (approver.getApproverEmployees() != null && !approver.getApproverEmployees().isEmpty()) {
                    for (EdsApproverEmployees edsApproverEmployees : approver.getApproverEmployees()) {
                        if (user.getObjectID().equals(edsApproverEmployees.getEmployeeId())) {
                            currentUserIsApprover = true;
                            break;
                        }
                    }
                }
            }
        }

        ArrayList<EmployeeTO> approvers = new ArrayList<>();
        if (currentUserIsApprover) {

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setSearchKey(query);
            fp.setFromMobile(true);
            fp.setSearchButton(true);
            fp.setStart(start != null && start >= 0 ? start : 0);
            fp.setLimit(limit != null && limit > 0 ? limit : MAX_LIMIT);
            fp.setResignedEmployeesIncluded(false);
            ListResult<EmployeeListItem> employees = employeeServiceLocal.getEmployeeList(fp);

            if (employees != null) {
                for (EmployeeListItem employeeItem : employees.getList()) {
                    EmployeeTO employeeTO = new EmployeeTO();
                    employeeTO.setId(employeeItem.getObjectID());
                    employeeTO.setName(employeeItem.getFullName());
                    employeeTO.setDepartment(employeeItem.getDepartment());

                    EdsUpload photo = userManager.get(employeeItem.getObjectID()).getPhoto();
                    if (photo != null) {
                        employeeTO.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                    }
                    approvers.add(employeeTO);
                }
            }

            EmployeeListTO result = new EmployeeListTO(approvers);
            if (employees != null) {
                result.setTotal_count(employees.getTotal());
                if (employees.getTotal() < (fp.getLimit() + fp.getStart())) {
                    result.setLeft(0);
                } else {
                    result.setLeft(employees.getTotal() - (fp.getStart() + fp.getLimit()));
                }
                result.setCount(employees.getList() != null ? employees.getList().size() : 0);
                result.setOffset(fp.getStart());
            } else {
                result.setLeft(0);
                result.setOffset(0);
                result.setCount(0);
                result.setTotal_count(0);
            }
            return successResponse(result);
        } else {
            EmployeeListTO result = new EmployeeListTO(new ArrayList<>());
            result.setLeft(0);
            result.setOffset(0);
            result.setCount(0);
            result.setTotal_count(0);
            return successResponse(result);
        }
    }

    @Operation(summary = "Get Leave States Calc", description = "Calculates total leave, used and left allowances based on provided user and state \n\n Date format should be dd-MM-yyyy'T'hh:mm:ssZ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have calculation of leave states"),
            @ApiResponse(responseCode = "400", description = "state_id is required"),
            @ApiResponse(responseCode = "404", description = "there is no reason or user with provided state_id or user_id"),
            @ApiResponse(responseCode = "406", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/leave_calc", method = RequestMethod.GET)
    public Object getLeaveCalc(@RequestParam(value = "state_id") Integer stateid,
                               @RequestParam(value = "user_id", required = false) Integer userid,
                               @RequestParam(value = "start_date", required = false) String start_date,
                               @RequestParam(value = "end_date", required = false) String end_date) throws RestException {

        EdsLeaveReason reason = null;
        if (stateid == null || stateid < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "state_id not provided.", REQUIRED, HttpStatus.BAD_REQUEST);
        } else {
            reason = leaveReasonManager.get(stateid);
            if (reason == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "There is no reason with such id " + stateid, INVALID, HttpStatus.NOT_FOUND);
            }
        }
        EdsUser user;
        if (userid != null) {
            user = userManager.get(userid);
            if (user == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "There is no user with such id " + userid, INVALID, HttpStatus.NOT_FOUND);
            }
        } else {
            user = sickRequestManager.getUser();
        }
        if (user == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        Date startDate;
        Date endDate;
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        if (StringUtils.isNotBlank(start_date)) {
            try {
                startDate = longDateTimezoneFormat.parse(start_date);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "start_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            startDate = ServerUtils.getStartDate(new Date());
        }
        if (StringUtils.isNotBlank(end_date)) {
            try {
                endDate = longDateTimezoneFormat.parse(end_date);
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "end_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            endDate = ServerUtils.getEndDate(new Date());
        }

        if (startDate.after(endDate)) {
            throw new RestException("Start date should be before the end date.", "Start date should be before the end date.", INVALID, HttpStatus.NOT_ACCEPTABLE);
        }

        ArrayList<LeaveReasonStateTO> leaveReasonStates = getLeaveReasonStateList(reason, user, startDate, endDate);

        return successResponse(new LeaveStatesCalResponseTO(leaveReasonStates));
    }

    private ArrayList<LeaveReasonStateTO> getLeaveReasonStateList(EdsLeaveReason reason, EdsUser user, Date startDate, Date endDate) throws RestException {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(user.getObjectID());
        fp.setReasonID(reason.getObjectID());
        fp.setYear(Integer.parseInt(new SimpleDateFormat("yyyy").format(startDate)));
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        LeaveRequestChartRpc data = availabilityServiceLocal.getLeaveRequestChartData(fp);

        ArrayList<LeaveReasonStateTO> leaveReasonStates = new ArrayList<>();

        EdsAnnualLeaveAllowance edsAnnualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(fp.getYear(), user.getObjectID(), reason.getCode(), null);
        if (edsAnnualLeaveAllowance != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Allowance", 0d, edsAnnualLeaveAllowance.getAllowanceDays()));
        }
        if (data.getPaid().length > 0 && data.getPaid()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Approved Paid Days", 0d, data.getPaid()[0]));
        }
        if (data.getLeft().length > 0 && data.getLeft()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Left Days", 0d, data.getLeft()[0]));
        }
        if (data.getNonPaid().length > 0 && data.getNonPaid()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Non-Paid Days", 0d, data.getNonPaid()[0]));
        }
        return leaveReasonStates;
    }

    @Operation(summary = "Create Leave Request", description = "Creates new Leave Request ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have if successfully added or false if not added successfully with error code"),
            @ApiResponse(responseCode = "400", description = "user is required"),
            @ApiResponse(responseCode = "404", description = "User or leave reason is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/leave_request", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = {/*MediaType.APPLICATION_JSON_UTF8_VALUE, */MediaType.MULTIPART_FORM_DATA_VALUE})
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU, PermissionConstants.HRMS_ADD_REQUEST})
    public Object createLeaveRequest(MultipartRequest multipartRequest,
                                     @Parameter(name = "file", description = "Leave Request attachments.", allowEmptyValue = true) @RequestPart(name = "file", required = false) MultipartFile[] files,
                                     @Parameter(name = "body", description = """
                                             {<br>
                                               "annual_allowance": true, <br>
                                               "approvers": [<br>
                                                 number<br>
                                               ],<br>
                                               "end_date": "dd-MM-yyyy'T'hh:mm:ssZ",<br>
                                               "description": "string",<br>  "leave_by": "DAY",<br>
                                               "leave_reason": number,<br>
                                               "start_date": "dd-MM-yyyy'T'hh:mm:ssZ",<br>
                                               "user_id": number<br>
                                             }""", schema = @Schema(type = "string")/*examples = @Example(value = {
                                             @ExampleProperty(
                                                     mediaType="application/json",
                                                     value = "{\"user_id\":\"1234\",\"userName\":\"JoshJ\"}"
                                             )
                                     })*/) @RequestParam(name = "body")/* CreateLeaveRequestTO body*/ String jsonString
            /*@Parameter(type = "string") @RequestParam("body") CreateLeaveRequestTO body*/) throws RestException {

        ObjectMapper mapper = new ObjectMapper();
        CreateLeaveRequestTO body;
        try {
            body = mapper.readValue(jsonString, CreateLeaveRequestTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsUser user = employeeManager.getUser();
        if (user == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String status = userManager.getUserStatus(user.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        EdsEmployee employee;
        if (body.getUser_id() != null) {
            employee = employeeManager.get(body.getUser_id());
            if (employee == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "There is no user with such id.", INVALID, HttpStatus.NOT_FOUND);
            }
            if (ServerUtils.hasPermission(PermissionConstants.HRMS_LEAVE_REQUESTS_ADD_FOR_OTHERS) || ServerUtils.hasPermission(PermissionConstants.HRMS_ADD_REQUEST)) {
                //todo
            } else {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } else {
            employee = employeeManager.get(user.getObjectID());
            if (employee == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, user.getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
            }
        }


        Date startDate;
        Date endDate;

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (StringUtils.isNotBlank(body.getStart_date())) {

            try {
                startDate = user.getUserDate(longDateTimezoneFormat.parse(body.getStart_date()));
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "start_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "start_date not provided (eg. " + longDateTimezoneFormat.toPattern() + ").", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotBlank(body.getEnd_date())) {
            try {
                endDate = user.getUserDate(longDateTimezoneFormat.parse(body.getEnd_date()));
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, "end_date format should be " + longDateTimezoneFormat.toPattern() + " (eg." + longDateTimezoneFormat.format(new Date()) + ").", INVALID, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "end_date not provided (eg. " + longDateTimezoneFormat.toPattern() + ").", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //Validate dates, there must be positive difference
        SimpleDateFormat dateFormatWithMinOnly = new SimpleDateFormat("dd-MM-yyyy'T'hh:mmZ");
        if (dateFormatWithMinOnly.format(startDate).equalsIgnoreCase(dateFormatWithMinOnly.format(endDate))) {
            throw new RestException("Invalid date.", "start_date and end_date are equal.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        //End date must be after start date
        if (startDate.after(endDate)) {
            throw new RestException("Invalid date.", "start_date after end_date.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        NewLeaveRequest newLeaveRequest = new NewLeaveRequest();
        newLeaveRequest.setEmployee(employee.getObjectID());
        newLeaveRequest.setDescription(body.getDescription());


        if (body.getLeave_reason() != null) {
            newLeaveRequest.setReasonId(body.getLeave_reason());
        }

        if (body.getApprovers() != null) {
            newLeaveRequest.setEmployeeIds(body.getApprovers());
        }
        newLeaveRequest.setTypeBoolean(body.isAnnual_allowance());
        newLeaveRequest.setTakeByMoney(Constants.MONEY.equalsIgnoreCase(body.getLeave_by()));

        newLeaveRequest.setStartHour(startDate.getHours());
        newLeaveRequest.setStartMinut(startDate.getMinutes());
        newLeaveRequest.setEndHour(endDate.getHours());
        newLeaveRequest.setEndMinut(endDate.getMinutes());

        DateNonConvertable start = new DateNonConvertable(startDate);
        if (start.getDate() != null) {
            newLeaveRequest.setDate((String.valueOf(start.getDate().getTime())));
            newLeaveRequest.setStartNonConverable(start);
        }
        newLeaveRequest.setDay(String.valueOf(WrapUtils.longToDate(startDate.getTime()).getDate()));
        newLeaveRequest.setMonth(String.valueOf(WrapUtils.longToDate(startDate.getTime()).getMonth()));

        DateNonConvertable end = new DateNonConvertable(endDate);
        if (end.getDate() != null) {
            newLeaveRequest.setDateE((String.valueOf(end.getDate().getTime())));
            newLeaveRequest.setEndNonConverable(end);
        }
        newLeaveRequest.setDayE(String.valueOf(WrapUtils.longToDate(endDate.getTime()).getDate()));
        newLeaveRequest.setMonthE(String.valueOf(WrapUtils.longToDate(endDate.getTime()).getMonth()));
        /**/
        if (body.getApprovers() != null && body.getApprovers().size() > 0) {
            newLeaveRequest.setApprovers(getChosenApprovers(body.getApprovers(), getAllAvailableApprovers(RelationItem.TYPE_LEAVE_REQUEST)));
        }

        newLeaveRequest.setType(EdsSickRequest.PAID);

        String hasAccessInsertRequest = availabilityServiceLocal.hasAccessInsertRequest(newLeaveRequest.getEmployee(), null, new DateNonConvertable(startDate), new DateNonConvertable(endDate), false);
        if (Constants.TRUE.equals(hasAccessInsertRequest)) {
            Integer leaveRequestId = 0;
            try {
                //Save Leave Request data
                leaveRequestId = availabilityServiceLocal.createLeaveRequest(newLeaveRequest);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (leaveRequestId != null && leaveRequestId > 0) {
                try {
                    //Save Leave Request Attachements
                    if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                        for (MultipartFile file : multipartRequest.getFileMap().values()) {
                            //Save Leave Request Attachement
                            documentsServiceLocal.saveDocumentFile(file, null, F_LEAVE_REQUEST, leaveRequestId, "");
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                }

                return successResponse(new ResponseData());
            } else {
                //if LeaveRequest was not created then throw exception
                throw new RestException(GENERAL_ERROR_MESSAGE, "Request has not been created.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            String message = employee.getFullName() + " already has request for this period.";
            throw new RestException("You already have requests on these dates. Please double check and try again", message, CONFLICT, HttpStatus.CONFLICT);
        }


    }


    @Operation(summary = "Get Leave Request Details", description = "Retrieving Leave Request details by ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"data\" field of response will have Leave Request details."),
            @ApiResponse(responseCode = "400", description = "request_id is not provided."),
            @ApiResponse(responseCode = "404", description = "Leave Request is not found.")})
    @RequestMapping(value = "/requests/leave_request", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getLeaveRequest(@RequestParam("request_id") Integer requestId) throws RestException {

        if (requestId == null || requestId < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is not provided.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsSickRequest sickRequest = sickRequestManager.get(requestId);
        if (sickRequest == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request with id " + requestId + " is not found.", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        StatisticsLeaveRequest leaveRequest = availabilityServiceLocal.getLeaveRequest(requestId);
        if (leaveRequest != null) {
            EdsUser user = userManager.getUser();

            LeaveRequestDetailsTO leaveRequestDetailsTO = new LeaveRequestDetailsTO();

            leaveRequestDetailsTO.setId(leaveRequest.getObjectID());
            leaveRequestDetailsTO.setTitle(leaveRequest.getReason());
            leaveRequestDetailsTO.setDescription(leaveRequest.getDescription());

            //We shouldn't return Owner if its same as current user
            if (!user.getObjectID().equals(leaveRequest.getEmployeeId())) {
                EmployeeTO owner = new EmployeeTO();
                owner.setId(leaveRequest.getEmployeeId());
                owner.setName(leaveRequest.getEmployee());
                owner.setDepartment(leaveRequest.getDepartment());
                EdsUpload photo = userManager.get(leaveRequest.getEmployeeId()).getPhoto();
                if (photo != null) {
                    owner.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                }

                leaveRequestDetailsTO.setOwner(owner);
            }

            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

            DateRangeTO dateRange = new DateRangeTO();

            //Android side adds timezone difference of user that's why we are subtracting the difference
            Date from = new Date(leaveRequest.getStartDDate().getNonConvertedDate().getTime() - user.getUserTimezone().getRawOffset());
            Date to = new Date(leaveRequest.getEndDDate().getNonConvertedDate().getTime() - user.getUserTimezone().getRawOffset());

            dateRange.setFrom(longDateTimezoneFormat.format(from));
            dateRange.setTo(longDateTimezoneFormat.format(to));
            leaveRequestDetailsTO.setDate_range(dateRange);

            if (leaveRequest.getNonPaidDays() != 0) {
                leaveRequestDetailsTO.setDays_not_paid(leaveRequest.getNonPaidDays());
            }

            if (leaveRequest.getApprovers() != null && leaveRequest.getApprovers().size() > 0) {

                ArrayList<LeaveRequestApproverTO> approvers = new ArrayList<>();

                //if only one approver
                if (leaveRequest.getApprovers().size() == 1) {
                    ApproverListStatusTO leaveStatus = new ApproverListStatusTO();
                    if (leaveRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(leaveRequest.getOverallStatus().getCode())) {
                        leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                        leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.NOT_DEFINED.equals(leaveRequest.getOverallStatus().getCode())) {
                        leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                    }
                    leaveRequestDetailsTO.setStatus(leaveStatus);
                    //Set Approver
                    ApproverItemMini onlyOneApprover = leaveRequest.getApprovers().get(0);

                    LeaveRequestApproverTO approverTO = new LeaveRequestApproverTO();
                    approverTO.setStatus(leaveStatus);

                    if (onlyOneApprover.getExactEmployee() != null) {
                        EdsUser employeeApprover = userManager.get(onlyOneApprover.getExactEmployee().getId());
                        if (employeeApprover != null) {
                            approverTO.setId(employeeApprover.getObjectID());
                            approverTO.setName(employeeApprover.getFullName());
                            if (employeeApprover.getPhoto() != null) {
                                approverTO.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employeeApprover.getObjectID()));
                            }
                            EdsEmployee employee = employeeApprover.isEmployee() ? employeeApprover.getEmployee() : null;
                            if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                                approverTO.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                            }
                        }
                    }
                    approvers.add(approverTO);
                } else {
                    //Means there are more than one approvers and we must set statuses based on them
                    FromValueTO dataTO = new FromValueTO();
                    dataTO.setFrom(leaveRequest.getApprovers().size());
                    dataTO.setValue(0);
                    for (ApproverItemMini approver : leaveRequest.getApprovers()) {
                        if (approver.getStatus() == null) {
                            continue;//todo temporary solution
                        }
                        //Set Multi Approvers
                        ApproverListStatusTO approverStatus = new ApproverListStatusTO();
                        if (EdsSickRequest.APPROVED.equals(approver.getStatus().getCode())) {
                            approverStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            //Count number of APPROVED approvers
                            dataTO.setValue(dataTO.getValue() + 1);
                        } else if (EdsSickRequest.DENIED.equals(approver.getStatus().getCode())) {
                            approverStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                        } else if (EdsSickRequest.NOT_DEFINED.equals(approver.getStatus().getCode())) {
                            approverStatus.setType(RequestStatusEnum.PENDING.getStatus());
                        }

                        LeaveRequestApproverTO approverTO = new LeaveRequestApproverTO();
                        approverTO.setStatus(approverStatus);

                        if (approver.getExactEmployee() != null) {
                            EdsUser employeeApprover = userManager.get(approver.getExactEmployee().getId());
                            if (employeeApprover != null) {
                                approverTO.setId(employeeApprover.getObjectID());
                                approverTO.setName(employeeApprover.getFullName());
                                if (employeeApprover.getPhoto() != null) {
                                    approverTO.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employeeApprover.getObjectID()));
                                }
                                EdsEmployee employee = employeeApprover.isEmployee() ? employeeApprover.getEmployee() : null;
                                if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                                    approverTO.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                                }
                            }
                        }
                        approvers.add(approverTO);
                        //End Of Set Multi Approvers
                    }
                    //Overall LeaveRequest Status
                    ApproverListStatusTO leaveStatus = new ApproverListStatusTO();
                    leaveStatus.setData(dataTO);
                    if (leaveRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(leaveRequest.getOverallStatus().getCode())) {
                        leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                        leaveStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else {
                        if (dataTO.getValue() == 0) {
                            leaveStatus.setType(RequestStatusEnum.PENDING.getStatus());
                        } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                            leaveStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                        } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                            leaveStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                        }
                    }
                    leaveRequestDetailsTO.setStatus(leaveStatus);

                }
                //Setting Approvers
                leaveRequestDetailsTO.setApprovers(approvers);
                //Set user actions
                leaveRequestDetailsTO.setUser_actions(getUserAction(leaveRequest, user));
            }

            if (leaveRequest.getCreatedDate() != null) {
                leaveRequestDetailsTO.setCreated_at(longDateTimezoneFormat.format(leaveRequest.getCreatedDate()));
            }
            //leave request attachment
            List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_LEAVE_REQUEST, requestId, requestId);
            if (attachments != null && !attachments.isEmpty()) {
                ArrayList<AttachmentTO> attachmentTOS = new ArrayList<>();
                for (FileResource fileResource : attachments) {
                    attachmentTOS.add(new AttachmentTO(fileResource.getFileName(), fileResource.getAmazonLink()));
                }
                leaveRequestDetailsTO.setAttachments(attachmentTOS);
            }

            leaveRequestDetailsTO.setState_records(getLeaveReasonStateList(leaveReasonManager.findByCode(leaveRequest.getReasonCode()), user, leaveRequest.getStartDDate().getNonConvertedDate(), leaveRequest.getEndDDate().getNonConvertedDate()));
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStartDate(from);
            fp.setEndDate(to);
            fp.setEmployeeId(leaveRequest.getEmployeeId());
            fp.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
            fp.setReasonCode(leaveRequest.getReasonCode());
            fp.setPaid(true);
            leaveRequestDetailsTO.getState_records().add(setLeaveRequestDetail(fp));
            return successResponse(leaveRequestDetailsTO);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request is not found.", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Leave Request Action", description = "Approves or Rejects leave request \n\n Request action should be APPROVE, REJECT or APPROVE_FOR_ALL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "request_id and request action are required"),
            @ApiResponse(responseCode = "404", description = "Leave request with provided request_id is not found"),
            @ApiResponse(responseCode = "409", description = "Leave request has already been approved or rejected"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/requests/leave_request", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object leaveRequestAction(@RequestBody RequestActionTO leaveAction) throws RestException {
        if (leaveAction.getRequest_id() == null || leaveAction.getRequest_id() <= 0) {
            throw new RestException(ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leaveAction.getAction())) {
            throw new RestException(ERROR_MESSAGE, "Request action is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        StatisticsLeaveRequest leaveRequest = availabilityServiceLocal.getLeaveRequest(leaveAction.getRequest_id());
        if (leaveRequest == null || leaveRequest.getObjectID() == null) {
            throw new RestException(ERROR_MESSAGE, "Leave request with id " + leaveAction.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        RequestUserActionTO userAction = getUserAction(leaveRequest, userManager.getUser());
        String status = null;
        boolean approveForAll = false;
        if (RequestActionEnum.APPROVE.name().equals(leaveAction.getAction())) {
            if (userAction.isApprove()) {
                status = Constants.LR_STATUS_SS_APPROVED;
            } else {
                if (leaveRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "Leave request has already been approved", CONFLICT, HttpStatus.CONFLICT);
                } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "You cannot approve already rejected request", CONFLICT, HttpStatus.CONFLICT);
                }
            }
        } else if (RequestActionEnum.REJECT.name().equals(leaveAction.getAction())) {
            if (userAction.isReject()) {
                status = Constants.LR_STATUS_SS_DENIED;
            } else {
                //overall status can be null if rejected
                if (leaveRequest.getOverallStatus() == null || EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "Leave request has already been rejected", CONFLICT, HttpStatus.CONFLICT);
                } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "Leave request has already been rejected", CONFLICT, HttpStatus.CONFLICT);
                } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "You cannot reject already approved request", CONFLICT, HttpStatus.CONFLICT);
                }
            }
        } else if (RequestActionEnum.APPROVE_FOR_ALL.name().equals(leaveAction.getAction())) {
            if (userAction.isApprove_for_all()) {
                status = Constants.LR_STATUS_SS_APPROVED;
                approveForAll = true;
            } else {
                if (leaveRequest.getOverallStatus() != null && EdsSickRequest.APPROVED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "Leave request has already been approved", CONFLICT, HttpStatus.CONFLICT);
                } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.DENIED.equals(leaveRequest.getOverallStatus().getCode())) {
                    throw new RestException(ERROR_MESSAGE, "You cannot approve already rejected request", CONFLICT, HttpStatus.CONFLICT);
                } else if (userAction.isApprove() && userAction.isReject()) {
                    if (leaveRequest.getOverallStatus() != null && EdsSickRequest.NOT_DEFINED.equals(leaveRequest.getOverallStatus().getCode())) {
                        throw new RestException(ERROR_MESSAGE, "You cannot approve for all. But you can approve or reject", CONFLICT, HttpStatus.CONFLICT);
                    }
                }
            }
        } else {
            throw new RestException(ERROR_MESSAGE, "Request action should be one of id APPROVE, REJECT, APPROVE_FOR_ALL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        try {
            availabilityServiceLocal.updateApprove(status, leaveAction.getRequest_id(), approveForAll);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    private RequestUserActionTO getUserAction(StatisticsLeaveRequest leaveRequest, EdsUser user) {
        RequestUserActionTO userAction = new RequestUserActionTO();
        if (!leaveRequest.isAction()) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
        } else if (EdsSickRequest.NOT_DEFINED.equals(leaveRequest.getOverallStatus().getCode())) {
            if (user.hasRole(EdsRole.ADMIN_CODE) || (leaveRequest.getCurrentApproverEmployeeID() != null && user.getObjectID().equals(leaveRequest.getCurrentApproverEmployeeID()))) {
                if (leaveRequest.isApproveForAll()) {
                    userAction.setApprove_for_all(true);
                    userAction.setApprove(true);
                    userAction.setReject(true);
                } else {
                    userAction.setApprove_for_all(false);
                    userAction.setApprove(true);
                    userAction.setReject(true);
                }
            } else {
                userAction.setApprove_for_all(false);
                userAction.setApprove(false);
                userAction.setReject(false);
            }
        } else {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
        }
        return userAction;
    }

    private LeaveReasonStateTO getLeaveReasonState(String title, String lRequest) {
        if (lRequest == null || lRequest.isEmpty()) {
            return null;
        }
        double days = Double.valueOf(lRequest.split("\\|\\|")[0]);
        double hours = Double.valueOf(lRequest.split("\\|\\|")[1]);

        return new LeaveReasonStateTO(title, hours, days);
    }

    private LeaveReasonStateTO setLeaveRequestDetail(ListingFilterParameter fp) {
        final HashMap<Integer, Double> duration = this.sickRequestDurationManager.getEmployeeLeaveDurations(fp);
        Double dayTaken = 0d;
        Double allowanceDays = 0d;
        final Double durationArray = duration.get(fp.getEmployeeId());
        if (durationArray != null) {
            dayTaken = durationArray;
        }
        return new LeaveReasonStateTO("Taken", 0.0d, dayTaken);
    }
}
