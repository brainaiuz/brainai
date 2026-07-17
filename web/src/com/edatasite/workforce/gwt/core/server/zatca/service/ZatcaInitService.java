package com.edatasite.workforce.gwt.core.server.zatca.service;

import com.edatasite.workforce.gwt.core.server.zatca.service.dto.RenewRequest;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.RenewResponse;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.SettingsRequest;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.SettingsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ZatcaInitService {

    @POST("settings/init")
    Call<SettingsResponse> init(@Body SettingsRequest request);

    @POST("settings/renew")
    Call<RenewResponse> renew(@Body RenewRequest request);
}
