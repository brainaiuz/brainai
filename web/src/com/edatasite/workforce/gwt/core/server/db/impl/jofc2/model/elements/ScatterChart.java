/*
This file is part of JOFC2.

JOFC2 is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as
published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

JOFC2 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

See <http://www.gnu.org/licenses/lgpl-3.0.txt>.
 */
package com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.elements;

import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.metadata.Alias;
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.model.metadata.Converter;
import com.edatasite.workforce.gwt.core.server.db.impl.jofc2.util.ScatterChartPointConverter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public class ScatterChart extends AnimatedElement {

	private static final String TYPE = "scatter";
	private static final long serialVersionUID = 3029567780918048503L;
	private String colour;
	@Alias("dot-style")
    private DotStyle dotStyle;
    @Alias("dot-size")
    private Integer dotSize;
    @Alias("halo-size")
    private Integer hollowSize;
    private Integer width;

	public ScatterChart() {
		super(TYPE);
	}

    public ScatterChart(Style style){
        super(style.getStyle());
    }

	public ScatterChart addPoints(Point... points) {
		getValues().addAll(Arrays.asList(points));
		return this;
	}

	public ScatterChart addPoint(Number x, Number y) {
		return addPoints(new Point(x, y));
	}

	public ScatterChart addPoints(Collection<Point> points) {
		getValues().addAll(points);
		return this;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

    public void setWidth(Integer width) {
       this.width = width;
    }

    public Integer getWidth() {
       return width;
    }

    public void setDotStyle(DotStyle dotStyle) {
        this.dotStyle = dotStyle;
    }

    public DotStyle getDotStyle() {
        return dotStyle;
    }

    public Integer getDotSize() {
        return dotSize;
    }

    public void setDotSize(Integer dotSize) {
        this.dotSize = dotSize;
    }

    public Integer getHollowSize() {
        return hollowSize;
    }

    public void setHollowSize(Integer hollowSize) {
        this.hollowSize = hollowSize;
    }

    public static class DotStyle implements Serializable {
        private String type;
        private String tip;

        public void setTooltip(String tip) {
            this.tip = tip;
        }

        public String getTooltip() {
            return tip;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

	@Converter(ScatterChartPointConverter.class)
	public static class Point implements Serializable {

		private Number x;
		private Number y;

		public Point(Number x, Number y) {
			this.x = x;
			this.y = y;
		}

		public Number getX() {
			return x;
		}

		public void setX(Number x) {
			this.x = x;
		}

		public Number getY() {
			return y;
		}

		public void setY(Number y) {
			this.y = y;
		}
    }

    public enum Style {
        NORMAL("scatter"), LINE("scatter_line");

		private String style;

		Style(String style) {
			this.style = style;
		}

		public String getStyle() {
			return style;
		}
	}
}
