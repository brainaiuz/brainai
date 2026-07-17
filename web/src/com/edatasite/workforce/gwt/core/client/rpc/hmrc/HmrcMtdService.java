/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.client.rpc.hmrc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.BotActivationService;
import com.edatasite.workforce.gwt.core.client.rpc.BotActivationServiceAsync;
import com.edatasite.workforce.gwt.core.server.app.hmrc.dto.VatObligationsDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

public interface HmrcMtdService extends RemoteService {

    String checkVatNumber(String vatNumber);

    class App {
        public static HmrcMtdServiceAsync get() {
            ServiceDefTarget target = GWT.create(HmrcMtdService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/hmrcMtdService");
            return (HmrcMtdServiceAsync) target;
        }
    }
}
