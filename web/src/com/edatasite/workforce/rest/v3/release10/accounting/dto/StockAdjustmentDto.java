package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 2/24/2021 5:00 PM
 */
public class StockAdjustmentDto extends DynamicDto {
    private String number;
    @NotNull(message = "Date is required.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;
    @NotNull(message = "Account is required.")
    private IdCode account;
    private String memo;

    @Valid
    private List<AdjustmentItemDto> items;

    public StockAdjustmentDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public IdCode getAccount() {
        return account;
    }

    public void setAccount(IdCode account) {
        this.account = account;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<AdjustmentItemDto> getItems() {
        return items;
    }

    public void setItems(List<AdjustmentItemDto> items) {
        this.items = items;
    }
}
