package com.edatasite.workforce.gwt.core.server.jfreechart;

import org.jfree.chart.renderer.category.BarRenderer;

import java.awt.*;

/**
 * Created by dilsh0d on 29.02.16.
 */
public class CustomBarRenderer extends BarRenderer {
    /**
     * The colors.
     */
    private Paint[] colors;

    /**
     * Creates a new renderer.
     *
     * @param colors the colors.
     */
    public CustomBarRenderer(final Paint[] colors) {
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
