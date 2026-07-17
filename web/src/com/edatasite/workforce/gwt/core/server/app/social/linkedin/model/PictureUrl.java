package com.edatasite.workforce.gwt.core.server.app.social.linkedin.model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Created by Anvar Akramov on 10/6/17.
 */
public class PictureUrl extends LinkedInObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer _total;

    private String values[];

    public PictureUrl() {

    }

    public Integer get_total() {
        return _total;
    }

    public void set_total(Integer _total) {
        this._total = _total;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }
}
