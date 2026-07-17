package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Map;

public class ResponseDto  implements IsSerializable {

    private boolean success;
    private Map<String, String> data;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
