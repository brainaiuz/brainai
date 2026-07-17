package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Created by Dilsh0d on 11/3/2017.
 */
public class InvoiceSalesAccountTO extends ResponseData {
    @Schema(required = true)
    private Integer sales_account_id;
    private String sales_account_name;

    public InvoiceSalesAccountTO() {
    }

    public InvoiceSalesAccountTO(Integer sales_account_id, String sales_account_name) {
        this.sales_account_id = sales_account_id;
        this.sales_account_name = sales_account_name;
    }

    public Integer getSales_account_id() {
        return sales_account_id;
    }

    public void setSales_account_id(Integer sales_account_id) {
        this.sales_account_id = sales_account_id;
    }

    public String getSales_account_name() {
        return sales_account_name;
    }

    public void setSales_account_name(String sales_account_name) {
        this.sales_account_name = sales_account_name;
    }
}
