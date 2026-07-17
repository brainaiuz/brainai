package com.edatasite.workforce.rest.v3.release10.accounting.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Normurod Buriev.
 * Date: 1/18/2021 5:05 PM
 */
public class UpdateStatusRequest {
    private Integer objectId;
    private String number;
    private String objectKey;
    @NotBlank
    @NotNull(message = "Status field is required!")
    private String status;

    public UpdateStatusRequest() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
