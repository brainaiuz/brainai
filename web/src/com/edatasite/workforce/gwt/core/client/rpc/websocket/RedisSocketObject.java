package com.edatasite.workforce.gwt.core.client.rpc.websocket;

public class RedisSocketObject {
    private Integer companyId;
    private boolean sendToAll;
    private WebSocketServerObject webSocketServerObject;

    public RedisSocketObject() {
    }

    public RedisSocketObject(Integer companyId, boolean sendToAll, WebSocketServerObject webSocketServerObject) {
        this.companyId = companyId;
        this.sendToAll = sendToAll;
        this.webSocketServerObject = webSocketServerObject;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public boolean isSendToAll() {
        return sendToAll;
    }

    public void setSendToAll(boolean sendToAll) {
        this.sendToAll = sendToAll;
    }

    public WebSocketServerObject getWebSocketServerObject() {
        return webSocketServerObject;
    }

    public void setWebSocketServerObject(WebSocketServerObject webSocketServerObject) {
        this.webSocketServerObject = webSocketServerObject;
    }
}
