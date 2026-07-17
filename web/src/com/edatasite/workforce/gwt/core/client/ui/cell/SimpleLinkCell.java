package com.edatasite.workforce.gwt.core.client.ui.cell;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 11/14/11
 * Time: 8:06 PM
 */

import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

/**
 * A cell that renders a link and takes a delegate to perform actions on
 * mouseUp.
 */
public class SimpleLinkCell extends AbstractSafeHtmlCell<String> {

    private ClickHandler clickHandler;
    private String styleName;
    /**
     * Construct a new {@link SimpleLinkCell}.
     */
    public SimpleLinkCell() {
        this(SimpleSafeHtmlRenderer.getInstance());
    }

    /**
     * Construct a new {@link SimpleLinkCell}.
     *
     * @param renderer SafeHtmlRenderer<String>
     */
    public SimpleLinkCell(SafeHtmlRenderer<String> renderer) {
        super(renderer, "click", "keydown");
    }

    public void setClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value,
                               NativeEvent event, ValueUpdater<String> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
            if (clickHandler != null) {
                clickHandler.onClick(null); // cannot find ClickEvent
            }
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }
            if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
                // Ignore clicks that occur outside of the main element.
                onEnterKeyDown(context, parent, value, event, valueUpdater);
            }
        }
    }

    @Override
    public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<a ");
        if (this.getStyleName() != null) {
            sb.appendHtmlConstant(" class=\"" + this.getStyleName() + "\"");
        }
        sb.appendHtmlConstant(" href='javascript:;' tabindex=\"-1\">");
        if (data != null) {
            sb.append(data);
        }
        sb.appendHtmlConstant("</a>");
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, String value,
                                  NativeEvent event, ValueUpdater<String> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}