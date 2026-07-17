package com.edatasite.workforce.gwt.core.client.ui.menu;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 09.02.12
 * Time: 12:47
 */
public class ToolItem extends DecoratedPopupPanel implements HasClickHandlers {


    private Anchor action;
    // 22 itemPX per menu item
    private int itemPX = 6 * 23;

    /**
     * Creates an empty decorated popup panel. A child widget must be added to it
     * before it is shown.
     *
     * @param itemCount
     */
    private int top = 0;

    public ToolItem(int itemCount) {
        super();
        // 22 itemPX per menu item
        this.itemPX = itemCount * 23;
        setAutoHideEnabled(true);
        setStyleName("");
        action = new Anchor("", true);
        action.setStyleName("action-listing ficon--more-horiz");

        setStyleName("action-listing-popup");
//        getElement().getStyle().setBorderColor(/*"#718BB7"*/"#999");
//        getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
//        getElement().getStyle().setBorderWidth(1d, Style.Unit.PX);

        addClickHandler(event -> hide());

        action.addClickHandler(event -> {
            final Widget source = (Widget) event.getSource();
            int left = source.getAbsoluteLeft() + 10;

            if ((Window.getClientHeight() - source.getAbsoluteTop()) <= (itemPX + 80)) {
                int spareItemList = (itemPX + 80) - (Window.getClientHeight() - source.getAbsoluteTop());  // pastga tushub ketayapgan menyuni qismi -> spareItemList
                top = source.getAbsoluteTop() - spareItemList;  // spareItemList shu qismni ayrib tashlaymiz
            } else {
                top = source.getAbsoluteTop() + 10;
            }
            if (Utils.isArabicLanguage()) {
                setPopupPositionAndShow((offsetWidth, offsetHeight) -> setPopupPosition(source.getAbsoluteLeft() + source.getOffsetWidth() - offsetWidth - 5, top));
            } else {
                setPopupPosition(left, top);
                // Show the popup
                show();
            }
        });
    }

    public Anchor getAction() {
        //action.setHTML("<span class='keySplit'>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
        return action;
    }

    public void setButtonText(String html) {
        action.setHTML(html);
    }

    public void setButtonStyle(String style) {
        action.setStyleName(style);
    }

    public Anchor getCustomButton() {
        return action;
    }

    public void setWidget(MenuBar menuBar) {
        super.setWidget(menuBar);
    }

    public static String getToolText(String text, String iconStyle) {
        return "<table cellspacing='0' cellpadding='0' class='my-toolitem my-no-selection my-toolitem-over' id='my-24'><tbody><tr>" +
                "<td class='my-toolitem-l'><div>&nbsp;</div></td>" +
                "<td class='my-toolitem-ml'><div class='my-icon-btn " + iconStyle + "' id='my-23'></div></td>" +
                "<td class='my-toolitem-c'><span class='my-toolitem-text'>" + text + "</span></td>" +
                "<td class='my-toolitem-mr'><div class='my-toolitem-split'></div></td>" +
                "<td class='my-toolitem-r'><div>&nbsp;</div></td>" +
                "</tr></tbody></table>";
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
}
