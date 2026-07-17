package com.edatasite.workforce.rest.v2.release10.enums;

/**
 * Created by Dilsh0d on 10/9/2017.
 */
public enum OrderByEnum {
    ASC(1, "ASC"),
    DESC(2, "DESC");

    private Integer id;
    private String direction;

    OrderByEnum(Integer id, String direction) {
        this.id = id;
        this.direction = direction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public static OrderByEnum getDirection(String direction) {
        if (direction == null) {
            return null;
        }
        try {
            return OrderByEnum.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
