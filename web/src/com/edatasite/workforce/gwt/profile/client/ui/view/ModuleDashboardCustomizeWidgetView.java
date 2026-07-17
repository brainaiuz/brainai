package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.UserDashboardSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.dashboard.GridStackItemPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.dashboard.GridStackPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.utils.DashboardUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 18:39
 */
public class ModuleDashboardCustomizeWidgetView extends View implements Colapse {
    interface ModuleDashboardCustomizeWidgetViewUiBinder extends UiBinder<HTMLPanel, ModuleDashboardCustomizeWidgetView> {
    }

    private static final ModuleDashboardCustomizeWidgetViewUiBinder ourUiBinder = GWT.create(ModuleDashboardCustomizeWidgetViewUiBinder.class);

    private static final ModuleDashboardServiceAsync moduleDashboardService = ModuleDashboardService.App.get();

    @UiField
    Div header;
    @UiField
    GridStackPanel gridStackPanel;

    private KpiSideNavBox quickAddBox;
    private KpiCheckBox applyForAllCheckBox;

    private final Integer objectId;
    private boolean hasOpenQuickForm = false;
    private final UserDashboardSettingsItem data = new UserDashboardSettingsItem();

    public ModuleDashboardCustomizeWidgetView(Integer objectId, boolean hasOpenQuickForm) {
        super("summary", "Customize Widget View");
        this.objectId = objectId;
        this.hasOpenQuickForm = hasOpenQuickForm;
    }

    @Override
    protected Widget onInitialize() {
        add(ourUiBinder.createAndBindUi(this));
        gridStackPanel.initializeConfig(false);
        getData();
        return null;
    }

    private void getData() {
        LoadingPanel.loading(true);
        moduleDashboardService.getDashboardComponentList(objectId, new AbstractAsyncCallback<ArrayList<DashboardComponentItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ArrayList<DashboardComponentItem> result) {
                initialize();
                setComponents(result, false);
                if (hasOpenQuickForm) {
                    quickAddBox.show();
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void initialize() {
        quickAddBox = new KpiSideNavBox();

        ModuleDashboardAddWidgetForm addWidgetForm = new ModuleDashboardAddWidgetForm();

        Heading quickAddHeader = new Heading(HeadingSize.H1);
        quickAddHeader.setText(wfmStrings.availableWidgets());

        WfmButton2 quickSaveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//        WfmButton2 quickCancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);

        quickSaveBtn.addClickHandler(event -> {
            quickSaveBtn.setEnabled(false);
//            quickCancelBtn.setEnabled(false);
            if (addWidgetForm.validate()) {
                setComponents(addWidgetForm.getValueToSave(), true);

                quickSaveBtn.setEnabled(true);
//                quickCancelBtn.setEnabled(true);
                quickAddBox.hide();
            } else {
                quickSaveBtn.setEnabled(true);
//                quickCancelBtn.setEnabled(true);
            }
        });
//        quickCancelBtn.addClickHandler(event -> {
//            addWidgetForm.clearForm();
//            quickAddBox.hide();
//        });
        quickAddBox.addOpeningHandler(event -> addWidgetForm.getData(objectId, gridStackPanel.getComponentCodes()));

        quickAddBox.addHeader(quickAddHeader);
        quickAddBox.addBody(addWidgetForm);
        quickAddBox.addFooter(quickSaveBtn);
//        quickAddBox.addFooter(quickCancelBtn);

        WfmButton2 addWidgetButton = new WfmButton2("Add Widget", WfmButton2.BTN_PRIMARY);
        addWidgetButton.addClickHandler(clickEvent -> {
            addWidgetForm.clearErrorStyle();
            quickAddBox.show();
        });

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> {
            WfmMessageBox modal = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, true);
            modal.setTitle(wfmStrings.confirmationMessage());
            modal.replaceWidget(applyForAllCheckBox);
            modal.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    saveData();
                }
            });
            modal.open();
        });
        applyForAllCheckBox = new KpiCheckBox();
        applyForAllCheckBox.setText("Apply this form for all users");

        Div panelBox = new Div("panel-box panel-box--right");

        Div saveItem = new Div("panel-box__item");
        saveItem.add(saveButton);
        panelBox.add(saveItem);

        Div addWidgetItem = new Div("panel-box__item");
        addWidgetButton.setText(wfmStrings.addWidget());
        addWidgetItem.add(addWidgetButton);
        panelBox.add(addWidgetItem);

        header.add(panelBox);
    }

    private void setComponents(List<DashboardComponentItem> components, boolean autoPosition) {
        List<DashboardBaseWidget> provideComponents = new ArrayList<>();
        if (components != null && components.size() > 0) {
            for (DashboardComponentItem componentItem : components) {
                DashboardBaseWidget component = DashboardUtils.generateComponent(componentItem, true, null);
                if (component == null) {
                    continue;
                }
                component.setFromSettings(true);
                provideComponents.add(component);
                GridStackItemPanel itemPanel = new GridStackItemPanel();
                itemPanel.setItemWidget(component);
                if (component.getGridItemConfig() != null) {
                    itemPanel.initConfig(component.getGridItemConfig());
                }
                itemPanel.getContent().add(component);
                gridStackPanel.addNewItem(itemPanel, autoPosition);
            }
            gridStackPanel.commit();
        }
        gridStackPanel.registerHandles();
        gridStackPanel.deleteAnimationHandles();

        if (provideComponents.size() > 0) {
            provideComponents.forEach(item -> {
                item.provideWithSampleData();
            });
        }
    }

    private void saveData() {
        if (!validate()) {
            return;
        }
        data.setDashboardId(objectId);
        data.setActiveComponents(gridStackPanel.getComponents());
        data.setApplyForAll(applyForAllCheckBox.getValue());
        LoadingPanel.loading(true);
        moduleDashboardService.saveDashboardComponents(data, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result == 1) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.dashboard()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MODULE_DASHBOARD_ADD, null, ModuleDashboardCustomizeWidgetView.this);
                    closeTab();
                } else if (result == -1) {
                    Info.show(wfmStrings.dashboardNotFound(), Info.Type.WARNING);
                } else if (result == -2) {
                    Info.show(wfmStrings.pleaseAddWidgets(), Info.Type.WARNING);
                }
            }
        });
    }

    private boolean validate() {
        if (!Utils.hasGenericAccess(GenericSettingsEnum.DASHBOARD_NUMBER_OF_WIDGETS_LIMITATION)) {
            if (gridStackPanel.getComponents().size() > Utils.dashboardWidgetsMaxLimit()) {
                Info.warn(wfmStrings.youCanNotAddMoreThanWidgets());
                return false;
            }
        }
        return true;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}