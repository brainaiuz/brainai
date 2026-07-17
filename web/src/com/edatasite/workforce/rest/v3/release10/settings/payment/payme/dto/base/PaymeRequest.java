package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base;

import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.Method;

public class PaymeRequest {
    private Method method;
    private Long id;
    private Object params;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }
}
