/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.client.rpc.hmrc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HmrcMtdServiceAsync {

    void checkVatNumber(String vatNumber, AsyncCallback<String> vatInfo);
}
