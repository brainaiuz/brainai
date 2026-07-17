package com.edatasite.workforce.gwt.materialkanban.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class KanbanMessage implements IsSerializable {
    public static final String OK = "200";
    public static final String SERVER_ERROR ="500";
    private Integer entityId;
    private Integer order;
    private String response;
    private String errorMessage;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
