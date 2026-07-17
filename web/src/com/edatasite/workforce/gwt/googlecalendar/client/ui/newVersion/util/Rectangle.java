package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Feb 18, 2010
 * Time: 12:26:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rectangle {

    private int left;
    private int top;
    private int offsetWidth;
    private int offsetHeight;

    public Rectangle(int left, int top, int offsetWidth, int offsetHeight) {
        this.left = left;
        this.top = top;
        this.offsetWidth = offsetWidth;
        this.offsetHeight = offsetHeight;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetWidth(int offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }
}
