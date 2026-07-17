package com.edatasite.workforce.rest.v3.release10.attendance;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.UserFingerPrintAdjustmentService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.attendance.dto.UserFingerprintDeviceDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "User Fingerprint Device", description = "User Fingerprint Device API")
@RestController
@RequestMapping(value = "/user-fingerprint-device", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiUserFingerprintDeviceControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiUserFingerprintDeviceControllerV3.class);
    private final HrmsService hrmsService;
    private final UserFingerPrintAdjustmentService userFingerPrintAdjustmentService;

    public ApiUserFingerprintDeviceControllerV3(HrmsService hrmsService,
                                                UserFingerPrintAdjustmentService userFingerPrintAdjustmentService) {
        this.hrmsService = hrmsService;
        this.userFingerPrintAdjustmentService = userFingerPrintAdjustmentService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> createUserFingerprintDevice(@RequestBody UserFingerprintDeviceDTO request) {
        log.info("REST request to Create User Fingerprint Device: {}", request);
        String key = request.getUserId() + "__" + request.getDeviceId();
        String value = request.getFingerprintId();
        hrmsService.saveUserFingerPrintDeviceData(new HashMap<>(Map.of(key, value)));
        return ResultTO.success();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<UserFingerPrintDeviceItem>> getUserFingerprintDevice(@RequestParam(required = false) String deviceUuid,
                                                                              @RequestParam(required = false) String fingerprintId,
                                                                              @RequestParam(required = false) Integer userId) {
        log.info("REST request to Get User Fingerprint Device: {} by fingerprint id: {}", deviceUuid, fingerprintId);
        List<UserFingerPrintDeviceItem> userFingerPrintDeviceItems;
        if (deviceUuid != null && fingerprintId != null) {
            userFingerPrintDeviceItems = userFingerPrintAdjustmentService.getAllByDeviceIdAndFingerprintId(deviceUuid, fingerprintId);
        } else if (deviceUuid != null) {
            userFingerPrintDeviceItems = userFingerPrintAdjustmentService.getAllByDeviceId(deviceUuid);
        } else if (userId != null) {
            userFingerPrintDeviceItems = userFingerPrintAdjustmentService.getFingerprintDevices(userId);
        } else {
            userFingerPrintDeviceItems = List.of();
        }
        return ResultTO.success(userFingerPrintDeviceItems);
    }
}
