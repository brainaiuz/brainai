package com.workforcetrack.api.controllers;

import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.domain.notificationmsg.EdsNotificationMessage;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.mobile.services.ApnsSenderWebService;
import com.workforcetrack.mobile.services.GcmSenderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by dilsh0d on 15.09.15.
 */
@Controller
@RequestMapping(value = "/notification")
public class NotificationApiController {

    @Autowired
    private ApnsSenderWebService apnsSenderWebService;
    @Autowired
    private GcmSenderWebService gcmSenderWebService;
    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;

    @RequestMapping(value = "/send", method = RequestMethod.POST,
            headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest(checkSession = false)
    @ResponseBody
    public Object send(@RequestBody Map<String, Object> params) throws BaseApiException {
        String deviceType = (String) params.get("deviceType"); // Please look at the class com.edatasite.workforce.core.domain.enums.DeviceTypeEnum
        String deviceToken = (String) params.get("deviceToken");

        EdsNotificationMessage edsNotificationMessage = new EdsNotificationMessage();
        edsNotificationMessage.setObjectID(0);
        edsNotificationMessage.setEntityID(1);
        edsNotificationMessage.setViewerUserID(1);// For test user ID viewer  May be change online userID
        edsNotificationMessage.setActorUserID(2);// For test user ID viewer May be change online userID
        edsNotificationMessage.setEntityType(NotificationTypeEnum.LeaveRequests);
        edsNotificationMessage.setActionOnEntity(ActionOnEntityEnum.WAIT_APPROVAL);
        edsNotificationMessage.setDate(new Date());
        if (DeviceTypeEnum.Android.equals(DeviceTypeEnum.valueOf(deviceType))) {
            gcmSenderWebService.sendMessage(deviceToken, edsNotificationMessage);
        } else if (DeviceTypeEnum.IPhone.equals(DeviceTypeEnum.valueOf(deviceType))) {
            apnsSenderWebService.sendMessage(deviceToken, edsNotificationMessage);
        } else {
            System.out.println(" --------------------------------------------------------------------------------------------");
            System.out.println("|                                                                                            |");
            System.out.println("|                                                                                            |");
            System.out.println("|------------------------------ DEVICE TYPE NULL OR EMPTY -----------------------------------|");
            System.out.println("|                                                                                            |");
            System.out.println("|                                                                                            |");
            System.out.println(" --------------------------------------------------------------------------------------------");
        }
        return "SENT";
    }

    @RequestMapping(value = "/updateclicked", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object updateClickedNotification(@RequestBody Map<String, Object> params) throws BaseApiException {
        String id = (String) params.get("id");
        try {
            if (id != null && !"".equals(id)) {
                notificationMsgServiceLocal.updateClicked(Integer.valueOf(id));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object setDeviceToken(@RequestBody Map<String, Object> params) throws BaseApiException {
        String deviceToken = (String) params.get("deviceToken");
        String deviceType = (String) params.get("deviceType");
        notificationMsgServiceLocal.updateUserToken(deviceToken, deviceType);
        return "success";
    }
}
