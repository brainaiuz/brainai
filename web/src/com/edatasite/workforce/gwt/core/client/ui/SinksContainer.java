package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.ContentHeader;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.interfaces.NoColapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.EdsShortcuts;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialIcon;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public abstract class SinksContainer implements Constants {
    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();


    public Integer id;
    protected String[] params;
    protected Workarea workarea = new Workarea();
    private MaterialIcon icon;
    private EdsShortcuts shortcutsContainer;
    private String description;//tab name
    private String title;//tab title
    private boolean initialized;
    private boolean dynamic;
    private String name;
    private String preparedView;
    private final LinkedList<View> viewList;
    private final HashMap<String, View> viewByName = new LinkedHashMap<>();
    private CustomFieldLookUpTypeEnum dynamicFormLookUpType;
    private Integer dynamicFormEntityId;
    private boolean isCrmAccount;
    private final HashMap<String, ShortcutItem> itemsByView = new LinkedHashMap<>(); // ShortcutsItems mapped by name
    private MaterialCollapsibleItem westMenuContainer;
    private int westMenuContainerWidth = -1;

    private boolean collapse;

    /**
     * this is for browser history
     */
    private String historyToken;

    public SinksContainer(final String name, final String description, final String[] params, final int tabStyle, final int westMenuContainerWidth) {
        this(name, description, params, tabStyle, westMenuContainerWidth, true);
    }

    public SinksContainer(final String name, final String description, final String[] params, final int tabStyle, final int westMenuContainerWidth, final boolean init) {
        this(name, description, params, tabStyle, westMenuContainerWidth, init, null);
    }

    //For Dynamic
    public SinksContainer(final String name, final String description, final String[] params, final int tabStyle, final int westMenuContainerWidth, final boolean init, final LinkedList<View> viewList) {

        if (params != null && !"".equals(params[0]) && !"add".equals(params[0]) && params[0].matches(Constants.REGEX_INTEGER)) {
            id = Integer.valueOf(params[0]);
        }
        this.name = name;
        this.description = description;
        //setText(description);
        this.params = params;
        this.viewList = viewList;
        this.westMenuContainerWidth = westMenuContainerWidth != -1 ? westMenuContainerWidth : 200;

        if (Constants.CLOSE == tabStyle) {
            this.dynamic = true;
        }
        if (init) {
            this.initialize();
        }
    }

    public SinksContainer(final String name, final String description, final String[] params, final int tabStyle) {
        this(name, description, params, tabStyle, -1);
    }

    public SinksContainer(final String name, final String description, final String[] params) {
        this(name, description, params, Constants.CLOSE);
    }

    public SinksContainer(final String name, final String description) {
        this(name, description, null, Constants.CLOSE);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /*
      Tab Name
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        //setText(description);
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Workarea getWorkarea() {
        return this.workarea;
    }

    public boolean isDynamic() {
        return this.dynamic;
    }

    protected void initialize() {
        this.checkForAccess(this.getDefaultGrantAccessCommand(), this.getDefaultDenyAccessCommand());
    }

    protected void checkForAccess(final Command grantAccess, final Command denyAccess) {
        grantAccess.execute(); //grant access by default
    }

    private Command getDefaultGrantAccessCommand() {
        return () -> this.renderSinksContainer();
    }

    private Command getDefaultDenyAccessCommand() {
        return () -> Info.warn(SinksContainer.wfmStrings.youDontHavePermission());
    }

    protected void renderSinksContainer() {
        if (this.viewList != null && this.viewList.size() > 0) {
            this.initViews(this.viewList);
        } else {
            this.initViews(); //Fill Sinks
        }
        this.initShortcuts(); //Fill Shortcuts

        this.reInit();
        //setWorkarea(workarea);
        this.initialized = true;
        this.lazyDraw();
    }

    public void reInit() {
        MainLayout.get().getMainContent().clear();
        MainLayout.get().getMainContent().add(this.workarea);
        MainLayout.get().clearDynamicTabsConfigs();
    }

    protected abstract void initViews();

    protected abstract void initViews(LinkedList<View> viewList);


    private void initShortcuts() {
        if (this.viewByName.isEmpty()) {
            return;
        }
        for (View view : this.viewByName.values()) {
            initShortcutItem(view);
        }
    }

    public void initShortcutItem(final View view) {
        final ShortcutItem shortcutItem = new ShortcutItem(null, view.getIconStyle(), view.getName(), view.getAddNew(), view.getFromView(), view.isCustomForm());
        shortcutItem.setViewId(view.getViewId());
        shortcutItem.setText(view.getDescription());
        shortcutItem.setName(view.getName());
        shortcutItem.setAccessKey(view.getAccessKey());
        shortcutItem.setColapse(view instanceof Colapse && !(view instanceof NoColapse));

        view.initStatistics(this.id, shortcutItem.getStatistics());

        shortcutItem.setStatisticCommand(() -> view.initStatistics(this.id, shortcutItem.getStatistics()));

        shortcutItem.setCmd(() -> {
            this.activate(view);
        });

        this.itemsByView.put(view.getName(), shortcutItem);
    }

    public void setCollapsed(final boolean b) {
        this.collapse = b;
    }

    public boolean isCollapse() {
        return this.collapse;
    }

    public void activate(View view) {
        this.activateView(view);
    }

    private void activateView(final View view) {
        if (!this.getWorkarea().displayedView(view.getName())) {
            this.getWorkarea().display(view);
            this.initializeAsync(view);
        }

        //This is to restore state (positions) of scrolls (TaskNumber: T2731)
        view.restoreState();

//        setCollapsed(view);
        this.setContentFitted(view);
        this.setContentHasHeader(view);
//        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SINKS_CONTAINER_VIEW_SELECTED, view.getName(), null);
    }

    private void setContentHasHeader(final View view) {
        MainLayout.get().considerBodyhasContentHeader(view instanceof ContentHeader);
    }

    private void setContentFitted(final View view) {
        MainLayout.get().considerBodyHasFittedContent(view instanceof FittedContent);
    }

    public void initializeAsync(View view) {
        LoadingPanel.loading(true);
        view.asyncOnInitialize(new AbstractAsyncCallback<Widget>() {
            public void failure(final Throwable reason) {
                LoadingPanel.loading(false);
                view.removeFromParent();
                SinksContainer.this.workarea.getViewById().remove(view.getName());
                Info.show(SinksContainer.wfmStrings.networkErrorOccurred(), Info.Type.WARNING);
            }

            public void success(final Widget widget) {
                LoadingPanel.loading(false);
            }
        });
    }

    public void show(final String viewName) {
        ShortcutItem shortcutItem = null;

        if (viewName != null && !viewName.isEmpty()) {
            shortcutItem = this.itemsByView.get(viewName);

            if (shortcutItem == null) {
                shortcutItem = this.itemsByView.values().iterator().next();
            }
        } else {
            shortcutItem = this.itemsByView.values().iterator().next();
        }

        if (shortcutItem != null) {
            shortcutItem.activate();
        }
    }

    public Set getViewsName() {
        return this.itemsByView.keySet();
    }

    /**
     * <i>... This is method changes to custom View builds ...</i>
     * <br/>
     * <b>... This method changed by developer {Dilshod.T} ...</b>
     * <br/>
     * <b>... Updated Date {14:20 11/05.2011} ...</b>
     * <br/>
     *
     * @param view
     */
    public void addView(final View view) {
        if (SinksContainerFactory.entryPoint.moduleSetting.isAddView(this.name, view.getName(), this.id)) {
            view.setContainer(this);
            this.viewByName.put(view.getName(), view);
        }
    }

    public void addDynamicView(final CustomFieldLookUpTypeEnum lookUpType, final Integer id) {
        dynamicFormLookUpType = lookUpType;
        dynamicFormEntityId = id;
    }

    public void addDynamicView(final boolean isCrmAccount, final Integer id) {
        this.isCrmAccount = isCrmAccount;
        dynamicFormEntityId = id;
    }

    public HashMap getViewByName() {
        return this.viewByName;
    }

    public String getPreparedView() {
        return this.preparedView;
    }

    public void setPreparedView(final String prepareToView) {
        preparedView = prepareToView;
    }

    public void showPrepared() {
        this.show(this.getPreparedView());
    }

    public EdsShortcuts getShortcutsContainer() {
        return this.shortcutsContainer;
    }

    public HashMap<String, ShortcutItem> getItemsByView() {
        return this.itemsByView;
    }

    public CustomFieldLookUpTypeEnum getDynamicFormLookUpType() {
        return dynamicFormLookUpType;
    }

    public Integer getDynamicFormEntityId() {
        return dynamicFormEntityId;
    }

    public boolean isCrmAccount() {
        return isCrmAccount;
    }

    public MaterialCollapsibleItem getWestMenuContainer() {
        return this.westMenuContainer;
    }

    public void setWestMenuContainer(final MaterialCollapsibleItem westMenuContainer) {
        this.westMenuContainer = westMenuContainer;
    }

    /**
     * <i>... This method return custom module views params ...</i>
     * <br/>
     * <i>... Writer developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {16:17 21/05/2011} ...</i>
     *
     * @param paramName
     * @return
     */
    public String getModuleParam(final String paramName) {
        return SinksContainerFactory.entryPoint.moduleSetting.getParams().get(paramName);
    }

    private void lazyDraw() {

    }

    public void removeWfmUiEvents() {
        for (final View view : this.viewByName.values()) {
            WfmUiEventsBus.unSubscribeWfmUiEvents(view.getEventList());
            view.clear();
            view.removeFromParent();
        }

        this.viewByName.clear();
        this.workarea.getViewById().clear();
        this.workarea.removeFromParent();
    }

    public String normalizeName(final String name) {
        String containerName = "";
        if (name != null) {
            containerName = name.replace(" ", "_")
                    .replace("/", "_");
        }
        return containerName;
    }

    public MaterialIcon getIcon() {
        return this.icon;
    }

    public void setIcon(final MaterialIcon icon) {
        this.icon = icon;
    }

    public String getHistoryToken() {
        return this.historyToken;
    }

    public void setHistoryToken(final String historyToken) {
        this.historyToken = historyToken;
    }

    protected String localizeDefaultDashboardNames(final String dashboardName) {
        if (dashboardName.equals(Constants.DASHBOARD_NAMES.DASHBOARD)) {
            return SinksContainer.wfmStrings.dashboard();
        }
        if (dashboardName.equals(Constants.DASHBOARD_NAMES.EMPLOYEE_PORTAL)) {
            return SinksContainer.wfmStrings.employeePortal();
        }
        if (dashboardName.equals(Constants.DASHBOARD_NAMES.HR_DASHBOARD)) {
            return SinksContainer.wfmStrings.hrDashboard();
        }
        return dashboardName;
    }
}
