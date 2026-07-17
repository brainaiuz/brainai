package com.edatasite.workforce.gwt.signup.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

public interface SignUpService extends RemoteService {
//    AntiBotImage getAntibotImage();

    ArrayList<SelectItem> getSupportedLocales();

    SelectItem[] getCountries();

    CreatedCompany createCompany(NewCompany company);

    DomainInfo findByGoogleAppDomain(String domainName, String googleAppDomain, String email);

    void getParamsFromMarketPlace(StringBuffer stringBuffer, String param);

//    void updateGeneralSettings(Integer companyID);

    class App {
        public static SignUpServiceAsync get() {
            ServiceDefTarget target = GWT.create(SignUpService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/signup");
            return (SignUpServiceAsync) target;
        }
    }

}
