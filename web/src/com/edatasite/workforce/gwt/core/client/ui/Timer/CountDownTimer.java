package com.edatasite.workforce.gwt.core.client.ui.Timer;

import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.html.Span;

public class CountDownTimer extends Composite {
    private static CountDownTimerUiBinder ourUiBinder = GWT.create(CountDownTimerUiBinder.class);
    @UiField
    MaterialLabel hoursLabel;
    private Span timerLabel;
    private Timer timer;

    public CountDownTimer() {
    }

    public CountDownTimer(String time) {
        initWidget(ourUiBinder.createAndBindUi(this));
        drawPanel(time);
    }

    private void drawPanel(String time) {
        String[] hourAndSeconds = time.split(",");
        timerLabel = new Span();
        timer = new Timer() {
            int hour = Integer.parseInt(hourAndSeconds[0]);
            int minutes = Integer.parseInt(hourAndSeconds[1]);
            int seconds = hourAndSeconds.length >= 3 ? Integer.parseInt(hourAndSeconds[2]) : 0;

            @Override
            public void run() {
                String displayHours = hour < 10 ? "0" + hour : String.valueOf(hour);
                String displayMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
                String displaySeconds = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
                String display = displayHours + " : " + displayMinutes + " : " + displaySeconds;
                timerLabel.setText(display);

                if (hour == 0 && minutes == 0 && seconds == 0) {
                    this.cancel();
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMER_ADDED, null, timerLabel);
                }
                if (seconds == 0) {
                    if (minutes == 0) {
                        hour--;
                        minutes = 60;
                        seconds = 60;
                    }
                    minutes--;
                    seconds = 60;
                }
                seconds--;
            }
        };
        timer.scheduleRepeating(1000);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BEFORE_REMOVE_TAB, this, (sender, args) -> {
            timer.cancel();
        });
        hoursLabel.add(timerLabel);
    }

    public void cancel() {
        this.timer.cancel();
    }

    interface CountDownTimerUiBinder extends UiBinder<HTMLPanel, CountDownTimer> {
    }
}