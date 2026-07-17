package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;

public class KpiTabItem extends MaterialTabItem {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    /**
     * container name used as a tabId
     */
    private String containerName;

    /**
     * Tab link responses a click tab action
     */
    private MaterialLink tabLink;

    /**
     * closing a tab action
     */
    private Icon closeTabIcon;

    /**
     * contains a tab title and manages editable it
     */
    private Span tabName;

    /**
     * list of container changes that are changed
     */
    private List<String> changedContainers = new ArrayList<>();

    public KpiTabItem() {
        super();
        initialize();
    }

    public KpiTabItem(SinksContainer container) {
        this();
        setContainer(container);
        registerChangeListener(containerName, KpiTabItem.this);
    }

    private void initialize() {
        tabLink = new MaterialLink();
        tabLink.setHref("#tab");
        tabLink.addClickHandler(e -> {
            e.preventDefault();
            e.stopPropagation();
            onSelect();
        });

        tabName = new Span();
        tabName.getElement().setInnerHTML("Tab");
        tabName.setTitle("Tab");

        tabLink.add(tabName);

        closeTabIcon = new Icon();
        closeTabIcon.setClass("close");

        closeTabIcon.addClickHandler(e -> {
            if (!changedContainers.contains(containerName)) {
                e.preventDefault();
                e.stopPropagation();
                onClose();
            } else {
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                messageBox.setMessage(wfmStrings.youhaveUnsavedChange());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        onClose();
                    }
                });
                messageBox.open();
            }
        });
        tabLink.add(closeTabIcon);
        add(tabLink);
    }

    public void onSelect() {

        if (containerName != null) {

            SinksContainer container = SinksContainerFactory.entryPoint.getContainerFactory().getContainerByName(containerName);

            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SELECTION_CHANGE_TAB, container, KpiTabItem.this);

            if (getParent() instanceof KpiTabContainer) {
                ((KpiTabContainer) getParent()).setSelection(this);
            }
        }
    }

    public void onClose() {
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BEFORE_REMOVE_TAB, SinksContainerFactory.entryPoint.getContainerFactory().getContainerByName(containerName), KpiTabItem.this);

        if (getParent() instanceof KpiTabContainer) {
            ((KpiTabContainer) getParent()).removeItem(this);
        }

        if ("customizeFormadd".equals(containerName)) {
            Utils.enableLeftMenu(true);
        }
        changedContainers.remove(containerName);
    }

    public void setContainer(SinksContainer container) {
        containerName = container.getName();

        tabLink.setHref("#" + containerName);

        tabName.getElement().setInnerHTML(container.getDescription());
        tabName.setTitle(container.getDescription());

        if (!container.isDynamic()) {
            closeTabIcon.removeFromParent();
        }
    }

    public void setTabName(String name) {
        this.setTabName(name, null);
    }

    public void setTabName(String name, String title) {
        tabName.getElement().setInnerHTML(name);
        if (!Utils.isNullOrEmpty(title)) {
            tabName.setTitle(title);
        } else {
            tabName.setTitle(name);
        }
    }

    public String getTabId() {
        return containerName;
    }

    public static native void registerChangeListener(String containerName, KpiTabItem view) /*-{
        var mainInterval = setInterval(function () {
            if ($wnd.jQuery(".whiteBGR").find(":input").length) {
                clearInterval(mainInterval);
                $wnd.jQuery(".whiteBGR").find(":input").change(function () {
                    view.@com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs.KpiTabItem::onFormChanged(Ljava/lang/String;)(containerName);
                });
            }
        }, 3500);
    }-*/;

    public void onFormChanged(String containerName) {
        if (!changedContainers.contains(containerName)) {
            changedContainers.add(containerName);
        }
    }
}
