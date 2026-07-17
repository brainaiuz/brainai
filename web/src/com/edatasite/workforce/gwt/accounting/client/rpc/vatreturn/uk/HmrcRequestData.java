/*
 * Copyright (c) 2023.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HmrcRequestData<T> implements IsSerializable {
    private T data;
    private FraudPreventionData fraudPreventionData;
}
