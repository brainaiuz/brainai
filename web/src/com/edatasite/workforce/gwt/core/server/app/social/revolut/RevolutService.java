package com.edatasite.workforce.gwt.core.server.app.social.revolut;

import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutResponseDto;
import com.google.gwt.user.client.rpc.RemoteService;

public interface RevolutService extends RemoteService {
    RevolutResponseDto createOrder(Integer amount, String currency, boolean isOurAccount, String description);
}
