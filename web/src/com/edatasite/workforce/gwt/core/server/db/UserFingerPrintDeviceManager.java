package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;

import java.util.List;
import java.util.Map;

/**
 * Created by Muhammad on 21.04.2016.
 */
public interface UserFingerPrintDeviceManager extends Manager<EdsUserFingerPrintDevice> {

    EdsUserFingerPrintDevice getUserFingerPrintdByUserIdAndDeviceId(Integer userId, String... deviceId);

    Map<String, EdsUserFingerPrintDevice> getUserDeviceMapByDeviceId(String... deviceId);

    List<EdsUserFingerPrintDevice> getUserDeviceFingerPrintList();

    List<EdsUserFingerPrintDevice> getAllByUserIdAndDeviceIds(Integer userId, String... deviceIds);

    List<EdsUserFingerPrintDevice> getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId);

    List<EdsUserFingerPrintDevice> getAllByDeviceId(String deviceId);
}
