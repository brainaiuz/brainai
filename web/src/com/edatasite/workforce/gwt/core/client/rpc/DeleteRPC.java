package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;

/**
 * Created by Omonullo Abdullaev on 6/7/2016.
 */
public class DeleteRPC implements Serializable {

    private Integer id;
    private String message;
    private Boolean deleted = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
