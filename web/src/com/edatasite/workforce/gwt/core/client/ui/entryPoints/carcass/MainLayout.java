package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomFormItemListView;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs.KpiTabContainer;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.security.ClientSecurityContext;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;

public class MainLayout extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final String STYLE_FRAME_CONTENT_2 = "frame__content--0";
    private static final MainLayout.MainLayoutUiBinder ourUiBinder = GWT.create(MainLayout.MainLayoutUiBinder.class);
    private static MainLayout instance;
    @UiField
    Div systemAlertContainer;
    @UiField
    MaterialPanel frameContainer;
    @UiField
    NavToolBar navToolBar;
    @UiField
    ModulesBar modulesBar;
    @UiField
    SideNav sideNavBar;
    @UiField
    MaterialPanel mainContent;
    @UiField
    MaterialPanel contentBody;
    @UiField
    MaterialPanel tabsContainer;
    @UiField
    MaterialTab viewTabs;
    @UiField
    MaterialPanel moreTabsContainer;
    @UiField
    MaterialLink moreTabs;
    @UiField
    MaterialDropDown moreTabsList;
    @UiField
    MaterialPanel actionsContainer;
    @UiField
    MaterialPanel modalContainer;
    @UiField
    Div tooltips;
    private SinksContainer dynamicContainer;
    private final Integer NOT_INCLUDING_WIDTH = 200; //690 width of the more tabs and back to buttons
    private Integer TAB_CONTAINER_WIDTH = 0;
    private Integer DYNAMIC_TAB_WIDTH = 0;
    private final Integer DYNAMIC_TAB_PADDING = 20;
    private SupportWidget supportWidget;

    private MainLayout() {
        this.initWidget(MainLayout.ourUiBinder.createAndBindUi(this));

        final Icon moreIcon = new Icon();
        moreIcon.setClass("ficon--more-horiz");
        this.moreTabs.add(moreIcon);
    }

    public static MainLayout get() {

        if (MainLayout.instance == null) {
            MainLayout.instance = new MainLayout();
        }

        return MainLayout.instance;
    }

    public void initialize() {
        this.modulesBar.initializeModules();
        this.navToolBar.initUserMenu();
        this.initToolTips();
        this.initSideNav();
        this.onPageOperPanelChange();
        this.initSupportWidget();
    }

    private void initSupportWidget() {
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_SUPPORT_CHAT)) {
            this.supportWidget = new SupportWidget();
            this.supportWidget.setAiChatToken(ClientSecurityContext.get().getSessionId(), Utils.getHostSubName());
            this.frameContainer.add(this.supportWidget);
        }
    }

    public void initRemoveSampleLink() {
        this.navToolBar.initRemoveSampleLink();
    }

    public Widget getRemoveSampleLink() {
        return this.navToolBar.getRemoveSampleLink();
    }

    private void initSideNav() {
        HashMap<String, String> moduleLocalizes = Utils.getModuleLocalizeMap();
        String title = MainLayout.wfmStrings.accounts();
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_ERP_TEXTILEFINDS_CUSTOMIZATION)) {
            title = MainLayout.wfmStrings.inventory();
        }
        if (Constants.MODULE_ACCOUNTING.equals(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_ACCOUNTING) != null ? moduleLocalizes.get(Constants.MODULE_ACCOUNTING) : title);
        } else if (Constants.MODULE_CRM.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_CRM) != null ? moduleLocalizes.get(Constants.MODULE_CRM) : MainLayout.wfmStrings.crm());
        } else if (Constants.MODULE_HRMS.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_HRMS) != null ? moduleLocalizes.get(Constants.MODULE_HRMS) : MainLayout.wfmStrings.hrms());
        } else if (Constants.MODULE_PM.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_PM) != null ? moduleLocalizes.get(Constants.MODULE_PM) : MainLayout.wfmStrings.projects());
        } else if (Constants.MODULE_PAYROLL.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_PAYROLL) != null ? moduleLocalizes.get(Constants.MODULE_PAYROLL) : MainLayout.wfmStrings.payroll());
        } else if (Constants.MODULE_REPORTING.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_REPORTING) != null ? moduleLocalizes.get(Constants.MODULE_REPORTING) : MainLayout.wfmStrings.reports());
        } else if (Constants.MODULE_DOCUMENTS.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_DOCUMENTS) != null ? moduleLocalizes.get(Constants.MODULE_DOCUMENTS) : MainLayout.wfmStrings.documents());
        } else if (Constants.MODULE_MYACCOUNT.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_MYACCOUNT) != null ? moduleLocalizes.get(Constants.MODULE_MYACCOUNT) : MainLayout.wfmStrings.currentSubscription());
        } else if (Constants.MODULE_MC.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_MC) != null ? moduleLocalizes.get(Constants.MODULE_MC) : MainLayout.wfmStrings.emailAccounts());
        } else if (Constants.MODULE_SETTINGS.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_SETTINGS) != null ? moduleLocalizes.get(Constants.MODULE_SETTINGS) : MainLayout.wfmStrings.settings());
        } else if (Constants.MODULE_TC.equalsIgnoreCase(GWT.getModuleName())) {
            this.sideNavBar.setMenuTitle(moduleLocalizes != null && moduleLocalizes.get(Constants.MODULE_TC) != null ? moduleLocalizes.get(Constants.MODULE_TC) : MainLayout.wfmStrings.trainingCenter());
        }
    }

    private void initToolTips() {
        this.tooltips.setVisible(false);
    }

    public void addDynamicContainer(final SinksContainer container) {
        this.addDynamicContainer(container, true);
    }

    public void addDynamicContainer(final SinksContainer container, final boolean clearConfigs) {
        if (container.getDynamicFormLookUpType() != null || container.isCrmAccount()) {
            CommonService.App.get().getDynamicCustomForms(container.isCrmAccount() ? null : container.getDynamicFormLookUpType().name(), container.isCrmAccount(), new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                @Override
                public void onFailure(final Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(final ArrayList<SelectItem> result) {
                    if (result != null && !result.isEmpty()) {
                        for (SelectItem item : result) {
                            initDynamicShortcutItem(container, item);
                        }
                    }
                    KpiTabContainer tabContainer = navToolBar.getTabContainer();
                    if (tabContainer.getTabItems() != null && !tabContainer.getTabItems().isEmpty() && tabContainer.getSelectedTab().getTabId().equals(container.getName())) {
                        addDynamicContainers(container, clearConfigs);
                    }
                }
            });
        } else {
            addDynamicContainers(container, clearConfigs);
        }


    }

    private void addDynamicContainers(final SinksContainer container, final boolean clearConfigs) {
        this.dynamicContainer = container;
        final View currentView = container.getWorkarea().getCurrentView();

        //when closing/canceling or clicking prev page then clearConfigs took "false" value and doesn't clear dynamic tabs
        if (clearConfigs) {
            this.clearDynamicTabsConfigs();
        }
        //if shown view has implemented Colapse interface then we should remove all tabs
        if (currentView != null && !(currentView instanceof NoColapse) && currentView instanceof Colapse) {
            return;
        }
        //if collapse were putted to view before its content was initialized then remove it from tabs
        if (container.isCollapse()) {
            //container.setCollapsed(false);
            return;
        }
        this.DYNAMIC_TAB_WIDTH = 0;

        //this variable is for dynamic more tabs menu
        this.TAB_CONTAINER_WIDTH = RootPanel.getBodyElement().getClientWidth() - this.NOT_INCLUDING_WIDTH;

        this.viewTabs.clear();
        this.onPageOperPanelChange();//viewTabs.clear
        boolean isTabsFulled = false; //this one is for generate more options of the tabs
        //We must clear first (issue mentioned in T3503)
        this.moreTabsList.clear();

        for (final ShortcutItem shortcut : container.getItemsByView().values()) {

            if (shortcut != null && !shortcut.isColapse()) {
                final MaterialLink link = new MaterialLink();
                link.getElement().setInnerHTML(shortcut.getDescription());
                link.setHref("#" + shortcut.getName());
                link.add(shortcut.getStatistics());
                link.addClickHandler(e -> {
                    //we must clear action buttons because they may change depending on view.
                    this.actionsContainer.clear();
                    shortcut.activate();
                });

                int length = !Utils.isNullOrEmpty(shortcut.getDescription()) ? shortcut.getDescription().length() * 8 : 0;
                this.DYNAMIC_TAB_WIDTH += this.DYNAMIC_TAB_PADDING + length; //TODO temporary solution. Don't have any idea right now!

                if (!isTabsFulled) {
                    MaterialTabItem tabItem = new MaterialTabItem();
                    tabItem.add(link);
                    viewTabs.add(tabItem);
                }

                if (!isTabsFulled && this.TAB_CONTAINER_WIDTH.compareTo(this.DYNAMIC_TAB_WIDTH) < 0) {
                    viewTabs.remove(this.viewTabs.getWidgetCount() - 1);

                    isTabsFulled = true;
                }
                if (isTabsFulled) {
                    link.setHref("javascript:void(0);");
                    this.moreTabsList.add(link);
                }
            }
        }

        if (this.viewTabs.getChildrenList() != null && !this.viewTabs.getChildrenList().isEmpty()) {
            this.frameContainer.addStyleName("has-tabs");
            this.tabsContainer.addStyleName("page-nav__tabs");
            this.tabsContainer.setVisible(true);

            if (currentView != null) {
                this.viewTabs.selectTab(container.getWorkarea().getCurrentView().getName());
            }
        }

        //if there are more tabs
        if (!this.moreTabsList.getItems().isEmpty()) {
            this.tabsContainer.addStyleName("page-nav--has-more");
        }
        this.onPageOperPanelChange(); //viewTabs.add or viewTabs.clear()
    }

    public void initDynamicShortcutItem(final SinksContainer container, final SelectItem item) {
        final ShortcutItem shortcutItem = new ShortcutItem(null, null, item.getName(), null, null, false);
        shortcutItem.setViewId(item.getId());
        shortcutItem.setText(item.getDescription());
        shortcutItem.setName(item.getName());
        shortcutItem.setColapse(false);

        final View view = new CustomFormItemListView(item.getId(), item.getCategory(), item.getCode(), container.isCrmAccount() ? "CRM_ACCOUNT" : container.getDynamicFormLookUpType().name(), container.getDynamicFormEntityId());
        view.initStatistics(item.getId(), shortcutItem.getStatistics());

        shortcutItem.setStatisticCommand(() -> view.initStatistics(item.getId(), shortcutItem.getStatistics()));

        shortcutItem.setCmd(() -> {
            container.activate(view);
        });

        container.getItemsByView().put(item.getCode(), shortcutItem);
    }

    public void removeTabsContainerFromParent() {
        if (this.tabsContainer != null) {
//            tabsContainer.removeFromParent();
            this.tabsContainer.setVisible(false);
        }
        this.onPageOperPanelChange();
    }

    public void removeFromButtonsContainer(final Widget... widgets) {
        if (widgets != null) {
            for (final Widget widget : widgets) {
                this.actionsContainer.remove(widget);
            }
        }
        this.onPageOperPanelChange();
    }

    public void addToActionsContainer(final Widget... widgets) {
        this.actionsContainer.clear();
        if (widgets != null) {
            for (final Widget widget : widgets) {
                this.actionsContainer.add(widget);
            }
        }
        this.onPageOperPanelChange();
    }

    private void onPageOperPanelChange() {
        if ((this.actionsContainer.isAttached() && this.actionsContainer.getWidgetCount() > 0) || (this.viewTabs.isAttached() && this.viewTabs.getWidgetCount() > 0)) {
            this.considerBodyHasPageOpers(true);
            this.considerPagerOpersEmptiness(false);
        } else {
            this.considerBodyHasPageOpers(false);
            this.considerPagerOpersEmptiness(true);
        }
    }

    /**
     * delete all dynamic container whose inside breadcrumb(from cache)
     */
    public void removeTreeDynamicContainer() {
        this.clearDynamicTabsConfigs();

        this.dynamicContainer = null;
    }

    public void clearDynamicTabsConfigs() {
        this.dynamicContainer = null;

        this.makeFrameContainerHaveTabsStyle(false);
        this.tabsContainer.removeStyleName("page-nav__tabs");
        this.tabsContainer.removeStyleName("page-nav--has-more");

        this.viewTabs.clear();
        this.moreTabsList.clear();
        this.actionsContainer.clear();
        this.onPageOperPanelChange(); //viewTabs, actionsContainer
    }

    public SinksContainer getCurrentContainer() {
        return this.dynamicContainer != null ? this.dynamicContainer : this.sideNavBar.getCurrentContainer();
    }

    /**
     * Clear all static and dynamic container from content if needed
     * It has been used after getting started pages
     */
    public void clearAllContainers() {
        this.sideNavBar.clearContainers();
        this.removeTreeDynamicContainer();
    }

    public void onSendfeedBack(final String from) {
        if (this.supportWidget != null) {
            this.supportWidget.onSendfeedBack(from);
        }
    }

    public NavToolBar getNavToolBar() {
        return this.navToolBar;
    }

    public SideNav getSideNavBar() {
        return this.sideNavBar;
    }

    public void resetSideNavContent() {
        this.sideNavBar.resetSideNavWidgetPlacement();
    }

    public void loadSideNavContent(final boolean value) {
        this.sideNavBar.loadSideNavContent(value);
    }

    public void replaceSideNavContent(final Widget... widget) {
        this.sideNavBar.replaceContent(widget);
    }

    public void makeFrameContainerHaveTabsStyle(final boolean value) {
        if (value) {
            this.frameContainer.addStyleName("has-tabs");
        } else {
            this.frameContainer.removeStyleName("has-tabs");
        }
        this.considerBodyHasPageOpers(value);
    }

    public MaterialPanel getMainContent() {
        return this.mainContent;
    }

    public MaterialPanel getContentBody() {
        return this.contentBody;
    }

    public MaterialPanel getModalContainer() {
        return this.modalContainer;
    }

    public void setSideNavResizeCommand(final Command sideNavResizeCommand) {
        this.modulesBar.setSideNavResizeCommand(sideNavResizeCommand);
    }

    public Div getTooltips() {
        return this.tooltips;
    }

    public void considerBodyHasOperPanel(final boolean isOperPanel) {
        if (isOperPanel) {
            RootPanel.get().addStyleName("has-operPanel");//remove when style name "operPanel__wrapper" is removed from code
        } else {
            RootPanel.get().removeStyleName("has-operPanel");//remove when style name "operPanel__wrapper" is removed from code
        }
    }

    public void considerPagerOpersEmptiness(final boolean empty) {
        if (empty) {
            RootPanel.get().addStyleName("page-opers--empty");
        } else {
            RootPanel.get().removeStyleName("page-opers--empty");
        }
    }

    public void considerBodyHasPageOpers(final boolean hasPageOpers) {
        if (hasPageOpers) {
            RootPanel.get().addStyleName("has-page-opers");//remove when style name "operPanel__wrapper" is removed from code
        } else {
            RootPanel.get().removeStyleName("has-page-opers");//remove when style name "operPanel__wrapper" is removed from code
        }
    }

    public void considerBodyHasFittedContent(final boolean contentFitted) {
        if (contentFitted) {
            RootPanel.get().addStyleName("fitted-content");
        } else {
            RootPanel.get().removeStyleName("fitted-content");
        }

    }

    public void mutateBodyWithFrameContent2(final boolean modify) {
        if (modify) {
            this.contentBody.addStyleName(MainLayout.STYLE_FRAME_CONTENT_2);
        } else {
            this.contentBody.removeStyleName(MainLayout.STYLE_FRAME_CONTENT_2);
        }
    }

    public void setSideMenuHovered(final boolean value) {
        if (value) {
            RootPanel.get().addStyleName("left-menu-hover");
        } else {
            RootPanel.get().removeStyleName("left-menu-hover");
        }
    }

    public void setSideMenuOpen(final boolean value) {
        RootPanel.get().removeStyleName("left-menu-open");
        RootPanel.get().removeStyleName("left-menu-closed");
        if (value) {
            RootPanel.get().addStyleName("left-menu-open");
        } else {
            RootPanel.get().addStyleName("left-menu-closed");
        }
    }

    public void considerBodyhasContentHeader(final boolean value) {
        if (value) {
            RootPanel.get().addStyleName("has-contentHeader");
        } else {
            RootPanel.get().removeStyleName("has-contentHeader");
        }
    }

    public void setTimerData(final Integer objectID, final int pmTask, final Integer projectId) {
        this.navToolBar.setTimerData(objectID, pmTask, projectId);
    }

    interface MainLayoutUiBinder extends UiBinder<Widget, MainLayout> {
    }

    public MaterialPanel getFrameContainer() {
        return this.frameContainer;
    }

}