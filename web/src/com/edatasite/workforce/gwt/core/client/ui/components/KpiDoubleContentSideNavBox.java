package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.UUID;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.Edge;
import gwt.material.design.client.events.SideNavOpenedEvent;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialSideNavContent;
import gwt.material.design.client.ui.MaterialSideNavDrawer;
import gwt.material.design.client.ui.html.Div;

public class KpiDoubleContentSideNavBox extends MaterialSideNavDrawer {

    interface KpiDoubleContentSideNavBoxUiBinder extends UiBinder<MaterialSideNavContent, KpiDoubleContentSideNavBox> {
    }

    private static KpiDoubleContentSideNavBoxUiBinder ourUiBinder = GWT.create(KpiDoubleContentSideNavBoxUiBinder.class);

    private MaterialSideNavContent mainContent;

    public static final int DEFAULT_WIDTH = 390;

    private MaterialPanel sideNavOverlay;


    private boolean autoHide = false;
    private int width;

    @UiField
    Div secondCol;
    @UiField
    MaterialPanel secondBody;
    @UiField
    Div contentHeader;
    @UiField
    MaterialPanel bodyContainer;
    @UiField
    MaterialPanel contentBody;
    @UiField
    Div contentFooter;
    @UiField
    Div mainCol;
    @UiField
    Div secondBodyContainer;
    @UiField
    Div secondHeaderContainer;
    @UiField
    Div secondHeader;


    public KpiDoubleContentSideNavBox(boolean autoHide) {
        this(autoHide, 0);
    }

    public KpiDoubleContentSideNavBox(boolean autoHide, int width) {
        this(autoHide, width, false);
    }

    public KpiDoubleContentSideNavBox(boolean autoHide, int width, boolean doubleContent) {
        super();
        this.mainContent = ourUiBinder.createAndBindUi(this);

        this.autoHide = autoHide;
        this.width = width;
        addStyleName("quick-add");

        initializeOptions(doubleContent);
    }

    private void initializeOptions(boolean doubleContent) {
        mainCol.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        secondCol.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        if (doubleContent) {
            secondCol.addStyleName("col-6");
            mainCol.addStyleName("col-6");
        } else {
            mainCol.addStyleName("col-12");
            secondCol.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
        setEdge(Edge.RIGHT);
        MaterialLink activator = new MaterialLink();
        activator.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

        String activationKey = UUID.uuid();
        activator.setActivates(activationKey);
        setId(activationKey);

        mainContent.setInitialClasses("form-row");
        mainContent.getElement().getStyle().setHeight(100, Style.Unit.PCT);
        mainContent.add(activator);
        add(mainContent);
        mainContent.getElement().getStyle().setDisplay(Style.Display.FLEX);

        bodyContainer.setInitialClasses("side-nav__body");
        contentBody.addStyleName("scroll-offset");
        bodyContainer.add(contentBody);

        contentFooter.setClass("side-nav__footer");

        setWidth(width > 0 ? width : DEFAULT_WIDTH);

        sideNavOverlay = new MaterialPanel();
        sideNavOverlay.setClass("kpi-sidenav-overlay");
        sideNavOverlay.setDisplay(Display.NONE);
        sideNavOverlay.addClickHandler(ch -> {

            if (autoHide) {
                sideNavOverlay.setDisplay(Display.NONE);
                hide();
            }
        });

        //T3413 - Escape must close sidenavbox, i've made it global for all sidenavboxes
        FocusPanel focusPanel = new FocusPanel(this);
        focusPanel.setHeight("100%");
        focusPanel.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent keyPressEvent) {
                if (keyPressEvent.getNativeKeyCode() == KeyboardListener.KEY_ESCAPE) {
                    hide();
                }
            }
        });
        addOpenedHandler(new SideNavOpenedEvent.SideNavOpenedHandler() {
            @Override
            public void onSideNavOpened(SideNavOpenedEvent event) {
                focusPanel.setFocus(true);
            }
        });
        MainLayout.get().getModalContainer().add(sideNavOverlay);
        MainLayout.get().getModalContainer().add(this);
        MainLayout.get().getModalContainer().add(focusPanel);
    }

    @Override
    public void open() {
        super.open();

        sideNavOverlay.setDisplay(Display.FLEX);
    }

    @Override
    public void hide() {
        super.hide();

        sideNavOverlay.setDisplay(Display.NONE);
    }

    public void close() {
        super.close();
        this.removeFromParent();
        sideNavOverlay.removeFromParent();
    }

    public void addHeader(Widget widget) {
        contentHeader.add(widget);
    }

    public void addToSecondHeader(Widget widget) {
        secondHeader.add(widget);
    }

    public void addBody(Widget widget) {
        contentBody.add(widget);

        initBodyScrollContent();
    }

    public void addSecondBody(Widget widget) {
        secondBody.add(widget);
        initSecondBodyScrollContent();
    }

    private void initSecondBodyScrollContent() {
        if (secondBody.getOffsetHeight() > secondBodyContainer.getElement().getClientHeight() && !secondBodyContainer.getStyleName().contains("has-scroll--vertical")) {
            secondBodyContainer.removeStyleName("has-scroll--vertical");
        } else {
            secondBodyContainer.addStyleName("has-scroll--vertical");
        }
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

    public void clear() {
        contentHeader.clear();
        contentBody.clear();
        contentFooter.clear();
    }

}
