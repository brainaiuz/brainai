package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.UserFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

public interface UserFingerPrintAdjustmentService extends RemoteService {
    void create(UserFingerPrintAdjustment item);

    List<UserFingerPrintDeviceItem> getFingerprintDevices(Integer userId);

    List<CompanyDomain> getFingerprintSetup();

    UserFingerPrintDeviceItem getFingerprintSetupByUserIdAndDeviceId(Integer userId, String deviceId);

    List<UserFingerPrintDeviceItem> getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId);

    List<UserFingerPrintDeviceItem> getAllByDeviceId(String deviceId);

    class App {
        public static UserFingerPrintAdjustmentServiceAsync get() {
            ServiceDefTarget target = GWT.create(UserFingerPrintAdjustmentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/userFingerPrintAdjustment");
            return (UserFingerPrintAdjustmentServiceAsync) target;
        }
    }
}
