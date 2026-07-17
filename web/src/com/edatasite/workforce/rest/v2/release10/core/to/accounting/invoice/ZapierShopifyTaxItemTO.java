package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ZapierShopifyTaxItemTO extends ResponseData {
    private Integer tax_id;
    private String tax_name;
    @Schema(required = true)
    private BigDecimal tax_rate;
    private BigDecimal tax_amount;


    public ZapierShopifyTaxItemTO() {
    }

    public Integer getTax_id() {
        return tax_id;
    }

    public void setTax_id(Integer tax_id) {
        this.tax_id = tax_id;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public BigDecimal getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(BigDecimal tax_rate) {
        this.tax_rate = tax_rate;
    }

    public BigDecimal getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(BigDecimal tax_amount) {
        this.tax_amount = tax_amount;
    }
}
