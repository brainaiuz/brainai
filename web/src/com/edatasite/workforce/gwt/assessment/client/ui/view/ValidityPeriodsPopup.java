package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

/**
 * User: Fathulla
 * Date: 20.02.13
 * Time: 12:36
 */
public class ValidityPeriodsPopup extends Composite implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ValidityPeriodItem item;

    private KpiCheckBox checkBox_appraisal;
    private KpiCheckBox checkBox_goal;
    private KpiCheckBox checkBox_bonus;
    private WfmButton2 button_add_period;
    private WfmButton2 button_cancel_period;
    private DatePicker from_date;
    private DatePicker to_date;
    private TextBox validity_period_description;
    private TextBox validity_period_name;
    private KpiModal validity_period_widgets;
    private WfmForm.Field validity_period_checkBox_field;
    private WfmForm.Field validity_period_description_field;
    private WfmForm.Field validity_period_name_field;
    private WfmForm validity_period_form;
    private WfmForm.Field validity_period_from_date_field;
    private WfmForm.Field validity_period_to_date_field;
    public int errors2 = 0;

    public ValidityPeriodsPopup(ValidityPeriodItem item) {
        this.item = item;
        onInit();
    }

    private void createValidityPeriodBox() {
        validity_period_form = new WfmForm();
        //validity period widgets dialog box
        validity_period_widgets = new KpiModal();
        validity_period_widgets.setTitle(item != null && item.getName() != null ? wfmStrings.editValidityPeriod() : wfmStrings.addNewValidityPeriod());
        validity_period_widgets.setWidth(315);
        validity_period_widgets.setMaxHeight("85%");
        //name
        validity_period_name = new TextBox();
        //description
        validity_period_description = new TextBox();
        //period to: appraisal
        checkBox_appraisal = new KpiCheckBox(wfmStrings.appraisal());
        //period to: goal
        checkBox_goal = new KpiCheckBox(wfmStrings.goal());
        //period to: bonus
        checkBox_bonus = new KpiCheckBox(wfmStrings.bonus());

        //period related to table
        FlexTable periodTypeTable = new FlexTable();
        periodTypeTable.setWidget(0, 0, checkBox_appraisal);
        periodTypeTable.setWidget(0, 1, checkBox_goal);
        periodTypeTable.setWidget(0, 2, checkBox_bonus);
        //save button
        button_add_period = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> {
            Date endDate = to_date.getDate();
            DateUtil.getDayLastTime(endDate);
            to_date.setDate(endDate);
            if (validatePeriod()) {
                setValidityPeriodItems();
                enableButtons(false);
                LoadingPanel.loading(true, validity_period_widgets);
                AssessmentService.App.get().createValidityPeriodItem(item, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        enableButtons(true);
                        try {
                            throw throwable;
                        } catch (InsufficientPermissionsException e) {
                            Info.show(wfmStrings.validityErrorMsg(), Info.Type.WARNING);
                        } catch (Throwable e) {
                            // last resort  a very unexpected exception
                        }
                    }

                    @Override
                    public void success(Integer result) {
                        LoadingPanel.loading(false);
                        enableButtons(true);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_VALIDITY_PERIOD_CHANGED, result, ValidityPeriodsPopup.this);
                        validity_period_widgets.close();
                    }
                });
            }
        });
        //cancel button
        button_cancel_period = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> validity_period_widgets.close());
        //from date
        from_date = new DatePicker(true);
        //to date
        to_date = new DatePicker(true);

        validity_period_name_field = validity_period_form.addField(wfmStrings.name(), validity_period_name, Boolean.TRUE);
        validity_period_description_field = validity_period_form.addField(wfmStrings.description(), validity_period_description);
        validity_period_from_date_field = validity_period_form.addField(wfmStrings.fromDate(), from_date, Boolean.TRUE);
        validity_period_to_date_field = validity_period_form.addField(wfmStrings.toDate(), to_date, Boolean.TRUE);

        validity_period_checkBox_field = validity_period_form.addField(wfmStrings.relatedTo(), periodTypeTable, Boolean.TRUE);

        validity_period_widgets.add(validity_period_form);

        validity_period_widgets.addButton(button_cancel_period);
        validity_period_widgets.addButton(button_add_period);

        from_date.getElement().getStyle().clearDisplay();
        to_date.getElement().getStyle().clearDisplay();
    }

    private void enableButtons(boolean b) {
        if (button_add_period != null) {
            button_add_period.setEnabled(b);
        }
        if (button_cancel_period != null) {
            button_cancel_period.setEnabled(b);
        }
    }

    private void onInit() {
        if (validity_period_form == null) {
            createValidityPeriodBox();
        }
        if (item == null) {
            item = new ValidityPeriodItem();
        }
        validity_period_name.setText(item.getName());
        validity_period_description.setText(item.getDescription());
        from_date.setDate(new Date());
        to_date.setDate(new Date());
        if (item.getFromDate() != null) {
            from_date.setDate(item.getFromDate());
        }
        if (item.getToDate() != null) {
            to_date.setDate(item.getToDate());
        }
        checkBox_appraisal.setValue(Boolean.FALSE);
        checkBox_goal.setValue(Boolean.FALSE);
        checkBox_bonus.setValue(Boolean.FALSE);
        for (SelectItem periodTypeItem : item.getPeriodTypeItems()) {
            if (periodTypeItem.getDescription().equals(ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL)) {
                checkBox_appraisal.setValue(Boolean.TRUE);
            } else if (periodTypeItem.getDescription().equals(ValidityPeriodItem.VALIDITY_PERIOD_GOAL)) {
                checkBox_goal.setValue(Boolean.TRUE);
            } else if (periodTypeItem.getDescription().equals(ValidityPeriodItem.VALIDITY_PERIOD_BONUS)) {
                checkBox_bonus.setValue(Boolean.TRUE);
            }
        }
        validity_period_widgets.open();
    }

    private void setValidityPeriodItems() {
        //validity period name
        item.setName(validity_period_name.getText());
        //validity period description
        item.setDescription(validity_period_description.getText());
        //validity period from date
        item.setFromDate(from_date.getDate());
        //validity period to date
        item.setToDate(to_date.getDate());
        //validity period related To
        item.getPeriodTypeCodeItems().clear();
        if (checkBox_appraisal.getValue()) {
            item.getPeriodTypeCodeItems().add(ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL);
        }
        if (checkBox_goal.getValue()) {
            item.getPeriodTypeCodeItems().add(ValidityPeriodItem.VALIDITY_PERIOD_GOAL);
        }
        if (checkBox_bonus.getValue()) {
            item.getPeriodTypeCodeItems().add(ValidityPeriodItem.VALIDITY_PERIOD_BONUS);
        }
    }

    private boolean validatePeriod() {
        validity_period_form.cleanupErrors();
        errors2 = 0;
        if (validity_period_name != null && !Validation.validateTextBoxRequired(validity_period_name, validity_period_name_field)) {
            errors2++;
        }
        if (validity_period_from_date_field != null && !Validation.validateDate(from_date, validity_period_from_date_field, Boolean.TRUE)) {
            errors2++;
        }
        if (validity_period_to_date_field != null && !Validation.validateDate(to_date, validity_period_to_date_field, Boolean.TRUE)) {
            errors2++;
        }
        if (checkBox_appraisal.getValue().equals(Boolean.FALSE) && checkBox_goal.getValue().equals(Boolean.FALSE) && checkBox_bonus.getValue().equals(Boolean.FALSE)) {
            Validation.validateCheckBoxRequired(checkBox_appraisal, validity_period_checkBox_field);
            errors2++;
        }
        if (errors2 > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
}
