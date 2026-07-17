package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 21.02.13
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class GanttChartWeekImpl extends GanttChart {

	private GanttChart ganttChart;
	private FlexTable.FlexCellFormatter chartPanelFormatter;
	private FlexTable.FlexCellFormatter backgroundPanelFormatter;
	private int index = 0;

	public GanttChartWeekImpl(GanttChart ganttChart) {
        this.ganttChart = ganttChart;
    }

    protected void drawRightContent(Date firstStartDate, int daysCount) {
		chartPanelFormatter = ganttChart.rightHeaderPanel.getFlexCellFormatter();
		backgroundPanelFormatter = ganttChart.rightBackgroundPanel.getFlexCellFormatter();
        Date startDate = (Date) firstStartDate.clone();
        Date startDate1 = (Date) firstStartDate.clone();
		String fmt = Utils.getShortDateFormat().replace("/yyyy", "");
		fmt = fmt.replace("yyyy/", "");
		fmt = fmt.replace(".yyyy", "");
		fmt = fmt.replace("yyyy.", "");
		fmt = fmt.replace(" yyyy", "");
		fmt = fmt.replace("yyyy ", "");
		fmt = fmt.replace("-yyyy", "");
		fmt = fmt.replace("yyyy-", "");
		fmt = fmt.replace(",", "");
		DateTimeFormat format = DateTimeFormat.getFormat(fmt);
        int weekIndex = 0;
		int daysDiff = DateUtil.getDaysCount(startDate, DateUtil.getFirstDateOfMonth(DateUtil.addMonths(startDate, 1)));
		int monthDaysCount = DateUtil.getMonthDaysCount(startDate);
		String styleName = "tableCellStyle";
		for (int i = 0; i < daysCount; i++) {
			if (index == 0 && daysDiff < monthDaysCount) {
				drawFirstLine(startDate, DateUtil.monthFormat.format(startDate1));
			} else {
				Date firstDateOfMonth = DateUtil.getFirstDateOfMonth(startDate1);
				if(firstDateOfMonth.equals(startDate1)) {
					drawFirstLine(firstDateOfMonth, DateUtil.monthFormat.format(startDate1));
				}
			}

			if (ganttChart.ganttItem.getDayOffs().contains(startDate1.getDay())) {
				styleName = "weekEndtableRows";
			} else {
				styleName = "tableCellStyle";
			}

			ganttChart.rightHeaderPanel.setText(2, i, getDayName(i));
			chartPanelFormatter.setStyleName(2, i, styleName + " week");
			backgroundPanelFormatter.setStyleName(0, i, styleName + " rightBackgr weekly");

			if (i%7 == 0) {
				String formatteddate = format.format(startDate) + " - " + format.format(DateUtil.addDays(startDate, 6));
				ganttChart.rightHeaderPanel.setHTML(1, weekIndex, "Week" + (weekIndex + 1) + "<p style=\"font-size:9px\">" + formatteddate + "</p>");
				chartPanelFormatter.setStyleName(1, weekIndex, styleName);
				chartPanelFormatter.setColSpan(1, weekIndex, 7);
				startDate = DateUtil.addDays(startDate, 7);
				weekIndex++;
			}
			startDate1 = DateUtil.addDays(startDate1, 1);
        }
    }

	private void drawFirstLine(Date startDate, String text) {
		HTML dateString = new HTML(text != null ? text : "");
		dateString.getElement().getStyle().setFontSize(13d, Style.Unit.PX);
		ganttChart.rightHeaderPanel.setWidget(0, index, dateString);
		chartPanelFormatter.setStyleName(0, index, "tableCellStyle");
		Date lastDateOfMonth = DateUtil.getLastDateOfMonth(startDate);
		int colspan = DateUtil.getDaysCount(startDate, lastDateOfMonth.before(ganttChart.endDate) ? lastDateOfMonth : ganttChart.endDate);
		dateString.setWidth(colspan * 7 + "px");
		chartPanelFormatter.setColSpan(0, index, colspan);
		index++;
	}

    /*public void drawDependencies(ArrayList<TaskSingleItem> tasks) {
        for (TaskSingleItem task : tasks) {
            Point[] path = null;
            TaskWidget taskWidget = ganttChart.taskWidgets.get(task.getObjectID());
            if (taskWidget != null) {
                if (task.getPredecessorTasks() != null && task.getPredecessorTasks().length> 0) {
                    Rectangle toRect = new Rectangle(taskWidget.getLeft(), taskWidget.getTop(), taskWidget.getWidth(), ganttChart.taskWidgetHeight);
                    for (int k=0; k<task.getPredecessorTasks().length; k++) {
						GanttTreeItem wsItem2 = ganttChart.taskItems.get(task.getPredecessorTasks()[k].getId());
						if (tasks.contains((TaskSingleItem)wsItem2.getUserObject())) {
							TaskWidget fromTask = ganttChart.taskWidgets.get(task.getPredecessorTasks()[k].getId());
							Rectangle fromRect = new Rectangle(fromTask.getLeft(), fromTask.getTop(), fromTask.getWidth(), ganttChart.taskWidgetHeight);
							PredecessorType type = PredecessorType.FS;
							if (fromRect != null && toRect != null) {
								path = CalculatorFactory.get(type).calculateWithOffset(fromRect, toRect);
								String key = task.getObjectID() + "-" + task.getPredecessorTasks()[k].getId();
								renderConnector(path, "p_" + key);
								GanttChartDragDropController.predecessorPaths.put(key, getSVGPath(path));
							}
						}
                    }
                }
            }
        }
    }*/
}
