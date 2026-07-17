package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/4/2018.
 */
public class AdjustmentTO extends ResponseData {

    private Integer id;
    private String number;
    private String date;
    private ArrayList<AdjustmentItemTO> adjustment_items;
    private IdNameTO account;
    private String memo;
    private Integer int_number;
    private ArrayList<Integer> rfpIds;

    public AdjustmentTO() {
    }

    public AdjustmentTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AdjustmentItemTO> getAdjustment_items() {
        return adjustment_items;
    }

    public void setAdjustment_items(ArrayList<AdjustmentItemTO> adjustment_items) {
        this.adjustment_items = adjustment_items;
    }

    public IdNameTO getAccount() {
        return account;
    }

    public void setAccount(IdNameTO account) {
        this.account = account;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getInt_number() {
        return int_number;
    }

    public void setInt_number(Integer int_number) {
        this.int_number = int_number;
    }

    public ArrayList<Integer> getRfpIds() {
        return rfpIds;
    }

    public void setRfpIds(ArrayList<Integer> rfpIds) {
        this.rfpIds = rfpIds;
    }
}
