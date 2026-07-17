package com.edatasite.workforce.gwt.core.client.ui.menu;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 08.02.12
 * Time: 20:21
 */
public class MenuPopItem extends MenuItem {
    private String text;
    private String iconStyle = "";

    public MenuPopItem(final String html, final String iconStyle) {
        super(getString(html, iconStyle));
        this.iconStyle = iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public static SafeHtml getString(final String html, final String iconStyle) {
        return (SafeHtml) () -> {
            if (iconStyle != null && !"".equals(iconStyle)) {
                return "<span class='list-action-menu-icon " + iconStyle + "'>" + html + "</span>";
            }
            return "<span>" + html + "</span>";
        };
    }

    public MenuPopItem(final String html) {
        this(html, "");
    }

    public MenuPopItem(final String html, final String iconStyle, MenuBar subMenu) {
        super(getString(html, iconStyle), subMenu);
    }

    public MenuPopItem(final String html, final String iconStyle, Command command) {
        super(getString(html, iconStyle), command);
    }

    public MenuPopItem(final String html, final AbstractImagePrototype icon) {
        super((SafeHtml) () -> "<span>" + icon.getHTML() + "&nbsp;&nbsp;" + html + "</span>");
    }

    @Override
    public void setText(String text) {
        super.setHTML(getString(text, iconStyle));    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setSelection(boolean isSelected) {
        if (isSelected) {
            this.addStyleName("icon-group-sel");
        } else {
            this.removeStyleName("icon-group-sel");
        }
    }

    public void closeAll(MenuBar parentMenuBar) {
        /*
        * Getting a parent menu bar
        * */
        if (parentMenuBar == null) {
            return;
        }
        /*
        * Menubar close all children
        * */
        parentMenuBar.closeAllChildren(true);
        /*
        * Remove current parent menu bar
        * */
        if (parentMenuBar.getParent() != null && parentMenuBar.getParent().getParent() instanceof ToolItem) {
            ToolItem popupPanel = (ToolItem) parentMenuBar.getParent().getParent();
            popupPanel.hide();
        }
    }
}
