package com.edatasite.workforce.gwt.core.client.rpc.funnel;

import com.edatasite.workforce.gwt.core.client.rpc.funnel.tags.PathBase;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Date: 31.07.12
 * Time: 18:14
 */
public class FunnelItem implements IsSerializable, Serializable {
    private static final long serialVersionUID = 1L;

    public FunnelItem() {
    }

    public FunnelItem(double value, String title) {
        this.value = value;
        this.title = title;
        int cr1 = (Math.abs((int) (Math.random() * 100) % 24) + 1) * 10;
        int cg1 = (Math.abs((int) (Math.random() * 100) % 24) + 1) * 10;
        int cb1 = (Math.abs((int) (Math.random() * 100) % 24) + 1) * 10;
        setColor("rgb(" + cr1 + "," + cg1 + "," + cb1 + ")");
        setColor2("rgb(" + (cr1 + 5) + "," + (cg1 + 5) + "," + (cb1 + 5) + ")");
    }

    private String color;
    private String color2;
    private double value;
    private String title;
    private String description;
    private LinkedList<PathBase> path = new LinkedList<>();
    private double centerX;
    private double centerY;
    private double height;
    private double bottomRadius;
    private double topRadius;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinkedList<PathBase> getPath() {
        return path;
    }

    public void setPath(LinkedList<PathBase> path) {
        this.path = path;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getColorAsString() {
        return color != null ? color : "";
    }

    public String getColor2AsString() {
        return color2 != null ? color2 : "";
    }

    public String getPathString(double offsetX, double offsetY) {
        StringBuilder sb = new StringBuilder();
        for (PathBase aPath : path) {
            sb.append(aPath.getString(offsetX, offsetY));
        }
        return sb.toString();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getBottomRadius() {
        return bottomRadius;
    }

    public void setBottomRadius(double bottomRadius) {
        this.bottomRadius = bottomRadius;
    }

    public double getTopRadius() {
        return topRadius;
    }

    public void setTopRadius(double topRadius) {
        this.topRadius = topRadius;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }
}
