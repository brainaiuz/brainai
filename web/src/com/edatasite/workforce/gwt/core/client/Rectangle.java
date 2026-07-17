package com.edatasite.workforce.gwt.core.client;

/**
 * User: Aziz
 * Date: 11/16/12
 * Copied from MyGWT
 * Instances of this class represent rectangular areas in an (x, y) coordinate
 * system.
 */
public class Rectangle {

    /**
     * The x coordinate.
     */
    public int x;

    /**
     * The y coordinate.
     */
    public int y;

    /**
     * The width of the rectangle
     */
    public int width;

    /**
     * The height of the rectangle
     */
    public int height;

    /**
     * Create a new rectangle instance.
     */
    public Rectangle() {

    }

    /**
     * Creates a new rectangle instance.
     *
     * @param x the x value
     * @param y the y value
     * @param width the rectangle's width
     * @param height the rectangle's height
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;this.height = height;
    }

    /**
     * Returns <code>true</code> if the point specified by the arguments is
     * inside the area specified by the receiver, and <code>false</code>
     * otherwise.
     *
     * @param x the x coordinate of the point to test for containment
     * @param y the y coordinate of the point to test for containment
     * @return <code>true</code> if the rectangle contains the point and
     *         <code>false</code> otherwise
     */
    public boolean contains(int x, int y) {
        return (x >= this.x) && (y >= this.y) && ((x - this.x) < width)
                && ((y - this.y) < height);
    }

    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Rectangle)) return false;
        Rectangle r = (Rectangle) object;
        return (r.x == this.x) && (r.y == this.y) && (r.width == this.width)
                && (r.height == this.height);
    }

    public String toString() {
        return "left: " + x + " top: " + y + " width: " + width + " height: " + height;
    }

}
