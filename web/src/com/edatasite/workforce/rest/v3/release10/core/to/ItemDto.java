package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"id", "name", "code"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto extends DynamicDto {
    private Integer id;
    private String name;
    private String code;
    private BigDecimal value;

    public ItemDto() {
    }

    public ItemDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemDto(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public ItemDto(Integer id, String name, String code, String color) {
        this.id = id;
        this.name = name;
        this.code = code;
        super.addProperty("color", color);
    }

    public String getValueByKey(String key) {
        Object value = getProperties().get(key);

        if (value != null && value instanceof String) {
            return String.valueOf(value);
        }
        return null;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
