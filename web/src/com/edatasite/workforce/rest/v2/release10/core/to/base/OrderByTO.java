package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Dilsh0d on 10/18/2017.
 */
public class OrderByTO extends ResponseData {
    private String type;
    private String direction;

    public OrderByTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
