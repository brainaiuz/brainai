package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class InvoiceStatusTO extends ResponseData {
    //    private Integer status_id;
    private String status_name;
    @Schema(required = true)
    private String status_code;

    public InvoiceStatusTO() {
    }

    public InvoiceStatusTO(String status_name, String status_code) {
//        this.status_id = status_id;
        this.status_name = status_name;
        this.status_code = status_code;
    }

//    public Integer getStatus_id() {
//        return status_id;
//    }
//
//    public void setStatus_id(Integer status_id) {
//        this.status_id = status_id;
//    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }
}
