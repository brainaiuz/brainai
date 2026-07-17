package com.edatasite.workforce.gwt.core.client.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: satimov
 * Date: 2/26/18 11:00 AM
 */
public enum ShippingDataStatus implements IsSerializable {
    NEW,
    PARTLY_CONVERTED,
    CONVERTED,
    PENDING,
    FAILED,
    SUCCESSFUL
}
