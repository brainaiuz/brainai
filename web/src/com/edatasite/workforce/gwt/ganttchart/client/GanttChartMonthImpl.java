package com.edatasite.workforce.gwt.ganttchart.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 21.02.13
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartMonthImpl extends GanttChart {

	private GanttChart ganttChart;
	private FlexTable.FlexCellFormatter chartPanelFormatter;
	private FlexTable.FlexCellFormatter backgroundPanelFormatter;
	private int qindex = 0;
	private int mindex = 0;

    public GanttChartMonthImpl(GanttChart ganttChart) {
		this.ganttChart = ganttChart;
    }

    protected void drawRightContent(Date firstStartDate, int daysCount) {
		chartPanelFormatter = ganttChart.rightHeaderPanel.getFlexCellFormatter();
		backgroundPanelFormatter = ganttChart.rightBackgroundPanel.getFlexCellFormatter();
        Date startDate = (Date) firstStartDate.clone();
        Date startDate1 = (Date) firstStartDate.clone();
        Date enddate = (Date) ganttChart.endDate.clone();

        int windex = 0;
        int dindex = 0;
		String styleName = "tableCellStyle";
        for (int i = 0; i < daysCount; i++) {
			if (startDate.before(ganttChart.endDate)) {
				if (qindex == 0) {
					drawFirstLine(startDate, DateUtil.getQuarterName(startDate));
				} else {
					Date firstDateOfQuarter = DateUtil.getFirstDateOfQuarter(startDate);
					if(firstDateOfQuarter.equals(startDate)) {
						drawFirstLine(firstDateOfQuarter, DateUtil.getQuarterName(startDate));
					}
				}
				int daysDiff = DateUtil.getDaysCount(startDate, DateUtil.getFirstDateOfMonth(DateUtil.addMonths(startDate, 1)));
				int monthDaysCount = DateUtil.getMonthDaysCount(startDate);
				if (mindex == 0 && daysDiff < monthDaysCount) {
					drawSecondLine(startDate);
				} else {
					Date firstDateOfMonth = DateUtil.getFirstDateOfMonth(startDate);
					if(firstDateOfMonth.equals(startDate)) {
						drawSecondLine(startDate);
					}
				}

				if (ganttChart.ganttItem.getDayOffs().contains(startDate.getDay())) {
					styleName = "weekEndtableRows";
				} else {
					styleName = "tableCellStyle";
				}
				backgroundPanelFormatter.setStyleName(0, i, styleName + " rightBackgr month");

				if (i%7 == 0) {
					ganttChart.rightHeaderPanel.setWidget(2, windex, new HTML("Week" + (windex+1)));
					chartPanelFormatter.setStyleName(2, windex, "tableCellStyle month");
					chartPanelFormatter.getElement(2, windex).getStyle().setWidth((double) (7 * getCellSize()), Style.Unit.PX);
					chartPanelFormatter.setColSpan(2, windex, 7);
					startDate1 = DateUtil.addDays(startDate1, 7);
					windex++;
				}
				startDate = DateUtil.addDays(startDate, 1);
			} else {
				break;
			}
		}
    }

	private void drawFirstLine(Date startDate, String text) {
		HTML dateString = new HTML(text != null ? text : "");
		dateString.getElement().getStyle().setFontSize(13d, Style.Unit.PX);
		ganttChart.rightHeaderPanel.setWidget(0, qindex, dateString);
		chartPanelFormatter.setStyleName(0, qindex, "tableCellStyle");
		Date lastDateOfQuarter = DateUtil.lastDateOfTheDay(DateUtil.getLastDateOfQuarter(startDate));
		int colspan = DateUtil.getDaysCount(startDate, lastDateOfQuarter.before(ganttChart.endDate) ? lastDateOfQuarter : ganttChart.endDate);
		chartPanelFormatter.setColSpan(0, qindex, colspan);
		qindex++;
	}

	private void drawSecondLine(Date startDate) {
		HTML dateString = new HTML(DateUtil.monthFormat.format(startDate));
		dateString.getElement().getStyle().setFontSize(13d, Style.Unit.PX);
		ganttChart.rightHeaderPanel.setWidget(1, mindex, dateString);
		chartPanelFormatter.setStyleName(1, mindex, "tableCellStyle");
		Date lastDateOfTheMonth = DateUtil.lastDateOfTheDay(DateUtil.getLastDateOfMonth(startDate));
		int colspan = DateUtil.getDaysCount(startDate, lastDateOfTheMonth.before(ganttChart.endDate) ? lastDateOfTheMonth : ganttChart.endDate);
		chartPanelFormatter.setColSpan(1, mindex, colspan);
		mindex++;
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
							Rectangle fromRect = new Rectangle(fromTask.getLeft(), fromTask.getTop(), fromTask.getWidth(), taskWidgetHeight);
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
