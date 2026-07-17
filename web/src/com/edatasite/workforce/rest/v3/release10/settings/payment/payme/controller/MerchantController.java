package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.controller;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeError;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeRequest;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeResponse;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CancelTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CheckPerformTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CheckTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CreateTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.GetStatement;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.PerformTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.Method;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.service.MerchantService;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util.AuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sharof Mukhtorov
 * Date: 07.05.2025
 * Time: 12:05:01
 */

@Hidden
@Tag(name = "Payme Merchant", description = "Merchant API for Payme")
@RestController
@RequestMapping(value = "/payme/merchant")
public class MerchantController {
    private static final Logger log = LoggerFactory.getLogger(MerchantController.class);

    private final MerchantService merchantService;
    private final AuthUtil authUtil;
    private final ObjectMapper objectMapper;
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    public MerchantController(MerchantService merchantService, AuthUtil authUtil, ObjectMapper objectMapper) {
        this.merchantService = merchantService;
        this.authUtil = authUtil;
        this.objectMapper = objectMapper;
    }

//    @SecurityRequirement(name = "Payme")
    @PostMapping
    public ResponseEntity<?> handle(@RequestParam("database") String database, // FREE|PAID
                                    @RequestParam("companyId") Integer companyId,
                                    @RequestBody String updateJson,
                                    @RequestHeader(value = "Authorization", required = false) String authHeader) throws JsonProcessingException {
        log.info("Payme request: {}", updateJson);
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        ServerSecurityContext.getInstance().setDatabase(database);
        if (!authUtil.isAuthorizedKpiVersion(authHeader)) {
            Map<String, String> message = PaymeError.message("Avtorizatsiya qilinmagan", "Unauthorized", "Unauthorized");
            return ResponseEntity.ok(PaymeResponse.error(new PaymeError(-32504, message, "authorization")));
        }
        PaymeRequest update = null;
        try {
            update = objectMapper.readValue(updateJson, PaymeRequest.class);
        } catch (JsonProcessingException e) {
            Map<String, String> message = PaymeError.message("Ma'lumot noto'g'ri kiritilgan", "Json parse failed", "Json parse failed");
            return ResponseEntity.ok(PaymeResponse.error(new PaymeError(-32700, message, "params")));
        }
        Long id = update.getId();
        String method = update.getMethod().toString();
        String params = update.getParams().toString();

        log.info("Payme request: method = {}, params = {}", method, params);
        PaymeResponse result = switch (Method.valueOf(method)) {
            case CheckPerformTransaction ->
                    merchantService.handleCheckPerformTransaction(id, gson.fromJson(params, CheckPerformTransaction.class));
            case CreateTransaction ->
                    merchantService.handleCreateTransaction(id, gson.fromJson(params, CreateTransaction.class));
            case PerformTransaction ->
                    merchantService.handlePerformTransaction(id, gson.fromJson(params, PerformTransaction.class));
            case CheckTransaction ->
                    merchantService.handleCheckTransaction(id, gson.fromJson(params, CheckTransaction.class));
            case CancelTransaction ->
                    merchantService.handleCancelTransaction(id, gson.fromJson(params, CancelTransaction.class));
            case GetStatement ->
                    merchantService.handleGetStatement(id, gson.fromJson(params, GetStatement.class));
        };
        log.info("Payme response: {}", result);
        return ResponseEntity.ok(gson.toJson(result));
    }
}
