package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.NotificationTO;
import com.edatasite.workforce.rest.base.to.UserTO;
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
@Tag(name = "Notification", description = "Notification API")
@RestController
@RequestMapping(value = "/notification", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiNotificationControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;
    @Autowired
    private NotificationMsgManager notificationMsgManager;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter filterParameter) {
        ArrayList<NotificationTO> result = new ArrayList<>();
        ListResult<NotificationItem> notificationListResult = notificationMsgServiceLocal.getNotificationsList(filterParameter.convertToFilterParameters());
        for (NotificationItem item : notificationListResult.getList()) {
            NotificationTO notificationTO = new NotificationTO();
            notificationTO.setId(item.getId());
            notificationTO.setName(item.getName());
            notificationTO.setDate(WrapUtils.dateToLong(item.getDate()));
            notificationTO.setRead(item.isRead());
            UserTO userTO = new UserTO(item.getActorUserId(), item.getActorUserName());
            notificationTO.setUser(userTO);
            result.add(notificationTO);
        }
        return successResponse(new ListResultTO<>(notificationListResult.getTotal(), result));
    }

    @RequestMapping(value = "/listCount", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getListCount(MListingFilterParameter filterParameter) {
        filterParameter.setSearchKey(null);
        return successResponse(notificationMsgManager.getListTotal(filterParameter.convertToFilterParameters(), null).intValue());
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Object getCategoryList() {
        return successResponse(notificationMsgServiceLocal.getCategoriesList(true));
    }

    @RequestMapping(value = "/{id}/read", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object markAsRead(@PathVariable(value = "id") Integer id) {
        try {
            notificationMsgServiceLocal.updateClicked(id);
            return successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object clearAll() {
        try {
            notificationMsgServiceLocal.clearAll();
            return successResponse(SUCCESS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }
    }

    @RequestMapping(value = "/count", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getNotificationCountByTypes(@RequestBody MListingFilterParameter filterParameter) {
        return successResponse(notificationMsgServiceLocal.getNotificationCountByTypes(filterParameter.convertToFilterParameters()));
    }


}
