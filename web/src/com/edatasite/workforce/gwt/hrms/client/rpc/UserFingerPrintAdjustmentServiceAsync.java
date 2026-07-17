package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface UserFingerPrintAdjustmentServiceAsync {
    void getFingerprintDevices(Integer userId, AsyncCallback<List<UserFingerPrintDeviceItem>> async);

    void getFingerprintSetup(AsyncCallback<List<CompanyDomain>> async);

    void getFingerprintSetupByUserIdAndDeviceId(Integer userId, String deviceId, AsyncCallback<UserFingerPrintDeviceItem> async);

    void getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId, AsyncCallback<List<UserFingerPrintDeviceItem>> async);

    void getAllByDeviceId(String deviceId, AsyncCallback<List<UserFingerPrintDeviceItem>> async);

    void create(UserFingerPrintAdjustment item, AsyncCallback<Void> async);
}
