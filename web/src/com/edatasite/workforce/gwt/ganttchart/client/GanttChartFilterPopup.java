package com.edatasite.workforce.gwt.ganttchart.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 5/22/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartFilterPopup {

	private final WfmStrings wfmStrings = WfmStrings.App.get();
	private final GanttChart ganttChart;
	private PopupPanel dialogBox;
	private FlexTable form;
    private DatePicker startDatePicker, endDatePicker;
    private DataListBox orderBy;

	private int left = 0, top = 0;

	public GanttChartFilterPopup(GanttChart ganttChart, int left, int top) {
		this.ganttChart = ganttChart;
		this.left = left;
		this.top = top;
		initialize();
	}

	public void initialize() {
		form = new FlexTable();
		dialogBox = new PopupPanel(false);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setAutoHideEnabled(true);
		dialogBox.setStyleName("dialogBoxStyle");

        startDatePicker = new DatePicker(true);
        DOM.setStyleAttribute(startDatePicker.getElement(), "textAlign", "center");
        startDatePicker.addChangeHandler(dateValueChangeEvent -> {
            dialogBox.hide();
            ganttChart.startDate = startDatePicker.getDate();
            ganttChart.redrawTasks(false);
        });
        endDatePicker = new DatePicker(true);
        DOM.setStyleAttribute(endDatePicker.getElement(), "textAlign", "center");
        endDatePicker.addChangeHandler(dateValueChangeEvent -> {
            dialogBox.hide();
            ganttChart.endDate = endDatePicker.getDate();
            ganttChart.redrawTasks(false);
        });

		orderBy = new DataListBox();
		orderBy.getElement().getStyle().setWidth(179d, Style.Unit.PX);
		orderBy.setAllowFirstItem(true);
		orderBy.addListItem(new SelectItem(1, wfmStrings.idAsc(), "id asc"));
		orderBy.addListItem(new SelectItem(2, wfmStrings.idDesc(), "id desc"));
		orderBy.addListItem(new SelectItem(3, wfmStrings.startDateAsc(), "startDate asc"));
		orderBy.addListItem(new SelectItem(4, wfmStrings.startDateDesc(), "startDate desc"));
		orderBy.setSelected(3);
		orderBy.addValueChangeHandler(changeEvent -> {
            dialogBox.hide();
            ganttChart.orderBy = orderBy.getSelectedItem().getDescription();
            ganttChart.redrawTasks(false);
        });
		
		form.setHTML(0, 0, wfmStrings.startDate());
        form.setWidget(0, 1, startDatePicker);
		form.setHTML(1, 0, wfmStrings.endDate());
		form.setWidget(1, 1, endDatePicker);
		form.setHTML(2, 0, wfmStrings.orderBy());
		form.setWidget(2, 1, orderBy);

		dialogBox.add(form);
		dialogBox.getElement().getStyle().setLeft(left, Style.Unit.PX);
		dialogBox.getElement().getStyle().setTop(top, Style.Unit.PX);
	}

	public void show() {
		dialogBox.show();
		dialogBox.getElement().getStyle().setLeft(left, Style.Unit.PX);
		dialogBox.getElement().getStyle().setTop(top, Style.Unit.PX);
	}

	public void setStartDate(Date startDate) {
		startDatePicker.setDate(startDate);
	}

	public void setEndDate(Date endDate) {
		endDatePicker.setDate(endDate);
	}

	public void setLeft(int left) {
		this.left = left;
		dialogBox.getElement().getStyle().setLeft(left, Style.Unit.PX);
	}

	public void setTop(int top) {
		this.top = top;
		dialogBox.getElement().getStyle().setTop(top, Style.Unit.PX);
	}
}