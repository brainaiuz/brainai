package com.edatasite.workforce.rest.v3.release10.core.to.accounting;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

/**
 * Created By : Dilsh0d Madrahimov on 9/30/2019 2:15 PM
 */
public class PickListDTO extends ResponseData {
    private Integer id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @Schema(required = true)
    private String ship_date;
    private String shipping_label;
    private String carrier_account_id;
    private ArrayList<PickListItemDTO> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public String getShipping_label() {
        return shipping_label;
    }

    public void setShipping_label(String shipping_label) {
        this.shipping_label = shipping_label;
    }

    public String getCarrier_account_id() {
        return carrier_account_id;
    }

    public void setCarrier_account_id(String carrier_account_id) {
        this.carrier_account_id = carrier_account_id;
    }

    public ArrayList<PickListItemDTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<PickListItemDTO> items) {
        this.items = items;
    }
}
