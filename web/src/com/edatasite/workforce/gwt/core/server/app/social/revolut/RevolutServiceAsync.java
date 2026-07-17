package com.edatasite.workforce.gwt.core.server.app.social.revolut;

import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutResponseDto;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RevolutServiceAsync {
    void createOrder(Integer amount, String currency, boolean isOurAccount, String description, AsyncCallback<RevolutResponseDto> callback);
}
