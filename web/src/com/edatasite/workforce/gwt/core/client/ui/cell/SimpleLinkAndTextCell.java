package com.edatasite.workforce.gwt.core.client.ui.cell;

/**
 * Created by Sherali Pirnafasov.
 * User: Sherali Pirnafasov
 * Email: sherali.pirnafasov@gmail.com
 * Date: 22/03/12
 * Time: 14:19 PM
 */

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * A cell that renders a link and takes a delegate to perform actions on
 * mouseUp.
 */
public class SimpleLinkAndTextCell extends AbstractCell<String[]> {

    interface Template extends SafeHtmlTemplates {
        @Template("<a href='javascript:;' tabindex=\"-1\">{0}</a>")
        SafeHtml link(String value);

        @Template("<br>{0}</br>")
        SafeHtml text(String value);

        @Template("<a href='javascript:;' class=\"{1}\" tabindex=\"-1\">{0}</a>")
        SafeHtml styleIconLink(String value, String icon);
    }

    private static Template template;

    /**
     * Construct a new {@link com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkAndTextCell}.
     */
    public SimpleLinkAndTextCell() {
        super("click", "keydown");
        if (template == null) {
            template = GWT.create(Template.class);
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, String value[],
                               NativeEvent event, ValueUpdater<String[]> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if ("click".equals(event.getType())) {
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
    public void render(Context context,final String[] data, SafeHtmlBuilder sb) {
        if (data != null) {
            if (data.length == 3) {
                sb.append(template.styleIconLink(data[0], data[2]));
            } else if (data.length == 4) {
                sb.append((SafeHtml) () -> "<a href='javascript:;'><span style='vertical-align: middle;'> <img src='" + data[1] + "'/></span>" + data[0] + "</a>");
            }
            else {
                if (data[0] != null && !"".equals(data[0]))
                    sb.append(template.link(data[0]));
                if (data[1] != null && !"".equals(data[1]))
                    sb.append(template.text(data[1]));
            }
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, String value[],
                                  NativeEvent event, ValueUpdater<String[]> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }
}