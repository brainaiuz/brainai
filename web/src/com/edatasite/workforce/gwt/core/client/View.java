package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs.KpiTabItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEvent;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 11, 2008 Time: 8:15:14 PM To
 * change this template use File | Settings | File Templates.
 */

public abstract class View extends FlowPanel implements ShortCutStatistics {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    protected static final CrmStrings crmStrings = CrmStrings.App.get();

    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected final MultiWidgetHandler handler = new MultiWidgetHandler();
    private String name;
    private String description;
    private String fromView;
    private int nonSavedFieldsCount = 0;
    protected int verticalScrollPosition = 0;
    protected SinksContainer container;
    private boolean collapse = false;
    protected FlowPanel helpPanel;
    private List<WfmUiEvent> eventList;
    private int lastScrollTop;
    protected Integer viewId;
    private Object addNew;
    protected Property property;
    private List<KpiWidgetData> widgetDataList;
    private Map<Integer, DashboardComponentItem> dashboards;
    private boolean customForm = false;

    public View(String name) {
        this.name = name;
        property = new Property(getPropertyCode());
    }

    public View(String name, String description) {
        this.name = name;
        this.description = description;
        if (getPropertyCode() != null) {
            property = new Property(getPropertyCode());
        }
    }

    public View(String name, String description, String propertyCode) {
        this.name = name;
        this.description = description;
        if (propertyCode != null) {
            property = new Property(propertyCode);
        }
    }

    public View() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*Use either goTo() or closeTab() methods*/

    public static void goTo(String destination) {
        goTo(destination, null);
    }

    public static void goTo(String destination, String tabName) {
        goTo(destination, tabName, null);
    }

    public static void goTo(String destination, String tabName, String tabTitle) {
        SinksContainerFactory.entryPoint.onHistoryChanged(destination, tabName, tabTitle);
    }

    public void closeTab() {
        closeTab(null);
    }

    public void closeTab(String destination) {
        closeTab(destination, null);
    }

    public void closeTab(String destination, String tabName) {
        closeTab(destination, tabName, null);
    }

    public void closeTab(String destination, String tabName, String tabTitle) {
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REMOVE_TAB, container, MainLayout.get().getNavToolBar());

        if (destination != null) {
            goTo(destination, tabName, tabTitle);
        }
    }

    public void removeWfmUiEvents() {
        if (container != null) {
            container.removeWfmUiEvents();
        }
    }

    public void setAddNew(String addNew) {
        this.addNew = addNew;
    }

    public void setAddNew(Command addNew) {
        this.addNew = addNew;
    }

    /*public void refreshOnDemand(String targetViewName, String fromViewName) {
        ObservableValve.observerObservable.put(targetViewName, fromViewName);
    }*/

    public String getFromView() {
        return fromView;
    }

    public void setFromView(String fromView) {
        this.fromView = fromView;
    }

    public int getNonSavedFieldsCount() {
        return nonSavedFieldsCount;
    }

    public void dataModified() {
        setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
    }

    public void setNonSavedFieldsCount(int nonSavedFieldsCount) {
        this.nonSavedFieldsCount = nonSavedFieldsCount;
    }

    public void resetNonSavedFieldsCount() {
        this.nonSavedFieldsCount = 0;
    }

    protected int getVerticalScrollPosition() {
        return verticalScrollPosition;
    }

    protected void setVerticalScrollPosition(int verticalScrollPosition) {
        this.verticalScrollPosition = verticalScrollPosition;
    }

    public void setContainer(SinksContainer container) {
        this.container = container;
    }

    public SinksContainer getContainer() {
        return container;
    }

    public Widget onReadyToInitialize() {
        return onInitialize();
    }

    protected Widget onInitialize() {
        return null;
    }

    public abstract String getIconStyle();

    public char getAccessKey() {
        return ' ';
    }

    public ImageResource getIconImage() {
        return null;
    }

    public FlowPanel getHelpContainer() {
        return null;
    }

    public FlowPanel getProfileContainer() {
        return null;
    }

    public void showMessage(IconEnum iconEnumStyle, String text, String message) {
        showMessage(iconEnumStyle, null, text, message, false);
    }

    public void showMessage(IconEnum iconEnumStyle, String width, String text, String message, boolean closeTab) {
        LoadingPanel.loading(false);
        final WfmMessageBox messageBox = new WfmMessageBox(iconEnumStyle, Action.OK, true);
        if (width != null) {
            messageBox.setWidth(width);
        }
        messageBox.setTitle(text);
        messageBox.setMessage(message);
        messageBox.open();
        if (closeTab) {
            messageBox.addCloseHandler(popupPanelCloseEvent -> closeTab());
        }
    }

    public boolean isEmpty(Object obj) {
        boolean bool = true;
        if (obj != null) {
            bool = "".equals(obj.toString());
        }
        return bool;
    }

    /**
     * <i>... Customise module params ...</i>
     * <br/>
     * <i>... Developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {16:36 21/05/2011} ...</i>
     *
     * @param paramName Param Name
     * @return
     */
    protected String getModuleParam(String paramName) {
        return container.getModuleParam(paramName);
    }

    public abstract void asyncOnInitialize(final AsyncCallback<Widget> callback);

    public ListingActionMenu getActionTools() {
        return null;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isCollapse() {
        return collapse;
    }

    public int getLastScrollTop() {
        return lastScrollTop;
    }

    public void setLastScrollTop(int lastScrollTop) {
        this.lastScrollTop = lastScrollTop;
    }

    public Object getAddNew() {
        return addNew;
    }

    public void restoreState() {
        if (getLastScrollTop() > 0) {
            Utils.scrollTo(getLastScrollTop());
        }
    }

    public boolean isCustomForm() {
        return customForm;
    }

    public void setCustomForm(boolean customForm) {
        this.customForm = customForm;
    }

    private class MultiWidgetHandler implements ValueChangeHandler, ClickHandler, BlurHandler, ChangeHandler {
        protected void handleIt() {
            setNonSavedFieldsCount(getNonSavedFieldsCount() + 1);
//            Window.alert("Count: " + getNonSavedFieldsCount());
        }

        public void onBlur(BlurEvent e) {
            handleIt();
        }

        public void onClick(ClickEvent e) {
            handleIt();
        }

        public void onValueChange(ValueChangeEvent e) {
            handleIt();
        }

        public void onChange(ChangeEvent e) {
            handleIt();
        }
    }

    public List<WfmUiEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<WfmUiEvent> eventList) {
        this.eventList = eventList;
    }

    public void addWfmUiEvent(WfmUiEvent event) {
        if (eventList == null) {
            eventList = new ArrayList<>();
        }
        eventList.add(event);
    }

    public void reInitialize() {

    }

    public Integer getViewId() {
        return viewId;
    }

    protected ActionButton getAddNewButton() {
        return getAddNewButton(ActionButton.Type.BUTTON);
    }

    protected ActionButton getAddNewButton(ActionButton.Type type) {
        ActionButton actionButton = new ActionButton("<div class=\"btn btn--new btn--circle\"><svg class=\"icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg></div>", type);
        actionButton.ensureDebugId(getName() != null && getName().length() > 0 ? getName() + "_new_button_id" : "create_button");
        return actionButton;
    }

    public void showAdvancedOptions(String title, Widget advancedOptions) {
        KpiSideNavBox popUp = new KpiSideNavBox();
        popUp.addBody(advancedOptions);
        popUp.getContentFooter().removeFromParent();
        popUp.show();
    }

    protected Div wrapToDiv(Widget btn) {
        Div wrapper = new Div();
        wrapper.add(btn);
        return wrapper;
    }

    public String getPropertyCode() {
        return null;
    }


    public void removeDeletedTab(String containerName) {
        for (KpiTabItem kpiTabItem : MainLayout.get().getNavToolBar().getTabContainer().getTabItems()) {
            if (kpiTabItem.getTabId().equals(containerName)) {
                kpiTabItem.onClose();
                break;
            }
        }
    }

    public List<KpiWidgetData> getWidgetDataList() {
        return widgetDataList;
    }

    public void setWidgetDataList(List<KpiWidgetData> widgetData) {
        this.widgetDataList = widgetData;
    }

    public Map<Integer, DashboardComponentItem> getDashboards() {
        return dashboards;
    }

    public void setDashboards(Map<Integer, DashboardComponentItem> dashboards) {
        this.dashboards = dashboards;
    }
}
