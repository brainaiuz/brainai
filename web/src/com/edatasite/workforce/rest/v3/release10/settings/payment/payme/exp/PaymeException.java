package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.exp;

import java.util.Map;
public class PaymeException extends RuntimeException {
    private Long requestId;
    private Integer code;
    private Map<String, String> desc;
    private String data;

    public PaymeException(Long requestId, Integer code, Map<String, String> desc, String data) {
        this.requestId = requestId;
        this.code = code;
        this.desc = desc;
        this.data = data;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, String> getDesc() {
        return desc;
    }

    public void setDesc(Map<String, String> desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
