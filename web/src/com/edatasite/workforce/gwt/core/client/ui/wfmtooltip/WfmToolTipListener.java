package com.edatasite.workforce.gwt.core.client.ui.wfmtooltip;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 2/18/13
 * Time: 6:04 PM
 */
public class WfmToolTipListener implements MouseOutHandler, MouseOverHandler, MouseDownHandler, MouseUpHandler,FocusHandler {

    private static final String DEFAULT_TOOLTIP_STYLE = "easyTooltip";
    private static final int DEFAULT_OFFSET_X = 10;
    private static final int DEFAULT_OFFSET_Y = 15;

    private TooltipPopup tooltipPopup;
    private String helpText;
    private String styleName;
    private int delay;
    private int offsetX = DEFAULT_OFFSET_X;
    private int offsetY = DEFAULT_OFFSET_Y;

    public WfmToolTipListener(String helpText, int delay, String... styleName) {
        this.helpText = helpText;
        this.delay = delay;
        this.styleName = (styleName != null && styleName.length > 0 && styleName[0] != null) ? styleName[0] : DEFAULT_TOOLTIP_STYLE;
    }

    public WfmToolTipListener(String helpText, int delay, int offsetX, int offsetY, String... styleName) {
        this(helpText, delay, styleName);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        if (tooltipPopup != null) {
            tooltipPopup.hide();
        }
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        if (tooltipPopup != null) {
            tooltipPopup.hide();
        }
        Widget widget = (Widget) event.getSource();
        tooltipPopup = new TooltipPopup(widget, offsetX, offsetY, helpText, delay, styleName);
        int screenWidth;
        int popupLeft = widget.getAbsoluteLeft();
        if (Utils.isIE()) {
            screenWidth = Utils.getIEScreenWidth();
        } else {
            screenWidth = Utils.getScreenWidth();
        }
        tooltipPopup.show();
        if (screenWidth < popupLeft + 150) {
            tooltipPopup.getElement().getStyle().setLeft((double) (popupLeft - 150), Style.Unit.PX);
        }
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        if (tooltipPopup != null) {
            tooltipPopup.hide();
        }
        Widget widget = (Widget) event.getSource();
        tooltipPopup = new TooltipPopup(widget, offsetX, offsetY, helpText, delay, styleName);
        int screenWidth;
        int popupLeft = widget.getAbsoluteLeft();
        if (Utils.isIE()) {
            screenWidth = Utils.getIEScreenWidth();
        } else {
            screenWidth = Utils.getScreenWidth();
        }
        tooltipPopup.show();
        if (screenWidth < popupLeft + 150) {
            tooltipPopup.getElement().getStyle().setLeft((double) (popupLeft - 150), Style.Unit.PX);
        }
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        if (tooltipPopup != null) {
            tooltipPopup.hide();
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (tooltipPopup != null) {
            tooltipPopup.hide();
        }
        Widget widget = (Widget) event.getSource();
        tooltipPopup = new TooltipPopup(widget, offsetX, offsetY, helpText, delay, styleName);
        int screenWidth;
        int popupLeft = widget.getAbsoluteLeft();
        if (Utils.isIE()) {
            screenWidth = Utils.getIEScreenWidth();
        } else {
            screenWidth = Utils.getScreenWidth();
        }
        tooltipPopup.show();
        if (screenWidth < popupLeft + 150) {
            tooltipPopup.getElement().getStyle().setLeft((double) (popupLeft - 150), Style.Unit.PX);
        }
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    private static class TooltipPopup extends PopupPanel {
        private int delay;

        private TooltipPopup(Widget widget, int offsetX, int offsetY, final String helpText, final int delay, final String styleName) {
            super(true);
            this.delay = delay;
            add(new HTML(helpText));
            int left = widget.getAbsoluteLeft() + offsetX;
            int top = widget.getAbsoluteTop() + offsetY;

            setPopupPosition(left, top);
            getElement().setId(styleName);
            getElement().setClassName(styleName);
        }

        @Override
        public void show() {
            super.show();
            Timer timer = new Timer() {
                @Override
                public void run() {
                    TooltipPopup.this.hide();
                }
            };
            timer.schedule(delay);
        }
    }
}