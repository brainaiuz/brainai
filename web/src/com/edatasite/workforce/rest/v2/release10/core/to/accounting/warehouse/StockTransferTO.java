package com.edatasite.workforce.rest.v2.release10.core.to.accounting.warehouse;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 3/4/2018.
 */
public class StockTransferTO extends ResponseData {

    private Integer id;
    private String name;
    private String number;
    private String date;
    private ArrayList<StockTransferItemTO> transfer_items;

    public StockTransferTO() {
    }

    public StockTransferTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<StockTransferItemTO> getTransfer_items() {
        return transfer_items;
    }

    public void setTransfer_items(ArrayList<StockTransferItemTO> transfer_items) {
        this.transfer_items = transfer_items;
    }
}
