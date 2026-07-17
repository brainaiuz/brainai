package com.edatasite.workforce.rest.v1.release10.fingerprint;

import com.edatasite.workforce.core.domain.EdsFingerPrintDeviceStatusHistory;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.db.FingerPrintDeviceStatusHistoryManager;
import com.edatasite.workforce.rest.base.enums.DeviceStatusEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.DeviceStatusHistoryTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Dilsh0d on 10/26/2017.
 */
@Tag(name = "Device Status History", description = "Device Status History API")
@RestController
@RequestMapping(value = "/deviceStatusHistory",
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiFingerPrintDeviceStatusHistoryV1 extends BaseApiControllerV1 implements ApiConstants {

    private static final Logger log = LoggerFactory.getLogger(ApiFingerPrintDeviceStatusHistoryV1.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private FingerPrintDeviceStatusHistoryManager fingerPrintDeviceStatusHistoryManager;

    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = ACCESS_TOKEN, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @Transactional
    public Object add(@RequestBody DeviceStatusHistoryTO data) {

        if (!fingerPrintDeviceStatusHistoryManager.getUser().hasRole(EdsRole.ADMIN_CODE)) {
            return errorResponse("You should have Admin privileges");
        }

        if (StringUtils.isBlank(data.getDeviceUniqueKey())) {
            return errorResponse("Device unique key is mandatory");
        }
        if (StringUtils.isBlank(data.getDeviceStatus()) || DeviceStatusEnum.getStatus(data.getDeviceStatus()) == null) {
            return errorResponse("Device status must be ON or OFF");
        }
        if (StringUtils.isBlank(data.getStatusUpdateTime())) {
            return errorResponse("The specified device does not exist");
        }
        try {
            dateFormat.parse(data.getStatusUpdateTime());
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return errorResponse("Device update time format should be : " + dateFormat.toPattern());
        }

        CompanyDomain companyDomain = globalAuthJdbcSpringManager.getCompanyIdByUniqueKey(data.getDeviceUniqueKey());
        if (companyDomain == null || companyDomain.getCompanyID() == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        ServerSecurityContext.getInstance().setCompanyId(companyDomain.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(companyDomain.getCompanyID()));

        try {
            EdsFingerPrintDeviceStatusHistory edsFingerPrintDeviceStatusHistory = new EdsFingerPrintDeviceStatusHistory();
            edsFingerPrintDeviceStatusHistory.setDeviceUniqueKey(companyDomain.getCompanyUniqueID());
            edsFingerPrintDeviceStatusHistory.setDeviceName(companyDomain.getCompanyBranchName());
            edsFingerPrintDeviceStatusHistory.setDeviceStatus(DeviceStatusEnum.getStatus(data.getDeviceStatus()));
            edsFingerPrintDeviceStatusHistory.setStatusUpdateTime(dateFormat.parse(data.getStatusUpdateTime()));
            edsFingerPrintDeviceStatusHistory.setDescription(data.getDescription());
            fingerPrintDeviceStatusHistoryManager.create(edsFingerPrintDeviceStatusHistory);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return errorResponse(e);
        }
        return successResponse("Finger print device(" + data.getDeviceUniqueKey() + ") status history has been saved successfully.", SUCCESS_SAVE, RESPONSE_SUCCESS_CODE);
    }
}
