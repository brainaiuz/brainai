package com.edatasite.workforce.gwt.profile.client.ui.view.workflow.actions;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.TelegramReportingRecurrenceView;

import java.util.LinkedList;

public class SaveReportScheduleViewSinksContainer extends SinksContainer {
    public SaveReportScheduleViewSinksContainer(String name, String description, String[] params) {
        super(name, description, params, CLOSE);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String ruleName = null;
        if (id == null && params != null && params.length > 1 && params[1] != null && !"".equals(params[1])) {
            try {
                id = Integer.parseInt(params[1]);
                if (params.length > 1 && !params[2].equals(wfmStrings.telegramAlert())) {
                    ruleName = params[2];
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        addView(new TelegramReportingRecurrenceView(id, ruleName));
    }
}
