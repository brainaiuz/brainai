package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

public class LeaveAllowanceBalanceWidget extends HorizontalPanel {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer id;
    private TextBox dayBox, fromDayBox, toDayBox;
    private HTML daysForLabel, fromLabel, toLabel, daysLabel;

    public LeaveAllowanceBalanceWidget() {
        super();
        init();
    }

    private void init() {
        daysForLabel = new HTML(wfmStrings.daysPerYear());
        fromLabel = new HTML(wfmStrings.from());
        toLabel = new HTML(wfmStrings.to());
        daysLabel = new HTML(wfmStrings.days());
        dayBox = new TextBox();
        dayBox.setWidth("50px");
        fromDayBox = new TextBox();
        fromDayBox.setWidth("50px");
        toDayBox = new TextBox();
        toDayBox.setWidth("50px");
        Validation.addNumericKeyboardListener(dayBox, 1);
        Validation.addNumericKeyboardListener(fromDayBox, 0);
        Validation.addNumericKeyboardListener(toDayBox, 0);
        add(dayBox);
        add(daysForLabel);
        add(fromLabel);
        add(fromDayBox);
        add(daysLabel);
        add(toLabel);
        add(toDayBox);
        add(daysLabel);
        setSpacing(5);
    }

    public TextBox getDayBox() {
        return dayBox;
    }


    public TextBox getFromDayBox() {
        return fromDayBox;
    }


    public TextBox getToDayBox() {
        return toDayBox;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
