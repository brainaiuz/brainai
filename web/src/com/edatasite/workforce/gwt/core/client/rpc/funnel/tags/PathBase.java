package com.edatasite.workforce.gwt.core.client.rpc.funnel.tags;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Date: 02.08.12
 * Time: 10:34
 */
public class PathBase implements Tag, IsSerializable {
    public PathBase() {
    }

    public PathBase(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private String command;
    private double x;
    private double y;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getString(double offsetX, double offsetY) {
        String result = " " + (x + offsetX) + "," + (y + offsetY);
        if (getCommand() == null || "".equals(getCommand())) {
            return result;
        }
        return " " + getCommand() + result;
    }
}
