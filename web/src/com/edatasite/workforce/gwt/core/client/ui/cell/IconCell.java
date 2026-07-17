package com.edatasite.workforce.gwt.core.client.ui.cell;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * User: Abror Abdukadirov
 * Date: 10.01.2018 15:15
 */
public class IconCell extends SimpleLinkCell {

    private String styleName;

    public IconCell(String styleName) {
        super();
        this.styleName = styleName;
    }

    @Override
    public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<i " + (this.styleName != null ? "class= \"" + styleName + "\"" : "") + ">");
        if (data != null) {
            sb.append(data);
        }
        sb.appendHtmlConstant("</i>");
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onEnterKeyDown(context, parent, value, event, valueUpdater);
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}
