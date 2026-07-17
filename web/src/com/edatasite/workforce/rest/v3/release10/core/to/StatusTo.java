package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;

public class StatusTo extends ResponseData {

    private Integer id;
    private String code;
    private String name;
    private ColorTO colorTO;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColorTO getColorTO() {
        return colorTO;
    }

    public void setColorTO(ColorTO colorTO) {
        this.colorTO = colorTO;
    }
}
