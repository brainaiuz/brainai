package com.edatasite.workforce.gwt.ganttchart.client.connector;

import com.edatasite.workforce.gwt.ganttchart.client.geometry.Point;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Rectangle;

public interface Calculator {
    /**
     * Calculates the path to connect two Rectangles with a line.
     *
     * @param r1
     * @param r2
     * @return
     */
    Point[] calculate(Rectangle r1, Rectangle r2);

    /**
     * Calculates the path to connect two Rectangles with a line,
     * including any offset required for arrows. In the case of SVG-drawn
     * paths, the arrow is added <b>after</b> the path ends. This means
     * we need to adjust the end point in the path to account for the
     * arrow's size.
     *
     * @param r1
     * @param r2
     * @return
     */
    Point[] calculateWithOffset(Rectangle r1, Rectangle r2);
}
