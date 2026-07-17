package com.edatasite.workforce.gwt.core.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public final class ClipboardUtil {
    private ClipboardUtil() {
    }

    public static boolean copy(String text) {
        TextArea ta = new TextArea();
        ta.setText(text);

        Element e = ta.getElement();
        Style s = e.getStyle();
        s.setPosition(Style.Position.FIXED);
        s.setLeft(-10000, Style.Unit.PX);
        s.setTop(0, Style.Unit.PX);
        s.setOpacity(0);

        RootPanel.get().add(ta);
        ta.setFocus(true);
        ta.selectAll();

        boolean ok = execCopy();
        RootPanel.get().remove(ta);
        return ok;
    }

    private static native boolean execCopy() /*-{
        try {
            return !!$doc.execCommand('copy');
        } catch (e) {
            return false;
        }
    }-*/;
}
