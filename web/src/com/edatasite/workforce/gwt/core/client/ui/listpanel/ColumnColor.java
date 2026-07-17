package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 23-Aug-2010
 * Time: 14:42:22
 */
public class ColumnColor implements IsSerializable {
    public static final String COLORISE_CONDITION_WORD = "ccw";
    public static final String ROW_COLOR = "rw";
    public static final String COLOMN_COLOR = "cc";
    public static final String WORD_COLOR = "wc";
    private String condition;
    private String target;
    private String color;
    private String type;
    private String betweenSecondValue;

    private Boolean isGradient;

    public Boolean getGradient() {
        return isGradient != null && isGradient;
    }

    public void setGradient(Boolean gradient) {
        isGradient = gradient;
    }


    public String getBetweenSecondValue() {
        return betweenSecondValue;
    }

    public void setBetweenSecondValue(String betweenSecondValue) {
        this.betweenSecondValue = betweenSecondValue;
    }

    public ColumnColor(String condition, String target, String color) {
        this();
        this.condition = condition;
        this.target = target;
        this.color = color;
    }

    public ColumnColor(String color) {
        this();
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ColumnColor() {

    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color != null ? "#" + color.replace("#", "") : null;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean hasColor() {
        return color != null && condition != null;
    }

    public boolean isRowColor() {
        return target != null && target.equals("r");
    }

    public boolean isColumnColor() {
        return target != null && target.equals("c");
    }

    public boolean isWordColor() {
        return target != null && target.equals("w");
    }
}
