package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.ganttchart.client.treetable.GanttTreeItem;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 4, 2011
 * Time: 10:44:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskWidget extends FlowPanel implements Constants {

	private GanttChart ganttChart;
	private boolean resize = false;
	private boolean move = false;
	private com.google.gwt.dom.client.Element movingPanelElement;
    private Integer top, left, width, dragStartX, mouseStartX;
    private TaskSingleItem task;
    private TaskSummaryPopup popup;
	private int order = 0;

    public TaskWidget(GanttChart ganttChart, TaskSingleItem task, int order) {
		this.ganttChart = ganttChart;
        this.task = task;
		this.order = order;
		int days = DateUtil.getDaysCount(ganttChart.startDate, task.getStartDate(), true);
		this.top = order * (cellSize) + order + 5;
		this.left = ganttChart.getCellSize() * days;
		this.width = (ganttChart.getCellSize()) * (DateUtil.getDaysCount(task.getStartDate(), task.getEndDate())) - 1;
		if (this.width <= 0) {
			this.width = ganttChart.getCellSize();
		}
        getTaskCanvas();
    }

    private FlowPanel getTaskCanvas() {
        getElement().setId("t_" + String.valueOf(task.getObjectID()));
		//Window.alert("getTaskCanvas() method: setWidth:" + Math.round(width));
		setWidth(Math.round(width) + "px");
		//Window.alert("getTaskCanvas(): getElement().getStyle().setTop(Math.round(top)): " + Math.round(top));
		getElement().getStyle().setTop(Math.round(top), Style.Unit.PX);
		//Window.alert("getTaskCanvas(): getElement().getStyle().setLeft(Math.round(left)): " + Math.round(left));
		getElement().getStyle().setLeft(Math.round(left), Style.Unit.PX);
		if (task.isWorkstream()) {
			setStyleName("workstreamStyle");
			SimplePanel arrowLeft = new SimplePanel();
			arrowLeft.setStyleName("arrowLeft");
			Image image1 = new Image();
			arrowLeft.add(image1);
			add(arrowLeft);

			SimplePanel arrowRight = new SimplePanel();
			arrowRight.setStyleName("arrowRight");
			Image image2 = new Image();
			arrowRight.add(image2);
			add(arrowRight);
		} else {
			setStyleName("taskStyle");
			//Window.alert("getTaskCanvas(): getElement().getStyle().setLeft(Math.round(left-1)): " + Math.round(left-1));
			getElement().getStyle().setLeft(Math.round(left-1), Style.Unit.PX);
			setHeight(taskWidgetHeight + "px");
			if (task.getPercent() != null && task.getPercent() > 0.0f) {
				int divWidth = Math.round(width * task.getPercent()/100);
				FlowPanel percentPanel = new FlowPanel();
				percentPanel.getElement().setId("pp_" + task.getObjectID());
				//Window.alert("getTaskCanvas(): percentPanel.setWidth(): setWidth:" + divWidth);
				percentPanel.setWidth(divWidth + "px");
				percentPanel.getElement().getStyle().setBackgroundColor("#33ff33");
				percentPanel.getElement().getStyle().setProperty("borderRadius", "3px 0 0 3px");
				percentPanel.setHeight(taskWidgetHeight + "px");
				add(percentPanel);
			}
			if (ganttChart.isShowActualBox() && task.getActualStartDate() != null && task.getActualEndDate() != null) {
				int days = DateUtil.getDaysCount(ganttChart.startDate, task.getActualStartDate(), true);
				int divTop = Math.round(25 * order + 7);
				int divLeft = Math.round(ganttChart.getCellSize() * days);
				int divWidth = Math.round((ganttChart.getCellSize()) * (DateUtil.getDaysCount(task.getActualStartDate(), task.getActualEndDate())) - 1);
				if (divWidth <=0) {
					divWidth = ganttChart.getCellSize();
				}
				FlowPanel percentPanel = new FlowPanel();
				percentPanel.getElement().setId("ap_" + task.getObjectID());
				percentPanel.setStyleName("actualBarStyle");
				//Window.alert("getTaskCanvas(): percentPanel.setWidth(): setWidth:" + divWidth);
				percentPanel.setWidth(divWidth + "px");
				//Window.alert("getTaskCanvas(): percentPanel.getElement().getStyle().setTop(divTop): " + divTop);
				percentPanel.getElement().getStyle().setTop(divTop, Style.Unit.PX);
				//Window.alert("getTaskCanvas(): percentPanel.getElement().getStyle().setLeft(divLeft): " + divLeft);
				percentPanel.getElement().getStyle().setLeft(divLeft, Style.Unit.PX);
				ganttChart.boundaryPanel.add(percentPanel);
			}
		}
		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.MOUSEEVENTS | Event.ONDBLCLICK);
        return this;
    }

	public void onBrowserEvent(Event event) {
		if (Utils.hasPermission(PermissionConstants.GANTTCHART_EDIT_PERMISSION)) {
			final int eventType = DOM.eventGetType(event);
			setMovingPanelElement(getElement());
			if (Event.ONDBLCLICK == eventType) {
				TaskSummaryPopup popup = new TaskSummaryPopup(task);
				int left = event.getClientX();//DOM.getAbsoluteLeft(DOM.eventGetTarget(event)) + 20;
				int top = DOM.getAbsoluteTop(DOM.eventGetTarget(event)) + 20;
				popup.setPopupPosition(left, top);
				popup.show();
			}
			if (Event.ONMOUSEOVER == eventType) {
				if (isCursorResize(event)) {
					DOM.setStyleAttribute(this.getElement(), "cursor", "ew-resize");
				} else if (isCursorMove(event)) {
					DOM.setStyleAttribute(this.getElement(), "cursor", "all-scroll");
				} else {
					DOM.setStyleAttribute(this.getElement(), "cursor", "default");
				}
			}

			if (Event.ONMOUSEDOWN == eventType) {
				if (isCursorResize(event)) {
					//enable/disable resize
					if (!resize) {
						resize = true;
						DOM.setCapture(this.getElement());
					}
				} else if (isCursorMove(event) && !move) {
					DOM.setCapture(this.getElement());
					move = true;
					dragStartX = event.getClientX();
					mouseStartX = getLeft();
				}
			} else if (Event.ONMOUSEMOVE == eventType) {
				//reset cursor-type
				if (!isCursorResize(event) && !isCursorMove(event)) {
					DOM.setStyleAttribute(this.getElement(), "cursor", "default");
				}

				//calculate and set the new size
				if (resize) {
					int absX = DOM.eventGetClientX(event);
					int absY = DOM.eventGetClientY(event);
					int originalX = DOM.getAbsoluteLeft(this.getElement());
					int originalY = DOM.getAbsoluteTop(this.getElement());

					//do not allow mirror-functionality
					if (absY > originalY && absX > originalX) {
						Integer width = absX - originalX + 2;
						//Window.alert("onBrowserEvent(): Event.ONMOUSEMOVE == eventType: setWidth:" + width);
						setWidth(width);
					}
				} else if (move) {
					int difference = event.getClientX() - dragStartX;
					setLeft((mouseStartX + difference) > 0 ? mouseStartX + difference : 0);
				}
			} else if (Event.ONMOUSEUP == eventType) {
				move = false;
				resize = false;
				DOM.releaseCapture(this.getElement());

				Integer width1 = Integer.valueOf(DOM.getStyleAttribute(getElement(), "width").replace("px", ""));
				int cellSize = ganttChart.getCellSize();
				setWidth(((width1 / cellSize + 1) * cellSize) - 2);
				Date taskStartDate = DateUtil.addDays(ganttChart.startDate, getLeft() / cellSize);
				Date taskEndDate = DateUtil.addDays(taskStartDate, width1 / cellSize);
				int daysCount = DateUtil.getDaysCount(ganttChart.startDate, taskStartDate);
				setLeft((daysCount - 1) * cellSize);
				GanttTreeItem ganttTreeItem = ganttChart.taskItems.get("t" + task.getObjectID());
				task.setStartDate(taskStartDate);
				task.setEndDate(taskEndDate);
				ganttTreeItem.setUserObject(task);
				ganttTreeItem.getTreeTable().render(ganttTreeItem);
				ganttChart.taskWidgets.remove("t_" + task.getObjectID());
				ganttChart.taskWidgets.put("t_" + task.getObjectID(), this);
				ganttChart.reDrawDependencies(ganttChart.tasks);
				ganttChart.ganttChartService.saveTaskDates(task.getObjectID(), taskStartDate, taskEndDate, new AbstractAsyncCallback<Void>() {
					@Override
					public void failure(Throwable throwable) {
						super.failure(throwable);
						dragStartX = null;
						mouseStartX = null;
					}

					@Override
					public void success(Void result) {
						super.success(result);
						dragStartX = null;
						mouseStartX = null;
					}
				});
			}
			if (Event.ONMOUSEOUT == eventType) {
				if (move) {
					move = false;
					DOM.releaseCapture(this.getElement());
				}
			}
			event.preventDefault();
		}
	}

	/**
	 * returns if mousepointer is in region to show cursor-resize
	 * @param event
	 * @return true if in region
	 */
	protected boolean isCursorResize(Event event) {
		int cursorY  = DOM.eventGetClientY(event);
		int initialY = this.getAbsoluteTop();
		int height   = this.getOffsetHeight();

		int cursorX  = DOM.eventGetClientX(event);
		int initialX = this.getAbsoluteLeft();
		int width    = this.getOffsetWidth();

		return (((initialX + width - 10) <= cursorX && cursorX <= (initialX + width))) &&
				(initialY <= cursorY && cursorY <= (initialY + height));
	}

	/**
	 * sets the element in panel
	 * @param movingPanelElement
	 */
	public void setMovingPanelElement(com.google.gwt.dom.client.Element movingPanelElement) {
		this.movingPanelElement = movingPanelElement;
	}

	/**
	 * is cursor in moving state?
	 * @param event event to process
	 * @return true if cursor is in movement
	 */
	protected boolean isCursorMove(Event event){
		if(movingPanelElement!=null){
			int cursorY = DOM.eventGetClientY(event);
			int initialY = movingPanelElement.getAbsoluteTop();
			int cursorX = DOM.eventGetClientX(event);
			int initialX = movingPanelElement.getAbsoluteLeft();
			//			if(initialY <= cursorY && initialX <= cursorX)
			return cursorX >= initialX && cursorX <= initialX + getWidth() && cursorY > initialY && cursorY < initialY + taskWidgetHeight + 10;
		}else
			return false;
	}

	public TaskSingleItem getTask() {
        return task;
    }

    public FlowPanel getTaskWidget() {
        return this;
    }

	public Integer getTop() {
		return Math.round(top);
	}

	public void setTop(Integer top) {
		this.top = Math.round(top);
		//Window.alert("setTop(Integer top) method: getElement().getStyle().setTop(): " + this.top);
		getElement().getStyle().setTop(this.top, Style.Unit.PX);
	}

	public Integer getLeft() {
        return Math.round(left);
    }

	public void setLeft(Integer left) {
		this.left = Math.round(left);
		//Window.alert("setLeft(Integer left) method: getElement().getStyle().setLeft(): " + this.left);
		getElement().getStyle().setLeft(this.left, Style.Unit.PX);
		if (task.getPercent() != null && task.getPercent() > 0.0f) {
			Element element = DOM.getElementById("pp_" + task.getObjectID());
			//Window.alert("setLeft(Integer left) method: setLeft(Integer left) method: element.getStyle().setLeft(): " + this.left);
			element.getStyle().setLeft(this.left, Style.Unit.PX);
		}
	}

	public Integer getWidth() {
        return Math.round(width);
    }

	public void setWidth(Integer width) {
		this.width = Math.round(width);
		//Window.alert("setWidth(Integer width) method: setWidth:" + this.width);
		setWidth(this.width + "px");
		if (task.getPercent() != null && task.getPercent() > 0.0f) {
			int divWidth = Math.round(this.width * task.getPercent()/100);
			Element element = DOM.getElementById("pp_" + task.getObjectID());
			//Window.alert("setWidth(Integer width) method: element.getStyle().setWidth(): " + divWidth);
			element.getStyle().setWidth(divWidth, Style.Unit.PX);
		}
	}
}
