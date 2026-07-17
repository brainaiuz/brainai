package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 11/18/12
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskSummaryPopup {

    private DecoratedPopupPanel popupPanel;
    private FlexTable content;
    private HTML name;
    private HTML description;
    private HTML startDate;
    private HTML endDate;
    private FlexTable assignees;
	private ScrollPanel scrollPanel;
    private TaskSingleItem item;

    public TaskSummaryPopup(TaskSingleItem taskItem) {
        this.item = taskItem;
        onInitialize();
    }

    private void onInitialize() {
        popupPanel = new DecoratedPopupPanel(true);
        popupPanel.setStyleName("popupBorderStyle");
        content = new FlexTable();
        content.setStyleName("popupBodyStyle");

        name = new HTML();
        name.addStyleName(DEFAULT_WIDTH);
        name.setText(item.getName());
        description = new HTML();
        description.addStyleName(DEFAULT_WIDTH);
        description.setText(item.getDescription());
        startDate = new HTML(DateUtils.format(item.getStartDate()));
        endDate = new HTML(DateUtils.format(item.getEndDate()));
        assignees = new FlexTable();
		if (item.getInvolvedMembers() != null && item.getInvolvedMembers().length > 0) {
			for (int i=0; i<item.getInvolvedMembers().length; i++) {
				assignees.setHTML(i, 0, item.getInvolvedMembers()[i].getEmployee() + " &nbsp; &nbsp; &nbsp;");
				FlowPanel percentPanel = new FlowPanel();
				percentPanel.setWidth("100px");
				percentPanel.getElement().getStyle().setBackgroundColor("#0066FF");
				percentPanel.getElement().getStyle().setProperty("border", "1px solid #000099");
				percentPanel.setHeight(Constants.taskWidgetHeight + "px");
				assignees.setWidget(i, 1, percentPanel);
				Float percent = Float.valueOf("0.00");
				if (item.getPercent() != null) {
					percent = item.getInvolvedMembers()[i].getPercent();
				}
				if (item.getInvolvedMembers()[i].getPercent() != null && item.getInvolvedMembers()[i].getPercent() > 0.0f) {
					int divWidth = Math.round(item.getInvolvedMembers()[i].getPercent());
					FlowPanel taskPercentPanel = new FlowPanel();
					taskPercentPanel.setWidth(divWidth + "px");
					taskPercentPanel.getElement().getStyle().setBackgroundColor("#33ff33");
					taskPercentPanel.getElement().getStyle().setProperty("borderRadius", "3px 0 0 3px");
					taskPercentPanel.setHeight(Constants.taskWidgetHeight + "px");
					taskPercentPanel.add(new Label(Constants.defaultNumberFormat.format(percent) + "%"));
					percentPanel.add(taskPercentPanel);
				} else {
					percentPanel.add(new Label(Constants.defaultNumberFormat.format(percent) + "%"));
				}
				assignees.getFlexCellFormatter().setWidth(i, 1, "50px");
			}
		}
		scrollPanel = new ScrollPanel(assignees);
		scrollPanel.getElement().getStyle().setProperty("maxHeight", "102px");

		FlowPanel percentPanel = new FlowPanel();
		percentPanel.setWidth("100px");
		percentPanel.getElement().getStyle().setBackgroundColor("#0066FF");
		percentPanel.getElement().getStyle().setProperty("border", "1px solid #000099");
		percentPanel.getElement().getStyle().setMarginLeft(2d, Style.Unit.PX);
		percentPanel.setHeight(Constants.taskWidgetHeight + "px");
		if (item.getPercent() != null && item.getPercent() > 0.0f) {
			int divWidth = Math.round(item.getPercent());
			FlowPanel taskPercentPanel = new FlowPanel();
			taskPercentPanel.setWidth(divWidth + "px");
			taskPercentPanel.getElement().getStyle().setBackgroundColor("#33ff33");
			taskPercentPanel.getElement().getStyle().setProperty("borderRadius", "3px 0 0 3px");
			taskPercentPanel.setHeight(Constants.taskWidgetHeight + "px");
			Float percent = Float.valueOf("0.00");
			if (item.getPercent() != null) {
				percent = item.getPercent();
			}
			taskPercentPanel.add(new Label(Constants.defaultNumberFormat.format(percent) + "%"));
			percentPanel.add(taskPercentPanel);
		} else {
			percentPanel.add(new Label(item.getPercent().toString() + "%"));
		}

        FlexTable.FlexCellFormatter formatter = content.getFlexCellFormatter();
        formatter.setColSpan(0, 0, 2);
        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        formatter.setWidth(1, 0, "100px");
        formatter.setWidth(1, 1, "200px");
        content.setWidget(0, 0, new HTML("<b>" + Property.get(com.edatasite.workforce.gwt.core.client.ui.Constants.TASK, GanttChart.wfmStrings.taskDetails(), GanttChart.wfmStrings.task()) + "</b>"));
        content.setWidget(1, 0, new HTML("<b>" + GanttChart.wfmStrings.name() + "</b>"));
        content.setWidget(1, 1, name);
        content.setWidget(2, 0, new HTML("<b>" + GanttChart.wfmStrings.startDate() + "</b>"));
        content.setWidget(2, 1, startDate);
        content.setWidget(3, 0, new HTML("<b>" + GanttChart.wfmStrings.dueDate() + "</b>"));
        content.setWidget(3, 1, endDate);
        content.setWidget(4, 0, new HTML("<b>" + GanttChart.wfmStrings.assignees() + "</b>"));
        content.setWidget(4, 1, scrollPanel);
        content.setWidget(5, 0, new HTML("<b>" + GanttChart.wfmStrings.percentCompleted() + "</b>"));
        content.setWidget(5, 1, percentPanel);
        popupPanel.setWidget(content);
    }

    public void show() {
        popupPanel.show();
    }

    public void hide() {
        popupPanel.hide();
    }

    public void setPopupPosition(int left, int top) {
        popupPanel.setPopupPosition(left, top);
    }

    public void setTask(TaskSingleItem item) {
        this.item = item;
        onInitialize();
    }
}
