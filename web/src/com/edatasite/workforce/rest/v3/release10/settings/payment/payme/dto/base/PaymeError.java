package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import java.util.HashMap;
import java.util.Map;

public class PaymeError {
    private Integer code;
    private Map<String, String> message;
    private String data;

    public PaymeError(Integer code, Map<String, String> message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static PaymeError error(Integer code, Map<String, String> message, String data) {
        return new PaymeError(code, message, data);
    }

    public static Map<String, String> message(String uz, String ru, String en) {
        Map<String, String> message = new HashMap<String, String>();
        message.put("uz", uz);
        message.put("ru", ru);
        message.put("en", en);
        return message;
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public String getData() {
        return data;
    }
}
