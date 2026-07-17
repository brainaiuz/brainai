package com.edatasite.workforce.gwt.core.client.ui.treetable;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 31-Jul-2010
 * Time: 14:20:47
 */
public class WfmToolBar extends HTMLPanel {

    private static final String wfmToolBal = "wfm-tool-bar";
//    private static final String style = "style='margin:auto;white-space:nowrap;height:35px;vertical-align:middle'";
    private static int num = 0;

    private int cell = 0;
    private String id;
    private FlowPanel contentPanel = new FlowPanel();

    public WfmToolBar() {
        super("<div id='" + (wfmToolBal + num) + "' ></div>");
        id = wfmToolBal + num++;
        contentPanel.setStyleName("operPanel");

        add(contentPanel, id);
    }

    public void addStyleName(String style) {
        contentPanel.addStyleName(style);
    }

    /**
     * Add Widget
     *
     * @param widget
     */
    private void addWidget(Widget widget, HasHorizontalAlignment.HorizontalAlignmentConstant alignment, String width) {
        contentPanel.add(widget);
    }

    /**
     * Add HTML text
     *
     * @param html
     */
    private void addHTML(String html, HasHorizontalAlignment.HorizontalAlignmentConstant alignment, String width) {
        contentPanel.add(new HTMLPanel(html));
    }

    @Override
    public void add(Widget widget) {
        addWidget(widget, null, null);
    }

    public void add(String html) {
        addHTML(html, null, null);
    }


    public void add(Widget widget, HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
        addWidget(widget, alignment, null);
    }

    public void add(String html, HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
        addHTML(html, alignment, null);
    }

    public void add(Widget widget, HasHorizontalAlignment.HorizontalAlignmentConstant alignment, String width) {
        addWidget(widget, alignment, width);
    }

    public void add(String html, HasHorizontalAlignment.HorizontalAlignmentConstant alignment, String width) {
        addHTML(html, alignment, width);
    }
}
