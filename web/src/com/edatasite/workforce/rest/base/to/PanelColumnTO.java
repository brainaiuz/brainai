package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Created by Umidbek on 06.02.2015.
 */
public class PanelColumnTO implements IsSerializable {
    public static final String TYPE_STATIC = "static";
    public static final String TYPE_SELECT = "select";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_TIME = "time";

    String type;
    String name;
    String code;
    Boolean openOnClick;

    List<SelectItemTO> options;

    public PanelColumnTO() {
        this.openOnClick = false;
        this.type = TYPE_STATIC;
    }

    public PanelColumnTO(String name, String code) {
        this(TYPE_STATIC, name, code, false);
    }

    public PanelColumnTO(String name, String code, Boolean openOnClick) {
        this(TYPE_STATIC, name, code, openOnClick);
    }

    public PanelColumnTO(String type, String name, String code) {
        this(type, name, code, false);
    }

    public PanelColumnTO(String name, String code, List<SelectItemTO> options) {
        this(TYPE_SELECT, name, code, false);
        this.options = options;
    }

    public PanelColumnTO(String type, String name, String code, Boolean openOnClick) {
        if (type.equals(TYPE_STATIC) ||
            type.equals(TYPE_SELECT) ||
            type.equals(TYPE_DATE) ||
            type.equals(TYPE_TIME)) {
            this.type = type;
        } else {
            this.type = TYPE_STATIC;
        }

        this.name = name;
        this.code = code;
        this.openOnClick = openOnClick;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Boolean getOpenOnClick() {
        return openOnClick;
    }

    public void setOpenOnClick(Boolean openOnClick) {
        this.openOnClick = openOnClick;
    }

    public List<SelectItemTO> getOptions() {
        return options;
    }

    public void setOptions(List<SelectItemTO> options) {
        this.options = options;
    }
}
