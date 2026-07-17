package com.edatasite.workforce.gwt.core.client.ui.wfmTabPanel;

import com.edatasite.workforce.gwt.core.client.ui.clickablePanel.ClickablePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.04.2009
 * Time: 12:08:54
 * To change this template use File | Settings | File Templates.
 */
public class WfmTabPanel extends Composite {

    private final String tabFixedHeight = "20px";

    /**
     * All css styles applyed in this panel.
     */
    private final String defaultTab = "default-tab-style";
    private final String onTabSelect = "on-tab-select";
    private final String lastArrange = "-last-arrange";
    private final String tabPanelStyle = "tab-panel-style";
    private final String beginArrange = onTabSelect + "-begin-arrange";
    private final String endArrange = onTabSelect + "-end-arrange";
    private final String onSelectLastArrange = onTabSelect + lastArrange;
    private final String defaultArrange = "un-tab-select";
    private final String unSelectLastArrange = defaultArrange + lastArrange;

    private WfmTabListener listener;

    private FlowPanel tabPanel;
    private HorizontalPanel tabs;
    private Widget selectedWidget;

    private int selectedTabIndex;

    private List tabsList;
    private List widgetsList;

    public WfmTabPanel() {
        initialize();
    }

    private void initialize() {
        tabPanel = new FlowPanel();
        tabPanel.setStyleName(tabPanelStyle);
        tabPanel.setSize("100%", "100%");
        tabs = new HorizontalPanel();

        tabsList = new ArrayList();
        widgetsList = new ArrayList();
        tabs.addStyleName("stepsPanel");
        VerticalPanel panel = new VerticalPanel();
        panel.add(tabs);
        panel.add(tabPanel);
        panel.setCellHeight(tabs, tabFixedHeight);
        panel.setHeight("100%");
        panel.setWidth("100%");
        initWidget(panel);
    }

    public void addTab(Widget widget, String tabText) {
        addTab(widget, tabText, true);
    }

    public void addTab(Widget widget, String tabText, String tabWidth) {
        insertTab(widget, tabText, getTabCount(), tabWidth);
    }

    public void addTab(Widget widget, String tabText, boolean asHTML) {
        insertTab(widget, tabText, getTabCount(), asHTML, null);
    }

    public void insertTab(Widget widget, String tabText, int beforeIndex, String tabWidth) {
        insertTab(widget, tabText, beforeIndex, true, tabWidth);
    }

    /**
     * Inserts a widget and puts text to the tab.
     *
     * @param widget      - it will be put in the panel
     * @param tabText     - text of the tab
     * @param beforeIndex - index of the tab
     * @param asHTML      - applyes HTML styles to the tabText if it is true.
     */
    public void insertTab(Widget widget, String tabText, int beforeIndex, boolean asHTML, String tabWidth) {
        if (checkInsertBeforeTabIndex(beforeIndex)) {
            return;
        }

        if (beforeIndex > 1) {
            putIconToTab(beforeIndex - 1, defaultArrange);
        }
        widgetsList.add(beforeIndex, widget);

        Tab newTab = new Tab(tabText, beforeIndex, tabWidth, asHTML);
        tabsList.add(beforeIndex, newTab);

        tabs.insert(newTab, beforeIndex);
        tabPanel.insert(widget, beforeIndex);

        widget.setVisible(false);
    }

    private boolean checkInsertBeforeTabIndex(int index) {
        return index > 0 && index < getTabCount();
    }

    /**
     * Gets the number of tabs present.
     *
     * @return the tab count
     */
    public int getTabCount() {
        return widgetsList.size();
    }

    public void selectTab(int tabIndex) {
        onSelectTab(tabIndex);
    }

    public int getSelectedTab() {
        return selectedTabIndex;
    }

    public Widget getTabWidget(int tabIndex) {
        return (Widget) widgetsList.get(tabIndex);
    }

    public Tab getTab(int tabIndex) {
        return (Tab) tabsList.get(tabIndex);
    }

    public void setWidth(String width) {
        tabPanel.setWidth(width);
    }

    public void setHeight(String height) {
        tabPanel.setHeight(height);
    }

    public void setSize(String width, String height) {
        tabPanel.setSize(width, height);
    }

    public void addTabListener(WfmTabListener listener) {
        this.listener = listener;
    }

    private void onSelectTab(int index) {
        if (listener != null) {
            if (listener.onBeforeTabSelected(index)) {
                listener.onTabSelected(index);
            } else {
                return;
            }
        }
        /**
         * These both previous selectedWidget and selectedTabIndex.
         * After the re-drawing and re-putting styles they accept new current selected tab parameters.
         */
        if (selectedWidget != null) {
            selectedWidget.setVisible(false);

            if (selectedTabIndex != 0) {
                putIconToTab(selectedTabIndex - 1, defaultArrange);
            }

            putIconAndStyleToTab(selectedTabIndex, defaultTab, selectedTabIndex == getTabCount() - 1 ? unSelectLastArrange : defaultArrange);
        }

        /**
         * Initializing new current tab value and widget.
         */
        selectedTabIndex = index;
        selectedWidget = getTabWidget(selectedTabIndex);
        selectedWidget.setVisible(true);

        if (selectedTabIndex != 0) {
            putIconToTab(selectedTabIndex - 1, endArrange);
        }

        putIconAndStyleToTab(selectedTabIndex, onTabSelect, selectedTabIndex == getTabCount() - 1 ? onSelectLastArrange : beginArrange);
    }

    private Tab putIconToTab(int tabIndex, String iconStyle) {
        Tab tab = getTab(tabIndex);
        tab.getPointer().setStyleName(iconStyle);
        return tab;
    }

    private void putIconAndStyleToTab(int tabIndex, String style, String iconStyle) {
        putIconToTab(tabIndex, iconStyle).setStyleName(style);
    }

    private class Tab extends ClickablePanel {

        private final String fixedWidth = "13px";
        private final int defaultWidth = 50;

        private Label text;
        private int index;
        private String width;
        private HTML pointer;

        public Tab(String text, int index, String width, boolean asHTML) {
            this.text = asHTML ? new HTML(text) : new Label(text);
            this.index = index;
            this.width = width;

            drawTab();
        }

        private String getWidth(String width) {
            int calcWidth = Double.valueOf(text.getText().length() * 6.5).intValue();
            return width != null ? width : (calcWidth > defaultWidth ? calcWidth : defaultWidth) + "px";
        }

        private void drawTab() {
            pointer = new HTML();
            pointer.setSize(fixedWidth, tabFixedHeight);
            pointer.setStyleName(/*defaultArrange*/unSelectLastArrange);

            text.setHorizontalAlignment(Label.ALIGN_CENTER);
            if (width != null) {
                text.setWidth(width);
            }

            addHorizontally(text);
            addHorizontally(pointer);
            addClickHandler(event -> onSelectTab(index));

            setCellHorizontalAlignment(text, ClickablePanel.ALIGN_CENTER);
            setCellVerticalAlignment(text, ClickablePanel.ALIGN_MIDDLE);
            setStyleName(defaultTab);
        }

        public HTML getPointer() {
            return pointer;
        }
    }
}
