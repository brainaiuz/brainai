package com.edatasite.workforce.gwt.core.client.ui.menu;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 13.02.12
 * Time: 12:25
 */

public class ActionButton extends Widget implements HasClickHandlers/*, HasMouseOutHandlers, HasMouseOverHandlers*/ {

    private String text;
    private String styleName;
    private Type type;
    private HTMLPanel item;
    private DecoratedPopupPanel popupPanel;
    private int topOffSet = 30;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    //count menuitems * 23 in menuBar
    private int itemCountPx = 23;
    private Widget widget;

    public ActionButton(String text, String styleName, Type type) {
        this.text = text;
        this.styleName = styleName;
        this.type = type;
        init();
    }

    @UiConstructor
    public ActionButton() {
        this("&nbsp;", "btnSearch", Type.BUTTON);
    }

    public ActionButton(String text) {
        this(text, "", Type.BUTTON);
    }

    public ActionButton(String text, Type type) {
        this(text, "", type);
    }

    public ActionButton(String text, String styleName) {
        this(text, styleName, Type.BUTTON);
    }

    private void init() {
        item = Type.TOOLMENU.equals(type) ? getToolMenu() : getButtonText();
        if (styleName != null && !styleName.isEmpty()) {
            item.addStyleName(styleName);
        }

        setElement(item.getElement());

    }

    public Type getType() {
        return type;
    }

    private HTMLPanel getButtonText() {
        item = new HTMLPanel("span", text);
        return item;
    }

    private HTMLPanel getToolMenu() {
        item = new HTMLPanel("<span>" + text + "</span>");
        //item.addStyleName("dropBtn");
        popupPanel = new DecoratedPopupPanel(true);
        popupPanel.setAutoHideEnabled(true);
        popupPanel.removeStyleName("gwt-DecoratedPopupPanel");
        popupPanel.setStyleName("dropBtn-popup");
        popupPanel.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        popupPanel.addDomHandler(event -> hide(), ClickEvent.getType());

        addClickHandler(event -> showPopup(event));

        return item;
    }

    private int top;

    private void showPopup(ClickEvent event) {
        final Widget source = (Widget) event.getSource();
        int left = source.getAbsoluteLeft();

        if ((Window.getClientHeight() - source.getAbsoluteTop()) <= (itemCountPx + 60)) {
            top = source.getAbsoluteTop() - itemCountPx;
            left += 10;
        } else {
            top = source.getAbsoluteTop() + topOffSet;
        }
        setPopupPosition(left, top);
        show();

        // Show the popup
        if (Utils.isArabicLanguage()) {
            popupPanel.setPopupPositionAndShow((offsetWidth, offsetHeight) -> setPopupPosition(source.getAbsoluteLeft() + source.getOffsetWidth() - offsetWidth, top));
        }
    }

    public void setToolTip(String title) {
//        listener = new ToolTipListener(text, 5000 /* timeout in milliseconds*/, "toolTip");
//        addMouseOverHandler(listener);
    }

    /**
     * Unchecked
     *
     * @param html
     */
    public void setText(String html) {
        item.getElement().setInnerHTML(html);
    }

    public void setItemCount(int itemCount) {
        this.itemCountPx = itemCount * 23;
    }

    public void setStyle(String styleName) {
        item.setStyleName(styleName);
    }

    public void show() {
        popupPanel.show();
    }

    public void hide() {
        popupPanel.hide();
    }

    public void setPopupPosition(int left, int right) {
        popupPanel.setPopupPosition(left, right);
    }

    public void add(Widget widget) {
        item.add(widget);
    }

    //if Type toolmenu then you should add popup MenuBar
    public void setMenu(Widget widget) {
        popupPanel.setWidget(widget);
        //popupPanel.addStyleName("gwt-PopupPanel");
    }

    /**
     * Adds a {@link com.google.gwt.event.dom.client.ClickEvent} handler.
     *
     * @param handler the click handler
     * @return {@link com.google.gwt.event.shared.HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public static String getNewString() {
        return wfmStrings.New();
    }

    public static String deleteString() {
        return wfmStrings.delete();
    }

    public static String getMoreString() {
        return wfmStrings.actions();
    }

    public void setDisable(boolean disable) {
        if (disable)
            item.addStyleName("disabled");
        else
            item.removeStyleName("disabled");
    }

    public void setTopOffSet(int topOffSet) {
        this.topOffSet = topOffSet;
    }

    public enum Type {
        TOOLMENU, BUTTON, LINK
    }
}
