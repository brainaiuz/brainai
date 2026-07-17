package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Paragraph;


public class ReportTypeWidget extends Composite {
    interface ReportTypeUiBinder extends UiBinder<Widget, ReportTypeWidget> {
    }

    private static ReportTypeUiBinder ourUiBinder = GWT.create(ReportTypeUiBinder.class);
    @UiField
    KpiRadioButton summaryRadioButton;
    @UiField
    Paragraph summaryReportsInfo;
    @UiField
    Paragraph tabularReportsInfo;
    @UiField
    KpiRadioButton tabularRadioButton;
    @UiField
    Div summaryBlock;
    @UiField
    Div tabularBlock;

    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private Command valueChangeCommand;

    public ReportTypeWidget(ReportType type) {
        initWidget(ourUiBinder.createAndBindUi(this));
        tabularReportsInfo.setText(reportingStrings.tabularReportsInfo());
        summaryReportsInfo.setText(reportingStrings.summaryReportsListYourDataInformation());
        tabularRadioButton.setText(reportingStrings.tabularReport());
        summaryRadioButton.setText(reportingStrings.summaryReport());
        if (ReportType.SUMMARY.equals(type)) {
            summaryRadioButton.setValue(true);
        } else if (ReportType.TABULAR.equals(type)){
            tabularRadioButton.setValue(true);
        }
        initValueChangeHandler();
    }

    private void initValueChangeHandler() {
        tabularRadioButton.addValueChangeHandler((e) -> {
            handleValueChangeEvent();
        });
        summaryRadioButton.addValueChangeHandler((e) -> {
            handleValueChangeEvent();
        });
        tabularBlock.addClickHandler((event) -> {
            tabularRadioButton.setValue(true);
            summaryRadioButton.setValue(false);
            handleValueChangeEvent();
        });
        summaryBlock.addClickHandler((event) -> {
            summaryRadioButton.setValue(true);
            tabularRadioButton.setValue(false);
            handleValueChangeEvent();
        });
    }

    private void handleValueChangeEvent() {
        if (valueChangeCommand != null) {
            valueChangeCommand.execute();
        }
    }

    public void setValueChangeCommand(Command valueChangeCommand) {
        this.valueChangeCommand = valueChangeCommand;
    }

    public ReportType getValue() {
        if (tabularRadioButton.getValue()) {
            return ReportType.TABULAR;
        } else if (summaryRadioButton.getValue()) {
            return ReportType.SUMMARY;
        }
        return null;
    }

}
