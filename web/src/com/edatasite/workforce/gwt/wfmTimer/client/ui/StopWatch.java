package com.edatasite.workforce.gwt.wfmTimer.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: Aug 11, 2010
 * Time: 2:09:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class StopWatch extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DateTimeFormat timerFormat = DateTimeFormat.getFormat("HH:mm:ss");
    private Label hoursLabel;
    private Button start;
    private Button reset;

    private final Date timerDate = new Date();
    //    private ClockItem item;
    private Timer timer;
    private boolean timerIsStarted = false;
    private Date startedTime;

    public StopWatch() {
        init();
    }

    protected Widget init() {
        hoursLabel = new Label();
        hoursLabel.setStyleName("wfmTimerHours");

        start = new Button(wfmStrings.start());
        start.setStyleName("wfmTimer-startButton");
        start.addClickHandler(event -> startTimer());

        reset = new Button(wfmStrings.reset());
        reset.setStyleName("wfmTimer-buttons");
        reset.addClickHandler(event -> reset());

        FlexTable buttonsPanel = new FlexTable();
        buttonsPanel.setWidget(0, 1, start);
        buttonsPanel.setWidget(0, 2, reset);

        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);
        resetHoursLabel();
        timer = new Timer() {
            @Override
            public void run() {
                timerDate.setSeconds(timerDate.getSeconds() + 1);
                hoursLabel.setText(timerFormat.format(timerDate));
            }
        };
        VerticalPanel v = new VerticalPanel();
        v.add(hoursLabel);
        v.add(buttonsPanel);
        initWidget(v);
        return null;
    }

    private void reset() {
        timer.cancel();
        start.setText(wfmStrings.start());
        reset.setEnabled(false);
        resetHoursLabel();
        startedTime = null;
    }

    public static Integer parseMinutes(String minutes) throws NumberFormatException, StringIndexOutOfBoundsException {
        if (minutes == null || minutes.equals("")) {
            return 0;
        }
        String[] parts = minutes.split(":");
        int h = 0;
        int m = 0;
        int s = 0;
        if (parts.length > 2) {
            h = Integer.parseInt(parts[0]);
            m = Integer.parseInt(parts[1]);
            s = Integer.parseInt(parts[2]);
        } else if (parts.length > 1) {
            m = Integer.parseInt(parts[0]);
            s = Integer.parseInt(parts[1]);
        }

        if (m >= 60) {
            h += m / 60;
            m = m % 60;
        }
        if (s >= 30) {
            m++;
        }
        return h * 60 + m;
    }

    public void resetHoursLabel() {
        timerDate.setHours(0);
        timerDate.setMinutes(0);
        timerDate.setSeconds(0);
        hoursLabel.setText(timerFormat.format(timerDate));
    }

    public void startTimer() {
        if (timerIsStarted) {
            timer.cancel();
            reset.setEnabled(true);
            start.setText(wfmStrings.start());
            timerIsStarted = false;
            startedTime = null;
        } else {
            timer.scheduleRepeating(1000);
            timerIsStarted = true;
            start.setText(wfmStrings.stop());
            reset.setEnabled(false);
            startedTime = new Date();
        }
    }

    public void setObjectId() {
    }

    public void setEntityType() {
    }

    public boolean isTimerStarted() {
        return timerIsStarted;
    }

    public Date getStartedTime() {
        return startedTime;
    }

}
