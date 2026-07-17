package com.edatasite.workforce.gwt.core.client.ui.components.dashboard;

import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.GettingStartedItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.UserDashboardSettingsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DashboardBaseWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.dashboardwidget.client.utils.DashboardUtils;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.dynamicwidget.WidgetRequest;
import com.edatasite.workforce.gwt.dashboardwidget.client.view.settings.GettingStartedWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardContainer extends Composite {
    interface DashboardContainerUiBinder extends UiBinder<HTMLPanel, DashboardContainer> {
    }

    private static final DashboardContainerUiBinder ourUiBinder = GWT.create(DashboardContainerUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel mainContent;
    @UiField
    Div header;
    @UiField
    Div removeSampleContainer;
    @UiField
    HTMLPanel content;
    @UiField
    HTMLPanel subContent;
    @UiField
    GridStackPanel container;
    @UiField
    Span emptyMessage;

    protected Integer dashboardId;

    private WfmButton2 saveButton;
    private WfmButton2 cancelButton;
    private DataListBox inactiveListBox;
    protected ArrayList<DashboardComponentItem> activeComponents = new ArrayList<>();
    protected ArrayList<DashboardComponentItem> inactiveComponents = new ArrayList<>();
    private final ArrayList<KpiWidgetData> kpiWidgetDataList = new ArrayList<>();
    private final LinkedHashMap<Integer, DashboardComponentItem> dashboards = new LinkedHashMap<>();

    public DashboardContainer(Integer dashboardId) {
        this.dashboardId = dashboardId;

        initWidget(ourUiBinder.createAndBindUi(this));
        Utils.userSettings.put(Constants.ENABLE_TO_SHOW_SAMPLE_DATA, "false");
        removeSampleContainer.removeFromParent();
        initialize();
    }

    private void initialize() {
        emptyMessage.setText("Please add a widgets");
        emptyMessage.setDisplay(Display.NONE);

        cancelButton = new WfmButton2(wfmStrings.cancel());
        cancelButton.addClickHandler(clickEvent -> {
            LoadingPanel.loading(true);
            resetAllItemPositions();
            initCustomizeView(true);
            LoadingPanel.loading(false);
        });
        cancelButton.setVisible(false);

        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> saveUserDashboardSettings());
        saveButton.setVisible(false);

        inactiveListBox = new DataListBox(false, true);
        inactiveListBox.setNullLabel(wfmStrings.addWidget());
        inactiveListBox.setDisplay(Display.NONE);
        inactiveListBox.addValueChangeHandler(event -> {
            if (inactiveListBox.getSelectedItem() != null && inactiveListBox.getSelectedItem() instanceof DashboardComponentItem) {
                DashboardComponentItem selectedItem = (DashboardComponentItem) inactiveListBox.getSelectedItem();
                if (selectedItem.getComponentCode() != null) {
                    drawWidget(selectedItem, true, dashboardId);
                    loadChart(selectedItem.getComponentCode());

                    inactiveListBox.removeBySelectItemId(inactiveListBox.getSelectedItem());
                    if (inactiveListBox.getItemCount() <= 1) {
                        inactiveListBox.setDisplay(Display.NONE);
                    }
                }
            }
        });
        Div panelBox = new Div("panel-box panel-box--right");
        Div inactiveItem = new Div("panel-box__item");
        inactiveItem.add(inactiveListBox);
        panelBox.add(inactiveItem);

        Div saveItem = new Div("panel-box__item");
        saveItem.add(saveButton);
        panelBox.add(saveItem);

        Div cancelItem = new Div("panel-box__item");
        cancelItem.add(cancelButton);
        panelBox.add(cancelItem);

        header.add(panelBox);

        loadSetupConfiguration();

        loadConfigs();

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DASHBOARD_SETTINGS_CLICK, DashboardContainer.this, (sender, args) -> {
            if (args != null && args.equals(dashboardId)) {
                initCustomizeView(false);
            }
        });

        MainLayout.get().setSideNavResizeCommand(new Command() {
            @Override
            public void execute() {
                if (!container.getGridItemMap().isEmpty()) {
                    Timer timer = new Timer() {
                        @Override
                        public void run() {
                            for (GridStackItemPanel gridStackItem : container.getGridItemMap().values()) {
                                if (gridStackItem == null || !(gridStackItem.getItemWidget() instanceof DashboardBaseWidget)) {
                                    continue;
                                }
                                DashboardBaseWidget component = ((DashboardBaseWidget) gridStackItem.getItemWidget());
                                component.resizeChart();
                            }
                        }
                    };
                    timer.schedule(320);
                }
            }
        });
    }

    private void loadSetupConfiguration() {
        if (dashboardId != null) {
            ModuleDashboardService.App.get().getDashboardSetupConfiguration(dashboardId, new AsyncCallback<ArrayList<GettingStartedItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ArrayList<GettingStartedItem> items) {
                    if (items != null) {
                        GettingStartedWidget gettingStarted = new GettingStartedWidget(items);
                        gettingStarted.init();
                        subContent.add(gettingStarted);
                    } else {
                        subContent.removeFromParent();
                    }
                }
            });
        }
    }

    private void loadConfigs() {
        getContainer().initializeConfig(true);

        if (dashboardId != null) {

            LoadingPanel.loading(true);
            ModuleDashboardService.App.get().getUserDashboardSettings(dashboardId, new AbstractAsyncCallback<UserDashboardSettingsItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(UserDashboardSettingsItem result) {
                    LoadingPanel.loading(false);
                    activeComponents = result.getActiveComponents();
                    inactiveComponents = result.getInactiveComponents();

                    if (result.getActiveComponents().size() > 0) {
                        initializeWidgets();
                    } else {
                        getEmptyMessage().setDisplay(Display.BLOCK);
                    }
                }
            });
        }
    }

    protected void initializeWidgets() {
        //Inactive component configs
        container.setInactiveListBox(inactiveListBox);

        if (inactiveComponents.size() > 0) {
            inactiveListBox.setItems(inactiveComponents.toArray(new DashboardComponentItem[]{}));
        }

        //drawing and loading active components
        if (activeComponents != null && activeComponents.size() > 0) {
            WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WIDGET_DATA_RECEIVED, new Widget(), (sender, args) -> {
                WidgetRequest request = (WidgetRequest) args;
                if (dashboardId.equals(request.getId())) {
                    kpiWidgetDataList.add(request.getKpiWidgetData());
                }
            });
            for (DashboardComponentItem componentItem : activeComponents) {

                if (componentItem == null || componentItem.getComponentCode() == null) {
                    continue;
                }
                drawWidget(componentItem, false, dashboardId);
                dashboards.put(componentItem.getReportId(), componentItem);
            }
            getContainer().commit();
            getContainer().registerHandles();
        }

        if (container.getComponentCodes() != null && !container.getComponentCodes().isEmpty()) {
            container.getComponentCodes().forEach(this::loadChart);
        }
    }

    public void reInitializeWidgets() {
        if (container.hasInitialized()) {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_RELOAD_DASHBOARD_COMPONENTS + dashboardId, null, DashboardContainer.this);
        } else {
            container.initializeConfig(true);
        }
    }

    protected void drawWidget(DashboardComponentItem componentItem, boolean autoPosition, Integer dashboardId) {
        DashboardBaseWidget component = DashboardUtils.generateComponent(componentItem, saveButton.isVisible(), dashboardId);
        if (component != null) {
            addItem(component, autoPosition);
        }
    }

    protected void loadChart(String componentCode) {

        if (container.getGridItemMap() != null && !container.getGridItemMap().isEmpty()) {
            container.getGridItemMap().values().forEach(item -> {

                if (item.getComponentCode().equals(componentCode)) {
                    ((DashboardBaseWidget) item.getItemWidget()).provide();
                }
            });
        }
    }

    private void addItem(DashboardBaseWidget item, boolean autoPosition) {
        GridStackItemPanel itemPanel = new GridStackItemPanel();
        itemPanel.setItemWidget(item);

        if (item.getGridItemConfig() != null) {
            itemPanel.initConfig(item.getGridItemConfig());
        }
        itemPanel.getContent().add(item);
        container.addNewItem(itemPanel, autoPosition);
        container.rewriteItemResizeHandle();
    }

    private void resetAllItemPositions() {
        getContainer().removeAllItem();

        inactiveListBox.clearSelected();
        inactiveListBox.removeListItems();
        if (inactiveComponents.size() > 0) {
            inactiveListBox.setItems(inactiveComponents.toArray(new DashboardComponentItem[]{}));
        }
        container.setInactiveListBox(inactiveListBox);
        if (activeComponents != null && activeComponents.size() > 0) {
            for (DashboardComponentItem componentItem : activeComponents) {
                if (componentItem == null || componentItem.getComponentCode() == null) {
                    continue;
                }
                drawWidget(componentItem, false, dashboardId);
            }
        }
        if (container.getComponentCodes() != null && !container.getComponentCodes().isEmpty()) {
            container.getComponentCodes().forEach(this::loadChart);
        }
    }

    private void getUserDashboardSettings() {
        LoadingPanel.loading(true);
        ModuleDashboardService.App.get().getUserDashboardSettings(dashboardId, new AbstractAsyncCallback<UserDashboardSettingsItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(UserDashboardSettingsItem result) {
                LoadingPanel.loading(false);
                activeComponents = result.getActiveComponents();
                inactiveComponents = result.getInactiveComponents();
                initCustomizeView(true);
            }
        });
    }

    private void saveUserDashboardSettings() {
        LoadingPanel.loading(true);
        mainContent.addStyleName("dashboard__container--loading");
        if (!validate()) {
            LoadingPanel.loading(false);
            mainContent.removeStyleName("dashboard__container--loading");
            return;
        }
        UserDashboardSettingsItem item = new UserDashboardSettingsItem();
        item.setDashboardId(dashboardId);
        item.setActiveComponents(getContainer().getComponents());
        ModuleDashboardService.App.get().saveUserDashboardSettings(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                mainContent.removeStyleName("dashboard__container--loading");
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                if (result == 1) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.dashboards()), Info.Type.INFO);
                    LoadingPanel.loading(true);
                    getUserDashboardSettings();
                } else if (result == -1) {
                    Info.show(wfmStrings.dashboardIsEmpty(), Info.Type.WARNING);
                } else if (result == -2) {
                    Info.show("User not found", Info.Type.WARNING);
                }
                LoadingPanel.loading(false);
                mainContent.removeStyleName("dashboard__container--loading");
            }
        });
    }

    private boolean validate() {
        int errors = 0;

        if (getContainer().getComponents().size() <= 0) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.dashboardIsEmpty(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void visibleButtons(boolean isVisible) {
        saveButton.setVisible(isVisible);
        cancelButton.setVisible(isVisible);
        if (isVisible && inactiveListBox.getItemCount() > 1) {
            inactiveListBox.setDisplay(Display.BLOCK);
        } else {
            inactiveListBox.setDisplay(Display.NONE);
        }
    }

    private void initCustomizeView(boolean isStatic) {
        if (isStatic) {
            mainContent.removeStyleName("dashboard__container--edit");
            getContainer().initializeConfig(true);
            getContainer().unbindDeleteAnimationHandles();
            visibleButtons(false);
            content.removeStyleName("dashboard__content--edit");
        } else {
            mainContent.addStyleName("dashboard__container--edit");
            getContainer().initializeConfig(false);
            getContainer().deleteAnimationHandles();
            visibleButtons(true);
            content.addStyleName("dashboard__content--edit");
        }
        resetAllItemPositions();
    }

    public GridStackPanel getContainer() {
        return container;
    }

    public HTMLPanel getMainContent() {
        return mainContent;
    }

    public Div getHeader() {
        return header;
    }

    public Span getEmptyMessage() {
        return emptyMessage;
    }

    public List<KpiWidgetData> getKpiWidgetDataList() {
        return kpiWidgetDataList;
    }

    public Map<Integer, DashboardComponentItem> getDashboards() {
        return dashboards;
    }
}
