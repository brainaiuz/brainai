package com.edatasite.workforce.core.domain.enums;

/**
 * Created by dilsh0d on 26.08.15.
 */
public enum DeviceTypeEnum {
    Browser,
    Android,
    IPhone;

    public static boolean isMobile(DeviceTypeEnum device){
        return DeviceTypeEnum.Android.equals(device) || DeviceTypeEnum.IPhone.equals(device);
    }
}
