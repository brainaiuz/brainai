package com.edatasite.workforce.rest.v2.release10.core.to.base.customfield;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomFieldPostTO extends ResponseData {
    private String column_code;
    private String data_type;
    private String string_value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private String date_value;

    public CompanyCustomFieldItem toCompanyCustomFieldItem() {
        CompanyCustomFieldItem item = new CompanyCustomFieldItem();
        item.setColumnCode(getColumn_code());
        item.setDataType(getData_type());
        item.setFieldStringValue(getString_value());
        item.setFieldDateNonConvertedValue(new DateNonConvertable(getConvertedDateValue()));
        return item;
    }

    private Date getConvertedDateValue() {
        if (!StringUtils.isBlank(getDate_value())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                return dateFormat.parse(getDate_value());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String getColumn_code() {
        return column_code;
    }

    public void setColumn_code(String column_code) {
        this.column_code = column_code;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getString_value() {
        return string_value;
    }

    public void setString_value(String string_value) {
        this.string_value = string_value;
    }

    public String getDate_value() {
        return date_value;
    }

    public void setDate_value(String date_value) {
        this.date_value = date_value;
    }
}
