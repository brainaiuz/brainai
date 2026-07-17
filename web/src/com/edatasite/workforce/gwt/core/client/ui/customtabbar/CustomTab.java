package com.edatasite.workforce.gwt.core.client.ui.customtabbar;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 7, 2010
 * Time: 6:51:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomTab extends CustomTabWidget {
    Widget widget;
    boolean scrollable = false;

    public CustomTab(Widget widget, String tabName, boolean... scrollable) {
        super(tabName);
        this.widget = widget;
        this.scrollable = scrollable != null && scrollable.length > 0 && scrollable[0];
    }

    @Override
    public void initData() {

    }

    @Override
    public void viewShow() {
        if (scrollable) {
            ScrollPanel scrollPanel = new ScrollPanel(widget);
            scrollPanel.setSize("445px", "180px");
            add(scrollPanel);
        } else {
            add(widget);
        }
    }
}
