package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsScoreTypeItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.ui.SliderBar;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.*;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

/**
 * User: Sherali Pirnafasov
 */
public class AppraisalsSettingsView extends FooteredView {

    private static WfmStrings wfmStrings = WfmStrings.App.get();
    private FormGroup useCompetenciesField;
    private FormGroup useGoalsField;
    private FormGroup scoreScaleField;
    private FormGroup previewField;
    private FormGroup scoreTypesTableField;
    private DynamicTable scoreTypesTable;

    private KpiCheckBox chUseCompetencies;
    private KpiCheckBox chUseGoals;
    private KpiCheckBox chEmployeeRate;

    private TextBox fromScale;
    private TextBox toScale;
    private TextBox stepSize;
    private SliderBar sliderBar;

    private WfmButton2 save;

    private boolean closeTab = false;

    private String appraisals_settings_ = "appraisals_settings_";

    private AppraisalsSettingsItem item;


    public AppraisalsSettingsView() {
        super("appraisalssettings", wfmStrings.appraisalsSettings());
    }

    protected Widget onInitialize() {
        initialize();
        return null;
    }

    private void initialize() {

        chUseCompetencies = new KpiCheckBox();
        chUseCompetencies.addClickHandler(event -> chUseCompetencies.removeStyleName(ERROR_FORM_STYLE));
        chUseGoals = new KpiCheckBox();

        chEmployeeRate = new KpiCheckBox();

        fromScale = new TextBox();
        fromScale.setWidth("60px");
        fromScale.ensureDebugId(appraisals_settings_ + "from_scale");

        toScale = new TextBox();
        toScale.setWidth("60px");
        toScale.ensureDebugId(appraisals_settings_ + "to_scale");

        stepSize = new TextBox();
        stepSize.setWidth("60px");
        stepSize.ensureDebugId(appraisals_settings_ + "step_size");

        useCompetenciesField = new FormGroup(wfmStrings.useCompetenciesForAppraisals(), chUseCompetencies);
        useGoalsField = new FormGroup(wfmStrings.useGoalsForAppraisals(), chUseGoals);
        FormGroup employeeRateGroup = new FormGroup(wfmStrings.enableEmployeeRate(), chEmployeeRate);

        Label toLabel = new Label(wfmStrings.to());
        toLabel.getElement().getStyle().setPadding(5, Style.Unit.PX);

        Label stepPadding = new Label(wfmStrings.stepSize() + ":");
        stepPadding.getElement().getStyle().setPadding(5, Style.Unit.PX);

        scoreScaleField = new FormGroup(wfmStrings.scoreScale(), new InputGroup(fromScale, toLabel, toScale, stepPadding, stepSize));

        sliderBar = new SliderBar(0, 100);
        sliderBar.setWidth("300px");
        sliderBar.setCurrentValue(50);
        sliderBar.setNumTicks(1);
        sliderBar.setStepSize(1);

        fromScale.setText("1");
        fromScale.addChangeHandler(event -> {
            sliderBar.setMinValue(Double.parseDouble(fromScale.getText()));
            sliderBar.redraw();
        });
        toScale.setText("100");
        toScale.addChangeHandler(event -> {
            sliderBar.setMaxValue(Double.parseDouble(toScale.getText()));
            sliderBar.redraw();
        });
        stepSize.setText("1");
        stepSize.addChangeHandler(event -> {
            sliderBar.setStepSize(Double.parseDouble(stepSize.getText()));
            sliderBar.redraw();
        });
        sliderBar.setNumTicks(1);

        previewField = new FormGroup(wfmStrings.preview(), sliderBar);

        scoreScaleField.setVisible(!Utils.isCustomRateEnable());
        previewField.setVisible(!Utils.isCustomRateEnable());

        scoreTypesTable = new DynamicTable(getScoreTypeColumns(), true);
        scoreTypesTable.addRow(getWidgetsForScoreTypeTable(null));
        scoreTypesTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                scoreTypesTable.insertRow(rowId + 1, getWidgetsForScoreTypeTable(null));
            }

            @Override
            public void minusClicked(int rowId, Integer objectId) {

            }
        });

        scoreTypesTableField = new FormGroup(wfmStrings.type(), scoreTypesTable);
        scoreTypesTableField.setVisible(!Utils.isCustomRateEnable());


        Div mainPanel = new Div("section-box box-bg--1");
        mainPanel.add(new GRow(
                new GColumn(GColumnEnum.COL_4, useCompetenciesField, useGoalsField, employeeRateGroup, scoreScaleField, previewField),
                new GColumn(GColumnEnum.COL_4, scoreTypesTableField)
        ));
        add(mainPanel);

        AssessmentService.App.get().getAppraisalsSettings(new AbstractAsyncCallback<AppraisalsSettingsItem>() {
            @Override
            public void onSuccess(AppraisalsSettingsItem result) {
                item = result;
                setFieldValues();
            }
        });

        add(createFooter());
    }

    private DynamicTableColumn[] getScoreTypeColumns() {
        DynamicTableColumn[] columns = new DynamicTableColumn[3];
        columns[0] = new DynamicTableColumn(wfmStrings.grade(), "grade", 50);
        columns[1] = new DynamicTableColumn(wfmStrings.score(), "rate", 50);
        columns[2] = new DynamicTableColumn(wfmStrings.name(), "name", 250);
        return columns;
    }

    private Widget[] getWidgetsForScoreTypeTable(AppraisalsScoreTypeItem item) {
        Widget[] widgets = new Widget[3];
        TextBox grade = new TextBox();
        grade.addKeyPressHandler(e -> grade.removeStyleName(ERROR_FORM_STYLE));
        TextBox rate = new TextBox();
        Validation.addNumericKeyboardListener(rate, 0, false, true);
        TextBox name = new TextBox();

        if (item != null) {
            grade.setText(item.getGrade());
            rate.setText(String.valueOf(item.getRate()));
            name.setText(item.getName());
        }

        widgets[0] = grade;
        widgets[1] = rate;
        widgets[2] = name;
        return widgets;
    }

    private void setFieldValues() {

        chUseCompetencies.setValue(item.isUseCompetencies());
        chUseGoals.setValue(item.isUseGoals());
        chEmployeeRate.setValue(item.isEmployeeRate());

        if (!Utils.isCustomRateEnable()) {
            fromScale.setText(String.valueOf(item.getFromScale()));
            toScale.setText(String.valueOf(item.getToScale()));
            stepSize.setText(String.valueOf(item.getStepSize()));

            sliderBar.setMinValue(item.getFromScale());
            sliderBar.setMaxValue(item.getToScale());
            sliderBar.setStepSize(item.getStepSize());
            sliderBar.setCurrentValue(item.getFromScale());
            sliderBar.redraw();
        }
        if (item.getScoreTypeItems() != null && item.getScoreTypeItems().size() > 0) {
            scoreTypesTable.clear();
            for (AppraisalsScoreTypeItem scoreTypeItem : item.getScoreTypeItems()) {
                scoreTypesTable.addRow(getWidgetsForScoreTypeTable(scoreTypeItem));
            }
        }
        LoadingPanel.loading(false);
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return AppraisalsSettingsView.this.getFooterLeftSideWidgets();
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return AppraisalsSettingsView.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> buttonList = new ArrayList<>();

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.getElement().setId("Appraisals_save_button");
        save.addClickHandler(sender -> save());

        Div saveWrapper = new Div();
        saveWrapper.add(save);

        buttonList.add(saveWrapper);

        return buttonList;
    }

    private List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    private void save() {
        if (!validate()) {
            return;
        }

        item.setStepSize(Double.parseDouble(stepSize.getText()));
        item.setFromScale(Double.parseDouble(fromScale.getText()));
        item.setToScale(Double.parseDouble(toScale.getText()));
        item.setUseCompetencies(chUseCompetencies.getValue());
        item.setUseGoals(chUseGoals.getValue());
        item.setEmployeeRate(chEmployeeRate.getValue());
        ArrayList<AppraisalsScoreTypeItem> typeItems = new ArrayList<>();
        for (int i = 0; i < scoreTypesTable.getRowNumber(); i++) {
            DynamicTableItem item = scoreTypesTable.getItem(i);
            TextBox grade = (TextBox) item.getColumnById("grade");
            TextBox rate = (TextBox) item.getColumnById("rate");
            TextBox name = (TextBox) item.getColumnById("name");
            if (grade.getText() != null && rate.getText() != null && name != null) {
                AppraisalsScoreTypeItem appraisalsScoreTypeItem = new AppraisalsScoreTypeItem();
                appraisalsScoreTypeItem.setRate(Double.parseDouble(rate.getText()));
                appraisalsScoreTypeItem.setName(name.getText());
                appraisalsScoreTypeItem.setGrade(grade.getText());
                typeItems.add(appraisalsScoreTypeItem);
            }
        }
        item.setScoreTypeItems(typeItems);

        item.getReviewers().clear();

        LoadingPanel.loading(true);
        AssessmentService.App.get().updateAppraisalsSettings(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.appraisal()));
                onShellOk();
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }
        });
    }

    private boolean validate() {
        int errors = 0;

        Set<String> typeItems = new HashSet<>();

        for (int i = 0; i < scoreTypesTable.getRowNumber(); i++) {
            DynamicTableItem item = scoreTypesTable.getItem(i);
            TextBox gradeBox = (TextBox) item.getColumnById("grade");
            TextBox rateBox = (TextBox) item.getColumnById("rate");
            TextBox nameBox = (TextBox) item.getColumnById("name");
            if (gradeBox != null && rateBox != null && nameBox != null) {
                if ("".equals(gradeBox.getText()) || (!typeItems.isEmpty() && typeItems.contains(gradeBox.getText()))) {
                    errors++;
                    gradeBox.addStyleName(ERROR_FORM_STYLE);
                    Info.warn("You cannot have empty or multiple identical grade");
                } else {
                    typeItems.add(gradeBox.getText());
                }
            }
        }

        if (chUseCompetencies.getValue().equals(Boolean.FALSE) && chUseGoals.getValue().equals(Boolean.FALSE)) {
            chUseCompetencies.addStyleName(ERROR_FORM_STYLE);
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void onShellOk() {
        if (closeTab) {
            closeTab();
        }
    }

    public String getIconStyle() {
        return "onboardingStep onboardingStep-list";
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

}
