package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;
import java.util.HashMap;

public class StartEndTime extends TextBox {

    private PopupPanel popup;
    private HashMap<String, Integer> listItems;
    private ListBox listTime;
    public String time;
    private Command command;
    final DateTimeFormat timeFormat = DateUtils.getTimeFormatInternal()/*getFormatInternal().getShortTimeFormat()*/;

    public StartEndTime(String time) {
        this.time = time;
        initComponents();
    }

    public void initComponents() {
        super.setText(time);
//        super.setReadOnly(true);
        super.setStyleName("form-control input-default-color");

        listItems = new HashMap<>();
        //listTime
        listTime = new ListBox(true);
        listTime.addStyleName("elm_time-list");
        listTime.setSize("100", "125");
        listTime.addClickHandler(be -> {
            popup.hide();
            setText(listTime.getItemText(listTime.getSelectedIndex()));//listTime.getSelectedItem().getText());

            if (command != null) {
                command.execute();
            }
        });

        popup = new PopupPanel(true);
        popup.setWidget(listTime);

        popup.getElement().getStyle().setZIndex(7000);
        setWorkingTime();

        super.addClickHandler(event -> {
            showPopup(((TextBox) event.getSource()));
            int scrollHeight = listTime.getElement().getScrollHeight();
            listTime.getElement().setScrollTop(scrollHeight / listTime.getItemCount() * getItemIndex(getValue()));
        });
    }

    private void setWorkingTime() {
        //items of ListBox in Time
        int k = 0;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j <= 45; j += 15) {
                Date date = new Date();
                date.setHours(i);
                date.setMinutes(j);
                listTime.addItem(timeFormat.format(date));
                listItems.put(timeFormat.format(date), k++);
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

    public int getItemIndex(String value) {
        if (listItems != null && listItems.containsKey(value)) {
            return listItems.get(value);
        }
        return -1;
    }

    public ListBox getListBox() {
        return listTime;
    }
}
