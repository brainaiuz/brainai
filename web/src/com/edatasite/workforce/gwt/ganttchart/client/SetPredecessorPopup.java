package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GCWorkstreamItem;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ilxom Lutfullaev on 25.04.14.
 */
public class SetPredecessorPopup extends KpiModal implements Constants {

	private WfmStrings wfmStrings = WfmStrings.App.get();
	private WfmMessages wfmMessages = WfmMessages.App.get();
	private GanttChartServiceAsync chartService = GanttChartService.App.get();
	private FlexTable table;
	private GanttItem ganttItem;
	private TaskSingleItem item;
	private HashMap<Integer, Integer> taskIDs = new HashMap<>();
	private int depth = 0;
	private String action;

	public SetPredecessorPopup(TaskSingleItem item, GanttItem ganttItem, String action) {
		this.item = item;
		this.ganttItem = ganttItem;
		this.action = action;

		initialize();
		setWidth(490);
		setHeight(247);
		add(table);
	}

	private void initialize() {
		if (SET_PREDECESSOR.equals(action)) {
			setTitle(wfmMessages.setPredecessorTo(item.getName()));
		} else {
			setTitle(wfmMessages.setSuccessorTo(item.getName()));
		}
		table = new FlexTable();
		table.getElement().getStyle().setOverflowY(Style.Overflow.SCROLL);
		table.setHeight("200px");

		chartService.getGanttChart(ganttItem, new AbstractAsyncCallback<GanttItem>() {
			@Override
			public void failure(Throwable throwable) {
				super.failure(throwable);
			}

			@Override
			public void success(GanttItem result) {
				drawTable(result);
			}
		});
	}

	private void drawTable(GanttItem result) {
		int row = 0;
		for (GCWorkstreamItem workstream : result.getSubWorkstreams()) {
			row = recursivelyDrawWorkSreams(workstream, row);
		}
		for (TaskSingleItem task : result.getTasks()) {
			row = drawTask(row, task);
		}
	}

	private int recursivelyDrawWorkSreams(final GCWorkstreamItem workstream, int row) {
		HTML html = new HTML("<b>" + workstream.getName() + "</b>");
		if ("ar".equals(ganttItem.getLocale())) {
			html.getElement().getStyle().setMarginRight((-1) * 10 * depth, Style.Unit.PX);
		} else {
			html.getElement().getStyle().setMarginLeft(10 * depth, Style.Unit.PX);
		}
		table.setWidget(row++, 0, html);
		ArrayList<TaskSingleItem> tasks = workstream.getTasks();
		if (tasks != null && !tasks.isEmpty()) {
			depth++;
			for (TaskSingleItem task : tasks) {
				if (!task.getObjectID().equals(item.getObjectID())) {
					row = drawTask(row, task);
				}
			}
			depth--;
		}
		if (!workstream.getSubWorkstreams().isEmpty()) {
			depth++;
			for (GCWorkstreamItem subItem : workstream.getSubWorkstreams()) {
				recursivelyDrawWorkSreams(subItem, row);
			}
			depth--;
		}
		return row;
	}

	private int drawTask(int row, final TaskSingleItem task) {
		taskIDs.put(row, task.getObjectID());
		RadioButton rb = new KpiRadioButton("rb", task.getName());
		if ("ar".equals(ganttItem.getLocale())) {
			rb.getElement().getStyle().setMarginRight((-1) * 10 * depth, Style.Unit.PX);
		} else {
			rb.getElement().getStyle().setMarginLeft(10 * depth, Style.Unit.PX);
		}
		final int finalRow = row;
		rb.addClickHandler(event -> {
            final Integer[] ids = new Integer[2];
            if (SET_PREDECESSOR.equals(action)) {
                ids[0] = item.getObjectID();
                ids[1] = taskIDs.get(finalRow);
            } else {
                ids[0] = taskIDs.get(finalRow);
                ids[1] = item.getObjectID();
            }
            chartService.saveTaskDependency(item.getObjectID(), taskIDs.get(finalRow), action, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    if (SET_PREDECESSOR.equals(action)) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
} else {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
}
                }

                @Override
                public void success(Void result) {
                    if (SET_PREDECESSOR.equals(action)) {
						Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.predeccessor()));
                    } else {
						Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.successor()));
                    }
                    close();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_PREDECESSOR_CHANGE, ids, SetPredecessorPopup.this);
                }
            });
        });
		table.setWidget(row++, 0, rb);
		return row;
	}
}
