package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Anvar Akramov on 3/4/2018.
 */
public class StockTransferItemTO extends ResponseData {

    private Integer id;
    private String date;
    private BigDecimal transfer_qty;
    private TransferTO from;
    private TransferTO to;

    public StockTransferItemTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TransferTO getFrom() {
        return from;
    }

    public void setFrom(TransferTO from) {
        this.from = from;
    }

    public TransferTO getTo() {
        return to;
    }

    public void setTo(TransferTO to) {
        this.to = to;
    }

    public BigDecimal getTransfer_qty() {
        return transfer_qty;
    }

    public void setTransfer_qty(BigDecimal transfer_qty) {
        this.transfer_qty = transfer_qty;
    }
}
