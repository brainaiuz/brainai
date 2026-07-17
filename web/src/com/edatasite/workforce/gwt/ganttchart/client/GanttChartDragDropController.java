package com.edatasite.workforce.gwt.ganttchart.client;

import com.allen_sauer.gwt.dnd.client.*;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.edatasite.workforce.gwt.ganttchart.client.connector.CalculatorFactory;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Point;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Rectangle;
import com.edatasite.workforce.gwt.ganttchart.client.model.PredecessorType;
import com.edatasite.workforce.gwt.ganttchart.client.widget.SVGPath;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 3, 2011
 * Time: 11:11:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartDragDropController extends AbsolutePositionDropController implements DragHandler {

	private GanttChart ganttChart;
    private final AbsolutePanel dropTarget;
    private int widgetTop = 0;
    private int widgetLeft = 0;
    public static HashMap<String, SVGPath> predecessorPaths = new HashMap<>();

    public GanttChartDragDropController(GanttChart ganttChart, AbsolutePanel dropTarget) {
        super(dropTarget);
		this.ganttChart = ganttChart;
        this.dropTarget = dropTarget;
    }

    @Override
    public void onDrop(DragContext context) {
        dropTarget.add(context.draggable);
        super.onDrop(context);
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {
        super.onPreviewDrop(context);
    }

    public void onDragEnd(DragEndEvent event) {
		FlowPanel widget = (FlowPanel) event.getSource();
        Integer days1 = widgetLeft / TaskWidget.cellSize;
        int widgetLeft1 = Integer.valueOf(DOM.getStyleAttribute(widget.getElement(), "left").replace("px", ""));
        int days2 = widgetLeft1 / TaskWidget.cellSize;
        widget.getElement().getStyle().setTop(widgetTop, Style.Unit.PX);
        widget.getElement().getStyle().setLeft(days2 * TaskWidget.cellSize, Style.Unit.PX);
        TaskWidget taskWidget = ganttChart.taskWidgets.get("t_"+widget.getElement().getId().split("t_")[1]);
        taskWidget.getTask().setStartDate(DateUtil.addDays(taskWidget.getTask().getStartDate(), (days2 - days1)));
        taskWidget.getTask().setEndDate(DateUtil.addDays(taskWidget.getTask().getEndDate(), (days2 - days1)));
        if (taskWidget.getTask().getPredecessorTasks() != null && taskWidget.getTask().getPredecessorTasks().length > 0) {
            Point[] path = null;
            PredecessorType type = PredecessorType.FS;
            for (int k=0; k<taskWidget.getTask().getPredecessorTasks().length; k++) {
                //------------------------------------------- Remove old paths ----------------------------------------------
                String key = taskWidget.getTask().getObjectID() + "-" + taskWidget.getTask().getPredecessorTasks()[k];
                ganttChart.removeConnector(predecessorPaths.get(key));
                //------------------------------------------- Remove next paths ---------------------------------------------
                for (String succKey : predecessorPaths.keySet()) {
                    if (succKey.split("-")[1].equals(taskWidget.getTask().getObjectID().toString())) {
                        ganttChart.removeConnector(predecessorPaths.get(succKey));

                        // Successors
                        int fromRectTop1 = Integer.parseInt(taskWidget.getTaskWidget().getElement().getStyle().getTop().replace("px", ""));
                        Rectangle fromRect1 = new Rectangle(days2 * TaskWidget.cellSize, fromRectTop1, taskWidget.getWidth(), TaskWidget.cellSize);
                        TaskWidget toTask1 = ganttChart.taskWidgets.get("t_"+succKey.split("-")[0]);
                        int toRectTop1 = Integer.parseInt(toTask1.getTaskWidget().getElement().getStyle().getTop().replace("px", ""));
                        Rectangle toRect1 = new Rectangle(toTask1.getLeft(), toRectTop1, toTask1.getWidth(), TaskWidget.cellSize);
                        if (fromRect1 != null && toRect1 != null) {
                            path = CalculatorFactory.get(type).calculateWithOffset(fromRect1, toRect1);
                            SVGPath svgPath = ganttChart.getSVGPath(path);
                            ganttChart.renderConnector(svgPath);
                            predecessorPaths.put(succKey, svgPath);
                        }
                    }
                }
                //------------------------------------------- Draw new paths -----------------------------------------------
                // Predecessors
                TaskWidget fromTask = ganttChart.taskWidgets.get("t_"+taskWidget.getTask().getPredecessorTasks()[k]);
                int fromRectTop = Integer.parseInt(fromTask.getTaskWidget().getElement().getStyle().getTop().replace("px", ""));
                Rectangle fromRect = new Rectangle(fromTask.getLeft(), fromRectTop, fromTask.getWidth(), TaskWidget.cellSize);
                int toRectTop = Integer.parseInt(taskWidget.getTaskWidget().getElement().getStyle().getTop().replace("px", ""));
                Rectangle toRect = new Rectangle(days2 * TaskWidget.cellSize, toRectTop, taskWidget.getWidth(), TaskWidget.cellSize);
                if (fromRect != null && toRect != null) {
                    path = CalculatorFactory.get(type).calculateWithOffset(fromRect, toRect);
                    SVGPath svgPath = ganttChart.getSVGPath(path);
                    ganttChart.renderConnector(svgPath);
                    predecessorPaths.put(key, svgPath);
                }
            }
        }
    }

    public void onDragStart(DragStartEvent event) {
        FlowPanel widget = (FlowPanel) event.getSource();
        widgetTop = Integer.valueOf(DOM.getStyleAttribute(widget.getElement(), "top").replace("px", ""));
        widgetLeft = Integer.valueOf(DOM.getStyleAttribute(widget.getElement(), "left").replace("px", ""));
    }

    public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {

    }

    public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {

    }
}
