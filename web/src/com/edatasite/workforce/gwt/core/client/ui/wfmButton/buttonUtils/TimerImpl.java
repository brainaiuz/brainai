package com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils;

import com.google.gwt.user.client.Timer;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.11.2008
 * Time: 15:44:17
 * To change this template use File | Settings | File Templates.
 */
public class TimerImpl extends Timer {

    private TimerListener timerListener;

    public void addTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public void run() {

        if (timerListener != null) {
            timerListener.timeHasCome();
        }
    }
}
