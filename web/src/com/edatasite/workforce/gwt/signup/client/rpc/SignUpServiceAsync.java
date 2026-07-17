package com.edatasite.workforce.gwt.signup.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface SignUpServiceAsync {

//    void getAntibotImage(AsyncCallback<AntiBotImage> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getSupportedLocales(AsyncCallback<ArrayList<SelectItem>> async);

    void createCompany(NewCompany company, AsyncCallback<CreatedCompany> async);

    void findByGoogleAppDomain(String domainName,String googleAppDomain, String email, AsyncCallback<DomainInfo> async);

    void getParamsFromMarketPlace(StringBuffer stringBuffer, String param, AsyncCallback<Void> callback);

//    void updateGeneralSettings(Integer companyID, AsyncCallback callback);

}
