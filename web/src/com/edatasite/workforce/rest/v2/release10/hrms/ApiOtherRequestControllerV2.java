package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPerformanceNote;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PerformanceNoteManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.TitleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.OtherActionRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.OtherRequestDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.OtherRequestListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.OtherRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.ApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.enums.OtherRequestEnum;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilxom Lutfullaev on 02.12.2017.
 */

@Tag(name = "Other Request", description = "Other Request API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiOtherRequestControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiOtherRequestControllerV2.class);

    @Autowired
    private HttpServletRequest servletRequest;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private PerformanceNoteManager performanceNoteManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;

    protected static Object getStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return new StatusTO(RequestStatusEnum.PENDING.getStatus());
        }
        return switch (status.getCode()) {
            case Constants.PAYMENT_STATUS_DRAFT -> new StatusTO(RequestStatusEnum.DRAFT.getStatus());
            case Constants.PAYMENT_STATUS_APPROVED -> new StatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.PAYMENT_STATUS_SUBMITTED -> new StatusTO(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
            case Constants.PAYMENT_STATUS_REJECTED -> new StatusTO(RequestStatusEnum.DECLINED.getStatus());
            default -> new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(), new TitleTO(status.getName()));
        };

    }

    @Operation(summary = "Get Other Requests list", description = "Retrieves Additional Payment, Incident and Goal List")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of Additional Payment, Incident and Goal"),
            @ApiResponse(responseCode = "400", description = "Year is required")})
    @RequestMapping(value = "/other_requests", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getOtherRequestList(@RequestParam(value = "year") Integer year) throws RestException {
        if (year == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "year is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        //Result to return
        ArrayList<OtherRequestTO> otherRequests = new ArrayList<>();

        EdsUser currentUser = userManager.getUser();
        SelectItem[] currentUserAsSelectItem = {new SelectItem(currentUser.getObjectID(), currentUser.getName())};
        SelectItem[] yearItem = {new SelectItem(year, year.toString())};

        //Additional Payments
        ListingFilterParameter additionalPaymentsFilterParameter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.AdditionalPayment);
        additionalPaymentsFilterParameter.setAscending(false);
        additionalPaymentsFilterParameter.setStart(0);
        additionalPaymentsFilterParameter.setLimit(MAX_LIMIT);
        //Create filter by year
        additionalPaymentsFilterParameter.getFacetFilter().getFacetContentMap().get(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[1]).setFacetItems(yearItem);
        //Create filter by Creator
        additionalPaymentsFilterParameter.getFacetFilter().getFacetContentMap().get(FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[2]).setFacetItems(currentUserAsSelectItem);
        //Retrieve data from SOLR
        ListResult<AdditionalPayment> additionalPaymentsList = payrollServiceLocal.getAdditionalPaymentList(additionalPaymentsFilterParameter);
        //Convert additional payments to API Transfer object
        if (additionalPaymentsList != null && additionalPaymentsList.getList() != null) {
            additionalPaymentsList.getList().forEach(additionalPayment -> {
                OtherRequestTO requestTO = new OtherRequestTO();
                requestTO.setId(additionalPayment.getObjectID());
                if (StringUtils.isNotBlank(additionalPayment.getReference())) {
                    requestTO.setTitle(additionalPayment.getReference());
                }
                requestTO.setType(OtherRequestEnum.ADDITIONAL_PAYMENT.getStatus());
                StringBuilder description = new StringBuilder();
                if (StringUtils.isNotBlank(additionalPayment.getMonth())) {
                    description.append(additionalPayment.getMonth());
                }
                if (additionalPayment.getYear() != null) {
                    description.append("-").append(additionalPayment.getYear());
                }
                if (StringUtils.isNotBlank(additionalPayment.getReference())) {
                    description.append(" ").append(additionalPayment.getReference());
                }
                if (description.length() > 144) {
                    requestTO.setDescription(description.substring(0, 141) + "...");
                } else {
                    requestTO.setDescription(description.toString());
                }

                if (additionalPayment.getApprover() != null && StringUtils.isNotBlank(additionalPayment.getApprover().getName())) {
                    requestTO.setApprover(additionalPayment.getApprover().getName());
                }
                requestTO.setRequester(additionalPayment.getCreator() != null && StringUtils.isNotBlank(additionalPayment.getCreator().getName()) ? additionalPayment.getCreator().getName() : "");
                SelectItem selectItem = new SelectItem();
                selectItem.setCode(additionalPayment.getStatusCode());
                selectItem.setName(additionalPayment.getStatus());
                requestTO.setStatus(getStatus(selectItem));

                EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(additionalPayment.getObjectID());
                List<EdsApprover> additionalPaymentApprovers = edsAdditionalPayment.getApprovers();
                if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPayment.getStatusCode())) {
                    if (additionalPaymentApprovers != null && additionalPaymentApprovers.size() > 0) {
                        ApproverListStatusTO paymentStatus = new ApproverListStatusTO();
                        if (additionalPaymentApprovers.size() == 1) {
                            if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                                paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                            }
                        } else {
                            //Means there are more than one approvers and we must set statuses based on them
                            FromValueTO dataTO = new FromValueTO();
                            dataTO.setFrom(additionalPaymentApprovers.size());
                            dataTO.setValue(0);
                            for (EdsApprover approver : additionalPaymentApprovers) {
                                if (Constants.PAYMENT_STATUS_APPROVED.equals(approver.getStatus().getCode())) {
                                    dataTO.setValue(dataTO.getValue() + 1);
                                }
                            }
                            paymentStatus.setData(dataTO);
                            if (additionalPayment.getStatusCode() != null && Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getStatusCode())) {
                                paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (additionalPayment.getOverallStatus() != null && Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getStatusCode())) {
                                paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else {
                                if (dataTO.getValue() == 0) {
                                    paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                    paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                    paymentStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                }
                            }
                        }
                        requestTO.setStatus(paymentStatus);
                    }
                }
                otherRequests.add(requestTO);
            });
        }
        //End Of Additional Payments


        //Goals
        ListingFilterParameter goalsFilterParameter = new ListingFilterParameter();
        goalsFilterParameter.setObjectId(currentUser.getObjectID());
        goalsFilterParameter.setYear(year);
        goalsFilterParameter.setStart(0);
        goalsFilterParameter.setLimit(MAX_LIMIT);

        ListResult<GoalItem> goalItemList;
        try {
            goalItemList = hrmsServiceLocal.getPersonalGoalList(goalsFilterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (goalItemList != null && goalItemList.getList() != null) {
            goalItemList.getList().forEach(goal -> {
                OtherRequestTO requestTO = new OtherRequestTO();
                requestTO.setId(goal.getObjectId());
                requestTO.setTitle(goal.getTitle());
                requestTO.setType(OtherRequestEnum.GOAL.getStatus());
                if (StringUtils.isNotBlank(goal.getDescription())) {
                    if (goal.getDescription().length() > 144) {
                        requestTO.setDescription(goal.getDescription().substring(0, 141) + "...");
                    } else {
                        requestTO.setDescription(goal.getDescription());
                    }
                }

                requestTO.setStatus(new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(),
                        goal.getStatus() != null && StringUtils.isNotBlank(goal.getStatus()) ? new TitleTO(goal.getStatus()) : null));
                otherRequests.add(requestTO);
            });
        }
        //End of Goals

        //Incidents
        ListingFilterParameter incidentsFilterParameter = new ListingFilterParameter();
        incidentsFilterParameter.setEmployeeId(currentUser.getObjectID());
        incidentsFilterParameter.setIncident(true);
        incidentsFilterParameter.setAllByFilter(true);
        incidentsFilterParameter.setYear(year);
        incidentsFilterParameter.setStart(0);
        incidentsFilterParameter.setLimit(MAX_LIMIT);
        //Retrieve Incidents from database because they are not solarized
        List<EdsPerformanceNote> incidentsList = performanceNoteManager.getList(incidentsFilterParameter);

        incidentsList.forEach(incident -> {
            OtherRequestTO requestTO = new OtherRequestTO();
            requestTO.setId(incident.getObjectID());
            requestTO.setTitle(incident.getName());
            requestTO.setType(OtherRequestEnum.INCIDENT.getStatus());
            if (StringUtils.isNotBlank(incident.getDescription())) {
                requestTO.setDescription(incident.getDescription());
            }
            requestTO.setStatus(new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(),
                    incident.getStatus() != null && StringUtils.isNotBlank(incident.getStatus().getName()) ? new TitleTO(incident.getStatus().getName()) : null));
            otherRequests.add(requestTO);
        });
        //End of Incidents
        return successResponse(new OtherRequestListTO(otherRequests));
    }

    @Operation(summary = "Get Other Request details", description = "Retrieves the details of Additional Payment, Incident and Goal based on provided request_id and request_type. \n" +
            "The request type should be one of the followings: ADDITIONAL_PAYMENT, INCIDENT, GOAL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have details of Additional Payment, Incident and Goal"),
            @ApiResponse(responseCode = "400", description = "request_id is required"),
            @ApiResponse(responseCode = "422", description = "request_id should be more then zero"),
            @ApiResponse(responseCode = "400", description = "type is required")
    })
    @RequestMapping(value = "/requests/other_request", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getOtherRequestDetails(@RequestParam(value = "request_id") Integer request_id, @RequestParam(value = "type") String type) throws RestException {
        if (request_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id should be more then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        EdsUser user = userManager.getUser();
        OtherRequestDetailsTO otherRequestDetails = new OtherRequestDetailsTO();
        if (OtherRequestEnum.ADDITIONAL_PAYMENT.getStatus().equalsIgnoreCase(type)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(request_id);
            AdditionalPayment additionalPayment;
            try {
                additionalPayment = payrollServiceLocal.getAdditionalPaymentData(filterParameter);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (additionalPayment.getObjectID() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Additional Payment has not been found with provided request_id", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            otherRequestDetails.setId(additionalPayment.getObjectID());
            if (additionalPayment.getEmployee() != null && !user.getObjectID().equals(additionalPayment.getEmployee().getId())) {
                EdsEmployee employee = employeeManager.get(additionalPayment.getEmployee().getId());
                if (employee != null) {
                    OwnerTO owner = new OwnerTO();
                    owner.setId(employee.getObjectID());
                    owner.setName(employee.getName());
                    try {
                        owner.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employee.getObjectID()));
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                        owner.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                    }
                    otherRequestDetails.setOwner(owner);
                }
            }
            otherRequestDetails.setTitle(additionalPayment.getReference());
            otherRequestDetails.setStatus(getStatus(additionalPayment.getOverallStatus()));

            EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(additionalPayment.getObjectID());
            List<EdsApprover> additionalPaymentApprovers = edsAdditionalPayment.getApprovers();
            if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPayment.getStatusCode())) {
                if (additionalPaymentApprovers != null && additionalPaymentApprovers.size() > 0) {
                    ApproverListStatusTO paymentStatus = new ApproverListStatusTO();
                    if (additionalPaymentApprovers.size() == 1) {
                        if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                            paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                        } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                            paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                        } else if (Constants.PAYMENT_STATUS_SUBMITTED.equals(additionalPaymentApprovers.get(0).getStatus().getCode())) {
                            paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                        }
                    } else {
                        //Means there are more than one approvers and we must set statuses based on them
                        FromValueTO dataTO = new FromValueTO();
                        dataTO.setFrom(additionalPaymentApprovers.size());
                        dataTO.setValue(0);
                        for (EdsApprover approver : additionalPaymentApprovers) {
                            if (Constants.PAYMENT_STATUS_APPROVED.equals(approver.getStatus().getCode())) {
                                dataTO.setValue(dataTO.getValue() + 1);
                            }
                        }
                        paymentStatus.setData(dataTO);
                        if (additionalPayment.getStatusCode() != null && Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getStatusCode())) {
                            paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                        } else if (additionalPayment.getOverallStatus() != null && Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getStatusCode())) {
                            paymentStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                        } else {
                            if (dataTO.getValue() == 0) {
                                paymentStatus.setType(RequestStatusEnum.PENDING.getStatus());
                            } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                paymentStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                paymentStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                            }
                        }
                    }
                    otherRequestDetails.setStatus(paymentStatus);
                }
            }


            if (additionalPayment.getApprovers() != null) {
                ArrayList<ApproversTO> approvers = new ArrayList<>();
                additionalPayment.getApprovers().forEach(approver -> {
                    if (approver.getExactEmployee() != null) {
                        EdsUser employeeApprover = userManager.get(approver.getExactEmployee().getId());
                        if (employeeApprover != null) {
                            ApproversTO approversTO = new ApproversTO();
                            approversTO.setId(employeeApprover.getObjectID());
                            approversTO.setName(employeeApprover.getName());
                            if (employeeApprover.getPhoto() != null) {
                                approversTO.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employeeApprover.getObjectID()));
                            }
                            EdsEmployee employee = employeeApprover.isEmployee() ? employeeApprover.getEmployee() : null;
                            if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                                approversTO.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                            }
                            if (approver.getStatus() != null) {
                                if (Constants.PAYMENT_STATUS_APPROVED.equalsIgnoreCase(approver.getStatus().getCode())) {
                                    approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.APPROVED.getStatus()));
                                } else if (Constants.PAYMENT_STATUS_REJECTED.equalsIgnoreCase(approver.getStatus().getCode())) {
                                    approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.DECLINED.getStatus()));
                                } else {
                                    approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                                }
                            } else {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                            }
                            approvers.add(approversTO);
                        }
                    }
                });
                otherRequestDetails.setApprovers(approvers);
            }

            otherRequestDetails.setUser_actions(getUserAction(additionalPayment, user));

        } else if (OtherRequestEnum.GOAL.getStatus().equalsIgnoreCase(type)) {
            GoalItem goalItem;
            try {
                goalItem = hrmsServiceLocal.editGoal(request_id, Constants.PERSONAL_GOAL);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (goalItem.getObjectId() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Goal Item has not been found with provided request_id", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            otherRequestDetails.setId(goalItem.getObjectId());
            if (goalItem.getGoalAssigneeItem() != null) {
                goalItem.getGoalAssigneeItem();
                for (GoalAssigneeItem assigneeItem : goalItem.getGoalAssigneeItem()) {
                    if (assigneeItem.isAssignee() && !user.getObjectID().equals(assigneeItem.getId())) {
                        OwnerTO owner = new OwnerTO();
                        owner.setId(assigneeItem.getId());
                        owner.setName(assigneeItem.getName());
                        try {
                            owner.setAvatar(hrmsServiceLocal.getEmployeeImageURL(assigneeItem.getId()));
                        } catch (Exception e) {
                            log.error("", e);
                        }
                        owner.setDepartment(assigneeItem.getDepartmentName());
                        otherRequestDetails.setOwner(owner);
                    }
                }
            }
            otherRequestDetails.setTitle(goalItem.getTitle());
            if (StringUtils.isNotBlank(goalItem.getDescription())) {
                otherRequestDetails.setDescription(goalItem.getDescription());
            }
            otherRequestDetails.setStatus(new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(),
                    goalItem.getStatus() != null && StringUtils.isNotBlank(goalItem.getStatus()) ? new TitleTO(goalItem.getStatus()) : null));

            ArrayList<CustomFieldsTO> customFields = getCustomFields(goalItem.getCustomFields());
            if (customFields != null && customFields.size() > 0) {
                otherRequestDetails.setCustom_fields(customFields);
            }
        } else if (OtherRequestEnum.INCIDENT.getStatus().equalsIgnoreCase(type)) {
            PerformanceNoteItem performanceNoteItem;
            try {
                performanceNoteItem = hrmsServiceLocal.getPerformanceNote(request_id);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (performanceNoteItem.getObjectID() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Incident Item has not been found with provided request_id", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            otherRequestDetails.setId(performanceNoteItem.getObjectID());
            if (performanceNoteItem.getResolverID() != null && !user.getObjectID().equals(performanceNoteItem.getResolverID())) {
                EdsEmployee employee = employeeManager.get(performanceNoteItem.getResolverID());
                if (employee != null) {
                    OwnerTO owner = new OwnerTO();
                    owner.setId(employee.getObjectID());
                    owner.setName(employee.getName());
                    try {
                        owner.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employee.getObjectID()));
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                        owner.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                    }
                    otherRequestDetails.setOwner(owner);
                }
            }
            otherRequestDetails.setTitle(performanceNoteItem.getName());
            if (StringUtils.isNotBlank(performanceNoteItem.getDescription())) {
                otherRequestDetails.setDescription(performanceNoteItem.getDescription());
            }
            otherRequestDetails.setStatus(new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(),
                    performanceNoteItem.getStatusID() != null && StringUtils.isNotBlank(performanceNoteItem.getStatusName()) ? new TitleTO(performanceNoteItem.getStatusName()) : null));

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_type should be one of ADDITIONAL_PAYMENT, GOAL, INCIDENT", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return successResponse(otherRequestDetails);
    }

    @Operation(summary = "Other Request Action", description = "Approves or rejects current request based on request_type, request_id and action \n" +
            "Action can be APPROVE, APPROVE_FOR_ALL, REJECT")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message"),
            @ApiResponse(responseCode = "400", description = "Request id is required"),
            @ApiResponse(responseCode = "400", description = "Request type is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @RequestMapping(value = "/requests/other_request", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object otherRequestAction(@RequestBody OtherActionRequestTO otherRequestAction) throws RestException {
        if (StringUtils.isBlank(otherRequestAction.getType())) {
            throw new RestException(ERROR_MESSAGE, "Request type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!Constants.ADDITIONAL_PAYMENT.equals(otherRequestAction.getType())) {
            throw new RestException(ERROR_MESSAGE, "Invalid request type", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (otherRequestAction.getRequest_id() == null || otherRequestAction.getRequest_id() <= 0) {
            throw new RestException(ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(otherRequestAction.getAction())) {
            throw new RestException(ERROR_MESSAGE, "Request action is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        String action = getRequestAction(otherRequestAction.getAction());
        if (StringUtils.isBlank(action)) {
            throw new RestException(ERROR_MESSAGE, "Request action should be one of APPROVE, REJECT, APPROVE_FOR_ALL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(otherRequestAction.getRequest_id());

        AdditionalPayment additionalPayment;
        try {
            additionalPayment = payrollServiceLocal.getAdditionalPaymentData(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (additionalPayment == null || additionalPayment.getObjectID() == null) {
            throw new RestException(ERROR_MESSAGE, "Additional payment with id " + otherRequestAction.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String status = null;
        RequestUserActionTO userAction = getUserAction(additionalPayment, userManager.getUser());
        if (RequestActionEnum.APPROVE.name().equals(action)) {
            if (userAction.isApprove()) {
                status = Constants.PAYMENT_STATUS_APPROVED;
            } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected additional payment cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Additional payment has already been approved", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.REJECT.name().equals(action)) {
            if (userAction.isReject()) {
                status = Constants.PAYMENT_STATUS_REJECTED;
            } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Additional payment has already been rejected", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Approved additional payment cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.APPROVE_FOR_ALL.name().equals(action)) {
            if (userAction.isApprove_for_all()) {
                status = Constants.PAYMENT_STATUS_APPROVED;
            } else if (Constants.PAYMENT_STATUS_APPROVED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Additional payment has already been approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.PAYMENT_STATUS_REJECTED.equals(additionalPayment.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected additional payment cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            }
        }
        additionalPayment.setStatusCode(status);
        try {
            payrollServiceLocal.saveAdditionalPayment(additionalPayment);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    private RequestUserActionTO getUserAction(AdditionalPayment additionalPayment, EdsUser user) {
        boolean canApprove = user.hasRole(EdsRole.ADMIN_CODE) || user.hasRole(EdsRole.DR_CODE);
        RequestUserActionTO userAction = new RequestUserActionTO();
        if (Constants.PAYMENT_STATUS_APPROVED.equalsIgnoreCase(additionalPayment.getOverallStatus().getCode()) || Constants.PAYMENT_STATUS_REJECTED.equalsIgnoreCase(additionalPayment.getOverallStatus().getCode())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        if (canApprove) {
            userAction.setApprove_for_all(true);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        if (user.getObjectID().equals(additionalPayment.getApprover().getId())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        if (!user.getObjectID().equals(additionalPayment.getApprover().getId())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        return userAction;
    }

    /*private RequestStatusEnum getRFPStatus(String kpiStatus) {
        if(StringUtils.isBlank(kpiStatus)) {
            return RequestStatusEnum.PENDING;
        }
        if(kpiStatus.equalsIgnoreCase(EdsRFP.APPROVE)) {
            return RequestStatusEnum.APPROVED;
        } else if(kpiStatus.equalsIgnoreCase(EdsRFP.REJECT)) {
            return RequestStatusEnum.DECLINED;
        } else if(kpiStatus.equalsIgnoreCase(EdsRFP.DRAFT)) {
            return RequestStatusEnum.PENDING;
        } else {
            return RequestStatusEnum.PARTIALLY_APPROVED;
        }
    }*/

    /*private RequestStatusEnum getSaleQuoteStatus(String kpiStatus) {
        if(StringUtils.isBlank(kpiStatus)) {
            return RequestStatusEnum.PENDING;
        }
        if(kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.APPROVE.getStatus()) || kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.PAID.getStatus())
                || kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.INVOICED.getStatus())) {
            return RequestStatusEnum.APPROVED;
        } else if(kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.DRAFT.getStatus()) || kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.OPEN.getStatus())
                || kpiStatus.equalsIgnoreCase(InvoiceStatusEnum.OVER_DUE.getStatus())) {
            return RequestStatusEnum.PENDING;
        } else {
            //
            return RequestStatusEnum.PARTIALLY_APPROVED;
        }
    }*/
}
