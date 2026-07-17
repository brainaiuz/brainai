package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InvLine {
    @SerializedName("ItemCode")
    Integer itemCode;

    @SerializedName("ID")
    String id;

    @SerializedName("Note")
    String note = "";

    @SerializedName("InvdQty")
    BigDecimal invdQty;

    @SerializedName("InvQtyUom")
    String invQtyUom = "";

    @SerializedName("LineExtAmt")
    BigDecimal lineExtAmt;

    @SerializedName("AlwChg")
    List<AlwChg> alwChg = new ArrayList<>();

    @SerializedName("TaxTotal")
    TaxTotal taxTotal = new TaxTotal();

    @SerializedName("Item")
    Item item = new Item();

    public Integer getItemCode() {
        return itemCode;
    }

    public void setItemCode(Integer itemCode) {
        this.itemCode = itemCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getInvdQty() {
        return invdQty;
    }

    public void setInvdQty(BigDecimal invdQty) {
        this.invdQty = invdQty;
    }

    public String getInvQtyUom() {
        return invQtyUom;
    }

    public void setInvQtyUom(String invQtyUom) {
        this.invQtyUom = invQtyUom;
    }

    public BigDecimal getLineExtAmt() {
        return lineExtAmt;
    }

    public void setLineExtAmt(BigDecimal lineExtAmt) {
        this.lineExtAmt = lineExtAmt;
    }

    public List<AlwChg> getAlwChg() {
        return alwChg;
    }

    public void setAlwChg(List<AlwChg> alwChg) {
        this.alwChg = alwChg;
    }

    public TaxTotal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(TaxTotal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
