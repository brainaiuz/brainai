package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardDefaultComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Abror Abdukadirov
 * Date: 11.04.2018 16:03
 */
public class ModuleDashboardAddWidgetForm extends Composite {
    interface ModuleDashboardAddWidgetFormUiBinder extends UiBinder<Widget, ModuleDashboardAddWidgetForm> {
    }

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final ModuleDashboardServiceAsync moduleDashboardService = ModuleDashboardService.App.get();

    private static ModuleDashboardAddWidgetFormUiBinder ourUiBinder = GWT.create(ModuleDashboardAddWidgetFormUiBinder.class);

    @UiField
    HTMLPanel panel;
    @UiField
    Label componentLabel;
    @UiField
    Div selectAll;
    @UiField
    Div componentPanel;

    private VerticalPanel componentTable;
    private Map<Integer, DashboardDefaultComponentItem> selectedComponentMap = new HashMap<>();
    private int widgetCount = 0;
    private KpiCheckBox selectAllCheckBox;
    private Integer numberOfWidget = -1;

    public ModuleDashboardAddWidgetForm() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initialize();
    }

    private void initialize() {
        componentLabel.setText(wfmStrings.selectWidgets());
        componentTable = new VerticalPanel();
        componentPanel.add(componentTable);
    }

    public void getData(Integer dashboardId, ArrayList<String> componentCodes) {
        this.widgetCount = componentCodes.size();
        LoadingPanel.loading(true, panel);
        moduleDashboardService.getDashboardWidgetsMaxCount(dashboardId, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(Integer maxCount) {
                numberOfWidget = maxCount;
            }
        });
        moduleDashboardService.getComponentList(dashboardId, componentCodes, new AbstractAsyncCallback<ArrayList<DashboardDefaultComponentItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void onSuccess(ArrayList<DashboardDefaultComponentItem> result) {
                fillFields(result);
                LoadingPanel.loading(false, panel);
            }
        });
    }

    public boolean validate() {
        int errors = 0;

        if (Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            if (numberOfWidget < widgetCount + getValueToSave().size()) {
                componentPanel.addStyleName(Constants.ERROR_FORM_STYLE);
                Info.warn(wfmMessages.youCanNotAddMoreThanWidgets(String.valueOf(numberOfWidget)), Info.Position.TOP_RIGHT);
                return false;
            }
        } else if ((widgetCount + selectedComponentMap.size()) > Utils.dashboardWidgetsMaxLimit()) {
            Info.warn(wfmMessages.youCanNotAddMoreThanWidgets(String.valueOf(Utils.dashboardWidgetsMaxLimit())), Info.Position.TOP_RIGHT);
            return false;
        }
        if (selectedComponentMap.size() <= 0) {
            componentPanel.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    private void fillFields(List<DashboardDefaultComponentItem> components) {
        componentTable.clear();
        selectAll.clear();
        selectedComponentMap.clear();

        if (components.size() > 0) {
            //Select All logic
            selectAllCheckBox = new KpiCheckBox(wfmStrings.selectAll());
            selectAllCheckBox.setValue(false);
            selectAll.add(selectAllCheckBox);

            List<KpiCheckBox> checkBoxes = new ArrayList<>();
            selectAllCheckBox.addValueChangeHandler(booleanValueChangeEvent -> {
                for (KpiCheckBox checkBox : checkBoxes) {
                    DashboardDefaultComponentItem currentComponent = (DashboardDefaultComponentItem) checkBox.getLayoutData();
                    if (currentComponent == null) {
                        return;
                    }
                    if (selectAllCheckBox.getValue()) {
                        checkBox.setValue(true, false);
                        if (!selectedComponentMap.containsKey(currentComponent.getObjectId())) {
                            selectedComponentMap.put(currentComponent.getObjectId(), currentComponent);
                        }
                    } else {
                        checkBox.setValue(false, false);
                        selectedComponentMap.remove(currentComponent.getObjectId());
                    }
                }
            });

            //Component checkboxes
            for (DashboardDefaultComponentItem component : components) {
                KpiCheckBox checkBox = new KpiCheckBox(component.getComponentName());
                checkBox.setLayoutData(component);
                checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
                    DashboardDefaultComponentItem currentComponent = (DashboardDefaultComponentItem) checkBox.getLayoutData();
                    if (currentComponent == null) {
                        return;
                    }
                    if (checkBox.getValue()) {
                        if (!selectedComponentMap.containsKey(currentComponent.getObjectId())) {
                            selectedComponentMap.put(currentComponent.getObjectId(), currentComponent);
                        }
                    } else {
                        selectedComponentMap.remove(currentComponent.getObjectId());
                    }
                });
                checkBoxes.add(checkBox);
                componentTable.add(checkBox);
            }
        }
    }

    public List<DashboardComponentItem> getValueToSave() {
        List<DashboardComponentItem> components = new ArrayList<>();
        if (selectedComponentMap.size() > 0) {
            for (DashboardDefaultComponentItem selectItem : selectedComponentMap.values()) {
                if (selectItem != null) {
                    DashboardComponentItem item = new DashboardComponentItem();
                    item.setName(selectItem.getComponentName());
                    item.setComponentCode(selectItem.getComponentCode());
                    item.setReportId(selectItem.getReportId());
                    item.setReportWidgetId(selectItem.getReportWidgetId());
                    item.setWidth(selectItem.getWidth());
                    item.setMinWidth(selectItem.getMinWidth());
                    item.setHeight(selectItem.getHeight());
                    item.setMinHeight(selectItem.getMinHeight());
                    components.add(item);
                }
            }
        }
        return components;
    }

    public void clearForm() {
        componentTable.clear();
        selectedComponentMap.clear();
    }

    public void clearErrorStyle() {
        componentPanel.removeStyleName(Constants.ERROR_FORM_STYLE);
    }
}