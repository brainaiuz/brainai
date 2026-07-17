package com.edatasite.workforce.gwt.core.server.jfreechart;

import org.jfree.chart.plot.RingPlot;

import java.awt.*;

/**
 * Created by dilsh0d on 01.03.16.
 */
public class CustomRingPlot extends RingPlot {
    /**
     * The colors.
     */
    private Paint[] colors;

    /**
     * Creates a new renderer.
     *
     * @param colors the colors.
     */
    public CustomRingPlot(final Paint[] colors) {
        this.colors = colors;
    }

    /**
     * Returns the paint for an item.  Overrides the default behaviour inherited from
     * AbstractSeriesRenderer.
     *
     * @param row    the series.
     * @param column the category.
     * @return The item color.
     */
    public Paint getItemPaint(final int row, final int column) {
        return this.colors[column % this.colors.length];
    }
}
