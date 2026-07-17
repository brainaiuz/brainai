package com.edatasite.workforce.gwt.core.client.ui.wfmtooltip;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * User: Ilhombek
 * Date: 2/18/13
 * Time: 6:03 PM
 */
@Deprecated
public class WfmToolTip extends Composite {

    /**
     * This is icon image type
     */
    public enum IMAGE_TYPE {
        HELP, WARNING
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmToolTipIconBundle toolTipIconBundle = GWT.create(WfmToolTipIconBundle.class);

    private Image helpImage;
    private HTMLPanel h;
    private IMAGE_TYPE image_type;

    private int offsetX = 10;
    private int offsetY = 15;

    private HandlerRegistration mouseOverHandlerRegistration;
    private HandlerRegistration mouseOutHandlerRegistration;

    private int delay = 5000;

    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * Generate default constructor
     */
    @Deprecated
    public WfmToolTip() {
        this(IMAGE_TYPE.HELP);
    }

    /**
     * Generate constructor for big texts
     */
    @Deprecated
    public WfmToolTip(int offsetX, int offsetY) {
        this(IMAGE_TYPE.HELP);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Generate constructor with icon image type
     *
     * @param image_type - icon image type
     */
    @Deprecated
    public WfmToolTip(IMAGE_TYPE image_type) {
        this.image_type = image_type;
        helpImage = new Image();
        h = new HTMLPanel("div", "");
        h.add(helpImage);
        initWidget(h);
    }

    /**
     * Get tooltip help icon image
     */
    public Image getHelpImage() {
        return helpImage;
    }

    public void setHelpText(String helpText, String... styleName) {
        clearMouseListeners();
        if (image_type == IMAGE_TYPE.HELP) {
            helpImage.setResource(toolTipIconBundle.helpIcon());
        } else if (image_type == IMAGE_TYPE.WARNING) {
            helpImage.setResource(toolTipIconBundle.warningIcon());
        }
        helpImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        h.setWidth(helpImage.getWidth() + "px");
        h.setHeight(helpImage.getHeight() + "px");
        String helpTextT = helpText == null || "".equals(helpText) ? wfmStrings.pleaseEnterValue() : helpText;
        WfmToolTipListener toolTipListener = new WfmToolTipListener(helpTextT, delay, offsetX, offsetY, /*"toolTip"*/styleName);
        mouseOverHandlerRegistration = helpImage.addMouseOverHandler(toolTipListener);
        mouseOutHandlerRegistration = helpImage.addMouseOutHandler(toolTipListener);
    }

    private void clearMouseListeners() {
        if (mouseOverHandlerRegistration != null) {
            mouseOverHandlerRegistration.removeHandler();
        }
        if (mouseOutHandlerRegistration != null) {
            mouseOutHandlerRegistration.removeHandler();
        }
    }
}