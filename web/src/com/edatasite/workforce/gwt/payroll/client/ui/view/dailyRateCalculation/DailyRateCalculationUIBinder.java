package com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DailyRateCalculationUIBinder {
    interface IDailyRateCalculationUIBinder extends UiBinder<HTMLPanel, DailyRateCalculationUIBinder> {
    }

    private static final IDailyRateCalculationUIBinder ourUiBinder = GWT.create(IDailyRateCalculationUIBinder.class);
    private final HTMLPanel rootElement;
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();

    @UiField
    WfmButton2 saveButton;
    @UiField
    SpanElement titleLabel;
    @UiField
    FormGroup paymentForm;
    @UiField
    FormGroup paymentFormula;
    @UiField
    FormGroup calendarForm;
    @UiField
    FormGroup employerForm;
    @UiField
    FormGroup workDays;

    private KpiRadioButton calendarType, paymentType, employerType;
    private KpiCheckBox holiday, weekend;
    private TextBox workDaysTextBox;

    private DailyRateSettings localData;

    public DailyRateCalculationUIBinder() {
        rootElement = ourUiBinder.createAndBindUi(this);
    }

    public void init() {
        titleLabel.setInnerText(payrollStrings.dailyRateCalculationOptions());
        paymentType = new KpiRadioButton("type", payrollStrings.byFormula());
        calendarType = new KpiRadioButton("type", payrollStrings.byCalendar());
        employerType = new KpiRadioButton("type", payrollStrings.byEmployerSettings());
        holiday = new KpiCheckBox("Exclude " + wfmStrings.holidays());
        weekend = new KpiCheckBox("Exclude " + hrmsStrings.weekends());
        workDaysTextBox = new TextBox();
        workDaysTextBox.addStyleName(Constants.DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(workDaysTextBox, 0);

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.add(holiday);
        verticalPanel.add(weekend);

        paymentForm.addToContent(paymentType);

        paymentFormula.setLabel("(" + wfmStrings.paymentAmount() + ") * 12 / 365");
        paymentFormula.addToContent(verticalPanel);

        calendarForm.addToContent(calendarType);

        employerForm.addToContent(employerType);

        workDays.setLabel(payrollStrings.numberOfWorkDaysInMonth());
        workDays.addToContent(workDaysTextBox);

        calendarType.addValueChangeHandler(valueChangeEvent -> enableDisableFieldSets(null));
        paymentType.addValueChangeHandler(valueChangeEvent -> enableDisableFieldSets(true));
        employerType.addValueChangeHandler(valueChangeEvent -> enableDisableFieldSets(false));

        PayrollService.App.get().getDailyRateSettings(new AsyncCallback<DailyRateSettings>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(DailyRateSettings dailyRateSettings) {
                localData = dailyRateSettings;
                setValues();
            }
        });

        saveButton.setText(wfmStrings.save());
        saveButton.setStyleName(WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(event -> save());
    }

    private void setValues() {
        if (DailyRateSettings.TYPE_CALENDAR.equals(localData.getDailyRateType())) {
            enableDisableFieldSets(null);

            calendarType.setValue(true);
            paymentType.setValue(false);
            employerType.setValue(false);
        } else if (DailyRateSettings.TYPE_FORMULA.equals(localData.getDailyRateType())) {
            enableDisableFieldSets(true);

            calendarType.setValue(false);
            paymentType.setValue(true);
            employerType.setValue(false);

            weekend.setValue(localData.isExcludeDayOffs());
            holiday.setValue(localData.isExcludeHoliday());
        } else if (DailyRateSettings.TYPE_EMPLOYER_SETTINGS.equals(localData.getDailyRateType())) {
            enableDisableFieldSets(false);

            calendarType.setValue(false);
            paymentType.setValue(false);
            employerType.setValue(true);
            if (localData.getWorkDaysInMonth() != null) {
                workDaysTextBox.setValue(String.valueOf(localData.getWorkDaysInMonth()));
            }
        }
    }


    private void enableDisableFieldSets(Boolean formula) {
        if (formula != null) {
            if (formula) {
                holiday.setEnabled(true);
                weekend.setEnabled(true);
                workDaysTextBox.setEnabled(false);
            } else {
                holiday.setEnabled(false);
                weekend.setEnabled(false);
                workDaysTextBox.setEnabled(true);
            }
        } else {
            holiday.setEnabled(false);
            weekend.setEnabled(false);
            workDaysTextBox.setEnabled(false);
        }
    }

    private void save() {
        if (!validation()) {
            return;
        }
        LoadingPanel.loading(true);
        PayrollService.App.get().saveDailyRateSettings(getData(), new AsyncCallback<TestRPC>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.error(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(TestRPC testRPC) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), payrollStrings.dailyRate()));
            }
        });

    }

    private DailyRateSettings getData() {
        DailyRateSettings settings = new DailyRateSettings();
        if (paymentType.getValue()) {
            settings.setExcludeDayOffs(weekend.getValue());
            settings.setExcludeHoliday(holiday.getValue());
        }
        if (employerType.getValue()) {
            settings.setWorkDaysInMonth(Integer.parseInt(workDaysTextBox.getValue()));
        }
        settings.setDailyRateType(getDailyRateType());
        return settings;
    }

    private String getDailyRateType() {
        String result = null;
        if (calendarType.getValue()) {
            result = DailyRateSettings.TYPE_CALENDAR;
        } else if (paymentType.getValue()) {
            result = DailyRateSettings.TYPE_FORMULA;
        } else if (employerType.getValue()) {
            result = DailyRateSettings.TYPE_EMPLOYER_SETTINGS;
        }
        return result;
    }

    private boolean validation() {
        int errors = 0;
        StringBuilder sb = new StringBuilder();
        if (getDailyRateType() == null) {
            Validation.validateRadioButtonRequired(paymentType);
            sb.append(payrollStrings.selectOneOfDailyRateTypes());
            errors++;
        }
        if (employerType.getValue() && !Validation.validateTextBoxRequired(workDaysTextBox)) {
            sb.append(wfmMessages.notFilled(payrollStrings.numberOfWorkDaysInMonth()));
            errors++;
        }
        if (errors > 0) {
            Info.show(sb.toString(), Info.Type.WARNING);
            return false;
        }
        if (employerType.getValue() && (Integer.parseInt(workDaysTextBox.getValue()) > 31 || Integer.parseInt(workDaysTextBox.getValue()) < 1)) {
            Info.show(payrollStrings.daysInMonthCannotBeAccepted(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public HTMLPanel getRootElement() {
        return rootElement;
    }
}
