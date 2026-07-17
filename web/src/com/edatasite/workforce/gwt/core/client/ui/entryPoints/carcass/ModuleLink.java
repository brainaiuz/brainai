package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;


public class ModuleLink extends SelectableListItem {
    private MaterialLink link;

    public ModuleLink(String color, SvgEnum iconType, String title) {
        super("main-modules__item", color);
        link = new MaterialLink();
        SvgIcon icon = new SvgIcon(iconType);
        link.add(icon);
        setTitle(title);
        add(link);
    }

    public void createDropDown(Widget... widgets) {
        MaterialDropDown dropDown = new MaterialDropDown(link);
        for (Widget widget : widgets) {
            dropDown.add(widget);
        }
        add(dropDown);
    }

    public void setLinkHref(String href) {
        link.setHref(href);
    }

    public void setTitle(String title) {
        link.setText(title);
    }

    public void setActive(boolean active) {
        if (active) {
            addStyleName("main-modules__item--active");
        }
    }
}
