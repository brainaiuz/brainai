package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.rest.v3.release10.core.to.AttendanceDto;

import java.util.List;

public interface UserFingerPrintAdjustmentServiceLocal {
    void create(UserFingerPrintAdjustment item);

    List<UserFingerPrintDeviceItem> getFingerprintDevices(Integer userId);

    List<CompanyDomain> getFingerprintSetup();

    UserFingerPrintDeviceItem getFingerprintSetupByUserIdAndDeviceId(Integer userId, String deviceId);

    List<UserFingerPrintDeviceItem> getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId);

    List<UserFingerPrintDeviceItem> getAllByDeviceId(String deviceId);

    AttendanceDto getByAttendanceId(Integer attendanceId);
}
