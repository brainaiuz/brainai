package com.edatasite.workforce.gwt.core.server.zatca.service;

import com.edatasite.workforce.gwt.core.server.zatca.service.dto.InvoiceRequest;
import com.edatasite.workforce.gwt.core.server.zatca.service.dto.InvoiceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ZatcaInvoiceService {

    @POST("invoice/compliance")
    Call<InvoiceResponse> compliance(@Body InvoiceRequest request);

    @POST("invoice/clearance")
    Call<InvoiceResponse> clearance(@Body InvoiceRequest request);
}
