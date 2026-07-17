package com.edatasite.workforce.gwt.invoice.client.ui.view.kanbanSettings;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
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
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

/**
 * Created by Yuldoshev Muhammadrizo on 08/10/2022.
 */
public class KanbanItemSettingsView extends View implements Colapse {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();

    private VerticalPanel columnsVerticalPanel;
    private PickupDragController draggableController;
    private DataListBox sectionListBox;
    private Div formColumn;
    private Div itemTableColumn;
    private WfmButton2 applyChangeButton;
    private MaterialPanel innerContainer;
    private SelectItem[] reletedFields;
    private HashMap<String, KanbanItemColumnConfigs> columnsMap;
    private String kanbanItemSectionCode;


    public KanbanItemSettingsView() {
        super("kanbanitemsettingsdraggable", settingsStrings.kanbanItemSettings());
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
            this.kanbanItemSectionCode = sectionListBox.getSelectedItem().getCode();

            CommonService.App.get().getRelatedFieldsBySectionName(kanbanItemSectionCode, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    reletedFields = selectItems;
                    drawMainTable();
                }
            });
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

        WfmButton2 addCustomFieldButton = new WfmButton2(wfmStrings.addCustomField(), WfmButton2.BTN_PRIMARY);
        addCustomFieldButton.getElement().setId("add_custom_field_button");

        Div pnlBox = new Div("panel-box panel-box--right");
        Div pnlBoxItem = new Div("panel-box__item");
        pnlBoxItem.add(addCustomFieldButton);


        applyChangeButton = new WfmButton2(wfmStrings.applyChanges(), WfmButton2.BTN_PRIMARY);
        applyChangeButton.getElement().setId("apply_change_button");
        applyChangeButton.addClickHandler(clickEvent -> saveSettings());
        Div pnlBoxItem2 = new Div("panel-box__item");
        pnlBoxItem2.add(applyChangeButton);

        pnlBox.add(pnlBoxItem2);
        pnlContainer.add(pnlBox);

        getData();

        innerContainer.clear();
        innerContainer.add(pnlContainer);
    }

    private void getData() {

        LoadingPanel.loading(true);
        CommonService.App.get().getKanbanColumnConfigs(getSectionEnumByCode(kanbanItemSectionCode), new AsyncCallback<KanbanItemColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(KanbanItemColumnConfigs[] columnConfigs) {
                LoadingPanel.loading(false);
                columnsMap = new HashMap<>();
                for (KanbanItemColumnConfigs columnConfig : columnConfigs) {
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
        CommonService.App.get().saveKanbanItemSettings(getSectionEnumByCode(kanbanItemSectionCode), getItemColumns(), new AsyncCallback<Integer>() {
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

    private KanbanItemSettingEnum getSectionEnumByCode(String kanbanItemSectionCode) {
        return KanbanItemSettingEnum.valueOf(kanbanItemSectionCode);
//        switch (kanbanItemSectionCode) {
//            case "TASK_ITEM_SETTINGS":
//                return KanbanItemSettingEnum.TASK_ITEM_SETTINGS;
//            case "OPPORTUNITY_ITEM_SETTINGS":
//                return KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS;
//            case "CANDIDATE_ITEM_SETTINGS":
//                return KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS;
//            case "LEAD_ITEM_SETTINGS":
//                return KanbanItemSettingEnum.LEAD_ITEM_SETTINGS;
//            case "CASE_ITEM_SETTINGS":
//                return KanbanItemSettingEnum.CASE_ITEM_SETTINGS;
//        }
//        return null;
    }

    private KanbanItemColumnConfigs[] getItemColumns() {
        for (int order = 0; order < columnsVerticalPanel.getWidgetCount(); order++) {
            Div formRowPanel = (Div) columnsVerticalPanel.getWidget(order);
            Div nameColumn = (Div) formRowPanel.getWidget(0);
            Div columnContainer = (Div) nameColumn.getWidget(0);
            Div relatedColumn = (Div) formRowPanel.getWidget(1);
            MaterialSwitch switcher = (MaterialSwitch) columnContainer.getLayoutData();
            if (switcher == null) {
                continue;
            }
            KanbanItemColumnConfigs columnConfig = columnsMap.get(switcher.getLayoutData());
            columnConfig.setSelected(switcher.getValue());
            try {
                DataListBox listBox = (DataListBox) relatedColumn.getWidget(0);
                columnConfig.setRelatedFieldCode(listBox.getSelectedItem().getDescription());
            } catch (Exception e) {
                columnConfig.setRelatedFieldCode(null);
                e.printStackTrace();
                GWT.log("Error occurred while parsing related column : " + columnConfig.getTitle());
            }
        }
        return columnsMap.values().toArray(new KanbanItemColumnConfigs[]{});
    }

    private Div drawTotalWidthPanel() {
        Div formRowPanel = new Div("form-row");

        Div fieldNameColumn = new Div("col-3");
        Div relatedFieldColumn = new Div("col-2");

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(relatedFieldColumn);

        return formRowPanel;
    }

    private Div drawHeaderPanel() {
        Div formRowPanel = new Div("form-row");
        formRowPanel.getElement().setAttribute("style", "border-bottom:1px solid #ced5db;padding-top:20px;");
        Div fieldNameColumn = new Div("col-3");
        fieldNameColumn.add(getSpan(wfmStrings.fieldName()));

        Div relatedFieldColumn = new Div("col-2");
        Span relatedFieldSpan = getSpan(wfmStrings.related());
        relatedFieldSpan.setDisplay(Display.INLINE);
        relatedFieldColumn.add(getSpan(wfmStrings.related()));

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(relatedFieldColumn);

        return formRowPanel;
    }

    private void addNewColumn(KanbanItemColumnConfigs item) {
        Div fieldNameColumn = new Div("col-3");
        Div relatedField = new Div("col-2");

        MaterialSwitch switcher = new MaterialSwitch();
        switcher.setLayoutData(item.getCode());
        switcher.setValue(item.isSelected());
        switcher.setEnabled(true);

        Div formRowPanel = new Div("form-row" + (switcher.getValue() ? " state-on" : " state-off"));


        DataListBox relatedFieldDropDown = new DataListBox(false);
        if (reletedFields != null) {
            relatedFieldDropDown.setItems(reletedFields);
            relatedFieldDropDown.setEnabled(item.isChangeable());
            if (item.getRelatedFieldCode() != null)
                relatedFieldDropDown.setSelectedByDescription(item.getRelatedFieldCode());
        }
        relatedField.add(relatedFieldDropDown);

        HTML columnTitle = new HTML(item.getLocalizationName());
        columnTitle.setStyleName("drag-tile__text");

        Div pnlColumn = new Div("drag-tile drag-tile--sm");
        Div pnlGrip = new Div("drag-tile__grip");

        Div pnlAction = new Div("drag-tile__actions");
        pnlAction.add(switcher);
//        pnlAction.setLayoutData(switcher);
        switcher.addValueChangeHandler(vh -> {
            if (switcher.getValue()) {
                formRowPanel.removeStyleName("state-off");
                formRowPanel.addStyleName("state-on");
            } else if (item.isMandatory()) {
                switcher.setValue(true);
                Info.warn(wfmStrings.youCannotDisableRequiredField());
            } else {
                formRowPanel.removeStyleName("state-on");
                formRowPanel.addStyleName("state-off");
            }
        });

        pnlColumn.add(pnlGrip);
        pnlColumn.add(columnTitle);
        pnlColumn.add(pnlAction);
        pnlColumn.setLayoutData(switcher);
        fieldNameColumn.add(pnlColumn);

        formRowPanel.add(fieldNameColumn);
        formRowPanel.add(relatedField);

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
                new SelectItem(++i, Property.get(Constants.TASK, wfmStrings.task()), KanbanItemSettingEnum.TASK_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.TASK_ITEM_SETTINGS.getName()),
                new SelectItem(++i, Property.get(Constants.OPPORTUNITY, wfmStrings.opportunity()), KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS.getName()),
                new SelectItem(++i, Property.get(Constants.LEADS, wfmStrings.leads()), KanbanItemSettingEnum.LEAD_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.LEAD_ITEM_SETTINGS.getName()),
                new SelectItem(++i, Property.get(Constants.CANDIDATE, wfmStrings.candidates()), KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS.getName()),
                new SelectItem(++i, Property.get("CASE", wfmStrings.cases()), KanbanItemSettingEnum.CASE_ITEM_SETTINGS.getCode(), null, KanbanItemSettingEnum.CASE_ITEM_SETTINGS.getName())
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
