package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * User : Dilsh0d Madrahimov on 9/17/2019 5:47 PM
 */
public class IdDTO extends ResponseData {
    private Integer id;

    public IdDTO() {
    }

    public IdDTO(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
