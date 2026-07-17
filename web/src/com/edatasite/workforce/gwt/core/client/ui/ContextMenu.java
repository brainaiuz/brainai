package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Created by IntelliJ IDEA.
 * User: hayot
 * Date: Nov 27, 2010
 * Time: 5:37:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContextMenu extends PopupPanel {

    private MenuBar menuBar;

    private int size = 0;

    /**
     * The widget's constructor.
     */
    public ContextMenu() {
        // The popup's constructor's argument is a boolean specifying that it
        // auto-close itself when the user clicks outside of it.
        super(true);
        setAnimationEnabled(true);
        menuBar = new MenuBar(true);
        add(menuBar);
    }

    public void addMenuItem(String text, boolean asHtml, Command command) {
        addMenuItem(text, null, asHtml, command);
    }

    public void addMenuItem(String text, String imageAsHtml, boolean asHtml, Command command) {
        menuBar.addItem("<span>" + (imageAsHtml != null && !"".equals(imageAsHtml) ? imageAsHtml : "") + "&nbsp;" + WordUtils.capitalizeFirst(text) + "</span>", asHtml, command);
        menuBar.ensureDebugId(text);
        size++;
    }

    public void addMenuItemWithMenuBar(String text, String imageAsHtml, boolean asHtml, MenuBar bar) {
        menuBar.addItem("<span>" + (imageAsHtml != null && !"".equals(imageAsHtml) ? imageAsHtml : "") + "&nbsp;" + WordUtils.capitalizeFirst(text) + "</span>", asHtml, bar);
        menuBar.ensureDebugId(text);
        size++;

        //T3398 Actions menu was not closing after i select one of the options of submenu
        bar.addHandler(clickEvent -> {
            if (menuBar.getLayoutData() != null) {
                ((ActionButton) (menuBar.getLayoutData())).hide();
            }
        }, ClickEvent.getType());
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
