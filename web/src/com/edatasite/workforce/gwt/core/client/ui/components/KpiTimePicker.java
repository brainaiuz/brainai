package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 26.09.2018 17:46
 */
public class KpiTimePicker extends Input {

    private Command changeCommand;
    private boolean hasInitialize;
    private PopupPanel parentPopup;
    private int[] time;

    // @hasInitialize - Initialize after adding to DOM
    public KpiTimePicker(boolean hasInitialize) {
        super();
        this.hasInitialize = hasInitialize;

        if (this.hasInitialize) {
            this.initialize();
        }
    }

    public KpiTimePicker(boolean hasInitialize, PopupPanel parent) {// Use only if 'u want to use KpiTimePicker in popup
        super();
        this.hasInitialize = hasInitialize;
        this.parentPopup = parent;

        if (this.hasInitialize) {
            this.initialize();
        }
    }

    public void initialize() {
        Element element = this.getElement();


        this.initializeNative(element);

        this.showWidgetEvent(element);

        this.hideWidgetEvent(element);

        this.updateEvent(element);

        if (this.time != null) {
            this.insertValue(time);
        }
    }

    private native void initializeNative(Element element) /*-{
        $wnd.$(element).timepicker({
            minuteStep: 5,
            showMeridian: false,
            maxHours: 24,
            icons: {
                up: 'ficon--keyboard-arrow-up',
                down: 'ficon--keyboard-arrow-down'
            },
        });

    }-*/;

    private native void showWidgetEvent(Element element) /*-{
        var that = this;
        $wnd.$(element).timepicker().on('show.timepicker', function (e) {
            $wnd.$(element).addClass('timepicker__show');
            that.@com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker::onTimePickerOpen()();
        });
    }-*/;

    private native void hideWidgetEvent(Element element) /*-{
        var that = this;
        $wnd.$(element).timepicker().on('hide.timepicker', function (e) {
            $wnd.$(element).removeClass('timepicker__show');
            that.@com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker::onTimePickerClose()();
        });
    }-*/;

    private native void updateEvent(Element element) /*-{
        var that = this;
        $wnd.$(element).timepicker().on('changeTime.timepicker', function(e) {
            that.@com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker::changeValueHandler()();
        });
    }-*/;

    private void changeValueHandler() {
        if (changeCommand != null) {
            changeCommand.execute();
        }
    }

    public void setValue(int[] time) {
        if (this.hasInitialize) {
            this.insertValue(time);
        } else {
            this.time = time;
        }
    }

    private void insertValue(int[] time) {
        StringBuilder value = new StringBuilder();
        if (time[0] < 10) {
            value.append("0" + time[0]);
        } else {
            value.append(time[0]);
        }
        value.append(":");

        if (time[1] < 10) {
            value.append("0" + time[1]);
        } else {
            value.append(time[1]);
        }
        setValueNative(this.getElement(), value.toString());
    }

    private native void setValueNative(Element elementId, String value) /*-{
        $wnd.$(elementId).timepicker('setTime', value);
    }-*/;

    public int[] getValue() {
        int[] result = new int[2];
        String value = getValueNative(this.getElement());
        String[] hm = value.split(":");
        result[0] = Integer.parseInt(hm[0]);
        result[1] = Integer.parseInt(hm[1]);
        return result;
    }

    private native String getValueNative(Element elementId) /*-{
        return $wnd.$(elementId).val();
    }-*/;

    public static int[] getHoursAndMinutes(Date date) {
        return DateUtil.getHoursAndMinutes(date);
    }

    public void setChangeCommand(Command changeCommand) {
        this.changeCommand = changeCommand;
    }

    private void onTimePickerOpen() {
        if (parentPopup != null) {
            RootPanel.get().addStyleName("cal-popup--opened");
            parentPopup.setAutoHideEnabled(false);
            parentPopup.setModal(false);
        }
    }

    private void onTimePickerClose() {
        if (parentPopup != null) {
            RootPanel.get().removeStyleName("cal-popup--opened");
            parentPopup.setAutoHideEnabled(true);
            parentPopup.setModal(true);
        }
    }
}
