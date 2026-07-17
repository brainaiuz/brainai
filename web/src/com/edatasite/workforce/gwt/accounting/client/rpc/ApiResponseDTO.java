package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ApiResponseDTO implements IsSerializable {
    private boolean success;
    private MappingResponseDto data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public MappingResponseDto getData() {
        return data;
    }

    public void setData(MappingResponseDto data) {
        this.data = data;
    }
}
