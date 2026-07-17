package com.edatasite.workforce.gwt.core.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * User: Aziz
 * Date: 11/16/12
 */
public class PanelUtils {

    public static void setXY(Element elem, int x, int y) {
        makePositionable(elem);
        int l = DOM.getIntStyleAttribute(elem, "left");
        x = x - elem.getAbsoluteLeft() + l;
        elem.getStyle().setProperty("left", x + "px");
        int t = DOM.getIntStyleAttribute(elem, "top");
        y = y - elem.getAbsoluteTop() + t;
        elem.getStyle().setProperty("top", y + "px");
    }

    public static void makePositionable(Element elem) {
        String position = elem.getStyle().getPosition();
        if (position.equals("") || position.equals("static")) {
            elem.getStyle().setProperty("position", "relative");
        }
    }

    public static void setBounds(Element elem, Rectangle rect) {
        setXY(elem, rect.x, rect.y);
        if (rect.height > 0) {
            elem.getStyle().setProperty("height", rect.height + "px");
        }
        if (rect.width > 0) {
            elem.getStyle().setProperty("width", rect.width + "px");
        }
    }

    public static Rectangle getBounds(Element elem) {
        int x = elem.getAbsoluteLeft();
        int y = elem.getAbsoluteTop();
        int width = elem.getPropertyInt("offsetWidth");
        int height = elem.getPropertyInt("offsetHeight");
        width = Math.max(0, width);
        height = Math.max(0, height);
        return new Rectangle(x, y, width, height);
    }

    public static void setVisibility(Element elem, boolean visible) {
        String value = visible ? "" : "hidden";
        elem.getStyle().setProperty("visibility", value);
    }

    public native static void setInnerHTML(Element elem, String html) /*-{
        if (!html) {
            html = '';
        }
        if ($wnd.escapeFlag === true) {
            html = $wnd.escapeHTML(html);
        }
        elem.innerHTML = html;
    }-*/;


    public static native Element getBody() /*-{
        return $doc.body;
    }-*/;

    public static void setLeft(Element elem, int left) {
        elem.getStyle().setProperty("left", left + "px");
    }

    public static void setTop(Element elem, int top) {
        elem.getStyle().setProperty("top", top + "px");
    }


}
