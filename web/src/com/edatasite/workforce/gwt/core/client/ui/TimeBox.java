package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 15, 2010
 * Time: 5:24:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeBox extends TextBox {
	private final NumberFormat numberFormat = NumberFormat.getFormat("00");
	private PopupPanel popup;
	private String time;
	private String styleName;
	private Command command;
	private ListBox listTimeNew;
	final DateTimeFormat timeFormat = DateUtils.getFormatInternal().getShortTimeFormat();
	private HashMap<String, Integer> listItems;

    public TimeBox() {
        this.time="00:00";
        initComponents();
    }

    public TimeBox(String time) {
		this.time = time;
		initComponents();
	}

	public TimeBox(String time, String styleName) {
		this.time = time;
		initComponents();
	}

	private void initComponents() {
		super.setText(time);
		super.setReadOnly(true);
		super.setStyleName("input-default-color");
		listItems = new HashMap<>();
		listTimeNew = new ListBox(true);
		listTimeNew.setSize("100%", "100%");
		listTimeNew.addClickHandler(be -> {
            popup.hide();
            setText(listTimeNew.getItemText(listTimeNew.getSelectedIndex()));

            if (command != null) {
                command.execute();
            }
        });

		popup = new PopupPanel(true);
		popup.setWidget(listTimeNew);
		popup.getElement().getStyle().setZIndex(7000);

		setWorkingTime();

		super.addClickHandler(event -> {
            showPopup(((TextBox) event.getSource()));
            int scrollHeight = listTimeNew.getElement().getScrollHeight();
            listTimeNew.getElement().setScrollTop(scrollHeight / listTimeNew.getItemCount() * getItemIndex(getValue()));
        });
	}

	private Date getDateTime(Date date, String time) {
		final int year = date.getYear();
		final int month = date.getMonth();
		final int day = date.getDate();
		String[] timeHour = time.split(":");
		String[] timeMinute = timeHour[1].split("\\ ");
		int hour = Integer.parseInt(timeHour[0]);
		int minute = Integer.parseInt(timeMinute[0]);
		return new Date(year, month, day, hour, minute);
	}

	private void setWorkingTime() {
		int k = 0;
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j <= 45; j += 15) {
				Date date = new Date();
				date.setHours(i);
				date.setMinutes(j);
				String hour = i < 10 ? "0" + i : String.valueOf(i);
				String minute = j < 10 ? "0" + j : String.valueOf(j);
				String formattedTime = hour + ":" + minute;
				listTimeNew.addItem(formattedTime);
				listItems.put(formattedTime, k++);
			}
		}
	}

	private void showPopup(final TextBox textbox) {
		popup.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
			if (offsetHeight + textbox.getOffsetHeight() < Window.getClientHeight() - textbox.getAbsoluteTop()) {
				popup.setPopupPosition(textbox.getAbsoluteLeft(), textbox.getAbsoluteTop() + textbox.getOffsetHeight());
			} else {
				popup.setPopupPosition(textbox.getAbsoluteLeft(), textbox.getAbsoluteTop() - offsetHeight);
			}
		});
	}

	public void onClick(Command command) {
		this.command = command;
	}

	public int parseTimeInMinutes() {
		int hour = Integer.parseInt(time.substring(0, 2));
		int minute = Integer.parseInt(time.substring(3, 5));
		return hour * 60 + minute;
	}

	public int getItemIndex(String value) {
		if (listItems != null && listItems.containsKey(value)) {
			return listItems.get(value);
		}
		return -1;
	}

	public ListBox getListBox() {
		return listTimeNew;
	}
}
