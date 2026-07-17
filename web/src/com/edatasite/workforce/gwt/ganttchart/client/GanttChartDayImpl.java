package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.ganttchart.client.connector.CalculatorFactory;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Point;
import com.edatasite.workforce.gwt.ganttchart.client.geometry.Rectangle;
import com.edatasite.workforce.gwt.ganttchart.client.model.PredecessorType;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.ganttchart.client.treetable.GanttTreeItem;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 21.02.13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartDayImpl extends GanttChart {

	private GanttChart ganttChart;

	public GanttChartDayImpl(GanttChart ganttChart) {
		this.ganttChart = ganttChart;
	}

	public GanttChartDayImpl(GanttItem ganttItem) {
		super(ganttTypeDaily, ganttItem);
	}

	@Override
    protected void drawRightContent(Date firstStartDate, int daysCount) {
		if (ganttChart == null) {
			this.ganttChart = this;
		}
		Date firstStartDateClone = (Date) firstStartDate.clone();
        FlexTable.FlexCellFormatter chartPanelFormatter = ganttChart.rightHeaderPanel.getFlexCellFormatter();
        FlexTable.FlexCellFormatter backgroundPanelFormatter = ganttChart.rightBackgroundPanel.getFlexCellFormatter();
        int index = 0;
        // tepadagi kunlar nomini yozish
        for (int i = 0; i < daysCount; i++) {
            if (i%7 == 0) {
                Date lastDate = DateUtil.getCompanyLastDateOfWeek(firstStartDateClone);
                HTML dateString = new HTML(DateUtil.getFormattedDate(firstStartDateClone) + " - " + DateUtil.getFormattedDate(lastDate));
				dateString.addStyleName("dateString");
				ganttChart.rightHeaderPanel.setWidget(0, index, dateString);
				chartPanelFormatter.setColSpan(0, index, 7);
				chartPanelFormatter.setStyleName(0, index, "tableCellStyle");
                index++;
            }
			String styleName = "tableCellStyle";
			if (ganttChart.ganttItem.getDayOffs().contains(firstStartDateClone.getDay())) {
				styleName = "weekEndtableRows";
			} else {
				styleName = "tableCellStyle";
			}
			backgroundPanelFormatter.setStyleName(0, i, styleName + " rightBackgr");
			chartPanelFormatter.setStyleName(1, i, styleName);
			chartPanelFormatter.setStyleName(2, i, styleName);
			backgroundPanelFormatter.setWidth(0, i, "24px");

			ganttChart.rightHeaderPanel.setText(1, i, DateUtil.dayFormat.format(firstStartDateClone));
			ganttChart.rightHeaderPanel.setText(2, i, getDayName(i));
			firstStartDateClone = DateUtil.addDays(firstStartDateClone, 1);
        }
    }

    public void drawDependencies(ArrayList<TaskSingleItem> tasks) {
        for (TaskSingleItem task : tasks) {
            Point[] path = null;
            TaskWidget taskWidget = ganttChart.taskWidgets.get("t_"+task.getObjectID());
            if (taskWidget != null) {
                if (task.getPredecessorTasks() != null && task.getPredecessorTasks().length> 0) {
					Rectangle toRect = new Rectangle(taskWidget.getLeft(), taskWidget.getTop(), taskWidget.getWidth(), taskWidgetHeight);
					if (task.getWorkstreamID() != null) {
						GanttTreeItem wsItem1 = ganttChart.taskItems.get("w"+task.getWorkstreamID());
						boolean open = true;
						GanttTreeItem testItem = wsItem1;
						while (testItem.getParentItem() != null) {
							if (!testItem.getParentItem().isOpen()) {
								open = false;
								break;
							}
							testItem = testItem.getParentItem();
						}
						if (open) {
							for (int k=0; k<task.getPredecessorTasks().length; k++) {
								GanttTreeItem wsItem2 = ganttChart.taskItems.get("t"+task.getPredecessorTasks()[k].getId());
								if (wsItem2 != null && wsItem2.getUserObject() != null) {
									if (wsItem2.getParentItem() == null || wsItem2.getParentItem() != null && wsItem2.getParentItem().isOpen()
										&& wsItem1.isOpen()) {
										drawConnectors(task, toRect, task.getPredecessorTasks()[k]);
									}
								}
							}
						}
					} else {
						for (int k=0; k<task.getPredecessorTasks().length; k++) {
							GanttTreeItem wsItem2 = ganttChart.taskItems.get("t"+task.getPredecessorTasks()[k].getId());
							if (tasks != null && wsItem2 != null) {
								drawConnectors(task, toRect, task.getPredecessorTasks()[k]);
							}
						}
					}
				}
            }
        }
    }

	private void drawConnectors(TaskSingleItem task, Rectangle toRect, SelectItem selectItem) {
		TaskWidget fromTask = ganttChart.taskWidgets.get("t_"+selectItem.getId());
		if (fromTask != null) {
			Rectangle fromRect = new Rectangle(fromTask.getLeft(), fromTask.getTop(), fromTask.getWidth(), taskWidgetHeight);
			PredecessorType type = PredecessorType.FS;
			if (fromRect != null && toRect != null) {
				Point[] path = CalculatorFactory.get(type).calculateWithOffset(fromRect, toRect);
				String key = task.getObjectID() + "-" + selectItem.getId();
				renderConnector(path, "p_" + key);
				GanttChartDragDropController.predecessorPaths.put(key, getSVGPath(path));
			}
		}
	}
}
