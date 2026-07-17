package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

public class PaymeResponse {
    private Long id;
    private Object result; // TODO something with this
    private PaymeError error;

    public static PaymeResponse ok(Long id, Object result) {
        PaymeResponse paymeResponse = new PaymeResponse();
        paymeResponse.setId(id);
        paymeResponse.setResult(result);
        return paymeResponse;
    }

    public static PaymeResponse error(Long id, PaymeError error) {
        PaymeResponse paymeResponse = new PaymeResponse();
        paymeResponse.setId(id);
        paymeResponse.setError(error);
        return paymeResponse;
    }

    public static PaymeResponse error(PaymeError error) {
        PaymeResponse paymeResponse = new PaymeResponse();
        paymeResponse.setError(error);
        return paymeResponse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public PaymeError getError() {
        return error;
    }

    public void setError(PaymeError error) {
        this.error = error;
    }
}
