package com.edatasite.workforce.gwt.ganttchart.client.widget;

import com.edatasite.workforce.gwt.ganttchart.client.GanttChart;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

public class SVGPanel extends ComplexPanel implements HasWidgets {
    public static int index = 0, x=0, y=0;
    public static HashMap<String, SVGPath> linesById = new HashMap<>();
	private GanttChart ganttChart;

	public SVGPanel() {
		setElement(SVG.createSvg());
//		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.MOUSEEVENTS | Event.ONCLICK);
	}
	public SVGPanel(GanttChart ganttChart) {
		this();
		this.ganttChart = ganttChart;
	}

	@Override
	public void setWidth(String value) {
		getElement().setAttribute("width", value);
	}

	@Override
	public void setHeight(String value) {
		getElement().setAttribute("height", value);
	}

	public void setPointerEvents(String value) {
		getElement().getStyle().setProperty("pointerEvents", value);
	}

	public void setShapeRendering(String value) {
		getElement().setAttribute("shape-rendering", value);
	}

	@Override
	public void add(Widget w) {
		super.add(w, getElement());
	}

    public void addWidget(SVGPath w) {
        super.add(w, getElement());
        index++;
        linesById.put(w.getValue(), w);
    }

	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}

    public void removePath(SVGPath svgPath) {
		SVGPath svgPath1 = linesById.get(svgPath.getValue());
		if (svgPath != null) {
            svgPath1.removeFromParent();
        }
    }

	/*@Override
	public void onBrowserEvent(Event event) {
		boolean isClicked = false;
		int eventType = event.getTypeInt();
		if (eventType == Event.ONCLICK) {
			if (!isClicked) {
				x = event.getClientX();
				y = event.getClientY();
			}
		}
		if (eventType == Event.ONMOUSEMOVE) {
			if (isClicked) {
				Point[] points = new Point[2];
				points[0] = new Point(x,y);
				points[1] = new Point(event.getClientX(),event.getClientY());
				SVGPath line = new SVGPath();
				line.setValue(points);
				line.setStroke("black");
				line.setFill("none");
				line.setStrokeWidth(1);
				line.setMarkerEnd(ganttChart.svgArrowMarker);
				line.getElement().setId("p_"+x+"_"+y);
				addWidget(line);
			}
		}
		if (eventType == Event.ONMOUSEOVER) {
			isClicked = false;
		}

		super.onBrowserEvent(event);
	}*/
}
