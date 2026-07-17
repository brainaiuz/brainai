/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/6 4:27:31                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui.customtabbar;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DecoratedTabBar;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: ${Dilsh0d}
 * Date: 14.10.2009
 * Time: 22:36:27
 * To change this template use File | Settings | File Templates.
 */
public class CustomTabBar extends Composite {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private CustomTabWidget[] tabWidgets;
    private int step = 0;
    private boolean onClickRef = false;
    private Image clickBut;
    private DecoratedTabBar tabBar;
    private VerticalPanel tabPanel;
    private DeckPanel deck;
    HorizontalPanel tabBarHP;
	private Command refreshCommand;

    public CustomTabBar(int tabCount) {
        tabBar = new DecoratedTabBar();
        tabBarHP = new HorizontalPanel();
        tabBarHP.setSpacing(0);
        tabBarHP.add(tabBar);
        tabBarHP.setCellVerticalAlignment(tabBar, HasVerticalAlignment.ALIGN_BOTTOM);
        tabBarHP.setCellWidth(tabBar, "10%");
        tabPanel = new VerticalPanel();
        deck = new DeckPanel();
        tabPanel.add(tabBarHP);
        tabPanel.add(deck);
        this.tabWidgets = new CustomTabWidget[tabCount > 0 ? tabCount : 0];
        initWidget(tabPanel);
        addTabBarListener();
    }

    /* add custom tab */

    public void addWidget(CustomTabWidget companent) {
        tabWidgets[step] = companent;
        tabWidgets[step].setTabBar(this);
        tabPanelAdd(tabWidgets[step], companent.getTabName());
        step++;
    }

    private void tabPanelAdd(CustomTabWidget tabWidget, Object tabName) {
        deck.add(tabWidget);
        if (tabName instanceof String) {
            tabBar.addTab((String) tabName);
        } else {
            tabBar.addTab((Widget) tabName);
        }
    }

    public void addWidget(CustomTabWidget companent, Widget widget) {
        tabWidgets[step] = companent;
        tabWidgets[step].setTabBar(this);
        HorizontalPanel horz = new HorizontalPanel();
        HTML tabName = new HTML("<b>" + companent.getTabName() + "</b>&nbsp;");
        tabName.setWordWrap(false);
        horz.add(tabName);
        horz.add(widget);
        tabPanelAdd(tabWidgets[step], horz);
        step++;
    }

    /* add custom tab with refresh button*/

    public void addWidget(CustomTabWidget companent, boolean isRefresh) {
        tabWidgets[step] = companent;
        tabWidgets[step].setTabBar(this);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        clickBut = new Image();
        clickBut.setStyleName("pointer");
        clickBut.setTitle(wfmStrings.refresh());
        clickBut.addClickHandler(event -> {
            onClickRef = true;
            tabBar.addSelectionHandler(integerSelectionEvent -> {
                if (onClickRef || tabWidgets[integerSelectionEvent.getSelectedItem()].isRefresh()) {
                    tabWidgets[integerSelectionEvent.getSelectedItem()].viewShow();
                    tabWidgets[integerSelectionEvent.getSelectedItem()].setRefresh(false);
                    if (refreshCommand != null) {
                        refreshCommand.execute();
                    }
                }
                onClickRef = false;
            });
        });
        HTML tabName = new HTML(("<b>") + companent.getTabName() + "</b>");//#084772
        tabName.setWordWrap(false);
        buttonPanel.add(tabName);
        buttonPanel.add(new HTML("&nbsp;"));
        buttonPanel.add(clickBut);
        buttonPanel.setCellVerticalAlignment(clickBut, HasVerticalAlignment.ALIGN_MIDDLE);
        tabPanelAdd(tabWidgets[step], buttonPanel);
        step++;
    }

    /*  tab click listener */

    private void addTabBarListener() {
        tabBar.addSelectionHandler(integerSelectionEvent -> {
            if (tabWidgets[integerSelectionEvent.getSelectedItem()].isRefresh()) {
                tabWidgets[integerSelectionEvent.getSelectedItem()].viewShow();
                tabWidgets[integerSelectionEvent.getSelectedItem()].setRefresh(false);
            }
            if (integerSelectionEvent.getSelectedItem() != null) {
                deck.showWidget(integerSelectionEvent.getSelectedItem());
            }
        });
    }

    public void setPanelSize(int width, int height) {
        if (isIE()) {
            this.deck.setSize(width + "px", height + "px");
        } else {
            this.deck.setSize((width - 4) + "px", (height - 4) + "px");
        }
    }

    public void setPanelHeight(String height) {
        this.deck.setHeight(height);
    }

    public void setPanelSize(String width, String height) {
        this.deck.setSize(width, height);
    }

	public void setRefreshCommand(Command refreshCommand) {
		this.refreshCommand = refreshCommand;
	}

	public void setMargin(String margin) {
        DOM.setStyleAttribute(this.tabPanel.getElement(), "margin", margin);
    }

    public void setPadding(String padding) {
        DOM.setStyleAttribute(this.tabPanel.getElement(), "padding", padding);
    }

    public CustomTabWidget getTabWidgets(int i) {
        return tabWidgets[i];
    }

    public void clearAllTabBars() {
        for (CustomTabWidget tabWidget : tabWidgets) {
            tabWidget.setRefresh(true);
        }
        tabBar.selectTab(0);
    }

    private static native boolean isIE() /*-{
        return (/MSIE (\d+\.\d+);/.test(navigator.userAgent));
    }-*/;

    public void insertLinks(Widget... widgets) {
        if (widgets != null && widgets.length > 0) {
            for (Widget widget : widgets) {
                if (widget != null) {
                    tabBarHP.add(widget);
                    tabBarHP.setCellVerticalAlignment(widget, HasVerticalAlignment.ALIGN_TOP);
                }
            }
        }
    }

    public void selectTab(int i) {
        if (i > -1 && i <= step) {
            tabBar.selectTab(i);
        }
    }

    public int getSelectedTab() {
        return tabBar.getSelectedTab();
    }

    public DecoratedTabBar getTabBar() {
        return tabBar;
    }

    public VerticalPanel getTabPanel() {
        return tabPanel;
    }
}
