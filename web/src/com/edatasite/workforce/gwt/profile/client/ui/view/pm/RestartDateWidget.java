package com.edatasite.workforce.gwt.profile.client.ui.view.pm;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.view.ReminderView.initializeDayOfMonth;
import static com.edatasite.workforce.gwt.core.client.ui.view.ReminderView.initializeMonthName;

public class RestartDateWidget extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private VerticalPanel verticalPanel;
    private KpiCheckBox restartCheckBox;
    private DataListBox dayOfMonth;
    private DataListBox monthsName;
    private InputGroup inputGroup;

    public RestartDateWidget() {
        this.initialize();
    }

    private void initialize() {

        FlexTable restartNumber = new FlexTable();
        getRestartNumber();
        restartNumber.setWidget(0, 0, restartCheckBox);
        HTML restartNumberingHtmlP = new HTML(wfmStrings.restartNumeringEveryYearOn() + "&nbsp;&nbsp;");
        restartNumber.setWidget(0, 1, restartNumberingHtmlP);
        restartNumber.addStyleName("table-checkbox");
        restartNumberingHtmlP.addStyleName("table-checkbox__label");

        dayOfMonth = new DataListBox();
        monthsName = new DataListBox();

        initializeDayOfMonth(dayOfMonth);
        initializeMonthName(monthsName);

        inputGroup = new InputGroup(dayOfMonth, monthsName);
        inputGroup.setVisible(false);

        restartNumber.setWidget(0, 2, inputGroup);
        initWidget(restartNumber);
    }

    public Date getSelectedDate() {
        if (restartCheckBox.getValue()) {
            if (monthsName.getSelectedId() != null && dayOfMonth.getSelectedId() != null) {
                Date selectedDate = new Date();
                selectedDate.setMonth(monthsName.getSelectedId() - 1);
                selectedDate.setDate(dayOfMonth.getSelectedId());
                if (selectedDate.before(new Date())) {
                    selectedDate.setYear(selectedDate.getYear() + 1);
                }
                return selectedDate;
            }
        }
        return null;
    }

    public void setSelectedDate(Date selectedDate) {
        if (selectedDate != null) {
            restartCheckBox.setValue(true);
            setVisibleDate(selectedDate.getDate(), selectedDate.getMonth() + 1);
        }
    }

    public KpiCheckBox getRestartNumber() {
        restartCheckBox = new KpiCheckBox();
        restartCheckBox.addClickHandler(clickEvent -> {
            if (restartCheckBox.getValue()) {
                setVisibleDate(1, 1);

            } else {
                inputGroup.setVisible(false);
            }
        });
        return restartCheckBox;
    }

    private void setVisibleDate(int dayOfMont, int month) {
        inputGroup.setVisible(true);
        dayOfMonth.setSelected(dayOfMont);
        monthsName.setSelected(month);
    }
}
