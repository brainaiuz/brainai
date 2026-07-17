package com.edatasite.workforce.gwt.core.client.ui.shortcut;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;

public class EdsShortcuts extends ScrollPanel {
    protected FlowPanel profilePanel = new FlowPanel();
    protected FlowPanel container = new FlowPanel();
    protected FlowPanel helpPanel = new FlowPanel();

    private ShortcutItem activated;
    private VerticalPanel panel = new VerticalPanel();
    //    private ArrayList<ShortcutItem> shortcutItems;
    private HTMLPanel menu = new HTMLPanel("ul", "");

    public EdsShortcuts(HashMap<String, ShortcutItem> itemsByView) {
        setStyleName("divisioncontainer");
        if (itemsByView.isEmpty()) {
            return;
        }
        panel.addStyleName("subDivisionsBar");
        menu.addStyleName("subDivisions");
        for (ShortcutItem item : itemsByView.values()) {
            //item.setParent(this);
            menu.add(item.getItem());
        }
        menu.getElement().getStyle().setPadding(0d, Style.Unit.PX);
        panel.add(profilePanel);
        panel.add(menu);
        panel.setWidth("100%");
        panel.add(container);
        panel.add(helpPanel);
        add(panel);
        panel.getElement().getParentElement().getStyle().clearPosition();
        panel.getElement().getParentElement().getStyle().clearZIndex();
        panel.getElement().getParentElement().getStyle().setWidth(100, Style.Unit.PCT);

    }

/*
    public EdsShortcuts(ArrayList<ShortcutItem> shortcutItems) {
        this.shortcutItems = shortcutItems;

        if (shortcutItems.isEmpty()) {
            return;
        }

        for (ShortcutItem item : shortcutItems) {
            item.setParent(this);
            panel.add(item.getRootElement());
        }

        panel.setWidth("100%");
        panel.add(container);
        panel.add(helpPanel);
        add(panel);
        panel.getElement().getParentElement().getStyle().clearPosition();
        panel.getElement().getParentElement().getStyle().clearZIndex();
        panel.getElement().getParentElement().getStyle().setWidth(100, Style.Unit.PCT);
    }
*/

    public void activate(ShortcutItem item) {
        if (activated != null) {
            activated.setDefaultStyle();
        }
        activated = item;
    }

    public void addWidget(Widget widget) {
        HorizontalPanel ticketPanel = new HorizontalPanel();
        ticketPanel.setSpacing(3);
        ticketPanel.add(widget);
        panel.add(ticketPanel);
    }

    public void addContainerWidget(Widget widget) {
        panel.add(widget);
    }

    public ShortcutItem getActive() {
        return activated;
    }

    public ShortcutItem getActivated() {
        return activated;
    }

    public void setHelpContainer(FlowPanel flowPanel) {
        helpPanel.clear();
        if (helpPanel != null && flowPanel != null) {
            flowPanel.setSize("100%", "100%");
            this.helpPanel.clear();
            this.helpPanel.add(flowPanel);
        }
    }

    public void setProfileContainer(FlowPanel flowPanel) {
        this.profilePanel.clear();
        if (profilePanel != null && flowPanel != null) {
            profilePanel.addStyleName("userShortProfile2");
            this.profilePanel.add(flowPanel);
        }
    }
/*
    public void removeFromVp(Widget widget) {
        panel.remove(widget);
    }
*/

    public void clear() {
        container.clear();
    }

/*
    public void removeShortcut(Widget widget) {

        container.remove(widget);
    }

    public void setVisibilityHelpContainer(boolean visibility) {
        if (container != null) {
            this.container.setVisible(visibility);
        }
    }
*/

    public void setVisibilityItems(boolean visibility) {
        if (panel.getWidgetCount() != 0) {
            for (int i = 0; i < panel.getWidgetCount(); i++) {
                panel.getWidget(i).setVisible(visibility);
            }
        }
    }
}
