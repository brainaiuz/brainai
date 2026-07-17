package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.Edge;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSideNavContent;
import gwt.material.design.client.ui.MaterialSideNavDrawer;
import gwt.material.design.client.ui.html.Div;

public class KpiSideNavBox extends MaterialSideNavDrawer {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();


    public static final int DEFAULT_WIDTH = 390;
    public static final int WIDE_FORM_WIDTH = 720;

    private MaterialSideNavContent content;
    private MaterialPanel contentHeader;
    private MaterialPanel headerContainer;
    private MaterialPanel contentBody;
    private MaterialPanel contentFooter; // ToDo Normurad "side-nav__footer__group" should be added to child div (buttons wrapper)

    private MaterialPanel sideNavOverlay;

    private Div bodyContainer;

    private Div closeContent;

    private boolean autoHide = false;
    private int width;

    protected Command command;

    private Command closeCommand;

    public KpiSideNavBox() {
        this(false);
    }

    public KpiSideNavBox(boolean autoHide) {
        this(autoHide, 0);
    }

    public KpiSideNavBox(int width) {
        this(false, width);
    }

    public KpiSideNavBox(boolean autoHide, int width) {
        super();
        this.autoHide = autoHide;
        this.width = width;
        addStyleName("quick-add");

        initializeOptions();
    }

    private void initializeOptions() {
        setEdge(Edge.RIGHT);
        MaterialLink activator = new MaterialLink();
        activator.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

        String activationKey = UUID.uuid();
        activator.setActivates(activationKey);
        setId(activationKey);

        closeContent = new Div("side-nav-close");
        SvgIcon closeIcon = new SvgIcon(SvgEnum.x);
        closeContent.addClickHandler(clickEvent -> {
            if (closeCommand != null) {
                closeCommand.execute();
            }
            hide();
        });
        closeContent.addHandler(event -> hide(), ClickEvent.getType());
        closeContent.add(closeIcon);
        closeContent.setVisible(false);
        add(closeContent);

        content = new MaterialSideNavContent();
        add(content);
        content.add(activator);
        content.getElement().getStyle().setDisplay(Style.Display.FLEX);

        //************SideNav header************//
        headerContainer = new MaterialPanel();
        headerContainer.setClass("side-nav__heading");
        content.add(headerContainer);

        contentHeader = new MaterialPanel();
        contentHeader.setClass("side-nav__title");
        headerContainer.add(contentHeader);


        //************SideNav body************//
        bodyContainer = new Div("side-nav__body");
        contentBody = new MaterialPanel();
        contentBody.setClass("scroll-offset");
        bodyContainer.add(contentBody);
        content.add(bodyContainer);


        //************SideNav footer************//
        contentFooter = new MaterialPanel();
        contentFooter.setClass("side-nav__footer");
        content.add(contentFooter);

        //Side nav default width

        setWidth(width > 0 ? width : DEFAULT_WIDTH);

        sideNavOverlay = new MaterialPanel();
        sideNavOverlay.setClass("kpi-sidenav-overlay");
        sideNavOverlay.setDisplay(Display.NONE);
        sideNavOverlay.addClickHandler(ch -> {

            if (autoHide) {
                hide();
            }
        });

        //T3413 - Escape must close sidenavbox, i've made it global for all sidenavboxes
        FocusPanel focusPanel = new FocusPanel(this);
        focusPanel.setHeight("100%");
        focusPanel.addKeyDownHandler(keyDownEvent -> {
            if (keyDownEvent.getNativeKeyCode() == KeyboardListener.KEY_ESCAPE) {
                hide();
            }
        });
        addOpenedHandler(event -> focusPanel.setFocus(true));
        MainLayout.get().getModalContainer().add(sideNavOverlay);
        MainLayout.get().getModalContainer().add(focusPanel);

        try {
            Utils.hideDropDownLookUp();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void open() {
        sideNavOverlay.setDisplay(Display.FLEX);
        closeContent.setVisible(true);
        MainLayout.get().getModalContainer().getElement().addClassName("kpi-overlay--active");
        super.open();
    }

    /**
     * will hide but still has in DOM
     */
    @Override
    public void close() {
        sideNavOverlay.setDisplay(Display.NONE);
        closeContent.setVisible(false);
        MainLayout.get().getModalContainer().getElement().removeClassName("kpi-overlay--active");
        super.close();
    }

    /**
     * will remove from DOM
     */
    public void remove() {
        super.close();
        this.removeFromParent();
        sideNavOverlay.removeFromParent();
    }

    public void addHeader(Widget widget) {
        contentHeader.add(widget);
    }

    public void addHeaderContainer(Widget widget) {
        headerContainer.add(widget);
    }

    public MaterialPanel getContentHeader() {
        return contentHeader;
    }

    public void addBody(Widget widget) {
        contentBody.add(widget);

        initBodyScrollContent();
    }

    public MaterialPanel getBody() {
        return contentBody;
    }

    protected void initBodyScrollContent() {

        if (contentBody.getOffsetHeight() > bodyContainer.getElement().getClientHeight() && !bodyContainer.getStyleName().contains("has-scroll--vertical")) {
            bodyContainer.addStyleName("has-scroll--vertical");
        } else {
            bodyContainer.removeStyleName("has-scroll--vertical");
        }
    }

    public void addFooter(Widget widget) {
        contentFooter.add(widget);
    }

    public MaterialPanel getContentFooter() {
        return contentFooter;
    }

    public void overlayRemoveFromParent() {
        sideNavOverlay.removeFromParent();
    }

    public void clear() {
        contentHeader.clear();
        contentBody.clear();
        contentFooter.clear();
        headerContainer.clear();
    }

    public void clearBody() {
        contentBody.clear();
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getCloseCommand() {
        return closeCommand;
    }

    public void setCloseCommand(Command closeCommand) {
        this.closeCommand = closeCommand;
    }

    public void enableScrollUpDownEventAction() {
        addStyleOnScroll(headerContainer.getElement(), bodyContainer.getElement());
    }

    public Div getCloseContent() {
        return closeContent;
    }

    public void disableScrollUpDownEventAction() {
        unbindScrollEvent(headerContainer.getElement());
    }

    private native void addStyleOnScroll(Element header, Element body)/*-{
        if (!$wnd.$(header).hasClass("scrolled--down") && !$wnd.$(header).hasClass("scrolled--up")) {
            $wnd.$(header).addClass("scrolled--up");
        }
        var lastScrollTop = $wnd.$(body).scrollTop();
        $wnd.$(body).scroll(function (event) {
            var st = $wnd.$(body).scrollTop();
            if (st > lastScrollTop) {
                $wnd.$(header).removeClass("scrolled--up").addClass("scrolled--down");
            } else if (st == lastScrollTop) {
                $wnd.$(header).removeClass("scrolled--down").addClass("scrolled--up");

            }
        });
    }-*/;

    private native void unbindScrollEvent(Element body)/*-{
        $wnd.$(body).unbind("scroll");
    }-*/;

    // To have opportunity to remove header and footer
    public void removeHeader() {
        if (headerContainer != null) {
            headerContainer.removeFromParent();
        }
    }

    public void removeFooter() {
        if (contentFooter != null) {
            contentFooter.removeFromParent();
        }
    }
}


