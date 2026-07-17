package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;

/**
 * Created by Hayot on 4/1/14.
 */
public class WorkflowDateSelecter extends InputGroup {
    public static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final SettingStrings settingsStrings = SettingStrings.App.get();
    public static final SelectItem[] START_DATE_ITEMS = new SelectItem[]{
            new SelectItem(1, wfmStrings.triggerDate(), Constants.WORKFLOW_START_TIME.TRIGGER_TIME),
            new SelectItem(2, wfmStrings.createdDate(), Constants.WORKFLOW_START_TIME.ENTITY_CREATION_TIME),
            new SelectItem(3, wfmStrings.modifiedDate(), Constants.WORKFLOW_START_TIME.ENTITY_MODIFICATION_TIME),
            new SelectItem(4, settingsStrings.byAttributes(), Constants.WORKFLOW_START_TIME.BY_ATTRIBUTES)
    };
    private final DataListBox startDate;
    private final TextBox dueUnit;
    private final DataListBox dueGranularity;
    private final FlexTable panel;
    public KpiSwitcher allDay;
    private final TextBox attributesUnit;
    public static final SelectItem[] DUE_GRANULARITY_ITEMS = new SelectItem[]{
            new SelectItem(1, wfmStrings.minutes(), Constants.TIME_GRANULARITY.MINUTES),
            new SelectItem(2, wfmStrings.hours(), Constants.TIME_GRANULARITY.HOURS),
            new SelectItem(3, wfmStrings.days(), Constants.TIME_GRANULARITY.DAYS)
    };

    public WorkflowDateSelecter(boolean withAllDay, boolean isTimeBased) {
        startDate = new DataListBox();
        startDate.setWithoutNullLabel(true);
        dueUnit = new TextBox();
        dueGranularity = new DataListBox();
        allDay = new KpiSwitcher(wfmStrings.allDay(), null, false);
        startDate.setAllowFirstItem(false);
        startDate.setItems(START_DATE_ITEMS);
        dueGranularity.setItems(DUE_GRANULARITY_ITEMS);
        dueGranularity.setAllowFirstItem(false);
        attributesUnit = new TextBox();
        attributesUnit.setVisible(false);

        startDate.addValueChangeHandler(v -> {
            if (startDate.getSelectedItem(true).getDescription().equals(Constants.WORKFLOW_START_TIME.BY_ATTRIBUTES)) {
                attributesUnit.setVisible(true);
            } else {
                attributesUnit.setVisible(false);
                attributesUnit.setText(null);
            }
        });

        panel = new FlexTable();
        if (!isTimeBased) {
            add(startDate);
            add(attributesUnit);
            Div toContainer = add(new HTML(wfmStrings.to()),true);
            toContainer.setWidth("40px");
            add(dueUnit);
            add(dueGranularity);
            if (withAllDay) {
                Div allDayContainer = add(allDay,true);
                allDayContainer.setWidth("120px");
                allDayContainer.setPaddingLeft(10);
            }
        } else {
            add(dueUnit);
            add(dueGranularity);
            Div afterContainer = add(new HTML(wfmStrings.after()),true);
            afterContainer.setPaddingLeft(10);
            afterContainer.setWidth("90px");
            add(startDate);
        }
    }

    public String getWorkflowStartDate() {
        return startDate.getSelectedItem(true).getDescription();
    }

    public String getWorkflowStartDateAttributes() {
        if (getWorkflowStartDate().equals(Constants.WORKFLOW_START_TIME.BY_ATTRIBUTES)) {
            return attributesUnit.getText();
        }
        return null;
    }

    public int getWorkflowDueDateUnit() {
        try {
            return Integer.parseInt(dueUnit.getText());
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public String getWorkflowDueDateGranularity() {
        return dueGranularity.getSelectedItem(true).getDescription();
    }

    public boolean isAllDay() {
        return allDay.getValue();
    }

    public void setStartDate(String workflowStartDate) {
        if (startDate != null) {
            startDate.setSelectedByDescription(workflowStartDate);
        }
    }

    public void setStartDateAttributes(String workflowStartDateAttributes) {
        if (attributesUnit != null) {
            attributesUnit.setVisible(true);
            attributesUnit.setText(workflowStartDateAttributes);
        }
    }

    public void setDueDateGranularity(String workflowDueDateGranularity) {
        if (dueGranularity != null) {
            dueGranularity.setSelectedByDescription(workflowDueDateGranularity);
        }
    }

    public void setDueDate(Integer workflowDueDate) {
        dueUnit.setText(workflowDueDate != null ? workflowDueDate.toString() : "0");
    }

    public void setDateItems(ArrayList<SelectItem> columns) {
        if (startDate != null) {
            startDate.clear();
            columns.add(new SelectItem(1, wfmStrings.triggerDate(), Constants.WORKFLOW_START_TIME.TRIGGER_TIME));
            columns.add(new SelectItem(2, wfmStrings.createdDate(), Constants.WORKFLOW_START_TIME.ENTITY_CREATION_TIME));
            columns.add(new SelectItem(3, wfmStrings.modifiedDate(), Constants.WORKFLOW_START_TIME.ENTITY_MODIFICATION_TIME));
            startDate.setItems(columns.toArray(new SelectItem[]{}));
        }
    }
}
