package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.ui.LinkInformation;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Span;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Anvar Akramov Date: Jan 25, 2008 Time:
 * 4:55:14 PM To change this template use File | Settings | File Templates.
 */

public abstract class BaseListView extends View implements FittedContent {

    private ListingPanel listingPanel;
    private QuickViewPanel quickViewPanel;
    private final ActionButton maximizeMinimize = new ActionButton("&nbsp;", "right markUploadR");
    private final DockLayoutPanel dockPanel = new DockLayoutPanel(Style.Unit.PX);
    private final ScrollPanel sp = new ScrollPanel();
    protected Span statisticShortcut;

    private final FlowPanel header = new FlowPanel();
    private final HorizontalPanel hrp = new HorizontalPanel();

    private boolean maximized = false;

    public BaseListView() {
        add(dockPanel);
    }

    public BaseListView(String name, String description) {
        super(name, description);
        add(dockPanel);
    }

    public BaseListView(String name) {
        super(name);
        add(dockPanel);
    }

    public BaseListView(String name, String description, ListingPanel listingPanel, QuickViewPanel quickViewPanel) {
        super(name, description);
        this.listingPanel = listingPanel;
        this.quickViewPanel = quickViewPanel;
        add(dockPanel);
    }

    public ListingPanel getListingPanel() {
        return listingPanel;
    }

    public void setListingPanel(ListingPanel listingPanel) {
        this.listingPanel = listingPanel;
    }

    public void setQuickViewPanel(QuickViewPanel quickViewPanel) {
        this.quickViewPanel = quickViewPanel;
    }

    public void display() {
        setHeight("100%");
        dockPanel.setSize("100%", "100%");
        hrp.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        hrp.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
        hrp.setStyleName("whiteText");

        header.setStyleName("quickViewHeaderSome");
        maximizeMinimize.addClickHandler(event -> {
            if (maximized) {
                maximized = false;
//                maximizeMinimize.setStyle("my-tool-maximize");
                drawPanel();
            } else {
                maximized = true;
//                    maximizeMinimize.setStyle("my-tool-restore");
                drawOnlyQuickViewPanel();
            }
        });

        if (quickViewPanel != null) {
            sp.add(quickViewPanel);
            drawPanel();
        } else {
            listingPanel.setHeight("100%");
            dockPanel.add(listingPanel);
        }
    }

    private void drawPanel() {
        dockPanel.clear();
        quickViewPanel.setParentListPanel(listingPanel);
        quickViewPanel.setLinksSetter(links -> addModifiedLinks(links));

        quickViewPanel.setStyleName("whiteBGR");

        header.add(maximizeMinimize);
        header.add(hrp);
        header.setHeight("25px");

        dockPanel.addNorth(listingPanel, 300);
        dockPanel.addNorth(header, 25);

        dockPanel.add(sp);
    }

    private void drawOnlyQuickViewPanel() {
        dockPanel.clear();

        quickViewPanel.setLinksSetter(links -> addModifiedLinks(links));

        quickViewPanel.setStyleName("whiteBGR");

        header.add(maximizeMinimize);
        header.add(hrp);
        header.setHeight("25px");

        dockPanel.addNorth(header, 25);
        dockPanel.add(sp);
    }

    public void addLinks(List links) {
        hrp.clear();
        hrp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        hrp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        hrp.setSpacing(5);
        hrp.setWidth("60%");
        hrp.setStyleName("whiteText");

        if (links != null && !links.isEmpty()) {
            for (Object link1 : links) {
                Widget link = (Widget) link1;
                link.setStyleName("whiteText");
                hrp.add(link);
                hrp.setCellHorizontalAlignment(link, HorizontalPanel.ALIGN_LEFT);
                hrp.setCellVerticalAlignment(link, HorizontalPanel.ALIGN_MIDDLE);
            }

        }
    }

    //For ToolItem List

    public void addModifiedLinks(List links) {
        hrp.clear();
        hrp.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
        hrp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        if (links != null && !links.isEmpty()) {
            for (Object object : links) {
                LinkInformation link;
                if (object instanceof LinkInformation) {
                    link = (LinkInformation) object;
                } else {
                    link = new LinkInformation(null, (Widget) object);
                }
                hrp.add(link.getLink());
                hrp.setCellHorizontalAlignment(link.getLink(), link.getHorizontalAlignment());
                hrp.setCellVerticalAlignment(link.getLink(), HorizontalPanel.ALIGN_MIDDLE);
            }
        }
    }

    //For now it is final to prevent from unexpected design alteration

    protected static final void drawPopup(int row, FlexTable flexTable, String name, String value) {
        flexTable.setText(row, 0, name + ":");
        flexTable.setText(row, 1, value);
        flexTable.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
    }

    protected final SimpleLink getLink(String name, String action) {
        return new SimpleLink(name, action);
    }

    protected final SimpleLink getLink(String name, String action, String tabTitle) {
        return new SimpleLink(name, action, tabTitle);
    }

    protected SimpleLink getLink(String name, String action, String tabName, String tabTitle) {
        return new SimpleLink(name, action, tabTitle, tabName, "");
    }

    @Deprecated
    protected final SimpleLink getLinkWithTabTitle(String name, String action, String tabTitle) {
        return new SimpleLink(name, action, tabTitle);
    }

    protected ActionButton topToolBarItem(String text, final String historyToken, ActionButton.Type type) {
        ActionButton newLocation = new ActionButton(text, type);
        newLocation.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(historyToken));
        return newLocation;
    }

    protected MenuPopItem topToolBarItem(String text, final String historyToken) {
        MenuPopItem newLocation = new MenuPopItem(text);
        newLocation.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(historyToken));
        return newLocation;
    }

    protected ActionButton getRemoveMoreButton(ClickHandler clickHandler) {
        ActionButton actionButton = new ActionButton("<span class=\"btn btn--circle btn--remove\">\n" +
                "<svg class=\" icon--trash2\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#trash2\"></use></svg>\n" +
                "</span>", ActionButton.Type.LINK);
        actionButton.addClickHandler(clickHandler);
        actionButton.ensureDebugId(getName() + "remove_button_id");
        return actionButton;
    }

    protected ActionButton getPrintButton(ClickHandler clickHandler) {
        ActionButton actionButton = new ActionButton("<span title=\"print\" class=\"btn btn--circle  btn--success\">\n" +
                "<svg class=\" icon--trash2\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#print\"></use></svg>\n" +
                "</span>", ActionButton.Type.LINK);
        actionButton.addClickHandler(clickHandler);
        actionButton.ensureDebugId(getName() + "print_button_id");
        return actionButton;
    }
}