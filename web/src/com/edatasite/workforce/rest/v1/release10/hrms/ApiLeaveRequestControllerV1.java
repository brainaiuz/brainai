package com.edatasite.workforce.rest.v1.release10.hrms;

import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestComment;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.CommentTO;
import com.edatasite.workforce.rest.base.to.LeaveRequestTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.UserTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilshod Madrahimov.
 */

@Tag(name = "Leave Request", description = "Leave Request API")
@RestController
@RequestMapping(value = "/leaveRequest", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiLeaveRequestControllerV1 extends BaseApiControllerV1 {
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter filterParameter) {
        ListResult<LeaveRequestLisItem> leaveRequestList = availabilityServiceLocal.getLeaveRequestList(filterParameter.convertToFilterParameters());
        ArrayList<LeaveRequestTO> leaveRequestTOs = new ArrayList<>();
        if (leaveRequestList != null) {
            for (LeaveRequestLisItem leaveRequest : leaveRequestList.getList()) {
                leaveRequestTOs.add(new LeaveRequestTO(leaveRequest, true));
            }
        }
        return successResponse(new ListResultTO<>(leaveRequestList.getTotal(), leaveRequestTOs));
    }

    @RequestMapping(value = "/listCount", method = RequestMethod.GET)
    public Object getListCount(@RequestBody MListingFilterParameter filterParameter) {
        return successResponse(availabilityServiceLocal.getLeaveRequestListCount(filterParameter.convertToFilterParameters()));
    }

    @RequestMapping(value = "/approversCount", method = RequestMethod.GET)
    public Object getApproversCount() {
        return successResponse(getChooseApprovers(RelationItem.TYPE_LEAVE_REQUEST));
    }

    @RequestMapping(value = "/{employeeId}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        StatisticsLeaveRequest item = availabilityServiceLocal.getLeaveRequest(id);
        if (item == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        LeaveRequestTO leaveRequestTO = new LeaveRequestTO();
        leaveRequestTO.setId(item.getObjectID());
        leaveRequestTO.setDescription(item.getDescription());
        leaveRequestTO.setReason(new SelectItemTO(item.getReasonId(), item.getReason()));
        leaveRequestTO.setType(new SelectItemTO(item.getTypeId(), item.getType()));

        leaveRequestTO.setFromDate(WrapUtils.dateToLong(item.getStartDDate()));
        leaveRequestTO.setStartHour((long) item.getStartDDate().getNonConvertedDate().getHours());
        leaveRequestTO.setStartMinut((long) item.getStartDDate().getNonConvertedDate().getMinutes());

        leaveRequestTO.setToDate(WrapUtils.dateToLong(item.getEndDDate()));
        leaveRequestTO.setEndHour((long) item.getEndDDate().getNonConvertedDate().getHours());
        leaveRequestTO.setEndMinut((long) item.getEndDDate().getNonConvertedDate().getMinutes());

        leaveRequestTO.setDuration(item.getDuration());
        leaveRequestTO.setEmployee(new UserTO(item.getEmployeeId(), item.getEmployee()));
        leaveRequestTO.setDepartment(item.getDepartment());
        leaveRequestTO.setTakeByMoney(item.getTakeByMoney());

        if (item.getOverallStatus() != null) {
            leaveRequestTO.setOverallStatus(new SelectItemTO(item.getOverallStatus().getId(), item.getOverallStatus().getName(), item.getOverallStatus().getDescription(), ""));
        }

        if (item.getApprovers() != null && item.getApprovers().size() > 0) {
            ArrayList<UserTO> approvers = new ArrayList<>();
            for (ApproverItemMini approverItem : item.getApprovers()) {
                approvers.add(new UserTO(approverItem.getExactEmployee().getId(), approverItem.getExactEmployee().getName()));
            }
            leaveRequestTO.setApprovers(approvers);
        }
        return successResponse(leaveRequestTO);
    }

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object create(@PathVariable(value = "employeeId") Integer employeeId, @RequestBody LeaveRequestTO leaveRequestTO) {
        NewLeaveRequest newLeaveRequest = new NewLeaveRequest();
        newLeaveRequest.setEmployee(employeeId);

        if (leaveRequestTO.getReason() != null) {
            newLeaveRequest.setReasonId(leaveRequestTO.getReason().getId());
        }
//        newLeaveRequest.setOtherReason(leaveRequestTO.getOtherReason());
        newLeaveRequest.setDescription(leaveRequestTO.getDescription());
        if (leaveRequestTO.getType() != null) {
            newLeaveRequest.setType(leaveRequestTO.getType().getName());
        }
        newLeaveRequest.setEmployeeIds(leaveRequestTO.getEmployeeList());
        newLeaveRequest.setTypeBoolean(leaveRequestTO.getTypeBoolean());
        newLeaveRequest.setTakeByMoney(leaveRequestTO.getTakeByMoney());

        newLeaveRequest.setStartHour(leaveRequestTO.getStartHour().intValue());
        newLeaveRequest.setStartMinut(leaveRequestTO.getStartMinut().intValue());

        newLeaveRequest.setEndHour(leaveRequestTO.getEndHour().intValue());
        newLeaveRequest.setEndMinut(leaveRequestTO.getEndMinut().intValue());

        Long startD = leaveRequestTO.getFromDate() + (leaveRequestTO.getStartHour() * 60 + leaveRequestTO.getStartMinut()) * 60 * 1000;
        DateNonConvertable start = new DateNonConvertable(new Date(startD));
        if (start.getDate() != null) {
            newLeaveRequest.setDate((String.valueOf(start.getDate().getTime())));
        }
        newLeaveRequest.setDay(String.valueOf(WrapUtils.longToDate(leaveRequestTO.getFromDate()).getDate()));
        newLeaveRequest.setMonth(String.valueOf(WrapUtils.longToDate(leaveRequestTO.getFromDate()).getMonth()));

        Long endD = leaveRequestTO.getToDate() + (leaveRequestTO.getEndHour() * 60 + leaveRequestTO.getEndMinut()) * 60 * 1000;
        DateNonConvertable end = new DateNonConvertable(new Date(endD));
        if (end.getDate() != null) {
            newLeaveRequest.setDateE((String.valueOf(end.getDate().getTime())));
        }
        newLeaveRequest.setDayE(String.valueOf(WrapUtils.longToDate(leaveRequestTO.getToDate()).getDate()));
        newLeaveRequest.setMonthE(String.valueOf(WrapUtils.longToDate(leaveRequestTO.getToDate()).getMonth()));


        if (leaveRequestTO.getEmployeeList() != null) {
            newLeaveRequest.setEmployeeIds(leaveRequestTO.getEmployeeList());
        }
        if (leaveRequestTO.getApprovers() != null && leaveRequestTO.getApprovers().size() > 0) {
            newLeaveRequest.setApprovers(getChosenApprovers(leaveRequestTO.getApprovers(), getChooseApprovers(RelationItem.TYPE_LEAVE_REQUEST)));
        }
        try {
            availabilityServiceLocal.createLeaveRequest(newLeaveRequest);
            return this.successResponse(SUCCESS_SAVE);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_SAVE);
        }

    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public Object getEmployeeList() {
        return successResponse(WrapUtils.wrapUserTOs(availabilityServiceLocal.getCompanyEmployeesAsAdmin()));
    }

    @RequestMapping(value = "/approvers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getApproverList(@RequestBody MListingFilterParameter filterParameter) {
        if (filterParameter == null) {
            filterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = filterParameter.convertToFilterParameters();
        fp.setListEmployees(true);
        fp.setApproverID(fp.getObjectId());
        List<UserTO> result = WrapUtils.wrapUserTOs(allInOneServiceLocal.getEmployeesAsSelectItem(new ListLoadConfig(), fp), true);
        ListLoadConfig config = new ListLoadConfig(fp);
        if (result.size() > config.getStart()) {
            result = ListUtils.getSublist(result, config.getStart(), config.getLimit());
        } else {
            result = new ArrayList<>();
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/reasons", method = RequestMethod.GET)
    public Object getReasonList() {
        return successResponse(WrapUtils.wrapSelectItemTOs(availabilityServiceLocal.getReasons(null)));
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public Object getTypeList() {
        List<SelectItem> types = availabilityServiceLocal.getLeaveRequrestTypes();
        return successResponse(WrapUtils.wrapSelectItemTOs(types.toArray(new SelectItem[0])));
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public Object getStatusList() {
        SelectItem[] types = availabilityServiceLocal.getSickStatusList();
        return successResponse(WrapUtils.wrapSelectItemTOs(types));
    }

    @RequestMapping(value = "/assignees", method = RequestMethod.GET)
    public Object getAssigneeList() {
        return getEmployeeList();
    }

    @RequestMapping(value = "/comment/{leaveRequestId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addComment(@PathVariable(value = "leaveRequestId") Integer leaveRequestId, @RequestParam(value = "content") String content) {
        availabilityServiceLocal.setComment(leaveRequestId, content);
        LeaveRequestComment[] comments = availabilityServiceLocal.getComments(leaveRequestId);
        if (comments == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        List<CommentTO> commentTOs = new ArrayList<>();
        for (LeaveRequestComment requestComment : comments) {
            CommentTO commentTO = new CommentTO();
            commentTO.setId(requestComment.getObjectID());
            commentTO.setCreationDate(WrapUtils.dateToLong(requestComment.getCreationDate()));
            commentTO.setMessage(requestComment.getText());
            UserTO userTO = new UserTO();
            userTO.setName(requestComment.getUser());
            commentTO.setUser(userTO);
            commentTOs.add(commentTO);
        }
        return successResponse(commentTOs);
    }

    @RequestMapping(value = "/comment/{commentId}", method = RequestMethod.DELETE)
    public Object deleteComment(@PathVariable(value = "commentId") Integer commentId) {
        try {
            availabilityServiceLocal.deleteLeaveRequestComment(commentId);
            return this.successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        try {
            availabilityServiceLocal.deleteRequest(id);
            return this.successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/{id}/{actionType}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object doAction(@PathVariable(value = "id") Integer id, @PathVariable(value = "actionType") String actionType, @RequestBody MListingFilterParameter mListingFilterParameter) {
        ApiActionEnum actionTypeEnum = getActionType(actionType);
        if (actionTypeEnum == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        String approveType;
        if (ApiActionEnum.APPROVE.getCode().equalsIgnoreCase(actionType)) {
            approveType = Constants.LR_STATUS_SS_APPROVED;
        } else if (ApiActionEnum.REJECT.getCode().equalsIgnoreCase(actionType)) {
            approveType = Constants.LR_STATUS_SS_DENIED;
        } else {
            return this.errorResponse("Action type should be approve or reject");
        }
        String type = mListingFilterParameter.isPaid() ? EdsSickRequest.PAID : EdsSickRequest.NON_PAID;

        try {
            availabilityServiceLocal.updateApprove(approveType, id, false);
            return this.successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    private String getAsDateAndTime(String lRequest) {
        if (lRequest == null || lRequest.isEmpty()) {
            return "";
        }
        double days = Double.valueOf(lRequest.split("\\|\\|")[0]);
        double hours = Double.valueOf(lRequest.split("\\|\\|")[1]);
        String dayString = days > 1 ? "Days" : "Day";
        String hourString = hours > 1 ? "Hours" : "Hour";
        return lRequest.split("\\|\\|")[1] + " " + hourString + " (" + lRequest.split("\\|\\|")[0] + " " + dayString + ")";
    }

}
