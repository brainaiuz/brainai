package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;

public class QuickAddSettingsView extends View implements Colapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private VerticalPanel columnsVerticalPanel;
    private PickupDragController draggableController;
    private DataListBox sectionListBox;
    private Div formColumn;
    private Div itemTableColumn;
    private WfmButton2 applyChangeButton;
    private MaterialPanel innerContainer;
    private SelectItem[] reletedFields;
    private HashMap<String, QuickAddColumnConfigs> columnsMap;
    private String form;


    public QuickAddSettingsView() {
        super("quickaddsettingsdraggable", settingsStrings.quickAddSettings());
    }

    @Override
    protected Widget onInitialize() {
        MaterialPanel container = new MaterialPanel();
        container.getElement().setAttribute("style", "padding:20px;border-radius:10px;background-color:white;");

        Div headerPanel = new Div("form-row");
        Div sectionColumn = new Div("col-4");
        formColumn = new Div("col-4");

        headerPanel.add(sectionColumn);
        headerPanel.add(formColumn);
        container.add(headerPanel);
        innerContainer = new MaterialPanel();
        container.add(innerContainer);
        columnsMap = new HashMap<>();
        sectionListBox = new DataListBox();
        sectionListBox.setItems(getSections());
        sectionListBox.addValueChangeHandler(handler -> {
            form = sectionListBox.getSelectedItem().getDescription();
            drawMainTable();
        });
        sectionColumn.add(sectionListBox);
        add(container);
        return null;
    }

    private void drawMainTable() {

        MaterialPanel pnlContainer = new MaterialPanel();

        columnsVerticalPanel = new VerticalPanel();
        AbsolutePanel draggableAbsolutePanel = new AbsolutePanel();
        draggableAbsolutePanel.addStyleName("drag-tiles--bordered");
        draggableAbsolutePanel.add(columnsVerticalPanel);

        draggableController = new PickupDragController(draggableAbsolutePanel, false);
        draggableController.setBehaviorMultipleSelection(false);
        draggableController.registerDropController(new VerticalPanelDropController(columnsVerticalPanel));

        pnlContainer.add(drawHeaderPanel());
        pnlContainer.add(draggableAbsolutePanel);
        pnlContainer.add(drawTotalWidthPanel());

        Div pnlBox = new Div("panel-box panel-box--right");
        Div pnlBoxItem = new Div("panel-box__item");

        applyChangeButton = new WfmButton2(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY);
        applyChangeButton.getElement().setId("apply_change_button");
        applyChangeButton.addClickHandler(clickEvent -> saveSettings());
        pnlBoxItem.add(applyChangeButton);

        pnlBox.add(pnlBoxItem);
        pnlContainer.add(pnlBox);

        getData();

        innerContainer.clear();
        innerContainer.add(pnlContainer);
    }

    private void getData() {

        LoadingPanel.loading(true);
        CommonService.App.get().getQuickAddColumnConfigs(getFormEnumByCode(form), new AsyncCallback<QuickAddColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(QuickAddColumnConfigs[] columnConfigs) {
                LoadingPanel.loading(false);
                columnsMap = new HashMap<>();
                for (QuickAddColumnConfigs columnConfig : columnConfigs) {
                    if (columnConfig != null) {
                        addNewColumn(columnConfig);
                        columnsMap.put(columnConfig.getCode(), columnConfig);
                    }
                }
            }
        });
    }

    private void saveSettings() {
        LoadingPanel.loading(true);
        CommonService.App.get().saveQuickAddSettings(getFormEnumByCode(form), getItemColumns(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Integer integer) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.settings()));
            }
        });
    }

    private QuickAddSettingsForm getFormEnumByCode(String form) {
        return QuickAddSettingsForm.valueOf(form);
    }

    private QuickAddColumnConfigs[] getItemColumns() {
        for (int order = 0; order < columnsVerticalPanel.getWidgetCount(); order++) {
            Div formRowPanel = (Div) columnsVerticalPanel.getWidget(order);
            Div nameColumn = (Div) formRowPanel.getWidget(0);
            Div columnContainer = (Div) nameColumn.getWidget(0);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (switcher == null) {
                continue;
            }
            QuickAddColumnConfigs columnConfig = columnsMap.get(switcher.getLayoutData());
            columnConfig.setSelected(switcher.getValue());
            columnConfig.setOrder(order);
        }
        return columnsMap.values().stream().sorted(Comparator.comparingInt(QuickAddColumnConfigs::getOrder)).collect(Collectors.toList())
                .toArray(new QuickAddColumnConfigs[]{});
    }

    private Div drawTotalWidthPanel() {
        Div formRowPanel = new Div("form-row");

        Div fieldNameColumn = new Div("col-3");
        Div dataTypeColumn = new Div("col-2");

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(dataTypeColumn);

        return formRowPanel;
    }

    private Div drawHeaderPanel() {
        Div formRowPanel = new Div("form-row");
        formRowPanel.getElement().setAttribute("style", "border-bottom:1px solid #ced5db;padding-top:20px;");
        Div fieldNameColumn = new Div("col-3");
        fieldNameColumn.add(getSpan(wfmStrings.fieldName()));

        Div dataTypeColumn = new Div("col-2");
        dataTypeColumn.add(getSpan(wfmStrings.dataType()));

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(dataTypeColumn);

        return formRowPanel;
    }

    private void addNewColumn(QuickAddColumnConfigs item) {
        Div fieldNameColumn = new Div("col-3");
        Div dataTypeColumn = new Div("col-2");

        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(item.getCode());
        switcher.setValue(item.isSelected());
        switcher.setEnabled(true);

        Div formRowPanel = new Div("form-row" + (switcher.getValue() ? " state-on" : " state-off"));


        HTML columnTitle = new HTML(item.getName() != null ? item.getName() : item.getCode());
        columnTitle.setStyleName("drag-tile__text");

        Div pnlColumn = new Div("drag-tile drag-tile--sm");
        Div pnlGrip = new Div("drag-tile__grip");

        Div pnlAction = new Div("drag-tile__actions");
        pnlAction.add(switcher);
        switcher.addValueChangeHandler(vh -> {
            if (switcher.getValue()) {
                formRowPanel.removeStyleName("state-off");
                formRowPanel.addStyleName("state-on");
            } else if (item.isRequired()) {
                switcher.setValue(true);
                Info.warn(wfmStrings.youCannotDisableRequiredField());
            } else {
                formRowPanel.removeStyleName("state-on");
                formRowPanel.addStyleName("state-off");
            }
        });

        HTML dateType = new HTML(item.getName() != null ? wfmStrings.customField() : wfmStrings.system());
        dateType.setStyleName("drag-tile__text");
        dataTypeColumn.add(dateType);

        pnlColumn.add(pnlGrip);
        pnlColumn.add(columnTitle);
        pnlColumn.add(pnlAction);
        pnlColumn.setLayoutData(switcher);
        fieldNameColumn.add(pnlColumn);

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(dataTypeColumn);

        columnsVerticalPanel.add(formRowPanel);
        draggableController.makeDraggable(formRowPanel, pnlGrip);
    }

    private Span getSpan(String fielName) {
        Span span = new Span(fielName);
        span.addStyleName("form-group__label");
        return span;
    }

    private SelectItem[] getSections() {
        int i = 0;
        return new SelectItem[]{
                new SelectItem(++i, Property.get(Constants.TASK, wfmStrings.task()), QuickAddSettingsForm.TASK.toString()),
//                new SelectItem(++i, Property.get(Constants.OPPORTUNITY, wfmStrings.opportunity()), KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS.getName()),
//                new SelectItem(++i, Property.get(Constants.LEADS, wfmStrings.leads()), KanbanItemSettingEnum.LEAD_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.LEAD_ITEM_SETTINGS.getName()),
//                new SelectItem(++i, Property.get(Constants.CANDIDATE, wfmStrings.candidates()), KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS.getName()),
//                new SelectItem(++i, Property.get("CASE", wfmStrings.cases()), KanbanItemSettingEnum.CASE_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.CASE_ITEM_SETTINGS.getName())
        };
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
