package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintDeviceManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Muhammad on 21.04.2016.
 */
@Repository("userfingerprintdevice")
public class UserFingerPrintDeviceManagerImpl extends BaseManager<EdsUserFingerPrintDevice> implements UserFingerPrintDeviceManager {

    public UserFingerPrintDeviceManagerImpl() {
        super(EdsUserFingerPrintDevice.class);
    }

    @Override
    public EdsUserFingerPrintDevice getUserFingerPrintdByUserIdAndDeviceId(Integer userId, String... deviceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fp from EdsUserFingerPrintDevice fp where fp.userId = ? ");
        if (deviceId != null && deviceId.length > 0) {
            sql.append("and fp.deviceId = '").append(deviceId[0]).append("'");
        }
        return (EdsUserFingerPrintDevice) findSingle(sql.toString(), userId);
    }

    @Override
    public Map<String, EdsUserFingerPrintDevice> getUserDeviceMapByDeviceId(String... deviceId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fp from EdsUserFingerPrintDevice fp  ");
        sql.append("where fp.user.deleted <> true ");
        if (deviceId != null && deviceId.length > 0) {
            sql.append(" and fp.deviceId = '").append(deviceId[0]).append("'");
        }
        List<EdsUserFingerPrintDevice> userFingerPrintDevices = find(sql.toString());
        return userFingerPrintDevices.stream().collect(Collectors.toMap(userDevice -> ((EdsEmployee) userDevice.getUser()).getProfile().getEmployeeCode(), userDevice -> userDevice, (x1, x2) -> x1));
    }

    @Override
    public List<EdsUserFingerPrintDevice> getUserDeviceFingerPrintList() {
        return find("select fpd from EdsUserFingerPrintDevice fpd");
    }

    @Override
    public List<EdsUserFingerPrintDevice> getAllByUserIdAndDeviceIds(Integer userId, String... deviceIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fpd from EdsUserFingerPrintDevice fpd where fpd.userId = ? ");
        if (deviceIds != null && deviceIds.length > 0) {
            String deviceUuids = "(" + String.join(",", deviceIds) + ")";
            sql.append("and fpd.deviceId in ").append(deviceUuids);
        }
        return find(sql.toString(), userId);
    }

    @Override
    public List<EdsUserFingerPrintDevice> getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId) {
        String sql = """
                select fpd from EdsUserFingerPrintDevice fpd
                where fpd.deviceId = ?
                    and fpd.fingerprintId = ?""";
        return find(sql, deviceUuid, fingerprintId);
    }

    @Override
    public List<EdsUserFingerPrintDevice> getAllByDeviceId(String deviceId) {
        String sql = """
                select fpd from EdsUserFingerPrintDevice fpd
                join fetch fpd.user u
                where fpd.deviceId = ?
                and u.deleted <> true
                order by u.firstName, u.lastName""";
        return find(sql, deviceId);
    }
}
