package com.edatasite.workforce.core.tools;

import java.io.Serial;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 16, 2010
 * Time: 6:34:20 PM
 * To change this template use File | Settings | File Templates.
 */
public final class MsgHeader implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -5722833299002367057L;
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
