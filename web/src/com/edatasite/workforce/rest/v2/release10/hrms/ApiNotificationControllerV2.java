package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSendItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.NotificationResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.NotificationsListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.SendSMSTO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Tag(name = "Notifications", description = "Notifications API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiNotificationControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiNotificationControllerV2.class);

    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CRMService crmService;

    protected static ArrayList<NotificationTypeEnum> getEntityTypes(String moduleCode) {
        if (StringUtils.isBlank(moduleCode) || PermissionConstants.ALL.equals(moduleCode) || PermissionConstants.DOCUMENTS_CONTEXT.equals(moduleCode)) {
            return null;//it means notifications in all sections should be retrieve
        }
        ArrayList<NotificationTypeEnum> entityList = new ArrayList<>();
        if (PermissionConstants.HRMS_MODULE.equals(moduleCode)) {
            entityList.add(NotificationTypeEnum.BenefitRequest);
            entityList.add(NotificationTypeEnum.LeaveRequests);
            entityList.add(NotificationTypeEnum.TimeSheetDueReminder);
            entityList.add(NotificationTypeEnum.TaskAssignee);
            entityList.add(NotificationTypeEnum.TaskDueReminder);
            entityList.add(NotificationTypeEnum.ExpenseClaim);
            entityList.add(NotificationTypeEnum.MeetingMinutesNotification);
            entityList.add(NotificationTypeEnum.ProjectDueReminder);
            entityList.add(NotificationTypeEnum.WorkstreamDueReminder);
            entityList.add(NotificationTypeEnum.Employee);
            entityList.add(NotificationTypeEnum.ProjectApproval);
            entityList.add(NotificationTypeEnum.Candidate);
            entityList.add(NotificationTypeEnum.CashAdvance);
            entityList.add(NotificationTypeEnum.OnboardingStep);
            entityList.add(NotificationTypeEnum.TimeSheetApproval);
            entityList.add(NotificationTypeEnum.GoogleCalendarEvent);
            entityList.add(NotificationTypeEnum.InvoiceDueReminder);
            return entityList;
        }
        if (PermissionConstants.CRM_MODULE.equals(moduleCode)) {
            entityList.add(NotificationTypeEnum.CRMCase);
            entityList.add(NotificationTypeEnum.LeadAssignee);
            entityList.add(NotificationTypeEnum.SaleInvoiceApproval);
            entityList.add(NotificationTypeEnum.CRMContact);
            entityList.add(NotificationTypeEnum.PurchaseOrder);
            entityList.add(NotificationTypeEnum.CrmOpportunity);
            entityList.add(NotificationTypeEnum.SalesQuote);
            return entityList;
        }

        return null;
    }

    @Operation(summary = "Get Notification List", description = "Retrieves all notification based on HRMS or CRM token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of HRMS notifications if HRMS access-token is provided. \n" +
            "If CRM access token is provided, all CRM notifications will be returned"),
            @ApiResponse(responseCode = "422", description = "Invalid Access Token")})
    @RequestMapping(value = "/notifications", method = RequestMethod.GET, headers = "Accept=application/json")
    public Object getNotificationsList(@RequestParam(value = "limit", required = false) Integer limit,
                                       @RequestParam(value = "offset", required = false) Integer offset,
                                       @RequestParam(value = "isRead", required = false) Boolean isRead) throws RestException {

        String accessToken = httpServletRequest.getHeader(ApiConstants.ACCESS_TOKEN);
        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

        ListingFilterParameter filterParameter = new ListingFilterParameter();

        if (isRead != null && !isRead) {
            filterParameter.setActive(true);
        }else {
            filterParameter.setActive(false);
        }
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        ListResult<NotificationItem> result;
        try {
            result = notificationMsgServiceLocal.getNotificationsList(filterParameter, getEntityTypes(apiAccessToken.getModuleCode()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        NotificationResultTO notificationResult = new NotificationResultTO();
        notificationResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            notificationResult.setLeft(0);
        } else {
            notificationResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        notificationResult.setCount(result.getList() != null ? result.getList().size() : 0);
        notificationResult.setOffset(start);

        ArrayList<NotificationsListTO> notifications = new ArrayList<>();

        result.getList().forEach(notificationItem -> {
            NotificationsListTO notification = new NotificationsListTO();
            notification.setId(notificationItem.getId());
            notification.setTitle(notificationItem.getName());
            if (StringUtils.isNotBlank(notificationItem.getActorUserName())) {
                notification.setContent(notificationItem.getActorUserName().concat(notificationItem.getUserInfo()));
            } else {
                notification.setContent(notificationItem.getUserInfo());
            }
            notification.setDepartment(notificationItem.getModuleName());
            if (notificationItem.getDate() != null) {
                notification.setDue_date(longDateTimezoneFormat.format(notificationItem.getDate()));
            }
            notification.setIcon(notificationItem.getActorUserImg());
            notification.setIs_new(!notificationItem.isRead());
            notifications.add(notification);
        });

        notificationResult.setNotifications(notifications);

        return successResponse(notificationResult);
    }

    @Operation(summary = "Read All Notifications", description = "Makes all notifications viewed")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/notifications/read_all", method = RequestMethod.POST)
    public Object readAllNotifications() throws RestException {

        String accessToken = httpServletRequest.getHeader(ApiConstants.ACCESS_TOKEN);
        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);

        notificationMsgServiceLocal.clearAll(getEntityTypes(apiAccessToken.getModuleCode()));
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Read Notification", description = "Makes particular notification viewed")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/notifications/read", method = RequestMethod.POST)
    public Object readNotification(@RequestParam("notification_id") Integer notification_id) throws RestException {
        if (notification_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "notification_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        notificationMsgServiceLocal.updateClicked(notification_id);
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Send SMS", description = "Sends sms to the provided number")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with appropriate error message "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/notifications/sendSMS", method = RequestMethod.POST)
    public Object sendSMS(@RequestBody SendSMSTO smsto) throws RestException {
        if (smsto.getNumber() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "phone number is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (smsto.getMessage() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "message is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (smsto.getProvider_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "provider_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        SmsSendItem smsSendItem = new SmsSendItem();
        smsSendItem.setToNumber(smsto.getNumber());
        smsSendItem.setMessageText(smsto.getMessage());
        smsSendItem.setSettingID(smsto.getProvider_id());

        try {
            crmService.smsSendTo(smsSendItem);
            return successResponse(new ApiResult());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
