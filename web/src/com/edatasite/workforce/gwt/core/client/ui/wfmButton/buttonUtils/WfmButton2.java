package com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.html.Icon;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 28.01.2009
 * Time: 16:11:52
 * To change this template use File | Settings | File Templates.
 */


public class WfmButton2 extends MaterialWidget {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    public static final String BTN_DEFAULT = Constants.BTN_DEFAULT;
    public static final String BTN_PRIMARY = Constants.BTN_PRIMARY;
    public static final String BTN_SUCCESS = Constants.BTN_SUCCESS;
    public static final String BTN_REJECT = Constants.BTN_REJECT;
    public static final String BTN_SECONDARY = Constants.BTN_DEFAULT; ///*"btn btn--secondary"*/; //ToDo as Munir told, we don't need use it any more
    public static final String BTN_RESET = Constants.BTN_DEFAULT;
    public static final String BTN_ACTIVE = "btn-flat btn--active";
    public static final String BTN_WHITE = "btn btn--white";
    public static final String BTN_WHITE_OUTLINE = "btn btn--white btn--outline";
    public static final String BTN_GREY = "btn btn--darkgrey";
    public static final String BTN_LIGHTGREY = "btn btn--lightgrey";

    public static final String ICON_CHECK = "ficon--check";
    public static final String ICON_CLOSE = "ficon--close";
    public static final String ICON_CANCEL = "ficon--cancel";
    public static final String ICON_SAVE = "ficon--save";
    public static final String ICON_TRASH = "ficon--trash";
    public static final String ICON_PLUS = "ficon--plus";
    public static final String ICON_MAIL = "ficon--mail-outline";

    /**
     * WfmButton2(html, styleName, iconLeftClass, iconRightClass);
     * <p>
     * <i class="iconLeftClass"></i><span>html</span><i class="iconRightClass"></i>
     */
    public WfmButton2() {
        this("");
    }

    public WfmButton2(String html) {
        this(html, BTN_DEFAULT);
    }

    public WfmButton2(String html, ClickHandler handler) {
        this(html, null, null, null, null, handler);
    }

    public WfmButton2(String html, SvgIcon svgIcon) {
        this(html, null, null, null, svgIcon, null);
    }

    public WfmButton2(String html, String styleName) {
        this(html, styleName, null, null, null, null);
    }

    public WfmButton2(String html, String stylyName, String iLeft) {
        this(html, stylyName, iLeft, null, null, null);
    }

    public WfmButton2(String html, String styleName, ClickHandler handler) {
        this(html, styleName, null, null, null, handler);
    }

    public WfmButton2(String html, String styleName, String iLeft, String iRight) {
        this(html, styleName, iLeft, iRight, null, null);
    }

    public WfmButton2(String html, String styleName, String iLeft, ClickHandler handler) {
        this(html, styleName, iLeft, null, null, handler);
    }


    public WfmButton2(String html, String styleName, String iLeft, String iRight, SvgIcon svgIcon, ClickHandler handler) {
        super(Document.get().createElement("button"));
        if (!Utils.isNullOrEmpty(iLeft)) {
            add(createIcon(iLeft));
        }
        if (svgIcon != null) {
            add(svgIcon);
        }
        if (html != null && !html.isEmpty()) {
            HTMLPanel textSpan = new HTMLPanel("span", html);
            add(textSpan);
        }
        if (!Utils.isNullOrEmpty(iRight)) {
            add(createIcon(iRight));
        }
        if (styleName != null) {
            setStyleName(styleName);
        } else {
            setStyleName(BTN_DEFAULT);
        }

        addMouseOverHandler(mouseOverEvent -> addStyleName("hover"));
        addMouseOutHandler(mouseOutEvent -> removeStyleName("hover"));
        if (handler != null) {
            addClickHandler(handler);
        }
    }

    private Icon createIcon(String iconStyle) {
        Icon result = new Icon();
        result.setStyleName(iconStyle);
        add(result);
        return result;
    }

    public void setText(String text) {
        getElement().setInnerHTML(text);
    }

    public void setEnabled(boolean val) {
        super.setEnabled(val);
        if (!Utils.isIE()) {
            if (val) {
                removeStyleName("disabled");
                setTitle(null);
            } else {
                addStyleName("disabled");
                setTitle(wfmStrings.disabled());
            }
        }
    }

    public void removeHasiconLeftStyle() {
        removeStyleName("hasicon--left");
    }

}



